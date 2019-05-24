<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function saveInitPw(){
	if(validator.validate('initPwForm')){
		var param = new ParamMap().getData('initPwForm');
		if(param.get('newPw1') != param.get('newPw2')){
			alert("<u:msg titleId='pt.jsp.setPw.not.chg2' alt='새로운 비밀번호와 새로운 비밀번호 확인이 같지 않습니다.' javaScriptEscape='true' />");
			$('#newPw2').focus();
		} else {
			callAjax("/cm/login/createSecuSessionAjx.do", null, function(data){
				var key = new RSAPublicKey(data.e, data.m);
				var data = encrypt(key, JSON.stringify(param.toJSON()));
				var $form = $('#initSecuFrm');
				$form.find('#secu').val(data);
				$form.attr("target","dataframe");
				$form.submit();
			});
		}
	}
}
function saveInitPwCallback(){
	getIframeContent(gOrgDetlViewType+'Frm').processSaveOrg();
}
//-->
</script>
<div style="width:400px">
<form id="initPwForm" method="post">
<u:listArea>
	<tr>
		<td class="head_lt"><u:msg titleId="pt.jsp.setPw.newPw1" alt="새로운 비밀번호" /></td>
		<td><u:input id="newPw1" type="password" titleId="pt.jsp.setPw.newPw1" mandatory="Y" minLength="${minLength}" maxLength="20" /></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="pt.jsp.setPw.newPw2" alt="새로운 비밀번호 확인" /></td>
		<td><u:input id="newPw2" type="password" titleId="pt.jsp.setPw.newPw2" mandatory="Y" minLength="${minLength}" maxLength="20" /></td>
	</tr>
</u:listArea>
</form>

<form id="initSecuFrm" action="./transInitPw.do" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" id="secu" />
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="saveInitPw();" auth="A" alt="저장" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>