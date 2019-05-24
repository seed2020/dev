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

function viewSurv(survId,ctId){
	$m.nav.next(event, '/ct/surv/viewSurv.do?menuId=${menuId}&survId=${param.survId}&ctId=${param.ctId}');
}

function goList(){
	$m.nav.prev(event, '/ct/surv/listSurv.do?menuId=${menuId}&ctId=${param.ctId}');
}


function listOendAnsPop(survId,quesId,quesCount){
	$m.dialog.open({
		id:'listOendAnsPop',
		title:'<u:msg titleId="wv.cols.set.listAn" alt="주관식 답변 리스트" />',
		url:'/ct/surv/listOendAnsPop.do?menuId=${menuId}&ctId=${param.ctId}&survId='+survId+'&quesId='+quesId+'&quesCount='+quesCount,
	});
}

function listMulcAnsPop(survId,quesId,examNo){
	$m.dialog.open({
		id:'listMulcAnsPop',
		title:'<u:msg titleId="wv.cols.set.reContOp" alt="객관식 입력 항목 답변 내용"/>',
		url:'/ct/surv/listMulcAnsPop.do?menuId=${menuId}&ctId=${param.ctId}&survId='+survId+'&quesId='+quesId+'&replyNo='+examNo,
	});
}
$(document).ready(function() {
	$layout.adjustBodyHtml('bodyHtmlArea2');
});
var contOn = false;
//-->
</script>
<!--btnarea S-->
       <div class="btnarea" id="btnArea">
           <div class="size">
           <dl>
				<u:set test="${ctsVo.regrUid == logUserUid || ctsVo.modrUid == logUserUid }" var="regrAuth"  value="Y" elseValue="N" />	
				<c:choose>
					<c:when test="${ctsVo.survPrgStatCd == '4'}">
						<c:if test="${regrAuth == 'Y'}">	
						</c:if>
					</c:when>
					<c:when test="${ctsVo.survPrgStatCd == '3'}">
						<c:if test="${regrAuth == 'Y'}">
							<%-- <u:msg titleId="wv.cols.set.chgEnd" alt="마감변경" var="chgEnd"/>
							<button titleId="wv.btn.endMod" alt="마감변경" href="javascript:dialog.open('setEndModPop','${chgEnd }','./setEndModPop.do?menuId=${menuId}&survId=${ctsVo.survId}&ctId=${ctId}');" /> --%>
						</c:if>
						<c:if test="${repetYn == 'Y'}">
							<dd class="btn" onclick="javascript:viewSurv('${ctsVo.survId}','${ctsVo.ctId}');"><u:msg titleId="ct.btn.repeatSurv" alt="재투표" /></dd>
						</c:if>
					</c:when>
				</c:choose>
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
            <dd class="tit">${ctsVo.subj}</dd>
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
        <div class="entryzone view contCls">
            <div class="entryarea">
            <dl>
            
            <dd class="etr_input">
            	<u:set var="questionCls" test="${!empty ctsQue.imgSavePath }" value="sv_question2" elseValue="sv_question"/>
            	<div class="${questionCls }">
            	<c:choose>
            		<c:when test="${!empty ctsQue.imgSavePath }">
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
            
           	<dd class="etr_input">
           		<div class="sv_btnin">
           			<c:if test="${empty ctsQue.mulChoiYn}">
           				<div class="btn" onclick="listOendAnsPop('${ctsQue.survId}','${ctsQue.quesId}','${status.count}');"><u:msg titleId="wv.btn.resView" alt="결과보기"/></div>
            		</c:if>
           		</div>
           	</dd>
            <c:forEach  var="ctsQueExam" items="${returnWcQueExamList}"  varStatus="queExStatus">
            	<c:if test="${ctsQueExam.quesId == ctsQue.quesId }">
		            <dd class="sv_photo">
		                <div class="sv_photoin">
		                   	<c:choose>
		                   		<c:when test="${!empty ctsQueExam.imgSavePath}">
		                   			<div class="sv_photolt_a">
				                        <div class="sv_photolt_b">
					                        <dl>
					                        <dd class="tit1"><u:out value="${ctsQueExam.examOrdr}. ${ctsQueExam.examDispNm}" /></dd>
					                        <dd class="tit2">
					                        	<input type="hidden" id="examDispNo" id="examDispNo" value="<u:msg titleId="cols.ques" alt="질문" />${status.count}) ${ctsQue.quesCont}" />
												<input type="hidden" id="examDispNm" id="examDispNm" value="${ctsQueExam.examOrdr}. ${ctsQueExam.examDispNm}" />
					                            <div class="pbara"><div class="pbarb" style="width:${ctsQueExam.quesAverage}%;"></div></div>
					                            <div class="tit"><strong>${ctsQueExam.selectCount}</strong> <u:msg titleId="wv.cols.set.menSel" alt="명 선택"  /> <strong>${ctsQueExam.quesAverage}%</strong></div>
					                            <c:if test="${ctsQueExam.inputYn == 'Y' && ctsQueExam.selectCount != 0}">
					                            	<div class="btn" onclick="listMulcAnsPop('${ctsQueExam.survId}','${ctsQueExam.quesId}','${ctsQueExam.examNo}');"><u:msg titleId="wv.btn.resView" alt="결과보기"/></div>
					                            </c:if>
					                        </dd>
					                     </dl>
				                        </div>
				                    </div>
			                    	<div id="ctsQueExamPhoto${ctsQueExam.survId }_${ctsQueExam.quesId }_${ctsQueExam.examNo }" onclick="$(this).hide()" style="background:url('${_ctx}${ctsQueExam.imgSavePath}') no-repeat 50% 0; background-size:contain; position:absolute; left:6px; right:6px; height:500px; display:none; z-index:999;"></div>
			                    	<div class="sv_photort">
		                    			<div class="photo" onclick="$('#ctsQueExamPhoto${ctsQueExam.survId }_${ctsQueExam.quesId }_${ctsQueExam.examNo }').show();"><img src="${_ctx}${ctsQueExam.imgSavePath}" width="73" height="73" /></div>
	                    			</div>
		                   		</c:when>
		                   		<c:otherwise>
		                   			<div class="sv_photolt_b2">
				                        <dl>
				                        <dd class="tit1"><u:out value="${ctsQueExam.examOrdr}. ${ctsQueExam.examDispNm}" /></dd>
				                        <dd class="tit2">
				                        	<input type="hidden" id="examDispNo" id="examDispNo" value="<u:msg titleId="cols.ques" alt="질문" />${status.count}) ${ctsQue.quesCont}" />
											<input type="hidden" id="examDispNm" id="examDispNm" value="${ctsQueExam.examOrdr}. ${ctsQueExam.examDispNm}" />
				                            <div class="pbara"><div class="pbarb" style="width:${ctsQueExam.quesAverage}%;"></div></div>
				                            <div class="tit"><strong>${ctsQueExam.selectCount}</strong> <u:msg titleId="wv.cols.set.menSel" alt="명 선택"  /> <strong>${ctsQueExam.quesAverage}%</strong></div>
				                            <c:if test="${ctsQueExam.inputYn == 'Y' && ctsQueExam.selectCount != 0}">
				                            	<div class="btn" onclick="listMulcAnsPop('${ctsQueExam.survId}','${ctsQueExam.quesId}','${ctsQueExam.examNo}');"><u:msg titleId="wv.btn.resView" alt="결과보기"/></div>
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
            <dd class="etr_input"><div class="etr_bodyline scroll editor" id="bodyHtmlArea2"><div id="zoom" class="etr_body_blue">${ctsVo.survFtr}</div></div></dd>
         </dl>
            </div>
        </div>
        <!--//entryzone E-->
        <div class="bodyzone_scroll view itntCls" style="display:none;"><!-- 취지 -->
			 <div class="bodyarea">
				<dl>
				<dd class="bodytxt_scroll">
					<div class="scroll editor" id="bodyHtmlArea"><div id="zoom"><u:out value="${ctsVo.survItnt}" type="noscript" /></div></div>
				</dd>
				</dl>
			</div>
		</div>
		<!--btnarea S-->
       <div class="btnarea" id="btnArea">
       		<div class="blank25"></div>
           <div class="size">
           <dl>
				<u:set test="${ctsVo.regrUid == logUserUid || ctsVo.modrUid == logUserUid }" var="regrAuth"  value="Y" elseValue="N" />	
				<c:choose>
					<c:when test="${ctsVo.survPrgStatCd == '4'}">
						<c:if test="${regrAuth == 'Y'}">	
						</c:if>
					</c:when>
					<c:when test="${ctsVo.survPrgStatCd == '3'}">
						<c:if test="${regrAuth == 'Y'}">
							<%-- <u:msg titleId="wv.cols.set.chgEnd" alt="마감변경" var="chgEnd"/>
							<button titleId="wv.btn.endMod" alt="마감변경" href="javascript:dialog.open('setEndModPop','${chgEnd }','./setEndModPop.do?menuId=${menuId}&survId=${ctsVo.survId}&ctId=${ctId}');" /> --%>
						</c:if>
						<c:if test="${repetYn == 'Y'}">
							<dd class="btn" onclick="javascript:viewSurv('${ctsVo.survId}','${ctsVo.ctId}');"><u:msg titleId="ct.btn.repeatSurv" alt="재투표" /></dd>
						</c:if>
					</c:when>
				</c:choose>
	       <dd class="btn" onclick="goList();"><u:msg titleId="cm.btn.list" alt="목록"  /></dd>
           <dd class="open" id="moreBtn" style="display:none" onclick="$layout.toggleBtns()"></dd>
        </dl>
      	 </div>
       </div>
       <!--//btnarea E-->    
    </div>
	<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
	
</section>