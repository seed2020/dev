package com.innobiz.orange.web.bb.ctrl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
import com.innobiz.orange.web.bb.vo.BaBullFileDVo;
import com.innobiz.orange.web.bb.vo.BaBullPopupDVo;
import com.innobiz.orange.web.bb.vo.BaBullPopupDispDVo;
import com.innobiz.orange.web.bb.vo.BaBullSubmLVo;
import com.innobiz.orange.web.bb.vo.BaCdDVo;
import com.innobiz.orange.web.bb.vo.BaColmDispDVo;
import com.innobiz.orange.web.bb.vo.BaMyBullMVo;
import com.innobiz.orange.web.bb.vo.BaPopUserBVo;
import com.innobiz.orange.web.bb.vo.BaReadHstLVo;
import com.innobiz.orange.web.bb.vo.BaScreHstLVo;
import com.innobiz.orange.web.bb.vo.BaSetupBVo;
import com.innobiz.orange.web.bb.vo.BaSnsRVo;
import com.innobiz.orange.web.bb.vo.BaTblColmDVo;
import com.innobiz.orange.web.bb.vo.BaUserSetupBVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
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
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.svc.EmSendSvc;
import com.innobiz.orange.web.em.svc.EmSnsSvc;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.em.utils.EmConstant;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.em.vo.EmSendBVo;
import com.innobiz.orange.web.em.vo.EmSendFileDVo;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wf.svc.WfMdFormSvc;

