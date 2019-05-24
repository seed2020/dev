<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="sendNo, recvNo, seqNo"/>
<u:authUrl var="savePltUrl" url="/cu/send/transNotePlt.do" authCheckUrl="/cu/send/listNote.do"/><!-- set 팝업 호출관련 url 조합(menuId추가) -->
<u:authUrl var="setNotePltUrl" url="/cu/recv/setNotePltPop.do" authCheckUrl="/cu/recv/listNote.do"/><!-- set 팝업 호출관련 url 조합(menuId추가) -->
<u:authUrl var="srchUserAjxUrl" url="/cu/recv/srchUserAjx.do" authCheckUrl="/cu/recv/listNote.do"/><!-- set 팝업 호출관련 url 조합(menuId추가) -->
<u:set var="srchUserAjx" test="${isPlt==true }" value="${srchUserAjxUrl }" elseValue="./srchUserAjx.do?menuId=${menuId}"/>

<script type="text/javascript">
<!--<% // [버튼클릭] - 사용자삭제  %>
function userSelectDel(obj){
	if(obj===undefined) $('#userSelectArea').html('');
	else $(obj).closest('div.ubox').remove();
};
<% // [검색어입력] - 사용자추가  %>
function addUser(){
	if($('#setForm #userNm').val()!=''){
		callAjax('${srchUserAjx}', {srchTyp:'user',srchName:$('#userNm').val()}, function(data) {
			if (data.returnString != null) {
				var idVaList=getUserList('userUid');
				var arr=[];
				arr.push($.parseJSON(data.returnString));
				setRowUser(arr, ["userUid","rescNm"], idVaList);
				return;
			}
			addRowUser();
		});
	}else{
		addRowUser();
	}
}
<% // [버튼클릭] - 사용자추가  %>
function addRowUser(){
	var data=[];
	var idVaList=getUserList('userUid');
	$.each(idVaList, function(index, userUid){
		data.push({userUid:userUid});
	});
	parent.searchUserPop({data:data, multi:true, mode:'search', userNm:encodeURIComponent($('#userNm').val())}, function(arr){
		if(arr!=null){
			setRowUser(arr, ["userUid","rescNm"], idVaList);
		}
	});
};<% // 사용자추가  %>
function setRowUser(arr, attrs, idVaList){	
	var buffer=[];
	var obj;
	arr.each(function(index, userVo){
		if($.inArray(userVo.userUid, idVaList)!=-1)
			return true;
		obj = {};
		attrs.each(function(index, attr){
			obj[attr] = userVo[attr];
		});
		buffer.push(createUbox(userVo.userUid, userVo.rescNm, getJsonVa(obj, ["userUid","rescNm"]), false));
	});
	if(buffer.length>0){
		$('#userSelectArea').append(buffer.join(''));
	}
}<% // 사용자 목록 생성  %>
function createUbox(idVa, text, jsonVa, isGrp){
	var buffer=[];
	buffer.push('<div class="ubox'+(isGrp ? ' grpList' : '')+'"><dl>');
	buffer.push('<dd class="title">');
	buffer.push('<a href="javascript:viewUserPop(\''+idVa+'\');">');
	buffer.push(text);
	buffer.push('</a>');
	buffer.push(jsonVa);
	buffer.push('</dd>');
	buffer.push('<dd class="btn"><a class="delete" onclick="userSelectDel(this);" href="javascript:void(0);"><span>delete</span></a></dd>');
	buffer.push('</dl></div>');
	return buffer.join('');
}<% // 보고대상 목록  %>
function getUserList(chkId){
	var idVaList=[], user;
	$('#userSelectArea input[id="idVa"]').each(function(){
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
}
<%// 저장%>
function save(){
	if (validator.validate('setForm')) {
		var $form = $('#setForm');
		if($('#userSelectArea input[id="idVa"]').length>0){
			var buffer=[];
			buffer.push('[');
			$('#userSelectArea input[id="idVa"]').each(function(index){
				if(index>0) buffer.push(',');
				buffer.push($(this).val());
			});
			buffer.push(']');
			$form.find("input[name='recvList']").remove();
			$form.appendHidden({name:'recvList',value:buffer.join('')});
		}else{
			alertMsg('cm.msg.noSelectedItem', callMsg('cols.user'));
			return;
		}
		
		$form.attr('method','post');
		<c:if test="${isPlt==true}">$form.attr('action', '${savePltUrl}');</c:if>
		<c:if test="${isPlt==false}">$form.attr('action','./transNote.do?menuId=${menuId}');</c:if>
		
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','dataframe');
		editor('cont').prepare();
		saveFileToForm('${filesId}', $form[0], null);
		//$form[0].submit();
		
		//dialog.close(this);
	}
};
<c:if test="${isPlt==true}">
<% // [답장버튼] - 등록 팝업 %>
function setReplyPop(recvNo){
	dialog.open('setNoteDialog','<u:msg titleId="cm.cols.send.note" alt="쪽지 보내기" />', '${setNotePltUrl}&recvNo='+recvNo);
}
</c:if>
<%// 팝업 닫기%>
function notePopClose(){
	dialog.close('setNoteDialog');
}
//-->
</script>
<div style="width:750px">
<c:if test="${viewMode == 'set' }">
<form id="setForm" name="setForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="listPage" value="./listNote.do?${paramsForList }" />

<% // 폼 필드 %>
<u:listArea colgroup="20%,">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.recvr" alt="받는사람" /></td>
		<td class="body_lt"><div style="display:block;width:100%;"><u:input id="userNm" value="" titleId="cols.guest" style="width: 80px;" onkeydown="if (event.keyCode == 13){addUser();return false;}" 
		/><u:buttonS href="javascript:addUser();" titleId="wl.btn.addUser" alt="사용자추가" auth="W" 
	/><u:buttonS href="javascript:userSelectDel();" titleId="wl.btn.delAll" alt="전체삭제" auth="W" 
	/></div><div id="userSelectArea" style="min-height:40px;"><c:if test="${!empty userInfos }"><div class="ubox"><dl><dd 
		class="title"><a href="javascript:viewUserPop('${userInfos[0] });">${userInfos[1] }</a>
		<input type="hidden" id="idVa" value='{"userUid":"${userInfos[0] }","rescNm":"${userInfos[1] }"}' 
		/></dd><dd class="btn"><a class="delete" onclick="userSelectDel(this);" href="javascript:void(0);"><span>delete</span></a></dd></dl></div></c:if></div></td>
	</tr><tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
		<td><u:input id="subj" titleId="cols.subj" style="width:96%;" mandatory="Y" maxByte="240"/></td>
	</tr>
</u:listArea>
<div id="contArea" class="listarea" style="width:100%; height:${empty namoEditorEnable ? 184 : 306}px; padding-top:2px"></div>
<u:editor id="cont" width="100%" height="${empty namoEditorEnable ? 180 : 300}px" module="cu" areaId="contArea" namoToolbar="wcPop" />
<u:listArea>
	<tr>
		<td>
			<u:files id="${filesId}" fileVoList="${fileVoList}" module="cu" mode="set" exts="${exts }" extsTyp="${extsTyp }" height="55"/>
		</td>
	</tr>
	</u:listArea>
<u:buttonArea>
	<u:button titleId="cm.btn.send" onclick="save();" alt="보내기" auth="W" />
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="취소" titleId="cm.btn.cancel" />
</u:buttonArea>

</form>
</c:if><c:if test="${viewMode == 'view' }">
<% // 폼 필드 %>
<u:listArea colgroup="20%,">
	<c:if test="${isRecvNote==true }"><tr>
		<td class="head_lt"><u:msg titleId="cm.cols.send.user" alt="보낸사람" /></td>
		<td class="body_lt"><a href="javascript:viewUserPop('${cuNoteRecvBVo.regrUid }');"><u:out value="${cuNoteRecvBVo.regrNm }" /></a></td>
	</tr></c:if><tr>
		<td class="head_lt"><u:msg titleId="cm.cols.${isRecvNote==true ? 'recv' : 'send' }.time" alt="보낸시간" /></td>
		<td class="body_lt"><u:out value="${cuNoteRecvBVo.regDt }" type="longdate"/></td>
	</tr>	<tr>
		<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td class="body_lt"><u:out value="${cuNoteRecvBVo.subj }" /></td>
	</tr>	<tr>
		<td colspan="2" class="body_lt" style="vertical-align:top;">
			<div style="overflow:auto;height:196px;" class="editor">${cuNoteRecvBVo.cont}</div>
		</td>
	</tr><tr>
		<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
		<td class="body_lt">
			<c:if test="${!empty fileVoList }"><u:files id="${filesId}_view" fileVoList="${fileVoList}" module="cu" mode="view" urlSuffix="${isAdmin==true ? '/adm' : '' }${isRecvNote==true ? '/recv' : '/send' }"/></c:if>
		</td>
	</tr>
</u:listArea>
<u:buttonArea>
	<c:if test="${isRecvNote==true && isAdmin ne true}"><u:button href="javascript:setReplyPop('${cuNoteRecvBVo.recvNo }');" alt="답장" titleId="cm.btn.recv.reply" /></c:if>
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</c:if>
</div>
