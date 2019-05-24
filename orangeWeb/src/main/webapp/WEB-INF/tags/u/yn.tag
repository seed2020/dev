<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.cm.utils.MessageProperties"
%><%@ attribute name="value"    required="false"
%><%@ attribute name="yesId"    required="false"
%><%@ attribute name="noId"    required="false"
%><%@ attribute name="alt"    required="false"
%><%

String msgId = "Y".equals(value) ? yesId : "N".equals(value) ? noId : null;
String msg = "";
if(msgId!=null){
	msg = MessageProperties.getInstance().getMessage(msgId, request);
}
%><%= msg%>