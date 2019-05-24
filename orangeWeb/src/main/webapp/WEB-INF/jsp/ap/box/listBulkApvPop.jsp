<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.net.URLEncoder"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%
// 일괄결재 화면구성 %>
function viewBulkApv(){<%
	
	// 화면 구성 %>
	var $hiddenTr = $("#bulkApvListArea #hiddenTr"), $cloneTr, $check;<%
	
	// gBulkApvData : 일괄결재 데이터 listApvBx.jsp 의 openBulkApv() 함수에서 세팅함
	%>
	gBulkApvData.checks.each(function(index, check){
		$check = $(check);
		$cloneTr = $hiddenTr.clone();
		$cloneTr.attr("id", "TR"+$check.val());
		$cloneTr.find("td:eq(0) div").attr("title", $check.attr("data-docSubj")).text($check.attr("data-docSubj"));
		$cloneTr.find("td:eq(1)").text($check.attr("data-docStatNm"));
		$cloneTr.insertBefore($hiddenTr);
		$cloneTr.show();
	});<%
	
	// 일관결재 진행 - 현재의 javascript 가 완료 되어야 팝업창이 보이게 되므로 timeout 처리함
	%>
	window.setTimeout('processBulkApv()', 20);
}<%
// 일괄결재 진행 %>
function processBulkApv(){
	var checks = gBulkApvData.checks, $check, msgs=[], successCnt=0, secuId = gBulkApvData.secuId;
	var $area = $("#bulkApvListArea");
	checks.each(function(index, check){
		if(gBulkApvData==null) return false;
		$check = $(check);
		var data = {process:"processBulkApv", apvNo:$check.val(), apvLnPno:$check.attr("data-apvLnPno"), apvLnNo:$check.attr("data-apvLnNo"), secuId:secuId};
		callAjax("./transDocProcessAjx.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}", data, function(data){
			if(data.message != null){
				msgs.push(data.message+" ("+$check.attr("data-docSubj")+")");
			}
			if(data.statNm != null) $area.find("#TR"+$check.val()+" td:eq(2)").text(data.statNm);
			secuId = data.secuId;
			if(data.result=='ok') successCnt++;
		});
	});
	gBulkApvData = null;<%
	// ap.msg.bulkApvRslt={0}건의 문서를 일괄결재 하였습니다. %>
	msgs.push(callMsg('ap.msg.bulkApvRslt',[successCnt+'']));
	alert(msgs.join('\n'));
	removeApCachedCountMap();
	location.replace(location.href);
}
<%
//onload 시 - 호출창(대기함 목록)에서 선택된 목록 중 일괄 결재 가능한 목록을 구성하고 하나씩 결재 요청함 %>
$(document).ready(function() {
	viewBulkApv();
});
//-->
</script>

<div style="width:500px">

<u:listArea id="bulkApvListArea" style="max-height:400px; overflow-y:auto;">

	<tr id="titleTr">
		<td width="55%" class="head_ct"><u:msg titleId="ap.doc.docSubj" alt="제목" /></td>
		<td width="25%" class="head_ct"><u:msg titleId="ap.list.docStatNm" alt="문서상태" /></td>
		<td width="20%" class="head_ct"><u:term termId="ap.jsp.procStat" alt="처리상태" /></td>
	</tr>
	<tr id="hiddenTr" onmouseover="this.className='trover'" onmouseout="this.className='trout'" style="display:none;">
		<td class="body_lt"><div class="ellipsis"></div></td>
		<td class="body_ct"></td>
		<td class="body_ct"></td>
	</tr>

</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>