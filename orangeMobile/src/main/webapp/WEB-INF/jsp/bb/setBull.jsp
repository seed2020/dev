<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><fmt:formatDate var="today" value="<%=new Date() %>" type="date" pattern="yyyy-MM-dd"/>
<fmt:formatDate var="todaytime" value="<%=new Date() %>" type="date" pattern="yyyy-MM-dd HH:mm" />
<u:secu auth="W"><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<u:set test="${!empty param.bullId && listPage != 'listTmpBull'}" var="fnc" value="mod" elseValue="reg" />
<u:out value="${bbBullLVo.bullRezvDt}" type="date" var="bullRezvYmd" />
<u:out value="${bbBullLVo.bullRezvDt}" type="hm" var="bullRezvHm" />
<u:out value="${bbBullLVo.bullExprDt}" type="date" var="bullExprYmd" />
<u:out value="${bbBullLVo.bullExprDt}" type="hm" var="bullExprHm" />
<script type="text/javascript">
//<![CDATA[
<% // [버튼클릭] - 사용자삭제  %>
function selectListDel(obj, containerId){
	if(obj===null) $('#'+containerId).html('');
	else $(obj).closest('div.ubox').remove();
};<% // [버튼클릭] - 사용자추가  %>
function addRowUser(containerId){
	var param = {};
	var idVaList=getUserList(containerId, 'userUid', 'U');
	
	$m.user.selectUsers(param, function(arr){
		if(arr==null || arr.length==0) return false;
		setRowUser(containerId, arr, ["userUid","rescNm"], idVaList);
	});
	
};<% // 다중 부서 선택 %>
function addRowDept(containerId){
	var data=[];
	var idVaList=getUserList(containerId, 'orgId', 'D');
	
	$('#'+containerId+' input[id="idVa"]').each(function(){
		user=JSON.parse($(this).val());
		data.push(JSON.parse('{"orgId":"'+user.orgId+'","rescNm":"'+user.rescNm+'"}'));
	});
	
	$m.org.selectOrgs({selected:data}, function(arr){
		if(arr!=null)
			setRowDept(containerId, arr, ["orgId","rescNm"], idVaList);
	});
}
<% // 사용자추가  %>
function setRowUser(containerId, arr, attrs, idVaList){	
	var buffer=[];
	var obj;
	var maxLen=50;
	var len=idVaList==null ? 0 : idVaList.length;
	arr.each(function(index, userVo){
		if($.inArray(userVo.userUid, idVaList)!=-1)
			return true;
		if(len>=maxLen){
			alertMsg('bb.msg.not.maxInsert',[maxLen]);
			return false;
		}
		obj = {};
		attrs.each(function(index, attr){
			obj[attr] = userVo[attr];
		});
		buffer.push(createUbox(userVo.userUid, userVo.rescNm, 'U', getJsonVa(obj, ["userUid", "rescNm"])));
		len++;
	});
	if(buffer.length>0){
		$('#'+containerId).append(buffer.join(''));
	}
}<% // 부서추가  %>
function setRowDept(containerId, arr, attrs, idVaList){	
	var buffer=[];
	var obj;
	var maxLen=50;
	var len=idVaList==null ? 0 : idVaList.length;
	arr.each(function(index, orgVo){
		if($.inArray(orgVo.orgId, idVaList)!=-1)
			return true;
		if(len>=maxLen){
			alertMsg('bb.msg.not.maxInsert',[maxLen]);
			return false;
		}
		obj = {};
		attrs.each(function(index, attr){
			obj[attr] = orgVo[attr];
		});
		buffer.push(createUbox(orgVo.orgId, orgVo.rescNm, 'D', getJsonVa(obj, ["orgId", "rescNm"])));
	});
	if(buffer.length>0){
		$('#'+containerId).append(buffer.join(''));
	}
}<% // 사용자 목록 생성  %>
function createUbox(idVa, text, pichTypCd, jsonVa){
	var buffer=[];
	buffer.push('<div class="ubox"><dl>');
	buffer.push('<dd class="title">');
	if(pichTypCd=='U') buffer.push('<a href="javascript:viewUserPop(\''+idVa+'\');">');
	buffer.push(text);
	if(pichTypCd=='U') buffer.push('</a>');
	//buffer.push(jsonVa.replaceAll('"','\\"'));
	buffer.push(jsonVa);
	buffer.push('</dd>');
	buffer.push('<dd class="btn" onclick="selectListDel(this);"></dd>');
	buffer.push('</dl></div>');
	return buffer.join('');
}<% // 보고대상 목록  %>
function getUserList(containerId, chkId){
	var idVaList=[], user;
	$('#'+containerId+' input[id="idVa"]').each(function(){
		user=JSON.parse($(this).val());
		idVaList.push(user[chkId]);	
	});
	return idVaList;
	
}<% // JSON 형식으로 변경  %>
function getJsonVa(obj, attrs){
	var buffer = new StringBuffer();
	buffer.append("<input type='hidden' id='idVa' value='{");
	attrs.each(function(index, attr){
		if(index!=0) buffer.append(', ');
		var va = obj[attr].replaceAll('"','\\"');
		buffer.append('"').append(attr).append('":"').append(escapeValue(va)).append('"');
	});
	buffer.append("}' />");
	return buffer.toString();
}<% // JSON 형식으로 변경  %>
function setSelectListToJson(){
	var buffer, id, vaList;
	var maxByte;
	var errorList=[];
	$('div[id^="selectListArea"]').each(function(index){
		vaList='';
		maxByte=$(this).attr('data-maxByte');
		if(maxByte===undefined) return true;
		$(this).find('input[id="idVa"]').each(function(index){
			if(index>0) vaList+=',';
			vaList+=$(this).val();
		});		
		id=$(this).attr('id');
		id=id.substring(id.indexOf('_')+1);
		if(id===undefined || id=='') return true;
		buffer=[];
		if(vaList!=''){			
			buffer.push('[');
			buffer.push(vaList);
			buffer.push(']');
		}
		if (isInUtf8Length(buffer.join(''), 0, maxByte) > 0) {
			errorList.push({title:$(this).attr('data-title'), maxByte:maxByte});
		}else{
			$(this).find("input[name='"+id+"']").remove();
			$(this).appendHidden({name:id,value:buffer.join('')});	
		}
	});
	return errorList;
}  
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


