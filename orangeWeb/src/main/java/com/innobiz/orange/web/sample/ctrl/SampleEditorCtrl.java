package com.innobiz.orange.web.sample.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SampleEditorCtrl {

	//  /sample/editor/editorSample
	
	@RequestMapping(value = "/sample/editor/editorSample")
	public String index(ModelMap model) {
		
		// 에디터 사용하겠다고 JS 등 include 함
		// /WEB-INF/tiles/headinc.jsp 에서 js - include 함
		model.addAttribute("JS_OPTS", new String[]{"editor"});
		
		
		model.addAttribute("htmlValue", "<p>테스트 데이터</p>\n<p>123</p>");
		
		return "layout/sample/editor/editorSample";
	}
	
}
