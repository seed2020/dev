<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%// 문서열람요청 %>
function saveRequest(param){
	callAjax('/dm/doc/transViewDocReqAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			if(opener != undefined) window.close(); else dialog.close('viewDocReqCfrmDialog'); 
		}
	});
}<%// [요청] 저장 %>
function dtlViewRequest(){
	if(validator.validate('setDocViewReqForm')){
		var param = new ParamMap().getData("setDocViewReqForm");
		saveRequest(param);
	}
}
$(document).ready(function() {
});
//-->
</script>
<div style="width:500px;">
<form id="setDocViewReqForm">
<u:input type="hidden" id="docGrpId" value="${param.docGrpId}" />
<u:listArea id="setDocViewReqArea" colgroup="35%," >
<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="dm.cols.dtlView.request.tgt" alt="열람요청대상" /></td>
	<td class="bodybg_lt" >
		<u:checkArea>
			<u:radio name="reqTgtTyp" value="D" titleId="cols.dept" />
			<u:radio name="reqTgtTyp" value="U" titleId="cols.user" checked="true"/>
		</u:checkArea>
	</td>
</tr>
<c:if test="${dtlViewPrdAllow eq 'Y' }">
<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="dm.cols.dtlView.request.prd" alt="열람요청기간" /></td>
	<td class="bodybg_lt" >
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:calendar id="readStrtDt" name="readStrtDt" option="{end:'readEndDt'}" titleId="dm.cols.dtlView.strtDt" value="${readStrtDt }"/></td>
			<td class="search_body_ct"> ~ </td>
			<td><u:calendar id="readEndDt" name="readEndDt" option="{start:'readStrtDt'}" titleId="dm.cols.dtlView.endDt" value="${readEndDt }" mandatory="Y"/></td>
			</tr>
		</table>
	</td>
</tr>
</c:if>
</u:listArea>
</form>	
<u:buttonArea>
	<u:button titleId="dm.btn.dtlView.request" alt="열람요청" href="javascript:dtlViewRequest();" auth="W" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="if(opener != undefined) window.close(); else dialog.close(this);" />
</u:buttonArea>

</div>