<% // 예약일시 세팅 %>
function setRezvDt() {
	if ($('#bullRezvYmd').val() != '' && $('#bullRezvYnChk').is(':checked')) {
		$('#bullRezvDt').val($('#bullRezvYmd').val() + ' ' + $('#bullRezvHm').val() + ':00');
	} else {
		$('#bullRezvDt').val('');
	}
}
<% // 완료일시 세팅 %>
function setExprDt() {
	if ($('#bullExprYmd').val() != '') {
		$('#bullExprDt').val($('#bullExprYmd').val() + ' ' + $('#bullExprHm').val() + ':00');
	}
}
<% // 게시대상 세팅 %>
function setBbTgt() {
	$('#tgtDeptYn').val($("#bbTgtDeptHidden input[name='orgId']").length > 0 ? 'Y' : 'N');
	$('#tgtUserYn').val($("#bbTgtUserHidden input[name='userUid']").length > 0 ? 'Y' : 'N');
}

function fnSetCatId(cd)
{
	$('#catId').val(cd);
	$('.schTxt2 span').text($(".schOpnLayer2 dd[data-schCd='"+cd+"']").text());
	$('.schOpnLayer2').hide();
}


function fnSetExtSelect(colm, cd)
{
	$('#'+colm).val(cd);
	$('.colmTxt'+colm+' span').text($(".colmLayer"+colm+" dd[data-schCd='"+cd+"']").text());
	$('.colmLayer'+colm).hide();
}

var holdHide = false, holdHide2 = false, holdHideExt = false;
$(document).ready(function() {
	fnSetCatId('${bbBullLVo.catId}');
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".schOpnLayer1").hide();
		if(holdHide2) holdHide2 = false;
		else $(".schOpnLayer2").hide();
		if(holdHideExt) holdHideExt = false;
		else $("div[class|='etr_open2 colmLayer']").hide();
	});
	<c:if test="${colmMap.cont.readDispYn eq 'Y' }">
	/* var bodyHtml = $("#lobHandlerArea").html();
	if(bodyHtml!=''){
		setEditHtml(bodyHtml);
	} */
	$layout.adjustBodyHtml('bodyHtmlArea');
	</c:if>
});

function saveBull() {
	<c:if test="${baBrdBVo.discYn != 'Y'}">
	$('#bullStatCd').val('B');
	</c:if>
	<c:if test="${baBrdBVo.discYn == 'Y'}">
	$('#bullStatCd').val('S');
	</c:if>
	setRezvDt();
	setExprDt();
	//setBbTgt();
	<c:if test="${colmMap.subj.readDispYn eq 'Y' }">
	if($("#subj").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [제목] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.subj"], function(){
			$("#setBullForm input[name='subj']").focus();
		});
		return;
	}
	</c:if>
	if (validator.validate('setBullForm') && validateEtc('setBullForm')) {
		var errorList=setSelectListToJson();
		if(errorList.length>0){
			var buffer=[];
			<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
			for(var i=0;i<errorList.length;i++){
				buffer.push($m.msg.callMsg('cm.input.check.maxbyte', [errorList[i].title,errorList[i].maxByte]));
			}
			alert(buffer.join('\n'));
			return;
		}
		$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
			if(result){
				var $form = $('#setBullForm');
				$m.nav.post($form);
			}
		});
	}
	
}

