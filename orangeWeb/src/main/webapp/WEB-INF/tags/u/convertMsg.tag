<%@	tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Locale, com.innobiz.orange.web.cm.utils.MessageProperties"
%><%@ attribute name="srcId" required="true"
%><%@ attribute name="var" required="false"
%><%@ attribute name="msgId" required="true"
%><%@ attribute name="langTypCd" required="true"
%><%
/*
	srcId 로 request 에서 값을 가져와서 없으면
		msgId 로 해당 언어의 Message 를 가져옴
	var 의 값에 해당 값을 세팅함
*/
	String value = (String)request.getAttribute(srcId);
	if(value==null){
		try{
			Locale locale = "ko".equals(langTypCd) ? Locale.KOREA :
				"en".equals(langTypCd) ? Locale.US :
					"ja".equals(langTypCd) ? Locale.JAPAN :
						"zh".equals(langTypCd) ? Locale.CHINA :
							new Locale(langTypCd);
			
			value = MessageProperties.getInstance().getMessage(msgId, locale);
			request.setAttribute(var, value);
			
		} catch(Exception e){
			e.printStackTrace();
			request.setAttribute(var, "");
		}
	} else {
		request.setAttribute(var, value);
	}

%>