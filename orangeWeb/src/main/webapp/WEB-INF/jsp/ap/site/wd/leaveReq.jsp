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
	휴가 신청서 : wdLeaveReq
*/
// 구분 목록
String[][] erpOptions = {
		{"anb","연차"},
		{"nanb","개정연차"},
		{"repb","대체휴가"},
		{"offb","공가"}
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
		var erpOptions=$('select[name="erpOptions"]').val();
		if(erpOptions==''){
			alert("휴가 종류를 선택해 주세요.");
			return false;
		}
		if(erpOptions == 'offb') return true; 
		
		var erpTotalDay = $('#erpTotalDay').val();
		var erpUseDate=$('#erpUseDate').val();
		if(parseInt(erpTotalDay)>parseInt(erpUseDate)){
			alert("사용가능일 보다 휴가일수가 많습니다.");
			return false;
		}
		// 연차데이터 로드
		loadUserVacation(true);
		// console.log(fromToList.join(','));
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
}<% // 체크박스 클릭 이벤트 제어 %>
function notChkEvt(obj){
	$(obj).prop('checked', !obj.checked).uniform();
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
}

<% // 기간 날짜 초기화 %>
function setDateInit(){
	$('#xml-body').find('input[name="erpStart"], input[name="erpEnd"]').val('');
}

