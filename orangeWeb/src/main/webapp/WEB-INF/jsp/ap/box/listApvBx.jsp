<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var gOptConfig = ${optConfigJson};<%// 결재 옵션 - json %><%
// 양식선택 %>
function selectForm(){
	dialog.open('selectFormDialog', '<u:msg alt="양식선택" titleId="ap.btn.selectForm" />', './selectFormPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}');
}<%
// 새창열기용 - 새로운 열린 윈도우 객체 - 포커스 전환이 안되어서 기존 창을 닫고 새창을 염 %>
var gNewTabWin = null;<%
// 문서 작성 - 양식 선택으로 새문서 작성 %>
function makeDoc(formId, refDocApvNo, newTab){
	var $form = initForm();
	var $bx = $form.find("#bxId");
	$('<input>', {'name':'formId','value':formId,'type':'hidden'}).insertBefore($bx);
	if(newTab==true){
		if(gNewTabWin != null) gNewTabWin.close();
		gNewTabWin = window.open("./setDoc.do?"+$form.serialize(), '_newTab');
		gNewTabWin.focus();
	} else {
		$form.attr("action", "./setDoc.do").attr("method", "GET").submit();
	}
}<%
// 문서 작성 - (임시저장,반려문서)의 재작성 %>
function remakeDoc(apvNo){
	var $form = $("#toDocForm");
	$form.find("[name='apvNo'], [name='formId'], [name='apvLnPno']").remove();
	var $bx = $form.find("#bxId");
	$('<input>', {'name':'apvNo','value':apvNo,'type':'hidden'}).insertBefore($bx);
	$form.attr("action", "./setDoc.do").attr("method", "GET").submit();
}<%
// 문서 조회 %>
function openDoc(apvNo, apvLnPno, apvLnNo, sendSeq, pwYn, popYn){
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
	
	$form.attr("method", "GET");
	
	if(pwYn=='Y'){
		if(popYn=='Y'){
			$form.attr("action", "./viewDocWinPop.do");
		}
		dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo);
	} else {
		if(popYn=='Y'){
			var url = "./viewDocWinPop.do?"+new ParamMap().getData($form).toQueryString();
			window.open(url,'','scrollbars=yes,resizable=yes, top=40, left=120, width=900,height=730');
		} else {
			$form.attr("action", "./setDoc.do");
			$form.attr("target", "");
			$form.submit();
		}
	}
}<%
// 문서 조회 - 팝업 %>
function popDoc(apvNo, apvLnPno, apvLnNo, sendSeq, pwYn){
	openDoc(apvNo, apvLnPno, apvLnNo, sendSeq, pwYn, 'Y');
}<%
// 비밀번호 세팅된 문서 열기 - setDocPwPop.jsp 에서 호출 %>
function openSecuDoc(secuId){
	var $form = initForm(false);
	if($form.attr("action")!=null && $form.attr("action").indexOf("viewDocWinPop")>0){
		$form.appendHidden({'name':'secuId','value':secuId});
		var url = "./viewDocWinPop.do?"+new ParamMap().getData($form).toQueryString();
		window.open(url,'','scrollbars=yes,resizable=yes, top=40, left=120, width=900,height=730');
	} else {
		$form.appendHidden({'name':'secuId','value':secuId}).attr("action", "./setDoc.do").submit();
	}
}<%
// 문서조회 Form 리턴 %>
function initForm(init){
	var $form = $("#toDocForm");
	if(init!=false){
		$form.find("[name='apvNo'], [name='formId'], [name='apvLnPno'], [name='secuId'], [name='sendSeq']").remove();
	}
	return $form;
}<%
// 첨부 조회%>
function viewAttach(apvNo, pwYn){
	if(pwYn == 'Y'){
		secuAttachApvNo = apvNo;
		${not empty isFrame ? 'parent.' : ''}dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&callback=viewSecuAttach&apvNo="+apvNo);
	} else {
		${not empty isFrame ? 'parent.' : ''}dialog.open("setDocAttchDialog", '<u:msg titleId="ap.btn.att" alt="첨부" />', "./viewDocAttchPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo);
	}
}<%
// 보안 첨부 조회%>
var secuAttachApvNo = null;
function viewSecuAttach(secuId){
	if(secuAttachApvNo != null){
		${not empty isFrame ? 'parent.' : ''}dialog.open("setDocAttchDialog", '<u:msg titleId="ap.btn.att" alt="첨부" />', "./viewDocAttchPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&secuId="+secuId+"&apvNo="+secuAttachApvNo);
		secuAttachApvNo = null;
	}
	dialog.close('setDocPwDialog');
}<%
// 선택열기 %>
function openSelected(){
	var $inputs = getSelected(false);
	if($inputs==null) return;
	if($inputs.length==1){
		${not empty isFrame ? 'parent.' : ''}openDoc($inputs.val(), $inputs.attr('data-apvLnPno'), $inputs.attr('data-apvLnNo'), $inputs.attr('data-sendSeq'), $inputs.attr('data-docPw'));
	} else {
		var first=null, selectedDocs=[], data, $check;
		$inputs.each(function(){
			$check = $(this);
			data = [$check.val(), $check.attr('data-apvLnPno'), $check.attr('data-apvLnNo'), $check.attr('data-sendSeq'), $check.attr('data-docPw')];
			if(first==null) first = data;
			selectedDocs.push(data.join(','));
		});
		${not empty isFrame ? 'parent.' : ''}openSelectedDocs(first, selectedDocs);
	}
}<%
// 상세정보 %>
function openDetl(onTab){
	var $inputs = getSelected(true);
	if($inputs!=null){
		${not empty isFrame ? 'parent.' : ''}dialog.open("setDocDetlDialog", '<u:msg titleId="ap.btn.docDetl" alt="상세정보" />', "./viewDocDetlPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+$inputs.val()+(onTab==null ? '':'&onTab='+onTab)+"&fromList=Y");
	}
}<%
// 접수확인 %>
function openCfrmRecv(onTab){
	var $inputs = getSelected(true);
	if($inputs!=null){
		if($inputs.attr('data-enfcStatCd')!='sent'){
			alertMsg("ap.msg.noSendStat");<%-- //ap.msg.noSendStat=발송되지 않은 문서 입니다. --%>
		} else {
			${not empty isFrame ? 'parent.' : ''}dialog.open('setCfrmRecvDialog', '<u:msg titleId="ap.btn.cfrmRecv" alt="접수확인" />', './setCfrmRecvPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo='+$inputs.val());
		}
	}
}<%
// 선택된 목록 리턴 %>
function getSelected(needOne){
	var $inputs = $("#listApvForm input[type='checkbox'][id!='checkHeader']:checked:visible");
	if($inputs.length==0){<%
		// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다. - ap.jsp.doc=문서 %>
		alertMsg("cm.msg.noSelectedItem", ["#ap.jsp.doc"]);
	} else if(needOne == true && $inputs.length > 1){<%
		// cm.msg.selectOneItem=하나의 "{0}"(을)를 선택해 주십시요. - ap.jsp.doc=문서 %>
		alertMsg("cm.msg.selectOneItem", ["#ap.jsp.doc"]);
	} else {
		return $inputs;
	}
	return null;
}<%
// 프레임 높이 조절 - 프레임 안에 삽입 되었을때 부모창의 프레임 높이를 목록 갯수 만큼 %>
function setFrameInfo(){
	var height = $(document.body)[0].scrollHeight;
	height = Math.max(490, height);
	parent.setFrameHeight(height);
	parent.setPageInfo('${param.pageNo}','${param.pageRowCnt}');
}<%
// 일괄접수 - 분류정보 사용 할 때 %>
function openRecvPop(){
	if(getSelected(false)==null) return;
	if(gOptConfig.catEnab=='Y'){<%
		// 분류정보 + 보안등급 설정 팝업 %>
		var seculPapram = (gOptConfig.recvRecLstSecuLvl=='Y') ? '&withSecul=Y&seculCd=${apvData.seculCd}' : '';
		dialog.open('setClsInfoDialog','<u:msg titleId="ap.btn.bulkRecv" alt="일괄접수" />','./treeOrgClsPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&callback=processBulkRecv'+seculPapram);
	} else if(gOptConfig.recvRecLstSecuLvl=='Y'){<%
		// 보안등급 설정 팝업 %>
		var seculPapram = '&withSecul=Y&seculCd=${apvData.seculCd}';
		dialog.open('setClsInfoDialog','<u:msg titleId="ap.btn.bulkRecv" alt="일괄접수" />','./treeOrgClsPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&callback=processBulkRecv&noCls=Y'+seculPapram);
	} else {<%
		// 접수 진행 %>
		processBulkRecv();
	}
}<%
// 일괄접수 - 분류정보 사용 안할 때  or 분류정보 팝업에서 호출 %>
function processBulkRecv(clsInfoId, seculCd){
	var $checks = getSelected(false);
	if($checks==null) return;<%
	// ap.cfrm.recv=접수 하시겠습니까 ? %>
	if(!confirmMsg('ap.cfrm.recv')) return;<%
	// 결재번호,발송일련번호 데이터 모으기 %>
	var recvDocs =[];
	$checks.each(function(){
		recvDocs.push({apvNo:$(this).val(),sendSeq:$(this).attr("data-sendSeq")});
	});
	var data = {process:"processBulkRecv", recvDocList:recvDocs};
	if(clsInfoId!=null) data['clsInfoId'] = clsInfoId;
	if(seculCd!=null) data['seculCd'] = seculCd;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", data, function(data){
		if(data.message != null) alert(data.message);
		if(data.url != null){<%
			// 접수대장 조회로 전환 %>
			removeApCachedCountMap();
			location.replace(data.url);
		} else if(data.result == 'ok'){<%
			// 접수대장에 권한이 없는 경우 - 목록 보기로 전환 %>
			removeApCachedCountMap();
			location.replace(location.href);
		}
	});
}<%
// 일괄배부 %>
function openDocDist(){
	if(getSelected(false)==null) return;
	dialog.open('setDocDistDialog', '<u:msg titleId="ap.btn.bulkDist" alt="일괄배부" /> - <u:msg titleId="ap.doc.recvDeptNm" alt="수신처" />', './setDocDistPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&sendSeq=${param.sendSeq}&callback=processBulkDist');
}<%
// 일괄배부 버튼 %>
function processBulkDist(){
	var $checks = getSelected(false);
	if($checks==null) return;
	<%
	// 접수처 데이터 모으기 %>
	var arr = collectDistDept(), result=false;
	if(arr.length==0){<%
		// ap.msg.noDistDept=배부처가 지정되지 않았습니다. %>
		alertMsg('ap.msg.noDistDept');
	} else {<%
		// ap.cfrm.dist=배부 하시겠습니까 ? %>
		if(!confirmMsg('ap.cfrm.dist')) return;<%
		// 결재번호,발송일련번호 데이터 모으기 %>
		var distDocs =[];
		$checks.each(function(){
			distDocs.push({apvNo:$(this).val(),sendSeq:$(this).attr("data-sendSeq")});
		});
		var data = {process:"processBulkDist", distDocList:distDocs, distList:arr};
		
		callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", data, function(data){
			if(data.message != null) alert(data.message);
			result = data.result == 'ok';
			if(data.url != null){<%
				// 접수대장 조회로 전환 %>
				removeApCachedCountMap();
				location.replace(data.url);
			} else if(data.result == 'ok'){<%
				// 접수대장에 권한이 없는 경우 - 목록 보기로 전환 %>
				removeApCachedCountMap();
				location.replace(location.href);
			}
		});
	}
}<%
// 일괄결재용 변수 %>
var gBulkApvData = null;<%
// 일괄결재 %>
function openBulkApv(){<%
	// 선택된 문서 가져오기 %>
	var $checks = getSelected(false);
	if($checks==null) return;<%
	// 일괄결재 가능한 갯수 파악 %>
	var roleCd, checks=[], roleMsgArr=[], hasSecuDoc=false;
	var roleMsgMap = {mak:'<u:term termId="ap.term.mak" />',makAgr:'<u:term termId="ap.term.makAgr" />',makVw:'<u:term termId="ap.term.makVw" />'};
	$checks.each(function(){
		roleCd = $(this).attr("data-apvrRoleCd");
		if(roleCd!='mak' && roleCd!='makAgr' && roleCd!='makVw'){
			if($(this).attr("data-docPw")=='Y'){
				hasSecuDoc = true;
			} else {
				checks.push(this);
			}
		} else {
			if(!roleMsgArr.contains(roleMsgMap[roleCd])) roleMsgArr.push(roleMsgMap[roleCd]);
		}
	});<%
	// checks.length:일괄결재 가능한 결재 수 %>
	if(checks.length==0){<%
		// ap.msg.noBulkApvDoc=일괄결재 가능한 문서가 없습니다.%>
		var msg = callMsg('ap.msg.noBulkApvDoc');
		var ruleOutMsg = getBuldApvRuleOutMsg(hasSecuDoc, roleMsgArr);<%// 일관결재 - 제외대상 메세지 %>
		alert(msg+(ruleOutMsg==null ? '' : '\n('+ruleOutMsg+')'));
		return;
	}<%
	// 도장 또는 서명이 설정 되었는지 체크 %>
	var valid = true;
	callAjax("./getStampInfoAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", null, function(data){
		if(data.message != null){
			alert(data.message);
			valid = false;
		}
	});
	if(!valid) return;<%
	// 일괄결재용 변수 세팅 %>
	gBulkApvData = {checks:checks, hasSecuDoc:hasSecuDoc, roleMsgArr:roleMsgArr};
	<%
	
	// 결재 비밀번호 사용 유무 체크해서 - openBulkApvPop() 함수 호출
	%>
	if('${optConfigMap.notAlwApvPw}' != 'Y'){<%// 결재 비밀번호 사용함 - 비밀번호 팝업 %>
		dialog.open("setApvPwDialog", '<u:msg titleId="ap.btn.bulkApv" alt="일괄결재" /> - <u:msg titleId="ap.trans.apvPw" alt="결재비밀번호" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&callback=openBulkApvPop");
	} else {
		openBulkApvPop();
	}
}<%
// 일괄결재 작업창 열기 %>
function openBulkApvPop(secuId){
	gBulkApvData['secuId'] = secuId;<%
	
	// 일괄결재 confirm 메세지 %>
	var ruleOutMsg = getBuldApvRuleOutMsg(gBulkApvData.hasSecuDoc, gBulkApvData.roleMsgArr);<%
	// ap.cfrm.bulkApv={0}건의 문서를 일괄결재 하시겠습니까 ? %>
	var msg = callMsg("ap.cfrm.bulkApv",[gBulkApvData.checks.length+'']);
	if(confirm((ruleOutMsg==null ? '' : ruleOutMsg+'\n') + msg)){
		dialog.open('listBulkApvDialog', '<u:msg titleId="ap.btn.bulkApv" alt="일괄결재" />', './listBulkApvPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}');
	}
	dialog.close('setApvPwDialog');
}<%
// 일괄결재 - 체크 메세지 리턴 %>
function getBuldApvRuleOutMsg(hasSecuDoc, roleMsgArr){
	if(hasSecuDoc || roleMsgArr.length>0){<%
		// ap.msg.noBulkApvDocWithSecuAndRoles="보안문서"와 결재자 역할이 {0}인 문서는 일괄결재에서 제외 됩니다.
		// ap.msg.noBulkApvDocWithSecu="보안문서"는 일괄결재에서 제외 됩니다.
		// ap.msg.noBulkApvDocWithRoles=결재자 역할이 {0}인 문서는 일괄결재에서 제외 됩니다. %>
		var msgId = (hasSecuDoc && roleMsgArr.lenghth>0) ? 'ap.msg.noBulkApvDocWithSecuAndRoles' : hasSecuDoc ? 'ap.msg.noBulkApvDocWithSecu' : 'ap.msg.noBulkApvDocWithRoles';
		return callMsg(msgId, ['\"'+roleMsgArr.join('\", \"')+'\"']);
	}
	return null;
}<%
// 삭제 %>
function deleteDoc(){<%
	// 선택된 문서 가져오기 %>
	var $checks = getSelected(false);
	if($checks==null) return;
	var bxId = '${param.bxId}';<%
	// cm.cfrm.del=삭제하시겠습니까 ? %>
	if(!confirmMsg('cm.cfrm.del')) return;
	var apvNos = [];
	$checks.each(function(){
		apvNos.push($(this).val());
	});
	
	var result = false;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"delDocs", apvNos:apvNos.join(',')}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result){
		removeApCachedCountMap();
		location.replace(location.href);
	}
}<%
// 저장소 변경 %>
function chngStorage(obj){
	var storId = $(obj).val();
	location.href = '${_uri}?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}' + (storId=='' ? '' : '&storId='+storId);
}<%
// 경로조회(결재라인 목록형 에서 호출:viewApvLnInc.jsp) - apvLnPno 가 없으면 "전체경로", 있으면 해당 서브 경로
// 주로 viewDocDetlPop.jsp 에서 사용되며, 본문에 목록형 결재라인이 추가되어 이곳으로 스크립트 옮겨옴 %>
function viewApvLnPop(apvNo, apvLnPno, deptNm){
	var popTitle = apvLnPno==null ? '<u:msg alt="전체경로" titleId="ap.btn.allApvLn" />' : '<u:msg alt="결재 경로" titleId="ap.jsp.apvLn" />' + (deptNm==null ? '' : ' - '+deptNm);
	var popId = apvLnPno==null ? 'viewApvLnDialog' : 'viewSubApvLnDialog';
	dialog.open(popId, popTitle, "./viewApvLnPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}${strMnuParam}&apvNo="+apvNo+(apvLnPno==null ? '' : '&apvLnPno='+apvLnPno));
}<%
// 읽지않음 표시 %>
function setUnread(proc){
	if(proc==null) proc = "unread";<%
	// 선택된 문서 가져오기 %>
	var $checks = getSelected(false);
	if($checks==null) return;<%
	// ap.cfrm.unreadMark={0}건의 문서를 읽지않음 표시 하시겠습니까 ?
	// ap.cfrm.ReadMark={0}건의 문서를 읽음 표시 하시겠습니까 ? %>
	if(!confirmMsg('ap.cfrm.'+proc+'Mark', [''+$checks.length])) return;<%
	// 결재번호,발송일련번호 데이터 모으기 %>
	var arr =[];
	$checks.each(function(){
		arr.push({apvNo:$(this).val(), apvLnPno:$(this).attr("data-apvLnPno"), apvLnNo:$(this).attr("data-apvLnNo")});
	});
	
	var result = false;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:proc, list:arr}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result){
		reload();
	}
}<%
// 읽음 표시 %>
function setRead(){
	setUnread('read');
}<%
// onload %>
$(document).ready(function() {
	setUniformCSS();
	${not empty isFrame ? 'setFrameInfo();' : ''}
	${adjustCachedBxCount}
	window.setTimeout('alertInitMsg()', 300);
	loadBodyPreview();
});<%
// onload 메세지 %>
function alertInitMsg(){
	var message = '<u:out value="${message}" type="script" />';
	if(message!='') alert(message);
}<%
// [이하] 내용 미리보기 용 %>
var previewBodyMap = null, previewIndex=3000;
function loadBodyPreview(){
	if('${empty isFrame and param.bxId eq "waitBx" ? optConfigMap.erpBodyPreview : ""}'!='Y') return;
	var refApvNos = [], intgNo;
	$("#listApvForm input[type='checkbox']").each(function(){
		intgNo = $(this).attr('data-intgNo');
		if(intgNo!=null && intgNo!=''){
			refApvNos.push($(this).val());
		}
	});
	if(refApvNos.length>0){
		previewBodyMap = {};
		callAjax("./getRefBodyAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {refApvNos:refApvNos.join(",")}, function(data){
			if(data.message != null) alert(data.message);
			refApvNos.each(function(index, refNo){
				refBodyMap = data[refNo];
				if(refBodyMap!=null){
					previewBodyMap[refNo] = refBodyMap.get('bodyHtml');
				}
			});
		});
	}
}
var gPreviewObj = null;
var gDelayCloseTimeout = null;
function openPreview(trObj){
	var apvNo = $(trObj).find('input[type=checkbox]:first').val();
	var previewHtml = previewBodyMap==null ? null : previewBodyMap[apvNo];
	if(previewHtml!=null){
		
		if(gPreviewObj!=null){
			if(gDelayCloseTimeout!=null) {
				window.clearTimeout(gDelayCloseTimeout);
				gDelayCloseTimeout = null;
			}
			if(gPreviewObj.attr('id') != ("preview"+apvNo)){
				closePreview();
			}
		}
		
		var previewObj = $('#preview'+apvNo);
		if(previewObj.length==0){
			var offset = $(trObj).offset();
			var div = "<div id='preview"+apvNo+"' style='position:absolute; z-index:"+(++previewIndex);
			div += "; left:"+offset.left+"px; top:"+(offset.top+21)+"px; overflow:show; background-color:white; border:1px solid black; padding:4px;'";
			div += "onmouseenter='clearPreviewObj();' onmouseleave='closePreviewById(\""+apvNo+"\")'>"+previewHtml+"</div>";
			$("body:first").append(div);
			gPreviewObj = $('#preview'+apvNo);
		} else {
			previewObj.show();
			gPreviewObj = previewObj;
		}
	}
}
function closePreview(){
	if(gDelayCloseTimeout != null){
		window.clearTimeout(gDelayCloseTimeout);
		gDelayCloseTimeout = null;
	}
	if(gPreviewObj!=null){
		gPreviewObj.hide();
		gPreviewObj = null;
	}
}
function delayClosePreview(){
	if(gDelayCloseTimeout != null){
		window.clearTimeout(gDelayCloseTimeout);
		gDelayCloseTimeout = null;
	}
	gDelayCloseTimeout = window.setTimeout('closePreview();',300);
}
function closePreviewById(apvNo){
	if(gDelayCloseTimeout != null){
		window.clearTimeout(gDelayCloseTimeout);
		gDelayCloseTimeout = null;
	}
	var previewObj = $('#preview'+apvNo);
	previewObj.hide();
}
function clearPreviewObj(){
	gPreviewObj = null;
}
//-->
</script><c:if


	test="${empty isFrame}">

