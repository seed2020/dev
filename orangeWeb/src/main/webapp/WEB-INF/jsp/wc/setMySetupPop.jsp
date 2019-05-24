<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>


<script type="text/javascript">

function formSubit(){
	
	if(validator.validate('setMySetupForm')){
		
		var aa = {PROM:$("input:checkbox[name='PROM']").is(":checked") ,
				WORK:$("input:checkbox[name='WORK']").is(":checked") , 
				EVNT:$("input:checkbox[name='EVNT']").is(":checked") , 
				ANNV:$("input:checkbox[name='ANNV']").is(":checked") ,
				PSN:$("input:checkbox[name='PSN']").is(":checked") ,
				DEPT:$("input:checkbox[name='DEPT']").is(":checked") ,
				COMP:$("input:checkbox[name='COMP']").is(":checked") ,
				GRP:$("input:checkbox[name='GRP']").is(":checked") 
		};	

		
		var param = $.parseJSON(JSON.stringify(aa));
		
		callAjax('./transSetMySetupPopSave.do?menuId=${menuId}', param, function(data) {
			if (data.result == 'ok') {
				
				if (data.message != null) {
					alert(data.message);				
					dialog.close('setMySetupPop');
					selectMonth('');
				}				
			}else{
				if (data.message != null) {
					alert(data.message);
				}
			}			
		});
	}	
}


</script>


<div style="width:600px">
<form id="setMySetupForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="18%" class="head_lt"><u:mandatory /><u:msg titleId="cols.schdlKnd" alt="일정종류" /></td>
	<td width="32%"><u:checkArea>
		<u:checkbox name="PROM" value="PROM" titleId="cols.prom" checked="${PROM}" inputClass="bodybg_lt" /></tr><tr>
		<u:checkbox name="WORK" value="WORK" titleId="cols.work" checked="${WORK}" inputClass="bodybg_lt" /></tr><tr>
		<u:checkbox name="EVNT" value="EVNT" titleId="cols.evnt" checked="${EVNT}" inputClass="bodybg_lt" /></tr><tr>
		<u:checkbox name="ANNV" value="ANNV" titleId="cols.annv" checked="${ANNV}" inputClass="bodybg_lt" />
		</u:checkArea></td>
	<td width="18%" class="head_lt"><u:mandatory /><u:msg titleId="cols.schdlTyp" alt="일정구분" /></td>
	<td width="32%"><u:checkArea>
		<u:checkbox name="PSN" value="PSN" titleId="wc.option.psnSchdl" checked="${PSN}" inputClass="bodybg_lt" /></tr><tr>
		<u:checkbox name="DEPT" value="DEPT" titleId="wc.option.deptSchdl" checked="${DEPT}" inputClass="bodybg_lt" /></tr><tr>
		<u:checkbox name="COMP" value="COMP" titleId="wc.option.compSchdl" checked="${COMP}" inputClass="bodybg_lt" /></tr><tr>
		<u:checkbox name="GRP" value="GRP" titleId="wc.option.grpSchdl" checked="${GRP}" inputClass="bodybg_lt" />
		</u:checkArea></td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="formSubit();" alt="저장" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
