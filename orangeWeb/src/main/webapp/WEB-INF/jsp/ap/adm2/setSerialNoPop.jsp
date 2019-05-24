<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="newSerialNo" value="332" elseValue="" />
<u:set test="${fnc == 'mod'}" var="newSeed" value="140215" elseValue="" />

<div style="width:400px">
<form id="setTaskInfoForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea colgroup="27%,73%">
	<tr>
	<td class="head_lt">부서명</td>
	<td class="body_lt">홍보부</td>
	</tr>
	
	<tr>
	<td class="head_lt">부서코드</td>
	<td class="body_lt">13233</td>
	</tr>
	
	<tr>
	<td class="head_lt">현재 채번값</td>
	<td class="body_lt">30</td>
	</tr>
	
	<tr>
	<td class="head_lt">수정 채번값</td>
	<td><u:input id="newSerialNo" value="${newSerialNo}" title="수정 채번값" style="width: 274px;" /></td>
	</tr>
	
	<tr>
	<td class="head_lt">현재 SEED</td>
	<td class="body_lt">140215</td>
	</tr>
	
	<tr>
	<td class="head_lt">수정 SEED</td>
	<td><u:input id="newSerialNo" value="${newSeed}" title="수정 SEED" style="width: 274px;" /></td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" alt="확인" onclick="dialog.close(this);" auth="A" />
	<u:button titleId="cm.btn.cancel" alt="취소" onclick="dialog.close(this);" />
</u:buttonArea>

</form>
</div>
