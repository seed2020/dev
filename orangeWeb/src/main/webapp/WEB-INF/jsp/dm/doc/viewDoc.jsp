<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="frmYn" test="${!empty pageSuffix && pageSuffix == 'Frm' }" value="Y" elseValue="N"/>
<u:set var="saveDisc" test="${!empty param.pltYn && param.pltYn eq 'Y' }" value="saveDiscAjx" elseValue="saveDisc"/>
<u:params var="nonPageParams" excludes="docId,pageNo,docGrpId,tgtId"/>
<u:params var="viewPageParams" excludes="docId,docPid,docGrpId"/>
<u:set var="includeParams" test="${!empty docPid}" value="&docId=${docPid }" elseValue="&docId=${dmDocLVoMap.docId }"/>
<c:set var="exKeys" value="view,update,owner,disuse,saveDisc,keepDdln,seculCd,recycle,fld,cls,docNoMod,setSubDoc,bumk,download,print,email,openApv"/><!-- 하단 버튼 제외 key -->
<u:set var="adminYn" test="${!empty isAdmin && isAdmin == true }" value="Y" elseValue="N"/>
<u:set var="paramStorIdQueryString" test="${!empty paramStorId }" value="&paramStorId=${paramStorId }" elseValue=""/>
<script type="text/javascript">
<!--<%// [버튼] 열람취소 - 삭제 %>
function openCancel(){
	var arrs = '${dmDocLVoMap.docGrpId}';
	if(arrs == '') return;
	var arrs2 = '${dmDocLVoMap.tgtId}';
	if(arrs2 == '') return;
	callAjax('./transOpenCancelAjx.do?menuId=${menuId}', {docGrpId:arrs,tgtId:arrs2}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace($('#listPage').val());
		}
	});
}<%// [저장] 열람요청승인 - 개별 %>
function saveOpenApv(param){
	var arrs = '${dmDocLVoMap.docGrpId}';
	if(arrs == '') return;
	var arrs2 = '${param.tgtId}';
	if(arrs2 == '') return;
	param.put('docGrpId',arrs);
	param.put('tgtId',arrs2);
	callAjax('./transOpenApvAjx.do?menuId=${menuId}${paramStorIdQueryString}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace($('#listPage').val());
		}
	});
}<%// [버튼] 열람승인 %>
function openApv(){
	parent.dialog.open('openApvDialog','<u:msg titleId="dm.jsp.dtlView.approved.title" alt="열람요청승인" />','./setOpenApvPop.do?menuId=${menuId}');
}<%// [팝업] 문서이력 %>
function docHst(srchCd){
	var url = './listDocTaskPop.do?menuId=${menuId}&docId=${dmDocLVoMap.docId}&dialog=docHstPop${paramStorIdQueryString}';
	if(srchCd != undefined) url+="&single=Y&srchCd=view";
	parent.dialog.open('docHstPop', '<u:msg titleId="dm.cols.auth.docHst" alt="문서이력" />', url);
};<%// [팝업] 보내기 %>
function send(){
	var url = './sendPop.do?${params}&docTyp=doc&listPage=${listPage}&refTyp=${dmDocLVoMap.refTyp}&dialog=sendDialog';
	parent.dialog.open('sendDialog', '<u:msg titleId="cm.btn.send" alt="보내기" />', url);
};<%// [팝업] 이메일 %>
function email(){
	<c:if test="${!empty paramStorId }">emailSendPop({docId:'${dmDocLVoMap.docId}',docTyp:'${param.docTyp }',paramStorId:'${paramStorId}'},'${menuId }');</c:if>
	<c:if test="${empty paramStorId }">emailSendPop({docId:'${dmDocLVoMap.docId}',docTyp:'${param.docTyp }'},'${menuId }');</c:if>
};<%// [팝업] 이동 %>
function move(){
	var url = './sendPop.do?${params}&mode=move&listPage=${listPage}';
	parent.dialog.open('sendPop', '<u:msg titleId="dm.jsp.send.move.title" alt="문서이동" />', url);
};<% // [즐겨찾기 저장 | 삭제] %>
function saveBumk(bumkId, mode){
	if(mode == undefined || mode == null) mode = 'insert';
	callAjax('./transBumkDocAjx.do?menuId=${menuId}', {bumkId:bumkId,mode:mode,regCat:'D',catVa:'${dmDocLVoMap.docGrpId}'}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			if(mode == 'del') location.replace($('#listPage').val());
			else dialog.close('selectBumkPop');
		}
	});
};<% // [하단버튼:즐겨찾기] %>
function bumkDoc(){
	parent.dialog.open('selectBumkPop', '<u:msg titleId="dm.cols.bumk" alt="즐겨찾기" />', './selectBumkPop.do?menuId=${menuId}');
}<% // [하단버튼:뒤로|취소] %>
function cancelDoc(){
	history.go(-1);
}<% // [하단버튼:제외키] %>
function getExKeys(){
	var exKeys = '${exKeys}'.split(',');
	var returnAttrs = [];
	for(var i=0;i<exKeys.length;i++){
		returnAttrs.push(exKeys[i]);
	}
	return returnAttrs;
}<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}<% // [하단버튼:복원] %>
function recovery() {
	if (confirmMsg("dm.cfrm.recovery")) <% // dm.cfrm.recovery=복원하시겠습니까? %>
		delDocTrans({docGrpId:$('#docGrpId').val(),statCd:'C',msgCode:'dm.msg.recovery.success'},false);	
}<% // [하단버튼:승인,반려] %>
function saveDiscAjx(discStatCd) {
	if(discStatCd == null) return;
	var $form = $("#setForm");
	$form.find("#statCd").remove();
	$form.appendHidden({name:'statCd',value:discStatCd});
	if(discStatCd == 'R'){
		if($('#rjtOpin').val() == ''){
			alertMsg('cm.input.check.mandatory',['<u:msg titleId="cols.rjtOpin" alt="반려의견" />']);
			return;
		}
		$form.find("#rjtOpin").remove();
		$form.appendHidden({name:'rjtOpin',value:$('#rjtOpin').val()});
	}
	var param = $('#setForm').serialize();
	if(confirmMsg("cm.cfrm.save")) {<%//cm.cfrm.save=저장 하시겠습니까? %>
		$.ajax({
	        url: './transDiscDoc${pageSuffix}Ajx.do?menuId=${menuId}',
	        type: 'POST',
	        data:param,
	        dataType:'json',
	        success: function(data){
	        	if(data.model.message!=null){
					alert(data.model.message);
				}
	        	if(data.model.result=='ok'){
	        		getIframeContent('PLT_${param.pltId}').getIframeContent('tabArea').reload();
	        	}
	        	dialog.close("viewDocPop");
	        }
		});
	}
};<% // [하단버튼:승인,반려] %>
function saveDisc(discStatCd) {
	if(discStatCd == null) return;
	var $form = $("#setForm");
	if(discStatCd == 'R'){
		if($('#rjtOpin').val() == ''){
			alertMsg('cm.input.check.mandatory',['<u:msg titleId="cols.rjtOpin" alt="반려의견" />']);
			return;
		}
		$form.find("#rjtOpin").remove();
		$form.appendHidden({name:'rjtOpin',value:$('#rjtOpin').val()});
	}
	if (confirmMsg("cm.cfrm.save")) {
		$form.find("#statCd").remove();
		$form.appendHidden({name:'statCd',value:discStatCd});
		$form.attr('method','post');
		$form.attr('action','./transDiscDoc${pageSuffix}.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};<% // [하단버튼:목록] %>
function listDoc(){
	location.replace($('#listPage').val());
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
			var url = './${viewPage}.do?${paramsForList }&docId=' + id;
			if(tgtId != undefined && tgtId != '') url+= '&tgtId='+tgtId;
			location.href = url;
		}
	});
}<% // 기본정보 수정 %>
function transferUpdate(arrs){
	var json = JSON.parse(arrs);
	var $form = $("#setForm");
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
	$form.appendHidden({name:'grpId',value:'${dmDocLVoMap.docGrpId }'});
	$form.attr('method','post');
	$form.attr('action','./transTransferDoc.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form.submit();
}<% // [하단버튼:수정] %>
function setUpdate(){
	parent.dialog.open('setSendOptDialog', '<u:msg titleId="dm.jsp.setDoc.mod.title" alt="문서수정" />', './setSendOptPop.do?${paramsForList}&callback=transferUpdate&mode=update');
}<% // [하단버튼:수정] %>
function setDoc(){
	location.href = './${setPage}.do?${paramsForList }&docId=${dmDocLVoMap.docId }';
}<% // [하단버튼:하위문서등록] %>
function setSubDoc(){
	location.href = './${setPage}.do?${paramsForList }&docPid=${dmDocLVoMap.docGrpId }';
}<%// 상태코드별 메세지[복원,폐기,삭제] %>
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
function delDocTrans(param, valid) {
	if('${adminYn}' == 'Y' && param.statCd == 'F' && param.docGrpId != undefined){
		delDocList(param.docGrpId,param.statCd);
	}else{
		if (!valid || confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
			callAjax('./transDocDelAjx.do?menuId=${menuId}${paramStorIdQueryString}', param, function(data) {
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.href = $('#listPage').val();
					/* <c:if test="${empty refDocId && empty docPid}">location.href = $('#listPage').val();</c:if>
					<c:if test="${empty refDocId && !empty docPid}">location.href = $('#viewPage').val();</c:if>
					<c:if test="${!empty refDocId}">location.href = $('#viewRefPage').val();</c:if> */
				}
			});
		}
	}
	
}<% // [하단버튼:삭제] %>
function delDocList(arrs, statCd) {
	if(arrs == null) return;
	var msgCd = getMsgCd(statCd);
	callAjax('./transDocDelListAjx.do?menuId=${menuId}${paramStorIdQueryString}', {docGrpId:arrs,statCd:statCd,msgCode:msgCd[1]}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.href = $('#listPage').val();
			/* <c:if test="${empty refDocId && empty docPid}">location.href = $('#listPage').val();</c:if>
			<c:if test="${empty refDocId && !empty docPid}">location.href = $('#viewPage').val();</c:if>
			<c:if test="${!empty refDocId}">location.href = $('#viewRefPage').val();</c:if> */
		}
	});
}<% // [하단버튼:삭제] 문서%>
function delDoc(statCd) {
	delDocTrans({docId:$('#docId').val(),statCd:statCd},true);
}<% // [하단버튼:삭제] 문서그룹전체%>
function delDocGrp(statCd) {
	if (confirmMsg("dm.cfrm.del.with")) <% // dm.cfrm.del.with=하위 버전이 있을 경우 함께 삭제됩니다\n그래도 하시겠습니까? %>
		delDocTrans({docGrpId:$('#docGrpId').val(),statCd:statCd},false);	
}<%// 탭 클릭 - 문서버전 %>
function toggleTabBtn(seq){
	location.href = "./${viewPage}.do?${paramsForList }&docId="+seq;
}<%// 팝업 - url형 문서보기 %>
function refUrlPop(url, title){
	// dm.msg.viewDoc.popTitle=[{0}] 정보
	parent.dialog.open('viewDocDialog', title, url);
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
$(document).ready(function() {
	/* <c:if test="${pageSuffix eq 'Frm' && dmDocLVoMap.dftYn ne 'Y'}">parent.applyDocBtn('viewHst');</c:if>
	<c:if test="${pageSuffix eq 'Frm' && dmDocLVoMap.dftYn eq 'Y'}">parent.applyDocBtn('view');</c:if> */
	setUniformCSS();
	// 버튼 활성화
	<c:if test="${pageSuffix eq 'Frm'}">parent.applyDocBtn();</c:if>
	// 결재정보 팝업
	<c:if test="${dmDocLVoMap.statCd eq 'C' && dmDocLVoMap.refTyp eq 'ap' && !empty dmDocLVoMap.refUrl && empty param.cancel}">refUrlPop('${dmDocLVoMap.refUrl}&menuId=${menuId}','<u:msg titleId="ap.tab.apvInof" alt="결재 정보" />');</c:if>
});
//-->
</script>
<c:if test="${dmDocLVoMap.statCd eq 'C' && !empty dmDocLVoMap.refTyp }">
<div class="color_txt">
	<u:msg titleId="dm.msg.viewDoc.send" arguments="#dm.cols.send.${dmDocLVoMap.refTyp },#dm.cols.orgnYn.${!empty dmDocLVoMap.orgnYn && dmDocLVoMap.orgnYn eq 'Y' ? 'orgn' : 'copy' }" alt="* 결재에서 보낸 문서입니다."/>
	<c:if test="${dmDocLVoMap.refTyp eq 'ap' && !empty dmDocLVoMap.refUrl}">
		<u:msg var="refPopTitle" titleId="dm.msg.viewDoc.popTitle" alt="[0] 정보" arguments="#dm.cols.send.${dmDocLVoMap.refTyp }"/>
		<u:buttonS title="${refPopTitle }" onclick="refUrlPop('${dmDocLVoMap.refUrl}&menuId=${menuId}','${refPopTitle }');" alt="결재정보보기" />
	</c:if>
</div>
</c:if>
<c:if test="${frmYn == 'Y' }">
<c:set var="frmStyle" value="style='padding:10px;'"/>
</c:if>
<c:if test="${frmYn == 'N' }">
<u:title titleId="dm.jsp.search.doc.title" alt="문서조회" menuNameFirst="true" />
</c:if>
<div ${frmStyle }>
<c:if test="${fn:length(dmDocVerLVoList)>0 }">
<u:tabGroup noBottomBlank="${true}">
	<c:forEach var="verVo" items="${dmDocVerLVoList }" varStatus="status">
		<u:tab alt="목록전체" title="${verVo.verVa }"
			on="${verVo.verVa == dmDocLVoMap.verVa}"
			onclick="toggleTabBtn('${verVo.docId }');" />
	</c:forEach>
</u:tabGroup>
<u:blank />
</c:if>
<c:set var="voMap" value="${dmDocLVoMap }" scope="request"/>

<input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams}" />
<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${viewPageParams}${includeParams }" />
<u:input type="hidden" id="viewRefPage" value="./${viewPage}.do?${paramsForList}&docId=${refDocId }" />
<u:input type="hidden" id="docId" value="${dmDocLVoMap.docId }" />
<u:input type="hidden" id="docGrpId" value="${dmDocLVoMap.docGrpId }" />

