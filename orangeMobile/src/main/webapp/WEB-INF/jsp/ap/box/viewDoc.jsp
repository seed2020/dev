<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="
		com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo,
		com.innobiz.orange.web.ap.vo.ApFormBVo"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

// u:set - atMak : 기안중 문서인지(상신할 문서)
// u:set - atWait : 현재 내 검토/결재/합의 를 기다리는 중인지
%><jsp:useBean id="call" class="com.innobiz.orange.web.ap.utils.ELCall" scope="application"
/><u:set
	test="${ call['ApDocUtil.isAtMak'][myTurnApOngdApvLnDVo.apvrRoleCd][myTurnApOngdApvLnDVo.apvStatCd] }"
	var="atMak" value="Y"
/><u:set
	test="${ call['ApDocUtil.isAtWait'][myTurnApOngdApvLnDVo.apvrRoleCd][myTurnApOngdApvLnDVo.apvStatCd] }"
	var="atWait" value="Y"
/><u:set
	test="${ myTurnApOngdApvLnDVo.apvLnPno != '0' or call['ApDocUtil.isAgrOfApvrRole'][myTurnApOngdApvLnDVo.apvrRoleCd] }"
	var="atAgr" value="Y"
/><u:set
	test="${ myApOngdApvLnDVo.apvLnPno != '0' or call['ApDocUtil.isAgrOfApvrRole'][myApOngdApvLnDVo.apvrRoleCd] }"
	var="atAgrStat" value="Y"
/><u:set
	test="${ call['ApDocUtil.isCmplOfDocProsStat'][apvData.docProsStatCd] }"
	var="atCmplDoc" value="Y"
/><u:set


	test="${atMakAgr=='Y' or myTurnApOngdApvLnDVo.apvLnPno!='0'}"
	var="apvLnOpt" value="agr"
/><u:set
	test="${empty apvLnOpt and apFormBVo.formApvLnTypCd=='apvLnDbl'}"
	var="apvLnOpt" value="${empty myTurnApOngdApvLnDVo.dblApvTypCd ? 'reqDept' : myTurnApOngdApvLnDVo.dblApvTypCd}"
	elseValue="${apvLnOpt}"
/><u:set
	test="${empty apvLnOpt and myTurnApOngdApvLnDVo.apvrRoleCd=='makVw'}"
	var="apvLnOpt" value="makVw" elseValue="${apvLnOpt}"
/><u:set
	test="${empty apvLnOpt and (
		myTurnApOngdApvLnDVo.apvrRoleCd=='fstVw'
		or myTurnApOngdApvLnDVo.apvrRoleCd=='pubVw')}"
	var="apvLnOpt" value="vw" elseValue="${apvLnOpt}"
/><u:set
	test="${empty apvLnOpt}"
	var="apvLnOpt" value="apv" elseValue="${apvLnOpt}"
/><u:secu
	auth="A"><u:set test="${true}" var="adminAuth" value="Y"
/></u:secu><u:secu
	auth="W"><u:set test="${true}" var="writeAuth" value="Y"
/></u:secu><c:if
	test="${_uri.startsWith('/ap/box/')}"><u:authUrl
		authCheckUrl="/ap/box/listApvBx.do?bxId=${param.bxId}"
		url="/ap/box/viewDocSub.do?bxId=${param.bxId}" var="refBxUrl"></u:authUrl></c:if><c:if
	test="${_uri.startsWith('/dm/doc/')}"><u:set test="${true}"
		value="/dm/doc/viewApDocSub.do?menuId=${menuId}&bxId=${param.bxId}" var="refBxUrl" /></c:if>
<script type="text/javascript">
//<![CDATA[
var gOptConfig = ${optConfigJson};<%// 결재 옵션 - json %>
var gApvLnMaxCnt = ${apvLnMaxCnt};<%// 결재라인 설정 최대값 - listApvLnFrm.jsp 에서 사용 %>
<%
//기안, 승인, 반려, 찬성, 반대, 재검토 - apvStatCd - mak:기안, apvd:승인, rejt:반려, pros:찬성, cons:반대, reRevw:재검토 
//- paramAtMakAgr : 부서합의 부서대기함에서 - 재검토 요청 할 때만 - true 넘어옴  %>
function openProsStat(apvStatCd, paramAtMakAgr){<%
	// 기안 상태인지 - "Y" or "" %>
	var atMak = "${atMak}";<%
	// 합의 기안 이면 %>
	var atMakAgr = "${atMakAgr=='Y' or myApOngdApvLnDVo.apvrRoleCd == 'makAgr' ? 'Y' : 'N'}";<%
	// 기안,임시저장 등 - 데이터가 설정 되어 전송되어야 하는 경우 %>
	if(atMak=='Y'){<%
		// 문서정보 세팅 체크 %>
		if(!wasDocInfoDone()) return;<%
		// 결재라인 세팅 체크 %>
		if(!wasApvLnDone()) return;<%
	// 합의기안 이면 - 부서대기함에서 상신하거나, 담당자가 상신할때 - 결재경로 체크 해야함 %>
	} else if(atMakAgr=='Y' && apvStatCd != 'reRevw'){<%
		// 결재라인 세팅 체크 %>
		if(!wasApvLnDone()) return;<%
	// 담당 이면 - 접수문서의 기안 %>
	} else if(apvStatCd == 'makVw'){<%
		// 결재라인 세팅 체크 %>
		if(!wasApvLnDone()) return;
	}<%
	// 이중결재 - 처리부서 첫번째 결재자 %>
	var prcDeptFirstApvr = '${prcDeptFirstApvr}';
	//var chkDblApvLn = "${myTurnApOngdApvLnDVo.apvrRoleCd=='prcDept' ? 'Y' : ''}";
	if(apvStatCd != 'reRevw' && prcDeptFirstApvr=='Y'){
		if(!wasDblApvLnDone()) return;
	}<%
	// 양식에 의견 표시칸이 있는지 여부 - Y / N %>
	var opinInDoc = "${opinInDoc}";
	var popTitle = $m.msg.callTerm('ap.term.'+apvStatCd);<%//mak:기안, apvd:승인, pros:찬성, cons:반대
	// 도장 표시 안함 - mak:기안, apvd:승인, pros:찬성, cons:반대 이 아닌 경우 %>
	var noStamp = (apvStatCd!='mak' && apvStatCd!='apvd' && apvStatCd!='pros' && apvStatCd!='cons') ? '&noStamp=Y' : '';<%
	// 재검토 때의 본문에 의견 표시 우선 세팅 %>
	var opDisp = "${opinInDoc=='N' or apvData.docProsStatCd=='pubVw' ? 'N' : (atMakAgr!='Y' and myTurnApOngdApvLnDVo.apvLnPno=='0') or deptAgrFirstApvr=='Y' ? 'Y' : 'N'}";
	if(apvStatCd=='makVw') opDisp = 'N';
	var isRootLn = "${(atMakAgr!='Y' and myTurnApOngdApvLnDVo.apvLnPno=='0') ? 'Y' : 'N'}";<%
	// 루트 라인이 아니면 %>
	if(isRootLn == 'N'){
		var isLast = "${deptAgrLastApvr}";
		var arr = getApvLnData();<%
		// 결재선이 변경 되지 않았으면 - 저장된 결재선에서 자기가 마지막 결재자인지 체크해서(isLast) - 마지막 이면 [본문에 의견 표시]여부 보여줌 %>
		if(arr==null){
			opDisp = (opinInDoc=='Y' && isLast=='Y' && apvStatCd!='reRevw') ? 'Y' : 'N';<%
			// 부서합의에서 다음 결재자가 있으므로 도장 표시 안함 - 마지막 결재자만 도장찍음 %>
			noStamp = (apvStatCd=='reRevw' || isLast!='Y') ? "&noStamp=Y" : "";
		} else {<% // 결재선이 변경되었으면 - 변경된 결재선에서 자기가 마지막인지 체크해서 - 마지막 이면 [본문에 의견 표시]여부 보여줌 %>
			var i, size = arr.length, afterMe = false, hasNext = false;
			for(i=0;i<size;i++){
				if(afterMe){
					if(arr[i]['apvrRoleCd']!='entu' && arr[i]['apvrRoleCd']!='abs'){<%// 위임, 공석 이 아니면 %>
						hasNext = true;
						break;
					}
				} else {
					if(arr[i]['apvrUid']=='${sessionScope.userVo.userUid}'){
						afterMe = true;
					}
				}
			}<%
			// 재검토가 아니고 문서에 의견 표시가 있을 때 %>
			if(apvStatCd!='reRevw' && opinInDoc=='Y'){
				opDisp = hasNext ? 'N' : 'Y';
			}<%
			// 부서합의에서 다음 결재자가 있으므로 도장 표시 안함 - 마지막 결재자만 도장찍음 %>
			noStamp = (apvStatCd=='reRevw' || hasNext) ? "&noStamp=Y" : "";
		}
	}
	$m.dialog.open({
		id:'apvStatPop',
		title:popTitle,
		url:"/ap/box/setDocProsPop.do?menuId=${menuId}&bxId=${param.bxId}&apvStatCd="+apvStatCd+"&opinInDoc="+opDisp+noStamp+(paramAtMakAgr? "&atMakAgr=Y" : "")
	});
}<%
//문서정보 설정 체크 %>
function wasDocInfoDone(){
	var subj = $docDataArea.find("#docInfoArea input[name='docSubj']").val();
	if(subj==null || subj==''){<%
		// ap.msg.needToSetup={0}를(을) 설정해 주십시요. - [문서정보]%>
		$m.msg.alertMsg("ap.msg.needToSetup", ["#ap.btn.docInfo"]);
		//openDocInfo();
		return false;
	}
	return true;}<%
