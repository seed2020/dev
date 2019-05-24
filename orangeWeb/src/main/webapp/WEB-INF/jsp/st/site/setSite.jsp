<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<%// 선택된 카테고리 %>
var gCatId=null;
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
	if(gCatId==null){
		alertMsg("st.msg.not.cat");<%//st.msg.not.cat='카테고리'를 선택 후 사용해 주십시요.%>
	} else {
		var contWin = getIframeContent('openSiteListFrm');
		contWin.move(direction);
	}
}<%// [우하단 버튼] 행추가%>
function addRow(){
	if(gCatId==null){
		alertMsg("st.msg.not.cat");<%//st.msg.not.cat='카테고리'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openSiteListFrm').addRow();
	}
}
<%// [우하단 버튼] 행삭제 %>
function delRow(){
	if(gCatId==null){
		alertMsg("st.msg.not.cat");<%//st.msg.not.cat='카테고리'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openSiteListFrm').delRow();
	}
}
<%// [우하단 버튼] 선택삭제 %>
function delSelRow(){
	if(gCatId==null){
		alertMsg("st.msg.not.cat");<%//st.msg.not.cat='카테고리'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openSiteListFrm').delSelRow();
	}
}<%// [우하단 버튼] 저장 %>
function saveSite(){
	if(gCatId==null){
		alertMsg("st.msg.not.cat");<%//st.msg.not.cat='카테고리'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openSiteListFrm').saveSite();
	}
}
<%// [버튼] 카테고리관리 %>
function setCatPop(){
	var url = './setCatPop.do?menuId=${menuId}&fncMul=Y';
	dialog.open('setCatDialog', '<u:msg titleId="st.btn.cat.mng" alt="카테고리관리" />', url);
}<%// [버튼] 사이트 등록 수정 %>
function setSitePop(id){
	var url = './setSitePop.do?menuId=${menuId}';
	url+='&catId='+gCatId;
	if(id!=undefined) url+='&siteId='+id; 
	dialog.open('setSiteDialog', '<u:msg titleId="st.title.site.mng" alt="사이트관리" />', url);
}<%// [버튼] 이미지ID 저장 %>
function setTmpImgId(id){
	if(gCatId==null){
		alertMsg("st.msg.not.cat");<%//st.msg.not.cat='카테고리'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openSiteListFrm').setTmpImgId(id);
	}
}<%// [버튼] 내용 저장 %>
function setSiteCont(arr){
	if(gCatId==null){
		alertMsg("st.msg.not.cat");<%//st.msg.not.cat='카테고리'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openSiteListFrm').setSiteCont(arr);
	}
}<% //내용 가져오기 %>
function getSiteCont(){
	if(gCatId==null){
		alertMsg("st.msg.not.cat");<%//st.msg.not.cat='카테고리'를 선택 후 사용해 주십시요.%>
	} else {
		return getIframeContent('openSiteListFrm').getSiteCont();
	}
}
<%// [리로드] - 프레임 %>
function reloadFrm(frmId, url, popCloseId){
	if(frmId!=undefined && frmId=='leftPage') reloadFrame('openSiteListFrm', '/cm/util/reloadable.do');
	reloadFrame(frmId, url);
	if(popCloseId != undefined) dialog.close(popCloseId);
}<%// [사이트클릭] - 오른쪽 리스트 열기 %>
function openSiteListFrm(id){
	if(id!=null){
		gCatId = id;
	}
	reloadFrame('openSiteListFrm', '/st/adm/site/listSiteFrm.do?menuId=${menuId}&catId='+gCatId);
};

//-->
</script>

<u:title titleId="st.title.site.mng" alt="사이트관리" menuNameFirst="true" />

<!-- LEFT -->
<div style="float:left; width:27.8%;">
<u:title titleId="st.jsp.cat.title" type="small" alt="카테고리" />
<u:titleArea frameId="leftPage" frameSrc="./listCatFrm.do?menuId=${menuId}"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	<u:secu auth="A">
	<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:setCatPop();" titleId="st.btn.cat.mng" alt="카테고리관리" auth="A" popYn="Y" />	
</u:buttonArea>
</u:secu>
</div>

	
<!-- RIGHT -->
<div style="float:right; width:71%;">
<u:title titleId="st.jsp.site.title" type="small" alt="사이트" >
	<u:titleIcon type="up" onclick="moveUp()" id="rightUp" auth="A" />
	<u:titleIcon type="down" onclick="moveDown()" id="rightDown" auth="A" />
</u:title>
<u:titleArea frameId="openSiteListFrm" frameSrc="/cm/util/reloadable.do"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	<u:secu auth="A">
		<u:buttonArea>
		<u:button href="javascript:addRow();" titleId="cm.btn.plus" alt="행추가" auth="A" />
		<u:button href="javascript:delRow();" titleId="cm.btn.minus" alt="행삭제" auth="A" />
		<u:button href="javascript:delSelRow();" titleId="cm.btn.selDel" alt="선택삭제" auth="A" />
		<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveSite();" auth="A" />
	</u:buttonArea>
	</u:secu>
</div>

<u:blank />

