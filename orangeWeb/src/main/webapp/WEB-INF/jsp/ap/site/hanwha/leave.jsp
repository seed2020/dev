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
	휴가(조퇴)원 : leave
*/
// 구분 목록
String[][] erpOptions = {
		{"03", "결근"},
		{"04", "오전반차"},
		{"14", "오후반차"},
		{"05", "연차휴가"},
		{"06", "훈련"},
		{"07", "생리휴가"},
		{"08", "경조휴가"},
		{"09", "대체휴무"},
	};	
request.setAttribute("erpOptions", erpOptions);
%>
<style type="text/css">
#xml-body{font-size:14px;}
#xml-body .head_title{letter-spacing:0.7em;}
</style>
<c:if
test="${formBodyMode ne 'view'}">
<script type="text/javascript">
<!--
<%//사용자 정보 변경 %>
function setUserPop(){
	var data = [];
	searchUserPop({data:data, multi:false, mode:'search'}, function(vo){
		if(vo!=null){
			var $table=$('#xml-body');
			$table.find('input[name="erpGubun"]').val(vo.refVa1);
			$table.find('input[name="erpEin"]').val(vo.ein);
			$table.find('input[name="erpDeptNm"]').val(vo.deptRescNm);
			$table.find('input[name="erpPositNm"]').val(vo.positNm);
			$table.find('input[name="erpUserNm"]').val(vo.rescNm);
			$table.find('input[name="erpPhone"]').val(vo.mbno);
			$table.find('input[name="erpUserUid"]').val(vo.userUid);
			$table.find("td#erpDeptNm").text(vo.deptRescNm);
			$table.find("td#erpPositNm").text(vo.positNm);
			$table.find("td#erpUserNm").text(vo.rescNm);
		}
	});
};
function setTotalDays(date){
	if($('#xml-body #erpStart').val()!='' && $('#xml-body #erpEnd').val()!=''){
	}
}

<%//기간 날짜 hidden 삽입 %>
function setDateHidden(arr){
	var $target=$('#xml-body #xml-dates\\/date');
	$target.html('');
	$.each(arr, function(index, val){
		$target.append('<div id="date"><input type="hidden" name="erpDate" value="'+val+'"/></div>');		
		//$target.appendHidden({name:'date',value:val});
	});
	if(isErpOpt()) return;
	$('#xml-body #erpTotalDay').val(arr.length);
}

<% // 기간 날짜 초기화 %>
function setDateInit(){
	$('#xml-body').find('input[name="erpStart"], input[name="erpEnd"]').val('');
}

<% // 기간 일수 초기화 %>
function setDayInit(){
	$('#xml-body #erpTotalDay').val('');
}

