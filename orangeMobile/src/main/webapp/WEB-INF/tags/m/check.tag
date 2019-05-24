<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Locale,
			com.innobiz.orange.web.cm.utils.EscapeUtil,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.SessionUtil,
			com.innobiz.orange.web.cm.utils.StringUtil"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ attribute name="id" required="false"
%><%@ attribute name="name" required="false"
%><%@ attribute name="value" required="true"
%><%@ attribute name="type" required="false"
%><%@ attribute name="disabled" required="false" type="java.lang.Boolean"
%><%@ attribute name="checked" required="false" type="java.lang.Boolean"
%><%@ attribute name="title" required="false"
%><%@ attribute name="titleId" required="false"
%><%@ attribute name="titleClass" required="false"
%><%@ attribute name="onclick" required="false"
%><%@ attribute name="inputId" required="false"
%><%@ attribute name="areaId" required="false"
%><%@ attribute name="extData" required="false"
%><%@ attribute name="alt" required="false"
%><%

	if(id==null) id="";
	if(name==null || name.isEmpty()) name = id;
	if(!"radio".equals(type)) type = "checkbox";
	if(onclick==null) onclick = "";

	if(titleId!=null && !titleId.isEmpty()){
		Locale locale = SessionUtil.getLocale(request);
		MessageProperties properties = MessageProperties.getInstance();
		title = properties.getMessage(titleId, locale);
	}
	
	String style = ("checkbox".equals(type) ? "check" : "radio")
			+ (disabled!=null && disabled ? "_disabled" : "")
			+ (checked!=null && checked ? "_on" : "");
	
	if(checked!=null) request.setAttribute("checked", checked);
	else request.removeAttribute("checked");
	if(disabled!=null) request.setAttribute("disabled", disabled);
	else request.removeAttribute("disabled");
	if(title!=null && !title.isEmpty()) request.setAttribute("title", title);
	else request.removeAttribute("title");
	if(titleClass==null || titleClass.isEmpty()) titleClass = "etr_body";
	
%><dd><div id="<%=id%>" class="<%= style%>" onclick="<%= onclick%>$ui.toggle(this, '${areaId}');"<c:if
	test="${disabled}"> data-disabled="Y"</c:if>><input name="<%=name%>" type="<%=type%>" style="display:none"<c:if
		test="${not empty inputId}"> id="<%= inputId%>"</c:if><c:if
		test="${checked}"> checked="checked"</c:if><c:if
		test="${disabled}"> disabled="disabled"</c:if> value="<u:out value="${value}" type="value" />" /></div><c:if
	test="${not empty title}"><span onclick="<%= onclick%>$ui.toggle($(this).prev()[0], '${areaId}');" class="<%= titleClass%>" style="line-height:30px;"><u:out value="${title}" /></span></c:if></dd>