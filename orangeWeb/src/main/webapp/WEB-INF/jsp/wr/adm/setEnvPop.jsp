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
<c:set var="colgroup" value="35%,65%"/>
<u:listArea colgroup="${colgroup }" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="wr.cols.schdlKndCd.useYn" alt="일정대상 사용여부" /></td>
	<td class="bodybg_lt"><u:checkArea><u:radio name="tgtUseYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${envConfigMap.tgtUseYn }"  inputClass="bodybg_lt"
	/><u:radio name="tgtUseYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${envConfigMap.tgtUseYn }" inputClass="bodybg_lt" checked="${empty envConfigMap.tgtUseYn }"
	/></u:checkArea></td></tr><tr>
	<td class="head_lt"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></td>
	<td class="bodybg_lt"><select id="schdlKndCd" name="schdlKndCd"
	><c:forEach items="${schdlKndCdList}" var="cd" varStatus="status"
	><u:option value="${cd[0]}" title="${cd[1] }" checkValue="${envConfigMap.schdlKndCd }"/></c:forEach></select></td>
</tr><tr>
	<td class="head_lt"><u:msg titleId="wc.cols.guest.useYn" alt="참석자 사용여부"/></td>
	<td class="bodybg_lt"><u:checkArea><u:radio name="guestUseYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${envConfigMap.guestUseYn }"  inputClass="bodybg_lt"
	/><u:radio name="guestUseYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${envConfigMap.guestUseYn }" inputClass="bodybg_lt" checked="${empty envConfigMap.guestUseYn }"
	/></u:checkArea></td>
</tr><tr>
	<td class="head_lt"><u:msg titleId="wr.cols.allComp.useYn" alt="타회사 조회 사용여부" /></td>
	<td class="bodybg_lt"><u:checkArea><u:radio name="allCompUseYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${envConfigMap.allCompUseYn }"  inputClass="bodybg_lt"
	/><u:radio name="allCompUseYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${envConfigMap.allCompUseYn }" inputClass="bodybg_lt" checked="${empty envConfigMap.allCompUseYn }"
	/></u:checkArea></td></tr>
</u:listArea>
</form>
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveEnv()" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>