//결재라인이 설정 되었는지 체크 %>
function wasApvLnDone(){
	var $apvLns = $docDataArea.find("#docApvLnArea input[name='apvLn']");
	var roleCd = null;
	if($apvLns.length==1){
		roleCd = JSON.parse($apvLns.val())['apvrRoleCd'];
	}
	if($apvLns.length==0 || ($apvLns.length==1 && roleCd != 'byOne' && roleCd != 'byOneAgr')){<%
		// ap.msg.needToSetup={0}를(을) 설정해 주십시요. - [결재라인] %>
		$m.msg.alertMsg("ap.msg.needToSetup", ["#ap.cmpt.apvLine"]);
		//openApvLn();
		return false;
	}
	return true;
}<%
//이중결재 결재라인이 세팅되었는지 체크 변수 %>
var gDblApvLnDone = ${not empty dblApvLnDone ? 'true' : 'false'};<%
//이중결재 결재라인이 설정 되었는지 체크 - 이중결재의 처리부서가 설정 되었을 때만 체크함 %>
function wasDblApvLnDone(){
	if(!gDblApvLnDone && '${myTurnApOngdApvLnDVo.apvrRoleCd}' != 'apv'){<%
		// ap.msg.needToSetup={0}를(을) 설정해 주십시요. - [결재라인] %>
		$m.msg.alertMsg("ap.msg.needToSetup", ["#ap.cmpt.apvLine"]);
		//openApvLn();
		return false;
	}
	return true;
}<%
//문서에 있는 결재선 정보 리턴 - 히든 테그에서 찾아서 리턴함 %>
function getApvLnData(){
	var arr = [];
	$docDataArea.find("#docApvLnArea input[name='apvLn']").each(function(){
		arr.push(JSON.parse(this.value));
	});
	return arr.length==0 ? null : arr;
}<%
//문서에 의견 정보 리턴 %>
function getOpinHiddenData(){
	var data = {};
	$docDataArea.find("#docOpinArea input").each(function(){
		data[$(this).attr('name')] = this.value;
	});
	return data;
}<%
// 상신자의 결재역할 리턴 %>
function getMakrRoleCd(){
	var $apvLns = $docDataArea.find("#docApvLnArea input[name='apvLn']:first");
	if($apvLns.length==1){
		return JSON.parse($apvLns.val())['apvrRoleCd'];
	}
	return null;
}<%
// 옵션 체크해서 메세지 컴 %>
function checkSubmitMsg(apvStatCd, handler){
	var roleCd = getMakrRoleCd(), execHandler = false;
	gDelayDialogClose = true;
	if(roleCd == 'byOne' || roleCd == 'byOneAgr'){
		if(gOptConfig.msgOnePsn=='Y'){<%
			// ap.cfrm.process={0} 하시겠습니까 ? %>
			$m.msg.confirmMsg("ap.cfrm.process", ["#ap.term.byOne"], handler);
			execHandler = true;
		}
	} else if(apvStatCd == 'reRevw'){
		if(gOptConfig.msgSign=='Y'){<%
			// ap.cfrm.reqProcess={0} 요청 하시겠습니까 ? %>
			$m.msg.confirmMsg("ap.cfrm.reqProcess", ["#ap.term.reRevw"], handler);
			execHandler = true;
		}
	} else {
		if(gOptConfig.msgSign=='Y'){<%
			// ap.cfrm.process={0} 하시겠습니까 ? %>
			$m.msg.confirmMsg("ap.cfrm.process", ["#ap.term."+apvStatCd], handler);
			execHandler = true;
		}
	}
	if(!execHandler && handler){
		gDelayDialogClose = false;
		handler(true);
	}
}<%
// 전송시 - 팝업창 닫기 딜레이 - 무한루프 방지 %>
var gDelayDialogClose = false;<%
// 전송 %>
function submitDoc(statCd){
	var wasTemp = (statCd=='mak' && "${not empty apvData.apvNo and apvData.docStatCd == 'temp' ? 'Y' : ''}" == 'Y');
	if(!gDelayDialogClose) $m.dialog.closeAll();
	var $form = $("#docForm");
	if(wasTemp){
		$form.find("#tempApvNo").val($form.find("#apvNo").val());
		$form.find("#apvNo").val("");
	}
	//if($("#bodyHtmlArea").length != 0) jellyEditor('bodyHtml').prepare();
	$form.find("#statCd").val(statCd==null ? "" : statCd);
	$form.attr("action", "./transDocPost.do?menuId=${menuId}&bxId=${param.bxId}");
	$m.nav.post($form);
	if(wasTemp){
		$form.find("#apvNo").val($form.find("#tempApvNo").val());
		$form.find("#tempApvNo").val("");
	}
	if(gDelayDialogClose) window.setTimeout('$m.dialog.closeAll()',100);
}<%
// 참조문서 열기 %>
function openRefDoc(apvNo){
	var url = '${refBxUrl}&refdBy=${param.apvNo}&apvNo='+apvNo;
	$m.nav.next(null, url);
}<%
// 전표보기 %>
function openViewChit(){
	var url = '${refBxUrl}&apvNo=${param.apvNo}&intgTypCd=ERP_CHIT';
	$m.nav.next(null, url);
}<%
// 파일 다운로드 %>
function downAttchFile(seq, dispNm) {
	var $form = $('<form>', {
			'method':'get','action':'/${apFileModule}/downFile.do','target':$m.browser.mobile && $m.browser.safari ? '' : 'dataframe'
		}).append($('<input>', {
			'name':'bxId','value':'${param.bxId}','type':'hidden'
		})).append($('<input>', {
			'name':'menuId','value':'${menuId}','type':'hidden'
		})).append($('<input>', {
			'name':'apvNo','value':"${vwMode=='trx' ? apvData.trxApvNo : param.apvNo}",'type':'hidden'
		})).append($('<input>', {
			'name':'attHstNo','value':'${not empty myTurnApOngdApvLnDVo.holdAttHstNo ? myTurnApOngdApvLnDVo.holdAttHstNo : apvData.attHstNo}','type':'hidden'
		})).append($('<input>', {
			'name':'attSeq','value':seq,'type':'hidden'
		}));
	
	//if($m.browser.naver || $m.browser.daum){
	//	$form.append($('<input>', {'name':'fwd','value':$form.attr('action'),'type':'hidden'}));
	//	$form.attr('action', '/cm/download/ap/'+encodeURI(dispNm));
	//}
	
	$(top.document.body).append($form);
	$m.secu.set();
	$form.submit();
	$form.remove();
}<%
// 사용자 조회 열기 %>
var viewUserLock = false;
function viewUser(userUid){
	if(viewUserLock) viewUserLock = false;
	else $m.user.viewUserPop(userUid);
}<%
// 취소 옵션

	// 다음 결재자가 읽지 않고 - 진행중인 문서
	if("N".equals(request.getAttribute("readByNextApvr")) && "ongo".equals( ((java.util.Map)request.getAttribute("apvData")).get("docProsStatCd") )){
		
		// 기안 이면 - [기안취소 함수]
		if("0".equals(request.getParameter("apvLnPno")) && "1".equals(request.getParameter("apvLnNo"))){
//기안 회수 %>
function cancelMak(){
	$m.msg.confirmMsg("ap.cfrm.act",["#ap.btn.cancelMak"], function(){
		var result = false;
		$m.ajax("${_cxPth}/ap/box/transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}", {process:"cancelMak", apvNo:"${param.apvNo}"}, function(data){
			if(data.message != null) $m.dialog.alert(data.message);
			result = data.result == 'ok';
		});
		if(result) $m.nav.prev(null, true);
	});
}<%
		}
		// 진행함 이고 - 합의가 아니면 - [승인취소 함수] > 대기함(waitBx) 이동
		if("ongoBx".equals(request.getParameter("bxId")) && !"Y".equals(request.getAttribute("atAgrStat"))){
// 승인 취소 %>
function cancelApvd(){
	$m.msg.confirmMsg("ap.cfrm.act",["#ap.btn.cancelApv"], function(){
		var result = false;
		$m.ajax("${_cxPth}/ap/box/transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}", {process:"cancelApv", apvNo:"${param.apvNo}",
				apvLnPno:"${myApOngdApvLnDVo.apvLnPno}", apvLnNo:"${myApOngdApvLnDVo.apvLnNo}"}, function(data){
			if(data.message != null) $m.dialog.alert(data.message);
			result = data.result == 'ok';
		});
		if(result) $m.nav.prev(null, true);
	});
}<%
		}
		// 진행함 이고 - 합의면 - [합의취소 함수] > 대기함(waitBx) 이동
		if("ongoBx".equals(request.getParameter("bxId")) && "Y".equals(request.getAttribute("atAgrStat"))){
// 합의 취소 %>
function cancelAgr(){
	$m.msg.confirmMsg("ap.cfrm.act",["#ap.btn.cancelAgr"], function(){
		var result = false;
		$m.ajax("${_cxPth}/ap/box/transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}", {process:"cancelAgr", apvNo:"${param.apvNo}",
				apvLnPno:"${myApOngdApvLnDVo.apvLnPno}", apvLnNo:"${myApOngdApvLnDVo.apvLnNo}"}, function(data){
					if(data.message != null) $m.dialog.alert(data.message);
			result = data.result == 'ok';
		});
		if(result) $m.nav.prev(null, true);
	});
}<%
		}
	}
