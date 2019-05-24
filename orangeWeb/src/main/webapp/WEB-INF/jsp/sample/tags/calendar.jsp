<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko"><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>달력 샘플</title>

<link rel="stylesheet" href="/css/uniform.default.blue.css" type="text/css" />
<link rel="stylesheet" href="/css/container.blue.css" type="text/css" />
<script type="text/javascript" src="/js/jquery-1.10.2.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/jquery.uniform.js" charset="UTF-8"></script>
<script type="text/javascript" src="/js/common.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/commonEx.js" charset="utf-8"></script>
<script type="text/javascript">
$(document).ready(function() {
	setUniformCSS();
});
</script>

</head>
<body>

	
<form class="styleThese">

<br/><br/><br/>
<table>
	<tr>
		<td width="200">생년월일 : 날짜</td>
		<td width="150"><u:calendar id="birth" titleId="cols.birth" alt="생년월일" /></td>
		<td width="150"></td>
	</tr>
	<tr>
		<td width="200">선택~완료 일시 : 기간</td>
		<td width="150"><u:calendar id="choiDt" option="{end:'cmltDt'}" titleId="cols.choiDt" alt="선택일시" /></td>
		<td width="150"><u:calendar id="cmltDt" option="{start:'choiDt'}" titleId="cols.cmltDt" alt="완료일시" /></td>
	</tr>
</table>

</form>
</body>
</html>