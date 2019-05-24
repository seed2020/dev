package com.innobiz.orange.web.pt.sso;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtSsoTVo;

public class SsoParamClientMobile extends SsoParamClientImpl implements SsoParamClient {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	@Override
	public boolean checkSso(HttpServletRequest request,
			HttpServletResponse response) {
		
		String paramName = getParamName();
		if(paramName == null) return false;
		
		try {
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
			String uri = request.getRequestURI();
			
			if(useSSL && !request.isSecure() && request.getServerPort() == 80){
				String ssoId = request.getParameter(paramName);
				String redirectUrl = "https://"+request.getServerName()+uri+"?"+paramName+"="+ssoId;
				request.setAttribute("SSO_REDIRECT", redirectUrl);
				return true;
			}
			
			String ssoId = request.getParameter(paramName);
			PtSsoTVo ptSsoTVo = new PtSsoTVo();
			ptSsoTVo.setSsoId(ssoId);
			ptSsoTVo = (PtSsoTVo)commonDao.queryVo(ptSsoTVo);
			
			if(ptSsoTVo==null) return false;
			commonDao.delete(ptSsoTVo);
			
			String regDt = ptSsoTVo.getRegDt();
			if(regDt==null || regDt.isEmpty()) return false;
			
			String currDt = commonDao.querySysdate(null);
			long diff = java.sql.Timestamp.valueOf(currDt).getTime() - java.sql.Timestamp.valueOf(regDt).getTime();
			if(diff > 30 * 1000) return false;
			
			boolean result = false;
			
			UserVo userVo = LoginSession.getUser(request);
			if(userVo!=null && userVo.getUserUid().equals(ptSsoTVo.getUserUid())){
				result = true;
			} else {
				result = super.createSession(request, response, ptSsoTVo.getUserUid(), ptSsoTVo.getLangTypCd(), null);
			}
			
			if(result){
				userVo = LoginSession.getUser(request);
				
				String authUrl = ptSsoTVo.getAuthUrl();
				String tgtUrl  = ptSsoTVo.getTgtUrl();
				if(authUrl==null || authUrl.isEmpty()) authUrl = tgtUrl;
				String redirectUrl = ptSecuSvc.toAuthMenuUrl(userVo, tgtUrl, authUrl);
				request.setAttribute("SSO_REDIRECT", redirectUrl);
			}
			return result;
			
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
