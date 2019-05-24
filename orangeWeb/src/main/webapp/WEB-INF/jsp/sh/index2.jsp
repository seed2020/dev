<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 팝업 열기 %>
var gResultWin = null;
function viewSrchDetlPop(url){
	var popTilte = "searchResultPop";
	if(gResultWin!=null) gResultWin.close();
	gResultWin = window.open(url, popTilte);
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>
<u:title title="${titleValue}" />

<%// 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./index.do"><c:if
		test="${not empty mdRid}">
	<input type="hidden" name="mdRid" value="${mdRid}" /></c:if><c:if
		test="${not empty mdBxId}">
	<input type="hidden" name="mdBxId" value="${mdBxId}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cm.schWord" alt="검색어" /></td>
		<td><u:input id="kwd" titleId="cm.schWord" style="width:250px;" value="${param.kwd}" maxByte="50"/></td>
		<td style="width:20px"></td>
		<u:checkbox value="Y" id="subjYn" name="subjYn" checked="${not empty subjYn or param.subjYn == 'Y'}" titleId="ap.doc.docSubj" />
		<u:checkbox value="Y" id="bodyYn" name="bodyYn" checked="${not empty bodyYn or param.bodyYn == 'Y'}" titleId="ap.btn.body" />
		<u:checkbox value="Y" id="attchYn" name="attchYn" checked="${not empty attchYn or param.attchYn == 'Y'}" titleId="ap.btn.att" />
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>


<%// 목록 %>
<u:listArea id="listArea" >

	<tr>
	<td class="head_ct"><u:msg titleId="ap.doc.docSubj" alt="제목" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="pt.sh.searchLoc" alt="검색 위치" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>

<c:if test="${fn:length(shSrchVoList)==0}" >
	<tr>
	<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(shSrchVoList)!=0}" >
	<c:forEach items="${shSrchVoList}" var="shSrchVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="tr${ptMnuGrpBVo.mnuGrpId}" >
		<td class="body_lt"><a href="javascript:void(0)" onclick="javascript:viewSrchDetlPop('${shSrchVo.url}');" title="<u:out value="${shSrchVo.subj}" type="value"/> - <u:msg titleId="cm.pop"/>"><u:out value="${shSrchVo.subj}" /></a></td>
		<td class="body_ct"><u:out value="${shSrchVo.mdBxNm}" /></td>
		<td class="body_ct"><c:if
			test="${not empty shSrchVo.regrUid}"><a href="javascript:viewUserPop('${shSrchVo.regrUid}')"><u:out value="${shSrchVo.regrNm}" /></a></c:if><c:if
			test="${empty shSrchVo.regrUid}"><u:out value="${shSrchVo.regrNm}" /></c:if></td>
		<td class="body_ct"><u:out value="${shSrchVo.regDt}" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<u:buttonArea>
	<u:button titleId="pt.jsp.listMnuGrp.mngCompSet" alt="관리 범위 설정" href="javascript:setMngCompSetting();" auth="SYS" popYn="Y" />
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:addMnuGrp();" auth="A" popYn="Y" />
	<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delMnuGrp();" auth="A" />
	<u:button titleId="cm.btn.up" alt="위로이동" href="javascript:moveMnuGrp('up');" auth="A" />
	<u:button titleId="cm.btn.down" alt="아래로이동" href="javascript:moveMnuGrp('down');" auth="A" />
</u:buttonArea>