<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String onTab = request.getParameter("onTab");
	if(!"docInfo".equals(onTab) && !"apvInof".equals(onTab) && !"recvInfo".equals(onTab) && !"refVw".equals(onTab)) onTab = "docInfo";
	request.setAttribute("onTab", onTab);
%>
<script type="text/javascript">
<!--<%
// 본문 이력 조회 %>
function viewBodyHis(apvNo, bodyHstNo){
	dialog.open("viewBodyHisDialog", '<u:msg titleId="ap.jsp.viewPrevBody" alt="변경전 본문 조회" />', "./viewBodyHisPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo+"&bodyHstNo="+bodyHstNo);
}<%
// 결재 경로 이력 조회 %>
function viewApvLnHis(apvNo, apvLnPno, apvLnHstNo){
	dialog.open("viewApvLnHisDialog", '<u:msg titleId="ap.jsp.viewPrevApvLn" alt="변경전 결재 경로 조회" />', "./viewApvLnHisPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo+"&apvLnPno="+apvLnPno+"&apvLnHstNo="+apvLnHstNo);
}<%
// 첨부 파일 이력 조회 %>
function viewAttchHis(apvNo, attHstNo){
	dialog.open("viewAttHisDialog", '<u:msg titleId="ap.jsp.viewPrevAtt" alt="변경전 첨부 파일 조회" />', "./viewAttHisPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo="+apvNo+"&attHstNo="+attHstNo);
}<%
// 전체경로 버튼 - 숨기기/보이기 %>
function toggleAllLnBtn(on){
	if(on) $("#allApvLn").show();
	else $("#allApvLn").hide();
}<%
// 보안등급 변경 %>
function setSeculCd(){
	var popTitle = '<u:msg alt="보안등급 변경" titleId="ap.btn.setSeculCd" />';
	dialog.open('setSeculCdDialog', popTitle, "./setSeculCdPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&seculCd=${apvData.seculCd}");
}<%
// 통보추가 %>
function addInfm(){
	var opt = {data:null, multi:true, mode:'search', title:'<u:term termId="ap.term.addInfm" alt="통보추가" />'};
	if('${optConfigMap.apvrFromOtherComp}'=='Y') opt['foreign'] = 'Y';
	searchUserPop(opt, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, userVo){
				buffer.push(userVo.userUid);
			});
			
			var result = false;
			callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {process:"addInfm", apvNo:"${param.apvNo}", apvrUids:buffer.join(',')}, function(data){
				if(data.message != null) alert(data.message);
				result = data.result == 'ok';
			});
			if(result) openDetl('apvInof');
		} else {<%
			// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.
			// cols.user=사용자%>
			alertMsg('cm.msg.noSelectedItem',['#cols.user']);
			return false;
		}
	});
}<%
// 통보추가 버튼 - 숨기기/보이기 %>
function toggleAddInfmBtn(on){
	if(on) $("#addInfmBtn").show();
	else $("#addInfmBtn").hide();
}<%
// 되돌리기 %>
function turnBackDoc(){
	var popTitle = '<u:msg alt="되돌리기" titleId="cm.btn.turnBack" />';
	dialog.open('setTurnBackDialog', popTitle, "./setTurnBackPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}");
}<%--
// 완결 취소 : 관리자용 --%>
function processCalcelCmpl(){
	
	var result = true;
	var notiDoc = false;
	var param = {apvNo:"${param.apvNo}"};
	callAjax("./checkCancelCmplAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", param, function(data){
		if(data.message != null){
			alert(data.message);
			result = false;
		}
		notiDoc = "Y" == data.notiDoc;
	});
	
	if(!result){
		return;
	} else if(notiDoc){<%-- ap.cfm.cancelNotiDoc=연계된 문서입니다. 완결을 취소 하시겠습니까 ? --%>
		if(!confirmMsg("ap.cfm.cancelNotiDoc")){<%-- ap.cfm.cancelNotiDoc=연계된 문서입니다. 완결을 취소 하시겠습니까 ? --%>
			return;
		}
	} else if(!confirmMsg("ap.cfm.cancelCmpl")){<%-- ap.cfm.cancelCmpl=완결을 취소 하시겠습니까 ? --%>
		return;
	}
	
	result = false;
	param = {process:"cancelCmpl", apvNo:"${param.apvNo}", force:"Y"};
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", param, function(data){
		if(data.message != null) alert(data.message);
		result = (data.result == 'ok');
	});
	if(result) toList();
}
//$(document).ready(function() {
//});
//-->
</script>

