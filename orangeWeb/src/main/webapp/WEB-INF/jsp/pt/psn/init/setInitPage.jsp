<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 저장 버튼 %>
function doSubmit(){
	var $form = $('#initPageForm');
	$form.attr("action","./transInitPage.do");
	$form.attr("target","dataframe");
	$form.submit();
}<%
// 로그인 사용자 선택 버튼 %>
function setInitUser(){
	var popTitle = '<u:msg titleId="pt.jsp.setInitUser" alt="로그인 사용자 선택" />';
	dialog.open("setInitUserDialog", popTitle, "./setInitUserPop.do?menuId=${menuId}");
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.setSkin.title" alt="스킨 설정" menuNameFirst="true" />

<form id="initPageForm">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea colgroup="${layout.icoLoutUseYn!='Y' or layout.bascLoutUseYn!='Y' ? '27%,73%' : '16%,34%,16%,34%'}">
	<tr>
	<c:if test="${layout.icoLoutUseYn=='Y'}">
		<td class="head_ct"><u:msg titleId="pt.jsp.setSkin.iconLout" alt="아이콘 레이아웃" /></td>
		<td class="body_lt" valign="top">
			<table border="0" cellpadding="0" cellspacing="0">
			<c:forEach items="${iconList}" var="ptMnuLoutDVo" varStatus="status">
			<c:if test="${fn:length(ptMnuLoutDVo.childList) <= 1}"
			><tr><u:radio value="${ptMnuLoutDVo.childList[0].mnuLoutId}" name="iconInitPage" checkValue="${loginMap.iconInitPage}" textColspan="2"
				title="${ptMnuLoutDVo.childList[0].rescNm}" noSpaceTd="true" /></tr></c:if>
			<c:if test="${fn:length(ptMnuLoutDVo.childList) > 1}"
				><tr><td></td><td colspan="2">${ptMnuLoutDVo.rescNm}</td></tr>
			<c:forEach
					items="${ptMnuLoutDVo.childList}" var="subPtMnuLoutDVo" varStatus="subStatus">
			<tr><td></td><u:radio value="${subPtMnuLoutDVo.mnuLoutId}" name="iconInitPage" checkValue="${loginMap.iconInitPage}"
				title="${subPtMnuLoutDVo.rescNm}" noSpaceTd="true" /></tr></c:forEach></c:if></c:forEach>
			<tr><u:radio value="" name="iconInitPage" checked="${empty loginMap.iconInitPage}" textColspan="2"
				titleId="cm.option.default" alt="기본값" noSpaceTd="true" /></tr>
			</table>
		</td>
	</c:if>
	<c:if test="${layout.bascLoutUseYn=='Y'}">
		<td class="head_ct"><u:msg titleId="pt.jsp.setSkin.bascLout" alt="기본 레이아웃" /></td>
		<td class="body_lt" valign="top">
			<table border="0" cellpadding="0" cellspacing="0">
			<c:forEach items="${mainList}" var="ptMnuLoutDVo" varStatus="status">
			<c:if
				test="${ptMnuLoutDVo.mnuLoutKndCd != 'F'}"
					><tr><u:radio value="${ptMnuLoutDVo.mnuLoutId}" name="bascInitPage" checkValue="${loginMap.bascInitPage}" textColspan="2"
				title="${ptMnuLoutDVo.rescNm}" noSpaceTd="true" /></tr></c:if>
			<c:if
				test="${ptMnuLoutDVo.mnuLoutKndCd == 'F'}"
					><tr><td></td><td colspan="2">${ptMnuLoutDVo.rescNm}</td></tr>
			<c:forEach items="${ptMnuLoutDVo.childList}" var="subPtMnuLoutDVo" varStatus="subStatus"
					><tr><td></td><u:radio value="${subPtMnuLoutDVo.mnuLoutId}" name="bascInitPage" checkValue="${loginMap.bascInitPage}"
				title="${subPtMnuLoutDVo.rescNm}" noSpaceTd="true" /></tr></c:forEach></c:if>
			</c:forEach>
			<tr><u:radio value="" name="bascInitPage" checked="${empty loginMap.bascInitPage}" textColspan="2"
				titleId="cm.option.default" alt="기본값" noSpaceTd="true" /></tr>
			</table>
		</td>
	</c:if>
	</tr>
</u:listArea>
</form>

<u:buttonArea><c:if
	test="${fn:length(sessionScope.userVo.adurs)>1}">
	<u:button titleId="pt.jsp.setInitUser" alt="로그인 사용자 선택" onclick="setInitUser()" auth="W" /></c:if>
	<u:button titleId="cm.btn.save" alt="저장" onclick="doSubmit()" auth="W" />
</u:buttonArea>
