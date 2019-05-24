<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Enumeration, java.util.ArrayList" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	String[] opts = "none,1,2,3,6,12,24".split(",");
	request.setAttribute("opts", opts);

%>
<script type="text/javascript">
<!--
function saveForeignIpBlockingPloc(){
	var $form = $('#transMsgLginForm');
	$form.attr('action','./transMsgLginPop.do');
	$form.attr('target', 'dataframe');
	$form.submit();
}
//-->
</script>
<div style="width:400px;">

<form id="transMsgLginForm">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea colgroup="50%,50%">
	<tr>
		<td class="head_ct"><u:msg titleId="or.jsp.setUserPop.useMsgLginVaildTime" alt="자동 로그인 유효 시간" /></td>
		<td class="head_lt"><select name="autoTime"<u:elemTitle titleId="or.jsp.setUserPop.useMsgLgin" />><c:forEach
			items="${opts}" var="opt">
			<u:option value="${opt}" titleId="pt.msgLgin.opt.${opt}" checkValue="${autoTime}" /></c:forEach></select></td>
	</tr>
</u:listArea>
</form>

<u:buttonArea>
	<u:button alt="저장" href="javascript:saveForeignIpBlockingPloc();" titleId="cm.btn.save" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>