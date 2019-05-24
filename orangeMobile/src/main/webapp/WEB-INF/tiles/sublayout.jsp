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
<c:if test="${not empty META_TITLE}"><title>${META_TITLE}</title></c:if>
<link rel="stylesheet" href="${_cxPth}/css/animation.css" type="text/css" />
<tiles:insertAttribute name="headinc" />
<script type="text/javascript" src="${_cxPth}/js/orange-layout.js" charset="UTF-8"></script>
<script type="text/javascript">
//<![CDATA[
var $m = parent.$m;
$(document).ready(function() {
	if(window==parent) location.replace('/');
	else {
		$layout.adjust();
		$m.nav.ready(window);
	}
});
//]]>
</script>
</head>
<body>
<div class="wrapper">
	<div class="wrapscroll">
	
	<header>
		<div class="back" style="postion:absolute" onclick="history.back();"></div><c:if
			test="${empty UI_BUTTON or empty UI_SCRIPT}">
		<div class="subtit" style="right:20px;"><u:out value="${UI_TITLE}" /></div></c:if><c:if
			test="${not empty UI_BUTTON and not empty UI_SCRIPT}">
		<div class="subtit2"><u:out value="${UI_TITLE}" /></div>
		<div class="subbtn" onclick="${UI_SCRIPT}"><u:out value="${UI_BUTTON}" /></div></c:if>
	</header>
	
<tiles:insertAttribute name="body" />

	</div>
</div>
</body>
</html>