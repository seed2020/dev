<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	request.setAttribute("_type", "single".equals(request.getParameter("mode")) ? "radio" : "checkbox");
%>
<script type="text/javascript">
<!--
function getCheckedObj(){
	var arr = [];
	$("#outsListArea input[type='${_type}']:visible:checked").not("#checkHeader").each(function(){
		arr.push({id:$(this).val(), nm:$(this).attr('data-mpNm'), type:'out'});
	});
	$("#searchForm1 #mpNm").blur();
	return arr;
}
function getCheckedId(){
	var arr = [];
	$("#outsListArea input[type='${_type}']:visible:checked").not("#checkHeader").each(function(){
		arr.push($(this).val());
	});
	$("#searchForm1 #mpNm").blur();
	return arr;
}
$(document).ready(function() {
	setUniformCSS();
	//$("#searchForm1 #mpNm").focus();
});
//-->
</script>

<%-- // 검색영역 --%>
<u:searchArea style="position:relative; z-index:2;">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="cat" value="${param.cat}" />
	<u:input type="hidden" id="role1Cd" value="${param.role1Cd}" /><c:if
		test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.nm" alt="이름" /></td>
		<td><u:input id="mpNm" titleId="cols.nm" alt="이름" value="${param.mpNm}" maxLength="10" /></td>
		</tr>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<u:listArea id="outsListArea" colgroup="4%,20%,24%,24%,28%">
<tr>
	<td class="head_bg"><c:if
		test="${ _type eq 'checkbox'}"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('outsListArea', this.checked);" value=""/></c:if></td>
	<td class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></td>
	<td class="head_ct"><u:msg titleId="cols.grade" alt="직급" /></td>
	<td class="head_ct"><u:msg titleId="cols.phon" alt="전화번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.email" alt="이메일" /></td>
</tr><c:if test="${fn:length(wpMpBVoList)==0}" >
<tr>
	<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
</tr>
</c:if><c:forEach
	items="${wpMpBVoList}" var="wpMpBVo" varStatus="status">
<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="bodybg_ct"><input name="mpId" id="mpId${wpMpBVo.mpId}" type="${_type}" value="${wpMpBVo.mpId}" data-mpNm="${wpMpBVo.mpNm}"/></td>
	<td class="body_ct"><u:out value="${wpMpBVo.mpNm}" /></td>
	<td class="body_ct"><u:out value="${wpMpBVo.mpGrade}" /></td>
	<td class="body_ct"><u:out value="${wpMpBVo.mpPhone}" /></td>
	<td class="body_ct"><u:out value="${wpMpBVo.mpEmail}" /></td>
</tr>
</c:forEach>
</u:listArea>

<u:pagination pageRowCnt="10" noTotalCount="true" />
