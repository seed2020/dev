<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
		import="java.util.ArrayList"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><script type="text/javascript">
<!--
function setWfFormBodyPop(){
	if(setWfFormBody($("#wfFormBodyPopArea"), true) == true){
		dialog.close("setDocBodyHtmlDialog");
	}
}
//-->
</script>
<div style="width:1024px;">

<div id="wfFormBodyPopArea" style="width:100%; max-height:600px; overflow:auto;"><u:set
	test="${true}" var="wfMenuParams" value="menuId=${menuId}&bxId=${param.bxId}${strMnuParam}" />
<jsp:include page="/WEB-INF/jsp/${includeJsp}" />
</div>

<div class="blank"></div>
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setWfFormBodyPop();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>
</div>