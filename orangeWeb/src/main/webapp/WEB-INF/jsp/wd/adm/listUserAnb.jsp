<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
/* 
wd.anbTypCd.anb=연차
wd.anbTypCd.nanb=개정연차
wd.anbTypCd.repb=대체휴무
wd.anbTypCd.offb=공가 */
request.setAttribute("anbTypCds", new String[]{"anb","nanb","repb","offb"});

%><script type="text/javascript">
<!--
function viewDetlPop(year, anbTypCd, uid, modTypCd){
	dialog.close("viewDetlDialog");
	var url = "./viewDetlPop.do?menuId=${menuId}&year="+year+"&anbTypCd="+anbTypCd+"&modTypCd="+modTypCd+"&odurUid="+uid;
	var anbTypNm = callMsg('wd.anbTypCd.'+anbTypCd);
	var popTit = '<u:msg titleId="ap.btn.detlInfo" alt="상세정보" /> ( '+year+' / '+ anbTypNm +' )';
	dialog.open("viewDetlDialog", popTit, url);
}
function openUserPop(){
	var data = {userUid:'${param.odurUid}'};
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			location.href = "${_uri}?menuId=${menuId}&odurUid="+userVo.odurUid;
		}
	});
}
$(document).ready(function() {
	setUniformCSS();
	${WD_RELOAD_SCRIPT}
});
//-->
</script>
<u:title alt="사용자별 관리" menuNameFirst="true" />

<div class="blank"></div>
<u:listArea id="listArea" colgroup="25%,25%,25%,25%">
	<tr>
	<td class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td class="head_ct"><u:msg titleId="cols.userNm" alt="사용자명" /> <u:buttonS titleId="cm.btn.sel" alt="선택" onclick="openUserPop();" /></td>
	<td class="head_ct"><u:msg titleId="cols.entraYmd" alt="입사일" /></td>
	<td class="head_ct"><u:msg titleId="or.cols.statCd" alt="상태코드" /></td>
	</tr>
	
	<tr id="tr${orUserBVo.odurUid}">
	<td class="body_ct"><u:out value="${orUserBVo.orgRescNm}" /></td>
	<td class="body_ct"><a href="javascript:viewUserPop('${orUserBVo.userUid}');"><u:out value="${orUserBVo.rescNm}" /></a></td>
	<td class="body_ct"><u:out value="${orUserBVo.entraYmd}" type="shortdate" /></td>
	<td class="body_ct"><u:out value="${orUserBVo.userStatNm}" /></td>
	</tr>
</u:listArea>

<c:if test="${not empty wdFirstYear and not empty wdCount and wdCount > 0}">
<c:forEach begin="0" end="${wdCount - 1}" var="idx" step="1"><u:set
	test="${true}" var="year" value="${wdFirstYear - idx}" />
