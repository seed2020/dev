<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<%// 위로, 아래로 이동%>
function move(direction){
	var arr = getCheckedValue("listArea", "ap.msg.noFormSelected");<%//ap.msg.noFormSelected=선택된 양식이 없습니다.%>
	if(arr!=null){
		var formIds = null;
		callAjax('./transFormMoveAjx.do?menuId=${menuId}', {direction:direction, formBxId:"${param.formBxId}", formIds:arr}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.formIds!=null) formIds = data.formIds.split(',');
		});
		if(formIds!=null){
			var $tbody = $("#listArea tbody:first"), $tr;
			formIds.each(function(index, va){
				$tr = $tbody.find("#tr"+va);
				if(direction=='up'){
					$tr.insertBefore($tr.prev());
				} else {
					$tr.insertAfter($tr.next());
				}
			});
		}
	}
}
<%// 목록 클릭 - 정보수정 팝업 / 팝업에서 양식 수정으로 이동 %>
function openFormPop(formId){
	var popTitle = formId==null ? '<u:msg titleId="ap.jsp.setApvForm.regForm" alt="양식 등록"/>' : '<u:msg titleId="ap.jsp.setApvForm.modForm" alt="양식 수정"/>';
	parent.dialog.open('setFormEssDialog', popTitle, './setFormEssPop.do?menuId=${menuId}&formBxId=${param.formBxId}'+(formId==null ? '' :'&formId='+formId));
}<%
// 선택된 양식 목록 - parent(setApvForm.jsp) 에서 호출 %>
function getSelected(){
	return getCheckedValue("listArea", "ap.msg.noFormSelected");<%//ap.msg.noFormSelected=선택된 양식이 없습니다.%>
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
	<input type="hidden" name="menuId" value="${menuId}" />
	<input type="hidden" name="formBxId" value="${param.formBxId}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.formNm" alt="양식명" /></td>
		<td><u:input id="schWord" titleId="cm.schWord" style="width:200px;" value="${param.formNm}" maxByte="60" /></td>
		<td class="width10"></td>
		<td class="search_tit"><u:msg titleId="cols.formTyp" alt="양식구분" /></td>
		<td><select id="formTypCd" name="formTypCd"<u:elemTitle titleId="cols.formTyp" alt="양식구분" />>
			<option value=""><u:msg titleId="cm.option.all" alt="전체" /></option><c:forEach
				items="${formTypCdList}" var="formTypCd" varStatus="status"
			><u:option value="${formTypCd.cd}" title="${formTypCd.rescNm}" selected="${param.formTypCd == formTypCd.cd}"
			/></c:forEach></select></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<%// 목록 %>
<u:listArea id="listArea">

	<tr>
	<td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
	<td class="head_ct"><u:msg titleId="cols.formNm" alt="양식명" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.formId" alt="양식ID" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.formTyp" alt="양식구분" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.tplYn" alt="템플릿여부" /></td>
	</tr>

<c:if test="${fn:length(apFormBVoList)==0}" >
	<tr>
	<td class="nodata" colspan="9"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(apFormBVoList) >0}" >
	<c:forEach items="${apFormBVoList}" var="apFormBVo" varStatus="status">
	<tr id="tr${apFormBVo.formId}" onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="bodybg_ct"><input type="checkbox" id="formId" value="${apFormBVo.formId}"/></td>
	<td class="body_lt"><a href="javascript:openFormPop('${apFormBVo.formId}');" title="<u:msg titleId="ap.jsp.setApvForm.modForm"/> - <u:msg titleId="cm.pop"/>"><u:out value="${apFormBVo.rescNm}" /></a></td>
	<td class="body_ct">${apFormBVo.formId}</td>
	<td class="body_ct">${apFormBVo.formTypNm}</td>
	<td class="body_ct">${apFormBVo.regrNm}</td>
	<td class="body_ct"><u:out value="${apFormBVo.regDt}" type="date" /></td>
	<td class="body_ct"><u:yn value="${apFormBVo.useYn}" yesId="cm.option.use" noId="cm.option.notUse" alt="사용/사용안함" /></td>
	<td class="body_ct">${apFormBVo.tplYn}</td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

</div>