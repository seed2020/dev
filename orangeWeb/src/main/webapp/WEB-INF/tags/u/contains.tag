<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="javax.servlet.jsp.tagext.JspFragment"
%><%@ attribute name="clist" required="true" type="java.lang.String"
%><%@ attribute name="key" required="true"
%><%
/*
	clist가 key를 포함하면 화면 표시한다.
	Attributes {
		clist: 콤마로 구분된 문자열 ex) AA,BB,CC
		key: 찾을 문자열 ex) CC
	}
*/

if ("".equals(clist)) return;
if ("".equals(key)) return;
boolean flag = false;

for (String str : clist.split(",")) {
	if (str.trim().equals(key)) flag = true;
}

if (flag) {
	JspFragment jspFragment = getJspBody();
	if(jspFragment!=null){
		jspFragment.invoke(out);
	}
}
%>