<% // [유효성검증:기타 - 체크박스,라디오,사용자,부서] %>
function validateEtc(id){
	var isValidate=true;
	var chkList=$('#'+id+' div[class="mandatory"]');
	console.log(chkList.length);
	if(chkList.length>0){
		var type;
		$.each(chkList, function(){
			type=$(this).attr('data-validateType');
			if(type){
				if(type=='check' || type=='radio'){
					$name=$(this).attr('data-name');
					if($name)
						$chkLen=$(this).find('input[name="'+$name+'"]:checked').length;
					else
						return true;
				}else if(type=='calendar'){
					$name=$(this).attr('data-name');
					if($name)
						$chkLen=$(this).find('input[name="'+$name+'"]').val()=='' ? 0 : 1;
					else
						return true;
				}else if(type=='ubox'){
					$chkLen=$(this).find('div[class="ubox"]').length;
				}else return true;
				
				if($chkLen==0){
					isValidate=false;
					$title=$(this).attr('data-title');
					if($title){
						alert($m.msg.callMsg('cm.input.check.mandatory',[$title]));
						return false;
					}else return true;
				}
				
			}
		});
	}
	return isValidate;
}

function fnCalendar(id,opt,hm,hmId,handler){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		url:'/cm/util/getCalendarPop.do?&id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&hm='+hm+'&hmId='+hmId+'&hmVal='+$('#'+hmId).val()+'&handler='+handler,
	});
}

function fnbullRezvYnChk(){
	if($('dd#bullRezvYnChkArea').attr("class") == "check"){ 
		$('.bullRezvCalLayer').show();
		$('#bullRezvYn').val('Y');
	}else{
		$('.bullRezvCalLayer').hide();
		$('#bullRezvYn').val('N');
	}
};

function getEditHtml(){
	return $('#bodyHtmlArea').html();
};

function setEditHtml(editHtml){
	$('#bodyHtmlArea').html(editHtml);
	$('#cont').val(editHtml);
};

function setFileInfo(id , va){
	$('#'+id+'Area').find('.filearea').each(function(){
		if(!$(this).hasClass('tmp')){
			$(this).remove();
		}
	});
	if(va == null ) {
		resetFileTag(id);
		return;
	};
	
	var $last = $('#'+id+'Area .filearea:last');
	var $clone = $last.clone();
	$last.removeClass('tmp');
	$last.show();
	
	var p = va.lastIndexOf('\\');
	if (p > 0) va = va.substring(p + 1);
	$last.find('#'+id+'_fileView').text(va).removeAttr('id');
	$clone.insertAfter($last);	
};

//file-tag 에서 사용
var gFileTagMap = {};
function resetFileTag(id){
	var html = gFileTagMap[id];
	if(html!=null){
		var $file = $("#"+id+"File");
		$file.before(html);
		$file.remove();
		$("#"+id+"FileView").val('');
	}
};
function setFileTag(id, value, handler, exts){
	var viewId = id+'FileView';
	if(value==null) value = "";
	else {
		var p = value.lastIndexOf('\\');
		if(p>0) value = value.substring(p+1);
	}
	var $view = $("#"+viewId);
	var oldValue = $view.val();
	$view.val(value);
	
	if(exts!=null && exts!="" && value!=""){
		if(oldValue!=value){//IE에서 클릭했을때 이벤트 타는 버그 고침
			var va = value.toLowerCase();
			var matched = false;
			extArr = exts.toLowerCase().split(",");
			extArr.each(function(index, ext){
				if(va.endsWith("."+ext.trim())){
					matched = true;
					return false;
				}
			});
			if(!matched){
				$m.msg.alertMsg("cm.msg.attach.not.support.ext",[exts]);
				resetFileTag(viewId.substring(0, viewId.length-8));
				if(handler!=null) handler(viewId.substring(0, viewId.length-8), null);
			} else {
				if(handler!=null) handler(viewId.substring(0, viewId.length-8), value);
			}
		}
	} else {
		if(handler!=null) handler(viewId.substring(0, viewId.length-8), value);
	}
};

function delFileInfo(checkedObj, id) {
	$m.msg.confirmMsg("cm.cfrm.del", null, function(result){ <% // cm.cfrm.del=삭제하시겠습니까 ? %>
		if(result){
			$area = $(checkedObj).parents('.filearea');
			if ($area.hasClass('tmp') == false) {
				$area.remove();
			}
			resetFileTag(id);
		}
	});
};
<% // 날짜 및 시간 변경시 히든값 변경 %>
function chnDateTime(date, id){
	if(id!=undefined){
		id=id.replace('Ymd','');
		$('#'+id).val(date+':00');
	}
}
//]]>
</script>
<div class="btnarea">
    <div class="size">
        <dl>
        	<dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>           	 
           	<c:if test="${writeAuth == 'Y'}">
            <dd class="btn" onclick="saveBull();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
            </c:if>
     </dl>
    </div>
