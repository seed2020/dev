<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<%// 사용자 정보 관리 항목 %>
var gAttrs = ["userUid", "odurUid", "orgId", "deptId", "rescId", "rescNm", "deptRescNm", "gradeNm", "titleNm", "positNm", "dutyNm", "mbno", "email", "ein", "compPhon", "compFno", "homePhon", "homeFno", "extnEmail", "refVa1", "refVa2", "entraYmd"];
<%// 선택된 사용자 목록 리턴 - uncheck:리턴할 때 선택을 해제함 %>
function getSelectedUsers(uncheck){
	var arr = [], $me, obj;
	$("#listUserForm input[name='userUid']:checked").each(function(){
		obj = {};
		$me = $(this);
		if(uncheck){
			$(this).checkInput(false);
		}
		gAttrs.each(function(index, attr){
			obj[attr] = $me.attr("data-"+attr);
		});
		arr.push(obj);
	});
	return arr.length==0 ? null : arr;
}
<%// 체크 해제%>
function deselectUsers(){
	$("#listUserForm input[type='checkbox']:checked").each(function(){
		$(this).checkInput(false);
	});
}
<%// 체크박스 클릭 %>
function clickUserCheck(va){
	if(parent.clickUserCheck) parent.clickUserCheck(va);
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script><u:set
	test="${not empty demoSite}" var="userClickFnc"
	value="processDemoLogin" elseValue="viewUserPop"/><u:set
	
	test="${not empty demoSite}" var="pagePadding"
	value="4px 2px 4px 2px" elseValue="8px 10px 10px 10px"/>
<form id="listUserForm" style="padding:${pagePadding};">

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
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'"><c:if
		test="${not empty param.opt}"><td class="bodybg_ct"><input type="<u:set
			test="${param.opt=='multi'}" value="checkbox" elseValue="radio" />" name="userUid" value="${orUserMap.userUid}" <u:set test="${param.userUid == orUserMap.userUid}" value='checked="checked"' elseValue="" />
			onclick="<u:set test="${not empty clickCallback and empty param.noUserClick}" value="${clickCallback}(this.value);" elseValue="" />"
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
			><a href="javascript:parent.${userClickFnc}('${orUserMap.userUid}')" title="<u:msg titleId="${not empty demoSite ? 'pt.demo.login' : 'or.jsp.viewUserPop.titlePop'}"
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