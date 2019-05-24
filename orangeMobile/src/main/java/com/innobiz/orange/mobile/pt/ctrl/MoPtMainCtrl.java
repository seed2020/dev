package com.innobiz.orange.mobile.pt.ctrl;

import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.mobile.pt.svc.MoMainSvc;
import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtMainSvc;
import com.innobiz.orange.web.pt.svc.PtPsnSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSsoSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtMnuGrpBVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;
import com.innobiz.orange.web.pt.vo.PtPushMsgDVo;

/** 메인 컨트롤러 */
@Controller
public class MoPtMainCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoPtMainCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 모바일 메인 서비스 */
	@Autowired
	private MoMainSvc moMainSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtPsnSvc ptPsnSvc;
	
	/** 포털 보안 서비스(레이아웃 포함) */
	@Autowired
	private PtSecuSvc ptSecuSvc;

	/** 포털 메인 서비스 */
	@Autowired
	private PtMainSvc ptMainSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
//	/** 로그인 관련 서비스 */
//	@Autowired
//	private PtLoginSvc ptLoginSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** SSO 처리용 서비스 */
	@Autowired
	private PtSsoSvc ptSsoSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	ApCmSvc apCmSvc;
	
//	/** 관리자 로그인 관련 서비스 */
//	@Autowired
//	private PtLoginSecuSvc ptLoginSecuSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
//	
//	/** 암호화 서비스 */
//	@Autowired
//	private CryptoSvc cryptoSvc;
	
//	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
//	@Autowired
//	private IpChecker ipChecker;

