package com.innobiz.orange.web.wl.ctrl;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.wl.svc.WlAdmSvc;
import com.innobiz.orange.web.wl.svc.WlRescSvc;
import com.innobiz.orange.web.wl.utils.WlConstant;
import com.innobiz.orange.web.wl.vo.WlRescBVo;

@Controller
public class WlAdmEnvCtrl {
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(WlAdmTaskLogCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 관리 서비스 */
	@Autowired
	private WlAdmSvc wlAdmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
//	/** 시스템설정 서비스 */
//	@Autowired
//	private PtSysSvc ptSysSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "wlRescSvc")
	private WlRescSvc wlRescSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 환경설정 */
	@RequestMapping(value = "/wl/adm/env/setEnv")
	public String setEnv(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 환경설정 세팅
		wlAdmSvc.getEnvConfigMap(model, userVo.getCompId());
		
		// 일정종류 목록
		model.put("typCdList", WlConstant.LOG_LIST);
		
		// 항목 목록
		model.put("colList", WlConstant.COL_LIST);
		
		return LayoutUtil.getJspPath("/wl/adm/env/setEnv");
	}
	
	/** [히든프레임] 옵션관리 - 저장 */
	@RequestMapping(value = "/wl/adm/env/transEnv")
	public String transOpt(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			// 파라미터 맵
			Map<String,String> paramMap = wlAdmSvc.getParameterMap(request, "_", null);
			
			String[] keyList = WlConstant.COL_LIST;
			WlRescBVo wlRescBVo=null;
			for(String key : keyList){
				wlRescBVo=wlRescSvc.collectWlRescBVo(request, key, queryQueue);
				if(wlRescBVo!=null){
					paramMap.put(key+"RescId", wlRescBVo.getRescId());
				}
			}
			
			// 맵으로 설정 정보 저장
			wlAdmSvc.setSysSetupMap(WlConstant.SYS_CONFIG+userVo.getCompId(), queryQueue, true, paramMap, false);
			
			//ptSysSvc.setSysSetup(WlConstant.SYS_CONFIG+userVo.getCompId(), queryQueue, true, request);

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
			model.put("todo", "parent.location.replace(parent.location.href);");
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
			model.put("todo", "parent.location.replace(parent.location.href);");
		}
		
		return LayoutUtil.getResultJsp();
	}
}