<c:if test="${dmDocLVoMap.statCd eq 'C' }">
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="15%"/><col width="35%"/><col width="15%"/><col width="35%"/></colgroup>
<c:if test="${itemDispMap.docNo.readDispYn eq 'Y' || itemDispMap.verVa.readDispYn eq 'Y'}">
<u:set var="itemColspan" test="${itemDispMap.docNo.readDispYn eq 'N' || itemDispMap.verVa.readDispYn eq 'N' }" value="colspan='3'" elseValue=""/>
<tr><c:if test="${itemDispMap.docNo.readDispYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="dm.cols.docNo" alt="문서번호" /></td>
	<td class="body_lt" ${itemColspan }>${dmDocLVoMap.docNo}</td></c:if>
	<c:if test="${itemDispMap.verVa.readDispYn eq 'Y' }">
	<td class="head_lt" ><u:msg titleId="dm.cols.verNo" alt="버전" /></td>
	<td class="body_lt" ${itemColspan }>${dmDocLVoMap.verVa}</td></c:if>
</tr>
</c:if>
<c:if test="${itemDispMap.regrNm.readDispYn eq 'Y' || itemDispMap.regDt.readDispYn eq 'Y'}">
<u:set var="itemColspan" test="${itemDispMap.regrNm.readDispYn eq 'N' || itemDispMap.regDt.readDispYn eq 'N' }" value="colspan='3'" elseValue=""/>
<tr><c:if test="${itemDispMap.regrNm.readDispYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="cols.regr" alt="작성자" /></td>
	<td class="body_lt" ${itemColspan }><a href="javascript:viewUserPop('${dmDocLVoMap.regrUid}');"><u:out value="${dmDocLVoMap.regrNm}" /></a></td></c:if>
	<c:if test="${itemDispMap.regDt.readDispYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="cols.regDt" alt="작성일시" /></td>
	<td class="body_lt" ${itemColspan }><u:out value="${dmDocLVoMap.regDt }" type="longdate" /></td></c:if>
