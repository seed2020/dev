package com.innobiz.orange.web.pt.ctrl;

import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.pt.secu.AuthCdDecider;
import com.innobiz.orange.web.pt.secu.IpChecker;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtMyMnuSvc;
import com.innobiz.orange.web.pt.svc.PtPltSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSsoSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.LoutUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtMnuGrpBVo;

/** 포털 공통 컨트롤러 - 포털 구성에 관련된 요청 처리(서비스그룹 구성페이지 등) */
@Controller
public class PtLoutCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** SSO 처리용 서비스 */
	@Autowired
	private PtSsoSvc ptSsoSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 포틀릿 관련 서비스 */
	@Autowired
	private PtPltSvc ptPltSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtMyMnuSvc ptMyMnuSvc;
	
	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
	@Autowired
	private IpChecker ipChecker;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;

	/** 컨텍스트 프로퍼티 */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
	/** 포털 서비스그룹 조회(포틀릿 포함) */
	@RequestMapping(value = {"/pt/lout/", "/pt/lout/index"})
	public String index(HttpServletRequest request,
			@RequestParam(value = "mnuGrpId", required = false) String mnuGrpId,
			ModelMap model) throws Exception {
		
		// RSA 암호화 스크립트 추가, 에디터 사용
		model.put("JS_OPTS", new String[]{"pt.rsa","editor"});
		
		UserVo userVo = LoginSession.getUser(request);
		PtMnuGrpBVo ptMnuGrpBVo = mnuGrpId==null ? null : ptLoutSvc.getMnuGrpByGrpId(mnuGrpId, userVo.getLangTypCd());
		
		// 포틀릿레이아웃코드 - FREE:1단자유구성, D2R37:2단 (3:7), D2R46:2단 (4:6), D2R55:2단 (5:5), D2R64:2단 (6:4), D2R73:2단 (7:3), D3R111:3단 (1:1:1), D3R112:3단 (1:1:2), D3R121:3단 (1:2:1), D3R211:3단 (2:1:1), D3R221:3단 (2:2:1), D3R212:3단 (2:1:2)
		String pltLoutCd = null;
		
		if(ptMnuGrpBVo!=null){
			
			model.put("ptMnuGrpBVo", ptMnuGrpBVo);
			
			// 관리자 여부
			boolean isAdmin = ArrayUtil.isInArray(userVo.getAdminAuthGrpIds(), PtConstant.AUTH_SYS_ADMIN)
					|| ArrayUtil.isInArray(userVo.getAdminAuthGrpIds(), PtConstant.AUTH_ADMIN);
			
			// 외부망 권한 적용
			boolean isExAuth = ipChecker.isExAuth(userVo);
			
			// 메뉴에 어떤 권한이 있는지 결정하는 객체
			AuthCdDecider authCdDecider = !isExAuth && isAdmin ? null : ptSecuSvc.getAuthCdDecider(userVo.getCompId(),
					userVo.isInternalIp() ? PtConstant.AUTH_IP_IN : PtConstant.AUTH_IP_EX);
			
			// 나의 메뉴 여부
			boolean isMyMnu = PtConstant.MNU_GRP_REF_MY.equals(ptMnuGrpBVo.getMdRid());
			if(isMyMnu){
				
				Map<String, String> myMnuMap = ptMyMnuSvc.getMyMnuSetup(request, userVo.getUserUid());
				if(!"Y".equals(myMnuMap.get("useMnu"))){
					model.put("UI_PORTLET", Boolean.TRUE);// 왼쪽 메뉴 표시영역 안보이게 하기
				}
				pltLoutCd = myMnuMap.get("pltLoutCd");
				
				ptPltSvc.setPltZoneList(mnuGrpId, pltLoutCd, userVo.getLangTypCd(), model,
						authCdDecider, userVo.getUserAuthGrpIds(), userVo.getUserUid());
			} else {
				// 메뉴그룹구분코드 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임
				String mnuGrpTypCd = ptMnuGrpBVo==null ? null : ptMnuGrpBVo.getMnuGrpTypCd();
				if("02".equals(mnuGrpTypCd)){
					model.put("UI_PORTLET", Boolean.TRUE);// 왼쪽 메뉴 표시영역 안보이게 하기
				}
				
				pltLoutCd = ptMnuGrpBVo.getPltLoutCd();
				
				ptPltSvc.setPltZoneList(mnuGrpId, pltLoutCd, userVo.getLangTypCd(), model,
						authCdDecider, userVo.getUserAuthGrpIds(), null);
			}
			
			if(pltLoutCd==null || "FREE".equals(pltLoutCd)){
				return LayoutUtil.getJspPath("/pt/lout/indexFree");
			} else {
				model.put("pltLoutCd", pltLoutCd);
				ptPltSvc.setPltZone(pltLoutCd, model);
				return LayoutUtil.getJspPath("/pt/lout/indexDiv");
			}
		}

		return LayoutUtil.getJspPath("/pt/lout/indexFree");
	}
	
	/** 포털 외부URL 메뉴그룹 조회 */
	@RequestMapping(value = "/pt/lout/viewFrame")
	public String viewFrame(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="mnuGrpId", required=false) String mnuGrpId,
			@RequestParam(value="mailRd", required=false) String mailRedirect,
			ModelMap model) throws Exception {
		
		// 캐쉬 방지
		response.setHeader("cache-control","no-store"); // http 1.1   
		response.setHeader("Pragma","no-cache"); // http 1.0   
		response.setDateHeader("Expires",0); // proxy server 에 cache방지. 
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = userVo.getLangTypCd();
		
		// 통합 검색에서 - 메일제목 클릭 - 메일 보기, 메일박스 클릭 - 메일 박스 목록 보기
		if(mailRedirect!=null && !mailRedirect.isEmpty()){
			
			if("U0000001".equals(userVo.getUserUid())){
				return LayoutUtil.getJspPath("/cm/error/error901", "Frm");
			}
			
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			String mailDomain = svrEnvMap.get("mailCall");
			
			String name;
			String[] skips = {"menuId", "mnuGrpId", "mailRd"};
			StringBuilder builder = new StringBuilder(512);
			builder.append("http://").append(mailDomain).append(mailRedirect);
			
			boolean first = true;
			Enumeration<String> names = request.getParameterNames();
			while(names.hasMoreElements()){
				name = names.nextElement();
				if(!ArrayUtil.isInArray(skips, name)){
					if(first){
						builder.append('?');
						first = false;
					} else {
						builder.append('&');
					}
					builder.append(name).append('=').append(URLEncoder.encode(request.getParameter(name), "UTF-8"));
				}
			}
			
			// SSO 정보 생성
			String encrypted = ptSsoSvc.createOnetime(userVo);
			builder.append("&onetime=").append(encrypted);
			
			String frameUrl = builder.toString();
			boolean useSSL = request.isSecure();
			if(useSSL){
				frameUrl = ptSysSvc.toZMailSslUrl(frameUrl);
			}
			
			model.put("frameUrl", frameUrl);
			
		} else {
			String frameUrl = ptLoutSvc.getMnuGrpUrlByMnuGrpId(mnuGrpId, langTypCd);
			if(frameUrl==null){
				//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
				String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
				throw new CmException(msg);
			}
			
			if("U0000001".equals(userVo.getUserUid()) && frameUrl.indexOf("/zmail/")>=0){
				return LayoutUtil.getJspPath("/cm/error/error901", "Frm");
			}
			
			// 메뉴에 등록된 치환 변수를 변환함
			frameUrl = LoutUtil.converTypedParam(frameUrl, userVo);
			
			// SSL 이면
			if(request.isSecure()){
				// 메일용 SSL URL로 변환
				frameUrl = ptSysSvc.toZMailSslUrl(frameUrl);
			}
			
			// SSO 정보 생성
			String encrypted = ptSsoSvc.createOnetime(userVo);
			frameUrl = frameUrl+(frameUrl.indexOf('?')>0?'&':'?')+"onetime="+encrypted;
			
			model.put("frameUrl", frameUrl);
		}
		
		//왼쪽메뉴 표시안함 처리
		model.put("UI_FRAME", Boolean.TRUE);
		return LayoutUtil.getJspPath("/pt/lout/viewFrame");
	}
	
	/** 포털 외부URL 메뉴 조회 */
	@RequestMapping(value = "/pt/lout/viewMenu")
	public String viewMenu(HttpServletRequest request,
			@Parameter(name="mnuId", required=false) String mnuId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = userVo.getLangTypCd();
		String frameUrl = ptLoutSvc.getMnuUrlByMnuId(mnuId, langTypCd);
		if(frameUrl==null){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			throw new CmException(msg);
		}

		// 메뉴에 등록된 치환 변수를 변환함
		frameUrl = LoutUtil.converTypedParam(frameUrl, userVo);
		
		// SSO 정보 생성
		String encrypted = ptSsoSvc.createOnetime(userVo);
		frameUrl = frameUrl+(frameUrl.indexOf('?')>0?'&':'?')+"onetime="+encrypted;
		
		model.put("frameUrl", frameUrl);
		
		return LayoutUtil.getJspPath("/pt/lout/viewMenu");
	}
}
