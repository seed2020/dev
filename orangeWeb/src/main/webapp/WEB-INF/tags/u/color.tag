<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Locale,
			com.innobiz.orange.web.cm.utils.EscapeUtil,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.SessionUtil,
			com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ attribute name="id"			required="false" rtexprvalue="true"
%><%@ attribute name="name"			required="false" rtexprvalue="true"
%><%@ attribute name="titleId"		required="false" rtexprvalue="true"
%><%@ attribute name="readonly"		required="false" rtexprvalue="true"
%><%@ attribute name="value"		required="false" rtexprvalue="true"
%><%@ attribute name="handler" required="false" rtexprvalue="true"
%><%@ attribute name="input" required="false" rtexprvalue="true"
%><%

if(titleId==null) titleId = "cm.btn.colorPicker";
if(name==null) name = id;
if(input==null) input = "N";
%>
<table cellspacing="0" cellpadding="0" border="0"><tbody><tr>
<td><input type="text" id="<%= id%>" name="<%= name%>" maxlength="7" style="width:60px; ime-mode:disabled;<%=
	(value!=null && !value.isEmpty() ? " color:"+value+";" : "")%>"<%
	if(!input.equals("Y")){ 
		%> onkeydown="onlyDelWorks(event, this);" class="input_disabled"<%
	} %> value="<%= (value==null ? "" : value)%>"/></td>
<%
	if(!"Y".equals(readonly)){
		String clickFnc = "showColorPop('"+id+"','"+handler+"');";
%><td><u:buttonS titleId="<%= titleId%>" onclick="<%= clickFnc%>" /></td><%
	}%>
</tr></tbody></table><%
	if(!"Y".equals(readonly)){
%><div id="<%= id%>ColorPopArea" style="position:absolute; z-index:10; width:425px; display:none;"></div><%
	}%>