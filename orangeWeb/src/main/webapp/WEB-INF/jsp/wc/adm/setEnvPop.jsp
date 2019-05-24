<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Enumeration, java.util.ArrayList" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // 저장 - 버튼 클릭 %>
function saveEnv(){
	var $form = $("#setEnvForm");
	$form.attr('method','post');
	$form.attr('action','./transEnv.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form.submit();
	dialog.close('setEnvDialog');
};
$(document).ready(function() {
	//setUniformCSS();
});
//-->
</script>
<div style="width:400px; ">

<form id="setEnvForm">
<input type="hidden" name="menuId" value="${menuId}" />
<c:set var="colgroup" value="25%,75%"/>
<u:listArea colgroup="${colgroup }" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="wc.cols.guest.acceptYn" alt="참석자 수락여부" /></td>
	<td class="bodybg_lt"><u:checkArea><u:radio value="Y" name="acceptYn" titleId="cm.option.yes" checked="${envConfigMap.acceptYn == 'Y'}" 
	/><u:radio value="N" name="acceptYn" titleId="cm.option.no" checked="${empty envConfigMap.acceptYn || envConfigMap.acceptYn == 'N'}" /></u:checkArea></td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></td>
	<td class="bodybg_lt"><select name="schdlKndCd"
	><u:option value="" titleId="cm.option.all" alt="전체선택"
	/><c:forEach items="${schdlKndCdList}" var="cd" varStatus="status"
	><u:option value="${cd[0]}" title="${cd[1] }" checkValue="${envConfigMap.schdlKndCd }"/></c:forEach></select></td>
</tr>
</u:listArea>
</form>
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveEnv()" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>