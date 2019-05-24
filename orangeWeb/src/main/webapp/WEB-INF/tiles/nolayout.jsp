<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"


%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="${_lang}" xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /><c:if test="${not empty MOBILE_VIEW}">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=3" /></c:if>
<tiles:insertAttribute name="headinc" />
</head>
<body style="height:auto; padding-bottom:0px;">

<div class="styleThese ${printView}">
<tiles:insertAttribute name="body" />
</div>

<c:if
	test="${browser.ie}">
<div style="display:none"><iframe id="dataframeForFrame" name="dataframeForFrame" src="about:blank" style="border:0px;width:0px;height:0px;"></iframe></div></c:if><c:if
	test="${not browser.ie}">
<iframe id="dataframeForFrame" name="dataframeForFrame" src="about:blank" style="border:0px;width:0px;height:0px;"></iframe></c:if>
</body>
</html>