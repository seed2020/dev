<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><!DOCTYPE html>
<html>
<head><script type="text/javascript">
function init(){
	var msg = "${msgVa}";
	if(msg!='') alert("${msgVa}");
	<c:if test="${not empty param.func}">
	if(typeof(opener) != "undefined") {
		try {
			opener.$m.nav.getWin(0).${param.func}();
		} catch(err) {}
	}
	</c:if>
	window.close();
}
</script>
</head><body onload="init()"></body>
</html>