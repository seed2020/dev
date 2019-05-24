package com.innobiz.orange.web.em.ctrl;

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
import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.RC4;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmSnsSvc;
import com.innobiz.orange.web.em.utils.EmConstant;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** SnS */
@Controller
public class EmSnsCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmSnsCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** SNS 서비스 */
	@Resource(name = "emSnsSvc")
	private EmSnsSvc emSnsSvc;
	
	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 설정 - 조회 */
	@RequestMapping(value = "/bb/adm/setEnvPop")
	public String setEnvPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 환경설정
		Map<String, String> envConfigMap = emSnsSvc.getEnvConfigMap(null, userVo.getCompId());
		model.put("envConfigMap", envConfigMap);
				
		return LayoutUtil.getJspPath("/em/adm/setEnvPop");
	}
	
	/** SNS 설정 - 저장 */
	@RequestMapping(value = "/bb/adm/transEnv")
	public String transSchdlSetup(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			ptSysSvc.setSysSetup(EmConstant.EM_SYS_CONFIG+userVo.getCompId(), queryQueue, true, request);

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] SnS 조회 */
	@RequestMapping(value = {"/bb/getSnsInfoAjx","/bb/adm/getSnsInfoAjx", "/cm/bb/getSnsInfoAjx"})
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
			if(request.getRequestURI().indexOf("bb")>0){
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
