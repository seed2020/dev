<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
import="org.springframework.web.context.WebApplicationContext"
import="org.springframework.web.context.support.WebApplicationContextUtils"
import="org.springframework.web.servlet.FrameworkServlet"
import="com.innobiz.orange.web.or.svc.OrCmSvc"
import="com.innobiz.orange.web.pt.secu.LoginSession"
import="com.innobiz.orange.web.pt.secu.UserVo"
import="java.util.Map"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
/*
	대체근무 신청서 : wdRepbReq
*/

%><style type="text/css">
.titlearea {
    width: 100%;
    height: 16px;
    margin: 0 0 9px 0;
}
.titlearea .tit_left .title_s {
    float: left;
    height: 13px;
    font-weight: bold;
    color: #454545;
}
.listarea {
    float: left;
    width: 100%;
    padding: 0;
    margin: 0;
    color: #454545;
}
.listtable {
    width: 100%;
    background: #bfc8d2;
    color: #454545;
}
.listtable tr {
    background: #ffffff;
}
.listtable .head_ct {
	height: 22px;
    text-align: center;
    background: #ebf1f6;
    line-height: 17px;
    padding: 2px 2px 0 2px;
}
.listtable .head_lt {
    height: 22px;
    background: #ebf1f6;
    line-height: 17px;
    padding: 2px 0 0 4px;
}
.body_ct {
    height: 22px;
    color: #454545;
    text-align: center;
    line-height: 17px;
    padding: 2px 3px 0 3px;
}
.body_lt {
    height: 22px;
    color: #454545;
    line-height: 17px;
    padding: 2px 3px 0 4px;
}
.blank {
    clear: both;
    height: 10px;
}
</style>
<script type="text/javascript">
<!--
<%//사용자 정보 변경 %>
function setUserPop(){
	var data = [];
	searchUserPop({data:data, multi:false, mode:'search'}, function(vo){
		if(vo!=null){
			var $table=$('#xml-body');
			$table.find('input[name="erpUserNm"]').val(vo.rescNm);
			$table.find('input[name="erpOdurUid"]').val(vo.odurUid);
		}
	});
};<% // 일시 replace %>
function getDayString(date , regExp){
	return date.replace(regExp,'');
};<% // 일시 비교 %>
function fnCheckDay(today , setday){
	return today > setday ? true : false;
};<% // 일자 체크 - 오늘 날짜 이후로만 - 과거날자도 가능하게 수정요청 : 2018-02-05 %>
function onChgTodayChk(date){
	/*
	var regExp = /[^0-9]/g;
	var today = getDayString(getToday(),regExp);
	var setday = getDayString(date,regExp);
	if(fnCheckDay(today , setday)){
		alertMsg('cm.calendar.check.dateAI');
		return true;
	}
	return false;
	*/
	return false;
}<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	if(validator.validate('xml\\-body')){
		var erpOptions=$('input[name="erpOptions"]').val();
		if(erpOptions==''){
			alert("코드가 없습니다.");
			return false;
		}
		var resultVa=false;
		if(fromToList!=null){
			callAjax('./checkUseYmdAjx.do?menuId=${menuId}&bxId=${param.bxId}', {anbTypCd:erpOptions, odurUid:$('#erpOdurUid').val(), useYmds:fromToList.join(',')}, function(data) {
				if (data.message != null) {
					alert(data.message);
				}
				if(data.result!=null && data.result=='ok'){
					resultVa=true;
				}
			});
		}
		return resultVa;		
	}
	return false;
}<%//기간 날짜 hidden 삽입 %>
function setDateHidden(arr){
	var $target=$('#xml-body #xml-dates\\/date');
	$target.html('');
	$.each(arr, function(index, val){
		$target.append('<div id="date"><input type="hidden" name="erpDate" value="'+val+'"/></div>');		
		//$target.appendHidden({name:'date',value:val});
	});
	fromToList=arr;
	var dayLen=arr.length;
	var halfDay=getHalfDay(); // 반차
	if(dayLen>0 && halfDay>0) dayLen-=halfDay;
	$('#xml-body #erpTotalDay').val(dayLen);
}<% // 기간 날짜 초기화 %>
function setDateInit(){
	$('#xml-body').find('input[name="erpStart"], input[name="erpEnd"]').val('');
}<% // 기간 일수 초기화 %>
function setDayInit(){
	$('#xml-body #erpTotalDay').val('');
}
var fromToList=null;
<% // 기간 날짜 조회 %>
function setFromToDate(){
	setDayInit();
	var erpStart=$('#xml-body').find('input[name="erpStart"]').val();
	var erpEnd=$('#xml-body').find('input[name="erpEnd"]').val();
	if(erpStart=='' || erpEnd=='') return;
	var $target=$('#xml-body #xml-dates\\/date');
	$target.html('');
	callAjax('/cm/date/getSrchDateListAjx.do?menuId=${menuId}', {start:erpStart, end:erpEnd, holiYn:'N', format:'yyyy-MM-dd', onlyHoli:'Y'}, function(data) {
		if (data.message != null){
			alert(data.message);
		}
		if (data.result == 'ok') {
			if(data.fromToList==null || data.fromToList.length==0) setDateInit();
			else{
				var dayLen=data.fromToList.length;
				var halfDay=getHalfDay(); // 반차
				if(dayLen>0 && halfDay>0) dayLen-=halfDay;
				$('#xml-body #erpTotalDay').val(dayLen);
				fromToList=data.fromToList;
			}
		}
	});
}

