<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:secu auth="SYS"><u:set test="${true}" var="hasAuthSys" value="Y" /></u:secu>
<script type="text/javascript">
<!--<%
// 이관 버튼 %><c:if test="${(not empty dmTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001'}">
function openTranPop(){
	dialog.open('setTranDialog','<u:msg titleId="dm.cols.doc.transfer" alt="문서이관" />','./setTranPop.do?menuId=${menuId}&paramCompId=${paramCompId}');
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
	dialog.open('setStorDialog','<u:msg titleId="ap.jsp.setStorage" alt="저장소 관리" />','./setStorPop.do?menuId=${menuId}&paramCompId=${paramCompId}&storId='+storId);
}<%
// 회사 콤보 변경 - 타이틀 옆 %>
function onCompChange(obj){
	reload('${uri}?menuId=${menuId}&paramCompId='+$(obj).val());
}<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
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
};<%// 선택목록 리턴 %>
function fnSelArrs(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArrs(arr);
	else return null;
};<%// 배열에 담긴 목록%>
function selRowInArrs(rowArr){
	var objArr = [], $tranId;
	for(var i=0;i<rowArr.length;i++){
		$tranId = $(rowArr[i]).find("input[name='tranId']");
		if($tranId.val()!=''){
			objArr.push($tranId.val());
		}
	}
	return objArr;
};<% // [하단버튼:삭제] 삭제 %>
function delTran() {
	var tranIds = fnSelArrs();
	if(tranIds == null || tranIds.length==0) return;
	if (confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transTranDelAjx.do?menuId=${menuId}&paramCompId=${paramCompId}', {tranIds:tranIds,delYn:'Y'}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
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
					<select id="paramCompId" name="paramCompId" <u:elemTitle titleId="cols.comp" /> onchange="onCompChange(this);">
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
	test="${!empty paramCompId}">
<u:input type="hidden" id="paramCompId" value="${paramCompId}" /></c:if>
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
<u:set var="colgroup" test="${(not empty dmTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001'}" value="3%,30%,15%,25%,15%,15%" elseValue="30%,15%,25%,15%,15%"/>
<% // 목록 %>
<u:listArea colgroup="${colgroup }" id="listArea">
	<tr id="headerTr">
		<c:if test="${(not empty dmTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001'}"><td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td></c:if>
		<td class="head_ct"><u:msg titleId="dm.cols.storNm" alt="저장소명" /></td>
		<td class="head_ct"><u:msg titleId="ap.cols.tgtCnt" alt="대상 건수" /></td>
		<td class="head_ct"><u:msg titleId="cols.prd" alt="기간" /></td>
		<td class="head_ct"><u:msg titleId="ap.cols.tranDt" alt="이관 일시" /></td>
		<td class="head_ct"><u:msg titleId="ap.cols.prcStat" alt="진행 상태" /></td>
	</tr>
	<c:choose>
		<c:when test="${!empty dmTranBVoList}">
			<c:forEach var="dmTranBVo" items="${dmTranBVoList}" varStatus="status">
				<tr>
					<c:if test="${(not empty dmTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001'}"><td class="bodybg_ct"><u:checkbox name="listChk" value="${dmTranBVo.tranId }" checked="false" /><u:input type="hidden" name="tranId" value="${dmTranBVo.tranId }"/></td></c:if>
					<td class="body_ct">${dmTranBVo.storRescNm}</td>
					<td class="bodybg_ct"><u:out value="${dmTranBVo.docTgtCnt}" type="number" /></td>
					<td class="bodybg_ct"><u:out value="${dmTranBVo.tranTgtStrtYmd}" type="shortdate"
						/> ~ <u:out value="${dmTranBVo.tranTgtEndYmd}" type="shortdate" /></td>
					<td class="bodybg_ct"><u:out value="${dmTranBVo.tranStrtDt}" type="longdate" /></td>
					<td class="bodybg_ct"><c:if
						test="${(not empty dmTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001'}"
						><a href="javascript:openTranProcPop('${dmTranBVo.tranId}')"><u:msg titleId="ap.tran.${dmTranBVo.tranProcStatCd}" alt="preparing:준비중, processing:진행중, completed:완료, error:에러" /></a></c:if><c:if
						test="${not((not empty dmTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001')}"
						><u:msg titleId="ap.tran.${dmTranBVo.tranProcStatCd}" alt="preparing:준비중, processing:진행중, completed:완료, error:에러" /></c:if></td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="${(not empty dmTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001' ? 6 : 5}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
</u:listArea>
<u:pagination  />
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea"><c:if
	test="${sessionScope.userVo.userUid eq 'U0000001'}">
<u:button titleId="cm.btn.sysHalt" alt="시스템 차단" href="javascript:setSysHalt('halt');" auth="SYS" /></c:if><c:if	
	test="${(not empty dmTranEnable and not empty hasAuthSys and not empty sysHalt) or sessionScope.userVo.userUid eq 'U0000001'}">
<u:button titleId="cm.btn.sysContinuation" alt="시스템 차단 해제" href="javascript:setSysHalt('continuation');" auth="SYS" /></c:if><c:if	
	test="${(not empty dmTranEnable and not empty hasAuthSys) or sessionScope.userVo.userUid eq 'U0000001'}">
<u:button titleId="ap.btn.transfer" alt="이관" href="javascript:openTranPop();" auth="SYS" /></c:if>
<u:button titleId="cm.btn.del" alt="삭제" onclick="delTran();" auth="SYS" />
</u:buttonArea>	