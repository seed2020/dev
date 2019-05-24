<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:out value="${ctBullMastBVo.bullExprDt}" type="date" var="bullExprYmd" />
<u:out value="${ctBullMastBVo.bullExprDt}" type="hm" var="bullExprHm" />
<script type="text/javascript">
<!--

//오늘 시간 리턴
function getToTime(){
	var d = new Date();
	return (d.getHours() < 10 ? "0"+d.getHours() : d.getHours() ) +":"+ (d.getMinutes() < 10 ? "0"+d.getMinutes() : d.getMinutes() );
}

<% // 일시 replace %>
function getDayString(date , regExp){
	return date.replace(regExp,'');
};
<% // 일시 비교 %>
function fnCheckDay(today , setday){
	return today > setday ? true : false;
};
<% // 게시완료일 체크 %>
function onBullExprDayChange(date){
	var regExp = /[^0-9]/g;
	var today = getDayString(getToday(),regExp);
	var setday = getDayString(date,regExp);
	if(fnCheckDay(today , setday)){
		//cm.calendar.check.dateAI=현재시간 포함 이후만 선택가능합니다.
		alertMsg('cm.calendar.check.dateAI');
		return true;
	}	
	return false;
};

<% // 예약시간 체크 %>
function onBullRezvTimeChange(objId , setday , initTime){
	var regExp = /[^0-9]/g;
	var today = getDayString(getToday(),regExp);
	setday = getDayString(setday,regExp);
	if(today == setday ){
		var toTime = getToTime().replace(/[^0-9]/g,'');
		var setTime = $('#'+objId).val().replace(/[^0-9]/g,'');
		if(fnCheckDay(toTime , setTime)){
			alertMsg('cm.calendar.check.dateAI');
			$('#'+objId).val(initTime);
			setUniformCSS();
		}
	}
};

<% // 완료일시 세팅 %>
function setExprDt() {
	if ($('#bullExprYmd').val() != '') {
		$('#bullExprDt').val($('#bullExprYmd').val() + ' ' + $('#bullExprHm').val() + ':00');
	}
}

function saveBull(){
	setExprDt();
	
	var $bullPid = $("#bullPid").val();
	if (isInUtf8Length($('#cont').val(), 0, '${bodySize}') > 0) {
		alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.cont" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
		return;
	}
	
	if (validator.validate('setBoardForm')) {
		var $form = $('#setBoardForm');
		$form.attr('method','post');
		if($bullPid == null || $bullPid == ''){
			$form.attr('action','./transSetBullSave.do?menuId=${menuId}&ctId=${ctId}');
		}else{
			$form.attr('action','./transSetReplySave.do?menuId=${menuId}&ctId=${ctId}&bullPid=${bullPid}');
		}
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','dataframe');
		editor('cont').prepare();
		saveFileToForm('${filesId}', $form[0], null);
		//$form[0].submit();
	}
	
}

$(document).ready(function() {
	/* if(unloadEvent.editorType != 'namo'){
		var bodyHtml = $("#lobHandlerArea").html();
		if(bodyHtml!=''){
			$('#contEdit').on('load',function () {
				editor('cont').setInitHtml(bodyHtml);
				editor('cont').prepare();
			});
		}
	} */
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

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="subj" value="Java 8 개요" elseValue="" />
<u:set test="${fnc == 'mod'}" var="cont" value="Java 8에서 새로운 점들을 정리해 보았습니다. 도움되시길 바랍니다. 감사합니다." elseValue="" />

<u:title title="${menuTitle }" alt="게시판 조회" menuNameFirst="true"/>

<form id="setBoardForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="bullId" value="${param.bullId}" />
<u:input type="hidden" id="bullPid" value="${param.bullPid}" />
<u:input type="hidden" id="bullStatCd" value="B" />
<u:input type="hidden" id="bullRezvDt" value="${ctBullMastBVo.bullRezvDt}" />
<u:input type="hidden" id="tgtDeptYn" value="${ctBullMastBVo.tgtDeptYn}" />
<u:input type="hidden" id="tgtUserYn" value="${ctBullMastBVo.tgtUserYn}" />
<% // 폼 필드 %>
<u:listArea  noBottomBlank="true">
	<tr>
		<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" />
			<input id="bullCtFncId" name="bullCtFncId" type="hidden" value="${bullCtFncId}"/>
			<input id="bullCtFncUid" name="bullCtFncUid" type="hidden" value="${bullCtFncUid}"/>
			<input id="bullCtFncPid" name="bullCtFncPid" type="hidden" value="${bullCtFncPid}"/>
			<input id="bullCtFncLocStep" name="bullCtFncLocStep" type="hidden" value="${bullCtFncLocStep}"/>
			<input id="bullCtFncOrdr" name="bullCtFncOrdr" type="hidden" value="${bullCtFncOrdr}"/>
			
			<input id="bullPid" name="bullPid" type="hidden" value="${bullPid}"/>
		</td>
		<td><u:input id="subj" name="subj" value="${ctBullMastBVo.subj}" titleId="cols.subj" style="width: 98%;" mandatory="Y" maxByte="240"/></td>
		</tr>
		<c:if test="${bullPid == null || bullPid == '' }">
			<tr>
				<td class="head_lt"><u:msg titleId="cols.bullExprDt" alt="게시완료일" /></td>
				<td>
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td><u:calendar id="bullExprYmd" value="${bullExprYmd}" option="{checkHandler:onBullExprDayChange}"/></td>
							<td><select id="bullExprHm" name="bullExprHm" onchange="onBullRezvTimeChange('bullExprHm',$('#bullExprYmd').val(),'${bullExprHm }');">
								<c:forEach begin="0" end="23" step="1" var="hour" varStatus="status">
									<u:set test="${hour < 10}" var="hh" value="0${hour}" elseValue="${hour}" />
									<u:option value="${hh}:00" title="${hh}:00" checkValue="${bullExprHm}" />
									<u:option value="${hh}:30" title="${hh}:30" checkValue="${bullExprHm}" />
								</c:forEach>
								</select>
								<u:input type="hidden" id="bullExprDt" value="${ctBullMastBVo.bullExprDt}" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</c:if>
</u:listArea>
<%
	com.innobiz.orange.web.ct.vo.CtBullMastBVo ctBullMastBVo = (com.innobiz.orange.web.ct.vo.CtBullMastBVo)request.getAttribute("ctBullMastBVo");
	if(ctBullMastBVo != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(ctBullMastBVo.getCont());
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", ctBullMastBVo.getCont());
		}
	}
%><u:editor id="cont" width="100%" height="400px" module="ct" value="${_bodyHtml}" padding="2" />
<u:listArea>
	<tr>
	<td>
	<u:set var="ctSendYn" test="${ctSendYn == 'Y'}" value="Y" elseValue="N"/>
	<u:files id="${filesId}" fileVoList="${fileVoList}" module="ct" mode="set" ctSendYn="${ctSendYn }" exts="${exts }" extsTyp="${extsTyp }"/>
	</td>
	</tr>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.save.success" var="msg" alt="저장 되었습니다." />
<%-- 	<u:button titleId="cm.btn.save" alt="저장" href="javascript:void(alert('${msg}'));" auth="W" /> --%>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveBull();"/>
	<u:button titleId="cm.btn.cancel" alt="취소" href="./listBoard.do?menuId=${menuId}&ctId=${ctId}" />
</u:buttonArea>

</form>
<div id="lobHandlerArea" style="display:none;"><c:if test="${!empty _bodyHtml }">${_bodyHtml}</c:if><c:if test="${!empty lobHandler }"><u:clob lobHandler="${lobHandler }"/></c:if></div>
