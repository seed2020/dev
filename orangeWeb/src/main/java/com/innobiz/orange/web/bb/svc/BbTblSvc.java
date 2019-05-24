package com.innobiz.orange.web.bb.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.bb.vo.BaRescBVo;
import com.innobiz.orange.web.bb.vo.BaTblBVo;
import com.innobiz.orange.web.bb.vo.BaTblColmDVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.IdUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

/** 테이블관리 서비스 */
@Service
public class BbTblSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BbTblSvc.class);

	/** 테이블명 prefix */
	public static final String tblNmPrefix = "BB_";

	/** 테이블명 suffix */
	public static final String tblNmSuffix = "_L";

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 게시판 공통 서비스 */
	@Autowired
	private BbCmSvc bbCmSvc;

	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "bbRescSvc")
	private BbRescSvc bbRescSvc;
	
	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 테이블관리(BA_TBL_B) 테이블 - SELECT */
	public BaTblBVo getBaTblBVo(String langTypCd, String tblId) throws SQLException {
		BaTblBVo baTblBVo = new BaTblBVo();
		baTblBVo.setQueryLang(langTypCd);
		baTblBVo.setTblId(tblId);
		return (BaTblBVo) commonDao.queryVo(baTblBVo);
	}

	/** 테이블명 생성 */
	@SuppressWarnings("unchecked")
	public String createTblNm() throws SQLException {
		BaTblBVo bbTblBVo = new BaTblBVo();
		List<BaTblBVo> bbTblBVoList = (List<BaTblBVo>) commonDao.queryList(bbTblBVo);
		int maxIndex = 0;
		for (BaTblBVo baTblBVo : bbTblBVoList) {
			String tblNm = baTblBVo.getTblNm();
			if(tblNm==null) continue;
			Integer index = IdUtil.toInt(tblNm.substring(1));
			if (index > maxIndex) {
				maxIndex = index;
			}
		}
		return 'B' + IdUtil.createId(maxIndex+1, 4);
	}

	/** 중복된 테이블명인가? */
	private boolean isTblNmExist(HttpServletRequest request, String tblNm) throws SQLException {

		BaTblBVo bbTblBVo = new BaTblBVo();
		bbTblBVo.setSchCat("TBL_NM");
		bbTblBVo.setSchWord(tblNm);

		return (commonDao.count(bbTblBVo) > 0);
	}

	/** 테이블컬럼 바인드, 저장 */
	@SuppressWarnings("unchecked")
	private List<BaTblColmDVo> makeColmVoList(HttpServletRequest request, BaTblBVo baTblBVo, QueryQueue queryQueue) throws SQLException {

		List<BaTblColmDVo> colmVoList = new ArrayList<BaTblColmDVo>();
		
		// 기본 컬럼 생성, 저장
		colmVoList.add(makeColmVo(request, queryQueue, "SUBJ", "cols.subj", "NVARCHAR", 240, "Y", "N"));                // 제목
		colmVoList.add(makeColmVo(request, queryQueue, "CAT_ID", "cols.cat", "VARCHAR", 30, "Y", "N"));                 // 카테고리ID
		colmVoList.add(makeColmVo(request, queryQueue, "BULL_REZV_DT", "cols.bullRezvDt", "DATETIME", null, "N", "N")); // 게시예약일시
		colmVoList.add(makeColmVo(request, queryQueue, "BULL_EXPR_DT", "cols.bullExprDt", "DATETIME", null, "N", "N")); // 게시완료일시
		colmVoList.add(makeColmVo(request, queryQueue, "CONT", "cols.cont", "TEXT", null, "N", "N"));                   // 본문
		colmVoList.add(makeColmVo(request, queryQueue, "REGR_UID", "cols.regr", "VARCHAR", 30, "Y", "N"));              // 등록자
		colmVoList.add(makeColmVo(request, queryQueue, "REG_DT", "cols.regDt", "DATETIME", null, "Y", "N"));            // 등록일시
		colmVoList.add(makeColmVo(request, queryQueue, "MODR_UID", "cols.modr", "VARCHAR", 30, "N", "N"));              // 수정자
		colmVoList.add(makeColmVo(request, queryQueue, "MOD_DT", "cols.modDt", "DATETIME", null, "N", "N"));            // 수정일시
		colmVoList.add(makeColmVo(request, queryQueue, "READ_CNT", "cols.readCnt", "INT", null, "Y", "N"));             // 조회수
		colmVoList.add(makeColmVo(request, queryQueue, "PROS_CNT", "cols.prosCnt", "INT", null, "N", "N"));             // 찬성수
		colmVoList.add(makeColmVo(request, queryQueue, "CONS_CNT", "cols.consCnt", "INT", null, "N", "N"));             // 반대수
		colmVoList.add(makeColmVo(request, queryQueue, "RECMD_CNT", "cols.recmdCnt", "INT", null, "N", "N"));           // 추천수
		colmVoList.add(makeColmVo(request, queryQueue, "SCRE", "cols.scre", "INT", null, "N", "N"));                    // 점수
		//colmVoList.add(makeColmVo(request, queryQueue, "ODUR_UID", "cols.odurUid", "VARCHAR", 30, "N", "N"));                    // 원직자UID : 추가
		
		for (BaTblColmDVo baTblColmDVo : colmVoList) {
			// 테이블컬럼(BA_TBL_COLM_D) 테이블
			baTblColmDVo.setColmId(bbCmSvc.createId("BA_TBL_COLM_D"));
			baTblColmDVo.setTblId(baTblBVo.getTblId());
			
			// 테이블컬럼(BA_TBL_COLM_D) 테이블 - UPDATE or INSERT		
			queryQueue.insert(baTblColmDVo);
		}

		// 확장테이블인가?
		if ("Y".equals(baTblBVo.getExYn())) {
			// 테이블컬럼(BA_TBL_COLM_D) 테이블 - BIND
			List<BaTblColmDVo> boundColmVoList = (List<BaTblColmDVo>) VoUtil.bindList(request, BaTblColmDVo.class, new String[]{"valid"});
			
			
			
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT, 테이블컬럼(BA_TBL_COLM_D) 테이블 - BIND
			bbRescSvc.collectBaRescBVoList(request, queryQueue, boundColmVoList, "valid", "rescId", "colmDispNm");
			
			// 테이블컬럼(BA_TBL_COLM_D) 테이블 - INSERT
			for (BaTblColmDVo baTblColmDVo : boundColmVoList) {
				// 컬럼ID, 테이블ID
				baTblColmDVo.setColmId(bbCmSvc.createId("BA_TBL_COLM_D"));
				baTblColmDVo.setTblId(baTblBVo.getTblId());
				// 수정자, 수정일시
				baTblColmDVo.setModrUid(baTblBVo.getModrUid());
				baTblColmDVo.setModDt("sysdate");
				toDbmsDataType(baTblColmDVo);
				
				queryQueue.insert(baTblColmDVo);
			}
			
			colmVoList.addAll(boundColmVoList);
		}
		
		return colmVoList;
	}

	/** 컬럼VO(BaTblColmDVo) 생성, 바인드, 저장 */
	private BaTblColmDVo makeColmVo(HttpServletRequest request, QueryQueue queryQueue, String colmNm, String titleId,
			String dataTyp, Integer colmLen, String listDispYn, String exColmYn) throws SQLException {
		
		BaTblColmDVo baTblColmDVo = new BaTblColmDVo();
		baTblColmDVo.setColmNm(colmNm);
		
		baTblColmDVo.setDataTyp(dataTyp);
		baTblColmDVo.setColmLen(colmLen);
		toDbmsDataType(baTblColmDVo);
		
		baTblColmDVo.setExColmYn(exColmYn);
		baTblColmDVo.setRegDispYn("Y");
		baTblColmDVo.setModDispYn("Y");
		baTblColmDVo.setReadDispYn("Y");
		baTblColmDVo.setListDispYn(listDispYn);

		BaRescBVo baRescBVo = bbRescSvc.setBaRescBVo(request, titleId, queryQueue);
		
		// 리소스 조회 후 리소스ID와 리소스명 세팅
		baTblColmDVo.setRescId(baRescBVo.getRescId());
		baTblColmDVo.setColmDispNm(baRescBVo.getRescVa());
		
		return baTblColmDVo;
	}
	
	/** 게시물 테이블 - CREATE */
	private void createTbl(String tblNm, List<BaTblColmDVo> colmVoList) throws SQLException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("createTbl called - tblNm = " + tblNm);
		}
		
		BaTblBVo baTblBVo = new BaTblBVo();
		baTblBVo.setTblNm(getFullTblNm(tblNm));
		baTblBVo.setColmVoList(colmVoList);

		try {
			// 게시물(BB_X000X_L) 테이블 - CREATE
			if(isOracle()){
				baTblBVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaTblBDao.createBbBullL_oracle1");
				commonDao.update(baTblBVo);
				baTblBVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaTblBDao.createBbBullL_oracle2");
				commonDao.update(baTblBVo);
				baTblBVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaTblBDao.createBbBullL_oracle3");
				commonDao.update(baTblBVo);
			} else {
				baTblBVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaTblBDao.createBbBullL");
				commonDao.update(baTblBVo);
			}
		} catch (SQLException e) {
			throw e;
		}
	}

	/** 게시물 테이블 - DROP */
	private void dropTbl(String tblNm) throws SQLException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("dropTbl called - tblNm = " + tblNm);
		}
		
		BaTblBVo baTblBVo = new BaTblBVo();
		baTblBVo.setTblNm(getFullTblNm(tblNm));

		try {
			if(isOracle()){
				// 게시물(BB_X000X_L) 테이블 - DROP
				baTblBVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaTblBDao.dropBbBullL_oracle");
				commonDao.update(baTblBVo);
			} else {
				// 게시물(BB_X000X_L) 테이블 - DROP
				baTblBVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaTblBDao.dropBbBullL");
				commonDao.update(baTblBVo);
			}
		} catch (SQLException e) {
			throw e;
		}
	}

	/** 게시물 테이블 - ALTER */
	private void alterTbl(String tblNm, List<BaTblColmDVo> colmVoList, QueryQueue queryQueue) throws SQLException {
		if (colmVoList == null || colmVoList.size() == 0) return;

		String queryId = isOracle() ? 
				"com.innobiz.orange.web.bb.dao.BaTblBDao.alterBbBullL_oracle" : 
				"com.innobiz.orange.web.bb.dao.BaTblBDao.alterBbBullL";
		String fullTableNm = getFullTblNm(tblNm);
		
		// 게시물(BB_X000X_L) 테이블 - ALTER
		for(BaTblColmDVo baTblColmDVo : colmVoList){
			baTblColmDVo.setTblId(fullTableNm);
			baTblColmDVo.setInstanceQueryId(queryId);
			queryQueue.update(baTblColmDVo);
		}
	}

	/** 테이블 등록 */
	// @Transactional - 오라클에서 이상 있음
	//@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
	public void insertTbl(HttpServletRequest request, BaTblBVo baTblBVo, QueryQueue queryQueue)
			throws CmException, SQLException {

		// 중복된 테이블명인가?
		if (isTblNmExist(request, baTblBVo.getTblNm())) {
			// bb.msg.dupTblNm=중복된 테이블명입니다.
			throw new CmException("bb.msg.dupTblNm", request);
		}
		// 테이블ID 생성
		baTblBVo.setTblId(bbCmSvc.createId("BA_TBL_B"));
		// 테이블관리(BA_TBL_B) 테이블 - INSERT
		queryQueue.insert(baTblBVo);
		// 테이블컬럼 바인드, 저장, 리소스기본 테이블 저장
		List<BaTblColmDVo> colmVoList = makeColmVoList(request, baTblBVo, queryQueue);
		// 게시물 테이블 - CREATE
		createTbl(baTblBVo.getTblNm(), colmVoList);

		try {
			commonDao.execute(queryQueue);
		} catch (SQLException e) {
			// 게시물 테이블 - DROP
			dropTbl(baTblBVo.getTblNm());
			throw e;
		}
	}
	
	/** 테이블 기본 컬럼 매핑 맵 */
	public Map<String,String> getDftColmMap(){
		Map<String,String> returnMap = new HashMap<String,String>();
		returnMap.put("SUBJ", "cols.subj");                // 제목
		returnMap.put("CAT_ID", "cols.cat");                 // 카테고리ID
		returnMap.put("BULL_REZV_DT","cols.bullRezvDt"); // 게시예약일시
		returnMap.put("BULL_EXPR_DT","cols.bullExprDt"); // 게시완료일시
		returnMap.put("CONT", "cols.cont");                   // 본문
		returnMap.put("REGR_UID", "cols.regr");              // 등록자
		returnMap.put("REG_DT", "cols.regDt");            // 등록일시
		returnMap.put("MODR_UID", "cols.modr");              // 수정자
		returnMap.put("MOD_DT", "cols.modDt");            // 수정일시
		returnMap.put("READ_CNT", "cols.readCnt");             // 조회수
		returnMap.put("PROS_CNT", "cols.prosCnt");             // 찬성수
		returnMap.put("CONS_CNT", "cols.consCnt");             // 반대수
		returnMap.put("RECMD_CNT", "cols.recmdCnt");           // 추천수
		returnMap.put("SCRE", "cols.scre");                    // 점수
		return returnMap;
	}
	
	/** 기본 컬럼 언어별 리소스 추가 */
	public void setColmResc(HttpServletRequest request, UserVo userVo, QueryQueue queryQueue, String tblId) throws SQLException{
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 테이블관리 기본(BA_TBL_B) 테이블 - BIND
		BaTblBVo baTblBVo = new BaTblBVo();
		baTblBVo.setTblId(tblId);
		baTblBVo.setQueryLang(langTypCd);
		
		// 테이블관리 기본(BA_TBL_B) 테이블 - SELECT
		baTblBVo = (BaTblBVo) commonDao.queryVo(baTblBVo);
		if(baTblBVo != null && baTblBVo.getRescId() != null){
			BaRescBVo baRescBVo = new BaRescBVo();
			baRescBVo.setRescId(baTblBVo.getRescId());
			// 회사별 설정된 리소스의 어권 정보
			List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
			int j, size = langTypCdList == null ? 0 : langTypCdList.size();
			String[] langs = new String[size];
			for (j = 0; j < size; j++) {
				langs[j] = langTypCdList.get(j).getCd();
			}
			// 테이블컬럼(BA_TBL_COLM_D) 테이블 - SELECT
			BaTblColmDVo baTblColmDVo = new BaTblColmDVo();
			baTblColmDVo.setQueryLang(langTypCd);
			baTblColmDVo.setTblId(baTblBVo.getTblId());
			baTblColmDVo.setExColmYn("N"); //기본컬럼
			@SuppressWarnings("unchecked")
			List<BaTblColmDVo> colmVoList = (List<BaTblColmDVo>) commonDao.queryList(baTblColmDVo);
			// 기본 컬럼 맵
			Map<String,String> dftColmMap = getDftColmMap();		
			// 어권 만큼 돌면서
			BaRescBVo rescVo = null;
			for (j = 0; j < size; j++) {
				baRescBVo.setLangTypCd(langs[j]);
				// 어권별 컬럼 리소스가 없으면 추가한다.
				if(commonDao.count(baRescBVo) > 0) continue;
				for(BaTblColmDVo storedBaTblColmDVo:colmVoList){
					if(!dftColmMap.containsKey(storedBaTblColmDVo.getColmNm())) continue;
					rescVo = new BaRescBVo();
					rescVo.setRescId(storedBaTblColmDVo.getRescId());
					rescVo.setLangTypCd(langs[j]);
					if(commonDao.count(rescVo) > 0) continue;
					String rescVa = messageProperties.getMessage(dftColmMap.get(storedBaTblColmDVo.getColmNm()), SessionUtil.toLocale(langs[j]));
					if(rescVa != null && !rescVa.isEmpty()){
						rescVo.setRescVa(rescVa);
						queryQueue.insert(rescVo);
					}
				}
			}
		}
		
	}
	
	/** 테이블 수정 */
	@SuppressWarnings("unchecked")
	// @Transactional - 오라클에서 이상 있음
	//@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
	public List<BaTblColmDVo> updateTbl(HttpServletRequest request, BaTblBVo baTblBVo, QueryQueue queryQueue)
			throws SQLException {

		// 테이블관리(BA_TBL_B) 테이블 - UPDATE
		queryQueue.update(baTblBVo);
		
		String[] skipColList=new String[]{"CALENDAR", "CALENDARTIME", "TEXTAREA"};
		// 확장테이블인가?
		List<BaTblColmDVo> boundColmVoList = null, alterColmVoList = new ArrayList<BaTblColmDVo>();//alter
		
		List<BaTblColmDVo> addColmList = null;
		if ("Y".equals(baTblBVo.getExYn())) {
			
			// 테이블 컬럼 맵
			Map<String,BaTblColmDVo> baTblColmDVoMap = getBaTblColmDVoMap(baTblBVo.getTblId());
			
			// 테이블컬럼(BA_TBL_COLM_D) 테이블 - BIND
			boundColmVoList = (List<BaTblColmDVo>) VoUtil.bindList(request, BaTblColmDVo.class, new String[]{"valid"});
			
			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT, 테이블컬럼(BA_TBL_COLM_D) 테이블 - BIND
			bbRescSvc.collectBaRescBVoList(request, queryQueue, boundColmVoList, "valid", "rescId", "colmDispNm");
			
			// 테이블컬럼(BA_TBL_COLM_D) 테이블 - SELECT
			BaTblColmDVo storedColmVo;
			
			BaTblColmDVo dateBaTblColmDVo;
			String colmTyp;
			// 테이블컬럼(BA_TBL_COLM_D) 테이블
//			List<BaTblColmDVo> skipList = new ArrayList<BaTblColmDVo>();
			BaTblColmDVo newBaTblColmDVo=null;
			for (BaTblColmDVo baTblColmDVo : boundColmVoList) {
				// 테이블ID
				baTblColmDVo.setTblId(baTblBVo.getTblId());
				// 수정자, 수정일시
				baTblColmDVo.setModrUid(baTblBVo.getModrUid());
				baTblColmDVo.setModDt("sysdate");
				
				String deleted = baTblColmDVo.getDeleted();
				Integer bullCnt = baTblBVo.getBullCnt();
				if (baTblColmDVo.getColmId() == null || baTblColmDVo.getColmId().isEmpty()) {
					// 컬럼ID
					baTblColmDVo.setColmId(bbCmSvc.createId("BA_TBL_COLM_D"));
					// 컬럼 추가
//					baTblColmDVo.setAdded("Y");
					queryQueue.insert(baTblColmDVo);
					
					newBaTblColmDVo=new BaTblColmDVo();
					BeanUtils.copyProperties(baTblColmDVo, newBaTblColmDVo);
					
					newBaTblColmDVo.setAlter("ADD");
					alterColmVoList.add(newBaTblColmDVo);
					
					if(addColmList==null)
						addColmList = new ArrayList<BaTblColmDVo>();
					addColmList.add(baTblColmDVo);
				} else if ("Y".equals(deleted)) {
					// 리소스 삭제
					BaRescBVo rescVo = new BaRescBVo();
					rescVo.setRescId(baTblColmDVo.getRescId());
					queryQueue.delete(rescVo);
					// 컬럼 삭제
					queryQueue.delete(baTblColmDVo);
					
					newBaTblColmDVo=new BaTblColmDVo();
					BeanUtils.copyProperties(baTblColmDVo, newBaTblColmDVo);
					
					newBaTblColmDVo.setAlter("DROP");
					alterColmVoList.add(newBaTblColmDVo);
				} else {
					colmTyp=null;
					dateBaTblColmDVo=baTblColmDVoMap.get(baTblColmDVo.getColmId());
					if(dateBaTblColmDVo!=null)
						colmTyp=dateBaTblColmDVo.getColmTyp();
					if (bullCnt != null && bullCnt > 0 && !ArrayUtil.isInArray(skipColList, colmTyp)) {
						// 테이블이 사용되는 게시판이 있으면 - 컬럼 변경은 못함
					} else {
						/*if(dateBaTblColmDVo!=null && colmTyp!=null && ArrayUtil.isInArray(skipColList, colmTyp) && 
								(baTblColmDVo.getColmTyp()==null || !baTblColmDVo.getColmTyp().equals(dateBaTblColmDVo.getColmTyp()))){
							baTblColmDVo.setColmTyp(dateBaTblColmDVo.getColmTyp());
						}*/
						// 테이블컬럼(BA_TBL_COLM_D) 테이블 - SELECT
						storedColmVo = (BaTblColmDVo) commonDao.queryVo(baTblColmDVo);
						
						// 데이터가 같지 않으면 - 전화번호 > 텍스트 등으로 변경 한 경우
						if (!baTblColmDVo.equals(storedColmVo)) {
							// 컬럼 수정
							queryQueue.update(baTblColmDVo);
						}
						
						// 길이가 변경 된 경우 - 컬럼 변경 수행
						if(baTblColmDVo.getColmLen()==null && storedColmVo.getColmLen()==null){
							// do nothing
						} else if(baTblColmDVo.getColmLen().equals(storedColmVo.getColmLen())){
							// do nothing
						} else {
							newBaTblColmDVo=new BaTblColmDVo();
							BeanUtils.copyProperties(baTblColmDVo, newBaTblColmDVo);
							newBaTblColmDVo.setAlter("MODIFY");
							alterColmVoList.add(newBaTblColmDVo);
						}
					}
				}
				// 오라클이면 오라클 컬럼으로
				toDbmsDataType(baTblColmDVo);
			}
//			if (skipList.size() > 0) boundColmVoList.removeAll(skipList);
		}
		
		//commonDao.execute(queryQueue);
		
		if ("Y".equals(baTblBVo.getExYn()) && !alterColmVoList.isEmpty()) {
			// 게시물 테이블 - ALTER
			alterTbl(baTblBVo.getTblNm(), alterColmVoList, queryQueue);
		}
		return addColmList;
	}

	/** 테이블 삭제 */
	@SuppressWarnings("unchecked")
	// @Transactional - 오라클에서 이상 있음
	//@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
	public void deleteTbl(HttpServletRequest request, String tblId, QueryQueue queryQueue)
			throws SQLException {

		BaTblBVo baTblBVo = new BaTblBVo();
		baTblBVo.setTblId(tblId);

		// 테이블관리(BA_TBL_B) 테이블 - SELECT
		BaTblBVo storedBaTblBVo = (BaTblBVo) commonDao.queryVo(baTblBVo);

		// 리소스기본(BA_RESC_B) 테이블 - DELETE
		if (storedBaTblBVo.getRescId() != null && !storedBaTblBVo.getRescId().isEmpty()) {
			BaRescBVo baRescBVo = new BaRescBVo();
			baRescBVo.setRescId(storedBaTblBVo.getRescId());
			queryQueue.delete(baRescBVo);
		}
		
		// 테이블관리(BA_TBL_B) 테이블 - DELETE
		queryQueue.delete(baTblBVo);

		// 테이블컬럼(BA_TBL_COLM_D) 테이블 - SELECT
		BaTblColmDVo baTblColmDVo = new BaTblColmDVo();
		baTblColmDVo.setTblId(tblId);
		List<BaTblColmDVo> colmVoList = (List<BaTblColmDVo>) commonDao.queryList(baTblColmDVo);

		// 리소스기본(BA_RESC_B) 테이블 - DELETE
		for (BaTblColmDVo colmVo : colmVoList) {
			if (colmVo.getRescId() != null && !colmVo.getRescId().isEmpty()) {
				BaRescBVo baRescBVo = new BaRescBVo();
				baRescBVo.setRescId(colmVo.getRescId());
				queryQueue.delete(baRescBVo);
			}
		}

		// 테이블컬럼(BA_TBL_COLM_D) 테이블 - DELETE
		queryQueue.delete(baTblColmDVo);

		commonDao.execute(queryQueue);

		// 게시물 테이블 - DROP
		dropTbl(storedBaTblBVo.getTblNm());
	}

	/** 전체 테이블명 얻기 */
	public String getFullTblNm(String tblNm) {
		return tblNmPrefix + tblNm + tblNmSuffix;
	}

	private boolean dbmsChecked = false;
	private boolean dbmsOracle = false;
	private boolean dbmsMysql = false;
