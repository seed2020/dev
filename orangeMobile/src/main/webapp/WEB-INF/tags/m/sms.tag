<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
%><%@ attribute name="value" required="false"
%><%@ attribute name="type" required="false"
%><%@ attribute name="text" required="false"
%><%@ attribute name="onclick" required="false"
%><%

if(text==null || text.isEmpty()) text = value;
if(onclick==null) onclick = "";
else if(!onclick.endsWith(";")) onclick += ";";

if("image".equals(type)){
	if(value==null || value.isEmpty()){
		// do nothing
	} else {
		%><dd class="sms" onclick="top.setTimeout(&quot;location.href='sms:<%= value%>'&quot;,1);"></dd><%
	}
} else {
	if(value==null || value.isEmpty()){
		%>&nbsp;<%
	} else {
		%><a href="javascript:;" onclick="top.setTimeout(&quot;location.href='sms:<%= value%>'&quot;,1);"><%= text%></a><%
	}
}
%>