<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function saveDebr(){
	if (validator.validate('setDebrForm')) {
		var $form = $("#setDebrForm");
		$form.attr("method", "POST");
		$form.attr("action", "./transSaveDebr.do?menuId=${menuId}&ctId=${ctId}");
		$form.submit();
	}
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<div style="width:600px">
<form id="setDebrForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="27%" class="head_lt"><u:mandatory /><u:msg titleId="cols.topc" alt="주제" /></td>
	<td width="73%">
		<u:input id="topc" name="topc" titleId="cols.topc" style="width:96%;" value="${ctDebrBVo.subj}" mandatory="Y" maxByte="240"/>
		<input id="debrId" name="debrId" value="${ctDebrBVo.debrId}" type="hidden"/>
	</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.itnt" alt="취지" /></td>
	<td><u:textarea id="itnt" name="itnt" value="${ctDebrBVo.estbItnt}" titleId="cols.itnt" style="width:96%" rows="2" mandatory="Y" maxByte="1000"/></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:saveDebr();" alt="저장"/>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
