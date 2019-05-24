<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:secu
	auth="W"><u:set test="${true}" var="writeAuth" value="Y"
/></u:secu>
<script type="text/javascript">
//<![CDATA[
// 전송 준비 - 승인, 상신 메세지 처리 %>
function saveApvd(apvStatCd){
	var $form = $("#setSurvApvd");
	
	if($form.find("#rjtOpin").val() == ''){
		$m.msg.alertMsg('cm.input.check.mandatory','<u:msg titleId="cols.rjtOpin"  />');
		return;
	}
	var win = $m.nav.getWin();
	win.submitApvd($form.find("#rjtOpin").val());
	
}
//]]>
</script>

<form id="setSurvApvd">
<div class="blankzone"><div class="blank15"></div></div>

<div class="pop_entryzone" >
<div class="entryarea">
	<dl>
		<dd class="etr_tit"><u:msg titleId="cols.rjtOpin" alt="반려의견" /></dd>
		<dd class="etr_input"><div class="etr_textareain"><textarea id="rjtOpin" class="etr_ta" rows="3" /></textarea></div></dd>
	</dl>
</div>
</div>

<div class="popbtnarea">
<div class="btnarea">
	<div class="size">
	<dl><u:secu auth="W">
		<dd class="btn" onclick="saveApvd();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
		</u:secu>
		<dd class="btn" onclick="$m.dialog.close('setSurvApvdPop')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
	</dl>
	</div>
</div>
</div>
</form>