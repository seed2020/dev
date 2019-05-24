<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="tabHght" test="${!empty ctPltCmListDVoList && fn:length(ctPltCmListDVoList) > 0 }" value="28" elseValue="0"/>
<u:set var="onclickFirst" test="${tId eq 'exnt' }" value="true" elseValue="false"/>

<script type="text/javascript">
<!--
function fnTabGroupClick()
{
	top.setupPlt('${param.pltId}', '/ct/plt/setExntCmTabPlt.do?menuId=${param.pltId}&pltId=${param.pltId}');
}
<% // [탭클릭] 게시물 조회 %>
function fnTabClick(tId,menuId){
	var url;

	if(tId.indexOf("mng") > -1)
	{
		url = "listCmMngTgtTabFrm";
		$("#divTabGroupBtn").hide();
	}
	else if(tId.indexOf("exnt") > -1)
	{	
		url = "listExntCmTabFrm";
		$("#divTabGroupBtn").show();
	}	
	else if(tId.indexOf("recmd") > -1)	
	{	
		url = "listRecmdCmTabFrm";
		$("#divTabGroupBtn").hide();
	}
	else if(tId.indexOf("new") > -1)
	{	
		url = "listNewCmTabFrm";
		$("#divTabGroupBtn").hide();
	}
	else		
	{	
		url = "listMyCmTabFrm";
		$("#divTabGroupBtn").hide();
	}
	getIframeContent('ctTabArea').reload('/ct/plt/'+url+'.do?menuId='+menuId+'&tId='+tId+'&${paramsForList }&hghtPx=${param.hghtPx-tabHght-15}');
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
		sum = sum + outerWidth + 20;  <% //20펙셀은 탭메뉴 설정버튼 때문에 추가함 %>
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
		
		doNm = null;
		if(tId.indexOf("mng") > -1)
			doNm = "listCmMngTgt";
		else if(tId.indexOf("my") > -1)
			doNm = "listMyCm";
		else
			return null;	

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
	<u:tabGroup id="ctTab" noBottomBlank="true" setBtnClick="fnTabGroupClick();" setBtnClickFirst="${onclickFirst}">
		<c:forEach var="ctPltCmListDVo" items="${ctPltCmListDVoList }" varStatus="status">
			<u:tab id="ctTab" title="${ctPltCmListDVo.bxNm }" on="${ctPltCmListDVo.bxId eq tId }" onclick="fnTabClick('${ctPltCmListDVo.bxId }','${ctPltCmListDVo.menuId }');"/>
		</c:forEach>
	</u:tabGroup>
</c:if>
<div style="height:4px;"></div>
<iframe id="ctTabArea" name="ctTabArea" src="/ct/plt/<c:choose>
<c:when test="${tId eq 'mng' }">listCmMngTgtTabFrm</c:when>
<c:when test="${tId eq 'exnt' }">listExntCmTabFrm</c:when>
<c:when test="${tId eq 'recmd' }">listRecmdCmTabFrm</c:when>
<c:when test="${tId eq 'new' }">listNewCmTabFrm</c:when>
<c:otherwise>listMyCmTabFrm</c:otherwise>
</c:choose>.do?menuId=${menuId}&tId=${tId }&${paramsForList }&hghtPx=${param.hghtPx-tabHght-15}" style="width:100%;height:${param.hghtPx-tabHght-15}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>