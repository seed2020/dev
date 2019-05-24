<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="pageSuffix" test="${lstTyp eq 'L' }" value="" elseValue="Frm"/>
<u:set var="frmYn" test="${!empty pageSuffix && pageSuffix == 'Frm' }" value="Y" elseValue="N"/>
<u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/><!-- 저장소ID -->
<u:set var="paramCompIdQueryString" test="${!empty paramCompId }" value="&paramCompId=${paramCompId }" elseValue=""/><!--회사ID -->
<u:set var="paramFldGrpId" test="${empty paramCompId }" value="&fldGrpId=DEPT" elseValue=""/>
<script type="text/javascript">
<!--<% // [버튼] 열람요청상세 %>
function viewOpenReqPop(docGrpId,tgtId){
	parent.dialog.open('viewOpenReqDialog','<u:msg titleId="dm.btn.dtlView.request" alt="열람요청" />','./setOpenApvPop.do?menuId=${menuId}&tgtId='+tgtId+'&docGrpId='+docGrpId);
}<%// [버튼] 열람취소 - 삭제 %>
function openCancel(){
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	var arrs2 = selDocIds('tgtId');
	if(arrs2 == null) return;
	callAjax('./transOpenCancelAjx.do?menuId=${menuId}', {docGrpId:arrs.join(','),tgtId:arrs2.join(',')}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace(location.href);
		}
	});
}<%// [저장] 열람요청승인 - 일괄 %>
function saveOpenApv(param){
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	var arrs2 = selDocIds('tgtId');
	if(arrs2 == null) return;
	param.put('docGrpId',arrs.join(','));
	param.put('tgtId',arrs2.join(','));
	callAjax('./transOpenApvAjx.do?menuId=${menuId}${paramStorIdQueryString}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace(location.href);
		}
	});
}<%// [버튼] 열람승인 %>
function openApv(){
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	parent.dialog.open('openApvDialog','<u:msg titleId="dm.jsp.dtlView.approved.title" alt="열람요청승인" />','./setOpenApvPop.do?menuId=${menuId}');
}<%// [버튼] 이관취소 %>
function transferDel(actKey){
	var param = new ParamMap().getData("setTransferHstForm");
	if(actKey == 'del'){
		var selIds = selDocIds('docGrpId');
		if(selIds == null) return;
		param.put('docGrpId',selIds.join(','))
	}
	param.put('mode','del');
	callAjax('./transferDelAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace(location.href);
		}
	});
}<% // [버튼] 문서이관상세(오류) %>
function viewTransferDtlPop(tranId, docGrpId){
	parent.dialog.open('viewTransferDtlDialog','<u:msg titleId="cm.msg.error" alt="오류" />','./viewTransferDtlPop.do?menuId=${menuId}&tranId='+tranId+'&docGrpId='+docGrpId);
}<%// [저장] 옵션포함저장 - 인수 %>
function transTakovr(arrs){
	var docGrpId = selDocIds('docGrpId');
	if(docGrpId == null) return;
	var json = JSON.parse(arrs);
	var $form = $("#takovrForm");
	$.each(json,function(key,value){
		var values = value.split(',');
		if(values.length>1){
			for(var i=0;i<values.length;i++){
				$form.appendHidden({name:key,value:values[i]});
			}
		}else{
			$form.appendHidden({name:key,value:value});
		}
	});
	$form.attr('method','post');
	$form.attr('action','./transTakovrDoc.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form.find('input[name="docGrpId"]').val(docGrpId.join(','));
	$form.submit();
}<%// [버튼] 재인계 %>
function transReSave(){
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	transTakovrSave('transReSave','dm.cfrm.transReSave',{docGrpId:arrs.join(','),mode:'transReSave',statCd:'O',msgCode:'dm.msg.save.trans'});
}<%// 회사별 인계 %>
function transTgtCompSave(comp){
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	transTakovrSave('transSave','dm.cfrm.transSave',{docGrpId:arrs.join(','),mode:'transSave',tgtOrgId:comp[0].compId,statCd:'O',msgCode:'dm.msg.save.trans',compYn:'Y'});
}<%// [버튼] 선택인계 %>
function transTgtSave(){
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	var option = {titleId:'dm.cols.doc.target.select'};
	var global = "${global}";
	if(global=='Y'){
		dialog.open('findCompDialog', '<u:msg titleId="dm.cols.doc.target.select" alt="대상선택" />', './findCompPop.do?menuId=${menuId}&callback=transTgtCompSave');
	}else{
		searchOrgPop(option, function(orgVo){
			if(orgVo!=null) {
				transTakovrSave('transSave','dm.cfrm.transSave',{docGrpId:arrs.join(','),mode:'transSave',tgtOrgId:orgVo.orgId,statCd:'O',msgCode:'dm.msg.save.trans'});
			}
		});	
	}
}<%// [버튼] 선택인계 %>
function transTakovrSave(actKey, msgCd, param){
	if (confirmMsg(msgCd)) {	<% // dm.cfrm.transSave=해당문서를 인계하시겠습니까? %>
		<% // dm.msg.save.trans=문서를 인계하였습니다.%>				
		callAjax('./transTakovrAjx.do?menuId=${menuId}', param, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
}<%// [버튼] 인계취소 %>
function transTgtCancel(){
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	transTakovrSave('transCancel','dm.cfrm.transCancel',{docGrpId:arrs.join(','),mode:'transCancel',statCd:'C',msgCode:'dm.msg.cancel.trans'});
}<%// [버튼] 인수 - 승인 %>
function transTakovrApvd(){
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	dialog.open('setSendOptDialog', '<u:msg titleId="dm.btn.trans.takovrApvd" alt="인수승인" />', '/cm/doc/setSendOptPop.do?menuId=${menuId}&tabId=doc&callback=transTakovr${paramFldGrpId}&mode=takovr');
}<%// [버튼] 인수 - 반려 %>
function transTakovrRjt(){
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	transTakovrSave('transCancel','dm.cfrm.takovrRjt',{docGrpId:arrs.join(','),mode:'transTakovr',statCd:'R',paramCompId:'${paramCompId}'});
}<%// [팝업] 조회이력 %>
function readHst(docId){
	var url = './listDocTaskPop.do?menuId=${menuId}&docId='+docId+'&single=Y&srchCd=view&dialog=docHstPop${paramStorIdQueryString}';
	parent.dialog.open('docHstPop', '<u:msg titleId="dm.cols.auth.docHst" alt="문서이력" />', url);
};<%// [팝업] 보내기 %>
function send(){
	var arrs = selDocIds('docId');
	if(arrs == null) return;
	var url = './sendPop.do?${params}&docTyp=doc&dialog=sendPop&multi=${multi}${paramStorIdQueryString}&docId='+arrs.join(',');
	parent.dialog.open('sendPop', '<u:msg titleId="cm.btn.send" alt="보내기" />', url);
};<%// [팝업] 이동 %>
function move(){
	var arrs = selDocIds('docId');
	if(arrs == null) return;
	var url = './sendPop.do?${params}&docTyp=doc&dialog=sendPop&mode=move&multi=${multi}${paramStorIdQueryString}&docId='+arrs.join(',');
	parent.dialog.open('sendPop', '<u:msg titleId="dm.jsp.send.move.title" alt="문서이동" />', url);
};<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}<%// [목록 URL] 조회 %>
function getPageUrl(page){
	return $('#'+page).val();
}<%// [우하단 버튼] 등록 %>
function setDoc(seq){
	var url = './${setPage}.do?${paramsForList }${paramStorIdQueryString}';
	if(seq != undefined) url+= '&docId='+seq;
	location.href = url;
}<% // [목록:제목] 상세 조회 %>
function viewDoc(id,docGrpId,tgtId) {
	callAjax('./getViewOptAjx.do?menuId=${menuId}${paramStorIdQueryString}', {docGrpId:docGrpId,viewPage:'${viewPage}'}, function(data) {
		if ((data.popYn == null || data.popYn == 'N') && data.message != null) {
			alert(data.message);
		}
		if (data.popYn != null && data.popYn == 'Y' && confirm(data.message)) {
			parent.dialog.open("viewDocReqCfrmDialog", '<u:msg titleId="dm.jsp.dtlView.request.title" alt="문서열람요청" />', "./viewDocReqCfrmPop.do?menuId=${menuId}&docGrpId="+docGrpId);
		}
		if (data.result == 'ok') {
			var url = './${viewPage}.do?${paramsForList }${paramStorIdQueryString}${paramCompIdQueryString}&docId=' + id;
			if(tgtId != undefined && tgtId != '') url+= '&tgtId='+tgtId;
			location.href = url;
			//location.href = './${viewPage}.do?${paramsForList }${paramStorIdQueryString}&docId=' + id;
		}
	});
}<%// 탭 클릭 - 목록:L/폴더:F/분류:C %>
function toggleTabBtn(tabCd){
	var $form = $("#searchForm");
	$form.find("input[name='lstTyp']").remove();
	$form.appendHidden({name:'lstTyp',value:tabCd});
	$form.submit();
}<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#listArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};<%// 선택목록 리턴 %>
function selDocIds(idNm){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr!=null) return selRowInArr(arr, idNm);
	else return null;
};
<%// 배열에 담긴 목록%>
function selRowInArr(rowArr, idNm){
	var objArr = [], $docId;
	for(var i=0;i<rowArr.length;i++){
		$docId = $(rowArr[i]).find("input[name='"+idNm+"']");
		if($docId.val()!=''){
			objArr.push($docId.val());
		}
	}
	return objArr;
};<%// 상태코드별 메세지[복원,폐기,삭제] %>
function getMsgCd(statCd){
	var arr = [];
	if(statCd == 'C') {
		arr.push("dm.cfrm.recovery");
		arr.push("dm.msg.recovery.success");
		return arr;
	}else if(statCd == 'F'){
		arr.push("dm.cfrm.disuse");
		arr.push("");
		return arr;
	}
	arr.push("cm.cfrm.del");
	arr.push("");
	return arr;
}<% // [하단버튼:삭제] %>
function delDocList(statCd) {
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	var msgCd = getMsgCd(statCd);
	if (confirmMsg(msgCd[0])) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transDocDelListAjx.do?menuId=${menuId}${paramStorIdQueryString}', {docGrpId:arrs.join(','),statCd:statCd,msgCode:msgCd[1]}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
}<%// 저장, 삭제시 리로드 %>
function reloadDocFrm(url, dialogId){
	//팝업 닫기
	if(dialogId != undefined && dialogId != null && dialogId !='') {
		if(dialogId == 'all') parent.dialog.closeAll();
		else parent.dialog.close(dialogId);
	}
	
	if(url != undefined && url != null) location.replace(url);
	else location.replace(location.href);
};
<% // [팝업] 파일목록 조회 %>
function viewFileListPop(id,docGrpId,tgtId) {
	callAjax('./getViewOptAjx.do?menuId=${menuId}${paramStorIdQueryString}', {docGrpId:docGrpId,viewPage:'${viewPage}'}, function(data) {
		if ((data.popYn == null || data.popYn == 'N') && data.message != null) {
			alert(data.message);
		}
		if (data.popYn != null && data.popYn == 'Y' && confirm(data.message)) {
			parent.dialog.open("viewDocReqCfrmDialog", '<u:msg titleId="dm.jsp.dtlView.request.title" alt="문서열람요청" />', "./viewDocReqCfrmPop.do?menuId=${menuId}&docGrpId="+docGrpId);
		}
		if (data.result == 'ok') {
			var url = './viewFileListPop.do?${paramsForList }${paramStorIdQueryString}${paramCompIdQueryString}&docGrpId='+docGrpId+'&docId=' + id;
			if(tgtId != undefined && tgtId != '') url+= '&tgtId='+tgtId;
			parent.dialog.open('viewFileListDialog','<u:msg titleId="cols.att" alt="첨부" />', url);
		}
	});	
}

$(document).ready(function() {
	/* <c:if test="${pageSuffix eq 'Frm'}">parent.applyDocBtn('list','${authJson }');</c:if> */
	setUniformCSS();
	<c:if test="${pageSuffix eq 'Frm'}">parent.applyDocBtn();</c:if>
	<c:if test="${frmYn eq 'Y' && (param.lstTyp eq 'C' || param.lstTyp eq 'F' || param.lstTyp eq 'B') && (!empty param.fldId || !empty param.clsId || !empty param.bumkId)}">
	parent.setParamNmId("${param.lstTyp eq 'C' ? param.clsId : param.lstTyp eq 'B' ? param.bumkId : param.fldId}");
	</c:if>
});
//-->
</script>
<c:if test="${empty pageSuffix}">
<c:if test="${empty storList && empty ptCompBVoList}"><u:title titleId="dm.jsp.search.doc.title" alt="문서조회" menuNameFirst="true" /></c:if>
<jsp:include page="/WEB-INF/jsp/dm/doc/listDocSrch.jsp" />
<form id="searchForm" name="searchForm" action="./${listPage }.do" style="padding:10px;">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="lstTyp" value="${lstTyp}" />
<!-- 저장소ID -->
<c:if test="${!empty paramStorId }">
<u:input type="hidden" id="paramStorId" value="${paramStorId}" />
</c:if>
<c:if test="${!empty paramEntryList}">
<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
	<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
</c:forEach>
</c:if>

<%-- <c:if test="${!empty param.fromStorId }"><u:input type="hidden" id="fromStorId" value="${param.fromStorId}" /></c:if>
<c:if test="${!empty param.tgtStorId }"><u:input type="hidden" id="tgtStorId" value="${param.tgtStorId}" /></c:if>
<c:if test="${!empty param.tranId }"><u:input type="hidden" id="tranId" value="${param.tranId}" /></c:if> --%>

</form>

<c:if test="${fn:length(tabList) > 1}">
<u:tabGroup noBottomBlank="${true}">
	<c:forEach var="tab" items="${tabList }" varStatus="status">
		<u:tab alt="목록전체" titleId="dm.cols.listTyp.${tab[1] }"
		on="${lstTyp == tab[0]}"
		onclick="toggleTabBtn('${tab[0] }');" />
	</c:forEach>
</u:tabGroup>
<u:blank />
</c:if>
</c:if>
<u:set var="style" test="${pageSuffix eq 'Frm' }" value="style='padding:10px;'" elseValue="style='padding-top:10px;'"/>
<form id="listForm" action="./listDoc.do" ${style }>
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="lstTyp" value="${lstTyp}" />
<c:set var="colSpan" value="${fn:length(itemDispList) }"/>
<%-- <u:secu auth="S"><c:set var="colSpan" value="${colSpan+1 }"/></u:secu> --%>
<c:if test="${!empty multi && multi eq 'Y' }"><c:set var="colSpan" value="${colSpan+1 }"/></c:if>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<tr id="headerTr">
		<c:if test="${!empty multi && multi eq 'Y' }"><td width="3%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td></c:if>
		<c:forEach var="dispVo" items="${itemDispList }" varStatus="status">
			<u:set var="wdthPerc" test="${empty dispVo.wdthPerc }" value="10%" elseValue="${dispVo.wdthPerc}%"/>
			<td width="${wdthPerc}" class="head_ct">${dispVo.colmVo.itemDispNm }</td>
		</c:forEach>
		<c:if test="${fn:startsWith(path,'listTmp')}"><c:set var="colSpan" value="${colSpan +1}"/><td width="5%" class="head_ct"><u:msg titleId="cols.note" alt="비고" /></td></c:if>
	</tr>
	<c:set var="viewAtrbs" value="subj,fldNm,clsNm,docNo,verVar"/><!-- 상세화면 비교 컬럼명-->
	<c:set var="uidAtrbs" value="regr,modr,ownr,tgt,reqUser"/><!-- 사용자 비교 컬럼명 -->
	<c:forEach var="map" items="${mapList}" varStatus="status">
		<c:set var="map" value="${map }" scope="request"/>
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' >
			<c:if test="${!empty multi && multi eq 'Y' }"><td class="bodybg_ct"><u:checkbox name="docChk" value="${map.docGrpId }" checked="false" /><u:input type="hidden" name="docId" value="${map.docId }"/><u:input type="hidden" name="docGrpId" value="${map.docGrpId }"/><c:if test="${!empty map.tgtId }"><u:input type="hidden" name="tgtId" value="${map.tgtId }"/></c:if></td></c:if>
			<c:forEach var="dispVo" items="${itemDispList }" varStatus="status">
				<c:choose>
					<c:when test="${dispVo.colmVo.itemTyp eq 'CALENDAR' && dispVo.colmVo.itemTypVa eq 'MERGE'}">
						<u:convertMap var="strtValue" srcId="map" attId="${fn:substring(dispVo.atrbId,0,fn:length(dispVo.atrbId)-2)}StrtDt" type="html" />
						<u:convertMap var="endValue" srcId="map" attId="${fn:substring(dispVo.atrbId,0,fn:length(dispVo.atrbId)-2)}EndDt" type="html" />
						<u:out var="strtDt" value="${strtValue }" type="date"/><u:out var="endDt" value="${endValue }" type="date"/>
						<c:set var="value" value="${strtDt }~${endDt }"/>
					</c:when>
					<c:otherwise><u:convertMap var="mapValue" srcId="map" attId="${dispVo.atrbId}" type="html" /><c:set var="value" value="${mapValue }"/></c:otherwise>
				</c:choose>
				<td class="body_lt" align="${dispVo.alnVa}" ${viewFunc }>
					<%-- <c:if test="${dispVo.atrbId eq 'subj' && map.keepDdlnYn == 'Y'}"><span>[<u:msg titleId="dm.option.keepDdln" alt="보존연한도래" />]</span></c:if> --%>
					<div class="ellipsis" title="${value }">
					<c:choose>
						<c:when test="${dispVo.colmVo.itemTyp eq 'TEXTAREA' }">
							<c:if test="${dispVo.atrbId eq 'errCont' }"><a href="javascript:;" onclick="viewTransferDtlPop('${map.tranId}','${map.docGrpId }');">${value }</a></c:if>
							<c:if test="${dispVo.atrbId eq 'rjtOpin' }"><a href="javascript:;" onclick="viewOpenReqPop('${map.docGrpId}','${map.tgtId }');">${value }</a></c:if>
							<c:if test="${dispVo.atrbId ne 'errCont' && dispVo.atrbId ne 'rjtOpin'}">${value }</c:if>
						</c:when>
						<c:when test="${fn:contains(uidAtrbs,fn:substring(dispVo.atrbId,0,fn:length(dispVo.atrbId)-2)) && (dispVo.colmVo.itemTyp eq 'UID' || dispVo.colmVo.itemTyp eq 'CID')}">
							<c:if test="${dispVo.colmVo.itemTyp eq 'UID' }"><u:convertMap var="valueUid" srcId="map" attId="${fn:substring(dispVo.atrbId,0,fn:length(dispVo.atrbId)-2)}Uid" type="html" /><a href="javascript:viewUserPop('${valueUid}');">${value }</a></c:if>							
							<c:if test="${dispVo.colmVo.itemTyp eq 'CID' }">
								<u:convertMap var="valueTypCd" srcId="map" attId="${fn:substring(dispVo.atrbId,0,fn:length(dispVo.atrbId)-2)}TypCd" type="html" />
								<c:if test="${valueTypCd eq 'U' }"><u:convertMap var="valueUid" srcId="map" attId="${fn:substring(dispVo.atrbId,0,fn:length(dispVo.atrbId)-2)}Id" type="html" /><a href="javascript:viewUserPop('${valueUid}');">${value }</a></c:if>
								<c:if test="${valueTypCd ne 'U' }">${value }</c:if>
							</c:if>
						</c:when>
						<c:when test="${dispVo.colmVo.itemTyp eq 'ID' && dispVo.colmVo.itemTypVa eq 'ORG_ID'}">${value }</c:when>
						<c:when test="${fn:endsWith(dispVo.atrbId,'Dt') || dispVo.colmVo.dataTyp eq 'DATETIME' || dispVo.colmVo.itemTyp eq 'CALENDAR' }"><u:out value="${value }" type="longdate"/></c:when>
						<c:when test="${dispVo.colmVo.itemTyp eq 'FILE' }"><c:if test="${value > 0}"><a href="javascript:viewFileListPop('${map.docId}','${map.docGrpId }','${map.tgtId }');"><u:icon type="att" /></a></c:if></c:when>
						<c:otherwise><c:if test="${dispVo.colmVo.itemTyp == 'CODE'}">
							<c:forEach items="${dispVo.colmVo.cdList}" var="cd" varStatus="status">
								<c:if test="${cd.cdId == value}">${cd.rescNm }</c:if>
							</c:forEach>
						</c:if><c:if test="${map.statCd eq 'T' || map.statCd eq 'M' }"><c:set var="setFunc" value="setDoc('${map.docId}');"/></c:if>
						<c:if test="${map.statCd ne 'T' && map.statCd ne 'M'}"><c:if test="${fn:contains(viewAtrbs,dispVo.atrbId) }"></c:if><c:set var="setFunc" value="viewDoc('${map.docId}','${map.docGrpId }','${map.tgtId }');"/>
							<c:if test="${dispVo.atrbId eq 'readCnt' }"><c:set var="setFunc" value="readHst('${map.docId }');"/></c:if>
						</c:if>
						<c:if test="${dispVo.colmVo.itemTyp != 'CODE'}"><u:set var="linkStyle" test="${fn:contains(viewAtrbs,dispVo.atrbId) || dispVo.atrbId eq 'readCnt'}" value="onclick=\"${setFunc }\"" elseValue=""/>
						<c:if test="${dispVo.atrbId eq 'subj'}">
							<u:set test="${isAdmin == false && isReadChk == true && map.readYn ne 'Y'}" var="style" value="style=\"font-weight: bold;\"" elseValue=""/>
							<c:if test="${map.subYn eq 'Y' }">
								<c:if test="${empty orderSubYn && (empty param.orderSubYn || param.orderSubYn eq 'Y')}"><u:icon type="indent" display="${map.sortDpth > 0}" repeat="${map.sortDpth - 1}" /><u:icon type="reply" display="${map.sortDpth > 0}" /></c:if>
								<c:if test="${(!empty param.orderSubYn && param.orderSubYn eq 'N') || (!empty orderSubYn && orderSubYn eq 'N')}"><u:icoCurr display="true" /></c:if>
							</c:if>
							<c:if test="${map.statCd == 'W' }"><u:icon type="lock" /></c:if>
							<c:if test="${map.newYn == 'Y' }"><u:icon type="new" /></c:if>
							<%-- <c:if test="${map.keepDdlnYn == 'Y' }"><u:icon type="notc" /></c:if> --%>
							<c:if test="${!empty map.refTyp && map.refTyp eq 'ap'}"><u:msg var="icoTitle" titleId="dm.cols.send.${map.refTyp }"/><img src="${_cxPth}/images/${_skin}/ico_document.png" title="${icoTitle }"/></c:if>
						</c:if>
						<c:if test="${!empty linkStyle }">
						<a href="javascript:;" title="${value}" ${linkStyle } ${dispVo.atrbId eq 'subj' ? style : '' }>${value }</a></c:if><c:if test="${empty linkStyle }">
						<c:if test="${dispVo.atrbId eq 'fldNm' && empty value }"><u:msg titleId="dm.msg.not.save.emptyCls" alt="미분류"/></c:if>${value }</c:if></c:if>
						<c:if test="${dispVo.atrbId eq 'subj' && map.statCd eq 'C' && !empty map.subDocCnt}"><span style="font-size: 11px;">(<u:out value="${map.subDocCnt}" type="number" />)</span></c:if>
						</c:otherwise>
					</c:choose>
					</div>
				</td>
			</c:forEach>
			<c:if test="${fn:startsWith(path,'listTmp')}"><td class="body_ct" ><u:buttonS titleId="cm.btn.mod" href="javascript:setDoc('${map.docId}');" alt="수정" auth="W" /></td></c:if>
		</tr>
		<%-- <c:if test="${!empty map.subDocMapList }">
			<c:forEach var="subMap" items="${map.subDocMapList}" varStatus="status">
				<c:set var="subMap" value="${subMap }" scope="request"/>
					<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"' >
					<c:forEach var="dispVo" items="${itemDispList }" varStatus="status">
						<u:convertMap var="value" srcId="subMap" attId="${dispVo.atrbId}" type="html" />
						<td class="body_lt" align="${dispVo.alnVa}" ${viewFunc }>
							<c:if test="${dispVo.atrbId eq 'subj' }">
								<c:if test="${subMap.statCd == 'W' }"><u:icon type="lock" /></c:if>
								<c:if test="${subMap.newYn == 'Y' }"><u:icon type="new" /></c:if>
								<c:if test="${subMap.keepDdlnYn == 'Y' }"><u:icon type="notc" /></c:if>
							</c:if>
							<c:if test="${dispVo.atrbId eq 'subj' && subMap.keepDdlnYn == 'Y'}"><span>[<u:msg titleId="dm.option.keepDdln" alt="보존연한도래" />]</span></c:if>
							<c:choose>
								<c:when test="${dispVo.colmVo.itemTyp eq 'TEXTAREA' }"><div class="ellipsis" title="${value }">${value }</div></c:when>
								<c:when test="${fn:contains(uidAtrbs,fn:substring(dispVo.atrbId,0,4)) }">
									<u:convertMap var="valueUid" srcId="subMap" attId="${fn:substring(dispVo.atrbId,0,4)}Uid" type="html" />
									<a href="javascript:viewUserPop('${valueUid}');">${value }</a>
								</c:when>
								<c:when test="${fn:endsWith(dispVo.atrbId,'Dt') || dispVo.colmVo.dataTyp eq 'DATETIME' || dispVo.colmVo.itemTyp eq 'CALENDAR' }"><u:out value="${value }" type="longdate"/></c:when>
								<c:when test="${dispVo.colmVo.itemTyp eq 'FILE' }"><c:if test="${value > 0}"><u:icon type="att" /></c:if></c:when>
								<c:otherwise><c:if test="${dispVo.colmVo.itemTyp == 'CODE'}">
									<c:forEach items="${dispVo.colmVo.cdList}" var="cd" varStatus="status">
										<c:if test="${cd.cdId == value}">${cd.rescNm }</c:if>
									</c:forEach>
								</c:if><u:set var="setFunc" test="${subMap.statCd eq 'T' }" value="setDoc('${subMap.docId}');" elseValue="viewDoc('${subMap.docId}');"/>
								<c:if test="${dispVo.colmVo.itemTyp != 'CODE'}"><u:set var="linkStyle" test="${fn:contains(viewAtrbs,dispVo.atrbId) }" value="onclick=\"${setFunc }\" style='cursor:pointer;'" elseValue=""/>
									<c:if test="${dispVo.atrbId eq 'subj' }"><u:icon type="indent" display="${subMap.sortDpth > 0}" repeat="${subMap.sortDpth - 1}" />
										<u:icon type="reply" display="${subMap.sortDpth > 0}" /></c:if>
								<c:if test="${!empty linkStyle }">
								<a href="javascript:;" title="${value}" ${linkStyle }>${value }</a></c:if><c:if test="${empty linkStyle }">
								${value }</c:if></c:if></c:otherwise>
							</c:choose>
						</td>
					</c:forEach>
					<c:if test="${fn:startsWith(path,'listTmp')}"><td class="body_ct" ><u:buttonS titleId="cm.btn.mod" href="javascript:setDoc('${subMap.docId}');" alt="수정" auth="W" /></td></c:if>
					<c:if test="${fn:startsWith(path,'listSubm') || fn:startsWith(path,'listDisc')}"><td class="body_ct" ><u:msg titleId="dm.cols.docStat${subMap.statCd }" alt="대기/반려/승인" /></td></c:if>
				</tr>
			</c:forEach>
		</c:if> --%>
	</c:forEach>
	<c:if test="${empty mapList }">
		<tr>
		<td class="nodata" colspan="${colSpan }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	
</table>
</div>
</form>
<u:blank />
<c:choose>
<c:when test="${frmYn eq 'Y' }"><div style="padding:10px;"><u:pagination /></div></c:when>
<c:otherwise><u:pagination /></c:otherwise>
</c:choose>
<u:blank />
<u:set var="rightBtnDisplay" test="${frmYn eq 'Y' }" value="display:none;" elseValue=""/>
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea" style="${rightBtnDisplay }">
	<c:if test="${!empty authMap.send }"><u:button titleId="dm.cols.auth.send" href="javascript:send();" alt="보내기" auth="W"/></c:if>
	<c:if test="${!empty authMap.move }"><u:button titleId="dm.cols.auth.move" href="javascript:move();" alt="이동" auth="W"/></c:if>
	<c:if test="${!empty authMap.recovery }"><u:button titleId="dm.cols.auth.recovery" href="javascript:delDocList('C');" alt="복원" auth="W"/></c:if>
	<c:if test="${!empty authMap.delete }"><u:button titleId="dm.cols.auth.recycle" href="javascript:delDocList('D');" alt="휴지통" auth="W"/></c:if>
	<c:if test="${!empty authMap.disuse }"><u:button titleId="dm.cols.auth.disuse" href="javascript:delDocList('F');" alt="완전삭제" auth="W"/></c:if>
	<c:if test="${!empty authMap.transferDel }"><u:button titleId="dm.btn.delete.all" alt="전체삭제" onclick="transferDel('delAll');" auth="A"/><u:button titleId="cm.btn.selDel" alt="선택삭제" onclick="transferDel('del');" auth="A"/></c:if>
	<c:if test="${!empty authMap.setDoc }"><u:button titleId="cm.btn.reg" alt="등록" href="javascript:setDoc();" auth="W" /></c:if>
	<c:if test="${!empty authMap.tranHst }"><u:button titleId="dm.btn.tranHst" alt="이관이력" href="javascript:transferHst();" auth="A" /></c:if>
	<c:if test="${!empty authMap.transferAll }"><u:button titleId="dm.btn.tranAll" alt="전체이관" href="javascript:transferDoc('A');" auth="A" /></c:if>
	<c:if test="${!empty authMap.transferSelect }"><u:button titleId="dm.btn.tranSelect" alt="선택이관" href="javascript:transferDoc('S');" auth="A" /></c:if>
	<c:if test="${!empty authMap.transTgtSave || !empty authMap.transTgtUpdate}">
		<u:set var="tgtSaveTitleCd" test="${!empty authMap.transTgtUpdate }" value="dm.btn.trans.tgtUpdate" elseValue="dm.btn.trans.tgtSave"/>
		<u:button titleId="${tgtSaveTitleCd }" alt="선택인계" href="javascript:transTgtSave();" auth="A" />
	</c:if>
	<c:if test="${!empty authMap.transReSave }"><u:button titleId="dm.btn.trans.tgtReSave" alt="재인계" href="javascript:transReSave();" auth="A" /></c:if>
	<c:if test="${!empty authMap.transTgtCancel }"><u:button titleId="dm.btn.trans.tgtCancel" alt="인계취소" href="javascript:transTgtCancel();" auth="A" /></c:if>
	<c:if test="${!empty authMap.transTakovrApvd }"><u:button titleId="dm.btn.trans.takovrApvd" alt="승인" href="javascript:transTakovrApvd();" auth="W" /></c:if>
	<c:if test="${!empty authMap.transTakovrRjt }"><u:button titleId="dm.btn.trans.takovrRjt" alt="거부" href="javascript:transTakovrRjt();" auth="W" /></c:if>
	<c:if test="${!empty authMap.openApv }">
		<c:if test="${empty param.viewReqStatCd || param.viewReqStatCd eq 'S'}"><u:button titleId="dm.cols.auth.openApv" alt="열람요청승인" href="javascript:openApv();" auth="A" /></c:if>
		<c:if test="${!empty param.viewReqStatCd && param.viewReqStatCd eq 'A'}"><u:button titleId="dm.btn.dtlView.cancel" alt="열람취소" href="javascript:openCancel();" auth="A" /></c:if>
		<c:if test="${!empty param.viewReqStatCd && param.viewReqStatCd eq 'R'}"><u:button titleId="cm.btn.del" alt="삭제" href="javascript:openCancel();" auth="A" /></c:if>
	</c:if>
	
</u:buttonArea>
<form id="takovrForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="tabId" value="doc"/>
<input type="hidden" name="docTyp" value="doc" />
<input type="hidden" name="afterActKey" value="transCmplSave" /><!-- 후처리 -->
<input type="hidden" name="mode" value="move"/><!-- 복사,이동 -->
<input type="hidden" name="dialog" value="setSendOptDialog"/><!-- 팝업명 -->
<input type="hidden" name="docGrpId" />
<u:input type="hidden" id="viewPage" value="./${viewPage }.do?${params}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${paramsForList}" />
<c:if test="${!empty paramCompId}"><u:input type="hidden" id="paramCompId" value="${paramCompId }" /></c:if>
</form>
<c:if test="${!empty paramTransferEntryList}">
<form id="setTransferHstForm" name="setTransferHstForm" >
	<c:forEach items="${paramTransferEntryList}" var="entry" varStatus="status">
		<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
</form>
</c:if>