<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
%>
<script type="text/javascript">
<!--<%--
[버튼] 저장 --%>
function savePrjGrp(){
	if(validator.validate('prjGrpForm')){
		var $form = $("#prjGrpForm");
		$form.attr('action', './transPrjGrp.do');
		$form.attr('target', 'dataframe');
		$form.submit();
	}
}
//-->
</script>
<div style="width:300px;">
<form id="prjGrpForm">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="cat" value="${param.cat}" />
<input type="hidden" name="grpId" value="${wpPrjGrpBVo.grpId}" />
<u:listArea colgroup="30%,70%">
<tr>
	<td class="head_ct"><u:msg titleId="wp.prjGrp" alt="프로잭트 그룹"  /></td>
	<td ><u:input id="grpNm" value="${wpPrjGrpBVo.grpNm}" maxLength="20" mandatory="Y" style="width:92%" /></td>
</tr>
</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:savePrjGrp();" alt="저장" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>