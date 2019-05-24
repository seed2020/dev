<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
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
<!--
//<c:if test="${formBodyMode eq 'edit'}">
function editErpUsers(){
	
	<%// html 만들기 %>
	var html = [];
	html.push('<div style="width:510px">');
	html.push('<div style="text-align:center; padding-bottom:6px;">콤마(,) 또는 엔터키로 구별하여 이름을 입력 하세요.</div>');
	html.push('<textarea rows="12" cols="80" name="erpUsers"></textarea>');
	html.push('<div class="blank"></div>');
	html.push('<div class="button_basic notPrint"><ul>');
	
	html.push('<li class="basic"><a href="javascript:void(0);" onclick="setErpUsers()" title="확인"><span>확인</span></a></li>');
	html.push('<li class="basic"><a href="javascript:void(0);" onclick="dialog.close(this)" title="닫기"><span>닫기</span></a></li>');
	
	html.push('</ul></div>');
	
	html.push('<div class="blank"></div>');
	html.push('</div>');
	
	<%// 팝업창 오픈 %>
	var option = {popHtml:html.join('')};
	dialog.open2('erpUserDialog', '이름 편집', null, option);
	
	<%// 사용자 정보 모으기 %>
	var users = [], va, idx=0;
	$("#xmlArea #xml-body input[name='erpName']").each(function(){
		va = $(this).val().trim();
		if(va != ''){
			if(idx!=0){<%// 5명 단위로 엔터 넣기 %>
				if(idx%5 == 0) { users.push(',\r\n'); }
				else { users.push(', '); }
			}
			users.push(va);
		}
		idx++;
	});
	
	<%// 팝업에 사용자 정보 세팅 %>
	$("#erpUserDialog textarea").val(users.length>0 ? users.join('') : '');
}
function setErpUsers(){
	<%// 팝업의 사용자 정보 가져오기 %>
	var va = $("#erpUserDialog textarea").val();
	va = va.replaceAll('\n', ',');
	va = va.replaceAll('\r', '');
	
	<%// 사용자 정보 배열로 만들기 %>
	var arr = [];
	va.split(',').each(function(index, value){
		value = value.trim();
		if(value!=''){
			arr.push(value);
		}
	});
	
	<%// 삭제할것, 마지막TR 정리 %>
	var $list = $("#xmlArea #xml-body .listarea[id='xml-users/user'] tbody:first");
	var dels = [], lastTr = null;
	$list.children().each(function(){
		if($(this).attr('id') == 'user'){
			dels.push(this);
		}
		if($(this).attr('id') == 'hiddenTr'){
			lastTr = this;
		}
	});
	
	<%// 삭제 %>
	dels.each(function(index, obj){
		$(obj).remove();
	});
	
	var appendHtml = lastTr.outerHTML;
	appendHtml = appendHtml.replace('hiddenTr','user');
	
	var appendObj;
	if(arr.length==0) arr.push('');
	
	<%// 추가 %>
	var seq = 1;
	arr.each(function(idx, va){
		appendObj = $(appendHtml);
		appendObj.find("#seq").text(seq++);
		appendObj.find("#erpNameTxt").text(va);
		appendObj.find("input[name='erpName']").val(va);
		appendObj.insertBefore(lastTr);
		
		appendObj.show();
		appendObj.find("input, textarea, select, button").not(".skipThese").uniform();
	});
	
	dialog.close('erpUserDialog');
}
$(document).ready(function() {
	$("#erpDate").focus().blur();
});
//</c:if>
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
}<% // 일시 replace %>
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
}<% // 체크박스 클릭 이벤트 제어 %>
function notChkEvt(obj){
	$(obj).prop('checked', !obj.checked).uniform();
}
$(document).ready(function(){
	$("#formBodyArea").find("input:visible").uniform();
	<c:if test="${formBodyMode eq 'view'}">
	$('tr#user input[type="checkbox"]').change(function(){
		notChkEvt(this);
	});
	</c:if>
});
-->
</script><c:if

	test="${formBodyMode ne 'view'}">
<div id="xmlArea" style="${formBodyMode eq 'pop' ? 'width:900px;' : ''}">

<div id="xml-head">
	<input type="hidden" name="typId" value="work"/>
	<input type="hidden" name="ver" value="1"/>
</div>

<div id="xml-body">
<u:listArea id="xml-date" colgroup="21%,*">
	<tr>
		<td class="head_ct">일자</td>
		<td><u:calendar id="erpDate" title="일자" option="{checkHandler:setDateToDocSubj}" value="${empty formBodyXML.getAttr('body/date.erpDate') and formBodyMode ne 'edit' ? 'today' : formBodyXML.getAttr('body/date.erpDate')}" /></td>
	</tr>
