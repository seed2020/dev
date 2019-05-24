<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
$(document).ready(function() {
	if($m.secu) $m.secu.setKey('${secuE}', '${secuM}', '${secuX}');
	var msgId = '${ptPushMsgDVo.pushMsgId}';<%
if(request.getAttribute("uagntToken")!=null){%>
	if(window.localStorage){ window.localStorage.setItem("uagntToken", "${uagntToken}"); }<%
}%>
	if(msgId!=''){
		top.location.replace('/');
	} else {
		if(window!=parent) {
			$m.nav.toFirst(false);
		}
	}
});
//]]>
</script>