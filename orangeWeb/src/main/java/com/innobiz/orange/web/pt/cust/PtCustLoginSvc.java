package com.innobiz.orange.web.pt.cust;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 로그인 커스트마이즈 서비스 */
@Service
public class PtCustLoginSvc {

	/** 커스트마이즈 로그인 진행 */
	public void processCustLogin(HttpServletRequest request, HttpServletResponse response, UserVo userVo, 
			OrUserBVo orUserBVo, OrOdurBVo orOdurBVo) throws IOException{

		///////////////////////////////
		//
		// 한화제약 - 도메인 쿠키 생성
		if(CustConfig.CUST_HANWHA && !ServerConfig.IS_MOBILE){
			String domain = request.getServerName();
			String refVa1 = (orUserBVo!=null) ? orUserBVo.getRefVa1() : null;
			int p = domain.indexOf('.');
			if(refVa1 != null && !refVa1.isEmpty() && p>0){
				
				Cookie cookie;
				domain = domain.substring(p);
				
				cookie = new Cookie("LEGACYUSERINFO", URLEncoder.encode(orOdurBVo.getLginId(), "utf-8"));
				cookie.setDomain(domain);
				cookie.setPath("/");
				response.addCookie(cookie);
				
				cookie = new Cookie("GUBUN", URLEncoder.encode(refVa1, "utf-8"));
				cookie.setDomain(domain);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		
	}
	
	
}
