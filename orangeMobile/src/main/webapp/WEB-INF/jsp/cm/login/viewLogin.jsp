<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
function processLogin(event){
	if(event!=null && event.keyCode!=13) return;
	
	if(!$m.secu.supported){
		$m.dialog.alert('<u:msg titleId="mpt.not.supp.browser" type="script" />');
		return;
	}
	if(!isLocalStorageNameSupported()){
		$m.dialog.alert('<u:msg titleId="mpt.msg.notSupportPrivateMode" type="script" />');
		return;
	}
	
	var param = new ParamMap().getData('loginFrm');
	if(param.get("lginId")==""){
		$m.msg.alertMsg('cm.input.check.require.users',['#cols.lginId'], param.get("langTypCd"));
		$("#lginId").focus();
		return;
	}
	if(param.get("pw")==""){
		$m.msg.alertMsg('cm.input.check.require.users',['#cols.pw'], param.get("langTypCd"));
		$("#pw").focus();
		return;
	}
	$('#pw').blur();
	$('#loginBtn').focus();
	
	param.put("time", ""+new Date().getTime());
	if(typeof(window.localStorage) != 'undefined'){
		var token = window.localStorage.getItem("uagntToken");
		if(token!=null && token!=""){
			param.put("token", token);
		}
	}
	$m.ajax("/cm/login/createSecuSessionAjx.do?secuSessionCode=${secuSessionCode}", null, 
		function(data){
			var key = $m.rsa.getKey(data.e, data.m);
			var data = $m.rsa.encrypt(key, JSON.stringify(param.toJSON()));
			setLginId(param);<%
			// 푸쉬 메세지 ID - 로그인 후 이동 페이지 찾기 위해 필요 %>
			$m.nav.curr(event, '/cm/login/processLogin.do?secu='+data+"${empty param.msgId ? '' : '&msgId='.concat(param.msgId)}");
		},
		{
			secuSession:true,
			error:function(xhr, ajaxOptions, thrownError) {
				var errCd = xhr.status;
				if(errCd=='400') errCd = '403';
				if(errCd=='0'){
					$m.dialog.alert('<u:msg titleId="mcm.conn.timeout" />', function(){
						location.replace(location.href);
					});
				} else if(errCd=='403'){
					$m.dialog.alert('<u:msg titleId="pt.login.renewConn" />', function(){
						location.replace(location.href);
					});
				} else {
					var msgCd = (errCd=='403' || errCd=='404') ? 'cm.msg.errors.'+errCd : 'cm.msg.errors.500';
					if(errCd=='403' || errCd=='404' || (param!=null && param.msgId!="cm.msg.errors.noMessage")){
						$m.msg.alertMsg(msgCd, [errCd, url]);
					}
				}
			}
		});
}
function setLginId(param){
	var storage = window.localStorage;
	if(storage != null){
		if(param.get('saveId')=='Y'){
			storage['lginId'] = param.get('lginId');
		} else {
			storage['lginId'] = '';
		}
		storage['langTypCd'] = param.get('langTypCd');
	}
}
var holdHide = false;
$(document).ready(function() {
	var inputId = $("#lginId");
	var inputPw = $("#pw");
	var storage = window.localStorage;
	if(storage != null){
		if(storage.lginId != null && storage.lginId != ''){
			inputId.val(storage.lginId);
			inputId.css('opacity', '1');
			$ui.apply(document.getElementById('saveId'), true);
		}
		if(storage.langTypCd!=null){
			var langListArea = $('#langListArea');
			var langNm = langListArea.find("[data-code='"+storage.langTypCd+"']").text();
			if(langNm!=null){
				setLangTypCd(storage.langTypCd, langNm);
			}
		}
	}
	
	var msg = '<u:out value="${message}" type="script" />';
	if(msg!='') alert('msg');
	
	var cfmMsg = '<u:out value="${confirmMessage}" type="script" />';
	if(cfmMsg!='' && !confirm(cfmMsg)){
		top.location.href = '/';
	}
	
	inputId.focus(function(){
		$(this).css('opacity', '1');
		$space.on(this, 50);
	}).blur(function(){
		if($(this).val() == ''){
			$(this).css('opacity', '0.1');
		}
		$space.off();
	});
	inputPw.focus(function(){
		$(this).css('opacity', '1');
		$space.on(this, 50);
	}).blur(function(){
		if($(this).val() == ''){
			$(this).css('opacity', '0.1');
		}
		$space.off();
	});
	
	if(!$m.browser.mobile){
		if(inputId.val()=='') $("#lginId").focus();
		else inputPw.focus();
	}
	
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $("#langArea div:first").hide();
	});
	
	if(!isLocalStorageNameSupported()){
		$m.dialog.alert('<u:msg titleId="mpt.msg.notSupportPrivateMode" type="script" />');
		return;
	}
	
});<%
// 언어코드 선택 %>
function setLangTypCd(code, value){
	var langArea = $("#langArea");
	langArea.find("input[name='langTypCd']").val(code);
	langArea.children('.select').find("dd.txt:first span").text(value);
	langArea.children(".select_open:first").hide();
}<%
// 로컬스토리지 지원 여부 - Private 모드 %>
function isLocalStorageNameSupported() {
	var testKey = 'test', storage = window.localStorage;
	try {
		storage.setItem(testKey, '1');
		storage.removeItem(testKey);
		return true;
	} catch (error) {
		return false;
	}
}
//-->
</script>

	<div class="header2">
	<div class="logo" style="background:url('${mobLginMap.logoImgPath}') no-repeat; background-size:contain; background-position: center left;"></div>
	<div class="tit" style="color:${mobLginMap.lginTitleColor};"><u:out value="${mobLginMap.lginTitle}" /></div>
	</div>
	
	<form id="loginFrm">
	<div class="inputarea2">
	<dl>
	<dd>
		<div class="input_pw"><u:msg titleId="mpt.label.id" alt="아이디" /></div>
		<div class="input_pw2"><input id="lginId" name="lginId" type="text" value="" class="input_lt" style="ime-mode:inactive;" maxlength="30" /></div></dd>
	<dd>
		<div class="input_pw"><u:msg titleId="mpt.label.pw" alt="비밀번호" /></div>
		<div class="input_pw2"><input id="pw" name="pw" type="password" value="" class="input_lt" onkeydown="processLogin(event)" style="ime-mode:inactive;" maxlength="30" /></div></dd>
	</dl>
	</div>
	
	<div class="langarea">
		<div class="checklt">
		<div>
		<dl>
			<m:check id="saveId" value="Y" titleId="mpt.label.saveId" titleClass="txt" alt="아이디 저장" />
		</dl>
		</div>
		</div>
		
		<div class="selectrt" id="langArea"><c:if
	
			test="${fn:length(langTypPtCdBVoList)>1}">
		<div class="select_open" style="display:none">
		<div class="open">
		<dl id="langListArea"><c:forEach
			items="${langTypPtCdBVoList}" var="langTypCdVo" varStatus="status"><c:if
				test="${not status.first}">
			<dd class="line"></dd></c:if>
			<dd class="txt" onclick="setLangTypCd($(this).attr('data-code'),$(this).text());" data-code="${langTypCdVo.cd}"><u:out value="${langTypCdVo.refVa1}" /></dd></c:forEach>
		</dl>
		<input type="hidden" name="langTypCd" value="${langTypPtCdBVoList[0].cd}" />
		</div>
		</div>
		
		<div class="select" onclick="holdHide=true; $('#langArea div:first').toggle();">
		<dl>
			<dd class="txt"><span><u:out value="${langTypPtCdBVoList[0].refVa1}" /></span></dd>
			<dd class="btn"></dd>
		</dl>
		</div></c:if><c:if
	
			test="${fn:length(langTypPtCdBVoList)<=1}">
			<input type="hidden" name="langTypCd" value="${langTypPtCdBVoList[0].cd}" /></c:if>
		</div>
	</div>
	</form>
	
	<div class="btnarea">
	<div class="btn" id="loginBtn" onclick="processLogin(null)"><u:msg titleId="pt.login.title" alt="로그인" /></div>
	</div>
	
	<div id="scrollSpace" style="height:0px;"></div>
	<div style="height:60px;"></div>
	<div class="footer" style="position:fixed">
	<div class="copyright">copyright(C)2015 <strong>INNOBIZ</strong>. all rights reserved.</div>
	</div>