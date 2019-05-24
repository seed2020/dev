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

function survAdmDel(survId){
	
	 if(confirmMsg("cm.cfrm.del")) { <% // cm.cfrm.del=삭제하시겠습니까? %>
			callAjax('./adm/transSetSurvDel.do?menuId=${menuId}', {survId:survId}, function(data) {
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					//dialog.close(this);
					location.href = './listMySurvApvd.do?menuId=${menuId}';
				}
			});
	 }
}

$(document).ready(function() {
	
	var survItnt =	"${wvsVo.survItnt}";
	var survFtr =	"${wvsVo.survFtr}";
	
	$("#survI").html(survItnt);
	$("#survFtr").html(survFtr);
	
	setUniformCSS();
});

function formSubit(){
	if($(':radio[name="apvdYn"]:checked').val() == "N"){
		if(validator.validate('viewSurvApvd')){
			var $form = $('#viewSurvApvd');
			
			$form.attr('method','post');
			$form.attr('action','./setSurvApvdSave.do?menuId=${menuId}&survId=${wvsVo.survId}');
			
			$form[0].submit();
		}
	}else{
		var $form = $('#viewSurvApvd');
		
		$form.attr('method','post');
		$form.attr('action','./setSurvApvdSave.do?menuId=${menuId}&survId=${wvsVo.survId}');
		
		$form[0].submit();
	}

}

//-->
</script>

<u:title titleId="wv.jsp.viewSurvApvdStat.title" alt="설문 승인 상태" menuNameFirst="true"/>

<form id="viewSurvApvd">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea>
	<tr>
		<td width="18%" class="head_lt"><u:msg titleId="wv.cols.apvdStat" alt="승인상태" /></td>
		<td class="body_lt">
			<c:choose>
				<c:when test="${wvsVo.survPrgStatCd == '1'}">
					<u:msg titleId="wv.cols.apvdSigned" alt="승인완료"/>
				</c:when>
				<c:when test="${wvsVo.survPrgStatCd == '2'}">
					<u:msg titleId="wv.cols.apvdStandBy" alt="승인대기중" />
				</c:when>
				<c:when test="${wvsVo.survPrgStatCd == '6'}">
					<u:msg titleId="wv.cols.tempSave" alt="임시저장"/>
				</c:when>
				<c:when test="${wvsVo.survPrgStatCd == '9'}">
					<u:msg titleId="wv.cols.rjt" alt="반려"/>
				</c:when>
			</c:choose>	
		</td>
	</tr>	
	<c:if test="${wvsVo.survPrgStatCd == '9'}">
		<tr>
			<td width="18%" class="head_lt"><u:msg titleId="cols.rjtOpin" alt="반려의견" /></td>
			<td>
				<u:checkArea>
					<u:input type="text"  style="width:98%;" id="returnSurvCont" name="returnSurvCont"  titleId="cols.rjtOpin" alt="반려의견" mandatory="Y" value="${wvsVo.admRjtOpinCont}" readonly="readonly"/>
				</u:checkArea>
			</td>
		</tr>
	</c:if>	
