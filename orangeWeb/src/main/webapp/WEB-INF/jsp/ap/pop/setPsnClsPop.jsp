<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function saveCls(){
	if(validator.validate('setPsnClsForm')){
		var $form = $("#setPsnClsForm");
		$form.attr('action','./transPsnCls.do');
		$form.attr('target','dataframe');
		$form.submit();
	}
}
$(document).ready(function() {
	if(browser.ie && browser.ver<9){
		$("#dataframe").attr("src","/cm/util/reloadable.do");
	}
});
//-->
</script>

<div style="width:450px">
<form id="setPsnClsForm">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="bxId" value="${param.bxId}" /><c:if test="${not empty param.psnClsInfoId}">
<u:input id="psnClsInfoId" type="hidden" value="${param.psnClsInfoId}" /></c:if><c:if test="${not empty param.psnClsInfoPid}">
<u:input id="psnClsInfoPid" type="hidden" value="${param.psnClsInfoPid}" /></c:if>

<u:listArea>
	<tr>
	<td width="30%" class="head_lt"><u:mandatory /><u:msg titleId="cols.clsNm" alt="분류명" /></td>
	<td><u:input id="psnClsInfoNm" titleId="cols.clsNm" value="${apPsnClsInfoDVo.psnClsInfoNm}" /></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveCls();" alt="저장" auth="W" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>