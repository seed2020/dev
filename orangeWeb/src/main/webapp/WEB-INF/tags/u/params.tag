<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="javax.servlet.jsp.tagext.JspFragment,
			com.innobiz.orange.web.cm.utils.ParamUtil"
%><%@ attribute name="excludes" required="false"
%><%@ attribute name="var" required="false"
%><%
/*
	파라미터(QueryString)을 제어한다.
*/
String[] excludeList = null;
if (excludes != null) {
	excludeList = excludes.split(",");
}
String queryString = ParamUtil.getQueryString(request, excludeList);

if (var!=null) {
	request.setAttribute(var, queryString);
} else {
	%><%= queryString%><%
}
%>