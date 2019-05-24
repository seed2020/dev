package com.innobiz.orange.web.bb.ctrl;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullFileSvc;
import com.innobiz.orange.web.bb.svc.BbBullPhotoSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.svc.BbBullTgtSvc;
import com.innobiz.orange.web.bb.svc.BbCmSvc;
import com.innobiz.orange.web.bb.svc.BbTblSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaBullOptDVo;
import com.innobiz.orange.web.bb.vo.BaBullPopupDVo;
import com.innobiz.orange.web.bb.vo.BaColmDispDVo;
import com.innobiz.orange.web.bb.vo.BaTblColmDVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
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
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

/* 게시물 (관리자) */
@Controller
public class BbBullAdmCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BbBullAdmCtrl.class);

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 게시판 공통 서비스 */
	@Autowired
	private BbCmSvc bbCmSvc;

	/** 테이블관리 서비스 */
	@Resource(name = "bbTblSvc")
	private BbTblSvc bbTblSvc;

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

	/** 게시물관리 목록조회 (관리자) */
	
//	/** 포털 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
//	/** 리소스 조회 저장용 서비스 */
//	@Autowired
//	private PtRescSvc ptRescSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/listBullMng")
	public String listBullMng(HttpServletRequest request,
			@RequestParam(value = "brdId", required = false) String brdId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
		baBrdBVo.setOrderBy("T.BRD_ID ASC");
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
		model.put("baBrdBVoList", baBrdBVoList);
		
		Integer recodeCount = 0;
		List<BbBullLVo> bbBullLVoList = null;
		if (baBrdBVoList != null && baBrdBVoList.size() > 0) {
			// bb.msg.noBb=생성된 게시판이 존재하지 않습니다.
			//String msg = messageProperties.getMessage("bb.msg.noBb", request);
			//throw new CmException(msg);
		
			// 검색할 게시판 결정
			baBrdBVo = null;
			for (BaBrdBVo vo : baBrdBVoList) {
				if (vo.getBrdId().equals(brdId)) {
					baBrdBVo = vo;
				}
			}
			if (baBrdBVo == null) baBrdBVo = baBrdBVoList.get(0);
		
			// 게시물(BB_X000X_L) 테이블 - BIND
			BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(baBrdBVo);
			VoUtil.bind(request, paramBullVo);

			// 답변형인 경우
			String orderBy = "T.BULL_ID DESC";
			if ("Y".equals(baBrdBVo.getReplyYn())) {
				orderBy = "T.REPLY_GRP_ID DESC, T.REPLY_ORDR ASC";
			}
			paramBullVo.setOrderBy(orderBy);
						
			// 게시물(BB_X000X_L) 테이블 - COUNT
			paramBullVo.setBrdId(baBrdBVo.getBrdId());
			recodeCount = commonSvc.count(paramBullVo);
			PersonalUtil.setPaging(request, paramBullVo, recodeCount);
		
			// 게시물(BB_X000X_L) 테이블 - SELECT
			bbBullLVoList = (List<BbBullLVo>) commonSvc.queryList(paramBullVo);
			
			// 첨부파일 수 조회
			bbBullFileSvc.setFileCount(bbBullLVoList);			
			// 한줄답변 수 조회
			bbCmSvc.setCmtCount(bbBullLVoList);
		
		}
		
		model.put("baBrdBVo", baBrdBVo);
		model.put("recodeCount", recodeCount);
		model.put("bbBullLVoList", bbBullLVoList);
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		// 전사 게시판 일 경우에는 시스템관리자 이외에는 답변 여부를 체크.
		boolean isSysAdmin = SecuUtil.hasAuth(request, "Y".equals(baBrdBVo.getAllCompYn()) ? "SYS" : "A", null);
		model.put("isSysAdmin", isSysAdmin);
		
		return LayoutUtil.getJspPath("/bb/adm/listBullMng");
	}

	/** [AJAX] 공지지정 (관리자) */
	@RequestMapping(value = "/bb/adm/transNotcAjx")
	public String transNotcAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String brdId = (String) object.get("brdId");
			JSONArray jsonArray = (JSONArray) object.get("bullIds");
			if (brdId == null || jsonArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시옵션(BA_BULL_OPT_D) 테이블 - UPDATE
			QueryQueue queryQueue = new QueryQueue();
			for (int i = 0; i < jsonArray.size(); i++) {
				String bullId = (String) jsonArray.get(i);
				BaBullOptDVo baBullOptDVo = new BaBullOptDVo();
				baBullOptDVo.setBullId(Integer.parseInt(bullId));
				baBullOptDVo.setNotcYn("Y");
				queryQueue.store(baBullOptDVo);
			}
			
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}

	/** [AJAX] 게시물이동 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/transBullMoveAjx")
	public String transBullMoveAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String srcBrdId = (String) object.get("brdId");
			JSONArray bullIdsArray = (JSONArray) object.get("bullIds");
			String targetBrdId = (String) object.get("targetBrdId");
			if (srcBrdId == null || bullIdsArray == null || targetBrdId == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			if (srcBrdId.equals(targetBrdId)) {
				// bb.msg.moveBull.sameBb=같은 게시판으로 이동할 수 없습니다.
				throw new CmException("bb.msg.moveBull.sameBb", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 원본 게시판 VO
			BaBrdBVo srcBrdVo = bbBrdSvc.getBaBrdBVo(langTypCd, srcBrdId);
			// 대상 게시판 VO
			BaBrdBVo targetBrdVo = bbBrdSvc.getBaBrdBVo(langTypCd, targetBrdId);
			
			// 원본 DB테이블명
			String srcTableName = bbTblSvc.getFullTblNm(srcBrdVo.getTblNm());
			// 대상 DB테이블명
			String targetTableName = bbTblSvc.getFullTblNm(targetBrdVo.getTblNm());
			
			BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(srcBrdVo);
			
			QueryQueue queryQueue = new QueryQueue();
			for (int i = 0; i < bullIdsArray.size(); i++) {
				// 원본 게시물ID
				String bullId = (String) bullIdsArray.get(i);
				// 게시물(BB_X000X_L) 테이블 - SELECT
				BbBullLVo srcBullVo = bbBullSvc.getBbBullLVo(srcBrdVo, Integer.parseInt(bullId), langTypCd);
				
				//원본글 - 검색 색인 테이블에서 삭제
				bbBullSvc.addSrchIndex(srcBullVo, userVo, queryQueue, "D");
				
				// 답변형이면
				if ("Y".equals(srcBrdVo.getReplyYn())) {
					// 답변글은 제외
					if (srcBullVo.getReplyDpth() != null && srcBullVo.getReplyDpth() > 0) continue;
					
					// 답변글이 있으면 모두 이동
					paramBullVo.setBullId(null);
					paramBullVo.setReplyGrpId(srcBullVo.getBullId());
					paramBullVo.setOrderBy("T.BULL_ID ASC");
					// 게시물(BB_X000X_L) 테이블 - SELECT
					List<BbBullLVo> replyVoList = (List<BbBullLVo>) commonSvc.queryList(paramBullVo);
					Integer newReplyGrpId = null;
					HashMap<Integer, Integer> bullPidMap = new HashMap<Integer, Integer>();
					for (BbBullLVo replyVo : replyVoList) {
						//답변글 - 검색 색인 테이블에서 삭제
						bbBullSvc.addSrchIndex(replyVo, userVo, queryQueue, "D");
						
						// 대상 게시물 VO
						BbBullLVo targetBullVo = bbBullSvc.newBbBullLVo(targetBrdVo);
						targetBullVo.setBrdId(targetBrdId);
						
						if (srcTableName.equals(targetTableName)) {
							// 게시물(BB_X000X_L) 테이블 - UPDATE
							targetBullVo.setBullId(replyVo.getBullId());
							queryQueue.update(targetBullVo);
						} else {
							// 속성 복사
							String[] ignores = new String[] { "bullId", "brdId", "bullPid", "tableName", "replyGrpId" };
							BeanUtils.copyProperties(replyVo, targetBullVo, ignores);
							// 게시물ID 생성
							Integer newBullId = bbCmSvc.createBullId();
							Integer bullPid = null;
							bullPidMap.put(replyVo.getBullId(), newBullId);
							if (replyVo.getBullPid() != null) {
								bullPid = bullPidMap.get(replyVo.getBullPid());
							}
							if (newReplyGrpId == null) newReplyGrpId = newBullId;
							// 게시물(BB_X000X_L) 테이블 - INSERT
							targetBullVo.setBullId(newBullId);
							targetBullVo.setBullPid(bullPid);
							targetBullVo.setReplyGrpId(newReplyGrpId);
							queryQueue.insert(targetBullVo);
							// 게시물(BB_X000X_L) 테이블 - DELETE
							replyVo.setTableName(srcTableName);
							queryQueue.delete(replyVo);
						}
						//대상글 - 검색 색인 테이블에 추가
						bbBullSvc.addSrchIndex(targetBullVo, userVo, queryQueue, "I");
					}
				} else {
					// 대상 게시물 VO
					BbBullLVo targetBullVo = bbBullSvc.newBbBullLVo(targetBrdVo);
					targetBullVo.setBrdId(targetBrdId);
					
					if (srcTableName.equals(targetTableName)) {
						// 게시물(BB_X000X_L) 테이블 - UPDATE
						targetBullVo.setBullId(srcBullVo.getBullId());
						queryQueue.update(targetBullVo);
					} else {
						// 속성 복사
						String[] ignores = new String[] { "bullId", "brdId", "tableName" };
						BeanUtils.copyProperties(srcBullVo, targetBullVo, ignores);
						// 게시물(BB_X000X_L) 테이블 - INSERT
						Integer newBullId = bbCmSvc.createBullId();
						targetBullVo.setBullId(newBullId);
						queryQueue.insert(targetBullVo);
						// 게시물(BB_X000X_L) 테이블 - DELETE
						srcBullVo.setTableName(srcTableName);
						queryQueue.delete(srcBullVo);
					}
					//대상글 - 검색 색인 테이블에 추가
					bbBullSvc.addSrchIndex(targetBullVo, userVo, queryQueue, "I");
				}
				
			}
			
			commonSvc.execute(queryQueue);
			
			// bb.msg.moveBull.success={0}건의 게시물을 성공적으로 이동하였습니다.
			String[] args = new String[] { String.valueOf(bullIdsArray.size()) };
			model.put("message", messageProperties.getMessage("bb.msg.moveBull.success", args, request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}

	/** [AJAX] 게시물복사 (관리자) */
	@RequestMapping(value = "/bb/adm/transBullCopyAjx")
	public String transBullCopyAjxAdm(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String srcBrdId = (String) object.get("brdId");
			JSONArray bullIdsArray = (JSONArray) object.get("bullIds");
			JSONArray brdIdsArray = (JSONArray) object.get("brdIds");
			if (srcBrdId == null || bullIdsArray == null || brdIdsArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 원본 게시판 VO
			BaBrdBVo srcBrdVo = bbBrdSvc.getBaBrdBVo(langTypCd, srcBrdId);
			
			// 게시물 복사
			QueryQueue queryQueue = new QueryQueue();
			int copyCnt = bbBullSvc.copyBull(request, srcBrdVo, bullIdsArray, brdIdsArray, queryQueue);
			
			commonSvc.execute(queryQueue);
			
			// bb.msg.copyBull.success={0}건의 게시물을 성공적으로 복사하였습니다.
			String[] args = new String[] { String.valueOf(copyCnt) };
			model.put("message", messageProperties.getMessage("bb.msg.copyBull.success", args, request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}

	/** [AJAX] 게시물 삭제 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/transBullDelAjx")
	public String transBullDelAjxAdm(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String brdId = (String) object.get("brdId");
			JSONArray jsonArray = (JSONArray) object.get("bullIds");
			String id = (String) object.get("bullId");
			if (brdId == null || (id == null && jsonArray == null)) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			String[] bullIds = null;
			if (jsonArray == null) {
				bullIds = new String[] {id};
			} else {
				bullIds = (String[]) jsonArray.toArray(new String[jsonArray.size()]);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
			// 게시물 삭제
			QueryQueue queryQueue = new QueryQueue();
			List<CommonFileVo> deletedFileList  = new ArrayList<CommonFileVo>();
			for (String bullId : bullIds) {
				// 게시물첨부파일 삭제
				deletedFileList.addAll(bbBullFileSvc.deleteBullFile(bullId, queryQueue));

				// 포토게시판이면
				if ("Y".equals(baBrdBVo.getPhotoYn())) {
					// 게시물사진 삭제
					deletedFileList.add(bbBullPhotoSvc.deletePhoto(Integer.valueOf(bullId), queryQueue));
				}
				
				// 답변형인 경우
				if ("Y".equals(baBrdBVo.getReplyYn())) {
					BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(baBrdBVo, true);
					paramBullVo.setBullPid(Integer.parseInt(bullId));
					if (commonSvc.count(paramBullVo) > 0) {//답변글이 1개 이상일경우
						//하위 게시판글[답변] 목록을 가져온다.
						List<BbBullLVo> bbBullLVoList = bbBullSvc.getChildBullList(paramBullVo, Integer.parseInt(bullId), new ArrayList<BbBullLVo>());
						if(bbBullLVoList.size() > 0 ){
							for(BbBullLVo storedBbBullLVo : bbBullLVoList){
								bbBullSvc.deleteBull(baBrdBVo, storedBbBullLVo.getBullId(), queryQueue);
							}
						}
					}
				}
				
				bbBullSvc.deleteBull(baBrdBVo, Integer.parseInt(bullId), queryQueue);
				
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
	
	
	/** [AJAX] 게시물 삭제 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/transBullDelAjxChk")
	public String transBullDelAjxChk(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String brdId = (String) object.get("brdId");
			String id = (String) object.get("bullId");
			JSONArray jsonArray = (JSONArray) object.get("bullIds");
			if (brdId == null || (id == null && jsonArray == null)) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			String[] bullIds = null;
			if (jsonArray == null) {
				bullIds = new String[] {id};
			} else {
				bullIds = (String[]) jsonArray.toArray(new String[jsonArray.size()]);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
			for (String bullId : bullIds) {
				// 게시물(BB_X000X_L) 테이블 - SELECT
				BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);
				
				if (bbBullLVo == null) {
					// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
					throw new CmException("bb.msg.bullNotExists", request);
				}
				
				// 사용자 권한 체크
				bbCmSvc.checkUserAuth(request, "A", bbBullLVo.getRegrUid());
				
				// 부서게시판이면
				if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
					bbCmSvc.checkUserDeptAuth(request, bbBullLVo.getDeptId());
				}
				
				// 답변형인 경우
				if ("Y".equals(baBrdBVo.getReplyYn())) {
					BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(baBrdBVo, true);
					paramBullVo.setBullPid(Integer.parseInt(bullId));
					if (commonSvc.count(paramBullVo) > 0) {
						// bb.msg.deleteBull.hasReply=답변글이 있는 게시물은 삭제할 수 없습니다.
						throw new CmException("bb.msg.deleteBull.hasReply", request);
					}
				}
			}
			
			model.put("result", "ok");
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}

	/** 게시물 조회 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/viewBullMng")
	public String viewBullMng(HttpServletRequest request,
		@RequestParam(value = "brdId", required = true) String brdId,
		@RequestParam(value = "bullId", required = true) String bullId,
		@RequestParam(value = "secu", required = false) String secu,
		ModelMap model) throws Exception {

		if (brdId.isEmpty() || bullId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 게시판관리(BA_BRD_B) - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		model.put("baBrdBVo", baBrdBVo);

		// 컬럼표시여부 리스트
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, true, null, null, null, false);
		model.put("baColmDispDVoList", baColmDispDVoList);
		
		// 컬럼표시여부 맵으로 세팅
		Map<String, BaColmDispDVo> colmMap = bbBrdSvc.getBaColmDispDVoListMap(model, baColmDispDVoList, true, true, "Y", null);
				
		// 컬럼표시여부 맵
		Map<String, BaColmDispDVo> baColmDispDVoMap = bbBullSvc.getColmDispMap(baColmDispDVoList);
		model.put("baColmDispDVoMap", baColmDispDVoMap);

		// 확장컬럼 코드 리스트 model에 추가
		bbBullSvc.putColmCdToModel(request, baColmDispDVoList, model, null);

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
		
		// 전사게시판이면서 게시글의 회사ID와 사용자 회사ID가 같은지 비교
		if("Y".equals(baBrdBVo.getAllCompYn()) && !userVo.getCompId().equals(bbBullLVo.getCompId())){
			model.put("eqCompYn", "N");
		}
		
		// 한줄 답변 조회용 목록
		List<BbBullLVo> bbBullLVoForCmtList = new ArrayList<BbBullLVo>();
		bbBullLVoForCmtList.add(bbBullLVo);
				
		// 보안글이면
		if ("Y".equals(bbBullLVo.getSecuYn()) && secu != null) {
		/*	if (pw == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}*/
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
		
		// json [사용자,조직] 데이터를 List Map 형태로 변환
		bbBullSvc.setColListJsonToMap(langTypCd, colmMap, null, bbBullLVo);
		
		// 비공개 여부
		bbBullTgtSvc.setPrivYn(model, baBrdBVo, bbBullLVo, bullId, userVo.getUserUid());
				
		// 관련글 목록
		if ("Y".equals(baBrdBVo.getReplyYn())) {
			// 게시물(BB_X000X_L) 테이블 - SELECT
			BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(baBrdBVo, true);
			paramBullVo.setReplyGrpId(bbBullLVo.getReplyGrpId());
			String orderBy = "T.REPLY_GRP_ID DESC, T.REPLY_ORDR ASC";
			paramBullVo.setOrderBy(orderBy);
			List<BbBullLVo> replyBullList = (List<BbBullLVo>) commonSvc.queryList(paramBullVo);
			model.put("replyBullList", replyBullList);
			
			int i, size = replyBullList==null ? 0 : replyBullList.size();
			for(i=0;i<size;i++){
				bbBullLVoForCmtList.add(replyBullList.get(i));
			}
		}

		// 이전글/다음글
		if ("Y".equals(baBrdBVo.getPrevNextYn())) {
			BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(baBrdBVo, true);
			VoUtil.bind(request, paramBullVo);
			if ("Y".equals(baBrdBVo.getReplyYn())) {
				paramBullVo.setReplyDpth(0);
				paramBullVo.setBullId(bbBullLVo.getReplyGrpId());
			}
			// PREV
			paramBullVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.selectPrevId");
			Integer prevId = commonSvc.queryInt(paramBullVo);
			if (prevId != null) {
				// 게시물(BB_X000X_L) 테이블 - SELECT
				BbBullLVo prevBullVo = bbBullSvc.getBbBullLVo(baBrdBVo, prevId, langTypCd);
				model.put("prevBullVo", prevBullVo);
				
				if(prevBullVo!=null){
					bbBullLVoForCmtList.add(prevBullVo);
				}
			}
			// NEXT
			paramBullVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.selectNextId");
			Integer nextId = commonSvc.queryInt(paramBullVo);
			if (nextId != null) {
				// 게시물(BB_X000X_L) 테이블 - SELECT
				BbBullLVo nextBullVo = bbBullSvc.getBbBullLVo(baBrdBVo, nextId, langTypCd);
				model.put("nextBullVo", nextBullVo);
				
				if(nextBullVo!=null){
					bbBullLVoForCmtList.add(nextBullVo);
				}
			}
		}
		
		// 한줄답변 수 조회
		bbCmSvc.setCmtCount(bbBullLVoForCmtList);
				
		// 게시물첨부파일 리스트 model에 추가
		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());

		// 포토게시판여부
		if ("Y".equals(baBrdBVo.getPhotoYn())) {
			// 게시물사진 세팅
			bbBullLVo.setPhotoVo(bbBullPhotoSvc.getPhotoVo(Integer.valueOf(bullId)));
		}

		// listPage
		model.addAttribute("listPage", "listBullMng");
		model.addAttribute("viewPage", "viewBullMng");
		model.addAttribute("setPage", "setBullMng");
		model.addAttribute("delAction", "transBullDelAjx");
		model.addAttribute("prevNextYn", "Y".equals(baBrdBVo.getPrevNextYn())); // 이전다음 버튼 표시여부
		model.addAttribute("etcDIspYn", false);                                 // 기타(점수, 추천, 찬반투표) 표시여부

		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});

		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId", "pw"));
		model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "bullId", "pw"));
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		if("POST".equals(request.getMethod())){
			// get 파라미터를 post 파라미터로 전달하기 위해
			model.put("paramEntryList", ParamUtil.getEntryMapList(request, "pw"));
		}
		
		// 관리자메뉴의 관리권한 여부에 따라 삭제(답변일괄) 처리
		// 전사 게시판 일 경우에는 시스템관리자 이외에는 답변 여부를 체크.
		boolean isSysAdmin = SecuUtil.hasAuth(request, "Y".equals(baBrdBVo.getAllCompYn()) ? "SYS" : "A", null);
		model.put("isSysAdmin", isSysAdmin);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
				
		return LayoutUtil.getJspPath("/bb/viewBull");
	}

	/** 게시물 수정용 조회 화면 (관리자) */
	@RequestMapping(value = "/bb/adm/setBullMng")
	public String setBullMng(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "bullId", required = true) String bullId,
			ModelMap model) throws Exception {

		if (brdId.isEmpty() || bullId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);

		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		model.put("baBrdBVo", baBrdBVo);

		if ("Y".equals(baBrdBVo.getCatYn())) {
			// 카테고리 목록 얻기
			model.put("baCatDVoList", bbBullSvc.getBaCatDVoList(baBrdBVo.getCatGrpId(), langTypCd));
		}

		// 컬럼표시여부 리스트
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, true, null, null, null, false);
		model.put("baColmDispDVoList", baColmDispDVoList);
		
		// 컬럼표시여부 맵으로 세팅
		Map<String, BaColmDispDVo> colmMap = bbBrdSvc.getBaColmDispDVoListMap(model, baColmDispDVoList, true, true, "Y", null);
				
		// 확장컬럼 코드 리스트 model에 추가
		bbBullSvc.putColmCdToModel(request, baColmDispDVoList, model, "Y");

		// 게시물(BB_X000X_L) 테이블 - SELECT
		BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);
		if (bbBullLVo == null) {
			// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("bb.msg.bullNotExists", request);
		}
		model.put("bbBullLVo", bbBullLVo);

		// 날짜 초기화
		bbBullSvc.initBullRezvDt(bbBullLVo);
		bbBullSvc.initBullExprDt(baBrdBVo, bbBullLVo);

		// 게시대상 model에 추가
		bbBullTgtSvc.putTgtListToModel(bbBullLVo, model);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		// 게시물첨부파일 리스트 model에 추가
		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());
		
		// json [사용자,조직] 데이터를 List Map 형태로 변환
		bbBullSvc.setColListJsonToMap(langTypCd, colmMap, null, bbBullLVo);
		
		// 비공개 여부
		bbBullTgtSvc.setPrivYn(model, baBrdBVo, bbBullLVo, bullId, userVo.getUserUid());
					
		// 포토게시판여부
		if ("Y".equals(baBrdBVo.getPhotoYn())) {
			// 게시물사진 세팅
			bbBullLVo.setPhotoVo(bbBullPhotoSvc.getPhotoVo(Integer.valueOf(bullId)));
		}
		
		// action
		model.addAttribute("action", "transBull");
		model.addAttribute("listPage", "listBullMng");
		model.addAttribute("viewPage", "viewBullMng");
		model.addAttribute("bullRezvDtYn", false);      // 게시예약일 활성화여부
		model.addAttribute("bullRezvChecked", false);   // 게시예약일 체크박스 체크여부
		model.addAttribute("bbChoiYn", false);          // 게시판선택 표시여부
		model.addAttribute("tmpSaveYn", false);         // 임시저장 버튼 표시여부
		boolean bbTgtDispYn = bbBullLVo.getBullPid() == null && !"Y".equals(baBrdBVo.getAllCompYn()) && "N".equals(baBrdBVo.getDeptBrdYn());
		model.addAttribute("bbTgtDispYn", bbTgtDispYn); // 게시대상 표시여부 (답변글이 아니고, 전사게시판이 아니고, 부서게시판이 아니면 표시)

		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});

		// 최대 본문 사이즈 model에 추가
		bbBullSvc.putBodySizeToModel(request, model);

		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId"));
		model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "bullId"));

		return LayoutUtil.getJspPath("/bb/setBull");
	}

	/** 게시물 저장 (관리자) */
	@RequestMapping(value = "/bb/adm/transBull", method = RequestMethod.POST)
	public String transBullAdm(HttpServletRequest request,
			ModelMap model) {

		UploadHandler uploadHandler = null;
		try {

			// Multipart 파일 업로드
			uploadHandler = bbBullFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();

			// parameters
			String viewPage     = ParamUtil.getRequestParam(request, "viewPage", true);
			String brdId        = ParamUtil.getRequestParam(request, "brdId", true);
			String bullId       = ParamUtil.getRequestParam(request, "bullId", false);
			String bullStatCd   = ParamUtil.getRequestParam(request, "bullStatCd", true);

			if (viewPage.isEmpty() || brdId.isEmpty() || bullStatCd.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			/**
			 * 게시판 관리자의 수정 저장시 상태변경(상신) 무시하고 내용만 저장해야함.
			 * */
			bullStatCd = "B";

			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);

			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);

			// 게시물 저장
			QueryQueue queryQueue = new QueryQueue();
			Integer storedBullId = bbBullSvc.saveBbBullLVo(request, baBrdBVo, bullId, bullStatCd, queryQueue);

			// 게시파일 저장
			List<CommonFileVo> deletedFileList = bbBullFileSvc.saveBullFile(request, String.valueOf(storedBullId), queryQueue);

			// 포토게시판이면
			if ("Y".equals(baBrdBVo.getPhotoYn())) {
				// 게시물사진 저장
				deletedFileList.add(bbBullPhotoSvc.savePhoto(request, storedBullId, "photo", queryQueue));
			}

			commonSvc.execute(queryQueue);

			// 파일 삭제
			bbBullFileSvc.deleteDiskFiles(deletedFileList);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace('" + viewPage + "');");

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
	
	/** 첨부파일 다운로드 (관리자) */
	@RequestMapping(value = "/bb/adm/downFile", method = RequestMethod.POST)
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "bullIds", required = true) String bullIds) throws Exception {

		try {
			if (bullIds.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// fileId
			String[] bullIdArray = bullIds.split(",");
			List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
			for (String bullId : bullIdArray) {
				// 게시물첨부파일 목록
				List<CommonFileVo> fileList = bbBullFileSvc.getFileVoList(bullId);
				
				if (fileList != null && fileList.size() > 0) {
					for (CommonFileVo fileVo : fileList) {
						File file = new File(fileVo.getSavePath());
						if (file.isFile()) {
							fileVoList.add(fileVo);
						}
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
	
	
	/** 엑셀파일 다운로드 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/excelDownLoad")
	public ModelAndView excelDownLoad(HttpServletRequest request,
			@RequestParam(value = "brdId", required = false) String brdId,
			@RequestParam(value = "ext", required = false) String ext,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
		baBrdBVo.setOrderBy("T.BRD_ID ASC");
		
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
		
		model.put("baBrdBVoList", baBrdBVoList);
		
		if (baBrdBVoList == null || baBrdBVoList.size() == 0) {
			// bb.msg.noBb=생성된 게시판이 존재하지 않습니다.
			String msg = messageProperties.getMessage("bb.msg.noBb", request);
			throw new CmException(msg);
		}
		
		// 검색할 게시판 결정
		baBrdBVo = null;
		for (BaBrdBVo vo : baBrdBVoList) {
			if (vo.getBrdId().equals(brdId)) {
				baBrdBVo = vo;
			}
		}
		if (baBrdBVo == null) baBrdBVo = baBrdBVoList.get(0);
		
		// 게시물(BB_X000X_L) 테이블 - BIND
		BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(baBrdBVo);
		VoUtil.bind(request, paramBullVo);

		// 답변형인 경우
		String orderBy = "T.BULL_ID DESC";
		if ("Y".equals(baBrdBVo.getReplyYn())) {
			orderBy = "T.REPLY_GRP_ID DESC, T.REPLY_ORDR ASC";
		}
		paramBullVo.setOrderBy(orderBy);
		
		// 게시물(BB_X000X_L) 테이블 - COUNT
		paramBullVo.setBrdId(baBrdBVo.getBrdId());
		
		// 게시물(BB_X000X_L) 테이블 - SELECT
		List<BbBullLVo> list = (List<BbBullLVo>) commonSvc.queryList(paramBullVo);
		
		try {
			ModelAndView mv = new ModelAndView("excelDownloadView");
			if(list.size() == 0 ){
				mv = new ModelAndView("cm/result/commonResult");
				mv.addObject("message", messageProperties.getMessage("em.msg.noExcelData", request));
				return mv;
			}
			BaColmDispDVo storedBaColmDispDVo = null;
			BaTblColmDVo storedColmVo = null;
			List<BaColmDispDVo> baColmDispDVoList = new ArrayList<BaColmDispDVo>();
			String[][] columns = null;
			columns = new String[][]{{"SUBJ","cols.subj"},{"REGR_UID","cols.regr"},{"REG_DT","cols.regDt"},{"READ_CNT","cols.readCnt"}};
			for(String[] column : columns){
				storedBaColmDispDVo = new BaColmDispDVo();
				storedBaColmDispDVo.setListDispYn("Y");
				storedBaColmDispDVo.setUseYn("Y");
				storedColmVo = new BaTblColmDVo();
				storedColmVo.setColmNm(column[0]);
				storedColmVo.setRescNm(messageProperties.getMessage(column[1], request));
				storedBaColmDispDVo.setColmVo(storedColmVo);
				baColmDispDVoList.add(storedBaColmDispDVo);
			}
			
			//컬럼명
			List<String> colNames = new ArrayList<String>();
			//데이터
			List<Object> colValue = null;
			Map<String,Object> colValues = new HashMap<String,Object>();
			Map<String,Object> tempMap = null;
			BaTblColmDVo colmVo = null;
			Integer cdListIndex;
			Object[] cdList = (Object[])model.get("cdList");
			List<PtCdBVo> ptCdBVoList = null;
			Object colValueTxt;
			for(int i=0;i<list.size();i++){
				tempMap = VoUtil.toMap(list.get(i), null);
				colValue = new ArrayList<Object>();
				cdListIndex = null;
				for(BaColmDispDVo baColmDispDVo : baColmDispDVoList){
					if("Y".equals(baColmDispDVo.getUseYn()) && "Y".equals(baColmDispDVo.getListDispYn())){
						colmVo = baColmDispDVo.getColmVo();
						if("Y".equals(baColmDispDVo.getListDispYn())) {
							if("Y".equals(baColmDispDVo.getUseYn()) && "Y".equals(colmVo.getExColmYn())){
								if("TEXT".equals(colmVo.getColmTyp()) || "TEXTAREA".equals(colmVo.getColmTyp()) || "PHONE".equals(colmVo.getColmTyp()) || "CALENDAR".equals(colmVo.getColmTyp())){
									colValue.add(list.get(i).getExColm(colmVo.getColmNm()));
								}else if("CODE".equals(colmVo.getColmTyp())){
									if(cdListIndex == null) cdListIndex = 0; else cdListIndex++;
									ptCdBVoList = (List<PtCdBVo>)cdList[cdListIndex];
									for(int k=0;k<ptCdBVoList.size();k++){
										if(ptCdBVoList.get(k).getCd().equals(list.get(i).getExColm(colmVo.getColmNm()))) {
											colValue.add(ptCdBVoList.get(k).getRescNm());
											break;
										}
									}
								}
							}else{							
								colValueTxt = "";
								if("CAT_ID".equals(colmVo.getColmNm())){
									colValueTxt = (Object)tempMap.get("catNm"); 
								}else if("REGR_UID".equals(colmVo.getColmNm())){
									colValueTxt = (Object)tempMap.get("regrNm");
								}else if("EXPR_DT".equals(colmVo.getColmNm())){
									colValueTxt = (Object)tempMap.get("bullExprDt");
								}else if("SUBJ".equals(colmVo.getColmNm())){
									if("Y".equals((String)tempMap.get("ugntYn"))) colValueTxt = colValueTxt + "["+messageProperties.getMessage("bb.option.ugnt", request)+"]";
									if("Y".equals((String)tempMap.get("secuYn"))) colValueTxt = colValueTxt + "["+messageProperties.getMessage("bb.option.secu", request)+"]";
									colValueTxt = colValueTxt + (String)tempMap.get("subj");
								}else{
									colValueTxt = (Object)tempMap.get(StringUtil.toCamelNotation(colmVo.getColmNm(), false));
								}
								colValue.add(colValueTxt);
							}
						}
						if( i == 0 )	colNames.add(colmVo.getRescNm()); //컬럼명 세팅
					}
				}
				colValues.put("col"+i,colValue);//데이터 세팅
			}
			
			mv.addObject("sheetName", baBrdBVo.getRescNm());//시트명
			mv.addObject("colNames", colNames);//컬럼명
			mv.addObject("colValues", colValues);//데이터
			mv.addObject("fileName", baBrdBVo.getRescNm());//파일명
			mv.addObject("ext", ext == null ? "xlsx" : ext);//파일 확장자(없으면 xls)
			
			return mv;
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	
	
	/** 팝업설정 */
	@RequestMapping(value = "/bb/adm/setPopupPop")
	public String setPopupPop(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) Integer bullId,
			@RequestParam(value = "brdId", required = true) String brdId,
			ModelMap model) throws SQLException {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 게시판관리(BA_BRD_B) - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		model.put("baBrdBVo", baBrdBVo);
		
		BaBullPopupDVo baBullPopupDVo =  new BaBullPopupDVo();
		baBullPopupDVo.setBrdId(brdId);
		baBullPopupDVo.setBullId(bullId);
		baBullPopupDVo =  (BaBullPopupDVo) commonSvc.queryVo(baBullPopupDVo);
		
		model.put("baBullPopupDVo", baBullPopupDVo);
		return LayoutUtil.getJspPath("/bb/setPopupPop");
	}
	
	/** 팝업설정 등록 수정 (저장) */
	@RequestMapping(value = "/bb/adm/transSetPopup")
	public String transBcScrn(HttpServletRequest request,
			@Parameter(name="menuId", required=true) String menuId,
			Locale locale,
			ModelMap model) throws Exception {
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();

			BaBullPopupDVo baBullPopupDVo = new BaBullPopupDVo();
			VoUtil.bind(request, baBullPopupDVo);

			baBullPopupDVo.setModrUid(userVo.getUserUid());
			baBullPopupDVo.setModDt("sysdate");
			baBullPopupDVo.setCompId(userVo.getCompId());
			
			queryQueue.store(baBullPopupDVo);

			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("todo", "parent.location.replace('./viewBullMng.do?menuId=" + menuId+"&brdId="+baBullPopupDVo.getBrdId()+"&bullId="+baBullPopupDVo.getBullId()+"');");
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 등록자 변경 (관리자) */
	@RequestMapping(value = "/bb/adm/transBullChnAjx")
	public String transBullChnAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String brdId = (String) object.get("brdId");
			JSONArray jsonArray = (JSONArray) object.get("bullIds");
			String regrUid = (String) object.get("regrUid");
			
			if (brdId == null || jsonArray == null || regrUid == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
						
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
			// 게시물(BB_X000X_L) 테이블 - 생성
			BbBullLVo bbBullLVo = null;
						
			QueryQueue queryQueue = new QueryQueue();
			String tableName=bbTblSvc.getFullTblNm(baBrdBVo.getTblNm());
			String bullId = null;
			for (int i = 0; i < jsonArray.size(); i++) {
				bullId = (String) jsonArray.get(i);
				bbBullLVo = bbBullSvc.newBbBullLVo(baBrdBVo);
				// 테이블명
				bbBullLVo.setTableName(tableName);
				// 게시물ID 세팅
				bbBullLVo.setBullId(Integer.parseInt(bullId));
				// 등록자UID 변경
				bbBullLVo.setRegrUid(regrUid);
				// 수정자UID 초기화
				bbBullLVo.setModrUid("");
				queryQueue.update(bbBullLVo);
			}
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
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