//	/** 중국, 해외 IP 차단 정책에 따른 요청에 차단 및 딜레이 적용 */
//	@Autowired
//	private ForeignIpBlocker foreignIpBlocker;
	
	/** 메인 : / */
	@RequestMapping(value = "/")
	public String indexRoot(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "msgId", required = false) String msgId,
			@RequestParam(value = "ssoOnetime", required = false) String ssoOnetime,
			ModelMap model) throws Exception {
		return index(request, response, msgId, ssoOnetime, model);
	}

	/** 메인 : index.do */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "msgId", required = false) String msgId,
			@RequestParam(value = "ssoOnetime", required = false) String ssoOnetime,
			ModelMap model) throws Exception {
		
		model.put("custCode", CustConfig.CUST_CODE);
		
		String iphoneShortcut = iphoneShortcutCheck(request);
		if(iphoneShortcut!=null){
			return iphoneShortcut;
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
		// SSL 사용 옵션에 - https 가 아닌 경우
		if(useSSL && !request.isSecure() && request.getServerPort() == 80){
			StringBuilder builder = new StringBuilder(128);
			builder.append("https://").append(request.getServerName()).append("/index.do");
			
			String name;
			boolean first = true;
			Enumeration<String> names = request.getParameterNames();
			while(names.hasMoreElements()){
				name = names.nextElement();
				if(first){
					builder.append('?');
					first = false;
				} else {
					builder.append('&');
				}
				builder.append(name).append('=').append(URLEncoder.encode(request.getParameter(name), "UTF-8"));
			}
			
			response.sendRedirect(builder.toString());
			return null;
		}
		
		// 메신저에서 SSO에 의해 호출된 것은 이미 userVo 생성 되어 있음
		UserVo userVo = LoginSession.getUser(request);
		
		// [msgId 로그인 1단계] - uagntToken 을 받기위한 암호화 세션 준비
		if(msgId!=null && !msgId.isEmpty()){
			return processMsgLoginStep1(request, response, msgId, model);
		}
		
		// 메신저에서 그룹웨어 터치 했을때 - 자동로그인
		if(ssoOnetime!=null && !ssoOnetime.isEmpty() && userVo!=null){
			return processMsgLoginStep1(request, response, PtConstant.UCWARE_FOR_MSN, model);
		}
		
		model.put("loginPage", PtConstant.URL_LOGIN);
		
		if(userVo==null){
			model.put("initPage", PtConstant.URL_LOGIN);
		} else {
			
			// 초기 페이지 설정 조회
			Map<String, String> loginMap = ptPsnSvc.getUserSetupMap(request, userVo.getUserUid(), PtConstant.MB_LOGIN, true);
			
			// 시스템 정책 조회
//			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			if(emailSvc.isInService() && (sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable")))){
				model.put("MAIL_ON", Boolean.TRUE);
			}
			
			// 세션의 메세지VO 확인
			HttpSession session = request.getSession(true);
			PtPushMsgDVo ptPushMsgDVo =(PtPushMsgDVo)session.getAttribute("ptPushMsgDVo");
			
			//////////////////////////////
			// 초기 페이지 확인 과정
			
			// 메세지에 의해 로그인 된 경우면
			if(ptPushMsgDVo != null){
				
				msgId = ptPushMsgDVo.getPushMsgId();
				session.removeAttribute("ptPushMsgDVo");
				model.put("ptPushMsgDVo", ptPushMsgDVo);
				
				if("MAIL".equals(ptPushMsgDVo.getMdRid())){
					// SSO 정보 생성
					String encrypted = ptSsoSvc.createOnetime(userVo);
					String url = ptPushMsgDVo.getMobUrl();
					url = url + (url.indexOf('?')>0 ? '&' : '?') + "byMsg=Y&onetime=" + encrypted;
					model.put("togo", url);
					model.put("noInitM", Boolean.TRUE);
					model.put("UI_TITLE", "Processing message ..");
					return MoLayoutUtil.getResultJsp();
				} else {
					String initPage = ptSecuSvc.toAuthMenuUrl(userVo, ptPushMsgDVo.getMobUrl(), ptPushMsgDVo.getMobAuthUrl());
					model.put("initPage", initPage);
					model.put("msgUrl", initPage);
				}
				
			} else {
				
				// 겸직 클릭해서 - 해당 겸직의 대기함으로 이동의 경우
				String destination = (String)session.getAttribute("destination");
				if("waitBx".equals(destination)){
					session.removeAttribute("destination");
					String initPage = ptSecuSvc.toAuthMenuUrl(userVo, "/ap/box/listApvBx.do?bxId=waitBx");
					model.put("initPage", initPage);
					
				// 일반 로그인 초기 화면 확인
				} else {
					
					String mnuLoutId = loginMap.get("initPage"), initPage = null;
					
					boolean defaultHome = true;
					PtMnuLoutDVo ptMnuLoutDVo = ptSecuSvc.getAuthedPtMnuLoutDVo(userVo, mnuLoutId, defaultHome);
					
					if(ptMnuLoutDVo != null){
						initPage = ptSecuSvc.getUrlByPtMnuLoutDVo(userVo, ptMnuLoutDVo, false);
						model.put("initPage", initPage);
						
						// 로그인 직 후면 - 자동 메뉴 오픈 - 한번만 띄우기 위해
						if(session.getAttribute("afterLoginForAutoMenu") != null){
							session.removeAttribute("afterLoginForAutoMenu");
							// 로그인 후 메뉴 열기
							if("Y".equals(loginMap.get("menuOpenYn"))){
								model.put("afterLoginMnuLoutId", ptMnuLoutDVo.getMnuLoutId());
							}
						}
					}
					
				}
			}
			
			// 메뉴 세팅
			moMainSvc.setMobileMenu(userVo, model);

			// 메뉴 아이콘 표시 - 우 하단 고정 위치에 메뉴 아이콘 표시 여부
			if("Y".equals(loginMap.get("fixedMenuIcon"))){
				model.put("fixedMenuIcon", Boolean.TRUE);
			}
			
			// 일정 대신 겸직 결재 표시
			if(userVo!=null){
				Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
				if(!"Y".equals(optConfigMap.get("adurMergLst"))){//겸직통합 표시
					loginMap = ptPsnSvc.getUserSetupMap(request, userVo.getOdurUid(), PtConstant.MB_LOGIN, true);
					if("Y".equals(loginMap.get("useOdurApvCnt")) && userVo!=null && userVo.getAdurs()!=null){
						model.put("useOdurApvCnt", Boolean.TRUE);
					}
				}
			}
		}
		return "index";
	}

	/** 로그아웃 - 메일의 메세지에 의한 로그인 일 경우 - 나가기가 안되어 모바일의 로그아웃을 호출하면 로그아웃 시키고 로그인 페이지 보여줌 */
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			ModelMap model) throws Exception {
		session.invalidate();
		response.sendRedirect("/");
		return null;
	}
	
	/** 아이폰 바로가기 인지 체크 - 메일쪽 세션 끊기는 현상 */
	private String iphoneShortcutCheck(HttpServletRequest request){
		
		String userAgent = request.getHeader("User-Agent");
		String device = userAgent.indexOf("iPhone")>=0 ? "iPhone" :
			userAgent.indexOf("iPad")>=0 ? "iPad" :
				userAgent.indexOf("iPod")>=0 ? "iPod" :
					null;
		if(device != null){
			// 바로가기
// Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13E238 - 바로가기
// Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13E238 Safari/601.1 - 사파리
// Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13E238 NAVER(inapp; search; 510; 7.1.0)
// Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13E238 DaumApps/6.0.0 DaumDevice/mobile
			
			int p = userAgent.indexOf("Mobile/");
			int q = userAgent.length()-p;//13
			if(p>0 && q<16){
				request.setAttribute("device", device);
				String qString = request.getQueryString();
				String shortcutUrl = "/" + (qString==null || qString.isEmpty() ? "" : "?" +qString);
				request.setAttribute("shortcutUrl", shortcutUrl);
				return "/cm/util/iphoneShortcut";
			}
		}
		return null;
	}
	
	/** msgId 로그인 1단계 */
	private String processMsgLoginStep1(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "msgId", required = false) String msgId,
			ModelMap model) throws Exception {
		
		// [msgId 로그인 1단계] - secu 로 uagntToken 을 받기 위한 준비
		model.put("msgId", msgId);

		// 시큐코드 생성(RSA 암호화 세션 생성용) - 중복 호출 오류 방지 - 각종 캐쉬 때문에 
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
		
//		UserVo userVo = LoginSession.getUser(request);
//		String msnLgin = (String)model.get("mobileMsnLginEnable");
//		if(userVo!=null && "Y".equals(msnLgin)){
//			// 세션에 사용자가 있고 - sso 에 의해 사용자 정보 생성됨
//			// 메신저 자동로그인 사용할 경우
//			// - uagntToken 토큰 체크 안하고 로그인 처리함
//			model.put("noUagntToken", Boolean.TRUE);
//		} else {
//			// uagntToken 토큰으로 로그인 처리함
//			model.put("needUagntToken", Boolean.TRUE);
////			if(userVo!=null && "N".equals(msnLgin)){
////				request.getSession().invalidate();
////			}
//		}
		model.put("needUagntToken", Boolean.TRUE);
		model.put("loginPage", PtConstant.URL_LOGIN);
		return "index";
	}
	
