<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
%><%@ attribute name="value" required="false"
%><%@ attribute name="type" required="false"
%><%

if(value==null || value.isEmpty()){
	%>&nbsp;<%
} else if("image".equals(type)){
	%><dd class="phone"><a href="javascript:;" onclick="$m.menu.openSso(event, {act:'writeMail',to:'<%= value%>'});"><%= value%></a></dd><%
} else {
	%><a href="javascript:;" onclick="$m.menu.openSso(event, {act:'writeMail',to:'<%= value%>'});"><%= value%></a><%
}
%>