// 이력 조회 %>
function viewDocHis(apvLnPno, apvLnNo, onTab){
	viewUserLock = true;
	$m.nav.next(null, '${_cxPth}/ap/box/viewDocHisSub.do?menuId=${menuId}&bxId=${param.bxId}&apvNo=${param.apvNo}&apvLnPno='+apvLnPno+'&apvLnNo='+apvLnNo+'&onTab='+onTab);
}<%
// 기안자 지정 - 부서합의에서 부서가 지정된 경우 - 해당 합의부서의 기안자를 지정 할 때 %>
function setMakAgr(){
	$m.user.selectOneUser({oneDeptId:'${myTurnApOngdApvLnDVo.apvDeptId}'}, function(userVo){
		if(userVo==null){<%
			// or.msg.noUser=선택된 사용자가 없습니다.%>
			$m.msg.alertMsg('or.msg.noUser');
		} else {<%
			// ap.cfrm.setMakr="{0}"을(를) 기안자로 지정 하시겠습니까 ? %>
			$m.msg.confirmMsg("ap.cfrm.setMakr", [userVo.rescNm], function(result){
				if(result){
					result = false;
					$m.ajax("${_cxPth}/ap/box/transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}", {process:"setMakAgr", apvNo:"${param.apvNo}",
							apvLnPno:"${myTurnApOngdApvLnDVo.apvLnPno=='0' ? myTurnApOngdApvLnDVo.apvLnNo : myTurnApOngdApvLnDVo.apvLnPno}", apvrUid:userVo.userUid}, function(data){
						if(data.message != null) alert(data.message);
						result = data.result == 'ok';
					});
					if(result) $m.nav.toList(true);
				}
			});
		}
		return false;
	});
}<%
// 담당자 지정 - 처리부서를 지정 했을 때 - 해당 처리부서의 첫번째 결재자를 지정 할 때 %>
function setPrcDeptAgr(){
	$m.user.selectOneUser(null, function(userVo){
		if(userVo==null){<%
			// or.msg.noUser=선택된 사용자가 없습니다.%>
			$m.msg.alertMsg('or.msg.noUser');
		} else {<%
			// ap.cfrm.setPich={0}을(를) 담당자로 지정 하시겠습니까 ? %>
			$m.msg.confirmMsg("ap.cfrm.setPich", [userVo.rescNm], function(result){
				if(result){
					result = false;
					$m.ajax("${_cxPth}/ap/box/transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}", {process:"setPrcDeptAgr", apvNo:"${param.apvNo}",
							apvLnPno:"${myTurnApOngdApvLnDVo.apvLnPno}", apvLnNo:"${myTurnApOngdApvLnDVo.apvLnNo}", apvrUid:userVo.userUid}, function(data){
						if(data.message != null) alert(data.message);
						result = data.result == 'ok';
					});
					if(result) $m.nav.toList(true);
				}
			});
		}
		return false;
	});
}<%
// 담당자 지정 - 공람 문서의 담당자를 지정할때 %>
function setMakVw(){
	$m.user.selectOneUser(null, function(userVo){
		if(userVo==null){<%
			// or.msg.noUser=선택된 사용자가 없습니다.%>
			$m.msg.alertMsg('or.msg.noUser');
		} else {<%
			// ap.cfrm.setPich={0}을(를) 담당자로 지정 하시겠습니까 ? %>
			$m.msg.confirmMsg("ap.cfrm.setPich", [userVo.rescNm], function(result){
				if(result){
					result = false;
					$m.ajax("${_cxPth}/ap/box/transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}", {process:"setMakVw", apvNo:"${param.apvNo}", apvrUid:userVo.userUid}, function(data){
						if(data.message != null) alert(data.message);
						result = data.result == 'ok';
					});
					if(result) $m.nav.toList(true);
				}
			});
		}
		return false;
	});
}<%
// 공람완료 %>
function setCmplVw(){<%
	// ap.cfrm.cmplVw=공람완료 하시겠습니까 ? %>
	$m.msg.confirmMsg("ap.cfrm.cmplVw", null, function(result){
		if(result){
			result = false;
			$m.ajax("${_cxPth}/ap/box/transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}", {process:"setCmplVw", apvNo:"${param.apvNo}"}, function(data){
				if(data.message != null) alert(data.message);
				result = data.result == 'ok';
			});
			if(result) $m.nav.toList(true);
		}
	});
}<%
// 경로지정 %>
function openApvLn(){
	var apvLnOpt = "${apvLnOpt}";
	var oldApvLnData = getApvLnData();
	var param = {apvLnOpt:apvLnOpt, apvNo:"${param.apvNo}", apvLnPno:"${atMakAgr=='Y' ? '1' : myTurnApOngdApvLnDVo.apvLnNo}", formApvLnTypCd:"${apFormBVo.formApvLnTypCd}", selected:oldApvLnData};
	if(param['apvLnOpt']=='agr') param['oneDeptId'] = '${myTurnApOngdApvLnDVo.apvDeptId}';
	//if(true) return;
	
	$m.user.selectUsers(param, function(arr){
		if(arr==null || arr.length==0) return true;<%
		
		// hidden data - 변경
		%>
		var $area = $("#docDataArea #docApvLnArea");
		$area.html("");<%
		// 진행문서결재라인상세(AP_ONGD_APV_LN_D) - 결재라인구분코드 %><c:if test="${empty param.apvLnPno or param.apvLnPno == '0'}">
		var apvLnTypCd = arr[0].apvrRoleCd=='byOne' ? 'byOne' : (arr[0].dblApvTypCd=='reqDept' || arr[0].dblApvTypCd=='prcDept') ? 'dblApv' : 'nomlApv';
		$area.append("<input name='apvLnTypCd' type='hidden' value='"+apvLnTypCd+"' />");</c:if>
		arr.each(function(index, data){
			$area.append("<input name='apvLn' type='hidden' value='"+escapeValue(JSON.stringify(data))+"' />");
		});<%
		
		// UI 변경
		%>
		$area = $tabViewArea.children('#apvLn').find('#apvLnUiArea');
		var uiApvrs = [], isAgr = (apvLnOpt=='agr');
		var uiParent = isAgr ? $area.children("[data-subLn='${myTurnApOngdApvLnDVo.apvLnPno}']:first") : $area;
		uiParent.children("[data-cmpl!='Y']").each(function(){ uiApvrs.push(this); });
		
		var i, size = arr.length, apvr=null, role, stat, html, next, uiIndex=0;
		for(i=0;i<size;i++){
			apvr = arr[i];
			role = apvr.apvrRoleCd;
			stat = apvr.apvStatCd;
			<%//apvd:승인, rejt:반려, cons:반대, pros:찬성, cmplVw:공람완료,%>
			if(stat=='apvd'||stat=='rejt'||stat=='cons'||stat=='pros'||stat=='cmplVw'){ 
				continue;
			}
			<%//inApv:결재중, inAgr:합의중, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, inVw:공람중%>
			if(stat=='inApv'||stat=='inAgr'||stat=='hold'||stat=='cncl'||stat=='reRevw'||stat=='inInfm'||stat=='inVw'){
				if(apvr.dblApvTypCd != ''){
					$(uiApvrs[uiIndex]).find('dd.body').text('['+apvr.dblApvTypNm+'] '+apvr.apvrRoleNm);
				} else {
					$(uiApvrs[uiIndex]).find('dd.body').text(apvr.apvrRoleNm);
				}
			} else {
				if(apvr.apvrUid != ''){
					html = [];
					html.push('<div class="'+(isAgr ? 'listdiv_fixed' : 'listdiv')+'" onclick="javascript:viewUser(\''+apvr.apvrUid+'\');" data-apvLnNo="'+apvr.apvLnNo+'" data-apvLnPno="'+apvr.apvLnPno+'" data-cmpl="N" data-befo="Y" data-in="N">');
					if(isAgr){
						html.push('<div class="listcheck_comment"><dl><dd class="comment"></dd></dl></div>');
					}
					html.push('<div class="'+(isAgr ? 'list_comment' : 'list')+'">');
					html.push('<dl>');
					
					html.push('	<dd class="tit">'+escapeHtml(apvr.apvrNm)+' / '+escapeHtml(apvr.apvDeptNm)+(apvr.apvrPositNm=='' ? '' : ' / '+apvr.apvrPositNm)+'</dd>');
					if(apvr.dblApvTypCd != ''){
						html.push('	<dd class="body">'+'['+escapeHtml(apvr.dblApvTypNm)+'] '+escapeHtml(apvr.apvrRoleNm)+(role=='abs' ? ' ['+apvr.absRsonNm+']' : '')+'</dd>');
					} else {
						html.push('	<dd class="body">'+escapeHtml(apvr.apvrRoleNm)+(role=='abs' ? ' ['+apvr.absRsonNm+']' : '')+'</dd>');
					}
					
					html.push('</dl>');
					html.push('</div>');
					html.push('</div>');
					
					if(uiApvrs.length>uiIndex){
						$(html.join('\n')).insertBefore(uiApvrs[uiIndex]);
						next = $(uiApvrs[uiIndex]).next();
						if(next.length!=0 && next.attr('data-subLn')!=null){
							next.remove();
						}
						$(uiApvrs[uiIndex]).remove();
					} else {
						uiParent.append(html.join('\n'));
					}
				} else {
					html = [];
					html.push('<div class="listdiv" onclick=";" data-apvLnNo="'+apvr.apvLnNo+'" data-apvLnPno="'+apvr.apvLnPno+'" data-cmpl="N" data-befo="Y" data-in="N">');
					html.push('<div class="list">');
					html.push('<dl>');
					
					html.push('	<dd class="tit">'+escapeHtml(apvr.apvDeptNm)+' / '+escapeHtml(apvr.apvrRoleNm)+'</dd>');
					
					html.push('</dl>');
					html.push('</div>');
					html.push('</div>');
					
					if(uiApvrs.length>uiIndex){
						$(html.join('\n')).insertBefore(uiApvrs[i]);
						next = $(uiApvrs[i]).next();
						if(next.length!=0 && next.attr('data-subLn')!=null && oldApvLnData.length>i && oldApvLnData.apvDeptId!=apvr.apvDeptId){
							next.remove();
						}
						$(uiApvrs[i]).remove();
					} else {
						uiParent.append(html.join('\n'));
					}
				}
			}
			uiIndex++;
		}
		
		if(uiApvrs.length > uiIndex){
			for(i=uiIndex; i<uiApvrs.length; i++){
				next = $(uiApvrs[i]).next();
				if(next.length!=0 && next.attr('data-subLn')!=null){
					next.remove();
				}
				$(uiApvrs[i]).remove();
			}
		}
		
		if(apvr!=null && apvr.dblApvTypCd != ''){
			gDblApvLnDone = true;
		}
		
		return true;
	});
	/*
	var modified =  $("#docDataArea #docApvLnArea").attr('data-modified');
	var params = [], apvLnPno = "${atMakAgr=='Y' ? param.apvLnNo : empty param.apvLnPno ? '0' : param.apvLnPno}";
	var apvrRoleCd = "${myTurnApOngdApvLnDVo.apvrRoleCd}";
	var apvNo="${param.apvNo}";<%// 결재번호 - 임시저장 등 저장된 경우 %>
	if(apvNo!='') params.push('&apvNo='+apvNo);
	params.push("&apvLnPno="+apvLnPno);
	params.push("&apvLnNo=${atMakAgr=='Y' ? '1' : myTurnApOngdApvLnDVo.apvLnNo}");
	params.push("&formApvLnTypCd=${apFormBVo.formApvLnTypCd}");
	params.push("${not empty myTurnApOngdApvLnDVo.holdApvLnHstNo ? '&apvLnHstNo='.concat(myTurnApOngdApvLnDVo.holdApvLnHstNo) : ''}");
	if(apvrRoleCd=='makAgr' || apvrRoleCd=='deptOrdrdAgr' || apvrRoleCd=='deptParalAgr'){
		params.push("&makrRoleCd=makAgr&apvrRoleCd=${myTurnApOngdApvLnDVo.apvrRoleCd}&oneDeptId=${myTurnApOngdApvLnDVo.apvDeptId}");
	} else if(apvrRoleCd=='makVw' || apvrRoleCd=='fstVw' || apvrRoleCd=='pubVw'){
		params.push("&makrRoleCd=makVw&apvrRoleCd=${myTurnApOngdApvLnDVo.apvrRoleCd}&oneDeptId=${myTurnApOngdApvLnDVo.apvDeptId}");
	} else {
		params.push("&apvrRoleCd=${myTurnApOngdApvLnDVo.apvrRoleCd}");
	}
	params.push("${myTurnApOngdApvLnDVo.dblApvTypCd =='prcDept' ? '&prcDeptYn=Y' : ''}");
	if(modified=='Y') params.push('&modified='+modified);
	dialog.open("setApvLnDialog", '<u:msg titleId="ap.btn.setApvLn" alt="결재선지정" />', "./setApvLnPop.do?menuId=${menuId}&bxId=${param.bxId}"+params.join(''));
	*/
}<%
// 에디터 - $m.openEditor()
// 에디터에서 HTML 조회 %>
function getEditHtml(){
	return $('#bodyHtmlArea').children('div:first').html();
}<%
// 에디터에서 HTML 세팅 %>
function setEditHtml(editHtml){
	$('#bodyHtmlArea').children('div:first').html(editHtml);
}
var $docDataArea = null;
var $tabViewArea = null;
$(document).ready(function() {
	<%// 본문의 넓이를 맞춤 %>
	$layout.adjustBodyHtml('bodyHtmlArea');
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('tabview');
	
	$docDataArea = $("#docDataArea");
	$tabViewArea = $("#tabViewArea");
	
	var notiMsg = '<u:out value="${notiMsg}" type="script" />';
	if(notiMsg != '') $m.dialog.alert(notiMsg);
});<%
// 참조열람 열람확인 %>
function cfrmRefVw(){
	var popTitle = '<u:term termId="ap.term.refVw" alt="참조열람" />';
	$m.dialog.open({
		id:'apvRefVwStatPop',
		title:popTitle,
		url:"/ap/box/setRefVwOpinPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}"
	});
}

