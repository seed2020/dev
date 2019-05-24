<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript" >
<!--
<%// 계열사 %>
function setAffiliatesPop(compId){
	dialog.open('setAffiliatesDialog','<u:msg titleId="pt.jsp.affiliates" alt="계열사"/>','./setAffiliatesPop.do?menuId=${menuId}&compId='+compId);
}
function chgEmailBulk(){
	callAjax('./transEmailBulkAjx.do?menuId=${menuId}', {compId:'${param.compId}'}, function(data){
		if(data.message!=null){
			alert(data.message);
		}
	});
}
//-->
</script>
<div style="width:550px">
<form id="setCompPop">
<input type="hidden" name="menuId" value="${menuId}" />
<u:listArea>

	<tr>
	<td width="18%" class="head_lt"><u:mandatory /><u:msg titleId="cols.compNm" alt="회사명" /></td>
	<td>
		<c:forEach items="${langTypCdList}" var="langTypCdVo" varStatus="status">
		<u:convert srcId="${ptCompBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
		<u:decode srcValue="${langTypCdVo.cd}" tgtId="lang_${langTypCdVo.cd}" var="style" value="" elseValue="display:none" />
		<div id="lang_${langTypCdVo.cd}" style="${style}"><u:input titleId="cols.compNm" id="rescVa_${langTypCdVo.cd}" value="${rescVa}" maxByte="60" /> <u:out value="${langTypCdVo.rescNm}" /></div>
		</c:forEach>
		<u:input type="hidden" id="rescId" value="${ptCompBVo.rescId}" />
	</td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.compId" alt="회사ID" /></td>
	<td<c:if test="${not empty ptCompBVo.compId}"> class="body_lt"</c:if>>
		<c:if test="${not empty ptCompBVo.compId}">
			${ptCompBVo.compId}<u:input name="compId" type="hidden" value="${ptCompBVo.compId}" />
		</c:if>
	</td>
	</tr>

	<tr>
	<td width="18%" class="head_lt"><u:mandatory /><u:msg titleId="em.mailDomain" alt="메일 도메인" /></td>
	<td><u:input id="mailDomain" value="${ptCompBVo.mailDomain}" titleId="em.mailDomain" maxByte="50" mandatory="Y" /></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.rcd" alt="참조코드" /></td>
	<td><u:input id="rcd" value="${ptCompBVo.rcd}" titleId="cols.rcd" maxByte="30" /></td>
	</tr>

	<c:if
		test="${empty licenseByCompEnable}">
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td colspan="3"><select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${ptCompBVo.useYn}" />
		<u:option value="N" titleId="cm.option.notUse" checkValue="${ptCompBVo.useYn}" />
		</select></td>
	</tr></c:if><c:if
		test="${not empty licenseByCompEnable}">
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td><select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
		<u:option value="Y" titleId="cm.option.use" checkValue="${ptCompBVo.useYn}" />
		<u:option value="N" titleId="cm.option.notUse" checkValue="${ptCompBVo.useYn}" />
		</select></td>
	<td width="18%" class="head_lt"><u:mandatory /><u:msg titleId="pt.label.licCnt" alt="라이선스 수" /></td>
	<td><u:input id="licCnt" value="${ptCompBVo.licCnt}" titleId="pt.label.licCnt" valueOption="number" mandatory="Y" maxInt="10000" /></td>
	</tr>
	</c:if>
	
	<tr>
	<td width="18%" class="head_lt"><u:mandatory /><u:msg titleId="pt.cols.useLang" alt="사용언어" /></td>
	<td class="bodybg_lt" colspan="3">
		<u:checkArea>
		<c:forEach items="${langTypCdList}" var="langTypCdVo" varStatus="status">
		<u:decode srcValue="${langTypCdVo.cd}" tgtId="lang_${langTypCdVo.cd}" var="checked" value="checked" />
		<u:checkbox name="langTypCd" value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" checkValue="${checked}" onclick="toggleRescVa(this)" />
		</c:forEach>
		</u:checkArea>
	</td>
	</tr><c:if
		test="${not empty affiliatesEnable and sessionScope.userVo.userUid eq 'U0000001'}">
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="pt.jsp.affiliates" alt="계열사" /></td>
	<td class="body_lt" colspan="3"><c:forEach items="${ptCompBVo.affiliateNms}" var="affiliateNm" varStatus="status"
		><c:if test="${not status.first}">, </c:if>${affiliateNm}</c:forEach></td>
	</tr></c:if>

</u:listArea>

<u:buttonArea><c:if
		test="${sessionScope.userVo.userUid eq 'U0000001' and not empty param.compId}"><c:if
		test="${not empty affiliatesEnable}">
	<u:button titleId="pt.jsp.affiliates" href="javascript:setAffiliatesPop('${ptCompBVo.compId}');" alt="계열사" auth="SYS" /></c:if>
	<u:button titleId="pt.btn.emailBulkChg" href="javascript:chgEmailBulk();" alt="이메일 일괄 변경" auth="SYS" /></c:if>
	<u:button titleId="cm.btn.save" href="javascript:saveComp();" alt="저장" auth="SYS" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>