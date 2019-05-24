<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function view(mbshId) {
	dialog.open('viewMngJoinReqsPop','<u:msg titleId="ct.cols.reqsJoinOk" alt="가입신청 승인" />','./viewMngJoinReqsPop.do?menuId=${menuId}&ctId=${ctId}&mbshId='+mbshId);
}

function apvd(mbshCtId,mbshId){
	//var $mbshCtId = $("#mbshCtId").val();
	if (confirmMsg("ct.cfrm.mbshApvd")) {
		callAjax('./transMbshApvd.do?menuId=${menuId}', {mbshCtId:mbshCtId, mbshId:mbshId}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listMngJoinReqs.do?menuId=${menuId}&ctId='+mbshCtId;
			}
		});
	}else{
		return;
	}
}

function napvd(mbshCtId,mbshId){
	//var $mbshCtId = $("#mbshCtId").val();
	if (confirmMsg("ct.cfrm.mbshNapvd")) {
		callAjax('./transMbshNapvd.do?menuId=${menuId}', {mbshCtId:mbshCtId, mbshId:mbshId}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listMngJoinReqs.do?menuId=${menuId}&ctId='+mbshCtId;
			}
		});
	}else{
		return false;
	}
}

$(document).ready(function() {
	setUniformCSS();
});

//-->
</script>

<u:title title="${menuTitle }"  alt="가입신청 관리" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listMngJoinReqs.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="ctId" value="${ctId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
				<u:option value="mbshNmOpt" titleId="ct.cols.userNm" alt="이름" checkValue="${param.schCat}" />
				<u:option value="mbshDeptNmOpt" titleId="ct.cols.deptNm" alt="부서" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<u:listArea id="listArea">
	<tr>
	<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.reqsDt" alt="신청일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.dftAuth" alt="기본권한" /></td>
	<td width="110px" class="head_ct"><u:msg titleId="ct.option.ctStat02" alt="승인" /></td>
	</tr>

	
	<c:forEach items="${ctMngJoinReqsList}" var="ctJoinList" varStatus="status" >
		<c:if test="${ctJoinList.joinStat == '1'}" >
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="bodybg_ct">${recodeCount - ctJoinList.rnum + 1}</td>
			<td class="body_lt"><a href="javascript:viewUserPop('${ctJoinList.userUid}');">${ctJoinList.userNm}</a></td>
			<td class="body_ct">${ctJoinList.deptNm}</td>
			<fmt:parseDate var="dateTempParse" value="${ctJoinList.joinDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			<td class="body_ct"><fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/></td>
			
			<c:choose>
				<c:when test="${ctJoinList.userSeculCd == 'S'}"> 
					<!-- <c:set var="joinState"	value= "<u:msg titleId='ct.option.mbshLev1' alt='스텝'/>" />-->
					<c:set var="joinState"	value="스텝"/>
				</c:when>
				<c:when test="${ctJoinList.userSeculCd == 'R'}"> 
					<!-- <c:set var="joinState"	value="<u:msg titleId='ct.option.mbshLev2' alt='정회원'/>"/> -->
					<c:set var="joinState"	value="정회원"/>
				</c:when>
				<c:when test="${ctJoinList.userSeculCd == 'A'}"> 
					<!-- <c:set var="joinState"	value="<u:msg titleId='ct.option.mbshLev3' alt='준회원'/>"/> -->
					<c:set var="joinState"	value="준회원"/>
				</c:when>
			</c:choose>
			<td class="body_ct">${joinState}</td>
			
			<td class="body_ct" align="center">
				<u:buttonArea >
					<u:button onclick="javascript:apvd('${ctJoinList.ctId}','${ctJoinList.userUid}');" titleId="cm.btn.apvd" alt="승인"  />
					<u:button onclick="javascript:napvd('${ctJoinList.ctId}','${ctJoinList.userUid}');" titleId="cm.btn.napvd" alt="미승인" />
				</u:buttonArea>
			</td>
			</tr>
		</c:if>
	</c:forEach>
	
	
	<c:if test="${fn:length(ctMngJoinReqsList) == 0}">
		<tr>
		<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
</u:listArea>

<u:pagination />