//	/** 메인 : index.do */
//	@RequestMapping(value = "/index2")
//	public String index2(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam(value = "msgId", required = false) String msgId,
//			ModelMap model) throws Exception {
//		
//		// [msgId 로그인 1단계] - secu 로 uagntToken 을 받음
//		if(msgId!=null && !msgId.isEmpty()){
//			
//			// 중복 호출 오류 방지 - 각종 캐쉬 때문에
//			HttpSession session = request.getSession(true);
//			@SuppressWarnings("unchecked")
//			List<String> secuSessionCodeList = (List<String>)session.getAttribute("secuSessionCodeList");
//			if(secuSessionCodeList==null){
//				secuSessionCodeList = new ArrayList<String>();
//				session.setAttribute("secuSessionCodeList", secuSessionCodeList);
//			}
//			if(secuSessionCodeList.size()>3){
//				secuSessionCodeList.remove(0);
//			}
//			
//			String secuSessionCode = StringUtil.getNextHexa();
//			secuSessionCodeList.add(secuSessionCode);
//			model.put("secuSessionCode", secuSessionCode);
//			
//			model.put("needUagntToken", Boolean.TRUE);
//			model.put("loginPage", PtConstant.URL_LOGIN);
//			return "index";
//		}
//		
//		UserVo userVo = LoginSession.getUser(request);
//		model.put("loginPage", PtConstant.URL_LOGIN);
//		
//		// 메세지 상세 조회
//		PtPushMsgDVo ptPushMsgDVo = null;
//		boolean afterProcessMsgLogin = false;
//		boolean needSecuKey = false;
//		
//		HttpSession session = request.getSession(true);
//		if(msgId!=null && !msgId.isEmpty()){
//			
//			// 메세지 로그인 1.1 - 메세지ID로 메세지상세 조회
//			ptPushMsgDVo = ptLoginSvc.getPushMsgDetl(msgId);
//			
//			// 겸직자의 경우 - 다른 계정이면 이전 사용자 세션 삭제함
//			if(userVo!=null && !userVo.getUserUid().equals(ptPushMsgDVo.getUserUid())
//					 && userVo.getAdurs() != null){
//				for(String[] adurs : userVo.getAdurs()){
//					if(adurs[1].equals(ptPushMsgDVo.getUserUid())){
//						// 사용자 정보 - 지우고 다시 로그인 처리 - 같은 사용자의 계정
//						userVo = null;
//						break;
//					}
//				}
//			}
//		} else {
//			// 세션의 메세지VO 확인
//			ptPushMsgDVo =(PtPushMsgDVo)session.getAttribute("ptPushMsgDVo");
//			
//			// 메세지 로그인 후 메뉴 세팅 및 해당 페이지 이동을 위한 리로드 한 경우
//			if(ptPushMsgDVo != null && userVo != null){
//				session.removeAttribute("ptPushMsgDVo");
//				afterProcessMsgLogin = true;
//				
//				msgId = ptPushMsgDVo.getPushMsgId();
//				model.put("ptPushMsgDVo", ptPushMsgDVo);
//			}
//		}
//		
//		// 메세지 로그인 후 리로드된 상태가 아니면 : !afterProcessMsgLogin
//		// ptPushMsgDVo 가 있으면 - msgId 가 넘겨오고 해당 값이 DB에 있을때
//		if(!afterProcessMsgLogin && ptPushMsgDVo!=null){
//			
//			// 로그인 되지 않았으면
//			if(userVo==null){
//				
//				// 메세지 로그인 1.2 - 메세지가 24시간 경과하지 않았을때
//				if(ptPushMsgDVo.getValid()){
//					// 메세지 로그인 1.2.1 - 푸쉬 메세지에 의한 로그인 처리
//					userVo = ptLoginSvc.processLoginByPtPushMsgDVo(request, ptPushMsgDVo, model);
//					if(userVo != null){
//						// 로그인 시키고 - 메세지에 저장된 페이지로 이동
//						needSecuKey = true;
//					}
//				} else {
//					// 메세지 로그인 1.2.2 - 로그인 화면에 msgId 넘겨줌(tiles/index.jsp)
//					model.put("tilesMsgIdParam", "?msgId="+msgId);
//				}
//			
//			// 이미 로그인은 되어 있는 경우 
//			} else {
//				
//				// 로그인이 이미 되어 있으나 - 메세지 로그인이 차단 되어 있는 경우 세션을 삭제함
//				
//				// 원직자기본(OR_ODUR_B) 테이블 - 조회
//				OrOdurBVo orOdurBVo = new OrOdurBVo();
//				orOdurBVo.setUserUid(ptPushMsgDVo.getUserUid());
//				orOdurBVo.setQueryLang(userVo.getLangTypCd());
//				orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
//				
//				if(orOdurBVo!=null){
//					// 모바일사용여부, 메세지로그인여부
//					if("N".equals(orOdurBVo.getMobUseYn()) || "N".equals(orOdurBVo.getMsgLginYn())){
//						request.getSession(true).invalidate();
//						userVo = null;
//						
//						// 로그인 화면에 msgId 넘겨 주기 위한 것(tiles/index.jsp)
//						model.put("tilesMsgIdParam", "?msgId="+msgId);
//					} else {
//						// 모바일 디바이스에서 새창이 되어 세션은 공유 되는데 세션스토리지가 공유가 안되어
//						// 매번 호출할때 마다 세션 스토리지를 생성해야함
//						needSecuKey = true;
//					}
//				}
//			}
//		}
//		
//		if(userVo==null){
//			if(ptPushMsgDVo!=null){
//				model.put("initPage", PtConstant.URL_LOGIN+"?msgId="+ptPushMsgDVo.getPushMsgId());
//				model.put("tilesMsgIdParam", "?msgId="+msgId);
//			} else {
//				model.put("initPage", PtConstant.URL_LOGIN);
//			}
//		} else {
//			// 시스템 정책 조회
//			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
//			
//			// 초기 페이지 설정 조회
//			Map<String, String> loginMap = ptPsnSvc.getUserSetupMap(request, userVo.getUserUid(), PtConstant.MB_LOGIN, true);
//			
//			if(emailSvc.isInService() && (sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable")))){
//				model.put("MAIL_ON", Boolean.TRUE);
//			}
//			
//			// 메세지에 의해 로그인 된 경우
//			if(needSecuKey){
//				// secu-rsa-key sessionStorage 처리 후 - "/" 리로드
//				request.getSession().setAttribute("ptPushMsgDVo", ptPushMsgDVo);
//				model.put("msgUrl", "/cm/login/processMsg.do?msgId="+ptPushMsgDVo.getPushMsgId());
//				model.put("initPage", PtConstant.URL_LOGIN);
//				
//			} else if(ptPushMsgDVo != null){
//				
//				if(userVo.getUserUid().equals(ptPushMsgDVo.getUserUid())){
//					
//					if("MAIL".equals(ptPushMsgDVo.getMdRid())){
//						// SSO 정보 생성
//						String encrypted = ptSsoSvc.createOnetime(userVo);
//						String url = ptPushMsgDVo.getMobUrl();
//						url = url + (url.indexOf('?')>0 ? '&' : '?') + "byMsg=Y&onetime=" + encrypted;
//						model.put("togo", url);
//						model.put("noInitM", Boolean.TRUE);
//						model.put("UI_TITLE", "Processing message ..");
//						return MoLayoutUtil.getResultJsp();
//					} else {
//						String initPage = ptSecuSvc.toAuthMenuUrl(userVo, ptPushMsgDVo.getMobUrl(), ptPushMsgDVo.getMobAuthUrl());
//						model.put("initPage", initPage);
//						model.put("msgUrl", initPage);
//					}
//					
//				} else {
//					// mpt.msg.msgIdUserDiff=로그인된 계정과 메세지의 계정이 다릅니다.\\n다시 로그인 하시겠습니까 ?
//					String reloginMsg = messageProperties.getMessage("mpt.msg.msgIdUserDiff", request);
//					model.put("reloginMsg", reloginMsg);
//					// 로그인 화면에 msgId 넘겨줌(tiles/index.jsp)
//					model.put("tilesMsgIdParam", "?msgId="+msgId);
//				}
//				
//			// 일반 로그인
//			} else {
//				
//				String destination = (String)session.getAttribute("destination");
//				if("waitBx".equals(destination)){
//					session.removeAttribute("destination");
//					String initPage = ptSecuSvc.toAuthMenuUrl(userVo, "/ap/box/listApvBx.do?bxId=waitBx");
//					model.put("initPage", initPage);
//				} else {
//					
//					String mnuLoutId = loginMap.get("initPage"), initPage = null;
//					
//					boolean defaultHome = true;
//					PtMnuLoutDVo ptMnuLoutDVo = ptSecuSvc.getAuthedPtMnuLoutDVo(userVo, mnuLoutId, defaultHome);
//					
//					if(ptMnuLoutDVo != null){
//						initPage = ptSecuSvc.getUrlByPtMnuLoutDVo(userVo, ptMnuLoutDVo, false);
//						model.put("initPage", initPage);
//						
//						// 로그인 직 후면 - 자동 메뉴 오픈 - 한번만 띄우기 위해
//						if(session.getAttribute("afterLoginForAutoMenu") != null){
//							session.removeAttribute("afterLoginForAutoMenu");
//							// 로그인 후 메뉴 열기
//							if("Y".equals(loginMap.get("menuOpenYn"))){
//								model.put("afterLoginMnuLoutId", ptMnuLoutDVo.getMnuLoutId());
//							}
//						}
//					}
//					
//				}
//			}
//			
//			// 메뉴 세팅
//			moMainSvc.setMobileMenu(userVo, model);
//
//			// 메뉴 아이콘 표시 - 우 하단 고정 위치에 메뉴 아이콘 표시 여부
//			if("Y".equals(loginMap.get("fixedMenuIcon"))){
//				model.put("fixedMenuIcon", Boolean.TRUE);
//			}
//			
//			// 일정 대신 겸직 결재 표시
//			loginMap = ptPsnSvc.getUserSetupMap(request, userVo.getOdurUid(), PtConstant.MB_LOGIN, true);
//			if("Y".equals(loginMap.get("useOdurApvCnt")) && userVo!=null && userVo.getAdurs()!=null){
//				model.put("useOdurApvCnt", Boolean.TRUE);
//			}
//			
//		}
//		return "index";
//	}
	
