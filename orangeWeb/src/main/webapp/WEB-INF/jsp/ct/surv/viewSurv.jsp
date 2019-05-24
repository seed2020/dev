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
	<c:forEach  var="ctsQue" items="${ctsQueList}"  varStatus="status">
		<c:forEach  var="ctsQueExam" items="${ctQueExamList}"  varStatus="stat">
			<c:if test="${ctsQueExam.quesId == ctsQue.quesId }">
				<c:if test="${ctsQue.mandaReplyYn == 'Y'}" >
					<c:choose>
						<c:when test="${ctsQue.mulChoiYn == 'N'}">
							if($('input:radio[id="radio${ctsQueExam.quesId}"]:checked').length < 1){
								valChk = 1;
							}
						</c:when>
						<c:when test="${ctsQue.mulChoiYn == 'Y'}">
							if($('input:checkbox[id="check${ctsQueExam.quesId}"]').is(":checked") == false){
								valChk = 1;
							}
						
						</c:when>
					</c:choose>
				</c:if>
			</c:if>
			</c:forEach>
			<c:if test="${ctsQue.mandaReplyYn == 'Y'}" >
				<c:if test="${empty ctsQue.mulChoiYn}">
					if($("#textarea${ctsQue.quesId}").val() == ''){
						valChk = 1;
					}
				</c:if>
			</c:if>
	</c:forEach>
		
	
	if(valChk != 1){
		var $form = $('#viewSurvForm');
		
		$form.attr('method','post');
		$form.attr('action','./viewSurvSave.do?menuId=${menuId}&survId=${ctsVo.survId}&ctId=${ctId}');
		
		$form[0].submit();
	}else{
		alert("<u:msg titleId="wv.msg.set.notDone" alt="설문이 완료 되지 않았습니다. \r\n 설문을 완료해주십시오." type="script"/>");
	}

}
//-->
</script>
<form id="viewSurvForm"  method="post" >
<u:title titleId="ct.jsp.viewServ.title" alt="투표참여" menuNameFirst="true"/>

<% // 폼 필드 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="16%"/><col width="52%"/><col width="16%"/><col width="*"/></colgroup>
	<tr>
		<td  width="16%" class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td  width="52%" class="body_lt">
			<div style="word-break:break-all; word-wrap:break-word;">
				<u:out value="${ctsVo.subj}" />
			</div>	
		</td>
		<td  width="16%" class="head_lt"><u:msg titleId="wv.cols.survRegr" alt="투표게시자" /></td>
		<td class="body_lt"><a href="javascript:viewUserPop('${ctsVo.regrUid}');">${ctsVo.regrNm}</a></td>
	</tr>
	<tr>
		<td class="head_lt" style="width:5%">
			<u:msg titleId="cols.itnt" alt="취지"  />
		</td>
		<td colspan="3" class="body_lt" >
			<div style="overflow:auto;height:200px;" class="editor">${ctsVo.survItnt}</div>
		</td>
	</tr>

	<!--  
	<tr>
		<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
		<td>
			<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="wv" mode="view" /></c:if>
		</td>
	</tr>
	
	 -->
</table>
</div>

<u:blank />

<%-- <div class="titlearea">
	<div class="tit_left">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;">
			<tr>
				<td class="body_lt" style="width:50px">
					<dl>
						<dd class="title_s"></dd>
					</dl>
						<strong><u:msg titleId="cols.itnt" alt="취지" /> :</strong>
				</td>
				<td class="body_lt"  style="width:95%">
					<div style="word-break:break-all; word-wrap:break-word;">
						<strong>${ctsVo.survItnt}</strong>
					</div>
					
				</td>
			</tr>
		</table>
	</div>