</div>
<section>

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<form id="setBullForm" name="setBullForm" enctype="multipart/form-data" action="/bb/${action}Post.do?menuId=${menuId}&brdId=${baBrdBVo.brdId}">
	<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
	<input type="hidden" id="listPage" name="listPage" value="/bb/${listPage}.do?${paramsForList}" />
	<c:if test="${fn:startsWith(viewPage,'list')}"><input type="hidden" id="viewPage" value="./${listPage}.do?${paramsForList}" /></c:if>
	<c:if test="${!fn:startsWith(viewPage,'list')}"><input type="hidden" id="viewPage" name="viewPage" value="/bb/${viewPage}.do?${params}&bullId=${param.bullId}" /></c:if>
	<input type="hidden" id="viewPage" name="viewPage" value="/bb/${viewPage}.do?${params}&bullId=${param.bullId}" />
	<input type="hidden" id="brdId" name="brdId" value="${baBrdBVo.brdId}" />
	<input type="hidden" id="bullId" name="bullId" value="${param.bullId}" />
	<input type="hidden" id="bullPid" name="bullPid" value="${param.bullPid}" />
	<input type="hidden" id="bullStatCd" name="bullStatCd" value="B" />
	<input type="hidden" id="bullRezvDt" name="bullRezvDt" value="${bbBullLVo.bullRezvDt}" />
	<input type="hidden" id="tgtDeptYn" name="tgtDeptYn" value="${bbBullLVo.tgtDeptYn}" />
	<input type="hidden" id="tgtUserYn" name="tgtUserYn" value="${bbBullLVo.tgtUserYn}" />
	
	<textarea id="cont" name="cont" style="display:none">${bbBullLVo.cont}</textarea>
	
	<input type="hidden" name="catId" id="catId" value="" />
	
	<div id="bbTgtDeptHidden">
	<c:if test="${bullTgtDeptVoList != null}">
		<c:forEach items="${bullTgtDeptVoList}" var="baBullTgtDVo" varStatus="status">
		<input name="orgId" type="hidden" value="${baBullTgtDVo.tgtId}" />
		<input name="withSubYn" type="hidden" value="${baBullTgtDVo.withSubYn}" />
		</c:forEach>
	</c:if></div>
	<div id="bbTgtUserHidden">
	<c:if test="${bullTgtUserVoList != null}">
		<c:forEach items="${bullTgtUserVoList}" var="baBullTgtDVo" varStatus="status">
		<input name="userUid" type="hidden" value="${baBullTgtDVo.tgtId}" />
		</c:forEach>
	</c:if></div>
	

   
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
        <c:if test="${baBrdBVo.optMap.privUseYn eq 'Y' }">
        <dd class="etr_input"><div class="etr_ipmany"><dl><m:check type="checkbox" id="privYn" name="privYn" inputId="privYn" value="Y" checked="${!empty privYn && privYn eq 'Y' }" titleId="bb.msg.save.priv" /></dl></div>
		</dd></c:if>
		<c:if test="${colmMap.subj.readDispYn eq 'Y' }"><u:set var="mandatoryYn" test="${!empty colmMap.subj.mandatoryYn }" value="${colmMap.subj.mandatoryYn }" elseValue="N"/>
        <dd class="etr_bodytit${mandatoryYn eq 'Y' ? '_asterisk' : '' }"><u:msg titleId="cols.subj" alt="제목" /></dd>
        <dd class="etr_input"><div class="etr_inputin"><m:input type="text" id="subj" name="subj" className="etr_iplt" value="${bbBullLVo.subj }" mandatory="${mandatoryYn }" maxByte="240"/></div></dd>
        </c:if>
		
		<c:if test="${baBrdBVo.catYn == 'Y' && param.bullPid == null && !empty colmMap.catId && colmMap.catId.readDispYn eq 'Y'}">
		<dd class="etr_bodytit"><u:msg titleId="cols.cat" alt="카테고리" /></dd>
		<dd class="etr_select">
             <div class="etr_open1 schOpnLayer2" style="display:none;">
                <div class="open_in1">
                    <div class="open_div">
                    <dl>
                    <dd class="txt" onclick="javascript:fnSetCatId('');" data-schCd=""><u:msg titleId="cm.option.all" alt="전체" /></dd>
					<dd class="line"></dd>
                    <c:forEach items="${baCatDVoList}" var="catVo" varStatus="status">
				        <dd class="txt" onclick="javascript:fnSetCatId('${catVo.catId}');" data-schCd="${catVo.catId}">${catVo.rescNm}</dd>
				        <dd class="line"></dd>
					</c:forEach>
                 </dl>
                    </div>
                </div>
            </div>
            
            <div class="etr_ipmany">
                <div class="select_in1 schTxt2" onclick="holdHide2 = true;$('.schOpnLayer2').toggle();">
                <dl>
                <dd class="select_txt"><span></span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </div>
        </dd>
		</c:if>
		<c:if test="${baBrdBVo.optMap.bbOptYn eq 'Y' }">
        <dd class="etr_bodytit"><u:msg titleId="bb.cols.bbOpt" alt="게시옵션" /></dd>
        <dd class="etr_input">
			<div class="etr_ipmany">
                <dl>
					<u:set var="checked" test="${bbBullLVo.secuYn == 'Y'}" value="true" elseValue="false"/>
					<m:check type="checkbox" id="secuYn" name="secuYn" inputId="secuYn" value="Y" checked="${checked }" titleId="bb.option.secu" />
					
					<u:set var="checked" test="${bbBullLVo.ugntYn == 'Y'}" value="true" elseValue="false"/>
					<m:check type="checkbox" id="ugntYn" name="ugntYn" inputId="ugntYn" value="Y" checked="${checked }" titleId="bb.option.ugnt" />
					
					<c:if test="${(param.bullPid == null || param.bullPid == '') && bbBullLVo.bullPid == null}">
					<u:set var="checked" test="${bbBullLVo.notcYn == 'Y'}" value="true" elseValue="false"/>
					<m:check type="checkbox" id="notcYn" name="notcYn" inputId="notcYn" value="Y" checked="${checked }" titleId="bb.option.notc" />
					</c:if>
					
                </dl>
            </div>
        </dd>
		</c:if>
		
		<u:set var="rezvYn" test="${!empty colmMap.bullRezvDt && colmMap.bullRezvDt.readDispYn eq 'Y'}" value="Y" elseValue="N"/>
		<u:set var="exprYn" test="${baBrdBVo.kndCd == 'R' && param.bullPid == null && bbBullLVo.bullPid == null && !empty colmMap.bullExprDt && colmMap.bullExprDt.readDispYn eq 'Y'}" value="Y" elseValue="N"/>
		<c:if test="${rezvYn eq 'Y'}">
		
		<dd class="etr_bodytit"><u:msg titleId="cols.bullRezvDt" alt="게시예약일" /></dd>
		<c:if test="${bullRezvDtYn == false}">
			<c:if test="${bbBullLVo.bullRezvYn == 'Y'}"><dd class="etr_bodytit"><u:out value="${bbBullLVo.bullRezvDt}" type="longdate" /></dd></c:if>
		</c:if>
		<c:if test="${bullRezvDtYn == true}">
		<input type="hidden" id="bullRezvYn" name="bullRezvYn" value="${bullRezvChecked == true ? 'Y' : 'N' }" />
        <dd class="etr_input">
			<div class="etr_ipmany">
                <dl>
					<u:set var="checked" test="${bullRezvChecked == true}" value="true" elseValue="false"/>
					<m:check type="checkbox" id="bullRezvYnChkArea" name="bullRezvYnChk" inputId="bullRezvYnChk" value="Y" checked="${checked }" titleId="bb.cols.bullRezv" onclick="fnbullRezvYnChk();" />
                </dl>
            </div>
        </dd>
		</c:if>
        <dd class="etr_select bullRezvCalLayer" style="display:none">
             <div class="etr_calendar">
             	<input id="bullRezvHm" name="bullRezvHm" value="${bullRezvHm}" type="hidden" />
             	<input id="bullRezvYmd" name="bullRezvYmd" value="${bullRezvYmd}" type="hidden" />
             	<div class="etr_calendarin">
                <dl>
                <dd class="ctxt" onclick="fnCalendar('bullRezvYmd','','m','bullRezvHm','onBullRezvDayChange');"><span id="bullRezvYmd">${bullRezvYmd} ${bullRezvHm}</span></dd>
                <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                <dd class="cbtn" onclick="fnCalendar('bullRezvYmd','','m','bullRezvHm','onBullRezvDayChange');"></dd>
                </dl>
                </div>
             </div>
        </dd> 
        </c:if>
        <c:if test="${exprYn eq 'Y'}">
        <dd class="etr_bodytit"><u:msg titleId="cols.bullExprDt" alt="게시완료일" /></dd>
        <dd class="etr_select">
             <div class="etr_calendar">
             	<input id="bullExprHm" name="bullExprHm" value="${bullExprHm}" type="hidden" />
             	<input id="bullExprYmd" name="bullExprYmd" value="${bullExprYmd}" type="hidden" />
             	<input type="hidden" name="bullExprDt" id="bullExprDt" value="${bbBullLVo.bullExprDt}" />
             	<div class="etr_calendarin">
                <dl>
                <dd class="ctxt" onclick="fnCalendar('bullExprYmd','','m','bullExprHm');"><span id="bullExprYmd">${bullExprYmd} ${bullExprHm}</span></dd>
                <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                <dd class="cbtn" onclick="fnCalendar('bullExprYmd','','m','bullExprHm');"></dd>
                </dl>
                </div>
             </div>
        </dd> 
		</c:if>
        
		<c:set var="map" value="${bbBullLVo.exColMap}" scope="request" />
	<c:forEach items="${baColmDispDVoList}" var="baColmDispDVo" varStatus="status">
		<c:set var="colmVo" value="${baColmDispDVo.colmVo}" />
		<c:set var="colmNm" value="${colmVo.colmNm.toLowerCase()}" />
		<c:set var="colmTyp" value="${colmVo.colmTyp}" />
		<c:set var="colmTypVal" value="${colmVo.colmTypVal}" />
	<c:if test="${baColmDispDVo.readDispYn == 'Y' && colmVo.exColmYn == 'Y'}">
	<u:set var="mandatoryYn" test="${!empty baColmDispDVo.mandatoryYn }" value="${baColmDispDVo.mandatoryYn }" elseValue="N"/>
	<c:if test="${colmTyp ne 'USER' && colmTyp ne 'DEPT'}"><dd class="etr_bodytit${mandatoryYn eq 'Y' ? '_asterisk' : '' }"><span>${colmVo.rescNm}</span></dd></c:if>
	<c:if test="${colmTyp == 'USER' || colmTyp == 'DEPT'}"><dd class="etr_bodytit${mandatoryYn eq 'Y' ? '_asterisk' : '' }">
			<div class="icotit_dot${mandatoryYn eq 'Y' ? '_asterisk' : '' }">${colmVo.rescNm}</div>
			<div id="deptBtnArea" class="icoarea" style="left:40%;">
				<dl>
				<c:if test="${colmTyp == 'USER' }"><dd class="btn" onclick="addRowUser('selectListArea_${colmNm }');"><u:msg titleId="cm.btn.add" alt="추가"/></dd>
				<dd class="btn" onclick="selectListDel(null, 'selectListArea_${colmNm }');"><u:msg titleId="cm.btn.allDel" alt="전체삭제"/></dd></c:if>
				<c:if test="${colmTyp == 'DEPT' }"><dd class="btn" onclick="addRowDept('selectListArea_${colmNm }');"><u:msg titleId="cm.btn.add" alt="추가"/></dd>
				<dd class="btn" onclick="selectListDel(null, 'selectListArea_${colmNm }');"><u:msg titleId="cm.btn.allDel" alt="전체삭제"/></dd></c:if>
				</dl>
			</div>
			</dd></c:if>
			
		<!-- 확장컬럼의 데이터 최대길이(byte) [input,textarea만 해당] -->
		<u:set var="dataMaxByte" test="${(colmTyp == 'TEXT' || colmTyp == 'TEXTAREA' || colmTyp == 'USER' || colmTyp == 'DEPT') && fn:startsWith(colmVo.dataTyp, 'VARCHAR') && !empty colmVo.colmLen }" value="${colmVo.colmLen }" elseValue="100"/>
		<c:if test="${colmTyp == 'TEXT'}">
			<dd class="etr_input"><div class="etr_inputin"><m:input type="text" id="${colmNm}" name="${colmNm}" className="etr_iplt" value="${bbBullLVo.getExColm(colmVo.colmNm)}" title="${colmVo.rescNm}" maxByte="${dataMaxByte }" mandatory="${mandatoryYn }"/></div></dd>
		</c:if>
		<c:if test="${colmTyp == 'TEXTAREA'}">
			<dd class="etr_input"><div class="etr_textareain"><m:textarea rows="3" id="${colmNm}" name="${colmNm}" className="etr_ta" value="${bbBullLVo.getExColm(colmVo.colmNm)}" title="${colmVo.rescNm}" maxByte="${dataMaxByte }" mandatory="${mandatoryYn }"/></div></dd>
		</c:if>
		<c:if test="${colmTyp == 'PHONE'}">
			<dd class="etr_input"><div class="etr_inputin"><m:input type="text" id="${colmNm}" name="${colmNm}" className="etr_iplt" value="${bbBullLVo.getExColm(colmVo.colmNm)}" title="${colmVo.rescNm}" maxByte="${dataMaxByte }" mandatory="${mandatoryYn }"/></div></dd>
		</c:if>
		<c:if test="${colmTyp == 'CALENDAR'}">	
	        <dd class="etr_select"><div <c:if test="${mandatoryYn eq 'Y' }">class="mandatory" data-validateType="calendar" data-name="${colmNm }" data-title="${colmVo.rescNm}"</c:if>>
	             <div class="etr_calendar">
	             	<u:set var="dateVa" test="${empty param.bullId && empty bbBullLVo.getExColm(colmVo.colmNm) && !empty colmTypVal && colmTypVal eq 'today'}" value="${todaytime }" elseValue="${bbBullLVo.getExColm(colmVo.colmNm) }"/>
	             	<input id="${colmNm}" name="${colmNm}" value="${dateVa}" type="hidden" />
	                 <div class="select_in1" onclick="fnCalendar('${colmNm}','','','');">
	                 <dl>
	                 <dd class="ctxt"><span id="${colmNm}">${dateVa}</span></dd>
	                 <dd class="cbtn"></dd>
	                 </dl>
	                 </div>
	             </div></div>
	        </dd> 
		</c:if>
		<c:if test="${colmTyp == 'CALENDARTIME'}">
        	<dd class="etr_select"><div <c:if test="${mandatoryYn eq 'Y' }">class="mandatory" data-validateType="calendar" data-name="${colmNm }" data-title="${colmVo.rescNm}"</c:if>>
             <div class="etr_calendar">
             	<u:set var="dateVa" test="${empty param.bullId && empty bbBullLVo.getExColm(colmVo.colmNm) && !empty colmTypVal && colmTypVal eq 'today'}" value="${todaytime }" elseValue="${bbBullLVo.getExColm(colmVo.colmNm) }"/>
             	<u:out value="${dateVa}" type="date" var="dateYmd" />
				<u:out value="${dateVa}" type="hm" var="dateTime" />
             	<input id="${colmNm}Hm" value="${dateTime}" type="hidden" />
             	<input id="${colmNm}Ymd" value="${dateYmd}" type="hidden" />             	
             	<input type="hidden" name="${colmNm}" id="${colmNm}" value="${dateVa}" />
             	<div class="etr_calendarin">
                <dl>
                <dd class="ctxt" onclick="fnCalendar('${colmNm}Ymd','','m','${colmNm}Hm', 'chnDateTime');"><span id="${colmNm}Ymd"><u:out value="${dateVa}" type="longdate" /></span></dd>
                <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                <dd class="cbtn" onclick="fnCalendar('${colmNm}Ymd','','m','${colmNm}Hm', 'chnDateTime');"></dd>
                </dl>
                </div>
             </div></div>
        </dd> 
		</c:if>
		<c:if test="${fn:startsWith(colmTyp,'CODE')}">
			<u:set test="${cdListIndex == null}" var="cdListIndex" value="0" elseValue="${cdListIndex + 1}" />
			<c:if test="${colmTyp == 'CODE'}">				
				<input type="hidden" name="${colmNm}" id="${colmNm}" value="${bbBullLVo.getExColm(colmVo.colmNm)}"/>
				<dd class="etr_input">
					<c:set var="cdNm" />
		              <div class="etr_ipmany">
		              <dl>
		              <dd class="etr_se_lt">
		                  <div class="etr_open2 colmLayer${colmNm}" style="display:none;">
		                    <div class="open_in1">
		                        <div class="open_div">
							        <dl>
							        <c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status">
								        <dd class="txt" onclick="javascript:fnSetExtSelect('${colmNm}','${cd.cdId}');" data-schCd="${cd.cdId}">${cd.rescNm}</dd>
								        <dd class="line"></dd>
									</c:forEach>
							    	</dl>
		                        </div>
		                    </div>
		                </div>   
		                <div class="select_in1 colmTxt${colmNm}" onclick="holdHideExt = true;$('.colmLayer${colmNm}').toggle();">
		                <dl>
		                <dd class="select_txt"><span>
				        <c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status">
				        	<c:if test="${empty bbBullLVo.getExColm(colmVo.colmNm) && status.first}"><c:set var="cdNm" value="${cd.rescNm}"/></c:if>
							<c:if test="${!empty bbBullLVo.getExColm(colmVo.colmNm) && cd.cdId == bbBullLVo.getExColm(colmVo.colmNm)}"><c:set var="cdNm" value="${cd.rescNm}"/></c:if>
						</c:forEach>${cdNm }</span></dd>
		                <dd class="select_btn"></dd>
		                </dl>
		                </div>
		            </dd>
		            </dl>
		            </div>
		        </dd>				
			</c:if>
			<c:if test="${colmTyp == 'CODECHK'}"><dd class="etr_input"><div <c:if test="${mandatoryYn eq 'Y' }">class="mandatory" data-validateType="check" data-name="${colmNm }" data-title="${colmVo.rescNm}"</c:if>>
			<div class="etr_ipmany" style="display:table-cell;">
                <dl>
                	<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status"><c:set var="checked" value="N" 
				/><c:forTokens var="chkId" items="${bbBullLVo.getExColm(colmVo.colmNm)}" delims=","><c:if test="${chkId==cd.cdId }"
                		><c:set var="checked" value="Y" /></c:if></c:forTokens
                		><m:check type="checkbox" id="${colmNm }${status.index }" name="${colmNm }" inputId="${colmNm }" value="${cd.cdId}" checked="${checked=='Y' }" title="${cd.rescNm}" />
					</c:forEach>
				</dl></div></div></dd>
			</c:if>
			<c:if test="${colmTyp == 'CODERADIO'}"><dd class="etr_input"><div <c:if test="${mandatoryYn eq 'Y' }">class="mandatory" data-validateType="radio" data-name="${colmNm }" data-title="${colmVo.rescNm}"</c:if>>
			<div class="etr_ipmany" id="${colmNm }RadioArea" style="display:table-cell;">
                <dl>
                	<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status"
                	><c:set var="value" value="${bbBullLVo.getExColm(colmVo.colmNm) }"
                	/><m:check type="radio" id="${colmNm }${status.index }" name="${colmNm }" inputId="${colmNm }${status.index }" areaId="${colmNm }RadioArea" value="${cd.cdId}" checked="${(empty value && status.first) || value == cd.cdId }" title="${cd.rescNm}" />
					</c:forEach>
				</dl></div></div></dd>
			</c:if>
		</c:if>
		<c:if test="${colmTyp == 'USER' || colmTyp == 'DEPT'}">
			<dd class="etr_input"><div <c:if test="${mandatoryYn eq 'Y' }">class="mandatory" data-validateType="ubox" data-title="${colmVo.rescNm}"</c:if>
			><div class="etr_bodyline" id="selectListArea_${colmNm }" style="min-height:40px;overflow-y:auto;" data-maxByte="${dataMaxByte }" data-title="${colmVo.rescNm}"
			><u:convertMap srcId="map" attId="${colmNm }MapList" var="mapList" /><c:if test="${!empty mapList }"><c:forEach 
		var="mapVo" items="${mapList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title"><c:if test="${colmTyp == 'USER' }"><a href="javascript:viewUserPop('${mapVo.id }');">${mapVo.rescNm }</a><input type="hidden" id="idVa" value='{"userUid":"${mapVo.id }","rescNm":"${mapVo.rescNm }"}'></c:if
		><c:if test="${colmTyp == 'DEPT' }">${mapVo.rescNm }<input type="hidden" id="idVa" value='{"orgId":"${mapVo.id }","rescNm":"${mapVo.rescNm }"}'></c:if></dd>
		<dd class="btn" onclick="selectListDel(this);"></dd></dl></div>
		</c:forEach></c:if><input type="hidden" id="${colmNm }" name="${colmNm }" value="${bbBullLVo.getExColm(colmVo.colmNm)}"/></div></div></dd>
		</c:if>
	</c:if>
	</c:forEach>
		
		
		</dl>
		</div>
	</div>

