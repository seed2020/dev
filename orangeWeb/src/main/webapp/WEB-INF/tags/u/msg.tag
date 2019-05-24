<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Locale,
			com.innobiz.orange.web.cm.utils.EscapeUtil,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.SessionUtil,
			com.innobiz.orange.web.cm.utils.StringUtil"

%><%@ attribute name="titleId" required="false"
%><%@ attribute name="title" required="false"
%><%@ attribute name="alt"     required="false"
%><%@ attribute name="arguments"   required="false"
%><%@ attribute name="separator"   required="false"
%><%@ attribute name="javaScriptEscape" required="false" type="java.lang.Boolean"
%><%@ attribute name="htmlEscape" required="false" type="java.lang.Boolean"
%><%@ attribute name="langTypCd" required="false"
%><%@ attribute name="charSeperator" required="false"
%><%@ attribute name="type" required="false"
%><%@ attribute name="var" required="false"
%><%
/*

<u:message titleId="msg.cmm.errors.range" arguments="#label.cmm.title,1,10" javaScriptEscape="true" argumentSeparator="," />

label.cmm.title : 제목
msg.cmm.errors.range : {0}은(는) {1}과 {2} 사이의 값이어야 합니다.
argumentSeparator : 없으면 콤마(,) 로 인식
  >>> 결과 "[[", "]]" 없이 아래와 같이 출력
  >>> [[제목]]은(는) [[0]]과 [[10]] 사이의 값이어야 합니다.

*/


if(separator==null || separator.isEmpty()) separator = ",";
String[] argArray = (arguments==null || arguments.isEmpty()) ? null : arguments.split(separator);
String message = "";

if( (titleId!=null && !titleId.isEmpty()) || (title!=null && !title.isEmpty())){
	if(titleId!=null && !titleId.isEmpty()){
		try{
			Locale locale = (langTypCd==null || langTypCd.isEmpty())
					? SessionUtil.getLocale(request) : SessionUtil.toLocale(langTypCd);
			MessageProperties properties = MessageProperties.getInstance();
			message = properties.getMessage(titleId, argArray, locale);
		} catch(Exception e){
			throw new RuntimeException("msg.tag : the code named '"+titleId+"' does not defined !");
		}
	} else if(title!=null && !title.isEmpty()){
		message = title;
	}

	if(javaScriptEscape!=null && Boolean.TRUE.equals(javaScriptEscape)){
		message = EscapeUtil.escapeScript(message);
	}
	if(htmlEscape!=null && Boolean.TRUE.equals(htmlEscape)){
		message = EscapeUtil.escapeHTML(message);
	}

	if(message!=null && !message.isEmpty() && charSeperator!=null && !charSeperator.isEmpty()){
		StringBuilder builder = new StringBuilder(64);
		boolean first = true;
		for(char c : message.toCharArray()){
			if(first) first=false;
			else builder.append(charSeperator);
			builder.append(c);
		}
		message = builder.toString();
	}

	if(type!=null && !type.isEmpty()){
		if(type==null || type.equals("html")) {
			message = EscapeUtil.escapeHTML(message, "");
		} else if(type.equals("value")) {
			message = EscapeUtil.escapeValue(message);
		} else if(type.equals("script")) {
			message = EscapeUtil.escapeScript(message);
		} else if(type.equals("number")) {
			message = StringUtil.toNumber(message);
		} else if(type.equals("date")) {
			message = StringUtil.toShortDate(message);
		} else if(type.equals("longdate")) {
			message = StringUtil.toLongDate(message);
		} else if(type.equals("shortdate")) {
			if (StringUtil.getCurrYmd().equals(StringUtil.toShortDate(message))) {
				message = StringUtil.toHourMinute(message);
			} else {
				message = StringUtil.toShortDate(message);
			}
		} else if(type.equals("hm")) {
			message = StringUtil.toHourMinute(message, "00:00");
		}
	}
}

if(var!=null){
	request.setAttribute(var, message);
} else {
	%><%= message%><%
}
%>