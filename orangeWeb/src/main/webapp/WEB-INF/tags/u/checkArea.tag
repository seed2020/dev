<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="javax.servlet.jsp.tagext.JspFragment"
%><%@ attribute name="id"    	required="false"
%><%@ attribute name="style"	required="false"
%><%

if(id==null) { id = ""; }
else { id = " id=\""+id+"\""; }

if(style==null) style = "";
else style = " style=\""+style+"\"";

%><table<%= style %><%= id %> border="0" cellpadding="0" cellspacing="0">
<tr>
<%
JspFragment jspFragment = getJspBody();
if(jspFragment!=null){
	jspFragment.invoke(out);
}
%>
</tr>
</table>