</tr>
</c:if>
<c:if test="${itemDispMap.modrNm.readDispYn eq 'Y' || itemDispMap.modDt.readDispYn eq 'Y'}">
<u:set var="itemColspan" test="${itemDispMap.modrNm.readDispYn eq 'N' || itemDispMap.modDt.readDispYn eq 'N' }" value="colspan='3'" elseValue=""/>
<tr><c:if test="${itemDispMap.modrNm.readDispYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="cols.modr" alt="수정자" /></td>
	<td class="body_lt" ${itemColspan }><a href="javascript:viewUserPop('${dmDocLVoMap.modrUid}');"><u:out value="${dmDocLVoMap.modrNm}" /></a></td></c:if>
	<c:if test="${itemDispMap.modDt.readDispYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="cols.modDt" alt="수정일시" /></td>
	<td class="body_lt" ${itemColspan }><u:out value="${dmDocLVoMap.modDt }" type="longdate" /></td></c:if>
</tr>
</c:if>
<c:if test="${itemDispMap.cmplDt.readDispYn eq 'Y' || itemDispMap.readCnt.readDispYn eq 'Y'}">
<u:set var="itemColspan" test="${empty itemDispMap.cmplDt.readDispYn || itemDispMap.cmplDt.readDispYn eq 'N' || empty itemDispMap.readCnt.readDispYn || itemDispMap.readCnt.readDispYn eq 'N' }" value="colspan='3'" elseValue=""/>
<tr><c:if test="${itemDispMap.cmplDt.readDispYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="dm.cols.cmplDt" alt="완료일시" /></td>
	<td class="body_lt" ${itemColspan }><u:out value="${dmDocLVoMap.cmplDt }" type="longdate" /></td></c:if>
	<c:if test="${itemDispMap.readCnt.readDispYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="cols.readCnt" alt="조회수" /></td>
	<td class="body_lt" ${itemColspan }><c:if test="${!empty authMap.docHst }"><a href="javascript:docHst('view');"><u:out value="${dmDocLVoMap.readCnt }" type="html" /></a></c:if><c:if test="${empty authMap.docHst }"><u:out value="${dmDocLVoMap.readCnt }" type="html" /></c:if></td></c:if>
