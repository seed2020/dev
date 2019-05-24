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
	휴가 취소 신청서 : wdLeaveCan
*/
//구분 목록
String[][] erpOptions = {
		{"anb","연차"},
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
<%//xml 수집 전에 호출함 %>
function checkFormBodyXML(){
	if(validator.validate('xml\\-body')){
		var returnVa=true;
		$.each($('#requiredArea').find('input[type="hidden"]'), function(){
			if($(this).val()==''){
				alert("결재된 문서 정보가 없습니다.");
				returnVa=false;
				return false;
			}
		});
		return returnVa;
	}
	return false;
}<%//기간 날짜 hidden 삽입 %>
function setDateHidden(arr){
	var $target=$('#xml-body #xml-dates\\/date');
	$target.html('');
	$.each(arr, function(index, val){
		$target.append('<div id="date"><input type="hidden" name="erpDate" value="'+val+'"/></div>');		
	});
}<%//기간 날짜 팝업 %>
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
}
var erpOptionMap=null;
$(document).ready(function(){
	<c:if
	test="${formBodyMode ne 'view'}">
	if(erpOptionMap==null)
		erpOptionMap=new ParamMap();
	<c:forEach items="${erpOptions}" var="erpOption" varStatus="status">
	erpOptionMap.put('${erpOption[0]}', '${erpOption[1]}');
	</c:forEach></c:if>
});
<%-- 결재된 문서 선택 --%>
function openCmplAnbPop(){
	var param = "&xmlTypId=wdLeaveReq&startAttr=body/leave.erpStart&endAttr=body/leave.erpEnd";
	dialog.open('openCmplAnbDialog', '결재된 문서 선택', './listCmplAnbPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}'+param);
}
<%-- 결재된 문서 선택 - 콜백 --%>
function setCmplAnbApvNo(apvNo){
	
	callAjax('./getAnbCmplDataAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}', {apvNo:apvNo}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if(data.returnMap!=null){
			var map=data.returnMap.map;
			$.each(map, function(key, va){
				$('#xml-leave input[name="'+key+'"]').val(va);
				$('#xml-leave span#'+key+'Txt').text(va);
			});
			if(erpOptionMap!=null){
				$('#fromToArea').show();
				$('#erpStartString').text(map['erpStartString']);
				$('#erpEndString').text(map['erpEndString']);
				
				$('#erpOptionsTxt').text(erpOptionMap.get(map['erpOptions']));
				if(map['dates']!=null){
					var arr=map['dates'].split(',');
					setDateHidden(arr);
				}
				
			}
		}
		//TODO
	});
}
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
<div class="front notPrint"><div class="front_right"><u:buttonS alt="결재된 문서 선택" title="결재된 문서 선택" href="javascript:openCmplAnbPop();"/></div></div>
<div id="xml-head">
	<input type="hidden" name="typId" value="wdLeaveCan"/><!-- wdLeaveCan, wdRepbReq, wdRepbCan -->
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<!-- 날짜 배열 -->
<div id="xml-dates/date" style="display:none;"><c:forEach
		items="${formBodyXML.getChildList('body/dates')}" var="date">
	<div id="date"><input type="hidden" name="erpDate" value="${date.getAttr('erpDate')}"/></div></c:forEach>
</div>
<u:listArea id="xml-leave" colgroup="15%,">
<div id="requiredArea">
	<u:input type="hidden" id="erpOptions" value="${formBodyXML.getAttr('body/leave.erpOptions') }"
	/><u:input type="hidden" id="erpUserNm" value="${formBodyXML.getAttr('body/leave.erpUserNm') }"
	/><u:input type="hidden" id="erpStart" value="${formBodyXML.getAttr('body/leave.erpStart') }"
	/><u:input type="hidden" id="erpEnd" value="${formBodyXML.getAttr('body/leave.erpEnd') }"
	/><u:input type="hidden" id="erpTotalDay" value="${formBodyXML.getAttr('body/leave.erpTotalDay') }"
	/><u:set var="erpOdurUid" test="${!empty formBodyXML.getAttr('body/leave.erpOdurUid')}" value="${formBodyXML.getAttr('body/leave.erpOdurUid') }" elseValue="${userMap.odurUid }"
	/><u:input type="hidden" id="erpOdurUid" value="${erpOdurUid }"
	/><u:input type="hidden" id="erpCanApvNo" value="${formBodyXML.getAttr('body/leave.erpCanApvNo') }"
	/></div>
