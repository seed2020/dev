<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Map,
			com.innobiz.orange.web.cm.utils.EscapeUtil,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.StringUtil,
			com.innobiz.orange.web.pt.utils.SysSetupUtil"
%><%@ attribute name="name"   required="true"
%><%@ attribute name="id"   required="false"
%><%@ attribute name="value" required="true"
%><%@ attribute name="className"  required="false"
%><%@ attribute name="labelClassName"  required="false"
%><%@ attribute name="labelStyle"  required="false"
%><%@ attribute name="title"  required="false"
%><%@ attribute name="titleId"  required="false"
%><%@ attribute name="termId" required="false"
%><%@ attribute name="checked"  required="false" type="java.lang.Boolean"
%><%@ attribute name="checkValue"  required="false"
%><%@ attribute name="onclick"   required="false"
%><%@ attribute name="disabled"   required="false"
%><%@ attribute name="desc"  required="false"
%><%@ attribute name="inputClass"  required="false"
%><%@ attribute name="inputStyle"  required="false"
%><%@ attribute name="textStyle"  required="false"
%><%@ attribute name="extraData"  required="false"
%><%@ attribute name="noLabel"  required="false" type="java.lang.Boolean"
%><%@ attribute name="alt"  required="false"
%><%
/*
HTML	: <input type='checkbox'>

id		: name + value
checked	: 'checked' 또는 value와 값이 같을때 선택됨
desc	: radio의 mouseover 할때 나타나는 설명을 변경하려고 할때

*/

if(id==null) id = (name==null) ? "" : name + value;

MessageProperties properties = MessageProperties.getInstance();
if(termId!=null){
	int p = termId.lastIndexOf('.');
	if(p>0){
		String setupClsId = termId.substring(0, p);
		String setupId = termId.substring(p+1);
		Map<String, String> termMap = SysSetupUtil.getTermMap(setupClsId, request);
		title = termMap==null ? null : termMap.get(setupId);
		if(title==null){
			title = properties.getMessage(termId, request);
		}
	}
} else if(titleId!=null){
	title = properties.getMessage(titleId, request);
}

String checkedAttr = "";
if(value.equals(checkValue) || "checked".equals(checkValue) || Boolean.TRUE.equals(checked)){
	checkedAttr = " checked=\"checked\"";
}

String actName = properties.getMessage("cm.radio.actname", request);
if(desc==null){
	if(title==null) desc = actName;
	else desc = title + " " + actName;
}
if(value==null) value="Y";
className = (className==null) ? "" : " class=\""+className+"\"";
labelClassName = (labelClassName==null) ? " class=\"bodyip_lt\"": " class=\""+labelClassName+"\"";
String dfLabelStyle = "vertical-align:bottom;";
labelStyle = (labelStyle==null) ? " style=\""+dfLabelStyle+"\"" : " style=\""+dfLabelStyle+labelStyle+"\"";
disabled = (disabled==null || !disabled.equals("Y")) ? "" : " disabled=\"disabled\"";
onclick = (onclick!=null) ? " onclick=\""+onclick+"\"" : "";

extraData = (extraData==null) ? "" : " data-extra=\""+extraData+"\"";

%><%if(title!=null && !title.isEmpty() && !Boolean.TRUE.equals(noLabel)){
	inputClass = inputClass==null ? "" : " class=\""+inputClass+"\"";
	inputStyle = inputStyle==null ? " style=\"display:inline;\"" : " style=\"display:inline;"+inputStyle+"\"";
	%><li<%= inputClass%><%= inputStyle%>><%}

%><input type="checkbox" name="<%=name%>" id="<%=id%>" value="<%=value%>" title="<%=desc%>"<%=checkedAttr%><%=onclick%><%=disabled%><%=className%><%=extraData%>/><%
if(title!=null && !title.isEmpty() && !Boolean.TRUE.equals(noLabel)){
	textStyle = textStyle==null ? "" : " style=\""+textStyle+"\"";
%><label for="<%=id%>" id="<%=id%>Label"<%=labelClassName%><%=labelStyle%>><%=title%></label></li>
<%
}%>