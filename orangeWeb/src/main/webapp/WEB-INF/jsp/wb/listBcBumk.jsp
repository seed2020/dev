<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<%// 체크된 목록 리턴 - uncheck : true 면 체크를 해제함 %>
function getChecked(uncheck){
	var va, arr = [];
	$("#listAreaFrom input:checked").each(function(){
		va = $(this).val();
		if(va!=null && va!=''){
			arr.push({bcId:va,bcNm:$(this).attr("data-bcNm"),compNm:$(this).attr("data-compNm"),dftCntcVal:$(this).attr("data-dftCntcVal"),email:$(this).attr("data-email")});
		}
		if(uncheck){
			$(this).trigger('click');
		}
	});
	if(arr.length==0){
		alertMsg("cm.msg.noSelect");<%//cm.msg.noSelect=선택한 항목이 없습니다.%>
		return null;
	}
	return arr;
};
//상세보기
function viewBc(bcId) {
	parent.viewBc(bcId);
};
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<div id="setFromBcBumk" style="padding:10px;">
	<% // 검색영역 %>
<u:searchArea>
<form name="searchForm" action="./listBcBumkFrm.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />

<table class="search_table" cellspacing="0" cellpadding="0" border="0" >
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<select name="schCat">
							<c:forEach	items="${wbBcUserLstSetupRVoList}" var="wbBcUserLstSetupRVo" varStatus="status">
								<u:option value="${wbBcUserLstSetupRVo.atrbId }" titleId="${wbBcUserLstSetupRVo.msgId }" alt="이름" checkValue="${param.schCat}" />
							</c:forEach>
						</select>
					</td>
					<td><u:input id="schWord" maxByte="50" name="schWord" value="${param.schWord}" titleId="cols.schWord" /></td>
				</tr>
			</table>
		</td>
		<td>
			<div class="button_search">
				<ul>
					<li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li>
				</ul>
			</div>
		</td>
	</tr>
</table>
</form>
</u:searchArea>
	<div id="listAreaFrom" class="listarea">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
		<tr id="headerTr">
			<th width="20px" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listAreaFrom', this.checked);" value=""/></th>
			<th width="20%" class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></th >
			<th class="head_ct"><u:msg titleId="cols.comp" alt="회사" /></th >
			<th width="20%" class="head_ct"><u:msg titleId="cols.reprCntc" alt="우선연락처" /></th >
			<th width="20%" class="head_ct"><u:msg titleId="cols.email" alt="이메일" /></th>
		</tr>
		<c:if test="${!empty wbBcBVoList}">
			<c:forEach var="list" items="${wbBcBVoList}" varStatus="status">
				<u:set var="dftCntcVal" test="${list.dftCntcTypCd eq 'homePhon' || list.dftCntcTypCd eq 'compPhon' }" value="${list.dftCntcTypCd eq 'homePhon' ? list.homePhon : list.compPhon }" elseValue="${list.mbno }"/>
				<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
					<td class="bodybg_ct"><input type="checkbox" value="${list.bcId}" data-bcNm="${list.bcNm }" data-compNm="${list.compNm }" data-dftCntcVal="${dftCntcVal }" data-email="${list.email }"/></td>
					<td class="body_ct"><div class="ellipsis" title="${list.bcNm }"><a href="javascript:viewBc('${list.bcId }');">${list.bcNm }</a></div></td>
					<td class="body_ct"><div class="ellipsis" title="${list.compNm }">${list.compNm }</div></td>
					<td class="body_ct">${dftCntcVal }</td>
					<td class="body_ct"><a href="javascript:parent.mailToPop('${list.email }');" title="<u:msg titleId='or.jsp.viewUserPop.mailToPop' />"><div class="ellipsis" title="${list.email }">${list.email }</div></a></td>
				</tr>
			</c:forEach>
		</c:if>
		</table>
		<u:blank />
		<u:pagination noLeftSelect="true" noTotalCount="true"/>
	</div>
</div>

