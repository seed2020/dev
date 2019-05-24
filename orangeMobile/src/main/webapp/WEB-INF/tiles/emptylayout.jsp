<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"
%><!DOCTYPE html>
<html lang="${_lang}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="format-detection" content="telephone=no" />
<c:if test="${not empty META_TITLE}"><title>${META_TITLE}</title></c:if>
<tiles:insertAttribute name="headinc" />
<script type="text/javascript" src="${_cxPth}/js/orange-layout.js" charset="UTF-8"></script>
<script type="text/javascript">
//<![CDATA[
var $m = parent.$m;
$(document).ready(function() {
	if(window==parent) location.replace('/');
	else { $m.nav.ready(window); }
});
//]]>
</script>
</head>
<body>
<div class="wrapper">
<div class="wrapscroll">
<tiles:insertAttribute name="body" />
</div>
</div>
</body>
</html>