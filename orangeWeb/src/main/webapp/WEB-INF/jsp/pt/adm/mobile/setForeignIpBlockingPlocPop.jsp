<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Enumeration, java.util.ArrayList" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
%>
<script type="text/javascript">
<!--
function saveForeignIpBlockingPloc(){
	var $form = $('#foreignIpBlockingPlocForm');
	$form.attr('action','./transForeignIpBlockingPolc.do');
	$form.attr('target', 'dataframe');
	$form.submit();
}
//-->
</script>
<div style="width:500px;">

<form id="foreignIpBlockingPlocForm">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea colgroup="30%,70%">
	<tr>
		<td class="head_ct"><u:msg titleId="pt.jsp.blockingCountry" alt="차단 국가" /></td>
		<td class="head_ct"><u:msg titleId="pt.jsp.blockForeignPloc" alt="해외 IP 차단 정책" /></td>
	</tr>
	<tr>
		<td class="body_ct"><u:msg titleId="pt.jsp.China" alt="중국" /></td>
		<td align="center"><u:checkArea>
			<u:radio value="block" name="chinese" alt="IP 차단" checkValue="${chinese}" titleId="pt.jsp.blockIp" />
			<u:radio value="delay" name="chinese" alt="IP 지연" checkValue="${chinese}" titleId="pt.jsp.delayIp" />
			<u:radio value="none" name="chinese" alt="정책 사용 안함" checkValue="${chinese}" titleId="pt.jsp.noPolc" />
			</u:checkArea>
		</td>
	</tr>
	<tr>
		<td class="body_ct"><u:msg titleId="pt.jsp.Foreign" alt="국외" /></td>
		<td align="center"><u:checkArea>
			<u:radio value="block" name="foreign" alt="IP 차단" checkValue="${foreign}" titleId="pt.jsp.blockIp" />
			<u:radio value="delay" name="foreign" alt="IP 지연" checkValue="${foreign}" titleId="pt.jsp.delayIp" />
			<u:radio value="none" name="foreign" alt="정책 사용 안함" checkValue="${foreign}" titleId="pt.jsp.noPolc" />
			</u:checkArea>
		</td>
	</tr>
</u:listArea>
</form>

<u:buttonArea>
	<c:if test="${sessionScope.userVo.userUid eq 'U0000001'}">
	<u:button alt="저장" href="javascript:saveForeignIpBlockingPloc();" titleId="cm.btn.save" /></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>