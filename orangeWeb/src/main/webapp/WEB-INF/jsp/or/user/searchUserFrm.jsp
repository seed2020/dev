<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String opt = request.getParameter("opt");
	if(opt==null || opt.equals("single")){
		request.setAttribute("outHeight", "370px");
		request.setAttribute("inHeight", "350px");
		request.setAttribute("divHeight", "345px");
		request.setAttribute("frmHeight", "340px");
	} else {
		request.setAttribute("outHeight", "260px");
		request.setAttribute("inHeight", "240px");
		request.setAttribute("divHeight", "235px");
		request.setAttribute("frmHeight", "230px");
	}

	String userUid = request.getParameter("userUid");
	String optParam = (opt==null ? "" : opt.equals("single") ? "&opt=single" : opt.equals("multi") ? "&opt=multi" : "")
			+ (userUid!=null && !userUid.isEmpty() ? "&userUid="+userUid : "");
	request.setAttribute("optParam", optParam);
	
%>
<script type="text/javascript">
<!--
<%// 사용자 정보 관리 항목 %>
var gAttrs = ["userUid", "orgId", "deptId", "rescId", "rescNm", "deptRescNm", "gradeNm", "titleNm", "positNm", "dutyNm", "mbno", "email", "compPhon", "compFno", "homePhon", "homeFno", "extnEmail", "entraYmd"];
<%// 선택된 사용자 목록 리턴 - uncheck:리턴할 때 선택을 해제함 %>
function getSelectedUsers(uncheck){
	var arr = [], $me, obj;
	$("#searchUserForm input:checked").each(function(){
		$me = $(this);
		if(uncheck) this.checked = false;
		if($me.attr('name')=='userUid'){
			obj = {};
			gAttrs.each(function(index, attr){
				obj[attr] = $me.attr("data-"+attr);
			});
			arr.push(obj);
		}
	});
	return arr;
}
<%// 체크 해제%>
function deselectUsers(){
	$("#searchUserForm input[type='checkbox']:checked").each(function(){
		$(this).checkInput(false);
	});
}
<%// 체크박스 클릭 %>
function clickUserCheck(va){
	if(parent.clickUserCheck) parent.clickUserCheck(va);
}
<%// 사용자명 엔터 - 조회함 %>
function searchUser(event){
	if(event!=null){
		if(event.keyCode!=13) return;
		if(event.preventDefault) event.preventDefault();
	}
	var $form = $("#searchUserPopForm");
	$form.attr('action','/or/user/searchUserFrm.do');
	$form.submit();
}
<%// 사용자명에 포커스 주기 - 다른프레임에서 호출 %>
function focusName(){ window.setTimeout('$("#userNm").focus()',20);}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
	<%// 데이터 1건일때 상세조회 팝업 오픈 %>
	<c:if test="${empty param.opt and recodeCount==1}" >parent.viewUserPop('${orUserMapList[0].userUid}');</c:if>
	<c:if test="${not empty param.opt or recodeCount!=1}" >$("#userNm").focus(); if(parent.dialog.isOpen('viewUserDialog')) parent.dialog.forward('searchUserDialog');</c:if>
});
//-->
</script>

<u:listArea>
	<tr><td width="16%" class="head_lt"><u:msg alt="사용자명" titleId="cols.userNm" /></td>
		<td ><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><form id="searchUserPopForm"><u:input id="userNm" value="${param.userNm}" titleId="cols.userNm"
				onkeydown="searchUser(event, 'keydown')"
				/><input type="hidden" name="opt" value="${param.opt}" /><c:if
					test="${not empty param.userUid}"><input type="hidden" name="userUid" value="${param.userUid}" /></c:if><c:if
					test="${not empty param.compId}"><input type="hidden" name="compId" value="${param.compId}" /></c:if><c:if
					test="${not empty param.upward}"><input type="hidden" name="upward" value="${param.upward}" /></c:if><c:if
					test="${not empty param.downward}"><input type="hidden" name="downward" value="${param.downward}" /></c:if><c:if
					test="${not empty param.oneDeptId}"><input type="hidden" name="oneDeptId" value="${param.oneDeptId}" /></c:if><c:if
					test="${not empty param.userStatCd}"><input type="hidden" name="userStatCd" value="${param.userStatCd}" /></c:if><c:if
					test="${not empty param.noUserClick}"><input type="hidden" name="noUserClick" value="${param.noUserClick}" /></c:if></form></td>
			<td><a id="searchUserBtn" href="javascript:searchUser()" class="ico_search"><span><u:msg alt="검색" titleId="cm.btn.search" /></span></a></td>
			</tr>
			</table></td>
	</tr>
