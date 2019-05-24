<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 저장 버튼 클릭 %>
function saveManlSendCmpl(){
	var $area = $("#setManlSendCmplPopArea");
	processCfrmRecvAjax('manlSendCmpl', '${param.apvNo}', '${param.sendSeqs}'.split(','), $area.find("#hdlrNm").val(), $area.find("#hdlDt").val());
	dialog.close('setManlSendCmplDialog');
}
//-->
</script>
<div style="width:400px">

<u:listArea id="setManlSendCmplPopArea" colgroup="30%,70%">
	<tr><td class="head_ct"><u:msg titleId="ap.jsp.recvr" alt="접수자" /></td>
		<td><u:input id="hdlrNm" titleId="ap.jsp.recvr" maxByte="20" /></td>
	</tr>
	<tr><td class="head_ct"><u:msg titleId="ap.list.recvDd" alt="접수일자" /></td>
		<td><u:calendar id="hdlDt" titleId="ap.list.recvDd" value="today" /></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveManlSendCmpl();" alt="저장" auth="${param.bxId=='myBx' ? 'W' : 'A'}" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>