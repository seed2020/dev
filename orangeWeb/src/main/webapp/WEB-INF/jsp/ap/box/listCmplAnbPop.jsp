<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
function setCmplAnbConfirm(){
	var apvNo = getIframeContent('listCmplAnbFrm').getChecked();
	if(apvNo==null){
		alertMsg('cm.msg.noSelect');<%-- // cm.msg.noSelect=선택한 항목이 없습니다. --%>
	} else {
		setCmplAnbApvNo(apvNo);
		dialog.close('openCmplAnbDialog');
	}
}
//-->
</script>

<div style="width:600px">

<iframe id="listCmplAnbFrm" style="width:100%; height:344px; border:0px;"
	src="./listCmplAnbFrm.do?menuId=${menuId}&bxId=${param.bxId}${strMnuParam}&xmlTypId=${param.xmlTypId}&startAttr=${param.startAttr}&endAttr=${param.endAttr}" ></iframe>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" href="javascript:setCmplAnbConfirm();" alt="확인" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>