package com.innobiz.orange.web.cu.svc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.or.sync.SimpleQuery;

/** 고객사별 기능 서비스 */
@Service
public class CuFncSvc {
	
	/** contextProperties */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
	public SimpleQuery createQuery(String dbName) throws SQLException {
		
		if(dbName==null) dbName = "erp";
		
		if("erp".equals(dbName)){
			// 프라코 - FB7F63
			if(CustConfig.CUST_PLAKOR){
				
				Map<String, String> configMap = new HashMap<String, String>();
				configMap.put("drv", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
				configMap.put("url", "jdbc:sqlserver://222.251.186.21:1433;DatabaseName=ERPDB");
				configMap.put("usr", "sa");
				configMap.put("pwd", "misinfo@536");
				configMap.put("validationSql", "SELECT 1");
				configMap.put("maxIdle", "10");
				configMap.put("initialSize", "5");
				
				return SimpleQuery.create(dbName, configMap, true);
			}// 개발PC - ABC123, 개발서버 - AD8227
			else if(CustConfig.DEV_PC || CustConfig.DEV_SVR){
				Map<String, String> configMap = new HashMap<String, String>();
				configMap.put("drv", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
				configMap.put("url", "jdbc:sqlserver://222.251.186.21:1433;DatabaseName=ERPDB");
				configMap.put("usr", "sa");
				configMap.put("pwd", "misinfo@536");
				configMap.put("validationSql", "SELECT 1");
				configMap.put("maxIdle", "10");
				configMap.put("initialSize", "5");
				
				return SimpleQuery.create(dbName, configMap, true);
			}
		}
		
		return null;
	}
	
}
