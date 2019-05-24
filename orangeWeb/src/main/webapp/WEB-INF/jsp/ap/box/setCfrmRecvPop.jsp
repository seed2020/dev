<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 회수 %>
function retrvDoc(){<%
	// ap.msg.noRetrivDoc=회수가능한 문서가 없습니다. %>
	processCfrmRecv('retrvDoc', 'retrvYn', 'ap.msg.noRetrivDoc');
}<%
// 재발송 %>
function reSendDoc(){<%
	// ap.msg.noReSendDoc=재발송 가능한 문서가 없습니다. %>
	processCfrmRecv('reSendDoc', 'reSendYn', 'ap.msg.noReSendDoc');
}<%
// 수동완료처리 %>
function setManlSendCmpl(){<%
	// ap.msg.noManlCmplDoc=수동전송완료 처리 할 문서가 없습니다. %>
	processCfrmRecv('manlSendCmpl', 'manlCmpl', 'ap.msg.noManlCmplDoc');
}<%
// 회수,재발송,수동완료처리 %>
function processCfrmRecv(process, attrId, msgId){
	var sendSeqs = [], $chks = $("#cfrmRecvArea input[name='sendSeq']:checked");
	if($chks.length==0){<%
		// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다. - ap.cols.item=항목 %>
		alertMsg('cm.msg.noSelectedItem', ['#ap.cols.item']);
		return;
	} else {
		$("#cfrmRecvArea input[name='sendSeq']:checked").each(function(){
			if($(this).attr('data-'+attrId)=='Y'){
				sendSeqs.push($(this).val());
			}
		});
		if(sendSeqs.length==0){
			alertMsg(msgId);
			return;
		}
	}
	if(process=='manlSendCmpl'){
		var popTitle = '<u:msg titleId="ap.btn.manlSendCmpl" alt="수동완료처리"/>';
		dialog.open('setManlSendCmplDialog', popTitle, './setManlSendCmplPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&sendSeqs='+sendSeqs.join(','));
	} else {
		processCfrmRecvAjax(process, "${param.apvNo}", sendSeqs);
	}
}<%
//회수,재발송,수동완료처리 - AJAX 처리 %>
function processCfrmRecvAjax(process, apvNo, sendSeqs, hdlrNm, hdlDt){
	var data = {process:process, apvNo:apvNo, sendSeqs:sendSeqs};
	if(hdlrNm!=null) data['hdlrNm'] = hdlrNm;
	if(hdlDt !=null) data['hdlDt' ] = hdlDt;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", data, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result){
		dialog.open('setCfrmRecvDialog', '<u:msg titleId="ap.btn.cfrmRecv" alt="접수확인" />', './setCfrmRecvPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}');
	}
}
<%
// 반송의견 조회 %>
function viewRetnOpin(sendSeq){
	dialog.open('viewRetnOpinDialog', '<u:msg titleId="ap.jsp.retnOpin" alt="반송의견" />', './viewRetnOpinPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&sendSeq='+sendSeq);
}
//-->
</script>

<div style="width:${param.bxId != 'distRecLst' ? '800px' : '600px'}">
<form id="listDocRcovForm">

<div class="headbox">
<dl>
<dd class="headbox_body"><u:msg
	titleId="ap.cfg.send" alt="발송" />:${sendCnt}&nbsp;&nbsp;&nbsp; -&gt; &nbsp;&nbsp;&nbsp;<u:msg
	titleId="ap.jsp.recv" alt="수신" />:${cmplCnt}, &nbsp;<u:msg
	titleId="ap.cfg.sendBck" alt="반송" />:${retnCnt}, &nbsp;<u:msg
	titleId="ap.btn.retrvDoc" alt="회수" />:${retrvCnt}, &nbsp;<u:msg
	titleId="ap.jsp.dup" alt="중복" />:${dupCnt}, &nbsp;<u:msg
	titleId="ap.jsp.befo" alt="대기" />:${befoCnt} </dd>
</dl>
</div>

<u:blank />

