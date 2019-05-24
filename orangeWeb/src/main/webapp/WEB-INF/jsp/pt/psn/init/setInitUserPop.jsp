<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
//  %>
function saveInitUser(){
	var defUserUid = $("#initUserForm input[name='defUserUid']:checked").val();
	callAjax("./transInitUserAjx.do?menuId=${menuId}", {defUserUid:defUserUid}, function(data){
		if(data.message != null) alert(data.message);
		if(data.result == 'ok'){
			dialog.close('setInitUserDialog');
		}
	});
}
//-->
</script>
<div style="width:400px">

<form id="initUserForm">
<u:listArea colgroup="30%,70%">
	<tr>
		<td class="head_ct"><u:msg titleId="pt.btn.dept" alt="부서" /></td>
		<td class="body_lt"><c:forEach
			items="${sessionScope.userVo.adurs}" var="adurs" varStatus="status">
			<u:checkArea>
			<tr><u:radio name="defUserUid" value="${adurs[1]}" title="${adurs[0]}" checked="${defUserUid == adurs[1]}" /></tr>
			</u:checkArea>
			</c:forEach>
		</td>
	</tr>
</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveInitUser();" alt="저장" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</div>