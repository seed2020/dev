<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	request.setAttribute("trOpen", "<tr>");
	request.setAttribute("trClose", "</tr>");
%>
<script type="text/javascript" >
<!--
<%// 계열사 %>
function saveAffiliate(){
	var affiliates=[];
	$("#setAffiliatesPop input:checked").each(function(){
		affiliates.push($(this).val());
	});
	
	callAjax('./transCompAffiliatesAjx.do?menuId=${menuId}', {compId:'${param.compId}', affiliates:affiliates.join(',')}, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.result == 'ok'){
			dialog.close('setAffiliatesDialog');
			dialog.open('setCompDialog','<u:msg titleId="pt.jsp.listComp.title"/>','./setCompPop.do?menuId=${menuId}&compId=${param.compId}');
		}
	});
}
//-->
</script>
<div style="width:750px">
<u:listArea>
<tr><td style="padding:8px; max-height:800px; overflow-y:auto">
<form id="setAffiliatesPop">
<table border="0" cellpadding="0" cellspacing="0">
<colgroup>
	<col />
	<col width="22%" />
	<col />
	<col />
	<col width="22%" />
	<col />
	<col />
	<col width="22%" />
	<col />
	<col />
	<col width="22%" />
	<col />
</colgroup>
<tbody>
	<tr>
	<c:forEach items="${compList}" var="ptCompBVo" varStatus="status"><c:if
		test="${not status.first and status.index % 4 eq 0}">
	${trClose}${trOpen}</c:if>
	<u:checkbox value="${ptCompBVo.compId}" name="affiliate" title="${ptCompBVo.rescNm}"
		checked="${ptCompBVo.compId eq param.compId or affiliateIds.contains(ptCompBVo.compId)}"
		disabled="${ptCompBVo.compId eq param.compId ? 'Y' : '' }" />
	</c:forEach>
	</tr>
</tbody>
</table>
</form>
</td></tr>
</u:listArea>


<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveAffiliate();" alt="저장" auth="SYS" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>
</div>