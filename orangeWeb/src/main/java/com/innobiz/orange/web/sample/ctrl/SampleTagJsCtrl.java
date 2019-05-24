package com.innobiz.orange.web.sample.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.pt.vo.PtRescDVo;

@Controller
public class SampleTagJsCtrl {
	
	@RequestMapping(value = "/sample/tags/{path1}")
	public String sampleTags(
			@PathVariable("path1") String path1,
			HttpServletRequest request, ModelMap model) {
		return "layout/sample/tags/"+path1;
	}
	
	@RequestMapping(value = "/sample/js/{path1}")
	public String sampleJs(
			@PathVariable("path1") String path1,
			HttpServletRequest request, ModelMap model) {
		
		if(path1.endsWith("Pop")){
			return "sample/js/"+path1;
		} else {
			return "layout/sample/js/"+path1;
		}
	}
	
	public static void main(String[] arg){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("compId", "test comp id");
		map.put("durCat", "test dur cat");
		PtRescDVo ptRescDVo = new PtRescDVo();
		ptRescDVo.fromMap(map);
		System.out.println(ptRescDVo);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/sample/js/getData")
	public String sampleGetData(
			@Parameter(name="data", required=false)String data,
			HttpServletRequest request, ModelMap model) {
		
		JSONObject object = (JSONObject)JSONValue.parse(data);
		PtRescDVo ptRescDVo = new PtRescDVo();
		ptRescDVo.fromMap(object);
		System.out.println(ptRescDVo);
		
		// 1. map
		Map<String, String> map = new HashMap<String, String>();
		map.put("a1", "111");
		map.put("a2", "222");
		model.put("map", map);
		
		// 2. vo
		PtRescDVo vo = new PtRescDVo();
		vo.setCompId("11111");
		vo.setDurCat("startDt");
		model.put("vo", vo);
		
		// 3. list
		List<PtRescDVo> list = new ArrayList<PtRescDVo>();
		list.add(vo);
		
		vo = new PtRescDVo();
		vo.setCompId("22222");
		vo.setDurCat("endDt");
		list.add(vo);
		
		model.put("list", list);
		
		return JsonUtil.returnJson(model);
	}
}
