<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 저장 버튼 클릭 %>
function saveRecvGrp(){
	if(validator.validate('setRecvGrpForm')){
		var $form = $("#setRecvGrpForm");
		$form.attr('action', './transRecvGrp.do');
		$form.attr('target', 'dataframe'); 
		$form.submit();
	}
}
-->
</script>
<div style="width:400px" >

<form id="setRecvGrpForm">
<u:input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" name="bxId" value="${param.bxId}" /><c:forEach
	items="${arrMnuParam}" var="mnuParam">
<u:input type="hidden" id="${mnuParam[0]}" value="${mnuParam[1]}" /></c:forEach><c:if
	test="${not empty param.mode}">
<u:input type="hidden" name="mode" value="${param.mode}" /></c:if>
<u:listArea>

	<c:if test="${param.mode!='pub'}">
	<tr>
	<td width="30%" class="head_lt"><u:mandatory /><u:msg titleId="ap.jsp.prvRecvGrp" alt="개인수신그룹" /></td>
	<td><u:input id="recvGrpNm" value="${apRecvGrpBVo.recvGrpNm}" titleId="ap.jsp.prvRecvGrp" maxByte="120" mandatory="Y"
		onkeydown="doNotSubmit(event, saveRecvGrp)"/><u:input type="hidden" id="recvGrpId" value="${apRecvGrpBVo.recvGrpId}" /></td>
	</tr>
	</c:if>
	
	<c:if test="${param.mode=='pub'}">
	<tr>
	<td width="30%" class="head_lt"><u:mandatory /><u:msg titleId="ap.jsp.pubRecvGrp" alt="공용수신그룹" /></td>
	<td>
		<u:input type="hidden" id="recvGrpId" value="${apRecvGrpBVo.recvGrpId}" />
		<table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${apRecvGrpBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:set test="${status.first}" var="style" value="" elseValue="display:none" />
		<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
		<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="ap.jsp.pubRecvGrp" value="${rescVa}" style="${style}"
			maxByte="200" validator="changeLangSelector('setRecvGrpForm', id, va)" mandatory="Y"
			onkeydown="doNotSubmit(event, saveRecvGrp)" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setRecvGrpForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${apRecvGrpBVo.rescId}" />
		</td>
		</tr>
		</table>
	</td>
	</tr>
	</c:if>
</u:listArea>
</form>

<u:buttonArea><c:if
		test="${param.mode=='pub'}">
	<u:button titleId="cm.btn.save" href="javascript:saveRecvGrp();" alt="저장" auth="A" /></c:if><c:if
		test="${param.mode!='pub'}">
	<u:button titleId="cm.btn.save" href="javascript:saveRecvGrp();" alt="저장" auth="W" /></c:if>
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</div>