<%//기간 날짜 팝업 %>
function fromToDatePop(mode){
	var erpStart=$('#xml-body').find('input[name="erpStart"]').val();
	var erpEnd=$('#xml-body').find('input[name="erpEnd"]').val();
	if(erpStart=='' || erpEnd=='') return;
	var url='/cm/date/getSrchDateListPop.do?menuId=${menuId}';
	url+='&start='+erpStart;
	url+='&end='+erpEnd;
	url+='&holiYn=N';
	url+='&format=yyyy-MM-dd';
	url+='&onlyHoli=Y'; // 공휴일만 조회
		
	var dateList = getDateList();
	if(dateList!=null) url+='&chkDates='+dateList.join(',');
	if(mode!=undefined) url+='&mode='+mode;
	dialog.open('fromToDateDialog', '<u:msg titleId="wc.jsp.fromToDate.title" alt="날짜선택"/>', url);	
}

<%//수동 등록된 날짜 조회 %>
function getDateList(){
	var arr=[];
	$('#xml-body #xml-dates\\/date input[name="erpDate"]').each(function(){
		if($(this).val()!=''){
			arr.push($(this).val());
		}
	});
	if(arr.length==0){
		return null;
	}
	return arr;
}<% // 반차 일 경우 일수를 0.5로 고정 %>
function getHalfDay(){
	var returnVa=0;
	if($('#halfDayArea #erpHalfStart').is(':checked')) returnVa+=0.5;
	if($('#halfDayArea #erpHalfEnd').is(':checked')) returnVa+=0.5;
	return returnVa;
}<% // 반차 체크할 경우 신청일 수 조정 %>
function chnHalfDay(obj){
	var erpTotalDay=$('#erpTotalDay').val();
	if(erpTotalDay=='') return;
	erpTotalDay=Number(erpTotalDay);
	if($(obj).is(':checked')) erpTotalDay-=0.5;
	else erpTotalDay+=0.5;	
	$('#erpTotalDay').val(erpTotalDay);
	if(erpTotalDay==0){
		$(obj).trigger('click');
	}
};<% // 기간 데이터 로드 %>
function setFromToList(){
	var erpStart=$('#xml-body').find('input[name="erpStart"]').val();
	var erpEnd=$('#xml-body').find('input[name="erpEnd"]').val();
	if(erpStart!='' && erpEnd!=''){
		var dateList = getDateList();
		if(dateList!=null){
			fromToList=dateList;
		}else{
			setFromToDate();
		}
	}
};
$(document).ready(function(){
	$("#formBodyArea").find("input:visible").uniform();
	<c:if
	test="${!empty formBodyMode && formBodyMode ne 'view'}">setFromToList();</c:if>
});


//-->
</script>
<c:if
	test="${formBodyMode ne 'view'}">
<%
WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext(), 
		FrameworkServlet.SERVLET_CONTEXT_PREFIX+"appServlet"); // "appServlet"는 web.xml의 org.springframework.web.servlet.DispatcherServlet의 servlet-name을 지정.
