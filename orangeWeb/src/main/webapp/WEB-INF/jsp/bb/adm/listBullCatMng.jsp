<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // [하단버튼:등록] 카테고리그룹 등록 %>
function regBullCatMng() {
	dialog.open('setBullCatMngPop','<u:msg titleId="bb.jsp.setBullCatMngPop.reg.title" alt="게시물 카테고리 등록" />','./setBullCatMngPop.do?menuId=${menuId}');
}
<% // [목록:카테고리그룹] 카테고리그룹 수정 %>
function modBullCatMng(id) {
	dialog.open('setBullCatMngPop','<u:msg titleId="bb.jsp.setBullCatMngPop.mod.title" alt="게시물 카테고리 수정" />','./setBullCatMngPop.do?menuId=${menuId}&catGrpId='+id);
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="bb.jsp.listBullCatMng.title" alt="게시물 카테고리 관리" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listBullCatMng.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.catGrp" alt="카테고리그룹" /></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 300px;" />
			<u:input type="hidden" id="schCat" value="CAT_GRP_NM" /></td>

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
	<td class="head_ct"><u:msg titleId="cols.catGrp" alt="카테고리그룹" /></td>
	<td class="head_ct"><u:msg titleId="cols.cat" alt="카테고리" /></td>
	<td width="14%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>

<c:if test="${fn:length(baCatGrpBVoList) == 0}">
	<tr>
	<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baCatGrpBVoList) > 0}">
	<c:forEach items="${baCatGrpBVoList}" var="baCatGrpBVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt"><a href="javascript:modBullCatMng('${baCatGrpBVo.catGrpId}');">${baCatGrpBVo.rescNm}</a></td>
	<td class="body_lt">
	<c:if test="${fn:length(baCatGrpBVo.catVoList) > 0}">
		<c:forEach items="${baCatGrpBVo.catVoList}" var="baCatDVo" varStatus="status">
		<u:set test="${status.last}" var="comma" value="" elseValue="," />
		${baCatDVo.rescNm}${comma}
		</c:forEach>
	</c:if></td>
	<td class="body_ct"><u:out value="${baCatGrpBVo.regDt}" type="date" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:;" onclick="regBullCatMng();" auth="A" />
</u:buttonArea>
