<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="nonPageParams" excludes="docId,pageNo,listPage,data,noCache,mode,tgtId"/>
<u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/>
<u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="setSelInfos"/>
<script type="text/javascript">
//<![CDATA[
<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name, rescId){
	if(id==null || id=='') return;
}
var clkTab = '${tabList[0]}';
function getTabId(){
	var tabClsNm = "tabbtn";
	var sendTabArea = $('#sendTabArea'); 
	var onId = sendTabArea.find('.'+tabClsNm+'_on').attr('id');
	return onId;
}<%// [TAB] 보내기 %>
function selectBtn(id){
	$('#sendBtnArea').find("#subDocListBtn").hide();
	if(id == 'doc') $('#sendBtnArea').find("#subDocListBtn").show();
	clkTab = id;
}; 
<%// [TAB] 보내기 %>
function sendTabBtn(id){
	$('#sendBtnArea').find("#subDocListBtn").hide();
	var tabClsNm = "tabbtn";
	var tabViewArea = $('#tabViewArea'); 
	var sendTabArea = $('#sendTabArea'); 
	var onId = sendTabArea.find('.'+tabClsNm+'_on').attr('class', tabClsNm).attr('id');
	tabViewArea.children('#'+onId).hide();
	sendTabArea.find('#'+id).attr('class', tabClsNm+'_on');
	tabViewArea.children('#'+id).show();
	clkTab = id;
	if(id == 'doc') $('#sendBtnArea').find("#subDocListBtn").show();
};<%// [하단버튼:] 보내기 %>
function setSendWritePop(){
	var selId = getSelectId();
	if(selId == null){
		$m.msg.alertMsg("dm.jsp.setDoc.not.fldSub");<%//dm.jsp.setDoc.not.fldSub=하위 '폴더'를 선택 후 사용해 주십시요.%>
		return;
	}
	if(selId == 'NONE'){
		$m.msg.alertMsg("dm.msg.not.save.emptyCls");<%//dm.msg.not.save.emptyCls='미분류' 로 저장할 수 없습니다.%>
		return;
	}
	
	if(clkTab=='TEST'){
		var menuId = getMenuIdByUrl("/bb/listBull.do?brdId="+selId);
		$m.ajax("/dm/doc/saveSendAjx.do?menuId=${menuId}", {docId:'${param.docId}',docTyp:'${param.docTyp}'}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.sendNo != null) {
				//sendBbPop(selId, data.sendNo);
				$m.nav.next(null,'/bb/setSendSub.do?brdId='+selId+(menuId=='' ? '' : "&menuId="+menuId)+'&sendNo='+data.sendNo+'&returnFunc=closePop');
			}
		});
	}else{
		$m.nav.next(null,'/dm/doc/setSendWriteSub.do?${paramsForList}&docId=${param.docId}${paramStorIdQueryString}&selId='+selId+'&tabId='+clkTab);
	}
	
	//var url = '/dm/doc/setSendWriteSub.do?${paramsForList}&docId=${param.docId}${paramStorIdQueryString}&selId='+selId+'&tabId='+clkTab;
	//$m.nav.next(null,url);
};<%// [하단버튼:] 하위문서 %>
function setSubDocPop(){
	var selId = getSelectId();
	if(selId == null){
		$m.msg.alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
		return;
	//	selId = '';
	}
	if(selId == 'NONE'){
		$m.msg.alertMsg("dm.msg.not.save.emptyCls");<%//dm.msg.not.save.emptyCls='미분류' 로 저장할 수 없습니다.%>
		return;
	}
	findDocPop(selId, null);
};<%// [팝업] 하위문서 %>
function findDocPop(selId, callback){
	var url = '/dm/doc/findDocSub.do?${paramsForList}&fncMul=N&tabId='+clkTab;
	if(selId != null) url+= "&fldId="+selId;
	if(callback != null) url+= "&callback="+callback;
	$m.nav.next(null, url);
	//dialog.open('findDocPop', '<u:msg titleId="dm.jsp.search.doc.title" alt="문서조회" />', url);
};<%// [하단버튼:] 보내기옵션 %>
function setSendOptPop(){
	var selId = getSelectId();
	if(selId == null){
		$m.msg.alertMsg("dm.jsp.setDoc.not.fldSub");<%//dm.jsp.setDoc.not.fldSub=하위 '폴더'를 선택 후 사용해 주십시요.%>
		return;
	}
	if(selId == 'NONE'){
		$m.msg.alertMsg("dm.msg.not.save.emptyCls");<%//dm.msg.not.save.emptyCls='미분류' 로 저장할 수 없습니다.%>
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
	$form.appendHidden({name:'docPid',value:arrs});
	//parent.dialog.close('findDocPop');
	sendSave(false);
}<%// [하단버튼] 보내기%>
function sendSave(valid){
	if(clkTab == null) return;
	var selId = getSelectId();
	var $form = $("#sendForm");	
	if(valid){
		if(selId == null){
			$m.msg.alertMsg("dm.jsp.setDoc.not.fldSub");<%//dm.jsp.setDoc.not.fldSub=하위 '폴더'를 선택 후 사용해 주십시요.%>
			return;
		}
		$form.find("[name='docPid']").remove();
	}
	$form.attr('method','post');
	$form.attr('action','/dm/doc/transSendDocPost.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	if(selId != null) $form.find('input[name="selId"]').val(selId);
	$form.find('input[name="tabId"]').val(clkTab);
	$m.nav.post($form);
	//$form.submit();
	
}<%// [하단버튼] 보내기 - 사용안함 %>
function transSendAjx(valid){
	if(clkTab == null) return;
	var selId = getSelectId();
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
}<%
//폴더 - 트리 %>
function selTree(tree, url, param){
	tree.setRoot('ROOT', '<u:msg titleId="cols.fld" alt="폴더"/>');
	
	$m.ajax('${_cxPth}/dm/doc/'+url+'.do?menuId=${menuId}${paramStorIdQueryString}', param, function(data){
		if(data.openLvl != null) tree.openLvl = data.openLvl;
		var i, objVo, objArr = data.dmFldBVoList;
		for(i=0;i<objArr.length;i++){
			objVo = objArr[i];
			tree.add(objVo.fldPid, objVo.fldId, objVo.fldNm, 'F' , (objVo.fldTypCd == 'F' ? '0' : '1')+objVo.sortOrdr, objVo.rescId, {id:objVo.fldId, nm:objVo.fldNm, pid:objVo.fldPid, storId:objVo.storId, rescId:objVo.rescId, useYn:objVo.useYn, title:objVo.fldId, fldGrpId:objVo.fldGrpId});
		}
		tree.draw();
	}, {async:true});
}
<% // 트리용 Object %>
var tree = null;
function initTree(tabId){
	var param = null;
	tree = TREE.create(tabId+'Tree');
	tree.onclick = 'onTreeClick';	
	tree.setSkin("${_skin}");
	<c:if test="${not empty openLvl}">tree.openLvl = ${openLvl};</c:if><%// 특정폴더 하위나 상위폴더 조회시 모든 트리를 펼쳐 보이게함 %>
	<c:if test="${not empty treeSelectOption}">TREE.selectOption = ${treeSelectOption};</c:if><%// 여러개 선택 가능 하도록 %>
	var url = "treeDocFldAjx";
	if(tabId == 'psn') url = "treeFldAjx";
	else {
		url = "treeDocFldAjx";
		param = {popYn:'Y'};
	}
	selTree(tree, url, param);
}
var brdArea = null;
<% // 게시판용 Object %>
function initBrd(){
	$m.ajax('${_cxPth}/dm/doc/listBrdAjx.do?menuId=${menuId}', null, function(html){
		if(brdArea==null) brdArea = $('#section').children('#tabViewArea').children('#brdArea').find('#brdListArea');
		brdArea.html(html);
	}, {mode:'HTML', async:true});
}<%// 트리의 선택 ID 리턴 %>
function getSelectId(){
	//var tabId = getTabId();
	//alert(tabId);
	if(clkTab == 'brd'){
		return brdArea.find('input:checked:first').val();
	}else{
		var sel = TREE.getTree(clkTab+'Tree').selected;
		if($(sel).attr('id')=='ROOTLI'){
			return null;
		} else {
			var exts = TREE.getExtData(clkTab+'Tree','exts');
			if(exts == null || exts.fldGrpId == 'COMP' || exts.fldGrpId == 'DEPT' || exts.fldGrpId == 'NONE') return null;
			return exts.id;
		}
	}
	
}<%// 트리의 선택 ID 리턴 %>
function getSelectAllId(){
	var sel = TREE.getTree('selTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('selTree','exts');
		if(exts == null) return null;
		return exts.id;
	}
}
$(document).ready(function() {
	<%// 트리 초기화 %>
	<c:forEach var="tab" items="${tabList }" varStatus="status">
		<c:if test="${tab eq 'brd'}">initBrd();</c:if>
		<c:if test="${tab ne 'brd'}">initTree('${tab}');</c:if>
	</c:forEach>
	var section = $("#section");
	$layout.tab.init(section.find('.tabarea2 dl'), section.find('#tabViewArea'), 'tabbtn');	
});
//]]>
</script>

<section id="section">
	<c:set var="subDocListBtnStyle" value="style=\"display:none;\""/>
	<div class="unified_tab" id="sendTabArea">
		<div class="tabarea2">
			<dl>
			<c:forEach var="tab" items="${tabList }" varStatus="status">
			<c:if test="${status.count==1 && tab eq 'doc'}"><c:set var="subDocListBtnStyle" value=""/></c:if>
			<c:if test="${status.count>1 }"><dd class="line"></dd></c:if>
			<dd class="tabsize2" onclick="$layout.tab.on($(this).children().attr('id'));selectBtn('${tab}');" style="width:${(100/fn:length(tabList))-2}%"><div class="tabbtn${status.count == 1 ? '_on' : ''}" id="${tab }Area" 
			><u:msg titleId="dm.cols.send.${tab }" alt="${tabTitle }"/></div></dd>
			</c:forEach>
			</dl>
		</div>
		</div>
	<div id="tabViewArea">
		<div id="docArea" class="unified_tree2" style="bottom:50px;">
			<div class="unified_treearea" style="overflow:auto;">
				<div class="treearea"><div class="tree" id="docTree"></div></div>
			</div>
		</div>
		<div id="psnArea" class="unified_tree2" style="bottom:50px;display:none;">
			<div class="unified_treearea" style="overflow:auto;">
				<div class="treearea"><div class="tree" id="psnTree"></div></div>
			</div>
		</div>
		<div id="brdArea" class="unified_listarea2" style="display:none;">
		<div class="listarea">
		<div class="blank5"></div>
		<article id="brdListArea">
		</article>
		</div>
		</div>
	</div>
	<div class="unified_btn2">
<div class="btnarea" id="sendBtnArea">
	<div class="size">
	<dl>
		<c:if test="${empty param.mode || param.mode eq 'copy'}">
		<c:if test="${isPsn == false }"><dd class="btn" onclick="sendSave(true);"><u:msg titleId="dm.btn.sendFast" alt="빠른보내기"/></dd></c:if>
		<u:set var="sendFunc" test="${!empty param.multi && param.multi eq 'Y' }" value="setSendOptPop();" elseValue="setSendWritePop();"/>
		<dd id="sendOptBtn" class="btn" onclick="${sendFunc }"><u:msg titleId="dm.btn.send" alt="보내기"/></dd>
	</c:if>
	<c:if test="${!empty param.mode && param.mode eq 'move'}">
		<dd class="btn" onclick="sendSave(true);"><u:msg titleId="dm.btn.send" alt="보내기"/></dd>
	</c:if>
	<dd id="subDocListBtn" class="btn" onclick="setSubDocPop();" ${subDocListBtnStyle }><u:msg titleId="dm.btn.sendSubDoc" alt="하위문서로 보내기" /></dd>
	<dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
	</dl>
	</div>
</div>
</div>
<form id="sendForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="docId" value="${param.docId}" />
<input type="hidden" name="docTyp" value="${param.docTyp}" />
<input type="hidden" name="selId" /><!-- 대상ID -->
<input type="hidden" name="tabId" /><!-- 대상구분[폴더,개인폴더,게시판] -->
<input type="hidden" name="mode" value="${empty param.mode ? 'copy' : param.mode}"/><!-- 복사,이동 -->
<c:if test="${!empty param.dialog }"><input type="hidden" name="dialog" value="${param.dialog }"/></c:if><!-- 팝업명 -->
<input type="hidden" name="multi" value="${param.multi}" />
<m:input type="hidden" id="viewPage" value="/dm/doc/viewDoc.do?${params}" />
<c:if test="${!empty param.listPage }"><m:input type="hidden" id="listPage" value="/dm/doc/${param.listPage}.do?${nonPageParams}" /></c:if>
<!-- 저장소ID -->
<c:if test="${!empty paramStorId }">
<m:input type="hidden" id="paramStorId" value="${paramStorId}" />
</c:if>
</form>
</section>