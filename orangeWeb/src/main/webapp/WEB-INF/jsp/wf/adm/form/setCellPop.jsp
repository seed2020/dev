<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="java.util.Locale,
			com.innobiz.orange.web.cm.utils.MessageProperties,
			com.innobiz.orange.web.cm.utils.SessionUtil"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	Locale locale = SessionUtil.getLocale(request);
	MessageProperties properties = MessageProperties.getInstance();
	String[][] aligns = {
		{"lt", properties.getMessage("cm.aln.left", locale)},//좌측 정렬
		{"ct", properties.getMessage("cm.aln.center", locale)},//중앙 정렬
		{"rt", properties.getMessage("cm.aln.right", locale)}//우측 정렬
	};
	request.setAttribute("aligns", aligns);
	
%>
<script type="text/javascript">
<!--
<% // [하단버튼] - 확인 %>
function setCell(){
	var data = $('#setCellPopForm').find('input, select').serializeArray();
	applyCell(data);
	dialog.close('setCellDialog');
}
//-->
</script>
<div style="width:200px">
<form id="setCellPopForm">
<u:listArea>
<tr>
	<td class="head_ct"><u:msg titleId="pt.jsp.setLstSetup.lrAlign" alt="좌우 정렬"/></td>
	<td class="body_ct" ><select id="alnVa" name="alnVa"<u:elemTitle titleId="pt.jsp.setLstSetup.lrAlign" alt="좌우 정렬" /> 
	><u:option value="" titleId="cm.option.noSelect" alt="좌우정렬" selected="true"/><c:forEach
			items="${aligns}" var="align" varStatus="alignStatus">
			<u:option value="${align[0]}" title="${align[1]}" /></c:forEach></select></td>
	</tr><tr><td class="head_ct"><u:msg titleId="wf.option.headerYn" alt="헤더여부"/></td><td class="body_ct"><select id="headerYn" name="headerYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${ptPushAppDVo.useYn}" />
		<u:option value="N" titleId="cm.option.notUse" checkValue="${ptPushAppDVo.useYn}" selected="true"/>
		</select></td>
	</tr>
	
</u:listArea>

</form>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setCell();" alt="확인" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>
</div>