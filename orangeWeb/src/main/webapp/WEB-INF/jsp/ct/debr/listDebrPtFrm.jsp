<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function send(url){
	parent.view(url);
	
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<form id="listBoardPtForm">
	<input type="hidden" id="fncUid" name="fncUid" value="${fncUid}" />
	<input type="hidden" id="ctId" name="ctId" value="${ctId}" />
	<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
	
	
	<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
		<tbody>
			<tr>
				<td width="50%" class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
				<td width="15%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
				<td width="25%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
				<td width="10%" class="head_ct"><u:msg titleId="cols.sitn" alt="회기" /></td>
			</tr>
			
			<c:forEach  var="ctDebrVo" items="${ctDebrList}" varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_lt">
						<div class="ellipsis" title="<u:out value="${ctDebrVo.subj}" />">
							<a href="javascript:send('./listOpin.do?menuId=${menuId}&ctId=${ctDebrVo.ctId}&debrId=${ctDebrVo.debrId}')"><u:out value="${ctDebrVo.subj}" /></a>
						</div>
					</td>
					<td class="body_ct"><a href="javascript:parent.viewUserPop('${ctDebrVo.regrUid}');">${ctDebrVo.regrNm}</a></td>
					<td class="body_ct">
						<fmt:parseDate var="dateTempParse" value="${ctDebrVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td class="body_ct">
						<c:if test="${ctDebrVo.finYn == 'Y'}">
							<u:msg titleId="ct.cols.finish" alt="마감"/>
						</c:if>
						<c:if test="${ctDebrVo.finYn == 'N'}">
							${ctDebrVo.sitn}<u:msg titleId="ct.cols.week" alt="주"/>
						</c:if>
						
					</td>
				</tr>
			</c:forEach>
			<c:if test="${fn:length(ctDebrList) == 0}">
				<tr>
				<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>
			</c:if>
		</tbody>
	</table>
</form>
<u:listArea/>
<u:pagination noTotalCount="true"/>
<div id="baordPt" ></div>