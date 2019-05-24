<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="params"/>
<script type="text/javascript">
<!--<%// 선택된 그룹 %>
var gGrpId = null;
<%// [아이콘] 위로이동 %>
function moveUp(frmId){
	moveDirection(frmId, 'up');
}
<%// [아이콘] 아래로이동 %>
function moveDown(frmId){
	moveDirection(frmId, 'down');
}
<%// [아이콘] 위/아래로 이동 %>
function moveDirection(frmId, direction){
	if(frmId=='grpTree'){
		getIframeContent(frmId).move(direction);
	}
}
<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openListFrm(id){
	if(id!=null){
		gGrpId = id;
	}
	
	<c:if test="${urlOpt eq 'works'}">
	var treeData=getIframeContent('grpTree').getTreeData();
	if(gGrpId=='ROOT' || treeData['grpTyp']!='A'){
		$("#formListFrm").attr('src', '/cm/util/reloadable.do');
	}else{
		$("#formListFrm").attr('src', './listWorksFrm.do?menuId=${menuId}&formNo='+gGrpId);	
	}
	</c:if>
	<c:if test="${urlOpt ne 'works'}">
	if(gGrpId=='ROOT'){
		$("#formListFrm").attr('src', '/cm/util/reloadable.do');
	}else{
		$("#formListFrm").attr('src', './listFormFrm.do?menuId=${menuId}&grpId='+gGrpId);	
	}
	</c:if>
	
}<%// [버튼]모듈 이동 %>
function moveGrp() {
	var selId = getSelectId();
	if(selId == null) return;
	var url = './findGrpPop.do?menuId=${menuId}&lstTyp=F&mode=move';	
	dialog.open('findGrpPop', '<u:msg titleId="dm.cols.listTyp.fld" alt="모듈보기" />', url);
}<%// 모듈 이동 저장 %>
function setGrpInfos(arr, lstTyp){
	if(arr == undefined || arr.length == 0) return;
	var selId = getSelectId();
	if(selId == null) return;
	
	if(selId == arr[0].id){
		alertMsg("dm.msg.not.save.duplGrp");<%//// dm.msg.not.save.duplGrp=동일한 모듈로 저장할수 없습니다.%>
		return;
	}
	// 부모모듈ID
	var fldPid = arr[0].id;
	callAjax('./transGrpMoveAjx.do?menuId=${menuId}', {fldPid:fldPid, fldId:selId}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			reloadFrame('grpTree', './treeGrpFrm.do?menuId=${menuId}&fldId='+fldPid);
		}
		dialog.close('findGrpPop');
	});
}<%// [팝업:저장 후처리] - 트리 리로드 %>
function reloadTree(grpId){
	getIframeContent('grpTree').reload('./treeGrpFrm.do?menuId=${menuId}&grpId='+grpId);
};<%// [좌하단버튼] - 등록|수정 %>
function setGrpPop(mod){
	var treeData=getIframeContent('grpTree').getTreeData();
	var isMod=mod!=undefined && mod;
	if(isMod && treeData['id']=='ROOT'){
		alertMsg("cm.msg.mod.root");<%//cm.msg.mod.root=최상위 항목은 수정 할 수 없습니다.%>
		return;
	}
	var url = './setGrpPop.do?menuId=${menuId}';
	if(isMod)
		url+='&grpId='+treeData['grpId'];
	
	if(!isMod && treeData['id']!='ROOT')
		url+='&grpPid='+treeData['grpId'];
		
	dialog.open('setGrpDialog', '<u:msg titleId="wf.jsp.form.set.grp.title" alt="양식 그룹 구성" />', url);	
	
}<% // [좌하단버튼] - 삭제 %>
function delGrp(){
	getIframeContent('grpTree').delGrp();
}<% // 양식[기본정보] 저장 %>
function formSave(){
	getIframeContent('formListFrm').formSave();
}
<%// 저장, 삭제시 리로드 - 오른쪽 리스트 열기 %>
function reloadForm(url, dialogId){
	if(dialogId!=undefined){
		dialog.close(dialogId);
	}
	getIframeContent('formListFrm').reloadForm(url, dialogId);
};
<%// [등록] - 우측하단버튼 %>
function setForm(){
	getIframeContent('formListFrm').setForm();
}
<%// [삭제] - 우측하단버튼 %>
function delFormList(){
	getIframeContent('formListFrm').delFormList();
}<%// [취소] - 우측하단버튼 %>
function listForm(){
	getIframeContent('formListFrm').listForm();
}<%// [이동] - 우측하단버튼 %>
function setMoveToFormGrpPop(){
	getIframeContent('formListFrm').setMoveToFormGrpPop();
}<% // [우측하단버튼:배포] - 선택된 양식을 배포(실제 테이블 구성) %>
function deployForm(){
	getIframeContent('formListFrm').deployForm();
}<% // [하단버튼:배포이력] - 배포이력 팝업 %>
function listDeployPop(){
	getIframeContent('formListFrm').listDeployPop();
}<%// [등록(목록관리)] - 우측하단버튼 %>
function setRegWorks(){
	getIframeContent('formListFrm').setRegWorks();
}<% // [하단버튼:메뉴등록] - 팝업 %>
function setMnuPop(val){
	getIframeContent('formListFrm').setMnuPop(val);
}<% // [하단버튼:삭제] 삭제 %>
function delWorks() {
	getIframeContent('formListFrm').delWorks();
}<%
//[팝업] - 양식 업로드 %>
function setFormUploadPop(){
	getIframeContent('formListFrm').setFormUploadPop();
}
<% // 양식 다운로드 %>
function formDownload() {
	getIframeContent('formListFrm').formDownload();
}
<%// 버튼 보이기 조절 - displayBtn 함수 이용  id배열 넘겨줌 %>
function applyFormBtn(){
	var $area = $("#rightBtnArea");
	$area.find('ul>li').remove();
	var btnHtml = getIframeContent('formListFrm').getRightBtnList();
	if(btnHtml != '')
		$area.append(btnHtml);
}<%// [화살표<< >>버튼] - 우측 목록 전체화면 %>
function pageToggle(typ){
	if(typ=='close'){
		$('#leftArea').hide();
		$('#rightArea').css('width', '98%');
		$('#toggleBtnArea a#closeBtn').hide();
		$('#toggleBtnArea a#openBtn').show();
	}else if(typ=='open'){
		$('#leftArea').show();
		$('#rightArea').css('width', '78%');
		$('#toggleBtnArea a#closeBtn').show();
		$('#toggleBtnArea a#openBtn').hide();
	}
}
//-->
</script>

