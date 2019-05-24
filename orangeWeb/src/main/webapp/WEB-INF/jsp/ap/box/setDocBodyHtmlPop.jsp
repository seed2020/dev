<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
function setDocBodyHtml(){
	var editorCtrl = editor('docBodyHtml');
	var html = editorCtrl.getHtml();
	$("#bodyHtmlViewArea td.editor").html(html);
	var $area = $("#bodyHtmlEditArea");
	var $bodyHtml = $area.find("input[name='bodyHtml']");
	if($bodyHtml.length==0){
		$area.append("<input type='hidden' name='bodyHtml' />");
		$area.find("[name='bodyHtml']").val(html);
	} else {
		$bodyHtml.val(html);
	}
	editorCtrl.prepare();
	dialog.close("setDocBodyHtmlDialog");
}
$(document).ready(function() {
	if(unloadEvent.editorType!='namo'){
		var bodyHtml = $("#bodyHtmlEditArea input[name='bodyHtml']").val();
		if(bodyHtml!=null){
			editor('docBodyHtml').setInitHtml(bodyHtml);
		}
	}
});
<%// 나모에디터 초기화 %>
function initNamo(){
	var bodyHtml = $("#bodyHtmlEditArea input[name='bodyHtml']").val();
	if(bodyHtml!=null){
		editor('docBodyHtml').setInitHtml(bodyHtml);
	} else {
		editor('docBodyHtml').setInitHtml($("#docBodyHtml").val());
	}
	editor('docBodyHtml').prepare();
}
//-->
</script>

<div style="width:935px; height:550px">

<div id="docBodyHtmlArea"></div><%
	com.innobiz.orange.web.ap.vo.ApOngdBodyLVo apOngdBodyLVo1 = (com.innobiz.orange.web.ap.vo.ApOngdBodyLVo)request.getAttribute("apOngdBodyLVo");
	if(apOngdBodyLVo1 != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(apOngdBodyLVo1.getBodyHtml());
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", apOngdBodyLVo1.getBodyHtml());
		}
	}
%>
<u:editor id="docBodyHtml" height="500px" width="100%" areaId="docBodyHtmlArea" value="${_bodyHtml}" namoInitFnc="initNamo"/>

<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" onclick="setDocBodyHtml();" alt="확인" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>
