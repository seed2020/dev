<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="nonParams" excludes="meetNo,pageNo"/>
<script type="text/javascript">
<!--
<%// 선택된 탭 %>
var clkId = null;
<%// 선택된 탭코드 체크 %>
function isRootChk(){
	if(clkId == null || clkId == 'ROOT') return true;
	return false;
}<%// 선택된 탭코드 체크 %>
function isRootChkMsg(){
	if(isRootChk()) {
		//alertMsg("dm.jsp.setDoc.not.${msgId}");<%//dm.jsp.setFld.msg1=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
		return true;
	}
	return false;
}
<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openListFrm(id){
	if(id!=null){
		clkId = id;
		if(isRootChk()) return;
	}else{
		id = clkId;
	}
	reloadFrame('openListFrm', './listMeetFrm.do?${nonParams}&orgId='+id);
	//applyDocBtn('list');
};<%// [트리] %>
function openOrgTree(){
	reloadFrame('leftPage', './treeOrgFrm.do?menuId=${menuId}');
};<%// 버튼 보이기 조절 - displayBtn 함수 이용  id배열 넘겨줌 %>
function applyDocBtn(action, param){
	var $area = $("#rightBtnArea");<%// lstTyp : 폴더ID, 분류ID %>
	$area.find('ul>li').remove();
	var btnHtml = getIframeContent('openListFrm').getRightBtnList();
	if(btnHtml != '')
		$area.append(btnHtml);
}
<%// [우하단 버튼] 등록 %>
function setMeet(){
	if(!isRootChkMsg())
		getIframeContent('openListFrm').setMeet();
}

<%// [화살표<< >>버튼] - 우측 목록 전체화면 %>
function pageToggle(typ){
	if(typ=='close'){
		$('#leftArea').hide();
		$('#rightArea').css('width', '98%');
		$('#toggleBtnArea a#closeBtn').hide();
		$('#toggleBtnArea a#openBtn').show();
	}else if(typ=='open'){
		$('#leftArea').show();
		$('#rightArea').css('width', '77%');
		$('#toggleBtnArea a#closeBtn').show();
		$('#toggleBtnArea a#openBtn').hide();
	}
	$('#searchForm1, #searchForm2').find('input[id="pageClose"]').val(typ);
}
var gHideDelDeptFlag = false;<%
//삭제 부서 숨김 %>
function hideDelDept(){
	gHideDelDeptFlag = !gHideDelDeptFlag;
	getIframeContent('leftPage').hideDelTree(gHideDelDeptFlag);
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<u:title titleId="dm.jsp.search.doc.title" alt="문서조회" menuNameFirst="true" />
<jsp:include page="/WEB-INF/jsp/cu/dyne/csmt/meet/listMeetSrch.jsp" />

<u:set var="leftDisplay" test="${!empty param.pageClose && param.pageClose eq 'close'}" value="display:none;"/>
<!-- LEFT -->
<div style="float:left; width:20.8%;${leftDisplay}" id="leftArea">

<u:titleArea frameId="leftPage" frameSrc="./treeOrgFrm.do?menuId=${menuId}"
	outerStyle="height:510px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:500px;"/>
	<c:if test="${isAdmin == true}">
	<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:hideDelDept();" id="btnHideDelDept" titleId="or.btn.hideDelDept" alt="삭제 부서 숨김" auth="A" />
</u:buttonArea>
</c:if>
</div>

<div id="toggleBtnArea" style="float:left; width:2%; text-align:center; margin:250px 0 0 0;">
	<a id="openBtn" href="javascript:pageToggle('open');"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" /> <c:if test="${empty param.pageClose || param.pageClose eq 'open'}">style="display:none;"</c:if>><img src="${_cxPth}/images/${_skin}/arrowr.png" width="15" height="17" /></a>
	<a id="closeBtn" href="javascript:pageToggle('close');"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" /> <c:if test="${!empty param.pageClose && param.pageClose eq 'close'}">style="display:none;"</c:if>><img src="${_cxPth}/images/${_skin}/arrowl.png" width="15" height="17" /></a>
</div>

<u:set var="rightDisplay" test="${!empty param.pageClose && param.pageClose eq 'close'}" value="width:98%;" elseValue="width:77%;"/>
	
<!-- RIGHT -->
<div style="float:right;${rightDisplay}" id="rightArea">

<u:titleArea frameId="openListFrm" frameSrc="/cm/util/reloadable.do"
	outerStyle="height:510px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:500px;"/>
<u:buttonArea id="rightBtnArea" noBottomBlank="true">
	<u:button id="btnSetDoc" titleId="cm.btn.reg" alt="등록" href="javascript:setMeet();" auth="W" />
</u:buttonArea>
</div>