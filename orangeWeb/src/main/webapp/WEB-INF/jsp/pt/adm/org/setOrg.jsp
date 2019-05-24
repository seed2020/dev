<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	if(request.getAttribute("detlViewType")==null){
		request.setAttribute("detlViewType","userList");// orgList, orgDetl, userList
	}
%>
<script type="text/javascript">
<!--<%
// 조직 상세보기 화면 타입 %>
var gOrgDetlViewType = "${detlViewType}";<%
// 선택된 조직 %>
var gOrgId = null;<%
// 부서 잘라내기 정보 %>
var gCopyInfo = null;<%
// 사용자 잘라내기 정보 %>
var gUserCut = null;<%
// [아이콘] 위로이동 %>
function moveUp(frmId){
	if(gOrgDetlViewType=='orgCls'){
		moveCls('up');
	} else {
		moveDirection(frmId, 'up');
	}
}<%
// [아이콘] 아래로이동 %>
function moveDown(frmId){
	if(gOrgDetlViewType=='orgCls'){
		moveCls('down');
	} else {
		moveDirection(frmId, 'down');
	}
}<%
// [아이콘] 위/아래로 이동 %>
function moveDirection(frmId, direction){
	if(frmId=='orgTree'){
		getIframeContent(frmId).move(direction);
	} else if(gOrgId==null || !gOrgDetlViewType.endsWith("List")){
		alertMsg("or.jsp.setOrg.msg1");<%//or.jsp.setOrg.msg1=왼쪽 '조직도'의 조직을 선택 후 사용해 주십시요.%>
	} else {
		var contWin = getIframeContent(gOrgDetlViewType+'Frm');
		contWin.move(direction);
	}
}<%
// [좌 트리 - 조직 클릭] - 오른쪽 리스트 열기 %>
function openUserListFrm(orgId, orgNm, rescId){
	if(orgId!=null){
		gOrgId = orgId;
	}
	applyOrgBtn();
	applyApvBtn();
	applyIconBtn();
	if(gOrgId!=null){
		var url = null;
		if(gOrgDetlViewType=='orgList'){<%// gOrgDetlViewType : orgList, orgDetl, userList, userDetl %>
			url = './listOrgFrm.do?menuId=${menuId}&orgPid='+gOrgId;
		} else if(gOrgDetlViewType=='userList'){
			url = './listUserFrm.do?menuId=${menuId}&orgId='+gOrgId;
		} else if(gOrgDetlViewType != null){
			setDetl(gOrgDetlViewType.substring(3, gOrgDetlViewType.length));
		}
		if(url!=null){
			reloadFrame(gOrgDetlViewType+'Frm', url);
		}
	}
}<%
// [우상단 탭 - 클릭] 하위 조직, 사용자 목록, 부서 정보, 관인/서명인 관리, 분류 정보   - 클릭 %>
function onDetlTabClick(detl){
	if(gOrgDetlViewType != detl){<%// gOrgDetlViewType : orgList, userList, orgInfo, orgImg, orgCls %>
		gOrgDetlViewType = detl;
		gCopyInfo = null;
		
		var onlyDept = (detl=='orgInfo' || detl=='orgImg' || detl=='orgCls');
		var treeWin = getIframeContent("orgTree");
		if(treeWin.gOnlyDept != onlyDept){<%
			// [탭]부서정보, [탭]관인/서명인 관리, [탭]분류정보 일 경우 - 파트 클릭해도 상위의 부서가 넘어오게 세팅 %>
			treeWin.gOnlyDept = onlyDept;
			treeWin.setTimeout("callSelectedClick()", 10);
		} else {
			openUserListFrm();
		}
	}
}<%
// 버튼 보이기 조절 - displayBtn 함수 이용  id배열 넘겨줌 - [조직도]의 것 %>
function applyOrgBtn(){
	var $area = $("#rightBtnArea");<%// gOrgDetlViewType : orgList, orgDetl, userList %>
	if(gOrgDetlViewType=="orgList"){<%// 조직 목록 - ROOT 이외 %>
		if(gOrgId==null || gOrgId=='ROOT'){<%// 조직 목록/사용자 목록 - ROOT의 경우 %>
			displayBtn($area, false, ["btnPopDetl","btnPopPw","btnCopy","btnCut","btnPaste"]);
			displayBtn($area, true,  ["btnSave","btnAddRow","btnDelRow","btnDelSel"]);
		} else {
			displayBtn($area, false, ["btnPopDetl","btnPopPw","btnCopy"]);
			displayBtn($area, true,  ["btnSave","btnCut","btnPaste","btnAddRow","btnDelRow","btnDelSel"]);
		}
		setCopyInfo(gCopyInfo);
	} else if(gOrgDetlViewType=="userList"){<%// 사용자 목록 %>
		var isRoot = (gOrgId==null || gOrgId=='ROOT');
		displayBtn($area, !isRoot,  ["btnSave","btnPopDetl","btnPopPw","btnCopy","btnCut","btnPaste","btnAddRow","btnDelRow","btnDelSel"]);
		setCopyInfo(gCopyInfo);
	} else {
		displayBtn($area, false,  ["btnSave","btnPopDetl","btnPopPw","btnCopy","btnCut","btnPaste","btnAddRow","btnDelRow","btnDelSel"]);
	}
}<%
// 좌측 상단 위로이동 아래로이동 아이콘 버튼 - 보이기 제어 %>
function applyIconBtn(){
	var $area = $("#orgDetl");
	if(gOrgId==null || gOrgId=='ROOT' || gOrgDetlViewType=="orgList" || gOrgDetlViewType=="userList"  || gOrgDetlViewType=="orgCls"){
		displayBtn($area, true,  ["rightUp","rightDown"]);
	} else {
		displayBtn($area, false,  ["rightUp","rightDown"]);
	}
}<%
// 버튼 보이기 조절 %>
function displayBtn($area, showFlag, arr){
	arr.each(function(index, obj){
		if(showFlag){
			$area.find("#"+obj).show();
		} else {
			$area.find("#"+obj).hide();
		}
	});
}<%
// [우하단 버튼] 행추가%>
function addRow(){
	if(gOrgId==null || !gOrgDetlViewType.endsWith("List")){
		alertMsg("or.jsp.setOrg.msg1");<%//or.jsp.setOrg.msg1=왼쪽 '조직도'의 조직을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent(gOrgDetlViewType+'Frm').addRow();
	}
}<%
// [우하단 버튼] 행삭제 %>
function delRow(){
	if(gOrgId==null || !gOrgDetlViewType.endsWith("List")){
		alertMsg("or.jsp.setOrg.msg1");<%//or.jsp.setOrg.msg1=왼쪽 '조직도'의 조직을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent(gOrgDetlViewType+'Frm').delRow();
	}
}<%
// [우하단 버튼] 선택삭제 %>
function delSelRow(){
	if(gOrgId==null || !gOrgDetlViewType.endsWith("List")){
		alertMsg("or.jsp.setOrg.msg1");<%//or.jsp.setOrg.msg1=왼쪽 '조직도'의 조직을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent(gOrgDetlViewType+'Frm').delSelRow();
	}
}<%
// [우하단 버튼] 저장 %>
function saveOrg(){
	if(gOrgId==null){
		alertMsg("or.jsp.setOrg.msg1");<%//or.jsp.setOrg.msg1=왼쪽 '조직도'의 조직을 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent(gOrgDetlViewType+'Frm').saveOrg();
	}
}<%
// [우하단 버튼] 이동 %>
function cutOrg(){ doCopy('cut'); }<%
// [우하단 버튼] 겸직,파견 %>
function copyOrg(){ doCopy('copy'); }<%
// 겸직,파견,이동 - 실제 함수%>
function doCopy(mode){<%// gOrgDetlViewType : orgList, orgDetl, userList %>
	if(gOrgDetlViewType=='orgList' || gOrgDetlViewType=='userList'){<%// 조직목록, 사용자목록 이면 %>
		var arr = getIframeContent(gOrgDetlViewType+'Frm').getCheckedArray();
		if(arr==null){
			var itemId = (gOrgDetlViewType=='orgList') ? "#cols.org" : "#cols.user";
			alertMsg("cm.msg.noSelectedItem", itemId);<%// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.%>
			setCopyInfo(null);
		} else {
			var isOrg = gOrgDetlViewType=='orgList';
			var copyInfo = isOrg ? { orgPid:gOrgId, ids:arr, mode:mode, type:gOrgDetlViewType } : { orgId:gOrgId, ids:arr, mode:mode, type:gOrgDetlViewType };
			setCopyInfo(copyInfo);
		}
	}
}<%
// [우하단 버튼] 붙여넣기 %>
function pasteOrg(){
	if(gCopyInfo!=null && (gOrgDetlViewType=='orgList' || gOrgDetlViewType=='userList')){
		if(gOrgDetlViewType=='orgList'){
			if(gCopyInfo.orgPid == gOrgId){
				alertMsg("or.msg.not.paste.org.toSameOrg");<%// or.msg.not.paste.org.toSameOrg=동일한 부서로 이동 할 수 없습니다. %>
				return;
			}<%
			// 조직의 경우 바로 저장 %>
			processPaste();
		} else {
			if(gCopyInfo.orgId == gOrgId){
				alertMsg("or.msg.not.paste.user.toSameOrg");<%// or.msg.not.paste.user.toSameOrg=동일한 부서로 겸직 또는 파견 할 수 없습니다. %>
				return;
			}
			if(gCopyInfo.mode=='cut'){
				processPaste();
			} else if(gCopyInfo.mode=='copy'){<%// 겸직,파견 - 선택 %>
				dialog.open('setAduTypDialog','<u:msg titleId="or.jsp.setOrg.aduTypTitle" alt="겸직 파견직 선택" />','./setAduTypPop.do?menuId=${menuId}');
			}
		}
	}
}<%
// 이동/겸직,파견 - 실행 %>
function processPaste(){
	gCopyInfo['toOrgId'] = gOrgId;
	getIframeContent(gOrgDetlViewType+'Frm').pasteArray(gCopyInfo);
	setCopyInfo(null);
}
var gCutTitle = '<u:msg titleId="or.btn.cut" alt="이동" />';
var gCopyTitle = '<u:msg titleId="or.btn.copy" alt="겸직,파견" />';<%
// 겸직,파견   이동  - 버튼 타이틀 갯수 설정/갯수 초기화 %>
function setCopyInfo(copyInfo){
	gCopyInfo = copyInfo;
	if(copyInfo==null){
		$("#btnCopy span").text(gCopyTitle);
		$("#btnCut span").text(gCutTitle);
	} else if(copyInfo.mode=='cut'){
		$("#btnCopy span").text(gCopyTitle);
		$("#btnCut span").text(gCutTitle+" ("+copyInfo.ids.length+")");
	} else if(copyInfo.mode=='copy'){
		$("#btnCopy span").text(gCopyTitle+" ("+copyInfo.ids.length+")");
		$("#btnCut span").text(gCutTitle);
	}
}<%
// [우하단 버튼] 사용자 상세정보 팝업 %>
function popDetl(){
	if(gOrgDetlViewType=='userList'){
		var arr = getIframeContent(gOrgDetlViewType+'Frm').getCheckedArray();
		if(arr==null){
			alertMsg("or.jsp.setOrg.msg2");<%//vor.jsp.setOrg.msg2=선택된 사용자가 없습니다. %>
		} else if(arr.length>1){
			alertMsg("or.jsp.setOrg.msg3");<%// or.jsp.setOrg.msg3=한면의 사용자를 선택해야 합니다. %>
		} else {
			openUserDetl(arr[0]);
		}
	}
}<%
// [상세정보팝업] - 사용자(select) 변경 %>
function openUserDetl(userUid){
	dialog.open('setUserDetlDialog','<u:msg titleId="or.jsp.setOrg.userDetlTitle" alt="사용자 상세 정보"/>','./setUserPop.do?menuId=${menuId}&userUid='+userUid);
	dialog.onClose("setUserDetlDialog", function(){ dialog.close('setPhotoDialog'); });
}<%
// [우하단 버튼] 비밀번호변경 %>
function popPw(userUid){
	if(gOrgDetlViewType=='userList'){<%// 사용자목록 이면 %>
		if(userUid==null){
			var arr = getIframeContent(gOrgDetlViewType+'Frm').getCheckedArray();
			if(arr==null){
				alertMsg("cm.msg.noSelectedItem", "#cols.user");<%// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.%>
				return;
			}
			userUid = arr.join(',');
		}
		dialog.open('setPwDialog','<u:msg titleId="pt.jsp.setPw.title" alt="비밀번호 변경"/>','./setPwPop.do?menuId=${menuId}&userUids='+userUid);
	}
}<%
// [우하단 버튼] 비밀번호변경 - 닫기 %>
function closePw(){ dialog.close("setPwDialog"); }<%

