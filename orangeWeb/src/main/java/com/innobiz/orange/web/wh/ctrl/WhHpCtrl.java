package com.innobiz.orange.web.wh.ctrl;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wh.svc.WhAdmSvc;
import com.innobiz.orange.web.wh.svc.WhCmSvc;
import com.innobiz.orange.web.wh.svc.WhFileSvc;
import com.innobiz.orange.web.wh.svc.WhHpSvc;
import com.innobiz.orange.web.wh.vo.WhMdBVo;
import com.innobiz.orange.web.wh.vo.WhReqBVo;
import com.innobiz.orange.web.wh.vo.WhReqCmplDVo;
import com.innobiz.orange.web.wh.vo.WhReqEvalDVo;
import com.innobiz.orange.web.wh.vo.WhReqOngdDVo;
import com.innobiz.orange.web.wh.vo.WhReqOngdHVo;
import com.innobiz.orange.web.wh.vo.WhReqStatDVo;

@Controller
public class WhHpCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WhHpCtrl.class);
    
    /** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WhCmSvc whCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 파일 서비스 */
	@Resource(name = "whFileSvc")
	private WhFileSvc whFileSvc;
	
	/** 헬프데스크 서비스 */
	@Resource(name = "whHpSvc")
	private WhHpSvc whHpSvc;
	
	/** 헬프데스크 관리 서비스 */
	@Resource(name = "whAdmSvc")
	private WhAdmSvc whAdmSvc;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	/** 포털 보안 서비스 */
	@Resource(name = "ptSecuSvc")
	private PtSecuSvc ptSecuSvc;
	
	/** 첨부설정 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
//	/** 포탈 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
//	/** 캐쉬 만료 처리용 서비스 */
//	@Autowired
//	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 목록 - 요청, 접수, 처리 */
	@RequestMapping(value = {"/wh/help/listReq", "/wh/help/listRecv", "/wh/help/listHdl", "/wh/adm/help/listReq",
			"/wh/help/listReqPltFrm", "/wh/help/listRecvPltFrm", "/wh/help/listHdlPltFrm"})
	public String listRecv(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wh/adm/");
				
		// 요청경로 세팅
		String path = whCmSvc.getRequestPath(request, model , null);
		
		// 포틀릿 여부
		boolean isPlt = request.getRequestURI().indexOf("Plt")>0;
		
		// 프레임 여부
		boolean isFrm=!isPlt && request.getRequestURI().indexOf("Frm")>0;
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String compId = userVo.getCompId();
		// 테이블관리 기본(WH_REQ_B) 테이블 - BIND
		WhReqBVo whReqBVo = new WhReqBVo();
		VoUtil.bind(request, whReqBVo);
		whReqBVo.setQueryLang(langTypCd);
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		whAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, whReqBVo, false);
		
		whReqBVo.setWithLob(false); // LOB 데이터 조회여부
		whReqBVo.setWithDtl(true); // 상세 데이터 조회여부
				
		// 조회조건 세팅
		whHpSvc.setQueryUrlOptions(request, model, whReqBVo, path, userVo, langTypCd, isAdmin, null);
		
		// parameters
		String mdId = ParamUtil.getRequestParam(request, "mdId", false);
					
		// 시스템 모듈 세팅
		whHpSvc.setParamSysMdList(request, model, compId, langTypCd, mdId, "Y");
		
		if(mdId!=null){
			// 담당자 목록
			if(!isPlt)
				model.put("whMdPichLVoList", whHpSvc.getUsePichList(userVo.getCompId(), langTypCd, mdId, null));
			
			WhMdBVo whMdBVo = whHpSvc.getWhMdBVo(mdId, langTypCd);
			// 유형 목록 조회
			if(whMdBVo!=null){
				if(!isPlt)
					model.put("whCatGrpLVoList", whHpSvc.getCatGrpDtlList(userVo.getCompId(), langTypCd, whMdBVo.getCatGrpId()));
				if(!"W".equals(whMdBVo.getMdTypCd())){
					List<String> mdIdList=whHpSvc.getSubIdAllList(null, compId, mdId, langTypCd);
					if(mdIdList!=null && mdIdList.size()>0)
						whReqBVo.setMdIdList(mdIdList);
					whReqBVo.setMdId(null);
				}
			}
		}
				
		Integer recodeCount = commonSvc.count(whReqBVo);
		if(isPlt){
			String pageRowCnt = ParamUtil.getRequestParam(request, "pageRowCnt", false);
			int rowCnt = 0;
			if(pageRowCnt!=null && !pageRowCnt.isEmpty()){
				try{
					rowCnt = Integer.parseInt(pageRowCnt);
					model.addAttribute("pageRowCnt", pageRowCnt);
				} catch(Exception ignore){}
			}
			if(rowCnt==0){
				String hghtPx = ParamUtil.getRequestParam(request, "hghtPx", true);
				String colYn = ParamUtil.getRequestParam(request, "colYn", true);
				// 한 페이지 레코드수 - 높이에 의한 계산
				int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
				int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
				rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
				model.addAttribute("pageRowCnt", Integer.valueOf(rowCnt));
			}
			PersonalUtil.setFixedPaging(request, whReqBVo, rowCnt, recodeCount);
		}else {
			PersonalUtil.setPaging(request, whReqBVo, recodeCount);
		}
		
		@SuppressWarnings("unchecked")
		List<WhReqBVo> list = (List<WhReqBVo>) commonSvc.queryList(whReqBVo);
		
		whHpSvc.setWhReqBVoFileCnt(list); // 파일 건수 세팅
		
		// 환경설정 - model에 추가(파일사용여부, 결과평가사용여부)
		Map<String, String> configMap=whHpSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
		
		// 모듈명 표시
		if(configMap.get("mdNmDisp")!=null && "top".equals(configMap.get("mdNmDisp"))){
			whHpSvc.setTopMdNm(langTypCd, compId, list);
		}
				
		if(isPlt && list!=null && list.size()>0){
			Map<String, Object> listMap;
			List<Map<String, Object>> rsltMapList = new ArrayList<Map<String, Object>>();
			for(WhReqBVo storedWhReqBVo : list){
				listMap = VoUtil.toMap(storedWhReqBVo, null);
				rsltMapList.add(listMap);
			}
			model.put("rsltMapList", rsltMapList);
		}
		
		model.put("recodeCount", recodeCount);
		model.put("whReqBVoList", list);
		
		
		model.put("isAdmin", isAdmin); // 관리자여부
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		if(isPlt){//포틀릿
			return LayoutUtil.getJspPath("/wh/plt/listHelpPltFrm");
		}else if(isFrm){// 프레임
			model.put("pageSuffix", "Frm");
			return LayoutUtil.getJspPath("/wh/help/listHelp","Frm");
		}
		
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request, "menuId", "reqNo"));
		
		return LayoutUtil.getJspPath("/wh/help/listHelp");
	}
	
	/** [팝업] - 관련 요청글 조회 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/wh/help/setRelReqPop", "/wh/adm/help/setRelReqPop", "/wh/help/viewRelReqPop", "/wh/adm/help/viewRelReqPop"})
	public String setRelReqPop(HttpServletRequest request,
			@RequestParam(value = "reqNo", required = false) String reqNo,
			ModelMap model) throws Exception {
		
		// 요청경로
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wh/adm/");
		
		List<WhReqBVo> whReqBVoList = null;
		if(reqNo!=null && !reqNo.isEmpty()){
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 테이블관리 기본(WH_REQ_B) 테이블 - BIND
			WhReqBVo whReqBVo = new WhReqBVo();
			whReqBVo.setRelNo(reqNo);
			whReqBVo.setQueryLang(langTypCd);
			// 회사 ID 조회조건 추가[계열사 설정 확인]
			whAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, whReqBVo, false);
			
			whReqBVo.setWithLob(false); // LOB 데이터 조회여부
			whReqBVo.setWithDtl(true); // 상세 데이터 조회여부
			whReqBVo.setWithRel(true); // 관련요청 데이터 조회여부
			whReqBVo.setStatCd("C"); // 완료상태
			whReqBVo.setOrderBy("R.SORT_ORDR ASC");
			whReqBVoList = (List<WhReqBVo>) commonSvc.queryList(whReqBVo);
		}
		
		if(whReqBVoList==null){
			whReqBVoList=new ArrayList<WhReqBVo>();
		}
		whReqBVoList.add(new WhReqBVo());
		model.put("whReqBVoList", whReqBVoList);
		model.put("isAdmin", isAdmin); // 관리자여부
		
		if(path.startsWith("view"))
			model.put("isView", Boolean.TRUE);
		return LayoutUtil.getJspPath("/wh/help/setRelReqPop");
	}
	
	/** [팝업] - 요청 상세 조회 */
	@RequestMapping(value = {"/wh/help/viewReqPop", "/wh/adm/help/viewReqPop"})
	public String viewReqPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wh/help/viewReqPop");
	}
	
	/** [팝업] - 요청 조회 */
	@RequestMapping(value = {"/wh/help/listReqListPop", "/wh/adm/help/listReqListPop", "/wh/help/findReqListPop", "/wh/adm/help/findReqListPop"})
	public String listReqListPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 목록조회여부
		boolean isFind = request.getRequestURI().startsWith("/wh/help/find") || request.getRequestURI().startsWith("/wh/adm/help/find");
		model.put("isFind", isFind ? "Y" : "N");
		
		return LayoutUtil.getJspPath("/wh/help/listReqListPop");
	}
	
	
	/** [프레임] - 요청 조회 */
	@RequestMapping(value = {"/wh/help/listReqListFrm", "/wh/adm/help/listReqListFrm"})
	public String listReqListFrm(HttpServletRequest request,
			@RequestParam(value = "statCd", required = false) String statCd,
			@RequestParam(value = "page", required = false) String page,
			ModelMap model) throws Exception {
		
		if(statCd==null || statCd.isEmpty()) statCd="C";
		if("W".equals(statCd)) statCd=null;
		if(page==null || page.isEmpty()) page="req";
		// 관리자 페이지 여부
		//boolean isAdmin = request.getRequestURI().startsWith("/wh/adm/");
		boolean isAdmin = true;//request.getRequestURI().startsWith("/wh/adm/");
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId = userVo.getCompId();
		
		// 테이블관리 기본(WH_REQ_B) 테이블 - BIND
		WhReqBVo whReqBVo = new WhReqBVo();
		VoUtil.bind(request, whReqBVo);
		whReqBVo.setQueryLang(langTypCd);
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		whAdmSvc.setCompAffiliateIdList(compId, langTypCd, whReqBVo, false);
		whReqBVo.setWithLob(false); // LOB 데이터 조회여부
		whReqBVo.setWithDtl(true); // 상세 데이터 조회여부
		if(statCd==null){ // 요청, 접수, 반려, 처리중, 처리완료			
			whReqBVo.setStatCdList(new String[]{"R","A","G","P","C"});
			whReqBVo.setStatCd(null);
		}
		else whReqBVo.setStatCd(statCd); // 처리완료 상태
		
		// 조회조건 세팅
		whHpSvc.setQueryUrlOptions(request, model, whReqBVo, page, userVo, langTypCd, isAdmin, null);
		
		Integer recodeCount = commonSvc.count(whReqBVo);
		PersonalUtil.setPaging(request, whReqBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<WhReqBVo> whReqBVoList = (List<WhReqBVo>) commonSvc.queryList(whReqBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("whReqBVoList", whReqBVoList);
		
		model.put("isAdmin", isAdmin); // 관리자여부
		
		// parameters
		String mdId = ParamUtil.getRequestParam(request, "mdId", false);
					
		// 시스템 모듈 세팅
		whHpSvc.setParamSysMdList(request, model, compId, langTypCd, mdId, "Y");
		
		if(mdId!=null){
			// 담당자 목록
			model.put("whMdPichLVoList", whHpSvc.getUsePichList(userVo.getCompId(), langTypCd, mdId, null));
			
			WhMdBVo whMdBVo = whHpSvc.getWhMdBVo(mdId, langTypCd);
			// 유형 목록 조회
			if(whMdBVo!=null){
				model.put("whCatGrpLVoList", whHpSvc.getCatGrpDtlList(userVo.getCompId(), langTypCd, whMdBVo.getCatGrpId()));
				if(!"W".equals(whMdBVo.getMdTypCd())){
					List<String> mdIdList=whHpSvc.getSubIdAllList(null, compId, mdId, langTypCd);
					if(mdIdList!=null && mdIdList.size()>0)
						whReqBVo.setMdIdList(mdIdList);
					whReqBVo.setMdId(null);
				}
			}
		}
				
		
		return LayoutUtil.getJspPath("/wh/help/listReqListFrm");
	}
	
	
	/** 등록 수정 - 작성한 */
	@RequestMapping(value = {"/wh/help/setReq", "/wh/help/setRecv", "/wh/help/setHdl", "/wh/adm/help/setReq"})
	public String setReq(HttpServletRequest request,
			@RequestParam(value = "reqNo", required = false) String reqNo,
			ModelMap model) throws Exception {
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wh/adm/");
				
		// 요청경로 세팅
		String path = whCmSvc.getRequestPath(request, model , null);
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId = userVo.getCompId();
		
		WhReqBVo whReqBVo = new WhReqBVo();
		
		// 환경설정 - 첨부파일 사용여부
		Map<String, String> configMap=whHpSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
				
		String mdId=null;
		boolean isEditor = false;
		// 수정인 경우
		if (reqNo != null && !reqNo.isEmpty()) {
			whReqBVo.setQueryLang(langTypCd);
			// 회사 ID 조회조건 추가[계열사 설정 확인]
			whAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, whReqBVo, false);
			
			whReqBVo.setReqNo(reqNo);
			whReqBVo=(WhReqBVo)commonSvc.queryVo(whReqBVo);
			if(whReqBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			WhReqOngdDVo whReqOngdDVo = whHpSvc.getReqOngdDVo(reqNo);
			if(whReqOngdDVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			// 권한체크
			whHpSvc.chkUrlAuth(request, path, userVo, whReqBVo, whReqOngdDVo, configMap, isAdmin);
			// 사용자 정보맵
			Map<String, Object> userMap = orCmSvc.getUserMap(whReqBVo.getRegrUid(), langTypCd);
			if(userMap!=null && userMap.containsKey("deptRescNm"))
				whReqBVo.setRegrDeptNm((String)userMap.get("deptRescNm"));
			model.put("whReqOngdDVo", whReqOngdDVo);
			mdId=whReqOngdDVo.getMdId();
			
			// 담당자 목록 조회
			if(whReqOngdDVo.getMdId()!=null)
				model.put("whMdPichLVoList", whHpSvc.getUsePichList(userVo.getCompId(), langTypCd, whReqOngdDVo.getMdId(), null));
			
			WhReqStatDVo whReqStatDVo = whHpSvc.getWhReqStatDVo(reqNo, "T".equals(whReqOngdDVo.getStatCd()) || "G".equals(whReqOngdDVo.getStatCd())? "R" : whReqOngdDVo.getStatCd());
			if(whReqStatDVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			isEditor=whReqStatDVo.getHtmlYn()!=null && "Y".equals(whReqStatDVo.getHtmlYn());
			
		}else{
			whReqBVo.setRegrNm(userVo.getUserNm());
			whReqBVo.setRegrDeptNm(userVo.getDeptNm());
			if(configMap.containsKey("dueDt"))
				whReqBVo.setCmplPdt(whHpSvc.getDateToAdd(null, Integer.parseInt(configMap.get("dueDt")), false));
			
			isEditor=configMap.containsKey("reqEditorYn") && "Y".equals(configMap.get("reqEditorYn"));
		}
		
		model.put("whReqBVo", whReqBVo);
		
		// 시스템 모듈 세팅
		whHpSvc.setParamSysMdList(request, model, compId, langTypCd, mdId, "Y");
		
		// 첨부파일 리스트 model에 추가
		if(configMap.containsKey("fileYn") && "Y".equals(configMap.get("fileYn"))){
			whFileSvc.putFileListToModel(reqNo, model, userVo.getCompId(), "R");
		}
		
		if(isEditor) // 에디터 사용
			model.addAttribute("JS_OPTS", new String[]{"editor"});
		model.put("isEditor", isEditor); // 에디터 사용여부
				
		// 최대 본문 사이즈 model에 추가
		whHpSvc.putBodySizeToModel(request, model);
		
		model.put("isAdmin", isAdmin); // 관리자여부
		
		return LayoutUtil.getJspPath("/wh/help/setHelp");
	}
	
	/** 상세보기 */
	@RequestMapping(value = {"/wh/help/viewReq", "/wh/help/viewRecv", "/wh/help/viewHdl", "/wh/adm/help/viewReq", "/wh/help/viewReqFrm", "/wh/adm/help/viewReqFrm"})
	public String viewReq(HttpServletRequest request,
			@RequestParam(value = "reqNo", required = false) String reqNo,
			ModelMap model) throws Exception {
		
		if (reqNo.isEmpty() || reqNo.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 프레임 여부
		boolean isFrm=request.getRequestURI().indexOf("Frm")>0;
				
		// 요청경로 세팅
		String path = whCmSvc.getRequestPath(request, model , null);
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wh/adm/") || isFrm;
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId = userVo.getCompId();
		
		// html 여부
		WhReqStatDVo whReqStatDVo = whHpSvc.getWhReqStatDVo(reqNo, "R");
		if(whReqStatDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		String reqEditorYn = whReqStatDVo.getHtmlYn();
		model.put("reqEditorYn", reqEditorYn);
		
		WhReqBVo whReqBVo = new WhReqBVo();
		whReqBVo.setQueryLang(langTypCd);
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		whAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, whReqBVo, false);
		
		whReqBVo.setReqNo(reqNo);
		if(reqEditorYn!=null && "Y".equals(reqEditorYn))
			whReqBVo.setWithLob(false); // LOB 데이터 조회여부
		
		whReqBVo=(WhReqBVo)commonSvc.queryVo(whReqBVo);
		
		if (whReqBVo == null) {
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		WhReqOngdDVo whReqOngdDVo = whHpSvc.getReqOngdDVo(reqNo);
		if (whReqOngdDVo == null) {
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		// 환경설정 - model에 추가(파일사용여부, 결과평가사용여부)
		Map<String, String> configMap=whHpSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
				
		// 권한체크
		whHpSvc.chkUrlAuth(request, path, userVo, whReqBVo, whReqOngdDVo, configMap, isAdmin);
		
		if(configMap.containsKey("fileYn") && "Y".equals(configMap.get("fileYn")))
			whReqBVo.setFileCnt(whFileSvc.getFileVoListCnt(reqNo, "R")); // 파일 건수 조회
		if(reqEditorYn!=null && "Y".equals(reqEditorYn)){//lobHandler
			model.put("reqLobHandler", lobHandler.create(
					"SELECT CONT FROM WH_REQ_B WHERE REQ_NO = ?", 
					new String[]{reqNo}
			));
		}
		
		// 사용자 정보맵
		Map<String, Object> userMap = orCmSvc.getUserMap(whReqBVo.getRegrUid(), langTypCd);
		if(userMap!=null && userMap.containsKey("deptRescNm"))
			whReqBVo.setRegrDeptNm((String)userMap.get("deptRescNm"));
					
		model.put("whReqBVo", whReqBVo);
		model.put("whReqOngdDVo", whReqOngdDVo);
		// 완료상태
		if("C".equals(whReqOngdDVo.getStatCd())){
			whReqStatDVo = whHpSvc.getWhReqStatDVo(reqNo, "C");
			if(whReqStatDVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			String cmplEditorYn = whReqStatDVo.getHtmlYn();
			model.put("cmplEditorYn", cmplEditorYn);
			
			WhReqCmplDVo whReqCmplDVo = whHpSvc.getReqCmplDVo(reqNo, "N".equals(cmplEditorYn));
			if (whReqCmplDVo == null) {
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			if(configMap.containsKey("fileYn") && "Y".equals(configMap.get("fileYn")))
				whReqCmplDVo.setFileCnt(whFileSvc.getFileVoListCnt(reqNo, "C")); // 파일 건수 조회
			model.put("whReqCmplDVo", whReqCmplDVo);
			
			
			if(cmplEditorYn!=null && "Y".equals(cmplEditorYn)){//lobHandler
				model.put("cmplLobHandler", lobHandler.create(
						"SELECT HDL_CONT FROM WH_REQ_CMPL_D WHERE REQ_NO = ?", 
						new String[]{reqNo}
				));
			}
			
			// 결과평가 조회
			if(whReqOngdDVo.getEvalYn()!=null && "Y".equals(whReqOngdDVo.getEvalYn()))
				model.put("whReqEvalDVo", whHpSvc.getReqEvalDVo(reqNo));
			
			// 이력조회
			WhReqCmplDVo hstVo = new WhReqCmplDVo();
			hstVo.setHst(true);
			hstVo.setReqNo(reqNo);
			if(commonSvc.count(hstVo)>0)
				model.put("hstList","Y");
		}
		
		if("R".equals(whReqOngdDVo.getStatCd()) || "P".equals(whReqOngdDVo.getStatCd()) || "C".equals(whReqOngdDVo.getStatCd())){
			// 완료처리 페이지
			boolean isCmplHdlPopup = configMap.containsKey("cmplHdlDisp") && "popup".equals(configMap.get("cmplHdlDisp"));
			if(isCmplHdlPopup && configMap.containsKey("cmplEditorYn") && "Y".equals(configMap.get("cmplEditorYn")))						
				model.addAttribute("JS_OPTS", new String[]{"editor"}); // 에디터 사용
		}
		
		if("P".equals(whReqOngdDVo.getStatCd()) || "C".equals(whReqOngdDVo.getStatCd())){
			// 진행처리 이력조회
			WhReqOngdHVo whReqOngdHVo = new WhReqOngdHVo();
			whReqOngdHVo.setReqNo(reqNo);
			if(commonSvc.count(whReqOngdHVo)>0)
				model.put("isOngdHst", "Y");
		}
		
		// 시스템 모듈 세팅
		whHpSvc.setParamSysMdList(request, model, compId, langTypCd, whReqOngdDVo.getMdId(), null);
				
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		model.put("isAdmin", isAdmin);
		
		if(isFrm){// 프레임
			model.put("pageSuffix", "Frm");
			return LayoutUtil.getJspPath("/wh/help/viewHelp","Frm");
		}
		
		// 관련요청 등록여부
		model.put("isReqRel", whHpSvc.isReqRel(reqNo));
		
		return LayoutUtil.getJspPath("/wh/help/viewHelp");
	}
	
	/** [팝업] - 모듈 조회 */
	@RequestMapping(value = {"/wh/help/findSysMdPop", "/wh/adm/help/findSysMdPop"})
	public String findSysMdPop(HttpServletRequest request,
			@Parameter(name="mdId", required=false) String mdId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId = userVo.getCompId();
		whHpSvc.setParamSysMdList(request, model, compId, langTypCd, null, "Y");
		
		return LayoutUtil.getJspPath("/wh/help/findSysMdPop");
	}
	
	/** 저장 */
	@RequestMapping(value = {"/wh/help/transReq", "/wh/adm/help/transReq"}, method = RequestMethod.POST)
	public String transReq(HttpServletRequest request,
			ModelMap model) {
		
		UploadHandler uploadHandler = null;
		try {
			
			// Multipart 파일 업로드
			uploadHandler = whFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 요청경로 세팅
			String path = whCmSvc.getRequestPath(request, model , null);
			path=path.toLowerCase();
			
			// parameters
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			String reqNo = ParamUtil.getRequestParam(request, "reqNo", false);
			
			if (listPage.isEmpty() || viewPage.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			QueryQueue queryQueue = new QueryQueue();
			
			// 저장
			String refId=whHpSvc.saveHelp(request, queryQueue, reqNo);
			
			// 파일 저장
			List<CommonFileVo> deletedFileList = whFileSvc.saveHpFile(request, refId, queryQueue);
			
			commonSvc.execute(queryQueue);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 담당자UID
			String pichUid = ParamUtil.getRequestParam(request, "pichUid", false);
			
			// 환경설정 로드
			Map<String, String> configMap=whHpSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
						
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			if((reqNo==null || reqNo.isEmpty()) && (pichUid!=null && !pichUid.isEmpty()) 
					&& sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable")) 
					&& configMap.get("mailSendPich")!=null && !"none".equals(configMap.get("mailSendPich"))){
				// 세션의 언어코드
				String langTypCd = LoginSession.getLangTypCd(request);
				
				WhReqBVo whReqBVo = new WhReqBVo();
				whReqBVo.setReqNo(refId);
				whReqBVo.setQueryLang(langTypCd);
				// 회사 ID 조회조건 추가[계열사 설정 확인]
				whAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, whReqBVo, false);
				
				whReqBVo.setWithLob(true); // LOB 데이터 조회여부
				whReqBVo.setWithDtl(true); // 상세 데이터 조회여부
				whReqBVo.setWithCmpl(false);
				
				whReqBVo=(WhReqBVo)commonSvc.queryVo(whReqBVo);
				if(whReqBVo!=null){
					// 사용자 정보맵
					Map<String, Object> userMap = orCmSvc.getUserMap(pichUid, langTypCd);
					// html 여부
					WhReqStatDVo whReqStatDVo = whHpSvc.getWhReqStatDVo(refId, "R");
					if(whReqStatDVo!=null && userMap!=null){
						String toMail=null;
						String mailSendPich=configMap.get("mailSendPich"); // 메일발송 대상
						if("in".equals(mailSendPich)){ // 내부메일 - 사용자 메일사용여부 체크
							// 원직자 보안 정보
							Map<String, String> odurSecuMap = orCmSvc.getOdurSecuMap((String)userMap.get("odurUid"));
							if(odurSecuMap==null || odurSecuMap.get("useMailYn")==null || (odurSecuMap.get("useMailYn")!=null && !"N".equals(odurSecuMap.get("useMailYn"))))
								toMail=(String)userMap.get("email");
						}else if("out".equals(mailSendPich)) // 외부메일
							toMail=(String)userMap.get("extnEmail");
						
						if(toMail!=null && !toMail.isEmpty()){							
							String[] to_list = new String[1];
							to_list[0]=toMail;
							//이메일 발송 wh.msg.req.reg=요청사항이 등록되었습니다.
							String subj = "["+messageProperties.getMessage("wh.cols.helpdesk", request) + "] - "+messageProperties.getMessage("wh.msg.req.reg", request);
							String content = whHpSvc.getReqMailHTML(request, whReqBVo, whReqStatDVo.getHtmlYn(), null);
							
							// 파일 목록
							String[][] fileList=emailSvc.getFileToArrayList(whFileSvc.getFileVoList(refId, "R"));
							
							emailSvc.sendMailSvc2(userVo.getUserUid(), to_list, subj, content, fileList, true, true, langTypCd);
						}
					}
				}
			}
			
			
			// 파일 삭제
			whFileSvc.deleteDiskFiles(deletedFileList);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
	        
			if(reqNo!=null && !reqNo.isEmpty()){
				model.put("todo", "parent.location.replace('" + viewPage + "');");
			}else{
				model.put("todo", "parent.location.replace('" + listPage + "');");
			}

		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		} finally {
			if (uploadHandler != null) try { uploadHandler.removeTempDir(); } catch (CmException ignored) {}
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] - 요청 삭제 */
	@RequestMapping(value = {"/wh/help/transReqDelAjx", "/wh/adm/help/transReqDelAjx"})
	public String transRecvDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String reqNo = (String) object.get("reqNo");
			JSONArray reqNoArray = (JSONArray) object.get("reqNos");
			if (reqNo == null && reqNoArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 관리자 페이지 여부
			boolean isAdmin = request.getRequestURI().startsWith("/wh/adm/");
			
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 삭제할 파일 목록
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			
			// 단건 삭제
			if(reqNo!=null && !reqNo.isEmpty())
				whHpSvc.deleteReq(request, queryQueue, userVo, reqNo, deletedFileList, isAdmin);
			
			// 복수 삭제
			if(reqNoArray!=null && !reqNoArray.isEmpty()){
				for(int i=0;i<reqNoArray.size();i++){
					reqNo = (String)reqNoArray.get(i);
					whHpSvc.deleteReq(request, queryQueue, userVo, reqNo, deletedFileList, isAdmin);
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// 실제 파일 삭제
			whFileSvc.deleteDiskFiles(deletedFileList);
						
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - 시스템 모듈 목록 불러오기 */
	@RequestMapping(value = {"/wh/help/getSysMdListAjx", "/wh/adm/help/getSysMdListAjx"})
	public String getSysMdListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mdPid = (String) object.get("mdPid");
			if (mdPid == null && mdPid == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			Map<String, Object> paramMap = JsonUtil.jsonToMap(object);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String compId=userVo.getCompId();
			WhMdBVo whMdBVo=whHpSvc.getWhMdBVo(mdPid, langTypCd);
			if(whMdBVo==null || whMdBVo.getMdTypCd()==null || !"F".equals(whMdBVo.getMdTypCd())){
				model.put("result", "end");
				return JsonUtil.returnJson(model);
			}
			
			// 하위 모듈 조회
			whMdBVo = new WhMdBVo();
			// jsonMap => Vo
			whCmSvc.setParamToVo(paramMap, whMdBVo);
			whMdBVo.setUseYn("Y"); // 사용중인 것만 조회
			model.put("whMdBVoList", whHpSvc.getSysMdList(whMdBVo, compId, langTypCd, null, null));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - 시스템 모듈 담당자 목록 불러오기 */
	@RequestMapping(value = {"/wh/help/getMdPichListAjx", "/wh/adm/help/getMdPichListAjx"})
	public String getMdPichListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mdId = (String) object.get("mdId");
			if (mdId == null || mdId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			WhMdBVo whMdBVo = whHpSvc.getWhMdBVo(mdId, langTypCd);
			// 담당자 목록 조회
			if(whMdBVo!=null && "W".equals(whMdBVo.getMdTypCd()))			
				model.put("whMdPichLVoList", whHpSvc.getUsePichList(whMdBVo.getCompId(), langTypCd, mdId, null));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - 시스템 모듈 처리유형 목록 불러오기 */
	@RequestMapping(value = {"/wh/help/getMdCatListAjx", "/wh/adm/help/getMdCatListAjx"})
	public String getMdCatListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mdId = (String) object.get("mdId");
			if (mdId == null && mdId == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			WhMdBVo whMdBVo = whHpSvc.getWhMdBVo(mdId, langTypCd);
			// 유형 목록 조회
			if(whMdBVo!=null)
				model.put("whCatGrpLVoList", whHpSvc.getCatGrpDtlList(whMdBVo.getCompId(), langTypCd, whMdBVo.getCatGrpId()));
			
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [팝업] - 접수 | 반려 */
	@RequestMapping(value = {"/wh/help/setReqHdl", "/wh/help/setReqHdlPop", "/wh/help/listReqHdlPop", 
			"/wh/adm/help/setReqHdl", "/wh/adm/help/setReqHdlPop", "/wh/adm/help/listReqHdlPop"})
	public String setRecvPop(HttpServletRequest request,
			@Parameter(name="reqNo", required=false) String reqNo,
			@Parameter(name="reqNos", required=false) String reqNos,
			@Parameter(name="ongoCd", required=false) String ongoCd,
			@Parameter(name="mdId", required=false) String mdId,
			ModelMap model) throws Exception {
		
		// 팝업여부
		boolean isPop = request.getRequestURI().indexOf("Pop")>0;
		
		// 목록여부
		boolean isList=request.getRequestURI().startsWith("/wh/help/list");
		if((!isList && reqNo==null) || (isList && (reqNos==null || mdId==null))){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wh/adm/");
				
		if(ongoCd!=null){
			boolean isEditor = false;
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			String compId=userVo.getCompId();
			
			// 환경설정 - 첨부파일 사용여부
			Map<String, String> configMap=whHpSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
			
			boolean isSetMdList=false;
			if(!isList){
				// 진행상세 조회
				WhReqOngdDVo whReqOngdDVo = whHpSvc.getReqOngdDVo(reqNo);
				if(whReqOngdDVo==null || (whReqOngdDVo!=null && "G".equals(whReqOngdDVo.getStatCd()))){
					// cm.msg.noData=해당하는 데이터가 없습니다.
					throw new CmException("cm.msg.noData", request);
				}
				mdId=whReqOngdDVo.getMdId();
				
				if("C".equals(ongoCd)){
					if("R".equals(whReqOngdDVo.getStatCd())){
						isSetMdList=true;
						model.put("isDirectCmpl", isSetMdList);						
					}
					if(!"C".equals(whReqOngdDVo.getStatCd()))
						model.put("prevStatCd", whReqOngdDVo.getStatCd());
					if("C".equals(whReqOngdDVo.getStatCd())){
						if(!configMap.containsKey("cmplModYn") || "N".equals(configMap.get("cmplModYn"))){
							// wh.jsp.not.cmpl.mod='완료'된 업무를 수정할 수 없습니다.
							throw new CmException("wh.jsp.not.cmpl.mod", request);
						}
						
						WhReqCmplDVo whReqCmplDVo = whHpSvc.getReqCmplDVo(reqNo);
						if (whReqCmplDVo == null) {
							// cm.msg.noData=해당하는 데이터가 없습니다.
							throw new CmException("cm.msg.noData", request);
						}
						model.put("whReqCmplDVo", whReqCmplDVo);
						model.put("hstYn", "Y"); // 이력여부
					}
					
					isEditor=configMap.containsKey("cmplEditorYn") && "Y".equals(configMap.get("cmplEditorYn"));
				}
				if("P".equals(ongoCd) || "C".equals(ongoCd)){
					
					// 권한체크
					whHpSvc.chkUrlAuth(request, "hdl", userVo, null, whReqOngdDVo, configMap, isAdmin);
					
					if("C".equals(ongoCd)){ // 첨부파일 리스트 model에 추가
						if(configMap.containsKey("fileYn") && "Y".equals(configMap.get("fileYn"))){
							model.put("fileYn", configMap.get("fileYn"));
							whFileSvc.putFileListToModel(reqNo, model, userVo.getCompId(), "C");
						}
					}
					
					if("P".equals(ongoCd) && configMap.containsKey("dueDt"))
						whReqOngdDVo.setCmplDueDt(whHpSvc.getDateToAdd(whReqOngdDVo.getRecvDt(), Integer.parseInt(configMap.get("dueDt")), false));
				}
				model.put("whReqOngdDVo", whReqOngdDVo);
			}
			
			if("A".equals(ongoCd) || isSetMdList)
				whHpSvc.setParamSysMdList(request, model, compId, langTypCd, mdId, "Y"); // 시스템 모듈 세팅
			
			// 담당자 목록 조회
			if(mdId!=null){
				WhMdBVo whMdBVo = whHpSvc.getWhMdBVo(mdId, langTypCd);
				if(whMdBVo!=null && "W".equals(whMdBVo.getMdTypCd())){
					model.put("whMdPichLVoList", whHpSvc.getUsePichList(userVo.getCompId(), langTypCd, mdId, null));
					if("A".equals(ongoCd) || "C".equals(ongoCd))// 유형 목록 조회
						model.put("whCatGrpLVoList", whHpSvc.getCatGrpDtlList(userVo.getCompId(), langTypCd, whMdBVo.getCatGrpId()));
				}
			}
			
			model.put("isEditor", isEditor); // 에디터 사용여부
			
			if(isPop){
				model.put("pageTyp", "popup");
				return LayoutUtil.getJspPath("/wh/help/setReqHdl", "Pop");
			}
			
			if("C".equals(ongoCd)){
				// 완료처리 페이지
				boolean isCmplHdlPopup = configMap.containsKey("cmplHdlDisp") && "popup".equals(configMap.get("cmplHdlDisp"));
				if(!isCmplHdlPopup && isEditor)
					model.addAttribute("JS_OPTS", new String[]{"editor"}); // 에디터 사용
			}
		}
		
		model.put("isAdmin", isAdmin);
		
		return LayoutUtil.getJspPath("/wh/help/setReqHdl");
	}
	
	/** [팝업] - 완료처리 수정 */
	@RequestMapping(value = {"/wh/help/setCmplHdl", "/wh/help/setCmplHdlPop", "/wh/adm/help/setCmplHdl", "/wh/adm/help/setCmplHdlPop"})
	public String setCmplHdl(HttpServletRequest request,
			@Parameter(name="reqNo", required=false) String reqNo,
			ModelMap model) throws Exception {
		
		// 팝업여부
		boolean isPop = request.getRequestURI().indexOf("Pop")>0;
		
		// 요청번호 검증
		if(reqNo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wh/adm/");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 진행상세 조회
		WhReqOngdDVo whReqOngdDVo = whHpSvc.getReqOngdDVo(reqNo);
		if(whReqOngdDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		model.put("whReqOngdDVo", whReqOngdDVo);
		
		if(whReqOngdDVo.getMdId()!=null){
			// 담당자 목록
			model.put("whMdPichLVoList", whHpSvc.getUsePichList(userVo.getCompId(), langTypCd, whReqOngdDVo.getMdId(), null));
			
			WhMdBVo whMdBVo = whHpSvc.getWhMdBVo(whReqOngdDVo.getMdId(), langTypCd);
			// 유형 목록 조회
			if(whMdBVo!=null)
				model.put("whCatGrpLVoList", whHpSvc.getCatGrpDtlList(userVo.getCompId(), langTypCd, whMdBVo.getCatGrpId()));
		}
		
		WhReqCmplDVo whReqCmplDVo = whHpSvc.getReqCmplDVo(reqNo);
		if (whReqCmplDVo == null) {
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		model.put("whReqCmplDVo", whReqCmplDVo);
		model.put("hstYn", "Y"); // 이력여부
		
		// 환경설정 - 첨부파일 사용여부
		Map<String, String> configMap=whHpSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
		
		if(configMap.containsKey("fileYn") && "Y".equals(configMap.get("fileYn"))){
			model.put("fileYn", configMap.get("fileYn"));
			whFileSvc.putFileListToModel(reqNo, model, userVo.getCompId(), "C");
		}
		
		WhReqStatDVo whReqStatDVo = whHpSvc.getWhReqStatDVo(reqNo, "C");
		if(whReqStatDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		boolean isEditor=whReqStatDVo.getHtmlYn()!=null && "Y".equals(whReqStatDVo.getHtmlYn());
		model.put("isEditor", isEditor);
		// 완료처리 페이지
		boolean isCmplHdlPopup = configMap.containsKey("cmplHdlDisp") && "popup".equals(configMap.get("cmplHdlDisp"));
		if(!isCmplHdlPopup && isEditor)
			model.addAttribute("JS_OPTS", new String[]{"editor"}); // 에디터 사용
		
		model.put("isAdmin", isAdmin);
		
		if(isPop){
			model.put("pageTyp", "popup");
			return LayoutUtil.getJspPath("/wh/help/setReqHdl", "Pop");
		}
		
		return LayoutUtil.getJspPath("/wh/help/setReqHdl");
	}
	
	/** [AJAX] - 접수 | 반려 | 진행중 처리 */
	@RequestMapping(value = {"/wh/help/transRecvAjx", "/wh/adm/help/transRecvAjx"})
	public String transRecvAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String reqNo = (String) object.get("reqNo"); // 요청번호
			String reqNos = (String) object.get("reqNos"); // 요청번호 콤마 구분
			//JSONArray reqNoArray = (JSONArray) object.get("reqNos");
			
			String statCd = (String) object.get("statCd"); // 상태코드
			if (((reqNo==null || reqNo.isEmpty()) && (reqNos==null || reqNos.isEmpty())) || statCd==null || statCd.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			if(!ArrayUtil.isInArray(new String[]{"A", "G", "P"}, statCd)){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			Map<String,Object> paramMap = JsonUtil.jsonToMap(object);
			QueryQueue queryQueue = new QueryQueue();
			
			if(reqNos!=null){ // 일괄 저장
				String[] reqNoList=reqNos.split(",");
				if(reqNoList!=null){
					for(int i=0;i<reqNoList.length;i++){
						reqNo = (String)reqNoList[i];
						whHpSvc.saveProgress(request, queryQueue, paramMap, reqNo, statCd);
					}
				}
			}else{
				whHpSvc.saveProgress(request, queryQueue, paramMap, reqNo, statCd);
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));			
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 완료 처리 */
	@RequestMapping(value = {"/wh/help/transHdl", "/wh/adm/help/transHdl"}, method = RequestMethod.POST)
	public String transHdl(HttpServletRequest request,
			ModelMap model) {
		
		UploadHandler uploadHandler = null;
		try {
			
			// Multipart 파일 업로드
			uploadHandler = whFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// parameters
			String reqNo = ParamUtil.getRequestParam(request, "reqNo", false);
			String statCd = ParamUtil.getRequestParam(request, "statCd", false);
			
			if (reqNo==null || reqNo.isEmpty() || statCd==null || statCd.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 이력여부
			String hstYn = ParamUtil.getRequestParam(request, "hstYn", false);
			
			// 상태코드 검증
			whHpSvc.chkSaveStatCd(request, reqNo, statCd);
			
			QueryQueue queryQueue = new QueryQueue();
			
			WhReqOngdDVo whReqOngdDVo = null;
			WhReqCmplDVo whReqCmplDVo = null;
			
			// 파일복사 목록			
			List<DmCommFileDVo> copyFileList = null;
			
			// 이력여부
			boolean isHst=hstYn!=null && "Y".equals(hstYn);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 환경설정 로드
			Map<String, String> configMap=whHpSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
			
			if(isHst){
				
				if(configMap.containsKey("cmplHstYn") && configMap.get("cmplHstYn")!=null && "Y".equals(configMap.get("cmplHstYn"))){
					// 진행상세 조회
					whReqOngdDVo = whHpSvc.getReqOngdDVo(reqNo);
					if(whReqOngdDVo==null){
						// cm.msg.noData=해당하는 데이터가 없습니다.
						throw new CmException("cm.msg.noData", request);
					}	
					
					whReqCmplDVo = whHpSvc.getReqCmplDVo(reqNo);
					if (whReqCmplDVo == null) {
						// cm.msg.noData=해당하는 데이터가 없습니다.
						throw new CmException("cm.msg.noData", request);
					}
					
					WhReqCmplDVo targetVo = new WhReqCmplDVo();
					BeanUtils.copyProperties(whReqCmplDVo, targetVo);
					targetVo.setHst(true); // 이력여부
					targetVo.setHstNo(whCmSvc.createNo("WH_REQ_CMPL_H").toString()); // 이력번호
					targetVo.setCmplDt(whReqOngdDVo.getCmplDt());
					targetVo.setRegrUid(userVo.getUserUid());
					targetVo.setRegDt("sysdate");
					queryQueue.insert(targetVo);
					
					copyFileList = new ArrayList<DmCommFileDVo>();
					
					//파일복사
					whFileSvc.copyFile(request, reqNo, targetVo.getHstNo(), queryQueue, "C", "H", copyFileList);
				}
			}else{
				whHpSvc.saveHtmlYn(request, queryQueue, reqNo, "C", null); // html여부 저장
			}
			
			whReqOngdDVo = new WhReqOngdDVo();
			VoUtil.bind(request, whReqOngdDVo);
			whHpSvc.saveProgress(request, queryQueue, null, whReqOngdDVo);
			
			whReqCmplDVo = new WhReqCmplDVo();
			VoUtil.bind(request, whReqCmplDVo);
			whHpSvc.saveComplete(request, queryQueue, null, whReqCmplDVo);
			
			// 파일 저장
			List<CommonFileVo> deletedFileList = whFileSvc.saveHpFile(request, reqNo, queryQueue);
			
			commonSvc.execute(queryQueue);
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			// 메일발송여부
			String mailSendYn = ParamUtil.getRequestParam(request, "mailSendYn", false);
			if(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable")) 
					&& configMap.get("mailSendTgt")!=null && !"none".equals(configMap.get("mailSendTgt")) 
					&& mailSendYn!=null && "Y".equals(mailSendYn)){
				// 세션의 언어코드
				String langTypCd = LoginSession.getLangTypCd(request);
				
				WhReqBVo whReqBVo = new WhReqBVo();
				whReqBVo.setReqNo(reqNo);
				whReqBVo.setQueryLang(langTypCd);
				// 회사 ID 조회조건 추가[계열사 설정 확인]
				whAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, whReqBVo, false);
				
				whReqBVo.setWithLob(false); // LOB 데이터 조회여부
				whReqBVo.setWithDtl(true); // 상세 데이터 조회여부
				whReqBVo.setWithCmpl(true);
				
				whReqBVo=(WhReqBVo)commonSvc.queryVo(whReqBVo);
				if(whReqBVo!=null){
					// 사용자 정보맵
					Map<String, Object> userMap = orCmSvc.getUserMap(whReqBVo.getRegrUid(), langTypCd);
					// html 여부
					WhReqStatDVo whReqStatDVo = whHpSvc.getWhReqStatDVo(reqNo, "C");
					if(whReqStatDVo!=null && userMap!=null){
						String toMail=null;
						String mailSendTgt=configMap.get("mailSendTgt"); // 메일발송 대상
						if("in".equals(mailSendTgt)){ // 내부메일 - 사용자 메일사용여부 체크
							// 원직자 보안 정보
							Map<String, String> odurSecuMap = orCmSvc.getOdurSecuMap((String)userMap.get("odurUid"));
							if(odurSecuMap==null || odurSecuMap.get("useMailYn")==null || (odurSecuMap.get("useMailYn")!=null && !"N".equals(odurSecuMap.get("useMailYn"))))
								toMail=(String)userMap.get("email");
						}else if("out".equals(mailSendTgt)) // 외부메일
							toMail=(String)userMap.get("extnEmail");
						
						if(toMail!=null && !toMail.isEmpty()){							
							String[] to_list = new String[1];
							to_list[0]=toMail;
							//이메일 발송
							String subj = "["+messageProperties.getMessage("wh.cols.helpdesk", request) + "] - "+messageProperties.getMessage("wh.msg.req.cmpl", request);
							String content = whHpSvc.getCmplMailHTML(request, whReqBVo, whReqStatDVo.getHtmlYn(), null);
							
							// 파일 목록
							String[][] fileList=emailSvc.getFileToArrayList(whFileSvc.getFileVoList(reqNo, "C"));
							
							emailSvc.sendMailSvc2(userVo.getUserUid(), to_list, subj, content, fileList, true, true, langTypCd);
						}
					}
				}
			}
			
			// 파일 복사
			if(copyFileList!=null && copyFileList.size()>0){
				whFileSvc.copyFileList(request, "/wh", copyFileList);
			}
						
			// 파일 삭제
			whFileSvc.deleteDiskFiles(deletedFileList);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reloadHdl(false);");

		} catch (CmException e) {
			//LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		} finally {
			if (uploadHandler != null) try { uploadHandler.removeTempDir(); } catch (CmException ignored) {}
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] - 진행처리 이력 조회 */
	@RequestMapping(value = {"/wh/help/listOngdHstPop", "/wh/adm/help/listOngdHstPop"})
	public String listOngdHstPop(HttpServletRequest request,
			@Parameter(name="reqNo", required=false) String reqNo,
			ModelMap model) throws Exception {
		
		if (reqNo==null || reqNo.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		WhReqOngdDVo whReqOngdDVo = whHpSvc.getReqOngdDVo(reqNo);
		if (whReqOngdDVo == null) {
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		WhReqOngdHVo whReqOngdHVo = new WhReqOngdHVo();
		whReqOngdHVo.setQueryLang(langTypCd);
		whReqOngdHVo.setReqNo(reqNo);
		whReqOngdHVo.setOrderBy(" T.REG_DT DESC");
		
		@SuppressWarnings("unchecked")
		List<WhReqOngdHVo> whReqOngdHVoList = (List<WhReqOngdHVo>) commonSvc.queryList(whReqOngdHVo);
		
		model.put("whReqOngdHVoList", whReqOngdHVoList);
		
		return LayoutUtil.getJspPath("/wh/help/listOngdHstPop");
	}
	
	/** [팝업] - 진행처리 이력 상세 조회 */
	@RequestMapping(value = {"/wh/help/viewOngdHstPop", "/wh/adm/help/viewOngdHstPop"})
	public String viewOngdHstPop(HttpServletRequest request,
			@Parameter(name="reqNo", required=false) String reqNo,
			@Parameter(name="hstNo", required=false) String hstNo,
			ModelMap model) throws Exception {
		
		if (reqNo==null || reqNo.isEmpty() || hstNo==null || hstNo.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		String langTypCd = LoginSession.getLangTypCd(request);
		
		WhReqOngdDVo whReqOngdDVo = whHpSvc.getReqOngdDVo(reqNo);
		if (whReqOngdDVo == null) {
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		WhReqOngdHVo whReqOngdHVo = whHpSvc.getWhReqOngdHVo(langTypCd, reqNo, hstNo);
		if (whReqOngdHVo == null) {
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		model.put("whReqOngdHVo", whReqOngdHVo);
		
		return LayoutUtil.getJspPath("/wh/help/viewOngdHstPop");
	}
	
	/** [AJAX] - 진행처리 이력 삭제 */
	@RequestMapping(value = {"/wh/help/transOngdHstDelAjx", "/wh/adm/help/transOngdHstDelAjx"})
	public String transOngdHstDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String reqNo = (String) object.get("reqNo");
			JSONArray jsonArray = (JSONArray) object.get("hstNos");
			if (reqNo==null || jsonArray==null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			if(jsonArray!=null && !jsonArray.isEmpty()){
				String hstNo=null;
				WhReqOngdHVo whReqOngdHVo=null;
				for(int i=0;i<jsonArray.size();i++){
					hstNo = (String)jsonArray.get(i);
					// 진행처리이력 삭제
					whReqOngdHVo=new WhReqOngdHVo();
					whReqOngdHVo.setReqNo(reqNo);
					whReqOngdHVo.setHstNo(hstNo);
					queryQueue.delete(whReqOngdHVo);
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [팝업] - 완료처리 이력 조회 */
	@RequestMapping(value = {"/wh/help/listCmplHstPop", "/wh/adm/help/listCmplHstPop"})
	public String listCmplHstPop(HttpServletRequest request,
			@Parameter(name="reqNo", required=false) String reqNo,
			ModelMap model) throws Exception {
		
		if (reqNo==null || reqNo.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 환경설정 - model에 추가(파일사용여부, 결과평가사용여부)
		Map<String, String> configMap=whHpSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
		
		WhReqOngdDVo whReqOngdDVo = whHpSvc.getReqOngdDVo(reqNo);
		if (whReqOngdDVo == null) {
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wh/adm/");
				
		// 권한체크
		whHpSvc.chkUrlAuth(request, "hdl", userVo, null, whReqOngdDVo, configMap, isAdmin);
				
		WhReqCmplDVo whReqCmplDVo = new WhReqCmplDVo();
		whReqCmplDVo.setQueryLang(langTypCd);
		whReqCmplDVo.setHst(true);
		whReqCmplDVo.setReqNo(reqNo);
		whReqCmplDVo.setOrderBy(" T.REG_DT DESC");
		
		@SuppressWarnings("unchecked")
		List<WhReqCmplDVo> whReqCmplDVoList = (List<WhReqCmplDVo>) commonSvc.queryList(whReqCmplDVo);
		
		model.put("whReqCmplDVoList", whReqCmplDVoList);
		
		return LayoutUtil.getJspPath("/wh/help/listCmplHstPop");
	}
	
	/** [팝업] - 완료처리 이력 상세 조회 */
	@RequestMapping(value = {"/wh/help/viewCmplHstPop", "/wh/adm/help/viewCmplHstPop"})
	public String viewCmplHstPop(HttpServletRequest request,
			@Parameter(name="reqNo", required=false) String reqNo,
			@Parameter(name="hstNo", required=false) String hstNo,
			ModelMap model) throws Exception {
		
		if (reqNo==null || reqNo.isEmpty() || hstNo==null || hstNo.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WhReqOngdDVo whReqOngdDVo = whHpSvc.getReqOngdDVo(reqNo);
		if (whReqOngdDVo == null) {
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		// 완료상태
		if(!"C".equals(whReqOngdDVo.getStatCd())){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		WhReqStatDVo whReqStatDVo = whHpSvc.getWhReqStatDVo(reqNo, "C");
		if(whReqStatDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		String cmplEditorYn = whReqStatDVo.getHtmlYn();
		model.put("cmplEditorYn", cmplEditorYn);
		
		WhReqCmplDVo whReqCmplDVo = whHpSvc.getReqCmplDVo(langTypCd, reqNo, hstNo, "N".equals(cmplEditorYn));
		if (whReqCmplDVo == null) {
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		// 환경설정 - model에 추가(파일사용여부, 결과평가사용여부)
		Map<String, String> configMap=whHpSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
				
		if(configMap.containsKey("fileYn") && "Y".equals(configMap.get("fileYn")))
			whReqCmplDVo.setFileCnt(whFileSvc.getFileVoListCnt(hstNo, "H")); // 파일 건수 조회
		
		model.put("whReqOngdDVo", whReqOngdDVo);
		model.put("whReqCmplDVo", whReqCmplDVo);
		
		if(cmplEditorYn!=null && "Y".equals(cmplEditorYn)){//lobHandler
			model.put("cmplLobHandler", lobHandler.create(
					"SELECT HDL_CONT FROM WH_REQ_CMPL_H WHERE HST_NO = ? AND REQ_NO = ?", 
					new String[]{hstNo, reqNo}
			));
		}
		
		return LayoutUtil.getJspPath("/wh/help/viewCmplHstPop");
	}
	
	/** [AJAX] - 완료처리 삭제 */
	@RequestMapping(value = {"/wh/help/transCmplHstDelAjx", "/wh/adm/help/transCmplHstDelAjx"})
	public String transCmplHstDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String reqNo = (String) object.get("reqNo");
			JSONArray jsonArray = (JSONArray) object.get("hstNos");
			if (reqNo==null || jsonArray==null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 삭제할 파일 목록
			List<CommonFileVo> deletedFileList=new ArrayList<CommonFileVo>();
			
			if(jsonArray!=null && !jsonArray.isEmpty()){
				String hstNo=null;
				WhReqCmplDVo whReqCmplDVo=null;
				for(int i=0;i<jsonArray.size();i++){
					hstNo = (String)jsonArray.get(i);
					// 파일 삭제
					whHpSvc.deleteFile(queryQueue, deletedFileList, hstNo, "H");
					// 완료이력 삭제
					whReqCmplDVo=new WhReqCmplDVo();
					whReqCmplDVo.setReqNo(reqNo);
					whReqCmplDVo.setHstNo(hstNo);
					whReqCmplDVo.setHst(true);
					queryQueue.delete(whReqCmplDVo);
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// 파일 삭제
			whFileSvc.deleteDiskFiles(deletedFileList);
						
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [팝업] - 결과평가 */
	@RequestMapping(value = {"/wh/help/setReqEvalPop", "/wh/adm/help/setReqEvalPop"})
	public String setReqEvalPop(HttpServletRequest request,
			@Parameter(name="reqNo", required=false) String reqNo,
			ModelMap model) throws Exception {
		
		if (reqNo.isEmpty() || reqNo.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		WhReqEvalDVo whReqEvalDVo = whHpSvc.getReqEvalDVo(reqNo); 
		if(whReqEvalDVo!=null){
			// wh.jsp.eval.dup=이미 평가가 완료되었습니다.
			throw new CmException("wh.jsp.eval.dup", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId = userVo.getCompId();
		
		// 결과평가 조회
		model.put("whResEvalBVoList", whHpSvc.getResEvalList(compId, langTypCd));
		
		return LayoutUtil.getJspPath("/wh/help/setReqEvalPop");
	}
	
	/** [AJAX] - 결과평가 저장 */
	@RequestMapping(value = {"/wh/help/transEvalAjx", "/wh/adm/help/transEvalAjx"})
	public String transEvalAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			Map<String,Object> paramMap = JsonUtil.jsonToMap(object);
			
			WhReqOngdDVo whReqOngdDVo = new WhReqOngdDVo();
			whCmSvc.setParamToVo(paramMap, whReqOngdDVo);
			
			QueryQueue queryQueue = new QueryQueue();
			whHpSvc.saveProgress(request, queryQueue, null, whReqOngdDVo);
			
			// 결과평가 저장
			WhReqEvalDVo whReqEvalDVo = new WhReqEvalDVo();
			whCmSvc.setParamToVo(paramMap, whReqEvalDVo);
			whReqEvalDVo.setRegrUid(userVo.getUserUid());
			whHpSvc.saveReqEval(request, queryQueue, null, whReqEvalDVo);
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - 결과평가 삭제 */
	@RequestMapping(value = {"/wh/help/transEvalDelAjx", "/wh/adm/help/transEvalDelAjx"})
	public String transEvalDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String reqNo = (String) object.get("reqNo");
			JSONArray reqNoArray = (JSONArray) object.get("reqNos");
			if (reqNo == null && reqNoArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 단건 삭제
			if(reqNo!=null && !reqNo.isEmpty())
				whHpSvc.deleteEvalList(request, queryQueue, reqNo);
			
			// 복수 삭제
			if(reqNoArray!=null && !reqNoArray.isEmpty()){
				for(int i=0;i<reqNoArray.size();i++){
					reqNo = (String)reqNoArray.get(i);
					whHpSvc.deleteEvalList(request, queryQueue, reqNo);
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** 목록 - 현황 조회 */
	@RequestMapping(value = {"/wh/help/listDashBrd", "/wh/help/listDashBrdPltFrm", "/wh/adm/help/listDashBrd"})
	public String listDashBrd(HttpServletRequest request,
			ModelMap model) throws Exception {
				
		// 관리자 페이지 여부
		boolean isAdmin = true; //request.getRequestURI().startsWith("/wh/adm/");
		
		// 요청경로 세팅
		String path = whCmSvc.getRequestPath(request, model , null);
		
		// 포틀릿 여부
		boolean isPlt = request.getRequestURI().indexOf("Plt")>0;
		
		// 프레임 여부
		boolean isFrm=!isPlt && request.getRequestURI().indexOf("Frm")>0;
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String compId=userVo.getCompId();
		// 현황 맵
		Map<String, Integer> dashBrdMap=new HashMap<String, Integer>();
		
		// 테이블관리 기본(WH_REQ_B) 테이블 - BIND
		WhReqBVo whReqBVo = new WhReqBVo();
		VoUtil.bind(request, whReqBVo);
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		whAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, whReqBVo, false);
		
		whReqBVo.setWithLob(false); // LOB 데이터 조회여부
		whReqBVo.setWithDtl(true); // 상세 데이터 조회여부
		
		// 조회조건 세팅
		whHpSvc.setQueryUrlOptions(request, model, whReqBVo, path, userVo, langTypCd, isAdmin, null);
				
		// parameters
		String mdId = ParamUtil.getRequestParam(request, "mdId", false);
					
		// 시스템 모듈 세팅
		whHpSvc.setParamSysMdList(request, model, compId, langTypCd, mdId, "Y");
		
		if(mdId!=null){
			// 담당자 목록
			model.put("whMdPichLVoList", whHpSvc.getUsePichList(userVo.getCompId(), langTypCd, mdId, null));
			
			WhMdBVo whMdBVo = whHpSvc.getWhMdBVo(mdId, langTypCd);
			// 유형 목록 조회
			if(whMdBVo!=null){
				model.put("whCatGrpLVoList", whHpSvc.getCatGrpDtlList(userVo.getCompId(), langTypCd, whMdBVo.getCatGrpId()));
				if(!"W".equals(whMdBVo.getMdTypCd())){
					List<String> mdIdList=whHpSvc.getSubIdAllList(null, compId, mdId, langTypCd);
					if(mdIdList!=null && mdIdList.size()>0)
						whReqBVo.setMdIdList(mdIdList);
					whReqBVo.setMdId(null);
				}
			}
		}
		
		// 요청, 접수, 반려, 처리중, 처리완료
		whReqBVo.setStatCdList(new String[]{"R","A","G","P","C"});
		Integer totalCnt=commonSvc.count(whReqBVo);
		dashBrdMap.put("W", totalCnt.intValue()); // 전체건수
		model.put("sumCnt", totalCnt);
		
		// 상태 코드 별 건수
		whReqBVo.setInstanceQueryId("com.innobiz.orange.web.wh.dao.WhReqBDao.selectDashBrd");
				
		@SuppressWarnings("unchecked")
		List<WhReqBVo> list = (List<WhReqBVo>) commonSvc.queryList(whReqBVo);
		
		if(list!=null && list.size()>0){
			for(WhReqBVo storedWhReqBVo : list){
				dashBrdMap.put(storedWhReqBVo.getStatCd(), storedWhReqBVo.getTotalCnt());
			}
			model.put("dashBrdMap", dashBrdMap);
		}
		
		// 환경설정 - model에 추가(결과평가사용여부)
		Map<String, String> configMap=whHpSvc.getEnvConfigAttr(model, userVo.getCompId(), new String[]{"resEvalUseYn"});
		if(configMap.containsKey("resEvalUseYn") && "Y".equals(configMap.get("resEvalUseYn"))){
			whReqBVo.setInstanceQueryId(null);
			whReqBVo.setEvalYn("Y");
			
			// 평가 건수 조회
			Integer cnt = commonSvc.count(whReqBVo);
			if(cnt!=null && cnt>0){
				dashBrdMap.put("E", cnt.intValue()); // 평가 건수 추가
				// 평가 상세 내역
				whReqBVo.setInstanceQueryId("com.innobiz.orange.web.wh.dao.WhReqBDao.selectEvalList");
				model.put("evalDtlList", commonSvc.queryList(whReqBVo));
			}
			
			model.put("statCdList", new String[]{"W","R","A","G","P","C","E"});			
		}else{
			model.put("statCdList", new String[]{"W","R","A","G","P","C"});
		}
		
		model.put("isAdmin", isAdmin); // 관리자여부
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		if(isPlt){//포틀릿
			model.put("isPlt", isPlt);
			// get 파라미터를 post 파라미터로 전달하기 위해
			model.put("paramEntryList", ParamUtil.getEntryMapList(request, "menuId"));
			return LayoutUtil.getJspPath("/wh/help/listDashBrd","Frm");
		}else if(isFrm){// 프레임
			model.put("pageSuffix", "Frm");
			return LayoutUtil.getJspPath("/wh/help/listDashBrd","Frm");
		}
		
		return LayoutUtil.getJspPath("/wh/help/listDashBrd");
	}
	
	/** 엑셀파일 다운로드 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/wh/help/excelDownLoad", "/wh/adm/help/excelDownLoad"}, method = RequestMethod.POST)
	public ModelAndView excelDownLoad(HttpServletRequest request,
			@Parameter(name="ext", required=false) String ext,
			ModelMap model) throws Exception {
		
		try {
			
			// 관리자 페이지 여부
			boolean isAdmin = request.getRequestURI().startsWith("/wh/adm/");
			
			String path = ParamUtil.getRequestParam(request, "listPage", false);
			path=path.replaceAll("list|set|view|trans|Del|PltFrm", "");
			path=path.toLowerCase();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 테이블관리 기본(WH_REQ_B) 테이블 - BIND
			WhReqBVo whReqBVo = new WhReqBVo();
			VoUtil.bind(request, whReqBVo);
			whReqBVo.setQueryLang(langTypCd);
			// 회사 ID 조회조건 추가[계열사 설정 확인]
			whAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, whReqBVo, false);
			
			whReqBVo.setWithLob(true); // LOB 데이터 조회여부
			whReqBVo.setWithDtl(true); // 상세 데이터 조회여부
			whReqBVo.setWithCmpl(true); // 완료처리 데이터 조회여부
			
			// 조회조건 세팅
			whHpSvc.setQueryUrlOptions(request, model, whReqBVo, path, userVo, langTypCd, isAdmin, null);

			List<WhReqBVo> whReqBVoList = (List<WhReqBVo>) commonSvc.queryList(whReqBVo);
			// 파일 목록조회
			ModelAndView mv = new ModelAndView("excelDownloadView");
			
			if(whReqBVoList==null || whReqBVoList.size() == 0 ){
				mv = new ModelAndView("cm/result/commonResult");
				mv.addObject("message", messageProperties.getMessage("em.msg.noExcelData", request));
				return mv;
			}
			// 컬럼 속성ID 조회
			List<String> colAtrbList = whHpSvc.getLstColAtrbList();
			// 컬럼명 조회
			List<String> colNames = whHpSvc.getLstColNmList(request);
			//데이터
			List<String> colValue = null;
			Map<String,Object> colValues = new HashMap<String,Object>();
			Map<String,Object> tempMap = null;
			int i=0;
			String va;
			int cellMaxLen=32767; // 셀 최대 글자수
			boolean isHtml=false;
			for(WhReqBVo storedWhReqBVo : whReqBVoList){
				tempMap = VoUtil.toMap(storedWhReqBVo, null);
				colValue = new ArrayList<String>();
				for(String atrbId : colAtrbList){
					va=(String)tempMap.get(atrbId);
					if(va!=null && atrbId.endsWith("Dt"))
						va = StringUtil.toShortDate(va);
					if(va!=null && (atrbId.endsWith("cont") || ("C".equals(storedWhReqBVo.getStatCd()) && atrbId.endsWith("hdlCont")))){
						isHtml=whHpSvc.isHtml(storedWhReqBVo.getReqNo(), atrbId.endsWith("cont") ? "R" : "C");
						if(isHtml && va.length()>cellMaxLen)
							va=null;
					}
					
					/*if(va!=null && atrbId.endsWith("cont")){
						isHtml=whHpSvc.isHtml(storedWhReqBVo.getReqNo(), "R");
						if(isHtml)
							va=EscapeUtil.escapeHTML(va);
					}*/
					/*if(va!=null && "C".equals(storedWhReqBVo.getStatCd()) && atrbId.endsWith("hdlCont")){
						isHtml=whHpSvc.isHtml(storedWhReqBVo.getReqNo(), "C");
						if(isHtml)
							va=EscapeUtil.escapeHTML(va);
					}*/
					if(va==null) va="";
					colValue.add(va);
				}
				colValues.put("col"+i,colValue);//데이터 세팅
				i++;
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			//시트명 세팅
			//String sheetNm = "wh.jsp.complete.title";
			//mv.addObject("sheetName", messageProperties.getMessage(sheetNm, request));//시트명
			mv.addObject("colNames", colNames);//컬럼명
			mv.addObject("colValues", colValues);//데이터
			mv.addObject("fileName", sdf.format(new Date(System.currentTimeMillis())));//파일명
			mv.addObject("ext", ext == null ? "xlsx" : ext);//파일 확장자(없으면 xls)
			
			return mv;
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		}
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/wh/downFile","/wh/adm/downFile"}, method = RequestMethod.POST)
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "fileIds", required = true) String fileIds
			) throws Exception {
		
		try {
			if (fileIds.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 다운로드 체크
			emAttachViewSvc.chkAttachDown(request, userVo.getCompId());
			
			// baseDir
			String wasCopyBaseDir = distManager.getWasCopyBaseDir();
			if (wasCopyBaseDir == null) {
				distManager.init();
				wasCopyBaseDir = distManager.getWasCopyBaseDir();
			}
			
			// fileId
			String[] fileIdArray = fileIds.split(",");
			List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
			for (String fileId : fileIdArray) {
				// 게시물첨부파일
				CommonFileVo fileVo = whFileSvc.getFileVo(Integer.valueOf(fileId));
				if (fileVo != null) {
					fileVo.setSavePath(wasCopyBaseDir+fileVo.getSavePath());
					File file = new File(fileVo.getSavePath());
					if (file.isFile()) {
						fileVoList.add(fileVo);
					}
				}
			}
			// 파일이 몇개인가
			if (fileVoList.size() == 0) {
				ModelAndView mv = new ModelAndView("cm/result/commonResult");
				Locale locale = SessionUtil.getLocale(request);
				// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
				mv.addObject("message", messageProperties.getMessage("cm.msg.file.fileNotFound", locale));
				return mv;
				
			} else if (fileVoList.size() == 1) {
				CommonFileVo fileVoVo = fileVoList.get(0);
				String savePath = fileVoVo.getSavePath();
				File file = new File(savePath);
				ModelAndView mv = new ModelAndView("fileDownloadView");
				mv.addObject("downloadFile", file);
				mv.addObject("realFileName", fileVoVo.getDispNm());
				return mv;
				
			} else {
				File zipFile = zipUtil.makeZipFile(fileVoList, "files.zip");
				ModelAndView mv = new ModelAndView("fileDownloadView");
				mv.addObject("downloadFile", zipFile);
				mv.addObject("realFileName", zipFile.getName());
				return mv;
			}
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
}
