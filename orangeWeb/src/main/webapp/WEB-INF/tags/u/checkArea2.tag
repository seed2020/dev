<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="javax.servlet.jsp.tagext.JspFragment"
%><%@ attribute name="id"    	required="false"
%><%@ attribute name="style"	required="false"
%><%

if(id==null) { id = ""; }
else { id = " id=\""+id+"\""; }

String dfStyle = "list-style-type:none;margin:0;padding:0;";
if(style==null) style = " style=\""+dfStyle+"\"";
else style = " style=\""+dfStyle+style+"\"";

%><ul <%= style %><%= id %>>
<%
JspFragment jspFragment = getJspBody();
if(jspFragment!=null){
	jspFragment.invoke(out);
}
%>
</ul>