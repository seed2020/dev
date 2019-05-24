<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function doSubmit(){
	if(validator.validate('pwArea')){
		var param = new ParamMap().getData('pwArea');
		if(param.get('orgPw') == param.get('newPw1')){
			alert("<u:msg titleId='pt.jsp.setPw.not.chg1' alt='기존 비밀번호와 동일한 비밀번호로 변경 할 수 없습니다.' javaScriptEscape='true' />");
			$('#newPw1').focus();
		} else if(param.get('newPw1') != param.get('newPw2')){
			alert("<u:msg titleId='pt.jsp.setPw.not.chg2' alt='새로운 비밀번호와 새로운 비밀번호 확인이 같지 않습니다.' javaScriptEscape='true' />");
			$('#newPw1').focus();
		} else {
			callAjax("/cm/login/createSecuSessionAjx.do", null, function(data){
				var key = new RSAPublicKey(data.e, data.m);
				var data = encrypt(key, JSON.stringify(param.toJSON()));
				var $form = $('#secuFrm');
				$form.find('#secu').val(data);
				$form.attr("target","dataframe");
				$form.submit();
			});
		}
	}
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
	<c:if test="${not empty sessionScope.FORCE_MOVE }">alert('<u:msg titleId="pt.pwPolc.change" alt="비밀번호 정책에 의하여 비밀번호를 변경해야 합니다." />');</c:if>
});
//-->
</script>

<u:title titleId="pt.jsp.setPw.title" alt="비밀번호 변경" menuNameFirst="true" />

<form id="pwArea">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.setPw.orgPw" alt="기존 비밀번호" /></td>
		<td><u:input id="orgPw" type="password" titleId="pt.jsp.setPw.orgPw" mandatory="Y" maxLength="20" /></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.setPw.newPw1" alt="새로운 비밀번호" /></td>
		<td><u:input id="newPw1" type="password" titleId="pt.jsp.setPw.newPw1" mandatory="Y"
			minLength="${pwPolc.polcUseYn eq 'Y' and not empty pwPolc.minLength ? pwPolc.minLength : '6'}"
			maxLength="${pwPolc.polcUseYn eq 'Y' and not empty pwPolc.maxLength ? pwPolc.maxLength : '20'}" /></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.setPw.newPw2" alt="새로운 비밀번호 확인" /></td>
		<td><u:input id="newPw2" type="password" titleId="pt.jsp.setPw.newPw2" mandatory="Y"
			minLength="${pwPolc.polcUseYn eq 'Y' and not empty pwPolc.minLength ? pwPolc.minLength : '6'}"
			maxLength="${pwPolc.polcUseYn eq 'Y' and not empty pwPolc.maxLength ? pwPolc.maxLength : '20'}" /></td>
	</tr><c:if
		test="${pwPolc.polcUseYn == 'Y' and not empty nextChgDt}">
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.setPw.lastChgDt" alt="마지막 변경일" /></td>
		<td class="body_lt"><u:out value="${lastChgDt}" /></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.setPw.nextChgDt" alt="다음 변경일" /></td>
		<td class="body_lt"><u:out value="${nextChgDt}" /></td>
	</tr></c:if>
</u:listArea>
</form>

<c:if test="${pwPolc.polcUseYn eq 'Y'}">
	<div class="color_txt">※ <u:msg titleId="pt.jsp.setPw.tx10"
		arguments="${pwPolc.minLength},${pwPolc.maxLength}" separator=","
		alt="{0}자 이상 {1}자 이하로 작성 합니다." /></div>
	<c:if test="${pwPolc.numberMandatoryYn eq 'Y'}">
	<div class="color_txt">※ <u:msg titleId="pt.jsp.setPw.tx20"
		alt="1자리 이상의 숫자를 반드시 포함 해야 포함해야 합니다." /></div>
	</c:if>
	<c:if test="${pwPolc.specailCharMandatoryYn eq 'Y'}">
	<div class="color_txt">※ <u:msg titleId="pt.jsp.setPw.tx40"
		alt="1자리 이상의 특수 문자를 포함해야 합니다." /></div>
	</c:if>
</c:if>
<u:blank />

<form id="secuFrm" action="./transPw.do" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" id="secu" />
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="doSubmit()" auth="W" />
</u:buttonArea>