<c:if test="${viewYn eq 'Y'}">
<% // 문서뷰어 %>
function viewAttchFile(id) {
	var url = '/${apFileModule}/attachViewSub.do?bxId=${param.bxId}&menuId=${menuId}';
	url+="&apvNo=${vwMode=='trx' ? apvData.trxApvNo : param.apvNo}";
	url+='&attHstNo=${not empty myTurnApOngdApvLnDVo.holdAttHstNo ? myTurnApOngdApvLnDVo.holdAttHstNo : apvData.attHstNo}';
	url+='&attSeq='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>
<%//사용자 팝업 %>
function viewUserPop(uid){
	$m.user.viewUserPop(uid);
}
<%//수동 등록된 날짜 조회 %>
function getDateList(){
	var arr=[];
	$('#xml-body #xml-dates\\/date input[name="erpDate"]').each(function(){
		if($(this).val()!=''){
			arr.push($(this).val());
		}
	});
	if(arr.length==0){
		return null;
	}
	return arr;
}
<%//기간 날짜 팝업 %>
function fromToDatePop(mode){
	var erpStart=$('#xml-body').find('input[name="erpStart"]').val();
	var erpEnd=$('#xml-body').find('input[name="erpEnd"]').val();
	if(erpStart=='' || erpEnd=='') return;
	var url='/cm/date/getSrchDateListPop.do?menuId=${menuId}';
	url+='&start='+erpStart;
	url+='&end='+erpEnd;
	url+='&holiYn=N';
		
	var dateList = getDateList();
	if(dateList!=null) url+='&chkDates='+dateList.join(',');
	if(mode!=undefined) url+='&mode='+mode;	
	$m.dialog.open({
		id:'fromToDateDialog',
		title:'<u:msg titleId="wc.jsp.fromToDate.title" alt="날짜선택"/>',
		url:url
	});
}
function openBodyWinPop(){
	var url = "/ap/box/viewBodyWinPop.do?bxId=${param.bxId}&menuId=${menuId}&apvNo=${param.apvNo}";
	if($m.browser.mobile && $m.browser.safari){
		var a = document.createElement('a');
	    a.setAttribute("href", url);
	    a.setAttribute("target", "_blank");
	    var dispatch = document.createEvent("HTMLEvents");
	    dispatch.initEvent("click", true, true);
	    a.dispatchEvent(dispatch);
	}else{
		window.open(url, 'BODY_HTML');
	}
}
var NO_SCRIPT = true;
var MOBILE_VIEW = true;
//]]>
</script>
<section>

	<div class="btnarea" id="btnArea">
		<div class="blank25"></div>
		<div class="size">
		<dl><c:if
			test="${param.intgTypCd ne 'ERP_CHIT' and apvData.intgTypCd eq 'ERP_CHIT'}">
		<dd class="btn" onclick="openViewChit()"><u:msg titleId="ap.btn.viewChit" alt="전표보기" /></dd></c:if><c:if
		
		
			test="${readByNextApvr == 'N' and apvData.docProsStatCd == 'ongo' and writeAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT'}"><u:cmt
			cmt="다음결재자가 읽지 않고 진행중인 문서 일때 - 기안회수, 승인취소, 합의취소" /><c:if
	
				test="${param.apvLnPno eq '0' and param.apvLnNo eq '1'}">
		<dd class="btn" onclick="cancelMak()"><u:msg titleId="ap.btn.cancelMak" alt="기안회수" /></dd></c:if><c:if
	
				test="${param.bxId eq 'ongoBx' and atAgrStat ne 'Y' and not (param.apvLnPno eq '0' and param.apvLnNo eq '1')}">
		<dd class="btn" onclick="cancelApvd()"><u:msg titleId="ap.btn.cancelApv" alt="승인취소" /></dd></c:if><c:if
	
				test="${param.bxId eq 'ongoBx' and atAgrStat ne 'Y' and not (param.apvLnPno eq '0' and param.apvLnNo eq '1')}">
		<dd class="btn" onclick="cancelAgr()"><u:msg titleId="ap.btn.cancelAgr" alt="합의취소" /></dd></c:if></c:if><c:if
		
		
			test="${param.bxId == 'waitBx' and atMak != 'Y' and atWait == 'Y' and writeAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT'}"><c:if
				test="${ myTurnApOngdApvLnDVo.apvLnPno=='0'
					and (myTurnApOngdApvLnDVo.apvrRoleCd=='revw'
						or myTurnApOngdApvLnDVo.apvrRoleCd=='revw2'
						or myTurnApOngdApvLnDVo.apvrRoleCd=='revw3'
						or myTurnApOngdApvLnDVo.apvrRoleCd=='apv'
						or myTurnApOngdApvLnDVo.apvrRoleCd=='pred')}"><u:cmt
				cmt="기본 라인이고 검토,결재,전결 일때" /><c:if
					test="${prcDeptFirstApvr=='Y'}"><u:cmt
					cmt="처리부서의 첫째 결재자 일때 - 담당자 지정" />
		<dd class="btn" onclick="openApvLn()"><u:msg titleId="ap.btn.setApvLn" alt="결재선지정" /></dd></c:if>
		<dd class="btn" onclick="openProsStat('apvd')"><u:term termId="ap.term.apvd" alt="승인" /></dd>
		<dd class="btn" onclick="openProsStat('rejt')"><u:term termId="ap.term.rejt" alt="반려" /></dd><c:if
			test="${rejtPrevEnab =='Y'}">
		<dd class="btn" onclick="openProsStat('reRevw')"><u:term termId="ap.term.reRevw" alt="재검토" /></dd></c:if><c:if
					test="${prcDeptFirstApvr=='Y'}"><u:cmt
					cmt="처리부서의 첫째 결재자 일때 - 담당자 지정" />
		<dd class="btn" onclick="setPrcDeptAgr()"><u:msg titleId="ap.btn.setPich" alt="담당자지정" /></dd></c:if></c:if><c:if
				test="${ myTurnApOngdApvLnDVo.apvLnPno!='0'
					or (myTurnApOngdApvLnDVo.apvrRoleCd=='psnOrdrdAgr'
					or myTurnApOngdApvLnDVo.apvrRoleCd=='psnParalAgr')}"><u:cmt
				cmt="합의 라인이거나, 개인순차합의, 개인병렬합의 일때" /><c:if
					test="${myTurnApOngdApvLnDVo.apvrRoleCd=='makAgr'}"><u:cmt
					cmt="부서합의의 기안자 일때 - 기안자 지정" />
		<dd class="btn" onclick="openApvLn()"><u:msg titleId="ap.btn.setApvLn" alt="결재선지정" /></dd></c:if>
		<dd class="btn" onclick="openProsStat('pros')"><u:term termId="ap.term.pros" alt="합의승인" /></dd><c:if
			test="${optConfigMap.noConsAtAgr ne 'Y'}">
		<dd class="btn" onclick="openProsStat('cons')"><u:term termId="ap.term.cons" alt="합의반대" /></dd></c:if><c:if
			test="${optConfigMap.rejtWhenPsnOrdrdAgr=='Y' and myTurnApOngdApvLnDVo.apvrRoleCd=='psnOrdrdAgr'}">
		<dd class="btn" onclick="openProsStat('rejt')"><u:term termId="ap.term.rejt" alt="반려" /></dd></c:if><c:if
			test="${rejtPrevEnab =='Y'}">
		<dd class="btn" onclick="openProsStat('reRevw')"><u:term termId="ap.term.reRevw" alt="재검토" /></dd></c:if><c:if
					test="${myTurnApOngdApvLnDVo.apvrRoleCd=='makAgr'}"><u:cmt
					cmt="부서합의의 기안자 일때 - 기안자 지정" />
		<dd class="btn" onclick="setMakAgr()"><u:msg titleId="ap.btn.setMakr" alt="기안자지정" /></dd></c:if></c:if><c:if
	
		test="${myTurnApOngdApvLnDVo.apvrRoleCd=='makVw'}">
		
		<dd class="btn" onclick="openApvLn()"><u:msg titleId="ap.btn.setApvLn" alt="결재선지정" /></dd>
		<dd class="btn" onclick="openProsStat('makVw')"><u:msg titleId="ap.btn.subm" alt="상신" /></dd>
		<dd class="btn" onclick="setCmplVw()"><u:msg titleId="ap.btn.cmplVw" alt="공람완료" /></dd>
		<dd class="btn" onclick="setMakVw()"><u:msg titleId="ap.btn.setPich" alt="담당자지정" /></dd></c:if><c:if
	
		test="${myTurnApOngdApvLnDVo.apvrRoleCd=='fstVw'}">
		<dd class="btn" onclick="openProsStat('fstVw')"><u:msg titleId="ap.term.fstVw" alt="선람" /></dd></c:if><c:if
	
		test="${myTurnApOngdApvLnDVo.apvrRoleCd=='pubVw' or myTurnApOngdApvLnDVo.apvrRoleCd=='paralPubVw'}">
		<dd class="btn" onclick="openProsStat('pubVw')"><u:msg titleId="ap.term.pubVw" alt="공람" /></dd></c:if></c:if><c:if
		
			test="${param.bxId == 'ongoBx' or param.bxId == 'apvdBx'}"></c:if><c:if
		
			test="${param.bxId == 'myBx'}"></c:if><c:if
		
			test="${param.bxId == 'rejtBx'}"><!-- 삭제 --></c:if><c:if
		
			test="${param.bxId == 'postApvdBx'}"></c:if><c:if
		
			test="${param.bxId == 'drftBx'}"><!-- 상신, 삭제 --></c:if><c:if
		
			test="${param.bxId == 'deptBx' and atWait == 'Y' and adminAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT'}"><c:if
				test="${myTurnApOngdApvLnDVo.apvrRoleCd=='deptOrdrdAgr' or myTurnApOngdApvLnDVo.apvrRoleCd=='deptParalAgr'}"><u:cmt
				cmt="부서순차합의, 부서병렬합의 일때 - 기안자 지정" />
		<dd class="btn" onclick="setMakAgr()"><u:msg titleId="ap.btn.setMakr" alt="기안자지정" /></dd></c:if><c:if
				test="${myTurnApOngdApvLnDVo.apvrRoleCd=='prcDept'}"><u:cmt
				cmt="처리부서 일때 - 담당자 지정" />
		<dd class="btn" onclick="setPrcDeptAgr()"><u:msg titleId="ap.btn.setPich" alt="담당자지정" /></dd></c:if></c:if><c:if
	
		test="${param.bxId eq 'refVwBx' and apOngdRefVwDVo.refVwStatCd eq 'inRefVw' and param.intgTypCd ne 'ERP_CHIT'}">
		<dd class="btn" onclick="cfrmRefVw()"><u:msg titleId="ap.term.cfrmRefVw" alt="열람확인" /></dd></c:if>
		<dd class="close" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
		</dl>
		</div>
	</div>
	
	<div class="titlezone">
		<div class="titarea">
			<dl>
			<dd class="tit"><u:out value="${apvData.docSubj}" /></dd>
			<dd class="name"><u:out value="${apvData.makDeptNm}"
				/>ㅣ<u:out value="${apvData.makrNm}"
				/>ㅣ<u:out value="${apvData.makDt}" type="date"
				/>ㅣ<c:if
				test="${param.bxId == 'waitBx' and myTurnApOngdApvLnDVo.apvStatCd == 'hold'}"><u:msg
					titleId="ap.btn.hold" alt="보류" /></c:if><c:if
				test="${param.bxId == 'waitBx' and myTurnApOngdApvLnDVo.apvStatCd == 'cncl'}"><c:if
					test="${apvData.apvrRoleCd == 'psnOrdrdAgr'
						or apvData.apvrRoleCd == 'psnParalAgr'
						or apvData.apvLnPno != '0'}"><u:msg
							titleId="ap.btn.cancelAgr" alt="합의취소" /></c:if><c:if
					test="${not (apvData.apvrRoleCd == 'psnOrdrdAgr'
						or apvData.apvrRoleCd == 'psnParalAgr'
						or apvData.apvLnPno != '0')}"><u:msg
							titleId="ap.btn.cancelApv" alt="승인취소" /></c:if></c:if><c:if
				test="${param.bxId != 'waitBx' or not (myTurnApOngdApvLnDVo.apvStatCd == 'hold' or myTurnApOngdApvLnDVo.apvStatCd == 'cncl')}"><u:out
					value="${apvData.docStatNm}" type="html" /></c:if></dd>
			</dl>
		</div>
	</div>
	<c:if test="${optConfigMap.viewMobileBody eq 'Y'}">
	<div class="btnarea" style="position:relative; height:0px; margin-top:-1px;">
		<div class="size">
			<dl>
			<dd class="btn" onclick="openBodyWinPop()"><u:msg titleId="ap.btn.viewBody" alt="본문보기" /></dd>
			</dl>
		</div>
	</div>
	</c:if>
	<div class="tabarea" id="tabBtnArea">
		<div class="tabsize"><c:if
				
				test="${isHis != 'Y' and isChit != 'Y'}">
		<dl>
			<dd class="tab_on" onclick="$layout.tab.on($(this).attr('id'))" id="body"><u:msg titleId="ap.btn.body" alt="본문" /></dd>
			<dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="detl"><u:msg titleId="map.btn.detl" alt="상세" /></dd>
			<dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="apvLn"<c:if test="${not empty showOpinAtBtn}"> style="color:#B03C3C; font-weight:bold;"</c:if>><u:msg titleId="map.btn.apvLn" alt="결재경로" /></dd><c:if
				test="${fn:length(apOngdRefVwDVoList) > 0}">
			<dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="refVw"><u:term termId="ap.term.refVw" alt="참조열람" /></dd></c:if><c:if
				test="${fn:length(apOngdAttFileLVoList) > 0}">
			<dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="attch"><u:msg titleId="map.btn.attch" alt="첨부" /></dd></c:if><c:if
				test="${fn:length(refApOngdBVoList) > 0 and not (apvData.recLstTypCd=='recvRecLst' or apvData.recLstTypCd=='distRecLst')}">
			<dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="ref"><u:msg titleId="map.btn.ref" alt="참조" /></dd></c:if><c:if
				test="${fn:length(apOngdRecvDeptLVoList) > 0 and not (apvData.recLstTypCd=='recvRecLst' or apvData.recLstTypCd=='distRecLst')}">
			<dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="recv"><u:msg titleId="map.btn.recv" alt="수신처" /></dd></c:if><c:if
				test="${apvData.recLstTypCd == 'regRecLst'
					and (param.bxId == 'myBx' or param.bxId == 'sendBx' or param.bxId == 'regRecLst')
					and (not empty reqCensrApOngdPichDVo or not empty censrApOngdPichDVo) }">
			<dd class="tab" onclick="$layout.tab.on($(this).attr('id'))" id="censr"><u:msg titleId="ap.cfg.censr" alt="심사" /></dd></c:if>
		</dl></c:if><c:if
				
				test="${isHis == 'Y'}">
		<dl><c:if test="${not empty hisApOngdBodyLVo or param.onTab=='body'}">
			<dd class="tab${param.onTab=='body' ? '_on' : ''}" onclick="$layout.tab.on($(this).attr('id'))" id="body"><u:msg titleId="map.btn.bodyHis" alt="본문 이력" /></dd></c:if><c:if
				test="${fn:length(hisApOngdApvLnDVoList) > 0 or param.onTab=='apvLn'}">
			<dd class="tab${param.onTab=='apvLn' ? '_on' : ''}" onclick="$layout.tab.on($(this).attr('id'))" id="apvLn"><u:msg titleId="map.btn.apvLnHis" alt="결재선 이력" /></dd></c:if><c:if
				test="${not empty hisApOngdAttFileLVoList or param.onTab=='attch'}">
			<dd class="tab${param.onTab=='attch' ? '_on' : ''}" onclick="$layout.tab.on($(this).attr('id'))" id="attch"><u:msg titleId="map.btn.attchHis" alt="첨부 이력" /></dd></c:if>
		</dl></c:if><c:if
				
				test="${isChit == 'Y'}">
		<dl>
			<dd class="tab_on" onclick="$layout.tab.on($(this).attr('id'))" id="body"><u:msg titleId="ap.tab.chitDetl" alt="전표내역" /></dd>
		</dl></c:if>
		</div>
		<div class="tab_icol" style="display:none" id="toLeft"></div>
		<div class="tab_icor" style="display:none" id="toRight"></div>
	</div>
	
	<div id="tabViewArea"><c:if
				
				
				test="${isHis != 'Y' and isChit != 'Y'}">
	
		<div class="bodyzone_scroll" id="body"><!-- 본문 -->
		<div class="bodyarea">
			<dl>
			<dd class="bodytxt_scroll">
				<div class="scroll editor" id="bodyHtmlArea"><div id="zoom"><u:out value="${apvData.bodyHtml}" type="${empty scriptBodyHtml ? 'noscript' : ''}" /></div></div>
			</dd>
			</dl>
		</div>
		</div>
		
		<div class="s_tablearea" id="detl" style="display:none;"><!-- 상세 -->
			<div class="blank30"></div>
			<table class="s_table">
			<caption style="display:none"><u:msg titleId="map.btn.detl" alt="상세" /></caption>
			<colgroup>
				<col width="33%"/>
				<col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th class="shead_lt"><u:msg titleId="cols.docNo" alt="문서번호" /></th>
					<td class="sbody_lt"><u:out value="${apvData.docNo}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="cols.subj" alt="제목" /></th>
					<td class="sbody_lt"><u:out value="${apvData.docSubj}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="cols.formNm" alt="양식명" /></th>
					<td class="sbody_lt"><u:out value="${apvData.formNm}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.doc.makrNm" alt="기안자" /></th>
					<td class="sbody_lt"><c:if test="${not empty apvData.makrNm}"
	><a href="javascript:$m.user.viewUserPop('${apvData.makrUid}');"><u:out value="${apvData.makrNm}" /></a></c:if></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.doc.makDt" alt="기안일시" /></th>
					<td class="sbody_lt"><u:out value="${apvData.makDt}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.doc.docKeepPrdNm" alt="보존기간" /></th>
					<td class="sbody_lt"><u:out value="${apvData.docKeepPrdNm}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="cols.ugntDoc" alt="긴급문서" /></th>
					<td class="sbody_lt"><c:if
	test="${apvData.ugntDocYn == 'Y'}" ><u:msg titleId="ap.option.ugnt" alt="긴급" langTypCd="${apvData.docLangTypCd}" /></c:if><c:if
	test="${apvData.ugntDocYn != 'Y'}" ><u:msg titleId="ap.option.mdrt" alt="보통" langTypCd="${apvData.docLangTypCd}" /></c:if></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="cols.secu" alt="보안" /></th>
					<td class="sbody_lt"><c:if
	test="${not empty apvData.seculNm}" ><u:out value="${apvData.seculNm}" nullValue="" /> </c:if><c:if
	test="${not empty apvData.docPwEnc}" >[<u:msg titleId="cols.pw" alt="비밀번호" langTypCd="${apvData.docLangTypCd}" />]</c:if></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="cols.readScop" alt="열람범위" /></th>
					<td class="sbody_lt"><c:if
	test="${apvData.allReadYn == 'Y'}" ><u:msg titleId="ap.option.all" alt="전체" langTypCd="${apvData.docLangTypCd}" /></c:if><c:if
	test="${apvData.allReadYn != 'Y'}" ><u:msg titleId="ap.option.dept" alt="부서" langTypCd="${apvData.docLangTypCd}" /></c:if></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="cols.rgstReg" alt="대장등록" /></th>
					<td class="sbody_lt"><c:if
	test="${apvData.regRecLstRegYn == 'Y'}" ><u:msg titleId="ap.option.regY" alt="등록" langTypCd="${apvData.docLangTypCd}" /></c:if><c:if
	test="${apvData.regRecLstRegYn != 'Y'}" ><u:msg titleId="ap.option.regN" alt="미등록" langTypCd="${apvData.docLangTypCd}"
	/><c:if test="${not empty apvData.regRecLstRegSkedYmd}"> (<u:msg titleId="ap.jsp.skedDt" alt="예약 등록일" /> : ${apvData.regRecLstRegSkedYmd})</c:if></c:if></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="cols.docTyp" alt="문서구분" /></th>
					<td class="sbody_lt"><u:out value="${apvData.docTypNm}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="cols.enfcScop" alt="시행범위" /></th>
					<td class="sbody_lt"><u:out value="${apvData.enfcScopNm}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="cols.clsInfo" alt="분류정보" /></th>
					<td class="sbody_lt"><u:out value="${apvData.clsInfoNm}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="cols.docLang" alt="문서언어" /></th>
					<td class="sbody_lt"><u:out value="${apvData.docLangTypNm}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.list.cmplrNm" alt="완결자" /></th>
					<td class="sbody_lt"><c:if test="${not empty apvData.cmplrNm}"
	><a href="javascript:$m.user.viewUserPop('${apvData.cmplrUid}');"><u:out value="${apvData.cmplrNm}" /></a></c:if></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.list.cmplDt" alt="완결일시" /></th>
					<td class="sbody_lt"><u:out value="${apvData.cmplDt}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.doc.enfcDt" alt="시행일시" /></th>
					<td class="sbody_lt"><u:out value="${apvData.enfcDt}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.doc.enfcDocKeepPrdNm" alt="시행문보존기간" /></th>
					<td class="sbody_lt"><u:out value="${apvData.enfcDocKeepPrdNm}" /></td>
				</tr><c:if
	test="${apvData.recLstTypCd == 'distRecLst'}">
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.doc.distNo" alt="배부번호" /></th>
					<td class="sbody_lt"><u:out value="${apvData.recvDocNo}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.list.distDt" alt="배부일시" /></th>
					<td class="sbody_lt"><u:out value="${apvData.recvDt}" /></td>
				</tr></c:if><c:if
	test="${apvData.recLstTypCd == 'recvRecLst'}">
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.doc.recvNo" alt="접수번호" /></th>
					<td class="sbody_lt"><u:out value="${apvData.recvDocNo}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.list.recvDt" alt="접수일시" /></th>
					<td class="sbody_lt"><u:out value="${apvData.recvDt}" /></td>
				</tr></c:if>
			
			</tbody>
			</table>
		</div>
		
		<div class="listarea" id="apvLn" style="display:none;"><!-- 결재선 -->
		<div class="blank30"></div>
		<article id="apvLnUiArea"><c:forEach
		items="${mobileApOngdApvLnDVoList}" var="apOngdApvLnDVo"><c:if
		
		
			test="${apOngdApvLnDVo.apvrRoleCd=='deptOrdrdAgr'
				or apOngdApvLnDVo.apvrRoleCd=='deptParalAgr'
				or apOngdApvLnDVo.apvrRoleCd=='deptInfm'
				or apOngdApvLnDVo.apvrRoleCd=='prcDept'}">
			<div class="${myTurnApOngdApvLnDVo.apvLnNo == apOngdApvLnDVo.apvLnNo and myTurnApOngdApvLnDVo.apvLnPno == apOngdApvLnDVo.apvLnPno
				? 'listdivline' : 'listdiv'}" onclick=";" data-apvLnNo="${apOngdApvLnDVo.apvLnNo
					}" data-apvLnPno="${apOngdApvLnDVo.apvLnPno}" data-cmpl="${
					(	apOngdApvLnDVo.apvStatCd == 'apvd'
					or	apOngdApvLnDVo.apvStatCd == 'rejt'
					or	apOngdApvLnDVo.apvStatCd == 'cons'
					or	apOngdApvLnDVo.apvStatCd == 'pros'
					or	apOngdApvLnDVo.apvStatCd == 'cmplVw') ? 'Y' : 'N'}" data-befo="${
					(	apOngdApvLnDVo.apvStatCd == 'befoApv'
					or	apOngdApvLnDVo.apvStatCd == 'befoAgr'
					or	apOngdApvLnDVo.apvStatCd == 'befoVw') ? 'Y' : 'N'}" data-in="${
					(	apOngdApvLnDVo.apvStatCd == 'inApv'
					or	apOngdApvLnDVo.apvStatCd == 'inAgr'
					or	apOngdApvLnDVo.apvStatCd == 'hold'
					or	apOngdApvLnDVo.apvStatCd == 'reRevw'
					or	apOngdApvLnDVo.apvStatCd == 'inInfm'
					or	apOngdApvLnDVo.apvStatCd == 'inVw') ? 'Y' : 'N'}"><!-- 부서합의 -->
			<div class="list">
			<dl>
				<dd class="tit"><u:out value="${apOngdApvLnDVo.apvDeptNm}" /> / <u:term termId="ap.term.${apOngdApvLnDVo.apvrRoleCd}" /></dd>
			</dl>
			</div>
			</div><u:convert srcId="sub${apOngdApvLnDVo.apvLnNo}ApOngdApvLnDVoList" var="subLine"
			/><c:if test="${not empty subLine}"><u:set test="${true}" var="isSub" value="Y" />
			<div data-subLn="${apOngdApvLnDVo.apvLnNo}"><c:forEach
		items="${subLine}" var="apOngdApvLnDVo">
