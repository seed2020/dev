<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	String[] periods = { "7","14","30","60","90","120","150","180" };
	request.setAttribute("periods", periods);

%>
<script type="text/javascript">
<!--
function doSubmit(){
	if(validator.validate('pwPolcArea')){
		
		var min = $('#minLength').val();
		var max = $('#maxLength').val();
		if(min!='' && max!='' && parseInt(min)>parseInt(max)){
			alert("<u:msg titleId='pt.jsp.setPwPolc.msg1' alt='최소 길이가 최대 길이 보다 클 수 없습니다.' type='script' />");
			$('#minLength').val(max);
			$('#minLength').focus();
			return;
		}
		
		var $form = $('#pwPolcArea');
		$form.attr("action","./transPwPolc.do");
		$form.attr("target","dataframe");
		$form.submit();
	}
}
function skipper(){
	return $('#polcUseYnN')[0].checked;
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.setPwPolc.title" alt="비밀번호 정책 설정" menuNameFirst="true" />

<form id="pwPolcArea">
<input type="hidden" name="menuId" value="${menuId}" /><c:if test="${not empty param.setPwPolc}">
<input type="hidden" name="compId" value="${param.compId}" /></c:if>
<u:listArea>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.pwPolc.polcUseYn" alt="비밀번호 정책 사용유뮤" /></td>
		<td class="bodybg_lt">
			<u:checkArea><u:decode srcId="_lang" tgtValue="ko" var="cdLoc" value="공통코드 : 포털 / 비밀번호변경주기" elseValue="" />
			<u:radio id="polcUseYnY" name="pt.pwPolc${compId}.polcUseYn" value="Y" titleId="cm.option.use" checked="${pwPolc.polcUseYn == 'Y'}" />
			<u:radio id="polcUseYnN" name="pt.pwPolc${compId}.polcUseYn" value="N" titleId="cm.option.notUse" checked="${pwPolc.polcUseYn != 'Y'}" />
			</u:checkArea></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt" title="${cdLoc}"><u:msg titleId="pt.pwPolc.updatePeriod" alt="변경주기" /></td>
		<td><select id="updatePeriod" name="pt.pwPolc${compId}.updatePeriod" <u:elemTitle titleId="pt.pwPolc.updatePeriod" />>
			<c:forEach items="${periods}" var="period" varStatus="status">
			<u:option value="${period}" title="${period}" selected="${pwPolc.updatePeriod == period or (pwPolc.updatePeriod == null and period == '30')}"/>
			</c:forEach>
			</select></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.pwPolc.minLength" alt="최소 길이" /></td>
		<td><u:input id="minLength" name="pt.pwPolc${compId}.minLength" titleId="pt.pwPolc.minLength"
			value="${pwPolc.minLength}" mandatory="Y" minInt="4" maxInt="40" valueOption="number" skipper="skipper()" /></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.pwPolc.maxLength" alt="최대 길이" /></td>
		<td><u:input id="maxLength" name="pt.pwPolc${compId}.maxLength" titleId="pt.pwPolc.maxLength"
			value="${pwPolc.maxLength}" mandatory="Y" minInt="4" maxInt="40" valueOption="number" skipper="skipper()" /></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.pwPolc.numberMandatoryYn" alt="숫자 사용 의무화" /></td>
		<td class="bodybg_lt">
			<u:checkArea>
			<u:radio id="numberMandatoryYnY" name="pt.pwPolc${compId}.numberMandatoryYn" value="Y" titleId="cm.option.use" checked="${pwPolc.numberMandatoryYn != 'N'}" />
			<u:radio id="numberMandatoryYnN" name="pt.pwPolc${compId}.numberMandatoryYn" value="N" titleId="cm.option.notUse" checked="${pwPolc.numberMandatoryYn == 'N'}" />
			</u:checkArea></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.pwPolc.specailCharMandatoryYn" alt="특수문자 사용 의무화" /></td>
		<td class="bodybg_lt">
			<u:checkArea>
			<u:radio id="specailCharMandatoryYnY" name="pt.pwPolc${compId}.specailCharMandatoryYn" value="Y" titleId="cm.option.use" checked="${pwPolc.specailCharMandatoryYn == 'Y'}" />
			<u:radio id="specailCharMandatoryYnN" name="pt.pwPolc${compId}.specailCharMandatoryYn" value="N" titleId="cm.option.notUse" checked="${pwPolc.specailCharMandatoryYn != 'Y'}" />
			</u:checkArea></td>
	</tr>
</u:listArea><c:if

	test="${not empty lostPwEnable}"><u:secu auth="SYS">
<u:title titleId="pt.jsp.lostPw" alt="비밀번호 찾기" type="small" />
<u:listArea>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.lostPw.senderName" alt="메일 발송자" /></td>
		<td><u:input id="senderName" titleId="pt.lostPw.senderName"
			value="${lostPwPolc.senderName}" mandatory="Y" minInt="4" maxInt="40" /></td>
	</tr>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="pt.lostPw.senderEmail" alt="메일 발송자 이메일" /></td>
		<td><u:input id="senderEmail" titleId="pt.lostPw.senderEmail"
			value="${lostPwPolc.senderEmail}" mandatory="Y" minInt="4" maxInt="40" valueOption="email" style="width:250px" /></td>
	</tr>
</u:listArea></u:secu></c:if>

</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="doSubmit()" auth="A" />
</u:buttonArea>