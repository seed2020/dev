<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%--
[버튼] 등록 --%>
function confirmProc(){
	var actCd = '${param.actCd}';
	var withOpin = '${param.opin}';
	
	var opin = $("#procForm #opin").val();
	if(actCd=='rejt' || (actCd=='askApv' && withOpin!='N')){
		if(opin.trim()==''){
			alertMsg('cm.input.check.mandatory', [$("#procForm #opinTitle").text()]);<%-- cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. --%>
			return;
		}
	}
	
	if(actCd=='apvd' || actCd=='rejt'){
		processAct(actCd, opin);
	} else if(actCd=='askApv'){
		savePrj(actCd, opin);
	}
	dialog.close('setProcDialog');
}
$(document).ready(function() {
	var actCd = '${param.actCd}';
	if(actCd=='askApv'){
		var opin = $("#prjOpinArea input[name=modCont]");
		if(opin.length>0){
			$("#procForm #opin").val(opin.val());
		}
	}
	
});
//-->
</script>
<div style="width:600px;">
<form id="procForm">
<u:listArea colgroup="20%,80%">
<tr>
	<td class="head_ct" id="opinTitle"><c:if
		test="${param.actCd eq 'askApv'}"><u:msg titleId="wp.opinAskApv" alt="변경 내용 및 사유" /></c:if><c:if
		test="${param.actCd eq 'apvd'}"><u:msg titleId="wp.opinApvd" alt="승인 의견" /></c:if><c:if
		test="${param.actCd eq 'rejt'}"><u:msg titleId="wp.opinRejt" alt="반려 의견" /></c:if></td>
	<td class="${not empty viewMode ? 'body_lt' : ''}"><textarea id="opin" rows="7" style="width:97%" ></textarea></td>
</tr>
</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:confirmProc();" alt="확인" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>