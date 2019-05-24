<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><fmt:formatDate var="today" value="<%=new Date() %>" type="date" pattern="yyyy-MM-dd"/>
<u:set var="compIdParam" test="${!empty param.compId }" value="&compId=${param.compId }" elseValue=""/>
<u:set var="listPage" test="${!empty param.compId }" value="listCoprBull" elseValue="${listPage }"/>
<u:out value="${bbBullLVo.bullRezvDt}" type="date" var="bullRezvYmd" />
<u:out value="${bbBullLVo.bullRezvDt}" type="hm" var="bullRezvHm" />
<u:out value="${bbBullLVo.bullExprDt}" type="date" var="bullExprYmd" />
<u:out value="${bbBullLVo.bullExprDt}" type="hm" var="bullExprHm" />
<u:set var="dataframe" test="${(!empty isFrame && isFrame == true) || (!empty isOpen && isOpen == true)}" value="dataframeForFrame" elseValue="dataframe"/>
<u:set var="openUrlSuffix" test="${!empty isOpen && isOpen == true}" value="Open" elseValue=""
/><u:set var="openCallback" test="${!empty param.openCallback}" value="${param.openCallback }" elseValue="reloadOpen"/>
<script type="text/javascript">
<!--<% // [버튼클릭] - 사용자삭제  %>
function selectListDel(obj, containerId){
	if(obj===null) $('#'+containerId).html('');
	else $(obj).closest('div.ubox').remove();
};<% // [버튼클릭] - 사용자추가  %>
function addRowUser(containerId){
	var data=[];
	var idVaList=getUserList(containerId, 'userUid', 'U');
	$.each(idVaList, function(index, userUid){
		data.push({userUid:userUid});
	});
	parent.searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
		if(arr!=null){
			setRowUser(containerId, arr, ["userUid","rescNm"], idVaList);
		}
	});
};<% // 다중 부서 선택 %>
function addRowDept(containerId){
	var data=[];
	var idVaList=getUserList(containerId, 'orgId', 'D');
	$.each(idVaList, function(index, orgId){
		data.push({orgId:orgId});
	});
	
	<% // option : data, multi, withSub, titleId %>
	parent.searchOrgPop({data:data, multi:true, withSub:false, mode:'search'}, function(arr){
		if(arr!=null){
			setRowDept(containerId, arr, ["orgId","rescNm"], idVaList);
		}
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
	buffer.push('<dd class="btn"><a class="delete" onclick="selectListDel(this);" href="javascript:void(0);"><span>delete</span></a></dd>');
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
			setUniformCSS();
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
<% // [하단버튼:저장] %>
function saveBull() {
	<c:if test="${baBrdBVo.discYn != 'Y'}">
	$('#bullStatCd').val('B');
	</c:if>
	<c:if test="${baBrdBVo.discYn == 'Y'}">
	$('#bullStatCd').val('S');
	</c:if>
	setRezvDt();
	setExprDt();
	setBbTgt();
	<c:if test="${empty isOnlyMd || isOnlyMd==false}">
	<c:if test="${colmMap.cont.readDispYn eq 'Y' }">
	if (isInUtf8Length($('#cont').val(), 0, '${bodySize}') > 0) {
		alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.cont" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
		return false;
	}
	</c:if></c:if>
	
	if (validator.validate('setBullForm') && validateEtc('setBullForm')) {
		var errorList=setSelectListToJson();
		if(errorList.length>0){
			var buffer=[];
			<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
			for(var i=0;i<errorList.length;i++){
				buffer.push(callMsg('cm.input.check.maxbyte', [errorList[i].title,errorList[i].maxByte]));
			}
			alert(buffer.join('\n'));
			return false;
		}
		
		<c:if test="${!empty isPcNoti && isPcNoti==true}">
		if($('#setBullForm input:checkbox[name="pcNotiYn"]').is(':checked')){
			var deptCnt=$('#bbTgtDeptHidden input[type="hidden"]').length;	
			var userCnt=$('#bbTgtUserHidden input[type="hidden"]').length;
			if(deptCnt==0 && userCnt==0 && !confirmMsg('bb.confirm.allUser.pcNoti')) // bb.confirm.allUser.pcNoti=모든 사용자에게 알림을 보냅니다.\n 계속 하시겠습니까?
				return;
		}	
		</c:if>
		
		var $form = $('#setBullForm');
		
		<c:if test="${!empty isOnlyMd && isOnlyMd==true}">
		if(!setSaveData($form, null, true)) return false;
		</c:if>
		$form.attr('method','post');
		$form.attr('action','./${action}${openUrlSuffix}.do?menuId=${menuId}&brdId=${baBrdBVo.brdId}');
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','${dataframe}');
		<c:if test="${empty isOnlyMd || isOnlyMd==false}">
		<c:if test="${colmMap.cont.readDispYn eq 'Y' }">editor('cont').prepare();</c:if>
		</c:if>
		saveFileToForm('${filesId}', $form[0], null);
		return true;
		//saveFileToForm($form[0]);
	}else{
		return false;
	}
}
function saveForm(){
	var $form = $('#setBullForm');
	$form[0].submit();
}
<% //[팝업에서 저장 후 페이지 새로고침] %>
function reloadOpenHandler(mobileDomain, msgId){
	<c:if test="${param.isMobile=='Y'}">location.replace(mobileDomain+'/callback.do?func=${openCallback}&msgId='+msgId);</c:if>
	<c:if test="${empty param.isMobile}">
		if(window.opener){
			window.opener.${openCallback}(); window.open('about:blank','_self').close();
		}
	</c:if>
	
}
<% // [하단버튼:임시저장] %>
<c:if test="${tmpSaveYn == true && baBrdBVo.optMap.tmpSaveYn eq 'Y'}">
function saveTmpBull() {
	$('#bullStatCd').val('T');
	setRezvDt();
	setExprDt();
	setBbTgt();
	<c:if test="${colmMap.cont.readDispYn eq 'Y' }">
	if (isInUtf8Length($('#cont').val(), 0, '${bodySize}') > 0) {
		alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.cont" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
		return;
	}
	</c:if>
	if (validator.validate('setBullForm') && validateEtc('setBullForm')) {
		var errorList=setSelectListToJson();
		if(errorList.length>0){
			var buffer=[];
			<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
			for(var i=0;i<errorList.length;i++){
				buffer.push(callMsg('cm.input.check.maxbyte', [errorList[i].title,errorList[i].maxByte]));
			}
			alert(buffer.join('\n'));
			return;
		}
		var $form = $('#setBullForm');
		$form.attr('method','post');
		$form.attr('action','./${action}${openUrlSuffix}.do?menuId=${menuId}&brdId=${baBrdBVo.brdId}');
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','dataframe');
		//alert($('#bullRezvYn').val());
		<c:if test="${colmMap.cont.readDispYn eq 'Y' }">editor('cont').prepare();</c:if>		
		saveFileToForm('${filesId}', $form[0], null);
	}
}
</c:if>

<% // [유효성검증:기타 - 체크박스,라디오,사용자,부서] %>
function validateEtc(id){
	var isValidate=true;
	var chkList=$('#'+id+' div[class="mandatory"]');
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
				}else if(type=='ubox'){
					$chkLen=$(this).find('div[class="ubox"]').length;
				}else return true;
				
				if($chkLen==0){
					isValidate=false;
					$title=$(this).attr('data-title');
					if($title){
						alert(callMsg('cm.input.check.mandatory',[$title]));
						return false;
					}else return true;
				}
				
			}
		});
	}
	return isValidate;
}

