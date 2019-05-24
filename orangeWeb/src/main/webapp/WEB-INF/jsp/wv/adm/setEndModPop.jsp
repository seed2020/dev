<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:msg titleId="ct.msg.noSelectCat" alt="카테고리를 선택해주시기 바랍니다." var="catSelect" />
<u:msg titleId="ct.msg.notInsertCatNm" alt="이름 수정란에 카테고리명을 입력해주시기 바랍니다." var="modCatNm" />
<script type="text/javascript">
<!--
//마감여부 저장
function endModSave(survId){
	if(confirmMsg("cm.cfrm.save")){
		var $form = $("#endModForm");
		$form.attr("method", "POST");
		$form.attr("action", "./transEndModSave.do?menuId=${menuId}&survId="+survId);
		$form.submit();
	}
}



//시작일 수정 차단
function startDtNotMod(){
	alert('시작일은 변경할 수 없습니다.');
	return false;
}

//오늘 날짜와 비교
function compareDate(setAnnvChoiDt){
	return todayCompare(setAnnvChoiDt);
}

function todayCompare(setAnnvChoiDt){
	var now = new Date();
	var todayAtMidn = new Date(now.getFullYear(), now.getMonth(), now.getDate());

	var startDate = new Date(setAnnvChoiDt);

	if (todayAtMidn.getTime() > startDate.getTime()) {
		alert('당일보다 이전일을 선택하실 수 없습니다.');
		
		return false;
	}
	else {
		return true;
	}
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />

<div style="width:300px">
<form id="endModForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.prd" alt="기간" /></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="body_lt"><u:msg titleId="cols.strtDt" alt="시작일시" /></td>
		<td class="width5"></td>
		<td ><u:calendar id="strtDt" option="{end:'finDt'}"  handler="function(date,option){return startDtNotMod();}" titleId="cols.choiDt" alt="선택일시" readonly="true" value="${wvsVo.survStartDt}" /></td>
		</tr>
		<tr>
		<td class="body_lt"><u:msg titleId="cols.finDt" alt="마감일시" /></td>
		<td class="width5"></td>
		<td ><u:calendar id="finDt" option="{start:'strtDt'}"  handler="function(date,option){return compareDate(date);}"  value="${wvsVo.survEndDt}"/></td>
		</tr>
		</table></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="wv.cols.endYn" alt="마감여부" /></td>
		<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><u:radio id="endYn" name="endYn" value="Y" titleId="cm.option.yes" /></td>
		<td><u:radio id="endYn" name="endYn" value="N" titleId="cm.option.no" checked="true"/></td>
		</tr>
		</table></td>
	
	</tr>
</u:listArea>
		
<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:endModSave('${wvsVo.survId}');" alt="저장" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
