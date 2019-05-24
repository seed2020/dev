package com.innobiz.orange.web.wl.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wl.svc.WlAdmSvc;
import com.innobiz.orange.web.wl.svc.WlCmSvc;
import com.innobiz.orange.web.wl.svc.WlFileSvc;
import com.innobiz.orange.web.wl.svc.WlLogSvc;
import com.innobiz.orange.web.wl.vo.WlReprtGrpBVo;
import com.innobiz.orange.web.wl.vo.WlReprtUserLVo;
import com.innobiz.orange.web.wl.vo.WlTaskLogBVo;

@Controller
public class WlTaskLogCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WlTaskLogCtrl.class);
    
    /** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WlCmSvc wlCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 파일 서비스 */
	@Resource(name = "wlFileSvc")
	private WlFileSvc wlFileSvc;
	
	/** 일지 서비스 */
	@Resource(name = "wlLogSvc")
	private WlLogSvc wlLogSvc;
	
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
	
//	/** 캐쉬 만료 처리용 서비스 */
//	@Autowired
//	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 관리 서비스 */
	@Autowired
	private WlAdmSvc wlAdmSvc;
	
	/** 목록 - 보고받은, 작성한, 취합된, 부서, 임시저장 */
	@RequestMapping(value = {"/wl/task/listRecv", "/wl/task/listLog", "/wl/task/listRecvFrm", "/wl/task/listLogFrm", "/wl/task/listConsol", "/wl/task/listDept", "/wl/task/listTemp", "/wl/adm/task/listLog"})
	public String listRecv(HttpServletRequest request,
			@RequestParam(value = "typCd", required = false) String typCd,
			ModelMap model) throws Exception {
		
		// 관리자 페이지 여부
		boolean isAdmin = request.getRequestURI().startsWith("/wl/adm/");
				
		// 요청경로 세팅
		String path = wlCmSvc.getRequestPath(request, model , null);
		
		// 포틀릿 여부
		boolean isPlt = path.endsWith("pltfrm");
		
		// 프레임 여부
		boolean isFrm=!isPlt && path.endsWith("frm");
		
		//path=path.replaceAll("Frm", "");
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 테이블관리 기본(WL_TASK_LOG_B) 테이블 - BIND
		WlTaskLogBVo wlTaskLogBVo = new WlTaskLogBVo();
		VoUtil.bind(request, wlTaskLogBVo);
		wlTaskLogBVo.setQueryLang(langTypCd);
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		wlAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, wlTaskLogBVo, false);		
		wlTaskLogBVo.setWithLob(false); // LOB 데이터 조회여부
		
		// 조회조건 세팅
		wlLogSvc.setQueryUrlOptions(request, model, wlTaskLogBVo, path, userVo, langTypCd, isAdmin);
		
		Integer recodeCount = commonSvc.count(wlTaskLogBVo);
		PersonalUtil.setPaging(request, wlTaskLogBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<WlTaskLogBVo> list = (List<WlTaskLogBVo>) commonSvc.queryList(wlTaskLogBVo);
		// 파일 등록 건수
		wlLogSvc.setFileCnt(userVo.getCompId(), list);
		
		model.put("recodeCount", recodeCount);
		model.put("wlTaskLogBVoList", list);
		
		// 탭 목록
		wlLogSvc.setTabList(model, userVo.getCompId(), true);
		
		// 보고 그룹 조회
		model.put("reprtGrpList", wlLogSvc.getReprtGrpList(langTypCd, userVo, false));
				
		// 환경설정 - 첨부파일 사용여부
		Map<String, String> configMap=wlLogSvc.getEnvConfigAttr(model, userVo.getCompId(), new String[]{"fileYn"});		
		if(configMap.containsKey("fileYn"))
			model.put("fileYn", configMap.get("fileYn"));
				
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		model.put("isAdmin", isAdmin); // 관리자여부
		
		if(isPlt){//포틀릿
			return LayoutUtil.getJspPath("/wl/task/listLogPltFrm");
		}else if(isFrm){// 프레임
			model.put("pageSuffix", "Frm");
			return LayoutUtil.getJspPath("/wl/task/listLog","Frm");
		}
		
		return LayoutUtil.getJspPath("/wl/task/listLog");
	}
	
	/** 상세보기 */
	@RequestMapping(value = {"/wl/task/viewRecv", "/wl/task/viewLog", "/wl/task/viewConsol", "/wl/task/viewDept", "/wl/task/viewTemp", "/wl/adm/task/viewLog"})
	public String viewBull(HttpServletRequest request,
			@RequestParam(value = "logNo", required = false) String logNo,
			ModelMap model) throws Exception {
		
		if (logNo==null || logNo.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		// 요청경로 세팅
		wlCmSvc.getRequestPath(request, model , null);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WlTaskLogBVo wlTaskLogBVo = new WlTaskLogBVo();
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		wlAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, wlTaskLogBVo, false);		
		wlTaskLogBVo.setLogNo(logNo);
		wlTaskLogBVo=(WlTaskLogBVo)commonSvc.queryVo(wlTaskLogBVo);
		
		if (wlTaskLogBVo == null) {
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		// 취합일지 일경우 취합된 업무일지 목록 조회
		//if(path.startsWith("viewconsol") && wlTaskLogBVo.getConsolYn() !=null && "Y".equals(wlTaskLogBVo.getConsolYn())){
		//	model.put("wlTaskConsolRVoList", wlLogSvc.getWlTaskConsolRVoList(logNo, langTypCd));
		//}
		model.put("wlTaskLogBVo", wlTaskLogBVo);
		model.put("wlTaskLogUserLVoList", wlLogSvc.getLogUserList(logNo, null)); // 사용자 목록 조회 - 보고대상
		
		// 환경설정 - 첨부파일 사용여부
		Map<String, String> configMap=wlLogSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
		
		// 컬럼 목록
		List<String[]> columnList=wlLogSvc.getColumnList(model, configMap, userVo.getCompId(), langTypCd);
		// 컬럼 내용 맵으로 변환
		wlLogSvc.getColumnContList(model, columnList, wlTaskLogBVo);
		
		// 첨부파일 리스트 model에 추가
		if(configMap.containsKey("fileYn")){
			model.put("fileYn", configMap.get("fileYn"));
			if("Y".equals(configMap.get("fileYn")))
				wlFileSvc.putFileListToModel(logNo, model, userVo.getCompId());
		}
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		return LayoutUtil.getJspPath("/wl/task/viewLog");
	}
	
	/** 등록 수정 - 작성한 */
	@RequestMapping(value = {"/wl/task/setRecv", "/wl/task/setLog", "/wl/task/setTemp", "/wl/adm/task/setLog", "/wl/task/setMerge"})
	public String setLog(HttpServletRequest request,
			@RequestParam(value = "logNo", required = false) String logNo,
			@RequestParam(value = "selLogNos", required = false) String selLogNos,
			@RequestParam(value = "grpNo", required = false) String grpNo,
			ModelMap model) throws Exception {
		
		// 관리자 페이지 여부
		//boolean isAdmin = request.getRequestURI().startsWith("/wl/adm/");
				
		// 요청경로 세팅
		String path = wlCmSvc.getRequestPath(request, model , null);
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WlTaskLogBVo wlTaskLogBVo=null;
		// 수정인 경우
		if (logNo != null && !logNo.isEmpty()) {
			wlTaskLogBVo = new WlTaskLogBVo();
			wlTaskLogBVo.setQueryLang(langTypCd);
			// 회사 ID 조회조건 추가[계열사 설정 확인]
			wlAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, wlTaskLogBVo, false);		
			wlTaskLogBVo.setLogNo(logNo);
			wlTaskLogBVo=(WlTaskLogBVo)commonSvc.queryVo(wlTaskLogBVo);
			model.put("wlTaskLogBVo", wlTaskLogBVo);
			model.put("wlTaskLogUserLVoList", wlLogSvc.getLogUserList(logNo, null)); // 사용자 목록 조회 - 보고대상
			
			// 취합대상 일지번호 목록 조회
			model.put("lstNos", wlLogSvc.getConsolRLogNos(logNo));
		}
		
		// 목록에서 취합여부
		boolean isListMerge=(logNo==null || logNo.isEmpty()) && path.startsWith("recv") && selLogNos!=null && !selLogNos.isEmpty();
		
		// 탭 목록
		wlLogSvc.setTabList(model, userVo.getCompId(), false);
		
		if(path.startsWith("recv")){
			model.put("reprtGrpList", wlLogSvc.getReprtGrpList(langTypCd, userVo, false));
			if(grpNo!=null && !grpNo.isEmpty())
				model.put("wlReprtGrpLVoList", wlLogSvc.getReprtGrpDtlList(langTypCd, grpNo, "U", null));
		}else // 보고 그룹 조회 - 나를 보고자로 지정한 그룹목록		
			model.put("reprtGrpList", wlLogSvc.getReprtGrpList(langTypCd, userVo, true));
		
		// 환경설정 - 첨부파일 사용여부
		Map<String, String> configMap=wlLogSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
				
		// 컬럼 목록
		List<String[]> columnList=wlLogSvc.getColumnList(model, configMap, userVo.getCompId(), langTypCd);
		
		List<String> logNoList = null;
		if(isListMerge){
			String[] logNos=selLogNos.split(",");
			List<WlTaskLogBVo> wlTaskLogBVoList = new ArrayList<WlTaskLogBVo>();
			logNoList = new ArrayList<String>();
			for(int i=0;i<logNos.length;i++){
				logNo = logNos[i].trim();
				wlTaskLogBVo = new WlTaskLogBVo();
				wlTaskLogBVo.setQueryLang(langTypCd);
				// 회사 ID 조회조건 추가[계열사 설정 확인]
				wlAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, wlTaskLogBVo, false);
				wlTaskLogBVo.setLogNo(logNo);
				wlTaskLogBVo=(WlTaskLogBVo)commonSvc.queryVo(wlTaskLogBVo);
				if(wlTaskLogBVo==null) continue;
					wlTaskLogBVoList.add(wlTaskLogBVo);
				logNoList.add(logNo);
			}
			wlLogSvc.getColumnContList(model, columnList, wlTaskLogBVoList);
			model.put("lstNos", selLogNos);
		}else{
			// 컬럼 내용 맵으로 변환
			if(wlTaskLogBVo!=null)
				wlLogSvc.getColumnContList(model, columnList, wlTaskLogBVo);
		}
		
		// 첨부파일 리스트 model에 추가
		if(configMap.containsKey("fileYn") && "Y".equals(configMap.get("fileYn"))){
			if(isListMerge)
				wlFileSvc.putFileListToModel(logNoList, model, userVo.getCompId());
			else
				wlFileSvc.putFileListToModel(logNo, model, userVo.getCompId());
		}
		
		// 사용자 환경설정 세팅
		Map<String, String> userConfigMap = wlCmSvc.getUserConfigMap(null, userVo.getUserUid());
		model.put("userConfigMap", userConfigMap);
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		// 최대 본문 사이즈 model에 추가
		wlLogSvc.putBodySizeToModel(request, model);
				
		return LayoutUtil.getJspPath("/wl/task/setLog");
	}
	
	/** 저장 (사용자) */
	@RequestMapping(value = {"/wl/task/transRecv", "/wl/task/transLog", "/wl/task/transTemp", "/wl/adm/task/transLog"}, method = RequestMethod.POST)
	public String transLog(HttpServletRequest request,
			ModelMap model) {
		
		UploadHandler uploadHandler = null;
		try {
			
			// Multipart 파일 업로드
			uploadHandler = wlFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 요청경로 세팅
			String path = wlCmSvc.getRequestPath(request, model , null);
			path=path.toLowerCase();
			
			// parameters
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			String logNo = ParamUtil.getRequestParam(request, "logNo", false);
			
			if (listPage.isEmpty() || viewPage.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 게시물 저장
			QueryQueue queryQueue = new QueryQueue();
			
			boolean isUserList=ArrayUtil.isInArray(new String[]{"log","recv","temp"}, path);
			
			// 업무일지 저장
			String refId=wlLogSvc.saveTaskLog(request, queryQueue, logNo, isUserList);
			
			// 파일 저장
			List<CommonFileVo> deletedFileList = wlFileSvc.saveLogFile(request, refId, queryQueue);
			
			commonSvc.execute(queryQueue);

			// 파일 삭제
			wlFileSvc.deleteDiskFiles(deletedFileList);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
	        
			if(logNo!=null && !logNo.isEmpty()){
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
	
	/** [AJAX] - 업무일지 삭제 */
	@RequestMapping(value = {"/wl/task/transRecvDelAjx", "/wl/adm/task/transRecvDelAjx", "/wl/task/transLogDelAjx", "/wl/adm/task/transLogDelAjx"})
	public String transRecvDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String logNo = (String) object.get("logNo");
			JSONArray logNoArray = (JSONArray) object.get("logNos");
			if (logNo == null && logNoArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시물 복사
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 삭제할 파일 목록
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			
			if(logNo!=null && !logNo.isEmpty()){
				wlLogSvc.deleteLog(queryQueue, userVo, logNo, deletedFileList);
			}
			
			if(logNoArray!=null && !logNoArray.isEmpty()){
				for(int i=0;i<logNoArray.size();i++){
					logNo = (String)logNoArray.get(i);
					wlLogSvc.deleteLog(queryQueue, userVo, logNo, deletedFileList);
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// 실제 파일 삭제
			wlFileSvc.deleteDiskFiles(deletedFileList);
						
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
	
	/** [AJAX] - 보고그룹 기본 불러오기 */
	@RequestMapping(value = "/wl/task/getReprtGrpAjx")
	public String getReprtGrpAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String grpNo = (String) object.get("grpNo");
			if (grpNo == null && grpNo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 보고자 그룹 VO
			WlReprtGrpBVo wlReprtGrpBVo = new WlReprtGrpBVo();
			wlReprtGrpBVo.setQueryLang(langTypCd);
			// 회사 ID 조회조건 추가[계열사 설정 확인]
			wlAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, wlReprtGrpBVo, false);		
			wlReprtGrpBVo.setGrpNo(grpNo);
			
			model.put("reprtGrpVo", commonSvc.queryVo(wlReprtGrpBVo));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] - 보고그룹 목록 불러오기 */
	@RequestMapping(value = "/wl/task/getReprtGrpListAjx")
	public String getReprtGrpListAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String grpNo = (String) object.get("grpNo");
			if (grpNo == null && grpNo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String tgtTypCd = (String) object.get("tgtTypCd");
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			model.put("reprtGrpDtlList", wlLogSvc.getReprtGrpDtlList(langTypCd, grpNo, tgtTypCd, null));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** [팝업] - 보고자 목록*/
	@RequestMapping(value = {"/wl/task/setReprtUserPop", "/wl/adm/task/setReprtUserPop"})
	public String setReprtUserPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 보고대상자 목록 조회
		WlReprtUserLVo wlReprtUserLVo = new WlReprtUserLVo();
		wlReprtUserLVo.setQueryLang(langTypCd);
		wlReprtUserLVo.setRegrUid(userVo.getUserUid());
		
		@SuppressWarnings("unchecked")
		List<WlReprtUserLVo> wlReprtUserLVoList = (List<WlReprtUserLVo>)commonSvc.queryList(wlReprtUserLVo);
		
		if(wlReprtUserLVoList==null || wlReprtUserLVoList.size()==0){
			wlReprtUserLVoList=new ArrayList<WlReprtUserLVo>();
		}
		// 화면 구성용 1개의 빈 vo 넣음
		wlReprtUserLVoList.add(new WlReprtUserLVo());
		model.put("wlReprtUserLVoList", wlReprtUserLVoList);
		
		// 탭 목록
		wlLogSvc.setTabList(model, userVo.getCompId(), false);
		
		return LayoutUtil.getJspPath("/wl/task/setReprtUserPop");
	}
	
	/** 보고대상 저장 - 취합자 */
	@RequestMapping(value = {"/wl/task/transReprtUser", "/wl/adm/task/transReprtUser"})
	public String transReprtUser(HttpServletRequest request,
			@RequestParam(value = "dialog", required = false) String dialog,
			ModelMap model) {

		try {
			// 등록 권한
			boolean isWrite = SecuUtil.hasAuth(request, "W", null);
			if(!isWrite){
				// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
				throw new CmException("cm.msg.errors.403", request);
			}
			
			// 사용자, 구분코드 목록 - BIND 
			@SuppressWarnings("unchecked")
			List<WlReprtUserLVo> boundVoList = (List<WlReprtUserLVo>) VoUtil.bindList(request, WlReprtUserLVo.class, new String[]{"userUid"});
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 리소스기본(DM_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			
			WlReprtUserLVo wlReprtUserLVo = new WlReprtUserLVo();
			wlReprtUserLVo.setRegrUid(userVo.getUserUid());
			if(commonSvc.count(wlReprtUserLVo)>0)
				queryQueue.delete(wlReprtUserLVo);
			
			int sortOrdr=1;
			for(WlReprtUserLVo storedVo : boundVoList){
				storedVo.setRegrUid(userVo.getUserUid());
				storedVo.setSortOrdr(sortOrdr);
				queryQueue.insert(storedVo);
				sortOrdr++;
			}
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(dialog!=null && !dialog.isEmpty())
				model.put("todo", "parent.dialog.close('" + dialog + "');");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] - 업무일지 목록 */
	@RequestMapping(value = {"/wl/task/viewTaskLogPop", "/wl/adm/task/viewTaskLogPop", "/wl/task/setTaskLogPop", "/wl/adm/task/setTaskLogPop"})
	public String getLogListMap(HttpServletRequest request,
			@RequestParam(value = "logNo", required = false) String logNo,
			@RequestParam(value = "lstNos", required = false) String lstNos,
			ModelMap model) throws Exception {
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
		if(path.endsWith("Pop")){
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			List<WlTaskLogBVo> wlTaskLogBVoList=null;
			
			if(path.startsWith("set")){
				// 탭 목록
				wlLogSvc.setTabList(model, userVo.getCompId(), false);
				
				// 수정일 경우 이전에 취합된 목록을 조회한다
				if(logNo!=null && !logNo.isEmpty())
					wlTaskLogBVoList=wlLogSvc.getWlTaskConsolRVoList(logNo, langTypCd, null);
				else
					wlTaskLogBVoList=wlLogSvc.getWlTaskConsolRVoList(logNo, langTypCd, lstNos);
				
				if(wlTaskLogBVoList==null)
					wlTaskLogBVoList=new ArrayList<WlTaskLogBVo>();
				// 화면 구성용 1개의 빈 vo 넣음
				wlTaskLogBVoList.add(new WlTaskLogBVo());
				model.put("wlTaskLogBVoList", wlTaskLogBVoList);
				model.put("isSelect", Boolean.TRUE); // 선택가능
			}else if(path.startsWith("view")){
				model.put("wlTaskLogBVoList", wlLogSvc.getWlTaskConsolRVoList(logNo, langTypCd, null));
				model.put("isSelect", Boolean.FALSE);
			}else{
				model.put("isSelect", Boolean.FALSE);
			}
			return LayoutUtil.getJspPath("/wl/task/findTaskLogPop");
		}
		
		return LayoutUtil.getJspPath("/wl/task/findTaskLogPop");
	}
	
	/** [AJAX] - 업무일지 불러오기 */
	@RequestMapping(value = "/wl/task/getListMapAjx")
	public String getListMapAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String logNo = (String) object.get("logNo");
			JSONArray logNoArray = (JSONArray) object.get("logNos");
			if (logNo == null && logNoArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 조회 VO
			WlTaskLogBVo wlTaskLogBVo = null;
			
			// 목록
			List<WlTaskLogBVo> wlTaskLogBVoList = new ArrayList<WlTaskLogBVo>(); 
					
			List<String> logNoList = new ArrayList<String>(); 
			for(int i=0;i<logNoArray.size();i++){
				logNo = (String)logNoArray.get(i);
				wlTaskLogBVo = new WlTaskLogBVo();
				wlTaskLogBVo.setQueryLang(langTypCd);
				// 회사 ID 조회조건 추가[계열사 설정 확인]
				wlAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, wlTaskLogBVo, false);		
				wlTaskLogBVo.setLogNo(logNo);
				wlTaskLogBVo=(WlTaskLogBVo)commonSvc.queryVo(wlTaskLogBVo);
				if(wlTaskLogBVo==null) continue;
					wlTaskLogBVoList.add(wlTaskLogBVo);
				logNoList.add(logNo);
			}
			// 환경설정 - 첨부파일 사용여부
			Map<String, String> configMap=wlLogSvc.getEnvConfigAttr(model, userVo.getCompId(), null);
			// 첨부파일 리스트 model에 추가
			if(configMap.containsKey("fileYn") && "Y".equals(configMap.get("fileYn"))){
				List<CommonFileVo> logFileList = wlLogSvc.getLogFileList(logNoList);
				if(logFileList!=null && logFileList.size()>0)
					model.put("logFileList", logFileList);
			}
			//wlTaskLogBVo.setLogNoList(logNoList);
			// 조회
			//@SuppressWarnings("unchecked")
			//List<WlTaskLogBVo> wlTaskLogBVoList = (List<WlTaskLogBVo>)commonSvc.queryList(wlTaskLogBVo);
			
			// 내용 합치기 - 맵
			model.put("returnMap", wlLogSvc.getLogCont(null, configMap, wlTaskLogBVoList, wlTaskLogBVo, userVo, langTypCd));
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
	@RequestMapping(value = {"/wl/downFile","/wl/adm/downFile"}, method = RequestMethod.POST)
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "fileIds", required = true) String fileIds) throws Exception {
		
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
				CommonFileVo fileVo = wlFileSvc.getFileVo(Integer.valueOf(fileId));
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
