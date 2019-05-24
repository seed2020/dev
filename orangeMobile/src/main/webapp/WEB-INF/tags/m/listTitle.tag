<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ attribute name="title" required="true"
%><%@ attribute name="ugnt" required="false" type="java.lang.Boolean"
%><%@ attribute name="secu" required="false" type="java.lang.Boolean"
%><%@ attribute name="agnt" required="false" type="java.lang.Boolean"
%><%@ attribute name="notRead" required="false" type="java.lang.Boolean"
%><%
	/**
	ugnt : 긴급
	secu : 보안
	agnt : 대결
	notRead : 읽지않음
	
	*/

	String[] strongs = Boolean.TRUE.equals(notRead) ? new String[]{"<strong>", "</strong>"} : new String[]{"",""};

%><%= strongs[0]%><c:if
	test="${ugnt}"><span class="ctxt1">[<u:msg titleId="bb.option.ugnt" alt="긴급" />]</span></c:if><c:if
	test="${secu}"><span class="ctxt2">[<u:msg titleId="bb.option.secu" alt="보안" />]</span></c:if><c:if
	test="${agnt}"><span class="ctxt3">[<u:term termId="ap.term.agnt" alt="대결" />]</span></c:if><u:out value="${title}" /><%= strongs[1]%>