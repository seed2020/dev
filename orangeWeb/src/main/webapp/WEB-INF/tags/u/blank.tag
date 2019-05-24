<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
%><%@ attribute name="id"    required="false"
%><%
if(id==null) id = "";
else id = " id=\""+id+"\"";

%>	<div class="blank"<%= id%>></div>