<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"	
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="${_lang}" lang="${_lang}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Content-Type" content="text/html; charset=utf-8" />
<meta name="format-detection" content="telephone=no" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1" />

<title><u:msg title="${message }" /></title>
<style>
body{font:12px "dotum","arial"; color:#454545; }
</style>
</head>
<body class="styleThese">
<script type="text/javascript">
$(document).ready(function() {
});
</script>
<div class="container">
	<div class="wrapper" style="text-align:center;">
		<div style="margin:0 auto;display:inline-block;">
			<div style="height:55;"><h1>${message }</h1></div>
			<div style="padding-left:320px; padding-top:30px">
				<u:buttonS href="javascript:;" titleId="cm.btn.close" alt="닫기" onclick="window.close();" />
			</div>
		</div>

	</div>
</div>
</body>
</html>