<%@ include file="viewDocApvLineInc.jsp" %>
			</c:forEach></div><u:set test="${true}" var="isSub" value="N" /></c:if></c:if><c:if
		
		
			test="${not(apOngdApvLnDVo.apvrRoleCd=='deptOrdrdAgr'
					or apOngdApvLnDVo.apvrRoleCd=='deptParalAgr'
					or apOngdApvLnDVo.apvrRoleCd=='deptInfm'
					or apOngdApvLnDVo.apvrRoleCd=='prcDept')
				and not empty apOngdApvLnDVo.apvrRoleCd}"><u:set test="${true}" var="isSub" value="N" />
<%@ include file="viewDocApvLineInc.jsp" %>
			</c:if></c:forEach>
		</article>
		</div><c:if
		
		
			test="${fn:length(apOngdRefVwDVoList) >0}">
		<div class="listarea" id="refVw" style="display:none;"><!-- 참조열람 -->
		<div class="blank30"></div>
		<article id="refVwUiArea"><c:forEach
		items="${apOngdRefVwDVoList}" var="apOngdRefVwDVo">
			<div class="listdiv" onclick="javascript:viewUser('${apOngdRefVwDVo.refVwrUid}');">
			<div class="list">
			<dl>
				<dd class="tit"><u:out value="${apOngdRefVwDVo.refVwrNm}" 
					/> / <u:out value="${apOngdRefVwDVo.refVwrDeptNm}" 
					/> / <u:out value="${apOngdRefVwDVo.refVwrPositNm}" /></dd>
				<dd class="body"><c:if
					test="${empty apOngdRefVwDVo.refVwDt}"><u:msg titleId="ap.jsp.notRead" alt="미열람" /></c:if><c:if
					test="${not empty apOngdRefVwDVo.refVwDt}">${apOngdRefVwDVo.refVwDt}</c:if></dd><c:if
					test="${not empty apOngdRefVwDVo.refVwOpinCont}">
				<dd class="reply"><div class="replyico"></div><div class="replyin"><u:out value="${apOngdRefVwDVo.refVwOpinCont}" /></div></dd></c:if>
			</dl>
			</div>
			</div></c:forEach>
		</article>
		</div></c:if><c:if
		
		
			test="${fn:length(apOngdAttFileLVoList) >0}">
		<div class="attachzone" id="attch" style="display:none;"><!-- 첨부파일 -->
			<div class="blank30"></div>
			<div class="attacharea"><c:forEach
				items="${apOngdAttFileLVoList}" var="apOngdAttFileLVo" ><u:out
					value="${apOngdAttFileLVo.attDispNm}" type="value" var="dispNm"/>
				<m:attach downFnc="downAttchFile('${apOngdAttFileLVo.attSeq}','${dispNm}');"
					fileKb="${apOngdAttFileLVo.fileKb}"
					fileName="${apOngdAttFileLVo.attDispNm}" viewFnc="viewAttchFile('${apOngdAttFileLVo.attSeq}');"/></c:forEach>
			</div>
		</div></c:if><c:if
		
		
			test="${fn:length(refApOngdBVoList) > 0 and not (apvData.recLstTypCd=='recvRecLst' or apvData.recLstTypCd=='distRecLst')}">
		<div class="s_tablearea" id="ref" style="display:none"><!-- 참조문서 -->
			<div class="blank30"></div>
			<table class="s_table">
			<caption style="display:none"><u:msg titleId="map.btn.ref" alt="참조" /></caption>
			<colgroup>
				<col width="66%"/>
				<col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th class="shead_ct"><u:msg titleId="cols.subj" alt="제목" /></th>
					<th class="shead_ct"><u:msg titleId="ap.doc.makrNm" alt="기안자" /></th>
				</tr><c:forEach
					items="${refApOngdBVoList}" var="refApOngdBVo" varStatus="status">
				<tr>
					<td class="sbody_lt"><c:if
		test="${empty param.refdBy and empty isDM}"><a href="javascript:openRefDoc('${refApOngdBVo.apvNo}')"><u:out
			value="${refApOngdBVo.docSubj}" /></a></c:if><c:if
		test="${not (empty param.refdBy and empty isDM)}"><u:out
			value="${refApOngdBVo.docSubj}" /></c:if></td>
					<td class="sbody_ct"><a href="javascript:$m.user.viewUserPop('${refApOngdBVo.makrUid}')"><u:out
			value="${refApOngdBVo.makrNm}" type="html" /></a></td>
				</tr></c:forEach>
			</tbody>
			</table>
		</div>
		</c:if><c:if
		
		
			test="${fn:length(apOngdRecvDeptLVoList) > 0 and not (apvData.recLstTypCd=='recvRecLst' or apvData.recLstTypCd=='distRecLst')}">
		<div class="s_tablearea" id="recv" style="display:none"><!-- 수신처 -->
			<div class="blank30"></div>
			<table class="s_table">
			<caption style="display:none"><u:msg titleId="map.btn.recv" alt="수신처" /></caption>
			<colgroup>
				<col width="66%"/>
				<col width=""/>
			</colgroup>
               <thead>
				<tr>
					<th class="shead_ct"><u:msg titleId="cols.recvDept" alt="수신처" /> (<u:msg titleId="cols.refDept" alt="참조처" />)</th>
					<th class="shead_ct"><u:msg titleId="ap.jsp.recvDept.recvTyp" alt="수신구분" /></th>
				</tr>
			</thead>
			<tbody><c:forEach
					items="${apOngdRecvDeptLVoList}" var="apOngdRecvDeptLVo">
				<tr>
					<td class="sbody_lt"><u:out value="${apOngdRecvDeptLVo.recvDeptNm}"
						/><c:if test="${not empty apOngdRecvDeptLVo.refDeptNm}"
						> (<u:out value="${apOngdRecvDeptLVo.refDeptNm}" />)</c:if></td>
					<td class="sbody_ct"><u:out value="${apOngdRecvDeptLVo.recvDeptTypNm}" /></td>
				</tr></c:forEach>
			</tbody>
			</table>
		</div>
		</c:if><c:if
	
	
		test="${apvData.recLstTypCd == 'regRecLst'
			and (param.bxId == 'myBx' or param.bxId == 'sendBx' or param.bxId == 'regRecLst')
			and (not empty reqCensrApOngdPichDVo or not empty censrApOngdPichDVo) }">
		<div class="s_tablearea" id="censr" style="display:none"><!-- 심사 -->
			<div class="blank30"></div>
			<table class="s_table">
			<caption style="display:none"><u:msg titleId="ap.cfg.censr" alt="심사" /></caption>
			<colgroup>
				<col width="33%"/>
				<col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.jsp.reqCensrUser" alt="심사요청자" /></th>
					<td class="sbody_lt"><a href="javascript:$m.user.viewUserPop('${reqCensrApOngdPichDVo.pichUid}');"><u:out value="${reqCensrApOngdPichDVo.pichNm}" /></a></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.jsp.reqCensrDt" alt="심사요청일시" /></th>
					<td class="sbody_lt"><u:out value="${reqCensrApOngdPichDVo.hdlDt}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.btn.reqCensrOpin" alt="심사요청의견" /></th>
					<td class="sbody_lt"><u:out value="${reqCensrApOngdPichDVo.pichOpinCont}" /></td>
				</tr>
			</tbody>
			</table><c:if test="${not empty censrApOngdPichDVo}">
			<div class="blank10"></div>
			<table class="s_table">
			<caption style="display:none"><u:msg titleId="ap.cfg.censr" alt="심사" /></caption>
			<colgroup>
				<col width="33%"/>
				<col width=""/>
			</colgroup>
			<tbody>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.jsp.censrUser" alt="심사자" /></th>
					<td class="sbody_lt"><a href="javascript:viewUserPop('${censrApOngdPichDVo.pichUid}');"><u:out value="${censrApOngdPichDVo.pichNm}" /></a></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.jsp.censrDt" alt="심사일시" /></th>
					<td class="sbody_lt"><u:out value="${censrApOngdPichDVo.hdlDt}" /></td>
				</tr>
				<tr>
					<th class="shead_lt"><u:msg titleId="ap.btn.censrOpin" alt="심사의견" /></th>
					<td class="sbody_lt"><u:out value="${censrApOngdPichDVo.pichOpinCont}" /></td>
				</tr>
			</tbody>
			</table></c:if>
		</div></c:if></c:if><c:if
				
				
				test="${isHis == 'Y'}"><c:if
				
					test="${not empty hisApOngdBodyLVo or param.onTab=='body'}">
	
		<div class="bodyzone_scroll" id="body" style="${param.onTab=='body' ? '' : 'display:none;'}"><!-- 본문 이력 -->
		<div class="bodyarea">
			<dl>
			<dd class="bodytxt_scroll">
				<div class="scroll" id="bodyHtmlArea"><div><u:out value="${hisApOngdBodyLVo.bodyHtml}" type="noscript" /></div></div>
			</dd>
			</dl>
		</div>
		</div></c:if><c:if
				
					test="${fn:length(hisApOngdApvLnDVoList) >0 or param.onTab=='apvLn'}">
		<div class="listarea" id="apvLn" style="${param.onTab=='apvLn' ? '' : 'display:none;'}"><!-- 결재선 이력 -->
		<div class="blank30"></div>
		<article><c:forEach
		items="${hisApOngdApvLnDVoList}" var="apOngdApvLnDVo"><c:if
		
		
			test="${apOngdApvLnDVo.apvrRoleCd=='deptOrdrdAgr'
				or apOngdApvLnDVo.apvrRoleCd=='deptParalAgr'
				or apOngdApvLnDVo.apvrRoleCd=='deptInfm'
				or apOngdApvLnDVo.apvrRoleCd=='prcDept'}">
			<div class="listdiv" onclick="javascript:;"><!-- 부서합의 -->
			<div class="list">
			<dl>
				<dd class="tit"><u:out value="${apOngdApvLnDVo.apvDeptNm}" /> / <u:term termId="ap.term.${apOngdApvLnDVo.apvrRoleCd}" /></dd>
			</dl>
			</div>
			</div><u:convert srcId="sub${apOngdApvLnDVo.apvLnNo}ApOngdApvLnDVoList" var="subLine"
			/><c:if test="${not empty subLine}"><u:set test="${true}" var="isSub" value="Y" />
			<div><c:forEach
		items="${subLine}" var="apOngdApvLnDVo">
