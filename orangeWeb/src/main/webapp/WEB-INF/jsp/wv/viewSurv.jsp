<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function cmplSurvClose(){
	parent.cmplSurvClose();
}
//이미지 미리보기 팝업
function viewSurvImagePop(survId,quesId,examNo){
	$('#viewSurvImagePop').remove();
	var url = './viewImagePop.do?menuId=${menuId}&survId='+survId+'&quesId='+quesId;
	if(examNo != undefined){
		url+='&examNo='+examNo;
	}
	dialog.open('viewSurvImagePop', '<u:msg titleId="or.jsp.setOrg.viewImageTitle" alt="이미지 보기" />', url);
};

$(document).ready(function() {
	<c:forEach var="survReplyVo" items="${survReplyList}" varStatus="replyStatus">
		$("input[id^='check${survReplyVo.quesId}']").each(function(){
			if($(this).val() == '${survReplyVo.replyNo}'){
				$(this).attr("checked", "true");
				$(this).parent().parent().parent().parent().find("input[id='inputCheck${survReplyVo.quesId}']").val('${survReplyVo.mulcInputReplyCont}');
			}
		});
		<u:out var='oendReplyCont' value='${survReplyVo.oendReplyCont}'/>
		$('#textarea${survReplyVo.quesId}').val(('${oendReplyCont}').replace(/<br\s?\/?>/g,"\r\n"));
	</c:forEach>
	setUniformCSS();
	
	//객관식 답변 추가 시 글자수 입력 제한 Alert
	$("#viewSurvForm").find("input[name^='input']").keyup(function(){
        if ($(this).val().length >= $(this).attr('maxlength')) {
            //alertMsg('wv.jsp.viewSurv.maxLengthAlert'); <%//wv.jsp.viewSurv.maxLengthAlert= 제한 글자 수를 초과하였습니다. %>
            $(this).val($(this).val().substr(0, $(this).attr('maxlength')));
        }
    });
	
	//주관식 답변 시 글자수 입력 제한 Alert
	$("#viewSurvForm").find("textarea[id^='textarea']").keyup(function(){
        if ($(this).val().length >= $(this).attr('maxlength')) {
            //alertMsg('wv.jsp.viewSurv.maxLengthAlert'); <%//wv.jsp.viewSurv.maxLengthAlert= 제한 글자 수를 초과하였습니다. %>
            $(this).val($(this).val().substr(0, $(this).attr('maxlength')));
        }
    });
});

function radioVal(chk){
	$("#radioValChk").val(chk);
}

function formSubit(){
	var valChk = 0;
	<c:forEach  var="wvsQue" items="${wvsQueList}"  varStatus="status">
		<c:forEach  var="wvsQueExam" items="${wcQueExamList}"  varStatus="stat">
			<c:if test="${wvsQueExam.quesId == wvsQue.quesId }">
				<c:if test="${wvsQue.mandaReplyYn == 'Y'}" >
					<c:choose>
						<c:when test="${wvsQue.mulChoiYn == 'N'}">
							if($('input:radio[id="radio${wvsQueExam.quesId}"]:checked').length < 1){
								valChk = 1;
							}
						</c:when>
						<c:when test="${wvsQue.mulChoiYn == 'Y'}">
							if($('input:checkbox[id="check${wvsQueExam.quesId}"]').is(":checked") == false){
								valChk = 1;
							}
						
						</c:when>
					</c:choose>
				</c:if>
			</c:if>
			</c:forEach>
			<c:if test="${wvsQue.mandaReplyYn == 'Y'}" >
				<c:if test="${empty wvsQue.mulChoiYn}">
					if($("#textarea${wvsQue.quesId}").val() == ''){
						valChk = 1;
					}
				</c:if>
			</c:if>
	</c:forEach>
		
	
	if(valChk != 1){
		var $form = $('#viewSurvForm');
		
		$form.attr('method','post');
		$form.attr('action','./viewSurvSave.do?menuId=${menuId}&survId=${wvsVo.survId}');
		
		$form[0].submit();
	}else{
		alert("<u:msg titleId="wv.msg.set.notDone" alt="설문이 완료 되지 않았습니다. \r\n 설문을 완료해주십시오."  type="script"/>");
	}

}
//-->
</script>
<form id="viewSurvForm"  method="post" >
<u:title titleId="wv.jsp.viewServ.title" alt="설문참여" menuNameFirst="true"/>
<c:if test="${!empty isFrm && isFrm==true}">
<input type="hidden" name="returnFunc" value="cmplSurvClose"/>
</c:if>
<% // 폼 필드 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="16%"/><col width="52%"/><col width="16%"/><col width="*"/></colgroup>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td class="body_lt">
			<div style="word-break:break-all; word-wrap:break-word;">
				<u:out value="${wvsVo.survSubj}" />
			</div> 
		</td>
		<td class="head_lt"><u:msg titleId="wv.cols.survRegr" alt="설문게시자" /></td>
		<td class="body_lt"><a href="javascript:viewUserPop('${wvsVo.regrUid}');">${wvsVo.regrNm}</a></td>
	</tr>
	<tr>
		<td class="head_lt" style="width:5%">
			<u:msg titleId="cols.itnt" alt="취지"  />
		</td>
		<td colspan="3" class="body_lt" >
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

