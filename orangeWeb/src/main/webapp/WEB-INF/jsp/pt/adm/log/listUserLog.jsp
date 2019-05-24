<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="nonPageParams" excludes="pageNo"/>
<script type="text/javascript">
<!--<%
//1명의 사용자 선택 %>
function schUserPop(){
	var data = {};<%// 팝업 열때 선택될 데이타 %>
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data,compId:$('#compId > option:selected').val()}, function(userVo){
		$('#schUserUid').val(userVo.userUid);
		$('#schUserNm').val(userVo.rescNm);
	});
}<c:if test="${sessionScope.userVo.userUid eq 'U0000001'}">
function schUserPop2(){
	searchUserPop({}, function(userVo){
		if(userVo!=null){
			location.href = "/cm/login/processAdurSwitch.do?userUid="+userVo.userUid;
		}
	});
}</c:if><%
// 설정 팝업 %>
function setUserLogSetupPop(){
	dialog.open('setUserLogSetupPop', '<u:msg titleId="pt.btn.delSetup" alt="삭제 설정" />', './setUserLogSetupPop.do?menuId=${menuId}');
}<%
// 선택삭제%>
function fnDelete(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) delRowInArr(arr);
}<%
// 전체삭제%>
function fnAllDelete(){
	if(confirmMsg("cm.cfrm.del")) {
		var $form = $('#deleteForm');
		$form.attr('method','post');
		$form.attr('action','./transUserLogAllDel.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		var compId = $('#compId > option:selected').val();
		$form.find('#delCompId').val(compId);
		$form[0].submit();
	}
}<%
//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}<%
// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	var delArr = [], $userUid,$sessionId;
	//if(delVa!='') delArr.push(delVa);
	for(var i=0;i<rowArr.length;i++){
		$userUid = $(rowArr[i]).find("input[name='userUid']");
		$sessionId = $(rowArr[i]).find("input[name='sessionId']");
		if($userUid.val()!='' && $sessionId.val()!=''){
			delArr.push($userUid.val()+":"+$sessionId.val());
		}
	}
	$("#delList").val(delArr.join(','));
	
	if(confirmMsg("cm.cfrm.del")) {
		var $form = $('#deleteForm');
		$form.attr('method','post');
		$form.attr('action','./transUserLogDel.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="wr.jsp.listResc.title" alt="사용자접속이력" menuNameFirst="true" ><c:if test="${sessionScope.userVo.userUid eq 'U0000001'}">
	<u:titleIcon type="plus" titleId="or.jsp.searchUserPop.title" onclick="schUserPop2()" auth="A" /></c:if
></u:title>

<% // 검색영역 %>
<u:searchArea>
<form name="searchForm" action="./listUserLog.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
			<tbody>
			<tr>
				<u:secu auth="SYS" >
				<td class="search_tit"><u:msg titleId="cols.comp" alt="회사" /></td>
				<td>
					<select id="compId" name="compId" >
						<u:option value="" titleId="cm.option.all" alt="전체"/>
						<c:forEach items="${ptCompBVoList}" var="list" varStatus="status">
							<u:option value="${list.compId}" title="${list.compNm}" selected="${param.compId == list.compId}"/>
						</c:forEach>
					</select>
				</td>
				<td class="width30"></td>
				</u:secu>
				<td class="search_tit"><u:msg titleId="cols.user" alt="사용자"/></td>
				<td>
					<u:input type="hidden" id="schUserUid" value="${param.schUserUid}" />
					<u:input id="schUserNm" name="schUserNm" value="${param.schUserNm}" titleId="cols.user" style="width:70px;" readonly="Y" />
					<u:buttonS titleId="cm.btn.choice" alt="선택" href="javascript:;" onclick="schUserPop();"
					/><u:buttonS titleId="cm.btn.del" alt="삭제" href="javascript:;" onclick="$('#schUserUid').val('');$('#schUserNm').val('');"
					/>
				</td>
				<td class="width30"></td>
				<td class="search_tit" ><u:msg titleId="cols.prd" alt="기간" /></td>
				<td>
					<select name="durCat">
						<u:option value="lginDt" titleId="cols.lginDt" alt="로그인일시" checkValue="${param.durCat }" selected="${empty param.durCat }"/>
						<u:option value="lgotDt" titleId="cols.lgotDt" alt="로그아웃일시" checkValue="${param.durCat }"/>
					</select>
				</td>
				<td>
					<u:calendar id="durStrtDt" value="${param.durStrtDt}" />
				</td>
				<td class="search_titx">&nbsp;&nbsp;~</td>
				<td><u:calendar id="durEndDt" value="${param.durEndDt}" /></td>
			</tr>
			</tbody>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
</table>	
</form>
</u:searchArea>
<% // 목록 %>
<c:set var="sysYn" value="N"/>
<u:secu auth="SYS" ><c:set var="sysYn" value="Y"/></u:secu>
<div class="listarea" id="listArea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="3%"/><c:if test="${sysYn eq 'Y' }"><col width="22%"/></c:if><col width="*"/><col width="12%"/><col width="15%"/><col width="15%"/><col width="15%"/></colgroup>
	<tr>
		<td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
		<c:if test="${sysYn eq 'Y' }"><td class="head_ct"><u:msg titleId="cols.compNm" alt="회사명" /></td></c:if>
		<td class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
		<td class="head_ct"><u:msg titleId="cols.userNm" alt="사용자명" /></td>
		<td class="head_ct"><u:msg titleId="cols.lginDt" alt="로그인일시" /></td>
		<td class="head_ct"><u:msg titleId="cols.lgotDt" alt="로그아웃일시" /></td>
		<td class="head_ct"><u:msg titleId="cols.accsIp" alt="접속IP" /></td>
	</tr>
	<c:choose>
		<c:when test="${!empty orUserLginHVoList}">
			<c:forEach var="list" items="${orUserLginHVoList}" varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="bodybg_ct"><u:checkbox name="userCheck" value="${list.userUid }" checked="false" /><u:input type="hidden" name="userUid" value="${list.userUid }"/><u:input type="hidden" name="sessionId" value="${list.sessionId }"/></td>
					<c:if test="${sysYn eq 'Y' }"><td class="body_ct"><div class="ellipsis" title="${list.compNm }">${list.compNm }</div></td></c:if>
					<td class="body_ct"><div class="ellipsis" title="${list.deptRescNm }">${list.deptRescNm }</div></td>
					<td class="body_ct"><div class="ellipsis" title="${list.rescNm }"><a href="javascript:viewUserPop('${list.userUid}');">${list.rescNm}</a></div></td>
					<td class="body_ct"><u:out value="${list.lginDt }" type="longdate"/></td>
					<td class="body_ct"><u:out value="${list.lgotDt }" type="longdate"/></td>
					<td class="body_ct">${list.accsIp }</td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="${sysYn eq 'Y' ? '7' : '6' }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
	</table>
</div>
<u:blank />
<u:pagination />
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.del" alt="삭제" href="javascript:fnDelete();" auth="SYS" />
	<u:button titleId="cm.btn.allDel" alt="전체삭제" href="javascript:fnAllDelete();" auth="SYS" />
	<u:button titleId="pt.btn.delSetup" alt="삭제 설정" href="javascript:;" onclick="setUserLogSetupPop();" auth="SYS" />
</u:buttonArea>
<form id="deleteForm" >
	<u:input type="hidden" id="listPage" value="./listUserLog.do?${nonPageParams }" />
	<u:input type="hidden" name="delList"  id="delList"/>
	<u:input type="hidden" name="delCompId"  id="delCompId"/>
</form>