<c:if
	test="${empty apStorCompRVoList}"><u:title title="대기함, 기안함 등" menuNameFirst="true" /></c:if><c:if
	test="${not empty apStorCompRVoList}">
<div class="front">
	<div class="front_left">		
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px"><u:title title="대기함, 기안함 등" menuNameFirst="true" /></td>
			<td class="width5"></td>
			<td class="frontinput notPrint">
				<select id="compId" name="compId" <u:elemTitle titleId="cols.comp" /> onchange="chngStorage(this);">
				<u:option value="" titleId="ap.jsp.defaultStorage" alt="기본 저장소"/>
				<c:forEach items="${apStorCompRVoList}" var="apStorCompRVo" varStatus="status">
					<u:option value="${apStorCompRVo.storId}" title="${apStorCompRVo.storRescNm}" checkValue="${param.storId}"/>
				</c:forEach>
				</select>
			</td>
	 		</tr>
		</table>
	</div>
</div>
</c:if>


<% // 검색영역 %>
<u:searchArea style="position:relative; z-index:2;">
	<div id="searchArea1" style="<c:if test="${not empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="bxId" value="${bxId}" /><c:if
		test="${not empty param.storId}">
	<u:input type="hidden" id="storId" value="${param.storId}" /></c:if><c:if
		test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><u:buttonIcon alt="검색 조건 펼치기" titleId="cm.ico.showCondi" image="ico_wdown.png" onclick="$('#searchArea1').toggle(); $('#searchArea2').toggle();" /></td>
		<td><select id="schCat" name="schCat" ><c:if
				test="${param.bxId == 'regRecLst'}">
			<u:option value="srchDocNo" titleId="ap.doc.docNo" alt="문서번호" selected="${param.schCat == 'srchDocNo'}" /></c:if><c:if
				test="${param.bxId == 'recvRecLst'}">
			<u:option value="srchRecvDocNo" titleId="ap.doc.recvNo" alt="접수번호" selected="${param.schCat == 'srchRecvDocNo'}" /></c:if><c:if
				test="${param.bxId == 'distRecLst'}">
			<u:option value="srchRecvDocNo" titleId="ap.doc.distNo" alt="배부번호" selected="${param.schCat == 'srchRecvDocNo'}" /></c:if>
			<u:option value="docSubj" titleId="ap.doc.docSubj" alt="문서제목" selected="${param.schCat == 'docSubj'}" />
			<u:option value="makrNm" titleId="ap.doc.makrNm" alt="기안자" selected="${param.schCat == 'makrNm'}" />
			<u:option value="bodyHtml" titleId="ap.jsp.bodyHtml" alt="결재본문" selected="${param.schCat == 'bodyHtml'}" />
			<u:option value="formNm" titleId="ap.list.formNm" alt="양식명" selected="${param.schCat == 'formNm'}" />
			</select></td>
		<td><u:input id="schWord" value="${param.schWord}" titleId="cols.schWord" style="width: 260px;" maxByte="50" /></td><c:if
					test="${bxId eq 'refVwBx'}">
		<td class="width20"></td>
		<td class="search_tit"><nobr><u:term termId="ap.term.refVw" alt="참조열람" /></nobr></td>
		<td><select id="refVwStatCd" name="refVwStatCd">
			<option value=""><u:msg titleId="cm.option.all" alt="전체" /></option><c:forEach
				items="${refVwStatCdList}" var="refVwStatCd"><c:if test="${refVwStatCd.cd != 'befoRefVw'}"
				>
			<u:option value="${refVwStatCd.cd}" title="${refVwStatCd.rescNm}" selected="${param.refVwStatCd eq refVwStatCd.cd or (param.refVwStatCd eq null and refVwStatCd.cd eq refVwStatCdDefault)}" /></c:if></c:forEach>
			</select></td>
		<td colspan="3"></td></c:if>
		</tr>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form></div>
	
	<div id="searchArea2" style="<c:if test="${empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm2" name="searchForm2" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="bxId" value="${param.bxId}" /><c:if
		test="${not empty param.storId}">
	<u:input type="hidden" id="storId" value="${param.storId}" /></c:if><c:if
		test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<u:input type="hidden" id="srchDetl" value="Y" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td>
		<div style="float:left; padding: 2px 10px 0px 0px;">
			<u:buttonIcon alt="검색 조건 숨기기" titleId="cm.ico.hideCondi" image="ico_wup.png" onclick="$('#searchArea1').toggle(); $('#searchArea2').toggle();" />
		</div>
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="ap.doc.docSubj" alt="문서제목" /></td>
		<td><u:input id="docSubj" titleId="ap.doc.docSubj" alt="문서제목" value="${param.docSubj}" style="width:200px;" maxByte="50"/></td>
		<td class="width20"></td><c:if
			test="${param.bxId == 'recvRecLst'}">
		<td class="search_tit"><u:msg titleId="ap.doc.recvNo" alt="접수번호" /></td>
		<td><u:input id="srchRecvDocNo" titleId="ap.doc.recvNo" alt="접수번호" value="${param.srchRecvDocNo}" style="width:200px;" maxByte="30"/></td>
		<td class="width20"></td></c:if><c:if
			test="${param.bxId == 'distRecLst'}">
		<td class="search_tit"><u:msg titleId="ap.doc.distNo" alt="배부번호" /></td>
		<td><u:input id="srchRecvDocNo" titleId="ap.doc.distNo" alt="배부번호" value="${param.srchRecvDocNo}" style="width:200px;" maxByte="30"/></td>
		<td class="width20"></td></c:if>
		<td class="search_tit"><u:msg titleId="ap.doc.docNo" alt="문서번호" /></td>
		<td><u:input id="srchDocNo" titleId="ap.doc.docNo" alt="문서번호" value="${param.srchDocNo}" style="width:200px;" maxByte="30"/></td>
		<td class="width20"></td><c:if
			test="${param.bxId ne 'recvRecLst' and param.bxId ne 'distRecLst'}">
		<td class="search_tit"><u:msg titleId="ap.list.formNm" alt="양식명" /></td>
		<td><u:input id="formNm" titleId="ap.list.formNm" alt="양식명" value="${param.formNm}" style="width:200px;" maxByte="50"/></td>
		<td class="width20"></td></c:if>
		</tr>
		
		<tr>
		<td class="search_tit"><u:msg titleId="ap.doc.makrNm" alt="기안자" /></td>
		<td><u:input id="makrNm" titleId="ap.doc.makrNm" alt="기안자" value="${param.makrNm}" style="width:200px;" maxByte="30"/></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="ap.doc.makDeptNm" alt="기안부서" /></td>
		<td><u:input id="makDeptNm" titleId="ap.doc.makDeptNm" alt="기안부서" value="${param.makDeptNm}" style="width:200px;" maxByte="30"/></td>
		<td class="width20"></td><c:if
			test="${param.bxId eq 'recvRecLst' or param.bxId eq 'distRecLst'}">
		<td class="search_tit"><u:msg titleId="ap.list.formNm" alt="양식명" /></td>
		<td><u:input id="formNm" titleId="ap.list.formNm" alt="양식명" value="${param.formNm}" style="width:200px;" maxByte="50"/></td>
		<td class="width20"></td></c:if><c:if
			test="${not(param.bxId eq 'recvRecLst' or param.bxId eq 'distRecLst')}">
		<td colspan="3"></td></c:if>
		</tr>
		
		<tr>
		<td class="search_tit"><u:msg titleId="ap.doc.docTypNm" alt="문서구분" /></td>
		<td><select id="docTypCd" name="docTypCd"<u:elemTitle titleId="ap.doc.docTypNm" alt="문서구분" />>
			<option value=""><u:msg titleId="cm.option.all" alt="전체" /></option><c:forEach
				items="${docTypCdList}" var="docTypCd"><c:if test="${docTypCd.cd != 'paper'
					or (param.bxId == 'regRecLst' or param.bxId == 'recvRecLst' or param.bxId == 'admApvdBx' or param.bxId == 'admRegRecLst' or param.bxId == 'admRecvRecLst')}"
				>
			<u:option value="${docTypCd.cd}" title="${docTypCd.rescNm}" selected="${param.docTypCd == docTypCd.cd}" /></c:if></c:forEach>
			</select></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="ap.doc.enfcScopNm" alt="시행범위" /></td>
		<td><select id="enfcScopCd" name="enfcScopCd"<u:elemTitle titleId="ap.doc.enfcScopNm" alt="시행범위" />>
			<option value=""><u:msg titleId="cm.option.all" alt="전체" /></option><c:forEach
				items="${enfcScopCdList}" var="enfcScopCd">
			<u:option value="${enfcScopCd.cd}" title="${enfcScopCd.rescNm}" selected="${param.enfcScopCd == enfcScopCd.cd}" /></c:forEach>
			</select></td>
		<td class="width20"></td>
		<td colspan="3"></td>
		</tr>
		
		<tr>
		<td class="search_tit"><u:msg titleId="cols.docStat" alt="문서상태" /></td>
		<td><select id="docStatCd" name="docStatCd"<u:elemTitle titleId="cols.docStat" alt="문서상태" />>
			<option value=""><u:msg titleId="cm.option.all" alt="전체" /></option><c:forEach
				items="${docStatCdList}" var="docStatCd">
			<u:option value="${docStatCd.cd}" title="${docStatCd.rescNm}" selected="${param.docStatCd == docStatCd.cd}" /></c:forEach>
			</select></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="ap.jsp.enfcStat" alt="시행상태" /></td>
		<td><select id="enfcStatCd" name="enfcStatCd"<u:elemTitle titleId="ap.jsp.enfcStat" alt="시행상태" />>
			<option value=""><u:msg titleId="cm.option.all" alt="전체" /></option><c:forEach
				items="${enfcStatCdList}" var="enfcStatCd">
			<u:option value="${enfcStatCd.cd}" title="${enfcStatCd.rescNm}" selected="${param.enfcStatCd == enfcStatCd.cd}" /></c:forEach>
			</select></td>
		<td class="width20"></td>
		<td colspan="3"></td>
		</tr><c:if
			test="${param.bxId == 'myBx' or param.bxId == 'apvdBx' or param.bxId == 'postApvdBx'
				or param.bxId == 'regRecLst' or param.bxId == 'recvRecLst' or param.bxId == 'distRecLst'}">
		<tr>
			<td class="search_tit"><c:if
			test="${param.bxId == 'myBx'}"><u:input type="hidden" id="durCat"
				value="makDt" /><u:msg titleId="ap.doc.makDd" alt="기안일자" /></c:if><c:if
			test="${param.bxId == 'apvdBx' or param.bxId == 'regRecLst' or param.bxId == 'postApvdBx'}"><u:input type="hidden" id="durCat"
				value="cmplDt" /><u:msg titleId="ap.list.cmplDd" alt="완결일자" /></c:if><c:if
			test="${param.bxId == 'recvRecLst'}"><u:input type="hidden" id="durCat"
				value="recvDt" /><u:msg titleId="ap.list.recvDd" alt="접수일자" /></c:if><c:if
			test="${param.bxId == 'distRecLst'}"><u:input type="hidden" id="durCat"
				value="recvDt" /><u:msg titleId="ap.list.distDd" alt="배부일자" /></c:if></td>
			<td colspan="4">
				<table border="0" cellpadding="0" cellspacing="0"><tr>
				<td><u:calendar
				titleId="cm.cal.startDd" alt="시작일자"
				id="durStrtDt" value="${param.durStrtDt}" option="{end:'durEndDt'}" /></td>
				<td style="padding:0 3px 0 6px;"> ~ </td>
				<td><u:calendar
				titleId="cm.cal.endDd" alt="종료일자"
				id="durEndDt" value="${param.durEndDt}" option="{start:'durStrtDt'}" />
				</td>
				</tr></table></td>
			<td class="width20"></td>
			<td colspan="3"></td>
		</tr></c:if><c:if
			
			test="${param.bxId eq 'refVwBx'}">
		<tr>
		<td class="search_tit"><u:term termId="ap.term.refVw" alt="참조열람" /></td>
		<td><select id="refVwStatCd" name="refVwStatCd">
			<option value=""><u:msg titleId="cm.option.all" alt="전체" /></option><c:forEach
				items="${refVwStatCdList}" var="refVwStatCd"><c:if test="${refVwStatCd.cd != 'befoRefVw'}"
				>
			<u:option value="${refVwStatCd.cd}" title="${refVwStatCd.rescNm}" selected="${param.refVwStatCd eq refVwStatCd.cd or (param.refVwStatCd eq null and refVwStatCd.cd eq refVwStatCdDefault)}" /></c:if></c:forEach>
			</select></td>
		<td class="width20"></td>
		<td colspan="3"></td>
		<td colspan="3"></td>
		</tr></c:if>
		</table>
		</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm2.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
	</div>
