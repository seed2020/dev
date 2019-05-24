<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var gMoveToFormGrpId=null;<%
// 프레임 안의 폼박스 클릭 %>
function setMoveToFormGrp(grpId){
	gMoveToFormGrpId = grpId;
}<%
// 저장 버튼 클릭 %>
function saveFormMove(){
	if(gMoveToFormGrpId==null || gMoveToFormGrpId=='ROOT'){
		alertMsg("wf.msg.selFormGrpFirst");<%// wf.msg.selFormGrpFirst=양식 그룹을 먼저 선택해 주십시요. %>
	} else {
		var param = {grpId:gMoveToFormGrpId, formNos:"${param.formNos}"};
		callAjax('./transMoveToFormGrpAjx.do?menuId=${menuId}', param, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				getIframeContent('formListFrm').reload();
				dialog.close("setMoveToFormGrpDialog");
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
<iframe id="treeFormGrpFrmPop" name="treeFormGrpFrmPop" src="./treeGrpFrm.do?menuId=${menuId}&noInit=Y&callback=setMoveToFormGrp" style="width:100%; height:300px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:boxArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveFormMove();" alt="저장" auth="A" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</div>