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
setUpload.jsp<br/><br/>
</strong>

<form action="./processSimpleUpload.do" method="post" enctype="multipart/form-data" >
	<select name="mode" onchange="document.forms[0].action = this.value">
		<option value="./processSimpleUpload.do">simple mode</option>
		<option value="./processDistributeUpload.do">distribute mode</option>
	</select><br/>
	<input type="file" name="aaa" /><br/>
	<input type="text" name="bbb" value="123" /><br/>
	<button>전송</button>
</form>

</body>
</html>