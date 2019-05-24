<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="params"/>
<script type="text/javascript">
<!--<c:if test="${!empty isFind && isFind eq 'Y' }"><%
// 체크된 목록 %>
var gRelReqs = [];<%
// 페이지 이동할때 체크된 목록 추가/제거 %>
function setRefApvChecks(checks, notChecks){
	var curRelReqs=[], reqNos=[];<%
	// 체크 안된 목록 제거%>
	gRelReqs.each(function(index, refReq){
		if(!notChecks.contains(refReq.reqNo)){
			curRelReqs.push(refReq);
			reqNos.push(refReq.reqNo);
		}
	});<%
	// 체크된 목록 추가 %>
	checks.each(function(index, newRefReq){
		if(!reqNos.contains(newRefReq.reqNo)){
			curRelReqs.push(newRefReq);
			reqNos.push(newRefReq.reqNo);
		}
	});
	gRelReqs = curRelReqs;
}<%
// 확인 버튼 클릭 - 관련요청 팝업의 저장 영역에 해당 데이터를 넣음 %>
function applyReqList(){
	getIframeContent('listReqListFrm').onPageChange();
	setReqToRelReqPop(gRelReqs);
}
</c:if>
$(document).ready(function() {
	<c:if test="${!empty isFind && isFind eq 'Y' }">
	<%
	// 첨부에 있는 참조 문서를 전역변수 gRelReqs 에 세팅함 %>
	gRelReqs = collectReqToRelReq();
	</c:if>
});
//-->
</script>
<u:set var="width" test="${!empty isFind && isFind eq 'Y' }" value="750" elseValue="850"/>
<div style="width:${width}px;height:490px;">
<iframe id="listReqListFrm" name="listReqListFrm" src="./listReqListFrm.do?${params }&isFind=${isFind}" style="width:100%; height:440px;" frameborder="0" marginheight="0" marginwidth="0" ></iframe>
<u:blank />
<u:buttonArea>
	<c:if test="${!empty isFind && isFind eq 'Y' }"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applyReqList();" /></c:if>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>