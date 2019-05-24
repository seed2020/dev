<%@   tag language="java" body-content="scriptless" pageEncoding="UTF-8"
	import="com.innobiz.orange.web.cm.utils.StringUtil,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.pt.secu.Browser"
%><%@ taglib prefix="u" tagdir="/WEB-INF/tags/u"
%><%@ attribute name="id"    required="true"
%><%@ attribute name="name" required="false"
%><%@ attribute name="titleId" required="false"
%><%@ attribute name="title" required="false"
%><%@ attribute name="desc" required="false"
%><%@ attribute name="value" required="false"
%><%@ attribute name="mandatory"  required="false"
%><%@ attribute name="readonly"  required="false"
%><%@ attribute name="cell1Style"  required="false"
%><%@ attribute name="cell2Style"  required="false"
%><%

if(name==null || name.isEmpty()) name = id;
String[] values = StringUtil.splitSsn(value);
String random = StringUtil.getNextIntHexa();

MessageProperties messageProperties = MessageProperties.getInstance();
if(title==null){
	if(titleId==null) titleId = "cols."+id;
	title = messageProperties.getMessage(titleId, request);
}
String[] titles = new String[]{
		 messageProperties.getMessage("cm.input.noPosi.1", new String[]{title}, request),
		 messageProperties.getMessage("cm.input.noPosi.2", new String[]{title}, request)
};

Browser brower = (Browser)request.getAttribute("browser");
String onclick = brower.isIe() ? "" : "$('#"+id+"File').click();";

if(cell1Style==null) cell1Style = "width:40px;";
if(cell2Style==null) cell2Style = brower.isIe() || brower.isFirefox() ? "width:85px;" : "width:60px;";

%>	<table border="0" cellpadding="0" cellspacing="0">
	<tr>
	<td><u:input id="<%=id+1%>" name="<%=name+1%>" value="<%=values[0]%>"
		mandatory="<%=mandatory%>" readonly="<%=readonly%>" title="<%=titles[0]%>"
		maxLength="6" minLength="6" valueOption="number" style="<%=cell1Style%>" className="input_center"/></td>
	<td class="search_body_ct"> - </td>
	<td><u:input id="<%=id+2%>" name="<%=name+2%>" value="<%=values[1]%>" type="password"
		mandatory="<%=mandatory%>" readonly="<%=readonly%>" title="<%=titles[1]%>"
		maxLength="7" minLength="7" valueOption="number" style="<%=cell2Style%>" className="input_center"/></td>
	</tr>
	</table>
	<input type="hidden" name="connectedTypes" id="<%=random%>" value="<%=name%>"><%
	// barConnectedTypes : 서버 사이드에서 데이터 조합시 "-" 를 붙여서 조합함 
	// connectedTypes : 서버 사이드에서 데이터 조합시를 그냥 연결함 

	if(!"Y".equals(mandatory)){
%>	<script type="text/javascript">
	$(document).ready(function() {
		validator.addHandler('<%=random%>', function(id, va){
			var i, no, arr=[], hasValCnt=0;
			for(i=0;i<2;i++){
				no = $('#<%=id%>'+(i+1)).val();
				arr.push(no);
				if(no!='') hasValCnt++;
			}
			if(hasValCnt!=0 && hasValCnt!=2){
				for(i=0;i<2;i++){
					if(arr[i]==''){
						var checkedId = '<%=id%>'+(i+1);
						validator.focusId = checkedId;
						alertMsg('cm.input.check.mandatory',[validator.getTitle(checkedId)]);
						return false;
					}
				}
			}
			if(hasValCnt==2 && !checkSSN(arr[0]+arr[1])){
				var checkedId = '<%=id%>2';
				validator.focusId = checkedId;
				alertMsg('cm.input.check.notValid',['<%= title%>']);
				return false;
			}
		});
	});
	</script>
<%
	}
%>