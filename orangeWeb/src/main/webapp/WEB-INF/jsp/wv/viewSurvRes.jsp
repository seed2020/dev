<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
//이미지 미리보기 팝업
function viewSurvImagePop(survId,quesId,examNo){
	$('#viewSurvImagePop').remove();
	var url = './viewImagePop.do?menuId=${menuId}&survId='+survId+'&quesId='+quesId;
	if(examNo != undefined){
		url+='&examNo='+examNo;
	}
	dialog.open('viewSurvImagePop', '<u:msg titleId="or.jsp.setOrg.viewImageTitle" alt="이미지 보기" />', url);
};

function viewSurv(survId){
	location.href = './viewSurv.do?menuId=${menuId}&survId=' + survId;
	
}
/*
function applyTooltip() {
	$('.wv_survInfo').mouseover(function(event){
		$('#tooltip').css('top', event.pageY - 85);
		$('#tooltip').css('left', event.pageX - 230);
		$('#tooltip').show();
		
		var $ctObj = $("#"+$(this).attr("id")).parent().parent();
		var $tooltip_title=$("#tooltip_title");
		
		$tooltip_title.html($ctObj.find("#examDispNo").val());
		var $tooltip_basicInfo = $("#tooltip_basicInfo");
		$tooltip_basicInfo.html($ctObj.find("#examDispNm").val());
		
	});
	
	$('.wv_survInfo').mouseout(function(event){
		$('#tooltip').hide();     
	});
	
	$('.wv_Cls_subj').mouseover(function(event){
		$('#tooltip').css('top', event.pageY - 85);
		$('#tooltip').css('left', event.pageX - 230);
		$('#tooltip').show();
		
		var $ctObj = $("#"+$(this).attr("id")).parent().parent();
		var $tooltip_title=$("#tooltip_title");
		
		$tooltip_title.html("제목");
		var $tooltip_basicInfo = $("#tooltip_basicInfo");
		$tooltip_basicInfo.html($ctObj.find("#wv_subj").val());
		
	});
	
	$('.wv_Cls_subj').mouseout(function(event){
		$('#tooltip').hide();     
	});
}
*/

$(document).ready(function() {

	setUniformCSS();
//	applyTooltip();
});
//-->
</script>

<u:title titleId="wv.jsp.viewServRes.title" alt="설문결과" menuNameFirst="true"/>

<% // 폼 필드 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="16%"/><col width="52%"/><col width="16%"/><col width="*"/></colgroup>
		<tr>
		<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td class="body_lt">
			<div class="ellipsis" title="${wvsVo.survSubj}">
				<span class="wv_Cls_subj" id="wc_Cls_subj" >
				 	<u:out value="${wvsVo.survSubj}" />
				 	<input type="hidden" id="wv_subj" name="wv_subj" value="${wvsVo.survSubj}" />
				</span>
			</div>
		</td>
		<td class="head_lt"><u:msg titleId="wv.cols.survRegr" alt="설문게시자" /></td>
		<td class="body_lt"><a href="javascript:viewUserPop('${wvsVo.regrUid}');">${wvsVo.regrNm}</a></td>
		</tr>
	
		<tr>
		<td class="head_lt" style="width:5%">
			<u:msg titleId="cols.itnt" alt="취지"  />
		</td>
		<td colspan="3" class="body_lt">
			<div style="overflow:auto;" class="editor">${wvsVo.survItnt}</div>
		</td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
			<td colspan="3">
				<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="wv" mode="view" /></c:if>
			</td>
		</tr>
	</table>
</div>
<u:blank />

<u:blank />

