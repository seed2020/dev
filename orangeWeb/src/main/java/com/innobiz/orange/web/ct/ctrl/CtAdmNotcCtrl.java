package com.innobiz.orange.web.ct.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtAdmNotcSvc;
import com.innobiz.orange.web.ct.svc.CtFileSvc;
import com.innobiz.orange.web.ct.vo.CtAdmNotcBVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/** 관리자 커뮤니티 공지사항 */
@Controller
public class CtAdmNotcCtrl {
	
	/** 관리자 커뮤니티 공지사항 서비스 */
	@Autowired
	private CtAdmNotcSvc ctAdmNotcSvc;
	
	/** 게시물 첨부파일  */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CtAdmNotcCtrl.class);
	
	
	/** 커뮤니티 공지사항 목록(사용자) */
	@RequestMapping(value = "/ct/notc/listNotc")
	public String listNotc(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		CtAdmNotcBVo ctAdmNotcBVo = new CtAdmNotcBVo();
		ctAdmNotcBVo.setCompId(userVo.getCompId());
		//만료된 공지사항 제외
		ctAdmNotcBVo.setSchExpr("N");
		VoUtil.bind(request, ctAdmNotcBVo);
		
		// 게시물(BB_X000X_L) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(ctAdmNotcBVo);
		PersonalUtil.setPaging(request, ctAdmNotcBVo, recodeCount);
		
		// 게시물 목록
		List<CtAdmNotcBVo> ctAdmNotcMapList = ctAdmNotcSvc.getCtAdmNotcVoList(ctAdmNotcBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("ctAdmNotcMapList", ctAdmNotcMapList);
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		
		return LayoutUtil.getJspPath("/ct/notc/listNotc");
				
	}
	
	/** 게시물 조회*/
	@RequestMapping(value = "/ct/notc/viewNotc")
	public String viewNotc(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			ModelMap model) throws Exception {
		
		if (bullId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 게시물 테이블
		CtAdmNotcBVo ctAdmNotcBVo = ctAdmNotcSvc.getCtAdmNotcBVo(Integer.parseInt(bullId), langTypCd);
		if (ctAdmNotcBVo == null) {
			// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("ct.msg.bullNotExists", request);
		}
		model.put("ctAdmNotcBVo", ctAdmNotcBVo);
		
		
		//조회이력 저장
		if(ctAdmNotcSvc.saveReadHst(bullId, userVo.getUserUid(), userVo.getCompId())){
			ctAdmNotcSvc.addReadCnt(Integer.parseInt(bullId));
		}
		
		// 게시물첨부파일 리스트 model에 추가
		ctAdmNotcSvc.putFileListToModel(bullId, model, userVo.getCompId());
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		
		return LayoutUtil.getJspPath("/ct/notc/viewNotc");
		
	}
	
	
	/** 커뮤니티 공지사항 목록(관리자) */
	@RequestMapping(value = "/ct/adm/listNotc")
	public String listAmdNotc(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		CtAdmNotcBVo ctAdmNotcBVo = new CtAdmNotcBVo();
		ctAdmNotcBVo.setCompId(userVo.getCompId());
		VoUtil.bind(request, ctAdmNotcBVo);
		
		// 게시물(BB_X000X_L) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(ctAdmNotcBVo);
		PersonalUtil.setPaging(request, ctAdmNotcBVo, recodeCount);
		
		// 게시물 목록
		List<CtAdmNotcBVo> ctAdmNotcMapList = ctAdmNotcSvc.getCtAdmNotcVoList(ctAdmNotcBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("ctAdmNotcMapList", ctAdmNotcMapList);
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		
		return LayoutUtil.getJspPath("/ct/adm/listNotc");
				
	}
			
			
	
	
	/** 게시물 수정용 조회 및 등록 화면 (관리자) */
	@RequestMapping(value = {"/ct/adm/setNotc"})
	public String setAdmNotc(HttpServletRequest request,
			@RequestParam(value = "bullId", required = false) String bullId,			
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		CtAdmNotcBVo ctAdmNotcBVo = null;
		
		// 수정인 경우
		if (bullId != null && !bullId.isEmpty()) {
			// 관리자 커뮤니티(CT_ADM_NOTC_B) 테이블
			ctAdmNotcBVo = ctAdmNotcSvc.getCtAdmNotcBVo(Integer.parseInt(bullId), langTypCd);
			if(ctAdmNotcBVo == null){
				// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("ct.msg.bullNotExists", request);
			}
			model.put("ctAdmNotcBVo", ctAdmNotcBVo);
			
			// 날짜 초기화
			ctAdmNotcSvc.initBullRezvDt(ctAdmNotcBVo);
			ctAdmNotcSvc.initBullExprDt(ctAdmNotcBVo);
		
		}else {
			ctAdmNotcBVo = new CtAdmNotcBVo();
			// 날짜 초기화
			ctAdmNotcSvc.initBullRezvDt(ctAdmNotcBVo);
			ctAdmNotcSvc.initBullExprDt(ctAdmNotcBVo);
			model.put("ctAdmNotcBVo", ctAdmNotcBVo);
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 게시물첨부파일 리스트 model에 추가
		ctFileSvc.putFileListToModel(bullId, model, userVo.getCompId());
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		// 최대 본문 사이즈 model에 추가
		ctAdmNotcSvc.putBodySizeToModel(request, model);
		
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId"));
		
		return LayoutUtil.getJspPath("/ct/adm/setNotc");
		
	}
	
	/** 게시물 저장 (관리자) */
	@RequestMapping(value = "/ct/adm/transSetNotcSave")
	public String transSetNotcSave(HttpServletRequest request, ModelMap model){
		UploadHandler uploadHandler = null;
		
		try{
			// Multipart 파일 업로드
			uploadHandler = ctFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// parameters
			String bullId       = ParamUtil.getRequestParam(request, "bullId", false);
			String bullStatCd   = ParamUtil.getRequestParam(request, "bullStatCd", true);
			String bullRezvDt   = ParamUtil.getRequestParam(request, "bullRezvDt", false);
			//String bullExprDt   = ParamUtil.getRequestParam(request, "bullExprDt", false);
			
			if (bullStatCd.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
			if ("B".equals(bullStatCd) && bullRezvDt != null && !bullRezvDt.isEmpty() && StringUtil.isAfterNow(bullRezvDt)) {
				bullStatCd = "R";
			}
			
			// 게시물 저장
			QueryQueue queryQueue = new QueryQueue();
			Integer storedBullId = ctAdmNotcSvc.saveCtBullLVo(request, bullId, bullStatCd, queryQueue);
			
			// 게시파일 저장
			String ctFncUid = request.getParameter("menuId");
			//List<CommonFileVo> deletedFileList = ctFileSvc.saveNotcFile(request, String.valueOf(storedBullId), queryQueue, ctFncUid);
			ctFileSvc.saveNotcFile(request, String.valueOf(storedBullId), queryQueue, ctFncUid);
			commonSvc.execute(queryQueue);
			//ctFileSvc.deleteDiskFiles(deletedFileList);
			
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			boolean goView = false;
			// 수정이고, 게시예약일과 게시완료일 사이이고, 보안글이 아니면 조회화면으로 이동
			if (bullId != null && !bullId.isEmpty()) {
				goView = true;
			}	
			
			if (goView) {
				model.put("todo", "parent.location.replace('/ct/adm/viewNotc.do?menuId="+request.getParameter("menuId")+"&bullId="+bullId+"');");
			} else {
				model.put("todo", "parent.location.replace('/ct/adm/listNotc.do?menuId="+request.getParameter("menuId")+"');");
			}
			
		}catch (CmException e) {
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
	
	/** 게시물 조회*/
	@RequestMapping(value = "/ct/adm/viewNotc")
	public String viewAdmNotc(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			ModelMap model) throws Exception {
		
		if (bullId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시물 테이블
		CtAdmNotcBVo ctAdmNotcBVo = ctAdmNotcSvc.getCtAdmNotcBVo(Integer.parseInt(bullId), langTypCd);
		if (ctAdmNotcBVo == null) {
			// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("ct.msg.bullNotExists", request);
		}
		model.put("ctAdmNotcBVo", ctAdmNotcBVo);
		
		
		//조회이력 저장
		if(ctAdmNotcSvc.saveReadHst(bullId, userVo.getUserUid(), userVo.getCompId())){
			ctAdmNotcSvc.addReadCnt(Integer.parseInt(bullId));
		}
		
		// 게시물첨부파일 리스트 model에 추가
		ctAdmNotcSvc.putFileListToModel(bullId, model, userVo.getCompId());
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		
		return LayoutUtil.getJspPath("/ct/adm/viewNotc");
		
	}
	
	/** 첨부파일 다운로드 */
	@RequestMapping(value = {"/ct/downFile","/ct/preview/downFile"}, method = RequestMethod.POST)
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
				CommonFileVo fileVo = ctFileSvc.getFileVo(Integer.valueOf(fileId));
				if (fileVo != null) {
					fileVo.setSavePath(wasCopyBaseDir+fileVo.getSavePath());
					//fileVo.setSavePath(fileVo.getSavePath());
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
		}catch (CmException e) {
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
	
	/** 게시물 삭제 */
	@RequestMapping(value = "/ct/adm/transAdmNotcDel")
	public String transAdmNotcDel(HttpServletRequest request,
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String bullId = (String) object.get("bullId");
			if(bullId == null){
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시물 테이블
			CtAdmNotcBVo ctAdmNotcBVo = ctAdmNotcSvc.getCtAdmNotcBVo(Integer.parseInt(bullId), langTypCd);
			if (ctAdmNotcBVo == null) {
				// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("ct.msg.bullNotExists", request);
			}

			//게시물 삭제
			QueryQueue queryQueue = new QueryQueue();
			ctAdmNotcSvc.deleteAdmNotc(Integer.parseInt(bullId), queryQueue);
			
			//게시물 첨부파일 삭제
			List<CommonFileVo> deletedFileList = ctFileSvc.deleteCtFile(bullId, queryQueue);
			
			commonDao.execute(queryQueue);
			
			//파일 삭제
			ctFileSvc.deleteDiskFiles(deletedFileList);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
			
		}catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		
		return JsonUtil.returnJson(model);
		
	}
	
		
	

}
