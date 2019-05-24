<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><script type="text/javascript">
<!--
function printExtDoc(){
	printWeb();
}
function toDoc(){
	history.go(-1);
}
//-->
</script>
<div id="docDiv">

<u:title titleId="ap.jsp.viewDoc" alt="문서 조회" notPrint="true" />

<%-- 버튼 --%>
<div id="docBtnArea">
<div class="front notPrint">
	<div class="front_right">
	<table border="0" cellpadding="0" cellspacing="0"><tbody><tr>
	<td class="frontbtn"><u:buttonS titleId="ap.btn.print" alt="인쇄" href="javascript:printExtDoc();" /></td>
	<td class="frontbtn"><u:buttonS titleId="cm.btn.close" alt="닫기" href="javascript:toDoc();" /></td>
	</tr></tbody></table>
	</div>
</div>
<div class="apBtnLine notPrint"></div>
</div>


<c:if test="${not empty apFormJspDVo.jspPath}"><jsp:include
	page="/WEB-INF/jsp${apFormJspDVo.jspPath}" flush="true"><jsp:param
		name="bxId" value="${param.bxId}"/></jsp:include></c:if>

</div>