<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function getChecked(){
	return $("#listCmplAnbApvNoForm input[name='apvNo']:checked").val();
}<%
// onload %>
$(document).ready(function() {<%
	// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>
<div style="width:100%">

<%// 목록 %>
<form id="listCmplAnbApvNoForm">

<u:listArea id="listArea" tableStyle="table-layout: fixed;">
	<tr>
		<td width="5.5%" class="head_bg">&nbsp;</td>
		<td width="44.5%" class="head_ct"><u:msg titleId="ap.doc.docSubj" alt="제목" /></td>
		<td width="30%" class="head_ct"><u:msg titleId="ap.cols.prd" alt="기간" /></td>
		<td width="20%" class="head_ct"><u:msg titleId="ap.list.cmplDd" alt="완결일자" /></td>
	</tr>
<c:if test="${recodeCount == 0}">
	<tr>
		<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>

<c:forEach items="${apOngdBVoMapList}" var="apOngdBVoMap" varStatus="outStatus"><c:set
	var="apOngdBVoMap" value="${apOngdBVoMap}" scope="request" />
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="bodybg_ct"><input type="radio" name="apvNo" value="${apOngdBVoMap.apvNo
			}" data-apvNo="<u:out value="${apOngdBVoMap.apvNo}" type="value"
			/>" data-docSubj="<u:out value="${apOngdBVoMap.docSubj}" type="value"
			/>" data-makrUid="<u:out value="${apOngdBVoMap.makrUid}" type="value"
			/>" data-makrNm="<u:out value="${apOngdBVoMap.makrNm}" type="value"
			/>" data-cmplDt="<u:out value="${apOngdBVoMap.cmplDt}" type="value"
			/>" data-secuYn="${
						not empty apOngdBVoMap.docPwEnc
						and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}" /></td>
		<td class="body_lt"><div class="ellipsis"><a href="javascript:parent.openDocView('${apOngdBVoMap.apvNo}','${
						not empty apOngdBVoMap.docPwEnc
						and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}')"><c:if
						test="${apOngdBVoMap.ugntDocYn == 'Y'}"
					>[<u:msg titleId="bb.option.ugnt" alt="긴급" />] </c:if><c:if
						test="${not empty apOngdBVoMap.docPwEnc}"
					>[<u:msg titleId="bb.option.secu" alt="보안" />] </c:if><u:out
				value="${apOngdBVoMap.docSubj}" /></a></div></td>
		<td class="body_ct">${apOngdBVoMap.period}</td>
		<td class="body_ct"><u:out value="${apOngdBVoMap.cmplDt}" type="date" /></td>
	</tr>
</c:forEach>
</u:listArea>
</form>
			
<u:pagination pageRowCnt="10" noBottomBlank="${true}" noTotalCount="true" onPageChange="onPageChange()" />

</div>
