<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:secu
	auth="W"><u:set test="${true}" var="writeAuth" value="Y"
/></u:secu>
<script type="text/javascript">
//<![CDATA[
var gOptConfig = ${optConfigJson};
function processApvPw(apvStatCd){
	
	var param = new ParamMap().getData($("#setApvPwForm")[0]);
	if(gOptConfig.notAlwApvPw!='Y'){
		if(param.get("pw").trim()==''){<%
			// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [비밀번호] %>
			$m.msg.alertMsg("cm.input.check.mandatory", ["#ap.jsp.doc.pw"], function(){
				$("#setApvPwForm input[name='pw']").focus();
			});
			return;
		}
	}
	
	var win = $m.nav.getWin();
	if(win==null) return;
	
	<%
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
			win.${empty param.callback ? 'callbackSecuId' : param.callback}(apvStatCd, secuId);
		}
	}
	$m.dialog.close('setApvPwPop')
}
//$(document).ready(function() {
//});
//]]>
</script>

<form id="setApvPwForm">
<div class="blankzone"><div class="blank15"></div></div>

<div class="pop_entryzone" >
<div class="entryarea">
	<dl><c:if test="${optConfigMap.notAlwApvPw != 'Y'}">
		<dd class="etr_tit"><u:msg titleId="ap.trans.apvPw" alt="결재비밀번호" /></dd>
		<dd class="etr_input"><div class="etr_inputin"><input type="password" name="pw" class="etr_iplt"/></div></dd>
		</c:if>
	</dl>
</div>
</div>

<div class="popbtnarea">
<div class="btnarea">
	<div class="size">
	<dl><u:secu auth="W">
		<dd class="btn" onclick="processApvPw('${param.apvStatCd}')"><u:term termId="ap.term.apvd" alt="승인" /></dd>
		</u:secu>
		<dd class="btn" onclick="$m.dialog.close('setApvPwPop')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
	</dl>
	</div>
</div>
</div>
</form>