/////////////////////////////////////////////////////
//
//          결재 화면 용
//

// 버튼 보이기 조절 - displayBtn 함수 이용  id배열 넘겨줌 - [결재]의 것 %>
function applyApvBtn(){
	var $area = $("#rightBtnArea");
	if(gOrgId==null || gOrgId=='ROOT'){
		displayBtn($area, false, ["deptInfoBtn","addImgBtn","addAllClsBtn","addClsBtn","modClsBtn","delClsBtn","moveClsBtn"]);
	} else if(gOrgDetlViewType=='orgInfo'){
		displayBtn($area, true,  ["deptInfoBtn"]);
		displayBtn($area, false, ["addImgBtn","addAllClsBtn","addClsBtn","modClsBtn","delClsBtn","moveClsBtn"]);
	} else if(gOrgDetlViewType=='orgImg'){
		displayBtn($area, true,  ["addImgBtn"]);
		displayBtn($area, false, ["deptInfoBtn","addAllClsBtn","addClsBtn","modClsBtn","delClsBtn","moveClsBtn"]);
	} else if(gOrgDetlViewType=='orgCls'){
		displayBtn($area, true,  ["addAllClsBtn","addClsBtn","modClsBtn","delClsBtn","moveClsBtn"]);
		displayBtn($area, false, ["addImgBtn","deptInfoBtn"]);
	} else {
		displayBtn($area, false, ["deptInfoBtn","addImgBtn","addAllClsBtn","addClsBtn","modClsBtn","delClsBtn","moveClsBtn"]);
	}
}<%
// 탭 변경 - 해당 프레임 리로드, 해당 버튼만 보이게 %>
function setDetl(frmNm, extParam){
	var url = (frmNm=='Cls' ? './treeOrgCls' : './setOrg'+frmNm)+"Frm.do?menuId=${menuId}&orgId="+gOrgId+(extParam==null ? "" : extParam);
	if(gOrgId==null || gOrgId=='ROOT'){
		url = "/cm/util/reloadable.do";
	}
	$("#dept"+frmNm+"Frame").attr('src', url);
}<%
// [버튼] 저장 - 부서정보[탭] %>
function saveOrgInfo(){
	getIframeContent("deptInfoFrame").saveOrgInfo();
}<%
// [버튼] 추가 - 부서이미지[탭] %>
function addOrgImg(){
	getIframeContent("deptImgFrame").addOrgImg();
}<%
// [버튼] 일괄 분류 등록, 분류 추가, 분류 수정, 분류 삭제 - 분류 정보[탭] %>
function clickManageCls(mode){
	getIframeContent("deptClsFrame").manageCls(mode);
}<%
// [버튼] 위로이동, 아래로이동 - 분류 정보[탭] %>
function moveCls(direction){
	getIframeContent("deptClsFrame").move(direction);
}<%
// [버튼] 분류이동 - callback %>
function callbackMoveCls(){
	var from = getIframeContent("deptClsFrame").gClsId;
	var to = gClsId;
	if(to==null){
		alertMsg('ap.msg.chooseCls');<%//ap.msg.chooseCls=분류정보를 선택해 주십시요.%>
		return;
	}
	if(from==to){
		alertMsg('ap.msg.chooseDifCls');<%//ap.msg.chooseDifCls=동일한 분류정보로 이동 할 수 없습니다.%>
		return;
	}
	var result = false;
	callAjax("./transOrgClsCutPasteAjx.do?menuId=${menuId}&orgId="+gOrgId, {from:from, to:to}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result){
		reloadTree(from, 'org');
		dialog.close('setMoveOrgClsDialog');
	}
}<%
// [버튼] 분류이동 - callback - 저장뒤 호출 %>
function reloadTree(clsInfoId, type){
	getIframeContent('deptClsFrame').reload('/pt/adm/org/treeOrgClsFrm.do?menuId=${menuId}&orgId='+gOrgId+'&clsInfoId='+clsInfoId);
	dialog.close('setMoveOrgClsDialog');
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
	$("#${detlViewType}Frm").show();
});<%
// 조직도 DB 동기화 %>
function popOrgDbSync(compId){
	dialog.open('orgDbSyncDialog','<u:msg titleId="or.txt.syncSetup" alt="동기화 설정"/>','./setOrgDbSyncPop.do?menuId=${menuId}'+(compId==null ? '' : '&compId='+compId));
}<%
// 동기화 실행 %>
function runSyncNow(){
	if(!confirmMsg('or.cfm.syscRun')) return;<%//or.cfm.syscRun=조직도 동기화를 진행 하시겠습니까 ?%>
	callAjax("./runNow.do?menuId=${menuId}", {}, function(data){
		if(data.message != null) alert(data.message);
	}, null, null, true);
}
var gHideDelDeptFlag = false;<%
// 삭제 부서 숨김 %>
function hideDelDept(){
	gHideDelDeptFlag = !gHideDelDeptFlag;
	getIframeContent('orgTree').hideDelTree(gHideDelDeptFlag);
}
//-->
</script>

