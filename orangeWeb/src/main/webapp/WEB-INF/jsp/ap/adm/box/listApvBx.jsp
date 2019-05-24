<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 문서 조회 %>
function openDoc(apvNo, apvLnPno, apvLnNo, sendSeq, pwYn){
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
		dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo);
	} else {
		$form.attr("action", "./setDoc.do").submit();
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
// 비밀번호 세팅된 문서 열기 - setDocPwPop.jsp 에서 호출 %>
function openSecuDoc(secuId){
	initForm(false).appendHidden({'name':'secuId','value':secuId
		}).attr("action", "./setDoc.do").submit();
}<%
// 문서조회 Form 리턴 %>
function initForm(init){
	var $form = $("#toDocForm");
	if(init!=false){
		$form.find("[name='apvNo'], [name='apvLnPno'], [name='secuId'], [name='sendSeq']").remove();
	}
	return $form;
}<%
// 첨부 조회%>
function viewAttach(apvNo){
	${not empty isFrame ? 'parent.' : ''}dialog.open("setDocAttchDialog", '<u:msg titleId="ap.btn.att" alt="첨부" />', "./viewDocAttchPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo);
}<%
// 상세정보 %>
function openDetl(){
	var $inputs = getSelected(true);
	if($inputs!=null){
		${not empty isFrame ? 'parent.' : ''}dialog.open("setDocDetlDialog", '<u:msg titleId="ap.btn.docDetl" alt="상세정보" />', "./viewDocDetlPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+$inputs.val()+"&fromList=Y");
	}
}<%
// 경로조회(결재라인 목록형 에서 호출:viewApvLnInc.jsp) - apvLnPno 가 없으면 "전체경로", 있으면 해당 서브 경로
// 주로 viewDocDetlPop.jsp 에서 사용되며, 본문에 목록형 결재라인이 추가되어 이곳으로 스크립트 옮겨옴 %>
function viewApvLnPop(apvNo, apvLnPno, deptNm){
	var popTitle = apvLnPno==null ? '<u:msg alt="전체경로" titleId="ap.btn.allApvLn" />' : '<u:msg alt="결재 경로" titleId="ap.jsp.apvLn" />' + (deptNm==null ? '' : ' - '+deptNm);
	var popId = apvLnPno==null ? 'viewApvLnDialog' : 'viewSubApvLnDialog';
	dialog.open(popId, popTitle, "./viewApvLnPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo+(apvLnPno==null ? '' : '&apvLnPno='+apvLnPno));
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
// 선택된 문서중 비밀번호 세팅된 문서 가져오기 %>
function getSelectedWithPw(){
	var $inputs = $("#listApvForm input[type='checkbox'][id!='checkHeader'][data-docPwWithMine='Y']:checked:visible");
	if($inputs.length==0){<%
		// ap.msg.selectHasPw=비밀번호가 설정된 문서를 선택 하십시요. %>
		alertMsg("ap.msg.selectHasPw");
		return null;
	}
	return $inputs;
}
<%
// 비밀번호 해제 %>
function delDocPw(){
	if(getSelected(false)==null) return;
	var $checks = getSelectedWithPw();
	if($checks==null) return;<%
	// ap.cfrm.delDocPwCnt="{0}"건의 문서 비밀번호를 해제 하시겠습니까 ? %>
	if(!confirmMsg('ap.cfrm.delDocPwCnt', [''+$checks.length])) return;
	
	var arr = [], result = false;
	$checks.each(function(){
		arr.push($(this).val());
	});
	
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"delDocPwByAdm", apvNos:arr.join(',')}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result) reload();
}<%
// 삭제 %>
function deleteDoc(){<%
	// 선택된 문서 가져오기 %>
	var $checks = getSelected(false);
	if($checks==null) return;<%
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
	if(result) location.replace(location.href);
}<%
// 설정 - 의견목록 에서 설정할 결재자 %>
function setOpinApvrLst(){
	var apvrs = '${apvrUids}';
	var data = [];
	if(apvrs!=''){
		var arr = apvrs.split(',');
		for(var i=0;i<arr.length;i++){
			data.push({userUid:arr[i]});
		}
	}
	searchUserPop({data:data, multi:true, titleId:'ap.cfg.apvr', mode:'search'}, function(arr){
		
		var uids = [];
		if(arr!=null){
			if(arr.length>20){<%//ap.msg.lessThanUser={0}명 이하의 사용자를 선택 하십시요%>
				alertMsg('ap.msg.lessThanUser',['20']);
				return false;
			}
			arr.each(function(index, user){
				uids.push(user.userUid);
			});
		}
		
		callAjax("./transOpinApvrAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {apvrUids:uids.join(',')}, function(data){
			if(data.message != null) alert(data.message);
			if(data.result == 'ok'){
				top.location.replace(top.location.href);
			}
		});
	});
}<%
// 엑셀 다운로드 %>
function excelDownFile() {
	var $form = $("#searchArea1:visible, #searchArea2:visible").find("form");
	
	var method = $form.attr('method');
	var action = $form.attr('action');
	var target = $form.attr('target');
	
	$form.attr('method','post');
	$form.attr('action','./excelDownLoad.do');
	$form.attr('target','dataframe');
	$form[0].submit();
	
	$form.attr('method', method==null ? "" : method);
	$form.attr('action', action);
	$form.attr('target', target==null ? "" : target);
	
};<%
// 진행상태 변경 - 의견목록 %>
function onChangePrgStat(stat){
	var durCatTxt = ("rejt"==stat) ? '<u:msg titleId="ap.list.rejtDd" alt="반려일자" />' : ("ongo"==stat) ? '<u:msg titleId="ap.doc.makDd" alt="기안일자" />' : '<u:msg titleId="ap.list.cmplDd" alt="완결일자" />';
	var durCat    = ("ongo"==stat) ? 'makDt' : 'cmplDt';
	var $area = $("#durCatArea");
	$area.find("#durCatTxt").text(durCatTxt);
	$area.find("#durCat").val(durCat);
}<%
// onload %>
$(document).ready(function() {
	setUniformCSS();
});
//<c:if test="${sessionScope.userVo.userUid eq 'U0000001'}"><%
// download %>
function openDownloadPop(){
	dialog.open('setDownloadPop', '<u:msg titleId="cm.btn.download" alt="다운로드" />', './setDownloadPop.do?menuId=${menuId}');
}
//</c:if>
//-->
</script>
<u:title title="대기함, 기안함 등" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea style="position:relative; z-index:2;">
	<div id="searchArea1" style="<c:if test="${not empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="bxId" value="${bxId}" /><c:forEach items="${arrMnuParam}" var="mnuParam">
	<u:input type="hidden" id="${mnuParam[0]}" value="${mnuParam[1]}" /></c:forEach><c:if
		test="${not empty param.pageRowCnt}">
	<u:input type="hidden" id="pageRowCnt" value="${param.pageRowCnt}" /></c:if>
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><c:if
	
			test="${param.bxId eq 'admOpinBx'}">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="ap.cfg.apvr" alt="결재자" /></td>
		<td><u:input id="apvrNm" titleId="ap.cfg.apvr" alt="결재자" value="${param.apvrNm}" onkeyup="enterSubmit(event)" style="width:200px;" maxByte="50"/></td>
		<td class="width20"></td>
		<td class="search_tit"><u:msg titleId="cols.prgStat" alt="진행상태" /></td>
		<td><select name="prgStat" onchange="onChangePrgStat(this.value)">
			<u:option value="apvd" titleId="ap.term.apvd" alt="승인" checkValue="${param.prgStat}" />
			<u:option value="rejt" titleId="ap.term.rejt" alt="반려" checkValue="${param.prgStat}" />
			<u:option value="ongo" titleId="ap.tran.processing" alt="진행중" checkValue="${param.prgStat}" /></select></td>
		<td class="width20"></td>
		<td class="search_tit" id="durCatArea"><c:if
			test="${param.prgStat eq 'rejt'}"><span id="durCatTxt"><u:msg titleId="ap.list.rejtDd" alt="반려일자" /></span><u:input type="hidden" name="durCat" value="cmplDt" /></c:if><c:if
			test="${param.prgStat eq 'ongo'}"><span id="durCatTxt"><u:msg titleId="ap.doc.makDd" alt="기안일자" /></span><u:input type="hidden" name="durCat" value="makDt" /></c:if><c:if
			test="${not(param.prgStat eq 'rejt' or param.prgStat eq 'ongo')}"><span id="durCatTxt"><u:msg titleId="ap.list.cmplDd" alt="완결일자" /></span><u:input type="hidden" name="durCat" value="cmplDt" /></c:if></td>
		<td width="5px"></td>
		<td><u:calendar
			titleId="cm.cal.startDd" alt="시작일자"
			id="durStrtDt" value="${param.durStrtDt}" option="{end:'durEndDt'}" /></td>
			<td style="padding:0 3px 0 6px;"> ~ </td>
			<td><u:calendar
			titleId="cm.cal.endDd" alt="종료일자"
			id="durEndDt" value="${param.durEndDt}" option="{start:'durStrtDt'}" />
		</td>
		<td class="width20"></td>
		<td><u:buttonS titleId="cm.option.config" alt="설정" onclick="setOpinApvrLst()" /></td><c:if
			test="${not empty apvrUidCnt}">
		<td class="width5"></td>
		<td class="body_lt">( ${apvrUidCnt} )</td></c:if>
		</tr>
		</table></c:if><c:if
		
			test="${param.bxId ne 'admOpinBx'}">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><u:buttonIcon alt="검색 조건 펼치기" titleId="cm.ico.showCondi" image="ico_wdown.png" onclick="$('#searchArea1').toggle(); $('#searchArea2').toggle();" /></td>
		<td><select id="schCat" name="schCat" >
			<u:option value="srchDocNo" titleId="ap.doc.docNo" alt="문서번호" selected="${param.schCat == 'srchDocNo'}" /><c:if
				test="${param.bxId == 'admRecvRecLst'}">
			<u:option value="srchRecvDocNo" titleId="ap.doc.recvNo" alt="접수번호" selected="${param.schCat == 'srchRecvDocNo'}" /></c:if><c:if
				test="${param.bxId == 'admDistRecLst'}">
			<u:option value="srchRecvDocNo" titleId="ap.doc.distNo" alt="배부번호" selected="${param.schCat == 'srchRecvDocNo'}" /></c:if>
			<u:option value="docSubj" titleId="ap.doc.docSubj" alt="제목" selected="${param.schCat == 'docSubj' or empty param.schCat}" />
			<u:option value="makrNm" titleId="ap.doc.makrNm" alt="기안자" selected="${param.schCat == 'makrNm'}" />
			<u:option value="bodyHtml" titleId="ap.jsp.bodyHtml" alt="결재본문" selected="${param.schCat == 'bodyHtml'}" />
			<u:option value="formNm" titleId="ap.list.formNm" alt="양식명" selected="${param.schCat == 'formNm'}" />
			</select></td>
		<td><u:input id="schWord" value="${param.schWord}" titleId="cols.schWord" style="width: 260px;" maxByte="50" /></td>
		</tr>
		</table></c:if>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form></div>
	
	<div id="searchArea2" style="<c:if test="${empty param.srchDetl}">display:none;</c:if>">
	<form id="searchForm2" name="searchForm2" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="bxId" value="${param.bxId}" /><c:forEach items="${arrMnuParam}" var="mnuParam">
	<u:input type="hidden" id="${mnuParam[0]}" value="${mnuParam[1]}" /></c:forEach><c:if
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
			test="${param.bxId eq 'admRecvRecLst'}">
		<td class="search_tit"><u:msg titleId="ap.doc.recvNo" alt="접수번호" /></td>
		<td><u:input id="srchRecvDocNo" titleId="ap.doc.recvNo" alt="접수번호" value="${param.srchRecvDocNo}" style="width:200px;" maxByte="30"/></td>
		<td class="width20"></td></c:if><c:if
			test="${param.bxId eq 'admDistRecLst'}">
		<td class="search_tit"><u:msg titleId="ap.doc.distNo" alt="배부번호" /></td>
		<td><u:input id="srchRecvDocNo" titleId="ap.doc.distNo" alt="배부번호" value="${param.srchRecvDocNo}" style="width:200px;" maxByte="30"/></td>
		<td class="width20"></td></c:if>
		<td class="search_tit"><u:msg titleId="ap.doc.docNo" alt="문서번호" /></td>
		<td><u:input id="srchDocNo" titleId="ap.doc.docNo" alt="문서번호" value="${param.srchDocNo}" style="width:200px;" maxByte="30"/></td>
		<td class="width20"></td><c:if
			test="${not(param.bxId eq 'admRecvRecLst' or param.bxId eq 'admDistRecLst')}">
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
			test="${param.bxId eq 'admRegRecLst' or param.bxId eq 'admApvdBx'}">
		<td class="search_tit"><u:msg titleId="ap.list.cmplrNm" alt="완결자" /></td>
		<td><u:input id="cmplrNm" titleId="ap.list.cmplrNm" alt="완결자" value="${param.cmplrNm}" style="width:200px;" maxByte="30"/></td>
		<td class="width20"></td></c:if><c:if
			test="${param.bxId eq 'admRejtBx'}">
		<td class="search_tit"><u:msg titleId="ap.list.rejtrNm" alt="반려자" /></td>
		<td><u:input id="cmplrNm" titleId="ap.list.rejtrNm" alt="반려자" value="${param.cmplrNm}" style="width:200px;" maxByte="30"/></td>
		<td class="width20"></td></c:if><c:if
			test="${param.bxId eq 'admOngoBx'}">
		<td class="search_tit"><u:msg titleId="ap.list.curApvrNm" alt="처리자" /></td>
		<td><u:input id="curApvrNm" titleId="ap.list.curApvrNm" alt="처리자" value="${param.curApvrNm}" style="width:200px;" maxByte="30"/></td>
		<td class="width20"></td></c:if><c:if
			test="${param.bxId eq 'admRecvRecLst' or param.bxId eq 'admDistRecLst'}">
		<td class="search_tit"><u:msg titleId="ap.list.formNm" alt="양식명" /></td>
		<td><u:input id="formNm" titleId="ap.list.formNm" alt="양식명" value="${param.formNm}" style="width:200px;" maxByte="50"/></td>
		<td class="width20"></td></c:if><c:if
			test="${not(param.bxId eq 'admRegRecLst' or param.bxId eq 'admApvdBx'
			or param.bxId eq 'admRejtBx' or param.bxId eq 'admOngoBx'
			or param.bxId eq 'admRecvRecLst' or param.bxId eq 'admDistRecLst')}">
		<td colspan="3"></td></c:if>
		</tr>
		
		<tr>
		<td class="search_tit"><u:msg titleId="ap.doc.docTypNm" alt="문서구분" /></td>
		<td><select id="docTypCd" name="docTypCd"<u:elemTitle titleId="ap.doc.docTypNm" alt="문서구분" />>
			<option value=""><u:msg titleId="cm.option.all" alt="전체" /></option><c:forEach
				items="${docTypCdList}" var="docTypCd"><c:if test="${docTypCd.cd != 'paper'
					or (param.bxId == 'admRegRecLst' or param.bxId == 'admApvdBx' or param.bxId == 'admRecvRecLst')}"
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
		<td class="width20"></td><c:if
			test="${param.bxId == 'admRecvRecLst' or param.bxId == 'admDistRecLst'}">
		<td colspan="3"></td></c:if>
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
		<td class="width20"></td><c:if
			test="${param.bxId == 'admRecvRecLst' or param.bxId == 'admDistRecLst'}">
		<td colspan="3"></td></c:if>
		</tr><c:if
		
			test="${param.bxId == 'admOngoBx' or param.bxId == 'admOngoFormBx'
				or param.bxId == 'admRegRecLst' or param.bxId == 'admApvdBx' or param.bxId == 'admApvdFormBx'
				or param.bxId == 'admRecvRecLst' or param.bxId == 'admDistRecLst'
				or param.bxId == 'admRejtBx'}">
		<tr>
			<td class="search_tit"><c:if
			test="${
				   param.bxId == 'admApvdBx' or param.bxId == 'admRegRecLst' or param.bxId == 'admApvdFormBx'
				or param.bxId == 'admRecvRecLst' or param.bxId == 'admDistRecLst'
				or param.bxId == 'admRejtBx'}"><u:msg titleId="ap.cols.prd" alt="기간" /></c:if><c:if
			test="${not (
				   param.bxId == 'admApvdBx' or param.bxId == 'admRegRecLst' or param.bxId == 'admApvdFormBx'
				or param.bxId == 'admRecvRecLst' or param.bxId == 'admDistRecLst'
				or param.bxId == 'admRejtBx')}"><u:msg titleId="ap.doc.makDd" alt="기안일자" /><u:input type="hidden" id="durCat"
				value="makDt" /></c:if></td>
			<td colspan="4">
				<table border="0" cellpadding="0" cellspacing="0"><tr><c:if
				test="${
					   param.bxId == 'admApvdBx' or param.bxId == 'admRegRecLst' or param.bxId == 'admApvdFormBx'
					or param.bxId == 'admRecvRecLst' or param.bxId == 'admDistRecLst'
					or param.bxId == 'admRejtBx'}">
				<td><select id="durCat" name="durCat"<u:elemTitle titleId="ap.cols.prd" alt="기간" />>
					<u:option value="makDt" titleId="ap.doc.makDd" alt="기안일자" selected="${param.durCat eq 'makDt'}" /><c:if
						test="${param.bxId == 'admApvdBx' or param.bxId == 'admRegRecLst' or param.bxId == 'admApvdFormBx'}">
					<u:option value="cmplDt" titleId="ap.list.cmplDd" alt="완결일자" selected="${param.durCat eq 'cmplDt'}" /></c:if><c:if
						test="${param.bxId == 'admRecvRecLst'}">
					<u:option value="recvDt" titleId="ap.list.recvDd" alt="접수일자" selected="${param.durCat eq 'recvDt'}" /></c:if><c:if
						test="${param.bxId == 'admDistRecLst'}">
					<u:option value="recvDt" titleId="ap.list.distDd" alt="배부일자" selected="${param.durCat eq 'recvDt'}" /></c:if><c:if
						test="${param.bxId == 'admRejtBx'}">
					<u:option value="cmplDt" titleId="ap.list.rejtDd" alt="반려일자" selected="${param.durCat eq 'cmplDt'}" /></c:if>
					</select></td>
				<td width="5px"></td></c:if>
				<td><u:calendar
				titleId="cm.cal.startDd" alt="시작일자"
				id="durStrtDt" value="${param.durStrtDt}" option="{end:'durEndDt'}" /></td>
				<td style="padding:0 3px 0 6px;"> ~ </td>
				<td><u:calendar
				titleId="cm.cal.endDd" alt="종료일자"
				id="durEndDt" value="${param.durEndDt}" option="{start:'durStrtDt'}" />
				</td>
				</tr></table></td>
			<td class="width20"></td><c:if
			test="${param.bxId == 'recvRecLst' or param.bxId == 'distRecLst'}">
		<td colspan="3"></td></c:if>
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