<%@ include file="viewDocApvLineInc.jsp" %>
			</c:forEach></div><u:set test="${true}" var="isSub" value="N" /></c:if></c:if><c:if
		
		
			test="${not(apOngdApvLnDVo.apvrRoleCd=='deptOrdrdAgr'
					or apOngdApvLnDVo.apvrRoleCd=='deptParalAgr'
					or apOngdApvLnDVo.apvrRoleCd=='deptInfm'
					or apOngdApvLnDVo.apvrRoleCd=='prcDept')
				and not empty apOngdApvLnDVo.apvrRoleCd}"><u:set test="${true}" var="isSub" value="N" />
<%@ include file="viewDocApvLineInc.jsp" %>
			</c:if></c:forEach>
		</article>
		</div></c:if><c:if
				
					test="${not empty hisApOngdAttFileLVoList or param.onTab=='attch'}"><c:if
					
					test="${fn:length(hisApOngdAttFileLVoList) == 0}">
		<div class="listarea" id="attch" style="${param.onTab=='attch' ? '' : 'display:none;'}">
			<div class="blank30"></div>
			<article>
			<div class="listdiv_nodata">
			<dl>
			<dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
			</dl>
			</div>
			</article>
		</div></c:if><c:if
					
					test="${fn:length(hisApOngdAttFileLVoList) > 0}">
		<div class="attachzone" id="attch" style="${param.onTab=='attch' ? '' : 'display:none;'}"><!-- 첨부파일 이력 -->
			<div class="blank30"></div>
			<div class="attacharea"><c:forEach
				items="${hisApOngdAttFileLVoList}" var="apOngdAttFileLVo" ><u:out
					value="${apOngdAttFileLVo.attDispNm}" type="value" var="dispNm"/>
				<m:attach downFnc="downAttchFile('${apOngdAttFileLVo.attSeq}','${dispNm}');"
					fileKb="${apOngdAttFileLVo.fileKb}"
					fileName="${apOngdAttFileLVo.attDispNm}" viewFnc="viewAttchFile('${apOngdAttFileLVo.attSeq}');"/></c:forEach>
			</div>
		</div></c:if></c:if>
		</c:if><c:if
				
				
				test="${isChit == 'Y'}">
	
		<div class="bodyzone_scroll" id="body"><!-- 본문 -->
		<div class="bodyarea">
			<dl>
			<dd class="bodytxt_scroll">
				<div class="scroll editor" id="bodyHtmlArea"><div id="zoom"><u:out value="${apvData.bodyHtml}" type="noscript" /></div></div>
			</dd>
			</dl>
		</div>
		</div>
		</c:if>
	</div>
	
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>
<c:if
	
	test="${false}">
