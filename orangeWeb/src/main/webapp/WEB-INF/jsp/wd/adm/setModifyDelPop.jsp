<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

request.setAttribute("modTypCds", new String[]{"forw","cre","calc","ongo"});

%><script type="text/javascript">
<!--
function saveModifyData(modTypCd){
	if(validator.validate('wdModifyForm')){
		var $form = $("#wdModifyForm");
		$form.attr("action","./transModifyDel.do");
		$form.attr("target","dataframe");
		$form.submit();
	}
}
// -->
</script>
<div style="width:500px">
<form id="wdModifyForm">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="year" value="${param.year}" />
<input type="hidden" name="anbTypCd" value="${param.anbTypCd}" />
<input type="hidden" name="odurUid" value="${param.odurUid}" />
<input type="hidden" name="useYmds" value="${param.useYmds}" />

<u:listArea id="modifyDataArea" colgroup="20%,80%">
<tr>
	<td class="head_ct"><u:mandatory/><u:msg titleId="wd.jsp.remark" alt="비고" /></td>
	<td><u:input id="note" name="note" maxByte="80" titleId="wd.jsp.remark" mandatory="Y" style="width:96%" /></td>
</tr>
</u:listArea>
</form>

<u:buttonArea>
	<u:button id="cmdModify" titleId="cm.btn.save" href="javascript:saveModifyData();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>