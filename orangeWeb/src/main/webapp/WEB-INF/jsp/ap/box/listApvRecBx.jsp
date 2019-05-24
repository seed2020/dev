<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><jsp:useBean id="call" class="com.innobiz.orange.web.ap.utils.ELCall" scope="application"
/>
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
	if(refDocApvNo!='') $('<input>', {'name':'refDocApvNo','value':refDocApvNo,'type':'hidden'}).insertBefore($bx);
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
// 비밀번호 세팅된 문서 열기 - setDocPwPop.jsp 에서 호출 %>
function openSecuDoc(secuId){
	initForm(false).appendHidden({'name':'secuId','value':secuId
		}).attr("action", "./setDoc.do").attr("method", "GET").submit();
}<%
// 첨부 조회%>
function viewAttach(apvNo){
	dialog.open("setDocAttchDialog", '<u:msg titleId="ap.btn.att" alt="첨부" />', "./viewDocAttchPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo);
}<%
// 분류변경 %>
var gChngClaApvNos = null;
function chngClsPop(){
	var $inputs = getSelected(false), arr = [];
	if($inputs==null){
		gChngClaApvNos = null;
		return;
	}
	$inputs.each(function(){
		arr.push(this.value);
	});
	gChngClaApvNos = arr.join(',');
	dialog.open("setChngOrgClsDialog", '<u:msg alt="분류변경" titleId="ap.btn.chngCls" />', "./treeOrgClsPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&callback=callbackChngCls");
}<%
// 분류변경 - callback %>
function callbackChngCls(){
	if(gChngClaApvNos==null){
		dialog.close('setChngOrgClsDialog');
		return;
	}
	if(gClsId=='ROOT' || gClsId==null){
		alertMsg('ap.msg.chooseCls');<%//ap.msg.chooseCls=분류정보를 선택해 주십시요.%>
		return;
	}
	if(!confirmMsg('ap.cfrm.moveDocToCls', [gClsNm])){<%//ap.cfrm.moveDocToCls=분류 "{0}"(으)로 문서를 이동하시겠습니까 ?%>
		return;
	}
	var result = false;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"chngCls", clsInfoId:gClsId, apvNos:gChngClaApvNos}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result){
		clickClsInfo(gClsInfoId);
	}
	dialog.close('setChngOrgClsDialog');
}<%