</u:searchArea>
<% // 상단메뉴 
/*
toSendBx=발송함
censrBx=심사함
recvBx=접수함
distBx=배부함
regRecLst=등록 대장
recvRecLst=접수 대장
distRecLst=배부 대장

*/%>
<div class="front">
	<div class="front_right">
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr><c:if
			test="${bxId == 'waitBx' and empty param.storId}"><%//waitBx=대기함%>
		<u:secu auth="W"
		><td class="frontbtn"><u:buttonS alt="일괄결재" titleId="ap.btn.bulkApv" href="javascript:openBulkApv();" /></td></u:secu>
		</c:if><c:if
			test="${bxId == 'recvBx' and empty param.storId}"><%//recvBx=접수함%>
		<u:secu auth="A"
		><td class="frontbtn"><u:buttonS alt="일괄접수" titleId="ap.btn.bulkRecv" href="javascript:${optConfigMap.catEnab=='Y' or optConfigMap.recvRecLstSecuLvl=='Y' ? 'openRecvPop()' : 'processBulkRecv()'};" /></td></u:secu>
		</c:if><c:if
			test="${bxId == 'distBx' and empty param.storId}"><%//distBx=배부함%>
		<u:secu auth="A"
		><td class="frontbtn"><u:buttonS alt="일괄배부" titleId="ap.btn.bulkDist" href="javascript:openDocDist();" /></td></u:secu>
		</c:if><c:if
			test="${bxId eq 'waitBx' or bxId eq 'ongoBx' or bxId eq 'apvdBx' or bxId eq 'myBx' or bxId eq 'refVwBx'
						or bxId eq 'rejtBx' or bxId eq 'postApvdBx' or bxId eq 'drftBx' or bxId eq 'deptBx'}"><%/*
				
			waitBx=대기함
			ongoBx=진행함
			apvdBx=완료함
			myBx=기안함
			rejtBx=반려함
			postApvdBx=후열함
			drftBx=개인함
			deptBx=부서 대기함
			*/%><c:if test="${false}">
		<td class="frontbtn"><u:buttonS alt="선택열기" titleId="ap.btn.openSel" href="javascript:openSelected();" /></td></c:if>
		<u:secu auth="W"><c:if
			test="${(bxId eq 'waitBx' and optConfigMap.waitBxUnread eq 'Y')
				or  (bxId eq 'postApvdBx' and optConfigMap.postApvdBxUnread eq 'Y')}"
		><td class="frontbtn"><u:buttonS alt="읽지않음 표시" titleId="ap.cfg.unreadMark" href="javascript:setUnread();" /></td>
		<td class="frontbtn"><u:buttonS alt="읽음 표시" titleId="ap.cfg.readMark" href="javascript:setRead();" /></td></c:if>
		<td class="frontbtn"><u:buttonS alt="양식선택" titleId="ap.btn.selectForm" href="javascript:selectForm();" /></td></u:secu>
		<td class="frontbtn"><u:buttonS alt="상세정보" titleId="ap.btn.detlInfo" href="javascript:openDetl();" /></td></c:if><c:if
			test="${bxId eq 'toSendBx' or bxId eq 'censrBx' or bxId eq 'recvBx' or bxId eq 'distBx'
				or bxId eq 'regRecLst' or bxId eq 'recvRecLst' or bxId eq 'distRecLst' }"><%/*
			toSendBx:발송함
			censrBx:심사함
			recvBx:접수함
			distBx:배부함
			
			regRecLst:등록 대장
			recvRecLst:접수 대장
			distRecLst:배부 대장
			*/%><c:if test="${false}">
		<td class="frontbtn"><u:buttonS alt="선택열기" titleId="ap.btn.openSel" href="javascript:openSelected();" /></td></c:if>
		<td class="frontbtn"><u:buttonS alt="상세정보" titleId="ap.btn.detlInfo" href="javascript:openDetl();" /></td></c:if><c:if
			test="${bxId == 'drftBx' and empty param.storId}"><%//drftBx=개인함 %><u:secu auth="W"
		><td class="frontbtn"><u:buttonS alt="삭제" titleId="cm.btn.del" href="javascript:deleteDoc();" /></td></u:secu>
		</c:if><c:if
			test="${bxId == 'rejtBx' and optConfigMap.keepRejtDoc != 'Y' and empty param.storId}"><%// rejtBx=반려함, [옵션]keepRejtDoc:반려된 문서 등록대장에 저장 %>
		<u:secu auth="W"
		><td class="frontbtn"><u:buttonS alt="삭제" titleId="cm.btn.del" href="javascript:deleteDoc();" /></td></u:secu>
		</c:if>
		</tr>
		</tbody></table>
	</div>
