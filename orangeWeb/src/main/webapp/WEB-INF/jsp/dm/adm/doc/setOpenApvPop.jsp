<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="saveOpenApv"/>
<script type="text/javascript">
<!--<%// [저장] 심의일괄처리 %>
function setOpenApv(){
	var statCd = $('#openApvDocForm').find('input[type="radio"]:checked').val();
	if(statCd == 'R' && $('#openApvDocForm #rjtOpin').val() == ''){
		alertMsg('cm.input.check.mandatory',['<u:msg titleId="cols.rjtOpin" alt="반려의견" />']);
		$('#openApvDocForm #rjtOpin').focus();
		return;	
	}
	var param = new ParamMap().getData("openApvDocForm");
	${callback}(param);
}
//-->
</script>
<div style="width:450px;">
<form id="openApvDocForm">
<u:listArea colgroup="29%," noBottomBlank="true">
<c:if test="${empty dmPubDocTgtDVo }">
<tr>
	<td></td>
	<td><u:checkArea>
		<u:radio name="statCd" value="A" titleId="cm.btn.apvd" checked="true"/>
		<u:radio name="statCd" value="R" titleId="cm.btn.rjt" />
	</u:checkArea></td>
</tr>
</c:if>
<u:set var="rjtReadonly" test="${!empty dmPubDocTgtDVo }" value="Y" elseValue="N"/>
<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.rjtOpin" alt="반려의견" /></td>
	<td colspan="3" class="body_lt"><u:textarea id="rjtOpin" value="${!empty dmPubDocTgtDVo ? dmPubDocTgtDVo.rjtOpin : ''}" titleId="cols.rjtOpin" maxByte="400" style="width:97%" rows="4" readonly="${rjtReadonly }"/></td>
	</tr>
</u:listArea>
</form>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
<c:if test="${rjtReadonly eq 'N' }"><u:button titleId="cm.btn.save" alt="저장" href="javascript:setOpenApv();" auth="A" /></c:if>
<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>