<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="javax.servlet.jsp.tagext.JspFragment"
%><%@ attribute name="id"			required="false"
%><%@ attribute name="className"	required="false"
%><%@ attribute name="style"		required="false"
%><%@ attribute name="outerStyle" required="false"
%><%@ attribute name="innerStyle"	required="false"
%><%@ attribute name="noBottomBlank" required="false" type="java.lang.Boolean"
%><%
/*

innerStyle : NO_INNER_IDV - inner div 를 생성 안함

*/

if(id==null) id = "";
else id = " id=\""+id+"\"";

if(className==null) className = "gbox";

if(style==null) style = "";
else style = " style=\""+style+"\"";

if(outerStyle==null) outerStyle = "";
else outerStyle = " style=\""+outerStyle+"\"";

if(innerStyle==null) innerStyle = "";
else innerStyle = " style=\""+innerStyle+"\"";

%>
	<div<%=id %> class="<%=className%>"<%=style%>>
	<b class="<%=className%>1g"></b>
	<b class="<%=className%>2"></b>
	<b class="<%=className%>3"></b>
	<b class="<%=className%>4"></b>
	<div class="<%=className%>5"<%=outerStyle%>><%
	if(!"NO_INNER_IDV".equals(innerStyle)){ %>
	<div<%=innerStyle%>><%
	}
	
	JspFragment jspFragment = getJspBody();
	if(jspFragment!=null){
		jspFragment.invoke(out);
	}


	if(!"NO_INNER_IDV".equals(innerStyle)){ %>
	</div><%
	}
%>
	</div>
	<b class="<%=className%>4"></b>
	<b class="<%=className%>3"></b>
	<b class="<%=className%>2"></b>
	<b class="<%=className%>7"></b>
	</div><%
	
	if(!Boolean.TRUE.equals(noBottomBlank)){
		%>
	<div class="blank"></div><%
	}
	%>