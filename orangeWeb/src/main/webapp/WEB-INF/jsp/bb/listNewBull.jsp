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
function viewBull(brdId, id) {
	location.href = './${viewPage}.do?${params}&brdId=' + brdId + '&bullId=' + id;
}
<% // [목록:제목] 보안글 조회를 위한 로그인폼 화면 %>
function openLogin(brdId, id) {
	dialog.open('setLoginPop','<u:msg titleId="bb.jsp.setLoginPop.title" alt="보안글 인증" />','./setLoginPop.do?${params}&viewPage=${viewPage}&brdId=' + brdId + '&bullId=' + id);
}

<% // [목록:조회수] 조회이력 %>
function readHst(id) {
	dialog.open('listReadHstPop','<u:msg titleId="bb.jsp.listReadHstPop.title" alt="조회자 정보" />','./listReadHstPop.do?menuId=${menuId}&bullId=' + id);
}
<% // [팝업] 파일목록 조회 %>
function viewFileListPop(id, brdId) {
	dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />','./viewFileListPop.do?menuId=${menuId}&brdId='+brdId+'&bullId='+id);
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="bb.jsp.${listPage}.title" alt="최신게시물/나의게시물" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./${listPage}.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
		<u:option value="SUBJ" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
		<u:option value="CONT" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
		<c:if test="${baBrdBVo.brdTypCd == 'N'}">
		<u:option value="REGR_NM" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" />
		</c:if>
		<c:if test="${baBrdBVo.brdTypCd == 'A'}">
		<u:option value="ANON_REGR_NM" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" />
		</c:if>
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

<% // 나의게시물 설정 %>
<c:if test="${listPage == 'listMyBull'}">
<div class="titlearea">
	<div class="tit_right">
	<u:msg titleId="bb.jsp.setMyBullPop.title" alt="나의 게시물 설정" var="setMyBullPop"/>
	<u:buttonS href="javascript:dialog.open('setMyBullPop','${setMyBullPop}','./setMyBullPop.do?menuId=${menuId}');" titleId="cm.btn.setup" alt="설정" />
	</div>
</div>
</c:if>

<% // 목록 %>
<div id="listArea" class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
	<tr>
	<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="14%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.readCnt" alt="조회수" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.att" alt="첨부" /></td>
	</tr>

<!-- 게시물 목록 -->
<c:if test="${fn:length(bbBullLVoList) == 0}">
	<tr>
	<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(bbBullLVoList) > 0}">
	<c:forEach items="${bbBullLVoList}" var="bbBullLVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct"><u:out value="${recodeCount - bbBullLVo.rnum + 1}" type="number" /></td>
	<td class="body_lt">
		<div class="ellipsis" title="${bbBullLVo.subj}">
		<u:icon type="indent" display="${bbBullLVo.replyDpth > 0}" repeat="${bbBullLVo.replyDpth - 1}" />
		<u:icon type="reply" display="${bbBullLVo.replyDpth > 0}" />
		<u:icon type="new" display="${bbBullLVo.newYn == 'Y'}" />
		<u:set test="${bbBullLVo.readYn == 'Y'}" var="style" value="font-weight: normal;" elseValue="font-weight: bold;" />
		<u:set test="${bbBullLVo.ugntYn == 'Y'}" var="style" value="${style} color:red;" elseValue="${style}" />
		<u:set test="${bbBullLVo.secuYn == 'Y'}" var="viewBull" value="openLogin" elseValue="viewBull" />
		<a href="javascript:${viewBull}('${bbBullLVo.brdId}', '${bbBullLVo.bullId}');" title="${bbBullLVo.subj}" style="${style}">
		<c:if test="${bbBullLVo.ugntYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if>
		<c:if test="${bbBullLVo.secuYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
		<u:out value="${bbBullLVo.subj}" />
		<c:if test="${bbBullLVo.cmtCnt > 0}"><span style="font-size: 10px;">(<u:out value="${bbBullLVo.cmtCnt}" type="number" />)</span></c:if></a>
		</div>
		</td>
	<td class="body_ct">${bbBullLVo.brdNm}</td>
	<td class="body_ct">
		<c:if test="${bbBullLVo.brdTypCd == 'N'}">
		<a href="javascript:viewUserPop('${bbBullLVo.regrUid}');"><u:out value="${bbBullLVo.regrNm}" /></a>
		</c:if>
		<c:if test="${bbBullLVo.brdTypCd == 'A'}">
		<u:out value="${bbBullLVo.anonRegrNm}" />
		</c:if>
		</td>
	<td class="body_ct"><u:out value="${bbBullLVo.regDt}" type="longdate" /></td>
	<td class="body_ct">
		<c:if test="${bbBullLVo.readHstUseYn == 'Y'}">
			<a href="javascript:readHst('${bbBullLVo.bullId}');"><u:out value="${bbBullLVo.readCnt}" type="number" /></a>
		</c:if>
		<c:if test="${bbBullLVo.readHstUseYn != 'Y'}">
			<u:out value="${bbBullLVo.readCnt}" type="number" />
		</c:if>
	</td>
	<td class="body_ct"><c:if test="${bbBullLVo.fileCnt > 0}"><a href="javascript:viewFileListPop('${bbBullLVo.bullId }', '${bbBullLVo.brdId}');"><u:icon type="att" /></a></c:if></td>
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
	<u:input type="hidden" id="brdId" value="${listPage eq 'listMyBull' ? 'my' : 'new'}" />
	<u:input type="hidden" id="schCat" value="${param.schCat}" />
	<u:input type="hidden" id="schWord" value="${param.schWord}" />
	<u:input type="hidden" id="strtYmd" value="${param.strtYmd}" />
	<u:input type="hidden" id="endYmd" value="${param.endYmd}" />
</form>