<div class="front">
	<div class="front_right">
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr><c:if
			test="${(param.bxId == 'admApvdBx' or param.bxId == 'admRegRecLst') and sessionScope.userVo.userUid eq 'U0000001'}">
		<td class="frontbtn"><u:buttonS titleId="cm.btn.download" alt="다운로드" href="javascript:openDownloadPop();" /></c:if><c:if
			test="${not empty hasDelPw}">
		<td class="frontbtn"><u:buttonS titleId="ap.btn.delDocPw" alt="비밀번호 해제" href="javascript:delDocPw();" auth="A" /></td></c:if><c:if
		
			test="${param.bxId == 'admOngoBx' or param.bxId == 'admOngoFormBx'
				or param.bxId == 'admApvdBx' or param.bxId == 'admRegRecLst' or param.bxId == 'admApvdFormBx'
				or param.bxId == 'admRecvRecLst' or param.bxId == 'admDistRecLst'
				or param.bxId == 'admRejtBx' or param.bxId == 'admOpinBx'}">
		<td class="frontbtn"><u:buttonS alt="상세정보" titleId="ap.btn.detlInfo" href="javascript:openDetl();" /></td></c:if><c:if
			test="${param.bxId == 'admOpinBx'}">
		<td class="frontbtn"><u:buttonS alt="엑셀다운" titleId="cm.btn.excelDown" href="javascript:excelDownFile();" auth="A" /></td></c:if>
		</tr>
		</tbody></table>
	</div>
