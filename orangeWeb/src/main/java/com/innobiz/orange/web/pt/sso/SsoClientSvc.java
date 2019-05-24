package com.innobiz.orange.web.pt.sso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.utils.LayoutUtil;

/** SSO 클라이언트 서비스 - 메시저 등 외부 시스템에서 포탈을 SSO로 로그인 할때 */
@Service
public class SsoClientSvc {

	/** 파라미터를 사용하여 SSO 처리하는 클라이언트 목록 */
	private List<SsoParamClient> paramClientList = null;
	
	/** SSO 파라미터로 세션 생성 */
	public boolean checkSso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		if(paramClientList != null){
			for(SsoParamClient client : paramClientList){
				
				String param = request.getParameter(client.getParamName());
				if(param != null && !param.isEmpty()){
					if(client.checkSso(request, response)){
						String message = (String)request.getAttribute("SSO_MESSAGE");
						if(message!=null){
							request.removeAttribute("SSO_MESSAGE");
							request.getSession().setAttribute("message", message);
						}
						String fwd = (String)request.getAttribute("SSO_REDIRECT");
						if(fwd!=null){
							request.removeAttribute("SSO_REDIRECT");
							response.sendRedirect(fwd);
							return true;
						}
						if(message!=null){
							request.getSession().setAttribute("togo", "/");
							response.sendRedirect(LayoutUtil.getResultJsp()+".do");
							return true;
						}
						return false;
					}
				}
				
			}
		}
		return false;
	}
	
	/** 설정된 SSO 클라이언트를 세팅함 - servlet-context.xml */
	@Resource(name = "ssoParamClientList")
	public void setSsoParamClientList(List<SsoParamClient> ssoParamClientList){
		if(ssoParamClientList != null){
			String paramName;
			for(SsoParamClient ssoParamClient : ssoParamClientList){
				paramName = ssoParamClient.getParamName();
				if(paramName != null && !paramName.isEmpty()){
					if(paramClientList==null){
						paramClientList = new ArrayList<SsoParamClient>();
					}
					paramClientList.add(ssoParamClient);
				}
			}
		}
	}
	
}
