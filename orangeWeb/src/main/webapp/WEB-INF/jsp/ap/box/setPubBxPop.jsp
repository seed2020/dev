<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 조직도 클릭 %>
function clickApTree(cd, nm){
	$("#setPubBxForm #pubBxDeptId").val(cd=='ROOT' ? '' : cd);
}<%
// 저장버튼 클릭 %>
function savePubBx(){
	var $form = $("#setPubBxForm");
	if(!validator.validate($form[0])){
		return;
	}
	var pubBxDeptId = $form.find("#pubBxDeptId").val();
	if(pubBxDeptId==''){<%
		// cm.select.check.mandatory="{0}"(을)를 선택해 주십시요. %>
		alertMsg('cm.select.check.mandatory', ['<u:msg titleId="ap.jsp.pubBxScope" alt="열람범위" />']);
		return;
	}
	var result = false;
	var paramObj = {process:"setPubBx", apvNos:"${param.apvNos}", pubBxDeptId:pubBxDeptId, pubBxEndYmd:$form.find("#pubBxEndYmd").val()};
	var regDeptId = "${param.recLstDeptId}";
	if(regDeptId!='') paramObj['regDeptId'] = regDeptId;
	callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", paramObj, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result) dialog.close('setPubBxDialog');
}
//-->
</script>
<div style="width:450px">

<form id="setPubBxForm">
<u:input id="pubBxDeptId" type="hidden" value="" mandatory="Y" /><c:if test="${not empty param.recLstDeptId}">
<u:input id="regDeptId" type="hidden" value="${param.recLstDeptId}" /></c:if>

<u:listArea>
	<tr>
		<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="ap.jsp.pubBxEndYmd" alt="게시기간" /></td>
		<td class="body_lt"><u:calendar id="pubBxEndYmd" titleId="ap.jsp.pubBxEndYmd" value="${pubBxEndYmd}" mandatory="Y" /></td>
	</tr>
	<tr>
		<td width="25%" class="head_lt"><u:mandatory /><u:msg titleId="ap.jsp.pubBxScope" alt="열람범위" /></td>
		<td><iframe id="treeRecLstOrgFrm" name="treeRecLstOrgFrm" src="./treeUpFrm.do?menuId=${menuId}&bxId=${param.bxId
			}&upward=${not empty param.recLstDeptId ? param.recLstDeptId : sessionScope.userVo.deptId
			}" height:180px;" frameborder="0" marginheight="0" marginwidth="0"></iframe></td>
	</tr>
</u:listArea>
</form>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:savePubBx();;" alt="저장" auth="A" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>