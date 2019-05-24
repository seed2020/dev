<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // [목록링크] 테이블 상세조회 %>
function listTblBb(id) {
	dialog.open('listTblBbPop','<u:msg titleId="bb.jsp.listTblBbPop.title" alt="테이블 상세조회" />','./listTblBbPop.do?menuId=${menuId}&tblId=' + id);
}
<% // [하단버튼:등록] 테이블 등록 %>
function regTbl() {
	dialog.open('setTblPop','<u:msg titleId="bb.jsp.listTbl.reg.title" alt="테이블 등록" />','./setTblPop.do?menuId=${menuId}');
}
<% // [목록버튼:수정] 테이블 수정 %>
function modTbl(id) {
	dialog.open('setTblPop','<u:msg titleId="bb.jsp.listTbl.mod.title" alt="테이블 수정" />','./setTblPop.do?menuId=${menuId}&tblId=' + id);
}
<% // [목록버튼:삭제] 테이블 삭제 %>
function delTbl(id) {
	if(confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transTblDelAjx.do?menuId=${menuId}', {tblId:id}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="bb.jsp.listTbl.title" alt="테이블 목록" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listTbl.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<u:option value="TBL_DISP_NM" titleId="cols.tblNm" alt="테이블명" checkValue="${param.schCat}" />
			<u:option value="TBL_NM" titleId="cols.dbTblNm" alt="DB테이블명" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" /></td>
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
	<td width="20%" class="head_ct"><u:msg titleId="cols.dbTblNm" alt="DB테이블명" /></td>
	<td class="head_ct"><u:msg titleId="cols.tblNm" alt="테이블명" /></td>
	<td width="14%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.exYn" alt="확장여부" /></td>
	<td width="12%" class="head_ct"><u:msg titleId="cols.fnc" alt="기능" /></td>
	</tr>

<c:if test="${fn:length(baTblBVoList) == 0}">
	<tr>
	<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baTblBVoList) > 0}">
	<c:forEach items="${baTblBVoList}" var="baTblBVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt">BB_${baTblBVo.tblNm}_L</td>
	<td class="body_lt"><a href="javascript:listTblBb('${baTblBVo.tblId}');">${baTblBVo.rescNm}</a></td>
	<td class="body_ct"><u:out value="${baTblBVo.regDt}" type="date" /></td>
	<td class="body_ct"><u:yn value="${baTblBVo.exYn}" yesId="bb.cols.ex" noId="bb.cols.def" alt="확장/기본" /></td>
	<td><table align="center" border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:buttonS href="javascript:modTbl('${baTblBVo.tblId}');" titleId="cm.btn.mod" alt="수정" auth="A" /></td>
		<td><u:buttonS href="javascript:delTbl('${baTblBVo.tblId}');" titleId="cm.btn.del" alt="삭제" auth="A" /></td>
		</tr>
		</tbody></table></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:regTbl();" auth="A" />
</u:buttonArea>

