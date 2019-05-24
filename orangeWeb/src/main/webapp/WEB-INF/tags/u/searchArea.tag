<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="javax.servlet.jsp.tagext.JspFragment"
%><%@ attribute name="id"    required="false"
%><%@ attribute name="style" required="false"
%><%@ attribute name="noBottomBlank" required="false" type="java.lang.Boolean"

%><%
if(id==null) id = "";
else id = " id=\""+id+"\"";

if(style==null) style = "";
else style = " style=\""+style+"\"";

%>	<div class="search notPrint"<%= id%><%= style%>>
	<b class="search1"></b>
	<b class="search2"></b>
	<b class="search3"></b>
	<b class="search4"></b>
	<div class="search5">
<%
JspFragment jspFragment = getJspBody();
if(jspFragment!=null){
	jspFragment.invoke(out);
}
%>
	</div>
	<b class="search4"></b>
	<b class="search3"></b>
	<b class="search2"></b>
	<b class="search1"></b>
	</div><%
	
	if(!Boolean.TRUE.equals(noBottomBlank)){
		%>
	<div class="blank"></div><%
	}
	%>