// 담당자 지정 - 접수문서의 담당자를 지정할때 %>
function setMakVw(){
	var $checks = getSelected(false);
	if($checks==null) return;
	var apvNos =[];
	$checks.each(function(){
		if($(this).attr('data-docStatCd')=='recv'){
			apvNos.push($(this).val());
		}
	});
	if(apvNos.length==0){<%
		// ap.msg.noPichDoc=담당자 지정할 문서가 없습니다. %>
		alertMsg('ap.msg.noPichDoc');
		return;
	}
	var opt = {titleId:"ap.title.setPich",userStatCd:"02"};
	var oneDeptId = "${optConfigMap.exRecvRange == 'Y' ? '' : sessionScope.userVo.deptId}";
	if(oneDeptId != '') opt['oneDeptId'] = oneDeptId;
	searchUserPop(opt, function(userVo){<%
		// ap.cfrm.setPich="{0}"을(를) 담당자로 지정 하시겠습니까 ? %>
		if(confirmMsg("ap.cfrm.setPich",[userVo.rescNm])){
			var result = false;
			var param = {process:"setMakVwList", apvNos:apvNos.join(','), apvrUid:userVo.userUid};
			callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", param, function(data){
				if(data.message != null) alert(data.message);
				result = data.result == 'ok';
			});
			if(result){<%
				// 목록 프레임 리로드 %>
				$("#searchForm1, #searchForm2").each(function(){
					$form = $(this);
					if($form.is(":visible")) $form.submit();
				});
			}
		} else {
			return false;
		}
	});
}<%
// 공람확인 %>
function listPubBxVw(){
	var $check = getSelected(true);
	if($check != null){
		var apvNo = $check.val();
		dialog.open("listPubBxVwDialog", '<u:msg alt="공람확인" titleId="ap.btn.listPubBxVw" />', "./listPubBxVwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo+"&pubBxDeptId="+gRecLstDeptId);
	}
}<%
// 공람게시 %>
function regPubBx(){
	var $checks = getSelected(false);
	if($checks!=null){
		var arr=[], apvNo;
		$checks.each(function(){
			apvNo = $(this).val();
			if(apvNo!=null && apvNo!='') arr.push(apvNo);
		});
		var withDeptId = "${param.bxId == 'recvRecLst' and optConfigMap.exRecvRange == 'Y' ? 'Y' : ''}";
		dialog.open("setPubBxDialog", '<u:msg titleId="ap.btn.regPubBx" alt="공람게시" />', "./setPubBxPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNos="+arr.join(',')+(withDeptId=='Y' && gRecLstDeptId!='' ? '&recLstDeptId='+gRecLstDeptId : ''));
	}
}<%

// 선택된 목록 리턴 %>
function getSelected(needOne){
	return getIframeContent("listApvBxFrm").getSelected(needOne);
}<%
// 상세정보 %>
function openDetl(onTab){
	getIframeContent("listApvBxFrm").openDetl(onTab);
}<%
// 접수확인 %>
function openCfrmRecv(onTab){
	getIframeContent("listApvBxFrm").openCfrmRecv(onTab);
}<%
// 문서조회 Form 리턴 %>
function initForm(init){
	var $form = $("#toDocForm");
	if(init!=false){
		$form.find("[name='apvNo'], [name='formId'], [name='apvLnPno'], [name='secuId'], [name='sendSeq']").remove();
	}
	return $form;
}<%
// 문서 조회 %>
function openDoc(apvNo, apvLnPno, apvLnNo, sendSeq, pwYn){
	var $form = initForm();
	var $bx = $form.find("#bxId");
	$('<input>', {'name':'apvNo','value':apvNo,'type':'hidden'}).insertBefore($bx);
	if(apvLnPno!='') $('<input>', {'name':'apvLnPno','value':apvLnPno,'type':'hidden'}).insertBefore($bx);
	if(apvLnNo!='') $('<input>', {'name':'apvLnNo','value':apvLnNo,'type':'hidden'}).insertBefore($bx);
	if(sendSeq!='') $('<input>', {'name':'sendSeq','value':sendSeq,'type':'hidden'}).insertBefore($bx);
	if(gRecLstDeptId!=''){
		if($bx.val()=='pubBx') $('<input>', {'name':'pubBxDeptId','value':gRecLstDeptId,'type':'hidden'}).insertBefore($bx);
		else if($bx.val()=='recvBx') $('<input>', {'name':'recvDeptId','value':gRecLstDeptId,'type':'hidden'}).insertBefore($bx);
	}
	setQueryString($form);
	if(pwYn=='Y'){
		dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo);
	} else {
		$form.attr("action", "./setDoc.do").attr("method", "GET").submit();
	}
}<%
// 종이 문서 조회 %>
function openPaperDoc(apvNo, pwYn){
	var $form = initForm();
	var $bx = $form.find("#bxId");
	$('<input>', {'name':'apvNo','value':apvNo,'type':'hidden'}).insertBefore($bx);
	if($bx.val()=='pubBx' && gRecLstDeptId!=''){
		$('<input>', {'name':'pubBxDeptId','value':gRecLstDeptId,'type':'hidden'}).insertBefore($bx);
	}
	setQueryString($form);
	
	if(pwYn=='Y'){
		dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo+"&callback=openSecuPaperDoc");
	} else {
		dialog.open("setPaperDocDialog", '<u:msg alt="종이문서 조회" titleId="ap.jsp.viewPaper" />', "./viewPaperDocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo);
	}
}<%
// 탭 클릭 - 등록대장 - allDept:분류정보/regRecLst:전체부서 %>
function toggleTabBtn(tabCd){
	var bxId = $("#toDocForm").find("#bxId").val();
	var catEnab = '${optConfigMap.catEnab}';
	if(bxId=='regRecLst'){<%// 등록대장 - 분류정보 사용 안할때 - 전체부서:79%, 소속부서:100% %>
		if(catEnab != 'Y'){
			$("#listApvBxFrmDiv").css("width", tabCd=='allDept' ? '79%' : '100%').css('float', tabCd=='allDept' ? 'right' : '');
		}
	} else if(bxId=='recvRecLst'){<%// 접수대장 - 분류정보 사용안함, 접수범위 확장 일때 - 관련부서:79%, 소속부서:100% %>
		var exRecvRange = '${optConfigMap.exRecvRange}';
		if(catEnab != 'Y' && exRecvRange == 'Y'){
			$("#listApvBxFrmDiv").css("width", tabCd=='relDept' ? '79%' : '100%').css('float', tabCd=='relDept' ? 'right' : '');
		}
	}
	
	var $form;
	$("#searchForm1, #searchForm2").each(function(){
		$form = $(this);
		$form.find("input[name='tabCd']").remove();
		$form.appendHidden({name:'tabCd',value:tabCd});
	});
	if(tabCd=='allDept'){
		clickRecLstOrg(gRecLstDeptId);
		$("#recLstTab .tab_right").find("#regPaperBtn, #regSpPaperBtn, #modPaperBtn, #delPaperBtn, #regPubBxBtn, #chngClsBtn, #cfrmRecvBtn").hide();
	} else if(tabCd=='relDept'){
		clickRecLstOrg(gRecLstDeptId);
		$("#recLstTab .tab_right").find("#regPaperBtn, #regSpPaperBtn, #modPaperBtn, #delPaperBtn, #chngClsBtn, #cfrmRecvBtn").hide();
	} else {
		clickClsInfo(gClsInfoId);
		$("#recLstTab .tab_right").find("#regPaperBtn, #regSpPaperBtn, #modPaperBtn, #delPaperBtn, #regPubBxBtn, #chngClsBtn, #cfrmRecvBtn").show();
	}
}<%
// 선택된 분류 정보 %>
var gClsInfoId='${param.clsInfoId}';<%
// 분류정보 클릭 - 분류정보 프레임에서 호출%>
function clickClsInfo(cd, nm){
	gClsInfoId = cd;
	var $form;
	$("#searchForm1, #searchForm2").each(function(){
		$form = $(this);
		$form.find("input[name='recLstDeptId'], input[name='pubBxDeptId'], input[name='clsInfoId']").remove();
		$form.appendHidden({name:'clsInfoId',value:cd=='ROOT' ? '' : cd});
		if($form.is(":visible")) $form.submit();
	});
}<%
// 선택된 부서 - 공람게시,등록대장(전체부서),접수함(대장) %>
var gRecLstDeptId='${not empty param.recLstDeptId ? param.recLstDeptId : param.recvDeptId}';
var firstDeptCall = true;<%
// 조직도 클릭 - 등록대장, 공람게시 %>
function clickRecLstOrg(id, nm){
	if(firstDeptCall){
		gRecLstDeptId = id;
		firstDeptCall = false;
	} else {
		gRecLstDeptId = id;
		var myDeptId = '${sessionScope.userVo.deptId}';
		$("#searchForm1, #searchForm2").each(function(){
			$form = $(this);
			if(!$form.is(":visible")) return;
			
			$form.find("input[name='recLstDeptId'], input[name='pubBxDeptId'], input[name='clsInfoId'], input[name='recvDeptId']").remove();
			var bxId = $form.find("[name='bxId']").val();
			if(bxId=='pubBx'){
				$form.appendHidden({name:'pubBxDeptId',value:id});
				$form.submit();
			} else if(bxId=='recvBx'){
				if(id!=myDeptId) $form.appendHidden({name:'recvDeptId',value:id});
				$form.submit();
			} else if(bxId=='regRecLst'){
				$form.appendHidden({name:'recLstDeptId',value:id});
				$form.submit();
			} else {<%
				//if(id!=myDeptId) $form.appendHidden({name:'recLstDeptId',value:id});// 자기부서 - 오픈 안된것 포함 조회 일 경우 %>
				if(id!=myDeptId) $form.appendHidden({name:'recLstDeptId',value:id});
				$form.submit();
			}
		});
	}
}<%
// 프레임 높이 조절 %>
function setFrameHeight(height){
	$("#listApvBxFrm").css("height", (height)+"px");
}<%
// 페이지 로 카운트 세팅 %>
function setPageInfo(pageNo, pageRowCnt){
	var $form;
	$("#searchForm1, #searchForm2").each(function(){
		$form = $(this);
		$form.find("input[name='pageRowCnt']").remove();
		if(pageRowCnt!='')$form.appendHidden({name:'pageRowCnt', value:pageRowCnt});
	});
	gPageNo = pageNo;
}<%
// 페이지번호 - 문서보기 후 취소로 목록에 왔을 때 해당 페이지로 이동할 목적 %>
var gPageNo = '';<%
// queryString 설정 - 문서보기 후 취소로 목록에 왔을 때 해당 페이지로 이동할 목적 %>
function setQueryString($form){
	var param = new ParamMap(), arr=[];
	if(gPageNo!='') param.put("pageNo",gPageNo);
	param.getData($("#searchForm1:visible, #searchForm2:visible")[0]);
	param.each(function(key, value){
		if(value!='' && key!='menuId' && key!='bxId'){
			arr.push(key+'='+encodeURIComponent(value));
		}
	});
	$form.find("#queryString").val(arr.join('&'));
}<%
// 종이문서 등록/수정/삭제/분리등록 %>
function setPaperDoc(mode){
	if(mode=='reg'){<%// 등록 %>
		dialog.open("setPaperDocDialog", '<u:msg alt="종이문서 등록" titleId="ap.btn.regPaper" />', "./setPaperDocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}");
	} else if(mode=='del'){<%// 삭제 %>
		var $checks = getSelected(false);
		if($checks==null) return;
		var apvNos=[], hasNotPaper = false;
		$checks.each(function(){
			if($(this).attr("data-docTypCd")!='paper') hasNotPaper = true;
			else apvNos.push($(this).val());
		});
		if(hasNotPaper){<%
			// ap.msg.onlyCanDelPaper=종이문서만 삭제 가능 합니다. %>
			alertMsg('ap.msg.onlyCanDelPaper');
			return;
		}<%
		// 분리등록한 부모인지 체크해서 메세지 출력 - confirm %>
		var message = '';
		callAjax("./checkPaperChildAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {apvNos:apvNos.join(',')}, function(data){
			message = data.cfrm;
		});
		if(!confirm(message)) return;
		
		callAjax("./delPaperDocAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {apvNos:apvNos.join(',')}, function(data){
			if(data.message != null) alert(data.message);
			if(data.result=='ok'){
				$("#searchForm1:visible, #searchForm2:visible").submit();
			}
		});
		
	} else {
		var $check = getSelected(true);
		if($check==null) return;
		if($check.attr("data-docTypCd")!='paper'){
			var act = mode=='mod' ? '<u:msg alt="종이문서 수정" titleId="ap.btn.modPaper" />' : '<u:msg alt="종이문서 분리등록" titleId="ap.btn.regSpPaper" />';<%
			// ap.msg.canDoOnlyPaper=종이문서만 "{0}"이(가) 가능 합니다. %>
			alertMsg('ap.msg.canDoOnlyPaper',[act]);
			return;
		}
		
		if($check.attr("data-docPw")=='Y'){
			dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+$check.val()+"&callback=openSecuPaperDoc&mode="+mode);
		} else {
			if(mode=='mod'){<%// 수정 %>
				dialog.open("setPaperDocDialog", '<u:msg alt="종이문서 등록" titleId="ap.btn.regPaper" />', "./setPaperDocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=set&apvNo="+$check.val());
			} else if(mode=='spReg'){<%// 분리등록 %>
				dialog.open("setPaperDocDialog", '<u:msg alt="종이문서 분리등록" titleId="ap.btn.regSpPaper" />', "./setPaperDocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=spReg&apvNo="+$check.val());
			}
		}
	}
}<%

