<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<%// 문서 비밀번호 확인 후 전송 %>
function openSecuDoc(secuId){
	var $form = $("#toDocForm");
	$form.find("#secuId").val(secuId);
	$form.attr("action", "./${empty dmView ? 'setDoc.do' : 'viewApDocFrm.do'}").attr("method", "GET").submit();
}
$(document).ready(function() {
	${frmYn == 'Y' ? 'parent.' : ''}dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}${not empty param.vwMode ? '&vwMode='.concat(param.vwMode) : ''}${callByIntg=='Y' ? '&callback=openSecuIntgDoc' : frmYn == 'Y' ? '&callback=openSecuDocFrm' : ''}");
	alert("${message}");
});
//-->
</script>
<c:if test="${frmYn != 'Y'}"><u:title titleId="ap.jsp.viewDoc" alt="문서 조회" notPrint="true" /></c:if>

<form id="toDocForm" method="post">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" name="bxId" value="${param.bxId}" />
<u:input type="hidden" id="apvNo" value="${param.apvNo}" /><c:if
	test="${not empty param.apvLnPno}">
<u:input type="hidden" id="apvLnPno" value="${param.apvLnPno}" /></c:if><c:if
	test="${not empty param.apvLnNo}">
<u:input type="hidden" id="apvLnNo" value="${param.apvLnNo}" /></c:if><c:if
	test="${not empty param.queryString}">
<u:input type="hidden" id="queryString" value="${param.queryString}" /></c:if><c:if
	test="${not empty param.vwMode}">
<u:input type="hidden" id="vwMode" value="${param.vwMode}" /></c:if>
<u:input type="hidden" name="secuId" value="" />
</form>