package com.innobiz.orange.web.em.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.vo.EmMailAdrTranBVo;
import com.innobiz.orange.web.em.vo.EmMailAdrTranTgtTVo;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.sync.SimpleIterator;
import com.innobiz.orange.web.or.sync.SimpleQuery;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wb.svc.WbBcSvc;
import com.innobiz.orange.web.wb.svc.WbCmSvc;
import com.innobiz.orange.web.wb.vo.WbBcBVo;
import com.innobiz.orange.web.wb.vo.WbBcCntcDVo;
import com.innobiz.orange.web.wb.vo.WbBcFldBVo;

/** 메일 주소록 이관 서비스 */
@Service
public class EmMailAdrTranSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmMailAdrTranSvc.class);
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 조직 서비스*/
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbCmSvc wbCmSvc;
	
//	/** 외부 DB 서비스*/
//	@Autowired
//	private CuFncSvc cuFncSvc;
	
	/** 명함 공통 서비스 */
	@Autowired
	private WbBcSvc wbBcSvc;
	
	/** contextProperties */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
	private LinkedList<String> tranIdQueue = new LinkedList<String>();
	
	private TransferThread transferThread = null;
	
	boolean isMssql = false;
	boolean isOracle = false;
	
	/** 이관항목 더하기 */
	public void addTranId(String tranId){
		synchronized (tranIdQueue) {
			tranIdQueue.add(tranId);
		}
		// 초기화
		if(transferThread == null){
			isMssql = "mssql".equals(contextProperties.getProperty("dbms"));
			isOracle = "oracle".equals(contextProperties.getProperty("dbms"));
			
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
		EmMailAdrTranBVo emMailAdrTranBVo = new EmMailAdrTranBVo();
		emMailAdrTranBVo.setTranId(tranId);
		emMailAdrTranBVo = (EmMailAdrTranBVo)commonSvc.queryVo(emMailAdrTranBVo);
		
		if(emMailAdrTranBVo == null){
			LOGGER.error("NO transfer data(EM_MAIL_ADR_TRAN_B) for tranId:"+tranId);
			return;
		}
		
		//이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러 
		if("processing".equals(emMailAdrTranBVo.getTranProcStatCd())){
			LOGGER.error("Transfering is already started ! - tranId:"+tranId);
			return;
		} else if("completed".equals(emMailAdrTranBVo.getTranProcStatCd())){
			LOGGER.error("Transfering is already completed ! - tranId:"+tranId);
			return;
		}
		
		try {
			// 시스템 차단
			//ptSysSvc.setSysHalt(emMailAdrTranBVo.getUserUid(), "wb", "cm.msg.halt.dmTran");
			
			// 이관 시작 처리
			startTransfer(emMailAdrTranBVo);
			
			// 1. 이관대상 저장
			extractTransferingData(emMailAdrTranBVo);
			
			// 2. 메일 주소록 이관
			copyTransferingData(emMailAdrTranBVo);
			
			// 이관 종료 처리
			endTransfer(emMailAdrTranBVo);
						
		}catch(Exception exception){
			writeErrorLog(emMailAdrTranBVo, exception);
			
			exception.printStackTrace();
		} finally {
			// 시스템 차단 해제
			//ptSysSvc.clearSysHalt();
		}
	}
	
	/** 이관 대상 저장 - 메일 사용자 */
	private void extractTransferingData(EmMailAdrTranBVo emMailAdrTranBVo) throws CmException, SQLException, IOException{
		
		// 이관대상 저장여부 조회
		EmMailAdrTranTgtTVo emMailAdrTranTgtTVo = new EmMailAdrTranTgtTVo();
		emMailAdrTranTgtTVo.setTranId(emMailAdrTranBVo.getTranId());
		if(commonSvc.count(emMailAdrTranTgtTVo)>0){
			LOGGER.info("[SKIP] extractTransferingData : Aleady Saved.");
			return;
		}
		Map<String,Object> params=new HashMap<String,Object>();
		//params.put("userUid", "U000000E");
		// 메일 주소록 그룹 목록
		List<Map<String,String>> mailUserList = getMailAdrList("mailUser", params);
		if(mailUserList==null){
			LOGGER.error("[ERROR] - Mail User List Size : 0");
			return;
		}
		LOGGER.info("[Start] extractTransferingData : Mail User ("+mailUserList.size()+") Count.");
		
		Map<String, Object> userMap=null;
		
		QueryQueue queryQueue = new QueryQueue();
		
		int seq=0;
		for(Map<String,String> map : mailUserList){
			if(!map.containsKey("userUid") || map.get("userUid")==null || "".equals(map.get("userUid")) || "MAILADMIN".equals(map.get("userUid"))) continue;
			userMap=orCmSvc.getUserMap(map.get("userUid"), "ko");
			if(userMap==null) continue;
			emMailAdrTranTgtTVo = new EmMailAdrTranTgtTVo();
			emMailAdrTranTgtTVo.setTranId(emMailAdrTranBVo.getTranId());
			emMailAdrTranTgtTVo.setSeq(++seq);
			emMailAdrTranTgtTVo.setUserUid(map.get("userUid"));
			emMailAdrTranTgtTVo.setUserSn(map.get("userSn"));
			queryQueue.insert(emMailAdrTranTgtTVo);
		}
		if(!queryQueue.isEmpty())
			commonSvc.execute(queryQueue);
		LOGGER.info("[End] extractTransferingData : Mail User Save Completed.");
	}
	
	/** 명함으로 이관 - 저장 */
	public void copyTransferingData(EmMailAdrTranBVo emMailAdrTranBVo) throws CmException, SQLException, IOException{
		// 이관대상 목록 조회
		EmMailAdrTranTgtTVo emMailAdrTranTgtTVo = new EmMailAdrTranTgtTVo();
		emMailAdrTranTgtTVo.setTranId(emMailAdrTranBVo.getTranId());
		if(commonSvc.count(emMailAdrTranTgtTVo)==0){
			LOGGER.error("[ERROR] copyTransferingData : TransferingData empty.");
			return;
		}
		QueryQueue queryQueue = null;
		@SuppressWarnings("unchecked")
		List<EmMailAdrTranTgtTVo> tgtVoList=(List<EmMailAdrTranTgtTVo>)commonSvc.queryList(emMailAdrTranTgtTVo);
		// 신규폴더ID 맵
		Map<String,String> fldIdMap = null;
		Map<String,Object> params=new HashMap<String,Object>();
		Map<String, Object> userMap=null;
		Integer fldCnt=0, adrCnt=0;
		Integer personCnt=null;
		for(EmMailAdrTranTgtTVo tgtVo : tgtVoList){
			if(tgtVo.getErrYn()!=null && "N".equals(tgtVo.getErrYn())) continue;
			userMap=orCmSvc.getUserMap(tgtVo.getUserUid(), "ko");
			if(userMap==null) continue;
			/*try {
				Thread.sleep(500);
			} catch (InterruptedException ignore) {
			}*/
			//System.out.println("사용자명 : "+userMap.get("rescNm"));
			/*if("김하나".equals(userMap.get("rescNm"))){
				throw new CmException("김하나");
			}*/
			fldIdMap = new HashMap<String,String>();
			queryQueue = new QueryQueue();
			params.put("userSn", tgtVo.getUserSn());
			try{
				fldCnt=saveMailAdrFldList(queryQueue, fldIdMap, (String)userMap.get("compId"), tgtVo.getUserUid(), emMailAdrTranBVo.getTranId(), params); // 폴더저장
				if(fldCnt!=null && fldCnt>0){
					personCnt=getMailAdrCount("personCnt", params);
					if(fldCnt<=2 && (personCnt==null || personCnt==0)) continue;
					adrCnt=saveMailAdrToBc(queryQueue, fldIdMap, (String)userMap.get("compId"), tgtVo.getUserUid(), emMailAdrTranBVo.getTranId(), params); // 명함저장
					commonSvc.execute(queryQueue);
				}else{
					adrCnt=0;
				}
				writeLogTgt(emMailAdrTranBVo, tgtVo.getSeq(), "N", fldCnt, adrCnt);
			}catch(Exception e){
				writeLogTgt(emMailAdrTranBVo, tgtVo.getSeq(), "Y", null, null);
			}
		}
	}
	
	/** 폴더 저장 */
	public Integer saveMailAdrFldList(QueryQueue queryQueue, Map<String,String> fldIdMap, String compId, String userUid, String tranId, Map<String,Object> params) throws CmException, SQLException, IOException{
		
		// 메일 주소록 그룹 목록
		List<Map<String,String>> mailGroupList = getMailAdrList("group", params);
		if(mailGroupList==null){
			LOGGER.error("[ERROR] - Mail Group List Size : 0");
			return null;
		}
		LOGGER.debug("[Start] copyTransferingData : Mail Group ("+mailGroupList.size()+") Count.");
		// 폴더 기본 테이블
		WbBcFldBVo wbBcFldBVo = null;
		String groupSn, fldId, upperGroupSn;
		
		List<WbBcFldBVo> fldList = new ArrayList<WbBcFldBVo>();
		Integer tranCnt=0;
		for(Map<String,String> map : mailGroupList){
			if(!map.containsKey("userUid") || map.get("userUid")==null || "".equals(map.get("userUid"))) continue;
			++tranCnt;
			groupSn=map.get("groupSn");
			upperGroupSn=map.get("upperGroupSn");
			fldId=wbCmSvc.createId("WB_BC_FLD_B");
			fldIdMap.put(groupSn, fldId); // 폴더ID맵
			wbBcFldBVo = new WbBcFldBVo();
			wbBcFldBVo.setRegrUid(userUid);
			wbBcFldBVo.setBcFldId(fldId);
			wbBcFldBVo.setFldNm("0".equals(upperGroupSn) ? tranId+"메일주소록" : map.get("groupName"));
			wbBcFldBVo.setRegDt("sysdate");
			wbBcFldBVo.setCompId(compId);
			wbBcFldBVo.setSortOrdr("1");
			wbBcFldBVo.setAbvFldId("0".equals(upperGroupSn) ? "ROOT" : fldIdMap.get(map.get("upperGroupSn")));
			fldList.add(wbBcFldBVo);
			queryQueue.insert(wbBcFldBVo); // 실서버 적용할때 주석 풀기
		}
		LOGGER.debug("[End] copyTransferingData : Mail Group Save Completed.");
		return tranCnt;
	}
	
	/** 명함 저장 */
	public Integer saveMailAdrToBc(QueryQueue queryQueue, Map<String,String> fldIdMap, String compId, String userUid, String tranId, Map<String,Object> params) throws CmException, SQLException, IOException{
		// 메일 주소록 목록
		List<Map<String,String>> mailPersonList = getMailAdrList("person", params);
		if(mailPersonList==null){
			LOGGER.error("[SKIP] - Mail Person List Size : 0");
			return null;
		}
		LOGGER.debug("[Start] copyTransferingData : Mail Person ("+mailPersonList.size()+") Count.");
		// 폴더 기본 테이블
		WbBcBVo wbBcBVo = null;
		String bcId;
		for(Map<String,String> map : mailPersonList){
			wbBcBVo=new WbBcBVo();
			bcId=wbCmSvc.createId("WB_BC_B");
			wbBcBVo.setBcId(bcId);
			wbBcBVo.setCompId(compId);
			wbBcBVo.setPublTypCd("priv"); // 비공개
			wbBcBVo.setFldId(fldIdMap.get(map.get("groupSn"))); // 폴더ID
			wbBcBVo.setDftCntcTypCd("compPhon");
			// 등록구분 신규 처리
			wbBcBVo.setBcRegTypCd("O");
			wbBcBVo.setMainSetupYn("O");
			wbBcBVo.setBumkYn("N");//즐겨찾기여부
			wbBcBVo.setRegrUid(userUid);
			wbBcBVo.setRegDt("sysdate");
			setMailToBcVo(wbBcBVo, map); // 명함기본
			queryQueue.insert(wbBcBVo); // 실서버 적용할때 주석 풀기
			setMailAdrToCntc(queryQueue, wbBcBVo, map); // 연락처
		}
		LOGGER.debug("[End] copyTransferingData : Mail Person Save Completed.");
		return mailPersonList.size();
				
	}
	
	/** 명함 VO 생성 */
	public void setMailToBcVo(WbBcBVo wbBcBVo, Map<String,String> map){
		wbBcBVo.setBcNm(map.get("personName"));
		wbBcBVo.setInitialNm(wbBcSvc.direct(wbBcBVo.getBcNm().charAt(0)));
		wbBcBVo.setBcEnNm(map.get("engName"));
		wbBcBVo.setCompNm(map.get("organName"));
		wbBcBVo.setDeptNm(map.get("deptName"));
		wbBcBVo.setGradeNm(map.get("roleName"));
		wbBcBVo.setTichCont(map.get("works"));
		wbBcBVo.setFno(map.get("officeFax"));		
		wbBcBVo.setCompZipNo(map.get("officeZipcode"));
		if(map.get("officeAddress1")!=null && map.get("officeAddress2")!=null)
			wbBcBVo.setCompAdr(map.get("officeAddress1")+" "+map.get("officeAddress2"));
		else
			wbBcBVo.setCompAdr(map.get("officeAddress1"));
		
		wbBcBVo.setHomeZipNo(map.get("homeZipcode"));
		if(map.get("homeAddress1")!=null && map.get("homeAddress2")!=null)
			wbBcBVo.setHomeAdr(map.get("homeAddress1")+" "+map.get("homeAddress2"));
		else
			wbBcBVo.setHomeAdr(map.get("homeAddress1"));
		
		if(map.get("genderCode")==null)
			wbBcBVo.setGenCd("M");
		else
			wbBcBVo.setGenCd("0".equals(map.get("genderCode")) ? "F" : "M");
		wbBcBVo.setNatyCd("KR");
		
		if(map.get("birthday")!=null && map.get("birthday").length()==8){
			wbBcBVo.setBirth(map.get("birthday"));
			wbBcBVo.setBirthSclcCd("0".equals(map.get("birthdayType")) ? "LUNA" : "SOLA");
		}
		
		if(map.get("anniversary")!=null && map.get("anniversary").length()==8){
			wbBcBVo.setWeddAnnv(map.get("anniversary"));
			wbBcBVo.setWeddAnnvSclcCd("SOLA");
		}
		wbBcBVo.setPsnHpageUrl(map.get("homepage"));
		wbBcBVo.setNoteCont(map.get("comments"));
		
	}
	
	/** 연락처 저장 */
	public void setMailAdrToCntc(QueryQueue queryQueue, WbBcBVo wbBcBVo, Map<String,String> map){
		// 연락처
		List<WbBcCntcDVo> wbBcCntcDVoList = new ArrayList<WbBcCntcDVo>();
		
		WbBcCntcDVo cntcVo=null;
		for(String key : new String[]{"email", "mobile", "homePhone","officePhone","officeLocalPhone","officeDPhone"}){
			if(!map.containsKey(key) || map.get(key)==null || "".equals(map.get(key))) continue;
			cntcVo = new WbBcCntcDVo();
			if(key.startsWith("office"))
				cntcVo.setCntcTypCd("compPhon");
			else if(key.startsWith("mobile"))
				cntcVo.setCntcTypCd("mbno");
			else if(key.startsWith("home"))
				cntcVo.setCntcTypCd("homePhon");
			else
				cntcVo.setCntcTypCd(key);
			cntcVo.setCntcClsCd("email".equals(key) ? "EMAIL" : "CNTC");
			cntcVo.setCntcCont(map.get(key));
			wbBcCntcDVoList.add(cntcVo);
		}
		if(wbBcCntcDVoList.size()>0){
			Map<String,Integer> seqMap = new HashMap<String,Integer>();
			for(WbBcCntcDVo wbBcCntcDVo : wbBcCntcDVoList){
				if(wbBcCntcDVo.getCntcCont() != null && !wbBcCntcDVo.getCntcCont().isEmpty()){
					//연락처 유형별로 시퀀스를 정의한다.
					if(!seqMap.containsKey(wbBcCntcDVo.getCntcTypCd())) seqMap.put(wbBcCntcDVo.getCntcTypCd(), 1); 
					else seqMap.put(wbBcCntcDVo.getCntcTypCd(), seqMap.get(wbBcCntcDVo.getCntcTypCd()).intValue()+1);
					wbBcCntcDVo.setBcId(wbBcBVo.getBcId());
					wbBcCntcDVo.setBcCntcSeq(seqMap.get(wbBcCntcDVo.getCntcTypCd()));
					queryQueue.insert(wbBcCntcDVo); // 실서버 적용할때 주석 풀기
				}
			}
		}
	}
	
	/** 메일 주소록 조회 */
	public List<Map<String,String>> getMailAdrList(String typ, Map<String,Object> params) throws SQLException, IOException{
		List<Map<String,String>> returnList=null;
		SimpleQuery query=createQuery("mail");
		if(query==null){
			LOGGER.error("[ERROR] SimpleQuery is null!!");
			return null;
		}
		if(query!=null){
			try{
				// 사용자 조회
				String sql=getSelectQuery(typ, params);
				if(sql==null){
					return null;
				}
				//System.out.println("sql : "+sql);
				SimpleIterator iterator = query.queryIterator(sql, null);
				Map<String, String> listData;
				
				if(iterator!=null){
					returnList=new ArrayList<Map<String,String>>();
					while((listData = iterator.next()) != null){
						returnList.add(listData);
					}
				}
			}finally{
				query.close();
			}
		}
		return returnList;
	}
	
	/** 메일 주소록 조회 */
	public Integer getMailAdrCount(String typ, Map<String,Object> params) throws SQLException, IOException{
		Integer returnCount=null;
		if(!isOracle && !isMssql){
			isMssql = "mssql".equals(contextProperties.getProperty("dbms"));
			isOracle = "oracle".equals(contextProperties.getProperty("dbms"));
		}
		SimpleQuery query=createQuery("mail");
		if(query==null){
			LOGGER.error("[ERROR] SimpleQuery is null!!");
			return 0;
		}
		if(query!=null){
			try{
				// 사용자 조회
				String sql=getSelectQuery(typ, params);
				if(sql==null){
					return null;
				}
				returnCount = query.queryCount(sql, null);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				query.close();
			}
		}
		return returnCount;
	}
	
	/** 조회 쿼리 */
	public String getSelectQuery(String typ, Map<String,Object> params){
		if(typ.startsWith("mailUser")){
			if(isOracle){
				StringBuilder sql = new StringBuilder();
				if("mailUserCnt".equals(typ))
					sql.append("SELECT COUNT(USER_SN) AS CNT \n");
				else{
					sql.append("SELECT USER_SN, UPPER(USER_ACCOUNT) AS USER_UID\n");					
				}
				sql.append("FROM ZT_USER_INFO\n");
				sql.append("WHERE USE_FLAG = 1\n");
				sql.append("AND USER_ACCOUNT IS NOT NULL\n");
				sql.append("AND UPPER(USER_ACCOUNT) != 'MAILADMIN'\n");
				if(params!=null){
					if(params.containsKey("userSn")) sql.append("AND USER_SN ='").append(params.get("userSn")).append("'\n");
					if(params.containsKey("userUid")) sql.append("AND UPPER(USER_ACCOUNT) ='").append(params.get("userUid")).append("'\n");
					if(params.containsKey("userUidList")){
						@SuppressWarnings("unchecked")
						List<String> userUidList=(List<String>)params.get("userUidList");
						sql.append(" AND UPPER(USER_ACCOUNT) IN (");
						for(int i=0;i<userUidList.size();i++){
							if(i>0) sql.append(",");
							sql.append("'"+userUidList.get(i)+"'");
						}
						sql.append(")").append("\n");
					}
				}
				if("mailUser".equals(typ))
					sql.append(" ORDER BY USER_UID");
				return sql.toString();
			}else if(isMssql){
				StringBuilder sql = new StringBuilder();
				if("mailUserCnt".equals(typ))
					sql.append("SELECT COUNT(USER_SN) AS CNT \n");
				else{
					sql.append("SELECT USER_SN, UPPER(USER_ACCOUNT) AS USER_UID\n");					
				}
				sql.append("FROM ZT_USER_INFO\n");
				sql.append("WHERE USE_FLAG = 1\n");
				sql.append("AND USER_ACCOUNT IS NOT NULL\n");
				sql.append("AND UPPER(USER_ACCOUNT) != 'MAILADMIN'\n");
				if(params!=null){
					if(params.containsKey("userSn")) sql.append("AND USER_SN ='").append(params.get("userSn")).append("'\n");
					if(params.containsKey("userUid")) sql.append("AND UPPER(USER_ACCOUNT) ='").append(params.get("userUid")).append("'\n");
					if(params.containsKey("userUidList")){
						@SuppressWarnings("unchecked")
						List<String> userUidList=(List<String>)params.get("userUidList");
						sql.append(" AND UPPER(USER_ACCOUNT) IN (");
						for(int i=0;i<userUidList.size();i++){
							if(i>0) sql.append(",");
							sql.append("'"+userUidList.get(i)+"'");
						}
						sql.append(")").append("\n");
					}
				}
				if("mailUser".equals(typ))
					sql.append(" ORDER BY USER_UID");
				return sql.toString();
			}
		}else if(typ.startsWith("group")){
			if(isOracle){
				StringBuilder sql = new StringBuilder();
				if("groupCnt".equals(typ))
					sql.append("SELECT COUNT(T.GROUP_SN) AS CNT \n");
				else{
					sql.append("SELECT T.GROUP_SN, T.USER_SN, T.UPPER_GROUP_SN, T.GROUP_NAME, T.SORT_CODE, \n");
					sql.append("(SELECT UPPER(USER_ACCOUNT) FROM ZT_USER_INFO U WHERE T.USER_SN = U.USER_SN) AS USER_UID\n");					
				}
				sql.append("FROM ZT_ADDRESS_GROUP T\n");
				if(params!=null){
					sql.append("WHERE 1=1\n");
					if(params.containsKey("userSn")) sql.append("AND T.USER_SN ='").append(params.get("userSn")).append("'\n");
				}
				if("group".equals(typ))
					sql.append(" ORDER BY T.USER_SN, T.UPPER_GROUP_SN, T.SORT_CODE");
				return sql.toString();
			}else if(isMssql){
				StringBuilder sql = new StringBuilder();
				if("groupCnt".equals(typ))
					sql.append("SELECT COUNT(T.GROUP_SN) AS CNT \n");
				else{
					sql.append("SELECT T.GROUP_SN, T.USER_SN, T.UPPER_GROUP_SN, T.GROUP_NAME, T.SORT_CODE, \n");
					sql.append("(SELECT UPPER(USER_ACCOUNT) FROM ZT_USER_INFO U WHERE T.USER_SN = U.USER_SN) AS USER_UID\n");					
				}
				sql.append("FROM ZT_ADDRESS_GROUP T\n");
				if(params!=null){
					sql.append("WHERE 1=1\n");
					if(params.containsKey("userSn")) sql.append("AND T.USER_SN ='").append(params.get("userSn")).append("'\n");
				}
				if("group".equals(typ))
					sql.append(" ORDER BY T.USER_SN, T.UPPER_GROUP_SN, T.SORT_CODE");
				return sql.toString();
			}
		}else if(typ.startsWith("person")){
			if(isOracle){
				StringBuilder sql = new StringBuilder();
				if("personCnt".equals(typ))
					sql.append("SELECT COUNT(T.USER_SN) AS CNT \n");
				else{
					sql.append("SELECT R.GROUP_SN, T.PERSON_NAME,T.SORT_CODE,T.ENG_NAME,T.HANJA_NAME,\n");
					sql.append("T.NICKNAME,T.EMAIL,T.MOBILE,T.GENDER_CODE,T.BIRTHDAY,T.BIRTHDAY_TYPE,T.ANNIVERSARY,T.HOME_ZIPCODE,\n");
					sql.append("T.HOME_ADDRESS1,T.HOME_ADDRESS2,T.HOME_PHONE,T.HOMEPAGE,T.COMPANY_CODE,T.ORGAN_NAME,T.DEPT_NAME,\n");
					sql.append("T.ROLE_NAME,T.POSITION_NAME,T.OFFICE_ZIPCODE,T.OFFICE_ADDRESS1,T.OFFICE_ADDRESS2,T.OFFICE_PHONE,T.OFFICE_LOCAL_PHONE,\n");
					sql.append("T.OFFICE_D_PHONE,T.OFFICE_FAX,T.PICTURE_FNAME,T.WORKS,T.COMMENTS\n");
				}
				sql.append("FROM ZT_ADDRESS_PERSON T INNER JOIN ZT_GROUP_PERSON R ON T.PERSON_SN = R.PERSON_SN\n");
				if(params!=null){
					sql.append("WHERE 1=1\n");
					if(params.containsKey("userSn")) sql.append("AND T.USER_SN ='").append(params.get("userSn")).append("'");
				}
				return sql.toString();
			}else if(isMssql){
				StringBuilder sql = new StringBuilder();
				if("personCnt".equals(typ))
					sql.append("SELECT COUNT(T.USER_SN) AS CNT \n");
				else{
					if(params.containsKey("pageNo") && params.containsKey("pageRowCnt"))
						sql.append("SELECT * FROM (\n");
					sql.append("SELECT ");
					if(params.containsKey("pageNo") && params.containsKey("pageRowCnt"))
						sql.append("TOP ").append(Integer.parseInt((String)params.get("pageNo"))*Integer.parseInt((String)params.get("pageRowCnt"))).append("ROW_NUMBER() OVER (ORDER BY T.USER_SN) AS RNUM,");
					sql.append(" R.GROUP_SN, T.PERSON_NAME,T.SORT_CODE,T.ENG_NAME,T.HANJA_NAME,\n");
					sql.append("T.NICKNAME,T.EMAIL,T.MOBILE,T.GENDER_CODE,T.BIRTHDAY,T.BIRTHDAY_TYPE,T.ANNIVERSARY,T.HOME_ZIPCODE,\n");
					sql.append("T.HOME_ADDRESS1,T.HOME_ADDRESS2,T.HOME_PHONE,T.HOMEPAGE,T.COMPANY_CODE,T.ORGAN_NAME,T.DEPT_NAME,\n");
					sql.append("T.ROLE_NAME,T.POSITION_NAME,T.OFFICE_ZIPCODE,T.OFFICE_ADDRESS1,T.OFFICE_ADDRESS2,T.OFFICE_PHONE,T.OFFICE_LOCAL_PHONE,\n");
					sql.append("T.OFFICE_D_PHONE,T.OFFICE_FAX,T.PICTURE_FNAME,T.WORKS,T.COMMENTS\n");
				}
				sql.append("FROM ZT_ADDRESS_PERSON T INNER JOIN ZT_GROUP_PERSON R ON T.PERSON_SN = R.PERSON_SN\n");
				if(params!=null){
					sql.append("WHERE 1=1\n");
					if(params.containsKey("userSn")) sql.append("AND T.USER_SN ='").append(params.get("userSn")).append("'");
				}
				if("person".equals(typ) && params.containsKey("pageNo") && params.containsKey("pageRowCnt"))
					sql.append(") M WHERE RNUM > (").append((Integer.parseInt((String)params.get("pageNo"))-1)*Integer.parseInt((String)params.get("pageRowCnt"))).append("AND RNUM <= ").append(Integer.parseInt((String)params.get("pageNo"))*Integer.parseInt((String)params.get("pageRowCnt")));
				
				return sql.toString();
			}
		}
		
		return null;
		
	}
	
	public SimpleQuery createQuery(String dbName) throws SQLException {
		
		if(dbName==null) dbName = "mail";
		
		if("mail".equals(dbName)){
			if(isOracle){
				// 나라셀라 - CD4B5E
				if(CustConfig.CUST_NARACELLAR){
					Map<String, String> configMap = new HashMap<String, String>();
					configMap.put("drv", "oracle.jdbc.driver.OracleDriver");
					configMap.put("url", "jdbc:oracle:thin:@192.168.47.227:61521:ORGW");
					configMap.put("usr", "MAIL_CD4B5E_USER");
					configMap.put("pwd", "asdqwe123$%^");
					configMap.put("validationSql", "SELECT 1 FROM DUAL");
					configMap.put("maxIdle", "10");
					configMap.put("initialSize", "5");
					return SimpleQuery.create(dbName, configMap, true);
				// 프라코 - FB7F63
				} else if(CustConfig.CUST_PLAKOR){
					Map<String, String> configMap = new HashMap<String, String>();
					configMap.put("drv", "oracle.jdbc.driver.OracleDriver");
					configMap.put("url", "jdbc:oracle:thin:@222.251.186.2:61521:orange");
					configMap.put("usr", "MAIL_FB7F63_USER");
					configMap.put("pwd", "asdqwe123$%^");
					configMap.put("validationSql", "SELECT 1 FROM DUAL");
					configMap.put("maxIdle", "10");
					configMap.put("initialSize", "5");
					return SimpleQuery.create(dbName, configMap, true);
				
				// 리눅스 개발서버 - LINUX
				}else if(CustConfig.DEV_LINUX){
					Map<String, String> configMap = new HashMap<String, String>();
					configMap.put("drv", "oracle.jdbc.driver.OracleDriver");
					configMap.put("url", "jdbc:oracle:thin:@192.168.0.19:51522:DEVDB");
					configMap.put("usr", "INCOM");
					configMap.put("pwd", "INCOM4321");
					configMap.put("validationSql", "SELECT 1 FROM DUAL");
					configMap.put("maxIdle", "10");
					configMap.put("initialSize", "5");
					
					return SimpleQuery.create(dbName, configMap, true);
				}
			}else if(isMssql){
				// 덴티움 - B5B877
				if(CustConfig.CUST_DENTIUM){
					Map<String, String> configMap = new HashMap<String, String>();
					configMap.put("drv", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
					configMap.put("url", "jdbc:sqlserver://192.168.10.10:1433;DatabaseName=navidmail_db");
					configMap.put("usr", "sa");
					configMap.put("pwd", "dentium123!@#");
					configMap.put("validationSql", "SELECT 1");
					configMap.put("maxIdle", "10");
					configMap.put("initialSize", "5");
					
					return SimpleQuery.create(dbName, configMap, true);
				// 개발서버 - AD8227, 개발PC - ABC123
				} else if(CustConfig.DEV_SVR || CustConfig.DEV_PC){
					Map<String, String> configMap = new HashMap<String, String>();
					configMap.put("drv", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
					configMap.put("url", "jdbc:sqlserver://192.168.0.145:1433;DatabaseName=navidmail_newdb");
					configMap.put("usr", "sa");
					configMap.put("pwd", "innodb12345!@#$%");
					configMap.put("validationSql", "SELECT 1");
					configMap.put("maxIdle", "10");
					configMap.put("initialSize", "5");
					
					return SimpleQuery.create(dbName, configMap, true);
				}
			}
		}
		
		return null;
	}
	
	/** 이관 시작 처리 */
	private void startTransfer(EmMailAdrTranBVo emMailAdrTranBVo) throws SQLException{
		EmMailAdrTranBVo updateVo = new EmMailAdrTranBVo();
		updateVo.setTranId(emMailAdrTranBVo.getTranId());
		updateVo.setTranStrtDt("sysdate");
		//이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
		updateVo.setTranProcStatCd("processing");
		commonSvc.update(updateVo);
	}
	
	/** 이관 종료 처리 */
	private void endTransfer(EmMailAdrTranBVo emMailAdrTranBVo) throws SQLException{
		EmMailAdrTranBVo updateVo = new EmMailAdrTranBVo();
		updateVo.setTranId(emMailAdrTranBVo.getTranId());
		updateVo.setTranEndDt("sysdate");
		//이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
		updateVo.setTranProcStatCd("completed");
		commonSvc.update(updateVo);
	}
	
	/** 로그 처리 - 사용자별 */
	private void writeLogTgt(EmMailAdrTranBVo emMailAdrTranBVo, Integer seq, String errYn, Integer fldCnt, Integer adrCnt) throws SQLException{
		EmMailAdrTranTgtTVo updateVo = new EmMailAdrTranTgtTVo();		
		updateVo.setTranId(emMailAdrTranBVo.getTranId());
		updateVo.setSeq(seq);
		if(errYn!=null) updateVo.setErrYn(errYn);
		if(fldCnt!=null) updateVo.setFldCnt(fldCnt);
		if(adrCnt!=null) updateVo.setAdrCnt(adrCnt);
		commonSvc.update(updateVo);
	}
	
	/** 오류 로그 남기기 */
	private void writeErrorLog(EmMailAdrTranBVo emMailAdrTranBVo, Exception exception) throws SQLException{
		
		EmMailAdrTranBVo errVo = new EmMailAdrTranBVo();
		errVo.setTranId(emMailAdrTranBVo.getTranId());
		errVo.setTranEndDt("sysdate");
		// 이관진행상태코드 - preparing:준비중, processing:진행중, completed:완료, error:에러
		errVo.setTranProcStatCd("error");
		
		// Exception 위치 잡기
		String where = null;
		String message = exception.getMessage()==null ? exception.getClass().getCanonicalName() : exception.getMessage();
		for(StackTraceElement trace : exception.getStackTrace()){
			if(trace.getClassName().equals("com.innobiz.orange.web.em.svc.EmMailAdrTranSvc")){
				where = trace.toString();
				break;
			}
		}
		
		// 오류 로그 저장
		errVo.setErrCont(where != null ? (where+": "+message) : message);
		errVo.setErrYn("Y");
		commonSvc.update(errVo);
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
	
	
}
