<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		java.util.Map,
		com.innobiz.orange.web.cm.utils.EscapeUtil,
		com.innobiz.orange.web.cm.utils.StringUtil,
		com.innobiz.orange.web.cm.utils.Hash"
%><%@ attribute name="srcId" required="true"
%><%@ attribute name="attId" required="true"
%><%@ attribute name="var" required="false"
%><%@ attribute name="scope" required="false"
%><%@ attribute name="altValue" required="false"
%><%@ attribute name="nullValue" required="false"
%><%@ attribute name="type" required="false"
%><%@ attribute name="hash" required="false" type="java.lang.Boolean"
%><%@ attribute name="intKey" required="false" type="java.lang.Boolean"
%><%
/*
	srcId >> var 으로 전환 라이브러리
	
*/
	Map map = null;
	Object obj = null;
	if("session".equals(scope)){
		map = (Map)session.getAttribute(srcId);
	} else {
		map = (Map)request.getAttribute(srcId);
	}
	Object key = Boolean.TRUE.equals(intKey) ? Integer.valueOf(attId) :
			Boolean.TRUE.equals(hash) ? Integer.valueOf(Hash.hashId(attId)) : 
				attId;
	if(map != null) obj = map.get(key);
	if(altValue!=null && obj==null) obj = altValue;
	
	if(obj!=null){
		if(var!=null){
			request.setAttribute(var, obj);
		} else {
			String value = obj.toString();
			if(type==null){
				%><%= value%><%
			} else if(type.equals("html")) {
				%><%= nullValue==null ? EscapeUtil.escapeHTML(value) : EscapeUtil.escapeHTML(value, nullValue) %><%
			} else if(type.equals("value")) {
				%><%= EscapeUtil.escapeValue(value) %><%
			} else if(type.equals("script")) {
				%><%= EscapeUtil.escapeScript(value) %><%
			} else if(type.equals("number")) {
				%><%= StringUtil.toNumber(value) %><%
			} else if(type.equals("date")) {
				%><%= StringUtil.toShortDate(value) %><%
			} else if(type.equals("longdate")) {
				%><%= StringUtil.toLongDate(value) %><%
			}
		}
	} else {
		if(var!=null){
			request.removeAttribute(var);
		}
	}

%>