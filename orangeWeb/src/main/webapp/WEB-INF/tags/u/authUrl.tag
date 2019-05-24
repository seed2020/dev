<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.pt.secu.UserVo,com.innobiz.orange.web.pt.svc.PtSecuSvc"
%><%@ attribute name="url" required="true"
%><%@ attribute name="authCheckUrl" required="false"
%><%@ attribute name="menuId" required="false"
%><%@ attribute name="var" required="false"
%><%
/*
	권한체크용 menuId를 붙인 경로로 전환
	
*/
	String authUrl = null;
	if(menuId==null){
		UserVo userVo = (UserVo)session.getAttribute("userVo");
		authUrl = PtSecuSvc.ins.toAuthMenuUrl(userVo, url, authCheckUrl);
	} else {
		if(url.indexOf('?')>0) authUrl = url + "&menuId=" + menuId;
		else authUrl = url + "?menuId=" + menuId;
	}
	
	if(var==null){
		%><%= authUrl%><%
	} else {
		request.setAttribute(var, authUrl);
	}

%>