package com.innobiz.orange.web.pt.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.pt.secu.Browser;
import com.innobiz.orange.web.pt.secu.CRC32;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtLoginSvc;
import com.innobiz.orange.web.pt.svc.PtMainSvc;
import com.innobiz.orange.web.pt.svc.PtMyMnuSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;
import com.innobiz.orange.web.pt.vo.PtPushMsgDVo;
import com.innobiz.orange.web.pt.vo.PtUserUagntDVo;

/** 메인 컨트롤러 */
@Controller
public class PtMainCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtMainCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
//	/** 메뉴 레이아웃 서비스 */
//	@Autowired
//	private PtLoutSvc ptLoutSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtMainSvc ptMainSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtMyMnuSvc ptMyMnuSvc;
	
	/** 로그인 관련 서비스  */
	@Autowired
	private PtLoginSvc ptLoginSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 메인 : / */
	@RequestMapping(value = "/")
	public String indexRoot(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="msgId", required=false) String msgId,
			@Parameter(name="secu", required=false) String secu,
			ModelMap model) throws Exception {
		return index(request, response, msgId, secu, model);
	}

	/** 메인 : index.do */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="msgId", required=false) String msgId,
			@Parameter(name="secu", required=false) String secu,
			ModelMap model) throws Exception {
		
		//////////////////////////
		//
		// SSO 1 단계 : 푸쉬클라이언트, 메신저 등에서  msgId 를 파라미터로 넘기면
		//          - 모바일 디바이스의 경우 모바일 서버로 redirect
		//			- 브라우저의 uagntToken 과 msgId를 암호화 하여 파라미터(secu)로 전달하기 위한 페이지로 이동함
		if(msgId!=null){
			
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			boolean useSSL = "Y".equals(sysPlocMap.get("useSSL"));
			
			// 메세지 조회
			PtPushMsgDVo ptPushMsgDVo = new PtPushMsgDVo();
			ptPushMsgDVo.setPushMsgId(msgId);
			ptPushMsgDVo = (PtPushMsgDVo)commonSvc.queryVo(ptPushMsgDVo);
			
			// 모바일 디바이스의 경우 - 모바일 웹서버로 redirect
			if(Browser.isMobile(request)){
				
				// 모바일 URL이 등록되지 않은 경우 - 모바일에서 지원하지 않는 기능 입니다. : 결재 [부서대기함]
				if(ptPushMsgDVo != null && (
						ptPushMsgDVo.getMobUrl()==null
						|| ptPushMsgDVo.getMobUrl().isEmpty()
						|| ("BB".equals(ptPushMsgDVo.getMdRid()) && ptPushMsgDVo.getUserUid().length()<8)
					)){
					Locale msgLocale = SessionUtil.toLocale(ptPushMsgDVo.getLangTypCd());
					// pt.notSupport.mobile=모바일에서 지원하지 않는 기능 입니다.
					String message = messageProperties.getMessage("pt.notSupport.mobile", msgLocale);
					model.put("message", message);
					model.put("todo", "window.close();");
					return LayoutUtil.getResultJsp();
				}
				
				// 모바일 서버로 리다이렉트
				Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
				String mobileDomain = svrEnvMap.get("mobileDomain");
				if(mobileDomain!=null && !mobileDomain.isEmpty()){
					response.sendRedirect((useSSL || request.isSecure() ? "https://" : "http://")+mobileDomain+"/index.do?msgId="+msgId);
					return null;
				} else {
					LOGGER.error("Mobile domain is not set !");
				}
			}
			
			if(useSSL && !request.isSecure() && request.getServerPort() == 80){
				response.sendRedirect("https://"+request.getServerName()+"/index.do?msgId="+msgId);
				return null;
			}
			
			// 게시 - 회사 공지
			if(ptPushMsgDVo!=null && "BB".equals(ptPushMsgDVo.getMdRid()) && ptPushMsgDVo.getUserUid().length()<8){
				UserVo userVo = LoginSession.getUser(request);
				if(userVo!=null && userVo.getCompId().equals(ptPushMsgDVo.getUserUid())){
					// menuId 를 url에 붙여서 - 메세지 페이지로 이동 
					String webUrl = ptSecuSvc.toAuthMenuUrl(userVo, ptPushMsgDVo.getWebUrl(), ptPushMsgDVo.getWebAuthUrl());
					response.sendRedirect(webUrl);
					return null;
				} else {
					//pt.login.notAllowLoginByMsg=사용자 인증에 실패하였습니다.
					String message = messageProperties.getMessage("pt.login.notAllowLoginByMsg", request);
					model.put("message", message);
					if(userVo==null){
						model.addAttribute("togo", PtConstant.URL_LOGIN);
					} else {
						model.addAttribute("todo", "window.close();");
					}
					return LayoutUtil.getResultJsp();
				}
			}
			
			
			// 로그인 페이지 설정 - uagntToken 이 없으면 로그인 페이지로 이동용
			model.put("loginUrl", PtConstant.URL_LOGIN);
			
			// 암호화 처리용
			String secuSessionCode = StringUtil.getNextHexa();
			model.put("secuSessionCode", secuSessionCode);
			HttpSession session = request.getSession(true);
			session.setAttribute("secuSessionCode", secuSessionCode);
			
			return "/cm/login/prepareMsgLogin";
		}
		
		//////////////////////////
		//
		// SSO 2 단계 : 푸쉬클라이언트, 메신저 등에서  msgId 를 파라미터로 넘기면
		//          - 모바일 디바이스의 경우 모바일 서버로 redirect
		//			- 브라우저의 uagntToken 과 msgId를 암호화 하여 파라미터(secu)로 전달하기 위한 페이지로 이동함
		if(secu!=null){
			
			JSONObject jsonObject = null;
			try {
				jsonObject = cryptoSvc.processRsa(request);
			} catch(CmException e){
				LOGGER.error("Msg Login-Fail : "+e.getMessage());
				// pt.login.fail.decrypt=복호화에 실패하였습니다.
				model.put("message", messageProperties.getMessage("pt.login.fail.decrypt", request));
				model.addAttribute("togo", PtConstant.URL_LOGIN);
				return LayoutUtil.getResultJsp();
			}
			
			msgId = (String)jsonObject.get("msgId");
			String token = (String)jsonObject.get("token");
			if(msgId==null || msgId.isEmpty() || token==null || token.isEmpty()){
				LOGGER.error("Msg Login-Fail :  msgId:"+msgId+"   token:"+token);
				//pt.login.notAllowLoginByMsg=사용자 인증에 실패하였습니다.
				String message = messageProperties.getMessage("pt.login.notAllowLoginByMsg", request);
				model.put("message", message);
				model.addAttribute("togo", PtConstant.URL_LOGIN);
				return LayoutUtil.getResultJsp();
			}
			
			// 메세지 데이터 조회
			PtPushMsgDVo ptPushMsgDVo = new PtPushMsgDVo();
			ptPushMsgDVo.setPushMsgId(msgId);
			ptPushMsgDVo = (PtPushMsgDVo)commonSvc.queryVo(ptPushMsgDVo);
			
			boolean newSessionByMsg = false;
			UserVo userVo = LoginSession.getUser(request);
			if(ptPushMsgDVo != null){
				String mdRid = ptPushMsgDVo.getMdRid();
				if( userVo==null || (
						!userVo.getUserUid().equals(ptPushMsgDVo.getUserUid())
						&& !("MAIL".equals(mdRid) && userVo.getOdurUid().equals(ptPushMsgDVo.getUserUid()))
						&& !("AP".equals(mdRid) && ptLoginSvc.isSameUser(userVo, ptPushMsgDVo.getUserUid()))
					)){
					// 메세지 로그인 처리
					userVo = ptLoginSvc.processLoginByPtPushMsgDVo(request, response, ptPushMsgDVo, model);
					newSessionByMsg = true;
				}
			}
			
			if(ptPushMsgDVo != null && userVo != null){
				
				// userAgent 토큰 확인
				String uagnt = request.getHeader("User-Agent");
				if(uagnt==null) uagnt = "empty";
				String uagntHash = Integer.toString(CRC32.hash(uagnt.getBytes("UTF-8")));
				
				PtUserUagntDVo ptUserUagntDVo = new PtUserUagntDVo();
				ptUserUagntDVo.setOdurUid(userVo.getOdurUid());
				ptUserUagntDVo.setUagntHashVa(uagntHash);
				ptUserUagntDVo = (PtUserUagntDVo)commonSvc.queryVo(ptUserUagntDVo);
				
				if(ptUserUagntDVo==null || !token.equals(ptUserUagntDVo.getUagntTknVa())){
					if(newSessionByMsg) {
						request.getSession().invalidate();
						LOGGER.error("Msg Login-Fail - token unmatched :  client-token:"+token+"   server-token:"
								+(ptUserUagntDVo==null ? "" : ptUserUagntDVo.getUagntTknVa()));
						//pt.login.notAllowLoginByMsg=사용자 인증에 실패하였습니다.
						String message = messageProperties.getMessage("pt.login.notAllowLoginByMsg", request);
						model.put("message", message);
						model.addAttribute("togo", PtConstant.URL_LOGIN);
						return LayoutUtil.getResultJsp();
					} else {
						//pt.login.notAllowLoginByMsg=사용자 인증에 실패하였습니다.
						String message = messageProperties.getMessage("pt.login.notAllowLoginByMsg", request);
						model.put("message", message);
						model.addAttribute("todo", "window.close();");
						return LayoutUtil.getResultJsp();
					}
				}
				
//				// 권한 있는 모듈 참조 ID
//				if(newSessionByMsg){
//					userVo.setMnuGrpMdRids(ptSecuSvc.getAuthdMnuGrpByMdRids(userVo, PtConstant.AUTH_CHK_MNU_GRP_MD_RIDS));
//				}
				
				String webUrl = ptPushMsgDVo.getWebUrl();
				if("MAIL".equals(ptPushMsgDVo.getMdRid())){
					if(webUrl!=null && !webUrl.isEmpty()){
						// 메세지 페이지로 이동
						response.sendRedirect(ptPushMsgDVo.getWebUrl());
						return null;
					}
				} else {
					if(webUrl!=null && !webUrl.isEmpty()){
						if(webUrl.indexOf("menuId=")>0 || webUrl.startsWith("/cm")){
							response.sendRedirect(webUrl);
							return null;
						} else {
							// menuId 를 url에 붙여서 - 메세지 페이지로 이동 
							webUrl = ptSecuSvc.toAuthMenuUrl(userVo, webUrl, ptPushMsgDVo.getWebAuthUrl());
							response.sendRedirect(webUrl);
							return null;
						}
					}
				}
			}
			
			if(ptPushMsgDVo==null){
				LOGGER.error("Msg Login-Fail - empty PtUserUagntDVo");
			} else if(userVo==null){
				LOGGER.error("Msg Login-Fail - empty UserVo");
			}
			
			//pt.login.notAllowLoginByMsg=사용자 인증에 실패하였습니다.
			String message = messageProperties.getMessage("pt.login.notAllowLoginByMsg", request);
			model.put("message", message);
			model.put("togo", PtConstant.URL_LOGIN);
			return LayoutUtil.getResultJsp();
		}
		//  메세지ID 처리
		//
		//////////////////////////
		
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null){
			model.put("url", PtConstant.URL_LOGIN);
			return LayoutUtil.returnJson(model);
		}
		
		String returnUrl = null;
		
		PtMnuLoutDVo ptMnuLoutDVo = ptSecuSvc.getAuthedPtMnuLoutDVo(userVo, null, true);
		if(ptMnuLoutDVo == null){
			// pt.msg.not.set.lout=레이아웃 설정이 변경되었거나 올바르지 않습니다.
			//model.put("message", messageProperties.getMessage("pt.msg.not.set.home", request));
		} else if(ptMnuLoutDVo.isMyMnu()){
			returnUrl = ptMyMnuSvc.getFirstMyPage(request, userVo);
		} else {
			returnUrl = ptSecuSvc.getUrlByPtMnuLoutDVo(userVo, ptMnuLoutDVo, false);
		}
		
		try{
			if(returnUrl==null){
				//pt.msg.not.set.home=홈 메뉴 그룹이 설정되지 않았습니다.
				throw new CmException("pt.msg.not.set.home", request);
			}
			response.sendRedirect(returnUrl);
			
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			request.setAttribute("exception", e);
			request.getRequestDispatcher(LayoutUtil.getErrorJsp(500)).forward(request,response);
		}
		
		return null;
	}
	
	/** [AJAX]메인 URL 구함 */
	@RequestMapping(value = "/cm/getHomeUrlAjx")
	public String getHomeUrlAjx(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws SQLException {
		
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null){
			model.put("url", PtConstant.URL_LOGIN);
			return LayoutUtil.returnJson(model);
		}
		
		String returnUrl = null;
		
		PtMnuLoutDVo ptMnuLoutDVo = ptSecuSvc.getAuthedPtMnuLoutDVo(userVo, null, true);
		if(ptMnuLoutDVo == null){
			// pt.msg.not.set.lout=레이아웃 설정이 변경되었거나 올바르지 않습니다.
			//model.put("message", messageProperties.getMessage("pt.msg.not.set.home", request));
		} else if(ptMnuLoutDVo.isMyMnu()){
			returnUrl = ptMyMnuSvc.getFirstMyPage(request, userVo);
		} else {
			returnUrl = ptSecuSvc.getUrlByPtMnuLoutDVo(userVo, ptMnuLoutDVo, false);
		}
		
		if(returnUrl==null){
			// pt.msg.not.set.home=홈 메뉴 그룹이 설정되지 않았습니다.
			model.put("message", messageProperties.getMessage("pt.msg.not.set.home", request));
		} else {
			model.put("url", returnUrl);
		}
		return LayoutUtil.returnJson(model);
	}
	
	/** [AJAX] 레이아웃 URL 구함 - 관리자가 아닌 사용자가 상단 메뉴를 클릭했을때 - 권한있는 첫번째 메뉴 URL 구하기 위한것 */
	@RequestMapping(value = "/cm/getLoutUrlAjx")
	public String getLoutUrlAjx(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null){
			model.put("url", PtConstant.URL_LOGIN);
			return LayoutUtil.returnJson(model);
		}
		
		JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
		String mnuLoutId = (String)jsonObject.get("mnuLoutId");
		String returnUrl = null;
		
		PtMnuLoutDVo ptMnuLoutDVo = ptSecuSvc.getAuthedPtMnuLoutDVo(userVo, mnuLoutId, false);
		if(ptMnuLoutDVo == null){
			// pt.msg.not.set.lout=레이아웃 설정이 변경되었거나 올바르지 않습니다.
			//model.put("message", messageProperties.getMessage("pt.msg.not.set.home", request));
		} else if(ptMnuLoutDVo.isMyMnu()){
			returnUrl = ptMyMnuSvc.getFirstMyPage(request, userVo);
		} else {
			returnUrl = ptSecuSvc.getUrlByPtMnuLoutDVo(userVo, ptMnuLoutDVo, false);
		}
		
		if(returnUrl==null){
			// pt.msg.not.set.lout=레이아웃 설정이 변경되었거나 올바르지 않습니다.
			model.put("message", messageProperties.getMessage("pt.msg.not.set.home", request));
		} else {
			model.put("url", returnUrl);
		}
		return LayoutUtil.returnJson(model);
	}
	
	/** [AJAX]메인 건수 구함 */
	@RequestMapping(value = "/cm/getMainCntAjx")
	public String getMainCntAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		try {
			
			JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
			String hasApMenu = (String)jsonObject.get("hasApMenu");
			String applyCachedCnt = (String)jsonObject.get("applyCachedCnt");
			String uid = (String)jsonObject.get("uid");
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			if( userVo == null ) model.put("result", "Login required !");
			else {
				
				boolean shouldQueryApCount = "Y".equals(hasApMenu) && (
						uid==null || !uid.equals(userVo.getUserUid()) || !"Y".equals(applyCachedCnt)
						);
				
				Map<String,String> rsltMap = new HashMap<String,String>();
				ptMainSvc.getMainCnt(request, userVo, rsltMap, shouldQueryApCount, model);
				
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
	
	/** [AJAX] PC 알림 데이터 */
	@RequestMapping(value = "/cm/getPcNoti")
	public String getPcNoti(HttpServletRequest request, HttpSession session,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) throws Exception {
		try {
			JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
			
			String permission = (String)jsonObject.get("permission");
			String clientServerTm = (String)jsonObject.get("pcNotiServerTm");
			String host = (String)jsonObject.get("host");
			boolean debug = jsonObject.get("debug") != null;
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			Browser browser = (Browser)request.getAttribute("browser");

//System.out.println(
//		(userVo==null ? "" : "userUid: "+userVo.getUserUid()+"\r\n")+
//		(permission==null ? "" : "permission: "+permission+"\r\n")+
//		"clientServerTm: "+clientServerTm+"\r\n"+
//		"host: "+host+"\r\n"
//		);

			// 시스템 정책 조회
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			String sessionSeverTm = (String)session.getAttribute("pcNotiServerTm");
			
			if(userVo == null || "denied".equals(permission) || !"Y".equals(sysPlocMap.get("pcNotiEnable"))){
				model.put("pcNotiMode", "deny");
				model.put("pcNotiServerTm", "");
				session.setAttribute("pcNotiServerTm", "");
				if(debug){
					LOGGER.warn("pcNoti"+(userVo==null ? "" : "("+userVo.getLginId()+":"+browser.getAgent()+")")
							+" - denied : "
							+("denied".equals(permission) ? " permission:denied" : ""));
				}
				return LayoutUtil.returnJson(model);
			} else if(sessionSeverTm==null){
				//DB 시간 조회
				String pcNotiServerTm = commonSvc.querySysdate(null);
				model.put("pcNotiMode", "init");
				model.put("pcNotiServerTm", pcNotiServerTm);
				model.put("pcNotiUid", Integer.toString(CRC32.hash(userVo.getUserUid().getBytes())));
				session.setAttribute("pcNotiServerTm", pcNotiServerTm);
				if(debug){
					LOGGER.warn("pcNoti"+(userVo==null ? "" : "("+userVo.getLginId()+":"+browser.getAgent()+")")
							+" - init (no session server time)");
				}
				return LayoutUtil.returnJson(model);
			} else if(clientServerTm==null || clientServerTm.isEmpty()){
				model.put("pcNotiMode", "init");
				model.put("pcNotiServerTm", sessionSeverTm);
				model.put("pcNotiUid", Integer.toString(CRC32.hash(userVo.getUserUid().getBytes())));
				session.setAttribute("pcNotiServerTm", sessionSeverTm);
				if(debug){
					LOGGER.warn("pcNoti"+(userVo==null ? "" : "("+userVo.getLginId()+":"+browser.getAgent()+")")
							+" - init (no client server time)");
				}
				return LayoutUtil.returnJson(model);
			} else if(!sessionSeverTm.equals(clientServerTm)){
				model.put("pcNotiMode", "init");
				model.put("pcNotiServerTm", sessionSeverTm);
				session.setAttribute("pcNotiServerTm", sessionSeverTm);
				model.put("pcNotiUid", Integer.toString(CRC32.hash(userVo.getUserUid().getBytes())));
				if(debug){
					LOGGER.warn("pcNoti"+(userVo==null ? "" : "("+userVo.getLginId()+":"+browser.getAgent()+")")
							+" - init (client/session - server time not mathched)");
				}
				return LayoutUtil.returnJson(model);
			}

			// DB 시간 조회
			String currServerTm = commonSvc.querySysdate(null);
			model.put("pcNotiServerTm", currServerTm);
			model.put("pcNotiUid", Integer.toString(CRC32.hash(userVo.getUserUid().getBytes())));
			session.setAttribute("pcNotiServerTm", currServerTm);
			
			// 개인 설정을 읽어 - List에 담음
			@SuppressWarnings("unchecked")
			Map<String, Map<String, ?>> userSetupMap = (Map<String, Map<String, ?>>)session.getAttribute("userSetupMap");
			Map<String, ?> psnPcNotiMap = userSetupMap.get("pcNotiMap");
			List<String> psnPcNotiList = new ArrayList<String>();
			for(String md : ptSysSvc.getPcNotiMds(sysPlocMap)){
				if(psnPcNotiMap==null || !"N".equals(psnPcNotiMap.get(md))){
					psnPcNotiList.add(md);
				}
			}
			// 개인 설정에 푸쉬 받는곳 없으면
			if(psnPcNotiList.isEmpty()){
				model.put("pcNotiMode", "return");
				if(debug){
					LOGGER.warn("pcNoti"+(userVo==null ? "" : "("+userVo.getLginId()+":"+browser.getAgent()+")")
							+" - receive no module messages");
				}
				return LayoutUtil.returnJson(model);
			}
			
			List<String> userUidList = new ArrayList<String>();
			
			userUidList.add(userVo.getUserUid());
			
			// 겸직자 - 원직 처리(메일)
			boolean mailOdurUid = false;
			if(!userVo.getUserUid().equals(userVo.getOdurUid()) && psnPcNotiList.contains("MAIL")){
				mailOdurUid = true;
				userUidList.add(userVo.getOdurUid());
			}
			
			// 결재 - 겸직 처리
			String[][] adurs = userVo.getAdurs();
			boolean apAdurMerg = false;
			if(adurs != null && psnPcNotiList.contains("AP")){
				// 결재 옵션
				Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, userVo.getCompId());
				if("Y".equals(optConfigMap.get("adurMergLst"))){
					// 겸직 통합 표시면
					apAdurMerg = true;
					for(String[] adur : adurs){
						if(!userUidList.contains(adur[1])) userUidList.add(adur[1]);
					}
				}
			}
			
			// 게시 - 회사 공지 처리
			boolean bbCompId = false;
			if(psnPcNotiList.contains("BB")){
				bbCompId = true;
				userUidList.add(userVo.getCompId());
			}
			
			// 데이터 조회
			PtPushMsgDVo ptPushMsgDVo = new PtPushMsgDVo();
			if(userUidList.size() == 1){
				ptPushMsgDVo.setUserUid(userVo.getUserUid());
			} else {
				ptPushMsgDVo.setUserUidList(userUidList);
			}
			ptPushMsgDVo.setMinIsuDt(clientServerTm);
			ptPushMsgDVo.setMaxIsuDt(currServerTm);
			if(debug){
				LOGGER.warn("pcNoti"+(userVo==null ? "" : "("+userVo.getLginId()+":"+browser.getAgent()+")")
						+" - query - isuDt: "+clientServerTm+" ~ "+currServerTm);
			}
			
			
//LOGGER.warn("Query : "+ptPushMsgDVo);
			@SuppressWarnings("unchecked")
			List<PtPushMsgDVo> ptPushMsgDVoList = (List<PtPushMsgDVo>)commonSvc.queryList(ptPushMsgDVo);
			
			if(ptPushMsgDVoList != null){
				
				String compId = userVo.getCompId();
				String userUid = userVo.getUserUid();
				String odurUid = userVo.getOdurUid();
				
				boolean adurBx = false;
				String title, mdRid;
				HashMap<String, String> mdMap = new HashMap<String, String>();
				
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				Map<String, String> map;
				for(PtPushMsgDVo storedPtPushMsgDVo : ptPushMsgDVoList){
					mdRid = storedPtPushMsgDVo.getMdRid();
					
					// 개인 설정 필터링 - 안 받겠다고 한 모듈 제외
					if(!psnPcNotiList.contains(mdRid)){
						continue;
					}
					
					if(userUid.equals(storedPtPushMsgDVo.getUserUid())){
						// userUid 같음
					} else if(mailOdurUid && "MAIL".equals(mdRid) && odurUid.equals(storedPtPushMsgDVo.getUserUid())){
						// 메일 - 원직
					} else if(bbCompId && "BB".equals(mdRid) && compId.equals(storedPtPushMsgDVo.getUserUid())){
						// 게시 - 회사공지
					} else if(apAdurMerg && "AP".equals(mdRid)){
						// 결재 - 겸직 통합 표시 대상
						adurBx = false;
						for(String bxId : ApConstant.ADUR_BXES){
							if(storedPtPushMsgDVo.getWebAuthUrl().endsWith(bxId)){
								adurBx = true;
								break;
							}
						}
						if(!adurBx){
							continue;
						}
					} else {
						// 기타 경우 제외 - 자원 알림 등
						continue;
					}
					
					map = new HashMap<String, String>();
					title = mdMap.get(mdRid);
					if(title == null){
						title = messageProperties.getMessage("pt.sysopt.pcNoti."+mdRid, request);
						mdMap.put(mdRid, title);
					}
					map.put("title", title);
					map.put("body", storedPtPushMsgDVo.getPushSubj());
					map.put("url", host+"/index.do?msgId="+storedPtPushMsgDVo.getPushMsgId());
					if(debug){
						LOGGER.warn("pcNoti"+(userVo==null ? "" : "("+userVo.getLginId()+":"+browser.getAgent()+")")
								+" "+map);
					}
					list.add(map);
				}
				
				if(!list.isEmpty()){
					model.put("list", list);
				}
				model.put("pcNotiMode", "return");
			}
			
		} catch (SQLException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.returnJson(model);
	}

	
	/** [팝업] 겸직자 사용자 결재 선택 */
	@RequestMapping(value = "/cm/ap/setApAddiUserPop")
	public String setApAddiUserPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		ptMainSvc.getMainApCntPop(request, userVo, model);
		
		return LayoutUtil.getJspPath("/cm/ap/setApAddiUserPop");
	}
}
