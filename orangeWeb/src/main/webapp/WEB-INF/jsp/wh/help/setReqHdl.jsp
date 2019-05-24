<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><c:if test="${empty pageTyp && param.ongoCd eq 'C'}"><u:params var="paramsForList" excludes="reqNo,statCd,ongoCd"/></c:if><c:if test="${empty pageTyp && param.ongoCd eq 'C'}"><style type="text/css">
ul.selectList{list-style:none;float:left;margin:0px;padding:0px;}
ul.selectList li{float:left;}
ul.selectList li.optionList{padding-left:5px;}
</style>
</c:if>
<script type="text/javascript">
<!--<% // 시스템 모듈 선택 %>
function selectSysMdPopList(obj){
	var target=$(obj).closest('li');
	$(target).nextAll().remove();
	// 담당자 select
	var pichSelect=$('#mdPichPopContainer select').eq(0);
	pichSelect.find('option').not(':first').remove(); // 선택을 제외하고 삭제
	setJsUniformPop($('#mdPichPopContainer'));
	
	// 처리유형 select
	var catSelect=$('#mdCatPopContainer select').eq(0);
	catSelect.find('option').not(':first').remove(); // 선택을 제외하고 삭제
	setJsUniformPop($('#mdCatPopContainer'));
	
	<c:if test="${!empty pageTyp && pageTyp eq 'popup'}">dialog.resize('setReqHdlDialog');</c:if>
	
	if(obj.value=='') return;
	callAjax('./getSysMdListAjx.do?menuId=${menuId}', {mdPid:obj.value}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			addSysMdPopList(target, data.whMdBVoList, obj.value, pichSelect, catSelect);
		}
		if (data.result == 'end') {
			setMdDtlList(obj.value, pichSelect, catSelect);
		}
	});
}<% // 담당자 | 유형  조회 %>
function setMdDtlList(mdId, pichSelect, catSelect){
	callAjax('./getMdPichListAjx.do?menuId=${menuId}', {mdId:mdId}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') { // 담당자 추가
			addMdPichPopList(pichSelect, data.whMdPichLVoList);
		}
	});
	callAjax('./getMdCatListAjx.do?menuId=${menuId}', {mdId:mdId}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') { // 담당자 추가
			addMdCatPopList(catSelect, data.whCatGrpLVoList);
		}
	});
}<% // 시스템 모듈 추가 %>
function addSysMdPopList(target, whMdBVoList, mdId, pichSelect, catSelect){
	if(whMdBVoList==null || whMdBVoList.length==0) return;
	var buffer=[];
	var parent=$('<li></li>');
	buffer.push('<select onchange="selectSysMdPopList(this);" style="min-width:100px;">');
	var whMdBVo;
	buffer.push('<option value="">'+callMsg('cm.select.actname')+'</option>');
	$.each(whMdBVoList, function(index, item){
		whMdBVo=item.map;
		buffer.push('<option value="'+whMdBVo.mdId+'">'+whMdBVo.mdNm+'</option>');
	});
	buffer.push('</select>');
	
	parent.append($(buffer.join('')));
	
	if(target!=undefined){
		restoreUniform('sysMdPopContainer');
		$(target).after(parent);		
		var container=$('#sysMdPopContainer');
		if(container.scrollTop()>0){
			container.css('height', (container.height()+container.scrollTop()+5)+'px');
		}
		applyUniform('sysMdPopContainer');
		<c:if test="${!empty pageTyp && pageTyp eq 'popup'}">dialog.resize('setReqHdlDialog');</c:if>
	}
}<% // 시스템 모듈 담당자 추가 %>
function addMdPichPopList(target, whMdPichLVoList){
	if(whMdPichLVoList==null || whMdPichLVoList.length==0) return;
	$.uniform.restore($(target.parent()).find('select'));
	$.each(whMdPichLVoList, function(index, item){
		whMdPichLVo=item.map;
		target.append('<option value="'+whMdPichLVo.idVa+'"'+(index==0 ? 'selected="selected"' : '')+'>'+whMdPichLVo.pichNm+'</option>');
	});
	$(target.parent()).find("select").uniform();
	
}<% // 시스템 모듈 처리유형 추가 %>
function addMdCatPopList(target, whCatGrpLVoList){
	if(whCatGrpLVoList==null || whCatGrpLVoList.length==0) return;
	$.uniform.restore($(target.parent()).find('select'));
	$.each(whCatGrpLVoList, function(index, item){
		whCatGrpLVo=item.map;
		target.append('<option value="'+whCatGrpLVo.catNo+'"'+(index==0 ? 'selected="selected"' : '')+'>'+whCatGrpLVo.catNm+'</option>');
	});
	$(target.parent()).find("select").uniform();
}<% // [하단우측버튼] - 확인 %>
function setConfirm(){
	var $form = $('#recvForm');
	<c:if test="${param.ongoCd eq 'A' || isDirectCmpl == true}">
	if(!chkSysMd()){
		alertMsg('wh.jsp.not.select.sysMd'); // wh.jsp.not.select.sysMd=시스템 '모듈'(을/를) 선택 후 사용해 주십시요.
		return;
	}
	if(!chkSysMdPich()){
		alertMsg('wh.jsp.empty.select.sysMdPich'); // wh.jsp.empty.select.sysMdPich=시스템 '모듈' 담당자가 없습니다.관리자에게 문의하세요.
		return;
	}
	<c:if test="${param.ongoCd eq 'A'}">
	if($('#recvForm select[name="pichUid"] option:selected').val()==''){
		alertMsg("cm.select.check.mandatory",["#wh.cols.recv.devPich"]);<%//cm.select.check.mandatory="{0}"(을)를 선택해 주십시요.%>
		return;
	}
	</c:if>
	<c:if test="${isDirectCmpl == true}">
	if($('#recvForm select[name="hdlrUid"] option:selected').val()==''){
		alertMsg("cm.select.check.mandatory",["#wh.cols.hdl.pich"]);<%//cm.select.check.mandatory="{0}"(을)를 선택해 주십시요.%>
		return;
	}
	</c:if>
	if($('#recvForm select[name="catNo"] option:selected').val()==''){
		alertMsg("cm.select.check.mandatory",["#wh.cols.hdl.typ"]);<%//cm.select.check.mandatory="{0}"(을)를 선택해 주십시요.%>
		return;
	}
	$.each($('#sysMdPopContainer select').get().reverse(), function(){
		if($(this).find('option:selected').val()!=''){
			$form.find("input[name='mdId']").remove();
			$form.appendHidden({name:'mdId',value:$(this).find('option:selected').val()});
			return false;
		}
	});
	</c:if>
	<c:if test="${param.ongoCd eq 'P' }">
	if($('#recvForm #cmplDueDt').val()==''){
		alertMsg("cm.input.check.mandatory",["#wh.cols.hdl.dueDt"]);<%//cm.select.check.mandatory="{0}"(을)를 입력해 주십시요.%>
		return;
	}
	</c:if>
	<c:if test="${param.ongoCd eq 'C' }">
		$form.attr('method','post');
		$form.attr('action','./transHdl.do?menuId=${menuId}');
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','dataframe');
		<c:if test="${isEditor == true }">editor('hdlCont').prepare();</c:if>
		saveFileToForm('${filesId}', $form[0], null);
	</c:if>
	<c:if test="${param.ongoCd ne 'C' }">transRecvAjx();</c:if>
	
}<% // 저장 - AJAX %>
function transRecvAjx(){
	var param=new ParamMap().getData('recvForm');
	callAjax('./transRecvAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			reloadHp(true);
			//location.replace(location.href);
		}
	});
}<% // 저장 - AJAX Form%>
function saveComplete(){
	var $form = $('#recvForm');
	$form.attr('method','post');
	$form.attr('action','./transHdl.do?menuId=${menuId}');
	$form.attr('enctype','multipart/form-data');
	$form.attr('target','dataframe');
	<c:if test="${isEditor == true }">editor('hdlCont').prepare();</c:if>
	$form[0].submit();
}<% // 시스템 모듈 선택 체크 %>
function chkSysMd(){
	var isValid=true;
	$.each($('#sysMdPopContainer select'), function(index, obj){
		if($(obj).val()==''){
			isValid=false;
			return false;
		}
	});
	return isValid;
}<% // 담당자 체크 %>
function chkSysMdPich(){
	return $('#mdPichPopContainer select > option').length>1;	
}<%//uniform 적용하기 %>
function setJsUniformPop(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}
<c:if test="${empty pageTyp && param.ongoCd eq 'C'}">
$(document).ready(function() {
	setUniformCSS();
});<%
// 나모 에디터 초기화 %>
function initNamo(id){
	var bodyHtml = $("#lobHandlerArea").html();
	if(bodyHtml!=''){
		editor('hdlCont').setInitHtml(bodyHtml);		
	}
	editor('hdlCont').prepare();
}
<% // 리로드 %>
function reloadHdl() {
	<c:if test="${isAdmin==true }">location.href = './viewReq.do?${paramsForList}&reqNo=${param.reqNo}';</c:if>
	<c:if test="${empty isAdmin || isAdmin==false }">location.href = './viewHdl.do?${paramsForList}&reqNo=${param.reqNo}';</c:if>
}
</c:if>
<c:if test="${!empty pageTyp && pageTyp eq 'popup'}">
<% // 리로드 %>
function reloadHdl(isList) {
	reloadHp(isList);
}
</c:if>
<% // 일시 replace %>
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
}
//-->
</script>
<c:if test="${empty pageTyp && param.ongoCd eq 'C'}"><u:msg var="menuTitle" titleId="wh.jsp.complete.title" />
<u:title title="${menuTitle }" alt="${menuTitle }" /></c:if>
<u:set var="style" test="${!empty pageTyp && pageTyp eq 'popup'}" value="width:${param.ongoCd eq 'C' ? '700' : '550' }px;" elseValue="width:100%;"/>
<div style="${style}">
<form id="recvForm">
<c:if test="${!empty param.reqNo }"><u:input type="hidden" id="reqNo" value="${param.reqNo }"/></c:if>
<c:if test="${!empty param.reqNos }"><u:input type="hidden" id="reqNos" value="${param.reqNos }"/></c:if>
<c:if test="${param.ongoCd eq 'C' && !empty prevStatCd }"><u:input type="hidden" id="prevStatCd" value="${prevStatCd }"/></c:if>
<c:if test="${!empty isEditor && isEditor == true}"><u:input type="hidden" id="htmlYn" value="Y" /></c:if>
<u:input type="hidden" id="statCd" value="${param.ongoCd }"/>