</div>

<%// 목록 %>
<form id="listApvForm">

<u:listArea id="listArea" tableStyle="table-layout:fixed;">
	<tr><c:set
			var="colCount" value="1"  />
		<td width="2.5%" class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('listApvForm', this.checked);" value=""/></td><c:forEach
		items="${ptLstSetupDVoList}" var="ptLstSetupDVo" varStatus="status"
		><c:if test="${ptLstSetupDVo.dispYn == 'Y'}"><c:set
			var="colCount" value="${colCount + 1}"  />
		<td width="${ptLstSetupDVo.wdthPerc}" class="head_ct"><c:if
			test="${param.bxId eq 'admOpinBx' and ptLstSetupDVo.msgId eq 'ap.list.cmplDd'}"><c:if
				test="${param.prgStat eq 'rejt'}"><u:msg titleId="ap.list.rejtDd" alt="반려일자" /></c:if><c:if
				test="${param.prgStat eq 'ongo'}"><u:msg titleId="ap.doc.makDd" alt="기안일자" /></c:if><c:if
				test="${not(param.prgStat eq 'rejt' or param.prgStat eq 'ongo')}"><u:msg titleId="ap.list.cmplDd" alt="완결일자" /></c:if></c:if><c:if
			test="${not(param.bxId eq 'admOpinBx' and ptLstSetupDVo.msgId eq 'ap.list.cmplDd')}"><u:msg titleId="${ptLstSetupDVo.msgId}" /></c:if></td></c:if></c:forEach>
	</tr>
