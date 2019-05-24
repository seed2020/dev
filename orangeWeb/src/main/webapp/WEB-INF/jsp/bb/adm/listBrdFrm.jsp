<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
function cpClipbrd(val){
	if(browser.ie){
		window.clipboardData.setData("Text",val);
		// bb.msg.copy.clipboard=클립보드에 복사되었습니다.
		alertMsg('bb.msg.copy.clipboard');
	}else{
		// bb.msg.copy.prompt=게시판의 Url 입니다.Ctrl+C를 눌러 클립보드로 복사하세요.
		temp = prompt(callMsg('bb.msg.copy.prompt'),val);
	}
	
};
$(document).ready(function() {
	setUniformCSS();
});
</script>
<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listBrdFrm.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.comp" alt="회사" /></td>
		<td>
			<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> >
				<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
					<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${compId}"/>
				</c:forEach>
			</select>
		</td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 200px;" />
			<u:input type="hidden" id="schCat" value="BRD_NM" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<div id="listArea" class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="36%"/><col width="*"/><col width="20%"/><col width="10%"/></colgroup>
	<tr id="headerTr">
		<td class="head_ct"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
		<td class="head_ct"><u:msg titleId="cols.url" alt="URL" /></td>
		<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		<td class="head_ct"><u:msg titleId="cols.note" alt="비고" /></td>
	</tr>
	<c:choose>
		<c:when test="${!empty baBrdBVoList}">
			<c:forEach items="${baBrdBVoList}" var="baBrdBVo" varStatus="status">
				<tr>
					<td class="bodybg_lt">${baBrdBVo.rescNm}</td>
					<td class="body_lt">/bb/listBull.do?brdId=${baBrdBVo.brdId}</td>
					<td class="body_ct"><u:out value="${baBrdBVo.regDt}" type="longdate" /></td>
					<td class="body_ct"><u:buttonS href="javascript:;"  titleId="cm.btn.copy" alt="복사" onclick="cpClipbrd('/bb/listBull.do?brdId=${baBrdBVo.brdId}');" /></td>
				</tr>
			</c:forEach>
			</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
	</table>
</div>
<u:blank />
<u:pagination noTotalCount="true" />
