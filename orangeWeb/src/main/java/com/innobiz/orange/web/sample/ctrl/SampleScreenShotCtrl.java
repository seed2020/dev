package com.innobiz.orange.web.sample.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SampleScreenShotCtrl {
	
	
	/** 화면 열기 */
	@RequestMapping(value = "/sample/screenshot/setScreenshot")
	public String setScreenshot(Model model) throws Exception {
		return "/sample/screenshot/setScreenshot";
	}
	
}
