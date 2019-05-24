<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.pt.secu.UserVo,com.innobiz.orange.web.cm.utils.FinderUtil"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%><%
	UserVo uvo = (UserVo)session.getAttribute("userVo");
	if(uvo!=null && FinderUtil.find(uvo.getOdurUid(), false)<0){
		response.sendRedirect("/c"+"m/er"+"ror/li"+"ce"+"nse.do");
		return;
	}
%><!DOCTYPE html>
<html lang="${_lang}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="format-detection" content="telephone=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="viewport" content="width=device-width, initial-scale=${browser.mobile && browser.safari ? '0.25' : '0.7'}, minimum-scale=0.1, maximum-scale=8" />
<c:if test="${not empty META_TITLE}"><title>${META_TITLE}</title></c:if>
<link rel="stylesheet" href="${_cxPth}/css/animation.css" type="text/css" />
<tiles:insertAttribute name="headinc" />
</head>
<body>

<tiles:insertAttribute name="body" />

</body>
</html>