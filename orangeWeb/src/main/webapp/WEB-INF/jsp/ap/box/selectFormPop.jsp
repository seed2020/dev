<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 트리클릭 - 양식함의 목록 조회 %>
function openFormList(formBxId){
	var url = formBxId=='ROOT' ? '${_cxPth}/cm/util/reloadable.do' : null;
	if(url==null){
		url = "./listFormFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}${param.formTypCd=='trans' ? '&formTypCd=trans' : ''}";
		url += "${empty param.forTrans ? '' : '&forTrans='.concat(param.forTrans)}";
		url += "${empty param.refDocApvNo ? '' : '&refDocApvNo='.concat(param.refDocApvNo)}&formBxId="+formBxId;
	}
	reloadFrame("formListFrm", url);
}<%
// 확인 버튼 클릭 %>
function applyDoc(){
	getIframeContent("formListFrm").applyDoc();
}<%
// 새창열기 버튼 클릭 %>
function applyNewTab(){
	getIframeContent("formListFrm").applyDoc(null, true);
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<div style="width:800px">

<div style="height:520px">
<!-- LEFT -->
<div class="left" style="float:left; width:23%;">

<u:title titleId="ap.jsp.setApvForm.setFormBx" type="small" alt="양식함 구성"></u:title>

<u:set test="${not empty param.formBxId}" var="formBxIdParam" value="&formBxId=${param.formBxId}" elseValue="" />
<u:titleArea frameId="formBxFrm" frameSrc="./treeFormBxFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}${formBxIdParam}"
	outerStyle="height:480px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:470px;" noBottomBlank="true" />
</div>

<!-- RIGHT -->
<div class="right" style="float:right; width:75%;">

<u:title titleId="ap.jsp.setApvForm.listForm" type="small" alt="양식 목록"></u:title>

<u:titleArea frameId="formListFrm" frameSrc="${_cxPth}/cm/util/reloadable.do"
	outerStyle="height:480px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:470px;" noBottomBlank="true" />
</div>
</div>

<u:buttonArea><c:if
		test="${empty param.formTypCd and empty param.refDocApvNo}">
	<u:button titleId="ap.btn.newWin" alt="새창열기" href="javascript:applyNewTab();" auth="W" /></c:if>
	<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applyDoc();" auth="W" />
	<u:button titleId="cm.btn.cancel" alt="취소" onclick="dialog.close(this);" />
</u:buttonArea>

</div>