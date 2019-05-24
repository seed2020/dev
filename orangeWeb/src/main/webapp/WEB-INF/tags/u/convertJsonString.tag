<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.cm.utils.JsonUtil"
%><%@ attribute name="var" required="true"
%><%@ attribute name="value" required="false" type="java.util.Map"
%><%
/*
	srcId >> var 으로 전환 라이브러리
	
*/
	Object obj=null;
	if(value!=null && !value.isEmpty()){
		obj = JsonUtil.toJson(value);
	}
	
	if(obj == null){
		if(var!=null){
			request.removeAttribute(var);
		}
	} else{
		request.setAttribute(var, obj);
	}
%>