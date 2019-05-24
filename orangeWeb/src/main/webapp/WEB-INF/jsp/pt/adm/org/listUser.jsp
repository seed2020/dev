<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// [상세정보팝업] - 사용자(select) 변경 %>
function openUserDetl(userUid){
	dialog.open('setUserDetlDialog','<u:msg titleId="or.jsp.setOrg.userDetlTitle" alt="사용자 상세 정보"/>','./setUserPop.do?menuId=${menuId}&userUid='+userUid);
	dialog.onClose("setUserDetlDialog", function(){ dialog.close('setPhotoDialog'); });
}<%
//[우하단 버튼] 보안 설정 %>
function setSecu(){
	var arr = getCheckedArray('odurUid');
	if(arr==null){
		alertMsg("cm.msg.noSelectedItem", "#cols.user");<%// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.%>
		return;
	}
	var odurUids = arr.join(',');
	dialog.open('setSecuDialog','<u:msg titleId="or.btn.bulkSecu" alt="일괄 보안 설정"/>','./setSecuPop.do?menuId=${menuId}&odurUids='+odurUids);
}<%
// [우하단 버튼] 비밀번호변경 %>
function popPw(userUid){
	if(userUid==null){
		var arr = getCheckedArray();
		if(arr==null){
			alertMsg("cm.msg.noSelectedItem", "#cols.user");<%// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.%>
			return;
		}
		userUid = arr.join(',');
	}
	dialog.open('setPwDialog','<u:msg titleId="pt.jsp.setPw.title" alt="비밀번호 변경"/>','./setPwPop.do?menuId=${menuId}&userUids='+userUid);
}<%
// 비밀번호 변경 Dialog 닫기 %>
function closePw(){
	dialog.close("setPwDialog");
}<%
// 상태코드 변경 %>
function changeStatCd(){
	var arr = getCheckedArray();
	if(arr==null){
		alertMsg("cm.msg.noSelectedItem", "#cols.user");<%// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.%>
		return;
	}
	userUid = arr.join(',');
	dialog.open('setPwDialog','<u:msg titleId="or.btn.changeStatCd" alt="상태코드 변경"/>','./setStatCdPop.do?menuId=${menuId}&userUids='+userUid);
}<%
// 체크된 목록 리턴 - 잘라내기용 %>
function getCheckedArray(attrNm){
	var arr = [], va;
	$("#listUserForm input[type=checkbox]:checked").each(function(){
		if(attrNm==null){
			va = $(this).val();
			if(va!=null && va!='') { arr.push(va); }
		} else {
			va = $(this).attr('data-'+attrNm);
			if(va!=null && va!='') { arr.push(va); }
		}
	});
	return arr.length==0 ? null : arr;
}<%
// 엔터키에 서밋 되도록%>
function onEnter(event, formId){
	if(event.which==13){
		$("#"+formId).submit();
	}
}<%
// 부서선택(서브조직 포함) %>
function openOrgWithSub(){
	var $area = $("#deptSearchArea");
	var data = {orgId:$area.find("#orgId").val(), withSub:$area.find("#withSub").val()};<%// 팝업 열때 선택될 데이타 %>
	var option = {data:data, withSub:true};
	var global = "${global}";
	if(global=='Y') option['global'] = 'Y';
	searchOrgPop(option, function(orgVo){
		if(orgVo!=null){
			$area.find("#orgId").val(orgVo.orgId);
			$area.find("#deptRescNm").val(orgVo.rescNm + (orgVo.withSub=="Y" ? "(<u:msg titleId='or.check.withSub.short' alt='하위포함' />)" : ""));
			$area.find("#withSub").val(orgVo.withSub);
		} else {
			$area.find("#orgId").val("");
			$area.find("#deptRescNm").val("");
			$area.find("#withSub").val("");
		}
	});
}<%
// 사용자 팝업 - 저장 후 호출됨 %>
function openUserListFrm(){ reload(); }<%
// 엑셀 업로드 %>
function popExcel(){
	dialog.open('setExcelDialog','<u:msg titleId="or.jsp.listUser.excel" alt="엑셀업로드"/>','./setExcelPop.do?menuId=${menuId}');
}<%
// 엑셀 다운로드 %>
function excelDownFile() {
	var $form = $('#searchForm');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
	
	$form.attr('method','get');
	$form.attr('action','./listUser.do');
	$form.attr('target','');
};
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
	$("#searchForm").find("input:text, select").first().focus();
});
//-->
</script>

