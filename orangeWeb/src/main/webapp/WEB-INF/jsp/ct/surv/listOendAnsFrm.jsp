<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
/*
function applyoendAnsTooltip() {
	$('.ct_survOendAnsInfo').mouseover(function(event){
		
		
		var parentLeft = parent.$("#listOendAnsPop").css("left");
		var subLeft = parentLeft.replace(/[^0-9]/g,'');
		var parentTop = parent.$("#listOendAnsPop").css("top");
		var subTop = parentTop.replace(/[^0-9]/g,'');

		parent.$('#oendAnsTooltip').css('top', (event.pageY + Number(subTop)) - 30 );
		parent.$('#oendAnsTooltip').css('left', event.pageX + Number(subLeft) - 250);
		
		parent.$('#oendAnsTooltip').show();
		
		var $ctObj = $("#"+$(this).attr("id")).parent().parent();
		var $oendAnsTooltip_title=parent.$("#oendAnsTooltip_title");
		
		$oendAnsTooltip_title.html($ctObj.find("#quesReplyNm").val());
		
	});
	
	$('.ct_survOendAnsInfo').mouseout(function(event){
		parent.$('#oendAnsTooltip').hide();     
	});
}
*/
$(document).ready(function() {
	
	setUniformCSS();
//	applyoendAnsTooltip();
});

-->
</script>



<div style="width:580px; padding:0px 0px 0px 3px" >
	<form id="listDeptStatForm" action="./listOendAnsFrm.do">

		<% // 질문 %>
		<div class="titlearea">
			<div class="tit_left" >
			<dl>
				<dd class="title_s">
					<div class="ellipsis" title="<u:msg titleId="cols.ques" alt="질문" />${quesCount} ) ${ctsQueVo.quesCont}">
						<input type="hidden" id="quesReplyNm" name="quesReplyNm" value="<u:msg titleId="cols.ques" alt="질문" />${quesCount} ) ${ctsQueVo.quesCont}"/>
						<u:msg titleId="cols.ques" alt="질문" /><u:out value="${quesCount} ) ${ctsQueVo.quesCont}"  maxLength="65" />
					</div>
				</dd>
			</dl>
			
			</div>
		</div>
		
		
		<% // 목록 %>
		<div class="listarea">
			<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
				<tr>
				<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
				<td class="head_ct"><u:msg titleId="cols.cont" alt="내용" /></td>
				</tr>
			
				<c:forEach  var="ctsOend" items="${ctSurvBMapList}"  varStatus="status">
					<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_ct"><u:out value="${ctsOend.rnum}"/></td>
					<td class="body_lt">
						<div class="ellipsis" title="${ctsOend.oendReplyCont}">
							<u:out value="${ctsOend.oendReplyCont}"/>
						</div>
					</td>
				</tr>
				
				</c:forEach>
				<c:if test="${fn:length(ctSurvBMapList) == 0}">
					<tr>
					<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
					</tr>
				</c:if>
			</table>
		</div>
	</form>
<u:blank />
<u:pagination noTotalCount="true" />

</div>

