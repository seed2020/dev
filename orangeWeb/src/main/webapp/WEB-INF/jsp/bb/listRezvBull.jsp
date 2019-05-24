<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" />
<script type="text/javascript">
<!--
<% // [목록:제목] 게시물 조회 %>
function viewBull(bullId) {
	location.href = './${viewPage}.do?${params}&bullId=' + bullId;
}
<% // [목록:제목] 보안글 조회를 위한 로그인폼 화면 %>
function openLogin(id) {
	dialog.open('setLoginPop','<u:msg titleId="bb.jsp.setLoginPop.title" alt="보안글 인증" />','./setLoginPop.do?${params}&viewPage=${viewPage}&bullId=' + id);
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="bb.jsp.listRezvBull.title" alt="예약저장함" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listRezvBull.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
		<u:option value="SUBJ" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
		<u:option value="CONT" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
		<u:option value="BRD_NM" titleId="cols.bbNm" alt="게시판명" checkValue="${param.schCat}" />
		</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm.submit();" /></td>
		<td class="width20"></td>
		<!-- 등록일시 -->
		<td class="search_tit"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="strtYmd" option="{end:'endYmd'}" value="${param.strtYmd}" /></td>
			<td class="search_body_ct"> ~ </td>
			<td><u:calendar id="endYmd" option="{start:'strtYmd'}" value="${param.endYmd}" /></td>
			</tr>
			</table></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<div id="listArea" class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
	<tr>
	<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.regBb" alt="등록게시판" /></td>
	<td width="14%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="14%" class="head_ct"><u:msg titleId="cols.bullRezvDt" alt="게시예약일시" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.att" alt="첨부" /></td>
	</tr>

<c:if test="${fn:length(baRezvSaveLVoList) == 0}">
	<tr>
	<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baRezvSaveLVoList) > 0}">
	<c:forEach items="${baRezvSaveLVoList}" var="baRezvSaveLVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct">
		<u:out value="${recodeCount - baRezvSaveLVo.rnum + 1}" type="number" />
		</td>
	<td class="body_lt">
		<div class="ellipsis" title="${baRezvSaveLVo.subj}">
		<u:set test="${baRezvSaveLVo.ugntYn == 'Y'}" var="style" value="color:red;" elseValue="" />
		<u:set test="${baRezvSaveLVo.secuYn == 'Y'}" var="viewBull" value="openLogin" elseValue="viewBull" />
		<a href="javascript:${viewBull}('${baRezvSaveLVo.bullId}');" title="${baRezvSaveLVo.subj}" style="${style}">
		<c:if test="${baRezvSaveLVo.ugntYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if>
		<c:if test="${baRezvSaveLVo.secuYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
		<u:out value="${baRezvSaveLVo.subj}" /></a>
		</div>
		</td>
	<td class="body_ct">${baRezvSaveLVo.brdNm}</td>
	<td class="body_ct"><u:out value="${baRezvSaveLVo.regDt}" type="longdate" /></td>
	<td class="body_ct"><u:out value="${baRezvSaveLVo.bullRezvDt}" type="longdate" /></td>
	<td class="body_ct"><c:if test="${baRezvSaveLVo.fileCnt > 0}"><u:icon type="att" /></c:if></td>
	</tr>
	</c:forEach>
</c:if>
</table>
</div>
<u:blank />

<u:pagination />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
</u:buttonArea>
