<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<div style="width:500px">
<form id="listReadHstPopForm">

<%-- <div class="front">
<div class="front_left">
	<table border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td class="color_stxt">※ <u:msg titleId="bb.jsp.listReadHstPop.tx01" alt="조회자는 최근 50명까지만 표시됩니다." /></td>
	</tr>
	</tbody></table>
</div>
</div> --%>

<iframe id="bbTabArea" name="bbTabArea" src="./listReadHstFrm.do?menuId=${menuId }&ctId=${param.ctId}&bullId=${param.bullId }" style="width:100%;height:330px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" />
</u:buttonArea>

</form>
</div>