<% // 게시예약 변경 이벤트 %>
function onBullRezvYnClick(checked) {
	if (checked) {
		$('#bullRezvYmdArea').show();
		$('#bullRezvHm').show();
		$('#bullRezvHm').parents('.selector').show();
		$('#bullRezvYn').val('Y');
	} else {
		$('#bullRezvYmdArea').hide();
		$('#bullRezvHm').hide();
		$('#bullRezvHm').parents('.selector').hide();
		$('#bullRezvYn').val('N');
	}
}
<% // 게시판선택 %>
function selectBb() {
	var callback = 'setBrdIds';
	var arr = '';
	var mul = (callback == 'moveBull') ? '&mul=N' : '&mul=Y';
	var params = '&brdId=${baBrdBVo.brdId}${compIdParam}' + mul + '&callback=' + callback + '&callbackArgs=' + arr;
	if($('#brdIdList').val() != '' ) params += "&brdIds="+$('#brdIdList').val();
	dialog.open('selectBbPop','<u:msg titleId="bb.jsp.selectBb.title" alt="게시판 선택" />','./selectBbPop.do?menuId=${menuId}' + params);
	dialog.onClose('selectBbPop', function(){
		$("#subj").focus();
	});
	
	//dialog.onClose('selectBbPop', function(){
	//	$("#schWord", getIframeContent('selectBbFrm')).blur();
	//});
}
<% // 게시판선택 callback %>
function setBrdIds(brdIds, brdNms) {
	if (brdIds.indexOf('${baBrdBVo.brdId}') == -1) {
		brdIds = '${baBrdBVo.brdId},' + brdIds;
		brdNms = '${baBrdBVo.brdNm},' + brdNms;
	}
	$('#brdIdList').val(brdIds);
	$('#brdNms').text(brdNms);
}
<% // [하단버튼:삭제] 삭제 %>
<c:if test="${delAction != null}">
function delBull() {
	if (confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./${delAction}.do?menuId=${menuId}', {brdId:'${baBrdBVo.brdId}', bullId:'${param.bullId}'}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './${listPage}.do?${paramsForList}';
			}
		});
	}
}
</c:if>
<% // 게시대상 세팅 %>
function setBbTgt() {
	$('#tgtDeptYn').val($("#bbTgtDeptHidden input[name='orgId']").length > 0 ? 'Y' : 'N');
	$('#tgtUserYn').val($("#bbTgtUserHidden input[name='userUid']").length > 0 ? 'Y' : 'N');
}
<% // 게시대상 부서 표시 %>
function setBbTgtDept(len) {
	var $nameTd = $("#bbTgtDept");
	if (len > 0) {
		$nameTd.html('<u:msg titleId="bb.btn.dept" alt="부서" /> ' + len);
		$nameTd.show();
	} else {
		$nameTd.html('');
		$nameTd.hide();
	}
}
<% // 게시대상 사용자 표시 %>
function setBbTgtUser(len) {
	var $nameTd = $("#bbTgtUser");
	if (len > 0) {
		$nameTd.html('<u:msg titleId="bb.btn.user" alt="사용자" /> ' + len);
		$nameTd.show();
	} else {
		$nameTd.html('');
		$nameTd.hide();
	}
}
<% // 다중 부서 선택 - 하위부서 여부 포함 %>
function openMuiltiOrgWithSub(mode){
	var $inputTd = $("#bbTgtDeptHidden"), data = [];<% // data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	var $subs = $inputTd.find("input[name='withSubYn']");
	$inputTd.find("input[name='orgId']").each(function(index){
		data.push({orgId:$(this).val(), withSub:$($subs[index]).val()});
	});
	<% // option : data, multi, withSub, titleId %>
	searchOrgPop({data:data, multi:true, withSub:true, compId: '${param.compId}'=='' ? null : '${param.compId}', mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, orgVo){
				buffer.push("<input name='orgId' type='hidden' value='"+orgVo.orgId+"' />\n");
				buffer.push("<input name='withSubYn' type='hidden' value='"+orgVo.withSub+"' />\n");
				buffer.push("\n");
			});
			$inputTd.html(buffer.join(''));
			setBbTgtDept(arr.length);
		} else {
			$inputTd.html('');
			setBbTgtDept(0);
		}
		//return false;// 창이 안닫힘
	});
}
<% // 여러명의 사용자 선택 %>
function openMuiltiUser(mode){
	var $inputTd = $("#bbTgtUserHidden"), data = [];<% // data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	$inputTd.find("input[name='userUid']").each(function(){
		data.push({userUid:$(this).val()});
	});
	<% // option : data, multi, titleId %>
	searchUserPop({data:data, multi:true, compId: '${param.compId}'=='' ? null : '${param.compId}',mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, userVo){
				buffer.push("<input name='userUid' type='hidden' value='"+userVo.userUid+"' />\n");
				buffer.push("\n");
			});
			$inputTd.html(buffer.join(''));
			setBbTgtUser(arr.length);
		} else {
			$inputTd.html('');
			setBbTgtUser(0);
		}
	});
}
$(document).ready(function() {
	<c:if test="${param.bullId == null || param.bullId == '' || bbBullLVo.bullRezvYn != 'Y'}">
	onBullRezvYnClick(false);
	</c:if>
	<c:if test="${param.bullId != null && param.bullId != ''}">
	//setReadonly($('#anonRegrNm'), true);
	</c:if>
	setBbTgtDept(${bullTgtDeptVoList != null ? bullTgtDeptVoList.size() : 0});
	setBbTgtUser(${bullTgtUserVoList != null ? bullTgtUserVoList.size() : 0});
	<c:if test="${colmMap.cont.readDispYn eq 'Y' && empty param.sendNo && empty param.emailNo}">
	/* if(unloadEvent.editorType != 'namo'){
		var bodyHtml = $("#lobHandlerArea").html();
		if(bodyHtml!=''){
			$('#contEdit').on('load',function () {
				editor('cont').setInitHtml(bodyHtml);
				editor('cont').prepare();
			});
		}
	} */
	</c:if>
	setUniformCSS();
});<%
// 나모 에디터 초기화 %>
function initNamo(id){
	var bodyHtml = $("#lobHandlerArea").html();
	if(bodyHtml!=''){
		editor('cont').setInitHtml(bodyHtml);		
	}
	editor('cont').prepare();
}
<c:if test="${!empty param.returnFunc }">
function ${param.returnFunc}(){
	parent.${param.returnFunc}();
}
</c:if>
//-->
</script>
<c:if test="${isOpen==true }"><c:set var="style" value=" style=\"min-width:1080px;padding:10px;\""/></c:if>
<div${style }>
<u:set test="${!empty param.bullId && listPage != 'listTmpBull'}" var="fnc" value="mod" elseValue="reg" />
<u:msg titleId="bb.jsp.setBull.${fnc}.title" var="title" alt="게시물 등록/게시물 수정" />
<c:if test="param.bullPid != null && param.bullPid != ''">
	<u:msg titleId="bb.jsp.setReply.title" var="title" alt="게시물 답변" />
