<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" />
<script type="text/javascript">
<!--
<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	var $form = $('#excelForm');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};
<% // [목록:제목] 게시물 조회 %>
function setBull(bullId) {
	location.href = './setDiscBull.do?${params}&bullId=' + bullId;
}
<% // [목록:제목] 보안글 조회를 위한 로그인폼 화면 %>
function openLogin(id) {
	dialog.open('setLoginPop','<u:msg titleId="bb.jsp.setLoginPop.title" alt="보안글 인증" />','./setLoginPop.do?${params}&viewPage=setDiscBull&bullId=' + id);
}
<% // [팝업] 파일목록 조회 %>
function viewFileListPop(id) {
	dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />','./viewFileListPop.do?menuId=${menuId}&bullId='+id);
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="bb.jsp.listDiscBull.title" alt="심의함" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listDiscBull.do" >
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
	<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="14%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.att" alt="첨부" /></td>
	</tr>

<c:if test="${fn:length(baBullSubmLVoList) == 0}">
	<tr>
	<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baBullSubmLVoList) > 0}">
	<c:forEach items="${baBullSubmLVoList}" var="baBullSubmLVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct">
		<u:out value="${recodeCount - baBullSubmLVo.rnum + 1}" type="number" />
		</td>
	<td class="body_lt">
		<div class="ellipsis" title="${baBullSubmLVo.subj}" >
		<u:set test="${baBullSubmLVo.ugntYn == 'Y'}" var="style" value="color:red;" elseValue="" />
		<u:set test="${baBullSubmLVo.secuYn == 'Y'}" var="setBull" value="openLogin" elseValue="setBull" />
		<a href="javascript:${setBull}('${baBullSubmLVo.bullId}');" title="${baBullSubmLVo.subj}" style="${style}">
		<c:if test="${baBullSubmLVo.ugntYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if>
		<c:if test="${baBullSubmLVo.secuYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
		<u:out value="${baBullSubmLVo.subj}" /></a>
		</div>
		</td>
	<td class="body_ct">${baBullSubmLVo.brdNm}</td>
	<td class="body_ct"><a href="javascript:viewUserPop('${baBullSubmLVo.regrUid}');"><u:out value="${baBullSubmLVo.regrNm}" /></a></td>
	<td class="body_ct"><u:out value="${baBullSubmLVo.regDt}" type="longdate" /></td>
	<td class="body_ct"><c:if test="${baBullSubmLVo.fileCnt > 0}"><a href="javascript:viewFileListPop('${baBullSubmLVo.bullId }');"><u:icon type="att" /></a></c:if></td>
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
	<u:button titleId="cm.btn.excelDown" alt="엑셀다운" onclick="excelDownFile();" auth="R" />
</u:buttonArea>
<form id="excelForm">
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="brdId" value="disc" />
	<u:input type="hidden" id="schCat" value="${param.schCat}" />
	<u:input type="hidden" id="schWord" value="${param.schWord}" />
	<u:input type="hidden" id="strtYmd" value="${param.strtYmd}" />
	<u:input type="hidden" id="endYmd" value="${param.endYmd}" />
</form>