<u:listArea id="listArea${year}">
	<tr>
	<td width="12%" class="head_ct"><u:msg titleId="wd.year" alt="년도" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="wd.anbTypCds" alt="연차 종류" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="wd.cnt.forw" alt="이월수" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="wd.cnt.cre" alt="발생수" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="wd.cnt.use" alt="사용수" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="wd.cnt.left" alt="잔여" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="wd.cnt.ongo" alt="결재중" /></td>
	</tr><u:set
		test="${true}" var="forwSum" value="0.0" /><u:set
		test="${true}" var="creSum" value="0.0" /><u:set
		test="${true}" var="useSum" value="0.0" /><u:set
		test="${true}" var="remainSum" value="0.0" /><u:set
		test="${true}" var="ongoSum" value="0.0" />
	
	<c:forEach items="${anbTypCds}" var="anbTypCd"><u:convert
		srcId="wd${year}${anbTypCd}" var="wdAnbBVo" /><u:set
		test="${not empty wdAnbBVo}" var="forwSum" value="${forwSum + (wdAnbBVo.forwCnt + wdAnbBVo.forwModCnt)}" elseValue="${forwSum}" /><u:set
		test="${not empty wdAnbBVo}" var="creSum" value="${creSum + (wdAnbBVo.creCnt + wdAnbBVo.creModCnt)}" elseValue="${creSum}" /><u:set
		test="${not empty wdAnbBVo}" var="useSum" value="${useSum + (wdAnbBVo.useCnt + wdAnbBVo.useModCnt)}" elseValue="${useSum}" /><u:set
		test="${not empty wdAnbBVo}" var="remainSum" value="${remainSum +
				(wdAnbBVo.forwCnt + wdAnbBVo.forwModCnt)
				+ (wdAnbBVo.creCnt + wdAnbBVo.creModCnt)
				- (wdAnbBVo.useCnt + wdAnbBVo.useModCnt)}" elseValue="${remainSum}" /><u:set
		test="${not empty wdAnbBVo}" var="ongoSum" value="${ongoSum + (wdAnbBVo.ongoCnt + wdAnbBVo.ongoModCnt)}" elseValue="${ongoSum}" />
		
	<tr id="tr${anbTypCd}" onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="body_ct">${year}</td>
	<td class="body_ct"><u:msg titleId="wd.anbTypCd.${anbTypCd}" /></td>
	<td class="body_ct" onclick="viewDetlPop('${year}','${anbTypCd}','${orUserBVo.odurUid}', 'forw')" style="cursor:pointer;${
		not empty wdAnbBVo.forwModCnt and wdAnbBVo.forwModCnt ne 0.0 ?
		' color:#0F6F8E; font-weight:bold;' : ''}">${not empty wdAnbBVo ? (wdAnbBVo.forwCnt + wdAnbBVo.forwModCnt) : ''}</td><%-- 이월수 --%>
	<td class="body_ct" onclick="viewDetlPop('${year}','${anbTypCd}','${orUserBVo.odurUid}', 'cre')" style="cursor:pointer;${
		not empty wdAnbBVo.creModCnt and wdAnbBVo.creModCnt ne 0.0 ?
		' color:#0F6F8E; font-weight:bold;' : ''}">${not empty wdAnbBVo ? (wdAnbBVo.creCnt + wdAnbBVo.creModCnt) : ''}</td><%-- 발생수 --%>
	<td class="body_ct" onclick="viewDetlPop('${year}','${anbTypCd}','${orUserBVo.odurUid}', 'use')" style="cursor:pointer;${
		not empty wdAnbBVo.useModCnt and wdAnbBVo.useModCnt ne 0.0 ?
		' color:#0F6F8E; font-weight:bold;' : ''}">${not empty wdAnbBVo ? (wdAnbBVo.useCnt + wdAnbBVo.useModCnt) : ''}</td><%-- 사용수 --%>
	<td class="body_ct" onclick="viewDetlPop('${year}','${anbTypCd}','${orUserBVo.odurUid}', 'remain')" style="cursor:pointer;">${
		not empty wdAnbBVo ? 
			  (wdAnbBVo.forwCnt + wdAnbBVo.forwModCnt)
			+ (wdAnbBVo.creCnt + wdAnbBVo.creModCnt)
			- (wdAnbBVo.useCnt + wdAnbBVo.useModCnt) : ''}</td><%-- 잔여 --%>
	<td class="body_ct" onclick="viewDetlPop('${year}','${anbTypCd}','${orUserBVo.odurUid}', 'ongo')" style="cursor:pointer;${
		not empty wdAnbBVo.ongoModCnt and wdAnbBVo.ongoModCnt ne 0.0 ?
		' color:#0F6F8E; font-weight:bold;' : ''}">${not empty wdAnbBVo ? (wdAnbBVo.ongoCnt + wdAnbBVo.ongoModCnt) : ''}</td><%-- 결재중 --%>
	</tr>
	</c:forEach>
	
	<tr id="trSum" onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="body_ct" colspan="2"><u:msg titleId="wd.jsp.sum" alt="합계" /></td>
	<td class="body_ct" >${forwSum}</td><%-- 이월수 --%>
	<td class="body_ct" >${creSum}</td><%-- 발생수 --%>
	<td class="body_ct" >${useSum}</td><%-- 사용수 --%>
	<td class="body_ct" >${remainSum}</td><%-- 잔여 --%>
	<td class="body_ct" >${ongoSum}</td><%-- 결재중 --%>
	</tr>
</u:listArea>
</c:forEach>
</c:if>


