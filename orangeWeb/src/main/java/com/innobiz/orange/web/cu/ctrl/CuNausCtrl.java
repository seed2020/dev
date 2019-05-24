package com.innobiz.orange.web.cu.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import com.innobiz.orange.web.ap.utils.SAXHandler;
import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.cu.svc.CuNausFileSvc;
import com.innobiz.orange.web.cu.svc.CuNausSvc;
import com.innobiz.orange.web.cu.utils.CuConstant;
import com.innobiz.orange.web.cu.vo.CuTaskStatBVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/** 업무현황 */
@Controller
public class CuNausCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CuNausCtrl.class);

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Resource(name = "cuNausSvc")
	private CuNausSvc cuNausSvc;
	
	/** 공통 서비스 */
	@Resource(name = "cuNausFileSvc")
	private CuNausFileSvc cuNausFileSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 첨부설정 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 목록 */
	@RequestMapping(value = {"/cu/task/listTaskStat", "/cu/adm/task/listTaskStat", // 업무현황
			"/cu/reprt/listWeekReprt", "/cu/adm/reprt/listWeekReprt"}) // 주간보고
	public String listTaskStat(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 테이블관리 기본(BA_TBL_B) 테이블 - BIND
		CuTaskStatBVo cuTaskStatBVo = new CuTaskStatBVo();		
		VoUtil.bind(request, cuTaskStatBVo);
		cuTaskStatBVo.setQueryLang(langTypCd);
		cuTaskStatBVo.setCompId(userVo.getCompId());
		
		// '관리' 또는 '수정' 권한이 없으면 공개 또는 비공개이면서 내가 등록한 목록만 조회
		if(!cuNausSvc.hasAuth(request, "A","M"))
			cuTaskStatBVo.setUserUid(userVo.getUserUid());
		
		// 테이블 구분(업무현황, 주간보고)
		String typCd = cuNausSvc.setTypCd(request, cuTaskStatBVo);
		
		Integer recodeCount = commonSvc.count(cuTaskStatBVo);
		PersonalUtil.setPaging(request, cuTaskStatBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<CuTaskStatBVo> cuTaskStatBVoList = (List<CuTaskStatBVo>) commonSvc.queryList(cuTaskStatBVo);

		model.put("recodeCount", recodeCount);
		model.put("cuTaskStatBVoList", cuTaskStatBVoList);
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		model.put("isAdmin", request.getRequestURI().startsWith("/cu/adm/"));
		
		if(typCd!=null && "REPRT".equals(typCd))
			return LayoutUtil.getJspPath("/cu/naus/reprt/listWeekReprt");
		
		return LayoutUtil.getJspPath("/cu/naus/listTaskStat");
	}
	
	/** 등록 화면 (사용자) */
	@RequestMapping(value = {"/cu/task/setTaskStat", "/cu/task/viewTaskStat", "/cu/adm/task/setTaskStat", "/cu/adm/task/viewTaskStat",
			"/cu/reprt/setWeekReprt", "/cu/reprt/viewWeekReprt", "/cu/adm/reprt/setWeekReprt", "/cu/adm/reprt/viewWeekReprt"})
	public String setTaskStat(HttpServletRequest request,
			@RequestParam(value = "statNo", required = false) String statNo,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 등록여부
		boolean isSet=request.getRequestURI().startsWith("/cu/task/set") || 
				request.getRequestURI().startsWith("/cu/adm/task/set") || 
				request.getRequestURI().startsWith("/cu/reprt/set") || 
				request.getRequestURI().startsWith("/cu/adm/reprt/set");
		
		// 테이블 구분(업무현황, 주간보고)
		String typCd = cuNausSvc.setTypCd(request, null);
				
		// 수정 또는 상세보기인 경우
		if (statNo != null && !statNo.isEmpty()) {
			CuTaskStatBVo cuTaskStatBVo = new CuTaskStatBVo();
			cuTaskStatBVo.setQueryLang(langTypCd);
			cuTaskStatBVo.setCompId(userVo.getCompId());
			cuTaskStatBVo.setStatNo(statNo);
			
			if(typCd!=null) cuTaskStatBVo.setTypCd(typCd);
			
			// '관리' 또는 '수정' 권한이 없으면 공개 또는 비공개이면서 내가 등록한 목록만 조회
			if(!cuNausSvc.hasAuth(request, "A","M"))
				cuTaskStatBVo.setUserUid(userVo.getUserUid());
						
			cuTaskStatBVo = (CuTaskStatBVo)commonSvc.queryVo(cuTaskStatBVo);
			
			if(cuTaskStatBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			model.put("cuTaskStatBVo", cuTaskStatBVo);
			model.put("formBodyXML", SAXHandler.parse(cuTaskStatBVo.getBodyXml()));
			
		}else{
			model.put("formBodyXML", new XMLElement(null));
		}
		
		if(!isSet){
			if (statNo == null || statNo.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			model.put("formBodyMode", "view");
			
			// print css 적용
			if(request.getAttribute("printView")==null){
				request.setAttribute("printView", "print100");
			}
		}else{
			// 최대 본문 사이즈 model에 추가
			cuNausSvc.putBodySizeToModel(request, model);
			
			// 에디터 사용
			model.addAttribute("JS_OPTS", new String[]{"editor"});
			model.put("formBodyMode", "set");
		}
		
		// 첨부파일 리스트 model에 추가
		cuNausFileSvc.putFileListToModel(statNo, model, userVo.getCompId(), typCd);
		
		model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "statNo"));
		model.put("isAdmin", request.getRequestURI().startsWith("/cu/adm/"));
		
		if(typCd!=null && "REPRT".equals(typCd)){ // 주간보고
			// 환경설정
			cuNausSvc.getReprtConfigMap(model, userVo.getCompId());
			return LayoutUtil.getJspPath("/cu/naus/reprt/setWeekReprt");
		}
		
		return LayoutUtil.getJspPath("/cu/naus/setTaskStat");
	}
	
	
	/** 저장 */
	@RequestMapping(value = {"/cu/task/transTaskStat", "/cu/adm/task/transTaskStat", 
			"/cu/reprt/transWeekReprt", "/cu/adm/reprt/transWeekReprt"}, method = RequestMethod.POST)
	public String transTaskStat(HttpServletRequest request,
			ModelMap model) {
		
		UploadHandler uploadHandler = null;
		try {
			// Multipart 파일 업로드
			uploadHandler = cuNausFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			
			String statNo = ParamUtil.getRequestParam(request, "statNo", false);
			
			// 테이블 구분(업무현황, 주간보고)
			String typCd = cuNausSvc.setTypCd(request, null);
			
			QueryQueue queryQueue = new QueryQueue();
			// 저장
			String refId=cuNausSvc.saveTask(request, queryQueue, statNo, typCd);
			
			// 파일 저장
			List<CommonFileVo> deletedFileList = cuNausFileSvc.saveFile(request, refId, queryQueue, typCd);
			
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			cuNausFileSvc.deleteDiskFiles(deletedFileList);
						
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(statNo!=null && !statNo.isEmpty()){
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
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 삭제 (사용자) */
	@RequestMapping(value = {"/cu/task/transTaskStatDelAjx", "/cu/adm/task/transTaskStatDelAjx", 
			"/cu/reprt/transWeekReprtDelAjx", "/cu/adm/reprt/transWeekReprtDelAjx"})
	public String transTaskStatDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String statNo = (String) object.get("statNo");
			JSONArray statNos = (JSONArray) object.get("statNos");
			if (statNo == null && statNos == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 관리자 페이지 여부
			boolean isAdmin = request.getRequestURI().startsWith("/cu/adm/");
						
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 삭제할 파일 목록
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			
			// 테이블 구분(업무현황, 주간보고)
			String typCd = cuNausSvc.setTypCd(request, null);
						
			// 단건 삭제
			if(statNo!=null && !statNo.isEmpty())
				cuNausSvc.deleteTask(request, queryQueue, userVo, statNo, deletedFileList, isAdmin, typCd);
			
			// 복수 삭제
			if(statNos!=null && !statNos.isEmpty()){
				for(int i=0;i<statNos.size();i++){
					statNo = (String)statNos.get(i);
					cuNausSvc.deleteTask(request, queryQueue, userVo, statNo, deletedFileList, isAdmin, typCd);
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// 파일 삭제
			cuNausFileSvc.deleteDiskFiles(deletedFileList);
			
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
	@RequestMapping(value = {"/cu/task/downFile","/cu/adm/task/downFile", 
			"/cu/reprt/downFile","/cu/adm/reprt/downFile"}, method = RequestMethod.POST)
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
			
			// 테이블 구분(업무현황, 주간보고)
			String typCd = cuNausSvc.setTypCd(request, null);
						
			// fileId
			String[] fileIdArray = fileIds.split(",");
			List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
			for (String fileId : fileIdArray) {
				// 게시물첨부파일
				CommonFileVo fileVo = cuNausFileSvc.getFileVo(Integer.valueOf(fileId), typCd);
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
	
	/** 설정 - 조회 */
	@RequestMapping(value = "/cu/reprt/setEnvPop")
	public String setEnvPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 환경설정
		cuNausSvc.getReprtConfigMap(model, userVo.getCompId());
		
		return LayoutUtil.getJspPath("/cu/naus/reprt/setEnvPop");
	}
	
	/** [히든프레임] 옵션관리 - 저장 */
	@RequestMapping(value = "/cu/reprt/transEnv")
	public String transSchdlSetup(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			ptSysSvc.setSysSetup(CuConstant.REPRT_CONFIG+userVo.getCompId(), queryQueue, true, request);

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
}
