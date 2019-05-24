package com.innobiz.orange.web.wc.svc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wc.utils.WcConstant;

@Service
public class WcAdmSvc {
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 환경 설정 로드 */
	public Map<String, String> getEnvConfigMap(ModelMap model, String compId) throws SQLException {
		Map<String, String> envConfigMap = ptSysSvc.getSysSetupMap(WcConstant.SYS_CONFIG+compId, true);
		if(envConfigMap == null || envConfigMap.isEmpty()){
			
			envConfigMap = new HashMap<String, String>();
			envConfigMap.put("guestAcceptYn", "Y");// 참석자수락여부
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", WcConstant.SYS_CONFIG+compId, envConfigMap);
		}
		if(model!=null) model.put("envConfigMap", envConfigMap);
		
		return envConfigMap;
	}
	
}