<div style="width:730px"><u:set
	test="${not empty param.apvLnPno and param.apvLnPno != '0'}" var="tabFunc" value="toggleAllApvLn(this);" elseValue="" >
</u:set>

<u:tabGroup id="docDetlTab" noBottomBlank="true">
	<u:tab id="docDetlTab" alt="문서정보" areaId="docInfoDetlArea" titleId="ap.tab.docInfo"
		on="${onTab == 'docInfo' or empty onTab}"
		onclick="${not empty param.apvLnPno and param.apvLnPno != '0' ? 'toggleAllLnBtn(false);' : ''
			}${(param.bxId == 'myBx' and sessionScope.userVo.userUid == apvData.makrUid) or (not empty apAdmAddInfm) ? 'toggleAddInfmBtn(false);' : ''}" /><c:if
	test="${param.bxId != 'recvBx' and param.bxId != 'distBx'
		and param.bxId != 'distRecLst' and param.bxId != 'admDistRecLst'
		and not (param.bxId == 'recvRecLst' and apvData.docStatCd == 'recv')}">
	<u:tab id="docDetlTab" alt="결재정보" areaId="apvInofDetlArea" titleId="ap.tab.apvInof"
		on="${onTab == 'apvInof'}"
		onclick="${not empty param.apvLnPno and param.apvLnPno != '0' ? 'toggleAllLnBtn(true);' : ''
			}${(param.bxId == 'myBx' and sessionScope.userVo.userUid == apvData.makrUid) or (not empty apAdmAddInfm) ? 'toggleAddInfmBtn(true);' : ''}" /></c:if><c:if
	test="${not empty apOngdRefVwDVoList and not (
		apvData.recLstTypCd eq 'recvRecLst' or apvData.recLstTypCd eq 'distRecLst'
		or param.bxId eq 'recvBx' or param.bxId eq 'distBx'
		)}">
	<u:tab id="docDetlTab" alt="참조열람" areaId="refVwDetlArea" termId="ap.term.refVw"
		on="${onTab == 'refVw'}"
		onclick="${not empty param.apvLnPno and param.apvLnPno != '0' ? 'toggleAllLnBtn(false);' : ''
			}${(param.bxId == 'myBx' and sessionScope.userVo.userUid == apvData.makrUid) or (not empty apAdmAddInfm) ? 'toggleAddInfmBtn(false);' : ''}" /></c:if><c:if
	test="${apvData.recLstTypCd == 'regRecLst'
		and (param.bxId == 'myBx' or param.bxId == 'sendBx' or param.bxId == 'regRecLst')
		and (not empty reqCensrApOngdPichDVo or not empty censrApOngdPichDVo) }">
	<u:tab id="docDetlTab" alt="심사정보" areaId="censrInfoDetlArea" titleId="ap.tab.censrInfo"
		onclick="${not empty param.apvLnPno and param.apvLnPno != '0' ? 'toggleAllLnBtn(false);' : ''
			}${(param.bxId == 'myBx' and sessionScope.userVo.userUid == apvData.makrUid) or (not empty apAdmAddInfm) ? 'toggleAddInfmBtn(false);' : ''}" /></c:if><c:if
	test="${apvData.docTypCd != 'intro' and (param.bxId == 'recvBx' 
			or (apvData.recLstTypCd != 'recvRecLst' and apvData.recLstTypCd != 'distRecLst'))}">
	<u:tab id="docDetlTab" alt="수신처정보" areaId="recvInfoDetlArea" titleId="ap.tab.recvInfo"
		on="${onTab == 'recvInfo'}"
		onclick="${not empty param.apvLnPno and param.apvLnPno != '0' ? 'toggleAllLnBtn(false);' : ''
			}${(param.bxId == 'myBx' and sessionScope.userVo.userUid == apvData.makrUid) or (not empty apAdmAddInfm) ? 'toggleAddInfmBtn(false);' : ''}" /></c:if><c:if
	test="${apvData.recLstTypCd == 'distRecLst' and param.bxId != 'recvBx' }">
	<u:tab id="docDetlTab" alt="배부정보" areaId="distInfoDetlArea" titleId="ap.tab.distInfo"
		onclick="${not empty param.apvLnPno and param.apvLnPno != '0' ? 'toggleAllLnBtn(false);' : ''
			}${(param.bxId == 'myBx' and sessionScope.userVo.userUid == apvData.makrUid) or (not empty apAdmAddInfm) ? 'toggleAddInfmBtn(false);' : ''}" /></c:if><c:if
		test="${not empty param.apvLnPno and param.apvLnPno != '0' and param.bxId != 'deptBx' and param.bxId != 'apvdBx'}">
	<u:tabButton id="allApvLn" alt="전체경로" titleId="ap.btn.allApvLn" href="javascript:viewApvLnPop('${param.apvNo}');" style="${onTab != 'apvInof' ? 'display:none' : ''}" /></c:if>
