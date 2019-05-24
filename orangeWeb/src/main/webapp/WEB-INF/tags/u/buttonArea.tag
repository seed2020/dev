<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="javax.servlet.jsp.tagext.JspFragment"
%><%@ attribute name="id" required="false"
%><%@ attribute name="topBlank" required="false" type="java.lang.Boolean"
%><%@ attribute name="style" required="false"
%><%@ attribute name="noBottomBlank" required="false" type="java.lang.Boolean"
%><%

style = style==null ? "" : " style=\""+style+"\"";
id = id==null ? "" : " id=\""+id+"\"";

JspFragment jspFragment = getJspBody();
if(jspFragment!=null){

	if(Boolean.TRUE.equals(topBlank)){
%>	<div class="blank"></div>
<%	}
	
%>	<div<%=id%> class="button_basic notPrint"<%=style%>>
	<ul>
<%
	jspFragment.invoke(out);
%>
	</ul>
	</div><%
	
	if(!Boolean.TRUE.equals(noBottomBlank)){
		%>
	<div class="blank"></div><%
	}
}
%>
