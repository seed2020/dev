package com.innobiz.orange.mobile.cm.ctrl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 기능성 빈 페이지 조회용 */
@Controller
public class MoCmUtilCtrl {

	/** 기능성 페이지 호출 조회 */
	@RequestMapping(value = "/cm/util/{path}", method = RequestMethod.GET)
	public String getPathUrl( HttpServletRequest request,
			@PathVariable("path") String path,
			ModelMap model){
		
		if("editor".equals(path) || "editorJelly".equals(path)){
			UserVo userVo = LoginSession.getUser(request);
			if(userVo==null){
				model.put("todo", "top.location = '/';");
				return LayoutUtil.getResultJsp();
			}
			return "/cm/util/"+path;
		}
		
		return "/cm/util/"+path;
	}
	
	/** 오류 조회 */
	@RequestMapping(value = "/cm/error/{errorId}", method = RequestMethod.GET)
	public String error403(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("errorId") String errorId,
			ModelMap model) throws ServletException, IOException{
		
		if("license".equals(errorId)){
			return MoLayoutUtil.getJspPath("/cm/error/error900", "empty");
		} else {
			return MoLayoutUtil.getJspPath("/cm/error/"+errorId);
		}
	}
}
