<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function doSubmit(){
	if(validator.validate('pltPltArea')){
		var $form = $('#pltPltArea');
		$form.attr("action","./transPltPolc.do");
		$form.attr("target","dataframe");
		$form.submit();
	}
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.setPltPolc.title" alt="포틀릿 환경 설정" menuNameFirst="true" />

<form id="pltPltArea">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.pltPolc.maxSetupCnt" alt="최대 설정 갯수" /></td>
		<td><u:input id="maxSetupCnt" name="pt.pltPolc.maxSetupCnt" titleId="pt.pltPolc.maxSetupCnt"
			value="${pltPolc.maxSetupCnt}" mandatory="Y" minInt="6" maxInt="30" valueOption="number" /></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.pltPolc.defaultWidth" alt="기본 넓이" /></td>
		<td><u:input id="defaultWidth" name="pt.pltPolc.defaultWidth" titleId="pt.pltPolc.defaultWidth"
			value="${pltPolc.defaultWidth}" mandatory="Y" minInt="200" maxInt="800" valueOption="number" /></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.pltPolc.defaultHeight" alt="기본 높이" /></td>
		<td><u:input id="defaultHeight" name="pt.pltPolc.defaultHeight" titleId="pt.pltPolc.defaultHeight"
			value="${pltPolc.defaultHeight}" mandatory="Y" minInt="200" maxInt="800" valueOption="number" /></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.pltPolc.freeSetupMovePx" alt="포틀릿 이동 단위(픽셀)" /></td>
		<td><select id="freeSetupMovePx" name="pt.pltPolc.freeSetupMovePx" <u:elemTitle titleId="pt.pltPolc.freeSetupMovePx" />>
			<c:forEach begin="1" end="10" step="1" var="no" varStatus="status">
			<u:option value="${no}" title="${no}" selected="${pltPolc.freeSetupMovePx == no}"/>
			</c:forEach>
			</select></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.pltPolc.defaultHLineTop" alt="기본 가로선 위치(픽셀)" /></td>
		<td><u:input id="defaultHLineTop" name="pt.pltPolc.defaultHLineTop" titleId="pt.pltPolc.defaultHLineTop"
			value="${pltPolc.defaultHLineTop}" minInt="200" maxInt="800" valueOption="number" /></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.pltPolc.defaultVLineLeft" alt="기본 세로선 위치(픽셀)" /></td>
		<td><u:input id="defaultVLineLeft" name="pt.pltPolc.defaultVLineLeft" titleId="pt.pltPolc.defaultVLineLeft"
			value="${pltPolc.defaultVLineLeft}" minInt="200" maxInt="800" valueOption="number" /></td>
	</tr>
</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="doSubmit()" auth="SYS" />
</u:buttonArea>