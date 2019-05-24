<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function doSubmit(){
	var $form = $('#antiCopyArea');
	$form.attr("action","./transAntiCopy.do");
	$form.attr("target","dataframe");
	if($("#antiCopyArea input[type='checkbox']:checked").length > 0){
		$("#antiCopyArea input[type='hidden']").remove();
	}
	$form.submit();
}
<%// 첫번째 콤보가 변경 될때 %>
function checkAntiCopyAll(checked){
	$("#antiCopyArea input[type='checkbox']").prop("checked", checked);
	$.uniform.update("#antiCopyArea input");
}

$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<div style="width:250px">
<form id="antiCopyArea">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea>
	<u:decode srcId="_lang" tgtValue="ko" var="cdLoc" value="공통코드 : 포털 / 페이지별레코드수" elseValue="" byAdmin="true" />
	<c:forEach items="${moduleCdList}" var="moduleCd" varStatus="status">
	<tr>
		<u:set test="${moduleCd.cd == '_ALL'}" var="onclick" value="checkAntiCopyAll(this.checked)" elseValue="" />
		<td width="50%" class="head_lt">${moduleCd.rescNm}</td><u:convertMap
			srcId="antiCopyMap" attId="${moduleCd.cd}" var="checkValue" />
		<td style="padding-left:3px"><u:checkbox id="${moduleCd.cd}" name="${setupClsId.concat(moduleCd.cd)}" value="Y" checkValue="${checkValue}" onclick="${onclick}" /></td>
	</tr>
	</c:forEach>
</u:listArea>
<input type="hidden" name="${setupClsId.concat('anticopy')}" value="N" />
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="doSubmit()" auth="A" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>