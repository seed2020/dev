<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 폼 양식 등록(erp 폼 용) %>
function openErpFormPop(erpFormId){
	var popTitle = erpFormId==null ? '<u:msg titleId="ap.jsp.setFormEdit.regFormCmpt" alt="본문 양식 등록" />' : '<u:msg titleId="ap.jsp.setFormEdit.modFormCmpt" alt="본문 양식 수정" />';
	dialog.open("setErpFormDialog", popTitle, "./setErpFormPop.do?menuId=${menuId}&erpFormTypCd=${param.erpFormTypCd}"+(erpFormId==null ? '' : '&erpFormId='+erpFormId));
	dialog.onClose("listErpFormDialog", function(){ dialog.close("setErpFormDialog"); });
}<%
// 폼 양식 수정 %>
function manageErpForm(mode){
	if(mode == 'reg'){
		openErpFormPop(null);
	} else {
		var erpFormId = getIframeContent('listErpFormFrm').getChecked();
		if(erpFormId != null){
			if(mode == 'mod'){
				openErpFormPop(erpFormId);
			} else if(mode == 'del'){
				delErpForm(erpFormId);
			}
		}
	}
}<%
// 폼 양식 삭제(erp 폼 용) %>
function delErpForm(erpFormId){
	if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transErpFormDelAjx.do?menuId=${menuId}', {erpFormId:erpFormId}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				getIframeContent('listErpFormFrm').reload();
			}
		});
	}
}<%
// 확인 버튼 클릭 - [본문(폼양식) 의 경우] %>
function applyXmlFrom(erpFormTypCd){
	var erpFormId = getIframeContent('listErpFormFrm').getChecked();
	if(erpFormId != null){
		if(erpFormTypCd=='xmlFromAp' || erpFormTypCd=='xmlEditFromAp'){
			setXmlFromApData(erpFormId, erpFormTypCd);
		} else if(erpFormTypCd=='xmlFromErp'){
			setXmlFromErpData(erpFormId);
		}
	}
}<%
// 확인 버튼 클릭 - [본문(폼양식) 의 경우] %>
function applyXmlEditFromApForm(){
	var erpFormId = getIframeContent('listErpFormFrm').getChecked();
	if(erpFormId != null){
		setXmlFromApData(erpFormId);
	}
}<%
// 확인 버튼 클릭 - [본문(편집기) 의 경우] %>
function applyXmlFromErpForm(){
	var erpFormId = getIframeContent('listErpFormFrm').getChecked();
	if(erpFormId != null){
		setXmlFromErpData(erpFormId);
	}
}<%
// 설정 지우기 버튼 클릭 %>
function clearXmlFromApForm(){
	setXmlFromErpData('');
}<%
// onload %>
$(document).ready(function() {
	//getIframeContent('listErpFormFrm').location.href= './listErpFormFrm.do?menuId=${menuId}&bxId=${param.bxId}';
});
//-->
</script>
<div style="width:500px">

<u:boxArea
	outerStyle="height:380px; overflow-x:hidden; overflow-y:auto; padding:10px 10px 0px 10px;" className="wbox"
	innerStyle = "NO_INNER_IDV">
<iframe id="listErpFormFrm" name="listErpFormFrm" src="./listErpFormFrm.do?menuId=${menuId}&bxId=${param.bxId}&erpFormTypCd=${param.erpFormTypCd}${not empty param.erpFormId ? '&erpFormId='.concat(param.erpFormId) : ''}" style="width:100%; height:370px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:boxArea>

<u:buttonArea>
	<u:button href="javascript:applyXmlFrom('${param.erpFormTypCd}');" titleId="cm.btn.confirm" alt="확인" auth="A" /><c:if test="${sessionScope.userVo.userUid eq 'U0000001'}">
	<u:button href="javascript:manageErpForm('reg')" titleId="cm.btn.reg" alt="등록" auth="A" popYn="Y" />
	<u:button href="javascript:manageErpForm('del')" titleId="cm.btn.del" alt="삭제" auth="A" /></c:if><c:if
		test="${param.erpFormTypCd eq  'xmlFromErp'}">
	<u:button href="javascript:clearXmlFromApForm()" titleId="ap.btn.delSetup" alt="설정 지우기" auth="A" popYn="Y" /></c:if>
	<u:button titleId="cm.btn.cancel" alt="취소" onclick="dialog.close(this);" />
</u:buttonArea>
</div>
