package com.innobiz.orange.web.em.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.em.svc.MessengerSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 메신저 [관리자] */
@Controller
public class EmMsgrCtrl {		
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmMsgrCtrl.class);
	
	/** 메신저 서비스 */
	@Resource(name = "emMessengerSvc")
	private MessengerSvc messengerSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 메신저 알림 발송 화면 */
	@RequestMapping(value = "/em/adm/setMsgNotice")
	public String setUcNotice(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 에디터 사용
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		model.addAttribute("isLinux", ServerConfig.IS_LINUX);
		return LayoutUtil.getJspPath("/em/adm/setMsgNotice");
	}
	
	/** 메신저 알림 발송 */
	@RequestMapping(value = "/em/adm/transMsgNotice")
	public String transMsgNotice(HttpServletRequest request,
			@RequestParam(value="recvId",required=true) String recvId,
			@RequestParam(value="sendId",required=false) String sendId,
			@RequestParam(value="subj",required=true) String subj,
			@RequestParam(value="catNm",required=true) String catNm,
			@RequestParam(value="url",required=false) String url,
			@RequestParam(value="contents",required=false) String contents,
			@RequestParam(value="msgKey",required=false) String msgKey,
			@RequestParam(value="osTyp",required=false) String osTyp,
			ModelMap model) throws Exception {
		
		//관리자 (사용자 권한 체크)
		checkUserAuth(request, "A", null);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		try{
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			
			if(sendId == null || sendId.isEmpty()) sendId = userVo.getUserUid();
			
			String msgCode = null;
			if(recvId.split(",").length > 1) msgCode = messengerSvc.sendNotice(recvId.split(","), sendId, catNm, subj, contents, url, msgKey);
			else msgCode = messengerSvc.sendNotice(recvId, sendId, catNm, subj, contents, url, msgKey);
			
			String msgKeyCode = "cm.msg.save.success";
			model.put("todo", "parent.location.replace('" + listPage + "');");
			if( msgCode == null || !"200".equals(msgCode)){
				msgKeyCode = "cm.msg.save.fail";// cm.msg.save.fail=저장에 실패하였습니다.
			}
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage(msgKeyCode, request));
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}
					
		return LayoutUtil.getResultJsp();
	}
	
	/** 사용자 권한 체크 */
	private void checkUserAuth(HttpServletRequest request, String auth, String regrUid) throws CmException {
		if (!SecuUtil.hasAuth(request, auth, regrUid)) {
			// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
			throw new CmException("cm.msg.errors.403", request);
		}
	}
}