<c:if test="${!empty isSns && isSns == true && baBrdBVo.optMap.snsUploadYn eq 'Y'}">	
<div class="entryzone">
	<div class="entryarea">
		<dl><dd class="etr_bodytit"><u:msg titleId="bb.sns.upload" alt="SNS 올리기" /></dd>
			<dd class="etr_input">
			<div class="etr_ipmany">
			<dl><m:sns mode="set" /></dl></div>
			</dd>
		</dl>
	</div>
</div>
</c:if>
				
    <div class="blankzone">
        <div class="blank25"></div>
        <div class="line1"></div>
        <div class="line8"></div>
        <div class="line1"></div>
        <div class="blank25"></div>
    </div>
	<!-- 게시물사진 -->
	<c:if test="${baBrdBVo.photoYn == 'Y'}">
		<!--entryzone S-->
            <div class="entryzone">
                <div class="entryarea">
                <dl>
                <dd class="etr_bodytit">
                    <div class="icotit_dot"><u:msg alt="사진 선택" titleId="or.jsp.setOrg.photoTitle" /></div>
                    <m:fileBtn id="photo" titleId="or.jsp.setOrg.photoTitle" alt="사진 선택" exts="gif,jpg,jpeg,png,tif" onchange="setFileInfo"/>
                </dd>
                </dl>
                </div>
            </div>
            <!--//entryzone E-->
            <m:file module="bb" mode="set" id="photo" />
            <div class="blank25"></div>
	</c:if>
