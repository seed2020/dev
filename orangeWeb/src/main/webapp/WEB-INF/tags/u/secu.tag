<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		javax.servlet.jsp.tagext.JspFragment,
		com.innobiz.orange.web.pt.secu.SecuUtil"
%><%@ attribute name="auth" required="true"
%><%@ attribute name="ownerUid" required="false"
%><%

if(SecuUtil.hasAuth(request, auth, ownerUid)){
	JspFragment jspFragment = getJspBody();
	if(jspFragment!=null){
		jspFragment.invoke(out);
	}
}

%>