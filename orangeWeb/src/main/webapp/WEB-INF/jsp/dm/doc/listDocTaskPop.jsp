<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><c:set var="exKeys" value="owner,docHst,version,disuse,saveDisc,keepDdln,seculCd,fld,cls,docNoMod,setSubDoc,bumk,verDel"/><!-- 제외 key -->
<c:set var="pageRowCnt" value="10"/>
<script type="text/javascript">
<!--<%// 조회 %>
function listDocTaskSearch(){
	var url = './listDocTaskFrm.do?${params }&pageRowCnt=${pageRowCnt}';
	url+= '&'+$('#listDocTaskForm').serialize();
	reloadFrame('listDocTaskFrm', url);
}
$(document).ready(function() {
});
//-->
</script>
<div style="width:650px;">
<c:if test="${empty param.single || param.single eq 'N'}">
<u:searchArea >
	<form id="listDocTaskForm">
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td class="bodyip_lt" width="15%"><strong><u:msg titleId="cols.regDt" alt="등록일시" /></strong></td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><u:input type="hidden" id="durCat" name="durCat" value="taskDt"/>
						<table border="0" cellpadding="0" cellspacing="0">
						<tr>
						<td><u:calendar id="durStrtDt" name="durStrtDt" option="{end:'durEndDt'}" titleId="cols.strtYmd" /></td>
						<td class="search_body_ct"> ~ </td>
						<td><u:calendar id="durEndDt" name="durEndDt" option="{start:'durStrtDt'}" titleId="cols.endYmd" /></td>
						</tr>
						</table></td>
					</tr>
				</table>
			</td>
			<td rowspan="2" width="15%"><div class="button_search"><ul><li class="search"><a href="javascript:listDocTaskSearch();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
		</tr>
		<tr>
			<td class="bodyip_lt"><strong><u:msg titleId="dm.cols.taskGubun" alt="작업구분" /></strong></td>
			<td>
				<u:checkArea2>
					<c:forEach var="colmVo" items="${taskList }" varStatus="status">
						<c:if test="${!fn:contains(exKeys,colmVo.va) }">
							<c:set var="checked" value="N"/>
							<c:forTokens var="srchCd" items="${param.srchCd }" delims=","><c:if test="${colmVo.va eq srchCd}"><c:set var="checked" value="Y"/></c:if></c:forTokens>
							<u:checkbox2 id="taskCd${status.index }" name="srchCd" value="${colmVo.va}" title="${colmVo.msg }" alt="${colmVo.msg }" checked="${checked eq 'Y' }"/>
						</c:if>
					</c:forEach>
				</u:checkArea2>
			</td>
		</tr>
	</table>
	</form>
</u:searchArea>
</c:if>
<iframe id="listDocTaskFrm" name="listDocTaskFrm" src="./listDocTaskFrm.do?${params }&pageRowCnt=${pageRowCnt}" style="width:100%; height:350px;" frameborder="0" marginheight="0" marginwidth="0" ></iframe>
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>