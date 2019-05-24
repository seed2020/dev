<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
function fnClnsSave(obj){
	if (validator.validate('setClnsForm')) {
		callAjax('./transClns.do?menuId=${menuId}', {clnsNm:$('#clnsNm').val(),schBcRegrUid:'${param.schBcRegrUid}'}, function(data){
			if(data.message!=null){
				alert(data.message);
			} else {
				alert('<u:msg titleId="cm.msg.save.success" alt="저장되었습니다" />');
				parent.fnClnsReload(data);
			}
		});
		dialog.close(obj);		
	}
};
</script>

<div style="width:300px">
<form id="setClnsForm" name="setClnsForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="schBcRegrUid" value="${param.schBcRegrUid }"/>

<% // 폼 필드 %>
<u:listArea>
	<tr>
		<td width="27%" class="head_lt"><u:mandatory /><u:msg titleId="cols.clns" alt="친밀도" /></td>
		<td width="73%"><u:input id="clnsNm" name="clnsNm" titleId="cols.clns" style="${style}" maxByte="120" mandatory="Y"/></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="fnClnsSave(this);" alt="저장" auth="W" />
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>

</form>
</div>
