<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<div style="width:600px">

<iframe id="listMulcAnsFrm" name="listMulcAnsFrm" src="/ct/surv/listMulcAnsFrm.do?menuId=${menuId}&survId=${survId}&quesId=${quesId}&replyNo=${replyNo}" style="width:100%; height:420px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" auth="R" />
</u:buttonArea>

</div>