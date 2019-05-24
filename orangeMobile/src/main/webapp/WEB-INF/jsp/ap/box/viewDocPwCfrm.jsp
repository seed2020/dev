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
		
		var docPw = $(parent.document).find('#docPw').val();
		if(docPw==''){
			$m.msg.alertMsg('cm.input.check.mandatory', ['<u:msg titleId="ap.trans.docPw" alt="문서비밀번호" />']);
			return false;
		}
		
		var key = null, secuId = null;
		$m.ajax("${_cxPth}/cm/login/createSecuSessionAjx.do", null, function(data){
			key = $m.rsa.getKey(data.e, data.m);
		});
		
		if(key != null){
			var data = $m.rsa.encrypt(key, JSON.stringify({apvNo:'${param.apvNo}',docPw:docPw}));
			$m.ajax("${_cxPth}${empty isDM ? '/ap/box' : '/dm/doc'}/getSecuIdAjx.do?menuId=${menuId}&bxId=${param.bxId}", {secu:data}, function(data){
				secuId = data['secuId'];
			});
		} else {
			return false;
		}
		
		if(secuId != null){
			$m.nav.curr(null, '${_uri}?menuId=${menuId}&apvNo=${param.apvNo}&bxId=${param.bxId}<c:if test="${empty isDM}">&apvLnPno=${param.apvLnPno}&apvLnNo=${param.apvLnNo}</c:if>&secuId='+secuId);
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
		<dd class="etr_tit"><u:msg titleId="ap.trans.docPw" alt="문서비밀번호" /></dd>
		<dd class="etr_input"><div class="etr_inputin"><input type="password" id="docPw" class="etr_iplt"/></div></dd>
	</dl>
</div>
</div>
</form>