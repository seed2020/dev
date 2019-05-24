<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="reprtNo"/>
<script type="text/javascript">
<!--<%// 부서 선택 %><c:if test="${isAdmin==true || isAuthMgm==true }">
function openOrgPop(id){
	var data = [];
	var val=$('#searchForm input[id="orgId"]').val();
	var param={};
	if(val!='') param['orgId']=val;//data.push({orgId:val});
	<c:if test="${isAuthMgm==true}">param['downward']='${sessionScope.userVo.orgId}';</c:if>
	parent.searchOrgPop(param, function(orgVo){
		if(orgVo!=null){
			$('#searchForm input[id="orgId"]').val(orgVo.orgId);
			$('#searchForm input[id="orgNm"]').val(orgVo.rescNm);
		}
	});
}</c:if><% // [목록:제목] - 상세보기 화면 이동 %>
function viewPage(id){
	location.href = './${viewPage}.do?${paramsForList}&reprtNo=' + id;
};<% // [팝업] 파일목록 조회 %>
function viewFileListPop(id) {
	var url = './viewFileListPop.do?${paramsForList }&reprtNo='+id;
	parent.dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />', url);
};<% // [하단버튼:삭제] 삭제 %>
function delWorkReprt() {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./${transDelPage}Ajx.do?menuId=${menuId}', {reprtNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './${listPage}.do?${paramsForList}';
			}
		});
	}
};<% //이벤트 버블 방지 %>
function notEvtBubble(event){
	if(event.stopPropagation) event.stopPropagation(); //MOZILLA
	else event.cancelBubble = true; //IE
}
$(document).ready(function() {
	setUniformCSS();
	$('#listArea tbody:first tr a').click(function(event){
		if(event.stopPropagation) event.stopPropagation(); //MOZILLA
		else event.cancelBubble = true; //IE
	});
});
//-->
</script>

<u:title menuNameFirst="true"/>

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" id="searchForm" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="schInitial" value="${param.schInitial}" />
	
	<table class="search_table" id="searchFormTbl1" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<u:option value="subj" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
			<u:option value="cont" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 200px;" onkeydown="if (event.keyCode == 13) searchForm.submit();" /></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.regDt" alt="등록일시"/><u:input type="hidden" id="durCat" value="regDt"/></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><u:calendar id="durStrtDt" option="{end:'durEndDt'}" value="${param.durStrtDt}" /></td>
					<td class="search_body_ct"> ~ </td>
					<td><u:calendar id="durEndDt" option="{start:'durStrtDt'}" value="${param.durEndDt}" /></td>
				</tr>
			</table>
		</td><c:if test="${isAdmin==true || isAuthMgm==true }"><td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.dept" alt="부서"/></td>
		<td><table border="0" cellpadding="0" cellspacing="0">
				<tbody><tr><td>
						<u:input type="hidden" id="orgId" value="${param.orgId}"
						/><u:input id="orgNm" titleId="cols.dept" readonly="Y" value="${param.orgNm }"
						/></td>
					<td><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="openOrgPop();" /></td></tr>
				</tbody>
			</table></td><td><u:checkArea><u:checkbox id="withSubYn" name="withSubYn" value="Y" titleId="or.check.withSub" checkValue="${param.withSubYn}"/></u:checkArea></td></c:if>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr><tr>
			<td colspan="2"><u:buttonS href="javascript:;" onclick="valueReset('searchFormTbl1',['durCat']);" titleId="cm.btn.srch.reset" alt="검색조건 초기화" /></td>
			<td class="width20"></td>			
		</tr>
	</table>
	</form>
</u:searchArea>

<u:listArea id="listArea" colgroup="3%,12%,,13%,13%,10%">	
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
		<th class="head_ct"><u:msg titleId="cols.dept" alt="부서"/></th>
		<th class="head_ct"><u:msg titleId="cols.subj" alt="제목"/></th>
		<th class="head_ct"><u:msg titleId="cols.regr" alt="등록자"/></th>
		<th class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시"/></th>
		<th class="head_ct"><u:msg titleId="cols.att" alt="첨부"/></th>
	</tr>
	<c:if test="${fn:length(wjWorkReprtBVoList) == 0}">
		<tr>
		<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(wjWorkReprtBVoList)>0}">
		<c:forEach items="${wjWorkReprtBVoList}" var="wjWorkReprtBVo" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' onclick="viewPage('${wjWorkReprtBVo.reprtNo}')">
				<td class="bodybg_ct"><input type="checkbox" value="${wjWorkReprtBVo.reprtNo }" onclick="notEvtBubble(event);"/></td>
				<td class="body_ct"><u:out value="${wjWorkReprtBVo.orgRescNm }"/></td>
				<td class="body_lt"><u:out value="${wjWorkReprtBVo.subj }"/></td>
				<td class="body_ct"><a href="javascript:viewUserPop('${wjWorkReprtBVo.regrUid }');">${wjWorkReprtBVo.regrNm }</a></td>
				<td class="body_ct"><u:out value="${wjWorkReprtBVo.regDt }" type="longdate"/></td>
				<td class="body_ct"><c:if test="${wjWorkReprtBVo.fileCnt>0}"><a href="javascript:viewFileListPop('${wjWorkReprtBVo.reprtNo}');"><u:icon type="att" /></a></c:if></td>
			</tr>
		</c:forEach>
	</c:if>
	
</u:listArea>
<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.write" alt="등록" href="./${setPage }.do?menuId=${menuId}" auth="W" />
<u:secu auth="A"><u:button titleId="cm.btn.del" alt="삭제" href="javascript:delWorkReprt();" /></u:secu>
</u:buttonArea>
