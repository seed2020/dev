package com.innobiz.orange.web.sample.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;

@Controller
public class SampleTablesCtrl {

	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	@RequestMapping(value = "/sample/tables/index")
	public String index(ModelMap model) {
		return "sample/tables/index";
	}
	
	@RequestMapping(value = "/sample/tables/{catId}/{tableId}")
	public String setTables(
			@PathVariable("catId") String catId, 
			@PathVariable("tableId") String tableId, 
			@RequestParam(value = "actMethod", required = false) String actMethod,
			HttpServletRequest request, ModelMap model) throws Exception {
		
		if(actMethod!=null && tableId!=null){
			String voName = tableId.substring(0, 1).toLowerCase() +  tableId.substring(1) + "Vo";
			String className = "com.innobiz.orange.web."+tableId.substring(0, 2).toLowerCase()+".vo."+tableId+"Vo";
			CommonVo vo = (CommonVo)Class.forName(className).newInstance();
			VoUtil.bind(request, vo);
			
			if("insert".equals(actMethod)){
				commonSvc.insert(vo);
			} else if("update".equals(actMethod)){
				commonSvc.update(vo);
			} else if("delete".equals(actMethod)){
				commonSvc.delete(vo);
			}
			
			if("select".equals(actMethod) || "insert".equals(actMethod) || "update".equals(actMethod)){
				vo = commonSvc.queryVo(vo);
				if(vo!=null) model.addAttribute(voName, vo);
			}
		}
		
		return "sample/tables/"+catId+"/"+tableId;
	}
}
