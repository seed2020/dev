<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.cm.utils.MessageProperties"

%><%@ attribute name="srcId" required="false"
%><%@ attribute name="srcValue" required="false"
%><%@ attribute name="tgtId" required="false"
%><%@ attribute name="tgtValue" required="false"
%><%@ attribute name="var" required="false"
%><%@ attribute name="value" required="false"
%><%@ attribute name="valueRescId" required="false"
%><%@ attribute name="elseValue" required="false"
%><%@ attribute name="elseValueRescId" required="false"
%><%@ attribute name="byAdmin"    required="false" type="java.lang.Boolean"
%><%

/*
	화면 UI 처리용
	
	srcValue 와 tgtValue 를 비교하여 같은면 value 를 다르면 elseValue 를 세팅/출력 함
	
	srcId 가 있으면 srcId 로 srcValue 를 구함 - from request
	tgtId 가 있으면 tgtId 로 tgtValue 를 구함 - from request
	
	var 가 있으면 request.setAttribute 로 request 에 세팅 함
	var 가 없으면 jsp 출력함
	
	valueRescId 가 있으면 메세지 파이로 부터 value 를 세팅 함
	elseValueRescId 가 있으면 메세지 파이로 부터 elseValue 를 세팅 함
	
*/
	if(srcId!=null){
		srcValue = (String)request.getAttribute(srcId);
	}
	if(tgtId!=null){
		tgtValue = (String)request.getAttribute(tgtId);
	}
	
	boolean adminOk = true;;
	if(byAdmin!=null && Boolean.TRUE.equals(byAdmin)){
		String uri = (String)request.getAttribute("_uri");
		adminOk = uri.indexOf("/adm/") >= 0;
	}
	
	if(srcValue!=null && srcValue.equals(tgtValue) && adminOk){
		if(valueRescId!=null){
			value = MessageProperties.getInstance().getMessage(valueRescId, request);
		}
		if(var!=null){
			if(value==null) request.removeAttribute(var);
			else request.setAttribute(var, value);
		} else {
			if(value!=null) { %><%= value%><% }
		}
		
	} else {
		if(elseValueRescId!=null){
			elseValue = MessageProperties.getInstance().getMessage(elseValueRescId, request);
		}
		if(var!=null){
			if(elseValue==null) request.removeAttribute(var);
			else request.setAttribute(var, elseValue);
		} else {
			if(elseValue!=null) { %><%= elseValue%><% }
		}
	}

%>