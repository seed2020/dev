<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// [AJAX] 이관 내역 조회 %>
function getTranProcList(genId, strtSeq){
	var param = {genId:genId};
	if(strtSeq != null && strtSeq != '') param['strtSeq'] = strtSeq;
	callAjax("./getTranProcListAjx.do?menuId=${menuId}", param, function(data){
		var strtSeq = appendProcList(data.procList);
		if(data.hasError){ $("#reTransferBtn").show(); }
		if(data.completed == 'N'){
			gTranProgressTimeout = window.setTimeout("getTranProcList('${param.genId}','"+strtSeq+"')", 750);
		} else {
			dialog.onClose("listTranProcDialog", function(){ reload(); });
		}
	}, null, null, true);
}<%
// AJAX 결과물 화면에 display %>
function appendProcList(procList){
	if(procList==null) return;
	var $area = $("#transferTbodyArea"), $tr, obj, html;
	for(var i=0; i<procList.length; i++){
		obj = procList[i].map;
		
		$tr = (i==0) ? $area.children('#SEQ_'+obj.seq) : null;
		if($tr==null || $tr.length==0){
			html = new StringBuffer();
			html.append("<tr id='SEQ_").append(obj.seq).append("'>");
			<%// td - 1 %>
			html.append("<td class='body_lt' style='vertical-align:top'>").append(obj.strtDt).append("</td>");
			<%// td - 2 open %>
			html.append("<td class='body_lt' style='vertical-align:top' id='contArea'><span id='tranContArea'>").append(obj.tranCont).append("</span>");
			
			html.append(" <span id='cntArea'>");
			if(obj.procCnt != null && obj.procCnt != "" && obj.allCnt != null &&  obj.allCnt != ""){
				html.append("( ").append(obj.procCnt).append(' / ').append(obj.allCnt).append(" )");
			}
			html.append("</span>");
			
			if(obj.errCont != null && obj.errCont != ""){
				html.append("<br/><span id='errContArea' style='color:red'>").append(obj.errCont).append("</span>");
			}
			<%// td - 2 close %>
			html.append("</td>");
			
			html.append("</tr>");

			$area.append(html.toString());
		} else {
			
			if(obj.procCnt != null && obj.procCnt != "" && obj.allCnt != null &&  obj.allCnt != ""){
				var html = new StringBuffer();
				html.append("( ").append(obj.procCnt).append(' / ').append(obj.allCnt).append(" )");
				$tr.find('#cntArea').text(html.toString());
			}
			
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
		return procList[procList.length-1].map.seq;
	}
	return null;
}<%
// 재이관 클릭 %>
function retransfer(){
	callAjax("./transRetransferAjx.do?menuId=${menuId}", {genId:'${param.genId}'}, function(data){
		openTranProcPop('${param.genId}');
	});
}
$(document).ready(function() {
	getTranProcList('${param.genId}');
});
//-->
</script>
<div style="width:700px;">

<div id="transferListDiv" style="height:350px; overflow-y:auto;">
<table  cellspacing="0" cellpadding="0" border="0">
<colgroup>
	<col width="140px" />
	<col />
</colgroup>
<tbody id="transferTbodyArea">
</tbody>
</table>
</div>

<u:blank />
<u:buttonArea>
<u:button titleId="wf.btn.deploy" alt="배포" onclick="deploy();" auth="SYS" id="reTransferBtn" style="display:none" />
<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>