////////////////////////////////////////////////
//
//  개인분류
//
// 개인분류 관리 - 기안함 %>
function managePsnTree(mode){
	getIframeContent('treePsnClsFrm').managePsnCls(mode);
}<%
// 개인분류 - 등록/수정/삭제 후 리로드 %>
function reloadTree(psnClsInfoId, type){
	if(psnClsInfoId==null || psnClsInfoId==''){
		getIframeContent('treePsnClsFrm').reload('./treePsnClsFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}');
	} else {
		getIframeContent('treePsnClsFrm').reload('./treePsnClsFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&psnClsInfoId='+psnClsInfoId);
	}
}
var gPsnClsInfoId = null;<%
// 개인분류 - 클릭 %>
function clickPsnClsInfo(cd, nm){
	gPsnClsInfoId = cd;
	var $form;
	$("#searchForm1, #searchForm2").each(function(){
		$form = $(this);
		$form.find("input[name='recLstDeptId'], input[name='pubBxDeptId'], input[name='clsInfoId'], input[name='psnClsInfoId']").remove();
		$form.appendHidden({name:'psnClsInfoId',value:cd=='ROOT' ? '' : cd});
		if($form.is(":visible")) $form.submit();
	});
}<%
// 개인분류 - 분류변경 - 문서의 분류를 변경 하는 것 - 개인분류 트리 팝업 %>
function chngPsnCls(){
	var $inputs = getSelected(false), arr = [];
	if($inputs==null){
		gChngClaApvNos = null;
		return;
	}
	$inputs.each(function(){
		arr.push(this.value);
	});
	gChngClaApvNos = arr.join(',');
	dialog.open("setChngPsnClsDialog", '<u:msg alt="개인분류변경" titleId="ap.btn.chngPsnCls" />', "./treePsnClsPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&callback=callbackChngPsnCls");
}<%
// 개인분류 - 분류변경 - callback:chngPsnCls %>
function callbackChngPsnCls(){
	if(gChngClaApvNos==null){
		dialog.close('setChngOrgClsDialog');
		return;
	}
	if(gClsId=='ROOT' || gClsId==null){
		alertMsg('ap.msg.chooseCls');<%//ap.msg.chooseCls=분류정보를 선택해 주십시요.%>
		return;
	}
	if(!confirmMsg('ap.cfrm.moveDocToCls', [gClsNm])){<%//ap.cfrm.moveDocToCls=분류 "{0}"(으)로 문서를 이동하시겠습니까 ?%>
		return;
	}
	var result = false;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"chngPsnCls", psnClsInfoId:gClsId, apvNos:gChngClaApvNos}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result){
		clickPsnClsInfo(gPsnClsInfoId);
	}
	dialog.close('setChngPsnClsDialog');
}<%
// 개인분류 - 분류 이동 callback %>
function callbackMovePsnCls(){
	if(gClsId==null){
		alertMsg('ap.msg.chooseCls');<%//ap.msg.chooseCls=분류정보를 선택해 주십시요.%>
		return;
	}
	if(gClsId == gPsnClsInfoId){
		alertMsg('ap.msg.chooseDifCls');<%//ap.msg.chooseDifCls=동일한 분류정보로 이동 할 수 없습니다.%>
		return;
	}
	var result = false;
	callAjax("./transPsnClsCutPasteAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {from:gPsnClsInfoId, to:gClsId}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result){
		reloadTree(gPsnClsInfoId, 'psn');
		dialog.close('setMovePsnClsDialog');
	}
}<%
// 개인분류 상 하 이동 %>
function movePsnCls(direction){
	getIframeContent('treePsnClsFrm').move(direction);
}<%
// 저장소 변경 %>
function chngStorage(obj){
	var storId = $(obj).val();
	location.href = '${_uri}?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}' + (storId=='' ? '' : '&storId='+storId);
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
	if(gRecLstDeptId!=null && gRecLstDeptId!='') data['recvDeptId'] = gRecLstDeptId;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", data, function(data){
		if(data.message != null) alert(data.message);
		dialog.close('setClsInfoDialog');<%
		// 목록 다시조회 %>
		clickRecLstOrg(gRecLstDeptId);
	});
}<%
// 경로조회(결재라인 목록형 에서 호출:viewApvLnInc.jsp) - apvLnPno 가 없으면 "전체경로", 있으면 해당 서브 경로
// 주로 viewDocDetlPop.jsp 에서 사용되며, 본문에 목록형 결재라인이 추가되어 이곳으로 스크립트 옮겨옴 %>
function viewApvLnPop(apvNo, apvLnPno, deptNm){
	var popTitle = apvLnPno==null ? '<u:msg alt="전체경로" titleId="ap.btn.allApvLn" />' : '<u:msg alt="결재 경로" titleId="ap.jsp.apvLn" />' + (deptNm==null ? '' : ' - '+deptNm);
	var popId = apvLnPno==null ? 'viewApvLnDialog' : 'viewSubApvLnDialog';
	dialog.open(popId, popTitle, "./viewApvLnPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo+(apvLnPno==null ? '' : '&apvLnPno='+apvLnPno));
}<%
// onload %>
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
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

<% // 검색영역 -  style="position:relative; z-index:2;" : 달력이 탭 밑으로 들어가는것 방지 %>
<u:searchArea style="position:relative; z-index:2;">
	<div id="searchArea1" style="<c:if test="${not empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm1" name="searchForm1" target="listApvBxFrm" action="./listApvBxFrm.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="bxId" value="${param.bxId}" /><c:if
		test="${not empty param.storId}">
	<u:input type="hidden" id="storId" value="${param.storId}" /></c:if><c:if
		test="${not empty param.tabCd}">
	<u:input type="hidden" id="tabCd" value="${param.tabCd}" /></c:if><c:if
		test="${not empty param.clsInfoId}">
	<u:input type="hidden" id="clsInfoId" value="${param.clsInfoId}" /></c:if><c:if
		test="${not empty param.recLstDeptId}">
	<u:input type="hidden" id="recLstDeptId" value="${param.recLstDeptId}" /></c:if><c:if
		test="${not empty param.pubBxDeptId}">
	<u:input type="hidden" id="pubBxDeptId" value="${param.pubBxDeptId}" /></c:if>
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
		<td><u:input id="schWord" value="${param.schWord}" titleId="cols.schWord" style="width: 260px;" maxByte="50" /></td>
		</tr>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form></div>
	
	<div id="searchArea2" style="<c:if test="${empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm2" name="searchForm2" target="listApvBxFrm" action="./listApvBxFrm.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="bxId" value="${param.bxId}" /><c:if
		test="${not empty param.storId}">
	<u:input type="hidden" id="storId" value="${param.storId}" /></c:if><c:if
		test="${not empty param.tabCd}">
	<u:input type="hidden" id="tabCd" value="${param.tabCd}" /></c:if><c:if
		test="${not empty param.clsInfoId}">
	<u:input type="hidden" id="clsInfoId" value="${param.clsInfoId}" /></c:if><c:if
		test="${not empty param.recLstDeptId}">
	<u:input type="hidden" id="recLstDeptId" value="${param.recLstDeptId}" /></c:if><c:if
		test="${not empty param.pubBxDeptId}">
	<u:input type="hidden" id="pubBxDeptId" value="${param.pubBxDeptId}" /></c:if>
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
			test="${param.bxId eq 'recvRecLst'}">
		<td class="search_tit"><u:msg titleId="ap.doc.recvNo" alt="접수번호" /></td>
		<td><u:input id="srchRecvDocNo" titleId="ap.doc.recvNo" alt="접수번호" value="${param.srchRecvDocNo}" style="width:200px;" maxByte="30"/></td>
		<td class="width20"></td></c:if><c:if
			test="${param.bxId eq 'distRecLst'}">
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
<c:if test="${param.bxId != 'pubBx'}">
		<tr>
		<td class="search_tit"><u:msg titleId="ap.doc.docTypNm" alt="문서구분" /></td>
		<td><select id="docTypCd" name="docTypCd"<u:elemTitle titleId="ap.doc.docTypNm" alt="문서구분" />>
			<option value=""><u:msg titleId="cm.option.all" alt="전체" /></option><c:forEach
				items="${docTypCdList}" var="docTypCd"><c:if test="${docTypCd.cd != 'paper'
					or (bxId == 'regRecLst' or bxId == 'recvRecLst')}"
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
		</tr>
</c:if><c:if test="${param.bxId == 'pubBx'}">
		<tr>
		<td class="search_tit"><u:msg titleId="ap.list.recLstTyp" alt="대장구분" /></td>
		<td><select id="recLstTypCd" name="recLstTypCd"<u:elemTitle titleId="ap.list.recLstTyp" alt="대장구분" />>
			<option value=""><u:msg titleId="cm.option.all" alt="전체" /></option>
			<option value="regRecLst" ${param.recLstTypCd=='regRecLst' ? 'selected="selected"' : ''}><u:msg titleId="ap.bx.regRecLst" alt="등록대장" /></option>
			<option value="recvRecLst" ${param.recLstTypCd=='recvRecLst' ? 'selected="selected"' : ''}><u:msg titleId="ap.bx.recvRecLst" alt="접수대장" /></option>
			</select></td>
		<td class="width20"></td>
		<td class="search_tit"><u:input type="hidden" id="durCat"
				value="pubBxDt" /><u:msg titleId="ap.list.pubBxDd" alt="게시일자" /></td>
		<td>
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
		</tr>
</c:if><c:if
			test="${param.bxId == 'regRecLst' or param.bxId == 'recvRecLst'
				or param.bxId == 'distRecLst' or param.bxId == 'myBx'}">
		<tr>
			<td class="search_tit"><c:if
			test="${param.bxId == 'regRecLst'}"><u:input type="hidden" id="durCat"
				value="cmplDt" /><u:msg titleId="ap.list.cmplDd" alt="완결일자" /></c:if><c:if
			test="${param.bxId == 'recvRecLst'}"><u:input type="hidden" id="durCat"
				value="recvDt" /><u:msg titleId="ap.list.recvDd" alt="접수일자" /></c:if><c:if
			test="${param.bxId == 'distRecLst'}"><u:input type="hidden" id="durCat"
				value="recvDt" /><u:msg titleId="ap.list.distDd" alt="배부일자" /></c:if><c:if
			test="${param.bxId == 'myBx'}"><u:input type="hidden" id="durCat"
				value="makDt" /><u:msg titleId="ap.doc.makDd" alt="기안일자" /></c:if></td>
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
		</tr>
		</c:if>
		</table>
		</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm2.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
	</div>
</u:searchArea>
<c:if



		test="${bxId == 'regRecLst'}"><%//regRecLst:등록 대장 %>
<div style="height:2px;"></div>
<u:tabGroup id="recLstTab" noBottomBlank="${true}">
	<u:tab id="recLstTab" alt="소속 부서" areaId="regRecLstTabArea" titleId="ap.cfg.inDept"
		on="${param.tabCd != 'allDept'}"
		onclick="toggleTabBtn('regRecLst');" />
	<u:tab id="recLstTab" alt="전체 부서" areaId="allDeptTabArea" titleId="ap.tab.allDept"
		on="${param.tabCd == 'allDept'}"
		onclick="toggleTabBtn('allDept');" /><c:if
		
		test="${empty param.storId}">
	<u:tabButton id="regPaperBtn" alt="종이문서 등록" titleId="ap.btn.regPaper" href="javascript:setPaperDoc('reg');" auth="A"
	/><u:tabButton id="regSpPaperBtn" alt="종이문서 분리등록" titleId="ap.btn.regSpPaper" href="javascript:setPaperDoc('spReg');" auth="A"
	/><u:tabButton id="modPaperBtn" alt="종이문서 수정" titleId="ap.btn.modPaper" href="javascript:setPaperDoc('mod');" auth="A"
	/><u:tabButton id="delPaperBtn" alt="종이문서 삭제" titleId="ap.btn.delPaper" href="javascript:setPaperDoc('del');" auth="A"
	/><span style="width:5px;" class="sbutton"></span></c:if><c:if
		test="${optConfigMap.regRecLstPubBx=='Y' and empty param.storId}"
	><u:tabButton id="regPubBxBtn" alt="공람게시" titleId="ap.btn.regPubBx" href="javascript:regPubBx();" auth="A"	/></c:if
	><c:if
		test="${optConfigMap.catEnab == 'Y'}"
	><u:tabButton id="chngClsBtn" alt="분류변경" titleId="ap.btn.chngCls" href="javascript:chngClsPop();" auth="A"	/></c:if
	><u:tabButton id="detlInfoBtn" alt="상세정보" titleId="ap.btn.detlInfo" href="javascript:openDetl();"
	/><u:tabButton id="cfrmRecvBtn" titleId="ap.btn.cfrmRecv" alt="접수확인" href="javascript:openCfrmRecv();"
	/>
</u:tabGroup>
<u:tabArea
	outerStyle="min-height:513px; overflow-x:hidden; overflow-y:hidden;"
	innerStyle="min-height:490px; margin:10px;">
<c:if

	test="${optConfigMap.catEnab == 'Y'}"><u:cmt cmt="[옵션] 분류정보 사용" />
<u:boxArea id="regRecLstTabArea"
	className="wbox" noBottomBlank="${true}"
	style="float:left; width:20%;${param.tabCd != 'allDept' ? '' : ' display:none;'}"
	outerStyle="min-height:490px; overflow:hidden;"
	innerStyle="NO_INNER_IDV">
<iframe id="treeClsInfoFrm" name="treeClsInfoFrm" src="./treeOrgClsFrm.do?bxId=${param.bxId}&menuId=${menuId
	}${not empty param.clsInfoId ? '&clsInfoId='.concat(param.clsInfoId) : ''}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:boxArea>