</tr>
</c:if>
</table>
</div>
<u:blank />
</c:if>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="15%"/><col width="35%"/><col width="15%"/><col width="35%"/></colgroup>
<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt" colspan="3">${dmDocLVoMap.subj}</td>
</tr>
<c:if test="${dmDocLVoMap.dftYn eq 'Y'}">
<c:if test="${itemDispMap.fldNm.readDispYn eq 'Y' || itemDispMap.clsNm.readDispYn eq 'Y'}">
<u:set var="itemColspan" test="${itemDispMap.fldNm.readDispYn eq 'N' || itemDispMap.clsNm.readDispYn eq 'N' }" value="colspan='3'" elseValue=""/>
<tr><c:if test="${itemDispMap.fldNm.readDispYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="cols.fld" alt="폴더" /></td>
	<td class="body_lt" ${itemColspan }><c:if test="${empty dmDocLVoMap.fldNm }"><u:msg titleId="dm.msg.not.save.emptyCls" alt="미분류"/></c:if>${dmDocLVoMap.fldNm}</td></c:if>
	<c:if test="${itemDispMap.clsNm.readDispYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="cols.cls" alt="분류" /></td>
	<td class="body_lt" ${itemColspan }>
		<c:set var="clsNmTmp" />
		<c:forEach var="clsVo" items="${dmClsBVoList }" varStatus="status">
			<u:input type="hidden" id="clsId" value="${clsVo.clsId }"/>
			<c:set var="clsNmTmp" value="${clsNmTmp}${status.count > 1 ? ',' : '' }${clsVo.clsNm }"/>
		</c:forEach>
		${clsNmTmp }
	</td></c:if>
