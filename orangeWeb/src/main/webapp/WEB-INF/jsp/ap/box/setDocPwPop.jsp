<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 문서 비밀번호 확인 후 전송 %>
function confirmDocPw(){
	$form = $("#confirmPwForm");
	if(!validator.validate($form[0])){
		return;
	}
	var key = null, secuId = null;
	callAjax("${_cxPth}/cm/login/createSecuSessionAjx.do", null, function(data){
		key = new RSAPublicKey(data.e, data.m);
	});
	if(key!=null){
		var apvNo = "${param.apvNo}";
		var plain = apvNo=='' ? {apvPw:$form.find("#pw").val()} : {apvNo:apvNo,docPw:$form.find("#pw").val()};
		var data = encrypt(key, JSON.stringify(plain));<%
		// 문서 비밀번호 확인하고 보안ID 발급 %>
		callAjax("${empty dmUriBase ? '.' : _cxPth.concat(dmUriBase)}/getSecuIdAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", {secu:data}, function(data){
			secuId = data['secuId'];
		});
	}
	if(secuId != null) ${empty param.callback ? 'openSecuDoc' : param.callback}(secuId);
}<%
// 팝업용 비밀번호 확인 - 팝업 열기 %>
function openSecuDocPop(secuId){
	var url = "./viewDocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}${not empty param.vwMode ? '&vwMode='.concat(param.vwMode) : ''}${not empty param.refdBy ? '&refdBy='.concat(param.refdBy) : ''}";
	url += "&secuId="+secuId;
	dialog.open2('viewDocDialog','<u:msg titleId="ap.jsp.viewDoc" alt="문서 조회" />', url, {padding:{right:"3px",top:"5px"}});
	dialog.close('setDocPwDialog');
}<%
// 팝업용 비밀번호 확인 - 열린 팝업내의 프레임 고치기 %>
function openSecuDocFrm(secuId){
	var base = "${not empty dmUriBase ? './viewApDocFrm.do' : './viewDocFrm.do'}";
	var url = base + "?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}${not empty param.vwMode ? '&vwMode='.concat(param.vwMode) : ''}${not empty param.refdBy ? '&refdBy='.concat(param.refdBy) : ''}";
	url += "&secuId="+secuId;
	getIframeContent('viewDocFrm').location.replace(url);
	dialog.close('setDocPwDialog');
}<%
// 팝업용 비밀번호 확인 - 열린 팝업내의 프레임 고치기 %>
function openSecuIntgDoc(secuId){
	var url = "./viewIntgDoc.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}${not empty param.vwMode ? '&vwMode='.concat(param.vwMode) : ''}${not empty param.refdBy ? '&refdBy='.concat(param.refdBy) : ''}";
	url += "&secuId="+secuId;
	location.replace(url);
}<%
// 포틀릿에서 호출 할 경우 %>
function openSecuPltDoc(secuId){
	getIframeContent("PLT_${param.pltId}").openSecuDoc(secuId);
}<%
// 팝업용 비밀번호 확인 - 종이문서 %>
function openSecuPaperDoc(secuId){
	var mode = '${param.mode}';
	if(mode=='' || mode=='view'){<%// 조회 %>
		dialog.open("setPaperDocDialog", '<u:msg alt="종이문서 조회" titleId="ap.jsp.viewPaper" />', "./viewPaperDocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&apvNo=${param.apvNo}");
	} else if(mode=='mod'){<%// 수정 %>
		dialog.open("setPaperDocDialog", '<u:msg alt="종이문서 등록" titleId="ap.btn.regPaper" />', "./setPaperDocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=set&apvNo=${param.apvNo}&secuId="+secuId);
	} else if(mode=='spReg'){<%// 분리등록 %>
		dialog.open("setPaperDocDialog", '<u:msg alt="종이문서 분리등록" titleId="ap.btn.regSpPaper" />', "./setPaperDocPop.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&mode=spReg&apvNo=${param.apvNo}&secuId="+secuId);
	}
	dialog.close('setDocPwDialog');
}
$(document).ready(function() {
	window.setTimeout("$('#confirmPwForm #pw').focus()", 20);
});
//-->
</script>
<div style="width:450px">
<form id="confirmPwForm" onsubmit="return doNotSubmit(event)">
<u:listArea colgroup="35%,65%">

	<tr>
		<td class="head_lt"><u:msg titleId="cols.pw" alt="비밀번호" /></td>
		<td><u:input id="pw" value="" titleId="cols.pw" maxLength="40" type="password" onkeydown="doNotSubmit(event, confirmDocPw)" mandatory="Y" /></td>
	</tr>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:confirmDocPw();" alt="확인" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>
</form>
</div>