</c:if>

<c:if test="${listPage == 'listTmpBull'}"><u:msg titleId="bb.jsp.listTmpBull.title" alt="임시저장함" var="bbNm" /></c:if>
<c:if test="${listPage == 'listRezvBull'}"><u:msg titleId="bb.jsp.listRezvBull.title" alt="예약저장함" var="bbNm" /></c:if>
<c:if test="${listPage == 'listBullMng'}"><u:msg titleId="bb.jsp.listBullMng.title" alt="게시물관리" var="bbNm" /></c:if>
<c:if test="${bbNm == null}"><c:set var="bbNm" value="${baBrdBVo.rescNm}" /></c:if>

<c:if test="${empty param.sendNo && empty param.emailNo}">
<u:title title="${title} - ${bbNm}" menuNameFirst="true"/>
</c:if>

<form id="setBullForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${paramsForList}" />
<c:if test="${fn:startsWith(viewPage,'list')}"><u:input type="hidden" id="viewPage" value="./${listPage}.do?${paramsForList}" /></c:if>
<c:if test="${!fn:startsWith(viewPage,'list')}"><u:input type="hidden" id="viewPage" value="./${viewPage}.do?${params}&bullId=${param.bullId}" /></c:if>
<u:input type="hidden" id="brdId" value="${baBrdBVo.brdId}" />
<u:input type="hidden" id="bullId" value="${param.bullId}" />
<u:input type="hidden" id="bullPid" value="${param.bullPid}" />
<u:input type="hidden" id="bullStatCd" value="B" />
<u:input type="hidden" id="bullRezvDt" value="${bbBullLVo.bullRezvDt}" />
<u:input type="hidden" id="tgtDeptYn" value="${bbBullLVo.tgtDeptYn}" />
<u:input type="hidden" id="tgtUserYn" value="${bbBullLVo.tgtUserYn}" />

