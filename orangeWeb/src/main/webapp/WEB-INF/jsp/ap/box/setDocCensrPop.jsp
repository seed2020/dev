<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 확인 버튼 클릭 %>
function doCensr(censrStatCd){
	var param = new ParamMap().getData($("#setCensrForm")[0]);
	var pichOpinCont = param.get("pichOpinCont").trim();
	if(censrStatCd=='rejt'){<%// apvd:심사승인, rejt:심사반려 %>
		if(pichOpinCont==''){<%
			// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [심사의견] %>
			alertMsg("cm.input.check.mandatory", ["#ap.jsp.doc.censrOpin"]);
			$("#setCensrForm #pichOpinCont").focus();
			return;
		}
	}
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"processCensr",apvNo:"${param.apvNo}",censrStatCd:censrStatCd,pichOpinCont:pichOpinCont}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result) toList();
}
//-->
</script>

<div style="width:600px">
<form id="setCensrForm">

<u:listArea><c:if
		test="${not empty apOngdPichDVo.pichOpinCont}">
	<tr>
	<td width="17%" class="head_ct"><u:msg titleId="ap.btn.reqCensrOpin" alt="심사요청의견" /></td>
	<td width="83%" class="body_lt"><u:out value="${apOngdPichDVo.pichOpinCont}" /></td>
	</tr></c:if>
	<tr>
	<td width="17%" class="head_ct"><u:msg titleId="ap.btn.censrOpin" alt="심사의견" /></td>
	<td width="83%"><u:textarea id="pichOpinCont" value="" titleId="ap.btn.censrOpin" maxByte="800" rows="5" style="width:97%" /></td>
	</tr>
</u:listArea>

<u:buttonArea><c:if
		test="${param.censrStatCd == 'apvd'}">
	<u:button titleId="ap.btn.censrApvd" alt="심사승인" onclick="doCensr('apvd');" auth="W" /></c:if><c:if
		test="${param.censrStatCd == 'rejt'}">
	<u:button titleId="ap.btn.censrRejt" alt="심사반려" onclick="doCensr('rejt');" auth="W" /></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
