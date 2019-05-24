<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // [목록:조회수] 조회이력 %>
function readHst(id) {
	dialog.open('listReadHstPop','<u:msg titleId="bb.jsp.listReadHstPop.title" alt="조회자 정보" />','./listReadHstPop.do?menuId=${menuId}&ctId=${ctId}&bullId=' + id);
}

<% // 점수주기 %>
function sendTo() {
	dialog.open('sendToPop','<u:msg titleId="cm.btn.send" alt="보내기" />','../sendToPop.do?menuId=${menuId}&ctId=${ctId}&bullId=${param.bullId}');
}

$(document).ready(function() {
	setUniformCSS();
});

<% // 점수주기 %>
function saveScre() {
	if (${screHstExist}) {
		alertMsg("ct.msg.scre.already");<% // bb.msg.scre.already=이미 점수를 준 게시물입니다. %>
		return;
	}
	var $checked = $('input[type=radio][name=scre]:checked');
	if ($checked.length == 0) {
		alertMsg("ct.msg.scre.notChecked");<% // bb.msg.scre.notChecked=점수를 선택하세요. %>
		return;
	}
	callAjax('./transBullScreAjx.do?menuId=${menuId}', {bullId:'${param.bullId}', scre:$checked.val()}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
	});
}

<% // 점수내역 %>
function viewScre() {
	dialog.open('viewScrePop','<u:msg titleId="ct.jsp.viewScrePop.title" alt="점수내역" />','./viewScrePop.do?menuId=${menuId}&ctId=${ctId}&bullId=${param.bullId}');
}

<% // 추천 %>
function recmdBull() {
	if (confirmMsg("ct.cfrm.recmd")) {	<% // ct.cfrm.recmd=추천하시겠습니까? %>
		callAjax('./transBullRecmdAjx.do?menuId=${menuId}', {bullId:'${param.bullId}'}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				$('#recmdCntTd').html(data.recmdCnt);
			}
		});
	}
}

function delBull(){
	if (confirmMsg("cm.cfrm.del")) {
		callAjax('./transBullDel.do?menuId=${menuId}&ctId=${ctId}', {bullId : '${ctBullMastBVo.bullId}'}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listBoard.do?menuId=${menuId}&ctId=${ctId}';
			}
		});
	}
}

function sendEmail(bullId,regrUid) {
	var $bullCont = $("#bullCont").val();
	var recvIds = [];
	
	recvIds.push(regrUid);
	
	if(recvIds == ''){
		emailSendPop({ctId:'${ctId}',bullId:bullId}, '${menuId }');
	}else{
		emailSendPop({ctId:'${ctId}',bullId:bullId,recvIds:recvIds}, '${menuId }');
	}
	
	
	/*
	
		
		callAjax('./transEmailAjx.do?menuId=${menuId}&ctId=${ctId}', {bullCont:$bullCont}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				openWindow('/cm/zmailPop.do?emailId='+data.emailId,'sendEmailPop',900,700,'resizable=yes,status=yes,toolbar=no,menubar=no,scrollbars=yes');	
			}
		});
		
	}else{
		callAjax('./sendMailBull.do?menuId=${menuId}&ctId=${ctId}', {recvId:recvIds, bullCont:$bullCont}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				openWindow('/cm/zmailPop.do?emailId='+data.emailId,'sendEmailPop',900,700,'resizable=yes,status=yes,toolbar=no,menubar=no,scrollbars=yes');	
			}
		});
	}
	//dialog.open('sendEmailPop','이메일 발송','../sendEmailPop.do?menuId=${menuId}');
	*/
}

$(document).ready(function() {
	var bodyHtml = $("#lobHandlerArea").html();
	if(bodyHtml!=''){
		$('#bullCont').val(bodyHtml);
	}
	setUniformCSS();
});
//-->
</script>

<c:set var="subj" value="Java 8 개요" />
<c:set var="regr" value="이채린" />
<c:set var="regDt" value="2014-01-20" />
<c:set var="readCnt" value="1" />
<c:set var="cont" value="Java 8에서 새로운 점들을 정리해 보았습니다. 도움되시길 바랍니다. 감사합니다." />

