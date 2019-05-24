package com.innobiz.orange.web.dm.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmFileSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.dm.vo.DmItemDispDVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

@Controller
public class DmPsnDocCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmPsnDocCtrl.class);
	
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
	
	/** [개인] 문서 목록 조회 */
	@RequestMapping(value = {"/dm/doc/listPsnDoc","/dm/adm/doc/listPsnDoc"})
	public String listPsnDoc(HttpServletRequest request,
			@RequestParam(value = "lstTyp", required = false) String lstTyp,
			@RequestParam(value = "fldId", required = false) String fldId,
			ModelMap model) throws Exception {
		
		// 관리자여부
		boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
		
		// 요청경로 세팅
		String path = dmCmSvc.getRequestPath(request, model , "Doc");
		
		// 탭목록 세팅
		lstTyp = dmDocSvc.setTabList(model, path, lstTyp);
		
		// 탭ID
		if(lstTyp == null){
			lstTyp = "F";// 폴더형 보기
		}
				
		model.put("lstTyp", lstTyp);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		//목록형이면 문서 목록 페이지 호출
		if("L".equals(lstTyp)) return listPsnDocFrm(request, model);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
				
		
		if(fldId == null || fldId.isEmpty()) fldId = "ROOT";
		
		boolean isRoot = fldId != null && !fldId.isEmpty() && "ROOT".equals(fldId);
		
		List<DmItemDispDVo> dispList = dmDocSvc.getPsnItemDispList(request, langTypCd, "list", isRoot, storId, fldId, null);
		
		// 관리자 일 경우 등록자 조회조건을 추가한다.
		/*if(isAdmin){
			List<DmItemDispDVo> dmItemDispDVoList = new ArrayList<DmItemDispDVo>();
			dmAdmSvc.setDftDispVoList(request, langTypCd, dmItemDispDVoList, null, null, new String[]{"regrUid"});
			if(dmItemDispDVoList.size()>0) dispList.addAll(dmItemDispDVoList);
		}*/
		//model.put("srchDispList", dmDocSvc.getSrchDispDList(dispList, "F", isAdmin ? new String[]{"modrUid"} : new String[]{"regrUid","modrUid"}, isAdmin));
		model.put("srchDispList", dmDocSvc.getSrchDispDList(dispList, "F", new String[]{"regrUid","modrUid"}, isAdmin));
				
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId"));
		model.put("params", ParamUtil.getQueryString(request));
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request, "menuId", "lstTyp", "paramStorId"));
				
		model.put("isAdmin",isAdmin);
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
				
		return LayoutUtil.getJspPath("/dm/psn/listDoc");
	}
	
	/** [FRAME] [개인] 문서 목록 조회 - 오른쪽 화면*/
	@RequestMapping(value = {"/dm/doc/listPsnDocFrm","/dm/adm/doc/listPsnDocFrm","/dm/doc/listPsnDocPltFrm"})
	public String listPsnDocFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		String path = dmCmSvc.getRequestPath(request, model , "Doc");
		
		// 포틀릿 여부
		boolean isPlt = path.endsWith("PltFrm");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String lstTyp = ParamUtil.getRequestParam(request, "lstTyp", false);
		if(isPlt) lstTyp = "L";
		
		// 탭ID
		if(lstTyp == null || lstTyp.isEmpty()){
			lstTyp = "L";// 목록형 보기
		}
		// 조회조건 매핑[개인문서 VO 생성]
		DmDocLVo dmDocLVo = dmDocSvc.newDmDocLVo();
		VoUtil.bind(request, dmDocLVo);
		dmDocLVo.setQueryLang(langTypCd);
		
		// 요청 Url 별 조회조건 세팅[문서,최신,소유,보존연한]
		//dmDocSvc.setQueryUrlOptions(request, model, dmDocLVo, path, userVo, null, null, isAdmin);
		
		boolean isSrchChk = true;
		// 관리자일 경우에는 조회조건에 등록자UID가 있어야만 조회 가능함.
		if(isAdmin){
			String regrUid = ParamUtil.getRequestParam(request, "regrUid", false);			
			if(regrUid != null && !regrUid.isEmpty()) dmDocLVo.setRegrUid(regrUid);
			else isSrchChk = false;
			// 조회조건 세팅 
			/*if(isSrchChk) */
			dmDocSvc.setPsnListQueryOptions(request, langTypCd, storId, dmDocLVo, lstTyp, regrUid, userVo.getCompId(), model, isAdmin);
			/*else {
				List<DmItemDispDVo> dispList = new ArrayList<DmItemDispDVo>();
				dmAdmSvc.setDftDispVoList(request, langTypCd, dispList, null, null, new String[]{"regrUid"});
				// 검색항목 재정의
				model.put("srchDispList", dmDocSvc.getSrchDispDList(dispList, "F", new String[]{"modrUid"}, isAdmin));
			}*/
		}else{
			dmDocLVo.setRegrUid(userVo.getUserUid());
			// 조회조건 세팅 
			dmDocSvc.setPsnListQueryOptions(request, langTypCd, storId, dmDocLVo, lstTyp, userVo.getUserUid(), userVo.getCompId(), model, isAdmin);
		}
		
		/*if(dmDocLVo.getDmFldBVoList() == null || dmDocLVo.getDmFldBVoList().size() ==0){
			isSrchChk = false;
		}*/
		
		// 카운트 조회
		Integer recodeCount = isSrchChk ? commonSvc.count(dmDocLVo) : 0;
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
			dmDocSvc.setPsnDocMapList(request, dmDocLVoList, mapList, userVo);
			model.put("mapList", mapList);
		}
		
		model.put("multi", "Y");
		model.put("isAdmin", isAdmin);
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId"));
		model.put("params", ParamUtil.getQueryString(request));
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request, "menuId", "lstTyp", "paramStorId"));
				
		if(isPlt){//포틀릿
			return LayoutUtil.getJspPath("/dm/plt/listDocPltFrm");
		}else if(path.endsWith("Frm")){// 프레임
			// print css 적용
			if(request.getAttribute("printView")==null){
				request.setAttribute("printView", "print100");
			}
			
			model.put("pageSuffix", "Frm");
			return LayoutUtil.getJspPath("/dm/psn/listDocCont","Frm");
		}
		
		return LayoutUtil.getJspPath("/dm/psn/listDocCont");
	}
	
	/** 문서 등록 수정 화면 */
	@RequestMapping(value = {"/dm/doc/setPsnDoc","/dm/doc/setPsnDocFrm"})
	public String setDoc(HttpServletRequest request,
			@RequestParam(value = "docId", required = false) String docId,
			@RequestParam(value = "fldId", required = false) String fldId,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		String path = dmCmSvc.getRequestPath(request, model , "Doc");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		DmDocLVo dmDocLVo;
		if(docId != null && !docId.isEmpty()){
			// 조회
			dmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, null, docId, null, true);
			if(dmDocLVo == null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			fldId = dmDocLVo.getFldId(); 
		}else{
			dmDocLVo = new DmDocLVo();
			VoUtil.bind(request, dmDocLVo);
			
			// 세팅할 문서ID - 계속등록
			String setDocId = ParamUtil.getRequestParam(request, "setDocId", false);
			if(setDocId != null && !setDocId.isEmpty()){
				// 세션의 사용자 정보
				UserVo userVo = LoginSession.getUser(request);
				// 조회
				DmDocLVo originVo = dmDocSvc.newDmDocLVo();
				originVo.setQueryLang(langTypCd);
				originVo.setDocId(setDocId);
				// 회사ID
				originVo.setCompId(userVo.getCompId());
				originVo.setRegrUid(userVo.getUserUid());
				originVo = (DmDocLVo) commonSvc.queryVo(originVo);
				fldId = originVo.getFldId();
			}
		}
		
		if(fldId == null || fldId.isEmpty()) fldId = "ROOT";
		
		boolean isRoot = fldId != null && !fldId.isEmpty() && "ROOT".equals(fldId);
		
		if(!isRoot){
			DmFldBVo dmFldBVo = new DmFldBVo(null);
			dmFldBVo.setQueryLang(langTypCd);
			dmFldBVo.setFldId(fldId);
			dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
			if(dmFldBVo != null){
				dmDocLVo.setFldId(dmFldBVo.getFldId());
				dmDocLVo.setFldNm(dmFldBVo.getFldNm());
			}
		}
		
		List<DmItemDispDVo> itemDispList = dmDocSvc.getPsnItemDispList(request, langTypCd, null, isRoot, storId, fldId, null);
		model.put("itemDispMap", dmDocSvc.getDmItemDispDMap(itemDispList));
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 첨부파일 리스트 model에 추가
		dmFileSvc.putFileListToModel(docId, model, null, userVo.getCompId());
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});

		// 최대 본문 사이즈 model에 추가
		dmDocSvc.putBodySizeToModel(request, model);
		
		// 맵으로 변환
		model.put("dmDocLVoMap", VoUtil.toMap(dmDocLVo, null));
		
		model.put("params", ParamUtil.getQueryString(request));
		
		// 프레임
		if(path.endsWith("Frm")) {
			model.put("pageSuffix", "Frm");
			model.put("paramsForList", ParamUtil.getQueryString(request, "docId"));
			return LayoutUtil.getJspPath("/dm/psn/setDoc","Frm");
		}
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId","fldId","clsId"));
		
		return LayoutUtil.getJspPath("/dm/psn/setDoc");
	}
	
	/** 문서 상세 화면 */
	@RequestMapping(value = {"/dm/doc/viewPsnDoc","/dm/doc/viewPsnDocFrm","/dm/adm/doc/viewPsnDoc","/dm/adm/doc/viewPsnDocFrm"})
	public String viewDoc(HttpServletRequest request,
			@RequestParam(value = "docId", required = false) String docId,
			ModelMap model) throws Exception {
		
		if (docId == null || docId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
				
		// 요청경로 세팅
		String path = dmCmSvc.getRequestPath(request, model , "Doc");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		DmDocLVo dmDocLVo = dmDocSvc.newDmDocLVo();
		dmDocLVo.setQueryLang(langTypCd);
		dmDocLVo.setDocId(docId);
		// 회사ID
		dmDocLVo.setCompId(userVo.getCompId());
		dmDocLVo = (DmDocLVo) commonSvc.queryVo(dmDocLVo);
		
		if(dmDocLVo == null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		String tblNm = DmConstant.TBl_NM_PREFIX + DmConstant.PSN_TBl_NM_SUFFIX + DmConstant.TBl_NM_SUFFIX;
		// lobHandler Select
		model.put("lobHandler", lobHandler.create(
				"SELECT CONT FROM "+tblNm+" WHERE DOC_ID = ?", 
				new String[]{docId}
		));
		
		// 폴더ID
		String fldId = dmDocLVo.getFldId();
		
		DmFldBVo dmFldBVo = new DmFldBVo(null);
		dmFldBVo.setQueryLang(langTypCd);
		dmFldBVo.setFldId(fldId);
		dmFldBVo = (DmFldBVo)commonSvc.queryVo(dmFldBVo);
		if(dmFldBVo != null){
			dmDocLVo.setFldId(dmFldBVo.getFldId());
			dmDocLVo.setFldNm(dmFldBVo.getFldNm());
		}
		
		if(fldId == null || fldId.isEmpty()) fldId = "ROOT";
		
		boolean isRoot = fldId != null && !fldId.isEmpty() && "ROOT".equals(fldId);
		
		List<DmItemDispDVo> itemDispList = dmDocSvc.getPsnItemDispList(request, langTypCd, "read", isRoot, storId, fldId, null);
		model.put("itemDispMap", dmDocSvc.getDmItemDispDMap(itemDispList));
		model.put("itemDispList", dmDocSvc.getDmItemDispDList(itemDispList, "Y"));
		
		// 목록구분
		String lstTyp = ParamUtil.getRequestParam(request, "lstTyp", false);
		
		// 탭ID
		if(lstTyp == null) lstTyp = "F";// 목록형 보기
				
		// 이전다음 조회
		DmDocLVo naviVo = dmDocSvc.newDmDocLVo();
		VoUtil.bind(request, naviVo);
		naviVo.setQueryLang(langTypCd);
		
		boolean isSrchChk = true;
		// 관리자일 경우에는 조회조건에 등록자UID가 있어야만 조회 가능함.
		if(isAdmin){
			String regrUid = ParamUtil.getRequestParam(request, "regrUid", false);			
			if(regrUid != null && !regrUid.isEmpty()) dmDocLVo.setRegrUid(regrUid);
			else isSrchChk = false;
			// 조회조건 세팅 
			if(isSrchChk) dmDocSvc.setPsnListQueryOptions(request, langTypCd, storId, naviVo, lstTyp, regrUid, userVo.getCompId(), null, isAdmin);
		}else{
			// 조회조건 세팅 
			dmDocSvc.setPsnListQueryOptions(request, langTypCd, storId, naviVo, lstTyp, userVo.getUserUid(), userVo.getCompId(), null, isAdmin);
		}
		
		naviVo.setDocId(docId);
		// 이전다음 문서ID 조회
		naviVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmDocLDao.selectNaviDmDocL");
		
		naviVo = (DmDocLVo)commonSvc.queryVo(naviVo);
		if(naviVo != null){
			// 이전문서
			if(naviVo.getPrevDocId() != null && !naviVo.getPrevDocId().isEmpty())
				model.put("prevVo", dmDocSvc.getDmDocLVo(langTypCd, null, naviVo.getPrevDocId(), null));
			// 다음문서
			if(naviVo.getNextDocId() != null && !naviVo.getNextDocId().isEmpty())
				model.put("nextVo", dmDocSvc.getDmDocLVo(langTypCd, null, naviVo.getNextDocId(), null));
			
		}
		
		// 첨부파일 리스트 model에 추가
		dmFileSvc.putFileListToModel(docId, model, null, userVo.getCompId());
		
		// 맵으로 변환
		model.put("dmDocLVoMap", VoUtil.toMap(dmDocLVo, null));
		
		model.put("params", ParamUtil.getQueryString(request));
		
		// 버튼 생성
		dmDocSvc.getAuthMap(model, false, null, null, null, langTypCd, path, false);
		
		model.put("isAdmin", isAdmin);
		
		// 프레임
		if(path.endsWith("Frm")) {
			// print css 적용
			if(request.getAttribute("printView")==null){
				request.setAttribute("printView", "print100");
			}
			
			model.put("pageSuffix", "Frm");
			model.put("paramsForList", ParamUtil.getQueryString(request, "docId"));
			return LayoutUtil.getJspPath("/dm/psn/viewDoc","Frm");
		}
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId","fldId","clsId"));
		
		return LayoutUtil.getJspPath("/dm/psn/viewDoc");
	}
	
	/** 문서 저장 */
	@RequestMapping(value = {"/dm/doc/transPsnDoc","/dm/doc/transPsnDocFrm"}, 
			method = RequestMethod.POST)
	public String transPsnDoc(HttpServletRequest request,
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
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			String docId = ParamUtil.getRequestParam(request, "docId", false);
			
			// 문서VO 생성
			DmDocLVo dmDocLVo = dmDocSvc.newDmDocLVo();
			VoUtil.bind(request, dmDocLVo);
			dmDocLVo.setQueryLang(langTypCd);
			// 회사ID
			dmDocLVo.setCompId(userVo.getCompId());
			
			// 등록
			if(docId == null || docId.isEmpty()){
				dmDocLVo.setDocId(dmCmSvc.createNo("DM_PSN_DOC_L").toString());
				// 등록자, 등록일시
				dmDocLVo.setRegrUid(userVo.getUserUid());
				dmDocLVo.setRegDt("sysdate");
				queryQueue.insert(dmDocLVo);
			}else{
				// 수정자, 수정일시
				dmDocLVo.setModrUid(userVo.getUserUid());
				dmDocLVo.setModDt("sysdate");
				queryQueue.update(dmDocLVo);
			}
			
			// 파일 저장
			List<CommonFileVo> deletedFileList = dmFileSvc.saveDmFile(request, dmDocLVo.getDocId(), queryQueue, null, "/dm/psn");
						
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			dmFileSvc.deleteDiskFiles(deletedFileList);
			
			// 계속 등록페이지
			String setPage = ParamUtil.getRequestParam(request, "setPage", false);
			
			String page = null;
			if((docId == null || docId.isEmpty()) && setPage != null && !setPage.isEmpty()){
				page = setPage+(setPage.indexOf("?")>0 ? "&" : "?") + "setDocId="+dmDocLVo.getDocId();
			}else{
				// cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				// 목록 여부
				boolean isListPage = docId == null || docId.isEmpty() ? true : false;
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
	
	/** [AJAX] 문서 삭제 */
	@RequestMapping(value = {"/dm/doc/transPsnDocDelAjx","/dm/adm/doc/transPsnDocDelAjx"})
	public String transDocDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			String message = null;

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String docId = (String) object.get("docId");
			if (docId == null || docId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// DmDocLVo 초기화
			DmDocLVo dmDocLVo = null;
			// 삭제할 파일 목록			
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			String[] docIds = docId.split(",");
			for(String id : docIds){
				dmDocLVo = dmDocSvc.newDmDocLVo();
				dmDocLVo.setDocId(id);
				dmDocSvc.delPsnDoc(request, queryQueue, dmDocLVo, id, userVo.getUserUid(), deletedFileList);
			}
			
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			dmFileSvc.deleteDiskFiles(deletedFileList);
			
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
	
	
	/** [AJAX] 전체삭제 - 관리자 */
	@RequestMapping(value = "/dm/adm/doc/transDelDocAllAjx")
	public String transDelDocAllAjx(HttpServletRequest request,			
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String regrUid = (String) object.get("regrUid");
			if ( regrUid == null || regrUid.isEmpty()) {
				LOGGER.error("[ERROR] fldId == null || fldId.isEmpty() || regrUid == null || regrUid.isEmpty()");
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
						
			QueryQueue queryQueue = new QueryQueue();
			
			// 삭제할 파일 목록
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			
			// 전체삭제
			dmDocSvc.delPsnDocAll(request, queryQueue, storId, regrUid, deletedFileList);
			
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			dmFileSvc.deleteDiskFiles(deletedFileList);
			
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
	
}
