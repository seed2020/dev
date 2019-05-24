package com.innobiz.orange.web.bb.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaBullSubmLVo;
import com.innobiz.orange.web.bb.vo.BaCatDVo;
import com.innobiz.orange.web.bb.vo.BaCdDVo;
import com.innobiz.orange.web.bb.vo.BaCdGrpBVo;
import com.innobiz.orange.web.bb.vo.BaColmDispDVo;
import com.innobiz.orange.web.bb.vo.BaMyBullMVo;
import com.innobiz.orange.web.bb.vo.BaPopUserBVo;
import com.innobiz.orange.web.bb.vo.BaReadHstLVo;
import com.innobiz.orange.web.bb.vo.BaRezvSaveLVo;
import com.innobiz.orange.web.bb.vo.BaSnsRVo;
import com.innobiz.orange.web.bb.vo.BaTblColmDVo;
import com.innobiz.orange.web.bb.vo.BaTmpSaveLVo;
import com.innobiz.orange.web.bb.vo.BaUserSetupBVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmSrchSvc;
import com.innobiz.orange.web.em.utils.EmConstant;
import com.innobiz.orange.web.em.vo.CmSrchBVo;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOrgHstRVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.svc.PtWebPushMsgSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtPushMsgDVo;

/** 게시물 서비스 */
@Service
public class BbBullSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BbBullSvc.class);

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 게시판 공통 서비스 */
	@Autowired
	private BbCmSvc bbCmSvc;

	/** 테이블관리 서비스 */
	@Resource(name = "bbTblSvc")
	private BbTblSvc bbTblSvc;

	/** 게시판관리 서비스 */
	@Resource(name = "bbBrdSvc")
	private BbBrdSvc bbBrdSvc;

	/** 게시대상 서비스 */
	@Resource(name = "bbBullTgtSvc")
	public BbBullTgtSvc bbBullTgtSvc;

	/** 게시옵션 서비스 */
	@Resource(name = "bbBullOptSvc")
	private BbBullOptSvc bbBullOptSvc;

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
	
	/** 검색 서비스 */
	@Autowired
	private EmSrchSvc emSrchSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** PC 알림 발송 서비스 */
	@Resource(name = "ptWebPushMsgSvc")
	private PtWebPushMsgSvc ptWebPushMsgSvc;
	
	/** 게시물(BB_X000X_L) 테이블 VO 생성 */
	public BbBullLVo newBbBullLVo(BaBrdBVo baBrdBVo) {
		return newBbBullLVo(baBrdBVo, false);
	}

	/** 게시물(BB_X000X_L) 테이블 VO 생성 */
	public BbBullLVo newBbBullLVo(BaBrdBVo baBrdBVo, boolean isKndCdRequired) {
		// SELECT 실행시 게시판종류(영구/유효기간)에 따른 WHERE절 검색조건이 필요한 경우 kndCd를 세팅해야 함.
		String kndCd = isKndCdRequired ? baBrdBVo.getKndCd() : null;
		return new BbBullLVo(bbTblSvc.getFullTblNm(baBrdBVo.getTblNm()), kndCd);
	}

	/** 게시물 VO 얻기 */
	public BbBullLVo getBbBullLVo(BaBrdBVo baBrdBVo, int bullId, String langTypCd) throws SQLException {
		// 게시물(BB_X000X_L) 테이블 - SELECT
		BbBullLVo bbBullLVo = newBbBullLVo(baBrdBVo, true);
		bbBullLVo.setQueryLang(langTypCd);
		bbBullLVo.setBullStatCd(null);
		bbBullLVo.setBrdId(baBrdBVo.getBrdId());
		bbBullLVo.setBullId(bullId);
		// 확장컬럼명 목록 세팅
		setExColmNmList(baBrdBVo, bbBullLVo);
		//bbBullLVo.setWithLob(true);
		
		return (BbBullLVo) commonDao.queryVo(bbBullLVo);
	}
	
	/** 게시물 VO 얻기 [lobHandler- withLob] */
	public BbBullLVo getBbBullLVo(BaBrdBVo baBrdBVo, int bullId, String langTypCd, boolean withLob) throws SQLException {
		// 게시물(BB_X000X_L) 테이블 - SELECT
		BbBullLVo bbBullLVo = newBbBullLVo(baBrdBVo, true);
		bbBullLVo.setQueryLang(langTypCd);
		bbBullLVo.setBullStatCd(null);
		bbBullLVo.setBrdId(baBrdBVo.getBrdId());
		bbBullLVo.setBullId(bullId);
		// 확장컬럼명 목록 세팅
		setExColmNmList(baBrdBVo, bbBullLVo);
		//bbBullLVo.setWithLob(true);
		bbBullLVo.setWithLob(withLob);
		
		return (BbBullLVo) commonDao.queryVo(bbBullLVo);
	}

	/** 확장컬럼명 목록 세팅 */
	@SuppressWarnings("unchecked")
	private void setExColmNmList(BaBrdBVo baBrdBVo, BbBullLVo bbBullLVo) throws SQLException {
		// 확장컬럼명 목록 세팅
		if ("Y".equals(baBrdBVo.getExYn())) {
			// 테이블컬럼(BA_TBL_COLM_D) 테이블 - SELECT
			BaTblColmDVo baTblColmDVo = new BaTblColmDVo();
			baTblColmDVo.setTblId(baBrdBVo.getTblId());
			baTblColmDVo.setExColmYn("Y");
			List<BaTblColmDVo> baTblColmDVoList = (List<BaTblColmDVo>) commonDao.queryList(baTblColmDVo);

			List<String> exColmNmList = new ArrayList<String>();
			for (BaTblColmDVo colmVo : baTblColmDVoList) {
				exColmNmList.add(colmVo.getColmNm());
			}
			bbBullLVo.setExColmNmList(exColmNmList);
		}
	}

	/** 확장컬럼 코드 리스트 model에 추가 */
	@SuppressWarnings("unchecked")
	public void putColmCdToModel(HttpServletRequest request, List<BaColmDispDVo> baColmDispDVoList, ModelMap model, String useYn) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		ArrayList<List<BaCdDVo>> cdList = new ArrayList<List<BaCdDVo>>();
		//ArrayList<List<PtCdBVo>> cdList = new ArrayList<List<PtCdBVo>>();
		for (BaColmDispDVo baColmDispDVo : baColmDispDVoList) {
			BaTblColmDVo colmVo = baColmDispDVo.getColmVo();
			if ("Y".equals(colmVo.getExColmYn())) {
				if (colmVo.getColmTyp().startsWith("CODE")) {
					BaCdDVo baCdDVo = new BaCdDVo();
					baCdDVo.setQueryLang(langTypCd);
					baCdDVo.setCdGrpId(colmVo.getColmTypVal());
					if(useYn!=null) baCdDVo.setUseYn(useYn);
					cdList.add((List<BaCdDVo>) commonDao.queryList(baCdDVo));
					//cdList.add(ptCmSvc.getCdList(colmVo.getColmTypVal(), langTypCd, "Y"));
				}
			}
		}
		model.put("cdList", cdList.toArray());
	}

	/** 게시물 목록 리턴 */
	public List<BbBullLVo> getBbBullVoList(BaBrdBVo baBrdBVo, BbBullLVo paramBullVo) throws SQLException {
		// 확장컬럼명 목록 세팅
		setExColmNmList(baBrdBVo, paramBullVo);
		// 게시물(BB_X000X_L) 테이블 - SELECT
		@SuppressWarnings("unchecked")
		List<BbBullLVo> bbBullLVoList = (List<BbBullLVo>) commonDao.queryList(paramBullVo);
		// 첨부파일 수 조회
		bbBullFileSvc.setFileCount(bbBullLVoList);
		// 한줄답변 수 조회
		bbCmSvc.setCmtCount(bbBullLVoList);
		return bbBullLVoList;
	}

	/** 조회이력 저장 */
	public boolean saveReadHst(String bullId, String userUid) throws SQLException {
		if("U0000001".equals(userUid)) return false;
		// 조회이력(BA_READ_HST_L) 테이블 - INSERT OR UPDATE
		BaReadHstLVo baReadHstLVo = new BaReadHstLVo();
		baReadHstLVo.setBullId(Integer.parseInt(bullId));
		baReadHstLVo.setUserUid(userUid);
		boolean isFirst = commonDao.update(baReadHstLVo) == 0;
		if (isFirst) {
			commonDao.insert(baReadHstLVo);
		}
		return isFirst;
	}

	/** 조회수 증가 */
	public void addReadCnt(BaBrdBVo baBrdBVo, Integer bullId) throws SQLException {
		// 게시물(BB_X000X_L) 테이블 - UPDATE
		BbBullLVo bbBullLVo = newBbBullLVo(baBrdBVo);
		bbBullLVo.setBullId(bullId);
		bbBullLVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.updateReadCnt");
		commonDao.update(bbBullLVo);
	}

	/** 게시물 복사 */
	public int copyBull(HttpServletRequest request, BaBrdBVo srcBrdVo, JSONArray bullIdsArray, JSONArray brdIdsArray,
			QueryQueue queryQueue) throws SQLException, IOException, CmException {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		int copyCnt = 0;
		for (int i = 0; i < bullIdsArray.size(); i++) {
			// 원본 게시물ID
			String bullId = (String) bullIdsArray.get(i);
			
			// 게시물(BB_X000X_L) 테이블 - SELECT
			BbBullLVo srcBullVo = getBbBullLVo(srcBrdVo, Integer.valueOf(bullId), langTypCd);
			
			// 답변글은 제외
			if (srcBullVo.getReplyDpth() != null && srcBullVo.getReplyDpth() > 0) continue;
			
			for (int k = 0; k < brdIdsArray.size(); k++) {
				// 대상 게시판ID
				String targetBrdId = (String) brdIdsArray.get(k);
				if (targetBrdId.equals(srcBrdVo.getBrdId())) {
					continue;
				}
				
				// 대상 게시판 VO
				BaBrdBVo targetBrdVo = bbBrdSvc.getBaBrdBVo("ko", targetBrdId);
				
				// 속성 복사
				BbBullLVo targetBullVo = newBbBullLVo(targetBrdVo);
				String[] ignores = new String[] { "bullId", "brdId", "tableName", "replyGrpId", "regrUid", "regDt", "modrUid", "modDt" };
				BeanUtils.copyProperties(srcBullVo, targetBullVo, ignores);
				
				// 게시물(BB_X000X_L) 테이블 - INSERT
				targetBullVo.setBrdId(targetBrdId);
				targetBullVo.setBullId(bbCmSvc.createBullId());
				//if ("Y".equals(targetBrdVo.getReplyYn())) {
				targetBullVo.setReplyGrpId(targetBullVo.getBullId());
				//}
				
				// 복사대상 게시판글에 부서ID가 있으면...
				/*if(srcBullVo.getDeptId() != null && !srcBullVo.getDeptId().isEmpty()){
					targetBullVo.setDeptId(userVo.getDeptId());
				}*/
				if("Y".equals(targetBrdVo.getDeptBrdYn())) {// 대상 게시판이 부서게시판이면...
					targetBullVo.setDeptId(userVo.getDeptId());
				}
				
				targetBullVo.setCompId(userVo.getCompId());
				targetBullVo.setModrUid(userVo.getUserUid());
				targetBullVo.setModDt("sysdate");
				targetBullVo.setReadCnt(0);
				targetBullVo.setProsCnt(0);
				targetBullVo.setConsCnt(0);
				targetBullVo.setRecmdCnt(0);
				targetBullVo.setScre(0);
				queryQueue.insert(targetBullVo);

				// 파일 복사
				//if (srcBullVo.getFileCnt() !=null && srcBullVo.getFileCnt() > 0) {
					bbBullFileSvc.copyBullFile(request, String.valueOf(srcBullVo.getBullId()), String.valueOf(targetBullVo.getBullId()), queryQueue);
				//}

				// 포토게시판이면
				if ("Y".equals(srcBrdVo.getPhotoYn())) {
					// 게시물사진 저장
					bbBullPhotoSvc.copyPhoto(request, srcBullVo.getBullId(), targetBullVo.getBullId(), queryQueue);
				}
				// 검색엔진 인덱싱 처리
				if("B".equals(targetBullVo.getBullStatCd()))
					addSrchIndex(targetBullVo, userVo, queryQueue, "I");
				
				copyCnt++;
			}
		}
		return copyCnt;
	}

	/** 하위 게시물 조회 */
	public List<BbBullLVo> getChildBullList(BbBullLVo paramBullVo , Integer bullId , List<BbBullLVo> bbBullLVoList) throws SQLException {
		paramBullVo.setBullPid(bullId);
		@SuppressWarnings("unchecked")
		List<BbBullLVo> childList = (List<BbBullLVo>) commonDao.queryList(paramBullVo);
		if(childList != null){// 하위 게시물이 있을경우
			for(BbBullLVo storedBbBullLVo : childList){
				bbBullLVoList.add(storedBbBullLVo);
				getChildBullList(paramBullVo, storedBbBullLVo.getBullId(), bbBullLVoList);
			}
		}
		return bbBullLVoList;
	}
	
	/** 게시물 삭제 */
	public void deleteBull(BaBrdBVo baBrdBVo, Integer bullId, QueryQueue queryQueue) {
		// 게시옵션 삭제
		bbBullOptSvc.deleteBullOpt(bullId, queryQueue);

		// 게시대상 삭제
		bbBullTgtSvc.deleteBullTgt(bullId, queryQueue);
		
		//조회이력 삭제 - 해야할까?
		//한줄답변 삭제 - 해야할까?
		
		// 게시물(BB_X000X_L) 테이블 - DELETE
		BbBullLVo paramBbBullLVo = newBbBullLVo(baBrdBVo);
		paramBbBullLVo.setBullId(bullId);
		
		try{
			BbBullLVo bbBullLVo = getBbBullLVo(baBrdBVo, bullId, "ko");
			if(bbBullLVo != null && "B".equals(bbBullLVo.getBullStatCd())){
				// 검색 색인 데이터를 더함
				UserVo userVo = new UserVo();
				userVo.setCompId(bbBullLVo.getCompId());
				userVo.setLoutCatId("B");//하드코딩 - 달라지는것 없음
				List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(bbBullLVo.getCompId(), "ko");
				userVo.setLangTypCd(langTypCdList.get(0).getCdVa());
				addSrchIndex(bbBullLVo, userVo, queryQueue, "D");
			}
		}catch(SQLException se){LOGGER.error("[addSrchIndex insert(D) fail!!] - brdId : "+baBrdBVo.getBrdId() + "  bullId : "+bullId);}
		queryQueue.delete(paramBbBullLVo);
	}

	/** 게시예약일 초기화 */
	public void initBullRezvDt(BbBullLVo bbBullLVo) {
		if (bbBullLVo.getBullRezvDt() == null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			bbBullLVo.setBullRezvDt(new Timestamp(cal.getTimeInMillis()).toString());
		}
	}

	/** 게시완료일 초기화 */
	public void initBullExprDt(BaBrdBVo baBrdBVo, BbBullLVo bbBullLVo) {
		if ("R".equals(baBrdBVo.getKndCd()) && bbBullLVo.getBullExprDt() == null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, baBrdBVo.getRezvPrd());
			cal.add(Calendar.HOUR, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			bbBullLVo.setBullExprDt(new Timestamp(cal.getTimeInMillis()).toString());
		}
	}

	/** 카테고리 목록 얻기 */
	@SuppressWarnings("unchecked")
	public List<BaCatDVo> getBaCatDVoList(String catGrpId, String langTypCd) throws SQLException {
		// 카테고리(BA_CAT_D) 테이블 - SELECT
		BaCatDVo baCatDVo = new BaCatDVo();
		baCatDVo.setCatGrpId(catGrpId);
		baCatDVo.setQueryLang(langTypCd);
		return (List<BaCatDVo>) commonDao.queryList(baCatDVo);
	}

	/** 게시물 임시저장 저장 */
	public void saveBaTmpSaveLVo(HttpServletRequest request, Integer bullId, QueryQueue queryQueue) {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시물 임시저장(BA_TMP_SAVE_L) 테이블 - BIND
		BaTmpSaveLVo baTmpSaveLVo = new BaTmpSaveLVo();
		VoUtil.bind(request, baTmpSaveLVo);
		
		// 게시물ID
		baTmpSaveLVo.setBullId(bullId);
		
		// 등록자
		baTmpSaveLVo.setRegrUid(userVo.getUserUid());
		
		// 수정자, 수정일시
		baTmpSaveLVo.setModrUid(userVo.getUserUid());
		baTmpSaveLVo.setModDt("sysdate");
		
		// 게시물 임시저장(BA_TMP_SAVE_L) 테이블 - INSERT OR UPDATE
		queryQueue.store(baTmpSaveLVo);
	}

	/** 게시상신함 VO 얻기 */
	public BaBullSubmLVo getBaBullSubmLVo(int bullId, String langTypCd) throws SQLException {
		BaBullSubmLVo baBullSubmLVo = new BaBullSubmLVo();
		baBullSubmLVo.setBullId(bullId);
		baBullSubmLVo.setQueryLang(langTypCd);
		return (BaBullSubmLVo) commonDao.queryVo(baBullSubmLVo);
	}

	/** 게시상신함 저장 */
	public void saveBaBullSubmLVo(HttpServletRequest request, Integer bullId, String discrUid, QueryQueue queryQueue) {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 게시상신함(BA_BULL_SUBM_L) 테이블 - BIND
		BaBullSubmLVo baBullSubmLVo = new BaBullSubmLVo();
		VoUtil.bind(request, baBullSubmLVo);

		// 게시물 ID
		baBullSubmLVo.setBullId(bullId);

		// 등록자
		baBullSubmLVo.setRegrUid(userVo.getUserUid());

		// 심의자
		baBullSubmLVo.setDiscrUid(discrUid);

		// 수정자, 수정일시
		baBullSubmLVo.setModrUid(userVo.getUserUid());
		baBullSubmLVo.setModDt("sysdate");

		// 게시상신함(BA_BULL_SUBM_L) 테이블 - INSERT OR UPDATE
		queryQueue.store(baBullSubmLVo);
	}

	/** 심의함 저장 */
	public void saveDiscBull(String bullId, String bullStatCd, String rjtOpin, QueryQueue queryQueue) {
		// 게시상신함(BA_BULL_SUBM_L) 테이블 - BIND
		BaBullSubmLVo baBullSubmLVo = new BaBullSubmLVo();
		
		// 게시물 ID
		baBullSubmLVo.setBullId(Integer.parseInt(bullId));
		// 게시물상태코드
		baBullSubmLVo.setBullStatCd(bullStatCd);
		// 반려의견
		baBullSubmLVo.setRjtOpin(rjtOpin);
		// 심의일시
		baBullSubmLVo.setDiscDt("sysdate");
		
		// 게시상신함(BA_BULL_SUBM_L) 테이블 - UPDATE
		queryQueue.update(baBullSubmLVo);
	}

	/** 게시물상태코드 변경 */
	public void updateBullStatCd(BaBrdBVo baBrdBVo, Integer bullId, String bullStatCd, QueryQueue queryQueue) {
		// 게시물(BB_X000X_L) 테이블 - BIND
		BbBullLVo bbBullLVo = newBbBullLVo(baBrdBVo);
		
		// 게시물ID
		bbBullLVo.setBullId(bullId);
		
		// 게시물상태코드
		bbBullLVo.setBullStatCd(bullStatCd);

		// 게시물(BB_X000X_L) 테이블 - UPDATE
		queryQueue.update(bbBullLVo);
	}

	/** 게시물 예약저장 VO 얻기 */
	public BaRezvSaveLVo getBaRezvSaveLVo(String regrUid, int bullId, String langTypCd) throws SQLException {
		BaRezvSaveLVo baBullSubmLVo = new BaRezvSaveLVo();
		baBullSubmLVo.setRegrUid(regrUid);
		baBullSubmLVo.setBullId(bullId);
		baBullSubmLVo.setQueryLang(langTypCd);
		return (BaRezvSaveLVo) commonDao.queryVo(baBullSubmLVo);
	}

	/** 게시물 예약저장 저장 */
	public void saveBaRezvSaveLVo(HttpServletRequest request, Integer bullId, QueryQueue queryQueue) {
		saveBaRezvSaveLVo(request, bullId, null, queryQueue);
	}

	/** 게시물 예약저장 저장 */
	public void saveBaRezvSaveLVo(HttpServletRequest request, Integer bullId, BaRezvSaveLVo baRezvSaveLVo, QueryQueue queryQueue)
			{
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		if (baRezvSaveLVo == null) {
			// 게시물 예약저장(BA_REZV_SAVE_L) 테이블 - BIND
			baRezvSaveLVo = new BaRezvSaveLVo();
			VoUtil.bind(request, baRezvSaveLVo);

			// 등록자
			baRezvSaveLVo.setRegrUid(userVo.getUserUid());
			
			// 수정자, 수정일시
			baRezvSaveLVo.setModrUid(userVo.getUserUid());
			baRezvSaveLVo.setModDt("sysdate");
		}
		
		// 게시물ID
		baRezvSaveLVo.setBullId(bullId);
		
		// 게시물 예약저장(BA_REZV_SAVE_L) 테이블 - INSERT OR UPDATE
		queryQueue.store(baRezvSaveLVo);
	}

	/** 게시물 저장 */
	public Integer saveBbBullLVo(HttpServletRequest request, BaBrdBVo baBrdBVo, String bullId, String bullStatCd,
			QueryQueue queryQueue) throws SQLException {
		return saveBbBullLVo(request, baBrdBVo, bullId, bullStatCd, null, queryQueue);
	}

	/** 게시물 저장 */
	public Integer saveBbBullLVo(HttpServletRequest request, BaBrdBVo baBrdBVo, String bullId, String bullStatCd,
			BbBullLVo bbBullLVo, QueryQueue queryQueue) throws SQLException {
		// brdIds
		String brdIdList = request.getParameter("brdIdList");
		String[] brdIds = null;
		if (brdIdList != null && !"".equals(brdIdList)) {
			brdIds = brdIdList.split(",");
		}
		return saveBbBullLVo(request, baBrdBVo, bullId, bullStatCd, null, brdIds, queryQueue);
	}

	/** 게시물 저장 */
	public Integer saveBbBullLVo(HttpServletRequest request, BaBrdBVo baBrdBVo, String bullId, String bullStatCd,
			BbBullLVo bbBullLVo, String[] brdIds, QueryQueue queryQueue) throws SQLException {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		if (bbBullLVo == null) {
			// 게시물(BB_X000X_L) 테이블 - BIND
			bbBullLVo = newBbBullLVo(baBrdBVo);
			VoUtil.bind(request, bbBullLVo);
			
			// 부서게시판이면
 			if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
				bbBullLVo.setDeptId(userVo.getDeptId());
			}
			
			// 회사ID
			//bbBullLVo.setCompId(baBrdBVo.getCompId() !=null ? baBrdBVo.getCompId() : userVo.getCompId());
 			bbBullLVo.setCompId(userVo.getCompId());
			// 수정자
			bbBullLVo.setModrUid(userVo.getUserUid());
			// 수정일시
			bbBullLVo.setModDt("sysdate");
		} else {
			
			String regDt=bbBullLVo.getRegDt();
			if(regDt != null && !"sysdate".equals(regDt) && regDt.length()>19){
				bbBullLVo.setRegDt(regDt.substring(0, 19));
			}
			
			String modDt=bbBullLVo.getModDt();
			if(modDt != null && !"sysdate".equals(modDt) && modDt.length()>19){
				bbBullLVo.setModDt(modDt.substring(0, 19));
			}
			
			if (bbBullLVo.getTableName() == null) bbBullLVo.setTableName(bbTblSvc.getFullTblNm(baBrdBVo.getTblNm()));
		}
		
		// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
		bbBullLVo.setBullStatCd(bullStatCd);
		
		if(bbBullLVo!=null){
			// 동일한 파라미터일 경우 String+Comma 형태로 변환
			setMultiParamToVo(request, bbBullLVo, baBrdBVo.getBrdId());
		}
		String privUseYn=baBrdBVo.getOptMap()!=null ? (String)baBrdBVo.getOptMap().get("privUseYn") : null;
		
		boolean isPriv=privUseYn!=null && "Y".equals(privUseYn);
		// 비공개 여부
		String privYn = request.getParameter("privYn");
		if(isPriv && privYn!=null && !privYn.isEmpty())
			bbBullLVo.setTgtUserYn("Y");
		else if(isPriv && (privYn==null || privYn.isEmpty()))
			bbBullLVo.setTgtUserYn("N");
		
		
		if (bullId == null || bullId.isEmpty()) {
			// 게시물ID 생성
			bbBullLVo.setBullId(bbCmSvc.createBullId());
			
			// 기본값 세팅
			bbBullLVo.setReplyGrpId(bbBullLVo.getBullId());
			bbBullLVo.setReplyOrdr(0);
			bbBullLVo.setReplyDpth(0);
			bbBullLVo.setReadCnt(0);
			bbBullLVo.setProsCnt(0);
			bbBullLVo.setConsCnt(0);
			bbBullLVo.setRecmdCnt(0);
			bbBullLVo.setScre(0);
			
			// 게시물(BB_X000X_L) 테이블 - INSERT
			queryQueue.insert(bbBullLVo);
			
			if ("B".equals(bullStatCd)) {
				// 게시판 복수 지정시
				if (brdIds != null && brdIds.length > 1) {
					bbBullLVo.setCompId(userVo.getCompId());//전사게시판이 아닐 경우 회사ID를 넣기 위해 세팅한다.
					bbBullLVo.setDeptId(userVo.getDeptId());//부서게시판일 경우 부서ID를 넣기 위해 세팅한다.
					// 게시판ID 배열로 게시물 등록
					insertBullsByBrdIds(request, bbBullLVo, brdIds, queryQueue);
				}
			}
		} else {
			//if(baBrdBVo.getCompId() !=null) bbBullLVo.setCompId(baBrdBVo.getCompId());
			bbBullLVo.setCompId(userVo.getCompId());
			// 게시물(BB_X000X_L) 테이블 - UPDATE
			queryQueue.update(bbBullLVo);
			
			//답변글 만료기간 수정(원본글일 경우) - 게시완료일이 있을경우에만 적용
			if(bbBullLVo.getBullExprDt() != null && !"".equals(bbBullLVo.getBullExprDt()) && ( bbBullLVo.getBullPid() == null || "".equals(bbBullLVo.getBullPid()) )){
				BbBullLVo updateBbBullLVo = new BbBullLVo();
				updateBbBullLVo.setTableName(bbBullLVo.getTableName());
				
				//답변글 만료기간 삽입(부모게시글) - 게시완료일이 있을경우에만 적용
				String bullExprDt=bbBullLVo.getBullExprDt();
				if(bullExprDt != null && !"".equals(bullExprDt) && bullExprDt.length()>19){
					bullExprDt=bullExprDt.substring(0, 19);
				}
				
				updateBbBullLVo.setBullExprDt(bullExprDt);
				updateBbBullLVo.setReplyGrpId(bbBullLVo.getBullId());
				updateBbBullLVo.setReplyOrdr(1);
				queryQueue.update(updateBbBullLVo);
			}
		}
		
		// 비공개 여부 저장
		if(isPriv){
			String userUid=userVo.getUserUid();
			if(bullId != null && !bullId.isEmpty() && privYn!=null && "Y".equals(privYn)) {
				// 게시물(BB_X000X_L) 테이블 - SELECT
				BbBullLVo vo = getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd, false);
				if(vo!=null) userUid=vo.getRegrUid();
			}
			
			bbBullTgtSvc.savePrivUser(queryQueue, privYn, bbBullLVo.getBullId(), userUid);
		}else{
			// 게시대상 저장
			if (bullId == null || bullId.isEmpty()) {
				bbBullTgtSvc.saveBullTgt(request, bbBullLVo.getBullId(), queryQueue);
			} else {
				BbBullLVo bullVo = getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd);
				saveBullTgtWithReplyGrp(request, baBrdBVo, bullVo, queryQueue);
			}
			
		}
		
		// 게시옵션 저장
		bbBullOptSvc.saveBullOpt(request, bbBullLVo.getBullId(), queryQueue);
		// 검색엔진 인덱싱 처리
		if("B".equals(bbBullLVo.getBullStatCd()))
			addSrchIndex(bbBullLVo, userVo, queryQueue, bullId == null || bullId.isEmpty() ? "I" : "U");
		
		return bbBullLVo.getBullId();
	}

	/** 게시대상 저장 */
	private void saveBullTgtWithReplyGrp(HttpServletRequest request, BaBrdBVo baBrdBVo, BbBullLVo bbBullLVo, QueryQueue queryQueue) throws SQLException {
		// 답변형게시판인 경우 모든 답변글의 게시대상을 변경한다.
		if ("Y".equals(baBrdBVo.getReplyYn()) && bbBullLVo.getBullPid() == null) {
			// 게시물(BB_X000X_L) 테이블 - SELECT
			BbBullLVo paramBullVo = newBbBullLVo(baBrdBVo);
			paramBullVo.setReplyGrpId(bbBullLVo.getReplyGrpId());
			List<BbBullLVo> bbBullVoList = getBbBullVoList(baBrdBVo, paramBullVo);

			// 대상부서지정여부, 대상사용자지정여부
			String[] orgIds = request.getParameterValues("orgId");
			String[] userUids = request.getParameterValues("userUid");
			String tgtDeptYn = (orgIds != null && orgIds.length > 0) ? "Y" : "N";
			String tgtUserYn = (userUids != null && userUids.length > 0) ? "Y" : "N";

			for (BbBullLVo bullVo : bbBullVoList) {
				// 게시대상 저장
				bbBullTgtSvc.saveBullTgt(request, bullVo.getBullId(), queryQueue);

				// 게시물(BB_X000X_L) 테이블 - UPDATE
				BbBullLVo updateBullVo = newBbBullLVo(baBrdBVo);
				updateBullVo.setBullId(bullVo.getBullId());
				updateBullVo.setTgtDeptYn(tgtDeptYn);
				updateBullVo.setTgtUserYn(tgtUserYn);
				queryQueue.update(updateBullVo);
			}
		}
	}

	/** 게시판ID 배열로 게시물 등록 */
	private void insertBullsByBrdIds(HttpServletRequest request, BbBullLVo bbBullLVo, String[] brdIds, QueryQueue queryQueue) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		Integer[] arrBullId = new Integer[brdIds.length-1];
		Integer nBrd = 0;
		for (String brdId : brdIds) {
			if (brdId.equals(bbBullLVo.getBrdId())) continue;
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo anotherBrdVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
			// 게시물(BB_X000X_L) 테이블 - BIND
			BbBullLVo anotherVo = newBbBullLVo(anotherBrdVo);
			BeanUtils.copyProperties(bbBullLVo, anotherVo, new String[] { "bullId", "brdId", "replyGrpId" });
			
			// 게시물ID 생성
			anotherVo.setBullId(bbCmSvc.createBullId());
			// 게시판ID
			anotherVo.setBrdId(brdId);
			// 기본값 세팅
			anotherVo.setReplyGrpId(anotherVo.getBullId());
			
			if("Y".equals(anotherBrdVo.getDeptBrdYn())) {// 대상 게시판이 부서게시판이면...
				anotherVo.setDeptId(bbBullLVo.getDeptId());
			}
			
			// 게시물(BB_X000X_L) 테이블 - INSERT
			queryQueue.insert(anotherVo);
			
			// 게시대상 저장
			bbBullTgtSvc.saveBullTgt(request, anotherVo.getBullId(), queryQueue);
			
			// 게시옵션 저장
			bbBullOptSvc.saveBullOpt(request, anotherVo.getBullId(), queryQueue);
			
			arrBullId[nBrd++] = anotherVo.getBullId();
			
			// 검색엔진 인덱싱 처리
			if("B".equals(anotherVo.getBullStatCd()))
				addSrchIndex(anotherVo, userVo, queryQueue, "I");
		}
		request.setAttribute("arrBullId", arrBullId);
	}

	/** 게시물 답변 저장 */
	public Integer saveReplyBbBullLVo(HttpServletRequest request, BaBrdBVo baBrdBVo, String bullStatCd, String bullPid, QueryQueue queryQueue) throws NumberFormatException, SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시물(BB_X000X_L) 테이블 - BIND
		BbBullLVo bbBullLVo = newBbBullLVo(baBrdBVo);
		VoUtil.bind(request, bbBullLVo);
		
		// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
		bbBullLVo.setBullStatCd(bullStatCd);
		
		// 부모 게시글 조회
		// 게시물(BB_X000X_L) 테이블 - SELECT
		BbBullLVo parentBullVo = getBbBullLVo(baBrdBVo, Integer.parseInt(bullPid), langTypCd);
		
		// 부모게시물ID
		bbBullLVo.setBullPid(Integer.parseInt(bullPid));
		
		// 답변그룹ID, 답변단계
		bbBullLVo.setReplyGrpId(parentBullVo.getReplyGrpId());
		bbBullLVo.setReplyDpth(parentBullVo.getReplyDpth() + 1);
		
		// 답변순서
		bbBullLVo.setReplyOrdr(getReplyOrdr(baBrdBVo, parentBullVo, queryQueue));
		
		// 카테고리ID
		if ("Y".equals(baBrdBVo.getCatYn())) {
			bbBullLVo.setCatId(parentBullVo.getCatId());
		}
		
		// 기본값 세팅
		bbBullLVo.setReadCnt(0);
		bbBullLVo.setProsCnt(0);
		bbBullLVo.setConsCnt(0);
		bbBullLVo.setRecmdCnt(0);
		bbBullLVo.setScre(0);
		
		//답변글 만료기간 삽입(부모게시글) - 게시완료일이 있을경우에만 적용
		String bullExprDt=parentBullVo.getBullExprDt();
		if(bullExprDt != null && !"".equals(bullExprDt)){
			if(bullExprDt.length()>19){
				bullExprDt=bullExprDt.substring(0, 19);
			}
			bbBullLVo.setBullExprDt(bullExprDt);
		}
		
		// 회사ID
		bbBullLVo.setCompId(userVo.getCompId());
		
		if(parentBullVo.getDeptId() != null){//[원본글이 deptId를 가지고 있을경우 답변에도 동일하게 적용한다.]
			// 부서ID
			bbBullLVo.setDeptId(parentBullVo.getDeptId());
		}
		
		// 수정자
		bbBullLVo.setModrUid(userVo.getUserUid());
		// 수정일시
		bbBullLVo.setModDt("sysdate");
		
		// 게시물ID 생성
		bbBullLVo.setBullId(bbCmSvc.createBullId());
		
		// 게시물(BB_X000X_L) 테이블 - INSERT
		queryQueue.insert(bbBullLVo);
		
		// 게시옵션 저장
		bbBullOptSvc.saveBullOpt(request, bbBullLVo.getBullId(), queryQueue);

		// 게시대상 저장
		bbBullTgtSvc.saveBullTgt(request, bbBullLVo.getBullId(), queryQueue);
		
		return bbBullLVo.getBullId();
	}

	/** 게시물 임시저장 삭제 */
	public void deleteBaTmpSaveLVo(HttpServletRequest request, Integer bullId, QueryQueue queryQueue) {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시물 임시저장(BA_TMP_SAVE_L) 테이블 - BIND
		BaTmpSaveLVo baTmpSaveLVo = new BaTmpSaveLVo();
		baTmpSaveLVo.setRegrUid(userVo.getUserUid());
		baTmpSaveLVo.setBullId(bullId);
		
		// 게시물 임시저장(BA_TMP_SAVE_L) 테이블 - DELETE
		queryQueue.delete(baTmpSaveLVo);
	}

	/** 게시상신함 삭제 */
	public void deleteBaBullSubmLVo(HttpServletRequest request, String regrUid, int bullId, QueryQueue queryQueue) {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시상신함(BA_BULL_SUBM_L) 테이블 - BIND
		BaBullSubmLVo baullSubmLVo = new BaBullSubmLVo();
		if (regrUid == null) regrUid = userVo.getUserUid();
		baullSubmLVo.setRegrUid(regrUid);
		baullSubmLVo.setBullId(bullId);
		
		// 게시상신함(BA_BULL_SUBM_L) 테이블 - DELETE
		queryQueue.delete(baullSubmLVo);		
	}

	/** 게시물 예약저장 삭제 */
	public void deleteBaRezvSaveLVo(HttpServletRequest request, int bullId, QueryQueue queryQueue) {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 게시물 예약저장(BA_REZV_SAVE_L) 테이블 - BIND
		BaRezvSaveLVo baRezvSaveLVo = new BaRezvSaveLVo();
		baRezvSaveLVo.setRegrUid(userVo.getUserUid());
		baRezvSaveLVo.setBullId(bullId);
		
		// 게시물 예약저장(BA_REZV_SAVE_L) 테이블 - DELETE
		queryQueue.delete(baRezvSaveLVo);		
	}

	/** 답변순서 구하기 */
	private int getReplyOrdr(BaBrdBVo baBrdBVo, BbBullLVo parentBullVo, QueryQueue queryQueue)
			throws SQLException {
		// 부모 게시글의 바로 아래 동생 게시글 답변순서
		BbBullLVo ordrBullVo = newBbBullLVo(baBrdBVo, true);
		ordrBullVo.setReplyGrpId(parentBullVo.getReplyGrpId());
		ordrBullVo.setReplyOrdr(parentBullVo.getReplyOrdr());
		ordrBullVo.setReplyDpth(parentBullVo.getReplyDpth());
		ordrBullVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.selectMinReplyOrdr");
		Integer minReplyOrdr = commonDao.queryInt(ordrBullVo);
		
		// 부모 게시글이 동생이 있다면 답변글이 동생 자리로 가기 위해 그 아래 모든 게시글들을 한칸씩 아래로 내린다.
		if (minReplyOrdr != null) {
			// 답변순서 업데이트
			// 게시물(BB_X000X_L) 테이블 - UPDATE
			BbBullLVo updateBullVo = newBbBullLVo(baBrdBVo);
			updateBullVo.setReplyGrpId(parentBullVo.getReplyGrpId());
			updateBullVo.setReplyOrdr(minReplyOrdr);
			updateBullVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.updateReplyOrdr");
			queryQueue.update(updateBullVo);
			
			return minReplyOrdr;
		} else {
			ordrBullVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.selectMaxReplyOrdr");
			Integer maxReplyOrdr = commonDao.queryInt(ordrBullVo);
			
			return maxReplyOrdr + 1;
		}
	}

	/** 게시물에 조회이력여부 세팅[게시물별] */
	public void setHstBullList(List<BbBullLVo> bbBullLVoList , String readHstUseYn){
		for(BbBullLVo bbBullLVo : bbBullLVoList){
			bbBullLVo.setReadHstUseYn(readHstUseYn);
		}
	}
	
	/** 최신 게시물 초기 목록 세팅 */
	public void setNewBullList(HttpServletRequest request, ModelMap model, UserVo userVo , String bullId ) throws SQLException {
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(userVo.getLangTypCd());
		bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
		baBrdBVo.setWhereSqllet("AND T.LAST_BULL_YN = 'Y'");
		@SuppressWarnings("unchecked")
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonDao.queryList(baBrdBVo);
		
		// 모듈 별 권한 있는 모듈참조ID 목록
		List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "BB", "R");
				
		// 게시판ID 목록
		List<String> brdIdList = new ArrayList<String>();
		for (BaBrdBVo brdVo : baBrdBVoList) {
			if(!bbBrdSvc.chkMdIds(mdIds, brdVo.getBrdId()))	continue;
			brdIdList.add(brdVo.getBrdId());
		}
		setPrevNextList(request, model, userVo, bullId , brdIdList);
	}
	
	/** 나의 게시물 초기 목록 세팅 */
	public void setMyBullList(HttpServletRequest request, ModelMap model, UserVo userVo , String bullId )throws SQLException {
		// 나의게시물설정(BA_MY_BULL_M) 테이블 - SELECT
		BaMyBullMVo paramMyBullVo = new BaMyBullMVo();
		paramMyBullVo.setUserUid(userVo.getUserUid());
		@SuppressWarnings("unchecked")
		List<BaMyBullMVo> baMyBullMVoList = (List<BaMyBullMVo>) commonDao.queryList(paramMyBullVo);
		
		// 모듈 별 권한 있는 모듈참조ID 목록
		List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "BB", "R");
				
		List<String> brdIdList = new ArrayList<String>();
		for (BaMyBullMVo baMyBullMVo : baMyBullMVoList) {
			if(!bbBrdSvc.chkMdIds(mdIds, baMyBullMVo.getBrdId())) continue;
			brdIdList.add(baMyBullMVo.getBrdId());
		}
		setPrevNextList(request, model, userVo, bullId , brdIdList);
	}
	
	/** 전체게시물 이전,다음 목록 얻기 */
	public void setPrevNextList(HttpServletRequest request, ModelMap model, UserVo userVo , String bullId , List<String> brdIdList)
			throws SQLException {
		// 기한(BA_SETUP_B) 테이블 - SELECT
		BaUserSetupBVo paramUserSetupVo = new BaUserSetupBVo();
		paramUserSetupVo.setUserUid(userVo.getUserUid());
		BaUserSetupBVo baUserSetupBVo = (BaUserSetupBVo) commonDao.queryVo(paramUserSetupVo);
		
		// 기한
		int ddln = 4;
		if (baUserSetupBVo != null && baUserSetupBVo.getDdln() != null) {
			ddln = baUserSetupBVo.getDdln();
		}
		request.setAttribute("listAll", "Y");
		// 최신게시물 목록 얻기
		List<BbBullLVo> bbBullLVoList = this.getNewBullList(request, brdIdList, ddln);
		for( int i=0;i<bbBullLVoList.size();i++){
			if(Integer.parseInt(bullId) == bbBullLVoList.get(i).getBullId().intValue()){
				if( i-1 >= 0 ) model.put("prevBullVo", bbBullLVoList.get(i-1));
				if( i+1 <= bbBullLVoList.size()-1 ) model.put("nextBullVo", bbBullLVoList.get(i+1));
				break;
			}
		}
		request.removeAttribute("listAll");
	}
	
	/** 최신게시물 목록 얻기 */
	@SuppressWarnings("unchecked")
	public List<BbBullLVo> getNewBullList(HttpServletRequest request, List<String> brdIdList, Integer ddln) throws SQLException
			{
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 기한
		String ddlnYmd = StringUtil.afterDays(-ddln);
		
		List<BbBullLVo> bbBullLVoList = new ArrayList<BbBullLVo>();
		for (String brdId : brdIdList) {
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
			
			// 게시물(BB_X000X_L) 테이블 - BIND
			BbBullLVo paramBullVo = newBbBullLVo(baBrdBVo, true);
			VoUtil.bind(request, paramBullVo);
			paramBullVo.setBrdId(baBrdBVo.getBrdId());
			if(request.getAttribute("listAll") != null && "Y".equals((String)request.getAttribute("listAll"))){
				paramBullVo.setBullId(null);
			}
			// 사용자정보 세팅
			setUserInfo(request, paramBullVo);
			
			// 부서게시판이면
			if ("Y".equals(baBrdBVo.getDeptBrdYn())) {
				setOrgHstIdList(langTypCd, userVo, paramBullVo);
				//paramBullVo.setDeptId(userVo.getDeptId());
			}

			// 전사게시판이면
			if ("Y".equals(baBrdBVo.getAllCompYn())) {
				paramBullVo.setCompId(null);
			} else {
				paramBullVo.setCompId(userVo.getCompId());
			}
			
			// 게시물(BB_X000X_L) 테이블 - SELECT
			paramBullVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.selectNewList");
			//paramBullVo.setWhereSqllet("AND T.REG_DT > CONVERT(DATETIME, '" + ddlnYmd + "', 120)");
			int p = ddlnYmd.lastIndexOf('.');
			if(p>0) ddlnYmd = ddlnYmd.substring(0, p);
			paramBullVo.setRegDt(ddlnYmd);
			
			// 답변형인 경우
			String orderBy = "T.BULL_ID DESC";
			if ("Y".equals(baBrdBVo.getReplyYn())) {
				orderBy = "T.REPLY_GRP_ID DESC, T.REPLY_ORDR ASC";
			}
			paramBullVo.setOrderBy(orderBy);
			
			// 대표컬럼 조회(목록기준)
			//BaColmDispDVo firstVo = bbBrdSvc.getFirstBaColmDispDVo(baBrdBVo, bbBrdSvc.getBaColmDispDVoList(request, brdId, true, null, "Y", null, false));
			//System.out.println("colmNm : "+firstVo.getColmVo().getColmNm());
			
			List<BbBullLVo> bullList = (List<BbBullLVo>) commonDao.queryList(paramBullVo);
			
			//for(BbBullLVo storedBbBullLVo : bullList) {
				// 대표 컬럼 SUBJ 로 세팅
				//bbBullSvc.setBbBullLVoSubj(langTypCd, baBrdBVo, storedBbBullLVo, firstVo, null);
			//}
			
			// 첨부파일 수 조회
			bbBullFileSvc.setFileCount(bullList);
			this.setHstBullList(bullList, baBrdBVo.getReadHstUseYn());
			
			bbBullLVoList.addAll(bullList);
		}
		//BbBullLVo compareTo메소드의 영향을 안받기 위해서 compareTo 메소드를 따로 구현한다.
		if(bbBullLVoList.size()>0){
			Comparator<BbBullLVo> comp = new Comparator<BbBullLVo>(){
				@Override
				public int compare(BbBullLVo o1, BbBullLVo o2) {
					int grpId1=o1.getReplyGrpId()==null ? 0 : o1.getReplyGrpId().intValue();
					int grpId2=o2.getReplyGrpId()==null ? 0 : o2.getReplyGrpId().intValue();
					return grpId1 < grpId2 ? 1 : ( grpId1==grpId2 ? 0 : -1);
				}
			};
			// 게시물ID로 정렬
			Collections.sort(bbBullLVoList , comp);
			//Collections.reverse(bbBullLVoList);//기존 sort 후 reverse 방식에서 compare의 재구현을 통해 게시글 sort 만으로 가능하게 수정 
			
			// rnum
			int rnum = 0;
			for (BbBullLVo bbBullLVo : bbBullLVoList) {
				bbBullLVo.setRnum(++rnum);
			}
		}
		
		return bbBullLVoList;
	}

	/** 사용자정보 세팅 */
	public void setUserInfo(HttpServletRequest request, BbBullLVo bbBullLVo) {
		UserVo userVo = LoginSession.getUser(request);
		bbBullLVo.setSchUserUid(userVo.getUserUid());
		bbBullLVo.setSchDeptId(userVo.getOrgId());
		if(userVo.getOrgPids() == null || userVo.getOrgPids().length == 0) {
			bbBullLVo.setSchOrgPids(null);
			return;
		}
		bbBullLVo.setSchOrgPids(userVo.getOrgPids());
	}

	/** 최대 본문 사이즈 model에 추가 */
	public void putBodySizeToModel(HttpServletRequest request, ModelMap model) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);

		// 시스템 설정 조회 - 본문 사이즈
		Integer bodySize = ptSysSvc.getBodySizeMap(langTypCd, userVo.getCompId()).get("bb") * 1024;
		model.put("bodySize", bodySize);
	}

	/** 기타(점수, 추천, 찬반투표) 참가여부 model에 추가 */
	public void putEtcToModel(HttpServletRequest request, BaBrdBVo baBrdBVo, String bullId, ModelMap model) throws SQLException {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		if ("Y".equals(baBrdBVo.getScreUseYn())) {
			boolean screHstExist = bbBullScreSvc.isScreHstExist(bullId, userVo.getUserUid());
			model.put("screHstExist", screHstExist);
		}
		if ("Y".equals(baBrdBVo.getRecmdUseYn())) {
			boolean recmdHstExist = bbBullRecmdSvc.isRecmdHstExist(bullId, userVo.getUserUid());
			model.put("recmdHstExist", recmdHstExist);
		}
		if ("Y".equals(baBrdBVo.getFavotYn())) {
			boolean favotHstExist = bbBullFavotSvc.isFavotHstExist(bullId, userVo.getUserUid());
			model.put("favotHstExist", favotHstExist);
		}
	}

	/** 컬럼표시여부 리스트 -> 맵 */
	public Map<String, BaColmDispDVo> getColmDispMap(List<BaColmDispDVo> baColmDispDVoList) {
		Map<String, BaColmDispDVo> baColmDispDVoMap = new HashMap<String, BaColmDispDVo>();
		for (BaColmDispDVo baColmDispDVo : baColmDispDVoList) {
			String colmNm = baColmDispDVo.getColmVo().getColmNm();
			if (baColmDispDVoMap.get(colmNm) == null) baColmDispDVoMap.put(colmNm, baColmDispDVo);
		}
		return baColmDispDVoMap;
	}
	
	/** 맵 형태의 데이터로 변환 (엑셀다운로드)*/
	public void setListBullMap(){
		
	}
	
	/** 검색 색인 데이터를 더함 */
	public void addSrchIndex(BbBullLVo bbBullLVo, UserVo userVo, QueryQueue queryQueue, String actId) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(sysPlocMap.get("integratedSearchEnable")==null || !"Y".equals(sysPlocMap.get("integratedSearchEnable")))//통합검색 사용여부
			return;
		try{
			String compId=bbBullLVo.getCompId()!=null ? bbBullLVo.getCompId() : userVo.getCompId();
			UserVo adminVo = ptSecuSvc.createEmptyAdmin(compId, userVo.getLoutCatId(), userVo.getLangTypCd());
			
			// 검색엔진 인덱싱 처리
			CmSrchBVo cmSrchBVo = emSrchSvc.createVo();
			cmSrchBVo.setCompId(compId);
			cmSrchBVo.setMdRid("BB");
			cmSrchBVo.setMdBxId(bbBullLVo.getBrdId());
			cmSrchBVo.setMdId(String.valueOf(bbBullLVo.getBullId()));
			// 리소스ID세팅
			ArrayList<PtMnuDVo> mnuList = ptSecuSvc.getAuthedMnuVoListByMdRid(adminVo, "BB");
			if(mnuList!=null){
				for(PtMnuDVo ptMnuDVo : mnuList){
					if(ptMnuDVo.getMdId().equals(bbBullLVo.getBrdId())){
						cmSrchBVo.setMdBxRescId(ptMnuDVo.getRescId());
						break;
					}
				}
			}
			// 모듈함리소스ID 가 없을 경우(메뉴에 등록되지 않았거나 해당 메뉴에 모듈ID가 지정되어 않은경우) 테이블에 저장하지 않는다.
			if(cmSrchBVo.getMdBxRescId() == null || cmSrchBVo.getMdBxRescId().isEmpty()) {
				throw new NullPointerException("[addSrchIndex insert("+actId+") fail!!] - mdBxRescId is null\nbrdId : "+bbBullLVo.getBrdId() + "  bullId : "+bbBullLVo.getBullId());
			}
			//TODO - I:INSERT, U:UPDATE, D:DELETE, C:CATEGORY UPDATE
			if(actId == null ) actId = "I";
			cmSrchBVo.setActId(actId);
			cmSrchBVo.setUrl("/cm/bb/redirect.do?brdId="+bbBullLVo.getBrdId()+"&bullId="+bbBullLVo.getBullId());
			cmSrchBVo.setRegDt("sysdate");
			queryQueue.insert(cmSrchBVo);
		}catch(SQLException se){
			LOGGER.error("[addSrchIndex insert("+actId+") fail!!] - brdId : "+bbBullLVo.getBrdId() + "  bullId : "+bbBullLVo.getBullId());
		}catch(NullPointerException npe){
			LOGGER.error(npe.getMessage());
		}
	}
	
	public boolean reindexBb(UserVo userVo, String compId) throws SQLException {
		
		BbBullLVo bbBullLVo;
		
		// 대장 리소스ID세팅
		String mxRescId = null;
		UserVo adminVo = ptSecuSvc.createEmptyAdmin(compId, userVo.getLoutCatId(), userVo.getLangTypCd());
		ArrayList<PtMnuDVo> mnuList = ptSecuSvc.getAuthedMnuVoListByMdRid(adminVo, "BB");
		
		boolean hasReindexData = false;
		Integer pageNo=0, pageRowCnt = 200;
		QueryQueue queryQueue;
		CmSrchBVo cmSrchBVo;
		
		for(PtMnuDVo storedPtMnuDVo : mnuList){
			// 게시판 ID
			String brdId = storedPtMnuDVo.getMdId();
			
			// 게시판관리(BA_BRD_B) - SELECT
			BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo("ko", brdId);
			
			if(baBrdBVo==null) continue;
			
			// 리소스ID세팅
			mxRescId = storedPtMnuDVo.getRescId();
			if(mxRescId==null) continue;
			
			pageNo=0;
			
			while(true){
				bbBullLVo = newBbBullLVo(baBrdBVo, true);
				bbBullLVo.setCompId(compId);
				bbBullLVo.setBrdId(brdId);
				bbBullLVo.setPageNo(++pageNo);
				bbBullLVo.setPageRowCnt(pageRowCnt);
				@SuppressWarnings("unchecked")
				List<BbBullLVo> bbBullLVoList = (List<BbBullLVo>) commonDao.queryList(bbBullLVo);
				if(bbBullLVoList != null && !bbBullLVoList.isEmpty()){
					
					queryQueue = new QueryQueue();
					for(BbBullLVo storedBbBullLVo : bbBullLVoList){
						
						// 검색 인덱싱 처리
						cmSrchBVo = emSrchSvc.createVo();
						cmSrchBVo.setCompId(userVo.getCompId());
						cmSrchBVo.setMdRid("BB");
						cmSrchBVo.setMdBxId(storedBbBullLVo.getBrdId());
						cmSrchBVo.setMdId(String.valueOf(storedBbBullLVo.getBullId()));
						cmSrchBVo.setMdBxRescId(mxRescId);
						cmSrchBVo.setActId("I");
						cmSrchBVo.setUrl("/cm/bb/redirect.do?brdId="+storedBbBullLVo.getBrdId()+"&bullId="+storedBbBullLVo.getBullId());
						cmSrchBVo.setRegDt("sysdate");
						queryQueue.insert(cmSrchBVo);
					}
					
					if(!queryQueue.isEmpty()){
						hasReindexData = true;
						commonDao.execute(queryQueue);
					}
					if(bbBullLVoList.size() < pageRowCnt){
						break;
					}
				} else {
					break;
				}
			}
		}
		return hasReindexData;
	}
	
	
	/** 로그인팝업사용자 체크 */
	public boolean isLginPopChk(UserVo userVo) throws SQLException{
		BaPopUserBVo baPopUserBVo = new BaPopUserBVo();
		baPopUserBVo.setCompId(userVo.getCompId());
		baPopUserBVo.setUserUid(userVo.getUserUid());
		return commonDao.count(baPopUserBVo)>0;
	}
	
	/** 로그인팝업 사용자 목록 조회 */
	public List<OrUserBVo> getBaPopUserBVoList(String langTypCd, String compId) throws SQLException{
		BaPopUserBVo baPopUserBVo = new BaPopUserBVo();
		baPopUserBVo.setCompId(compId);
		@SuppressWarnings("unchecked")
		List<BaPopUserBVo> baPopUserBVoList = (List<BaPopUserBVo>)commonDao.queryList(baPopUserBVo);
		if(baPopUserBVoList.size()==0) return null;
		// 사용자UID
		List<String> userUidList = new ArrayList<String>();
		for(BaPopUserBVo storedBaPopUserBVo : baPopUserBVoList){
			userUidList.add(storedBaPopUserBVo.getUserUid());
		}
		if(userUidList.size()==0) return null;
		// 사용자기본(OR_USER_B) 테이블 조회 후 맵으로 전환
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo.setUserUidList(userUidList);
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
		
		return orUserBVoList;
		
	}
	
	/** 게시판 Sns 사용 여부 조회 */
	@SuppressWarnings("unchecked")
	public void setSnsList(ModelMap model, String compId, String brdId, String bullId, String useYn) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		// SNS 사용여부가 'N' 일 경우 return
		if(!sysPlocMap.containsKey("brdSnsEnable") || "N".equals(sysPlocMap.get("brdSnsEnable"))) return;
				
		List<BaSnsRVo> baSnsRVoList = null;
		BaSnsRVo baSnsRVo = null;
		if(brdId!=null && bullId!=null){
			baSnsRVo = new BaSnsRVo();
			baSnsRVo.setBrdId(brdId);
			baSnsRVo.setBullId(Integer.parseInt(bullId));
			if(useYn!=null) baSnsRVo.setUseYn(useYn);
			baSnsRVoList = (List<BaSnsRVo>)commonDao.queryList(baSnsRVo);
		}
		if(useYn==null && (baSnsRVoList==null || baSnsRVoList.size()==0)){
			baSnsRVoList=new ArrayList<BaSnsRVo>();
			for(String sns : EmConstant.SNS_LIST){
				baSnsRVo = new BaSnsRVo();
				setSnsRVo(baSnsRVo, null, null, sns, null);
				baSnsRVoList.add(baSnsRVo);
			}
		}
		
		/*if(useYn!=null && baSnsRVoList!=null && baSnsRVoList.size()>0){
			// 환경설정
			Map<String, String> envConfigMap = getEnvConfigMap(null, compId);
			// APP ID 가 등록되어 있지 않을경우 SNS 미사용 처리
			if(envConfigMap==null || (!envConfigMap.containsKey("appId.facebook") && !envConfigMap.containsKey("appId.twitter"))) return;
			
		}*/
		
		model.put("snsList", baSnsRVoList);
		model.put("isSns", Boolean.TRUE);
	}
	
	/** 게시판 SnsVo 세팅 */
	public void setSnsRVo(BaSnsRVo baSnsRVo, String brdId, Integer bullId, String atrbId, String useYn){
		if(brdId!=null) baSnsRVo.setBrdId(brdId);
		if(bullId!=null) baSnsRVo.setBullId(bullId);
		baSnsRVo.setAtrbId(atrbId);
		if(useYn!=null) baSnsRVo.setUseYn(useYn);
	}
	
	/** 게시판 Sns 사용 여부 저장 */
	public void saveSnsList(HttpServletRequest request, QueryQueue queryQueue, 
			String brdId, Integer bullId) throws SQLException, CmException{
		if(bullId==null) return;
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		// SNS 사용여부가 'N' 일 경우 return  
		if(!sysPlocMap.containsKey("brdSnsEnable") || "N".equals(sysPlocMap.get("sysPlocMap"))) return;
		
		List<BaSnsRVo> baSnsRVoList = new ArrayList<BaSnsRVo>();
		String tmpSns = null;
		BaSnsRVo baSnsRVo = null;
		int sortOrdr=0;
		for(String sns : EmConstant.SNS_LIST){
			baSnsRVo = new BaSnsRVo();
			tmpSns = ParamUtil.getRequestParam(request, sns, false);
			setSnsRVo(baSnsRVo, brdId, bullId, sns, tmpSns ==null || tmpSns.isEmpty() ? "N" : "Y");
			baSnsRVo.setSortOrdr(++sortOrdr);
			baSnsRVoList.add(baSnsRVo);
		}
		
		if(baSnsRVoList.size()>0){
			for(BaSnsRVo storedBaSnsRVo : baSnsRVoList){
				queryQueue.store(storedBaSnsRVo);
			}
		}
	}
	
	/** 조직 이력 ID 목록 세팅 */
	public void setOrgHstIdList(String langTypCd, UserVo userVo, BbBullLVo paramBullVo) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(sysPlocMap.containsKey("deptBrdHstYn") && "Y".equals(sysPlocMap.get("deptBrdHstYn"))){
			String orgId=userVo.getDeptId();
			// 조직 이력 ID 목록 조회
			List<String> orgIdList=new ArrayList<String>();
			orgIdList.add(orgId);
			// 조직이력관계(OR_ORG_HST_R) 테이블
			OrOrgHstRVo orOrgHstRVo = new OrOrgHstRVo();
			orOrgHstRVo.setOrgId(orgId);
			orOrgHstRVo.setQueryLang(langTypCd);
			@SuppressWarnings("unchecked")
			List<OrOrgHstRVo> orOrgHstRVoList = (List<OrOrgHstRVo>)commonDao.queryList(orOrgHstRVo);
			if(orOrgHstRVoList != null && !orOrgHstRVoList.isEmpty()){
				for(OrOrgHstRVo storedOrOrgHstRVo : orOrgHstRVoList){
					orgIdList.add(storedOrOrgHstRVo.getHstOrgId());
				}
			}
			
			paramBullVo.setDeptIdList(orgIdList);
			return;
		}
		paramBullVo.setDeptId(userVo.getDeptId());
	}
	
	/** 동일한 이름의 파라미터를 String+Comma 형태로 변환 후 VO에 세팅 */
	public void setMultiParamToVo(HttpServletRequest request, CommonVo commonVo, String brdId) throws SQLException{
		// 컬럼표시여부 리스트
		List<BaColmDispDVo> baColmDispDVoList = bbBrdSvc.getBaColmDispDVoList(request, brdId, true, "Y", null, null, false);
		
		// 컬럼표시여부
		Map<String, BaColmDispDVo> colmMap = bbBrdSvc.getBaColmDispDVoListMap(null, baColmDispDVoList, false, true, null, null);
		Entry<String, BaColmDispDVo> entry;
		Iterator<Entry<String, BaColmDispDVo>> iterator = colmMap.entrySet().iterator();
		BaColmDispDVo baColmDispDVo;
		BaTblColmDVo baTblColmDVo;
		List<String> attrList=new ArrayList<String>();
		while(iterator.hasNext()){
			entry = iterator.next();
			baColmDispDVo=entry.getValue();
			baTblColmDVo=baColmDispDVo.getColmVo();
			if(baTblColmDVo.getColmTyp()==null || !ArrayUtil.isInArray(new String[]{"CODECHK"}, baTblColmDVo.getColmTyp())) continue;
			attrList.add(baTblColmDVo.getColmNm().toLowerCase());
		}
		
		if(attrList.size()>0){
			String[] attrs=null;
			for(String attrId : attrList){
				attrs=request.getParameterValues(attrId);
				if(attrs!=null)
					VoUtil.setValue(commonVo, attrId, StringUtils.join(attrs, ','));
				else
					VoUtil.setValue(commonVo, attrId, "");
			}
		}
	}
	
	/** json 형식 컬럼 세팅 */
	public void setChkJsonColList(Map<String, BaColmDispDVo> colmMap, List<String> attrList, Map<String,String> attDtlMap){
		Entry<String, BaColmDispDVo> entry;
		Iterator<Entry<String, BaColmDispDVo>> iterator = colmMap.entrySet().iterator();
		BaColmDispDVo baColmDispDVo;
		BaTblColmDVo baTblColmDVo;
		
		while(iterator.hasNext()){
			entry = iterator.next();
			baColmDispDVo=entry.getValue();
			baTblColmDVo=baColmDispDVo.getColmVo();
			if(baTblColmDVo.getColmTyp()==null || !ArrayUtil.isInArray(new String[]{"USER", "DEPT"}, baTblColmDVo.getColmTyp())) continue;
			attrList.add(baTblColmDVo.getColmNm().toLowerCase());
			attDtlMap.put(baTblColmDVo.getColmNm().toLowerCase(), baTblColmDVo.getColmTyp());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setVoJsonToMap(String langTypCd, BbBullLVo bbBullLVo, List<String> attrList, Map<String,String> attDtlMap) throws SQLException{
		if(attrList.size()>0){
			Map<String,Object> exColMap=new HashMap<String,Object>();
			String value=null;
			Object obj=null;
			List<Map<String,Object>> jsonList=null;
			List<String> idList=null;
			String attTyp=null;
			List<Map<String,String>> dtlList=null;
			Map<String,String> dtlMap=null;
			for(String attrId : attrList){
				value=(String)VoUtil.getValue(bbBullLVo, attrId);
				if(value==null || value.isEmpty()) continue;
				obj=JsonUtil.jsonToObj(value);
				if(obj==null) continue;
				attTyp=attDtlMap.get(attrId);
				
				jsonList=(List<Map<String,Object>>)obj;
				idList=new ArrayList<String>();
				for(Map<String,Object> map :  jsonList){
					if("USER".equals(attTyp)) idList.add((String)map.get("userUid"));
					else if("DEPT".equals(attTyp)) idList.add((String)map.get("orgId"));
					else continue;
				}
				if(idList.size()>0){
					dtlList=null;
					if("USER".equals(attTyp)){
						// 사용자기본(OR_USER_B) 테이블 조회 후 맵으로 전환
						OrUserBVo orUserBVo = new OrUserBVo();
						orUserBVo.setQueryLang(langTypCd);
						orUserBVo.setUserUidList(idList);
						List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
						if(orUserBVoList!=null){
							dtlList=new ArrayList<Map<String,String>>();
							for(OrUserBVo storedOrUserBVo : orUserBVoList){
								dtlMap=new HashMap<String,String>();
								dtlMap.put("id", storedOrUserBVo.getUserUid());
								dtlMap.put("rescNm", storedOrUserBVo.getRescNm());
								dtlList.add(dtlMap);
							}
						}
					}else if("DEPT".equals(attTyp)){
						dtlList=new ArrayList<Map<String,String>>();
						for(String orgId : idList){
							String rescNm=orCmSvc.getOrgRescNmByOrgTypCd(orgId, "D", langTypCd);
							if(rescNm!=null){
								dtlMap=new HashMap<String,String>();
								dtlMap.put("id", orgId);
								dtlMap.put("rescNm", rescNm);
								dtlList.add(dtlMap);
							}
						}
					}
					if(dtlList!=null) exColMap.put(attrId+"MapList", dtlList);
				}
				
			}
			if(exColMap!=null && exColMap.size()>0){
				bbBullLVo.setExColMap(exColMap);
			}
		}
	}
	
	/** json to map */
	public void setColListJsonToMap(String langTypCd, Map<String, BaColmDispDVo> colmMap, List<BbBullLVo> bbBullLVoList, BbBullLVo bbBullLVo) throws SQLException{
		List<String> attrList=new ArrayList<String>();
		Map<String,String> attDtlMap=new HashMap<String,String>();
		setChkJsonColList(colmMap, attrList, attDtlMap);
		if(attrList.size()==0) return;
		if(bbBullLVoList!=null){
			for(BbBullLVo storedBbBullLVo : bbBullLVoList){
				setVoJsonToMap(langTypCd, storedBbBullLVo, attrList, attDtlMap);
			}
		}else if(bbBullLVo!=null){
			setVoJsonToMap(langTypCd, bbBullLVo, attrList, attDtlMap);
		}
	}
	
	/** 정렬순서 적용 */
	public void setSortOrdr(List<BaColmDispDVo> baColmDispDVoList, BbBullLVo bbBullLVo){
		if(bbBullLVo.getOrderBy() != null && !bbBullLVo.getOrderBy().isEmpty()) return;
		
		BaColmDispDVo baColmDispDVo=null;
		// 정렬순서용 설정 찾기
		for(BaColmDispDVo storedBaColmDispDVo: baColmDispDVoList){
			if(storedBaColmDispDVo.getDataSortVa() != null && !storedBaColmDispDVo.getDataSortVa().isEmpty()){
				baColmDispDVo = storedBaColmDispDVo;
				break;
			}
		}
		// 정렬순서 세팅
		if(baColmDispDVo!=null){
			// 컬럼 데이터
			BaTblColmDVo colmVo=baColmDispDVo.getColmVo();
			
			// 항목구분[폴더,분류,텍스트,코드,에디터,달력UID]
			String colmTypVal = colmVo.getColmTypVal();
			String sortOptVa = baColmDispDVo.getSortOptVa();//정렬옵션값 - code:코드 테이블의 정렬순서, name:텍스트순
			String dataSortVa = baColmDispDVo.getDataSortVa();//데이터정렬값 - asc:내림차순, desc:올림차순
			dataSortVa = dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase();
			String column = colmVo.getColmNm();
			if("code".equals(sortOptVa)){
				bbBullLVo.setDispOrdr("(SELECT SORT_ORDR FROM BA_CD_D WHERE CD_GRP_ID ='"+colmTypVal+"' AND CD_ID=T."+column+")");
				bbBullLVo.setOrderBy("DISP_ORDR "+dataSortVa);
			}else {
				bbBullLVo.setOrderBy(column+" "+(dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase()));
			}
		}
	}
	
	/** 조회조건 적용 */
	public void setListCondApply(HttpServletRequest request, ModelMap model, BaBrdBVo baBrdBVo, List<BaColmDispDVo> baColmDispDVoList, BbBullLVo paramBullVo) throws SQLException, CmException{
		if(baBrdBVo.getOptMap()==null || baBrdBVo.getOptMap().get("listCondApplyYn")==null || "N".equals(baBrdBVo.getOptMap().get("listCondApplyYn")) || !"N".equals(baBrdBVo.getBrdTypCd()))
			return;
		
		// 목록 조회 컬럼
		List<BaColmDispDVo> dispList = bbBrdSvc.getColDispList(baColmDispDVoList, "Y", null, true, true);
		if(dispList==null || dispList.size()==0)
			return;
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 기본 목록 맵
		Map<String,Object> baseList = new HashMap<String,Object>();
		
		// 텍스트 콤보 목록
		List<String[]> textOptList=null;
		// 날짜 콤보 목록
		List<String[]> dateOptList=null;
		
		// 코드 콤보 목록(코드그룹)
		List<String[]> codeOptList=null;
				
		boolean isExColm=false;
		String[][] textCols=new String[][]{{"SUBJ", "CONT", "REGR_UID", "MODR_UID"}, {"TEXT", "TEXTAREA", "USER", "DEPT"}}; // [0] : 컬럼명, [1] : 컬럼타입
		String[][] dateCols=new String[][]{{"REG_DT", "MOD_DT"}, {"CALENDAR", "CALENDARTIME"}}; // [0] : 컬럼명, [1] : 컬럼타입
		String[] codeCols=new String[]{"CODE","CODECHK","CODERADIO"}; // [0] : 컬럼명, [1] : 컬럼타입
		
		// 상세 조회 목록
		List<String[]> dtlList=new ArrayList<String[]>();
		
		// 전체 조회 목록
		List<String[]> totalSrchList=new ArrayList<String[]>();
				
		String title,va, colmNm, colmTyp, colmTypVal;
		for(BaColmDispDVo dispVo : dispList){
			colmNm=dispVo.getColmVo().getColmNm();
			isExColm=dispVo.getColmVo().getExColmYn()!=null && "Y".equals(dispVo.getColmVo().getExColmYn());
			title=dispVo.getColmVo().getRescNm();
			va=StringUtil.toCamelNotation(colmNm, false);
			if((!isExColm && ArrayUtil.isInArray(textCols[0], colmNm)) || 
					(isExColm && ArrayUtil.isInArray(textCols[1], dispVo.getColmVo().getColmTyp()))){ // 텍스트 컬럼
				if(textOptList==null) textOptList=new ArrayList<String[]>();
				textOptList.add(new String[]{va,title });
			}
			
			if((!isExColm && ArrayUtil.isInArray(dateCols[0], colmNm)) || 
					(isExColm && ArrayUtil.isInArray(dateCols[1], dispVo.getColmVo().getColmTyp()))){ // 날짜 컬럼
				if(dateOptList==null) dateOptList=new ArrayList<String[]>();
				dateOptList.add(new String[]{va,title });
			}
			
			colmTypVal=dispVo.getColmVo().getColmTypVal();
			if(ServerConfig.IS_MOBILE && isExColm && ArrayUtil.isInArray(codeCols, dispVo.getColmVo().getColmTyp())){ // 코드 컬럼
				if(codeOptList==null) codeOptList=new ArrayList<String[]>();
				codeOptList.add(getBaCdGrpBVos(langTypCd, title, colmTypVal, dispVo.getAtrbId()));
			}
			colmTyp=dispVo.getColmVo().getColmTyp();
			if(!ServerConfig.IS_MOBILE){ 
				if(!isExColm){
					if(ArrayUtil.isInArray(new String[]{"SUBJ","CONT"}, colmNm))
						dtlList.add(new String[]{va, title, "TEXT"});
					else if(ArrayUtil.isInArray(new String[]{"REGR_UID","MODR_UID"}, colmNm))
						dtlList.add(new String[]{va, title, "USER"});
					else if(ArrayUtil.isInArray(new String[]{"REG_DT","MOD_DT"}, colmNm))
						dtlList.add(new String[]{va, title, "CALENDAR"});
				}
				if(isExColm){				
					dtlList.add(new String[]{va, title, colmTyp});
				}
			}
			/*if(ServerConfig.IS_MOBILE && dispVo.getMbTitlColYn()!=null && "Y".equals(dispVo.getMbTitlColYn())){ // 모바일 대표컬럼
				model.put("titleDispVo", dispVo);
			}*/
			totalSrchList.add(new String[]{va, title, colmTyp, colmNm, colmTypVal});
			
		}
		
		if(textOptList!=null)
			baseList.put("textOptList", textOptList);
		if(dateOptList!=null)
			baseList.put("dateOptList", dateOptList);
		if(codeOptList!=null)
			baseList.put("codeOptList", codeOptList);
		
		if(dtlList!=null){
			String queryString = ParamUtil.getQueryString(request, "menuId", "brdId", "compId", "srchDetl");
			try{
				model.put("paramMap", ParamUtil.getMapFromQueryString(queryString)); // 조회조건 맵
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
		
		model.put("baseList", baseList); // 기본 조회조건
		model.put("dtlList", dtlList); // 상세 조회조건
		model.put("listCondApplyYn", Boolean.TRUE); // 목록 조회조건 적용여부
		
		if(paramBullVo!=null){
			// 상세조회여부
            String srchDetl = ParamUtil.getRequestParam(request, "srchDetl", false);
            if(srchDetl==null || srchDetl.isEmpty()) srchDetl="N";
            
        	String[] dateEq=new String[]{"CALENDAR"}; // [0] : 컬럼명, [1] : 컬럼타입
        	String[] timeEq=new String[]{"CALENDARTIME"}; // [0] : 컬럼명, [1] : 컬럼타입
        	String[] uidEq=new String[]{"REGR_UID", "MODR_UID"}; // [0] : 컬럼명, [1] : 컬럼타입
        	String[] dateTimeEq=new String[]{"REG_DT", "MOD_DT"}; // [0] : 컬럼명, [1] : 컬럼타입
        	
            // 목록조회 맵
            Map<String,List<String[]>> paramMap=null;
            List<String[]> paramList=null;
        	String key;
        	
            if("Y".equals(srchDetl)){ // 상세조회일 경우
            	String[][] textLike=new String[][]{{"SUBJ", "CONT"}, {"TEXT", "TEXTAREA", "USER", "DEPT"}}; // [0] : 컬럼명, [1] : 컬럼타입
            	for(String[] dispVo : totalSrchList){
    				va=request.getParameter(dispVo[0]);
    				if(va!=null && !va.isEmpty()){ // 파라미터 값이 있을경우
    					if(ArrayUtil.isInArray(uidEq, dispVo[3])){
    						//VoUtil.setValue(paramBullVo, dispVo[0], va);
    						continue;
    					}
    					if(ArrayUtil.isInArray(dateTimeEq, dispVo[3])){
    						continue;
    					}
    					
    					if(paramMap==null) paramMap=new HashMap<String,List<String[]>>();
    					
    					if(ArrayUtil.isInArray(textLike[0], dispVo[3]) || ArrayUtil.isInArray(textLike[1], dispVo[2]))
    						key="TEXT_LIKE";
    					else if(ArrayUtil.isInArray(dateEq, dispVo[2]))
    						key="DATE";
    					else if(ArrayUtil.isInArray(timeEq, dispVo[2]))
    						key="DATE_TIME";
    					else if(dispVo[2].startsWith("CODE"))
    						key="CODE";
    					else continue;
    					if(paramMap.get(key)==null) paramList=new ArrayList<String[]>();
						else paramList=paramMap.get(key);
    					paramList.add(new String[]{dispVo[3], va});
    					paramMap.put(key, paramList);
    				}
    			}
            }else{
            	String[][] textLike=new String[][]{{"SUBJ", "CONT"}, {"TEXT", "TEXTAREA", "USER", "DEPT"}}; // [0] : 컬럼명, [1] : 컬럼타입
            	String[] codeEq=new String[]{"CODE", "CODECHK", "CODERADIO"}; // [0] : 컬럼명, [1] : 컬럼타입
            	
            	String schCat=ParamUtil.getRequestParam(request, "schCat", false);
            	String schWord=ParamUtil.getRequestParam(request, "schWord", false);
        		
            	String durCat=ParamUtil.getRequestParam(request, "durCat", false);
            	String durStrtDt=ParamUtil.getRequestParam(request, "durStrtDt", false);
            	String durEndDt=ParamUtil.getRequestParam(request, "durEndDt", false);
            	
            	// 코드
            	String cdCat = ParamUtil.getRequestParam(request, "cdCat", false);
            	String schCd = ParamUtil.getRequestParam(request, "schCd", false);
            	
            	boolean isSchCat=schCat!=null && !schCat.isEmpty() && schWord!=null && !schWord.isEmpty();
            	boolean isDurCat=durCat!=null && !durCat.isEmpty() && ((durStrtDt!=null && !durStrtDt.isEmpty()) || (durEndDt!=null && !durEndDt.isEmpty()));
            	boolean isCdCat=cdCat!=null && !cdCat.isEmpty() && schCd!=null && !schCd.isEmpty();
            	String[] cdCats = cdCat!=null && !cdCat.isEmpty() ? cdCat.split("_") : null;
            	if(isSchCat || isDurCat || isCdCat){
	            	for(String[] dispVo : totalSrchList){
	            		if(isSchCat && schCat.equals(dispVo[0])){
	            			if(ArrayUtil.isInArray(uidEq, dispVo[3])){
	    						VoUtil.setValue(paramBullVo, dispVo[0].replace("Uid","")+"Nm", schWord);
	    						continue;
	    					}
	            			if(paramMap==null) paramMap=new HashMap<String,List<String[]>>();
	            			if(ArrayUtil.isInArray(textLike[0], dispVo[3]) || ArrayUtil.isInArray(textLike[1], dispVo[2]))
	    						key="TEXT_LIKE";	            			
	    					else continue;
	            			if(paramMap.get(key)==null) paramList=new ArrayList<String[]>();
							else paramList=paramMap.get(key);
	    					paramList.add(new String[]{dispVo[3], schWord});
	    					paramMap.put(key, paramList);
	            		}else if(isDurCat && durCat.equals(dispVo[0])){
	            			if(ArrayUtil.isInArray(dateTimeEq, dispVo[3]))
	            				continue;
	            			if(paramMap==null) paramMap=new HashMap<String,List<String[]>>();
	            			if(ArrayUtil.isInArray(dateEq, dispVo[2]))
	    						key="DATE_FROM";
	    					else if(ArrayUtil.isInArray(timeEq, dispVo[2]))
	    						key="DATE_TIME_FROM";
	    					else continue;
	            			if(paramMap.get(key)==null) paramList=new ArrayList<String[]>();
							else paramList=paramMap.get(key);
	    					paramList.add(new String[]{dispVo[3], durStrtDt, durEndDt});
	    					paramMap.put(key, paramList);
	            		}else if(isCdCat && ArrayUtil.isInArray(codeEq, dispVo[2]) && cdCats[0].equals(dispVo[4]) && cdCats[1].equals(dispVo[0])){
	            			key="CODE";
	            			if(paramMap==null) paramMap=new HashMap<String,List<String[]>>();
	            			if(paramMap.get(key)==null) paramList=new ArrayList<String[]>();
							else paramList=paramMap.get(key);
	            			paramList.add(new String[]{dispVo[3], schCd});
	    					paramMap.put(key, paramList);
	            		}
	            	}
            	}
            	
            	if(cdCats!=null){
            		baseList.put("codeSelectList", getBaCdDVoList(langTypCd, cdCats[0]));
            	}

            }
            
            paramBullVo.setListCondApply(true); // 목록조건 적용여부
            
            if(paramMap!=null)
            	paramBullVo.setParamMap(paramMap);
            
         // DEBUG
            /*if(paramMap!=null){
	    		Entry<String, List<String[]>> entry;
    			Iterator<Entry<String, List<String[]>>> iterator = paramMap.entrySet().iterator();
    			
    			List<String[]> tmpList=null;
    			while(iterator.hasNext()){
    				entry = iterator.next();
    				tmpList=entry.getValue();
    				for(String[] tmp : tmpList){
    					for(String tt : tmp){
    						System.out.println("tt : "+tt);
    					}
    				}
    				System.out.println("===============================================");
    			}
            }*/
		}
		
	}
	
	/** 목록을 맵으로 변환 */
	public List<Map<String,Object>> getListToMap(ModelMap model, BaBrdBVo baBrdBVo, List<BbBullLVo> bbBullLVoList){
		if(baBrdBVo.getOptMap()==null || baBrdBVo.getOptMap().get("listCondApplyYn")==null || "N".equals(baBrdBVo.getOptMap().get("listCondApplyYn")) || !"N".equals(baBrdBVo.getBrdTypCd()) || bbBullLVoList==null || bbBullLVoList.size()==0)
			return null;
		List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		for(BbBullLVo storedBbBullLVo : bbBullLVoList){
			mapList.add(VoUtil.toMap(storedBbBullLVo, null));
		}
		return mapList;
	}
	
	/** 코드그룹 조회 */
	public String[] getBaCdGrpBVos(String langTypCd, String rescNm, String cdGrpId, String atrbId) throws SQLException{
		// 코드그룹(BA_CD_GRP_B) 테이블 - BIND
		BaCdGrpBVo baCdGrpBVo = new BaCdGrpBVo();
		baCdGrpBVo.setQueryLang(langTypCd);
		baCdGrpBVo.setCdGrpId(cdGrpId);
		
		baCdGrpBVo = (BaCdGrpBVo)commonDao.queryVo(baCdGrpBVo);
		if(baCdGrpBVo!=null)
			return new String[]{baCdGrpBVo.getCdGrpId(), rescNm!=null ? rescNm : baCdGrpBVo.getCdGrpNm(), atrbId};
		return null;
		
	}
	
	/** 코드상세 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<BaCdDVo> getBaCdDVoList(String langTypCd, String cdGrpId) throws SQLException{
		// 코드상세(BA_CD_D) 테이블 - BIND
		BaCdDVo baCdDVo = new BaCdDVo();
		baCdDVo.setQueryLang(langTypCd);
		baCdDVo.setCdGrpId(cdGrpId);
		baCdDVo.setUseYn("Y");
		return (List<BaCdDVo>) commonDao.queryList(baCdDVo);
	}
	
	
	/** 대표 컬럼 제목으로 세팅 */
	public void setBbBullLVoSubj(String langTypCd, BaBrdBVo baBrdBVo, BbBullLVo bbBullLVo, BaColmDispDVo firstVo, String titleColNm) {
		if(baBrdBVo.getOptMap()==null || baBrdBVo.getOptMap().get("listCondApplyYn")==null || 
				"N".equals(baBrdBVo.getOptMap().get("listCondApplyYn")) || !"N".equals(baBrdBVo.getBrdTypCd()) || 
				firstVo==null || bbBullLVo==null)
			return;
		
		String atrbId=firstVo.getAtrbId();
		BaTblColmDVo colmVo = firstVo.getColmVo();
		Map<String,Object> voMap = VoUtil.toMap(bbBullLVo, null);
		String value=(String)(voMap.get(atrbId)+"");
		String returnValue="";
		if(colmVo.getColmTyp()!=null && "CALENDAR".equals(colmVo.getColmTyp())){
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
			for(int i=0;i<5;i++){
				if((i+1)<=Integer.parseInt(value)) returnValue+="★";
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
						@SuppressWarnings("unchecked")
						List<BaCdDVo> baCdDVoList = (List<BaCdDVo>) commonDao.queryList(baCdDVo);
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
							@SuppressWarnings("unchecked")
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
		if(titleColNm!=null)
			VoUtil.setValue(bbBullLVo, titleColNm, returnValue);
		else
			bbBullLVo.setSubj(returnValue);
	}
	
	/** 조회조건 추가 [회사 및 계열사]*/
	public void setCompAffiliateIdList(String compId, String langTypCd, BbBullLVo bbBullLVo) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(sysPlocMap.get("affiliatesEnable")==null || !"Y".equals(sysPlocMap.get("affiliatesEnable"))){
			bbBullLVo.setCompId(null);
			return;
		}
		
		PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
		if(ptCompBVo!=null && ptCompBVo.getAffiliateIds()!=null){
			List<String> affiliateIds=ptCompBVo.getAffiliateIds();
			affiliateIds.add(compId);
			// HashSet 으로 중복ID 제거
			Set<String> hs = new HashSet<String>(affiliateIds);
			affiliateIds = new ArrayList<String>(hs);
			bbBullLVo.setCompId(null);
			bbBullLVo.setCompIdList(affiliateIds);
		}else{
			bbBullLVo.setCompId(compId);
		}
	}
	
	/** pc알림 가능 여부 */
	public boolean isPcNoti(HttpServletRequest request, UserVo userVo) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean pcNotiEnable = "Y".equals(sysPlocMap.get("pcNotiEnable")) && "Y".equals(sysPlocMap.get("pcNotiBB"));
		
		// 메뉴 관리자 여부
		boolean isMnuAdmin = SecuUtil.hasAuth(request, "A", userVo.getUserUid());
		
		return pcNotiEnable && isMnuAdmin;
	}
	
	/** pc알림 사용자 목록 조회 */
	public List<String> getNotiUserList(HttpServletRequest request, UserVo userVo) throws SQLException{
		
		List<String> returnList=null;
		
		String[] userUids = request.getParameterValues("userUid");
		
		if(userUids!=null) returnList = Arrays.asList(userUids);
		
		String[] orgIds = request.getParameterValues("orgId");
		String[] withSubYns = request.getParameterValues("withSubYn");
		
		if(returnList==null) returnList=new ArrayList<String>();
		
		if (orgIds != null && orgIds.length > 0) {
			List<String> orgIdList=new ArrayList<String>();
			List<String> subOrgList = null;
			for (int i = 0; i < orgIds.length; i++) {
				orgIdList.add(orgIds[i]); // 선택된 조직ID
				if(withSubYns[i]!=null && "Y".equals(withSubYns[i])){ //하위부서포함여부
					subOrgList = orCmSvc.getOrgSubIdList(orgIds[i], "ko");
					if(subOrgList!=null) {
						orgIdList.addAll(subOrgList);
					}
				}				
			}
			if(orgIdList.size()>0){
				orgIdList=new ArrayList<String>(new HashSet<String>(orgIdList));
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserStatCd("02");
				orUserBVo.setOrgIdList(orgIdList);//조직ID 목록
				
				// 목록 조회
				@SuppressWarnings("unchecked")
				List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
				if(orUserBVoList!=null){
					for(OrUserBVo storedOrUserBVo : orUserBVoList){
						returnList.add(storedOrUserBVo.getUserUid());
					}
				}
			}
		}
		// UserUid 중복제거
		if(returnList!=null && returnList.size()>0)
			returnList=new ArrayList<String>(new HashSet<String>(returnList));
		
		if(userUids==null && orgIds==null){
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setUserStatCd("02"); // 근무중
			orUserBVo.setCompId(userVo.getCompId());
			
			// 목록 조회
			@SuppressWarnings("unchecked")
			List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
			if(orUserBVoList!=null){
				for(OrUserBVo storedOrUserBVo : orUserBVoList){
					returnList.add(storedOrUserBVo.getUserUid());
				}
			}
		}
		
		return returnList;
	}
	
	/** 알림 발송 */
	public void sendNoti(HttpServletRequest request, BaBrdBVo baBrdBVo, UserVo userVo, String mdId) throws SQLException{		
		if(!(isPcNoti(request, userVo) && baBrdBVo.getOptMap()!=null && baBrdBVo.getOptMap().get("bbOptYn")!=null 
				&& "Y".equals(baBrdBVo.getOptMap().get("bbOptYn")))){
			return;
		}		
		// 게시대상에서 pc 알림 여부
		String pcNotiYn = (String)request.getParameter("pcNotiYn");
		if(pcNotiYn!=null && "Y".equals(pcNotiYn)){ // pc알림여부가 'Y'일 경우
			List<String> notiUserList=getNotiUserList(request, userVo);
			String subj = (String)request.getParameter("subj"); // 제목
			// bb.msg.not.subj=제목없음
			if(subj==null || subj.isEmpty()) subj=messageProperties.getMessage("bb.msg.not.subj", request);
			
			// pt.sysopt.pcNoti.BB=게시
			String title=messageProperties.getMessage("pt.sysopt.pcNoti.BB", request);
			
			QueryQueue queryQueue=new QueryQueue();
			
			// pc 알림 목록
			List<Map<String, String>> pcNotiList=null;
			Map<String, String> pcNotiData;
			
			String pushMsgId=null;
			if(notiUserList!=null && notiUserList.size()>0){ // 알림대상자에게 발송
				pcNotiList=new ArrayList<Map<String, String>>();
				for(String userUid : notiUserList){
					pushMsgId=savePushMsg(queryQueue, userVo, baBrdBVo.getBrdId(), userUid, mdId, subj);
					pcNotiData=new HashMap<String, String>();
					pcNotiData.put("uid", userUid);
					pcNotiData.put("title", title);
					pcNotiData.put("body", subj);
					pcNotiData.put("url", "/index.do?msgId="+pushMsgId);
					pcNotiList.add(pcNotiData);
					
				}
			}else{ // 소속 회사
				pushMsgId=savePushMsg(queryQueue, userVo, baBrdBVo.getBrdId(), userVo.getCompId(), mdId, subj);
			}
			if(!queryQueue.isEmpty()){
				commonDao.execute(queryQueue);
				// PC 알림 발송
				if(pcNotiList==null){
					ptWebPushMsgSvc.setWebPushByCompId(userVo.getCompId(), title, subj, "/index.do?msgId="+pushMsgId);
				}else{
					ptWebPushMsgSvc.sendAsyncWebPush(pcNotiList);
				}
			}
		}
		
	}
	
	/** 푸쉬 데이터 저장 */
	public String savePushMsg(QueryQueue queryQueue, UserVo userVo, String brdId, String userUid, String mdId, String subj){
		PtPushMsgDVo ptPushMsgDVo;
		String pushMsgId = StringUtil.getNextHexa(24);
		
		ptPushMsgDVo = new PtPushMsgDVo();
		ptPushMsgDVo.setPushMsgId(pushMsgId);
		ptPushMsgDVo.setLangTypCd(userVo.getLangTypCd());
		ptPushMsgDVo.setMdRid("BB");
		ptPushMsgDVo.setMdId(mdId);
		ptPushMsgDVo.setPushSubj(subj);
		
		ptPushMsgDVo.setWebUrl("/bb/viewBull.do?brdId="+brdId+"&bullId="+mdId);
		ptPushMsgDVo.setWebAuthUrl("/bb/listBull.do?brdId="+brdId);
		ptPushMsgDVo.setMobUrl("/bb/viewBull.do?brdId="+brdId+"&bullId="+mdId);
		ptPushMsgDVo.setMobAuthUrl("/bb/listBull.do?brdId="+brdId);
		
		ptPushMsgDVo.setUserUid(userUid);
		//ptPushMsgDVo.setCompId(data.get("compId"));
		
		ptPushMsgDVo.setIsuDt("sysdate");
		
		// 푸쉬메시지상세(PT_PUSH_MSG_D) 테이블 - INSERT
		queryQueue.insert(ptPushMsgDVo);
		return pushMsgId;
	}
}
