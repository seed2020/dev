<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
		import="java.util.Map,
			com.innobiz.orange.web.pt.utils.SysSetupUtil,
			com.innobiz.orange.web.cm.utils.EscapeUtil,
			com.innobiz.orange.web.cm.utils.SessionUtil,
			com.innobiz.orange.web.cm.utils.MessageProperties"
		
%><%@ attribute name="title"    required="false"
%><%@ attribute name="titleId"    required="false"
%><%@ attribute name="termId"    required="false"
%><%@ attribute name="var"    required="false"
%><%@ attribute name="type"    required="false"
%><%@ attribute name="koDesc"    required="false"
%><%@ attribute name="alt"    required="false"
%><%

String langTypCd = SessionUtil.getLangTypCd(request);
if("ko".equals(langTypCd) && koDesc!=null && !koDesc.isEmpty()){
	title = EscapeUtil.escapeValue(koDesc);
} else {

	if(termId!=null && !termId.isEmpty()){
		int p = termId.lastIndexOf('.');
		String termVa = null;
		if(p>0){
			String setupClsId = termId.substring(0, p);
			String setupId = termId.substring(p+1);
			Map<String, String> termMap = SysSetupUtil.getTermMap(setupClsId, request);
			termVa = termMap==null ? null : termMap.get(setupId);
			if(termVa==null){
				termVa = MessageProperties.getInstance().getMessage(termId, request);
			}
		}
		title = termVa;
	
	} else if(titleId!=null && !titleId.isEmpty()){
		title = MessageProperties.getInstance().getMessage(titleId, request);
	}
	
	if(title!=null && !title.isEmpty()){
		String actNm = null;
		if("ko".equals(langTypCd)){
			String actId = type==null || "select".equals(type) ? "cm.select.actname" : 
				"change".equals(type) ? "cm.select.change" : 
				"radio".equals(type) || "checkbox".equals(type) ? "cm.radio.actname" : 
				"input".equals(type) || "textarea".equals(type) ? "cm.input.actname" : null;
			
			if(actId!=null){
				actNm = MessageProperties.getInstance().getMessage(actId, request);
			}
		}
		
		if(actNm!=null){
			title = title+" "+actNm;
		}
	}
}

if(var!=null && !var.isEmpty()){
	if(title!=null){
		request.setAttribute(var, title);
	} else {
		request.removeAttribute(var);
	}
} else if(title!=null){
	%> title="<%= title%>"<%
}

%>