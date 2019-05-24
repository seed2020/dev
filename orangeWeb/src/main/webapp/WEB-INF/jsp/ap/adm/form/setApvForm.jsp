<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 트리클릭 - 양식함의 목록 조회 %>
function openFormList(formBxId){
	var url = formBxId=='ROOT' ? '${_cxPth}/cm/util/reloadable.do' : './listFormFrm.do?menuId=${menuId}&formBxId='+formBxId;
	reloadFrame("formListFrm", url);
}<%
// [아이콘] 위로이동 %>
function moveSort(frmId, direction){
	var tree = getIframeContent("formBxFrm").getTreeData('reg');
	if(tree.id=='ROOT' && frmId=='formListFrm'){
		alertMsg("ap.jsp.setApvForm.selFormBxFirst");<%//ap.jsp.setApvForm.selFormBxFirst=양식함을 먼저 선택해 주십시요.%>
	} else {
		getIframeContent(frmId).move(direction);
	}
}<%
// 양식함 등록/수정/삭제 버튼 %>
function manageFormBx(mode){
	getIframeContent("formBxFrm").manageFormBx(mode);
}<%
// [추가] - 양식 추가 팝업 %>
function openFormPop(){
	var tree = getIframeContent("formBxFrm").getTreeData('reg');
	if(tree.id=='ROOT'){
		alertMsg("ap.jsp.setApvForm.selFormBxFirst");<%//ap.jsp.setApvForm.selFormBxFirst=양식함을 먼저 선택해 주십시요.%>
	} else {
		getIframeContent("formListFrm").openFormPop();
	}
}<%
// [이동] - 양식 이동 팝업 %>
function moveForm(){
	var arr = getIframeContent("formListFrm").getSelected();
	if(arr!=null){
		dialog.open('setMoveToFormBxDialog','<u:msg titleId="or.btn.cut" alt="이동"/>','./setMoveToFormBxPop.do?menuId=${menuId}&formIds='+arr.join(','));
	}
}<%
// 삭제 %>
function delForm(){
	var arr = getIframeContent("formListFrm").getSelected();
	if(arr==null) return;
	
	callAjax('./transFormDelAjx.do?menuId=${menuId}', {formIds:arr.join(',')}, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.result=='ok'){
			getIframeContent("formListFrm").reload();
		}
	});	
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<u:title title="양식 관리" menuNameFirst="true" />

<!-- LEFT -->
<div class="left" style="float:left; width:23.5%;">

<u:title titleId="ap.jsp.setApvForm.setFormBx" type="small" alt="양식함 구성">
	<u:titleIcon type="up" href="javascript:moveSort('formBxFrm','up')" auth="A" />
	<u:titleIcon type="down" href="javascript:moveSort('formBxFrm','down')" auth="A" />
</u:title>

<u:set test="${not empty param.formBxId}" var="formBxIdParam" value="&formBxId=${param.formBxId}" elseValue="" />
<u:titleArea frameId="formBxFrm" frameSrc="./treeFormBxFrm.do?menuId=${menuId}${formBxIdParam}"
	outerStyle="height:580px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:570px;" />

<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:manageFormBx('reg')" titleId="cm.btn.reg" alt="등록" auth="A" popYn="Y" />
	<u:button href="javascript:manageFormBx('mod')" titleId="cm.btn.mod" alt="수정" auth="A" popYn="Y" />
	<u:button href="javascript:manageFormBx('del')" titleId="cm.btn.del" alt="삭제" auth="A" />
</u:buttonArea>
</div>

<!-- RIGHT -->
<div class="right" style="float:right; width:75%;">

<u:title titleId="ap.jsp.setApvForm.listForm" type="small" alt="양식 목록">
	<u:titleIcon type="up" href="javascript:moveSort('formListFrm','up')" auth="A" />
	<u:titleIcon type="down" href="javascript:moveSort('formListFrm','down')" auth="A" />
</u:title>

<u:titleArea frameId="formListFrm" frameSrc="${_cxPth}/cm/util/reloadable.do"
	outerStyle="height:580px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:570px;" />

<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:moveForm()" titleId="or.btn.cut" alt="이동" auth="A" />
	<u:button href="javascript:openFormPop()" titleId="cm.btn.reg" alt="등록" auth="A" />
	<u:button href="javascript:delForm()" titleId="cm.btn.del" alt="삭제" auth="A" />
</u:buttonArea>

</div>