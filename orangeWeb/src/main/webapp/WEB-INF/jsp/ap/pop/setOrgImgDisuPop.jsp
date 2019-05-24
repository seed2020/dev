<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function saveDisu(){
	if(validator.validate('disuImageForm')){
		var $form = $("#disuImageForm");
		$form.attr('action','./transOrgImgDisu.do');
		$form.attr('target','dataframe');
		$form.submit();
	}
}
$(document).ready(function() {
	if(browser.ie && browser.ver<9){
		$("#dataframe").attr("src","${_cxPth}/cm/util/reloadable.do");
	}
});
//-->
</script>
<div style="width:400px">
<form id="disuImageForm" method="get" >
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="orgId" value="${param.orgId}" />
<input type="hidden" name="seq" value="${param.seq}" />

<u:listArea>
	<tr>
	<th width="30%" class="head_lt"><u:msg titleId="or.cols.disuRson" alt="폐기 사유" /></th>
	<td width="70%"><u:textarea id="chnRson" titleId="or.cols.disuRson" alt="폐기 사유" value="" rows="5"
		style="width:95%" maxByte="800" mandatory="Y" /></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="saveDisu();" alt="확인" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>