//	/** 푸쉬메시지상세 VO 로 로그인 시키기 */
//	private UserVo processLoginByPtPushMsgDVo(HttpServletRequest request,
//			PtPushMsgDVo ptPushMsgDVo, ModelMap model) throws IOException, CmException, SQLException{
//		
//		HttpSession session = request.getSession(true);
//		String langTypCd = ptPushMsgDVo.getLangTypCd();
//		Locale locale = SessionUtil.toLocale(langTypCd);
//		session.setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", locale);
//		
//		// admin 계정 로그인
//		if("U0000001".equals(ptPushMsgDVo.getUserUid())){
//			session.setAttribute("SKIP_ADMIN_PW", Boolean.TRUE);
//			ptLoginSecuSvc.processAdminLogin(request, session, null, locale);
//			return LoginSession.getUser(request);
//			
//		// 일반계정 로그인
//		} else {
//			
//			// 원직자기본(OR_ODUR_B) 테이블 - 로그인 아이디로 사용자 정보 조회
//			OrOdurBVo orOdurBVo = new OrOdurBVo();
//			orOdurBVo.setUserUid(ptPushMsgDVo.getUserUid());
//			orOdurBVo.setQueryLang(langTypCd);
//			orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
//			
//			if(orOdurBVo==null){
//				LOGGER.error("Login-Fail by msgId : no user(OR_ODUR_B) : pushMsgId: "+ptPushMsgDVo.getPushMsgId()+"  userUid:"+ptPushMsgDVo.getUserUid());
//				// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
//				model.addAttribute("errorByMsgId", messageProperties.getMessage("pt.login.noUser", locale));
//				return null;
//			}
//			String odurUid = orOdurBVo.getOdurUid();
//			
//			// 사용자상태코드 체크 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 99:삭제
//			if(!"02".equals(orOdurBVo.getUserStatCd())){
//				//pt.login.notAllowedStat=로그인 가능 사용자가 아닙니다.
//				LOGGER.error("Login-Fail by msgId : user stat(not 02) - pushMsgId:"+ptPushMsgDVo.getPushMsgId()+"  odurUid:"+odurUid+"  userStatCd:"+orOdurBVo.getUserStatCd());
//				model.addAttribute("errorByMsgId", messageProperties.getMessage("pt.login.notAllowedStat", locale));
//				return null;
//			}
//			
//			// 모바일사용여부
//			if("N".equals(orOdurBVo.getMobUseYn())){
//				//pt.login.notAllowedStat=로그인 가능 사용자가 아닙니다.
//				LOGGER.error("Login-Fail by msgId - mobUseYn:N  odurUid:"+orOdurBVo.getOdurUid()+"  pushMsgId:"+ptPushMsgDVo.getPushMsgId());
//				model.addAttribute("errorByMsgId", messageProperties.getMessage("pt.login.notAllowedStat", locale));
//				return null;
//			}
//			
//			// 메세지로그인여부
//			if("N".equals(orOdurBVo.getMsgLginYn())){
//				//pt.login.notAllowLoginByMsg=메세지에 의해 로그인 할 수 없습니다.
//				LOGGER.error("Login-Fail by msgId - msgLginYn:N  odurUid:"+orOdurBVo.getOdurUid()+"  pushMsgId:"+ptPushMsgDVo.getPushMsgId());
//				model.addAttribute("errorByMsgId", messageProperties.getMessage("pt.login.notAllowLoginByMsg", locale));
//				return null;
//			}
//			
//			// 사용자 세션 정보 생성
//			ptLoginSvc.createUserSessionByOdurUid(odurUid, ptPushMsgDVo.getUserUid(), request, orOdurBVo);
//			
//			// 로그인 팝업 조회
//			UserVo userVo = LoginSession.getUser(request);
//			
//			// IP 정책 체크 - 외부 로그인 가능한 사용자 체크
//			String ip = ipChecker.getIp(request);
//			userVo.setLginIp(ip);//로그인IP
//			
//			// IP 정책 체크 - 외부 로그인 가능한 사용자 체크
//			boolean canLogin = ipChecker.canLogin(ip);
//			// 제외대상로그인여부
//			boolean excliLginYn = userVo.getLginIpExcliYn();
//			
//			boolean hasAllowLginPolc = ipChecker.hasAllowLginPolc();
//			
//			// 제외대상자-가 아니고, 허용되는 IP설정으로 로그인 한 사용자가 아니면
//			if(!excliLginYn && !(hasAllowLginPolc && canLogin) ){
//				boolean requestOk = foreignIpBlocker.isLoginNoBlockByMsgId(request, ptPushMsgDVo.getPushMsgId());
//				if(!requestOk){
//					LOGGER.error("Login-Fail by msgId : Foreign IP Policy - pushMsgId:"+ptPushMsgDVo.getPushMsgId()+"  odurUid:"+odurUid+"  ip:"+ip);
//					//해외 IP 로그인 Fail 응답 딜레이
//					try{ Thread.sleep(1000); }catch(Exception ignore){}
//					request.getSession().invalidate();
//					// pt.login.notAllowedIp=IP 정책에 의해 로그인이 차단 되었습니다.
//					model.addAttribute("errorByMsgId", messageProperties.getMessage("pt.login.notAllowedIp", locale));
//					//LOGGER.error("Login-Fail by msgId : IP Policy - loginId:"+lginId+"  odurUid:"+odurUid+"  ip:"+ip);
//					return null;
//				}
//			}
//			return userVo;
//		}
//	}
	
	/** [AJAX]메인 건수 구함 */
	@RequestMapping(value = "/cm/getMainCntAjx")
	public String getMainCntAjx(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try {
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			if( userVo == null ) model.put("result", "Login required !");
			else {
				Map<String,String> rsltMap = new HashMap<String,String>();
				ptMainSvc.getMainCnt(request, userVo, rsltMap, false, model);
				
				if(rsltMap.size() > 0 ){
					model.put("rsltMap", rsltMap);
					model.put("result", "ok");
				}else{
					model.put("result", "fail");
				}
			}
		} catch (SQLException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.returnJson(model);
	}
	/** [AJAX] 외부 URL 구해옴 / SSO 처리 */
	@RequestMapping(value = "/cm/getOuterUrlAjx")
	public String getOuterUrlAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
		String act = (String)jsonObject.get("act");
		String to = (String)jsonObject.get("to");
		String mnuGrpId = (String)jsonObject.get("mnuGrpId");
		
		Map<String, String> map = ptSysSvc.getSvrEnvMap();
		String domain = map.get("mailCall");
		
		if("mail".equals(act) || "notReadMail".equals(act) || "writeMail".equals(act) || "notApvReadMail".equals(act)){
			
			if("Y".equals(map.get("default")) || domain==null || domain.length() < 5){
				LOGGER.error("SSO Error - Mail domain needs setting !");
			} else {
				
				// mail - 메뉴의 아이콘 클릭 - 받은메일함
				// notReadMail - 메뉴의 메일 숫자 클릭 - 안 읽은 메일
				// writeMail - 메일 쓰기
				
				UserVo userVo = LoginSession.getUser(request);
				String encrypted = ptSsoSvc.createOnetime(userVo);
//				String path = "notReadMail".equals(act) ? "/zmail/m_login.nvd?cmd=receive_mailbox_notread" : 
//						"writeMail".equals(act) ? "/zmail/m_login.nvd?cmd=write_mail_to&to="+to : "";
				String path = "notReadMail".equals(act) ? "/zmail/m_login.nvd?cmd=mail_link_2" : 
					"writeMail".equals(act) ? "/zmail/m_login.nvd?cmd=write_mail_to&to="+to : "notApvReadMail".equals(act) ? "/zmail/m_login.nvd?cmd=approve_receive_mailbox" : "";
				String url = "http://"+domain+path+(path.indexOf('?')>0?'&':'?')+"onetime="+encrypted;
				
				boolean useSSL = request.isSecure();
				if(useSSL){
					url = ptSysSvc.toZMailSslUrl(url);
				}
				
				model.put("url", url);
				return LayoutUtil.returnJson(model);
			}
		} else if(mnuGrpId != null && !mnuGrpId.isEmpty()){
			
			PtMnuGrpBVo ptMnuGrpBVo = null;
			String url = null, mdRid = null;
//			boolean valid = true;
			
			if(mnuGrpId != null && !mnuGrpId.isEmpty()){
				String langTypCd = LoginSession.getLangTypCd(request);
				ptMnuGrpBVo = ptLoutSvc.getMnuGrpByGrpId(mnuGrpId, langTypCd);
				if(ptMnuGrpBVo != null){
					mdRid = ptMnuGrpBVo.getMdRid();
				} else {
//					valid = false;
					LOGGER.error("SSO Error - No data (PT_MNU_GRP_B) by mnuGrpId:"+mnuGrpId);
				}
			}
			
			if("MAIL".equals(mdRid)){
				
				if("Y".equals(map.get("default")) || domain==null || domain.length() < 5){
					LOGGER.error("SSO Error - Mail domain needs setting !");
				} else {
					url = ptMnuGrpBVo.getMnuUrl();
					if(url!=null && url.length()>10){
						int p = url.indexOf('/', 9);
						if(p<0){
//							valid = false;
							LOGGER.error("SSO Error - Not valid url (PT_MNU_GRP_B) by mnuGrpId:"+mnuGrpId);
						} else if("Y".equals(map.get("default")) || domain==null || domain.length() < 5){
//							valid = false;
							LOGGER.error("SSO Error - Not valid mail domain");
						} else {
							
							UserVo userVo = LoginSession.getUser(request);
							if(userVo!=null){
								String encrypted = ptSsoSvc.createOnetime(userVo);
								url = "http://"+domain+url.substring(p)+(url.indexOf('?')>0?'&':'?')+"onetime="+encrypted;
								boolean useSSL = request.isSecure();
								if(useSSL){
									url = ptSysSvc.toZMailSslUrl(url);
								}
								model.put("url", url);
								return LayoutUtil.returnJson(model);
							} else {
								// pt.logout.timeout=로그인 세션이 종료 되었습니다.
								String message = messageProperties.getMessage("pt.logout.timeout", request);
								model.put("message", message);
								return LayoutUtil.returnJson(model);
							}
						}
					}
				}
				
			} else {
				// 외부 URL 등록할 경우 - 메일이 아닐때
				url = ptMnuGrpBVo.getMnuUrl();
				
				if(url != null){
					if(url.indexOf("<userId>")>0){
						UserVo userVo = LoginSession.getUser(request);
						url = url.replace("<userId>", "userId="+userVo.getLginId());
					}
				}
				
				model.put("url", url);
				return LayoutUtil.returnJson(model);
				
//				if(valid){
//					valid = false;
//					LOGGER.error("SSO Error - Not valid mdRid:"+mdRid+" (PT_MNU_GRP_B) by mnuGrpId:"+mnuGrpId);
//				}
			}
			
		} else {
			LOGGER.error("SSO Error - null mnuGrpId");
		}
		
		//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
		String message = messageProperties.getMessage("cm.msg.notValidCall", request);
		model.put("message", message);
		return LayoutUtil.returnJson(model);
	}

	
	/** [AJX] 겸직자 사용자 결재 선택 */
	@RequestMapping(value = "/cm/ap/setApAddiUserAjx")
	public String setApAddiUserPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		ptMainSvc.getMainApCntPop(request, userVo, model);
		
		return LayoutUtil.returnJson(model);
	}
	
	/** [AJX] 겸직자 사용자 결재 선택 */
	@RequestMapping(value = "/callback")
	public String callback(HttpServletRequest request,
			@RequestParam(value = "msgId", required = false) String msgId,
			ModelMap model) throws Exception {
		
		try{
			if(msgId!=null && !msgId.isEmpty()){
				String msgVa = ptCmSvc.removeSsoMsg(msgId);
				if(msgVa != null) model.put("msgVa", msgVa);
			}
		} catch(Exception ignore){}
		
		return "/cm/util/callback";
	}
	
	/** [AJX] 겸직자 사용자 결재 선택 */
	@RequestMapping(value = {"/cm/getSsoMsgAjx", "/cm/removeSsoMsgAjx"})
	public String getSsoMsgAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
		String msgId = (String)jsonObject.get("msgId");
		
		if(msgId!=null && !msgId.isEmpty()){
			String uri = request.getRequestURI();
			boolean isRemove = uri.indexOf("remove") > 0;
			String msgVa = isRemove ? ptCmSvc.removeSsoMsg(msgId) : ptCmSvc.getSsoMsg(msgId);
			if(msgVa!=null && !msgVa.isEmpty()){
				model.put("msgVa", msgVa);
			}
		}
		
		return LayoutUtil.returnJson(model);
	}
}
