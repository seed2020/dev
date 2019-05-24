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
		<th width="30%" class="head_lt"><u:msg titleId="pt.jsp.listMnuGrp.mngCompSet" alt="관리 범위 설정" /></th>
		<td>
			<u:checkArea>
			<u:radio name="setupVa" value="allComp" titleId="cm.option.allComp" alt="전체회사"
				inputClass="bodybg_ct" textClass="body_lt" checkValue="${setupVa}" />
			<u:radio name="setupVa" value="myComp" titleId="cm.option.myComp" alt="소속회사"
				inputClass="bodybg_ct" textClass="body_lt" checkValue="${setupVa}" />
			</u:checkArea>
		</td>
	</tr>

</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:saveMngComp();" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</div>