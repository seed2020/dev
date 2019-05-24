<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

String[] apvLns = {"apvLn","apvLnMixd","apvLn1LnAgr","apvLn2LnAgr","apvLnDbl"};
request.setAttribute("apvLns", apvLns);

String apvLnPno = request.getParameter("apvLnPno");
if(apvLnPno!=null && !apvLnPno.isEmpty()){
	request.setAttribute("apvLnPnoParam", "&apvLnPno="+apvLnPno);
}
String apvLnNo = request.getParameter("apvLnNo");
if(apvLnNo!=null && !apvLnNo.isEmpty()){
	request.setAttribute("apvLnNoParam", "&apvLnNo="+apvLnNo);
}

// 결재 본문 보기 사용자
if("waitBx".equals(request.getParameter("bxId"))
		&& request.getAttribute("frmYn") == null
		&& com.innobiz.orange.web.pt.utils.SysSetupUtil.isApBodyViewUser(
				(com.innobiz.orange.web.pt.secu.UserVo)session.getAttribute("userVo"))){
	request.setAttribute("apBodyViewUser", Boolean.TRUE);
}

// u:set - atMak : 기안중 문서인지(상신할 문서)
// u:set - atWait : 현재 내 검토/결재/합의 를 기다리는 중인지
%><jsp:useBean id="call" class="com.innobiz.orange.web.ap.utils.ELCall" scope="application"
/><u:set
	test="${ call['ApDocUtil.isAtMak'][myTurnApOngdApvLnDVo.apvrRoleCd][myTurnApOngdApvLnDVo.apvStatCd]
		and param.intgTypCd ne 'ERP_CHIT' }"
	var="atMak" value="Y"
/><u:set
	test="${ call['ApDocUtil.isAtWait'][myTurnApOngdApvLnDVo.apvrRoleCd][myTurnApOngdApvLnDVo.apvStatCd]
		and param.intgTypCd ne 'ERP_CHIT' }"
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
/><u:secu
	auth="A"><u:set test="${true}" var="adminAuth" value="Y"
/></u:secu><u:secu
	auth="W"><u:set test="${true}" var="writeAuth" value="Y"
/></u:secu><c:if

		test="${not empty downDocYn}">
<script type="text/javascript">
<!--
function doNothing(){}
//-->
</script></c:if><c:if

		test="${empty downDocYn}">
<script type="text/javascript">
<!--
<% // 게시판선택%>
function listBrdPop(){
	dialog.open('sendApvDialog', '<u:msg titleId="bb.btn.bbChoi" alt="게시판선택" />', './listBrdPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}');
}<%
// 게시보내기 - 버튼 클릭시 - 게시판 선택 팝업 띄우기 %>
function sendToBb(){
	dialog.open('sendApvDialog', '<u:msg titleId="bb.btn.bbChoi" alt="게시판선택" />', './listBrdPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&sendFunc=sendToBbByBrdId');
}<%
// 게시보내기 - 게시판 선택후 호출됨 %>
function sendToBbByBrdId(brdId){
	var menuId = getMenuIdByUrl("/bb/listBull.do?brdId="+brdId);
	callAjax('./saveSendAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}', {apvNo:'${param.apvNo}'}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.sendNo != null) {
			dialog.close('sendApvDialog');
			dialog.open('setSendWritePop', '<u:msg titleId="ap.btn.sendToBb" alt="게시 보내기" />', '/bb/setSendPop.do?brdId='+brdId+(menuId=='' ? '' : "&menuId="+menuId)+'&sendNo='+data.sendNo+'&returnFunc=sendToBbClose');
		}
	});
}<%
// 게시보내기 - 저장 버튼 처리후 호출 %>
function sendToBbClose(){
	dialog.close('setSendWritePop');
}<%
// 메일보내기 %>
function sendToMail(){
	var param = {bxId:'${param.bxId}', apvNo:'${param.apvNo}'};<c:forEach
		items="${arrMnuParam}" var="mnuParam">
	param['${mnuParam[0]}'] = '${mnuParam[1]}';</c:forEach>
	emailSendPop(param,'${menuId}');
}
var gOptConfig = ${optConfigJson};<%// 결재 옵션 - json %>
var gApvLnMaxCnt = ${apvLnMaxCnt};<%// 결재라인 설정 최대값 - listApvLnFrm.jsp 에서 사용 %>
var gLstDupDispYn = "${apv.lstDupDispYn}";<%// 목록중복표시여부 - 최종결재+리스트,서명+리스트 - ApOngoFormApvLnDVo 가  도장방 영역별("apv", "agr", "req", "prc")로 들어 있음 %>
<%
// [버튼] 문서정보 %>
function openDocInfo(initAuto){
	var $docNmArea = $("#docForm #headerArea #docNameViewArea");
	var donNmParam = $docNmArea.length==0 ? "&noDocNm=Y" : "";
	var option = (initAuto==true) ? {hidden:true} : null;
	dialog.open2("setDocInfoDialog", '<u:msg titleId="ap.btn.docInfo" alt="문서정보" />', "./setDocInfoPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}"+donNmParam+(initAuto==true ? '&initAuto=Y' : ''), option);
}<%
// [버튼] 상세정보 - 조회용 (문서정보 / 결재정보(결재라인) / 수신처정보) %>
function viewDocDetl(onTab){
	onTab = onTab==null ? "${apvData.docProsStatCd=='ongo' ? 'apvInof' : ''}" : onTab;
	${frmYn=='Y'? 'parent.' : ''}dialog.open("setDocDetlDialog", '<u:msg titleId="ap.btn.docDetl" alt="상세정보" />', "./viewDocDetlPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${not empty param.apvNo ? param.apvNo : param.orgnApvNo}${apvLnPnoParam}${apvLnNoParam}${empty param.refdBy ? '' : '&refdBy='.concat(param.refdBy)}"+(onTab=='' ? '' : '&onTab='+onTab));
}<%// 관리자 - 보안등급 변경 - 후처리용(목록의 함수와 명 맞춤) %>
function openDetl(onTab){ viewDocDetl(onTab); }<%
// [버튼] 본문수정 %>
function editDocBodyDetl(){
	var bodyHstNo = '${myTurnApOngdApvLnDVo.holdBodyHstNo}';
	dialog.open("setDocBodyHtmlDialog", '<u:msg titleId="ap.btn.modBody" alt="본문수정" />', "./setDocBodyHtmlPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${not empty param.orgnApvNo ? param.orgnApvNo : param.apvNo}"+(bodyHstNo=='' ? '' : '&bodyHstNo='+bodyHstNo));
	dialog.onClose("setDocBodyHtmlDialog", function(){ editor('docBodyHtml').clean(); unloadEvent.removeEditor('docBodyHtml'); });
}<%
// [버튼] 제목 수정 %>
function modifySubj(){
	dialog.open("setModifySubjDialog", '<u:msg titleId="ap.jsp.modTitle" alt="제목 수정" />', "./setModifySubjPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${not empty param.orgnApvNo ? param.orgnApvNo : param.apvNo}");
}<%
// 데이터 영역의 데이터 리턴 %>
function getDocDataArea(areaId){
	var $subArea = $gDocDataArea.find("#"+areaId+"Area");
	if($subArea.length==0) return null;
	return new ParamMap().getData($subArea[0]);
}<%
// 인쇄 모드로 변환 후 인쇄 - 기안중 일대 %>
function printEditDoc(){
	setViewMode('print');
	printDoc();
	setViewMode('edit');
}<%
// 문서 인쇄 %>
function printDoc(){<%
	// 3줄 도장방이 아니거나  apvLnList:결재자 리스트, apvLnOneTopList:최종결재 리스트, apvLnMultiTopList:도장방 리스트 면 %>
	var $docArea = $("#docArea");
	var $apvLnAreas = $docArea.children("div[id='apvLnArea']");
	var $apvLnArea1 = $apvLnAreas.eq(0);
	var $apvLnArea2 = $apvLnAreas.eq(1);
	var $apvLn = $apvLnArea1.find('#apvLn:first');
	var apvLnDispTypCd = $apvLn.attr('data-apvLnDispTypCd');
	var formApvLnTypCd = $apvLn.attr('data-formApvLnTypCd');
	var stampType = "";
	
	if(apvLnDispTypCd=='3row'){
		if(formApvLnTypCd == 'apvLnMixd' || formApvLnTypCd == 'apvLn'){
			stampType = "1Line";
		} else if(formApvLnTypCd == 'apvLn2LnAgr'){
			stampType = "2Line";
		} else if(formApvLnTypCd == 'apvLn1LnAgr' || formApvLnTypCd == 'apvLnDbl' || formApvLnTypCd == 'apvLnWrtn'){
			stampType = "1Line2";
		}
	}
	
	var $pArea = $("body div:first");
	var widths = [['printMin', 650], ['printAp6', 705], ['printAp7', 805], ['printAp8', 905], ['printAp9', 1005], ['printAp10', 1105], ['printAp11', 1205], ['printAp100',905]];
	var printWidth = 0;
	widths.each(function(index, obj){
		if(printWidth==0 && $pArea.hasClass(obj[0])){
			printWidth = obj[1];
		}
	});
	
	var $bodyHtmlViewArea = $("#bodyHtmlViewArea");
	if(!(browser.ieCompatibility || (browser.ie && browser.ver < 9))){
		$bodyHtmlViewArea.css('overflow-x','visible');
	}
	
	var bxCnt=0, calcWidth=0, headWidth=28, adjustWidth = 8;
	var widthObjs = $apvLnAreas.find('td.approval_body,td.approval_img'), headWidthObjs=null;
	var marginWidth = getMarginWidth();
	<%
	// 3줄짜리의 도장방 이면서, 인쇄설정에서 넓이를 지정 한것만 %>
	if(stampType != "" && printWidth != 0){<%
		// 도장방별 넓이 계산해서 각각의 셀 넓이를 지정함
		%>
		if(stampType=='1Line'){
			bxCnt = $apvLnArea1.find("td.approval_img").length;
			if(bxCnt > 8) headWidth = 22;
			calcWidth = Math.floor((printWidth - marginWidth - bxCnt - (headWidth+2)) / bxCnt);
			
		} else if(stampType=='2Line'){
			bxCnt = Math.max($apvLnArea1.find("#apvLn td.approval_img").length, $apvLnArea2.find("#apvLn td.approval_img").length);
			if(bxCnt > 8) headWidth = 22;
			calcWidth = Math.floor((printWidth - marginWidth - bxCnt - (headWidth+2)) / bxCnt);
			
		} else if(stampType=='1Line2'){
			bxCnt = $apvLnArea1.find("td.approval_img").length;
			if(bxCnt > 8) headWidth = 22;
			var gapBtwLn = bxCnt>=10 ? 5 : bxCnt>=8 ? 10 : 20;
			calcWidth = Math.floor((printWidth - marginWidth - bxCnt - (headWidth+headWidth+4+gapBtwLn)) / bxCnt);
		}
		
		if(calcWidth > 103 - adjustWidth) calcWidth = 103 - adjustWidth;
		
		if(headWidth!=28){
			headWidthObjs = $apvLnAreas.find('td.approval_head');
			headWidthObjs.width(headWidth+'px');
		}
		
		widthObjs.each(function(){
			$(this).attr('data-viewWidth', $(this).width());
			$(this).width((calcWidth-adjustWidth)+'px');
		});
	}
	<%
	// 인쇄여백 설정 %>
	var formMagn = '${apFormBVo.formMagnVa}';
	if(formMagn!='') $("#docDiv").css("margin", formMagn);
	
	printWeb();
	
	if(formMagn!='') $("#docDiv").css("margin", '');
	<%
	// 3줄짜리의 도장방 이면서, 인쇄설정에서 넓이를 지정 한것만
	// - 인쇄용으로 변형한 html 을 원복시킴 %>
	if(stampType != "" && printWidth != 0){
		widthObjs.each(function(){
			$(this).width($(this).attr('data-viewWidth')+'px');
		});
		if(headWidth!=28){
			headWidthObjs.width(28+'px');
		}
	}

	if(!(browser.ieCompatibility || (browser.ie && browser.ver < 9))){
		$bodyHtmlViewArea.css('overflow-x','visible');
	}
}<%
// 인쇄전환, 편집전환 %>
function setViewMode(mode){
	var $docDiv = $("#docDiv");
	if(mode=='print'){
		var $bodyHtmlArea = $docDiv.find("#bodyHtmlArea");
		if($bodyHtmlArea.length>0){
			$bodyHtmlArea.hide();
			$docDiv.find("#bodyHtmlViewArea").show().find(".editor").html(editor("bodyHtml").getHtml());
		}
	} else {
		var $bodyHtmlArea = $docDiv.find("#bodyHtmlArea");
		if($bodyHtmlArea.length>0){
			$docDiv.find("#bodyHtmlViewArea").hide();
			$bodyHtmlArea.show();
		}
	}
	if(window.setXmlEditViewMode){
		setXmlEditViewMode(mode);<%// 편집양식 인쇄모드 변경 %>
	}
}<%
// 인쇄 여백 설정 %>
function getMarginWidth(){
	var formMagn = '${apFormBVo.formMagnVa}';
	if(formMagn=='') return 0;
	var magns = formMagn.replace(/px/gi, '').split(' ');
	var w = 0;
	if(magns.length>1 && magns[1]!='') w += parseInt(magns[1]);
	if(magns.length>3 && magns[3]!='') w += parseInt(magns[3]);
	return w;
}<%
//경로지정 %>
var setAutoApvLnGrpDone = true;
function openApvLn(apvLnGrpId, fixdApvrYn, autoApvLnCd){
	if(!setAutoApvLnGrpDone){
		openApvLn2('${empty refMakApvLn ? formApvLnGrpId : ''}', '${empty refMakApvLn and fixdApvrYn eq 'Y' ? 'Y' : 'N'}', '${empty refMakApvLn ? autoApvLnCd : ''}');
	} else {
		openApvLn2(apvLnGrpId, fixdApvrYn, autoApvLnCd);
	}
}<%
//경로지정 %>
function openApvLn2(apvLnGrpId, fixdApvrYn, autoApvLnCd){
	var modified =  $("#docDataArea #docApvLnArea").attr('data-modified');
	var params = [], apvLnPno = "${atMakAgr=='Y' ? param.apvLnNo : empty param.apvLnPno ? '0' : param.apvLnPno}";
	var apvrRoleCd = "${myTurnApOngdApvLnDVo.apvrRoleCd}";
	var apvNo="${param.apvNo}";<%// 결재번호 - 임시저장 등 저장된 경우 %>
	if(apvNo!='') params.push('&apvNo='+apvNo);
	params.push("&apvLnPno="+apvLnPno);
	params.push("&apvLnNo=${atMakAgr=='Y' ? '1' : myTurnApOngdApvLnDVo.apvLnNo}");
	params.push("&formApvLnTypCd=${apFormBVo.formApvLnTypCd}");
	params.push("${not empty myTurnApOngdApvLnDVo.holdApvLnHstNo ? '&apvLnHstNo='.concat(myTurnApOngdApvLnDVo.holdApvLnHstNo) : ''}");
	if(apvrRoleCd=='makAgr' || apvrRoleCd=='deptOrdrdAgr' || apvrRoleCd=='deptParalAgr' || apvLnPno != '0'){
		params.push("&makrRoleCd=makAgr&apvrRoleCd=${myTurnApOngdApvLnDVo.apvrRoleCd}");
		var upward = "${optConfigMap.exDeptAgrRange == 'Y' ? '' : myTurnApOngdApvLnDVo.apvDeptId}";
		if(upward != '') params.push("&upward="+upward);
	} else if(apvrRoleCd=='makVw' || apvrRoleCd=='fstVw' || apvrRoleCd=='pubVw' || apvrRoleCd=='paralPubVw'){
		params.push("&makrRoleCd=makVw&apvrRoleCd=${myTurnApOngdApvLnDVo.apvrRoleCd}");
		var upward = "${optConfigMap.exPubVwRange == 'Y' ? '' : apvData.recLstDeptId}";
		if(upward != '') params.push("&upward="+upward);
		else {
			var selOrgId = "${myTurnApOngdApvLnDVo.apvDeptId == apvData.recLstDeptId ? '' : apvData.recLstDeptId}";
			if(selOrgId != '') params.push("&selOrgId="+selOrgId);
		}
	} else {
		params.push("&apvrRoleCd=${myTurnApOngdApvLnDVo.apvrRoleCd}");
	}
	params.push("${myTurnApOngdApvLnDVo.dblApvTypCd =='prcDept' ? '&prcDeptYn=Y' : ''}");
	if(apvLnGrpId!=null && apvLnGrpId!=''){
		params.push('&apvLnGrpId='+apvLnGrpId);
		if(fixdApvrYn!=null){
			params.push('&fixdApvrYn='+fixdApvrYn);
			params.push('&apvLnGrpTypCd=pub');
		}
	}
	if(autoApvLnCd!=null){
		params.push('&autoApvLnCd='+autoApvLnCd);
	}
	if(apvLnGrpId!=null || autoApvLnCd!=null){
		params.push('&initAuto=Y');
	}
	if(modified=='Y') params.push('&modified='+modified);
	var option = (apvLnGrpId!=null || apvLnGrpId!=null) ? {hidden:true} : null;
	dialog.open2("setApvLnDialog", '<u:msg titleId="ap.btn.setApvLn" alt="결재선지정" />', "./setApvLnPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}"+params.join(''), option);
}<%
// 결재선 지정 팝업에서 - 결재자 추가시 자동 순서조정 할 때, 현재 결재자 앞으로 가지 못하게 하기 위한 것 %>
function getMyTurnUid(){
	return "${myTurnApOngdApvLnDVo.apvrUid}";
}<%
// 현 결재자 정보 %>
function getMyTurnObj(){
	return {apvNo:"${myTurnApOngdApvLnDVo.apvNo}", apvLnPno:"${myTurnApOngdApvLnDVo.apvLnPno}", apvLnNo:"${myTurnApOngdApvLnDVo.apvLnNo}", apvLnHstNo:"${myTurnApOngdApvLnDVo.apvLnHstNo}", apvrUid:"${myTurnApOngdApvLnDVo.apvrUid}"};
}<%
// 도장방 3줄용 넓이 조회 %>
function get3RowWdth(){
	var wdth3Row = null;
	$("#docArea #apvLn").each(function(){
		if(wdth3Row==null && $(this).attr("data-apvLnDispTypCd")=='3row'){
			wdth3Row = $(this).attr('data-3rowStyle');
		}
	});
	return wdth3Row==null ? '' : wdth3Row;
}<%
// 전송 %>
function submitDoc(statCd){
	var wasTemp = (statCd=='mak' && "${not empty apvData.apvNo and apvData.docStatCd eq 'temp' and empty apvData.intgNo ? 'Y' : ''}" == 'Y');
	dialog.closeAll();
	var $form = $("#docForm");
	if(wasTemp){
		$form.find("#tempApvNo").val($form.find("#apvNo").val());
		$form.find("#apvNo").val("");
	}
	if($("#bodyHtmlArea").length != 0) editor('bodyHtml').prepare();
	$form.find("#statCd").val(statCd==null ? "" : statCd);
	$form.attr("method", "post");
	$form.attr("action", "./transDoc.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}");
	$form.attr('target','dataframe');
	saveFileToForm('docAttch', $form, null);
	//$form.submit();
	if(wasTemp){
		$form.find("#apvNo").val($form.find("#tempApvNo").val());
		$form.find("#tempApvNo").val("");
	}
}<%
// 의견, 심사요청의견 - mode:censr 가 넘어옴 %>
function openOpin(mode){
	var popTitle = mode=='censr' ? '<u:msg titleId="ap.btn.reqCensrOpin" alt="심사요청의견" />' : '<u:msg titleId="ap.doc.opin" alt="의견" />';
	dialog.open("setDocOpinDialog", popTitle, "./setDocOpinPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}${atMakAgr!='Y' and not empty myTurnApOngdApvLnDVo ? '&apvNo='.concat(myTurnApOngdApvLnDVo.apvNo) : ''}${atMakAgr!='Y' and not empty myTurnApOngdApvLnDVo ? '&apvLnPno='.concat(myTurnApOngdApvLnDVo.apvLnPno) : ''}${atMakAgr!='Y' and not empty myTurnApOngdApvLnDVo ? '&apvLnNo='.concat(myTurnApOngdApvLnDVo.apvLnNo) : ''}&opinInDoc=${myTurnApOngdApvLnDVo.apvLnPno=='0' and atMakAgr!='Y' and apvData.docProsStatCd!='apvd' and apvData.docProsStatCd!='pubVw' ? opinInDoc : 'N'}"+(mode=='censr' ? '&reqCensr=Y' : ''));
}<%
// 기안자 지정 - 부서합의에서 부서가 지정된 경우 - 해당 합의부서의 기안자를 지정 할 때 %>
function setMakAgr(){<%
	//var opt = {data:{userUid:"${deptAgrMakr.apvrUid}"},titleId:"ap.title.setMakr",userStatCd:"02",oneDeptId:'${myTurnApOngdApvLnDVo.apvDeptId}'}; %>
	var opt = {titleId:"ap.title.setMakr",userStatCd:"02"};
	var oneDeptId = "${optConfigMap.exDeptAgrMakerRange == 'Y' ? '' : myTurnApOngdApvLnDVo.apvDeptId}";
	if(oneDeptId != '') opt['oneDeptId'] = oneDeptId;
	searchUserPop(opt, function(userVo){
		if(userVo==null){
			alertMsg('or.msg.noUser');
			return false;<%
		// ap.cfrm.setMakr="{0}"을(를) 기안자로 지정 하시겠습니까 ? %>
		} else if(confirmMsg("ap.cfrm.setMakr",[userVo.rescNm])){
			var result = false;
			callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"setMakAgr", apvNo:"${param.apvNo}",
					apvLnPno:"${myTurnApOngdApvLnDVo.apvLnPno=='0' ? myTurnApOngdApvLnDVo.apvLnNo : myTurnApOngdApvLnDVo.apvLnPno}", apvrUid:userVo.userUid}, function(data){
				if(data.message != null) alert(data.message);
				result = data.result == 'ok';
			});
			if(result) toList();
		} else {
			return false;
		}
	});
}<%
// 담당자 지정 - 처리부서를 지정 했을 때 - 해당 처리부서의 첫번째 결재자를 지정 할 때 %>
function setPrcDeptAgr(){
	searchUserPop({data:{userUid:""},titleId:"ap.title.setPich",userStatCd:"02"}, function(userVo){
		if(userVo==null){
			alertMsg('or.msg.noUser');
			return false;<%
		// ap.cfrm.setPich={0}을(를) 담당자로 지정 하시겠습니까 ? %>
		} else if(confirmMsg("ap.cfrm.setPich",[userVo.rescNm])){
			var result = false;
			callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"setPrcDeptAgr", apvNo:"${param.apvNo}",
					apvLnPno:"${myTurnApOngdApvLnDVo.apvLnPno}", apvLnNo:"${myTurnApOngdApvLnDVo.apvLnNo}", apvrUid:userVo.userUid}, function(data){
				if(data.message != null) alert(data.message);
				result = data.result == 'ok';
			});
			if(result) toList();
		} else {
			return false;
		}
	});
}<%
// 기안, 승인, 반려, 찬성, 반대, 재검토 - apvStatCd - mak:기안, apvd:승인, rejt:반려, pros:찬성, cons:반대, reRevw:재검토 
//   - paramAtMakAgr : 부서합의 부서대기함에서 - 재검토 요청 할 때만 - true 넘어옴  %>
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
		// 수신처 지정 체크 %>
		if(!wasRecverDone()) return;<%
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
	var popTitle = callTerm('ap.term.'+apvStatCd);<%//mak:기안, apvd:승인, pros:찬성, cons:반대
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
	if(setErpXML() == false) return;
	if($("#wfFormBodyArea").attr("data-edit") == "Y"){<%--
		업무관리 : 양식 데이터 말기 --%>
		if(setWfFormBody($("#docForm #wfFormBodyArea")) == false) return;
	}
	var prosUrl = "./setDocProsPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvStatCd="+apvStatCd+"&opinInDoc="+opDisp+noStamp+(paramAtMakAgr? "&atMakAgr=Y" : "");
	var dialogOption = gOptConfig.upwardApvPop == 'Y' ? {offsetTop:157} : null;
	dialog.open("setDocProsDialog", popTitle, prosUrl, null, null, null, null, null, dialogOption);
}<%
// 문서에 있는 결재선 정보 리턴 - 히든 테그에서 찾아서 리턴함 %>
function getApvLnData(){
	var arr = [];
	$("#docDataArea #docApvLnArea input[name='apvLn']").each(function(){
		arr.push(JSON.parse(this.value));
	});
	return arr.length==0 ? null : arr;
}<%
// 문서정보 설정 체크 %>
function wasDocInfoDone(){
	var subj = $gDocDataArea.find("#docInfoArea input[name='docSubj']").val();
	var docLangTypCd = $gDocDataArea.find("#docInfoArea input[name='docLangTypCd']").val();
	if(subj==null || subj=='' || docLangTypCd==null || docLangTypCd==''){<%
		// ap.msg.needToSetup={0}를(을) 설정해 주십시요. - [문서정보]%>
		alertMsg("ap.msg.needToSetup", ["#ap.btn.docInfo"]);
		openDocInfo();
		return false;
	}
	return true;
}<%
// 결재라인이 설정 되었는지 체크 %>
function wasApvLnDone(){
	var $apvLns = $gDocDataArea.find("#docApvLnArea input[name='apvLn']");
	var apvrObj=null, roleCd=null, dblApvTypCd=null, apvStatCd=null, formApvLnTypCd="${apFormBVo.formApvLnTypCd}";
	var isByOne=false, isMakVw=false, hasLastApvr=false;
	var isDblApv=(formApvLnTypCd=='apvLnDbl' || formApvLnTypCd=='apvLnDblList'), isCurReqDept=true, hasPrcDept=false;
	var apvLen = $apvLns.length;
	
	for(var i=0;i<apvLen;i++){
		apvrObj = JSON.parse($($apvLns[i]).val());
		roleCd = apvrObj['apvrRoleCd'];
		
		if(i==0){
			isByOne = (roleCd == 'byOne' || roleCd == 'byOneAgr');
			isMakVw = (roleCd == 'makVw');
		}
		
		if(roleCd=='apv' || roleCd=='pred'){
			hasLastApvr = true;
		}
		
		if(isDblApv){
			dblApvTypCd = apvrObj['dblApvTypCd'];
			apvStatCd = apvrObj['apvStatCd'];
			
			if(dblApvTypCd=='prcDept'){
				hasPrcDept = true;
				if(apvStatCd=='inApv'){
					isCurReqDept = false;
				}
			}
		}
	}
	
	if(apvLen==0 || (isMakVw && apvLen==1) || (isDblApv && !hasPrcDept)){
		alertMsg("ap.msg.needToSetup", ["#ap.cmpt.apvLine"]);
		openApvLn();
		return false;
	} else if(gOptConfig.needLastApvr=='Y' && !hasLastApvr){
		if(!isByOne && !isMakVw && !(isDblApv && isCurReqDept)){
			alertMsg("ap.msg.needToSetup", ["#ap.cmpt.apvLine"]);
			openApvLn();
			return false;
		}
	}
	return true;
}<%--
// 수신처 지정 되었는지 --%>
function wasRecverDone(){
	var $area = $("#docDataArea");
	if(gOptConfig.mandRecvr=='Y' && $area.find("#docInfoArea input[name='docTypCd']").val()=='extro'){
		if($area.find("#recvDeptArea input[name='recvDept']").length==0){
			alertMsg('ap.msg.noRecvDept');<%--//ap.msg.noRecvDept=수신처가 지정되지 않았습니다.--%>
			openRecvDept();
			return false;
		}
	}
	return true;
}<%
// 상신자의 결재역할 리턴 %>
function getMakrRoleCd(){
	var $apvLns = $gDocDataArea.find("#docApvLnArea input[name='apvLn']:first");
	if($apvLns.length==1){
		return JSON.parse($apvLns.val())['apvrRoleCd'];
	}
	return null;
}<%
// 이중결재 결재라인이 세팅되었는지 체크 변수 %>
var gDblApvLnDone = ${not empty dblApvLnDone ? 'true' : 'false'};<%
// 이중결재 결재라인이 설정 되었는지 체크 - 이중결재의 처리부서가 설정 되었을 때만 체크함 %>
function wasDblApvLnDone(){
	if(!gDblApvLnDone && '${myTurnApOngdApvLnDVo.apvrRoleCd}' != 'apv'){<%
		// ap.msg.needToSetup={0}를(을) 설정해 주십시요. - [결재라인] %>
		alertMsg("ap.msg.needToSetup", ["#ap.cmpt.apvLine"]);
		openApvLn();
		return false;
	}
	return true;
}<%
// 임시저장, 보류 - mode - temp:임시저장, hold:보류 %>
function saveDoc(docStat){<%
	// 기안 상태인지 - "Y" or "" %>
	var atMak = "${atMak}";
	if(atMak=='Y'){
		var docLangTypCd = $gDocDataArea.find("#docInfoArea input[name='docLangTypCd']").val();
		var docSubj = $gDocDataArea.find("#docInfoArea input[name='docSubj']").val();
		if(docLangTypCd==null || docLangTypCd=='' || docSubj==null || docSubj==''){<%
			// ap.msg.needToSetup={0}를(을) 설정해 주십시요. - [문서정보]%>
			alertMsg("ap.msg.needToSetup", ["#ap.btn.docInfo"]);
			openDocInfo();
			return;
		}
	}
	if(setErpXML()==false) return;<%
	// ap.cfrm.act={0} 하시겠습니까 ? %>
	if(confirmMsg("ap.cfrm.act", ["#ap.btn."+docStat])){
		submitDoc(docStat);
	}
}<%
// [첨부] 버튼 클릭 - 편집 모드 %>
function openAttch(){
	var attHstNo = '${empty myTurnApOngdApvLnDVo.holdAttHstNo ? apvData.attHstNo : myTurnApOngdApvLnDVo.holdAttHstNo}';
	var refDocHstNo = '${myTurnApOngdApvLnDVo.holdRefDocHstNo}';
	var modified =  $("#docDataArea #docFileInfoArea").attr('data-modified');
	var paramApvNo = '${not empty param.rejtApvNo ? param.rejtApvNo : not empty param.orgnApvNo ? param.orgnApvNo: not empty param.refDocApvNo ? param.refDocApvNo: not empty param.apvNo ? param.apvNo: ''}';
	dialog.open("setDocAttchDialog", '<u:msg titleId="ap.btn.att" alt="첨부" />', "./setDocAttchPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}${param.bxId=='recvRecLst' and not empty param.refDocApvNo ? '&showRef=Y' : ''}${not empty inTrx ? '&inTrx=Y' : ''}"+(paramApvNo!='' ? '&apvNo='+paramApvNo : '')+(attHstNo=='' ? '' : '&attHstNo='+attHstNo)+(refDocHstNo=='' ? '' : '&refDocHstNo='+refDocHstNo)+(modified=='Y' ? '&modified=Y' : ''));
}<%
// [첨부] 버튼 클릭 - 조회 모드 %>
function viewAttch(){
	var attHstNo = "${empty myTurnApOngdApvLnDVo.holdAttHstNo ? apvData.attHstNo : myTurnApOngdApvLnDVo.holdAttHstNo}";
	${frmYn=='Y'? 'parent.' : ''}dialog.open("setDocAttchDialog", '<u:msg titleId="ap.btn.att" alt="첨부" />', "./viewDocAttchPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}${vwMode=='trx' ? '&vwMode=trx' : ''}${not empty param.refdBy ? '&refdBy='.concat(param.refdBy) : ''}${apvData.sendWithRefDocYn eq 'Y' ? '&sendWithRefDocYn=Y' : ''}${winPop eq 'Y' ? '&winPop=Y' : ''}"+(attHstNo=='' ? '' : '&attHstNo='+attHstNo));
}<%
// 파일 다운로드 %>
function downAttchFile(seq) {
	var $form = $('<form>', {
			'method':'post','action':'/${apFileModule}/down${apFileTarget}File.do','target':'dataframe'
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
	<c:forEach items="${arrMnuParam}" var="mnuParam">
	$form.append($('<input>', {'name':'${mnuParam[0]}','value':'${mnuParam[1]}','type':'hidden'}));</c:forEach>
	$(document.body).append($form);
	$form.submit();
	$form.remove();
}<%
// ERP 연계 파일 다운로드 %>
function downErpIntgAttchFile(intgNo, seq) {
	var $form = $('<form>', {
			'method':'post','action':'/${apFileModule}/down${apFileTarget}File.do','target':'dataframe'
		}).append($('<input>', {
			'name':'bxId','value':'${param.bxId}','type':'hidden'
		})).append($('<input>', {
			'name':'menuId','value':'${menuId}','type':'hidden'
		})).append($('<input>', {
			'name':'intgNo','value':"${param.intgNo}",'type':'hidden'
		})).append($('<input>', {
			'name':'attSeq','value':seq,'type':'hidden'
		}));
	$(document.body).append($form);
	$form.submit();
	$form.remove();
}<%
// 참조문서 문서에 세팅 %>
function setRefApvToDoc(refApvs){
	var $area = $("#docDataArea #docRefDocArea"), buffer;
	$area.html('');<%// 기존 데이터 삭제 %>
	$area.attr('data-modified','Y');<%// 변경 되었음 세팅 %>
	$area.append("<input type='hidden' name='refApvModified' value='Y' />");<%// 변경 되었음 세팅 - parameter %><%
	
	// 히든테그 만들기 - 데이타 JSON으로 변환 %>
	var attrs = ["apvNo","docSubj","makrUid","makrNm","cmplDt","secuYn"];
	refApvs.each(function(refIndex, refApv){
		buffer = new StringBuffer();
		buffer.append("<input type='hidden' name='refApv' value='{");
		attrs.each(function(index, attr){
			if(index!=0) buffer.append(', ');
			var va = refApv[attr].replaceAll('"','\\"');
			buffer.append('"').append(attr).append('":"').append(escapeValue(va)).append('"');
		});
		buffer.append("}' />");
		$area.append(buffer.toString());
	});<%
	
	// 항목지정 - 참조문서 %>
	$area = $("#docArea div[data-name='itemsArea'] #refDocNmView");
	if($area.length>0){
		buffer = new StringBuffer();
		refApvs.each(function(index, refApv){
			if(index!=0) buffer.append(', ');
			buffer.append('<nobr><a href="javascript:openDocView(\''+refApv['apvNo']+'\',\''+refApv['secuYn']+'\')">').append(refApv['docSubj']);
		});
		$area.html(buffer.toString());
	}<%
	
	// 참조문서 본문 삽입 %>
	$area = $("#docArea #refDocBdoyArea");
	if($area.length>0){
		var refMap = {}, refApvNos = [];
		refApvs.each(function(index, refApv){
			refMap[refApv['apvNo']] = refApv;
			refApvNos.push(refApv['apvNo']);
		});
		
		if(refApvNos.length==0){
			$area.html('').hide();
		} else {
			$area.html('').show();
			
			var templateHtml = $("#docArea #refDocBdoyHtmlArea").html();
			var refBodyMap, refData;
			
			callAjax("./getRefBodyAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {refApvNos:refApvNos.join(",")}, function(data){
				if(data.message != null) alert(data.message);
				refApvNos.each(function(index, refNo){
					refBodyMap = data[refNo];
					if(refBodyMap!=null){
						refData = refMap[refNo];
						refData['cmplDt'] = refBodyMap.get('cmplDt');
						refData['bodyHtml'] = refBodyMap.get('bodyHtml');
						appendRefBodyHtml($area, refData, templateHtml);
					}
				});
			});
		}
	}
}<%
// 참조문서의 본문을 양식에 삽입함 %>
function appendRefBodyHtml($area, refData, html){
	html = html.replace('id="ref"', 'id="ref'+refData['apvNo']+'"');
	html = html.replace('<a href="javascript:;">[docSubj]</a>', '<a href="javascript:openDocView(\''+refData['apvNo']+'\',\''+refData['secuYn']+'\')">[docSubj]</a>');
	html = html.replace('[docSubj]', refData['docSubj']);
	html = html.replace('[cmplDt]', refData['cmplDt']);
	html = html.replace('[bodyHtml]', refData['bodyHtml']);
	$area.append(html);
}<%
// 수신처 설정, 수신처 변경(완결 후 발송전, mode:sending 넘어옴) %>
function openRecvDept(mode){
	var $docDataArea = $("#docDataArea");
	var enfcScopCd = $docDataArea.find("#docInfoArea [name='enfcScopCd']").val();
	if(enfcScopCd==null) enfcScopCd = "${apvData.enfcScopCd}";
	var recvDeptHstNo = '${myTurnApOngdApvLnDVo.holdRecvDeptHstNo}';
	var modified =  $docDataArea.find("#recvDeptArea").attr('data-modified');
	var popTitle = mode=='modify' ? '<u:msg titleId="ap.btn.modRecvDept" alt="수신처변경" />' : mode=='sendMore' ? '<u:msg titleId="ap.btn.sendAddDoc" alt="추가발송" />' : '<u:msg titleId="ap.btn.setRecv" alt="수신처지정" />';
	var paramApvNo = '${not empty param.orgnApvNo ? param.orgnApvNo: not empty param.apvNo ? param.apvNo: ''}';
	dialog.open("setDocRecvDeptDialog", popTitle, "./setDocRecvDeptPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}"+(paramApvNo!='' ? '&apvNo='+paramApvNo : '')+(enfcScopCd==null ? '' : '&enfcScopCd='+enfcScopCd)+(recvDeptHstNo=='' ? '' : '&recvDeptHstNo='+recvDeptHstNo)+(mode=='sendMore' ? '&sendMore=Y' : '')+(modified=='Y' ? '&modified=Y' : ''));
}<%
// 수신처 변경 %>
function modifyRecvDept(){ openRecvDept('modify'); }<%
// 추가발송 %>
function openMoreRecvDept(){ openRecvDept('sendMore'); }<%
	// 취소 옵션
	
	// 다음 결재자가 읽지 않고 - 진행중인 문서
	if("N".equals(request.getAttribute("readByNextApvr")) && "ongo".equals( ((java.util.Map)request.getAttribute("apvData")).get("docProsStatCd") )){
		
		// 기안 이면 - [기안취소 함수]
		if("0".equals(request.getParameter("apvLnPno")) && "1".equals(request.getParameter("apvLnNo"))){
// 기안 회수 %>
function cancelMak(){
	if(confirmMsg("ap.cfrm.act",["#ap.btn.cancelMak"])){
		var result = false;
		callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"cancelMak", apvNo:"${param.apvNo}"}, function(data){
			if(data.message != null) alert(data.message);
			result = data.result == 'ok';
		});
		if(result) toList("${param.bxId eq 'myBx' ? '' : 'myBx'}");
	}
}<%
		}
		// 진행함 이고 - 합의가 아니면 - [승인취소 함수] > 대기함(waitBx) 이동
		if("ongoBx".equals(request.getParameter("bxId")) && !"Y".equals(request.getAttribute("atAgrStat"))){
// 승인 취소 %>
function cancelApvd(){
	if(confirmMsg("ap.cfrm.act",["#ap.btn.cancelApv"])){
		var result = false;
		callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"cancelApv", apvNo:"${param.apvNo}",
				apvLnPno:"${myApOngdApvLnDVo.apvLnPno}", apvLnNo:"${myApOngdApvLnDVo.apvLnNo}"}, function(data){
			if(data.message != null) alert(data.message);
			result = data.result == 'ok';
		});
		if(result) toList('waitBx');
		
	}
}<%
		}
		// 진행함 이고 - 합의면 - [합의취소 함수] > 대기함(waitBx) 이동
		if("ongoBx".equals(request.getParameter("bxId")) && "Y".equals(request.getAttribute("atAgrStat"))){
// 합의 취소 %>
function cancelAgr(){
	if(confirmMsg("ap.cfrm.act",["#ap.btn.cancelAgr"])){
		var result = false;
		callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"cancelAgr", apvNo:"${param.apvNo}",
				apvLnPno:"${myApOngdApvLnDVo.apvLnPno}", apvLnNo:"${myApOngdApvLnDVo.apvLnNo}"}, function(data){
			if(data.message != null) alert(data.message);
			result = data.result == 'ok';
		});
		if(result) toList('waitBx');
	}
}
<%
		}
	}

