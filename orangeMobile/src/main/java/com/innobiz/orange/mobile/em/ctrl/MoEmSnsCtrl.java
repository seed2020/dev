package com.innobiz.orange.mobile.em.ctrl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.RC4;
import com.innobiz.orange.web.em.utils.EmConstant;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** SnS */
@Controller
public class MoEmSnsCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoEmSnsCtrl.class);
	
	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** [AJAX] SnS 조회 */
	@RequestMapping(value = {"/bb/getSnsInfoAjx","/bb/adm/getSnsInfoAjx"})
	public String getSnsInfoAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			String webDomain = svrEnvMap.get("webDomain");
			String url=request.getScheme()+"://"+webDomain;
			//if(!"80".equals(request.getServerPort())) link+=":"+request.getServerPort();
			url+=EmConstant.SNS_URL;
			
			boolean isLink=false;
			if(request.getRequestURI().startsWith("/bb")){
				String brdId = ParamUtil.getRequestParam(request, "brdId", true);
				String bullId = ParamUtil.getRequestParam(request, "bullId", true);
				
				// SNS 조회
				bbBullSvc.setSnsList(model, userVo.getCompId(), brdId, bullId, "Y");
				url+="/bb";
				url+="/"+RC4.getEncrypt(bullId);
				isLink=true;
			}
			if(!isLink){
				// cm.msg.notValidPage=파라미터가 잘못되었거나 보안상의 이유로 해당 페이지를 표시할 수 없습니다.
				throw new CmException("cm.msg.notValidPage", request);
			}
			url+=EmConstant.SNS_EXT;
			long startTime = System.currentTimeMillis();
			url+="?id="+startTime;
			model.put("url", url);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
}