<c:if test="${!empty param.sendNo }"><u:input type="hidden" id="sendNo" value="${param.sendNo}" /></c:if>
<c:if test="${!empty param.emailNo }"><u:input type="hidden" id="emailNo" value="${param.emailNo}" /></c:if>
<c:if test="${!empty param.returnFunc }"><u:input type="hidden" id="returnFunc" value="${param.returnFunc}" /></c:if>
<c:if test="${!empty param.compId }"><u:input type="hidden" id="compId" value="${param.compId}" /></c:if>

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

<c:if test="${baBrdBVo.optMap.privUseYn eq 'Y' }">
<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr><td class="color_stxt"><input type="checkbox" id="privYn" name="privYn" value="Y" <c:if test="${!empty privYn && privYn eq 'Y' }">checked="checked"</c:if>/><label for="privYn"><u:msg titleId="bb.msg.save.priv" alt="비공개로 등록합니다." /></label></td>
			</tr>
		</table>
	</div>
</div>
</c:if>
<% // 폼 필드 %>
<u:set var="rezvYn" test="${!empty colmMap.bullRezvDt && colmMap.bullRezvDt.readDispYn eq 'Y'}" value="Y" elseValue="N"/>
<u:set var="exprYn" test="${baBrdBVo.kndCd == 'R' && param.bullPid == null && bbBullLVo.bullPid == null && !empty colmMap.bullExprDt && colmMap.bullExprDt.readDispYn eq 'Y'}" value="Y" elseValue="N"/>
<%-- <u:set test="${(bbTgtDispYn && baBrdBVo.optMap.bbTgtDispYn eq 'Y' && baBrdBVo.optMap.bbOptYn eq 'Y') || (rezvYn eq 'Y' && exprYn eq 'Y')}" var="colgroup" value="18%,32%,18%,32%" elseValue="18%,82%" /> --%>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="18%"/><col width="32%"/><col width="18%"/><col width="32%"/></colgroup>
	<c:if test="${colmMap.subj.readDispYn eq 'Y' }"><u:set var="mandatoryYn" test="${!empty colmMap.subj.mandatoryYn }" value="${colmMap.subj.mandatoryYn }" elseValue="N"/>
	<tr>
	<td class="head_lt"><c:if test="${mandatoryYn eq 'Y' }"><u:mandatory /></c:if><u:msg titleId="cols.subj" alt="제목" /></td>
	<td colspan="3"><u:input id="subj" titleId="cols.subj" style="width:98%;" value="${bbBullLVo.subj}" mandatory="${mandatoryYn }" maxByte="240" /></td>
	</tr>
	</c:if>
	<c:if test="${baBrdBVo.brdTypCd == 'A'}">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td colspan="3"><u:input id="anonRegrNm" titleId="cols.regr" style="width: 150px;" value="${bbBullLVo.anonRegrNm}" mandatory="Y" maxByte="30"/></td>
	</tr>
	</c:if>

	<c:if test="${baBrdBVo.allCompYn != 'Y' && baBrdBVo.optMap.brdCopyYn eq 'Y'}">
	<c:if test="${bbChoiYn == true}">
		<u:set test="${brdNms != null}" var="brdNms" value="${brdNms}" elseValue="${baBrdBVo.rescNm}" />
		<u:set test="${brdIdList != null}" var="brdIdList" value="${brdIdList}" elseValue="${baBrdBVo.brdId}" />
	<tr>
	<td class="head_lt"><u:msg titleId="cols.bb" alt="게시판" /></td>
	<td colspan="3"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="body_lt"><span id="brdNms">${brdNms}</span></td>
		<td><u:buttonS titleId="bb.btn.bbChoi" alt="게시판선택" onclick="selectBb();" />
			<u:input type="hidden" id="brdIdList" value="${brdIdList}" /></td>
		</tr>
		</table></td>
	</tr>
	</c:if>
	</c:if>

	<c:if test="${baBrdBVo.catYn == 'Y' && param.bullPid == null && !empty colmMap.catId && colmMap.catId.readDispYn eq 'Y'}">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.cat" alt="카테고리" /></td>
	<td colspan="3"><select name="catId">
		<c:forEach items="${baCatDVoList}" var="catVo" varStatus="status">
		<u:option value="${catVo.catId}" title="${catVo.rescNm}" checkValue="${bbBullLVo.catId}" />
		</c:forEach>
		</select></td>
	</tr>
	</c:if>
	<c:if test="${baBrdBVo.optMap.bbTgtDispYn eq 'Y' || baBrdBVo.optMap.bbOptYn eq 'Y' }">
	<u:set test="${baBrdBVo.optMap.bbOptYn eq 'Y'}" var="colspan" value="1" elseValue="3" />
	<tr>
	<c:if test="${bbTgtDispYn && baBrdBVo.optMap.bbTgtDispYn eq 'Y'}">
	<td class="head_lt"><u:msg titleId="bb.cols.bbTgt" alt="게시대상" /></td>
	<td colspan="${colspan}"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td id="bbTgtDept" style="display: none;" class="body_lt"></td>
		<c:if test="${empty bbTgtYn || bbTgtYn == false}">
			<td><u:buttonS titleId="bb.btn.dept" alt="부서" onclick="openMuiltiOrgWithSub()" /></td>
		</c:if>
		<td id="bbTgtUser" style="display: none;" class="body_lt"></td>
		<c:if test="${empty bbTgtYn || bbTgtYn == false}">
			<td><u:buttonS titleId="bb.btn.user" alt="사용자" onclick="openMuiltiUser()" /></td>
		</c:if>
		</tr>
		</tbody></table></td>
	</c:if>
	<u:set test="${bbTgtDispYn && baBrdBVo.optMap.bbTgtDispYn eq 'Y'}" var="colspan" value="1" elseValue="3" />
	<c:if test="${baBrdBVo.optMap.bbOptYn eq 'Y' }">
	<td class="head_lt"><u:msg titleId="bb.cols.bbOpt" alt="게시옵션" /></td>
	<td colspan="${colspan}"><u:checkArea>
		<u:checkbox name="secuYn" value="Y" titleId="bb.option.secu" alt="보안" inputClass="bodybg_lt" checkValue="${bbBullLVo.secuYn}" />
		<u:checkbox name="ugntYn" value="Y" titleId="bb.option.ugnt" alt="긴급" inputClass="bodybg_lt" checkValue="${bbBullLVo.ugntYn}" />
		<c:if test="${(param.bullPid == null || param.bullPid == '') && bbBullLVo.bullPid == null}">
		<u:checkbox name="notcYn" value="Y" titleId="bb.option.notc" alt="공지" inputClass="bodybg_lt" checkValue="${bbBullLVo.notcYn}" />
		</c:if><c:if test="${!empty isPcNoti && isPcNoti==true}"><u:checkbox name="pcNotiYn" value="Y" titleId="bb.option.pcNoti" alt="PC알림" inputClass="bodybg_lt" /></c:if>
		</u:checkArea></td></c:if>
	</tr>
	</c:if>	
	<c:if test="${rezvYn eq 'Y' || exprYn eq 'Y'}">
	<tr><c:if test="${rezvYn eq 'Y'}">
	<td class="head_lt"><u:msg titleId="cols.bullRezvDt" alt="게시예약일" /></td>
	<td <c:if test="${exprYn eq 'N' }">colspan="3"</c:if>><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<c:if test="${bullRezvDtYn == false}">
		<td class="body_lt">
			<c:if test="${bbBullLVo.bullRezvYn == 'Y'}">
			<u:out value="${bbBullLVo.bullRezvDt}" type="longdate" />
			</c:if>
			</td>
		</c:if>
		<c:if test="${bullRezvDtYn == true}">
			<u:input type="hidden" id="bullRezvYn" value="${bullRezvChecked == true ? 'Y' : 'N' }" />
			<c:choose>
				<c:when test="${param.bullId != null && !empty param.bullId }">
					<u:checkbox  name="bullRezvYnChk" id="bullRezvYnChk" value="Y" titleId="bb.cols.bullRezv" alt="게시예약" checked="${bullRezvChecked}" onclick="onBullRezvYnClick(this.checked);" inputClass="bodybg_lt" />
				</c:when>
				<c:otherwise><u:checkbox name="bullRezvYnChk" id="bullRezvYnChk" value="Y" titleId="bb.cols.bullRezv" alt="게시예약" checked="${bullRezvChecked}" onclick="onBullRezvYnClick(this.checked);" inputClass="bodybg_lt" /></c:otherwise>
			</c:choose>
			<td><u:calendar id="bullRezvYmd" value="${bullRezvYmd}" option="{checkHandler:onBullRezvDayChange}"/></td>
			<td>
				<select id="bullRezvHm" name="bullRezvHm" onchange="onBullRezvTimeChange('bullRezvHm',$('#bullRezvYmd').val(),'${bullRezvHm }');">
				<c:forEach begin="0" end="23" step="1" var="hour" varStatus="status">
					<u:set test="${hour < 10}" var="hh" value="0${hour}" elseValue="${hour}" />
					<u:option value="${hh}:00" title="${hh}:00" checkValue="${bullRezvHm}" />
					<u:option value="${hh}:30" title="${hh}:30" checkValue="${bullRezvHm}" />
				</c:forEach>
				</select>
			</td>
		</c:if>
		</tr>
		</table></td></c:if>
	<c:if test="${exprYn eq 'Y'}">
	<td class="head_lt"><u:msg titleId="cols.bullExprDt" alt="게시완료일" /></td>
	<td <c:if test="${rezvYn eq 'N' }">colspan="3"</c:if>><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><u:calendar id="bullExprYmd" value="${bullExprYmd}" option="{checkHandler:onBullExprDayChange}"/></td>
		<td><select id="bullExprHm" name="bullExprHm" onchange="onBullRezvTimeChange('bullExprHm',$('#bullExprYmd').val(),'${bullExprHm }');">
			<c:forEach begin="0" end="23" step="1" var="hour" varStatus="status">
				<u:set test="${hour < 10}" var="hh" value="0${hour}" elseValue="${hour}" />
				<u:option value="${hh}:00" title="${hh}:00" checkValue="${bullExprHm}" />
				<u:option value="${hh}:30" title="${hh}:30" checkValue="${bullExprHm}" />
			</c:forEach>
			</select>
			<u:input type="hidden" id="bullExprDt" value="${bbBullLVo.bullExprDt}" />
			</td>
		</tr>
		</table></td>
	</c:if>
	</tr></c:if>
	
	<!-- SNS 사용 -->
	<c:if test="${!empty isSns && isSns == true && baBrdBVo.optMap.snsUploadYn eq 'Y'}">
	<tr>
	<td class="head_lt"><u:msg titleId="bb.sns.upload" alt="SNS 올리기" /></td>
	<td colspan="3"><u:sns mode="set" /></td>
	</c:if>
	
	<!-- 게시물사진 -->
	<c:if test="${baBrdBVo.photoYn == 'Y'}">
	<tr>
	<td class="head_lt"><u:msg alt="사진 선택" titleId="or.jsp.setOrg.photoTitle" /></td>
	<td colspan="3"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:file id="photo" titleId="or.jsp.setOrg.photoTitle" alt="사진 선택" exts="gif,jpg,jpeg,png,tif" /></td>
		</tr>
		
		<c:if test="${bbBullLVo.photoVo != null}">
		<c:set var="maxWdth" value="200" />
		<u:set test="${bbBullLVo.photoVo.imgWdth <= maxWdth}" var="imgWdth" value="${bbBullLVo.photoVo.imgWdth}" elseValue="${maxWdth}" />
		<tr>
		<td><img src="${_cxPth}${bbBullLVo.photoVo.savePath}" width="${imgWdth}"></td>
		</tr>
		</c:if>
		</tbody></table></td>
	</tr>
	</c:if>
	<c:set var="map" value="${bbBullLVo.exColMap}" scope="request" />
	<c:set var="maxCnt" value="2"/>
	<c:set var="dispCnt" value="1"/>
	<c:set var="maxTrCnt" value="0"/>
	<c:set var="maxLen" value="${fn:length(baColmDispDVoList) }"/>
	<!-- 확장컬럼 -->
	<c:forEach items="${baColmDispDVoList}" var="baColmDispDVo" varStatus="colStatus">
		<c:set var="colmVo" value="${baColmDispDVo.colmVo}" />
		<c:set var="colmNm" value="${colmVo.colmNm.toLowerCase()}" />
		<c:set var="colmTyp" value="${colmVo.colmTyp}" />
		<c:set var="colmTypVal" value="${colmVo.colmTypVal}" />
		<u:set var="colsWdthVa" test="${!empty baColmDispDVo.colsWdthVa }" value="${baColmDispDVo.colsWdthVa }" elseValue="0.5"/>
		<u:set var="nextColsWdthVa" test="${colStatus.count<maxLen && !empty baColmDispDVoList[colStatus.index+1].colsWdthVa }" value="${baColmDispDVoList[colStatus.index+1].colsWdthVa }" elseValue="0.5"/>
	<c:if test="${baColmDispDVo.readDispYn == 'Y' && colmVo.exColmYn == 'Y'}">
	<c:if test="${dispCnt==1 || dispCnt%maxCnt == 1 }"><tr><c:set var="maxTrCnt" value="${maxTrCnt+1 }"/></c:if>
	<u:set var="colspan" test="${(dispCnt == 1 || dispCnt<maxCnt || dispCnt%maxCnt > 0) 
	&& ( colmTyp eq 'TEXTAREA' || fn:length(baColmDispDVoList) == colStatus.count || colsWdthVa=='1' || nextColsWdthVa=='1')}" value="colspan='${(((maxCnt*maxTrCnt)-dispCnt)*2)+1 }'" elseValue=""/>
	<u:set var="mandatoryYn" test="${!empty baColmDispDVo.mandatoryYn }" value="${baColmDispDVo.mandatoryYn }" elseValue="N"/>
	<td class="head_lt"><c:if test="${mandatoryYn eq 'Y' }"><u:mandatory /></c:if>${colmVo.rescNm}</td>
	<td class="body_lt" ${colspan }>
		<!-- 확장컬럼의 데이터 최대길이(byte) [input,textarea만 해당] -->
		<u:set var="dataMaxByte" test="${(colmTyp == 'TEXT' || colmTyp == 'TEXTAREA' || colmTyp == 'USER' || colmTyp == 'DEPT') && colmVo.dataTyp eq 'VARCHAR' && !empty colmVo.colmLen }" value="${colmVo.colmLen }" elseValue="100"/>
		<c:if test="${colmTyp == 'TEXT'}"><u:input id="${colmNm}" value="${bbBullLVo.getExColm(colmVo.colmNm)}" title="${colmVo.rescNm}" maxByte="${dataMaxByte }" style="width: 95%;" mandatory="${mandatoryYn }"/></c:if>
		<c:if test="${colmTyp == 'TEXTAREA'}"><u:textarea id="${colmNm}" value="${bbBullLVo.getExColm(colmVo.colmNm)}" title="${colmVo.rescNm}" maxByte="${dataMaxByte }" style="width:95%;" rows="${colmTypVal}" mandatory="${mandatoryYn }"/></c:if>
		<c:if test="${colmTyp == 'PHONE'}"><u:phone id="${colmNm}" value="${bbBullLVo.getExColm(colmVo.colmNm)}" title="${colmVo.rescNm}" mandatory="${mandatoryYn }"/></c:if>
		<c:if test="${colmTyp == 'CALENDAR'}"><u:set var="value" test="${empty param.bullId && empty bbBullLVo.getExColm(colmVo.colmNm) }" value="${colmTypVal }" elseValue="${bbBullLVo.getExColm(colmVo.colmNm) }"/><u:calendar id="${colmNm}" value="${value}" title="${colmVo.rescNm}" mandatory="${mandatoryYn }"/></c:if>
		<c:if test="${colmTyp == 'CALENDARTIME'}"><u:set var="value" test="${empty param.bullId && empty bbBullLVo.getExColm(colmVo.colmNm) }" value="${colmTypVal }" elseValue="${bbBullLVo.getExColm(colmVo.colmNm) }"/><u:calendartime id="${colmNm}" value="${value }" title="${colmVo.rescNm}" mandatory="${mandatoryYn }"/></c:if>
		<c:if test="${fn:startsWith(colmTyp,'CODE')}">
			<u:set test="${cdListIndex == null}" var="cdListIndex" value="0" elseValue="${cdListIndex + 1}" />
			<c:if test="${colmTyp == 'CODE'}">
				<select name="${colmNm}">
				<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status">
				<option value="${cd.cdId}" <c:if test="${cd.cdId == bbBullLVo.getExColm(colmVo.colmNm)}">selected="selected"</c:if>>${cd.rescNm}</option>
				</c:forEach>
				</select>
			</c:if>
			<c:if test="${colmTyp == 'CODECHK'}"><div <c:if test="${mandatoryYn eq 'Y' }">class="mandatory" data-validateType="check" data-name="${colmNm }" data-title="${colmVo.rescNm}"</c:if>>
				<u:checkArea><c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status"><c:set var="checked" value="N" 
				/><c:forTokens var="chkId" items="${bbBullLVo.getExColm(colmVo.colmNm)}" delims=","><c:if test="${chkId==cd.cdId }"><c:set var="checked" value="Y" 
				/></c:if></c:forTokens><u:checkbox value="${cd.cdId}" name="${colmNm }" title="${cd.rescNm}" alt="${cd.rescNm}" checked="${checked=='Y' }"/></c:forEach></u:checkArea></div>
			</c:if>
			<c:if test="${colmTyp == 'CODERADIO'}">
				<div <c:if test="${mandatoryYn eq 'Y' }">class="mandatory" data-validateType="radio" data-name="${colmNm } data-title="${colmVo.rescNm}"</c:if>><u:checkArea><c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status"><u:radio name="${colmNm }" value="${cd.cdId}" title="${cd.rescNm}" alt="${cd.rescNm}" checkValue="${bbBullLVo.getExColm(colmVo.colmNm)}"  inputClass="bodybg_lt" checked="${empty bbBullLVo.getExColm(colmVo.colmNm) && status.first }"/></c:forEach></u:checkArea></div>
			</c:if>
		</c:if>
		<c:if test="${colmTyp == 'USER' || colmTyp == 'DEPT'}"><div style="display:block;width:100%;"
		><c:if test="${colmTyp == 'USER' }"><u:buttonS href="javascript:addRowUser('selectListArea_${colmNm }');" titleId="bb.btn.addUser" alt="사용자추가" auth="W" 
	/></c:if><c:if test="${colmTyp == 'DEPT' }"><u:buttonS href="javascript:addRowDept('selectListArea_${colmNm }');" titleId="bb.btn.addDept" alt="부서추가" auth="W" 
	/></c:if><u:buttonS href="javascript:selectListDel(null, 'selectListArea_${colmNm }');" titleId="wl.btn.delAll" alt="전체삭제" auth="W" 
	/></div><div id="selectListArea_${colmNm }" style="min-height:40px;" data-maxByte="${dataMaxByte }" data-title="${colmVo.rescNm}" <c:if test="${mandatoryYn eq 'Y' }">class="mandatory" data-validateType="ubox"</c:if>
	><u:convertMap srcId="map" attId="${colmNm }MapList" var="mapList" /><c:if test="${!empty mapList }"><c:forEach 
		var="mapVo" items="${mapList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title"><c:if test="${colmTyp == 'USER' }"><a href="javascript:viewUserPop('${mapVo.id }');">${mapVo.rescNm }</a><input type="hidden" id="idVa" value='{"userUid":"${mapVo.id }"}'></c:if
		><c:if test="${colmTyp == 'DEPT' }">${mapVo.rescNm }<input type="hidden" id="idVa" value='{"orgId":"${mapVo.id }"}'></c:if></dd>
		<dd class="btn"><a class="delete" onclick="selectListDel(this);" href="javascript:void(0);"><span>delete</span></a></dd></dl></div>
		</c:forEach></c:if><u:input type="hidden" id="${colmNm }" value="${bbBullLVo.getExColm(colmVo.colmNm)}" /></div></c:if>
		</td>
	<c:if test="${colStatus.count == fn:length(baColmDispDVoList) }"></tr></c:if>
	<c:if test="${!empty colspan && colStatus.count < fn:length(baColmDispDVoList) }"><c:set var="dispCnt" value="${dispCnt+(maxCnt-1) }"/></c:if>
	<c:set var="dispCnt" value="${dispCnt+1 }"/>
	</c:if>
	</c:forEach>
