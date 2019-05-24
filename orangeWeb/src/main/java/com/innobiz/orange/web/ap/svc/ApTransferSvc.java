package com.innobiz.orange.web.ap.svc;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.vo.ApStorBVo;
import com.innobiz.orange.web.ap.vo.ApStorCompRVo;
import com.innobiz.orange.web.ap.vo.ApTranBVo;
import com.innobiz.orange.web.ap.vo.ApTranFromRVo;
import com.innobiz.orange.web.ap.vo.ApTranProcLVo;
import com.innobiz.orange.web.ap.vo.ApTranTgtTVo;
import com.innobiz.orange.web.ap.vo.ApxTranVo;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 결재 이관 서비스 */
@Service
public class ApTransferSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApTransferSvc.class);
	
	/** 한번에 처리할 데이터 처리 최대 갯수 */
	private static final Integer MAX_TRANSACTION_SIZE = 1000;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** contextProperties */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
	private LinkedList<String> tranIdQueue = new LinkedList<String>();
	
	private TransferThread transferThread = null;
	
	boolean isOracle = false;
	boolean isMssql = false;
	boolean isMysql = false;
	boolean isKo = false;
	
	/** 이관항목 더하기 */
	public void addTranId(String tranId){
		synchronized (tranIdQueue) {
			tranIdQueue.add(tranId);
		}
		// 초기화
		if(transferThread == null){
			String dbms = contextProperties.getProperty("dbms");
			isOracle = "oracle".equals(dbms);
			isMssql = "mssql".equals(dbms);
			isMysql = "mysql".equals(dbms);
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
		
		// 이관기본(AP_TRAN_B) - 조회
		ApTranBVo apTranBVo = new ApTranBVo();
		apTranBVo.setTranId(tranId);
		apTranBVo = (ApTranBVo)commonSvc.queryVo(apTranBVo);
		
		if(apTranBVo == null){
			LOGGER.error("NO transfer data(AP_TRAN_B) for tranId:"+tranId);
			return;
		}
		
		//이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러 
		if("processing".equals(apTranBVo.getTranProcStatCd())){
			LOGGER.error("Transfering is already started ! - tranId:"+tranId);
			return;
		} else if("completed".equals(apTranBVo.getTranProcStatCd())){
			LOGGER.error("Transfering is already completed ! - tranId:"+tranId);
			return;
		}
		
		// 저장소기본(AP_STOR_B) - 조회
		ApStorBVo apStorBVo = new ApStorBVo();
		apStorBVo.setStorId(apTranBVo.getStorId());
		apStorBVo = (ApStorBVo)commonSvc.queryVo(apStorBVo);
		if(apStorBVo == null){
			LOGGER.error("NO transfer data(AP_STOR_B) for tranId:"+tranId+"  storId:"+apTranBVo.getStorId());
			return;
		}
		
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
		
		try {
			// 시스템 차단
			ptSysSvc.setSysHalt(apTranBVo.getUserUid(), "ap", "cm.msg.halt.apTran");
			
			// 이관 시작 처리
			startTransfer(apTranBVo);
			
			//이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러 
			if("error".equals(apTranBVo.getTranProcStatCd())){
				// 오류의 경우 마지막 작업 시퀀스를 조회하여 해당 시퀀스 부터 작업 시작함
				strtSeq = queryMaxTranProcSeq(tranId);
				if(strtSeq == null) strtSeq = 0;
				else {
					// 오류 로그 삭제 - 로그 중첩 insert 방지
					ApTranProcLVo apTranProcLVo = new ApTranProcLVo();
					apTranProcLVo.setTranId(apTranBVo.getTranId());
					apTranProcLVo.setSeq(strtSeq.toString());
					commonSvc.delete(apTranProcLVo);
				}
			}
			
			// 1. 테이블 생성
			if(!"Y".equals(apStorBVo.getCrtYn())){
				procSeq++;
				if(procSeq >= strtSeq){
					createTables(apTranBVo, procSeq, strtSeq);
					
					// 저장소 생성됨 저장
					ApStorBVo updateApStorBVo = new ApStorBVo();
					updateApStorBVo.setStorId(apStorBVo.getStorId());
					updateApStorBVo.setCrtYn("Y");
					commonSvc.update(updateApStorBVo);
					
					createTables = true;
				}
			} else {
				procSeq++;
				if(procSeq >= strtSeq){
					writeSkipLog(apTranBVo, procSeq, 1);
				}
			}
			
			// 2. 이관 대상 추출
			procSeq++;
			if(procSeq >= strtSeq){
				extractTransferingData(apTranBVo, procSeq, strtSeq);
			}
			
			// 결재문서, 시행변환문서, 종이문서
			String[] tranDataTypCds = { "doc", "trx", "paper" };
			
			// 문서포함여부
			boolean withDoc = "Y".equals(apTranBVo.getDocInclYn());
			// 종이문서포함여부
			boolean withPapDoc = "Y".equals(apTranBVo.getPapDocInclYn());
			
			// 3. 데이터 이관
			for(String tranDataTypCd : tranDataTypCds){
				if(!withDoc && (tranDataTypCd.equals("doc") || tranDataTypCd.equals("trx"))) continue;
				if(!withPapDoc && tranDataTypCd.equals("paper")) continue;
				
				for(String[] table : getTranferTables(tranDataTypCd)){
					
					procSeq++;
					if(procSeq >= strtSeq){
						copyTransferingData(apTranBVo, tranDataTypCd, table, procSeq, strtSeq);
					}
				}
			}
			
			// 4. 인덱스 생성
			if(!"Y".equals(apStorBVo.getCrtYn())){
				procSeq++;
				if(procSeq >= strtSeq){
					createIndexes(apTranBVo, procSeq, strtSeq, false);
					createIndexes = true;
				}
			} else {
				procSeq++;
				if(procSeq >= strtSeq){
					writeSkipLog(apTranBVo, procSeq, 4);
				}
			}
			
			// 5. 이관 문서 저장소 변경
			for(String tranDataTypCd : tranDataTypCds){
				if(!withDoc && (tranDataTypCd.equals("doc") || tranDataTypCd.equals("trx"))) continue;
				if(!withPapDoc && tranDataTypCd.equals("paper")) continue;
				
				procSeq++;
				if(procSeq >= strtSeq){
					updateStorageOfData(apTranBVo, tranDataTypCd, procSeq, strtSeq);
				}
			}
			
			// 6. 이관된 데이터 삭제
			for(String tranDataTypCd : tranDataTypCds){
				if(!withDoc && (tranDataTypCd.equals("doc") || tranDataTypCd.equals("trx"))) continue;
				if(!withPapDoc && tranDataTypCd.equals("paper")) continue;
				
				for(String[] table : getTranferTables(tranDataTypCd)){
					procSeq++;
					if(procSeq >= strtSeq){
						deleteTransferedData(apTranBVo, tranDataTypCd, table, procSeq, strtSeq);
					}
				}
			}
			
			// 7. 테이블 조각 모음
			if("Y".equals(apTranBVo.getDefragYn())){
				
				int docTgtCnt = parseInt(apTranBVo.getDocTgtCnt());
				int trxTgtCnt = parseInt(apTranBVo.getTrxTgtCnt());
				int papTgtCnt = parseInt(apTranBVo.getPapTgtCnt());
				
				String tranDataTypCd = (trxTgtCnt==0 && papTgtCnt==0) ? "doc"
						: (docTgtCnt==0 && papTgtCnt>0) ? "paper" : null;
				
				for(String[] table : getTranferTables(tranDataTypCd)){
					procSeq++;
					if(procSeq >= strtSeq){
						processDefragmentation(apTranBVo, table, procSeq, strtSeq);
					}
				}
			} else {
				procSeq++;
				if(procSeq >= strtSeq){
					writeSkipLog(apTranBVo, procSeq, 7);
				}
			}
			
			// 이관 종료 처리
			procSeq++;
			if(procSeq >= strtSeq){
				endTransfer(apTranBVo, apStorBVo, procSeq);
			}
			
		} catch(Exception exception){
			
			writeErrorLog(apTranBVo, procSeq, exception);
			
			// 테이블 생성되고 인덱스 생성 안되었을때 - 인덱스 생성
			if(createTables && !createIndexes){
				procSeq++;
				boolean noLog = true;// 진행사항 로그 남기지 말것
				createIndexes(apTranBVo, procSeq, strtSeq, noLog);
			}
			
			exception.printStackTrace();
			
		} finally {
			// 시스템 차단 해제
			ptSysSvc.clearSysHalt();
		}
	}
	
	/** 최대 진행 순번 구하기 - 오류난 순번 */
	private Integer queryMaxTranProcSeq(String tranId) throws SQLException {
		ApxTranVo apxTranVo = new ApxTranVo();
		apxTranVo.setTranId(tranId);
		apxTranVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApxTranDao.queryMaxTranProcSeq");
		Integer seq = commonSvc.queryInt(apxTranVo);
		return seq==null ? 0 : seq;
	}
	
	/** 7. 테이블 조각 모음 */
	private void processDefragmentation(ApTranBVo apTranBVo, String[] table, Integer procSeq, Integer strtSeq) throws SQLException{
		
		String queryId = null;
		int i, size = 0;
		if(isMssql){
			queryId = "com.innobiz.orange.web.ap.dao.ApxTranDao.processDefragmentationForMSSQL";
			size = 1;
		} else if(isOracle){
			queryId = "com.innobiz.orange.web.ap.dao.ApxTranDao.processDefragmentationForORACLE";
			size = 7;
		}
		
		if(size==0){
			writeSkipLog(apTranBVo, procSeq, 7);
			return;
		}
		
		// 인덱스 조회 - 오라클 용
		ApxTranVo apxTranVo = new ApxTranVo();
		apxTranVo.setInstanceQueryId(queryId);
		apxTranVo.setSrcTbl(table[0]);
		@SuppressWarnings("unchecked")
		List<String> indexList = isOracle ? (List<String>)commonSvc.queryList(apxTranVo) : null;
		
		// 시작 로그
		String tranCont = isKo ? "[7/7] 조각모음 : "+table[0] : "[7/7] Defragmentation of "+table[0];
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		writeStartLog(apTranBVo, procSeq, "1", tranCont);
		
		apxTranVo = new ApxTranVo();
		apxTranVo.setInstanceQueryId(queryId);
		apxTranVo.setSrcTbl(table[0]);
		for(i=1; i<=size; i++){
			apxTranVo.setSqlSeq(i);
			
			if(isOracle && (i==2 || i==6)){
				if(indexList != null){
					for(String index : indexList){
						apxTranVo.setIdxNm(index);
						commonSvc.update(apxTranVo);
					}
					apxTranVo.setIdxNm(null);
				}
			} else {
				commonSvc.update(apxTranVo);
			}
		}
		
		// 진행 로그
		writeInProcLog(apTranBVo, procSeq, "1");
		
		// 종료 로그
		writeEndLog(apTranBVo, procSeq);
	}
	
	/** 6. 이관된 데이터 삭제 */
	private void deleteTransferedData(ApTranBVo apTranBVo, String tranDataTypCd, String table[], Integer procSeq, Integer strtSeq) throws SQLException{
		
		// 문서 건수
		int copyCount = 0;
		
		// 로그 타이틀
		String tranCont = null;
		if(tranDataTypCd.equals("doc")){
			tranCont = isKo ? "[6/7] 이관된 데이터 삭제 (결재 문서): "+table[0] : "[6/7] Delete transfered data(approval) of "+table[0];
			// 문서대상건수
			copyCount = parseInt(apTranBVo.getDocTgtCnt());
		} else if(tranDataTypCd.equals("trx")){
			tranCont = isKo ? "[6/7] 이관된 데이터 삭제 (시행변환 문서): "+table[0] : "[6/7] Delete transfered data(transformed) of "+table[0];
			// 변환대상건수
			copyCount = parseInt(apTranBVo.getTrxTgtCnt());
		} else if(tranDataTypCd.equals("paper")){
			tranCont = isKo ? "[6/7] 이관된 데이터 삭제 (종이 문서): "+table[0] : "[6/7] Delete transfered data(paper) of "+table[0];
			// 종이대상건수
			copyCount = parseInt(apTranBVo.getPapTgtCnt());
		}
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		
		int pageCnt = (int)Math.ceil((double)copyCount / MAX_TRANSACTION_SIZE.intValue());
		
		// 시작 로그
		writeStartLog(apTranBVo, procSeq, Integer.toString(pageCnt), tranCont);
		
		String srcTbl = table[0];
		
		// 테이블별 이관 실행
		ApxTranVo apxTranVo = new ApxTranVo();
		apxTranVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApxTranDao.deleteTransferedData");
		apxTranVo.setTranId(apTranBVo.getTranId());
		apxTranVo.setSrcTbl(srcTbl);
		apxTranVo.setTranDataTypCd(tranDataTypCd);
		apxTranVo.setKeyCol("AP_ONGD_TRX_D".equals(srcTbl) ? "TRX_APV_NO" : "APV_NO");
		apxTranVo.setPageRowCnt(MAX_TRANSACTION_SIZE);
		
		// 1000 건 단위로 나누어 실행함 - 롤백 세크먼트 고려 - 페이징 0부터 시작함
		for(int i=0; i<pageCnt; i++){
			apxTranVo.setPageNo(Integer.valueOf(i));
			commonSvc.insert(apxTranVo);
			
			// 진행사항 로그
			writeInProcLog(apTranBVo, procSeq, Integer.toString(i+1));
		}
		
		// 종료 로그
		writeEndLog(apTranBVo, procSeq);
	}

	/** 5. 이관 문서 저장소 변경 */
	private void updateStorageOfData(ApTranBVo apTranBVo, String tranDataTypCd, Integer procSeq, Integer strtSeq) throws SQLException{
		
		// 문서 건수
		int copyCount = 0;
		
		// 로그 타이틀
		String tranCont = null;
		if(tranDataTypCd.equals("doc")){
			tranCont = isKo ? "[5/7] 이관된 문서의 저장소 변경 (결재 문서)" : "[5/7] Change the storage of the transfered data (approval)";
			// 문서대상건수
			copyCount = parseInt(apTranBVo.getDocTgtCnt());
		} else if(tranDataTypCd.equals("trx")){
			tranCont = isKo ? "[5/7] 이관된 문서의 저장소 변경 (시행변환 문서)" : "[5/7] Change the storage of the transfered data (transformed)";
			// 변환대상건수
			copyCount = parseInt(apTranBVo.getTrxTgtCnt());
		} else if(tranDataTypCd.equals("paper")){
			tranCont = isKo ? "[5/7] 이관된 문서의 저장소 변경 (종이 문서)" : "[5/7] Change the storage of the transfered data (paper)";
			// 종이대상건수
			copyCount = parseInt(apTranBVo.getPapTgtCnt());
		}
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		
		int pageCnt = (int)Math.ceil((double)copyCount / MAX_TRANSACTION_SIZE.intValue());
		
		// 시작 로그
		writeStartLog(apTranBVo, procSeq, "2", tranCont);
		
		ApxTranVo apxTranVo = new ApxTranVo();
		apxTranVo.setTranId(apTranBVo.getTranId());
		apxTranVo.setTranDataTypCd(tranDataTypCd);
		apxTranVo.setStorId(apTranBVo.getStorId());
		apxTranVo.setPageRowCnt(MAX_TRANSACTION_SIZE);
		
		// 저장소관계 - 데이터 삭제
		apxTranVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApxTranDao.deleteStorageOfData");
		// 1000 건 단위로 나누어 실행함 - 롤백 세크먼트 고려 - 페이징 0부터 시작함
		for(int i=0; i<pageCnt; i++){
			apxTranVo.setPageNo(Integer.valueOf(i));
			commonSvc.delete(apxTranVo);
		}
		writeInProcLog(apTranBVo, procSeq, "1");
		
		// 저장소관계 - 데이터 입력
		apxTranVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApxTranDao.insertStorageOfData");
		// 1000 건 단위로 나누어 실행함 - 롤백 세크먼트 고려 - 페이징 0부터 시작함
		for(int i=0; i<pageCnt; i++){
			apxTranVo.setPageNo(Integer.valueOf(i));
			commonSvc.insert(apxTranVo);
		}
		writeInProcLog(apTranBVo, procSeq, "2");
		
		// 종료 로그
		writeEndLog(apTranBVo, procSeq);
	}
	
	
	/** 3. 데이터 이관 */
	private void copyTransferingData(ApTranBVo apTranBVo, String tranDataTypCd, String table[], Integer procSeq, Integer strtSeq) throws SQLException{
		
		String srcTbl = table[0];
		String tgtTbl = table[0].replace("ONGD", apTranBVo.getStorId());
		
		// 문서 건수
		int copyCount = 0;
		
		// 로그 타이틀
		String tranCont = null;
		if(tranDataTypCd.equals("doc")){
			tranCont = isKo ? "[3/7] 데이터 이관 (결재 문서) : "+table[0] : "[3/7] Copy the \"approval\" data of "+table[0];
			// 문서대상건수
			copyCount = parseInt(apTranBVo.getDocTgtCnt());
		} else if(tranDataTypCd.equals("trx")){
			tranCont = isKo ? "[3/7] 데이터 이관 (시행변환 문서) : "+table[0] : "[3/7] Copy the \"transformed\" data of "+table[0];
			// 변환대상건수
			copyCount = parseInt(apTranBVo.getTrxTgtCnt());
		} else if(tranDataTypCd.equals("paper")){
			tranCont = isKo ? "[3/7] 데이터 이관 (종이 문서) : "+table[0] : "[3/7] Copy the \"paper\" data of "+table[0];
			// 종이대상건수
			copyCount = parseInt(apTranBVo.getPapTgtCnt());
		}
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		
		int pageCnt = (int)Math.ceil((double)copyCount / MAX_TRANSACTION_SIZE.intValue());
		
		// 시작 로그
		writeStartLog(apTranBVo, procSeq, Integer.toString(pageCnt), tranCont);
		
		// 테이블별 이관 실행
		ApxTranVo apxTranVo = new ApxTranVo();
		apxTranVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApxTranDao.copyTransferingData");
		apxTranVo.setTranId(apTranBVo.getTranId());
		apxTranVo.setTranDataTypCd(tranDataTypCd);
		apxTranVo.setSrcTbl(srcTbl);
		apxTranVo.setTgtTbl(tgtTbl);
		apxTranVo.setKeyCol("AP_ONGD_TRX_D".equals(srcTbl) ? "TRX_APV_NO" : "APV_NO");
		apxTranVo.setPageRowCnt(MAX_TRANSACTION_SIZE);
		apxTranVo.setOrderBy(table[1]);
		
		// 1000 건 단위로 나누어 실행함 - 롤백 세크먼트 고려 - 페이징 0부터 시작함
		for(int i=0; i<pageCnt; i++){
			apxTranVo.setPageNo(Integer.valueOf(i));
			commonSvc.insert(apxTranVo);
			
			// 진행사항 로그
			writeInProcLog(apTranBVo, procSeq, Integer.toString(i+1));
		}
		
		// 종료 로그
		writeEndLog(apTranBVo, procSeq);
	}
	
	/** 이관 대상 테이블 리턴 */
	private String[][] getTranferTables(String tranDataTypCd){
		
		// { 테이블ID, 키 컬럼 }
		
		if("trx".equals(tranDataTypCd)){
			return new String[][]{
					{"AP_ONGD_TRX_D", "TRX_APV_NO"},
					{"AP_ONGD_ATT_FILE_L", "APV_NO, ATT_HST_NO, ATT_SEQ"},
					{"AP_ONGD_BODY_L", "APV_NO, BODY_HST_NO"},
			};
		} else if("paper".equals(tranDataTypCd)){
			return new String[][]{
					{"AP_ONGD_B", "APV_NO"},
					{"AP_ONGD_ATT_FILE_L", "APV_NO, ATT_HST_NO, ATT_SEQ"},
					{"AP_ONGD_EXTN_DOC_D", "APV_NO"},
			};
		} else {//doc
			return new String[][]{
					{"AP_ONGD_B", "APV_NO"},
					{"AP_ONGD_APV_LN_D", "APV_NO, APV_LN_PNO, APV_LN_NO"},
					{"AP_ONGD_APV_LN_E", "APV_NO, APV_LN_PNO, APV_LN_NO"},
					{"AP_ONGD_APV_LN_H", "APV_NO, APV_LN_HST_NO, APV_LN_PNO, APV_LN_NO"},
					{"AP_ONGD_ATT_FILE_L", "APV_NO, ATT_HST_NO, ATT_SEQ"},
					{"AP_ONGD_BODY_L", "APV_NO, BODY_HST_NO"},
					{"AP_ONGD_ERP_FORM_D", "APV_NO, ERP_VA_TYP_CD"},
					{"AP_ONGD_EX_D", "APV_NO, EX_ID"},
					{"AP_ONGD_EXTN_DOC_D", "APV_NO"},
					{"AP_ONGD_PICH_D", "APV_NO, PICH_TYP_CD"},
					{"AP_ONGD_PUB_BX_CNFM_L", "PUB_BX_DEPT_ID, APV_NO, USER_UID"},
					{"AP_ONGD_RECV_DEPT_L", "APV_NO, RECV_DEPT_HST_NO, RECV_DEPT_SEQ"},
					{"AP_ONGD_REF_DOC_L", "APV_NO, REF_DOC_HST_NO, REF_APV_NO"},
					{"AP_ONGD_SEND_D", "APV_NO, SEND_SEQ"},
					{"AP_ONGD_REF_VW_D", "APV_NO, REF_VWR_UID"},
					{"AP_ONGD_REF_VW_E", "APV_NO, REF_VWR_UID"},
					{"AP_ONGD_REF_VW_H", "APV_NO, REGR_UID, REF_VWR_UID"},
//					{"AP_ONGD_HOLD_OPIN_D", "APV_NO, APVR_UID"},
//					{"AP_ONGD_PUB_BX_D", "PUB_BX_DEPT_ID, APV_NO"},
//					{"AP_ONGD_TRX_D", "TRX_APV_NO"},
			};
		}
	}
	
	/** 2. 이관 대상 추출 */
	private void extractTransferingData(ApTranBVo apTranBVo, Integer procSeq, Integer strtSeq) throws SQLException{

		// 이관진행내역(AP_TRAN_PROC_L) - 시작 로그
		String tranCont = isKo ? "[2/7] 이관 대상 추출" : "[2/7] Extract data to transfer";
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		writeStartLog(apTranBVo, procSeq, "4", tranCont);
		
		// 문서포함여부
		boolean withDoc = "Y".equals(apTranBVo.getDocInclYn());
		// 종이문서포함여부
		boolean withPapDoc = "Y".equals(apTranBVo.getPapDocInclYn());
		
		// 문서포함여부
		if(withDoc){
			
			// 2.1 - 이관대상 - 결재문서
			
			// 이관 대상 양식
			String[] formIds = null;
			
			// 이관 대상 양식 조회
			ApTranFromRVo apTranFromRVo = new ApTranFromRVo();
			apTranFromRVo.setTranId(apTranBVo.getTranId());
			@SuppressWarnings("unchecked")
			List<ApTranFromRVo> apTranFromRVoList = (List<ApTranFromRVo>)commonSvc.queryList(apTranFromRVo);
			if(apTranFromRVoList!=null && apTranFromRVoList.size()>0){
				formIds = new String[apTranFromRVoList.size()];
				for(int i=0;i<apTranFromRVoList.size(); i++){
					formIds[i] = apTranFromRVoList.get(i).getFormId();
				}
			}
			
			// 이관대상 추출 실행
			ApxTranVo apxTranVo = createTranTgtVo(apTranBVo.getCompId(), apTranBVo.getTranTgtStrtYmd(), apTranBVo.getTranTgtEndYmd(), formIds, false);
			apxTranVo.setTranId(apTranBVo.getTranId());
			apxTranVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApxTranDao.extractTransferingData");
			apxTranVo.setTranDataTypCd("doc");//이관데이터구분코드 - doc:문서, trx:시행변환, paper:종이문서
			apxTranVo.setWhereSqllet("AND DOC_TYP_CD IN('intro', 'extro')");//문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서
			if(isMssql){
				apxTranVo.setRnumSql("ROW_NUMBER() OVER(ORDER BY TRX_APV_NO)");
			} else if(isOracle){
				apxTranVo.setRnumSql("ROWNUM");
			}
			commonSvc.insert(apxTranVo);
			
			// 진행사항 로그 남기기
			writeInProcLog(apTranBVo, procSeq, "1");
			
			// 2.2 - 이관대상 - 시행 변환된 문서
			
			// 시행변환 - 이관 대상 추출 실행
			apxTranVo = new ApxTranVo();
			apxTranVo.setTranId(apTranBVo.getTranId());
			apxTranVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApxTranDao.extractTrxTransferingData");
			if(isMssql){
				apxTranVo.setRnumSql("ROW_NUMBER() OVER(ORDER BY TRX_APV_NO)");
			} else if(isOracle){
				apxTranVo.setRnumSql("ROWNUM");
			}
			commonSvc.insert(apxTranVo);
		}
		// 진행사항 로그 남기기
		writeInProcLog(apTranBVo, procSeq, "2");
		
		// 종이문서포함여부
		if(withPapDoc){
			
			// 2.3 이관대상 - 종이문서
			ApxTranVo apxTranVo = createTranTgtVo(apTranBVo.getCompId(), apTranBVo.getTranTgtStrtYmd(), apTranBVo.getTranTgtEndYmd(), null, true);
			apxTranVo.setTranId(apTranBVo.getTranId());
			apxTranVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApxTranDao.extractTransferingData");
			apxTranVo.setTranDataTypCd("paper");//이관데이터구분코드 - doc:문서, trx:시행변환, paper:종이문서
			apxTranVo.setWhereSqllet("AND DOC_TYP_CD = 'paper'");//문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서
			if(isMssql){
				apxTranVo.setRnumSql("ROW_NUMBER() OVER(ORDER BY APV_NO)");
			} else if(isOracle){
				apxTranVo.setRnumSql("ROWNUM");
			}
			commonSvc.insert(apxTranVo);
		}
		// 진행사항 로그 남기기
		writeInProcLog(apTranBVo, procSeq, "3");
		
		// 문서대상건수 - 조회
		ApTranTgtTVo apTranTgtTVo = new ApTranTgtTVo();
		apTranTgtTVo.setTranId(apTranBVo.getTranId());
		apTranTgtTVo.setTranDataTypCd("doc");
		Integer docTgtCnt = commonSvc.count(apTranTgtTVo);
		// 변환대상건수 - 조회
		apTranTgtTVo.setTranId(apTranBVo.getTranId());
		apTranTgtTVo.setTranDataTypCd("trx");
		Integer trxTgtCnt = commonSvc.count(apTranTgtTVo);
		// 종이문서대상건수 - 조회
		apTranTgtTVo.setTranId(apTranBVo.getTranId());
		apTranTgtTVo.setTranDataTypCd("paper");
		Integer papTgtCnt = commonSvc.count(apTranTgtTVo);
		
		// 이관기본(AP_TRAN_B) - 대상건수,변환건수 update
		ApTranBVo updateApTranBVo = new ApTranBVo();
		updateApTranBVo.setTranId(apTranBVo.getTranId());
		updateApTranBVo.setDocTgtCnt(docTgtCnt==null ? "0" : docTgtCnt.toString());
		updateApTranBVo.setTrxTgtCnt(trxTgtCnt==null ? "0" : trxTgtCnt.toString());
		updateApTranBVo.setPapTgtCnt(papTgtCnt==null ? "0" : papTgtCnt.toString());
		commonSvc.update(updateApTranBVo);
		
		apTranBVo.setDocTgtCnt(docTgtCnt==null ? "0" : docTgtCnt.toString());
		apTranBVo.setTrxTgtCnt(trxTgtCnt==null ? "0" : trxTgtCnt.toString());
		apTranBVo.setPapTgtCnt(papTgtCnt==null ? "0" : papTgtCnt.toString());
		
		// 진행사항 로그 남기기
		writeInProcLog(apTranBVo, procSeq, "4");
		
		// 종료 로그
		writeEndLog(apTranBVo, procSeq);
	}
	
	/** 1. 테이블 생성 */
	private void createTables(ApTranBVo apTranBVo, Integer procSeq, Integer strtSeq) throws SQLException {
		
		// { query-id, 해당 id 내의 sql 갯수 }
		String[][] tables = null;
		
		if(isMssql){
			tables = new String[][]{
					{"ApOngdApvLnD", "44"},
					{"ApOngdApvLnE", "44"},
					{"ApOngdApvLnH", "44"},
					{"ApOngdAttFileL", "11"},
					{"ApOngdB", "65"},
					{"ApOngdBodyL", "7"},
					{"ApOngdErpFormD", "5"},
					{"ApOngdExD", "5"},
					{"ApOngdExtnDocD", "13"},
					{"ApOngdHoldOpinD", "6"},
					{"ApOngdPichD", "13"},
					{"ApOngdPubBxCnfmL", "11"},
					{"ApOngdPubBxD", "10"},
					{"ApOngdRecvDeptL", "14"},
					{"ApOngdRefDocL", "8"},
					{"ApOngdRefVwD", "19"},
					{"ApOngdRefVwE", "19"},
					{"ApOngdRefVwH", "19"},
					{"ApOngdSendD", "15"},
					{"ApOngdTrxD", "9"},
			};
		} else if(isOracle){
			tables = new String[][]{
					{"ApOngdApvLnD", "46"},
					{"ApOngdApvLnE", "46"},
					{"ApOngdApvLnH", "46"},
					{"ApOngdAttFileL", "13"},
					{"ApOngdB", "67"},
					{"ApOngdBodyL", "9"},
					{"ApOngdErpFormD", "7"},
					{"ApOngdExD", "7"},
					{"ApOngdExtnDocD", "15"},
					{"ApOngdHoldOpinD", "8"},
					{"ApOngdPichD", "15"},
					{"ApOngdPubBxCnfmL", "13"},
					{"ApOngdPubBxD", "12"},
					{"ApOngdRecvDeptL", "16"},
					{"ApOngdRefDocL", "10"},
					{"ApOngdRefVwD", "21"},
					{"ApOngdRefVwE", "21"},
					{"ApOngdRefVwH", "21"},
					{"ApOngdSendD", "17"},
					{"ApOngdTrxD", "11"},
			};
		} else if(isMysql){
			tables = new String[][]{
					{"ApOngdApvLnD", "1"},
					{"ApOngdApvLnE", "1"},
					{"ApOngdApvLnH", "1"},
					{"ApOngdAttFileL", "1"},
					{"ApOngdB", "1"},
					{"ApOngdBodyL", "1"},
					{"ApOngdErpFormD", "1"},
					{"ApOngdExD", "1"},
					{"ApOngdExtnDocD", "1"},
					{"ApOngdHoldOpinD", "1"},
					{"ApOngdPichD", "1"},
					{"ApOngdPubBxCnfmL", "1"},
					{"ApOngdPubBxD", "1"},
					{"ApOngdRecvDeptL", "1"},
					{"ApOngdRefDocL", "1"},
					{"ApOngdRefVwD", "1"},
					{"ApOngdRefVwE", "1"},
					{"ApOngdRefVwH", "1"},
					{"ApOngdSendD", "1"},
					{"ApOngdTrxD", "1"},
			};
		} else {
			return;
		}
		
		// 이관진행내역(AP_TRAN_PROC_L) - 시작 로그
		String tranCont = isKo ? "[1/7] 테이블 생성" : "[1/7] Create tables";
		if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
		writeStartLog(apTranBVo, procSeq, Integer.toString(tables.length), tranCont);
		
		// 테이블 생성용
		ApStorBVo apStorBVo = new ApStorBVo();
		apStorBVo.setStorId(apTranBVo.getStorId());
		
		String sqlIdPrefix = isMssql ?
				"com.innobiz.orange.web.ap.dao.ApxTableMSSQLDao.create" : 
					"com.innobiz.orange.web.ap.dao.ApxTableORACLEDao.create";
		
		int i, size, tableSeq = 0;
		for(String[] table : tables){
			apStorBVo.setInstanceQueryId(sqlIdPrefix+table[0]);
			size = Integer.parseInt(table[1]);
			// 테이블 생성
			for(i=1;i<=size;i++){
				apStorBVo.setSqlSeq(i);
				commonSvc.update(apStorBVo);
			}
			
			// 진행사항 로그 남기기
			writeInProcLog(apTranBVo, procSeq, Integer.toString(++tableSeq));
		}
		
		// 종료 로그
		writeEndLog(apTranBVo, procSeq);
	}
	
	/** 4. 인덱스 생성 */
	private void createIndexes(ApTranBVo apTranBVo, Integer procSeq, Integer strtSeq, boolean noLog) throws SQLException{
		
		// { query-id, 해당 id 내의 sql 갯수 }
		String[][] indexes = null;
		
		if(isMssql){
			indexes = new String[][]{
					{"ApOngdApvLnD", "2"},
					{"ApOngdApvLnH", "1"},
					{"ApOngdB", "7"},
					{"ApOngdExtnDocD", "1"},
					{"ApOngdPichD", "2"},
					{"ApOngdPubBxD", "1"},
					{"ApOngdRefVwD", "1"},
					{"ApOngdRefVwE", "1"},
					{"ApOngdSendD", "1"},
					{"ApOngdTrxD", "1"},
			};
		} else if(isOracle){
			indexes = new String[][]{
					{"ApOngdApvLnD", "2"},
					{"ApOngdApvLnH", "1"},
					{"ApOngdB", "7"},
					{"ApOngdExtnDocD", "1"},
					{"ApOngdPichD", "2"},
					{"ApOngdPubBxD", "1"},
					{"ApOngdRefVwD", "1"},
					{"ApOngdRefVwE", "1"},
					{"ApOngdSendD", "1"},
					{"ApOngdTrxD", "1"},
			};
		} else if(isMysql){
			indexes = new String[][]{
					{"ApOngdApvLnD", "2"},
					{"ApOngdApvLnH", "1"},
					{"ApOngdB", "7"},
					{"ApOngdExtnDocD", "1"},
					{"ApOngdPichD", "2"},
					{"ApOngdPubBxD", "1"},
					{"ApOngdRefVwD", "1"},
					{"ApOngdRefVwE", "1"},
					{"ApOngdSendD", "1"},
					{"ApOngdTrxD", "1"},
			};
		} else {
			return;
		}
		
		if(!noLog){
			// 이관진행내역(AP_TRAN_PROC_L) - 시작 로그
			String tranCont = isKo ? "[4/7] 인덱스 생성" : "[4/7] Create indexes";
			if(procSeq.intValue() == strtSeq.intValue()) tranCont += " [Retry]";
			writeStartLog(apTranBVo, procSeq, Integer.toString(indexes.length), tranCont);
		}
		
		// 인덱스 생성용
		ApStorBVo apStorBVo = new ApStorBVo();
		apStorBVo.setStorId(apTranBVo.getStorId());
		
		String sqlIdPrefix = isMssql ?
				"com.innobiz.orange.web.ap.dao.ApxTableMSSQLDao.index" : 
					"com.innobiz.orange.web.ap.dao.ApxTableORACLEDao.index";
		
		int i, size, indexSeq = 0;
		for(String[] index : indexes){
			apStorBVo.setInstanceQueryId(sqlIdPrefix+index[0]);
			size = Integer.parseInt(index[1]);
			// 인덱스 생성
			for(i=1;i<=size;i++){
				apStorBVo.setSqlSeq(i);
				commonSvc.update(apStorBVo);
			}
			
			if(!noLog){
				// 진행사항 로그 남기기
				writeInProcLog(apTranBVo, procSeq, Integer.toString(++indexSeq));
			}
		}
		
		if(!noLog){
			// 종료 로그
			writeEndLog(apTranBVo, procSeq);
		}
	}
	
	/** 이관 시작 처리 */
	private void startTransfer(ApTranBVo apTranBVo) throws SQLException{
		ApTranBVo updateApTranBVo = new ApTranBVo();
		updateApTranBVo.setTranId(apTranBVo.getTranId());
		updateApTranBVo.setTranStrtDt("sysdate");
		//이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
		updateApTranBVo.setTranProcStatCd("processing");
		commonSvc.update(updateApTranBVo);
	}
	
	/** 이관 종료 처리 */
	private void endTransfer(ApTranBVo apTranBVo, ApStorBVo apStorBVo, Integer procSeq) throws SQLException{
		ApTranBVo updateApTranBVo = new ApTranBVo();
		updateApTranBVo.setTranId(apTranBVo.getTranId());
		updateApTranBVo.setTranEndDt("sysdate");
		//이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
		updateApTranBVo.setTranProcStatCd("completed");
		commonSvc.update(updateApTranBVo);
		
		// 이관된 문서 수
		int docTgtCnt = parseInt(apTranBVo.getDocTgtCnt());
		int papTgtCnt = parseInt(apTranBVo.getPapTgtCnt());
		int copyCnt = docTgtCnt + papTgtCnt;
		
		// 회사별 문서건수 수정
		// 조회
		ApStorCompRVo apStorCompRVo = new ApStorCompRVo();
		apStorCompRVo.setStorId(apTranBVo.getStorId());
		apStorCompRVo.setCompId(apTranBVo.getCompId());
		apStorCompRVo = (ApStorCompRVo)commonSvc.queryVo(apStorCompRVo);
		// 수정
		int compDocCnt = parseInt(apStorCompRVo.getCompDocCnt());
		apStorCompRVo.setCompDocCnt(Integer.toString(copyCnt + compDocCnt));
		commonSvc.update(apStorCompRVo);
		
		// 저장소별 문서건수 수정
		ApStorBVo updateApStorBVo = new ApStorBVo();
		updateApStorBVo.setStorId(apStorBVo.getStorId());
		updateApStorBVo.setAllDocCnt(Integer.toString(copyCnt + parseInt(apStorBVo.getAllDocCnt())));
		commonSvc.update(updateApStorBVo);
		
		// 이관 완료 로그
		ApTranProcLVo apTranProcLVo = new ApTranProcLVo();
		apTranProcLVo.setTranId(apTranBVo.getTranId());
		apTranProcLVo.setSeq(procSeq.toString());
		apTranProcLVo.setTranCont(isKo ? "이관 완료" : "Transfering completed");
		apTranProcLVo.setStrtDt("sysdate");
		apTranProcLVo.setEndDt("sysdate");
		commonSvc.insert(apTranProcLVo);
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
	public ApxTranVo createTranTgtVo(String compId, String durStrtDt, String durEndDt, String[] formIds, boolean isPaper) throws SQLException{
		
		ApxTranVo apxTranVo = new ApxTranVo();
		apxTranVo.setCompId(compId);
		
		if(durStrtDt != null || durEndDt != null){
			
			boolean hasDate = false;
			if(durStrtDt != null && !durStrtDt.isEmpty() && durStrtDt.length()==10){
				apxTranVo.setDurStrtDt(durStrtDt+" 00:00:00");
				hasDate = true;
			}
			if(durEndDt != null && !durEndDt.isEmpty() && durEndDt.length()==10){
				apxTranVo.setDurEndDt(durEndDt+" 23:59:59");
				hasDate = true;
			}
			
			if(hasDate){
				if(isPaper){
					apxTranVo.setRegRecLstBaseDt("CMPL_DT");
					apxTranVo.setRecvRecLstBaseDt("RECV_DT");
				} else {
					Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, compId);
					// 등록대장 채번 기준일 - stDt:기안일, enDt:완료일
					if("stDt".equals(optConfigMap.get("regRecLstBaseDt"))){
						apxTranVo.setRegRecLstBaseDt("MAK_DT");
					} else {
						apxTranVo.setRegRecLstBaseDt("CMPL_DT");
					}
					// 접수(배부) 대장 채번 기준일 - enDt:완료일, recvDt:접수(배부)일
					if("stDt".equals(optConfigMap.get("recvRecLstBaseDt"))){
						apxTranVo.setRecvRecLstBaseDt("CMPL_DT");
					} else {
						apxTranVo.setRecvRecLstBaseDt("RECV_DT");
					}
				}
			}
		}
		
		// 양식 목록
		if(!isPaper && formIds!=null && formIds.length>0){
			apxTranVo.setFormIdList(ArrayUtil.toList(formIds, true));
		}
		
		return apxTranVo;
	}
	
	/** 시작 로그 */
	private void writeStartLog(ApTranBVo apTranBVo, Integer procSeq, String allCnt, String tranCont) throws SQLException{
		ApTranProcLVo apTranProcLVo = new ApTranProcLVo();
		apTranProcLVo.setTranId(apTranBVo.getTranId());
		apTranProcLVo.setSeq(procSeq.toString());
		apTranProcLVo.setTranCont(tranCont);
		apTranProcLVo.setAllCnt(allCnt);
		apTranProcLVo.setProcCnt("0");
		apTranProcLVo.setStrtDt("sysdate");
		commonSvc.insert(apTranProcLVo);
	}
	
	/** 진행 로그 남기기 */
	private void writeInProcLog(ApTranBVo apTranBVo, Integer procSeq, String procCnt) throws SQLException{
		ApTranProcLVo apTranProcLVo = new ApTranProcLVo();
		apTranProcLVo.setTranId(apTranBVo.getTranId());
		apTranProcLVo.setSeq(procSeq.toString());
		apTranProcLVo.setProcCnt(procCnt);
		commonSvc.update(apTranProcLVo);
	}
	
	/** 종료 로그 남기기 */
	private void writeEndLog(ApTranBVo apTranBVo, Integer procSeq) throws SQLException{
		ApTranProcLVo apTranProcLVo = new ApTranProcLVo();
		apTranProcLVo.setTranId(apTranBVo.getTranId());
		apTranProcLVo.setSeq(procSeq.toString());
		apTranProcLVo.setEndDt("sysdate");
		commonSvc.update(apTranProcLVo);
	}
	
	/** 생략 로그 남기기 */
	private void writeSkipLog(ApTranBVo apTranBVo, Integer procSeq, int majorSeq) throws SQLException{
		ApTranProcLVo apTranProcLVo = new ApTranProcLVo();
		apTranProcLVo.setTranId(apTranBVo.getTranId());
		apTranProcLVo.setSeq(procSeq.toString());
		apTranProcLVo.setTranCont(isKo ? "["+majorSeq+"/7] 생략" : "["+majorSeq+"/7] Skip");
		apTranProcLVo.setStrtDt("sysdate");
		apTranProcLVo.setEndDt("sysdate");
		commonSvc.insert(apTranProcLVo);
	}
	
	/** 오류 로그 남기기 */
	private void writeErrorLog(ApTranBVo apTranBVo, Integer procSeq, Exception exception) throws SQLException{
		
		ApTranBVo errApTranBVo = new ApTranBVo();
		errApTranBVo.setTranId(apTranBVo.getTranId());
		errApTranBVo.setTranEndDt("sysdate");
		// 이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
		errApTranBVo.setTranProcStatCd("error");
		commonSvc.update(errApTranBVo);
		
		// Exception 위치 잡기
		String where = null, thisClsNm = this.getClass().getCanonicalName();
		String message = exception.getMessage()==null ? exception.getClass().getCanonicalName() : exception.getMessage();
		for(StackTraceElement trace : exception.getStackTrace()){
			if(trace.getClassName().equals(thisClsNm)){
				where = trace.toString();
				break;
			}
		}
		
		// 오류 로그 저장
		ApTranProcLVo apTranProcLVo = new ApTranProcLVo();
		apTranProcLVo.setTranId(apTranBVo.getTranId());
		apTranProcLVo.setSeq(procSeq.toString());
		apTranProcLVo.setEndDt("sysdate");
		apTranProcLVo.setErrCont(where != null ? (where+": "+message) : message);
		apTranProcLVo.setErrYn("Y");
		commonSvc.update(apTranProcLVo);
	}
}
