<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<div style="width:500px">

<iframe id="readHstArea" name="readHstArea" src="./listReadHstFrm.do?menuId=${menuId }&formNo=${param.formNo}&workNo=${param.workNo }" style="width:100%;height:330px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" />
</u:buttonArea>

</div>
