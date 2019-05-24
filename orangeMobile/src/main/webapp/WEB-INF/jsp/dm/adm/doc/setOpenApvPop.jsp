<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
//-->
</script>
<!--entryzone S-->
<div class="entryzone">
    <div class="entryarea" id="discArea">
    <dl>
    	<dd class="etr_bodytit"><u:msg titleId="cols.rjtOpin" alt="반려의견" /></dd>
    	<dd class="etr_input"><div class="etr_textareain"><textarea name="rjtOpin" id="rjtOpin" rows="5" class="etr_ta" readonly="readonly">${!empty dmPubDocTgtDVo ? dmPubDocTgtDVo.rjtOpin : ''}</textarea></div></dd>
    </dl>
    </div>
</div>
<div class="popbtnarea">
<div class="btnarea">
	<div class="size">
	<dl>
		<dd class="btn" onclick="$m.dialog.close('viewOpenReqDialog');"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
	</dl>
	</div>
</div>
</div>
