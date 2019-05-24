<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="hghtPx,colYn,pageRowCnt"/>
<c:set var="ifrHght" value="${param.hghtPx -43}" />
<script type="text/javascript">
<!--<%
// 포틀릿 탭 클릭 %>
function clickWorksPltTab(bxId, menuId){
	var ifrm = getIframeContent("worksPltFrm");
	ifrm.reload('${_cxPth}/wf/works/listWorksPltFrm.do?menuId='+menuId+'&colYn=${param.colYn}&hghtPx=${ifrHght}&formNo='+bxId);
	var $form = $("#toWorksForm");
	$form.find("#bxId").val(bxId);
	$form.find("#menuId").val(menuId);
	$form.find("#viewPage").val(bxId.replace('list','view'));
}<%
// 포틀릿 쿼리 스트링 %>
var pltQueryString = '';
function setQueryString(qstr){
	pltQueryString = qstr;
}<%
//onload %>
$(document).ready(function() {<%
	// 텝 - 가로폭에 따라서 두줄로 나타나지 않게 숨김 %>
	resizeForPltTab('worksPltTab');
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
	var href = getIframeContent('worksPltFrm').location.href;
	var p = href.indexOf('?');
	var q = href.indexOf('&colYn=');
	if(p>0 && q>p){
		bxId = $('#toWorksForm #bxId').val();
		return '${_cxPth}/wf/works/listWorks.do'+href.substring(p,q)+'&formNo='+bxId;
	}
	return null;
}

$(window).bind("pageshow", function (event) {
	if (event.originalEvent.persisted) {
	}else {
		var tabId=$('#worksPltTab ul > li[class="basic_open"]').attr('data-areaid');
		var bxId=$('#toWorksForm #bxId').val();
		if(tabId!=undefined && bxId!=undefined && !tabId.startsWith(bxId)){
			$('#worksPltTab ul > li').each(function(){
				$(this).attr('class','basic');
				if($(this).attr('data-areaid').startsWith(bxId))
					$(this).attr('class','basic_open');
			});
		}
	}
});

//-->
</script>
<div class="ptlbody_ct">
	<c:if test="${fn:length(wfPltSetupDVoList)>0 }">
	<u:tabGroup id="worksPltTab" noBottomBlank="true"><c:forEach
		items="${wfPltSetupDVoList}" var="wfPltSetupDVo" varStatus="status">
		<u:tab id="worksPltTab" areaId="${wfPltSetupDVo.bxId}PltFrm" title="${wfPltSetupDVo.bxNm}" on="${status.first}"
			onclick="clickWorksPltTab('${wfPltSetupDVo.bxId}', '${wfPltSetupDVo.menuId}');" /></c:forEach>
	</u:tabGroup>
	</c:if>
	<div style="height:4px;"></div>
	<c:if test="${fn:length(wfPltSetupDVoList)>0 }">
	<iframe id="worksPltFrm" name="worksPltFrm"
		src="${_cxPth}/wf/works/listWorksPltFrm.do?menuId=${wfPltSetupDVoList[0].menuId}&colYn=${param.colYn}&hghtPx=${ifrHght}&formNo=${wfPltSetupDVoList[0].bxId}"
		style="width:100%; height:${ifrHght}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	</c:if>
	<c:if test="${fn:length(wfPltSetupDVoList)==0 }">
		<table class="ptltable" id="ptltable" border="0" cellpadding="0" cellspacing="0" style="width:100%; table-layout:fixed;">
		<tbody>
		<tr id="lineTr"><td class="line"></td></tr>
		<tr id="headerTr">
			<td class="head_ct"><div class="ellipsis" title="<u:msg titleId="cols.subj" />"><u:msg titleId="cols.subj"
				/></div></td>
		</tr>
		<tr id="lineTr"><td class="line"></td></tr>
		<tr><td class="nodata" ><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td></tr>
		<tr id="lineTr"><td class="line"></td></tr>
	</tbody>
	</table>
	</c:if>
</div>

<form id="toWorksForm" target="_parent">
<u:input id="bxId" value="${wfPltSetupDVoList[0].bxId}" type="hidden" />
<u:input id="viewPage" value="viewWorks" type="hidden" />
<u:input id="menuId" value="${wfPltSetupDVoList[0].menuId}" type="hidden" />
</form>