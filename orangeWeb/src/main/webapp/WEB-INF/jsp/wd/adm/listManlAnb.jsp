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
[버튼] 수정 --%>
function modifyManlData(odurUid){
	dialog.close("setManlModifyDialog");
	var url = "./setManlModifyPop.do?menuId=${menuId}&year=${year}&anbTypCd=anb&odurUid=" + odurUid;
	dialog.open("setManlModifyDialog", '<u:msg titleId="cm.btn.mod" alt="수정" />', url);
}<%--
[엑셀 다운] --%>
function excelDownFile(){
	var $form = $('#searchForm1');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
	
	$form.attr('method','get');
	$form.attr('action','./listUser.do');
	$form.attr('target','');
}<%--
[엑셀 업로드] --%>
function popExcel(){
	dialog.open('setExcelDialog','<u:msg titleId="or.jsp.listUser.excel" alt="엑셀업로드"/>','./setExcelPop.do?menuId=${menuId}');
}
$(document).ready(function() {
	setUniformCSS();
	${WD_RELOAD_SCRIPT}
});
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

	<tr><u:set test="${true}" var="tdWidth" value="9%" />
	<td class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="cols.userNm" alt="사용자명" /></td>
	<td width="9%" class="head_ct"><u:msg titleId="cols.entraYmd" alt="입사일" /></td>
	<td width="${tdWidth}" class="head_ct"><u:msg titleId="or.cols.statCd" alt="상태코드" /></td>
	
	
	<td width="${tdWidth}" class="head_ct" id="creTxt"><u:msg titleId="wd.cnt.cre" alt="발생수" /></td>
	<td width="${tdWidth}" class="head_ct" id="useTxt"><u:msg titleId="wd.cnt.use" alt="사용수" /></td>
	<td width="${tdWidth}" class="head_ct" id="ongoTxt"><u:msg titleId="wd.cnt.ongo" alt="결재중" /></td>
	<td width="${tdWidth}" class="head_ct" id="leftTxt"><u:msg titleId="wd.cnt.left" alt="잔여" /></td>
	<td width="${tdWidth}" class="head_ct" id="ongoTxt"><u:msg titleId="cm.btn.mod" alt="수정" /></td>
	</tr>


<c:if test="${fn:length(orUserBVoList)==0}" >
	<tr>
	<td class="nodata" colspan="10"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
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
	
	<td class="body_ct" >${not empty wdAnbBVo ? (wdAnbBVo.creCnt + wdAnbBVo.creModCnt) : ''}</td><%-- 발생수 --%>
	<td class="body_ct" >${not empty wdAnbBVo ? (wdAnbBVo.useCnt + wdAnbBVo.useModCnt) : ''}</td><%-- 사용수 --%>
	<td class="body_ct" >${not empty wdAnbBVo ? (wdAnbBVo.ongoCnt + wdAnbBVo.ongoModCnt) : ''}</td><%-- 결재중 --%>
	<td class="body_ct" >${
		not empty wdAnbBVo ? 
			  (wdAnbBVo.forwCnt + wdAnbBVo.forwModCnt)
			+ (wdAnbBVo.creCnt + wdAnbBVo.creModCnt)
			- (wdAnbBVo.useCnt + wdAnbBVo.useModCnt) : ''}</td><%-- 잔여 --%>
	<td class="body_ct"><u:buttonS titleId="cm.btn.mod" alt="수정" onclick="modifyManlData('${orUserBVo.odurUid}')" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<u:buttonArea>
	<u:button href="javascript:excelDownFile();" id="changeStatCd" titleId="cm.btn.excelDown" alt="엑셀다운" auth="A" />
	<u:button href="javascript:popExcel();" id="changeStatCd" titleId="or.jsp.listUser.excel" alt="엑셀업로드" auth="A" />
</u:buttonArea>

