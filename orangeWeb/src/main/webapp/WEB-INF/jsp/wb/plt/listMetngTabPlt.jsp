<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="tabHght" test="${!empty tabList && fn:length(tabList) > 1 }" value="28" elseValue="0"/>
<script type="text/javascript">
<!--
<% // [탭클릭] 게시물 조회 %>
function fnTabClick(tId){
	getIframeContent('bcTabArea').reload('./listMetngTabFrm.do?tId='+tId+'&${paramsForList }&hghtPx=${param.hghtPx-tabHght-15}');
};

$(document).ready(function() {
	<%// 유니폼 적용 %>
	//setUniformCSS();
	<%// 텝 - 가로폭에 따라서 두줄로 나타나지 않게 숨김 %>
	resizeForPltTab('bcTab');
});

<%//포틀릿 탭 - 가로폭에 따라서 넘치는 탭 숨기기 %>
function resizeForPltTab(tabId){
	gPltTabId = tabId;
	onResizeForPltTab();
	$(window).resize(onResizeForPltTab);
}

<%
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
}
//-->
</script>
<% // 목록 %>
<div class="ptlbody_ct" >
<!-- TAB ID가 2개 이상일경우에만 탭을 구성한다. -->
<c:if test="${tabHght > 0 }">
	<u:tabGroup id="bcTab" noBottomBlank="true">
		<c:forEach var="tabInfo" items="${tabList }" varStatus="status">
			<u:tab id="bcTab" titleId="wb.jsp.${tabInfo[1] }.title" on="${tabInfo[0] eq tId }" onclick="fnTabClick('${tabInfo[0] }');"/>
		</c:forEach>
	</u:tabGroup>
</c:if>
<div style="height:4px;"></div>
<iframe id="bcTabArea" name="bcTabArea" src="./listMetngTabFrm.do?tId=${tId }&${paramsForList }&hghtPx=${param.hghtPx-tabHght-15}" style="width:100%;height:${param.hghtPx-tabHght-15}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>