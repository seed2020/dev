<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function formSubmit(){
	
	var $form=$("#setEstbPolcForm");
	
	$form.attr('method','post');
	$form.attr('action','./transSetEstbPolcSave.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
	
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ct.jsp.setEstbPolc.title" alt="커뮤니티 개설정책" menuNameFirst="true" />

<form id="setEstbPolcForm">
<% // 목록 %>
<u:listArea id="listArea">
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.estbPolc" alt="개설정책" /></td>
	<td><u:checkArea>
		<u:radio name="estbPolc" value="Y" titleId="ct.option.estbPolcY" inputClass="bodybg_lt" checked="${apvdY}" />
		</tr><tr>
		<u:radio name="estbPolc" value="N" titleId="ct.option.estbPolcN" inputClass="bodybg_lt" checked="${apvdN}"/>
		</u:checkArea></td>
	</tr>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:formSubmit()" auth="W" />
</u:buttonArea>
</form>
