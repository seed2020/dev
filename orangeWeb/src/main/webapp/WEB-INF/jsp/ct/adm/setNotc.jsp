<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:out value="${ctAdmNotcBVo.bullExprDt}" type="date" var="bullExprYmd" />
<u:out value="${ctAdmNotcBVo.bullExprDt}" type="hm" var="bullExprHm" />
<script type="text/javascript">
<!--

//오늘 시간 리턴
function getToTime(){
	var d = new Date();
	return (d.getHours() < 10 ? "0"+d.getHours() : d.getHours() ) +":"+ (d.getMinutes() < 10 ? "0"+d.getMinutes() : d.getMinutes() );
}

<% // 완료일시 세팅 %>
function setExprDt() {
	if ($('#bullExprYmd').val() != '') {
		$('#bullExprDt').val($('#bullExprYmd').val() + ' ' + $('#bullExprHm').val() + ':00');
	}
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

function saveNotc(){
	setExprDt();
	
	if (isInUtf8Length($('#cont').val(), 0, '${bodySize}') > 0) {
		alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.cont" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
		return;
	}
	
	if (validator.validate('viewNotcForm')) {
		var $form = $('#viewNotcForm');
		$form.attr('method','post');
		$form.attr('action','./transSetNotcSave.do?menuId=${menuId}');
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','dataframe');
		editor('cont').prepare();
		saveFileToForm('${filesId}', $form[0], null);
		//$form[0].submit();
	}
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="subj" value="커뮤니티 마스터 상반기 모임 공지" elseValue="" />
<u:set test="${fnc == 'mod'}" var="regr" value="조용필" elseValue="" />
<u:set test="${fnc == 'mod'}" var="regDt" value="2014-01-21" elseValue="" />
<u:set test="${fnc == 'mod'}" var="cont" value="부담없이 놀러 오세요~ 많이 많이 놀러 오세요~ 감사합니다." elseValue="" />

<u:title titleId="ct.jsp.setNotc.${fnc}.title" alt="커뮤니티 공지사항 등록/커뮤니티 공지사항 수정" menuNameFirst="true"/>

<form id="viewNotcForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="bullId" value="${param.bullId}" />
<u:input type="hidden" id="bullPid" value="${param.bullPid}" />
<u:input type="hidden" id="bullStatCd" value="B" />
<u:input type="hidden" id="bullRezvDt" value="${ctAdmNotcBVo.bullRezvDt}" />
<u:input type="hidden" id="tgtDeptYn" value="${ctAdmNotcBVo.tgtDeptYn}" />
<u:input type="hidden" id="tgtUserYn" value="${ctAdmNotcBVo.tgtUserYn}" />

<% // 폼 필드 %>
<u:listArea colgroup="18%,82%"  noBottomBlank="true">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td><u:input id="subj" name="subj" value="${ctAdmNotcBVo.subj}" titleId="cols.subj" style="width: 98%;" mandatory="Y"  maxByte="240"/></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.bullExprDt" alt="게시완료일" />
		</td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><u:calendar id="bullExprYmd" value="${bullExprYmd}" option="{checkHandler:onBullExprDayChange}"/></td>
					<td>
					<select id="bullExprHm" name="bullExprHm" onchange="onBullRezvTimeChange('bullExprHm',$('#bullExprYmd').val(),'${bullExprHm }');">
						<c:forEach begin="0" end="23" step="1" var="hour" varStatus="status">
							<u:set test="${hour < 10}" var="hh" value="0${hour}" elseValue="${hour}" />
							<u:option value="${hh}:00" title="${hh}:00" checkValue="${bullExprHm}" />
							<u:option value="${hh}:30" title="${hh}:30" checkValue="${bullExprHm}" />
						</c:forEach>
					</select>
					<u:input type="hidden" id="bullExprDt" value="${ctAdmNotcBVo.bullExprDt}" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</u:listArea>	

<div id="editor1Area" class="listarea" style="width:100%; height:302px; padding-top:2px"></div>
<u:editor id="cont" width="100%" height="300" module="ct" areaId="editor1Area" value="${ctAdmNotcBVo.cont}" />

<% // 첨부파일 %>
<u:listArea>
	<tr>
	<td><u:files id="${filesId}" fileVoList="${fileVoList}" module="ct" mode="set" exts="${exts }" extsTyp="${extsTyp }"/></td>
	</tr>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.save.success" var="msg" alt="저장 되었습니다." />
<%-- 	<u:button titleId="cm.btn.save" alt="저장" href="javascript:void(alert('${msg}'));" auth="W" /> --%>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveNotc();" auth="W" />
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>

</form>

