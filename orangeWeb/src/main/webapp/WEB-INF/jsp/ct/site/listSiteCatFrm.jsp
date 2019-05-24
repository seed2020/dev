<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function sltedCat(){
	var $form = $('#listSiteCatForm');
	$form.attr('method','post');
	$form.attr('action','./sendSiteCatId.do?menuId=${menuId}&ctId=${ctId}');
	$form.attr('target','siteCat');
	$form.submit();
}

<%// [버튼] 카테고리 삭제 %>
function delSiteCat(){
		var $form = $('#listSiteCatForm');
		$form.attr('method','post');
		$form.attr('action','./transSiteCatDel.do?menuId=${menuId}&ctId=${ctId}');
		$form.attr('target','siteCat');
		$form.submit();
}

$(document).ready(function() {
	setUniformCSS();
	$("#listSiteCatForm").find(".listtable").attr("cellspacing", "0");
	$("#listSiteCatForm").find(".blank").remove();
});
//-->
</script>
<form id="listSiteCatForm">
<input type="hidden" id="fncUid" name="fncUid" value="${fncUid}" />
<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
<u:listArea>
	<tr>
<!-- 	<td colspan="2"><select id="catSlt" name="catSlt" size="5" style="width: 355px;" onclick="javascript:sltedCat()"> -->
	<td colspan="2"><select id="catSlt" name="catSlt" size="5" style="width: 355px;" >
		<c:forEach var="siteCatVo" items="${siteCatList}" varStatus="status">
			<option value="${siteCatVo.catId}">${siteCatVo.catNm}</option>
		</c:forEach>
		</select></td>
	<td><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="javascript:delSiteCat()" /></td>
	</tr>
</u:listArea>
</form>
