<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="paramsForList" excludes="menuId, cardNo"/>
<script type="text/javascript">
<!--
<% // [하단버튼:삭제] %>
function delTaskStat() {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./transTaskStatDelAjx.do?menuId=${menuId}', {statNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
};
<% // [목록클릭] - 상세보기 화면으로 이동 %>
function viewTaskStat(statNo){
	location.href='./viewTaskStat.do?${paramsForList}&statNo='+statNo;
}

function notEvtBubble(event){
	if(event.stopPropagation) event.stopPropagation(); //MOZILLA
	else event.cancelBubble = true; //IE
}

$(document).ready(function() {
	setUniformCSS();
	$('#listArea tbody:first tr a').click(function(event){
		notEvtBubble(event);
	});
});
//-->
</script>

<u:title menuNameFirst="true"/>

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listTaskStat.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<u:option value="subj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
			<u:option value="cont" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" /></td>
		<td class="width20"></td>
		<td class="search_tit">날짜<u:input type="hidden" id="durCat" value="reprtDt"/></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><u:calendar id="durStrtDt" option="{end:'durEndDt'}" value="${param.durStrtDt}" /></td>
					<td class="search_body_ct"> ~ </td>
					<td><u:calendar id="durEndDt" option="{start:'durStrtDt'}" value="${param.durEndDt}" /></td>
				</tr>
			</table>
		</td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>
<u:secu auth="A"><c:set var="auth" value="A"/></u:secu>
<u:set var="colspan" test="${!empty auth && auth eq 'A' }" value="3%,15%,*,13%,13%" elseValue="15%,*,13%,13%"/>
<u:listArea id="listArea" colgroup="${colspan }">	
	<tr id="headerTr">
		<c:if test="${!empty auth && auth eq 'A' }"><td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td></c:if>
		<th class="head_ct">날짜</th>
		<th class="head_ct">제목</th>
		<th class="head_ct">등록자</th>
		<th class="head_ct">수정자</th>
	</tr>
	<c:if test="${fn:length(cuTaskStatBVoList) == 0}">
		<tr>
		<td class="nodata" colspan="${empty auth ? 4 : 5 }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(cuTaskStatBVoList)>0}">
		<c:forEach items="${cuTaskStatBVoList}" var="cuTaskStatBVo" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' onclick="viewTaskStat('${cuTaskStatBVo.statNo}')">
				<c:if test="${!empty auth && auth eq 'A' }"><td class="bodybg_ct"><input type="checkbox" value="${cuTaskStatBVo.statNo }" onclick="notEvtBubble(event);"/></td></c:if>
				<td class="body_ct"><u:out value="${cuTaskStatBVo.reprtDt }" type="date"/></td>
				<td class="body_lt"><u:out value="${cuTaskStatBVo.subj }"/></td>
				<td class="body_ct"><a href="javascript:viewUserPop('${cuTaskStatBVo.regrUid }');">${cuTaskStatBVo.regrNm }</a></td>
				<td class="body_ct"><a href="javascript:viewUserPop('${cuTaskStatBVo.modrUid }');">${cuTaskStatBVo.modrNm }</a></td>
			</tr>
		</c:forEach>
	</c:if>
	
</u:listArea>
<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.write" alt="등록" href="./setTaskStat.do?menuId=${menuId}" auth="W" />
<c:if test="${!empty auth && auth eq 'A' }"><u:button titleId="cm.btn.del" alt="삭제" href="javascript:delTaskStat();" /></c:if>
</u:buttonArea>
