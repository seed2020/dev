<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var gEmptyRight = true;
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
	if(frmId=='mnuTree'){
		getIframeContent(frmId).move(direction);
	} else {
		var src = getIframeContent('mnuList').location.href;
		if(src==null || src.indexOf('listMnuFrm.do')<0){
			alertMsg("pt.jsp.setMnu.msg3");<%//pt.jsp.setMnu.msg3=왼쪽 '메뉴 트리 구성'의 항목을 선택 후 사용해 주십시요.%>
		} else {
			getIframeContent(frmId).move(direction);
		}
	}
}
<%// [버튼] 폴더 - 등록/수정/삭제 %>
function mngFld(mode){
	if(mode=='del'){
		getIframeContent('mnuTree').delFld();
	} else {
		var tree = getIframeContent('mnuTree').getTreeData(mode);
		if(tree==null){
			alertMsg('cm.msg.noSelect');<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
		} else if(tree.fldYn=='N') {
			if(mode=='reg'){
				alertMsg('pt.jsp.setMnu.msg1');<%//pt.jsp.setMnu.msg1=메뉴에는 하위 폴더를 등록 할 수 없습니다.%>
			} else if(mode=='mod'){
				alertMsg('pt.jsp.setMnu.msg2');<%//pt.jsp.setMnu.msg2=메뉴는 오른쪽 상세설정에서 수정해 주십시요.%>
			}
		} else {
			var popTitle = (mode=='reg') ? '<u:msg titleId="pt.jsp.setCd.regFld" alt="폴더등록"/>' : '<u:msg titleId="pt.jsp.setCd.modFld" alt="폴더수정"/>';
			var url = './setFldPop.do?menuId=${menuId}${mnuGrpMdCdParam}&mnuGrpId='+tree.mnuGrpId;
			url += '&'+((mode=='reg') ? 'mnuPid' : 'mnuId')+'='+tree.mnuId;
			dialog.open('setFldDialog', popTitle, url);
		}
		
	}
}
<%// [팝업:폴더등록, 폴더수정] - 저장 버튼 %>
function saveFld(){
	if(validator.validate('setFldPop')){
		var $frm = $('#setFldPop');
		$frm.attr('action','./transFld.do');
		$frm.attr('target','dataframe');
		$frm.submit();
	}
}
<%// [팝업:저장 후처리] - 트리 리로드 %>
function reloadTree(mnuPid, mnuId){
	getIframeContent('mnuTree').reload('./treeMnuFrm.do?menuId=${menuId}&mnuGrpId=${param.mnuGrpId}&mnuPid='+mnuPid+(mnuId=='' ? '' : '&mnuId='+mnuId));
	dialog.close('setFldDialog');
}
<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openMnu(id, fldYn){
	if(fldYn=='Y'){<%// 오른쪽 - 목록 열기 %>
		getIframeContent('mnuList').reload('./listMnuFrm.do?menuId=${menuId}${mnuGrpMdCdParam}&mnuGrpId=${param.mnuGrpId}&mnuPid='+id);
		$("#btnAddRow, #btnDelRow, #btnDelSel").show();
	} else {<%// 오른쪽 - 상세정보 열기 %>
		getIframeContent('mnuList').reload('./setMnuFrm.do?menuId=${menuId}${mnuGrpMdCdParam}&mnuGrpId=${param.mnuGrpId}&mnuId='+id);
		$("#btnAddRow, #btnDelRow, #btnDelSel").hide();
	}
}
<%// [우버튼] 행추가%>
function addRow(){
	var src = getIframeContent('mnuList').location.href;
	if(src==null || src.indexOf('listMnuFrm.do')<0){
		alertMsg("pt.jsp.setMnu.msg3");<%//pt.jsp.setMnu.msg3=왼쪽 '메뉴 트리 구성'의 항목을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('mnuList').addRow();
	}
}
<%// [우버튼] 행삭제%>
function delRow(){
	var src = getIframeContent('mnuList').location.href;
	if(src==null || src.indexOf('listMnuFrm.do')<0){
		alertMsg("pt.jsp.setMnu.msg3");<%//pt.jsp.setMnu.msg3=왼쪽 '메뉴 트리 구성'의 항목을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('mnuList').delRow();
	}
}
<%// [우버튼] 선택삭제%>
function delSelRow(){
	var src = getIframeContent('mnuList').location.href;
	if(src==null || src.indexOf('listMnuFrm.do')<0){
		alertMsg("pt.jsp.setMnu.msg3");<%//pt.jsp.setMnu.msg3=왼쪽 '메뉴 트리 구성'의 항목을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('mnuList').delSelRow();
	}
}
<%// [우버튼] 저장%>
function saveMnu(){
	var src = getIframeContent('mnuList').location.href;
	if(src==null || (src.indexOf('listMnuFrm.do')<0 && src.indexOf('setMnuFrm.do')<0)){
		alertMsg("pt.jsp.setMnu.msg3");<%//pt.jsp.setMnu.msg3=왼쪽 '메뉴 트리 구성'의 항목을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('mnuList').saveMnu();
	}
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title alt="관리자/사용자/모바일 메뉴그룹 관리" menuNameFirst="true" />

<!-- LEFT -->
<div class="left" style="float:left; width:23.5%;">

<u:title titleId="pt.jsp.setMnu.mnuTreeTitle" alt="메뉴 트리 구성" type="small" >
	<u:titleIcon type="up" href="javascript:moveUp('mnuTree')" auth="A" />
	<u:titleIcon type="down" href="javascript:moveDown('mnuTree')" auth="A" />
</u:title>

<u:titleArea frameId="mnuTree"
	outerStyle="height:580px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameSrc="./treeMnuFrm.do?menuId=${menuId}&mnuGrpId=${param.mnuGrpId}${mnuGrpMdCdParam}"
	frameStyle="width:100%; height:570px;" />
<c:if test="${mnuGrpMdCd != 'M'}">
<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:mngFld('reg');" titleId="cm.btn.reg" alt="등록" auth="A" popYn="Y" />
	<u:button href="javascript:mngFld('mod');" titleId="cm.btn.mod" alt="수정" auth="A" popYn="Y" />
	<u:button href="javascript:mngFld('del');" titleId="cm.btn.del" alt="삭제" auth="A" />
</u:buttonArea></c:if>
</div>

<!-- RIGHT -->
<div class="right" style="float:right; width:75%;">

<u:title titleId="pt.jsp.setMnu.mnuListTitle" type="small" alt="메뉴 상세 설정">
	<u:titleIcon type="up" href="javascript:moveUp('mnuList')" auth="A" />
	<u:titleIcon type="down" href="javascript:moveDown('mnuList')" auth="A" />
</u:title>

<u:titleArea frameId="mnuList"
	outerStyle="height:580px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameSrc="/cm/util/reloadable.do" frameStyle="width:100%; height:570px;" />

<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:addRow();" id="btnAddRow" titleId="cm.btn.plus" alt="행추가" auth="A" />
	<u:button href="javascript:delRow();" id="btnDelRow" titleId="cm.btn.minus" alt="행삭제" auth="A" />
	<u:button href="javascript:delSelRow();" id="btnDelSel" titleId="cm.btn.selDel" alt="선택삭제" auth="A" />
	<u:button href="javascript:saveMnu();" titleId="cm.btn.save" alt="저장" auth="A" />
	<u:button href="javascript:history.go(-1);" titleId="cm.btn.cancel" alt="취소" />
</u:buttonArea>

</div>