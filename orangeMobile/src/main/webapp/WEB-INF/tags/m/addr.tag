<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ attribute name="zipNo" required="false"
%><%@ attribute name="addr" required="false"
%><%

	if(addr==null) addr = "&nbsp;";
	if(zipNo!=null && !zipNo.isEmpty()) addr = "["+zipNo+"] "+addr;

%><u:out value="${addr}" />