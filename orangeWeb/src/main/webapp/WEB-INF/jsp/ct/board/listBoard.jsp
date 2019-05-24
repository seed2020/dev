<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<!--
<% // [목록:조회수] 조회이력 %>
function readHst(id) {
	dialog.open('listReadHstPop','<u:msg titleId="bb.jsp.listReadHstPop.title" alt="조회자 정보" />','./listReadHstPop.do?menuId=${menuId}&ctId=${ctId}&bullId=' + id);
}


// 게시글 등록
function regBoard(){
	var $menuId = $("#bullMenuId").val();
	var $ctId= $("#bullCtId").val();
	
	var $form = $("#boardInfoForm");
	$form.attr("method", "POST");
	$form.attr("action", "./setBoard.do?menuId="+$menuId+"&ctId="+$ctId);
	$form.submit();
}

//function view() {
//	location.href = "./viewBoard.do?menuId=${menuId}&ctId=${ctBullVo.ctId}&bullId=${ctBullVo.bullId}";
//}
<% // [팝업] 파일목록 조회 %>
function viewFileListPop(id) {
	dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />','./viewFileListPop.do?menuId=${menuId}&bullId='+id);
}
$(document).ready(function() {
	setUniformCSS();
	
	//달력셋팅
	$("#strtDt").val($("#startDt").val());
	$("#finDt").val($("#endDt").val());
});
//-->
</script>

<u:title title="${menuTitle }" alt="게시판" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listBoard.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="ctId" name="ctId" value="${ctId}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<u:option value="SUBJ" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
			<u:option value="CONT" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
			<u:option value="REGR_NM" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="" titleId="cols.schWord" style="width: 400px;" /></td>
		<td class="width20"></td>
		
		<!-- 등록일시 -->
		<td class="search_tit"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
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
<form id="boardInfoForm">
	<div class="listarea">
		<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
			<tr>
			<td width="5%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
			<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
			<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
			<td width="18%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
			<td width="6%" class="head_ct"><u:msg titleId="cols.recmdCnt" alt="추천수" /></td>
			<td width="6%" class="head_ct"><u:msg titleId="cols.readCnt" alt="조회수" /></td>
			<td width="6%" class="head_ct"><u:msg titleId="cols.att" alt="첨부" />
				<input id="bullMenuId" name="bullMenuId" type="hidden" value="${menuId}"/>
				<input id="bullCtId" name="bullCtId" type="hidden" value="${ctFncDVo.ctId}"/>
				<input id="bullCtFncId" name="bullCtFncId" type="hidden" value="${ctFncDVo.ctFncId }"/>
				<input id="bullCtFncUid" name="bullCtFncUid" type="hidden" value="${ctFncDVo.ctFncUid}"/>
				<input id="bullCtFncPid" name="bullCtFncPid" type="hidden" value="${ctFncDVo.ctFncPid }"/>
				<input id="bullCtFncLocStep" name="bullCtFncLocStep" type="hidden" value="${ctFncDVo.ctFncLocStep}"/>
				<input id="bullCtFncOrdr" name="bullCtFncOrdr" type="hidden" value="${ctFncDVo.ctFncOrdr}"/>
			
			</td>
			</tr>
			
			<c:forEach var="ctBullVo" items="${ctBullList}" varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<td class="body_ct">${recodeCount - ctBullVo.rnum + 1}</td>
				<td class="body_lt">
					<div class="ellipsis" title="<u:out value="${ctBullVo.subj}"/>">
						<u:icon type="indent" display="${ctBullVo.replyDpth > 0}" repeat="${ctBullVo.replyDpth - 1}" />
						<u:icon type="reply" display="${ctBullVo.replyDpth > 0}" />
						<u:icon type="new" display="${ctBullVo.newYn == 'Y'}"/>
						<a href="./viewBoard.do?menuId=${menuId}&ctId=${ctBullVo.ctId}&bullId=${ctBullVo.bullId}"><u:out value="${ctBullVo.subj}"/></a>
					</div>
				</td>
				<td class="body_ct"><a href="javascript:viewUserPop('${ctBullVo.regrUid}');"><u:out value="${ctBullVo.regrNm}"/></a></td>
				<td class="body_ct">
					<fmt:parseDate var="dateTempParse" value="${ctBullVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td class="body_ct">${ctBullVo.recmdCnt}</td>
				<td class="body_ct">
					<a href="javascript:readHst('${ctBullVo.bullId}');">${ctBullVo.readCnt}</a>
				</td>
				<td class="body_ct">
					<c:if test="${ctBullVo.attYn == 'Y'}">
						<a href="javascript:viewFileListPop('${ctBullVo.bullId }');"><u:icon type="att" /></a>
					</c:if>
				</td>
			</c:forEach>
			<c:if test="${fn:length(ctBullList) == 0 }">
				<tr>
					<td class="nodata" colspan="7"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>	
			</c:if>
			
		</table>
	</div>
</form>
<u:blank/>
<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<c:if test="${!empty authChkW && authChkW == 'W' }">
		<u:button titleId="cm.btn.reg" alt="등록" onclick="javascript:regBoard();" />
	</c:if>
<%-- 	<u:button titleId="cm.btn.reg" alt="등록" href="./setBoard.do?menuId=${menuId}" auth="W" /> --%>
</u:buttonArea>