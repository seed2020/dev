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
<u:title titleId="bb.sns.facebook" alt="페이스북" type="small" />
<c:set var="colgroup" value="25%,75%"/>
<u:listArea colgroup="${colgroup }" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="bb.sns.appId" alt="APP ID" /></td>
	<td class="bodybg_lt"><u:convertMap srcId="envConfigMap" attId="appId.facebook" var="facebookId" 
		/><u:input id="appId.facebook" titleId="bb.sns.appId" value="${facebookId}" maxByte="240" mandatory="Y" style="width:95%;"/></td>
</tr>
</u:listArea>
<u:blank />
<u:title titleId="bb.sns.twitter" alt="트위터" type="small" />
<u:listArea colgroup="${colgroup }" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="bb.sns.appId" alt="APP ID" /></td>
	<td class="bodybg_lt"><u:convertMap srcId="envConfigMap" attId="appId.twitter" var="twitterId" 
		/><u:input id="appId.twitter" titleId="bb.sns.appId" value="${twitterId}" maxByte="240" mandatory="Y" style="width:95%;"/></td>
</tr>
</u:listArea>
</form>
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveEnv()" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>