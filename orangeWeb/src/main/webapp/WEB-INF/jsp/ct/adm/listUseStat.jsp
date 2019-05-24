<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	var $form = $('#excelForm');
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};

$(document).ready(function() {
	setUniformCSS();
	
	//달력셋팅
	$("#strtDt").val($("#startDt").val());
	$("#finDt").val($("#endDt").val());
});
//-->
</script>

<u:title titleId="ct.jsp.listUseStat.title" alt="커뮤니티 사용현황" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listUseStat.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.schWord" alt="검색어" /></td>
		<td><select name="schCat">
				<u:option value="communityOpt" titleId="ct.cols.ctNm" alt="커뮤니티" checkValue="${param.schCat}" />
				<u:option value="masterOpt" titleId="ct.cols.mastNm" alt="마스터" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;"/></td>
		</tr>
		
		<tr>
		<td class="search_tit"><u:msg titleId="cols.prd" alt="기간" /></td>
		<td colspan="2"><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td><u:calendar id="strtDt" option="{end:'finDt'}" mandatory="Y" />
				<input name ="startDT" id="startDt" value="${startDt}" type="hidden"/></td>
			</td>
			<td class="body_lt">~</td>
			<td ><u:calendar id="finDt" option="{start:'strtDt'}" mandatory="Y"/>
				<input name ="endDT" id="endDt" value="${endDt}" type="hidden"/></td>
			</td>
			</tr>
			</tbody></table></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<u:listArea id="listArea">
	<tr>
	<td class="head_ct"><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.cls" alt="분류" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mast" alt="마스터" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.createDt" alt="생성일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.stat" alt="상태" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.mbshCnt" alt="회원수" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.vistCnt" alt="방문횟수" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.bullCnt" alt="게시물수" /></td>
	</tr>
	
	<c:forEach var="ctEstbVo" items="${ctEstbList}" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="body_lt"><u:out value="${ctEstbVo.ctNm}"/></td>
			<td class="body_ct">${ctEstbVo.catNm}</td>
			<td class="body_ct"><a href="javascript:viewUserPop('${ctEstbVo.mastUid}');">${ctEstbVo.mastNm}</a></td>
			<td class="body_ct">
				<fmt:parseDate var="dateTempParse" value="${ctEstbVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			</td>
			<td class="body_ct">
				<c:choose>
					<c:when test="${ctEstbVo.ctActStat == 'S'}">
						<u:msg titleId="ct.cols.ready" alt="준비중" />
					</c:when>
					<c:when test="${ctEstbVo.ctActStat == 'A'}">
						<u:msg titleId="ct.cols.act" alt="활동중" />
					</c:when>
					<c:when test="${ctEstbVo.ctActStat == 'C'}">
						<u:msg titleId="ct.cols.close" alt="폐쇄" />
					</c:when>
				</c:choose>
			</td>
			<c:if test="${startDt == null && endDt == null}">
				<td class="body_ct">0 /${ctEstbVo.mbshCnt}</td>
				<td class="body_ct">0 /${ctEstbVo.ctTotalVisitCnt}</td>
				<td class="body_ct">0 /${ctEstbVo.ctTotalBullCnt}</td>
			</c:if>
			<c:if test="${startDt != null || endDt != null}">
				<td class="body_ct">${ctEstbVo.ctTermMbshCnt} /${ctEstbVo.mbshCnt}</td>
				<td class="body_ct">${ctEstbVo.ctTermVisitCnt} /${ctEstbVo.ctTotalVisitCnt}</td>
				<td class="body_ct">${ctEstbVo.ctTermBullCnt} /${ctEstbVo.ctTotalBullCnt}</td>
			</c:if>
		</tr>
	</c:forEach>
	<c:if test="${fn:length(ctEstbList) == 0}">
			<tr>
				<td class="nodata" colspan="8"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
	</c:if>
	
</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
	<u:button titleId="cm.btn.excelDown" alt="엑셀다운" auth="W" href="javascript:excelDownFile();" />
</u:buttonArea>

<form id="excelForm">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
	<u:input type="hidden" id="listPage" value="${listPage}" />
</form>

