<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function send(url){
	parent.view(url);
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<form id="listBoardPtForm">
<input type="hidden" id="fncUid" name="fncUid" value="${fncUid}" />
<input type="hidden" id="ctId" name="ctId" value="${ctId}" />
<input type="hidden" id="menuId" name="menuId" value="${menuId}" />


<u:listArea>
	<tr>
		<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td width="20%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
		<td width="20%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>
	
	<c:forEach  var="ctRecList" items="${ctRecMastList}" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="body_lt">
				<u:icon type="new" display="${ctRecList.newYn == 'Y'}"/>
				<a href="javascript:send('./viewPds.do?menuId=${menuId}&ctId=${ctRecList.ctId}&bullId=${ctRecList.bullId}');">${ctRecList.subj}</a>
			</td>
			<td class="body_ct"><a href="javascript:viewUserPop('${ctRecList.regrUid}');">${ctRecList.regrNm}</a></td>
			<td class="body_ct">
				<fmt:parseDate var="dateTempParse" value="${ctRecList.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			</td>
		</tr>
	</c:forEach>
	<c:if test="${fn:length(ctRecMastList) == 0}">
		<tr>
		<td class="nodata" colspan="3"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
</u:listArea>

</form>
