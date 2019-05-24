<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	request.setAttribute("envIds", new String[]{
			"mailCall", "smtpCall", "messengerCall", 
			"webDomain", "imgDomain", "mobileDomain", 
			"erpCall", "pushPort",
			"apInterface1","apInterface2","apInterface3"});

%>
<script type="text/javascript">
<!--
function doSubmit(){
	if(validator.validate('svrSetupForm')){
		var $form = $('#svrSetupForm');
		$form.attr("action","./transEngSvrSetup.do");
		$form.attr("target","dataframe");
		$form.submit();
	}
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.setOrTerms.title" alt="조직도 용어 설정" menuNameFirst="true" />

<form id="svrSetupForm">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="setupClsId" value="${setupClsId}" />

<u:listArea id="termsArea">

	<c:forEach items="${envIds}" var="envId" varStatus="status"><u:set
		test="${ (envId == 'webDomain' or envId == 'imgDomain' or envId == 'smtpCall')
			or (envId == 'mailCall' and sysPlocMap.mailEnable == 'Y')
			or (envId == 'messengerCall' and sysPlocMap.messengerEnable == 'Y')
			or (envId == 'mobileDomain' and sysPlocMap.mobileEnable == 'Y' and sysPlocMap.mobilePushEnable == 'Y')
			or (envId == 'erpCall' and sysPlocMap.erpEnable == 'Y')
			or (envId == 'pushPort' and sysPlocMap.pcNotiEnable == 'Y')
			or (envId == 'apInterface1' and sysPlocMap.apInterface1 == 'Y')
			or (envId == 'apInterface2' and sysPlocMap.apInterface2 == 'Y')
			or (envId == 'apInterface3' and sysPlocMap.apInterface3 == 'Y')
			 }"
		var="optEnabled" value="Y" elseValue="N" cmt="해당 설정 가능 여부" /><u:msg
		titleId="em.${envId}" var="envNm" /><u:convertMap
		var="envValue" srcId="svrEnvMap" attId="${envId}" /><c:if test="${optEnabled == 'Y'}">
	<tr>
		<td width="18%" class="head_lt">${envNm}</td>
		<td><u:input id="${envId}" name="${setupClsId}_${envId}" title="${envNm}" value="${envValue}" style="width:98%" maxByte="100" /></td>
	</tr></c:if><c:if
		test="${optEnabled != 'Y'}">
	<u:input id="${envId}" name="${setupClsId}_${envId}" title="${envNm}" value="${envValue}" type="hidden" />
	</c:if>
	</c:forEach>

</u:listArea>
</form>

<u:buttonArea><c:if test="${sessionScope.userVo.userUid == 'U0000001'}"><u:msg
		titleId="pt.sys.setup" alt="시스템 설정" var="setupTitle" />
	<u:button titleId="pt.sys.setup" alt="시스템 설정" onclick="dialog.open('setSysPolcDialog','${setupTitle}','./setSysPolcPop.do?menuId=${menuId}');" /></c:if>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:doSubmit();" auth="SYS" />
</u:buttonArea>