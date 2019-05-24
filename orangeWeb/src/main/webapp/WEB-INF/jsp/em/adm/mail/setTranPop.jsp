<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<style>
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
			dialog.onClose("setTranDialog", function(){ reload(); });
		}
	});
}
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
</tr>
<tr id="countArea">
	<td class="body_ct" id="userCnt">${userCnt }</td>
	<td class="body_ct" id="groupCnt">${groupCnt }</td>
	<td class="body_ct" id="personCnt">${personCnt }</td>
	<td class="body_ct"><div class="status progress" style="width:90%;"><img src="${_ctx}/images/${_skin}/pollbar.png" width="0%" height="10"></div></td>
</tr>
</u:listArea>

<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
<c:if
	test="${sessionScope.userVo.userUid eq 'U0000001'}">
<u:button id="transferBtn" title="이관시작" alt="이관시작" onclick="processTransfer();" auth="SYS" />
</c:if>
<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>