//System.out.println("!!" + java.util.Arrays.asList(wac.getBeanDefinitionNames()));
UserVo userVo = LoginSession.getUser(request);
OrCmSvc orCmSvc = (OrCmSvc)wac.getBean("orCmSvc");
Map<String, Object> map = orCmSvc.getUserMap(userVo.getUserUid(), "ko");
request.setAttribute("userMap", map);
%>	
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">
<div id="xml-head">
	<input type="hidden" name="typId" value="wdRepbReq"/><!-- wdLeaveCan, wdRepbReq, wdRepbCan -->
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<!-- 날짜 배열 -->
<div id="xml-dates/date" style="display:none;"><c:forEach
		items="${formBodyXML.getChildList('body/dates')}" var="date">
	<div id="date"><input type="hidden" name="erpDate" value="${date.getAttr('erpDate')}"/></div></c:forEach>
</div>
<u:listArea id="xml-leave" colgroup="15%,">
	<u:set var="erpUserNm" test="${!empty formBodyXML.getAttr('body/leave.erpUserNm')}" value="${formBodyXML.getAttr('body/leave.erpUserNm') }" elseValue="${userMap.userNm }"
	/><u:set var="erpOdurUid" test="${!empty formBodyXML.getAttr('body/leave.erpOdurUid')}" value="${formBodyXML.getAttr('body/leave.erpOdurUid') }" elseValue="${userMap.odurUid }"
	/><u:input type="hidden" id="erpOdurUid" value="${erpOdurUid }"
	/><u:input type="hidden" id="erpOptions" value="repb"/>
<tr><td class="head_ct"><u:mandatory />근무 기간</td>
<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="erpStart" name="erpStart" option="{end:'erpEnd',onchange:setFromToDate}" title="시작일" value="${formBodyXML.getAttr('body/leave.erpStart')}" mandatory="Y"/></td>
			<td class="width15">
			<td>~</td>
			<td class="width15">
			<td><u:calendar id="erpEnd" name="erpEnd" option="{start:'erpStart',onchange:setFromToDate}" title="종료일" value="${formBodyXML.getAttr('body/leave.erpEnd')}" mandatory="Y"/></td>
			<td class="width15">
			<td>/</td>
			<td class="width15">
			<td> 근무일수 : </td>
			<td><u:input id="erpTotalDay" value="${formBodyXML.getAttr('body/leave.erpTotalDay')}" title="근무일수" valueOption="number" maxLength="9" style="width:50px;" valueAllowed="." mandatory="Y" onclick="fromToDatePop();"/></td>
			<td class="width15">
			<td> 일</td>
			</tr>
			</table></td>
</tr>
<tr><td class="head_ct"><u:mandatory />신청자</td>
<td class="body_lt" ><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="bodyip_lt"><strong>신청자명</strong></td>
			<td><u:input id="erpUserNm" value="${erpUserNm }" title="신청자명" style="width:98%;" maxByte="100" mandatory="Y" readonly="Y"/></td>
			</tr>
			</table></td>
</tr><tr><td class="head_ct">근무 시간</td>
<td class="body_lt"><u:checkArea id="halfDayArea">
			<u:checkbox value="Y" id="erpHalfStart" name="erpHalfStart" titleId="cols.strtYmd" alt="시작일" checkValue="${formBodyXML.getAttr('body/leave.erpHalfStart') }" onclick="chnHalfDay(this);"/><td>(</td>
			<u:radio name="erpHalfStartAmPm" value="AM" titleId="cm.option.am" checkValue="${formBodyXML.getAttr('body/leave.erpHalfStartAmPm') }"  checked="${empty formBodyXML.getAttr('body/leave.erpHalfStartAmPm')}" />
			<u:radio name="erpHalfStartAmPm" value="PM" titleId="cm.option.pm" checkValue="${formBodyXML.getAttr('body/leave.erpHalfStartAmPm') }" /><td>)</td>
			<td class="width20"></td>
			<u:checkbox value="Y" id="erpHalfEnd" name="erpHalfEnd" titleId="cols.endYmd" alt="종료일" checkValue="${formBodyXML.getAttr('body/leave.erpHalfEnd') }" onclick="chnHalfDay(this);"/><td>(</td>
			<u:radio name="erpHalfEndAmPm" value="AM" titleId="cm.option.am" checkValue="${formBodyXML.getAttr('body/leave.erpHalfEndAmPm') }" checked="${empty formBodyXML.getAttr('body/leave.erpHalfEndAmPm')}" />
			<u:radio name="erpHalfEndAmPm" value="PM" titleId="cm.option.pm" checkValue="${formBodyXML.getAttr('body/leave.erpHalfEndAmPm') }" /><td>)</td>
			</u:checkArea></td>
