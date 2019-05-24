package com.innobiz.orange.web.cm.ctrl;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;

/** 기능성 빈 페이지 조회용 */
@Controller
public class CmUtilCtrl {

	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 기능성 페이지 호출 조회 */
	@RequestMapping(value = "/cm/util/{path}", method = RequestMethod.GET)
	public String getPathUrl( HttpServletRequest request,
			@PathVariable("path") String path,
			ModelMap model){
		return "/cm/util/"+path;
	}
	
	/** 403 오류 조회 */
	@RequestMapping(value = "/cm/error/error403", method = RequestMethod.GET)
	public String error403(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws ServletException, IOException{
		request.getRequestDispatcher(LayoutUtil.getErrorJsp(403)).forward(request,response);
		return null;
	}
	
	/** 404 오류 조회 */
	@RequestMapping(value = "/cm/error/error404", method = RequestMethod.GET)
	public String error404(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws ServletException, IOException{
		request.getRequestDispatcher(LayoutUtil.getErrorJsp(404)).forward(request,response);
		return null;
	}
	
	/** 500 오류 조회 */
	@RequestMapping(value = "/cm/error/error500", method = RequestMethod.GET)
	public String error500(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws ServletException, IOException{
		request.getRequestDispatcher(LayoutUtil.getErrorJsp(500)).forward(request,response);
		return null;
	}
	
	/** 900 오류 조회 - 라이센스 사용자 초과 */
	@RequestMapping(value = "/cm/error/license", method = RequestMethod.GET)
	public String license(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws ServletException, IOException{
		request.getRequestDispatcher(LayoutUtil.getErrorJsp(900)).forward(request,response);
		return null;
	}
	
	/** 결과 메세지 */
	@RequestMapping(value = "/cm/result/commonResult", method = RequestMethod.GET)
	public String result(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws ServletException, IOException{
		request.getRequestDispatcher(LayoutUtil.getErrorJsp(0)).forward(request,response);
		return null;
	}
	
	/** 메뉴ID 조회 */
	@RequestMapping(value = "/cm/secu/getMenuIdAjx", method = RequestMethod.GET)
	public String getMenuId(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws ServletException, IOException, SQLException{
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		if(userVo==null){
			model.put("menuId", "");
		} else {
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String url = (String)object.get("url");
			String menuId = ptSecuSvc.getSecuMenuId(userVo, url);
			model.put("menuId", menuId);
		}
		
		return JsonUtil.returnJson(model);
	}
}
