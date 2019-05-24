<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Enumeration, java.util.ArrayList" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function submitLostPw(){
	if(validator.validate('lostPwArea')){
		var param = new ParamMap().getData('lostPwArea');
		param.put('langTypCd','${param.langTypCd}');
		callAjax('./transLostPwAjx.do', param.map, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				dialog.close('setLostPwDialog');
			}
		});
	}
}
//-->
</script>
<div style="width:300px; ">

<u:listArea id="lostPwArea" colgroup="35%,*">
	<tr>
		<td class="head_lt"><u:msg titleId="cols.lginId" alt="로그인ID" /></td>
		<td><u:input id="lginId" maxLength="30" alt="로그인ID" style="width:92%" mandatory="Y" /></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.extnEmail" alt="외부이메일" /></td>
		<td><u:input id="extnEmail" maxLength="40" alt="외부이메일" valueOption="email" style="width:92%" mandatory="Y" /></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" alt="확인" onclick="submitLostPw()" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>
</div>