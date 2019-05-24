<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function sendPsd(){
	var $selectFncUid = $("#pdsList").val();
	var $selectCtId = $("#ctList").val();
	if($selectFncUid == null || $selectCtId == null){
		//ct.msg.selectCtPdsPop.notSelectedCtBoard = 커뮤니티 게시판을 선택해주시기 바랍니다.
		alertMsg("ct.msg.selectCtPdsPop.notSelectedCtBoard");
	}else{
		if (confirmMsg("ct.cfrm.send")) {
			callAjax('../sendPsd.do?menuId=${menuId}', {ctId:'${ctId}', bullId:'${bullId}', ctFncUid:$selectFncUid, selectCtId:$selectCtId}, function(data){
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					if (confirmMsg("ct.cfrm.move")){
						location.href = './listBoard.do?menuId='+$selectFncUid+'&ctId='+$selectCtId;
					}else{
						location.href = './viewBoard.do?menuId=${menuId}&ctId=${ctId}&bullId=${bullId}';
					}
					
				}
			});
		}
	}
	
}

function ctSelected(ctId){
	var $selectCtId = $("#selectedCtId").val(ctId);
	dialog.open('selectCtPds','커뮤니티 게시판','../selectCtPdsPop.do?menuId=${menuId}&ctId=${ctId}&bullId=${bullId}&selectCtId='+ctId);
}

$(document).ready(function() {
	setUniformCSS();
	
});
//-->
</script>
<div id="selectCtBoardPopDiv" style="width:700px">
	<u:title titleId="ct.jsp.selectOtherCtBoardPop.title" alt="타 커뮤니티 게시판 선택" />
	
	<u:boxArea className="gbox" outerStyle="height:300px;padding:9px 12px 0 10px;" innerStyle="NO_INNER_IDV">
	
	<!-- LEFT -->
	<div class="left" style="width:49%; height: 250px;">
	<input id="selectedCtId" name="selectedCtId" type="hidden" value=""/>
	<input id="ctId" name="ctId" type="hidden" value="${ctId}"/>
	<input id="bullId" name="bullId" type="hidden" value="${bullId}"/>
	
	<u:title titleId="ct.jsp.selectCtPdsPop.subTitle01" type="small" alt="타 커뮤니티" />
		<select id="ctList" size="13" style="width:100%; height:187px;" onchange="ctSelected(this.value)">
			<c:forEach  var="otherEstbVo" items="${otherEstbList}" varStatus="status">
				<c:if test="${otherEstbVo.ctId == selectCtId}">
					<option value="${otherEstbVo.ctId}" selected> ${otherEstbVo.ctNm}</option>
				</c:if>
				<c:if test="${otherEstbVo.ctId != selectCtId}">
					<option value="${otherEstbVo.ctId}"> ${otherEstbVo.ctNm}</option>
				</c:if>
			</c:forEach>
		</select>
	</div>
	
	<!-- RIGHT -->
	<div class="right" style="width:49.8%; height: 250px;">
	
	<u:title titleId="ct.jsp.selectCtPdsPop.subTitle02" type="small" alt="자료실" />
		<select id="pdsList" size="13" style="width:100%; height:187px;">
		<c:forEach  var="writeFncVo" items="${writeFncList}" varStatus="status">
			<option value="${writeFncVo.ctFncUid}"> ${writeFncVo.ctFncNm}</option>
		</c:forEach>
		</select>
	</div>
	
	<u:buttonArea>
		<u:button titleId="cm.btn.confirm" alt="확인" href="javascript:sendPsd();" />
		<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
	</u:buttonArea>
	
	</u:boxArea>
</div>