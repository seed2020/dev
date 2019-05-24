<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:700px">
	<iframe id="listFrm" name="listFrm" src="./listBrdFrm.do?menuId=${menuId }" style="width:100%; height:400px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
	<u:blank />
	<% // 하단 버튼 %>
	<u:buttonArea>
		<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" auth="R" />
	</u:buttonArea>
	
</div>