<c:if test="${colmMap.cont.readDispYn eq 'Y' }">
<%
	com.innobiz.orange.web.bb.vo.BbBullLVo bbBullLVo = (com.innobiz.orange.web.bb.vo.BbBullLVo)request.getAttribute("bbBullLVo");
	if(bbBullLVo != null){
		if(request.getAttribute("namoEditorEnable")==null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(bbBullLVo.getCont());
			request.setAttribute("_bodyHtml", _bodyHtml);
		} else {
			request.setAttribute("_bodyHtml", bbBullLVo.getCont());
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
         <dd class="etr_input"><div class="etr_bodyline editor" id="bodyHtmlArea"><c:if test="${!empty _bodyHtml }"><u:out value="${_bodyHtml}" type="noscript" /></c:if></div></dd>

		</dl>
		</div>
	</div></c:if><c:if test="${baBrdBVo.optMap.fileUploadYn eq 'Y'}">
       <!--entryzone S-->
       <div class="entryzone">
           <div class="blank20"></div> 
           <div class="entryarea">
           <dl>
           <dd class="etr_bodytit">
               <div class="icotit_dot"><u:msg titleId="cols.attFile" alt="첨부파일" /></div>
               <div class="icoarea">
               <dl>
               <dd class="btn" onclick="addFile('${filesId}');"><u:msg titleId="cm.btn.fileAtt" alt="파일첨부"  /></dd>
               </dl>
               </div>
           </dd>
           </dl>
           </div>
       </div>
       <!--//entryzone E-->
       <m:files id="${filesId}" fileVoList="${fileVoList}" module="bb" mode="set" exts="${exts }" extsTyp="${extsTyp }"/>
	</c:if>
    <div class="blank25"></div>
    <div class="btnarea">
        <div class="size">
            <dl>
            <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
            <c:if test="${writeAuth == 'Y'}">
            <dd class="btn" onclick="saveBull();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
            </c:if>
         </dl>
        </div>
    </div>

	
	</form>
	
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
 </section>
 <c:if test="${colmMap.cont.readDispYn eq 'Y' }"><div id="lobHandlerArea" style="display:none;"><c:if test="${!empty _bodyHtml }">${_bodyHtml}</c:if><c:if test="${!empty lobHandler }"><u:clob lobHandler="${lobHandler }"/></c:if></div></c:if>