<u:listArea>
	<c:forEach  var="wvsQue" items="${wvsQueList}"  varStatus="status">
		<c:choose>

			
			<c:when test="${wvsQue.mulChoiYn == 'Y'}"> 
				<u:msg var="wvsQue_multi"	alt="여러개 선택" titleId="wv.cols.set.multiSel"/>
			</c:when>
			<c:when test="${wvsQue.mulChoiYn == 'N'}"> 
				<u:msg var="wvsQue_multi"	alt="한개 선택" titleId="wv.cols.set.onlySel"/>
			</c:when>
			<c:otherwise>
				<u:msg var="wvsQue_multi"	alt="주관식 질문" titleId="wv.cols.set.ans"/>
				<c:set var="wvsQue_oendCount"	value= "${wvsQue.oendCount}" />
				
			</c:otherwise>
			
	</c:choose>
	<tr>
	<td><table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
		<tr class="head_rd">
		<td colspan="12" class="height3"></td>
		</tr>

		<tr class="head_rd">
			<td class="head_lt" colspan="11">
					<div style="word-break:break-all; word-wrap:break-word;">
						<strong><u:msg titleId="cols.ques" alt="질문" /><u:out value="${status.count} ) ${wvsQue.quesCont}" /></strong>
						<br><span style="padding: 0 0 0 50px;"  >[${wvsQue_multi}]</span>
				 		<c:if test="${empty wvsQue.mulChoiYn}">
				 				${wvsQue_oendCount}
				 		</c:if>
			 	</div>
			 </td>
			 
			<c:if test="${!empty wvsQue.mulChoiYn}">
				<u:msg titleId="wv.cols.set.deptStat" alt="부서별통계" var="deptStat"/>
				<td class="listicon_rt"><u:buttonS href="javascript:dialog.open('listDeptStacsPop','${deptStat }','./listDeptStacsPop.do?menuId=${menuId}&survId=${wvsQue.survId}&quesId=${wvsQue.quesId}&quesCount=${status.count}');" titleId="wv.btn.deptStacs" alt="부서별통계" /></td>
			</c:if>
			<c:if test="${empty wvsQue.mulChoiYn}">
				<u:msg titleId="wv.cols.set.listAn" alt="주관식 답변 리스트" var="listAn"/>
				<td class="listicon_rt"><u:buttonS href="javascript:dialog.open('listOendAnsPop','${listAn }','./listOendAnsPop.do?menuId=${menuId}&&survId=${wvsQue.survId}&quesId=${wvsQue.quesId}&quesCount=${status.count}');" titleId="wv.btn.resView" alt="결과보기" /></td>
			</c:if>
		</tr>
		<c:if test="${!empty wvsQue.imgSavePath}">
			<tr class="head_rd">
				<td colspan="12" class="body_lt"><label for="${wvsQue.quesSortOrdr}">
						<c:forEach begin="1" end="12">&nbsp;</c:forEach>
						<a href="javascript:viewSurvImagePop('${wvsQue.survId}','${wvsQue.quesId }');"><img src="${_ctx}${wvsQue.imgSavePath}" width="110" height="125"/></a>
					</label>
				</td>
			</tr>
		</c:if>

		<tr class="head_rd">
		<td colspan="12" class="height5"></td>
		</tr>
		</table></td>
	</tr>
	
	<tr>
		<td>
			<c:forEach  var="wvsQueExam" items="${returnWcQueExamList}"  varStatus="queExStatus">
				<c:if test="${wvsQueExam.quesId == wvsQue.quesId }">
		
					<div style="padding:5px 10px 5px 10px;">
						<table border="0" cellpadding="0" cellspacing="0" style="width:50%; table-layout:fixed;">
							<tr>
								<td width="300" class="body_lt">
									<div class="ellipsis" title="${wvsQueExam.examOrdr}.${wvsQueExam.examDispNm}">
										<span class="wv_survInfo" id="wv_survInfo${queExStatus.count}">
											<u:out value="${wvsQueExam.examOrdr}.${wvsQueExam.examDispNm}" />
											<input type="hidden" id="examDispNo" id="examDispNo" value="<u:msg titleId="cols.ques" alt="질문" />${status.count}) ${wvsQue.quesCont}" />
											<input type="hidden" id="examDispNm" id="examDispNm" value="${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}" />
										</span>
									</div>
								</td>
								<td width="100" class="body_lt" rowspan="2">${wvsQueExam.selectCount}<u:msg titleId="wv.cols.set.menSel" alt="명 선택"  /></td>
								<td width="30" class="body_lt" rowspan="2">${wvsQueExam.quesAverage}%</td>
								<td width="200" rowspan="2"><div style="width:99%;height:10px;border:1px solid #bfc8d2;background-color:#ffffff;"><img src="${_ctx}/images/${_skin}/pollbar.png" width="${wvsQueExam.quesAverage}%" height="10"></div></td>
								<td width="80" class="body_ct" rowspan="2"> 
									<c:if test="${wvsQueExam.inputYn == 'Y' }">
										<c:if test="${wvsQueExam.selectCount != 0}">
											<u:msg titleId="wv.cols.set.reContOp" alt="객관식 입력 항목 답변 내용" var="reContOp"/>
											<u:buttonS href="javascript:dialog.open('listMulcAnsPop','${reContOp }','./listMulcAnsPop.do?menuId=${menuId}&survId=${wvsQueExam.survId}&quesId=${wvsQueExam.quesId}&replyNo=${wvsQueExam.examNo}');" titleId="wv.btn.resView" alt="결과보기" />
										</c:if>
									</c:if>
								</td>
							</tr>
							<c:if test="${!empty wvsQueExam.imgSavePath}">
									<td colspan="2" class="body_lt"><label for="${wvsQueExam.examOrdr}">
										<c:forEach begin="1" end="3">&nbsp;</c:forEach>
										<a href="javascript:viewSurvImagePop('${wvsQueExam.survId}','${wvsQueExam.quesId }','${wvsQueExam.examNo }');"><img src="${_ctx}${wvsQueExam.imgSavePath}" width="110" height="125" /></a>
										</label>
									</td>
								</c:if>
						</table>
					</div>
					
					<c:if test="${not queExStatus.last }">
						<div class="dotline"></div>
					</c:if>
					
	
		
				</c:if>
			</c:forEach>
		</td>
	</tr>
	</c:forEach>

