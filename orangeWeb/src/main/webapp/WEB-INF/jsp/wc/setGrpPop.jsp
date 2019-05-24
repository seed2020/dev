<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="grpNm" value="부천지역모임" elseValue="" />
<u:set test="${fnc == 'mod'}" var="selected" value=" selected" elseValue="" />

<div style="width:600px">
<form id="setGrpForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="27%" class="head_lt"><u:mandatory /><u:msg titleId="cols.grpNm" alt="그룹명" /></td>
	<td width="73%"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:input id="grpNm" value="${grpNm}" titleId="cols.grpNm" style="width: 194px;" /></td>
		<td><select>
			<option>국문</option>
			<option>영문</option>
			</select></td>
		</tr>
		</tbody></table></td>
	</tr>
	
	<tr>
	<td rowspan="2" class="head_lt"><u:msg titleId="cols.mbsh" alt="회원" /></td>
	<td><table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td width="230"><u:input id="recvr" value="" titleId="cols.recvr" style="width: 384px;" /></td>
		<td><u:buttonS titleId="cm.btn.choice" alt="선택" onclick="dialog.open('findOrgcPop','조직도','/bb/findOrgcPop.do?menuId=${menuId}');" /></td>
		</tr>
		</tbody></table></td>
	</tr>
	
	<tr>
	<td><table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td width="230"><select size="3" style="width: 393px;">
			<option>${recvr}</option>
			</select></td>
		<td style="padding-top: 2px; vertical-align: top;"><u:buttonS href="" titleId="cm.btn.del" alt="삭제" /></td>
		</tr>
		</tbody></table></td>
	</tr>
	
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.dftAuth" alt="기본권한" /></td>
	<td><select>
		<option>읽기</option>
		<option${selected}>쓰기</option>
		<option>관리</option>
		</select></td>
	</tr>
	<c:if test="${mailEnable == 'Y' }">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.emailSendYn" alt="이메일발송여부" /></td>
	<td><u:checkArea>
		<u:radio name="emailSendYn" value="Y" titleId="cm.option.yes" alt="예" checked="${fnc == 'mod'}" inputClass="bodybg_lt" />
		<u:radio name="emailSendYn" value="N" titleId="cm.option.no" alt="아니오" inputClass="bodybg_lt" />
		</u:checkArea></td>
	</tr>
	</c:if>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="dialog.close(this);" alt="저장" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
