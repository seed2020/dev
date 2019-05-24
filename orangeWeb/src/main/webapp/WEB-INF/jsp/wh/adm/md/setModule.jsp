<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--<%// [왼쪽버튼] - 엑셀업로드 %>
function setExcelUploadPop(){
	dialog.open('setExcelUploadDialog', '<u:msg titleId="wh.jsp.listMd.excel" alt="엑셀업로드" />', './setExcelUploadPop.do?menuId=${menuId}');
}<%// 선택된 조직 %>
var gMdId = null;
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
	if(frmId=='mdTree'){
		getIframeContent(frmId).move(direction);
	} else {
		var src = getIframeContent('mdList').location.href;
		if(src==null || src.indexOf('listMdFrm.do')<0){
			alertMsg("pt.jsp.setMnu.msg3");<%//pt.jsp.setMnu.msg3=왼쪽 '메뉴 트리 구성'의 항목을 선택 후 사용해 주십시요.%>
		} else {
			getIframeContent(frmId).move(direction);
		}
	}
}
<%// [우하단 버튼] 행추가%>
function addRow(){
	if(gMdId==null){
		alertMsg("or.jsp.setOrg.msg1");<%//dm.jsp.setMd.msg1=왼쪽 '모듈'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('mdList').addRow();
	}
}
<%// [우하단 버튼] 행삭제 %>
function delRow(){
	if(gMdId==null){
		alertMsg("or.jsp.setOrg.msg1");<%//dm.jsp.setMd.msg1=왼쪽 '모듈'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('mdList').delRow();
	}
}
<%// [우하단 버튼] 선택삭제 %>
function delSelRow(){
	if(gMdId==null){
		alertMsg("or.jsp.setOrg.msg1");<%//dm.jsp.setMd.msg1=왼쪽 '모듈'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('mdList').delSelRow();
	}
}
<%// [우하단 버튼] 저장 %>
function saveMd(){
	if(gMdId==null){
		alertMsg("or.jsp.setOrg.msg1");<%//dm.jsp.setMd.msg1=왼쪽 '모듈'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('mdList').saveMd();
	}
}<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openListFrm(id, isFld){
	if(id!=null){
		gMdId = id;
	}
	if(isFld){<%// 오른쪽 - 목록 열기 %>
		$("#mdList").attr('src', './listMdFrm.do?menuId=${menuId}&mdPid='+id);
		//getIframeContent('mdList').reload('./listMdFrm.do?menuId=${menuId}&mdPid='+id);
		$("#btnAddRow, #btnDelRow, #btnDelSel").show();
	} else {<%// 오른쪽 - 상세정보 열기 %>
		$("#mdList").attr('src', './setMdFrm.do?menuId=${menuId}&mdId='+id);
		//getIframeContent('mdList').reload('./setMdFrm.do?menuId=${menuId}&mdId='+id);
		$("#btnAddRow, #btnDelRow, #btnDelSel").hide();
	}
}<%// [버튼]모듈 이동 %>
function moveMd() {
	var selId = getSelectId();
	if(selId == null) return;
	var url = './findMdPop.do?menuId=${menuId}&lstTyp=F&mode=move';	
	dialog.open('findMdPop', '<u:msg titleId="dm.cols.listTyp.fld" alt="모듈보기" />', url);
}<%// 모듈 이동 저장 %>
function setMdInfos(arr, lstTyp){
	if(arr == undefined || arr.length == 0) return;
	var selId = getSelectId();
	if(selId == null) return;
	
	if(selId == arr[0].id){
		alertMsg("dm.msg.not.save.duplMd");<%//// dm.msg.not.save.duplMd=동일한 모듈로 저장할수 없습니다.%>
		return;
	}
	// 부모모듈ID
	var fldPid = arr[0].id;
	callAjax('./transMdMoveAjx.do?menuId=${menuId}', {fldPid:fldPid, fldId:selId}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			reloadFrame('mdTree', './treeMdFrm.do?menuId=${menuId}&fldId='+fldPid);
		}
		dialog.close('findMdPop');
	});
}<%// [팝업:저장 후처리] - 트리 리로드 %>
function reloadTree(mdId){
	getIframeContent('mdTree').reload('./treeMdFrm.do?menuId=${menuId}&mdId='+mdId);
};<%// [좌하단버튼] - 등록|수정 %>
function setMdPop(mod){
	var treeData=getIframeContent('mdTree').getTreeData();
	var isMod=mod!=undefined && mod;
	if(isMod && treeData['id']=='ROOT'){
		alertMsg("cm.msg.mod.root");<%//cm.msg.mod.root=최상위 항목은 수정 할 수 없습니다.%>
		return;
	}
	if(treeData['mdTypCd']=='W'){
		alertMsg("wh.jsp.not.md.mod");<%//wh.jsp.not.md.mod=모듈은 오른쪽 상세설정에서 수정해 주십시요.%>
		return;
	}
	var url = './setMdPop.do?menuId=${menuId}';
	if(isMod)
		url+='&mdId='+treeData['mdId'];
	
	if(!isMod && treeData['id']!='ROOT')
		url+='&mdPid='+treeData['mdId'];
		
	dialog.open('setMdDialog', '<u:msg titleId="wh.jsp.sysMd.title" alt="시스템모듈" />', url);	
	
}<%// [좌하단버튼] - 삭제 %>
function delMd(){
	getIframeContent('mdTree').delMd();
}<%// [담당자그룹] - 추가 %>
function setRowGrp(arr){
	getIframeContent('mdList').setRowGrp(arr);
}
//-->
</script>