</c:if>

<u:boxArea id="allDeptTabArea"
	className="wbox" noBottomBlank="${true}"
	style="float:left; width:20%;${param.tabCd == 'allDept' ? '' : ' display:none;'}"
	outerStyle="height:490px; overflow:hidden;"
	innerStyle="NO_INNER_IDV">
<iframe id="treeRecLstOrgFrm" name="treeRecLstOrgFrm" src="./treeRecLstOrgFrm.do?bxId=${param.bxId}&menuId=${menuId
	}${not empty param.recLstDeptId ? '&orgId='.concat(param.recLstDeptId) : ''}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:boxArea>

<div id="listApvBxFrmDiv" style="${optConfigMap.catEnab == 'Y' ? 'float:right; width:79%' : ''}">
<iframe id="listApvBxFrm" name="listApvBxFrm" src="./listApvBxFrm.do?bxId=${param.bxId}&menuId=${menuId
	}${not empty queryString ? '&'.concat(queryString) : ''}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
</u:tabArea>
</c:if><c:if



		test="${bxId == 'recvRecLst' and optConfigMap.exRecvRange == 'Y'}"><%//recvRecLst:접수 대장, 접수 범위 확장 의 경우 %>
<div style="height:2px;"></div>
<u:tabGroup id="recLstTab" noBottomBlank="${true}">
	<u:tab id="recLstTab" alt="소속 부서" areaId="regRecLstTabArea" titleId="ap.cfg.inDept"
		on="${param.tabCd != 'relDept'}"
		onclick="toggleTabBtn('recvRecLst');" />
	<u:tab id="recLstTab" alt="관련 부서" areaId="allDeptTabArea" titleId="ap.cfg.relDept"
		on="${param.tabCd == 'relDept'}"
		onclick="toggleTabBtn('relDept');" /><c:if
		
		test="${empty param.storId}">
	<u:tabButton id="regPaperBtn" alt="종이문서 등록" titleId="ap.btn.regPaper" href="javascript:setPaperDoc('reg');" auth="A"
	/><u:tabButton id="regSpPaperBtn" alt="종이문서 분리등록" titleId="ap.btn.regSpPaper" href="javascript:setPaperDoc('spReg');" auth="A"
	/><u:tabButton id="modPaperBtn" alt="종이문서 수정" titleId="ap.btn.modPaper" href="javascript:setPaperDoc('mod');" auth="A"
	/><u:tabButton id="delPaperBtn" alt="종이문서 삭제" titleId="ap.btn.delPaper" href="javascript:setPaperDoc('del');" auth="A"
	/><span style="width:5px;" class="sbutton"></span></c:if><c:if
		test="${empty param.storId}"
	><u:tabButton id="setPichBtn" alt="담당자지정" titleId="ap.btn.setPich" href="javascript:setMakVw();" auth="A"	/></c:if><c:if
		test="${optConfigMap.regRecLstPubBx=='Y' and empty param.storId}"
	><u:tabButton id="regPubBxBtn" alt="공람게시" titleId="ap.btn.regPubBx" href="javascript:regPubBx();" auth="A"	/></c:if
	><c:if
		test="${optConfigMap.catEnab == 'Y'}"
	><u:tabButton id="chngClsBtn" alt="분류변경" titleId="ap.btn.chngCls" href="javascript:chngClsPop();" auth="A"	/></c:if
	><u:tabButton id="detlInfoBtn" alt="상세정보" titleId="ap.btn.detlInfo" href="javascript:openDetl();"
	/>
