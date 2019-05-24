<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function getChecked(){
	var $obj = $("#listLinkedApvNoForm input[name='apvNo']:checked");
	if($obj.length>0){
		var returnObj = {apvNo:$obj.val(), docSubj:$obj.attr("data-docSubj"), secuYn:$obj.attr("data-secuYn")};
		var erpLinkedApvNo = $obj.attr('data-erpLinkedApvNo');
		if(erpLinkedApvNo!='' && erpLinkedApvNo!='${param.apvNo}'){
			returnObj['msg'] = '<u:msg titleId="ap.cfm.alreadyRelated" alt="이미 관련문서가 등록된 문서 입니다. 계속하시겠습니까 ?" />';
		}
		return returnObj;
	}
	return null;
}<%
// onload %>
$(document).ready(function() {
	var erpLinkedApvNo = '${param.erpLinkedApvNo}';
	if(erpLinkedApvNo!=''){
		var ck = $("#listLinkedApvNoForm input[value='"+erpLinkedApvNo+"']");
		if(ck.length>0) ck[0].checked = true;
	}<%
	// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>
<div style="width:100%">

<%// 목록 %>
<form id="listLinkedApvNoForm">

<u:listArea id="listArea" tableStyle="table-layout: fixed;">
	<tr>
		<td width="4.5%" class="head_bg">&nbsp;</td>
		<td width="43.5%" class="head_ct"><u:msg titleId="ap.doc.docSubj" alt="제목" /></td>
		<td width="12%" class="head_ct"><u:msg titleId="ap.jsp.relatedDoc" alt="관련문서" /></td>
		<td width="20%" class="head_ct"><u:msg titleId="ap.doc.makDt" alt="기안일시" /></td>
		<td width="20%" class="head_ct"><u:msg titleId="ap.doc.makDeptNm" alt="기안부서" /></td>
	</tr>
<c:if test="${recodeCount == 0}">
	<tr>
		<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>

<c:forEach items="${apOngdBVoMapList}" var="apOngdBVoMap" varStatus="outStatus"><c:set
	var="apOngdBVoMap" value="${apOngdBVoMap}" scope="request" />
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="bodybg_ct"><input type="radio" name="apvNo" value="${apOngdBVoMap.apvNo
			}" data-apvNo="<u:out value="${apOngdBVoMap.apvNo}" type="value"
			/>" data-docSubj="<u:out value="${apOngdBVoMap.docSubj}" type="value"
			/>" data-makrUid="<u:out value="${apOngdBVoMap.makrUid}" type="value"
			/>" data-erpLinkedApvNo="<u:out value="${apOngdBVoMap.erpLinkedApvNo}" type="value"
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
		<td class="body_ct">${not empty apOngdBVoMap.erpLinkedApvNo ? 'Y' : ''}</td>
		<td class="body_ct"><u:out type="longdate" value="${apOngdBVoMap.makDt}" /></td>
		<td class="body_ct"><div class="ellipsis"><u:out value="${apOngdBVoMap.makDeptNm}" /></div></td>
	</tr>
</c:forEach>
</u:listArea>
</form>
			
<u:pagination pageRowCnt="10" noBottomBlank="${true}" noTotalCount="true" onPageChange="onPageChange()" />

</div>
