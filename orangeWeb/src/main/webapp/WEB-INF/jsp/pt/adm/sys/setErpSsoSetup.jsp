<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
function doSubmit(){
	if(validator.validate('erpSsoSetupForm')){
		var $form = $('#erpSsoSetupForm');
		$form.attr("action","./transErpSsoSetup.do");
		$form.attr("target","dataframe");
		$form.submit();
	}
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.erpSso.enable" alt="ERP SSO" menuNameFirst="true" />
<form id="erpSsoSetupForm">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="setupClsId" value="${setupClsId}" />

<u:listArea>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="or.cols.userId" alt="사용자 ID" /></td>
		<td class="bodybg_lt"><u:checkArea>
			<u:radio name="${setupClsId}_erpSsoUserId" value="refVa1"  alt="참조값1" titleId="cols.refVa1" checked="${erpSsoMap.erpSsoUserId != 'refVa2'}" />
			<u:radio name="${setupClsId}_erpSsoUserId" value="refVa2"  alt="참조값2" titleId="cols.refVa2" checked="${erpSsoMap.erpSsoUserId == 'refVa2'}" />
		</u:checkArea></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.erp.erpTkUrl" alt="ERP 티켓 URL" /></td>
		<td><u:input id="${envId}" name="${setupClsId}_erpTkUrl" titleId="pt.erp.erpTkUrl" value="${erpSsoMap.erpTkUrl}" style="width:98%" maxByte="200" /></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.erp.erpSsoUrl" alt="ERP SSO URL" /></td>
		<td><u:input id="${envId}" name="${setupClsId}_erpSsoUrl" titleId="pt.erp.erpSsoUrl" value="${erpSsoMap.erpSsoUrl}" style="width:98%" maxByte="200" /></td>
	</tr>
</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:doSubmit();" auth="SYS" />
</u:buttonArea>