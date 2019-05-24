<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set test="${!empty param.bullId}" var="fnc" value="mod" elseValue="reg" />
<u:out value="${ctBullMastBVo.bullExprDt}" type="date" var="bullExprYmd" />
<u:out value="${ctBullMastBVo.bullExprDt}" type="hm" var="bullExprHm" />
<script type="text/javascript">
//<![CDATA[
//오늘 날짜 리턴
function getToday(){
	var d = new Date();
	return d.getFullYear() +"-"+ ((d.getMonth() > 8) ? d.getMonth()+1 : "0"+(d.getMonth()+1)) +"-"+ ((d.getDate() > 9) ? d.getDate() : "0"+d.getDate());
}         
           
//오늘 시간 리턴
function getToTime(){
	var d = new Date();
	return (d.getHours() < 10 ? "0"+d.getHours() : d.getHours() ) +":"+ (d.getMinutes() < 10 ? "0"+d.getMinutes() : d.getMinutes() );
}

<% // 일시 replace %>
function getDayString(date , regExp){
	return date.replace(regExp,'');
};

<% // 일시 비교 %>
function fnCheckDay(today , setday){
	return today > setday ? true : false;
};

<% // 예약일시 체크 %>
function onBullRezvDayChange(date){
	var regExp = /[^0-9]/g;
	var today = getDayString(getToday(),regExp);
	var setday = getDayString(date,regExp);
	if(fnCheckDay(today , setday)){
		alertMsg('cm.calendar.check.dateAI');
		return true;
	}
	onBullRezvTimeChange('bullRezvHm',setday,'${bullRezvHm }');
	return false;
};

<% // 게시완료일 체크 %>
function onBullExprDayChange(date){
	var regExp = /[^0-9]/g;
	var today = getDayString(getToday(),regExp);
	var setday = getDayString(date,regExp);
	if(fnCheckDay(today , setday)){
		alertMsg('cm.calendar.check.dateAI');
		return true;
	}
	onBullRezvTimeChange('bullExprHm',setday,'${bullExprHm }');
	return false;
};

<% // 예약시간 체크 %>
function onBullRezvTimeChange(objId , setday , initTime){
	var regExp = /[^0-9]/g;
	var today = getDayString(getToday(),regExp);
	setday = getDayString(setday,regExp);
	if(today == setday ){
		var toTime = getToTime().replace(/[^0-9]/g,'');
		var setTime = $('#'+objId).val().replace(/[^0-9]/g,'');
		if(fnCheckDay(toTime , setTime)){
			alertMsg('cm.calendar.check.dateAI');
			$('#'+objId).val(initTime);
		}
	}
};


<% // 완료일시 세팅 %>
function setExprDt() {
	if ($('#bullExprYmd').val() != '') {
		$('#bullExprDt').val($('#bullExprYmd').val() + ' ' + $('#bullExprHm').val() + ':00');
	}
}

$(document).ready(function() {
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	/* var bodyHtml = $("#lobHandlerArea").html();
	if(bodyHtml!=''){
		setEditHtml(bodyHtml);
	} */
	$layout.adjustBodyHtml('bodyHtmlArea');

});

function saveBull() {
	setExprDt();
	var $bullPid = $("#bullPid").val();
	
	if($("#subj").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [제목] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.subj"], function(){
			$("#setBoardForm input[name='subj']").focus();
		});
		return;
	}
	
	$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
		if(result){
			var $form = $('#setBoardForm');
			if($bullPid == null || $bullPid == ''){
				$form.attr('action','/ct/board/transSetBullSavePost.do?menuId=${menuId}&ctId=${ctId}');
			}else{
				$form.attr('action','/ct/board/transSetReplySavePost.do?menuId=${menuId}&ctId=${ctId}&bullPid=${bullPid}');
			}
			$m.nav.post($form);
		}
	});
	
}

function fnCalendar(id,opt,hm,hmId,handler){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		url:'/cm/util/getCalendarPop.do?&id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&hm='+hm+'&hmId='+hmId+'&hmVal='+$('#'+hmId).val()+'&handler='+handler,
	});
}


function getEditHtml(){
	return $('#bodyHtmlArea').html();
}
function setEditHtml(editHtml){
	$('#bodyHtmlArea').html(editHtml);
	$('#cont').html(editHtml);
}
//]]>
</script>
<div class="btnarea">
    <div class="size">
        <dl>
        	<dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>           	 
           	<dd class="btn" onclick="saveBull();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
     </dl>
    </div>
