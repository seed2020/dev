package com.innobiz.orange.web.sample.ctrl;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SampleIndexCtrl {
	
	@RequestMapping(value = "/sample/index")
	public String index(ModelMap model) {
		return "layout/sample/index";
	}
	
	/**
	 * Tiles 레이아웃 설명 1
	 * 
	 * return-String 에 "layout/" 를 붙이면 상단 좌측 메뉴가 붙여진 레이아웃이 나타남
	 * 
	 * */
	@RequestMapping(value = "/sample/tilesSample1")
	public String tilesSample1(Locale locale, ModelMap model) {
		
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(new Date());
		model.addAttribute("serverTime", formattedDate );
		
		return "layout/sample/tilesSample";
	}
	
	/**
	 * Tiles 레이아웃 설명 2
	 * 
	 * return-String 에 "layout/" 를 [안] 붙이면 상단 좌측 메뉴가 [안] 붙여진 레이아웃이 나타남
	 * 
	 * */
	@RequestMapping(value = "/sample/tilesSample2")
	public String tilesSample2(Locale locale, Model model) {
		
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(new Date());
		model.addAttribute("serverTime", formattedDate );
		
		return "sample/tilesSample";
	}
	
	@RequestMapping(value = "/sample/test/*")
	public Model test1(Locale locale, Model model) {
		
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(new Date());
		model.addAttribute("serverTime", formattedDate );
		
		return model;
	}
}