</u:listArea>
<u:listArea id="xml-users/user" colgroup="8%,13%,8%,8%,8%,8%,8%,8%,8%,8%,*">
	<tr id="headerTr">
		<th class="head_ct" rowspan="2">순번</th>
		<th class="head_ct" rowspan="2">이름<c:if test="${formBodyMode eq 'edit'}">
			<br/><u:buttonS title="편집" onclick="editErpUsers()" /></c:if></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork1')">주간잔업</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork2')">주간근무</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork3')">야간잔업</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork4')">평일야간</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork5')">토요주간</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork6')">토요주간<br />잔업</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork7')">토요야간</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork8')">토요야간<br />잔업</a></th>
		<th class="head_ct">비고</th>
	</tr>
	<tr id="headerTr">
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork1')">08:30<br />~21:00</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork2')">08:30<br />~17:30</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork3')">21:00<br />~08:30</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork4')">17:30<br />~01:30</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork5')">08:30<br />~17:30</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork6')">08:30<br />~21:00</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork7')">17:30<br />~01:30</a></th>
		<th class="head_ct"><a href="javascript:void(0)" onclick="checkAllByName('xmlArea', 'erpWork8')">21:00<br />~08:30</a></th>
		<th class="head_ct">(결근/조퇴)</th>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/users', 1, 1)}" var="user" varStatus="status" >
	<tr id="${status.last ? 'hiddenTr' : 'user'}" style="${status.last ? 'display:none' : ''}">
		<td id="seq" class="body_ct">${status.index + 1}</td>
		<td class="body_ct"><span id="erpNameTxt">${user.getAttr('erpName')}</span><input type="hidden" name="erpName" value="${user.getAttr('erpName')}" data-validation="Y" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork1" id="erpWork1_${status.index }" value="Y" checkValue="${user.getAttr('erpWork1')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork2" id="erpWork2_${status.index }" value="Y" checkValue="${user.getAttr('erpWork2')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork3" id="erpWork3_${status.index }" value="Y" checkValue="${user.getAttr('erpWork3')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork4" id="erpWork4_${status.index }" value="Y" checkValue="${user.getAttr('erpWork4')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork5" id="erpWork5_${status.index }" value="Y" checkValue="${user.getAttr('erpWork5')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork6" id="erpWork6_${status.index }" value="Y" checkValue="${user.getAttr('erpWork6')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork7" id="erpWork7_${status.index }" value="Y" checkValue="${user.getAttr('erpWork7')}" /></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork8" id="erpWork8_${status.index }" value="Y" checkValue="${user.getAttr('erpWork8')}" /></td>
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

<div id="xml-body">
<u:listArea id="xml-date" colgroup="21%,*">
	<tr>
		<td class="head_ct">일자</td>
		<td class="body_lt">${formBodyXML.getAttr('body/date.erpDate')}</td>
	</tr>
</u:listArea>
<div class="listarea" id="xml-users/user">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="8%"/><col width="13%"/><col width="8%"/><col width="8%"/><col width="8%"/><col width="8%"/><col width="8%"/><col width="8%"/><col width="8%"/><col width="8%"/><col width="*"/></colgroup>
	<tr id="headerTr">
		<th class="head_ct" rowspan="2">순번</th>
		<th class="head_ct" rowspan="2">이름</th>
		<th class="head_ct">주간잔업</th>
		<th class="head_ct">주간근무</th>
		<th class="head_ct">야간잔업</th>
		<th class="head_ct">평일야간</th>
		<th class="head_ct">토요주간</th>
		<th class="head_ct">토요주간<br />잔업</th>
		<th class="head_ct">토요야간</th>
		<th class="head_ct">토요야간<br />잔업</th>
		<th class="head_ct" >비고</th>
	</tr>
	<tr id="headerTr">
		<th class="head_ct">08:30<br />~21:00</th>
		<th class="head_ct">08:30<br />~17:30</th>
		<th class="head_ct">21:00<br />~08:30</th>
		<th class="head_ct">17:30<br />~01:30</th>
		<th class="head_ct">08:30<br />~17:30</th>
		<th class="head_ct">08:30<br />~21:00</th>
		<th class="head_ct">17:30<br />~01:30</th>
		<th class="head_ct">21:00<br />~08:30</th>
		<th class="head_ct">(결근/조퇴)</th>
	</tr><c:forEach
			items="${formBodyXML.getChildList('body/users', 1)}" var="user" varStatus="status" >
	<tr id="user">
		<td id="seq" class="body_ct">${status.index + 1}</td>
		<td class="body_ct"><span id="erpNameTxt">${user.getAttr('erpName')}</span></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork1" value="Y" checkValue="${user.getAttr('erpWork1')}"/></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork2" value="Y" checkValue="${user.getAttr('erpWork2')}"/></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork3" value="Y" checkValue="${user.getAttr('erpWork3')}"/></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork4" value="Y" checkValue="${user.getAttr('erpWork4')}"/></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork5" value="Y" checkValue="${user.getAttr('erpWork5')}"/></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork6" value="Y" checkValue="${user.getAttr('erpWork6')}"/></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork7" value="Y" checkValue="${user.getAttr('erpWork7')}"/></td>
		<td class="bodybg_ct"><u:checkbox name="erpWork8" value="Y" checkValue="${user.getAttr('erpWork8')}"/></td>
		<td class="body_lt wordbreak"><u:out value="${user.getAttr('erpRemarks')}" /></td>
	</tr></c:forEach>
</table></div>
</div>

<div class="blank"></div>

</div>
</c:if>