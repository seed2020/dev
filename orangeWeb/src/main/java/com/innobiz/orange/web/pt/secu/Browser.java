package com.innobiz.orange.web.pt.secu;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/** 브라우저 정보를 담는 객체 */
public class Browser {

	/** MS Explore */
	private boolean ie = false;
	/** Opera */
	private boolean opera = false;
	/** Chrome */
	private boolean chrome = false;
	/** Firefox */
	private boolean firefox = false;
	/** Safari */
	private boolean safari = false;
	/** IE 호환성보기 */
	private boolean ieCompatibility = false;
	/** 기타 */
	private boolean etc = false;
	/** https 여부 */
	private boolean secure = false;
	/** 버전 */
	private int ver = 0;
	/** 이름 */
	private String agent = null;
	
	private static Pattern ieCompatibilityPattern = Pattern.compile("MSIE 7.*Trident");

	/** 브라우저 정보를 세팅함 */
	public static void setBrowser(HttpServletRequest request){
		String agent = request.getHeader("User-Agent");
		
		if(agent==null || agent.isEmpty()){
			Browser browser = new Browser();
			browser.etc = true;
			browser.secure = request.isSecure();
			request.setAttribute("browser", browser);
			return;
		}
		
		int p;
		Browser browser = new Browser();
		browser.secure = request.isSecure();
		
		if((p=agent.indexOf("MSIE"))>0){
			browser.ie = true;
			String ver = agent.substring(p+5, agent.indexOf(";",p+5));
			if((p=ver.indexOf('.'))>0) ver = ver.substring(0, p).trim();
			browser.ver = Integer.parseInt(ver);
			browser.agent = "ie"+ver;
		} else if((p=agent.indexOf("Trident"))>0){
			browser.ie = true;
			p = agent.indexOf("rv:",p+8);
			browser.ver = Integer.parseInt(agent.substring(p+3, agent.indexOf('.',p+3)));
			browser.agent = "ie"+browser.ver;
		} else if(agent.indexOf("OPR/")>0){
			browser.opera = true;
			browser.agent = "opera";
		} else if(agent.indexOf("Chrome")>0){
			browser.chrome = true;
			browser.agent = "chrome";
		} else if(agent.indexOf("Firefox")>0){
			browser.firefox = true;
			browser.agent = "firefox";
		} else if(agent.indexOf("Safari")>0){
			browser.safari = true;
			browser.agent = "safari";
		} else if( (agent.indexOf("iPhone")>0 || agent.indexOf("iPad")>0 || agent.indexOf("iPod")>0) && agent.indexOf("AppleWebKit")>0){
			browser.safari = true;
			browser.agent = "safari";
		} else if(agent.indexOf("Opera")>0){
			browser.opera = true;
			browser.agent = "opera";
		} else {
			browser.etc = true;
			browser.agent = "etc browser";
		}
		
		if(ieCompatibilityPattern.matcher(agent).find()){
			browser.ieCompatibility = true;
		}
		
		request.setAttribute("browser", browser);
	}

	/** IE */
	public boolean isIe() {
		return ie;
	}
	/** 오페라 */
	public boolean isOpera() {
		return opera;
	}
	/** 크롬 */
	public boolean isChrome() {
		return chrome;
	}
	/** 파이어폭스 */
	public boolean isFirefox() {
		return firefox;
	}
	/** 사파리 */
	public boolean isSafari() {
		return safari;
	}
	/** 기타 */
	public boolean isEtc() {
		return etc;
	}
	/** https 여부 */
	public boolean isSecure(){
		return secure;
	}
	/** IE 호환성보기 */
	public boolean isIeCompatibility() {
		return ieCompatibility;
	}
	/** 버전 */
	public int getVer() {
		return ver;
	}
	public String getAgent(){
		return agent;
	}
	/** 모바일 여부 */
	public static boolean isMobile(HttpServletRequest request){
		String agent = request.getHeader("User-Agent");
		if(agent==null || agent.isEmpty()) return false;
		
		return agent.matches(".*(Android|iPhone|iPad|iPod).*")
				|| agent.matches(".*(LG|SAMSUNG|Samsung).*")
				|| agent.matches(".*(Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*");
	}
}
