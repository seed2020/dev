<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<%// 사용자 정보 관리 항목 %>
var gAttrs = ["userUid", "odurUid", "orgId", "deptId", "rescId", "rescNm", "deptRescNm", "gradeNm", "titleNm", "positNm", "dutyNm", "mbno", "email", "ein", "compPhon", "compFno", "homePhon", "homeFno", "extnEmail", "refVa1", "refVa2"];
<%// 선택된 사용자 목록 리턴 - uncheck:리턴할 때 선택을 해제함 %>
function getSelectedUsers(uncheck){
	var arr=[], $me, obj;
	$("#listUserForm input[name='userUid']:checked").each(function(){
		obj = {};
		$me = $(this);
		if(uncheck) this.checked = false;
		gAttrs.each(function(index, attr){
			obj[attr] = $me.attr("data-"+attr);
		});
		arr.push(obj);
	});
	return arr.length==0 ? null : arr;
}
<%// 모든 사용자 리턴 %>
function getAllUsers(){
	var arr=[], $me, obj;
	$("#listUserForm input[name='userUid']").each(function(){
		obj = {};
		$me = $(this);
		gAttrs.each(function(index, attr){
			obj[attr] = $me.attr("data-"+attr);
		});
		arr.push(obj);
	});
	return arr.length==0 ? null : arr;
}
<%// [소버튼] +추가 %>
function addSelectedUser(arr){
	var mergeArr = [];
	$("#listUserForm input[name='userUid']").each(function(){
		mergeArr.push($(this).val());
	});
	var changed = false;
	if(arr!=null){
		arr.each(function(index, userVo){
			if(!mergeArr.contains(userVo.userUid)){
				changed = true;
				mergeArr.push(userVo.userUid);
			}
		});
	}
	if(changed){
		location.replace("/or/user/listSeltdUserFrm.do?lstSetupMetaId=OR_SETUP&opt=multi&userUids="+mergeArr.join(','));
	}
}
<%// [소버튼] -삭제 %>
function delSelectedUser(){
	var arr=[];
	$("#listUserForm input[name='userUid']:checked").each(function(){
		arr.push(getParentTag(this, 'tr'));
	});
	arr.each(function(index, tr){
		$(tr).remove();
	});
}
<%// 사용자의 uid 배열 리턴 %>
function getUserUids(attrId){
	var arr = [], obj;
	$("#listUserForm input[id!='checkHeader'][name='userUid']").each(function(){
		if($(this).val()!=''){
			if(attrId==null){
				arr.push($(this).val());
			} else {
				obj = {};
				obj[attrId] = $(this).val();
				arr.push(obj);
			}
		}
	});
	return arr;
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script><u:set test="${param.noPad=='Y'}" var="_padding" value="0px;" elseValue="8px 10px 10px 10px;" />
<form id="listUserForm" style="padding:${_padding}">

<u:listArea noBottomBlank="true">
	<tr><c:if
		test="${not empty param.opt}"><td width="5%" class="head_bg"><c:if
		test="${param.opt=='multi'}"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('listUserForm', this.checked);" value=""/></c:if></td></c:if><c:forEach
		items="${ptLstSetupDVoList}" var="ptLstSetupDVo" varStatus="status"
		><c:if test="${ptLstSetupDVo.dispYn == 'Y'}">
		<td width="${ptLstSetupDVo.wdthPerc}" class="head_ct"><u:term termId="${ptLstSetupDVo.msgId}"
			/></td></c:if></c:forEach>
	</tr>
<c:forEach items="${orUserMapList}" var="orUserMap" varStatus="outStatus"><c:set
	var="orUserMap" value="${orUserMap}" scope="request" />
	<tr><c:if
		test="${not empty param.opt}"><td class="bodybg_ct"><input type="<u:set
			test="${param.opt=='multi'}" value="checkbox" elseValue="radio" />" name="userUid" value="${orUserMap.userUid}"
			data-userUid="${orUserMap.userUid
			}" data-odurUid="${orUserMap.odurUid
			}" data-orgId="${orUserMap.orgId
			}" data-deptId="${orUserMap.deptId
			}" data-rescId="${orUserMap.rescId
			}" data-rescNm="${orUserMap.rescNm
			}" data-deptRescNm="${orUserMap.deptRescNm
			}" data-gradeNm="${orUserMap.gradeNm
			}" data-titleNm="${orUserMap.titleNm
			}" data-positNm="${orUserMap.positNm
			}" data-dutyNm="${orUserMap.dutyNm
			}" data-mbno="${orUserMap.mbno
			}" data-email="${orUserMap.email
			}" data-ein="${orUserMap.ein
			}" data-compPhon="${orUserMap.compPhon
			}" data-compFno="${orUserMap.compFno
			}" data-homePhon="${orUserMap.homePhon
			}" data-homeFno="${orUserMap.homeFno
			}" data-extnEmail="${orUserMap.extnEmail
			}" data-refVa1="${orUserMap.refVa1
			}" data-refVa2="${orUserMap.refVa2
			}" data-entraYmd="<u:out value="${orUserMap.entraYmd }" type="date"/>"
			/></td></c:if><c:forEach
		items="${ptLstSetupDVoList}" var="ptLstSetupDVo" varStatus="status"
		><c:if test="${ptLstSetupDVo.dispYn == 'Y'}">
		<td class="body_lt" align="${ptLstSetupDVo.alnVa}"><c:if
				test="${ptLstSetupDVo.atrbId == 'rescNm'}"
			><a href="javascript:top.viewUserPop('${orUserMap.userUid}')" title="<u:msg titleId='or.jsp.viewUserPop.titlePop'
				/>"><u:convertMap srcId="orUserMap" attId="${ptLstSetupDVo.atrbId}" type="html" /></a></c:if><c:if
				test="${(ptLstSetupDVo.atrbId == 'email' or ptLstSetupDVo.atrbId == 'extnEmail') and sessionScope.userVo.hasMnuGrpMdRidOf('MAIL')}"
			><a href="javascript:top.mailToPop('<u:convertMap srcId="orUserMap" attId="${ptLstSetupDVo.atrbId}" type="script"
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