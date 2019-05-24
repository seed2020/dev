<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<u:set test="${not empty param.clsCd}" var="emptyRight" value="false" elseValue="true" />
var gEmptyRight = ${emptyRight};
<%// [아이콘] 위로이동 %>
function moveUp(frmId){
	if(gEmptyRight && frmId=='cdList'){
		alertMsg("pt.jsp.setCd.msg1");<%//pt.jsp.setCd.msg1=왼쪽 '코드 트리 구성'의 항목을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent(frmId).move('up');
	}
}
<%// [아이콘] 아래로이동 %>
function moveDown(frmId){
	if(gEmptyRight && frmId=='cdList'){
		alertMsg("pt.jsp.setCd.msg1");<%//pt.jsp.setCd.msg1=왼쪽 '코드 트리 구성'의 항목을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent(frmId).move('down');
	}
}
<%// [버튼] 폴더삭제 %>
function delFld(){
	getIframeContent('cdTree').delFld();
}
<%// [버튼] 폴더등록 %>
function regFld(){
	var tree = getIframeContent('cdTree').getTreeData('reg');
	if(tree!=null){<%//pt.jsp.setCd.regFld=폴더등록%>
		dialog.open('setFldDialog','<u:msg titleId="pt.jsp.setCd.regFld"/>','./setFldPop.do?menuId=${menuId}&mode=reg&clsCd='+tree.id);
	}
}
<%// [버튼] 폴더수정 %>
function modFld(){<%//pt.jsp.setCd.modFld=폴더수정%>
	var tree = getIframeContent('cdTree').getTreeData('mod');
	if(tree!=null){
		dialog.open('setFldDialog','<u:msg titleId="pt.jsp.setCd.modFld"/>','./setFldPop.do?menuId=${menuId}&mode=mod&clsCd='+tree.pid+'&cd='+tree.id);
	}
}
<%// [팝업:어권 변경] 폴더명 어권 변경 - select
/* commonEx.js 로 이관
function changeLangTypCd(areaId, langAreaId, langCd){
	$('#'+areaId+' #'+langAreaId+' input').each(function(){
		var rescNm = $(this).attr('name');
		if(rescNm.endsWith(langCd)){
			$(this).show(); $(this).focus();
		} else { $(this).hide(); }
	});
} */
%>
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
function reloadTree(clsCd, cd){
	getIframeContent('cdTree').reload('./treeCdFrm.do?menuId=${menuId}&clsCd='+clsCd+(cd=='' ? '' : '&cd='+cd));
	dialog.close('setFldDialog');
}
<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openCdList(id){
	if(id=='ROOT'){
		$("#cdList").attr('src', 'about:blank');
		gEmptyRight = true;
	} else {
		$("#cdList").attr('src', './listCdFrm.do?menuId=${menuId}&clsCd='+id);
		gEmptyRight = false;
	}
}
<%// [우버튼] 행추가%>
function addRow(){
	if(gEmptyRight){
		alertMsg("pt.jsp.setCd.msg1");<%//pt.jsp.setCd.msg1=왼쪽 '코드 트리 구성'의 항목을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('cdList').addRow();
	}
}
<%// [우버튼] 행삭제%>
function delRow(){
	if(gEmptyRight){
		alertMsg("pt.jsp.setCd.msg1");<%//pt.jsp.setCd.msg1=왼쪽 '코드 트리 구성'의 항목을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('cdList').delRow();
	}
}
<%// [우버튼] 선택삭제%>
function delSelRow(){
	if(gEmptyRight){
		alertMsg("pt.jsp.setCd.msg1");<%//pt.jsp.setCd.msg1=왼쪽 '코드 트리 구성'의 항목을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('cdList').delSelRow();
	}
}
<%// [우버튼] 저장%>
function saveCd(){
	if(gEmptyRight){
		alertMsg("pt.jsp.setCd.msg1");<%//pt.jsp.setCd.msg1=왼쪽 '코드 트리 구성'의 항목을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('cdList').saveCd();
	}
}
<%// 코드 저장후 되돌아갈 페이지 - 파라미터로 넘겨옴 %>
<c:if test="${not empty param.backTo}">
function goBack(){
	location.replace("${param.backTo}");
}
</c:if>
<%// 관리 범위 설정%>
function setMngCompSetting(){
	dialog.open('setMngCompDialog','<u:msg titleId="pt.jsp.listMnuGrp.mngCompSet" alt="관리 범위 설정" />','./setMngCompPop.do?menuId=${menuId}&setupId=${setupId}');
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//openCdList
</script>
<u:title titleId="pt.jsp.setCd.title" alt="코드 관리" menuNameFirst="true" />

<!-- LEFT -->
<div class="left" style="float:left; width:23.5%;">

<u:title titleId="pt.jsp.setCd.cdTreeTitle" type="small" alt="코드 트리 구성">
	<u:titleIcon type="up" href="javascript:moveUp('cdTree')" auth="${cdEditAuth}" />
	<u:titleIcon type="down" href="javascript:moveDown('cdTree')" auth="${cdEditAuth}" />
</u:title>
<u:set test="${not empty param.clsCd}" var="frameSrc" value="./treeCdFrm.do?menuId=${menuId}&clsCd=${param.clsCd}" elseValue="./treeCdFrm.do?menuId=${menuId}" />
<u:titleArea frameId="cdTree" frameSrc="${frameSrc}"
	outerStyle="height:580px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:570px;" />

<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:regFld()" titleId="cm.btn.reg" alt="등록" auth="${cdEditAuth}" popYn="Y" />
	<u:button href="javascript:modFld()" titleId="cm.btn.mod" alt="수정" auth="${cdEditAuth}" popYn="Y" />
	<u:button href="javascript:delFld()" titleId="cm.btn.del" alt="삭제" auth="${cdEditAuth}" />
</u:buttonArea>
</div>

<!-- RIGHT -->
<div class="right" style="float:right; width:75%;">

<u:title titleId="pt.jsp.setCd.cdListTitle" type="small" alt="코드 상세 설정">
	<u:titleIcon type="up" href="javascript:moveUp('cdList')" auth="${cdEditAuth}" />
	<u:titleIcon type="down" href="javascript:moveDown('cdList')" auth="${cdEditAuth}" />
</u:title>

<u:set test="${not empty param.clsCd}" var="frameSrc" value="./listCdFrm.do?menuId=${menuId}&clsCd=${param.clsCd}" elseValue="about:blank" />
<u:titleArea frameId="cdList" frameSrc="${frameSrc}"
	outerStyle="height:580px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:570px;" />

<u:buttonArea noBottomBlank="true">
	<u:button titleId="pt.jsp.listMnuGrp.mngCompSet" alt="관리 범위 설정" href="javascript:setMngCompSetting();" auth="SYS" popYn="Y" />
	<u:button href="javascript:addRow()" titleId="cm.btn.plus" alt="행추가" auth="${cdEditAuth}" />
	<u:button href="javascript:delRow()" titleId="cm.btn.minus" alt="행삭제" auth="${cdEditAuth}" />
	<u:button href="javascript:delSelRow()" titleId="cm.btn.selDel" alt="선택삭제" auth="${cdEditAuth}" />
	<u:button href="javascript:saveCd()" titleId="cm.btn.save" alt="저장" auth="${cdEditAuth}" />
	<c:if test="${not empty param.backTo}"><u:button href="javascript:goBack()" titleId="cm.btn.cancel" alt="취소" /></c:if>
</u:buttonArea>

</div>