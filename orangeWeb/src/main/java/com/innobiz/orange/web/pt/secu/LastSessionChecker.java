package com.innobiz.orange.web.pt.secu;

import java.util.HashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.utils.Hash;

/*  - web.xml
	<listener>
		<listener-class>com.innobiz.orange.web.pt.secu.LastSessionChecker</listener-class>
	</listener>
*/
/** 최종 세션 체크 - 중복 로그인 방지시 사용 */
public class LastSessionChecker implements HttpSessionListener, ServletContextListener {

	/** 최종 세션 체크 여부 */
	private static boolean checkLastSession = false;
	
	/** 세션ID 맵 */
	private static HashMap<Integer, String> LAST_SESSIONID_MAP = new HashMap<Integer, String>();
	
	/** 세션생성 */
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		// do nothing
	}

	/** 세션소멸 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		// MOBILE SSO 에 의한 세션, ADMIN 에 의한 세션
		if(session.getAttribute("SKIP_SESSION_CHECK")!=null) return;
		
		UserVo userVo = session==null ? null : (UserVo)session.getAttribute("userVo");
		if(userVo != null){
			Integer odurHash = Hash.hashId(userVo.getOdurUid());
			String lastSessionId = LAST_SESSIONID_MAP.get(odurHash);
			if(lastSessionId!=null && lastSessionId.equals(session.getId())){
				LAST_SESSIONID_MAP.remove(odurHash);
			}
		}
		if(ServerConfig.IS_LOC){
			KeepAliver.destroyKeepAlive(session.getId());
		}
	}
	
	/** 최종세션 설정 - 로그인시 처리 */
	public static void setLastSession(boolean checkLastSession, String odurUid, HttpServletRequest request){
		if(LastSessionChecker.checkLastSession != checkLastSession){
			LastSessionChecker.checkLastSession = checkLastSession;
		}
		
		if(checkLastSession){
			HttpSession session = request.getSession();
			
			// MOBILE SSO 에 의한 세션 or 관리자에 의한 세션
			if(request.getAttribute("BY_ADMIN") != null
					|| request.getAttribute("BY_MSG") != null
					|| request.getAttribute("BY_SSO") != null ){
				session.setAttribute("SKIP_SESSION_CHECK", Boolean.TRUE);
				return;
			}
			
			LAST_SESSIONID_MAP.put(Hash.hashId(odurUid), session.getId());
		}
		if(ServerConfig.IS_LOC){
			HttpSession session = request.getSession();
			UserVo userVo = (UserVo)session.getAttribute("userVo");
			KeepAliver.createKeepAlive(session.getId(), userVo.getUserUid());
		}
	}
	
	/** 최종세션 체크 */
	public static boolean isLastSession(String odurUid, HttpSession session){
		if(!checkLastSession) return true;
		if("U0000001".equals(odurUid)) return true;
		if(session.getAttribute("SKIP_SESSION_CHECK")!=null) return true;
		String lastSessionId = LAST_SESSIONID_MAP.get(Hash.hashId(odurUid));
		return lastSessionId!=null && lastSessionId.equals(session.getId());
	}
	
	/** 세션 정보 제거 */
	public static void clearLastSession(){
		LAST_SESSIONID_MAP.clear();
	}

	/////////////////////////////////////////
	//
	//   for - ServletContextListener
	//
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		if(ServerConfig.IS_LOC){
			KeepAliver.initializKeepAlive();
		}
	}
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if(ServerConfig.IS_LOC){
			KeepAliver.storeKeepAlive();
		}
	}
}
