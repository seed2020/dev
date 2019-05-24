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
	if(request.getAttribute("printView")==null){
		request.setAttribute("printView", "printAp8");
	}
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="${_lang}" lang="${_lang}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
<c:if test="${not empty META_TITLE}"><title>${META_TITLE}</title></c:if>
<tiles:insertAttribute name="headinc" />
</head>
<body class="styleThese">

<!--Homewrapper S -->
<div class="homewrapper ${printView}">

<!--Header S --><u:set test="${_loutCatId != 'I'}" var="_background" value="background:url(${_bgImg}) no-repeat;" elseValue="" />
<div id="header_${_skin}" style="position:relative; z-index:200; ${_background}">
<c:if test="${_loutCatId == 'I'}"><tiles:insertAttribute name="headerIcon" /></c:if>
<c:if test="${_loutCatId != 'I'}"><tiles:insertAttribute name="headerBasc" /></c:if>
</div>
<!--//Header E -->

<!--Container S -->
<div class="container">
<c:if test="${empty UI_PORTLET and empty UI_FRAME}">
	<div class="sidebar">
	<c:if test="${param.menuId.substring(0,2) == 'CT'}">
<tiles:insertAttribute name="leftCt" />
	</c:if>
	<c:if test="${menuId == 'SH'}">
<tiles:insertAttribute name="leftSh" />
	</c:if>
	<c:if test="${param.menuId.substring(0,2) != 'CT' and menuId != 'SH'}">
<tiles:insertAttribute name="left" />
	</c:if>
	</div>

	<div class="content">
	<div class="wrapper" <c:if test="${_loutCatId ne 'I' and _subMnuOption eq 'M'}">style="padding-top:38px"</c:if>>
<tiles:insertAttribute name="body" />
	</div>
	</div>
</c:if>
<c:if test="${not empty UI_PORTLET}">
	<div class="wrapper">
<tiles:insertAttribute name="body" />
	</div>
</c:if>
<c:if test="${not empty UI_FRAME}">
<tiles:insertAttribute name="body" />
</c:if>
</div>
<!--//Container E -->

</div>
<!--//Homewrapper E -->
<c:if test="${not empty sessionScope.loginPops || not empty sessionScope.loginSurvPops}">
<script type="text/javascript">
<!--
$(document).ready(function() {
	<c:forEach items="${sessionScope.loginPops}" var="popArr">dialog.open('${popArr[1]}Pop', '${popArr[2]}', '/cm/viewBullPop.do?bullId=${popArr[0]}&brdId=${popArr[1]}');dialog.resize('${popArr[1]}Pop');</c:forEach>
	<c:forEach items="${sessionScope.loginSurvPops}" var="popSurvArr">dialog.open('${popSurvArr[0]}Pop', '${popSurvArr[1]}', '/cm/viewSurvPop.do?survId=${popSurvArr[0]}&popId=${popSurvArr[0]}Pop');dialog.resize('${popSurvArr[0]}Pop');</c:forEach>
	
});
//-->
</script><c:remove var="loginPops" scope="session" 
/><c:remove var="loginSurvPops" scope="session" />
</c:if>
<iframe id="dataframe" name="dataframe" src="about:blank" style="border:0px;width:0px;height:0px;"></iframe>

</body>
</html>