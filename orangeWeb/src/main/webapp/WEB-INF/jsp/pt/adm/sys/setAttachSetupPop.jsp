<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// [버튼] 저장 %>
function saveAttSetup(){
	var $form = $('#setAttachSetupForm');
	$form.attr("action","./transAttachSetup.do");
	$form.attr("target","dataframe");
	$form.submit();
}
$(document).ready(function() {
});
//-->
</script>
<div style="width:300px;">
<form id="setAttachSetupForm">
<input type="hidden" name="menuId" value="${menuId}" />
<u:title titleId="cm.btn.download" alt="다운로드" type="small" />
<u:listArea id="setAttachSetupArea" colgroup="35%," >
<tr>
	<td width="30%" class="head_lt">WEB</td>
	<td><u:convertMap srcId="attcSetupMap" attId="webDownYn" var="webDownYn" 
		/><select id="webDownYn" name="${attcSetupPrefix}.webDownYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${webDownYn}" selected="${empty webDownYn }"/>
		<u:option value="N" titleId="cm.option.notUse" checkValue="${webDownYn}" />
		</select>
	</td>
</tr>
<tr>
	<td width="30%" class="head_lt">MOBILE</td>
	<td><u:convertMap srcId="attcSetupMap" attId="mobDownYn" var="mobDownYn" 
		/><select id="mobDownYn" name="${attcSetupPrefix}.mobDownYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${mobDownYn}" selected="${empty mobDownYn }"/>
		<u:option value="N" titleId="cm.option.notUse" checkValue="${mobDownYn}" />
		</select>
	</td>
</tr>
</u:listArea>
<c:if test="${viewerWebEnable == true || viewerMobEnable == true}">
<u:blank />
<u:title titleId="pt.docViewer.title" alt="문서뷰어" type="small" />
<u:listArea id="setAttachSetupArea" colgroup="35%," >
<c:if test="${viewerWebEnable == true}">
<tr>
	<td width="30%" class="head_lt">WEB</td>
	<td><u:convertMap srcId="attcSetupMap" attId="webViewYn" var="webViewYn" 
		/><select id="webViewYn" name="${attcSetupPrefix}.webViewYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${webViewYn}" selected="${empty webViewYn }"/>
		<u:option value="N" titleId="cm.option.notUse" checkValue="${webViewYn}" />
		</select>
	</td>
</tr>
</c:if>
<c:if test="${viewerMobEnable == true}">
<tr>
	<td width="30%" class="head_lt">MOBILE</td>
	<td><u:convertMap srcId="attcSetupMap" attId="mobViewYn" var="mobViewYn" 
		/><select id="mobViewYn" name="${attcSetupPrefix}.mobViewYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${mobViewYn}" selected="${empty mobViewYn }"/>
		<u:option value="N" titleId="cm.option.notUse" checkValue="${mobViewYn}" />
		</select>
	</td>
</tr>
</c:if>
</u:listArea>
</c:if>
</form>	
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveAttSetup()" auth="A" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>