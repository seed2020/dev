<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Locale,
			javax.servlet.jsp.tagext.JspFragment,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.SessionUtil,
			com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"
%><%@ attribute name="id" required="true"
%><%@ attribute name="name" required="false"
%><%@ attribute name="value" required="false"
%><%@ attribute name="titleId" required="false"
%><%@ attribute name="alt" required="false"
%><%@ attribute name="title" required="false"
%><%@ attribute name="mandatory" required="false"
%><%@ attribute name="readonly"  required="false"
%><%@ attribute name="option" required="false"
%><%@ attribute name="handler" required="false"
%><%@ attribute name="timeHandler" required="false"
%><%@ attribute name="mnalYn" required="false"
%><%@ attribute name="type" required="false"
%><%@ attribute name="timeStrt" required="false"
%><%@ attribute name="timeUnit" required="false"
%><%@ attribute name="placeholder" required="false"
%><%
/*
	option : 체크 옵션
	- 시작일(startDt), 종료일(endDt) 가 있을때
	- 시작일은 {end:'endDt'}, 종료일은 {start:'startDt'} 를 넣어줌
	
	type : 달력 구분
	- calendartime : 달력 + 시간
	- calendar : 달력
	- time : 시간
	
	timeStrt : 시작시간
	- 8 : 오전 8시부터
			
	timeUnit : 시간단위
	- 30 : 30분단위[00분, 30분]
*/

if(type==null) type="time";
	
if(name==null) name = id;

if(timeUnit==null || timeUnit.isEmpty() || 60%Integer.parseInt(timeUnit)>0)
	timeUnit="30";

int maxMin=60/Integer.parseInt(timeUnit);

Locale locale = SessionUtil.getLocale(request);
MessageProperties properties = MessageProperties.getInstance();
if(title==null && titleId==null){
	titleId = "cols."+id;
}
if(titleId!=null){
	title = properties.getMessage(titleId, locale);
}
if(title==null) title = "";
option = option==null ? "" : ", "+option;

String hideBtn = "Y".equals(readonly) ? " style=\"display:none;\"" : "";
readonly = "Y".equals(readonly) ? " readonly=\"readonly\"" : "";

if(mnalYn==null) mnalYn="Y";

if(timeStrt==null)
	timeStrt="0";

if(timeHandler!=null && "time".equals(type))
	timeHandler=" data-onchange=\""+timeHandler+"('"+id+"');\"";
else timeHandler="";

if(value==null || value.isEmpty()) value="";

if(placeholder==null) placeholder="";
%>
<div id="<%= id%>Area" class="datetime">
	<%
	if("time".equals(type)){ %>
	<c:if test="${empty timeUnit }"><c:set var="timeUnit" value="<%=timeUnit %>"/></c:if>
	<c:if test="${empty type }"><c:set var="type" value="<%=type %>"/></c:if>
	<div class="input_select_list" style="max-width:100%;"><input id="<%= id%>" name="<%= name%>" value="<%=value%>" type="text" class="etr_iplt" style="ime-mode:disabled;" class="input_center"<%= readonly%> title="<%=title%>" maxlength="5"<%=timeHandler %>>
	<ul class="select_list" style="display:none;" id="selectTimeArea"><c:forEach begin="0" end="23" var="time"><u:set var="si" test="${time+timeStrt>23 }" value="${(time+timeStrt)-24 }" elseValue="${time+timeStrt }"/><c:forEach begin="1" end="${60/timeUnit }" var="min"><li><span class="txt">${si<10 ? '0' : '' }${si}:${min==1 ? '00' : (min-1)*timeUnit }</span></li></c:forEach></c:forEach></ul></div>
	<%if("calendartime".equals(type)){ %><m:input type="hidden" id="<%= id%>" value="<%=value%>" /><%} %>
	<%} %>
</div>

<script type="text/javascript">
$(document).ready(function() {
	setTimeSelectEvt('<%= id%>');
});
</script>