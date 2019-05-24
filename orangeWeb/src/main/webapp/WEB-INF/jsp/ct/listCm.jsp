<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function viewCm(ctId, fncUid) {
	//location.href = "./viewCm.do?menuId=MC000000";
	location.href = "./viewCm.do?menuId="+ fncUid +"&ctId=" + ctId + "&prevMenuId=${menuId}";
}

function join(ctId) {
	location.href = "./setCmJoin.do?menuId=${menuId}&ctId="+ctId+"&catId=${catId}&signal=normal";
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:msg titleId="ct.jsp.listCm.title" var="title" alt="커뮤니티 목록" />
<c:set var="clsNm" value="프로그래밍" />

<u:title title="${title} (${clsNm})" alt="커뮤니티 목록 (분류명)" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listCm.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="signal" value="" />
	<u:input type="hidden" id="catId" value="${catId}"/>

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td><table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td><select name="schCat">
						<u:option value="communityOpt" titleId="ct.cols.ctNm" alt="커뮤니티" checkValue="${param.schCat}" />
						<u:option value="masterOpt" titleId="ct.cols.mastNm" alt="마스터" checkValue="${param.schCat}" />
					</select></td>
				<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>
				</tr>
				</table></td>
			<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
		</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<u:listArea id="listArea">
	<tr>
	<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mast" alt="마스터" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mbshCnt" alt="회원수" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.createDt" alt="생성일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="ct.cols.join" alt="가입" /></td>
	</tr>
	
	<c:forEach var="catCmntVo" items="${catCmntList}" varStatus="status">
		<tr>
			<td class="body_ct"><u:out value="${recodeCount - catCmntVo.rnum + 1}" type="number" /></td>
			<td class="body_lt"><a href="javascript:viewCm('${catCmntVo.ctId}','${catCmntVo.ctFncUid}');"><u:out value="${catCmntVo.ctNm}" /></a>
				<input id="ctCatId" name="ctCatId" type="hidden" value="${catCmntVo.catId}">
				<c:choose>
					<c:when test="${catCmntVo.ctEvalCd == 'B'}">
						<u:icoBest icon="best" />
					</c:when>
					<c:when test="${catCmntVo.ctEvalCd == 'E'}">
						<u:icoBest icon="excellent" />
					</c:when>
					<c:when test="${catCmntVo.ctEvalCd == 'G'}">
						<u:icoBest icon="good" />
					</c:when>
					<c:otherwise>
						<u:icoBest icon="" />
					</c:otherwise>
				</c:choose>
				<c:if test="${catCmntVo.recmdYn == 'Y'}">
					<u:icon type="recommend" display="true" />
				</c:if>
			</td>
			<td class="body_ct"><a href="javascript:viewUserPop('${catCmntVo.mastUid}');">${catCmntVo.mastNm}</a></td>
			<td class="body_ct">${catCmntVo.mbshCnt}</td>
			<td class="body_ct">
				<fmt:parseDate var="dateTempParse" value="${catCmntVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			</td>
			<c:choose>
				<c:when test="${catCmntVo.logUserJoinStat == null}">
					<td class="listicon_ct"><u:buttonS titleId="ct.btn.join" alt="가입" onclick="join('${catCmntVo.ctId}');" /></td>
				</c:when>
				<c:when test="${catCmntVo.logUserJoinStat == '1'}">
					<td class="body_ct"><u:msg titleId="ct.option.logUserJoinStat01" alt="승인대기중"/></td>
				</c:when>
				<c:when test="${catCmntVo.logUserJoinStat == '2'}">
					<td class="body_ct"><u:msg titleId="ct.option.logUserJoinStat02" alt="가입불가"/></td>
				</c:when>
				<c:when test="${catCmntVo.logUserJoinStat == '3'}">
					<td class="body_ct"><u:msg titleId="ct.option.logUserJoinStat03" alt="가입완료"/></td>
				</c:when>
			
			</c:choose>
		</tr>
	
	</c:forEach>
	
	
	<c:if test="${fn:length(catCmntList) == 0}">
			<tr>
				<td class="nodata" colspan="9"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
	</c:if>

</u:listArea>

<u:pagination />