</u:tabGroup>
<u:tabArea
	outerStyle="min-height:513px; overflow-x:hidden; overflow-y:hidden;"
	innerStyle="min-height:490px; margin:10px;">
<c:if

	test="${optConfigMap.catEnab == 'Y'}"><u:cmt cmt="[옵션] 분류정보 사용" />
<u:boxArea id="regRecLstTabArea"
	className="wbox" noBottomBlank="${true}"
	style="float:left; width:20%;${param.tabCd != 'relDept' ? '' : ' display:none;'}"
	outerStyle="min-height:490px; overflow:hidden;"
	innerStyle="NO_INNER_IDV">
<iframe id="treeClsInfoFrm" name="treeClsInfoFrm" src="./treeOrgClsFrm.do?bxId=${param.bxId}&menuId=${menuId
	}${not empty param.clsInfoId ? '&clsInfoId='.concat(param.clsInfoId) : ''}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:boxArea>
</c:if>

<u:boxArea id="allDeptTabArea"
	className="wbox" noBottomBlank="${true}"
	style="float:left; width:20%;${param.tabCd == 'relDept' ? '' : ' display:none;'}"
	outerStyle="height:490px; overflow:hidden;"
	innerStyle="NO_INNER_IDV">
<iframe id="treeClsFrm" name="treeClsFrm" src="./treeRecLstOrgFrm.do?bxId=${param.bxId}&menuId=${menuId
	}&upward=${sessionScope.userVo.orgId}&brother=Y${empty param.recLstDeptId ? '' : '&orgId='.concat(param.recLstDeptId)}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:boxArea>

