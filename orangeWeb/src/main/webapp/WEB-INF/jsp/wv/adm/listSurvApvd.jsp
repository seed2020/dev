<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function viewSurv(survId){
	var $form = $('#listSurvApvdForm');
	
	$form.attr('method','post');
	$form.attr('action','./viewSurvApvd.do?menuId=${menuId}&survId=' + survId);
	
	$form[0].submit();
	
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="wv.jsp.listSurvApvd.title" alt="신청중인 설문목록" menuNameFirst="true"/>

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listSurvApvd.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<u:option value="survSubj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
			<u:option value="survItnt" titleId="cols.itnt" alt="취지" checkValue="${param.schCat}" />
			</select>
		</td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 300px" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
		<form id="listSurvApvdForm" name="listSurvApvdForm" >
			<tr>
			<td width="50" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
			<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
			<td width="100" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
			<td width="100" class="head_ct"><u:msg titleId="cols.strtDt" alt="시작일시" /></td>
			<td width="100" class="head_ct"><u:msg titleId="cols.finDt" alt="마감일시" /></td>
			</tr>
			<c:forEach var="wvSurvBVo" items ="${wvSurvBMapList}" varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_ct"><u:out value="${recodeCount - wvSurvBVo.rnum + 1}" type="number" /></td>
					<td class="body_lt">
						<div class="ellipsis" title="${wvSurvBVo.survSubj}">
							<a href="javascript:viewSurv('${wvSurvBVo.survId}')"><u:out value="${wvSurvBVo.survSubj}" /></a>
						</div>
					</td>
					<td class="body_ct"><a href="javascript:viewUserPop('${wvSurvBVo.regrUid}');">${wvSurvBVo.regrNm}</a></td>
					<fmt:parseDate var="dateStartTempParse" value="${wvSurvBVo.survStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:formatDate var="startYmd" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/> 
					<fmt:parseDate var="dateEndTempParse" value="${wvSurvBVo.survEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:formatDate var="endYmd" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/> 
					<td class="body_ct">${startYmd}</td>
					<td class="body_ct">${endYmd}</td>
				</tr>
			</c:forEach>
			
			<c:if test="${fn:length(wvSurvBMapList) == 0}">
				<tr>
					<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>
			</c:if>
	
		</form>
	</table>
</div>
<u:blank />

<u:pagination />

