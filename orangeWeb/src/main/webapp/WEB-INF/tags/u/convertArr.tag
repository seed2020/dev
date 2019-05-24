<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
%><%@ attribute name="srcId" required="true"
%><%@ attribute name="var" required="false"
%><%@ attribute name="index" type="java.lang.Integer" required="true"
%><%@ attribute name="scope" required="false"
%><%
/*
	srcId >> var 으로 전환 라이브러리
	
*/

	Object obj = null;
	if("session".equals(scope)){
		obj = session.getAttribute(srcId);
	} else {
		obj = request.getAttribute(srcId);
	}
	
	if(obj == null){
		if(var!=null){
			request.removeAttribute(var);
		}
	} else if(obj instanceof Object[]){
		Object[] array = (Object[])obj;
		if(array.length > index){
			if(var!=null){
				request.setAttribute(var, array[index]);
			} else {
				%><%= array[index].toString()%><%
			}
		} else {
			if(var!=null){
				request.removeAttribute(var);
			}
		}
	} else if(obj instanceof java.util.List){
		java.util.List list = (java.util.List)obj;
		if(list.size() > index){
			if(var!=null){
				request.setAttribute(var, list.get(index));
			} else {
				%><%= list.get(index).toString()%><%
			}
		} else {
			if(var!=null){
				request.removeAttribute(var);
			}
		}
	}
%>