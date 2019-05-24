<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
// 초기화
function formReset(){
	
	location.href = "./listMyBull.do?menuId=${menuId}";
	/*
	$('form').each(function(){
	    this.reset();
	    $("#selectCmnt").find("#selectCtId").val(null);
		$("#selectCmnt").find("#selectCt").val(null);
		$("#schCat").val("");
		$("#schWord").val(null);
	  });
	
	var $form = $("#searchForm");
	$form.attr("method","POST");
	$form.attr("action","./listMyBull.do?");
	$form.submit();
	*/
	
}
function setSearchCt(ctId, ctNm){
	$("#selectCmnt").find("#selectCtId").val(ctId);
	$("#selectCmnt").find("#selectCt").val(ctNm);
	dialog.close('findCmPop');
	
	$("#searchForm").attr("method", "post");
	$("#searchForm").attr("action", "./listMyBull.do");
	$("#searchForm").submit();
}

function findCmPop(ctActStat){
	var $findCt = $("#findCmnt").find("input[name='parentSchWord']").val();
	var $schCatVal = $("#searchCt").val();
	var url = './findCmPop.do?menuId=${menuId}&schCat='+$schCatVal+'&schWord='+encodeURIComponent($findCt);
	if(ctActStat != undefined) url+='&ctActStat='+ctActStat;	
	dialog.open('findCmPop','<u:msg titleId="ct.jsp.cm.sel.title" alt="커뮤니티 선택"/>',url);
}


function viewCm(fncUid, ctId) {
	location.href = "./board/listBoard.do?menuId="+fncUid+"&ctId=" + ctId;
}

function viewBull(fncUid, bullId, ctId) {
	location.href = "./board/viewBoard.do?menuId="+fncUid + "&bullId="+bullId + "&ctId=" + ctId;
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ct.jsp.listCmBull.title" alt="커뮤니티 게시물" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form id="searchForm" name="searchForm" action="./listMyBull.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="ct.btn.cmSearch" alt="커뮤니티찾기"/></td>
		<td><select id="searchCt" name="parentSchCat">
			<u:option value="communityOpt" titleId="ct.cols.ctNm" alt="커뮤니티" />
			<u:option value="masterOpt" titleId="cols.mast" alt="마스터" />
			</select></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td id="findCmnt"><u:input id="parentSchWord" name="parentSchWord" value="" titleId="cols.schWord" style="width: 423px;" /></td>
			<td><u:buttonS titleId="ct.btn.cmSearch" alt="커뮤니티찾기" icon="true" onclick="javascript:findCmPop('A');" /></td>
			</tr>
			</tbody></table></td>
		</tr>
		
		<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td id="selectCmnt">
			<u:input id="selectCt" name="selectCt" value="${selectCtNm}" titleId="cols.schWord" style="width: 423px;" readonly="readonly" />
			<u:input id="selectCtId" name="selectCtId" value="${selectCtId}" type="hidden" />
		
		</td>
		</tr>
		
		<tr>
		<td class="search_tit"><u:msg titleId="ct.jsp.listCmBull.title" alt="커뮤니티 게시물" /></td>
		<td style="text-align: right;"><select name="schCat">
			<u:option value="SUBJ" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
			<u:option value="CONT" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
			<u:option value="REGR_NM" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" />
			</select></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td><u:input id="schWord" maxByte="50" name="schWord" value="${param.schWord}" titleId="cols.schWord" style="width: 423px;" /></td>
			<td><u:buttonS titleId="cm.btn.reset" alt="초기화" onclick="javascript:formReset();" /></td>
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
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
	<tr>
	<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.readCnt" alt="조회수" /></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.att" alt="첨부" /></td>
	</tr>
	
	<c:forEach var="ctBullMastVo" items="${ctBullMastList}" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="body_ct">${recodeCount - ctBullMastVo.rnum +1}</td>
			
			<td class="body_lt">
				<div class="ellipsis" title="${ctBullMastVo.ctNm}">
					<a href="javascript:viewCm('${ctBullMastVo.ctFncUid}','${ctBullMastVo.ctId}');"><u:out value="${ctBullMastVo.ctNm}"/></a>
				</div>
			</td>
			<td class="body_lt">
				<div class="ellipsis" title="${ctBullMastVo.subj}">
					<a href="javascript:viewBull('${ctBullMastVo.ctFncUid}','${ctBullMastVo.bullId}','${ctBullMastVo.ctId}');"><u:out value="${ctBullMastVo.subj}"/></a>
				</div>
			</td>
			<td class="body_ct"><a href="javascript:viewUserPop('${ctBullMastVo.regrUid}');">${ctBullMastVo.regrNm}</a></td>
			<td class="body_ct">
				<fmt:parseDate var="dateTempParse" value="${ctBullMastVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			</td>
			<td class="body_ct"><u:out value="${ctBullMastVo.readCnt}" type="number"/></td>
			<td class="body_ct">
				<c:if test="${ctBullMastVo.attYn == 'Y'}">
					<u:icon type="att" />
				</c:if>
				<c:if test="${ctBullMastVo.attYn == 'N'}">
				</c:if>
			</td>
	</c:forEach>
	<c:if test="${fn:length(ctBullMastList) == 0}">
			<tr>
				<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
	</c:if>
	</table>
</div>
<u:blank />
<u:pagination />

