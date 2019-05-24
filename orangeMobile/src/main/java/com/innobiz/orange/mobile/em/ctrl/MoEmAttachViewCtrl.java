package com.innobiz.orange.mobile.em.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 파일 미리보기(문서뷰어) */
@Controller
public class MoEmAttachViewCtrl {
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MoEmAttachViewCtrl.class);
	
	/** 파일 미리보기 */
	@RequestMapping(value = {"/ap/box/attachViewSub","/bb/attachViewSub","/bb/adm/attachViewSub","/ct/attachViewSub", 
			"/wb/attachViewSub","/mt/attachViewSub","/wc/attachViewSub","/dm/attachViewSub","/dm/attachViewSub","/wv/attachViewSub"})
	public String attachViewSub(HttpServletRequest request,
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
			model.put("paramEntryList", ParamUtil.getEntryMapList(request, "noCache"));
			
			model.put("UI_TITLE", messageProperties.getMessage("pt.docViewer.title", request));// pt.docViewer.title=문서뷰어
			/*if(model.containsKey("dispNm")){
				model.put("UI_TITLE", model.get("dispNm"));
			}*/
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			return MoLayoutUtil.getResultJsp();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
			return MoLayoutUtil.getResultJsp();
		}
				
		return MoLayoutUtil.getJspPath("/em/attachViewSub");
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
	
}