/* 게시물 */
@Controller
public class BbBullCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BbBullCtrl.class);

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

	/** 게시물 추천 서비스 */
	@Resource(name = "bbBullRecmdSvc")
	private BbBullRecmdSvc bbBullRecmdSvc;

	/** 게시물 찬반투표 서비스 */
	@Resource(name = "bbBullFavotSvc")
	private BbBullFavotSvc bbBullFavotSvc;

	/** 게시물 점수주기 서비스 */
	@Resource(name = "bbBullScreSvc")
	private BbBullScreSvc bbBullScreSvc;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 문서 서비스 */
	@Resource(name = "dmDocSvc")
	private DmDocSvc dmDocSvc;
	
	/** 첨부설정 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 보내기 서비스 */
	@Autowired
	private EmSendSvc emSendSvc;
	
	/** SNS 서비스 */
	@Autowired
	private EmSnsSvc emSnsSvc;
	
	/** 모듈양식 서비스 */
	@Autowired
	private WfMdFormSvc wfMdFormSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 게시물 목록 (사용자) */
	@RequestMapping(value = "/bb/listBull")
	public String listBull(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "listTyp", required = false) String listTyp,
			@RequestParam(value = "compId", required = false) String compId,
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
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, true, null, null, null, false);
		model.put("baColmDispDVoList", baColmDispDVoList);
		
		// 확장컬럼 코드 리스트 model에 추가
		bbBullSvc.putColmCdToModel(request, baColmDispDVoList, model, null);
		
		// 담당자 정보 세팅
		bbBrdSvc.setPichVo(baBrdBVo, langTypCd);
		
		if ("Y".equals(baBrdBVo.getCatYn())) {
			// 카테고리 목록 얻기
			model.put("baCatDVoList", bbBullSvc.getBaCatDVoList(baBrdBVo.getCatGrpId(), langTypCd));
		}
		
		// 게시물(BB_X000X_L) 테이블 - BIND
		BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(baBrdBVo, true);
		VoUtil.bind(request, paramBullVo);
		
		// 조회조건 적용
		bbBullSvc.setListCondApply(request, model, baBrdBVo, baColmDispDVoList, paramBullVo);
				
		// 법인별 게시판 관리여부[ 회사ID + 메뉴 관리권한]
		boolean isCoprUser=compId!=null && !compId.isEmpty() && SecuUtil.hasAuth(request, "A", null);
		
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
				// 회사 ID 조회조건 추가[계열사 설정 확인]
				bbBullSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, paramBullVo);
				//paramBullVo.setCompId(null);
			} else {
				paramBullVo.setCompId(userVo.getCompId());
			}
		}
		
		// 답변형인 경우
		String orderBy = "T.BULL_ID DESC";
		if ("Y".equals(baBrdBVo.getReplyYn())) {
			orderBy = "T.REPLY_GRP_ID DESC, T.REPLY_ORDR ASC";
		}
		paramBullVo.setOrderBy(orderBy);
		
		// 정렬조건 적용
		//bbBullSvc.setSortOrdr(baColmDispDVoList, paramBullVo);
		
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
					
		// 공지글 목록
		// 게시물(BB_X000X_L) 테이블 - SELECT
		paramBullVo.setNotcYn("Y");
		String strtYmd = StringUtil.afterDays(-baBrdBVo.getNotcDispPrd());
		paramBullVo.setStrtYmd(strtYmd);
		List<BbBullLVo> notcBullList = bbBullSvc.getBbBullVoList(baBrdBVo, paramBullVo);
		
		// json [사용자,조직] 데이터를 List Map 형태로 변환
		if(notcBullList!=null && notcBullList.size()>0)
			bbBullSvc.setColListJsonToMap(langTypCd, colmMap, notcBullList, null);
		model.put("recodeCount", recodeCount);
		model.put("bbBullLVoList", bbBullLVoList);
		model.put("notcBullList", notcBullList);
		
		model.addAttribute("viewPage", "viewBull");
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});

		// 포토게시판여부
		String jspPath = "/bb/listBull";
		if ("Y".equals(baBrdBVo.getPhotoYn()) && !"B".equals(listTyp)) {
			jspPath = "/bb/listPhotoBull";
			// 게시물사진 세팅
			bbBullPhotoSvc.setPhotoVo(bbBullLVoList);
		}
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
				
		return LayoutUtil.getJspPath(jspPath);
	}
	
	/** 게시물 조회 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/bb/viewBull", "/bb/viewBullFrm", "/bb/viewBullOpen"})
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
		
		// 프레임 여부
		boolean isFrm=request.getRequestURI().indexOf("Frm")>0;
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시판관리(BA_BRD_B) - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		model.put("baBrdBVo", baBrdBVo);

		// 컬럼표시여부 리스트
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, false, null, null, null, true);
		List<BaColmDispDVo> dispList = bbBrdSvc.getColDispList(baColmDispDVoList, null, "Y", false, true);
		model.put("baColmDispDVoList", dispList);
		//model.put("baColmDispDVoList", baColmDispDVoList);
		
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
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// 업무관리 사용여부가 'Y' 일 경우 양식 데이터 조회
		if(sysPlocMap.containsKey("wfEnable") && "Y".equals(sysPlocMap.get("wfEnable"))){
			// 양식 사용여부[본문삽입용]
			String wfFormNo=bbBrdSvc.getBrdToFormNo(baBrdBVo);
			
			if(wfFormNo!=null){
				wfMdFormSvc.viewFormByBB(request, model, wfFormNo, brdId, bullId);
			}
		}
				
		//lobHandler
		model.put("lobHandler", lobHandler.create(
				"SELECT CONT FROM "+bbBullLVo.getTableName()+" WHERE BULL_ID = ? AND BRD_ID = ?", 
				new String[]{bullId, brdId}
		));
		
		model.put("bbBullLVo", bbBullLVo);
		
		// 로그인팝업사용자 체크
		if((baBrdBVo.getPhotoYn()==null || (baBrdBVo.getPhotoYn()!=null && !"Y".equals(baBrdBVo.getPhotoYn()))) && bbBullLVo.getBullPid()==null){
			model.put("isLginPop", bbBullSvc.isLginPopChk(userVo));
		}
				
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
		if(isCoprUser) isTgtChk=false;
		
		// 게시대상 model에 추가
		bbBullTgtSvc.putTgtListToModel(bbBullLVo, model);

		// 게시대상에 포함되는가?
		if(isTgtChk) bbBullTgtSvc.checkTgt(request, bbBullLVo, model);
		
		// json [사용자,조직] 데이터를 List Map 형태로 변환
		bbBullSvc.setColListJsonToMap(langTypCd, colmMap, null, bbBullLVo);
		
		// 비공개 여부
		bbBullTgtSvc.setPrivYn(model, baBrdBVo, bbBullLVo, bullId, userVo.getUserUid());
					
		// 대표 컬럼 SUBJ 로 세팅
		//bbBullSvc.setBbBullLVoSubj(langTypCd, baBrdBVo, dispList, bbBullLVo);
		
		// 조회이력 저장
		if (!isCoprUser && bbBullSvc.saveReadHst(bullId, userVo.getUserUid())) {
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
			// 대표컬럼 조회(목록기준)
			BaColmDispDVo firstVo = bbBrdSvc.getFirstBaColmDispDVo(baBrdBVo, bbBrdSvc.getBaColmDispDVoList(request, brdId, true, null, "Y", null, false));
			
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
					// 회사 ID 조회조건 추가[계열사 설정 확인]
					bbBullSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, paramBullVo);
					//paramBullVo.setCompId(null);
				} else {
					paramBullVo.setCompId(userVo.getCompId());
				}
			}
						
			// PREV
			paramBullVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.selectPrevId");
			Integer prevId = commonSvc.queryInt(paramBullVo);
			if (prevId != null) {
				// 게시물(BB_X000X_L) 테이블 - SELECT
				BbBullLVo prevBullVo = bbBullSvc.getBbBullLVo(baBrdBVo, prevId, langTypCd, false);
				
				// json [사용자,조직] 데이터를 List Map 형태로 변환
				bbBullSvc.setColListJsonToMap(langTypCd, colmMap, null, prevBullVo);
				
				// 대표 컬럼 SUBJ 로 세팅
				bbBullSvc.setBbBullLVoSubj(langTypCd, baBrdBVo, prevBullVo, firstVo, null);
				
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
				BbBullLVo nextBullVo = bbBullSvc.getBbBullLVo(baBrdBVo, nextId, langTypCd, false);
				
				// json [사용자,조직] 데이터를 List Map 형태로 변환
				bbBullSvc.setColListJsonToMap(langTypCd, colmMap, null, nextBullVo);
				
				// 대표 컬럼 SUBJ 로 세팅
				bbBullSvc.setBbBullLVoSubj(langTypCd, baBrdBVo, nextBullVo, firstVo, null);
				
				
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
		
		// 기타(점수, 추천, 찬반투표) 참가여부 model에 추가
		bbBullSvc.putEtcToModel(request, baBrdBVo, bullId, model);
		
		// listPage
		model.addAttribute("listPage", "listBull");
		model.addAttribute("viewPage", "viewBull");
		model.addAttribute("setPage", "setBull");
		model.addAttribute("delAction", "transBullDelAjx");
		model.addAttribute("prevNextYn", "Y".equals(baBrdBVo.getPrevNextYn()));             // 이전다음 버튼 표시여부
		model.addAttribute("etcDIspYn", true);                                              // 기타(점수, 추천, 찬반투표) 표시여부
		model.addAttribute("brdId", brdId);
		
		boolean bbTgtDispYn = bbBullLVo.getBullPid() == null && !"Y".equals(baBrdBVo.getAllCompYn());
		model.addAttribute("bbTgtDispYn", bbTgtDispYn);                             // 게시대상 표시여부 (답변글이 아니고, 전사게시판이 아니면 표시)
		
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
		
		// 관리자 여부상관없이 사용자단 화면에서는 답변 확인후 삭제처리
		model.put("isSysAdmin", false);
		
		if("Y".equals(sysPlocMap.get("mailEnable"))){
			model.put("mailEnable", "Y");
		}
		if(sysPlocMap.containsKey("dmEnable") && "Y".equals(sysPlocMap.get("dmEnable"))) model.put("dmEnable", "Y");
		else model.put("dmEnable", "N");
		
		// 윈도우 팝업 여부
		boolean isOpen=request.getRequestURI().indexOf("Open")>0;
		
		if(isFrm){// 프레임
			model.put("isFrm", isFrm);
			return LayoutUtil.getJspPath("/bb/viewBull","Frm");
		}else if(isOpen){
			model.put("isOpen", isOpen);
			model.put("MOBILE_VIEW", Boolean.TRUE);
			return LayoutUtil.getJspPath("/bb/viewBull","Frm");
		}
		
		// SNS 조회
		//bbBullSvc.setSnsList(model, userVo.getCompId(), brdId, bullId, "Y");
		
		return LayoutUtil.getJspPath("/bb/viewBull");
	}

	/** [팝업] 보안글 인증 (사용자) */
	@RequestMapping(value = {"/bb/setLoginPop", "/bb/adm/setLoginPop"})
	public String setLoginPop(HttpServletRequest request,
			ModelMap model) throws UnsupportedEncodingException {
		
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryList(request, "viewPage"));
		
		return LayoutUtil.getJspPath("/bb/setLoginPop");
	}

	/** [팝업] 조회자 정보 (사용자) */
	@RequestMapping(value = {"/bb/listReadHstPop", "/bb/adm/listReadHstPop"})
	public String listReadHstPop(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			ModelMap model) throws Exception {
		/**
		 *  기존 Pop 목록출력을 iframe 목록페이징으로 변경 처리함.
		**/
		return LayoutUtil.getJspPath("/bb/listReadHstPop");
	}
	
	/** [팝업] 조회자 정보 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/bb/listReadHstFrm", "/bb/adm/listReadHstFrm"})
	public String listReadHstFrm(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 조회이력(BA_READ_HST_L) 테이블 - SELECT
		BaReadHstLVo baReadHstLVo = new BaReadHstLVo();
		baReadHstLVo.setQueryLang(langTypCd);
		baReadHstLVo.setBullId(Integer.parseInt(bullId));
		
		Integer recodeCount = commonSvc.count(baReadHstLVo);
		PersonalUtil.setPaging(request, baReadHstLVo, recodeCount);
		
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
		
		return LayoutUtil.getJspPath("/bb/listReadHstFrm");
	}

	/** 게시물 수정용 조회 및 등록 화면 (사용자) */
	@RequestMapping(value = {"/bb/setBull", "/bb/setNewBull", "/bb/setMyBull", "/bb/setBullFaq", "/bb/setBullOpen"})
	public String setBull(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "bullId", required = false) String bullId,
			@RequestParam(value = "compId", required = false) String compId,
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
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// 양식 사용여부[본문삽입용]
		String wfFormNo=null;
					
		// 업무관리 사용여부가 'Y' 일 경우 양식 데이터 조회
		if(sysPlocMap.containsKey("wfEnable") && "Y".equals(sysPlocMap.get("wfEnable"))){
			wfFormNo=bbBrdSvc.getBrdToFormNo(baBrdBVo);
			
			if(wfFormNo!=null){
				wfMdFormSvc.setFormByBB(request, model, wfFormNo, brdId, bullId);
			}
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
			model.put("bbBullLVo", bbBullLVo);
			
			// 사용자 권한 체크
			bbCmSvc.checkUserAuth(request, "M", bbBullLVo.getRegrUid());
			
			// 게시대상 체크여부
			boolean isTgtChk=true;
			
			// 법인별 게시판 관리여부[ 회사ID + 메뉴 관리권한]
			boolean isCoprUser=compId!=null && !compId.isEmpty() && SecuUtil.hasAuth(request, "A", null);
			
			// 부서게시판이면
			if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
				if(!"Y".equals(bbBullLVo.getTgtDeptYn()) && !"Y".equals(bbBullLVo.getTgtUserYn())) bbCmSvc.checkUserDeptAuth(request, bbBullLVo.getDeptId());
				if(userVo.getDeptId() != null && userVo.getDeptId().equals(bbBullLVo.getDeptId())) isTgtChk=false;
			}			
			
			if(isCoprUser) isTgtChk=false;
			
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
		
        // 프레임 여부
        boolean isFrame=false;
        if(bullId==null || bullId.isEmpty()){
        	// 보내기번호 
            String sendNo = ParamUtil.getRequestParam(request, "sendNo", false);
            
            // 이메일번호 
            String emailNo = ParamUtil.getRequestParam(request, "emailNo", false);
            
        	// 이메일을 제외한 그외 업무에서 보낸경우
        	if((sendNo!=null && !sendNo.isEmpty()) || (emailNo!=null && !emailNo.isEmpty())){
        		// 메일여부
        		Boolean isMail=sendNo!=null && !sendNo.isEmpty() ? null : true;
        		// 테이블번호
        		String no=isMail==null ? sendNo : emailNo;
        		// 저장된 보내기 데이터 조회
            	EmSendBVo emSendBVo = emSendSvc.getEmSendBVo(no, isMail);
            	if(emSendBVo!=null){
            		bbBullLVo.setSubj(emSendBVo.getSubj()); // 제목
            		if(isMail!=null && isMail.booleanValue()){
            			// 첫 줄 삽입여부
            			boolean isFirstRow=sysPlocMap != null && sysPlocMap.containsKey("sendRowAddEnable") && "Y".equals(sysPlocMap.get("sendRowAddEnable"));						
            			// 첫 줄 
            			String cont=isFirstRow ? "<p><br/></p>" : "";
            			
            			if(emSendBVo.getCont()!=null)
        					cont+=emSendBVo.getCont();
        				if(!cont.isEmpty()) bbBullLVo.setCont(cont); // 내용
            		}else{
            			bbBullLVo.setCont(emSendBVo.getCont()); // 내용
            		}
            	}
            	
            	// 파일목록 조회
            	List<EmSendFileDVo> sendFileList = emSendSvc.getFileVoList(no, isMail);
            	
            	// model에 담을 파일 목록
            	List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
            	BaBullFileDVo baBullFileDVo = null;
            	for(EmSendFileDVo emSendFileDVo : sendFileList){
            		baBullFileDVo=new BaBullFileDVo();
            		emSendSvc.setFileVo((CommonVo)emSendFileDVo, (CommonVo)baBullFileDVo, "dispNm","fileExt","fileSize");
    				baBullFileDVo.setFileId(emSendFileDVo.getFileSeq());
    				baBullFileDVo.setRefId(sendNo);
    				baBullFileDVo.setUseYn("Y");
    				fileVoList.add(baBullFileDVo);
            	}
            	model.put("fileVoList", fileVoList);
            	// 파일 관련 설정 model에 담음
            	emSendSvc.putFileListToModel(model, userVo.getCompId(), "bb", bbBullFileSvc.getFilesId());
            	isFrame=true;
        	}else{
            	// 게시물첨부파일 리스트 model에 추가
        		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());
            }
        	
        }else{
        	// 게시물첨부파일 리스트 model에 추가
    		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());
        }

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
		} else if ("setBullFaq".equals(setPage)) {
			model.addAttribute("action", "transBull");
			model.addAttribute("listPage", "listBull");
			model.addAttribute("viewPage", "listBull");
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
		
		// 게시대상에서 pc 알림 여부
		if(request.getRequestURI().startsWith("/bb/setBull") && bbBullSvc.isPcNoti(request, userVo) && baBrdBVo.getOptMap()!=null && baBrdBVo.getOptMap().get("bbOptYn")!=null 
				&& "Y".equals(baBrdBVo.getOptMap().get("bbOptYn"))){
			model.put("isPcNoti", Boolean.TRUE);
		}
		
		// 에디터 사용
		if((colmMap!=null && colmMap.get("cont")!=null && "Y".equals(colmMap.get("cont").getReadDispYn())) || wfFormNo!=null)		
			model.addAttribute("JS_OPTS", new String[]{"editor"});

		// 최대 본문 사이즈 model에 추가
		bbBullSvc.putBodySizeToModel(request, model);
		
		// SNS 조회
		bbBullSvc.setSnsList(model, userVo.getCompId(), brdId, bullId, null);
		
		// 양식 로드
		//wfMdFormSvc.setFormByReleaseDt(request, model, null, "16", null);
		
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId"));
		if ("setMyBull".equals(setPage) || "setNewBull".equals(setPage)) {
			model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "brdId", "bullId"));
		} else {
			model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "bullId"));
		}
		
		model.put("isFrame", isFrame);
		
		// 윈도우 팝업 여부
		boolean isOpen=request.getRequestURI().indexOf("Open")>0;
		if(isFrame){ 
			return LayoutUtil.getJspPath("/bb/setBull", "Frm");
		}else if(isOpen){
			model.put("isOpen", isOpen);
			model.put("MOBILE_VIEW", Boolean.TRUE);
			return LayoutUtil.getJspPath("/bb/setBull", "Frm");
		}
		
		return LayoutUtil.getJspPath("/bb/setBull");
	}

	/** 게시물 저장 (사용자) */
	@RequestMapping(value = {"/bb/transBull", "/bb/transBullOpen"}, method = RequestMethod.POST)
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
			
			String compId   = ParamUtil.getRequestParam(request, "compId", false);
			
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
			
			if(compId!=null && !compId.isEmpty()) baBrdBVo.setCompId(compId);
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
 				//deletedFileList.add(bbBullPhotoSvc.savePhoto(request, storedBullId, "photo", queryQueue));
 				CommonFileVo photoVo=bbBullPhotoSvc.savePhoto(request, storedBullId, "photo", queryQueue);
				if(photoVo!=null) deletedFileList.add(photoVo);
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
			
			// SNS 저장
			if(baBrdBVo.getOptMap()!=null && baBrdBVo.getOptMap().get("snsUploadYn") !=null && "Y".equals(baBrdBVo.getOptMap().get("snsUploadYn")))			
				bbBullSvc.saveSnsList(request, queryQueue, brdId, storedBullId);
			
			commonSvc.execute(queryQueue);

			// 파일 삭제
			bbBullFileSvc.deleteDiskFiles(deletedFileList);
			
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
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
			commonSvc.execute(queryQueue);
			
			// pc 알림
			if("B".equals(bullStatCd))
				bbBullSvc.sendNoti(request, baBrdBVo, userVo, String.valueOf(storedBullId));
			
			// cm.msg.save.success=저장 되었습니다.
			String message=messageProperties.getMessage("cm.msg.save.success", request);
						
			boolean goView = false;
			// 수정이고, 게시예약일과 게시완료일 사이이고, 보안글이 아니면 조회화면으로 이동
			if (bullId != null && !bullId.isEmpty()) {
				goView = true;
				if ("R".equals(baBrdBVo.getKndCd())) {
					Date now = new Date();
					if (bullRezvDt != null && !bullRezvDt.isEmpty() && now.before(Timestamp.valueOf(bullRezvDt))) goView = false;
					if (bullExprDt != null && !bullExprDt.isEmpty() && now.after(Timestamp.valueOf(bullExprDt))) goView = false;
				}
				//String secuYn = ParamUtil.getRequestParam(request, "secuYn", false);
				/*if ("Y".equals(secuYn)) {
					goView = false;
				}*/
			}
			// 보내기번호 
	        String sendNo = ParamUtil.getRequestParam(request, "sendNo", false);
	        
	        // 이메일 번호 
	        String emailNo = ParamUtil.getRequestParam(request, "emailNo", false);
	        
	        // 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			// SNS 사용여부가 'Y' 일 경우 HTML 생성
			if(sysPlocMap!= null && sysPlocMap.containsKey("brdSnsEnable") && "Y".equals(sysPlocMap.get("brdSnsEnable"))){
				String subj = ParamUtil.getRequestParam(request, "subj", false);
				String cont = ParamUtil.getRequestParam(request, "cont", false);
				//if(!"80".equals(request.getServerPort())) link+=":"+request.getServerPort();
				emSnsSvc.createSnsFile(request, EmConstant.SNS_URL+"/bb", String.valueOf(storedBullId), subj, cont, null);
			}
	        
			if((sendNo==null || sendNo.isEmpty()) && (emailNo==null || emailNo.isEmpty())){
				// 윈도우 팝업 여부
				boolean isOpen=request.getRequestURI().indexOf("Open")>0;
				if(isOpen){
					// 모바일 URL
					Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
					String mobileDomain = svrEnvMap.get("mobileDomain");
					boolean useSSL = request.isSecure();
					if(mobileDomain!=null && !mobileDomain.isEmpty()){
						mobileDomain=(useSSL ? "https://" : "http://")+mobileDomain;
					}
					// 메세지를 msgId로 리턴
					String msgId=ptCmSvc.createSsoMsg(message);
					model.put("todo", "parent.reloadOpenHandler('"+mobileDomain+"', '"+msgId+"');");
					
				}else{
					model.put("message", message);
					if (goView) {
						/**
						 * 저장후 내용보기 페이지로 이동시 secu파라미터 존재시(보안글)
						 *  패스워드 확인페이지로 이동처리
						 */
						String menuId   = ParamUtil.getRequestParam(request, "menuId", false);
						if(viewPage.indexOf("secu") > 0)
							model.put("todo", "parent.location.replace('/bb/viewBullPw.do?menuId="+menuId+"&brdId="+brdId+"&bullId="+bullId+"&viewPage=viewBull.do');");	
						else
							model.put("todo", "parent.location.replace('" + viewPage + "');");
					} else {
						model.put("todo", "parent.location.replace('" + listPage + "');");
					}
				}
			}else{
				model.put("message", message);
				// 리턴 함수
		        String returnFunc = ParamUtil.getRequestParam(request, "returnFunc", false);
		        if(returnFunc!=null && !returnFunc.isEmpty())
		        	model.put("todo", "parent." + returnFunc + "();");
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
	
	/** [팝업] 보안글 인증 (사용자) */
	@RequestMapping(value = "/bb/viewBullPw")
	public String viewBullPw(HttpServletRequest request,
			ModelMap model) throws UnsupportedEncodingException {

		return LayoutUtil.getJspPath("/bb/viewBullPw");
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
		
		// 확장컬럼 코드 리스트 model에 추가
		bbBullSvc.putColmCdToModel(request, baColmDispDVoList, model, "Y");
				
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
		
		// action
		model.addAttribute("action", "transReply");
		model.addAttribute("listPage", "listBull");
		model.addAttribute("viewPage", "viewBull");
		model.addAttribute("bullRezvDtYn", true);       // 게시예약일 활성화여부
		model.addAttribute("bullRezvChecked", false);   // 게시예약일 체크박스 체크여부
		model.addAttribute("bbChoiYn", false);          // 게시판선택 표시여부
		model.addAttribute("tmpSaveYn", false);         // 임시저장 버튼 표시여부
		model.addAttribute("bbTgtDispYn", false);       // 게시대상 표시여부 (답변글이 아니고, 전사게시판이 아니고, 부서게시판이 아니면 표시)
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});

		// 최대 본문 사이즈 model에 추가
		bbBullSvc.putBodySizeToModel(request, model);
		
		// SNS 조회
		bbBullSvc.setSnsList(model, userVo.getCompId(), brdId, null, null);
				
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullPid"));
		model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "bullPid"));
		
		return LayoutUtil.getJspPath("/bb/setBull");
	}

	/** 게시물 답변 저장 (사용자) */
	@RequestMapping(value = "/bb/transReply", method = RequestMethod.POST)
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
			
			// SNS 저장
			if(baBrdBVo.getOptMap()!=null && baBrdBVo.getOptMap().get("snsUploadYn") !=null && "Y".equals(baBrdBVo.getOptMap().get("snsUploadYn")))			
				bbBullSvc.saveSnsList(request, queryQueue, brdId, newBullId);
						
			commonSvc.execute(queryQueue);
			
			// 파일 삭제
			bbBullFileSvc.deleteDiskFiles(deletedFileList);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
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

	/** [AJAX] 게시물복사 (사용자) */
	@RequestMapping(value = "/bb/transBullCopyAjx")
	public String transBullCopyAjx(HttpServletRequest request,
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
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			// 업무관리 사용여부가 'Y' 일 경우 양식 데이터 조회
			if(sysPlocMap.containsKey("wfEnable") && "Y".equals(sysPlocMap.get("wfEnable"))){
				// 양식 사용여부[본문삽입용]
				String wfFormNo=bbBrdSvc.getBrdToFormNo(baBrdBVo);
				
				// 양식 데이터 삭제
				if(wfFormNo!=null){
					String mdCd="BB";
					wfMdFormSvc.deleteMdByWorks(queryQueue, wfFormNo, mdCd, brdId, bullId);
				}
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

	/** [AJAX] 게시물 삭제 (사용자) */
	@RequestMapping(value = {"/bb/transBullDelAjxChk", "/bb/transRezvBullDelAjxChk"})
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
			LOGGER.error(e.getMessage(), e);
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
 			LOGGER.error(e.getMessage(), e);
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
 			LOGGER.error(e.getMessage(), e);
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
 			LOGGER.error(e.getMessage(), e);
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

		return LayoutUtil.getJspPath("/bb/viewScrePop");
	}

	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/bb/downFile","/cm/bb/downFile","/bb/preview/downFile"}, method = RequestMethod.POST)
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

	/** 본문저장 (사용자) */
	@RequestMapping(value = {"/bb/saveBody", "/bb/adm/saveBody"}, method = RequestMethod.POST)
	public ModelAndView saveBody(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "bullId", required = true) String bullId) throws Exception {

		try {
			if (brdId.isEmpty() || bullId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);

			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);

			// 게시물(BB_X000X_L) 테이블 - SELECT
			BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);
			StringBuilder sb = new StringBuilder();
			sb.append("<!doctype html> <html lang=\"ko\"> <head><meta charset=\"utf-8\"></head><body>");
			sb.append(bbBullLVo.getCont());
			sb.append("</body></html>");
			ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadBytes", sb.toString().getBytes("UTF-8"));
			mv.addObject("realFileName", baBrdBVo.getRescNm() + "_" + bbBullLVo.getBullId() + ".html");
			return mv;

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
	
	
	/** [AJAX] 게시판 이메일 초기정보 저장 (사용자) */
	@RequestMapping(value = {"/bb/transEmailAjx", "/bb/adm/transEmailAjx"})
	public String transBullEmailAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			String bullId = (String)object.get("bullId");
			String brdId = (String)object.get("brdId");
			JSONArray recvIds = (JSONArray) object.get("recvIds");
			if (bullId == null || brdId == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시판관리(BA_BRD_B) - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
			// 게시물(BB_X000X_L) 테이블 - SELECT
			BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);
			if (bbBullLVo == null) {
				// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("bb.msg.bullNotExists", request);
			}
			//model.put("bbBullLVo", bbBullLVo);
			
			// 양식번호 조회[게시판에서 양식을 사용할 경우]
			String wfFormNo=bbBrdSvc.getBrdToFormNo(baBrdBVo);
			
			// 시스템 설정
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			// 첫 줄 삽입여부
			boolean isFirstRow=sysPlocMap != null && sysPlocMap.containsKey("sendRowAddEnable") && "Y".equals(sysPlocMap.get("sendRowAddEnable"));						
			// 첫 줄 
			String cont=isFirstRow ? "<p><br/></p>" : "";
						
			// 양식 사용여부[본문삽입용]
			boolean isUseForm=wfFormNo!=null;
			if(isUseForm){
				String mdCd="BB";
				String mdRefId=brdId;
				String mdNo=bullId;
				String workNo=null;
				String colmTypCd="editor";
				List<String> editorList = wfMdFormSvc.getByMdWorksColmData(langTypCd, mdCd, wfFormNo, mdRefId, mdNo, workNo, colmTypCd);
				
				if(editorList!=null){
					for(String editor : editorList){
						cont+=editor;
					}
				}
			
			}
						
			// 이메일 초기 정보 저장
			QueryQueue queryQueue = new QueryQueue();
			
			//이메일 Vo[업무별 정보 세팅-제목,내용]
			CmEmailBVo cmEmailBVo = new CmEmailBVo();
			cmEmailBVo.setSubj(bbBullLVo.getSubj());
			
			if(!isUseForm && bbBullLVo.getCont()!=null){
				cont+=bbBullLVo.getCont();
			}
			if(!cont.isEmpty()) cmEmailBVo.setCont(cont);
			
			//cmEmailBVo.setCont(bbBullLVo.getCont());
			
			// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
			BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
			baBullFileDVo.setRefId(String.valueOf(bbBullLVo.getBullId()));
			@SuppressWarnings("unchecked")
			List<CommonFileVo> fileList = (List<CommonFileVo>) commonSvc.queryList(baBullFileDVo);
			
			//이메일 정보 저장
			Integer emailId = emailSvc.saveEmailInfo(cmEmailBVo , recvIds , fileList , queryQueue , userVo);
			//Integer emailId = bbBullSvc.saveBullLEmail(bbBullLVo,queryQueue , userVo);
			
			commonSvc.execute(queryQueue);
						
			//메세지 처리
			emailSvc.setEmailMessage(model, request, emailId);
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** 엑셀파일 다운로드 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/excelDownLoad", method = RequestMethod.POST)
	public ModelAndView excelDownLoad(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "ext", required = false) String ext,
			ModelMap model) throws Exception {
		
		try {
			if (brdId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			//시트명 세팅
			String rescNm = "";
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			List<BbBullLVo> list = new ArrayList<BbBullLVo>();
			// 컬럼 표시 여부(고정게시판은 임의로 생성한다.)
			List<BaColmDispDVo> baColmDispDVoList = null;
			if("new".equals(brdId) || "my".equals(brdId) || "disc".equals(brdId)){
				if("my".equals(brdId)) rescNm = messageProperties.getMessage("bb.jsp.listMyBull.title", request); 
				else if("disc".equals(brdId)) rescNm = messageProperties.getMessage("bb.jsp.listDiscBull.title", request);
				else rescNm = messageProperties.getMessage("bb.jsp.listNewBull.title", request);
				
				BaColmDispDVo baColmDispDVo = null;
				BaTblColmDVo colmVo = null;
				baColmDispDVoList = new ArrayList<BaColmDispDVo>();
				
				//보여줄 목록 정의
				String[][] columns = null;
				if("disc".equals(brdId)){
					columns = new String[][]{{"SUBJ","cols.subj"},{"BRD_NM","cols.regBb"},{"REGR_UID","cols.regr"},{"REG_DT","cols.regDt"}};
					// 게시상신함(BA_BULL_SUBM_L) 테이블 - BIND
					BaBullSubmLVo baBullSubmLVo = new BaBullSubmLVo();
					baBullSubmLVo.setQueryLang(langTypCd);
					//baBullSubmLVo.setPageRowCnt(pageRowCnt);
					String orderBy = "T.BULL_ID DESC";
					baBullSubmLVo.setOrderBy(orderBy);
					
					// 게시상신함(BA_BULL_SUBM_L) 테이블 - COUNT
					baBullSubmLVo.setDiscrUid(userVo.getUserUid());
					baBullSubmLVo.setBullStatCd("S");
					
					// 게시상신함(BA_BULL_SUBM_L) 테이블 - SELECT
					list = (List<BbBullLVo>) commonSvc.queryList(baBullSubmLVo);
					
				}else {
					columns = new String[][]{{"SUBJ","cols.subj"},{"BRD_NM","cols.bbNm"},{"REGR_UID","cols.regr"},{"REG_DT","cols.regDt"},{"READ_CNT","cols.readCnt"}};
					// 게시판ID 목록
					List<String> brdIdList = new ArrayList<String>();
					// 기한
					int ddln = 4;
					if("my".equals(brdId)){
						// 나의게시물설정(BA_MY_BULL_M) 테이블 - SELECT
						BaMyBullMVo paramMyBullVo = new BaMyBullMVo();
						paramMyBullVo.setUserUid(userVo.getUserUid());
						List<BaMyBullMVo> baMyBullMVoList = (List<BaMyBullMVo>) commonSvc.queryList(paramMyBullVo);
						
						for (BaMyBullMVo baMyBullMVo : baMyBullMVoList) {
							brdIdList.add(baMyBullMVo.getBrdId());
						}
						
						// 기한(BA_SETUP_B) 테이블 - SELECT
						BaUserSetupBVo paramUserSetupVo = new BaUserSetupBVo();
						paramUserSetupVo.setUserUid(userVo.getUserUid());
						BaUserSetupBVo baUserSetupBVo = (BaUserSetupBVo) commonSvc.queryVo(paramUserSetupVo);
						
						if (baUserSetupBVo != null && baUserSetupBVo.getDdln() != null) {
							ddln = baUserSetupBVo.getDdln();
						}
					}else{
						// 게시판관리(BA_BRD_B) 테이블 - SELECT
						BaBrdBVo baBrdBVo = new BaBrdBVo();
						baBrdBVo.setQueryLang(langTypCd);
						bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
						baBrdBVo.setWhereSqllet("AND T.LAST_BULL_YN = 'Y'");
						List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
						
						for (BaBrdBVo brdVo : baBrdBVoList) {
							brdIdList.add(brdVo.getBrdId());
						}
						
						// 기한(BA_SETUP_B) 테이블 - SELECT
						BaSetupBVo paramSetupVo = new BaSetupBVo();
						BaSetupBVo baSetupBVo = (BaSetupBVo) commonSvc.queryVo(paramSetupVo);
						
						if (baSetupBVo != null && baSetupBVo.getDdln() != null) {
							ddln = baSetupBVo.getDdln();
						}
					}
					// 최신게시물 목록 얻기
					list = bbBullSvc.getNewBullList(request, brdIdList, ddln);
				}
				
				for(String[] column : columns){
					baColmDispDVo = new BaColmDispDVo();
					baColmDispDVo.setListDispYn("Y");
					baColmDispDVo.setUseYn("Y");
					colmVo = new BaTblColmDVo();
					colmVo.setColmNm(column[0]);
					colmVo.setRescNm(messageProperties.getMessage(column[1], request));
					baColmDispDVo.setColmVo(colmVo);
					baColmDispDVoList.add(baColmDispDVo);
				}
			}else{
				// 게시판관리(BA_BRD_B) 테이블 - SELECT
				BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
				model.put("baBrdBVo", baBrdBVo);

				// 컬럼표시여부 리스트
				baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, true, null, "Y", null, false);
				
				// 확장컬럼 코드 리스트 model에 추가
				//bbBullSvc.putColmCdToModel(request, baColmDispDVoList, model, "Y");
				
				// 담당자 정보 세팅
				bbBrdSvc.setPichVo(baBrdBVo, langTypCd);
				
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
				}
				
				// 전사게시판이면
				if ("Y".equals(baBrdBVo.getAllCompYn())) {
					// 회사 ID 조회조건 추가[계열사 설정 확인]
					bbBullSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, paramBullVo);
					//paramBullVo.setCompId(null);
				} else {
					paramBullVo.setCompId(userVo.getCompId());
				}
				
				// 답변형인 경우
				String orderBy = "T.BULL_ID DESC";
				if ("Y".equals(baBrdBVo.getReplyYn())) {
					orderBy = "T.REPLY_GRP_ID DESC, T.REPLY_ORDR ASC";
				}
				paramBullVo.setOrderBy(orderBy);
				// 게시물 목록
				List<BbBullLVo> bbBullLVoList = bbBullSvc.getBbBullVoList(baBrdBVo, paramBullVo);
				
				// 컬럼표시여부 맵으로 세팅[목록표시여부'Y']
				Map<String, BaColmDispDVo> colmMap = bbBrdSvc.getBaColmDispDVoListMap(null, baColmDispDVoList, false, true, null, "Y");
						
				// json [사용자,조직] 데이터를 List Map 형태로 변환
				if(bbBullLVoList!=null && bbBullLVoList.size()>0)
					bbBullSvc.setColListJsonToMap(langTypCd, colmMap, bbBullLVoList, null);
				
				// 공지글 목록
				// 게시물(BB_X000X_L) 테이블 - SELECT
				paramBullVo.setNotcYn("Y");
				String strtYmd = StringUtil.afterDays(-baBrdBVo.getNotcDispPrd());
				paramBullVo.setStrtYmd(strtYmd);
				list = bbBullSvc.getBbBullVoList(baBrdBVo, paramBullVo);
				
				// json [사용자,조직] 데이터를 List Map 형태로 변환
				if(list!=null && list.size()>0)
					bbBullSvc.setColListJsonToMap(langTypCd, colmMap, list, null);
				
				list.addAll(bbBullLVoList);//공지글과 게시글 통합
				
				rescNm = baBrdBVo.getRescNm();
			}
			
			ModelAndView mv = new ModelAndView("excelDownloadView");
			if(list.size() == 0 ){
				mv = new ModelAndView("cm/result/commonResult");
				mv.addObject("message", messageProperties.getMessage("em.msg.noExcelData", request));
				return mv;
			}
			//컬럼명
			List<String> colNames = new ArrayList<String>();
			//데이터
			List<Object> colValue = null;
			Map<String,Object> colValues = new HashMap<String,Object>();
			Map<String,Object> voMap = null;
			BaTblColmDVo colmVo = null;
			List<BaCdDVo> baCdDVoList = null;
			
			String atrbId=null;
			BbBullLVo bbBullLVo;
			String returnValue, value;
			
			for(int i=0;i<list.size();i++){
				bbBullLVo=list.get(i);
				voMap = VoUtil.toMap(bbBullLVo, null);
				colValue = new ArrayList<Object>();
				for(BaColmDispDVo baColmDispDVo : baColmDispDVoList){
					if("Y".equals(baColmDispDVo.getListDispYn())){
						colmVo = baColmDispDVo.getColmVo();
						if( i == 0 )	colNames.add(colmVo.getRescNm()); //컬럼명 세팅
						returnValue="";
						atrbId=baColmDispDVo.getAtrbId();						
						value=(String)(voMap.get(atrbId)+"");
						if(value==null || value.isEmpty()){
							colValue.add(returnValue);
							continue;
						}
						if("subj".equals(atrbId)){
							if("Y".equals((String)voMap.get("ugntYn"))) returnValue += "["+messageProperties.getMessage("bb.option.ugnt", request)+"]";
							if("Y".equals((String)voMap.get("secuYn"))) returnValue += "["+messageProperties.getMessage("bb.option.secu", request)+"]";
							returnValue += value;
						}else if(colmVo.getColmTyp()!=null && "CALENDAR".equals(colmVo.getColmTyp())){
							returnValue=StringUtil.toShortDate(value);
						}else if(atrbId.endsWith("Dt") || (colmVo.getColmTyp()!=null && "CALENDARTIME".equals(colmVo.getColmTyp()))){
							returnValue=StringUtil.toLongDate(value);
						}else if(atrbId.endsWith("Cnt")){
							returnValue=StringUtil.toNumber(value);
						}else if(atrbId.endsWith("Uid")){
							atrbId=atrbId.replaceAll("Uid", "")+"Nm";
							returnValue=(String)voMap.get(atrbId);
						}else if(atrbId.endsWith("Id")){
							atrbId=atrbId.replaceAll("Id", "")+"Nm";
							returnValue=(String)voMap.get(atrbId);
						}else if("scre".equals(atrbId)){
							for(int j=0;j<5;j++){
								if((j+1)<=Integer.parseInt(value)) returnValue+="★";
								else returnValue+="☆";
							}
						}else{
							if(colmVo.getColmTyp()!=null && !colmVo.getColmTyp().isEmpty()){
								try{
									if(colmVo.getColmTyp().startsWith("CODE")){
										BaCdDVo baCdDVo = new BaCdDVo();
										baCdDVo.setQueryLang(langTypCd);
										baCdDVo.setCdGrpId(colmVo.getColmTypVal());
										baCdDVo.setUseYn("Y");
										baCdDVoList = (List<BaCdDVo>) commonSvc.queryList(baCdDVo);
										if(baCdDVoList!=null && baCdDVoList.size()>0){
											if("CODECHK".equals(colmVo.getColmTyp())){
												String[] values = value.split(",");
												if(values!=null){
													boolean first=false;
													for(BaCdDVo storedBaCdDVo : baCdDVoList){
														for(String va : values){
															if(storedBaCdDVo.getCdId().equals(va.trim())){
																if(first) returnValue+=", ";
																if(!first) first=true;
																returnValue+=storedBaCdDVo.getRescNm();
															}
														}										
													}
												}
											}else{
												for(BaCdDVo storedBaCdDVo : baCdDVoList){
													if(storedBaCdDVo.getCdId().equals(value.trim())){
														returnValue+=storedBaCdDVo.getRescNm();
														break;
													}										
												}
											}							
										}
									}else if("USER".equals(colmVo.getColmTyp()) || "DEPT".equals(colmVo.getColmTyp())){
										// json [사용자,조직] 데이터를 List Map 형태로 변환
										Map<String, Object> exColMap = bbBullLVo.getExColMap();
										if(exColMap!=null){
											List<Map<String,String>> dtlList = (List<Map<String,String>>)exColMap.get(atrbId+"MapList");
											if(dtlList!=null){
												int index=0;
												for(Map<String,String> dtlMap : dtlList){
													if(index>0) returnValue+=", ";
													returnValue+=dtlMap.get("rescNm");
													index++;
												}
											}
										}
									}else
										returnValue=value;
								}catch(SQLException sqle){
									sqle.printStackTrace();
								}
							}else{
								returnValue=value;
							}
						}
						//if(returnValue.isEmpty()) continue;
						colValue.add(returnValue);
						
					}
				}
				colValues.put("col"+i,colValue);//데이터 세팅
			}
			
			mv.addObject("sheetName", rescNm);//시트명
			mv.addObject("colNames", colNames);//컬럼명
			mv.addObject("colValues", colValues);//데이터
			mv.addObject("fileName", rescNm);//파일명
			mv.addObject("ext", ext == null ? "xlsx" : ext);//파일 확장자(없으면 xls)
			
			return mv;
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	/** 팝업설정 */
	@RequestMapping(value = "/bb/setPopupPop")
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
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 로그인팝업사용자 체크
		model.put("isLginPop", bbBullSvc.isLginPopChk(userVo));
				
		return LayoutUtil.getJspPath("/bb/setPopupPop");
	}
	
	/** 팝업설정 등록 수정 (저장) */
	@RequestMapping(value = "/bb/transSetPopup")
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
			
			// 게시판ID 
            String brdId = ParamUtil.getRequestParam(request, "brdId", false);
            
			// 게시글ID 
            String bullId = ParamUtil.getRequestParam(request, "bullId", false);
            
			// 로그인팝업 이력 삭제[로그인 팝업 데이터가 수정되면]
			if(brdId!=null && !brdId.isEmpty() && bullId!=null && !bullId.isEmpty()){
				BaBullPopupDispDVo baBullPopupDispDVo = new BaBullPopupDispDVo();
				baBullPopupDispDVo.setBrdId(brdId);
				baBullPopupDispDVo.setBullId(Integer.parseInt(bullId));
				if(commonSvc.count(baBullPopupDispDVo)>0){
					queryQueue.delete(baBullPopupDispDVo);
				}
			}
						
			commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("todo", "parent.location.replace('./viewBull.do?menuId=" + menuId+"&brdId="+baBullPopupDVo.getBrdId()+"&bullId="+baBullPopupDVo.getBullId()+"');");
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** 팝업 보기 */
	@RequestMapping(value = {"/bb/viewPopupPop", "/cm/viewBullPop"})
	public String viewPopupPop(HttpServletRequest request,
			@RequestParam(value = "bullId", required = true) String bullId,
			@RequestParam(value = "brdId", required = true) String brdId,
			ModelMap model) throws Exception {

		if (brdId.isEmpty() || bullId.isEmpty()) {
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 게시판관리(BA_BRD_B) - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		model.put("baBrdBVo", baBrdBVo);
		
		// 게시물(BB_X000X_L) 테이블 - SELECT
		BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);
		if (bbBullLVo == null) {
			// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("bb.msg.bullNotExists", request);
		}
		model.put("bbBullLVo", bbBullLVo);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 게시물첨부파일 리스트 model에 추가
		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());
		
		BaBullPopupDVo baBullPopupDVo =  new BaBullPopupDVo();
		baBullPopupDVo.setBrdId(brdId);
		baBullPopupDVo.setBullId(Integer.parseInt(bullId));
		baBullPopupDVo =  (BaBullPopupDVo) commonSvc.queryVo(baBullPopupDVo);
		model.put("baBullPopupDVo", baBullPopupDVo);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		// SNS 사용여부가 'N' 일 경우 return
		if(sysPlocMap.containsKey("brdSnsEnable") && "Y".equals(sysPlocMap.get("brdSnsEnable"))){
			BaSnsRVo baSnsRVo = null;
			if(brdId!=null && bullId!=null){
				baSnsRVo = new BaSnsRVo();
				baSnsRVo.setBrdId(brdId);
				baSnsRVo.setBullId(Integer.parseInt(bullId));
				baSnsRVo.setUseYn("Y");
				if(commonSvc.count(baSnsRVo)>0)
					model.put("isSns", Boolean.TRUE);
			}
		}
				
		return LayoutUtil.getJspPath("/bb/viewPopupPop");
	}
	
	/** [AJAX] 팝업출력사용자설정 */
	@RequestMapping(value = "/cm/transSetViewPopupDisp")
	public String transSetViewPopupDisp(HttpServletRequest request,
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
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			if(userVo==null){
				// pt.logout.timeout=로그인 세션이 종료 되었습니다.
				throw new CmException("pt.logout.timeout", request);
			}
			
			QueryQueue queryQueue = new QueryQueue();

			BaBullPopupDispDVo baBullPopupDispDVo = new BaBullPopupDispDVo();
			baBullPopupDispDVo.setBrdId(brdId);
			baBullPopupDispDVo.setBullId(Integer.parseInt(bullId));
			baBullPopupDispDVo.setUserUid(userVo.getUserUid());
			
			queryQueue.store(baBullPopupDispDVo);

			commonSvc.execute(queryQueue);

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
	
	/** 게시물을 문서로 보내기 (사용자) */
	@RequestMapping(value = "/bb/transSendDoc", method = RequestMethod.POST)
	public String transSendDoc(HttpServletRequest request,
			@RequestParam(value = "brdId", required = false) String brdId,
			@RequestParam(value = "bullId", required = false) String bullId,
			ModelMap model) {
		
		try {
			if (brdId == null || brdId.isEmpty() || bullId == null || bullId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 게시판관리(BA_BRD_B) - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			model.put("baBrdBVo", baBrdBVo);


			// 게시물(BB_X000X_L) 테이블 - SELECT
			BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd, true);
			if (bbBullLVo == null) {
				// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
				throw new CmException("bb.msg.bullNotExists", request);
			}
			
			// 문서매핑 맵
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("subj", bbBullLVo.getSubj());
			paramMap.put("cont", bbBullLVo.getCont());
			paramMap.put("fromTyp", "bb");
			
			// 파일목록
			List<CommonFileVo> fileVoList = bbBullFileSvc.getFileVoList(bullId);
			
			// 문서저장
			dmDocSvc.sendDoc(request, null, paramMap, fileVoList);
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace(parent.location.href);");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		} 
		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] - 로그인팝업 사용자 목록 */
	@RequestMapping(value = "/bb/adm/setLginPopUserPop")
	public String setLginPopUserPop(HttpServletRequest request,
			ModelMap model) throws SQLException {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 목록 조회
		List<OrUserBVo> orUserBVoList = bbBullSvc.getBaPopUserBVoList(langTypCd, userVo.getCompId());
		if(orUserBVoList==null) orUserBVoList = new ArrayList<OrUserBVo>();
		orUserBVoList.add(new OrUserBVo());
		
		model.put("orUserBVoList", orUserBVoList);
		
		return LayoutUtil.getJspPath("/bb/adm/setLginPopUserPop");
	}
	
	/** [히든프레임] 카테고리 일괄 저장(왼쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/transLginPopUser")
	public String transLginPopUser(HttpServletRequest request,			
			@Parameter(name="delList", required=false) String delList,
			ModelMap model) throws Exception {
		
		try{
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 팝업사용자(BA_POP_USER_B) 테이블 VO
			BaPopUserBVo baPopUserBVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String userUid;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			
			for(i=0;i<delCds.length;i++){
				userUid = delCds[i];
				
				// 기본 테이블 - 삭제
				baPopUserBVo = new BaPopUserBVo();
				baPopUserBVo.setUserUid(userUid);
				baPopUserBVo.setCompId(userVo.getCompId());
				queryQueue.delete(baPopUserBVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			// 팝업사용자(BA_POP_USER_B) 테이블
			List<BaPopUserBVo> baPopUserBVoList = (List<BaPopUserBVo>)VoUtil.bindList(request, BaPopUserBVo.class, new String[]{"userUid"});
			size = baPopUserBVoList==null ? 0 : baPopUserBVoList.size();
			for(i=0;i<size;i++){
				baPopUserBVo = baPopUserBVoList.get(i);
				baPopUserBVo.setCompId(userVo.getCompId());
				queryQueue.store(baPopUserBVo);
			}
			
			if(!queryQueue.isEmpty()) commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.dialog.close('setLginPopUserDialog');");
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
	/** 게시물 목록 (법인별) */
	@RequestMapping(value = "/bb/listCoprBull")
	public String listCoprBull(HttpServletRequest request,
			@RequestParam(value = "compId", required = false) String compId,
			@RequestParam(value = "brdId", required = false) String brdId,
			@RequestParam(value = "listTyp", required = false) String listTyp,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 회사 목록 조회
		model.put("ptCompBVoList", bbBrdSvc.getCompList(langTypCd));
					
		if(compId==null || compId.isEmpty()){
			compId=userVo.getCompId();
		}
		if(compId!=null && !compId.isEmpty()){
			// 게시판 목록 조회
			model.put("baBrdBVoList", bbBrdSvc.getCompBrdList(langTypCd, compId, "N", "N", "N", true));
		}
		model.put("paramCompId", compId);
		if(brdId==null || brdId.isEmpty()){
			return LayoutUtil.getJspPath("/bb/listBull");
		}
		
		return this.listBull(request, brdId, listTyp, compId, model);
	}
	
	/** [AJAX] - 보안글 확인 (ID/PASS) */
	@RequestMapping(value = "/bb/chkValidBullAjx")
	public String chkValidBullAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String secu = (String) object.get("secu");
			
			if (secu==null || secu.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 비밀번호 확인
			if (!bbCmSvc.isValidUserPw(request, secu)) {
				// pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				throw new CmException("pt.login.noUserNoPw", request);
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
			/*if(baBrdBVo.getBrdTypCd()!=null && "F".equals(baBrdBVo.getBrdTypCd()) && 
					baBrdBVo.getOptMap().get("fileUploadYn")!=null && "Y".equals(baBrdBVo.getOptMap().get("fileUploadYn"))){
				List<CommonFileVo> fileVoList = bbBullFileSvc.getFileVoList(bullId);
				model.put("fileVoList", fileVoList);
			}*/
			
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** 게시물 조회 - FAQ */
	@RequestMapping(value = "/bb/viewFaq")
	public String viewFaq(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
			@RequestParam(value = "bullId", required = true) String bullId,
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
		
		// 컬럼표시여부 맵으로 세팅
		bbBrdSvc.getBaColmDispDVoListMap(model, baColmDispDVoList, true, false, "Y", null);

		// 게시물(BB_X000X_L) 테이블 - SELECT
		BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd, false);
		if (bbBullLVo == null) {
			// bb.msg.bullNotExists=게시물이 존재하지 않습니다.
			throw new CmException("bb.msg.bullNotExists", request);
		}
		model.put("bbBullLVo", bbBullLVo);
		
		// 전사게시판이면서 게시글의 회사ID와 사용자 회사ID가 같은지 비교
		if("Y".equals(baBrdBVo.getAllCompYn()) && !userVo.getCompId().equals(bbBullLVo.getCompId())){
			model.put("eqCompYn", "N");
		}
				
		// 게시물첨부파일 리스트 model에 추가
		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());

		// 포토게시판여부
		if ("Y".equals(baBrdBVo.getPhotoYn())) {
			// 게시물사진 세팅
			bbBullLVo.setPhotoVo(bbBullPhotoSvc.getPhotoVo(Integer.valueOf(bullId)));
		}
		
		// 기타(점수, 추천, 찬반투표) 참가여부 model에 추가
		bbBullSvc.putEtcToModel(request, baBrdBVo, bullId, model);
		
		// listPage
		model.addAttribute("listPage", "listBull");
		model.addAttribute("viewPage", "viewBull");
		model.addAttribute("setPage", "setBull");
		model.addAttribute("delAction", "transBullDelAjx");
		model.addAttribute("brdId", brdId);
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		// print css 적용
		if(request.getAttribute("printView")==null){
			request.setAttribute("printView", "print100");
		}
		
		// SNS 조회
		//bbBullSvc.setSnsList(model, userVo.getCompId(), brdId, bullId, "Y");
		
		return LayoutUtil.getJspPath("/bb/viewFaq", "Frm");
	}
	
	
}
