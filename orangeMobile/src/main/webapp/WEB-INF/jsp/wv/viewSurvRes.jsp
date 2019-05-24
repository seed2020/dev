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

function viewSurv(survId){
	$m.nav.next(event, '/wv/viewSurv.do?menuId=${menuId}&survId=' + survId);
}

function goList(){
	$m.nav.prev(event, '/wv/listSurv.do?menuId=${menuId}');
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
}
<c:if test="${viewYn eq 'Y'}">
<% // 문서뷰어 %>
function viewAttchFile(id) {
	var url = "/wv/attachViewSub.do?menuId=${menuId}&survId=${param.survId}";
	url+='&fileIds='+id;
	openViewAttchFile(url);
	//$m.nav.next(null, url);
}
</c:if>
function listOendAnsPop(survId,quesId,quesCount){
	$m.dialog.open({
		id:'listOendAnsPop',
		title:'<u:msg titleId="wv.cols.set.listAn" alt="주관식 답변 리스트" />',
		url:'/wv/listOendAnsPop.do?menuId=${menuId}&survId='+survId+'&quesId='+quesId+'&quesCount='+quesCount,
	});
}
<% //부서별 통계 %>
function listDeptStacsPop(survId,quesId,quesCount){
	$m.nav.next(event, '/wv/listDeptStacsSub.do?menuId=${menuId}&survId='+survId+'&quesId='+quesId+'&quesCount='+quesCount);
};

function listMulcAnsPop(survId,quesId,examNo){
	$m.dialog.open({
		id:'listMulcAnsPop',
		title:'<u:msg titleId="wv.cols.set.reContOp" alt="객관식 입력 항목 답변 내용"/>',
		url:'/wv/listMulcAnsPop.do?menuId=${menuId}&survId='+survId+'&quesId='+quesId+'&replyNo='+examNo,
	});
};
var contOn = false;
$(document).ready(function() {
	$layout.adjustBodyHtml('bodyHtmlArea2');
	<%// 목록의 footer 위치를 일정하게 %>
});
//]]>
</script>
<!--btnarea S-->
       <div class="btnarea" id="btnArea">
           <div class="size">
           <dl>
			<c:if test="${repetYn == 'Y'}">
				<dd class="btn" onclick="javascript:viewSurv('${wvsVo.survId}');"><u:msg titleId="wv.btn.repeatSurv" alt="재설문"  /></dd>
			</c:if>
	       <dd class="btn" onclick="goList();"><u:msg titleId="cm.btn.list" alt="목록"  /></dd>
           <dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
        </dl>
      	 </div>
       </div>
       <!--//btnarea E-->
