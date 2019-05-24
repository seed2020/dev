<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
/* 
wd.anbTypCd.anb=연차
wd.anbTypCd.nanb=개정연차
wd.anbTypCd.repb=대체휴무
wd.anbTypCd.offb=공가 */
request.setAttribute("anbTypCds", new String[]{"anb","nanb","repb","offb"});

String anbTypCd = request.getParameter("anbTypCd");
if(anbTypCd == null || anbTypCd.isEmpty()) anbTypCd = "anb";
request.setAttribute("anbTypCd", anbTypCd);

String year = request.getParameter("year");
if(year == null || year.isEmpty()) year = request.getAttribute("currYear").toString();
request.setAttribute("year", year);

%><script type="text/javascript">
<!--<%--
부서 [선택] 클릭 --%>
function openOrgPop(){
	var $form = $("#searchForm1");
	var orgIds = $form.find("#orgIds").val(), data = [];
	if(orgIds!=''){
		var oids = orgIds.split(',');
		for(var i=0;i<oids.length;i++){
			if(oids[i].indexOf('|')>0){
				data.push({orgId:oids[i].substring(0, oids[i].indexOf('|')), withSub:'Y'});
			} else {
				data.push({orgId:oids[i], withSub:'N'});
			}
		}
	}
	searchOrgPop({data:data, multi:true, withSub:true, mode:'search'}, function(arr){
		if(arr!=null){
			var dispVas=[], ids=[], withSubTxt = '(<u:msg titleId="or.check.withSub.short" alt="하위포함" />)';
			arr.each(function(index, orgVo){
				if(orgVo.withSub=='Y'){
					dispVas.push(orgVo.rescNm+withSubTxt);
					ids.push(orgVo.orgId+'|Y');
				} else {
					dispVas.push(orgVo.rescNm);
					ids.push(orgVo.orgId);
				}
			});
			$form.find("#orgDispArea").text(dispVas.join(', '));
			$form.find("#orgNms").val(dispVas.join(', '));
			$form.find("#orgIds").val(ids.join(','));
		} else {
			$form.find("#orgDispArea").text('');
			$form.find("#orgNms").val('');
			$form.find("#orgIds").val('');
		}
	});
}<%--
사용자 [선택] 클릭 --%>
function openUserPop(){
	var $form = $("#searchForm1");
	var userUids = $form.find("#userUids").val(), data = [];
	if(userUids != null){
		userUids.split(',').each(function(index, uid){
			data.push({userUid:uid});
		});
	}
	searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
		if(arr!=null){
			var dispVas=[], ids=[];
			arr.each(function(index, userVo){
				dispVas.push(userVo.rescNm);
				ids.push(userVo.userUid);
			});
			$form.find("#userDispArea").text(dispVas.join(', '));
			$form.find("#userNms").val(dispVas.join(', '));
			$form.find("#userUids").val(ids.join(','));
		} else {
			$form.find("#userDispArea").text('');
			$form.find("#userNms").val('');
			$form.find("#userUids").val('');
		}
	});
}<%--
연차 생성 클릭 --%>
function createAnb(){
	callAjax('./processAnbAjx.do?menuId=${menuId}', null, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if(data.result == 'ok') reload();
	});
}<%--
개정연차 생성 클릭 --%>
function createNanb(){
	callAjax('./processNanbAjx.do?menuId=${menuId}', null, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if(data.result == 'ok') reload();
	});
}
function viewDetlPop(year, anbTypCd, uid, modTypCd){
	dialog.close("viewDetlDialog");
	var url = "./viewDetlPop.do?menuId=${menuId}&year="+year+"&anbTypCd="+anbTypCd+"&modTypCd="+modTypCd+"&odurUid="+uid;
	var anbTypNm = callMsg('wd.anbTypCd.'+anbTypCd);
	var popTit = '<u:msg titleId="ap.btn.detlInfo" alt="상세정보" /> ( '+year+' / '+ anbTypNm +' )';
	dialog.open("viewDetlDialog", popTit, url);
}
$(document).ready(function() {
	setUniformCSS();
	${WD_RELOAD_SCRIPT}
});
//<c:if test="${sessionScope.userVo.userUid eq 'U0000001'}">
<%-- 초기 데이타 --%>
function initData(){
	dialog.open('setInitDataDialog', '<u:msg titleId="cm.btn.allDel" alt="전체삭제" />', './setInitDataPop.do?menuId=${menuId}');
}
//</c:if>
//-->
</script>
<u:title alt="연도별 관리" menuNameFirst="true" />

