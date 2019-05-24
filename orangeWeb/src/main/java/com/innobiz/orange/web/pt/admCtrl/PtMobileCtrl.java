package com.innobiz.orange.web.pt.admCtrl;

import java.io.File;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtSysSetupDVo;

/** 모바일 설정 관리 */
@Controller
public class PtMobileCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtMobileCtrl.class);

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

//	/** 포털 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;

	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 로그인 페이지 설정 */
	@RequestMapping(value = "/pt/adm/mobile/setLoginPage")
	public String setLoginPage(HttpServletRequest request,
			//@Parameter(name="popYn", required=false) String popYn,
			ModelMap model, Locale locale) throws Exception {
		
		// 셋업분류ID
		//model.put("setupClsId", PtConstant.MB_MOB_LGIN);
		// 서버 설정 목록 조회
		Map<String, String> mobLginMap = ptSysSvc.getMobileLogin(locale.getLanguage());
		model.put("mobLginMap", mobLginMap);
		
		return LayoutUtil.getJspPath("/pt/adm/mobile/setLoginPage");
	}
	
	/** 로그인 페이지 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/mobile/transLoginPage")
	public String transLoginPage(HttpServletRequest request,
			ModelMap model, Locale locale) throws Exception {
		
		try{

			QueryQueue queryQueue = new QueryQueue();
			
			UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "pt");
			uploadHandler.upload();//업로드 파일 정보
			
			Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
			Map<String, File> fileMap = uploadHandler.getFileMap();//파일 리스트 Map
			
			DistHandler distHandler = distManager.createHandler("images/upload/pt/login", locale);
			
			String setupClsId = PtConstant.MB_MOB_LGIN;
			
			// 서버 설정 목록 조회
			Map<String, String> mobLginMap = ptSysSvc.getMobileLogin(locale.getLanguage());
			
			PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
			ptSysSetupDVo.setSetupClsId(setupClsId);
			queryQueue.delete(ptSysSetupDVo);
			
			// logoImgPath
			ptSysSetupDVo = new PtSysSetupDVo();
			ptSysSetupDVo.setSetupClsId(setupClsId);
			ptSysSetupDVo.setSetupId("logoImgPath");
			
			File imgFile = fileMap.get("logoImgFile");
			if(imgFile != null){
				String distPath = distHandler.addWebList(imgFile.getAbsolutePath());
				ptSysSetupDVo.setSetupVa(distPath);
			} else {
				ptSysSetupDVo.setSetupVa(mobLginMap.get("logoImgPath"));
			}
			queryQueue.insert(ptSysSetupDVo);
			
			// lginTitle
			ptSysSetupDVo = new PtSysSetupDVo();
			ptSysSetupDVo.setSetupClsId(setupClsId);
			ptSysSetupDVo.setSetupId("lginTitle");
			
			String lginTitle = paramMap.get("lginTitle");
			if(lginTitle!=null && !lginTitle.isEmpty()){
				ptSysSetupDVo.setSetupVa(lginTitle);
			} else {
				ptSysSetupDVo.setSetupVa(mobLginMap.get("lginTitle"));
			}
			queryQueue.insert(ptSysSetupDVo);
			
			// lginTitleColor
			ptSysSetupDVo = new PtSysSetupDVo();
			ptSysSetupDVo.setSetupClsId(setupClsId);
			ptSysSetupDVo.setSetupId("lginTitleColor");
			
			String lginTitleColor = paramMap.get("lginTitleColor");
			if(lginTitleColor!=null && !lginTitleColor.isEmpty()){
				ptSysSetupDVo.setSetupVa(lginTitleColor);
			} else {
				ptSysSetupDVo.setSetupVa(mobLginMap.get("lginTitleColor"));
			}
			queryQueue.insert(ptSysSetupDVo);
			
			if(imgFile != null){
				distHandler.distribute();
			}
			
			String dbTime = ptCacheExpireSvc.getDbTime();
			
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
			
			uploadHandler.removeTempDir();
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			LOGGER.error(message);
			// cm.msg.save.fail=저장에 실패하였습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.fail", request));
			e.printStackTrace();
		}
		
		return LayoutUtil.getResultJsp();
	}

	
	/** 해외 IP 차단 정책 - 설정 */
	@RequestMapping(value = {"/pt/adm/mobile/setForeignIpBlockingPlocPop", "/pt/adm/sys/setForeignIpBlockingPlocPop"})
	public String viewHeaderPop(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		if(!"U0000001".equals(userVo.getUserUid())){
			response.sendError(404);
			return null;
		}
		
		boolean forMobile = request.getRequestURI().indexOf("mobile")>0;
		
		// 해외 IP 정책 조회
		Map<String, String> foreignIpPloc = ptSysSvc.getForeignIpBlockingPloc(forMobile);
		model.addAllAttributes(foreignIpPloc);
		
		return LayoutUtil.getJspPath("/pt/adm/mobile/setForeignIpBlockingPlocPop");
	}
	
	/** IP 정책 설정 - 저장 */
	@RequestMapping(value = {"/pt/adm/mobile/transForeignIpBlockingPolc", "/pt/adm/sys/transForeignIpBlockingPolc"})
	public String transSecuPolc(HttpServletRequest request,
			@Parameter(name="chinese", required=false) String chinese,
			@Parameter(name="foreign", required=false) String foreign,
			ModelMap model) throws Exception {
		
		try{
			QueryQueue queryQueue = new QueryQueue();
			
			boolean forMobile = request.getRequestURI().indexOf("mobile")>0;
			String setupClsId = forMobile ? PtConstant.MB_FOREIGN_IP_PLOC : PtConstant.PT_FOREIGN_IP_PLOC;
			if(chinese!=null && !chinese.isEmpty()){
				PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
				ptSysSetupDVo.setSetupClsId(setupClsId);
				ptSysSetupDVo.setSetupId("chinese");
				ptSysSetupDVo.setSetupVa(chinese);
				queryQueue.store(ptSysSetupDVo);
			}
			if(foreign!=null && !foreign.isEmpty()){
				PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
				ptSysSetupDVo.setSetupClsId(setupClsId);
				ptSysSetupDVo.setSetupId("foreign");
				ptSysSetupDVo.setSetupVa(foreign);
				queryQueue.store(ptSysSetupDVo);
			}

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				model.put("todo", "parent.dialog.close('setForeignIpBlockingPlocDialog');");
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] 메세지 로그인 - 설정 */
	@RequestMapping(value = "/pt/adm/mobile/setMsgLginPop")
	public String setMsgLginPop(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		
		Map<String, String> mobMsgLginMap = ptSysSvc.getSysSetupMap(PtConstant.MB_MOB_MSG_LGIN, true);
		if(mobMsgLginMap!=null){
			if(mobMsgLginMap.get("autoTime")==null){
				mobMsgLginMap.put("autoTime", "12");
			}
			model.putAll(mobMsgLginMap);
		}
		return LayoutUtil.getJspPath("/pt/adm/mobile/setMsgLginPop");
	}
	/** 메세지 로그인 - 설정 저장 */
	@RequestMapping(value = "/pt/adm/mobile/transMsgLginPop")
	public String transMsgLginPop(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="autoTime", required=false) String autoTime,
			ModelMap model) throws Exception {

		try{
			QueryQueue queryQueue = new QueryQueue();
			
			if(autoTime!=null && !autoTime.isEmpty()){
				PtSysSetupDVo ptSysSetupDVo = new PtSysSetupDVo();
				ptSysSetupDVo.setSetupClsId(PtConstant.MB_MOB_MSG_LGIN);
				ptSysSetupDVo.setSetupId("autoTime");
				ptSysSetupDVo.setSetupVa(autoTime);
				queryQueue.store(ptSysSetupDVo);
			}

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				model.put("todo", "parent.dialog.close('setUseMsgLginDialog');");
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
}
