<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
	import="com.innobiz.orange.web.cm.config.ServerConfig"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	request.removeAttribute("demoSite");

%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko"><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
<title><c:if
	test="${empty UI_TITLE}" ><u:term termId="or.term.siteName" /> <u:msg titleId="pt.login.title" alt="로그인" /></c:if><c:if
	test="${not empty UI_TITLE}" >${UI_TITLE}</c:if></title>
<link rel="stylesheet" href="/css/uniform.default.blue.css" type="text/css" />
<link rel="stylesheet" href="/css/container.blue.css" type="text/css" />
<script type="text/javascript" src="${_cxPth}/js/jquery-1.10.2.js" charset="utf-8"></script>
<script type="text/javascript" src="${_cxPth}/js/common.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/commonEx.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/validator.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/jquery.uniform.min.js" charset="utf-8"></script>
<script type="text/javascript" src="${_cxPth}/js/rsa.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/json2.js" charset="UTF-8"></script>
<script type="text/javascript">
<!--
function processLogin(event){
	if(event!=null){
		if(event.keyCode!=13) return;
	}
	var param = new ParamMap().getData('loginFrm');
	if(param.get("lginId")==""){
		alertMsg('cm.input.check.require.users',['#cols.lginId'], param.get("langTypCd"));
		$("#loginArea #lginId").focus();
		return;
	} else {
		param.put("lginId", encodeURIComponent(param.get("lginId")));
	}
	if(param.get("pw")==""){
		alertMsg('cm.input.check.require.users',['#cols.pw'], param.get("langTypCd"));
		$("#loginArea #pw").focus();
		return;
	}
	param.put("time", ""+new Date().getTime());
	if(typeof(window.localStorage) != 'undefined'){
		var token = window.localStorage.getItem("uagntToken");
		if(token!=null && token!=""){
			param.put("token", token);
		}
	}
	callAjax("/cm/login/createSecuSessionAjx.do?secuSessionCode=${secuSessionCode}", null, function(data){
		var key = new RSAPublicKey(data.e, data.m);
		var data = encrypt(key, JSON.stringify(param.toJSON()));
		var frm = $('#loginSecuFrm');
		frm.find('#secu').val(data);
		frm.submit();
	}, null, function (xhr, ajaxOptions, thrownError) {
		var errCd = xhr.status;
		if(errCd=='400') errCd = '403';
		if(errCd=='403'){
			alert('<u:msg titleId="pt.login.renewConn" />');
			reload();
		} else {
			var msgCd = (errCd=='403' || errCd=='404') ? 'cm.msg.errors.'+errCd : 'cm.msg.errors.500';
			if(errCd=='403' || errCd=='404' || (param!=null && param.msgId!="cm.msg.errors.noMessage")){
				alertMsg(msgCd, [errCd, url]);
			}
		}
	});
}
function alertLoinMsg(){
	var message = "<u:out value='${message}' type='script' />";
	if(message!=''){
		alert(message);
		if($("#loginArea #lginId").val()=="") $("#lginId").focus();
		else $("#loginArea #pw").focus();
	}
}
function openLostPw(){
	var langTypCd = $('#loginArea #langTypCd').val();
	var popTitle = callMsg('pt.jsp.lostPw', null, langTypCd);
	dialog.open('setLostPwDialog', popTitle, './setLostPwPop.do?langTypCd='+langTypCd);
}
$(document).ready(function() {
	$uniformed = $(".styleThese").find("input, textarea, select, button").not(".skipThese");
	$uniformed.uniform();
	
	if($("#loginArea #lginId").val()=="") $("#loginArea #lginId").focus();
	else $("#loginArea #pw").focus();
	
	window.setTimeout('alertLoinMsg()', 200);
});
<c:if test="${not empty demoSite}">
function toggleDemoLogin(){<%// 데모 로그인 - 버튼%>
	var $orgChart = $("#orgChart");
	if($orgChart.css('display')=='none'){
		$("#loginBackground").css('background-image', '');
		$("#loginLogo").hide();
		$("#orgChart").show();
	} else {
		$("#loginBackground").css('background-image', 'url(${imageMap.bgImgPath})');
		$("#loginLogo").show();
		$("#orgChart").hide();
	}
}
var gDemoOrgId = null;
function openDemoUser(id, name){<%// 데모 로그인 조직도 클릭 %>
	gDemoOrgId = id;
	reloadFrame("userFrm", "/cm/demo/listUserFrm.do?orgId="+id);
}
function processDemoLogin(userUid){
	if(userUid==null || userUid=='') return;
	
	var storage = window.localStorage;
	if(storage != null && gDemoOrgId != null){
		storage.setItem('demoOrgId', gDemoOrgId);
	}
	
	var param = new ParamMap();
	param.put("userUid", userUid);
	param.put("demoLogin", "Y");
	param.put("langTypCd", $('#langTypCd').val());
	param.put("time", ""+new Date().getTime());
	callAjax("/cm/login/createSecuSessionAjx.do?secuSessionCode=${secuSessionCode}", null, function(data){
		var key = new RSAPublicKey(data.e, data.m);
		var data = encrypt(key, JSON.stringify(param.toJSON()));
		var frm = $('#loginSecuFrm');
		frm.find('#secu').val(data);
		frm.submit();
	}, null, function (xhr, ajaxOptions, thrownError) {
		var errCd = xhr.status;
		if(errCd=='400') errCd = '403';
		if(errCd=='403'){
			alert('<u:msg titleId="pt.login.renewConn" />');
			reload();
		} else {
			var msgCd = (errCd=='403' || errCd=='404') ? 'cm.msg.errors.'+errCd : 'cm.msg.errors.500';
			if(errCd=='403' || errCd=='404' || (param!=null && param.msgId!="cm.msg.errors.noMessage")){
				alertMsg(msgCd, [errCd, url]);
			}
		}
	});
}
</c:if>
//-->
</script>

