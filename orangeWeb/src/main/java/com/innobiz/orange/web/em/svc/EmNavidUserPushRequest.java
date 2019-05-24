package com.innobiz.orange.web.em.svc;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.sync.PushRequest;

/** 나비드메일 사용자 정보 푸쉬용 */
public class EmNavidUserPushRequest extends PushRequest {

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** mail 시스템 호스트 명 */
	private String mailHost = null;

	public EmNavidUserPushRequest() {
		this.deleteType = "userUid";
	}
	
	public boolean isValid(){
		
		Map<String, String> svrEnvMap = null;
		String mailHost = null;
		try {
			svrEnvMap = ptSysSvc.getSvrEnvMap();
			mailHost = svrEnvMap.get("mailCall");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// callback - "/cm/sync/getSecuData.do?secuData="+encrypted
		if(this.url == null || (mailHost!=null && mailHost.equals(this.mailHost))){
			this.url = "http://"+mailHost+"/zmail/api/innobiz_sync.nvd";
			this.mailHost = mailHost;
		}
		
		return this.deleteType!= null && this.url != null;
	}
}