<% // 목록 %>
<u:listArea id="cfrmRecvArea" colgroup="${param.bxId != 'distRecLst' ? '3%,19%,19%,9%,9%,9%,16%,16%' : '3%,25%,12%,16%,22%,22%'}" style="${sendCnt > 15 ? 'height:401px; overflow-y:auto' : ''}">
	<tr>
	<td class="head_bg"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all'
		/>" onclick="checkAllCheckbox('cfrmRecvArea', this.checked);" value=""/></td>
	<td class="head_ct"><u:msg titleId="cols.recvDept" alt="수신처" /></td><c:if
		test="${param.bxId != 'distRecLst'}">
	<td class="head_ct"><u:msg titleId="cols.refDept" alt="참조처" /></td>
	<td class="head_ct"><u:msg titleId="ap.jsp.recvDept.recvTyp" alt="수신구분" /></td></c:if>
	<td class="head_ct"><u:msg titleId="ap.jsp.recvDept.recvStat" alt="수신상태" /></td>
	<td class="head_ct"><u:msg titleId="ap.jsp.recvr" alt="접수자" /></td>
	<td class="head_ct"><u:msg titleId="ap.jsp.recvRetnDt" alt="접수/반송 일시" /></td>
	<td class="head_ct"><u:msg titleId="${param.bxId=='distRecLst' ? 'ap.list.distDt' : 'ap.jsp.sendDt'}" alt="배부일시/발송일시" /></td>
	</tr><c:forEach items="${apOngdSendDVoList}" var="apOngdSendDVo">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><input type="checkbox" name="sendSeq" value="${apOngdSendDVo.sendSeq
		}" data-retrvYn="${apOngdSendDVo.hdlStatCd=='befoRecv' or apOngdSendDVo.hdlStatCd=='befoDist'
			or apOngdSendDVo.hdlStatCd=='manl' or apOngdSendDVo.hdlStatCd=='manlCmpl'
			? 'Y' : 'N'
		}" data-reSendYn="${apOngdSendDVo.hdlStatCd=='recvRetn' or apOngdSendDVo.hdlStatCd=='recvRetrv'
			or apOngdSendDVo.hdlStatCd=='distRetn' or apOngdSendDVo.hdlStatCd=='distRetrv'
			or apOngdSendDVo.hdlStatCd=='manlRetrv'
			? 'Y' : 'N'
		}" data-manlCmpl="${apOngdSendDVo.hdlStatCd=='manl' or apOngdSendDVo.hdlStatCd=='manlRetrv' ? 'Y' : 'N'
		}"<c:if
			test="${apOngdSendDVo.hdlStatCd=='recvCmpl' or apOngdSendDVo.hdlStatCd=='distCmpl' or apOngdSendDVo.hdlStatCd=='dupSend'}"> disabled="disabled"</c:if> /></td>
	<td class="body_ct"><u:out value="${apOngdSendDVo.recvDeptNm}" /></td><c:if
		test="${param.bxId != 'distRecLst'}">
	<td class="body_ct"><u:out value="${apOngdSendDVo.refDeptNm}" /></td>
	<td class="body_ct"><u:out value="${apOngdSendDVo.recvDeptTypNm}" /></td></c:if>
	<td class="body_ct" style="${apOngdSendDVo.hdlStatCd=='recvRetn' or apOngdSendDVo.hdlStatCd=='distRetn'
		? 'font-weight:bold' : ''}"><c:if
		test="${ (apOngdSendDVo.hdlStatCd=='recvRetn' or apOngdSendDVo.hdlStatCd=='distRetn')
			and not empty apOngdSendDVo.hdlrOpinCont }"><a href="javascript:viewRetnOpin('${apOngdSendDVo.sendSeq}');"><u:out value="${apOngdSendDVo.hdlStatNm}" /></a></c:if><c:if
		test="${ not( (apOngdSendDVo.hdlStatCd=='recvRetn' or apOngdSendDVo.hdlStatCd=='distRetn')
			and not empty apOngdSendDVo.hdlrOpinCont) }"><u:out value="${apOngdSendDVo.hdlStatNm}" /></c:if></td>
	<td class="body_ct"><c:if
		test="${not empty apOngdSendDVo.hdlrUid}"><a href="javascript:parent.viewUserPop('${apOngdSendDVo.hdlrUid
			}');"><u:out value="${apOngdSendDVo.hdlrNm}" /></a></c:if><c:if
		test="${empty apOngdSendDVo.hdlrUid}"><u:out value="${apOngdSendDVo.hdlrNm}" /></c:if></td>
	<td class="body_ct"><u:out value="${apOngdSendDVo.hdlDt}" /></td>
	<td class="body_ct"><u:out value="${apOngdSendDVo.prevApvrApvDt}" /></td>
	</tr></c:forEach>
</u:listArea>

</form>

<u:buttonArea><c:if
		test="${empty storage and (param.bxId eq 'myBx' or param.bxId eq 'regRecLst' or param.bxId eq 'distRecLst')}">
	<u:button titleId="ap.btn.retrvDoc" href="javascript:retrvDoc();" alt="회수" auth="${param.bxId eq 'myBx' ? 'W' : 'A'}" />
	<u:button titleId="ap.btn.reSendDoc" href="javascript:reSendDoc();" alt="재발송" auth="${param.bxId eq 'myBx' ? 'W' : 'A'}" /><c:if
			test="${param.bxId ne 'distRecLst'}">
	<u:button titleId="ap.btn.manlSendCmpl" href="javascript:setManlSendCmpl();" alt="수동완료처리" auth="${param.bxId=='myBx' ? 'W' : 'A'}" /></c:if></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>