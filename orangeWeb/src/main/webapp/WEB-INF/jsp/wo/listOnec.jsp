<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%


%><script type="text/javascript">
<!--
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<u:title alt="원카드 목록 / 원카드 관리" menuNameFirst="true" />

<%-- // 검색영역 --%>
<u:searchArea style="position:relative; z-index:2;">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="cat" value="${param.cat}" /><c:if
		test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit">Item</td>
		<td><u:input id="itemNm" title="Item" value="${param.itemNm}" /></td>
		<td style="width:25px;"></td>
		
		<td class="search_tit"><u:msg titleId="wo.clsCd.ONEC_TYP_CD" alt="분류" /></td>
		<td><select name="onecTypCd" id="onecTypCd">
			<u:option value="" title="" /><c:forEach items="${ONEC_TYP_CDList}" var="cdVo"><c:if test="${cdVo.useYn ne 'N'}">
		<u:option value="${cdVo.cd}" title="${cdVo.cdVa}" checkValue="${param.onecTypCd}" /></c:if></c:forEach></select></td>
		<td style="width:25px;"></td>
		
		<td class="search_tit">Revised date</td>
		<td>
		<u:calendar
				titleId="cm.cal.startDd" alt="시작일자"
				id="minRevsDt" value="${param.minRevsDt}" option="{end:'maxRevsDt'}" /></td>
		<td style="padding-left:8px; padding-right:5px;">~</td>
		<td><u:calendar
				titleId="cm.cal.endDd" alt="종료일자"
				id="maxRevsDt" value="${param.minRevsDt}" option="{start:'minRevsDt'}" /></td>
		<td style="width:25px;"></td>
		
		</tr>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<%-- 원카드 목록 (자기것 + 공유받은것) --%>
<c:if test="${param.cat eq 'list'}">
<div style="width:100%;">
<div style="float:left; width:58%; height:440px;">
<u:title titleId="wo.allList" alt="전체 목록" type="small" />

<u:listArea colgroup="35%,15%,15%,20%,15%">
<tr><%--
	<th class="head_ct">Item <span class="updown">▲</span></th> --%>
	<th class="head_ct">Item</th>
	<th class="head_ct">Issued</th>
	<th class="head_ct">Revised</th>
	<th class="head_ct">Frequency</th>
	<th class="head_ct">Holder</th>
</tr><c:if test="${empty woOnecBVoList}">
<tr>
	<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
</tr>
</c:if><c:forEach items="${woOnecBVoList}" var="woOnecBVo">
<tr>
	<td class="body_lt"><a href="./viewOnec.do?cat=${param.cat}&onecNo=${woOnecBVo.onecNo}&menuId=${menuId}"><u:out value="${woOnecBVo.itemNm}" /></a></td>
	<td class="body_ct"><u:out value="${woOnecBVo.issDt}" type="shortdate" /></td>
	<td class="body_ct"><u:out value="${woOnecBVo.revsDt}" type="shortdate" /></td>
	<td class="body_ct"><c:if test="${not empty woOnecBVo.freqCd}"><u:msg titleId="wo.freqCd.${woOnecBVo.freqCd}" /></c:if></td>
	<td class="body_ct"><c:if test="${not empty woOnecBVo.holdrUid}"><a href="javascript:viewUserPop('${woOnecBVo.holdrUid}')"><u:out value="${woOnecBVo.holdrNm}" /></a></c:if></td>
</tr>
</c:forEach>
</u:listArea>
</div>

<div style="float:right; width:40%; height:440px; overflow-y:auto; overflow-x:show">
<u:title titleId="wo.thisMonList" alt="당월 보고" type="small" />

<u:listArea colgroup="50%,30%,20%">
<tr>
	<th class="head_ct">Item</th>
	<th class="head_ct">Frequency</th>
	<th class="head_ct">Status</th>
</tr><c:if test="${empty freqList}">
<tr>
	<td class="nodata" colspan="3"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
