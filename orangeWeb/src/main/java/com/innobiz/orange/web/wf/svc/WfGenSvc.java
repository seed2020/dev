package com.innobiz.orange.web.wf.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wf.utils.FormUtil;
import com.innobiz.orange.web.wf.utils.WfConstant;
import com.innobiz.orange.web.wf.vo.WfFormBVo;
import com.innobiz.orange.web.wf.vo.WfFormColmLVo;
import com.innobiz.orange.web.wf.vo.WfFormGenBVo;
import com.innobiz.orange.web.wf.vo.WfFormGenLVo;
import com.innobiz.orange.web.wf.vo.WfFormGenProcLVo;
import com.innobiz.orange.web.wf.vo.WfFormGenTgtTVo;
import com.innobiz.orange.web.wf.vo.WfFormLstDVo;
import com.innobiz.orange.web.wf.vo.WfFormRegDVo;
import com.innobiz.orange.web.wf.vo.WfWorksLVo;
import com.innobiz.orange.web.wf.vo.WfxTranVo;

@Service
public class WfGenSvc {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WfGenSvc.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
//	/** 공통 서비스 */
//	@Autowired
//	private WfCmSvc wfCmSvc;
	
	/** 양식 서비스 */
	@Resource(name = "wfFormSvc")
	private WfFormSvc wfFormSvc;
	
	/** 관리 서비스 */
	@Resource(name = "wfAdmSvc")
	private WfAdmSvc wfAdmSvc;
	
	/** contextProperties */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
	
	private LinkedList<String> genIdQueue = new LinkedList<String>();
	
	private GenerateThread generateThread = null;
	
	/** 한번에 처리할 데이터 처리 최대 갯수 */
	private static final Integer MAX_TRANSACTION_SIZE = 1000;
	
	boolean isMssql = false;
	boolean isOracle = false;
	boolean isMysql = false;
	boolean isKo = false;
	
	/** 양식 생성 기본 데이터 저장 */
	public void saveWfFormGenBVo(HttpServletRequest request, QueryQueue queryQueue, UserVo userVo, String genId, String defragYn) throws SQLException, CmException{
		
		// 생성기본(WF_FORM_GEN_B) 테이블 - INSERT
		WfFormGenBVo wfFormGenBVo = new WfFormGenBVo();
		wfFormGenBVo.setGenId(genId);
		wfFormGenBVo.setCompId(userVo.getCompId());
		wfFormGenBVo.setProcStatCd("preparing");
		wfFormGenBVo.setDefragYn(defragYn);
		wfFormGenBVo.setRegrUid(userVo.getUserUid());
		wfFormGenBVo.setRegDt("sysdate");
		queryQueue.insert(wfFormGenBVo);
	}
	