</u:tabGroup>

<u:tabArea outerStyle="height:350px; overflow-x:hidden; overflow-y:auto;" innerStyle="margin:10px;" >
<div id="docInfoDetlArea" style="<u:set test="${onTab == 'docInfo' or empty onTab}" value="" elseValue="display:none;" />"><!--문서정보-->
<u:listArea colgroup="17%,33%,17%,33%">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td colspan="3" class="body_lt"><u:out value="${apvData.docSubj}" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.formNm" alt="양식명" /></td>
	<td colspan="3" class="body_lt"><u:out value="${apvData.formNm}" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.docNo" alt="문서번호" /></td>
	<td class="body_lt"><u:out value="${apvData.docNo}" /></td>
	<td class="head_lt"><u:msg titleId="ap.doc.docKeepPrdNm" alt="보존연한" /></td>
	<td class="body_lt"><u:out value="${apvData.docKeepPrdNm}" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.ugntDoc" alt="긴급문서" /></td>
	<td class="body_lt"><c:if
		test="${apvData.ugntDocYn == 'Y'}" ><u:msg titleId="ap.option.ugnt" alt="긴급" langTypCd="${apvData.docLangTypCd}" /></c:if><c:if
		test="${apvData.ugntDocYn != 'Y'}" ><u:msg titleId="ap.option.mdrt" alt="보통" langTypCd="${apvData.docLangTypCd}" /></c:if></td>
	<td class="head_lt"><u:msg titleId="cols.secu" alt="보안" /></td>
	<td class="body_lt"><c:if
		test="${not empty apvData.seculNm}" ><u:out value="${apvData.seculNm}" nullValue="" /> </c:if><c:if
		test="${not empty apvData.docPwEnc}" >[<u:msg titleId="cols.pw" alt="비밀번호" langTypCd="${apvData.docLangTypCd}" />]</c:if></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.readScop" alt="열람범위" /></td>
	<td class="body_lt"><c:if
		test="${apvData.allReadYn == 'Y'}" ><u:msg titleId="ap.option.all" alt="전체" langTypCd="${apvData.docLangTypCd}" /></c:if><c:if
		test="${apvData.allReadYn != 'Y'}" ><u:msg titleId="ap.option.dept" alt="부서" langTypCd="${apvData.docLangTypCd}" /></c:if></td>
	<td class="head_lt"><u:msg titleId="cols.rgstReg" alt="대장등록" /></td>
	<td class="body_lt"><c:if
		test="${apvData.regRecLstRegYn == 'Y'}" ><u:msg titleId="ap.option.regY" alt="등록" langTypCd="${apvData.docLangTypCd}" /></c:if><c:if
		test="${apvData.regRecLstRegYn != 'Y'}" ><u:msg titleId="ap.option.regN" alt="미등록" langTypCd="${apvData.docLangTypCd}"
			/><c:if test="${not empty apvData.regRecLstRegSkedYmd}"> (<u:msg titleId="ap.jsp.skedDt" alt="예약 등록일" /> : ${apvData.regRecLstRegSkedYmd})</c:if></c:if></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.docTyp" alt="문서구분" /></td>
	<td class="body_lt"><u:out value="${apvData.docTypNm}" /></td>
	<td class="head_lt"><u:msg titleId="cols.enfcScop" alt="시행범위" /></td>
	<td class="body_lt"><u:out value="${apvData.enfcScopNm}" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="cols.clsInfo" alt="분류정보" /></td>
	<td class="body_lt"><u:out value="${apvData.clsInfoNm}" /></td>
	<td class="head_lt"><u:msg titleId="cols.docLang" alt="문서언어" /></td>
	<td class="body_lt"><u:out value="${apvData.docLangTypNm}" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="ap.doc.makrNm" alt="기안자" /></td>
	<td class="body_lt"><c:if test="${not empty apvData.makrNm}"
		><c:if
			test="${not empty apvData.makrUid}"
			><a href="javascript:viewUserPop('${apvData.makrUid}');"><u:out value="${apvData.makrNm}" /></a></c:if><c:if
			test="${empty apvData.makrUid}"
			><u:out value="${apvData.makrNm}" /></c:if></c:if></td>
	<td class="head_lt"><u:msg titleId="ap.doc.makDt" alt="기안일시" /></td>
	<td class="body_lt"><u:out value="${apvData.makDt}" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="ap.list.cmplrNm" alt="완결자" /></td>
	<td class="body_lt"><c:if test="${not empty apvData.cmplrNm}"
		><c:if
			test="${not empty apvData.cmplrUid}"
			><a href="javascript:viewUserPop('${apvData.cmplrUid}');"><u:out value="${apvData.cmplrNm}" /></a></c:if><c:if
			test="${empty apvData.cmplrUid}"
			><u:out value="${apvData.cmplrNm}" /></c:if></c:if></td>
	<td class="head_lt"><u:msg titleId="ap.list.cmplDt" alt="완결일시" /></td>
	<td class="body_lt"><u:out value="${apvData.cmplDt}" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="ap.doc.enfcDt" alt="시행일시" /></td>
	<td class="body_lt"><u:out value="${apvData.enfcDt}" /></td>
	<td class="head_lt"><u:msg titleId="ap.doc.enfcDocKeepPrdNm" alt="시행문보존연한" /></td>
	<td class="body_lt"><u:out value="${apvData.enfcDocKeepPrdNm}" /></td>
	</tr><c:if
		test="${apvData.recLstTypCd == 'distRecLst'}">
	<tr>
	<td class="head_lt"><u:msg titleId="ap.doc.distNo" alt="배부번호" /></td>
	<td class="body_lt"><u:out value="${apvData.recvDocNo}" /></td>
	<td class="head_lt"><u:msg titleId="ap.list.distDt" alt="배부일시" /></td>
	<td class="body_lt"><u:out value="${apvData.recvDt}" /></td>
	</tr></c:if><c:if
		test="${apvData.recLstTypCd == 'recvRecLst'}">
	<tr>
	<td class="head_lt"><u:msg titleId="ap.doc.recvNo" alt="접수번호" /></td>
	<td class="body_lt"><u:out value="${apvData.recvDocNo}" /></td>
	<td class="head_lt"><u:msg titleId="ap.list.recvDt" alt="접수일시" /></td>
	<td class="body_lt"><u:out value="${apvData.recvDt}" /></td>
	</tr></c:if><c:if
		test="${not empty sendToDmNm}">
	<tr>
	<td class="head_lt"><u:msg titleId="ap.btn.sendToDm" alt="문서관리" /></td>
	<td class="body_lt" colspan="3"><u:out value="${sendToDmNm}" /></td>
	</tr></c:if>
