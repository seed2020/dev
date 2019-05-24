package com.innobiz.orange.web.pt.secu;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.exception.InternetAccessException;
import com.innobiz.orange.web.cm.exception.LicenseLimitException;
import com.innobiz.orange.web.cm.exception.SecuException;
import com.innobiz.orange.web.cm.exception.SessionIpException;
import com.innobiz.orange.web.cm.utils.EscapeUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoHolder;
import com.innobiz.orange.web.pt.utils.LoutUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

/**
 * 권한 및 상단, 좌측 메뉴를 처리하기 위한 필터 <br/>
 *  - 공통적으로 사용하는 값을 request 에 담는 역할 도 함 : 디폴트 세팅 처리
 * 
 * */
public class SecurityFilter implements Filter {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(SecurityFilter.class);
	
	private String[] ruleOutPrefix = null;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		String uri = null;
		VoHolder.clear();
		
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		
		if(ServerConfig.IS_LOC){
			KeepAliver.processKeepAlive(request, response);
		}
		
		try {
			
			// browser 정보 세팅
			Browser.setBrowser(request);
			
			SecuBridge ins = SecuBridge.ins;
			// SSO 체크
			if(ins!=null && ins.ssoClientSvc!=null){
				if(SecuBridge.ins.ssoClientSvc.checkSso(request, response)){
					// response.sendRedirect 된것은 true 리턴함
					return;
				}
			}
			
			// 디폴트 세팅
			// _uri : 호출된 URI
			// _skin : 스킨경로 - 이미지 경로 결정용
			// _cxPth : 컨텍스트 패스
			// _lang : 언어구분코드
			// _langTypCdListByCompId : 회사별 세팅된 언어
			// _logoImg : 로고 이미지
			// _bgImg : 기본레이아웃의 배경 이미지 
			
			// uri
			uri = request.getRequestURI();
			if(uri.indexOf("/sns/")>=0){
				chain.doFilter(request, response);
				return;
			}
			
			//boolean skipSecu = uri.startsWith("/cm/") || uri.startsWith("/editor/") || uri.startsWith("/ws/");
			boolean skipSecu = uri.startsWith("/cm/") || uri.startsWith("/editor/");

			request.setAttribute("_uri", uri);
			UserVo userVo = LoginSession.getUser(request);
			
			// lang type cd
			String langTypCd = SessionUtil.getLangTypCd(request);
			request.setAttribute("_lang", langTypCd);
			
			// context path
			String _cxPth = request.getContextPath();
			request.setAttribute("_cxPth", _cxPth);
			
			// skin
			String _skin = userVo==null ? "blue" : userVo.getSkin();
			request.setAttribute("_skin", _skin);
			
			// 미리보기 경로면 - 미리보기 권한 체크
			if(uri.startsWith("/viewer/preview")){
				if(SecuBridge.ins!=null && SecuBridge.ins.previewAuthSvc!=null
						&& SecuBridge.ins.previewAuthSvc.hasAuth(request)){
					chain.doFilter(request, response);
					return;
				} else {
					response.setStatus(403);
					return;
				}
			}
			if(uri.indexOf("/self/")>=0){
				if("127.0.0.1".equals(IpChecker.ins.getIp(request))){
					chain.doFilter(request, response);
					return;
				}
			}
			
			if(uri.indexOf("/support/app/")>=0){
				chain.doFilter(request, response);
				return;
			}
			
			if(uri.equals("/index.do")){
				chain.doFilter(request, response);
				return;
			}
			
			if(ServerConfig.IS_LOC && (// 로컬 개발 환경의 웹서버 역할 - 패스함
					uri.startsWith("/images/") || uri.startsWith("/js/") || uri.startsWith("/css/")
					|| uri.startsWith("/editor/") || uri.startsWith("/sample/") || uri.endsWith(".ico") )){
				// do not check security
				chain.doFilter(request, response);
				return;
			} else {
				
				if(userVo==null){
					if(!skipSecu){
						if(!SecuBridge.ins.ptSysSvc.hasAdminPw()){
							String secuSessionCode = StringUtil.getNextHexa();
							request.getSession(true).setAttribute("secuSessionCode", secuSessionCode);
							request.setAttribute("secuSessionCode", secuSessionCode);
							// 관리자 비밀번호 세팅 - 설치 후 최초 한번
							request.getRequestDispatcher("/WEB-INF/jsp/pt/adm/sys/setAdmPw.jsp").forward(request,response);
							return;
						}
						int pageType = LoutUtil.getPageType(uri);
						//pt.logout.timeout=로그인 세션이 종료 되었습니다.
						String message = pageType==LoutUtil.TYPE_PAGE ? null :
							SecuBridge.ins==null || SecuBridge.ins.messageProperties==null ? null :
								SecuBridge.ins.messageProperties.getMessage("pt.logout.timeout", request);
						toLogin(request, response, pageType, message);
						return;
					}
				} else {
					
					// 시스템 차단 여부 조회
					if(!uri.startsWith("/cm/") && ins.ptSysSvc.denyUseOfHaltedSystem(userVo.getUserUid())){
						int pageType = LoutUtil.getPageType(uri);
						toLogin(request, response, pageType, null);
						return;
					}
					
					// 로고 이미지, 기본레이아웃의 배경 이미지
					if(ins!=null && ins.ptSysSvc!=null){
						ins.ptSecuSvc.getLoutCatId(userVo, ins.ptSysSvc.getLayoutSetup());
						Map<String, String> layout = ins.ptSysSvc.getSkinImage(userVo.getCompId(), _cxPth);
						
						//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃
						if(!"I".equals(userVo.getLoutCatId())){
							// 로고 이미지 : _logoImg
							request.setAttribute("_logoImg", layout.get(_skin+"BascLogo"));
							
							// 기본레이아웃의 배경 이미지 : _bgImg
							request.setAttribute("_bgImg", layout.get(_skin+"BascBg"));
						} else {
							// 로고 이미지 : _logoImg
							request.setAttribute("_logoImg", layout.get(_skin+"IconLogo"));
						}
					}
					
					if(ins!=null && ins.ptCmSvc!=null){
						List<PtCdBVo> langTypCdList = ins.ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
						// 회사별 언어코드
						request.setAttribute("_langTypCdListByCompId", langTypCdList);
					}
					
					// 복사 방지
					if(uri.length()>3 && uri.charAt(0)=='/' && uri.charAt(3)=='/' && userVo!=null && !"U0000001".equals(userVo.getUserUid())){
						String module = uri.substring(1, 3);
						Map<String, String> antiCopyMap = ins.ptSysSvc.getSysSetupMap(PtConstant.PT_ANTI_COPY+userVo.getCompId(), true);
						if(antiCopyMap.get(module) != null){
							request.setAttribute("noCopy", Boolean.TRUE);
						}
					}
					
				}
				
				if(!skipSecu){
					
					// 최종 로그인 세션인지 체크
					if(!LastSessionChecker.isLastSession(userVo.getOdurUid(), request.getSession())){
						request.getSession().invalidate();
						//pt.msg.dupLogin=동일한 계정으로 로그인하여 로그인이 차단 되었습니다.
						String message = SecuBridge.ins==null || SecuBridge.ins.messageProperties==null ? null
								: SecuBridge.ins.messageProperties.getMessage("pt.msg.dupLogin", request);
						int pageType = LoutUtil.getPageType(uri);
						toLogin(request, response, pageType, message);
						return;
					}
					
					try {
						if(ServerConfig.IS_LOC) checkMenuId(request, uri);
						boolean ruleout = uri.equals("/") ? true : shouldRuleOut(uri);
						// 권한 체크 및 상단/좌측 메뉴 설정
						ins.ptSecuSvc.processSecurity(request, uri, userVo, ruleout);
					} catch(LicenseLimitException e){
						response.sendRedirect("/cm/er"+"ror/li"+"cense.do");
					} catch(InternetAccessException e){
						LOGGER.error(
								"[Access Admin from outside] - "+userVo.getCompId()+"/"+userVo.getUserUid()
									+"/"+userVo.getUserNm()+" - curr Ip : "+e.getIp());
						request.setAttribute("message", e.getMessage());
						request.setAttribute("UI_TITLE", "Security Policy");
						request.setAttribute("togo", PtConstant.URL_LOGIN);
						request.getRequestDispatcher(LayoutUtil.getErrorJsp(0)).forward(request,response);
						return;
					} catch(SessionIpException e){
						LOGGER.error(
								"[Session IP] - "+userVo.getCompId()+"/"+userVo.getUserUid()
									+"/"+userVo.getUserNm()+" - login Ip : "+userVo.getLginIp()+"  curr Ip : "+e.getIp());
						request.getSession().invalidate();
						request.setAttribute("message", e.getMessage());
						request.setAttribute("UI_TITLE", "Security Policy");
						request.setAttribute("togo", PtConstant.URL_LOGIN);
						request.getRequestDispatcher(LayoutUtil.getErrorJsp(0)).forward(request,response);
						return;
					} catch(SecuException e){
						Throwable root = getRootThrowable(e);
						Integer pageType = (Integer)request.getAttribute("_pageType");
						int typNo = pageType==null ? 0 : pageType.intValue();
						if(typNo != LoutUtil.TYPE_PLT_IFRM){
							LOGGER.error(
									"[Access denied]\n"+
									"compId/userUid/name - url : "+userVo.getCompId()+"/"+userVo.getUserUid()
										+"/"+userVo.getUserNm()+" - "+uri+"\n"+root.getStackTrace()[0]);
						}
						request.setAttribute("exception", root);
						request.getRequestDispatcher(LayoutUtil.getErrorJsp(403)).forward(request,response);
						return;
					} catch(IOException e){
						throw new IOException(getRootThrowable(e));
					} catch(Exception e){
						Throwable root = getRootThrowable(e);
						StringBuilder builder = new StringBuilder(256);
						
						String msg = root.getMessage();
						if(msg==null) msg = root.getClass().getCanonicalName();
						builder.append(msg).append(" - url : ").append(uri);
						
						StackTraceElement[] stacks = root.getStackTrace();
						int i, size = Math.min(8, stacks.length);
						for(i=0;i<size;i++) builder.append('\n').append(stacks[i]);
						LOGGER.error(builder.toString());
						request.setAttribute("exception", e);
						request.getRequestDispatcher(LayoutUtil.getErrorJsp(500)).forward(request,response);
						return;
					}
				}
			}
			
			// 패스워드 강제 변경
			String forceMove = (String)request.getSession().getAttribute("FORCE_MOVE");
			if(forceMove!=null){
				if(!uri.startsWith("/cm/")
						&& !uri.startsWith(PtConstant.URL_SET_PW)
						&& !uri.startsWith("/pt/psn/pw/transPw.do") ){
					response.sendRedirect(forceMove);
				}
			} else {
				if(ins.ptSysSvc.getSysPlocMap().get("namoEditorEnable")!=null){
					request.setAttribute("namoEditorEnable", Boolean.TRUE);
				}
			}
			chain.doFilter(request, response);
		} catch(IOException e){
			Throwable root = getRootThrowable(e);
			if(root instanceof LicenseLimitException){
				response.sendRedirect("/cm/er"+"ror/li"+"cense.do");
			} else {
				throw new IOException(root);
			}
		} catch(ServletException e){
			Throwable root = getRootThrowable(e);
			String msg = root.getMessage();
			// for spring-path-ctrl-method
			if(msg!=null && msg.indexOf(".jsp")>0 && msg.endsWith("not found")){
				LOGGER.error(EscapeUtil.unescapeHTML(msg)+" - url : "+uri+"\n"+root.getStackTrace()[0]);
				request.setAttribute("exception", root);
				try{
					request.setAttribute("NO_MENU", Boolean.TRUE);
					request.getRequestDispatcher(LayoutUtil.getErrorJsp(404)).include(request, response);
				} catch(Exception ignore){
					//request.setAttribute("NO_MENU", Boolean.TRUE);
					//request.getRequestDispatcher(LayoutUtil.getErrorJsp(404)).include(request, response);
				}
				return;
			}
			throw new ServletException(root);
		} catch(Exception e){
			throw new ServletException(getRootThrowable(e));
		}
	}
	
	/** 로그인 페이지로 이동 처리 */
	private void toLogin(HttpServletRequest request, HttpServletResponse response, int pageType, String message) throws IOException, ServletException{
		if(pageType == LoutUtil.TYPE_PAGE){
			if(message!=null){
				request.getSession().setAttribute("loginMessage", message);
			}
			response.sendRedirect(PtConstant.URL_LOGIN);
			return;
		} else {
			if(pageType == LoutUtil.TYPE_IFRM || pageType == LoutUtil.TYPE_PLT
					|| pageType == LoutUtil.TYPE_PLT_IFRM || pageType == LoutUtil.TYPE_TRANS){
				request.setAttribute("todo", "top.location.href = '"+PtConstant.URL_LOGIN+"';");
				if(message!=null) request.setAttribute("message", message);
				request.getRequestDispatcher("/WEB-INF/jsp"+LayoutUtil.getResultJsp()+".jsp").forward(request,response);
				return;
			} else if(pageType == LoutUtil.TYPE_POP || pageType == LoutUtil.TYPE_AJAX){
				ModelMap model = new ModelMap();
				model.put("message", message==null ? "" : message);
				model.put("logout", Boolean.TRUE);
				request.setAttribute("jsonString", JsonUtil.toJson(model));
				request.getRequestDispatcher("/WEB-INF/jsp/cm/result/toJsonObject.jsp").forward(request,response);
				return;
			}
		}
		// 로그인 페이지로 리다이렉트
		response.sendRedirect(PtConstant.URL_LOGIN);
		return;
	}
	
	/** 최상위  Throwable 리턴*/
	private Throwable getRootThrowable(Exception e){
		Throwable parent=null, root=e;
		while((parent = root.getCause())!=null) root = parent;
		return root;
	}
	
	/** 메뉴ID 체크 */
	private void checkMenuId(HttpServletRequest request, String uri){
		if(uri.endsWith(".do") && !"/sh/index.do".equals(uri)){
			String menuId = request.getParameter("menuId");
			if(menuId == null || menuId.isEmpty()){
				if(!shouldRuleOut(uri)){
					LOGGER.warn("parameter \"menuId\" required ! - url : "+uri);
				}
			} else {
				request.setAttribute("menuId", menuId);
			}
		}
	}
	
	@Override
	public void destroy() {
	}

	/** 제외대상여부 확인 */
	public boolean shouldRuleOut(String uri){
		if(ruleOutPrefix!=null){
			for(String prefix : ruleOutPrefix){
				if(uri.startsWith(prefix)) return true;
			}
		}
		return false;
	}
	
	/** 제외대상 prefix 설정 */
	public void setRuleOutPrefix(List<String> list){
		int i, size = list==null ? 0 : list.size();
		if(size>0){
			ruleOutPrefix = new String[size];
			for(i=0;i<size;i++){
				ruleOutPrefix[i] = list.get(i);
			}
		} else {
			ruleOutPrefix = null;
		}
	}
}
