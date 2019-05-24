<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="${_lang}"><%
	String title = (String)request.getAttribute("UI_TITLE");
	if(title==null) request.setAttribute("UI_TITLE", "DATA FRAME");
%>
<head><title>${UI_TITLE}</title>
<script type="text/javascript" >
<!--
window.onload = function(){
	<%
	if(request.getAttribute("exception")!=null){
		
		Exception e = (Exception)request.getAttribute("exception");
		String message = e.getMessage();
		if(message==null || message.isEmpty()){
			message = e.getClass().getCanonicalName() + "\n" + e.getStackTrace()[0].toString().trim();
		}
		request.setAttribute("message", message);
		%>alert("<u:out value="${message}" type="script" />");<%
	} else if(request.getAttribute("messageCode")!=null){
		%>alert("<u:msg titleId="${messageCode}" javaScriptEscape="true" />");<%
	} else if(request.getAttribute("message")!=null){
		%>alert("<u:out value="${message}" type="script" />");<%
	} else if(session.getAttribute("message")!=null){
		%>alert("<u:out value="${sessionScope.message}" type="script" />");<%
		session.removeAttribute("message");
	}
	if(request.getAttribute("uagntToken")!=null){
		%>if(window.localStorage){ window.localStorage.setItem("uagntToken", "${uagntToken}"); }<%
	}
	if(request.getAttribute("todo")!=null){
		%>${todo}<%
	} else if(session.getAttribute("todo")!=null){
		%>${sessionScope.todo}<%
		session.removeAttribute("todo");
	}
	if(request.getAttribute("togo")!=null){
		%>top.location.replace("${togo}");<%
	} else if(session.getAttribute("togo")!=null){
		%>top.location.replace("${sessionScope.togo}");<%
		session.removeAttribute("togo");
	}
	%>
};
//-->
</script>
</head>
<body></body>
</html>