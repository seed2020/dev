package com.innobiz.orange.web.pt.ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.LicenseException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.sso.SsoConstant;
import com.innobiz.orange.web.pt.svc.PtSsoSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;

/** SSO 처리용 */
@Controller
public class PtSsoCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtSsoCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** SSO 처리용 서비스 */
	@Autowired
	private PtSsoSvc ptSsoSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** WAS SSO Key 동기화 */
	@RequestMapping(value = "/cm/sso/syncOnetime")
	public String syncOnetime( HttpServletRequest request,
			@RequestParam(value = "odurUid", required = false) String odurUid,
			@RequestParam(value = "skin", required = false) String skin,
			@RequestParam(value = "lang", required = false) String lang,
			@RequestParam(value = "onetime", required = false) String onetime,
			HttpServletResponse response,
			ModelMap model) throws LicenseException, IOException{
		
		String userAgent = request.getHeader("User-Agent");
		if(userAgent==null || !userAgent.startsWith(SsoConstant.USER_AGENT)){
			response.sendError(403);
			return null;
		}
		
		if(		odurUid==null || odurUid.isEmpty()
			||	skin==null || skin.isEmpty()
			||	lang==null || lang.isEmpty()
			||	onetime==null || onetime.isEmpty()){
			response.sendError(403);
			return null;
		}
		
		try {
			ptSsoSvc.syncOnetime(odurUid, skin, lang, onetime);
		} catch(Exception e){
			response.sendError(403);
			return null;
		}
		
		return null;
	}
	
	/** 원타임ID 로 사용자 정보 조회 */
	@RequestMapping(value = "/cm/sso/getOnetime")
	public String getOnetime(HttpServletRequest request,
			@RequestParam(value = "secuData", required = true) String secuData,
			HttpServletResponse response, ModelMap model) throws IOException {
		
		String userAgent = request.getHeader("User-Agent");
		if(userAgent==null || !userAgent.startsWith(SsoConstant.USER_AGENT)){
			response.sendError(403);
			return null;
		}
		
		try{
			if(secuData != null && !secuData.isEmpty()){
				String encrypted = ptSsoSvc.getOnetimeData(secuData);
				if(encrypted!=null){
					response.getOutputStream().write(encrypted.getBytes());
				}
				return null;
			}
		} catch(Exception ignore){
			ignore.printStackTrace();
		}
		
		response.sendError(403);
		return null;
	}
	
	/** [AJAX]ERP SSO URL 구함 */
	@RequestMapping(value = "/cm/sso/getErpTkUrlAjx")
	public String getHomeUrlAjx(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws SQLException {
		
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null){
			model.put("lginUrl", PtConstant.URL_LOGIN);
			return LayoutUtil.returnJson(model);
		}
		
		String setupClsId = PtConstant.PT_ERP_SSO+userVo.getCompId();
		Map<String, String> erpSsoMap = ptSysSvc.getSysSetupMap(setupClsId, true);
		
		// 서버 설정 목록 조회
		String erpSsoUserId = "refVa2".equals(erpSsoMap.get("erpSsoUserId")) ? "refVa2" : "refVa1";
		String erpTkUrl = erpSsoMap.get("erpTkUrl");
		String erpSsoUrl = erpSsoMap.get("erpSsoUrl");
		
		if(erpTkUrl==null || erpTkUrl.isEmpty() || erpSsoUrl==null || erpSsoUrl.isEmpty()){
			LOGGER.error("ERP SSO - No Config");
			// pt.msg.noCfg=설정 정보를 확인 할 수 없습니다.
			model.put("message", messageProperties.getMessage("pt.msg.noCfg", request));
			return LayoutUtil.returnJson(model);
		}
		
		// userId 확인
		String userId = null;
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setOdurUid(userVo.getOdurUid());
		orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
		if(orOdurBVo!=null){
			userId = "refVa2".equals(erpSsoUserId) ? orOdurBVo.getRefVa2() : orOdurBVo.getRefVa1();
		}
		if(userId==null || userId.isEmpty()){
			LOGGER.error("ERP SSO - No "+erpSsoUserId+" - uid:"+userVo.getUserUid()+" lginId:"+userVo.getLginId());
			// pt.login.noUser=사용자 정보를 확인 할 수 없습니다.
			model.put("message", messageProperties.getMessage("pt.login.noUser", request));
			return LayoutUtil.returnJson(model);
		}
		
		// ERP Ticket 확인
		String tk = null;
		HttpClient http = new HttpClient();
		try {
			String ticketString = http.sendGet(erpTkUrl+"&UserID="+userId, null, "EUC-KR");
			if(ticketString!=null) tk = ticketString.trim();
			
			if(tk==null){
				LOGGER.error("ERP SSO - Empty result - "+erpSsoUserId+":"+userId+" uid:"+userVo.getUserUid()+" lginId:"+userVo.getLginId());
				//pt.msg.noErpUsr=ERP 사용자를 확인 할 수 없습니다.
				model.put("message", messageProperties.getMessage("pt.msg.noErpUsr", request));
				return LayoutUtil.returnJson(model);
			}
		} catch(Exception e){
			LOGGER.error("ERP SSO - Error result - "+erpSsoUserId+":"+userId+" uid:"+userVo.getUserUid()+" lginId:"+userVo.getLginId()+"  "+e.getMessage());
			// pt.msg.errErp=ERP 응답 오류
			model.put("message", messageProperties.getMessage("pt.msg.errErp", request)+" : "+e.getMessage());
			return LayoutUtil.returnJson(model);
			
		}
		
		model.put("url", erpSsoUrl+"&uid="+userId+"&tk="+tk);
		return LayoutUtil.returnJson(model);
	}
	
}
