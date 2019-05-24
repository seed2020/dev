<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // [하단버튼:등록] 코드그룹 등록 %>
function regCd() {
	dialog.open('setCdPop','<u:msg titleId="pt.jsp.setCd.title" alt="코드관리" />','./setCdPop.do?menuId=${menuId}');
}
<% // [목록:코드그룹] 코드그룹 수정 %>
function modCd(id) {
	dialog.open('setCdPop','<u:msg titleId="pt.jsp.setCd.title" alt="코드관리" />','./setCdPop.do?menuId=${menuId}&cdGrpId='+id);
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.setCd.title" alt="코드관리" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listCd.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="bb.cols.codeGrp" alt="코드그룹" /></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 300px;" />
			<u:input type="hidden" id="schCat" value="CD_GRP_NM" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>

	</form>
</u:searchArea>

<% // 목록 %>
<u:listArea id="listArea" colgroup="15%,,10%,14%">
	<tr>
	<td class="head_ct"><u:msg titleId="bb.cols.codeGrp" alt="코드그룹" /></td>
	<td class="head_ct"><u:msg titleId="bb.cols.code" alt="코드" /></td>
	<td class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>

<c:if test="${fn:length(baCdGrpBVoList) == 0}">
	<tr>
	<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baCdGrpBVoList) > 0}">
	<c:forEach items="${baCdGrpBVoList}" var="baCdGrpBVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt"><a href="javascript:modCd('${baCdGrpBVo.cdGrpId}');">${baCdGrpBVo.rescNm}</a></td>
	<td class="body_lt">
	<c:if test="${fn:length(baCdGrpBVo.baCdDVoList) > 0}">
		<c:forEach items="${baCdGrpBVo.baCdDVoList}" var="baCatDVo" varStatus="status">
		<c:if test="${status.count > 1 }">,</c:if>		
		${baCatDVo.rescNm}
		</c:forEach>
	</c:if></td>
	<td class="body_ct"><u:set var="useMsgCd" test="${baCdGrpBVo.grpUseYn eq 'Y'}" value="cm.option.use" elseValue="cm.option.notUse"/><u:msg titleId="${useMsgCd }" alt="사용여부" /></td>
	<td class="body_ct"><u:out value="${baCdGrpBVo.regDt}" type="date" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:;" onclick="regCd();" auth="A" />
</u:buttonArea>
