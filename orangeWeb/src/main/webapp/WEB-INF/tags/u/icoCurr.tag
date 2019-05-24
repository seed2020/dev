<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.cm.utils.MessageProperties"
%><%@ attribute name="id" required="false"
%><%@ attribute name="display" required="false" type="java.lang.Boolean"
%><%
/*
	현재글(->) 표시
*/
String altTxt = MessageProperties.getInstance().getMessage("cm.ico.curr", request);

if(id!=null && !id.isEmpty()) id = " id=\""+id+"\"";
else id = "";

String style = "";
if(Boolean.FALSE.equals(display)){
	style = " style=\"display:none\"";
}

%><span<%=id %> title="<%=altTxt%>" class="color_txt"<%=style %>><strong>→</strong></span>