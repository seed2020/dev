<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="javax.servlet.jsp.tagext.JspFragment"
%><%@ attribute name="id"    required="false"
%><%@ attribute name="frameSrc" required="false"
%><%@ attribute name="frameId" required="false"
%><%@ attribute name="style" required="false"
%><%@ attribute name="outerStyle" required="false"
%><%@ attribute name="innerStyle" required="false"
%><%@ attribute name="frameStyle" required="false"
%><%@ attribute name="blueTop" required="false" type="java.lang.Boolean" 
%><%@ attribute name="noBottomBlank" required="false" type="java.lang.Boolean"
%><%
/*

innerStyle : NO_INNER_IDV - inner div 를 생성 안함

*/

if(id==null) id = "";
else id = " id=\""+id+"\"";

if(frameSrc!=null && frameId==null) frameId = "frm";

if(outerStyle==null){
	outerStyle = "height:600px; overflow-x:hidden; overflow-y:auto;";
}
if(innerStyle==null){
	innerStyle = "height:580px; margin:10px;";
}
style = style==null ? "" : (" style=\""+style+"\"");
if(frameSrc!=null && frameStyle==null){
	frameStyle = "width:100%; height:580px;";
}
if(frameSrc!=null && frameStyle==null){
	frameStyle = "width:100%; height:580px;";
}
String box1="wbox1";
if(!Boolean.TRUE.equals(blueTop)){
	box1 += "g";
}
%>	<div class="wbox"<%= id%><%= style%>>
	<b class="<%= box1 %>"></b>
	<b class="wbox2"></b>
	<b class="wbox3"></b>
	<b class="wbox4"></b>
	<div class="wbox5" style="<%=outerStyle%>"><%
	if(!"NO_INNER_IDV".equals(innerStyle)){ %>
	<div style="<%=innerStyle%>"><%
	}
	
	if(frameSrc!=null){
%>	<iframe id="<%=frameId%>" name="<%=frameId%>" src="<%=frameSrc%>" style="<%=frameStyle%>" frameborder="0" marginheight="0" marginwidth="0"></iframe><%		
	} else {
		JspFragment jspFragment = getJspBody();
		if(jspFragment!=null){
			jspFragment.invoke(out);
		}
	}

	if(!"NO_INNER_IDV".equals(innerStyle)){ %>
	</div><%
	}
%>
	</div>
	<b class="wbox4"></b>
	<b class="wbox3"></b>
	<b class="wbox2"></b>
	<b class="wbox7"></b>
	</div><%
	
	if(!Boolean.TRUE.equals(noBottomBlank)){
		%>
	<div class="blank"></div><%
	}
	%>