<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
		import="com.innobiz.orange.web.cm.utils.EscapeUtil, com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ attribute name="value" required="false"
%><%@ attribute name="altValue" required="false"
%><%@ attribute name="nullValue" required="false"
%><%@ attribute name="type" required="false"
%><%@ attribute name="var" required="false"
%><%@ attribute name="maxLength" required="false" type="java.lang.Integer"
%><%
/*

	설명 : XSS 보안 적용용 테그 라이브러리
	
type ---------	
	value  : <input ~ value="~~요기">
	html   : html 내에 삽입
	script : var aaa ="~~요기" 에사용 - 항상 쌍따옴표("") 로 앞뒤를 감싸 주어야함
	number : 123,456 출력
	date  : yyyy-mm-dd
	longdate: yyyy-mm-dd hh:mi:ss
	hm    : hh:mi
	
*/

if(value==null || value.isEmpty()) value = altValue;

String result = "";

if(type==null || type.equals("html")) {
	result = nullValue==null ? EscapeUtil.escapeHTML(value) : EscapeUtil.escapeHTML(value, nullValue);
} else if(type.equals("value")) {
	result = EscapeUtil.escapeValue(value);
} else if(type.equals("script")) {
	result = EscapeUtil.escapeScript(value);
} else if(type.equals("noscript")) {
	if(value != null && !value.isEmpty()) result = EscapeUtil.replaceScript(value);
} else if(type.equals("number")) {
	result = StringUtil.toNumber(value);
} else if(type.equals("date")) {
	result = StringUtil.toShortDate(value);
} else if(type.equals("longdate")) {
	result = StringUtil.toLongDate(value);
} else if(type.equals("shortdate")) {
	if (StringUtil.getCurrYmd().equals(StringUtil.toShortDate(value))) {
		result = StringUtil.toHourMinute(value);
	} else {
		result = StringUtil.toShortDate(value);
	}
} else if(type.equals("textarea")) {
	result = EscapeUtil.escapeTextarea(value);
} else if(type.equals("hm")) {
	result = StringUtil.toHourMinute(value, "00:00");
} else if(type.equals("jsonValue")){
	if(value!=null) result = value.replace("\"", "\\\"");
} else {
	result = value;
}

if(maxLength!=null){
	result = StringUtil.cutString(result, maxLength, true);
}

if(var!=null){
	request.setAttribute(var, result);
} else {
	%><%= result %><%
}
%>