<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" />
<script type="text/javascript">
<!--
<% // [목록] 조회 %>
function viewBb(id) {
	location.href = './viewBb.do?${params}&brdId=' + id;
}
<% // [하단버튼:등록] 게시판 등록 %>
function regBb() {
	location.href = './setBb.do?menuId=${menuId}';
}
<% // 메뉴등록 %>
function setMnu(mnuGrpId,valUM) {
	var p = mnuGrpId != undefined ? '&mnuGrpId=' + mnuGrpId : '';
	dialog.open('setBbMnuPop', '<u:msg titleId="bb.jsp.setBbMnuPop.title" alt="서비스 조회" />', './setBbMnuPop.do?menuId=${menuId}&valUM='+valUM+'&' + p);
}

<%// [팝업] 포틀릿 저장 %>
function savePlt(){
	if(validator.validate('setPltPop')){
		var $form = $("#setPltPop");
		$form.attr("action","./transBbPlt.do");
		$form.attr("target","dataframe");
		$form.submit();
	}
};

function setPlt(id) {
	dialog.open('setBbPltPop', '포틀릿 등록', './setBbPltPop.do?menuId=${menuId}&brdId='+ id);
};

<% // 게시판조회 팝업 %>
function listBrdPop(){
	dialog.open('listBrdPop', '게시판조회', './listBrdPop.do?menuId=${menuId}');	
};

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="bb.jsp.listBb.title" alt="게시판목록" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listBb.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 200px;" />
			<u:input type="hidden" id="schCat" value="BRD_NM" /></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.tblNm" alt="테이블명" /></td>
		<td><select name="schTblId">
		<c:if test="${fn:length(baTblBVoList) > 0}">
			<u:option value="" titleId="cm.option.all" alt="전체" checkValue="${param.schTblId}" />
			<c:forEach items="${baTblBVoList}" var="baTblBVo" varStatus="status">
			<u:option value="${baTblBVo.tblId}" title="${baTblBVo.rescNm}" checkValue="${param.schTblId}" />
			</c:forEach>
		</c:if>
			</select></td>
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
	<td class="head_ct"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.knd" alt="종류" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.prd" alt="기간" /></td>
	<td width="28%" class="head_ct"><u:msg titleId="cols.tblNm" alt="테이블명" /></td>
	<td width="14%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
<%-- 	<td width="8%" class="head_ct"><u:msg titleId="cols.fnc" alt="기능" /></td> --%>
	</tr>

<c:if test="${fn:length(baBrdBVoList) == 0}">
	<tr>
	<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baBrdBVoList) > 0}">
	<c:forEach items="${baBrdBVoList}" var="baBrdBVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt"><a href="javascript:viewBb('${baBrdBVo.brdId}');">${baBrdBVo.rescNm}</a></td>
	<td class="body_ct">${baBrdBVo.brdTypNm}</td>
	<td class="body_ct">${baBrdBVo.kndNm}</td>
	<td class="body_ct">${baBrdBVo.tblDispNm}</td>
	<td class="body_ct"><u:out value="${baBrdBVo.regDt}" type="longdate" /></td>
<%-- 	<td><table align="center" border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:buttonS onclick="setPlt('${baBrdBVo.brdId}');" titleId="bb.btn.pltReg" alt="포틀릿등록" auth="A" /></td>
		</tr>
		</tbody></table></td> --%>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="bb.jsp.viewBb.title" alt="게시판 조회" onclick="listBrdPop();" auth="SYS" />
	<c:if test="${mobileEnable == 'Y' }">
		<u:button titleId="bb.btn.mnuMobileReg" alt="모바일등록" onclick="setMnu('${baBrdBVo.brdId}','M');" auth="A" />
	</c:if>
	<u:button titleId="bb.btn.mnuReg" alt="메뉴등록" onclick="setMnu('${baBrdBVo.brdId}','U');" auth="A" />
	<u:button titleId="cm.btn.reg" alt="등록" href="javascript:regBb();" auth="A" />
</u:buttonArea>

