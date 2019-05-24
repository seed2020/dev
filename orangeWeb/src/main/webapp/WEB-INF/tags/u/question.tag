<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
%><%@ attribute name="id"    required="false"
%><%@ attribute name="byAdmin"    required="false" type="java.lang.Boolean"
%><%

if(id==null) id = "";
else id = " id=\""+id+"\"";

boolean shouldShow = "ko".equals(request.getAttribute("_lang"));
if(shouldShow && byAdmin!=null && Boolean.TRUE.equals(byAdmin)){
	String uri = (String)request.getAttribute("_uri");
	shouldShow = uri.indexOf("/adm/") >= 0;
}

if(shouldShow){
%><img<%= id%> src="${_cxPth}/images/${_skin}/ico_question.png" alt="부가설명" width="9" height="11"/><%
}
%>