<u:title titleId="or.jsp.setOrg.title" alt="조직도 사용자 관리" menuNameFirst="true" />

<!-- LEFT -->
<div class="left" style="float:left; width:23.5%;">

<u:title titleId="or.jsp.setOrg.orgTreeTitle" alt="조직도" type="small" >
	<u:titleIcon type="up" href="javascript:moveUp('orgTree')" auth="A" />
	<u:titleIcon type="down" href="javascript:moveDown('orgTree')" auth="A" />
</u:title>

<u:set test="${not empty param.compId}" var="compParam" value="&compId=${param.compId}" elseValue="" 
/><u:titleArea
	outerStyle = "height:580px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameId="orgTree"
	frameSrc="./treeOrgFrm.do?menuId=${menuId}${compParam}"
	frameStyle="width:100%; height:570px;" />

<!-- buttonArea -->
<u:buttonArea id="leftBtnArea" noBottomBlank="true">
	<u:button href="javascript:hideDelDept();" id="btnHideDelDept" titleId="or.btn.hideDelDept" alt="삭제 부서 숨김" auth="A" /><c:if
		test="${not empty orgSyncEnable}"><c:if
		test="${not empty orDbIntgDVo}">
	<u:button href="javascript:runSyncNow();" id="btnRunSyncNow" titleId="or.btn.runNow" alt="동기화 실행" auth="A" /></c:if><c:if
		test="${sessionScope.userVo.userUid eq 'U0000001'}">
	<u:button href="javascript:popOrgDbSync();" id="btnOrgDbSync" titleId="pt.btn.sync" alt="동기화" auth="A" /></c:if></c:if>
