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
	휴가 신청서 : leaveReq
*/
// 구분 목록
String[][] erpOptions = {
		{"01","연차"},
		{"03","병가"},
		{"06","훈련"},
		{"07","공가"},
		{"08","경조휴가"},
		{"11","교육"},
		{"15","생리휴가"},
		{"17","육아휴직"},
		{"18","하계휴가"},
		{"98","국내출장"},
		{"99","해외출장"},
		{"31","외출"},
		{"32","지각"},
		{"33","조퇴"},
		{"34","결근"},
		{"00","기타"}
	};	
request.setAttribute("erpOptions", erpOptions);
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
			$table.find('input[name="erpDeptNm"]').val(vo.deptRescNm);
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
	<c:if test="${formBodyMode eq 'edit'}">return true;</c:if>
	return validator.validate('xml\\-body');
}<% // 체크박스 클릭 이벤트 제어 %>
function notChkEvt(obj){
	$(obj).prop('checked', !obj.checked).uniform();
}<%//기간 날짜 hidden 삽입 %>
function setDateHidden(arr){
	var $target=$('#xml-body #xml-dates\\/date');
	$target.html('');
	$.each(arr, function(index, val){
		$target.append('<div id="date"><input type="hidden" name="erpDate" value="'+val+'"/></div>');		
	});
	fromToList=arr;
	var dayLen=arr.length;
	//var halfDay=getHalfDay(); // 반차
	//if(dayLen>0 && halfDay>0) dayLen-=halfDay;
	$('#xml-body #erpTotalDay').val(dayLen);
	$('#xml-body #erpHiddenTotal').val(dayLen);
	initDateHalf();
}

