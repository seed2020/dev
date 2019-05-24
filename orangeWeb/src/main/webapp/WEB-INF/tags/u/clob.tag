<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
%><%@ attribute name="lobHandler" required="true" type="com.innobiz.orange.web.cm.dao.LobHandler"
%><%
	if(lobHandler != null){
		try{
			lobHandler.writeText(out, true);
		}catch(NullPointerException npe){}
	}
%>