<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="
		com.innobiz.orange.web.cm.utils.StringUtil,
		com.innobiz.orange.web.cm.utils.MessageProperties,
		com.innobiz.orange.web.pt.utils.SysSetupUtil"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ attribute name="id"    required="true"
%><%@ attribute name="name" required="false"
%><%@ attribute name="titleId" required="false"
%><%@ attribute name="title" required="false"
%><%@ attribute name="desc" required="false"
%><%@ attribute name="value" required="false"
%><%@ attribute name="type" required="false"
%><%@ attribute name="mandatory"  required="false"
%><%@ attribute name="readonly"  required="false"
%><%@ attribute name="cellStyle"  required="false"
%><%

boolean exPhoneEnable = "Y".equals(SysSetupUtil.getSysPlocMap().get("exPhoneEnable"));
if(name==null || name.isEmpty()) name = id;


String[] values = exPhoneEnable ? null : StringUtil.splitPhone(value);
String random = StringUtil.getNextIntHexa();

if(exPhoneEnable){
	if("view".equals(type)){
		%><%= value%><%
	} else {
		
%><u:input id="<%=id%>" name="<%=name%>" value="<%=value%>"
		mandatory="<%=mandatory%>" readonly="<%=readonly%>" title="<%=title%>" titleId="<%=titleId%>"
		maxByte="20" className="input_center"/><%
		
	}
} else if("view".equals(type)){
	if(!values[0].isEmpty() && !values[1].isEmpty()){
		%><%= values[0]%>-<%= values[1]%>-<%= values[2]%><%
	}
} else {
	MessageProperties messageProperties = MessageProperties.getInstance();
	if(title==null){
		if(titleId==null || titleId.isEmpty()){
			titleId = "cols."+id;
		}
		title = messageProperties.getMessage(titleId, request);
	}
	
	String[] titles = new String[]{
			 messageProperties.getMessage("cm.input.noPosi.1", new String[]{title}, request),
			 messageProperties.getMessage("cm.input.noPosi.2", new String[]{title}, request),
			 messageProperties.getMessage("cm.input.noPosi.3", new String[]{title}, request)
	};
	
	
	if(cellStyle==null) cellStyle = "width:30px;";

%>	<table border="0" cellpadding="0" cellspacing="0">
	<tr>
	<td><u:input id="<%=id+1%>" name="<%=name+1%>" value="<%=values[0]%>"
		mandatory="<%=mandatory%>" readonly="<%=readonly%>" title="<%=titles[0]%>"
		maxLength="4" minLength="2" valueOption="number" style="<%=cellStyle%>" className="input_center"/></td>
	<td class="search_body_ct"> - </td>
	<td><u:input id="<%=id+2%>" name="<%=name+2%>" value="<%=values[1]%>"
		mandatory="<%=mandatory%>" readonly="<%=readonly%>" title="<%=titles[1]%>"
		maxLength="4" minLength="3" valueOption="number" style="<%=cellStyle%>" className="input_center"/></td>
	<td class="search_body_ct"> - </td>
	<td><u:input id="<%=id+3%>" name="<%=name+3%>" value="<%=values[2]%>"
		mandatory="<%=mandatory%>" readonly="<%=readonly%>" title="<%=titles[2]%>"
		maxLength="4" minLength="4" valueOption="number" style="<%=cellStyle%>" className="input_center"/></td>
	</tr>
	</table>
	<input type="hidden" name="barConnectedTypes" id="<%=random%>" value="<%=name%>"><%
	// barConnectedTypes : 서버 사이드에서 데이터 조합시 "-" 를 붙여서 조합함 
	// connectedTypes : 서버 사이드에서 데이터 조합시를 그냥 연결함 

	if(!"Y".equals(mandatory)){
%>	<script type="text/javascript">
	$(document).ready(function() {
		validator.addHandler('<%=random%>', function(id, va){
			var i, no, arr=[], hasValCnt=0;
			for(i=0;i<3;i++){
				no = $('#<%=id%>'+(i+1)).val();
				arr.push(no);
				if(no!='') hasValCnt++;
			}
			if(hasValCnt!=0 && hasValCnt!=3){
				for(i=0;i<3;i++){
					if(arr[i]==''){
						var checkedId = '<%=id%>'+(i+1);
						validator.focusId = checkedId;
						alertMsg('cm.input.check.mandatory',[validator.getTitle(checkedId)]);
						return false;
					}
				}
			}
			if(arr[0]!='' && arr[0].charAt(0)!='0'){
				var checkedId = '<%=id%>1';
				validator.focusId = checkedId;
				alertMsg('cm.input.phone.1.startsWith',[validator.getTitle(checkedId)]);
				return false;
			}
		});
	});
	</script>
<%
	}
}
%>