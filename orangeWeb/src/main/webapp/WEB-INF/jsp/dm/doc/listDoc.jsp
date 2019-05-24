<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="pageSuffix" test="${lstTyp eq 'L' }" value="" elseValue="Frm"/>
<u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/>
<u:set var="fldGrpIdQueryString" test="${!empty fldGrpId }" value="&fldGrpId=${fldGrpId }" elseValue=""/>
<c:if test="${lstTyp eq 'C' }">
<c:set var="paramNm" value="clsId" />
<c:set var="leftPage" value="treeDocClsFrm" />
<c:set var="msgId" value="cls" />
<u:params var="nonParams" excludes="docId,clsId,lstTyp,pageNo"/>
</c:if>
<c:if test="${lstTyp eq 'F' }">
<c:set var="paramNm" value="fldId" />
<c:set var="leftPage" value="treeDocFldFrm" />
<c:set var="msgId" value="fld"/>
<u:params var="nonParams" excludes="docId,fldId,lstTyp,pageNo"/>
</c:if>
<c:if test="${lstTyp eq 'B' }">
<c:set var="paramNm" value="bumkId" />
<c:set var="leftPage" value="listBumkFrm" />
<c:set var="msgId" value="bumk"/>
<u:params var="nonParams" excludes="docId,lstTyp,pageNo"/>
</c:if>
<c:set var="leftPageParam" value="&useYn=Y&initSelect=Y&${paramNm }=${param.paramNmId }${paramStorIdQueryString }${fldGrpIdQueryString }" />
<script type="text/javascript">
<!--
<%// 선택된 탭 %>
var clkId = null;
<%// 선택된 탭코드 체크 %>
function isRootChk(){
	if(clkId == null || ('${lstTyp}' == 'F' && clkId == 'ROOT')) return true;
	return false;
}<%// 선택된 탭코드 체크 %>
function isRootChkMsg(){
	if(isRootChk()) {
		alertMsg("dm.jsp.setDoc.not.${msgId}");<%//dm.jsp.setFld.msg1=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
		return true;
	}
	return false;
}<%// [팝업] 작업이력 %>
function docHst(srchCd){
	getIframeContent('openListFrm').docHst(srchCd);
};<% // [하단버튼:휴지통,완전삭제,복원] %>
function delDocList(statCd) {
	getIframeContent('openListFrm').delDocList(statCd);
}<%// [버튼] 즐겨찾기관리 %>
function setBumkPop(){
	var url = './setBumkPop.do?menuId=${menuId}&fncMul=Y&popYn=Y';
	dialog.open('setBumkPop', '<u:msg titleId="dm.btn.bumk.mng" alt="즐겨찾기관리" />', url);
}<%// [리로드] - 프레임 %>
function reloadFrm(frmId, url, popCloseId){
	reloadFrame(frmId, url);
	if(popCloseId != undefined && popCloseId != null) dialog.close(popCloseId);
}<%// 저장, 삭제시 리로드 - 오른쪽 리스트 열기 %>
function reloadDocFrm(url, dialogId){
	getIframeContent('openListFrm').reloadDocFrm(url, dialogId);
	//reloadFrame('openListFrm', page);
};<%// [팝업] 이메일 %>
function email(){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').email();
};<%// [저장] 옵션포함저장 - 인수 %>
function transTakovr(arrs){
	getIframeContent('openListFrm').transTakovr(arrs);
}<%// [버튼] 재인계 %>
function transReSave(){
	getIframeContent('openListFrm').transReSave();
}<%// [버튼] 선택인계 %>
function transTgtSave(){
	getIframeContent('openListFrm').transTgtSave();
}
<%// [버튼] 인계취소 %>
function transTgtCancel(){
	getIframeContent('openListFrm').transTgtCancel();
}<%// [버튼] 인수 - 승인 %>
function transTakovrApvd(){
	getIframeContent('openListFrm').transTakovrApvd();
}<%// [버튼] 인수 - 반려 %>
function transTakovrRjt(){
	getIframeContent('openListFrm').transTakovrRjt();
}<%// [팝업] 보내기 %>
function send(){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').send();
};<%// [팝업] 이동 %>
function move(){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').move();
};<% // [하단버튼:즐겨찾기] %>
function saveBumk(bumkId,mode){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').saveBumk(bumkId,mode);
}<% // [하단버튼:즐겨찾기] %>
function bumkDoc(){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').bumkDoc();
}<%// [우하단 버튼] 임시저장 %>
function tmpSave(){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').tmpSave();
}<%// 문서열람요청 %>
function saveRequest(param){
	getIframeContent('openListFrm').saveRequest(param);
}<%// [우하단 버튼] 목록 %>
function listDoc(){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').listDoc();
}<%// [우하단 버튼] 취소 %>
function cancelDoc(){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').cancelDoc();
}<%// [우하단 버튼] 하위문서등록 %>
function setSubDoc(){
	if(!isRootChkMsg())
		getIframeContent('openListFrm').setSubDoc();
}<% // 기본정보 수정 %>
function transferUpdate(arrs){
	if(!isRootChkMsg()) getIframeContent('openListFrm').transferUpdate(arrs);
}<% // [하단버튼:수정] %>
function setUpdate(){
	if(!isRootChkMsg()) getIframeContent('openListFrm').setUpdate();
}<%// [우하단 버튼] 등록 %>
function setDoc(){
	if(!isRootChkMsg())
		getIframeContent('openListFrm').setDoc();
}<%// [우하단 버튼] 삭제 %>
function delDoc(statCd){
	if(!isRootChkMsg())
		getIframeContent('openListFrm').delDoc(statCd);
}<%// [우하단 버튼] 삭제 %>
function delDocGrp(statCd){
	if(!isRootChkMsg())
		getIframeContent('openListFrm').delDocGrp(statCd);
}<% // [하단버튼:승인,반려] %>
function saveDisc(discStatCd) {
	if(!isRootChkMsg())
		getIframeContent('openListFrm').saveDisc(discStatCd);
}<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openListFrm(id){
	if(id!=null){
		clkId = id;
		if(isRootChk()) return;
	}else{
		id = clkId;
	}
	reloadFrame('openListFrm', './${listPage}Frm.do?${nonParams}&lstTyp=${lstTyp}${paramStorIdQueryString }&${paramNm}='+id);
	//applyDocBtn('list');
};<%// [트리] %>
function openDocTree(){
	reloadFrame('leftPage', './${leftPage }.do?menuId=${menuId}${leftPageParam }${paramStorIdQueryString }');
};<%// 버튼 보이기 조절 - displayBtn 함수 이용  id배열 넘겨줌 %>
function applyDocBtn(action, param){
	var $area = $("#rightBtnArea");<%// lstTyp : 폴더ID, 분류ID %>
	$area.find('ul>li').remove();
	var btnHtml = getIframeContent('openListFrm').getRightBtnList();
	if(btnHtml != '')
		$area.append(btnHtml);
}<%// 버튼 보이기 조절 - displayBtn 함수 이용  id배열 넘겨줌 %>
function applyDocBtnTmp(action, param){
	var $area = $("#rightBtnArea");<%// lstTyp : 폴더ID, 분류ID %>
	if(isRootChk()){<%// 폴더/분류 - ROOT의 경우 %>
		displayBtn($area, false, ["btnSetDoc","btnSave","btnCancel","btnMod","btnDel","btnList"]);
		return;
	} 
	// param.contains('update');
	if(action=="reg"){<%// 등록 %>
		displayBtn($area, false,  ["btnSetDoc","btnMod","btnDel","btnList"]);
		displayBtn($area, true,  ["btnSave","btnCancel"]);
	} else if(action=="view"){<%// 상세정보 %>
		displayBtn($area, false, ["btnSetDoc","btnSave","btnCancel"]);
		displayBtn($area, true,  ["btnMod","btnDel","btnList"]);
	} else if(action=="viewHst"){<%// 상세정보[버전] %>
		displayBtn($area, false, ["btnSetDoc","btnSave","btnMod","btnCancel"]);
		displayBtn($area, true,  ["btnDel","btnList"]);
	} else if(action=="list"){<%// 목록 %>
		displayBtn($area, false, ["btnSave","btnCancel","btnMod","btnDel","btnList"]);
		displayBtn($area, true,  ["btnSetDoc"]);
	} else {
		displayBtn($area, false, ["btnSetDoc","btnSave","btnCancel","btnMod","btnDel","btnList"]);
	}
}<%// 버튼 보이기 조절 %>
function displayBtn($area, showFlag, arr){
	arr.each(function(index, obj){
		if(showFlag){
			$area.find("#"+obj).show();
		} else {
			$area.find("#"+obj).hide();
		}
	});
}<% // 탭 클릭 - 목록:L/폴더:F/분류:C %>
function toggleTabBtn(lstTyp){
	var $form = $("#searchForm");
	$form.find("input[name='lstTyp']").remove();
	$form.appendHidden({name:'lstTyp',value:lstTyp});
	$form.submit();
}<%// [버튼] 저장 %>
function saveDoc(){
	if(isRootChk()){
		alertMsg("dm.jsp.setDoc.not.${msgId}");<%//dm.jsp.setFld.msg1=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').saveDoc();
	}
}<%// [버튼] 저장 %>
function saveContinue(){
	if(isRootChk()){
		alertMsg("dm.jsp.setDoc.not.${msgId}");<%//dm.jsp.setFld.msg1=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').saveContinue();
	}
}<%// [폴더,분류] 조회조건 세팅 %>
function setParamNmId(val){
	if(val != undefined){
		$('#searchForm1').find("input[name='paramNmId']").remove();
		$('#searchForm1').appendHidden({name:'paramNmId',value:val});
		$('#searchForm2').find("input[name='paramNmId']").remove();
		$('#searchForm2').appendHidden({name:'paramNmId',value:val});
	}
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
<c:if test="${empty storList }"><u:title titleId="dm.jsp.search.doc.title" alt="문서조회" menuNameFirst="true" /></c:if>
<jsp:include page="/WEB-INF/jsp/dm/doc/listDocSrch.jsp" />
<form id="searchForm" name="searchForm" action="./${listPage }.do" style="padding:10px;">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="lstTyp" value="${lstTyp}" />
<!-- 저장소ID -->
<c:if test="${!empty paramStorId }">
<u:input type="hidden" id="paramStorId" value="${paramStorId}" />
</c:if>
<c:if test="${!empty paramEntryList}">
<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
	<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
</c:forEach>
</c:if>
</form>
<c:if test="${fn:length(tabList) > 1}">
<u:tabGroup noBottomBlank="${true}">
	<c:forEach var="tab" items="${tabList }" varStatus="status">
		<u:tab alt="목록전체" titleId="dm.cols.listTyp.${tab[1] }"
		on="${lstTyp == tab[0]}"
		onclick="toggleTabBtn('${tab[0] }');" />
	</c:forEach>
</u:tabGroup>
<u:blank />
</c:if>
<!-- LEFT -->
<div style="float:left; width:17.8%;" id="leftContainer">

<u:titleArea frameId="leftPage" frameSrc="./${leftPage }.do?menuId=${menuId}${leftPageParam }"
	outerStyle="height:510px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:500px;"/>
	<c:if test="${lstTyp eq 'B' || (isAdmin == true && lstTyp eq 'F')}">
	<u:buttonArea noBottomBlank="true">
	<c:if test="${lstTyp eq 'B' }"><u:button href="javascript:setBumkPop();" titleId="dm.btn.bumk.mng" alt="즐겨찾기관리" auth="W" popYn="Y" /></c:if>
	<c:if test="${isAdmin == true && lstTyp eq 'F' }"><u:button href="javascript:hideDelDept();" id="btnHideDelDept" titleId="or.btn.hideDelDept" alt="삭제 부서 숨김" auth="A" /></c:if>
</u:buttonArea>
</c:if>
</div>

	
<!-- RIGHT -->
<div style="float:right; width:81%;" id="rContainer">

<u:titleArea frameId="openListFrm" frameSrc="/cm/util/reloadable.do"
	outerStyle="height:510px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:500px;"/>
<u:buttonArea id="rightBtnArea" noBottomBlank="true">
	<c:if test="${!empty authMap.setDoc }"><u:button id="btnSetDoc" titleId="cm.btn.reg" alt="등록" href="javascript:setDoc();" auth="W" /></c:if>
	<%-- <u:button id="btnMod" titleId="cm.btn.mod" alt="수정" href="javascript:setDoc();" auth="W" style="display:none;"/>
	<u:button id="btnDel" titleId="cm.btn.del" alt="삭제" href="javascript:delDoc();" auth="W" style="display:none;"/>
	<u:button id="btnSave" titleId="cm.btn.save" alt="저장" href="javascript:save();" auth="W" style="display:none;"/>
	<u:button id="btnCancel" titleId="cm.btn.cancel" alt="취소" href="javascript:cancelDoc();" style="display:none;"/>
	<u:button id="btnList" titleId="cm.btn.list" alt="목록" href="javascript:listDoc();" style="display:none;"/> --%>
</u:buttonArea>
</div>