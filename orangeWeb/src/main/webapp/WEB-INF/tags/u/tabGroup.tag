<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		javax.servlet.jsp.tagext.JspFragment,
		com.innobiz.orange.web.pt.secu.LoginSession,
		com.innobiz.orange.web.cm.utils.MessageProperties,
		com.innobiz.orange.web.cm.utils.StringUtil"

%><%@ attribute name="id"  required="false"
%><%@ attribute name="style"  required="false"
%><%@ attribute name="setBtnClick"  required="false" 
%><%@ attribute name="setBtnClickFirst"  required="false"   type="java.lang.Boolean"
%><%@ attribute name="noBottomBlank" required="false" type="java.lang.Boolean"
%><%
/*

id		: 텝의 ID - 차후 텝의 추가 삭제 등 스크립트에 활용

참고
----------
<u:tabGroup></u:tabGroup> 사이에 tab 테그를 넣을 수 있으며 tab 테그를 넣을때 auth 값에 의해 권한 관리 할 수 있음

*/

if(id==null){
	id = StringUtil.getNextIntHexa();
	request.setAttribute("tabId", id);
}
style = style==null ? "" : " style=\""+style+"\"";
if(style==null) style = "";
request.setAttribute("tabNo-"+id, new Integer(0));

%>	<div id="<%= id%>" class="tab_basic"<%=style%>>
	<div class="tab_left">
	<ul><%

	JspFragment jspFragment = getJspBody();
	if(jspFragment!=null){
		jspFragment.invoke(out);
		
		String tabSubType = (String)request.getAttribute("tabSubType");
		if(tabSubType!=null && !tabSubType.equals("tab")){
			request.removeAttribute("tabSubType");
			// sub-tag : titleButton, titleIcon, titleText 에서 열어 놓은 li 테그를 닫음
			out.println("\r\n		</li>");
		}
	}
	
	%>
	</ul>
	</div>
	<%
		if(setBtnClick!=null) 
		{
			setBtnClick = " onclick=\""+setBtnClick+"\"";
	%>	
	<div id="divTabGroupBtn" class="tab_right" <%if(!Boolean.TRUE.equals(setBtnClickFirst)) out.print("style=\"display:none\"");%>>
		<a href="javascript:void(0);"<%=setBtnClick %>><img src="${_cxPth}/images/${_skin}/icoptl_setting.png" width="20" height="20" /></a>
	</div>
	<%
		}
	%>
	</div><%
	
	if(!Boolean.TRUE.equals(noBottomBlank)){
		%>
	<div class="blank"></div><%
	}
	%>