<%-- <div class="titlearea">
	<div class="tit_left">
		<table  border="0" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;">
			<tr>
				<td class="body_lt" style="width:50px;">
					<dl>
						<dd class="title_s"></dd>
					</dl>
						<strong><u:msg titleId="cols.itnt" alt="취지" /> :</strong>
				</td>
				<td class="body_lt"  style="width:95%">
					<div style="word-break:break-all; word-wrap:break-word;">
						<strong>${wvsVo.survItnt}</strong>
					</div>
					
				</td>
			</tr>
		</table>
	</div>
</div> --%>

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
			</c:otherwise>
		</c:choose>
		
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0"  style="width:100%;table-layout:fixed;">
					
					<tr class="head_rd">
						<td class="height3" colspan="2"></td>
					</tr>
					
					
					<tr class="head_rd">
						<td class="head_lt" colspan="2">
							<div style="word-break:break-all; word-wrap:break-word;">
								<strong><u:msg titleId="cols.ques" alt="질문" /><u:out value="${status.count} ) ${wvsQue.quesCont}"/></strong>
							</div>
							<span style="padding: 0 0 0 50px;">[${wvsQue_multi}]</span>
						</td>
					</tr>
					<c:if test="${!empty wvsQue.imgSavePath}">
						<tr class="head_rd">
							<td colspan="2" class="body_lt">
								<label for="${wvsQue.quesSortOrdr}">
									<c:forEach begin="1" end="12">&nbsp;</c:forEach>
									<a href="javascript:viewSurvImagePop('${wvsQue.survId}','${wvsQue.quesId }');"><img src="${_ctx}${wvsQue.imgSavePath}" width="110" height="125"/></a>
								</label>
							</td>
						</tr>
					</c:if>
			
					<tr class="head_rd">
						<td class="height5" colspan="2"></td>
					</tr>
				</table>
			</td>
		</tr>
		<c:forEach  var="wvsQueExam" items="${wcQueExamList}"  varStatus="stat">
			<c:if test="${wvsQueExam.quesId == wvsQue.quesId }">
			<tr>
				<td style="padding:5px;">
					<table border="0" cellpadding="0" cellspacing="0"style="width:100%;table-layout:fixed;">
						<colgroup>
							<col style="width: 23px;">
							<col>
						</colgroup>
						<c:choose>
							<c:when test="${wvsQue.mulChoiYn == 'N'}">
								<c:if test="${fn:length(wvsQue.wvSurvReplyDVo) != 0}"> 
									<c:forEach var="survReplyVo" items="${wvsQue.wvSurvReplyDVo}" varStatus="replyStatus">
										<c:if test="${wvsQueExam.quesId == survReplyVo.quesId}">
											<tr style="width:100%">
												<td style="width:1%" valign="top">
													<input type="radio" id="radio${wvsQueExam.quesId}" name="${wvsQueExam.quesId}" value="${wvsQueExam.examNo}" <c:if test="${wvsQueExam.examNo == survReplyVo.replyNo}">checked="true"</c:if> /> 
												</td>
												<td class="body_lt" style="width:98%">
													<div style="word-break:break-all; word-wrap:break-word;">
													<label for="${wvsQueExam.examNo}">${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}</label>
													</div>
														<input id="radioInputYn" name="radioInputYn${ctsQueExam.quesId}" type ="hidden" value="${wvsQueExam.inputYn}" onclick="radioVal('1')" />
														<u:input id="radioValChk" name="radioValChk" type="hidden" value="" mandatory="Y"/>
													<c:if test="${wvsQueExam.inputYn == 'Y'}">
														<input id="inputRadio${wvsQueExam.quesId}${wvsQueExam.examOrdr}" name ="inputRadio${wvsQueExam.quesId}${wvsQueExam.examOrdr}" style="width:98%;" type="text" maxlength="250" <c:if test="${wvsQueExam.examNo == survReplyVo.replyNo}">value="${survReplyVo.mulcInputReplyCont}"</c:if>> 
															</input>
													</c:if>
												</td>
											</tr>
										</c:if>
										
									</c:forEach>
								</c:if>
								<c:if test="${fn:length(wvsQue.wvSurvReplyDVo) == 0}">
									 
									<tr>
										<td width="25px" valign="top">
											<input type="radio" id="radio${wvsQueExam.quesId}" name="${wvsQueExam.quesId}" value="${wvsQueExam.examNo}"></input>
										</td>
										
										<td class="body_lt">
											<table>
												<tbody>
													<tr>
														<div style="word-break:break-all; word-wrap:break-word;">
															<label for="${wvsQueExam.examNo}">${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}</label>
														</div>
														<input id="radioInputYn" name="radioInputYn" type ="hidden" value="${wvsQueExam.inputYn}" onclick="radioVal('1')" />
														<u:input id="radioValChk" name="radioValChk" type="hidden" value="" mandatory="Y"/>
													</tr>
													<c:if test="${wvsQueExam.inputYn == 'Y'}">
														<tr>
														<input id="inputRadio${wvsQueExam.quesId}${wvsQueExam.examOrdr}" name ="inputRadio${wvsQueExam.quesId}${wvsQueExam.examOrdr}" type="text" style="width:98%;" maxlength="250"/>
														</tr>
													</c:if>
												</tbody>
											</table>
										</td>
									</tr>
								</c:if>
								
								<c:if test="${!empty wvsQueExam.imgSavePath}">
									<tr>
										<td>
										</td>
										<td class="body_lt">
											<label for="exam41">
												<a href="javascript:viewSurvImagePop('${wvsQueExam.survId}','${wvsQueExam.quesId }','${wvsQueExam.examNo }');"><img src="${_ctx}${wvsQueExam.imgSavePath}" width="110" height="125" /></a>
											</label>
										</td>
									</tr>
								</c:if>
							</c:when>
							<c:when test="${wvsQue.mulChoiYn == 'Y'}">
								<c:if test="${fn:length(survReplyList) != 0}">
									<tr>
										<td width="30px"><input type="checkbox" id="check${wvsQueExam.quesId}" name="${wvsQueExam.quesId}" value="${wvsQueExam.examNo}"></input>
										</td>
										<td class="bodyip_lt">
											<div style="word-break:break-all; word-wrap:break-word;">
												<label for="${wvsQueExam.examNo}">${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}</label>
											</div>
											<input id="checkInputYn" name="checkInputYn"  type ="hidden" value="${wvsQueExam.inputYn}" />
											<u:input id="checkedValChk" name="checkedValChk" type="hidden" value="" mandatory="Y"/>
											<c:if test="${wvsQueExam.inputYn == 'Y'}">
												<input id="inputCheck${wvsQueExam.quesId}" name ="inputCheck${wvsQueExam.quesId}" type="text" style="width:98%;" maxlength="250"></input>
											</c:if>
										</td>
									</tr>
								</c:if>
								<c:if test="${fn:length(survReplyList) == 0}">
									<tr>
										<td width="30px">
											<input type="checkbox" id="check${wvsQueExam.quesId}" name="${wvsQueExam.quesId}" value="${wvsQueExam.examNo}" ></input>
										</td>
										<td class="bodyip_lt">
											<div style="word-break:break-all; word-wrap:break-word;">
												<label for="${wvsQueExam.examNo}">${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}</label>
											</div>
											<input id="checkInputYn" name="checkInputYn"  type ="hidden" value="${wvsQueExam.inputYn}" />
											<u:input id="checkedValChk" name="checkedValChk" type="hidden" value="" mandatory="Y"/>
											<c:if test="${wvsQueExam.inputYn == 'Y'}">
												<input id="inputCheck${wvsQueExam.quesId}" name ="inputCheck${wvsQueExam.quesId}" type="text" style="width:98%;" maxlength="250"/>
											</c:if>
										</td>
									</tr>
								</c:if>
								<c:if test="${!empty wvsQueExam.imgSavePath}">
									<tr>
										<td></td>
										<td class="body_lt">
											<label for="exam41">
												<a href="javascript:viewSurvImagePop('${wvsQueExam.survId}','${wvsQueExam.quesId }','${wvsQueExam.examNo }');"><img src="${_ctx}${wvsQueExam.imgSavePath}" width="110" height="125" /></a>
											</label>
										</td>
									</tr>
								</c:if>
							</c:when>
						</c:choose>
					</table>
				</td>
			
			</tr>
			</c:if>

		</c:forEach>
		<c:if test="${wvsQueExam.quesId != wvsQue.quesId }">
			<c:if test="${empty wvsQue.mulChoiYn}">
				<c:if test="${fn:length(survReplyList) != 0}">
					<tr>
						<td><u:textarea rows="3" style="width:98%;" titleId="cols.cmItro" id="textarea${wvsQue.quesId}" name="${wvsQue.quesId}" maxByte="2000"></u:textarea></td>
					</tr>
				</c:if>
				<c:if test="${fn:length(survReplyList) == 0}">
					<tr>
						<td><u:textarea rows="3" style="width:98%;" titleId="cols.cmItro" id="textarea${wvsQue.quesId}" name="${wvsQue.quesId}" maxByte="2000"></u:textarea></td>
					</tr>
				</c:if>
			</c:if>
		</c:if>
		
		
	</c:forEach>
	
	<!-- 
	
	<c:forEach var="survReplyVo" items="${survReplyList}" varStatus="replyStatus">
		<input id="${replyStatus.index}" value="${survReplyVo.quesId}" type="hidden"/>
		<input id="${survReplyVo.quesId}" value="${survReplyVo.oendReplyCont}" type="hidden"/>
		<input id="reply${survReplyVo.quesId}${replyStatus.index}" value="${survReplyVo.replyNo}" type="hidden"/>
	</c:forEach>
	 -->
	 
	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
		<tr>
			<td class="red_txt" >
				<div style="overflow:auto;" class="editor">${wvsVo.survFtr}</div>
			</td>
		</tr>
	</table>


</u:listArea>




<% // 하단 버튼 %>
<c:if test="${empty isFrm || isFrm==false}">
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:formSubit();"  />
	<!--<u:button titleId="cm.btn.save" alt="저장" href="javascript:formSubit(); history.go(-1);" auth="W" /> -->
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>
<u:blank />
</c:if>

</form>