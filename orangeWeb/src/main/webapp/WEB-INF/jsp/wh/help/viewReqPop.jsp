<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="params"/>
<script type="text/javascript">
<!--
//-->
</script>
<div style="width:800px">
<iframe id="viewReqFrm" name="viewReqFrm" src="./viewReqFrm.do?${params }" style="width:100%; height:650px;" frameborder="0" marginheight="0" marginwidth="0" ></iframe>
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>
