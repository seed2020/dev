package com.innobiz.orange.mobile.bb.ctrl;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullFavotSvc;
import com.innobiz.orange.web.bb.svc.BbBullFileSvc;
import com.innobiz.orange.web.bb.svc.BbBullPhotoSvc;
import com.innobiz.orange.web.bb.svc.BbBullRecmdSvc;
import com.innobiz.orange.web.bb.svc.BbBullScreSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.svc.BbBullTgtSvc;
import com.innobiz.orange.web.bb.svc.BbCmSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaBullPopupDVo;
import com.innobiz.orange.web.bb.vo.BaCmtDVo;
import com.innobiz.orange.web.bb.vo.BaColmDispDVo;
import com.innobiz.orange.web.bb.vo.BaReadHstLVo;
import com.innobiz.orange.web.bb.vo.BaScreHstLVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmSnsSvc;
import com.innobiz.orange.web.em.utils.EmConstant;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wf.svc.WfMdFormSvc;

/* 게시물 */
@Controller
public class MoBbBullCtrl {
	
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
	
	/** 게시물사진 서비스 */
	@Resource(name = "bbBullPhotoSvc")
	private BbBullPhotoSvc bbBullPhotoSvc;
	
	/** 게시대상 서비스 */
	@Resource(name = "bbBullTgtSvc")
	public BbBullTgtSvc bbBullTgtSvc;

	/** 게시파일 서비스 */
	@Resource(name = "bbBullFileSvc")
	private BbBullFileSvc bbBullFileSvc;
	
	/** 게시물 추천 서비스 */
	@Resource(name = "bbBullRecmdSvc")
	private BbBullRecmdSvc bbBullRecmdSvc;

	/** 게시물 찬반투표 서비스 */
	@Resource(name = "bbBullFavotSvc")
	private BbBullFavotSvc bbBullFavotSvc;

	/** 게시물 점수주기 서비스 */
	@Resource(name = "bbBullScreSvc")
	private BbBullScreSvc bbBullScreSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** SNS 서비스 */
	@Autowired
	private EmSnsSvc emSnsSvc;
	
	@Autowired
	private WfMdFormSvc wfMdFormSvc;
	
