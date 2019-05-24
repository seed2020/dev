<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><script type="text/javascript">
function saveJspPath(){
	var $form = $("#saveJspPathForm");
	$form.attr("action", "./transExtDoc.do");
	$form.attr("target", "dataframe");
	$form.submit();
}
</script>
<div style="width:500px">

<form id="saveJspPathForm">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea colgroup="30%,70%">
<tr>
	<th class="head_ct">FORM ID</th>
	<th class="head_ct">JSP PATH</th>
</tr>
<c:forEach items="${apFormJspDVoList}" var="apFormJspDVo">
<tr>
	<td><u:input id="formId" title="FORM ID" value="${apFormJspDVo.formId}" style="width:90%"/></td>
	<td><u:input id="jspPath" title="JSP PATH" value="${apFormJspDVo.jspPath}" style="width:96%"/></td>
</tr>
</c:forEach>
</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="saveJspPath();" alt="저장" auth="A" id="startDownloadBtn" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>