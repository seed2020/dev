<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<%
//이관 진행 내역 조회용 Timeout %>
var gGenProgressTimeout = null;<%
//이관 진행 내역 팝업 - 진행상태 클릭 하면 %>
function listDeployProcPop(genId ,formNo){
	if(genId==null || genId=='' || formNo==null || formNo=='') return;
	
	dialog.open('listDeployProcDialog','<u:msg titleId="wf.jsp.deploy.process" alt="배포 진행 내역" />','./listDeployProcPop.do?menuId=${menuId}&genId='+genId+'&formNo='+formNo);
	dialog.onClose('listDeployProcDialog', function(){
		if(gGenProgressTimeout != null){
			window.clearTimeout(gGenProgressTimeout);
			gGenProgressTimeout = null;
		}
	});
}<% // 배포 완료시 목록 리로드 %>
function listDeployReload(){
	getIframeContent('listDeployFrm').listDeployReload();
}
<% // [하단버튼:메뉴등록] - 팝업 %>
function delGenList(){
	getIframeContent('listDeployFrm').delGenList();
}
$(document).ready(function() {
});
//-->
</script>
<div style="width:800px;">

<iframe id="listDeployFrm" name="listDeployFrm" src="./listDeployFrm.do?menuId=${menuId}" style="width:100%;min-height:600px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<u:buttonArea topBlank="true">
	<u:button href="javascript:delGenList();" titleId="cm.btn.del" alt="삭제" auth="A" 
	/><u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>
