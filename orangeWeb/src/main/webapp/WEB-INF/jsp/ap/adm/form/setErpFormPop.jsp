<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
function saveErpForm(){
	$form = $('#setErpFormPop');
	if(validator.validate($form[0])){
		$form.attr('action','./transErpForm.do');
		$form.attr('target','dataframe');
		$form.submit();
	}
}
</script>
<div style="width:450px">
<form id="setErpFormPop">
<input type="hidden" name="menuId" value="${menuId}" /><c:if
	test="${not empty param.erpFormId}">
<u:input id="erpFormId" type="hidden" value="${param.erpFormId}" /></c:if><c:if
	test="${not empty param.erpFormTypCd}">
<u:input id="erpFormTypCd" type="hidden" value="${param.erpFormTypCd}" /></c:if>
<u:listArea>

	<tr>
	<td width="35%" class="head_lt"><u:mandatory /><u:msg titleId="ap.formCmpt.nm" alt="폼 양식 명" /></td>
	<td>
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status"
			><u:convert srcId="${apErpFormBVo.rescId}_${langTypCdVo.cd}" var="rescVa"
			/><u:set test="${status.first}" var="style" value="" elseValue="display:none"
			/><u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}"
			/>
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="pt.cols.fldNm" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelector('setErpFormPop', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setErpFormPop','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${apErpFormBVo.rescId}" />
		</td>
		</tr>
		</table>
	</td>
	</tr>
	<tr>
	<td width="35%" class="head_lt"><u:msg titleId="cols.regUrl" alt="등록 URL" /></td>
	<td><u:input id="regUrl" value="${apErpFormBVo.regUrl}" titleId="cols.regUrl" maxByte="200" style="width:94%" /></td>
	</tr>
	<tr>
	<td width="35%" class="head_lt"><u:msg titleId="cols.infmUrl" alt="통보 URL" /></td>
	<td><u:input id="infmUrl" value="${apErpFormBVo.infmUrl}" titleId="cols.infmUrl" maxByte="200" style="width:94%" /></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveErpForm();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>