<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" />
<jsp:useBean id="now" class="java.util.Date" />
<script type="text/javascript">
<!--  
function viewSurvApvd(survId){
	var $form = $('#listSurvForm');
	
	$form.attr('method','post');
	$form.attr('action','./viewMySurvApvd.do?menuId=${menuId}&survId=' + survId);
	
	$form[0].submit();
	
}

function setSurvPop(){
	
	var $form = $('#listSurvForm');
	
	$form.attr('method','post');
	$form.attr('action','./setSurv.do?menuId=${menuId}&fnc=reg');
	
	$form[0].submit();
}


$(document).ready(function() {
	setUniformCSS();
});
-->
</script>

<u:title titleId="wv.jsp.listSurv.title" alt="설문목록" menuNameFirst="true"/>

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listMySurvApvd.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="logUserUid" value="${logUserUid}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
				<u:option value="survSubj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
				<u:option value="survItnt" titleId="cols.itnt" alt="취지" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 300px" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>
		
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.stat" alt="상태" /></td>
		<td>
			<select id="schCtStat" name="schCtStat" style="width:220px;">
				<u:option value="" titleId="cm.option.all" alt="전체선택"/>
				<%-- <u:option value="1" titleId="wv.cols.ingStandBy" alt="진행대기중" checkValue="${param.schCtStat}"/> --%>
				<u:option value="2" titleId="wv.cols.apvdStandBy" alt="승인대기중" checkValue="${param.schCtStat}"/>
				<u:option value="6" titleId="wv.cols.tempSave" alt="임시저장" checkValue="${param.schCtStat}"/>
				<u:option value="9" titleId="wv.cols.rjt" alt="반려" checkValue="${param.schCtStat}"/>
			</select>
		</td>
					
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	<!--  
	<div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm.submit()"><span>검색</span></a></li></ul></div></td>
	-->
	</tr>
	</table>
	</form>
</u:searchArea>




<% // 목록 %>

<div class="listarea">
	<form id="listSurvForm" name="listSurvForm" >
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
		<tbody>
			<tr>
				<td class="head_ct" width="5%"><u:msg titleId="cols.no" alt="번호" /></td>
				<td class="head_ct" width="45%"><u:msg titleId="cols.subj" alt="제목" /></td>
				<td class="head_ct" width="15%"><u:msg titleId="cols.strtDt" alt="시작일시" /></td>
				<td class="head_ct" width="15%"><u:msg titleId="cols.finDt" alt="마감일시" /></td>
				<td class="head_ct" width="10%"><u:msg titleId="cols.stat" alt="상태" /></td>
			</tr>
			<c:forEach var="wvSurvBVo" items ="${wvSurvList}" varStatus="status">
				<tr onmouseover='this.className="trover"' id="${wvSurvBVo.survId}" onmouseout='this.className="trout"'>
					<td class="body_ct"><u:out value="${recodeCount - wvSurvBVo.rnum + 1}" type="number" /></td>
					<td class="body_lt">
						<div class="ellipsis" title="${wvSurvBVo.survSubj}">
							<a href="javascript:viewSurvApvd('${wvSurvBVo.survId}')"><u:out value="${wvSurvBVo.survSubj}"/></a>
						</div>
					</td>
					<td class="body_ct">
						<fmt:parseDate var="dateTempParse" value="${wvSurvBVo.survStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
					</td>
					<td class="body_ct">
						<fmt:parseDate var="endDtTempParse" value="${wvSurvBVo.survEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:formatDate var="survEndDt" value="${endDtTempParse}" pattern="yyyy-MM-dd"/>
						<u:out value="${survEndDt}"/>
					</td>
					<td class="body_ct">
						<c:choose>
							<c:when test="${wvSurvBVo.survPrgStatCd == '1'}">
								<u:msg titleId="wv.cols.ingStandBy" alt="진행대기중"/>
							</c:when>
							<c:when test="${wvSurvBVo.survPrgStatCd == '2'}">
								<u:msg titleId="wv.cols.apvdStandBy" alt="승인대기중" />
							</c:when>
							<c:when test="${wvSurvBVo.survPrgStatCd == '6'}">
								<u:msg titleId="wv.cols.tempSave" alt="임시저장"/>
							</c:when>
							<c:when test="${wvSurvBVo.survPrgStatCd == '9'}">
								<u:msg titleId="wv.cols.rjt" alt="반려"/>
							</c:when>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${fn:length(wvSurvList) == 0}">
				<tr>
					<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>
			</c:if>
		</tbody>
	</table>
	</form>
</div>
<u:blank />
<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:setSurvPop();" auth="W" />
</u:buttonArea>