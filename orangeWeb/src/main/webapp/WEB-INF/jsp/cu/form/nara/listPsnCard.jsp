<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="paramsForList" excludes="menuId, cardNo"/>
<script type="text/javascript">
<!--
<% // [하단버튼:삭제] %>
function delPsnCard() {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./transPsnCardDelAjx.do?menuId=${menuId}', {cardNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
};

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title menuNameFirst="true"/>

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listPsnCard.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">			
			<u:option value="koNm" title="성명(한글)" alt="성명(한글)" checkValue="${param.schCat}" />
			<u:option value="compNm" title="소속법인" alt="소속법인" checkValue="${param.schCat}" />
			<u:option value="deptNm" title="부서명" alt="부서명" checkValue="${param.schCat}" />
			<u:option value="teamNm" title="팀명" alt="팀명" checkValue="${param.schCat}" />
			<u:option value="ein" title="사번" alt="사번" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<c:if test="${isAdmin == true }"><c:set var="auth" value="A"/></c:if>
<u:set var="colspan" test="${!empty auth && auth eq 'A' }" value="3%,15%,*,25%,20%,15%" elseValue="15%,*,25%,20%,15%"/>
<u:listArea id="listArea" colgroup="${colspan }">	
	<tr id="headerTr">
		<c:if test="${!empty auth && auth eq 'A' }"><td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td></c:if>
		<th class="head_ct">사원번호</th>
		<th class="head_ct">성명(한글)</th>
		<th class="head_ct">소속법인</th>
		<th class="head_ct">부서명</th>
		<th class="head_ct">등록일자</th>
	</tr>
	<c:if test="${fn:length(cuPsnCardBVoList) == 0}">
		<tr>
		<td class="nodata" colspan="${empty auth ? 5 : 6 }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(cuPsnCardBVoList)>0}">
		<c:forEach items="${cuPsnCardBVoList}" var="cuPsnCardBVo" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<c:if test="${!empty auth && auth eq 'A' }"><td class="bodybg_ct"><input type="checkbox" value="${cuPsnCardBVo.cardNo }" /></td></c:if>
				<td class="body_ct"><u:out value="${cuPsnCardBVo.ein }"/></td>
				<td class="body_ct"><a href="./viewPsnCard.do?${paramsForList}&cardNo=${cuPsnCardBVo.cardNo }"><u:out value="${cuPsnCardBVo.koNm }"/></a></td>
				<td class="body_ct"><u:out value="${cuPsnCardBVo.compNm }"/></td>
				<td class="body_ct"><u:out value="${cuPsnCardBVo.deptNm }"/></td>
				<td class="body_ct"><u:out value="${cuPsnCardBVo.regDt }" type="date"/></td>
			</tr>
		</c:forEach>	
	</c:if>
	
</u:listArea>
<u:pagination />

<c:if test="${isAdmin == true }">
<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.write" alt="등록" href="./setPsnCard.do?menuId=${menuId}" auth="A" />
<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delPsnCard();" />
</u:buttonArea>
</c:if>
