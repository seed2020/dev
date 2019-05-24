<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

//게시글 등록
function regBoard(){
	var $menuId = $("#bullMenuId").val();
	var $ctId= $("#bullCtId").val();
	
	var $form = $("#pdsInfoForm");
	$form.attr("method", "POST");
	$form.attr("action", "./setPds.do?menuId="+$menuId+"&ctId="+$ctId);
	$form.submit();
}

//function view() {
//	location.href = "./viewPds.do?menuId=${menuId}";
//}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ct.jsp.listPds.title" alt="자료실" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listPds.do" >
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
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<form id="pdsInfoForm">
	<u:listArea id="listArea">
		<tr>
		<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
		<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
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
		
		<c:forEach var="ctRecVo" items="${ctRecList}" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<td class="body_ct">${recodeCount - ctRecVo.rnum + 1}</td>
				<td class="body_lt">
					<u:icon type="indent" display="${ctRecVo.replyDpth > 0}" repeat="${ctRecVo.replyDpth - 1}" />
					<u:icon type="reply" display="${ctRecVo.replyDpth > 0}" />
					<u:icon type="new" display="${ctRecVo.newYn == 'Y'}"/>
					<a href="./viewPds.do?menuId=${menuId}&ctId=${ctRecVo.ctId}&bullId=${ctRecVo.bullId}">${ctRecVo.subj}</a>
				</td>
				<td class="body_ct"><a href="javascript:viewUserPop('${ctRecVo.regrUid}');"><u:out value="${ctRecVo.regrNm}"/></a></td>
				<td class="body_ct">
					<fmt:parseDate var="dateTempParse" value="${ctRecVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
				</td>
				<td class="body_ct">${ctRecVo.readCnt}</td>
				<td class="body_ct">
					<c:if test="${ctRecVo.attYn == 'Y'}">
						<u:icon type="att" />
					</c:if>
				</td>
		</c:forEach>
		<c:if test="${fn:length(ctRecList) == 0 }">
				<tr>
					<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>	
		</c:if>
	<!-- 
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="body_ct">2</td>
		<td class="body_lt"><a href="javascript:view();">Java Language Reference Guide</a></td>
		<td class="body_ct"><a href="javascript:viewUserPop('U0000003');">이채린</a></td>
		<td class="body_ct">2014-01-17</td>
		<td class="body_ct">1</td>
		<td class="body_ct"><u:icon type="att" /></td>
		</tr>
		
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="body_ct">1</td>
		<td class="body_lt"><a href="javascript:view();">Java Virtual Machine Specification</a></td>
		<td class="body_ct"><a href="javascript:viewUserPop('U0000003');">이채린</a></td>
		<td class="body_ct">2013-11-10</td>
		<td class="body_ct">1</td>
		<td class="body_ct"><u:icon type="att" /></td>
		</tr>
	 -->
	</u:listArea>
</form>
<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록" onclick="javascript:regBoard();" auth="W" />
<%-- 	<u:button titleId="cm.btn.reg" alt="등록" href="./setPds.do?menuId=${menuId}" auth="W" /> --%>
</u:buttonArea>