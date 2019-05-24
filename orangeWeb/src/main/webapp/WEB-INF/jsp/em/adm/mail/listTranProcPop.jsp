<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<style type="text/css">
.status.progress {border: 1px solid rgb(191, 200, 210); border-image: none; background-color: rgb(255, 255, 255);text-align:left;width:90%;}
</style>
<script type="text/javascript">
<!--<%
// 이관 클릭 %>
function processTransfer(){
	<%
	// 이관 진행 %>
	callAjax("./tranMailAdrTranAjx.do?menuId=${menuId}", null, function(data){
		if(data.message != null) alert(data.message);
		if(data.result=='ok'){
			$("#transferBtn").hide();
			if(data.tranId!=null)
				getTranProcList(data.tranId);
			dialog.onClose("setTranDialog", function(){ reload(); });
		}			
	});
}<%
// [AJAX] 이관 내역 조회 %>
function getTranProcList(tranId){
	var param = {tranId:tranId};
	callAjax("./getMailAdrTranAjx.do?menuId=${menuId}", param, function(data){
		//appendProcList(data.procList);
		if(data.hasError){ $("#reTransferBtn").show(); $('#errYn').text('Y');}
		else{
			if(data.completed===undefined || data.completed == 'N'){
				if(data.completed == 'N'){
					$('#progress').find('img').eq(0).css("width",(data.processCnt / Number('${userCnt }')) * 100 + "%");
				}
				gTranProgressTimeout = window.setTimeout("getTranProcList('"+tranId+"')", 750);
			} else if(data.completed == 'Y'){
				$('#progress').find('img').eq(0).css("width","100%");
				$('#errYn').text('완료');
			}	
		}
		dialog.onClose("listTranProcDialog", function(){ reload(); });
	}, null, null, true);
}<%
// 재이관 클릭 %>
function retransfer(){
	callAjax("./transReMailAdrTranAjx.do?menuId=${menuId}", {tranId:'${param.tranId}'}, function(data){
		openTranProcPop('${param.tranId}');
	});
}
$(document).ready(function() {
	<c:if
	test="${!empty param.tranId}">	getTranProcList('${param.tranId}');</c:if>
});
//-->
</script>
<div id="transferPop" style="width:450px;">

<u:title titleId="ap.cols.tgtCnt" alt="대상 건수" type="small" />
<u:listArea colgroup=",20%,20%,30%" noBottomBlank="true">
<tr>
	<th class="head_ct"><u:msg titleId="cols.user" alt="사용자" /></th>
	<th class="head_ct"><u:msg titleId="cols.fld" alt="폴더" /></th>
	<th class="head_ct"><u:msg titleId="cols.adrBook" alt="주소록" /></th>
	<th class="head_ct">진행상태</th>
	<th class="head_ct">오류여부</th>
</tr>
<tr id="countArea">
	<td class="body_ct" id="userCnt">${userCnt }</td>
	<td class="body_ct" id="fldCnt">${fldCnt }</td>
	<td class="body_ct" id="adrCnt">${adrCnt }</td>
	<td class="body_ct"><div class="status progress" style="width:100%;" id="progress"><img src="${_ctx}/images/${_skin}/pollbar.png" width="0%" height="10"></div></td>
	<td class="body_ct" id="errYn">${errYn }</td>
</tr>
</u:listArea>

<u:blank />
<u:buttonArea>
<c:if
	test="${sessionScope.userVo.userUid eq 'U0000001'}"><c:if
	test="${!empty param.tranId}">	
<u:button titleId="ap.btn.reTransfer" alt="재이관" onclick="retransfer();" auth="SYS" id="reTransferBtn" style="display:none" />
</c:if><c:if
	test="${empty param.tranId}">	
<u:button id="transferBtn" title="이관시작" alt="이관시작" onclick="processTransfer();" auth="SYS" />
</c:if></c:if>
<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>