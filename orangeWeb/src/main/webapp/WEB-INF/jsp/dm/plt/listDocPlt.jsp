<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="hghtPx,colYn,pageRowCnt"/>
<c:set var="ifrHght" value="${param.hghtPx -43}" />
<script type="text/javascript">
<!--<%
// 포틀릿 탭 클릭 %>
function clickDmPltTab(bxId, menuId){
	if(bxId=='') return;
	var ifrm = getIframeContent("docPltFrm");
	ifrm.reload('${_cxPth}/dm/doc/'+bxId+'PltFrm.do?menuId='+menuId+'&colYn=${param.colYn}&hghtPx=${ifrHght}&bxId='+bxId);
	var $form = $("#toDocForm");
	$form.find("#bxId").val(bxId);
	$form.find("#menuId").val(menuId);
	$form.find("#viewPage").val(bxId.replace('list','view'));
}<% // [목록:제목] 상세 조회 - 문서조회 %>
function viewDoc(id,docGrpId,tgtId) {
	var viewPage = $('#toDocForm #viewPage').val();
	var menuId = $('#toDocForm #menuId').val();
	callAjax('/dm/doc/getViewOptAjx.do?menuId='+menuId, {docGrpId:docGrpId,viewPage:viewPage}, function(data) {
		if ((data.popYn == null || data.popYn == 'N') && data.message != null) {
			alert(data.message);
		}
		if (data.popYn != null && data.popYn == 'Y' && confirm(data.message)) {
			parent.dialog.open("viewDocReqCfrmDialog", '<u:msg titleId="dm.jsp.dtlView.request.title" alt="문서열람요청" />', "/dm/doc/viewDocReqCfrmPop.do?menuId="+menuId+"&docGrpId="+docGrpId);
		}
		if (data.result == 'ok') {
			var $form = initForm();
			$form.attr("method", "GET");
			var url = '${_cxPth}/dm/doc/'+viewPage+'.do?${paramsForList }';
			if(tgtId != undefined && tgtId != '') url+= '&tgtId='+tgtId;
			$form.appendHidden({'name':'docId','value':id
			}).attr("action", url).submit();
		}
	});
}<% // [목록:제목] 상세 조회 - 개인문서 %>
function viewPsnDoc(id) {
	var viewPage = $('#toDocForm #viewPage').val();
	var $form = initForm();
	$form.attr("method", "GET");
	var url = '${_cxPth}/dm/doc/'+viewPage+'.do?${paramsForList }';
	$form.appendHidden({'name':'docId','value':id
	}).attr("action", url).submit();
}<%
// 문서조회 Form 리턴 %>
function initForm(init){
	var $form = $("#toDocForm");
	if(init!=false){
		$form.find("[name='bxId'], [name='docId'], [name='pltQueryString']").remove();
	}
	return $form;
}<%
// 포틀릿 쿼리 스트링 %>
var pltQueryString = '';
function setQueryString(qstr){
	pltQueryString = qstr;
}<%
//onload %>
$(document).ready(function() {<%
	// 텝 - 가로폭에 따라서 두줄로 나타나지 않게 숨김 %>
	resizeForPltTab('docPltTab');
});<%


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
	<c:if test="${empty dmPltSetupDVoList }">
	return '#';
	</c:if>
	var href = getIframeContent('docPltFrm').location.href;
	var p = href.indexOf('?');
	var q = href.indexOf('&colYn=');
	var bxId=$('#toDocForm #bxId').val();
	if(bxId=='') bxId='listDoc';
	if(p>0 && q>p){
		return '${_cxPth}/dm/doc/'+bxId+'.do'+href.substring(p,q);
	}
	return null;
}
//-->
</script>
<div class="ptlbody_ct">
	<u:tabGroup id="docPltTab" noBottomBlank="true"><c:forEach
		items="${dmPltSetupDVoList}" var="dmPltSetupDVo" varStatus="status">
		<u:tab id="docPltTab" areaId="${dmPltSetupDVo.bxId}PltFrm" title="${dmPltSetupDVo.bxNm}" on="${status.first}"
			onclick="clickDmPltTab('${dmPltSetupDVo.bxId}', '${dmPltSetupDVo.menuId}');" /></c:forEach>
	</u:tabGroup>
	<div style="height:4px;"></div>
	<u:set var="firstUrl" test="${empty dmPltSetupDVoList }" value="listDoc" elseValue="${dmPltSetupDVoList[0].bxId}"/>
	<c:if test="${!empty dmPltSetupDVoList }">
	<iframe id="docPltFrm" name="docPltFrm"
		src="${_cxPth}/dm/doc/${firstUrl }PltFrm.do?menuId=${dmPltSetupDVoList[0].menuId}&colYn=${param.colYn}&hghtPx=${ifrHght}&bxId=${dmPltSetupDVoList[0].bxId}"
		style="width:100%; height:${ifrHght}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe></c:if>
</div>
<form id="toDocForm" target="_parent">
<u:input id="bxId" value="${dmPltSetupDVoList[0].bxId}" type="hidden" />
<u:input id="viewPage" value="${fn:replace(dmPltSetupDVoList[0].bxId,'list','view')}" type="hidden" />
<u:input id="menuId" value="${dmPltSetupDVoList[0].menuId}" type="hidden" />
</form>