<div id="listApvBxFrmDiv" style="${optConfigMap.catEnab == 'Y' ? 'float:right; width:79%' : ''}">
<iframe id="listApvBxFrm" name="listApvBxFrm" src="./listApvBxFrm.do?bxId=${param.bxId}&menuId=${menuId
	}${not empty queryString ? '&'.concat(queryString) : ''}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
</u:tabArea>
</c:if><c:if



	test="${bxId == 'recvRecLst' and optConfigMap.exRecvRange != 'Y'}"><%//recvRecLst:접수 대장, 접수 범위 확장 이 아닌 경우 %>
<div class="front">
	<div class="front_right">
		<table border="0" cellpadding="0" cellspacing="0"><tbody><tr><u:secu
			auth="A"><c:if
		
				test="${empty param.storId}">
		<td class="frontbtn"><u:buttonS id="regPaperBtn" alt="종이문서 등록" titleId="ap.btn.regPaper" href="javascript:setPaperDoc('reg');" /></td>
		<td class="frontbtn"><u:buttonS id="regSpPaperBtn" alt="종이문서 분리등록" titleId="ap.btn.regSpPaper" href="javascript:setPaperDoc('spReg');" /></td>
		<td class="frontbtn"><u:buttonS id="modPaperBtn" alt="종이문서 수정" titleId="ap.btn.modPaper" href="javascript:setPaperDoc('mod');" /></td>
		<td class="frontbtn"><u:buttonS id="delPaperBtn" alt="종이문서 삭제" titleId="ap.btn.delPaper" href="javascript:setPaperDoc('del');" /></td>
		<td style="width:5px;"></td></c:if><c:if
				test="${empty param.storId}">
		<td class="frontbtn"><u:buttonS id="setPichBtn" alt="담당자지정" titleId="ap.btn.setPich" href="javascript:setMakVw();" /></td></c:if><c:if
			test="${optConfigMap.recvRecLstPubBx=='Y' and empty param.storId}">
		<td class="frontbtn"><u:buttonS id="regPubBxBtn" alt="공람게시" titleId="ap.btn.regPubBx" href="javascript:regPubBx();" /></td></c:if></u:secu><c:if
			test="${optConfigMap.catEnab == 'Y'}"><u:secu
			auth="A">
		<td class="frontbtn"><u:buttonS id="chngClsBtn" alt="분류변경" titleId="ap.btn.chngCls" href="javascript:chngClsPop();" /></td></u:secu></c:if>
		<td class="frontbtn"><u:buttonS alt="상세정보" titleId="ap.btn.detlInfo" href="javascript:openDetl();" /></td>
		</tr></tbody></table>
	</div>