<u:title titleId="wf.jsp.set.form.title" alt="양식관리" menuNameFirst="true" />

<!-- LEFT -->
<div style="float:left; width:20%;" id="leftArea">

<u:title titleId="wf.jsp.form.set.grp.title" type="small" alt="양식 그룹 구성" >
	<u:titleIcon type="up" onclick="moveUp('grpTree')" auth="A" />
	<u:titleIcon type="down" onclick="moveDown('grpTree')" auth="A" />
</u:title>

<u:titleArea frameId="grpTree" frameSrc="./treeGrpFrm.do?${params }"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
	<u:buttonArea>
		<u:button href="javascript:setGrpPop();" titleId="cm.btn.reg" alt="등록" auth="A" />
		<u:button href="javascript:setGrpPop(true);" titleId="cm.btn.mod" alt="수정" auth="A" />
		<u:button href="javascript:delGrp();" titleId="cm.btn.del" alt="삭제" auth="A" />
	</u:buttonArea>
</div>

<div id="toggleBtnArea" style="float:left; width:2%; text-align:center; margin:250px 0 0 0;">
	<a id="openBtn" href="javascript:pageToggle('open');"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" /> <c:if test="${empty param.pageClose || param.pageClose eq 'N'}">style="display:none;"</c:if>><img src="${_cxPth}/images/${_skin}/arrowr.png" width="15" height="17" /></a>
	<a id="closeBtn" href="javascript:pageToggle('close');"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" /> <c:if test="${!empty param.pageClose && param.pageClose eq 'Y'}">style="display:none;"</c:if>><img src="${_cxPth}/images/${_skin}/arrowl.png" width="15" height="17" /></a>
</div>
	
<!-- RIGHT -->
<div style="float:right; width:78%;" id="rightArea">

<u:title titleId="wf.jsp.form.list.title" type="small" alt="양식 목록" >
	<%-- <u:titleIcon type="up" onclick="moveUp('formListFrm')" auth="A" />
	<u:titleIcon type="down" onclick="moveDown('formListFrm')" auth="A" /> --%>
</u:title>

<u:titleArea frameId="formListFrm" frameSrc="/cm/util/reloadable.do"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
	<u:buttonArea id="rightBtnArea"><li></li></u:buttonArea>
	
</div>

<u:blank />

