<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		com.innobiz.orange.web.pt.secu.SecuUtil,
		com.innobiz.orange.web.cm.utils.SessionUtil,
		com.innobiz.orange.web.cm.utils.MessageProperties"

%><%@ attribute name="id"    required="false"
%><%@ attribute name="title"    required="false"
%><%@ attribute name="titleId"  required="false"
%><%@ attribute name="href"  	required="false"
%><%@ attribute name="onclick"  required="false"
%><%@ attribute name="koDesc" required="false"
%><%@ attribute name="alt"  required="false"
%><%@ attribute name="auth"  required="false"
%><%@ attribute name="ownerUid" required="false"


%><%
/*
title.tag, tabGroup.tag 안에서 쓰임


titleId : message 파일에 등록된 메세지 아이디
href	: a 테그의 href
onclick	: a 테그의 onclick
auth	: S:슈퍼 > A:관리 > M:수정 > W:쓰기 > R:읽기
koDesc	: 한글일 경우 표시 할 title 풍선도움말(title값)
alt		: 사용되지 않음 - 참조용으로 한글명 넣어 주세요

*/


if(SecuUtil.hasAuth(request, auth, ownerUid)){

	if(titleId!=null){
		title = MessageProperties.getInstance().getMessage(titleId, request);
	}
	
	if(id==null) id = "";
	else id = " id=\""+id+"\"";
	
	// title/tagGroup 에 붙을 수 있는 서브타입 체크해서 현재의 타입이 아니면
	// 해당하는 테그를 닫거나 열어 주는 역할을 함
	String tabSubType = (String)request.getAttribute("tabSubType");
	if(tabSubType==null || "tab".equals(tabSubType)){
		out.println("\r\n	</ul>\r\n	</div>");	// ul,div for tab(left-part) - close
		out.println("\r\n	<div class=\"tab_right\">\r\n	<ul>");	// ul,div for (right-part) - open
		
		out.println("\r\n		<li class=\"txt\">");	// li 테그 for txt - open
		request.setAttribute("tabSubType", "txt");
	} else if(!"txt".equals(tabSubType)){
		out.println("\r\n		</li>");				// 기존에 열린 li 테그 - close
		out.println("\r\n		<li class=\"txt\">");	// li 테그 for txt - open
		request.setAttribute("tabSubType", "txt");
	}
	
	boolean hasLink = true;
	if(href==null && onclick==null) hasLink = false;
	if(href==null) href="javascript:void(0);";
	if(onclick==null) onclick = "";
	else onclick = " onclick=\""+onclick+"\"";
	
	if("ko".equals(SessionUtil.getLangTypCd(request))){
		if(koDesc==null) koDesc = title;
	} else {
		koDesc = title;
	}
	
	if(hasLink){
		%><a<%=id%> href="<%=href%>"<%=onclick %> title="<%= koDesc%>"><%= title%></a><%
	} else {
		%><span><%= title%></span><%
	}
	
}
%>