package com.innobiz.orange.web.ap.svc;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.vo.ApAgnApntDVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApStorRVo;
import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.or.vo.OrOrgApvDVo;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtLstSetupDVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;

/** 결재 공통 서비스 */
@Service
public class ApCmSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApCmSvc.class);

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	/** 옵션 조회(캐쉬) - key : optConfigMap */
	public Map<String, String> getOptConfigMap(ModelMap model, String compId) throws SQLException {
		Map<String, String> optConfigMap = ptSysSvc.getSysSetupMap(ApConstant.AP_SYS_CONFIG+compId, true);
		if(optConfigMap == null || optConfigMap.isEmpty()){
			
			optConfigMap = new HashMap<String, String>();
			optConfigMap.put("absMark", "absRson");
			optConfigMap.put("censrLoc", "all");
			optConfigMap.put("docNoCat", "notUse");
			optConfigMap.put("docNoDept", "deptAbs");
			optConfigMap.put("docNoSeqLen", "4");
			optConfigMap.put("docNoFxLen", "Y");
			optConfigMap.put("docNoYr", "YYYY");
			optConfigMap.put("needLastApvr", "Y");
			optConfigMap.put("introRange", "org");
			optConfigMap.put("alwChgOfcSeal", "Y");
			optConfigMap.put("recoDt", "1");
			optConfigMap.put("recoMt", "1");
			optConfigMap.put("recvRecLstBaseDt", "recvDt");
			optConfigMap.put("regRecLstBaseDt", "enDt");
			optConfigMap.put("signAreaDt", "yyyy-MM-dd");
			optConfigMap.put("signAreaSign", "psn");
			optConfigMap.put("signAreaUserTitl", "posit");
			optConfigMap.put("signAreaDeptTitl", "deptNm");
			optConfigMap.put("waitBxAbs", "Y");
			optConfigMap.put("sendFrom", "toSendBx");
			//optConfigMap.put("pichBx", "waitBx");
			
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", ApConstant.AP_SYS_CONFIG+compId, optConfigMap);
		}
		if(model!=null) model.put("optConfigMap", optConfigMap);
		
		return optConfigMap;
	}
	
	/** 옵션설정을 JSON 형태로 Model 에 설정 - key : optConfigJson - javascript 에서 사용할 목적 */
	public String setOptConfigJson(ModelMap model, String compId) throws SQLException {
		String jsonString = (String)ptCacheSvc.getCache(CacheConfig.SYS_SETUP, "ko", ApConstant.AP_SYS_CONFIG_JSON, 10);
		if(jsonString==null){
			Map<String, String> optConfigMap = getOptConfigMap(null, compId);
			jsonString = JsonUtil.toJson(optConfigMap);
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", ApConstant.AP_SYS_CONFIG_JSON, jsonString);
		}
		model.put("optConfigJson", jsonString);
		return jsonString;
	}
	
	/** 결재 용어설정을 Model 에 설정 - key : apTermList - javascript 에서 사용할 목적 */
	public List<String[]> setApTermList(ModelMap model, Locale locale) throws SQLException{
		
		String termVa;
		Map<String, String> termMap = ptSysSvc.getTermMap("ap.term", locale.getLanguage());
		
		List<String[]> apTermList = new ArrayList<String[]>();
		for(String termId : ApConstant.AP_TERMS){
			termVa = termMap.get(termId);
			try{
				if(termVa==null) termVa = messageProperties.getMessage("ap.term."+termId, locale);
			} catch(Exception exception){
				LOGGER.error(exception.getMessage());
			}
			if(termVa!=null){
				apTermList.add(new String[]{ termId, termVa });
			}
		}
		model.put("apTermList", apTermList);
		return apTermList;
	}
	
	/** 결재 용어설정 값 리턴 */
	public String getApTerm(String termId, Locale locale) throws SQLException{
		Map<String, String> termMap = ptSysSvc.getTermMap("ap.term", locale.getLanguage());
		String termVa = termMap.get(termId);
		if(termVa==null) termVa = messageProperties.getMessage("ap.term."+termId, locale);
		return termVa;
	}
	
	/** 피 대결자 UID 목록 리턴 - 자신의 UID 포함 */
	public List<String> getApAgntUidList(UserVo userVo) throws SQLException{
		Long agntChkTm = userVo.getAgntChkTm();
		if(agntChkTm==null || SessionUtil.isApAgntExpired(agntChkTm.longValue())){
			long current = System.currentTimeMillis();
			userVo.setAgntChkTm(current);
			
			// WAS 기준 시간으로 해야함
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
			String dbTime = format.format(new Date(current));
			int hour = Integer.parseInt(dbTime.substring(11, 13), 10);
			String standardDt = dbTime.substring(0, 11) + (hour < 12 ? "00:00:00" : "12:00:00");
			
			// 피 대결자 목록 조회
			ApAgnApntDVo apAgnApntDVo = new ApAgnApntDVo();
			apAgnApntDVo.setAgntUid(userVo.getUserUid());
			apAgnApntDVo.setDurCat("between");
			apAgnApntDVo.setDurStrtDt(standardDt);
			apAgnApntDVo.setDurEndDt(standardDt);
			apAgnApntDVo.setOrderBy("USER_UID");
			@SuppressWarnings("unchecked")
			List<ApAgnApntDVo> apAgnApntDVoList = (List<ApAgnApntDVo>)commonDao.queryList(apAgnApntDVo);
			
			// 피 대결자 목록 - UserVo에 세팅
			if(apAgnApntDVoList==null || apAgnApntDVoList.isEmpty()){
				userVo.setAgntUids(null);
			} else {
				int i, size = apAgnApntDVoList.size();
				String[] agntUids = new String[size];
				for(i=0;i<size;i++) agntUids[i] = apAgnApntDVoList.get(i).getUserUid();
				userVo.setAgntUids(agntUids);
			}
		}
		
		// 피 대결자로 지정되지 않았으면 null 리턴
		String[] agntUids = userVo.getAgntUids();
		if(agntUids==null) return null;
		
		// 자신을 포함한 피 대결자 목록 - List로 변환 리턴
		List<String> agntUidList = new ArrayList<String>();
		agntUidList.add(userVo.getUserUid());
		for(String agntUid : agntUids){
			agntUidList.add(agntUid);
		}
		return agntUidList;
	}
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws SQLException{
		if("AP_RESC_B".equals(tableName)){
			return commonSvc.createId(tableName, 'R', 8);
		} else if("AP_RESC_D".equals(tableName)){
			return commonSvc.createId(tableName, 'R', 8);
		} else if("AP_CLS_INFO_D".equals(tableName)){
			return commonSvc.createId(tableName, 'C', 8);
		} else if("AP_FORM_BX_D".equals(tableName)){
			return commonSvc.createId(tableName, 'B', 8);
		} else if("AP_FORM_B".equals(tableName)){
			return commonSvc.createId(tableName, 'F', 8);
		} else if("AP_APV_LN_GRP_B".equals(tableName)){
			return commonSvc.createId(tableName, 'L', 8);
		} else if("AP_RECV_GRP_B".equals(tableName)){
			return commonSvc.createId(tableName, 'V', 8);
		} else if("AP_ERP_FORM_B".equals(tableName)){
			return commonSvc.createId(tableName, 'E', 8);
		} else if("AP_PSN_CLS_INFO_D".equals(tableName)){
			return commonSvc.createId(tableName, 'P', 8);
		} else if("AP_TRAN_B".equals(tableName)){
			return commonSvc.createId(tableName, 'T', 8);
		}
		return null;
	}
	
	/** 리스트 환경 설정 - 에 따른 정렬순서 세팅 */
	public List<PtLstSetupDVo> setListQueryOptions(ApOngdBVo apOngdBVo, String bxId) throws SQLException{
		// 리스트 옵션 조회
		List<PtLstSetupDVo> ptLstSetupDVoList = ptSysSvc.getPtLstSetupDVoList(bxId);
		if(apOngdBVo==null) return ptLstSetupDVoList;
		
		// 정렬순서용 설정 찾기
		PtLstSetupDVo ptLstSetupDVo = null;
		for(PtLstSetupDVo storedPtLstSetupDVo : ptLstSetupDVoList){
			if(storedPtLstSetupDVo.getDataSortVa() != null && !storedPtLstSetupDVo.getDataSortVa().isEmpty()){
				ptLstSetupDVo = storedPtLstSetupDVo;
				break;
			}
		}
		
		// 정렬순서 세팅
		if(ptLstSetupDVo!=null){
			String atrbId = ptLstSetupDVo.getAtrbId();//속성ID
			String sortOptVa = ptLstSetupDVo.getSortOptVa();//정렬옵션값 - code:코드 테이블의 정렬순서, name:텍스트순
			String dataSortVa = ptLstSetupDVo.getDataSortVa();//데이터정렬값 - asc:내림차순, desc:올림차순
			dataSortVa = dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase();
			
			if("code".equals(sortOptVa)){
				String column = StringUtil.fromCamelNotation(atrbId.substring(0, atrbId.length()-2)+"Cd");
				apOngdBVo.setSortOrdr("(SELECT SORT_ORDR FROM PT_CD_B WHERE CLS_CD='"+column+"' AND CD=T."+column+")");
				apOngdBVo.setOrderBy("SORT_ORDR "+dataSortVa);
			} else if(atrbId.endsWith("Dd")) {
				String column = StringUtil.fromCamelNotation(atrbId.substring(0, atrbId.length()-2)+"Dt");
				apOngdBVo.setOrderBy(column+" "+dataSortVa);
			} else {
				String column = StringUtil.fromCamelNotation(atrbId);
				apOngdBVo.setOrderBy(column+" "+(dataSortVa==null||dataSortVa.isEmpty() ? "ASC" : dataSortVa.toUpperCase()));
			}
		} else {
			// 정렬순서 - 함별 디폴트 설정
			if("waitBx".equals(bxId) || "deptBx".equals(bxId) || "myBx".equals(bxId) || "postApvdBx".equals(bxId)){//대기함, 부서대기함, 기안함, 후열함 - 결재선 도착일시
				apOngdBVo.setOrderBy("PREV_APVR_APV_DT DESC");//이전결재자결재일시
			} else if("toSendBx".equals(bxId) || "censrBx".equals(bxId)){//발송함, 심사함 - 담당자 도착일시
				apOngdBVo.setOrderBy("PREV_APVR_APV_DT DESC");//이전담당자처리일시
			} else if("ongoBx".equals(bxId) || "drftBx".equals(bxId) || "admOngoBx".equals(bxId)){//진행함, 개인함
				apOngdBVo.setOrderBy("MAK_DT DESC");//기안일시
			} else if("apvdBx".equals(bxId) || "rejtBx".equals(bxId) || "admRegRecLst".equals(bxId) || "admApvdBx".equals(bxId) || "admRejtBx".equals(bxId)){//완료함, 반려함, 반려문서(관리자-추가)
				apOngdBVo.setOrderBy("CMPL_DT DESC");//완료일시
			} else if("recvBx".equals(bxId) || "distBx".equals(bxId) || "admRecvRecLst".equals(bxId) || "admDistRecLst".equals(bxId)){//접수함, 배부함
				apOngdBVo.setOrderBy("CMPL_DT DESC");//완료일시
			} else if("regRecLst".equals(bxId)){//등록대장
				apOngdBVo.setOrderBy("CMPL_DT DESC");//완료일시
			} else if("recvRecLst".equals(bxId) || "distRecLst".equals(bxId)){// 접수대장, 배부대장
				apOngdBVo.setOrderBy("RECV_DT DESC");//접수일자(배부일자)
			} else if("admOpinBx".equals(bxId) || "opinBx".equals(bxId)){
				apOngdBVo.setOrderBy("CMPL_DT DESC, APV_NO DESC");//접수일자(배부일자)
			}
		}
		
		return ptLstSetupDVoList;
	}
	
	/** 저장소 조회 */
	public String queryStorage(String apvNo) throws SQLException{
		ApStorRVo apStorRVo = new ApStorRVo();
		apStorRVo.setApvNo(apvNo);
		apStorRVo = (ApStorRVo)commonDao.queryVo(apStorRVo);
		if(apStorRVo!=null && !apStorRVo.getStorId().isEmpty()){
			return apStorRVo.getStorId();
		}
		return null;
	}
	
	/** NO 통합 생성 */
	public Long createNo(String tableName) throws SQLException {
		if("AP_ONGD_B".equals(tableName)){
			return commonSvc.nextVal(tableName);
		}
		return null;
	}
	
	/** String 을 int 로 변환 후 1을 빼서 String 변환하여 리턴 */
	public String addNo(String no, int add){
		int intNo = (no==null || no.isEmpty()) ? 0 : Integer.parseInt(no);
		return Integer.toString(intNo + add);
	}
	
	/** 문서과 조회 */
	public String getCrdOrgId(String deptId) throws SQLException{
		// 배부함, 배부대장의 경우 - 문서과 또는 대리문서과로 해당 부서를 지정 한 경우
		OrOrgApvDVo orOrgApvDVo = new OrOrgApvDVo();
		orOrgApvDVo.setRealCrdOrgId(deptId);
		@SuppressWarnings("unchecked")
		List<OrOrgApvDVo> orOrgApvDVoList = (List<OrOrgApvDVo>)commonDao.queryList(orOrgApvDVo);
		if(orOrgApvDVoList != null && !orOrgApvDVoList.isEmpty()){
			return orOrgApvDVoList.get(0).getOrgId();
		}
		return null;
	}
	
	/** 결재 메뉴의 리소스명 조회 */
	public String getMenuRescNmByBxId(String compId, String bxId, String langTypCd) throws SQLException{
		List<PtMnuDVo> mnuList = ptLoutSvc.getMnuListByGrpMdRid("AP", compId, langTypCd);
		if(mnuList==null) return null;
		String finding = "bxId="+bxId;
		String url = null;
		for(PtMnuDVo ptMnuDVo : mnuList){
			url = ptMnuDVo.getMnuUrl();
			// 시행문 작성 등 - form을 물고 있는 추가 메뉴 제외
			if(url!=null && url.indexOf(finding)>0 && url.indexOf("formId=")<0){
				return ptMnuDVo.getRescNm();
			}
		}
		return null;
	}
}
