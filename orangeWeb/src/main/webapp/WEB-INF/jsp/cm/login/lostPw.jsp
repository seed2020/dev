<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
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
<title>LOST PASSWORD</title>
<script type="text/javascript">
<!--<%
// 저장 버튼 %>
function doSubmit(){
	if(validator.validate('pwArea')){
		var param = new ParamMap().getData('pwArea');
		if(param.get('newPw1') != param.get('newPw2')){
			alert("<u:msg titleId='pt.jsp.setPw.not.chg2' alt='새로운 비밀번호와 새로운 비밀번호 확인이 같지 않습니다.' javaScriptEscape='true' />");
			$('#newPw1').focus();
		} else {
			callAjax("/cm/login/createSecuSessionAjx.do?secuSessionCode=${secuSessionCode}", null, function(data){
				var key = new RSAPublicKey(data.e, data.m);
				var data = encrypt(key, JSON.stringify(param.toJSON()));
				var $form = $('#secuFrm');
				$form.find('#secu').val(data);
				$form.attr("target","dataframe");
				$form.attr("action","/cm/login/transLostPw.do");
				$form.submit();
			});
		}
	}
}
$(document).ready(function() {<%
	// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>
</head>
<body class="styleThese">
<div class="container">
<div class="wrapper">

	<div id="pwArea" style="position:absolute; margin-left: 50%; left:-200px; margin-top:160px; width:400px">
	
		<u:title titleId="pt.jsp.setPw.title" alt="비밀번호 변경" />
		<u:listArea>
			<tr>
				<td width="40%" class="head_lt"><u:msg titleId="pt.jsp.setPw.newPw1" alt="새로운 비밀번호" /></td>
				<td><u:input id="newPw1" type="password" titleId="pt.jsp.setPw.newPw1" mandatory="Y"
					minLength="${pwPolc.polcUseYn eq 'Y' and not empty pwPolc.minLength ? pwPolc.minLength : '6'}"
					maxLength="${pwPolc.polcUseYn eq 'Y' and not empty pwPolc.maxLength ? pwPolc.maxLength : '20'}" /></td>
			</tr>
			<tr>
				<td width="40%" class="head_lt"><u:msg titleId="pt.jsp.setPw.newPw2" alt="새로운 비밀번호 확인" /></td>
				<td><u:input id="newPw2" type="password" titleId="pt.jsp.setPw.newPw2" mandatory="Y"
					minLength="${pwPolc.polcUseYn eq 'Y' and not empty pwPolc.minLength ? pwPolc.minLength : '6'}"
					maxLength="${pwPolc.polcUseYn eq 'Y' and not empty pwPolc.maxLength ? pwPolc.maxLength : '20'}" /></td>
			</tr>
		</u:listArea>
		<input type="hidden" name="lostPwId" value="${param.lostPwId}" />
		<div style="float:right;"><u:buttonS titleId="cm.btn.save" alt="저장" onclick="doSubmit()" /></div>
		
		<c:if test="${pwPolc.polcUseYn == 'Y'}">
			<div class="color_txt">※ <u:msg titleId="pt.jsp.setPw.tx10"
				arguments="${pwPolc.minLength},${pwPolc.maxLength}" separator=","
				alt="{0}자 이상 {1}자 이하로 작성 합니다." /></div>
			<c:if test="${pwPolc.numberMandatoryYn == 'Y'}">
			<div class="color_txt">※ <u:msg titleId="pt.jsp.setPw.tx20"
				alt="1자리 이상의 숫자를 반듯이 포함 해야 포함해야 합니다." /></div>
			</c:if>
			<c:if test="${pwPolc.specailCharMandatoryYn == 'Y'}">
			<div class="color_txt">※ <u:msg titleId="pt.jsp.setPw.tx40"
				alt="1자리 이상의 특수 문자를 포함해야 합니다." /></div>
			</c:if>
		</c:if>
		
	</div>

</div>
</div>

<form id="secuFrm"><input type="hidden" id="secu" name="secu" /></form>
<iframe id="dataframe" name="dataframe" src="about:blank" style="border:0px;width:0px;height:0px;"></iframe>
</body>
</html>