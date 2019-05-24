<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
function setLinkedDocConfirm(){
	var obj = getIframeContent('listLinkedDocFrm').getChecked();
	if(obj==null){
		alertMsg('cm.msg.noSelect');<%-- // cm.msg.noSelect=선택한 항목이 없습니다. --%>
		return;
	}
	if(obj.msg==null || confirm(obj.msg)){
		setLinkedDoc(obj.apvNo, obj.docSubj, obj.secuYn);
		dialog.close('openLinkedDocDialog');
	}
}
//-->
</script>

<div style="width:700px">

<iframe id="listLinkedDocFrm" style="width:100%; height:344px; border:0px;"
	src="./listLinkedDocFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&xmlTypId=${param.xmlTypId}<c:if
		test="${not empty param.apvNo}">&apvNo=${param.apvNo}</c:if><c:if
		test="${not empty param.erpLinkedApvNo}">&erpLinkedApvNo=${param.erpLinkedApvNo}</c:if>" ></iframe>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setLinkedDocConfirm();" alt="확인" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>