<% // 기간 날짜 조회 %>
function setFromToDate(){
	setDayInit();
	var erpStart=$('#xml-body').find('input[name="erpStart"]').val();
	var erpEnd=$('#xml-body').find('input[name="erpEnd"]').val();
	if(erpStart=='' || erpEnd=='') return;
	var $target=$('#xml-body #xml-dates\\/date');
	$target.html('');
	if(isErpOpt()) return;
	callAjax('/cm/date/getSrchDateListAjx.do?menuId=${menuId}', {start:erpStart, end:erpEnd, holiYn:'N'}, function(data) {
		if (data.message != null){
			alert(data.message);
		}
		if (data.result == 'ok') {
			if(data.fromToList==null || data.fromToList.length==0) setDateInit();
			else $('#xml-body #erpTotalDay').val(data.fromToList.length);
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
}

<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	if($('#xml-leave input[name="erpGubun"]').val()==''){
		alert('구분(회사)이/가 없어서 요청할 수 없습니다.');
		return false;
	}
	if($('#xml-leave input[name="erpEin"]').val()==''){
		alert('사번이 없어서 요청할 수 없습니다.');
		return false;
	}
	return validator.validate('xml\\-body');
}
<% // 반차 일 경우 일수를 0.5로 고정 %>
function isErpOpt(){
	var erpOptions=$('select[name="erpOptions"]').val();
	if(erpOptions!=undefined && (erpOptions=='04' || erpOptions=='14')){
		$('#xml-body #erpTotalDay').val('0.5');
		return true;
	}
	return false;
}
//-->
</script></c:if>
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
	<input type="hidden" name="typId" value="leave"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<!-- 날짜 배열 -->
<div id="xml-dates/date" style="display:none;"><c:forEach
		items="${formBodyXML.getChildList('body/dates')}" var="date">
	<div id="date"><input type="hidden" name="erpDate" value="${date.getAttr('erpDate')}"/></div></c:forEach>
</div>
<div id="xml-leave" class="listarea" style="width:100%;">
	<input type="hidden" name="erpGubun" value="${!empty formBodyXML.getAttr('body/leave.erpGubun') ? formBodyXML.getAttr('body/leave.erpGubun') : userMap.refVa1}"
	/><input type="hidden" name="erpEin" value="${!empty formBodyXML.getAttr('body/leave.erpEin') ? formBodyXML.getAttr('body/leave.erpEin') : userMap.ein}"
	/><u:set var="erpDeptNm" test="${!empty formBodyXML.getAttr('body/leave.erpDeptNm')}" value="${formBodyXML.getAttr('body/leave.erpDeptNm') }" elseValue="${sessionScope.userVo.deptNm }"
	/><u:input type="hidden" id="erpDeptNm" value="${erpDeptNm }"
	/><u:set var="erpPositNm" test="${!empty formBodyXML.getAttr('body/leave.erpPositNm')}" value="${formBodyXML.getAttr('body/leave.erpPositNm') }" elseValue="${userMap.positNm }"
	/><u:input type="hidden" id="erpPositNm" value="${erpPositNm }"
	/><u:set var="erpUserNm" test="${!empty formBodyXML.getAttr('body/leave.erpUserNm')}" value="${formBodyXML.getAttr('body/leave.erpUserNm') }" elseValue="${sessionScope.userVo.userNm }"
	/><u:input type="hidden" id="erpUserNm" value="${erpUserNm }"
	/><u:set var="erpUserUid" test="${!empty formBodyXML.getAttr('body/leave.erpUserUid')}" value="${formBodyXML.getAttr('body/leave.erpUserUid') }" elseValue="${sessionScope.userVo.userUid }"
	/><u:input type="hidden" id="erpUserUid" value="${erpUserUid }"/>
	<table class="listtable" border="0" cellpadding="10" cellspacing="0" style="border:1px solid #bfc8d2;">
	<colgroup><col width="17%"/><col width="83%"/></colgroup>
	<tr>
		<td>1. <span class="head_title"><u:msg title="소속" alt="소속" /></span><span style="float:right;">:</span></td>
		<td id="erpDeptNm">${erpDeptNm }</td>
	</tr>
	<tr>
		<td>2. <span class="head_title"><u:msg title="직위" alt="직위" /></span><span style="float:right;">:</span></td>
		<td id="erpPositNm">${erpPositNm }</td>
	</tr>
	<tr>
		<td>3. <span class="head_title"><u:msg title="성명" alt="성명" /></span><span style="float:right;">:</span></td>
		<td id="erpUserNm">${erpUserNm }</td>
	</tr>
	<tr>
		<td colspan="2">
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td>다음과 같이 </td>
			<td> <select name="erpOptions"  onchange="setFromToDate();">
			<c:forEach items="${erpOptions}" var="erpOption" varStatus="status">
				<u:option value="${erpOption[0]}" title="${erpOption[1]}" selected="${empty formBodyXML.getAttr('body/leave.erpGubun') && status.index==0}" 
				checkValue="${empty formBodyXML.getAttr('body/leave.erpOptions') ? '' : formBodyXML.getAttr('body/leave.erpOptions')}"/>
			</c:forEach>
		</select> </td>
			<td>을(를) 신청하오니 허락하여 주시기 바랍니다.</td>
			</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>4. <span class="head_title"><u:msg title="일자" alt="일자" /></span><u:mandatory /><span style="float:right;">:</span></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="erpStart" name="erpStart" option="{end:'erpEnd',onchange:setFromToDate}" title="시작일" value="${formBodyXML.getAttr('body/leave.erpStart')}" mandatory="Y"/></td>
			<td>부터</td>
			<td><u:calendar id="erpEnd" name="erpEnd" option="{start:'erpStart',onchange:setFromToDate}" title="종료일" value="${formBodyXML.getAttr('body/leave.erpEnd')}" mandatory="Y"/></td>
			<td> 까지 / </td>
			<td> 휴가일수 : </td>
			<td><u:input id="erpTotalDay" value="${formBodyXML.getAttr('body/leave.erpTotalDay')}" title="휴가일수" valueOption="number" maxLength="9" style="width:50px;" valueAllowed="." mandatory="Y" onclick="fromToDatePop();"/></td>
			<td> 일</td>
			</tr>
			</table>
		 </td>
	</tr>
	<tr>
		<td>5. <span class="head_title"><u:msg title="사유" alt="사유" /></span><u:mandatory /><span style="float:right;">:</span></td>
		<td><u:input id="erpCont" value="${formBodyXML.getAttr('body/leave.erpCont')}" title="사유" style="width:98%;" maxByte="300" mandatory="Y"/></td>
	</tr>
	<tr>
		<td>6. <span class="head_title"><u:msg title="긴급연락처" alt="긴급연락처" /></span><u:mandatory /><span style="float:right;">:</span></td>
		<td><u:input id="erpPhone" value="${empty formBodyXML.getAttr('body/leave.erpPhone') ? userMap.mbno : formBodyXML.getAttr('body/leave.erpPhone')}" title="긴급연락처" maxLength="12" minLength="10" mandatory="Y"/><!-- valueOption="number" validator="checkPhone(inputTitle, va)" onblur="fnPhoneInput(this);" onfocus="fnPhoneUnInput(this);" --></td>
	</tr>
	<tr>
		<td colspan="2">
			<ul style="margin-top:30px; padding:5px; list-style:none;">
				<li>※ 휴가일수는 공휴일 및 회사휴일을 제외하고 입력할 것</li>
				<li>※ 종목 : 결근, 반차, 훈련, 생리휴가, 연차휴가, 경조휴가</li>
				<li>※ 3일이상 병가시 진단서 첨부</li>
				<li>※ 긴급시 이외에는 발생 5일전에 신청할 것</li>
			</ul>
		</td>
	</tr>
</table></div>

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
<div class="listarea">
	<table class="listtable" border="0" cellpadding="10" cellspacing="0" style="border:1px solid #bfc8d2;">
	<colgroup><col width="17%"/><col width="83%"/></colgroup>
	<tbody>
	<tr>
		<td>1. <span class="head_title"><u:msg title="소속" alt="소속" /></span><span style="float:right;">:</span></td>
		<td>${formBodyXML.getAttr('body/leave.erpDeptNm') }</td>
	</tr>
	<tr>
		<td>2. <span class="head_title"><u:msg title="직위" alt="직위" /></span><span style="float:right;">:</span></td>
		<td>${formBodyXML.getAttr('body/leave.erpPositNm') }</td>
	</tr>
	<tr>
		<td>3. <span class="head_title"><u:msg title="성명" alt="성명" /></span><span style="float:right;">:</span></td>
		<td><a href="javascript:;" onclick="viewUserPop('${formBodyXML.getAttr('body/leave.erpUserUid') }');">${formBodyXML.getAttr('body/leave.erpUserNm') }</a></td>
	</tr>
	<tr>
		<td colspan="2">다음과 같이 <c:forEach items="${erpOptions}" var="erpOption" varStatus="status" 
			><c:if test="${formBodyXML.getAttr('body/leave.erpOptions') eq  erpOption[0]}"><strong>${erpOption[1]}</strong></c:if
			></c:forEach> 을(를) 신청하오니 허락하여 주시기 바랍니다.</td>
	</tr>
	<tr>
		<td>4. <span class="head_title"><u:msg title="일자" alt="일자" /></span><span style="float:right;">:</span></td>
		<td><fmt:parseDate var="erpStart" value="${formBodyXML.getAttr('body/leave.erpStart')}" pattern="yyyy-MM-dd"
		/><fmt:formatDate value="${erpStart}" type="date" dateStyle="long" 
		/> 부터 <fmt:parseDate var="erpEnd" value="${formBodyXML.getAttr('body/leave.erpEnd')}" pattern="yyyy-MM-dd"
		/><fmt:formatDate value="${erpEnd}" type="date" dateStyle="long" 
		/> 까지 / 휴가일수 : <a href="javascript:;" onclick="fromToDatePop('view');">${formBodyXML.getAttr('body/leave.erpTotalDay')} 일</a></td>
	</tr>
	<tr>
		<td>5. <span class="head_title"><u:msg title="사유" alt="사유" /></span><span style="float:right;">:</span></td>
		<td>${formBodyXML.getAttr('body/leave.erpCont')}</td>
	</tr>
	<tr>
		<td>6. <span class="head_title"><u:msg title="긴급연락처" alt="긴급연락처" /></span><span style="float:right;">:</span></td>
		<td>${formBodyXML.getAttr('body/leave.erpPhone')}</td>
	</tr>
	<tr>
		<td colspan="2">
			<ul style="margin-top:30px; padding:5px; list-style:none;">
				<li>※ 휴가일수는 공휴일 및 회사휴일을 제외하고 입력할 것</li>
				<li>※ 종목 : 결근, 반차, 훈련, 생리휴가, 연차휴가, 경조휴가</li>
				<li>※ 3일이상 병가시 진단서 첨부</li>
				<li>※ 긴급시 이외에는 발생 5일전에 신청할 것</li>
			</ul>
		</td>
	</tr>
	</tbody>
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