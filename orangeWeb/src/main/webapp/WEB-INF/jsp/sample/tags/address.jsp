<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
%>
<script type="text/javascript">
$(document).ready(function() {
	setUniformCSS();
});
</script>

</head>
<body>


<br/><br/><br/>
<table border="1">
	<tr>
		<td width="150">1</td>
		<td width="150"><a href="javascript:test()">test</a></td>
		<td width="250"><a href="javascript:test()">test</a></td>
	</tr>
	<tr>
		<td width="150">자택우편번호</td>
		<td colspan="2"><u:address id="home" alt="자택우편번호" adrStyle="width:94%"
			zipNoValue="" adrValue="" detlAdrValue="" /></td>
	</tr>
</table>

