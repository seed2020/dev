<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%><script type="text/javascript">
//<![CDATA[
$(document).ready(function() {
	var msg = '<u:out value="${message}" type="script" />';
	if(msg!=''){
		$m.dialog.alert(msg);
	} else if(window!=parent){
		$m.nav.toFirst(true);
	}
});
//]]>
</script>