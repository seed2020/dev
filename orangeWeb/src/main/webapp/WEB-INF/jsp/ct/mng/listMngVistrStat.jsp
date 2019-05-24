<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
<script type="text/javascript">
<!--

$(document).ready(function() {
	setUniformCSS();
	//달력셋팅
	$("#strtDt").val("${strtDt}");
	$("#finDt").val("${endDt}");
});
//-->
</script>

<u:title title="${menuTitle }"  alt="커뮤니티 방문자현황" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listMngVistrStat.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="ctId" name="ctId" value="${ctId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.prd" alt="기간" /></td>
		<td colspan="2"><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td><u:calendar id="strtDt" name="strtDT" option="{end:'finDt'}" mandatory="Y" /></td>
			<td class="body_lt">~</td>
			<td><u:calendar id="finDt" name="endDT" option="{start:'strtDt'}" mandatory="Y"/></td>
			</tr>
			</tbody></table></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm.submit()"><span>검색</span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<u:listArea id="listArea">
	<tr>
	<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.joinDt" alt="가입일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.dftAuth" alt="기본권한" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.vistCnt" alt="방문횟수" /></td>
	</tr>
	<c:forEach var="ctMngVistList" items="${ctMngVistHstList}" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="body_ct">${recodeCount - ctMngVistList.rnum + 1}</td>
		<td class="body_lt"><a href="javascript:viewUserPop('${ctMngVistList.userUid}');">${ctMngVistList.userNm}</a></td>
		<td class="body_ct">${ctMngVistList.deptNm}</td>
		<td class="body_ct">
			<fmt:parseDate var="dateTempParse" value="${ctMngVistList.joinDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
		</td>
		<td class="body_ct">
			<c:choose>
				<c:when test="${ctMngVistList.userSeculCd == 'M'}">
					<u:msg titleId="ct.cols.mbshLev0" alt="마스터" />
				</c:when>
				<c:when test="${ctMngVistList.userSeculCd == 'S'}">
					<u:msg titleId="ct.cols.mbshLev1" alt="스텝" />
				</c:when>
				<c:when test="${ctMngVistList.userSeculCd == 'A'}">
					<u:msg titleId="ct.cols.mbshLev3" alt="준회원" />
				</c:when>
				<c:when test="${ctMngVistList.userSeculCd == 'R'}">
					<u:msg titleId="ct.cols.mbshLev2" alt="정회원" />
				</c:when>
			</c:choose>
		</td>
		<td class="body_ct">
			${ctMngVistList.ctTodayCount} / ${ctMngVistList.ctAllCount}
		</td>
		</tr>
	</c:forEach>
	
</u:listArea>

<u:pagination />

