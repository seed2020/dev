<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 양식 클릭 %>
function applyDoc(formId, newTab){
	if(formId==null){
		var $chk = $("#formListArea input[name='formId']:checked");
		if($chk.length == 0){
			alertMsg("cm.msg.noSelectedItem",["#cols.form"]);<%//cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.%>
			return;
		}
		formId = $chk.val();
	}<%
	// 시행용 변환일 경우
	//if("trans".equals(request.getParameter("formTypCd")) || request.getParameter("forTrans") != null){
	if(request.getParameter("forTrans") != null){ %>
	parent.transEnfc(formId);<%
	// 일반 양식 선택
	} else { %>
	if(parent.makeDoc) parent.makeDoc(formId, '${param.refDocApvNo}', newTab);
	else {
		if(newTab==true){
			parent.open("./setDoc.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}${empty param.refDocApvNo ? '' : '&refDocApvNo='.concat(param.refDocApvNo)}&formId="+formId,'_blank').focus();
		} else {
			parent.location.href="./setDoc.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}${empty param.refDocApvNo ? '' : '&refDocApvNo='.concat(param.refDocApvNo)}&formId="+formId;
		}
	}<%
	} %>
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<div style="padding:10px 10px 0px 10px;">
<%// 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listFormFrm.do">
	<input type="hidden" name="menuId" value="${menuId}" /><c:if
		test="${not empty param.bxId}">
	<input type="hidden" name="bxId" value="${param.bxId}" /></c:if>
	<input type="hidden" name="formBxId" value="${param.formBxId}" /><c:if
		test="${not empty param.forTrans or param.formTypCd=='trans'}">
	<input type="hidden" name="forTrans" value="Y" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.formNm" alt="양식명" /></td>
		<td><u:input id="schWord" titleId="cm.schWord" style="width:200px;" value="${param.formNm}" maxByte="30" /></td>
		<td class="width10"></td>
		</tr>
		<tr>
		<td class="search_tit"><u:msg titleId="cols.formTyp" alt="양식구분" /></td>
		<td><select id="formTypCd" name="formTypCd"<u:elemTitle titleId="cols.formTyp" alt="양식구분" />>
			<option value=""><u:msg titleId="cm.option.all" alt="전체" /></option><c:forEach
				items="${formTypCdList}" var="formTypCd" varStatus="status"><c:if
				test="${formTypCd.cd != 'trans' or not empty param.forTrans or param.formTypCd=='trans'}">
			<u:option value="${formTypCd.cd}" title="${formTypCd.rescNm}" selected="${param.formTypCd == formTypCd.cd}"
			/></c:if></c:forEach></select></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<%// 목록 %>
<u:listArea id="formListArea">

	<tr>
	<td width="3%" class="head_bg"></td>
	<td class="head_ct"><u:msg titleId="cols.formNm" alt="양식명" /></td>
	<td width="13%" class="head_ct"><u:msg titleId="cols.formId" alt="양식ID" /></td>
	<td width="22%" class="head_ct"><u:msg titleId="cols.formTyp" alt="양식구분" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>

<c:if test="${fn:length(apFormBVoList)==0}" >
	<tr>
	<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(apFormBVoList) >0}" >
	<c:forEach items="${apFormBVoList}" var="apFormBVo" varStatus="status">
	<tr id="tr${apFormBVo.formId}" onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="bodybg_ct"><input type="radio" name="formId" value="${apFormBVo.formId}"/></td>
	<td class="body_lt"><a href="javascript:applyDoc('${apFormBVo.formId}');" title="<u:msg titleId="ap.jsp.setApvForm.modForm"/> - <u:msg titleId="cm.pop"/>"><u:out value="${apFormBVo.rescNm}" /></a></td>
	<td class="body_ct">${apFormBVo.formId}</td>
	<td class="body_ct">${apFormBVo.formTypNm}</td>
	<td class="body_ct"><u:out value="${apFormBVo.regDt}" type="date" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

</div>