</u:buttonArea>
</div>

<!-- RIGHT -->
<div class="right" style="float:right; width:75%;">

<u:tabGroup id="orgDetl" noBottomBlank="true">
	<u:tab alt="하위 조직"	areaId="orgListTabArea" onclick="onDetlTabClick('orgList');" id="orgDetl" titleId="or.jsp.setOrg.orgSubListTitle" onAreaId="${detlViewType}Frm" />
	<u:tab alt="사용자 목록" areaId="userListTabArea" onclick="onDetlTabClick('userList');" id="orgDetl" titleId="or.jsp.setOrg.userListTitle" onAreaId="${detlViewType}Frm" on="true" />
	<u:tab id="orgDetl" areaId="deptInfoTabArea" onclick="onDetlTabClick('orgInfo');" titleId="ap.jsp.setOrgEnv.tab.deptInfo" alt="부서 정보" on="${tab == 'Info'}" />
	<u:tab id="orgDetl" areaId="deptImgTabArea" onclick="onDetlTabClick('orgImg');" titleId="ap.jsp.setOrgEnv.tab.deptImg" alt="관인/서명인 관리" on="${tab == 'Img'}" />
	<u:tab id="orgDetl" areaId="deptClsTabArea" onclick="onDetlTabClick('orgCls');" titleId="ap.jsp.setOrgEnv.tab.deptCls" alt="분류 정보" on="${tab == 'Cls'}" />
	<u:tabIcon type="up" onclick="moveUp('orgDetl')" id="rightUp" />
	<u:tabIcon type="down" onclick="moveDown('orgDetl')" id="rightDown" />
