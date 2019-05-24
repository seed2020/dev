<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 체크된 목록 리턴 / 또는 전체 목록 리턴 %>
function getCheckedList(mode){
	var arr = [];
	$("#listArea input[type='checkbox']"+(mode=='all' ? "" : ":checked")).each(function(){
		if($(this).val()!=''){
			arr.push({mnuGrpId:$(this).val(), rescId:$(this).attr('data-rescId'), rescNm:$(this).attr('data-rescNm')});
		}
	});
	checkAllCheckbox('listArea', false);
	return arr;
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>

<u:title titleId="pt.jsp.setIconLout.listMnuGrp" alt="메뉴 그룹 목록" type="small" />

<%// 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listMnuGrpFrm.do">
	<input type="hidden" name="menuId" value="${menuId}" />
	<c:if test="${not empty param.mnuGrpMdCd}"><input type="hidden" name="mnuGrpMdCd" value="${param.mnuGrpMdCd}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.mnuGrpNm" alt="메뉴그룹명" /></td>
		<td><u:input id="schWord" titleId="cm.schWord" style="width:300px;" value="${param.schWord}" maxByte="50"/></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<%// 목록 %>
<u:listArea id="listArea" >

	<tr>
	<td width="4%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
	<td class="head_ct"><u:msg titleId="cols.mnuGrpNm" alt="메뉴그룹명" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="pt.cols.openScop" alt="공개범위" /></td>
	<td width="22%" class="head_ct"><u:msg titleId="cols.mnuGrpTyp" alt="메뉴그룹구분" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>

<c:if test="${fn:length(ptMnuGrpBVoList)==0}" >
	<tr>
	<td class="nodata" colspan="8"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(ptMnuGrpBVoList)!=0}" >
	<c:forEach items="${ptMnuGrpBVoList}" var="ptMnuGrpBVo" varStatus="status">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="tr${ptMnuGrpBVo.mnuGrpId}" >
		<td class="bodybg_ct"><input type="checkbox" value="${ptMnuGrpBVo.mnuGrpId}" data-rescId="${ptMnuGrpBVo.rescId}" data-rescNm="<u:out value="${ptMnuGrpBVo.rescNm}" type="value" />" /></td>
		<td class="body_lt"><u:out value="${ptMnuGrpBVo.rescNm}" /></td>
		<td class="body_ct"><u:out value="${ptMnuGrpBVo.openCompNm}" /></td>
		<td class="body_ct"><u:out value="${ptMnuGrpBVo.mnuGrpTypNm}" /></td>
		<td class="body_ct"><u:out value="${ptMnuGrpBVo.regrNm}" /></td>
		<td class="body_ct"><u:out value="${ptMnuGrpBVo.regDt}" type="date" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination pageRowCnt="10" noBottomBlank="true" noTotalCount="true" />
