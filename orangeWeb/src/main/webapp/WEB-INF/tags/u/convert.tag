<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
%><%@ attribute name="srcId" required="false"
%><%@ attribute name="var" required="false"
%><%@ attribute name="value" required="false" type="java.lang.Object"
%><%@ attribute name="scope" required="false"
%><%@ attribute name="cmt" required="false"
%><%
/*
	srcId >> var 으로 전환 라이브러리
	
*/
	Object obj = null;
	if(srcId != null){
		if("session".equals(scope)){
			obj = session.getAttribute(srcId);
		} else if("param".equals(scope)){
			obj = request.getParameter(srcId);
		} else {
			obj = request.getAttribute(srcId);
		}
	} else {
		obj = value;
	}
	
	if(obj!=null){
		if(var!=null){
			request.setAttribute(var, obj);
		} else {
			%><%= obj.toString()%><%
		}
	} else {
		if(var!=null){
			request.removeAttribute(var);
		}
	}

%>