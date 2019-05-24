<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"


%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="${_lang}" xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${_cxPth}/js/jquery-1.10.2.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/common.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/json2.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/rsa.js" charset="UTF-8"></script>
<script type="text/javascript">
$(document).ready(function() {
	var token = (typeof(window.localStorage) != 'undefined') ? window.localStorage.getItem("uagntToken") : null;
	if(token==null || token==""){
		alertMsg("pt.login.notAllowLoginByMsg");<%// pt.login.notAllowLoginByMsg=메세지에 의해 로그인 할 수 없습니다.%>
		location.replace("${loginUrl}");
		return;
	}
	var param = new ParamMap({"token":token, msgId:"${param.msgId}"});
	callAjax("/cm/login/createSecuSessionAjx.do?secuSessionCode=${secuSessionCode}", null, function(data){
		var key = new RSAPublicKey(data.e, data.m);
		var data = encrypt(key, JSON.stringify(param.toJSON()));
		location.replace("/index.do?secu="+data);
	});
});
//loginUrl
</script>
</head>
<body style="height:auto; padding-bottom:0px;"></body>
</html>