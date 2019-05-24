<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<% // [팝업] More %>
function subListDetlPop(containerId){
	//레이어
	var container = $('#listArea #'+containerId+'_container');
	//레이어 display 가 block 일 경우 숨김
	if(container.css('display') == 'block') { container.hide(); return;}	
	$('#listArea .moreContainer').hide();
	container.show();
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<form name="listDocTaskForm" action="./listDocTaskFrm.do" >
<c:if test="${!empty paramEntryList}">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
</c:if>	
<div id="listArea" class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="*"/><col width="15%"/><col width="23%"/><col width="35%"/></colgroup>
	<tr id="headerTr">
		<td class="head_ct"><u:msg titleId="cols.userNm" alt="사용자명"/></td>
		<td class="head_ct"><u:msg titleId="cols.typ" alt="구분"/></td>
		<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시"/></td>
		<td class="head_ct"><u:msg titleId="cols.note" alt="비고" /></td>
	</tr>
	<c:forEach var="list" items="${dmTaskHVoList}" varStatus="status">
		<tr>
			<td class="body_lt" ><a href="javascript:parent.viewUserPop('${list.userUid}');"><u:out value="${list.userNm }" /></a></td>
			<td class="body_ct" ><u:msg titleId="dm.cols.task.${list.taskCd }"/></td>
			<td class="body_ct" ><u:out value="${list.taskDt }" type="longdate"/></td>
			<td class="body_lt" ><c:set var="subListCnt" value="0" /><%-- <u:convertMap var="userInfoMap" srcId="${list.docGrpId}_${list.taskDt }" attId="userInfo" type="html" 
			/><c:forEach var="subList" items="${userInfoMap }" varStatus="userStatus"
			><c:if test="${userStatus.count>1 }">/</c:if>${subList.value }</c:forEach
			> --%><u:convertMap var="detlInfoMap" srcId="${list.docGrpId}_${list.taskDt }" attId="taskDetl" type="html" 
			/><c:forEach var="subList" items="${detlInfoMap }" varStatus="detlStatus"
			><div class="ellipsis" title="${subList.value }"><u:msg var="msgTitle" titleId="${subList.msgId }"
			/>${msgTitle }<c:set var="subListCnt" value="${fn:length(fn:split(subList.value,',')) }" /><c:if test="${subListCnt > 1 }"><a href="javascript:;" onclick="subListDetlPop('detl_${status.count}');">(${subListCnt })</a></c:if
			>: <strong>${subList.value }</strong></div><c:if test="${subListCnt > 1 }"
			><div id="detl_${status.count}_container" class="moreContainer" style="position:absolute;border:2px solid #6e6e6e;z-index:999;background:#ffffff;width:200px;padding:2px;display:none;">
				<div id="titleContainer" style="background:#f2f2f2;text-align:center;height:22px;vertical-align:middle;padding:3px 2px 0px 3px;"
				><strong style="float:left;font-size:13px;color:#1c1c1c;">${msgTitle }</strong><span style="float:right;"
				><a href="javascript:;" onclick="subListDetlPop('detl_${status.count}');"><img src="${_cxPth}/images/${_skin}/btn_close11.gif" alt="닫기" width="15" height="14"/></a
				></span></div><div id="contentsContainer" style="background:#ffffff;height:120px;overflow-y:auto;width:100%;"
				><c:forTokens var="subContents" items="${subList.value }" delims="," varStatus="subContentsStatus"
				><div class="ellipsis" title="${subContents }">${subContents }</div></c:forTokens></div
				></div
			></c:if></c:forEach
			></td>
		</tr>
	</c:forEach>
	<c:if test="${empty dmTaskHVoList }">
		<tr>
		<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	
</table>
</div>
</form>
<u:blank />
<u:pagination noLeftSelect="true"/>
