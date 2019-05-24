<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><c:set var="ifrHght" value="${param.hghtPx -43}" />
<u:set var="tabHght" test="${!empty wbBcPltSetupDVoList && fn:length(wbBcPltSetupDVoList) > 0 }" value="28" elseValue="0"/>
<script type="text/javascript">
<!--<%
// 포틀릿 탭 클릭 %>
function clickBcPltTab(bxId, menuId){
	var urlSuffix = bxId.indexOf("Metng")>-1 ? 'listMetngTabFrm' : 'listBcTabFrm';
	var ifrm = getIframeContent("bcPltFrm");
	ifrm.reload('${_cxPth}/wb/'+urlSuffix+'.do?menuId='+menuId+'&colYn=${param.colYn}&hghtPx=${ifrHght}&bxId='+bxId);
	var $form = $("#toBcForm");
	$form.find("#bxId").val(bxId);
	$form.find("#menuId").val(menuId);
	$form.find("#viewPage").val(bxId.replace('list','view'));
}
<%
//포틀릿 쿼리 스트링 %>
var pltQueryString = '';
function setQueryString(qstr){
	pltQueryString = qstr;
}
$(document).ready(function() {
	<%// 유니폼 적용 %>
	//setUniformCSS();
	<%// 텝 - 가로폭에 따라서 두줄로 나타나지 않게 숨김 %>
	resizeForPltTab('bcPltTab');
});
<%
//포틀릿 탭 - 가로폭에 따라서 넘치는 탭 숨기기 %>
function resizeForPltTab(tabId){
	gPltTabId = tabId;
	onResizeForPltTab();
	$(window).resize(onResizeForPltTab);
}<%
//포틀릿 탭 - window 이벤트 사용을 위해서 - 탭ID 보관용 %>
var gPltTabId = null;<%
//포틀릿 탭 - 가로폭에 따라서 넘치는 탭 숨기기 - 실제 함수 %>
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
	var href = getIframeContent('bcPltFrm').location.href;
	var p = href.indexOf('?');
	var q = href.indexOf('&colYn=');
	if(p>0 && q>p){
		bxId = $('#toBcForm #bxId').val();
		doNm = 'listBc';

		if(bxId == 'agnt')
			doNm = 'listAgntBc';
		else if(bxId == 'psnMetng')
			doNm = 'listMetng';
		else if(bxId == 'open')
			doNm = 'listOpenBc';
		else if(bxId == 'bumk')
			doNm = 'setBcBumk';
		else if(bxId == 'agntMetng')
			doNm = 'listAgntMetng';
		else if(bxId == 'pub')
			doNm = 'pub/listPubBc';
		
		return '${_cxPth}/wb/'+doNm+'.do'+href.substring(p,q);
	}
	return null;
}
//-->
</script>
<% // 목록 %>
<div class="ptlbody_ct" >
	<u:tabGroup id="bcPltTab" noBottomBlank="true"><c:forEach
		items="${wbBcPltSetupDVoList}" var="wbBcPltSetupDVo" varStatus="status">
		<u:tab id="bcPltTab" areaId="${wbBcPltSetupDVo.bxId}PltFrm" title="${wbBcPltSetupDVo.bxNm}" on="${status.first}"
			onclick="clickBcPltTab('${wbBcPltSetupDVo.bxId}', '${wbBcPltSetupDVo.menuId}');" /></c:forEach>
	</u:tabGroup>
<div style="height:4px;"></div>
<u:set var="urlSuffix" test="${fn:contains(wbBcPltSetupDVoList[0].bxId, 'Metng') }" value="listMetngTabFrm" elseValue="listBcTabFrm"/>
<iframe id="bcPltFrm" name="bcPltFrm"
		src="${_cxPth}/wb/${urlSuffix }.do?menuId=${wbBcPltSetupDVoList[0].menuId}&colYn=${param.colYn}&hghtPx=${ifrHght}&bxId=${wbBcPltSetupDVoList[0].bxId}"
		style="width:100%; height:${ifrHght}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
<form id="toBcForm" target="_parent">
<u:input id="bxId" value="${wbBcPltSetupDVoList[0].bxId}" type="hidden" />
<u:input id="viewPage" value="${fn:replace(wbBcPltSetupDVoList[0].bxId,'list','view')}" type="hidden" />
<u:input id="menuId" value="${wbBcPltSetupDVoList[0].menuId}" type="hidden" />
</form>



