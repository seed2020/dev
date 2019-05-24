package com.innobiz.orange.web.ap.cust;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.or.sync.SimpleQuery;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 고객사별 기능 서비스 */
@Service
public class ApCustFncSvc {

//	@Autowired
//	private ApCustHanwhaSvc apCustHanwhaSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 디폴트 값 */
	private Map<String, String> custMap = new HashMap<String, String>();
	
	/** 생성자 */
	public ApCustFncSvc(){
		custMap.put("signAreaMaxCnt", "8");
	}
	
	public SimpleQuery createQuery(String dbName) throws SQLException {
		
		if(dbName==null) dbName = "fnc";
		
		// E78CEB : 한화제약
		if(CustConfig.CUST_HANWHA){
			
			Map<String, String> configMap = new HashMap<String, String>();
			configMap.put("drv", "oracle.jdbc.driver.OracleDriver");
			configMap.put("url", "jdbc:oracle:thin:@192.168.10.3:1521:hwbl21");
			configMap.put("usr", "hwblerp");
			configMap.put("pwd", "hwbl21");
			configMap.put("validationSql", "SELECT 1 FROM DUAL");
			configMap.put("maxIdle", "10");
			configMap.put("initialSize", "5");
			
			return SimpleQuery.create(dbName, configMap, true);
		}// AD8227 : 테스트
		else if(CustConfig.DEV_SVR || CustConfig.DEV_PC){
			Map<String, String> configMap = new HashMap<String, String>();
			configMap.put("drv", "oracle.jdbc.driver.OracleDriver");
			configMap.put("url", "jdbc:oracle:thin:@192.168.0.19:51522:DEVDB");
			configMap.put("usr", "GW_LINUX_USER");
			configMap.put("pwd", "asdqwe123$%^");
			configMap.put("validationSql", "SELECT 1 FROM DUAL");
			configMap.put("maxIdle", "10");
			configMap.put("initialSize", "5");
			
			return SimpleQuery.create(dbName, configMap, true);
		}
		
		return null;
	}
	
	/** 회사별 서면 결재 직위칸 하드코딩 */
	public void setCustApvLnWrtn(ModelMap model, String makDt, String langTypCd){
		
		String[] positDispVas = null;
		
		// FB7F63 : (주)프라코
		if(CustConfig.CUST_PLAKOR){
			
			// 타이틀 높이를 작게함
			model.put("shortTitleYn", Boolean.TRUE);
			
			// 한글이면
			if("ko".equals(langTypCd)){
				
				if(makDt!=null && !makDt.isEmpty() && "2018-01-24".compareTo(makDt) > 0){
					positDispVas = new String[]{"담당","대리","차장","부장"};
				} else {
					positDispVas = new String[]{"담당","과장","차장","부장"};
				}
			}
			
		// DOOWON : (주)두원
		} else if(CustConfig.CUST_DOOWON){
			
			// 한글이면
			if("ko".equals(langTypCd)){
				positDispVas = new String[]{"담당/대리","과장/차장","팀장"};
			}
			
		// DAEJIN : 대진글라스
		} else if(CustConfig.CUST_DAEJIN_G){
			// 한글이면
			if("ko".equals(langTypCd)){
				positDispVas = new String[]{"담당","검토","팀장","임원"};
			}
		}
		
		if(positDispVas != null){
			@SuppressWarnings("unchecked")
			List<ApOngdApvLnDVo> apvrList = (List<ApOngdApvLnDVo>)model.get("prcApvrList");
			// 양식의 길이가 설정된 것과 같으면
			if(apvrList !=null && apvrList.size() == positDispVas.length){
				for(int i=0;i<positDispVas.length;i++){
					apvrList.get(i).setPositDispVa(positDispVas[i]);
				}
			}
		}
		
	}
	
	/** erp xml 로부터 html 생성 */
	public String createBodyHtmlFromErpXml(String intgNo, String erpFormId){
		
		try {
			HttpClient client = new HttpClient();
			
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			String domain = ServerConfig.IS_LOC ? "127.0.0.1:8080" : svrEnvMap.get("webDomain");
			String port = "Y".equals(sysPlocMap.get("portForwarding")) ? ":60080" : "";
			String url = "http://"+domain+port+"/ap/self/getHtmlFromErpXml.do?intgNo="+intgNo+"&erpFormId="+erpFormId;
			return client.sendGet(url, "UTF-8");
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Scheduled(fixedDelay=120000)
	public void checkExpired(){
		try {
			SimpleQuery.checkExpired();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 고객사별 값 조회 */
	public String getCustValue(String key) throws CmException{
		
		// 도장방 최대 갯수
		if(key.equals("signAreaMaxCnt")){
			// (주)프라코
			if(CustConfig.CUST_PLAKOR) return "12";
			// SENTEC : (주)센텍
			if(CustConfig.CUST_SENTEC) return "10";
			// 개발서버
			if(CustConfig.DEV_SVR || CustConfig.DEV_PC) return "14";
			// DOOWON : (주)두원
			if(CustConfig.CUST_DOOWON) return "10";
		}
		
		return custMap.get(key);
	}
	
	/** 고객사별 값 조회 - Integer */
	public Integer getIntCustValue(String key) throws CmException{
		return Integer.valueOf(getCustValue(key));
	}
}
