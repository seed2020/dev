<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Map,
			com.innobiz.orange.web.cm.utils.EscapeUtil,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.StringUtil,
			com.innobiz.orange.web.pt.utils.SysSetupUtil,
			com.innobiz.orange.web.pt.secu.SecuUtil"

%><%@ attribute name="id"  required="false"
%><%@ attribute name="title"  required="false"
%><%@ attribute name="titleId"  required="false"
%><%@ attribute name="termId" required="false"
%><%@ attribute name="areaId"  required="false"
%><%@ attribute name="onclick"  required="false"
%><%@ attribute name="on"  required="false" type="java.lang.Boolean"
%><%@ attribute name="onAreaId"  required="false"
%><%@ attribute name="auth"  required="false"
%><%@ attribute name="ownerUid" required="false"
%><%@ attribute name="style" required="false"
%><%@ attribute name="mobAStyle" required="false"
%><%@ attribute name="alt"  required="false"

%><%
/*

id		: 텝의 ID - 차후 텝의 추가 삭제 등 스크립트에 활용
on		: 활성화 여부 Y/N

*/
if(SecuUtil.hasAuth(request, auth, ownerUid)) {
	
	if(id==null){
		id = (String)request.getAttribute("tabId");
	}
	request.setAttribute("tabId", id);
	if(onAreaId!=null && onAreaId.equals(areaId)) on = Boolean.TRUE;
	String className = "basic";
	if(on!=null && on){
		className = "basic_open";
	}
	
	Integer idx = (Integer)request.getAttribute("tabNo-"+id);
	if(idx==null) idx = new Integer(0);
	
	request.setAttribute("tabNo-"+id, new Integer(idx+1));
	if(onclick==null) onclick = "";
	else {
		onclick = onclick.trim();
		if(!onclick.isEmpty() && !onclick.endsWith(";")){
			onclick = onclick+";";
		}
	}
	
	if(termId!=null && !termId.isEmpty()){
		int p = termId.lastIndexOf('.');
		String termVa = null;
		if(p>0){
			if(termId.indexOf(".term")>0){
				String setupClsId = termId.substring(0, p);
				String setupId = termId.substring(p+1);
				Map<String, String> termMap = SysSetupUtil.getTermMap(setupClsId, request);
				termVa = termMap==null ? null : termMap.get(setupId);
			}
			
			if(termVa==null){
				termVa = MessageProperties.getInstance().getMessage(termId, request);
			}
		}
		title = termVa;
	} else if(titleId!=null && !titleId.isEmpty()){
		title = MessageProperties.getInstance().getMessage(titleId, request);
	}
	
	request.setAttribute("tabSubType", "tab");
	style = (style==null) ? "" : " style=\""+style+"\"";
	
	mobAStyle = (mobAStyle==null) ? "" : " style=\""+mobAStyle+"\"";
%><li class="<%= className%>"<%= style%> <%
		if(!"NO_AREA".equals(areaId)){%>data-areaId="<%= areaId%>"<%}
	%>><a href="javascript:changeTab('<%= id%>',<%= idx%>);<%= onclick%>"<%= mobAStyle%>><span><%= title%></span></a></li><%
}
%>