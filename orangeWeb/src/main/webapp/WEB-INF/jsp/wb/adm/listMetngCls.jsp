<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
//삭제
function fnClsDelete(rescId){
	if(rescId == null ) return;
	$('#rescId').val(rescId);
	if(confirmMsg("cm.cfrm.del")) {
		var $form = $('#deleteClsForm');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();	
	}
};

$(document).ready(function() {
setUniformCSS();
});
//-->
</script>

<u:title titleId="wb.jsp.listMetngCls.title" alt="관련미팅분류" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
<form name="searchForm" action="./listMetngCls.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />

<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="search_tit"><u:msg titleId="cols.clsNm" alt="분류명" /></td>
					<td><u:input id="schWord" maxByte="50" value="${param.schWord }" titleId="cols.schWord" style="width: 400px;" /></td>
				</tr>
			</table>
		</td>
		<td>
			<div class="button_search">
				<ul>
					<li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li>
				</ul>
			</div>
		</td>
	</tr>
</table>
</form>
</u:searchArea>

<% // 목록 %>
<u:listArea id="listArea">
	<tr>
		<td class="head_ct"><u:msg titleId="cols.clsNm" alt="분류명" /></td>
		<td width="20%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일" /></td>
		<td width="20%" class="head_ct"><u:msg titleId="cols.fnc" alt="기능" /></td>
	</tr>
	<c:choose>
		<c:when test="${!empty wbMetngClsCdBVoList}">
			<c:forEach var="list" items="${wbMetngClsCdBVoList }" varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_ct">
						<u:msg var="setMetngClsModPop" titleId="wb.jsp.setMetngClsModPop.title" alt="관련미팅분류 수정"/>
						<a href="javascript:dialog.open('setMetngClsPop','${setMetngClsModPop }','./setMetngClsPop.do?menuId=${menuId}&rescId=${list.rescId }');">${list.rescNm }</a></td>
					<td class="body_ct">${list.regDt }</td>
					<td>
						<table align="center" border="0" cellpadding="0" cellspacing="0">
							<tbody>
							<tr>
								<td><u:buttonS href="javascript:dialog.open('setMetngClsPop','${setMetngClsModPop }','./setMetngClsPop.do?menuId=${menuId}&rescId=${list.rescId }');" titleId="cm.btn.mod" alt="수정" auth="A"/></td>
								<td><u:buttonS href="javascript:fnClsDelete('${list.rescId }');" titleId="cm.btn.del" alt="삭제" auth="A"/></td>
							</tr>
							</tbody>
						</table>
					</td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="3"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
</u:listArea>

<u:pagination />

<u:msg var="setMetngClsRegPop" titleId="wb.jsp.setMetngClsRegPop.title" alt="관련미팅분류 등록"/>
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.reg" alt="등록" href="javascript:dialog.open('setMetngClsPop','${setMetngClsRegPop }','./setMetngClsPop.do?menuId=${menuId}');" auth="A" />
</u:buttonArea>
<form id="deleteClsForm" name="deleteClsForm" action="./transMetngClsDel.do">
	<u:input type="hidden" name="menuId" value="${menuId}" />
	<u:input type="hidden" name="rescId"  id="rescId"/>
</form>

