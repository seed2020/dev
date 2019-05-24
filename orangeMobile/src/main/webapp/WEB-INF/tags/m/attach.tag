<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ attribute name="fileName" required="true"
%><%@ attribute name="fileKb" required="true"
%><%@ attribute name="downFnc" required="true"
%><%@ attribute name="openFnc" required="false"
%><%@ attribute name="viewFnc" required="false"
%><%

	if(fileKb!=null && !fileKb.isEmpty()) fileName = fileName + " ("+StringUtil.toNumber(fileKb)+" KB)";
	request.setAttribute("fileName2", fileName);
	if(openFnc==null) openFnc = downFnc;
%>
<div class="attachin">
<div class="attach" <c:if test="${viewYn eq 'Y' }">onclick="<%= viewFnc%>"</c:if><c:if test="${viewYn ne 'Y' && (empty downYn || downYn eq 'Y')}">onclick="<%= openFnc%>"</c:if>>
	<div class="btn"></div>
	<div class="txt"><u:out value="${fileName2}" /></div>
</div>
<c:if test="${empty downYn || downYn eq 'Y'}"><div class="down" onclick="<%= downFnc%>"></div></c:if>
</div>