<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="hghtPx,colYn,pageRowCnt"/>
<c:set var="ifrHght" value="${param.hghtPx -43}" />
<script type="text/javascript">
<!--<%
// 포틀릿 탭 클릭 %>
function clickRescPltTab(bxId, menuId){
	var ifrm = getIframeContent("wrRescPltFrm");
	ifrm.reload('${_cxPth}/wr/listRescTabFrm.do?menuId='+menuId+'&colYn=${param.colYn}&hghtPx=${ifrHght}&bxId='+bxId+'&pltId=${param.pltId}');
	var $form = $("#toRescForm");
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
	<%// 텝 - 가로폭에 따라서 두줄로 나타나지 않게 숨김 %>
	resizeForPltTab('rescPltTab');
});
<%
// 포틀릿 탭 - 가로폭에 따라서 넘치는 탭 숨기기 %>
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
	var href = getIframeContent('wrRescPltFrm').location.href;
	var p = href.indexOf('?');
	var q = href.indexOf('&colYn=');
	if(p>0 && q>p){
		bxId = $('#toRescForm #bxId').val();
		doNm = 'listRezv';

		if(bxId == 'resc')
			doNm = 'listResc';
		else if(bxId == 'disc')
			doNm = 'listRezvDisc';

		return '${_cxPth}/wr/'+doNm+'.do'+href.substring(p,q) + (bxId=='my'?'&fncMy=Y':'');
	}
	return null;
}
//-->
</script>
<% // 목록 %>
<div class="ptlbody_ct" >
	<u:tabGroup id="rescPltTab" noBottomBlank="true"><c:forEach
		items="${wrRescPltSetupDVoList}" var="wrRescPltSetupDVo" varStatus="status">
		<u:tab id="rescPltTab" areaId="${wrRescPltSetupDVo.bxId}PltFrm" title="${wrRescPltSetupDVo.bxNm}" on="${status.first}"
			onclick="clickRescPltTab('${wrRescPltSetupDVo.bxId}', '${wrRescPltSetupDVo.menuId}');" /></c:forEach>
	</u:tabGroup>
<div style="height:4px;"></div>
<iframe id="wrRescPltFrm" name="wrRescPltFrm"
		src="${_cxPth}/wr/listRescTabFrm.do?menuId=${wrRescPltSetupDVoList[0].menuId}&colYn=${param.colYn}&hghtPx=${ifrHght}&bxId=${wrRescPltSetupDVoList[0].bxId}&pltId=${param.pltId}"
		style="width:100%; height:${ifrHght}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
<form id="toRescForm" target="_parent">
<u:input id="bxId" value="${wrRescPltSetupDVoList[0].bxId}" type="hidden" />
<u:input id="viewPage" value="${fn:replace(wrRescPltSetupDVoList[0].bxId,'list','view')}" type="hidden" />
<u:input id="menuId" value="${wrRescPltSetupDVoList[0].menuId}" type="hidden" />
</form>