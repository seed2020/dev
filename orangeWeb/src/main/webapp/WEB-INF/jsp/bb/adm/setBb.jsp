<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:params var="params" excludes="brdId" />
<u:set test="${!empty baBrdBVo.brdId}" var="fnc" value="mod" elseValue="reg" />
<c:set var="bodySizeDefVal" value="307200" />
<c:set var="attSizeDefVal" value="10485760" />
<c:set var="attCapaDefVal" value="150" />

<script type="text/javascript">
<!--
<% // [하단버튼:양식불러오기] - 팝업 %>
function findFormPop(){
	var url='./findFormPop.do?menuId=${menuId}&mdTypCd=BB';
	parent.dialog.open('findFormDialog','<u:msg titleId="wf.jsp.form.list.title" alt="양식 목록" />', url);
}
<% // 확장테이블 Array %>
var exTbl = [];
<c:forEach items="${baTblBVoList}" var="baTblBVo" varStatus="status">
<c:if test="${baTblBVo.exYn == 'Y'}">exTbl.push('${baTblBVo.tblId}');</c:if>
</c:forEach>
<% // 테이블선택, 답변형여부 변경시 심의여부 비활성화 세팅 %>
function setDiscYn() {
	var tblId = $('#tblId > option:selected').val();
	var replyYn = $('#replyYn > option:selected').val();
	var $discYn = $('#discYn');
	if (exTbl.contains(tblId) || 'Y' == replyYn) {
		$discYn.val('N');
		setDisabled($discYn, true);
		$('#discYnMsg').show();
	} else {
		setDisabled($discYn, false);
		$('#discYnMsg').hide();
	}
}
<% // 답변형여부 변경시 포토게시판여부 비활성화 세팅 %>
function setPhotoYn() {
	var replyYn = $('#replyYn > option:selected').val();
	var $photoYn = $('#photoYn');
	if ('Y' == replyYn) {
		$photoYn.val('N');
		setDisabled($photoYn, true);
		$('#photoYnMsg').show();
	} else {
		setDisabled($photoYn, false);
		$('#photoYnMsg').hide();
	}
}
<% // 게시물 카테고리그룹 선택 %>
function selectBullCat(schWord) {
	dialog.open('selectBullCatPop','<u:msg titleId="bb.jsp.selectBullCatPop.title" alt="게시물 카테고리그룹 선택" />','./selectBullCatPop.do?menuId=${menuId}&selectedId=${baBrdBVo.catGrpId}');
}
<% // 게시물 카테고리그룹 변경 이벤트 %>
function changeCatYn(va) {
	if (va == 'Y') {
		$('#catGrpNm').show();
		$('#catGrpBtn').show();
	} else {
		$('#catGrpId').val('');
		$('#catGrpNm').val('');
		$('#catGrpBtn').off();
		$('#catGrpNm').hide();
		$('#catGrpBtn').hide();
	}
}
<% // 심의자 선택 %>
function searchDiscr(schWord) {
	var $view = $("#setBbForm");
	var data = {userUid:$view.find("#discrUid").val()};<% // 팝업 열때 선택될 데이타 %>
	<% // option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$view.find("#discrUid").val(userVo.userUid);
			$view.find("#discrNm").val(userVo.rescNm);
			// alert(new ParamMap(userVo));
		}else{
			return false;
		}
	});
}
<% // 심의여부 변경 이벤트 %>
function changeDiscYn(va) {
	if (va == 'Y') {
		$('#discrBtn').on('click', function() { searchDiscr(''); });
		$('#discrNm').show();
		$('#discrBtn').show();
	} else {
		$('#discrUid').val('');
		$('#discrNm').val('');
		$('#discrBtn').off();
		$('#discrNm').hide();
		$('#discrBtn').hide();
	}
}
<% // 종류 변경 이벤트 %>
function changeKndCd(va) {
	if (va == 'R') {
		setDisabled($('#rezvPrd'), false);
	} else {
		setDisabled($('#rezvPrd'), true);
	}
}
<% // 신규게시물표시 변경 이벤트 %>
function changeNewDispYn(va) {
	if (va == 'N') {
		setReadonly($('#newDispPrd'), true);
	} else {
		setReadonly($('#newDispPrd'), false);
	}
}
<% // 담당자 선택 %>
function searchPich(schWord) {
	var $view = $("#setBbForm");
	var data = {userUid:$view.find("#pichUid").val()};<% // 팝업 열때 선택될 데이타 %>
	<% // option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$view.find("#pichUid").val(userVo.userUid);
			$view.find("#pichNm").val(userVo.rescNm);
			$view.find("#pichDeptNm").val(userVo.deptRescNm);
			$view.find("#pichPhon").val(userVo.mbno);
			$view.find("#pichEmail").val(userVo.email);
			// alert(new ParamMap(userVo));
		}else{
			return false;
		}
	});
}
<% // 담당자 변경 이벤트 %>
function changePichDispYn(va) {
	if (va == 'Y') {
		$('#pichBtn').on('click', function() { searchPich(''); });
		$('#pichBtn').show();
	} else {
		$('#pichUid').val('');
		$('#pichNm').val('');
		$('#pichDeptNm').val('');
		$('#pichPhon').val('');
		$('#pichEmail').val('');
		$('#pichBtn').off();
		$('#pichBtn').hide();
	}
}
<% // 기본값사용 초기화 %>
function setDef(chk, id, val, defVal) {
	restoreUniform('setBbForm');
	var $chk = $('input[name="'+chk+'"]');
	var $input = $('#'+id);
	if (val == '' || defVal == val) {
		$chk.prop('checked', true);
		setReadonly($input, true);
		$input.val(defVal);
	} else {
		$chk.prop('checked', false);
		setReadonly($input, false);
		$input.val(val);
	}
	applyUniform('setBbForm');
}
<% // 기본값사용 onclick 이벤트 %>
function checkDef(chk, id, defVal) {
	var $chk = $('input[name="'+chk+'"]');
	var $input = $('#'+id);
	var checked = $chk.is(':checked');
	setReadonly($input, checked);
	if (checked) {
		$input.val(defVal);
	}
}
<% // [하단버튼:저장] 저장 %>
function saveBb() {
	if (validator.validate('setBbForm')) {
		var $form = $('#setBbForm');
		setJsonVa($form);
		$form.attr('method','post');
		$form.attr('action','./transBb.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}
<% // [하단버튼:취소] 취소 %>
function cancelBb() {
	if ($('#brdId').val() != '') {
		location.href = './viewBb.do?${params}&brdId=${baBrdBVo.brdId}';
	} else {
		history.go(-1);
	}
}

<% // 수정불가 항목 설정 %>
function initDisabled() {
	setDisabled($('#brdTypCd'), true);
	setDisabled($('#replyYn'), true);
	setDisabled($('#discYn'), true);
	setDisabled($('#tblId'), true);
}
<% // JSON 형식으로 변경  %>
function setJsonVa(form){
	var arr={}, attrs=[], name;
	$('#optVaArea input:not([type=hidden]):checked').each(function(){
		if($(this).val()!=''){
			name=$(this).attr('name');
			arr[name]=$(this).val();
			attrs.push(name);
		}
	});
	
	// 본문삽입용 양식 데이타
	$('#wfFormParamArea input[type=hidden]').each(function(){
		if($(this).val()!=''){
			name=$(this).attr('name');
			arr[name]=$(this).val();
			attrs.push(name);
		}
	});
	form.find("input[name='optVa']").remove();
	if(attrs.length>0){
		var optVa=getJsonVa(arr, attrs);		
		form.appendHidden({name:'optVa',value:optVa});
	}else{
		form.appendHidden({name:'optVa',value:''});
	}
}
<% // JSON 형식으로 변경  %>
function getJsonVa(obj, attrs){
	var buffer = new StringBuffer();
	buffer.append("{");
	attrs.each(function(index, attr){
		if(index!=0) buffer.append(', ');
		var va = obj[attr].replaceAll('"','\\"');
		buffer.append('"').append(attr).append('":"').append(escapeValue(va)).append('"');
	});
	buffer.append("}");
	return buffer.toString();
}
<% // 비공개 사용 선택시 게시대상 체크해제  %>
function chnPrivYn(obj, typ){
	if(!$(obj).is(':checked')) return;
	var chkObj=null;
	if(typ=='tgt')
		chkObj=$('#optVaArea input:checkbox[name="privUseYn"]');
	else if(typ=='priv')
		chkObj=$('#optVaArea input:checkbox[name="bbTgtDispYn"]');
	else return;
	
	if(chkObj!=null && chkObj.is(':checked')){
		restoreUniform('optVaArea');
		chkObj.prop('checked', false);
		applyUniform('optVaArea');
	}
		
}
<%
//[업무관리] 양식 열기 %>
function openWfFormList(){
	var url='./findFormPop.do?menuId=${menuId}&mdTypCd=BB&callback=setWfForm';
	parent.dialog.open('findFormDialog','<u:msg titleId="ap.cmpt.wfFormBody" alt="본문 (업무양식)" />', url);
}<%
//[업무관리] 양식 열기 - callback %>
function setWfForm(data){
	$("#wfFormNm").val(data.formNm);
	$("#wfFormNo").val(data.formNo);
	$("#wfGenId").val(data.genId);
	$("#wfRescId").val(data.rescId);
}<%
//[업무관리] 양식 삭제 %>
function delWfFormList(){
	$("#wfFormNm").val('');
	$("#wfFormNo").val('');
	$("#wfGenId").val('');
	$("#wfRescId").val('');
}
$(document).ready(function() {
	<c:if test="${fnc == 'mod'}">
	initDisabled();
	</c:if>
	setDiscYn();
	changeCatYn('${baBrdBVo.catYn}');
	changeDiscYn('${baBrdBVo.discYn}');
	changeKndCd('${baBrdBVo.kndCd}');
	changeNewDispYn('${baBrdBVo.newDispYn}');
	changePichDispYn('${baBrdBVo.pichDispYn}');
	setPhotoYn();

	setUniformCSS();

	setDef('bodySizeDef', 'bodySizeLim', '${baBrdBVo.bodySizeLim}', '${bodySizeDefVal}');
	setDef('attSizeDef', 'attSizeLim', '${baBrdBVo.attSizeLim}', '${attSizeDefVal}');
	setDef('attCapaDef', 'attCapaLim', '${baBrdBVo.attCapaLim}', '${attCapaDefVal}');
});
//-->
</script>

<u:title titleId="bb.jsp.setBb.${fnc}.title" alt="게시판 등록" menuNameFirst="true"/>

<form id="setBbForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="params" value="${params}" />
<u:input type="hidden" id="brdId" value="${baBrdBVo.brdId}" />

<% // 폼 필드 %>
<u:listArea>
	<tr>
	<td width="18%" class="head_lt"><u:mandatory /><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
	<td colspan="3"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convert srcId="${baBrdBVo.rescId}_${langTypCdVo.cd}" var="brdRescVa" />
			<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="brdRescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.bbNm" value="${brdRescVa}" style="${style}"
				maxByte="100" validator="changeLangSelector('setBbForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setBbForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="brdRescId" value="${baBrdBVo.rescId}" />
		</td>
		</tr></table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.desc" alt="설명" /></td>
	<td colspan="3"><u:textarea id="brdDesc" value="${baBrdBVo.brdDesc}" titleId="cols.desc" maxByte="400" style="width:95%" rows="3" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.deptBbYn" alt="부서게시판여부" /></td>
	<td><u:checkArea>
		<td><u:elemTitle titleId="cols.useYn" var="title" />
			<select id="deptBrdYn" name="deptBrdYn" title="${title}">
			<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${baBrdBVo.deptBrdYn}" />
			<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${baBrdBVo.deptBrdYn}" />
			</select></td>
		</u:checkArea></td>
	<td class="head_lt"><u:secu auth="SYS"><u:msg titleId="cols.allCompBbYn" alt="전사게시판여부" /></u:secu></td>
	<td><u:secu auth="SYS"><u:checkArea>
		<td><u:elemTitle titleId="cols.useYn" var="title" />
			<select id="allCompYn" name="allCompYn" title="${title}">
			<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${baBrdBVo.allCompYn}" />
			<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${baBrdBVo.allCompYn}" />
			</select></td>
		</u:checkArea></u:secu></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.tblChoi" alt="테이블선택" /></td>
	<td><select id="tblId" name="tblId" onchange="setDiscYn();">
		<c:forEach items="${baTblBVoList}" var="baTblBVo" varStatus="status">
		<u:option value="${baTblBVo.tblId}" title="${baTblBVo.rescNm}" checkValue="${baBrdBVo.tblId}" />
		</c:forEach>
		</select></td>
	<td class="head_lt"><u:msg titleId="cols.bullCat" alt="게시물카테고리" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:elemTitle titleId="cols.useYn" var="title" />
			<select id="catYn" name="catYn" ${title} onchange="changeCatYn(this.value);">
			<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${baBrdBVo.catYn}" />
			<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${baBrdBVo.catYn}" />
			</select></td>
		<td><u:input id="catGrpNm" value="${baBrdBVo.catGrpNm}" titleId="cols.cat" readonly="Y" style="width: 100px;" />
			<u:input type="hidden" id="catGrpId" value="${baBrdBVo.catGrpId}" /></td>
		<td><u:buttonS id="catGrpBtn" titleId="bb.btn.catChoi" alt="카테고리선택" onclick="selectBullCat();" /></td>
		</tr>
		</tbody></table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.bbTyp" alt="게시판타입" /></td>
	<td width="32%"><select id="brdTypCd" name="brdTypCd">
		<c:forEach items="${brdTypCdList}" var="brdTypCdVo" varStatus="status">
		<u:option value="${brdTypCdVo.cd}" title="${brdTypCdVo.rescNm}" checkValue="${baBrdBVo.brdTypCd}" />
		</c:forEach>
		</select></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.replyYn" alt="답변형여부" /></td>
	<td width="32%"><select id="replyYn" name="replyYn" onchange="setDiscYn();setPhotoYn();">
		<u:option value="Y" titleId="bb.option.replyY" alt="답변형" checkValue="${baBrdBVo.replyYn}" />
		<u:option value="N" titleId="bb.option.replyN" alt="비답변형" checkValue="${baBrdBVo.replyYn}" />
		</select></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.discYn" alt="심의여부" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><select id="discYn" name="discYn" onchange="changeDiscYn(this.value);">
			<u:option value="N" titleId="bb.option.discN" alt="미심의" checkValue="${baBrdBVo.discYn}" />
			<u:option value="Y" titleId="bb.option.discY" alt="심의" checkValue="${baBrdBVo.discYn}" />
			</select></td>
		<td id="discYnMsg" class="body_lt" style="display: none;">(<u:msg titleId="bb.jsp.setBb.tx04" alt="기본테이블, 비답변형일 때 선택 가능" />)</td>
		</tr>
		</tbody></table></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.discr" alt="심의자" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:input id="discrNm" titleId="cols.discr" value="${baBrdBVo.discrNm}" readonly="Y" />
			<u:input type="hidden" id="discrUid" value="${baBrdBVo.discrUid}" /></td>
		<td><u:buttonS id="discrBtn" titleId="cm.btn.search" alt="검색" /></td>
		</tr>
		</tbody></table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.knd" alt="종류" /></td>
	<td><select id="kndCd" name="kndCd" onchange="changeKndCd(this.value);">
		<c:forEach items="${kndCdList}" var="kndCdVo" varStatus="status">
		<u:option value="${kndCdVo.cd}" title="${kndCdVo.rescNm}" checkValue="${baBrdBVo.kndCd}" />
		</c:forEach>
		</select></td>
	<td class="head_lt"><u:msg titleId="cols.bullRezvPrd" alt="게시예약기간" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><select id="rezvPrd" name="rezvPrd">
			<u:option value="1" checkValue="${baBrdBVo.rezvPrd}" />
			<u:option value="2" checkValue="${baBrdBVo.rezvPrd}" />
			<u:option value="3" checkValue="${baBrdBVo.rezvPrd}" />
			<u:option value="4" checkValue="${baBrdBVo.rezvPrd}" />
			<u:option value="5" checkValue="${baBrdBVo.rezvPrd}" />
			<u:option value="6" checkValue="${baBrdBVo.rezvPrd}" />
			<u:option value="9" checkValue="${baBrdBVo.rezvPrd}" />
			<u:option value="12" checkValue="${baBrdBVo.rezvPrd}" />
			<u:option value="24" checkValue="${baBrdBVo.rezvPrd}" />
			<u:option value="36" checkValue="${baBrdBVo.rezvPrd}" />
			</select></td>
		<td class="body_lt"><u:msg titleId="cols.mth" alt="개월" /></td>
		</tr>
		</tbody></table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.notcDispPrd" alt="공지표시기간" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:input id="notcDispPrd" value="${baBrdBVo.notcDispPrd}" titleId="cols.notcDispPrd" style="width: 50px;" mandatory="Y" valueOption="number" /></td>
		<td class="body_lt"><u:msg titleId="bb.cols.day" alt="일" /></td>
		</tr>
		</tbody></table></td>
	<td class="head_lt"><u:msg titleId="cols.readHst" alt="조회이력" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:elemTitle titleId="cols.useYn" var="title" />
			<select id="readHstUseYn" name="readHstUseYn" ${title}>
			<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${baBrdBVo.readHstUseYn}" />
			<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${baBrdBVo.readHstUseYn}" />
			<u:option value="R" titleId="bb.option.use.readCnt" alt="사용(조회수만)" checkValue="${baBrdBVo.readHstUseYn}" />
			</select></td>
		<td class="body_lt">(※ <u:msg titleId="bb.jsp.setBb.tx03" alt="사용자 조회 이력을 기록합니다." />)</td>
		</tr></tbody></table></td>
	</tr>


	<tr>
	<td class="head_lt"><u:msg titleId="cols.newBullDisp" alt="신규게시물표시" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><select id="newDispYn" name="newDispYn" onchange="changeNewDispYn(this.value);">
			<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${baBrdBVo.newDispYn}" />
			<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${baBrdBVo.newDispYn}" />
			</select></td>
		<u:set test="${baBrdBVo.newDispPrd != null}" var="newDispPrd" value="${baBrdBVo.newDispPrd}" elseValue="1" />
		<td><u:input id="newDispPrd" value="${newDispPrd}" titleId="cols.newBullDisp" style="width: 20px"
			valueOption="number" maxInt="365" /></td>
		<td class="body_lt"><u:msg titleId="bb.jsp.setBb.tx01" alt="일 이내 표시" /></td>
		</tr>
		</tbody></table></td>
	<td class="head_lt"><u:msg titleId="cols.prevNext" alt="이전/다음" /></td>
	<td><u:elemTitle titleId="cols.useYn" var="title" />
		<select id="prevNextYn" name="prevNextYn" ${title}>
		<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${baBrdBVo.prevNextYn}" />
		<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${baBrdBVo.prevNextYn}" />
		</select></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.cmt" alt="한줄답변" /></td>
	<td><u:elemTitle titleId="cols.useYn" var="title" />
		<select id="cmtYn" name="cmtYn" ${title}>
		<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${baBrdBVo.cmtYn}" />
		<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${baBrdBVo.cmtYn}" />
		</select></td>
	<td class="head_lt"><u:msg titleId="cols.favot" alt="찬반투표" /></td>
	<td><u:elemTitle titleId="cols.useYn" var="title" />
		<select id="favotYn" name="favotYn" ${title}>
		<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${baBrdBVo.favotYn}" />
		<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${baBrdBVo.favotYn}" />
		</select></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.recmd" alt="추천" /></td>
	<td><u:elemTitle titleId="cols.useYn" var="title" />
		<select id="recmdUseYn" name="recmdUseYn" ${title}>
		<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${baBrdBVo.recmdUseYn}" />
		<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${baBrdBVo.recmdUseYn}" />
		</select></td>
	<td class="head_lt"><u:msg titleId="cols.scre" alt="점수" /></td>
	<td><u:elemTitle titleId="cols.useYn" var="title" />
		<select id="screUseYn" name="screUseYn" ${title}>
		<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${baBrdBVo.screUseYn}" />
		<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${baBrdBVo.screUseYn}" />
		</select></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.pichDisp" alt="담당자표시" /></td>
	<td><u:elemTitle titleId="cols.useYn" var="title" />
		<select id="pichDispYn" name="pichDispYn" ${title} onchange="changePichDispYn(this.value);">
		<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${baBrdBVo.pichDispYn}" />
		<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${baBrdBVo.pichDispYn}" />
		</select></td>
	<td class="head_lt"><u:msg titleId="cols.photoBbYn" alt="포토게시판여부" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:elemTitle titleId="cols.useYn" var="title" />
			<select id="photoYn" name="photoYn" ${title}>
			<u:option value="N" titleId="cm.option.notUse" alt="사용안함" checkValue="${baBrdBVo.photoYn}" />
			<u:option value="Y" titleId="cm.option.use" alt="사용" checkValue="${baBrdBVo.photoYn}" />
			</select></td>
		<td id="photoYnMsg" class="body_lt" style="display: none;">(<u:msg titleId="bb.jsp.setBb.tx05" alt="비답변형일 때 선택 가능" />)</td>
		</tr>
		</tbody></table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.pich" alt="담당자" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><u:input id="pichNm" value="${baBrdBVo.pichVo.rescNm}" titleId="cols.pich" readonly="Y" />
			<u:input type="hidden" id="pichUid" value="${baBrdBVo.pichUid}" /></td>
		<td><u:buttonS id="pichBtn" titleId="cm.btn.search" alt="검색" /></td>
		</tr>
		</tbody></table></td>
	<td class="head_lt"><u:msg titleId="cols.pichDept" alt="담당자 부서" /></td>
	<td><u:input id="pichDeptNm" value="${baBrdBVo.pichVo.deptRescNm}" titleId="cols.pichDept" readonly="Y" /></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.pichPhon" alt="담당자 전화번호" /></td>
	<td><u:input id="pichPhon" value="${baBrdBVo.pichPinfoVo.mbno}" titleId="cols.pichPhon" readonly="Y" /></td>
	<td class="head_lt"><u:msg titleId="cols.pichEmail" alt="담당자 이메일" /></td>
	<td><u:input id="pichEmail" value="${baBrdBVo.pichPinfoVo.email}" titleId="cols.pichEmail" readonly="Y" /></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="bb.cols.addOpt" alt="추가옵션" /></td>
	<td colspan="3"><u:checkArea id="optVaArea"><u:checkbox value="Y" name="brdCopyYn" titleId="bb.cols.brdCopy" alt="게시물 등록시 게시판 선택" checkValue="${baBrdBVo.optMap.brdCopyYn }"
	/><u:checkbox value="Y" name="bbTgtDispYn" titleId="bb.cols.bbTgt" alt="게시대상" checkValue="${baBrdBVo.optMap.bbTgtDispYn }" onclick="chnPrivYn(this, 'tgt');"
	/><u:checkbox value="Y" name="bbOptYn" titleId="bb.cols.bbOpt" alt="게시옵션" checkValue="${baBrdBVo.optMap.bbOptYn }" 
	/><u:checkbox value="Y" name="snsUploadYn" titleId="bb.sns.upload" alt="SNS 올리기" checkValue="${baBrdBVo.optMap.snsUploadYn }"
	/><u:checkbox value="Y" name="tmpSaveYn" titleId="cm.btn.tmpSave" alt="임시저장" checkValue="${baBrdBVo.optMap.tmpSaveYn }"
	/><u:checkbox value="Y" name="fileUploadYn" titleId="cols.attFile" alt="첨부파일" checkValue="${baBrdBVo.optMap.fileUploadYn }" checked="${empty baBrdBVo.optMap.fileUploadYn }"
	/><u:checkbox value="Y" name="listCondApplyYn" titleId="bb.cols.listCondApply" alt="목록 조회조건 적용" checkValue="${baBrdBVo.optMap.listCondApplyYn }"
	/><u:checkbox value="Y" name="privUseYn" titleId="bb.cols.priv.use" alt="비공개 사용" checkValue="${baBrdBVo.optMap.privUseYn }" onclick="chnPrivYn(this, 'priv');"
	/></u:checkArea></td>	
	</tr><c:if test="${sysPloc.wfEnable eq 'Y'}">
	<tr>
	<td class="head_lt"><u:msg titleId="ap.cmpt.wfFormBody" alt="본문 (업무양식)" /></td>
	<td colspan="3"><table id="wfFormParamArea" cellspacing="0" cellpadding="0" border="0"><tr>
			<td><input id="wfFormNm" name="wfFormNm" value="${wfFormBVo.formNm }" style="width:250px" /></td><td>
			<input type="hidden" id="wfFormNo" name="wfFormNo" value="${baBrdBVo.optMap.wfFormNo }" />
			<input type="hidden" id="wfGenId" name="wfGenId" value="${baBrdBVo.optMap.wfGenId }" />
			<input type="hidden" id="wfRescId" name="wfRescId" value="${baBrdBVo.optMap.wfRescId }" />
			<u:buttonS titleId="cm.btn.sel"  href="javascript:openWfFormList();" alt="선택" auth="A" 
			/></td><td><u:buttonS titleId="cm.btn.del"  href="javascript:delWfFormList();" alt="삭제" auth="A" /></td></tr></table></td>
	</tr>
	</c:if>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="wf.btn.hst.regFormAll" alt="전체 양식보기" onclick="findFormPop();" auth="A" />
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveBb();" auth="A" />
	<u:button titleId="cm.btn.cancel" alt="취소" onclick="cancelBb();" />
</u:buttonArea>

</form>

