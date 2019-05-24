<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><fmt:formatDate var="today" value="<%=new Date() %>" type="date" pattern="yyyy-MM-dd"/>
<u:params var="params"/>
<u:params var="paramsForList" excludes="reqNo"/><style type="text/css">
ul.selectList{list-style:none;float:left;margin:0px;padding:0px;}
ul.selectList li{float:left;}
ul.selectList li.optionList{padding-left:5px;}
</style>
<script type="text/javascript">
<!--<%
// 참조문서 문서에 세팅 %>
function setRelReqList(reqArrs){
	var $area = $("#reqRelArea"), buffer;
	$area.html('');<%// 기존 데이터 삭제 %>
	$area.attr('data-modified','Y');<%// 변경 되었음 세팅 %>
	$area.append("<input type='hidden' name='relReqModified' value='Y' />");<%// 변경 되었음 세팅 - parameter %><%
	
	// 히든테그 만들기 - 데이타 JSON으로 변환 %>
	var attrs = ["reqNo","subj","hdlrUid","hdlrNm","cmplDt"];
	reqArrs.each(function(refIndex, refReq){
		buffer = new StringBuffer();
		buffer.append("<input type='hidden' name='relReq' value='{");
		attrs.each(function(index, attr){
			if(index!=0) buffer.append(', ');
			var va = refReq[attr].replaceAll('"','\\"');
			buffer.append('"').append(attr).append('":"').append(escapeValue(va)).append('"');
		});
		buffer.append("}' />");
		$area.append(buffer.toString());
	});
}<% // [팝업] - 관련요청 %>
function setRelReqPop(){
	var url='./setRelReqPop.do?menuId=${param.menuId}';
	<c:if test="${!empty param.reqNo}">url+='&reqNo=${param.reqNo}'</c:if>
	dialog.open('setRelReqDialog','<u:msg titleId="wh.cols.req.relReqNm" alt="관련요청" />', url);
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
}<% // 의뢰서 사용여부 %>
function resEvalUseChange(obj){
	$('#writtenReqNo').val('');
	setDisabled($('#writtenReqNo'), obj.value=='N');
}<% // 시스템 모듈 선택 %>
function selectSysMdList(obj){
	var target=$(obj).closest('li');
	$(target).nextAll().remove();
	// 담당자 select
	var pichSelect=$('#mdPichContainer select').eq(0);
	pichSelect.find('option').not(':first').remove(); // 선택을 제외하고 삭제
	setJsUniform($('#mdPichContainer'));
	
	if(obj.value=='') return;
	callAjax('./getSysMdListAjx.do?menuId=${menuId}', {mdPid:obj.value}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			addSysMdList(target, data.whMdBVoList, obj.value, pichSelect);
		}
		
		if (data.result == 'end') {
			setMdDtlList(obj.value, pichSelect);
		}
	});
}<% // 담당자 조회 %>
function setMdDtlList(mdId, pichSelect){
	callAjax('./getMdPichListAjx.do?menuId=${menuId}', {mdId:mdId}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') { // 담당자 추가
			addMdPichList(pichSelect, data.whMdPichLVoList);
		}
	});
}<% // 시스템 모듈 추가 %>
function addSysMdList(target, whMdBVoList, mdId, pichSelect){
	if(whMdBVoList==null || whMdBVoList.length==0) return;
	var buffer=[];
	var parent=$('<li></li>');
	buffer.push('<select onchange="selectSysMdList(this);" style="min-width:100px;">');
	var whMdBVo;
	buffer.push('<option value="">'+callMsg('cm.select.actname')+'</option>');
	$.each(whMdBVoList, function(index, item){
		whMdBVo=item.map;
		buffer.push('<option value="'+whMdBVo.mdId+'">'+whMdBVo.mdNm+'</option>');
	});
	buffer.push('</select>');
	
	parent.append($(buffer.join('')));
	
	if(target!=undefined){
		restoreUniform('sysMdContainer');
		$(target).after(parent);		
		var container=$('#sysMdContainer');
		if(container.scrollTop()>0){
			container.css('height', (container.height()+container.scrollTop()+5)+'px');
		}
		applyUniform('sysMdContainer');
	}
}<% // 시스템 모듈 담당자 추가 %>
function addMdPichList(target, whMdPichLVoList){
	if(whMdPichLVoList==null || whMdPichLVoList.length==0) return;
	$.each(whMdPichLVoList, function(index, item){
		whMdPichLVo=item.map;
		target.append('<option value="'+whMdPichLVo.idVa+'">'+whMdPichLVo.pichNm+'</option>');
	});
	
}<% // 시스템 모듈 선택 체크 %>
function chkSysMd(){
	return $('#sysMdContainer select:first option:selected').val()!='';
}<% // 담당자 체크 %>
function chkSysMdPich(){
	return $('#mdPichContainer select > option').length>1;	
}<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
}<% // [버튼클릭] - 임시저장  %>
function saveReq() {
	if (!validator.validate('setForm'))
		return;
	save('R');
};<% // [버튼클릭] - 저장 %>
function saveTmp() {
	if($('#setForm #subj').val()==''){
		alertMsg('cm.input.check.mandatory', ['#cols.subj']);
		$('#setForm #subj').focus();
		return;
	}
	save('T');	
};<% // [저장] 테이블 저장 %>
function save(statCd) {
	if(!chkSysMd()){
		alertMsg('wh.jsp.not.select.sysMd'); // wh.jsp.not.select.sysMd=시스템 '모듈'(을/를) 선택 후 사용해 주십시요.
		return;
	}
	//if(!chkSysMdPich()){
	//	alertMsg('wh.jsp.empty.select.sysMdPich'); // wh.jsp.empty.select.sysMdPich=시스템 '모듈' 담당자가 없습니다.관리자에게 문의하세요.
	//	return;
	//}
	var $form = $('#setForm');
	$.each($('#sysMdContainer select').get().reverse(), function(){
		if($(this).find('option:selected').val()!=''){
			$form.find('input[name="mdId"]').val($(this).find('option:selected').val());
			return false;
		}
	});
	
	if (isInUtf8Length($('#cont').val(), 0, '${bodySize}') > 0) {
		alertMsg('cm.input.check.maxbyte', [callMsg('cols.cont'), '${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
		return;
	}
	if(statCd!=undefined){
		$form.find("input[name='statCd']").remove();
		$form.appendHidden({name:'statCd',value:statCd});	
	}
	
	// 수정이면서 상태가 변경될경우(임시<=>저장) 목록으로 리다이렉트.
	<c:if test="${!empty param.reqNo}">
		if(statCd!='${whReqOngdDVo.statCd}'){
			$form.find("input[name='viewPage']").val($form.find("input[name='listPage']").val());
		}
	</c:if>
	
	$form.attr('method','post');
	$form.attr('action','./${transPage}.do?menuId=${menuId}');
	$form.attr('enctype','multipart/form-data');
	$form.attr('target','dataframe');
	<c:if test="${isEditor == true }">editor('cont').prepare();</c:if>
	saveFileToForm('${filesId}', $form[0], null);
};
$(document).ready(function() {
	setUniformCSS();
});<%
// 나모 에디터 초기화 %>
function initNamo(id){
	var bodyHtml = $("#lobHandlerArea").html();
	if(bodyHtml!=''){
		editor('cont').setInitHtml(bodyHtml);		
	}
	editor('cont').prepare();
}
//-->
</script>
<c:if test="${empty pageSuffix}"><u:msg var="menuTitle" titleId="wh.jsp.${path }.title" />
<u:title title="${menuTitle }" menuNameFirst="true" alt="${menuTitle }" /></c:if>

<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${paramsForList}" />
<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${params}" />
<u:input type="hidden" id="mdId" value="${whReqOngdDVo.mdId }" />
<c:if test="${!empty param.reqNo }"><u:input type="hidden" id="reqNo" value="${param.reqNo}" /></c:if>
<c:if test="${empty param.reqNo && isEditor == true}"><u:input type="hidden" id="htmlYn" value="Y" /></c:if>

<div id="reqRelArea"></div>

<u:title titleId="wh.jsp.req.small.title" alt="요청사항" type="small" notPrint="true">
<u:titleButton titleId="wh.cols.req.relReqNm" onclick="setRelReqPop();" alt="관련요청"/>
</u:title>
<% // 폼 필드 %>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="12%"/><col width="38%"/><col width="12%"/><col width="38%"/></colgroup>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.req.deptNm" alt="요청부서" /></td>
	<td class="body_lt"><u:out value="${sessionScope.userVo.deptNm }"/></td>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.req.reqr" alt="요청자" /></td>
	<td class="body_lt"><u:out value="${sessionScope.userVo.userNm }"/></td>
	</tr>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.jsp.sysMd.title" alt="시스템 모듈" /></td>
	<td class="body_lt"><u:set var="paramMdId" test="${!empty whReqOngdDVo && !empty whReqOngdDVo.mdId}" value="${whReqOngdDVo.mdId }" elseValue="${param.mdId }"/><div id="sysMdContainer" style="width:100%;overflow-y:auto;"><ul id="sysMdArea" class="selectList"><c:forEach items="${paramMdList}" var="whMdBVoList" varStatus="paramStatus"
		><li><select onchange="selectSysMdList(this);" style="min-width:100px;"><u:option value="" titleId="cm.select.actname" alt="선택"
		/><c:forEach items="${whMdBVoList}" var="whMdBVoVo" varStatus="status"><u:option value="${whMdBVoVo.mdId }" title="${whMdBVoVo.mdNm }" checkValue="${empty paramMdIds ? paramMdId : paramMdIds[paramStatus.index]}" /></c:forEach></select></li></c:forEach></ul></div></td>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.pich" alt="처리담당자" /></td>
	<td class="body_lt"><div id="mdPichContainer"><select name="pichUid" style="min-width:100px;"><u:option value="" titleId="cm.select.actname" alt="선택"
		/><c:if test="${!empty whMdPichLVoList }"><c:forEach var="whMdPichLVo" items="${whMdPichLVoList }" varStatus="status"><u:option value="${whMdPichLVo.idVa }" title="${whMdPichLVo.pichNm }" checkValue="${whReqOngdDVo.pichUid }"/></c:forEach></c:if></select></div></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="wh.cols.req.progrmId" alt="프로그램ID" /></td>
	<td class="body_lt"><u:input id="progrmId" titleId="wh.cols.req.progrmId" style="width:98%;" value="${whReqBVo.progrmId }" maxByte="120"/></td>
	<td class="head_lt"><u:msg titleId="wh.cols.req.progrmNm" alt="프로그램명" /></td>
	<td class="body_lt"><u:input id="progrmNm" titleId="wh.cols.req.progrmNm" style="width:98%;" value="${whReqBVo.progrmNm }" maxByte="240"/></td>
	</tr>
	<tr>
	<u:set var="writtenReqYn" test="${!empty whReqBVo.writtenReqYn && whReqBVo.writtenReqYn eq 'Y'}" value="Y" elseValue="N"/>
	<td class="head_lt"><u:msg titleId="wh.cols.req.writtenYn" alt="의뢰서여부" /></td>
	<td class="body_lt"><u:checkArea>
				<u:radio name="writtenReqYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${writtenReqYn}"  inputClass="bodybg_lt" onclick="resEvalUseChange(this);"/>
				<u:radio name="writtenReqYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${writtenReqYn }" inputClass="bodybg_lt" onclick="resEvalUseChange(this);"/>
			</u:checkArea></td>
	<td class="head_lt"><u:msg titleId="wh.cols.req.writtenNo" alt="의뢰서번호" /></td>
	<td class="body_lt"><u:input id="writtenReqNo" titleId="wh.cols.req.writtenNo" style="width:98%;" value="${whReqBVo.writtenReqNo }" maxByte="120" disabled="${writtenReqYn eq 'N' ? 'Y' : 'N'}"/></td>
	</tr>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt" colspan="3"><u:input id="subj" titleId="cols.subj" style="width:98%;" value="${whReqBVo.subj }" mandatory="Y" maxByte="240"/></td>
	</tr>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.req.reqYmd" alt="요청일" /></td>
	<td class="body_lt"><u:calendar id="reqDt" titleId="wh.cols.req.reqYmd" option="{checkHandler:onChgTodayChk}" value="${!empty whReqBVo.reqDt ? whReqBVo.reqDt : today}" /></td>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.req.cmplYmd" alt="완료희망일" /></td>
	<td class="body_lt"><u:calendar id="cmplPdt" titleId="wh.cols.req.cmplYmd" option="{checkHandler:onChgTodayChk}" value="${whReqBVo.cmplPdt}" mandatory="Y" /></td>
	</tr>
	<c:if test="${isEditor == false}">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.cont" alt="내용" /></td>
	<td class="body_lt" colspan="3"><u:textarea id="cont" titleId="cols.cont" maxByte="8000" style="width:95%" rows="15" value="${whReqBVo.cont }"/></td>
	</tr>
	</c:if>
</table>
</div>
<c:if test="${isEditor == true }">
<%
	com.innobiz.orange.web.wh.vo.WhReqBVo whReqBVo = (com.innobiz.orange.web.wh.vo.WhReqBVo)request.getAttribute("whReqBVo");
	if(whReqBVo != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(whReqBVo.getCont());
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", whReqBVo.getCont());
		}
	}
%>
<u:editor id="cont" width="100%" height="300px" module="wh" value="${_bodyHtml }" padding="2" />
<div id="lobHandlerArea" style="display:none;"><c:if test="${!empty _bodyHtml }">${_bodyHtml}</c:if><c:if test="${!empty lobHandler }"><u:clob lobHandler="${lobHandler }"/></c:if></div>
</c:if>
<c:if test="${envConfigMap.fileYn eq 'Y' }">
<u:blank />
<u:listArea>
	<tr>
	<td><u:files id="${filesId}" fileVoList="${fileVoList}" module="wh" mode="set" exts="${exts }" extsTyp="${extsTyp }"/></td>
	</tr>
</u:listArea>
</c:if>
</form>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveReq();" auth="W" />
	<c:if test="${path eq 'req' && isAdmin==false}">
	<u:button titleId="cm.btn.tmpSave" alt="임시저장" onclick="saveTmp();" auth="W" />
	</c:if>
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>
<div id="lobHandlerArea" style="display:none;"><c:if test="${!empty _bodyHtml }">${_bodyHtml}</c:if><c:if test="${!empty lobHandler }"><u:clob lobHandler="${lobHandler }"/></c:if></div>