<u:title titleId="or.jsp.listUser.title" alt="사용자 관리" menuNameFirst="true" ><c:if test="${sessionScope.userVo.userUid eq 'U0000001'}">
	<u:titleIcon type="plus" titleId="or.jsp.listUser.excel" onclick="popExcel()" auth="A" /></c:if
></u:title>

<%// 검색영역 %>
<u:searchArea>
	<form id="searchForm" name="searchForm" action="./listUser.do">
	<input type="hidden" name="menuId" value="${menuId}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0"><c:forEach
			items="${ptLstSetupDVoCondiList}" var="ptLstSetupDVos" varStatus="outStatus">
		<tr><c:forEach
			items="${ptLstSetupDVos}" var="ptLstSetupDVo" varStatus="status"><c:if
				test="${not empty ptLstSetupDVo}"><c:if test="${not status.first }">
			<td width="10px;"></td></c:if>
			<td class="search_tit"><u:msg titleId="${ptLstSetupDVo.msgId}" /></td>
			<td><c:if
								test="${ptLstSetupDVo.sortOptVa == 'value'}"
				><u:convert srcId="${ptLstSetupDVo.atrbId}" scope="param" var="_value"
				/><c:if
					test="${ptLstSetupDVo.atrbId == 'deptRescNm'}"
					><table id="deptSearchArea" border="0" cellpadding="0" cellspacing="0"><tr>
                        <td><u:input
						id="${ptLstSetupDVo.atrbId}" titleId="${ptLstSetupDVo.msgId}" style="width:120px;" value="${_value}"
						readonly="Y" /><input id="orgId" name="orgId" type="hidden" value="${param.orgId}"/>
						<input id="withSub" name="withSub" type="hidden" value="${param.withSub}"/></td>
                        <td><a href="javascript:openOrgWithSub();" class="ico_search" title="<u:msg titleId='cm.btn.search' alt='검색' />"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></td>
                        </tr></table></c:if><c:if
					test="${ptLstSetupDVo.atrbId != 'deptRescNm'}"
					><u:input
						id="${ptLstSetupDVo.atrbId}" titleId="${ptLstSetupDVo.msgId}" style="width:140px;" value="${_value}"
						onkeydown="onEnter(event, 'searchForm')" maxByte="30" /></c:if></c:if><c:if
								test="${ptLstSetupDVo.sortOptVa == 'code'}"
					><u:convert srcId="${fn:substring(ptLstSetupDVo.atrbId, 0, fn:length(ptLstSetupDVo.atrbId)-2)}Cd" scope="param" var="_value"
				/><select name="${fn:substring(ptLstSetupDVo.atrbId, 0, fn:length(ptLstSetupDVo.atrbId)-2)}Cd"<u:elemTitle titleId="${ptLstSetupDVo.msgId}" />>
					<option value=""> - <u:msg titleId="cm.option.all" alt="전체" /> - </option><u:convert
						srcId="${fn:substring(ptLstSetupDVo.atrbId, 0, fn:length(ptLstSetupDVo.atrbId)-2)}CdList" var="_cdList"
						/><c:forEach
						items="${_cdList}" var="cdVo" varStatus="inStatus">
					<u:option value="${cdVo.cd}" title="${cdVo.rescNm}" checkValue="${_value}"
					/></c:forEach>
					</select></c:if><c:if
								test="${ptLstSetupDVo.sortOptVa == 'idList'}"
					><u:convert srcId="${ptLstSetupDVo.atrbId}" scope="param" var="_value"
				/><select name="${ptLstSetupDVo.atrbId}"<u:elemTitle titleId="${ptLstSetupDVo.msgId}" />>
					<option value=""> - <u:msg titleId="cm.option.all" alt="전체" /> - </option><c:if
						test="${ptLstSetupDVo.atrbId == 'compId'}"><c:forEach
						items="${ptCompBVoList}" var="ptCompBVo" varStatus="inStatus">
					<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${_value}"
					/></c:forEach></c:if>
					</select></c:if></td></c:if><c:if
				test="${empty ptLstSetupDVo}">
			<td></td><td></td></c:if></c:forEach>
		</tr>
	</c:forEach>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<%// 목록 %>