</u:tabGroup>

<u:tabArea outerStyle="height:580px; overflow-x:hidden; overflow-y:auto;" innerStyle="NO_INNER_IDV" >
<div id="orgListTabArea" style="display:none">
<iframe id="orgListFrm" name="orgListFrm" src="/cm/util/reloadable.do" style="width:100%; height:570px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
<div id="userListTabArea">
<iframe id="userListFrm" name="userListFrm" src="/cm/util/reloadable.do" style="width:100%; height:570px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
<div id="deptInfoTabArea" style="display:none">
<iframe id="deptInfoFrame" name="deptInfoFrame" src="/cm/util/reloadable.do" style="width:100%; height:570px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
<div id="deptImgTabArea" style="display:none">
<iframe id="deptImgFrame" name="deptImgFrame" src="/cm/util/reloadable.do" style="width:100%; height:570px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
<div id="deptClsTabArea" style="display:none">
<iframe id="deptClsFrame" name="deptClsFrame" src="/cm/util/reloadable.do" style="width:100%; height:576px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
</u:tabArea>

<u:buttonArea id="rightBtnArea" noBottomBlank="true">
	<u:button href="javascript:popDetl();" id="btnPopDetl" titleId="cm.btn.detl" alt="상세정보" style="display:none;" auth="A" />
	<u:button href="javascript:popPw();" id="btnPopPw" titleId="pt.jsp.setPw.title" alt="비밀번호 변경" style="display:none;" auth="A" />
	<u:button href="javascript:copyOrg();" id="btnCopy" titleId="or.btn.copy" alt="겸직,파견" style="display:none;" auth="A" />
	<u:button href="javascript:cutOrg();" id="btnCut" titleId="or.btn.cut" alt="이동" style="display:none;" auth="A" />
	<u:button href="javascript:pasteOrg();" id="btnPaste" titleId="cm.btn.paste" alt="붙여넣기" style="display:none;" auth="A" />
	<u:button href="javascript:addRow();" id="btnAddRow" titleId="cm.btn.plus" alt="행추가" auth="A" />
	<u:button href="javascript:delRow();" id="btnDelRow" titleId="cm.btn.minus" alt="행삭제" auth="A" />
	<u:button href="javascript:delSelRow();" id="btnDelSel" titleId="cm.btn.selDel" alt="선택삭제" auth="A" />
	<u:button href="javascript:saveOrg();" id="btnSave" titleId="cm.btn.save" alt="저장" auth="A" />
	
	<u:button titleId="cm.btn.save" alt="저장" id="deptInfoBtn" href="javascript:saveOrgInfo();" auth="A" style="display:none;" />
	<c:if test="${not empty adminPage or optConfigMap.alwChgOfcSeal == 'Y'}">
	<u:button titleId="cm.btn.add" alt="추가" id="addImgBtn" href="javascript:addOrgImg();" auth="A" style="display:none;" />
	</c:if>
	<c:if test="${not empty adminPage and false}">
	<u:button titleId="ap.btn.addAllCls" alt="일괄 분류 등록" id="addAllClsBtn" href="javascript:clickManageCls('addAllCls');" auth="A" style="display:none;" />
	</c:if>
	<u:button titleId="ap.btn.addCls" alt="분류 추가" id="addClsBtn" href="javascript:clickManageCls('addCls');" auth="A" style="display:none;" />
	<u:button titleId="ap.btn.modCls" alt="분류 수정" id="modClsBtn" href="javascript:clickManageCls('modCls');" auth="A" style="display:none;" />
	<u:button titleId="ap.btn.delCls" alt="분류 삭제" id="delClsBtn" href="javascript:clickManageCls('delCls');" auth="A" style="display:none;" />
	<u:button titleId="ap.btn.moveCls" alt="분류 이동" id="moveClsBtn" href="javascript:clickManageCls('moveCls');" auth="A" style="display:none;" />
	<u:button href="javascript:history.go(-1);" titleId="cm.btn.cancel" alt="취소" />
</u:buttonArea>

</div>