<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var i=0;
var pasInt =0;

$(function(){
	var $reqsCtId = $("#reqsCtId").val();
	$("#rjtCtId").val($reqsCtId);
	
});

<% // 폼 전송 %>
function formSubmit(){
	var $selectCtClose = $("#selectCtCloseList");
	var selectCloseCtIds = [];
	var $closeOp = $("#rjtOpCont").val();
	var $checkedLength = $('input:checkbox:checked').length;
	var $ctCloseListTbl = $("#listArea");
	var $apprYnVal = '${apprYn}';
	if($checkedLength == '0'){
		alert("<u:msg titleId="ct.msg.noSelectCt" alt="커뮤니티를 선택해주시기 바랍니다." />");
	}else{
		
		$ctCloseListTbl.find("tr[name='ctCloseVo']").each(function(){
			var $selectedCloseCtId = $(this).find("#closeCtId").val();
			$(this).find("#checkFlag").each(function(){
				if($(this).is(":checked")||$(this).attr("checked") == "checked"){
					if($(this).attr("tr")!='headerTr'){
						selectCloseCtIds.push($selectedCloseCtId);
					}
				}
			});

		});
		if($apprYnVal == 'Y'){
			if(confirmMsg("ct.cfrm.close")){
				callAjax('./setCloseAppr.do?menuId=${menuId}', {selectCloseCtIds:selectCloseCtIds, closeOp:$closeOp, signal:$apprYnVal}, function(data){
					if (data.message != null) {
						alert(data.message);
					}
					if (data.result == 'ok') {
						location.href = './listCloseReqs.do?menuId=${menuId}';
					}
				});
			}
		}else{
			if(confirmMsg("ct.cfrm.closeRjt")){
				callAjax('./setCloseAppr.do?menuId=${menuId}', {selectCloseCtIds:selectCloseCtIds, closeOp:$closeOp, signal:$apprYnVal}, function(data){
					if (data.message != null) {
						alert(data.message);
					}
					if (data.result == 'ok') {
						location.href = './listCloseReqs.do?menuId=${menuId}';
					}
				});
			}
		}
	}
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
	<td class="head_ct" width="15%">
	<c:if test="${apprYn == 'Y'}">
		<u:msg titleId="ct.cols.closeOp" alt="폐쇄사유" />
	</c:if>
	<c:if test="${apprYn == 'N'}">
		<u:msg titleId="ct.cols.closeRjtOp" alt="폐쇄거부 사유" />
	</c:if>
	</td>
	<td class="body_ct">
		<input id="rjtOpCont" name="rjtOpCont" value="" style="width:97%;" />
		<input id="rjtCtId" name="rjtCtId" value="" type="hidden" style="width:97%;" />
	</td>
	</tr>
</u:listArea>


<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="javascript:formSubmit();" alt="확인" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>