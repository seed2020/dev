<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var gMoveToFormBxId=null;<%
// 프레임 안의 폼박스 클릭 %>
function setMoveToFormBx(formBxId){
	gMoveToFormBxId = formBxId;
}<%
// 저장 버튼 클릭 %>
function saveFormMove(){
	if(gMoveToFormBxId==null || gMoveToFormBxId=='ROOT'){
		alertMsg("ap.jsp.setApvForm.selFormBxFirst");<%// ap.jsp.setApvForm.selFormBxFirst=양식함을 먼저 선택해 주십시요.%>
	} else {
		var param = {formBxId:gMoveToFormBxId, formIds:"${param.formIds}"};
		var result = false;
		callAjax('./transMoveToFormBxAjx.do?menuId=${menuId}', param, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				getIframeContent('formListFrm').reload();
				dialog.close("setMoveToFormBxDialog");
			}
		});
	}
}
//-->
</script>
<div style="width:400px; height:350px">

<u:boxArea
	outerStyle="width:398px; overflow-x:hidden; overflow-y:hidden; padding:0px;" className="wbox"
	innerStyle = "NO_INNER_IDV">
<iframe id="treeFormBxFrmPop" name="treeFormBxFrmPop" src="./treeFormBxFrm.do?menuId=${menuId}&noInit=Y&callback=setMoveToFormBx" style="width:100%; height:300px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:boxArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveFormMove();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</div>