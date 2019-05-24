<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="nonPageParams" excludes="docId,pageNo,listPage,data,mode"/>
<u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/>
<script type="text/javascript">
<!--var clkTab = null;
function sendBbPop(brdId, sendNo){
	var width = 900; 
    var height = 750; 
    var top = (screen.availHeight / 2) - (height / 2); 
    var left = (screen.availWidth / 2) - (width / 2);
    var opt ="location=0, status=0, scrollbars=0,resizeable=0, width="+ width +", height="+ height +", top=" + top + ", left=" + left;
	window.open('/cm/bb/setSendPop.do?brdId='+brdId+'&sendNo='+sendNo, 'setSendWriteDialog', opt);
}
<%// [TAB] 보내기 %>
function sendTabBtn(id){
	$('#sendBtnArea').find("#subDocListBtn").hide();
	if(id == 'doc'){
		$('#sendBtnArea').find("#subDocListBtn").show();
		reloadFrame('sendFrm', './treeDocFldFrm.do?menuId=${menuId}&popYn=Y&initFrm=N');
	}else if(id == 'psn'){
		reloadFrame('sendFrm', './treeFldFrm.do?menuId=${menuId}&initFrm=N');
	}else if(id == 'brd'){
		reloadFrame('sendFrm', './listBrdFrm.do?menuId=${menuId}&initFrm=N');
	}
	clkTab = id;
};<%// [하단버튼:] 보내기 %>
function setSendWritePop(){
	var selId = getIframeContent('sendFrm').getSelectId();
	if(selId == null){
		alertMsg("dm.jsp.setDoc.not.fldSub");<%//dm.jsp.setDoc.not.fldSub=하위 '폴더'를 선택 후 사용해 주십시요.%>
		return;
	}
	if(selId == 'NONE'){
		alertMsg("dm.msg.not.save.emptyCls");<%//dm.msg.not.save.emptyCls='미분류' 로 저장할 수 없습니다.%>
		return;
	}
	
	if(clkTab=='brd'){
		var menuId = getMenuIdByUrl("/bb/listBull.do?brdId="+selId);
		callAjax('./saveSendAjx.do?menuId=${menuId}', {docId:'${param.docId}',docTyp:'${param.docTyp}'}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.sendNo != null) {
				//sendBbPop(selId, data.sendNo);
				dialog.open('setSendWritePop', '<u:msg titleId="dm.jsp.sendWrite.title" alt="보내기작성" />', '/bb/setSendPop.do?brdId='+selId+(menuId=='' ? '' : "&menuId="+menuId)+'&sendNo='+data.sendNo+'&returnFunc=closePop');
			}
		});
		
	}else{
		dialog.open('setSendWritePop', '<u:msg titleId="dm.jsp.sendWrite.title" alt="보내기작성" />', './setSendWritePop.do?${paramsForList}&docId=${param.docId}${paramStorIdQueryString}&selId='+selId+'&tabId='+clkTab);	
	}
	
};<%// [하단버튼:] 하위문서 %>
function setSubDocPop(){
	var selId = getIframeContent('sendFrm').getSelectAllId();
	if(selId == null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
		return;
	//	selId = '';
	}
	if(selId == 'NONE'){
		alertMsg("dm.msg.not.save.emptyCls");<%//dm.msg.not.save.emptyCls='미분류' 로 저장할 수 없습니다.%>
		return;
	}
	findDocPop(selId, null);
};<%// [팝업] 하위문서 %>
function findDocPop(selId, callback){
	var url = './findDocPop.do?${paramsForList}&fncMul=N&tabId='+clkTab;
	if(selId != null) url+= "&fldId="+selId;
	if(callback != null) url+= "&callback="+callback;
	dialog.open('findDocPop', '<u:msg titleId="dm.jsp.search.doc.title" alt="문서조회" />', url);
};<%// [하단버튼:] 보내기옵션 %>
function setSendOptPop(){
	var selId = getIframeContent('sendFrm').getSelectId();
	if(selId == null){
		alertMsg("dm.jsp.setDoc.not.fldSub");<%//dm.jsp.setDoc.not.fldSub=하위 '폴더'를 선택 후 사용해 주십시요.%>
		return;
	}
	if(selId == 'NONE'){
		alertMsg("dm.msg.not.save.emptyCls");<%//dm.msg.not.save.emptyCls='미분류' 로 저장할 수 없습니다.%>
		return;
	}
	dialog.open('setSendOptDialog', '<u:msg titleId="dm.jsp.sendWrite.title" alt="보내기작성" />', '/cm/doc/setSendOptPop.do?${paramsForList}&docId=${param.docId}&selId='+selId+'&tabId='+clkTab);
	//params = '?${paramsForList}&docId=${param.docId}&selId='+selId+'&tabId='+clkTab;
	//sendDocOptPop(params);
};<%// [저장] 옵션포함저장 %>
function saveSendOpt(arrs){
	var $form = $("#sendForm");
	$.each(arrs,function(index,vo){
		//$form.find("[name='"+vo.name+"']").remove();
		$form.appendHidden({name:vo.name,value:vo.value});
	});
	sendSave(false);
}<%// [저장] 하위문서 저장 %>
function saveSubDoc(arrs){
	var $form = $("#sendForm");
	$form.find("[name='docPid']").remove();
	$form.appendHidden({name:'docPid',value:arrs[0]});
	parent.dialog.close('findDocPop');
	sendSave(false);
}<%// [하단버튼] 보내기%>
function sendSave(valid){
	if(clkTab == null) return;
	var selId = getIframeContent('sendFrm').getSelectId();
	var $form = $("#sendForm");	
	if(valid){
		if(selId == null){
			alertMsg("dm.jsp.setDoc.not.fldSub");<%//dm.jsp.setDoc.not.fldSub=하위 '폴더'를 선택 후 사용해 주십시요.%>
			return;
		}
		$form.find("[name='docPid']").remove();
	}
	$form.attr('method','post');
	$form.attr('action','./transSendDoc.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	if(selId != null) $form.find('input[name="selId"]').val(selId);
	$form.find('input[name="tabId"]').val(clkTab);
	$form.submit();
	
}<%// [하단버튼] 보내기 - 사용안함 %>
function transSendAjx(valid){
	if(clkTab == null) return;
	var selId = getIframeContent('sendFrm').getSelectId();
	if(selId == null) return;
	if(confirmMsg("cm.cfrm.save")) {<%//cm.cfrm.save=저장 하시겠습니까? %>
		callAjax('./transSendDocAjx.do?menuId=${menuId}', {docIds:'${param.docId}',selId:selId,tabId:clkTab,mode:'copy'}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				//dialog.close("sendPop");
			}
		});
	}
}<%// [닫기] 팝업 %>
function closePop(){
	dialog.close('setSendWritePop');
}
$(document).ready(function() {
	<c:if test="${fn:length(tabList) > 0}">sendTabBtn('${tabList[0]}');</c:if>
	//reloadFrame('sendFrm', './treeDocFldFrm.do?menuId=${menuId}');
});
//-->
</script>
<div style="width:500px;">
<u:tabGroup noBottomBlank="${true}">
	<c:forEach var="tab" items="${tabList }" varStatus="status">
		<u:msg var="tabTitle" titleId="dm.cols.send.${tab }"/>
		<c:if test="${tab ne 'brd' }"><u:tab alt="${tabTitle }" titleId="dm.cols.send.${tab }" on="${status.count == 1 }" onclick="sendTabBtn('${tab}');" /></c:if>
		<c:if test="${tab eq 'brd' }"><u:internalIp><u:tab alt="${tabTitle }" titleId="dm.cols.send.${tab }"	on="${status.count == 1 }" onclick="sendTabBtn('${tab}');" /></u:internalIp></c:if>
	</c:forEach>
	<%-- <u:tab alt="이메일발송" titleId="cm.btn.emailSend"
		on="false"
		onclick="sendTabBtn('email');" /> --%>