</tr>
</c:if>

<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.kwd" alt="키워드" /></td>
	<td class="body_lt" colspan="3">
		<c:set var="kwnNms" />
		<c:forEach var="kwdVo" items="${dmKwdLVoList }" varStatus="status"><c:set var="kwnNms" value="${kwnNms }${status.count > 1 ? ',' : ''}${kwdVo.kwdNm }"/></c:forEach>
		${kwnNms}
	</td>
</tr>

<c:if test="${itemDispMap.docKeepPrdNm.readDispYn eq 'Y' || itemDispMap.seculNm.readDispYn eq 'Y'}">
<u:set var="itemColspan" test="${itemDispMap.docKeepPrdNm.readDispYn eq 'N' || itemDispMap.seculNm.readDispYn eq 'N' }" value="colspan='3'" elseValue=""/>
<tr><c:if test="${itemDispMap.docKeepPrdNm.readDispYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="dm.cols.keepPrd" alt="보존연한" /></td>
	<td class="body_lt" ${itemColspan }>${dmDocLVoMap.docKeepPrdNm }<c:if test="${!empty dmDocLVoMap.keepDdlnDt }"> (<u:out value="${dmDocLVoMap.keepDdlnDt }" type="date"/>)</c:if></td></c:if>
	<c:if test="${itemDispMap.seculNm.readDispYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="dm.cols.secul" alt="보안등급" /></td>
	<td class="body_lt" ${itemColspan }>${dmDocLVoMap.seculNm }</td></c:if>
</tr>
</c:if>

<c:if test="${itemDispMap.ownrNm.readDispYn eq 'Y'}">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.ownr" alt="소유자" /></td>
	<td class="body_lt" colspan="3"><a href="javascript:viewUserPop('${dmDocLVoMap.ownrUid}');"><u:out value="${dmDocLVoMap.ownrNm}" /></a></td>
