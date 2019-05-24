package com.innobiz.orange.web.dm.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
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
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmDocNoSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmFileSvc;
import com.innobiz.orange.web.dm.svc.DmRescSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.svc.DmTaskSvc;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmBumkBVo;
import com.innobiz.orange.web.dm.vo.DmCatBVo;
import com.innobiz.orange.web.dm.vo.DmClsBVo;
import com.innobiz.orange.web.dm.vo.DmClsRVo;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.dm.vo.DmDocDVo;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmDocVerLVo;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.dm.vo.DmItemDispDVo;
import com.innobiz.orange.web.dm.vo.DmPubDocTgtDVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.dm.vo.DmSubmLVo;
import com.innobiz.orange.web.dm.vo.DmTaskHVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

@Controller
public class DmDocCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmDocCtrl.class);
    
    /** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 문서 서비스 */
	@Resource(name = "dmDocSvc")
	private DmDocSvc dmDocSvc;
	
	/** 관리 서비스 */
	@Resource(name = "dmAdmSvc")
	private DmAdmSvc dmAdmSvc;
	
	/** 파일 서비스 */
	@Resource(name = "dmFileSvc")
	private DmFileSvc dmFileSvc;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	/** 문서번호 서비스 */
	@Resource(name = "dmDocNoSvc")
	private DmDocNoSvc dmDocNoSvc;
	
	/** 포털 보안 서비스 */
	@Resource(name = "ptSecuSvc")
	private PtSecuSvc ptSecuSvc;
	
	/** 작업이력 서비스 */
	@Resource(name = "dmTaskSvc")
	private DmTaskSvc dmTaskSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "dmRescSvc")
	private DmRescSvc dmRescSvc;
	
