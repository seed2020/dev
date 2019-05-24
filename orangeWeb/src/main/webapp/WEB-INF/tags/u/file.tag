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
%><%@ attribute name="mandatory"  required="false"
%><%@ attribute name="exts"  required="false"
%><%@ attribute name="recomend"  required="false"
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
	titleId = "cols."+id;
}
if(titleId!=null){
	title = properties.getMessage(titleId, locale);
}

String btnId = id+"FileBtn";
String onclick = "$('#"+id+"File').trigger('click');";

Browser browser = (Browser)request.getAttribute("browser");
String btnTitle = properties.getMessage("cm.btn.file", locale);

if(recomend!=null){
	recomend = properties.getMessage("pt.jsp.setSkinImg.recommend", locale)+" ( "+recomend+" )";
}

%>
<table cellspacing="0" cellpadding="0" border="0"><tbody><tr>
	<td width="140px"><input id="<%= id%>FileView" readonly="readonly" style="<%=style%>" /></td><%
	
	String divStyle="", tdStyle="";
	if(browser.isIe() && browser.getVer() < 9){
		divStyle = "position:absolute; margin-left:3px; width:77px; overflow:hidden; filter:alpha(opacity:0); z-index:1";
		tdStyle = (browser.getVer() == 7)
			? "position:relative; padding-top:3px;"
			: "position:relative";
	} else {
		divStyle = "width:0px; height:0px; overflow:hidden;";
	}
	
	%>
	<td width="66px" style="<%= tdStyle%>"><div style="<%= divStyle%>"><input type="file" name="<%= name%>" class="skipThese" style="height:20px; margin-left:-155px" id="<%= id%>File" onchange="setFileTag('<%= id%>FileView', this.value, <%=onchange%>,'<%= (exts==null ? "" : exts)%>');" /></div>
		<%
		if(browser.isIe() && browser.getVer() < 11){
		%><label for="<%= id%>File"><u:buttonS id="<%= btnId%>" title="<%=btnTitle%>" /></label><%
		} else {
		%><u:buttonS id="<%= btnId%>" title="<%=btnTitle%>" onclick="<%= onclick%>" /><%
		}
		%></td><%if(recomend!=null){ %>
		<td style="padding-left:6px; padding-top:4px;"><%= recomend%></td><%} %>
</tr></tbody></table><%


	if("Y".equals(mandatory) || (exts!=null && !exts.isEmpty())){
%>
<script type="text/javascript">
$(document).ready(function() {
	gFileTagMap['<%= id%>'] = $("#<%= id%>File")[0].outerHTML;
	validator.addHandler('<%= id%>File', function(id, va){
		if(isEmptyVa(va)){<% if("Y".equals(mandatory)){ %>
			alertMsg('cm.input.check.mandatory',['<%=title%>']);
			validator.focusId = "<%= id%>FileBtn";
			return false;<% } %>
		} else {<% if(exts!=null && !exts.isEmpty()){ %>
			var exts = "<%=exts%>".toLowerCase().split(',');
			va = va.toLowerCase();
			var matched = false;
			exts.each(function(index, ext){
				if(va.endsWith("."+ext.trim())){
					matched = true;
					return false;
				}
			});
			if(!matched){
				alertMsg('cm.msg.attach.not.support.ext',['<%=exts%>']);
				validator.focusId = "<%= id%>FileBtn";
				return false;
			}
			<% } %>
		}
	});
});
</script>
<%
	}
%>