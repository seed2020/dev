package com.innobiz.orange.web.wj.ctrl;

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

import com.innobiz.orange.web.cm.dao.LobHandler;
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
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wj.svc.WjCmSvc;
import com.innobiz.orange.web.wj.svc.WjFileSvc;
import com.innobiz.orange.web.wj.svc.WjWorkReprtSvc;
import com.innobiz.orange.web.wj.vo.WjWorkReprtBVo;

/** 업무보고 */
@Controller
public class WjWorkReprtCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WjWorkReprtCtrl.class);

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 공통 서비스 */
	@Autowired
	private WjCmSvc wjCmSvc;
	
	/** 업무보고 처리용 서비스 */
	@Resource(name = "wjWorkReprtSvc")
	private WjWorkReprtSvc wjWorkReprtSvc;
	
	/** 파일 서비스 */
	@Resource(name = "wjFileSvc")
	private WjFileSvc wjFileSvc;
	
	/** 첨부설정 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 업무보고 목록 조회 */
	@RequestMapping(value = {"/wj/work/listWorkReprt", "/wj/adm/work/listWorkReprt"})
	public String listLog(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		wjCmSvc.getRequestPath(request, model , null);
		
		// 관리자 여부
		boolean isAdmin=request.getRequestURI().startsWith("/wj/adm/");
		model.put("isAdmin", isAdmin);
		
		// 메뉴 관리권한여부
		boolean isAuthMgm = SecuUtil.hasAuth(request, "A", null);
		model.put("isAuthMgm", isAuthMgm);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 테이블관리 기본(WJ_WORK_REPRT_B) 테이블 - BIND
		WjWorkReprtBVo wjWorkReprtBVo = new WjWorkReprtBVo();
		VoUtil.bind(request, wjWorkReprtBVo);
		wjWorkReprtBVo.setQueryLang(langTypCd);
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		wjWorkReprtSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, wjWorkReprtBVo, false);		
		wjWorkReprtBVo.setWithLob(false); // LOB 데이터 조회여부
		
		// 메뉴관리 권한 사용자
		if(isAdmin || isAuthMgm) {
			// 조직ID - 검색조건
			String orgId = ParamUtil.getRequestParam(request, "orgId", false);
			// 하위부서포함 - 검색조건
			String withSubYn = ParamUtil.getRequestParam(request, "withSubYn", false);
			boolean isWithSub = withSubYn!=null && "Y".equals(withSubYn);
			
			if(isAuthMgm && (orgId==null || orgId.isEmpty())) orgId=userVo.getOrgId(); // 조직ID가 검색조건에 없으면 사용자의 조직ID 세팅
			if(orgId!=null && !orgId.isEmpty()) {
				if(isWithSub) { // 하위부서 포함
					wjWorkReprtBVo.setOrgId(null);
					// 하위조직ID 조회 - 본인 부서 포함 하위 부서 조회
					List<String> orgIdList = new ArrayList<String>();
					orgIdList.add(orgId);
					List<String> orgSubIdList = orCmSvc.getOrgSubIdList(orgId, langTypCd);
					if(orgSubIdList!=null && orgSubIdList.size()>0)
						orgIdList.addAll(orgSubIdList);
						wjWorkReprtBVo.setOrgIdList(orgIdList);
				}else {
					wjWorkReprtBVo.setOrgId(orgId);
				}
				
			}
			
		// 일반 사용자[관리자X, 메뉴관리권한X] - 본인이 등록한 것만 조회
		}else{
			wjWorkReprtBVo.setRegrUid(userVo.getUserUid());
			//wjWorkReprtBVo.setOrgId(userVo.getOrgId()); // 조직ID - 조직이 변경될 경우에도 본인것을 조회할 건인지 소속부서 + 본인것만 조회할 것인지 협의 필요
		}
		
		Integer recodeCount = commonSvc.count(wjWorkReprtBVo);
		PersonalUtil.setPaging(request, wjWorkReprtBVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<WjWorkReprtBVo> list = (List<WjWorkReprtBVo>) commonSvc.queryList(wjWorkReprtBVo);
		model.put("recodeCount", recodeCount);
		model.put("wjWorkReprtBVoList", list);
		
		// 파일건수 추가
		wjWorkReprtSvc.setFileCnt(userVo.getCompId(), list);
				
		return LayoutUtil.getJspPath("/wj/work/listWorkReprt");
	}
	
	
	/** 업무보고 등록수정 */
	@RequestMapping(value = {"/wj/work/setWorkReprt", "/wj/adm/work/setWorkReprt"})
	public String setLog(HttpServletRequest request,
			@RequestParam(value = "reprtNo", required = false) String reprtNo,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		wjCmSvc.getRequestPath(request, model , null);
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 수정인 경우
		if (reprtNo != null && !reprtNo.isEmpty()) {
			WjWorkReprtBVo wjWorkReprtBVo = new WjWorkReprtBVo();
			wjWorkReprtBVo.setQueryLang(langTypCd);
			// 회사 ID 조회조건 추가[계열사 설정 확인]
			wjWorkReprtSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, wjWorkReprtBVo, false);		
			wjWorkReprtBVo.setReprtNo(reprtNo);
			wjWorkReprtBVo.setWithLob(true);
			wjWorkReprtBVo=(WjWorkReprtBVo)commonSvc.queryVo(wjWorkReprtBVo);
			if(wjWorkReprtBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
		}
		
		// 첨부파일 추가
		wjFileSvc.putFileListToModel(reprtNo, model, userVo.getCompId(), null);
		
		// 최대 본문 사이즈 model에 추가
		wjWorkReprtSvc.putBodySizeToModel(request, model);
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
				
		return LayoutUtil.getJspPath("/wj/work/setWorkReprt");
	}
	
	/** 업무보고 상세보기 */
	@RequestMapping(value = {"/wj/work/viewWorkReprt", "/wj/adm/work/viewWorkReprt"})
	public String viewWorkReprt(HttpServletRequest request,
			@RequestParam(value = "reprtNo", required = false) String reprtNo,
			ModelMap model) throws Exception {
		
		if (reprtNo==null || reprtNo.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 요청경로 세팅
		wjCmSvc.getRequestPath(request, model , null);
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WjWorkReprtBVo wjWorkReprtBVo = new WjWorkReprtBVo();
		wjWorkReprtBVo.setQueryLang(langTypCd);
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		wjWorkReprtSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, wjWorkReprtBVo, false);		
		wjWorkReprtBVo.setReprtNo(reprtNo);
		wjWorkReprtBVo=(WjWorkReprtBVo)commonSvc.queryVo(wjWorkReprtBVo);
		if(wjWorkReprtBVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		//lobHandler
		model.put("lobHandler", lobHandler.create(
				"SELECT CONT FROM WJ_WORK_REPRT_B WHERE REPRT_NO = ?", 
				new String[]{reprtNo}
		));
				
		model.put("wjWorkReprtBVo", wjWorkReprtBVo);
		
		wjFileSvc.putFileListToModel(reprtNo, model, userVo.getCompId(), null);
		
		return LayoutUtil.getJspPath("/wj/work/viewWorkReprt");
	}
	
	/** 저장 (사용자) */
	@RequestMapping(value = {"/wj/work/transWorkReprt", "/wj/adm/work/transWorkReprt"}, method = RequestMethod.POST)
	public String transWorkReprt(HttpServletRequest request,
			ModelMap model) {
		
		UploadHandler uploadHandler = null;
		try {
			
			// Multipart 파일 업로드
			uploadHandler = wjFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 요청경로 세팅
			String path = wjCmSvc.getRequestPath(request, model , null);
			path=path.toLowerCase();
			
			// parameters
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			String reprtNo = ParamUtil.getRequestParam(request, "reprtNo", false);
			
			if (listPage.isEmpty() || viewPage.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 게시물 저장
			QueryQueue queryQueue = new QueryQueue();
			
			// 업무보고 저장
			String refId=wjWorkReprtSvc.save(request, queryQueue, reprtNo);
			
			// 파일 저장
			List<CommonFileVo> deletedFileList = wjFileSvc.saveFile(request, refId, queryQueue);
			
			commonSvc.execute(queryQueue);

			// 파일 삭제
			wjFileSvc.deleteDiskFiles(deletedFileList);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
	        
			if(reprtNo!=null && !reprtNo.isEmpty()){
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
	
	/** [AJAX] - 업무보고 삭제 */
	@RequestMapping(value = {"/wj/work/transWorkReprtDelAjx", "/wj/adm/work/transWorkReprtDelAjx"})
	public String transWorkReprtDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String reprtNo = (String) object.get("reprtNo");
			JSONArray reprtNoArray = (JSONArray) object.get("reprtNos");
			if (reprtNo == null && reprtNoArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시물 복사
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 삭제할 파일 목록
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			
			if(reprtNo!=null && !reprtNo.isEmpty()){
				wjWorkReprtSvc.delete(queryQueue, userVo, reprtNo, deletedFileList);
			}
			
			if(reprtNoArray!=null && !reprtNoArray.isEmpty()){
				for(int i=0;i<reprtNoArray.size();i++){
					reprtNo = (String)reprtNoArray.get(i);
					wjWorkReprtSvc.delete(queryQueue, userVo, reprtNo, deletedFileList);
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// 실제 파일 삭제
			wjFileSvc.deleteDiskFiles(deletedFileList);
						
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
	@RequestMapping(value = {"/wj/downFile","/wj/adm/downFile"}, method = RequestMethod.POST)
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
				CommonFileVo fileVo = wjFileSvc.getFileVo(Integer.valueOf(fileId));
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