</u:listArea>

<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<c:if test="${repetYn == 'Y'}">
	<u:button titleId="wv.btn.repeatSurv" alt="재설문" href="javascript:viewSurv('${wvsVo.survId}');" />
	</c:if>
	<u:button titleId="cm.btn.list" alt="목록" href="javascript:history.go(-1);" />
</u:buttonArea>

<% // TOOLTIP %>
<div id="tooltip" style="position:absolute; top:315px; left:90px; z-index:1; display: none;">
	<div class="tooltip">
		<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
		<div class="tooltip_body">
			<div class="tooltip_text" >
			
				<ul>
				<li ><strong  id="tooltip_title"></strong></li>
				<li class="blank_s2" ></li>
				<li class="tooltip_line"></li>
				<li class="blank_s5"></li>
				<li>
				 <span id="tooltip_basicInfo"></span><br>
				 <span id="tooltip_ctItro"></span><br></li>
					
				</ul>
			</div>
		</div>
	</div>
</div>

<% //주관식 TOOLTIP %>
<div id="oendAnsTooltip" style="position:absolute; left:0px; z-index:9999; display: none;">
	<div class="oendAnsTooltip">
		<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
		<div class="tooltip_body">
			<div class="tooltip_text" >
			
				<ul>
				<li ><strong  id="oendAnsTooltip_title"></strong></li>
				
					
				</ul>
			</div>
		</div>
	</div>
</div>

<% //객관식 TOOLTIP %>
<div id="mulcAnsTooltip" style=" position:absolute; left:0px; z-index:9999; display: none;">
	<div class="mulcAnsTooltip">
		<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
		<div class="tooltip_body">
			<div class="tooltip_text" >
			
				<ul>
				<li ><strong  id="mulcAnsTooltip_title"></strong></li>
					
				</ul>
			</div>
		</div>
	</div>
</div>

<% //부서 TOOLTIP %>
<div id="deptStacsTooltip" style="position:absolute; left:0px; z-index:9999; display: none;">
	<div class="deptStacsTooltip">
		<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
		<div class="tooltip_body">
			<div class="tooltip_text" >
			
				<ul>
 				<li ><strong  id="deptStacsTooltip_title"></strong></li> 
				<li class="blank_s2" ></li>
				<li class="tooltip_line"></li>
				<li class="blank_s5"></li>

				<li>
				 <span id="deptStacsTooltip_basicInfo"></span><br>
				 <span id="deptStacsTooltip_ctItro"></span><br></li>
				</ul>
			</div>
		</div>
	</div>
</div>