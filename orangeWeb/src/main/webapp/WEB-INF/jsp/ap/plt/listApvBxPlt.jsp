<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><c:set var="ifrHght" value="${param.hghtPx -43}" />
<script type="text/javascript">
<!--<%
// 포틀릿 탭 클릭 %>
function clickApPltTab(bxId, menuId){
	var ifrm = getIframeContent("apvPltFrm");
	ifrm.reload('${_cxPth}/ap/box/listApvBxPltFrm.do?menuId='+menuId+'&bxId='+bxId+'&colYn=${param.colYn}&hghtPx=${ifrHght}');
	var $form = $("#toDocForm");
	$form.find("#bxId").val(bxId);
	$form.find("#menuId").val(menuId);
}<%
// 양식선택 %>
function selectForm(){
	dialog.open('selectFormDialog', '<u:msg alt="양식선택" titleId="ap.btn.selectForm" />', './selectFormPop.do?menuId='+gFrmMenuId+'&bxId=${param.bxId}');
}<%
// 문서 작성 - 양식 선택으로 새문서 작성 %>
function makeDoc(formId){
	var $form = initForm();
	var $bx = $form.find("#bxId");
	$('<input>', {'name':'formId','value':formId,'type':'hidden'}).insertBefore($bx);
	$form.attr("action", "/ap/box/setDoc.do").attr("method", "GET").submit();
}<%
// 문서 작성 - (임시저장,반려문서)의 재작성 %>
function remakeDoc(apvNo){
	var $form = $("#toDocForm");
	$form.find("[name='apvNo'], [name='formId'], [name='apvLnPno']").remove();
	var $bx = $form.find("#bxId");
	$('<input>', {'name':'apvNo','value':apvNo,'type':'hidden'}).insertBefore($bx);
	$form.attr("action", "/ap/box/setDoc.do").attr("method", "GET").submit();
}<%
// 문서 조회 %>
function openDoc(apvNo, apvLnPno, apvLnNo, sendSeq, pwYn){
	var $form = initForm();
	var $bx = $form.find("#bxId");
	var p = apvNo.indexOf(',');
	if(p>0){
		$('<input>', {'name':'apvNo','value':apvNo.substring(0,p),'type':'hidden'}).insertBefore($bx);
		$('<input>', {'name':'apvNos','value':apvNo,'type':'hidden'}).insertBefore($bx);
	} else {
		$('<input>', {'name':'apvNo','value':apvNo,'type':'hidden'}).insertBefore($bx);
	}
	if(apvLnPno!='') $('<input>', {'name':'apvLnPno','value':apvLnPno,'type':'hidden'}).insertBefore($bx);
	if(apvLnNo!='') $('<input>', {'name':'apvLnNo','value':apvLnNo,'type':'hidden'}).insertBefore($bx);
	if(sendSeq!='') $('<input>', {'name':'sendSeq','value':sendSeq,'type':'hidden'}).insertBefore($bx);
	if(pltQueryString!='') $('<input>', {'name':'pltQueryString','value':pltQueryString,'type':'hidden'}).insertBefore($bx);
	$form.attr("method", "GET");
	if(pwYn=='Y'){
		parent.dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "/ap/box/setDocPwPop.do?menuId="+$form.find("#menuId").val()+"&bxId="+$form.find("#bxId").val()+"&apvNo="+apvNo+"&pltId=${param.pltId}&callback=openSecuPltDoc");
	} else {
		$form.attr("action", "${_cxPth}/ap/box/setDoc.do").submit();
	}
}<%
// 비밀번호 세팅된 문서 열기 - setDocPwPop.jsp 에서 호출 %>
function openSecuDoc(secuId){
	initForm(false).appendHidden({'name':'secuId','value':secuId
		}).attr("action", "${_cxPth}/ap/box/setDoc.do").submit();
}<%
// 문서조회 Form 리턴 %>
function initForm(init){
	var $form = $("#toDocForm");
	if(init!=false){
		$form.find("[name='apvNo'], [name='formId'], [name='apvLnPno'], [name='secuId'], [name='sendSeq'], [name='pltQueryString']").remove();
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
	resizeForPltTab('apvPltTab');
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
	var href = getIframeContent('apvPltFrm').location.href;
	var p = href.indexOf('?');
	var q = href.indexOf('&colYn=');
	if(p>0 && q>p){
		return '${_cxPth}/ap/box/listApvBx.do'+href.substring(p,q);
	}
	return null;
}
//-->
</script>
<div class="ptlbody_ct">
	<u:tabGroup id="apvPltTab" noBottomBlank="true"><c:forEach
		items="${apPltSetupDVoList}" var="apPltSetupDVo" varStatus="status">
		<u:tab id="apvPltTab" areaId="${apPltSetupDVo.bxId}PltFrm" title="${apPltSetupDVo.bxNm}" on="${status.first}"
			onclick="clickApPltTab('${apPltSetupDVo.bxId}', '${apPltSetupDVo.menuId}');" /></c:forEach>
	</u:tabGroup>
	<div style="height:4px;"></div>
	<iframe id="apvPltFrm" name="apvPltFrm"
		src="${_cxPth}/ap/box/listApvBxPltFrm.do?menuId=${apPltSetupDVoList[0].menuId}&bxId=${apPltSetupDVoList[0].bxId}&colYn=${param.colYn}&hghtPx=${ifrHght}"
		style="width:100%; height:${ifrHght}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
<form id="toDocForm" target="_parent">
<u:input id="bxId" value="${apPltSetupDVoList[0].bxId}" type="hidden" />
<u:input id="menuId" value="${apPltSetupDVoList[0].menuId}" type="hidden" />
</form>