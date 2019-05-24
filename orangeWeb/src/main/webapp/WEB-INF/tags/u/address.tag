<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="java.util.Locale,
			javax.servlet.jsp.tagext.JspFragment,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.StringUtil,
			com.innobiz.orange.web.cm.utils.SessionUtil"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"
%><%@ attribute name="id" required="true"
%><%@ attribute name="name" required="false"
%><%@ attribute name="type" required="false"
%><%@ attribute name="zipNoValue" required="false"
%><%@ attribute name="adrValue" required="false"
%><%@ attribute name="alt" required="false"
%><%@ attribute name="zipNoTitleId" required="false"
%><%@ attribute name="zipNoTitle" required="false"
%><%@ attribute name="adrTitleId" required="false"
%><%@ attribute name="adrTitle" required="false"
%><%@ attribute name="zipNoStyle" required="false"
%><%@ attribute name="adrStyle" required="false"
%><%@ attribute name="mandatory" required="false"
%><%@ attribute name="readonly"  required="false"
%><%@ attribute name="frameId"  required="false"
%><%@ attribute name="adrName" required="false"
%><%@ attribute name="zipNoName" required="false"
%><%

if(name==null) name = id;
String zipNo = zipNoValue;

if(adrValue==null) adrValue = "";

Locale locale = SessionUtil.getLocale(request);
boolean isKor = locale.getLanguage().equals("ko");
MessageProperties properties = MessageProperties.getInstance();
String actName = " "+properties.getMessage("cm.input.actname", Locale.KOREA);
if(!"view".equals(type) && zipNoTitle==null){
	if(zipNoTitleId==null) zipNoTitleId = id.isEmpty() ? "cols.zipNo" : "cols."+id+"ZipNo";
	zipNoTitle = properties.getMessage(zipNoTitleId, locale);
}
if(!"view".equals(type) && adrTitle==null){
	if(adrTitleId==null) adrTitleId = id.isEmpty() ? "cols.adr" : "cols."+id+"Adr";
	adrTitle = properties.getMessage(adrTitleId, locale);
}

String zipTitle = properties.getMessage("cm.input.noPosi.1", new String[]{zipNoTitle}, locale);
//String zip2Title = properties.getMessage("cm.input.noPosi.2", new String[]{zipNoTitle}, locale);

if(zipNoStyle==null) zipNoStyle = "ime-mode:disabled; width:80px";
if(adrStyle==null) adrStyle = "width:96%";

boolean emptyId = id==null || id.isEmpty();
boolean emptyNm = name==null || name.isEmpty();

if(mandatory==null) mandatory = "";
String btnStyle = "Y".equals(readonly) || !isKor ? "" : "";

boolean lock = false;// isKor
String readonlyZipNo = lock || "Y".equals(readonly) ? "readonly=\"readonly\"" : "";
String readonlyAdr = lock || "Y".equals(readonly) ? "Y" : "";
String readonlyDetlAdr = "Y".equals(readonly) ? "Y" : "";

if("view".equals(type)){
	if(zipNoValue!=null && !zipNoValue.isEmpty()){
		%>[<%= zipNoValue%>] <%
	}
	%><%= adrValue%><%
} else {
%>
<c:set var="frameId" value="<%=frameId %>"/>
<table border="0" cellpadding="0" cellspacing="0" style="width:100%">
	<tbody>
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td>
						<input id='<%= emptyId  ? "zipNo" : (id+"ZipNo")%>' name='<%= zipNoName != null ? zipNoName : emptyNm  ? "zipNo" : (name+"ZipNo")%>' value="<%=zipNo%>" <%= readonlyZipNo%>
						title="<%= (isKor ? zipTitle+actName : zipTitle)%>" maxlength="5" style="<%=zipNoStyle%>" class="input_center"/>
					</td>
					<%
						if(isKor){							
					%>
					<td style="padding-left:2px"><u:buttonS id='<%= id+"ZipBtn"%>' onclick="findZipCodePopup(this.id,'${menuId}','${frameId }');" alt="우편번호 검색" titleId="cm.btn.zipNo" style="<%= btnStyle%>" popYn="Y" /></td>
					<%} %>
					<td style="padding-left:2px"><u:buttonS id='<%= id+"ZipBtn"%>' onclick="setZipCodePopup(this.id,'${menuId}', '${frameId }');" alt="직접입력" titleId="cm.btn.zipNoMng" style="<%= btnStyle%>" popYn="Y" /></td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<u:input id='<%= emptyId ? "adr" : (id+"Adr")%>' name='<%= adrName != null ? adrName : emptyNm ? "adr" : (name+"Adr")%>'
			style="<%= adrStyle%>" readonly="<%= readonlyAdr%>" value="<%= adrValue%>" title="<%= (isKor ? adrTitle+actName : adrTitle)%>"/>
		</td>
	</tr>
	</tbody>
</table>

<%
	if(!"Y".equals(readonly) && "Y".equals(mandatory)){
%>
<script type="text/javascript">
$(document).ready(function() {
	validator.addHandler('<%= emptyId  ? "zipNo" : (id+"ZipNo")%>', function(id, va){
		if(isEmptyVa(va)){
			alertMsg('cm.calendar.check.mandatory',['<%=zipNoTitle%>']);
			validator.focusId = "<%= id%>ZipBtn";
			return false;
		}
	});
	validator.addHandler('<%= emptyId ? "adr" : (id+"Adr")%>', function(id, va){
		if(isEmptyVa(va)){
			alertMsg('cm.calendar.check.mandatory',['<%=zipNoTitle%>']);
			validator.focusId = "<%= id%>ZipBtn";
			return false;
		}
	});
});
</script>
<%
	}
}
%>