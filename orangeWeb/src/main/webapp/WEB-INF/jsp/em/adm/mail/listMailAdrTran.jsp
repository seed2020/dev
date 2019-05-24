<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<%// [하단버튼] - 메일 주소록 이관%>
function setTranPop(){
	var url='./setTranPop.do?menuId=${menuId}';
	dialog.open('setTranDialog','메일주소록 이관', url);
}<% // [하단버튼:삭제] 삭제 %>
function delTran() {
	var tranIds = getCheckedValue("listArea", "cm.msg.noSelect");<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	if (tranIds != null && confirmMsg("cm.cfrm.del")) {<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transMailAdrTranDelAjx.do?menuId=${menuId}', {tranIds:tranIds}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
}<%
// 이관 진행 내역 조회용 Timeout %>
var gTranProgressTimeout = null;<%
// 이관 진행 내역 팝업 - 진행상태 클릭 하면 %>
function openTranProcPop(tranId){
	if(tranId==null || tranId=='') return;
	
	dialog.open('listTranProcDialog','<u:msg titleId="ap.jsp.tran.process" alt="이관 진행 내역" />','./listTranProcPop.do?menuId=${menuId}&tranId='+tranId);
	dialog.onClose('listTranProcDialog', function(){
		if(gTranProgressTimeout != null){
			window.clearTimeout(gTranProgressTimeout);
			gTranProgressTimeout = null;
		}
	});
}
<% // [버튼클릭] - 사용자추가  %>
function setMailAdrUserPop(){
	var data=[];
	searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
		if(arr!=null){
			var userUidList=[];
			arr.each(function(index, userVo){
				userUidList.push(userVo.userUid);
			});
			if(userUidList.length>0){
				listTranProcPop(null, userUidList);
			}
		}
	});
};<%// [하단버튼] - 메일 주소록 이관%>
function listTranProcPop(tranId, userUidList){
	var url='./listTranProcPop.do?menuId=${menuId}';
	if(tranId!=null) url+='&tranId='+tranId;
	if(userUidList!=null) url+='&userUidList='+userUidList;
	dialog.open('listTranProcDialog','메일주소록 이관', url);
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<u:title menuNameFirst="true" />
	
<u:listArea id="listArea" colgroup="3%,,12%,12%,20%,20%,10%">
	<tr id="headerTr">
		<td class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/></td>
		<th class="head_ct">이관ID</th>
		<th class="head_ct">이관시작일시</th>
		<th class="head_ct">이관종료일시</th>
		<th class="head_ct">진행상태</th>
	</tr>
	<c:if test="${fn:length(emMailAdrTranBVoList) == 0}">
		<tr>
		<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(emMailAdrTranBVoList)>0}">
		<c:forEach items="${emMailAdrTranBVoList}" var="emMailAdrTranBVo" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<td class="bodybg_ct"><input type="checkbox" value="${emMailAdrTranBVo.tranId }" /></td>
				<td class="bodybg_ct"><a href="javascript:openTranProcPop('${emMailAdrTranBVo.tranId}');">${emMailAdrTranBVo.tranId }</a></td>
				<td class="bodybg_ct"><u:out value="${emMailAdrTranBVo.tranStrtDt }" type="date"/></td>
				<td class="bodybg_ct"><u:out value="${emMailAdrTranBVo.tranEndDt }" type="date"/></td>
				<td class="bodybg_ct"><c:if test="${emMailAdrTranBVo.tranProcStatCd eq 'completed'}">완료</c:if
				><c:if test="${emMailAdrTranBVo.tranProcStatCd eq 'processing'}">진행중</c:if
				><c:if test="${emMailAdrTranBVo.tranProcStatCd eq 'error'}">오류</c:if></td>
			</tr>
		</c:forEach>	
	</c:if>
	
</u:listArea>
<u:pagination />
<c:if
	test="${sessionScope.userVo.userUid eq 'U0000001'}">
<% // 하단 버튼 %>
<u:buttonArea>
<u:button title="메일주소록 이관" alt="등록" href="javascript:setTranPop();" auth="SYS" />
<u:button titleId="cm.btn.del" alt="삭제" onclick="delTran();" auth="SYS" />
</u:buttonArea>
</c:if>
