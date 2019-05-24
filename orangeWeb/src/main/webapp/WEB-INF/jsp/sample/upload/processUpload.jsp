<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.js" charset="UTF-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" charset="UTF-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/validator.js" charset="UTF-8"></script>
<title>Upload / Download sample</title>
<script>

</script>
</head>
<body>

<strong>
SampleUploadCtrl.java<br/>
processUpload.jsp<br/><br/>
</strong>

<form action="./downloadSample.do" method="get" >
	<span style="width:50px">원본 파일명</span> <input type="text" name="name" value="${paramMap.aaa}" size="60" /><br/>
	<span style="width:50px">임시 저장명</span> <input type="text" name="path" value="${fileMap.aaa}" size="60" /><br/>
	<button>${paramMap.aaa} 다운로드</button>
</form>

</body>
</html>