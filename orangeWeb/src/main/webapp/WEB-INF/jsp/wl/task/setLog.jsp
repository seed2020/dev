<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><fmt:formatDate var="today" value="<%=new Date() %>" type="date" pattern="yyyy-MM-dd"/>
<u:params var="params"/>
<u:params var="paramsForList" excludes="logNo"/>
<script type="text/javascript">
<!--<% // [세팅] - 취합목록ID  %>
function setLstNos(arr, isInit){
	if(isInit)
		$('#setForm input[id="lstNos"]').val(arr.join(','));
	else{
		var lstNos = $('#setForm input[id="lstNos"]').val();
		if(lstNos!='') lstNos=lstNos.split(',');
		else lstNos=[];
	
		$.each(arr, function(i, el){
			if($.inArray(el, lstNos) === -1) lstNos.push(el);
		});
		
		if(lstNos.length>0)
			$('#setForm input[id="lstNos"]').val(lstNos.join(','));
	}
}<% // [취합] - 업무일지 취합  %>
function setLogList(arr, isInit){
	if (arr != null) {
		callAjax('./getListMapAjx.do?menuId=${menuId}', {logNos:arr}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				if(data.returnMap!=null){
					setLstNos(arr, isInit); // 목록ID 세팅
					$.each(data.returnMap.map, function(key, value){
						if(isInit!=undefined && !isInit) value=editor(key).getHtml()+value;
						editor(key).setInitHtml(value);
						editor(key).prepare();
					});
				}
				if(data.logFileList!=null){
					var fileList=[];
					var fileVo;
					$.each(data.logFileList, function(index, vo){
						fileVo=vo.map;
						fileList.push({
							name:fileVo.dispNm,
							fileId:fileVo.fileId,
							useYn:fileVo.useYn,
							size:fileVo.fileSize,
							fileExt:fileVo.fileExt
					    });
					});
					if(isInit!=undefined && isInit) delFile(true);
					setFileList(fileList);
				}
				//console.log('returnMap : '+data.returnMap);
				//editor('cont').setInitHtml(bodyHtml);
				//editor('cont').prepare();
			}
		});
	}
};<% // [버튼클릭] - 업무일지 선택 팝업  %>
function findLogListPop(logTyp){
	var url='./setTaskLogPop.do?menuId=${menuId}&multi=Y';
	<c:if test="${!empty param.logNo}">url+='&logNo=${param.logNo}'</c:if>
	if($('#setForm input[id="lstNos"]').val()!='')
		url+='&lstNos='+$('#setForm input[id="lstNos"]').val();
	dialog.open('findTaskLogDialog','<u:msg titleId="wl.btn.task.select" alt="업무일지 선택" />', url);
};<% // [버튼클릭] - 사용자삭제  %>
function userSelectDel(obj){
	if(obj===undefined) $('#userSelectArea').html('');
	else $(obj).closest('div.ubox').remove();
};<% // [버튼클릭] - 사용자추가  %>
function addRowUser(){
	var data=[];
	$.each($('#userSelectArea input[name="userUid"]'), function(){
		data.push({userUid:$(this).val()});
	});
	searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
		if(arr!=null){
			//$('#userSelectArea').html('');
			setRowUser(arr);
		}
	});
};<% // 사용자추가  %>
function setRowUser(arr){	
	var buffer=[];
	var userUidList=getUserList();
	arr.each(function(index, userVo){
		if($.inArray(userVo.userUid, userUidList)!=-1)
			return true;
		buffer.push(createUbox(userVo.userUid, userVo.rescNm, false));
	});
	if(buffer.length>0){
		$('#userSelectArea').append(buffer.join(''));
	}
}<% // [버튼클릭] - 보고그룹 상세 목록조회  %>
function setReprtGrpList(obj){
	//$('#userSelectArea div.grpList').remove();
	if(obj.value=='') return;
	callAjax('./getReprtGrpListAjx.do?menuId=${menuId}', {grpNo:obj.value, tgtTypCd:'U'}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok' && data.reprtGrpDtlList!=null) {
			var userUidList=getUserList();
			var buffer=[];
			var reprtGrpDtlList=data.reprtGrpDtlList;
			$.each(reprtGrpDtlList, function(index, userVo){
				userVo=userVo.map;
				if($.inArray(userVo.userUid, userUidList)!=-1){
					//$('#userSelectArea input[name="userUid"][value="'+userVo.userUid+'"]').closest('div.ubox').addClass('grpList');
					return true;
				}
				buffer.push(createUbox(userVo.userUid, userVo.userNm, true));
			});
			if(buffer.length>0){
				$('#userSelectArea').append(buffer.join(''));
			}
		}
	});
	
}<% // 사용자 목록 생성  %>
function createUbox(userUid, userNm, isGrp){
	var buffer=[];
	buffer.push('<div class="ubox'+(isGrp ? ' grpList' : '')+'"><dl>');
	buffer.push('<dd class="title"><a href="javascript:viewUserPop(\''+userUid+'\');">');
	buffer.push(userNm);
	buffer.push('</a>');
	buffer.push('<input type="hidden" name="userUid" value="'+userUid+'"/>');
	buffer.push('</dd>');
	buffer.push('<dd class="btn"><a class="delete" onclick="userSelectDel(this);" href="javascript:void(0);"><span>delete</span></a></dd>');
	buffer.push('</dl></div>');
	return buffer.join('');
}<% // 보고대상 목록  %>
function getUserList(){
	var userUidList=[];
	$('#userSelectArea input[name="userUid"]').each(function(){
		userUidList.push($(this).val());
	});
	return userUidList;
	
}<% // [버튼클릭] - 저장  %>
function saveLog() {
	if($('#userSelectArea div.ubox').length==0){
		alertMsg('cm.input.check.require.users',['<u:msg titleId="wl.cols.reprtTgt" alt="보고대상" />']);
		return;
	}
	if (validator.validate('setForm'))
		save('C');
	
};<% // [버튼클릭] - 임시저장 %>
function saveTmp() {
	if($('#setForm #subj').val()==''){
		alertMsg('cm.input.check.mandatory', ['#cols.subj']);
		$('#setForm #subj').focus();
		return;
	}
	save('T');	
};<% // [저장] 테이블 저장 %>
function save(statCd) {
	var isLimit=false, title=null;
	$.each(['result','plan','etc'], function(index, prefix){
		if($('#'+prefix+'Cont')!=undefined){
			if (isInUtf8Length($('#'+prefix+'Cont').val(), 0, '${bodySize}') > 0) {
				title=callMsg('wl.cols.'+prefix);
				alertMsg('cm.input.check.maxbyte', [title,'${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
				isLimit=true;
				return false;
			}
		}
		
	});
	if(isLimit) return;
	var $form = $('#setForm');
	if(statCd!=undefined){
		$form.find("input[name='statCd']").remove();
		$form.appendHidden({name:'statCd',value:statCd});	
	}
	
	// 수정이면서 상태가 변경될경우(임시<=>저장) 목록으로 리다이렉트.
	<c:if test="${!empty param.logNo}">
		if(statCd!='{wlTaskLogBVo.statCd}'){
			$form.find("input[name='viewPage']").val($form.find("input[name='listPage']").val());
		}
	</c:if>
	$form.attr('method','post');
	$form.attr('action','./${transPage}.do?menuId=${menuId}');
	$form.attr('enctype','multipart/form-data');
	$form.attr('target','dataframe');
	<c:forEach var="columnVo" items="${columnList }" varStatus="status">
	editor('${columnVo[0] }Cont').prepare();
	</c:forEach>
	saveFileToForm('${filesId}', $form[0], null);
};
$(document).ready(function() {
	setUniformCSS();
	<c:if test="${path eq 'recv' && !empty param.selLogNos}">
	//setLogList('${param.selLogNos}'.split(','),true);
	</c:if>
	
});<%
// 나모 에디터 초기화 %>
function initNamo(id){
	return;
}
//-->
</script>
<u:msg var="menuTitle" titleId="wl.jsp.${path }.title" />
<u:title title="${menuTitle }" menuNameFirst="true" alt="${menuTitle }" />

<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${paramsForList}" />
<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${params}" />
<c:if test="${!empty param.logNo }"><u:input type="hidden" id="logNo" value="${param.logNo}" /></c:if>
<c:if test="${path eq 'recv' || wlTaskLogBVo.consolYn eq 'Y'}">
<u:input type="hidden" id="lstNos" value="${lstNos }" />
</c:if>
<% // 폼 필드 %>
<div class="listarea" id="listArea">
<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
<colgroup><col width="12%"/><col width="38%"/><col width="12%"/><col width="38%"/></colgroup>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wl.cols.typCd.select" alt="종류선택" /></td>
	<td class="body_lt"><u:checkArea id="typCdsArea">
			<c:forEach var="tab" items="${tabList }" varStatus="status">
				<u:radio id="typCds${status.index }" name="typCd" value="${tab}" titleId="wl.cols.typCd.${tab }" checked="${(empty wlTaskLogBVo.typCd && status.index==0)}" checkValue="${!empty param.typCd ? param.typCd : wlTaskLogBVo.typCd }"/>
			</c:forEach>
		</u:checkArea></td>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wl.cols.reprtDt" alt="보고일자" /></td>
	<td class="body_lt"><u:calendar id="reprtDt" titleId="wl.cols.reprtDt" value="${empty wlTaskLogBVo.reprtDt ? today : wlTaskLogBVo.reprtDt}"/></td>
	</tr>
	<c:if test="${path eq 'log' || path eq 'recv' || path eq 'temp'}">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wl.cols.reprtTgt" alt="보고대상" /></td>
	<td class="body_lt" colspan="3"><div style="display:block;width:100%;"
		><select name="grpNo" style="min-width:100px;" onchange="setReprtGrpList(this);"><u:option value="" titleId="wl.cols.reprtTgt.select" alt="보고대상선택" selected="${empty wlTaskLogBVo.grpNo }"
		/><c:forEach var="list" items="${reprtGrpList }" varStatus="status"><u:option value="${list.grpNo }" title="${list.grpNm }" alt="${list.grpNm }" checkValue="${wlTaskLogBVo.grpNo }"
		/></c:forEach></select><u:buttonS href="javascript:addRowUser();" titleId="wl.btn.addUser" alt="사용자추가" auth="W" 
	/><u:buttonS href="javascript:userSelectDel();" titleId="wl.btn.delAll" alt="전체삭제" auth="W" 
	/></div><div id="userSelectArea" style="min-height:40px;"><c:forEach 
		var="wlTaskLogUserLVo" items="${wlTaskLogUserLVoList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title"><a href="javascript:viewUserPop('${wlTaskLogUserLVo.userUid }');">${wlTaskLogUserLVo.userNm }</a><input type="hidden" name="userUid" value="${wlTaskLogUserLVo.userUid }"
		/></dd><dd class="btn"><a class="delete" onclick="userSelectDel(this);" href="javascript:void(0);"><span>delete</span></a></dd></dl></div>
		</c:forEach><c:forEach 
		var="wlReprtGrpLVo" items="${wlReprtGrpLVoList }" varStatus="status">
		<div class="ubox grpList"><dl><dd 
		class="title"><a href="javascript:viewUserPop('${wlReprtGrpLVo.userUid }');">${wlReprtGrpLVo.userNm }</a><input type="hidden" name="userUid" value="${wlReprtGrpLVo.userUid }"
		/></dd><dd class="btn"><a class="delete" onclick="userSelectDel(this);" href="javascript:void(0);"><span>delete</span></a></dd></dl></div>
		</c:forEach></div></td>
	</tr>
	</c:if>
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt" colspan="3"><u:input id="subj" titleId="cols.subj" style="width:98%;" value="${wlTaskLogBVo.subj}" mandatory="Y" maxByte="240"/></td>
	</tr>
	<c:if test="${!empty columnList && !empty columnMap }">
		<c:set var="map" value="${columnMap }" scope="request"/>
	</c:if>
	<c:if test="${!empty userConfigMap}">
		<c:set var="userMap" value="${userConfigMap }" scope="request"/>
	</c:if>
	<c:forEach var="columnVo" items="${columnList }" varStatus="status">
		<c:if test="${!empty map}"><u:convertMap var="value" srcId="map" attId="${columnVo[0] }Cont" type="html" /></c:if>
		<tr>
		<td class="head_lt"><u:mandatory /><c:if test="${empty columnVo[1] }"><u:msg titleId="wl.cols.${columnVo[0] }" /></c:if><c:if test="${!empty columnVo[1] }">${columnVo[1] }</c:if></td>
		<td class="body_lt" colspan="3"><c:if test="${!empty userMap }"><u:convertMap var="colHeight" srcId="userMap" attId="${columnVo[0]}Height" type="html" 
		/></c:if><u:set var="heightVa" test="${!empty colHeight }" value="${colHeight }" elseValue="${columnVo[2] }"
		/><u:editor id="${columnVo[0] }Cont" width="100%" height="${!empty heightVa ? heightVa : '300'}px" module="wl" value="${value }" padding="2" /></td>
		</tr>
	</c:forEach>
</table>
</div>	
<c:if test="${!empty configMap.fileYn && configMap.fileYn eq 'Y'}">
<u:blank />
<u:listArea>
	<tr>
	<td><u:files id="${filesId}" fileVoList="${fileVoList}" module="wl" mode="set" exts="${exts }" extsTyp="${extsTyp }"/></td>
	</tr>
</u:listArea>
</c:if>

</form>

<% // 하단 버튼 %>
<u:buttonArea topBlank="true">
	<c:if test="${path eq 'recv' || wlTaskLogBVo.consolYn eq 'Y'}">
	<u:button titleId="wl.btn.task.select" alt="업무일지 선택" onclick="findLogListPop();" auth="W" />
	</c:if>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveLog();" auth="W" />
	<u:button titleId="cm.btn.tmpSave" alt="임시저장" onclick="saveTmp();" auth="W" />
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>