<% // 기간 날짜 초기화 %>
function setDateInit(){
	$('#xml-body').find('input[name="erpStart"], input[name="erpEnd"]').val('');
}
<% // 반차 초기화 %>
function initDateHalf(obj){
	var target=obj!=undefined ? $(obj) : $('#xml-body').find('input[name="erpHalfStart"], input[name="erpHalfEnd"]');
	target.prop('checked', false);
	target.uniform.update();
}
<% // 기간 일수 초기화 %>
function setDayInit(){
	initDateHalf();
	$('#xml-body #erpTotalDay').val('');
}
<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
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
	callAjax('/cm/date/getSrchDateListAjx.do?menuId=${menuId}', {start:erpStart, end:erpEnd, holiYn:'N', format:'yyyy-MM-dd'/*, onlyHoli:'Y'*/}, function(data) {
		if (data.message != null){
			alert(data.message);
		}
		if (data.result == 'ok') {
			if(data.fromToList==null || data.fromToList.length==0) setDateInit();
			else{
				var dayLen=data.fromToList.length;
				//var halfDay=getHalfDay(); // 반차
				//if(dayLen>0 && halfDay>0) dayLen-=halfDay;
				$('#xml-body #erpTotalDay').val(dayLen);
				$('#xml-body #erpHiddenTotal').val(dayLen);
				fromToList=data.fromToList;
			}
		}
	});
}
<%// 시작일 종료일 같은지 비교 %>
function isEqDate(){
	var erpStart=$('#xml-body').find('input[name="erpStart"]').val();
	var erpEnd=$('#xml-body').find('input[name="erpEnd"]').val();
	if(erpStart=='' || erpEnd=='') return false;
	return erpStart==erpEnd;
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
	if(erpTotalDay==''){
		if($(obj).is(':checked')) initDateHalf(obj);
		return false;
	}
	erpTotalDay=Number(erpTotalDay);
	var totalDays=$('#xml-body #erpHiddenTotal').val();
	if(totalDays===undefined || totalDays=='') totalDays=1;
	var repetCnt=$('#erpHalfRepet').is(':checked') && totalDays!=null ? totalDays : 1;
	if($(obj).is(':checked')) erpTotalDay-=(0.5*repetCnt);
	else erpTotalDay+=(0.5*repetCnt);
	if(erpTotalDay<=0){
		initDateHalf(obj);
	}
	if(erpTotalDay>=0.5) $('#erpTotalDay').val(erpTotalDay);
	return true;
	
};<% // 반차 매일 반복 %>
function chnHalfRepet(obj){
	setFromToDate();
	
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
	
	// 반차 계산
	<c:if test="${formBodyMode ne 'view'}">	
	$('#halfDayArea').find('input[name="erpHalfStart"], input[name="erpHalfEnd"]').change(function(){chnHalfDay(this);});
	$('#halfDayArea #erpHalfRepet').change(function(){chnHalfRepet(this);});
	</c:if>
	
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
<div class="front notPrint"><div class="front_right"><u:buttonS alt="신청자 변경" title="신청자 변경" href="javascript:setUserPop();"/></div></div>
<div id="xml-head">
	<input type="hidden" name="typId" value="enobizLeaveReq"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<!-- 날짜 배열 -->
<div id="xml-dates/date" style="display:none;"><c:forEach
		items="${formBodyXML.getChildList('body/dates')}" var="date">
	<div id="date"><input type="hidden" name="erpDate" value="${date.getAttr('erpDate')}"/></div></c:forEach>
</div>
<u:listArea id="xml-leave" colgroup="15%,">
	<c:if test="${formBodyMode ne 'edit' }">
	<u:set var="erpUserNm" test="${!empty formBodyXML.getAttr('body/leave.erpUserNm')}" value="${formBodyXML.getAttr('body/leave.erpUserNm') }" elseValue="${userMap.userNm }"
	/><u:set var="erpOdurUid" test="${!empty formBodyXML.getAttr('body/leave.erpOdurUid')}" value="${formBodyXML.getAttr('body/leave.erpOdurUid') }" elseValue="${userMap.odurUid }"
	/><u:input type="hidden" id="erpOdurUid" value="${erpOdurUid }"
	/><u:set var="erpDeptNm" test="${!empty formBodyXML.getAttr('body/leave.erpDeptNm')}" value="${formBodyXML.getAttr('body/leave.erpDeptNm') }" elseValue="${userMap.deptRescNm }"
	/></c:if><c:if test="${formBodyMode eq 'edit'}"><tr><td class="head_ct"><u:mandatory />근태일정 연계</td>
<td class="body_lt"><u:checkArea><u:radio name="erpWorkSchdlYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${formBodyXML.getAttr('body/leave.erpWorkSchdlYn')}" inputClass="bodybg_lt" checked="${empty formBodyXML.getAttr('body/leave.erpWorkSchdlYn')}"/>
					<u:radio name="erpWorkSchdlYn" value="N" titleId="cm.option.notUse" alt="사용 안함" checkValue="${formBodyXML.getAttr('body/leave.erpWorkSchdlYn')}" inputClass="bodybg_lt" />
				</u:checkArea></td></tr></c:if>
<tr><td class="head_ct"><u:mandatory />휴가 종류</td>
<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><select id="erpOptions" name="erpOptions" >
			<c:forEach items="${erpOptions}" var="erpOption" varStatus="status">
				<u:option value="${erpOption[0]}" title="${erpOption[1]}" 
				checkValue="${empty formBodyXML.getAttr('body/leave.erpOptions') ? '' : formBodyXML.getAttr('body/leave.erpOptions')}"/>
			</c:forEach>
		</select><c:if test="${formBodyMode ne 'edit' && !empty formBodyXML.getAttr('body/leave.erpWorkSchdlYn') }"><u:input type="hidden" id="erpWorkSchdlYn" value="${formBodyXML.getAttr('body/leave.erpWorkSchdlYn') }"
		/></c:if></td><td class="width15"></td><td><div id="txtVacaDate"></div></td><td class="width15"></td><td><div id="useVacaDate" style="font-weight:bold;"></div><input type="hidden" id="erpUseDate" name="erpUseDate"/></td
		></tr></table></td>		
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 기간</td>
<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="erpStart" name="erpStart" option="{end:'erpEnd',onchange:setFromToDate}" title="시작일" value="${formBodyXML.getAttr('body/leave.erpStart')}" mandatory="Y"/></td>
			<td class="width15">
			<td>~</td>
			<td class="width15">
			<td><u:calendar id="erpEnd" name="erpEnd" option="{start:'erpStart',onchange:setFromToDate}" title="종료일" value="${formBodyXML.getAttr('body/leave.erpEnd')}" mandatory="Y"/></td>
			<td class="width15">
			<td>/</td>
			<td><u:input id="erpTotalDay" value="${formBodyXML.getAttr('body/leave.erpTotalDay')}" title="휴가일수" valueOption="number" maxLength="9" style="width:50px;" valueAllowed="." mandatory="Y" onclick="fromToDatePop();" readonly="Y"/></td>
			<td class="width5"></td>
			<td> 일</td>
			<td class="width15">
			<td>/</td>
			<td><u:input id="erpTotalTime" value="${formBodyXML.getAttr('body/leave.erpTotalTime')}" title="시간" valueOption="number" maxLength="3" style="width:50px;text-align:right;" valueAllowed="." /></td>
			<td class="width5"></td>
			<td> 시간(외출, 지각, 조퇴 시에 입력)</td>
			</tr>
			</table></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 신청자</td>
<td class="body_lt" ><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="bodyip_lt"><strong>소속부서</strong></td>
			<td><u:input id="erpDeptNm" value="${erpDeptNm }" title="소속부서" style="width:98%;" maxByte="200" readonly="Y" mandatory="Y"/></td>
			<td class="width15"></td>
			<td class="bodyip_lt"><strong>신청자명</strong></td>
			<td><u:input id="erpUserNm" value="${erpUserNm }" title="신청자명" style="width:98%;" maxByte="100" readonly="Y" mandatory="Y"/></td>
			</tr>
			</table></td>
</tr>
<tr><td class="head_ct">반차 여부</td>
<td class="body_lt"><u:checkArea id="halfDayArea">
			<u:checkbox value="Y" id="erpHalfStart" name="erpHalfStart" titleId="cols.strtYmd" alt="시작일" checkValue="${formBodyXML.getAttr('body/leave.erpHalfStart') }" /><td>(</td>
			<u:radio name="erpHalfStartAmPm" value="AM" titleId="cm.option.am" checkValue="${formBodyXML.getAttr('body/leave.erpHalfStartAmPm') }"  checked="${empty formBodyXML.getAttr('body/leave.erpHalfStartAmPm')}" />
			<u:radio name="erpHalfStartAmPm" value="PM" titleId="cm.option.pm" checkValue="${formBodyXML.getAttr('body/leave.erpHalfStartAmPm') }" /><td>)</td>
			<td class="width20"></td>
			<u:checkbox value="Y" id="erpHalfEnd" name="erpHalfEnd" titleId="cols.endYmd" alt="종료일" checkValue="${formBodyXML.getAttr('body/leave.erpHalfEnd') }" /><td>(</td>
			<u:radio name="erpHalfEndAmPm" value="AM" titleId="cm.option.am" checkValue="${formBodyXML.getAttr('body/leave.erpHalfEndAmPm') }" checked="${empty formBodyXML.getAttr('body/leave.erpHalfEndAmPm')}" />
			<u:radio name="erpHalfEndAmPm" value="PM" titleId="cm.option.pm" checkValue="${formBodyXML.getAttr('body/leave.erpHalfEndAmPm') }" /><td>)</td>
			<td class="width20"></td>
			<u:checkbox value="Y" id="erpHalfRepet" name="erpHalfRepet" title="매일반복" alt="매일반복" checkValue="${formBodyXML.getAttr('body/leave.erpHalfRepet') }" />
			</u:checkArea><u:input type="hidden" id="erpHiddenTotal" value="${formBodyXML.getAttr('body/leave.erpHiddenTotal') }"/></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 사유</td>
<td class="body_lt"><u:textarea id="erpReason" value="${formBodyXML.getAttr('body/leave.erpReason')}" title="휴가사유" maxByte="2000" style="width:98%;" rows="10" mandatory="Y"/></td>
</tr><tr><td class="head_ct">연락처</td>
<td class="body_lt"><u:input id="erpMobile" maxByte="200" value="${formBodyXML.getAttr('body/leave.erpMobile')}" title="연락처" style="width: 98%;"/></td>
</tr><tr><td class="head_ct">인수자</td>
<td class="body_lt"><u:input id="erpReceiver" maxByte="200" value="${formBodyXML.getAttr('body/leave.erpReceiver')}" title="인수자" style="width: 98%;" /></td>
</tr>
</u:listArea>
</div>

<!-- <div class="color_txt"><strong>[당일 반차 신청시]</strong> 시작일만 오전/오후 체크</div>
<div class="color_txt"><strong>[예비군/민방위 신청시]</strong> 통지서 스캔하여 파일 첨부</div>
<div class="color_txt"><strong>[경조휴가 신청시]</strong> 증빙서류 스캔하여 파일 첨부 (예: 청첩장 등본 등)</div> -->

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
<tr><td class="head_ct"><u:mandatory />휴가 종류</td>
<td class="body_lt"><c:forEach items="${erpOptions}" var="erpOption" varStatus="status" 
			><c:if test="${formBodyXML.getAttr('body/leave.erpOptions') eq  erpOption[0]}"><strong>${erpOption[1]}</strong></c:if
			></c:forEach></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 기간</td>
<td class="body_lt"><fmt:parseDate var="erpStart" value="${formBodyXML.getAttr('body/leave.erpStart')}" pattern="yyyy-MM-dd"
		/><fmt:formatDate value="${erpStart}" type="date" dateStyle="long" 
		/> 부터 <fmt:parseDate var="erpEnd" value="${formBodyXML.getAttr('body/leave.erpEnd')}" pattern="yyyy-MM-dd"
		/><fmt:formatDate value="${erpEnd}" type="date" dateStyle="long" 
		/> 까지 / 휴가일수 : <a href="javascript:;" onclick="fromToDatePop('view');">${formBodyXML.getAttr('body/leave.erpTotalDay')} 일</a>
		<c:if test="${!empty formBodyXML.getAttr('body/leave.erpTotalTime')}"
		> / ${formBodyXML.getAttr('body/leave.erpTotalTime') } 시간</c:if></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 신청자</td>
<td class="body_lt" ><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="bodyip_lt"><strong>소속부서</strong></td>
			<td class="width5"></td>
			<td class="bodyip_lt"><u:out value="${formBodyXML.getAttr('body/leave.erpDeptNm') }"/></td>
			<td class="width15"></td>
			<td class="bodyip_lt"><strong>신청자명</strong></td>
			<td class="width5"></td>
			<td class="bodyip_lt"><u:out value="${formBodyXML.getAttr('body/leave.erpUserNm') }"/></td>
			</tr>
			</table></td>
</tr>
<tr><td class="head_ct">반차 여부</td>
<td class="body_lt"><u:set var="isHalfRepet" test="${!empty formBodyXML.getAttr('body/leave.erpHalfRepet') }" value="Y" elseValue="N"
/><c:if test="${!empty formBodyXML.getAttr('body/leave.erpHalfStart')}"
><u:set var="erpHalfStartAmPmTxt" test="${formBodyXML.getAttr('body/leave.erpHalfStartAmPm') eq 'AM'}" value="오전" elseValue="오후"
/><c:if test="${isHalfRepet eq 'N' }">시작일 (</c:if><span id="erpHalfStartAmPmTxt">${erpHalfStartAmPmTxt }</span><c:if test="${isHalfRepet eq 'N' }">)</c:if></c:if
><c:if test="${!empty formBodyXML.getAttr('body/leave.erpHalfEnd')}"
><u:set var="erpHalfEndAmPmTxt" test="${formBodyXML.getAttr('body/leave.erpHalfEndAmPm') eq 'AM'}" value="오전" elseValue="오후"
/><c:if test="${!empty formBodyXML.getAttr('body/leave.erpHalfStart')}"
>&nbsp;</c:if><c:if test="${isHalfRepet eq 'N' }">종료일 (</c:if><span id="erpHalfEndAmPmTxt">${erpHalfEndAmPmTxt }</span><c:if test="${isHalfRepet eq 'N' }">)</c:if></c:if>
<c:if test="${(!empty formBodyXML.getAttr('body/leave.erpHalfStart') || !empty formBodyXML.getAttr('body/leave.erpHalfEnd')) && !empty formBodyXML.getAttr('body/leave.erpHalfRepet') }"> - 매일반복</c:if></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 사유</td>
<td class="body_lt wordbreak"><u:out value="${formBodyXML.getAttr('body/leave.erpReason')}"/></td>
</tr><tr><td class="head_ct">연락처</td>
<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/leave.erpMobile') }"/></td>
</tr><tr><td class="head_ct">인수자</td>
<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/leave.erpReceiver') }"/></td>
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
<!-- <div class="color_txt"><strong>[당일 반차 신청시]</strong> 시작일만 오전/오후 체크</div>
<div class="color_txt"><strong>[예비군/민방위 신청시]</strong> 통지서 스캔하여 파일 첨부</div>
<div class="color_txt"><strong>[경조휴가 신청시]</strong> 증빙서류 스캔하여 파일 첨부 (예: 청첩장 등본 등)</div> -->
</c:if>