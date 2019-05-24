package com.innobiz.orange.web.cm.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/** Session 관련 처리 Util */
public class SessionUtil {

	/** 메뉴, 권한 변경 시간 - 이 시간을 기준으로 세션정보의 메뉴를 다시 구성함 */
	public static long lastAuthChangedTime = 0;

	/** 결재 대리인설정 만료 시간 */
	public static long lastApAgntTime = 0;
	
	/** 세션의 로케일 리턴 */
	public static Locale getLocale(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Locale locale = session == null ? null : (Locale)session.getAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE");
		if(locale==null) locale = Locale.KOREA;
		return locale;
	}
	
	/** 세션의 로케일의 LangTypCd 리턴 */
	public static String getLangTypCd(HttpServletRequest request) {
		return getLocale(request).getLanguage();
	}
	
	/** 스트링을 Locale로 변환 */
	public static Locale toLocale(String langTypCd){
		Locale locale = "ko".equals(langTypCd) || langTypCd==null || langTypCd.isEmpty() ? Locale.KOREA :
			 "en".equals(langTypCd) ? Locale.US :
				 "zh".equals(langTypCd) ? Locale.CHINA :
					 "ja".equals(langTypCd) ? Locale.JAPAN :
						 new Locale(langTypCd);
		return locale;
	}
	
	/** 권한 및 메뉴 변경시간 설정 */
	public static void expireSessionAuth(){
		lastAuthChangedTime = System.currentTimeMillis();
	}
	
	/** 세션에 저장된 메뉴가 만료되었는지 리턴(변경되었는지) */
	public static boolean isAuthInSessionExpired(long authCachedTime){
		return authCachedTime==0 || lastAuthChangedTime > authCachedTime;
	}
	
	/** 결재 대리인설정 만료 시킴 */
	public static void expireApAgnt(){
		lastApAgntTime = System.currentTimeMillis();
	}
	
	/** 결재 대리인설정이 만료되었는지 리턴 */
	public static boolean isApAgntExpired(long apAgntTime){
		return apAgntTime==0 || lastApAgntTime > apAgntTime;
	}
}
