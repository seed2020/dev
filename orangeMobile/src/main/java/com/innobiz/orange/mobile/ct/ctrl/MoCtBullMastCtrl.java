package com.innobiz.orange.mobile.ct.ctrl;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtBullMastSvc;
import com.innobiz.orange.web.ct.svc.CtCmntSvc;
import com.innobiz.orange.web.ct.svc.CtFileSvc;
import com.innobiz.orange.web.ct.svc.CtRecmdHstSvc;
import com.innobiz.orange.web.ct.svc.CtScreHstSvc;
import com.innobiz.orange.web.ct.vo.CtBullMastBVo;
import com.innobiz.orange.web.ct.vo.CtBullMastCmdDVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtScreHstLVo;
import com.innobiz.orange.web.ct.vo.CtVistrHstDVo;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/** 커뮤니티 게시물 */
@Controller
public class MoCtBullMastCtrl {

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 커뮤니티 게시판 서비스 */
	@Autowired
	private CtBullMastSvc ctBullMastSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
	/** 추천이력 서비스 */
	@Autowired
	private CtRecmdHstSvc ctRecmdHstSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 점수이력 서비스 */
	@Autowired
	private CtScreHstSvc ctScreHstSvc;
	
	/** 게시물 첨부파일  */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	/** 커뮤니티 해당 게시판 게시글 목록 조회 */
	@RequestMapping(value = "/ct/board/listBoard")
	public String listBoard(HttpServletRequest request, ModelMap model)throws Exception{
		String ctFncUid = request.getParameter("menuId");
		String ctId = request.getParameter("ctId");

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

		@SuppressWarnings("unchecked")
		List<CtBullMastBVo> ctBullList = (List<CtBullMastBVo>) commonDao.queryList(ctBullMastBVo);
		
		//저장 "W"/ 삭제"D"
		ctCmntSvc.putAuthChk(request, model, "W", ctId, ctFncUid);
				
		model.put("recodeCount", recodeCount);
		model.put("ctBullList", ctBullList);
		model.put("ctFncDVo", ctFncDVo);
		model.put("ctId", ctId);

		return MoLayoutUtil.getJspPath("/ct/board/listBoard", "ct");
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
		String ctId = request.getParameter("ctId");
		
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
		if(ctBullMastSvc.saveReadHst(bullId, userVo.getUserUid(), userVo.getCompId())){
			ctBullMastSvc.addReadCnt(Integer.parseInt(bullId));
		}
		
		// 관련글 목록
		CtBullMastBVo bullMastBVo = new CtBullMastBVo();
		bullMastBVo.setQueryLang(langTypCd);
		bullMastBVo.setReplyGrpId(ctBullMastBVo.getReplyGrpId());
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
		ctBullMastSvc.putFileListToModel(bullId, model, userVo.getCompId());
		
		// 기타(점수, 추천, 찬반투표) 참가여부 model에 추가
		ctBullMastSvc.putEtcToModel(request, bullId, model);
		
		
		ctCmntSvc.putAuthChk(request, model, "R", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "W", ctId, fncUid);
		ctCmntSvc.putAuthChk(request, model, "D", ctId, fncUid);
		model.put("myAuth", ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId));

		model.put("ctId", ctId);
		
		// 한줄답변(CT_BULL_MAST_CMD_D) 테이블 - BIND
		CtBullMastCmdDVo ctBullMastCmdDVo = new CtBullMastCmdDVo();
		VoUtil.bind(request, ctBullMastCmdDVo);
		
		// 한줄답변(CT_BULL_MAST_CMD_D) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(ctBullMastCmdDVo);
		model.addAttribute("recodeCount", recodeCount);
		PersonalUtil.setPaging(request, ctBullMastCmdDVo, recodeCount);
		
		// 한줄답변(CT_BULL_MAST_CMD_D) 테이블 - SELECT
		@SuppressWarnings("unchecked")
		List<CtBullMastCmdDVo> ctCmtDVoList = (List<CtBullMastCmdDVo>) commonSvc.queryList(ctBullMastCmdDVo);
		model.addAttribute("ctCmtDVoList", ctCmtDVoList);
		
		
		return MoLayoutUtil.getJspPath("/ct/board/viewBoard", "ct");
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
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
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
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
		
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

