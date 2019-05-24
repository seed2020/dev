<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
%><%@ attribute name="test" required="true" type="java.lang.Boolean"
%><%@ attribute name="var" required="false"
%><%@ attribute name="value" required="false"
%><%@ attribute name="elseValue" required="false"
%><%@ attribute name="cmt" required="false"
%><%@ attribute name="func" required="false"
%><%
/*
	test 가 참이면
		var attribute 에 value 를 세팅
	test 가 거짓이면
		var attribute 에 elseValue 를 세팅 함
	
	cmt: 주석용
*/
	if(!test) value = elseValue;
	if(func!=null && value!=null && !value.isEmpty()){
		if(func.equals("round.1")){
			double doubleValue = Double.parseDouble(value);
			doubleValue = Math.round(doubleValue * 10.0 ) / 10.0;
			value = Double.toString(doubleValue);
		}
	}
	
	if(value==null){
		if(var!=null){
			request.removeAttribute(var);
		}
	} else {
		if(var!=null){
			request.setAttribute(var, value);
		} else {
			%><%= value%><%
		}
	}
%>