<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
$(document).ready(function() {
	if($m && $m.secu) $m.secu.clearKey();
	var msg = '<u:out value="${message}" type="script" />';
	if(window!=parent){
		if(msg!=''){
			$m.dialog.alert(msg, function(){
				$m.nav.toFirst(true);
			});
		} else {
			$m.nav.toFirst(true);
		}
	} else {
		if(msg!=''){
			if($m==null){
				alert(msg);
				location.replace('/');
			} else {
				$m.dialog.alert(msg, function(){
					location.replace('/');
				});
			}
		} else {
			location.replace('/');
		}
	}
});
//]]>
</script>