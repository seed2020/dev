<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
//자원상세보기 팝업
function viewRescInfoPop(rescMngId){
	dialog.open('setRescPop', '<u:msg titleId="wb.jsp.viewRescMngPop.title" alt="자원상세보기" />', './viewRescPop.do?menuId=${menuId}&rescMngId='+rescMngId);
};
//-->
</script>
<div style="width:700px">
	
	<iframe id="bcListFrm" name="listRezvStatFrm" src="./listRezvStatFrm.do?${params }" style="width:100%; height:400px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

	<% // 하단 버튼 %>
	<u:buttonArea>
	<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" auth="R" />
	</u:buttonArea>

</div>
