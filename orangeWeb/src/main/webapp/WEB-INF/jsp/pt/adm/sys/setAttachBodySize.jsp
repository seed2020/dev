<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function setAttachSetupPop(){
	dialog.open('setAttachSetupDialog', '<u:msg titleId="cm.btn.set.attfile" alt="첨부파일설정" />', './setAttachSetupPop.do?menuId=${menuId}');
}
function doSubmit(){
	var $form = $('#pageRecCntArea');
	$form.attr("action","./transAttachBodySize.do");
	$form.attr("target","dataframe");
	$form.submit();
}
<%// 첫번째 콤보가 변경 될때 %>
function changeVa(va, prefix){
	$("#pageRecCntArea select[name^='"+prefix+"']").val(va);
	$.uniform.update("#pageRecCntArea select");
}

$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title title="본문/첨부 용량 설정" menuNameFirst="true" />

<form id="pageRecCntArea">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea>
	<tr><td width="18%" class="head_lt"></td>
		<td width="18%" class="head_lt"><u:msg alt="본문 용량" titleId="pt.jsp.setAttachBodySize.bodySize" /></td>
		<td class="head_lt"><u:msg alt="첨부 용량" titleId="pt.jsp.setAttachBodySize.attacSize" /></td>

	</tr>
	<c:forEach items="${pageRecSetupCdList}" var="pageRecSetupCd" varStatus="status">
	<tr><u:set
		test="${pageRecSetupCd.cd == '_ALL'}" var="onchange" value="changeVa(this.value,'${bodyPrefix}')" elseValue="" />
		<td width="18%" class="head_lt">${pageRecSetupCd.rescNm}</td>
		<td ><select name="${bodyPrefix}.${pageRecSetupCd.cd}" onchange="${onchange}" <u:elemTitle title="${pageRecSetupCd.rescNm}" />>
			<c:forEach items="${bodySizes}" var="bodySize" varStatus="status2">
			<u:convertMap srcId="bodySizeMap" attId="${pageRecSetupCd.cd}" var="storedSize" />
			<u:option value="${bodySize}" title="${bodySize}" selected="${storedSize == bodySize}"/>
			</c:forEach>
			</select> &nbsp; K-Bytes</td><u:set
		test="${pageRecSetupCd.cd == '_ALL'}" var="onchange" value="changeVa(this.value,'${attachPrefix}')" elseValue="" />
		<td ><select name="${attachPrefix}.${pageRecSetupCd.cd}" onchange="${onchange}" <u:elemTitle title="${pageRecSetupCd.rescNm}" />>
			<c:forEach items="${attachSizes}" var="attachSize" varStatus="status2">
			<u:convertMap srcId="attachSizeMap" attId="${pageRecSetupCd.cd}" var="storedSize" />
			<u:option value="${attachSize}" title="${attachSize}" selected="${storedSize == attachSize}"/>
			</c:forEach>
			</select> &nbsp; M-Bytes</td>
	</tr>
	</c:forEach>

</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.set.attfile" alt="첨부파일설정" onclick="setAttachSetupPop()" auth="A" />
	<u:button titleId="cm.btn.save" alt="저장" onclick="doSubmit()" auth="A" />
</u:buttonArea>