</u:listArea>

</div>

<div id="apvInofDetlArea" style="<u:set test="${onTab == 'apvInof'}" value="" elseValue="display:none;" />"><!--결재정보-->
<jsp:include page="./viewApvLnInc.jsp" flush="false" />
</div>

<div id="refVwDetlArea" style="<u:set test="${onTab == 'refVw'}" value="" elseValue="display:none;" />"><!--참조열람-->
<c:if test="${not empty apOngdRefVwDVoList and not (
	apvData.recLstTypCd eq 'recvRecLst' or apvData.recLstTypCd eq 'distRecLst'
	or param.bxId eq 'recvBx' or param.bxId eq 'distBx')}"><jsp:include page="./viewRefVwInc.jsp" flush="false" /></c:if>
</div>

<div id="censrInfoDetlArea" style="<u:set test="${onTab == 'censrInfo'}" value="" elseValue="display:none;" />"><!--심사정보-->
<u:listArea colgroup="17%,33%,17%,33%"><c:if test="${not empty reqCensrApOngdPichDVo}">
	<tr>
	<td class="head_lt"><u:msg titleId="ap.jsp.reqCensrUser" alt="심사요청자" /></td>
	<td class="body_lt"><c:if
			test="${not empty reqCensrApOngdPichDVo.pichUid}"
			><a href="javascript:viewUserPop('${reqCensrApOngdPichDVo.pichUid}');"><u:out value="${reqCensrApOngdPichDVo.pichNm}" /></a></c:if><c:if
			test="${empty reqCensrApOngdPichDVo.pichUid}"
			><u:out value="${reqCensrApOngdPichDVo.pichNm}" /></c:if></td>
	<td class="head_lt"><u:msg titleId="ap.jsp.reqCensrDt" alt="심사요청일시" /></td>
	<td class="body_lt"><u:out value="${reqCensrApOngdPichDVo.hdlDt}" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="ap.btn.reqCensrOpin" alt="심사요청의견" /></td>
	<td class="body_lt" colspan="3"><u:out value="${reqCensrApOngdPichDVo.pichOpinCont}" /></td>
	</tr></c:if><c:if test="${not empty censrApOngdPichDVo}">
	<tr>
	<td class="head_lt"><u:msg titleId="ap.jsp.censrUser" alt="심사자" /></td>
	<td class="body_lt"><c:if
			test="${not empty censrApOngdPichDVo.pichUid}"
			><a href="javascript:viewUserPop('${censrApOngdPichDVo.pichUid}');"><u:out value="${censrApOngdPichDVo.pichNm}" /></a></c:if><c:if
			test="${empty censrApOngdPichDVo.pichUid}"
			><u:out value="${censrApOngdPichDVo.pichNm}" /></c:if></td>
	<td class="head_lt"><u:msg titleId="ap.jsp.censrDt" alt="심사일시" /></td>
	<td class="body_lt"><u:out value="${censrApOngdPichDVo.hdlDt}" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="ap.btn.censrOpin" alt="심사의견" /></td>
	<td class="body_lt" colspan="3"><u:out value="${censrApOngdPichDVo.pichOpinCont}" /></td>
	</tr></c:if>
