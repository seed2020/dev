<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%--
[버튼] 확인 --%>
function confirmOutsourcing(){
	var arr = getIframeContent('listOutsFrm').getCheckedObj();
	setOutsourcingData(arr, '${param.role1Cd}');<%-- setPrj.jsp --%>
	dialog.close('listOutsDialog');
}<%--
[버튼] 등록 --%>
function regOutsourcing(mpId){
	setOutsourcing();
}<%--
[버튼] 수정 --%>
function modOutsourcing(mpId){
	var arr = getIframeContent('listOutsFrm').getCheckedId();
	if(arr==null || arr.length==0){
		alertMsg('cm.msg.noSelect');<%-- cm.msg.noSelect=선택한 항목이 없습니다. --%>
	} else {
		setOutsourcing(arr[0]);
	}
}<%--
[팝업] 등록/수정 --%>
function setOutsourcing(mpId){
	dialog.open("setOutsDialog", '<u:msg titleId="wp.outsourcing" alt="외주 직원" />', "./setOutsPop.do?menuId=${menuId}&cat=${param.cat}&role1Cd=${param.role1Cd}"+(mpId!=null ? ("&mpId="+mpId) : ''));
}<%--
[버튼] 삭제 --%>
function delOutsourcing(){
	var arr = getIframeContent('listOutsFrm').getCheckedId();
	if(arr.length==0){
		alertMsg("cm.msg.noSelect");<%--//cm.msg.noSelect=선택한 항목이 없습니다.--%>
	} else {
		callAjax('./transMpDelAjx.do?menuId=${menuId}&cat=${param.cat}', {mpIds:arr.join(',')}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				getIframeContent('listOutsFrm').reload();
			}
		});
	}
}
//-->
</script>

<div style="width:600px">

<iframe id="listOutsFrm" style="width:100%; height:384px; border:0px;"
	src="./listOutsFrm.do?menuId=${menuId}&cat=${param.cat}&mode=${param.mode}" ></iframe>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:confirmOutsourcing();" alt="확인" /><c:if
		test="${param.mode ne 'single'}">
	<u:button titleId="cm.btn.reg" href="javascript:regOutsourcing();" alt="등록" />
	<u:button titleId="cm.btn.mod" href="javascript:modOutsourcing();" alt="수정" />
	<u:button titleId="cm.btn.del" href="javascript:delOutsourcing();" alt="삭제" /></c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>