<% // 기간 일수 초기화 %>
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
	callAjax('/cm/date/getSrchDateListAjx.do?menuId=${menuId}', {start:erpStart, end:erpEnd, holiYn:'N', format:'yyyy-MM-dd'/*, onlyHoli:'Y'*/}, function(data) {
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
	//url+='&onlyHoli=Y';
		
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
};<% // 저장시에 연차 데이터 hidden 세팅 %>
function setSaveVacation(isHidden){
	if(isHidden===undefined) $('#erpDayTxt').html('');
	// 휴가 키 목록
	var buffer=[];
	var vacationKeys=['Anb', 'Nanb', 'Repb'];
	var useDate, optData, opt;
	var index=0;
	$.each(vacationKeys, function(idx, key){
		opt=key.toLowerCase();
		useDate=userVacation[opt]; // 잔여일
		if(useDate==undefined) useDate=0;
		useDate=Number(useDate);
		if(useDate==0){
			$('#leaveDayArea input[name="erp'+key+'Day"]').val('');
			return true;
		}
		
		optData=userVacation[opt+'Ongo'];
		if(optData!=undefined && optData>0){
			useDate-=Number(optData);
		}
		
		if(isHidden!=undefined && isHidden){
			$('#leaveDayArea input[name="erp'+key+'Day"]').val(useDate);
		}else{
			if(index>0) buffer.push(' , ');
			buffer.push('<span>');
			buffer.push(erpOptionMap.get(opt));
			buffer.push(': <strong>');
			buffer.push(useDate);
			buffer.push('</strong>');
			buffer.push('</span> ');
			index++;
		}
	})
	if(buffer.length>0) $('#erpDayTxt').html(buffer.join(' '));
}
<% // 수정시에 연차 데이터 로드 %>
function loadVacationData(){
	$('#erpOptions').trigger('change');
}
var erpOptionMap=null;
function setErpOptionMap(){
	if(erpOptionMap==null) erpOptionMap=new ParamMap();
	<c:forEach items="${erpOptions}" var="erpOption" varStatus="status">
	erpOptionMap.put('${erpOption[0]}', '${erpOption[1]}');
	</c:forEach>
}
<% // 연차 데이터 로드 %>
function loadUserVacation(isSave){
	callAjax('./getAnbAjx.do?menuId=${menuId}&bxId=${param.bxId}', {odurUid:$('#erpOdurUid').val()}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}else{
			// 휴가 키 목록
			var vacationKeys=['anb', 'anbMinus', 'anbOngo', 'nanb', 'nanbMinus', 'nanbUse', 'nanbOngo', 'repb', 'repbOngo', 'newYearMonth', 'newYearDay'];
			userVacation={};
			vacationKeys.each(function(idx, key){
				if(data[key]!=undefined) userVacation[key]=data[key];
			});
			
			// 현재 연도가 없을 경우 연도 세팅
			//if(userVacation['newYear']===undefined){
			//	var today=getToday();
			//	userVacation['newYear']=today.substring(0,4);
			//}
			
			if(isSave!=undefined && isSave){
				setSaveVacation(isSave);
			}else{
				setErpOptionMap(); // 휴가종류 맵으로 세팅
				loadVacationData(); // 수정시에 연차 데이터 로드
				setFromToList(); // 기간 데이터 세팅
				setSaveVacation(); // 연차 데이터 텍스트 표시
			}
		}
	});
};
var userVacation=null;<% // 휴가 종류 선택시 이벤트 %>
function chnErpOptions(obj){
	$('#txtVacaDate, #useVacaDate').html('');
	$('#leaveDayArea input[type="hidden"]').val('');
	if(obj.value=='' || obj.value=='offb') {
		return; // 공가는 연차갯수를 조회 안함
	}
	setVacationData(obj.value);
};<% // 휴가 기간 세팅 %>
function setVacationData(opt){
	if(userVacation!=null){
		var totalDate=userVacation[opt]; // 잔여일
		if(totalDate==undefined) totalDate=0;
		
		// 사용가능일
		var useDate=Number(totalDate);
		var buffer=[];
		
		// 당겨쓰기, 결재중, 사용중 
		var txtKeys=['Minus', 'Ongo', 'Use'];
		var titleKeys={minus:'당겨쓰기', ongo:'결재중', use:'사용'};
		if(totalDate<=0) totalDate=0;
		if(totalDate>0){
		buffer.push('잔여일 : ');		
		buffer.push(totalDate);
		buffer.push(' , ');
		}
		
		var optKey, optData, title, isSeperator=false, isMinus=true, maxMinus=0;
		
		// 개정연차 일 경우 사용일수와 잔여일수 계산
		if(opt=='nanb'){
			var nanbKeyList = ['nanb', 'nanbUse'];
			var totalNanbCnt=0;
			$.each(nanbKeyList, function(idx, key){
				if(userVacation[key]==undefined) return true;
				totalNanbCnt+=Number(userVacation[key]);
			});
			maxMinus=11-totalNanbCnt;
			if(maxMinus==0) isMinus=false;
		}
		
		txtKeys.each(function(idx, key){
			optKey=opt+key;
			optData=userVacation[optKey];
			if(optData!=undefined && optData>0){
				if(isSeperator) buffer.push(' , ');
				title=titleKeys[key.toLowerCase()];
				buffer.push(title);
				buffer.push(' : ');
				// 개정연차 에서 당겨쓰기 일수 초기화
				if(opt=='nanb' && key=='Minus'){
					if(!isMinus) optData=0; // 개정연차 부여일수가 11일이면 당겨쓰기 사용X
					if(optData > maxMinus) optData=maxMinus; // 개정연차 부여일수가 11일 미만이면서 당겨쓰기 가능일이 부여가능일보다 많을경우 개정연차 부여가능일(11일-개정연차) 만큼만 가능
				} 
				buffer.push(optData);
				if(key=='Minus' && isMinus) useDate+=Number(optData);
				else if(key=='Ongo') useDate-=Number(optData);
				else if(opt=='nanb' && key=='Use' && Number(optData)>10) useDate=0;
				isSeperator=true;
			}
		});
		if(buffer.length>0) $('#txtVacaDate').html(buffer.join(''));
		if(opt=='nanb' && useDate>11) useDate=11; // 개정연차는 사용가능일이 최대 11일
		var useDateTxt="사용가능일 : "+useDate;
		$('#useVacaDate').text(useDateTxt);
		$('#erpUseDate').val(useDate);
	}
};<% // 휴가 기간 초기화 %>
function initVacationData(){
	userVacation=null;
	$('#xml-body').find('input[type="text"], select, textarea').val('');
	$('#xml-body').find(':checkbox').prop('checked', false);
	$('#erpOptions').val('');
	$('#txtVacaDate, #useVacaDate').html('');
}
$(document).ready(function(){
	$("#formBodyArea").find("input:visible").uniform();
	<c:if
	test="${formBodyMode ne 'view'}">loadUserVacation();</c:if>
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
<%-- <div class="front notPrint"><div class="front_right"><u:buttonS alt="신청자 변경" title="신청자 변경" href="javascript:setUserPop();"/></div></div> --%>
<div id="xml-head">
	<input type="hidden" name="typId" value="wdLeaveReq"/><!-- wdLeaveReq, wdLeaveCan, wdRepbReq, wdRepbCan -->
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<!-- 날짜 배열 -->
<div id="xml-dates/date" style="display:none;"><c:forEach
		items="${formBodyXML.getChildList('body/dates')}" var="date">
	<div id="date"><input type="hidden" name="erpDate" value="${date.getAttr('erpDate')}"/></div></c:forEach>
</div>
<u:listArea id="xml-leave" colgroup="15%,">
	<u:set var="erpUserNm" test="${!empty formBodyXML.getAttr('body/leave.erpUserNm')}" value="${formBodyXML.getAttr('body/leave.erpUserNm') }" elseValue="${sessionScope.userVo.userNm }"
	/><u:set var="erpOdurUid" test="${!empty formBodyXML.getAttr('body/leave.erpOdurUid')}" value="${formBodyXML.getAttr('body/leave.erpOdurUid') }" elseValue="${userMap.odurUid }"
	/><u:input type="hidden" id="erpOdurUid" value="${erpOdurUid }"
	/><div id="leaveDayArea"><u:input type="hidden" id="erpAnbDay" value="${formBodyXML.getAttr('body/leave.erpAnbDay') }"
	/><u:input type="hidden" id="erpNanbDay" value="${formBodyXML.getAttr('body/leave.erpNanbDay') }"
	/><u:input type="hidden" id="erpRepbDay" value="${formBodyXML.getAttr('body/leave.erpRepbDay') }"
	/></div>
<tr><td class="head_ct"><u:mandatory />휴가 종류</td>
<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><select id="erpOptions" name="erpOptions" onchange="chnErpOptions(this);"><u:option value="" titleId="cm.select.actname" selected="${empty formBodyXML.getAttr('body/leave.erpGubun')}" />
			<c:forEach items="${erpOptions}" var="erpOption" varStatus="status">
				<u:option value="${erpOption[0]}" title="${erpOption[1]}" 
				checkValue="${empty formBodyXML.getAttr('body/leave.erpOptions') ? '' : formBodyXML.getAttr('body/leave.erpOptions')}"/>
			</c:forEach>
		</select></td><td class="width15"></td><td><div id="txtVacaDate"></div></td><td class="width15"></td><td><div id="useVacaDate" style="font-weight:bold;"></div><input type="hidden" id="erpUseDate" name="erpUseDate"/></td></tr></table></td>
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
			<td class="width15">
			<td> 휴가일수 : </td>
			<td><u:input id="erpTotalDay" value="${formBodyXML.getAttr('body/leave.erpTotalDay')}" title="휴가일수" valueOption="number" maxLength="9" style="width:50px;" valueAllowed="." mandatory="Y" onclick="fromToDatePop();"/></td>
			<td class="width15">
			<td> 일</td>
			</tr>
			</table></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 신청자</td>
<td class="body_lt" ><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="bodyip_lt"><strong>신청자명</strong></td>
			<td><u:input id="erpUserNm" value="${erpUserNm }" title="신청자명" style="width:98%;" maxByte="100" mandatory="Y" readonly="Y"/></td>
			</tr>
			</table></td>
</tr>
<tr><td class="head_ct">반차 여부</td>
<td class="body_lt"><u:checkArea id="halfDayArea">
			<u:checkbox value="Y" id="erpHalfStart" name="erpHalfStart" titleId="cols.strtYmd" alt="시작일" checkValue="${formBodyXML.getAttr('body/leave.erpHalfStart') }" onclick="chnHalfDay(this);"/><td>(</td>
			<u:radio name="erpHalfStartAmPm" value="AM" titleId="cm.option.am" checkValue="${formBodyXML.getAttr('body/leave.erpHalfStartAmPm') }"  checked="${empty formBodyXML.getAttr('body/leave.erpHalfStartAmPm')}" />
			<u:radio name="erpHalfStartAmPm" value="PM" titleId="cm.option.pm" checkValue="${formBodyXML.getAttr('body/leave.erpHalfStartAmPm') }" /><td>)</td>
			<td class="width20"></td>
			<u:checkbox value="Y" id="erpHalfEnd" name="erpHalfEnd" titleId="cols.endYmd" alt="종료일" checkValue="${formBodyXML.getAttr('body/leave.erpHalfEnd') }" onclick="chnHalfDay(this);"/><td>(</td>
			<u:radio name="erpHalfEndAmPm" value="AM" titleId="cm.option.am" checkValue="${formBodyXML.getAttr('body/leave.erpHalfEndAmPm') }" checked="${empty formBodyXML.getAttr('body/leave.erpHalfEndAmPm')}" />
			<u:radio name="erpHalfEndAmPm" value="PM" titleId="cm.option.pm" checkValue="${formBodyXML.getAttr('body/leave.erpHalfEndAmPm') }" /><td>)</td>
			</u:checkArea></td>
</tr><tr><td class="head_ct">잔여 휴가</td>
<td class="body_lt"><div id="erpDayTxt"><c:if test="${!empty formBodyXML.getAttr('body/leave.erpAnbDay')}"><span>연차 : <strong>${formBodyXML.getAttr('body/leave.erpAnbDay') }</strong></span></c:if>
	<c:if test="${!empty formBodyXML.getAttr('body/leave.erpNanbDay')}"
	><c:if test="${!empty formBodyXML.getAttr('body/leave.erpAnbDay') }"> , </c:if><span>개정연차 : <strong>${formBodyXML.getAttr('body/leave.erpNanbDay') }</strong></span></c:if>
	<c:if test="${!empty formBodyXML.getAttr('body/leave.erpRepbDay')}"
	><c:if test="${!empty formBodyXML.getAttr('body/leave.erpAnbDay') || !empty formBodyXML.getAttr('body/leave.erpRepbDay')}"> , </c:if
	><span>대체휴가 : <strong>${formBodyXML.getAttr('body/leave.erpRepbDay') }</strong></span></c:if></div></td></tr>
<tr><td class="head_ct"><u:mandatory />휴가 사유</td>
<td class="body_lt"><u:textarea id="erpReason" value="${formBodyXML.getAttr('body/leave.erpReason')}" title="휴가사유" maxByte="2000" style="width:98%;" rows="10" mandatory="Y"/></td>
</tr>
</u:listArea>
</div>

<div class="color_txt"><strong>[당일 반차 신청시]</strong> 시작일만 오전/오후 체크</div>
<div class="color_txt"><strong>[예비군/민방위 신청시]</strong> 통지서 스캔하여 파일 첨부</div>
<div class="color_txt"><strong>[경조휴가 신청시]</strong> 증빙서류 스캔하여 파일 첨부 (예: 청첩장 등본 등)</div>

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
		/> 까지 / 휴가일수 : <a href="javascript:;" onclick="fromToDatePop('view');">${formBodyXML.getAttr('body/leave.erpTotalDay')} 일</a></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 신청자</td>
<td class="body_lt" ><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="bodyip_lt"><strong>신청자명</strong></td>
			<td class="width5"></td>
			<td class="bodyip_lt"><u:out value="${formBodyXML.getAttr('body/leave.erpUserNm') }"/></td>
			</tr>
			</table></td>
</tr>
<tr><td class="head_ct">반차 여부</td>
<td class="body_lt"><c:if test="${!empty formBodyXML.getAttr('body/leave.erpHalfStart')}"
><u:set var="erpHalfStartAmPmTxt" test="${formBodyXML.getAttr('body/leave.erpHalfStartAmPm') eq 'AM'}" value="오전" elseValue="오후"
/>시작일 (<span id="erpHalfStartAmPmTxt">${erpHalfStartAmPmTxt }</span>)</c:if><c:if test="${!empty formBodyXML.getAttr('body/leave.erpHalfEnd')}"
><u:set var="erpHalfEndAmPmTxt" test="${formBodyXML.getAttr('body/leave.erpHalfEndAmPm') eq 'AM'}" value="오전" elseValue="오후"
/><c:if test="${!empty formBodyXML.getAttr('body/leave.erpHalfStart')}"
>&nbsp;</c:if>종료일 (<span id="erpHalfEndAmPmTxt">${erpHalfEndAmPmTxt }</span>)</c:if></td>
</tr><tr><td class="head_ct">잔여 휴가</td>
<td class="body_lt"><div id="erpDayTxt"><c:if test="${!empty formBodyXML.getAttr('body/leave.erpAnbDay')}"><span>연차 : <strong>${formBodyXML.getAttr('body/leave.erpAnbDay') }</strong></span></c:if>
	<c:if test="${!empty formBodyXML.getAttr('body/leave.erpNanbDay')}"
	><c:if test="${!empty formBodyXML.getAttr('body/leave.erpAnbDay') }"> , </c:if><span>개정연차 : <strong>${formBodyXML.getAttr('body/leave.erpNanbDay') }</strong></span></c:if>
	<c:if test="${!empty formBodyXML.getAttr('body/leave.erpRepbDay')}"
	><c:if test="${!empty formBodyXML.getAttr('body/leave.erpAnbDay') || !empty formBodyXML.getAttr('body/leave.erpRepbDay')}"> , </c:if
	><span>대체휴가 : <strong>${formBodyXML.getAttr('body/leave.erpRepbDay') }</strong></span></c:if></div></td></tr>
<tr><td class="head_ct"><u:mandatory />휴가 사유</td>
<td class="body_lt wordbreak"><u:out value="${formBodyXML.getAttr('body/leave.erpReason')}"/></td>
</tr>
</table></td></tr></table></div>
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