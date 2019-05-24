<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
//-->
</script>

<div style="width:650px">
<u:title titleId="pt.jsp.listPlt.title" alt="포틀릿 설정" />

<%// 검색영역 %>
<u:searchArea>
	<form id="searchForm" name="searchForm" action="./listPltPop.do" onsubmit="searchPlt(); return false;">
	<input type="hidden" name="menuId" value="${menuId}" />
	<input type="hidden" name="schCat" value="pltNm" />
	<input type="hidden" name="zoneCd" value="${param.zoneCd}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr><u:decode srcId="_lang" tgtValue="ko" var="cdLoc" value="공통코드 : 포털 / 포털카테고리 / 포틀릿카테고리" elseValue="" />
		<td class="search_tit" ><u:msg titleId="cols.pltCat" alt="포틀릿카테고리" /></td>
		<td><select name="pltCatCd" <u:elemTitle titleId="cols.pltCat" />>
			<u:option value="" titleId="cm.option.all" alt="전체" checkValue="${param.pltCatCd}" />
			<c:forEach items="${pltCatCdList}" var="pltCatCd" varStatus="status">
			<u:option value="${pltCatCd.cd}" title="${pltCatCd.rescNm}" checkValue="${param.pltCatCd}" />
			</c:forEach></select></td>
		<td class="width10"></td>
		<td class="search_tit"><u:msg titleId="cols.pltNm" alt="포틀릿명" /></td>
		<td><u:input id="schWord" titleId="cm.schWord" style="width:200px;" value="${param.schWord}" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:searchPlt();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<%// 목록 %>
<u:listArea id="listArea">

	<tr>
	<td width="4%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.pltCat" alt="포틀릿카테고리" /></td>
	<td class="head_ct"><u:msg titleId="cols.pltNm" alt="포틀릿명" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="pt.cols.openScop" alt="공개범위" /></td>
	<td width="15%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>

<c:if test="${fn:length(ptPltDVoList)==0}" >
	<tr>
	<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(ptPltDVoList) >0}" >
	<c:forEach items="${ptPltDVoList}" var="ptPltDVo" varStatus="status">
	<tr>
	<td class="bodybg_ct"><input type="checkbox" value="${ptPltDVo.pltId}" data-rescId="${ptPltDVo.rescId}" data-rescNm="<u:out value='${ptPltDVo.rescNm}' type="value" />" data-wdthPx="${ptPltDVo.wdthPx}"  data-hghtPx="${ptPltDVo.hghtPx}"/></td>
	<td class="body_ct"><u:out value="${ptPltDVo.pltCatNm}" /></td>
	<td class="body_lt"><u:out value="${ptPltDVo.rescNm}" /></td>
	<td class="body_ct"><u:out value="${ptPltDVo.openCompNm}" /></td>
	<td class="body_ct"><u:out value="${ptPltDVo.regDt}" type="date" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination onPageChange="onPageChange(pageNo)" noTotalCount="true" />

<u:msg titleId="pt.jsp.listPlt.reg" var="addTitle" alt="포틀릿 등록" />
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:setCheckedPlt('${param.zoneCd}');" auth="${empty saveAuth ? 'A' : saveAuth}" />
	<u:button titleId="cm.btn.cancel" alt="취소" onclick="dialog.close(this);" />
</u:buttonArea>
</div>