<c:if test="${recodeCount == 0}">
	<tr>
		<td class="nodata" colspan="${colCount}"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
	
<c:forEach items="${apOngdBVoMapList}" var="apOngdBVoMap" varStatus="outStatus"><c:set
	var="apOngdBVoMap" value="${apOngdBVoMap}" scope="request" />
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
		<td class="bodybg_ct"><input type="checkbox" name="apvNo" value="${apOngdBVoMap.apvNo
			}" data-apvLnPno="${apOngdBVoMap.apvLnPno
			}" data-apvLnNo="${apOngdBVoMap.apvLnNo
			}" data-docPw="${not empty apOngdBVoMap.docPwEnc and apOngdBVoMap.makrUid != sessionScope.userVo.userUid ? 'Y' : ''
			}" data-docPwWithMine="${not empty apOngdBVoMap.docPwEnc ? 'Y' : ''
			}" data-docTypCd="${apOngdBVoMap.docTypCd
			}" data-sendSeq="${apOngdBVoMap.sendSeq
			}" data-docStatCd="${apOngdBVoMap.docStatCd
			}" data-docStatNm="${apOngdBVoMap.docStatNm
			}" data-apvrRoleCd="${apOngdBVoMap.apvrRoleCd
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
					/>
			</c:if><div class="ellipsis" title="<u:convertMap
				srcId="apOngdBVoMap" attId="${ptLstSetupDVo.atrbId}" type="value" />"><c:if
						test="${ptLstSetupDVo.atrbId == 'docNo' and (bxId=='recvRecLst' or bxId=='distRecLst')}"
					><u:convertMap
				srcId="apOngdBVoMap" attId="${ptLstSetupDVo.atrbId}" type="html" /></c:if><c:if
						test="${not (ptLstSetupDVo.atrbId == 'docNo' and (bxId=='recvRecLst' or bxId=='distRecLst'))}"
					><a href="javascript:${docOpenFunc}" style="${ptLstSetupDVo.atrbId == 'docSubj' ? apOngdBVoMap.fontStyle : ''}"><c:if
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
					
				test="${ptLstSetupDVo.atrbId == 'apvrNm'}"
			><c:if
			test="${not empty apOngdBVoMap.apvrUid}"
			><a href="javascript:${not empty isFrame ? 'parent.' : ''}viewUserPop('${apOngdBVoMap.apvrUid}')"><u:out
					value="${apOngdBVoMap.apvrNm}" type="html" /></a></c:if><c:if
			test="${empty apOngdBVoMap.apvrUid}"
			><u:out value="${apOngdBVoMap.apvrNm}" type="html" /></c:if></c:if><c:if
					
				test="${ptLstSetupDVo.atrbId == 'curApvrNm' and apOngdBVoMap.curApvrDeptYn != 'N'}"
			><u:out value="${apOngdBVoMap.curApvrNm}" type="html" /></c:if><c:if
			
				test="${ptLstSetupDVo.atrbId == 'attFileYn' and apOngdBVoMap.attFileYn == 'Y'}"
			><a href="javascript:${not empty isFrame ? 'parent.' : ''}viewAttach('${apOngdBVoMap.apvNo}')"><img alt="file download - pop"
					src="${_cxPth}/images/${_skin}/ico_file.png" /></a></c:if><c:if
					
				test="${ptLstSetupDVo.atrbId == 'makDd' or ptLstSetupDVo.atrbId == 'cmplDd'
					 or ptLstSetupDVo.atrbId == 'enfcDd' or ptLstSetupDVo.atrbId == 'recvDd'}"
			><c:if
				test="${param.bxId eq 'admOpinBx' and ptLstSetupDVo.atrbId eq 'cmplDd' and param.prgStat eq 'ongo'}"><u:convertMap srcId="apOngdBVoMap" type="date"
				attId="makDt" /></c:if><c:if
				test="${not(param.bxId eq 'admOpinBx' and ptLstSetupDVo.atrbId eq 'cmplDd' and param.prgStat eq 'ongo')}"><u:convertMap srcId="apOngdBVoMap" type="date"
				attId="${fn:substring(ptLstSetupDVo.atrbId, 0, fn:length(ptLstSetupDVo.atrbId)-2)}Dt" /></c:if></c:if><c:if
				
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
					and ptLstSetupDVo.atrbId != 'apvrNm'
					
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
<u:input type="hidden" id="bxId" value="${bxId}" /><c:forEach items="${arrMnuParam}" var="mnuParam">
<u:input type="hidden" id="${mnuParam[0]}" value="${mnuParam[1]}" /></c:forEach><c:if
	test="${not empty queryString}">
<u:input type="hidden" id="queryString" value="${queryString}" /></c:if>
</form>


