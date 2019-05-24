package com.innobiz.orange.web.dm.svc;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.dm.vo.DmTranBVo;
import com.innobiz.orange.web.dm.vo.DmTranProcLVo;
import com.innobiz.orange.web.dm.vo.DmTranTgtTVo;
import com.innobiz.orange.web.dm.vo.DmxTranVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 문서 이관 서비스 */
@Service
public class DmTransferSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmTransferSvc.class);
	
	private static final Integer TRANS_ROW_SIZE = 1000;
	
	//패키지 경로[추후 클래스를 통한 패키지 자동 매핑]
	private final String pakage = DmConstant.pakage;
	
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 문서 공통 서비스 */
//	@Autowired
//	private DmCmSvc dmCmSvc;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** contextProperties */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
	private LinkedList<String> tranIdQueue = new LinkedList<String>();
	
	private TransferThread transferThread = null;
	
	boolean isMssql = false;
	boolean isOracle = false;
	boolean isKo = false;
	
	/** 이관항목 더하기 */
	public void addTranId(String tranId){
		synchronized (tranIdQueue) {
			tranIdQueue.add(tranId);
		}
		// 초기화
		if(transferThread == null){
			isMssql = "mssql".equals(contextProperties.getProperty("dbms"));
			isOracle = "oracle".equals(contextProperties.getProperty("dbms"));
			isKo = "ko".equals(contextProperties.getProperty("login.default.lang", "ko"));
			
			transferThread = new TransferThread();
			transferThread.start();
		} else {
			synchronized(transferThread){
				transferThread.notify();
			}
		}
	}
	
	/** 이관 진행 */
	private void processTransfer(String tranId) throws SQLException{
		
		// 이관기본(DM_TRAN_B) - 조회
		DmTranBVo dmTranBVo = new DmTranBVo();
		dmTranBVo.setTranId(tranId);
		dmTranBVo = (DmTranBVo)commonSvc.queryVo(dmTranBVo);
		
		if(dmTranBVo == null){
			LOGGER.error("NO transfer data(DM_TRAN_B) for tranId:"+tranId);
			return;
		}
		
		//이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러 
		if("processing".equals(dmTranBVo.getTranProcStatCd())){
			LOGGER.error("Transfering is already started ! - tranId:"+tranId);
			return;
		} else if("completed".equals(dmTranBVo.getTranProcStatCd())){
			LOGGER.error("Transfering is already completed ! - tranId:"+tranId);
			return;
		}
		
		// 기본저장소(DM_STOR_B) - 조회
		DmStorBVo srcStorVo = dmStorSvc.getDmStorBVo(dmTranBVo.getCompId(), "ko", null);
		if(srcStorVo == null){
			LOGGER.error("NO default data(DM_STOR_B) for tranId:"+tranId+"  compId:"+dmTranBVo.getCompId());
			return;
		}
		
		// 저장소(DM_STOR_B) - 조회
		DmStorBVo dmStorBVo = new DmStorBVo();
		dmStorBVo.setStorId(dmTranBVo.getStorId());
		dmStorBVo = (DmStorBVo)commonSvc.queryVo(dmStorBVo);
		if(dmStorBVo == null){
			LOGGER.error("NO transfer data(DM_STOR_B) for tranId:"+tranId+"  storId:"+dmTranBVo.getStorId());
			return;
		}
		
		if(dmStorBVo.getTblNm() != null && !dmStorBVo.getTblNm().isEmpty()) dmTranBVo.setTblNm(dmStorBVo.getTblNm());
		///////////////////////////////////////////////////////////////////////
		//
		//  이관 작업
		
		// 작업 시퀀스
		Integer procSeq = 0;
		// 시작 작업 시퀀스 - 오류 발생시 재 이관 목적
		Integer strtSeq = 0;
		
		// 테이블 생성여부, 인덱스 생성여부 - 테이블 생성 후 인덱스 생성 안되고 오류 발생시 - 인덱스 생성하기 위한것
		boolean createTables = false;
		boolean createIndexes = false;
		
		// 원본 테이블
		String srcTblId = srcStorVo.getTblNm();
		try {
			// 시스템 차단
			ptSysSvc.setSysHalt(dmTranBVo.getUserUid(), "dm", "cm.msg.halt.dmTran");
			
			// 이관 시작 처리
			startTransfer(dmTranBVo);
			
			//이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러 
			if("error".equals(dmTranBVo.getTranProcStatCd())){
				// 오류의 경우 마지막 작업 시퀀스를 조회하여 해당 시퀀스 부터 작업 시작함
				strtSeq = queryMaxTranProcSeq(tranId);
				if(strtSeq == null) strtSeq = 0;
				else {
					// 오류 로그 삭제 - 로그 중첩 insert 방지
					DmTranProcLVo dmTranProcLVo = new DmTranProcLVo();
					dmTranProcLVo.setTranId(dmTranBVo.getTranId());
					dmTranProcLVo.setSeq(strtSeq.toString());
					commonSvc.delete(dmTranProcLVo);
				}
			}
			
			// 1. 테이블 생성
			if(dmStorBVo.getTblNm()==null || dmStorBVo.getTblNm().isEmpty()){
				procSeq++;
				if(procSeq >= strtSeq){
					dmTranBVo.setTblNm(dmStorSvc.createTblNm(dmTranBVo.getCompId()));
					createTables(dmTranBVo, procSeq, strtSeq);
					
					// 저장소 생성됨 저장
					DmStorBVo updateDmStorBVo = new DmStorBVo();
					updateDmStorBVo.setStorId(dmStorBVo.getStorId());
					updateDmStorBVo.setTblNm(dmTranBVo.getTblNm());
					commonSvc.update(updateDmStorBVo);
					
					createTables = true;
				}
			} else {
				procSeq++;
				if(procSeq >= strtSeq){
					writeSkipLog(dmTranBVo, procSeq, 1);
				}
			}
			
			// 2. 이관 대상 추출
			procSeq++;
			if(procSeq >= strtSeq){
				extractTransferingData(srcTblId, dmTranBVo, procSeq, strtSeq);
			}
			
			if(dmStorBVo.getTblNm()==null || dmStorBVo.getTblNm().isEmpty()){
				// 3. 공통 데이터 이관
				for(String[] table : getTranferTables(true)){
					procSeq++;
					if(procSeq >= strtSeq){
						copyShareOfData(srcStorVo.getStorId(), dmTranBVo.getStorId(), dmTranBVo, table, procSeq, strtSeq);
					}
				}
			}else{
				procSeq++;
				// 기존 저장소에 이관하는 경우 SKIP
				writeSkipLog(dmTranBVo, procSeq, 3);
			}
						
			// 4. 문서 데이터 이관
			for(String[] table : getTranferTables(false)){
				procSeq++;
				if(procSeq >= strtSeq){
					copyTransferingData(srcTblId, dmTranBVo, table, procSeq, strtSeq);
				}
			}
			
			// 5. 인덱스 생성
			if(dmStorBVo.getTblNm()==null || dmStorBVo.getTblNm().isEmpty()){
				procSeq++;
				if(procSeq >= strtSeq){
					createIndexes(dmTranBVo, procSeq, strtSeq, false);
					createIndexes = true;
				}
			} else {
				procSeq++;
				if(procSeq >= strtSeq){
					writeSkipLog(dmTranBVo, procSeq, 5);
				}
			}
			
			// 6. 이관 문서 저장소 변경
			procSeq++;
			if(procSeq >= strtSeq){
				updateStorageOfData(dmTranBVo, procSeq, strtSeq);
			}
			
			// 7. 이관된 데이터 삭제
			for(String[] table : getTranferTables(false)){
				procSeq++;
				if(procSeq >= strtSeq){
					deleteTransferedData(srcTblId, dmTranBVo, table, procSeq, strtSeq);
				}
			}
			
			// 8. 테이블 조각 모음
			if("Y".equals(dmTranBVo.getDefragYn())){
				
				for(String[] table : getTranferTables(false)){
					procSeq++;
					if(procSeq >= strtSeq){
						processDefragmentation(srcTblId, dmTranBVo, table, procSeq, strtSeq);
					}
				}
			} else {
				procSeq++;
				if(procSeq >= strtSeq){
					writeSkipLog(dmTranBVo, procSeq, 8);
				}
			}
			
			// 이관 종료 처리
			procSeq++;
			if(procSeq >= strtSeq){
				endTransfer(dmTranBVo, dmStorBVo, procSeq);
			}
			
		} catch(Exception exception){
			writeErrorLog(dmTranBVo, procSeq, exception);
			
			// 테이블 생성되고 인덱스 생성 안되었을때 - 인덱스 생성
			if(createTables && !createIndexes){
				procSeq++;
				createIndexes(dmTranBVo, procSeq, strtSeq, true);
			}
			
			exception.printStackTrace();
		} finally {
			// 시스템 차단 해제
			ptSysSvc.clearSysHalt();
		}
	}
	
	/** 최대 진행 순번 구하기 - 오류난 순번 */
	private Integer queryMaxTranProcSeq(String tranId) throws SQLException {
		DmxTranVo dmxTranVo = new DmxTranVo();
		dmxTranVo.setTranId(tranId);
		dmxTranVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmxTranDao.queryMaxTranProcSeq");
		Integer seq = commonSvc.queryInt(dmxTranVo);
		return seq==null ? 0 : seq;
	}
	
	/** 7. 테이블 조각 모음 */
	private void processDefragmentation(String srcTblId, DmTranBVo dmTranBVo, String[] table, Integer procSeq, Integer strtSeq) throws SQLException{
		
		String queryId = null;
		int i, size = 0;
		if(isMssql){
			queryId = "com.innobiz.orange.web.dm.dao.DmxTranDao.processDefragmentationForMSSQL";
			size = 1;
		} else if(isOracle){
			queryId = "com.innobiz.orange.web.dm.dao.DmxTranDao.processDefragmentationForORACLE";
			size = 7;
		}
		
		if(size==0){
			writeSkipLog(dmTranBVo, procSeq, 7);
			return;
		}
		
		String srcTbl = table[0].replace("$tblNm$", srcTblId);
		// 테이블명
		String tableName = table[0].replace("$tblNm$_", "");
		// 인덱스 조회 - 오라클 용
		DmxTranVo dmxTranVo = new DmxTranVo();
		dmxTranVo.setInstanceQueryId(queryId);
		dmxTranVo.setSrcTbl(srcTbl);
		@SuppressWarnings("unchecked")
		List<String> indexList = isOracle ? (List<String>)commonSvc.queryList(dmxTranVo) : null;
		
		// 시작 로그
		String tranCont = isKo ? "[8/8] 조각모음 : "+tableName : "[8/8] Defragmentation of "+tableName;
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		writeStartLog(dmTranBVo, procSeq, "1", tranCont);
		
		dmxTranVo = new DmxTranVo();
		dmxTranVo.setInstanceQueryId(queryId);
		dmxTranVo.setSrcTbl(srcTbl);
		for(i=1; i<=size; i++){
			dmxTranVo.setSqlSeq(i);
			
			if(isOracle && (i==2 || i==6)){
				if(indexList != null){
					for(String index : indexList){
						dmxTranVo.setIdxNm(index);
						commonSvc.update(dmxTranVo);
					}
					dmxTranVo.setIdxNm(null);
				}
			} else {
				commonSvc.update(dmxTranVo);
			}
		}
		
		// 진행 로그
		writeInProcLog(dmTranBVo, procSeq, "1");
		
		// 종료 로그
		writeEndLog(dmTranBVo, procSeq);
	}
	
	/** 6. 이관된 데이터 삭제 */
	private void deleteTransferedData(String srcTblId, DmTranBVo dmTranBVo, String table[], Integer procSeq, Integer strtSeq) throws SQLException{
		
		// 문서 건수
		int copyCount = 0;
		
		String srcTbl = table[0].replace("$tblNm$", srcTblId);
		// 테이블명
		String tableName = table[0].replace("$tblNm$_", "");
		
		// 로그 타이틀
		String tranCont = isKo ? "[7/8] 이관된 데이터 삭제 : "+tableName : "[7/8] Delete transfered data(document) of "+tableName;
		// 문서대상건수
		copyCount = parseInt(dmTranBVo.getDocTgtCnt());
		 
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		
		int pageCnt = (int)Math.ceil((double)copyCount / TRANS_ROW_SIZE.intValue());
		
		// 시작 로그
		writeStartLog(dmTranBVo, procSeq, Integer.toString(pageCnt), tranCont);
		
		// 테이블별 이관 실행
		DmxTranVo dmxTranVo = new DmxTranVo();
		dmxTranVo.setTranId(dmTranBVo.getTranId());
		dmxTranVo.setSrcTbl(srcTbl);
		if("DM_FILE_D".equals(tableName)){
			dmxTranVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmxTranDao.deleteTransferedDocData");
			dmxTranVo.setKeyCol("DM_REF_ID");
			dmxTranVo.setSubTbl("DM_"+srcTblId+"_DOC_L");
			dmxTranVo.setSubKeyCol("DOC_ID");
		}else{
			dmxTranVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmxTranDao.deleteTransferedData");
			dmxTranVo.setKeyCol("DOC_GRP_ID");
		}
		dmxTranVo.setPageRowCnt(TRANS_ROW_SIZE);
		
		// 1000 건 단위로 나누어 실행함 - 롤백 세크먼트 고려
		for(int i=0; i<pageCnt; i++){
			dmxTranVo.setPageNo(Integer.valueOf(i));
			commonSvc.insert(dmxTranVo);
			
			// 진행사항 로그
			writeInProcLog(dmTranBVo, procSeq, Integer.toString(i+1));
		}
		
		// 종료 로그
		writeEndLog(dmTranBVo, procSeq);
	}

	/** 6. 이관 문서 저장소 변경 */
	private void updateStorageOfData(DmTranBVo dmTranBVo, Integer procSeq, Integer strtSeq) throws SQLException{
		
		// 문서 건수
		int copyCount = 0;
		
		// 로그 타이틀
		String tranCont = isKo ? "[6/8] 이관된 문서의 저장소 변경 " : "[6/8] Change the storage of the transfered data (document)";
		// 문서대상건수
		copyCount = parseInt(dmTranBVo.getDocTgtCnt());
		
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		
		int pageCnt = (int)Math.ceil((double)copyCount / TRANS_ROW_SIZE.intValue());
		
		// 시작 로그
		writeStartLog(dmTranBVo, procSeq, "2", tranCont);
		
		DmxTranVo dmxTranVo = new DmxTranVo();
		dmxTranVo.setTranId(dmTranBVo.getTranId());
		dmxTranVo.setStorId(dmTranBVo.getStorId());
		dmxTranVo.setPageRowCnt(TRANS_ROW_SIZE);
		
		// 저장소관계 - 데이터 삭제
		dmxTranVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmxTranDao.deleteStorageOfData");
		// 1000 건 단위로 나누어 실행함 - 롤백 세크먼트 고려
		for(int i=0; i<pageCnt; i++){
			dmxTranVo.setPageNo(Integer.valueOf(i));
			commonSvc.delete(dmxTranVo);
		}
		writeInProcLog(dmTranBVo, procSeq, "1");
		
		// 저장소관계 - 데이터 입력
		dmxTranVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmxTranDao.insertStorageOfData");
		// 1000 건 단위로 나누어 실행함 - 롤백 세크먼트 고려
		for(int i=0; i<pageCnt; i++){
			dmxTranVo.setPageNo(Integer.valueOf(i));
			commonSvc.insert(dmxTranVo);
		}
		writeInProcLog(dmTranBVo, procSeq, "2");
		
		// 종료 로그
		writeEndLog(dmTranBVo, procSeq);
	}
	
	/** 3. 공통 데이터 복사 */
	private void copyShareOfData(String storId, String tgtStorId, DmTranBVo dmTranBVo, String table[], Integer procSeq, 
			Integer strtSeq) throws SQLException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException{
		
		// 원본 테이블
		//String srcTbl = table[0];
		// 대상 테이블
		
		String voName = StringUtil.toCamelNotation(table[0], true)+"Vo";
		
		
		// 로그 타이틀
		String tranCont = isKo ? "[3/8] 공통 데이터 복사 : "+table[0] : "[3/8] Copy the \"common\" data of "+table[0];
		
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		
		//이관할 VO 명 배열
		CommonVo commonVo;
		String nm = pakage + "."+voName;
		@SuppressWarnings("unchecked")
		Class<? extends CommonVo> clazz = (Class<? extends CommonVo>)Class.forName(nm);
		if(ArrayUtil.isInArray(DmConstant.STOR_VOS, voName)){
			commonVo = clazz.getDeclaredConstructor(String.class).newInstance(storId);
		}else{
			commonVo = clazz.newInstance();
			VoUtil.setValue(commonVo, "storId", storId);
		}
		
		// 복사 건수
		int copyCount = commonSvc.count(commonVo);
		
		// 시작 로그
		writeStartLog(dmTranBVo, procSeq, "1", tranCont);
		
		// 이관할 목록을 조회해서 건별 복사
		if(copyCount > 0){
			@SuppressWarnings("unchecked")
			List<? extends CommonVo> list = (List<? extends CommonVo>)commonSvc.queryList(commonVo);
			for(CommonVo storedCommonVo : list){
				VoUtil.setValue(storedCommonVo, "storId", tgtStorId);
				commonSvc.insert(storedCommonVo);
			}
			// 진행사항 로그
			writeInProcLog(dmTranBVo, procSeq, "1");
		}
		
		// 종료 로그
		writeEndLog(dmTranBVo, procSeq);
		
	}
	
	/** 4. 데이터 이관 */
	private void copyTransferingData(String srcTblId, DmTranBVo dmTranBVo, String table[], Integer procSeq, Integer strtSeq) throws SQLException{
		
		// 원본 테이블
		String srcTbl = table[0].replace("$tblNm$", srcTblId);
		// 대상 테이블
		String tgtTbl = table[0].replace("$tblNm$", dmTranBVo.getTblNm());
		// 테이블명
		String tableName = table[0].replace("$tblNm$_", "");
		// 문서 건수
		int copyCount = 0;
		
		
		
		// 로그 타이틀
		String tranCont = isKo ? "[4/8] 데이터 이관 : "+tableName : "[4/8] Copy the \"document\" data of "+tableName;
		// 문서대상건수
		copyCount = parseInt(dmTranBVo.getDocTgtCnt());
		
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		
		int pageCnt = (int)Math.ceil((double)copyCount / TRANS_ROW_SIZE.intValue());
		
		// 시작 로그
		writeStartLog(dmTranBVo, procSeq, Integer.toString(pageCnt), tranCont);
		
		// 테이블별 이관 실행
		DmxTranVo dmxTranVo = new DmxTranVo();		
		dmxTranVo.setTranId(dmTranBVo.getTranId());
		dmxTranVo.setSrcTbl(srcTbl);
		dmxTranVo.setTgtTbl(tgtTbl);
		if("DM_FILE_D".equals(tableName)){
			dmxTranVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmxTranDao.copyTransferingDocData");
			dmxTranVo.setKeyCol("DM_REF_ID");
			dmxTranVo.setSubTbl("DM_"+srcTblId+"_DOC_L");
			dmxTranVo.setSubKeyCol("DOC_ID");
		}else{
			dmxTranVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmxTranDao.copyTransferingData");
			dmxTranVo.setKeyCol("DOC_GRP_ID");
		}
		
		dmxTranVo.setPageRowCnt(TRANS_ROW_SIZE);
		dmxTranVo.setOrderBy(table[1]);
		
		// 1000 건 단위로 나누어 실행함 - 롤백 세크먼트 고려
		for(int i=0; i<pageCnt; i++){
			dmxTranVo.setPageNo(Integer.valueOf(i));
			commonSvc.insert(dmxTranVo);
			
			// 진행사항 로그
			writeInProcLog(dmTranBVo, procSeq, Integer.toString(i+1));
		}
		
		// 종료 로그
		writeEndLog(dmTranBVo, procSeq);
		
		
	}
	
	/** 이관 대상 테이블 리턴 */
	private String[][] getTranferTables(boolean isShare){
		if(isShare){
			// { 테이블ID, 키 컬럼 }
			return new String[][]{
					{"DM_CAT_B", "STOR_ID, CAT_ID"},
					{"DM_CAT_DISP_D", "STOR_ID, CAT_ID, ITEM_ID"},
					{"DM_CD_D", "STOR_ID, CD_GRP_ID, CD_ID"},
					{"DM_CD_GRP_B", "STOR_ID, CD_GRP_ID"},
					{"DM_CLS_B", "STOR_ID, CLS_ID"},
					{"DM_FLD_B", "STOR_ID, FLD_ID"},
					{"DM_ITEM_B", "STOR_ID, ITEM_ID"},
					{"DM_RESC_B", "STOR_ID, RESC_ID, LANG_TYP_CD"}
			};
		}
		// { 테이블ID, 키 컬럼 }
		return new String[][]{
				{"DM_$tblNm$_DOC_L", "DOC_ID"},
				{"DM_$tblNm$_CLS_R", "DOC_GRP_ID, CLS_ID"},
				{"DM_$tblNm$_DOC_D", "DOC_GRP_ID"},
				{"DM_$tblNm$_DOC_VER_L", "DOC_GRP_ID, DOC_ID"},
				{"DM_$tblNm$_KWD_L", "DOC_GRP_ID, KWD_NM"},
				{"DM_$tblNm$_FILE_D", "FILE_ID"},
				{"DM_$tblNm$_TASK_H", "DOC_GRP_ID, TASK_DT, USER_UID, TASK_CD"}
		};
	}
	
	/** 2. 이관 대상 추출 */
	private void extractTransferingData(String srcTbl, DmTranBVo dmTranBVo, Integer procSeq, Integer strtSeq) throws SQLException{
		
		if(srcTbl==null || srcTbl.isEmpty()) {
			LOGGER.error("[extractTransferingData] srcTbl is null!!");
			return;
		}
		// 이관진행내역(DM_TRAN_PROC_L) - 시작 로그
		String tranCont = isKo ? "[2/8] 이관 대상 추출" : "[2/8] Extract data to transfer";
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		writeStartLog(dmTranBVo, procSeq, "2", tranCont);
		
		// 이관대상 추출 실행
		DmxTranVo dmxTranVo = createTranTgtVo(dmTranBVo.getCompId(), dmTranBVo.getTranTgtStrtYmd(), dmTranBVo.getTranTgtEndYmd());
		dmxTranVo.setTranId(dmTranBVo.getTranId());
		dmxTranVo.setSrcTbl(srcTbl);
		dmxTranVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmxTranDao.extractTransferingData");
		dmxTranVo.setDurCat("regDt"); // 등록일 기준
		dmxTranVo.setWhereSqllet("AND STAT_CD = 'C'"); // 정상문서
		if(isMssql){
			dmxTranVo.setRnumSql("ROW_NUMBER() OVER(ORDER BY T.DOC_GRP_ID)");
		} else if(isOracle){
			dmxTranVo.setRnumSql("ROWNUM");
		}
		commonSvc.insert(dmxTranVo);
		
		// 진행사항 로그 남기기
		writeInProcLog(dmTranBVo, procSeq, "1");
					
		// 문서대상건수 - 조회
		DmTranTgtTVo dmTranTgtTVo = new DmTranTgtTVo();
		dmTranTgtTVo.setTranId(dmTranBVo.getTranId());
		Integer docTgtCnt = commonSvc.count(dmTranTgtTVo);
		
		// 이관기본(DM_TRAN_B) - 대상건수 update
		DmTranBVo updateDmTranBVo = new DmTranBVo();
		updateDmTranBVo.setTranId(dmTranBVo.getTranId());
		updateDmTranBVo.setDocTgtCnt(docTgtCnt==null ? "0" : docTgtCnt.toString());
		commonSvc.update(updateDmTranBVo);
		
		dmTranBVo.setDocTgtCnt(docTgtCnt==null ? "0" : docTgtCnt.toString());
		
		// 진행사항 로그 남기기
		writeInProcLog(dmTranBVo, procSeq, "2");
		
		// 종료 로그
		writeEndLog(dmTranBVo, procSeq);
		
	}
	
	/** 1. 테이블 생성 */
	private void createTables(DmTranBVo dmTranBVo, Integer procSeq, Integer strtSeq) throws SQLException {
		
		// 중복된 테이블명 체크
		if (dmStorSvc.isTblNmExist(dmTranBVo.getTblNm())) {
			LOGGER.error("table name is allready!!");
			return;
		}
				
		String[][] tables = null;
		
		if(isMssql){
			// { query-id, 해당 id 내의 sql 갯수 }
			tables = new String[][]{
					{"DmDocL", "5"},
					{"DmDocD", "21"},
					{"DmClsR", "4"},
					{"DmDocVerL", "5"},
					{"DmKwdL", "4"},
					{"DmFileD", "12"},
					{"DmTaskH", "7"}
			};
		} else if(isOracle){
			// { query-id, 해당 id 내의 sql 갯수 }
			tables = new String[][]{
					{"DmDocL", "13"},
					{"DmDocD", "23"},
					{"DmClsR", "6"},
					{"DmDocVerL", "7"},
					{"DmKwdL", "6"},
					{"DmFileD", "14"},
					{"DmTaskH", "9"}
			};
		} else {
			return;
		}
		
		// 이관진행내역(DM_TRAN_PROC_L) - 시작 로그
		String tranCont = isKo ? "[1/8] 테이블 생성" : "[1/8] Create tables";
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		writeStartLog(dmTranBVo, procSeq, Integer.toString(tables.length), tranCont);
		
		// 테이블 생성용
		DmStorBVo dmStorBVo = new DmStorBVo();
		dmStorBVo.setStorId(dmTranBVo.getStorId());
		
		String sqlIdPrefix = isMssql ?
				"com.innobiz.orange.web.dm.dao.DmxTableMSSQLDao.create" : 
					"com.innobiz.orange.web.dm.dao.DmxTableORACLEDao.create";
		
		int i, size, tableSeq = 0;
		for(String[] table : tables){
			dmStorBVo.setInstanceQueryId(sqlIdPrefix+table[0]);
			size = Integer.parseInt(table[1]);
			
			if("DmDocL".equals(table[0])){// 기본문서에 확장컬럼 추가
				dmStorBVo.setColmVoList(dmStorSvc.getAddItemList(null, null));
			}
			dmStorBVo.setTblNm(dmTranBVo.getTblNm());
			// 테이블 생성
			for(i=1;i<=size;i++){
				dmStorBVo.setSqlSeq(i);
				commonSvc.update(dmStorBVo);
			}
			
			// 진행사항 로그 남기기
			writeInProcLog(dmTranBVo, procSeq, Integer.toString(++tableSeq));
		}
		
		// 종료 로그
		writeEndLog(dmTranBVo, procSeq);
	}
	
	/** 4. 인덱스 생성 */
	private void createIndexes(DmTranBVo dmTranBVo, Integer procSeq, Integer strtSeq, boolean noLog) throws SQLException{
		
		String[][] indexes = null;
		
		if(isMssql){
			// { query-id, 해당 id 내의 sql 갯수 }
			indexes = new String[][]{
					{"DmDocL", "1"},
					{"DmDocD", "1"}
			};
		} else if(isOracle){
			// { query-id, 해당 id 내의 sql 갯수 }
			indexes = new String[][]{
					{"DmDocL", "1"},
					{"DmDocD", "1"}
			};
		} else {
			return;
		}
		
		if(!noLog){
			// 이관진행내역(DM_TRAN_PROC_L) - 시작 로그
			String tranCont = isKo ? "[5/8] 인덱스 생성" : "[5/8] Create indexes";
			if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
			writeStartLog(dmTranBVo, procSeq, Integer.toString(indexes.length), tranCont);
		}
		
		// 인덱스 생성용
		DmStorBVo dmStorBVo = new DmStorBVo();
		dmStorBVo.setStorId(dmTranBVo.getStorId());
		dmStorBVo.setTblNm(dmTranBVo.getTblNm());
		
		String sqlIdPrefix = isMssql ?
				"com.innobiz.orange.web.dm.dao.DmxTableMSSQLDao.index" : 
					"com.innobiz.orange.web.dm.dao.DmxTableORACLEDao.index";
		
		int i, size, indexSeq = 0;
		for(String[] index : indexes){
			dmStorBVo.setInstanceQueryId(sqlIdPrefix+index[0]);
			size = Integer.parseInt(index[1]);
			// 인덱스 생성
			for(i=1;i<=size;i++){
				dmStorBVo.setSqlSeq(i);
				commonSvc.update(dmStorBVo);
			}
			
			if(!noLog){
				// 진행사항 로그 남기기
				writeInProcLog(dmTranBVo, procSeq, Integer.toString(++indexSeq));
			}
		}
		
		if(!noLog){
			// 종료 로그
			writeEndLog(dmTranBVo, procSeq);
		}
	}
	
	/** 이관 시작 처리 */
	private void startTransfer(DmTranBVo dmTranBVo) throws SQLException{
		DmTranBVo updateDmTranBVo = new DmTranBVo();
		updateDmTranBVo.setTranId(dmTranBVo.getTranId());
		updateDmTranBVo.setTranStrtDt("sysdate");
		//이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
		updateDmTranBVo.setTranProcStatCd("processing");
		commonSvc.update(updateDmTranBVo);
	}
	
	/** 이관 종료 처리 */
	private void endTransfer(DmTranBVo dmTranBVo, DmStorBVo dmStorBVo, Integer procSeq) throws SQLException{
		DmTranBVo updateDmTranBVo = new DmTranBVo();
		updateDmTranBVo.setTranId(dmTranBVo.getTranId());
		updateDmTranBVo.setTranEndDt("sysdate");
		//이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
		updateDmTranBVo.setTranProcStatCd("completed");
		commonSvc.update(updateDmTranBVo);
		
		// 이관된 문서 수
		//int docTgtCnt = parseInt(dmTranBVo.getDocTgtCnt());
		
		// 이관 완료 로그
		DmTranProcLVo dmTranProcLVo = new DmTranProcLVo();
		dmTranProcLVo.setTranId(dmTranBVo.getTranId());
		dmTranProcLVo.setSeq(procSeq.toString());
		dmTranProcLVo.setTranCont(isKo ? "이관 완료" : "Transfering completed");
		dmTranProcLVo.setStrtDt("sysdate");
		dmTranProcLVo.setEndDt("sysdate");
		commonSvc.insert(dmTranProcLVo);
	}
	
	/** int 변환 - null 처리 */
	private int parseInt(String no){
		return no==null || no.isEmpty() ? 0 : Integer.parseInt(no);
	}
	
	/** 이관 실행용 쓰레드 */
	private class TransferThread extends Thread {
		
		@Override
		public void run() {
			
			// 이관ID
			String tranId;
			// 시스템 차단 여부
			boolean isInHalt = false;
			
			while(true){
				
				// 시스템 차단 여부 - 조회
				try {
					isInHalt = ptSysSvc.isSysInHalt();
				} catch (SQLException e) {
					isInHalt = true;
				}
				
				// 시스템이 차단되어 있으면 - 2분 후 다시 체크
				if(isInHalt){
					try {
						Thread.sleep(1000 * 120);
					} catch (InterruptedException ignore) {
					}
				} else {
					
					// 이관ID - 조회 : 이관 대상이 있는지 체크
					synchronized (tranIdQueue) {
						if(tranIdQueue.isEmpty()) tranId = null;
						else tranId = tranIdQueue.removeFirst();
					}
					
					// 이관대상이 없으면 - wait 모드
					if(tranId == null){
						synchronized(this){
							try {
								this.wait();
							} catch (InterruptedException e) {
								transferThread = null;
								break;
							}
						}
					} else {
						// 이관 실행
						try {
							processTransfer(tranId);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
				}
				
			}//while(true){
		}
	}
	
	/** 이관 대상 조회용 VO 생성 */
	public DmxTranVo createTranTgtVo(String compId, String durStrtDt, String durEndDt) throws SQLException{
		
		DmxTranVo dmxTranVo = new DmxTranVo();
		dmxTranVo.setCompId(compId);
		
		if(durStrtDt != null || durEndDt != null){
			dmxTranVo.setDurCat("regDt");
			if(durStrtDt != null && !durStrtDt.isEmpty() && durStrtDt.length()==10){
				dmxTranVo.setDurStrtDt(durStrtDt+" 00:00:00");
			}
			if(durEndDt != null && !durEndDt.isEmpty() && durEndDt.length()==10){
				dmxTranVo.setDurEndDt(durEndDt+" 23:59:59");
			}
		}
		
		return dmxTranVo;
	}
	
	/** 시작 로그 */
	private void writeStartLog(DmTranBVo dmTranBVo, Integer procSeq, String allCnt, String tranCont) throws SQLException{
		DmTranProcLVo dmTranProcLVo = new DmTranProcLVo();
		dmTranProcLVo.setTranId(dmTranBVo.getTranId());
		dmTranProcLVo.setSeq(procSeq.toString());
		dmTranProcLVo.setTranCont(tranCont);
		dmTranProcLVo.setAllCnt(allCnt);
		dmTranProcLVo.setProcCnt("0");
		dmTranProcLVo.setStrtDt("sysdate");
		commonSvc.insert(dmTranProcLVo);
	}
	
	/** 진행 로그 남기기 */
	private void writeInProcLog(DmTranBVo dmTranBVo, Integer procSeq, String procCnt) throws SQLException{
		DmTranProcLVo dmTranProcLVo = new DmTranProcLVo();
		dmTranProcLVo.setTranId(dmTranBVo.getTranId());
		dmTranProcLVo.setSeq(procSeq.toString());
		dmTranProcLVo.setProcCnt(procCnt);
		commonSvc.update(dmTranProcLVo);
	}
	
	/** 종료 로그 남기기 */
	private void writeEndLog(DmTranBVo dmTranBVo, Integer procSeq) throws SQLException{
		DmTranProcLVo dmTranProcLVo = new DmTranProcLVo();
		dmTranProcLVo.setTranId(dmTranBVo.getTranId());
		dmTranProcLVo.setSeq(procSeq.toString());
		dmTranProcLVo.setEndDt("sysdate");
		commonSvc.update(dmTranProcLVo);
	}
	
	/** 생략 로그 남기기 */
	private void writeSkipLog(DmTranBVo dmTranBVo, Integer procSeq, int majorSeq) throws SQLException{
		DmTranProcLVo dmTranProcLVo = new DmTranProcLVo();
		dmTranProcLVo.setTranId(dmTranBVo.getTranId());
		dmTranProcLVo.setSeq(procSeq.toString());
		dmTranProcLVo.setTranCont(isKo ? "["+majorSeq+"/8] 생략" : "["+majorSeq+"/8] Skip");
		dmTranProcLVo.setStrtDt("sysdate");
		dmTranProcLVo.setEndDt("sysdate");
		commonSvc.insert(dmTranProcLVo);
	}
	
	/** 오류 로그 남기기 */
	private void writeErrorLog(DmTranBVo dmTranBVo, Integer procSeq, Exception exception) throws SQLException{
		
		DmTranBVo errDmTranBVo = new DmTranBVo();
		errDmTranBVo.setTranId(dmTranBVo.getTranId());
		errDmTranBVo.setTranEndDt("sysdate");
		// 이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
		errDmTranBVo.setTranProcStatCd("error");
		commonSvc.update(errDmTranBVo);
		
		// Exception 위치 잡기
		String where = null;
		String message = exception.getMessage()==null ? exception.getClass().getCanonicalName() : exception.getMessage();
		for(StackTraceElement trace : exception.getStackTrace()){
			if(trace.getClassName().equals("com.innobiz.orange.web.dm.svc.DmTransferSvc")){
				where = trace.toString();
				break;
			}
		}
		
		// 오류 로그 저장
		DmTranProcLVo dmTranProcLVo = new DmTranProcLVo();
		dmTranProcLVo.setTranId(dmTranBVo.getTranId());
		dmTranProcLVo.setSeq(procSeq.toString());
		dmTranProcLVo.setEndDt("sysdate");
		dmTranProcLVo.setErrCont(where != null ? (where+": "+message) : message);
		dmTranProcLVo.setErrYn("Y");
		commonSvc.update(dmTranProcLVo);
	}
}
