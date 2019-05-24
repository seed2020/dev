<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // [팝업] 파일목록 조회 %>
function viewFileListPop(id) {
	dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />','./viewFileListPop.do?menuId=${menuId}&bullId='+id);
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ct.jsp.listNotc.title" alt="커뮤니티 공지사항" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listNotc.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="brdId" value="${param.brdId}" />

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
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.valdPrd" alt="유효기간" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<u:checkbox name="schExpr" value="Y" titleId="cols.expr" checkValue="${param.schExpr}" textClass="search_body" />
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
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
	<tr>
	<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="ct.cols.exprDt" alt="만료일시" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.readCnt" alt="조회수" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.att" alt="첨부" /></td>
	</tr>
	
	<c:forEach var="ctAdmNotcVo" items="${ctAdmNotcMapList}" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="body_ct">${recodeCount- ctAdmNotcVo.rnum +1}</td>
			<td class="body_lt">
				<div class="ellipsis" title="<u:out value="${ctAdmNotcVo.subj}"/>">
					<a href="./viewNotc.do?menuId=${menuId}&bullId=${ctAdmNotcVo.bullId}"><u:icon type="new" display="${ctAdmNotcVo.newYn == 'Y'}"/>
					<c:if test="${ctAdmNotcVo.exprYn == 'Y'}">
						(<u:msg titleId="cols.expr" alt="만료"/>)
					</c:if>
					<u:out value="${ctAdmNotcVo.subj}"/></a>
				</div>
			</td>
			<td class="body_ct"><a href="javascript:viewUserPop('${ctAdmNotcVo.regrUid}');">${ctAdmNotcVo.regrNm}</a></td>
			<td class="body_ct">			
				<fmt:parseDate var="dateTempParse" value="${ctAdmNotcVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</td>
			<td class="body_ct">			
				<fmt:parseDate var="dateTempParse" value="${ctAdmNotcVo.bullExprDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</td>
			<td class="body_ct"><u:out value="${ctAdmNotcVo.readCnt}" type="number"/></td>
			<td class="body_ct">
				<c:if test="${ctAdmNotcVo.attYn == 'Y'}">
					<a href="javascript:viewFileListPop('${ctAdmNotcVo.bullId }');"><u:icon type="att" /></a>
				</c:if>
				<c:if test="${ctAdmNotcVo.attYn == 'N'}">
				</c:if>
			</td>
		</tr>
	
	</c:forEach>
	<c:if test="${fn:length(ctAdmNotcMapList) == 0}">
			<tr>
				<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
	</c:if>

</table>
</div>

<u:blank />
<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록" auth="W" href="./setNotc.do?menuId=${menuId}" />
</u:buttonArea>
