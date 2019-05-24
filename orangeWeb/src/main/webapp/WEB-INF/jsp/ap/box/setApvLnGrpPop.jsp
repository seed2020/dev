<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 저장 버튼 클릭 %>
function saveApvLnGrp(){
	if(validator.validate('setApvLnGrpForm')){
		var $form = $("#setApvLnGrpForm");
		$form.attr('action', './transApvLnGrp.do');
		$form.attr('target', 'dataframe'); 
		$form.submit();
	}
}
-->
</script>
<div style="width:450px" >

<form id="setApvLnGrpForm">
<u:input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" name="bxId" value="${param.bxId}" /><c:forEach
	items="${arrMnuParam}" var="mnuParam">
<u:input type="hidden" id="${mnuParam[0]}" value="${mnuParam[1]}" /></c:forEach><c:if
	test="${not empty param.apvLnGrpTypCd}">
<u:input type="hidden" name="apvLnGrpTypCd" value="${param.apvLnGrpTypCd}" /></c:if>
<u:listArea>

	<tr>
	<td width="35%" class="head_lt"><u:mandatory /><u:msg titleId="ap.jsp.prvApvLnGrpId" alt="경로그룹ID" /></td>
	<td class="body_lt">${apApvLnGrpBVo.apvLnGrpId}</td>
	</tr>

	<c:if test="${param.apvLnGrpTypCd eq 'prv' or param.apvLnGrpTypCd eq 'prvRef'}">
	<tr>
	<td width="35%" class="head_lt"><u:mandatory /><u:msg titleId="ap.jsp.apvLnGrpNm" alt="경로그룹명" /></td>
	<td><u:input id="apvLnGrpNm" value="${apApvLnGrpBVo.apvLnGrpNm}" titleId="ap.jsp.prvApvLnGrp" maxByte="120" mandatory="Y"
		onkeydown="doNotSubmit(event, saveApvLnGrp)" /><u:input type="hidden" id="apvLnGrpId" value="${apApvLnGrpBVo.apvLnGrpId}" /></td>
	</tr>
	</c:if>
	
	<c:if test="${param.apvLnGrpTypCd eq 'pub' or param.apvLnGrpTypCd eq 'pubRef'}">
	<tr>
	<td width="35%" class="head_lt"><u:mandatory /><u:msg titleId="ap.jsp.apvLnGrpNm" alt="경로그룹명" /></td>
	<td>
		<u:input type="hidden" id="apvLnGrpId" value="${apApvLnGrpBVo.apvLnGrpId}" />
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${apApvLnGrpBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="ap.jsp.pubApvLnGrp" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelector('setApvLnGrpForm', id, va)" mandatory="Y"
			onkeydown="doNotSubmit(event, saveApvLnGrp)" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setApvLnGrpForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${apApvLnGrpBVo.rescId}" />
		</td>
		</tr>
		</table>
	</td>
	</tr>
	</c:if>
</u:listArea>
</form>

<u:buttonArea><c:if
		test="${param.apvLnGrpTypCd=='pub'}">
	<u:button titleId="cm.btn.save" href="javascript:saveApvLnGrp();" alt="저장" auth="A" /></c:if><c:if
		test="${param.apvLnGrpTypCd!='pub'}">
	<u:button titleId="cm.btn.save" href="javascript:saveApvLnGrp();" alt="저장" auth="W" /></c:if>
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</div>