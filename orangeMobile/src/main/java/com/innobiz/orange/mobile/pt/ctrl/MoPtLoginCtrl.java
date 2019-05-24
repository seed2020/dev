package com.innobiz.orange.mobile.pt.ctrl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.crypto.rsa.RsaKey;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserLginHVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.pt.ip.ForeignIpBlocker;
import com.innobiz.orange.web.pt.secu.CRC32;
import com.innobiz.orange.web.pt.secu.IpChecker;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuCookie;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoginSecuSvc;
import com.innobiz.orange.web.pt.svc.PtLoginSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtPushMsgDVo;
import com.innobiz.orange.web.pt.vo.PtUserUagntDVo;

/** 로그인 컨트롤러 */
@Controller
public class MoPtLoginCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoPtLoginCtrl.class);
	
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
	
//	/** 포털 보안 서비스 */
//	@Autowired
//	private PtSecuSvc ptSecuSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
//	/** 결재 함 서비스 */
//	@Autowired
//	private ApBxSvc apBxSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
	@Autowired
	private IpChecker ipChecker;
	
//	/** 사용자 개인 설정 서비스 */
//	@Autowired
//	private PtMyMnuSvc ptMyMnuSvc;

	/** 중국, 해외 IP 차단 정책에 따른 요청에 차단 및 딜레이 적용 */
	@Autowired
	private ForeignIpBlocker foreignIpBlocker;
	
	/** [AJAX] 시큐어 세션 생성 : RSA키를 세션에 저장하고, 공개키를 Json으로 브라우저에 전달함 */
	@RequestMapping(value = "/cm/login/createSecuSessionAjx", method = RequestMethod.GET)
	public String createSecuSession(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "secuSessionCode", required = false) String secuSessionCode,
			ModelMap model){
		
		HttpSession session = request.getSession(false);
		if(session==null){
			response.setStatus(403);
			return null;
		}
		
		UserVo userVo = LoginSession.getUser(request);
		if(userVo == null){
			// 파라미터 시큐코드 체크
			if(secuSessionCode==null || secuSessionCode.isEmpty()){
				response.setStatus(400);
				return null;
			}
			// 세션 시큐코드와 비교
			// 중복 호출 오류 방지 - 각종 캐쉬 때문에
			@SuppressWarnings("unchecked")
			List<String> secuSessionCodeList = (List<String>)session.getAttribute("secuSessionCodeList");
			session.removeAttribute("secuSessionCodeList");
			if(secuSessionCodeList==null || !secuSessionCodeList.contains(secuSessionCode)){
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
	public String index(HttpServletRequest request, ModelMap model, Locale locale) throws Exception {
		return viewLogin(request, null, model, locale);
	}
	
	/** 로그인 화면 */
	@RequestMapping(value = "/cm/login/viewLogin", method = RequestMethod.GET)
	public String viewLogin(HttpServletRequest request,
			@RequestParam(value = "lginImgId", required = false) String lginImgId,
			ModelMap model, Locale locale) throws Exception {
		
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
		
		model.put("JS_OPTS", new String[]{"login"});
		
		// 로그인 페이지 - 배경 이미지 세팅
		Map<String, String> mobLginMap = ptSysSvc.getMobileLogin(locale.getLanguage());
		model.put("mobLginMap", mobLginMap);
		
		// 사용중인 언어 설정 조회
		List<PtCdBVo> langTypPtCdBVoList = ptCmSvc.getCdList("LANG_TYP_CD", "ko", "Y");
		model.put("langTypPtCdBVoList", langTypPtCdBVoList);
		
		// 중복 호출 오류 방지 - 각종 캐쉬 때문에
		HttpSession session = request.getSession(true);
		@SuppressWarnings("unchecked")
		List<String> secuSessionCodeList = (List<String>)session.getAttribute("secuSessionCodeList");
		if(secuSessionCodeList==null){
			secuSessionCodeList = new ArrayList<String>();
			session.setAttribute("secuSessionCodeList", secuSessionCodeList);
		}
		if(secuSessionCodeList.size()>3){
			secuSessionCodeList.remove(0);
		}
		
		String secuSessionCode = StringUtil.getNextHexa();
		secuSessionCodeList.add(secuSessionCode);
		model.put("secuSessionCode", secuSessionCode);
		
		return MoLayoutUtil.getJspPath("/cm/login/viewLogin", "empty");
	}
	
	/** 로그인 처리 */
	@RequestMapping(value = "/cm/login/processLogin", method = RequestMethod.GET)
	public String processLogin(
			@RequestParam(value = "secu", required = true) String secu,
			@RequestParam(value = "msgId", required = false) String msgId,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, ModelMap model) throws Exception {
		
		// 실패 타이틀
		String failTitle = messageProperties.getMessage("pt.login.fail.title", request);
		
		JSONObject jsonObject = null;
		try {
			jsonObject = cryptoSvc.processRsa(request);
		} catch(CmException e){
			// 해외 IP 로그인 Fail 응답 딜레이
			foreignIpBlocker.delayFailResponse(request);
			LOGGER.error("Login-Fail : "+e.getMessage());
			//pt.login.fail.decrypt=복호화에 실패하였습니다.
			return gotoLogin("pt.login.fail.decrypt", failTitle, model);
		}
		
		String lginId = (String)jsonObject.get("lginId");
		String pw = (String)jsonObject.get("pw");
		//String saveId = (String)jsonObject.get("saveId");
		String langTypCd = (String)jsonObject.get("langTypCd");
		String time = (String)jsonObject.get("time");
		String token = (String)jsonObject.get("token");
		
		Long createTime = Long.parseLong(time);
		
		// 언어 설정 세션에 저장 - 실제 사용안됨
		if(langTypCd==null || langTypCd.isEmpty()){
			langTypCd = contextProperties.getProperty("login.default.lang", "ko");
		}
		Locale locale = SessionUtil.toLocale(langTypCd);
		session.setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", locale);
		
		if(PtConstant.ADMIN_ID.equals(lginId)){
			try {
				ptLoginSecuSvc.processAdminLogin(request, session, pw, locale);
//				String togo = ptLoginSecuSvc.processAdminLogin(request, session, pw, locale);
//				model.addAttribute("togo", togo);
			} catch(Exception e){
				// 해외 IP 로그인 Fail 응답 딜레이
				foreignIpBlocker.delayFailResponse(request);
				String message = e.getMessage();
				// pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
				if(message==null) message = messageProperties.getMessage("pt.login.noUserNoPw", locale);
				model.addAttribute("message", message);
				model.addAttribute("togo", PtConstant.URL_LOGIN);
				return LayoutUtil.getResultJsp();
			}
			
			model.put("UI_TITLE", "Longin Process");
			request.getSession().removeAttribute("secuSessionCode");
			
			RsaKey rsaKey = cryptoSvc.getNextCookieKey();
			SecuCookie secuCookie = new SecuCookie(rsaKey, createTime);
			UserVo userVo = LoginSession.getUser(request);
			userVo.setSecuCookie(secuCookie);
			model.put("secuE", rsaKey.getPublicKey());
			model.put("secuM", rsaKey.getModulus());
			model.put("secuX", secuCookie.getSecuCode());
			
			// 로그인 직후 라고 세션에 세팅
			session.setAttribute("afterLoginForAutoMenu", Boolean.TRUE);
			
			return MoLayoutUtil.getJspPath("/cm/login/processLogin");
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		OrOdurBVo orOdurBVo = null;
		
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
		
		if(orOdurBVo==null){
			// 해외 IP 로그인 Fail 응답 딜레이
			foreignIpBlocker.delayFailResponse(request);
			LOGGER.error("Login-Fail : no user(OR_ODUR_B) : "+lginId);
			//pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
			return gotoLogin("pt.login.noUserNoPw", failTitle, model);
		}
		
		// 원직자 보안 정보
		Map<String, String> odurSecuMap = orCmSvc.getOdurSecuMap(orOdurBVo.getOdurUid());
		
		if("N".equals(odurSecuMap.get("useMobYn"))){//useMobYn:모바일사용여부
			//pt.login.notAllowedStat=로그인 가능 사용자가 아닙니다.
			LOGGER.error("Login-Fail : mobUseYn:N - loginId:"+lginId+"  odurUid:"+orOdurBVo.getOdurUid());
			return gotoLogin("pt.login.notAllowedStat", failTitle, model);
		}
		
		// 원직자UID
		String odurUid = orOdurBVo.getOdurUid();
		// 로그인 패스워드 암호화
		String encPw = License.ins.encryptPw(pw, odurUid);
		
		// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - 조회
		OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
		orUserPwDVo.setOdurUid(odurUid);
		orUserPwDVo.setPwTypCd("SYS");//SYS:시스템 비밀번호, APV:결재 비밀번호
		orUserPwDVo = (OrUserPwDVo)commonSvc.queryVo(orUserPwDVo);
		if(orUserPwDVo==null){
			// 해외 IP 로그인 Fail 응답 딜레이
			foreignIpBlocker.delayFailResponse(request);
			LOGGER.error("Login-Fail : no password(OR_USER_PW_D) - loginId:"+lginId+"  odurUid:"+odurUid);
			//pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
			return gotoLogin("pt.login.noUserNoPw", failTitle, model);
		}
		// 패스워드 비교
		if(encPw==null || !encPw.equals(orUserPwDVo.getPwEnc())){
			// 해외 IP 로그인 Fail 응답 딜레이
			foreignIpBlocker.delayFailResponse(request);
			LOGGER.error("Login-Fail : password not matched - loginId:"+lginId+"  odurUid:"+odurUid);
			//pt.login.noUserNoPw=비밀번호가 다르거나 사용자 정보를 확인 할 수 없습니다.
			return gotoLogin("pt.login.noUserNoPw", failTitle, model);
		}
		
		// 사용자상태코드 체크 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 99:삭제
		if(!"02".equals(orOdurBVo.getUserStatCd())){
			// 해외 IP 로그인 Fail 응답 딜레이
			foreignIpBlocker.delayFailResponse(request);
			//pt.login.notAllowedStat=로그인 가능 사용자가 아닙니다.
			LOGGER.error("Login-Fail : user stat(not 02) - loginId:"+lginId+"  odurUid:"+odurUid+"  userStatCd:"+orOdurBVo.getUserStatCd());
			return gotoLogin("pt.login.notAllowedStat", failTitle, model);
		}
		
		// 사용자 세션 정보 생성
		//String togo = ptLoginSvc.createUserSessionByOdurUid(odurUid, null, request, orOdurBVo);
		ptLoginSvc.createUserSessionByOdurUid(odurUid, null, request, response, orOdurBVo);
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 메세지 상세 조회
		PtPushMsgDVo ptPushMsgDVo = null;
		if(msgId!=null && !msgId.isEmpty()){
			ptPushMsgDVo = ptLoginSvc.getPushMsgDetl(msgId);
			if(ptPushMsgDVo!=null){
				model.put("ptPushMsgDVo", ptPushMsgDVo);
				request.getSession().setAttribute("ptPushMsgDVo", ptPushMsgDVo);
			}
		}
		
//		// IP 정책 체크 - 외부 로그인 가능한 사용자 체크
//		String ip = ipChecker.getIp(request);
//		
//		boolean internalIp = ipChecker.isInternalIpRange(ip);// 내부망 IP 여부
		boolean excliLginYn = userVo.getLginIpExcliYn();// 제외대상로그인여부
//		boolean internalIpOnlyPloc = ipChecker.isInternalIpOnlyPloc();// 내부망만 사용 정책
		
		// 제외대상자-가 아니고, 허용되는 IP설정으로 로그인 한 사용자가 아니면
		if(!excliLginYn){
			boolean requestOk = foreignIpBlocker.isLoginNoBlockByLginId(request, lginId);
			if(!requestOk){
				//해외 IP 로그인 Fail 응답 딜레이
				try{ Thread.sleep(1000); }catch(Exception ignore){}
				request.getSession().invalidate();
				// pt.login.notAllowedIp=IP 정책에 의해 로그인이 차단 되었습니다.
				LOGGER.error("Login-Fail : Foreign IP Policy - loginId:"+lginId+"  odurUid:"+odurUid+"  ip:"+userVo.getLginIp());
				return gotoLogin("pt.login.notAllowedIp", failTitle, model);
			}
		}
		
//		userVo.setLginIp(ip);//로그인IP
//		userVo.setInternalIp(true);//내부망 IP 여부
		
//		// 권한 있는 모듈 참조 ID
//		userVo.setMnuGrpMdRids(ptSecuSvc.getAuthdMnuGrpByMdRids(userVo, PtConstant.AUTH_CHK_MNU_GRP_MD_RIDS));
		
		//////////////////////////////
		//
		// uagntToken
		// 메세지 로그인의 보안 보완용 브라우저별 토큰 : localStorage 에 저장 후 메세지 로그인에서 토큰 확인하여 메세지 로그인 여부 결정
		String uagnt = request.getHeader("User-Agent");
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
			orUserLginHVo.setAccsIp(userVo.getLginIp());
			orUserLginHVo.setCompId(userVo.getCompId());
			String lginDt = getCurrTime();
			orUserLginHVo.setLginDt(lginDt);
			orUserLginHVo.setDeviNm("Mobile");
			commonSvc.insert(orUserLginHVo);
		}catch(Exception e){
			LOGGER.error("user log insert error : "+e.getMessage());
		}
		model.put("UI_TITLE", "Longin Process");
		request.getSession().removeAttribute("secuSessionCode");
		
		RsaKey rsaKey = cryptoSvc.getNextCookieKey();
		SecuCookie secuCookie = new SecuCookie(rsaKey, createTime);
		userVo.setSecuCookie(secuCookie);
		model.put("secuE", rsaKey.getPublicKey());
		model.put("secuM", rsaKey.getModulus());
		model.put("secuX", secuCookie.getSecuCode());
		
		// 로그인 직후 라고 세션에 세팅
		if(ptPushMsgDVo==null){
			session.setAttribute("afterLoginForAutoMenu", Boolean.TRUE);
		}
		
		return MoLayoutUtil.getJspPath("/cm/login/processLogin");
	}
	
	/** 겸직 사용자 변경 */
	@RequestMapping(value = "/cm/login/processAdurSwitch", method = RequestMethod.GET)
	public String processAdurSwitch(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "userUid", required = true) String userUid,
			@RequestParam(value = "destination", required = false) String destination,
			HttpSession session, Locale locale, ModelMap model) throws Exception {
		
		boolean confirmed = false;
		UserVo oldUserVo = LoginSession.getUser(request);
		
		if(oldUserVo!=null){
			
			if(oldUserVo.getUserUid().equals("U0000001")){
//				if(request.getParameter("compId")!=null){
//					ptLoginSecuSvc.processAdminLogin(request, session, null, locale);
//					return MoLayoutUtil.getJspPath("/cm/login/processAdurSwitch");
//				}
				
				if(userUid!=null){
					OrUserBVo orUserBVo = new OrUserBVo();
					orUserBVo.setUserUid(userUid);
					orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
					
					if(orUserBVo != null){
						request.setAttribute("BY_ADMIN", Boolean.TRUE);
						ptLoginSvc.createUserSessionByOdurUid(orUserBVo.getOdurUid(), userUid, request, response, null);
						
//						UserVo userVo = LoginSession.getUser(request);
//						String ip = ipChecker.getIp(request);
//						userVo.setLginIp(ip);
//						userVo.setInternalIp(true);
//						
//						SecuCookie secuCookie = oldUserVo.getSecuCookie();
//						userVo.setSecuCookie(secuCookie);
//						
//						// 권한 있는 모듈 참조 ID
//						userVo.setMnuGrpMdRids(ptSecuSvc.getAuthdMnuGrpByMdRids(userVo, PtConstant.AUTH_CHK_MNU_GRP_MD_RIDS));
					}
					return MoLayoutUtil.getJspPath("/cm/login/processAdurSwitch");
				}
			}
			
			String[][] adurs = oldUserVo==null ? null : oldUserVo.getAdurs();
			int i, size = (userUid==null || adurs==null) ? 0 : adurs.length;
			for(i=0;i<size;i++){
				if(userUid.equals(adurs[i][1])){
					confirmed = true;
					break;
				}
			}
		}
		
		if(confirmed){

			// 관리자 세션 유지
			if(oldUserVo.isAdminSesn()) request.setAttribute("BY_ADMIN", Boolean.TRUE);
			// 제외대상로그인여부
			if(oldUserVo.getExcliLginYn()){ request.setAttribute("EXCLI_LGIN", Boolean.TRUE); }
			// 사용자 세션 정보 생성
			ptLoginSvc.createUserSessionByOdurUid(oldUserVo.getOdurUid(), userUid, request, response, null);
			
//			UserVo userVo = LoginSession.getUser(request);
//			// IP 정책 체크 - 외부 로그인 가능한 사용자 체크
//			String ip = ipChecker.getIp(request);
////			if(!ipChecker.canLogin(ip)){
////				userVo.setExcliLginYn(true);//제외대상로그인여부
////			}
//			userVo.setLginIp(ip);//로그인IP
//			userVo.setInternalIp(true);
//			userVo.setSecuCookie(oldUserVo.getSecuCookie());
			
			// 겸직 결재 건수 클릭시 - 결재 대기함으로 이동하기 위한것
			if(destination!=null && !destination.isEmpty()){
				request.getSession().setAttribute("destination", destination);
			}
			
//			// 권한 있는 모듈 참조 ID
//			userVo.setMnuGrpMdRids(ptSecuSvc.getAuthdMnuGrpByMdRids(userVo, PtConstant.AUTH_CHK_MNU_GRP_MD_RIDS));
			
			/*
			// 겸직 로그인 후 - 이동 페이지 설정
			String returnUrl = null;
			PtMnuLoutDVo ptMnuLoutDVo = ptSecuSvc.getAuthedPtMnuLoutDVo(userVo, null, true);
			if(ptMnuLoutDVo == null){
				model.addAttribute("togo", "/");
			} else if(ptMnuLoutDVo.isMyMnu()){
				returnUrl = ptMyMnuSvc.getFirstMyPage(request, userVo);
				model.addAttribute("togo", returnUrl);
			} else {
				returnUrl = ptSecuSvc.getUrlByPtMnuLoutDVo(userVo, ptMnuLoutDVo, false);
				model.addAttribute("togo", returnUrl);
			}*/
			return MoLayoutUtil.getJspPath("/cm/login/processAdurSwitch");
		} else {
			// pt.login.noAdur=겸직자 정보를 확인 할 수 없습니다.
			model.put("message", messageProperties.getMessage("pt.login.noAdur", locale));
			return MoLayoutUtil.getJspPath("/cm/login/processAdurSwitch");
		}
	}
	
	/** 로그아웃 처리 */
	@RequestMapping(value = "/cm/login/processLogout", method = RequestMethod.GET)
	public String processLogout(
			HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {
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
				String lgotDt = getCurrTime();
				orUserLginHVo.setLgotDt(lgotDt);
				commonSvc.update(orUserLginHVo);
			}
		}catch(Exception e){
			LOGGER.error("user log update error : "+e.getMessage());
		}
		
		session.invalidate();
		String title = messageProperties.getMessage("pt.logout.title", request);//pt.logout.title=로그아웃
		model.put("UI_TITLE", title);
		
		return MoLayoutUtil.getJspPath("/cm/login/processLogout");
	}
	
	/** 오라클 여부 */
	private Boolean dmbsOracle = null;
	private Boolean dbmsMySql  = null;
	private String getCurrTime(){
		if(dmbsOracle==null){
			String dbms = contextProperties.getProperty("dbms");
			dmbsOracle = "oracle".equals(dbms) ? Boolean.TRUE : Boolean.FALSE;
			dbmsMySql  = "mysql".equals(dbms) ? Boolean.TRUE : Boolean.FALSE;
		}
		String currTime = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();
		if(dmbsOracle.booleanValue() || dbmsMySql.booleanValue()){
			int p = currTime.lastIndexOf('.');
			if(p>0){
				currTime = currTime.substring(0,p);
			}
		}
		return currTime;
	}
	
	/** 로그인 실패 처리 */
	private String gotoLogin(String messageCode, String title, ModelMap model){
		if(messageCode!=null) { model.addAttribute("messageCode", messageCode); }
		if(title!=null){ model.put("UI_TITLE", title); }
		model.put("todo", "if(parent.$m) parent.$m.nav.curr(null, '/cm/login/viewLogin.do'); else location.replace('/');");
		return MoLayoutUtil.getResultJsp();
	}
	
	
	/** 메세지 로그인 처리 */
	@RequestMapping(value = "/cm/login/processMsg", method = RequestMethod.GET)
	public String processMsg(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "secu", required = false) String secu,
			ModelMap model, Locale locale) throws Exception {
		
		String time = null;
		boolean isMsnLgin = false;
		
		// [msgId 로그인 2단계] - msgId 와 uagntToken 으로 로그인 시킴
		if(secu!=null && !secu.isEmpty()){
			
			// 2.1 복호화
			JSONObject jsonObject = null;
			try {
				jsonObject = cryptoSvc.processRsa(request);
			} catch(CmException e){
				LOGGER.error("Msg Login-Fail - fail decrypt : "+e.getMessage());
				// pt.login.fail.decrypt=복호화에 실패하였습니다.
				String message = messageProperties.getMessage("pt.login.fail.decrypt", request);
				return processMsgLoginFail(null, message, model);
			}
			
			// 2.2 복호화된 데이타에서 msgId, token 뽑아내기
			String msgId = (String)jsonObject.get("msgId");
			String token = (String)jsonObject.get("token");
			time = (String)jsonObject.get("time");
			
			// 메신저 로그인 여부 - 그룹웨어 클릭 했을때
			isMsnLgin = PtConstant.UCWARE_FOR_MSN.equals(msgId);
			// uagntToken 체크 생략
			boolean skipUagntTokenCheck = false;
			
			if(!isMsnLgin){
				if(msgId==null || msgId.isEmpty() || token==null || token.isEmpty()){
					LOGGER.error("Msg Login-Fail :  msgId:"+msgId+"   token:"+token);
					// pt.login.notAllowLoginByMsg=메세지에 의해 로그인 할 수 없습니다.
					String message = messageProperties.getMessage("pt.login.notAllowLoginByMsg", request);
					return processMsgLoginFail(msgId, message, model);
				}
			}
			
			boolean newSessionByMsg = false;
			UserVo userVo = null;
			PtPushMsgDVo ptPushMsgDVo = null;
			
			// 메세지 로그인의 경우
			if(!isMsnLgin){
				
				// 2.3 메세지ID로 메세지상세 조회
				ptPushMsgDVo = ptLoginSvc.getPushMsgDetl(msgId);
				if(ptPushMsgDVo==null){
					LOGGER.error("Msg Login-Fail - no data PT_PUSH_MSG_D :  msgId:"+msgId);
					// pt.login.notAllowLoginByMsg=메세지에 의해 로그인 할 수 없습니다.
					String message = messageProperties.getMessage("pt.login.notAllowLoginByMsg", request);
					return processMsgLoginFail(null, message, model);
				}
				
				userVo = LoginSession.getUser(request);
				
				String mdRid = ptPushMsgDVo.getMdRid();
				if( userVo==null || (
						!userVo.getUserUid().equals(ptPushMsgDVo.getUserUid())
						&& !("MAIL".equals(mdRid) && userVo.getOdurUid().equals(ptPushMsgDVo.getUserUid()))
						&& !("AP".equals(mdRid) && ptLoginSvc.isSameUser(userVo, ptPushMsgDVo.getUserUid()))
					)){
					// 2.4 로그인 처리
					userVo = ptLoginSvc.processLoginByPtPushMsgDVo(request, response, ptPushMsgDVo, model);
					newSessionByMsg = true;
				}
				
				if(userVo==null){
					//LOGGER.error("Msg Login-Fail - no valid user by msgId :  msgId:"+msgId);
					// pt.login.notAllowLoginByMsg=메세지에 의해 로그인 할 수 없습니다.
					String message = messageProperties.getMessage("pt.login.notAllowLoginByMsg", request);
					return processMsgLoginFail(msgId, message, model);
				}
				
			// 메신저 로그인의 경우
			} else {
				// 시스템 정책 조회
				Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
				skipUagntTokenCheck = "Y".equals(sysPlocMap.get("mobileMsnLginEnable"));
				newSessionByMsg = true;
				
				userVo = LoginSession.getUser(request);
				if(userVo==null || (!skipUagntTokenCheck && (token==null || token.isEmpty()))){
					return viewLogin(request, null, model, locale);
				}
			}
			
			if(!skipUagntTokenCheck){
				// 2.5 userAgent 토큰 확인
				String uagnt = request.getHeader("User-Agent");
				String uagntHash = Integer.toString(CRC32.hash(uagnt.getBytes("UTF-8")));
				
				// 2.5.1 토큰 조회
				PtUserUagntDVo ptUserUagntDVo = new PtUserUagntDVo();
				ptUserUagntDVo.setOdurUid(userVo.getOdurUid());
				ptUserUagntDVo.setUagntHashVa(uagntHash);
				ptUserUagntDVo = (PtUserUagntDVo)commonSvc.queryVo(ptUserUagntDVo);
				
				// 2.5.2 토큰이 같은지 확인
				String serverToken = ptUserUagntDVo==null ? null : ptUserUagntDVo.getUagntTknVa();
				if(serverToken==null || !serverToken.equals(token)){
					if(newSessionByMsg) request.getSession().invalidate();
					String message = null; 
					if(isMsnLgin){
						LOGGER.error("Messenger Login-Fail - token unmatched :  token:"+token
								+"   serverToken:"+serverToken);
						return processMsnLoginFail(msgId, message, model);
					} else {
						LOGGER.error("Msg Login-Fail - token unmatched :  token:"+token
								+"   serverToken:"+serverToken);
						//pt.login.notAllowLoginByMsg=메세지에 의해 로그인 할 수 없습니다.
						message = messageProperties.getMessage("pt.login.notAllowLoginByMsg", request);
						return processMsgLoginFail(msgId, message, model);
					}
				}
			}
			
			if(ptPushMsgDVo != null){
				session.setAttribute("ptPushMsgDVo", ptPushMsgDVo);
			}
		}
		
		// [msgId 로그인 3단계]
		//  - 상단 : 복호화 후 유효시간 내의 메세지에 대해 로그인 시키거나
		//  - id/pw 입력 로그인 처리 후 해당 페이지 호출로 처리됨
		model.put("UI_TITLE", "Processing message ..");
		
		UserVo userVo = LoginSession.getUser(request);
		PtPushMsgDVo ptPushMsgDVo = (PtPushMsgDVo)session.getAttribute("ptPushMsgDVo");
		
		// 로그인 페이지로
		if(userVo==null || (!isMsnLgin && ptPushMsgDVo==null)){
			return viewLogin(request, null, model, locale);
		}
		if(ptPushMsgDVo!=null){
			model.addAttribute("ptPushMsgDVo", ptPushMsgDVo);
		}
		
//		// IP 정책 체크 - 외부 로그인 가능한 사용자 체크
//		String ip = ipChecker.getIp(request);
//		userVo.setLginIp(ip);//로그인IP
//		userVo.setInternalIp(true);//내부망 IP 여부
//		
//		// 권한 있는 모듈 참조 ID
//		userVo.setMnuGrpMdRids(ptSecuSvc.getAuthdMnuGrpByMdRids(userVo, PtConstant.AUTH_CHK_MNU_GRP_MD_RIDS));
		
		
		long createTime = (time==null || time.isEmpty()) ? 
				System.currentTimeMillis() - 10000 : Long.parseLong(time);
		RsaKey rsaKey = cryptoSvc.getNextCookieKey();
		SecuCookie secuCookie = new SecuCookie(rsaKey, createTime);
		userVo.setSecuCookie(secuCookie);
		model.put("secuE", rsaKey.getPublicKey());
		model.put("secuM", rsaKey.getModulus());
		model.put("secuX", secuCookie.getSecuCode());
		
		return MoLayoutUtil.getJspPath("/cm/login/processLogin");
	}

	/** 메세지 로그인 실패 처리 */
	private String processMsgLoginFail(String msgId, String message, ModelMap model){
		if(message!=null) model.put("message", message);
		if(msgId==null || msgId.isEmpty()){
			model.addAttribute("togo", PtConstant.URL_LOGIN);
		} else {
			model.addAttribute("togo", PtConstant.URL_LOGIN+"?msgId="+msgId);
		}
		return LayoutUtil.getResultJsp();
	}

	/** 메세지 로그인 실패 처리 */
	private String processMsnLoginFail(String msgId, String message, ModelMap model){
		if(message!=null) model.put("message", message);
		model.addAttribute("togo", PtConstant.URL_LOGIN);
		return LayoutUtil.getResultJsp();
	}
}
