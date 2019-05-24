package com.innobiz.orange.mobile.bb.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullFileSvc;
import com.innobiz.orange.web.bb.svc.BbBullPhotoSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.svc.BbBullTgtSvc;
import com.innobiz.orange.web.bb.svc.BbCmSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaCmtDVo;
import com.innobiz.orange.web.bb.vo.BaColmDispDVo;
import com.innobiz.orange.web.bb.vo.BaMyBullMVo;
import com.innobiz.orange.web.bb.vo.BaSetupBVo;
import com.innobiz.orange.web.bb.vo.BaUserSetupBVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.dao.LobHandler;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

/* 최신게시물, 나의게시물 */
@Controller
public class MoBbBullNewCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 게시판관리 서비스 */
	@Resource(name = "bbBrdSvc")
	private BbBrdSvc bbBrdSvc;
	
	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;
	
	/** 게시판 공통 서비스 */
	@Autowired
	private BbCmSvc bbCmSvc;
	
	/** 게시대상 서비스 */
	@Resource(name = "bbBullTgtSvc")
	public BbBullTgtSvc bbBullTgtSvc;

	/** 게시물사진 서비스 */
	@Resource(name = "bbBullPhotoSvc")
	private BbBullPhotoSvc bbBullPhotoSvc;
	
	/** 게시파일 서비스 */
	@Resource(name = "bbBullFileSvc")
	private BbBullFileSvc bbBullFileSvc;
	
	/** CLOB, BLOB 데이터 핸들링 - BIG SIZE */
	@Resource(name = "lobHandler")
	private LobHandler lobHandler;
	
	/** 포털 보안 서비스(레이아웃 포함) */
	@Autowired
	private PtSecuSvc ptSecuSvc; 
	
	/** 최신게시물 목록 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/listNewBull")
	public String listNewBull(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 모듈 별 권한 있는 모듈참조ID 목록
		List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "BB", "R");
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
		baBrdBVo.setWhereSqllet("AND T.LAST_BULL_YN = 'Y'");
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
		
		// 게시판ID 목록
		List<String> brdIdList = new ArrayList<String>();
		for (BaBrdBVo brdVo : baBrdBVoList) {
			if(!bbBrdSvc.chkMdIds(mdIds, brdVo.getBrdId()))	continue;
			brdIdList.add(brdVo.getBrdId());
		}
		
		// 기한(BA_SETUP_B) 테이블 - SELECT
		BaSetupBVo paramSetupVo = new BaSetupBVo();
		BaSetupBVo baSetupBVo = (BaSetupBVo) commonSvc.queryVo(paramSetupVo);
		
		// 기한
		int ddln = 4;
		if (baSetupBVo != null && baSetupBVo.getDdln() != null) {
			ddln = baSetupBVo.getDdln();
		}
		
		// 최신게시물 목록 얻기
		List<BbBullLVo> bbBullLVoList = bbBullSvc.getNewBullList(request, brdIdList, ddln);
		
		BbBullLVo paramBullVo = new BbBullLVo();
		Integer recodeCount = bbBullLVoList.size();
		PersonalUtil.setPaging(request, paramBullVo, recodeCount);
		
		int fromIndex = (paramBullVo.getPageNo() - 1) * paramBullVo.getPageRowCnt();
		int toIndex = paramBullVo.getPageNo() * paramBullVo.getPageRowCnt();
		toIndex = toIndex < recodeCount ? toIndex : recodeCount;
		
		model.put("recodeCount", recodeCount);
		model.put("bbBullLVoList", bbBullLVoList.subList(fromIndex, toIndex));
		
		model.put("listPage", "listNewBull");
		model.put("viewPage", "viewNewBull");
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId", "pw", "noCache", "secu"));
		
		return MoLayoutUtil.getJspPath("/bb/listNewBull");
	}
	
	/** 최신게시물 조회 (사용자) */
	@RequestMapping(value = "/bb/viewNewBull")
	public String viewNewBull(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
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

		// 보안글이면
		if ("Y".equals(bbBullLVo.getSecuYn())) {
			if (secu == null || secu.isEmpty()) {
				return MoLayoutUtil.getJspPath("/bb/setLoginCfrm");
			}
			// 비밀번호 확인
			if (!bbCmSvc.isValidUserPw(request, secu)) {
				// pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				throw new CmException("pt.login.noUserNoPw", request);
			}
		}
		
		// 부서게시판이면
		if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
			bbCmSvc.checkUserDeptAuth(request, bbBullLVo.getDeptId());
		}
		
		// 게시대상 model에 추가
		bbBullTgtSvc.putTgtListToModel(bbBullLVo, model);

		// 게시대상에 포함되는가?
		bbBullTgtSvc.checkTgt(request, bbBullLVo, model);
		
		// json [사용자,조직] 데이터를 List Map 형태로 변환
		bbBullSvc.setColListJsonToMap(langTypCd, colmMap, null, bbBullLVo);
				
		// 이전글/다음글
		userVo.setLangTypCd(langTypCd);
		bbBullSvc.setNewBullList(request, model, userVo , bullId );
		
		// 게시물첨부파일 리스트 model에 추가
		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());
				
		// 포토게시판여부
		if ("Y".equals(baBrdBVo.getPhotoYn())) {
			// 게시물사진 세팅
			bbBullLVo.setPhotoVo(bbBullPhotoSvc.getPhotoVo(Integer.valueOf(bullId)));
		}
		
		// 기타(점수, 추천, 찬반투표) 참가여부 model에 추가
		bbBullSvc.putEtcToModel(request, baBrdBVo, bullId, model);

		// 조회이력 저장
		if (bbBullSvc.saveReadHst(bullId, userVo.getUserUid())) {
			// 조회수 증가
			bbBullSvc.addReadCnt(baBrdBVo, Integer.parseInt(bullId));
		}
		
		
		// 한줄답변(BA_CMT_D) 테이블 - BIND
		BaCmtDVo baCmtDVo = new BaCmtDVo();
		VoUtil.bind(request, baCmtDVo);
		
		// 한줄답변(BA_CMT_D) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(baCmtDVo);
		model.addAttribute("recodeCount", recodeCount);
		
		// 한줄답변(BA_CMT_D) 테이블 - SELECT
		@SuppressWarnings("unchecked")
		List<BaCmtDVo> baCmtDVoList = (List<BaCmtDVo>) commonSvc.queryList(baCmtDVo);
		model.addAttribute("baCmtDVoList", baCmtDVoList);
		
		model.put("listPage", "listNewBull");
		model.addAttribute("viewPage", "viewNewBull");
		model.addAttribute("setPage", "setNewBull");
		model.addAttribute("delAction", "transBullDelAjx");
		
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId", "pw", "noCache", "secu"));
		model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "brdId", "bullId", "pw", "noCache", "secu"));
		
		model.addAttribute("prevNextYn", true);                                                // 이전다음 버튼 표시여부
		//model.addAttribute("etcDIspYn", true);                                                  // 기타(점수, 추천, 찬반투표) 표시여부
				
		return MoLayoutUtil.getJspPath("/bb/viewBull");
	}
	

	/** 나의게시물 목록 (사용자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/listMyBull")
	public String listMyBull(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 모듈 별 권한 있는 모듈참조ID 목록
		List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "BB", "R");
				
		// 나의게시물설정(BA_MY_BULL_M) 테이블 - SELECT
		BaMyBullMVo paramMyBullVo = new BaMyBullMVo();
		paramMyBullVo.setUserUid(userVo.getUserUid());
		List<BaMyBullMVo> baMyBullMVoList = (List<BaMyBullMVo>) commonSvc.queryList(paramMyBullVo);
		
		// 게시판ID 목록
		List<String> brdIdList = new ArrayList<String>();
		for (BaMyBullMVo baMyBullMVo : baMyBullMVoList) {
			if(!bbBrdSvc.chkMdIds(mdIds, baMyBullMVo.getBrdId())) continue;
			brdIdList.add(baMyBullMVo.getBrdId());
		}
		
		// 기한(BA_SETUP_B) 테이블 - SELECT
		BaUserSetupBVo paramUserSetupVo = new BaUserSetupBVo();
		paramUserSetupVo.setUserUid(userVo.getUserUid());
		BaUserSetupBVo baUserSetupBVo = (BaUserSetupBVo) commonSvc.queryVo(paramUserSetupVo);
		
		// 기한
		int ddln = 4;
		if (baUserSetupBVo != null && baUserSetupBVo.getDdln() != null) {
			ddln = baUserSetupBVo.getDdln();
		}
		
		// 최신게시물 목록 얻기
		List<BbBullLVo> bbBullLVoList = bbBullSvc.getNewBullList(request, brdIdList, ddln);
		
		BbBullLVo paramBullVo = new BbBullLVo();
		Integer recodeCount = bbBullLVoList.size();
		PersonalUtil.setPaging(request, paramBullVo, recodeCount);
		
		int fromIndex = (paramBullVo.getPageNo() - 1) * paramBullVo.getPageRowCnt();
		int toIndex = paramBullVo.getPageNo() * paramBullVo.getPageRowCnt();
		toIndex = toIndex < recodeCount ? toIndex : recodeCount;
		
		model.put("recodeCount", recodeCount);
		model.put("bbBullLVoList", bbBullLVoList.subList(fromIndex, toIndex));
	
		model.put("listPage", "listMyBull");
		model.put("viewPage", "viewMyBull");
		
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId", "pw", "noCache", "secu"));
				
		return MoLayoutUtil.getJspPath("/bb/listNewBull");
	}

	/** 나의게시물 조회 (사용자) */
	@RequestMapping(value = "/bb/viewMyBull")
	public String viewMyBull(HttpServletRequest request,
			@RequestParam(value = "brdId", required = true) String brdId,
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
		
		// 게시판관리(BA_BRD_B) - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		model.put("baBrdBVo", baBrdBVo);
		
		// 컬럼표시여부 리스트
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, true, null, null, null, false);
		model.put("baColmDispDVoList", baColmDispDVoList);

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

		// 보안글이면
		if ("Y".equals(bbBullLVo.getSecuYn())) {
			if (secu == null || secu.isEmpty()) {
				return MoLayoutUtil.getJspPath("/bb/setLoginCfrm");
			}
			// 비밀번호 확인
			if (!bbCmSvc.isValidUserPw(request, secu)) {
				// pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				throw new CmException("pt.login.noUserNoPw", request);
			}
		}
		
		// 부서게시판이면
		if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
			bbCmSvc.checkUserDeptAuth(request, bbBullLVo.getDeptId());
		}
		
		// 게시대상 model에 추가
		bbBullTgtSvc.putTgtListToModel(bbBullLVo, model);

		// 게시대상에 포함되는가?
		bbBullTgtSvc.checkTgt(request, bbBullLVo, model);
		
		// 조회이력 저장
		if (bbBullSvc.saveReadHst(bullId, userVo.getUserUid())) {
			// 조회수 증가
			bbBullSvc.addReadCnt(baBrdBVo, Integer.parseInt(bullId));
		}
		
		// 이전글/다음글
		bbBullSvc.setMyBullList(request, model, userVo , bullId );

		// 게시물첨부파일 리스트 model에 추가
		bbBullFileSvc.putFileListToModel(bullId, model, userVo.getCompId());
		
		// 포토게시판여부
		if ("Y".equals(baBrdBVo.getPhotoYn())) {
			// 게시물사진 세팅
			bbBullLVo.setPhotoVo(bbBullPhotoSvc.getPhotoVo(Integer.valueOf(bullId)));
		}

		// 기타(점수, 추천, 찬반투표) 참가여부 model에 추가
		bbBullSvc.putEtcToModel(request, baBrdBVo, bullId, model);
		
		
		// 한줄답변(BA_CMT_D) 테이블 - BIND
		BaCmtDVo baCmtDVo = new BaCmtDVo();
		VoUtil.bind(request, baCmtDVo);
		
		// 한줄답변(BA_CMT_D) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(baCmtDVo);
		model.addAttribute("recodeCount", recodeCount);
		
		// 한줄답변(BA_CMT_D) 테이블 - SELECT
		@SuppressWarnings("unchecked")
		List<BaCmtDVo> baCmtDVoList = (List<BaCmtDVo>) commonSvc.queryList(baCmtDVo);
		model.addAttribute("baCmtDVoList", baCmtDVoList);
		
		model.put("listPage", "listMyBull");
		model.addAttribute("viewPage", "viewMyBull");
		model.addAttribute("setPage", "setMyBull");
		
		// params
		model.addAttribute("params", ParamUtil.getQueryString(request, "bullId", "pw", "noCache", "secu"));
		model.addAttribute("paramsForList", ParamUtil.getQueryString(request, "brdId", "bullId", "pw", "noCache", "secu"));
		
		model.addAttribute("prevNextYn", true);             // 이전다음 버튼 표시여부
		//model.addAttribute("etcDIspYn", true);                                                  // 기타(점수, 추천, 찬반투표) 표시여부
		
		return MoLayoutUtil.getJspPath("/bb/viewBull");
	}
}
