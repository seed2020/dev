<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.cm.utils.MessageProperties"
%><%@ attribute name="id" required="false"
%><%@ attribute name="display" required="false" type="java.lang.Boolean"
%><%@ attribute name="icon" required="false"
%><%
/*
	최우수(B)/우수(E)/장려(G) 표시
	
	예시)
	<u:icoBest />						-> 최우수(B) 아이콘 표시
	<u:icoBest icon="best" />			-> 최우수(B) 아이콘 표시
	<u:icoBest icon="excellent" />	-> 우수(E) 아이콘 표시
	<u:icoBest icon="good" />			-> 장려(G) 아이콘 표시
	<u:icoBest icon="" />				-> 아이콘 표시하지 않음
*/
if (icon == null) icon = "best";
if ("".equals(icon)) return;

String altTxt = MessageProperties.getInstance().getMessage("ct.ico." + icon, request);

if(id!=null && !id.isEmpty()) id = " id=\""+id+"\"";
else id = "";

String style = "";
if(Boolean.FALSE.equals(display)){
	style = " style=\"display:none\"";
}

%><img src="${_cxPth}/images/${_skin}/ico_<%=icon%>.png"<%=id %> alt="<%=altTxt%>" title="<%=altTxt%>" width="15" height="11"<%=style %>/>