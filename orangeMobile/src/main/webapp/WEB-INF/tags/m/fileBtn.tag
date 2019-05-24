<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Locale,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.SessionUtil,
			com.innobiz.orange.web.pt.secu.Browser"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ attribute name="id"    required="true" rtexprvalue="true"
%><%@ attribute name="name" required="false" rtexprvalue="true"
%><%@ attribute name="titleId" required="false"
%><%@ attribute name="alt" required="false"
%><%@ attribute name="title" required="false"
%><%@ attribute name="style"  required="false"
%><%@ attribute name="onchange"  required="false"
%><%@ attribute name="readonly"  required="false"
%><%@ attribute name="exts"  required="false"
%><%@ attribute name="recomend"  required="false"
%><%@ attribute name="delBtn"  required="false"
%><%@ attribute name="delBtnDisplay"  required="false"
%><%@ attribute name="accept"  required="false"
%><%
/*
	onchange 스크립트의 변수 : id, value - 확장자가 다르거나 선택 취소시(IE외) value = null
*/
if(name==null) name = id;
if(onchange==null) onchange = "null";
if(style==null) style = "";

Locale locale = SessionUtil.getLocale(request);
MessageProperties properties = MessageProperties.getInstance();
if(title==null && titleId==null){
	titleId = "cm.btn.fileAtt";
}
if(titleId!=null){
	title = properties.getMessage(titleId, locale);
}

String btnId = id+"FileBtn";
String onclick = "$('#"+id+"File').trigger('click');";

String btnTitle = properties.getMessage("cm.btn.file", locale);

if(recomend!=null){
	recomend = properties.getMessage("pt.jsp.setSkinImg.recommend", locale)+" ( "+recomend+" )";
}

if(accept==null || accept.isEmpty()) accept="";
else accept=" accept=\""+accept+"\"";
%>
<input id="<%= id%>FileView" readonly="readonly" style="display:none;" />
<div class="icoarea" id="<%= id%>FileArea">
<dl>
<%
if(delBtn!=null){ 
%>
<dd class="btn" onclick="<%=delBtn%>('<%= id%>');" id="<%= id%>FileDel"><u:msg titleId="cm.btn.del" alt="삭제" /></dd>
<%} %>
<dd style="width:0px; height:0px; overflow:hidden;"><input type="file" name="<%= name%>" style="margin-left:30px" id="<%= id%>File" onchange="setFileTag('<%= id%>', this.value, <%=onchange%>,'<%= (exts==null ? "" : exts)%>');"<%=accept %>/></dd>                    
<dd class="btn" id="<%= btnId%>"><label for="<%= id%>File"><u:msg titleId="<%=titleId %>" alt="<%=title%>"  /></label></dd>
</dl>
</div>
	<%
	if(exts!=null && !exts.isEmpty()){
%>
<script type="text/javascript">
$(document).ready(function() {
	gFileTagMap['<%= id%>'] = $("#<%= id%>File")[0].outerHTML;
});
</script>
<%
	}
%>