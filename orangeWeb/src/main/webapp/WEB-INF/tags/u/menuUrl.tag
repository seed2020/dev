<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		com.innobiz.orange.web.pt.secu.UserVo,
		com.innobiz.orange.web.pt.utils.LoutUtil"
%><%@ attribute name="url" required="true"
%><%
	UserVo userVo = (UserVo)session.getAttribute("userVo");
	url = LoutUtil.converTypedParam(url, userVo);
	if(url==null) url = "";
%><%= url %>