		return MoLayoutUtil.getJspPath("/ct/board/viewScrePop");
		
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
			model.put("message", e.getMessage());
		}
		
		
		return JsonUtil.returnJson(model);
	}
	
	/** [팝업] 조회자 정보 (사용자) */
	@RequestMapping(value = "/ct/board/listReadHstSub")
	public String listReadHstPop(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			Locale locale,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 조회이력(BA_READ_HST_L) 테이블 - SELECT
		CtVistrHstDVo ctVistrHstDVo = new CtVistrHstDVo();
		ctVistrHstDVo.setQueryLang(langTypCd);
		ctVistrHstDVo.setCtId(bullId);

		Integer recodeCount = commonSvc.count(ctVistrHstDVo);
		PersonalUtil.setPaging(request, ctVistrHstDVo, recodeCount);
		
		@SuppressWarnings("unchecked")
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
		model.put("UI_TITLE", messageProperties.getMessage("bb.jsp.listReadHstPop.title", locale));
		return MoLayoutUtil.getJspPath("/ct/board/listReadHstSub");
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
				
				ctBullMastBVo = new CtBullMastBVo();
				ctBullMastBVo.setSubj(bullMastBVo.getSubj());
				ctBullMastBVo.setCont(bullMastBVo.getCont());
				bullId = String.valueOf(bullMastBVo.getBullId());
				
				// 날짜 초기화
				ctBullMastSvc.initBullRezvDt(ctBullMastBVo);
				ctBullMastSvc.initBullExprDt(ctBullMastBVo);
				
				model.put("ctBullMastBVo", ctBullMastBVo);
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

		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId"));
		
		String ctId = request.getParameter("ctId");
		ctCmntSvc.putLeftMenu(request, ctId, model);
		model.put("ctId", request.getParameter("ctId"));
		CtFncDVo ctFncDVo = new CtFncDVo();
		ctFncDVo.setCtFncUid(request.getParameter("menuId"));
		if(selectBullId == null || selectBullId.isEmpty()){
			ctFncDVo.setCtId(ctId);
			model.put("bullPid", request.getParameter("bullPid"));
		}
		ctFncDVo = (CtFncDVo) commonDao.queryVo(ctFncDVo);
		model.put("bullCtFncId", ctFncDVo.getCtFncId());
		model.put("bullCtFncUid", ctFncDVo.getCtFncUid());
		model.put("bullCtFncPid", ctFncDVo.getCtFncPid());
		model.put("bullCtFncLocStep", ctFncDVo.getCtFncLocStep());
		model.put("bullCtFncOrdr", ctFncDVo.getCtFncOrdr());
		
		return MoLayoutUtil.getJspPath("/ct/board/setBoard", "ct");
	}
	
	
	/** 커뮤니티 게시글 저장 */
	@RequestMapping(value = "/ct/board/transSetBullSavePost")
	public String transSetBullSave(HttpServletRequest request, ModelMap model){
		UploadHandler uploadHandler = null;
		try{
			// Multipart 파일 업로드
			uploadHandler = ctFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			String bullId       = ParamUtil.getRequestParam(request, "bullId", false);
			String bullStatCd   = ParamUtil.getRequestParam(request, "bullStatCd", true);
			String bullRezvDt   = ParamUtil.getRequestParam(request, "bullRezvDt", false);
			
			String ctFncUid = request.getParameter("menuId");
			String ctId = request.getParameter("ctId");
			
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
			ctFileSvc.saveSurvFile(request, String.valueOf(storedBullId), queryQueue, ctFncUid, ctId);
			commonSvc.execute(queryQueue);
			
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			boolean goView = false;
			// 수정이고, 게시예약일과 게시완료일 사이이고, 보안글이 아니면 조회화면으로 이동
			if (bullId != null && !bullId.isEmpty()) {
				goView = true;
			}	
			
			if (goView) {
				model.put("todo", "$m.nav.prev(event, '/ct/board/viewBoard.do?menuId="+request.getParameter("menuId")+"&ctId="+ctId+"&bullId="+bullId+"');");
			} else {
				model.put("todo", "$m.nav.prev(event, '/ct/board/listBoard.do?menuId="+request.getParameter("menuId")+"&ctId="+ctId+"');");
			}
		}catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("exception", e);
		} finally {
			if (uploadHandler != null) try { uploadHandler.removeTempDir(); } catch (CmException ignored) {}
		}

		return MoLayoutUtil.getResultJsp();
		
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
		
		CtBullMastBVo parentBullMastBVo = ctBullMastSvc.getCtBullMastBVo(Integer.parseInt(bullPid), langTypCd);
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
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시물첨부파일 리스트 model에 추가
		ctBullMastSvc.putFileListToModel(null, model, userVo.getCompId());

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

		return MoLayoutUtil.getJspPath("/ct/board/setBoard", "ct");
	}
	
	/** 커뮤니티 게시물 답변 저장 */
	@RequestMapping(value = "/ct/board/transSetReplySavePost")
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
			model.put("todo", "$m.nav.prev(event, '/ct/board/viewBoard.do?menuId="+request.getParameter("menuId")+"&ctId="+ctId+"&bullId="+newBullId+"');");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("exception", e);
		}

		return MoLayoutUtil.getResultJsp();
		
	}
}
