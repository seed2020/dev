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
		
		String apvrRoleCds = request.getParameter("apvrRoleCds");
		if(apvrRoleCds!=null && !apvrRoleCds.isEmpty()) {
			request.setAttribute("apvrRoleCds", apvrRoleCds.split(","));
		}
	}

	
	String optParam = (opt==null ? "" : opt.equals("single") ? "&opt=single" : opt.equals("multi") ? "&opt=multi" : "");
	request.setAttribute("optParam", optParam);
	
	String userUid = request.getParameter("userUid");
	if(userUid!=null && !userUid.isEmpty()){
		request.setAttribute("uidParam", "&userUid="+userUid);
	}

	String downward = request.getParameter("downward");
	if(downward!=null && !downward.isEmpty()){
		request.setAttribute("downParam", "&downward="+downward);
	}
	
	String userStatCd = request.getParameter("userStatCd");
	if(userStatCd!=null && !userStatCd.isEmpty()){
		request.setAttribute("userStatParam", "&userStatCd="+userStatCd);
	}
	
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
<!--<%
// 선택된 탭번호 %>
var searchUserTabNo = ${tabNo};<%
// 선택된 탭번호 %>
var lastOrgTabNo = ${empty param.userNm ? 0 : 2};<%
// 부서 주소 표시 %>
var gDispDeptAdr = "${empty dispDeptAddrEnable ? '' : 'Y'}";<%
// 설정된 조직도 하단 디스플레이 목록 %>
var displayBottom = [""<c:forEach
items="${ptLstSetupDVoList}" var="ptLstSetupDVo" varStatus="status"
 ><c:if test="${ptLstSetupDVo.dispYn == 'Y'}">, {atrbId:"${ptLstSetupDVo.atrbId}",alnVa:"${ptLstSetupDVo.atrbId}"}</c:if></c:forEach>].slice(1);
<%// 조직도 클릭시 %>
function openUserListFrm(id, name, rescId){
	reloadFrame("searchUserListFrm", "/or/user/listUserFrm.do?orgId="+id+"${optParam}${uidParam}${userStatParam}&noUserClick=Y");
	if(gDispDeptAdr=='Y'){<%// 부서 주소 표시 %>
		callAjax('/or/org/getDeptAddrAjx.do?', {orgId:id}, function(data){
			var prefix = '<u:msg alt="부서 주소" titleId="cols.deptAdr" /> : ';
			$("#searchUserDeptAddrArea").text(data.deptAddr==null ? prefix : prefix+data.deptAddr);
		});
	}
}<%
// [탭 클릭] %>
function searchUserTabClick(tabNo){
	searchUserTabNo = tabNo;
	if(tabNo==0){
		if(lastOrgTabNo != tabNo){
			reloadFrame("searchUserOrgFrm","/or/org/treeOrgFrm.do${orgTreeParam}");
			lastOrgTabNo = tabNo;
		}
	} else if(tabNo==2) {
		if(lastOrgTabNo != tabNo){
			reloadFrame("searchUserOrgFrm","/or/org/treeOrgFrm.do${foreignTreeParam}");
			lastOrgTabNo = tabNo;
		}
	} else if(tabNo==1) {
		getIframeContent("searchUserSearchFrm").focusName();
	}
}<%
// 확인 버튼 클릭 - single %>
function setUsers(){
	var frameId = searchUserTabNo==1 ? "searchUserSearchFrm" : "searchUserListFrm";
	var arr = getIframeContent(frameId).getSelectedUsers(true);
	var result = null;
	if(gUserHandler!=null){
		var obj = null;
		if(arr!=null && arr.length>0){
			<c:if
				test="${param.opt=='single'}">obj = arr[0];</c:if><c:if
				test="${param.opt=='multi'}">obj = arr;</c:if>
		}
		var apvrRoleCd = $("#apvrRoleCdArea input:checked").val();
		if(apvrRoleCd!=null) obj['apvrRoleCd'] = apvrRoleCd;
		result = gUserHandler(obj);
	}
	if(result!=false){
		gUserHandler = null;
		gUserSelectedObj = null;
		dialog.close('searchUserDialog');
	}
}<%
//확인 버튼 클릭 - multi %>
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
}<%
// 사용자 조회창 맨 앞으로 %>
function forwardSearchUserDialog(){
	if(dialog.isOpen('viewUserDialog')) dialog.forward('searchUserDialog');
}<%
// [소버튼] +추가 %>
function addSelectedUser(){
	var frameId = searchUserTabNo==1 ? "searchUserSearchFrm" : "searchUserListFrm";
	var arr = getIframeContent(frameId).getSelectedUsers(true);
	getIframeContent("searchUserCollectedFrm").addSelectedUser(arr);
}<%
// [소버튼] -삭제 %>
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

