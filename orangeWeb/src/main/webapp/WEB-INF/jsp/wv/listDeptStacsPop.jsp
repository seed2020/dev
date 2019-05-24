<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
/*
function applyDeptStacsTooltip() {
	$('.wv_survDeptStacsInfo').mouseover(function(event){
		
		parent.$('#deptStacsTooltip').css('top', Number(event.pageY) - 88 );
		parent.$('#deptStacsTooltip').css('left', parent.event.pageX - 265);
		

		parent.$('#deptStacsTooltip').show();
		
		parent.$(".tooltip_line").hide();
		parent.$("#deptStacsTooltip_basicInfo").hide();
		parent.$("#deptStacsTooltip_ctItro").hide();
	
		var $ctObj = $("#"+$(this).attr("id")).parent().parent();
		var $deptStacsTooltip_title=parent.$("#deptStacsTooltip_title");
		
		$deptStacsTooltip_title.html($ctObj.find("#quesReplyNm").val());
		
		var $deptStacsTooltip_basicInfo=parent.$("#deptStacsTooltip_basicInfo");
		$deptStacsTooltip_basicInfo.html("");
		
	});
	
	$('.wv_survDeptStacsInfo').mouseout(function(event){
		parent.$('#deptStacsTooltip').hide();     
	});
	
	$('.wv_survDeptStacsCols').mouseover(function(event){
		
		
		parent.$('#deptStacsTooltip').css('top', Number(event.pageY) - 88 );
		parent.$('#deptStacsTooltip').css('left', parent.event.pageX - 265);
		

		parent.$(".tooltip_line").show();
		parent.$("#deptStacsTooltip_basicInfo").show();
		parent.$("#deptStacsTooltip_ctItro").show();
		
		parent.$('#deptStacsTooltip').show();
		var $ctObj = $("#"+$(this).attr("id"));
		
		
		
		var examTitle = "<u:msg titleId="cols.exam" alt="보기" />" + $ctObj.find("#examDispNm").attr("name");
		
		var $deptStacsTooltip_basicInfo=parent.$("#deptStacsTooltip_basicInfo");
		$deptStacsTooltip_basicInfo.html(examTitle + ")");
		
		
		var $deptStacsTooltip_ctItro=parent.$("#deptStacsTooltip_ctItro");
		$deptStacsTooltip_ctItro.html($ctObj.find("#examDispNm").val());
		
		//title 질문 제목
		var $ctHObj = $("#"+$(this).attr("id")).parent().parent().parent().parent().parent().parent().parent().parent().parent().parent().parent().parent();
		var $deptStacsTooltip_title=parent.$("#deptStacsTooltip_title");
		
		$deptStacsTooltip_title.html($ctHObj.find("#quesReplyNm").val());
		
	});
	
	$('.wv_survDeptStacsCols').mouseout(function(event){
		parent.$('#deptStacsTooltip').hide();     
	});
}
*/
$(document).ready(function() {
	
	setUniformCSS();
	//applyDeptStacsTooltip();
});
//-->
</script>


<div style="width:600px">
<form id="listDeptStacsForm">

<% // 질문 %>
<div class="titlearea">
	<div class="tit_left">
		<dl>
			<dd class="title_s">
				<div class="ellipsis" title="<u:msg titleId="cols.ques" alt="질문" />${quesCount} ) ${wvsQueVo.quesCont}">
					<input type="hidden" id="quesReplyNm" name="quesReplyNm" value="<u:msg titleId="cols.ques" alt="질문" />${quesCount} ) ${wvsQueVo.quesCont}"/>
					<u:msg titleId="cols.ques" alt="질문" /><u:out value="${quesCount} ) ${wvsQueVo.quesCont}"  maxLength="65" />
				</div>
			</dd>
		</dl>

	</div>
</div>

<% // 목록 %>
<u:listArea id="listArea">
	<tr>
		<td class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
		<c:forEach  var="wvsQue" items="${wcQueExamList}"  varStatus="status">
			<td class="head_ct">
				<span class="wv_survDeptStacsCols" id="wv_survDeptStacsCols${status.count}" title="${status.count}.${wvsQue.examDispNm}">
					<!-- <u:out value="${wvsQue.examDispNm}" maxLength="9"/> -->
					<u:msg titleId="cols.exam" alt="보기" />${status.count}  
					<input type="hidden" id="examDispNm" name="${status.count}" value="${wvsQue.examDispNm}" />
				</span>
			</td>
		</c:forEach>
		<td width="20%" class="head_ct"><u:msg titleId="wv.cols.sum" alt="합계" /></td>
	</tr>
	<c:forEach  var="wvsDept" items="${wvSurvReplyDeptList}"  varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<td class="body_ct">${wvsDept.replyrDeptNm}</td>
		<c:forEach  var="wvsQue" items="${wvsDept.quesExam}"  varStatus="status">
			<td class="body_ct">${wvsQue.selectCount}</td>
			
		</c:forEach>
			<td class="body_ct">${wvsDept.deptTotalCount}</td>
		
	
	</tr>
	
	</c:forEach>
	<c:if test="${fn:length(wcQueExamList) == 0}">
		<tr>
		<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea topBlank="true">
	<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" auth="R" />
</u:buttonArea>



</form>
</div>
<u:blank />