// 재기안 %>
function remakeDoc(){
	$("#remakForm").submit();
}<%
// 수신처 데이터, 항목지정, 바닥글, 히든 데이터 영역에 세팅하기 %>
function setRecvDeptData(arr, recvDeptArea){
	var $area = recvDeptArea==null ? $("#docDataArea #recvDeptArea") : recvDeptArea, buffer, first = true;
	if(recvDeptArea==null){<%// 완결 후 [수신처변경] 버튼의 팝업에서는 바로 저장하므로 히든 테그 생성 위치를 넘김 %>
		$area.html('');
		$area.attr('data-modified','Y');<%// 변경 되었음 세팅 %>
		$area.append("<input type='hidden' name='recvDeptModified' value='Y' />");<%// 변경 되었음 세팅 - parameter %>
	}<%
	
	// 히든테그 만들기 - 데이타 JSON으로 변환 %>
	arr.each(function(inputIndex, inputData){
		buffer = new StringBuffer();
		buffer.append("<input type='hidden' name='recvDept' value='{");
		first = true;
		gRecvDeptAttrNms.each(function(idx, name){
			if(first) first = false;
			else buffer.append(',');
			buffer.append('"').append(name).append('":"').append(inputData[name]).append('"');
		});
		buffer.append("}' />");
		$area.append(buffer.toString());
	});
	<%
	
	// 항목지정 - 수신처 : 수신처 나열함, 참조처 있으면:수신처(참조처) %>
	$area = $("#docArea div[data-name='itemsArea'] #recvDeptNmView");
	if($area.length>0){
		buffer = new StringBuffer();
		appendRecvDept(buffer, arr);
		$area.html(buffer.toString());
	}<%
	
	// 항목지정 - 수신처 참조 : 수신처가 1개 이상:수신처참조, 수신처가 1개:해당수신처 출력, 참조처 있으면:수신처(참조처) %>
	$area = $("#docArea div[data-name='itemsArea'] #recvDeptRefNmView");
	if($area.length>0){
		if(arr.lenth==0){
			$area.html('');
		} else if(arr.lenth==1){
			buffer = new StringBuffer();
			appendRecvDept(buffer, arr);
			$area.html(buffer.toString());
		} else {
			$area.html('<u:msg titleId="ap.jsp.recvDept.recvRefDisp" alt="수신처참조" />');
		}
	}<%
	
	// 바닥글 - 수신처 %>
	$area = $("#senderArea #docReceiverViewArea");
	if($area.length>0){
		buffer = new StringBuffer();
		buffer.append('<nobr><u:msg titleId="cols.recvDept" alt="수신처" /> : </nobr>');
		appendRecvDept(buffer, arr);
		$area.html(buffer.toString());
	}
	
}<%
// 수신처 데이터 buffer 에 더하기 %>
function appendRecvDept(buffer, arr){
	arr.each(function(inputIndex, inputData){
		if(inputIndex!=0) buffer.append(', ');
		buffer.append('<nobr>').append(inputData['recvDeptNm']);
		if(inputData['refDeptNm']!=null && inputData['refDeptNm']!=''){
			buffer.append('(').append(inputData['refDeptNm']).append(')');
		}
		buffer.append('</nobr>');
	});
}<%
// 문서 팝업 조회 - 참조 문서 조회용 - 문서화면에서 %>
function openDocView(apvNo, pw){
	if(pw=='Y'){
		${frmYn eq 'Y' ? 'parent.' : ''}dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo+"&refdBy=${param.apvNo}&callback=openSecuDocPop");
	} else {
		${frmYn eq 'Y' ? 'parent.' : ''}dialog.open2('viewDocDialog','<u:msg titleId="ap.jsp.viewDoc" alt="문서 조회" />','./view${not empty dmUriBase ? "Ap" : ""}DocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo='+apvNo+'&refdBy=${param.apvNo}', {padding:{right:"3px",top:"5px"}});
	}
}<%
// 문서 팝업 조회 - 참조 문서 조회용 - 팝업 에서 %>
function openDocFrm(apvNo, pw){
	if(pw=='Y'){
		parent.dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo+"&refdBy=${param.apvNo}&callback=openSecuDocFrm");
	} else {
		location.replace("./viewDocFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo+"&refdBy=${param.apvNo}");
	}
}<%
// 팝업의 데이터를 저장할 히든 div - div 안에 input[type=hidden] 테그를 생성함 %>
var $gDocDataArea = null;
$(document).ready(function() {
	window.focus();
	setUniformCSS();
	$gDocDataArea = $("#docDataArea");
	
	if('Y'=='${callByIntg}'){
		$('body:first').css('padding-left', '15px').css('padding-right', '13px');
	}
	var apvLnGrpId = '${param.apvLnGrpId}';<%// 개인경로그룹 - ERP 에서 넘겨 올 수 있음 %>
	var formApvLnGrpId = '${empty refMakApvLn ? formApvLnGrpId : ''}';<%// 양식에 설정된 - 공용 결재 그룹 ID %>
	var autoApvLnCd = '${empty refMakApvLn ? autoApvLnCd : ''}';<%// 양식에 설정된 - 자동 결재선 코드 %>
	if(apvLnGrpId!=''){
		openApvLn(apvLnGrpId);
	} else if(formApvLnGrpId!='' || autoApvLnCd!=''){
		setAutoApvLnGrpDone = false;
		openApvLn(formApvLnGrpId, "${empty refMakApvLn and fixdApvrYn eq 'Y' ? 'Y' : 'N'}", autoApvLnCd);
	}
	${atMak eq 'Y' ? 'openDocInfo(true);' : ''}<%// 문서정보 히든 오픈 %>
	
	fixedBtn.setFixed("docBtnArea");<c:if test="${not empty apBodyViewUser and atMak ne 'Y'}">scrollToBody();</c:if>
	window.setTimeout('alertInitMsg()', 300);
});<%
// 본문까지 자동 스크롤 %>
function scrollToBody(){
	var bodyArea = $("#formBodyArea:visible, #bodyHtmlArea:visible, #bodyHtmlViewArea:visible");
	if(bodyArea.length>0){
		window.scrollTo(0,bodyArea.offset().top - 40);
	}
}<%
// 버튼 고정 - 상단 버튼 스크롤 고정 %>
var fixedBtn = {
	fixed:false, btnArea:null, btnNextArea:null, areaTop:null, areaHeight:null,
	setFixed:function(areaId){
		var area = $("#"+areaId);
		if(area.length>0){
			this.btnArea = area;
			this.btnNextArea = area.next();
			
			var p = area.offset();
			this.areaTop = parseInt(p.top);
			this.areaHeight = area.height()+16;
			
			$(window).scroll(function(event){
			    fixedBtn.onScroll(event);
			});
		}
	},
	onScroll:function(event){
		if(!fixedBtn.fixed){
			if(fixedBtn.areaTop - $(window).scrollTop() < 5){
				fixedBtn.btnArea.css('position','fixed').css('top','4px').css('width','${frmYn=='Y'?"100%":"82.8%"}').css('right','0px').css('padding-right','${frmYn=='Y'?"4px":"10px"}').css('background-color','white').css('z-index','1000');
				fixedBtn.btnNextArea.css('margin-top', fixedBtn.areaHeight+'px');
				fixedBtn.fixed = true;
			}
		} else {
			if(fixedBtn.areaTop - $(window).scrollTop() >= 5){
				fixedBtn.btnArea.css('position','').css('top','').css('width','').css('right','').css('padding-right','').css('background-color','').css('z-index','');
				fixedBtn.btnNextArea.css('margin-top', '');
				fixedBtn.fixed = false;
			}
		}
	}
};<%
// onload 메세지 %>
function alertInitMsg(){
	var message = '<u:out value="${message}" type="script" />';
	if(message!='') alert(message);
}<%


