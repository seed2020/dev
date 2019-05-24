<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
// 신청자 초기 row수
request.setAttribute("initStrtCnt", "1");
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
</style><script type="text/javascript">
<!--<% // 일시 replace %>
function getDayString(date , regExp){
	return date.replace(regExp,'');
};<% // 일시 비교 %>
function fnCheckDay(today , setday){
	return today > setday ? true : false;
};<% // 일자 체크 - 오늘 날짜 이후로만 %>
function onChgTodayChk(date){
	var regExp = /[^0-9]/g;
	var today = getDayString(getToday(),regExp);
	var setday = getDayString(date,regExp);
	if(fnCheckDay(today , setday)){
		alertMsg('cm.calendar.check.dateAI');
		return true;
	}
	return false;
}<%// 선택삭제%>
function delSelRow(conId){
	var arr = getCheckedTrs(conId, "cm.msg.noSelect");
	if(arr!=null) {
		delRowInArr(arr);
		<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
	}
}
<%// 행삭제%>
function delRow(conId){
	conId=conId.replace('/','\\/');
	var len=$("#"+conId+" tbody:first tr").not('#hiddenTr').length;
	if(len<2){
		alert('최소 1줄 이상 입력해야 합니다.');
		return;
	}
	var $tr = $("#"+conId+" tbody:first #hiddenTr").prev();
	delRowInArr([$tr[0]]);
}
<%// 삭제 - 배열에 담긴 목록%>
function delRowInArr(rowArr){
	for(var i=0;i<rowArr.length;i++){
		$(rowArr[i]).remove();
	}
}
<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(conId, noSelectMsg){
	var arr=[], id, obj;
	conId=conId.replace('/','\\/');
	$("#"+conId+" tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
}
<%// 다음 Row 번호 %>
var gMaxRow = parseInt("${fn:length(formBodyXML.getChildList('body/users'))}");
<%// 행추가%>
function addRow(conId, trId, strtCnt){
	if(strtCnt==undefined) strtCnt = 0;
	strtCnt++;
	conId=conId.replace('/','\\/');
	var $tr = $("#"+conId+" tbody:first #hiddenTr");
	var div=$tr.closest('div');
	$.uniform.restore(div.find('input, textarea, select, button'));
	var html = $tr[0].outerHTML;
	html = html.replace(/_NO/g,'_'+(gMaxRow+parseInt(strtCnt)));
	gMaxRow++;
	$tr.before(html);
	$tr = $tr.prev();
	$tr.attr('id',trId);
	$tr.attr('style','');
	var seq=$("#"+conId+" tbody:first tr").not('#hiddenTr').length;
	$tr.find('td#seq').text(seq++);
	div.find("input, textarea, select, button").uniform();
	<c:if test="${formBodyMode eq 'pop'}">dialog.resize("setDocBodyHtmlDialog");</c:if>
}

<%// 전체 선택 %>
function checkAllByName(areaId, name){
	var $pop = $("#setDocBodyHtmlDialog");<%// 내용수정 - 팝업의 경우 %>
	var first = true, chked = false;
	($pop.length==0 ? $("#"+areaId) : $pop.find("#"+areaId)).find("input[name='"+name+"']").each(function(){
		if(first){
			chked = !this.checked;
			first = false;
		}
		this.checked = chked;
		$(this).uniform.update();
	});
}<% // 체크박스 클릭 이벤트 제어 %>
function notChkEvt(obj){
	$(obj).prop('checked', !obj.checked).uniform();
}
$(document).ready(function(){
	$("#formBodyArea").find("input:visible").uniform();
	<c:if test="${formBodyMode eq 'view'}">
	$('tr#user input[type="checkbox"]').change(function(){notChkEvt(this);});
	</c:if>
});

-->
</script><c:if

	test="${formBodyMode ne 'view'}">
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">

<div id="xml-head">
	<input type="hidden" name="typId" value="overtimeInput"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div style="font-size:12pt; text-align:center; color:#454545; padding:20px;">사규에 의거하여 다음과 같이 특근 신청서를 제출합니다.</div>

<div id="xml-body">
<u:set var="userStrtCnt" test="${empty formBodyXML.getChildList('body/users') }" value="${initStrtCnt }" elseValue="0"/><!-- 추가 Row수 -->
<div style="display:block;float:right;height:25px;"><u:buttonS title="행추가" onclick="addRow('xml-users\\/user', 'user', '${userStrtCnt }');" alt="일정추가"/><u:buttonS title="행삭제" onclick="delRow('xml-users/user');" alt="행삭제"/></div>
<u:listArea id="xml-overtime" colgroup="8%,13%,16%,16%,16%,16%,*" noBottomBlank="true">
	<tr id="headerTr">
		<td class="head_ct" rowspan="3">순번</td>
		<td class="head_ct" rowspan="3">이름</td>
		<td class="body_ct" colspan="2"><u:calendar id="erpStrtDate" title="일자" value="${formBodyXML.getAttr('body/overtime.erpStrtDate')}" align="center" /></td>
		<td class="body_ct" colspan="2"><u:calendar id="erpEndDate" title="일자" value="${formBodyXML.getAttr('body/overtime.erpEndDate')}" align="center" /></td>
		<td class="head_ct" rowspan="3">비고</td>
	</tr>
	<tr id="headerTr">
		<td class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpStrtDayWork')">주간</a></td>
		<td class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpStrtNightWork')">야간</a></td>
		<td class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpEndDayWork')">주간</a></td>
		<td class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpEndNightWork')">야간</a></td>
	</tr><tr id="headerTr">
		<td class="body_ct"><table border="0" cellpadding="0" cellspacing="0" style="margin:auto;">
				<tbody><tr><td><select name="erpStrtDayHrs" style="width:40px;" ><c:forEach var="strtHrs" begin="0" end="23"
				><u:set var="timeVal" test="${strtHrs < 10 }" value="0${strtHrs }" elseValue="${strtHrs }"
				/><u:option value="${timeVal}" title="${timeVal}" checkValue="${formBodyXML.getAttr('body/overtime.erpStrtDayHrs')}" /></c:forEach></select></td>
					<td class="bodyip_lt"><strong>시</strong></td>
					<td><select name="erpStrtDayMin" style="width:40px;" ><c:forEach var="strtMin" begin="0" end="5"
					><u:set var="timeVal" test="${strtMin*10 < 10 }" value="0${strtMin*10 }" elseValue="${strtMin*10 }"
					/><u:option value="${timeVal}" title="${timeVal}" checkValue="${formBodyXML.getAttr('body/overtime.erpStrtDayMin')}" /></c:forEach></select>
					</td><td class="bodyip_lt"><strong>분</strong></td></tr></tbody></table></td>
		<td class="body_ct"><table border="0" cellpadding="0" cellspacing="0" style="margin:auto;">
				<tbody><tr><td><select name="erpStrtNightHrs" style="width:40px;" ><c:forEach var="strtHrs" begin="0" end="23"
				><u:set var="timeVal" test="${strtHrs < 10 }" value="0${strtHrs }" elseValue="${strtHrs }"
				/><u:option value="${timeVal}" title="${timeVal}" checkValue="${formBodyXML.getAttr('body/overtime.erpStrtNightHrs')}" /></c:forEach></select></td>
					<td class="bodyip_lt"><strong>시</strong></td>
					<td><select name="erpStrtNightMin" style="width:40px;" ><c:forEach var="strtMin" begin="0" end="5"
					><u:set var="timeVal" test="${strtMin*10 < 10 }" value="0${strtMin*10 }" elseValue="${strtMin*10 }"
					/><u:option value="${timeVal}" title="${timeVal}" checkValue="${formBodyXML.getAttr('body/overtime.erpStrtNightMin')}" /></c:forEach></select>
					</td><td class="bodyip_lt"><strong>분</strong></td></tr></tbody></table></td>
		<td class="body_ct"><table border="0" cellpadding="0" cellspacing="0" style="margin:auto;">
				<tbody><tr><td><select name="erpEndDayHrs" style="width:40px;" ><c:forEach var="strtHrs" begin="0" end="23"
				><u:set var="timeVal" test="${strtHrs < 10 }" value="0${strtHrs }" elseValue="${strtHrs }"
				/><u:option value="${timeVal}" title="${timeVal}" checkValue="${formBodyXML.getAttr('body/overtime.erpEndDayHrs')}" /></c:forEach></select></td>
					<td class="bodyip_lt"><strong>시</strong></td>
					<td><select name="erpEndDayMin" style="width:40px;" ><c:forEach var="strtMin" begin="0" end="5"
					><u:set var="timeVal" test="${strtMin*10 < 10 }" value="0${strtMin*10 }" elseValue="${strtMin*10 }"
					/><u:option value="${timeVal}" title="${timeVal}" checkValue="${formBodyXML.getAttr('body/overtime.erpEndDayMin')}" /></c:forEach></select>
					</td><td class="bodyip_lt"><strong>분</strong></td></tr></tbody></table></td>
		<td class="body_ct"><table border="0" cellpadding="0" cellspacing="0" style="margin:auto;">
				<tbody><tr><td><select name="erpEndNightHrs" style="width:40px;" ><c:forEach var="strtHrs" begin="0" end="23"
				><u:set var="timeVal" test="${strtHrs < 10 }" value="0${strtHrs }" elseValue="${strtHrs }"
				/><u:option value="${timeVal}" title="${timeVal}" checkValue="${formBodyXML.getAttr('body/overtime.erpEndNightHrs')}" /></c:forEach></select></td>
					<td class="bodyip_lt"><strong>시</strong></td>
					<td><select name="erpEndNightMin" style="width:40px;" ><c:forEach var="strtMin" begin="0" end="5"
					><u:set var="timeVal" test="${strtMin*10 < 10 }" value="0${strtMin*10 }" elseValue="${strtMin*10 }"
					/><u:option value="${timeVal}" title="${timeVal}" checkValue="${formBodyXML.getAttr('body/overtime.erpEndNightMin')}" /></c:forEach></select>
					</td><td class="bodyip_lt"><strong>분</strong></td></tr></tbody></table></td>
	</tr>
</u:listArea><div class="blank" style="height:5px;"></div>
<u:listArea id="xml-users/user" colgroup="8%,13%,16%,16%,16%,16%,*">
	<c:forEach
			items="${formBodyXML.getChildList('body/users', 1, 1)}" var="user" varStatus="status" >
	<tr id="${status.last ? 'hiddenTr' : 'user'}" style="${status.last ? 'display:none' : ''}">
		<td id="seq" class="body_ct">${status.index + 1}</td>
		<td class="body_ct"><u:input id="erpUserNm${status.index }" name="erpUserNm" value="${user.getAttr('erpUserNm')}" title="신청자명" style="width:90%;" maxByte="100" mandatory="Y" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpStrtDayWork" id="erpStrtDayWork${status.index }" value="Y" checkValue="${user.getAttr('erpStrtDayWork')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpStrtNightWork" id="erpStrtNightWork${status.index }" value="Y" checkValue="${user.getAttr('erpStrtNightWork')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpEndDayWork" id="erpEndDayWork${status.index }" value="Y" checkValue="${user.getAttr('erpEndDayWork')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpEndNightWork" id="erpEndNightWork${status.index }" value="Y" checkValue="${user.getAttr('erpEndNightWork')}" /></td>
		<td><u:input id="erpRemarks${status.index }" name="erpRemarks" value="${user.getAttr('erpRemarks')}" title="비고" style="width:90%;" maxByte="200" mandatory="Y"/></td>
	</tr></c:forEach>
</u:listArea>
</div>

<div class="blank"></div>

<c:if
	test="${formBodyMode eq 'pop'}">
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setErpXMLPop();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</c:if>

</div></c:if><c:if
	test="${formBodyMode eq 'view'}">
<div id="xmlArea">

<div style="font-size:12pt; text-align:center; color:#454545; padding:20px;">사규에 의거하여 다음과 같이 특근 신청서를 제출합니다.</div>

<div id="xml-body">
<div class="listarea" id="xml-overtime">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="8%"/><col width="13%"/><col width="16%"/><col width="16%"/><col width="16%"/><col width="16%"/><col width="*"/></colgroup>
	<tr id="headerTr">
		<td class="head_ct" rowspan="3">순번</td>
		<td class="head_ct" rowspan="3">이름</td>
		<td class="head_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/overtime.erpStrtDate')}"/></td>
		<td class="head_ct" colspan="2"><u:out value="${formBodyXML.getAttr('body/overtime.erpEndDate')}"/></td>
		<td class="head_ct" rowspan="3">비고</td>
	</tr>
	<tr id="headerTr">
		<td class="head_ct">주간</td>
		<td class="head_ct">야간</td>
		<td class="head_ct">주간</td>
		<td class="head_ct">야간</td>
	</tr><tr id="headerTr">
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/overtime.erpStrtDayHrs')}"/>시 <u:out value="${formBodyXML.getAttr('body/overtime.erpStrtDayMin')}"/>분</td>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/overtime.erpStrtNightHrs')}"/>시 <u:out value="${formBodyXML.getAttr('body/overtime.erpStrtNightMin')}"/>분</td>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/overtime.erpEndDayHrs')}"/>시 <u:out value="${formBodyXML.getAttr('body/overtime.erpEndDayMin')}"/>분</td>
		<td class="head_ct"><u:out value="${formBodyXML.getAttr('body/overtime.erpEndNightHrs')}"/>시 <u:out value="${formBodyXML.getAttr('body/overtime.erpEndNightMin')}"/>분</td>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/users', 1)}" var="user" varStatus="status" >
	<tr id="user">
		<td id="seq" class="body_ct">${status.index + 1}</td>
		<td class="body_ct"><span id="erpNameTxt">${user.getAttr('erpUserNm')}</span></td>
		<td class="bodybg_ct"><u:checkbox name="erpStrtDayWork" value="Y" checkValue="${user.getAttr('erpStrtDayWork')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpStrtNightWork" value="Y" checkValue="${user.getAttr('erpStrtNightWork')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpEndDayWork" value="Y" checkValue="${user.getAttr('erpEndDayWork')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpEndNightWork" value="Y" checkValue="${user.getAttr('erpEndNightWork')}" /></td>
		<td class="body_lt wordbreak"><u:out value="${user.getAttr('erpRemarks')}" /></td>
	</tr></c:forEach>
</table></div>
</div>

<div class="blank"></div>

</div>
</c:if>