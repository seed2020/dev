<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.cm.utils.MessageProperties"
%><%@ attribute name="id" required="false"
%><%@ attribute name="display" required="false" type="java.lang.Boolean"
%><%
/*
	별표 필수입력표시
*/
String altTxt = MessageProperties.getInstance().getMessage("cm.ico.mandatory", request);

if(id!=null && !id.isEmpty()) id = " id=\""+id+"\"";
else id = "";

String style = "";
if(Boolean.FALSE.equals(display)){
	style = " style=\"display:none\"";
}

%><img src="${_cxPth}/images/${_skin}/ico_asterisk.png"<%=id %> alt="<%=altTxt%>" width="10" height="9"<%=style %>/>