<form id="docForm" method="post" enctype="multipart/form-data" style="display:none">
<input type="hidden" name="bxId" value="${param.bxId}" />
<input id="apvNo" type="hidden" value="${param.apvNo}" />
<input id="tempApvNo" type="hidden" value="" /><c:if
	test="${not empty myApOngdApvLnDVo.apvLnPno}">
<input id="apvLnPno" type="hidden" value="${atMakAgr=='Y' ? myTurnApOngdApvLnDVo.apvLnNo : myApOngdApvLnDVo.apvLnPno}" /></c:if><c:if
	test="${not empty myTurnApOngdApvLnDVo.apvLnNo}">
<input id="apvLnNo" type="hidden" value="${atMakAgr=='Y' ? '1' : myApOngdApvLnDVo.apvLnNo}" /></c:if><c:if
	test="${not empty param.formId or not empty param.rejtApvNo or apvData.docStatCd=='temp'}">
<input id="formId" type="hidden" value="${apFormBVo.formId}" /></c:if>
<input id="formApvLnTypCd" type="hidden" value="${apFormBVo.formApvLnTypCd}" />
<input id="statCd" type="hidden" value="" /><c:if
	test="${not empty atMakAgr}">
<input id="atMakAgr" type="hidden" value="${atMakAgr}" /></c:if><c:if
	test="${not empty atMakPrcDept}">
<input id="atMakPrcDept" type="hidden" value="${atMakPrcDept}" /></c:if><c:if
	test="${not empty param.rejtApvNo}">
<input id="rejtApvNo" type="hidden" value="${param.rejtApvNo}" /></c:if><c:if
	test="${not empty param.refDocApvNo and empty param.rejtApvNo}">
<input id="rejtApvNo" type="hidden" value="${param.refDocApvNo}" /></c:if><c:if
	test="${not empty param.orgnApvNo}">
<input id="orgnApvNo" type="hidden" value="${param.orgnApvNo}" /></c:if>
</form></c:if>

