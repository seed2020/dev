<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--


//1명의 사용자 선택
function openSingUser(){
	var $view = $("#modMastInfo");
	var data = {userUid:$view.find("#userUid").val()};<%// 팝업 열때 선택될 데이타 %>
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if($('#setMastForm').find("#prevMastUid").val() == userVo.userUid) return;
		if(userVo!=null){
			$view.find("#userUid").val(userVo.userUid);
			$view.find("#rescNm").val(userVo.rescNm);
			//$view.find("#rescId").val(userVo.rescId);
			$view.find("#compId").val('1');
			$view.find("#deptRescNm").val(userVo.deptRescNm);
			$view.find("#gradeNm").val(userVo.gradeNm);
			$view.find("#mbno").val(userVo.mbno);
			$view.find("#email").val(userVo.email);
			//alert(new ParamMap(userVo));
		}
	});
	
}

function modMastSave(){
	//alert($("#schdlGrpId").val());
	if($('#modMastInfo #userUid').val() == ''){
		alertMsg('wc.msg.not.mastInfo');//변경할 마스터 정보가 없습니다.
		return;
	}
	if (true/*confirmMsg("cm.cfrm.save")*/ ) {
		var $form = $("#setMastForm");
		$form.attr("method", "POST");
		$form.attr("action", "./listGrpMng.do?menuId=${menuId}");
		$form.submit();
		dialog.close("setMastPop");
	}
}

$(document).ready(function() {
	setUniformCSS();
});

//-->
</script>

<c:set var="mast" value="홍길동" />

<div style="width:700px">

<form id="setMastForm">

<u:input type="hidden" id="menuId" value="${menuId}" />
<input id = "behave" name = "behave" type = "hidden" value = "modMast"/>
<input id = "schdlGrpId" name = "schdlGrpId" type = "hidden" value = "${schdlGrpId}"/>
<input id = "prevMastUid" name = "prevMastUid" type = "hidden" value = "${prevMastUid}"/>
<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="18%" class="head_lt"><strong><u:msg titleId="ct.cols.curMast" alt="현재 마스터" /></strong></td>
	<td><u:input id="mastNm" value="${mastNm}" titleId="cols.mast" readonly="Y" style="width: 557px;" /></td>
	</tr>
</u:listArea>

<u:listArea id = "modMastInfo">
	<tr>
	<td width="18%" class="head_lt"><strong><u:msg titleId="ct.cols.modMast" alt="변경할 마스터" /></strong></td>
	<td colspan="3"><u:buttonS titleId="cm.btn.search" alt="찾기" href="javascript:openSingUser();" /></td>
	<!-- 
	<td colspan="3"><u:buttonS titleId="cm.btn.search" alt="찾기" href="javascript:dialog.open('findOrgcPop','조직도','/bb/findOrgcPop.do?menuId=${menuId}');" /></td>
	 -->
	</tr>
	
	<tr>
	<u:input id="userUid" name="userUid" type = "hidden" value="${userUid}" />
	<td width="18%" class="head_lt"><u:msg titleId="cols.nm" alt="이름" /></td>
	<td><u:input id="rescNm" name = "rescNm" value="${rescNm}" titleId="cols.nm" readonly="Y" style="width: 207px;" /></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.comp" alt="회사" /></td>
	<td><u:input id="compId" name="compId" value="${compId}" titleId="cols.comp" readonly="Y" style="width: 207px;" /></td>
	</tr>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td><u:input id="deptRescNm" name="deptRescNm" value="${deptRescNm}" titleId="cols.dept" readonly="Y" style="width: 207px;" /></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.grade" alt="직급" /></td>
	<td><u:input id="gradeNm" name="gradeNm" value="${gradeNm}" titleId="cols.grade" readonly="Y" style="width: 207px;" /></td>
	</tr>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.phon" alt="전화번호" /></td>
	<td><u:input id="mbno" name="mbno" value="${mbno}" titleId="cols.phon" readonly="Y" style="width: 207px;" /></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.email" alt="이메일" /></td>
	<td><u:input id="email" name="email" value="${email}" titleId="cols.email" readonly="Y" style="width: 207px;" /></td>
	</tr>
</u:listArea>

<div class="headbox">
<dl>
<dd class="headbox_body"><table border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td>※ <u:msg titleId="ct.msg.mastChgFront" alt="이전 마스터의 권한은" /></td>
	<td><select id="userSecuCd" name="userSecuCd">
		<option value="R"><u:msg titleId="wc.cols.grp.red" alt="읽기" /></option>
		<option value="W"><u:msg titleId="wc.cols.grp.wrt" alt="쓰기" /></option>
		<option value="A"><u:msg titleId="wc.cols.grp.adm" alt="관리" /></option>
		</select></td>
	<td><u:msg titleId="ct.msg.mastChgBack" alt="으로 변경합니다." /></td>
	</tr>
	</tbody></table></dd>
</dl>
</div>

<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.save.success" var="msg" alt="저장 되었습니다." />
	<u:button titleId="cm.btn.save" alt="저장" auth="W" onclick="javascript:modMastSave();" />
	<!-- 
	<u:button titleId="cm.btn.save" alt="저장" auth="W" onclick="dialog.close(this); alert('${msg}');" />
	 -->
	<u:button titleId="cm.btn.close" alt="닫기" auth="W" onclick="dialog.close(this);" />
</u:buttonArea>

</form>
</div>

