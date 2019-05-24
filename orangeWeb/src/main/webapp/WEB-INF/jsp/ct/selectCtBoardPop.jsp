<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function sendPsd(){
	var $selectCtFncUid = $("#boardList").val();
	if($selectCtFncUid == null){
		alert("커뮤니티 게시판을 선택해주시기 바랍니다.");
	}else{
		if (confirmMsg("ct.cfrm.send")) {
			callAjax('../sendSetBull.do?menuId=${menuId}', {ctId:'${ctId}', bullId:'${bullId}', selectCtFncUid:$selectCtFncUid}, function(data){
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.href = './setBoard.do?menuId='+$selectCtFncUid+'&ctId=${ctId}&selectBullId=${bullId}';
				}
			});
		}
	}
	
}

$(document).ready(function() {
	setUniformCSS();
	
});
//-->
</script>
<div id="selectCtBoardPopDiv" style="width:700px">
	<u:title titleId="ct.jsp.selectCtBoardPop.title" alt="커뮤니티 게시판 선택" />
	
	<u:boxArea className="gbox" outerStyle="height:300px;padding:9px 12px 0 10px;" innerStyle="NO_INNER_IDV">
	
	<!-- LEFT -->
	<div class="left" style="width:49%; height: 250px;">
	<input id="selectFncUid" name="selectFncUid" type="hidden" value=""/>
	<input id="ctId" name="ctId" type="hidden" value="${ctId}"/>
	<input id="bullId" name="bullId" type="hidden" value="${bullId}"/>
	
	<u:title titleId="ct.jsp.selectCtBoardPop.subTitle01" type="small" alt="현재 커뮤니티" />
		<select id="ctList" size="13" style="width:100%; height:187px;" >
			<option value="${ctEstbBVo.ctId}" selected> ${ctEstbBVo.ctNm}</option>
		</select>
	</div>
	
	<!-- RIGHT -->
	<div class="right" style="width:49.8%; height: 250px;">
	
	<u:title titleId="ct.jsp.selectCtBoardPop.subTitle02" type="small" alt="게시판" />
		<select id="boardList" size="13" style="width:100%; height:187px;">
		<c:forEach  var="writeFncVo" items="${writeFncList}" varStatus="status">
			<option value="${writeFncVo.ctFncUid}"> ${writeFncVo.ctFncNm}</option>
		</c:forEach>
		</select>
	</div>
	
	<u:buttonArea>
		<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:sendPsd()" />
		<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
	</u:buttonArea>
	
	</u:boxArea>
</div>