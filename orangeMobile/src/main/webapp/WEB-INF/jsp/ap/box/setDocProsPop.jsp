<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:secu
	auth="W"><u:set test="${true}" var="writeAuth" value="Y"
/></u:secu>
<script type="text/javascript">
//<![CDATA[
var gOptConfig = ${optConfigJson};
var gStampMsg = null;
function submitPros(apvStatCd){
	if(gStampMsg!=null){<%// [도장/서명] 이미지 설정 검사 %>
		$m.dialog.alert(gStampMsg);
		return;
	}
	var param = new ParamMap().getData($("#setProsForm")[0]);
	if(gOptConfig.notAlwApvPw!='Y'){
		if(param.get("pw").trim()==''){<%
			// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [비밀번호] %>
			$m.msg.alertMsg("cm.input.check.mandatory", ["#ap.jsp.doc.pw"], function(){
				$("#setProsForm input[name='pw']").focus();
			});
			return;
		}
	}
	if(apvStatCd=='reRevw' || apvStatCd=='rejt' || apvStatCd=='cons'){<%// reRevw:재검토, rejt:반려, cons:반대 %>
		if(param.get("apvOpinCont").trim()==''){<%
			// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [결재의견] %>
			$m.msg.alertMsg("cm.input.check.mandatory", ["#ap.jsp.doc.apvOpin"], function(){
				$("#setProsForm [name='apvOpinCont']").focus();
			});
			return;
		}
	}
	var win = $m.nav.getWin();
	if(win==null) return;
	
	var $area = win.$docDataArea.find('#docOpinArea'), input;
	param.each(function(key, va){
		if(key!='pw'){
			input = $area.find("[name='"+key+"']");
			if(input.length!=0){
				input.val(va);
			} else {
				$area.append("<input type='hidden' name='"+key+"' value='"+escapeValue(va)+"' />");
			}
		}
	});<%
	// [옵션]결재 비밀번호 사용 안함 - N 일 경우
	//     - 암호화 세션 만들고 비빌번호 확인후 확인키(secuId) 받아서 문서에 세팅함
	%>
	if(gOptConfig.notAlwApvPw!='Y'){
		var key = null, secuId = null;
		$m.ajax("${_cxPth}/cm/login/createSecuSessionAjx.do", null, function(data){
			key = $m.rsa.getKey(data.e, data.m);
		});
		if(key != null){
			var data = $m.rsa.encrypt(key, JSON.stringify({apvPw:param.get('pw')}));
			$m.ajax("${_cxPth}/ap/box/getSecuIdAjx.do?menuId=${menuId}&bxId=${param.bxId}", {secu:data}, function(data){
				secuId = data['secuId'];
			});
		} else {
			return false;
		}
		
		if(secuId!=null){<%
			// secuId 를 서밋할 곳에 세팅함 %>
			win.$docDataArea.find('#secuId').val(secuId);<%
			// 옵션에 따른 상신 메세지 체크 %>
			prepareSubmit(apvStatCd, win);
		}
	} else {
		prepareSubmit(apvStatCd, win);
	}
}<%
// 전송 준비 - 승인, 상신 메세지 처리 %>
function prepareSubmit(apvStatCd, win){
	if(win==null) win = $m.nav.getWin();
	win.checkSubmitMsg(apvStatCd, function(){
		win.submitDoc(apvStatCd);
	});
}
$(document).ready(function() {
	var data = $m.nav.getWin().getOpinHiddenData();
	var $form = $("#setProsForm");
	if(data['apvOpinCont']!=null) $form.find("textarea[name='apvOpinCont']:first").val(data['apvOpinCont']);
	if(data['apvOpinDispYn']=='Y'){
		$ui.apply($form.find("[name='apvOpinDispYn']:first")[0], true);
	}<%
	
	// 반려가 아니고(승인,찬성,반대), 텍스트서명(결재자명)이 아닐때 - 도장 or 서명이 없으면
	//    - 메세지 출력 및 진행 금지
	// ap.msg.noImgNoApv={0} 이미지를 설정하지 않아서 진행 할 수 없습니다. - [도장 or 서명]
	%>
	<c:if test="${hasNoStamp}"><c:if
		test="${signMthdCd=='01'}">gStampMsg = '<u:msg titleId="ap.msg.noImgNoApv" arguments="#or.txt.stamp"/>';</c:if><c:if
		test="${signMthdCd=='02'}">gStampMsg = '<u:msg titleId="ap.msg.noImgNoApv" arguments="#or.txt.sign"/>';</c:if>
	</c:if>
	if(gStampMsg!=null) $m.dialog.alert(gStampMsg);
});
//]]>
</script>

