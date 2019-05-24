<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="com.innobiz.orange.web.cm.config.ServerConfig"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 추가 - 소버튼 %>
function addInput(lang){
	var $area = $("#"+lang+"Area");
	$area.append("<br/><input name=\""+lang+"\" value=\"\" maxlength=\"30\" style=\"width:94%\" />\n");
	$area.find("input:last").uniform();
}
function saveFonts(){
	var $form = $("#fontListForm");
	$form.attr("action","./transFontByLang.do");
	$form.attr("target","dataframe");
	$form.submit();
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title title="어권별 폰트 설정" menuNameFirst="true" />

<form id="fontListForm">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea >
	<tr><c:forEach items="${langTypCdList}" var="langTypCd" varStatus="langStatus">
		<td class="head_ct" >${langTypCd.rescNm}</td></c:forEach>
	</tr>
	<tr><c:forEach items="${langTypCdList}" var="langTypCd" varStatus="langStatus">
		<td id="${langTypCd.cd}Area" valign="top"><u:convert srcId="${langTypCd.cd}" var="fonts" />
		<c:forEach items="${fonts}" var="font" varStatus="status">
		<input name="${langTypCd.cd}" value="<u:out value="${font}" type="value" />" maxlength="30" style="width:94%" /><br/></c:forEach>
		<input name="${langTypCd.cd}" value="" maxlength="30" style="width:94%" />
		</td></c:forEach>
	</tr>
	<tr><c:forEach items="${langTypCdList}" var="langTypCd" varStatus="langStatus">
		<td align="right" style="padding:2px;"><u:buttonS titleId="cm.btn.add" alt="추가" onclick="addInput('${langTypCd.cd}')" /></td></c:forEach>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveFonts()" auth="SYS" />
</u:buttonArea>
</form>