///////////////////////////////////////////////////////////////////////////////
//
//
// 이하 - 유통 관련 함수


// 심사요청 버튼 %>
function reqCensr(){
	var opt = {data:{userUid:""},titleId:"ap.title.setPich",userStatCd:"02"};
	if(gOptConfig.censrLoc=='inDept') opt['oneDeptId'] = '${apvData.makDeptId}';
	searchUserPop(opt, function(userVo){
		if(userVo==null){
			alertMsg('or.msg.noUser');
			return false;<%
		// ap.cfrm.setCensr="{0}"을(를) 심사자로 지정 하시겠습니까 ? %>
		} else if(confirmMsg("ap.cfrm.setCensr",[userVo.rescNm])){<%
			// 첨부 파일이 있을 수 있어서 히든 프레임으로 전송함 %>
			var $trxArea = $("#trxDataArea");
			$trxArea.html('');
			$trxArea.append('<input name="pichUid" value="'+userVo.userUid+'" />');
			submitDoc('reqCensr');
		} else {
			return false;
		}
	});
}<%
// 심사 %>
function processCensr(censrStatCd){
	var popTitle = censrStatCd=='rejt' ? '<u:msg titleId="ap.btn.censrRejt" alt="심사반려" />' : '<u:msg titleId="ap.btn.censrApvd" alt="심사승인" />';
	dialog.open("setDocCensrDialog", popTitle, "./setDocCensrPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&censrStatCd="+censrStatCd);
}<%
// 관인 버튼 %>
function setOfse(ofseTypCd){<%// 관인구분코드 - 01:관인(기관), 02:서명인(부서) %>
	var popTitle = ofseTypCd=='01' ? '<u:msg titleId="ap.btn.ofcSeal" alt="관인" />' : '<u:msg titleId="ap.btn.sign" alt="서명인" />';
	dialog.open("setOfseDialog", popTitle, "./setOfsePop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&orgId=${apvData.makDeptId}&ofseTypCd="+ofseTypCd+'&docLangTypCd=${apvData.docLangTypCd}');
}<%
// 발송 버튼 %>
function sendDoc(){
	var $stampArea = $("#senderArea #stampArea");
	if($stampArea.length>0 && $stampArea.find("img").length==0 && $stampArea.attr('data-noImage')!='Y'){<%
		// ap.msg.needSignOrSeal=관인 또는 서명인을 설정해 주십시요.%>
		alertMsg('ap.msg.needSignOrSeal');
		return;
	}
	var refDocCnt = ${fn:length(refApOngdBVoList)};
	if(gOptConfig.sendWithRefDoc=='Y' && refDocCnt>0){<%
		// 발송시 - 참조문서 유지 %>
		dialog.open("setSendDocCfrmDialog", '<u:msg titleId="ap.btn.sendDoc" alt="발송" />', "./setSendDocCfrmPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}");
	} else {<%
		// ap.cfrm.sendDoc=발송 하시겠습니까 ? %>
		if(!confirmMsg('ap.cfrm.sendDoc')) return;
		submitDoc('sendDoc');
	}
}<%
// 시행취소 버튼 %>
function cnclEnfc(){<%
	// ap.cfrm.cnclEnfc=시행취소 하시겠습니까 ? %>
	if(!confirmMsg('ap.cfrm.cnclEnfc')) return;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"cnclEnfc", apvNo:"${not empty param.apvNo ? param.apvNo : param.orgnApvNo}"}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result) toList();
}<%
// 시행변환(기존양식) %>
function transEnfcAsIs(){
	transEnfc();
}<%
// 시행변환(양식선택) %>
function transEnfcNew(formId, formSeq){
	dialog.open('selectFormDialog', '<u:msg titleId="ap.btn.transEnfcNew" alt="시행변환(양식선택)" />', './selectFormPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&forTrans=Y');<%//&formTypCd=trans%>
}<%
// 시행변환 %>
function transEnfc(formId){
	var $form = $("#trxForm");
	if(formId==null){
		$form.find("[name='formId']").val('${apvData.formId}');
		$form.find("[name='formSeq']").val('${apvData.formSeq}');
		$form.submit();
	} else {
		var result = false;
		callAjax("./checkEnfcFormAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {fromFormId:"${apvData.formId}", toFormId:formId}, function(data){
			if(data.message != null) alert(data.message);
			result = data.result == 'ok';
		});
		if(result){
			$form.find("[name='formId']").val(formId);
			$form.find("[name='formSeq']").val('');
			$form.submit();
		}
	}
}<%
// 시행처리 %>
function toToSendBx(){<%
	// ap.cfrm.toToSendBx=시행처리 하시겠습니까 ? %>
	if(!confirmMsg('ap.cfrm.toToSendBx')) return;
	submitDoc('toToSendBx');
}<%
// 목록 가기 %>
function toList(bxId){
	var qstr = $("#queryString").val();
	callAjax("./getBxUrlAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {bxId:(bxId==null || bxId=='' ? '${param.bxId}' : bxId),keepLoc:(bxId==null || bxId=='' ? 'Y' : 'N')}, function(data){
		location.href = data.url+(qstr==null || qstr=='' ? '' : '&'+qstr);
	});
}<%
// 원문보기, 변환문 보기 %>
function changeViewMode(){<%
if("Y".equals(request.getAttribute("frmYn"))){
	// 팝업에서 변환문 보기 %>
	if("${not empty apvData.docPwEnc and apvData.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}"=='Y'){
		parent.dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&vwMode=${vwMode!='trx' ? 'trx' : 'orgn'}${not empty param.refdBy ? '&refdBy='.concat(param.refdBy) : ''}&callback=openSecuDocFrm");
	} else {
		location.replace("./viewDocFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&vwMode=${vwMode!='trx' ? 'trx' : 'orgn'}${not empty param.refdBy ? '&refdBy='.concat(param.refdBy) : ''}");
	}<%
} else {
	// 팝업이아닌 화면에서 변환문 보기 %>
	if("${not empty apvData.docPwEnc and apvData.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}"=='Y'){
		dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&callback=callbackViewMod");
	} else {
		$("#viewModeForm").submit();
	}<%
}%>
}<%
// 보안문서의 변환문/원문 보기 콜백 함수 %>
function callbackViewMod(secuId){
	var $form = $("#viewModeForm");
	$form.find("#secuId").val(secuId);
	$form.submit();
}<%
// 접수확인 %>
function openCfrmRecv(){
	var popTitle = ("${param.bxId=='distRecLst' ? 'Y' : ''}"=='Y') ? '<u:msg titleId="ap.btn.cfrmDist" alt="배부확인" />' : '<u:msg titleId="ap.btn.cfrmRecv" alt="접수확인" />';
	dialog.open('setCfrmRecvDialog', popTitle, './setCfrmRecvPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}');
}<%
// 반송 - 배부함 반송, 접수합 반송 %>
function openDocRetn(){
	dialog.open('setDocRetnDialog', '<u:msg titleId="ap.btn.retn" alt="반송" />', './setDocRetnPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&sendSeq=${param.sendSeq}');
}<%
// 접수 - 분류정보 사용 할 때 %>
function openRecvPop(){
	if(gOptConfig.catEnab=='Y' || gOptConfig.recvRecLstSecuLvl=='Y'){<%
		// 분류정보 + 보안등급 설정 팝업 %>
		var param = '';
		if(gOptConfig.catEnab=='Y' && gOptConfig.recvRecLstSecuLvl=='Y') param = '&withSecul=Y&seculCd=${apvData.seculCd}';
		else if(gOptConfig.catEnab=='Y') param = '';
		else if(gOptConfig.recvRecLstSecuLvl=='Y') param = '&noCls=Y&withSecul=Y&seculCd=${apvData.seculCd}';
		dialog.open('setClsInfoDialog','<u:msg titleId="ap.btn.recv" alt="접수" />','./treeOrgClsPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}'+param);
	} else {<%
		// 접수 진행 %>
		processRecv();
	}
}<%
// 접수 - 분류정보 사용 안할 때  or 분류정보 팝업에서 호출 %>
function processRecv(clsInfoId, seculCd){<%
	// ap.cfrm.recv=접수 하시겠습니까 ? %>
	if(!confirmMsg('ap.cfrm.recv')) return;
	var data = {process:"processRecv", apvNo:"${param.apvNo}", sendSeq:"${param.sendSeq}"};
	if(clsInfoId!=null) data['clsInfoId'] = clsInfoId;
	if(seculCd!=null) data['seculCd'] = seculCd;
	var recvDeptId = '${param.recvDeptId}';
	if(recvDeptId!='') data['recvDeptId'] = recvDeptId;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", data, function(data){
		if(data.message != null) alert(data.message);
		if(data.url != null){<%
			// 접수대장 조회로 전환 %>
			removeApCachedCountMap();
			location.replace(data.url);
		} else if(data.result == 'ok'){<%
			// 접수대장에 권한이 없는 경우 - 목록 보기로 전환 %>
			removeApCachedCountMap();
			toList();
		}
	});
}<%
// 배부 %>
function openDocDist(){
	dialog.open('setDocDistDialog', '<u:msg titleId="ap.btn.dist" alt="배부" /> - <u:msg titleId="ap.doc.recvDeptNm" alt="수신처" />', './setDocDistPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&sendSeq=${param.sendSeq}');
}<%
// 배부 버튼 %>
function processDist(){<%
	// 접수처 데이터 모으기 %>
	var arr = collectDistDept(null, true), result=false;<%// collectDistDept() : setDocDistPop.jsp %>
	if(arr==null) return;<%// 이미 메세지 처리 함 %>
	if(arr.length==0){<%
		// ap.msg.noDistDept=배부처가 지정되지 않았습니다. %>
		alertMsg('ap.msg.noDistDept');
	} else {<%
		// ap.cfrm.dist=배부 하시겠습니까 ? %>
		if(!confirmMsg('ap.cfrm.dist')) return;
		callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"processDist",apvNo:"${param.apvNo}",sendSeq:"${param.sendSeq}",distList:arr}, function(data){
			if(data.message != null) alert(data.message);
			result = data.result == 'ok';
		});
		if(result){
			if("${param.bxId=='distBx' ? 'Y' : 'N'}"=='Y') toList();
			else{
				dialog.close('setDocDistDialog');
			}
		}
	}
}<%
// 담당자 지정 - 접수문서의 담당자를 지정할때 %>
function setMakVw(){
	var opt = {titleId:"ap.title.setPich",userStatCd:"02"};
	var oneDeptId = "${optConfigMap.exPubVwMakerRange == 'Y' ? '' : apvData.recLstDeptId}";
	if(oneDeptId != '') opt['oneDeptId'] = oneDeptId;
	else {
		var orgId = "${sessionScope.userVo.deptId == apvData.recLstDeptId ? '' : apvData.recLstDeptId}";
		if(orgId != '') opt['orgId'] = orgId;
	}
	searchUserPop(opt, function(userVo){
		if(userVo==null){
			alertMsg('or.msg.noUser');
			return false;<%
		// ap.cfrm.setPich="{0}"을(를) 담당자로 지정 하시겠습니까 ? %>
		} else if(confirmMsg("ap.cfrm.setPich",[userVo.rescNm])){
			var result = false;
			callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"setMakVw", apvNo:"${param.apvNo}", apvrUid:userVo.userUid}, function(data){
				if(data.message != null) alert(data.message);
				result = data.result == 'ok';
			});
			if(result) toList();
		} else {
			return false;
		}
	});
}<%
// 공람게시 %>
function regPubBx(){
	dialog.open("setPubBxDialog", '<u:msg titleId="ap.btn.regPubBx" alt="공람게시" />', "./setPubBxPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNos=${param.apvNo}");
}<%
// 공람완료 %>
function setCmplVw(){<%
	// ap.cfrm.cmplVw=공람완료 하시겠습니까 ? %>
	if(confirmMsg("ap.cfrm.cmplVw")){
		var result = false;
		callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"setCmplVw", apvNo:"${param.apvNo}"}, function(data){
			if(data.message != null) alert(data.message);
			result = data.result == 'ok';
		});
		if(result) toList();
	}
}<%
// 참조기안 - 양식선택 팝업 - 접수대장 에서 %>
function makeRefDoc(){
	var erpForm = "${not empty apErpFormBVo ? 'Y' : ''}";
	if(erpForm == 'Y'){
		${frmYn=='Y'? 'parent.' : ''}location.href = "./setDoc.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&refDocApvNo=${param.apvNo}&formId=${apvData.formId}";
	} else {
		dialog.open('selectFormDialog', '<u:msg alt="양식선택" titleId="ap.btn.selectForm" />', './selectFormPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&refDocApvNo=${param.apvNo}');
	}
}<%
// 대장등록 %>
function regRegRecLst(){<%
	// ap.cfrm.regRegRecLst=대장등록 하시겠습니까 ? %>
	if(!confirmMsg('ap.cfrm.regRegRecLst')) return;
	var result = false;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"regRegRecLst", apvNo:"${param.apvNo}"}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result) reload();
}<%
// 비밀번호 해제 %>
function delDocPw(){<%
	// ap.cfrm.delDocPw=비밀번호 해제 하시겠습니까 ? %>
	if(!confirmMsg('ap.cfrm.delDocPw')) return;
	var result = false;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"delDocPw", apvNo:"${param.apvNo}"}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result) reload();
}<%
// 시스템 반려 %>
function forceRejt(){<%// ap.cfrm.forceRejt=시스템 반려 하시겠습니까 ?%>
	if(!confirmMsg('ap.cfrm.forceRejt')) return;
	var result = false;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"forceRejt", apvNo:"${param.apvNo}"}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result) reload();
}<%
// 공람확인 %>
function listPubBxVw(){
	dialog.open("listPubBxVwDialog", '<u:msg alt="공람확인" titleId="ap.btn.listPubBxVw" />', "./listPubBxVwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&pubBxDeptId=${param.pubBxDeptId}");
}<%
// 이전문서, 다음문서 %>
var gApvNoForNext=null, gApvLnPnoForNext=null, gApvLnNoForNext=null, gAddPageNo=null; gNextCallbackCalled=false;<%
// 이전문서 %>
function moveToPrevDoc(noMsg){
	var apvNo = "${prevApOngdBVo.apvNo}";
	var apvLnPno = "${prevApOngdBVo.apvLnPno}";
	var apvLnNo = "${prevApOngdBVo.apvLnNo}";
	var addPageNo = "${prevDocAtPrevPage}";
	var pwYn = "${not empty prevApOngdBVo.docPwEnc ? 'Y' : ''}";
	var subj = "<u:out value='${prevApOngdBVo.docSubj}' type='script' />";
	if(apvNo==''){
		if(noMsg==true) toList();
		else alertMsg('ap.msg.noPrevDoc');<%//ap.msg.noPrevDoc=이전 문서가 없습니다.%>
	} else {
		moveToPrevNext(apvNo, apvLnPno, apvLnNo, addPageNo, pwYn, '', subj, noMsg);
	}
}<%
// 다음문서 %>
function moveToNextDoc(noMsg){
	if($("#docDiv #docBtnArea .front_left").length==0){
		toList();
		return;
	}
	var apvNo = "${nextApOngdBVo.apvNo}";
	var apvLnPno = "${nextApOngdBVo.apvLnPno}";
	var apvLnNo = "${nextApOngdBVo.apvLnNo}";
	var addPageNo = "${nextDocAtNextPage}";
	var pwYn = "${not empty nextApOngdBVo.docPwEnc ? 'Y' : ''}";
	var subj = "<u:out value='${nextApOngdBVo.docSubj}' type='script' />";
	if(apvNo==''){
		if(noMsg==true) toList();
		else alertMsg('ap.msg.noNextDoc');<%//ap.msg.noNextDoc=다음 문서가 없습니다.%>
	} else {
		moveToPrevNext(apvNo, apvLnPno, apvLnNo, addPageNo, pwYn, '', subj, noMsg);
	}
}<%
// 이전문서, 다음문서 - callback %>
function callbackPrevNext(secuId){
	gNextCallbackCalled = true;
	moveToPrevNext(gApvNoForNext, gApvLnPnoForNext, gApvLnNoForNext, gAddPageNo, '', secuId);
}<%
// 이전문서, 다음문서 %>
function moveToPrevNext(apvNo, apvLnPno, apvLnNo, addPageNo, pwYn, secuId, subj, noMsg){
	if(apvNo=='') toList();
	else if(pwYn=='Y'){
		gApvNoForNext = apvNo;
		gAddPageNo = addPageNo;
		gApvLnPnoForNext = apvLnPno;
		gApvLnNoForNext = apvLnNo;
		
		var popTitle = '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />';
		if(secuId!=null){
			popTitle += ' - [<u:msg titleId="ap.doc.docSubj" alt="제목" />]'+subj;
			if(popTitle.length>37) popTitle = popTitle.substring(0,35)+'..';
		}
		dialog.open("setDocPwDialog", popTitle, "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&callback=callbackPrevNext&apvNo="+apvNo);
		dialog.onClose('setDocPwDialog', function(){
			if(noMsg && !gNextCallbackCalled){<%// 결재 후 다음문서가 보안문서 이고 비밀번호 취소 시키면 목록으로 이동하도록 함 %> 
				toList();
			}
		});
	} else {
		var $form = $("#prevNextForm");
		$form.find("#apvNo").val(apvNo);
		if(apvLnPno==null || apvLnPno=='') $form.find("#apvLnPno").remove();
		else $form.find("#apvLnPno").val(apvLnPno);
		if(apvLnNo==null || apvLnNo=='') $form.find("#apvLnNo").remove();
		else $form.find("#apvLnNo").val(apvLnNo);
		
		if(addPageNo!=''){
			var $qstr = $form.find("[name='queryString'], [name='pltQueryString']");
			var qstr = $qstr.val();
			var p = qstr.indexOf('pageNo=');
			var q = qstr.indexOf('&',p+1);
			var strNo = (q>p) ? qstr.substring(p+7, q) : qstr.substring(p+7);
			qstr =  qstr.substring(0,p+7)+(parseInt(strNo)+parseInt(addPageNo))+(q>p ? qstr.substring(q) : '');
			$qstr.val(qstr);
		}
		if(secuId!=null){
			$form.appendHidden({'name':'secuId','value':secuId});
		}
		$form.submit();
	}
}<%
// 삭제 %>
function deleteDoc(){
	if(!confirmMsg('cm.cfrm.del')) return;
	var result = false;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"delDocs", apvNos:'${param.apvNo}'}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result) toList();
}<%
// 문서관리 보내기 %>
function sendDocToDm(){
	var param = new ParamMap();
	param.put('seculCd','${apvData.seculCd}');
	param.put('docKeepPrdCd','${apvData.docKeepPrdCd}');
	param.put('ownrUid','${apvData.makrUid}');
	var sendToDm = $('#setDocInfoForm #sendToDm').val();
	sendDocOptPop('callbackSendToDm', param);
}<%
// 문서관리 콜백 %>
function callbackSendToDm(dmData){
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"sendToDm", apvNos:'${param.apvNo}', bxId:"${param.bxId}", dmData:dmData}, function(data){
		if(data.message != null) alert(data.message);
		if(data.result == 'ok'){
		}
	});
}<%
// 경로조회(결재라인 목록형 에서 호출:viewApvLnInc.jsp) - apvLnPno 가 없으면 "전체경로", 있으면 해당 서브 경로
// 주로 viewDocDetlPop.jsp 에서 사용되며, 본문에 목록형 결재라인이 추가되어 이곳으로 스크립트 옮겨옴 %>
function viewApvLnPop(apvNo, apvLnPno, deptNm){
	var popTitle = apvLnPno==null ? '<u:msg alt="전체경로" titleId="ap.btn.allApvLn" />' : '<u:msg alt="결재 경로" titleId="ap.jsp.apvLn" />' + (deptNm==null ? '' : ' - '+deptNm);
	var popId = apvLnPno==null ? 'viewApvLnDialog' : 'viewSubApvLnDialog';
	${frmYn=='Y'? 'parent.' : ''}dialog.open(popId, popTitle, "./viewApvLnPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo+(apvLnPno==null ? '' : '&apvLnPno='+apvLnPno));
}<%
// 전표보기 %>
function popViewChit(){
	var previewEnable = "${empty previewEnable ? '' : 'Y'}";
	if(previewEnable=='Y'){
		window.open('./view${not empty dmUriBase ? "Ap" : ""}DocFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}${empty param.apvNo ? "&intgNo=".concat(param.intgNo) : "&apvNo=".concat(param.apvNo)}&intgTypCd=ERP_CHIT&winPop=Y', 'chitDocPop', 'history=no,status=no,scrollbars=yes,menuebar=no,resizable=yes,top=50,left=50,width=920, height=720');
	} else {
		${frmYn=='Y'? 'parent.' : ''}dialog.open2('viewDocDialog','<u:msg alt="전표보기" titleId="ap.btn.viewChit" />','./view${not empty dmUriBase ? "Ap" : ""}DocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}${empty param.apvNo ? "&intgNo=".concat(param.intgNo) : "&apvNo=".concat(param.apvNo)}&intgTypCd=ERP_CHIT', {padding:{right:"3px",top:"5px"}});
	}
}<%
// 개인분류 - 분류변경 - 문서의 분류를 변경 하는 것 - 개인분류 트리 팝업 %>
function chngPsnCls(){
	dialog.open("setChngPsnClsDialog", '<u:msg alt="개인분류변경" titleId="ap.btn.chngPsnCls" />', "./treePsnClsPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&callback=callbackChngPsnCls");
}<%
// 개인분류 - 분류변경 - callback:chngPsnCls %>
function callbackChngPsnCls(){
	if(gClsId=='ROOT' || gClsId==null){
		alertMsg('ap.msg.chooseCls');<%//ap.msg.chooseCls=분류정보를 선택해 주십시요.%>
		return;
	}
	if(!confirmMsg('ap.cfrm.moveDocToCls', [gClsNm])){<%//ap.cfrm.moveDocToCls=분류 "{0}"(으)로 문서를 이동하시겠습니까 ?%>
		return;
	}
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"chngPsnCls", psnClsInfoId:gClsId, apvNos:"${param.apvNo}"}, function(data){
		if(data.message != null) alert(data.message);
		if(data.result == 'ok'){
			alertMsg("cm.msg.modify.success");
		}
	});
	dialog.close('setChngPsnClsDialog');
}<%
// ERP 양식 결재 - xml 세팅 %>
function setErpXML(){
	var $xmlArea = $("#xmlArea");
	if($xmlArea.length>0 && '${atMak}'=='Y'){
		var formBodyXML = '';
		if(window.collectFormBodyXML){
			formBodyXML = collectFormBodyXML();
			if(formBodyXML==null) return false;
		} else if(window.checkFormBodyXML && checkFormBodyXML()==false){
			return false;
		} else {
			formBodyXML = getFormBodyXML();
		}
		
		setErpXMLByValue(formBodyXML, false);
	}
}<%
// ERP 양식 결재 - xml 세팅 - 팝업 창에서 %>
function setErpXMLPop(){
	var formBodyXML = '';
	if(window.collectFormBodyXML){
		formBodyXML = collectFormBodyXML();
		if(formBodyXML==null) return false;
	} else if(window.checkFormBodyXML && checkFormBodyXML()==false){
		return false;
	} else {
		formBodyXML = getFormBodyXML();
	}
	dialog.close("setDocBodyHtmlDialog");
	
	setErpXMLByValue(formBodyXML, true);
}<%
// xml 히든값 세팅 및, html 변환 세팅 %>
function setErpXMLByValue(formBodyXML, applyHtmlArea){
	var $xmlDataArea = $("#formBodyXMLDataArea");
	$xmlDataArea.html('');
	if($("#docArea #bodyHtmlViewArea").length==0){
		$xmlDataArea.appendHidden({'name':'noBodyHtml','value':'Y'});
	}
	
	var ieVer8 = (browser.ie==true && browser.ver<9);
	if(ieVer8){
		var escapeVa = formBodyXML;
		escapeVa = escapeVa.replaceAll("&quot;", "&amp;quot;");
		escapeVa = escapeVa.replaceAll("&#10;",  "\r\n");
		$xmlDataArea.appendHidden({'name':'formBodyXML','value':escapeVa});
	} else {
		$xmlDataArea.appendHidden({'name':'formBodyXML','value':formBodyXML});
	}
	
	var param = {erpFormId:'${apErpFormBVo.erpFormId}',formBodyXML:(formBodyXML==null ? '' : formBodyXML)};
	var formBodyHTML = callHtml('./getErpFormHtmlAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}', param);
	
	$xmlDataArea.appendHidden({'name':'formBodyHTML','value':formBodyHTML});
	if(applyHtmlArea){
		$('#formBodyArea').html(formBodyHTML+'\r\n<div class="blank"></div>');
	}
}<%
// [버튼] 본문수정 - ERP 양식 결재 - 내용 수정 %>
function editFormBodyDetl(){
	var erpFormTypCd = "${apErpFormBVo.erpFormTypCd}";
	if(erpFormTypCd == 'wfForm'){
		var $wfFormDataArea = $("#docForm #wfFormDataArea");
		var wfWorkNo = $wfFormDataArea.find("[name='wfWorkNo']").val();
		if(wfWorkNo == null) wfWorkNo = '${empty wfWorkNoHold ? wfWorkNo : wfWorkNoHold}';
		var param = {formId:'${apvData.formId}',wfFormNo:"${wfFormNo}",wfWorkNo:wfWorkNo};
		var url = "./setWfFormBodyPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}";
		dialog.open("setDocBodyHtmlDialog", '<u:msg titleId="ap.btn.modCont" alt="내용수정" />', url, param);
		dialog.onClose("setDocBodyHtmlDialog", function(){ if(window.clearWfEditor){clearWfEditor();} });
	} else {
		var formBodyXML = $("#formBodyXMLDataArea input[name='formBodyXML']").val();
		var param = {erpFormId:'${apErpFormBVo.erpFormId}',formBodyXML:(formBodyXML==null ? '' : formBodyXML)};
		var url = "./setFormBodyXMLPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${not empty param.orgnApvNo ? param.orgnApvNo : param.apvNo}";
		dialog.open("setDocBodyHtmlDialog", '<u:msg titleId="ap.btn.modCont" alt="내용수정" />', url, param);
		dialog.onClose("setDocBodyHtmlDialog", function(){ if(window.clearFormEditor){clearFormEditor();} });
	}
}<%--
// 업무관리 - 선저장 & html 처리  --%>
function setWfFormBody($wfFormBodyArea, isPop){
	var $wfFormDataArea = $("#docForm #wfFormDataArea");
	var wfWorkNo = $wfFormDataArea.find("[name='wfWorkNo']").val();
	var wfGenId  = $wfFormDataArea.find("[name='wfGenId']").val();
	if(wfWorkNo == null) wfWorkNo = "";
	if(wfGenId == null)  wfGenId  = "${wfGenId}";
	
	var rObj = saveToWorkNo({workNo:wfWorkNo, genId:wfGenId}, $wfFormBodyArea, $wfFormDataArea);
	if(rObj == null) return false;
	
	var param = {wfFormNo:'${wfFormNo}', wfWorkNo:rObj.workNo, wfGenId:rObj.genId, wfMode:"view"};
	var formBodyHTML = callHtml('./getWfFormHtmlAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}', param);
	
	['wfWorkNo','wfWorkNo','wfGenId','wfFormDataArea','formBodyHTML'].each(function(idx, obj){
		$wfFormDataArea.find("[name='"+obj+"']").remove();
	});
	$wfFormDataArea.appendHidden({'name':'wfFormNo','value':'${wfFormNo}'});
	$wfFormDataArea.appendHidden({'name':'wfWorkNo','value':rObj.workNo});
	$wfFormDataArea.appendHidden({'name':'wfGenId' ,'value':rObj.genId});
	$wfFormDataArea.appendHidden({'name':'noBodyHtml','value':'Y'});
	$wfFormDataArea.appendHidden({'name':'formBodyHTML','value':formBodyHTML});
	
	if(isPop == true){
		$("#docForm #wfFormBodyArea").html(formBodyHTML);
	}
	return true;
}<%
// 양식결재(formBodyXML) 관련 %>
function getFormBodyXML(){
	var xmlDoc = createXMLParser("<formBodyXML></formBodyXML>");
	var root = xmlDoc.getElementsByTagName("formBodyXML")[0];
	
	var head = xmlDoc.createElement("head");
	root.appendChild(head);
	
	var body = xmlDoc.createElement("body");
	root.appendChild(body);
	
	var $popArea = $("#setDocBodyHtmlDialog");
	var $area = $popArea.length==0 ? $("#xmlArea") : $popArea.find("#xmlArea");
	$area.find("[id='xml-head'] :input").each(function(){
		$(head).attr($(this).attr('name'), $(this).val());
	});
	
	var ieVer8 = (browser.ie==true && browser.ver<9);
	
	var elemId, loopId=null, p;
	$area.find("[id='xml-body'] [id^='xml-']").each(function(){
		elemId = $(this).attr('id').substring(4);
		p = elemId.indexOf('/');
		if(p>0){
			loopId = elemId.substring(p+1);
			elemId = elemId.substring(0,p);
			
			var pElem = xmlDoc.createElement(elemId), elem;
			body.appendChild(pElem);
			
			$(this).find("[id='"+loopId+"']").each(function(){
				var elem = xmlDoc.createElement(loopId), tp, attNm, attVa;
				var valid = true;
				$(this).find(":input").each(function(){
					if(valid){
						tp = $(this).attr('type');
						tp = tp==null ? '' : tp.toLowerCase();
						if(tp=='radio'||tp=='checkbox'){
							if(!this.checked) return;
						}
						attNm = $(this).attr('name');
						attVa = $(this).val();
						if(attNm!=null && attNm.startsWith('erp')){
							if($(this).attr('data-validation')=='Y' && attVa==''){
								valid = false;
							} else {
								$(elem).attr($(this).attr('name'), attVa);
								if(this.tagName.toLowerCase()=='select'){
									$(elem).attr(attNm+'Nm', $(this).find("option:selected").text());
								}
							}
						}
					}
				});
				if(valid){
					pElem.appendChild(elem);
				}
			});
		} else {
			var elem = xmlDoc.createElement(elemId);
			body.appendChild(elem);
			var tp, attNm, attVa;
			$(this).find(":input").each(function(){
				tp = $(this).attr('type');
				tp = tp==null ? '' : tp.toLowerCase();
				if(tp=='radio'||tp=='checkbox'){
					if(!this.checked) return;
				}
				attNm = $(this).attr('name');
				attVa = $(this).val();
				if(attNm!=null && attNm.startsWith('erp')){
					
					if($(this).attr('data-validation')=='Y' && attVa==''){
						valid = false;
					} else {
						$(elem).attr($(this).attr('name'), attVa);
						if(this.tagName.toLowerCase()=='select'){
							$(elem).attr(attNm+'Nm', $(this).find("option:selected").text());
						}
					}
				}
			});
		}
	});
	if(ieVer8){
		var xml = serializeXML(xmlDoc).trim();
		xml = xml.replaceAll("\r\n", "&#10;");
		xml = xml.replaceAll("'",    "&apos;");
		return xml;
	} else {
		return serializeXML(xmlDoc);
	}
}
function createXMLParser(xmlString){
	if(window.DOMParser){
		var parser = new DOMParser();
		return parser.parseFromString(xmlString, "text/xml");
	} else {
		var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.async = false;
		xmlDoc.loadXML(xmlString);
        return xmlDoc;
	}
}
function serializeXML(xmlDoc){
	if(window.XMLSerializer){
		var serializer = new XMLSerializer();
		return serializer.serializeToString(xmlDoc);
	} else {
		return xmlDoc.xml;
	}
}
function setDateToDocSubj(dt){
	var $form = $("#docForm");
	var $docInfoArea = $form.children("#docDataArea").children("#docInfoArea");
	var formNm = $docInfoArea.find("[name='formNm']").val(), formNmVa='';
	if(dt==null || dt==''){
		formNmVa = formNm;
	} else {
		formNmVa = formNm+' ('+dt+')';
	}
	$docInfoArea.find("[name='docSubj']").val(formNmVa);
	
	$form.children("#docArea").children().each(function(){
		if($(this).attr('id')=='itemsArea'){
			$(this).find("td[id='docSubjView']").text(formNmVa);
		}
	});
}<%
// 참조열람 %>
function openRefVw(){
	var modified =  $("#docDataArea #docRefVwArea").attr('data-modified') == 'Y';
	var popTitle = '<u:term termId="ap.term.refVw" alt="참조열람" />';
	dialog.open("setRefVwDialog", popTitle, "./setRefVwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}${empty param.apvNo ? '' : '&apvNo='.concat(param.apvNo)}${atMak eq 'Y' ? '&statCd=mak' : ''}"+(modified ? "&modified=Y" : ""));
}<%
// 참조열람 세팅된 데이터 조회 %>
function getRefVwList(){
	var arr = [];
	$("#docDataArea #docRefVwArea input[name='refVw']").each(function(){
		arr.push(JSON.parse(this.value));
	});
	return arr.length==0 ? null : arr;
}<%
// 참조열람 열람확인 %>
function cfrmRefVw(){
	var popTitle = '<u:term termId="ap.term.refVw" alt="참조열람" />';
	dialog.open("setRefVwOpinDialog", popTitle, "./setRefVwOpinPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}");
}<%
// 추가검토 - 조직도 열기 %>
var gAdiRevwr = null;
function setAdiRevw(){
	gAdiRevwr = null;
	var opt = {data:{userUid:""},titleId:"ap.cfg.adiRevw",userStatCd:"02",apvrRoleCds:"revw${apFormBVo.formApvLnTypCd eq 'apvLnDbl' or apFormBVo.formApvLnTypCd eq 'apvLnDblList' ? '' : ',psnOrdrdAgr'}${optConfigMap.revw2Enable eq 'Y' ? ',revw2' : ''}${optConfigMap.revw3Enable eq 'Y' ? ',revw3' : ''}"};
	if('Y'=='${optConfigMap.apvrFromOtherComp}') opt['foreign'] = 'Y';
	searchUserPop(opt, function(userVo){
		if(userVo==null){
			alertMsg('or.msg.noUser');
			return false;
		} else {
			gAdiRevwr = userVo;
			window.setTimeout('openAdiRevwOpin()',10);
		}
	});
}<%
// 추가검토 의견창 열기 %>
function openAdiRevwOpin(){
	var popTitle = '<u:term termId="ap.cfg.adiRevw" alt="추가검토" />';
	dialog.open("setAdiRevwOpinDialog", popTitle, "./setRefVwOpinPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&callback=callbackAdiRevw");
}<%
//추가검토 의견창 - 콜백 %>
function callbackAdiRevw(opin){
	var result = false;
	var param = {process:"setAdiRevwr", apvNo:"${param.apvNo}", apvLnPno:"${myTurnApOngdApvLnDVo.apvLnPno}", apvLnNo:"${myTurnApOngdApvLnDVo.apvLnNo}", dblApvTypCd:"${myTurnApOngdApvLnDVo.dblApvTypCd}", apvrUid:gAdiRevwr.userUid, apvrRoleCd:gAdiRevwr.apvrRoleCd, apvOpinCont:opin};
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", param, function(data){
		if(data.message != null) alert(data.message);
		result = (data.result == 'ok');
		dialog.close("setAdiRevwOpinDialog");
	});
	if(result) moveToNextDoc();
}<%--
// 대외공문 --%>
function viewExtDoc(){
	$("#viewExtDocForm").submit();
}<%--
// 완결 문서 취소 --%>
function cancelCmpl(){
	<%-- ap.cfm.cancelCmpl=문서의 승인을 취소 하시겠습니까 ? --%>
	if(confirmMsg("ap.cfm.cancelCmpl")){
		var result = false;
		var param = {process:"cancelCmpl", apvNo:"${param.apvNo}"};
		callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", param, function(data){
			if(data.message != null) alert(data.message);
			result = (data.result == 'ok');
		});
		if(result) toList();
	}
}
//-->
</script></c:if><c:if

		test="${not empty downDocYn}">
