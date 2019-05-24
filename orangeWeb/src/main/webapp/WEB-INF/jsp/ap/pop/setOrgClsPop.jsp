<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function saveCls(){
	if(validator.validate('setOrgClsForm')){
		var $form = $("#setOrgClsForm");
		$form.attr('action','./transOrgCls.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form.submit();
	}
}
$(document).ready(function() {
	if(browser.ie && browser.ver<9){
		$("#dataframe").attr("src","/cm/util/reloadable.do");
	}
});
//-->
</script>

<div style="width:450px">
<form id="setOrgClsForm">
<input type="hidden" name="menuId" value="${menuId}" /><c:if test="${not empty param.orgId}">
<u:input id="orgId" type="hidden" value="${param.orgId}" /></c:if><c:if test="${not empty param.clsInfoId}">
<u:input id="clsInfoId" type="hidden" value="${param.clsInfoId}" /></c:if><c:if test="${not empty param.clsInfoPid}">
<u:input id="clsInfoPid" type="hidden" value="${param.clsInfoPid}" /></c:if>
<u:listArea>

	<tr>
	<td width="30%" class="head_lt"><u:mandatory /><u:msg titleId="cols.clsNm" alt="분류명" /></td>
	<td>
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${apClsInfoDVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.clsNm" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelector('setOrgClsForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setOrgClsForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${apClsInfoDVo.rescId}" />
		</td>
		</tr>
		</table>
	</td>
	</tr>
	<tr>
	<td width="35%" class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td><select id="useYn" name="useYn"<u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${apClsInfoDVo.useYn}" />
		<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${apClsInfoDVo.useYn}" />
		</select></td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveCls();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>