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
		$m.nav.curr(event, '/wv/viewSurvSave.do?menuId=${menuId}&survId=${wvsVo.survId}&'+$('#viewSurvForm').serialize());
	}else{
		$m.dialog.alert("<u:msg titleId="wv.msg.set.notDone" alt="설문이 완료 되지 않았습니다. \r\n 설문을 완료해주십시오."  type="script"/>");
	}

}

function goList(){
	$m.nav.prev(event, '/wv/listSurv.do?menuId=${menuId}');
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

<% // 파일 다운로드 %>
function downFile(id,dispNm) {

	var ids = [];
	ids.push(id);
	var $form = $('<form>', {
			'method':'get',
			'action':'/wv/downFile.do',
			'target':$m.browser.mobile && $m.browser.safari ? '' : 'dataframe'
		}).append($('<input>', {
			'name':'menuId',
			'value':'${menuId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'survId',
			'value':'${param.survId}',
			'type':'hidden'
		})).append($('<input>', {
			'name':'fileIds',
			'value':ids,
			'type':'hidden'
		}));
	if($m.browser.naver || $m.browser.daum){
		$form.append($('<input>', {'name':'fwd','value':$form.attr('action'),'type':'hidden'}));
		$form.attr('action', '/cm/download/wv/'+encodeURI(dispNm));
	}
	
	$(top.document.body).append($form);
	$m.secu.set();
	$form.submit();
	$form.remove();
};

<c:if test="${viewYn eq 'Y'}">
<% // 문서뷰어 %>
function viewAttchFile(id) {
	var url = "/wv/attachViewSub.do?menuId=${menuId}&survId=${param.survId}";
	url+='&fileIds='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>

var contOn = false;
$(document).ready(function() {
	<%// 목록의 footer 위치를 일정하게 %>
	//$space.placeFooter('tabview');
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	$layout.adjustBodyHtml('bodyHtmlArea2');
	$("input[type='text'], textarea").bind("focus blur", function (e) {
        if (e.type == 'focus') {
           $('#focusLayout').show();
        }else{
        	$('#focusLayout').hide();
        }
    });

});

//]]>
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
		    <dd class="tit">${wvsVo.survSubj}</dd>
		    <dd class="name">
		    	${wvsVo.regrNm } |
				<fmt:parseDate var="dateTempParse" value="${wvsVo.survStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="survStartDt" value="${dateTempParse}" pattern="yyyy-MM-dd"/>
				<u:out value="${survStartDt}"/> ~
				<fmt:parseDate var="endDtTempParse" value="${wvsVo.survEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
                 <c:if test="${!empty fileVoList }">
                 	<dd class="tab" onclick="javascript:fnTabOn(this,'attch');"><u:msg titleId="cols.att" alt="첨부" /></dd>
                 </c:if>
              </dl>
             </div>
			<div class="tab_icol" style="display:none" id="toLeft"></div>
			<div class="tab_icor" style="display:none" id="toRight"></div>
         </div>
         <!--//tabarea E-->

        <div class="bodyzone_scroll view itntCls" style="display:none;"><!-- 취지 -->
		 <div class="bodyarea">
			<dl>
			<dd class="bodytxt_scroll">
				<div class="scroll editor" id="bodyHtmlArea"><div id="zoom"><u:out value="${wvsVo.survItnt}" type="noscript" /></div></div>
			</dd>
			</dl>
		</div>
		</div>
	<div class="blank30 view contCls"></div>
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
            
            
            <!--entryzone S-->
            <div class="entryzone view contCls" id="ques_${wvsQue.quesId }">
                <div class="entryarea">
                <dl>
                <dd class="etr_input">
                	<u:set var="questionCls" test="${!empty wvsQue.imgSavePath }" value="sv_question2" elseValue="sv_question"/>
                	<div class="${questionCls }">
	            	<c:choose>
	            		<c:when test="${!empty wvsQue.imgSavePath}">
	                        <div class="question">
	                        <table><tr><td><strong><u:msg titleId="cols.ques" alt="질문" /> <u:out value="${status.count}"/>)</strong> ${wvsQue.quesCont} [${wvsQue_multi}] ${wvsQue_oendCount}</td></tr></table>
	                        </div> 
                        	<div id="wvsQuePhoto${wvsQue.survId }_${wvsQue.quesId }" onclick="$(this).hide()" style="background:url('${_ctx}${wvsQue.imgSavePath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:999;"></div>
                        	<div class="photo"><div class="img" onclick="$('#wvsQuePhoto${wvsQue.survId }_${wvsQue.quesId }').show();"><img src="${_ctx}${wvsQue.imgSavePath}" width="73" height="73" /></div></div>
	            		</c:when>
	            		<c:otherwise>
		            		<strong><u:msg titleId="cols.ques" alt="질문" /> <u:out value="${status.count}"/>)</strong> ${wvsQue.quesCont} [${wvsQue_multi}]
	            		</c:otherwise>
	            	</c:choose>
					</div>
                </dd>
                <c:forEach  var="wvsQueExam" items="${wcQueExamList}"  varStatus="stat">
					<c:if test="${wvsQueExam.quesId == wvsQue.quesId }">
						<c:choose>
							<c:when test="${wvsQue.mulChoiYn == 'N'}">
								<c:if test="${fn:length(wvsQue.wvSurvReplyDVo) != 0}"> 
									<c:forEach var="survReplyVo" items="${wvsQue.wvSurvReplyDVo}" varStatus="replyStatus">
										<c:if test="${wvsQueExam.quesId == survReplyVo.quesId}">
											<dd class="etr_blank"></dd>
							                <dd class="etr_input">
							                    <div class="sv_radiolt"><div class="radio${wvsQueExam.examNo == survReplyVo.replyNo ? '_on' : '' }" id="check${wvsQueExam.quesId}_${wvsQueExam.examOrdr}" onclick="$ui.toggle(this, 'ques_${wvsQue.quesId }');"><input name="${wvsQueExam.quesId}" type="radio" id="radio${wvsQueExam.quesId}" style="display:none" value="${wvsQueExam.examNo}" <c:if test="${wvsQueExam.examNo == survReplyVo.replyNo}">checked="checked"</c:if>/></div></div>
							                    <div class="sv_radiort">
							                    <dl>
							                    <dd class="sv_body" onclick="$('#check${wvsQueExam.quesId}_${wvsQueExam.examOrdr}').trigger('click');">${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}</dd>
							                    <c:if test="${wvsQueExam.inputYn == 'Y'}">
							                    	<dd class="sv_inputsv"><input type="text" class="sv_ipsv" id="inputRadio${wvsQueExam.quesId}${wvsQueExam.examOrdr}" name ="inputRadio${wvsQueExam.quesId}${wvsQueExam.examOrdr}" maxlength="250" <c:if test="${wvsQueExam.examNo == survReplyVo.replyNo}">value="${survReplyVo.mulcInputReplyCont}"</c:if>/></dd>
							                    </c:if>
							                    <c:if test="${!empty wvsQueExam.imgSavePath}">
							                    	<div id="wvsQueExamPhoto${wvsQueExam.survId }_${wvsQueExam.quesId }_${wvsQueExam.examNo }" onclick="$(this).hide()" style="background:url('${_ctx}${wvsQueExam.imgSavePath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:999;"></div>
							                    	<dd class="photo">							                    		
							                    		<div class="img" onclick="$('#wvsQueExamPhoto${wvsQueExam.survId }_${wvsQueExam.quesId }_${wvsQueExam.examNo }').show();"><img src="${_ctx}${wvsQueExam.imgSavePath}" width="73" height="73" /></div>
							                    	</dd>
							                    </c:if>
							                    </dl>
							                    </div>
							                </dd>
										</c:if>
									</c:forEach>
								</c:if>
								<c:if test="${fn:length(wvsQue.wvSurvReplyDVo) == 0}">
									<dd class="etr_blank"></dd>
					                <dd class="etr_input">
					                    <div class="sv_radiolt"><div class="radio" id="check${wvsQueExam.quesId}_${wvsQueExam.examOrdr}" onclick="$ui.toggle(this, 'ques_${wvsQue.quesId }');"><input name="${wvsQueExam.quesId}" type="radio" id="radio${wvsQueExam.quesId}" style="display:none" value="${wvsQueExam.examNo}" /></div></div>
					                    <div class="sv_radiort">
					                    <dl>
					                    <dd class="sv_body" onclick="$('#check${wvsQueExam.quesId}_${wvsQueExam.examOrdr}').trigger('click');">${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}</dd>
					                    <c:if test="${wvsQueExam.inputYn == 'Y'}">
					                    	<dd class="sv_inputsv"><input type="text" class="sv_ipsv" id="inputRadio${wvsQueExam.quesId}${wvsQueExam.examOrdr}" name ="inputRadio${wvsQueExam.quesId}${wvsQueExam.examOrdr}" maxlength="250" /></dd>
					                    </c:if>
					                    <c:if test="${!empty wvsQueExam.imgSavePath}">
					                    	<dd class="photo">
					                    		<div id="wvsQueExamPhoto${wvsQueExam.survId }_${wvsQueExam.quesId }_${wvsQueExam.examNo }" onclick="$(this).hide()" style="background:url('${_ctx}${wvsQueExam.imgSavePath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:999;"></div>
					                    		<div class="img" onclick="$('#wvsQueExamPhoto${wvsQueExam.survId }_${wvsQueExam.quesId }_${wvsQueExam.examNo }').show();"><img src="${_ctx}${wvsQueExam.imgSavePath}" width="73" height="73" /></div>
					                    	</dd>
					                    </c:if>
					                    </dl>
					                    </div>
					                </dd>
								</c:if>
							</c:when>
							<c:when test="${wvsQue.mulChoiYn == 'Y'}">
								<dd class="etr_blank"></dd>
				                <dd class="etr_input">
				                    <div class="sv_radiolt"><div class="check" id="check${wvsQueExam.quesId}_${wvsQueExam.examOrdr}" onclick="$ui.toggle(this, '');"><input name="${wvsQueExam.quesId}" type="checkbox" id="check${wvsQueExam.quesId}" style="display:none" value="${wvsQueExam.examNo}" /></div></div>
				                    <div class="sv_radiort">
				                    <dl>
				                    <dd class="sv_body" onclick="$('#check${wvsQueExam.quesId}_${wvsQueExam.examOrdr}').trigger('click');">${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}</dd>
				                    <c:if test="${wvsQueExam.inputYn == 'Y'}">
				                    	<dd class="sv_inputsv"><input type="text" class="sv_ipsv" id="inputCheck${wvsQueExam.quesId}" name ="inputCheck${wvsQueExam.quesId}" maxlength="250" /></dd>
				                    </c:if>
				                    <c:if test="${!empty wvsQueExam.imgSavePath}">
				                    	<dd class="photo">
				                    		<div id="wvsQueExamPhoto${wvsQueExam.survId }_${wvsQueExam.quesId }_${wvsQueExam.examNo }" onclick="$(this).hide()" style="background:url('${_ctx}${wvsQueExam.imgSavePath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:999;"></div>
				                    		<div class="img" onclick="$('#wvsQueExamPhoto${wvsQueExam.survId }_${wvsQueExam.quesId }_${wvsQueExam.examNo }').show();"><img src="${_ctx}${wvsQueExam.imgSavePath}" width="73" height="73" /></div>
				                    	</dd>
				                    </c:if>
				                    </dl>
				                    </div>
				                </dd>
							</c:when>
						</c:choose>
					</c:if>
				</c:forEach>
                <c:if test="${wvsQueExam.quesId != wvsQue.quesId }">
                	<c:if test="${empty wvsQue.mulChoiYn}">
						<dd class="etr_blank"></dd>
                		<dd class="etr_input"><div class="etr_textareain"><textarea rows="3" class="etr_ta" id="textarea${wvsQue.quesId}" name="${wvsQue.quesId}"></textarea></div></dd>
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
						<div id="zoom">${wvsVo.survFtr}</div>
					</div>
				</dd>
			</dl>
			</div>
		</div>
    	
		<div class="attachzone view attchCls"  style="display:none;">
			<div class="blank30"></div>
			<div class="attacharea">
				<c:if test="${!empty fileVoList }">
					<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
						<m:attach fileName="${fileVo.dispNm}" fileKb="${fileVo.fileSize/1024 }" downFnc="downFile('${fileVo.fileId}','${fileVo.dispNm}');" viewFnc="viewAttchFile('${fileVo.fileId}');"/>
					</c:forEach>
				</c:if>
			</div>
         </div>  
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
	</form>
	<div id="focusLayout" style="height:100px;width:100%;float:left;"></div>
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
		
</section>