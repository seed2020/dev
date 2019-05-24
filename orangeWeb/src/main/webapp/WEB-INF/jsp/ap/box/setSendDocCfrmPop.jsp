<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 발송 버튼 클릭 %>
function sendDocWithRefDoc(){
	var chk = $("#sendWithRefDocArea [name='sendWithRefDoc']");
	var area = $("#docDataArea #sendInfoArea");
	area.html("");
	if(chk.length>0 && chk[0].checked){
		area.appendHidden({'name':'sendWithRefDocYn','value':'Y'});
	}
	submitDoc('sendDoc');
	dialog.close('setSendDocCfrmDialog');
}
-->
</script>
<div style="width:250px" >

<u:listArea id="sendWithRefDocArea">
<tr>
<td style="padding:8px">
	<u:msg titleId="ap.cfrm.sendDoc" alt="발송 하시겠습니까 ?" />
	<br/><br/><br/>
	<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:checkbox name="sendWithRefDoc" value="Y" titleId="ap.cfg.sendWithRefDoc" alt="참조문서 유지" /></td>
		</tr>
	</tbody></table>
</td>
</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="ap.btn.sendDoc" href="javascript:sendDocWithRefDoc();" alt="발송" auth="W" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>
</div>