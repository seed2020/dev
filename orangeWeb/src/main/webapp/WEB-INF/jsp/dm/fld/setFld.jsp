<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<%// 선택된 조직 %>
var gFldId = null;
<%// [아이콘] 위로이동 %>
function moveUp(){
	moveDirection('up');
}
<%// [아이콘] 아래로이동 %>
function moveDown(){
	moveDirection('down');
}
<%// [아이콘] 위/아래로 이동 %>
function moveDirection(direction){
	if(gFldId==null){
		alertMsg("or.jsp.setOrg.msg1");<%//dm.jsp.setFld.msg1=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
	} else {
		var contWin = getIframeContent('openListFrm');
		contWin.move(direction);
	}
}<%// [우하단 버튼] 행추가%>
function addRow(){
	if(gFldId==null){
		alertMsg("or.jsp.setOrg.msg1");<%//dm.jsp.setFld.msg1=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').addRow();
	}
}
<%// [우하단 버튼] 행삭제 %>
function delRow(){
	if(gFldId==null){
		alertMsg("or.jsp.setOrg.msg1");<%//dm.jsp.setFld.msg1=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').delRow();
	}
}
<%// [우하단 버튼] 선택삭제 %>
function delSelRow(){
	if(gFldId==null){
		alertMsg("or.jsp.setOrg.msg1");<%//dm.jsp.setFld.msg1=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').delSelRow();
	}
}
<%// [우하단 버튼] 저장 %>
function saveFld(){
	if(gFldId==null){
		alertMsg("or.jsp.setOrg.msg1");<%//dm.jsp.setFld.msg1=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').saveFld();
	}
}<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openListFrm(id){
	if(id!=null){
		gFldId = id;
	}
	reloadFrame('openListFrm', './listFldFrm.do?menuId=${menuId}&fldPid='+id);
}<%// 폴더 이동%>
function getSelectId() {
	var selId = getIframeContent('fldTree').getSelectId();
	if(selId == null){
		alertMsg("dm.jsp.setDoc.not.fldSub");<%//dm.jsp.setDoc.not.fldSub=하위 '폴더'를 선택 후 사용해 주십시요.%>
		return null;
	}
	if(selId == 'NONE'){
		alertMsg("dm.msg.not.save.emptyCls");<%//dm.msg.not.save.emptyCls='미분류' 로 저장할 수 없습니다.%>
		return null;
	}
	return selId;
}<%// [버튼]폴더 이동 %>
function moveFld() {
	var selId = getSelectId();
	if(selId == null) return;
	var url = './findFldPop.do?menuId=${menuId}&lstTyp=F&mode=move';	
	dialog.open('findFldPop', '<u:msg titleId="dm.cols.listTyp.fld" alt="폴더보기" />', url);
}<%// 폴더 이동 저장 %>
function setFldInfos(arr, lstTyp){
	if(arr == undefined || arr.length == 0) return;
	var selId = getSelectId();
	if(selId == null) return;
	
	if(selId == arr[0].id){
		alertMsg("dm.msg.not.save.duplFld");<%//// dm.msg.not.save.duplFld=동일한 폴더로 저장할수 없습니다.%>
		return;
	}
	// 부모폴더ID
	var fldPid = arr[0].id;
	callAjax('./transFldMoveAjx.do?menuId=${menuId}', {fldPid:fldPid, fldId:selId}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			reloadFrame('fldTree', './treeFldFrm.do?menuId=${menuId}&fldId='+fldPid);
		}
		dialog.close('findFldPop');
	});
}

//-->
</script>

<u:title titleId="dm.jsp.fldPsn.title" alt="개인폴더" menuNameFirst="true" />

<!-- LEFT -->
<div style="float:left; width:27.8%;">

<u:title titleId="cols.fld" type="small" alt="폴더" />

<u:titleArea frameId="fldTree" frameSrc="./treeFldFrm.do?menuId=${menuId}"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
<div class="titlearea">
	<div class="tit_right">
		<u:titleButton titleId="cm.btn.move" alt="이동" onclick="moveFld();" auth="W"/>
	</div>
</div>	
</div>

	
<!-- RIGHT -->
<div style="float:right; width:71%;">

<u:title titleId="dm.jsp.list.fld.title" type="small" alt="폴더목록" >
	<u:titleIcon type="up" onclick="moveUp()" id="rightUp" auth="W" />
	<u:titleIcon type="down" onclick="moveDown()" id="rightDown" auth="W" />
</u:title>

<u:titleArea frameId="openListFrm" frameSrc="/cm/util/reloadable.do"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
	<u:buttonArea>
		<u:button href="javascript:addRow();" id="btnAddRow" titleId="cm.btn.plus" alt="행추가" auth="W" />
		<u:button href="javascript:delRow();" id="btnDelRow" titleId="cm.btn.minus" alt="행삭제" auth="W" />
		<u:button href="javascript:delSelRow();" id="btnDelSel" titleId="cm.btn.selDel" alt="선택삭제" auth="W" />
		<u:button href="javascript:saveFld();" id="btnSave" titleId="cm.btn.save" alt="저장" auth="W" />
	</u:buttonArea>
	
</div>

<u:blank />