<u:listArea id="listArea" colgroup="15%,35%,15%,35%"><%-- <tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.prgStat" alt="진행상태" /></td>
	<td class="body_lt" colspan="3"><select name="statCd" style="min-width:100px;"
	><c:forTokens var="statCd" items="A,G,P,C" delims=","><u:option value="A" titleId="wh.option.statCd${statCd }" /></c:forTokens></select></td></tr> --%>
	<c:if test="${param.ongoCd eq 'A'}"><tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.jsp.sysMd.title" alt="시스템 모듈" /></td>
	<td class="body_lt" colspan="3"><u:set var="paramMdId" test="${!empty whReqOngdDVo && !empty whReqOngdDVo.mdId}" value="${whReqOngdDVo.mdId }" elseValue="${param.mdId }"/><div id="sysMdPopContainer" style="width:100%;overflow-y:auto;"><ul id="sysMdArea" class="selectList"><c:forEach items="${paramMdList}" var="whMdBVoList" varStatus="paramStatus"
		><li><select onchange="selectSysMdPopList(this);" style="min-width:100px;"><u:option value="" titleId="cm.select.actname" alt="선택"
		/><c:forEach items="${whMdBVoList}" var="whMdBVoVo" varStatus="status"><u:option value="${whMdBVoVo.mdId }" title="${whMdBVoVo.mdNm }" checkValue="${empty paramMdIds ? paramMdId : paramMdIds[paramStatus.index]}" /></c:forEach></select></li></c:forEach></ul></div></td>
	</tr><tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.recv.devPich" alt="개발담당자" /></td>
	<td class="body_lt"><div id="mdPichPopContainer"><select name="pichUid" style="min-width:100px;"><u:option value="" titleId="cm.select.actname" alt="선택"
		/><c:if test="${!empty whMdPichLVoList }"
	><c:forEach var="whMdPichLVo" items="${whMdPichLVoList }" varStatus="status"
	><u:option value="${whMdPichLVo.idVa }" title="${whMdPichLVo.pichNm }" checkValue="${whReqOngdDVo.pichUid }"/></c:forEach></c:if></select></div></td>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.hdl.typ" alt="처리유형" /></td>
	<td class="body_lt"><div id="mdCatPopContainer"><select name="catNo" style="min-width:100px;" <u:elemTitle titleId="wh.cols.hdl.typ" />
		><u:option value="" titleId="cm.select.actname" alt="선택"	/><c:forEach var="whCatGrpLVo" items="${whCatGrpLVoList }" varStatus="status"
		><u:option value="${whCatGrpLVo.catNo}" title="${whCatGrpLVo.catNm}" selected="${status.first}"/></c:forEach></select></div></td>
	</tr></c:if>
	
	<c:if test="${param.ongoCd eq 'A'}"><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.recv.recvr" alt="접수자" /></td>
	<td class="body_lt"><u:out value="${sessionScope.userVo.userNm }"/><u:input type="hidden" id="recvUid" value="${sessionScope.userVo.userUid }"/></td>
	<td class="head_lt"><u:msg titleId="wh.cols.recv.recvYmd" alt="접수일" /></td>
	<td class="body_lt"><u:calendar id="recvDt" titleId="wh.cols.recv.recvYmd" value="today" option="{checkHandler:onChgTodayChk}"/></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.recv.recvCont" /></td>
	<td class="body_lt" colspan="3"><u:textarea id="recvCont" titleId="wh.cols.recv.recvCont" maxByte="400" style="width:95%" rows="7" /></td></tr></c:if>
	<c:if test="${param.ongoCd eq 'G'}"><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.recv.rjtr" alt="반려자" /></td>
	<td class="body_lt"><u:out value="${sessionScope.userVo.userNm }"/><u:input type="hidden" id="recvUid" value="${sessionScope.userVo.userUid }"/></td>
	<td class="head_lt"><u:msg titleId="wh.cols.recv.rjtYmd" alt="반려일" /></td>
	<td class="body_lt"><u:calendar id="recvDt" titleId="wh.cols.recv.rjtYmd" value="today" option="{checkHandler:onChgTodayChk}"/></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.recv.rejectCont" /></td>
	<td class="body_lt" colspan="3"><u:textarea id="recvCont" titleId="wh.cols.recv.recvCont" maxByte="400" style="width:95%" rows="7" /></td></tr></c:if>

	<c:if test="${param.ongoCd eq 'P'}"><tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.recv.devPich" alt="개발담당자" /></td>
	<td class="body_lt"><select name="pichUid" style="min-width:100px;"><u:option value="" titleId="cm.select.actname" alt="선택"
		/><c:if test="${!empty whMdPichLVoList }"
	><c:forEach var="whMdPichLVo" items="${whMdPichLVoList }" varStatus="status"
	><u:option value="${whMdPichLVo.idVa }" title="${whMdPichLVo.pichNm }" checkValue="${whReqOngdDVo.pichUid }"/></c:forEach></c:if></select></td>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.hdl.dueDt" alt="처리예정일" /></td>
	<td class="body_lt"><u:calendar id="cmplDueDt" titleId="wh.cols.hdl.dueDt" value="${!empty whReqOngdDVo.cmplDueDt ? whReqOngdDVo.cmplDueDt : 'today'}" option="{checkHandler:onChgTodayChk}" /></td></tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.ongoCont" alt="진행사항"/></td>
	<td class="body_lt" colspan="3"><u:textarea id="ongoCont" titleId="wh.cols.hdl.hdlCont" maxByte="400" style="width:95%" rows="7" value=""/></td></tr></c:if>
	
	<c:if test="${param.ongoCd eq 'C' }"><c:if test="${isDirectCmpl == true }"><tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.jsp.sysMd.title" alt="시스템 모듈" /></td>
	<td class="body_lt" colspan="3"><u:set var="paramMdId" test="${!empty whReqOngdDVo && !empty whReqOngdDVo.mdId}" value="${whReqOngdDVo.mdId }" elseValue="${param.mdId }"/><div id="sysMdPopContainer" style="width:100%;overflow-y:auto;"><ul id="sysMdArea" class="selectList"><c:forEach items="${paramMdList}" var="whMdBVoList" varStatus="paramStatus"
		><li><select onchange="selectSysMdPopList(this);" style="min-width:100px;"><u:option value="" titleId="cm.select.actname" alt="선택"
		/><c:forEach items="${whMdBVoList}" var="whMdBVoVo" varStatus="status"><u:option value="${whMdBVoVo.mdId }" title="${whMdBVoVo.mdNm }" checkValue="${empty paramMdIds ? paramMdId : paramMdIds[paramStatus.index]}" /></c:forEach></select></li></c:forEach></ul></div></td>
	</tr><tr>	
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.hdl.typ" alt="처리유형" /></td>
	<td class="body_lt" colspan="3"><div id="mdCatPopContainer"><select name="catNo" style="min-width:100px;" <u:elemTitle titleId="wh.cols.hdl.typ" />
		><u:option value="" titleId="cm.select.actname" alt="선택"	/><c:forEach var="whCatGrpLVo" items="${whCatGrpLVoList }" varStatus="status"
		><u:option value="${whCatGrpLVo.catNo}" title="${whCatGrpLVo.catNm}" selected="${status.first}"/></c:forEach></select></div></td>
	</tr>	</c:if><tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.hdl.pich" alt="처리담당자" /></td>
	<td class="body_lt"><div <c:if test="${isDirectCmpl == true }">id="mdPichPopContainer"</c:if>><select name="hdlrUid" style="min-width:100px;"><u:option value="" titleId="cm.select.actname" alt="선택"
		/><c:if test="${!empty whMdPichLVoList }"
	><u:set var="setHdlrUid" test="${!empty whReqOngdDVo.hdlrUid }" value="${whReqOngdDVo.hdlrUid }" elseValue="${whReqOngdDVo.pichUid }"/><c:forEach var="whMdPichLVo" items="${whMdPichLVoList }" varStatus="status"
	><u:option value="${whMdPichLVo.idVa }" title="${whMdPichLVo.pichNm }" checkValue="${setHdlrUid }"/></c:forEach></c:if></select></div></td>
	<td class="head_lt"><u:msg titleId="wh.cols.req.cmplDt" alt="완료일" /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0"><tr><td><u:calendar id="cmplDt" titleId="wh.cols.req.cmplDt" value="${empty whReqCmplDVo ? 'today' : whReqOngdDVo.cmplDt }" option="{checkHandler:onChgTodayChk}"/></td>
				<c:if test="${!empty hstYn && hstYn eq 'Y' && envConfigMap.cmplHstYn eq 'Y' }"><td class="width10"></td><td><u:checkArea><u:checkbox value="Y" name="hstYn" titleId="wh.cols.cmpl.hstAdd" alt="이력생성" /></u:checkArea></td></c:if></tr></table>	
	</td>
	</tr><c:if test="${envConfigMap.mailSendTgt ne 'none' || envConfigMap.devHourYn eq 'Y' }"><tr><c:if test="${envConfigMap.mailSendTgt ne 'none' }"><td class="head_lt"><u:msg titleId="wh.cols.mail.send" alt="메일 발송"/></td>
	<td class="body_lt" <c:if test="${envConfigMap.devHourYn ne 'Y' }">colspan="3"</c:if>><u:checkArea>
	<u:checkbox value="Y" name="mailSendYn" titleId="wh.cols.mail.send.reqr" alt="요청자에게 메일 발송" checked="${empty hstYn || hstYn eq 'N' }"/></u:checkArea></td></c:if>
	<c:if test="${envConfigMap.devHourYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="wh.cfg.devHour" alt="공수"/></td>
	<td class="body_lt" <c:if test="${empty envConfigMap.mailSendTgt || envConfigMap.mailSendTgt eq 'none'}">colspan="3"</c:if>><u:input id="devHourVa" titleId="wh.cfg.devHour" style="width:60px;" value="${empty whReqCmplDVo.devHourVa ? '1.0' : whReqCmplDVo.devHourVa}" maxLength="8" valueOption="number" valueAllowed="." commify="Y"/></td>
	</c:if></tr></c:if><c:if test="${envConfigMap.hdlCompYn eq 'Y' }"><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.compNm" alt="처리업체명" /></td>
	<td class="body_lt"><u:input id="hdlCompNm" titleId="wh.cols.hdl.compNm" style="width:94%;" maxByte="120" value="${whReqCmplDVo.hdlCompNm }"/></td>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.hdlCost" alt="처리비용" /></td>
	<td class="body_lt"><u:input id="hdlCost" titleId="wh.cols.hdl.hdlCost" style="width:94%;" maxByte="13" valueOption="number" commify="Y" value="${whReqCmplDVo.hdlCost }"/></td>
	</tr></c:if><c:if test="${envConfigMap.testInfoYn eq 'Y' }"><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.test" alt="테스트" /></td>
	<td colspan="3" style="padding:3px;"><u:listArea colgroup="15%,35%,15%,35%"><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.testPich" alt="테스트 담당자" /></td>
	<td class="body_lt"><select name="testPichUid" style="min-width:100px;"><u:option value="" titleId="cm.select.actname" alt="선택" selected="${empty whReqCmplDVo.testPichUid }"/><c:if test="${!empty whMdPichLVoList }"
	><c:forEach var="whMdPichLVo" items="${whMdPichLVoList }" varStatus="status"
	><u:option value="${whMdPichLVo.idVa }" title="${whMdPichLVo.pichNm }" checkValue="${whReqCmplDVo.testPichUid }"/></c:forEach></c:if></select></td>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.testDueDt" alt="테스트 기간" /></td>
	<td class="body_lt" ><table border="0" cellpadding="0" cellspacing="0"><tbody><tr><td>
	<u:calendar id="strtDt" option="{end:'endDt'}" titleId="cols.strtYmd" alt="시작일" value="${whReqCmplDVo.strtDt }"/></td><td>
	<u:calendar id="endDt" option="{start:'strtDt'}" titleId="cols.endYmd" alt="종료일" value="${whReqCmplDVo.endDt }"/></td></tr></table></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.testList" alt="테스트 진행내역" /></td>
	<td class="body_lt" colspan="3"><u:textarea id="testOngoCont" titleId="wh.cols.hdl.testList" maxByte="400" style="width:95%" rows="5" value="${whReqCmplDVo.testOngoCont }"/></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.testResult" alt="테스트 결과" /></td>
	<td class="body_lt" colspan="3"><u:textarea id="testResCont" titleId="wh.cols.hdl.testResult" maxByte="400" style="width:95%" rows="5" value="${whReqCmplDVo.testResCont }"/></td>
	</tr>		
	</u:listArea></td></tr></c:if><c:if test="${isEditor == false }"><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.hdlCont" alt="완료처리 사항" /></td>
	<td class="body_lt" colspan="3"><u:textarea id="hdlCont" titleId="wh.cols.hdl.hdlCont" maxByte="8000" style="width:95%" rows="10" value="${whReqCmplDVo.hdlCont }"/></td></tr></c:if></c:if>
