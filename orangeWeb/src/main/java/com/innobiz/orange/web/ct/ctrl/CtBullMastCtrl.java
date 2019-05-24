package com.innobiz.orange.web.ct.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.dao.LobHandler;
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
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtBullMastSvc;
import com.innobiz.orange.web.ct.svc.CtCmSvc;
import com.innobiz.orange.web.ct.svc.CtCmntSvc;
import com.innobiz.orange.web.ct.svc.CtFileSvc;
import com.innobiz.orange.web.ct.svc.CtRecmdHstSvc;
import com.innobiz.orange.web.ct.svc.CtScreHstSvc;
import com.innobiz.orange.web.ct.vo.CtBullMastBVo;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFileDVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtFncMbshAuthDVo;
import com.innobiz.orange.web.ct.vo.CtMbshDVo;
import com.innobiz.orange.web.ct.vo.CtScreHstLVo;
import com.innobiz.orange.web.ct.vo.CtVistrHstDVo;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/** 커뮤니티 게시물 */
@Controller
public class CtBullMastCtrl {
	
	/** 커뮤니티 게시판 서비스 */
	@Autowired
	private CtBullMastSvc ctBullMastSvc;
	
	/** 게시물 첨부파일  */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
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
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 추천이력 서비스 */
	@Autowired
	private CtRecmdHstSvc ctRecmdHstSvc;
	