<style type="text/css">
<!--
.login_img { position:absolute; width:756px; height:465px; top:50%; left:50%; text-align:center; margin:-260px 0 0 -378px;  }
.login_line { width:756px; height:465px; background:url(/images/login/bg_box.png) no-repeat; }
.login_logo { float:left; padding:23px 0 0 30px; }
.login_input { position:absolute; height:27px; top:472px; right:0; margin:0 15px 0 0; }
.login_copy { position:absolute; top:510px; right:0; font:11px "dotum","arial"; color:#777777; line-height:17px; margin:0 15px 0 0; }
.login_copy span { font-weight:bold; color:#777777; }
--> 
</style> 

</head>
<body>

<form id="loginFrm" class="styleThese">

<!--LoginImg S-->
<div class="login_img" id="loginBackground" style="background:url(${imageMap.bgImgPath}) no-repeat; background-position: ${imageMap.leftPx}px ${imageMap.topPx}px;">
<div class="login_line">
<c:if test="${not empty imageMap.logoImgPath}">
	<div class="login_logo" id="loginLogo">
		<img src="${imageMap.logoImgPath}" alt="logo" onerror="this.style.display='none'" />
	</div>
</c:if>
<c:if test="${not empty demoSite}">
	<div id="orgChart" style="position:relative; top:16px; left:16px; right:16px; bottom:15px; display:none; z-index:2; background-color:#FFFFFF">
		<div style="height:431px; width:251px; float:left; border:1px solid #bfc8d2; background-color:#FFFFFF;">
		<iframe id="orgFrm" name="orgFrm" src="/cm/demo/treeOrgFrm.do" style="position:relative; height:431px; width:251px; border:0px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
		</div>
		<div style="height:431px; width:5px; float:left;"></div>
		<div style="height:431px; width:464px; float:left; border:1px solid #bfc8d2; background-color:#FFFFFF;">
		<iframe id="userFrm" name="userFrm" src="/cm/util/reloadable.do" style="position:relative; top:6px; left:0px; height:411px; width:444px; border:0px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
		</div>
	</div>
</c:if>
	<div id="loginArea" class="login_input">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="body_lt"><strong>ID</strong></td>
			<td><u:input id="lginId" mandatory="Y" value="${loginId}" style="ime-mode:inactive;" /></td>
			<td class="width13"></td>
			<td class="body_lt"><strong>PW</strong></td>
			<td><u:input id="pw" type="password" mandatory="Y" onkeydown="processLogin(event)" /></td>
			<td class="width15"></td>
			<td><u:checkbox id="saveId" name="saveId" value="Y" checked="${not empty loginId}"/></td>
			<td class="bodyip_lt"><label for="saveId">ID Save</label></td>
			<c:if
				test="${fn:length(langTypPtCdBVoList)>1}">
			<td class="width2"></td>
			<td><select id="langTypCd" name="langTypCd" name="langTypCd" <u:elemTitle titleId="cols.langTyp" />><c:forEach
				items="${langTypPtCdBVoList}" var="langTypCdVo" varStatus="status">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.refVa1}" checkValue="${langTypCd}" /></c:forEach>
			</select></td>
			</c:if>
			<td class="width13"><c:if
				test="${fn:length(langTypPtCdBVoList)==1}"
				><input type="hidden" id="langTypCd" name="langTypCd" value="${langTypPtCdBVoList[0].cd}" />
			</c:if></td>
			<td><div class="button_basic"><ul><li class="basic"><a href="javascript:processLogin();"><span>&nbsp;<strong> Login </strong>&nbsp;</span></a></li></ul></div></td>
			<c:if
				test="${not empty lostPwEnable}">
			<td class="width5"></td>
			<td><div class="button_basic"><ul><li class="basic"><a href="javascript:openLostPw();"><span><strong> <u:msg titleId="pt.jsp.lostPw" alt="비밀번호 찾기" /> </strong></span></a></li></ul></div></td>
			</c:if>
		</tr>
		</table>
	</div>
	
	<div class="login_copy">
		<dl>
		<dd>COPYRIGHT(C)2014 <span>INNOBIZ</span>. ALL RIGHTS RESERVED.</dd>
		</dl>
	</div>

</div>
<c:if test="${not empty demoSite}">
<div class="button_basic" style="position:absolute; top:-20px; left:20px;"><ul><li class="basic"><a href="javascript:toggleDemoLogin();"><span>&nbsp;<strong> <u:msg titleId="pt.demo.login" alt="데모 로그인"/> </strong>&nbsp;</span></a></li></ul></div>
</c:if>
</div>

</form>
<form id="loginSecuFrm" action="/cm/login/processLogin.do" method="get">
<u:input type="hidden" id="secu" />
</form>
</body>
</html>