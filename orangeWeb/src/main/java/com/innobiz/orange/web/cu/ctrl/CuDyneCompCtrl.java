package com.innobiz.orange.web.cu.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;

/** 고객관리 */
@Controller
public class CuDyneCompCtrl {

	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(CuDyneCompCtrl.class);

	/** 메세지 */
	@Autowired
	//private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	
	/** 회사 목록 조회 */
	@RequestMapping(value = "/cu/dyne/csmt/listComp")
	public String listComp(HttpServletRequest request,
			ModelMap model) throws Exception {
		                               
		return LayoutUtil.getJspPath("/cu/dyne/csmt/comp/listComp");
	}
	
	/** 회사 등록 수정 */
	@RequestMapping(value = "/cu/dyne/csmt/setComp")
	public String setComp(HttpServletRequest request,
			ModelMap model) throws Exception {
		                               
		return LayoutUtil.getJspPath("/cu/dyne/csmt/comp/setComp");
	}
	
	
	
	
	
}
