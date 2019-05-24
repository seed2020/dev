package com.innobiz.orange.web.pt.secu;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Repository;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.crypto.rsa.RsaUtil;

/** 보안쿠키 체크용 객체 */
@Repository
public class SecuCookieChecker {

	/** 보안쿠키의 유효성 검증 */
	public boolean check(HttpServletRequest request, SecuCookie secuCookie){
		
		long timeAllowance = 1000L * (ServerConfig.IS_MOBILE && ServerConfig.IS_LOC ? 5 : 30);
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(Cookie cookie : cookies){
				if("secuCode".equals(cookie.getName())){
					String value = cookie.getValue();
					try {
						String dec = RsaUtil.decrypt(value, secuCookie.getPriKey(), secuCookie.getModKey(), secuCookie.getBlockSize());
						if(!dec.startsWith(secuCookie.getSecuCode())) return false;
						long reqTime = Long.parseLong(dec.substring(secuCookie.getSecuCode().length()+1));
						long timeGap = System.currentTimeMillis() - reqTime - secuCookie.getTimeGap();
						if(Math.abs(timeGap) > timeAllowance){
							return false;
						}
						return true;
					} catch(Exception ignore){
						return false;
					}
				}
			}
		}
		return false;
	}
}