<form id="setProsForm">
<div class="blankzone"><div class="blank15"></div></div>

<div class="pop_entryzone" >
<div class="entryarea">
	<dl><c:if test="${optConfigMap.notAlwApvPw != 'Y'}">
		<dd class="etr_tit"><u:msg titleId="ap.trans.apvPw" alt="결재비밀번호" /></dd>
		<dd class="etr_input"><div class="etr_inputin"><input type="password" name="pw" class="etr_iplt"/></div></dd>
		</c:if>
		
		<dd class="etr_blank"></dd>
		<dd class="etr_tit"><u:msg titleId="ap.jsp.doc.apvOpin" alt="결재의견" /></dd>
		<dd class="etr_input"><div class="etr_textareain"><textarea name="apvOpinCont" rows="4" class="etr_ta"></textarea></div></dd>

		<c:if test="${param.opinInDoc == 'Y' && param.apvStatCd != 'reRevw'}" 
		><dd class="etr_blank"></dd>
		<dd class="etr_tit"><u:msg titleId="ap.jsp.doc.opinDisp" alt="의견표시" /></dd>
		<dd class="etr_blank"></dd>
		<m:checkArea>
			<m:check id="apvOpinDispYn" value="Y" titleId="ap.jsp.doc.opinDispDetl" />
		</m:checkArea>
		</c:if>
	</dl>
</div>
</div>

<div class="popbtnarea">
<div class="btnarea">
	<div class="size">
	<dl><u:secu auth="W"><c:if
		test="${param.apvStatCd == 'mak' and not hasNoStamp}">
		<dd class="btn" onclick="submitPros('${param.apvStatCd}')"><u:msg titleId="ap.btn.subm" alt="상신" /></dd></c:if><c:if
		test="${param.apvStatCd == 'apvd' and not hasNoStamp}">
		<dd class="btn" onclick="submitPros('${param.apvStatCd}')"><u:term termId="ap.term.apvd" alt="승인" /></dd></c:if><c:if
		test="${param.apvStatCd == 'rejt'}">
		<dd class="btn" onclick="submitPros('${param.apvStatCd}')"><u:term termId="ap.term.rejt" alt="반려" /></dd></c:if><c:if
		test="${param.apvStatCd == 'pros' and not hasNoStamp}">
		<dd class="btn" onclick="submitPros('${param.apvStatCd}')"><u:term termId="ap.term.pros" alt="합의승인" /></dd></c:if><c:if
		test="${param.apvStatCd == 'cons' and not hasNoStamp}">
		<dd class="btn" onclick="submitPros('${param.apvStatCd}')"><u:term termId="ap.term.cons" alt="합의반대" /></dd></c:if><c:if
		test="${param.apvStatCd == 'reRevw'}">
		<dd class="btn" onclick="submitPros('${param.apvStatCd}')"><u:term termId="ap.term.reRevw" alt="재검토" /></dd></c:if><c:if
		test="${param.apvStatCd == 'makVw'}">
		<dd class="btn" onclick="submitPros('${param.apvStatCd}')"><u:msg titleId="ap.btn.subm" alt="상신" /></dd></c:if><c:if
		test="${param.apvStatCd == 'fstVw'}">
		<dd class="btn" onclick="submitPros('${param.apvStatCd}')"><u:term termId="ap.term.fstVw" alt="선람" /></dd></c:if><c:if
		test="${param.apvStatCd == 'pubVw'}">
		<dd class="btn" onclick="submitPros('${param.apvStatCd}')"><u:term termId="ap.term.pubVw" alt="공람" /></dd></c:if><c:if
		test="${hasNoStamp}"><u:authUrl url="/pt/psn/setEnv.do" var="regStampUrl" />
		<dd class="btn" onclick="$m.nav.next(event, '${url}')"><u:msg titleId="mpt.btn.setEnv" alt="환경설정" /></dd></c:if>
		</u:secu>
		<dd class="btn" onclick="$m.dialog.close('apvStatPop')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
	</dl>
	</div>
</div>
</div>
</form>