</div>
<section>

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<form id="setBoardForm" name="setBoardForm" enctype="multipart/form-data">
	<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
	<input type="hidden" id="bullId" name="bullId" value="${param.bullId}" />
	<input type="hidden" id="bullPid" name="bullPid" value="${param.bullPid}" />
	<input type="hidden" id="bullStatCd" name="bullStatCd" value="B" />
	<input type="hidden" id="bullRezvDt" name="bullRezvDt" value="${ctBullMastBVo.bullRezvDt}" />
	<input type="hidden" id="tgtDeptYn" name="tgtDeptYn" value="${ctBullMastBVo.tgtDeptYn}" />
	<input type="hidden" id="tgtUserYn" name="tgtUserYn" value="${ctBullMastBVo.tgtUserYn}" />
	
	<textarea id="cont" name="cont" style="display:none">${ctBullMastBVo.cont}</textarea>

	<input id="bullCtFncId" name="bullCtFncId" type="hidden" value="${bullCtFncId}"/>
	<input id="bullCtFncUid" name="bullCtFncUid" type="hidden" value="${bullCtFncUid}"/>
	<input id="bullCtFncPid" name="bullCtFncPid" type="hidden" value="${bullCtFncPid}"/>
	<input id="bullCtFncLocStep" name="bullCtFncLocStep" type="hidden" value="${bullCtFncLocStep}"/>
	<input id="bullCtFncOrdr" name="bullCtFncOrdr" type="hidden" value="${bullCtFncOrdr}"/>

    <div class="entryzone">
        <div class="entryarea">
        <dl>
        <dd class="etr_tit">
			<u:msg titleId="bb.jsp.setBull.${fnc}.title" var="title" alt="게시물 등록/게시물 수정" />
			<c:if test="param.bullPid != null && param.bullPid != ''">
				<u:msg titleId="bb.jsp.setReply.title" var="title" alt="게시물 답변" />
			</c:if>
			${title}
        </dd>

        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.subj" alt="제목" /></dd>
        <dd class="etr_input"><div class="etr_inputin"><input type="text" id="subj" name="subj" class="etr_iplt" value="${ctBullMastBVo.subj}" /></div></dd>

		
		<c:if test="${bullPid == null || bullPid == '' }">
        <dd class="etr_bodytit"><u:msg titleId="cols.bullExprDt" alt="게시완료일" /></dd>
        <dd class="etr_select">
            <div class="etr_calendar">
            	<input id="bullExprHm" name="bullExprHm" value="${bullExprHm}" type="hidden" />
             	<input id="bullExprYmd" name="bullExprYmd" value="${bullExprYmd}" type="hidden" />
             	<input type="hidden" name="bullExprDt" id="bullExprDt" value="${bbBullLVo.bullExprDt}" />
            	<div class="etr_calendarin">
                <dl>
                <dd class="ctxt" onclick="fnCalendar('bullExprYmd','','m','bullExprHm','onBullRezvDayChange');"><span id="bullExprYmd">${bullExprYmd} ${bullExprHm}</span></dd>
                <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                <dd class="cbtn" onclick="fnCalendar('bullExprYmd','','m','bullExprHm','onBullRezvDayChange');"></dd>
                </dl>
                </div>
             </div> 
        </dd> 
		</c:if>

		</dl>
		</div>
	</div>

    <div class="blankzone">
        <div class="blank25"></div>
        <div class="line1"></div>
        <div class="line8"></div>
        <div class="line1"></div>
        <div class="blank25"></div>
    </div>

<%
	com.innobiz.orange.web.ct.vo.CtBullMastBVo ctBullMastBVo = (com.innobiz.orange.web.ct.vo.CtBullMastBVo)request.getAttribute("ctBullMastBVo");
	if(ctBullMastBVo != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(ctBullMastBVo.getCont());
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", ctBullMastBVo.getCont());
		}
	}
%>
    <div class="entryzone">
        <div class="entryarea">
        <dl>

         <dd class="etr_bodytit">
         	<div class="icotit_dot"><u:msg titleId="cols.cont" alt="내용" /></div>
            <div class="icoarea">
            <dl>
            <dd class="btn" onclick="$m.openEditor();"><u:msg titleId="mcm.title.editCont" alt="내용편집" /></dd>
            </dl>
            </div>
         </dd>
         <dd class="etr_input"><div class="etr_bodyline editor" id="bodyHtmlArea"><u:out value="${_bodyHtml}" type="noscript" /></div></dd>

		</dl>
		</div>
	</div>
	
	
<c:set var="id" value="ctfiles"/>
<script type="text/javascript">
//<![CDATA[
function addFile() {
	$('#${id}_file').trigger('click');
}       
           
function setFile(obj) {
	var va = $(obj).val();
	if (va != '') {
		var $last = $('.filearea:last');
		var $clone = $last.clone();
		$last.removeClass('tmp');
		$last.show();
		var p = va.lastIndexOf('\\');
		if (p > 0) va = va.substring(p + 1);
		$last.find('#${id}_fileView').text(va).removeAttr('id');
		$last.find('input[name="${id}_valid"]').val('Y');
		
		$area = $(obj).parents('.${id}attachBtnArea');
		var $cloneArea = $('#${id}attachBtnAreaParent').clone();
		$cloneArea.replaceWith( $cloneArea.clone(true) );
		$cloneArea.insertBefore($area);

		var id = $area.attr('id');
		var fileSeq = id.substring(id.lastIndexOf('_')+1);
		$last.find('input[name="${id}_fileSeq"]').val(fileSeq);
		fileSeq++;
		$cloneArea.attr('id','${id}attachBtnArea_'+fileSeq);
		$clone.insertAfter($last);
	}
}      
           
