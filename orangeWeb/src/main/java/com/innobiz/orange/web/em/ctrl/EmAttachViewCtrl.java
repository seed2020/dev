package com.innobiz.orange.web.em.ctrl;

import java.io.File;
import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.em.utils.EmCmUtil;
import com.innobiz.orange.web.em.utils.EmConstant;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 파일 미리보기(문서뷰어) */
@Controller
public class EmAttachViewCtrl {
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
//	/** 메세지 처리용 프라퍼티 - 다국어 */
//	@Autowired
//	private MessageProperties messageProperties;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmAttachViewCtrl.class);
	
	/** 파일 미리보기 */
	@RequestMapping(value = {"/ap/box/attachView", "/bb/attachView","/bb/adm/attachView","/ct/attachView", 
			"/wb/attachView", "/mt/attachView", "/wc/attachView", "/dm/doc/attachView","/dm/adm/doc/attachView", "/wv/attachView"})
	public String attachView(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			String[] reqUrls = request.getRequestURI().split("/");
			if(reqUrls.length<3){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			String module = "".equals(reqUrls[0]) ? reqUrls[1] : reqUrls[0];
			emAttachViewSvc.convertPreview(request, model, userVo, module);
			
			// 모듈 파라미터 세팅
			model.put("module", module);
			// get 파라미터를 post 파라미터로 전달하기 위해
			model.put("paramEntryList", ParamUtil.getEntryMapList(request));
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			return LayoutUtil.getResultJsp();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
			return LayoutUtil.getResultJsp();
		}
				
		return LayoutUtil.getJspPath("/em/attachView", "Frm");
	}
	
	/** [AJAX] 파일 미리보기 */
	@RequestMapping(value = {"/ap/box/attachViewAjx","/bb/attachViewAjx","/bb/adm/attachViewAjx","/ct/attachViewAjx", 
			"/wb/attachViewAjx","/mt/attachViewAjx","/wc/attachViewAjx","/dm/attachViewAjx","/dm/attachViewAjx","/wv/attachViewAjx"})
	public String attachViewAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			String[] reqUrls = request.getRequestURI().split("/");
			if(reqUrls.length<3){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			String module = "".equals(reqUrls[0]) ? reqUrls[1] : reqUrls[0];
			emAttachViewSvc.convertPreview(request, model, userVo, module);
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
				
		return JsonUtil.returnJson(model);
	}
	
	/** [SCHEDULED] 미리보기 파일 삭제 스케줄링 */
	//@Scheduled(cron=" 0 0 0 * * *")//매일 0시에 초기화
	//@Scheduled(fixedDelay=20000)
	public void deletePreviewScheduled() {
		try {
			// 미리보기 HTML 소스 업로드 기본 경로
			String webCopyBaseDir = distManager.getWebCopyBaseDir();
			if (webCopyBaseDir == null) {
				distManager.init();
				webCopyBaseDir = distManager.getWebCopyBaseDir();
			}
			String path = webCopyBaseDir+File.separator+EmConstant.VIEWER+File.separator+EmConstant.PREVIEW;
			//System.out.println("path : "+path);
			
			EmCmUtil.deleteAllFiles(path.replace('\\', '/'));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Preview File Delete completed at " + new Timestamp(System.currentTimeMillis()).toString());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
}
