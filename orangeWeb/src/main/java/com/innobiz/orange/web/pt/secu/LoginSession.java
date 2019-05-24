package com.innobiz.orange.web.pt.secu;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;

/** 사용자의 로그인 정보 조회  */
public class LoginSession {
	
	/** 사용자 세션정보 */
	public static UserVo getUser(HttpServletRequest request){
		
		if(!ServerConfig.IS_LOC || ServerConfig.IS_MOBILE){
			HttpSession session = request.getSession(false);
			UserVo userVo = session==null ? null : (UserVo)session.getAttribute("userVo");
			return userVo;
		} else {
			HttpSession session = request.getSession(true);
			UserVo userVo = session==null ? null : (UserVo)session.getAttribute("userVo");
			if(userVo == null && "mssql".equals(System.getProperty("GW_DBMS"))){
				try {
					// 개발PC - ABC123
					if(CustConfig.DEV_PC){
						userVo = new UserVo();
						userVo.setUserUid("U0000001");
						userVo.setOdurUid("U0000001");
						userVo.setUserNm("관리자");
						userVo.setLginId("admin");
						userVo.setOrgId("O0000001");
						userVo.setDeptNm("기술지원팀");
						userVo.setCompId("A01");
						userVo.setDeptId("O0000001");
						userVo.setOrgPids(new String[]{ "O0000001" });
						userVo.setDeptPids(new String[]{ "O0000001" });
						userVo.setLoutCatId("I");
						userVo.setSkin("blue");//blue green yellow pink
						userVo.setLangTypCd("ko");
						userVo.setAdminAuthGrpIds(new String[]{PtConstant.AUTH_SYS_ADMIN, PtConstant.AUTH_ADMIN});
						userVo.setLginIp("127.0.0.1");
						userVo.setInternalIp(true);
						//session.setAttribute("userVo", userVo);
						try {
							Map<String, Map<String, ?>> userSetupMap = SecuBridge.ins.PtPsnSvc.getUserSetup("U0000001", "U0000001", null, request);
							session.setAttribute("userSetupMap", userSetupMap);
						} catch(Exception e){
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return userVo;
		}
	}
	
	/** 세션의 로케일의 LangTypCd 리턴 */
	public static String getLangTypCd(HttpServletRequest request) {
		return SessionUtil.getLangTypCd(request);
	}
	/** 세션의 로케일 리턴 */
	public static Locale getLocale(HttpServletRequest request){
		return SessionUtil.getLocale(request);
	}
}
