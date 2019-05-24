<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="hghtPx,colYn,pageRowCnt"/>
<c:set var="ifrHght" value="${param.hghtPx -43}" />
<script type="text/javascript">
<!--<%
// 포틀릿 탭 클릭 %>
function clickHelpPltTab(bxId, menuId){
	var ifrm = getIframeContent("helpPltFrm");
	ifrm.reload('${_cxPth}/wh/help/list'+bxId+'PltFrm.do?menuId='+menuId+'&colYn=${param.colYn}&hghtPx=${ifrHght}&bxId='+bxId);
	var $form = $("#toHelpForm");
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
	resizeForPltTab('helpPltTab');
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
	var href = getIframeContent('helpPltFrm').location.href;
	var p = href.indexOf('?');
	var q = href.indexOf('&colYn=');
	if(p>0 && q>p){
		bxId = $('#toHelpForm #bxId').val();
		return '${_cxPth}/wh/help/list'+bxId+'.do'+href.substring(p,q);
	}
	return null;
}

$(window).bind("pageshow", function (event) {
	if (event.originalEvent.persisted) {
	}else {
		var tabId=$('#helpPltTab ul > li[class="basic_open"]').attr('data-areaid');
		var bxId=$('#toHelpForm #bxId').val();
		if(tabId!=undefined && bxId!=undefined && !tabId.startsWith(bxId)){
			$('#helpPltTab ul > li').each(function(){
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
	<c:if test="${fn:length(whPltSetupDVoList)>0 }">
	<u:tabGroup id="helpPltTab" noBottomBlank="true"><c:forEach
		items="${whPltSetupDVoList}" var="whPltSetupDVo" varStatus="status">
		<u:tab id="helpPltTab" areaId="${whPltSetupDVo.bxId}PltFrm" title="${whPltSetupDVo.bxNm}" on="${status.first}"
			onclick="clickHelpPltTab('${whPltSetupDVo.bxId}', '${whPltSetupDVo.menuId}');" /></c:forEach>
	</u:tabGroup>
	</c:if>
	<div style="height:4px;"></div>
	<c:if test="${fn:length(whPltSetupDVoList)>0 }">
	<iframe id="helpPltFrm" name="helpPltFrm"
		src="${_cxPth}/wh/help/list${whPltSetupDVoList[0].bxId }PltFrm.do?menuId=${whPltSetupDVoList[0].menuId}&colYn=${param.colYn}&hghtPx=${ifrHght}&bxId=${whPltSetupDVoList[0].bxId}"
		style="width:100%; height:${ifrHght}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	</c:if>
	<c:if test="${fn:length(whPltSetupDVoList)==0 }">
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

<form id="toHelpForm" target="_parent">
<u:input id="bxId" value="${whPltSetupDVoList[0].bxId}" type="hidden" />
<u:input id="viewPage" value="${fn:replace(whPltSetupDVoList[0].bxId,'list','view')}" type="hidden" />
<u:input id="menuId" value="${whPltSetupDVoList[0].menuId}" type="hidden" />
</form>