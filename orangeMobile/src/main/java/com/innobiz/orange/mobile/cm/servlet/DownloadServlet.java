package com.innobiz.orange.mobile.cm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** naver, daum 앱에서 다운로드 목적으로 만든 포워드 서블릿 */
public class DownloadServlet extends HttpServlet {

	/** serialVersionUID */
	private static final long serialVersionUID = 5165335086741424309L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String fwd = request.getParameter("fwd");
		if(fwd!=null && !fwd.isEmpty()){
			request.getRequestDispatcher(fwd).forward(request,response);
		}
	}

}
