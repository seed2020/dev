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


<% // [하단버튼:승인] %>
function apvdBull() {
	submitForm('bb.cnfm.apvd','Y');
};
<% // [하단버튼:반려] %>
function rjtBull() {
	if($('#rjtOpin').val() == ''){
		$m.msg.alertMsg('cm.input.check.mandatory','<u:msg titleId="cols.rjtOpin"  />');
		return;
	}
	submitForm('bb.cnfm.rjt','N');
};
<% // [하단버튼:목록] 목록으로 이동 %>
function goList() {
	$m.nav.prev(event, './listSurvApvd.do?menuId=${menuId}');
};
<% // submit form %>
function submitForm(cnfmMsg,cd) {
	if(cd == 'Y') $('#rjtOpin').val('');
	$m.msg.confirmMsg(cnfmMsg,null, function(result){
		if(result)
		{
			$m.ajax('/wv/setSurvApvdSave.do?menuId=${menuId}', {survId:'${wvsVo.survId}',apvdYn:cd,returnSurvCont:$('#rjtOpin').val()}, function(data) {
				if (data.message != null) {
					$m.dialog.alert(data.message);
				}
				if (data.result == 'ok') {
					$m.nav.next(event, '/wv/listSurvApvd.do?menuId=${menuId}');
				}
			});
		}
	});
};

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
<%// 반려 팝업 %>
function rjtPop(){
	$m.dialog.open({
		id:'setSurvApvdPop',
		title:'<u:msg titleId="cols.rjtOpin" alt="반려의견" />',
		url:"/wv/setSurvApvdPop.do?menuId=${menuId}&survId=${wvsVo.survId}"
	});
};

<%// 반려 저장 %>
function submitApvd(val){
	$m.dialog.closeAll();
	var $form = $("#viewSurvApvd");
	$form.find("#rjtOpin").val(val);
	submitForm('bb.cnfm.rjt','N');
};
var contOn = false;
$(document).ready(function() {
	$layout.adjustBodyHtml('bodyHtmlArea2');
	<%// 목록의 footer 위치를 일정하게 %>
	//$space.placeFooter('tabview');
});

//]]>
</script>
<!--btnarea S-->
<div class="btnarea" id="btnArea">
	<div class="size">
		<dl>
		<dd class="btn" onclick="apvdBull();"><u:msg titleId="cm.btn.apvd" alt="승인" /></dd>
		<dd class="btn" onclick="rjtPop();"><u:msg titleId="cm.btn.rjt" alt="반려" /></dd>
		<dd class="btn" onclick="goList();"><u:msg titleId="cm.btn.list" alt="목록"  /></dd>
		<dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
		</dl>
	</div>
</div>
<!--//btnarea E-->
<section>
	<form id="viewSurvApvd"  method="post" >
		<input type="hidden" name="rjtOpin" id="rjtOpin"/>
       
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
         <div class="tabarea" id="tabBtnArea" >
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
	<div id="tabViewArea">
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
							                    <div class="sv_radiolt"><div class="radio_disabled${wvsQueExam.examNo == survReplyVo.replyNo ? '_on' : '' }" id="check${wvsQueExam.quesId}_${wvsQueExam.examOrdr}" onclick="$ui.toggle(this, 'ques_${wvsQue.quesId }');"><input name="${wvsQueExam.quesId}" type="radio" id="radio${wvsQueExam.quesId}" style="display:none" value="${wvsQueExam.examNo}" <c:if test="${wvsQueExam.examNo == survReplyVo.replyNo}">checked="checked"</c:if>/></div></div>
							                    <div class="sv_radiort">
							                    <dl>
							                    <dd class="sv_body" onclick="$('#check${wvsQueExam.quesId}_${wvsQueExam.examOrdr}').trigger('click');">${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}</dd>
							                    <c:if test="${wvsQueExam.inputYn == 'Y'}">
							                    	<dd class="sv_inputsv_disabled"><input type="text" class="sv_ipsv_disabled" disabled="disabled" id="inputRadio${wvsQueExam.quesId}${wvsQueExam.examOrdr}" name ="inputRadio${wvsQueExam.quesId}${wvsQueExam.examOrdr}" maxlength="250" <c:if test="${wvsQueExam.examNo == survReplyVo.replyNo}">value="${survReplyVo.mulcInputReplyCont}"</c:if>/></dd>
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
									</c:forEach>
								</c:if>
								<c:if test="${fn:length(wvsQue.wvSurvReplyDVo) == 0}">
									<dd class="etr_blank"></dd>
					                <dd class="etr_input">
					                    <div class="sv_radiolt"><div class="radio_disabled" id="check${wvsQueExam.quesId}_${wvsQueExam.examOrdr}" onclick="$ui.toggle(this, 'ques_${wvsQue.quesId }');"><input name="${wvsQueExam.quesId}" type="radio" id="radio${wvsQueExam.quesId}" style="display:none" value="${wvsQueExam.examNo}" /></div></div>
					                    <div class="sv_radiort">
					                    <dl>
					                    <dd class="sv_body" onclick="$('#check${wvsQueExam.quesId}_${wvsQueExam.examOrdr}').trigger('click');">${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}</dd>
					                    <c:if test="${wvsQueExam.inputYn == 'Y'}">
					                    	<dd class="sv_inputsv_disabled"><input type="text" class="sv_ipsv_disabled" disabled="disabled" id="inputRadio${wvsQueExam.quesId}${wvsQueExam.examOrdr}" name ="inputRadio${wvsQueExam.quesId}${wvsQueExam.examOrdr}" maxlength="250" /></dd>
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
				                    <div class="sv_radiolt"><div class="check_disabled" onclick="$ui.toggle(this, '');"><input name="${wvsQueExam.quesId}" type="checkbox" id="check${wvsQueExam.quesId}" style="display:none" value="${wvsQueExam.examNo}" /></div></div>
				                    <div class="sv_radiort">
				                    <dl>
				                    <dd class="sv_body"><label for="${wvsQueExam.examNo}">${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}</label></dd>
				                    <c:if test="${wvsQueExam.inputYn == 'Y'}">
				                    	<dd class="sv_inputsv_disabled"><input type="text" class="sv_ipsv_disabled" disabled="disabled" id="inputCheck${wvsQueExam.quesId}" name ="inputCheck${wvsQueExam.quesId}" maxlength="250" /></dd>
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
                		<dd class="etr_input"><div class="etr_textareain_disabled"><textarea rows="3" class="etr_ta_disabled" id="textarea${wvsQue.quesId}" name="${wvsQue.quesId}" disabled="disabled"></textarea></div></dd>
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
    	<div class="entryzone view contCls">
            <div class="entryarea">
	            <dl>
	            <dd class="etr_input"><div class="etr_bodyline scroll editor" id="bodyHtmlArea2"><div id="zoom" class="etr_body_blue">${wvsVo.survFtr}</div></div></dd>
	         </dl>
            </div>
        </div>
        <!--//entryzone E-->
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
				<dd class="btn" onclick="apvdBull();"><u:msg titleId="cm.btn.apvd" alt="승인" /></dd>
	        <dd class="btn" onclick="rjtPop();"><u:msg titleId="cm.btn.rjt" alt="반려" /></dd>
	       <dd class="btn" onclick="goList();"><u:msg titleId="cm.btn.list" alt="목록"  /></dd>
           <dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
        </dl>
      	 </div>
       </div>
       <!--//btnarea E-->  
       </div>
	</form>
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</section>