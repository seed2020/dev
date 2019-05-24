<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<div style="width:400px">
<form id="setRetnForm">

<u:listArea colgroup="20%,80%">
	<tr>
	<td class="head_ct"><u:msg titleId="ap.jsp.retnOpin" alt="반송의견" /></td>
	<td class="body_lt" style="min-height:50px;"><u:out value="${apOngdSendDVo.hdlrOpinCont}" /></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>