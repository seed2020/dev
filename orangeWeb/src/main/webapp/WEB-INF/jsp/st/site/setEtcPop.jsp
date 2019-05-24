<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="setSiteCont"/>
<script type="text/javascript">
<!--<%// [요청] 저장 %>
function setEtc(){
	var arr={cont:$('#cont').val()};
	${callback}(arr);
	dialog.close('setEtcDialog');
}
$(document).ready(function() {
	<c:if test="${!empty stSiteBVo && empty stSiteBVo.cont}">
	var cont = getSiteCont();
	if(cont!=undefined) $('#etcListArea #cont').val(cont);
	</c:if>	
});
//-->
</script>

<div style="width:400px;">
<u:listArea id="etcListArea" colgroup="20%," >
<tr>
	<td class="head_lt"><u:msg titleId="cols.cont" alt="내용" /></td>
	<td class="bodybg_lt"><u:textarea id="cont" value="${stSiteBVo.cont}" titleId="cols.cont" maxByte="240" style="width:95.5%;" rows="4" /></td>	
</tr>
</u:listArea>
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:setEtc();" auth="A" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>