<u:title titleId="ct.jsp.viewBoard.title" alt="게시판 조회" menuNameFirst="true"/>

<form id="viewBoardForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<div id="listArea" class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="18%"/><col width="32%"/><col width="18%"/><col width="32%"/></colgroup>
			<tr>
			<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
			<td colspan="3" class="body_lt">${ctBullMastBVo.subj}</td>
			</tr>
			
			<tr>
			<td width="18%" class="head_lt"><u:msg titleId="cols.regr" alt="등록자" /></td>
			<td width="32%" class="body_lt">
				<a href="javascript:sendEmail('${ctBullMastBVo.bullId}' ,'${ctBullMastBVo.regrUid}');">${ctBullMastBVo.regrNm}</a>
			</td>
			<td width="18%" class="head_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
			<td width="32%" class="body_lt">
				<fmt:parseDate var="dateTempParse" value="${ctBullMastBVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</td>
			</tr>
			<tr>
			<td width="18%" class="head_lt">
				<u:msg titleId="cols.readCnt" alt="조회수" />
			</td>
			<td width="32%" class="body_lt">
				<a href="javascript:readHst('${ctBullMastBVo.bullId}');">${ctBullMastBVo.readCnt}</a>
			</td>
			<td width="18%" class="head_lt"><u:msg titleId="ct.cols.exprDt" alt="만료일시" /></td>
			<td width="32%" class="body_lt">
				<fmt:parseDate var="dateTempParse" value="${ctBullMastBVo.bullExprDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</td>
			</tr>
			<tr>
			<td colspan="4" class="body_lt"><div id="lobHandlerArea" style="overflow:auto;" class="editor"><u:clob lobHandler="${lobHandler }"/></div>
				<input id="bullCont" name="bullCont" type="hidden" value="${ctBullMastBVo.cont}"/>
			</td>
			</tr>
			
			<tr>
				<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
				<td colspan="3">
					<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="ct" mode="view" /></c:if>
				</td>
			</tr>
			<tr>
				<td class="head_lt"><u:msg titleId="cols.recmdCnt" alt="추천수" /></td>
				<td colspan="3"><table border="0" cellpadding="0" cellspacing="0"><tbody>
					<tr>
					<td id="recmdCntTd" class="body_lt">${ctBullMastBVo.recmdCnt}</td>
					<c:if test="${recmdHstExist == false}">
					<td><u:buttonS titleId="bb.btn.recmd" alt="추천" onclick="javascript:recmdBull();" /></td>
					</c:if>
					</tr>
				</tbody></table></td>
			</tr>
		</table></div>
<% // 점수주기 %>
<u:boxArea className="gbox" outerStyle="height:47px;overflow:hidden;" innerStyle="NO_INNER_IDV">
	<table border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td colspan="2" class="body_lt">※ <u:msg titleId="bb.cols.saveScre" alt="점수주기" /> - <u:msg titleId="bb.jsp.viewBull.saveScre" alt="이 글에 대한 점수를 남겨 주세요." /></td>
	</tr>
	
	<tr>
	<td style="padding: 0 0 0 15px;"><u:checkArea>
		<u:radio name="scre" value="1" title="★☆☆☆☆" inputClass="bodybg_lt" textStyle="color: #777;" />
		<u:radio name="scre" value="2" title="★★☆☆☆" inputClass="bodybg_lt" textStyle="color: #777;" />
		<u:radio name="scre" value="3" title="★★★☆☆" inputClass="bodybg_lt" textStyle="color: #777;" />
		<u:radio name="scre" value="4" title="★★★★☆" inputClass="bodybg_lt" textStyle="color: #777;" />
		<u:radio name="scre" value="5" title="★★★★★" inputClass="bodybg_lt" textStyle="color: #777;" />
		</u:checkArea></td>
	<td><u:buttonS titleId="bb.btn.saveScre" alt="점수주기" onclick="saveScre();" /></td>
	<td><u:buttonS titleId="bb.btn.viewScre" alt="점수내역" onclick="viewScre();" /></td>
	</tr>
	</tbody></table>
</u:boxArea>

