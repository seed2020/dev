<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // 폼 전송 %>
function formSubmit(){
	if (validator.validate('setGrpForm') && true/*confirmMsg("cm.cfrm.save")*/ ) {
		
		var userInfo = [];
		var defaultAuthVal = $("#defaultAuth").val();
		var emailSendYnVal = $(":radio[name='emailSendYn']:checked").val();
		
		userInfo.push("<input id='defAuth' name='defAuth' type='hidden' style='width:80px' value='"+defaultAuthVal+"' />");
		userInfo.push("<input id='emailSendYns' name='emailSendYns' type='hidden' style='width:80px' value='"+emailSendYnVal+"' />");
		$("#userInfo").append(userInfo.join(''));
		
		var $form = $("#setGrpForm");
		$form.attr('method', 'POST');
		$form.attr('action', './transSetGrpPopSave.do?menuId=${menuId}');
		$form.submit();
	}
}

<% %>
function delUser(){
	var index = $("#selectedGrpMbsh option").index($("#selectedGrpMbsh option:selected"));
	$("#selectedGrpMbsh option:selected").remove();
	var userUidIndex=0;

	$("#userInfo").find("#userUid").each(function(){
		if(userUidIndex==index){
			$(this).remove();
			return false;
		}
	userUidIndex++;
	});
}


<% // 여러명의 사용자 선택 %>
//여러명의 사용자 선택
function openMuiltiUser(mode){
	var $view = $("#selectedGrpMbsh"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	
	$("#userInfo").find("#userUid").each(function(){
		data.push({userUid:$(this).val()});
	});
	
	<%// option : data, multi, titleId %>
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			var userInfo = [];
			arr.each(function(index, userVo){
				buffer.push("<option>"+userVo.rescNm+", "+userVo.deptRescNm+", "+userVo.gradeNm+"</option>");
				userInfo.push("<input id='userUid' name='userUid' type='hidden' style='width:80px' value='"+userVo.userUid+"' />");
				userInfo.push("<input id='rescNm' name='rescNm' type='hidden' style='width:80px' value='"+userVo.rescNm+"' />");
			});
			$view.html(buffer.join(''));
			$("#userInfo").html(userInfo.join(''));
			setUniformCSS($view[0]);
		}
	});
}


//-->
</script>
<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="grpNm" value="부천지역모임" elseValue="" />
<u:set test="${fnc == 'mod'}" var="selected" value=" selected" elseValue="" />
<input id = "userUid" name = "userUid" type = "hidden" />
<div style="width:600px">
<form id="setGrpForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<input id="setSchdlGrp" name="setSchdlGrp" value="setSchdlGrp" type="hidden"/>
<div id = "userInfo" name = "userInfo">
</div>


<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="27%" class="head_lt"><u:mandatory /><u:msg titleId="cols.grpNm" alt="그룹명" /></td>
	<td width="73%"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:input id="grpNm" value="${grpNm}" titleId="cols.grpNm" style="width: 194px;" maxByte="100" mandatory="Y"/></td>
		</tbody></table></td>
	</tr>
	
	<tr>
	<td rowspan="2" class="head_lt"><u:msg titleId="cols.mbsh" alt="회원" /></td>
	<td><table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td width="230"><u:input id="recvr" value="" titleId="cols.recvr" style="width: 384px;" /></td>
		<td><u:buttonS titleId="cm.btn.choice" alt="선택" onclick="javascript:openMuiltiUser();" /></td>
		<!-- 
		<td><u:buttonS titleId="cm.btn.choice" alt="선택" onclick="dialog.open('findOrgcPop','조직도','/bb/findOrgcPop.do?menuId=${menuId}');" /></td>
		 -->
		</tr>
		</tbody></table></td>
	</tr>
	
	<tr >
	<td><table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td width="230"><select id="selectedGrpMbsh" size="3" style="width: 393px;">
			</select></td>
		<td style="padding-top: 2px; vertical-align: top;"><u:buttonS titleId="cm.btn.del" alt="삭제" onclick="javascript:delUser();"/></td>
		</tr>
		</tbody></table></td>
	</tr>
	
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.dftAuth" alt="기본권한" /></td>
	<td><select id = "defaultAuth">
		<option value = "R"><u:msg titleId="wc.cols.grp.red" alt="읽기" /></option>
		<option value = "W"><u:msg titleId="wc.cols.grp.wrt" alt="쓰기" /></option>
		<option value = "A"><u:msg titleId="wc.cols.grp.adm" alt="관리" /></option>
		</select></td>
	</tr>
	
	<%-- <tr>
	<td class="head_lt"><u:msg titleId="cols.emailSendYn" alt="이메일발송여부" /></td>
	<td><u:checkArea >
		<!-- 
		<u:radio name="emailSendYn" value="Y" titleId="cm.option.yes" alt="예" checked="${fnc == 'mod'}" inputClass="bodybg_lt" />
		 -->
		<u:radio name="emailSendYn" value="Y" titleId="cm.option.yes" alt="예" checked="true" inputClass="bodybg_lt" />
		<u:radio name="emailSendYn" value="N" titleId="cm.option.no" alt="아니오" inputClass="bodybg_lt" />
		</u:checkArea></td>
	</tr> --%>
</u:listArea>

<u:buttonArea>
	<!--
	<u:button titleId="cm.btn.save" onclick="dialog.close(this);" alt="저장" auth="W" />
	  -->
	<u:button titleId="cm.btn.save" onclick="javascript:formSubmit();" alt="저장" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
