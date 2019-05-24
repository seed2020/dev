<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="paramsForList" excludes="menuId, cardNo"/>
<script type="text/javascript">
<!--
<% // [하단버튼:삭제] %>
function delDrivRec() {
	var arr = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (arr != null && confirmMsg("cm.cfrm.del")) {<% // 삭제하시겠습니까? %>
		callAjax('./transDrivRecDelAjx.do?menuId=${menuId}', {recNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
};<% // [콤보박스] - 법인선택시 차종목록 조회 %>
function setSelectCorpNo(){
	$('#corpNo').change(function(){
 		$('#carKndNo').find('option').each(function(){
 			$(this).remove();
 		});
 		$('#carKndNo').append('<u:option value="" titleId="wa.cols.carKndNm" alt="차종"/>');
 		$("#carKndNo option:eq(0)").attr("selected", "selected"); 
 		$('#carKndNo').uniform();
 		if($(this).val() != ''){
 			callAjax('./getCarKndListAjx.do?menuId=${menuId}', {corpNo:$(this).val()}, function(data) {
 				if (data.message != null) {
 					alert(data.message);
 					return;
 				}
 				if(data.waCarKndLVoList != null){
 					$.each(data.waCarKndLVoList , function(index, carVo) {
 						$vo=carVo.map;
 		        		$('#carKndNo').append('<u:option value="'+$vo.carKndNo+'" title="'+$vo.carKndNm+'"/>');
 		        	});
 				}
 			});
 		}
 	});
}

$(document).ready(function() {
	setUniformCSS();
	
	setSelectCorpNo();
});
//-->
</script>

<u:title titleId="wa.title.car.mng" menuNameFirst="true" alt="업무용승용차관리" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listDrivRec.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select id="corpNo" name="corpNo" style="min-width:100px;"><u:option value="" titleId="wa.jsp.corp.title" alt="법인" checkValue="${param.corpNo}" selected="${empty param.corpNo }"/>
			<c:forEach var="waCorpBVo" items="${waCorpBVoList }" varStatus="status">
				<u:option value="${waCorpBVo.corpNo }" title="${waCorpBVo.corpNm }" alt="${waCorpBVo.corpNm }" checkValue="${param.corpNo}" />
			</c:forEach></select></td>
		<td><select id="carKndNo" name="carKndNo" style="min-width:150px;"><u:option value="" titleId="wa.cols.carKndNm" alt="차종" checkValue="${param.carKndNo}" selected="${empty param.carKndNo }"
		/><c:if test="${!empty waCarKndLVoList }"><c:forEach var="waCarKndLVo" items="${waCarKndLVoList }" varStatus="status"
		><u:option value="${waCarKndLVo.carKndNo }" title="${waCarKndLVo.carKndNm }" alt="${waCarKndLVo.carKndNm }" checkValue="${param.carKndNo}" /></c:forEach>
		</c:if></select></td>
		<td class="width20"></td>
		<!-- 등록일시 -->
		<td class="search_tit"><u:msg titleId="wa.cols.corp.year" alt="사업연도" /></td>
		<td><u:input type="hidden" id="durCat" value="useYmd" />
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="durStrtDt" option="{end:'durEndDt'}" value="${param.durStrtDt}" /></td>
			<td class="search_body_ct"> ~ </td>
			<td><u:calendar id="durEndDt" option="{start:'durStrtDt'}" value="${param.durEndDt}" /></td>
			</tr>
			</table></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<u:secu auth="A"><c:set var="auth" value="A"/></u:secu>
<u:set var="colspan" test="${!empty auth && auth eq 'A' }" value="3%,15%,*,25%,20%,15%" elseValue="15%,*,25%,20%,15%"/>
<u:listArea id="listArea" colgroup="${colspan }">	
	<tr id="headerTr">
		<c:if test="${!empty auth && auth eq 'A' }"><td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td></c:if>
		<th class="head_ct">사업연도</th>
		<th class="head_ct">차종</th>
		<th class="head_ct">자동차등록번호</th>
		<th class="head_ct">등록일자</th>
	</tr>
	<c:if test="${fn:length(waDrivRecBVoList) == 0}">
		<tr>
		<td class="nodata" colspan="${empty auth ? 4 : 5 }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(waDrivRecBVoList)>0}">
		<c:forEach items="${waDrivRecBVoList}" var="waDrivRecBVo" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<c:if test="${!empty auth && auth eq 'A' }"><td class="bodybg_ct"><input type="checkbox" value="${waDrivRecBVo.recNo }" /></td></c:if>
				<td class="body_ct"><a href="./viewDrivRec.do?${paramsForList}&recNo=${waDrivRecBVo.recNo }"><u:out value="${waDrivRecBVo.strtDt }" type="date"/>~<u:out value="${waDrivRecBVo.endDt }" type="date"/></a></td>
				<td class="body_ct"><a href="./viewDrivRec.do?${paramsForList}&recNo=${waDrivRecBVo.recNo }"><u:out value="${waDrivRecBVo.carKndNm }"/></a></td>
				<td class="body_ct"><u:out value="${waDrivRecBVo.carRegNo }"/></td>
				<td class="body_ct"><u:out value="${waDrivRecBVo.regDt }" type="date"/></td>
			</tr>
		</c:forEach>	
	</c:if>
	
</u:listArea>
<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
<u:button titleId="cm.btn.write" alt="등록" href="./setDrivRec.do?menuId=${menuId}" auth="W" />
<c:if test="${!empty auth && auth eq 'A' }"><u:button titleId="cm.btn.del" alt="삭제" href="javascript:delDrivRec();" /></c:if>
</u:buttonArea>
