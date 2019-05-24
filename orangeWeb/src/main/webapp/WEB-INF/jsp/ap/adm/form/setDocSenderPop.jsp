<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%
// openStylePop() - setFormEdit.jsp
%><%
// 스타일 소버튼 후 저장 %>
function applyStylePop(formTxtTypCd){
	var param = getStyleData();<%// getStyleData() - setDocStylePop.jsp %>
	param.setData(formTxtTypCd+"PopArea", formTxtTypCd+"-");
}<%
// 텍스트 입력값이 없으면 사용여부 언체크 함 %>
function onTxtContBlur(obj){
	obj.value = obj.value.trim();
	if(obj.value!=''){
		var id = $(obj).attr("id");
		var $useYn = $("#"+(id.substring(0, id.indexOf('-')))+"-useYn");
		if(!$useYn[0].checked) $useYn.trigger('click');
	}
}<%
// 사용여부 체크박스 클릭 %>
function onUseYnClick(obj){
	var id = $(obj).attr("id");
	if(id!=null){
		if(id.endsWith("-useYn")) id = id.substring(0,id.length-6);
		if(obj.checked){
			if(id=='docSender'){
				$('#setSenderPopForm #docSender-txtCont').val('${docSenderTxt}');
			} else if(id=='docReceiver'){
				$('#setSenderPopForm #docReceiver-txtCont').val('${docReceiverTxt}');
			}
		} else {
			$('#setSenderPopForm #'+id+'-txtCont').val('');
		}
	}
}<%
// 확인 버튼 클릭 %>
function setSender(){
	var param = new ParamMap().getData("setSenderPopForm");
	if(param.get("docSender-useYn")!='Y' && param.get("docReceiver-useYn")!='Y'){
		alertMsg("ap.cfg.noValidVa");<%//ap.cfg.noValidVa=설정된 값이 없습니다.%>
		return;
	} else {
		setSenderData(param);<%// - setFormEdit.jsp %>
	}
	dialog.close("setDocSenderDialog");
}
$(document).ready(function() {
	var param = new ParamMap().getData("senderDataArea");
	if(param.isEmpty()){
		param.put("docSender-useYn","Y");
		param.put("docReceiver-useYn","Y");
	} else {
		if(param.get("docSender-useYn")!="Y"){
			param.put("docSender-txtCont","");
		}
		if(param.get("docReceiver-useYn")!="Y"){
			param.put("docReceiver-txtCont","");
		}
	}
	param.setData("setSenderPopForm");
});
//-->
</script>
<div style="width:650px">
<form id="setSenderPopForm">
<u:listArea>

	<tr>
	<td width="20%" class="head_lt"><u:msg titleId="ap.cmpt.sender" alt="발신명의" /></td>
	<td class="body_lt" id="docSenderPopArea"><u:checkArea>
		<u:checkbox value="Y" id="docSender-useYn" name="docSender-useYn" titleId="cols.useYn" onclick="onUseYnClick(this);" alt="사용여부" />
		<td><u:input id="docSender-txtCont" titleId="ap.cmpt.sender" value="${docSender.txtCont}" style="width:200px" readonly="Y" />
			<u:input type="hidden" id="docSender-txtFontVa" value="${docSender.txtFontVa}" />
			<u:input type="hidden" id="docSender-txtStylVa" value="${docSender.txtStylVa}" />
			<u:input type="hidden" id="docSender-txtSize" value="${docSender.txtSize}" />
			<u:input type="hidden" id="docSender-txtColrVa" value="${docSender.txtColrVa}" /></td>
		<td><u:buttonS titleId="ap.cmpt.style" alt="스타일" onclick="openStylePop('docSender', 'ap.cmpt.sender');" /></td>
	</u:checkArea></td>
	</tr>
	
	<tr>
	<td width="20%" class="head_lt"><u:msg titleId="ap.cmpt.receiver" alt="수신처" /></td>
	<td class="body_lt" id="docReceiverPopArea"><u:checkArea>
		<u:checkbox value="Y" id="docReceiver-useYn" name="docReceiver-useYn" titleId="cols.useYn" onclick="onUseYnClick(this);" alt="사용여부" />
		<td><u:input id="docReceiver-txtCont" titleId="ap.cmpt.receiver" value="${docReceiver.txtCont}" style="width:200px" readonly="Y" />
			<u:input type="hidden" id="docReceiver-txtFontVa" value="${docReceiver.txtFontVa}" />
			<u:input type="hidden" id="docReceiver-txtStylVa" value="${docReceiver.txtStylVa}" />
			<u:input type="hidden" id="docReceiver-txtSize" value="${docReceiver.txtSize}" />
			<u:input type="hidden" id="docReceiver-txtColrVa" value="${docReceiver.txtColrVa}" /></td>
		<td><u:buttonS titleId="ap.cmpt.style" alt="스타일" onclick="openStylePop('docReceiver', 'ap.cmpt.receiver');" /></td>
	</u:checkArea></td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setSender();" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>