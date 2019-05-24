<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><!DOCTYPE html>
<html lang="${_lang}"><%
	String title = (String)request.getAttribute("UI_TITLE");
	if(title==null) request.setAttribute("UI_TITLE", "DATA FRAME");
%>
<head><title>${UI_TITLE}</title>
<script type="text/javascript" >
<!--
window.onload = function(){<%
	if(request.getAttribute("noInitM")==null){
	%>
	var $m = parent.$m;
	$m.nav.noLaoutReady(window, '${noHisAdd}');
	<%
	}

	if(request.getAttribute("exception")!=null){
		
		Exception e = (Exception)request.getAttribute("exception");
		String message = e.getMessage();
		if(message==null || message.isEmpty()){
			message = e.getClass().getCanonicalName() + "\n" + e.getStackTrace()[0].toString().trim();
		}
		request.setAttribute("message", message);
		%>$m.dialog.alert("<u:out value='${message}' type='script' />");<%
	}
	
	
	if(request.getAttribute("todo") == null && request.getAttribute("togo") == null){
		if(request.getAttribute("messageCode")!=null){
			%>$m.dialog.alert("<u:msg titleId='${messageCode}' javaScriptEscape='true' />");<%
		} else if(request.getAttribute("message")!=null){
			%>$m.dialog.alert("<u:out value='${message}' type='script' />");<%
		}
	}
	
	if(request.getAttribute("todo")!=null){
		if(request.getAttribute("message")!=null){
			%>$m.dialog.alert("<u:out value='${message}' type='script' />", function(){
				${todo}
			});<%
		} else {
			%>${todo}<%
		}
	}
	
	if(request.getAttribute("togo")!=null){
		if(request.getAttribute("message")!=null){
			%>$m.dialog.alert("<u:out value='${message}' type='script' />", function(){
				location.replace("${togo}");
			});<%
		} else {
			%>location.replace("${togo}");<%
		}
	}
	%>
};
//-->
</script>
</head><body></body></html>