<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="com.innobiz.orange.web.cm.crypto.License"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ page isErrorPage="true"%><%

	// 라이센스, 사용불가 사용자
	request.setAttribute("errorCode", "license");

	String notice = License.ins.getNotice();
	if(notice==null) notice = "";
	else{
		notice = notice.replace("**********************************************************", "").substring(4);
		notice = notice.replace(" ","&nbsp;");
		notice = notice.replace("\r\n","<br/>\r\n");
	}
	request.setAttribute("notice", notice);
	session.invalidate();
	
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="${_lang}" lang="${_lang}">
<head>
<title><u:msg titleId="cm.msg.errors.licenseLimit" alt="라이센스 사용자 초과"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="${_cxPth}/css/container.blue.css" type="text/css" />
</head>
<body>
<div class="container">
<div class="wrapper">

	<div style="position:absolute; margin-left: 50%; left:-320px; margin-top:90px; width:800px">
		<div style="height:55; background:url('${_cxPth}/images/cm/warn.gif') left 7px no-repeat; padding-left:60px; padding-top:1px;"><h1><u:msg titleId="cm.error.title.${errorCode}" /></h1></div>
		<div style="background:url('${_cxPth}/images/cm/error_line.gif') left top no-repeat; width:100%; height:20px; margin-top:60px;"></div>
		<div style="padding:20px 0 10px 120px; font:14px 'dotum','arial'; color:#454545;">${notice}</div>
		<div style="background:url('${_cxPth}/images/cm/error_line.gif') left top no-repeat; width:100%; height:20px; margin-top:20px;"></div>
		<div style="padding-left:320px; padding-top:30px"><u:buttonS titleId="cm.error.toLogin" alt="로그인" href="${_cxPth}/cm/login/viewLogin.do" /></div>
	</div>

</div>
</div>
</body>
</html>