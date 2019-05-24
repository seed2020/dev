<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--


// 게시물 보내기 
function sendBull(){
	var $selectOp = $("#quesArea").find("input:radio:checked").val();
	
	if($selectOp == '1'){
		dialog.open('selectCtPds','다른 커뮤니티 게시판','../selectCtPdsPop.do?menuId=${menuId}&ctId=${ctId}&bullId=${bullId}');
	}else if($selectOp == '2'){
		sendEmail('${bullId}');
	}else{
		dialog.open('selectCtBoard','커뮤니티 게시판','../selectCtBoardPop.do?menuId=${menuId}&ctId=${ctId}&bullId=${bullId}');
	}
}

$(document).ready(function() {
	setUniformCSS();
});

//-->
</script>

<div style="width:400px">

<div id="quesArea">
	<u:radio name="cmd" value="1" titleId="ct.option.sendToCm" alt="다른 커뮤니티의 게시판으로" inputClass="bodybg_lt" checked="true" /><br />
	<c:if test="${mailEnable == 'Y' }">
		<u:radio name="cmd" value="2" titleId="ct.option.sendToEmail" alt="편지작성으로" inputClass="bodybg_lt" /><br />
	</c:if>
	<u:radio name="cmd" value="3" titleId="ct.option.sendToBb" alt="현재 커뮤니티의 다른 게시판으로" inputClass="bodybg_lt" />
	<input id="selectOp" name="selectOp" type="hidden" value="" />
</div>
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:sendBull()" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</div>
