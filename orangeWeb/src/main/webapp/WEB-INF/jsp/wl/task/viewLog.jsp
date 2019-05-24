<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="logNo"/>
<script type="text/javascript">
<!--<% // [버튼클릭] - 업무일지 선택 팝업  %>
function findLogListPop(logTyp){
	var url='./viewTaskLogPop.do?menuId=${menuId}&multi=N';
	<c:if test="${!empty param.logNo}">url+='&logNo=${param.logNo}'</c:if>
	dialog.open('findTaskLogDialog','<u:msg titleId="wl.cols.task.consol" alt="취합된 업무일지" />', url);
};<% // [하단버튼:수정] 수정 %>
function setLog() {
	location.href = './${setPage}.do?${paramsForList}&logNo=${param.logNo}';
}<% // [하단버튼:삭제] 삭제 %>
function delLog() {
	callAjax('./${transDelPage}Ajx.do?menuId=${menuId}', {logNo:'${param.logNo}'}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.href = './${listPage}.do?${paramsForList}';
		}
	});
}<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	location.href = './${listPage}.do?${paramsForList}';
}
$(document).ready(function() {
	setUniformCSS();
});

//-->
</script>
<u:msg var="menuTitle" titleId="wl.jsp.${path }.title" />
<u:title title="${menuTitle }" menuNameFirst="true" alt="${menuTitle }" />

<% // 폼 필드 %>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="12%"/><col width="38%"/><col width="12%"/><col width="38%"/></colgroup>
	<tr>
	<td class="head_lt"><u:msg titleId="wl.cols.typCd.select" alt="종류선택" /></td>
	<td class="body_lt"><u:msg var="typNm" titleId="wl.cols.typCd.${wlTaskLogBVo.typCd }"/>${typNm }</td>
	<td class="head_lt"><u:msg titleId="wl.cols.reprtDt" alt="보고일자" /></td>
	<td class="body_lt"><u:out value="${wlTaskLogBVo.reprtDt }" type="date"/></td>
	</tr>
	<c:if test="${path eq 'log' || path eq 'temp'}">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wl.cols.reprtTgt" alt="보고대상" /></td>
	<td class="body_lt" colspan="3"><div id="userSelectArea" style="min-height:40px;"><c:forEach 
		var="wlTaskLogUserLVo" items="${wlTaskLogUserLVoList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title_view"><a href="javascript:viewUserPop('${wlTaskLogUserLVo.userUid }');">${wlTaskLogUserLVo.userNm }</a><input type="hidden" name="userUid" value="${wlTaskLogUserLVo.userUid }"
		/></dd></dl></div>
		</c:forEach></div></td>
	</tr>
	</c:if>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt" colspan="3"><u:out value="${wlTaskLogBVo.subj }"/></td>
	</tr>
	<c:if test="${!empty columnList && !empty columnMap }">
		<c:set var="map" value="${columnMap }" scope="request"/>		
		<c:forEach var="columnVo" items="${columnList }" varStatus="status">
			<u:convertMap var="value" srcId="map" attId="${columnVo[0] }Cont" type="html" />
			<tr>
			<td class="head_lt"><c:if test="${empty columnVo[1] }"><u:msg titleId="wl.cols.${columnVo[0] }" /></c:if><c:if test="${!empty columnVo[1] }">${columnVo[1] }</c:if></td>
			<td class="body_lt" colspan="3"><div style="overflow:auto;min-height:100px;" class="editor printNoScroll" id="${columnVo[0] }Cont">${value }</div></td>
			</tr>
		</c:forEach>
	</c:if>
	<c:if test="${!empty fileYn && fileYn eq 'Y'}">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
	<td colspan="3">
		<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="wl" mode="view" /></c:if>
	</td>
	</tr>
	</c:if>
</table>
</div>	

<% // 하단 버튼 %>
<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
	<c:if test="${path eq 'consol' || wlTaskLogBVo.consolYn eq 'Y'}">
	<u:button titleId="wl.cols.task.consol" alt="취합된 업무일지" onclick="findLogListPop();" auth="W" />
	</c:if>
	<c:if test="${path eq 'log' || path eq 'temp'}">
	<u:button titleId="cm.btn.mod" alt="수정" onclick="setLog();" auth="W" ownerUid="${wlTaskLogBVo.regrUid}" />	
	<u:button titleId="cm.btn.del" alt="삭제" onclick="delLog();" auth="W" ownerUid="${wlTaskLogBVo.regrUid}" />
	</c:if>
	<u:button titleId="cm.btn.list" alt="목록" onclick="goList();" />
</u:buttonArea>