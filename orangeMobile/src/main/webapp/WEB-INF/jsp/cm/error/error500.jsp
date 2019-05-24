<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ page isErrorPage="true"
%><%@ page isErrorPage="true"%><%!
	private Throwable getRoot(Throwable throwable){
		while(true){
			Throwable cause = throwable.getCause();
			if(cause == null) return throwable;
			throwable = cause;
		}
	}
%><%
	Throwable throwable = (Throwable) request.getAttribute("exception");
	if(throwable==null) {
		throwable = pageContext.getException();
		throwable.printStackTrace();
	}

	request.setAttribute("errorCode", "500");
	response.setStatus(200);
%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><u:msg titleId="cm.error.title.${errorCode}" /></title>
</head>
<body>HTTP 500 ERROR</body>
</html>