<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ page isErrorPage="true"
%><%
	request.setAttribute("errorCode", "404");
	String uri = (String)request.getAttribute("_uri");
	if(uri.indexOf(".do")>0){
		System.out.println("[404] "+uri);
	}
%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><u:msg titleId="cm.error.title.${errorCode}" /></title>
</head>
<body>HTTP 404 ERROR</body>
</html>