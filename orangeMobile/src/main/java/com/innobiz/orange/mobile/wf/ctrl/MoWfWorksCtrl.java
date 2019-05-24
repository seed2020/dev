package com.innobiz.orange.mobile.wf.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSsoSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wf.svc.WfAdmSvc;
import com.innobiz.orange.web.wf.svc.WfCdSvc;
import com.innobiz.orange.web.wf.svc.WfCmSvc;
import com.innobiz.orange.web.wf.svc.WfFileSvc;
import com.innobiz.orange.web.wf.svc.WfFormSvc;
import com.innobiz.orange.web.wf.utils.WfConstant;
import com.innobiz.orange.web.wf.vo.WfFormBVo;
import com.innobiz.orange.web.wf.vo.WfFormColmLVo;
import com.innobiz.orange.web.wf.vo.WfFormLstDVo;
import com.innobiz.orange.web.wf.vo.WfFormRegDVo;
import com.innobiz.orange.web.wf.vo.WfWorksImgDVo;
import com.innobiz.orange.web.wf.vo.WfWorksLVo;

@Controller
public class MoWfWorksCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoWfWorksCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WfCmSvc wfCmSvc;
	
	/** 파일 서비스 */
	@Resource(name = "wfFileSvc")
	private WfFileSvc wfFileSvc;
	
	/** 양식 서비스 */
	@Resource(name = "wfFormSvc")
	private WfFormSvc wfFormSvc;
	
	/** 코드 서비스 */
	@Resource(name = "wfCdSvc")
	private WfCdSvc wfCdSvc;
	
	/** 관리 서비스 */
	@Resource(name = "wfAdmSvc")
	private WfAdmSvc wfAdmSvc;
	
	/** 첨부설정 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** SSO 처리용 서비스 */
	@Autowired
	private PtSsoSvc ptSsoSvc;
	
	/** 목록 */
	@RequestMapping(value = {"/wf/works/listWorks", "/wf/works/listWorksFrm", "/wf/adm/works/listWorksFrm", "/wf/works/listWorksPltFrm"})
	public String listWorks(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="formNo", required=false) String formNo,
			ModelMap model) throws Exception {
		
		if(formNo==null || formNo.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String compId = userVo.getCompId();
				
		WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, "Y");
		
		if(wfFormBVo==null || wfFormBVo.getGenId()==null){
			request.getRequestDispatcher(MoLayoutUtil.getErrorJsp(404)).forward(request, response);
			return null;
			// cm.msg.noData=해당하는 데이터가 없습니다.
			//throw new CmException("cm.msg.noData", request);
		}
		
		// 전사여부
		boolean isAllComp=wfFormBVo.getAllCompYn()!=null && "Y".equals(wfFormBVo.getAllCompYn());
		if(!isAllComp && !wfFormBVo.getCompId().equals(compId)){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		// 생성ID
		String genId = wfFormBVo.getGenId();
		
		// 양식등록상세 조회
		WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, null);
		if(wfFormRegDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		model.put("wfFormRegDVo", wfFormRegDVo);
		
		// 모바일 용
		// 현재 등록상세 데이터 조회
		wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(null, formNo, false, null);
		if(wfFormRegDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		String mobRegTypCd=wfFormRegDVo.getMobRegTypCd();
		model.put("mobRegTypCd", mobRegTypCd);
				
		// 외부모듈코드(컨텐츠의 크기 제한, 파일 크기제한, 목록갯수 등 해당 모듈의 설정을 위한 코드)
		String mdTypCd = wfFormBVo.getMdTypCd();
				
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wf/adm/");
				
		// 요청경로 세팅
		wfCmSvc.getRequestPath(request, model , null);
		
		// 포틀릿 여부
		boolean isPlt = request.getRequestURI().indexOf("Plt")>0;
		
		// 프레임 여부
		boolean isFrm=!isPlt && request.getRequestURI().indexOf("Frm")>0;
		
		// 기본으로 설정된 목록 컬럼 조회[배포기준]
		List<WfFormColmLVo> wfFormColmLVoList = wfAdmSvc.getCurrColmVoList(genId, formNo, null);
		
		// 모듈코드[웹,모바일,포틀릿]
		String mdCd = isPlt ? "P" : ServerConfig.IS_MOBILE ? "M" : "U";
		
		// 데이터 컬럼 조회[배포 버전이 아닌 현재 버전]
		List<WfFormLstDVo> wfFormLstDVoList = wfAdmSvc.getWfFormLstDVoList(request, null, formNo, null, mdCd, false);
		
		// 배포된 테이블의 컬럼 중에 목록 컬럼 추출
		wfFormColmLVoList = wfAdmSvc.getLstToColmVoList(wfFormColmLVoList, wfFormLstDVoList);
				
		// 목록컬럼 + 조회컬럼 세팅
		wfFormSvc.setLstColmList(model, genId, formNo, userVo, langTypCd, wfFormColmLVoList, wfFormLstDVoList);
		
		// 테이블관리 기본(WF_0000_WORKS_L) 테이블 - BIND
		WfWorksLVo wfWorksLVo = wfFormSvc.newWfWorksLVo(genId, formNo, true);
		VoUtil.bind(request, wfWorksLVo);
		wfWorksLVo.setQueryLang(langTypCd);
		
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		wfAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, wfWorksLVo, isAllComp);
		
		// 검색 컬럼 조회
		List<WfFormLstDVo> srchColmVoList = wfAdmSvc.getLstToSrchVoList(wfFormLstDVoList, wfFormRegDVo);
				
		// 조회조건 세팅
		wfFormSvc.setQueryUrlOptions(request, model, wfWorksLVo, wfFormLstDVoList, srchColmVoList, userVo, langTypCd, isAdmin, null);
				
		Integer recodeCount = commonSvc.count(wfWorksLVo);
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
			PersonalUtil.setFixedPaging(request, wfWorksLVo, rowCnt, recodeCount);
		}else {
			// 양식에 외부모듈코드(참조) 가 있으면 해당 모듈의 페이지별 레코드수 세팅
			if(mdTypCd!=null && !mdTypCd.isEmpty() && !"no".equals(mdTypCd)){
				HttpSession session = request.getSession();
				Integer pageRowCnt=PersonalUtil.getPageRowCnt(mdTypCd, session);
				PersonalUtil.setFixedPaging(request, wfWorksLVo, pageRowCnt, recodeCount);
				model.put("pageRowCnt", pageRowCnt);
			}else{
				PersonalUtil.setPaging(request, wfWorksLVo, recodeCount);
			}
		}
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> mapList = (List<Map<String, Object>>) commonSvc.queryList(wfWorksLVo);
		
		// 목록 권한 세팅
		wfFormSvc.setAuthListMap(userVo, mapList, isAdmin);
				
		model.put("mapList", mapList);
		
		// 코드 목록 조회
		wfFormSvc.setCodeListMap(langTypCd, formNo, wfFormColmLVoList, mapList);
		
		// 이미지 목록 조회
		wfFormSvc.setImgListMap(langTypCd, formNo, wfFormColmLVoList, mapList);
		
		// 파일 목록 조회
		wfFormSvc.setListFileCnt(formNo, wfFormColmLVoList, mapList);
		
		// 양식 코드 목록 세팅
		wfCdSvc.setFormCdListMap(model, wfFormColmLVoList, wfFormRegDVo, langTypCd);
				
		model.put("recodeCount", recodeCount);
		
		model.put("wfFormBVo", wfFormBVo); // 양식 데이터
		
		model.put("isAdmin", isAdmin); // 관리자여부
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		if(isPlt){//포틀릿
			return MoLayoutUtil.getJspPath("/wf/plt/listWorksPltFrm");
		}else if(isFrm){// 프레임
			model.put("pageSuffix", "Frm");
			return MoLayoutUtil.getJspPath("/wf/works/listWorks","Frm");
		}
		
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request, "menuId", "formNo"));
		
		return MoLayoutUtil.getJspPath("/wf/works/listWorks");
	}
	
	/** 등록화면 */
	@RequestMapping(value = {"/wf/works/setWorks", "/wf/adm/works/setWorks"})
	public String setRegData(HttpServletRequest request,
			@RequestParam(value = "formNo", required = false) String formNo,
			@RequestParam(value = "workNo", required = false) String workNo,
			ModelMap model) throws Exception {
		
		// 양식번호 체크
		if(formNo==null || formNo.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String compId = userVo.getCompId();
				
		WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, "Y");
		
		if(wfFormBVo==null || wfFormBVo.getGenId()==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		// 외부모듈코드(컨텐츠의 크기 제한, 파일 크기제한, 목록갯수 등 해당 모듈의 설정을 위한 코드)
		String mdTypCd = wfFormBVo.getMdTypCd();
		
		// 전사여부
		boolean isAllComp=wfFormBVo.getAllCompYn()!=null && "Y".equals(wfFormBVo.getAllCompYn());
		if(!isAllComp && !wfFormBVo.getCompId().equals(compId)){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
				
		model.put("wfFormBVo", wfFormBVo);
		
		// 생성ID
		String genId = wfFormBVo.getGenId();
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wf/adm/");
				
		// 요청경로 세팅
		wfCmSvc.getRequestPath(request, model , null);
		
		// 양식등록상세 조회[배포기준]
		WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, null);
		if(wfFormRegDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		model.put("wfFormRegDVo", wfFormRegDVo);
		
		// 탭목록맵 세팅
		wfFormSvc.setLangTabListMap(model, wfFormRegDVo);
		
		// 모바일 용
		// 현재 등록상세 데이터 조회
		wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(null, formNo, false, null);
		if(wfFormRegDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		String mobRegTypCd=wfFormRegDVo.getMobRegTypCd();
		model.put("mobRegTypCd", mobRegTypCd);
		
		// 양식모바일상세 조회[배포기준]
		WfFormRegDVo wfFormMobDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, Boolean.TRUE);
		if(wfFormMobDVo==null) // 배포된 모바일 양식이 없을 경우 기본양식으로 세팅
			wfFormMobDVo=wfAdmSvc.getWfFormRegDVo(null, formNo, false, Boolean.TRUE);
		model.put("wfFormMobDVo", wfFormMobDVo);
		
		if(workNo!=null && !workNo.isEmpty()){
			// 테이블관리 기본(WF_0000_WORKS_L) 테이블 - BIND
			WfWorksLVo wfWorksLVo = wfFormSvc.newWfWorksLVo(genId, formNo, null);
			wfWorksLVo.setQueryLang(langTypCd);
			wfWorksLVo.setWorkNo(workNo);
			Map<String, Object> wfWorksLVoMap = commonSvc.queryMap(wfWorksLVo);
			//wfWorksLVo=(WfWorksLVo)commonSvc.queryVo(wfWorksLVo);
			if(wfWorksLVoMap==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			model.put("wfWorksLVoMap", wfWorksLVoMap);
			
			String workGenId=(String)wfWorksLVoMap.get("genId");
			
			if(!workGenId.equals(genId))
				model.put("notGenId", Boolean.TRUE);
			
			// 코드 목록 조회 세팅
			Map<String, String[]> cdListMap = wfFormSvc.getCodeListMap(formNo, workNo, langTypCd);
			if(cdListMap!=null) model.put("cdListMap", cdListMap);
			
			// 이미지 목록 조회
			wfFormSvc.setImgVoMap(langTypCd, formNo, null, wfWorksLVoMap);
			
		}
		
		// 기본으로 설정된 목록 컬럼 조회
		List<WfFormColmLVo> wfFormColmLVoList = wfAdmSvc.getCurrColmVoList(genId, formNo, null);
		if(wfFormColmLVoList!=null){
			Map<String, WfFormColmLVo> colmListMap = new HashMap<String, WfFormColmLVo>();
			for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
				colmListMap.put(storedWfFormColmLVo.getColmNm(), storedWfFormColmLVo);
			}
			model.put("colmListMap", colmListMap);
		}		
		// 양식 코드 목록 세팅
		wfCdSvc.setFormCdListMap(model, wfFormColmLVoList, wfFormRegDVo, langTypCd);
		
		// 최대 본문 사이즈 model에 추가
		wfFormSvc.putBodySizeToModel(request, model, mdTypCd);
				
		// 첨부파일 리스트 model에 추가
		wfFileSvc.putFileListToModel(formNo, workNo, model, userVo.getCompId(), mdTypCd);
		
		// 유효성 검증
		model.addAttribute("JS_OPTS", new String[]{"validator"});
				
		model.put("isAdmin", isAdmin);
		
		return MoLayoutUtil.getJspPath("/wf/works/setWorks");
	}
	
	
	/** 상세 보기 */
	@RequestMapping(value = {"/wf/works/viewWorks", "/wf/works/viewWorksPop", "/wf/adm/works/viewWorks", "/wf/adm/works/viewWorksPop"})
	public String viewWorks(HttpServletRequest request,
			@RequestParam(value = "formNo", required = false) String formNo,
			@RequestParam(value = "workNo", required = false) String workNo,
			ModelMap model) throws Exception {
		
		// 양식번호 체크
		if(formNo==null || formNo.isEmpty() || workNo==null || workNo.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wf/adm/");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String compId = userVo.getCompId();
				
		WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, "Y");
		
		if(wfFormBVo==null || wfFormBVo.getGenId()==null){
			//return MoLayoutUtil.getErrorJsp(404);
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		// 전사여부
		boolean isAllComp=wfFormBVo.getAllCompYn()!=null && "Y".equals(wfFormBVo.getAllCompYn());
		if(!isAllComp && !wfFormBVo.getCompId().equals(compId)){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
				
		model.put("wfFormBVo", wfFormBVo);
		
		// 생성ID
		String genId = null;
				
		// 테이블관리 기본(WF_0000_WORKS_L) 테이블 - BIND
		WfWorksLVo wfWorksLVo = wfFormSvc.newWfWorksLVo(null, formNo, null);
		wfWorksLVo.setQueryLang(langTypCd);
		wfWorksLVo.setWorkNo(workNo);
		Map<String, Object> wfWorksLVoMap = commonSvc.queryMap(wfWorksLVo);
		//wfWorksLVo=(WfWorksLVo)commonSvc.queryVo(wfWorksLVo);
		if(wfWorksLVoMap==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		// 목록 권한 세팅
		wfFormSvc.setAuthVoMap(userVo, wfWorksLVoMap, isAdmin);
				
		model.put("wfWorksLVoMap", wfWorksLVoMap);
		
		// 사용자가 등록한 생성ID 세팅
		genId=(String)wfWorksLVoMap.get("genId");
		
		if(genId==null) genId=wfFormBVo.getGenId();
				
		// 요청경로 세팅
		wfCmSvc.getRequestPath(request, model , null);
		
		// 양식등록상세 조회
		WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, null);
		if(wfFormRegDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		model.put("wfFormRegDVo", wfFormRegDVo);
		
		// 탭목록맵 세팅
		wfFormSvc.setLangTabListMap(model, wfFormRegDVo);
				
		// 모바일 용
		// 현재 등록상세 데이터 조회
		wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(null, formNo, false, null);
		if(wfFormRegDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		String mobRegTypCd=wfFormRegDVo.getMobRegTypCd();
		model.put("mobRegTypCd", mobRegTypCd);
		//if(mobRegTypCd!=null && "M".equals(mobRegTypCd)){
			// 양식모바일상세 조회[배포기준]
			WfFormRegDVo wfFormMobDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, Boolean.TRUE);
			if(wfFormMobDVo==null) // 배포된 모바일 양식이 없을 경우 기본양식으로 세팅
				wfFormMobDVo=wfAdmSvc.getWfFormRegDVo(null, formNo, false, Boolean.TRUE);
			model.put("wfFormMobDVo", wfFormMobDVo);
		//}
		
		// 기본으로 설정된 목록 컬럼 조회
		List<WfFormColmLVo> wfFormColmLVoList = wfAdmSvc.getCurrColmVoList(genId, formNo, null);
		if(wfFormColmLVoList!=null){
			Map<String, WfFormColmLVo> colmListMap = new HashMap<String, WfFormColmLVo>();
			for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
				colmListMap.put(storedWfFormColmLVo.getColmNm(), storedWfFormColmLVo);
			}
			model.put("colmListMap", colmListMap);
		}		
		
		// 조회수 저장
		wfFormSvc.setWorksReadCnt(formNo, workNo, userVo.getUserUid(), wfWorksLVo, isAdmin);
				
		// 코드 목록 조회
		wfFormSvc.setCodeVoMap(langTypCd, formNo, wfFormColmLVoList, wfWorksLVoMap);
		
		// 이미지 목록 조회
		wfFormSvc.setImgVoMap(langTypCd, formNo, wfFormColmLVoList, wfWorksLVoMap);
					
		// 첨부파일 리스트 model에 추가
		wfFileSvc.putFileListToModel(formNo, workNo, model, userVo.getCompId(), null);
				
		model.put("isAdmin", isAdmin);
		
		// 팝업여부
		boolean isPop=request.getRequestURI().indexOf("Pop")>0;
		if(isPop){
			model.put("isPop", isPop);
			return MoLayoutUtil.getJspPath("/wf/works/viewWorks", "Frm");
		}
		
		return MoLayoutUtil.getJspPath("/wf/works/viewWorks");
	}
	
	/** [AJAX] - 웹버전 URL 리턴 */
	@RequestMapping(value = {"/wf/works/getWorksUrlAjx", "/ap/box/getWorksUrlAjx", "/bb/getWorksUrlAjx"})
	public String viewWorksUrlAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			
			// 업무관리여부
			boolean isWorks = request.getRequestURI().startsWith("/wf/works/");
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String formNo = (String) object.get("formNo");
			String mode = (String) object.get("mode");
			
			if(mode==null || mode.isEmpty()) mode="view";
			
			String mdCd = (String) object.get("mdCd");
			String mdRefId = (String) object.get("mdRefId");
			String mdNo = (String) object.get("mdNo");
			
			String workNo = (String) object.get("workNo");
			if((isWorks && (formNo==null || formNo.isEmpty() || ("view".equals(mode) && (workNo==null || workNo.isEmpty())))) 
					|| (!isWorks && (mdCd==null || mdCd.isEmpty() || mdRefId==null || mdRefId.isEmpty() || ("view".equals(mode) && (mdNo==null || mdNo.isEmpty()))))){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
				
			}
			
			// 저장후 콜백 함수명
			String openCallback = (String) object.get("openCallback");
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String url=null;
			String authUrl=null;
			if(isWorks){
				url="/wf/works/"+mode+"WorksOpen.do?formNo="+formNo;
				if(workNo!=null && !workNo.isEmpty())
					url+="&workNo="+workNo;
				authUrl="/wf/works/listWorks.do?formNo="+formNo;
			}else{
				if("AP".equals(mdCd)){
					String refParams = (String) object.get("refParams");
					url="/ap/box/setDocOpen.do?"+refParams;
					authUrl="/ap/box/listApvBx.do?"+refParams;
					if(mdNo!=null && !mdNo.isEmpty())
						url+="&apvNo="+mdNo;					
				}else if("BB".equals(mdCd)){
					url="/bb/"+mode+"BullOpen.do?brdId="+mdRefId;
					authUrl="/bb/listBull.do?brdId="+mdRefId;
					if(mdNo!=null && !mdNo.isEmpty())
						url+="&bullId="+mdNo;
				}
			}
			
			if(url==null || authUrl==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			url+=url.indexOf("?")>-1 ? "&" : "?"; 
			url+="isMobile=Y";
			
			if(openCallback!=null && !openCallback.isEmpty())
				url+="&openCallback="+openCallback;
			
			// 웹버전 보기 URL
			String webUrl=ptSsoSvc.createSsoMobile(userVo, url, authUrl);
			
			if(webUrl!=null)
				model.put("webUrl", webUrl);
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 상세 보기 - 웹버전 */
	@RequestMapping(value = {"/wf/works/viewWorksWeb", "/wf/works/viewWorksWebPop"})
	public String viewWorksWeb(HttpServletRequest request,
			@RequestParam(value = "formNo", required = false) String formNo,
			@RequestParam(value = "workNo", required = false) String workNo,
			ModelMap model) throws Exception {
		
		// 양식번호 체크
		if(formNo==null || formNo.isEmpty() || workNo==null || workNo.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String compId = userVo.getCompId();
				
		WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, "Y");
		
		if(wfFormBVo==null || wfFormBVo.getGenId()==null){
			//return MoLayoutUtil.getErrorJsp(404);
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		// 전사여부
		boolean isAllComp=wfFormBVo.getAllCompYn()!=null && "Y".equals(wfFormBVo.getAllCompYn());
		if(!isAllComp && !wfFormBVo.getCompId().equals(compId)){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
				
		model.put("wfFormBVo", wfFormBVo);
		
		// 생성ID
		String genId = null;
				
		// 테이블관리 기본(WF_0000_WORKS_L) 테이블 - BIND
		WfWorksLVo wfWorksLVo = wfFormSvc.newWfWorksLVo(null, formNo, null);
		wfWorksLVo.setQueryLang(langTypCd);
		wfWorksLVo.setWorkNo(workNo);
		Map<String, Object> wfWorksLVoMap = commonSvc.queryMap(wfWorksLVo);
		//wfWorksLVo=(WfWorksLVo)commonSvc.queryVo(wfWorksLVo);
		if(wfWorksLVoMap==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		// 목록 권한 세팅
		wfFormSvc.setAuthVoMap(userVo, wfWorksLVoMap, false);
				
		model.put("wfWorksLVoMap", wfWorksLVoMap);
		
		// 사용자가 등록한 생성ID 세팅
		genId=(String)wfWorksLVoMap.get("genId");
		
		if(genId==null) genId=wfFormBVo.getGenId();
				
		// 요청경로 세팅
		wfCmSvc.getRequestPath(request, model , null);
		
		// 양식등록상세 조회
		WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, null);
		if(wfFormRegDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		model.put("wfFormRegDVo", wfFormRegDVo);
		
		// 기본으로 설정된 목록 컬럼 조회
		List<WfFormColmLVo> wfFormColmLVoList = wfAdmSvc.getCurrColmVoList(genId, formNo, null);
		if(wfFormColmLVoList!=null){
			Map<String, WfFormColmLVo> colmListMap = new HashMap<String, WfFormColmLVo>();
			for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
				colmListMap.put(storedWfFormColmLVo.getColmNm(), storedWfFormColmLVo);
			}
			model.put("colmListMap", colmListMap);
		}		
				
		// 코드 목록 조회
		wfFormSvc.setCodeVoMap(langTypCd, formNo, wfFormColmLVoList, wfWorksLVoMap);
		
		// 이미지 목록 조회
		wfFormSvc.setImgVoMap(langTypCd, formNo, wfFormColmLVoList, wfWorksLVoMap);
					
		// 첨부파일 리스트 model에 추가
		wfFileSvc.putFileListToModel(formNo, workNo, model, userVo.getCompId(), null);
				
		// 서버 설정 목록 조회
		Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		String domain = ServerConfig.IS_LOC ? "127.0.0.1:8080" : svrEnvMap.get("webDomain");
		String port = "Y".equals(sysPlocMap.get("portForwarding")) ? ":60080" : "";
		String url = "http://"+domain+port;
		
		model.put("webUrl", url);
		
		// 팝업여부
		boolean isPop=request.getRequestURI().indexOf("Pop")>0;
		if(isPop){
			model.put("isPop", isPop);
			return MoLayoutUtil.getJspPath("/wf/works/viewWorksWeb", "Pop");
		}
		
		return MoLayoutUtil.getJspPath("/wf/works/viewWorksWeb", "Sub");
	}
	
	/** 저장 */
	@RequestMapping(value = {"/wf/works/transWorksPost", "/wf/adm/works/transWorks"}, method = RequestMethod.POST)
	public String transWorks(HttpServletRequest request,
			ModelMap model) {
		
		UploadHandler uploadHandler = null;
		try {
			
			// Multipart 파일 업로드
			uploadHandler = wfFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 요청경로 세팅
			String path = wfCmSvc.getRequestPath(request, model , null);
			path=path.toLowerCase();
			
			// parameters
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			String formNo = ParamUtil.getRequestParam(request, "formNo", true);
			String workNo = ParamUtil.getRequestParam(request, "workNo", false);
			
			if (listPage.isEmpty() || viewPage.isEmpty() || formNo.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			String compId = userVo.getCompId();
			
			WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, "Y");
			
			if(wfFormBVo==null || wfFormBVo.getGenId()==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			// 전사여부
			boolean isAllComp=wfFormBVo.getAllCompYn()!=null && "Y".equals(wfFormBVo.getAllCompYn());
			if(!isAllComp && !wfFormBVo.getCompId().equals(compId)){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			// 생성ID
			String genId = wfFormBVo.getGenId();
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 저장
			String refId=wfFormSvc.saveWorks(request, queryQueue, genId, formNo, workNo, null, null, wfFormBVo.getFormTyp()!=null && "A".equals(wfFormBVo.getFormTyp()) ? true : false);
			
			// 파일 저장
			List<CommonFileVo> deletedFileList = wfFileSvc.saveFile(request, formNo, refId, queryQueue);
			
			List<String> deleteFilePathList=null;
			
			if(workNo!=null){
				// 삭제할 이미지 번호 목록
				String delImgNo = ParamUtil.getRequestParam(request, "delImgNoList", false);
				
				if(delImgNo!=null && !delImgNo.isEmpty()){
					String[] delImgNos=delImgNo.split(",");
					List<String> imgNoList=new ArrayList<String>();
					for(String imgNo : delImgNos){
						imgNoList.add(imgNo.trim());
					}
					// 이미지 삭제
					deleteFilePathList=wfFormSvc.deleteImgList(queryQueue, formNo, workNo, imgNoList);
				}
			}
			
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			wfFileSvc.deleteDiskFiles(deletedFileList);
			
			// 실제 이미지 파일 삭제	
			if(deleteFilePathList!=null && deleteFilePathList.size()>0)
				wfFileSvc.deleteWebFiles(deleteFilePathList);
						
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
	        
			if(workNo!=null && !workNo.isEmpty()){
				model.put("todo", "$m.nav.prev(event, '" + viewPage + "');");
			}else{
				model.put("todo", "$m.nav.curr(null, '" + listPage + "');");
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

		return MoLayoutUtil.getResultJsp();
	}
	
	/** [AJAX] - 양식 삭제 */
	@RequestMapping(value = {"/wf/works/transWorksDelAjx", "/wf/adm/works/transWorksDelAjx"})
	public String transWorksDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String formNo = (String) object.get("formNo");
			String workNo = (String) object.get("workNo");
			JSONArray workNos = (JSONArray) object.get("workNos");
			if (formNo==null || formNo.isEmpty() || ((workNo==null || workNo.isEmpty()) && (workNos==null || workNos.isEmpty()))) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
						
			WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, "Y");
			
			if(wfFormBVo==null || wfFormBVo.getGenId()==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			// 생성ID
			String genId = wfFormBVo.getGenId();
			
			QueryQueue queryQueue = new QueryQueue();
			
			
			// 삭제할 파일 경로
			List<CommonFileVo> deletedFileList=new ArrayList<CommonFileVo>();
			
			// 삭제할 이미지 경로			
			List<String> deleteFilePathList=new ArrayList<String>();
			
			// 단건 삭제
			if(workNo!=null && !workNo.isEmpty())
				wfFormSvc.deleteWorks(queryQueue, genId, formNo, workNo, deletedFileList, deleteFilePathList, true);
			
			// 복수 삭제
			if(workNos!=null && !workNos.isEmpty()){
				for(int i=0;i<workNos.size();i++){
					workNo = (String)workNos.get(i);
					wfFormSvc.deleteWorks(queryQueue, genId, formNo, workNo, deletedFileList, deleteFilePathList, true);
				}
			}
			
			// 삭제 쿼리 실행
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// 파일 제거
			if(deletedFileList.size()>0)
				wfFileSvc.deleteDiskFiles(deletedFileList);
			
			// 이미지 제거
			if(deleteFilePathList.size()>0)
				wfFileSvc.deleteWebFiles(deleteFilePathList);
			
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
	
	/** [팝업] 이미지 선택 */
	@RequestMapping(value = {"/wf/works/viewImagePop", "/wf/adm/works/viewImagePop", "/wf/works/setImagePop", "/wf/adm/works/setImagePop", "/wf/adm/form/setImagePop"})
	public String setImagePop(HttpServletRequest request,
			@Parameter(name="formNo", required=true) String formNo,
			@Parameter(name="workNo", required=false) String workNo,
			@Parameter(name="colmNm", required=false) String colmNm,
			@Parameter(name="previewYn", required=false) String previewYn,
			ModelMap model) throws Exception {
		
		boolean isView=request.getRequestURI().startsWith("/wf/works/view") || request.getRequestURI().startsWith("/wf/adm/works/view");
		
		if(!isView && colmNm!=null && !colmNm.isEmpty()){
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			String compId = userVo.getCompId();
			
			WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, "Y");
			
			if(wfFormBVo==null || wfFormBVo.getGenId()==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			// 전사여부
			boolean isAllComp=wfFormBVo.getAllCompYn()!=null && "Y".equals(wfFormBVo.getAllCompYn());
			if(!isAllComp && !wfFormBVo.getCompId().equals(compId)){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			// 양식등록상세 조회
			WfFormRegDVo wfFormRegDVo = null;
			
			if(previewYn!=null && "Y".equals(previewYn)){
				// 양식등록상세 조회
				wfFormRegDVo = WfConstant.getPreviewVo();
				model.put("isPreview", Boolean.TRUE);
			}else{
				// 생성ID
				String genId = wfFormBVo.getGenId();
				
				// 양식등록상세 조회
				wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(genId, formNo, true, null);
			}
			
			if(wfFormRegDVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			if(wfFormRegDVo.getAttrVa()!=null && !wfFormRegDVo.getAttrVa().isEmpty()){
				// JSON 객체로 변환
				JSONObject attrVa = (JSONObject) JSONValue.parse(wfFormRegDVo.getAttrVa());
				if(attrVa.containsKey(colmNm)){
					model.put("imgAttrVa", attrVa.get(colmNm));
				}
				
			}
		}
		if(workNo!=null && !workNo.isEmpty() && colmNm!=null && !colmNm.isEmpty()){
						
			WfWorksImgDVo wfWorksImgDVo = new WfWorksImgDVo(formNo);
			wfWorksImgDVo.setWorkNo(workNo);
			wfWorksImgDVo.setColmNm(colmNm);
			wfWorksImgDVo = (WfWorksImgDVo)commonSvc.queryVo(wfWorksImgDVo);
			model.put("wfWorksImgDVo", wfWorksImgDVo);
		}
		//if(request.getRequestURI().startsWith("/st/adm/")) model.put("isView", Boolean.FALSE);
		model.put("isView", isView);
		return MoLayoutUtil.getJspPath("/wf/works/setImagePop");
	}
	
	/** [AJAX] 이미지 개별 삭제 */
	@RequestMapping(value = {"/wf/works/transImgDelAjx", "/wf/adm/works/transImgDelAjx"})
	public String transSiteImgDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String formNo = (String) object.get("formNo");
			String workNo = (String) object.get("workNo");
			String imgNo = (String) object.get("imgNo");
			
			if (formNo==null || formNo.isEmpty() || workNo==null || workNo.isEmpty() || imgNo==null || imgNo.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String compId = userVo.getCompId();
			WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, "Y");
			
			if(wfFormBVo==null || wfFormBVo.getGenId()==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			// 전사여부
			boolean isAllComp=wfFormBVo.getAllCompYn()!=null && "Y".equals(wfFormBVo.getAllCompYn());
			if(!isAllComp && !wfFormBVo.getCompId().equals(compId)){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			List<String> imgNoList=new ArrayList<String>();
			imgNoList.add(imgNo);
			
			// 이미지 삭제
			List<String> deleteFilePathList=wfFormSvc.deleteImgList(queryQueue, formNo, workNo, imgNoList);
			
			if(!queryQueue.isEmpty()){
				commonSvc.execute(queryQueue);
				// 실제 이미지 파일 삭제	
				wfFileSvc.deleteWebFiles(deleteFilePathList);
			}
			
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
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/wf/works/downFile","/wf/adm/works/downFile"})
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "formNo", required = true) String formNo,
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
				CommonFileVo fileVo = wfFileSvc.getFileVo(formNo, Integer.valueOf(fileId));
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
