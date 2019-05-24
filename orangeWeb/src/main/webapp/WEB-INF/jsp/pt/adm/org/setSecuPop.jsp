<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 저장 %>
function saveSecu(secuId){
	var $form = $('#secuSetupForm');
	var obj = $form.find('#'+secuId)[0];
	var val = obj.checked ? obj.value : obj.value=='Y' ? 'N' : 'Y';
	
	var result = false;
	callAjax("./transSecuAjx.do?menuId=${menuId}", {'secuId':secuId, 'val':val, 'odurUids':$form.find('#odurUids').val()}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	
	if(result){
		dialog.close('setSecuDialog');
	}
}
//-->
</script>
<div style="width:350px">
<form id="secuSetupForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="odurUids" value="${param.odurUids}" id="odurUids" />
<u:listArea>

	<tr><td style="padding-left:2px">
		<u:checkArea style="width:100%"><u:msg
		titleId="or.jsp.setUserPop.lginIp" alt="로그인 IP" var="lginIp" /><u:msg
		titleId="or.jsp.setUserPop.polcExp" alt="정책 예외" var="polcExp" />
		<u:checkbox id="lginIpExYn" value="Y" name="lginIpExYn" title="${lginIp} ${polcExp}" inputStyle="width:20px" />
		<td style="padding:2px 3px 2px 0; float:right"><u:buttonS onclick="saveSecu('lginIpExYn');" titleId="cm.btn.save" auth="A" /></td>
		</u:checkArea>
		</td>
	</tr>

	<tr><td style="padding-left:2px">
		<u:checkArea style="width:100%"><u:msg
		titleId="or.jsp.setUserPop.sesnIp" alt="세션 IP" var="sesnIp" />
		<u:checkbox id="sesnIpExYn" value="Y" name="sesnIpExYn" title="${sesnIp} ${polcExp}" inputStyle="width:20px" />
		<td style="padding:2px 3px 2px 0; float:right"><u:buttonS onclick="saveSecu('sesnIpExYn');" titleId="cm.btn.save" auth="A" /></td>
		</u:checkArea>
		</td>
	</tr>

	<tr><td style="padding-left:2px">
		<u:checkArea style="width:100%">
		<u:checkbox id="useMobYn" value="N" name="useMobYn"
			titleId="or.jsp.setUserPop.notUseMobile" alt="모바일 사용 안함" inputStyle="width:20px" />
		<td style="padding:2px 3px 2px 0; float:right"><u:buttonS onclick="saveSecu('useMobYn');" titleId="cm.btn.save" auth="A" /></td>
		</u:checkArea>
		</td>
	</tr>

	<tr><td style="padding-left:2px">
		<u:checkArea style="width:100%">
		<u:checkbox id="useMsgLginYn" value="N" name="useMsgLginYn"
			titleId="or.jsp.setUserPop.notUseMsgLgin" alt="메세지 자동 로그인 사용 안함" inputStyle="width:20px" />
		<td style="padding:2px 3px 2px 0; float:right"><u:buttonS onclick="saveSecu('useMsgLginYn');" titleId="cm.btn.save" auth="A" /></td>
		</u:checkArea>
		</td>
	</tr>

	<tr><td style="padding-left:2px">
		<u:checkArea style="width:100%">
		<u:checkbox id="useMailYn" value="N" name="useMailYn"
			titleId="or.txt.notUseMail" alt="메일 사용 안함" inputStyle="width:20px" />
		<td style="padding:2px 3px 2px 0; float:right"><u:buttonS onclick="saveSecu('useMailYn');" titleId="cm.btn.save" auth="A" /></td>
		</u:checkArea>
		</td>
	</tr>

	<tr><td style="padding-left:2px">
		<u:checkArea style="width:100%">
		<u:checkbox id="useMsgrYn" value="N" name="useMsgrYn"
			titleId="or.txt.notUseMsgr" alt="메신저 사용 안함" inputStyle="width:20px" />
		<td style="padding:2px 3px 2px 0; float:right"><u:buttonS onclick="saveSecu('useMsgrYn');" titleId="cm.btn.save" auth="A" /></td>
		</u:checkArea>
		</td>
	</tr>

</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>