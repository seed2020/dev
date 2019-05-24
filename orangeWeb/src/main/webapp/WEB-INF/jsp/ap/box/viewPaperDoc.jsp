<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<%// 문서 비밀번호 확인 후 전송 %>
function openSecuDoc(secuId){
	var $form = $("#toDocForm");
	$form.find("#secuId").val(secuId);
	$form.attr("action", "./setDoc.do").attr("method", "GET").submit();
}<%
// 목록 가기 %>
function toList(bxId){
	var qstr = $("#queryString").val();
	callAjax("./getBxUrlAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {bxId:(bxId==null || bxId=='' ? '${param.bxId}' : bxId)}, function(data){
		location.href = data.url+(qstr==null || qstr=='' ? '' : '&'+qstr);
	});
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
}
$(document).ready(function() {
	<c:if test="${noPw == 'Y'}">
	dialog.open("setDocPwDialog", '<u:msg titleId="ap.titl.docPwCfrm" alt="문서비밀번호 확인" />', "./setDocPwPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}&callback=openSecuPaperDoc&mode=${empty param.mode ? 'view' : param.mode}");
	alert("${message}");
	</c:if>
	<c:if test="${viewPaper == 'Y'}">
	var mode = '${param.mode}';
	if(mode=='' || mode=='view'){<%// 조회 %>
		dialog.open("setPaperDocDialog", '<u:msg alt="종이문서 조회" titleId="ap.jsp.viewPaper" />', "./viewPaperDocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}");
	} else if(mode=='mod'){<%// 수정 %>
		dialog.open("setPaperDocDialog", '<u:msg alt="종이문서 등록" titleId="ap.btn.regPaper" />', "./setPaperDocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=set&apvNo=${param.apvNo}");
	} else if(mode=='spReg'){<%// 분리등록 %>
		dialog.open("setPaperDocDialog", '<u:msg alt="종이문서 분리등록" titleId="ap.btn.regSpPaper" />', "./setPaperDocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=spReg&apvNo=${param.apvNo}");
	}
	</c:if>
});
//-->
</script>
<c:if test="${frmYn != 'Y'}"><u:title titleId="ap.jsp.viewDoc" alt="문서 조회" notPrint="true" /><c:if
		test="${frmYn != 'Y' and (not empty prevApOngdBVo or not empty nextApOngdBVo)}">
<div class="front notPrint">
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
	</div>
	<div class="front_right">
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td class="frontbtn"><u:buttonS titleId="cm.btn.cancel" alt="취소" href="javascript:toList();" /></td>
		</tr>
		</tbody>
		</table>
	</div>
</div>
<div class="apBtnLine notPrint"></div></c:if></c:if>

<form id="toDocForm" method="post">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" name="bxId" value="${param.bxId}" />
<u:input type="hidden" id="apvNo" value="${param.apvNo}" /><c:if
	test="${not empty param.apvLnPno}">
<u:input type="hidden" id="apvLnPno" value="${param.apvLnPno}" /></c:if><c:if
	test="${not empty param.apvLnNo}">
<u:input type="hidden" id="apvLnNo" value="${param.apvLnNo}" /></c:if><c:if
	test="${not empty param.queryString}">
<u:input type="hidden" id="queryString" value="${param.queryString}" /></c:if><c:if
	test="${not empty param.vwMode}">
<u:input type="hidden" id="vwMode" value="${param.vwMode}" /></c:if>
<u:input type="hidden" name="secuId" value="" />
</form><c:if


	test="${not empty prevApOngdBVo or not empty nextApOngdBVo}">
<form id="prevNextForm" action="./setDoc.do">
<u:input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" name="bxId" value="${param.bxId}" />
<u:input type="hidden" name="apvNo" value="" />
<u:input type="hidden" name="apvLnPno" value="" />
<u:input type="hidden" name="apvLnNo" value="" /><c:if
	test="${not empty vwMode}">
<u:input type="hidden" name="vwMode" value="${vwMode!='trx' ? 'trx' : 'orgn'}" /></c:if><c:if
	test="${not empty param.queryString}">
<u:input type="hidden" name="queryString" value="${param.queryString}" /></c:if><c:if
	test="${not empty param.pltQueryString}">
<u:input type="hidden" name="pltQueryString" value="${param.pltQueryString}" /></c:if><c:if
	test="${empty param.queryString and empty param.pltQueryString}">
<u:input type="hidden" name="queryString" value="${queryString}" /></c:if>
</form></c:if>