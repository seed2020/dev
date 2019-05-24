<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

request.setAttribute("modTypCds", new String[]{"forw","cre","use","calc","ongo"});

%><script type="text/javascript">
<!--
function saveModifyData(){
	if(validator.validate('wdModifyForm')){
		var $form = $("#wdModifyForm");
		$form.attr("action","./transModify.do");
		$form.attr("target","dataframe");
		$form.submit();
	}
}
// -->
</script>
<div style="width:500px">
<form id="wdModifyForm">
<input type="hidden" name=menuId value="${menuId}" />
<input type="hidden" name="year" value="${param.year}" />
<input type="hidden" name="anbTypCd" value="${param.anbTypCd}" />
<input type="hidden" name="odurUid" value="${param.odurUid}" />

<u:listArea id="modifyDataArea" colgroup="20%,80%">
<tr>
	<td class="head_ct"><u:mandatory/><u:msg titleId="wd.jsp.gubun" alt="구분" /></td><c:if
		test="${param.modTypCd eq 'use'}">
	<td class="body_lt"><input type="hidden" name="modTypCd" value="use"
		/><u:msg titleId="wd.modTypCd.use" alt="사용"/></td></c:if><c:if
		test="${param.modTypCd ne 'use'}">
	<td><select name="modTypCd"><c:forEach
			items="${modTypCds}" var="modTypCd"><c:if
				test="${sysPlocMap.enterBaseAnbMak eq 'Y' or modTypCd ne 'calc'}">
			<u:option value="${modTypCd}" titleId="wd.modTypCd.${modTypCd}" /></c:if></c:forEach></select></td></c:if>
</tr><c:if

		test="${param.modTypCd eq 'use'}">
<tr>
	<td class="head_ct"><u:mandatory/><u:msg titleId="wd.jsp.useDate" alt="사용일" /></td>
	<td class="body_lt"><u:calendar id="useYmd" titleId="wd.jsp.useDate" /></td>
</tr></c:if>
<tr>
	<td class="head_ct"><u:mandatory/><u:msg titleId="wd.jsp.cnt" alt="수량" /></td><c:if
		test="${param.modTypCd ne 'use'}">
	<td><u:input
			id="modCnt" name="modCnt" maxLength="5" maxFloat="25" minFloat="-25"
			valueOption="number" valueAllowed=".-" titleId="wd.jsp.cnt" mandatory="Y" /></td></c:if><c:if
		test="${param.modTypCd eq 'use'}">
	<td><table cellspacing="0" cellpadding="0" border="0">
		<tr><u:radio name="modCnt" value="0.5" title="0.5"
			/><u:radio name="modCnt" value="1" title="1" checked="true"
			/></tr>
		</table></td></c:if>
</tr>
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