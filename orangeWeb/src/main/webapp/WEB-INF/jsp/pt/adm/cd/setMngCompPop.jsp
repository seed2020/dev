<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function saveMngComp(){
	var $form = $("#mngCompForm");
	$form.attr("action","./transMngComp.do");
	$form.attr('target','dataframe');
	$form.submit();
}
//-->
</script>
<div style="width:400px">
<form id="mngCompForm">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="setupId" value="${param.setupId}" />
<u:listArea>

	<tr>
		<td style="padding:2px"><u:checkArea>
			<u:checkbox value="allow" id="setupVa" name="setupVa" checked="${setupVa=='allow'}"
				titleId="pt.jsp.setCd.allowByAdmin" alt="관리자가 코드를 수정 할 수 있도록 합니다." />
		</u:checkArea>
		</td>
	</tr>

</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveMngComp();" alt="저장" auth="SYS" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</div>