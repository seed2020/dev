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
		{"01","연차"},
		{"03","병가"},
		{"06","훈련"},
		{"07","공가"},
		{"08","경조휴가"},
		{"11","교육"},
		{"12","반차"},
		{"15","생리휴가"},
		{"17","육아휴직"},
		{"18","하계휴가"},
		{"98","국내출장"},
		{"99","해외출장"},
		{"00","기타(외출,지각,조퇴,결근)"}
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
			$table.find('input[name="erpEin"]').val(vo.ein);
			$table.find('input[name="erpUserNm"]').val(vo.rescNm);
			$table.find('input[name="erpUserUid"]').val(vo.userUid);
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
	if(validator.validate('xml\\-body') && chkValidUser())
		return true;
	return false;
}<% // [버튼] - ERP 사용자 검증 %>
function chkValidUser(){
	if($('#erpUserNm').val()=='' || $('#erpEin').val()=='') return false;
	var returnVa=false;
	callAjax('./chkNausErpEinAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}', {erpUserNm:$('#erpUserNm').val(), erpEin:$('#erpEin').val()}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			if(data.isValid!='Y') {
				alert("존재하지 않는 사용자 입니다.");
				returnVa=false;
			}else{
				returnVa=true;
			}
		}
	}, null, null, false);
	return returnVa;
}<% // 체크박스 클릭 이벤트 제어 %>
function notChkEvt(obj){
	$(obj).prop('checked', !obj.checked).uniform();
}<% // 기간 날짜 조회 %>
function setFromToDate(){
	var erpStart=$('#xml-body').find('input[name="erpStart"]').val();
	var erpEnd=$('#xml-body').find('input[name="erpEnd"]').val();
	if(erpStart=='' || erpEnd=='') return;
	initDateHalf();
}<% // 반차 체크할 경우 날짜 비교 %>
function chnHalfDay(obj){
	var erpStart=$('#xml-body').find('input[name="erpStart"]').val();
	var erpEnd=$('#xml-body').find('input[name="erpEnd"]').val();
	if(erpStart=='' || erpEnd=='') return;
	
	if(erpStart==erpEnd && $('#halfDayArea #erpHalfStart').is(':checked') && $('#halfDayArea #erpHalfEnd').is(':checked')){
		initDateHalf(obj);
	}
	
}<% // 반차 초기화 %>
function initDateHalf(obj){
	var target=obj!=undefined ? $(obj) : $('#xml-body').find('input[name="erpHalfStart"], input[name="erpHalfEnd"]');
	target.prop('checked', false);
	//target.uniform();
	setJsUniform($('#halfDayArea'));
}<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}
$(document).ready(function(){
	$("#formBodyArea").find("input:visible").uniform();
	<c:if test="${formBodyMode eq 'view'}">
	$('#halfYnArea input[type="checkbox"]').change(function(){
		notChkEvt(this);
	});
	$('#halfYnArea input[type="radio"]').change(function(){
		$data=$(this).attr('data-extra');
		if($data){
			$name=$(this).attr('name');
			$('input:radio[name="'+$name+'"]').prop('checked',false);
			$('input:radio[name="'+$name+'"]:input[value='+$data+']').prop('checked',true);
			$('input:radio[name="'+$name+'"]').uniform();
		}
	});
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
	<input type="hidden" name="typId" value="leave"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<u:listArea id="xml-leave" colgroup="15%,">
	<u:set var="erpEin" test="${!empty formBodyXML.getAttr('body/leave.erpEin')}" value="${formBodyXML.getAttr('body/leave.erpEin') }" elseValue="${userMap.ein }"
	/><u:set var="erpUserNm" test="${!empty formBodyXML.getAttr('body/leave.erpUserNm')}" value="${formBodyXML.getAttr('body/leave.erpUserNm') }" elseValue="${sessionScope.userVo.userNm }"
	/><u:set var="erpUserUid" test="${!empty formBodyXML.getAttr('body/leave.erpUserUid')}" value="${formBodyXML.getAttr('body/leave.erpUserUid') }" elseValue="${sessionScope.userVo.userUid }"
	/><u:input type="hidden" id="erpUserUid" value="${erpUserUid }"/>
<%-- <tr><td class="head_ct"><u:mandatory />제목</td>
<td class="body_lt"><u:input id="erpSubj" value="${formBodyXML.getAttr('body/leave.erpSubj')}" title="제목" style="width:98%;" maxByte="300" mandatory="Y"/></td>
</tr> --%>
<tr><td class="head_ct"><u:mandatory />휴가 종류</td>
<td class="body_lt"><select name="erpOptions">
			<c:forEach items="${erpOptions}" var="erpOption" varStatus="status">
				<u:option value="${erpOption[0]}" title="${erpOption[1]}" selected="${empty formBodyXML.getAttr('body/leave.erpGubun') && status.index==0}" 
				checkValue="${empty formBodyXML.getAttr('body/leave.erpOptions') ? '' : formBodyXML.getAttr('body/leave.erpOptions')}"/>
			</c:forEach>
		</select></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 기간</td>
<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="erpStart" name="erpStart" option="{end:'erpEnd',onchange:setFromToDate,checkHandler:onChgTodayChk}" title="시작일" value="${formBodyXML.getAttr('body/leave.erpStart')}" mandatory="Y"/></td>
			<td>~</td>
			<td><u:calendar id="erpEnd" name="erpEnd" option="{start:'erpStart',onchange:setFromToDate,checkHandler:onChgTodayChk}" title="종료일" value="${formBodyXML.getAttr('body/leave.erpEnd')}" mandatory="Y"/></td>
			</tr>
			</table></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 신청자</td>
<td class="body_lt" ><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="bodyip_lt"><strong>신청자명</strong></td>
			<td><u:input id="erpUserNm" value="${erpUserNm }" title="신청자명" style="width:98%;" maxByte="100" mandatory="Y"/></td>
			<td class="width15"></td>
			<td class="bodyip_lt"><strong>사번</strong></td>
			<td><u:input id="erpEin" value="${erpEin }" title="사번" style="width:98%;" maxByte="100" mandatory="Y"/></td>
			<td class="width15"></td>
			<td><u:buttonS alt="검증" title="검증" href="javascript:chkValidUser();"/></td>
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
</tr>
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
<%-- <tr><td class="head_ct"><u:mandatory />제목</td>
<td class="body_lt"><u:out value="${formBodyXML.getAttr('body/leave.erpSubj')}"/></td>
</tr> --%>
<tr><td class="head_ct"><u:mandatory />휴가 종류</td>
<td class="body_lt"><c:forEach items="${erpOptions}" var="erpOption" varStatus="status" 
			><c:if test="${formBodyXML.getAttr('body/leave.erpOptions') eq  erpOption[0]}"><strong>${erpOption[1]}</strong></c:if
			></c:forEach></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 기간</td>
<td class="body_lt"><fmt:parseDate var="erpStart" value="${formBodyXML.getAttr('body/leave.erpStart')}" pattern="yyyy-MM-dd"
		/><fmt:formatDate value="${erpStart}" type="date" dateStyle="long" 
		/>~<fmt:parseDate var="erpEnd" value="${formBodyXML.getAttr('body/leave.erpEnd')}" pattern="yyyy-MM-dd"
		/><fmt:formatDate value="${erpEnd}" type="date" dateStyle="long" 
		/></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 신청자</td>
<td class="body_lt" ><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td class="bodyip_lt"><strong>신청자명</strong></td>
			<td class="width5"></td>
			<td class="bodyip_lt"><u:out value="${formBodyXML.getAttr('body/leave.erpUserNm') }"/></td>
			<td class="width15"></td>
			<td class="bodyip_lt"><strong>사번</strong></td>
			<td class="width5"></td>
			<td class="bodyip_lt"><u:out value="${formBodyXML.getAttr('body/leave.erpEin') }"/></td>
			</tr>
			</table></td>
</tr>
<tr><td class="head_ct">반차 여부</td>
<td class="body_lt"><u:checkArea id="halfYnArea">
			<u:checkbox value="Y" name="erpHalfStart" titleId="cols.strtYmd" alt="시작일" checkValue="${formBodyXML.getAttr('body/leave.erpHalfStart') }" /><td>(</td>
			<u:set var="erpHalfStartAmPm" test="${empty formBodyXML.getAttr('body/leave.erpHalfStartAmPm')}" value="AM" elseValue="${formBodyXML.getAttr('body/leave.erpHalfStartAmPm') }"/>
			<u:radio name="erpHalfStartAmPm" value="AM" titleId="cm.option.am" checkValue="${erpHalfStartAmPm }" extraData="${erpHalfStartAmPm }" />
			<u:radio name="erpHalfStartAmPm" value="PM" titleId="cm.option.pm" checkValue="${erpHalfStartAmPm }" extraData="${erpHalfStartAmPm }" /><td>)</td>
			<td class="width20"></td>
			<u:checkbox value="Y" name="erpHalfEnd" titleId="cols.endYmd" alt="종료일" checkValue="${formBodyXML.getAttr('body/leave.erpHalfEnd') }" /><td>(</td>
			<u:set var="erpHalfEndAmPm" test="${empty formBodyXML.getAttr('body/leave.erpHalfEndAmPm')}" value="AM" elseValue="${formBodyXML.getAttr('body/leave.erpHalfEndAmPm') }"/>
			<u:radio name="erpHalfEndAmPm" value="AM" titleId="cm.option.am" checkValue="${erpHalfEndAmPm}" extraData="${erpHalfEndAmPm }"/>
			<u:radio name="erpHalfEndAmPm" value="PM" titleId="cm.option.pm" checkValue="${erpHalfEndAmPm}" extraData="${erpHalfEndAmPm }"/><td>)</td>
			</u:checkArea></td>
</tr>
<tr><td class="head_ct"><u:mandatory />휴가 사유</td>
<td class="body_lt wordbreak"><u:out value="${formBodyXML.getAttr('body/leave.erpReason')}"/></td>
</tr>
</table></div>

<div class="color_txt"><strong>[당일 반차 신청시]</strong> 시작일만 오전/오후 체크</div>
<div class="color_txt"><strong>[예비군/민방위 신청시]</strong> 통지서 스캔하여 파일 첨부</div>
<div class="color_txt"><strong>[경조휴가 신청시]</strong> 증빙서류 스캔하여 파일 첨부 (예: 청첩장 등본 등)</div>
</c:if>