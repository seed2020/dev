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
			</tr>
			
			<c:forEach  var="ctSiteVo" items="${ctSiteList}" varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_lt">
						<div class="ellipsis" title="<u:out value="${ctSiteVo.subj}"/>">
							<a href="javascript:send('./viewSite.do?menuId=${menuId}&ctId=${ctSiteVo.ctId}&siteId=${ctSiteVo.siteId}&catId=${ctSiteVo.catId}')"><u:out value="${ctSiteVo.subj}"/></a>
						</div>
					</td>
					<td class="body_ct"><a href="javascript:parent.viewUserPop('${ctSiteVo.regrUid}');">${ctSiteVo.regrNm}</a></td>
					<td class="body_ct">
						<fmt:parseDate var="dateTempParse" value="${ctSiteVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${fn:length(ctSiteList) == 0}">
				<tr>
				<td class="nodata" colspan="3"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
				</tr>
			</c:if>
		</tbody>
	</table>
</form>
<u:listArea/>
<u:pagination noTotalCount="true"/>
<div id="baordPt" ></div>