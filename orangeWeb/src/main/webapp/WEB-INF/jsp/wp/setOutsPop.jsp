<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%--
[버튼] 등록 --%>
function saveMp(){
	if(validator.validate('wpMpBVoForm')){
		var $form = $("#wpMpBVoForm");
		$form.attr('action', './transMp.do');
		$form.attr('target', 'dataframe'); 
		$form.submit();
	}
}
//-->
</script>
<div style="width:300px;">
<form id="wpMpBVoForm">
<u:listArea colgroup="30%,70%">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="mpId" value="${wpMpBVo.mpId}" />
<input type="hidden" name="role1Cd" value="${param.role1Cd}" />
<tr>
	<td class="head_ct"><u:msg titleId="cols.nm" alt="이름" /><c:if test="${empty viewMode}"><u:mandatory /></c:if></td>
	<td class="${not empty viewMode ? 'body_lt' : ''}"><u:input id="mpNm" value="${wpMpBVo.mpNm}"
		titleId="cols.nm" mandatory="Y" maxByte="30" style="width:93%"
		type="${not empty viewMode ? 'view' : ''}" /></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="cols.grade" alt="직급" /></td>
	<td class="${not empty viewMode ? 'body_lt' : ''}"><u:input id="mpGrade" value="${wpMpBVo.mpGrade}"
		titleId="cols.grade" maxByte="50" style="width:93%"
		type="${not empty viewMode ? 'view' : ''}" /></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="cols.phon" alt="전화번호" /></td>
	<td class="${not empty viewMode ? 'body_lt' : ''}"><u:input id="mpPhone" value="${wpMpBVo.mpPhone}"
		titleId="cols.phon" maxByte="50" style="width:93%"
		type="${not empty viewMode ? 'view' : ''}" /></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="cols.email" alt="이메일" /></td>
	<td class="${not empty viewMode ? 'body_lt' : ''}"><u:input id="mpEmail" value="${wpMpBVo.mpEmail}"
		titleId="cols.email" maxByte="50" style="width:93%"
		type="${not empty viewMode ? 'view' : ''}" /></td>
</tr>
</u:listArea>
</form>

<u:buttonArea><c:if
		test="${empty wpMpBVo and empty viewMode}">
	<u:button titleId="cm.btn.reg" href="javascript:saveMp();" alt="등록" /></c:if><c:if
		test="${not empty wpMpBVo and empty viewMode}">
	<u:button titleId="cm.btn.mod" href="javascript:saveMp();" alt="수정" /></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>