</div>
</c:if><u:cmt cmt=" c:if - empty isFrame "/>

<%// 목록 %>
<form id="listApvForm">

<u:listArea id="listArea" tableStyle="table-layout:fixed;">
	<tr><c:set
			var="colCount" value="1"  />
		<td width="${not empty isFrame ? '3%' : '2.5%'}" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('listApvForm', this.checked);" value=""/></td><c:forEach
		items="${ptLstSetupDVoList}" var="ptLstSetupDVo" varStatus="status"
		><c:if test="${ptLstSetupDVo.dispYn == 'Y'}"><c:set
			var="colCount" value="${colCount + 1}"  />
		<td width="${ptLstSetupDVo.wdthPerc}" class="head_ct"><u:msg titleId="${ptLstSetupDVo.msgId}"
			/></td></c:if></c:forEach>
	</tr>
<c:if test="${recodeCount == 0}">
	<tr>
		<td class="nodata" colspan="${colCount}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
	
<c:forEach items="${apOngdBVoMapList}" var="apOngdBVoMap" varStatus="outStatus"><c:set
	var="apOngdBVoMap" value="${apOngdBVoMap}" scope="request" />
	<tr onmouseenter="this.className='trover';${optConfigMap.erpBodyPreview eq 'Y' and not empty apOngdBVoMap.intgNo
			?  'openPreview(this);': ''}" onmouseleave="this.className='trout';${optConfigMap.erpBodyPreview eq 'Y' and not empty apOngdBVoMap.intgNo
			? 'delayClosePreview();': ''}">
		<td class="bodybg_ct"><input type="checkbox" name="apvNo" value="${apOngdBVoMap.apvNo
			}" data-apvLnPno="${apOngdBVoMap.apvLnPno
			}" data-apvLnNo="${apOngdBVoMap.apvLnNo
			}" data-docPw="${not empty apOngdBVoMap.docPwEnc and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? 'Y' : ''
			}" data-docTypCd="${apOngdBVoMap.docTypCd
			}" data-sendSeq="${apOngdBVoMap.sendSeq
			}" data-docStatCd="${apOngdBVoMap.docStatCd
			}" data-docStatNm="${apOngdBVoMap.docStatNm
			}" data-apvrRoleCd="${apOngdBVoMap.apvrRoleCd
			}" data-enfcStatCd="${apOngdBVoMap.enfcStatCd
			}" data-intgNo="${apOngdBVoMap.intgNo
			}" data-docSubj="<u:out value="${apOngdBVoMap.docSubj}" type="value"
			/>" /></td><c:forEach
		items="${ptLstSetupDVoList}" var="ptLstSetupDVo" varStatus="status"
		><c:if test="${ptLstSetupDVo.dispYn == 'Y'}">
		<td class="body_lt" align="${ptLstSetupDVo.alnVa}"><c:if
		
				test="${ptLstSetupDVo.atrbId == 'docNo' or ptLstSetupDVo.atrbId == 'recvDocNo'
					 or ptLstSetupDVo.atrbId == 'docSubj'}"
			><u:set test="${apOngdBVoMap.docTypCd=='paper'}" var="docOpenFunc" value="${not empty isFrame ? 'parent.' : ''
				}openPaperDoc('${apOngdBVoMap.apvNo}','${
						not empty apOngdBVoMap.docPwEnc
						and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}')"
			/><c:if test="${apOngdBVoMap.docTypCd!='paper'}"><u:set test="${bxId=='drftBx' or (bxId=='rejtBx' and apOngdBVoMap.makrUid == sessionScope.userVo.userUid)}"
				var="docOpenFunc"
				value="remakeDoc('${apOngdBVoMap.apvNo}','${apOngdBVoMap.apvLnPno}','${apOngdBVoMap.apvLnNo}')"
				elseValue="${not empty isFrame ? 'parent.' : ''}openDoc('${apOngdBVoMap.apvNo}','${apOngdBVoMap.apvLnPno}','${apOngdBVoMap.apvLnNo}','${apOngdBVoMap.sendSeq}','${
						not empty apOngdBVoMap.docPwEnc
						and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}')"
					/><u:set test="${bxId=='drftBx' or (bxId=='rejtBx' and apOngdBVoMap.makrUid == sessionScope.userVo.userUid)}"
				var="docPopFunc"
				value="remakeDoc('${apOngdBVoMap.apvNo}','${apOngdBVoMap.apvLnPno}','${apOngdBVoMap.apvLnNo}')"
				elseValue="${not empty isFrame ? 'parent.' : ''}popDoc('${apOngdBVoMap.apvNo}','${apOngdBVoMap.apvLnPno}','${apOngdBVoMap.apvLnNo}','${apOngdBVoMap.sendSeq}','${
						not empty apOngdBVoMap.docPwEnc
						and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}')"
					/>
			</c:if><div class="ellipsis" title="<u:convertMap
				srcId="apOngdBVoMap" attId="${ptLstSetupDVo.atrbId}" type="value" />"><c:if
						test="${ptLstSetupDVo.atrbId == 'docNo' and (bxId=='recvRecLst' or bxId=='distRecLst')}"
					><u:convertMap
				srcId="apOngdBVoMap" attId="${ptLstSetupDVo.atrbId}" type="html" /></c:if><c:if
						test="${not (ptLstSetupDVo.atrbId == 'docNo' and (bxId=='recvRecLst' or bxId=='distRecLst'))}"
					><c:if test="${ptLstSetupDVo.atrbId == 'docSubj'
						and optConfigMap.openPop == 'Y'
						and (bxId=='ongoBx' or bxId=='apvdBx' or bxId=='postApvdBx')}">
						<div style="float:left; width:20px;"><a href="javascript:${docPopFunc}" onclick="$(this).parent().next().css('font-weight','normal');"><img src="/images/cm/ico_pop.gif" alt="pop-up" title="<u:msg titleId="cm.title.popup" />" /></a></div>
					</c:if><a href="javascript:${docOpenFunc}" style="${ptLstSetupDVo.atrbId == 'docSubj' ? apOngdBVoMap.fontStyle : ''}"><c:if
						test="${ptLstSetupDVo.atrbId == 'docSubj' and apOngdBVoMap.ugntDocYn == 'Y'}"
					>[<u:msg titleId="bb.option.ugnt" alt="긴급" />] </c:if><c:if
						test="${ptLstSetupDVo.atrbId == 'docSubj' and not empty apOngdBVoMap.docPwEnc}"
					>[<u:msg titleId="bb.option.secu" alt="보안" />] </c:if><u:convertMap
				srcId="apOngdBVoMap" attId="${ptLstSetupDVo.atrbId}" type="html" /></a></c:if></div></c:if><c:if
				
				test="${ptLstSetupDVo.atrbId == 'makrNm'}"
			><c:if
			test="${not empty apOngdBVoMap.makrUid}"
			><a href="javascript:${not empty isFrame ? 'parent.' : ''}viewUserPop('${apOngdBVoMap.makrUid}')"><u:out
					value="${apOngdBVoMap.makrNm}" type="html" /></a></c:if><c:if
			test="${empty apOngdBVoMap.makrUid}"
			><u:out value="${apOngdBVoMap.makrNm}" type="html" /></c:if></c:if><c:if
					
				test="${ptLstSetupDVo.atrbId == 'cmplrNm'}"
			><c:if
			test="${not empty apOngdBVoMap.cmplrUid}"
			><a href="javascript:${not empty isFrame ? 'parent.' : ''}viewUserPop('${apOngdBVoMap.cmplrUid}')"><u:out
				value="${apOngdBVoMap.cmplrNm}" type="html" /></a></c:if><c:if
			test="${empty apOngdBVoMap.cmplrUid}"
			><u:out value="${apOngdBVoMap.cmplrNm}" type="html" /></c:if></c:if><c:if
				
				test="${ptLstSetupDVo.atrbId == 'curApvrNm' and apOngdBVoMap.curApvrDeptYn == 'N'}"
			><c:if
			test="${not empty apOngdBVoMap.curApvrId}"
			><a href="javascript:${not empty isFrame ? 'parent.' : ''}viewUserPop('${apOngdBVoMap.curApvrId}')"><u:out
					value="${apOngdBVoMap.curApvrNm}" type="html" /></a></c:if><c:if
			test="${empty apOngdBVoMap.curApvrId}"
			><u:out value="${apOngdBVoMap.curApvrNm}" type="html" /></c:if></c:if><c:if
					
				test="${ptLstSetupDVo.atrbId == 'regrNm'}"
			><c:if
			test="${not empty apOngdBVoMap.regrUid}"
			><a href="javascript:${not empty isFrame ? 'parent.' : ''}viewUserPop('${apOngdBVoMap.regrUid}')"><u:out
					value="${apOngdBVoMap.makrNm}" type="html" /></a></c:if><c:if
			test="${empty apOngdBVoMap.regrUid}"
			><u:out value="${apOngdBVoMap.makrNm}" type="html" /></c:if></c:if><c:if
					
				test="${ptLstSetupDVo.atrbId == 'curApvrNm' and apOngdBVoMap.curApvrDeptYn != 'N'}"
			><u:out value="${apOngdBVoMap.curApvrNm}" type="html" /></c:if><c:if
			
				test="${ptLstSetupDVo.atrbId == 'attFileYn' and apOngdBVoMap.attFileYn == 'Y'}"
			><a href="javascript:${not empty isFrame ? 'parent.' : ''}viewAttach('${apOngdBVoMap.apvNo}','${
						not empty apOngdBVoMap.docPwEnc
						and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}')"><img alt="file download - pop"
					src="${_cxPth}/images/${_skin}/ico_file.png" /></a></c:if><c:if
					
				test="${ptLstSetupDVo.atrbId == 'makDd' or ptLstSetupDVo.atrbId == 'cmplDd'
					 or ptLstSetupDVo.atrbId == 'enfcDd' or ptLstSetupDVo.atrbId == 'recvDd'}"
			><u:convertMap srcId="apOngdBVoMap" type="date"
				attId="${fn:substring(ptLstSetupDVo.atrbId, 0, fn:length(ptLstSetupDVo.atrbId)-2)}Dt" /></c:if><c:if
				
				test="${ptLstSetupDVo.atrbId == 'docStatNm'}"
			><c:if
					test="${bxId == 'waitBx' and apOngdBVoMap.apvStatCd == 'hold'}"><u:msg
						titleId="ap.btn.hold" alt="보류" /></c:if><c:if
					test="${bxId == 'waitBx' and apOngdBVoMap.apvStatCd == 'cncl'}"><c:if
						test="${apOngdBVoMap.apvrRoleCd == 'psnOrdrdAgr'
							or apOngdBVoMap.apvrRoleCd == 'psnParalAgr'
							or apOngdBVoMap.apvrRoleCd == 'deptOrdrdAgr'
							or apOngdBVoMap.apvrRoleCd == 'deptParalAgr'
							or apOngdBVoMap.apvLnPno != '0'}"><u:msg
								titleId="ap.btn.cancelAgr" alt="합의취소" /></c:if><c:if
						test="${not (apOngdBVoMap.apvrRoleCd == 'psnOrdrdAgr'
							or apOngdBVoMap.apvrRoleCd == 'psnParalAgr'
							or apOngdBVoMap.apvrRoleCd == 'deptOrdrdAgr'
							or apOngdBVoMap.apvrRoleCd == 'deptParalAgr'
							or apOngdBVoMap.apvLnPno != '0')}"><u:msg
								titleId="ap.btn.cancelApv" alt="승인취소" /></c:if></c:if><c:if
					test="${bxId != 'waitBx' or not (apOngdBVoMap.apvStatCd == 'hold' or apOngdBVoMap.apvStatCd == 'cncl')}"><u:term
						termId="ap.term.${apOngdBVoMap.docStatCd}" /></c:if></c:if><c:if
						
						
				test="${ptLstSetupDVo.atrbId != 'docNo' and ptLstSetupDVo.atrbId != 'recvDocNo' 
					and ptLstSetupDVo.atrbId != 'docSubj' 
					
					and ptLstSetupDVo.atrbId != 'makrNm' and ptLstSetupDVo.atrbId != 'cmplrNm'
					and ptLstSetupDVo.atrbId != 'curApvrNm' and ptLstSetupDVo.atrbId != 'regrNm'
					
					and ptLstSetupDVo.atrbId != 'makDd' and ptLstSetupDVo.atrbId != 'cmplDd'
					and ptLstSetupDVo.atrbId != 'enfcDd' and ptLstSetupDVo.atrbId != 'recvDd'
					
					and ptLstSetupDVo.atrbId != 'attFileYn' and ptLstSetupDVo.atrbId != 'docStatNm'}"
			><div class="ellipsis" title="<u:convertMap
				srcId="apOngdBVoMap" attId="${ptLstSetupDVo.atrbId}" type="value"
				/>"><u:convertMap srcId="apOngdBVoMap" attId="${ptLstSetupDVo.atrbId}" type="html"
			/></div></c:if></td></c:if></c:forEach>
	</tr>
</c:forEach>
</u:listArea>
</form>
<c:if
			test="${bxId == 'waitBx'}"></c:if>
<u:pagination />

<form id="toDocForm" method="post">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="bxId" value="${bxId}" /><c:if
	test="${not empty queryString}">
<u:input type="hidden" id="queryString" value="${queryString}" /></c:if>
</form>


