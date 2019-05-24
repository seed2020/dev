<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="javax.servlet.jsp.tagext.JspFragment"
%><%@ attribute name="id"    	required="false"
%><%
	if(id==null) { id = ""; }
	else { id = " id=\""+id+"\""; }

%>	<dd class="etr_input"<%= id %>>
	<div class="etr_ipmany">
		<dl><%
JspFragment jspFragment = getJspBody();
if(jspFragment!=null){
	jspFragment.invoke(out);
}
%></dl>
	</div>
	</dd>