<script type="text/javascript">
var NO_SCRIPT = true;
var MOBILE_VIEW = false;
</script></c:if>

<div id="docDiv" style="${winPop=='Y'?'padding-left:4px;':''}"><c:if
	test="${frmYn ne 'Y'}"><%

// 상단메뉴 %><c:if
	test="${atMak eq 'Y'}"><u:title titleId="ap.jsp.setDoc" alt="문서 작성" notPrint="true" /></c:if><c:if
	test="${atMak ne 'Y'}"><u:title titleId="ap.jsp.viewDoc" alt="문서 조회" notPrint="true" /></c:if></c:if><c:if
	
	test="${empty downDocYn}">
<div id="docBtnArea" style="margin-top:-5px;">
<div style="height:5px; background-color:white;"></div>
<div class="front notPrint"><c:if
		test="${frmYn ne 'Y' and (not empty prevApOngdBVo or not empty nextApOngdBVo)}">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td class="frontico"><u:buttonIcon titleId="ap.btn.prevDoc" alt="이전 문서" image="ico_left.png"
			onclick="moveToPrevDoc();" /></td>
		<td class="width5"></td>
		<td class="frontico"><u:buttonIcon titleId="ap.btn.nextDoc" alt="다음 문서" image="ico_right.png"
			onclick="moveToNextDoc();" /></td>
		</tr>
		</tbody></table>
	</div></c:if>
	<div class="front_right">
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr><c:if
		
			test="${not empty apFormJspDVo.jspPath}">
		<td class="frontbtn"><u:buttonS titleId="ap.cfg.extDoc" alt="대외공문" href="javascript:viewExtDoc();" spanStyle="${optConfigMap.empsApvBtn eq 'Y' ? 'color:#0441B3; font-weight:bold;' : ''}"
			/></td></c:if><c:if
		
			test="${not empty apCnclApvEnable
				and (param.bxId eq 'admRegRecLst' or param.bxId eq 'admApvdBx')
				and apvData.enfcStatCd ne 'sent'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.cancelCmpl" alt="완결 취소" href="javascript:cancelCmpl();"
			/></td></c:if><u:set
				
				test="${optConfigMap.erpBodyLock eq 'Y'}" var="erpBodyLock" value="Y" elseValue="${erpBodyLock}"
				cmt="본문 잠금 - 결재 옵션처럼 잠금기능 하도록 - one card 적용" /><c:if
		
		
		
			test="${ atMak ne 'Y' and atWait == 'Y' and param.bxId eq 'waitBx' and param.intgTypCd ne 'ERP_CHIT' }"><%
				// 기안이 아니고 & 내 차례 이고 & 대기함 이면  - (승인/반려), (찬성/반대) - 버튼 디스플레이 조절
				//    - 주결재라인(apvLnPno=='0') 이고, 검토,결재,전결 - (승인/반려)
				//    - 주결재라인(apvLnPno!='0') 아니고, 개인순차합의, 개인병렬합의 - (찬성,반대)
				%><c:if
				test="${ myTurnApOngdApvLnDVo.apvLnPno=='0'
					and ( myTurnApOngdApvLnDVo.apvrRoleCd=='revw'
						or myTurnApOngdApvLnDVo.apvrRoleCd=='revw2'
						or myTurnApOngdApvLnDVo.apvrRoleCd=='revw3'
						or myTurnApOngdApvLnDVo.apvrRoleCd=='apv'
						or myTurnApOngdApvLnDVo.apvrRoleCd=='pred')}"
				>
		<u:secu auth="W"
		><td class="frontbtn"><u:buttonS termId="ap.term.apvd" alt="승인" href="javascript:openProsStat('apvd');" spanStyle="${optConfigMap.empsApvBtn eq 'Y' ? 'color:#0441B3; font-weight:bold;' : ''}" /></td>
		<td class="frontbtn"><u:buttonS termId="ap.term.rejt" alt="반려" href="javascript:openProsStat('rejt');" spanStyle="${optConfigMap.empsApvBtn eq 'Y' ? 'color:#0441B3; font-weight:bold;' : ''}" /></td><c:if
			test="${rejtPrevEnab =='Y'}">
		<td class="frontbtn"><u:buttonS termId="ap.term.reRevw" alt="재검토" href="javascript:openProsStat('reRevw');" spanStyle="${optConfigMap.empsApvBtn eq 'Y' ? 'color:#0441B3; font-weight:bold;' : ''}" /></td></c:if>
		<td class="width5"></td>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.hold" alt="보류" href="javascript:saveDoc('hold');" /></td></u:secu></c:if><c:if
		
				test="${ ( myTurnApOngdApvLnDVo.apvLnPno!='0'
					or (myTurnApOngdApvLnDVo.apvrRoleCd=='psnOrdrdAgr'
						or myTurnApOngdApvLnDVo.apvrRoleCd=='psnParalAgr')) and writeAuth == 'Y' }">
		<td class="frontbtn"><u:buttonS termId="ap.term.pros" alt="합의승인" href="javascript:openProsStat('pros');" spanStyle="${optConfigMap.empsApvBtn eq 'Y' ? 'color:#0441B3; font-weight:bold;' : ''}" /></td><c:if
			test="${optConfigMap.noConsAtAgr ne 'Y'}">
		<td class="frontbtn"><u:buttonS termId="ap.term.cons" alt="합의반대" href="javascript:openProsStat('cons');" spanStyle="${optConfigMap.empsApvBtn eq 'Y' ? 'color:#0441B3; font-weight:bold;' : ''}" /></td></c:if><c:if
					test="${optConfigMap.rejtWhenPsnOrdrdAgr=='Y' and myTurnApOngdApvLnDVo.apvrRoleCd=='psnOrdrdAgr'}">
		<td class="frontbtn"><u:buttonS termId="ap.term.rejt" alt="반려" href="javascript:openProsStat('rejt');" spanStyle="${optConfigMap.empsApvBtn eq 'Y' ? 'color:#0441B3; font-weight:bold;' : ''}" /></td></c:if><c:if
					test="${rejtPrevEnab =='Y'}">
		<td class="frontbtn"><u:buttonS termId="ap.term.reRevw" alt="재검토" href="javascript:openProsStat('reRevw');" spanStyle="${optConfigMap.empsApvBtn eq 'Y' ? 'color:#0441B3; font-weight:bold;' : ''}" /></td></c:if>
		<td class="width5"></td>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.hold" alt="보류" href="javascript:saveDoc('hold');" /></td></c:if></c:if><c:if
		
		
			test="${param.intgTypCd ne 'ERP_CHIT' and apvData.intgTypCd eq 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS alt="전표보기" titleId="ap.btn.viewChit" href="javascript:popViewChit();" /></c:if><c:if
			
			test="${param.bxId eq 'myBx' and optConfigMap.psnCatEnab == 'Y' and not empty param.apvNo and apvData.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y'}">
		<td class="frontbtn"><u:buttonS alt="개인분류변경" titleId="ap.btn.chngPsnCls" href="javascript:chngPsnCls();" /></td></c:if><c:if
			
			test="${param.bxId eq 'pubBx' and param.intgTypCd ne 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS alt="공람확인" titleId="ap.btn.listPubBxVw" href="javascript:listPubBxVw();" /></td></c:if><c:if
		
			test="${param.bxId eq 'myBx' and apvData.docStatCd == 'apvd' and apvData.regRecLstRegYn == 'N' and writeAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.regRegRecLst" alt="대장등록" href="javascript:regRegRecLst();" /></td></c:if><c:if
		
			test="${param.bxId eq 'myBx' and apvData.docStatCd == 'apvd' and not empty apvData.docPwEnc and writeAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.delDocPw" alt="비밀번호 해제" href="javascript:delDocPw();" /></td></c:if><c:if
		
			test="${param.bxId eq 'admOngoBx' and apvData.docProsStatCd eq 'ongo' and (sessionScope.userVo.userUid eq 'U0000001' or (not empty apSysRejtEnable and adminAuth eq 'Y'))}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.forceRejt" alt="시스템 반려" href="javascript:forceRejt();" /></td></c:if><c:if
		
			test="${param.bxId eq 'distBx' and adminAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.dist" alt="배부" href="javascript:openDocDist();" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.retn" alt="반송" href="javascript:openDocRetn();" /></td></c:if><c:if
		
			test="${param.bxId eq 'distRecLst' and adminAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.cfrmDist" alt="배부확인" href="javascript:openCfrmRecv();" /></td><c:if
					test="${empty storage and param.intgTypCd ne 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.reDist" alt="재배부" href="javascript:openDocDist();" /></td></c:if></c:if><c:if
		
			test="${param.bxId eq 'recvBx' and adminAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.recv" alt="접수" href="javascript:${optConfigMap.catEnab=='Y' or optConfigMap.recvRecLstSecuLvl=='Y' ? 'openRecvPop()' : 'processRecv()'};" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.retn" alt="반송" href="javascript:openDocRetn();" /></td></c:if><c:if
		
			test="${(param.bxId eq 'recvRecLst' and apvData.docStatCd == 'recv' and adminAuth == 'Y' and empty dmUriBase)
				or (param.bxId eq 'waitBx' and myTurnApOngdApvLnDVo.apvrRoleCd=='makVw' and writeAuth == 'Y')}"><c:if
				
					test="${empty storage and param.intgTypCd ne 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.setPich" alt="담당자지정" href="javascript:setMakVw();" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.setApvLn" alt="결재선지정" href="javascript:openApvLn();" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.doc.opin" alt="의견" href="javascript:openOpin();" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.subm" alt="상신" href="javascript:openProsStat('makVw');" /></td></c:if></c:if><c:if
		
		
		
			test="${param.bxId eq 'waitBx' and writeAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT'}"><c:if
		
			test="${myTurnApOngdApvLnDVo.apvrRoleCd=='makVw'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.cmplVw" alt="공람완료" href="javascript:setCmplVw();" /></td></c:if><c:if
		
			test="${myTurnApOngdApvLnDVo.apvrRoleCd=='fstVw'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.setApvLn" alt="결재선지정" href="javascript:openApvLn();" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.term.fstVw" alt="선람" href="javascript:openProsStat('fstVw');" /></td></c:if><c:if
		
			test="${myTurnApOngdApvLnDVo.apvrRoleCd=='pubVw'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.setApvLn" alt="결재선지정" href="javascript:openApvLn();" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.term.pubVw" alt="공람" href="javascript:openProsStat('pubVw');" /></td></c:if><c:if
		
			test="${myTurnApOngdApvLnDVo.apvrRoleCd=='paralPubVw'}">
		<td class="frontbtn"><u:buttonS titleId="ap.term.pubVw" alt="공람" href="javascript:openProsStat('pubVw');" /></td></c:if></c:if><c:if
		
		
		
			test="${(param.bxId eq 'recvRecLst' or param.bxId eq 'apvdBx' or param.bxId eq 'myBx')
				and apvData.docProsStatCd eq 'apvd' and empty param.orgnApvNo and param.vwMode ne 'trx'
				and writeAuth eq 'Y' and empty param.refDocApvNo and frmYn ne 'Y'
				and empty dmUriBase and empty storage and param.intgTypCd ne 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.refMak" alt="참조기안" href="javascript:makeRefDoc();" /></td></c:if><c:if
		
			test="${vwMode == 'trx' and param.bxId ne 'recvBx' and param.bxId ne 'distBx' and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.vwOrgnDoc" alt="원문보기" href="javascript:changeViewMode();" /></td></c:if><c:if
		
			test="${vwMode != 'trx' and not empty apvData.trxApvNo and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.vwTrxDoc" alt="변환문보기" href="javascript:changeViewMode();" /></td></c:if><c:if
		
		
			test="${param.bxId eq 'myBx' and apvData.enfcStatCd == 'befoEnfc' and writeAuth == 'Y' and empty storage and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y'}"><c:if
				test="${optConfigMap.enabCensr == 'Y'}"><u:cmt cmt="[옵션] 심사 사용 이면" />
		<td class="frontbtn"><u:buttonS titleId="ap.btn.reqCensrOpin" alt="심사요청의견" href="javascript:openOpin('censr');" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.reqCensr" alt="심사요청" href="javascript:reqCensr();" /></td></c:if><c:if
				test="${optConfigMap.enabCensr != 'Y' and optConfigMap.sendFrom == 'toSendBx'}"><u:cmt
					cmt="[옵션] 심사 사용안함 & 발송함에서 발송이면" />
		<td class="frontbtn"><u:buttonS titleId="ap.btn.toToSendBx" alt="시행처리" href="javascript:toToSendBx();" /></td></c:if><c:if
				test="${optConfigMap.enabCensr != 'Y' and optConfigMap.sendFrom == 'myBx' and vwMode != 'orgn'}"><u:cmt
					cmt="[옵션] 심사 사용안함 & 기안함에서 발송이면" /><c:if
					test="${(apvData.enfcScopCd=='for' or apvData.enfcScopCd=='both')
						or optConfigMap.alwIntroOfcSeal=='Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.ofcSeal" alt="관인" href="javascript:setOfse('01');" /></td></c:if><c:if
					test="${apvData.enfcScopCd=='dom' or optConfigMap.alwExtroSign=='Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.sign" alt="서명인" href="javascript:setOfse('02');" /></td></c:if>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.sendDoc" alt="발송" href="javascript:sendDoc();" /></td></c:if>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.cnclEnfc" alt="시행취소" href="javascript:cnclEnfc();" /></td><c:if
			test="${empty param.orgnApvNo}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.transEnfcAsIs" alt="시행변환(기존양식)" href="javascript:transEnfcAsIs();" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.transEnfcNew" alt="시행변환(양식선택)" href="javascript:transEnfcNew();" /></td></c:if>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.modRecvDept" alt="수신처변경" href="javascript:modifyRecvDept();" /></td></c:if><c:if
		
		
			test="${param.bxId eq 'censrBx' and apvData.enfcStatCd == 'inCensr' and optConfigMap.sendFrom != 'censrBx'
				and writeAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.censrApvd" alt="심사승인" href="javascript:processCensr('apvd');" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.censrRejt" alt="심사반려" href="javascript:processCensr('rejt');" /></td></c:if><c:if
		
		
			test="${ frmYn ne 'Y' and empty storage and param.intgTypCd ne 'ERP_CHIT' and
					(	(param.bxId eq 'myBx' and optConfigMap.sendFrom == 'myBx' and apvData.enfcStatCd == 'befoSend' and writeAuth == 'Y')
					or (param.bxId eq 'censrBx' and optConfigMap.sendFrom == 'censrBx' and apvData.enfcStatCd == 'inCensr' and adminAuth == 'Y')
					or (param.bxId eq 'toSendBx' and optConfigMap.sendFrom == 'toSendBx' and apvData.enfcStatCd == 'befoSend' and writeAuth == 'Y')
					or (param.bxId eq 'regRecLst' and apvData.enfcStatCd == 'befoSend' and adminAuth == 'Y' and empty dmUriBase)	)
					}"><c:if
					
					test="${vwMode != 'orgn'}"><c:if
						test="${(apvData.enfcScopCd=='for' or apvData.enfcScopCd=='both')
							or optConfigMap.alwIntroOfcSeal=='Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.ofcSeal" alt="관인" href="javascript:setOfse('01');" /></td></c:if><c:if
					test="${apvData.enfcScopCd=='dom' or optConfigMap.alwExtroSign=='Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.sign" alt="서명인" href="javascript:setOfse('02');" /></td></c:if>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.sendDoc" alt="발송" href="javascript:sendDoc();" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.cnclEnfc" alt="시행취소" href="javascript:cnclEnfc();" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.modRecvDept" alt="수신처변경" href="javascript:modifyRecvDept();" /></c:if><c:if
					
					test="${vwMode == 'orgn'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.cnclEnfc" alt="시행취소" href="javascript:cnclEnfc();" /></td></c:if></c:if><c:if
		
		
			test="${param.bxId eq 'myBx' and not empty param.orgnApvNo
				and apvData.makrUid==sessionScope.userVo.userUid and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y'}"><c:if
				test="${optConfigMap.modTitle eq 'Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.jsp.modTitle" alt="제목 수정" href="javascript:modifySubj();" /></td></c:if><c:if
					test="${empty apErpFormBVo or not empty hasBodyHtml}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.modBody" alt="본문수정" href="javascript:editDocBodyDetl();" /></td></c:if><c:if
					test="${not empty apErpFormBVo and empty WD_XML}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.modCont" alt="내용수정" href="javascript:editFormBodyDetl();" /></td></c:if>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.att" alt="첨부" href="javascript:openAttch();" /></td></c:if><c:if
		
		
			test="${ apvData.enfcStatCd == 'sent' and (
						(param.bxId eq 'myBx' and writeAuth == 'Y')
						or
						(param.bxId eq 'regRecLst')
						or
						(param.bxId eq 'apvdBx')
					 )
					 and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y' and empty dmUriBase
					}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.cfrmRecv" alt="접수확인" href="javascript:openCfrmRecv();" /></td><c:if
						test="${empty storage and (param.bxId eq 'myBx' or (param.bxId eq 'regRecLst' and adminAuth == 'Y'))}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.sendAddDoc" alt="추가발송" href="javascript:openMoreRecvDept();" /></c:if></c:if><c:if
		
		
			test="${adminAuth == 'Y' and empty param.refDocApvNo and sessionScope.userVo.deptId==apvData.recLstDeptId
			and empty storage and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y' and (
				(param.bxId eq 'recvRecLst' and optConfigMap.recvRecLstPubBx=='Y' and empty dmUriBase)
				or (param.bxId eq 'regRecLst' and optConfigMap.regRecLstPubBx=='Y' and empty dmUriBase)
				) and param.intgTypCd ne 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.regPubBx" alt="공람게시" href="javascript:regPubBx();" /></td></c:if><c:if
		
			test="${not empty dmEnable and frmYn ne 'Y'  and empty dmUriBase and empty storage and param.intgTypCd ne 'ERP_CHIT'
				and (  (param.bxId eq 'regRecLst'  and optConfigMap.dmFromRegRecLst  == 'Y')
					or (param.bxId eq 'recvRecLst' and optConfigMap.dmFromRecvRecLst == 'Y'))
				and adminAuth == 'Y' and empty param.refDocApvNo and param.intgTypCd ne 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.sendToDm" alt="문서관리 보내기" href="javascript:sendDocToDm();" /></td></c:if><c:if


		
			test="${param.bxId eq 'rejtBx' and sessionScope.userVo.userUid == apvData.makrUid
				and empty param.rejtApvNo
				and writeAuth == 'Y' and empty apvData.intgNo and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.reMak" alt="재기안" href="javascript:remakeDoc();" /></td></c:if><c:if
		
		
		
			test="${readByNextApvr == 'N' and apvData.docProsStatCd == 'ongo' and writeAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y'}"><u:cmt
			cmt="다음결재자가 읽지 않고 진행중인 문서 일때 - 기안회수, 승인취소, 합의취소" />${myTurnApOngdApvLnDVo}<c:if
		
			test="${(param.apvLnPno eq '0' and param.apvLnNo eq '1')}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.cancelMak" alt="기안회수" href="javascript:cancelMak();" /></td></c:if><c:if
		
			test="${param.bxId eq 'ongoBx' and atAgrStat != 'Y' and not (param.apvLnPno eq '0' and param.apvLnNo eq '1')}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.cancelApv" alt="승인취소" href="javascript:cancelApvd();" /></td></c:if><c:if
		
			test="${param.bxId eq 'ongoBx' and atAgrStat == 'Y' and not (param.apvLnPno eq '0' and param.apvLnNo eq '1')}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.cancelAgr" alt="합의취소" href="javascript:cancelAgr();" /></td></c:if></c:if><c:if
			
			
			
			test="${atWait == 'Y' and param.bxId eq 'waitBx' and writeAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y'}"><c:if
				test="${myTurnApOngdApvLnDVo.apvrRoleCd=='makAgr'}"><u:cmt
				cmt="부서합의의 기안자 일때 - 기안자 지정" />
		<td class="frontbtn"><u:buttonS titleId="ap.btn.setMakr" alt="기안자지정" href="javascript:setMakAgr();" /></td></c:if><c:if
				test="${prcDeptFirstApvr=='Y'}"><u:cmt
				cmt="처리부서 첫번째 결재자 일때 - 담당자 지정" />
		<td class="frontbtn"><u:buttonS titleId="ap.btn.setPich" alt="담당자지정" href="javascript:setPrcDeptAgr();" /></td></c:if></c:if><c:if
		
		
		
			test="${ atWait == 'Y' and param.bxId eq 'deptBx' and adminAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y'}"><u:cmt
				cmt="부서대기함 에서 - 해당 결재 차례 부서의 사용자가 볼때" /><c:if
				test="${myTurnApOngdApvLnDVo.apvrRoleCd=='deptOrdrdAgr' or myTurnApOngdApvLnDVo.apvrRoleCd=='deptParalAgr'}"><u:cmt
				cmt="부서순차합의, 부서병렬합의 일때 - 기안자 지정" />
		<td class="frontbtn"><u:buttonS titleId="ap.btn.setMakr" alt="기안자지정" href="javascript:setMakAgr();" /></td></c:if><c:if
				test="${myTurnApOngdApvLnDVo.apvrRoleCd=='prcDept'}"><u:cmt
				cmt="처리부서 일때 - 담당자 지정" />
		<td class="frontbtn"><u:buttonS titleId="ap.btn.setPich" alt="담당자지정" href="javascript:setPrcDeptAgr();" /></td></c:if></c:if><c:if
			
			test="${atMak eq 'Y'
				and (apvData.docStatCd eq 'retrvMak' or myTurnApOngdApvLnDVo.apvStatCd eq 'reRevw') and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y'}">
		<td class="frontbtn"><u:buttonS alt="삭제" titleId="cm.btn.del" href="javascript:deleteDoc();" /></td></c:if><c:if
			
			test="${param.bxId eq 'refVwBx' and apOngdRefVwDVo.refVwStatCd eq 'inRefVw' and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y'}">
		<td class="frontbtn"><u:buttonS alt="열람확인" termId="ap.term.cfrmRefVw" href="javascript:cfrmRefVw();" /></td></c:if><c:if
		
		
			test="${ param.intgTypCd ne 'ERP_CHIT' and ( 
				atMak ne 'Y'
				or myTurnApOngdApvLnDVo.apvStatCd == 'reRevw'
				or myTurnApOngdApvLnDVo.apvStatCd == 'cncl'
				)}"><u:cmt
				cmt="기안이 아니거나, 재검토, 취소 면 - 기안자 재검토 일경우도 보이게" /><c:if
					test="${not empty showOpinAtBtn}"><u:msg
					titleId="ap.btn.docDetl" var="txtDocDetl" alt="상세정보" /><u:msg
					titleId="ap.txt.hasOpin" var="txtHasOpin" alt="의견있음" />
		<td class="frontbtn"><u:buttonS title="${txtDocDetl} [${txtHasOpin}]" alt="상세정보(의견있음)" href="javascript:viewDocDetl('apvInof');" spanStyle="color:#B03C3C; font-weight:bold;" /></td></c:if><c:if
					test="${empty showOpinAtBtn}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.docDetl" alt="상세정보" href="javascript:viewDocDetl();" /></td></c:if></c:if><c:if
		
		
			test="${ atMak eq 'Y' and writeAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT'}"><u:cmt cmt="기안할 상태면" />
		<td class="frontbtn"><u:buttonS titleId="ap.btn.docInfo" alt="문서정보" href="javascript:openDocInfo();" /></td></c:if><c:if
		
		
			test="${ atMak ne 'Y' and atWait == 'Y' and atAgr != 'Y' and param.bxId eq 'waitBx'
				and apvData.docProsStatCd != 'pubVw' and writeAuth == 'Y'
				and not (erpBodyLock eq 'Y' and not empty apvData.intgNo)
				and param.intgTypCd ne 'ERP_CHIT' }"><c:if
				test="${optConfigMap.modTitle eq 'Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.jsp.modTitle" alt="제목 수정" href="javascript:modifySubj();" /></td></c:if><u:cmt
				cmt="기안이 아니고 & 내 차례 이고 & 대기함 이고 & 합의가 아니면" /><c:if
					test="${empty apErpFormBVo or not empty hasBodyHtml}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.modBody" alt="본문수정" href="javascript:editDocBodyDetl();" /></td></c:if><c:if
					test="${not empty apErpFormBVo and empty WD_XML}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.modCont" alt="내용수정" href="javascript:editFormBodyDetl();" /></td></c:if></c:if><c:if
		
		
			test="${ atWait == 'Y' and atAgr == 'Y'
				and ((param.bxId eq 'waitBx' and writeAuth == 'Y') or (param.bxId eq 'deptBx' and adminAuth == 'Y'))
				and not (erpBodyLock eq 'Y' and not empty apvData.intgNo)
				and param.intgTypCd ne 'ERP_CHIT'}"><c:if
				test="${optConfigMap.chgBodyAtPsnOrdrdAgr eq 'Y' and myTurnApOngdApvLnDVo.apvrRoleCd=='psnOrdrdAgr'}"><c:if
					test="${optConfigMap.modTitle eq 'Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.jsp.modTitle" alt="제목 수정" href="javascript:modifySubj();" /></td></c:if><c:if
					test="${empty apErpFormBVo or not empty hasBodyHtml}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.modBody" alt="본문수정" href="javascript:editDocBodyDetl();" /></td></c:if><c:if
					test="${not empty apErpFormBVo and empty WD_XML}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.modCont" alt="내용수정" href="javascript:editFormBodyDetl();" /></td></c:if></c:if><c:if
				test="${myTurnApOngdApvLnDVo.apvrRoleCd=='deptOrdrdAgr' 
					or myTurnApOngdApvLnDVo.apvrRoleCd=='deptParalAgr'
					or myTurnApOngdApvLnDVo.apvLnPno != '0'
					or (optConfigMap.chgApvrAtPsnOrdrdAgr eq 'Y' and myTurnApOngdApvLnDVo.apvrRoleCd=='psnOrdrdAgr')}"
		><td class="frontbtn"><u:buttonS titleId="ap.btn.setApvLn" alt="결재선지정" href="javascript:openApvLn();" /></td></c:if><c:if
			test="${optConfigMap.refVwEnable eq 'Y' and atMak ne 'Y'
				and apvData.docProsStatCd eq 'ongo' and param.bxId eq 'waitBx'}">
		<td class="frontbtn"><u:buttonS termId="ap.term.refVw" alt="참조열람" href="javascript:openRefVw();" /></td></c:if>
		<td class="frontbtn"><u:buttonS titleId="ap.doc.opin" alt="의견" href="javascript:openOpin();" /></td></c:if><c:if
		
		
			test="${ ( atMak eq 'Y' or ( apvData.docProsStatCd != 'pubVw' 
				and atWait == 'Y' and atAgr != 'Y' and myTurnApOngdApvLnDVo.apvLnPno == '0'
				and (param.bxId eq 'waitBx' or param.bxId eq 'myBx' or param.bxId eq 'drftBx'
				 or (param.bxId eq 'deptBx' and adminAuth == 'Y')) )) and writeAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT' }"><u:cmt cmt="내 차례 이고 & (대기함, 기안함, 개인함)이면" />
		<td class="frontbtn"><u:buttonS titleId="ap.btn.setApvLn" alt="결재선지정" href="javascript:openApvLn();" /></td><c:if
						test="${optConfigMap.adiRevw eq 'Y' and param.bxId eq 'waitBx' and myTurnApOngdApvLnDVo.apvLnPno eq '0' and
							(myTurnApOngdApvLnDVo.apvrRoleCd eq 'revw'
							or myTurnApOngdApvLnDVo.apvrRoleCd eq 'revw2'
							or myTurnApOngdApvLnDVo.apvrRoleCd eq 'revw3'
							or myTurnApOngdApvLnDVo.apvrRoleCd eq 'apv'
							or myTurnApOngdApvLnDVo.apvrRoleCd eq 'pred')}">
		<td class="frontbtn"><u:buttonS titleId="ap.cfg.adiRevw" alt="추가검토" href="javascript:setAdiRevw();" /></td></c:if><c:if
			test="${optConfigMap.refVwEnable eq 'Y' and (atMak eq 'Y' or (
				atMak ne 'Y' and apvData.docProsStatCd eq 'ongo' and param.bxId eq 'waitBx'))}">
		<td class="frontbtn"><u:buttonS termId="ap.term.refVw" alt="참조열람" href="javascript:openRefVw();" /></td></c:if><c:if
			test="${apFormBVo.formTypCd == 'extro'}"><u:cmt cmt="시행문서만 - 수신처지정 보이게"/>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.setRecv" alt="수신처지정" href="javascript:openRecvDept();" /></td></c:if>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.att" alt="첨부" href="javascript:openAttch();" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.doc.opin" alt="의견" href="javascript:openOpin();" /></td></c:if><c:if
		
		
			test="${ empty param.orgnApvNo and apvData.docProsStatCd != 'pubVw' and param.intgTypCd ne 'ERP_CHIT' 
				and not ( atMak eq 'Y' or ( atWait == 'Y' and atAgr != 'Y' and myTurnApOngdApvLnDVo.apvLnPno == '0'
				and (param.bxId eq 'waitBx' or param.bxId eq 'myBx' or param.bxId eq 'drftBx'
				 or (param.bxId eq 'deptBx' and adminAuth == 'Y')) )) }"><%// 기안 포함 내가 처리할 차례인지 - 가 아니면 %><c:if
				 	test="${myTurnApOngdApvLnDVo.apvrRoleCd eq 'psnOrdrdAgr' and optConfigMap.chgAttchAtPsnOrdrdAgr eq 'Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.att" alt="첨부" href="javascript:openAttch();" /></td></c:if><c:if
				 	test="${not (myTurnApOngdApvLnDVo.apvrRoleCd eq 'psnOrdrdAgr' and optConfigMap.chgAttchAtPsnOrdrdAgr eq 'Y')}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.att" alt="첨부" href="javascript:viewAttch();" /></td></c:if></c:if><c:if
		
			test="${apvData.docProsStatCd == 'pubVw' and param.intgTypCd ne 'ERP_CHIT'}"><%// 유통 문서 이면 %>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.att" alt="첨부" href="javascript:viewAttch();" /></td></c:if><c:if
		
		
			test="${ atMak eq 'Y' and writeAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT' }"><%// 기안할 상태면 - 상신, 임시저장 %>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.subm" alt="상신" href="javascript:openProsStat('mak');" /></td>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.temp" alt="임시저장" href="javascript:saveDoc('temp');" /></td></c:if><c:if
		
		
		
			test="${ myTurnApOngdApvLnDVo.apvLnPno=='0' and param.bxId eq 'deptBx' and adminAuth == 'Y' and param.intgTypCd ne 'ERP_CHIT'}"><c:if
			
				test="${myTurnApOngdApvLnDVo.apvrRoleCd=='deptOrdrdAgr' or myTurnApOngdApvLnDVo.apvrRoleCd=='deptParalAgr'}">
		<td class="frontbtn"><u:buttonS termId="ap.term.pros" alt="합의승인" href="javascript:openProsStat('pros');" /></td><c:if
			test="${optConfigMap.noConsAtAgr ne 'Y'}">
		<td class="frontbtn"><u:buttonS termId="ap.term.cons" alt="합의반대" href="javascript:openProsStat('cons');" /></td></c:if><c:if
			test="${rejtPrevEnab =='Y'}">
		<td class="frontbtn"><u:buttonS termId="ap.term.reRevw" alt="재검토" href="javascript:openProsStat('reRevw', true);" /></td></c:if></c:if><c:if
		
				test="${myTurnApOngdApvLnDVo.apvrRoleCd=='prcDept'}">
		<td class="frontbtn"><u:buttonS termId="ap.term.apvd" alt="승인" href="javascript:openProsStat('apvd');" /></td>
		<td class="frontbtn"><u:buttonS termId="ap.term.rejt" alt="반려" href="javascript:openProsStat('rejt');" /></td><c:if
			test="${rejtPrevEnab =='Y'}">
		<td class="frontbtn"><u:buttonS termId="ap.term.reRevw" alt="재검토" href="javascript:openProsStat('reRevw');" /></td></c:if></c:if></c:if><c:if
		
		
			test="${ frmYn ne 'Y' and atMak eq 'Y' and param.intgTypCd ne 'ERP_CHIT' }"><%// 기안할 상태면 - 에디터 - view 모드로 전환 후 출력 %>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.print" alt="인쇄" href="javascript:printEditDoc();" /></td></c:if><c:if
		
			test="${empty storage and param.intgTypCd ne 'ERP_CHIT' and apvData.docProsStatCd eq 'apvd' and param.bxId eq 'myBx' and frmYn ne 'Y'
				and sessionScope.userVo.hasMnuGrpMdRidOf('BB')
				and optConfigMap.sendToBb eq 'Y' and sessionScope.userVo.isInternalIp()}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.sendToBb" alt="게시 보내기" href="javascript:sendToBb();" /></td></c:if><c:if
		
			test="${empty storage and not empty param.apvNo and param.intgTypCd ne 'ERP_CHIT' and frmYn ne 'Y'
			 and sessionScope.userVo.isInternalIp()
			 and sessionScope.userVo.hasMnuGrpMdRidOf('MAIL')
			 and ( (optConfigMap.sendMailAtMyBx eq 'Y' and param.bxId eq 'myBx' and atMak ne 'Y')
			 	or (optConfigMap.sendMailAtOngoBx eq 'Y' and param.bxId eq 'ongoBx')
			 	or (optConfigMap.sendMailAtWaitBx eq 'Y' and param.bxId eq 'waitBx')
			 	or (optConfigMap.sendMailAtApvdBx eq 'Y' and param.bxId eq 'apvdBx')
			 	or (optConfigMap.sendMailAtRejtBx eq 'Y' and param.bxId eq 'rejtBx')
			 	or (optConfigMap.sendMailAtPostApvdBx eq 'Y' and param.bxId eq 'postApvdBx')
			 	or (optConfigMap.sendMailRegRecLst eq 'Y' and param.bxId eq 'regRecLst' and adminAuth eq 'Y')
			 	or (optConfigMap.sendMailRecvRecLst eq 'Y' and param.bxId eq 'recvRecLst' and adminAuth eq 'Y')
			 	)}">
		<td class="frontbtn"><u:buttonS titleId="ap.cfg.sendMail" alt="메일 보내기" href="javascript:sendToMail();" /></td></c:if><c:if
		
			test="${ frmYn ne 'Y' and atMak ne 'Y' }"><%// 기안이 아니면 - 그냥 출력 %>
		<td class="frontbtn"><u:buttonS titleId="ap.btn.print" alt="인쇄" href="javascript:printDoc();" /></td></c:if><c:if
		
			test="${ frmYn ne 'Y' }"><%// titleId="cm.btn.cancel" alt="취소" 에서 변경 %>
		<td class="frontbtn"><u:buttonS titleId="cm.btn.close" alt="닫기" href="javascript:toList();" /></td></c:if><c:if
		
			test="${ frmYn eq 'Y' and callByIntg eq 'Y'}">
		<td class="frontbtn"><u:buttonS titleId="cm.btn.close" alt="닫기" onclick="window.close();" /></td></c:if><c:if
		
			test="${ frmYn eq 'Y' and callByIntg ne 'Y' and param.intgTypCd != 'ERP_CHIT'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.print" alt="인쇄" href="javascript:printDoc();" /></td><c:if
			test="${winPop eq 'Y'}">
		<td class="frontbtn"><u:buttonS titleId="cm.btn.close" alt="닫기" onclick="window.close();" /></td></c:if><c:if
			test="${winPop ne 'Y'}">
		<td class="frontbtn"><u:buttonS titleId="cm.btn.close" alt="닫기" onclick="parent.dialog.close('viewDocDialog');" /></td></c:if></c:if><c:if
		
			test="${ param.intgTypCd == 'ERP_CHIT'}"><c:if
				test="${param.winPop eq 'Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.print" alt="인쇄" href="javascript:printDoc();" /></td>
		<td class="frontbtn"><u:buttonS titleId="cm.btn.close" alt="닫기" onclick="window.close();" /></td></c:if><c:if
				test="${param.winPop ne 'Y'}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.print" alt="인쇄" href="javascript:printDoc();" /></td>
		<td class="frontbtn"><u:buttonS titleId="cm.btn.close" alt="닫기" onclick="parent.dialog.close('viewDocDialog');" /></td></c:if></c:if>
		</tr>
		</tbody></table>
	</div>