<% // 한줄답변 %>
<div style="height: 25px;"><a href="javascript:" onclick="$('#listCmtFrm').toggle();"><u:msg titleId="cols.cmt" alt="한줄답변" /> (${ctBullMastBVo.cmtCnt})</a></div>
<iframe id="listCmtFrm" name="listCmtFrm" src="./listCmtFrm.do?menuId=${menuId}&ctId=${ctId}&bullId=${ctBullMastBVo.bullId}" style="width:100%;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<c:if test="${fn:length(replyBullList) > 1}">
<% // 관련글 %>
<div class="titlearea">
	<div class="tit_left">
	<dl>
	<dd class="txt">※ <span class="red_txt">${fn:length(replyBullList)}</span> <u:msg titleId="bb.jsp.viewBull.tx01" alt="개의 관련글이 있습니다" />
		<u:icoCurr /> <u:msg titleId="bb.jsp.viewBull.tx02" alt="표시의 글은 현재글입니다." /></dd>
	</dl>
	</div>
</div>

<% // 관련글 목록 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
		<tr>
		<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
		<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
		<td width="12%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		</tr>

	<c:if test="${fn:length(replyBullList) == 0}">
		<tr>
		<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(replyBullList) > 0}">
		<c:forEach items="${replyBullList}" var="bullVo" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="listicon_ct"><u:icoCurr display="${ctBullMastBVo.bullId == bullVo.bullId}" /></td>
		<td class="body_lt">
			<div class="ellipsis" title="<u:out value="${bullVo.subj}"/>">
				<u:icon type="indent" display="${bullVo.replyDpth > 0}" repeat="${bullVo.replyDpth - 1}" />
				<u:icon type="reply" display="${bullVo.replyDpth > 0}" />
				<u:icon type="new" display="${bullVo.newYn == 'Y'}" />
				<a href="./viewBoard.do?menuId=${menuId}&ctId=${ctId}&bullId=${bullVo.bullId}" title="${bullVo.subj}"><u:out value="${bullVo.subj}"/></a>
				<span style="font-size: 10px;">(<u:out value="${bullVo.cmtCnt}" type="number" />)</span></a>
			</div>
		</td>
		<td class="body_ct"><a href="javascript:viewUserPop('${bullVo.regrUid}');">${bullVo.regrNm}</a></td>
		<td class="body_ct"><u:out value="${bullVo.regDt}" type="longdate" /></td>
		</tr>
		</c:forEach>
	</c:if>
	</table>
</div>
<u:blank/>
</c:if>

<% // 이전글 다음글 %>
<u:listArea colgroup="6%,,10%,12%">
<c:if test="${prevBullVo == null}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="listicon_ct"><u:icon type="prev" /></td>
	<td class="body_lt"><u:msg titleId="bb.jsp.viewBull.prevNotExists" alt="이전글이 존재하지 않습니다." /></td>
	<td class="body_ct">&nbsp;</td>
	<td class="body_ct">&nbsp;</td>
	</tr>
</c:if>
<c:if test="${prevBullVo != null}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="listicon_ct"><a href="./viewBoard.do?menuId=${menuId}&ctId=${ctId}&bullId=${prevBullVo.bullId}"><u:icon type="prev" /></a></td>
	<td class="body_lt"><a href="./viewBoard.do?menuId=${menuId}&ctId=${ctId}&bullId=${prevBullVo.bullId}" title="${prevBullVo.subj}"><u:out value="${prevBullVo.subj}" maxLength="80" /></a>
		(<u:out value="${prevBullVo.cmtCnt}" type="number" />)</span>
		</td>
	<td class="body_ct"><a href="javascript:viewUserPop('${prevBullVo.regrUid}');">${prevBullVo.regrNm}</a></td>
	<td class="body_ct">
		<fmt:parseDate var="dateTempParse" value="${prevBullVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
	</td>
	</tr>
</c:if>

<c:if test="${nextBullVo == null}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="listicon_ct"><u:icon type="next" /></td>
	<td class="body_lt"><u:msg titleId="bb.jsp.viewBull.nextNotExists" alt="다음글이 존재하지 않습니다." /></td>
	<td class="body_ct">&nbsp;</td>
	<td class="body_ct">&nbsp;</td>
	</tr>
