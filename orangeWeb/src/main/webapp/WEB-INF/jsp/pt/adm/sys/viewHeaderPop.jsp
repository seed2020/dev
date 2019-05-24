<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Enumeration, java.util.ArrayList" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	Enumeration<String> enums = request.getHeaderNames();
	ArrayList<String[]> headers = new ArrayList<String[]>();
	String header;
	while(enums.hasMoreElements()){
		header = enums.nextElement();
		headers.add(new String[]{header, request.getHeader(header)});
	}
	request.setAttribute("headers", headers);
%>
<script type="text/javascript">
<!--

//-->
</script>
<div style="width:700px; ">

<u:listArea style="max-height:400px; overflow-y:auto;" colgroup="20%,80%">
	<tr>
		<td class="head_ct">Header</td>
		<td class="head_ct">Value</td>
	</tr>
	<c:forEach items="${headers}" var="arr">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="body_lt">${arr[0]}</td>
		<td class="body_lt">${arr[1]}</td>
	</tr></c:forEach>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>