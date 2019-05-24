<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript" src="/js/rsa.js" charset="UTF-8"></script>
<script type="text/javascript">
<!--
<% // 게시물 조회 %>
function viewBullAction(event) {
	if (validator.validate('setLoginForm')) {
		var param = new ParamMap().getData('setLoginForm');
		callAjax("/cm/login/createSecuSessionAjx.do", null, function(data){
			var key = new RSAPublicKey(data.e, data.m);
			var data = encrypt(key, JSON.stringify(param.toJSON()));
			<c:if test="${!empty param.callback }">${param.callback}('${param.bullId}', data);</c:if>
			<c:if test="${empty param.callback }">
			var frm = $('#loginSecuFrm');
			frm.find('#secu').val(data);
			frm.attr('action','${!empty param.pltYn ? "/bb" : "."}/${param.viewPage}.do?menuId=${menuId}');
			frm.submit();
			</c:if>			
		});
		
	}
}
//-->
</script>
<div style="width:200px">
<form id="setLoginForm" onsubmit="return doNotSubmit(event)">
<% // 폼 필드 %>
<div class="front">
<div class="front_left">
	<table border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td class="color_stxt"><u:msg titleId="bb.jsp.setLoginPop.tx01" alt="로그인 비밀번호를 입력하세요." /></td>
	</tr>
	</tbody></table>
</div>
</div>
<u:listArea colgroup="27%,73%">
<tr>
<td><u:input id="pw" type="password" mandatory="Y" onkeydown="doNotSubmit(event, viewBullAction)" style="width: 183px;" /></td>
</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" alt="확인" onclick="viewBullAction();" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
<form id="loginSecuFrm" method="post">
<c:if test="${fn:length(paramEntryList) > 0}">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
	<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
</c:if>
<u:input type="hidden" id="secu" />
</form>
</div>
