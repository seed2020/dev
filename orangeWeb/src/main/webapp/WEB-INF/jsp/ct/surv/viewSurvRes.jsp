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


function viewSurv(survId, ctId){
	location.href = './viewSurv.do?menuId=${menuId}&survId=' + survId +'&ctId=' + ctId;
	
}

function survAdmDel(survId, ctId){
	
	 if(confirmMsg("cm.cfrm.del")) { <% // cm.cfrm.del=삭제하시겠습니까? %>
			callAjax('./transSetSurvDel.do?menuId=${menuId}', {survId:survId, ctId:ctId}, function(data) {
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					//dialog.close(this);
					location.href = './listSurv.do?menuId=${menuId}&ctId=' + ctId;
				}
			});
	 }
}
/*
function applyTooltip() {
	$('.ct_survInfo').mouseover(function(event){
		$('#tooltip').css('top', event.pageY - 85);
		$('#tooltip').css('left', event.pageX - 230);
		$('#tooltip').show();
		
		var $ctObj = $("#"+$(this).attr("id")).parent().parent();
		var $tooltip_title=$("#tooltip_title");
		
		$tooltip_title.html($ctObj.find("#examDispNo").val());
		var $tooltip_basicInfo = $("#tooltip_basicInfo");
		$tooltip_basicInfo.html($ctObj.find("#examDispNm").val());
		
	});
	
	$('.ct_survInfo').mouseout(function(event){
		$('#tooltip').hide();     
	});
	
	$('.ct_Cls_subj').mouseover(function(event){
		$('#tooltip').css('top', event.pageY - 85);
		$('#tooltip').css('left', event.pageX - 230);
		$('#tooltip').show();
		
		var $ctObj = $("#"+$(this).attr("id")).parent().parent();
		var $tooltip_title=$("#tooltip_title");
		
		$tooltip_title.html("제목");
		var $tooltip_basicInfo = $("#tooltip_basicInfo");
		$tooltip_basicInfo.html($ctObj.find("#ct_subj").val());
		
	});
	
	$('.ct_Cls_subj').mouseout(function(event){
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

<u:title titleId="ct.jsp.viewServRes.title" alt="투표결과" menuNameFirst="true"/>

<% // 폼 필드 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
		<tr>
		<td width="16%" class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td width="52%" class="body_lt">
			<div class="ellipsis" title="${ctsVo.subj}">
				<span class="ct_Cls_subj" id="wc_Cls_subj" >
				 	<u:out value="${ctsVo.subj}" />
				 	<input type="hidden" id="ct_subj" name="ct_subj" value="${ctsVo.subj}" />
				</span>
			</div>
		</td>
		<td width="16%" class="head_lt"><u:msg titleId="wv.cols.survRegr" alt="투표게시자" /></td>
		<td class="body_lt"><a href="javascript:viewUserPop('${ctsVo.regrUid}');">${ctsVo.regrNm}</a></td>
		</tr>
	
		<tr>
			<td class="head_lt"  style="width:auto">
				<u:msg titleId="cols.itnt" alt="취지" />
			</td>
			<td colspan="3" class="body_lt" style="width:auto">
				<div style="word-break:break-all; word-wrap:break-word;">
					${ctsVo.survItnt}
				</div>
			</td>
		</tr>
		<!-- 
		<tr>
			<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
			<td colspan="3">
				<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="ct" mode="view" /></c:if>
			</td>
		</tr>
		 -->
	</table>
</div>
<u:blank />

<u:listArea>
	<c:forEach  var="ctsQue" items="${ctsQueList}"  varStatus="status">
		<c:choose>
			<c:when test="${ctsQue.mulChoiYn == 'Y'}"> 
				<u:msg var="ctsQue_multi"	alt="여러개 선택" titleId="wv.cols.set.multiSel"/>
			</c:when>
			<c:when test="${ctsQue.mulChoiYn == 'N'}"> 
				<u:msg var="ctsQue_multi"	alt="한개 선택" titleId="wv.cols.set.onlySel"/>
			</c:when>
			<c:otherwise>
				<u:msg var="ctsQue_multi"	alt="주관식 질문" titleId="wv.cols.set.ans"/>
				<c:set var="ctsQue_oendCount"	value= "${ctsQue.oendCount}" />
				
			</c:otherwise>
	</c:choose>
	<tr>
	<td><table width="100%" border="0" cellpadding="0" cellspacing="0"  style="table-layout:fixed;">
		<tr class="head_rd">
		 <c:choose>
		 	<c:when test="${empty ctsQue.mulChoiYn}">
		 		<c:set var="cols" value="14"></c:set>
		    </c:when>
		    <c:when test="${!empty ctsQue.mulChoiYn}">
		      	<c:set var="cols" value="13"></c:set>
		    </c:when>
		  </c:choose>
		<td colspan="${cols}" class="height3"></td>
		</tr>

		<tr class="head_rd">
			
			<td class="head_lt" colspan="13">
			<div style="word-break:break-all; word-wrap:break-word;">
				<strong><u:msg titleId="cols.ques" alt="질문" /><u:out value="${status.count}) ${ctsQue.quesCont}"/></strong>
				<br><span style="padding: 0 0 0 50px;">[${ctsQue_multi}]</span>
					<c:if test="${empty ctsQue.mulChoiYn}">
				 			${ctsQue_oendCount}
				 	</c:if>
				 </div>
			</td>
				<c:if test="${empty ctsQue.mulChoiYn}">
					<u:msg titleId="wv.cols.set.listAn" alt="주관식 답변 리스트" var="listAn"/>
					<td class="listicon_rt"><u:buttonS href="javascript:dialog.open('listOendAnsPop','${listAn }','./listOendAnsPop.do?menuId=${menuId}&survId=${ctsQue.survId}&quesId=${ctsQue.quesId}&quesCount=${status.count}');" titleId="wv.btn.resView" alt="결과보기" /></td>
				</c:if>
		</tr>
		<c:if test="${!empty ctsQue.imgSavePath}">
			<tr class="head_rd">
				<td colspan="${cols}" class="body_lt"><label for="${ctsQue.quesSortOrdr}">
				<c:forEach begin="1" end="12">&nbsp;</c:forEach>
				<a href="javascript:viewSurvImagePop('${ctsQue.survId}','${ctsQue.quesId }');"><img src="${_ctx}${ctsQue.imgSavePath}" width="110" height="125"/></a>
				</label></td>
			</tr>	
		</c:if>
		

		<tr class="head_rd">
		<td colspan="${cols}" class="height5"></td>
		</tr>
		</table></td>
	</tr>
	
	<tr>
		<td>
			<c:forEach  var="ctsQueExam" items="${returnWcQueExamList}"  varStatus="queExStatus">
				<c:if test="${ctsQueExam.quesId == ctsQue.quesId }">
		
					<div style="padding:5px 10px 5px 10px;">
						<table border="0" cellpadding="0" cellspacing="0" style="width:50%; table-layout:fixed;">
							<tr> 
								<td width="300" class="body_lt">
									<div class="ellipsis" title="${ctsQueExam.examOrdr}.${ctsQueExam.examDispNm}">
										<span class="ct_survInfo" id="ct_survInfo${queExStatus.count}">
											<u:out value="${ctsQueExam.examOrdr}.${ctsQueExam.examDispNm}" />
											<input type="hidden" id="examDispNo" id="examDispNo" value="<u:msg titleId="cols.ques" alt="질문" />${status.count}) ${ctsQue.quesCont}" />
											<input type="hidden" id="examDispNm" id="examDispNm" value="${ctsQueExam.examOrdr}. ${ctsQueExam.examDispNm}" />
										</span>
									</div>
								</td>
								<td width="100" class="body_lt" rowspan="2">${ctsQueExam.selectCount}<u:msg titleId="wv.cols.set.menSel" alt="명 선택"  /></td>
								<td width="30" class="body_lt" rowspan="2">${ctsQueExam.quesAverage}%</td>
								<td width="200" rowspan="2"><div style="width:99%;height:10px;border:1px solid #bfc8d2;background-color:#ffffff;"><img src="${_ctx}/images/${_skin}/pollbar.png" width="${ctsQueExam.quesAverage}%" height="10"></div></td>
								<td width="80" class="body_ct" rowspan="2">  
									<c:if test="${ctsQueExam.inputYn == 'Y' }">
										<c:if test="${ctsQueExam.selectCount != 0}">
											<u:msg titleId="wv.cols.set.reContOp" alt="객관식 입력 항목 답변 내용" var="reContOp"/>
											<u:buttonS href="javascript:dialog.open('listMulcAnsPop','${reContOp }','./listMulcAnsPop.do?menuId=${menuId}&survId=${ctsQueExam.survId}&quesId=${ctsQueExam.quesId}&replyNo=${ctsQueExam.examNo}');" titleId="wv.btn.resView" alt="결과보기" />
										</c:if>
									</c:if>
								</td>
							</tr>
							<c:if test="${!empty ctsQueExam.imgSavePath}">
								<td colspan="2" class="body_lt"><label for="exam41">
								<c:forEach begin="1" end="3">&nbsp;</c:forEach>
								<a href="javascript:viewSurvImagePop('${ctsQueExam.survId}','${ctsQueExam.quesId }','${ctsQueExam.examNo }');"><img src="${_ctx}${ctsQueExam.imgSavePath}" width="110" height="125" /></a>
								</label></td>
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
	<u:set test="${ctsVo.regrUid == logUserUid || ctsVo.modrUid == logUserUid }" var="regrAuth"  value="Y" elseValue="N" />	
	<c:choose>
		<c:when test="${ctsVo.survPrgStatCd == '4'}">
			<c:if test="${regrAuth == 'Y'}">	
				<u:button titleId="cm.btn.del" alt="삭제" href="javascript:survAdmDel('${ctsVo.survId}','${ctsVo.ctId}');" />
			</c:if>
		</c:when>
		<c:when test="${ctsVo.survPrgStatCd == '3'}">
			<c:if test="${regrAuth == 'Y'}">
				<u:msg titleId="wv.cols.set.chgEnd" alt="마감변경" var="chgEnd"/>
				<u:button titleId="wv.btn.endMod" alt="마감변경" href="javascript:dialog.open('setEndModPop','${chgEnd }','./setEndModPop.do?menuId=${menuId}&survId=${ctsVo.survId}&ctId=${ctId}');" />
			</c:if>
			<c:if test="${repetYn == 'Y'}">
				<u:button titleId="ct.btn.repeatSurv" alt="재투표" href="javascript:viewSurv('${ctsVo.survId}','${ctsVo.ctId}');" />
			</c:if>
		</c:when>
	</c:choose>
	<u:button titleId="cm.btn.list" alt="목록" href="javascript:history.go(-1);"/>
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