<section>
       <!--titlezone S-->
		<div class="titlezone">
		    <div class="titarea">
		    <dl>
		    <dd class="tit">${wvsVo.survSubj}</dd>
		    <dd class="name">
		    	${wvsVo.regrNm} |
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
				<c:set var="wvsQue_oendCount"	value= "${wvsQue.oendCount}" />
			</c:otherwise>	
		</c:choose>
		
		
		<!--entryzone S-->
        <div class="entryzone view contCls">
            <div class="entryarea">
            <dl>
            
            <dd class="etr_input">
            	<u:set var="questionCls" test="${!empty wvsQue.imgSavePath }" value="sv_question2" elseValue="sv_question"/>
            	<div class="${questionCls }">
            	<c:choose>
            		<c:when test="${!empty wvsQue.imgSavePath }">
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
            
           	<dd class="etr_input">
           		<div class="sv_btnin">
           			<c:if test="${!empty wvsQue.mulChoiYn}">			
           				<div class="btn" onclick="listDeptStacsPop('${wvsQue.survId}','${wvsQue.quesId}','${status.count}');"><u:msg titleId="wv.cols.set.deptStat" alt="부서별통계" /></div>
           			</c:if>
           			<c:if test="${empty wvsQue.mulChoiYn}">
           				<div class="btn" onclick="listOendAnsPop('${wvsQue.survId}','${wvsQue.quesId}','${status.count}');"><u:msg titleId="wv.btn.resView" alt="결과보기"/></div>
            		</c:if>
           		</div>
           	</dd>
            <c:forEach  var="wvsQueExam" items="${returnWcQueExamList}"  varStatus="queExStatus">
            	<c:if test="${wvsQueExam.quesId == wvsQue.quesId }">
		            <dd class="sv_photo">
		                <div class="sv_photoin">
		                   	<c:choose>
		                   		<c:when test="${!empty wvsQueExam.imgSavePath}">
		                   			<div class="sv_photolt_a">
				                        <div class="sv_photolt_b">
					                        <dl>
					                        <dd class="tit1"><u:out value="${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}" /></dd>
					                        <dd class="tit2">
					                        	<input type="hidden" id="examDispNo" id="examDispNo" value="<u:msg titleId="cols.ques" alt="질문" />${status.count}) ${wvsQue.quesCont}" />
												<input type="hidden" id="examDispNm" id="examDispNm" value="${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}" />
					                            <div class="pbara"><div class="pbarb" style="width:${wvsQueExam.quesAverage}%;"></div></div>
					                            <div class="tit"><strong>${wvsQueExam.selectCount}</strong> <u:msg titleId="wv.cols.set.menSel" alt="명 선택"  /> <strong>${wvsQueExam.quesAverage}%</strong></div>
					                            <c:if test="${wvsQueExam.inputYn == 'Y' && wvsQueExam.selectCount != 0}">
					                            	<div class="btn" onclick="listMulcAnsPop('${wvsQueExam.survId}','${wvsQueExam.quesId}','${wvsQueExam.examNo}');"><u:msg titleId="wv.btn.resView" alt="결과보기"/></div>
					                            </c:if>
					                        </dd>
					                     </dl>
				                        </div>
				                    </div>
			                    	<div id="wvsQueExamPhoto${wvsQueExam.survId }_${wvsQueExam.quesId }_${wvsQueExam.examNo }" onclick="$(this).hide()" style="background:url('${_ctx}${wvsQueExam.imgSavePath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:999;"></div>
			                    	<div class="sv_photort">
		                    			<div class="photo" onclick="$('#wvsQueExamPhoto${wvsQueExam.survId }_${wvsQueExam.quesId }_${wvsQueExam.examNo }').show();"><img src="${_ctx}${wvsQueExam.imgSavePath}" width="73" height="73" /></div>
	                    			</div>
		                   		</c:when>
		                   		<c:otherwise>
		                   			<div class="sv_photolt_b2">
				                        <dl>
				                        <dd class="tit1"><u:out value="${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}" /></dd>
				                        <dd class="tit2">
				                        	<input type="hidden" id="examDispNo" id="examDispNo" value="<u:msg titleId="cols.ques" alt="질문" />${status.count}) ${wvsQue.quesCont}" />
											<input type="hidden" id="examDispNm" id="examDispNm" value="${wvsQueExam.examOrdr}. ${wvsQueExam.examDispNm}" />
				                            <div class="pbara"><div class="pbarb" style="width:${wvsQueExam.quesAverage}%;"></div></div>
				                            <div class="tit"><strong>${wvsQueExam.selectCount}</strong> <u:msg titleId="wv.cols.set.menSel" alt="명 선택"  /> <strong>${wvsQueExam.quesAverage}%</strong></div>
				                            <c:if test="${wvsQueExam.inputYn == 'Y' && wvsQueExam.selectCount != 0}">
				                            	<div class="btn" onclick="listMulcAnsPop('${wvsQueExam.survId}','${wvsQueExam.quesId}','${wvsQueExam.examNo}');"><u:msg titleId="wv.btn.resView" alt="결과보기"/></div>
				                            </c:if>
				                        </dd>
					                    </dl>
				                        </div>
		                   		</c:otherwise>
		                   	</c:choose>
		                </div>
		                <div class="sv_line"></div>
		            </dd>
		        </c:if>
            </c:forEach>
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
	<!--entryzone S-->
        <div class="entryzone view contCls">
            <div class="entryarea">
            <dl>
            <dd class="etr_input"><div class="etr_bodyline scroll editor" id="bodyHtmlArea2"><div id="zoom" class="etr_body_blue">${wvsVo.survFtr}</div></div></dd>
         </dl>
            </div>
        </div>
        <!--//entryzone E-->
        
        <div class="bodyzone_scroll view itntCls" style="display:none;"><!-- 취지 -->
			 <div class="bodyarea">
				<dl>
				<dd class="bodytxt_scroll">
					<div class="scroll editor" id="bodyHtmlArea"><div id="zoom"><u:out value="${wvsVo.survItnt}" type="noscript" /></div></div>
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
       		<div class="blank25"></div>
           <div class="size">
           <dl>
			<c:if test="${repetYn == 'Y'}">
				<dd class="btn" onclick="javascript:viewSurv('${wvsVo.survId}');"><u:msg titleId="wv.btn.repeatSurv" alt="재설문"  /></dd>
			</c:if>
	       <dd class="btn" onclick="goList();"><u:msg titleId="cm.btn.list" alt="목록"  /></dd>
           <dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
        </dl>
      	 </div>
       </div>
       <!--//btnarea E-->
	</div>
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
	
</section>