</tr>
<tr><td class="head_ct"><u:mandatory />근무 사유</td>
<td class="body_lt"><u:textarea id="erpReason" value="${formBodyXML.getAttr('body/leave.erpReason')}" title="근무사유" maxByte="2000" style="width:98%;" rows="10" mandatory="Y"/></td>
</tr>
</u:listArea>
</div>

<c:if
	test="${formBodyMode eq 'pop'}">
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setErpXMLPop();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</c:if>

</div></c:if><c:if
	test="${formBodyMode eq 'view'}">
<div class="listarea" id="xml-leave">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="15%"/><col width="*"/></colgroup>	
<tr><td class="head_ct"><u:mandatory />근무 기간</td>
<td class="body_lt"><fmt:parseDate var="erpStart" value="${formBodyXML.getAttr('body/leave.erpStart')}" pattern="yyyy-MM-dd"
		/><fmt:formatDate value="${erpStart}" type="date" dateStyle="long" 
		/> 부터 <fmt:parseDate var="erpEnd" value="${formBodyXML.getAttr('body/leave.erpEnd')}" pattern="yyyy-MM-dd"
		/><fmt:formatDate value="${erpEnd}" type="date" dateStyle="long" 
		/> 까지 / 근무일수 : <a href="javascript:;" onclick="fromToDatePop('view');"><strong>${formBodyXML.getAttr('body/leave.erpTotalDay')}</strong> 일</a></td>
</tr>
<tr><td class="head_ct"><u:mandatory />신청자</td>
<td class="body_lt" ><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="bodyip_lt"><strong>신청자명</strong></td>
			<td class="width5"></td>
			<td class="bodyip_lt"><u:out value="${formBodyXML.getAttr('body/leave.erpUserNm') }"/></td>
			</tr>
			</table></td>
</tr><tr><td class="head_ct">근무 시간</td>
<td class="body_lt"><c:if test="${!empty formBodyXML.getAttr('body/leave.erpHalfStart')}"
><u:set var="erpHalfStartAmPmTxt" test="${formBodyXML.getAttr('body/leave.erpHalfStartAmPm') eq 'AM'}" value="오전" elseValue="오후"
/>시작일 (<span id="erpHalfStartAmPmTxt">${erpHalfStartAmPmTxt }</span>)</c:if><c:if test="${!empty formBodyXML.getAttr('body/leave.erpHalfEnd')}"
><u:set var="erpHalfEndAmPmTxt" test="${formBodyXML.getAttr('body/leave.erpHalfEndAmPm') eq 'AM'}" value="오전" elseValue="오후"
/><c:if test="${!empty formBodyXML.getAttr('body/leave.erpHalfStart')}"
>&nbsp;</c:if>종료일 (<span id="erpHalfEndAmPmTxt">${erpHalfEndAmPmTxt }</span>)</c:if></td>
</tr>
<tr><td class="head_ct"><u:mandatory />근무 사유</td>
<td class="body_lt wordbreak"><u:out value="${formBodyXML.getAttr('body/leave.erpReason')}"/></td>
</tr>
</table></div>
<!-- 날짜 배열 -->
<div id="xml-body">
<div id="xml-dates/date"><c:forEach
		items="${formBodyXML.getChildList('body/dates')}" var="date">
	<input type="hidden" name="erpDate" value="${date.getAttr('erpDate')}"/></c:forEach>
</div>
<input type="hidden" name="erpStart" value="${formBodyXML.getAttr('body/leave.erpStart')}"/>
<input type="hidden" name="erpEnd" value="${formBodyXML.getAttr('body/leave.erpEnd')}"/>
</div>
</c:if>