//	/** 포탈 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
//	/** 캐쉬 만료 처리용 서비스 */
//	@Autowired
//	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 문서 목록 조회 */
	@RequestMapping(value = {"/dm/doc/listDoc","/dm/doc/listBumkDoc","/dm/doc/listNewDoc","/dm/doc/listOwnDoc","/dm/doc/listKprdDoc",
			"/dm/doc/listDiscDoc","/dm/doc/listSubmDoc","/dm/doc/listTmpDoc","/dm/doc/listRecycleDoc","/dm/adm/doc/listRecycleDoc",
			"/dm/adm/doc/listDoc","/dm/adm/doc/listKprdDoc",
			"/dm/doc/listTransferDoc","/dm/adm/doc/listTransferDoc","/dm/adm/doc/listTransferDtlDoc",
			"/dm/doc/listTransTgtDoc","/dm/doc/listTransWaitDoc","/dm/doc/listTakovrDoc",
			"/dm/adm/doc/listTransTgtDoc","/dm/adm/doc/listTransWaitDoc","/dm/adm/doc/listTakovrDoc",
			"/dm/doc/listOpenReqDoc","/dm/adm/doc/listOpenApvDoc"})
	public String listDoc(HttpServletRequest request,
			@RequestParam(value = "lstTyp", required = false) String lstTyp,
			@RequestParam(value = "paramCompId", required = false) String paramCompId,
			ModelMap model) throws Exception {
		
		// 관리자여부
		boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 요청경로 세팅
		String path = dmCmSvc.getRequestPath(request, model , "Doc");
				
		// 탭목록 세팅
		lstTyp = dmDocSvc.setTabList(model, path, lstTyp);
		
		// 사용자 설정이 없을경우 - 관리자 환경설정에서 조회
		Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
					
		// 목록보기
		if(lstTyp == null || lstTyp.isEmpty()) {
			lstTyp = envConfigMap.get("lstTyp");
		}
		
		// 탭ID
		if(lstTyp == null){
			lstTyp = "L";// 목록형 보기
		}
				
		model.put("orgId", userVo.getOrgId());
		model.put("lstTyp", lstTyp);
		
		//목록형이면 문서 목록 페이지 호출
		if("L".equals(lstTyp)) return listDocFrm(request, model);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, paramCompId, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
				
		// 폴더ID
		//String fldId = ParamUtil.getRequestParam(request, "fldId", false);
		
		// 분류체계 ID 가 있을 경우 조회조건에 추가한다.
		dmDocSvc.setDmClsBVoList(request, model, storId, new ArrayList<DmClsBVo>(), null, null, null, null, userVo.getLangTypCd());
				
		// URL 별 제외컬럼 목록
		//List<String> exDispList = new ArrayList<String>();
		
		// 요청 Url 별 조회조건 세팅[문서,최신,소유,보존연한]
		//dmDocSvc.setQueryUrlOptions(model, null, path, userVo, exDispList);
				
		// 검색항목 재정의
		List<DmItemDispDVo> dispList = dmDocSvc.getDmItemDispDList(request, storId, null, lstTyp, userVo.getCompId(), "list", false);
		model.put("srchDispList", dmDocSvc.getSrchDispDList(dispList, lstTyp, null, isAdmin));
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 권한 세팅
		dmDocSvc.getAuthMap(model, isAdmin, null, userVo, storId, langTypCd, path, false);
				
		// URL Suffix
		/*String suffix = dmCmSvc.getPathSuffix(path);
		if(!admUriChk && suffix.startsWith("Tran")) model.put("isTran", Boolean.TRUE);
		else model.put("isTran", Boolean.FALSE);*/
		
		String suffix = dmCmSvc.getPathSuffix(path);
		
		// 분류탭 일경우 하위문서정렬여부를 고정한다.
		if("C".equals(lstTyp) || !suffix.startsWith("Doc")){
			model.put("orderSubYn", "N");
		}
				
		// 이관문서조회
		if(suffix.startsWith("TransferDoc")){
			if(model != null) model.put("storList", dmStorSvc.getStorList(userVo.getLangTypCd(), userVo.getCompId(), true, "Y"));
		}
		// 폴더그룹ID - 인수인계 시에만 활성화(부서폴더만 표시)
		String fldGrpId = request.getRequestURI().startsWith("/dm/doc/") && (suffix.startsWith("TransTgt") || suffix.startsWith("TransWait") || suffix.startsWith("Takovr")) ? DmConstant.FLD_DEPT : null;
		if(fldGrpId != null) model.put("fldGrpId", fldGrpId);
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId","tgtId","paramStorId"));
		model.put("params", ParamUtil.getQueryString(request));
		
		model.put("isAdmin", isAdmin);
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request, "menuId", "lstTyp", "paramStorId"));
		
		return LayoutUtil.getJspPath("/dm/doc/listDoc");
	}
	
	
	/** [FRAME] 문서 목록 조회 - 오른쪽 화면*/
	@RequestMapping(value = {"/dm/doc/listDocFrm","/dm/doc/listBumkDocFrm","/dm/doc/listNewDocFrm","/dm/doc/listOwnDocFrm","/dm/doc/listKprdDocFrm",
			"/dm/doc/listDiscDocFrm","/dm/doc/listSubmDocFrm","/dm/doc/listTmpDocFrm","/dm/doc/listRecycleDocFrm","/dm/adm/doc/listRecycleDocFrm",
			"/dm/adm/doc/listDocFrm","/dm/adm/doc/listKprdDocFrm","/dm/bumk/listBumkDocFrm",
			"/dm/doc/listTransferDocFrm","/dm/adm/doc/listTransferDocFrm","/dm/adm/doc/listTransferDtlDocFrm",
			"/dm/doc/listTransTgtDocFrm","/dm/doc/listTransWaitDocFrm","/dm/doc/listTakovrDocFrm",
			"/dm/adm/doc/listTransTgtDocFrm","/dm/adm/doc/listTransWaitDocFrm","/dm/adm/doc/listTakovrDocFrm",
			"/dm/doc/listOpenReqDocFrm","/dm/adm/doc/listOpenApvDocFrm",
			"/dm/doc/listDocPltFrm","/dm/doc/listNewDocPltFrm","/dm/doc/listOwnDocPltFrm","/dm/doc/listKprdDocPltFrm","/dm/doc/listOpenReqDocPltFrm"})
	public String listDocFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		String path = dmCmSvc.getRequestPath(request, model , "Doc");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 포틀릿 여부
		boolean isPlt = path.endsWith("PltFrm");
		if(isPlt){
			String menuId=ParamUtil.getRequestParam(request, "menuId", false);
			if(menuId==null || menuId.isEmpty())			
				return LayoutUtil.getJspPath("/dm/plt/listDocPltFrm");
		}
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// URL Suffix
		String suffix = dmCmSvc.getPathSuffix(path);
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
				
		// 회사ID
		String paramCompId = dmDocSvc.getParamCompId(request, model, userVo, suffix, isAdmin);
		
		// 저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, paramCompId, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		// 저장소ID
		String storId = dmStorBVo.getStorId();
		
		// 조회조건 매핑
		DmDocLVo dmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
		VoUtil.bind(request, dmDocLVo);
		dmDocLVo.setQueryLang(langTypCd);
		
		String lstTyp = ParamUtil.getRequestParam(request, "lstTyp", false);
		if(isPlt) lstTyp = "L";
		else{
			// 탭목록 세팅
			lstTyp = dmDocSvc.setTabList(model, path, lstTyp);
			
			// 탭ID
			if(lstTyp == null){
				lstTyp = "L";// 목록형 보기
			}
		}
		
		// URL 별 제외컬럼 목록
		List<String> exDispList = new ArrayList<String>();
		
		// URL 별 추가컬럼 목록
		List<String> inDispList = new ArrayList<String>();
		
		// URL 별 제한컬럼 목록
		//List<String> fxDispList = new ArrayList<String>();
		
		// 분류탭 일경우 하위문서정렬여부를 고정한다.
		if("C".equals(lstTyp) || (!suffix.startsWith("Doc") && !suffix.startsWith("TransferDoc"))){
			dmDocLVo.setOrderSubYn("N");
			model.put("orderSubYn", "N");
		}
				
		// 요청 Url 별 조회조건 세팅[문서,최신,소유,보존연한]
		dmDocSvc.setQueryUrlOptions(request, model, dmDocLVo, suffix, userVo, exDispList, inDispList, isAdmin);
		// 조회조건 세팅 
		dmDocSvc.setListQueryOptions(request, langTypCd, dmDocLVo, lstTyp, userVo.getCompId(), userVo.getOrgId(), model, exDispList, inDispList, isAdmin);
		
		if(isPlt){ // 포틀릿
			if(suffix.startsWith("OpenReq")){ // 문서열람요청
				dmDocLVo.setViewReqStatCd("A"); // 승인
			}
		}
		// 테이블명
		String tableName = dmDocLVo.getTableName();
		
		// 카운트 조회
		Integer recodeCount = commonSvc.count(dmDocLVo);
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
			PersonalUtil.setFixedPaging(request, dmDocLVo, rowCnt, recodeCount);
		}else {
			PersonalUtil.setPaging(request, dmDocLVo, recodeCount);
		}
		
		model.put("recodeCount", recodeCount);
		// 레코드 조회
		if(recodeCount.intValue()>0){
			@SuppressWarnings("unchecked")
			List<DmDocLVo> dmDocLVoList = (List<DmDocLVo>)commonSvc.queryList(dmDocLVo);
			//model.put("dmDocLVoList", dmDocLVoList);
			
			// 맵으로 변환
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			dmDocSvc.setDocMapList(request, dmDocLVoList, storId, mapList, userVo, tableName, suffix);
			model.put("mapList", mapList);
		}
		
		// 관리자 또는 이관문서 일경우 체크박스 오픈
		if(isAdmin ||  ArrayUtil.isStartsWithArray(DmConstant.LIST_MULTI_SUFFIX, suffix)) model.put("multi", "Y");
		else model.put("multi", "N");
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId","tgtId","paramStorId"));
		model.put("params", ParamUtil.getQueryString(request));
		
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request, "menuId", "lstTyp", "paramStorId"));
				
		// 권한 세팅
		dmDocSvc.getAuthMap(model, isAdmin, null, userVo, storId, langTypCd, path, false);
		
		model.put("isAdmin", isAdmin);
		
		// 읽음표시 여부
		model.put("isReadChk", ArrayUtil.isStartsWithArray(DmConstant.READ_URL_SUFFIX, suffix));
		
		if(isPlt){//포틀릿
			return LayoutUtil.getJspPath("/dm/plt/listDocPltFrm");
		}else if(path.endsWith("Frm")){// 프레임
			model.put("pageSuffix", "Frm");
			return LayoutUtil.getJspPath("/dm/doc/listDocCont","Frm");
		}
		
		return LayoutUtil.getJspPath("/dm/doc/listDocCont");
	}
	
	/** [팝업] 문서 목록*/
	@RequestMapping(value = {"/dm/doc/findDocPop","/dm/adm/doc/findDocPop","/cm/doc/findDocPop"})
	public String findDocPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		model.put("params", ParamUtil.getQueryString(request));
		return LayoutUtil.getJspPath("/dm/doc/findDocPop");
	}
	
	/** [FRAME] 문서 목록 조회 - 팝업*/
	@RequestMapping(value = {"/dm/doc/findDocFrm","/dm/adm/doc/findDocFrm","/cm/doc/findDocFrm"})
	public String findDocFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 관리자여부
		boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		// 저장소ID
		String storId = dmStorBVo.getStorId();
		// 테이블명
		String tableName = dmStorBVo.getTblNm();
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 조회조건 매핑
		DmDocLVo dmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
		VoUtil.bind(request, dmDocLVo);
		dmDocLVo.setQueryLang(langTypCd);
		//dmDocLVo.setOrderBy(" SUBJ ASC");
		dmDocLVo.setDftYn("Y");// 기본여부
		dmDocLVo.setStatCd("C");// 상태코드[완료]
		dmDocLVo.setOrderSubYn("Y"); // 하위문서정렬		
		// 조회조건 세팅 
		dmDocSvc.setListQueryOptions(request, langTypCd, dmDocLVo, "F", userVo.getCompId(), userVo.getOrgId(), model, null, null, isAdmin);
		
		// 미분류는 조회안되게 처리
		String addWhere = " AND FLD_ID != '"+DmConstant.EMPTY_CLS+"'";
		if(dmDocLVo.getWhereSqllet() != null && !"".equals(dmDocLVo.getWhereSqllet())) dmDocLVo.setWhereSqllet(dmDocLVo.getWhereSqllet()+addWhere);
		else dmDocLVo.setWhereSqllet(addWhere);
		
		// 카운트 조회
		Integer recodeCount = commonSvc.count(dmDocLVo);
		PersonalUtil.setPaging(request, dmDocLVo, recodeCount);
		model.put("recodeCount", recodeCount);
		// 레코드 조회
		if(recodeCount.intValue()>0){
			@SuppressWarnings("unchecked")
			List<DmDocLVo> dmDocLVoList = (List<DmDocLVo>)commonSvc.queryList(dmDocLVo);
			for(DmDocLVo storedDmDocLVo : dmDocLVoList){
				// 폴더가 미분류일 경우에 '미분류'로 세팅한다.
				if(storedDmDocLVo.getFldId() != null && DmConstant.EMPTY_CLS.equals(storedDmDocLVo.getFldId()))
					storedDmDocLVo.setFldNm(messageProperties.getMessage("dm.cols.emptyCls", request));
				// 분류체계 조회
				dmDocSvc.setDmClsBVoList(request, null, storId, new ArrayList<DmClsBVo>(), storedDmDocLVo, storedDmDocLVo.getDocGrpId(), "clsNm", tableName, langTypCd);
			}
			model.put("dmDocLVoList", dmDocLVoList);
		}
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId"));
		model.put("params", ParamUtil.getQueryString(request));
		
		return LayoutUtil.getJspPath("/dm/doc/findDocFrm");
	}
	
	/** 문서 등록 수정 화면 */
	@RequestMapping(value = {"/dm/doc/setDoc","/dm/doc/setDocFrm","/dm/doc/setBumkDocFrm","/dm/doc/setNewDoc","/dm/doc/setNewDocFrm",
			"/dm/doc/setOwnDoc","/dm/doc/setOwnDocFrm","/dm/doc/setKprdDoc","/dm/doc/setKprdDocFrm",
			"/dm/doc/setDiscDoc","/dm/doc/setDiscDocFrm","/dm/doc/setSubmDoc","/dm/doc/setSubmDocFrm",
			"/dm/doc/setTmpDoc","/dm/doc/setTmpDocFrm","/dm/doc/setRecycleDoc","/dm/doc/setRecycleDocFrm",
			"/dm/adm/doc/setDoc","/dm/adm/doc/setDocFrm","/dm/adm/doc/setKprdDoc","/dm/adm/doc/setKprdDocFrm",
			"/dm/doc/setSubDoc","/dm/doc/setSubDocFrm","/dm/adm/doc/setTransferDtlDoc","/dm/adm/doc/setTransferDtlDocFrm"})
	public String setDoc(HttpServletRequest request,
			@RequestParam(value = "docId", required = false) String docId,
			@RequestParam(value = "docPid", required = false) String docPid,
			@RequestParam(value = "fldId", required = false) String fldId,
			ModelMap model) throws Exception {
		
		// 관리자여부
		boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
				
		// 요청경로 세팅
		String path = dmCmSvc.getRequestPath(request, model , "Doc");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		// 저장소ID
		String storId = dmStorBVo.getStorId();
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		//환경설정 로드
		//Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(model, userVo.getCompId());
		
		// 테이블명
		String tableName = dmStorBVo.getTblNm();
		
		DmDocLVo dmDocLVo = null;
		String docGrpId = null;
		if((docId != null && !docId.isEmpty()) || (docPid != null && !docPid.isEmpty())){
			// 조회
			dmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, docId, docPid, true);
			if(dmDocLVo == null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			/** [권한] Start */
			boolean admUriChk = request.getRequestURI().startsWith("/dm/adm/");
			Map<String,String> authMap = dmDocSvc.getAuthMap(model, admUriChk, dmDocLVo, userVo, storId, langTypCd, path, false);
			// 권한체크 제외 URL
			boolean isUrlChk = dmDocSvc.chkNotAuth(path);
			if(!admUriChk && !isUrlChk && !dmDocSvc.chkDocSeculCd(userVo, dmDocLVo, dmDocSvc.chkDocAuth(authMap, "update", path))){
				LOGGER.error("[ERROR] User Auth Check Fail - request path : "+path+"\tuserUid : "+userVo.getUserUid()+"\tauth : update");
				//cm.msg.noAuth=권한이 없습니다.
				model.put("messageCode", "cm.msg.noAuth");
				model.put("todo", "history.go(-1);");
				return LayoutUtil.getResultJsp();
			}
			fldId = dmDocLVo.getFldId();
			docGrpId = dmDocLVo.getDocGrpId();
			if(docId != null && !docId.isEmpty()){
				// 키워드 조회
				dmDocSvc.setDmKwdLVoList(request, model, null, dmDocLVo.getDocGrpId(), tableName);
				
				// 폴더가 미분류일 경우에 '미분류'로 세팅한다.
				if(dmDocLVo.getFldId() != null && DmConstant.EMPTY_CLS.equals(dmDocLVo.getFldId()))
					dmDocLVo.setFldNm(messageProperties.getMessage("dm.cols.emptyCls", request));
			}
			if(docPid != null && !docPid.isEmpty()){
				dmDocLVo = new DmDocLVo();//dmDocSvc.newDmDocLVo(dmStorBVo);
				VoUtil.bind(request, dmDocLVo);
				dmDocLVo.setDocPid(docPid);
				dmDocLVo.setFldId(fldId);
			}
		}else{
			dmDocLVo = new DmDocLVo();
			VoUtil.bind(request, dmDocLVo);
		}
		
		// 세팅할 문서ID - 계속등록
		String setDocId = ParamUtil.getRequestParam(request, "setDocId", false);
		if(setDocId != null && !setDocId.isEmpty()){
			// 조회
			DmDocLVo originVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, setDocId, null);
			fldId = originVo.getFldId();
			docGrpId = originVo.getDocGrpId();
			dmDocLVo.setDocKeepPrdCd(originVo.getDocKeepPrdCd()); // 보존연한코드
			dmDocLVo.setSeculCd(originVo.getSeculCd()); // 보안등급
		}
		if(docId == null || docId.isEmpty()){
			// 초기 소유자 세팅
			dmDocLVo.setOwnrNm(userVo.getUserNm());
			dmDocLVo.setOwnrUid(userVo.getUserUid());
		}
		
		// 폴더 정보 맵핑
		if(fldId != null && !fldId.isEmpty()){
			DmFldBVo dmFldBVo = new DmFldBVo(storId);
			dmFldBVo.setQueryLang(langTypCd);
			dmFldBVo.setFldId(fldId);
			dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
			if(dmFldBVo != null){
				dmDocLVo.setFldId(dmFldBVo.getFldId());
				dmDocLVo.setFldNm(dmFldBVo.getFldNm());
			}
		}
					
		// 분류체계 조회
		dmDocSvc.setDmClsBVoList(request, model, storId, new ArrayList<DmClsBVo>(), null, docGrpId, null, tableName, langTypCd);
					
		// 항목 정보 조회
		List<DmItemDispDVo> itemDispList = dmDocSvc.getDmItemDispDList(request, storId, dmDocLVo.getFldId(), null, userVo.getCompId(), null, false);
		model.put("itemDispMap", dmDocSvc.getDmItemDispDMap(itemDispList));
		
		// 즐겨찾기[개인] 조회
		List<DmBumkBVo> dmBumkBVoList = dmDocSvc.getDmBumkBVoList(langTypCd, "P", userVo, null);
		model.put("dmBumkBVoList", dmBumkBVoList);
		
		// 개인폴더 조회
		//List<DmFldBVo> dmFldBVoList = dmDocSvc.getDmFldBVoList(langTypCd, null, null, userVo.getUserUid(), null);
		//model.put("dmFldBVoList", dmFldBVoList);
		
		// 첨부파일 리스트 model에 추가
		dmFileSvc.putFileListToModel(docId, model, tableName, userVo.getCompId());
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});

		// 최대 본문 사이즈 model에 추가
		dmDocSvc.putBodySizeToModel(request, model);
		
		// 맵으로 변환
		model.put("dmDocLVoMap", VoUtil.toMap(dmDocLVo, null));
		
		model.put("params", ParamUtil.getQueryString(request));
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId","fldId","clsId"));
		
		model.put("isAdmin", isAdmin);
		
		// 프레임
		if(path.endsWith("Frm")) {
			model.put("pageSuffix", "Frm");
			return LayoutUtil.getJspPath("/dm/doc/setDoc","Frm");
		}
		//model.put("paramsForList", ParamUtil.getQueryString(request, "docId","fldId","clsId"));
		
		return LayoutUtil.getJspPath("/dm/doc/setDoc");
	}
	
	/** 문서 상세 화면 */
	@RequestMapping(value = {"/dm/doc/viewDoc","/dm/doc/viewDocFrm","/dm/doc/viewBumkDocFrm","/dm/doc/viewNewDoc","/dm/doc/viewNewDocFrm",
			"/dm/doc/viewOwnDoc","/dm/doc/viewOwnDocFrm","/dm/doc/viewKprdDoc","/dm/doc/viewKprdDocFrm",
			"/dm/doc/viewDiscDoc","/dm/doc/viewDiscDocFrm","/dm/doc/viewSubmDoc","/dm/doc/viewSubmDocFrm",
			"/dm/doc/viewTmpDoc","/dm/doc/viewTmpDocFrm","/dm/doc/viewRecycleDoc","/dm/doc/viewRecycleDocFrm",
			"/dm/adm/doc/viewRecycleDoc","/dm/adm/doc/viewRecycleDocFrm",
			"/dm/adm/doc/viewDoc","/dm/adm/doc/viewDocFrm","/dm/adm/doc/viewKprdDoc","/dm/adm/doc/viewKprdDocFrm",
			"/dm/doc/viewSubDoc","/dm/doc/viewSubDocFrm","/dm/bumk/viewBumkDocFrm",
			"/dm/doc/viewTransTgtDoc","/dm/doc/viewTransWaitDoc","/dm/doc/viewTakovrDoc","/dm/doc/viewTransTgtDocFrm","/dm/doc/viewTransWaitDocFrm","/dm/doc/viewTakovrDocFrm",
			"/dm/adm/doc/viewTransTgtDoc","/dm/adm/doc/viewTransWaitDoc","/dm/adm/doc/viewTakovrDoc","/dm/adm/doc/viewTransTgtDocFrm","/dm/adm/doc/viewTransWaitDocFrm","/dm/adm/doc/viewTakovrDocFrm",
			"/dm/doc/viewTransferDoc","/dm/doc/viewTransferDocFrm","/dm/adm/doc/viewTransferDoc","/dm/adm/doc/viewTransferDocFrm",
			"/dm/adm/doc/viewTransferDtlDoc","/dm/adm/doc/viewTransferDtlDocFrm",
			"/dm/doc/viewOpenReqDoc","/dm/doc/viewOpenReqDocFrm","/dm/adm/doc/viewOpenApvDoc","/dm/adm/doc/viewOpenApvDocFrm"})
	public String viewDoc(HttpServletRequest request,
			@RequestParam(value = "docId", required = false) String docId,
			@RequestParam(value = "docGrpId", required = false) String docGrpId,
			ModelMap model) throws Exception {
		
		if ((docId == null || docId.isEmpty()) && (docGrpId == null || docGrpId.isEmpty())) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		// 관리자여부
		boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
				
		// 요청경로 세팅
		String path = dmCmSvc.getRequestPath(request, model , "Doc");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 회사ID
		String paramCompId = ParamUtil.getRequestParam(request, "paramCompId", false);
				
		// 문서에 매핑되어 있는 저장소ID 조회
		String paramStorId = dmStorSvc.getStorId(docGrpId);
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, paramCompId, paramStorId, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 기본저장소가 아닐경우
		if(docGrpId != null && !docGrpId.isEmpty() && !"Y".equals(dmStorBVo.getDftYn()) && request.getRequestURI().startsWith("/dm/doc/viewDoc")){
			String menuId = ptSecuSvc.getSecuMenuId(userVo, "/dm/doc/listTransferDoc.do");
			String returnUrl = "/dm/doc/viewTransferDoc.do?menuId="+menuId+"&paramStorId="+dmStorBVo.getStorId()+"&docGrpId="+docGrpId;
			model.put("togo", returnUrl);
			return LayoutUtil.getResultJsp();
		}
		String storId = dmStorBVo.getStorId();
		
		String tableName = dmStorBVo.getTblNm();
		
		// URL Suffix
		String suffix = dmCmSvc.getPathSuffix(path);
		
		// 문서(DM_X000X_L) 테이블 - SELECT
		DmDocLVo newDmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
		// 요청 Url 별 조회조건 세팅[문서,최신,소유,보존연한]
		//dmDocSvc.setQueryUrlOptions(request, model, newDmDocLVo, suffix, userVo, null, null, isAdmin);
		
		// 대상ID - 열람요청
		String tgtId = ParamUtil.getRequestParam(request, "tgtId", false);
		if(tgtId != null && !tgtId.isEmpty()) newDmDocLVo.setTgtId(tgtId);
		/*newDmDocLVo.setDftYn(null);
		newDmDocLVo.setStatCd(null);
		newDmDocLVo.setDiscStatCd(null);*/
		// 즐겨찾기ID
		String bumkId = ParamUtil.getRequestParam(request, "bumkId", false);
		if(bumkId != null && !bumkId.isEmpty()) newDmDocLVo.setBumkId(bumkId);
		// 문서정보 조회
		//DmDocLVo dmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, docId, docGrpId);
		DmDocLVo dmDocLVo = dmDocSvc.getDmDocLVo(newDmDocLVo, langTypCd, docId, docGrpId, false);
		if(dmDocLVo == null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		if(docId==null) docId=dmDocLVo.getDocId();
		// 상세정보 URL 체크
		boolean viewUrlChk = dmDocSvc.viewChkUrlOptions(request, model, dmDocLVo.getDocGrpId(), userVo, path);
		if(!viewUrlChk){
			return LayoutUtil.getResultJsp();
		}
		
		/** [권한] Start */
		Map<String,String> authMap = dmDocSvc.getAuthMap(model, isAdmin, dmDocLVo, userVo, storId, langTypCd, path, false);
		
		// 권한체크 제외 URL
		boolean isUrlChk = dmDocSvc.chkNotAuth(path);
		
		if(!isAdmin && !isUrlChk && !dmDocSvc.chkDocSeculCd(userVo, dmDocLVo, dmDocSvc.chkDocAuth(authMap, "view", path))){
			// 관리자 환경설정에서 이력대상코드 조회
			Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());

			// 열람조회요청여부
			String dtlViewOpt = envConfigMap == null ? "N" : envConfigMap.get("dtlViewOpt");
			if(dtlViewOpt==null) dtlViewOpt = "N";
			
			// 열람조회요청여부가 'Y' 이면
			if("Y".equals(dtlViewOpt)) {
				// 문서그룹ID
				String paramDocGrpId = dmDocLVo.getDocGrpId();
				
				String today = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
				// 현재날짜 기준 열람조회요청건이 있는지 체크
				DmPubDocTgtDVo dmPubDocTgtDVo = new DmPubDocTgtDVo();
				dmPubDocTgtDVo.setDocGrpId(paramDocGrpId);
				dmPubDocTgtDVo.setDurCat("readDt");
				dmPubDocTgtDVo.setDurStrtDt(today);
				dmPubDocTgtDVo.setDurEndDt(today);
				
				// 요청여부 체크
				boolean isReqChk = dmDocSvc.isViewReqChk(dmPubDocTgtDVo, userVo);
				if(isReqChk){
					// dm.msg.dtlView.request.cmpl=열람조회를 요청한 문서입니다. 
					model.put("messageCode", "dm.msg.dtlView.request.cmpl");
					model.put("todo", "if(opener != undefined) window.close(); else history.go(-1);");
					return LayoutUtil.getResultJsp();
				}
				model.put("docGrpId", paramDocGrpId);
				// dm.cfrm.dtlView.noAuth=권한이 없습니다.\n열람요청을 하시겠습니까?
				//model.put("messageCode", "dm.cfrm.dtlView.noAuth");
				// 프레임
				if(path.endsWith("Frm")) return LayoutUtil.getJspPath("/dm/doc/viewDocReqCfrm","Frm");
				return LayoutUtil.getJspPath("/dm/doc/viewDocReqCfrm");
			}
			LOGGER.error("[ERROR] User Auth Check Fail - request path : "+path+"\tuserUid : "+userVo.getUserUid()+"\tauth : view");
			model.put("todo", "if(opener != undefined) window.close(); else history.go(-1);");
			//cm.msg.noAuth=권한이 없습니다.
			model.put("messageCode", "cm.msg.noAuth");
			
			return LayoutUtil.getResultJsp();
		}
		/** [권한] End */
		
		// lobHandler Select
		model.put("lobHandler", lobHandler.create(
				"SELECT CONT FROM "+dmDocSvc.getFullTblNm(tableName)+" WHERE DOC_ID = ?", 
				new String[]{docId}
		));
		
		//버전목록 조회
		model.put("dmDocVerLVoList", dmDocSvc.getDocVerLVoList(dmDocLVo.getDocGrpId(), null, "VER_VA DESC", tableName));
		
		// 폴더가 미분류일 경우에 '미분류'로 세팅한다.
		if(dmDocLVo.getFldId() != null && DmConstant.EMPTY_CLS.equals(dmDocLVo.getFldId()))
			dmDocLVo.setFldNm(messageProperties.getMessage("dm.cols.emptyCls", request));
		
		// 최신(기본)버전 문서가 아닌경우 문서그룹ID로 기본문서의 ID를 조회한다.
		if("N".equals(dmDocLVo.getDftYn())){
			DmDocLVo dftDmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, null, dmDocLVo.getDocGrpId());
			if(dftDmDocLVo != null){
				model.put("refDocId", dftDmDocLVo.getDocId());
			}
		}
		
		// 상위 문서 조회 - 문서 삭제시 이동할 페이지
		if("Y".equals(dmDocLVo.getSubYn())){
			DmDocLVo parentVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, null, dmDocLVo.getDocPid());
			if(parentVo != null && dmDocLVo.getStatCd().equals(parentVo.getStatCd())){
				model.put("docPid", parentVo.getDocId());
			}
		}
		
		// URL별 상세보기 옵션
		dmDocSvc.viewUrlOptions(model, storId, dmDocLVo, path, userVo, isAdmin);
		
		// 목록구분
		String lstTyp = ParamUtil.getRequestParam(request, "lstTyp", false);
		
		// 탭ID
		//if(lstTyp == null) lstTyp = "L";// 목록형 보기
				
		//추가항목 조회
		List<DmItemDispDVo> itemDispList = dmDocSvc.getDmItemDispDList(request, storId, dmDocLVo.getFldId(), "F", userVo.getCompId(), "view", false);
		model.put("itemDispMap", dmDocSvc.getDmItemDispDMap(itemDispList));//
		
		model.put("itemDispList", dmDocSvc.getDmItemDispDList(itemDispList, "Y"));
		
		// 키워드 조회
		dmDocSvc.setDmKwdLVoList(request, model, null, dmDocLVo.getDocGrpId(), tableName);
		
		// 분류체계 조회
		dmDocSvc.setDmClsBVoList(request, model, storId, new ArrayList<DmClsBVo>(), null, dmDocLVo.getDocGrpId(), null, tableName, langTypCd);
		
		if(bumkId != null && !bumkId.isEmpty()){
			// 즐겨찾기 저장 여부
			model.put("isBumkSave", dmDocSvc.getDmBumkBVoListCnt(storId, bumkId, "D", dmDocLVo.getDocGrpId())>0);
		}
		
		boolean isSubDocUse = model.containsKey("subDocListYn") && model.get("subDocListYn") == Boolean.TRUE;
		// 관련문서 사용여부
		if(isSubDocUse){
			// 관련문서 조회
			DmDocLVo parentVo = dmDocSvc.newDmDocLVo(dmStorBVo);
			model.put("subDocList", dmDocSvc.getSubDmDocLVoList(dmDocLVo, parentVo));
		}
		
		boolean naviYn = model.containsKey("naviYn") && model.get("naviYn") == Boolean.TRUE;
		// 이전다음 사용여부
		if(naviYn){
			// 이전다음 조회
			DmDocLVo naviVo = dmDocSvc.newDmDocLVo(dmStorBVo);
			VoUtil.bind(request, naviVo);
			naviVo.setQueryLang(langTypCd);
			naviVo.setDftYn("Y");
			
			// 하위문서정렬여부를 고정한다.
			if(!suffix.startsWith("Doc") && !suffix.startsWith("TransferDoc")){
				naviVo.setOrderSubYn("N");
			}
			
			// 하위포함 여부
			//if(naviVo.getWithSubYn() != null && "N".equals(naviVo.getWithSubYn()))
			//	naviVo.setSubYn("N");
					
			// 요청 Url 별 조회조건 세팅[문서,최신,소유,보존연한]
			dmDocSvc.setQueryUrlOptions(request, model, naviVo, suffix, userVo, null, null, isAdmin);
					
			// 조회조건 세팅 
			dmDocSvc.setListQueryOptions(request, langTypCd, naviVo, lstTyp, userVo.getCompId(), userVo.getOrgId(), null, null, null, isAdmin);
			
			// 상태코드가 동일한 문서만 조회
			//naviVo.setStatCd(dmDocLVo.getStatCd());
			/*if(!dmDocSvc.isParentTop(dmDocLVo)){
				//naviVo.setSubYn("Y");
				naviVo.setSortDpth(dmDocLVo.getSortDpth());
				naviVo.setSubDocGrpId(dmDocLVo.getSubDocGrpId());
				//naviVo.setOrderBy(" SORT_ORDR ASC");
			}else{			
				//naviVo.setSubYn("N");
			}*/
			
			naviVo.setDocId(docId);
			naviVo.setDocGrpId(null);
			// 이전다음 문서ID 조회
			naviVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocLDao.selectNaviDmDocL");
			
			naviVo = (DmDocLVo)commonSvc.queryVo(naviVo);
			if(naviVo != null){
				// 이전문서
				if(naviVo.getPrevDocId() != null && !naviVo.getPrevDocId().isEmpty())
					model.put("prevVo", dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, naviVo.getPrevDocId(), null));
				// 다음문서
				if(naviVo.getNextDocId() != null && !naviVo.getNextDocId().isEmpty())
					model.put("nextVo", dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, naviVo.getNextDocId(), null));
			}
		}
		
		// 첨부파일 리스트 model에 추가
		dmFileSvc.putFileListToModel(docId, model, tableName, userVo.getCompId());
		
		// 맵으로 변환
		model.put("dmDocLVoMap", VoUtil.toMap(dmDocLVo, null));
		
		model.put("params", ParamUtil.getQueryString(request));
		
		// 문서가 완료 상태('C') 면서 출처가 결재 일 경우
		if(dmDocLVo.getStatCd() != null && "C".equals(dmDocLVo.getStatCd()) && dmDocLVo.getRefTyp() != null && "ap".equals(dmDocLVo.getRefTyp())){
			// RSA 암호화 스크립트 추가 - 문서 비밀번호 확인용
			model.addAttribute("JS_OPTS", new String[]{"pt.rsa"});
		}
		
		model.put("isAdmin", isAdmin);
		
		// 작업 이력저장[사용자]		
		if(!isAdmin && ArrayUtil.isStartsWithArray(DmConstant.READ_URL_SUFFIX, suffix)){
			String taskCd = "view";
			// 관리자 환경설정에서 이력대상코드 조회
			Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
			// 관리자 설정에서 작업구분을 체크하고 작업이력 테이블에 중복데이터가 있는지 체크
			if(dmTaskSvc.getTaskCdChk(envConfigMap, userVo.getCompId(), taskCd)){
				// insert or update or null
				boolean isReadHst = dmTaskSvc.isChkReadHst(envConfigMap, langTypCd, tableName, dmDocLVo.getDocGrpId(), userVo.getUserUid());
				if(!isReadHst){
					// 조회수 증가
					dmDocSvc.addReadCnt(tableName, dmDocLVo.getDocGrpId());
				}
			}
		}
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
				
		// 프레임
		if(path.endsWith("Frm")) {
			model.put("pageSuffix", "Frm");
			model.put("paramsForList", ParamUtil.getQueryString(request, "docId"));
			return LayoutUtil.getJspPath("/dm/doc/viewDoc","Frm");
		}
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId","fldId","clsId","docGrpId"));
		
		return LayoutUtil.getJspPath("/dm/doc/viewDoc");
	}
	
	/** [AJAX] 열람여부 조회 */
	@RequestMapping(value = {"/dm/doc/getViewOptAjx","/dm/adm/doc/getViewOptAjx"})
	public String getViewOptAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 권한 세팅
			boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
			if(isAdmin) {
				model.put("result", "ok");
				return JsonUtil.returnJson(model);
			}
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String docGrpId = (String) object.get("docGrpId");
			String viewPage = (String) object.get("viewPage");
			if ( docGrpId == null || docGrpId.isEmpty() || viewPage == null || viewPage.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 문서에 매핑되어 있는 저장소ID 조회
			String paramStorId = dmStorSvc.getStorId(docGrpId);
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, paramStorId, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			DmDocLVo dmDocLVo = dmDocSvc.getDmDocLVo("ko", dmStorBVo, null, docGrpId);
			if(dmDocLVo == null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			/** [권한] Start */
			Map<String,String> authMap = dmDocSvc.getAuthMap(model, isAdmin, dmDocLVo, userVo, storId, "ko", "/viewDoc", false);
			
			// 권한체크 제외 URL
			boolean isUrlChk = dmDocSvc.chkNotAuth(viewPage);
			if(!isAdmin && !isUrlChk && !dmDocSvc.chkDocSeculCd(userVo, dmDocLVo, dmDocSvc.chkDocAuth(authMap, "view", viewPage))){
				// 관리자 환경설정
				Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
							
				// 열람조회요청여부
				String dtlViewOpt = envConfigMap == null ? "N" : envConfigMap.get("dtlViewOpt");
				if(dtlViewOpt==null) dtlViewOpt = "N";
				if("Y".equals(dtlViewOpt)) {
					String today = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
					
					// 현재날짜 기준 열람조회요청건이 있는지 체크
					DmPubDocTgtDVo dmPubDocTgtDVo = new DmPubDocTgtDVo();
					dmPubDocTgtDVo.setDocGrpId(docGrpId);
					dmPubDocTgtDVo.setDurCat("readDt");
					dmPubDocTgtDVo.setDurStrtDt(today);
					dmPubDocTgtDVo.setDurEndDt(today);
					
					// 요청여부 체크
					boolean isReqChk = dmDocSvc.isViewReqChk(dmPubDocTgtDVo, userVo);
			
					if(isReqChk){
						// dm.msg.dtlView.request.cmpl=열람조회를 요청한 문서입니다. 
						throw new CmException("dm.msg.dtlView.request.cmpl", request);
					}
					
					model.put("popYn", "Y");
					//dm.cfrm.dtlView.noAuth=권한이 없습니다.\n열람요청을 하시겠습니까?
					model.put("message", messageProperties.getMessage("dm.cfrm.dtlView.noAuth", request));
				}
				else {
					model.put("popYn", "N");
					//cm.msg.noAuth=권한이 없습니다.
					model.put("message", messageProperties.getMessage("cm.msg.noAuth", request));
				}
				
				return JsonUtil.returnJson(model);
			}
			
			/** [권한] End */
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [팝업] 문서열람 요청 */
	@RequestMapping(value = {"/dm/doc/viewDocReqCfrmPop","/dm/adm/doc/viewDocReqCfrmPop"})
	public String setDocViewReqPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		// 관리자 환경설정
		Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
		
		// 기간선택여부
		String dtlViewPrdAllow = envConfigMap.get("dtlViewPrdAllow");
		if(dtlViewPrdAllow == null) dtlViewPrdAllow = "N";
		model.put("dtlViewPrdAllow", dtlViewPrdAllow);
		
		// 열람기간
		String dtlViewPrdSelect = envConfigMap.get("dtlViewPrdSelect");
		if(dtlViewPrdSelect == null) dtlViewPrdSelect = "1";
		
		// 열람기간 단위
		String dtlViewPrdUnit = envConfigMap.get("dtlViewPrdUnit");
		if(dtlViewPrdUnit == null || "unlimited".equals(dtlViewPrdUnit)) dtlViewPrdUnit = "week";
		
		String readStrtDt = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
		// 시작일자
		model.put("readStrtDt", readStrtDt);
		// 종료일자 - 시작일 기준으로 일주일 후를 세팅한다.
		model.put("readEndDt", dmDocSvc.getDateOfDay(readStrtDt, dtlViewPrdUnit, "p", null, Integer.parseInt(dtlViewPrdSelect)));
		return LayoutUtil.getJspPath("/dm/doc/viewDocReqCfrmPop");
	}
	
	/** [AJAX] 문서열람 요청 저장 */
	@RequestMapping(value = "/dm/doc/transViewDocReqAjx")
	public String transViewDocReqAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String docGrpId = (String) object.get("docGrpId"); // 문서그룹ID
			String reqTgtTyp = (String) object.get("reqTgtTyp"); // 열람요청대상
			if ( docGrpId == null || docGrpId.isEmpty() || reqTgtTyp == null || reqTgtTyp.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 문서에 매핑되어 있는 저장소ID 조회
			String paramStorId = dmStorSvc.getStorId(docGrpId);
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, paramStorId, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			DmPubDocTgtDVo dmPubDocTgtDVo = new DmPubDocTgtDVo();
			dmPubDocTgtDVo.setDocGrpId(docGrpId);
			dmPubDocTgtDVo.setDurCat("readDt");
			dmPubDocTgtDVo.setTgtTypCd(reqTgtTyp);
			dmPubDocTgtDVo.setTgtId("D".equals(reqTgtTyp) ? userVo.getOrgId() : userVo.getUserUid()); // dept:부서, user:사용자
			dmPubDocTgtDVo.setStatCd("A"); // 승인
			
			// 관리자 환경설정
			Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
			
			// 기간선택여부 - 'N'이면 열람기간을 세팅한다.
			String dtlViewPrdAllow = envConfigMap.get("dtlViewPrdAllow");
			if(dtlViewPrdAllow == null) dtlViewPrdAllow = "N";
			if("N".equals(dtlViewPrdAllow)){
				// 열람기간
				String dtlViewPrdSelect = envConfigMap.get("dtlViewPrdSelect");
				if(dtlViewPrdSelect == null) dtlViewPrdSelect = "1";
				
				// 열람기간 단위
				String dtlViewPrdUnit = envConfigMap.get("dtlViewPrdUnit");
				if(dtlViewPrdUnit == null) dtlViewPrdUnit = "week";
				if(!"unlimited".equals(dtlViewPrdUnit)){
					String readStrtDt = commonSvc.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
					// 시작일자
					dmPubDocTgtDVo.setReadStrtDt(readStrtDt);
					// 종료일자 - 시작일 기준으로 일주일 후를 세팅한다.
					dmPubDocTgtDVo.setReadEndDt(dmDocSvc.getDateOfDay(readStrtDt, dtlViewPrdUnit, "p", null, Integer.parseInt(dtlViewPrdSelect)));
					dmPubDocTgtDVo.setDurStrtDt(readStrtDt);
					dmPubDocTgtDVo.setDurEndDt(dmPubDocTgtDVo.getReadEndDt());
				}
			}else{
				String readStrtDt = (String) object.get("readStrtDt"); // 열람시작일시
				String readEndDt = (String) object.get("readEndDt"); // 열람종료일시
				dmPubDocTgtDVo.setReadStrtDt(readStrtDt);
				dmPubDocTgtDVo.setReadEndDt(readEndDt);
				dmPubDocTgtDVo.setDurStrtDt(readStrtDt);
				dmPubDocTgtDVo.setDurEndDt(readEndDt);
				
			}
			
			if(commonSvc.count(dmPubDocTgtDVo)>0){
				// dm.msg.dtlView.request.dup=이미 요청한 문서입니다.
				throw new CmException("dm.msg.dtlView.request.dup", request);
			}
			
			dmPubDocTgtDVo.setStatCd("S"); // 요청
			
			if(commonSvc.count(dmPubDocTgtDVo)>0){
				// dm.msg.dtlView.request.dup=이미 요청한 문서입니다.
				throw new CmException("dm.msg.dtlView.request.dup", request);
			}
			
			dmPubDocTgtDVo.setRegrUid(userVo.getUserUid());
			dmPubDocTgtDVo.setRegDt("sysdate");
			
			
			queryQueue.store(dmPubDocTgtDVo);
			
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
	
	/** [팝업] 문서열람 승인|반려 - 화면출력*/
	@RequestMapping(value = {"/dm/doc/setOpenApvPop","/dm/adm/doc/setOpenApvPop"})
	public String setOpenApvPop(HttpServletRequest request,
			@Parameter(name="docGrpId", required=false) String docGrpId,
			@Parameter(name="tgtId", required=false) String tgtId,
			ModelMap model) throws Exception {
		
		// 문서그룹ID 와 대상ID가 있으면 상세정보를 조회한다.			
		if(docGrpId != null && !docGrpId.isEmpty() && tgtId != null && !tgtId.isEmpty()){
			DmPubDocTgtDVo dmPubDocTgtDVo = new DmPubDocTgtDVo();
			dmPubDocTgtDVo.setDocGrpId(docGrpId); // 문서그룹ID
			dmPubDocTgtDVo.setTgtId(tgtId); // 대상ID
			dmPubDocTgtDVo = (DmPubDocTgtDVo)commonSvc.queryVo(dmPubDocTgtDVo);
			if(dmPubDocTgtDVo != null) model.put("dmPubDocTgtDVo", dmPubDocTgtDVo);
		}
		return LayoutUtil.getJspPath("/dm/adm/doc/setOpenApvPop");
	}
	
	/** [AJAX] 문서열람 승인 저장 */
	@RequestMapping(value = "/dm/adm/doc/transOpenApvAjx")
	public String transOpenApvAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String statCd = (String) object.get("statCd"); // 상태코드
			String docGrpId = (String) object.get("docGrpId"); // 문서그룹ID
			String tgtId = (String) object.get("tgtId"); // 열람요청대상
			
			if ( statCd == null || statCd.isEmpty() || docGrpId == null || docGrpId.isEmpty() || tgtId == null || tgtId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				//return LayoutUtil.getResultJsp();
				throw new CmException("dm.msg.nodata.stor", request);
			}
						
			// 열람요청 문서그룹ID
			String[] docGrpIds = docGrpId.split(",");
			
			// 열람요청 대상ID
			String[] tgtIds = tgtId.split(",");
			
			if(docGrpIds.length != tgtIds.length){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 반려의견
			String rjtOpin = (String) object.get("rjtOpin");
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 이메일을 발송할 문서 목록			
			List<Map<String,String>> emailList = new ArrayList<Map<String,String>>();
			Map<String,String> emailMap = null;
			DmDocLVo dmDocLVo = null;
			DmPubDocTgtDVo dmPubDocTgtDVo = null, tmpVo;
			for(int i=0;i<docGrpIds.length;i++){
				dmPubDocTgtDVo = new DmPubDocTgtDVo();
				dmPubDocTgtDVo.setDocGrpId(docGrpIds[i]);
				dmPubDocTgtDVo.setTgtId(tgtIds[i]);
				tmpVo = (DmPubDocTgtDVo)commonSvc.queryVo(dmPubDocTgtDVo);
				if(tmpVo != null){
					dmDocLVo = dmDocSvc.getDmDocLVo("ko", dmStorBVo, null, docGrpIds[i]);
					if(dmDocLVo != null){
						emailMap = new HashMap<String,String>();
						emailMap.put("subj", dmDocLVo.getSubj());
						emailMap.put("regDt", tmpVo.getRegDt());
						emailMap.put("receiveUid", tmpVo.getRegrUid());
						emailList.add(emailMap);
					}
				}
				dmPubDocTgtDVo.setStatCd(statCd);
				if("R".equals(statCd) && rjtOpin != null && !rjtOpin.isEmpty()) dmPubDocTgtDVo.setRjtOpin(rjtOpin);
				else dmPubDocTgtDVo.setRjtOpin("");
				queryQueue.update(dmPubDocTgtDVo);
			}
			
			commonSvc.execute(queryQueue);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
						
			// 이메일 발송
			if(emailList != null && emailList.size()>0){
				// 세션의 사용자 정보
				UserVo userVo = LoginSession.getUser(request);
				for(Map<String,String> voMap : emailList){
					dmDocSvc.sendDiscDocEmail(request, langTypCd, voMap.get("subj"), statCd, voMap.get("receiveUid"), userVo, voMap.get("regDt"), rjtOpin, "view");
				}
			}
						
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
	
	/** [AJAX] 문서열람 취소 저장 */
	@RequestMapping(value = "/dm/adm/doc/transOpenCancelAjx")
	public String transOpenCancelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String docGrpId = (String) object.get("docGrpId"); // 문서그룹ID
			String tgtId = (String) object.get("tgtId"); // 열람요청대상
			
			if ( docGrpId == null || docGrpId.isEmpty() || tgtId == null || tgtId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 열람요청 문서그룹ID
			String[] docGrpIds = docGrpId.split(",");
			
			// 열람요청 대상ID
			String[] tgtIds = tgtId.split(",");
			
			if(docGrpIds.length != tgtIds.length){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			DmPubDocTgtDVo dmPubDocTgtDVo = null;
			for(int i=0;i<docGrpIds.length;i++){
				dmPubDocTgtDVo = new DmPubDocTgtDVo();
				dmPubDocTgtDVo.setDocGrpId(docGrpIds[i]);
				dmPubDocTgtDVo.setTgtId(tgtIds[i]);
				queryQueue.delete(dmPubDocTgtDVo);
			}
			
			commonSvc.execute(queryQueue);
			
			// dm.msg.dtlView.cancel.success=처리되었습니다.
			model.put("message", messageProperties.getMessage("dm.msg.dtlView.cancel.success", request));
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 심의여부 조회 */
	@RequestMapping(value = {"/dm/doc/getDiscYnAjx","/dm/adm/doc/getDiscYnAjx"})
	public String getDiscYnAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String fldId = (String) object.get("fldId");
			if ( fldId == null || fldId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				//return LayoutUtil.getResultJsp();
				throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			DmCatBVo dmCatBVo = dmDocSvc.getDmCatBVo(storId, fldId);
			if(dmCatBVo == null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 유형을 조회해서 심의여부를 리턴한다.
			if(dmCatBVo != null && dmCatBVo.getDiscYn() != null && !dmCatBVo.getDiscYn().isEmpty()){
				model.put("discYn", dmCatBVo.getDiscYn());
			}
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 환경설정 정보 로드[관리자,개인] */
	@RequestMapping(value = {"/dm/doc/getEnvConfigAjx","/dm/adm/doc/getEnvConfigAjx"})
	public String getEnvConfigAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mode = (String) object.get("mode");
			if ( mode == null || mode.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 환경설정 로드
			Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
			if(envConfigMap == null || envConfigMap.isEmpty()){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			model.put("envConfigMap", envConfigMap);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [팝업] 저장 - 문서번호,버전 등 조회 */
	@RequestMapping(value = {"/dm/doc/setDocCfrmPop","/dm/adm/doc/setDocCfrmPop"})
	public String setDocCfrmPop(HttpServletRequest request,
			@Parameter(name="docId", required=false) String docId,
			@Parameter(name="docPid", required=false) String docPid,
			@Parameter(name="fldId", required=false) String fldId,
			ModelMap model) throws Exception {
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 사용자 설정이 없을경우 - 관리자 환경설정에서 조회
		Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(model, userVo.getCompId());
		if(envConfigMap == null || envConfigMap.isEmpty()){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		//DmDocLVo dmDocLVo = dmDocSvc.getDmDocLVo(request, dmStorBVo, docId);
		
		//문서 VO 생성
		//DmDocLVo dmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
		
		
		DmDocLVo dmDocLVo = null;
		// 신규 버전 조회
		if(docId != null && !docId.isEmpty()){
			// 문서 조회
			dmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, docId, null);
			if(dmDocLVo == null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			fldId = dmDocLVo.getFldId();
			
			if(!"C".equals(dmDocLVo.getStatCd())){
				// 문서 생성
				//dmDocLVo = new DmDocLVo();
				dmDocLVo.setVerVa(envConfigMap.get("verDft"));
			}
			if(dmDocLVo.getDocNo() == null || dmDocLVo.getDocNo().isEmpty()){
				dmDocNoSvc.setDocNo(dmDocLVo, storId, langTypCd, "W", dmStorBVo.getCompId(), dmDocLVo.getFldId(), dmDocLVo.getRegrUid());
			}
		}else{
			String docNoPrefix = null;
			
			if(docPid != null && !docPid.isEmpty()){
				DmDocLVo parentVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, null, docPid);
				if(parentVo != null && parentVo.getDocNo() != null && !parentVo.getDocNo().isEmpty()){
					docNoPrefix = parentVo.getDocNo();
				}
			}
			
			// 문서 조회
			dmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
			if(docNoPrefix != null)
				dmDocLVo.setDocNo(docNoPrefix);
			else{
				dmDocNoSvc.setDocNo(dmDocLVo, storId, langTypCd, "W", dmStorBVo.getCompId(), fldId, userVo.getUserUid());
			}
			dmDocLVo.setVerVa(envConfigMap.get("verDft"));
		}
		
		model.put("dmDocLVo", dmDocLVo);
		
		return LayoutUtil.getJspPath("/dm/doc/setDocCfrmPop");
	}
	
	/** [FRAME] 추가항목 조회 */
	@RequestMapping(value = {"/dm/doc/listAddItemFrm","/dm/adm/doc/listAddItemFrm","/dm/doc/listPsnAddItemFrm","/cm/doc/listAddItemFrm"})
	public String listAddItemFrm(HttpServletRequest request,
			@Parameter(name="fldId", required=false) String fldId,
			@Parameter(name="docId", required=false) String docId,
			@Parameter(name="docPid", required=false) String docPid,
			@Parameter(name="docTyp", required=false) String docTyp,
			ModelMap model) throws Exception {
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String path = dmCmSvc.getRequestPath(request, model , "Doc");
		
		// 공통문서여부[복사원본문서가 공통문서인지 여부]
		boolean isCmDoc = ((docTyp == null || docTyp.isEmpty() || "doc".equals(docTyp)) && !path.startsWith("listPsn"))  || ("doc".equals(docTyp) && path.startsWith("listPsn")) || (docPid != null && !docPid.isEmpty());
		String storId = dmStorBVo.getStorId();
		boolean isPsn = path.startsWith("listPsn");
		if((docId != null && !docId.isEmpty()) || (docPid != null && !docPid.isEmpty())){
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 하위문서 보내기일 경우 해당문서ID 초기화
			if(docPid != null && !docPid.isEmpty()) docId = null;
			DmDocLVo dmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, isCmDoc ? dmStorBVo : null, docId, docPid);
			if(dmDocLVo == null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			fldId = dmDocLVo.getFldId();
			// 맵으로 변환
			model.put("dmDocLVoMap", VoUtil.toMap(dmDocLVo, null));
		}
		
		if(fldId == null || fldId.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		if(fldId != null && !fldId.isEmpty()){
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			String dispCol = docId != null && !docId.isEmpty() ? "mod" : "reg";
			List<DmItemDispDVo> itemDispList = dmDocSvc.getDmItemDispDList(request, storId, fldId, null, userVo.getCompId(), dispCol, isPsn);
			model.put("itemDispList", dmDocSvc.getDmItemDispDList(itemDispList, "Y"));
			
			DmCatBVo dmCatBVo = dmDocSvc.getDmCatBVo(storId, fldId);
			if(dmCatBVo != null){
				// 유형을 조회해서 심의여부를 리턴한다.
				if(dmCatBVo != null && dmCatBVo.getDiscYn() != null && !dmCatBVo.getDiscYn().isEmpty()){
					model.put("discYn", dmCatBVo.getDiscYn());
				}
			}
			/*			
			DmFldBVo dmFldBVo = new DmFldBVo(storId);
			dmFldBVo.setFldId(fldId);
			dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
			
			if(dmFldBVo != null) {
				dmDocSvc.setItemDispSort(addItemDispList);
				model.put("itemDispList", addItemDispList);
				model.put("itemDispList", dmAdmSvc.getDmCatDispDVoList(request, dmStorBVo.getStorId(), dmFldBVo.getCatId(), true, "Y", dispCol, "Y"));
			}*/
		}
		//else model.put("itemDispList", dmAdmSvc.getDftItemDispList(request, false, "Y", dispCol));
				
		return LayoutUtil.getJspPath("/dm/doc/listAddItemFrm");
	}
	
	/** 문서 저장 */
	@RequestMapping(value = {"/dm/doc/transDoc","/dm/doc/transDocFrm","/dm/doc/transBumkDocFrm","/dm/doc/transNewDoc","/dm/doc/transNewDocFrm",
			"/dm/doc/transOwnDoc","/dm/doc/transOwnDocFrm","/dm/doc/transKprdDoc","/dm/doc/transKprdDocFrm",
			"/dm/doc/transTmpDoc","/dm/doc/transTmpDocFrm","/dm/adm/doc/transDoc","/dm/adm/doc/transDocFrm",
			"/dm/doc/transSubDoc","/dm/doc/transSubDocFrm",
			"/dm/adm/doc/transTransferDtlDoc","/dm/adm/doc/transTransferDtlDocFrm"}, 
			method = RequestMethod.POST)
	public String transDoc(HttpServletRequest request,
			ModelMap model) {
		
		UploadHandler uploadHandler = null;
		
		try {
			// Multipart 파일 업로드
			uploadHandler = dmFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 요청경로 세팅
			String path = dmCmSvc.getRequestPath(request, model , "Doc");
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String tableName = dmStorBVo.getTblNm();
			String storId = dmStorBVo.getStorId();
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			String docId = ParamUtil.getRequestParam(request, "docId", false);
			
			boolean admUriChk = request.getRequestURI().startsWith("/dm/adm/");
			
			DmDocLVo newDmDocLVo = null;
			String regrUid = null;
			String regDt = null;
			String statCd = null;
			// 수정시에 권한 체크
			if(docId != null && !docId.isEmpty()){
				newDmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, docId, null);
				if(newDmDocLVo == null){
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
				
				//상태코드 체크[이관중인문서 수정시 제외]
				if(!request.getRequestURI().startsWith("/dm/adm/doc/transTransferDtlDoc"))				
					dmDocSvc.chkDocStatCd(request, "update", newDmDocLVo.getStatCd());
				
				/** [권한] Start */				
				Map<String,String> authMap = dmDocSvc.getAuthMap(null, admUriChk, newDmDocLVo, userVo, storId, langTypCd, path, false);
				// 권한체크 제외 URL
				boolean isUrlChk = dmDocSvc.chkNotAuth(path);
				if(!admUriChk && !isUrlChk && !dmDocSvc.chkDocSeculCd(userVo, newDmDocLVo, dmDocSvc.chkDocAuth(authMap, "update", path))){
					LOGGER.error("[ERROR] User Auth Check Fail - request path : ./transDoc.do \tuserUid : "+userVo.getUserUid()+"\tauth : update");
					//cm.msg.noAuth=권한이 없습니다.
					String message = messageProperties.getMessage("cm.msg.noAuth", request);
					model.put("message", message);
					return LayoutUtil.getResultJsp();
				}
				/** [권한] End */
				regDt = newDmDocLVo.getRegDt();
				regrUid = newDmDocLVo.getRegrUid();
				statCd = newDmDocLVo.getStatCd();
			}
			// 부모문서ID
			String docPid = ParamUtil.getRequestParam(request, "docPid", false);
			// 하위문서여부
			boolean isSub = docPid != null && !docPid.isEmpty() ? true : false;
			
			// 파일복사 목록
			List<DmCommFileDVo> copyFileList = new ArrayList<DmCommFileDVo>();
			
			// 문서그룹ID
			String docGrpId = ParamUtil.getRequestParam(request, "docGrpId", false);
				
			// 저장[기본정보]
			DmDocLVo dmDocLVo = dmDocSvc.saveDoc(request, queryQueue, dmStorBVo, null, userVo, docId, isSub, copyFileList);
			// 정상상태의 문서를 수정할 경우 해당코드를 세팅한다
			if(statCd != null && !statCd.isEmpty() && "C".equals(statCd)) dmDocLVo.setStatCd(statCd);
			if(dmDocLVo.getFldId() != null && !dmDocLVo.getFldId().isEmpty() && ( dmDocLVo.getStatCd() == null || dmDocLVo.getStatCd().isEmpty())){
				// 폴더유형중에 심의여부가 'Y' 이면 'S'[심의요청] 을 아니면 'C'[완료] 로 세팅한다.
				dmDocSvc.setDmStatCd(dmDocLVo, storId, dmDocLVo.getFldId());
			}
			//if(docGrpId == null || docGrpId.isEmpty()){
				// 상신목록 저장
			if(dmDocLVo.getStatCd() != null && !dmDocLVo.getStatCd().isEmpty() && "S".equals(dmDocLVo.getStatCd())){
				dmDocSvc.saveDisc(queryQueue, userVo.getCompId(), dmDocLVo.getDocGrpId(), "S", null, dmDocLVo.getDiscrUid(), userVo.getUserUid(), "insert");
			}
			//}
			// 임시저장 목록 저장 - 문서테이블에서 상태코드로 관리 하면서 필요없어짐
			/*if(dmDocLVo.getStatCd() != null && "T".equals(dmDocLVo.getStatCd())){
				dmDocSvc.saveTmpSave(queryQueue, userVo.getCompId(), dmDocLVo.getDocId(), userVo.getUserUid(), "store");
			}*/
						
			// 문서가 '완료' 상태이면서 문서번호가 없으면 문서번호를 부여한다.
			if(dmDocLVo.getStatCd() != null && "C".equals(dmDocLVo.getStatCd()) && ( dmDocLVo.getDocNo() == null || dmDocLVo.getDocNo().isEmpty())){
				if(regrUid == null) regrUid = userVo.getUserUid();
				//문서번호 세팅[등록:심의여부'N',수정:상태코드가'C']
				dmDocNoSvc.setDocNo(dmDocLVo, storId, langTypCd, "C", dmStorBVo.getCompId(), dmDocLVo.getFldId(), regrUid);
			}
			
			// 완료상태 문서일 경우 검색 색인 데이터 추가
			if("C".equals(dmDocLVo.getStatCd())){
				/** 검색 색인 데이터를 더함 */
				dmDocSvc.addSrchIndex(dmDocLVo.getDocGrpId(), userVo, queryQueue, docGrpId == null || docGrpId.isEmpty() ? "I" : "U");
			}
			
			// 등록일자 세팅
			if(docId != null && !docId.isEmpty() && regDt != null && dmDocLVo.getRegDt()==null) dmDocLVo.setRegDt(regDt);
			
			// 문서상세 저장[보존기한등]
			if(((docId == null || docId.isEmpty()) || (statCd != null && !statCd.isEmpty() && "T".equals(statCd))) && dmDocLVo.getStatCd() != null && "C".equals(dmDocLVo.getStatCd()))
				dmDocLVo.setCmplDt("sysdate");
			dmDocSvc.saveDmDocD(queryQueue, dmDocLVo, isSub ? dmDocLVo.getSubDocGrpId() : null);
			
			// 기본 테이블 저장 후 Url에 따른 추가 저장 옵션 실행[임시저장등]
			dmDocSvc.transUrlOptions(queryQueue, dmDocLVo, path, userVo);
			
			/** 부가정보 저장[분류체계,키워드,즐겨찾기] */
			if((docId == null || docId.isEmpty() ) || (docId != null && !docId.isEmpty() && !isSub)){
				// 분류체계 조회
				dmDocSvc.setDmClsBVoList(request, null, storId, new ArrayList<DmClsBVo>(), dmDocLVo, null, "clsNm", dmDocLVo.getTableName(), langTypCd);
				List<DmClsRVo> dmClsRVoList = dmDocLVo.getDmClsRVoList();
				// 키워드 조회
				String[] kwdList = dmDocSvc.getDmKwdLVoList(request, dmDocLVo, null, tableName);
				
				// 부가정보 삭제여부
				boolean isDel = docId != null && !docId.isEmpty();
				if(dmClsRVoList != null && dmClsRVoList.size()>0) dmDocSvc.saveDmClsRVoList(queryQueue, dmClsRVoList, dmDocLVo.getDocGrpId(), tableName, isDel);
				if(kwdList != null && kwdList.length>0) dmDocSvc.saveDmKwdLVoList(queryQueue, kwdList, dmDocLVo.getDocGrpId(), tableName, isDel);
				// 즐겨찾기관계 저장
				String bumkId = ParamUtil.getRequestParam(request, "bumkId", false);
				if((docId == null || docId.isEmpty()) &&bumkId != null && !bumkId.isEmpty())
					dmDocSvc.saveBumkDoc(request, queryQueue, storId, bumkId, "D", dmDocLVo.getDocGrpId(), false);
				
				// 보안등급코드
				String seculCd = ParamUtil.getRequestParam(request, "seculCd", false);
				// 관련문서가 있을 경우 기본정보를 변경해준다.(최상위문서를 수정하였을때)
				if(newDmDocLVo != null)	dmDocSvc.setDocDSync(queryQueue, newDmDocLVo, tableName, seculCd);
				
				/*if(docId != null && !docId.isEmpty() && !isSub){
					// 상세정보 조회
					DmDocLVo docVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, docId, null);
					DmDocLVo parentVo = dmDocSvc.newDmDocLVo(dmStorBVo);
					// 하위문서 조회(최상위문서포함) - 상위문서의 분류체계를 수정할 경우 하위문서도 바꿔주기 위함
					List<DmDocLVo> subList = dmDocSvc.getSubDmDocLVoList(docVo, parentVo);
					for(DmDocLVo storedDmDocLVo : subList){
						if(dmClsRVoList != null && dmClsRVoList.size()>0)
							dmDocSvc.saveDmClsRVoList(queryQueue, dmClsRVoList, storedDmDocLVo.getDocGrpId(), tableName, true);
						if(kwdList != null && kwdList.length>0)
							dmDocSvc.saveDmKwdLVoList(queryQueue, kwdList, storedDmDocLVo.getDocGrpId(), tableName, true);
					}
				}*/
			}
			
			// 파일 저장
			List<CommonFileVo> deletedFileList = dmFileSvc.saveDmFile(request, dmDocLVo.getDocId(), queryQueue, dmDocLVo.getTableName(), null);
						
			commonSvc.execute(queryQueue);
			
			// 상신목록 저장
			if("S".equals(dmDocLVo.getStatCd()))	{
				//이메일 발송
				dmDocSvc.sendDiscDocEmail(request, langTypCd, dmDocLVo.getSubj(), "S", dmDocLVo.getDiscrUid(), userVo, regDt, null, "disc");
			}
			
			// 작업 이력저장[사용자]
			if(!admUriChk){
				String taskCd = docId != null && !docId.isEmpty() ? "update" : "insert";
				// 관리자 설정에서 작업이력구분 체크
				if(dmTaskSvc.getTaskCdChk(null, userVo.getCompId(), taskCd)){
					// 작업상세 목록을 담을 배열[null or 상세배열] - 등록,수정은 버전/분류체계
					String[][] rescVals = null;
					if("update".equals(taskCd)) rescVals = new String[][]{{"dm.cols.verNo",dmDocLVo.getVerVa()}};
					dmTaskSvc.saveDmTask(null, tableName, langTypCd, dmDocLVo.getDocGrpId(), userVo.getUserUid(), taskCd, dmTaskSvc.getTaskMapList(rescVals));
				}
			}
			
			// 파일 삭제
			dmFileSvc.deleteDiskFiles(deletedFileList);
			
			// 파일 복사
			if(copyFileList.size()>0){
				dmFileSvc.copyFileList(request, "doc", copyFileList);
			}
			
			// 계속 등록페이지
			String setPage = ParamUtil.getRequestParam(request, "setPage", false);
			
			String page = null;
			if((docId == null || docId.isEmpty()) && setPage != null && !setPage.isEmpty()){
				page = setPage+(setPage.indexOf("?")>0 ? "&" : "?") + "setDocId="+dmDocLVo.getDocId();
				if(docPid != null && !docPid.isEmpty()) page+= "&docPid="+docPid;
			}else{
				String messageCode = "S".equals(dmDocLVo.getStatCd()) ? "dm.msg.save.disc.success" : "cm.msg.save.success";
				// cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage(messageCode, request));
				
				// 목록 여부 - 하위문서 신규작성시 부모문서 화면으로 이동
				boolean isListPage = (!isSub || (!"C".equals(dmDocLVo.getStatCd()) && isSub )) && (docId == null || docId.isEmpty()) ? true : false;
				// 등록일 경우 목록 화면, 수정일 경우 상세화면으로 이동
				page = isListPage ? listPage : viewPage;
				
			}
			
			if(path.endsWith("Frm"))
				model.put("todo", "parent.reloadDocFrm('"+page+"');");
			else
				model.put("todo", "parent.location.replace('" + page + "');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 심의 저장 */
	@RequestMapping(value = {"/dm/doc/transDiscDoc","/dm/doc/transDiscDocFrm",
			"/dm/adm/doc/transDiscDoc","/dm/adm/doc/transDiscDocFrm"}, method = RequestMethod.POST)
	public String transDiscDoc(HttpServletRequest request,
			ModelMap model) {
		
		try {
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			String docGrpId = ParamUtil.getRequestParam(request, "docGrpId", false);
			String statCd = ParamUtil.getRequestParam(request, "statCd", false);
			
			if(docGrpId == null || docGrpId.isEmpty() || statCd == null || statCd.isEmpty()){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 저장[기본정보]
			//dmDocSvc.saveDoc(request, queryQueue, dmStorBVo, userVo, docId, isSub);
			
			// 문서정보 조회
			DmDocLVo dmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, null, docGrpId);
			
			// 문서심의정보 조회
			DmSubmLVo dmSubmLVo = new DmSubmLVo();
			dmSubmLVo.setCompId(userVo.getCompId());
			dmSubmLVo.setDocGrpId(docGrpId);
			dmSubmLVo = (DmSubmLVo)commonSvc.queryVo(dmSubmLVo);
			if(dmSubmLVo == null || !userVo.getUserUid().equals(dmSubmLVo.getDiscrUid())){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 승인일 경우 문서상세[문서번호,보존기한등] 저장
			if("A".equals(statCd)){
				// 하위문서여부
				boolean isSub = dmDocLVo.getDocPid() != null && !dmDocLVo.getDocPid().isEmpty() ? true : false;
				
				// 문서 생성
				DmDocLVo newDmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
				newDmDocLVo.setStatCd("C");//완료로 변경
				newDmDocLVo.setDocGrpId(dmDocLVo.getDocGrpId());// 문서그룹ID
				newDmDocLVo.setDocKeepPrdCd(dmDocLVo.getDocKeepPrdCd()); // 문서보존기한코드
				
				//문서번호 세팅[등록:심의여부'N',수정:상태코드가'C']
				dmDocNoSvc.setDocNo(newDmDocLVo, storId, langTypCd, "C", userVo.getCompId(), dmDocLVo.getFldId(), dmDocLVo.getRegrUid());
				//setDocNo(dmDocLVo, userVo, isComp ? userVo.getCompId() : userVo.getOrgId());
				newDmDocLVo.setCmplDt("sysdate");
				
				if(isSub){
					// 부모문서정보 조회
					DmDocLVo parentVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, null, dmDocLVo.getDocPid());
					
					// 부모문서가 없거나 부모문서의 폴더ID와 승인된문서의 폴더ID가 다를경우 승인문서는 최상위 문서가 된다.
					if(parentVo == null || (parentVo != null && !parentVo.getFldId().equals(dmDocLVo.getFldId()))){
						isSub = false;
					}else{
						newDmDocLVo.setSortOrdr(dmDocSvc.getSortOrdr(dmStorBVo, parentVo, queryQueue)+"");
						newDmDocLVo.setSortDpth(parentVo.getSortDpth()+1);
						
						// 문서상세 저장[보존기한등]
						dmDocSvc.saveDmDocD(queryQueue, newDmDocLVo, dmDocLVo.getDocPid());
					}
				}
				
				if(!isSub){
					newDmDocLVo.setRegDt(dmDocLVo.getRegDt());
					newDmDocLVo.setSubYn("N");
					newDmDocLVo.setSubDocGrpId(docGrpId);
					newDmDocLVo.setDocPid("");
					newDmDocLVo.setSortOrdr("0");
					newDmDocLVo.setSortDpth(0);
					// 문서상세 저장[보존기한등]
					dmDocSvc.saveDmDocD(queryQueue, newDmDocLVo, null);
				}
				
				// 관리자 환경설정 조회
				Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, userVo.getCompId());
				// 기본버전 저장
				newDmDocLVo.setVerVa(envConfigMap.get("verDft"));
				// 버전 목록 저장
				dmDocSvc.saveDocVer(queryQueue, newDmDocLVo.getDocGrpId(), dmDocLVo.getDocId(), dmStorBVo.getTblNm(), newDmDocLVo.getVerVa(), "store");
				/** 검색 색인 데이터를 더함 */
				dmDocSvc.addSrchIndex(dmDocLVo.getDocGrpId(), userVo, queryQueue, "I");
			}else{
				DmDocLVo newDmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
				newDmDocLVo.setDocGrpId(dmDocLVo.getDocGrpId());
				newDmDocLVo.setStatCd(statCd);
				// 문서상세 저장[보존기한등]
				dmDocSvc.saveDmDocD(queryQueue, newDmDocLVo, null);
			}
			
			// 반려 의견
			String rjtOpin = ParamUtil.getRequestParam(request, "rjtOpin", false);
			
			// 상신저장
			dmDocSvc.saveDisc(queryQueue, userVo.getCompId(), docGrpId, statCd, rjtOpin, null, userVo.getUserUid(), "update");
			
			commonSvc.execute(queryQueue);
			
			//이메일 발송
			dmDocSvc.sendDiscDocEmail(request, langTypCd, dmDocLVo.getSubj(), statCd, dmSubmLVo.getRegrUid(), userVo, dmSubmLVo.getRegDt(), rjtOpin, "disc");
						
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			// 요청경로 세팅
			String path = dmCmSvc.getRequestPath(request, model , "Doc");
			
			if(path.endsWith("Frm")){
				if (docGrpId == null || docGrpId.isEmpty() || "R".equals(statCd)) {// 등록 또는 반려일 경우 목록 화면으로 이동
					model.put("todo", "parent.reloadDocFrm('"+listPage+"','list');");
				} else {// 수정일 경우 상세보기 화면으로 이동
					model.put("todo", "parent.reloadDocFrm('"+viewPage+"','view');");
				}
			}else{
				if (docGrpId == null || docGrpId.isEmpty() || "R".equals(statCd)) {// 등록 또는 반려일 경우 목록 화면으로 이동
					model.put("todo", "parent.location.replace('" + listPage + "');");
				} else {// 수정일 경우 상세보기 화면으로 이동
					model.put("todo", "parent.location.replace('" + viewPage + "');");
				}
			}
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 문서 번호 조회 */
	@RequestMapping(value = "/dm/doc/getDocNoAjx")
	public String getDocNoAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			//String message = null;

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String fldId = (String) object.get("fldId");
			if (fldId == null || fldId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			DmFldBVo dmFldBVo = new DmFldBVo(storId);
			dmFldBVo.setStorId(storId);
			dmFldBVo.setFldId(fldId);
			dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
			
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 문서 삭제 */
	@RequestMapping(value = {"/dm/doc/transDocDelAjx","/dm/adm/doc/transDocDelAjx"})
	public String transDocDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			String message = null;

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String docId = (String) object.get("docId");
			String docGrpId = (String) object.get("docGrpId");
			String statCd = (String) object.get("statCd");
			if ((statCd == null || statCd.isEmpty()) || ((docId == null || docId.isEmpty()) && (docGrpId == null || docGrpId.isEmpty()))) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String tableName = dmStorBVo.getTblNm();
			QueryQueue queryQueue = new QueryQueue();

			// DmDocLVo 초기화
			DmDocLVo dmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 삭제할 문서ID 배열
			List<String> delDocIdList = new ArrayList<String>();
			
			// 권한 세팅
			boolean isAdmin = false;
			
			// 관리자 여부
			boolean admUriChk = request.getRequestURI().startsWith("/dm/adm/");
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 그룹ID를 기준으로 문서ID 배열 추가
			if(docGrpId != null && !docGrpId.isEmpty()){
				dmDocLVo.setDocGrpId(docGrpId);
				// 버전 목록 조회
				List<DmDocVerLVo> dmDocVerLVoList = dmDocSvc.getDocVerLVoList(docGrpId, docId, "VER_VA DESC", dmDocLVo.getTableName());
				for(DmDocVerLVo storedDmDocVerLVo : dmDocVerLVoList){
					delDocIdList.add(storedDmDocVerLVo.getDocId());
				}
			}
			
			if("F".equals(statCd)){ // 완전삭제[폐기]
				List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
				dmDocSvc.delDoc(request, queryQueue, langTypCd, dmDocLVo, dmStorBVo, docGrpId, docId, userVo.getUserUid(), delDocIdList, deletedFileList, isAdmin);
				/** 검색 색인 데이터를 삭제 */
				dmDocSvc.addSrchIndex(docGrpId, userVo, queryQueue, "D");
				// 파일 삭제
				if(deletedFileList.size()>0)				
					dmFileSvc.deleteDiskFiles(deletedFileList);
			}else if("C".equals(statCd)){// 복원
				// 복원할 문서 목록맵
				Map<String,List<DmDocDVo>> addDocVoMap = new HashMap<String,List<DmDocDVo>>();
				
				// 문서 조회
				DmDocDVo dmDocDVo = new DmDocDVo();
				dmDocDVo.setTableName(tableName);
				dmDocDVo.setDocGrpId(docGrpId);
				DmDocDVo tmpDmDocDVo = (DmDocDVo)commonSvc.queryVo(dmDocDVo);
				// 하위문서그룹ID + '|' + 폴더ID 조합키
				String comKey = null;
				if(tmpDmDocDVo != null) {
					comKey = tmpDmDocDVo.getSubDocGrpId()+DmConstant.SPRIT+tmpDmDocDVo.getFldId();
					if(!addDocVoMap.containsKey(comKey)) addDocVoMap.put(comKey, new ArrayList<DmDocDVo>());
					addDocVoMap.get(comKey).add(tmpDmDocDVo);
				}
				// 문서상세 초기화
				dmDocDVo = new DmDocDVo();
				dmDocDVo.setTableName(tableName);
				// 완료 상태 문서만 조회
				dmDocDVo.setStatCd("C");
				//dmDocDVo.setWhereSqllet(" AND T.STAT_CD IN('C','D')");
				// 최상위 문서그룹ID 맵
				Map<String,String> topDocMap = new HashMap<String,String>();
				// 관련문서 전체 목록
				List<DmDocDVo> subGrpList = new ArrayList<DmDocDVo>();
				// 관련 문서맵
				//Map<String,DmDocDVo> subGrpListMap = new HashMap<String,DmDocDVo>();
				dmDocSvc.setSubDocGrpSortOrdrs(dmDocDVo, addDocVoMap, topDocMap, subGrpList, null, null, 0);
				if(subGrpList.size()>0)
					dmDocSvc.updateSortOrdrs(queryQueue, subGrpList, statCd, tableName);
				
				// 작업 이력저장[사용자]
				if(!admUriChk && dmTaskSvc.getTaskCdChk(null, userVo.getCompId(), "recovery")) dmTaskSvc.saveDmTask(queryQueue, tableName, langTypCd, docGrpId, userVo.getUserUid(), "recovery", null);
				/** 검색 색인 데이터를 더함 */
				dmDocSvc.addSrchIndex(docGrpId, userVo, queryQueue, "I");
			}else {// 상태코드 변경(삭제) - 휴지통
				DmDocDVo dmDocDVo = new DmDocDVo();
				dmDocDVo.setTableName(tableName);
				dmDocDVo.setDocPid(docGrpId);
				dmDocDVo.setStatCd("C");
				// 하위문서가 있으면 삭제 중지
				if(commonSvc.count(dmDocDVo)>0){
					// dm.msg.deleteDoc.hasSub=하위문서가 있는 문서는 삭제할 수 없습니다.
					throw new CmException("dm.msg.deleteDoc.hasSub", request);
				}
				//삭제
				dmDocDVo = new DmDocDVo();
				dmDocDVo.setTableName(tableName);
				dmDocDVo.setDocGrpId(docGrpId);
				dmDocDVo.setStatCd(statCd);
				queryQueue.update(dmDocDVo);
				
				// 작업 이력저장[사용자]
				if(!admUriChk && dmTaskSvc.getTaskCdChk(null, userVo.getCompId(), "recycle")) dmTaskSvc.saveDmTask(queryQueue, tableName, langTypCd, docGrpId, userVo.getUserUid(), "recycle", null);
				/** 검색 색인 데이터를 삭제 */
				dmDocSvc.addSrchIndex(docGrpId, userVo, queryQueue, "D");
			}
			
			commonSvc.execute(queryQueue);
			
			String msgCode = (String) object.get("msgCode");
			
			// cm.msg.del.success=삭제 되었습니다.
			message = messageProperties.getMessage(msgCode != null && !msgCode.isEmpty() ? msgCode : "cm.msg.del.success", request);
			model.put("message", message);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 문서 다수 삭제 - 관리자 */
	@RequestMapping(value = {"/dm/doc/transDocDelListAjx","/dm/adm/doc/transDocDelListAjx"})
	public String transDocDelListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			String message = null;

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String docGrpId = (String) object.get("docGrpId");
			String statCd = (String) object.get("statCd");
			if ((statCd == null || statCd.isEmpty()) || docGrpId == null || docGrpId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String tableName = dmStorBVo.getTblNm();
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
						
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 권한 세팅
			boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
						
			// 문서ID배열
			String[] docGrpIds = docGrpId.split(",");
			
			// 하위문서그룹ID 상태코드[완전삭제,휴지통]
			String[] subGrpStatCd = new String[]{"F","D"};
			
			//관리자 일경우 하위문서를 조회하고 문서그룹ID를 중복제거 후 배열에 담는다.
			if(ArrayUtil.isInArray(subGrpStatCd, statCd) && isAdmin){
				// DmDocLVo 초기화
				//DmDocLVo srchDmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
				
				DmDocDVo srchDmDocDVo = new DmDocDVo();
				srchDmDocDVo.setTableName(tableName);
				
				// 하위문서그룹ID를 조회한다.
				docGrpIds = dmDocSvc.getSubDocIdList(srchDmDocDVo, docGrpIds, new String[]{"C","R"});
			}
			
			// DmDocLVo 초기화
			DmDocLVo dmDocLVo = null;
			
			if("F".equals(statCd)){ // 완전삭제[폐기]
				// 삭제할 문서ID 배열
				List<String> delDocIdList = null;
				
				// 디스크에서 삭제할 파일 목록 VO
				List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
				for(String grpId : docGrpIds){
					delDocIdList = new ArrayList<String>();
					// 문서의 버전 목록을 조회
					List<DmDocVerLVo> dmDocVerLVoList = dmDocSvc.getDocVerLVoList(grpId, null, "VER_VA DESC", tableName);
					for(DmDocVerLVo storedDmDocVerLVo : dmDocVerLVoList){
						delDocIdList.add(storedDmDocVerLVo.getDocId());
					}
					dmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
					dmDocLVo.setDocGrpId(grpId);
					// 문서 삭제
					dmDocSvc.delDoc(request, queryQueue, langTypCd, dmDocLVo, dmStorBVo, grpId, null, userVo.getUserUid(), delDocIdList, deletedFileList, isAdmin);
					/** 검색 색인 데이터를 삭제 */
					dmDocSvc.addSrchIndex(grpId, userVo, queryQueue, "D");
				}
				// 파일 삭제
				if(deletedFileList.size()>0)				
					dmFileSvc.deleteDiskFiles(deletedFileList);
			}else if("C".equals(statCd)){// 복원
				// 복원할 문서 목록맵
				Map<String,List<DmDocDVo>> addDocVoMap = new HashMap<String,List<DmDocDVo>>();
				DmDocDVo dmDocDVo = null;
				
				// 문서그룹 맵 세팅
				dmDocSvc.setSubDocGrpMap(tableName, addDocVoMap, docGrpIds, null, null, false);
				
				// 문서상세 초기화
				dmDocDVo = new DmDocDVo();
				dmDocDVo.setTableName(tableName);
				// 완료 상태 문서만 조회
				dmDocDVo.setStatCd("C");
				// 최상위 문서그룹ID 맵
				Map<String,String> topDocMap = new HashMap<String,String>();
				// 관련문서 전체 목록
				List<DmDocDVo> subGrpList = new ArrayList<DmDocDVo>();
				// 관련 문서맵
				//Map<String,DmDocDVo> subGrpListMap = new HashMap<String,DmDocDVo>();
				dmDocSvc.setSubDocGrpSortOrdrs(dmDocDVo, addDocVoMap, topDocMap, subGrpList, null, null, 0);
				if(subGrpList.size()>0)
					dmDocSvc.updateSortOrdrs(queryQueue, subGrpList, statCd, tableName);
				
				if(!isAdmin && dmTaskSvc.getTaskCdChk(null, userVo.getCompId(), "recovery")){
					for(String grpId : docGrpIds){
						// 작업 이력저장[사용자]
						dmTaskSvc.saveDmTask(queryQueue, tableName, langTypCd, grpId, userVo.getUserUid(), "recovery", null);
						/** 검색 색인 데이터를 더함 */
						dmDocSvc.addSrchIndex(grpId, userVo, queryQueue, "I");
					}
				}
				
			}else {// 상태코드 변경(삭제) - 휴지통
				for(String grpId : docGrpIds){
					dmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
					dmDocLVo.setDocGrpId(grpId);
					dmDocLVo.setStatCd(statCd);
					dmDocSvc.saveDmDocD(queryQueue, dmDocLVo, null);
					// 작업 이력저장[사용자]
					if(!isAdmin && dmTaskSvc.getTaskCdChk(null, userVo.getCompId(), "recycle")) dmTaskSvc.saveDmTask(queryQueue, tableName, langTypCd, grpId, userVo.getUserUid(), "recycle", null);
					
					/** 검색 색인 데이터를 삭제 */
					dmDocSvc.addSrchIndex(grpId, userVo, queryQueue, "D");
				}
			}
			
			commonSvc.execute(queryQueue);
			
			String msgCode = (String) object.get("msgCode");
			
			// cm.msg.del.success=삭제 되었습니다.
			message = messageProperties.getMessage(msgCode != null && !msgCode.isEmpty() ? msgCode : "cm.msg.del.success", request);
			model.put("message", message);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [팝업] 분류체계,폴더 목록 조회 */
	@RequestMapping(value = {"/dm/doc/findSelPop","/dm/fld/findSelPop","/dm/adm/doc/findSelPop","/cm/doc/findSelPop","/dm/adm/fld/findSelPop"})
	public String listClsPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		model.put("isCm", request.getRequestURI().startsWith("/cm/doc"));
		
		return LayoutUtil.getJspPath("/dm/doc/findSelPop");
	}
	
	/** [FRAME] 분류체계,폴더 목록 조회(오른쪽 프레임) */
	@RequestMapping(value = {"/dm/doc/findSelFrm","/dm/fld/findSelFrm","/dm/adm/doc/findSelFrm","/cm/doc/findSelFrm"})
	public String selectClsFrm(HttpServletRequest request,
			@Parameter(name="lstTyp", required=true) String lstTyp,
			@Parameter(name="selIds", required=false) String selIds,
			ModelMap model) throws Exception {
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();		
		// userUids 를 List로 만듬
		List<String> selIdList = new ArrayList<String>();
		if(selIds!=null && !selIds.isEmpty()){
			for(String selId : selIds.split(",")){
				selIdList.add(selId);
			}
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 선택한 ID 목록 세팅
		dmCmSvc.setSelMapByIds(storId, "C".equals(lstTyp) ? new DmClsBVo() : new DmFldBVo(storId), selIdList, langTypCd, model, true);
				
		return LayoutUtil.getJspPath("/dm/doc/findSelFrm");
	}
	
	/** [팝업] 작업 목록*/
	@RequestMapping(value = {"/dm/doc/listDocTaskPop","/dm/adm/doc/listDocTaskPop"})
	public String findDocTaskPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 이력대상 목록
		model.put("taskList", dmTaskSvc.getDmTaskCdList(request));
		model.put("params", ParamUtil.getQueryString(request));
		return LayoutUtil.getJspPath("/dm/doc/listDocTaskPop");
	}
	
	/** [FRAME] 작업 목록 조회 - 팝업*/
	@RequestMapping(value = {"/dm/doc/listDocTaskFrm","/dm/adm/doc/listDocTaskFrm"})
	public String findDocTaskFrm(HttpServletRequest request,
			@RequestParam(value = "docId", required = false) String docId,
			@RequestParam(value = "srchCd", required = false) String srchCd,
			@RequestParam(value = "dialog", required = false) String dialog,
			ModelMap model) throws Exception {
		
		if (docId == null || docId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 요청경로 세팅
		String path = "view";
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		// 저장소ID
		String storId = dmStorBVo.getStorId();
		// 테이블명
		String tableName = dmStorBVo.getTblNm();
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
						
		DmDocLVo dmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, dmStorBVo, docId, null);
		if(dmDocLVo == null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		/** [권한] Start */
		boolean admUriChk = request.getRequestURI().startsWith("/dm/adm/");
		Map<String,String> authMap = dmDocSvc.getAuthMap(model, admUriChk, dmDocLVo, userVo, storId, langTypCd, path, false);
		// 권한체크 제외 URL
		boolean isUrlChk = dmDocSvc.chkNotAuth(path);
		if(!admUriChk && !isUrlChk && !dmDocSvc.chkDocSeculCd(userVo, dmDocLVo, dmDocSvc.chkDocAuth(authMap, "docHst", path))){
			LOGGER.error("[ERROR] User Auth Check Fail - request path : "+path+"\tuserUid : "+userVo.getUserUid()+"\tauth : view");
			//cm.msg.noAuth=권한이 없습니다.
			model.put("messageCode", "cm.msg.noAuth");
			model.put("todo", "parent.dialog.close('"+dialog+"');");
			return LayoutUtil.getResultJsp();
		}
		/** [권한] End */
		
		// 문서그룹ID
		String docGrpId = dmDocLVo.getDocGrpId();
		DmTaskHVo dmTaskHVo = new DmTaskHVo();
		VoUtil.bind(request, dmTaskHVo);
		dmTaskHVo.setQueryLang(langTypCd);
		dmTaskHVo.setTableName(tableName);
		dmTaskHVo.setDocGrpId(docGrpId);
		
		if(srchCd != null && !srchCd.isEmpty()){
			String[] srchCds = srchCd.split(",");
			if(srchCds.length>0) dmTaskHVo.setTaskCdList(srchCds);
		}
		
		// 카운트 조회
		Integer recodeCount = commonSvc.count(dmTaskHVo);
		PersonalUtil.setPaging(request, dmTaskHVo, recodeCount);
		model.put("recodeCount", recodeCount);
		// 레코드 조회
		if(recodeCount.intValue()>0){
			@SuppressWarnings("unchecked")
			List<DmTaskHVo> dmTaskHVoList = (List<DmTaskHVo>)commonSvc.queryList(dmTaskHVo);
			
			JSONObject jsonObject = null;
			for(DmTaskHVo storedDmTaskHVo : dmTaskHVoList){
				jsonObject = (JSONObject)JSONValue.parse(storedDmTaskHVo.getNote());
				model.put(storedDmTaskHVo.getDocGrpId()+"_"+storedDmTaskHVo.getTaskDt(), JsonUtil.jsonToMap(jsonObject));
			}
			
			model.put("dmTaskHVoList", dmTaskHVoList);
		}
		model.put("params", ParamUtil.getQueryString(request));
		
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request));
					
		return LayoutUtil.getJspPath("/dm/doc/listDocTaskFrm");
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/dm/downFile","/dm/adm/downFile","/dm/preview/downFile"}, method = RequestMethod.POST)
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "fileIds", required = true) String fileIds,
			@RequestParam(value = "actionParam", required = false) String actionParam
			) throws Exception {
		
		try {
			if (fileIds.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			 
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, null, null, null, null, null);
			
			String tableName = actionParam == null || actionParam.isEmpty() ? dmStorBVo.getTblNm() : null;
			
			// 파일명을 담을 객체
			List<String> fileNmList = new ArrayList<String>();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 파일 목록조회
			ModelAndView mv = dmFileSvc.getFileList(request, fileIds, actionParam, tableName, fileNmList, userVo.getCompId());
			
			String docGrpId = ParamUtil.getRequestParam(request, "docGrpId", false);
			if(docGrpId != null && !docGrpId.isEmpty()){
				// 관리자여부
				boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
							
				// 작업 이력저장[사용자]
				if(!isAdmin && tableName != null && fileNmList.size()>0){
					String taskCd = "download";
					// 세션의 언어코드
					String langTypCd = LoginSession.getLangTypCd(request);
					
					// 관리자 설정에서 작업이력구분 체크
					if(dmTaskSvc.getTaskCdChk(null, userVo.getCompId(), taskCd)){
						String[][] rescVals = new String[][]{{"cols.attFile",StringUtils.join(fileNmList, ',')}};
						dmTaskSvc.saveDmTask(null, tableName, langTypCd, docGrpId, userVo.getUserUid(), taskCd, dmTaskSvc.getTaskMapList(rescVals));
					}
				}
			}
			
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
		}
		return null;
	}
	
	/** 기본정보 변경 */
	@RequestMapping(value = "/dm/adm/doc/transTransferDoc", 
			method = RequestMethod.POST)
	public String transTransferDoc(HttpServletRequest request,
			@RequestParam(value = "grpId", required = false) String grpId,
			ModelMap model) {
		
		try {
			if(grpId == null || grpId.isEmpty()){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
			}
			String tableName = dmStorBVo.getTblNm();
			String storId = dmStorBVo.getStorId();
			QueryQueue queryQueue = new QueryQueue();
			
			// 문서기본 초기화
			DmDocLVo dmDocLVo = null;
			// 분류체계 초기화
			List<DmClsRVo> dmClsRVoList = null;
			String[] grpIds = grpId.split(",");
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			for(String docGrpId : grpIds){
				dmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
				VoUtil.bind(request, dmDocLVo);
				dmDocLVo.setDocGrpId(docGrpId);
				dmDocSvc.saveDmDocD(queryQueue, dmDocLVo, null);
				
				// 분류체계 조회
				dmDocSvc.setDmClsBVoList(request, null, storId, new ArrayList<DmClsBVo>(), dmDocLVo, null, "clsNm", dmDocLVo.getTableName(), langTypCd);

				dmClsRVoList = dmDocLVo.getDmClsRVoList();
				if(dmClsRVoList != null && dmClsRVoList.size()>0) dmDocSvc.saveDmClsRVoList(queryQueue, dmClsRVoList, dmDocLVo.getDocGrpId(), tableName, true);
			}
						
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace(parent.location.href);");
				
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [SCHEDULED] 문서 스케줄링[최신,보존연한 문서 여부 변경] *//*
	@Scheduled(cron=" 0 30 0 * * *")//매일 0시 30분에 초기화
	//@Scheduled(fixedDelay=20000)
	public void updateDocLogScheduled() {
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			String today = sdf.format(cal.getTime());
			
			DmStorBVo dmStorBVo;
			// 회사목록
			List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", "ko");
			DmDocLVo dmDocLVo;
			// 환경설정
			Map<String,String>	envConfigMap;
			String compId;
			int cnt=0,ddln;
			for(PtCompBVo storedPtCompBVo : ptCompBVoList){
				compId = storedPtCompBVo.getCompId();
				dmStorBVo = dmStorSvc.getDmStorBVo(compId, "ko");
				if(dmStorBVo != null){
					dmDocLVo = dmDocSvc.newDmDocLVo(dmStorBVo);
					if(dmDocLVo != null){
						// 회사별 환경설정 로드
						envConfigMap = dmAdmSvc.getEnvConfigMap(null, compId);
					}
				}
			}
			
			if(!queryQueue.isEmpty()) commonSvc.execute(queryQueue);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Scheduled deleteUserLogScheduled completed at " + new Timestamp(System.currentTimeMillis()).toString() + ", delete count = " + cnt);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}*/
	
}
