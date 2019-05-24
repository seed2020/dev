//package com.innobiz.orange.web.ct.ctrl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.log4j.Logger;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONValue;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.innobiz.orange.web.cm.dao.CommonDao;
//import com.innobiz.orange.web.cm.exception.CmException;
//import com.innobiz.orange.web.cm.files.DistManager;
//import com.innobiz.orange.web.cm.files.UploadHandler;
//import com.innobiz.orange.web.cm.files.UploadManager;
//import com.innobiz.orange.web.cm.files.ZipUtil;
//import com.innobiz.orange.web.cm.svc.CommonSvc;
//import com.innobiz.orange.web.cm.utils.JsonUtil;
//import com.innobiz.orange.web.cm.utils.LayoutUtil;
//import com.innobiz.orange.web.cm.utils.MessageProperties;
//import com.innobiz.orange.web.cm.utils.ParamUtil;
//import com.innobiz.orange.web.cm.utils.StringUtil;
//import com.innobiz.orange.web.cm.utils.VoUtil;
//import com.innobiz.orange.web.cm.vo.CmEmailBVo;
//import com.innobiz.orange.web.cm.vo.CommonFileVo;
//import com.innobiz.orange.web.cm.vo.QueryQueue;
//import com.innobiz.orange.web.ct.svc.CtCmSvc;
//import com.innobiz.orange.web.ct.svc.CtCmntSvc;
//import com.innobiz.orange.web.ct.svc.CtFileSvc;
//import com.innobiz.orange.web.ct.svc.CtRecMastSvc;
//import com.innobiz.orange.web.ct.vo.CtEstbBVo;
//import com.innobiz.orange.web.ct.vo.CtFileDVo;
//import com.innobiz.orange.web.ct.vo.CtFncDVo;
//import com.innobiz.orange.web.ct.vo.CtFncMbshAuthDVo;
//import com.innobiz.orange.web.ct.vo.CtMbshDVo;
//import com.innobiz.orange.web.ct.vo.CtRecMastBVo;
//import com.innobiz.orange.web.or.svc.OrCmSvc;
//import com.innobiz.orange.web.pt.secu.LoginSession;
//import com.innobiz.orange.web.pt.secu.UserVo;
//import com.innobiz.orange.web.pt.utils.PersonalUtil;
//
///** 커뮤니티 자료실 */
//@Controller
//public class CtRecMastCtrl {
//	
//	/** 커뮤니티 자료실 서비스 */
//	@Autowired
//	private CtRecMastSvc ctRecMastSvc;
//	
//	/** 게시물 첨부파일  */
//	@Autowired
//	private CtFileSvc ctFileSvc;
//	
//	/** 공통 서비스 */
//	@Autowired
//	private CtCmntSvc ctCmntSvc;
//	
//	/** 공통 서비스 */
//	@Autowired
//	private CtCmSvc ctCmSvc;
//	
//	/** 공통 DAO */
//	@Resource(name = "commonDao")
//	private CommonDao commonDao;
//	
//	/** 공통 DB 처리용 서비스 */
//	@Resource(name = "commonSvc")
//	private CommonSvc commonSvc;
//	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
//	
//	/** 메세지 */
//	@Autowired
//	private MessageProperties messageProperties;
//	
//	/** 업로드 메니저 */
//	@Resource(name = "uploadManager")
//	private UploadManager uploadManager;
//	
//	/** 배포 매니저 */
//	@Resource(name = "distManager")
//	private DistManager distManager;
//	
//	/** 압축 파일관련 서비스 */
//	@Resource(name = "zipUtil")
//	private ZipUtil zipUtil;
//	
//	
//	/** Logger */
//	private static final Logger LOGGER = Logger.getLogger(CtRecMastCtrl.class);
//	
//	/** 커뮤니티 게시물 보내기 */
//	@RequestMapping(value = "/ct/sendSetBull")
//	public String sendSetBull(HttpServletRequest request,
//			@RequestParam(value="data", required = true) String data,
//			ModelMap model){
//		
//		try{
//			//파라미터 검사
//			JSONObject object = (JSONObject) JSONValue.parse(data);
//			String selectCtFncUid = (String) object.get("selectCtFncUid");
//			String ctId = (String) object.get("ctId");
//			String bullId = (String) object.get("bullId");
//			CtRecMastBVo ctRecMastBVo = new CtRecMastBVo();
//			ctRecMastBVo = ctRecMastSvc.getCtRecMastBVo(Integer.parseInt(bullId));
//			
//			if(ctRecMastBVo == null){
//				// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
//				throw new CmException("ct.msg.bullNotExists", request);
//			}
//			
//			ctRecMastBVo.setSubj(null);
//			model.put("ctBullMastBVo", ctRecMastBVo);
//			
//			// 게시물첨부파일 리스트 model에 추가
//			ctFileSvc.putFileListToModel(bullId, model);
//			
//			// 에디터 사용
//			model.addAttribute("JS_OPTS", new String[]{"editor"});
//			
//			// 최대 본문 사이즈 model에 추가
//			ctRecMastSvc.putBodySizeToModel(request, model);
//			
//			// params
//			model.addAttribute("params", ParamUtil.getQueryString(request, "bullId"));
//			
//			ctCmntSvc.putLeftMenu(request, ctId, model);
//			model.put("ctId", ctId);
//			model.put("selectCtFncUid", selectCtFncUid);
//			model.put("result", "ok");
//			
//			}catch (CmException e) {
//				model.put("message", e.getMessage());
//			}catch (Exception e) {
//				LOGGER.error(e.getMessage(), e);
//				model.put("message", e.getMessage());
//			}
//			return JsonUtil.returnJson(model);
//	}
//	
//	/** 현재 커뮤니티 게시판 Pop */
//	@RequestMapping(value = "/ct/selectCtBoardPop")
//	public String selectCtBoardPop(HttpServletRequest request, ModelMap model)throws Exception{
//		
//		// 세션의 언어코드
//		String langTypCd = LoginSession.getLangTypCd(request);
//		
//		// 세션의 사용자 정보
//		UserVo userVo = LoginSession.getUser(request);
//		
//		String ctId = request.getParameter("ctId");
//		String bullId = request.getParameter("bullId");
//		
//		model.put("ctId", ctId);
//		model.put("bullId", bullId);
//		CtEstbBVo ctEstbBVo = new CtEstbBVo();
//		ctEstbBVo.setCompId(userVo.getCompId());
//		ctEstbBVo.setCtId(ctId);
//		ctEstbBVo.setLogUserUid(userVo.getUserUid());
//		ctEstbBVo.setQueryLang(langTypCd);
//		ctEstbBVo.setLangTyp(langTypCd);
//		
//		// 현재 커뮤니티 
//		ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
//		
//		model.put("ctEstbBVo", ctEstbBVo);
//		
//	
//		CtFncDVo ctFncDVo = new CtFncDVo();
//		ctFncDVo.setCtId(ctId);
//		ctFncDVo.setLangTyp(langTypCd);
//		// 게시판 구분 ID
//		ctFncDVo.setCtFncId("F000000L"); 
//		// 현재 커뮤니티의 모든 게시판 목록
//		List<CtFncDVo> ctFncList = (List<CtFncDVo>) commonDao.queryList(ctFncDVo);
//		// 선택된 커뮤니티에서 쓰기 권한이 있는 자료실 목록
//		List<CtFncDVo> writeFncList = new ArrayList<CtFncDVo>();
//		
//		//사용자 권한
//		CtMbshDVo ctMbshDVo = new CtMbshDVo();
//		ctMbshDVo.setUserUid(userVo.getUserUid());
//		ctMbshDVo.setCtId(ctId);
//		ctMbshDVo.setLangTyp(langTypCd);
//		ctMbshDVo = (CtMbshDVo) commonDao.queryVo(ctMbshDVo);
//		ctMbshDVo.getUserSeculCd();
//		
//		for(CtFncDVo storeCtfncVo : ctFncList){
//			CtFncMbshAuthDVo ctFncMbshAuthDVo = new CtFncMbshAuthDVo();
//			ctFncMbshAuthDVo.setCtFncUid(storeCtfncVo.getCtFncUid());
//			ctFncMbshAuthDVo.setSeculCd(ctMbshDVo.getUserSeculCd());
//			ctFncMbshAuthDVo = (CtFncMbshAuthDVo) commonDao.queryVo(ctFncMbshAuthDVo);
//			String [] authArr = ctFncMbshAuthDVo.getAuthCd().split("\\/", -1);
//			for(int i=0; i < authArr.length ; i++){
//				if(authArr[i].equals("W")){
//					writeFncList.add(storeCtfncVo);
//				}
//			}
//		}
//		model.put("writeFncList", writeFncList);
//		
//		return LayoutUtil.getJspPath("/ct/selectCtBoardPop");
//	}
//	
//	/** 커뮤니티 자료실 게시물 보내기 */
//	@RequestMapping(value = "/ct/sendPsd")
//	public String sendPsd(HttpServletRequest request, 
//			@RequestParam(value="data", required = true) String data,
//			ModelMap model){
//		
//		try{
//			//파라미터 검사
//			JSONObject object = (JSONObject) JSONValue.parse(data);
//			String ctId = (String) object.get("ctId");
//			String bullId = (String) object.get("bullId");
//			String ctFncUid = (String) object.get("ctFncUid");
//			String selectCtId = (String) object.get("selectCtId");
//			// MultipartRequest
//			//request = uploadHandler.getMultipartRequest();
//			// Multipart 파일 업로드
//			//uploadHandler = ctFileSvc.upload(request);
//			// 세션의 언어코드
//			String langTypCd = LoginSession.getLangTypCd(request);
//			
//			CtRecMastBVo ctRecMastBVo = new CtRecMastBVo();
//			ctRecMastBVo.setBullId(Integer.parseInt(bullId));
//			ctRecMastBVo = (CtRecMastBVo) commonDao.queryVo(ctRecMastBVo);
//			// 게시물 저장
//			QueryQueue queryQueue = new QueryQueue();
//			Integer storedBullId = ctRecMastSvc.sendCtBullLVo(request, bullId, selectCtId, ctFncUid, queryQueue);
//			
//			// 파일 저장
//			CtFileDVo ctFileDVo = new CtFileDVo();
//			ctFileDVo.setRefId(bullId);
//			List<CtFileDVo> ctFileList = (List<CtFileDVo>) commonDao.queryList(ctFileDVo);
//			if(ctFileList.size() != 0){
//				for(CtFileDVo storeFileVo : ctFileList){
//					CtFileDVo clonFileVo = new CtFileDVo();
//					storeFileVo.setRefId(String.valueOf(storedBullId));
//					clonFileVo = storeFileVo;
//					clonFileVo.setFileId(ctCmSvc.createFileId("CT_FILE_D"));
//					clonFileVo.setCtId(selectCtId);
//					clonFileVo.setCtFncUid(ctFncUid);
//					queryQueue.insert(clonFileVo);
//				}
//			}
//
//			commonDao.execute(queryQueue);
//			
//			model.put("result", "ok");
//				
//		}catch (CmException e) {
//			model.put("message", e.getMessage());
//		}catch (Exception e) {
//			LOGGER.error(e.getMessage(), e);
//			model.put("message", e.getMessage());
//		}
//		return JsonUtil.returnJson(model);
//	}
//	
//	/** 타 커뮤니티 목록 Pop */
//	@RequestMapping(value = "/ct/selectCtPdsPop")
//	public String selectCtPdsPop(HttpServletRequest request, ModelMap model)throws Exception{
//		
//		// 세션의 언어코드
//		String langTypCd = LoginSession.getLangTypCd(request);
//		
//		// 세션의 사용자 정보
//		UserVo userVo = LoginSession.getUser(request);
//		
//		String ctId = request.getParameter("ctId");
//		String bullId = request.getParameter("bullId");
//		String selectCtId= request.getParameter("selectCtId");
//		
//		model.put("ctId", ctId);
//		model.put("bullId", bullId);
//		model.put("selectCtId", selectCtId);
//		CtEstbBVo ctEstbBVo = new CtEstbBVo();
//		ctEstbBVo.setCompId(userVo.getCompId());
//		ctEstbBVo.setLogUserUid(userVo.getUserUid());
//		ctEstbBVo.setQueryLang(langTypCd);
//		ctEstbBVo.setLangTyp(langTypCd);
//		ctEstbBVo.setSignal("my");
//		
//		// 나의 커뮤니티 전체 목록
//		List<CtEstbBVo> ctEstbList = (List<CtEstbBVo>) commonDao.queryList(ctEstbBVo);
//		
//		// 현재 커뮤니티를 제외한 목록
//		List<CtEstbBVo> otherEstbList = new ArrayList<CtEstbBVo>();
//		
//		for(CtEstbBVo storeEstbVo : ctEstbList){
//			if(!storeEstbVo.getCtId().equals(ctId)){
//				otherEstbList.add(storeEstbVo);
//			}
//		}
//		model.put("otherEstbList", otherEstbList);
//		
//		
//		if(selectCtId != null){
//			CtFncDVo ctFncDVo = new CtFncDVo();
//			ctFncDVo.setCtId(selectCtId);
//			ctFncDVo.setLangTyp(langTypCd);
//			// 자료실 구분 ID
//			ctFncDVo.setCtFncId("F000000M"); 
//			// 선택된 커뮤니티의 모든 자료실 목록
//			List<CtFncDVo> ctFncList = (List<CtFncDVo>) commonDao.queryList(ctFncDVo);
//			// 선택된 커뮤니티에서 쓰기 권한이 있는 자료실 목록
//			List<CtFncDVo> writeFncList = new ArrayList<CtFncDVo>();
//			
//			//사용자 권한
//			CtMbshDVo ctMbshDVo = new CtMbshDVo();
//			ctMbshDVo.setUserUid(userVo.getUserUid());
//			ctMbshDVo.setCtId(selectCtId);
//			ctMbshDVo.setLangTyp(langTypCd);
//			ctMbshDVo = (CtMbshDVo) commonDao.queryVo(ctMbshDVo);
//			ctMbshDVo.getUserSeculCd();
//			
//			for(CtFncDVo storeCtfncVo : ctFncList){
//				CtFncMbshAuthDVo ctFncMbshAuthDVo = new CtFncMbshAuthDVo();
//				ctFncMbshAuthDVo.setCtFncUid(storeCtfncVo.getCtFncUid());
//				ctFncMbshAuthDVo.setSeculCd(ctMbshDVo.getUserSeculCd());
//				ctFncMbshAuthDVo = (CtFncMbshAuthDVo) commonDao.queryVo(ctFncMbshAuthDVo);
//				String [] authArr = ctFncMbshAuthDVo.getAuthCd().split("\\/", -1);
//				for(int i=0; i < authArr.length ; i++){
//					if(authArr[i].equals("W")){
//						writeFncList.add(storeCtfncVo);
//					}
//				}
//			}
//			model.put("writeFncList", writeFncList);
//		}
//		
//		return LayoutUtil.getJspPath("/ct/selectCtPdsPop");
//	}
//	
//	/** 커뮤니티 게시물 보내기 POP */
//	@RequestMapping(value = "/ct/sendToPop")
//	public String sendToPop(HttpServletRequest request, ModelMap model)throws Exception{
//		String ctId = request.getParameter("ctId");
//		String bullId = request.getParameter("bullId");
//		
//		model.put("ctId", ctId);
//		model.put("bullId", bullId);
//		
//		return LayoutUtil.getJspPath("/ct/sendToPop");
//	}
//	
//	/** 커뮤니티 게시물 삭제 */
//	@RequestMapping(value = "/ct/pds/transBullDel")
//	public String transBullDel(HttpServletRequest request,
//			@RequestParam(value="data", required = true) String data,
//			ModelMap model){
//		
//		try{
//			
//			//파라미터 검사
//			JSONObject object = (JSONObject) JSONValue.parse(data);
//			String bullId = (String) object.get("bullId");
//			if(bullId == null){
//				throw new CmException("pt.msg.nodata.passed", request);
//			}
//			
//			// 세션의 언어코드
//			String langTypCd = LoginSession.getLangTypCd(request);
//			
//			// 게시물 테이블
//			CtRecMastBVo ctRecMastBVo = ctRecMastSvc.getCtRecMastBVo(Integer.parseInt(bullId));
//			if (ctRecMastBVo == null) {
//				// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
//				throw new CmException("ct.msg.bullNotExists", request);
//			}
//
//			//게시물 삭제
//			QueryQueue queryQueue = new QueryQueue();
//			ctRecMastSvc.deleteBoard(Integer.parseInt(bullId), queryQueue);
//			
//			//게시물 첨부파일 삭제
//			List<CommonFileVo> deletedFileList = ctFileSvc.deleteCtFile(bullId, queryQueue);
//			
//			commonDao.execute(queryQueue);
//			
//			//파일 삭제
//			ctFileSvc.deleteDiskFiles(deletedFileList);
//			
//			// cm.msg.del.success=삭제 되었습니다.
//			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
//			model.put("result", "ok");
//			
//			
//		}catch (CmException e) {
//			model.put("message", e.getMessage());
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage(), e);
//			model.put("message", e.getMessage());
//		}
//		
//		
//		return JsonUtil.returnJson(model);
//	}
//	
//	/** 커뮤니티 게시물 본문내용 이메일로 발송 */
//	@RequestMapping(value = "/ct/pds/sendMailBull")
//	public String sendMailBull(HttpServletRequest request,
//			@RequestParam(value = "data", required = true) String data,
//			ModelMap model) throws Exception{
//		
//		try {
//			// 세션의 사용자 정보
//			UserVo userVo = LoginSession.getUser(request);
//
//			// 파라미터 검사
//			JSONObject object = (JSONObject) JSONValue.parse(data);
//			JSONArray recvIds = (JSONArray) object.get("recvId");
//			String bullCont = (String) object.get("bullCont");
//			String bullId = (String) object.get("bullId");
////			if (recvIds == null) {
////				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
////				throw new CmException("pt.msg.nodata.passed", request);
////			}
//			
//			// 세션의 언어코드
//			String langTypCd = LoginSession.getLangTypCd(request);
//			
//			// 이메일 초기 정보 저장
//			QueryQueue queryQueue = new QueryQueue();
//			
//			//이메일 Vo[업무별 정보 세팅-제목,내용]
//			CmEmailBVo cmEmailBVo = new CmEmailBVo();
//			
//			List<CommonFileVo> allFileList = ctRecMastSvc.getFileVoList(bullId);
//			String Context = bullCont;
////			cmEmailBVo.setSubj(userVo.getUserNm() + "[" + wcsList  +"]");
//			cmEmailBVo.setCont(Context);
//			
//			//이메일 정보 저장
//			Integer emailId = commonEmailSvc.saveEmailInfo(cmEmailBVo , recvIds , allFileList , queryQueue , userVo);
//			
//			commonSvc.execute(queryQueue);
//			model.put("emailId", emailId);
//			model.put("result", "ok");
//			
//			
//		}catch (CmException e) {
//			model.put("message", e.getMessage());
//		}catch (Exception e) {
//			LOGGER.error(e.getMessage(), e);
//			model.put("message", e.getMessage());
//		}
//		
//		return JsonUtil.returnJson(model);
//	}
//	
//	/** 커뮤니티 자료실 게시글 조회 */
//	@RequestMapping(value = "/ct/pds/viewPds")
//	public String viewPds(HttpServletRequest request, 
//			@RequestParam(value = "bullId", required = true) String bullId,
//			ModelMap model) throws Exception {
//		if (bullId.isEmpty()) {
//			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
//			throw new CmException("pt.msg.nodata.passed", request);
//		}
//		
//		// 세션의 언어코드
//		String langTypCd = LoginSession.getLangTypCd(request);
//		
//		// 세션의 사용자 정보
//		UserVo userVo = LoginSession.getUser(request);
//		String ctId = request.getParameter("ctId");
//		ctCmntSvc.putLeftMenu(request, ctId, model);
//		
//		// 게시물 테이블
//		CtRecMastBVo ctRecMastBVo = ctRecMastSvc.getCtRecMastBVo(Integer.parseInt(bullId));
//		if(ctRecMastBVo == null){
//			// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
//			throw new CmException("ct.msg.bullNotExists", request);
//		}
//		model.put("ctRecMastBVo", ctRecMastBVo);
//		
//		//조회이력 저장
//		if(ctRecMastSvc.saveReadHst(bullId, userVo.getUserUid(), userVo.getCompId())){
//			ctRecMastSvc.addReadCnt(Integer.parseInt(bullId));
//		}
//		
//		// 게시물첨부파일 리스트 model에 추가
//		ctRecMastSvc.putFileListToModel(bullId, model);
//		
//		// 에디터 사용
//		model.addAttribute("JS_OPTS", new String[]{"editor"});
//		model.put("ctId", request.getParameter("ctId"));
//		
//		return LayoutUtil.getJspPath("/ct/pds/viewPds");
//		
//	}
//	
//	/** 커뮤니티 자료실 게시글 저장 */
//	@RequestMapping(value = "/ct/pds/transSetBullSave")
//	public String transSetBullSave(HttpServletRequest request, ModelMap model){
//		UploadHandler uploadHandler = null;
//		try{
//			// Multipart 파일 업로드
//			uploadHandler = ctFileSvc.upload(request);
//			
//			// MultipartRequest
//			request = uploadHandler.getMultipartRequest();
//			
//			// parameters
////			String listPage     = ParamUtil.getRequestParam(request, "listPage", true);
////			String viewPage     = ParamUtil.getRequestParam(request, "viewPage", true);
////			String brdId        = ParamUtil.getRequestParam(request, "brdId", true);
//			String bullId       = ParamUtil.getRequestParam(request, "bullId", false);
//			String bullStatCd   = ParamUtil.getRequestParam(request, "bullStatCd", true);
//			String bullRezvDt   = ParamUtil.getRequestParam(request, "bullRezvDt", false);
//			String bullExprDt   = ParamUtil.getRequestParam(request, "bullExprDt", false);
//			String ctFncUid = request.getParameter("menuId");
//			String ctId = request.getParameter("ctId");
////			if (listPage.isEmpty() || viewPage.isEmpty() || brdId.isEmpty() || bullStatCd.isEmpty()) {
//			if (bullStatCd.isEmpty()) {
//				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
//				throw new CmException("pt.msg.nodata.passed", request);
//			}
//			
//			// 세션의 언어코드
//			String langTypCd = LoginSession.getLangTypCd(request);
//			
//			// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
//			if ("B".equals(bullStatCd) && bullRezvDt != null && !bullRezvDt.isEmpty() && StringUtil.isAfterNow(bullRezvDt)) {
//				bullStatCd = "R";
//			}
//			
//			// 게시물 저장
//			QueryQueue queryQueue = new QueryQueue();
//			Integer storedBullId = ctRecMastSvc.saveCtBullLVo(request, bullId, bullStatCd, queryQueue);
//			
//			// 게시파일 저장
//			List<CommonFileVo> deletedFileList = ctFileSvc.saveSurvFile(request, String.valueOf(storedBullId), queryQueue, ctFncUid, ctId);
//			
//			commonSvc.execute(queryQueue);
//			//ctFileSvc.deleteDiskFiles(deletedFileList);
//			
//			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
//			
//			boolean goView = false;
//			// 수정이고, 게시예약일과 게시완료일 사이이고, 보안글이 아니면 조회화면으로 이동
//			if (bullId != null && !bullId.isEmpty()) {
//				goView = true;
//			}	
//			
//			if (goView) {
//				model.put("todo", "parent.location.replace('/ct/pds/viewPds.do?menuId="+request.getParameter("menuId")+"&ctId="+ctId+"&bullId="+bullId+"');");
//			} else {
//				model.put("todo", "parent.location.replace('/ct/pds/listPds.do?menuId="+request.getParameter("menuId")+"&ctId="+ctId+"');");
//			}
//		}catch (CmException e) {
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage(), e);
//			model.put("exception", e);
//		} finally {
//			if (uploadHandler != null) try { uploadHandler.removeTempDir(); } catch (CmException ignored) {}
//		}
//
//		return LayoutUtil.getResultJsp();
//	}
//	
//	/** 커뮤니티 자료실 게시글 등록 페이지 */
//	@RequestMapping(value = "/ct/pds/setPds")
//	public String setPds(HttpServletRequest request, 
//			@RequestParam(value = "bullId", required = false) String bullId,			
//			ModelMap model) throws Exception {
//		
//		CtRecMastBVo ctRecMastBVo = null;
//		
//		// 수정인 경우
//		if (bullId != null && !bullId.isEmpty()) {
//			ctRecMastBVo = ctRecMastSvc.getCtRecMastBVo(Integer.parseInt(bullId));
//			
//			if(ctRecMastBVo == null){
//				// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
//				throw new CmException("ct.msg.bullNotExists", request);
//			}
//			model.put("ctRecMastBVo", ctRecMastBVo);
//			
//			// 날짜 초기화
//			ctRecMastSvc.initBullRezvDt(ctRecMastBVo);
//			ctRecMastSvc.initBullExprDt(ctRecMastBVo);
//		}else{
//			ctRecMastBVo = new CtRecMastBVo();
//			// 날짜 초기화
//			ctRecMastSvc.initBullRezvDt(ctRecMastBVo);
//			ctRecMastSvc.initBullExprDt(ctRecMastBVo);
//			
//			model.put("ctRecMastBVo", ctRecMastBVo);
//		}
//		
//		// 게시물첨부파일 리스트 model에 추가
//		ctFileSvc.putFileListToModel(bullId, model);
//		
//		// 에디터 사용
//		model.addAttribute("JS_OPTS", new String[]{"editor"});
//		
//		// 최대 본문 사이즈 model에 추가
//		ctRecMastSvc.putBodySizeToModel(request, model);
//		
//		// params
//		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId"));
//		
//		String ctId = request.getParameter("ctId");
//		ctCmntSvc.putLeftMenu(request, ctId, model);
//		model.put("ctId", request.getParameter("ctId"));
//		model.put("bullCtFncId", request.getParameter("bullCtFncId"));
//		model.put("bullCtFncUid", request.getParameter("bullCtFncUid"));
//		model.put("bullCtFncPid", request.getParameter("bullCtFncPid"));
//		model.put("bullCtFncLocStep", request.getParameter("bullCtFncLocStep"));
//		model.put("bullCtFncOrdr", request.getParameter("bullCtFncOrdr"));
//		
//		return LayoutUtil.getJspPath("/ct/pds/setPds");
//	}
//	
//	/** 커뮤니티 해당 자료실 게시글 목록 조회 */
//	@RequestMapping(value = "/ct/pds/listPds")
//	public String listPds(HttpServletRequest request, ModelMap model)throws Exception{
//		//세션의 언어코드
//		String langTypCd = LoginSession.getLangTypCd(request);
//		
//		//세션의 사용자 정보
//		UserVo userVo = LoginSession.getUser(request);
//		
//		String ctFncUid = request.getParameter("menuId");
//		String ctId = request.getParameter("ctId");
//		ctCmntSvc.putLeftMenu(request, ctId, model);
//		
//		//fncUid&CtId로 CT_FNC_D의 해당 Vo를 가지고 온다.
//		CtFncDVo ctFncDVo = new CtFncDVo();
//		ctFncDVo.setCtFncUid(ctFncUid);
//		ctFncDVo.setCtId(ctId);
//		ctFncDVo = (CtFncDVo) commonDao.queryVo(ctFncDVo);
//		
//		//해당 자료실 게시글 목록
//		CtRecMastBVo ctRecMastBVo = new CtRecMastBVo();
//		VoUtil.bind(request, ctRecMastBVo);
//		ctRecMastBVo.setCtId(ctFncDVo.getCtId());
//		ctRecMastBVo.setCtFncId(ctFncDVo.getCtFncId());
//		ctRecMastBVo.setCtFncUid(ctFncDVo.getCtFncUid());
//		ctRecMastBVo.setCtFncPid(ctFncDVo.getCtFncPid());
//		ctRecMastBVo.setCtFncLocStep(ctFncDVo.getCtFncLocStep());
//		ctRecMastBVo.setCtFncOrdr(ctFncDVo.getCtFncOrdr());
//		
//		Integer recodeCount = commonDao.count(ctRecMastBVo);
//		PersonalUtil.setPaging(request, ctRecMastBVo, recodeCount);
//		
//		List<CtRecMastBVo> ctRecList = (List<CtRecMastBVo>) commonDao.queryList(ctRecMastBVo);
//		
//		model.put("recodeCount", recodeCount);
//		model.put("ctRecList", ctRecList);
//		model.put("ctFncDVo", ctFncDVo);
//		model.put("ctId", ctId);
//		
//		return LayoutUtil.getJspPath("/ct/pds/listPds");
//	}
//
//}
