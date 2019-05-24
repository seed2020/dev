<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
$(document).ready(function() {
	var msg = "<u:out value="${message}" type="script" />";
	if(msg!='') alert(msg);
	var togo = "${togo}";
	if(togo!='') location.href = togo;
	var todo = "<u:out value="${todo}" type="script" />";
	if(todo!='') eval(todo);
});
//-->
</script>