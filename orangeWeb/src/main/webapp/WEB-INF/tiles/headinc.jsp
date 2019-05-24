<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="com.innobiz.orange.web.cm.utils.ArrayUtil"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%
	String[] CSS_OPTS = (String[])request.getAttribute("CSS_OPTS");
	String[] JS_OPTS = (String[])request.getAttribute("JS_OPTS");
	
%>
<link rel="stylesheet" href="${_cxPth}/css/uniform.default.${_skin}.css" type="text/css" />
<link rel="stylesheet" href="${_cxPth}/css/container.${_skin}.css" type="text/css" /><c:if test="${_loutCatId != 'I'}">
<link rel="stylesheet" href="${_cxPth}/css/header.text.css" type="text/css" /></c:if><c:if test="${_loutCatId == 'I'}">
<link rel="stylesheet" href="${_cxPth}/css/header.icon.css" type="text/css" />
<link rel="stylesheet" href="${_cxPth}/css/header.icon.img.css" type="text/css" /></c:if><%

	if(ArrayUtil.isInArray(CSS_OPTS, "iconImg")){ %>
<link rel="stylesheet" href="${_cxPth}/css/header.icon.img.css" type="text/css" /><%
	}

	if(ArrayUtil.isInArray(JS_OPTS, "editor") && request.getAttribute("namoEditorEnable") == null){ %>
<link rel="stylesheet" href="${_cxPth}/editor/jelly/jelly.css" type="text/css" /><%
	}
%>
<link rel="stylesheet" href="${_cxPth}/css/print.css" type="text/css" media="print" />

<script type="text/javascript" src="${_cxPth}/js/jquery-1.10.2.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/common.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/commonEx.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/validator.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/jquery.uniform.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/json2.js" charset="UTF-8"></script><%

	if(ArrayUtil.isInArray(JS_OPTS, "editor")){
		if(request.getAttribute("namoEditorEnable") != null){%>
<script type="text/javascript" src="${_cxPth}/editor/namo/js/namo_scripteditor.js" charset="UTF-8"></script><%
		} else {%>
<script type="text/javascript" src="${_cxPth}/editor/jelly/jellyEditor.1.0.min.js" charset="UTF-8"></script><%
		}
	}

	if(ArrayUtil.isInArray(JS_OPTS, "pt.rsa")){ %>
<script type="text/javascript" src="${_cxPth}/js/rsa.js" charset="UTF-8"></script><%
	}

	if(ArrayUtil.isInArray(JS_OPTS, "pt.pltSetup")){ %>
<script type="text/javascript" src="${_cxPth}/js/pltSetup.js" charset="UTF-8"></script><%
	}
	
	if(ArrayUtil.isInArray(JS_OPTS, "editor")){
		if(request.getAttribute("namoEditorEnable") != null){%>
<script type="text/javascript">unloadEvent.editorType = "namo";</script><%
		}
	}
	if(request.getAttribute("noCopy") != null){%>
<script type="text/javascript">$(document).ready(function(){ copyDisable(); });</script><%
	}
	if(com.innobiz.orange.web.cm.config.ServerConfig.IS_LOC){%>
<script type="text/javascript">$(document).ready(function(){ dialog.local = true; });</script><%
	}

%>