</tr>
</c:if>
</c:if>
<c:if test="${dmDocLVoMap.dftYn eq 'Y' && empty dmDocLVoMap.docId}">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.bumk" alt="즐겨찾기" /></td>
	<td class="body_lt" >${dmDocLVoMap.bumkNm }</td>
	<td class="head_lt"><u:msg titleId="dm.cols.auth.psn" alt="개인폴더" /></td>
	<td class="body_lt" >${dmDocLVoMap.psnFldNm }</td>
</tr>
</c:if>
<c:if test="${!empty authMap.saveDisc}">
<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.rjtOpin" alt="반려의견" /></td>
	<td colspan="3" class="body_lt"><u:textarea id="rjtOpin" value="" titleId="cols.rjtOpin" maxByte="1000" style="width:98%" rows="3" /></td>
	</tr>
</c:if>
<c:if test="${dmSubmLVo.statCd eq 'R' && !empty dmSubmLVo}">
<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.rjtOpin" alt="반려의견" /></td>
	<td colspan="3" class="body_lt"><u:textarea id="rjtOpin" value="${dmSubmLVo.rjtOpin }" titleId="cols.rjtOpin" maxByte="1000" style="width:98%" rows="3" readonly="Y"/></td>
	</tr>
</c:if>
<c:if test="${!empty lobHandler }">
<tr>
	<td colspan="4" ><div style="overflow:auto;min-height:100px;" class="editor"><u:clob lobHandler="${lobHandler }"/></div></td>
</tr>
</c:if>
<c:if test="${itemDispMap.fileCnt.readDispYn eq 'Y' && !empty authMap.download }">
<tr>
	<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
	<td colspan="3">
		<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="dm" mode="view" urlParam="docGrpId=${dmDocLVoMap.docGrpId }${paramStorIdQueryString}" /></c:if>
	</td>
</tr>
</c:if>
</table>
</div>

<u:blank />
<c:if test="${!empty itemDispList }">
<c:set var="maxCnt" value="2"/>
<c:set var="dispCnt" value="1"/>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="15%"/><col width="35%"/><col width="15%"/><col width="35%"/></colgroup>
<tr>
<c:forEach var="dispVo" items="${itemDispList }" varStatus="status">
<c:set var="colmVo" value="${dispVo.colmVo}" />
<c:set var="itemTyp" value="${colmVo.itemTyp}" />
<c:set var="itemNm" value="${dispVo.atrbId}" />
<u:convertMap var="docVal" srcId="voMap" attId="${itemNm }" />
	<u:set var="colspan" test="${(dispCnt == 1 || dispCnt%maxCnt == 1) && ( itemTyp eq 'TEXTAREA' || fn:length(itemDispList) == status.count )}" value="colspan='${(maxCnt*2)-1 }'" elseValue=""/>
	<c:if test="${dispCnt > 1 && dispCnt%maxCnt == 1}"></tr><tr></c:if>
	<td class="head_lt">${colmVo.itemDispNm }</td>
	<td class="body_lt" ${colspan }>
		<c:if test="${itemTyp != 'CODE'}">${docVal }</c:if>
		<c:if test="${itemTyp == 'CODE'}">
			<c:forEach items="${colmVo.cdList}" var="cd" varStatus="status">
				<c:if test="${cd.cdId == docVal}">${cd.rescNm}</c:if>
			</c:forEach>
		</c:if>
	</td>
	<c:if test="${!empty colspan && status.count < fn:length(itemDispList) }"></tr><tr><c:set var="dispCnt" value="0"/></c:if>
	<c:set var="dispCnt" value="${dispCnt+1 }"/>
</c:forEach>
</tr>
</table>
</div>
<u:blank />
</c:if>

<c:if test="${dmDocLVoMap.dftYn eq 'Y' && subDocListYn == true}">
<% // 하위문서 %>
<div class="titlearea">
	<div class="tit_left">
	<dl><dd class="txt">※ <span class="red_txt">${fn:length(subDocList)-1}</span> <u:msg titleId="dm.jsp.viewDoc.tx01" alt="개의 관련문서가 있습니다." /></dd></dl>
	</div>
</div>
<% // 하위문서 목록 %>
<u:listArea id="subDocList" colgroup="15%,,10%,15%">
	<tr>
	<td class="head_ct"><u:msg titleId="dm.cols.docNo" alt="문서번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>