</div><%

// 상단 결재 기능 버튼 아래 라인 %>
<div class="apBtnLine notPrint"></div>
</div></c:if>

<form id="docForm" method="post" enctype="multipart/form-data"><c:if
	
	test="${empty downDocYn}">
<u:input type="hidden" name="bxId" value="${param.bxId}" />
<u:input id="apvNo" type="hidden" value="${param.apvNo}" />
<u:input id="tempApvNo" type="hidden" value="" /><c:if
	test="${not empty myApOngdApvLnDVo.apvLnPno}">
<u:input id="apvLnPno" type="hidden" value="${atMakAgr=='Y' ? myTurnApOngdApvLnDVo.apvLnNo : myApOngdApvLnDVo.apvLnPno}" /></c:if><c:if
	test="${not empty myTurnApOngdApvLnDVo.apvLnNo}">
<u:input id="apvLnNo" type="hidden" value="${atMakAgr=='Y' ? '1' : myApOngdApvLnDVo.apvLnNo}" /></c:if><c:if
	test="${not empty param.formId or not empty param.intgNo or not empty param.rejtApvNo or apvData.docStatCd eq 'temp'}">
<u:input id="formId" type="hidden" value="${apFormBVo.formId}" /></c:if>
<u:input id="formApvLnTypCd" type="hidden" value="${apFormBVo.formApvLnTypCd}" />
<u:input id="statCd" type="hidden" value="" /><c:if
	test="${not empty atMakAgr}">
