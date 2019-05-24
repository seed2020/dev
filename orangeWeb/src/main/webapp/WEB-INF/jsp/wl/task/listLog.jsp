<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="paramsForList" excludes="logNo"/>
<script type="text/javascript">
<!--<c:if test="${isAdmin == true}"><% // 검색조건 등록자 선택 %>
function schUserPop() {
	var $view = $("#searchForm");
	var data = {userUid:$view.find("#schUserUid").val()};<% // 팝업 열때 선택될 데이타 %>
	<% // option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$view.find("#schUserUid").val(userVo.userUid);
			$view.find("#schUserNm").val(userVo.rescNm);
		}else{
			return false;
		}
	});
}<%// 부서 선택 %>
function schOrgPop(){
	var data = [];
	searchOrgPop({data:data}, function(orgVo){
		if(orgVo!=null){
			$('#schOrgId').val(orgVo.orgId);
			$('#schOrgNm').val(orgVo.rescNm);
		}
	});
}
</c:if>
<% // [하단버튼:취합] 파일목록 조회 %>
function setMerge(id) {
	var arr = getCheckedValue("listArea", null);<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	var url='./setRecv.do?${paramsForList}';
	if(arr!=null && arr.length>0)
		url+='&selLogNos='+arr.join(',');
	location.href = url;
}
<% // [하단버튼:보고자선택] - 팝업 %>
function setReprtUserPop(id) {
	dialog.open('setReprtUserDialog','<u:msg titleId="wl.btn.user.select" alt="보고자선택" />','./setReprtUserPop.do?menuId=${menuId}');
}<% // 탭 클릭 - 일일:/주간:/월간:/연간: %>
function toggleTabBtn(typCd){
	var $form = $("#searchForm");
	$form.find('input[type="text"]').val('');
	$form.find("input[name='typCd']").remove();
	$form.appendHidden({name:'typCd',value:typCd});
	$form.submit();
}<% // [팝업] 파일목록 조회 %>
function viewFileListPop(id) {
	var url = './viewFileListPop.do?${paramsForList }&logNo='+id;
	parent.dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />', url);
}<% // [하단버튼:삭제] 삭제 %>
function delLog() {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./${transDelPage}Ajx.do?menuId=${menuId}', {logNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './${listPage}.do?${paramsForList}';
			}
		});
	}
}<%//  정보 관리 항목 %>
var gAttrs = ["logNo", "subj", "regrNm", "reprtDt"];<%// 선택된 목록 리턴 - uncheck:리턴할 때 선택을 해제함 %>
function getSelectedLogs(uncheck){
	var arr = [], $me, obj, tr, id;
	$("#listArea input[type=checkbox]:checked").each(function(){
		tr = getParentTag(this, 'tr');
		id = $(tr).attr('id');
		if(id=='headerTr' || id=='hiddenTr') return true;
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
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<c:if test="${empty pageSuffix}"><u:msg var="menuTitle" titleId="wl.jsp.${path }.title" />
<u:title title="${menuTitle }" menuNameFirst="true" alt="${menuTitle }" /></c:if>

<c:if test="${empty pageSuffix && fn:length(tabList) > 1}">
<u:tabGroup noBottomBlank="${true}">
	<c:forEach var="tab" items="${tabList }" varStatus="status">
		<u:tab alt="목록전체" titleId="wl.cols.typCd.${tab }"
		on="${(!empty typCd && typCd == tab) || (empty param.typCd && empty typCd && status.first) || param.typCd == tab}"
		onclick="toggleTabBtn('${tab }');" />
	</c:forEach>
</u:tabGroup>
<u:blank />
</c:if>
	
<% // 검색영역 %>
<u:searchArea>
	<form id="searchForm" name="searchForm" action="./${listPage }.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="typCd" value="${param.typCd}" />
	<c:if test="${!empty param.pageRowCnt }"><u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<c:if test="${!empty param.multi }"><u:input type="hidden" id="multi" value="${param.multi}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
		<u:option value="SUBJ" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
		<u:option value="CONT" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" /></select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm.submit();" /></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.prd" alt="기간" /></td>
		<td><u:input type="hidden" id="durCat" value="reprtDt" />
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="durStrtDt" option="{end:'durEndDt'}" value="${!empty durMap ? durMap.durStrtDt : param.durStrtDt}" /></td>
			<td class="search_body_ct"> ~ </td>
			<td><u:calendar id="durEndDt" option="{start:'durStrtDt'}" value="${!empty durMap ? durMap.durEndDt : param.durEndDt}" /></td>
			</tr>
			</table></td><c:if test="${path eq 'recv' }"><td class="width20"></td><td class="search_tit"><u:msg titleId="wl.jsp.group.reprtGrp.title" alt="보고대상그룹" /></td><td>
			<select name="grpNo" style="min-width:100px;"><u:option value="" titleId="cm.option.all" alt="전체" selected="${empty wlTaskLogBVo.grpNo }"
		/><c:forEach var="list" items="${reprtGrpList }" varStatus="status"><u:option value="${list.grpNo }" title="${list.grpNm }" alt="${list.grpNm }" checkValue="${param.grpNo }"
		/></c:forEach></select></td></c:if>
		</tr><c:if test="${isAdmin == true }"><tr>
		<td><select name="schOptCat">
		<u:option value="REGRUID" titleId="cols.regr" alt="등록자" checkValue="${param.schOptCat}" />
		<u:option value="REPRTUID" titleId="wl.cols.reprtTgt" alt="보고대상" checkValue="${param.schOptCat}" /></select></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td><u:input id="schUserNm" value="${param.schUserNm}" titleId="cols.pich" readonly="Y" />
				<u:input type="hidden" id="schUserUid" value="${param.schUserUid}" /></td>
			<td><u:buttonS titleId="cm.btn.search" alt="검색" onclick="schUserPop();" /></td>
			</tr>
			</tbody></table></td>
		<td></td>
			<td class="search_tit"><u:msg titleId="cols.dept" alt="부서" /></td>
			<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
				<tr>
				<td><u:input id="schOrgNm" value="${param.schOrgNm}" titleId="cols.pich" readonly="Y" />
					<u:input type="hidden" id="schOrgId" value="${param.schOrgId}" /></td>
				<td><u:buttonS titleId="cm.btn.search" alt="검색" onclick="schOrgPop();" /></td>
				</tr>
				</tbody></table></td><td class="width20"></td>
		<td class="search_tit"><u:msg titleId="wl.jsp.task.title" alt="업무일지" /></td>
		<td><select name="consolYn"><u:option value="" titleId="cm.option.all" alt="전체" selected="${empty param.consolYn }"
		/><u:option value="N" titleId="wl.cols.deptScope.log" alt="개별" checkValue="${param.consolYn}" 
		/><u:option value="Y" titleId="wl.cols.deptScope.consol" alt="취합" checkValue="${param.consolYn}" 
		/></select></td></tr></c:if>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>
<c:if test="${path eq 'recv' && !empty notReprtUserList}">
<u:set var="paramTypCd" test="${!empty typCd }" value="${typCd }" elseValue="${param.typCd }"/>
<div class="color_txt">※ <u:msg titleId="wl.jsp.not.reprtUser.title" alt="보고하지 않은 사용자" /> <c:if test="${!empty paramTypCd && paramTypCd ne 'all'}">(<u:out value="${durMap.durStrtDt }" type="date"/>~<u:out value="${durMap.durEndDt }" type="date"/>)</c:if></div>
<div class="color_txt"><div id="userSelectArea" style="min-height:40px;"><c:forEach 
		var="reprtUser" items="${notReprtUserList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title_view" style="font-weight:bold;"><a href="javascript:viewUserPop('${reprtUser[0] }');">${reprtUser[1] }</a></dd></dl></div>
		</c:forEach></div></div>
<u:blank />
</c:if>
<c:set var="chkYn" value="N"/>
<c:set var="colspan" value="3"/>
<c:set var="colgroup" value=",15%,15%"/>
<c:if test="${path eq 'recv' || path eq 'consol' || path eq 'dept' || isAdmin == true}">
<c:set var="colspan" value="${colspan+1 }"/>
<c:set var="colgroup" value=",12%,15%,15%"/>
</c:if>
<c:if test="${empty pageSuffix && (empty param.typCd || param.typCd eq 'all')}">
<c:set var="colspan" value="${colspan+1 }"/>
<c:set var="colgroup" value="${colgroup },10%"/>
</c:if>
<c:if test="${path eq 'log' || path eq 'recv' || (!empty param.multi && param.multi eq 'Y')}">
<c:set var="chkYn" value="Y"/>
<c:set var="colspan" value="${colspan+1 }"/>
<c:set var="colgroup" value="3%,${colgroup }"/>
</c:if>
<c:if test="${fileYn eq 'Y' }">
<c:set var="colspan" value="${colspan+1 }"/>
<c:set var="colgroup" value="${colgroup },10%"/>
</c:if>
<u:listArea id="listArea" colgroup="${colgroup }">
	<tr id="headerTr">
		<c:if test="${chkYn eq 'Y'}"><td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td></c:if>
		<th class="head_ct"><u:msg titleId="cols.subj" alt="제목"/></th>
		<c:if test="${path eq 'recv' || path eq 'consol' || path eq 'dept' || isAdmin == true}"><th class="head_ct"><u:msg titleId="cols.regr" alt="등록자"/></th></c:if>
		<th class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시"/></th>
		<th class="head_ct"><u:msg titleId="wl.cols.reprtDt" alt="보고일자"/></th>
		<c:if test="${empty pageSuffix && (empty param.typCd || param.typCd eq 'all')}"><th class="head_ct"><u:msg titleId="wl.cols.typCd" alt="종류"/></th></c:if>
		<c:if test="${fileYn eq 'Y' }"><th class="head_ct"><u:msg titleId="cols.att" alt="첨부"/></th></c:if>
	</tr>
	<c:if test="${fn:length(wlTaskLogBVoList) == 0}">
		<tr>
		<td class="nodata" colspan="${colspan }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(wlTaskLogBVoList)>0}">
		<c:forEach items="${wlTaskLogBVoList}" var="wlTaskLogBVo" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<c:if test="${chkYn eq 'Y'}"><td class="bodybg_ct"><input type="checkbox" value="${wlTaskLogBVo.logNo }" data-logNo="${wlTaskLogBVo.logNo }" data-subj="${wlTaskLogBVo.subj }" data-regrNm="${wlTaskLogBVo.regrNm }" data-regDt="<u:out value="${wlTaskLogBVo.regDt }" type="date"/>" data-reprtDt="<u:out value="${wlTaskLogBVo.reprtDt }" type="date"/>"/></td></c:if>
				<td class="body_lt" id="subj"><a href="./${viewPage }.do?${paramsForList}&logNo=${wlTaskLogBVo.logNo }"><u:out value="${wlTaskLogBVo.subj }"/></a></td>
				<c:if test="${path eq 'recv' || path eq 'consol' || path eq 'dept' || isAdmin == true}"><td class="body_ct" id="regrNm"><a href="javascript:viewUserPop('${wlTaskLogBVo.regrUid }');"><u:out value="${wlTaskLogBVo.regrNm }" /></a></td></c:if>
				<td class="body_ct" id="regDt"><u:out value="${wlTaskLogBVo.regDt }" type="date"/></td>
				<td class="body_ct" id="reprtDt"><u:out value="${wlTaskLogBVo.reprtDt }" type="date"/></td>
				<c:if test="${empty pageSuffix && (empty param.typCd || param.typCd eq 'all')}"><td class="body_ct"><u:msg titleId="wl.cols.typCd.${wlTaskLogBVo.typCd }"/></td></c:if>
				<c:if test="${fileYn eq 'Y' }"><td class="body_ct"><c:if test="${wlTaskLogBVo.fileCnt>0}"><a href="javascript:viewFileListPop('${wlTaskLogBVo.logNo}');"><u:icon type="att" /></a></c:if></td></c:if>
			</tr>
		</c:forEach>	
	</c:if>
	
</u:listArea>
<u:pagination />
<% // 하단 버튼 %>
<u:buttonArea>
<c:if test="${path eq 'log' }">
<c:if test="${isAdmin == false}"><u:button titleId="cm.btn.write" alt="등록" href="./${setPage }.do?${paramsForList }" auth="W" /></c:if>
<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delLog();" />
</c:if>
<c:if test="${path eq 'recv'}">
<u:button titleId="wl.btn.user.select" alt="보고자선택" onclick="setReprtUserPop();" auth="W" />
<u:button titleId="wl.btn.consol" alt="취합" onclick="setMerge();" auth="W" />
</c:if>
</u:buttonArea>
