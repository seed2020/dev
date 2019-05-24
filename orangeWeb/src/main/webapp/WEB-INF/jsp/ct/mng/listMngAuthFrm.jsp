<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<%// [버튼] 기능권한등록 %>
function fncAuthSubmit(){
		var $form = $('#listMngAuthListForm');
		$form.attr('method','post');
		$form.attr('action','./transCtAuthSaveAjx.do?');
		$form.attr('target','mngAuth');
		$form.submit();
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<form id="listMngAuthListForm">
<input type="hidden" id="fncUid" name="fncUid" value="${fncUid}" />
<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
<input type="hidden" id="ctId" name="ctId" value="${param.ctId}" />
<u:listArea>
		<tr>
		<td class="head_lt"><u:msg titleId="ct.cols.mbshLev1" alt="스텝" /></td>
		</tr>
		
		<tr>
		<td><table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
				<u:set test="${ctAuthS.authSR =='R' && !empty ctAuthS.authSR}" var="ctAuthSR" value="checked" elseValue=""/>
				<u:set test="${ctAuthS.authSW =='W' && !empty ctAuthS.authSW}" var="ctAuthSW" value="checked" elseValue=""/>
				<u:set test="${ctAuthS.authSD =='D' && !empty ctAuthS.authSD}" var="ctAuthSD" value="checked" elseValue=""/> 
				<td><u:checkbox name="authSR" value="R" titleId="ct.option.authR" checkValue="${ctAuthSR}" inputClass="bodybg_lt" /></td>
				<td><u:checkbox name="authSW" value="W" titleId="ct.option.authW" checkValue="${ctAuthSW}" inputClass="bodybg_lt" /></td>
				<td><u:checkbox name="authSD" value="D" titleId="ct.option.authD" checkValue="${ctAuthSD}" inputClass="bodybg_lt" /></td>
			</tr>
			</tbody></table></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="ct.cols.mbshLev2" alt="정회원" /></td>
		</tr>
		
		<tr>
		<td><table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
				<u:set test="${ctAuthR.authRR =='R' && !empty ctAuthR.authRR}" var="ctAuthRR" value="checked" elseValue=""/>
				<u:set test="${ctAuthR.authRW =='W' && !empty ctAuthR.authRW}" var="ctAuthRW" value="checked" elseValue=""/>
				<u:set test="${ctAuthR.authRD =='D' && !empty ctAuthR.authRD}" var="ctAuthRD" value="checked" elseValue=""/> 
				<td><u:checkbox name="authRR" value="R" titleId="ct.option.authR" checkValue="${ctAuthRR}"  inputClass="bodybg_lt" /></td>
				<td><u:checkbox name="authRW" value="W" titleId="ct.option.authW" checkValue="${ctAuthRW}" inputClass="bodybg_lt" /></td>
				<td><u:checkbox name="authRD" value="D" titleId="ct.option.authD" checkValue="${ctAuthRD}" inputClass="bodybg_lt" /></td>
			</tr>
			</tbody></table></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="ct.cols.mbshLev3" alt="준회원" /></td>
		</tr>
		
		<tr>
		<td><table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
				<u:set test="${ctAuthA.authAR =='R' && !empty ctAuthA.authAR}" var="ctAuthAR" value="checked" elseValue=""/>
				<u:set test="${ctAuthA.authAW =='W' && !empty ctAuthA.authAW}" var="ctAuthAW" value="checked" elseValue=""/>
				<u:set test="${ctAuthA.authAD =='D' && !empty ctAuthA.authAD}" var="ctAuthAD" value="checked" elseValue=""/> 
				<td><u:checkbox name="authAR" value="R" titleId="ct.option.authR" checkValue="${ctAuthAR}" inputClass="bodybg_lt" /></td>
				<td><u:checkbox name="authAW" value="W" titleId="ct.option.authW" checkValue="${ctAuthAW}" inputClass="bodybg_lt" /></td>
				<td><u:checkbox name="authAD" value="D" titleId="ct.option.authD" checkValue="${ctAuthAD}" inputClass="bodybg_lt" /></td>
			</tr>
			</tbody></table></td>
		</tr>
		
		<tr>
		<td class="head_lt"><!--<u:msg titleId="ct.cols.mbshLev4" alt="게스트" /> --></td>
		</tr>
		
		<tr>
		<td><table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			
			<!-- 
			<td><u:checkbox name="authR4" value="" titleId="ct.option.authR" checked="false" inputClass="bodybg_lt" /></td>
			<td><u:checkbox name="authW4" value="" titleId="ct.option.authW" checked="false" inputClass="bodybg_lt" /></td>
			<td><u:checkbox name="authD4" value="" titleId="ct.option.authD" checked="false" inputClass="bodybg_lt" /></td>
			 -->
			</tr>
			</tbody></table></td>
		</tr>
	</u:listArea>
	

</form>

<div id="mngAuth" ></div>