<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="com.innobiz.orange.web.cm.config.ServerConfig"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="${_lang}" lang="${_lang}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/css/container.blue.css" type="text/css" />
<link rel="stylesheet" href="${_cxPth}/css/uniform.default.${_skin}.css" type="text/css" />
<script type="text/javascript" src="${_cxPth}/js/jquery-1.10.2.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/common.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/commonEx.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/validator.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/jquery.uniform.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/json2.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/rsa.js" charset="UTF-8"></script>
<title><u:msg titleId="pt.gwLgin" alt="그룹웨어 로그인" /></title>
<script type="text/javascript">
<!--<%
// 저장 버튼 %>
function doSubmit(){
	if(validator.validate('pwArea')){
		callAjax("/cm/login/createSecuSessionAjx.do?secuSessionCode=${secuSessionCode}", null, function(data){
			var key = new RSAPublicKey(data.e, data.m);
			var param = new ParamMap().getData('pwArea');
			var data = encrypt(key, JSON.stringify(param.toJSON()));
			var $form = $('#secuFrm');
			$form.find('#secu').val(data);
			$form.submit();
		});
	}
}
function checkEnter(event){
	if (event.keyCode == 13) {
		doSubmit();
	}
}
$(document).ready(function() {<%
	// 유니폼 적용 %>
	setUniformCSS();
	$("#password").focus();
	var msg = "<u:out value='${sessionScope.ERO_SSO_MSG}' type='script' />";
	if(msg!='') alert(msg);
});
//--><% session.removeAttribute("ERO_SSO_MSG"); %>
</script>
</head>
<body class="styleThese">
<div class="container">
<div class="wrapper">

	<div style="position:absolute; margin-left: 50%; left:-200px; margin-top:160px; width:300px">
	
		<u:title titleId="pt.gwLgin" alt="그룹웨어 로그인" />
		<u:listArea id="pwArea">
			<tr>
				<td width="35%" class="head_lt"><u:msg titleId="cols.lginId" alt="로그인ID" /></td>
				<td class="body_lt">${sessionScope.ERO_SSO_LGIN_ID}</td>
			</tr>
			<tr>
				<td width="35%" class="head_lt"><u:msg titleId="cols.pw" alt="비밀번호" /></td>
				<td><u:input id="password" type="password" titleId="cols.pw"
				 mandatory="Y" minLength="6" maxLength="20" style="width:92.5%" onkeyup="checkEnter(event);" /></td>
			</tr>
		</u:listArea>
		<div style="float:right;"><u:buttonS titleId="cm.btn.confirm" alt="확인" onclick="doSubmit()" /></div>
	</div>

</div>
</div>

<form id="secuFrm" action="${sessionScope.ERO_SSO_URI}">
<input type="hidden" name="${sessionScope.ERO_SSO_PARAM}" value="${sessionScope.ERO_SSO_VALUE}" />
<input type="hidden" id="secu" name="secu" /></form>
</body>
</html>