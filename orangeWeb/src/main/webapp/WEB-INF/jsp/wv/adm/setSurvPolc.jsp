<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function formSubit(){
	var $form = $('#setSurvPolcForm');
	
	$form.attr('method','post');
	$form.attr('action','./setSurvPolcSave.do?menuId=${menuId}');
	
	$form[0].submit();
	
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="wv.jsp.setSurvPolc.title" alt="설문정책" menuNameFirst="true" />
<form id="setSurvPolcForm">
<% // 목록 %>
<u:listArea id="listArea">
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.survPolc" alt="설문정책" /></td>
	<td><u:checkArea>
		<u:radio name="survPolc" value="Y" titleId="sv.option.survPolcY" alt="설문 작성 후 관리자의 승인 필요" inputClass="bodybg_lt" checked="${apvdY}" />
		</tr><tr>
		<u:radio name="survPolc" value="N" titleId="sv.option.survPolcN" alt="설문 작성 후 관리자의 승인 불필요" inputClass="bodybg_lt" checked="${apvdN}"/>
		</u:checkArea></td>
	</tr>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:formSubit()" auth="W" />
</u:buttonArea>
</form>