	/** 게시물 목록 (사용자) */
	@RequestMapping(value = "/bb/listBull")
	public String listBull(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "listTyp", required = false) String listTyp,
			ModelMap model) throws Exception {
		
		if (brdId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		model.put("baBrdBVo", baBrdBVo);
		
		// 컬럼표시여부 리스트
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, true, null, "Y", null, false);
		baColmDispDVoList = bbBrdSvc.getMobileListSort(baBrdBVo, baColmDispDVoList);
		model.put("baColmDispDVoList", baColmDispDVoList);
		
		// 확장컬럼 코드 리스트 model에 추가
		bbBullSvc.putColmCdToModel(request, baColmDispDVoList, model, null);
				
		if ("Y".equals(baBrdBVo.getCatYn())) {
			// 카테고리 목록 얻기
			model.put("baCatDVoList", bbBullSvc.getBaCatDVoList(baBrdBVo.getCatGrpId(), langTypCd));
		}

		// 게시물(BB_X000X_L) 테이블 - BIND
		BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(baBrdBVo, true);
		VoUtil.bind(request, paramBullVo);
				
		// 조회조건 적용
		bbBullSvc.setListCondApply(request, model, baBrdBVo, baColmDispDVoList, paramBullVo);
				
		// 사용자정보 세팅
		bbBullSvc.setUserInfo(request, paramBullVo);
		
		// 부서게시판이면
		if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
			if (userVo.getDeptId() == null) {
				paramBullVo.setDeptId("DEPT_ID_NULL");
			} else {
				bbBullSvc.setOrgHstIdList(langTypCd, userVo, paramBullVo);
				//paramBullVo.setDeptId(userVo.getDeptId());
			}
			paramBullVo.setTgtSchYn("Y"); // 게시대상 조회여부			
		}
		
		// 전사게시판이면
		if ("Y".equals(baBrdBVo.getAllCompYn())) {
			paramBullVo.setCompId(null);
		} else {
			paramBullVo.setCompId(userVo.getCompId());
		}
		
		// 답변형인 경우
		String orderBy = "T.BULL_ID DESC";
		if ("Y".equals(baBrdBVo.getReplyYn())) {
			orderBy = "T.REPLY_GRP_ID DESC, T.REPLY_ORDR ASC";
		}
		paramBullVo.setOrderBy(orderBy);
		
		// 게시물(BB_X000X_L) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(paramBullVo);
		PersonalUtil.setPaging(request, paramBullVo, recodeCount);
		
		// 게시물 목록
		List<BbBullLVo> bbBullLVoList = bbBullSvc.getBbBullVoList(baBrdBVo, paramBullVo);
		
		// 컬럼표시여부 맵으로 세팅[목록표시여부'Y']
		Map<String, BaColmDispDVo> colmMap = bbBrdSvc.getBaColmDispDVoListMap(null, baColmDispDVoList, false, true, null, "Y");
				
		// json [사용자,조직] 데이터를 List Map 형태로 변환
		if(bbBullLVoList!=null && bbBullLVoList.size()>0)
			bbBullSvc.setColListJsonToMap(langTypCd, colmMap, bbBullLVoList, null);
		
		// 게시물 맵으로 변환
		List<Map<String,Object>> bbBullLVoListMap=bbBullSvc.getListToMap(model, baBrdBVo, bbBullLVoList);
		if(bbBullLVoListMap!=null)
			model.put("bbBullLVoListMap", bbBullLVoListMap);
				
		// 공지글 목록
		// 게시물(BB_X000X_L) 테이블 - SELECT
		paramBullVo.setNotcYn("Y");
		String strtYmd = StringUtil.afterDays(-baBrdBVo.getNotcDispPrd());
		paramBullVo.setStrtYmd(strtYmd);
		List<BbBullLVo> notcBullList = bbBullSvc.getBbBullVoList(baBrdBVo, paramBullVo);
		
		// json [사용자,조직] 데이터를 List Map 형태로 변환
		if(notcBullList!=null && notcBullList.size()>0)
			bbBullSvc.setColListJsonToMap(langTypCd, colmMap, notcBullList, null);
		
		// 공지글 맵으로 변환
		bbBullLVoListMap=bbBullSvc.getListToMap(model, baBrdBVo, notcBullList);
		if(bbBullLVoListMap!=null)
			model.put("notcBullListMap", bbBullLVoListMap);
				
		model.put("recodeCount", recodeCount);
		model.put("bbBullLVoList", bbBullLVoList);
		model.put("notcBullList", notcBullList);

		// 포토게시판여부
		String jspPath = "/bb/listBull";
		if ("Y".equals(baBrdBVo.getPhotoYn()) && !"B".equals(listTyp)) {
			jspPath = "/bb/listPhotoBull";
			// 게시물사진 세팅
			bbBullPhotoSvc.setPhotoVo(bbBullLVoList);
		}
		
		model.addAttribute("listPage", "listBull");
		model.addAttribute("viewPage", "viewBull");
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId", "pw", "noCache", "secu"));
		
		return MoLayoutUtil.getJspPath(jspPath);
	}
	
	/** 게시물 조회 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/viewBull")
	public String viewBull(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "bullId", required = true) String bullId,
			@RequestParam(value = "secu", required = false) String secu,
			@RequestParam(value = "compId", required = false) String compId,
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
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, false, null, null, null, true);
		List<BaColmDispDVo> dispList=bbBrdSvc.getColDispList(baColmDispDVoList, null, "Y", false, true);
		model.put("baColmDispDVoList", dispList);
		
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
		
		// 양식 사용여부[본문삽입용]
		String wfFormNo=bbBrdSvc.getBrdToFormNo(baBrdBVo);
		
		if(wfFormNo!=null){
			wfMdFormSvc.viewFormByBB(request, model, wfFormNo, brdId, bullId);
		}else{
			//lobHandler
			model.put("lobHandler", lobHandler.create(
					"SELECT CONT FROM "+bbBullLVo.getTableName()+" WHERE BULL_ID = ? AND BRD_ID = ?", 
					new String[]{bullId, brdId}
			));
		}
		
		model.put("bbBullLVo", bbBullLVo);
		
		// 한줄 답변 조회용 목록
		List<BbBullLVo> bbBullLVoForCmtList = new ArrayList<BbBullLVo>();
		bbBullLVoForCmtList.add(bbBullLVo);
				
		// 보안글이면
		if ("Y".equals(bbBullLVo.getSecuYn())) {
			// 비밀번호 확인
			if (secu == null || secu.isEmpty() || !bbCmSvc.isValidUserPw(request, secu)) {
				// pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				//throw new CmException("pt.login.noUserNoPw", request);
				return MoLayoutUtil.getJspPath("/bb/setLoginCfrm");
			}
		}
		
		// 게시대상 체크여부
		boolean isTgtChk=true;
		
		// 법인별 게시판 관리여부[ 회사ID + 메뉴 관리권한]
		boolean isCoprUser=compId!=null && !compId.isEmpty() && SecuUtil.hasAuth(request, "A", null);
				
		// 부서게시판이면
		if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
			if(!"Y".equals(bbBullLVo.getTgtDeptYn()) && !"Y".equals(bbBullLVo.getTgtUserYn())) bbCmSvc.checkUserDeptAuth(request, bbBullLVo.getDeptId());
			if(userVo.getDeptId() != null && userVo.getDeptId().equals(bbBullLVo.getDeptId())) isTgtChk=false;
			//bbCmSvc.checkUserDeptAuth(request, bbBullLVo.getDeptId());
		}
		
		// 게시대상 model에 추가
		bbBullTgtSvc.putTgtListToModel(bbBullLVo, model);

		// 게시대상에 포함되는가?
		if(isTgtChk) bbBullTgtSvc.checkTgt(request, bbBullLVo, model);
		
		// json [사용자,조직] 데이터를 List Map 형태로 변환
		bbBullSvc.setColListJsonToMap(langTypCd, colmMap, null, bbBullLVo);
		
		// 비공개 여부
		bbBullTgtSvc.setPrivYn(model, baBrdBVo, bbBullLVo, bullId, userVo.getUserUid());
				
		// 대표컬럼 조회(목록기준)
		BaColmDispDVo firstVo = bbBrdSvc.getFirstBaColmDispDVo(baBrdBVo, bbBrdSvc.getBaColmDispDVoList(request, brdId, true, null, "Y", null, false));
		
		// 대표 컬럼 SUBJ 로 세팅
		bbBullSvc.setBbBullLVoSubj(langTypCd, baBrdBVo, bbBullLVo, firstVo, "viewTitle");
		
		// 조회이력 저장
		if (bbBullSvc.saveReadHst(bullId, userVo.getUserUid())) {
			// 조회수 증가
			bbBullSvc.addReadCnt(baBrdBVo, Integer.parseInt(bullId));
		}

		// 관련글 목록
		if ("Y".equals(baBrdBVo.getReplyYn())) {
			// 게시물(BB_X000X_L) 테이블 - SELECT
			BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(baBrdBVo, true);
			paramBullVo.setReplyGrpId(bbBullLVo.getReplyGrpId());
			String orderBy = "T.REPLY_GRP_ID DESC, T.REPLY_ORDR ASC";
			paramBullVo.setOrderBy(orderBy);
			paramBullVo.setQueryLang(langTypCd);
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
			
			if(!isCoprUser){
				// 사용자정보 세팅
				bbBullSvc.setUserInfo(request, paramBullVo);
				
				// 부서게시판이면
				if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
					if (userVo.getDeptId() == null) {
						paramBullVo.setDeptId("DEPT_ID_NULL");
					} else {
						bbBullSvc.setOrgHstIdList(langTypCd, userVo, paramBullVo);
						//paramBullVo.setDeptId(userVo.getDeptId());
					}
					paramBullVo.setTgtSchYn("Y"); // 게시대상 조회여부
				}
				
				// 전사게시판이면
				if ("Y".equals(baBrdBVo.getAllCompYn())) {
					paramBullVo.setCompId(null);
				} else {
					paramBullVo.setCompId(userVo.getCompId());
				}
			}
			
			// PREV
			paramBullVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.selectPrevId");
			Integer prevId = commonSvc.queryInt(paramBullVo);
			if (prevId != null) {
				// 게시물(BB_X000X_L) 테이블 - SELECT
				BbBullLVo prevBullVo = bbBullSvc.getBbBullLVo(baBrdBVo, prevId, langTypCd);
				
				// json [사용자,조직] 데이터를 List Map 형태로 변환
				bbBullSvc.setColListJsonToMap(langTypCd, colmMap, null, prevBullVo);
				
				// 대표 컬럼 SUBJ 로 세팅
				bbBullSvc.setBbBullLVoSubj(langTypCd, baBrdBVo, prevBullVo, firstVo, null);
				
				model.put("prevBullVo", prevBullVo);
			}
			// NEXT
			paramBullVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.selectNextId");
			Integer nextId = commonSvc.queryInt(paramBullVo);
			if (nextId != null) {
				// 게시물(BB_X000X_L) 테이블 - SELECT
				BbBullLVo nextBullVo = bbBullSvc.getBbBullLVo(baBrdBVo, nextId, langTypCd);
				
				// json [사용자,조직] 데이터를 List Map 형태로 변환
				bbBullSvc.setColListJsonToMap(langTypCd, colmMap, null, nextBullVo);
				
				// 대표 컬럼 SUBJ 로 세팅
				bbBullSvc.setBbBullLVoSubj(langTypCd, baBrdBVo, nextBullVo, firstVo, null);
				
				model.put("nextBullVo", nextBullVo);
			}
		}
		
		// 한줄답변 수 조회
		bbCmSvc.setCmtCount(bbBullLVoForCmtList);
				
		// 게시물첨부파일 리스트 model에 추가
		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());
		
		// 기타(점수, 추천, 찬반투표) 참가여부 model에 추가
		bbBullSvc.putEtcToModel(request, baBrdBVo, bullId, model);
		
		// 포토게시판여부
		if ("Y".equals(baBrdBVo.getPhotoYn())) {
			// 게시물사진 세팅
			bbBullLVo.setPhotoVo(bbBullPhotoSvc.getPhotoVo(Integer.valueOf(bullId)));
		}
		
		// 한줄답변(BA_CMT_D) 테이블 - BIND
		BaCmtDVo baCmtDVo = new BaCmtDVo();
		VoUtil.bind(request, baCmtDVo);
		
		// 한줄답변(BA_CMT_D) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(baCmtDVo);
		model.addAttribute("recodeCount", recodeCount);
		PersonalUtil.setPaging(request, baCmtDVo, recodeCount);
		baCmtDVo.setPageRowCnt(recodeCount);
		
		// 한줄답변(BA_CMT_D) 테이블 - SELECT
		List<BaCmtDVo> baCmtDVoList = (List<BaCmtDVo>) commonSvc.queryList(baCmtDVo);
		model.addAttribute("baCmtDVoList", baCmtDVoList);
		
		model.addAttribute("listPage", "listBull");
		model.addAttribute("viewPage", "viewBull");
		model.addAttribute("setPage", "setBull");
		
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId", "pw", "noCache", "secu"));
		model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "bullId", "pw", "noCache", "secu"));
		
		model.addAttribute("prevNextYn", "Y".equals(baBrdBVo.getPrevNextYn()));             // 이전다음 버튼 표시여부
		//model.addAttribute("etcDIspYn", true);                                              // 기타(점수, 추천, 찬반투표) 표시여부

		return MoLayoutUtil.getJspPath("/bb/viewBull");
	}
	
	/** [AJAX] 게시 비밀번호 / 사용자 비밀번호 확인 */
	@RequestMapping(value = "/bb/getLoginChkAjx")
	public String getLoginChkAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {

		String message = null;
		try {
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String secu =	(String)jsonObject.get("secu");
			// 비밀번호 확인
			if (!bbCmSvc.isValidUserPw(request, secu)) {
				// pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				throw new CmException("pt.login.noUserNoPw", request);
			}
			model.put("result", "ok");

		} catch(CmException e){
			message = e.getMessage();
		} catch(Exception e){
			message = e.getMessage();
			e.printStackTrace();
		}
		
		return MoLayoutUtil.returnJson(model, message);
	}

	
	/** [AJAX] 게시물 삭제 (사용자) */
	@RequestMapping(value = "/bb/transBullDelAjx")
	public String transBullDelAjx(HttpServletRequest request,
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
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
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
			
			// 게시물 삭제
			QueryQueue queryQueue = new QueryQueue();
			bbBullSvc.deleteBull(baBrdBVo, Integer.parseInt(bullId), queryQueue);
			
			// 게시물첨부파일 삭제
			List<CommonFileVo> deletedFileList = bbBullFileSvc.deleteBullFile(bullId, queryQueue);

			// 포토게시판이면
			if ("Y".equals(baBrdBVo.getPhotoYn())) {
				// 게시물사진 삭제
				deletedFileList.add(bbBullPhotoSvc.deletePhoto(Integer.valueOf(bullId), queryQueue));
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 팝업창 설정 정보 삭제
			BaBullPopupDVo baBullPopupDVo =  new BaBullPopupDVo();
			baBullPopupDVo.setBrdId(brdId);
			baBullPopupDVo.setBullId(Integer.parseInt(bullId));
			//baBullPopupDVo.setCompId(baBrdBVo.getCompId());
			baBullPopupDVo.setCompId(userVo.getCompId());
			queryQueue.delete(baBullPopupDVo);
			
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			bbBullFileSvc.deleteDiskFiles(deletedFileList);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}

	/** [AJAX] 게시물 삭제 (사용자) */
	@RequestMapping(value = "/bb/transBullDelAjxChk")
	public String transBullDelAjxChk(HttpServletRequest request,
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
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
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
			
			model.put("result", "ok");
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 게시물 추천 (사용자) */
	@RequestMapping(value = "/bb/transBullRecmdAjx")
	public String transBullRecmdAjx(HttpServletRequest request,
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

			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);

			// 게시물(BB_X000X_L) 테이블 - SELECT
			BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);

			if (bbBullLVo == null) {
				// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("bb.msg.bullNotExists", request);
			}

			// 부서게시판이면
			if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
				bbCmSvc.checkUserDeptAuth(request, bbBullLVo.getDeptId());
			}

			QueryQueue queryQueue = new QueryQueue();
			// 추천이력 존재여부
			if (bbBullRecmdSvc.isRecmdHstExist(bullId, userVo.getUserUid())) {
				// bb.msg.recmd.already=이미 추천하였습니다.
				throw new CmException("bb.msg.recmd.already", request);
			} else {
				// 추천이력 저장
				bbBullRecmdSvc.insertRecmdHst(bullId, userVo.getUserUid(), queryQueue);
				// 추천수 증가
				bbBullRecmdSvc.addRecmdCnt(baBrdBVo, Integer.parseInt(bullId), queryQueue);
			}

 			commonSvc.execute(queryQueue);

			// bb.msg.recmd.success=추천하였습니다.
			model.put("message", messageProperties.getMessage("bb.msg.recmd.success", request));
			model.put("result", "ok");
			model.put("recmdCnt", bbBullLVo.getRecmdCnt() + 1);

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}

	/** [AJAX] 게시물 찬반투표 (사용자) */
	@RequestMapping(value = "/bb/transBullFavotAjx")
	public String transBullFavotAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String brdId = (String) object.get("brdId");
			String bullId = (String) object.get("bullId");
			String favotVa = (String) object.get("favotVa");
			if (brdId == null || bullId == null || favotVa == null || !"FA".contains(favotVa)) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);

			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);

			// 게시물(BB_X000X_L) 테이블 - SELECT
			BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);

			if (bbBullLVo == null) {
				// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("bb.msg.bullNotExists", request);
			}

			// 부서게시판이면
			if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
				bbCmSvc.checkUserDeptAuth(request, bbBullLVo.getDeptId());
			}

			QueryQueue queryQueue = new QueryQueue();
			// 찬반투표 존재여부
			if (bbBullFavotSvc.isFavotHstExist(bullId, userVo.getUserUid())) {
				// bb.msg.favot.already=이미 투표하였습니다.
				throw new CmException("bb.msg.favot.already", request);
			} else {
				// 찬반투표 저장
				bbBullFavotSvc.insertFavotHst(bullId, userVo.getUserUid(), favotVa, queryQueue);
				// 찬성수/반대수 증가
				bbBullFavotSvc.addFavotCnt(baBrdBVo, Integer.parseInt(bullId), favotVa, queryQueue);
			}

 			commonSvc.execute(queryQueue);

			// bb.msg.favot.success=투표하였습니다.
			model.put("message", messageProperties.getMessage("bb.msg.favot.success", request));
			model.put("result", "ok");
			if ("F".equals(favotVa)) {
				model.put("prosCnt", bbBullLVo.getProsCnt() + 1);
			} else {
				model.put("consCnt", bbBullLVo.getConsCnt() + 1);
			}

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}

	/** [AJAX] 게시물 점수주기 (사용자) */
	@RequestMapping(value = "/bb/transBullScreAjx")
	public String transBullScreAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String brdId = (String) object.get("brdId");
			String bullId = (String) object.get("bullId");
			String strScre = (String) object.get("scre");
			int scre = Integer.parseInt(strScre);
			if (brdId == null || bullId == null || scre < 1 || scre > 5) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);

			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);

			// 게시물(BB_X000X_L) 테이블 - SELECT
			BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);

			if (bbBullLVo == null) {
				// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("bb.msg.bullNotExists", request);
			}

			// 부서게시판이면
			if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
				bbCmSvc.checkUserDeptAuth(request, bbBullLVo.getDeptId());
			}

			QueryQueue queryQueue = new QueryQueue();
			// 점수주기 존재여부
			if (bbBullScreSvc.isScreHstExist(bullId, userVo.getUserUid())) {
				// bb.msg.scre.already=이미 점수를 준 게시물입니다.
				throw new CmException("bb.msg.scre.already", request);
			} else {
				// 점수주기 저장
				bbBullScreSvc.insertScreHst(bullId, userVo.getUserUid(), scre, queryQueue);
				// 점수 업데이트
				bbBullScreSvc.updateScre(baBrdBVo, Integer.parseInt(bullId), queryQueue);
			}

 			commonSvc.execute(queryQueue);

			// bb.msg.scre.success=점수를 저장하였습니다.
			model.put("message", messageProperties.getMessage("bb.msg.scre.success", request));
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}

	/** 점수내역 */
	@RequestMapping(value = "/bb/viewScrePop")
	public String viewScrePop(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			ModelMap model) throws SQLException {

		// 평균점수
		Integer avgScre = bbBullScreSvc.getAvgScre(Integer.valueOf(bullId));
		model.put("avgScre", avgScre);

		// 점수 목록
		List<BaScreHstLVo> baScreHstLVoList = bbBullScreSvc.getBaScreHstLVoList(request, Integer.parseInt(bullId));
		model.put("baScreHstLVoList", baScreHstLVoList);

		return MoLayoutUtil.getJspPath("/bb/viewScrePop");
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/bb/downFile","/bb/preview/downFile"})
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
				CommonFileVo fileVo = bbBullFileSvc.getFileVo(Integer.valueOf(fileId));
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
				
			} 
			
		} catch (CmException e) {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	/** [팝업] 조회자 정보 (사용자) */
	@RequestMapping(value = "/bb/listReadHstSub")
	public String listReadHstPop(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			Locale locale,
			ModelMap model) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 조회이력(BA_READ_HST_L) 테이블 - SELECT
		BaReadHstLVo baReadHstLVo = new BaReadHstLVo();
		baReadHstLVo.setQueryLang(langTypCd);
		baReadHstLVo.setBullId(Integer.parseInt(bullId));
		
		Integer recodeCount = commonSvc.count(baReadHstLVo);
		PersonalUtil.setPaging(request, baReadHstLVo, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<BaReadHstLVo> baReadHstLVoList = (List<BaReadHstLVo>) commonSvc.queryList(baReadHstLVo);
		
		for (BaReadHstLVo readHstVo : baReadHstLVoList) {
			// 사용자기본(OR_USER_B) 테이블 - SELECT
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setQueryLang(langTypCd);
			orUserBVo.setUserUid(readHstVo.getUserUid());
			orUserBVo = (OrUserBVo) commonSvc.queryVo(orUserBVo);
			if(orUserBVo==null) continue;
			readHstVo.setOrUserBVo(orUserBVo);
			
			// 원직자기본(OR_ODUR_B) 테이블 - SELECT
			OrOdurBVo orOdurBVo = new OrOdurBVo();
			orOdurBVo.setOdurUid(orUserBVo.getOdurUid());
			orOdurBVo = (OrOdurBVo) commonSvc.queryVo(orOdurBVo);
			if(orOdurBVo==null) continue;
			readHstVo.setOrOdurBVo(orOdurBVo);
		}
		
		model.put("baReadHstLVoList", baReadHstLVoList);
		model.put("recodeCount", recodeCount);
		
		model.put("UI_TITLE", messageProperties.getMessage("bb.jsp.listReadHstPop.title", locale));
		return MoLayoutUtil.getJspPath("/bb/listReadHstSub");
	}
	
	/** [팝업] 게시판 담당자  */
	@RequestMapping(value = "/bb/viewBullPichPop")
	public String viewBullPichPop(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "listTyp", required = false) String listTyp,
			ModelMap model) throws Exception {
		
		if (brdId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		model.put("baBrdBVo", baBrdBVo);
		
		// 담당자 정보 세팅
		bbBrdSvc.setPichVo(baBrdBVo, langTypCd);

		return MoLayoutUtil.getJspPath("/bb/viewBullPichPop");
	}
	
	/** 게시물 수정용 조회 및 등록 화면 (사용자) */
	@RequestMapping(value = {"/bb/setBull", "/bb/setNewBull", "/bb/setMyBull"})
	public String setBull(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "bullId", required = false) String bullId,
			ModelMap model) throws Exception {

		if (brdId.isEmpty()) {
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
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, false, null, null, null, true);
		model.put("baColmDispDVoList", bbBrdSvc.getColDispList(baColmDispDVoList, null, "Y", false, true));
		
		// 컬럼표시여부 맵으로 세팅
		Map<String, BaColmDispDVo> colmMap = bbBrdSvc.getBaColmDispDVoListMap(model, baColmDispDVoList, true, true, "Y", null);
				
		// 확장컬럼 코드 리스트 model에 추가
		bbBullSvc.putColmCdToModel(request, baColmDispDVoList, model, "Y");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 양식 사용여부[본문삽입용]
		String wfFormNo=bbBrdSvc.getBrdToFormNo(baBrdBVo);
		
		if(wfFormNo!=null){
			wfMdFormSvc.setFormByBB(request, model, wfFormNo, brdId, bullId);
		}
				
		BbBullLVo bbBullLVo = null;
				
		// 수정인 경우
		if (bullId != null && !bullId.isEmpty()) {
			// 게시물(BB_X000X_L) 테이블 - SELECT
			bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);
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
			
			// 사용자 권한 체크
			bbCmSvc.checkUserAuth(request, "M", bbBullLVo.getRegrUid());
			
			// 게시대상 체크여부
			boolean isTgtChk=true;
						
			// 부서게시판이면
			if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
				if(!"Y".equals(bbBullLVo.getTgtDeptYn()) && !"Y".equals(bbBullLVo.getTgtUserYn())) bbCmSvc.checkUserDeptAuth(request, bbBullLVo.getDeptId());
				if(userVo.getDeptId() != null && userVo.getDeptId().equals(bbBullLVo.getDeptId())) isTgtChk=false;
			}
						
			// 날짜 초기화
			bbBullSvc.initBullRezvDt(bbBullLVo);
			bbBullSvc.initBullExprDt(baBrdBVo, bbBullLVo);
			
			// 게시대상 model에 추가
			bbBullTgtSvc.putTgtListToModel(bbBullLVo, model);
						
			// 게시대상에 포함되는가?
			if(isTgtChk) bbBullTgtSvc.checkTgt(request, bbBullLVo, model);
			
			// json [사용자,조직] 데이터를 List Map 형태로 변환
			bbBullSvc.setColListJsonToMap(langTypCd, colmMap, null, bbBullLVo);
			
			// 비공개 여부
			bbBullTgtSvc.setPrivYn(model, baBrdBVo, bbBullLVo, bullId, userVo.getUserUid());
						
			// 포토게시판여부
			if ("Y".equals(baBrdBVo.getPhotoYn())) {
				// 게시물사진 세팅
				bbBullLVo.setPhotoVo(bbBullPhotoSvc.getPhotoVo(Integer.valueOf(bullId)));
			}
			
			// 답변형인 경우
			if ("Y".equals(baBrdBVo.getReplyYn())) {
				BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(baBrdBVo, true);
				paramBullVo.setBullPid(Integer.parseInt(bullId));
				//게시대상 수정 가능여부[원본글에 답변이 있을경우 게시대상을 수정할수 없다.]
				model.put("bbTgtYn", commonSvc.count(paramBullVo) > 0 );
			}
						
		} else {
			bbBullLVo = new BbBullLVo();
			// 날짜 초기화
			bbBullSvc.initBullRezvDt(bbBullLVo);
			bbBullSvc.initBullExprDt(baBrdBVo, bbBullLVo);
			
			model.put("bbBullLVo", bbBullLVo);
		}
		
		// 게시물첨부파일 리스트 model에 추가
		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());
		
		// SNS 조회
		bbBullSvc.setSnsList(model, userVo.getCompId(), brdId, bullId, null);
				
		// setPage
		String setPage = request.getRequestURI().replace("/bb/", "").replace(".do", "");
		
		// action
		if ("setMyBull".equals(setPage)) {
			model.addAttribute("action", "transBull");
			model.addAttribute("listPage", "listMyBull");
			model.addAttribute("viewPage", "viewMyBull");
		} else if ("setNewBull".equals(setPage)) {
			model.addAttribute("action", "transBull");
			model.addAttribute("listPage", "listNewBull");
			model.addAttribute("viewPage", "viewNewBull");
		} else {
			model.addAttribute("action", "transBull");
			model.addAttribute("listPage", "listBull");
			model.addAttribute("viewPage", "viewBull");
		}
		model.addAttribute("bullRezvDtYn", (bullId == null || bullId.isEmpty()));   // 게시예약일 활성화여부
		model.addAttribute("bullRezvChecked", false);                               // 게시예약일 체크박스 체크여부
		model.addAttribute("bbChoiYn", (bullId == null || bullId.isEmpty()));       // 게시판선택 표시여부
		model.addAttribute("tmpSaveYn", (bullId == null || bullId.isEmpty()));      // 임시저장 버튼 표시여부
		//boolean bbTgtDispYn = bbBullLVo.getBullPid() == null && !"Y".equals(baBrdBVo.getAllCompYn()) && "N".equals(baBrdBVo.getDeptBrdYn());
		boolean bbTgtDispYn = bbBullLVo.getBullPid() == null && !"Y".equals(baBrdBVo.getAllCompYn());
		model.addAttribute("bbTgtDispYn", bbTgtDispYn);                             // 게시대상 표시여부 (답변글이 아니고, 전사게시판이 아니면 표시)
		
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId", "noCache"));
		if ("setMyBull".equals(setPage) || "setNewBull".equals(setPage)) {
			model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "brdId", "bullId", "noCache"));
		} else {
			model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "bullId", "noCache"));
		}
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"validator"});
				
		return MoLayoutUtil.getJspPath("/bb/setBull");
	}
	
	/** 게시물 저장 (사용자) */
	@RequestMapping(value = "/bb/transBullPost", method = RequestMethod.POST)
	public String transBull(HttpServletRequest request,
			ModelMap model) {
		
		UploadHandler uploadHandler = null;
		try {
			
			// Multipart 파일 업로드
			uploadHandler = bbBullFileSvc.upload(request);
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// parameters
			String listPage     = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage     = ParamUtil.getRequestParam(request, "viewPage", true);
			String brdId        = ParamUtil.getRequestParam(request, "brdId", true);
			String bullId       = ParamUtil.getRequestParam(request, "bullId", false);
			String bullStatCd   = ParamUtil.getRequestParam(request, "bullStatCd", true);
			String bullRezvDt   = ParamUtil.getRequestParam(request, "bullRezvDt", false);
			String bullExprDt   = ParamUtil.getRequestParam(request, "bullExprDt", false);
			
			if (listPage.isEmpty() || viewPage.isEmpty() || brdId.isEmpty() || bullStatCd.isEmpty()) {
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
			
			// 게시물 저장
			QueryQueue queryQueue = new QueryQueue();
			Integer storedBullId = bbBullSvc.saveBbBullLVo(request, baBrdBVo, bullId, bullStatCd, queryQueue);
			
			// 양식 사용여부[본문삽입용]
			String wfFormNo=bbBrdSvc.getBrdToFormNo(baBrdBVo);
			
			if(wfFormNo!=null){
				String genId = ParamUtil.getRequestParam(request, "genId", false);
				wfMdFormSvc.saveFormByBB(request, queryQueue, genId, wfFormNo, brdId, String.valueOf(storedBullId));
			}
						
			// 게시파일 저장
			List<CommonFileVo> deletedFileList = bbBullFileSvc.saveBullFile(request, String.valueOf(storedBullId), queryQueue);

			// 포토게시판이면
			if ("Y".equals(baBrdBVo.getPhotoYn())) {
				// 게시물사진 저장
 				deletedFileList.add(bbBullPhotoSvc.savePhoto(request, storedBullId, "photo", queryQueue));
			}
			
			if ("T".equals(bullStatCd)) {
				// 게시물 임시저장 저장
				bbBullSvc.saveBaTmpSaveLVo(request, storedBullId, queryQueue);
				
			} else if ("S".equals(bullStatCd)) {
				// 게시상신함 저장
				bbBullSvc.saveBaBullSubmLVo(request, storedBullId, baBrdBVo.getDiscrUid(), queryQueue);
				
			} else if ("R".equals(bullStatCd)) {
				// 게시물 예약저장 저장
				bbBullSvc.saveBaRezvSaveLVo(request, storedBullId, queryQueue);
			}
			
			commonSvc.execute(queryQueue);

			// 파일 삭제
			bbBullFileSvc.deleteDiskFiles(deletedFileList);
			
			// 신규 등록시 게시글 멀티 등록의 경우 첨부파일 멀티 복사
			queryQueue = new QueryQueue();
			if (bullId == null || bullId.isEmpty()) {
				Integer[] arrBullId = (Integer[])request.getAttribute("arrBullId");
				
				if (arrBullId != null && arrBullId.length > 0) {
					for (Integer anotherBullId : arrBullId) {
						// 첨부파일 저장
						bbBullFileSvc.copyBullFile(request, String.valueOf(storedBullId), String.valueOf(anotherBullId), queryQueue);
					}
				}
			}
			
			// SNS 저장
			bbBullSvc.saveSnsList(request, queryQueue, brdId, storedBullId);
						
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			boolean goView = false;
			// 수정이고, 게시예약일과 게시완료일 사이이고, 보안글이 아니면 조회화면으로 이동
			if (bullId != null && !bullId.isEmpty()) {
				goView = true;
				if ("R".equals(baBrdBVo.getKndCd())) {
					Date now = new Date();
					if (bullRezvDt != null && !bullRezvDt.isEmpty() && now.before(Timestamp.valueOf(bullRezvDt))) goView = false;
					if (bullExprDt != null && !bullExprDt.isEmpty() && now.after(Timestamp.valueOf(bullExprDt))) goView = false;
				}
			}
			if (goView) {
				model.put("todo", "$m.nav.prev(event, '" + viewPage + "');");
			} else {
				model.put("todo", "$m.nav.prev(event, '" + listPage + "');");
			}
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			// SNS 사용여부가 'Y' 일 경우 HTML 생성
			if(sysPlocMap!= null && sysPlocMap.containsKey("brdSnsEnable") && "Y".equals(sysPlocMap.get("brdSnsEnable"))){
				String subj = ParamUtil.getRequestParam(request, "subj", false);
				String cont = ParamUtil.getRequestParam(request, "cont", false);
				emSnsSvc.createSnsFile(request, EmConstant.SNS_URL+"/bb", String.valueOf(storedBullId), subj, cont, null);
			}

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("exception", e);
		} finally {
			if (uploadHandler != null) try { uploadHandler.removeTempDir(); } catch (CmException ignored) {}
		}

		return MoLayoutUtil.getResultJsp();
	}
	
	/** 게시물 답변 등록 화면 (사용자) */
	@RequestMapping(value = "/bb/setReply")
	public String setReply(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "bullPid", required = false) String bullPid,
			ModelMap model) throws Exception {

		if (brdId.isEmpty() || bullPid.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		model.put("baBrdBVo", baBrdBVo);
		
		// 컬럼표시여부 리스트
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, false, null, null, null, true);
		model.put("baColmDispDVoList", bbBrdSvc.getColDispList(baColmDispDVoList, null, "Y", false, true));
		
		// 컬럼표시여부 맵으로 세팅
		bbBrdSvc.getBaColmDispDVoListMap(model, baColmDispDVoList, true, true, "Y", null);
				
		// 게시물(BB_X000X_L) 테이블 - SELECT
		BbBullLVo parentBullVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullPid), langTypCd);
		model.put("parentBullVo", parentBullVo);

		BbBullLVo bbBullLVo = new BbBullLVo();
		// 날짜 초기화
		bbBullSvc.initBullRezvDt(bbBullLVo);
		bbBullSvc.initBullExprDt(baBrdBVo, bbBullLVo);
		// 제목 초기화
		bbBullLVo.setSubj("Re: " + parentBullVo.getSubj());
		// 내용 초기화
		StringBuilder cont = new StringBuilder();
		cont.append("<br/><br/>--------------------------------------------- ")
			.append(messageProperties.getMessage("bb.jsp.setReply.tx01", request))  // bb.jsp.setReply.tx01=원본 내용
			.append(" ---------------------------------------------<br/><br/>");
		if(parentBullVo.getCont()!=null && !parentBullVo.getCont().isEmpty())
			cont.append(parentBullVo.getCont());
		bbBullLVo.setCont(cont.toString());
		model.put("bbBullLVo", bbBullLVo);

		// 부서게시판이면
		if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
			bbCmSvc.checkUserDeptAuth(request, parentBullVo.getDeptId());
		}
		
		// 기본값 세팅
		bbBullSvc.initBullRezvDt(parentBullVo);
		if ("R".equals(baBrdBVo.getKndCd())) {
			parentBullVo.setBullExprDt(parentBullVo.getBullExprDt());
		}

		// 게시대상 model에 추가
		bbBullTgtSvc.putTgtListToModel(parentBullVo, model);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 게시물첨부파일 리스트 model에 추가
		bbBullFileSvc.putFileListToModel(null, model, userVo.getCompId());
		
		// SNS 조회
		bbBullSvc.setSnsList(model, userVo.getCompId(), brdId, null, null);
				
		// action
		model.addAttribute("action", "transReply");
		model.addAttribute("listPage", "listBull");
		model.addAttribute("viewPage", "viewBull");
		model.addAttribute("bullRezvDtYn", true);       // 게시예약일 활성화여부
		model.addAttribute("bullRezvChecked", false);   // 게시예약일 체크박스 체크여부
		model.addAttribute("bbChoiYn", false);          // 게시판선택 표시여부
		model.addAttribute("tmpSaveYn", false);         // 임시저장 버튼 표시여부
		model.addAttribute("bbTgtDispYn", false);       // 게시대상 표시여부 (답변글이 아니고, 전사게시판이 아니고, 부서게시판이 아니면 표시)

		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullPid", "noCache"));
		model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "bullPid", "noCache"));
		
		return MoLayoutUtil.getJspPath("/bb/setBull");
	}

	/** 게시물 답변 저장 (사용자) */
	@RequestMapping(value = "/bb/transReplyPost", method = RequestMethod.POST)
	public String transReply(HttpServletRequest request,
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
			String bullPid      = ParamUtil.getRequestParam(request, "bullPid", true);
			String bullRezvDt   = ParamUtil.getRequestParam(request, "bullRezvDt", false);
			
			if (brdId.isEmpty() || bullPid.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
			// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
			String bullStatCd = "B";
			if (bullRezvDt != null && !bullRezvDt.isEmpty() && StringUtil.isAfterNow(bullRezvDt)) {
				bullStatCd = "R";
			}
			
			// 게시물 답변 저장
			QueryQueue queryQueue = new QueryQueue();
			Integer newBullId = bbBullSvc.saveReplyBbBullLVo(request, baBrdBVo, bullStatCd, bullPid, queryQueue);

			// 게시파일 저장
			List<CommonFileVo> deletedFileList = bbBullFileSvc.saveBullFile(request, String.valueOf(newBullId), queryQueue);
			
			if ("R".equals(bullStatCd)) {
				// 게시물 예약저장 저장
				bbBullSvc.saveBaRezvSaveLVo(request, newBullId, queryQueue);
			}
			
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			bbBullFileSvc.deleteDiskFiles(deletedFileList);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "$m.nav.prev(event, '" + listPage + "');");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("exception", e);
		}

		return MoLayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 게시물 내용(HTML) 조회 */
	@RequestMapping(value = "/bb/getBullHtmlAjx")
	public String getBullHtmlAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String brdId = (String) object.get("brdId");
			String bullId = (String) object.get("bullId");
			
			if (brdId == null || brdId.isEmpty() || bullId == null || bullId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			if (baBrdBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시물(BB_X000X_L) 테이블 - SELECT
			BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd, true);
			if (bbBullLVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			if(sysPlocMap.get("namoEditorEnable")==null){
				String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(bbBullLVo.getCont());
				model.put("bodyHtml", _bodyHtml);
			} else {
				model.put("bodyHtml", bbBullLVo.getCont());
			}
			// FAQ 일 경우 첨부 파일 추가
			if(baBrdBVo.getBrdTypCd()!=null && "F".equals(baBrdBVo.getBrdTypCd()) && 
					baBrdBVo.getOptMap().get("fileUploadYn")!=null && "Y".equals(baBrdBVo.getOptMap().get("fileUploadYn"))){
				List<CommonFileVo> fileVoList = bbBullFileSvc.getFileVoList(bullId);
				model.put("fileVoList", fileVoList);
			}
			
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** [AJAX] 코드 목록 조회 */
	@RequestMapping(value = "/bb/getSelectCdListAjx")
	public String getSelectCdListAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {

		String message = null;
		try {
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String cdGrpId =	(String)jsonObject.get("cdGrpId");
			if (cdGrpId==null || cdGrpId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			model.put("cdList", bbBullSvc.getBaCdDVoList(langTypCd, cdGrpId));
			model.put("result", "ok");

		} catch(CmException e){
			message = e.getMessage();
		} catch(Exception e){
			message = e.getMessage();
			e.printStackTrace();
		}
		
		return MoLayoutUtil.returnJson(model, message);
	}
	
}
