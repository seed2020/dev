<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><script type="text/javascript" src="${_cxPth}/js/validator.js" charset="UTF-8"></script>
<script type="text/javascript">
<!--<%// [확인] 저장 %>
function applyCfrm(){
	if($('#rjtOpin').val() == ''){
		$m.msg.alertMsg('cm.input.check.mandatory',['<u:msg titleId="cols.rjtOpin" alt="반려의견" />']);
		return;
	}	
	var arr=[];
	$('#setDiscForm').find("textarea").each(function(){
		if($(this).attr('disabled')!='disabled'){
			arr.push({name:$(this).attr('name'),value:$(this).val()});
		}
	});
	if(arr.length==0){
		return null;
	}
	var win = $m.nav.getWin();
	win.setDiscInfo(arr);
	$m.dialog.close('saveDiscDialog');
}
//-->
</script>
<form id="setDiscForm">
<!--entryzone S-->
<div class="entryzone">
    <div class="entryarea" id="discArea">
    <dl>
    	<dd class="etr_bodytit"><u:msg titleId="cols.rjtOpin" alt="반려의견" /></dd>
    	<dd class="etr_input"><div class="etr_textareain"><textarea name="rjtOpin" id="rjtOpin" rows="5" class="etr_ta"></textarea></div></dd>
    </dl>
    </div>
</div>
<div class="popbtnarea">
<div class="btnarea">
	<div class="size">
	<dl>
		<u:secu auth="W"><dd class="btn" onclick="applyCfrm();"><u:msg titleId="cm.btn.confirm" alt="확인" /></dd></u:secu>
		<dd class="btn" onclick="$m.dialog.close('saveDiscDialog');"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
	</dl>
	</div>
</div>
</div>
</form>
