<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params"/>
<u:params var="paramsForList" excludes="reprtNo"/>
<script type="text/javascript">
<!--
<% // [하단버튼:삭제] 삭제 %>
function delWorkReprt() {
	callAjax('./${transDelPage}Ajx.do?menuId=${menuId}', {reprtNo:'${param.reprtNo}'}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.href = './${listPage}.do?${paramsForList}';
		}
	});
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<u:title menuNameFirst="true"/>
<% // 폼 필드 %>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="12%"/><col width="38%"/><col width="12%"/><col width="38%"/></colgroup>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt" colspan="3"><u:out value="${wjWorkReprtBVo.subj}"/></td>
	</tr><tr>
	<td class="body_lt" colspan="4"><div style="overflow:auto;" class="editor printNoScroll" ><u:clob lobHandler="${lobHandler }"/></div></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
	<td colspan="3">
		<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="wj" mode="view" /></c:if>
	</td>
	</tr>
</table>
</div>

<% // 하단 버튼 %>
<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
	<u:button titleId="cm.btn.mod" alt="수정" onclick="setLog();" href="./${setPage}.do?${paramsForList}&reprtNo=${param.reprtNo}"  auth="W" ownerUid="${wjWorkReprtBVo.regrUid}" />	
	<u:button titleId="cm.btn.del" alt="삭제" onclick="delWorkReprt();" auth="W" ownerUid="${wjWorkReprtBVo.regrUid}" />
	<u:button titleId="cm.btn.list" alt="목록" href="./${listPage}.do?${paramsForList}"/>
</u:buttonArea>