</u:listArea>
</div>

<div id="recvInfoDetlArea" style="<u:set test="${onTab == 'recvInfo'}" value="" elseValue="display:none;" />"><!--수신처정보-->

<u:listArea style="overflow:auto;" id="recvDeptDataPopArea" colgroup="${apvData.docProsStatCd=='apvd' ? '35%,35%,15%,15%' : '35%,35%,30%'}">
	<tr id="titleTr">
		<td class="head_ct"><u:msg titleId="cols.recvDept" alt="수신처" /></td>
		<td class="head_ct"><u:msg titleId="cols.refDept" alt="참조처" /></td>
		<td class="head_ct"><u:msg titleId="ap.jsp.recvDept.recvTyp" alt="수신구분" /></td><c:if
			test="${apvData.docProsStatCd=='apvd'}">
		<td class="head_ct"><u:msg titleId="ap.btn.sendAddDoc" alt="추가발송" /></td></c:if>
	</tr>
<c:if test="${fn:length(apOngdRecvDeptLVoList)==0}">
	<tr>
		<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:forEach items="${apOngdRecvDeptLVoList}" var="apOngdRecvDeptLVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="body_ct"><u:out value="${apOngdRecvDeptLVo.recvDeptNm}" /></td>
		<td class="body_ct"><u:out value="${apOngdRecvDeptLVo.refDeptNm}" /></td>
		<td class="body_ct"><u:out value="${apOngdRecvDeptLVo.recvDeptTypNm}" /></td><c:if
			test="${apvData.docProsStatCd=='apvd'}">
		<td class="body_ct">${apOngdRecvDeptLVo.addSendYn == 'Y' ? 'Y' : ''}</td></c:if>
	</tr>