function delFile(checkedObj) {
	$m.msg.confirmMsg("cm.cfrm.del", null, function(result){ <% // cm.cfrm.del=삭제하시겠습니까 ? %>
		if(result){
			$area = $(checkedObj).parents('.filearea');
			if ($area.hasClass('tmp') == false) {
				$area.find('input[name="${id}_useYn"]').val('N');
				var fileSeq = $area.find('input[name="${id}_fileSeq"]').val();
				if(fileSeq != undefined){
					$('#${id}attachBtnArea_'+fileSeq).remove();
				}
				$area.hide();
			}
		}
	});
}   
//]]>
</script>

            <!--entryzone S-->
            <div class="entryzone">
                <div class="blank20"></div> 
                <div class="entryarea">
                <dl>
                <dd class="etr_bodytit">
                    <div class="icotit_dot"><u:msg titleId="cols.attFile" alt="첨부파일" /></div>
                    <div class="icoarea">
                    <dl>
                    <dd class="btn" onclick="addFile();"><u:msg titleId="cm.btn.fileAtt" alt="파일첨부"  /></dd>
                    </dl>
                    </div>
                </dd>
                </dl>
                </div>
            </div>
            <!--//entryzone E-->
            
            <!--attachzone S-->
            <div class="attachzone">
            <div class="attacharea">
            
				<div id="${id}attachBtnArea_0" class="${id}attachBtnArea" style="display:none"><input type="file" id="${id}_file" name="${id}_file" onchange="setFile(this);" /></div>
				<div id="${id}attachBtnAreaParent" class="${id}attachBtnArea" style="display:none"><input type="file" id="${id}_file" name="${id}_file" onchange="setFile(this);" /></div>
				<c:if test="${fn:length(fileVoList) > 0}">
					<c:forEach items="${fileVoList}" var="fileVo" varStatus="status">
					<c:set var="fileSizeKB" value="${fileVo.fileSize/1024 }"/>
					<c:set var="fileSizeMB" value="${fileVo.fileSize/(1024*1024) }"/>
					<c:set var="fileSizeTemp" value="${fileSizeKB+(1-(fileSizeKB%1))%1}"/>
					<u:set var="fileSize" test="${fileSizeTemp > 999 }" value="${fileSizeMB+(1-(fileSizeMB%1))%1 }" elseValue="${fileSizeTemp }"/>
					<u:set var="fileSizeUnit" test="${fileSizeTemp > 999 }" value="MB" elseValue="KB"/>
					<u:set var="ctSendYn" test="${ctSendYn == 'Y' }" value="Y" elseValue="N"/>
		                <div class="attachin filearea">
		                    <div class="attach" onclick="javascript:;">
		                        <div class="btn"></div>
		                        <div class="txt">${fileVo.dispNm}(<fmt:formatNumber value="${fileSize}" type="number"/>${fileSizeUnit })</div>
		                    </div>
		                    <div class="delete" onclick="javascript:delFile(this);"></div>
							<input type="hidden" name="${id}_fileId" value="${fileVo.fileId}" />
							<input type="hidden" name="${id}_valid" value="${ctSendYn}" />
							<input type="hidden" name="${id}_useYn" value="${fileVo.useYn}" />
							<input type="hidden" name="${id}_ctSendYn" value="${ctSendYn}" />
		                </div>
					</c:forEach>
				</c:if>

				<div class="attachin filearea tmp" style="display:none">
				<div class="attach" >
				    <div class="btn"></div>
				    <div class="txt"><span id="${id}_fileView"></span></div>
				</div>
				<div class="delete" onclick="javascript:delFile(this);"></div>
				<input type="hidden" name="${id}_fileSeq" value="" />
				<input type="hidden" name="${id}_fileId" value="" />
				<input type="hidden" name="${id}_valid" value="N" />
				<input type="hidden" name="${id}_useYn" value="Y" />
				<input type="hidden" name="${id}_ctSendYn" value="" />
				</div>

            </div>
            </div>
            <!--//attachzone E-->

	
	
    <div class="blank25"></div>
    <div class="btnarea">
        <div class="size">
            <dl>
            <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
            <dd class="btn" onclick="saveBull();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
         </dl>
        </div>
    </div>

	
	</form>
	
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
 </section>
 <div id="lobHandlerArea" style="display:none;"><c:if test="${!empty _bodyHtml }">${_bodyHtml}</c:if><c:if test="${!empty lobHandler }"><u:clob lobHandler="${lobHandler }"/></c:if></div>