//	/** 오라클 체크 */
	private void checkDbms(){
		String dbms = contextProperties.getProperty("dbms");
		dbmsOracle = "oracle".equals(dbms);
		dbmsMysql = "mysql".equals(dbms);
		dbmsChecked = true;
	}
	public boolean isOracle(){
		if(!dbmsChecked) checkDbms();
		return dbmsOracle;
	}
	public boolean isMysql(){
		if(!dbmsChecked) checkDbms();
		return dbmsMysql;
	}
	/** DBMS 별 컬럼타입, 길이 변환 - 기준 MS-SQL */
	private void toDbmsDataType(BaTblColmDVo baTblColmDVo){
		if(isOracle()){
			String dataTyp = baTblColmDVo.getDataTyp();
			if("VARCHAR".equals(dataTyp)){
				baTblColmDVo.setDataTyp("VARCHAR2");
			}else if("NVARCHAR".equals(dataTyp)){
				baTblColmDVo.setDataTyp("NVARCHAR2");
			}else if("INT".equals(dataTyp)){
				baTblColmDVo.setDataTyp("NUMBER");
				baTblColmDVo.setColmLen(10);
			} else if("BIGINT".equals(dataTyp)){
				baTblColmDVo.setDataTyp("NUMBER");
				baTblColmDVo.setColmLen(22);
			} else if("DATETIME".equals(dataTyp)){
				baTblColmDVo.setDataTyp("DATE");
				baTblColmDVo.setColmLen(null);
			} else if("TEXT".equals(dataTyp) || "NTEXT".equals(dataTyp)){
				baTblColmDVo.setDataTyp("CLOB");
				baTblColmDVo.setColmLen(null);
			} else if("IMAGE".equals(dataTyp)){
				baTblColmDVo.setDataTyp("BLOB");
				baTblColmDVo.setColmLen(null);
			}
		} else if(isMysql()){
			String dataTyp = baTblColmDVo.getDataTyp();
			if(dataTyp.equals("INT") || dataTyp.equals("BIGINT") || dataTyp.equals("DATETIME")){
				// 그대로
			} else if("NVARCHAR".equals(dataTyp)){
				baTblColmDVo.setDataTyp("VARCHAR");
			} else if("TEXT".equals(dataTyp) || "NTEXT".equals(dataTyp)){
				baTblColmDVo.setDataTyp("LONGTEXT");
				baTblColmDVo.setColmLen(null);
			} else if(dataTyp.equals("IMAGE") || dataTyp.equals("FILE") || dataTyp.equals("VARBINARY")){
				baTblColmDVo.setDataTyp("LONGBLOB");
				baTblColmDVo.setColmLen(null);
			}
		}
	}
	
	/** 테이블 컬럼 상세 맵으로 리턴*/
	public Map<String,BaTblColmDVo> getBaTblColmDVoMap(String tblId) throws SQLException{
		Map<String,BaTblColmDVo> returnMap = null;
		BaTblColmDVo baTblColmDVo = new BaTblColmDVo();
		baTblColmDVo.setTblId(tblId);
		@SuppressWarnings("unchecked")
		List<BaTblColmDVo> baTblColmDVoList = (List<BaTblColmDVo>)commonDao.queryList(baTblColmDVo);
		if(baTblColmDVoList!=null){
			returnMap = new HashMap<String,BaTblColmDVo>();
			for(BaTblColmDVo storedBaTblColmDVo : baTblColmDVoList){
				returnMap.put(storedBaTblColmDVo.getColmId(), storedBaTblColmDVo);
			}
		}
		return returnMap;
		
	}
}
