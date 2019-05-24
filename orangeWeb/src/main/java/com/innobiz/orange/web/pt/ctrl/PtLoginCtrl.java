package com.innobiz.orange.web.pt.ctrl;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaBullPopupDVo;
import com.innobiz.orange.web.bb.vo.BaBullPopupDispDVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserLginHVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.or.vo.OrUserPwLostDVo;
import com.innobiz.orange.web.pt.ip.ForeignIpBlocker;
import com.innobiz.orange.web.pt.secu.CRC32;
import com.innobiz.orange.web.pt.secu.FailLockManager;
import com.innobiz.orange.web.pt.secu.IpChecker;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoginSecuSvc;
import com.innobiz.orange.web.pt.svc.PtLoginSvc;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.utils.SysSetupUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtLginImgDVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtUserUagntDVo;
import com.innobiz.orange.web.wv.svc.WvSurvSvc;

/** 로그인 로그아웃 관련 일련의 작업 처리 */
@Controller
public class PtLoginCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtLoginCtrl.class);
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 패스워드 암호화 : SHA 알고리즘 */
	@Resource(name = "passwordEncoder")
	private PasswordEncoder passwordEncoder;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 로그인 관련 서비스  */
	@Autowired
	private PtLoginSvc ptLoginSvc;

	/** 관리자 로그인 관련 서비스 */
	@Autowired
	private PtLoginSecuSvc ptLoginSecuSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
//	/** 결재 함 서비스 */
//	@Autowired
//	private ApBxSvc apBxSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
	@Autowired
	private IpChecker ipChecker;
	