</tr>
</c:if><c:forEach items="${freqList}" var="woOnecBVo">
<tr>
	<td class="body_lt"><a href="./${woOnecBVo.statCd eq 'askApv' ? 'viewOnec' : 'setOnec'}.do?cat=${param.cat}&onecNo=${woOnecBVo.onecNo}&menuId=${menuId}"><u:out value="${woOnecBVo.itemNm}" /></a></td>
	<td class="body_ct"><c:if test="${not empty woOnecBVo.freqCd}"><u:msg titleId="wo.freqCd.${woOnecBVo.freqCd}" /></c:if></td>
	<td class="body_ct"><c:if test="${not empty woOnecBVo.statCd}"><u:msg titleId="wo.statCd.${woOnecBVo.statCd}" /></c:if></td>
</tr>
</c:forEach>

</u:listArea>

</div>
</div>
</c:if>

<%-- 원카드 변경 관리 : 자기것 --%>
<c:if test="${param.cat ne 'list'}">
<u:listArea colgroup="27%,10%,12%,12%,12%,15%,12%">
<tr>
	<th class="head_ct">Item</th>
	<th class="head_ct"><u:msg titleId="wo.clsCd.ONEC_TYP_CD" alt="분류" /></th>
	<th class="head_ct">Frequency</th>
	<th class="head_ct">Issued</th>
	<th class="head_ct">Revised</th>
	<th class="head_ct">Saved</th><c:if
		test="${param.cat eq 'modify'}">
	<th class="head_ct">Status</th></c:if><c:if
		test="${param.cat ne 'modify'}">
	<th class="head_ct">Holder</th></c:if>
	<%-- <th class="head_ct">Holder</th> --%>
</tr><c:if test="${empty woOnecBVoList}">
<tr>
	<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
</tr>
</c:if><c:forEach items="${woOnecBVoList}" var="woOnecBVo">
<tr>
	<td class="body_lt"><a href="./${param.cat ne 'all' and (woOnecBVo.statCd eq 'temp' or woOnecBVo.statCd eq 'modify') ? 'setOnec.do' : 'viewOnec.do'}?cat=${param.cat}&onecNo=${woOnecBVo.onecNo}&menuId=${menuId}"><u:out value="${woOnecBVo.itemNm}" /></a></td>
	<td class="body_ct"><c:if test="${not empty woOnecBVo.onecTypCd}"><u:convertMap srcId="ONEC_TYP_CDMap" attId="${woOnecBVo.onecTypCd}" /></c:if></td>
	<td class="body_ct"><c:if test="${not empty woOnecBVo.freqCd}"><u:msg titleId="wo.freqCd.${woOnecBVo.freqCd}" /></c:if></td>
	<td class="body_ct"><u:out value="${woOnecBVo.issDt}" type="date" /></td>
	<td class="body_ct"><u:out value="${woOnecBVo.revsDt}" type="date" /></td>
	<td class="body_ct"><u:out value="${woOnecBVo.regDt}" type="longdate" /></td><c:if
		test="${param.cat eq 'modify'}">
	<td class="body_ct"><c:if test="${not empty woOnecBVo.statCd}"><u:msg titleId="wo.statCd.${woOnecBVo.statCd}" /></c:if></c:if><c:if
		test="${param.cat ne 'modify'}">
	<td class="body_ct"><u:out value="${woOnecBVo.holdrNm}" /></td></c:if>
	<%-- <td class="body_ct"><c:if test="${not empty woOnecBVo.holdrUid}"><a href="javascript:viewUserPop('${woOnecBVo.holdrUid}')"><u:out value="${woOnecBVo.holdrNm}" /></a></c:if></td> --%>
</tr>
</c:forEach>
</u:listArea>

</c:if>

<u:buttonArea><c:if
		test="${param.cat eq 'list'}" >
	<u:button titleId="cm.btn.write" alt="등록" href="./setOnec.do?menuId=${menuId}&cat=${param.cat}" auth="W" /></c:if>
</u:buttonArea>
