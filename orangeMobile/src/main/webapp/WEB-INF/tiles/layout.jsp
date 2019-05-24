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
		<div class="menu" onclick="$m.menu.open('${_ptMnuLoutDVo.mnuLoutId}', '${menuId}');"></div><c:if
			test="${false}"><!--<div class="logo"></div>--></c:if>
		<div class="tit"><u:out value="${_ptMnuLoutDVo.rescNm}" /></div>
		<div class="hd_btnarea">
			<dl>
				<dd class="back" onclick="history.back();"></dd>
				<dd id="space" style="display:none; cursor:default;"></dd>
				<dd class="reload" onclick="$m.nav.reload(null, true);"></dd>
				<dd class="search" onclick="javascript:$m.user.search();"></dd>
			</dl>
		</div>
	</header>
	
	<nav id="navArea">
		<div class="nav">
			<ul><c:forEach items="${_sideList}" var="ptMnuLoutCombDVo">
				<li class="navtit${menuId == ptMnuLoutCombDVo.mnuLoutCombId ? '_on' : ''}" onclick="$m.menu.menuClick(event, '${ptMnuLoutCombDVo.ptMnuDVo.mnuTypCd}', '${ptMnuLoutCombDVo.mnuUrl}');"><u:out value="${ptMnuLoutCombDVo.rescNm}" /></li></c:forEach>
			</ul>
		</div>
		<div class="nav_icol" id="toLeft" style="display:none;"></div>
		<div class="nav_icor" id="toRight" style="display:none;"></div>
	</nav>
	
<tiles:insertAttribute name="body" />

	<div id="inputSpace" style="height:280px; display:none;"></div>
	</div>
</div>
</body>
</html>