<%-- // 검색영역 --%>
<u:searchArea style="position:relative; z-index:2;">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" /><c:if
		test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="wd.year" alt="년도" /></td>
		<td><select id="year" name="year" ><c:forEach begin="${currYear - 5}" step="1" end="${currYear}" var="yearNo">
			<u:option value="${yearNo}" title="${yearNo}" selected="${yearNo eq year}" /></c:forEach>
			</select></td>
		
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="wd.anbTypCds" alt="연차 종류" /></td>
		<td><select id="anbTypCd" name="anbTypCd" ><c:forEach items="${anbTypCds}" var="anbTypCd">
			<u:option value="${anbTypCd}" titleId="wd.anbTypCd.${anbTypCd}" selected="${param.anbTypCd eq anbTypCd}"/></c:forEach></select></td>
		
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.entraYmd" alt="입사일" /></td>
		<td><u:calendar
			titleId="cm.cal.startDd" alt="시작일자"
			id="minEntraYmd" value="${param.minEntraYmd}" option="{end:'maxEntraYmd'}" /></td>
			<td style="padding:0 3px 0 6px;"> ~ </td>
			<td><u:calendar
			titleId="cm.cal.endDd" alt="종료일자"
			id="maxEntraYmd" value="${param.maxEntraYmd}" option="{start:'minEntraYmd'}" />
		</td>
		
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="or.cols.statCd" alt="상태코드" /></td>
		<td><select id="userStatCd" name="userStatCd" >
			<u:option value="_" titleId="cm.option.all" alt="전체"/><c:forEach items="${userStatCdList}" var="userStat">
			<u:option value="${userStat.cd}" title="${userStat.rescNm}" selected="${userStat.cd eq (empty param.userStatCd ? '02' : param.userStatCd)}"/></c:forEach></select></td>
		</tr>
		</table>
		<table border="0" cellpadding="0" cellspacing="0" style="padding-top:3px;">
		<tr>
		<td class="search_tit"><u:msg alt="사용자" titleId="pt.jsp.setAuthGrp.user" /></td><!--
		<td><u:input id="userNm" name="userNm" alt="사용자명" titleId="cols.userNm" value="${param.userNm}" maxLength="10" /></td>-->
		<td class="width5"></td>
		<td><u:buttonS titleId="cm.btn.sel" alt="선택" onclick="openUserPop();" /></td>
		<td class="width5"></td>
		<td><div id="userDispArea" style="width:550px; margin-top:6px;" class="body_lt"><u:out value='${param.userNms}' /></div>
			<input id="userNms" name="userNms" type="hidden" value="<u:out value='${param.userNms}' type='value' />">
			<input id="userUids" name="userUids" type="hidden" value="<u:out value='${param.userUids}' type='value' />"></td>
		</tr>
		<tr>
		<td class="search_tit"><u:msg titleId="cols.dept" alt="부서" /></td><!--
		<td><u:input id="deptNm" name="deptNm" titleId="cols.dept" alt="부서" value="${param.deptNm}" maxLength="10" /></td>-->
		<td class="width5"></td>
		<td><u:buttonS titleId="cm.btn.sel" alt="선택" onclick="openOrgPop();" /></td>
		<td class="width5"></td>
		<td><div id="orgDispArea" style="width:550px; margin-top:6px;" class="body_lt"><u:out value='${param.orgNms}' /></div>
			<input id="orgNms" name="orgNms" type="hidden" value="<u:out value='${param.orgNms}' type='value' />">
			<input id="orgIds" name="orgIds" type="hidden" value="<u:out value='${param.orgIds}' type='value' />"></td>
		</tr>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<%--// 목록 --%>
<u:listArea id="listArea">

	<tr><u:set test="${sysPlocMap.enterBaseAnbMak eq 'Y'}" var="tdWidth" value="7%" elseValue="8%" />
	<td class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="cols.userNm" alt="사용자명" /></td>
	<td width="9%" class="head_ct"><u:msg titleId="cols.entraYmd" alt="입사일" /></td>
	<td width="${tdWidth}" class="head_ct"><u:msg titleId="or.cols.statCd" alt="상태코드" /></td>
	
	<td width="${tdWidth}" class="head_ct" id="forwTxt"><u:msg titleId="wd.cnt.forw" alt="이월수" /></td>
	<td width="${tdWidth}" class="head_ct" id="creTxt"><u:msg titleId="wd.cnt.cre" alt="발생수" /></td>
	<td width="${tdWidth}" class="head_ct" id="useTxt"><u:msg titleId="wd.cnt.use" alt="사용수" /></td>
	<td width="${tdWidth}" class="head_ct" id="leftTxt"><u:msg titleId="wd.cnt.left" alt="잔여" /></td>
	<td width="${tdWidth}" class="head_ct" id="ongoTxt"><u:msg titleId="wd.cnt.ongo" alt="결재중" /></td>
	<td width="7%" class="head_ct" id="ongoTxt"><u:msg titleId="wd.jsp.personal" alt="개인별" /></td>
	</tr>


<c:if test="${fn:length(orUserBVoList)==0}" >
	<tr>
	<td class="nodata" colspan="11"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(orUserBVoList) >0}" >
	<c:forEach items="${orUserBVoList}" var="orUserBVo" varStatus="status">
	<tr id="tr${orUserBVo.odurUid}" onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="body_ct"><u:out value="${orUserBVo.orgRescNm}" /></td>
	<td class="body_ct"><a href="javascript:viewUserPop('${orUserBVo.userUid}');"><u:out value="${orUserBVo.rescNm}" /></a></td>
	<td class="body_ct"><u:out value="${orUserBVo.entraYmd}" type="shortdate" /></td>
	<td class="body_ct"><u:out value="${orUserBVo.userStatNm}" /></td><u:convert
		srcId="wdAnbBVo${orUserBVo.odurUid}" var="wdAnbBVo" />
	
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
	<td class="body_ct"><u:buttonS titleId="wd.jsp.personal" alt="개인별" href="${byUserUrl}&odurUid=${orUserBVo.odurUid}" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<u:buttonArea><c:if
		test="${sysPlocMap.manlAnbMak ne 'Y'}">
	<u:button titleId="wd.cmd.manualCreNanb" alt="수동 개정연차 생성" href="javascript:createNanb();" auth="A" />
	<u:button titleId="wd.cmd.manualCreAnb" alt="수동 연차 생성" href="javascript:createAnb();" auth="A" /></c:if><c:if
		test="${sessionScope.userVo.userUid eq 'U0000001'}">
	<u:button titleId="cm.btn.allDel" alt="전체삭제" href="javascript:initData();" /></c:if>
</u:buttonArea>

