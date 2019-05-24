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
	var selectCtIds = [];
	var closeOp = $("#rjtOpCont").val();
	var $checkedLength = $('input:checkbox:checked').length;
	var $ctListTbl = $("#listArea");
	if($checkedLength == '0'){
		alert('${ctSelect}');
	}else{
		$ctListTbl.find("tr[name='cmntEstbVo']").each(function(){
			var $selectedCtId = $(this).find("#cmntId").val();
			$(this).find("#checkFlag").each(function(){
				
				if($(this).is(":checked")||$(this).attr("checked") == "checked"){
					if($(this).attr("tr")!='headerTr'){
						selectCtIds.push($selectedCtId);
					}
				}
				
			});
		});
		//ct.cfrm.close = 폐쇄하시겠습니까?
		if (confirmMsg("ct.cfrm.close")) {
			callAjax('./ajaxCtClose.do?menuId=${menuId}', {selectCtIds:selectCtIds, closeOp:closeOp }, function(data){
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.href = './listCmInfo.do?menuId=${menuId}';
				}
			});
		}else{
			return;
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
		<u:msg titleId="ct.cols.closeOp" alt="폐쇄사유" />
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