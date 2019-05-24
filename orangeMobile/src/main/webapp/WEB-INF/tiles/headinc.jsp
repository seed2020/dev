<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="com.innobiz.orange.web.cm.utils.ArrayUtil"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%
	String[] CSS_OPTS = (String[])request.getAttribute("CSS_OPTS");
	String[] JS_OPTS = (String[])request.getAttribute("JS_OPTS");
	
	if(ArrayUtil.isInArray(JS_OPTS, "login")){
		%><link rel="stylesheet" href="${_cxPth}/css/m.gworange.${_skin}.login.css" type="text/css" /><%
	} else {
		%><link rel="stylesheet" href="${_cxPth}/css/m.gworange.${_skin}.css" type="text/css" /><%		
	}
	
%>
<script type="text/javascript" src="${_cxPth}/js/jquery-2.1.3.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/common.js" charset="UTF-8"></script><%

	if(ArrayUtil.isInArray(JS_OPTS, "tree")){ %>
<script type="text/javascript" src="${_cxPth}/js/tree.js" charset="UTF-8"></script><%
	}

	if(ArrayUtil.isInArray(JS_OPTS, "validator")){ %>
<script type="text/javascript" src="${_cxPth}/js/validator.js" charset="UTF-8"></script><%
	}

	if(request.getAttribute("noCopy") != null){%>
<script type="text/javascript">$(document).ready(function(){ copyDisable(); });</script><%
	}
%>	