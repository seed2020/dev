package com.innobiz.orange.mobile.dm.ctrl;

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

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
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
public class MoDmPsnDocCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoDmPsnDocCtrl.class);
	
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
	
	/** 파일 서비스 */
	@Resource(name = "dmFileSvc")
	private DmFileSvc dmFileSvc;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	/** [개인] 문서 목록 조회*/
	@RequestMapping(value = {"/dm/doc/listPsnDoc","/dm/adm/doc/listPsnDoc"})
	public String listPsnDoc(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		dmCmSvc.getRequestPath(request, model , "Doc");
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return MoLayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/dm/adm/");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String lstTyp = "L"; // 모바일은 목록형만 가능.
		
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
			if(isSrchChk) dmDocSvc.setPsnListQueryOptions(request, langTypCd, storId, dmDocLVo, lstTyp, regrUid, userVo.getCompId(), model, isAdmin);
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
		PersonalUtil.setPaging(request, dmDocLVo, recodeCount);
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
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId","noCache"));
		model.put("params", ParamUtil.getQueryString(request));
		
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request, "menuId", "lstTyp", "paramStorId","noCache"));
				
		return MoLayoutUtil.getJspPath("/dm/psn/listDoc");
	}
	
	/** 문서 등록 수정 화면 */
	@RequestMapping(value = {"/dm/doc/setPsnDoc","/dm/doc/setPsnDocFrm"})
	public String setDoc(HttpServletRequest request,
			@RequestParam(value = "docId", required = false) String docId,
			@RequestParam(value = "fldId", required = false) String fldId,
			ModelMap model) throws Exception {
		
		dmCmSvc.getRequestPath(request, model , "Doc");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return MoLayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		DmDocLVo dmDocLVo;
		if(docId != null && !docId.isEmpty()){
			// 조회
			dmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, null, docId, null);
			if(dmDocLVo == null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			String tblNm = DmConstant.TBl_NM_PREFIX + DmConstant.PSN_TBl_NM_SUFFIX + DmConstant.TBl_NM_SUFFIX;
			//lobHandler
			model.put("lobHandler", lobHandler.create(
					"SELECT CONT FROM "+tblNm+" WHERE DOC_ID = ?", 
					new String[]{docId}
			));
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
		
		// 스크립트 사용
		model.addAttribute("JS_OPTS", new String[]{"validator"});

		// 최대 본문 사이즈 model에 추가
		dmDocSvc.putBodySizeToModel(request, model);
		
		// 맵으로 변환
		model.put("dmDocLVoMap", VoUtil.toMap(dmDocLVo, null));
		
		model.put("params", ParamUtil.getQueryString(request));
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId","fldId","clsId","noCache"));
				
		return MoLayoutUtil.getJspPath("/dm/psn/setDoc");
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
			return MoLayoutUtil.getResultJsp();
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
		
		model.put("paramsForList", ParamUtil.getQueryString(request, "docId","fldId","clsId","noCache"));
		
		return MoLayoutUtil.getJspPath("/dm/psn/viewDoc");
	}
	
	/** 문서 저장 */
	@RequestMapping(value = {"/dm/doc/transPsnDocPost","/dm/doc/transPsnDocFrm"}, 
			method = RequestMethod.POST)
	public String transPsnDoc(HttpServletRequest request,
			ModelMap model) {
		
		UploadHandler uploadHandler = null;
		
		try {
			// Multipart 파일 업로드
			uploadHandler = dmFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
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
				model.put("todo", "$m.nav.curr(null, '" + page + "');");
			}else{
				// 목록 여부
				boolean isListPage = docId == null || docId.isEmpty() ? true : false; 
				
				// cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				
				// 등록일 경우 목록 화면, 수정일 경우 상세화면으로 이동
				page = isListPage ? listPage : viewPage;
				
				model.put("todo", "$m.nav.prev(event, '" + page + "');");
			}
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return MoLayoutUtil.getResultJsp();
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
}
