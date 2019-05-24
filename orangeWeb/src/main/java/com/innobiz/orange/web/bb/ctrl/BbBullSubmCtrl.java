package com.innobiz.orange.web.bb.ctrl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullFileSvc;
import com.innobiz.orange.web.bb.svc.BbBullPhotoSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.svc.BbBullTgtSvc;
import com.innobiz.orange.web.bb.svc.BbCmSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaBullSubmLVo;
import com.innobiz.orange.web.bb.vo.BaRezvSaveLVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/* 게시상신함 */
@Controller
public class BbBullSubmCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BbBullSubmCtrl.class);

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 게시판 공통 서비스 */
	@Autowired
	private BbCmSvc bbCmSvc;

	/** 게시판관리 서비스 */
	@Resource(name = "bbBrdSvc")
	private BbBrdSvc bbBrdSvc;

	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;

	/** 게시대상 서비스 */
	@Resource(name = "bbBullTgtSvc")
	public BbBullTgtSvc bbBullTgtSvc;

	/** 게시파일 서비스 */
	@Resource(name = "bbBullFileSvc")
	private BbBullFileSvc bbBullFileSvc;

	/** 게시물사진 서비스 */
	@Resource(name = "bbBullPhotoSvc")
	private BbBullPhotoSvc bbBullPhotoSvc;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	/** 게시상신함 목록 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/listSubmBull")
	public String listSubmBull(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시상신함(BA_BULL_SUBM_L) 테이블 - BIND
		BaBullSubmLVo baBullSubmLVo = new BaBullSubmLVo();
		baBullSubmLVo.setQueryLang(langTypCd);
		VoUtil.bind(request, baBullSubmLVo);
		
		String orderBy = "T.BULL_ID DESC";
		baBullSubmLVo.setOrderBy(orderBy);
		
		// 게시상신함(BA_BULL_SUBM_L) 테이블 - COUNT
		baBullSubmLVo.setRegrUid(userVo.getUserUid());
		Integer recodeCount = commonSvc.count(baBullSubmLVo);
		PersonalUtil.setPaging(request, baBullSubmLVo, recodeCount);
		
		// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
		List<BaBullSubmLVo> baBullSubmLVoList = (List<BaBullSubmLVo>) commonSvc.queryList(baBullSubmLVo);
		
		model.put("recodeCount", recodeCount);
		model.put("baBullSubmLVoList", baBullSubmLVoList);

		model.put("viewPage", "viewSubmBull");
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
				
		return LayoutUtil.getJspPath("/bb/listSubmBull");
	}

	/** 게시상신함 조회 (사용자) */
	@RequestMapping(value = "/bb/viewSubmBull")
	public String viewSubmBull(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			@RequestParam(value = "secu", required = false) String secu,
			ModelMap model) throws Exception {
		
		if (bullId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
		BaBullSubmLVo baBullSubmLVo = bbBullSvc.getBaBullSubmLVo(Integer.parseInt(bullId), langTypCd);
		if (baBullSubmLVo == null) {
			// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("bb.msg.bullNotExists", request);
		}
		
		// 사용자 권한 체크
		bbCmSvc.checkUserAuth(request, "A", baBullSubmLVo.getRegrUid());
		
		model.put("baBullSubmLVo", baBullSubmLVo);
		
		// 게시판ID목록
		String brdIdList = baBullSubmLVo.getBrdIdList();
		if (brdIdList != null && !"".equals(brdIdList)) {
			model.put("brdNms", bbBrdSvc.getBrdNms(langTypCd, brdIdList));
		}
		
		// 게시판ID
		String brdId = baBullSubmLVo.getBrdId();
		
		// 게시판관리(BA_BRD_B) - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		model.put("baBrdBVo", baBrdBVo);
		
		// 게시물(BB_X000X_L) 테이블 - SELECT
		BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd, false);
		if (bbBullLVo == null) {
			// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("bb.msg.bullNotExists", request);
		}
		//lobHandler
		model.put("lobHandler", lobHandler.create(
				"SELECT CONT FROM "+bbBullLVo.getTableName()+" WHERE BULL_ID = ? AND BRD_ID = ?", 
				new String[]{bullId, brdId}
		));
		model.put("bbBullLVo", bbBullLVo);

		// 보안글이면
		if ("Y".equals(bbBullLVo.getSecuYn()) && secu != null) {
			// 비밀번호 확인
			if (!bbCmSvc.isValidUserPw(request, secu)) {
				// pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				model.addAttribute("messageCode", "pt.login.noUserNoPw");
				model.addAttribute("todo", "history.go(-1);");
				return LayoutUtil.getResultJsp();
			}
		}
		
		// 게시대상 model에 추가
		bbBullTgtSvc.putTgtListToModel(bbBullLVo, model);
		
		// 이전글/다음글
		BaBullSubmLVo paramBullSubmVo = new BaBullSubmLVo();
		paramBullSubmVo.setRegrUid(userVo.getUserUid());
		VoUtil.bind(request, paramBullSubmVo);
		
		// PREV
		paramBullSubmVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaBullSubmLDao.selectPrevId");
		Integer prevId = commonSvc.queryInt(paramBullSubmVo);
		if (prevId != null) {
			// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
			BbBullLVo prevBullVo = bbBullSvc.getBaBullSubmLVo(prevId, langTypCd);
			model.put("prevBullVo", prevBullVo);
		}
		// NEXT
		paramBullSubmVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaBullSubmLDao.selectNextId");
		Integer nextId = commonSvc.queryInt(paramBullSubmVo);
		if (nextId != null) {
			// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
			BbBullLVo nextBullVo = bbBullSvc.getBaBullSubmLVo(nextId, langTypCd);
			model.put("nextBullVo", nextBullVo);
		}
		
		// 게시물첨부파일 리스트 model에 추가
		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());

		// 포토게시판여부
		if ("Y".equals(baBrdBVo.getPhotoYn())) {
			// 게시물사진 세팅
			bbBullLVo.setPhotoVo(bbBullPhotoSvc.getPhotoVo(Integer.valueOf(bullId)));
		}
		
		// listPage
		model.addAttribute("listPage", "listSubmBull");
		model.addAttribute("viewPage", "viewSubmBull");
		model.addAttribute("delAction", "transSubmBullDelAjx");
		
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId", "pw"));
		
		return LayoutUtil.getJspPath("/bb/viewSubmBull");
	}

	/** [AJAX] 게시상신함 삭제 (사용자) */
	@RequestMapping(value = "/bb/transSubmBullDelAjx")
	public String transSubmBullDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String brdId = (String) object.get("brdId");
			String bullId = (String) object.get("bullId");
			if (brdId == null || bullId == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
			BaBullSubmLVo baBullSubmLVo = bbBullSvc.getBaBullSubmLVo(Integer.parseInt(bullId), langTypCd);
			if (baBullSubmLVo == null) {
				// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("bb.msg.bullNotExists", request);
			}
			
			// 사용자 권한 체크
			bbCmSvc.checkUserAuth(request, "A", baBullSubmLVo.getRegrUid());
			
			// 게시상신함 삭제
			QueryQueue queryQueue = new QueryQueue();
			bbBullSvc.deleteBaBullSubmLVo(request, null, Integer.parseInt(bullId), queryQueue);
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
			// 게시물 삭제
			bbBullSvc.deleteBull(baBrdBVo, Integer.parseInt(bullId), queryQueue);
			
			// 게시물첨부파일 삭제
			List<CommonFileVo> deletedFileList = bbBullFileSvc.deleteBullFile(bullId, queryQueue);

			// 포토게시판이면
			if ("Y".equals(baBrdBVo.getPhotoYn())) {
				// 게시물사진 삭제
				deletedFileList.add(bbBullPhotoSvc.deletePhoto(Integer.valueOf(bullId), queryQueue));
			}
			
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			bbBullFileSvc.deleteDiskFiles(deletedFileList);
			
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

	/** 심의함 목록 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/listDiscBull")
	public String listDiscBull(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시상신함(BA_BULL_SUBM_L) 테이블 - BIND
		BaBullSubmLVo baBullSubmLVo = new BaBullSubmLVo();
		baBullSubmLVo.setQueryLang(langTypCd);
		VoUtil.bind(request, baBullSubmLVo);
		
		String orderBy = "T.BULL_ID DESC";
		baBullSubmLVo.setOrderBy(orderBy);
		
		// 게시상신함(BA_BULL_SUBM_L) 테이블 - COUNT
		baBullSubmLVo.setDiscrUid(userVo.getUserUid());
		baBullSubmLVo.setBullStatCd("S");
		Integer recodeCount = commonSvc.count(baBullSubmLVo);
		PersonalUtil.setPaging(request, baBullSubmLVo, recodeCount);
		
		// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
		List<BaBullSubmLVo> baBullSubmLVoList = (List<BaBullSubmLVo>) commonSvc.queryList(baBullSubmLVo);
		// 첨부파일 수 조회
		bbBullFileSvc.setFileCount(baBullSubmLVoList);
		
		model.put("recodeCount", recodeCount);
		model.put("baBullSubmLVoList", baBullSubmLVoList);
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
				
		return LayoutUtil.getJspPath("/bb/listDiscBull");
	}

	/** 심의함 수정용 조회 화면 (사용자) */
	@RequestMapping(value = "/bb/setDiscBull")
	public String setDiscBull(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			@RequestParam(value = "secu", required = false) String secu,
			ModelMap model) throws Exception {

		if (bullId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
		BaBullSubmLVo baBullSubmLVo = bbBullSvc.getBaBullSubmLVo(Integer.parseInt(bullId), langTypCd);
		if (baBullSubmLVo == null) {
			// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("bb.msg.bullNotExists", request);
		}
		
		// 사용자 권한 체크
		bbCmSvc.checkUserAuth(request, "A", baBullSubmLVo.getDiscrUid());
		
		// 게시판ID목록
		String brdIdList = baBullSubmLVo.getBrdIdList();
		if (brdIdList != null && !"".equals(brdIdList)) {
			model.put("brdIdList", brdIdList);
			model.put("brdNms", bbBrdSvc.getBrdNms(langTypCd, brdIdList));
		}
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, baBullSubmLVo.getBrdId());
		model.put("baBrdBVo", baBrdBVo);
		
		// 게시물(BB_X000X_L) 테이블 - SELECT
		BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd, false);
		if (bbBullLVo == null) {
			// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("bb.msg.bullNotExists", request);
		}
		//lobHandler
		model.put("lobHandler", lobHandler.create(
				"SELECT CONT FROM "+bbBullLVo.getTableName()+" WHERE BULL_ID = ? AND BRD_ID = ?", 
				new String[]{bullId, baBrdBVo.getBrdId()}
		));
		model.put("bbBullLVo", bbBullLVo);
		
		// 게시대상 model에 추가
		bbBullTgtSvc.putTgtListToModel(bbBullLVo, model);

		// 보안글이면
		if ("Y".equals(bbBullLVo.getSecuYn()) && secu != null) {
			// 비밀번호 확인
			if (!bbCmSvc.isValidUserPw(request, secu)) {
				// pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				model.addAttribute("messageCode", "pt.login.noUserNoPw");
				model.addAttribute("todo", "history.go(-1);");
				return LayoutUtil.getResultJsp();
			}
		}
		
		// 이전글/다음글
		BaBullSubmLVo paramBullSubmVo = new BaBullSubmLVo();
		paramBullSubmVo.setDiscrUid(userVo.getUserUid());
		VoUtil.bind(request, paramBullSubmVo);
		paramBullSubmVo.setBullStatCd("S");
		// PREV
		paramBullSubmVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaBullSubmLDao.selectPrevId");
		Integer prevId = commonSvc.queryInt(paramBullSubmVo);
		if (prevId != null) {
			// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
			BbBullLVo prevBullVo = bbBullSvc.getBaBullSubmLVo(prevId, langTypCd);
			model.put("prevBullVo", prevBullVo);
		}
		// NEXT
		paramBullSubmVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaBullSubmLDao.selectNextId");
		Integer nextId = commonSvc.queryInt(paramBullSubmVo);
		if (nextId != null) {
			// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
			BbBullLVo nextBullVo = bbBullSvc.getBaBullSubmLVo(nextId, langTypCd);
			model.put("nextBullVo", nextBullVo);
		}
		
		// 게시물첨부파일 리스트 model에 추가
		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());
		
		// 포토게시판여부
		if ("Y".equals(baBrdBVo.getPhotoYn())) {
			// 게시물사진 세팅
			bbBullLVo.setPhotoVo(bbBullPhotoSvc.getPhotoVo(Integer.valueOf(bullId)));
		}
				
		// action
		model.addAttribute("listPage", "listDiscBull");
		model.addAttribute("setPage", "setDiscBull");
		
		return LayoutUtil.getJspPath("/bb/setDiscBull");
	}

	/** 심의함 저장 (사용자) */
	@RequestMapping(value = "/bb/transDiscBull")
	public String transDiscBull(HttpServletRequest request,
			@RequestParam(value = "params", required = true) String params,
			@RequestParam(value = "listPage", required = true) String listPage,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "bullId", required = true) String bullId,
			@RequestParam(value = "bullStatCd", required = true) String bullStatCd,
			@RequestParam(value = "rjtOpin", required = true) String rjtOpin,
			@RequestParam(value = "bullRezvDt", required = false) String bullRezvDt,
			ModelMap model) {
		
		try {
			if (params.isEmpty() || listPage.isEmpty() || brdId.isEmpty() || bullStatCd.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
			// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
			if ("B".equals(bullStatCd) && bullRezvDt != null && !bullRezvDt.isEmpty() && StringUtil.isAfterNow(bullRezvDt)) {
				bullStatCd = "R";
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			if ("J".equals(bullStatCd)) {
				// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
				BaBullSubmLVo baBullSubmLVo = bbBullSvc.getBaBullSubmLVo(Integer.parseInt(bullId), langTypCd);
				
				if(baBullSubmLVo == null)
					throw new CmException("bb.msg.bullNotExists", request);
				
				
				// 게시물상태코드 변경
				bbBullSvc.updateBullStatCd(baBrdBVo, Integer.parseInt(bullId), bullStatCd, queryQueue);
				
				// 심의함 저장
				bbBullSvc.saveDiscBull(bullId, bullStatCd, rjtOpin, queryQueue);
				
			} else if ("R".equals(bullStatCd)) {
				// 게시물(BB_X000X_L) 테이블 - SELECT
				BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);
				if (bbBullLVo == null) {
					// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
					throw new CmException("bb.msg.bullNotExists", request);
				}
				
				// 게시물상태코드 변경
				bbBullSvc.updateBullStatCd(baBrdBVo, Integer.parseInt(bullId), bullStatCd, queryQueue);
				
				// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
				BaBullSubmLVo baBullSubmLVo = bbBullSvc.getBaBullSubmLVo(Integer.parseInt(bullId), langTypCd);
				
				// 게시상신함 삭제
				bbBullSvc.deleteBaBullSubmLVo(request, baBullSubmLVo.getRegrUid(), Integer.parseInt(bullId), queryQueue);
				
				// 게시물 예약저장(BA_REZV_SAVE_L) 테이블 - BIND
				BaRezvSaveLVo baRezvSaveLVo = new BaRezvSaveLVo();
				BeanUtils.copyProperties(baBullSubmLVo, baRezvSaveLVo);
				
				// 추가적으로 게시예약일 확인함.
				baRezvSaveLVo.setBullRezvDt(bbBullLVo.getBullRezvDt());
				
				// 게시물 예약저장 저장
				bbBullSvc.saveBaRezvSaveLVo(request, Integer.parseInt(bullId), baRezvSaveLVo, queryQueue);
				
			} else if ("B".equals(bullStatCd)) {
				// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
				BaBullSubmLVo baBullSubmLVo = bbBullSvc.getBaBullSubmLVo(Integer.parseInt(bullId), langTypCd);
				
				if(baBullSubmLVo == null)
					throw new CmException("bb.msg.bullNotExists", request);
				
				// 게시상신함 삭제
				bbBullSvc.deleteBaBullSubmLVo(request, baBullSubmLVo.getRegrUid(), Integer.parseInt(bullId), queryQueue);
				
				// 게시물(BB_X000X_L) 테이블 - SELECT
				BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);
				
				// 기존 게시물 삭제
				bbBullSvc.deleteBull(baBrdBVo, Integer.parseInt(bullId), queryQueue);
				
				// brdIds
				String brdIdList = baBullSubmLVo.getBrdIdList();
				String[] brdIds = null;
				if (brdIdList != null && !"".equals(brdIdList)) {
					brdIds = brdIdList.split(",");
				}
				
				// 게시물 저장
				bbBullSvc.saveBbBullLVo(request, baBrdBVo, null, bullStatCd, bbBullLVo, brdIds, queryQueue);
				
				// 포토게시판이면
				if ("Y".equals(baBrdBVo.getPhotoYn())) {
					// 게시물사진 저장
					bbBullPhotoSvc.copyPhoto(request, Integer.parseInt(bullId), bbBullLVo.getBullId(), queryQueue);
					
					List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
					// 게시물사진 삭제
					deletedFileList.add(bbBullPhotoSvc.deletePhoto(Integer.valueOf(bullId), queryQueue));
					
					// 게시물사진 파일 삭제
					bbBullFileSvc.deleteDiskFiles(deletedFileList);
				}
			}
			
			commonSvc.execute(queryQueue);
			
			if ("J".equals(bullStatCd)) {
				// bb.msg.rjt.success=반려 처리되었습니다.
				model.put("message", messageProperties.getMessage("bb.msg.rjt.success", request));
			} else {
				// bb.msg.apvd.success=승인 처리되었습니다.
				model.put("message", messageProperties.getMessage("bb.msg.apvd.success", request));
			}
			model.put("todo", "parent.location.replace('" + listPage + "');");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
}
