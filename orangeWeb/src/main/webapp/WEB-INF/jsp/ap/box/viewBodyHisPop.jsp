<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<%
// 본문 높이기 넘칠때 세로 스크롤이 가려져서 본문의 넓이를 줄임 %>
function adjustBodyHisArea(){
	var editArea = $("#bodyHisArea .editor");
	var scrollH = parseInt(editArea.prop("scrollHeight"));
	var maxH = parseInt(editArea.css("max-height"));
	if(scrollH>maxH){
		editArea.width(editArea.width() - 10);
	}
}
$(document).ready(function() {
	window.setTimeout('adjustBodyHisArea()', 20);
});
//-->
</script>
<div style="width:800px">

<u:titleArea id="bodyHisArea" outerStyle="width:798px; overflow-x:hidden;" innerStyle="NO_INNER_IDV" >
<div class="editor" style="color:#454545; width:98.8%; max-height:600px; overflow-x:auto; overflow-y:auto;">${apOngdBodyLVo.bodyHtml}</div>
</u:titleArea>

<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>