</div> --%>

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
		<td><table border="0" cellpadding="0" cellspacing="0"  style="width:100%;table-layout:fixed;">
			
			<tr class="head_rd">
			<td class="height3" colspan="2"></td>
			</tr>
			
			
			<tr class="head_rd">
				<td class="head_lt" colspan="2">
					<div style="word-break:break-all; word-wrap:break-word;">
						<strong><u:msg titleId="cols.ques" alt="질문" /><u:out value="${status.count} ) ${ctsQue.quesCont}"/></strong>
					</div>
					<span style="padding: 0 0 0 50px;">[${ctsQue_multi}]</span>
				</td>
			</tr>
			<c:if test="${!empty ctsQue.imgSavePath}">
				<tr class="head_rd">
					<td colspan="2" class="body_lt"><label for="${ctsQue.quesSortOrdr}">
					<c:forEach begin="1" end="12">&nbsp;</c:forEach>
					<a href="javascript:viewSurvImagePop('${ctsQue.survId}','${ctsQue.quesId }');"><img src="${_ctx}${ctsQue.imgSavePath}" width="110" height="125"/></a>
					</label></td>
				</tr>
			</c:if>
	
			<tr class="head_rd">
			<td class="height5" colspan="2"></td>
			</tr>
			</table></td>
		</tr>
		<c:forEach  var="ctsQueExam" items="${ctQueExamList}"  varStatus="stat">
			<c:if test="${ctsQueExam.quesId == ctsQue.quesId }">
			<tr>
				<td style="padding:5px;">
					<table border="0" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;">
						<colgroup>
							<col style="width: 23px;">
							<col>
						</colgroup>
						<c:choose>
							<c:when test="${ctsQue.mulChoiYn == 'N'}">
								<c:if test="${fn:length(ctsQue.ctSurvReplyDVo) != 0}"> 
									<c:forEach var="survReplyVo" items="${ctsQue.ctSurvReplyDVo}" varStatus="replyStatus">
										<c:if test="${ctsQueExam.quesId == survReplyVo.quesId}">
											<tr style="width:100%">
												<td style="width:1%" valign="top">
													<input type="radio" id="radio${ctsQueExam.quesId}" name="${ctsQueExam.quesId}" value="${ctsQueExam.examNo}" <c:if test="${ctsQueExam.examNo == survReplyVo.replyNo}">checked="true"</c:if>> 
													</input>
												</td>
												<td class="body_lt" style="width:98%">
													<div style="word-break:break-all; word-wrap:break-word;">
														<label for="${ctsQueExam.examNo}">${ctsQueExam.examOrdr}. ${ctsQueExam.examDispNm}</label>
													</div>
														<input id="radioInputYn" name="radioInputYn${ctsQueExam.quesId}" type ="hidden" value="${ctsQueExam.inputYn}" onclick="radioVal('1')" />
														<u:input id="radioValChk" name="radioValChk" type="hidden" value="" mandatory="Y"/>
													<c:if test="${ctsQueExam.inputYn == 'Y'}">
														<input id="inputRadio${ctsQueExam.quesId}${ctsQueExam.examOrdr}" name ="inputRadio${ctsQueExam.quesId}${ctsQueExam.examOrdr}" style="width:98%;" type="text" maxlength="250" <c:if test="${ctsQueExam.examNo == survReplyVo.replyNo}">value="${survReplyVo.mulcInputReplyCont}"</c:if>> 
															</input>
													</c:if>
												</td>
											</tr>
										</c:if>
										
									</c:forEach>
								</c:if>
								<c:if test="${fn:length(ctsQue.ctSurvReplyDVo) == 0}">
									 
									<tr>
										<td width="25px" valign="top">
											<input type="radio" id="radio${ctsQueExam.quesId}" name="${ctsQueExam.quesId}" value="${ctsQueExam.examNo}"></input>
										</td>
										
										<td class="body_lt">
											<table>
												<tbody>
													<tr>
														<div style="word-break:break-all; word-wrap:break-word;">
															<label for="${ctsQueExam.examNo}">${ctsQueExam.examOrdr}. ${ctsQueExam.examDispNm}</label>
														</div>
														<input id="radioInputYn" name="radioInputYn" type ="hidden" value="${ctsQueExam.inputYn}" onclick="radioVal('1')" />
														<u:input id="radioValChk" name="radioValChk" type="hidden" value="" mandatory="Y"/>
													</tr>
													<c:if test="${ctsQueExam.inputYn == 'Y'}">
														<tr>
														<input id="inputRadio${ctsQueExam.quesId}${ctsQueExam.examOrdr}" name ="inputRadio${ctsQueExam.quesId}${ctsQueExam.examOrdr}" type="text" style="width:98%;" maxlength="250"/>
														</tr>
													</c:if>
												</tbody>
											</table>
										</td>
									</tr>
								</c:if>
								
								<c:if test="${!empty ctsQueExam.imgSavePath}">
									<tr>
										<td>
										</td>
										<td class="body_lt">
											<label for="exam41">
												<a href="javascript:viewSurvImagePop('${ctsQueExam.survId}','${ctsQueExam.quesId }','${ctsQueExam.examNo }');"><img src="${_ctx}${ctsQueExam.imgSavePath}" width="110" height="125" /></a>
											</label>
										</td>
									</tr>
								</c:if>
							</c:when>
							<c:when test="${ctsQue.mulChoiYn == 'Y'}">
								<c:if test="${fn:length(survReplyList) != 0}">
									<tr>
										<td width="30px"><input type="checkbox" id="check${ctsQueExam.quesId}" name="${ctsQueExam.quesId}" value="${ctsQueExam.examNo}"></input>
										</td>
										<td class="bodyip_lt">
											<div style="word-break:break-all; word-wrap:break-word;">
												<label for="${ctsQueExam.examNo}">${ctsQueExam.examOrdr}. ${ctsQueExam.examDispNm}</label>
											</div>
											<input id="checkInputYn" name="checkInputYn"  type ="hidden" value="${ctsQueExam.inputYn}" />
											<u:input id="checkedValChk" name="checkedValChk" type="hidden" value="" mandatory="Y"/>
											<c:if test="${ctsQueExam.inputYn == 'Y'}">
												<input id="inputCheck${ctsQueExam.quesId}" name ="inputCheck${ctsQueExam.quesId}" type="text" style="width:98%;" maxlength="250"></input>
											</c:if>
										</td>
									</tr>
								</c:if>
								<c:if test="${fn:length(survReplyList) == 0}">
									<tr>
										<td width="30px">
											<input type="checkbox" id="check${ctsQueExam.quesId}" name="${ctsQueExam.quesId}" value="${ctsQueExam.examNo}" ></input>
										</td>
										<td class="bodyip_lt">
											<div style="word-break:break-all; word-wrap:break-word;">
												<label for="${ctsQueExam.examNo}">${ctsQueExam.examOrdr}. ${ctsQueExam.examDispNm}</label>
											</div>
											<input id="checkInputYn" name="checkInputYn"  type ="hidden" value="${ctsQueExam.inputYn}" />
											<u:input id="checkedValChk" name="checkedValChk" type="hidden" value="" mandatory="Y"/>
											<c:if test="${ctsQueExam.inputYn == 'Y'}">
												<input id="inputCheck${ctsQueExam.quesId}" name ="inputCheck${ctsQueExam.quesId}" type="text" style="width:98%;" maxlength="250"/>
											</c:if>
										</td>
									</tr>
								</c:if>
								<c:if test="${!empty ctsQueExam.imgSavePath}">
									<tr>
										<td></td>
										<td class="body_lt">
											<label for="exam41">
												<a href="javascript:viewSurvImagePop('${ctsQueExam.survId}','${ctsQueExam.quesId }','${ctsQueExam.examNo }');"><img src="${_ctx}${ctsQueExam.imgSavePath}" width="110" height="125" /></a>
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
		<c:if test="${ctsQueExam.quesId != ctsQue.quesId }">
			<c:if test="${empty ctsQue.mulChoiYn}">
				<c:if test="${fn:length(survReplyList) != 0}">
					<tr>
						<td><textarea rows="3" style="width:98%;" id="textarea${ctsQue.quesId}" name="${ctsQue.quesId}" maxlength="980" ></textarea></td>
					</tr>
				</c:if>
				<c:if test="${fn:length(survReplyList) == 0}">
					<tr>
						<td><textarea rows="3" style="width:98%;" id="textarea${ctsQue.quesId}" name="${ctsQue.quesId}" maxlength="980"></textarea></td>
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
					<div style="overflow:auto;min-height:50px;" class="editor">${ctsVo.survFtr}</div>
				</td>
			</tr>
		</table>
	 
</u:listArea>


<u:blank />


<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:formSubit();"  />
	<!--<u:button titleId="cm.btn.save" alt="저장" href="javascript:formSubit(); history.go(-1);" auth="W" /> -->
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" auth="R" />
</u:buttonArea>
</form>