//	/** 사용자 개인 설정 서비스 */
//	@Autowired
//	private PtMyMnuSvc ptMyMnuSvc;

	/** 중국, 해외 IP 차단 정책에 따른 요청에 차단 및 딜레이 적용 */
	@Autowired
	private ForeignIpBlocker foreignIpBlocker;
	
	/** 로그인 실패 장금 장치 */
	@Autowired
	private FailLockManager failLockManager;
	
	/** 비밀번호 찾기 유효 시간 */
	private static final int LOST_PW_VALID_HOUR = 4;
	
	/** 게시판관리 서비스 */
	@Resource(name = "bbBrdSvc")
	private BbBrdSvc bbBrdSvc;

	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;
	
	/** 설문 서비스 */
	@Resource(name = "wvSurvSvc")
	private WvSurvSvc wvSurvSvc;
	
	/** [AJAX] 시큐어 세션 생성 : RSA키를 세션에 저장하고, 공개키를 Json으로 브라우저에 전달함 */
	@RequestMapping(value = "/cm/login/createSecuSessionAjx", method = RequestMethod.GET)
	public String createSecuSessionAjx(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "secuSessionCode", required = false) String secuSessionCode,
			ModelMap model){
		
		// 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지.
		
		HttpSession session = request.getSession(false);
		if(session==null){
			response.setStatus(403);
			return null;
		}
		
		UserVo userVo = LoginSession.getUser(request);
		if(userVo == null){
			if(secuSessionCode==null || secuSessionCode.isEmpty()
					|| !secuSessionCode.equals(session.getAttribute("secuSessionCode"))){
				response.setStatus(400);
				return null;
			}
		}
		
		if(secuSessionCode!=null && !secuSessionCode.isEmpty()){
			try{
				//IP 정책 체크 - 외부 로그인 가능한 사용자 체크
				String ip = ipChecker.getIp(request);
				// 로그인 IP 정책 이 있고 로그인 할 수 있는 사용자는 제외
				if( !(ipChecker.isInternalIpOnlyPloc() && ipChecker.isInternalIpRange(ip)) ){
					// 정책에 따른 로그인 화면 딜레이 적용 - 중국계IP 또는, 외국 IP
					boolean requestOk = foreignIpBlocker.delaySecureSession(request);
					if(!requestOk){
						response.setStatus(400);
						return null;
					}
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		cryptoSvc.prepareRsa(session, model);
		return JsonUtil.returnJson(model);
	}
	
	/** 로그인 화면 */
	@RequestMapping(value = "/cm/login/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		return viewLogin(request, response, null, model);
	}
	
	/** 로그인 화면 */
	@RequestMapping(value = {"/cm/login/viewLogin", "/cm/login/viewLoginNoSSL"}, method = RequestMethod.GET)
	public String viewLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "lginImgId", required = false) String lginImgId,
			ModelMap model) throws Exception {
		
		// 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지.
		
		if(SysSetupUtil.isDemoSite()){
			model.put("demoSite", Boolean.TRUE);
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
		if("Y".equals(sysPlocMap.get("singleDomainEnable"))){
			String domain = request.getServerName();
			
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			String webDomain = svrEnvMap.get("webDomain");
			int p = webDomain.indexOf(':');
			String pureDomain = p<0 ? webDomain : webDomain.substring(0, p);
			
			// 서버의 도메인과 호출 도메인이 다르면
			if(webDomain!=null && !webDomain.isEmpty() && !pureDomain.equals(domain)){
				response.sendRedirect((useSSL || request.isSecure() ? "https://" : "http://")+webDomain+"/cm/login/viewLogin.do");
				return null;
			}
		}
		// SSL 사용 옵션에 - https 가 아닌 경우
		if(lginImgId==null && useSSL && !request.isSecure() && request.getServerPort() == 80){
			if(request.getRequestURI().indexOf("viewLoginNoSSL")<0){
				response.sendRedirect("https://"+request.getServerName()+"/cm/login/viewLogin.do");
				return null;
			}
		}
		
		// 비밀번호 찾기
		boolean lostPwEnable = false;
		if("Y".equals(sysPlocMap.get("lostPwEnable"))){
			lostPwEnable = true;
			model.put("lostPwEnable", Boolean.TRUE);
		}
		
		//IP 정책 체크 - 외부 로그인 가능한 사용자 체크
		String ip = ipChecker.getIp(request);
		// 로그인 IP 정책 이 있고 로그인 할 수 있는 사용자는 제외
		if( !(ipChecker.isInternalIpOnlyPloc() && ipChecker.isInternalIpRange(ip)) ){
			// 정책에 따른 로그인 화면 딜레이 적용 - 중국계IP 또는, 외국 IP
			boolean requestOk = foreignIpBlocker.delayViewLogin(request);
			if(!requestOk){
				// pt.login.title=로그인
				String title = messageProperties.getMessage("pt.login.title", request);
				// pt.secu.tryLater=잠시 후 다시 시도해 주시기 바랍니다.
				return gotoLogin("pt.secu.tryLater", title, model);
			}
		}
		
		Map<String, String> imageMap = null;
		// 미리보기용
		if(lginImgId != null && !lginImgId.isEmpty()){
			PtLginImgDVo ptLginImgDVo = new PtLginImgDVo();
			ptLginImgDVo.setLginImgId(lginImgId);
			ptLginImgDVo = (PtLginImgDVo)commonSvc.queryVo(ptLginImgDVo);
			// 디폴트 설정이 있으면 - 첫번째 설정 리턴(마지막 등록한 것)
			if(ptLginImgDVo != null){
				imageMap = new HashMap<String, String>();
				if(ptLginImgDVo.getLogoImgPath() != null){
					imageMap.put("logoImgPath", ptLginImgDVo.getLogoImgPath());
				}
				imageMap.put("bgImgPath", ptLginImgDVo.getBgImgPath());
				imageMap.put("topPx", ptLginImgDVo.getTopPx());
				imageMap.put("leftPx", ptLginImgDVo.getLeftPx());
			}
		}
		
		// 로그인 페이지 - 배경 이미지 세팅
		if(imageMap==null) imageMap = ptSysSvc.getLoginImage();
		model.put("imageMap", imageMap);
		
		// 사용중인 언어 설정 조회
		List<PtCdBVo> langTypPtCdBVoList = ptCmSvc.getCdList("LANG_TYP_CD", "ko", "Y");
		model.put("langTypPtCdBVoList", langTypPtCdBVoList);
		
		// 암호화된 쿠키값(secuId)을 읽어서 복호화하여 로그인 아이디로 세팅함
		String encLogin = getCookieValue(request.getCookies(), "encLogin");
		String langTypCd = null;
		if(encLogin!=null){
			try{
				String decLogin = cryptoSvc.decryptCookie(encLogin);
				JSONObject jsonObject = (JSONObject)JSONValue.parse(decLogin);
				model.put("loginId", jsonObject.get("id"));
				
				langTypCd = (String)jsonObject.get("lc");
				if(langTypCd!=null && !langTypCd.isEmpty()){
					model.put("langTypCd", langTypCd);
					
					if(lostPwEnable){
						Locale locale = SessionUtil.toLocale(langTypCd);
						request.getSession().setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", locale);
					}
				}
			} catch(Exception ignore){}
		}
		
		String secuSessionCode = StringUtil.getNextHexa();
		model.put("secuSessionCode", secuSessionCode);
		HttpSession session = request.getSession(true);
		session.setAttribute("secuSessionCode", secuSessionCode);
		
		String message = (String)session.getAttribute("loginMessage");
		if(message != null && !message.isEmpty()){
			request.setAttribute("message", message);
			session.removeAttribute("loginMessage");
		}
		
		// System halt 처리 (시스템 중단)
		UserVo userVo = LoginSession.getUser(request);
		String userUid = userVo==null ? null : userVo.getUserUid();
		langTypCd = userVo!=null ? userVo.getLangTypCd() : 
			langTypCd!=null ? langTypCd :
				contextProperties.getProperty("login.default.lang", "ko");
		// 시스템 차단 메세지 조회 - 차단중일 경우만 메세지 리턴함
		String haltMsg = ptSysSvc.getHaltMsg(userUid, langTypCd);
		if(haltMsg != null){
			request.setAttribute("message", haltMsg);
		}
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		return "/cm/login/viewLogin";
	}
	
	/** 로그인 화면 */
	@RequestMapping(value = "/cm/login/viewSsoLogin", method = RequestMethod.GET)
	public String viewSsoLogin(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		
		// 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지.
		
		String secuSessionCode = StringUtil.getNextHexa();
		model.put("secuSessionCode", secuSessionCode);
		HttpSession session = request.getSession(true);
		session.setAttribute("secuSessionCode", secuSessionCode);
		
		// RSA 암호화 스크립트 추가
		model.put("JS_OPTS", new String[]{"pt.rsa"});
		return "/cm/login/viewSsoLogin";
	}
	/** 쿠키값 리턴 */
	private String getCookieValue(Cookie[] cookies, String name){
		if(cookies==null) return null;
		for(Cookie cookie : cookies){
			if(cookie.getName().equals(name)) return cookie.getValue();
		}
		return null;
	}
	
	/** 로그인 처리 */
	@RequestMapping(value = "/cm/login/processLogin", method = RequestMethod.GET)
	public String processLogin(
			@RequestParam(value = "secu", required = true) String secu,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, ModelMap model) throws Exception {
		
		// 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지.
		
		// 실패 타이틀
		String failTitle = messageProperties.getMessage("pt.login.fail.title", request);
		
		JSONObject jsonObject = null;
		try {
			jsonObject = cryptoSvc.processRsa(request);
		} catch(CmException e){
			//해외 IP 로그인 Fail 응답 딜레이
			foreignIpBlocker.delayFailResponse(request);
			LOGGER.error("Login-Fail : "+e.getMessage());
			//pt.login.fail.decrypt=복호화에 실패하였습니다.
			return gotoLogin("pt.login.fail.decrypt", failTitle, model);
		}
		
		if(jsonObject==null){
			//pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
			return gotoLogin("pt.login.noUserNoPw", failTitle, model);
		}
		
		String lginId = (String)jsonObject.get("lginId");
		lginId = URLDecoder.decode(lginId, "UTF-8");
		String pw = (String)jsonObject.get("pw");
		String saveId = (String)jsonObject.get("saveId");
		String langTypCd = (String)jsonObject.get("langTypCd");
		String token = (String)jsonObject.get("token");
		
		boolean demoLogin = "Y".equals(jsonObject.get("demoLogin"));
		String demoUserUid = (String)jsonObject.get("userUid");
		
		// 언어 설정 세션에 저장 - 실제 사용안됨
		if(langTypCd==null || langTypCd.isEmpty()){
			langTypCd = contextProperties.getProperty("login.default.lang", "ko");
		}
		Locale locale = SessionUtil.toLocale(langTypCd);
		session.setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", locale);
		
		OrOdurBVo orOdurBVo = null;
		
		// 데모용 사이트를 제외한 데모로그인 제거
		if(demoLogin){
			if(!SysSetupUtil.isDemoSite() || demoUserUid==null || demoUserUid.isEmpty()){
				// pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				String message = messageProperties.getMessage("pt.login.noUserNoPw", locale);
				model.addAttribute("message", message);
				model.addAttribute("togo", PtConstant.URL_LOGIN);
				return LayoutUtil.getResultJsp();
			}
			
			// 원직자기본(OR_ODUR_B) 테이블 - 로그인 아이디로 사용자 정보 조회
			orOdurBVo = new OrOdurBVo();
			orOdurBVo.setUserUid(demoUserUid);
			orOdurBVo.setQueryLang(langTypCd);
			orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
			
		} else {
			// ID저장 - 암호화된 ID를 쿠키에 저장
			setLoginIdCookie(lginId, langTypCd, saveId, response);
			if(PtConstant.ADMIN_ID.equals(lginId)){
				try {
					String togo = ptLoginSecuSvc.processAdminLogin(request, session, pw, locale);
					model.addAttribute("togo", togo);
				} catch(Exception e){
					//e.printStackTrace();
					String message = e.getMessage();
					model.addAttribute("message", message);
					model.addAttribute("togo", PtConstant.URL_LOGIN);
				}
				model.put("UI_TITLE", "Longin Process");
				return LayoutUtil.getResultJsp();
			}
			
			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			// 사번 로그인
			if("Y".equals(sysPlocMap.get("einLginEnable"))){
				orOdurBVo = new OrOdurBVo();
				orOdurBVo.setEin(lginId);
				orOdurBVo.setQueryLang(langTypCd);
				@SuppressWarnings("unchecked")
				List<OrOdurBVo> orOdurBVoList = (List<OrOdurBVo>)commonSvc.queryList(orOdurBVo);
				
				if(orOdurBVoList==null || orOdurBVoList.size()==0){
					orOdurBVo = null;
				} else if(orOdurBVoList.size()==1){
					orOdurBVo = orOdurBVoList.get(0);
				} else {
					orOdurBVo = null;
					LOGGER.error("Login-Fail : Dup Ein : "+lginId+ "  count:"+orOdurBVoList.size());
				}
			// ID 로그인
			} else {
				// 원직자기본(OR_ODUR_B) 테이블 - 로그인 아이디로 사용자 정보 조회
				orOdurBVo = new OrOdurBVo();
				orOdurBVo.setLginId(lginId);
				orOdurBVo.setQueryLang(langTypCd);
				orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
			}
		}
		
		if(orOdurBVo==null){
			// 해외 IP 로그인 Fail 응답 딜레이
			foreignIpBlocker.delayFailResponse(request);
			LOGGER.error("Login-Fail : no user(OR_ODUR_B) : "+lginId);
			//pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
			return gotoLogin("pt.login.noUserNoPw", failTitle, model);
		}
		// 원직자UID
		String odurUid = orOdurBVo.getOdurUid();
		String ip = ipChecker.getIp(request);
		
		if(!demoLogin){
			
			// 장금 여부 확인
			if(failLockManager.isLocked(odurUid, ip)){
				//pt.login.denialOfSecu=보안상의 이유로 로그인 할 수 없습니다.
				return gotoLogin("pt.login.denialOfSecu", failTitle, model);
			}
			
			// 로그인 패스워드 암호화
			String encPw = License.ins.encryptPw(pw, odurUid);
			
			// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - 조회
			OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
			orUserPwDVo.setOdurUid(odurUid);
			orUserPwDVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
			orUserPwDVo = (OrUserPwDVo)commonSvc.queryVo(orUserPwDVo);
			if(orUserPwDVo==null){
				//해외 IP 로그인 Fail 응답 딜레이
				foreignIpBlocker.delayFailResponse(request);
				LOGGER.error("Login-Fail : no password(OR_USER_PW_D) - loginId:"+lginId+"  odurUid:"+odurUid);
				//pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				return gotoLogin("pt.login.noUserNoPw", failTitle, model);
			}
			// 패스워드 비교
			if(encPw==null || !encPw.equals(orUserPwDVo.getPwEnc())){
				//해외 IP 로그인 Fail 응답 딜레이
				foreignIpBlocker.delayFailResponse(request);
				LOGGER.error("Login-Fail : password not matched - loginId:"+lginId+"  odurUid:"+odurUid);
				// 비빌번호 - 실패 회수 늘리기
				failLockManager.addFail(odurUid, ip);
				
				//pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				return gotoLogin("pt.login.noUserNoPw", failTitle, model);
			}
		}
		
		// 사용자상태코드 체크 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 99:삭제
		if(!"02".equals(orOdurBVo.getUserStatCd())){
			//해외 IP 로그인 Fail 응답 딜레이
			foreignIpBlocker.delayFailResponse(request);
			//pt.login.notAllowedStat=로그인 가능 사용자가 아닙니다.
			LOGGER.error("Login-Fail : user stat(not 02) - loginId:"+lginId+"  odurUid:"+odurUid+"  userStatCd:"+orOdurBVo.getUserStatCd());
			return gotoLogin("pt.login.notAllowedStat", failTitle, model);
		}
		
		// 사용자 세션 정보 생성
		String togo = ptLoginSvc.createUserSessionByOdurUid(odurUid, null, request, response, orOdurBVo);
		
		// 로그인 후 이동 페이지 설정
		model.addAttribute("togo", togo);
		
		UserVo userVo = LoginSession.getUser(request);
		// 시스템 차단 조회
		if(ptSysSvc.denyUseOfHaltedSystem(userVo.getUserUid())){
			return gotoLogin(null, "SYSTEM HALT", model);
		}
		
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		// 인증서 만료시 처리
		if(!"U0000001".equals(userVo.getUserUid())){
			boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
			if(useSSL && !request.isSecure()){
				session.invalidate();
				// 웹 도메인
				Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
				String webDomain = svrEnvMap.get("webDomain");
				response.sendRedirect("https://"+webDomain+"/cm/login/viewLogin.do");
				return null;
			}
		}
		
		// 로그인 팝업 조회
		if(userVo != null){
			
			BaBullPopupDispDVo baBullPopupDispDVo = new BaBullPopupDispDVo();
			baBullPopupDispDVo.setUserUid(userVo.getUserUid());
			baBullPopupDispDVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaBullPopupDDao.selectLoginBaBullPopupD");
			@SuppressWarnings("unchecked")
			List<BaBullPopupDVo> baBullPopupDVoList = (List<BaBullPopupDVo>)commonSvc.queryList(baBullPopupDispDVo);
			PtMnuDVo ptMnuDVo;
			String menuId, compId = userVo.getCompId();
			if(baBullPopupDVoList != null && !baBullPopupDVoList.isEmpty()){
				List<String[]> loginPops = new ArrayList<String[]>();
				// 게시판관리(BA_BRD_B) 테이블 - SELECT
				BaBrdBVo baBrdBVo = null;
				// 게시물(BB_X000X_L) 테이블 - SELECT
				BbBullLVo bbBullLVo = null;
				
				for(BaBullPopupDVo baBullPopupDVo : baBullPopupDVoList){
					if(!compId.equals(baBullPopupDVo.getCompId())) continue;
					
					// 게시판
					baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, baBullPopupDVo.getBrdId());
					if(baBrdBVo==null) continue;
					// 게시물
					bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, baBullPopupDVo.getBullId(), langTypCd);
					if(bbBullLVo==null || bbBullLVo.getBullStatCd()==null || !"B".equals(bbBullLVo.getBullStatCd())) continue;
					
					// menuId는 메뉴 생성시 변경 되므로 런타임에 다시 조회함
					menuId = ptSecuSvc.getSecuMenuId(userVo, "/bb/listBull.do?brdId="+baBullPopupDVo.getBrdId());
					ptMnuDVo = ptSecuSvc.getMenuByMenuId(compId, menuId, langTypCd);
					if(ptMnuDVo != null){
						loginPops.add(new String[]{ baBullPopupDVo.getBullId().toString(), baBullPopupDVo.getBrdId(), ptMnuDVo.getRescNm() });
					}
				}
				
				if(!loginPops.isEmpty()){
					request.getSession().setAttribute("loginPops", loginPops);
				}
			}
			
			// 설문 목록 팝업
			// menuId는 메뉴 생성시 변경 되므로 런타임에 다시 조회함
			menuId = ptSecuSvc.getSecuMenuId(userVo, "/wv/listSurv.do");
			ptMnuDVo = ptSecuSvc.getMenuByMenuId(compId, menuId, langTypCd);
			if(ptMnuDVo != null){
				List<String> survIdList =  wvSurvSvc.getPopSurvIdList(request, userVo);
				if(survIdList!=null){
					List<String[]> loginSurvPops = new ArrayList<String[]>();
					for(String survId : survIdList){
						loginSurvPops.add(new String[]{ survId, ptMnuDVo.getRescNm()});
					}
					if(!loginSurvPops.isEmpty()){
						request.getSession().setAttribute("loginSurvPops", loginSurvPops);
					}
				}
			}
			
		}
		
//		// IP 정책 체크 - 외부 로그인 가능한 사용자 체크
////		String ip = ipChecker.getIp(request);
//		boolean internalIp = ipChecker.isInternalIpRange(ip);// 내부망 IP 여부
//		userVo.setInternalIp(internalIp);
//		
		// 내부망 - 외부망 : 정책 체크
		if(ipChecker.isInternalIpOnlyPloc() && !userVo.isInternalIp()){// 내부망만 사용 & !내부망 IP
			if(!userVo.getLginIpExcliYn()){// 로그인IP제외대상여부
				request.getSession().invalidate();
				// pt.login.notAllowedIp=보안 정책에 의해 로그인이 차단 되었습니다.(로그인 불가능 IP)
				LOGGER.error("Login-Fail : IP Policy - loginId:"+lginId+"  odurUid:"+odurUid+"  ip:"+ip);
				return gotoLogin("pt.login.notAllowedIp", failTitle, model);
			} else {
				userVo.setExcliLginYn(true);//제외대상로그인여부
			}
		}
		
		// 국내 - 해외 - 중국 : 정책 체크
		if(!userVo.getLginIpExcliYn()){// 로그인IP제외대상여부
			boolean requestOk = foreignIpBlocker.isLoginNoBlockByLginId(request, lginId);
			if(!requestOk){
				//해외 IP 로그인 Fail 응답 딜레이
				try{ Thread.sleep(1000); }catch(Exception ignore){}
				request.getSession().invalidate();
				// pt.login.notAllowedIp=보안 정책에 의해 로그인이 차단 되었습니다.(로그인 불가능 IP)
				return gotoLogin("pt.login.notAllowedIp", failTitle, model);
			}
		}
		
//		// 권한 있는 모듈 참조 ID
//		userVo.setMnuGrpMdRids(ptSecuSvc.getAuthdMnuGrpByMdRids(userVo, PtConstant.AUTH_CHK_MNU_GRP_MD_RIDS));
//		userVo.setLginIp(ip);//로그인IP
		
		if(demoLogin){
			// 데모 로그인의 경우 - 관리자 권한 제거
			userVo.setAdminAuthGrpIds(new String[]{});
			userVo.setDemoLgin(Boolean.TRUE);
		}
		
		//////////////////////////////
		//
		// uagntToken
		// 메세지 로그인의 보안 보완용 브라우저별 토큰 : localStorage 에 저장 후 메세지 로그인에서 토큰 확인하여 메세지 로그인 여부 결정
		String uagnt = request.getHeader("User-Agent");
		if(uagnt==null) uagnt = "empty";
		String uagntHash = Integer.toString(CRC32.hash(uagnt.getBytes("UTF-8")));
		
		PtUserUagntDVo ptUserUagntDVo = new PtUserUagntDVo();
		ptUserUagntDVo.setOdurUid(odurUid);
		ptUserUagntDVo.setUagntHashVa(uagntHash);
		ptUserUagntDVo = (PtUserUagntDVo)commonSvc.queryVo(ptUserUagntDVo);
		if(ptUserUagntDVo == null || !(token!=null && token.equals(ptUserUagntDVo.getUagntTknVa()))){
			
			boolean needInsert = (ptUserUagntDVo == null);
			token = StringUtil.getNextHexa();
			
			ptUserUagntDVo = new PtUserUagntDVo();
			ptUserUagntDVo.setOdurUid(odurUid);
			ptUserUagntDVo.setUagntHashVa(uagntHash);
			ptUserUagntDVo.setUagntTknVa(token);
			
			if(needInsert){
				commonSvc.insert(ptUserUagntDVo);
			} else {
				commonSvc.update(ptUserUagntDVo);
			}
			model.put("uagntToken", token);
		}
		
		try {
			//사용자 로그인 이력 저장
			OrUserLginHVo orUserLginHVo = new OrUserLginHVo();
			orUserLginHVo.setUserUid(odurUid);
			orUserLginHVo.setSessionId(request.getSession().getId());
			orUserLginHVo.setAccsIp(ip);
			orUserLginHVo.setCompId(userVo.getCompId());
			if(demoLogin){
				orUserLginHVo.setDeviNm("DEMO");
			} else {
				orUserLginHVo.setDeviNm("PC");
			}
			String lginDt = StringUtil.getCurrDateTime();
			orUserLginHVo.setLginDt(lginDt);
			if(commonSvc.count(orUserLginHVo)==0) commonSvc.insert(orUserLginHVo);
		}catch(Exception e){
			LOGGER.error("user log insert error : "+e.getMessage());
		}
		model.put("UI_TITLE", "Longin Process");
		
		request.getSession().removeAttribute("secuSessionCode");
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 로그인ID / 언어설정 - 쿠키에 저장(15일) */
	private void setLoginIdCookie(String loginId, String langTypCd,
			String saveId, HttpServletResponse response) throws IOException, CmException{
		if("Y".equals(saveId)){
			Cookie cookie = new Cookie("encLogin", 
					cryptoSvc.encryptCookie("{\"id\":\""+loginId+"\",\"lc\":\""+langTypCd+"\"}"));
			cookie.setMaxAge(3600*24*15);
			response.addCookie(cookie);
		} else {
			Cookie cookie = new Cookie("encLogin", null);
			cookie.setMaxAge(-1);
			response.addCookie(cookie);
		}
	}
	
	/** 겸직 사용자 변경 */
	@RequestMapping(value = "/cm/login/processAdurSwitch", method = RequestMethod.GET)
	public String processAdurSwitch(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "userUid", required = false) String userUid,
			@RequestParam(value = "destination", required = false) String destination,
			HttpSession session, ModelMap model, Locale locale) throws Exception {
		
		// 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지.
		
		boolean confirmed = false;
		UserVo userVo = LoginSession.getUser(request);
		if(userVo!=null){
			
			// 관리자(admin)의 경우
			if(userVo.getUserUid().equals("U0000001")){
				if(request.getParameter("compId")!=null){
					ptLoginSecuSvc.processAdminLogin(request, session, null, locale);
					model.addAttribute("togo", "/");
					return LayoutUtil.getResultJsp();
				}
				
				if(userUid!=null){
					OrUserBVo orUserBVo = new OrUserBVo();
					orUserBVo.setUserUid(userUid);
					orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
					
					if(orUserBVo != null){
						request.setAttribute("BY_ADMIN", Boolean.TRUE);
						ptLoginSvc.createUserSessionByOdurUid(orUserBVo.getOdurUid(), userUid, request, response, null);
//						userVo = LoginSession.getUser(request);
//						if(userVo != null) userVo.setAdminSesn(Boolean.TRUE);
					}
				}
				model.addAttribute("togo", "/");
				return LayoutUtil.getResultJsp();
			}
			
			String[][] adurs = userVo.getAdurs();
			int i, size = (userUid==null || adurs==null) ? 0 : adurs.length;
			for(i=0;i<size;i++){
				if(userUid.equals(adurs[i][1])){
					confirmed = true;
					break;
				}
			}
		}
		
		String referer = request.getHeader("Referer");
		if(confirmed){
			
			// 데모 로그인 여부
			Boolean demoLgin = userVo.isDemoLgin();
			
			// 관리자 세션 유지
			if(userVo.isAdminSesn()) request.setAttribute("BY_ADMIN", Boolean.TRUE);
			// 제외대상로그인여부
			if(userVo.getExcliLginYn()){ request.setAttribute("EXCLI_LGIN", Boolean.TRUE); }
			// 사용자 세션 정보 생성
			String togo = ptLoginSvc.createUserSessionByOdurUid(userVo.getOdurUid(), userUid, request, response, null);
			
			userVo = LoginSession.getUser(request);
//			// IP 정책 체크 - 외부 로그인 가능한 사용자 체크
//			//String ip = ipChecker.getIp(request);
//			String ip = userVo.getLginIp();
//			boolean internalIp = ipChecker.isInternalIpRange(ip);
//			if(ipChecker.isInternalIpOnlyPloc() && !internalIp){
//				userVo.setExcliLginYn(true);//제외대상로그인여부
//			}
			
//			userVo.setLginIp(ip);// 로그인IP
//			userVo.setInternalIp(internalIp);// 내부망 IP 여부
			if(demoLgin!=null && Boolean.TRUE.equals(demoLgin)){
				// 데모 로그인의 경우 - 관리자 권한 제거
				userVo.setAdminAuthGrpIds(new String[]{});
				userVo.setDemoLgin(Boolean.TRUE);
			}
			
			if("waitBx".equals(destination)){
				togo = ptSecuSvc.toAuthMenuUrl(userVo, "/ap/box/listApvBx.do?bxId=waitBx");
				model.addAttribute("togo", togo);
			} else {
				// 겸직 로그인 후 - 이동 페이지 설정
				model.addAttribute("togo", togo==null ? PtConstant.URL_SET_INIT : togo);
//				String returnUrl = null;
//				PtMnuLoutDVo ptMnuLoutDVo = ptSecuSvc.getAuthedPtMnuLoutDVo(userVo, null, true);
//				if(ptMnuLoutDVo == null){
//					model.addAttribute("togo", "/");
//				} else if(ptMnuLoutDVo.isMyMnu()){
//					returnUrl = ptMyMnuSvc.getFirstMyPage(request, userVo);
//					model.addAttribute("togo", returnUrl);
//				} else {
//					returnUrl = ptSecuSvc.getUrlByPtMnuLoutDVo(userVo, ptMnuLoutDVo, false);
//					model.addAttribute("togo", returnUrl);
//				}
			}
			
//			// 권한 있는 모듈 참조 ID
//			userVo.setMnuGrpMdRids(ptSecuSvc.getAuthdMnuGrpByMdRids(userVo, PtConstant.AUTH_CHK_MNU_GRP_MD_RIDS));
			
			return LayoutUtil.getResultJsp();
		} else {
			// pt.login.noAdur=겸직자 정보를 확인 할 수 없습니다.
			model.addAttribute("messageCode", "pt.login.noAdur");
			model.addAttribute("togo", referer==null ? "/" : referer);
			return LayoutUtil.getResultJsp();
		}
	}
	
	/** 로그아웃 처리 */
	@RequestMapping(value = "/cm/login/processLogout", method = RequestMethod.GET)
	public String processLogout(
			HttpServletRequest request,  HttpServletResponse response,
			HttpSession session, ModelMap model) throws Exception {
		
		// 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지.
		
		try {
			UserVo userVo = LoginSession.getUser(request);
			if(userVo != null){
				//사용자 로그아웃 이력 저장
				OrUserLginHVo orUserLginHVo = new OrUserLginHVo();
				orUserLginHVo.setUserUid(userVo.getUserUid());
				orUserLginHVo.setSessionId(request.getSession().getId());
				orUserLginHVo.setCompId(userVo.getCompId());
				String lginDt = (String)request.getSession().getAttribute("userLginDt");
				orUserLginHVo.setLginDt(lginDt);
				String lgotDt = StringUtil.getCurrDateTime();
				orUserLginHVo.setLgotDt(lgotDt);
				commonSvc.update(orUserLginHVo);
			}
		}catch(Exception e){
			LOGGER.error("user log update error : "+e.getMessage());
		}
		
		session.invalidate();
		String title = messageProperties.getMessage("pt.logout.title", request);//pt.logout.title=로그아웃
		return gotoLogin(null, title, model);
	}
	
	/** 로그인 실패 처리 */
	private String gotoLogin(String messageCode, String title, ModelMap model){
		if(messageCode!=null) { model.addAttribute("messageCode", messageCode); }
		if(title!=null){ model.put("UI_TITLE", title); }
		model.addAttribute("togo", PtConstant.URL_LOGIN);
		return LayoutUtil.getResultJsp();
	}
	
	/** 비밀번호분실 */
	@RequestMapping(value = "/cm/login/setLostPwPop")
	public String setLostPwPop(HttpServletRequest request, HttpSession session, 
			@RequestParam(value = "langTypCd", required = false) String langTypCd, ModelMap model) throws Exception {
		if(langTypCd!=null && !langTypCd.isEmpty()){
			Locale locale = SessionUtil.toLocale(langTypCd);
			session.setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", locale);
		}
		return LayoutUtil.getJspPath("/cm/login/setLostPwPop");
	}
	
	/** [AJAX] 비밀번호분실 - 메일발송 */
	@RequestMapping(value = "/cm/login/transLostPwAjx")
	public String transLostPwAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		String lginId = (String)jsonObject.get("lginId");
		String extnEmail = (String)jsonObject.get("extnEmail");
		String langTypCd = (String)jsonObject.get("langTypCd");
		
		try {
			Locale locale = SessionUtil.toLocale(langTypCd);
			
			// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
			String message = messageProperties.getMessage("pt.login.noUser", locale);
			
			OrOdurBVo orOdurBVo = new OrOdurBVo();
			orOdurBVo.setLginId(lginId);
			orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
			if(orOdurBVo==null){
				LOGGER.error("LOST PW - no OrOdurBVo - lginId:"+lginId);
				model.put("message", message);
				return LayoutUtil.returnJson(model);
			}
			String odurUid = orOdurBVo.getOdurUid();
			
			OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
			orUserPinfoDVo.setOdurUid(orOdurBVo.getOdurUid());
			orUserPinfoDVo = (OrUserPinfoDVo)commonSvc.queryVo(orUserPinfoDVo);
			if(orUserPinfoDVo==null){
				LOGGER.error("LOST PW - no OrUserPinfoDVo - lginId:"+lginId+"  odurUid:"+odurUid);
				model.put("message", message);
				return LayoutUtil.returnJson(model);
			}
			
			orCmSvc.decryptUserPinfo(orUserPinfoDVo);// 복호화
			if(orUserPinfoDVo.getExtnEmail()==null || !extnEmail.equals(orUserPinfoDVo.getExtnEmail())){
				LOGGER.error("LOST PW - extnEmail - lginId:"+lginId+"  odurUid:"+odurUid+"  submited:"+extnEmail+"  stored:"+orUserPinfoDVo.getExtnEmail());
				model.put("message", message);
				return LayoutUtil.returnJson(model);
			}
			
			if(!"02".equals(orOdurBVo.getUserStatCd())){
				LOGGER.error("LOST PW - no OrOdurBVo - lginId:"+lginId);
				//pt.login.notAllowedStat=로그인 가능 사용자가 아닙니다.
				message = messageProperties.getMessage("pt.login.notAllowedStat", locale);
				model.put("message", message);
				return LayoutUtil.returnJson(model);
			}
			
			// 비밀번호 찾기 - 발송자 정보 조회
			Map<String, String> lostPwPolc = ptSysSvc.getLostPwPolc();
			String senderName  = lostPwPolc.get("senderName");
			String senderEmail = lostPwPolc.get("senderEmail");
			if(senderName==null || senderName.isEmpty() || senderEmail==null || senderEmail.isEmpty()){
				// pt.lostPw.noSender=메일 발송자가 설정되지 않았습니다.
				message = messageProperties.getMessage("pt.login.noUser", locale);
				LOGGER.error("LOST PW - no mail sender info.");
				model.put("message", message);
				return LayoutUtil.returnJson(model);
			}
			
			String lostId = StringUtil.getNextHexa(32);
			
			OrUserPwLostDVo orUserPwLostDVo = new OrUserPwLostDVo();
			orUserPwLostDVo.setOdurUid(odurUid);
			commonSvc.delete(orUserPwLostDVo);
			
			orUserPwLostDVo.setLostId(lostId);
			orUserPwLostDVo.setOdurUid(odurUid);
			orUserPwLostDVo.setLangTypCd(langTypCd);
			orUserPwLostDVo.setRegDt("sysdate");
			commonSvc.insert(orUserPwLostDVo);
			
			Map<String, String> termMap = ptSysSvc.getTermMap("or.term", langTypCd);
			String siteName = termMap.get("siteName");
			
			// 웹 도메인
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			String domain = svrEnvMap.get("webDomain");
			
//			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
//			boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
			
//			pt.lostPw.mailSent=메일이 발송되었습니다.
//			pt.lostPw.mailSubj={0} 비밀번호 변경
//			pt.lostPw.mailCont1=비빌번호 변경을 위해 링크를 클릭 하십시요.
//			pt.lostPw.mailCont2=해당 링크는 12시간 유효 합니다.
			String mailCont1 = messageProperties.getMessage("pt.lostPw.mailCont1", locale);
			String mailCont2 = messageProperties.getMessage("pt.lostPw.mailCont2", new String[]{Integer.toString(LOST_PW_VALID_HOUR)}, locale);
			
			String subject = siteName==null || siteName.isEmpty() ? mailCont1 : messageProperties.getMessage("pt.lostPw.mailSubj", new String[]{siteName}, locale);
			//String content = "<div><a href=\""+(useSSL ? "https://" : "http://")+domain+"/cm/login/lostPw.do?lostPwId="+lostId+"\" target=\"_blank\">"+mailCont1+"</a><br/><br/>"+mailCont2+"</div>";
			String content = "<div><a href=\""+"http://"+domain+"/cm/login/lostPw.do?lostPwId="+lostId+"\" target=\"_blank\">"+mailCont1+"</a><br/><br/>"+mailCont2+"</div>";
			
			emailSvc.sendMailSvc3(senderName, senderEmail, new String[]{extnEmail}, subject, content,
					null, false, false);
			
			message = messageProperties.getMessage("pt.lostPw.mailSent", locale);
			model.put("message", message);
			model.put("result", "ok");
			
		} catch(Exception e){
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	/** 비밀번호분실 -  */
	@RequestMapping(value = "/cm/login/lostPw")
	public String lostPw(HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			@RequestParam(value = "lostPwId", required = false) String lostPwId,
			ModelMap model) throws Exception {
		
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
		if(useSSL && !request.isSecure() && request.getServerPort()==80){
			response.sendRedirect("https://"+request.getServerName()+"/cm/login/lostPw.do?lostPwId="+lostPwId);
			return null;
		}
		
		//pt.jsp.lostPw=비밀번호 찾기
		String title = messageProperties.getMessage("pt.jsp.lostPw", request);
		
		try {
			
			boolean valid = true;
			if(lostPwId==null || lostPwId.isEmpty()){
				valid = false;
				LOGGER.error("LOST PW BY ID - no lostPwId");
			}
			
			OrUserPwLostDVo orUserPwLostDVo = null;
			if(valid){
				orUserPwLostDVo = new OrUserPwLostDVo();
				orUserPwLostDVo.setLostId(lostPwId);
				orUserPwLostDVo = (OrUserPwLostDVo)commonSvc.queryVo(orUserPwLostDVo);
				if(orUserPwLostDVo == null) {
					valid = false;
					LOGGER.error("LOST PW BY ID - no OrUserPwLostDVo - lostId:"+lostPwId);
				}
			}
			
			String langTypCd = orUserPwLostDVo.getLangTypCd();
			if(langTypCd!=null && !langTypCd.isEmpty()){
				Locale locale = SessionUtil.toLocale(langTypCd);
				session.setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", locale);
			}
			
			if(valid){
				if(StringUtil.isAfter(orUserPwLostDVo.getRegDt(), 0, LOST_PW_VALID_HOUR, 0)){
					valid = false;
					LOGGER.error("LOST PW BY ID - after valid hour("+LOST_PW_VALID_HOUR+") - lostId:"+lostPwId
							+ "  stored:"+orUserPwLostDVo.getRegDt() + "  current:"+StringUtil.getCurrDateTime());
				}
			}
			
			if(!valid){
				//pt.lostPw.invalid=링크가 유효하지 않습니다.
				return gotoLogin("pt.lostPw.invalid", title, model);
			}
			
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setUserUid(orUserPwLostDVo.getOdurUid());
			orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
			if(orUserBVo==null){
				LOGGER.error("LOST PW BY ID - no OrUserBVo - lostId:"+lostPwId+"  userUid:"+orUserPwLostDVo.getOdurUid());
				//pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
				return gotoLogin("pt.login.noUser", title, model);
			}
			
			// RSA 암호화 스크립트 추가
			model.put("JS_OPTS", new String[]{"pt.rsa"});
			
			String secuSessionCode = StringUtil.getNextHexa();
			model.put("secuSessionCode", secuSessionCode);
			session.setAttribute("secuSessionCode", secuSessionCode);

			// 패스워드 정책 조회
			Map<String, String> pwPolc = ptSysSvc.getPwPolc(orUserBVo.getCompId());
			model.put("pwPolc", pwPolc);
			
			return "/cm/login/lostPw";
			
		}catch(Exception e){
			LOGGER.error("user log update error : "+e.getMessage());
		}
		
		//pt.lostPw.invalid=링크가 유효하지 않습니다.
		return gotoLogin("pt.lostPw.invalid", title, model);
	}
	
	/** 로그인 처리 */
	@RequestMapping(value = "/cm/login/transLostPw", method = RequestMethod.GET)
	public String transLostPw(
			@RequestParam(value = "secu", required = true) String secu,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, ModelMap model) throws Exception {
		
		//pt.jsp.lostPw=비밀번호 찾기
		String title = messageProperties.getMessage("pt.jsp.lostPw", request);
		
		JSONObject jsonObject = null;
		try {
			jsonObject = cryptoSvc.processRsa(request);
		} catch(CmException e){
			//해외 IP 로그인 Fail 응답 딜레이
			foreignIpBlocker.delayFailResponse(request);
			LOGGER.error("Login-Fail : "+e.getMessage());
			//pt.login.fail.decrypt=복호화에 실패하였습니다.
			return gotoLogin("pt.login.fail.decrypt", title, model);
		}
		
		String newPw = (String)jsonObject.get("newPw1");
		String lostPwId = (String)jsonObject.get("lostPwId");
		
		// 사용자비밀번호분실상세(OR_USER_PW_LOST_D)
		OrUserPwLostDVo orUserPwLostDVo = new OrUserPwLostDVo();
		orUserPwLostDVo.setLostId(lostPwId);
		orUserPwLostDVo = (OrUserPwLostDVo)commonSvc.queryVo(orUserPwLostDVo);
		if(orUserPwLostDVo == null) {
			LOGGER.error("LOST PW BY ID - no OrUserPwLostDVo - lostId:"+lostPwId);
			//pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
			return gotoLogin("pt.login.noUser", title, model);
		}
		String odurUid = orUserPwLostDVo.getOdurUid();
		String langTypCd = orUserPwLostDVo.getLangTypCd();
		Locale locale = SessionUtil.toLocale(langTypCd);
		
		// 사용자기본(OR_USER_B)
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(orUserPwLostDVo.getOdurUid());
		orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
		if(orUserBVo==null){
			LOGGER.error("LOST PW BY ID - no OrUserBVo - lostId:"+lostPwId+"  userUid:"+orUserPwLostDVo.getOdurUid());
			//pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
			return gotoLogin("pt.login.noUser", title, model);
		}
		String compId = orUserBVo.getCompId();
		
		try {
			ptPsnSvc.changePw(odurUid, odurUid, null, newPw, compId, "LOST PW", locale);
			
			// lostPwId 삭제
			orUserPwLostDVo = new OrUserPwLostDVo();
			orUserPwLostDVo.setLostId(lostPwId);
			commonSvc.delete(orUserPwLostDVo);
			
			// cm.msg.modify.success=변경 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.modify.success", locale));
			model.addAttribute("togo", PtConstant.URL_LOGIN);
			return LayoutUtil.getResultJsp();
			
		} catch(Exception e){
			model.addAttribute("message", e.getMessage());
			
			if(lostPwId!=null && !lostPwId.isEmpty()){
				model.addAttribute("togo", "./lostPw.do?lostPwId="+lostPwId);
			} else {
				model.addAttribute("togo", PtConstant.URL_LOGIN);
			}
			return LayoutUtil.getResultJsp();
		}
	}
}