</c:if>
<c:if test="${nextBullVo != null}">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="listicon_ct"><a href="./viewBoard.do?menuId=${menuId}&ctId=${ctId}&bullId=${nextBullVo.bullId}"><u:icon type="next" /></a></td>
	<td class="body_lt"><a href="./viewBoard.do?menuId=${menuId}&ctId=${ctId}&bullId=${nextBullVo.bullId}" title="${nextBullVo.subj}"><u:out value="${nextBullVo.subj}" maxLength="80" /></a>
		<span style="font-size: 10px;">(<u:out value="${nextBullVo.cmtCnt}" type="number" />)</span>
		</td>
	<td class="body_ct"><a href="javascript:viewUserPop('${nextBullVo.regrUid}');">${nextBullVo.regrNm}</a></td>
	<td class="body_ct">
		<fmt:parseDate var="dateTempParse" value="${nextBullVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
	</td>
	</tr>
</c:if>
</u:listArea>


<% // 하단 버튼 %>
<u:buttonArea>

	<c:choose>
		<c:when test="${!empty myAuth && myAuth == 'M' }">
			<u:button titleId="cm.btn.send" alt="보내기" href="javascript:sendTo();" />
			<c:if test="${mailEnable == 'Y' }">
				<u:button titleId="cm.btn.emailSend" alt="이메일발송" href="javascript:sendEmail('${ctBullMastBVo.bullId}');" />
			</c:if>
			<u:button titleId="bb.btn.reply" alt="답변" href="./setReply.do?menuId=${menuId}&ctId=${ctId}&bullPid=${ctBullMastBVo.bullId}" />
			<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" />
			<u:button titleId="cm.btn.mod" alt="수정" href="./setBoard.do?menuId=${menuId}&ctId=${ctId}&bullId=${ctBullMastBVo.bullId}&bullPid=${ctBullMastBVo.bullPid}" />
			<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?" var="msg" />
			<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delBull();" />
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${!empty authChkD && authChkD == 'D' }">
					<u:button titleId="cm.btn.send" alt="보내기" href="javascript:sendTo();" />
					<c:if test="${mailEnable == 'Y' }">
						<u:button titleId="cm.btn.emailSend" alt="이메일발송" href="javascript:sendEmail('${ctBullMastBVo.bullId}');" />
					</c:if>
					<u:button titleId="bb.btn.reply" alt="답변" href="./setReply.do?menuId=${menuId}&ctId=${ctId}&bullPid=${ctBullMastBVo.bullId}"/>
					<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" />
					<c:if test="${ctBullMastBVo.regrUid != logUserUid}">
						<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delBull();" />
					</c:if>
				</c:when>
				<c:otherwise>
					<c:if test="${!empty authChkR && authChkR == 'R' }">
						<c:if test="${mailEnable == 'Y' }">
							<u:button titleId="cm.btn.emailSend" alt="이메일발송" href="javascript:sendEmail('${ctBullMastBVo.bullId}');"/>
						</c:if>
						<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" />
					</c:if>
					<c:if test="${!empty authChkW && authChkW == 'W' }">
						<u:button titleId="cm.btn.send" alt="보내기" href="javascript:sendTo();" />
						<u:button titleId="bb.btn.reply" alt="답변" href="./setReply.do?menuId=${menuId}&ctId=${ctId}&bullPid=${ctBullMastBVo.bullId}" />
					</c:if>
				</c:otherwise>
			</c:choose>
			<c:if test="${ctBullMastBVo.regrUid == logUserUid}">
				<u:button titleId="cm.btn.mod" alt="수정" href="./setBoard.do?menuId=${menuId}&ctId=${ctId}&bullId=${ctBullMastBVo.bullId}&bullPid=${ctBullMastBVo.bullPid}" />
				<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delBull();" />
			</c:if>
		
		</c:otherwise>
	
	</c:choose>
	
	<u:button titleId="cm.btn.list" alt="목록" href="./listBoard.do?menuId=${menuId}&ctId=${ctId}" />

<%-- 	./listNotc.do?menuId=${menuId} --%>
</u:buttonArea>

</form>
