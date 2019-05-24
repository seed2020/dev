<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ct.jsp.listPr.title" alt="홍보마당" menuNameFirst="true" />

<% // 검색영역 %>`
<u:searchArea>
	<form name="searchForm" action="./listPr.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<u:option value="SUBJ" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
			<u:option value="CONT" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
			<u:option value="REGR_NM" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" onkeydown="if (event.keyCode == 13) searchForm.submit();" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<u:listArea id="listArea">
	<tr>
	<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.readCnt" alt="조회수" /></td>
	</tr>
	
	<c:forEach var="ctPrVo" items="${ctPrList}" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="body_ct">${recodeCount - ctPrVo.rnum + 1 }</td>
			<td class="body_lt"><a href="./viewPr.do?menuId=${menuId}&bullId=${ctPrVo.bullId}"><u:out value="${ctPrVo.subj}"/></a></td>
			<td class="body_ct"><a href="javascript:viewUserPop('${ctPrVo.regrUid}');">${ctPrVo.regrNm}</a></td>
			<td class="body_ct">
				<fmt:parseDate var="dateTempParse" value="${ctPrVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			</td>
			<td class="body_ct"><u:out value="${ctPrVo.readCnt}" type="number"/></td>
		</tr>
	</c:forEach>
	<c:if test="${fn:length(ctPrList) == 0}">
		<tr>
			<td class="nodata" colspan="9"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
<!-- 
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct">2</td>
	<td class="body_lt"><a href="./viewPr.do?menuId=${menuId}">[JQuery 연구 모임] 놀러 오세요~</a></td>
	<td class="body_ct"><a href="javascript:viewUserPop('U0000003');">김윤아</a></td>
	<td class="body_ct">2014-01-17</td>
	<td class="body_ct">1</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct">1</td>
	<td class="body_lt"><a href="./viewPr.do?menuId=${menuId}">[JSON을 사랑하는 사람들] 많은 가입 바랍니다.</a></td>
	<td class="body_ct"><a href="javascript:viewUserPop('U0000003');">김건모</a></td>
	<td class="body_ct">2013-11-10</td>
	<td class="body_ct">1</td>
	</tr>
 -->
</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록" auth="W" href="./setPr.do?menuId=${menuId}"/>
</u:buttonArea>
