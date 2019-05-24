<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function fnCntcTypCd( obj ){
	$('li.cntcCls').each(function(){$(this).hide()});
	$('li.'+obj.value+'Cls').each(function(){$(this).show()});
};

$(document).ready(function() {
	setUniformCSS();
	//$('#schWord').focus();
});
//-->
</script>
<% // 검색 %>
<form name="searchForm" action="./findBcPwsmFrm.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="typ" value="${param.typ}" />
	<u:input type="hidden" id="bcId" value="${param.bcId}" />
	<u:input type="hidden" id="schCat" value="pwsmName" />

	<u:listArea>
		<tr>
			<td width="27%" class="head_lt"><u:msg titleId="cols.nm" alt="이름" /></td>
			<td >
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.nm" />
						</td>
						<td><a href="javascript:searchForm.submit()" class="ico_search"><span><u:msg alt="검색" titleId="cm.btn.search" /></span></a></td>
					</tr>
				</table>
			</td>
		</tr>
	</u:listArea>
</form>

<% // 목록 %>
<u:listArea id="listArea">
	<tr>
		<td width="8%" class="head_bg">&nbsp;</td>
		<td width="18%" class="head_bg"><u:msg titleId="cols.nm" alt="이름" /></td>
		<td width="25%" class="head_ct"><u:msg titleId="cols.comp" alt="회사" /></td>
		<td width="*" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
		<td width="20%" class="head_ct">
			<select name="cntcTypCd" onchange="fnCntcTypCd(this);">
				<u:option value="compPhon" titleId="cols.comp" alt="회사" />
				<u:option value="homePhon" titleId="cols.home" alt="자택" />
				<u:option value="mbno" titleId="cols.mob" alt="휴대" />
			</select>
		</td>
	</tr>
	<c:choose>
		<c:when test="${!empty wbBcBVoList }">
			<c:forEach var="list" items="${wbBcBVoList}" varStatus="status">
				<tr>
					<td class="bodybg_ct"><u:radio name="bcNmCheck" value="${list.bcId}" checked="${status.count == 1}"  /></td>
					<td class="bodybg_ct">${list.bcNm }</td>
					<td class="bodybg_ct">${list.compNm }</td>
					<td class="bodybg_ct">${list.deptNm }</td>
					<td class="bodybg_ct">
						<ul style="list-style:none;margin:0;padding:0px;">
							<li class="cntcCls compPhonCls" >${list.compPhon }</li>
							<li class="cntcCls homePhonCls" style="display:none;">${list.homePhon }</li>
							<li class="cntcCls mbnoCls" style="display:none;">${list.mbno }</li>
						</ul>
					</td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise><tr><td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td></tr></c:otherwise>
	</c:choose>
</u:listArea>

<u:pagination noTotalCount="true" />

	