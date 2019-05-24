<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
function fnTabOn(obj,val){
	$(".view").each(function(){$(this).hide();});
	$(".view."+val+"Cls").each(function(){$(this).show();});
	$(".tabarea dd").each(function(){$(this).attr("class", "tab");});
	$(obj).attr("class","tab_on");
};  


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
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	$layout.adjustBodyHtml('bodyHtmlArea2');
	<c:forEach var="survReplyVo" items="${survReplyList}" varStatus="replyStatus">
		$("input[id^='check${survReplyVo.quesId}']").each(function(){
			if($(this).val() == '${survReplyVo.replyNo}'){
				$(this).attr("checked", "true");
				
				$(this).parent().parent().parent().find("div.check").attr("class","check_on");
				$(this).parent().parent().parent().find("input[id='inputCheck${survReplyVo.quesId}']").val('${survReplyVo.mulcInputReplyCont}');
			}
		});
		<u:out var='oendReplyCont' value='${survReplyVo.oendReplyCont}'/>
		$('#textarea${survReplyVo.quesId}').val(('${oendReplyCont}').replace(/<br\s?\/?>/g,"\r\n"));
	</c:forEach>

	
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
		$m.nav.curr(event, '/ct/surv/viewSurvSave.do?menuId=${menuId}&survId=${ctsVo.survId}&ctId=${param.ctId}&'+$('#viewSurvForm').serialize());
	}else{
		$m.dialog.alert("<u:msg titleId="wv.msg.set.notDone" alt="설문이 완료 되지 않았습니다. \r\n 설문을 완료해주십시오."  type="script"/>");
	}
}

function goList(){
	$m.nav.prev(event, '/ct/surv/listSurv.do?menuId=${menuId}&ctId=${param.ctId}');
}

function fnChecked(dd, type, quesId, examOrdr){	
	var obj = $('input:'+(type=='check'?type+'box':type)+'[id="'+type+quesId+'"]:eq('+(examOrdr-1)+')');
	
	if(type == 'radio'){
		$('dd#radioDD'+quesId).each(function(){
			$(this).attr("class", type);
		});
	}
	
	if(type == 'radio')
		$(dd).attr("class", type+'_on');
	
	if(obj.is(":checked") == false){
		obj.prop('checked',true);
	}
	else{
		if(type == 'check'){
			obj.prop('checked',false);
		}
	}
}

var contOn = false;
//-->
</script>
<!--btnarea S-->
<div class="btnarea" id="btnArea">
	<div class="size">
		<dl>
		<dd class="btn" onclick="formSubit();"><u:msg titleId="cm.btn.save" alt="저장"  /></dd>
		<dd class="btn" onclick="goList();"><u:msg titleId="cm.btn.list" alt="목록"  /></dd>
		<dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
		</dl>
	</div>
