<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Map,
			com.innobiz.orange.web.pt.utils.SysSetupUtil,
			com.innobiz.orange.web.cm.utils.EscapeUtil,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ attribute name="titleId"  required="false"
%><%@ attribute name="title"  required="false"
%><%@ attribute name="value" required="true"
%><%@ attribute name="selected"  required="false" type="java.lang.Boolean"
%><%@ attribute name="checkValue" required="false"
%><%@ attribute name="alt" required="false"
%><%@ attribute name="termId" required="false"
%><%
/*
HTML	: <option >

checked	: 'checked' 또는 'checked' 일때, value와 값이 같을때 선택됨
	
*/

if(titleId!=null){
	title = MessageProperties.getInstance().getMessage(titleId, request);
}

int p = termId==null ? 0 : termId.lastIndexOf('.');
String termVa = null;
if(p>0){
	if(termId.indexOf(".term")>0){
		String setupClsId = termId.substring(0, p);
		String setupId = termId.substring(p+1);
		Map<String, String> termMap = SysSetupUtil.getTermMap(setupClsId, request);
		termVa = termMap==null ? null : termMap.get(setupId);
	}
	
	if(termVa==null){
		termVa = MessageProperties.getInstance().getMessage(termId, request);
	}
	if(termVa!=null){
		title = termVa;
	}
}

if(title==null) title = value;

String selectedAttr = "";
if(value.equals(checkValue) || "selected".equals(checkValue) || Boolean.TRUE.equals(selected)){
	selectedAttr = " selected=\"selected\"";
}
//selected = (selected!=null && (selected.equals("selected") || selected.equals(value))) ? " selected=\"selected\"" : "";


%><option value="<%=value%>"<%= selectedAttr%> ><%=title%></option>