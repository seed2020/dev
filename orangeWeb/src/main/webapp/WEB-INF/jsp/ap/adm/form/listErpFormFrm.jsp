<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 선택된 항목 리턴 %>
function getChecked(){
	var $chked = $("#listApvForm input[name='apErpForm']:checked");
	if($chked.length==0) {
		alertMsg("cm.msg.noSelectedItem",["#ap.formCmpt"]);<%//cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.%>
		return null;
	} else {
		return $chked.val();
	}
}<%
// onload %>
$(document).ready(function() {<%
	// 유니폼 적용 %>
	setUniformCSS();
	$('#schWord').focus();
});
//-->
</script>
<div style="width:100%">

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="bxId" value="${param.bxId}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="ap.formCmpt.nm" alt="폼 양식 명" /><input type="hidden" name="schCat" value="erpFormNm" /></td>
		<td><u:input id="schWord" value="${param.schWord}" titleId="cols.schWord" style="width: 260px;" /></td>
		</tr>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<%// 목록 %>
<form id="listApvForm">

<u:listArea id="listArea">
	<tr>
		<td class="head_ct" colspan="2"><u:msg titleId="ap.formCmpt.nm" alt="폼 양식 명" /></td>
	</tr>
<c:if test="${empty recodeCount or recodeCount == 0}">
	<tr>
		<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:forEach items="${apErpFormBVoList}" var="apErpFormBVo" varStatus="outStatus">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="bodybg_ct" width="4%"><input type="radio" name="apErpForm" id="apErpForm${apErpFormBVo.erpFormId}" value="${apErpFormBVo.erpFormId
			}" data-rescNm="<u:out value="${apErpFormBVo.rescNm}" type="value"
			/>" data-regUrl="<u:out value="${apErpFormBVo.regUrl}" type="value"
			/>" ${apErpFormBVo.erpFormId eq param.erpFormId ? 'checked="checked"' : ''}/></td>
		<td class="body_lt"><c:if test="${sessionScope.userVo.userUid eq 'U0000001'}"
			><a href="javascript:;" onclick="parent.openErpFormPop('${apErpFormBVo.erpFormId}');"><u:out value="${apErpFormBVo.rescNm}" /></a></c:if><c:if test="${sessionScope.userVo.userUid ne 'U0000001'}"
			><label for="apErpForm${apErpFormBVo.erpFormId}"><u:out value="${apErpFormBVo.rescNm}" /></label></c:if></td>
	</tr>
</c:forEach>
</u:listArea>
</form>

<u:pagination pageRowCnt="10" noBottomBlank="${true}" noTotalCount="true"  />

</div>
