<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // [팝업:선택] 선택된 값을 세팅 %>
function setChecked() {
	var $checked = $('#selectBbFrm').contents().find('input[name="chk"]:checked');
	if ($checked.length == 0) {
		alertMsg('cm.msg.noSelect'); <% // cm.msg.noSelect=선택한 항목이 없습니다. %>
		return;
	}
	<c:if test="${param.callback != null && param.callback != ''}">
	var brdIds = [], brdNms = [];
	$checked.each(function() {
		brdIds.push($(this).val());
		brdNms.push($(this).parents('tr:first').find('label').text());
	});
	${param.callback}(brdIds, brdNms, '${param.callbackArgs}');
	</c:if>
	dialog.close('selectBbPop');
}
//-->
</script>
<div style="width:400px">

<iframe id="selectBbFrm" name="selectBbFrm" src="./selectBbFrm.do?${params}" style="width:100%; height:360px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" alt="확인" onclick="setChecked();" auth="W" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>