	/** 점수이력 서비스 */
	@Autowired
	private CtScreHstSvc ctScreHstSvc;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CtBullMastCtrl.class);
	
	/** [팝업] 조회자 정보 (사용자) */
	@RequestMapping(value = {"/ct/board/listReadHstPop"})
	public String listReadHstPop(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/ct/board/listReadHstPop");
	}
	
	/** [팝업] 조회자 정보 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/ct/board/listReadHstFrm"})
	public String listReadHstFrm(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 조회이력(BA_READ_HST_L) 테이블 - SELECT
		CtVistrHstDVo ctVistrHstDVo = new CtVistrHstDVo();
		ctVistrHstDVo.setQueryLang(langTypCd);
		ctVistrHstDVo.setCtId(bullId);

		Integer recodeCount = commonSvc.count(ctVistrHstDVo);
		PersonalUtil.setPaging(request, ctVistrHstDVo, recodeCount);
		
		List<CtVistrHstDVo> ctVistrHstLVoList = (List<CtVistrHstDVo>) commonSvc.queryList(ctVistrHstDVo);
		
		for (CtVistrHstDVo readHstVo : ctVistrHstLVoList) {
			// 사용자기본(OR_USER_B) 테이블 - SELECT
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setQueryLang(langTypCd);
			orUserBVo.setUserUid(readHstVo.getUserUid());
			orUserBVo = (OrUserBVo) commonSvc.queryVo(orUserBVo);
			readHstVo.setOrUserBVo(orUserBVo);
			
			// 원직자기본(OR_ODUR_B) 테이블 - SELECT
			OrOdurBVo orOdurBVo = new OrOdurBVo();
			orOdurBVo.setOdurUid(orUserBVo.getOdurUid());
			orOdurBVo = (OrOdurBVo) commonSvc.queryVo(orOdurBVo);
			readHstVo.setOrOdurBVo(orOdurBVo);
		}
		
		model.put("ctVistrHstLVoList", ctVistrHstLVoList);
		model.put("recodeCount", recodeCount);
		
		return LayoutUtil.getJspPath("/ct/board/listReadHstFrm");
	}
	
	/** 커뮤니티 게시물 보내기 */
	@RequestMapping(value = "/ct/sendSetBull")
	public String sendSetBull(HttpServletRequest request,
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String selectCtFncUid = (String) object.get("selectCtFncUid");
			String ctId = (String) object.get("ctId");
			String bullId = (String) object.get("bullId");
			CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
			ctBullMastBVo.setWithLob(true);
			ctBullMastBVo = ctBullMastSvc.getCtBullMastBVo(Integer.parseInt(bullId), langTypCd);
			
			if(ctBullMastBVo == null){
				// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("ct.msg.bullNotExists", request);
			}
			
			ctBullMastBVo.setSubj(null);
			model.put("ctBullMastBVo", ctBullMastBVo);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 게시물첨부파일 리스트 model에 추가
			ctFileSvc.putFileListToModel(bullId, model, userVo.getCompId());
			
			// 에디터 사용
			model.addAttribute("JS_OPTS", new String[]{"editor"});
			
			// 최대 본문 사이즈 model에 추가
			ctBullMastSvc.putBodySizeToModel(request, model);
			
			// params
			model.addAttribute("params", ParamUtil.getQueryString(request, "bullId"));
			
			ctCmntSvc.putLeftMenu(request, ctId, model);
			model.put("ctId", ctId);
			model.put("selectCtFncUid", selectCtFncUid);
			model.put("result", "ok");
			
			}catch (CmException e) {
				model.put("message", e.getMessage());
			}catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				model.put("message", e.getMessage());
			}
			return JsonUtil.returnJson(model);
	}
	
	/** 커뮤니티 자료실 게시물 보내기 */
	@RequestMapping(value = "/ct/sendPsd")
	public String sendPsd(HttpServletRequest request, 
			@RequestParam(value="data", required = true) String data,
			ModelMap model){
		
		try{
			//파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			//String ctId = (String) object.get("ctId");
			String bullId = (String) object.get("bullId");
			String ctFncUid = (String) object.get("ctFncUid");
			String selectCtId = (String) object.get("selectCtId");
			// MultipartRequest
			//request = uploadHandler.getMultipartRequest();
			// Multipart 파일 업로드
			//uploadHandler = ctFileSvc.upload(request);
			
			CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
			ctBullMastBVo.setBullId(Integer.parseInt(bullId));
			ctBullMastBVo = (CtBullMastBVo) commonDao.queryVo(ctBullMastBVo);
			// 게시물 저장
			QueryQueue queryQueue = new QueryQueue();
			Integer storedBullId = ctBullMastSvc.sendCtBullLVo(request, bullId, selectCtId, ctFncUid, queryQueue);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 파일 저장
			CtFileDVo ctFileDVo = new CtFileDVo();
			ctFileDVo.setRefId(bullId);
			@SuppressWarnings("unchecked")
			List<CtFileDVo> ctFileList = (List<CtFileDVo>) commonDao.queryList(ctFileDVo);
			if(ctFileList.size() != 0){
				for(CtFileDVo storeFileVo : ctFileList){
					CtFileDVo clonFileVo = new CtFileDVo();
					BeanUtils.copyProperties(storeFileVo, clonFileVo, new String[]{"refId", "savePath", "regrUid", "regDt"});
					clonFileVo.setFileId(ctCmSvc.createFileId("CT_FILE_D"));
					clonFileVo.setRefId(String.valueOf(storedBullId));
					
					// 파일 새이름으로 복사 후 파일 배포
					String newSavePath = ctFileSvc.copyAndDist(request, "/ct", storeFileVo.getSavePath(), storeFileVo.getFileExt());
					
					clonFileVo.setSavePath(newSavePath);
					clonFileVo.setRegrUid(userVo.getUserUid());
					clonFileVo.setRegDt("sysdate");
					queryQueue.insert(clonFileVo);
				}
			}

			commonDao.execute(queryQueue);
			
			model.put("result", "ok");
				
		}catch (CmException e) {
			model.put("message", e.getMessage());
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		return JsonUtil.returnJson(model);
	}
	
	/** 현재 커뮤니티 게시판 Pop */
	@RequestMapping(value = "/ct/selectCtBoardPop")
	public String selectCtBoardPop(HttpServletRequest request, ModelMap model)throws Exception{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String ctId = request.getParameter("ctId");
		String bullId = request.getParameter("bullId");
		String fncUid = request.getParameter("menuId");
		
		
		model.put("ctId", ctId);
		model.put("bullId", bullId);
		
		//커뮤니티 Vo얻기
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		//ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLogUserUid(userVo.getUserUid());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		
		// 현재 커뮤니티 
		ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		
		model.put("ctEstbBVo", ctEstbBVo);
		
	
		CtFncDVo ctFncDVo = new CtFncDVo();
		ctFncDVo.setCtId(ctId);
		ctFncDVo.setLangTyp(langTypCd);
		// 게시판 구분 ID
		ctFncDVo.setCtFncId("F0000001"); 
		// 현재 커뮤니티의 모든 게시판 목록
		@SuppressWarnings("unchecked")
		List<CtFncDVo> ctFncList = (List<CtFncDVo>) commonDao.queryList(ctFncDVo);
		
		// 현재 게시판을 제외한 나머지 게시판 목록
		List<CtFncDVo> otherCtFncList = new ArrayList<CtFncDVo>();
		for(CtFncDVo storeFncDvo : ctFncList){
			if(!storeFncDvo.getCtFncUid().equals(fncUid)){
				otherCtFncList.add(storeFncDvo);
			}
		}
		
		
		// 선택된 커뮤니티에서 쓰기 권한이 있는 자료실 목록
		List<CtFncDVo> writeFncList = new ArrayList<CtFncDVo>();
		
		//사용자 권한
		CtMbshDVo ctMbshDVo = new CtMbshDVo();
		ctMbshDVo.setUserUid(userVo.getUserUid());
		ctMbshDVo.setCtId(ctId);
		ctMbshDVo.setLangTyp(langTypCd);
		ctMbshDVo = (CtMbshDVo) commonDao.queryVo(ctMbshDVo);
		ctMbshDVo.getUserSeculCd();
		
		for(CtFncDVo storeCtfncVo : otherCtFncList){
			CtFncMbshAuthDVo ctFncMbshAuthDVo = new CtFncMbshAuthDVo();
			ctFncMbshAuthDVo.setCtFncUid(storeCtfncVo.getCtFncUid());
			ctFncMbshAuthDVo.setSeculCd(ctMbshDVo.getUserSeculCd());
			ctFncMbshAuthDVo = (CtFncMbshAuthDVo) commonDao.queryVo(ctFncMbshAuthDVo);
			String [] authArr = ctFncMbshAuthDVo.getAuthCd().split("\\/", -1);
			for(int i=0; i < authArr.length ; i++){
				if(authArr[i].equals("W")){
					writeFncList.add(storeCtfncVo);
				}
			}
		}
		model.put("writeFncList", writeFncList);
		
		return LayoutUtil.getJspPath("/ct/selectCtBoardPop");
	}
	
	/** 타 커뮤니티 목록 Pop */
	@RequestMapping(value = "/ct/selectCtPdsPop")
	public String selectCtPdsPop(HttpServletRequest request, ModelMap model)throws Exception{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String ctId = request.getParameter("ctId");
		String bullId = request.getParameter("bullId");
		String selectCtId= request.getParameter("selectCtId");
		
		model.put("ctId", ctId);
		model.put("bullId", bullId);
		model.put("selectCtId", selectCtId);
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		//ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setLogUserUid(userVo.getUserUid());
		ctEstbBVo.setQueryLang(langTypCd);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setCtStat("A");
		ctEstbBVo.setCtActStat("A");
		ctEstbBVo.setSignal("my");
		
		// 나의 커뮤니티 전체 목록
		@SuppressWarnings("unchecked")
		List<CtEstbBVo> ctEstbList = (List<CtEstbBVo>) commonDao.queryList(ctEstbBVo);
		
		// 현재 커뮤니티를 제외한 목록
		List<CtEstbBVo> otherEstbList = new ArrayList<CtEstbBVo>();
		
		for(CtEstbBVo storeEstbVo : ctEstbList){
			if(!storeEstbVo.getCtId().equals(ctId)){
				otherEstbList.add(storeEstbVo);
			}
		}
		model.put("otherEstbList", otherEstbList);
		
		if(selectCtId != null){
			CtFncDVo ctFncDVo = new CtFncDVo();
			ctFncDVo.setCtId(selectCtId);
			ctFncDVo.setLangTyp(langTypCd);
			// 자료실 구분 ID
			ctFncDVo.setCtFncId("F0000001"); 
			// 선택된 커뮤니티의 모든 자료실 목록
			@SuppressWarnings("unchecked")
			List<CtFncDVo> ctFncList = (List<CtFncDVo>) commonDao.queryList(ctFncDVo);
			// 선택된 커뮤니티에서 쓰기 권한이 있는 자료실 목록
			List<CtFncDVo> writeFncList = new ArrayList<CtFncDVo>();
			
			//사용자 권한
			CtMbshDVo ctMbshDVo = new CtMbshDVo();
			ctMbshDVo.setUserUid(userVo.getUserUid());
			ctMbshDVo.setCtId(selectCtId);
			ctMbshDVo.setLangTyp(langTypCd);
			ctMbshDVo = (CtMbshDVo) commonDao.queryVo(ctMbshDVo);
			ctMbshDVo.getUserSeculCd();
			
			for(CtFncDVo storeCtfncVo : ctFncList){
				CtFncMbshAuthDVo ctFncMbshAuthDVo = new CtFncMbshAuthDVo();
				ctFncMbshAuthDVo.setCtFncUid(storeCtfncVo.getCtFncUid());
				ctFncMbshAuthDVo.setSeculCd(ctMbshDVo.getUserSeculCd());
				ctFncMbshAuthDVo = (CtFncMbshAuthDVo) commonDao.queryVo(ctFncMbshAuthDVo);
				
				String [] authArr = ctFncMbshAuthDVo.getAuthCd().split("\\/", -1);
				for(int i=0; i < authArr.length ; i++){
					if(authArr[i].equals("W")){
						writeFncList.add(storeCtfncVo);
					}
				}
			}
			model.put("writeFncList", writeFncList);
		}
		
		return LayoutUtil.getJspPath("/ct/selectCtPdsPop");
		
	}
	
	
	
	
	/** 커뮤니티 게시물 보내기 POP */
	@RequestMapping(value = "/ct/sendToPop")
	public String sendToPop(HttpServletRequest request, ModelMap model)throws Exception{
		String ctId = request.getParameter("ctId");
		String bullId = request.getParameter("bullId");
		
		model.put("ctId", ctId);
		model.put("bullId", bullId);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		
		return LayoutUtil.getJspPath("/ct/sendToPop");
	}
	
	/** 점수내역 */
	@RequestMapping(value = "/ct/board/viewScrePop")
	public String viewScrePop(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			ModelMap model) throws SQLException {
		// 평균점수
		Integer avgScre = ctScreHstSvc.getAvgScre(Integer.valueOf(bullId));
		model.put("avgScre", avgScre);
		
		// 점수 목록
		List<CtScreHstLVo> ctScreHstLVoList = ctScreHstSvc.getCtScreHstLVoList(request, Integer.parseInt(bullId));
		model.put("ctScreHstLVoList", ctScreHstLVoList);

		return LayoutUtil.getJspPath("/ct/board/viewScrePop");
		
	}
	
	/** [AJAX] 게시물 점수주기 (사용자) */
	@RequestMapping(value = "/ct/board/transBullScreAjx")
	public String transBullScreAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String bullId = (String) object.get("bullId");
			String strScre = (String) object.get("scre");
			int scre = Integer.parseInt(strScre);
			if (bullId == null || scre < 1 || scre > 5) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시물 테이블
			CtBullMastBVo ctBullMastBVo = ctBullMastSvc.getCtBullMastBVo(Integer.parseInt(bullId), langTypCd);
			if(ctBullMastBVo == null){
				// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("ct.msg.bullNotExists", request);
			}
			QueryQueue queryQueue = new QueryQueue();
			
			// 점수주기 존재여부
			if (ctScreHstSvc.isScreHstExist(bullId, userVo.getUserUid())) {
				// bb.msg.scre.already=이미 점수를 준 게시물입니다.
				throw new CmException("ct.msg.scre.already", request);
			} else {
				// 점수주기 저장
				ctScreHstSvc.insertScreHst(bullId, userVo.getUserUid(), scre, queryQueue);
				// 점수 업데이트
				ctScreHstSvc.updateScre(Integer.parseInt(bullId), queryQueue);
			}
			
			commonSvc.execute(queryQueue);

			// ct.msg.scre.success=점수를 저장하였습니다.
			model.put("message", messageProperties.getMessage("ct.msg.scre.success", request));
			model.put("result", "ok");
		}catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
 			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
		
	}
	
	
	/** [AJAX] 게시물 추천 (사용자) */
	@RequestMapping(value = "/ct/board/transBullRecmdAjx")
	public String transBullRecmdAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String bullId = (String) object.get("bullId");
			if (bullId == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시물 테이블
			CtBullMastBVo ctBullMastBVo = ctBullMastSvc.getCtBullMastBVo(Integer.parseInt(bullId), langTypCd);
			if(ctBullMastBVo == null){
				// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("ct.msg.bullNotExists", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			// 추천이력 존재여부
			if(ctRecmdHstSvc.isRecmdHstExist(bullId, userVo.getUserUid())){
				// ct.msg.recmd.already = 이미 추천하였습니다.
				throw new CmException("ct.msg.recmd.already", request);
			} else {
				// 추천이력 저장
				ctRecmdHstSvc.insertRecmdHst(bullId, userVo.getUserUid(), queryQueue);
				// 추천수 증가
				ctRecmdHstSvc.addRecmdCnt(Integer.parseInt(bullId), queryQueue);
			}
			
			commonSvc.execute(queryQueue);

			// bb.msg.recmd.success=추천하였습니다.
			model.put("message", messageProperties.getMessage("bb.msg.recmd.success", request));
			model.put("result", "ok");
			model.put("recmdCnt", ctBullMastBVo.getRecmdCnt() + 1);
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
 			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	
	
	
	/** 커뮤니티 게시물 답변 저장 */
	@RequestMapping(value = "/ct/board/transSetReplySave")
	public String transSetReplySave(HttpServletRequest request,
			ModelMap model) {
		
		UploadHandler uploadHandler = null;
		try {

			// Multipart 파일 업로드
			uploadHandler = ctFileSvc.upload(request);
	
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// parameters
			String bullPid      = ParamUtil.getRequestParam(request, "bullPid", true);
			String bullRezvDt   = ParamUtil.getRequestParam(request, "bullRezvDt", false);
			String ctFncUid = request.getParameter("menuId");
			String ctId = request.getParameter("ctId");
			
			if (bullPid.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
			String bullStatCd = "B";
			if (bullRezvDt != null && !bullRezvDt.isEmpty() && StringUtil.isAfterNow(bullRezvDt)) {
				bullStatCd = "R";
			}
			
			// 게시물 답변 저장
			QueryQueue queryQueue = new QueryQueue();
			Integer newBullId = ctBullMastSvc.saveReplyBbBullLVo(request, bullStatCd, bullPid, queryQueue);
			
			// 게시파일 저장
			List<CommonFileVo> deletedFileList = ctFileSvc.saveSurvFile(request, String.valueOf(newBullId), queryQueue, ctFncUid, ctId);
			
			commonSvc.execute(queryQueue);
			
			ctFileSvc.deleteDiskFiles(deletedFileList);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('/ct/board/viewBoard.do?menuId="+request.getParameter("menuId")+"&ctId="+ctId+"&bullId="+newBullId+"');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
		
	}
	
	/** 커뮤니티 게시물 답변 등록 화면 */
	@RequestMapping(value = "/ct/board/setReply")
	public String setReply(HttpServletRequest request,
			@RequestParam(value = "bullPid", required = false) String bullPid,
			ModelMap model) throws Exception {
		if (bullPid.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		CtBullMastBVo parentBullMastBVo = ctBullMastSvc.getCtBullMastBVo(Integer.parseInt(bullPid), langTypCd, true);
		model.put("parentBullMastBVo", parentBullMastBVo);
		
		CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
		
		//날짜 초기화
		ctBullMastSvc.initBullRezvDt(ctBullMastBVo);
		ctBullMastSvc.initBullExprDt(ctBullMastBVo);
		
		// 제목 초기화
		ctBullMastBVo.setSubj("Re: " + parentBullMastBVo.getSubj());
		
		// 내용 초기화
		StringBuilder cont = new StringBuilder();
		cont.append("<br/><br/>--------------------------------------------- ")
			.append(messageProperties.getMessage("bb.jsp.setReply.tx01", request))  // bb.jsp.setReply.tx01=원본 내용
			.append(" ---------------------------------------------<br/><br/>");
		if(parentBullMastBVo.getCont()!=null && !parentBullMastBVo.getCont().isEmpty())
			cont.append(parentBullMastBVo.getCont());
		ctBullMastBVo.setCont(cont.toString());
		model.put("ctBullMastBVo", ctBullMastBVo);
		
		// 기본값 세팅
		ctBullMastSvc.initBullRezvDt(parentBullMastBVo);

//		// 게시대상 model에 추가
//		ctBullMastSvc.putTgtListToModel(parentBullMastBVo, model);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 게시물첨부파일 리스트 model에 추가
		ctBullMastSvc.putFileListToModel(null, model, userVo.getCompId());
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		// 최대 본문 사이즈 model에 추가
		ctBullMastSvc.putBodySizeToModel(request, model);
		
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullPid"));
		model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "bullPid"));
		
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		model.put("ctId", request.getParameter("ctId"));
		model.put("bullCtFncId", parentBullMastBVo.getCtFncId());
		model.put("bullCtFncUid", parentBullMastBVo.getCtFncUid());
		model.put("bullCtFncPid", parentBullMastBVo.getCtFncPid());
		model.put("bullCtFncLocStep", parentBullMastBVo.getCtFncLocStep());
		model.put("bullCtFncOrdr", parentBullMastBVo.getCtFncOrdr());
		
		model.put("bullPid", bullPid);
		
		String fncUid = request.getParameter("menuId");
				
		/**
		 * 커뮤니티 기능정보에서 메뉴명 가져오기
		 */
		CtFncDVo ctFncDVo =  new CtFncDVo();
		//ctFncDVo.setCompId(userVo.getCompId());
		ctFncDVo.setCtFncUid(fncUid);
		ctFncDVo.setLangTyp(langTypCd);
		CtFncDVo CtFncDVo =  (CtFncDVo) commonDao.queryVo(ctFncDVo);
		model.put("menuTitle", CtFncDVo.getCtFncNm());
		
		return LayoutUtil.getJspPath("/ct/board/setBoard");
	}
	
	/** 커뮤니티 게시물 본문내용 이메일로 발송 */
	@RequestMapping(value = "/ct/board/transEmailAjx")
	public String transEmailAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String bullId = (String)object.get("bullId");
			JSONArray recvIds = (JSONArray) object.get("recvIds");
			
			
			if (bullId == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// jsonArray size만큼 String[] 생성
		
//			if(recvIds != null){
//				String[][] recvList = new String[recvIds.size()][2];
//				
//				// jsonArray를 String[]에 담는다.
//				for(int i=0;i<recvIds.size();i++){
//					
//				//Email 정보조회
//				OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
//				orUserPinfoDVo.setOdurUid((String) recvIds.get(i));
//				orUserPinfoDVo = commonEmailSvc.getOrUserPinfoDVo(orUserPinfoDVo);
//				// 사용자 정보 조회
//				OrUserBVo orUserBVo = new OrUserBVo();
//				orUserBVo.setUserUid((String) recvIds.get(i));
//				orUserBVo.setQueryLang(langTypCd);
//				orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
//				
//				recvList[i][0] = orUserBVo.getUserNm();
//				recvList[i][1] = orUserPinfoDVo.getEmail();
//				
//				}
//			}else{
//				recvIds = null;
//			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 이메일 초기 정보 저장
			QueryQueue queryQueue = new QueryQueue();
			
			CtBullMastBVo ctBullMastBVo = ctBullMastSvc.getCtBullMastBVo(Integer.parseInt(bullId), langTypCd);
			
			//이메일 Vo[업무별 정보 세팅-제목,내용]
			CmEmailBVo cmEmailBVo = new CmEmailBVo();
			
//			List<CommonFileVo> allFileList = new ArrayList<CommonFileVo>();
//			String Context = bullCont;
			cmEmailBVo.setSubj(ctBullMastBVo.getSubj());
			cmEmailBVo.setCont(ctBullMastBVo.getCont());
			
			CtFileDVo ctFileDVo = new CtFileDVo();
			ctFileDVo.setRefId(bullId);
			@SuppressWarnings("unchecked")
			List<CommonFileVo> fileList = (List<CommonFileVo>) commonSvc.queryList(ctFileDVo);
			
			//이메일 정보 저장
			Integer emailId = emailSvc.saveEmailInfo(cmEmailBVo , recvIds , fileList , queryQueue , userVo);
			
			commonSvc.execute(queryQueue);
			//메세지 처리
			emailSvc.setEmailMessage(model, request, emailId);
			
			
		}catch (CmException e) {
			model.put("message", e.getMessage());
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 커뮤니티 게시물 삭제 */
	@RequestMapping(value = "/ct/board/transBullDel")
	public String transBullDel(HttpServletRequest request,
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
			CtBullMastBVo ctBullMastBVo = ctBullMastSvc.getCtBullMastBVo(Integer.parseInt(bullId), langTypCd);
			
			if (ctBullMastBVo == null) {
				// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("ct.msg.bullNotExists", request);
			}
			
			
			CtBullMastBVo bullMastBVo = new CtBullMastBVo();
			bullMastBVo.setBullPid(Integer.parseInt(bullId));
			if(commonSvc.count(bullMastBVo) > 0){
				// ct.msg.deleteBull.hasReply=답변글이 있는 게시물은 삭제할 수 없습니다.
				throw new CmException("ct.msg.deleteBull.hasReply", request);
			}

			//게시물 삭제
			QueryQueue queryQueue = new QueryQueue();
			ctBullMastSvc.deleteBoard(Integer.parseInt(bullId), queryQueue);
			
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
	
	/** 커뮤니티 게시물 조회 */
	@RequestMapping(value = "/ct/board/viewBoard")
	public String viewBoard(HttpServletRequest request, 
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
		model.put("logUserUid", userVo.getUserUid());
		
		String fncUid = request.getParameter("menuId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLangTyp(langTypCd);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		// 게시물 테이블
		CtBullMastBVo ctBullMastBVo = ctBullMastSvc.getCtBullMastBVo(Integer.parseInt(bullId), langTypCd, false);
		if(ctBullMastBVo == null){
			// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("ct.msg.bullNotExists", request);
		}
		//lobHandler
		model.put("lobHandler", lobHandler.create(
				"SELECT CONT FROM CT_BULL_MAST_B WHERE BULL_ID = ?", 
				new String[]{bullId}
		));
		model.put("ctBullMastBVo", ctBullMastBVo);
		
		//조회이력 저장
		if(ctBullMastSvc.saveReadHst(bullId, userVo.getUserUid(), ctEstbBVo.getCompId())){
			ctBullMastSvc.addReadCnt(Integer.parseInt(bullId));
		}
		
		
		// 관련글 목록
		CtBullMastBVo bullMastBVo = new CtBullMastBVo();
		bullMastBVo.setQueryLang(langTypCd);
		bullMastBVo.setReplyGrpId(ctBullMastBVo.getReplyGrpId());
		bullMastBVo.setCtFncUid(ctBullMastBVo.getCtFncUid());
		//bullMastBVo.setCtId(ctId);
		String orderBy = "CBMB_T.REPLY_GRP_ID DESC, CBMB_T.REPLY_ORDR ASC";
		bullMastBVo.setOrderBy(orderBy);
		@SuppressWarnings("unchecked")
		List<CtBullMastBVo> replyBullList = (List<CtBullMastBVo>) commonSvc.queryList(bullMastBVo);
		model.put("replyBullList", replyBullList);
		
		
		// 이전글/다음글
		CtBullMastBVo paramBullVo = new CtBullMastBVo();
		VoUtil.bind(request, paramBullVo);
		paramBullVo.setReplyDpth(0);
		paramBullVo.setBullId(ctBullMastBVo.getReplyGrpId());
		paramBullVo.setCtFncUid(ctBullMastBVo.getCtFncUid());
		
		// PREV
		paramBullVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtBullMastBDao.selectPrevId");
		Integer prevId = commonSvc.queryInt(paramBullVo);
		if (prevId != null) {
			// 게시물(BB_X000X_L) 테이블 - SELECT
			CtBullMastBVo prevBullVo = ctBullMastSvc.getCtBullMastBVo(prevId, langTypCd, false);
			model.put("prevBullVo", prevBullVo);
		}
		// NEXT
		paramBullVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtBullMastBDao.selectNextId");
		Integer nextId = commonSvc.queryInt(paramBullVo);
		if (nextId != null) {
			// 게시물(BB_X000X_L) 테이블 - SELECT
			CtBullMastBVo nextBullVo = ctBullMastSvc.getCtBullMastBVo(nextId, langTypCd, false);
			model.put("nextBullVo", nextBullVo);
		}
		
		// 게시물첨부파일 리스트 model에 추가
		ctBullMastSvc.putFileListToModel(bullId, model, ctEstbBVo.getCompId());
		
		// 기타(점수, 추천, 찬반투표) 참가여부 model에 추가
		ctBullMastSvc.putEtcToModel(request, bullId, model);
		
		
		ctCmntSvc.putAuthChk(request, model, "R", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "D", ctId, fncUid);
		model.put("myAuth", ctCmntSvc.getUserSeculCd(ctEstbBVo.getCompId(), userVo.getUserUid(), ctId));
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		model.put("ctId", request.getParameter("ctId"));
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		
		return LayoutUtil.getJspPath("/ct/board/viewBoard");
	}
	
	/** 커뮤니티 게시글 저장 */
	@RequestMapping(value = "/ct/board/transSetBullSave")
	public String transSetBullSave(HttpServletRequest request, ModelMap model){
		UploadHandler uploadHandler = null;
		try{
			// Multipart 파일 업로드
			uploadHandler = ctFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// parameters
//			String listPage     = ParamUtil.getRequestParam(request, "listPage", true);
//			String viewPage     = ParamUtil.getRequestParam(request, "viewPage", true);
//			String brdId        = ParamUtil.getRequestParam(request, "brdId", true);
			String bullId       = ParamUtil.getRequestParam(request, "bullId", false);
			String bullStatCd   = ParamUtil.getRequestParam(request, "bullStatCd", true);
			String bullRezvDt   = ParamUtil.getRequestParam(request, "bullRezvDt", false);
			//String bullExprDt   = ParamUtil.getRequestParam(request, "bullExprDt", false);
			String ctFncUid = request.getParameter("menuId");
			String ctId = request.getParameter("ctId");
//			if (listPage.isEmpty() || viewPage.isEmpty() || brdId.isEmpty() || bullStatCd.isEmpty()) {
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
			Integer storedBullId = ctBullMastSvc.saveCtBullLVo(request, bullId, bullStatCd, queryQueue);
			
			// 게시파일 저장
			//List<CommonFileVo> deletedFileList = ctFileSvc.saveSurvFile(request, String.valueOf(storedBullId), queryQueue, ctFncUid, ctId);
			ctFileSvc.saveSurvFile(request, String.valueOf(storedBullId), queryQueue, ctFncUid, ctId);
			commonSvc.execute(queryQueue);
			//ctFileSvc.deleteDiskFiles(deletedFileList);
			
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			boolean goView = false;
			// 수정이고, 게시예약일과 게시완료일 사이이고, 보안글이 아니면 조회화면으로 이동
			if (bullId != null && !bullId.isEmpty()) {
				goView = true;
			}	
			
			if (goView) {
				model.put("todo", "parent.location.replace('/ct/board/viewBoard.do?menuId="+request.getParameter("menuId")+"&ctId="+ctId+"&bullId="+bullId+"');");
			} else {
				model.put("todo", "parent.location.replace('/ct/board/listBoard.do?menuId="+request.getParameter("menuId")+"&ctId="+ctId+"');");
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
	
	/** 커뮤니티 게시글 등록 페이지 */
	@RequestMapping(value = "/ct/board/setBoard")
	public String setBoard(HttpServletRequest request, 
			@RequestParam(value = "bullId", required = false) String bullId,			
			ModelMap model) throws Exception {
		
		CtBullMastBVo ctBullMastBVo = null;
		//게시글 보내기로 받아온 경우
		String selectBullId = request.getParameter("selectBullId");
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 수정인 경우
		if (bullId != null && !bullId.isEmpty()) {
			
			ctBullMastBVo = ctBullMastSvc.getCtBullMastBVo(Integer.parseInt(bullId), langTypCd);
			if(ctBullMastBVo == null){
				// ct.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("ct.msg.bullNotExists", request);
			}
			model.put("ctBullMastBVo", ctBullMastBVo);
			
			// 날짜 초기화
			ctBullMastSvc.initBullRezvDt(ctBullMastBVo);
			ctBullMastSvc.initBullExprDt(ctBullMastBVo);
			
		}else{
			
			if(selectBullId != null){
				CtBullMastBVo bullMastBVo = new CtBullMastBVo();
				bullMastBVo.setBullId(Integer.parseInt(selectBullId));
				bullMastBVo = (CtBullMastBVo) commonDao.queryVo(bullMastBVo);
//				CtRecMastBVo ctRecMastBVo = new CtRecMastBVo();
//				ctRecMastBVo.setBullId(Integer.parseInt(selectBullId));
//				ctRecMastBVo = (CtRecMastBVo) commonDao.queryVo(ctRecMastBVo);
				
				//ctBullMastBVo = new CtBullMastBVo();
				//ctBullMastBVo.setSubj(bullMastBVo.getSubj());
				
				//ctBullMastBVo.setCont(bullMastBVo.getCont());
				bullId = String.valueOf(bullMastBVo.getBullId());
				
				// 날짜 초기화
				ctBullMastSvc.initBullRezvDt(bullMastBVo);
				ctBullMastSvc.initBullExprDt(bullMastBVo);
				
				model.put("ctBullMastBVo", bullMastBVo);
				model.put("ctSendYn", "Y");
			}else{
				ctBullMastBVo = new CtBullMastBVo();
				// 날짜 초기화
				ctBullMastSvc.initBullRezvDt(ctBullMastBVo);
				ctBullMastSvc.initBullExprDt(ctBullMastBVo);
				
				model.put("ctBullMastBVo", ctBullMastBVo);
			}
		}
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 게시물첨부파일 리스트 model에 추가
		ctFileSvc.putFileListToModel(bullId, model, userVo.getCompId());
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		// 최대 본문 사이즈 model에 추가
		ctBullMastSvc.putBodySizeToModel(request, model);
		
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId"));
		
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		model.put("ctId", request.getParameter("ctId"));
		if(selectBullId != null){
			CtFncDVo ctFncDVo = new CtFncDVo();
			ctFncDVo.setCtFncUid(request.getParameter("menuId"));
			ctFncDVo = (CtFncDVo) commonDao.queryVo(ctFncDVo);
			
			model.put("bullCtFncId", ctFncDVo.getCtFncId());
			model.put("bullCtFncUid", ctFncDVo.getCtFncUid());
			model.put("bullCtFncPid", ctFncDVo.getCtFncPid());
			model.put("bullCtFncLocStep", ctFncDVo.getCtFncLocStep());
			model.put("bullCtFncOrdr", ctFncDVo.getCtFncOrdr());
		}else{
			model.put("bullCtFncId", request.getParameter("bullCtFncId"));
			model.put("bullCtFncUid", request.getParameter("bullCtFncUid"));
			model.put("bullCtFncPid", request.getParameter("bullCtFncPid"));
			model.put("bullCtFncLocStep", request.getParameter("bullCtFncLocStep"));
			model.put("bullCtFncOrdr", request.getParameter("bullCtFncOrdr"));
			model.put("bullPid", request.getParameter("bullPid"));
		}
		
		String fncUid = request.getParameter("menuId");

		/**
		 * 커뮤니티 기능정보에서 메뉴명 가져오기
		 */
		CtFncDVo ctFncDVo =  new CtFncDVo();
		//ctFncDVo.setCompId(userVo.getCompId());
		ctFncDVo.setCtFncUid(fncUid);
		ctFncDVo.setLangTyp(langTypCd);
		CtFncDVo CtFncDVo =  (CtFncDVo) commonDao.queryVo(ctFncDVo);
		model.put("menuTitle", CtFncDVo.getCtFncNm());
		
		return LayoutUtil.getJspPath("/ct/board/setBoard");
	}
	
	/** 커뮤니티 해당 게시판 게시글 목록 조회 */
	@RequestMapping(value = "/ct/board/listBoard")
	public String listBoard(HttpServletRequest request, ModelMap model)throws Exception{
		String ctFncUid = request.getParameter("menuId");
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		
		//fncUid&CtId로 CT_FNC_D의 해당 Vo를 가지고 온다.
		CtFncDVo ctFncDVo = new CtFncDVo();
		ctFncDVo.setCtFncUid(ctFncUid);
		ctFncDVo.setCtId(ctId);
		ctFncDVo = (CtFncDVo) commonDao.queryVo(ctFncDVo);
		
		//해당 게시판 게시글 목록
		CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
		VoUtil.bind(request, ctBullMastBVo);
		ctBullMastBVo.setCtId(ctFncDVo.getCtId());
		ctBullMastBVo.setCtFncId(ctFncDVo.getCtFncId());
		ctBullMastBVo.setCtFncUid(ctFncDVo.getCtFncUid());
		ctBullMastBVo.setCtFncPid(ctFncDVo.getCtFncPid());
		ctBullMastBVo.setCtFncLocStep(ctFncDVo.getCtFncLocStep());
		ctBullMastBVo.setCtFncOrdr(ctFncDVo.getCtFncOrdr());
		ctBullMastBVo.setBullExprDt("");
		
		// 기간 시작일 & 마감일
		String startDt = request.getParameter("strtDt");
		String endDt = request.getParameter("finDt");
		if(startDt != null && endDt != null){
			ctBullMastBVo.setStrtDt(startDt);
			ctBullMastBVo.setEndDt(endDt);
			model.put("startDt", startDt);
			model.put("endDt", endDt);
		}
		
		Integer recodeCount = commonDao.count(ctBullMastBVo);
		
		PersonalUtil.setPaging(request, ctBullMastBVo, recodeCount);
		// 게시물(BB_X000X_L) 테이블 - SELECT
//		ctBullMastBVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtBullMastBDao.selectCtBullMastB");
//		ctBullMastBVo.setWhereSqllet("AND T.REG_DT > CONVERT(DATETIME, '" + ddlnYmd + "', 120)");
		
		// 답변형인 경우
//		String orderBy = "CBMB_T.BULL_ID DESC";
//		if ("Y".equals(baBrdBVo.getReplyYn())) {
//			orderBy = "CBMB_T.REPLY_GRP_ID DESC, CBMB_T.REPLY_ORDR ASC";
//		}
//		ctBullMastBVo.setOrderBy(orderBy);
		@SuppressWarnings("unchecked")
		List<CtBullMastBVo> ctBullList = (List<CtBullMastBVo>) commonDao.queryList(ctBullMastBVo);
		
		//저장 "W"/ 삭제"D"
		ctCmntSvc.putAuthChk(request, model, "W", ctId, ctFncUid);
		
		model.put("recodeCount", recodeCount);
		model.put("ctBullList", ctBullList);
		model.put("ctFncDVo", ctFncDVo);
		model.put("ctId", ctId);
		
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		String fncUid = request.getParameter("menuId");
		
		/**
		 * 커뮤니티 기능정보에서 메뉴명 가져오기
		 */
		CtFncDVo ctFncDVoMenu =  new CtFncDVo();
		//ctFncDVoMenu.setCompId(userVo.getCompId());
		ctFncDVoMenu.setCtFncUid(fncUid);
		ctFncDVoMenu.setLangTyp(langTypCd);
		CtFncDVo CtFncDVo =  (CtFncDVo) commonDao.queryVo(ctFncDVoMenu);
		model.put("menuTitle", CtFncDVo.getCtFncNm());
		
		return LayoutUtil.getJspPath("/ct/board/listBoard");
	}
	
	
	/** 커뮤니티 게시물 목록 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ct/listMyBull")
	public String listMyBull(HttpServletRequest request,
			ModelMap model) throws Exception{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
		VoUtil.bind(request, ctBullMastBVo);
		//ctBullMastBVo.setCompId(userVo.getCompId());
		ctBullMastBVo.setLogUserUid(userVo.getUserUid());
		ctBullMastBVo.setLangTyp(langTypCd);
		ctBullMastBVo.setSignal("my");
		
		if(request.getParameter("selectCt") != null && !request.getParameter("selectCt").equals("")){
			ctBullMastBVo.setCtId(request.getParameter("selectCtId"));
			model.put("selectCtId", request.getParameter("selectCtId"));
			model.put("selectCtNm", request.getParameter("selectCt"));
		}
		
		/*if(request.getParameter("schCat") == null){
			VoUtil.bind(request, ctBullMastBVo);
		}else{
			ctBullMastBVo.setSchCat(request.getParameter("schCat"));
			ctBullMastBVo.setSchWord(request.getParameter("schWord"));
		}*/
		
		Integer recodeCount = commonSvc.count(ctBullMastBVo);
		PersonalUtil.setPaging(request, ctBullMastBVo, recodeCount);
		
		List<CtBullMastBVo> ctBullMastList = (List<CtBullMastBVo>) commonDao.queryList(ctBullMastBVo);
		
		model.put("recodeCount", recodeCount);
		model.put("ctBullMastList", ctBullMastList);
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		
		return LayoutUtil.getJspPath("/ct/listMyBull");
		
	}
	
	/** 커뮤니티 찾기 Frame*/
	@RequestMapping(value="/ct/findCmFrm")
	public String findCmFrm(HttpServletRequest request, ModelMap model)throws Exception{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		
		String parentSchCat = request.getParameter("parentSchCat");
		String parentSchWord = request.getParameter("parentSchWord");
		
		//ctEstbBVo.setCompId(userVo.getCompId());
		ctEstbBVo.setLogUserUid(userVo.getUserUid());
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setSignal("my");
		
		ctEstbBVo.setSchCat(parentSchCat);
		ctEstbBVo.setSchWord(parentSchWord);
		model.put("parentSchCat", parentSchCat);
		model.put("parentSchWord", parentSchWord);
		//System.out.println("ctEstbBVo.getCtActStat() : "+ctEstbBVo.getCtActStat());
		request.setAttribute("pageRowCnt", 5);
		
		Map<String, Object> rsltMap = ctBullMastSvc.getMyCtMapList(request, ctEstbBVo);
		
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("myCtMapList", rsltMap.get("myCtMapList"));
		
		
		return LayoutUtil.getJspPath("/ct/findCmFrm");
		
	}
	
	
	/** 커뮤니티 찾기 Pop*/
	@RequestMapping(value="/ct/findCmPop")
	public String findCmPop(HttpServletRequest request, ModelMap model)throws Exception{
		
		// 하단 커뮤니티 검색 조건
		String parentSchCat = request.getParameter("schCat");
		String parentSchWord = request.getParameter("schWord");
		
		model.put("parentSchCat", parentSchCat);
		model.put("parentSchWord", parentSchWord);
		
		return LayoutUtil.getJspPath("/ct/findCmPop");
	}
	
	/** 커뮤니티 찾기 dialog 내 검색 */
	@RequestMapping(value="/ct/setSearchCt")
	public String setSearchCt(HttpServletRequest request, 
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		try {
			// 파라미터 검사
			//JSONObject object = (JSONObject) JSONValue.parse(data);
//			String survId  = (String) object.get("survId");
//			String quesId  = (String) object.get("quesId");
//			if (survId == null || survId.equalsIgnoreCase("")
//					|| quesId == null || quesId.equalsIgnoreCase("")) {
//				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
//				throw new CmException("pt.msg.nodata.passed", request);
//			}
			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			//ctEstbBVo.setCompId(userVo.getCompId());
			ctEstbBVo.setLogUserUid(userVo.getUserUid());
			ctEstbBVo.setLangTyp(langTypCd);
			ctEstbBVo.setSignal("my");
			VoUtil.bind(request, ctEstbBVo);
			Map<String, Object> rsltMap = ctBullMastSvc.getMyCtMapList(request, ctEstbBVo);
			
			model.put("recodeCount", rsltMap.get("recodeCount"));
			model.put("myCtMapList", rsltMap.get("myCtMapList"));
			
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