</u:tabGroup>
<iframe id="sendFrm" name="sendFrm" src="/cm/util/reloadable.do" style="width:100%;height:300px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
<u:blank />
<u:buttonArea id="sendBtnArea">
	<c:if test="${empty param.mode || param.mode eq 'copy'}">
		<c:if test="${isPsn == false }"><u:button titleId="dm.btn.sendFast" alt="빠른보내기" onclick="sendSave(true);" /></c:if>
		<u:set var="sendFunc" test="${!empty param.multi && param.multi eq 'Y' }" value="setSendOptPop();" elseValue="setSendWritePop();"/>
		<u:button id="sendOptBtn" titleId="dm.btn.send" alt="보내기" onclick="${sendFunc }" />
	</c:if>
	<c:if test="${!empty param.mode && param.mode eq 'move'}">
		<u:button titleId="dm.btn.send" alt="보내기" onclick="sendSave(true);" />
	</c:if>
	<u:button id="subDocListBtn" titleId="dm.btn.sendSubDoc" alt="하위문서로 보내기" onclick="setSubDocPop();" style="display:none;"/>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
<form id="sendForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="docId" value="${param.docId}" />
<input type="hidden" name="docTyp" value="${param.docTyp}" />
<input type="hidden" name="selId" /><!-- 대상ID -->
<input type="hidden" name="tabId" /><!-- 대상구분[폴더,개인폴더,게시판] -->
<input type="hidden" name="mode" value="${empty param.mode ? 'copy' : param.mode}"/><!-- 복사,이동 -->
<c:if test="${!empty param.dialog }"><input type="hidden" name="dialog" value="${param.dialog }"/></c:if><!-- 팝업명 -->
<input type="hidden" name="multi" value="${param.multi}" />
<u:input type="hidden" id="viewPage" value="./viewDoc.do?${params}" />
<c:if test="${!empty param.listPage }"><u:input type="hidden" id="listPage" value="./${param.listPage}.do?${nonPageParams}" /></c:if>
<!-- 저장소ID -->
<c:if test="${!empty paramStorId }">
<u:input type="hidden" id="paramStorId" value="${paramStorId}" />
</c:if>
</form>
</div>