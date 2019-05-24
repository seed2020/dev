<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="compIdParam" test="${!empty param.compId }" value="&compId=${param.compId }" elseValue=""/>
<u:set var="listPage" test="${!empty ptCompBVoList }" value="listCoprBull" elseValue="listBull"/>
<u:params var="params" />
<script type="text/javascript">
<!--
<% // [목록:제목] 게시물 조회 %>
function viewBull(id) {
	location.href = './${viewPage}.do?${params}&bullId=' + id;
}
<% // [목록:제목] 보안글 조회를 위한 로그인폼 화면 %>
function openLogin(id) {
	dialog.open('setLoginPop','<u:msg titleId="bb.jsp.setLoginPop.title" alt="보안글 인증" />','./setLoginPop.do?${params}&viewPage=${viewPage}&bullId=' + id);
}
<% // [목록:조회수] 조회이력 %>
function readHst(id) {
	dialog.open('listReadHstPop','<u:msg titleId="bb.jsp.listReadHstPop.title" alt="조회자 정보" />','./listReadHstPop.do?menuId=${menuId}&bullId=' + id);
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<!-- 타이틀 -->
<jsp:include page="/WEB-INF/jsp/bb/listTopTitle.jsp" />

<form id="searchForm" name="searchForm" action="./${listPage }.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="brdId" value="${param.brdId}" />

<% // 검색영역 %>
<u:searchArea>
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
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" onkeydown="if (event.keyCode == 13) searchForm.submit();" /></td>
		<!-- 카테고리 -->
		<c:if test="${baBrdBVo.catYn == 'Y'}">
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.cat" alt="카테고리" /></td>
		<td><select name="catId" onchange="searchForm.submit();">
			<u:option value="" titleId="cm.option.all" alt="전체" checkValue="${param.catId}" />
			<c:forEach items="${baCatDVoList}" var="catVo" varStatus="status">
			<u:option value="${catVo.catId}" title="${catVo.rescNm}" checkValue="${param.catId}" />
			</c:forEach>
			</select></td>
		</c:if>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
</u:searchArea>

<% // 목록타입 %>
<c:if test="${baBrdBVo.photoYn == 'Y'}">
<div class="titlearea">
	<div class="tit_right">
	<ul>
	<li><select name="listTyp" onchange="searchForm.submit();">
		<u:option value="I" titleId="bb.cols.listTyp.Image" alt="앨범형" checkValue="${param.listTyp}" />
		<u:option value="B" titleId="bb.cols.listTyp.Board" alt="게시판형" checkValue="${param.listTyp}" />
		</select></li>
	</ul>
	</div>
</div>
</c:if>

</form>

<% // 앨범형 목록 %>
<c:if test="${fn:length(bbBullLVoList) == 0}">
	<u:listArea id="listArea">
		<tr>
		<td class="nodata" colspan="${fn:length(baColmDispDVoList)}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</u:listArea>
</c:if>
<c:if test="${fn:length(bbBullLVoList) > 0}">

	<u:titleArea outerStyle="overflow:hidden;" innerStyle="padding:0 0 0 10px;">
		<c:forEach items="${bbBullLVoList}" var="bbBullLVo" varStatus="status">
		<c:set var="photoVo" value="${bbBullLVo.photoVo}" />
		<c:set var="maxWdth" value="100" />
		<c:set var="maxHght" value="100" />
		<u:set test="${photoVo != null}" var="savePath" value="${_cxPth}${photoVo.savePath}" elseValue="${_cxPth}/images/${_skin}/photo_noimg.png" />
		<u:set test="${photoVo != null && photoVo.imgWdth <= maxWdth}" var="imgWdth" value="${photoVo.imgWdth}" elseValue="${maxWdth}" />
		<u:set test="${photoVo != null && photoVo.imgHght <= maxHght}" var="imgHght" value="${photoVo.imgHght}" elseValue="${maxHght}" />
		<u:set test="${photoVo.imgWdth < photoVo.imgHght}" var="imgWdthHgth" value="height='${imgHght}'" elseValue="width='${imgWdth}'" />
		<u:set test="${bbBullLVo.secuYn == 'Y'}" var="viewBull" value="openLogin" elseValue="viewBull" />
		<div class="listarea" style="float:left; width:19.2%; padding:8px 8px 0 0;">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1"><tbody>
		<tr>
		<td class="photo_ct"><a href="javascript:${viewBull}('${bbBullLVo.bullId}');"><img src="${savePath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' ${imgWdthHgth}></a></td>
		</tr>
		<tr>
		<td style="padding: 4px;">
			<table border="0" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;"><tbody>
			<tr>
			<td class="body_lt">
				<div class="ellipsis" title="${bbBullLVo.subj}" >
				<u:set test="${bbBullLVo.readYn == 'Y'}" var="subjMaxLength" value="18" elseValue="16" />
				<u:set test="${bbBullLVo.secuYn == 'Y'}" var="subjMaxLength" value="${subjMaxLength-4 }" elseValue="${subjMaxLength }" />
				<u:set test="${bbBullLVo.ugntYn == 'Y'}" var="subjMaxLength" value="${subjMaxLength-4 }" elseValue="${subjMaxLength }" />
				<u:icon type="new" display="${bbBullLVo.newYn == 'Y'}" />
				<u:set test="${bbBullLVo.readYn == 'Y'}" var="style" value="font-weight: normal;" elseValue="font-weight: bold;" />
				<u:set test="${bbBullLVo.ugntYn == 'Y'}" var="style" value="${style} color:red;" elseValue="${style}" />
				<a href="javascript:${viewBull}('${bbBullLVo.bullId}');" title="${bbBullLVo.subj}" style="${style}">
				<c:if test="${bbBullLVo.ugntYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if>
				<c:if test="${bbBullLVo.secuYn == 'Y'}"><span style="${style}">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if>
				<u:out value="${bbBullLVo.subj}" />
				<c:if test="${baBrdBVo.cmtYn == 'Y'}"><span style="font-size: 10px;">(<u:out value="${bbBullLVo.cmtCnt}" type="number" />)</span></c:if></a>
				</div>
				</td>
			</tr>
			<tr>
			<td class="body_lt">
				<u:msg titleId="cols.readCnt" alt="조회수" />
				<c:if test="${baBrdBVo.readHstUseYn == 'Y'}">
				<a href="javascript:readHst('${bbBullLVo.bullId}');"><u:out value="${bbBullLVo.readCnt}" type="number" /></a>
				</c:if>
				<c:if test="${baBrdBVo.readHstUseYn != 'Y'}">
				<u:out value="${bbBullLVo.readCnt}" type="number" />
				</c:if>
				|
				<u:out value="${bbBullLVo.regDt}" type="shortdate" />
				</td>
			</tr>
			
			<tr>
			<td class="body_lt"><a href="javascript:viewUserPop('${bbBullLVo.regrUid}');"><u:out value="${bbBullLVo.regrNm}" /></a></td>
			</tr>
			</tbody></table></td>
		</tr>
		</tbody></table>
		</div>
		
		</c:forEach>
	
		<u:blank />
	</u:titleArea>

</c:if>

<u:pagination />

<c:if test="${empty ptCompBVoList || (!empty ptCompBVoList && !empty param.brdId) }">
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.write" alt="등록" href="./setBull.do?menuId=${menuId}&brdId=${param.brdId}${compIdParam }" auth="W" />
</u:buttonArea>
</c:if>

<% // 게시판 담당자 %>
<c:if test="${baBrdBVo.pichDispYn == 'Y'}">
<u:title titleId="bb.jsp.listBull.pich.title" alt="게시판 담당자" type="small" />

<u:listArea id="pichArea">

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.pich" alt="담당자" /></td>
	<td width="32%" class="body_lt"><a href="javascript:viewUserPop('${baBrdBVo.pichUid}');">${baBrdBVo.pichVo.rescNm}</a></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="32%" class="body_lt">${baBrdBVo.pichVo.deptRescNm}</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.phon" alt="전화번호" /></td>
	<td class="body_lt">${baBrdBVo.pichPinfoVo.mbno}</td>
	<td class="head_lt"><u:msg titleId="cols.email" alt="이메일" /></td>
	<td class="body_lt"><a href="javascript:parent.mailToPop('${baBrdBVo.pichPinfoVo.email}')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />">${baBrdBVo.pichPinfoVo.email}</a></td>
	</tr>

</u:listArea>
</c:if>