<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" />
<script type="text/javascript">
<!--
<% // [팝업:선택] 선택된 값을 리턴 %>
function setChecked() {
	var $checked = $('#selectBullCatFrm').contents().find('input[name="radio"]:checked');
	if ($checked.length == 0) {
		alertMsg('cm.msg.noSelect'); <% // cm.msg.noSelect=선택한 항목이 없습니다. %>
		return;
	}
	$('#catGrpId').val($checked.val());
	$('#catGrpNm').val($checked.parents('td:first').find('input[name="nm"]').val());
	dialog.close('selectBullCatPop');
}
//-->
</script>
<div style="width:400px">

<iframe id="selectBullCatFrm" name="selectBullCatFrm" src="/bb/adm/selectBullCatFrm.do?menuId=${menuId }" style="width:100%; height:353px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" alt="확인" onclick="setChecked();" auth="A" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>
