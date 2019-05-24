<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<%// 선택된 법인 %>
var gCorpNo=null;
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
	if(gCorpNo==null){
		alertMsg("");<%//='법인'를 선택 후 사용해 주십시요.%>
	} else {
		var contWin = getIframeContent('openCarListFrm');
		contWin.move(direction);
	}
}<%// [우하단 버튼] 행추가%>
function addRow(){
	if(gCorpNo==null){
		alertMsg("");<%//='법인'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openCarListFrm').addRow();
	}
}
<%// [우하단 버튼] 행삭제 %>
function delRow(){
	if(gCorpNo==null){
		alertMsg("");<%//='법인'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openCarListFrm').delRow();
	}
}
<%// [우하단 버튼] 선택삭제 %>
function delSelRow(){
	if(gCorpNo==null){
		alertMsg("");<%//='법인'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openCarListFrm').delSelRow();
	}
}<%// [우하단 버튼] 저장 %>
function saveCar(){
	if(gCorpNo==null){
		alertMsg("wa.msg.not.corp");<%//='법인'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openCarListFrm').saveCar();
	}
}
<%// [버튼] 법인관리 %>
function setCorpPop(){
	var url = './setCorpPop.do?menuId=${menuId}&fncMul=Y';
	dialog.open('setCatDialog', '<u:msg titleId="wa.btn.cat.mng" alt="법인관리" />', url);
}<%// [버튼] 이미지ID 저장 %>
function setTmpImgId(id){
	if(gCorpNo==null){
		alertMsg("");<%//='법인'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openCarListFrm').setTmpImgId(id);
	}
}<%// [버튼] 내용 저장 %>
function setCarCont(arr){
	if(gCorpNo==null){
		alertMsg("wa.msg.not.corp");<%//='법인'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openCarListFrm').setCarCont(arr);
	}
}<% //내용 가져오기 %>
function getCarCont(){
	if(gCorpNo==null){
		alertMsg("");<%//='법인'를 선택 후 사용해 주십시요.%>
	} else {
		return getIframeContent('openCarListFrm').getCarCont();
	}
}
<%// [리로드] - 프레임 %>
function reloadFrm(frmId, url, popCloseId){
	if(frmId!=undefined && frmId=='leftPage') reloadFrame('openCarListFrm', '/cm/util/reloadable.do');
	reloadFrame(frmId, url);
	if(popCloseId != undefined) dialog.close(popCloseId);
}<%// [사이트클릭] - 오른쪽 리스트 열기 %>
function openCarListFrm(id){
	if(id!=null){
		gCorpNo = id;
	}
	reloadFrame('openCarListFrm', '/wa/adm/car/listCarFrm.do?menuId=${menuId}&corpNo='+gCorpNo);
};

//-->
</script>

<u:title titleId="wa.title.car.mng" alt="업무용승용차관리" menuNameFirst="true" />

<!-- LEFT -->
<div style="float:left; width:27.8%;">
<u:title titleId="wa.jsp.corp.title" type="small" alt="법인" />
<u:titleArea frameId="leftPage" frameSrc="./listCorpFrm.do?menuId=${menuId}"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	<u:secu auth="A">
	<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:setCorpPop();" titleId="wa.btn.cat.mng" alt="법인관리" auth="A" popYn="Y" />	
</u:buttonArea>
</u:secu>
</div>

	
<!-- RIGHT -->
<div style="float:right; width:71%;">
<u:title titleId="wa.jsp.car.title" type="small" alt="승용차" >
	<u:titleIcon type="up" onclick="moveUp()" id="rightUp" auth="A" />
	<u:titleIcon type="down" onclick="moveDown()" id="rightDown" auth="A" />
</u:title>
<u:titleArea frameId="openCarListFrm" frameSrc="/cm/util/reloadable.do"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	<u:secu auth="A">
		<u:buttonArea>
		<u:button href="javascript:addRow();" titleId="cm.btn.plus" alt="행추가" auth="A" />
		<u:button href="javascript:delRow();" titleId="cm.btn.minus" alt="행삭제" auth="A" />
		<u:button href="javascript:delSelRow();" titleId="cm.btn.selDel" alt="선택삭제" auth="A" />
		<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveCar();" auth="A" />
	</u:buttonArea>
	</u:secu>
</div>

<u:blank />

