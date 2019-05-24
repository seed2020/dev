<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

//request.setAttribute("modTypCds", new String[]{"forw","cre","calc","ongo"});

%><script type="text/javascript">
<!--
function saveModifyData(modTypCd){
	/* if(validator.validate('wdModifyForm')){
		var $form = $("#wdModifyForm");
		$form.attr("action","./transModifyDel.do");
		$form.attr("target","dataframe");
		$form.submit();
	} */
}<%--
데이터 초기화 --%>
function clearData(){<%--
	wd.cfm.clearData=전체 데이터를 삭제하시겠습니까 ? --%>
	if(!confirmMsg("wd.cfm.clearData")){
		return
	}
	callAjax('./processClearDataAjx.do?menuId=${menuId}', null, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if(data.result == 'ok') reload();
	});
}
// -->
</script>
<div style="width:500px">
<form id="setInitDataForm">
<input type="hidden" name="menuId" value="${menuId}" />

<u:listArea id="modifyDataArea" colgroup="20%,80%">
<tr>
	<td class="head_ct"><u:mandatory/><u:msg titleId="wd.jsp.remark" alt="비고" /></td>
	<td><u:input id="note" name="note" maxByte="80" titleId="wd.jsp.remark" mandatory="Y" style="width:96%" /></td>
</tr>
</u:listArea>
</form>

<u:buttonArea>
	<u:button id="cmdModify" titleId="cm.btn.allDel" alt="전체삭제" href="javascript:clearData();" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>