</u:listArea>

<form id="searchUserForm">
<u:listArea>
	<tr><c:if
		test="${not empty param.opt}"><td width="3%" class="head_bg"><c:if
		test="${param.opt=='multi'}"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('searchUserForm', this.checked);" value=""/></c:if></td></c:if><c:forEach
		items="${ptLstSetupDVoList}" var="ptLstSetupDVo" varStatus="status"
		><c:if test="${ptLstSetupDVo.dispYn == 'Y'}">
		<td width="${ptLstSetupDVo.wdthPerc}" class="head_ct"><u:term termId="${ptLstSetupDVo.msgId}"
			/></td></c:if></c:forEach>
	</tr>
<c:if test="${not empty param.userNm and fn:length(orUserMapList)==0}" >
	<tr>
		<td class="nodata" colspan="${fn:length(ptLstSetupDVoList)+1}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:forEach items="${orUserMapList}" var="orUserMap" varStatus="outStatus"><c:set
	var="orUserMap" value="${orUserMap}" scope="request" />
	<tr><c:if
		test="${not empty param.opt}"><td class="bodybg_ct"><input type="<u:set
			test="${param.opt=='multi'}" value="checkbox" elseValue="radio" />" name="userUid" <u:set test="${param.userUid == orUserMap.userUid}" value='checked="checked"' elseValue="" />
			onclick="<u:set test="${param.opt=='multi' and empty param.noUserClick}" value="clickUserCheck(this.value);" elseValue="" />"
			data-userUid="${orUserMap.userUid}
			" data-orgId="${orUserMap.orgId}
			" data-deptId="${orUserMap.deptId}
			" data-rescId="${orUserMap.rescId}
			" data-rescNm="${orUserMap.rescNm}
			" data-deptRescNm="${orUserMap.deptRescNm}
			" data-gradeNm="${orUserMap.gradeNm}
			" data-titleNm="${orUserMap.titleNm}
			" data-positNm="${orUserMap.positNm}
			" data-dutyNm="${orUserMap.dutyNm}
			" data-mbno="${orUserMap.mbno}
			" data-email="${orUserMap.email}
			" data-compPhon="${orUserMap.compPhon}
			" data-compFno="${orUserMap.compFno}
			" data-homePhon="${orUserMap.homePhon}
			" data-homeFno="${orUserMap.homeFno}
			" data-extnEmail="${orUserMap.extnEmail
			}" data-refVa1="${orUserMap.refVa1
			}" data-refVa2="${orUserMap.refVa2
			}" data-entraYmd="<u:out value="${orUserMap.entraYmd }" type="date"/>"
			/></td></c:if><c:forEach
		items="${ptLstSetupDVoList}" var="ptLstSetupDVo" varStatus="status"
		><c:if test="${ptLstSetupDVo.dispYn == 'Y'}">
		<td class="body_lt" align="${ptLstSetupDVo.alnVa}"><c:if
				test="${ptLstSetupDVo.atrbId == 'rescNm'}"
			><a href="javascript:parent.viewUserPop('${orUserMap.userUid}')" title="<u:msg titleId='or.jsp.viewUserPop.titlePop'
				/>"><u:convertMap srcId="orUserMap" attId="${ptLstSetupDVo.atrbId}" type="html" /></a></c:if><c:if
				test="${(ptLstSetupDVo.atrbId == 'email' or ptLstSetupDVo.atrbId == 'extnEmail') and sessionScope.userVo.hasMnuGrpMdRidOf('MAIL')}"
			><a href="javascript:parent.mailToPop('<u:convertMap srcId="orUserMap" attId="${ptLstSetupDVo.atrbId}" type="script"
				/>')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop'
				/>"><u:convertMap srcId="orUserMap" attId="${ptLstSetupDVo.atrbId}" type="html" /></a></c:if><c:if
				test="${ptLstSetupDVo.atrbId != 'rescNm' and
					not ((ptLstSetupDVo.atrbId == 'email' or ptLstSetupDVo.atrbId == 'extnEmail') and sessionScope.userVo.hasMnuGrpMdRidOf('MAIL'))}"
			><u:convertMap srcId="orUserMap" attId="${ptLstSetupDVo.atrbId}" type="html"
			/></c:if></td></c:if></c:forEach>
	</tr>
</c:forEach>
</u:listArea>
</form>

<u:pagination noTotalCount="true" noBottomBlank="true" />