</div>
<c:if

	test="${optConfigMap.catEnab == 'Y'}"><u:cmt cmt="[옵션] 분류정보 사용" />
<u:boxArea
	className="wbox" noBottomBlank="${true}"
	style="float:left; width:20%;"
	outerStyle="min-height:490px; overflow:hidden;}"
	innerStyle="NO_INNER_IDV">
<iframe id="treeClsInfoFrm" name="treeClsInfoFrm" src="./treeOrgClsFrm.do?menuId=${menuId}&bxId=${param.bxId
	}${not empty param.clsInfoId ? '&clsInfoId='.concat(param.clsInfoId) : ''}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:boxArea>
</c:if>

<div id="listApvBxFrmDiv" style="${optConfigMap.catEnab == 'Y' ? 'float:right; width:79%' : ''}">
<iframe id="listApvBxFrm" name="listApvBxFrm" src="./listApvBxFrm.do?bxId=${param.bxId}&menuId=${menuId
	}${not empty queryString ? '&'.concat(queryString) : ''}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
</c:if><c:if



	test="${bxId == 'pubBx'}"><%//pubBx:공람게시%>
<div class="front">
	<div class="front_right">
		<table border="0" cellpadding="0" cellspacing="0"><tbody><tr>
		<td class="frontbtn"><u:buttonS alt="공람확인" titleId="ap.btn.listPubBxVw" href="javascript:listPubBxVw();" /></td>
		<td class="frontbtn"><u:buttonS alt="상세정보" titleId="ap.btn.detlInfo" href="javascript:openDetl();" /></td>
		</tr></tbody></table>
	</div>
</div>

<u:boxArea
	className="wbox" noBottomBlank="${true}"
	style="float:left; width:20%;"
	outerStyle="height:490px; overflow:hidden;"
	innerStyle="NO_INNER_IDV">