<c:if test="${empty subDocList || fn:length(subDocList) <= 1}">
	<tr>
	<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${!empty subDocList && fn:length(subDocList) > 1}">
	<c:forEach items="${subDocList}" var="subDocVo" varStatus="status">
	<u:set var="currYn" test="${dmDocLVoMap.docId == subDocVo.docId}" value="Y" elseValue="N"/>
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="listicon_lt"><u:icoCurr display="${currYn eq 'Y'}" /> ${subDocVo.docNo }</td>
	<td class="body_lt">
		<u:icon type="indent" display="${subDocVo.sortDpth > 0}" repeat="${subDocVo.sortDpth - 1}" />
		<u:icon type="reply" display="${subDocVo.sortDpth > 0}" />
		<u:icon type="new" display="${subDocVo.newYn == 'Y'}" />
		<c:if test="${currYn eq 'Y'}"><u:out value="${subDocVo.subj}" maxLength="80" /></c:if>
		<c:if test="${currYn eq 'N'}"><a href="javascript:viewDoc('${subDocVo.docId}','${subDocVo.docGrpId }','${subDocVo.tgtId }');" title="${subDocVo.subj}"><u:out value="${subDocVo.subj}" maxLength="80" /></a></c:if>
		</td>
	<td class="body_ct"><a href="javascript:viewUserPop('${subDocVo.regrUid}');">${subDocVo.regrNm}</a></td>
	<td class="body_ct"><u:out value="${subDocVo.regDt}" type="longdate" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>
</c:if>
<c:if test="${dmDocLVoMap.dftYn eq 'Y' && naviYn == true}">
<% // 이전글 다음글 %>
<u:listArea colgroup="6%,,10%,15%">
<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
<td class="listicon_ct"><u:icon type="prev" /></td>
<c:choose>
	<c:when test="${!empty prevVo }"><td class="body_lt"><a href="javascript:viewDoc('${prevVo.docId}','${prevVo.docGrpId}','${prevVo.tgtId}');" title="${prevVo.subj}"><u:out value="${prevVo.subj}" maxLength="80" /></a></td>
<td class="body_ct"><a href="javascript:viewUserPop('${prevVo.regrUid}');"><u:out value="${prevVo.regrNm}" /></a></td>
<td class="body_ct"><u:out value="${prevVo.regDt }" type="longdate" /></td></c:when>
	<c:otherwise><td class="body_lt"><u:msg titleId="dm.jsp.viewDoc.prevNotExists" alt="이전 문서가 없습니다." /></td>
<td class="body_ct">&nbsp;</td>
<td class="body_ct">&nbsp;</td></c:otherwise>
</c:choose>
</tr>

<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
<td class="listicon_ct"><u:icon type="next" /></td>
<c:choose>
	<c:when test="${!empty nextVo }"><td class="body_lt"><a href="javascript:viewDoc('${nextVo.docId}','${nextVo.docGrpId}','${nextVo.tgtId}');" title="${nextVo.subj}"><u:out value="${nextVo.subj}" maxLength="80" /></a></td>
<td class="body_ct"><a href="javascript:viewUserPop('${nextVo.regrUid}');"><u:out value="${nextVo.regrNm}" /></a></td>
<td class="body_ct"><u:out value="${nextVo.regDt }" type="longdate" /></td></c:when>
	<c:otherwise><td class="body_lt"><u:msg titleId="dm.jsp.viewDoc.nextNotExists" alt="다음 문서가 없습니다." /></td>
<td class="body_ct">&nbsp;</td>
<td class="body_ct">&nbsp;</td></c:otherwise>
</c:choose>
</tr>
</u:listArea>
</c:if>
<u:set var="rightBtnDisplay" test="${frmYn eq 'Y' }" value="display:none;" elseValue=""/>

