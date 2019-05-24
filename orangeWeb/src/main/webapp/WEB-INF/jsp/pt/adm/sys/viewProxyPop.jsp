<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Enumeration, java.util.ArrayList" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--

//-->
</script>
<div style="width:400px; ">

<u:listArea style="max-height:320px; overflow-y:auto;">
	<tr>
		<td class="head_ct">Header</td>
	</tr>
	<c:forEach items="${headerNames}" var="headerName">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="body_lt">${headerName}</td>
	</tr></c:forEach>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>