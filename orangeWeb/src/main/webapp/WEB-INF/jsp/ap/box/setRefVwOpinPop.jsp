<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// [열람확인] 버튼 클릭 %>
function saveRefVwOpin(){
	var opin = $("#setRefVwOpinForm #opin").val();
	callAjax('./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}', {process:'processRefVw',apvNo:'${param.apvNo}', refVwOpinCont:opin}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok'){
			moveToNextDoc(true);
		}
	});
}<%
// 확인 버튼 - callback 함수 호출 %>
function processOpinCallback(){
	var opin = $("#setRefVwOpinForm #opin").val();
	${param.callback}(opin);
}<%
// onload %>
//$(document).ready(function() {
//});
//-->
</script>

<div style="width:600px">
<form id="setRefVwOpinForm">

<u:listArea>
	<tr>
	<td width="17%" class="head_ct"><u:msg titleId="ap.doc.opin" alt="의견" /></td>
	<td width="83%"><u:textarea id="opin" value="" titleId="ap.doc.opin" maxByte="800" rows="5" style="width:97%" /></td>
	</tr>
</u:listArea>

<u:buttonArea><c:if
		test="${empty param.callback}">
	<u:button titleId="ap.term.cfrmRefVw" onclick="saveRefVwOpin();" alt="열람확인" auth="W" /></c:if><c:if
		test="${not empty param.callback}">
	<u:button titleId="cm.btn.confirm" alt="확인" onclick="processOpinCallback()" auth="W" /></c:if>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</form>
</div>
