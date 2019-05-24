<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--

<% // 검색 버튼 %>
function searchBullCat(event) {
	if (event != null) {
		if (event.keyCode != 13) return;
		event.preventDefault();
	}
	var $form = $("#selectBullCatForm");
	$form.attr('action','/bb/adm/selectBullCatFrm.do?menuId=${menuId}');
	$form.submit();
}
$(document).ready(function() {
	setUniformCSS();
	$('#schWord').focus();
});
//-->
</script>

<% // 검색 %>
<form id="selectBullCatForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:listArea>
	<tr>
	<td width="27%" class="head_lt"><u:msg titleId="cols.catGrp" alt="카테고리그룹" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.catGrp" onkeydown="searchBullCat(event, 'keydown')" />
			<u:input type="hidden" id="schCat" value="CAT_GRP_NM" />
			</td>
		<td><a href="javascript:searchBullCat();" class="ico_search"><span><u:msg alt="검색" titleId="cm.btn.search" /></span></a></td>
		</tr>
		</table></td>
	</tr>
</u:listArea>
</form>

<u:listArea>
	<tr>
	<td width="27" class="head_bg">&nbsp;</td>
	<td class="head_ct"><u:msg titleId="cols.catGrp" alt="카테고리그룹" /></td>
	</tr>

<c:if test="${fn:length(baCatGrpBVoList) == 0}">
	<tr>
	<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baCatGrpBVoList) > 0}">
	<c:forEach items="${baCatGrpBVoList}" var="baCatGrpBVo" varStatus="status">
	<tr>
	<td class="bodybg_ct"><u:radio id="radio${baCatGrpBVo.catGrpId}" name="radio" value="${baCatGrpBVo.catGrpId}" checkValue="${param.selectedId}" />
		<u:input type="hidden" id="nm" value="${baCatGrpBVo.rescNm}" />
		</td>
	<td class="body_lt"><label for="radio${baCatGrpBVo.catGrpId}">${baCatGrpBVo.rescNm}</label></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination noTotalCount="true" />