</u:listArea>
<c:if test="${param.ongoCd eq 'C' && isEditor == true }">
<%
	com.innobiz.orange.web.wh.vo.WhReqCmplDVo whReqCmplDVo = (com.innobiz.orange.web.wh.vo.WhReqCmplDVo)request.getAttribute("whReqCmplDVo");
	if(whReqCmplDVo != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(whReqCmplDVo.getHdlCont());
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", whReqCmplDVo.getHdlCont());
		}
	}
%>
<c:if test="${!empty pageTyp && pageTyp eq 'popup'}">
<div id="contArea" class="listarea" style="width:100%; height:${empty namoEditorEnable ? 250 : 306}px; padding-top:2px"></div>
<u:editor id="hdlCont" width="100%" height="${empty namoEditorEnable ? 250 : 300}px" module="wh" value="${_bodyHtml }" areaId="contArea" namoToolbar="wcPop" />
</c:if>

<c:if test="${empty pageTyp}">
<u:editor id="hdlCont" width="100%" height="300px" module="wh" value="${_bodyHtml }" padding="2" />
<div id="lobHandlerArea" style="display:none;"><c:if test="${!empty _bodyHtml }">${_bodyHtml}</c:if><c:if test="${!empty cmplLobHandler }"><u:clob lobHandler="${cmplLobHandler }"/></c:if></div>
</c:if>


</c:if>
<c:if test="${param.ongoCd eq 'C' && fileYn eq 'Y'}">
<u:listArea>
	<tr>
	<td><u:files id="${filesId}" fileVoList="${fileVoList}" module="wh" mode="set" exts="${exts }" extsTyp="${extsTyp }"/></td>
	</tr>
</u:listArea>
</c:if>
</form>
	<u:blank />
	<% // 하단 버튼 %>
<u:buttonArea style="clear:both;">
	<u:button titleId="cm.btn.save" onclick="setConfirm();" alt="저장" />
	<c:if test="${!empty pageTyp && pageTyp eq 'popup'}"><u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" /></c:if>
	<c:if test="${empty pageTyp}"><u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" /></c:if>
</u:buttonArea>
</div>
