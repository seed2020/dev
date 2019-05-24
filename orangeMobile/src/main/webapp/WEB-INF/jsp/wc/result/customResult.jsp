<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><!DOCTYPE html>
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
		%>alert("<u:out value='${message}' type='script' />");<%
	} else if(request.getAttribute("messageCode")!=null){
		%>alert("<u:msg titleId='${messageCode}' javaScriptEscape='true' />");<%
	} else if(request.getAttribute("messageTogo")!=null){
		%>alert("<u:out value='${messageTogo}' type='script' />", function(){
			location.replace("${togo}");
		});<%
	} else if(request.getAttribute("message")!=null){
		%>alert("<u:out value='${message}' type='script' />");<%
	}
	if(request.getAttribute("todo")!=null){
		%>${todo}<%
	}
	if(request.getAttribute("togo")!=null){
		%>location.replace("${togo}");<%
	}
	if(request.getAttribute("close")!=null){
		%>window.close();<%
	}
	%>
};
//-->
</script>
</head><body></body></html>