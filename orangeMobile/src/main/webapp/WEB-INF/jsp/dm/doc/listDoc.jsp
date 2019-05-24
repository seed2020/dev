<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/><!-- 저장소ID -->
<u:set var="paramCompIdQueryString" test="${!empty paramCompId }" value="&paramCompId=${paramCompId }" elseValue=""/><!--회사ID -->
<u:set var="paramFldGrpId" test="${empty paramCompId }" value="&fldGrpId=DEPT" elseValue=""/>
<script type="text/javascript">
//<![CDATA[<% // [버튼] 열람요청상세 %>
function viewOpenReqPop(docGrpId,tgtId){
	$m.dialog.open({
		id:'viewOpenReqDialog',
		title:'<u:msg titleId="dm.btn.dtlView.request" alt="열람요청" />',
		url:'/dm/doc/setOpenApvPop.do?menuId=${menuId}&tgtId='+tgtId+'&docGrpId='+docGrpId,
	});
}<%// [버튼] 열람취소 - 삭제 %>
function openCancel(){
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	var arrs2 = selDocIds('tgtId');
	if(arrs2 == null) return;
	$m.ajax('./transOpenCancelAjx.do?menuId=${menuId}', {docGrpId:arrs.join(','),tgtId:arrs2.join(',')}, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
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
	$m.ajax('./transOpenApvAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
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
}<%// 문서열람요청 %>
function saveRequest(param){
	$m.ajax('/dm/doc/transViewDocReqAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
		}
		if (data.result == 'ok') {
			$m.dialog.close('viewDocReqCfrmDialog'); 
		}
	});
}<%// [버튼] 이관취소 %>
function transferDel(actKey){
	var param = new ParamMap().getData("setTransferHstForm");
	if(actKey == 'del'){
		var selIds = selDocIds('docGrpId');
		if(selIds == null) return;
		param.put('docGrpId',selIds.join(','))
	}
	param.put('mode','del');
	$m.ajax('./transferDelAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace(location.href);
		}
	});
}<% // [버튼] 문서이관상세(오류) %>
function viewTransferDtlPop(tgtStorId, docGrpId){
	parent.dialog.open('viewTransferDtlDialog','<u:msg titleId="cm.msg.error" alt="오류" />','./viewTransferDtlPop.do?menuId=${menuId}&tgtStorId='+tgtStorId+'&docGrpId='+docGrpId);
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
	transTakovrSave('transReSave','dm.cfrm.transSave',{docGrpId:arrs.join(','),mode:'transReSave',statCd:'O',msgCode:'dm.msg.save.trans'});
}<%// 회사별 인계 %>
function transTgtCompSave(comp){
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	transTakovrSave('transSave','dm.cfrm.transSave',{docGrpId:arrs.join(','),mode:'transSave',tgtOrgId:comp[0].compId,statCd:'O',msgCode:'dm.msg.save.trans',compYn:'Y'});
}<%// [버튼] 선택인계 %>
function transTgtSave(){
	var arrs = selDocIds('docGrpId');
	if(arrs == null) return;
	searchOrgPop(null, function(orgVo){
		if(orgVo!=null) {
			transTakovrSave('transSave','dm.cfrm.transSave',{docGrpId:arrs.join(','),mode:'transSave',tgtOrgId:orgVo.orgId,statCd:'O',msgCode:'dm.msg.save.trans'});
		}
	});
}
<%// [버튼] 선택인계 %>
function transTakovrSave(actKey, msgCd, param){
	if (confirmMsg(msgCd)) {	<% // dm.cfrm.transSave=해당문서를 인계하시겠습니까? %>
		<% // dm.msg.save.trans=문서를 인계하였습니다.%>				
		$m.ajax('./transTakovrAjx.do?menuId=${menuId}', param, function(data) {
			if (data.message != null) {
				$m.dialog.alert(data.message);
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
	dialog.open('setSendOptDialog', '<u:msg titleId="dm.btn.trans.takovrApvd" alt="인수승인" />', '/cm/doc/setSendOptPop.do?${paramsForList}&tabId=doc&callback=transTakovr${paramFldGrpId}&mode=takovr');
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
	var url = '/dm/doc/${setPage}.do?${paramsForList }${paramStorIdQueryString}';
	if(seq != undefined) url+= '&docId='+seq;
	$m.nav.next(null, url);
}<% // [목록:제목] 상세 조회 %>
function viewDoc(id,docGrpId,tgtId) {
	$m.ajax('/dm/doc/getViewOptAjx.do?menuId=${menuId}${paramStorIdQueryString}', {docGrpId:docGrpId,viewPage:'${viewPage}'}, function(data) {
		if(data.message != null){
			$m.dialog.alert(data.message);
		}
		if ((data.popYn == null || data.popYn == 'N') && data.messageCd != null) {
			$m.msg.alertMsg(data.messageCd);
		}
		if (data.popYn != null && data.popYn == 'Y' && data.messageCd != null) {
			$m.msg.confirmMsg(data.messageCd, null, function(result){
				if(result){
					$m.dialog.open({
						id:'viewDocReqCfrmDialog',
						title:'<u:msg titleId="dm.jsp.dtlView.request.title" alt="문서열람요청" />',
						url:"/dm/doc/viewDocReqCfrmPop.do?menuId=${menuId}&docGrpId="+docGrpId,
					});
				}
			});
		}
		if (data.result == 'ok') {
			var url = '/dm/doc/${viewPage}.do?${paramsForList }${paramStorIdQueryString}${paramCompIdQueryString}&docId=' + id;
			if(tgtId != undefined) url+= '&tgtId='+tgtId;
			<c:if test="${!empty bumkId}">url+= '&bumkId=${bumkId}';</c:if>			
			$m.nav.next(null, url);
			//location.href = url;
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
		if(noSelectMsg!=null) $m.msg.alertMsg(noSelectMsg);
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
		$m.ajax('./transDocDelListAjx.do?menuId=${menuId}${paramStorIdQueryString}', {docGrpId:arrs.join(','),statCd:statCd,msgCode:msgCd[1]}, function(data) {
			if (data.message != null) {
				$m.dialog.alert(data.message);
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
var holdHide = false;
$(document).ready(function() {
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $("#listsearch #searchCat").hide();
	});
	
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});

//]]>
</script>
<c:if test="${!empty authMap.setDoc }">
<u:secu auth="W">
<div class="btnarea">
    <div class="size">
        <dl>
           	 <dd class="btn" onclick="setDoc();"><u:msg titleId="cm.btn.write" alt="등록" /></dd>
     </dl>
    </div>
</div>
</u:secu>
</c:if>
<!-- 검색조건 페이지 -->
<jsp:include page="/WEB-INF/jsp/dm/doc/listDocSrch.jsp" />

<section>
<div class="listarea">
<article><c:forEach
	items="${mapList}" var="map" varStatus="status">
	<u:set var="docLink" test="${map.statCd eq 'T' || map.statCd eq 'M' }" value="setDoc('${map.docId}');" elseValue="viewDoc('${map.docId}','${map.docGrpId }','${map.tgtId }');"/>
	<u:set var="docLink" test="${!empty map.viewReqStatCd && map.viewReqStatCd eq 'R'}" value="viewOpenReqPop('${map.docGrpId }','${map.tgtId }');" elseValue="${docLink }"/>
	<div class="listdiv" onclick="${docLink}">
		<c:if test="${map.subYn eq 'Y' }">
		<div class="listcheck_comment"><dl><dd class="comment"></dd></dl></div>
        </c:if>
		<div class="list${map.subYn eq 'Y' ? '_comment' : '' }">
		<dl>
			<dd class="tit"><c:set var="subjIco" value="" /><c:if test="${map.newYn == 'Y'}"><c:set var="subjIco" value="${subjIco }new"/></c:if
			><c:if test="${!empty subjIco}"><div class="new"></div></c:if>	<c:if test="${!empty map.refTyp && (map.refTyp eq 'ap' || map.refTyp eq 'bb')}"><span class="ctxt1">[<u:msg titleId="dm.cols.send.${map.refTyp }" alt="게시판/결재" />]</span></c:if
			><u:set test="${isAdmin == false && isReadChk == true && map.readYn ne 'Y'}" var="style" value="style=\"font-weight: bold;\"" elseValue=""
			/><span ${style }>${map.subj }<c:if test="${map.statCd eq 'C' && !empty map.subDocCnt}"
			>(<u:out value="${map.subDocCnt}" type="number" />)</c:if></span>
			</dd><dd class="name"><u:out value="${map.fldNm }"/>ㅣ <u:out value="${map.regrNm}"/> ㅣ <u:out value="${map.regDt}" type="date" 
			/>	ㅣ <u:out value="${map.readCnt}" type="number" /></dd>
		</dl>
		</div>
	</div></c:forEach><c:if
	
		test="${recodeCount == 0}">
	<div class="listdiv_nodata" >
		<dl>
		<dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
		</dl>
	</div></c:if>
</article>
</div>

<m:pagination />

<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
