<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
// jstl 에서 스페이스 + 엔터 치환
pageContext.setAttribute("enter","\r\n"); %>
<script type="text/javascript">
<!--
<% // 일시 replace %>
function getDayString(date , regExp){
	return date.replace(regExp,'');
};<% // 일자 체크 - 오늘 날짜 이후로만 %>
function onChgTodayChk(date){
	var regExp = /[^0-9]/g;
	var today = getDayString(getToday(),regExp);
	var setday = getDayString(date,regExp);
	if(today > setday){
		alertMsg('cm.calendar.check.dateAI');
		return true;
	}
	return false;
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<div style="min-height:500px;padding:5px;">
<c:set var="idPrefix" value="preview" scope="request"/>
<!-- 탭 -->
<jsp:include page="/WEB-INF/jsp/wf/works/inclTab.jsp" flush="false">
<jsp:param value="preview" name="tabPage"/>
</jsp:include>

<jsp:include page="/WEB-INF/jsp/wf/works/inclRegForm.jsp" flush="false">
<jsp:param value="preview" name="page"/>
</jsp:include>
</div>
