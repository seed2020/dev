<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
%>
<script type="text/javascript">
<!--
<%// 포틀릿 리로드 %>
function reloadPlt(pltId, url){
	getIframeContent("PLT_"+pltId).reload(url);
}
<%// 포틀릿 more... %>
function morePlt(pltId, url){
	var pltWin = getIframeContent('PLT_'+pltId);
	var tabUrl = (pltWin && pltWin.getMoreUrl) ? pltWin.getMoreUrl() : null;
	location.href = tabUrl || url;
}
<%// 포틀릿 설정 %>
function setupPlt(pltId, url){
	var popTitle = '<u:msg titleId="pt.jsp.lout.setupPlt" alt="포틀릿 설정" />' + (url.indexOf('&byAdm=Y')>0 ? ' - <u:msg titleId="pt.top.adm" alt="관리자" />' : '');
	dialog.open('setupPltDialog', popTitle, url);
	dialog.onClose("setupPltDialog", function(){ getIframeContent("PLT_"+pltId).reload(); });
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	setUniformCSS();
});
//-->
</script>
<u:set
	test="${browser.opera or browser.chrome}" 
	var="paddingLeft" value="padding-right:1px;" elseValue=""
	/>
<c:forEach items="${pltZone}" var="zone" varStatus="zoneStatus">
<div style="float:left; width:${zone};<c:if
		test="${not zoneStatus.first }"> margin-left:1%;</c:if>"><u:convert
	srcId="ptPltSetupRVoList${zoneStatus.index + 1}" var="ptPltSetupRVoList"
	/><c:forEach
		items="${ptPltSetupRVoList}" var="ptPltSetupRVo" varStatus="status"><c:if
		test="${not status.first }"><u:blank /></c:if>
	<div class="portlet"><u:authUrl
			var="pltUrl" url="${ptPltSetupRVo.ptPltDVo.pltUrl}" menuId="${ptPltSetupRVo.pltId}" /><c:if
			test="${ptPltSetupRVo.ptPltDVo.pltTitlYn eq 'Y'}"><u:set
				var="ifrHght" test="${true}" value="${ptPltSetupRVo.ptPltDVo.hghtPx - 35}" /></c:if><c:if
			test="${not (ptPltSetupRVo.ptPltDVo.pltTitlYn eq 'Y')}"><u:set
				var="ifrHght" test="${ptPltSetupRVo.ptPltDVo.bordYn eq 'Y'}"
				value="${ptPltSetupRVo.ptPltDVo.hghtPx - 11}" elseValue="${ptPltSetupRVo.ptPltDVo.hghtPx}" /></c:if>
		<c:if test="${ptPltSetupRVo.ptPltDVo.pltTitlYn eq 'Y'}">
			<div class="ptltit" style="${paddingLeft}">
				<dl>
				<dd class="title">${ptPltSetupRVo.ptPltDVo.rescNm}</dd>
				<dd class="btn"><a href="javascript:reloadPlt('${ptPltSetupRVo.pltId}', '${pltUrl}&hghtPx=${ifrHght}&colYn=${ptPltSetupRVo.ptPltDVo.itemTitlYn}&pltId=${ptPltSetupRVo.pltId}');"><img src="/images/${_skin}/icoptl_reload.png" width="19" height="21" /></a></dd><c:if
					
					test="${not empty ptPltSetupRVo.ptPltDVo.setupUrl}"><u:authUrl
						url="${ptPltSetupRVo.ptPltDVo.setupUrl}" menuId="${ptPltSetupRVo.pltId}" var="setupUrl" />
				<dd class="btn"><a href="javascript:setupPlt('${ptPltSetupRVo.pltId}', '${setupUrl
					}&pltId=${ptPltSetupRVo.pltId}');"><img src="/images/${_skin}/icoptl_setting.png" width="19" height="21" /></a></dd><c:if
						
						test="${sessionScope.userVo.userUid eq 'U0000001' and setupUrl.startsWith('/bb/plt/setBaBxPltSetupPop')}">
				<dd class="btn"><a href="javascript:setupPlt('${ptPltSetupRVo.pltId}', '${setupUrl
					}&pltId=${ptPltSetupRVo.pltId}&byAdm=Y');"><img src="/images/${_skin}/icoptl_setting.png" width="19" height="21" /></a></dd></c:if></c:if><c:if
					
					test="${not empty ptPltSetupRVo.ptPltDVo.moreUrl}"><u:authUrl
						url="${ptPltSetupRVo.ptPltDVo.moreUrl}" var="moreUrl" />
				<dd class="btn"><a href="javascript:morePlt('${ptPltSetupRVo.pltId}', '${moreUrl
					}');"><img src="/images/${_skin}/more.png" width="38" height="21" /></a></dd></c:if>
				</dl>
			</div>
		</c:if><c:if
			test="${ptPltSetupRVo.ptPltDVo.pltTitlYn eq 'Y' or ptPltSetupRVo.ptPltDVo.bordYn eq 'Y'}">
		<div class="ptlbody ${ptPltSetupRVo.ptPltDVo.pltTitlYn eq 'N' ? 'ptlbody_top' : ''}" style="${paddingLeft}">
			<iframe id="PLT_${ptPltSetupRVo.pltId}" src="${pltUrl}&hghtPx=${ifrHght}&colYn=${ptPltSetupRVo.ptPltDVo.itemTitlYn}&pltId=${ptPltSetupRVo.pltId}" style="width:100%; height:${ifrHght}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
			<div class="btnarea">
			<a href="javascript:"><img src="/images/${_skin}/btn_portlet.png" width="12" height="9" /></a>
			</div>
		</div>
		</c:if><c:if
			test="${not (ptPltSetupRVo.ptPltDVo.pltTitlYn eq 'Y' or ptPltSetupRVo.ptPltDVo.bordYn eq 'Y')}">
			<div style="overflow-y:hidden; padding-right:1px;">
			<iframe id="PLT_${ptPltSetupRVo.pltId}" src="${pltUrl}&hghtPx=${ifrHght}&colYn=${ptPltSetupRVo.ptPltDVo.itemTitlYn}&pltId=${ptPltSetupRVo.pltId}" style="width:100%; height:${ifrHght}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
			</div>
		</c:if>
	</div></c:forEach>
</div>
</c:forEach>