<u:input id="atMakAgr" type="hidden" value="${atMakAgr}" /></c:if><c:if
	test="${not empty atMakPrcDept}">
<u:input id="atMakPrcDept" type="hidden" value="${atMakPrcDept}" /></c:if><c:if
	test="${not empty param.rejtApvNo}">
<u:input id="rejtApvNo" type="hidden" value="${param.rejtApvNo}" /></c:if><c:if
	test="${not empty param.refDocApvNo and empty param.rejtApvNo}">
<u:input id="rejtApvNo" type="hidden" value="${param.refDocApvNo}" /></c:if><c:if
	test="${not empty param.orgnApvNo}">
<u:input id="orgnApvNo" type="hidden" value="${param.orgnApvNo}" /></c:if><c:if
	test="${not empty param.intgNo}">
<u:input id="intgNo" type="hidden" value="${param.intgNo}" /></c:if></c:if>
<div id="docArea" style="min-height:610px">
<u:cmt
	
	cmt="위아래 순서 조절된 순서대로 출력하기 위한 루프" />
<c:forEach items="${apFormCombDVoList}" var="apFormCombDVo" varStatus="apFormStatus"><c:if

	test="${apFormCombDVo.formCombId == 'header'}"

><c:if test="${not empty docName.txtCont or not empty docHeaderImg.imgPath or not empty docLogo.imgPath or not empty docSymbol.imgPath or not empty docHeader.txtCont}">
<div id="headerArea">
<div><c:if
		test="${not empty docHeader.txtCont}">
	<div id="docHeaderViewArea" style="width:100%; text-align:center; color:${docHeader.txtColrVa}; font-family:${docHeader.txtFontVa}; font-size:${docHeader.txtSize}; ${docHeader.txtStylVa}"><u:out value="${docHeader.txtCont}" nullValue="" /></div></c:if><c:if
		test="${not empty docHeaderImg.imgPath}">
	<div id="docHeaderImgViewArea" style="width:100%; text-align:center;"><img width="${docHeaderImg.imgWdth}" height="${docHeaderImg.imgHght}" src="${docHeaderImg.imgPath}"></div></c:if><c:if
		test="${not (empty docLogo.imgPath and empty docSymbol.imgPath and empty docName.txtCont)}">
	<div id="docLogoViewArea" style="float:left; width:20%; height:${empty shortTitleYn ? '75px;' : '30px;'}"><c:if test="${not empty docLogo.imgPath}"><img height="75" src="${docLogo.imgPath}"></c:if></div>
	<div id="docNameViewArea" style="float:left; width:60%; height:${empty shortTitleYn ? '75px;' : '30px;'}; text-align:center; margin-top:15px; color:${docName.txtColrVa}; font-family:${docName.txtFontVa}; font-size:${docName.txtSize}; ${docName.txtStylVa}"><u:out value="${apvData.formNm}" /></div>
	<div id="docSymbolViewArea" style="float:right; width:20%; height:${empty shortTitleYn ? '75px;' : '30px;'}; text-align: right;"><c:if test="${not empty docSymbol.imgPath}"><img height="75" src="${docSymbol.imgPath}"></c:if></div></c:if>
</div>
<div class="blank"></div>
</div>
</c:if></c:if><c:if

	test="${apFormCombDVo.formCombId == 'apvLn'}"

>
<div id="apvLnArea" data-seq="${apFormCombDVo.formCombSeq}">
<u:convert
	srcId="apvLn${apFormCombDVo.formCombSeq}" var="apvLns"/><c:forEach
	items="${apvLns}" var="apvLnTitlTypCd"><u:set
		test="${true}" var="apvLnTitlTypCd" value="${apvLnTitlTypCd}" /><c:if
		test="${apFormBVo.formApvLnTypCd == 'apvLnOnecard'}">
<div><img src="/images/etc/DYNE.png" style="float:left; width:216px; height:72px" /></div></c:if><u:convert
	
	srcId="${apvLnTitlTypCd}ApvrList" var="apvrList"/><u:convert
	
	srcId="${apvLnTitlTypCd}" var="apFormApvLnDVo"/><u:cmt
	
		cmt="apvLnDispTypCd : 도장방에 합의를 따로 표현 하는데 합의자가 하나도 세팅 안되면 합의 자체를 표시하지 않기 위해 none 세팅 - 나머지 : 3row, 2row, 1row" /><u:set
		test="${apvLnTitlTypCd=='agr' and empty apvrList}"
		var="apvLnDispNone" value="display:none" elseValue="" /><c:if



	test="${apFormApvLnDVo.apvLnDispTypCd == '3row'}"
><u:set test="${apFormBVo.formApvLnTypCd=='apvLnMultiTopList'}"	var="valid3rowStart"
	value="${fn:length(apvrList) - apvLnMaxCnt_apv}" elseValue="${-1}"
	cmt="서명+리스트 일 경우 - 성명란 표시 숫자 외의 것을 숨기기 위한 것"
/><div id="apvLn" data-apvLnDispTypCd="3row" data-formApvLnTypCd="${apFormBVo.formApvLnTypCd}" data-apvLnTitlTypCd="${apvLnTitlTypCd}" style="${apvLnDispNone}" data-3rowStyle="${
				apFormApvLnDVo.maxSum>12 or ((apFormBVo.formApvLnTypCd eq 'apvLn1LnAgr' or apFormBVo.formApvLnTypCd eq 'apvLnDbl') and apFormApvLnDVo.maxSum>11)
					? 'width:53px' : 
				apFormApvLnDVo.maxSum>10 or ((apFormBVo.formApvLnTypCd eq 'apvLn1LnAgr' or apFormBVo.formApvLnTypCd eq 'apvLnDbl')and apFormApvLnDVo.maxSum>9)
					? 'width:63px' : 
				apFormApvLnDVo.maxSum> 8 ? 'width:77px' : ''}">
	<table border="0" cellspacing="1" cellpadding="0" class="approvaltable" style="float:${apFormApvLnDVo.alnVa};<c:if
		test="${apFormApvLnDVo.bordUseYn eq 'N'}"> background:rgb(255, 255, 255);</c:if>"><tbody>
	<tr><c:if test="${apFormApvLnDVo.titlUseYn != 'N'}"><td class="approval_head" rowspan="3"><u:cmt
			cmt="FB7F63:프라코"></u:cmt><c:if
		test="${custCode eq 'FB7F63' and apFormBVo.formApvLnTypCd eq 'apvLnWrtn' and sessionScope.userVo.langTypCd eq 'ko'}"
		><u:msg title="${apvLnTitlTypCd=='req' ? '현업부서' : '경리부서'}" charSeperator="<br/>" alt="결<br/>재" /></c:if><u:cmt
			cmt="DAEJIN:대진글라스"></u:cmt><c:if
		test="${custCode eq 'DAEJIN' and apFormBVo.formApvLnTypCd eq 'apvLnWrtn' and sessionScope.userVo.langTypCd eq 'ko'}"
		><u:msg title="${apvLnTitlTypCd=='req' ? '현업부서' : '회계부서'}" charSeperator="<br/>" alt="결<br/>재" /></c:if><c:if
		test="${not(
			(custCode eq 'FB7F63' and apFormBVo.formApvLnTypCd eq 'apvLnWrtn' and sessionScope.userVo.langTypCd eq 'ko')
			or (custCode eq 'DAEJIN' and apFormBVo.formApvLnTypCd eq 'apvLnWrtn' and sessionScope.userVo.langTypCd eq 'ko')
			)}"
		><u:term termId="ap.term.${apvLnTitlTypCd}" charSeperator="<br/>" alt="결<br/>재" /></c:if></td></c:if>
		<c:forEach items="${apvrList}" var="apOngdApvLnDVo" varStatus="tdStatus"
		><td class="approval_body" data-apvStatCd="${apOngdApvLnDVo.apvStatCd}" style="${
		tdStatus.index < valid3rowStart ? 'display:none; ' : ''}${
				apFormApvLnDVo.maxSum>12 or ((apFormBVo.formApvLnTypCd eq 'apvLn1LnAgr' or apFormBVo.formApvLnTypCd eq 'apvLnDbl') and apFormApvLnDVo.maxSum>11)
					? 'width:53px' : 
				apFormApvLnDVo.maxSum>10 or ((apFormBVo.formApvLnTypCd eq 'apvLn1LnAgr' or apFormBVo.formApvLnTypCd eq 'apvLnDbl')and apFormApvLnDVo.maxSum>9)
					? 'width:63px' : 
				apFormApvLnDVo.maxSum> 8 ? 'width:77px' : ''}"><div class="approval_bodyin">${apOngdApvLnDVo.positDispVa}</div></td></c:forEach></tr>
	<tr><c:forEach items="${apvrList}" var="apOngdApvLnDVo" varStatus="tdStatus"
		><td class="approval_img" style="position:relative; font-weight:bold; ${
		tdStatus.index < valid3rowStart ? 'display:none' : ''}${
				apFormApvLnDVo.maxSum>12 or ((apFormBVo.formApvLnTypCd eq 'apvLn1LnAgr' or apFormBVo.formApvLnTypCd eq 'apvLnDbl') and apFormApvLnDVo.maxSum>11)
					? 'width:53px' : 
				apFormApvLnDVo.maxSum>10 or ((apFormBVo.formApvLnTypCd eq 'apvLn1LnAgr' or apFormBVo.formApvLnTypCd eq 'apvLnDbl')and apFormApvLnDVo.maxSum>9)
					? 'width:63px' : 
				apFormApvLnDVo.maxSum> 8 ? 'width:77px' : ''}" data-apvStatCd="${apOngdApvLnDVo.apvStatCd}"><c:if
			test="${apOngdApvLnDVo.apvrRoleCd=='abs'}"><u:out value="${apOngdApvLnDVo.absRsonNm}" /></c:if><c:if
			test="${ apOngdApvLnDVo.apvrRoleCd!='abs' and (
					apOngdApvLnDVo.apvStatCd=='apvd' or apOngdApvLnDVo.apvStatCd=='rejt'
					or apOngdApvLnDVo.apvStatCd=='pros' or apOngdApvLnDVo.apvStatCd=='cons')}"><u:set
				
				test="${not empty apOngdApvLnDVo.signImgPath}" var="signHtml"
				value='<img alt="sign" src="${_cxPth}${apOngdApvLnDVo.signImgPath}" height="40px" />'
				elseValue="${apOngdApvLnDVo.signDispVa}"
				cmt="서명용 사진 또는 텍스트를 'signHtml'에 저장 후 링크별(사용자 팝업, 결재선팝업)로 사용"
				/><c:if
			test="${
					(apvData.recLstTypCd!='recvRecLst' and apvData.recLstTypCd!='distRecLst')
				and (param.bxId ne 'recvBx' and param.bxId ne 'distBx')
				and (apOngdApvLnDVo.apvrRoleCd == 'deptOrdrdAgr' or apOngdApvLnDVo.apvrRoleCd == 'deptParalAgr')}"
			><u:term termId="ap.term.${apOngdApvLnDVo.apvrRoleCd}" langTypCd="${apvData.docLangTypCd}" var="apvrRoleNm"
				/><a href="javascript:viewApvLnPop('${apOngdApvLnDVo.apvNo}','${apOngdApvLnDVo.apvLnNo}','<u:out value="${apOngdApvLnDVo.apvDeptNm} (${apvrRoleNm})" type="value"
				/>')">${signHtml}</a></c:if><c:if
			test="${not (
					(apvData.recLstTypCd!='recvRecLst' and apvData.recLstTypCd!='distRecLst')
				and (param.bxId ne 'recvBx' and param.bxId ne 'distBx')
				and (apOngdApvLnDVo.apvrRoleCd == 'deptOrdrdAgr' or apOngdApvLnDVo.apvrRoleCd == 'deptParalAgr'))}"
			><c:if
			test="${not empty apOngdApvLnDVo.apvrUid}"
			><a href="javascript:viewUserPop('${apOngdApvLnDVo.apvrUid}');">${signHtml}</a></c:if><c:if
			test="${empty apOngdApvLnDVo.apvrUid}"
			>${signHtml}</c:if></c:if><c:if
				
				
				test="${apOngdApvLnDVo.predYn=='Y' or not empty apOngdApvLnDVo.agntUid
					or (optConfigMap.dispConsAtAgr eq 'Y' and apOngdApvLnDVo.apvStatCd eq 'cons')
					or (optConfigMap.dispOpinAtSignArea eq 'Y' and not empty apOngdApvLnDVo.apvOpinCont)}"
				><div style="position:absolute; top:3px; left:3px; font-size:11px; font-weight:normal; color:#646464;"><c:if
					test="${apOngdApvLnDVo.predYn=='Y'}"><u:term
						termId="ap.term.pred" alt="전결" /></c:if><c:if
					test="${not empty apOngdApvLnDVo.agntUid}"> <a href="javascript:viewUserPop('${apOngdApvLnDVo.agntUid}');"><u:term
						termId="ap.term.agnt" alt="대결" /></a></c:if><c:if
					test="${optConfigMap.dispConsAtAgr eq 'Y' and apOngdApvLnDVo.apvStatCd eq 'cons'}"> <u:msg
						titleId="ap.txt.signAreaCons" alt="반대" /></c:if><c:if
					test="${optConfigMap.dispOpinAtSignArea eq 'Y' and not empty apOngdApvLnDVo.apvOpinCont}"> <a href="javascript:viewDocDetl('apvInof');"><u:msg
						titleId="ap.doc.opin" alt="의견" /></a></c:if></div></c:if></c:if></td></c:forEach></tr>
	<tr><c:forEach items="${apvrList}" var="apOngdApvLnDVo" varStatus="tdStatus"
		><td class="approval_body" data-apvStatCd="${apOngdApvLnDVo.apvStatCd}" style="${
		tdStatus.index < valid3rowStart ? 'display:none' : ''}${
				apFormApvLnDVo.maxSum>12 or ((apFormBVo.formApvLnTypCd eq 'apvLn1LnAgr' or apFormBVo.formApvLnTypCd eq 'apvLnDbl') and apFormApvLnDVo.maxSum>11)
					? 'width:53px;' : 
				apFormApvLnDVo.maxSum>10 or ((apFormBVo.formApvLnTypCd eq 'apvLn1LnAgr' or apFormBVo.formApvLnTypCd eq 'apvLnDbl')and apFormApvLnDVo.maxSum>9)
					? 'width:63px;' : 
				apFormApvLnDVo.maxSum> 8 ? 'width:77px;' : ''}${
				(not empty optConfigMap.signAreaDt2) ? ' height:34px;' : ''}"><c:if
			test="${(apOngdApvLnDVo.apvStatCd=='apvd' or apOngdApvLnDVo.apvStatCd=='rejt'
					or apOngdApvLnDVo.apvStatCd=='pros' or apOngdApvLnDVo.apvStatCd=='cons')}">${apOngdApvLnDVo.dtDispVa}</c:if><c:if
			test="${not (apOngdApvLnDVo.apvStatCd=='apvd' or apOngdApvLnDVo.apvStatCd=='rejt'
					or apOngdApvLnDVo.apvStatCd=='pros' or apOngdApvLnDVo.apvStatCd=='cons')}"><c:if
						test="${optConfigMap.signAreaDt2 eq 'name'}">&nbsp;<br/>${apOngdApvLnDVo.apvrNm}</c:if><c:if
						test="${optConfigMap.signAreaDt2 ne 'name'}">${apOngdApvLnDVo.dtDispVa}</c:if></c:if></td></c:forEach></tr>
	</tbody></table>
</div></c:if><c:if



	test="${apFormApvLnDVo.apvLnDispTypCd == '2row'}"
