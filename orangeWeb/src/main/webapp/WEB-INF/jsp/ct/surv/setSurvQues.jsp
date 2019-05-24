<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
//객관식 
function setMulcQues(fnc,quesId) {
	var title ='';
	if(fnc == 'reg'){
		title = '<u:msg titleId="wv.cols.set.opQAdd" alt="객관식 질문 추가" />';
	}else if(fnc ='mod'){
		title = '<u:msg titleId="wv.cols.set.opQMod" alt="객관식 질문 수정" />';
	}
	
	dialog.open('setMulcQuesPop', title ,'./setMulcQuesPop.do?menuId=${menuId}&survId=${ctSurvBVo.survId}&fnc=' + fnc + '&quesId='+quesId+'&ctId=${ctId}');
}

//질문 저장
function saveSurvQues(){

	var $form=$("#setSurvQues");
	$form.attr("method", "POST");
	$form.attr("action", "./transSetSurvQuesSave.do?menuId=${menuId}&survId=${survId}&ctId=${ctId}");
	$form.submit();
}

//임시 저장
function saveTempSurvQues(){

	var $form=$("#setSurvQues");
	$form.attr("method", "POST");
	$form.attr("action", "./transSetTmpSaveSurvQues.do?menuId=${menuId}&survId=${survId}&ctId=${ctId}");
	$form.submit();
}

//주관식
function setOendQues(fnc,quesId) {
	
	var title ='';
	if(fnc == 'reg'){
		title = '<u:msg titleId="wv.cols.set.anQAdd" alt="주관식 질문 추가"/>';
	}else if(fnc ='mod'){
		title = '<u:msg titleId="wv.cols.set.anQMod" alt="'주관식 질문 수정"/>';
	}
	dialog.open('setOendQuesPop', title,'./setOendQuesPop.do?menuId=${menuId}&survId=${ctSurvBVo.survId}&fnc=' + fnc + '&quesId='+quesId+'&ctId=${ctId}');
}

function quesDel(survId, quesId){
	 if(confirmMsg("cm.cfrm.del")) { <% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./setSurvQuesDel.do?menuId=${menuId}', {survId:survId,quesId:quesId}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				//dialog.close(this);
				location.href = './setSurvQues.do?fnc=${fnc}&menuId=${menuId}&survId=${ctSurvBVo.survId}&ctId=${ctId}';
			}
		});
	 }
}

function addSubQues() {
	$('#subQuesTd').css('display', 'block');
}

function showSubQuesBtn(id) {
	var btn = $('#' + id);
	var arr = btn.parentsUntil('table').find('a');
	arr.each(function () {
		if ($(this).attr('id') == btn.attr('id')) {
			display = 'block';
		} else {
			display = 'none';
		}
		$(this).css('display', display);
	});
}