</c:forEach>
</u:listArea>

</div>

<c:if
	test="${apvData.recLstTypCd == 'distRecLst'}">
<div id="distInfoDetlArea" style="<u:set test="${onTab == 'distInfo'}" value="" elseValue="display:none;" />"><!--배부정보-->

<u:listArea style="overflow:auto;" id="recvDeptDataPopArea" colgroup="40%,30%,30%">
	<tr id="titleTr">
		<td class="head_ct"><u:msg titleId="cols.recvDept" alt="수신처" /></td>
		<td class="head_ct"><u:msg titleId="ap.jsp.recvDept.recvStat" alt="수신상태" /></td>
		<td class="head_ct"><u:msg titleId="ap.list.distDt" alt="배부일시" /></td>
	</tr>
<c:if test="${fn:length(apOngdSendDVoList)==0}">
	<tr>
		<td class="nodata" colspan="3"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:forEach items="${apOngdSendDVoList}" var="apOngdSendDVo">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="body_ct"><u:out value="${apOngdSendDVo.recvDeptNm}" /></td>
		<td class="body_ct" style="${apOngdSendDVo.hdlStatCd=='recvRetn' or apOngdSendDVo.hdlStatCd=='distRetn'
			? 'font-weight:bold' : ''}"><c:if
			test="${ (apOngdSendDVo.hdlStatCd=='recvRetn' or apOngdSendDVo.hdlStatCd=='distRetn')
				and not empty apOngdSendDVo.hdlrOpinCont }"><a href="javascript:viewRetnOpin('${apOngdSendDVo.sendSeq}');"><u:out value="${apOngdSendDVo.hdlStatNm}" /></a></c:if><c:if
			test="${ not( (apOngdSendDVo.hdlStatCd=='recvRetn' or apOngdSendDVo.hdlStatCd=='distRetn')
				and not empty apOngdSendDVo.hdlrOpinCont) }"><u:out value="${apOngdSendDVo.hdlStatNm}" /></c:if></td>
		<td class="body_ct"><u:out value="${apOngdSendDVo.prevApvrApvDt}" /></td>
	</tr>
</c:forEach>
</u:listArea>

</div>
</c:if>
<div>
</div>

</u:tabArea>

<u:buttonArea><c:if
		test="${not empty isAdminPage and (param.bxId == 'admApvdBx' or param.bxId == 'admRegRecLst' or param.bxId == 'admRecvRecLst')}">
	<u:button onclick="setSeculCd();" alt="보안등급 변경" titleId="ap.btn.setSeculCd" /></c:if><c:if
		test="${(param.bxId == 'myBx' and sessionScope.userVo.userUid == apvData.makrUid and apvData.docProsStatCd=='apvd')
			or (not empty apAdmAddInfm)}">
	<u:button onclick="addInfm();" alt="통보추가" termId="ap.term.addInfm" id="addInfmBtn" style="${onTab == 'apvInof' ? '' : 'display:none'}" /></c:if><c:if
		test="${sessionScope.userVo.userUid eq 'U0000001' and (param.bxId eq 'admRegRecLst' or param.bxId eq 'admApvdBx') and empty param.fromList}">
	<u:button onclick="deleteDoc();" alt="삭제" titleId="cm.btn.del" /></c:if><c:if
		test="${sessionScope.userVo.userUid eq 'U0000001' and param.bxId eq 'admOngoBx' and empty param.fromList}">
	<u:button onclick="turnBackDoc();" alt="되돌리기" titleId="cm.btn.turnBack" /></c:if><c:if
		test="${sessionScope.userVo.userUid eq 'U0000001' and (param.bxId eq 'admRegRecLst' or param.bxId eq 'admApvdBx') and apvData.enfcStatCd ne 'sent' and empty param.fromList}">
	<u:button onclick="processCalcelCmpl();" alt="완결 취소" titleId="ap.btn.cancelCmpl" /></c:if><c:if
		test="${sessionScope.userVo.userUid eq 'U0000001' and param.bxId eq 'admOngoBx' and not empty myTurnApOngdApvLnDVo}">
	<u:button onclick="openApvLn();" alt="결재선지정" titleId="ap.btn.setApvLn" /></c:if>
	<u:button onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</div>