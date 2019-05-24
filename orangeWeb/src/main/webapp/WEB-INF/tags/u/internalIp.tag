<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		javax.servlet.jsp.tagext.JspFragment,
		com.innobiz.orange.web.pt.secu.UserVo,
		com.innobiz.orange.web.pt.secu.LoginSession"
%><%

	UserVo userVo = LoginSession.getUser(request);
	
	if(userVo!=null && userVo.isInternalIp()){
		JspFragment jspFragment = getJspBody();
		if(jspFragment!=null){
			jspFragment.invoke(out);
		}
	}
%>