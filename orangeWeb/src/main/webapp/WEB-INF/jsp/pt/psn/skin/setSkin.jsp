<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function doSubmit(){
	var $form = $('#skinForm');
	$form.attr("action","./transSkinSetup.do");
	$form.attr("target","dataframe");
	$form.submit();
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.setSkin.title" alt="스킨 설정" menuNameFirst="true" />

<form id="skinForm">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea>
	<c:if test="${layout.icoLoutUseYn=='Y'}">
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.setSkin.iconLout" alt="아이콘 레이아웃" /></td>
		<td class="body_lt">
			<u:checkArea>
				<u:radio value="I_blue" name="skinSetup" checkValue="${skinSetup}" title="blue" labelStyle="color:#1c7cff; font-weight:bold;"/>
				<u:radio value="I_green" name="skinSetup" checkValue="${skinSetup}" title="green" labelStyle="color:#1e9828; font-weight:bold;"/>
				<u:radio value="I_pink" name="skinSetup" checkValue="${skinSetup}" title="pink" labelStyle="color:#dc485e; font-weight:bold;"/>
				<u:radio value="I_yellow" name="skinSetup" checkValue="${skinSetup}" title="yellow" labelStyle="color:#ffc555; font-weight:bold;"/>
			</u:checkArea>
		</td>
	</tr></c:if>
	<c:if test="${layout.bascLoutUseYn=='Y'}">
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.setSkin.bascLout" alt="기본 레이아웃" /></td>
		<td class="body_lt">
			<u:checkArea>
				<u:radio value="B_blue" name="skinSetup" checkValue="${skinSetup}" title="blue" labelStyle="color:#1c7cff; font-weight:bold;"/>
				<u:radio value="B_green" name="skinSetup" checkValue="${skinSetup}" title="green" labelStyle="color:#1e9828; font-weight:bold;"/>
				<u:radio value="B_pink" name="skinSetup" checkValue="${skinSetup}" title="pink" labelStyle="color:#dc485e; font-weight:bold;"/>
				<u:radio value="B_yellow" name="skinSetup" checkValue="${skinSetup}" title="yellow" labelStyle="color:#ffc555; font-weight:bold;"/>
			</u:checkArea>
		</td>
	</tr></c:if>
</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="doSubmit()" auth="W" />
</u:buttonArea>
