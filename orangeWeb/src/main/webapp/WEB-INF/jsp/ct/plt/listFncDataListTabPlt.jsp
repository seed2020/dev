<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="tabHght" test="${!empty CtPltFncListDVoList && fn:length(CtPltFncListDVoList) > 0 }" value="28" elseValue="0"/>
<script type="text/javascript">
<!--
var vTabId = "${tId }";
function fnTabGroupClick()
{
	if(vTabId == "surv")
		url = "/ct/plt/setCtFuncPlt.do?ctFncId=F000000Q";
	else if(vTabId == "site")
		url = "/ct/plt/setCtFuncPlt.do?ctFncId=F000000O";
	else if(vTabId == "debr")
		url = "/ct/plt/setCtFuncPlt.do?ctFncId=F000000P";
	else if(vTabId == "board")
		url = "/ct/plt/setCtFuncPlt.do?ctFncId=F000000L";
	else  if(vTabId == "schdl")
		url = "/ct/plt/setCtFuncPlt.do?ctFncId=F000000N";
											
	top.setupPlt('${param.pltId}', url+'&menuId=${param.pltId}&pltId=${param.pltId}');
}
<% // [탭클릭] 게시물 조회 %>
function fnTabClick(tId,menuId){
	vTabId = tId;
	var url;
	if(tId.indexOf("surv") > -1)
		url = "plt/listSurvCmTabFrm";
	else if(tId.indexOf("site") > -1)
		url = "plt/listSiteCmTabFrm";
	else if(tId.indexOf("debr") > -1)	
		url = "plt/listDebrCmTabFrm";
	else if(tId.indexOf("schdl") > -1)
		url = "plt/listSchdlCmTabFrm";
	else		
		url = "plt/listBoardCmTabFrm";

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
		sum = sum + outerWidth + 20;  <% //20펙셀은 탭메뉴 설정버튼 때문에 추가함 %>
		if(sum > pltWidth){
			$(this).parent().hide();
		}
	});
}
//-->
</script>
<% // 목록 %>
<div class="ptlbody_ct" >
<!-- 게시판ID가 1개 이상일경우에만 탭을 구성한다. -->
<c:if test="${tabHght > 0 }">
	<u:tabGroup id="ctTab" noBottomBlank="true" setBtnClick="fnTabGroupClick();" setBtnClickFirst="true">
		<c:forEach var="ctPltFncListDVo" items="${CtPltFncListDVoList }" varStatus="status" >
			<u:tab id="ctTab" title="${ctPltFncListDVo.bxNm }" on="${ctPltFncListDVo.bxId eq tId }" onclick="fnTabClick('${ctPltFncListDVo.bxId }','${ctPltFncListDVo.menuId }');"/>
		</c:forEach>
	</u:tabGroup>
</c:if>
<div style="height:4px;"></div>
<iframe id="ctTabArea" name="ctTabArea" src="/ct/<c:choose>
<c:when test="${tId eq 'surv' }">plt/listSurvCmTabFrm</c:when>
<c:when test="${tId eq 'site' }">plt/listSiteCmTabFrm</c:when>
<c:when test="${tId eq 'debr' }">plt/listDebrCmTabFrm</c:when>
<c:when test="${tId eq 'schdl' }">plt/listSchdlCmTabFrm</c:when>
<c:otherwise>plt/listBoardCmTabFrm</c:otherwise>
</c:choose>.do?menuId=${menuId}&tId=${tId }&${paramsForList }&hghtPx=${param.hghtPx-tabHght-15}" style="width:100%;height:${param.hghtPx-tabHght-15}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>