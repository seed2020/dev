<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<%// 선택된 조직 %>
var gClsId = null;
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
	if(gClsId==null){
		alertMsg("dm.jsp.setDoc.not.cls");<%//dm.jsp.setDoc.not.cls='분류'를 선택 후 사용해 주십시요.%>
	} else {
		var contWin = getIframeContent('openListFrm');
		contWin.move(direction);
	}
}<%// [우하단 버튼] 행추가%>
function addRow(){
	if(gClsId==null){
		alertMsg("dm.jsp.setDoc.not.cls");<%//dm.jsp.setDoc.not.cls='분류'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').addRow();
	}
}
<%// [우하단 버튼] 행삭제 %>
function delRow(){
	if(gClsId==null){
		alertMsg("dm.jsp.setDoc.not.cls");<%//dm.jsp.setDoc.not.cls='분류'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').delRow();
	}
}
<%// [우하단 버튼] 선택삭제 %>
function delSelRow(){
	if(gClsId==null){
		alertMsg("dm.jsp.setDoc.not.cls");<%//dm.jsp.setDoc.not.cls='분류'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').delSelRow();
	}
}
<%// [우하단 버튼] 저장 %>
function saveCls(){
	if(gClsId==null){
		alertMsg("dm.jsp.setDoc.not.cls");<%//dm.jsp.setDoc.not.cls='분류'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').saveCls();
	}
}<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openListFrm(id){
	if(id!=null){
		gClsId = id;
	}
	reloadFrame('openListFrm', './listClsFrm.do?menuId=${menuId}&clsPid='+id);
};

//-->
</script>

<u:title titleId="dm.jsp.clsMgm.title" alt="분류체계관리" menuNameFirst="true" />

<!-- LEFT -->
<div style="float:left; width:27.8%;">

<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?" var="msg" />

<u:title titleId="cols.cls" type="small" alt="분류" />

<u:titleArea frameId="clsTree" frameSrc="./treeClsFrm.do?menuId=${menuId}"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
</div>

	
<!-- RIGHT -->
<div style="float:right; width:71%;">

<u:title titleId="dm.jsp.list.cls.title" type="small" alt="분류목록" >
	<u:titleIcon type="up" onclick="moveUp()" id="rightUp" auth="A" />
	<u:titleIcon type="down" onclick="moveDown()" id="rightDown" auth="A" />
</u:title>

<u:titleArea frameId="openListFrm" frameSrc="/cm/util/reloadable.do"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
	<u:buttonArea>
		<u:button href="javascript:addRow();" id="btnAddRow" titleId="cm.btn.plus" alt="행추가" auth="A" />
		<u:button href="javascript:delRow();" id="btnDelRow" titleId="cm.btn.minus" alt="행삭제" auth="A" />
		<u:button href="javascript:delSelRow();" id="btnDelSel" titleId="cm.btn.selDel" alt="선택삭제" auth="A" />
		<u:button href="javascript:saveCls();" id="btnSave" titleId="cm.btn.save" alt="저장" auth="A" />
	</u:buttonArea>
	
</div>

<u:blank />

