<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var i=0;
var pasInt =0;

$(function(){
	// 미승인 처리하고자 하는 커뮤니티 ID를 셋팅해준다.
	var $reqsCtId = '${reqsCtId}';
	$("#rjtCtId").val($reqsCtId);
});

<% // 폼 전송 %>
function formSubmit(){
	var $form = $("#setRjtOpForm");
	$form.attr('method', 'POST');
	$form.attr('action', './transSetRjtOpContSave.do?menuId=${menuId}');
	$form.submit();
}

$(document).ready(function() {
	setUniformCSS();
});

//-->
</script>

<div style="width:750px">
<form id="setRjtOpForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<input id="setSchdlGrp" name="setSchdlGrp" value="setSchdlGrp" type="hidden"/>

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td class="head_ct" width="70px"><u:msg titleId="ct.cols.rjtOp" alt="반려사유" /></td>
	<td class="body_ct">
		<u:input id="rjtOpCont" name="rjtOpCont" titleId="cols.cont"  value="" style="width:650px;" maxByte="255"/>
		<input id="rjtCtId" name="rjtCtId" value="" type="hidden" style="width:650px;" />
	</td>
	</tr>
</u:listArea>


<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="javascript:formSubmit();" alt="확인" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>