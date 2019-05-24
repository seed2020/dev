<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function viewOpin(debrId, opinOrdr) {
	dialog.open('viewOpinPop','<u:msg titleId="ct.cols.opinView" alt="의견 조회"/>','./viewOpinPop.do?menuId=${menuId}&ctId=${ctId}&debrId='+debrId+'&opinOrdr='+opinOrdr);
}

function setOpin(debrId, opinOrdr, finYn) {
	if(finYn == 'Y'){
		alertMsg("ct.msg.debr.closed"); <% //ct.msg.debr.closed = 이미 마감된 토론입니다. %>
	}else{
		dialog.open('setOpinPop','<u:msg titleId="ct.btn.opinReg" alt="의견 등록"/>','./setOpinPop.do?menuId=${menuId}&ctId=${ctId}&debrId='+debrId+'&opinOrdr='+opinOrdr);
	}
	
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;">
		<tr>
			<td>
				<div class="ellipsis" title="<u:out value="${ctDebrBVo.subj}"/>">
					<u:title title="${menuTitle } - ${ctDebrBVo.subj}" alt="토론실/의견목록 - 토론주제" menuNameFirst="true" />
				</div>
			</td>
		</tr>
	</table>
</div>
<u:blank/>
<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listOpin.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="ctId" name="ctId" value="${ctId}" />
	<u:input type="hidden" id="debrId" name="debrId" value="${debrId}" />
	
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<u:option value="SUBJ" titleId="cols.subj" alt="제목" checkValue="${param.schCat}" />
			<u:option value="CONT" titleId="cols.cont" alt="내용" checkValue="${param.schCat}" />
			<u:option value="REGR_NM" titleId="cols.regr" alt="등록자" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" onkeydown="if (event.keyCode == 13) searchForm.submit();" /></td>
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
			<td width="10%" class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
			<td class="body_lt" style="word-break:break-all; word-wrap:break-word;">
					<u:out value="${ctDebrBVo.subj}"/>
			</td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="ct.cols.itnt" alt="취지" /></td>
			<td class="body_lt" style="word-break:break-all; word-wrap:break-word;">
				<u:out value="${ctDebrBVo.estbItnt}"/>
			</td>
		</tr>
	</table>
</div>
<u:blank/>

<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
		<tr>
			<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
			<td class="head_ct"><u:msg titleId="ct.cols.debrCont" alt="토론내용" /></td>
			<td width="8%"class="head_ct"><u:msg titleId="cols.fna" alt="찬반" /></td>
			<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
			<td width="10%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		<%-- 	<td width="6%" class="head_ct"><u:msg titleId="cols.readCnt" alt="조회수" /></td> --%>
		</tr>
		
		<c:forEach var="opinVo" items="${opinList}" varStatus="status">
			<tr>
				<td class="body_ct" rowspan="2">${recodeCount - opinVo.rnum + 1 }</td>
				<td class="body_lt">
					<div class="ellipsis" title="<u:out value="${opinVo.subj}"/>">
						<a href="javascript:viewOpin('${ctDebrBVo.debrId}','${opinVo.opinOrdr}');"><u:out value="${opinVo.subj}"/></a>
					</div>
				</td>
				<td class="body_ct">
					<c:choose>
						<c:when test="${opinVo.prosConsCd == 'A'}">
							<u:msg titleId="ct.option.for" alt="친상" />
						</c:when>
						<c:when test="${opinVo.prosConsCd == 'O'}">
							<u:msg titleId="ct.option.against" alt="반대" />
						</c:when>
						<c:when test="${opinVo.prosConsCd == 'E'}">
							<u:msg titleId="ct.option.etc" alt="기타" />
						</c:when>
					</c:choose>
				</td>
				<td class="body_ct"><a href="javascript:viewUserPop('${opinVo.regrUid}');">${opinVo.regrNm}</a></td>
				<td class="body_ct">
					<fmt:parseDate var="dateTempParse" value="${opinVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
				</td>
	<%-- 			<td class="body_ct">${opinVo.readCnt}</td> --%>
			</tr>
			<tr>
				<td colspan="4" class="body_lt">
					<div style="overflow:auto;height:120px;" class="editor">${opinVo.opin}</div>
				</td>
			</tr>
		</c:forEach>
		<c:if test="${fn:length(opinList) == 0 }">
			<tr>
				<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>	
		</c:if>
	</table>
</div>
<u:blank/>	

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<c:choose>
		<c:when test="${!empty myAuth && myAuth == 'M' }">
			<u:button titleId="ct.btn.opinReg" alt="의견등록" onclick="setOpin('${ctDebrBVo.debrId}', '', '${ctDebrBVo.finYn}');" />
		</c:when>
		<c:otherwise>
			<c:if test="${!empty authChkW && authChkW == 'W' }">
				<u:button titleId="ct.btn.opinReg" alt="의견등록" onclick="setOpin('${ctDebrBVo.debrId}', '', '${ctDebrBVo.finYn}');" />
			</c:if>
		</c:otherwise>
	</c:choose>
	<u:button titleId="ct.btn.debrList" alt="토론실목록" href="./listDebr.do?menuId=${menuId}&ctId=${ctId}" />
	
</u:buttonArea>
