<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="javax.servlet.jsp.tagext.JspFragment"
%><%@ attribute name="id"    required="false"
%><%@ attribute name="colgroup"    required="false"
%><%@ attribute name="style" required="false"
%><%@ attribute name="alt" required="false"
%><%@ attribute name="tbodyClass" required="false"
%><%@ attribute name="tableStyle" required="false"
%><%@ attribute name="extData" required="false"
%><%@ attribute name="noBottomBlank" required="false" type="java.lang.Boolean"
%><%

if(id==null) { id = ""; }
else { id = " id=\""+id+"\""; }

if(style==null) style = "";
else style = " style=\""+style+"\"";

String[] colgroups = null;
if(colgroup!=null){
	colgroups = colgroup.split(",");
}

if(tableStyle!=null && !tableStyle.isEmpty()){
	tableStyle = " style=\""+tableStyle+"\"";
} else {
	tableStyle = "";
}

if(tbodyClass!=null && !tbodyClass.isEmpty()){
	tbodyClass = "class=\""+tbodyClass+"\"";
} else {
	tbodyClass = "";
}

if(extData!=null && !extData.isEmpty()){
	int p;
	StringBuilder builder = new StringBuilder();
	for(String singleData : extData.split("\\|")){
		p = singleData.indexOf(':');
		if(p>0){
			builder.append(" data-").append(singleData.substring(0, p)).append("=\"").append(singleData.substring(p+1)).append('\"');
		}
	}
	extData = builder.toString();
} else {
	extData = "";
}

%>	<div class="listarea"<%= id%><%= extData%><%= style%>>
	<table class="listtable" border="0" cellpadding="0" cellspacing="1"<%= tableStyle%>><%
	
	// colgroups 테그 넣기 - 컬럼 width 관련
	if(colgroups!=null){
%>
	<colgroup><%
		for(int i=0;i<colgroups.length;i++){
		%>
		<col width="<%= colgroups[i]%>"><%
		}
%>
	</colgroup><%
	}
%>
	<tbody style="border:0"<%=tbodyClass %>>
<%
JspFragment jspFragment = getJspBody();
if(jspFragment!=null){
	jspFragment.invoke(out);
}
%>
	</tbody>
	</table>
	</div><%
	
	if(!Boolean.TRUE.equals(noBottomBlank)){
		%>
	<div class="blank"></div><%
	}
	%>