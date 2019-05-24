package com.innobiz.orange.web.pt.sso;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtLoginSvc;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;

/** SSO 클라이언트 구현체 */
public abstract class SsoParamClientImpl implements SsoParamClient {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(SsoParamClientImpl.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 로그인 관련 서비스  */
	@Autowired
	private PtLoginSvc ptLoginSvc;
	
	/** 사용자 설정 관련 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
//	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
//	@Autowired
//	private IpChecker ipChecker;
	
	/** SSO 체크 파라미터 */
	private String paramName;
	
//	/** 포털 보안 서비스 */
//	@Autowired
//	private PtSecuSvc ptSecuSvc;

	/** SSO 파라미터 명 세팅 */
	@Override
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/** SSO 파라미터 명 리턴 */
	@Override
	public String getParamName() {
		return paramName;
	}
	
	/** 원직자로 지정해야할 요청 URI 체크*/
	public boolean chkOdurUris(HttpServletRequest request){
		boolean flag = false;
		String chkUri = request.getRequestURI();
		String[] odurUris = new String[]{"/","/cm/zmailPop.do","/ap/box/listApvBx.do"};
		for(String storedUri : odurUris){
			if(chkUri.equals(storedUri)){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/** SSO 세션 생성 */
	protected boolean createSession(HttpServletRequest request, HttpServletResponse response, 
			String userUid, String langTypCd, OrUserBVo orUserBVo) throws SQLException, CmException, IOException {
		
        // 세션 조회
        UserVo userVo = LoginSession.getUser(request);
        String sessionLangTypCd = LoginSession.getLangTypCd(request);
        
        // 원직자 대상 URI 체크
        boolean isOdurUri = chkOdurUris(request);
        // 세션의 사용자 UID와 같고 언어가 같으면 패스 - true 리턴[원직자 지정 uri가 아닌경우]
        if(!isOdurUri && userVo != null && userVo.getUserUid().equals(userUid)){
        	if(langTypCd!=null && !langTypCd.equals(sessionLangTypCd)){
        		request.getSession().setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", SessionUtil.toLocale(langTypCd));
        		userVo.setLangTypCd(langTypCd);
        	}
        	return true;
        }
        
        if(orUserBVo == null || !userUid.equals(orUserBVo.getUserUid())){
        	orUserBVo = new OrUserBVo();
    		orUserBVo.setUserUid(userUid);
    		orUserBVo.setQueryLang(langTypCd);
    		orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
        }
        
        if(orUserBVo == null){
        	LOGGER.error("SSO Login-Fail ["+request.getAttribute("BY_SSO_TYPE")+"] - "+paramName+" - no user(OR_USER_B) : "+userUid);
			return false;
        }
        
		// 원직자기본(OR_ODUR_B) 테이블 : 사용자 정보 확인 못하면 - false 리턴
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setOdurUid(orUserBVo.getOdurUid());
		orOdurBVo.setQueryLang(langTypCd);
		orOdurBVo = (OrOdurBVo)commonDao.queryVo(orOdurBVo);
		if(orOdurBVo==null){
			LOGGER.error("SSO Login-Fail ["+request.getAttribute("BY_SSO_TYPE")+"] - "+paramName+" - no user(OR_ODUR_B) : "+orUserBVo.getOdurUid());
			return false;
		}
		
		// 원직자UID
		String odurUid = orOdurBVo.getOdurUid();
		
		// 사용자상태코드 체크 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 99:삭제
		if(!"02".equals(orOdurBVo.getUserStatCd())){
			//pt.login.notAllowedStat=로그인 가능 사용자가 아닙니다.
			LOGGER.error("SSO Login-Fail ["+request.getAttribute("BY_SSO_TYPE")+"] - "+paramName+" - user stat(not 02) - odurUid:"+odurUid+"  userStatCd:"+orOdurBVo.getUserStatCd());
			return false;
		}
		
		// 원직자 지정 uri일경우 사용자 UID를 원직자UID(기본설정UID)로 변경 
		if(isOdurUri) {
			// 원직자의 개인설정 조회 - 디폴트 로그인 계정, 비밀번호변경일
			Map<String, String> odurLoginMap = ptPsnSvc.getUserSetupMap(request, odurUid, PtConstant.PT_LOGIN, true);
			// 원직자의 디폴트 로그인 계정을 조회함 
			userUid = odurLoginMap.get("defUserUid");
			//기본설정 사용자 UID가 없을경우 원직자 UID를 사용자UID에 세팅
			if(userUid == null || userUid.isEmpty()) userUid = odurUid;
		}
		
		// 세션의 사용자 UID와 사용자UID가 같고 언어가 같으면 패스 - true 리턴[원직자 지정 uri일경우]
        if(isOdurUri && userVo != null && userVo.getUserUid().equals(userUid)){
        	if(langTypCd!=null && !langTypCd.equals(sessionLangTypCd)){
        		request.getSession().setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", SessionUtil.toLocale(langTypCd));
        		userVo.setLangTypCd(langTypCd);
        	}
        	return true;
        }
        
		// 언어 설정 세션에 저장
		if(langTypCd!=null){
			request.getSession().setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", SessionUtil.toLocale(langTypCd));
		}
		
		// 이중 로그인 방지 - 에서 이전 로그인 차단 금지
		request.setAttribute("BY_SSO", Boolean.TRUE);
		// 사용자 세션 정보 생성
		ptLoginSvc.createUserSessionByOdurUid(odurUid, userUid, request, response, orOdurBVo);
		userVo = LoginSession.getUser(request);
			
		return true;
	}
	
}
