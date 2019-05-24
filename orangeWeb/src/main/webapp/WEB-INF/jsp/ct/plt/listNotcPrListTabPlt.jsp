<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="tabHght" test="${!empty CtPltNoPrListDVoList && fn:length(CtPltNoPrListDVoList) > 0 }" value="28" elseValue="0"/>
<script type="text/javascript">
<!--
<% // [탭클릭] 게시물 조회 %>
function fnTabClick(tId,menuId){
	var url;
	if(tId.indexOf("pr") > -1)
		url = "pr/listPrTabFrm";
	else		
		url = "notc/listNotcTabFrm";

	getIframeContent('ctTabArea').reload('/ct/'+url+'.do?menuId='+menuId+'&tId='+tId+'&${paramsForList }&hghtPx=${param.hghtPx-tabHght-15}');
};

$(document).ready(function() {
	<%// 유니폼 적용 %>
	//setUniformCSS();
	<%// 텝 - 가로폭에 따라서 두줄로 나타나지 않게 숨김 %>
	resizeForPltTab('ctTab');
});

<%//포틀릿 탭 - 가로폭에 따라서 넘치는 탭 숨기기 %>
function resizeForPltTab(tabId){
	gPltTabId = tabId;
	onResizeForPltTab();
	$(window).resize(onResizeForPltTab);
}<%
// 포틀릿 탭 - window 이벤트 사용을 위해서 - 탭ID 보관용 %>
var gPltTabId = null;<%
// 포틀릿 탭 - 가로폭에 따라서 넘치는 탭 숨기기 - 실제 함수 %>
function onResizeForPltTab(){
	if(gPltTabId==null) return;
	var pltWidth = $(".ptlbody_ct").width(), sum=2, outerWidth;
	$("#"+gPltTabId+" .tab_left li a").each(function(){
		if(sum<pltWidth && !$(this).is(":visible")){
			$(this).parent().show();
		}
		outerWidth = $(this).outerWidth();
		sum = sum + outerWidth;
		if(sum > pltWidth){
			$(this).parent().hide();
		}
	});
}<%
// moreUrl 리턴 %>
function getMoreUrl(){
	var href = getIframeContent('ctTabArea').location.href;
	var p = href.indexOf('?');
	var q = href.indexOf('&colYn=');
	if(p>0 && q>p){
		tId = href.substring(href.indexOf('tId=')+4,q);
		doNm = 'notc/listNotc';
		if(tId = 'pr')
			doNm = 'pr/listPr';
		return '${_cxPth}/ct/'+doNm+'.do'+href.substring(p,q);
	}
	return null;
}
//-->
</script>
<% // 목록 %>
<div class="ptlbody_ct" >
<!-- 게시판ID가 1개 이상일경우에만 탭을 구성한다. -->
<c:if test="${tabHght > 0 }">
	<u:tabGroup id="ctTab" noBottomBlank="true">
		<c:forEach var="ctPltNoPrListDVo" items="${CtPltNoPrListDVoList }" varStatus="status">
			<u:tab id="ctTab" title="${ctPltNoPrListDVo.bxNm }" on="${ctPltNoPrListDVo.bxId eq tId }" onclick="fnTabClick('${ctPltNoPrListDVo.bxId }','${ctPltNoPrListDVo.menuId }');"/>
		</c:forEach>
	</u:tabGroup>
</c:if>
<div style="height:4px;"></div>
<iframe id="ctTabArea" name="ctTabArea" src="/ct/<c:choose>
<c:when test="${tId eq 'pr' }">pr/listPrTabFrm</c:when>
<c:otherwise>notc/listNotcTabFrm</c:otherwise>
</c:choose>.do?menuId=${menuId}&tId=${tId }&${paramsForList }&hghtPx=${param.hghtPx-tabHght-15}" style="width:100%;height:${param.hghtPx-tabHght-15}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>