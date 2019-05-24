<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--<% // [하단버튼:설정] %>
function setEnvPop(){
	dialog.open('setEnvDialog','<u:msg titleId="cm.option.config" alt="설정" />','./setEnvPop.do?menuId=${menuId}');
}
<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	getIframeContent('listRescMngFrm').excelDownFile();
};

//자원종류 선택시 오른쪽 프레임 조회
function fnRescMngSearch(rescKndId){
	var typ = getIframeContent('listRescMngFrm').getTyp();
	$("#listRescMngFrm").attr('src', './listRescMngFrm.do?menuId=${menuId}&typ='+typ+'&rescKndId='+rescKndId);
};

//자원종류 프레임 리로드
function reloadRescKnd(areaId , listPage){
	reloadPage('listRescKnd' , listPage,'setRescKndPop');
};

//자원관리 프레임 리로드
function reloadRescMng(areaId , listPage){
	reloadPage('listRescMngFrm' , listPage,'setRescPop');
};

//페이지 리로딩
function reloadPage( areaId , listPage , popId){
	getIframeContent(areaId).reload(listPage);
	dialog.close(popId);
};

//상세보기
function viewRescMngPop(rescMngId , params) {
	var url = './viewRescMngPop.do?menuId=${menuId}&rescMngId='+rescMngId;
	if(params!=undefined) url+='&'+params;
	dialog.open('setRescPop', '<u:msg titleId="wb.jsp.viewRescMngPop.title" alt="자원상세보기" />', url);
};

//등록 수정 팝업[자원종류]
function setRescKndPop(rescKndId , params){
	var url = './setRescKndPop.do?'+params;
	var popTitle = '<u:msg titleId="wr.btn.rescKndReg" alt="자원종류등록"/>';
	if(rescKndId != null){
		url+= '&rescKndId='+rescKndId;
		popTitle = '<u:msg titleId="wr.btn.rescKndMod" alt="자원종류수정"/>';
	}
	dialog.open('setRescKndPop',popTitle,url);
};

//등록 수정 팝업[자원관리]
function setRescPop(rescMngId , params) {
	var url = './setRescMngPop.do?menuId=${menuId}';
	var popTitle = '<u:msg titleId="wb.jsp.regRescMngPop.title" alt="자원등록" />';
	if(rescMngId != null){
		url+= '&rescMngId='+rescMngId;
		popTitle = '<u:msg titleId="wb.jsp.modRescMngPop.title" alt="자원수정" />';
	}
	if(params!=undefined) url+='&'+params;
	dialog.open('setRescPop',popTitle,url);
};

//삭제
function fnRescDelete(rescMngId){
	if(rescMngId == null ) return;
	$('#deleteForm #rescMngId').val(rescMngId);
	if(confirmMsg("wr.cfrm.rescMng.del")) {
		var $form = $('#deleteForm');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();	
	}
};
<%// [아이콘] 위로이동 %>
function moveUp(id){
	moveDirection('up', id);
}
<%// [아이콘] 아래로이동 %>
function moveDown(id){
	moveDirection('down', id);
}
<%// [아이콘] 위/아래로 이동 %>
function moveDirection(direction, id){
	var contWin = getIframeContent(id);
	contWin.move(direction);
}
<% // 순서 변경 버튼 보이기 %>
function loadMoveBtn(display){
	if(display=='show'){
		$('#moveSaveBtn').show();
		$('#moveUpBtn').show();
		$('#moveDownBtn').show();
	}else{
		$('#moveSaveBtn').hide();
		$('#moveUpBtn').hide();
		$('#moveDownBtn').hide();
	}
}

<% // 순서 저장 %>
function moveSave(id) {
	getIframeContent(id).moveSave();
};

$(document).ready(function() {
setUniformCSS();
});
//-->
</script>

<!-- LEFT -->
<div style="float:left; width:27.8%;">
<u:title titleId="wr.jsp.listRescKnd.title" type="small" alt="자원종류관리" menuNameFirst="true">
	<u:titleButton titleId="cm.btn.reg" alt="등록" href="javascript:;" onclick="setRescKndPop(null,'menuId=${menuId }');" auth="A"/>
	<u:buttonS alt="순서저장" onclick="moveSave('listRescKnd');" titleId="bb.btn.ordr.save" auth="A" />
	<u:titleIcon type="up" onclick="moveUp('listRescKnd')" id="rightUp" auth="A" />
	<u:titleIcon type="down" onclick="moveDown('listRescKnd')" id="rightDown" auth="A" />
</u:title>

<u:titleArea frameId="listRescKnd" frameSrc="./listRescKndFrm.do?menuId=${menuId}"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
</div>

	
<!-- RIGHT -->
<div style="float:right; width:71%;">
<u:title titleId="wr.jsp.listResc.title" type="small" alt="자원현황" menuNameFirst="true" >
	<u:titleButton titleId="cm.btn.excelDown" alt="엑셀다운" href="javascript:;" onclick="excelDownFile();" auth="R"/>
	<u:titleButton titleId="cm.btn.reg" alt="등록" href="javascript:;" onclick="setRescPop(null,'menuId=${menuId }&typ='+getIframeContent('listRescMngFrm').getTyp());" auth="A"/>
	<u:buttonS alt="순서저장" onclick="moveSave('listRescMngFrm');" titleId="bb.btn.ordr.save" auth="A" id="moveSaveBtn" style="display:none;"/>
	<u:titleIcon type="up" onclick="moveUp('listRescMngFrm')" id="moveUpBtn" auth="A" style="display:none;" />
	<u:titleIcon type="down" onclick="moveDown('listRescMngFrm')" id="moveDownBtn" auth="A" style="display:none;" />
</u:title>

<u:titleArea frameId="listRescMngFrm" frameSrc="./listRescMngFrm.do?menuId=${menuId}&typ=I"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
</div>
<% // 하단 버튼 %>
<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.setup" alt="설정" href="javascript:setEnvPop();" auth="A" />
</u:buttonArea>