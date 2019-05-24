<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--<% // [버튼클릭] - 사용자삭제  %>
function userSelectDel(obj){
	if(obj===undefined) $('#userSelectArea').html('');
	else $(obj).closest('div.ubox').remove();
};<% // [버튼클릭] - 사용자추가  %>
function addRowUser(){
	var data=[];
	var idVaList=getUserList('userUid', 'U');
	$.each(idVaList, function(index, userUid){
		data.push({userUid:userUid});
	});
	parent.searchUserPop({data:data, multi:true, mode:'search'}, function(arr){
		if(arr!=null){
			setRowUser(arr, ["userUid","rescNm"], idVaList);
		}
	});
};<% // [팝업] - 담당자그룹추가  %>
function addRowGrp(){
	parent.dialog.open('findPichGrpListDialog', '<u:msg titleId="wh.jsp.pichGrp.title" alt="담당자그룹" />', './findPichGrpListPop.do?menuId=${menuId}&callback=setRowGrp');
};<% // 담당자그룹추가  %>
function setRowGrp(arr){	
	var buffer=[];
	var idVaList=getUserList('pichGrpId', 'G');
	arr.each(function(index, vo){
		if($.inArray(vo['pichGrpId'], idVaList)!=-1)
			return true;
		vo['pichTypCd']='G';
		buffer.push(createUbox(vo['pichGrpId'], vo['pichGrpNm'], 'G', getJsonVa(vo, ["pichGrpId","pichGrpNm","pichTypCd"]), false));
	});
	if(buffer.length>0){
		$('#userSelectArea').append(buffer.join(''));
	}
}<% // 사용자추가  %>
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
		obj['pichTypCd']='U';
		buffer.push(createUbox(userVo.userUid, userVo.rescNm, 'U', getJsonVa(obj, ["userUid","rescNm","pichTypCd"]), false));
	});
	if(buffer.length>0){
		$('#userSelectArea').append(buffer.join(''));
	}
}<% // 사용자 목록 생성  %>
function createUbox(idVa, text, pichTypCd, jsonVa, isGrp){
	var buffer=[];
	buffer.push('<div class="ubox'+(isGrp ? ' grpList' : '')+'"><dl>');
	buffer.push('<dd class="title">');
	if(pichTypCd=='U') buffer.push('<a href="javascript:viewUserPop(\''+idVa+'\');">');
	buffer.push(text);
	if(pichTypCd=='U') buffer.push('</a>');
	//buffer.push(jsonVa.replaceAll('"','\\"'));
	buffer.push(jsonVa);
	buffer.push('</dd>');
	buffer.push('<dd class="btn"><a class="delete" onclick="userSelectDel(this);" href="javascript:void(0);"><span>delete</span></a></dd>');
	buffer.push('</dl></div>');
	return buffer.join('');
}<% // 보고대상 목록  %>
function getUserList(chkId, pichTypCd){
	var idVaList=[], user;
	$('#userSelectArea input[id="idVa"]').each(function(){
		user=JSON.parse($(this).val());
		if(user['pichTypCd']==pichTypCd)
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
function saveMd() {
	if (validator.validate('setForm')) {
		var param = new ParamMap().getData('setForm');
		if($('#userSelectArea input[id="idVa"]').length>0){
			var buffer=[];
			buffer.push('[');
			$('#userSelectArea input[id="idVa"]').each(function(index){
				if(index>0) buffer.push(',');
				buffer.push($(this).val());
			});
			buffer.push(']');
			//alert(buffer.join(''));
			param.put('pichList', buffer.join(''));	
		}
		
		callAjax('./transMdAjx.do?menuId=${menuId}', param, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				parent.reloadFrame('mdTree', './treeMdFrm.do?menuId=${menuId}&mdId='+data.mdId);
			}
		});
	}
};
function openViewPop(idVa, typCd){
	if(typCd=='U'){
		parent.viewUserPop(idVa);
	}else if(typCd=='G'){
		parent.dialog.open('openViewDialog','<u:msg titleId="wh.jsp.pichList.title" alt="담당자목록" />','./viewPichGrpPop.do?menuId=${menuId}&pichGrpId='+idVa);
	}
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<form id="setForm" style="padding:10px;">
<input type="hidden" name="menuId" value="${menuId}" />
<input type="hidden" name="mdId" value="${whMdBVo.mdId}" />
<% // 폼 필드 %>
	<u:listArea id="grpArea" colgroup="15%,">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.md.nm" alt="모듈명" /></td>
		<td class="body_lt">
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${whMdBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="wh.cols.md.nm" value="${rescVa}" style="${style}"
								maxByte="120" validator="changeLangSelector('grpArea', id, va)" mandatory="Y" />
						</c:forEach>
						<u:input type="hidden" id="rescId" value="${whMdBVo.rescId}" />
					</td>
					<td id="langTypOptions">
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('grpArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
					</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="wh.cols.md.grp" alt="모듈그룹" /></td>
		<td class="body_lt"><select id="mdTypCd" name="mdTypCd" <u:elemTitle titleId="wh.cols.md.grp" />>
			<u:option value="F" title="Y" checkValue="${whMdBVo.mdTypCd}" />
			<u:option value="W" title="N" checkValue="${whMdBVo.mdTypCd}" selected="${empty whMdBVo.mdTypCd }"/>
			</select></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td class="body_lt">
			<u:checkArea>
				<u:radio name="useYn" value="Y" titleId="cm.option.use" alt="사용" checkValue="${whMdBVo.useYn }"  inputClass="bodybg_lt" checked="${empty whMdBVo.useYn }"/>
				<u:radio name="useYn" value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${whMdBVo.useYn }" inputClass="bodybg_lt" />
			</u:checkArea>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="wh.cols.hdl.pich" alt="처리담당자" /></td>
		<td class="body_lt"><div style="display:block;width:100%;"><u:buttonS href="javascript:addRowUser();" titleId="wl.btn.addUser" alt="사용자추가" auth="W" 
	/><u:buttonS href="javascript:addRowGrp();" titleId="wh.btn.addGrp" alt="그룹추가" auth="W" 
	/><u:buttonS href="javascript:userSelectDel();" titleId="wl.btn.delAll" alt="전체삭제" auth="W" 
	/></div><div id="userSelectArea" style="min-height:40px;"><c:forEach 
		var="whMdPichLVo" items="${whMdPichLVoList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title"><a href="javascript:openViewPop('${whMdPichLVo.idVa }', '${whMdPichLVo.pichTypCd }');">${whMdPichLVo.pichNm }</a>
		<input type="hidden" id="idVa" value='{"${whMdPichLVo.pichTypCd eq 'G' ? 'pichGrpId' : 'userUid' }":"${whMdPichLVo.idVa }","pichTypCd":"${whMdPichLVo.pichTypCd }"}' /></dd><dd class="btn"><a class="delete" onclick="userSelectDel(this);" href="javascript:void(0);"><span>delete</span></a></dd></dl></div>
		</c:forEach></div></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="wh.cols.hdl.typ" alt="처리유형" /></td>
		<td class="body_lt"><select name="catGrpId" style="min-width:100px;" <u:elemTitle titleId="wh.cols.md.grp" />
		><c:forEach var="whCatGrpBVo" items="${whCatGrpBVoList }" varStatus="status"
		><u:option value="${whCatGrpBVo.catGrpId}" title="${whCatGrpBVo.catGrpNm}" checkValue="${whMdBVo.catGrpId}" selected="${(empty whMdBVo.catGrpId && whCatGrpBVo.dftYn eq 'Y') || (whCatGrpBVo.dftYn ne 'Y' && empty whMdBVo.catGrpId && status.first)}"/></c:forEach></select></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.regDt" alt="등록일" /></td>
		<td class="body_lt"><u:out value="${whMdBVo.regDt }" type="longdate"/></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.modDt" alt="수정일" /></td>
		<td class="body_lt"><u:out value="${whMdBVo.modDt }" type="longdate"/></td>
	</tr>
	
</u:listArea>
</form>