><div id="apvLn" data-apvLnDispTypCd="2row" data-formApvLnTypCd="${apFormBVo.formApvLnTypCd}" data-apvLnTitlTypCd="${apvLnTitlTypCd}" style="${apvLnDispNone}">
	<table border="0" cellspacing="1" cellpadding="0" class="approvaltable" style="float:${apFormApvLnDVo.alnVa};<c:if test="${apFormApvLnDVo.bordUseYn eq 'N'}"> background:rgb(255, 255, 255);</c:if>"><tbody>
	<tr><c:if test="${apFormApvLnDVo.titlUseYn != 'N'}"><td class="approval_head" rowspan="2"><u:msg titleId="ap.signArea.${apvLnTitlTypCd}" charSeperator="<br/>" alt="결<br/>재" /></td></c:if>
		<c:forEach items="${apvrList}" var="apOngdApvLnDVo" 
		><td class="approval_body" rowspan="2" data-apvStatCd="${apOngdApvLnDVo.apvStatCd}"><div class="approval_bodyin">${apOngdApvLnDVo.positDispVa}</div></td>
		<td class="approval_body" data-apvStatCd="${apOngdApvLnDVo.apvStatCd}"><c:if
			test="${apOngdApvLnDVo.apvStatCd=='apvd' or apOngdApvLnDVo.apvStatCd=='rejt'
					or apOngdApvLnDVo.apvStatCd=='pros' or apOngdApvLnDVo.apvStatCd=='cons'}">${apOngdApvLnDVo.dtDispVa}</c:if></td></c:forEach></tr>
	<tr><c:forEach items="${apvrList}" var="apOngdApvLnDVo" 
		><td class="approval_body" style="position:relative; font-weight:bold;" data-apvStatCd="${apOngdApvLnDVo.apvStatCd}"><c:if
			test="${apOngdApvLnDVo.apvrRoleCd=='abs'}"><u:out value="${apOngdApvLnDVo.absRsonNm}" /></c:if><c:if
			test="${ apOngdApvLnDVo.apvrRoleCd!='abs' and (
					apOngdApvLnDVo.apvStatCd=='apvd' or apOngdApvLnDVo.apvStatCd=='rejt'
					or apOngdApvLnDVo.apvStatCd=='pros' or apOngdApvLnDVo.apvStatCd=='cons')}"><u:set
				
				test="${not empty apOngdApvLnDVo.signImgPath}" var="signHtml"
				value='<img alt="sign" src="${_cxPth}${apOngdApvLnDVo.signImgPath}" height="20px" />'
				elseValue="${apOngdApvLnDVo.signDispVa}"
				cmt="서명용 사진 또는 텍스트를 'signHtml'에 저장 후 링크별(사용자 팝업, 결재선팝업)로 사용"
				/><c:if
			test="${
					(apvData.recLstTypCd!='recvRecLst' and apvData.recLstTypCd!='distRecLst')
				and (param.bxId ne 'recvBx' and param.bxId ne 'distBx')
				and (apOngdApvLnDVo.apvrRoleCd == 'deptOrdrdAgr' or apOngdApvLnDVo.apvrRoleCd == 'deptParalAgr')}"
			><u:term termId="ap.term.${apOngdApvLnDVo.apvrRoleCd}" langTypCd="${apvData.docLangTypCd}" var="apvrRoleNm"
				/><a href="javascript:viewApvLnPop('${apOngdApvLnDVo.apvNo}','${apOngdApvLnDVo.apvLnNo}','<u:out value="${apOngdApvLnDVo.apvDeptNm} (${apvrRoleNm})" type="value"
				/>')">${signHtml}</a></c:if><c:if
			test="${not (
					(apvData.recLstTypCd!='recvRecLst' and apvData.recLstTypCd!='distRecLst')
				and (param.bxId ne 'recvBx' and param.bxId ne 'distBx')
				and (apOngdApvLnDVo.apvrRoleCd == 'deptOrdrdAgr' or apOngdApvLnDVo.apvrRoleCd == 'deptParalAgr'))}"
			><c:if
			test="${not empty apOngdApvLnDVo.apvrUid}"
			><a href="javascript:viewUserPop('${apOngdApvLnDVo.apvrUid}');">${signHtml}</a></c:if><c:if
			test="${empty apOngdApvLnDVo.apvrUid}"
			>${signHtml}</c:if></c:if><c:if
			
			
				test="${apOngdApvLnDVo.predYn=='Y' or not empty apOngdApvLnDVo.agntUid}"
				><div style="position:absolute; top:5px; left:4px; font-size:11px; font-weight:normal; color:#646464;"><c:if
					test="${apOngdApvLnDVo.predYn=='Y'}"><u:term
					termId="ap.term.pred" alt="전결" /></c:if><c:if
					test="${not empty apOngdApvLnDVo.agntUid}"> <a href="javascript:viewUserPop('${apOngdApvLnDVo.agntUid}');"><u:term
					termId="ap.term.agnt" alt="대결" /></a></c:if></div></c:if></c:if></td></c:forEach></tr>
	</tbody></table>
</div></c:if><c:if



	test="${apFormApvLnDVo.apvLnDispTypCd == '1row'}"
><div id="apvLn" data-apvLnDispTypCd="1row" data-formApvLnTypCd="${apFormBVo.formApvLnTypCd}" data-apvLnTitlTypCd="${apvLnTitlTypCd}" style="${apvLnDispNone}">
	<table border="0" cellspacing="1" cellpadding="0" class="approvaltable" style="float:${apFormApvLnDVo.alnVa};<c:if test="${apFormApvLnDVo.bordUseYn eq 'N'}"> background:rgb(255, 255, 255);</c:if>"><tbody>
	<tr><c:if test="${apFormApvLnDVo.titlUseYn != 'N'}"><td class="approval_headw"><u:msg titleId="ap.signArea.${apvLnTitlTypCd}" alt="결재" /></td></c:if>
		<c:forEach items="${apvrList}" var="apOngdApvLnDVo" 
		><td class="approval_body" data-apvStatCd="${apOngdApvLnDVo.apvStatCd}"><div class="approval_bodyin">${apOngdApvLnDVo.positDispVa}</div></td>
		<td class="approval_body" data-apvStatCd="${apOngdApvLnDVo.apvStatCd}"><c:if
			test="${apOngdApvLnDVo.apvStatCd=='apvd' or apOngdApvLnDVo.apvStatCd=='rejt'
					or apOngdApvLnDVo.apvStatCd=='pros' or apOngdApvLnDVo.apvStatCd=='cons'}">${apOngdApvLnDVo.dtDispVa}</c:if></td>
		<td class="approval_body" style="position:relative; font-weight:bold;" data-apvStatCd="${apOngdApvLnDVo.apvStatCd}"><c:if
			test="${apOngdApvLnDVo.apvrRoleCd=='abs'}"><u:out value="${apOngdApvLnDVo.absRsonNm}" /></c:if><c:if
			test="${ apOngdApvLnDVo.apvrRoleCd!='abs' and (
					apOngdApvLnDVo.apvStatCd=='apvd' or apOngdApvLnDVo.apvStatCd=='rejt'
					or apOngdApvLnDVo.apvStatCd=='pros' or apOngdApvLnDVo.apvStatCd=='cons')}"><u:set
				
				test="${not empty apOngdApvLnDVo.signImgPath}" var="signHtml"
				value='<img alt="sign" src="${_cxPth}${apOngdApvLnDVo.signImgPath}" height="20px" />'
				elseValue="${apOngdApvLnDVo.signDispVa}"
				cmt="서명용 사진 또는 텍스트를 'signHtml'에 저장 후 링크별(사용자 팝업, 결재선팝업)로 사용"
				/><c:if
			test="${
					(apvData.recLstTypCd!='recvRecLst' and apvData.recLstTypCd!='distRecLst')
				and (param.bxId ne 'recvBx' and param.bxId ne 'distBx')
				and (apOngdApvLnDVo.apvrRoleCd == 'deptOrdrdAgr' or apOngdApvLnDVo.apvrRoleCd == 'deptParalAgr')}"
			><u:term termId="ap.term.${apOngdApvLnDVo.apvrRoleCd}" langTypCd="${apvData.docLangTypCd}" var="apvrRoleNm"
				/><a href="javascript:viewApvLnPop('${apOngdApvLnDVo.apvNo}','${apOngdApvLnDVo.apvLnNo}','<u:out value="${apOngdApvLnDVo.apvDeptNm} (${apvrRoleNm})" type="value"
				/>')">${signHtml}</a></c:if><c:if
			test="${not (
					(apvData.recLstTypCd!='recvRecLst' and apvData.recLstTypCd!='distRecLst')
				and (param.bxId ne 'recvBx' and param.bxId ne 'distBx')
				and (apOngdApvLnDVo.apvrRoleCd == 'deptOrdrdAgr' or apOngdApvLnDVo.apvrRoleCd == 'deptParalAgr'))}"
			><c:if
			test="${not empty apOngdApvLnDVo.apvrUid}"
			><a href="javascript:viewUserPop('${apOngdApvLnDVo.apvrUid}');">${signHtml}</a></c:if><c:if
			test="${empty apOngdApvLnDVo.apvrUid}"
			>${signHtml}</c:if></c:if><c:if
			
			
			test="${apOngdApvLnDVo.predYn=='Y' or not empty apOngdApvLnDVo.agntUid}"
				><div style="position:absolute; top:5px; left:4px; font-size:11px; font-weight:normal; color:#646464;"><c:if
					test="${apOngdApvLnDVo.predYn=='Y'}"><u:term
					termId="ap.term.pred" alt="전결" /></c:if><c:if
					test="${not empty apOngdApvLnDVo.agntUid}"> <a href="javascript:viewUserPop('${apOngdApvLnDVo.agntUid}');"><u:term
					termId="ap.term.agnt" alt="대결" /></a></c:if></div></c:if></c:if></td></c:forEach></tr>
	</tbody></table>
</div></c:if>
</c:forEach>

<c:if test="${apFormBVo.formApvLnTypCd == 'apvLnList'
	or apFormBVo.formApvLnTypCd == 'apvLnDblList'
	or apFormBVo.formApvLnTypCd == 'apvLnOneTopList'
	or apFormBVo.formApvLnTypCd == 'apvLnMultiTopList'}">
<c:if test="${apFormBVo.formApvLnTypCd == 'apvLnOneTopList'}">
<u:listArea id="oneTopArea" colgroup="13%,20%,2.8%,31.2%,33%" extData="name:itemsArea" noBottomBlank="true">
	<tr><u:convertArr srcId="oneTopItems" index="0" var="apFormItemLVo" />
		<td class="head_lt"><c:if test="${not empty apFormItemLVo.itemId}"><u:msg titleId="ap.doc.${apFormItemLVo.itemId}" /></c:if></td>
		<td class="body_lt"<c:if test="${not empty apFormItemLVo.itemId}"> id="${apFormItemLVo.itemId}View"</c:if>><c:if
			test="${not empty apFormItemLVo.itemId}"><%@ include file="./setDocItemViewInc.jsp"%></c:if></td>
		<td class="head_ct" rowspan="5"><u:msg titleId="ap.signArea.apv" charSeperator="<br/>" alt="결<br/>재" /></td>
		<td class="head_ct" id="lastApvrPositDispVa"><c:if
			test="${not empty lastApOngdApvLnDVo}">${lastApOngdApvLnDVo.positDispVa}</c:if><c:if
			test="${empty lastApOngdApvLnDVo}"><u:msg titleId="ap.cmpt.signArea.sampleCEO" alt="대표이사" /></c:if></td>
		<td class="head_ct"><u:msg titleId="ap.cmpt.order" alt="지시사항" /></td>
	</tr>
	<tr><u:convertArr srcId="oneTopItems" index="1" var="apFormItemLVo" />
		<td class="head_lt"><c:if test="${not empty apFormItemLVo.itemId}"><u:msg titleId="ap.doc.${apFormItemLVo.itemId}" /></c:if></td>
		<td class="body_lt"<c:if test="${not empty apFormItemLVo.itemId}"> id="${apFormItemLVo.itemId}View"</c:if>><c:if
			test="${not empty apFormItemLVo.itemId}"><%@ include file="./setDocItemViewInc.jsp"%></c:if></td>
		<td class="body_ct" rowspan="3" style="position:relative; font-weight:bold;"><c:if
			test="${lastApOngdApvLnDVo.apvrRoleCd=='abs'}"><u:out value="${lastApOngdApvLnDVo.absRsonNm}" /></c:if><c:if
			test="${lastApOngdApvLnDVo.apvrRoleCd!='abs' and (
					lastApOngdApvLnDVo.apvStatCd=='apvd' or lastApOngdApvLnDVo.apvStatCd=='rejt'
					or lastApOngdApvLnDVo.apvStatCd=='pros' or lastApOngdApvLnDVo.apvStatCd=='cons')}"><u:set
				
				test="${not empty lastApOngdApvLnDVo.signImgPath}" var="signHtml"
				value='<img alt="sign" src="${_cxPth}${lastApOngdApvLnDVo.signImgPath}" height="60px" />'
				elseValue="${lastApOngdApvLnDVo.signDispVa}"
				cmt="서명용 사진 또는 텍스트를 'signHtml'에 저장 후 링크별(사용자 팝업, 결재선팝업)로 사용"
				
				/><c:if
			test="${not empty lastApOngdApvLnDVo.apvrUid}"
			><a href="javascript:viewUserPop('${lastApOngdApvLnDVo.apvrUid}');">${signHtml}</a></c:if><c:if
			test="${empty lastApOngdApvLnDVo.apvrUid}"
			>${signHtml}</c:if><c:if
				test="${lastApOngdApvLnDVo.predYn=='Y' or not empty lastApOngdApvLnDVo.agntUid}"
				><div style="position:absolute; top:3px; left:3px; font-size:11px; font-weight:normal; color:#646464;"><c:if
					test="${lastApOngdApvLnDVo.predYn=='Y'}"><u:term
					termId="ap.term.pred" alt="전결" /></c:if><c:if
					test="${not empty lastApOngdApvLnDVo.agntUid}"> <a href="javascript:viewUserPop('${apOngdApvLnDVo.agntUid}');"><u:term
					termId="ap.term.agnt" alt="대결" /></a></c:if></div></c:if></c:if></td>
		<td class="body_ct" rowspan="5"><u:out value="${lastApOngdApvLnDVo.apvOpinCont}" /></td>
	</tr>
	<tr><u:convertArr srcId="oneTopItems" index="2" var="apFormItemLVo" />
		<td class="head_lt"><c:if test="${not empty apFormItemLVo.itemId}"><u:msg titleId="ap.doc.${apFormItemLVo.itemId}" /></c:if></td>
		<td class="body_lt"<c:if test="${not empty apFormItemLVo.itemId}"> id="${apFormItemLVo.itemId}View"</c:if>><c:if
			test="${not empty apFormItemLVo.itemId}"><%@ include file="./setDocItemViewInc.jsp"%></c:if></td>
	</tr>
	<tr><u:convertArr srcId="oneTopItems" index="3" var="apFormItemLVo" />
		<td class="head_lt"><c:if test="${not empty apFormItemLVo.itemId}"><u:msg titleId="ap.doc.${apFormItemLVo.itemId}" /></c:if></td>
		<td class="body_lt"<c:if test="${not empty apFormItemLVo.itemId}"> id="${apFormItemLVo.itemId}View"</c:if>><c:if
			test="${not empty apFormItemLVo.itemId}"><%@ include file="./setDocItemViewInc.jsp"%></c:if></td>
	</tr>
	<tr><u:convertArr srcId="oneTopItems" index="4" var="apFormItemLVo" />
		<td class="head_lt"><c:if test="${not empty apFormItemLVo.itemId}"><u:msg titleId="ap.doc.${apFormItemLVo.itemId}" /></c:if></td>
		<td class="body_lt"<c:if test="${not empty apFormItemLVo.itemId}"> id="${apFormItemLVo.itemId}View"</c:if>><c:if
			test="${not empty apFormItemLVo.itemId}"><%@ include file="./setDocItemViewInc.jsp"%></c:if></td>
		<td class="body_ct"><c:if
			test="${lastApOngdApvLnDVo.apvStatCd=='apvd' or lastApOngdApvLnDVo.apvStatCd=='rejt'
					or lastApOngdApvLnDVo.apvStatCd=='pros' or lastApOngdApvLnDVo.apvStatCd=='cons'}">${lastApOngdApvLnDVo.dtDispVa}</c:if></td>
	</tr>
</u:listArea>
</c:if>

<u:set test="true" var="apvLnListInDoc" value="Y" />
<c:if test="${apFormBVo.formApvLnTypCd ne 'apvLnDblList'}">
<jsp:include page="./viewApvLnInc.jsp" flush="false" />
</c:if><c:if test="${apFormBVo.formApvLnTypCd eq 'apvLnDblList'}"><u:convert
	var="dispApOngdApvLnDVoList" srcId="reqApvrList" />
<jsp:include page="./viewApvLnInc.jsp" flush="false" /><c:if
	test="${not (apvData.docStatCd eq 'apvd' and empty prcApvrList)}"><u:cmt cmt="한화제약 - 이관문서 - 처리부서 없는곳 제거함" />
<div class="blank"></div><u:convert
	var="dispApOngdApvLnDVoList" srcId="prcApvrList" />
<jsp:include page="./viewApvLnInc.jsp" flush="false" /></c:if>
</c:if>
</c:if>


<u:cmt

		cmt="apvLnAgrBlank : 합의를 지정 안했을 때 - 합의 결재방 박스를 지우면서 아래의 칸 띄우기 위한 div 도 같이 제거하기 위한 것 "
/><u:set
		test="${apFormBVo.formApvLnTypCd=='apvLn2LnAgr' and apvLnTitlTypCd=='agr'}"
		var="blankId" value="apvLnAgrBlank" elseValue="" />
<u:set
		test="${apFormBVo.formApvLnTypCd=='apvLn2LnAgr' and apvLnTitlTypCd=='agr' and empty apvrList}"
		var="blankStyle" value="display:none;" elseValue="" />
<div class="blank" id="${blankId}" style="${blankStyle}"></div>
</div>
</c:if><c:if

	test="${apFormCombDVo.formCombId == 'items'}"

><u:convert srcId="items${apFormCombDVo.formCombSeq}" var="apFormItemDVo"
/><c:if test="${not empty apFormItemDVo}">
<div id="itemsArea" data-name="itemsArea" data-seq="${apFormCombDVo.formCombSeq}">
<u:listArea id="itemsViewArea" colgroup="${ apFormItemDVo.colCnt=='1' ? '13%,87%' : apFormItemDVo.colCnt=='2' ? '13%,37%,13%,37%' : '13%,20%,13%,21%,13%,20%' }" tableStyle="table-layout:fixed;" noBottomBlank="true"
><u:set test="${true}" var="rowIndex" value="1" />
<c:forEach items="${apFormItemDVo.childList}" var="apFormItemLVo" varStatus="status"><u:set
	test="${status.first}" value="<tr>" elseValue="" /><u:set
	test="${rowIndex != apFormItemLVo.rowNo}" value="
</tr><tr>" elseValue="" /><u:set
	test="${rowIndex != apFormItemLVo.rowNo}" var="rowIndex" value="${apFormItemLVo.rowNo}" elseValue="${rowIndex}" />
	<td class="head_lt"><c:if
		test="${not empty apFormItemLVo.itemId}"><u:msg titleId="ap.doc.${apFormItemLVo.itemId}" /></c:if></td><td id="${apFormItemLVo.itemId}View" class="body_lt"<c:if
		test="${not empty apFormItemLVo.cspnVa and apFormItemLVo.cspnVa != '1'}"> colspan="${(apFormItemLVo.cspnVa * 2) - 1}"</c:if><c:if
		test="${atMak eq 'Y' and apFormItemLVo.itemId eq 'docSubj'}"> onclick="openDocInfo();" style="cursor:pointer;"</c:if>>
		<%@ include file="./setDocItemViewInc.jsp"%></td><u:set
	test="${status.last}" value="
</tr>" elseValue=""
	/></c:forEach>
</u:listArea>
<div class="blank"></div>
</div></c:if></c:if><c:if

	test="${apFormCombDVo.formCombId == 'bodyHtml'}"

><u:set
	test="${ atMak eq 'Y' 
		and not (erpBodyLock eq 'Y' and (not empty param.intgNo or not empty apvData.intgNo))}"
	var="bodyHtmlViewMode" value="N" elseValue="Y" /><c:if

	test="${ bodyHtmlViewMode eq 'N' }">
<div id="bodyHtmlArea"><%
	java.util.Map apvDataMap1 = (java.util.Map)request.getAttribute("apvData");
	if(apvDataMap1 != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml((String)apvDataMap1.get("bodyHtml"));
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", apvDataMap1.get("bodyHtml"));
		}
	}
%>
<u:editor id="bodyHtml" width="100%" height="400px" module="ap.form" value="${_bodyHtml}" noFocus="${not empty param.apvLnGrpId}" />
<div class="blank"></div>
</div>
<div id="bodyHtmlViewArea" style="display:none; width:100%; overflow-y:visible; ${
	browser.ieCompatibility or (browser.ie and browser.ver < 9) ? '' : 'overflow-x:auto'}"><c:if
		test="${apFormBVo.bodyOlineYn ne 'N'}">
<u:listArea noBottomBlank="true">
<tr><td class="editor" valign="top" style="${not empty apvData.bodyHghtPx and apvData.bodyHghtPx != '0'
		? ' height:'.concat(apvData.bodyHghtPx).concat('px;') : ''}"><u:out value="${apvData.bodyHtml}" type="${empty scriptBodyHtml ? 'noscript' : ''}" /></td></tr>
</u:listArea></c:if><c:if
		test="${apFormBVo.bodyOlineYn eq 'N'}">
<div class="editor" style="padding:0px 0px 0px 0px;${not empty apvData.bodyHghtPx and apvData.bodyHghtPx != '0'
		? ' height:'.concat(apvData.bodyHghtPx).concat('px;') : ''}"><u:out value="${apvData.bodyHtml}" type="${empty scriptBodyHtml ? 'noscript' : ''}" /></div>
</c:if>
<div class="blank"></div>
</div>
</c:if><c:if


	test="${ bodyHtmlViewMode ne 'N' }">
<div id="bodyHtmlViewArea" style="width:100%;<c:if
		test="${empty noBodyOverflow}"> overflow-y:visible; ${
	browser.ieCompatibility or (browser.ie and browser.ver < 9) ? '' : 'overflow-x:auto;'}</c:if>"><c:if
		test="${apFormBVo.bodyOlineYn ne 'N'}">
<u:listArea noBottomBlank="true">
<tr><td class="editor" valign="top" style="${not empty apvData.bodyHghtPx and apvData.bodyHghtPx != '0'
		? ' height:'.concat(apvData.bodyHghtPx).concat('px;') : ''}"><u:out value="${apvData.bodyHtml}" type="${empty scriptBodyHtml ? 'noscript' : ''}" /></td></tr>
</u:listArea></c:if><c:if
		test="${apFormBVo.bodyOlineYn eq 'N'}">
<div class="editor" style="padding:0px 0px 0px 0px;${not empty apvData.bodyHghtPx and apvData.bodyHghtPx != '0'
		? ' height:'.concat(apvData.bodyHghtPx).concat('px;') : ''}"><u:out value="${apvData.bodyHtml}" type="${empty scriptBodyHtml ? 'noscript' : ''}" /></div>
</c:if><c:if test="${ atMak eq 'Y' }">
<u:textarea id="bodyHtml" value="${apvData.bodyHtml}" titleId="cols.body" style="display:none" />
</c:if>
<div class="blank"></div>
</div></c:if></c:if><c:if

	test="${apFormCombDVo.formCombId eq 'formBody' or apFormCombDVo.formCombId eq 'formEditBody'}"

><c:if
	test="${atMak eq 'Y' and not empty apErpFormBVo.regUrl}"><u:set
		test="${true}" var="formBodyMode" value="reg" />
<div id="formBodyArea" data-name="formBodyArea">
<jsp:include page="/WEB-INF/jsp/${apErpFormBVo.regUrl}" />
<div class="blank"></div>
</div>
</c:if><c:if
	test="${atMak ne 'Y'}">
