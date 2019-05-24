<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function delSite(){
	var selectCtSiteIds = [];
	selectCtSiteIds.push('${siteId}');
	if (confirmMsg("cm.cfrm.del")) {
		callAjax('./transSiteListDel.do?menuId=${menuId}&ctId=${ctId}', {selectCtSiteIds:selectCtSiteIds}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listSite.do?menuId=${menuId}&ctId=${ctId}';
			}
		});
	}else{
		return;
	}
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<c:set var="siteNm" value="OKJSP" />
<c:set var="siteUrl" value="http://www.okjsp.net" />
<c:set var="recmdRson" value="JSP 커뮤니티 사이트, 강좌, Q&A, ..." />

<u:title titleId="ct.jsp.viewSite.title" alt="Cool Site 조회" menuNameFirst="true"/>

<form id="viewSiteForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="27%"/><col width="73%"/></colgroup>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.siteNm" alt="사이트명" /></td>
	<td class="body_lt">${ctSiteBVo.subj}</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.cat" alt="카테고리" /></td>
	<td class="body_lt">${ctSiteBVo.catNm}</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.siteUrl" alt="Site URL" /></td>
	<td class="body_lt"><a href="${ctSiteBVo.url}" target="_blank">${ctSiteBVo.url}</a></td>
	</tr>
	
	<tr>
	<td colspan="2" class="head_ct"><u:msg titleId="cols.recmdRson" alt="추천사유" /></td>
	</tr>
	
	<tr>
	<td colspan="2" class="body_lt"><div style="overflow:auto;" class="editor">${ctSiteBVo.cont}</div></td>
	</tr>
</table>
</div>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<c:choose>
		<c:when test="${!empty myAuth && myAuth == 'M' }">
			<u:button titleId="cm.btn.mod" alt="수정" href="./setSite.do?menuId=${menuId}&ctId=${ctId}&siteId=${siteId}&catId=${catId}" />
			<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delSite();" />
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${ctSiteBVo.regrUid == logUserUid}">
					<u:button titleId="cm.btn.mod" alt="수정" href="./setSite.do?menuId=${menuId}&ctId=${ctId}&siteId=${siteId}&catId=${catId}" />
					<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delSite();" />
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${!empty authChkD && authChkD == 'D' }">
							<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delSite();" />
						</c:when>
					</c:choose>
				</c:otherwise> 
			</c:choose>
		</c:otherwise>
	</c:choose>
	<u:button titleId="cm.btn.list" alt="목록" href="./listSite.do?menuId=${menuId}&ctId=${ctId}" />
</u:buttonArea>

</form>

