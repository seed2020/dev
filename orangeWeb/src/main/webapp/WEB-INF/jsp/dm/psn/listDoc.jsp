<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="pageSuffix" test="${lstTyp eq 'L' }" value="" elseValue="Frm"/>

<c:set var="paramNm" value="fldId" />
<c:set var="leftPage" value="treeFldFrm" />
<c:set var="msgId" value="fld"/>
<c:set var="leftPageParam" value="&useYn=Y&initSelect=Y&regrUid=${param.regrUid }&${paramNm }=${param.paramNmId }" />
<u:params var="nonParams" excludes="docId,clsId,fldId,lstTyp,pageNo"/>

<script type="text/javascript">
<!--
<%// 선택된 탭 %>
var clkId = null;
<%// 선택된 탭코드 체크 %>
function isRootChk(){
	return false;
}<%// 선택된 탭코드 체크 %>
function isRootChkMsg(){
	if(isRootChk()) {
		alertMsg("dm.jsp.setDoc.not.${msgId}");<%//dm.jsp.setFld.msg1=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
		return true;
	}
	return false;
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
};<%// [팝업] 보내기 %>
function send(){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').send();
};<%// [팝업] 이동 %>
function move(){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').move();
};<% // [하단버튼:즐겨찾기] %>
function bumkDoc(){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').bumkDoc();
}<%// [우하단 버튼] 임시저장 %>
function tmpSave(){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').tmpSave();
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
}<%// [우하단 버튼] 등록 %>
function setDoc(){
	if(!isRootChkMsg())
		getIframeContent('openListFrm').setDoc();
}<%// [우하단 버튼] 삭제 %>
function delDocList(){
	if(!isRootChkMsg())
		getIframeContent('openListFrm').delDocList();
}<%// [우하단 버튼] 삭제 %>
function delDoc(){
	if(!isRootChkMsg())
		getIframeContent('openListFrm').delDoc();
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
	reloadFrame('openListFrm', './${listPage}Frm.do?${nonParams}&lstTyp=${lstTyp}&${paramNm}='+id);
	//applyDocBtn('list');
};<%// [트리] %>
function openDocTree(){
	reloadFrame('leftPage', './${leftPage }.do?menuId=${menuId}${leftPageParam }');
};<%// 버튼 보이기 조절 - displayBtn 함수 이용  id배열 넘겨줌 %>
function applyDocBtn(action, param){
	var $area = $("#rightBtnArea");<%// lstTyp : 폴더ID, 분류ID %>
	$area.find('ul>li').remove();
	var btnHtml = getIframeContent('openListFrm').getRightBtnList();
	if(btnHtml != '')
		$area.append(btnHtml);
}<%// 버튼 보이기 조절 %>
function displayBtn($area, showFlag, arr){
	arr.each(function(index, obj){
		if(showFlag){
			$area.find("#"+obj).show();
		} else {
			$area.find("#"+obj).hide();
		}
	});
}<%
// 탭 클릭 - 목록:L/폴더:F/분류:C %>
function toggleTabBtn(lstTyp){
	var $form = $("#searchForm");
	$form.find("input[name='lstTyp']").remove();
	$form.appendHidden({name:'lstTyp',value:lstTyp});
	$form.submit();
}<%// [버튼] 저장 %>
function saveDoc(){
	if(!isRootChkMsg())
		getIframeContent('openListFrm').saveDoc();
}<%// [버튼] 저장 %>
function saveContinue(){
	if(!isRootChkMsg()) 
		getIframeContent('openListFrm').saveContinue();
}<%// [버튼] 전체삭제 %>
function delPsn(){
	// 등록자UID
	var regrUid = $('#searchForm1').find("input[name='regrUid']").val();
	if(regrUid == undefined || regrUid == '') {
		alertMsg("dm.msg.not.del.regrUid");<%//dm.msg.not.del.regrUid=등록자가 없습니다.%>
		return;
	}
	if(confirmMsg("dm.cfrm.del.all")){<%//dm.cfrm.del.all=사용자가 등록한 모든 정보가 삭제됩니다.\n계속하시겠습니까?%>
		callAjax('./transDelDocAllAjx.do?menuId=${menuId}', {regrUid:regrUid}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
	
};<%// [버튼] 즐겨찾기삭제 %>
function delBumk(){
	// 등록자UID
	var regrUid = $('#searchForm1').find("input[name='regrUid']").val();
	if(regrUid == undefined || regrUid == '') {
		alertMsg("dm.msg.not.del.regrUid");<%//dm.msg.not.del.regrUid=등록자가 없습니다.%>
		return;
	}
	if(confirmMsg("cm.cfrm.del")){<%//cm.cfrm.del=삭제하시겠습니까?%>
		callAjax('./transDelDocBumkAjx.do?menuId=${menuId}', {regrUid:regrUid}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
	
};<%// [버튼] 폴더삭제 %>
function delFld(){
	var selId = getIframeContent('leftPage').getSelectId();
	if(selId == null){
		alertMsg("dm.jsp.setDoc.not.fldSub");<%//dm.jsp.setDoc.not.fldSub=하위 '폴더'를 선택 후 사용해 주십시요.%>
		return;
	}
	if(selId == 'NONE'){
		alertMsg("dm.msg.not.save.emptyCls");<%//dm.msg.not.save.emptyCls='미분류' 로 저장할 수 없습니다.%>
		return;
	}
	// 등록자UID
	var regrUid = $('#searchForm1').find("input[name='regrUid']").val();
	if(regrUid == undefined || regrUid == '') {
		alertMsg("dm.msg.not.del.regrUid");<%//dm.msg.not.del.regrUid=등록자가 없습니다.%>
		return;
	}
	if(confirmMsg("dm.cfrm.del.fld")){<%//dm.cfrm.del.fld=폴더와 문서가 삭제됩니다.\n계속하시겠습니까?%>
		callAjax('./transDelDocFldAjx.do?menuId=${menuId}', {regrUid:regrUid, fldId:selId}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				openDocTree();
			}
		});
	}
	
};<%// [버튼] 저장 %>
function save(){
	if(isRootChk()){
		alertMsg("dm.jsp.setDoc.not.${msgId}");<%//dm.jsp.setFld.msg1=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').save();
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
$(document).ready(function() {
	setUniformCSS();
	});
//-->
</script>

<u:title titleId="dm.jsp.search.doc.title" alt="문서조회" menuNameFirst="true" />

<form id="searchForm" name="searchForm" action="./${listPage }.do" style="padding:10px;">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="lstTyp" value="${lstTyp}" />
<c:if test="${!empty paramEntryList}">
<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
	<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
</c:forEach>
</c:if>
</form>
<jsp:include page="/WEB-INF/jsp/dm/psn/listDocSrch.jsp" />
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
	<c:if test="${lstTyp eq 'B' }">
		<u:buttonArea noBottomBlank="true">
		<u:button href="javascript:setBumkPop();" titleId="dm.btn.bumk.mng" alt="즐겨찾기관리" auth="W" popYn="Y" />
	</u:buttonArea>
</c:if>
<c:if test="${lstTyp eq 'F' && isAdmin == true }">
<div class="titlearea">
	<div class="tit_right">
		<u:titleButton titleId="dm.btn.delete.all" alt="전체삭제" onclick="delPsn();" auth="A"/>
		<u:titleButton titleId="dm.btn.delete.bumk" alt="즐겨찾기삭제" onclick="delBumk();" auth="A"/>
		<u:titleButton titleId="dm.btn.delete.fld" alt="폴더삭제" onclick="delFld();" auth="A"/>
	</div>
</div>
</c:if>
</div>

	
<!-- RIGHT -->
<div style="float:right; width:81%;" id="rContainer">

<u:titleArea frameId="openListFrm" frameSrc="/cm/util/reloadable.do"
	outerStyle="height:510px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:500px;"/>
<u:buttonArea id="rightBtnArea" noBottomBlank="true">
</u:buttonArea>
</div>