<iframe id="treeRecLstOrgFrm" name="treeRecLstOrgFrm" src="./treeRecLstOrgFrm.do?menuId=${menuId}&bxId=${param.bxId
	}&upward=${sessionScope.userVo.deptId
	}${not empty param.pubBxDeptId ? '&orgId='.concat(param.pubBxDeptId) : ''}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:boxArea>

<div id="listApvBxFrmDiv" style="float:right; width:79%">
<iframe id="listApvBxFrm" name="listApvBxFrm" src="./listApvBxFrm.do?menuId=${menuId}&bxId=${param.bxId
	}${not empty queryString ? '&'.concat(queryString) : ''}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
</c:if><c:if



	test="${bxId == 'myBx'}"><%//myBx:기안함 %>
<div class="front">
	<div class="front_right">
		<table border="0" cellpadding="0" cellspacing="0"><tbody><tr><c:if
			test="${optConfigMap.psnCatEnab == 'Y'}">
		<td class="frontbtn"><u:buttonS alt="개인분류변경" titleId="ap.btn.chngPsnCls" href="javascript:chngPsnCls();" /></td></c:if>
		<td class="frontbtn"><u:buttonS alt="양식선택" titleId="ap.btn.selectForm" href="javascript:selectForm();" /></td>
		<td class="frontbtn"><u:buttonS alt="상세정보" titleId="ap.btn.detlInfo" href="javascript:openDetl();" /></td>
		</tr></tbody></table>
	</div>
</div>
<c:if

	test="${optConfigMap.psnCatEnab == 'Y'}"><u:cmt cmt="개인 분류 사용(기안함)" />

<u:boxArea
	className="wbox" noBottomBlank="${true}"
	style="float:left; width:20%;"
	outerStyle="min-height:490px; overflow:hidden;"
	innerStyle="NO_INNER_IDV">

<u:secu auth="W"><div class="titlearea" style="position:absolute; top:-23px; right:0px">
	<div class="tit_right">
	<ul>
			<li class="ico">
			<a href="javascript:movePsnCls('up')"><img width="20" height="20" title="위로이동" src="/images/blue/ico_wup.png"></a>
			<a href="javascript:movePsnCls('down')"><img width="20" height="20" title="아래로이동" src="/images/blue/ico_wdown.png"></a>
			</li>
	</ul>
	</div>
</div></u:secu>
	
<iframe id="treePsnClsFrm" name="treePsnClsFrm" src="./treePsnClsFrm.do?menuId=${menuId}&bxId=${param.bxId
	}${not empty param.psnClsInfoId ? '&psnClsInfoId='.concat(param.psnClsInfoId) : ''}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<u:secu auth="W"><u:buttonArea style="position:absolute; bottom:-32px; right:0px;" noBottomBlank="${true}">
	<u:button href="javascript:managePsnTree('addCls')" titleId="cm.btn.write" alt="등록" popYn="Y" />
	<u:button href="javascript:managePsnTree('modCls')" titleId="cm.btn.mod" alt="수정" popYn="Y" />
	<u:button href="javascript:managePsnTree('delCls')" titleId="cm.btn.del" alt="삭제" popYn="Y" />
	<u:button href="javascript:managePsnTree('moveCls')" titleId="cm.btn.move" alt="이동" popYn="Y" />
</u:buttonArea></u:secu>

</u:boxArea>
</c:if>

<div id="listApvBxFrmDiv" style="${optConfigMap.psnCatEnab == 'Y' ? 'float:right; width:79%' : ''}">
<iframe id="listApvBxFrm" name="listApvBxFrm" src="./listApvBxFrm.do?bxId=${param.bxId}&menuId=${menuId
	}${not empty queryString ? '&'.concat(queryString) : ''}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
</c:if><c:if



	test="${bxId == 'recvBx'}"><%//recvBx:접수함 %>
<div class="front">
	<div class="front_right">
		<table border="0" cellpadding="0" cellspacing="0"><tbody><tr>
		<u:secu auth="A"
		><td class="frontbtn"><u:buttonS alt="일괄접수" titleId="ap.btn.bulkRecv" href="javascript:${optConfigMap.catEnab=='Y' or optConfigMap.recvRecLstSecuLvl=='Y' ? 'openRecvPop()' : 'processBulkRecv()'};" /></td></u:secu>
		<td class="frontbtn"><u:buttonS alt="상세정보" titleId="ap.btn.detlInfo" href="javascript:openDetl();" /></td>
		</tr></tbody></table>
	</div>
</div>
<c:if

	test="${optConfigMap.exRecvRange == 'Y'}"><u:cmt cmt="접수 범위 확장" />

<u:boxArea
	className="wbox" noBottomBlank="${true}"
	style="float:left; width:20%;"
	outerStyle="min-height:490px; overflow:hidden;"
	innerStyle="NO_INNER_IDV">

<iframe id="treeClsFrm" name="treeClsFrm" src="./treeOrgFrm.do?bxId=${param.bxId}&menuId=${menuId
	}&upward=${sessionScope.userVo.orgId}&brother=Y${empty param.recvDeptId ? '' : '&orgId='.concat(param.recvDeptId)}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

</u:boxArea>
</c:if>

<div id="listApvBxFrmDiv" style="${optConfigMap.exRecvRange == 'Y' ? 'float:right; width:79%' : ''}">
<iframe id="listApvBxFrm" name="listApvBxFrm" src="./listApvBxFrm.do?bxId=${param.bxId}&menuId=${menuId
	}${not empty queryString ? '&'.concat(queryString) : ''}" style="width:100%; height:490px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
</c:if>

<form id="toDocForm" method="post">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="bxId" value="${bxId}" />
<u:input type="hidden" id="queryString" value="${queryString}" />
</form>