<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea" style="${rightBtnDisplay }">
	<c:if test="${dmDocLVoMap.dftYn eq 'Y' }">
		<c:if test="${!empty authMap.print}"><u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" /></c:if>
		<c:forEach items="${authMap}" var="cdVo" varStatus="status">
			<c:if test="${!fn:contains(exKeys,cdVo.key) }">
				<u:msg var="btnTitle" titleId="dm.cols.auth.${cdVo.value }"/>
				<u:button title="${btnTitle }" href="javascript:${cdVo.key }();" alt="${btnTitle }" auth="W"/>
			</c:if>
		</c:forEach>
		<c:if test="${!empty authMap.email && dmDocLVoMap.refTyp ne 'ap'}">
			<u:internalIp><u:button titleId="dm.cols.auth.email" href="javascript:email();" alt="이메일작성" /></u:internalIp>
		</c:if>
		<c:if test="${!empty authMap.bumk}">
			<u:button titleId="dm.cols.bumk" href="javascript:bumkDoc();" alt="즐겨찾기" />
		</c:if>
		<c:if test="${!empty param.bumkId && isBumkSave == true}">
			<u:button titleId="dm.btn.delete.bumk" href="javascript:saveBumk('${param.bumkId }','del');" alt="즐겨찾기삭제" />
		</c:if>
		<c:if test="${!empty authMap.saveDisc}">
			<u:button titleId="cm.btn.apvd" href="javascript:;" onclick="${saveDisc }('A');" alt="승인" auth="W" />
			<u:button titleId="cm.btn.rjt" href="javascript:;" onclick="${saveDisc }('R');" alt="반려" auth="W" />
		</c:if>
		<c:if test="${!empty authMap.recycle}">
			<c:if test="${fn:length(dmDocVerLVoList) > 1 }"><u:button titleId="dm.cols.auth.verDel" href="javascript:delDoc('F');" alt="버전삭제" auth="W"/></c:if>
			<c:if test="${dmDocLVoMap.statCd eq 'C' && dmDocLVoMap.dftYn eq 'Y'}"><u:button titleId="dm.cols.auth.recycle" href="javascript:delDocGrp('D');" alt="휴지통" auth="W"/></c:if>
		</c:if>
		<c:if test="${!empty authMap.disuse}">
			<u:button titleId="dm.cols.auth.disuse" href="javascript:delDocGrp('F');" alt="완전삭제" auth="W"/>
		</c:if>
		<c:if test="${!empty authMap.update}">
			<u:button titleId="dm.cols.setSubDoc" href="javascript:setSubDoc();" alt="하위문서작성" auth="W"/>
			<u:button titleId="cm.btn.mod" href="javascript:setDoc();" alt="수정" auth="W"/>
		</c:if>
		<c:if test="${!empty authMap.openApv }">
			<c:if test="${empty dmDocLVoMap.viewReqStatCd || dmDocLVoMap.viewReqStatCd eq 'S'}"><u:button titleId="dm.cols.auth.openApv" alt="열람요청승인" href="javascript:openApv();" auth="A" /></c:if>
			<c:if test="${!empty dmDocLVoMap.viewReqStatCd && dmDocLVoMap.viewReqStatCd eq 'A'}"><u:button titleId="dm.btn.dtlView.cancel" alt="열람취소" href="javascript:openCancel();" auth="A" /></c:if>
			<c:if test="${!empty dmDocLVoMap.viewReqStatCd && dmDocLVoMap.viewReqStatCd eq 'R'}"><u:button titleId="cm.btn.del" alt="삭제" href="javascript:openCancel();" auth="A" /></c:if>
		</c:if>
		<c:if test="${dmDocLVoMap.dftYn eq 'Y' && dmDocLVoMap.subYn eq 'Y'}">
			<u:button titleId="cm.btn.back" href="javascript:cancelDoc();" alt="뒤로" />			
		</c:if>
		<c:if test="${dmDocLVoMap.dftYn eq 'Y' && dmDocLVoMap.subYn ne 'Y'}">
			<u:button titleId="cm.btn.list" href="javascript:listDoc();" alt="목록" />
		</c:if>
	</c:if>
	<c:if test="${dmDocLVoMap.dftYn ne 'Y' }">
		<c:if test="${!empty authMap.recycle && fn:length(dmDocVerLVoList) > 1}">
			<u:button titleId="dm.cols.auth.verDel" href="javascript:delDoc('F');" alt="버전삭제" auth="W"/>
		</c:if>
		<u:button titleId="cm.btn.back" href="javascript:cancelDoc();" alt="뒤로" />
	</c:if>
	
</u:buttonArea>
</div>
<form id="setForm" name="setForm">
<u:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams}" />
<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${params}" />
<u:input type="hidden" id="viewRefPage" value="./${viewPage}.do?${paramsForList}&docId=${refDocId }" />
<u:input type="hidden" id="docId" value="${dmDocLVoMap.docId }" />
<u:input type="hidden" id="docGrpId" value="${dmDocLVoMap.docGrpId }" />
<!-- 저장소ID -->
<c:if test="${!empty paramStorId }">
<u:input type="hidden" id="paramStorId" value="${paramStorId}" />
</c:if>
</form>