function setSurv(){
	 $form = $('#setSurvQues');
	    $form.attr('method','post');
	    $form.attr('action','./setSurv.do?menuId=${menuId}&fnc=mod&survId=${ctSurvBVo.survId}&ctId=${ctId}');
	    $form.submit();
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="subj" value="점심 메뉴에 대한 투표" elseValue="" />
<c:set var="quesNo" value="0" />

<u:title title="${menuTitle }" alt="투표등록" menuNameFirst="true"/>

<form id="setSurvQues">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="survId" name="survId" value="${ctSurvBVo.survId}" />

<% // 폼 필드 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt">${ctSurvBVo.subj}</td>
	</tr>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.itnt" alt="취지" /></td>
	<td class="body_lt">
		<div style="word-break:break-all; word-wrap:break-word;">
			${ctSurvBVo.survItnt}
		</div>
	</td>
	</tr>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="wv.cols.greeting" alt="하단인사말" /></td>
	<td class="body_lt">
		<div style="word-break:break-all; word-wrap:break-word;">
			${ctSurvBVo.survFtr}
		</div>
	</td>
	</tr>
	</table>
</div>
<u:blank />

<u:listArea id="quesList">
	<c:forEach  var="ctsQue" items="${ctsQueList}"  varStatus="status">
		<tr>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr class="head_rd">
						<td class="head_lt"><strong><u:msg titleId="cols.ques" alt="질문" /> ${status.count})</strong></td>
						<td class="listicon_rt">
							<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?" var="msg" />
							<c:if test="${ctsQue.mulChoiYn != null}">
								<u:buttonS onclick="setMulcQues('mod','${ctsQue.quesId}');" titleId="wv.btn.quesMod" alt="질문수정"/>
							</c:if>
							<c:if test="${ctsQue.mulChoiYn == null}">
								<u:buttonS onclick="setOendQues('mod','${ctsQue.quesId}');" titleId="wv.btn.quesMod" alt="질문수정"/>
							</c:if>
							<u:buttonS onclick="javascript:quesDel('${ctsQue.survId}','${ctsQue.quesId}');" titleId="wv.btn.quesDel" alt="질문삭제"/>
						</td>
					</tr>
					
					
					<tr>
						<td colspan="2" style="padding:5px;">
						<table width="100%" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
							<tr>
								<td class="body_lt" colspan="2">
									<div style="word-break:break-all; word-wrap:break-word;">
										<strong><u:out value="${ctsQue.quesCont}" /></strong>
									</div>
								</td>
							</tr>
							
							<c:if test="${!empty ctsQue.imgSavePath}">
								<tr>
									<td colspan="2" class="body_lt">
										<label for="${ctsQue.quesSortOrdr}">
											<img src="${_ctx}${ctsQue.imgSavePath}" width="110" height="125"/>
										</label>
									</td>
								</tr>
							</c:if>
				
							<tr>
								<td  colspan="2" class="dotline"></td>
							</tr>
							<tr>
								<td class="height3"></td>
							</tr>
							<c:forEach  var="ctsQueExam" items="${returnWcQueExamList}"  varStatus="status">
								<c:if test="${ctsQueExam.quesId == ctsQue.quesId }">
								<tr>
									<td colspan="2">
										<table border="0" cellpadding="0" cellspacing="0" style="width:100%;table-layout:fixed;">
											<c:choose>
												<c:when test="${ctsQue.mulChoiYn == 'N'}"> 
													<tr>
														<td width="25px"><input type="radio" id="${ctsQueExam.quesId}" name="${ctsQueExam.quesId}" value="${ctsQueExam.examNo}" disabled="disabled" /></td>
														<td class="body_lt" colspan="2">
															<div style="word-break:break-all; word-wrap:break-word;">
																<label for="${ctsQueExam.examNo}">${ctsQueExam.examOrdr}. <u:out value="${ctsQueExam.examDispNm}" /></label>
															</div>
															
															<input id="radioInputYn" name="radioInputYn" type ="hidden" value="${ctsQueExam.inputYn}" />
															<c:if test="${ctsQueExam.inputYn == 'Y'}">
																</br><input id="inputRadio${ctsQueExam.quesId}" name ="inputRadio${ctsQueExam.quesId}" type="text" style="width:98%;" readonly="readonly" disabled="disabled"/>
															</c:if>
														</td>
													</tr>
													<c:if test="${!empty ctsQueExam.imgSavePath}">
														<tr>
															<td colspan="2" class="body_lt">
																<label for="exam41">
																	<c:forEach begin="1" end="6">&nbsp;</c:forEach>
																	<img src="${_ctx}${ctsQueExam.imgSavePath}" width="110" height="125"/>
																</label>
															</td>
														</tr>
													</c:if>
												</c:when>
												<c:when test="${ctsQue.mulChoiYn == 'Y'}"> 
													<tr>
														<td width="30px"><u:checkbox id="${ctsQueExam.quesId}" name="${ctsQueExam.quesId}" value="${ctsQueExam.examNo}" checked="false" disabled="Y" /></td>
														<td class="bodyip_lt" colspan="2">
															<div style="word-break:break-all; word-wrap:break-word;">
																	<label for="${ctsQueExam.examNo}">${ctsQueExam.examOrdr}. <u:out value="${ctsQueExam.examDispNm}" /></label>	
															</div>	
																
															<input id="checkInputYn" name="checkInputYn"  type ="hidden" value="${ctsQueExam.inputYn}" />
															<c:if test="${ctsQueExam.inputYn == 'Y'}">
																</br><input id="inputCheck${ctsQueExam.quesId}" name ="inputCheck${ctsQueExam.quesId}" type="text" style="width:98%;" disabled="disabled" readonly="readonly"/>
															</c:if>
														</td>
													</tr>
													<c:if test="${!empty ctsQueExam.imgSavePath}">
														<tr>
															<td colspan="2" class="body_lt">
																<label for="exam41">
																	<c:forEach begin="1" end="6">&nbsp;</c:forEach>
																	<img src="${_ctx}${ctsQueExam.imgSavePath}" width="110" height="125"/>
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
									<tr>
										<td colspan="2"><textarea rows="3" style="width:98%;" id="${ctsQue.quesId}" name="${ctsQue.quesId}" readonly="readonly" disabled="disabled"></textarea></td>
									</tr>
								</c:if>
							</c:if>
						</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		
		
		
	</c:forEach>

	
	<tr>
	<td><table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr class="head_rd">
		<td class="head_lt"><strong>[ <u:msg titleId="cols.ques" alt="질문" /> 등록 ]</strong></td>
		<td class="listicon_rt">
			<u:buttonS onclick="setMulcQues('reg');" titleId="wv.btn.mulcQues" alt="객관식질문"
			/><u:buttonS onclick="setOendQues('reg');" titleId="wv.btn.oendQues" alt="주관식질문"
			/></td>
		</tr>
		</table></td>
	</tr>
	
	
</u:listArea>

<u:blank />

<u:buttonArea>
	
	<c:if test="${!empty authChkW && authChkW == 'W' }">
		<u:button titleId="cm.btn.back" href="javascript:setSurv();" alt="뒤로"/>
		<c:if test="${wvSurvBVo.survPrgStatCd != '2'}">
			<u:button titleId="cm.btn.tmpSave" href="javascript:saveTempSurvQues();" alt="임시저장" />
		</c:if>
		<u:button titleId="cm.btn.save" href="javascript:saveSurvQues();" alt="저장"/>
	</c:if>
	<u:button titleId="cm.btn.cancel" href="javascript:history.go(-2);" alt="취소" />
</u:buttonArea>

<u:blank />

</form>

