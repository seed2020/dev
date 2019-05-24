<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var authGrad = "R";

function authSet(){
	
	var grad = $("#authSelectBox").val();
	
	if(grad=="A"){
		authGrad = "A";
	}else if(grad=="W"){
		authGrad = "W";
	}else{
		authGrad = "R";
	}
	var authGradSet = schdlGrpMbshAuthSet();
	
}

function authGradSet(){
	return {authGrad:authGrad};	
}

//-->
</script>

<div style="width:400px">
<form id="setAuthForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<div class="front">
<div class="front_left">
	<table border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td class="color_txt">※ <u:msg titleId="wc.jsp.setAuthPop.tx01" alt="선택한 회원들의 권한을 일괄 적용합니다." /></td>
	</tr>
	</tbody></table>
</div>
</div>

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="27%" class="head_lt"><u:msg titleId="cols.auth" alt="권한" /></td>
	<td width="73%"><select id="authSelectBox">
		<option value = "R"><u:msg titleId="wc.cols.grp.red" alt="읽기" /></option>
		<option value = "W"><u:msg titleId="wc.cols.grp.wrt" alt="쓰기" /></option>
		<option value = "A"><u:msg titleId="wc.cols.grp.adm" alt="관리" /></option>
		</select></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:authSet();" alt="저장" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
