<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.cm.utils.MessageProperties"
%><%@ attribute name="id" required="false"
%><%@ attribute name="type" required="true"
%><%@ attribute name="display" required="false" type="java.lang.Boolean"
%><%@ attribute name="repeat" required="false" type="java.lang.Integer"
%><%@ attribute name="titleId" required="false"
%><%
/*
	첨부파일 (@) 표시		-> <u:icon type="att" />
	들여쓰기(indent) 표시	-> <u:icon type="indent" />
	자물쇠 아이콘			-> <u:icon type="lock" />
	새글 (new) 표시		-> <u:icon type="new" />
	다음글(▼) 표시		-> <u:icon type="next" />
	이전글(▲) 표시		-> <u:icon type="prev" />
	추천 표시				-> <u:icon type="recommend" />
	답변 표시				-> <u:icon type="reply" />
	공지 표시				-> <u:icon type="notc" />
*/

if(id!=null && !id.isEmpty()) id = " id=\""+id+"\"";
else id = "";

String style = "";
if(Boolean.FALSE.equals(display)){
	style = " style=\"display:none\"";
}

String altTxt = "", file = "", width = "10", height = "10";
if ("att".equals(type)) {
	altTxt = MessageProperties.getInstance().getMessage("cm.ico.att", request);
	file = "ico_file.png"; width = "10"; height = "10";
} else if ("indent".equals(type)) {
	altTxt = "";
	file = "ico_blank.png"; width = "13"; height = "10";
} else if ("lock".equals(type)) {
	altTxt = MessageProperties.getInstance().getMessage("cm.ico.priv", request);
	file = "ico_lock.png"; width = "11"; height = "10";
} else if ("new".equals(type)) {
	altTxt = "new";
	file = "ico_new.png"; width = "12"; height = "10";
} else if ("next".equals(type)) {
	altTxt = MessageProperties.getInstance().getMessage("cm.ico.next", request);
	file = "ico_wdown.png"; width = "20"; height = "20";
} else if ("prev".equals(type)) {
	altTxt = MessageProperties.getInstance().getMessage("cm.ico.prev", request);
	file = "ico_wup.png"; width = "20"; height = "20";
} else if ("recommend".equals(type)) {
	altTxt = MessageProperties.getInstance().getMessage("cm.ico.recmd", request);
	file = "ico_recommend.png"; width = "12"; height = "13";
} else if ("reply".equals(type)) {
	altTxt = "reply";
	file = "ico_comment.png"; width = "13"; height = "10";
} else if ("notc".equals(type)) {
	altTxt = "notice";
	file = "ico_notice.png"; width = "12"; height = "10";
}
MessageProperties properties = MessageProperties.getInstance();
if(titleId!=null && !titleId.isEmpty()){	
	altTxt = MessageProperties.getInstance().getMessage(titleId, request);
}
if (repeat == null) repeat = 1;
for (int i = 0; i < repeat; i++) {
	%><img src="${_cxPth}/images/${_skin}/<%=file%>"<%=id %> alt="<%=altTxt%>" title="<%=altTxt%>" width="<%=width%>" height="<%=height%>"<%=style %>/><%
}
%>