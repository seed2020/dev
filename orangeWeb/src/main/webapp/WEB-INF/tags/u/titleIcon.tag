<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		com.innobiz.orange.web.pt.secu.SecuUtil,
		com.innobiz.orange.web.cm.utils.SessionUtil,
		com.innobiz.orange.web.cm.utils.MessageProperties"

%><%@ attribute name="id"    required="false"
%><%@ attribute name="title"    required="false"
%><%@ attribute name="titleId"  required="false"
%><%@ attribute name="type"  required="false"
%><%@ attribute name="href"  	required="false"
%><%@ attribute name="onclick"  required="false"
%><%@ attribute name="koDesc" required="false"
%><%@ attribute name="style" required="false"
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

type	: up, down, plus, minus 중 하나

*/

if(SecuUtil.hasAuth(request, auth, ownerUid)){

	if(title==null){
		if(titleId==null) titleId = "cm.btn."+type;
		title = MessageProperties.getInstance().getMessage(titleId, request);
	}
	
	if(id==null) id = "";
	else id = " id=\""+id+"\"";
	
	// title/tagGroup 에 붙을 수 있는 서브타입 체크해서 현재의 타입이 아니면
	// 해당하는 테그를 닫거나 열어 주는 역할을 함
	String titleButtonType = (String)request.getAttribute("titleSubType");
	if(titleButtonType==null){
		out.println("\r\n			<li class=\"ico\">");	// li btn 테그 - open
		request.setAttribute("titleSubType", "ico");
	} else if(!"ico".equals(titleButtonType)){
		out.println("\r\n			</li>");				// 기존에 열린 li 테그 - close
		out.println("\r\n			<li class=\"ico\">");	// li btn 테그 - open
		request.setAttribute("titleSubType", "ico");
	}
	
	if(href==null) href="javascript:void(0);";
	if(onclick==null) onclick = "";
	else onclick = " onclick=\""+onclick+"\"";
	
	if("ko".equals(SessionUtil.getLangTypCd(request))){
		if(koDesc==null) koDesc = title;
	} else {
		koDesc = title;
	}
	
	String imgPath = "";
	if("up".equals(type)){
		imgPath = "ico_wup.png";
	} else if("down".equals(type)){
		imgPath = "ico_wdown.png";
	} else if("plus".equals(type)){
		imgPath = "ico_wadd.png";
	} else if("minus".equals(type)){
		imgPath = "ico_wminus.png";
	} else if("move.top".equals(type)){
		imgPath = "ico_move.top.png";
	} else if("move.bottom".equals(type)){
		imgPath = "ico_move.bottom.png";
	} else {
		throw new RuntimeException("'type' attribute in 'titleIcon.tag' has to be one of 'up', 'down', 'plus', 'minus'.");
	}

	style = (style==null) ? "" : " style=\""+style+"\"";
%>
			<a<%=id%> href="<%=href%>"<%=onclick %><%=style%>><img src="${_cxPth}/images/${_skin}/<%=imgPath%>" width="20" height="20" title="<%= title%>" /></a><%
}
%>