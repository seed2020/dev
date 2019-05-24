package com.innobiz.orange.web.bb.ctrl;

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

import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullFileSvc;
import com.innobiz.orange.web.bb.svc.BbBullPhotoSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.svc.BbBullTgtSvc;
import com.innobiz.orange.web.bb.svc.BbCmSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaColmDispDVo;
import com.innobiz.orange.web.bb.vo.BaTmpSaveLVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
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

/* 게시물 임시저장함 */
@Controller
public class BbBullTmpSaveCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BbBullTmpSaveCtrl.class);

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
	
	/** 게시물 임시저장 목록 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/listTmpBull")
	public String listTmpBull(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시물 임시저장(BA_TMP_SAVE_L) 테이블 - BIND
		BaTmpSaveLVo baTmpSaveLVo = new BaTmpSaveLVo();
		baTmpSaveLVo.setQueryLang(langTypCd);
		VoUtil.bind(request, baTmpSaveLVo);
		
		String orderBy = "T.BULL_ID DESC";
		baTmpSaveLVo.setOrderBy(orderBy);
		
		// 게시물 임시저장(BA_TMP_SAVE_L) 테이블 - COUNT
		baTmpSaveLVo.setRegrUid(userVo.getUserUid());
		Integer recodeCount = commonSvc.count(baTmpSaveLVo);
		PersonalUtil.setPaging(request, baTmpSaveLVo, recodeCount);
		
		// 게시물 임시저장(BA_TMP_SAVE_L) 테이블 - SELECT
		List<BaTmpSaveLVo> baTmpSaveLVoList = (List<BaTmpSaveLVo>) commonSvc.queryList(baTmpSaveLVo);
		// 첨부파일 수 조회
		bbBullFileSvc.setFileCount(baTmpSaveLVoList);
		
		model.put("recodeCount", recodeCount);
		model.put("baTmpSaveLVoList", baTmpSaveLVoList);
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
				
		return LayoutUtil.getJspPath("/bb/listTmpBull");
	}

	/** 게시물 임시저장 수정용 조회 화면 (사용자) */
	@RequestMapping(value = "/bb/setTmpBull")
	public String setTmpBull(HttpServletRequest request,
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
		
		// 게시물 임시저장(BA_TMP_SAVE_L) 테이블 - SELECT
		BaTmpSaveLVo baTmpSaveLVo = new BaTmpSaveLVo();
		baTmpSaveLVo.setRegrUid(userVo.getUserUid());
		baTmpSaveLVo.setBullId(Integer.parseInt(bullId));
		baTmpSaveLVo = (BaTmpSaveLVo) commonSvc.queryVo(baTmpSaveLVo);
		if (baTmpSaveLVo == null) {
			// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("bb.msg.bullNotExists", request);
		}
		
		// 사용자 권한 체크
		bbCmSvc.checkUserAuth(request, "W", baTmpSaveLVo.getRegrUid());
		
		// 게시판ID목록
		String brdIdList = baTmpSaveLVo.getBrdIdList();
		if (brdIdList != null && !"".equals(brdIdList)) {
			model.put("brdIdList", brdIdList);
			model.put("brdNms", bbBrdSvc.getBrdNms(langTypCd, brdIdList));
		}
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, baTmpSaveLVo.getBrdId());
		model.put("baBrdBVo", baBrdBVo);
		
		// 게시물(BB_X000X_L) 테이블 - SELECT
		BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);
		if (bbBullLVo == null) {
			// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("bb.msg.bullNotExists", request);
		}
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
		
		if ("Y".equals(baBrdBVo.getCatYn())) {
			// 카테고리 목록 얻기
			model.put("baCatDVoList", bbBullSvc.getBaCatDVoList(baBrdBVo.getCatGrpId(), langTypCd));
		}
		
		// 컬럼표시여부 리스트
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, baTmpSaveLVo.getBrdId(), true, null, null, null, false);
		model.put("baColmDispDVoList", baColmDispDVoList);
		
		// 컬럼표시여부 맵
		Map<String, BaColmDispDVo> baColmDispDVoMap = bbBullSvc.getColmDispMap(baColmDispDVoList);
		model.put("baColmDispDVoMap", baColmDispDVoMap);
		
		// 확장컬럼 코드 리스트 model에 추가
		bbBullSvc.putColmCdToModel(request, baColmDispDVoList, model, "Y");
		
		// 컬럼표시여부 맵으로 세팅
		Map<String, BaColmDispDVo> colmMap = bbBrdSvc.getBaColmDispDVoListMap(model, baColmDispDVoList, true, true, "Y", null);
		
		// json [사용자,조직] 데이터를 List Map 형태로 변환
		bbBullSvc.setColListJsonToMap(langTypCd, colmMap, null, bbBullLVo);
				
		// 기본값 세팅
		bbBullSvc.initBullRezvDt(bbBullLVo);
		bbBullSvc.initBullExprDt(baBrdBVo, bbBullLVo);
		
		// 게시대상 model에 추가
		bbBullTgtSvc.putTgtListToModel(bbBullLVo, model);
		
		// 게시물첨부파일 리스트 model에 추가
		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());

		// 포토게시판여부
		if ("Y".equals(baBrdBVo.getPhotoYn())) {
			// 게시물사진 세팅
			bbBullLVo.setPhotoVo(bbBullPhotoSvc.getPhotoVo(Integer.valueOf(bullId)));
		}
		
		// action
		model.addAttribute("action", "transTmpBull");
		model.addAttribute("delAction", "transTmpBullDelAjx");
		model.addAttribute("listPage", "listTmpBull");
		model.addAttribute("bullRezvDtYn", true);                                       // 게시예약일 활성화여부
		model.addAttribute("bullRezvChecked", "Y".equals(bbBullLVo.getBullRezvYn()));   // 게시예약일 체크박스 체크여부
		model.addAttribute("bbChoiYn", true);                                           // 게시판선택 표시여부
		model.addAttribute("tmpSaveYn", true);                                          // 임시저장 버튼 표시여부
		boolean bbTgtDispYn = bbBullLVo.getBullPid() == null && !"Y".equals(baBrdBVo.getAllCompYn()) && "N".equals(baBrdBVo.getDeptBrdYn());
		model.addAttribute("bbTgtDispYn", bbTgtDispYn);                                 // 게시대상 표시여부 (답변글이 아니고, 전사게시판이 아니고, 부서게시판이 아니면 표시)
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});

		// 최대 본문 사이즈 model에 추가
		bbBullSvc.putBodySizeToModel(request, model);
		
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId", "pw"));
		model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "bullId", "pw"));
		
		return LayoutUtil.getJspPath("/bb/setBull");
	}

	/** 게시물 임시저장 저장 (사용자) */
	@RequestMapping(value = "/bb/transTmpBull", method = RequestMethod.POST)
	public String transTmpBull(HttpServletRequest request,
			ModelMap model) {

		UploadHandler uploadHandler = null;
		try {

			// Multipart 파일 업로드
			uploadHandler = bbBullFileSvc.upload(request);

			// MultipartRequest
			request = uploadHandler.getMultipartRequest();

			// parameters
			String listPage     = ParamUtil.getRequestParam(request, "listPage", true);
			String brdId        = ParamUtil.getRequestParam(request, "brdId", true);
			String bullId       = ParamUtil.getRequestParam(request, "bullId", false);
			String bullStatCd   = ParamUtil.getRequestParam(request, "bullStatCd", true);
			String bullRezvDt   = ParamUtil.getRequestParam(request, "bullRezvDt", false);
			
			if (listPage.isEmpty() || brdId.isEmpty() || bullStatCd.isEmpty()) {
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
			List<CommonFileVo> deletedFileList = null;
			if ("T".equals(bullStatCd)) {
				// 게시물 저장
				bbBullSvc.saveBbBullLVo(request, baBrdBVo, bullId, bullStatCd, queryQueue);

				// 게시파일 저장
				deletedFileList = bbBullFileSvc.saveBullFile(request, bullId, queryQueue);

				// 포토게시판이면
				if ("Y".equals(baBrdBVo.getPhotoYn())) {
					// 게시물사진 저장
					deletedFileList.add(bbBullPhotoSvc.savePhoto(request, Integer.valueOf(bullId), "photo", queryQueue));
				}
				
				// 게시물 임시저장 저장
				bbBullSvc.saveBaTmpSaveLVo(request, Integer.parseInt(bullId), queryQueue);
				
			} else if ("S".equals(bullStatCd)) {
				// 게시물 저장
				bbBullSvc.saveBbBullLVo(request, baBrdBVo, bullId, bullStatCd, queryQueue);

				// 게시파일 저장
				deletedFileList = bbBullFileSvc.saveBullFile(request, bullId, queryQueue);

				// 포토게시판이면
				if ("Y".equals(baBrdBVo.getPhotoYn())) {
					// 게시물사진 저장
					deletedFileList.add(bbBullPhotoSvc.savePhoto(request, Integer.valueOf(bullId), "photo", queryQueue));
				}
				
				// 게시물 임시저장 삭제
				bbBullSvc.deleteBaTmpSaveLVo(request, Integer.parseInt(bullId), queryQueue);
				
				// 게시상신함 저장
				bbBullSvc.saveBaBullSubmLVo(request, Integer.parseInt(bullId), baBrdBVo.getDiscrUid(), queryQueue);
				
			} else if ("R".equals(bullStatCd)) {
				// 게시물 저장
				bbBullSvc.saveBbBullLVo(request, baBrdBVo, bullId, bullStatCd, queryQueue);

				// 게시파일 저장
				deletedFileList = bbBullFileSvc.saveBullFile(request, bullId, queryQueue);

				// 포토게시판이면
				if ("Y".equals(baBrdBVo.getPhotoYn())) {
					// 게시물사진 저장
					deletedFileList.add(bbBullPhotoSvc.savePhoto(request, Integer.valueOf(bullId), "photo", queryQueue));
				}
				
				// 게시물 임시저장 삭제
				bbBullSvc.deleteBaTmpSaveLVo(request, Integer.parseInt(bullId), queryQueue);
				
				// 게시물 예약저장 저장
				bbBullSvc.saveBaRezvSaveLVo(request, Integer.parseInt(bullId), queryQueue);
				
			} else if ("B".equals(bullStatCd)) {
				// 게시물 임시저장 삭제
				bbBullSvc.deleteBaTmpSaveLVo(request, Integer.parseInt(bullId), queryQueue);
				
				// 기존 게시물 삭제
				bbBullSvc.deleteBull(baBrdBVo, Integer.parseInt(bullId), queryQueue);
				
				// 게시물 저장
				Integer newBullId = bbBullSvc.saveBbBullLVo(request, baBrdBVo, null, bullStatCd, queryQueue);

				// 게시파일 저장
				deletedFileList = bbBullFileSvc.saveBullFile(request, bullId, queryQueue);

				// 포토게시판이면
				if ("Y".equals(baBrdBVo.getPhotoYn())) {
					// 게시물사진 저장
					deletedFileList.add(bbBullPhotoSvc.savePhoto(request, Integer.valueOf(bullId), "photo", queryQueue));
				}
				
				// 게시파일 참조ID 수정
				bbBullFileSvc.updateRefId(bullId, String.valueOf(newBullId), queryQueue);
			}
			
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			bbBullFileSvc.deleteDiskFiles(deletedFileList);
			
			if ("T".equals(bullStatCd)) {
				// bb.msg.save.tmp.success=임시저장함에 저장 되었습니다.
				model.put("message", messageProperties.getMessage("bb.msg.save.tmp.success", request));
			} else {
				// cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			}
			model.put("todo", "parent.location.replace('" + listPage + "');");

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

	/** [AJAX] 게시물 임시저장 삭제 (사용자) */
	@RequestMapping(value = "/bb/transTmpBullDelAjx")
	public String transTmpBullDelAjx(HttpServletRequest request,
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
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 게시물 임시저장(BA_TMP_SAVE_L) 테이블 - SELECT
			BaTmpSaveLVo paramTmpSaveVo = new BaTmpSaveLVo();
			paramTmpSaveVo.setRegrUid(userVo.getUserUid());
			paramTmpSaveVo.setBullId(Integer.parseInt(bullId));
			BaTmpSaveLVo baTmpSaveLVo = (BaTmpSaveLVo) commonSvc.queryVo(paramTmpSaveVo);
			
			if (baTmpSaveLVo == null) {
				// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("bb.msg.bullNotExists", request);
			}
			
			// 게시물 임시저장 삭제
			QueryQueue queryQueue = new QueryQueue();
			bbBullSvc.deleteBaTmpSaveLVo(request, Integer.parseInt(bullId), queryQueue);
			
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

}
