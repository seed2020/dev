<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="paramCompId" test="${not empty param.compId}"
	value="${param.compId}" elseValue="${sessionScope.userVo.compId}"
/><u:secu auth="A"><u:set test="${true}" var="hasAuthA" value="Y" /></u:secu
><u:secu auth="SYS"><u:set test="${true}" var="hasAuthSys" value="Y" /></u:secu>
<script type="text/javascript">
<!--<%
// 이관 버튼 %><c:if test="${(not empty apTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001'}">
function openTranPop(){
	dialog.open('setTranDialog','<u:msg titleId="dm.cols.doc.transfer" alt="문서이관" />','./setTranPop.do?menuId=${menuId}&compId=${paramCompId}');
}</c:if><%
// 이관 진행 내역 조회용 Timeout %>
var gTranProgressTimeout = null;<%
// 이관 진행 내역 팝업 - 진행상태 클릭 하면 %>
function openTranProcPop(tranId){
	if(tranId==null || tranId=='') return;
	
	dialog.open('listTranProcDialog','<u:msg titleId="ap.jsp.tran.process" alt="이관 진행 내역" />','./listTranProcPop.do?menuId=${menuId}&tranId='+tranId);
	dialog.onClose('listTranProcDialog', function(){
		if(gTranProgressTimeout != null){
			window.clearTimeout(gTranProgressTimeout);
			gTranProgressTimeout = null;
		}
	});
}<%
// 저장소 조회/수정 %>
function openStorPop(storId){
	dialog.open('setStorDialog','<u:msg titleId="ap.jsp.setStorage" alt="저장소 관리" />','./setStorPop.do?menuId=${menuId}&compId=${paramCompId}&storId='+storId);
}<%
// 회사 콤보 변경 - 타이틀 옆 %>
function onCompChange(obj){
	reload('${uri}?menuId=${menuId}&compId='+$(obj).val());
}<%
// 시스템 차단 / 차단 해제 %><c:if test="${sessionScope.userVo.userUid eq 'U0000001'}">
function setSysHalt(cmd){
	callAjax("./setSysHaltAjx.do?menuId=${menuId}", {cmd:cmd}, function(data){
		if(data.message!=null) alert(data.message);
	});
}</c:if>
$(document).ready(function() {
	setUniformCSS();
	openTranProcPop('${processingTranId}');
});
//-->
</script>
<c:if test="${!empty ptCompBVoList }">
<div class="front notPrint">
	<div class="front_left">		
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px"><u:title menuNameFirst="true" /></td>
			<td class="width5"></td>
				<td class="frontinput">
					<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> onchange="onCompChange(this);">
						<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
							<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${paramCompId}"/>
						</c:forEach>
					</select>
				</td>
	 		</tr>
		</table>
	</div>
</div>
</c:if>
<c:if test="${empty ptCompBVoList }"><u:title menuNameFirst="true" /></c:if>
<% // 검색영역 %>
<u:searchArea>
<form id="searchForm" name="searchForm" action="${_uri}" >
<u:input type="hidden" id="menuId" value="${menuId}" /><c:if
	test="${!empty param.compId}">
<u:input type="hidden" id="compId" value="${param.compId}" /></c:if>
<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="search_tit"><u:msg titleId="dm.cols.storNm" alt="저장소명" /></td>
					<td><u:input id="schWord" maxByte="50" name="schWord" value="${param.schWord}" titleId="cols.schWord" style="width:250px" /></td>
				</tr>
			</table>
		</td>
		<td>
			<div class="button_search">
				<ul>
					<li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li>
				</ul>
			</div>
		</td>
	</tr>
</table>
</form>
</u:searchArea>
<% // 목록 %>
<u:listArea colgroup="30%,15%,25%,15%,15%" id="listArea">
	<tr id="headerTr">
		<td class="head_ct"><u:msg titleId="dm.cols.storNm" alt="저장소명" /></td>
		<td class="head_ct"><u:msg titleId="ap.cols.tgtCnt" alt="대상 건수" /></td>
		<td class="head_ct"><u:msg titleId="cols.prd" alt="기간" /></td>
		<td class="head_ct"><u:msg titleId="ap.cols.tranDt" alt="이관 일시" /></td>
		<td class="head_ct"><u:msg titleId="ap.cols.prcStat" alt="진행 상태" /></td>
	</tr>
	<c:choose>
		<c:when test="${!empty apTranBVoList}">
			<c:forEach var="apTranBVo" items="${apTranBVoList}" varStatus="status">
				<tr>
					<td class="body_ct"><c:if
						test="${not empty hasAuthA}"><a href="javascript:openStorPop('${apTranBVo.storId}');">${apTranBVo.storRescNm}</a></c:if><c:if
						test="${empty hasAuthA}">${apTranBVo.storRescNm}</c:if></td>
					<td class="bodybg_ct"><u:out value="${apTranBVo.allTgtCnt}" type="number" /></td>
					<td class="bodybg_ct"><u:out value="${apTranBVo.tranTgtStrtYmd}" type="shortdate"
						/> ~ <u:out value="${apTranBVo.tranTgtEndYmd}" type="shortdate" /></td>
					<td class="bodybg_ct"><u:out value="${apTranBVo.tranStrtDt}" type="longdate" /></td>
					<td class="bodybg_ct"><c:if
						test="${(not empty apTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001'}"
						><a href="javascript:openTranProcPop('${apTranBVo.tranId}')"><u:msg titleId="ap.tran.${apTranBVo.tranProcStatCd}" alt="preparing:준비중, processing:진행중, completed:완료, error:에러" /></a></c:if><c:if
						test="${not((not empty apTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001')}"
						><u:msg titleId="ap.tran.${apTranBVo.tranProcStatCd}" alt="preparing:준비중, processing:진행중, completed:완료, error:에러" /></c:if></td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
</u:listArea>
<u:pagination  />
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea"><c:if
	test="${sessionScope.userVo.userUid eq 'U0000001'}">
<u:button titleId="cm.btn.sysHalt" alt="시스템 차단" href="javascript:setSysHalt('halt');" auth="SYS" /></c:if><c:if	
	test="${(not empty apTranEnable and not empty hasAuthSys and not empty sysHalt) or sessionScope.userVo.userUid eq 'U0000001'}">
<u:button titleId="cm.btn.sysContinuation" alt="시스템 차단 해제" href="javascript:setSysHalt('continuation');" auth="SYS" /></c:if><c:if	
	test="${(not empty apTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001'}">
<u:button titleId="ap.btn.transfer" alt="이관" href="javascript:openTranPop();" auth="SYS" /></c:if>
</u:buttonArea>	