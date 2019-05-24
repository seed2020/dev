<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<div style="width:700px">

<div style="height:350px; overflow-x:hidden; overflow-y:auto;">
<jsp:include page="./viewApvLnInc.jsp" flush="false" />
</div>

<u:buttonArea topBlank="true">
	<u:button onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</div>