<u:title titleId="wh.jsp.sysMd.title" alt="시스템모듈" menuNameFirst="true" />

<!-- LEFT -->
<div style="float:left; width:27.8%;">

<u:title titleId="wh.jsp.md.title" type="small" alt="모듈" >
	<u:buttonS href="javascript:setExcelUploadPop();" titleId="wh.jsp.listMd.excel" alt="엑셀업로드" auth="A" />
	<u:titleIcon type="up" onclick="moveUp('mdTree')" auth="A" />
	<u:titleIcon type="down" onclick="moveDown('mdTree')" auth="A" />
</u:title>

<u:titleArea frameId="mdTree" frameSrc="./treeMdFrm.do?menuId=${menuId}"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
	<u:buttonArea>
		<u:button href="javascript:setMdPop();" titleId="cm.btn.reg" alt="등록" auth="A" />
		<u:button href="javascript:setMdPop(true);" titleId="cm.btn.mod" alt="수정" auth="A" />
		<u:button href="javascript:delMd();" titleId="cm.btn.del" alt="삭제" auth="A" />
	</u:buttonArea>
</div>

	
<!-- RIGHT -->
<div style="float:right; width:71%;">

<u:title titleId="wh.jsp.set.mdDtl.title" type="small" alt="모듈상세설정" >
	<u:titleIcon type="up" onclick="moveUp('mdList')" auth="A" />
	<u:titleIcon type="down" onclick="moveDown('mdList')" auth="A" />
</u:title>

<u:titleArea frameId="mdList" frameSrc="/cm/util/reloadable.do"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
	<u:buttonArea>
		<u:button href="javascript:addRow();" id="btnAddRow" titleId="cm.btn.plus" alt="행추가" auth="A" />
		<u:button href="javascript:delRow();" id="btnDelRow" titleId="cm.btn.minus" alt="행삭제" auth="A" />
		<u:button href="javascript:delSelRow();" id="btnDelSel" titleId="cm.btn.selDel" alt="선택삭제" auth="A" />
		<u:button href="javascript:saveMd();" id="btnSave" titleId="cm.btn.save" alt="저장" auth="A" />
	</u:buttonArea>
	
</div>

<u:blank />

