<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 반송 버튼 클릭 %>
function retnDoc(){
	var param = new ParamMap().getData($("#setRetnForm")[0]);
	var retnOpin = param.get("retnOpin").trim();
	if(retnOpin==''){<%
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [심사의견] %>
		alertMsg("cm.input.check.mandatory", ["#ap.jsp.retnOpin"]);
		$("#setCensrForm #pichOpinCont").focus();
		return;
	}
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"retnDoc",apvNo:"${param.apvNo}",sendSeq:"${param.sendSeq}",retnOpin:retnOpin}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result) toList();
}
//-->
</script>

<div style="width:600px">
<form id="setRetnForm">

<u:listArea colgroup="17%,83%">
	<tr>
	<td class="head_ct"><u:msg titleId="ap.jsp.retnOpin" alt="반송의견" /></td>
	<td ><u:textarea id="retnOpin" value="" titleId="ap.jsp.retnOpin" maxByte="800" rows="5" style="width:97%" /></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="ap.btn.retn" alt="반송" href="javascript:retnDoc();" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
