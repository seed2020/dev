<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function apvd(mbshId){
	var $mbshCtId = $("#mbshCtId").val();
	if (confirmMsg("ct.cfrm.mbshApvd")) {
		callAjax('./transMbshApvd.do?menuId=${menuId}', {mbshCtId:$mbshCtId, mbshId:mbshId}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listMngJoinReqs.do?menuId=${menuId}&ctId='+$mbshCtId;
			}
		});
	}else{
		return;
	}
}
function napvd(mbshId){
	var $mbshCtId = $("#mbshCtId").val();
	if (confirmMsg("ct.cfrm.mbshNapvd")) {
		callAjax('./transMbshNapvd.do?menuId=${menuId}', {mbshCtId:$mbshCtId, mbshId:mbshId}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './listMngJoinReqs.do?menuId=${menuId}&ctId='+$mbshCtId;
			}
		});
	}else{
		return false;
	}
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<div style="width:600px">

<% // 표 %>
<u:listArea id="quesArea">
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.nm" alt="이름" /></td>
	<td width="32%" class="body_lt" colspan="3">
			${mbshInfoMap.userNm}
			<input id="mbshCtId" name="mbshCtId" type="hidden" value="${mbshCtId}" />
	</td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.userId" alt="사용자아이디" /></td>
	<td width="32%" class="body_lt"  colspan="3">${mbshInfoMap.userUid}</td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.comp" alt="회사" /></td>
	<td class="body_lt">${mbshCompNm}</td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td class="body_lt">${mbshInfoMap.deptRescNm}</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.posit" alt="직위" /></td>
	<td class="body_lt">${mbshInfoMap.positNm}</td>
	<td class="head_lt"><u:msg titleId="cols.ein" alt="사번" /></td>
	<td class="body_lt">${mbshInfoMap.ein}&nbsp;</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.compPhon" alt="회사전화" /></td>
	<td class="body_lt">${mbshInfoMap.compPhon}</td>
	<td class="head_lt"><u:msg titleId="cols.compFax" alt="회사FAX" /></td>
	<td class="body_lt">${mbshInfoMap.compFno}</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.homePhon" alt="자택전화" /></td>
	<td class="body_lt">${mbshInfoMap.homePhon}</td>
	<td class="head_lt"><u:msg titleId="cols.homeFax" alt="자택FAX" /></td>
	<td class="body_lt">${mbshInfoMap.homeFno}</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.mobPhon" alt="휴대전화" /></td>
	<td colspan="3" class="body_lt">${mbshInfoMap.mbno}</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.compAdr" alt="회사주소" /></td>
	<td colspan="3" class="body_lt">${mbshInfoMap.compAdr}</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.homeAdr" alt="자택주소" /></td>
	<td colspan="3" class="body_lt">${mbshInfoMap.homeAdr}</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.tich" alt="담당업무" /></td>
	<td colspan="3" class="body_lt">${mbshInfoMap.dutyNm}</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.email" alt="이메일" /></td>
	<td colspan="3" class="body_lt">${mbshInfoMap.email}</td>
	</tr>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button onclick="javascript:apvd('${mbshInfoMap.userUid}');" titleId="cm.btn.apvd" alt="승인" auth="R" />
	<u:button onclick="javascript:napvd('${mbshInfoMap.userUid}');" titleId="cm.btn.napvd" alt="미승인" auth="R" />
	<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" auth="R" />
</u:buttonArea>

</div>
