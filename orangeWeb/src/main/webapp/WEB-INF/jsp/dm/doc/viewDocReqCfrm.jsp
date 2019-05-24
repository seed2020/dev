<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%// 문서열람요청 %>
function saveRequestTmp(param){
	callAjax('./transViewDocReqAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			history.go(-1);
		}
	});
}
$(document).ready(function() {
	if(confirmMsg("dm.cfrm.dtlView.noAuth")){ <% // dm.cfrm.dtlView.noAuth=권한이 없습니다.\n열람요청을 하시겠습니까? %>
		parent.dialog.open("viewDocReqCfrmDialog", '<u:msg titleId="dm.jsp.dtlView.request.title" alt="문서열람요청" />', "./viewDocReqCfrmPop.do?menuId=${menuId}&docGrpId=${docGrpId}");
	}else{
		if(opener != undefined) window.close();
	}
});
//-->
</script>
<c:if test="${frmYn != 'Y'}"><u:title titleId="ap.jsp.viewDoc" alt="문서 조회" notPrint="true" /></c:if>

<form id="dtlViewRequestForm" method="post">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" name="docGrpId" value="${docGrpId}" />

</form>