</table></div>
<c:if test="${empty isOnlyMd || isOnlyMd==false}">
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
%><u:editor id="cont" width="100%" height="400px" module="bb" value="${_bodyHtml}" padding="2" /></c:if>
</c:if>
<c:if test="${!empty isOnlyMd && isOnlyMd==true}">
<u:blank />
<jsp:include page="/WEB-INF/jsp/wf/works/setMdWorks.jsp" flush="false" />
</c:if>
<c:if test="${baBrdBVo.optMap.fileUploadYn eq 'Y'}">
<c:if test="${colmMap.cont.readDispYn ne 'Y' }"><u:blank /></c:if>
<u:listArea>
	<tr>
	<td><u:files id="${filesId}" fileVoList="${fileVoList}" module="bb" mode="set" exts="${exts }" extsTyp="${extsTyp }" urlParam="brdId=${param.brdId }"/></td>
	</tr>
</u:listArea>
</c:if>

<c:if test="${empty param.sendNo && empty param.emailNo}">
<c:if test="${colmMap.cont.readDispYn ne 'Y' && baBrdBVo.optMap.fileUploadYn ne 'Y'}"><u:blank /></c:if>
<% // 하단 버튼 %>
<u:buttonArea>
<c:if test="${empty isOpen && delAction != null}">
	<u:button titleId="cm.btn.del" alt="삭제" onclick="delBull();" auth="A" ownerUid="${bbBullLVo.regrUid}" />
</c:if>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveBull();" auth="W" />
	<c:if test="${empty isOpen && tmpSaveYn == true && baBrdBVo.optMap.tmpSaveYn eq 'Y'}">
	<u:button titleId="cm.btn.tmpSave" alt="임시저장" onclick="saveTmpBull();" auth="W" />
	</c:if>
	<c:if test="${!empty isOpen && isOpen==true }"><u:button titleId="cm.btn.close" alt="닫기" href="javascript:;" onclick="window.open('about:blank','_self').close();"/></c:if>
	<c:if test="${empty isOpen }"><u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" /></c:if>
</u:buttonArea>
</c:if>
</form>

<c:if test="${colmMap.cont.readDispYn eq 'Y' }"><div id="lobHandlerArea" style="display:none;"><c:if test="${!empty _bodyHtml }">${_bodyHtml}</c:if><c:if test="${!empty lobHandler }"><u:clob lobHandler="${lobHandler }"/></c:if></div></c:if>
</div>
