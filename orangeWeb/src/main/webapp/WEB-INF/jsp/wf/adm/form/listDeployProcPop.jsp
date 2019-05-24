<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// [AJAX] 배포 내역 조회 %>
function getDeployProcList(genId, formNo, strtSeq){
	var param = {genId:genId, formNo:formNo};
	if(strtSeq != null && strtSeq != '') param['strtSeq'] = strtSeq;
	callAjax("./getDeployProcListAjx.do?menuId=${menuId}", param, function(data){
		var strtSeq = appendProcList(data.procList);
		//if(data.hasError){ $("#reGenBtn").show(); }
		if(data.completed == 'N'){
			gGenProgressTimeout = window.setTimeout("getDeployProcList('${param.genId}','${param.formNo}','"+strtSeq+"')", 750);
		} else {
			dialog.onClose("listDeployProcDialog", function(){ listDeployReload(); });
		}
	}, null, null, true);
}<%
// AJAX 결과물 화면에 display %>
function appendProcList(procList){
	if(procList==null) return;
	var $area = $("#genTbodyArea"), $tr, obj, html;
	for(var i=0; i<procList.length; i++){
		obj = procList[i].map;
		
		$tr = (i==0) ? $area.children('#SEQ_'+obj.seq) : null;
		if($tr==null || $tr.length==0){
			html = new StringBuffer();
			html.append("<tr id='SEQ_").append(obj.seq).append("'>");
			<%// td - 1 %>
			html.append("<td class='body_lt' style='vertical-align:top'>").append(obj.strtDt).append("</td>");
			<%// td - 2 open %>
			html.append("<td class='body_lt' style='vertical-align:top' id='contArea'><span id='procContArea'>").append(obj.procCont).append("</span>");
			
			if(obj.errCont != null && obj.errCont != ""){
				html.append("<br/><span id='errContArea' style='color:red'>").append(obj.errCont).append("</span>");
			}
			<%// td - 2 close %>
			html.append("</td>");
			
			html.append("</tr>");

			$area.append(html.toString());
		} else {
			
			if(obj.errCont != null && obj.errCont != ""){
				var html = new StringBuffer();
				html.append("<br/><span id='errCont' style='color:red'>").append(obj.errCont).append("</span>");
				$tr.find('#contArea').append(html.toString());
			}
		}
	}
	
	var tranDiv = $("#transferListDiv")[0];
	tranDiv.scrollTop = tranDiv.scrollHeight;
	
	if(procList.length > 0){
		return procList[procList.length-1].map.seq+1;
	}
	return null;
}<%
// 재배포 클릭 %>
function reGen(){
	callAjax("./transRetransferAjx.do?menuId=${menuId}", {genId:'${param.genId}'}, function(data){
		openTranProcPop('${param.genId}');
	});
}
$(document).ready(function() {
	getDeployProcList('${param.genId}', '${param.formNo}');
});
//-->
</script>
<div style="width:400px;">

<div id="transferListDiv" style="height:250px; overflow-y:auto;">
<table width="100%" cellspacing="0" cellpadding="0" border="0">
<colgroup>
	<col width="40%," />
	<col />
</colgroup>
<tbody id="genTbodyArea">
</tbody>
</table>
</div>

<u:blank />
<u:buttonArea>
<u:button titleId="ap.btn.reTransfer" alt="재배포" onclick="reGen();" auth="SYS" id="reGenBtn" style="display:none" />
<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>