</u:listArea>

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
	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;">
		<tr>
			<td class="body_lt" style="width:50px">
				<dl><dd class="title_s"></dd></dl><strong><u:msg titleId="cols.itnt" alt="취지" /> :</strong>
			</td>
			<td class="body_lt" style="width:94%">
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
		<td><table width="100%" border="0" cellpadding="0" cellspacing="0">
			
			<tr class="head_rd">
			<td class="height3"></td>
			</tr>
			
			
			<tr class="head_rd">
			<td class="head_lt">
				<div style="word-break:break-all; word-wrap:break-word;">
					<strong><u:msg titleId="cols.ques" alt="질문" /><u:out value="${wvsQue.quesSortOrdr} ) ${wvsQue.quesCont}" /></strong>
				</div>
				<br><span style="padding: 0 0 0 50px;">[${wvsQue_multi}]</span></td>
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
			<td class="height5"></td>
			</tr>
			</table></td>
		</tr>
		<c:forEach  var="wvsQueExam" items="${wcQueExamList}"  varStatus="status">
			<c:if test="${wvsQueExam.quesId == wvsQue.quesId }">
			<tr>
				<td style="padding:5px;">
					<table border="0" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;">
						<colgroup>
							<col style="width: 23px;">
							<col>
						</colgroup>
						<c:choose>
							<c:when test="${wvsQue.mulChoiYn == 'N'}"> 
								<tr>
									<td width="25px" valign="top"><input type="radio" id="${wvsQueExam.quesId}" name="${wvsQueExam.quesId}" value="${wvsQueExam.examNo}" disabled="disabled"/></td>
									<td class="body_lt">
										<div style="word-break:break-all; word-wrap:break-word;">
											<label for="${wvsQueExam.examNo}"><u:out value="${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}"/></label>
										</div>
										<c:if test="${wvsQueExam.inputYn == 'Y'}">
											<br><input id="inputRadio${wvsQueExam.quesId}" name ="inputRadio${wvsQueExam.quesId}" type="text" disabled="disabled"/>
										</c:if>
									</td>

								</tr>
								<c:if test="${!empty wvsQueExam.imgSavePath}">
									<tr>
										<td colspan="2" class="body_lt">
											<label for="exam41">
												<c:forEach begin="1" end="6">&nbsp;</c:forEach>
												<a href="javascript:viewSurvImagePop('${wvsQueExam.survId}','${wvsQueExam.quesId }','${wvsQueExam.examNo }');"><img src="${_ctx}${wvsQueExam.imgSavePath}" width="110" height="125" /></a>
											</label>
										</td>
									</tr>
								</c:if>
							</c:when>
							<c:when test="${wvsQue.mulChoiYn == 'Y'}"> 
								<tr>
									<td width="30px" valign="top"><u:checkbox id="${wvsQueExam.quesId}" name="${wvsQueExam.quesId}" value="${wvsQueExam.examNo}" checked="false" disabled="Y" /></td>
									<td class="bodyip_lt">
										<div style="word-break:break-all; word-wrap:break-word;">
											<label for="${wvsQueExam.examNo}"><u:out value="${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}"/></label>
										</div>
										<c:if test="${wvsQueExam.inputYn == 'Y'}">
											<br /><input id="inputRadio${wvsQueExam.quesId}" name ="inputRadio${wvsQueExam.quesId}" type="text" disabled="disabled"/>
										</c:if>
									</td>
								</tr>
								<c:if test="${!empty wvsQueExam.imgSavePath}">
									<tr>
										<td colspan="2" class="body_lt">
											<label for="exam41">
												<c:forEach begin="1" end="6">&nbsp;</c:forEach>
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
				<tr>
					<td><textarea rows="3" style="width:98%;" id="${wvsQue.quesId}" name="${wvsQue.quesId}" disabled="disabled"></textarea></td>
				</tr>
				<c:if test="${!empty wvsQueExam.imgSavePath}">
					<tr>
						<td colspan="2" class="body_lt">
							<label for="exam41">
								<c:forEach begin="1" end="6">&nbsp;</c:forEach>
								<a href="javascript:viewSurvImagePop('${wvsQueExam.survId}','${wvsQueExam.quesId }','${wvsQueExam.examNo }');"><img src="${_ctx}${wvsQueExam.imgSavePath}" width="110" height="125" /></a>
							</label>
						</td>
					</tr>
				</c:if>
			</c:if>
		</c:if>
		
		
	</c:forEach>
	
	</u:listArea>




	<div class="front_left">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
			<tr>
				<td class="red_txt" >
					<div style="overflow:auto;" class="editor">${wvsVo.survFtr}</div>
				</td>
			</tr>
		</table>
	</div>


<% // 하단 버튼 %>

<u:buttonArea>
<c:if test="${wvsVo.survPrgStatCd == '9'}">
	<u:button  href="javascript:survAdmDel('${wvsVo.survId}');" titleId="cm.btn.del" alt="삭제" />
</c:if>
<c:if test="${wvsVo.survPrgStatCd == '2' || wvsVo.survPrgStatCd == '6' || wvsVo.survPrgStatCd == '9'}">
	<u:button titleId="cm.btn.mod" alt="수정" href="./setSurv.do?menuId=${menuId}&survId=${wvsVo.survId}&fnc=mod" />
</c:if>

	<u:button titleId="cm.btn.list" alt="목록" href="./listMySurvApvd.do?"/>
</u:buttonArea>

<u:blank />

</form>

