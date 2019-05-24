<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
$(document).ready(function() {
	
	var html = $('#pwForm').html();
	$m.dialog.confirm(html, function(result){
		
		if(!result) {
			$m.nav.prev();
			return;
		}

		var pw = $(parent.document).find('#pw').val();
		if(pw==''){
			$m.msg.alertMsg('cm.input.check.mandatory', ['<u:msg titleId="cols.pw" alt="비밀번호" />']);
			return false;
		}
		
		var key = null, result = null, data = null;
		$m.ajax("${_cxPth}/cm/login/createSecuSessionAjx.do", null, function(data){
			key = $m.rsa.getKey(data.e, data.m);
		});

		if(key != null){
			data = $m.rsa.encrypt(key, JSON.stringify({pw:pw}));
			$m.ajax('/bb/getLoginChkAjx.do?brdId=${param.brdId}&menuId=${menuId}', {secu:data}, function(data){
				result = data['result'];
			});
		} else {
			return false;
		}
		
		if(result == 'ok'){
			$m.ajax("${_cxPth}/cm/login/createSecuSessionAjx.do", null, function(data){
				key = $m.rsa.getKey(data.e, data.m);
			});
			if(key != null){
				data = $m.rsa.encrypt(key, JSON.stringify({pw:pw}));
				$m.nav.curr(null, '${_uri}?menuId=${menuId}&brdId=${param.brdId}&bullId=${param.bullId}&secu='+data);
			} else {
				return false;
			}
		} else {
			return false;
		}

	}, true);
});
//]]>
</script>

<form style="display:none" id="pwForm">
<div class="blankzone"><div class="blank15"></div></div>

<div class="pop_entryzone" >
<div class="entryarea">
	<dl>
		<dd class="etr_tit"><u:msg titleId="bb.jsp.setLoginPop.tx01" alt="로그인 비밀번호를 입력하세요." /></dd>
		<dd class="etr_blank"></dd>
		<dd class="etr_input"><div class="etr_inputin"><input type="password" id="pw" class="etr_iplt"/></div></dd>
	</dl>
</div>
</div>
</form>

