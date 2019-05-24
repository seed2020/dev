<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String opt = request.getParameter("opt");
	if("multi".equals(opt)){
		request.setAttribute("outHeight", "240px");
		request.setAttribute("inHeight", "230px");
		request.setAttribute("divHeight", "215px");
		request.setAttribute("frmHeight", "210px");
	} else {
		request.setAttribute("outHeight", "370px");
		request.setAttribute("inHeight", "350px");
		request.setAttribute("divHeight", "345px");
		request.setAttribute("frmHeight", "340px");
	}

	String downward = request.getParameter("downward");
	if(downward!=null && !downward.isEmpty()){
		request.setAttribute("downParam", "?downward="+downward);
	}
	
	String userUid = request.getParameter("userUid");
	String optParam = opt==null ? "" : 
		(opt.equals("single") ? "&opt=single" : opt.equals("multi") ? "&opt=multi" : "");
	String uidParam = (userUid==null || userUid.isEmpty() ? "" : "&userUid="+userUid);
	request.setAttribute("optParam", optParam);
	request.setAttribute("uidParam", uidParam);
	String userNm = request.getParameter("userNm");
	if(userNm!=null && !userNm.isEmpty()){
		userNm = URLEncoder.encode(userNm, "UTF-8");
		request.setAttribute("userNm", userNm);
		request.setAttribute("tabNo", "1");
	} else {
		request.setAttribute("tabNo", "0");
	}
%>
<script type="text/javascript">
<!--
<%// 선택된 탭번호 %>
var searchUserTabNo = ${tabNo};
<%// 설정된 조직도 하단 디스플레이 목록 %>
var displayBottom = [""<c:forEach
items="${ptLstSetupDVoList}" var="ptLstSetupDVo" varStatus="status"
 ><c:if test="${ptLstSetupDVo.dispYn == 'Y'}">, {atrbId:"${ptLstSetupDVo.atrbId}",alnVa:"${ptLstSetupDVo.atrbId}"}</c:if></c:forEach>].slice(1);
<%// 조직도 클릭시 %>
function openUserListFrm(id, name, rescId){
	alert("/or/user/listUserFrm.do?orgId="+id+"${optParam}${uidParam}&noUserClick=Y");
	reloadFrame("searchUserListFrm", "/or/user/listUserFrm.do?orgId="+id+"${optParam}${uidParam}&noUserClick=Y");
}
<%// [탭 클릭] %>
function searchUserTabClick(tabNo){
	searchUserTabNo = tabNo;
	if(tabNo==0){
		if(getIframeContent("searchUserOrgFrm").location.href.indexOf("reloadable.do")>0){
			reloadFrame("searchUserOrgFrm","/or/org/treeOrgFrm.do${downParam}");
		}
	} else if(tabNo==1) {
		getIframeContent("searchUserSearchFrm").focusName();
	}
}
<%// 확인 버튼 클릭 %>
function setUsers(){
	var frameId = searchUserTabNo==0 ? "searchUserListFrm" : "searchUserSearchFrm";
	var arr = getIframeContent(frameId).getSelectedUsers(true);
	var result = null;
	if(gUserHandler!=null){
		var obj = null;
		if(arr!=null && arr.length>0){
			<c:if
				test="${param.opt=='single'}">obj = arr[0];</c:if><c:if
				test="${param.opt=='multi'}">obj = arr;</c:if>
		}
		result = gUserHandler(obj);
	}
	if(result!=false){
		gUserHandler = null;
		gUserSelectedObj = null;
		dialog.close('searchUserDialog');
	}
}
function setCollectedUsers(){
	var arr = getIframeContent("searchUserCollectedFrm").getAllUsers();
	var result = null;
	if(gUserHandler!=null){
		result = gUserHandler(arr);
	}
	if(result!=false){
		gUserHandler = null;
		gUserSelectedObj = null;
		dialog.close('searchUserDialog');
	}
}
<%// 사용자 조회창 맨 앞으로 %>
function forwardSearchUserDialog(){
	if(dialog.isOpen('viewUserDialog')) dialog.forward('searchUserDialog');
}
<%// [소버튼] +추가 %>
function addSelectedUser(){
	var frameId = searchUserTabNo==0 ? "searchUserListFrm" : "searchUserSearchFrm";
	var arr = getIframeContent(frameId).getSelectedUsers(true);
	getIframeContent("searchUserCollectedFrm").addSelectedUser(arr);
}
<%// [소버튼] -삭제 %>
function delSelectedUser(){
	getIframeContent("searchUserCollectedFrm").delSelectedUser();
}
$(document).ready(function() {<c:if test="${recodeCount==1}" >
	viewUserPop('${orUserMapList[0].userUid}');<%// 데이터 1건일때 상세조회 팝업 오픈 %>
	dialog.noFocus = true;<%// 사용자명 input에 포커스 금지 %>
	</c:if><c:if test="${empty param.opt}">
	dialog.onClose('searchUserDialog', function(){
		$("#topSearchUserForm input[name='userNm']").focus();<%// 닫을때 상단의 "임직원 검색"에 focus %>
	});
	</c:if><c:if test="${param.opt == 'multi'}">
	var arr = [];
	if(gUserSelectedObj!=null){
		gUserSelectedObj.each(function(index, userVo){
				if(userVo.userUid!=null && userVo.userUid!='')arr.push(userVo.userUid);
			});
	}
	reloadFrame("searchUserCollectedFrm", "/or/user/listSeltdUserFrm.do?lstSetupMetaId=OR_SETUP${optParam}&userUids="+arr.join(','));
	</c:if>
});
//-->
</script>

<div style="width:800px">

<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto; padding:0px"
	innerStyle="NO_INNER_IDV">

<iframe id="searchUserCollectedFrm" name="searchUserCollectedFrm" src="/cm/util/reloadable.do" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>

</u:titleArea>

<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>