<div id="formBodyArea" data-name="formBodyArea">
${apvData.bodyHtml}<%--// ${formBodyHTML} >> ${apvData.bodyHtml} : 진행함,기안함에 보류 문서 보이는 문제, 2018.07.25 --%>
<div class="blank"></div>
</div>
</c:if></c:if><c:if

	test="${apFormCombDVo.formCombId eq 'wfFormBody'}"

><c:if
	test="${atMak eq 'Y' and not empty apErpFormBVo.regUrl}"><u:set
		test="${true}" var="wfMenuParams" value="menuId=${menuId}&bxId=${param.bxId}${strMnuParam}" />
<div id="wfFormBodyArea" data-name="wfFormBodyArea" data-edit="Y">
<jsp:include page="/WEB-INF/jsp/${apErpFormBVo.regUrl}" />
<div class="blank"></div>
</div>
</c:if><c:if
	test="${atMak ne 'Y'}">
<div id="wfFormBodyArea" data-name="wfFormBodyArea">
${apvData.bodyHtml}
<div class="blank"></div>
</div>
</c:if></c:if><c:if

	test="${apFormCombDVo.formCombId == 'refVw'}"

>
<div id="refVwArea" data-name="refVwArea" style="${empty apOngdRefVwDVoList and atMak ne 'Y' ? 'display:none' : ''}"><u:set
	test="true" var="refVwListInDoc" value="Y" />
<jsp:include page="./viewRefVwInc.jsp" flush="false" />
<div class="blank"></div>
</div></c:if><c:if

	test="${apFormCombDVo.formCombId == 'infm'}"

>
<div id="infmArea" data-name="infmArea" style="${empty infmApOngdApvLnDVoList and atMak ne 'Y' ? 'display:none' : ''}"><u:set
	test="true" var="refVwListInDoc" value="Y" />
<u:listArea colgroup="13%,87%" noBottomBlank="true">
	<tr><td class="head_ct"><u:term termId="ap.term.infm" alt="통보" /></td>
		<td class="body_lt" id="infmTdArea"><c:forEach
	items="${infmApOngdApvLnDVoList}" var="infmApOngdApvLnDVo" varStatus="infmStatus" ><c:if
		test="${infmApOngdApvLnDVo.apvrRoleCd eq 'psnInfm'}"><c:if
			test="${empty infmApOngdApvLnDVo.apvrUid}"><nobr><u:out
				value="${infmApOngdApvLnDVo.apvrNm}" /></nobr><c:if test="${not infmStatus.last}">, </c:if></c:if><c:if
			test="${not empty infmApOngdApvLnDVo.apvrUid}"><nobr><a href="javascript:${frmYn=='Y'? 'parent.' : ''}viewUserPop('${infmApOngdApvLnDVo.apvrUid}');"><u:out
				value="${infmApOngdApvLnDVo.apvrNm}" /></a></nobr><c:if test="${not infmStatus.last}">, </c:if></c:if></c:if><c:if
		test="${infmApOngdApvLnDVo.apvrRoleCd eq 'deptInfm'}"><nobr><u:out
			value="${infmApOngdApvLnDVo.apvDeptNm}" /></nobr><c:if test="${not infmStatus.last}">, </c:if></c:if></c:forEach></td>
	</tr>
</u:listArea>
<div class="blank"></div>
</div></c:if><c:if

	test="${apFormCombDVo.formCombId == 'refDocBdoy'}"

><c:if test="${empty apvData.docProsStatCd or apvData.docProsStatCd eq 'temp' or apvData.docProsStatCd eq 'mak' or apvData.docProsStatCd eq 'ongo'}">
<div id="refDocBdoyArea"<c:if test="${empty refApOngdBVoList}"> style="display:none"</c:if>><c:if
	test="${not empty refApOngdBVoList}"><c:forEach
		items="${refApOngdBVoList}" var="refApOngdBVo" varStatus="status">
<u:listArea id="ref${refApOngdBVo.apvNo}" colgroup="13%,54%,13%,20%" tableStyle="table-layout:fixed;" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="ap.doc.docSubj" alt="제목" /></td><td class="body_lt"><a href="javascript:openDoc${frmYn=='Y' ? 'Frm' : 'View'}('${refApOngdBVo.apvNo}','${
			not empty refApOngdBVo.docPwEnc
			and refApOngdBVo.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}');"><u:out value="${refApOngdBVo.docSubj}" /></a></td>
	<td class="head_lt"><u:msg titleId="ap.list.cmplDt" alt="완결일시" /></td><td class="body_lt" id="cmplDt">${refApOngdBVo.cmplDt}</td>
</tr>
<tr>
	<td colspan="4" class="editor" valign="top" style="padding:6px;">${refApOngdBVo.bodyHtml}</td>
</tr>
</u:listArea>
<div class="blank"></div></c:forEach>
</c:if>
</div>
<div id="refDocBdoyHtmlArea" style="display:none">
<u:listArea id="ref" colgroup="13%,54%,13%,20%" tableStyle="table-layout:fixed;" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:msg titleId="ap.doc.docSubj" alt="제목" /></td><td class="body_lt"><a href="javascript:;">[docSubj]</a></td>
	<td class="head_lt"><u:msg titleId="ap.list.cmplDt" alt="완결일시" /></td><td class="body_lt" id="cmplDt">[cmplDt]</td>
</tr>
<tr>
	<td colspan="4" class="editor" valign="top" style="padding:6px;">[bodyHtml]</td>
</tr>
</u:listArea>
<div class="blank"></div>
</div></c:if></c:if><c:if

	test="${apFormCombDVo.formCombId == 'sender'}"

><c:if test="${not empty docSender.txtCont}">
<div id="senderArea">
<div>
	<div style="width:100%; height:95px;">
		<dl style="position:relative; float:right; left:-50%; z-index:1; margin-top:30px;">
			<dd style="position:relative; float:left; left:50%; width:80px; z-index:2;">&nbsp;</dd>
			<dd style="position:relative; float:left; left:50%; text-align:center; z-index:2;">
				<div id="docSenderViewArea" style="color:${docSender.txtColrVa}; font-family:${docSender.txtFontVa}; font-size:${docSender.txtSize}; ${docSender.txtStylVa}"><c:if
					test="${empty apvData.sendrNmRescNm}"><u:out value="${docSender.txtCont}" /></c:if><c:if
					test="${not empty apvData.sendrNmRescNm}"><u:out value="${apvData.sendrNmRescNm}" /></c:if></div>
			</dd>
			<dd id="stampArea" style="text-align:center; position:relative; float:left; left:50%; width:115px; margin-left:-35px; margin-top:${empty apvData.ofseHghtPx or apvData.ofseHghtPx=='80' ? '-20' : 20 - (apvData.ofseHghtPx/2)}px; z-index:1;"><c:if
				test="${not empty apvData.ofsePath}"><img height="${apvData.ofseHghtPx}px" src="${_cxPth}${apvData.ofsePath}" /></c:if></dd>
		</dl>
	</div><c:if
		test="${not empty docReceiver.txtCont}">
	<div style="width:100%; text-align:center;">
		<div id="docReceiverViewArea" style="color:${docReceiver.txtColrVa}; font-family:${docReceiver.txtFontVa}; font-size:${docReceiver.txtSize}; ${docReceiver.txtStylVa}"><c:if
			test="${empty apOngdRecvDeptLVoList}"><u:out value="${docReceiver.txtCont}" /></c:if><c:if
			test="${not empty apOngdRecvDeptLVoList}"><u:msg titleId="cols.recvDept" alt="수신처" langTypCd="${apvData.docLangTypCd}" /> : <c:forEach
			items="${apOngdRecvDeptLVoList}" var="apOngdRecvDeptLVo" varStatus="recvDeptStatus"><c:if
				test="${apOngdRecvDeptLVo.addSendYn=='N'}"
				><c:if
				test="${not recvDeptStatus.first}"
				>, </c:if><nobr><u:out value="${apOngdRecvDeptLVo.recvDeptNm}" /><c:if
				test="${not empty apOngdRecvDeptLVo.refDeptNm}"
				>(<u:out value="${apOngdRecvDeptLVo.refDeptNm}" />)</c:if></nobr></c:if></c:forEach></c:if></div>
	</div></c:if>
</div>
<div class="blank"></div>
</div></c:if></c:if><c:if

	test="${apFormCombDVo.formCombId == 'footer'}"

><c:if test="${not empty docFooter.txtCont}">
<div id="footerArea">
<div>
	<div id="docFooterViewArea" style="width:100%; text-align:center; color:${docFooter.txtColrVa
		}; font-family:${docFooter.txtFontVa}; font-size:${docFooter.txtSize
		}; ${docFooter.txtStylVa}"><u:out value="${empty apvData.footerVa ? docFooter.txtCont : apvData.footerVa}" /></div>
</div>
<div class="blank"></div>
</div></c:if></c:if>
</c:forEach><c:if


	test="${not empty linkedApOngdBVo and atMak ne 'Y'}">
<u:title titleId="ap.jsp.relatedDoc" alt="관련문서" type="small" />
<u:listArea id="linkedArea" colgroup="13%,87%">
<tr>
	<td class="head_ct"><u:msg titleId="ap.doc.docSubj" alt="제목" /></td>
	<td class="body_lt" id="linkedSubjArea"><a href="javascript:openDocView('${linkedApOngdBVo.apvNo}','${
			not empty linkedApOngdBVo.docPwEnc
			and linkedApOngdBVo.makrUid != sessionScope.userVo.userUid ? 'Y' : ''}');"><u:out value="${linkedApOngdBVo.docSubj}" /></a></td>
</tr>
</u:listArea>
<div class="blank"></div>
</c:if><c:if

		test="${not empty downDocYn and not empty itemsDown}"
><u:convert srcId="itemsDown" var="apFormItemDVo"
/><c:if test="${not empty apFormItemDVo}">
<div id="itemsArea" data-name="itemsArea" data-seq="${apFormCombDVo.formCombSeq}">
<u:listArea id="itemsViewArea" colgroup="${ apFormItemDVo.colCnt=='1' ? '13%,87%' : apFormItemDVo.colCnt=='2' ? '13%,37%,13%,37%' : '13%,20%,13%,21%,13%,20%' }" tableStyle="table-layout:fixed;" noBottomBlank="true"
><u:set test="${true}" var="rowIndex" value="1" />
<c:forEach items="${apFormItemDVo.childList}" var="apFormItemLVo" varStatus="status"><u:set
	test="${status.first}" value="<tr>" elseValue="" /><u:set
	test="${rowIndex != apFormItemLVo.rowNo}" value="
</tr><tr>" elseValue="" /><u:set
	test="${rowIndex != apFormItemLVo.rowNo}" var="rowIndex" value="${apFormItemLVo.rowNo}" elseValue="${rowIndex}" />
	<td class="head_lt"><c:if
		test="${not empty apFormItemLVo.itemId}"><u:msg titleId="ap.doc.${apFormItemLVo.itemId}" /></c:if></td><td id="${apFormItemLVo.itemId}View" class="body_lt"<c:if
		test="${not empty apFormItemLVo.cspnVa and apFormItemLVo.cspnVa != '1'}"> colspan="${(apFormItemLVo.cspnVa * 2) - 1}"</c:if><c:if
		test="${atMak eq 'Y' and apFormItemLVo.itemId eq 'docSubj'}"> onclick="openDocInfo();" style="cursor:pointer;"</c:if>>
		<%@ include file="./setDocItemViewInc.jsp"%></td><u:set
	test="${status.last}" value="
</tr>" elseValue=""
	/></c:forEach>
</u:listArea>
<div class="blank"></div>
</div></c:if>
</c:if>

</div><c:if
		test="${empty downDocYn}">
<div id="docDataArea" style="display:none"><%
		// 문서정보 - 문서정보 팝업 %>
	<div id="docInfoArea" style="display:none" data-modified="${atMak}"><c:if
			test="${atMak eq 'Y'}">
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
		<input type="hidden" name="regRecLstRegSkedYmd" value="<u:out value="${apvData.regRecLstRegSkedYmd}" type="value" />">
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
		<input type="hidden" name="enfcScopCd" value="<u:out value="${apvData.enfcScopCd}" type="value" />"></c:if><c:if
			test="${not empty sendToDm}">
		<input type="hidden" name="sendToDm" value="<u:out value="${sendToDm}" type="value" />"></c:if>
	</div><%
		// 결재선 설정 정보 - 결재선유형, 부모결재선번호 %>
	<div id="docApvLnSetupArea" style="display:none"></div><%
		// 결재선 정보 - 결재자, 합의부서 등 
		// data-modified : Y 가 설정되면 결재선을 조회하지 않고, 현재 세션정보를 결재선의 기안자로 세팅함 %><u:set
		test="${atMak eq 'Y'
			or myTurnApOngdApvLnDVo.apvrRoleCd == 'makAgr' or myTurnApOngdApvLnDVo.apvrRoleCd == 'makVw'
			}"
		var="docApvLnModified" value="Y" />
	<div id="docApvLnArea" style="display:none" data-modified="${docApvLnModified}"><c:if
		test="${docApvLnModified == 'Y'}">
		<u:input type="hidden" name="apvLnTypCd" value="${apvData.apvLnTypCd}" /><c:forEach
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
	<div id="docOpinArea" style="display:none"><c:if test="${atCmplDoc != 'Y'}"><c:if
	
		test="${not empty apOngdHoldOpinDVo}">
		<u:input type="hidden" name="apvOpinCont" value="${apOngdHoldOpinDVo.apvOpinCont}" />
		<u:input type="hidden" name="apvOpinDispYn" value="${optConfigMap.opinDftDisp eq 'Y' ? 'Y' : apOngdHoldOpinDVo.apvOpinDispYn}" /></c:if><c:if
		
		test="${empty apOngdHoldOpinDVo}">
		<u:input type="hidden" name="apvOpinCont" value="${myTurnApOngdApvLnDVo.apvOpinCont}" />
		<u:input type="hidden" name="apvOpinDispYn" value="${optConfigMap.opinDftDisp eq 'Y' ? 'Y' : myTurnApOngdApvLnDVo.apvOpinDispYn}" /></c:if>
	</c:if></div><%
		// 첨부 파일 - 파일 시퀀스, 파일명 등의 기존 저장 데이터 정보 %>
	<dl id="docFileInfoArea" style="display:none" data-modified="${atMak}"><c:if test="${atMak eq 'Y'}">
		<input type="hidden" name="maxFileSeq" value="${not empty param.intgNo and not empty apErpIntgFileDVoList ? fn:length(apErpIntgFileDVoList) : '0'}"><c:if
			test="${not empty param.intgNo and not empty apErpIntgFileDVoList}"><c:forEach
			items="${apErpIntgFileDVoList}" var="pErpIntgFileDVo">
		<dd class="sizeZero fileItem" style="display:none">
			<input type="hidden" name="attSeq" value="${pErpIntgFileDVo.seq}">
			<input type="hidden" name="attDispNm" value="${pErpIntgFileDVo.attDispNm}">
			<input type="hidden" name="attUseYn" value="Y">
			<input type="hidden" name="attIntgNo" value="${param.intgNo}">
			<input type="hidden" name="fileKb" value="${pErpIntgFileDVo.fileKb}">
		</dd></c:forEach></c:if><c:forEach
			items="${apOngdAttFileLVoList}" var="apOngdAttFileLVo">
		<dd class="sizeZero fileItem" style="display:none">
			<input type="hidden" name="attSeq" value="${apOngdAttFileLVo.attSeq}">
			<input type="hidden" name="attDispNm" value="${apOngdAttFileLVo.attDispNm}">
			<input type="hidden" name="attUseYn" value="Y">
			<input type="hidden" name="fileKb" value="${apOngdAttFileLVo.fileKb}">
		</dd></c:forEach>
	</c:if></dl><%
		// 첨부 파일 - 실제 input[type=file] 테그가 위치할 곳 %>
	<div id="docFileTagArea" style="display:none"></div><%
		// 참조 문서 정보 %>
	<div id="docRefDocArea" style="display:none"><c:if
		test="${atMak eq 'Y'}">
		<input type='hidden' name='refApvModified' value='Y' /><c:forEach
			items="${refApOngdBVoList}" var="refApOngdBVo"><u:out
				var="refDocSubj" value="${refApOngdBVo.docSubj}" type="jsonValue" />
		<input name="refApv" value="<u:out value='{"apvNo":"${refApOngdBVo.apvNo
			}","docSubj":"${refDocSubj
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
		test="${atMak eq 'Y'}">
		<input type='hidden' name='recvDeptModified' value='Y' /><c:forEach
			items="${apOngdRecvDeptLVoList}" var="apOngdRecvDeptLVo">
		<input name="recvDept" value="<u:out value='{"recvDeptTypCd":"${apOngdRecvDeptLVo.recvDeptTypCd
			}","recvDeptTypNm":"${apOngdRecvDeptLVo.recvDeptTypNm
			}","recvDeptId":"${apOngdRecvDeptLVo.recvDeptId
			}","recvDeptNm":"${apOngdRecvDeptLVo.recvDeptNm
			}","refDeptId":"${apOngdRecvDeptLVo.refDeptId
			}","refDeptNm":"${apOngdRecvDeptLVo.refDeptNm}"}' type="value" />" type="hidden"></c:forEach>
	</c:if></div><%
		// 참조열람 설정 정보 %>
	<div id="docRefVwArea" style="display:none" data-modified="${atMak}"><c:if
		test="${atMak eq 'Y' and optConfigMap.refVwEnable eq 'Y'}">
		<input type='hidden' name='refVwModified' value='Y' /><c:forEach
			items="${apOngdRefVwDVoList}" var="apOngdRefVwDVo"><c:if
				test="${not empty apOngdRefVwDVo.refVwrUid}">
		<input name="refVw" value="<u:out value='{"refVwrUid":"${apOngdRefVwDVo.refVwrUid
			}","refVwrNm":"${apOngdRefVwDVo.refVwrNm
			}","refVwStatCd":"${apOngdRefVwDVo.refVwStatCd
			}","refVwStatNm":"${apOngdRefVwDVo.refVwStatNm
			}","refVwrPositNm":"${apOngdRefVwDVo.refVwrPositNm
			}","refVwrTitleNm":"${apOngdRefVwDVo.refVwrTitleNm
			}","refVwrDeptId":"${apOngdRefVwDVo.refVwrDeptId
			}","refVwrDeptNm":"${apOngdRefVwDVo.refVwrDeptNm
			}","refVwDt":"${apOngdRefVwDVo.refVwDt
			}","refVwFixdApvrYn":"${apOngdRefVwDVo.refVwFixdApvrYn
			}","hasOpin":"${empty apOngdRefVwDVo.refVwOpinCont ? "" : "Y"
			}"}' type="value" />" type="hidden"></c:if></c:forEach>
	</c:if></div>
	<div id="senderStampArea" style="display:none"><c:if
		test="${not empty autoOfcSeal}">
			<u:input type="hidden" name="ofsePath" value="${apvData.ofsePath}" />
			<u:input type="hidden" name="ofseHghtPx" value="${apvData.ofseHghtPx}" />
			<u:input type="hidden" name="sendrNmRescId" value="${apvData.sendrNmRescId}" />
			<u:input type="hidden" name="sendrNmRescNm" value="${apvData.sendrNmRescNm}" />
		</c:if></div>
	<div id="trxDataArea" style="display:none"></div>
	<div id="sendInfoArea" style="display:none"></div>
	<div id="formBodyXMLDataArea" style="display:none"></div>
	<div id="wfFormDataArea" style="display:none"></div>
</div></c:if><c:if
	test="${not empty param.queryString}">
<u:input type="hidden" name="queryString" value="${param.queryString}" /></c:if>
</form><c:if


	test="${param.bxId eq 'rejtBx' && apvData.docProsStatCd == 'rejt'
		&& sessionScope.userVo.userUid == apvData.makrUid}">
<form id="remakForm" action="./setDoc.do">
<u:input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" name="bxId" value="${param.bxId}" />
<u:input type="hidden" name="rejtApvNo" value="${param.apvNo}" /><c:if
	test="${not empty param.queryString}">
<u:input type="hidden" name="queryString" value="${param.queryString}" /></c:if>
</form></c:if><c:if


	test="${param.bxId eq 'myBx' and apvData.enfcStatCd == 'befoEnfc' and writeAuth == 'Y'}">
<form id="trxForm" action="./setDoc.do">
<u:input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" name="bxId" value="${param.bxId}" />
<u:input type="hidden" name="orgnApvNo" value="${param.apvNo}" />
<u:input type="hidden" name="formId" value="${apvData.formId}" />
<u:input type="hidden" name="formSeq" value="${apvData.formSeq}" /><c:if
	test="${not empty param.queryString}">
<u:input type="hidden" name="queryString" value="${param.queryString}" /></c:if>
</form></c:if><c:if


	test="${not empty vwMode or not empty apvData.trxApvNo}">
<form id="viewModeForm" action="./setDoc.do">
<u:input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" name="bxId" value="${param.bxId}" />
<u:input type="hidden" name="apvNo" value="${param.apvNo}" />
<u:input type="hidden" name="vwMode" value="${vwMode!='trx' ? 'trx' : 'orgn'}" /><c:if
	test="${not empty apvData.docPwEnc}">
<u:input type="hidden" name="secuId" value="" /></c:if><c:if
	test="${not empty param.queryString}">
<u:input type="hidden" name="queryString" value="${param.queryString}" /></c:if>
</form></c:if><c:if


	test="${not empty apFormJspDVo.jspPath}">
<form id="viewExtDocForm" action="./setDoc.do">
<u:input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" name="bxId" value="${param.bxId}" />
<u:input type="hidden" name="apvNo" value="${param.apvNo}" />
<u:input type="hidden" name="vwMode" value="extDoc" /><c:if
	test="${not empty apvData.docPwEnc}">
<u:input type="hidden" name="secuId" value="" /></c:if><c:if
	test="${not empty param.queryString}">
<u:input type="hidden" name="queryString" value="${param.queryString}" /></c:if>
</form></c:if><c:if


	test="${not empty prevApOngdBVo or not empty nextApOngdBVo}">
<form id="prevNextForm" action="./setDoc.do">
<u:input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" name="bxId" value="${param.bxId}" />
<u:input type="hidden" name="apvNo" value="" />
<u:input type="hidden" name="apvLnPno" value="" />
<u:input type="hidden" name="apvLnNo" value="" /><c:if
	test="${not empty param.formId and (param.bxId eq 'admOngoFormBx' or param.bxId eq 'admApvdFormBx')}">
<u:input type="hidden" id="formId" value="${param.formId}" /></c:if><c:if
	test="${not empty vwMode}">
<u:input type="hidden" name="vwMode" value="${vwMode!='trx' ? 'trx' : 'orgn'}" /></c:if><c:if
	test="${not empty param.queryString}">
<u:input type="hidden" name="queryString" value="${param.queryString}" /></c:if><c:if
	test="${not empty param.pltQueryString}">
<u:input type="hidden" name="pltQueryString" value="${param.pltQueryString}" /></c:if><c:if
	test="${empty param.queryString and empty param.pltQueryString}">
<u:input type="hidden" name="queryString" value="${queryString}" /></c:if>
</form></c:if>

</div>