<u:tabGroup id="searchUserTab" noBottomBlank="true">
	<u:tab onclick="searchUserTabClick(0)" areaId="searchUserTreeArea" alt="조직도" titleId="or.jsp.setOrg.orgTreeTitle" id="searchUserTab" on="${empty param.userNm}" /><c:if
		test="${not empty param.foreign}">
	<u:tab onclick="searchUserTabClick(2)" areaId="searchUserTreeArea" alt="대외 조직도" titleId="ap.jsp.ForeignComp" id="searchUserTab" /></c:if>
	<u:tab onclick="searchUserTabClick(1)" areaId="searchUserSchArea" alt="임직원 검색" titleId="pt.top.orgSrch" id="searchUserTab" on="${not empty param.userNm}" />
</u:tabGroup>

<u:tabArea
	outerStyle="height:${outHeight}; overflow-x:hidden; overflow-y:hidden;"
	innerStyle="height:${inHeight}; margin:10px;">

<u:cmt cmt="[조직도]탭 영역" />
<div id="searchUserTreeArea" <c:if test="${not empty param.userNm}">style="display:none"</c:if>>

<div style="float:left; width:28.8%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"><u:set
		test="${empty param.userNm}" var="orgTreeUrl" value="/or/org/treeOrgFrm.do${orgTreeParam}" elseValue="/cm/util/reloadable.do" />
<iframe id="searchUserOrgFrm" name="searchUserOrgFrm" src="${orgTreeUrl}" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

<div style="float:right; width:70%;">
<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
<iframe id="searchUserListFrm" name="searchUserListFrm" src="/cm/util/reloadable.do" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

</div>

<u:cmt cmt="[임직원 검색]탭 영역" />
<div id="searchUserSchArea" <c:if test="${empty param.userNm}">style="display:none"</c:if>>

<iframe id="searchUserSearchFrm" name="searchUserSearchFrm" src="/or/user/searchUserFrm.do?userNm=${userNm}${optParam}${uidParam}${schUserParam}${downParam}${userStatParam}&noUserClick=Y" style="width:100%; height:${inHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>

</div>

</u:tabArea>

<c:if test="${param.opt == 'multi'}">

<u:title alt="선택된 리스트" titleId="cm.selectedList" >
	<u:titleButton titleId="cm.btn.add" alt="추가" img="ico_add.png" href="javascript:addSelectedUser()" />
	<u:titleButton titleId="cm.btn.del" alt="삭제" img="ico_minus.png" href="javascript:delSelectedUser()" />
</u:title>

<u:titleArea
	outerStyle="height:${divHeight}; overflow-x:hidden; overflow-y:auto; padding:0px"
	innerStyle="NO_INNER_IDV">

<iframe id="searchUserCollectedFrm" name="searchUserCollectedFrm" src="/cm/util/reloadable.do" style="width:100%; height:${frmHeight};" frameborder="0" marginheight="0" marginwidth="0"></iframe>

</u:titleArea>

</c:if>

<c:if test="${not empty apvrRoleCds}">
<u:titleArea
	outerStyle="padding:3px 0px 1px 5px;"
	innerStyle="NO_INNER_IDV">
<u:checkArea id="apvrRoleCdArea"><c:forEach items="${apvrRoleCds}" var="apvrRoleCd" varStatus="status">
<u:radio value="${apvrRoleCd}" name="apvrRoleCd" termId="ap.term.${apvrRoleCd}" checked="${status.first}" /></c:forEach></u:checkArea>
</u:titleArea></c:if>

<c:if test="${not empty dispDeptAddrEnable}">
<u:titleArea
	outerStyle="padding:3px 0px 0px 5px;"
	innerStyle="NO_INNER_IDV">
<div id="searchUserDeptAddrArea" style="color:#454545;"><u:msg alt="부서 주소" titleId="cols.deptAdr" /> : </div>
</u:titleArea></c:if>

<u:buttonArea><c:if
		test="${param.opt=='single'}">
	<u:button titleId="cm.btn.confirm" onclick="setUsers();" alt="확인" /></c:if><c:if
		test="${param.opt=='multi'}">
	<u:button titleId="cm.btn.confirm" onclick="setCollectedUsers();" alt="확인" /></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>