<form id="docForm" method="post" enctype="multipart/form-data" style="display:none">
<div id="docDataArea" style="display:none">
	<div id="basicValues">
	<input id="apvNo" name="apvNo" type="hidden" value="${param.apvNo}" />
	<input id="tempApvNo" name="tempApvNo" type="hidden" value="" /><c:if
		test="${not empty myApOngdApvLnDVo.apvLnPno}">
	<input id="apvLnPno" name="apvLnPno" type="hidden" value="${atMakAgr=='Y' ? myTurnApOngdApvLnDVo.apvLnNo : myApOngdApvLnDVo.apvLnPno}" /></c:if><c:if
		test="${not empty myTurnApOngdApvLnDVo.apvLnNo}">
	<input id="apvLnNo" name="apvLnNo" type="hidden" value="${atMakAgr=='Y' ? '1' : myApOngdApvLnDVo.apvLnNo}" /></c:if>
	<input id="statCd" name="statCd" type="hidden" value="" />
	<input id="secuId" name="secuId" type="hidden" value="" />
	</div><%
		// 문서정보 - 문서정보 팝업 %>
	<div id="docInfoArea" data-modified="${atMak}"><c:if
			test="${atMak == 'Y'}">
		<input type="hidden" name="docSubj" value="<u:out value="${apvData.docSubj}" type="value" />">
		<input type="hidden" name="formNm" value="<u:out value="${apvData.formNm}" type="value" />">
		<input type="hidden" name="docNo" value="<u:out value="${apvData.docNo}" type="value" />">
		<input type="hidden" name="docKeepPrdCd" value="<u:out value="${apvData.docKeepPrdCd}" type="value" />">
		<input type="hidden" name="enfcDocKeepPrdCd" value="<u:out value="${apvData.enfcDocKeepPrdCd}" type="value" />">
		<input type="hidden" name="ugntDocYn" value="<u:out value="${apvData.ugntDocYn}" type="value" />">
		<input type="hidden" name="seculCd" value="<u:out value="${apvData.seculCd}" type="value" />">
		<input type="hidden" name="secuDocYn" value="<u:out value="${apvData.secuDocYn}" type="value" />">
		<input type="hidden" name="docPw" value="<u:out value="${apvData.docPw}" type="value" />">
		<input type="hidden" name="allReadYn" value="<u:out value="${apvData.allReadYn}" type="value" />">
		<input type="hidden" name="regRecLstRegYn" value="<u:out value="${apvData.regRecLstRegYn}" type="value" />">
		<input type="hidden" name="docTypCd" value="<u:out value="${apvData.docTypCd}" type="value" />">
		<input type="hidden" name="enfcScopCd" value="<u:out value="${apvData.enfcScopCd}" type="value" />">
		<input type="hidden" name="sendrNmRescId" value="<u:out value="${apvData.sendrNmRescId}" type="value" />">
		<input type="hidden" name="clsInfoNm" value="<u:out value="${apvData.clsInfoNm}" type="value" />">
		<input type="hidden" name="clsInfoId" value="<u:out value="${apvData.clsInfoId}" type="value" />">
		<input type="hidden" name="docLangTypCd" value="<u:out value="${apvData.docLangTypCd}" type="value" />">
		<input type="hidden" name="sendrNmRescNm" value="<u:out value="${apvData.sendrNmRescNm}" type="value" />">
		<input type="hidden" name="docKeepPrdNm" value="<u:out value="${apvData.docKeepPrdNm}" type="value" />">
		<input type="hidden" name="enfcDocKeepPrdNm" value="<u:out value="${apvData.enfcDocKeepPrdNm}" type="value" />">
		<input type="hidden" name="docTypNm" value="<u:out value="${apvData.docTypNm}" type="value" />">
		<input type="hidden" name="enfcScopNm" value="<u:out value="${apvData.enfcScopNm}" type="value" />">
		<input type="hidden" name="formWdthTypCd" value="<u:out value="${apvData.formWdthTypCd}" type="value" />">
		<input type="hidden" name="bodyHghtPx" value="<u:out value="${apvData.bodyHghtPx}" type="value" />"></c:if><c:if
			test="${apvData.docProsStatCd == 'apvd' and apvData.docTypCd == 'extro'}">
		<input type="hidden" name="enfcScopCd" value="<u:out value="${apvData.enfcScopCd}" type="value" />"></c:if>
	</div><%
		// 결재선 설정 정보 - 결재선유형, 부모결재선번호 %>
	<div id="docApvLnSetupArea"></div><%
		// 결재선 정보 - 결재자, 합의부서 등 
		// data-modified : Y 가 설정되면 결재선을 조회하지 않고, 현재 세션정보를 결재선의 기안자로 세팅함 %><u:set
		test="${atMak == 'Y'
			or myTurnApOngdApvLnDVo.apvrRoleCd == 'makAgr'
			or myTurnApOngdApvLnDVo.apvrRoleCd == 'makVw'
			or prcDeptFirstApvr == 'Y'
			}"
		var="docApvLnModified" value="Y" />
	<div id="docApvLnArea" data-modified="${docApvLnModified}"><c:if
		test="${docApvLnModified == 'Y'}">
		<input type="hidden" name="apvLnTypCd" value="${apvData.apvLnTypCd}" /><c:forEach
			items="${currApOngdApvLnDVoList}" var="apOngdApvLnDVo">
		<input name="apvLn" value="<u:out value='{"apvrRoleCd":"${apOngdApvLnDVo.apvrRoleCd
			}","dblApvTypCd":"${apOngdApvLnDVo.dblApvTypCd
			}","apvrUid":"${apOngdApvLnDVo.apvrUid
			}","apvrNm":"${apOngdApvLnDVo.apvrNm
			}","apvrPositNm":"${apOngdApvLnDVo.apvrPositNm
			}","apvrTitleNm":"${apOngdApvLnDVo.apvrTitleNm
			}","apvDeptId":"${apOngdApvLnDVo.apvDeptId
			}","apvDeptNm":"${apOngdApvLnDVo.apvDeptNm
			}","apvDeptAbbrNm":"${apOngdApvLnDVo.apvDeptAbbrNm
			}","absRsonCd":"${apOngdApvLnDVo.absRsonCd
			}","absRsonNm":"${apOngdApvLnDVo.absRsonNm
			}","apvStatCd":"${apOngdApvLnDVo.apvStatCd
			}","apvrDeptYn":"${apOngdApvLnDVo.apvrDeptYn}"}' type="value" />" type="hidden"></c:forEach>
	</c:if></div><%
		// 의견정보 - 의견 + 결재비밀번호 확인값 %>
	<div id="docOpinArea"><c:if test="${atCmplDoc != 'Y'}"><c:if
	
		test="${not empty apOngdHoldOpinDVo}">
		<input type="hidden" name="apvOpinCont" value="<u:out value="${apOngdHoldOpinDVo.apvOpinCont}" type="value" />" />
		<input type="hidden" name="apvOpinDispYn" value="${apOngdHoldOpinDVo.apvOpinDispYn}" /></c:if><c:if
		
		test="${empty apOngdHoldOpinDVo}">
		<input type="hidden" name="apvOpinCont" value="<u:out value="${myTurnApOngdApvLnDVo.apvOpinCont}" type="value" />" />
		<input type="hidden" name="apvOpinDispYn" value="${myTurnApOngdApvLnDVo.apvOpinDispYn}" /></c:if>
	</c:if></div><%
		// 첨부 파일 - 파일 시퀀스, 파일명 등의 기존 저장 데이터 정보 %>
	<dl id="docFileInfoArea" data-modified="${atMak}"><c:if test="${atMak == 'Y'}">
		<input type="hidden" name="maxFileSeq" value="0"><c:forEach
			items="${apOngdAttFileLVoList}" var="apOngdAttFileLVo">
		<dd class="sizeZero" style="display:none">
			<input type="hidden" name="attSeq" value="${apOngdAttFileLVo.attSeq}">
			<input type="hidden" name="attDispNm" value="${apOngdAttFileLVo.attDispNm}">
			<input type="hidden" name="attUseYn" value="Y">
		</dd></c:forEach>
	</c:if></dl><%
		// 첨부 파일 - 실제 input[type=file] 테그가 위치할 곳 %>
	<div id="docFileTagArea" style="display:none"></div><%
		// 참조 문서 정보 %>
	<div id="docRefDocArea" style="display:none"><c:if
		test="${atMak == 'Y'}">
		<input type='hidden' name='refApvModified' value='Y' /><c:forEach
			items="${refApOngdBVoList}" var="refApOngdBVo">"apvNo","docSubj","makrUid","makrNm","cmplDt","secuYn"
		<input name="refApv" value="<u:out value='{"apvNo":"${refApOngdBVo.apvNo
			}","docSubj":"${refApOngdBVo.docSubj
			}","makrUid":"${refApOngdBVo.makrUid
			}","makrNm":"${refApOngdBVo.makrNm
			}","cmplDt":"${refApOngdBVo.cmplDt
			}","secuYn":"${not empty refApOngdBVo.docPwEnc
				and refApOngdBVo.makrUid != sessionScope.userVo.userUid ? "Y" : ""}"}' type="value" />" type="hidden"></c:forEach>
	</c:if></div><%
		// 본문 수정 - 기안 이후의 결재자가 수정한 본문 HTML %>
	<div id="bodyHtmlEditArea" style="display:none"></div><%
		// 수신처 정보 %>
	<div id="recvDeptArea" style="display:none" data-modified="${atMak}"><c:if
		test="${atMak == 'Y'}">
		<input type='hidden' name='recvDeptModified' value='Y' /><c:forEach
			items="${apOngdRecvDeptLVoList}" var="apOngdRecvDeptLVo">
		<input name="recvDept" value="<u:out value='{"recvDeptTypCd":"${apOngdRecvDeptLVo.recvDeptTypCd
			}","recvDeptTypNm":"${apOngdRecvDeptLVo.recvDeptTypNm
			}","recvDeptId":"${apOngdRecvDeptLVo.recvDeptId
			}","recvDeptNm":"${apOngdRecvDeptLVo.recvDeptNm
			}","refDeptId":"${apOngdRecvDeptLVo.refDeptId
			}","refDeptNm":"${apOngdRecvDeptLVo.refDeptNm}"}' type="value" />" type="hidden"></c:forEach>
	</c:if></div>
	<div id="senderStampArea" style="display:none"></div>
	<div id="trxDataArea" style="display:none"></div>
</div>
</form>