<tr><td class="head_ct">휴가 종류</td>
<td class="body_lt"><span id="erpOptionsTxt" style="font-weight:bold;"><c:forEach items="${erpOptions}" var="erpOption" varStatus="status" 
			><c:if test="${formBodyXML.getAttr('body/leave.erpOptions') eq  erpOption[0]}">${erpOption[1]}</c:if
			></c:forEach></span></td>
</tr>
<tr><td class="head_ct">휴가 기간</td>
<td class="body_lt"><u:set var="fromToStyle" test="${empty formBodyXML.getAttr('body/leave.erpStart') }" value=" style=\"display:none;\""/><div id="fromToArea"${fromToStyle }><fmt:parseDate var="erpStart" value="${formBodyXML.getAttr('body/leave.erpStart')}" pattern="yyyy-MM-dd"
		/><span id="erpStartString"><fmt:formatDate value="${erpStart}" type="date" dateStyle="long" 
		/></span> 부터 <fmt:parseDate var="erpEnd" value="${formBodyXML.getAttr('body/leave.erpEnd')}" pattern="yyyy-MM-dd"
		/><span id="erpEndString"><fmt:formatDate value="${erpEnd}" type="date" dateStyle="long" 
		/></span> 까지 / 휴가일수 : <a href="javascript:;" onclick="fromToDatePop('view');"><span id="erpTotalDayTxt">${formBodyXML.getAttr('body/leave.erpTotalDay')}</span> 일</a></div></td>
</tr>
<tr><td class="head_ct">휴가 신청자</td>
<td class="body_lt" ><span id="erpUserNmTxt"><u:out value="${formBodyXML.getAttr('body/leave.erpUserNm') }"/></span></td>
</tr>
<tr><td class="head_ct"><u:mandatory />취소 사유</td>
<td class="body_lt"><u:textarea id="erpCancelReason" value="${formBodyXML.getAttr('body/leave.erpCancelReason')}" title="취소사유" maxByte="2000" style="width:98%;" rows="10" mandatory="Y"/></td>
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
<tr><td class="head_ct"><u:mandatory />휴가 종류</td>
<td class="body_lt"><span id="erpOptionsTxt" style="font-weight:bold;"><c:forEach items="${erpOptions}" var="erpOption" varStatus="status" 
			><c:if test="${formBodyXML.getAttr('body/leave.erpOptions') eq  erpOption[0]}">${erpOption[1]}</c:if
			></c:forEach></span></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 기간</td>
<td class="body_lt"><u:set var="fromToStyle" test="${empty formBodyXML.getAttr('body/leave.erpStart') }" value=" style=\"display:none;\""/><div id="fromToArea"${fromToStyle }><fmt:parseDate var="erpStart" value="${formBodyXML.getAttr('body/leave.erpStart')}" pattern="yyyy-MM-dd"
		/><span id="erpStartTxt"><fmt:formatDate value="${erpStart}" type="date" dateStyle="long" 
		/></span> 부터 <fmt:parseDate var="erpEnd" value="${formBodyXML.getAttr('body/leave.erpEnd')}" pattern="yyyy-MM-dd"
		/><span id="erpEndTxt"><fmt:formatDate value="${erpEnd}" type="date" dateStyle="long" 
		/></span> 까지 / 휴가일수 : <a href="javascript:;" onclick="fromToDatePop('view');"><span id="erpTotalDayTxt">${formBodyXML.getAttr('body/leave.erpTotalDay')}</span> 일</a></div></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 신청자</td>
<td class="body_lt" ><span id="erpUserNmTxt"><u:out value="${formBodyXML.getAttr('body/leave.erpUserNm') }"/></span></td>
</tr>
<tr><td class="head_ct"><u:mandatory />취소 사유</td>
<td class="body_lt wordbreak"><u:out value="${formBodyXML.getAttr('body/leave.erpCancelReason')}"/></td>
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