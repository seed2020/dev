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
%><%@ attribute name="dataList"  required="false"
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

if(type==null) type="calendartime";
	
if(name==null) name = id;
value = "today".equals(value) ? "calendartime".equals(type) || "time".equals(type) ? StringUtil.getCurrDateTime() : StringUtil.getCurrYmd() : value;

String dateVa=value==null || value.length()<10 ? "" : value.charAt(4)!='-' && value.charAt(7)!='-' ? "" : value.substring(0, 10); 
String timeVa=value==null || value.length()<16 ? value!=null && value.length()==5 ? value : "" : value.charAt(13)!=':'  ? "" : value.substring(11, 16);

String dateIdSfx="calendartime".equals(type) ? "Dt" : "";
String timeIdSfx="calendartime".equals(type) ? "Tm" : "";

if("calendartime".equals(type)){
	option=option==null ? "{onchange:chnDateTime}" : option.substring(0, option.length()-1)+", onchange:chnDateTime}";
}

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
String onkeyup="Y".equals(mnalYn) ? " onkeyup=\"chkMnalCalendar(this, "+handler+option+");\" onblur=\"validMnalDate(this);\"" : "";

if(timeStrt==null)
	timeStrt="0";

if(timeHandler!=null && "time".equals(type))
	timeHandler=" data-onchange=\""+timeHandler+"('"+id+"');\"";

StringBuffer buffer = new StringBuffer(128);	

if(dataList!=null && !dataList.isEmpty()) buffer.append(" "+dataList);

if(placeholder!=null && !placeholder.isEmpty()) buffer.append(" placeholder=\""+placeholder+"\"");

%>
<div id="<%= id%>Area" class="datetime">
	<%if("calendartime".equals(type) || "calendar".equals(type)){ %>
	
	<div class="input_date"><div id="<%= id%><%=dateIdSfx%>CalArea" style="position:absolute; display:none;"></div>
	<span class="input"><input id="<%= id%><%=dateIdSfx%>" name="<%= name%><%=dateIdSfx%>" value="<%=dateVa%>" type="text" style="width:80px; ime-mode:disabled;" class="input_center"<%= readonly%> onkeydown="onlyDelWorks(event, this, '<%=mnalYn %>');" title="<%=title%>"<%= onkeyup%> maxlength="10"<%=buffer.toString()%>/></span>
	<span class="calbtn"><a href="javascript:<%=option.indexOf("iframeId")>-1 ? "parent." : "" %>calendar.open('<%= id%><%=dateIdSfx%>'<%= option%>,<%=handler==null ? null : handler%>);" id="<%= id%><%=dateIdSfx%>CalBtn" 
	class="ico_calendar" title="<u:msg	titleId='cm.btn.calendar' alt='달력 - 팝업' />"<%= hideBtn%>><span><u:msg titleId="cm.btn.calendar" alt="달력 - 팝업" /></span></a></span></div><%}
	if("calendartime".equals(type) || "time".equals(type)){ %>
	<c:if test="${empty timeUnit }"><c:set var="timeUnit" value="<%=timeUnit %>"/></c:if>
	<c:if test="${empty type }"><c:set var="type" value="<%=type %>"/></c:if>
	<div class="input_select_list" style="max-width:70px;"><input id="<%= id%><%=timeIdSfx%>" name="<%= name%><%=timeIdSfx%>" value="<%=timeVa%>" type="text" style="width:60px; ime-mode:disabled;" class="input_center"<%= readonly%> title="<%=title%>" maxlength="5"<%=timeHandler %><c:if test="${type eq 'calendartime' }"> data-onchange="chnDateTime({myId:'${id}Tm'});"</c:if><%="time".equals(type) ? buffer.toString() : ""%>>
	<ul class="select_list" style="display:none;" id="selectTimeArea"><c:forEach begin="0" end="23" var="time"><u:set var="si" test="${time+timeStrt>23 }" value="${(time+timeStrt)-24 }" elseValue="${time+timeStrt }"/><c:forEach begin="1" end="${60/timeUnit }" var="min"><li><span class="txt">${si<10 ? '0' : '' }${si}:${min==1 ? '00' : (min-1)*timeUnit }</span></li></c:forEach></c:forEach></ul></div>
	<%if("calendartime".equals(type)){ %><u:input type="hidden" id="<%= id%>" value="<%=value%>" /><%} %>
	<%} %>
</div>
<%
JspFragment jspFragment = getJspBody();
if(jspFragment!=null){
	jspFragment.invoke(out);
}
	if(!"Y".equals(readonly) && "Y".equals(mandatory)){
%>
<script type="text/javascript">
$(document).ready(function() {
	<%if("calendartime".equals(type) || "calendar".equals(type)){ %>
	validator.addHandler('<%= id%><%=dateIdSfx%>', function(id, va){
		if(isEmptyVa(va)){
			alertMsg('cm.calendar.check.mandatory',['<%=title%>']);
			validator.focusId = "<%= id%><%=dateIdSfx%>CalBtn";
			return false;
		}
	});<%}%><%if("calendartime".equals(type) || "time".equals(type)){ %>
	validator.addHandler('<%= id%><%=timeIdSfx%>', function(id, va){
		if(isEmptyVa(va)){
			alertMsg('cm.time.check.mandatory',['<%=title%>']);
			validator.focusId = "<%= id%><%=timeIdSfx%>";
			return false;
		}
	});<%}%><%
	if(request.getAttribute("CAL_SKIN")==null){%>
	calendar.skin = '${_skin}';
	<%
	request.setAttribute("CAL_SKIN", Boolean.TRUE);
	}%>
});
</script>
<%
	} else if(request.getAttribute("CAL_SKIN")==null){
		%>
<script type="text/javascript">
$(document).ready(function() {
	calendar.skin = '${_skin}';
});
</script><%
		request.setAttribute("CAL_SKIN", Boolean.TRUE);
	}
%><%if("calendartime".equals(type) || "time".equals(type)){ %>
<script type="text/javascript">
$(document).ready(function() {
	setTimeSelectEvt('<%= id%>');
});
</script><%}%>