	/** 양식 생성 목록 데이터 저장 */
	public void saveWfFormGenLVo(HttpServletRequest request, QueryQueue queryQueue, UserVo userVo, String genId, String formNo) throws SQLException, CmException{
		WfFormBVo wfFormBVo = new WfFormBVo();
		wfFormBVo.setCompId(userVo.getCompId());
		wfFormBVo.setQueryLang(userVo.getLangTypCd());
		wfFormBVo.setFormNo(formNo);
		wfFormBVo=(WfFormBVo)commonDao.queryVo(wfFormBVo);
		
		if(wfFormBVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		// 생성목록(WF_FORM_GEN_L) 테이블 - INSERT
		WfFormGenLVo wfFormGenLVo = new WfFormGenLVo();
		wfFormGenLVo.setGenId(genId);
		wfFormGenLVo.setFormNo(formNo);
		wfFormGenLVo.setCompId(userVo.getCompId());
		wfFormGenLVo.setFormRescId(wfFormBVo.getRescId());
		wfFormGenLVo.setProcStatCd("preparing");
		wfFormGenLVo.setRegDt("sysdate");
		queryQueue.insert(wfFormGenLVo);
	}
	
	/** 생성항목 더하기 */
	public void addGenId(String genId){
		synchronized (genIdQueue) {
			genIdQueue.add(genId);
		}
		// 초기화
		if(generateThread == null){
			isMssql = "mssql".equals(contextProperties.getProperty("dbms"));
			isOracle = "oracle".equals(contextProperties.getProperty("dbms"));
			isMysql = "mysql".equals(contextProperties.getProperty("dbms"));
			isKo = "ko".equals(contextProperties.getProperty("login.default.lang", "ko"));
			
			generateThread = new GenerateThread();
			generateThread.start();
		} else {
			synchronized(generateThread){
				generateThread.notify();
			}
		}
	}
	
	/** 생성 진행 */
	private void processGenerate(String genId) throws SQLException{
		
		// 생성기본(WF_FORM_GEN_B) - 조회
		WfFormGenBVo wfFormGenBVo = new WfFormGenBVo();
		wfFormGenBVo.setGenId(genId);
		wfFormGenBVo = (WfFormGenBVo)commonDao.queryVo(wfFormGenBVo);
		
		if(wfFormGenBVo == null){
			LOGGER.error("NO generate data(WF_FORM_GEN_B) for genId:"+genId);
			return;
		}
		
		//생성진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러 
		if("processing".equals(wfFormGenBVo.getProcStatCd())){
			LOGGER.error("Generateing is already started ! - genId:"+genId);
			return;
		} else if("completed".equals(wfFormGenBVo.getProcStatCd())){
			LOGGER.error("Generateing is already completed ! - genId:"+genId);
			return;
		}
		
		WfFormGenLVo wfFormGenLVo = new WfFormGenLVo();
		wfFormGenLVo.setGenId(genId);
		if(commonDao.count(wfFormGenLVo)==0){
			LOGGER.error("NO generate data(WF_FORM_GEN_L) for genId:"+genId);
			return;
		}
		// 시스템 차단
		//ptSysSvc.setSysHalt(wfFormGenBVo.getUserUid(), "dm", "cm.msg.halt.dmGen");
		
		@SuppressWarnings("unchecked")
		List<WfFormGenLVo> wfFormGenLVoList = (List<WfFormGenLVo>)commonDao.queryList(wfFormGenLVo);
		WfFormBVo wfFormBVo;
		String formNo;
		
		// 생성 시작 처리
		startGenerate(genId);
					
		for(WfFormGenLVo storedWfFormGenLVo : wfFormGenLVoList){
			formNo=storedWfFormGenLVo.getFormNo();
			wfFormBVo = wfFormSvc.getWfFormBVo(null, formNo, null);
			processGenerate(wfFormGenBVo, storedWfFormGenLVo, wfFormBVo, formNo, storedWfFormGenLVo.getProcStatCd());
		}
		
		// 생성 완료 처리
		endGenerate(genId);
		
	}
	
	/** 생성 진행 */
	private void processGenerate(WfFormGenBVo wfFormGenBVo, WfFormGenLVo wfFormGenLVo, WfFormBVo wfFormBVo, String formNo, String procStatCd) throws SQLException{
		
		///////////////////////////////////////////////////////////////////////
		//
		//  생성 작업
		
		// 작업 시퀀스
		Integer procSeq = 0;
		// 시작 작업 시퀀스 - 오류 발생시 재 생성 목적
		Integer strtSeq = 0;
		
		String formGenId=wfFormBVo.getGenId();
		
		// 최초 테이블 생성여부
		boolean firstTables = formGenId!=null && !formGenId.isEmpty();
		
		// 인덱스 추가 여부[ORACLE, MYSQL 은 테이블 생성하면서 인덱스 생성]
		//boolean createIndex = isMssql;
		
		// 전체 작업 시퀀스
		Integer totalSeq=firstTables ? 7 : 3;
		
		// ORACLE, MYSQL 은 인덱스를 테이블 생성하면서 같이 생성하므로 전체 작업 시퀀스를 1 빼준다.
		//if(!createIndex) totalSeq-=1;
		
		// 생성ID
		String genId = wfFormGenLVo.getGenId();
		
		try {
			
			// 양식의 사용여부 'N'로 변경 - 접근불가
			wfAdmSvc.updateWfFormBVo(formNo, null, null, "N", null);
			
			// 시스템 차단
			//ptSysSvc.setSysHalt(dmTranBVo.getUserUid(), "dm", "cm.msg.halt.dmTran");
			
			//생성진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러 
			if("error".equals(procStatCd)){
				// 오류의 경우 마지막 작업 시퀀스를 조회하여 해당 시퀀스 부터 작업 시작함
				strtSeq = queryMaxGenProcSeq(genId, formNo);
				if(strtSeq == null) strtSeq = 0;
				else {
					// 오류 로그 삭제 - 로그 중첩 insert 방지
					WfFormGenProcLVo wfFormGenProcLVo = new WfFormGenProcLVo();
					wfFormGenProcLVo.setGenId(genId);
					wfFormGenProcLVo.setFormNo(formNo);
					wfFormGenProcLVo.setSeq(strtSeq.toString());
					commonDao.delete(wfFormGenProcLVo);
				}
			}
			
			// 배포할 테이블 컬럼목록 조회
			List<WfFormColmLVo> colmVoList=wfAdmSvc.getColmVoList(formNo, "Y");
			List<WfFormColmLVo> dbColmList=getDbColmList(colmVoList, WfConstant.EXCLUDE_SELECT); // 파일 컬럼은 생성에서 제외
			
			// 테이블ID 생성
			wfFormGenLVo.setFormId(wfFormBVo.getFormId());
			
			// 1. 테이블 생성
			if(!firstTables){
				procSeq++;
				if(procSeq >= strtSeq){
					createTables(wfFormGenLVo, formNo, procSeq, strtSeq, totalSeq, dbColmList, firstTables);
				}else{
					// SKIP
					writeSkipLog(genId, formNo, procSeq, procSeq);
				}
				procSeq++;
				if(procSeq >= strtSeq){// 인덱스 생성
					createIndexes(wfFormGenLVo, formNo, procSeq, strtSeq, totalSeq, false, firstTables);
				}else{
					// SKIP
					writeSkipLog(genId, formNo, procSeq, procSeq);
				}
				
				
			} else {
				procSeq++;
				if(procSeq >= strtSeq){ // 데이터 복사 대상 저장
					extractTransData(wfFormGenLVo, formGenId, formNo, procSeq, strtSeq, totalSeq);
				}else{
					// SKIP
					writeSkipLog(genId, formNo, procSeq, procSeq);
				}
				
				procSeq++;
				if(procSeq >= strtSeq){// 데이터 백업
					backupTables(wfFormGenLVo, formNo, procSeq, strtSeq, totalSeq);
				}else{
					// SKIP
					writeSkipLog(genId, formNo, procSeq, procSeq);
				}
				procSeq++;
				if(procSeq >= strtSeq){// 테이블 생성
					createTables(wfFormGenLVo, formNo, procSeq, strtSeq, totalSeq, dbColmList, firstTables);
				}else{
					// SKIP
					writeSkipLog(genId, formNo, procSeq, procSeq);
				}
				
				procSeq++;
				if(procSeq >= strtSeq){ // 데이터 복사
					copyTransData(wfFormGenLVo, formGenId, formNo, procSeq, strtSeq, totalSeq, colmVoList);
				}else{
					// SKIP
					writeSkipLog(genId, formNo, procSeq, procSeq);
				}
				procSeq++;
				if(procSeq >= strtSeq){// 인덱스 생성
					createIndexes(wfFormGenLVo, formNo, procSeq, strtSeq, totalSeq, false, firstTables);
				}else{
					// SKIP
					writeSkipLog(genId, formNo, procSeq, procSeq);
				}
				
				procSeq++;
				if(procSeq >= strtSeq){// 백업테이블 제거
					String genCont = isKo ? "["+procSeq+"/"+totalSeq+"] 백업테이블 제거" : "["+procSeq+"/"+totalSeq+"] Backup Table Drop";			
					if(procSeq.intValue() == strtSeq.intValue()) genCont += " [Retry]";
					writeStartLog(genId, formNo, procSeq, "1", genCont);
					
					// 백업 테이블 제거
					wfFormGenLVo.setInstanceQueryId("com.innobiz.orange.web.wf.dao.WfxTableDao.dropWfWorksDBak");
					commonSvc.update(wfFormGenLVo);
					
					// 진행사항 로그 남기기
					writeInProcLog(genId, formNo, procSeq, "1");
					
					// 종료 로그
					writeEndLog(genId, formNo, procSeq);
					
				}else{
					// SKIP
					writeSkipLog(genId, formNo, procSeq, procSeq);
				}
				
			}
			
			procSeq++;
			if(procSeq >= strtSeq){// 이력 저장
				saveHstList(genId, formNo, totalSeq, procSeq, strtSeq, false);
			}else{
				// SKIP
				writeSkipLog(genId, formNo, procSeq, procSeq);
			}
			
			// 테이블 조각 모음
			/*if("Y".equals(wfFormGenBVo.getDefragYn())){
				
				for(String[] table : getGenferTables(false)){
					procSeq++;
					if(procSeq >= strtSeq){
						processDefragmentation(srcTblId, wfFormGenBVo, table, procSeq, strtSeq);
					}
				}
			} else {
				procSeq++;
				if(procSeq >= strtSeq){
					writeSkipLog(genId, formNo, procSeq, procSeq);
				}
			}*/
			// 생성 종료 처리
			procSeq++;
			if(procSeq >= strtSeq){
				// 양식의 생성ID 및 사용여부 'Y'로 변경
				wfAdmSvc.updateWfFormBVo(formNo, genId, "sysdate", "Y", "D");
				endGenerate(genId, formNo, procSeq);
			}
			
		} catch(Exception exception){
			writeErrorLog(genId, formNo, procSeq, exception);
			
			exception.printStackTrace();
		} finally {
			// 시스템 차단 해제
			//ptSysSvc.clearSysHalt();
		}
	}
	
	/** 최대 진행 순번 구하기 - 오류난 순번 */
	private Integer queryMaxGenProcSeq(String genId, String formNo) throws SQLException {
		WfFormGenProcLVo wfFormGenProcLVo = new WfFormGenProcLVo();
		wfFormGenProcLVo.setGenId(genId);
		wfFormGenProcLVo.setFormNo(formNo);
		wfFormGenProcLVo.setInstanceQueryId("com.innobiz.orange.web.wf.dao.WfxTranDao.queryMaxGenProcSeq");
		Integer seq = commonDao.queryInt(wfFormGenProcLVo);
		return seq==null ? 0 : seq;
	}
	
	/** 1.1 테이블 생성 */
	private void createTables(WfFormGenLVo wfFormGenLVo, String formNo, Integer procSeq, Integer strtSeq, Integer totalSeq, List<WfFormColmLVo> dbColmList, boolean first) throws SQLException {
				
		String[][] tables = null;
		
		if(isMssql){
			// { query-id, 해당 id 내의 sql 갯수 }
			tables = new String[][]{
					{"WfWorksL", "16"},
					{"WfWorksCodeL", "7"},
					{"WfWorksFileD", "12"},
					{"WfWorksImgD", "10"},
					{"WfWorksReadH", "5"},
					{"WfWorksD", "11"}					 
			};
		} else if(isOracle){
			// { query-id, 해당 id 내의 sql 갯수 }
			tables = new String[][]{
					{"WfWorksL", "16"},
					{"WfWorksCodeL", "9"},
					{"WfWorksFileD", "14"},
					{"WfWorksImgD", "12"},
					{"WfWorksReadH", "7"},
					{"WfWorksD", "11"}					 
			};
		} else if(isMysql){
			// { query-id, 해당 id 내의 sql 갯수 }
			tables = new String[][]{
					{"WfWorksL", "1"},
					{"WfWorksCodeL", "3"},
					{"WfWorksFileD", "3"},
					{"WfWorksImgD", "3"},
					{"WfWorksReadH", "3"},
					{"WfWorksD", "1"}					 
			};
		} else {
			return;
		}
		
		String genId = wfFormGenLVo.getGenId();
		// 생성진행내역(WF_FORM_GEN_PROC_L) - 시작 로그
		String genCont = isKo ? "["+procSeq+"/"+totalSeq+"] 테이블 생성" : "["+procSeq+"/"+totalSeq+"] Create tables";
		if(procSeq.intValue() == strtSeq.intValue()) genCont += " [Retry]";
		writeStartLog(genId, formNo, procSeq, first ? "1" : Integer.toString(tables.length), genCont);
		
		// 테이블 생성용
		String sqlIdPrefix = "com.innobiz.orange.web.wf.dao.WfxTableDao.create";
		
		// 컬럼 목록 세팅
		wfFormGenLVo.setColmVoList(dbColmList);
		
		int i, size, idx=0, tableSeq = 0;;
		for(String[] table : tables){
			if(first && idx<5) {
				idx++;
				continue;
			}
			wfFormGenLVo.setInstanceQueryId(sqlIdPrefix+table[0]);
			size = Integer.parseInt(table[1]);
			
			// 테이블 생성
			for(i=1;i<=size;i++){
				wfFormGenLVo.setSqlSeq(i);
				commonSvc.update(wfFormGenLVo);
			}
			idx++;
			
			// 진행사항 로그 남기기
			writeInProcLog(genId, formNo, procSeq, Integer.toString(++tableSeq));
		}
		
		// 시퀀스 CM_SEQ 데이터 생성
		if(!first){
			sqlIdPrefix = "com.innobiz.orange.web.wf.dao.WfxTableDao.insertCmSeq";
			
			WfFormGenLVo seqVo=new WfFormGenLVo();
			seqVo.setFormId(wfFormGenLVo.getFormId());
			seqVo.setInstanceQueryId(sqlIdPrefix);
			commonSvc.update(seqVo);
		}
		
		// 종료 로그
		writeEndLog(genId, formNo, procSeq);
	}
	
	/** 1.1 이력 저장 */
	public void saveHstList(String genId, String formNo, Integer totalSeq, Integer procSeq, Integer strtSeq, boolean noLog) throws SQLException{
		if(!noLog){
			String genCont = isKo ? "["+procSeq+"/"+totalSeq+"] 이력 저장" : "["+procSeq+"/"+totalSeq+"] Save history";			
			if(procSeq.intValue() == strtSeq.intValue()) genCont += " [Retry]";
			writeStartLog(genId, formNo, procSeq, "1", genCont);
		}
		QueryQueue queryQueue = new QueryQueue();
		
		// 컬럼 목록 전체 조회
		List<WfFormColmLVo> colmVoList=wfAdmSvc.getColmVoList(formNo, null);
		for(WfFormColmLVo storedWfFormColmLVo : colmVoList){
			storedWfFormColmLVo.setGenId(genId);
			storedWfFormColmLVo.setHst(true);
			queryQueue.insert(storedWfFormColmLVo);
		}
		
		// 목록 데이터 전체 조회
		List<WfFormLstDVo> wfFormLstDVoList=wfAdmSvc.getWfFormLstDVoList(null, null, formNo, null, null, false);
		for(WfFormLstDVo storedWfFormLstDVo : wfFormLstDVoList){
			storedWfFormLstDVo.setGenId(genId);
			storedWfFormLstDVo.setHst(true);
			queryQueue.insert(storedWfFormLstDVo);
		}
				
		// 양식등록상세 조회
		WfFormRegDVo wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(null, formNo, false, null);
		if(wfFormRegDVo!=null){
			wfFormRegDVo.setGenId(genId);
			wfFormRegDVo.setHst(true);
			queryQueue.insert(wfFormRegDVo);
		}
		// 양식모바일상세 조회
		WfFormRegDVo wfFormMobDVo = wfAdmSvc.getWfFormRegDVo(null, formNo, false, Boolean.TRUE);
		if(wfFormMobDVo!=null){
			wfFormMobDVo.setGenId(genId);
			wfFormMobDVo.setHst(true);
			wfFormMobDVo.setMob(true); // 모바일
			queryQueue.insert(wfFormMobDVo);
		}
				
		commonDao.execute(queryQueue);
		
		if(!noLog){
			writeInProcLog(genId, formNo, procSeq, "1");
			// 종료 로그
			writeEndLog(genId, formNo, procSeq);
		}
	}
	
	/** 1.2 데이터 백업 */
	private void backupTables(WfFormGenLVo wfFormGenLVo, String formNo, Integer procSeq, Integer strtSeq, Integer totalSeq) throws SQLException {
		String[][] tables = null;
		
		if(isMssql){
			// { query-id, 해당 id 내의 sql 갯수 }
			tables = new String[][]{
					{"WfWorksD", "2"}
			};
		} else if(isOracle){
			// { query-id, 해당 id 내의 sql 갯수 }
			tables = new String[][]{
					{"WfWorksD", "2"}
			};
		} else if(isMysql){
			// { query-id, 해당 id 내의 sql 갯수 }
			tables = new String[][]{
					{"WfWorksD", "2"}
			};
		} else {
			return;
		}
		
		String genId = wfFormGenLVo.getGenId();
		// 생성진행내역(WF_FORM_GEN_PROC_L) - 시작 로그
		String genCont = isKo ? "["+procSeq+"/"+totalSeq+"] 데이터 백업" : "["+procSeq+"/"+totalSeq+"] Backup tables";		
		if(procSeq.intValue() == strtSeq.intValue()) genCont += " [Retry]";
		writeStartLog(genId, formNo, procSeq, Integer.toString(tables.length), genCont);
		
		// 테이블 백업용
		String sqlIdPrefix = "com.innobiz.orange.web.wf.dao.WfxTableDao.backup";
		
		int i, size, tableSeq = 0;
		for(String[] table : tables){
			wfFormGenLVo.setInstanceQueryId(sqlIdPrefix+table[0]);
			size = Integer.parseInt(table[1]);
			
			// 테이블 생성
			for(i=1;i<=size;i++){
				wfFormGenLVo.setSqlSeq(i);
				commonSvc.update(wfFormGenLVo);
			}
			
			// 진행사항 로그 남기기
			writeInProcLog(genId, formNo, procSeq, Integer.toString(++tableSeq));
						
		}
		
		// 종료 로그
		writeEndLog(genId, formNo, procSeq);
	}
	
	/** 1.3 데이터 복사대상 저장 */
	private void extractTransData(WfFormGenLVo wfFormGenLVo, String formGenId, String formNo, Integer procSeq, Integer strtSeq, Integer totalSeq) throws SQLException {
		String genId=wfFormGenLVo.getGenId();
		// 데이터 복사대상 (WF_FORM_GEN_PROC_L) - 시작 로그
		String genCont = isKo ? "["+procSeq+"/"+totalSeq+"] 데이터 복사대상 저장" : "["+procSeq+"/"+totalSeq+"] Transfer Data save";		
		if(procSeq.intValue() == strtSeq.intValue()) genCont += " [Retry]";
		writeStartLog(genId, formNo, procSeq, "1", genCont);
				
		// 양식생성대상임시
		WfxTranVo wfxTranVo = new WfxTranVo(formNo);
		wfxTranVo.setGenId(genId);
		wfxTranVo.setInstanceQueryId("com.innobiz.orange.web.wf.dao.WfxTranDao.extractTransferingData");
		if(isMssql){
			wfxTranVo.setRnumSql("ROW_NUMBER() OVER(ORDER BY T.WORK_NO)");
		} else if(isOracle){
			wfxTranVo.setRnumSql("ROWNUM");
		} else if(isMysql){
			wfxTranVo.setRnumSql("@RNUM := @RNUM + 1");
		}
		commonSvc.insert(wfxTranVo);
		
		// 진행사항 로그 남기기
		writeInProcLog(genId, formNo, procSeq, "1");
		
		// 종료 로그
		writeEndLog(genId, formNo, procSeq);
		
	}
	
	/** 1.3 데이터 복사 - [컬럼 갯수가 달라지면(추가되면) json 데이터를 이전. 그게 아니하면 전체 insert select 문 실행] */
	@SuppressWarnings("unchecked")
	private void copyTransData(WfFormGenLVo wfFormGenLVo, String formGenId, String formNo, Integer procSeq, Integer strtSeq, Integer totalSeq, List<WfFormColmLVo> colmVoList) throws SQLException {
		
		// 배포할 생성ID
		String genId = wfFormGenLVo.getGenId();
				
		// 양식생성대상임시
		WfFormGenTgtTVo wfFormGenTgtTVo = new WfFormGenTgtTVo();
		wfFormGenTgtTVo.setGenId(genId);
		wfFormGenTgtTVo.setFormNo(formNo);
		
		// 복사할 데이터 건수
		int copyCount=commonSvc.count(wfFormGenTgtTVo);
		int pageCnt = (int)Math.ceil((double)copyCount / MAX_TRANSACTION_SIZE.intValue());
		
		// 생성진행내역(WF_FORM_GEN_PROC_L) - 시작 로그
		String genCont = isKo ? "["+procSeq+"/"+totalSeq+"] 데이터 복사" : "["+procSeq+"/"+totalSeq+"] Transfer tables";		
		if(procSeq.intValue() == strtSeq.intValue()) genCont += " [Retry]";
				
		writeStartLog(genId, formNo, procSeq, copyCount==0 ? "1" : Integer.toString(pageCnt) , genCont);
		
		if(copyCount>0){
			
			// 현재 테이블 컬럼목록 조회
			List<WfFormColmLVo> currList=wfAdmSvc.getCurrColmVoList(formGenId, formNo, "Y");
			//List<WfFormColmLVo> dbColmList=getDbColmList(colmVoList, new String[]{"file"}); // 파일 컬럼은 생성에서 제외
			
			// 현재 테이블 컬럼명 목록
			List<String> currColmList=new ArrayList<String>();
			for(WfFormColmLVo storedWfFormColmLVo : currList){
				if(ArrayUtil.isStartsWithArray(WfConstant.EXCLUDE_SELECT, storedWfFormColmLVo.getColmNm())) continue;
				currColmList.add(storedWfFormColmLVo.getColmNm());
			}
			
			// 배포될 테이블 컬럼명 목록
			List<String> genColmList=new ArrayList<String>();
			for(WfFormColmLVo storedWfFormColmLVo : colmVoList){
				genColmList.add(storedWfFormColmLVo.getColmNm());
			}
			
			//System.out.println("currColmList.containsAll(genColmList) : "+currColmList.containsAll(genColmList));
			//System.out.println("genColmList.containsAll(currColmList) : "+genColmList.containsAll(currColmList));
			// 컬럼 목록 다른지 여부[컬럼이 추가되면]
			//boolean isDiff=!(currColmList.containsAll(genColmList) && genColmList.containsAll(currColmList));
			boolean isDiff=!currColmList.containsAll(genColmList);
			
			// 검색 시작
	        //long start = System.currentTimeMillis();
	        
			// 컬럼목록이 다를 경우 json 컬럼에서 데이터를 변환해서 처리
			if(isDiff){
				QueryQueue queryQueue = new QueryQueue();
				
				// 컬럼목록 별로 저장될 데이터 수집
				List<String> attributeList = new ArrayList<String>();
				
				for(String colmNm : genColmList){
					if(ArrayUtil.isStartsWithArray(WfConstant.CODE_COLM_LIST, colmNm)){
						continue;
					}
					attributeList.add(colmNm);
				}
				
				// 복사할 데이터(JSON) 목록 조회
				WfxTranVo wfxTranVo = new WfxTranVo(formNo);
				wfxTranVo.setGenId(genId);
				wfxTranVo.setInstanceQueryId("com.innobiz.orange.web.wf.dao.WfxTranDao.extractCopyData");
				wfxTranVo.setPageRowCnt(MAX_TRANSACTION_SIZE);
				
				String jsonString=null;
				// JSON 객체로 변환
				JSONObject jsonVa = null;
				
				// 업무상세
				WfWorksLVo wfWorksDVo = null;
				
				Map<String, Object> voMap = null;
				
				// 컬럼 목록 조회
				List<String[]> colmList=wfFormSvc.getColmNmList(colmVoList, null, null);
				
				List<WfWorksLVo> wfWorksLVoList=null;
				for(int i=0; i<pageCnt; i++){
					wfxTranVo.setPageNo(Integer.valueOf(i));
					wfWorksLVoList=(List<WfWorksLVo>) commonSvc.queryList(wfxTranVo);
					// 검색 시작
			        //long insertStart = System.currentTimeMillis();
					if(wfWorksLVoList!=null){
						queryQueue = new QueryQueue();
						
						for(WfWorksLVo storedWfWorksLVo : wfWorksLVoList){
							jsonString=storedWfWorksLVo.getJsonVa();
							//if(jsonString==null || jsonString.isEmpty()) continue;
							if(jsonString==null) 	jsonVa=null;
							else jsonVa=(JSONObject) JSONValue.parse(jsonString);
							wfWorksDVo = new WfWorksLVo(formNo);
							BeanUtils.copyProperties(storedWfWorksLVo, wfWorksDVo, new String[]{"formNo", "formId", "jsonVa"});
							wfWorksDVo.setDetail(true);
							
							// 컬럼 목록 세팅
							wfWorksDVo.setColmList(colmList);
							
							voMap = new HashMap<String, Object>();
							if(attributeList.size()>0){
								FormUtil.bindJsonMap(jsonVa, voMap, attributeList, null);
								wfWorksDVo.setVoMap(voMap);
							}
							//System.out.println("voMap : "+voMap);
							
							// set header
							/*Entry<String, Object> entry;
							if(voMap!=null){
								Iterator<Entry<String, Object>> iterator = voMap.entrySet().iterator();
								while(iterator.hasNext()){
									entry = iterator.next();
									System.out.println("entry.getKey() : "+entry.getKey()+"\tcolmNm[1] : "+entry.getValue());
								}
							}*/
							
							queryQueue.insert(wfWorksDVo);
						}
						// 검색 종료
				       // long inserDataEnd = System.currentTimeMillis();
				        //System.out.println("copy data put time : "+((inserDataEnd-insertStart)/1000.0f));
				        if(!queryQueue.isEmpty())
				        	commonDao.execute(queryQueue);
						// 검색 종료
				       // long insertEnd = System.currentTimeMillis();
				       // System.out.println("copy data time("+MAX_TRANSACTION_SIZE+") : "+((insertEnd-insertStart)/1000.0f));
						//queryQueue = new QueryQueue();
						// 진행사항 로그
						writeInProcLog(genId, formNo, procSeq, Integer.toString(i+1));
					}
				}
		        
			}else{ // 같을 경우 insert select 로 데이터 복사
				// 컬럼명 배열로 변환
				List<String> colmNmList=new ArrayList<String>();
				for(WfFormColmLVo storedWfFormColmLVo : currList){
					colmNmList.add(storedWfFormColmLVo.getColmNm());
				}
				
				// select 구문에 들어간 컬럼 목록
				List<WfFormColmLVo> mergeColmVoList=new ArrayList<WfFormColmLVo>();
				String[] arr=ArrayUtil.toArray(colmNmList);
				
				WfFormColmLVo wfFormColmLVo=null;
				// 변경할 컬럼목록에서 현재 컬럼이 없을경우 null 처리
				for(WfFormColmLVo storedWfFormColmLVo : colmVoList){
					wfFormColmLVo=new WfFormColmLVo();
					if(!ArrayUtil.isInArray(arr, storedWfFormColmLVo.getColmNm())) wfFormColmLVo.setColmDbNm("NULL");
					else wfFormColmLVo.setColmDbNm(storedWfFormColmLVo.getColmDbNm());
					mergeColmVoList.add(wfFormColmLVo);
				}
				
				WfxTranVo wfxTranVo = new WfxTranVo(formNo);
				wfxTranVo.setGenId(genId);
				wfxTranVo.setInstanceQueryId("com.innobiz.orange.web.wf.dao.WfxTranDao.copyTransferingData");
				wfxTranVo.setColmVoList(colmVoList);
				wfxTranVo.setMergeColmVoList(mergeColmVoList);
				wfxTranVo.setPageRowCnt(MAX_TRANSACTION_SIZE);
				
				// 1000 건 단위로 나누어 실행함 - 롤백 세크먼트 고려 - 페이징 0부터 시작함
				for(int i=0; i<pageCnt; i++){
					wfxTranVo.setPageNo(Integer.valueOf(i));
					commonSvc.insert(wfxTranVo);
					
					// 진행사항 로그
					writeInProcLog(genId, formNo, procSeq, Integer.toString(i+1));
				}
			}
			// 검색 종료
	       // long end = System.currentTimeMillis();
	        //System.out.println("total copy data time : "+((end-start)/1000.0f));
		}
        
		// 종료 로그
		writeEndLog(genId, formNo, procSeq);
					
	}
	
	/** 1.4 인덱스 생성 */
	private void createIndexes(WfFormGenLVo wfFormGenLVo, String formNo, Integer procSeq, Integer strtSeq, Integer totalSeq, boolean noLog, boolean first) throws SQLException{
		
		String[][] indexes = null;
		
		if(isMssql){
			// { query-id, 해당 id 내의 sql 갯수 }
			indexes = new String[][]{
					{"WfWorksL", "1"},
					{"WfWorksD", "1"}
			};
		} else if(isOracle){
			// { query-id, 해당 id 내의 sql 갯수 }
			indexes = new String[][]{
					{"WfWorksL", "2"},
					{"WfWorksD", "2"}
			};
		} else if(isMysql){
			// { query-id, 해당 id 내의 sql 갯수 }
			indexes = new String[][]{
					{"WfWorksL", "1"},
					{"WfWorksD", "1"}
			};	
		} else {
			return;
		}
		String genId = wfFormGenLVo.getGenId();
		if(!noLog){
			// 생성진행내역(WF_FORM_GEN_PROC_L) - 시작 로그
			String genCont = isKo ? "["+procSeq+"/"+totalSeq+"] 인덱스 생성" : "["+procSeq+"/"+totalSeq+"] Create indexes";
			if(procSeq.intValue() == strtSeq.intValue()) genCont += " [Retry]";
			writeStartLog(genId, formNo, procSeq, first ? "1" : Integer.toString(indexes.length), genCont);
		}
		
		// 인덱스 생성용
		String sqlIdPrefix = "com.innobiz.orange.web.wf.dao.WfxTableDao.index";
		
		int i, size, idx=0, indexSeq = 0;
		for(String[] index : indexes){
			if(first && idx==0) {
				idx++;
				continue;
			}
			wfFormGenLVo.setInstanceQueryId(sqlIdPrefix+index[0]);
			size = Integer.parseInt(index[1]);
			// 인덱스 생성
			for(i=1;i<=size;i++){
				wfFormGenLVo.setSqlSeq(i);
				commonSvc.update(wfFormGenLVo);
			}
			idx++;
			if(!noLog){
				// 진행사항 로그 남기기
				writeInProcLog(genId, formNo, procSeq, Integer.toString(++indexSeq));
			}
		}
		
		if(!noLog){
			// 종료 로그
			writeEndLog(genId, formNo, procSeq);
		}
	}
	
	
	/** 테이블 제거 */
	@Async
	public void dropTables(List<String> formNoList) throws SQLException {
		
		QueryQueue queryQueue = new QueryQueue();
		String[] tables = new String[]{"WfWorksL", "WfWorksCodeL", "WfWorksFileD", "WfWorksImgD", "WfWorksReadH", "WfWorksD"};
		
		// 테이블 제거용
		String sqlIdPrefix = "com.innobiz.orange.web.wf.dao.WfxTableDao.drop";
				
		WfFormGenLVo wfFormGenLVo = null;
		String formId=null;
		for(String formNo : formNoList){
			// 테이블ID 생성
			formId=wfAdmSvc.getTblId(formNo);
			for(String tblNm : tables){
				wfFormGenLVo = new WfFormGenLVo();
				wfFormGenLVo.setInstanceQueryId(sqlIdPrefix+tblNm);
				wfFormGenLVo.setFormId(formId);
				queryQueue.update(wfFormGenLVo);
				
				// 시퀀스 삭제
				if("WfWorksL".equals(tblNm)){
					wfFormGenLVo = new WfFormGenLVo();
					wfFormGenLVo.setInstanceQueryId("com.innobiz.orange.web.wf.dao.WfxTableDao.deleteCmSeq");
					wfFormGenLVo.setFormId(formId);
					queryQueue.update(wfFormGenLVo);
				}
			}
		}
		if(!queryQueue.isEmpty()) commonDao.execute(queryQueue);
	}
	
	/** 생성 시작 처리 */
	private void startGenerate(String genId) throws SQLException{
		WfFormGenBVo updateWfFormGenBVo = new WfFormGenBVo();
		updateWfFormGenBVo.setGenId(genId);
		updateWfFormGenBVo.setStrtDt("sysdate");
		//생성진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
		updateWfFormGenBVo.setProcStatCd("processing");
		commonDao.update(updateWfFormGenBVo);
	}
	
	/** 생성 종료 처리 */
	private void endGenerate(String genId) throws SQLException{
		WfFormGenBVo updateWfFormGenBVo = new WfFormGenBVo();
		updateWfFormGenBVo.setGenId(genId);
		updateWfFormGenBVo.setEndDt("sysdate");
		//생성진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
		updateWfFormGenBVo.setProcStatCd("completed");
		commonDao.update(updateWfFormGenBVo);
	}
	
	/** 생성 종료 처리 */
	private void endGenerate(String genId, String formNo, Integer procSeq) throws SQLException{
		// 생성목록(WF_FORM_GEN_L) 테이블 - INSERT
		WfFormGenLVo wfFormGenLVo = new WfFormGenLVo();
		wfFormGenLVo.setGenId(genId);
		wfFormGenLVo.setFormNo(formNo);
		wfFormGenLVo.setProcStatCd("complete");
		commonSvc.update(wfFormGenLVo);
		
		// 생성 완료 로그
		WfFormGenProcLVo wfFormGenProcLVo = new WfFormGenProcLVo();
		wfFormGenProcLVo.setGenId(genId);
		wfFormGenProcLVo.setFormNo(formNo);
		wfFormGenProcLVo.setSeq(procSeq.toString());
		wfFormGenProcLVo.setProcCont(isKo ? "생성 완료" : "Generateing completed");
		wfFormGenProcLVo.setStrtDt("sysdate");
		wfFormGenProcLVo.setEndDt("sysdate");
		commonSvc.insert(wfFormGenProcLVo);
	}
	
	/** 양식 삭제 */
	public void deleteGenList(QueryQueue queryQueue, UserVo userVo, String genId) throws SQLException{
		
		// 삭제여부[현재 양식에서 사용하고 있으면 삭제불가]
		WfFormBVo wfFormBVo = new WfFormBVo();
		wfFormBVo.setGenId(genId);
		if(commonDao.count(wfFormBVo)>0) return;
		
		// 양식생성진행내역(WF_FORM_GEN_PROC_L) 삭제
		WfFormGenProcLVo wfFormGenProcLVo = new WfFormGenProcLVo();
		wfFormGenProcLVo.setGenId(genId);
		queryQueue.delete(wfFormGenProcLVo);		
					
		// 양식생성목록(WF_FORM_GEN_L) 삭제
		WfFormGenLVo wfFormGenLVo = new WfFormGenLVo();
		wfFormGenLVo.setGenId(genId);
		queryQueue.delete(wfFormGenLVo);		
				
		// 양식생성기본(WF_FORM_GEN_B) 삭제
		WfFormGenBVo wfFormGenBVo = new WfFormGenBVo();
		wfFormGenBVo.setCompId(userVo.getCompId());
		wfFormGenBVo.setGenId(genId);
		queryQueue.delete(wfFormGenBVo);
		
	}
	
	/** int 변환 - null 처리 */
	/*private int parseInt(String no){
		return no==null || no.isEmpty() ? 0 : Integer.parseInt(no);
	}*/
	
	/** 생성 실행용 쓰레드 */
	private class GenerateThread extends Thread {
		
		@Override
		public void run() {
			
			// 생성ID
			String genId;
			// 시스템 차단 여부
			boolean isInHalt = false;
			
			while(true){
				
				// 시스템 차단 여부 - 조회
				isInHalt = false;
				/*try {
					//isInHalt = ptSysSvc.isSysInHalt();
					isInHalt = false;
				} catch (SQLException e) {
					isInHalt = true;
				}*/
				
				// 시스템이 차단되어 있으면 - 2분 후 다시 체크
				if(isInHalt){
					try {
						Thread.sleep(1000 * 120);
					} catch (InterruptedException ignore) {
					}
				} else {
					
					// 생성ID - 조회 : 생성 대상이 있는지 체크
					synchronized (genIdQueue) {
						if(genIdQueue.isEmpty()) genId = null;
						else genId = genIdQueue.removeFirst();
					}
					
					// 생성대상이 없으면 - wait 모드
					if(genId == null){
						synchronized(this){
							try {
								this.wait();
							} catch (InterruptedException e) {
								generateThread = null;
								break;
							}
						}
					} else {
						// 생성 실행
						try {
							processGenerate(genId);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
				}
				
			}//while(true){
		}
	}
	
	/** 시작 로그 */
	private void writeStartLog(String genId, String formNo, Integer procSeq, String allCnt, String genCont) throws SQLException{
		WfFormGenProcLVo wfFormGenProcLVo = new WfFormGenProcLVo();
		wfFormGenProcLVo.setGenId(genId);
		wfFormGenProcLVo.setFormNo(formNo);
		wfFormGenProcLVo.setSeq(procSeq.toString());
		wfFormGenProcLVo.setProcCont(genCont);
		wfFormGenProcLVo.setAllCnt(allCnt);
		wfFormGenProcLVo.setProcCnt("0");
		wfFormGenProcLVo.setStrtDt("sysdate");
		commonSvc.insert(wfFormGenProcLVo);
	}
	
	/** 진행 로그 남기기 */
	private void writeInProcLog(String genId, String formNo, Integer procSeq, String procCnt) throws SQLException{
		WfFormGenProcLVo wfFormGenProcLVo = new WfFormGenProcLVo();
		wfFormGenProcLVo.setGenId(genId);
		wfFormGenProcLVo.setFormNo(formNo);
		wfFormGenProcLVo.setSeq(procSeq.toString());
		wfFormGenProcLVo.setProcCnt(procCnt);
		commonSvc.update(wfFormGenProcLVo);
	}
	
	/** 종료 로그 남기기 */
	private void writeEndLog(String genId, String formNo, Integer procSeq) throws SQLException{
		WfFormGenProcLVo wfFormGenProcLVo = new WfFormGenProcLVo();
		wfFormGenProcLVo.setGenId(genId);
		wfFormGenProcLVo.setFormNo(formNo);
		wfFormGenProcLVo.setSeq(procSeq.toString());
		wfFormGenProcLVo.setEndDt("sysdate");
		commonSvc.update(wfFormGenProcLVo);
	}
	
	/** 생략 로그 남기기 */
	private void writeSkipLog(String genId, String formNo, Integer procSeq, int majorSeq) throws SQLException{
		WfFormGenProcLVo wfFormGenProcLVo = new WfFormGenProcLVo();
		wfFormGenProcLVo.setGenId(genId);
		wfFormGenProcLVo.setFormNo(formNo);
		wfFormGenProcLVo.setSeq(procSeq.toString());
		wfFormGenProcLVo.setProcCont(isKo ? "["+majorSeq+"/8] 생략" : "["+majorSeq+"/8] Skip");
		wfFormGenProcLVo.setStrtDt("sysdate");
		wfFormGenProcLVo.setEndDt("sysdate");
		commonSvc.insert(wfFormGenProcLVo);
	}
	
	/** 오류 로그 남기기 */
	private void writeErrorLog(String genId, String formNo, Integer procSeq, Exception exception) throws SQLException{
		
		WfFormGenBVo errWfFormGenBVo = new WfFormGenBVo();
		errWfFormGenBVo.setGenId(genId);
		errWfFormGenBVo.setEndDt("sysdate");
		// 생성진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
		errWfFormGenBVo.setProcStatCd("error");
		commonSvc.update(errWfFormGenBVo);
		
		// Exception 위치 잡기
		String where = null;
		String message = exception.getMessage()==null ? exception.getClass().getCanonicalName() : exception.getMessage();
		for(StackTraceElement trace : exception.getStackTrace()){
			if(trace.getClassName().equals("com.innobiz.orange.web.wf.svc.WfGenSvc")){
				where = trace.toString();
				break;
			}
		}
		
		// 오류 로그 저장
		WfFormGenProcLVo wfFormGenProcLVo = new WfFormGenProcLVo();
		wfFormGenProcLVo.setGenId(genId);
		wfFormGenProcLVo.setFormNo(formNo);
		wfFormGenProcLVo.setSeq(procSeq.toString());
		wfFormGenProcLVo.setEndDt("sysdate");
		wfFormGenProcLVo.setErrCont(where != null ? (where+": "+message) : message);
		wfFormGenProcLVo.setErrYn("Y");
		commonSvc.update(wfFormGenProcLVo);
	}
	
	/** 분할해야 할 컬럼이 있으면 분할갯수 리턴*/
	public Integer getDivisionColmList(String colm){
		Integer divisionCnt=null;
		for(String[] division : WfConstant.DIVISION_COLM_LIST){
			if(colm.startsWith(division[0])){
				divisionCnt=Integer.parseInt(division[1]);
				break;
			}
		}
		return divisionCnt;
	}
	
	/** 컬럼 목록 세팅 */
	public List<WfFormColmLVo> getDbColmList(List<WfFormColmLVo> wfFormColmLVoList, String[] exclude){
		if(wfFormColmLVoList==null) return null;
		List<WfFormColmLVo> returnList = new ArrayList<WfFormColmLVo>();
		String colmDbNm;
		int i;
		Integer division=null;
		
		WfFormColmLVo newWfFormColmLVo;
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			colmDbNm=StringUtil.toCamelNotation(storedWfFormColmLVo.getColmNm(), false);
			if(exclude!=null && ArrayUtil.isStartsWithArray(exclude, colmDbNm)) continue;
			division=getDivisionColmList(colmDbNm);
			
			if(division!=null){
				for(i=1;i<=division.intValue();i++){
					newWfFormColmLVo=new WfFormColmLVo();
					BeanUtils.copyProperties(storedWfFormColmLVo, newWfFormColmLVo, new String[]{"colmDbNm"});
					// db별 데이터 타입 변환
					toDbmsDataType(newWfFormColmLVo);
					newWfFormColmLVo.setColmDbNm(colmDbNm.toUpperCase()+"_"+i); // 컬럼명은 대문자로 변환
					returnList.add(newWfFormColmLVo);
				}
			}else{
				// db별 데이터 타입 변환
				toDbmsDataType(storedWfFormColmLVo);
				storedWfFormColmLVo.setColmDbNm(colmDbNm.toUpperCase()); // 컬럼명은 대문자로 변환
				returnList.add(storedWfFormColmLVo);
			}
			
		}
		return returnList;
	}
	
	/** DBMS 별 컬럼타입, 길이 변환 - 기준 MS-SQL */
	private void toDbmsDataType(WfFormColmLVo wfFormColmLVo){
		String colmTypCd = wfFormColmLVo.getColmTypCd();
		
		if(colmTypCd.startsWith("number")){
			if(isMssql){
				wfFormColmLVo.setDataTyp("VARCHAR");
			}else if(isOracle){
				wfFormColmLVo.setDataTyp("VARCHAR2");
			}else if(isMysql){
				wfFormColmLVo.setDataTyp("VARCHAR");
			}
			wfFormColmLVo.setDataLen(120);
		}else if(colmTypCd.startsWith("text")){
			if(isMssql){
				wfFormColmLVo.setDataTyp("NVARCHAR");
			}else if(isOracle){
				wfFormColmLVo.setDataTyp("NVARCHAR2");
			}else if(isMysql){
				wfFormColmLVo.setDataTyp("VARCHAR");
			}
			wfFormColmLVo.setDataLen(2000);
		}else if(colmTypCd.startsWith("textarea")){
			if(isMssql){
				wfFormColmLVo.setDataTyp("NVARCHAR");
			}else if(isOracle){
				wfFormColmLVo.setDataTyp("NVARCHAR2");
			}else if(isMysql){
				wfFormColmLVo.setDataTyp("VARCHAR");
			}
			wfFormColmLVo.setDataLen(4000);
		}else if(colmTypCd.startsWith("editor")){
			if(isMssql){
				wfFormColmLVo.setDataTyp("NTEXT");
			}else if(isOracle){
				wfFormColmLVo.setDataTyp("NCLOB");
			}else if(isMysql){
				wfFormColmLVo.setDataTyp("LONGTEXT");
			}
		}else if(colmTypCd.startsWith("date") || colmTypCd.startsWith("period") || colmTypCd.startsWith("time") || colmTypCd.startsWith("calculate")){
			if(isMssql){
				wfFormColmLVo.setDataTyp("VARCHAR");
			}else if(isOracle){
				wfFormColmLVo.setDataTyp("VARCHAR2");
			}else if(isMysql){
				wfFormColmLVo.setDataTyp("VARCHAR");
			}
			wfFormColmLVo.setDataLen(120);
		}else if(colmTypCd.startsWith("radio") || colmTypCd.startsWith("select") || colmTypCd.startsWith("checkbox")){
			if(isMssql){
				wfFormColmLVo.setDataTyp("VARCHAR");
			}else if(isOracle){
				wfFormColmLVo.setDataTyp("VARCHAR2");
			}else if(isMysql){
				wfFormColmLVo.setDataTyp("VARCHAR");
			}
			wfFormColmLVo.setDataLen(120);
		}else{
			if(isMssql){
				wfFormColmLVo.setDataTyp("VARCHAR");
			}else if(isOracle){
				wfFormColmLVo.setDataTyp("VARCHAR2");
			}else if(isMysql){
				wfFormColmLVo.setDataTyp("VARCHAR");
			}
			wfFormColmLVo.setDataLen(2000);
		}
		
	}
	
}
