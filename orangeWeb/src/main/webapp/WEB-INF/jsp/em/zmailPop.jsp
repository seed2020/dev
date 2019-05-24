<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
// 현재 사용 안함
%><c:if
	test="${not empty redirectUrl}"><c:redirect url="${redirectUrl}" /></c:if><c:if
	test="${empty redirectUrl}"><c:redirect
	
	url="http://${mailSvr}/zmail/login.nvd?cmd=${cmd}&email_id=${param.emailId }&etc=close_window&onetime=${onetime}" />
<script type="text/javascript">
if('${onetime}' == ''){
	//window.close();
}
</script>
</c:if>