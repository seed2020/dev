<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Locale,
			javax.servlet.jsp.tagext.JspFragment,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.SessionUtil,
			com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
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
%><%@ attribute name="mnalYn" required="false"
%><%@ attribute name="align" required="false"
%><%@ attribute name="type" required="false"
%><%
/*
	type : view 면 텍스트 출력

	option : 체크 옵션
	- 시작일(startDt), 종료일(endDt) 가 있을때
	- 시작일은 {end:'endDt'}, 종료일은 {start:'startDt'} 를 넣어줌
*/

if(name==null) name = id;
value = "today".equals(value) ? StringUtil.getCurrYmd() : 
	value==null || value.length()<10 ? "" : 
	value.charAt(4)!='-' && value.charAt(7)!='-' ? "" : value.substring(0, 10);

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

boolean isReadOnly = "Y".equals(readonly);
String hideBtn = isReadOnly ? " style=\"display:none;\"" : "";
readonly = isReadOnly ? " readonly=\"readonly\"" : "";

if(mnalYn==null) mnalYn="Y";
String onkeyup="Y".equals(mnalYn) ? " onkeyup=\"chkMnalCalendar(this, "+handler+option+");\" onblur=\"validMnalDate(this);\"" : "";

String tableStyle="";
if(align!=null && !"left".equals(align)){
	if("center".equals(align))
		tableStyle="style=\"margin:auto;\"";
	else if("center".equals(align))	
		tableStyle="style=\"float:right;\"";
}
if("view".equals(type)){
	%><u:out value="<%= value%>" type="shortdate" /><%
} else {
%>
<table id="<%= id%>Area" border="0" cellpadding="0" cellspacing="0" <%=tableStyle %>>
<tr>
<td><input id="<%= id%>" name="<%= name%>" value="<%= value%>" type="text" style="width:80px; ime-mode:disabled;" class="input_center<%= (isReadOnly ? " input_disabled" : "")%>"<%= readonly%> onkeydown="onlyDelWorks(event, this, '<%=mnalYn %>');" title="<%=title%>"<%= onkeyup%> maxlength="10"/></td>
<td>
	<%
	if(handler != null){
	%>
	<a href="javascript:<%=option.indexOf("iframeId")>-1 ? "parent." : "" %>calendar.open('<%= id%>'<%= option%>,<%=handler%>);" id="<%= id%>CalBtn" class="ico_calendar" title="<u:msg	titleId='cm.btn.calendar' alt='달력 - 팝업' />"<%= hideBtn%>>
	<%
	}else{
	%>
	<a href="javascript:<%=option.indexOf("iframeId")>-1 ? "parent." : "" %>calendar.open('<%= id%>'<%= option%>);" id="<%= id%>CalBtn" class="ico_calendar" title="<u:msg	titleId='cm.btn.calendar' alt='달력 - 팝업' />"<%= hideBtn%>>
	<%
	}
	%>

<span>
	<u:msg titleId="cm.btn.calendar" alt="달력 - 팝업" />
</span>
</a>

</td><%

JspFragment jspFragment = getJspBody();
if(jspFragment!=null){
	jspFragment.invoke(out);
}
%>
</tr>
</table><div id="<%= id%>CalArea" style="position:absolute; display:none;"></div><%


	if(!"Y".equals(readonly) && "Y".equals(mandatory)){
%>
<script type="text/javascript">
$(document).ready(function() {
	validator.addHandler('<%= id%>', function(id, va){
		if(isEmptyVa(va)){
			alertMsg('cm.calendar.check.mandatory',['<%=title%>']);
			validator.focusId = "<%= id%>CalBtn";
			return false;
		}
	});<%
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
}// else - if("view".equals(type)){
%>