</div>
<!--//btnarea E-->
<section>
	<form id="viewSurvForm"  method="post" >
       <!--titlezone S-->
        <div class="titlezone">
            <div class="titarea">
            <dl>
            <dd class="tit"><u:out value="${ctsVo.subj}" /></dd>
            <dd class="name">
		    	${ctsVo.regrNm} |
				<fmt:parseDate var="dateTempParse" value="${ctsVo.survStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="survStartDt" value="${dateTempParse}" pattern="yyyy-MM-dd"/>
				<u:out value="${survStartDt}"/> ~
				<fmt:parseDate var="endDtTempParse" value="${ctsVo.survEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="survEndDt" value="${endDtTempParse}" pattern="yyyy-MM-dd"/>
				<u:out value="${survEndDt}"/>
		    </dd>
         </dl>
            </div>
        </div>
        <!--//titlezone E-->

         <!--tabarea S-->
         <div class="tabarea" id="tabBtnArea">
             <div class="tabsize">
                 <dl>
                 <dd class="tab_on" onclick="javascript:fnTabOn(this,'cont');"><u:msg titleId="cols.cont" alt="내용" /></dd>
                 <dd class="tab" onclick="javascript:fnTabOn(this,'itnt');if(!contOn){$layout.adjustBodyHtml('bodyHtmlArea');contOn = true;}"><u:msg titleId="cols.itnt" alt="취지"  /></dd>
              </dl>
             </div>
			<div class="tab_icol" style="display:none" id="toLeft"></div>
			<div class="tab_icor" style="display:none" id="toRight"></div>
         </div>
         <!--//tabarea E-->
	<div class="blank30 view contCls"></div>
	<div id="tabViewArea">
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
		
		
		<!--entryzone S-->
            <div class="entryzone view contCls" id="ques_${ctsQue.quesId }">
                <div class="entryarea">
                <dl>
                <dd class="etr_input">
                	<u:set var="questionCls" test="${!empty ctsQue.imgSavePath }" value="sv_question2" elseValue="sv_question"/>
                	<div class="${questionCls }">
	            	<c:choose>
	            		<c:when test="${!empty ctsQue.imgSavePath}">
	                        <div class="question">
	                        <table><tr><td><strong><u:msg titleId="cols.ques" alt="질문" /> <u:out value="${status.count}"/>)</strong> ${ctsQue.quesCont} [${ctsQue_multi}] ${ctsQue_oendCount}</td></tr></table>
	                        </div> 
                        	<div id="ctsQuePhoto${ctsQue.survId }_${ctsQue.quesId }" onclick="$(this).hide()" style="background:url('${_ctx}${ctsQue.imgSavePath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:999;"></div>
                        	<div class="photo"><div class="img" onclick="$('#ctsQuePhoto${ctsQue.survId }_${ctsQue.quesId }').show();"><img src="${_ctx}${ctsQue.imgSavePath}" width="73" height="73" /></div></div>
	            		</c:when>
	            		<c:otherwise>
		            		<strong><u:msg titleId="cols.ques" alt="질문" /> <u:out value="${status.count}"/>)</strong> ${ctsQue.quesCont} [${ctsQue_multi}]
	            		</c:otherwise>
	            	</c:choose>
					</div>
                </dd>
                <c:forEach  var="ctsQueExam" items="${ctQueExamList}"  varStatus="stat">
					<c:if test="${ctsQueExam.quesId == ctsQue.quesId }">
						<c:choose>
							<c:when test="${ctsQue.mulChoiYn == 'N'}">
								<c:if test="${fn:length(ctsQue.ctSurvReplyDVo) != 0}"> 
									<c:forEach var="survReplyVo" items="${ctsQue.ctSurvReplyDVo}" varStatus="replyStatus">
										<c:if test="${ctsQueExam.quesId == survReplyVo.quesId}">
											<dd class="etr_blank"></dd>
							                <dd class="etr_input">
							                    <div class="sv_radiolt"><div class="radio${ctsQueExam.examNo == survReplyVo.replyNo ? '_on' : '' }" id="check${ctsQueExam.quesId}_${ctsQueExam.examOrdr}" onclick="$ui.toggle(this, 'ques_${ctsQue.quesId }');"><input name="${ctsQueExam.quesId}" type="radio" id="radio${ctsQueExam.quesId}" style="display:none" value="${ctsQueExam.examNo}" <c:if test="${ctsQueExam.examNo == survReplyVo.replyNo}">checked="checked"</c:if>/></div></div>
							                    <div class="sv_radiort">
							                    <dl>
							                    <dd class="sv_body" onclick="$('#check${ctsQueExam.quesId}_${ctsQueExam.examOrdr}').trigger('click');">${ctsQueExam.examOrdr}. ${ctsQueExam.examDispNm}</dd>
							                    <c:if test="${ctsQueExam.inputYn == 'Y'}">
							                    	<dd class="sv_inputsv"><input type="text" class="sv_ipsv" id="inputRadio${ctsQueExam.quesId}${ctsQueExam.examOrdr}" name ="inputRadio${ctsQueExam.quesId}${ctsQueExam.examOrdr}" maxlength="250" <c:if test="${ctsQueExam.examNo == survReplyVo.replyNo}">value="${survReplyVo.mulcInputReplyCont}"</c:if>/></dd>
							                    </c:if>
							                    <c:if test="${!empty ctsQueExam.imgSavePath}">
							                    	<dd class="photo">
							                    		<div id="ctsQueExamPhoto${ctsQueExam.survId }_${ctsQueExam.quesId }_${ctsQueExam.examNo }" onclick="$(this).hide()" style="background:url('${_ctx}${ctsQueExam.imgSavePath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:999;"></div>
							                    		<div class="img" onclick="$('#ctsQueExamPhoto${ctsQueExam.survId }_${ctsQueExam.quesId }_${ctsQueExam.examNo }').show();"><img src="${_ctx}${ctsQueExam.imgSavePath}" width="73" height="73" /></div>
							                    	</dd>
							                    </c:if>
							                    </dl>
							                    </div>
							                </dd>
										</c:if>
									</c:forEach>
								</c:if>
								<c:if test="${fn:length(ctsQue.ctSurvReplyDVo) == 0}">
									<dd class="etr_blank"></dd>
					                <dd class="etr_input">
					                    <div class="sv_radiolt"><div class="radio" id="check${ctsQueExam.quesId}_${ctsQueExam.examOrdr}" onclick="$ui.toggle(this, 'ques_${ctsQue.quesId }');"><input name="${ctsQueExam.quesId}" type="radio" id="radio${ctsQueExam.quesId}" style="display:none" value="${ctsQueExam.examNo}" /></div></div>
					                    <div class="sv_radiort">
					                    <dl>
					                    <dd class="sv_body" onclick="$('#check${ctsQueExam.quesId}_${ctsQueExam.examOrdr}').trigger('click');">${ctsQueExam.examOrdr}. ${ctsQueExam.examDispNm}</dd>
					                    <c:if test="${ctsQueExam.inputYn == 'Y'}">
					                    	<dd class="sv_inputsv"><input type="text" class="sv_ipsv" id="inputRadio${ctsQueExam.quesId}${ctsQueExam.examOrdr}" name ="inputRadio${ctsQueExam.quesId}${ctsQueExam.examOrdr}" maxlength="250" /></dd>
					                    </c:if>
					                    <c:if test="${!empty ctsQueExam.imgSavePath}">
					                    	<dd class="photo">
					                    		<div id="ctsQueExamPhoto${ctsQueExam.survId }_${ctsQueExam.quesId }_${ctsQueExam.examNo }" onclick="$(this).hide()" style="background:url('${_ctx}${ctsQueExam.imgSavePath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:999;"></div>
					                    		<div class="img" onclick="$('#ctsQueExamPhoto${ctsQueExam.survId }_${ctsQueExam.quesId }_${ctsQueExam.examNo }').show();"><img src="${_ctx}${ctsQueExam.imgSavePath}" width="73" height="73" /></div>
					                    	</dd>
					                    </c:if>
					                    </dl>
					                    </div>
					                </dd>
								</c:if>
							</c:when>
							<c:when test="${ctsQue.mulChoiYn == 'Y'}">
								<dd class="etr_blank"></dd>
				                <dd class="etr_input">
				                    <div class="sv_radiolt"><div class="check" id="check${ctsQueExam.quesId}_${ctsQueExam.examOrdr}" onclick="$ui.toggle(this, '');"><input name="${ctsQueExam.quesId}" type="checkbox" id="check${ctsQueExam.quesId}" style="display:none" value="${ctsQueExam.examNo}" /></div></div>
				                    <div class="sv_radiort">
				                    <dl>
				                    <dd class="sv_body" onclick="$('#check${ctsQueExam.quesId}_${ctsQueExam.examOrdr}').trigger('click');"><label for="${ctsQueExam.examNo}">${ctsQueExam.examOrdr}. ${ctsQueExam.examDispNm}</label></dd>
				                    <c:if test="${ctsQueExam.inputYn == 'Y'}">
				                    	<dd class="sv_inputsv"><input type="text" class="sv_ipsv" id="inputCheck${ctsQueExam.quesId}" name ="inputCheck${ctsQueExam.quesId}" maxlength="250" /></dd>
				                    </c:if>
				                    <c:if test="${!empty ctsQueExam.imgSavePath}">
				                    	<dd class="photo">
				                    		<div id="ctsQueExamPhoto${ctsQueExam.survId }_${ctsQueExam.quesId }_${ctsQueExam.examNo }" onclick="$(this).hide()" style="background:url('${_ctx}${ctsQueExam.imgSavePath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:999;"></div>
				                    		<div class="img" onclick="$('#ctsQueExamPhoto${ctsQueExam.survId }_${ctsQueExam.quesId }_${ctsQueExam.examNo }').show();"><img src="${_ctx}${ctsQueExam.imgSavePath}" width="73" height="73" /></div>
				                    	</dd>
				                    </c:if>
				                    </dl>
				                    </div>
				                </dd>
							</c:when>
						</c:choose>
					</c:if>
				</c:forEach>
                <c:if test="${ctsQueExam.quesId != ctsQue.quesId }">
                	<c:if test="${empty ctsQue.mulChoiYn}">
						<dd class="etr_blank"></dd>
                		<dd class="etr_input"><div class="etr_textareain"><textarea rows="3" class="etr_ta" id="textarea${ctsQue.quesId}" name="${ctsQue.quesId}"></textarea></div></dd>
					</c:if>
                </c:if>
	            </dl>
                </div>
            </div>
            <!--//entryzone E-->
            <!--blankzone S-->
            <div class="blankzone view contCls">
                <div class="blank25"></div>
                <div class="line1"></div>
                <div class="line8"></div>
                <div class="line1"></div>
                <div class="blank25"></div>
            </div>
            <!--//blankzone E-->
            
	</c:forEach>
		<div class="bodyzone_scroll view contCls">
			<div class="bodyarea">
			<dl>
				<dd class="bodytxt_scroll">
					<div class="scroll editor" id="bodyHtmlArea2">
						<div id="zoom">${ctsVo.survFtr}</div>
					</div>
				</dd>
			</dl>
			</div>
		</div>
        <div class="bodyzone_scroll view itntCls" style="display:none;"><!-- 취지 -->
			 <div class="bodyarea">
				<dl>
				<dd class="bodytxt_scroll">
					<div class="scroll editor" id="bodyHtmlArea"><div id="zoom"><u:out value="${ctsVo.survItnt}" type="noscript" /></div></div>
				</dd>
				</dl>
			</div>
		</div>    
		<div class="btnarea" id="btnArea">
			<div class="size">
				<dl>
				<dd class="btn" onclick="formSubit();"><u:msg titleId="cm.btn.save" alt="저장"  /></dd>
				<dd class="btn" onclick="goList();"><u:msg titleId="cm.btn.list" alt="목록"  /></dd>
				<dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
				</dl>
			</div>
		</div>
		<!--//btnarea E-->
		</div>
	</form>
	

<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
		
</section>