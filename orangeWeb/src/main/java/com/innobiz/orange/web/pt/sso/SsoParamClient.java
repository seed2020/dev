package com.innobiz.orange.web.pt.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** SSO 클라이언트 인터페이스 */
public interface SsoParamClient {

	/** SSO를 체크함 - 사용자가 확인되면 true 리턴 */
	public boolean checkSso(HttpServletRequest request, HttpServletResponse response);
	
	/** SSO 파라미터 명 세팅 */
	public void setParamName(String paramName);
	
	/** SSO 파라미터 명 리턴 */
	public String getParamName();
}