<form id="listUserForm">

<u:listArea id="listArea">
	<tr>
		<td width="2.5%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('listUserForm', this.checked);" value=""/></td><c:if
			test="${not empty ptCompBVoList}">
		<td width="10%" class="head_bg"><u:msg titleId="cols.comp" alt="회사"
			/></td></c:if><c:forEach
		items="${ptLstSetupDVoList}" var="ptLstSetupDVo" varStatus="status"
		><c:if test="${ptLstSetupDVo.dispYn == 'Y'}">
		<td width="${ptLstSetupDVo.wdthPerc}" class="head_ct"><u:msg titleId="${ptLstSetupDVo.msgId}"
			/></td></c:if></c:forEach>
	</tr>
	
<c:forEach items="${orUserMapList}" var="orUserMap" varStatus="outStatus"><c:set
	var="orUserMap" value="${orUserMap}" scope="request" />
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="bodybg_ct"><input type="checkbox" name="userUid" value="${orUserMap.userUid
			}" data-userUid="${orUserMap.userUid
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
			}" /></td><c:if
			test="${not empty ptCompBVoList}">
		<td class="body_lt" align="center"><u:out value="${orUserMap.compNm}" /></td></c:if><c:forEach
		items="${ptLstSetupDVoList}" var="ptLstSetupDVo" varStatus="status"
		><c:if test="${ptLstSetupDVo.dispYn == 'Y'}">
		<td class="body_lt" align="${ptLstSetupDVo.alnVa}"><c:if
				test="${ptLstSetupDVo.atrbId == 'rescNm'}"
			><a href="javascript:top.openUserDetl('${orUserMap.userUid}')" title="<u:msg titleId='or.jsp.listUser.modifyUser' alt='사용자 수정 - 팝업'
				/>"><u:convertMap srcId="orUserMap" attId="${ptLstSetupDVo.atrbId}" type="html" /></a></c:if><c:if
				test="${ptLstSetupDVo.atrbId == 'email' or ptLstSetupDVo.atrbId == 'extnEmail'}"
			><a href="javascript:top.mailToPop('<u:convertMap srcId="orUserMap" attId="${ptLstSetupDVo.atrbId}" type="script"
				/>')" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop'
				/>"><u:convertMap srcId="orUserMap" attId="${ptLstSetupDVo.atrbId}" type="html" /></a></c:if><c:if
				test="${ptLstSetupDVo.atrbId != 'rescNm' and ptLstSetupDVo.atrbId != 'email' and ptLstSetupDVo.atrbId != 'extnEmail'}"
			><u:convertMap srcId="orUserMap" attId="${ptLstSetupDVo.atrbId}" type="html"
			/></c:if></td></c:if></c:forEach>
	</tr>
</c:forEach>
</u:listArea>
</form>

<u:pagination />

<u:buttonArea>
	<u:button href="javascript:setSecu();" id="btnPopPw" titleId="or.btn.bulkSecu" alt="일괄 보안 설정" auth="A" />
	<u:button href="javascript:popPw();" id="btnPopPw" titleId="pt.jsp.setPw.title" alt="비밀번호 변경" auth="A" />
	<u:button href="javascript:changeStatCd();" id="changeStatCd" titleId="or.btn.changeStatCd" alt="상태코드 변경" auth="A" />
	<u:button href="javascript:excelDownFile();" id="changeStatCd" titleId="cm.btn.excelDown" alt="엑셀다운" auth="A" />
</u:buttonArea>
