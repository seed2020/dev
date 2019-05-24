<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="saveDiscDoc"/>
<script type="text/javascript">
<!--<%// [저장] 심의일괄처리 %>
function saveDisc(){
	var param = new ParamMap().getData("discDocForm");
	${callback}(param);
}
//-->
</script>
<div style="width:250px;">
<form id="discDocForm">
<u:listArea colgroup="25%," noBottomBlank="true">
<tr>
	<td></td>
	<td><u:checkArea>
		<u:radio name="statCd" value="A" titleId="cm.btn.apvd" checked="true"/>
		<u:radio name="statCd" value="R" titleId="cm.btn.rjt" />
	</u:checkArea></td>
</tr>
<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.rjtOpin" alt="반려의견" /></td>
	<td colspan="3" class="body_lt"><u:textarea id="rjtOpin" value="" titleId="cols.rjtOpin" maxByte="1000" style="width:98%" rows="3" /></td>
	</tr>
</u:listArea>
</form>
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveDisc();" auth="W" />
<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>