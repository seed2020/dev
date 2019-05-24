<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%


%>
<script type="text/javascript">
function moveTo(year){
	var url = "./usrAnbPlt.do?menuId=${menuId}&pltId=${param.pltId}${empty param.anbTypCd ? '' : '&anbTypCd='.concat(param.anbTypCd)}&year="+year;
	location.href = url;
}
function showDetlPop(){
	var url = "/wd/plt/listAnbDetlPop.do?menuId=${menuId}${empty param.anbTypCd ? '' : '&anbTypCd='.concat(param.anbTypCd)}&year=${year}";
	parent.dialog.open("listAnbDetlDialog", '<u:msg titleId="wd.jsp.detl" alt="상세" />', url);
}
</script>
<div class="ptlbody_ct">
<table class="ptltable" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td class="line"></td>
</tr>
<tr>
	<td class="head_ct" style="padding:2px 0 2px 0;">
		<table class="center" border="0" cellpadding="0" cellspacing="0">
		<tr><td class="frontico"><a href="javascript:" onclick="moveTo(${year-1});"><img src="${_ctx}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
			<td class="head_ct" style="font-weight:bold;width:60px;">${year}</td>
			<td class="frontico"><a href="javascript:" onclick="moveTo(${year+1});"><img src="${_ctx}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr></table>
	</td>
</tr>
<tr>
	<td class="line"></td>
</tr>
<tr>
	<td style="height:10px"></td>
</tr>
<tr>
	<td class="line"></td>
</tr>
<tr>
	<td style="padding:2px 0 2px 0;">
	<table class="center" border="0" cellpadding="0" cellspacing="0" style="width:100%; table-layout:fixed;"><tr>
		<td width="30%" class="body_lt"><u:msg titleId="wd.cnt.cre" alt="발생수" /></td>
		<td width="20%" class="body_rt"><c:if test="${not empty wdAnbBVo}">${wdAnbBVo.creCnt + wdAnbBVo.creModCnt}</c:if></td>
		<td width="40%"></td>
	</tr></table>
	</td>
</tr>
<tr>
	<td class="line"></td>
</tr>
<tr>
	<td style="padding:2px 0 2px 0;">
	<table class="center" border="0" cellpadding="0" cellspacing="0" style="width:100%; table-layout:fixed;"><tr>
		<td width="30%" class="body_lt"><u:msg titleId="wd.cnt.use" alt="사용수" /></td>
		<td width="20%" class="body_rt"><c:if test="${not empty wdAnbBVo}">${wdAnbBVo.useCnt + wdAnbBVo.useModCnt}</c:if></td>
		<td width="40%" class="body_rt"><u:buttonS titleId="wd.jsp.detl" alt="상세" onclick="showDetlPop()" /></td>
	</tr></table>
	</td>
</tr>
<tr>
	<td class="line"></td>
</tr>
<tr>
	<td style="padding:2px 0 2px 0;">
	<table class="center" border="0" cellpadding="0" cellspacing="0" style="width:100%; table-layout:fixed;"><tr>
		<td width="30%" class="body_lt"><u:msg titleId="wd.cnt.ongo" alt="결재중" /></td>
		<td width="20%" class="body_rt"><c:if test="${not empty wdAnbBVo}">${wdAnbBVo.ongoCnt + wdAnbBVo.ongoModCnt}</c:if></td>
		<td width="40%"></td>
	</tr></table>
	</td>
</tr>
<tr>
	<td class="line"></td>
</tr>
<tr>
	<td style="padding:2px 0 2px 0;">
	<table class="center" border="0" cellpadding="0" cellspacing="0" style="width:100%; table-layout:fixed;"><tr>
		<td width="30%" class="body_lt"><u:msg titleId="wd.cnt.left" alt="잔여" /></td>
		<td width="20%" class="body_rt"><c:if test="${not empty wdAnbBVo}">${
			wdAnbBVo.forwCnt + wdAnbBVo.forwModCnt
			+ wdAnbBVo.creCnt + wdAnbBVo.creModCnt
			- wdAnbBVo.useCnt - wdAnbBVo.useModCnt
			- wdAnbBVo.ongoCnt - wdAnbBVo.ongoModCnt
			}</c:if></td>
		<td width="40%"></td>
	</tr></table>
	</td>
</tr>
<tr>
	<td class="line"></td>
</tr>
</table>
</div>