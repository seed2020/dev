<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="hghtPx,colYn,pageRowCnt"/>
<c:set var="ifrHght" value="${param.hghtPx -43}" />
<u:authUrl var="setPopUrl" url="/cu/send/setNotePltPop.do" authCheckUrl="/cu/send/listNote.do"/><!-- set 팝업 호출관련 url 조합(menuId추가) -->
<u:authUrl var="listRecvPopUrl" url="/cu/send/listRecvPop.do" authCheckUrl="/cu/send/listNote.do"/><!-- 수신확인 팝업 호출관련 url 조합(menuId추가) -->
<script type="text/javascript">
<!--<%
// 포틀릿 탭 클릭 %>
function clickNotePltTab(bxId, menuId){
	var ifrm = getIframeContent("notePltFrm");
	ifrm.reload('${_cxPth}/cu/'+(bxId=='send' ? 'send' : 'recv')+'/listNotePltFrm.do?menuId='+menuId+'&colYn=${param.colYn}&hghtPx=${ifrHght}&bxId='+bxId);
}<%
// 포틀릿 쿼리 스트링 %>
var pltQueryString = '';
function setQueryString(qstr){
	pltQueryString = qstr;
}<%
//onload %>
$(document).ready(function() {<%
	// 텝 - 가로폭에 따라서 두줄로 나타나지 않게 숨김 %>
	resizeForPltTab('notePltTab');
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
	var href = getIframeContent('notePltFrm').location.href;
	var p = href.indexOf('?');
	var q = href.indexOf('&colYn=');
	if(p>0 && q>p){
		bxId = $('#toNoteForm #bxId').val();
		return '${_cxPth}/cu/'+(bxId=='send' ? 'send' : 'recv')+'/listNote.do'+href.substring(p,q);
	}
	return null;
}
<% // [등록버튼] - 등록 팝업 %>
function setNotePop(){
	top.dialog.open('setNoteDialog','<u:msg titleId="cm.cols.send.note" alt="쪽지 보내기" />', '${setPopUrl}');
}<% // [수신확인버튼] - 수신확인 팝업 %>
function listRecvPop(sendNo){
	dialog.open('listRecvDialog','<u:msg titleId="cm.btn.detl" alt="상세정보" />', '${listRecvPopUrl}');
}
$(window).bind("pageshow", function (event) {
	if (event.originalEvent.persisted) {
	}else {
		var tabId=$('#notePltTab ul > li[class="basic_open"]').attr('data-areaid');
		var bxId=$('#toNoteForm #bxId').val();
		if(tabId!=undefined && bxId!=undefined && !tabId.startsWith(bxId)){
			$('#notePltTab ul > li').each(function(){
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
	<c:if test="${fn:length(cuNotePltSetupDVoList)>0 }">
	<u:tabGroup id="notePltTab" noBottomBlank="true"><c:forEach
		items="${cuNotePltSetupDVoList}" var="cuNotePltSetupDVo" varStatus="status">
		<u:tab id="notePltTab" areaId="${cuNotePltSetupDVo.bxId}PltFrm" title="${cuNotePltSetupDVo.bxNm}" on="${status.first}"
			onclick="clickNotePltTab('${cuNotePltSetupDVo.bxId}', '${cuNotePltSetupDVo.menuId}');" /></c:forEach>
		<u:tabButton titleId="cm.cols.send.note" alt="쪽지 보내기" href="javascript:setNotePop();" spanStyle="font: 12px 'dotum','arial';color:rgb(4, 65, 179);font-weight:bold;" auth="W"/>
	</u:tabGroup>
	</c:if>
	<div style="height:4px;"></div>
	<c:if test="${fn:length(cuNotePltSetupDVoList)>0 }">
	<u:set var="urlSuffix" test="${cuNotePltSetupDVoList[0].bxId eq 'send' }" value="send" elseValue="recv"/>
	
	<iframe id="notePltFrm" name="notePltFrm"
		src="${_cxPth}/cu/${urlSuffix }/listNotePltFrm.do?menuId=${cuNotePltSetupDVoList[0].menuId}&colYn=${param.colYn}&hghtPx=${ifrHght}&bxId=${cuNotePltSetupDVoList[0].bxId}"
		style="width:100%; height:${ifrHght}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	</c:if>
	<c:if test="${fn:length(cuNotePltSetupDVoList)==0 }">
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

<form id="toNoteForm" target="_parent">
<u:input id="bxId" value="${cuNotePltSetupDVoList[0].bxId}" type="hidden" />
</form>