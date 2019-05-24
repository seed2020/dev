<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="
		java.io.PrintStream,
		java.io.ByteArrayOutputStream,
		java.util.Enumeration,
		com.innobiz.orange.web.cm.utils.EscapeUtil,com.innobiz.orange.web.pt.utils.LoutUtil,
		com.innobiz.orange.web.pt.svc.PtSecuSvc,
		com.innobiz.orange.web.pt.secu.LoginSession,
		com.innobiz.orange.web.pt.secu.UserVo"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ page isErrorPage="true"%><%

	// 권한없음
	request.setAttribute("errorCode", "403");


	Throwable throwable = (Throwable) request.getAttribute("exception");
	if(throwable==null) throwable = pageContext.getException();
	String message = throwable==null ? null : throwable.getMessage();
	if(message!=null) request.setAttribute("message", message);
	/*
	String exceptionName = null;
	String trace = null;
	StringBuilder builder = new StringBuilder(1024 * 8);
	if(throwable!=null){
		exceptionName = throwable.getClass().getName();
		if(exceptionName!=null && !exceptionName.isEmpty()){
			builder.append("<b>Exception Name</b> : ");
			builder.append(exceptionName).append("<br /><br />\n");
		}
		message = throwable.getMessage();
		if(message!=null && !message.isEmpty() && !message.equals(exceptionName)){
			builder.append("<b>Exception Message</b> : ");
			builder.append(message).append("<br /><br />\n");
		}
		
		if(builder.length()>0){ builder.append("<br />\n"); }
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bout);
		throwable.printStackTrace(ps);
		trace = bout.toString("EUC-KR");
		builder.append(EscapeUtil.escapeHTML(trace));
	}
	*/
	response.setStatus(200);
	
	String uri = (String)request.getAttribute("_uri");
	int pageType = LoutUtil.getPageType(uri);
	
	if(pageType == LoutUtil.TYPE_AJAX){
%>{"message":"<u:msg titleId="cm.error.title.${errorCode}" type="value" />","model":{}}<%
		return;
	}
	if(pageType == LoutUtil.TYPE_POP){
		String style = (pageType == LoutUtil.TYPE_POP) ? "width:650px" : "";
%><div style="<%= style%>"><h1><u:msg titleId="cm.error.title.${errorCode}" /></h1></div><%
		return;
	}
	
	if(pageType == LoutUtil.TYPE_PAGE){
		
		// 레이아웃 설정이 없으면 - top 메뉴 레이아웃 다시 로드함
		if(request.getAttribute("_loutCatId")==null){
			try{
				UserVo userVo = LoginSession.getUser(request);
				PtSecuSvc.ins.processSecurity(request, uri==null ? "/" : uri, userVo, true);
			} catch(Exception ignore){
				//ignore.printStackTrace();
			}
		}
		
		request.setAttribute("isPage", Boolean.TRUE);
	} else {
		request.setAttribute("isPage", Boolean.FALSE);
		
		if(pageType == LoutUtil.TYPE_TRANS){
			request.setAttribute("isTrans", Boolean.TRUE);
		} else if(pageType == LoutUtil.TYPE_IFRM){
			request.setAttribute("isIfrm", Boolean.TRUE);
		} else if(pageType == LoutUtil.TYPE_PLT || pageType == LoutUtil.TYPE_PLT_IFRM){
			request.setAttribute("isPlt", Boolean.TRUE);
		}
	}
	
	if(request.getAttribute("_skin")==null) request.setAttribute("_skin", "blue");
	
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="${_lang}" lang="${_lang}">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><u:msg titleId="cm.error.title.${errorCode}" /></title>
<jsp:include page="/WEB-INF/tiles/headinc.jsp" />
</head>
<body class="styleThese">

<c:if test="${not isPage}"><c:if

	test="${isTrans}">
<script type="text/javascript">
$(document).ready(function() {
	alert("<u:msg titleId="cm.error.title.${errorCode}" type="script" />");
});
</script></c:if><c:if

	test="${isPlt or isIfrm}">
<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="0" style="width:100%;">
<tr id="lineTr"><td class="line"></td></tr>
<tr><td class="nodata"><u:msg titleId="cm.error.title.${errorCode}" /></td></tr>
<tr id="lineTr"><td class="line"></td></tr>
</table></c:if>
</c:if>

<c:if test="${isPage}">
	<div class="homewrapper">
	<u:set test="${_loutCatId != 'I'}" var="_background" value="background:url(/images/cm/bg_a.png) no-repeat;" elseValue="" />
	<div id="header_${_skin}" style="${_background}">
	<c:if test="${_loutCatId == 'I'}"><jsp:include page="/WEB-INF/tiles/headerIcon.jsp" /></c:if>
	<c:if test="${_loutCatId != 'I'}"><jsp:include page="/WEB-INF/tiles/headerBasc.jsp" /></c:if>
	</div>
	
	<div class="container">
		<div class="wrapper">
		
		<div style="position:absolute; margin-left: 50%; left:-320px; margin-top:80px; width:800px">
			<div style="height:55; background:url('${_cxPth}/images/cm/warn.gif') left 7px no-repeat; padding-left:60px; padding-top:1px;"><h1><u:msg titleId="cm.error.title.${errorCode}" /></h1></div>
			<div style="background:url('${_cxPth}/images/cm/error_line.gif') left top no-repeat; width:100%; height:20px; margin-top:80px;"></div>
			<div style="padding:20px 0 10px 30px; font:12px 'dotum','arial'; color:#454545;"><b><u:msg titleId="cm.error.msg" /> : </b>${message}</div>
			<div style="padding:0px 0 10px 30px; font:12px 'dotum','arial'; color:#454545;"><b><u:msg titleId="cm.error.url" /> : </b>${_uri}</div>
			<div style="background:url('${_cxPth}/images/cm/error_line.gif') left top no-repeat; width:100%; height:20px; margin-top:20px;"></div>
			<div style="padding-left:320px; padding-top:30px"><u:buttonS titleId="cm.error.toHome" alt="홈으로" href="javascript:goHome();" /></div>
		</div>

		</div>
	</div>
	</div>
</c:if>

</body>
</html>