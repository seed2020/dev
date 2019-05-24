package com.innobiz.orange.mobile.em.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;

/** 기타 모듈 정의(푸쉬앱 다운로드) */
@Controller
public class MoEmEtcCtrl {
	
	
	/** [POPUP] - 푸쉬앱 다운로드 */
	@RequestMapping(value = "/cm/pushAppDownPop")
	public String pushAppDownPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return MoLayoutUtil.getJspPath("/em/pushAppDown","Pop");
	}
	
}
