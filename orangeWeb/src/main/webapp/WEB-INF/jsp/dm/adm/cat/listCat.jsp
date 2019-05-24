<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="nonPageParams" excludes="catId,pageNo"/>
<script type="text/javascript">
<!--<% // [하단버튼:항목관리] 항목 관리 %>
function setItemMngPop(id) {
	dialog.open('setItemMngPop','<u:msg titleId="dm.jsp.setItemMgm" alt="항목관리" />','./setItemMngPop.do?menuId=${menuId}&catId='+id);
}<% // [버튼:기본으로설정] 항목 관리 %>
function saveDftCat(id){
	if(confirmMsg("dm.cfrm.set.dftCat")) {	<% // dm.cfrm.set.dftCat=기본으로 설정하시겠습니까? %>
		callAjax('./transCatAjx.do?menuId=${menuId}', {catId:id,dftYn:'Y'}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
}<% // [하단버튼:목록순서] 목록 순서 %>
function setListOrdrPop(id) {
	dialog.open('setListOrdrPop','<u:msg titleId="bb.jsp.setListOrdrPop.title" alt="목록순서" />','./setListOrdrPop.do?menuId=${menuId}&catId='+id);
}<% // [하단버튼:삭제] 삭제 %>
function delCat(id) {
	if (confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./${transDelPage}Ajx.do?menuId=${menuId}', {catId:id}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './${listPage}.do?${nonPageParams}';
			}
		});
	}
}<% // [하단버튼:등록] 테이블 등록 %>
function setItemPop() {
	dialog.open('setItemPop','<u:msg titleId="dm.jsp.setItemMgm" alt="항목관리" />','./setItemPop.do?menuId=${menuId}');
}<%// 상세보기%>
function setCat(id) {
	location.href="./${setPage}.do?${nonPageParams }&catId="+id;
};
$(document).ready(function() {
setUniformCSS();
});
//-->
</script>
<u:title titleId="wr.jsp.listResc.title" alt="자원현황" menuNameFirst="true" />
<% // 검색영역 %>
<u:searchArea>
<form name="searchForm" action="./listCat.do">
	<u:input type="hidden" id="menuId" value="${menuId }"/>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<select name="schCat">
							<u:option value="catNm" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
							<u:option value="catDesc" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
						</select>
					</td>
					<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm.submit();" /></td>
				</tr>
			</table>
		</td>
		<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
</table>
</form>
</u:searchArea>
<% // 목록 %>
<u:listArea colgroup=",15%,15%,25%">
	<tr>
		<td class="head_ct"><u:msg titleId="dm.cols.catNm" alt="유형명" /></td>
		<td class="head_ct"><u:msg titleId="dm.cols.discYn" alt="검증여부" /></td>
		<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		<td class="head_ct"><u:msg titleId="cols.note" alt="비고" /></td>
	</tr>
	<c:choose>
		<c:when test="${!empty dmCatBVoList}">
			<c:forEach var="list" items="${dmCatBVoList }" varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_lt"><a href="javascript:setCat('${list.catId }');">${list.catNm }</a></td>
					<td class="body_ct"><c:if test="${list.discYn eq 'Y'}"><u:msg titleId="cols.discr" alt="심의자"/> : <a href="javascript:viewUserPop('${list.discrUid}');">${list.discrNm }</a></c:if></td>
					<td class="body_ct"><u:out value="${list.regDt }" type="longdate" /></td>
					<td class="body_lt"><u:buttonS href="javascript:setItemMngPop('${list.catId}');" titleId="dm.jsp.setItemMgm" alt="항목관리" auth="A" />
					<u:buttonS href="javascript:setListOrdrPop('${list.catId}');" titleId="bb.btn.listOrdr" alt="목록순서" auth="A" />
					<u:buttonS href="javascript:delCat('${list.catId}');" titleId="cm.btn.del" alt="삭제" auth="A" />
					<c:if test="${empty list.dftYn || list.dftYn eq 'N'}">
						<u:buttonS href="javascript:saveDftCat('${list.catId}');" titleId="dm.btn.set.dft" alt="기본으로설정" auth="A" />
					</c:if>
					</td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
</u:listArea>
<u:pagination  />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="dm.btn.setAddItem" alt="추가항목관리" href="javascript:setItemPop();" auth="A" />
	<u:button titleId="cm.btn.reg" alt="등록" href="./${setPage }.do?${paramsForList }" auth="A" />
</u:buttonArea>