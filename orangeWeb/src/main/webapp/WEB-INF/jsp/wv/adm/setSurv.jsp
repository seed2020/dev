<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:set test="${!empty param.fnc}" var="fnc" value="${param.fnc}" elseValue="reg" />

<script type="text/javascript">
<!--

$(function(){
	setVoteTgtDept('${voteTgtDeptCnt}');
	setVoteTgtUser('${voteTgtUserCnt}');
	setChkTgtDept('${chkTgtDeptCnt}');
	setChkTgtUser('${chkTgtUserCnt}');
	
	$("#subj").focus();
});

function survDel(survId){
	
	 if(confirmMsg("cm.cfrm.del")) { <% // cm.cfrm.del=삭제하시겠습니까? %>
			callAjax('./transSetSurvDel.do?menuId=${menuId}', {survId:survId}, function(data) {
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					//dialog.close(this);
					location.href = './${listpage}.do?menuId=${menuId}';
				}
			});
	 }
}

function setSurvAuth() {
	dialog.open('setSurvAuthPop', '<u:msg titleId="wv.cols.set.right" alt="권한설정" />', './setSurvAuthPop.do?menuId=${menuId}');
}

function formSubit(){
    
    var edit1Check = editor('editor1').empty();
	var edit2Check = editor('editor2').empty();
	
	// 투표 및 조회 부서&유저 ID 목록
	var voteChkTgtIds = [];
	
	// 투표 권한 부서 ID 목록
	var voteTgtDeptIds = [];
	var $voteTgtDeptList = $("#voteTgtDeptHidden").find("input[name='orgId']");
	$voteTgtDeptList.each(function(){
		voteTgtDeptIds.push($(this).val());
	});
	voteChkTgtIds.push("<input id='voteTgtDeptIds' name='voteTgtDeptIds' type='hidden' value ='"+voteTgtDeptIds+"'/>");
	
	//투표 권한 부서 - 하위부서포함여부
	var voteWithSubYns = [];
	var $voteWithSubYnList = $("#voteTgtDeptHidden").find("input[name='withSubYn']");
	$voteWithSubYnList.each(function(){
		voteWithSubYns.push($(this).val());
	});
	voteChkTgtIds.push("<input id='voteWithSubYns' name='voteWithSubYns' type='hidden' value ='"+voteWithSubYns+"'/>");
	
	// 투표 권한 유저 ID 목록
	var voteTgtUserIds = [];
	var $voteTgtUserList = $("#voteTgtUserHidden").find("input[name='userUid']");
	$voteTgtUserList.each(function(){
		voteTgtUserIds.push($(this).val());
	});
	voteChkTgtIds.push("<input id='voteTgtUserIds' name='voteTgtUserIds' type='hidden' value ='"+voteTgtUserIds+"'/>");
	
	// 조회 권한 부서 ID 목록
	var chkTgtDeptIds = [];
	var $chkTgtDeptList = $("#chkTgtDeptHidden").find("input[name='orgId']");
	$chkTgtDeptList.each(function(){
		chkTgtDeptIds.push($(this).val());
	});
	voteChkTgtIds.push("<input id='chkTgtDeptIds' name='chkTgtDeptIds' type='hidden' value ='"+chkTgtDeptIds+"'/>");
	
	//조회 권한 부서 - 하위부서포함여부
	var chkWithSubYns = [];
	var $chkWithSubYnList = $("#chkTgtDeptHidden").find("input[name='withSubYn']");
	$chkWithSubYnList.each(function(){
		chkWithSubYns.push($(this).val());
	});
	voteChkTgtIds.push("<input id='chkWithSubYns' name='chkWithSubYns' type='hidden' value ='"+chkWithSubYns+"'/>");
	
	
	// 조회 권한 유저 ID 목록
	var chkTgtUserIds = [];
	var $chkTgtUserList = $("#chkTgtUserHidden").find("input[name='userUid']");
	$chkTgtUserList.each(function(){
		chkTgtUserIds.push($(this).val());
	});
	voteChkTgtIds.push("<input id='chkTgtUserIds' name='chkTgtUserIds' type='hidden' value ='"+chkTgtUserIds+"'/>");
	
	$("#voteChkTgt").html(voteChkTgtIds.join(''));
	
	
	if(validator.validate('setSurv')){
		if(edit1Check == true){
			alert("<u:msg titleId="wv.msg.set.obj" alt="'취지'(을)를 입력해 주십시오." />");
			//return false;
			//$dd.attr("mandatory", "Y");
		}else{
			
			if (isInUtf8Length($('#editor1').val(), 0, '${bodySize}') > 0) {
				alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.itnt" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
				return;
			}
			
			
			if (isInUtf8Length($('#editor2').val(), 0, '${bodySize}') > 0) {
				alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="wv.cols.greeting" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
				return;
			}
			
			var $form = $('#setSurv');
			if(voteTgtDeptIds.length > 0 || voteTgtUserIds.length > 0 || chkTgtDeptIds.length > 0 || chkTgtUserIds.length > 0){
				$form.find('input[name="noAuthYn"]').val('N');
			}else{
				$form.find('input[name="noAuthYn"]').val('Y');
			}
			
		    $form.attr('method','post');
		    $form.attr('enctype','multipart/form-data');
		    $form.attr('action','./transSetSurvQuesNextSave.do?menuId=${menuId}&fnc=${fnc}&survId=${wvsVo.survId}');
		    editor('editor1').prepare();
		    editor('editor2').prepare();
		    saveFileToForm('wvfiles', $form[0], null);
		    //$form.submit();

		}

	}
	
	
}

function compareDate(setAnnvChoiDt){
	$("#seledtBox option:eq(0)").attr("selected", "selected");
	$("#seledtBox").trigger('click');
	return todayCompare(setAnnvChoiDt);
}

function todayCompare(setAnnvChoiDt){
	var now = new Date();
	var todayAtMidn = new Date(now.getFullYear(), now.getMonth(), now.getDate());

	var startDate = new Date(setAnnvChoiDt);

	if (todayAtMidn.getTime() > startDate.getTime()) {
		alert('<u:msg titleId="wv.msg.set.chkDt" alt="당일보다 이전일을 선택하실 수 없습니다." />');
		
		return false;
	}
	else {
		return true;
	}
}


<% // 투표권한 -게시대상 부서 표시 %>
function setVoteTgtDept(len) {
	var $nameTd = $("#voteTgtDept");
	if (len > 0) {
		$nameTd.html('<u:msg titleId="wv.cols.set.dept" alt="부서" /> ' + len);
		$nameTd.show();
	} else {
		$nameTd.html('');
		$nameTd.hide();
	}
}
<% // 투표권한 - 게시대상 사용자 표시 %>
function setVoteTgtUser(len) {
	var $nameTd = $("#voteTgtUser");
	if (len > 0) {
		$nameTd.html('<u:msg titleId="wv.cols.set.user" alt="사용자" /> ' + len);
		$nameTd.show();
	} else {
		$nameTd.html('');
		$nameTd.hide();
	}
}

<% // 조회권한 -게시대상 부서 표시 %>
function setChkTgtDept(len) {
	
	var $nameTd = $("#chkTgtDept");
	if (len > 0) {
		$nameTd.html('<u:msg titleId="wv.cols.set.dept" alt="부서" /> ' + len);
		$nameTd.show();
	} else {
		$nameTd.html('');
		$nameTd.hide();
	}
}
<% // 조회권한 - 게시대상 사용자 표시 %>
function setChkTgtUser(len) {
	var $nameTd = $("#chkTgtUser");
	if (len > 0) {
		$nameTd.html('<u:msg titleId="wv.cols.set.user" alt="사용자" /> ' + len);
		$nameTd.show();
	} else {
		$nameTd.html('');
		$nameTd.hide();
	}
}

Date.prototype.yyyymmdd = function()
{
    var yyyy = this.getFullYear().toString();
    var mm = (this.getMonth() + 1).toString();
    var dd = this.getDate().toString();


    return yyyy + "-"+(mm[1] ? mm : '0'+mm[0]) + "-"+(dd[1] ? dd : '0'+dd[0]);
}

//설문 기간 선택
function survTermChoi(v) { 
	var $startdt = $("#strtDt").val();
	if($startdt.length != 10) return;
	var $finishDt = $("#finDt").val();
	var strtYmd = $startdt.split('-');
	var yyyy = strtYmd[0],mm=strtYmd[1],dd=strtYmd[2];
	var $myDate = new Date(yyyy,parseInt(mm)-1,dd);

	if(v=="<u:msg titleId="wv.cols.set.week1" alt="1주" />"){
		$myDate.setDate($myDate.getDate()+7);		
	}else if(v=="<u:msg titleId="wv.cols.set.week2" alt="2주" />"){
		$myDate.setDate($myDate.getDate()+14);	
	}else if(v=="<u:msg titleId="wv.cols.set.week3" alt="3주" />"){
		$myDate.setDate($myDate.getDate()+21);
	}else if(v=="<u:msg titleId="wv.cols.set.week4" alt="4주" />"){
		$myDate.setDate($myDate.getDate()+28);
	}else if(v=="<u:msg titleId="wv.cols.set.month" alt="한달" />"){
		$myDate.setMonth($myDate.getMonth()+1);
	}else{
	}
	var $stringDate = $myDate.yyyymmdd();
	var $val = $('#finDt').val($stringDate);

}

function setToday(){
	var newDate = new Date();
	var yy = newDate.getFullYear().toString();
	var mm = (newDate.getMonth() + 1).toString();
	var dd = newDate.getDate().toString();
	var toDay = yy + "-" + (mm[1] ? mm : '0'+mm[0]) + "-" + (dd[1] ? dd : '0'+dd[0]);
	return toDay;
}


 // 투표권한 - 다중 부서 선택 - 하위부서 여부 포함 
function voteAurhOpenMuiltiOrgWithSub(mode){
	var $inputTd = $("#voteTgtDeptHidden"), data = [];// data: 팝업 열때 오른쪽에 뿌릴 데이타 
	var $subs = $inputTd.find("input[name='withSubYn']");
	$inputTd.find("input[name='orgId']").each(function(index){
		data.push({orgId:$(this).val(), withSub:$($subs[index]).val()});
	});
	// option : data, multi, withSub, titleId 
	searchOrgPop({data:data, multi:true, withSub:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, orgVo){
				buffer.push("<input name='orgId' type='hidden' value='"+orgVo.orgId+"' />\n");
				buffer.push("<input name='withSubYn' type='hidden' value='"+orgVo.withSub+"' />\n");
				buffer.push("\n");
			});
			$inputTd.html(buffer.join(''));
			setVoteTgtDept(arr.length);
		} else {
			$inputTd.html('');
			setVoteTgtDept(0);
		}
		//return false;// 창이 안닫힘
	});
}
 // 투표권한 - 여러명의 사용자 선택 
function voteAurhOpenMuiltiUser(mode){
	var $inputTd = $("#voteTgtUserHidden"), data = [];// data: 팝업 열때 오른쪽에 뿌릴 데이타 
	$inputTd.find("input[name='userUid']").each(function(){
		data.push({userUid:$(this).val()});
	});
	 // option : data, multi, titleId
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, userVo){
				buffer.push("<input name='userUid' type='hidden' value='"+userVo.userUid+"' />\n");
				buffer.push("\n");
			});
			$inputTd.html(buffer.join(''));
			setVoteTgtUser(arr.length);
		} else {
			$inputTd.html('');
			setVoteTgtUser(0);
		}
	});
}

 // 조회권한 - 다중 부서 선택 - 하위부서 여부 포함 
function chkAurhOpenMuiltiOrgWithSub(mode){
	var $inputTd = $("#chkTgtDeptHidden"), data = []; // data: 팝업 열때 오른쪽에 뿌릴 데이타 
	var $subs = $inputTd.find("input[name='withSubYn']");
	$inputTd.find("input[name='orgId']").each(function(index){
		data.push({orgId:$(this).val(), withSub:$($subs[index]).val()});
	});
	// option : data, multi, withSub, titleId 
	searchOrgPop({data:data, multi:true, withSub:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, orgVo){
				buffer.push("<input name='orgId' type='hidden' value='"+orgVo.orgId+"' />\n");
				buffer.push("<input name='withSubYn' type='hidden' value='"+orgVo.withSub+"' />\n");
				buffer.push("\n");
			});
			$inputTd.html(buffer.join(''));
			setChkTgtDept(arr.length);
		} else {
			$inputTd.html('');
			setChkTgtDept(0);
		}
		//return false;// 창이 안닫힘
	});
}
// 조회권한 - 여러명의 사용자 선택 
function chkAurhOpenMuiltiUser(mode){
	var $inputTd = $("#chkTgtUserHidden"), data = [];// data: 팝업 열때 오른쪽에 뿌릴 데이타 
	$inputTd.find("input[name='userUid']").each(function(){
		data.push({userUid:$(this).val()});
	});
	// option : data, multi, titleId
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, userVo){
				buffer.push("<input name='userUid' type='hidden' value='"+userVo.userUid+"' />\n");
				buffer.push("\n");
			});
			$inputTd.html(buffer.join(''));
			setChkTgtUser(arr.length);
		} else {
			$inputTd.html('');
			setChkTgtUser(0);
		}
	});
}

//사진 변경 - 팝업 오픈 
function setImagePop(){
	dialog.open('setImageDialog','<u:msg titleId="or.jsp.setOrg.photoTitle" alt="사진 선택" />','./setImagePop.do?menuId=${menuId}&bcId=${param.bcId}');
}

//사진 변경 - 후처리 
function setImage( filePath, width, height){
	var $img = $("#bcImage");
	$img.attr("src", filePath);
	if($img.parent().tagName()=='a'){
		$img.parent().attr("href","javascript:viewImageDetl('${param.bcId}');");
	}
	dialog.close('setImageDialog');
}



$(document).ready(function() {
	setUniformCSS();
	
	if("${fnc}" != 'mod'){
		$("#strtDt").val(setToday());
		$("#finDt").val(setToday());
		
	}
	
	$("#subj").focus();
	
});

//-->
</script>
<%
	com.innobiz.orange.web.wv.vo.WvSurvBVo wvSurvBVo = (com.innobiz.orange.web.wv.vo.WvSurvBVo)request.getAttribute("wvsVo");
	if(wvSurvBVo != null){
		String _survItnt = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(wvSurvBVo.getSurvItnt());
		request.setAttribute("_survItnt", _survItnt);
		String _survFtr = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(wvSurvBVo.getSurvFtr());
		request.setAttribute("_survFtr", _survFtr);
	}
%>

<u:title titleId="wv.jsp.setSurv.title" alt="설문등록" menuNameFirst="true"/>

<form id="setSurv">
<u:input type="hidden" id="menuId" value="${menuId}" />
<!-- 비권한여부 -->
<u:input type="hidden" id="noAuthYn" value="${wvsVo.noAuthYn}" />

<div id="voteTgtDeptHidden">
	<c:forEach var="voteTgtDeptUid" items="${voteTgtDeptList}" varStatus="status">
		<input id="orgId" name="orgId" type="hidden" value="${voteTgtDeptUid.authTgtUid}"/>
		<input name="withSubYn" type="hidden" value="${voteTgtDeptUid.authInclYn}" />
	</c:forEach>
</div>
<div id="voteTgtUserHidden">
	<c:forEach var="voteTgtUserUid" items="${voteTgtUserList}" varStatus="status">
		<input id="userUid" name="userUid" type="hidden" value="${voteTgtUserUid.authTgtUid}"/>
	</c:forEach>
</div>
<div id="chkTgtDeptHidden">
	<c:forEach var="chkTgtDeptUid" items="${chkTgtDeptList}" varStatus="status">
		<input id="orgId" name="orgId" type="hidden" value="${chkTgtDeptUid.authTgtUid}"/>
		<input name="withSubYn" type="hidden" value="${chkTgtDeptUid.authInclYn}" />
	</c:forEach>
</div>
<div id="chkTgtUserHidden">
	<c:forEach var="chkTgtUserUid" items="${chkTgtUserList}" varStatus="status">
		<input id="userUid" name="userUid" type="hidden" value="${chkTgtUserUid.authTgtUid}"/>
	</c:forEach>
</div>
<div id="voteChkTgt"></div>

<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="red_stxt"><u:msg titleId="wv.jsp.setSurv.tx01" alt="* 설문 문항은 다음 화면에서 등록(수정)합니다" /></td>
		</tr>
		</table>
	</div>
</div>

<% // 폼 필드 %>
<u:listArea>
	<tr>
		<td width="18%" class="head_lt">
			<u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
		<td width="18">
			<u:input id="subj" value="${wvsVo.survSubj}" titleId="cols.subj" style="width: 99%;"  mandatory="Y" maxByte="256"/></td>
	</tr>

	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.itnt" alt="취지" /></td>
	<td id="editor1Area" style="padding:2px;height:202px;vertical-align:middle;">
		<u:input id="edit1Check" name="edit1Check" type="hidden" mandatory="Y" />
		<u:editor id="editor1" width="100%" height="200" module="wv" areaId="editor1Area"  value="${_survItnt}" />
	</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="wv.cols.greeting" alt="하단인사말" /></td>
	<td id="editor2Area" style="padding:2px;height:202px;vertical-align:middle;">
		<u:editor id="editor2" width="100%" height="200" module="wv" areaId="editor2Area" value="${_survFtr}"/>
	</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.prd" alt="기간" /></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="body_lt"><u:msg titleId="cols.strtDt" alt="시작일시" /></td>
		<td class="width5"></td>
		
		<td ><u:calendar id="strtDt" option="{end:'finDt'}"  handler="function(date,option){return compareDate(date);}" titleId="cols.choiDt" alt="선택일시" mandatory="Y" value="${wvsVo.survStartDt}" /></td>
		<td>&nbsp;</td>
		</tr>

		
		<tr>
		<td class="body_lt"><u:msg titleId="cols.finDt" alt="마감일시" /></td>
		<td class="width5"></td>
		<td ><u:calendar id="finDt" option="{start:'strtDt'}"  handler="function(date,option){return compareDate(date);}"  value="${wvsVo.survEndDt}"/></td>
		
		<td>
		 	<select id="seledtBox" onChange=javascript:survTermChoi(this.value);>
				<option id="0" value="<u:msg titleId="wv.cols.set.sel" alt="선택" />"><u:msg titleId="wv.cols.set.selPeriod" alt="투표 기간 선택" /></option>
				<option id="1" value="<u:msg titleId="wv.cols.set.week1" alt="1주" />"><u:msg titleId="wv.cols.set.week1" alt="1주" /></option>
				<option id="2" value="<u:msg titleId="wv.cols.set.week2" alt="2주" />"><u:msg titleId="wv.cols.set.week2" alt="2주" /></option>
				<option id="3" value="<u:msg titleId="wv.cols.set.week3" alt="3주" />"><u:msg titleId="wv.cols.set.week3" alt="3주" /></option>
				<option id="4" value="<u:msg titleId="wv.cols.set.week4" alt="4주" />"><u:msg titleId="wv.cols.set.week4" alt="4주" /></option>
				<option id="5" value="<u:msg titleId="wv.cols.set.month" alt="한달" />"><u:msg titleId="wv.cols.set.month" alt="한달" /></option>
			</select>
		</td>
		</tr>
		</table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="wv.cols.resView" alt="결과보기" /></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		
		<tr>
		<!-- 
		<td><u:radio name="resPublYn" value="publ" titleId="cm.option.publ" checked="${fnc == 'mod'}" /></td>
		<td><u:radio name="resPublYn" value="priv" titleId="cm.option.priv" /></td>
		 -->
		 <c:if test="${fnc == 'mod'}">
		 	<c:choose>
				<c:when test="${wvsVo.openYn == 'Y'}"> 
					<c:set var="pulb"	value= "true" />
					<c:set var="priv"	value= "false" />
				</c:when>
				<c:when test="${wvsVo.openYn == 'N'}"> 
					<c:set var="pulb"	value= "false" />
					<c:set var="priv"	value= "true" />
				</c:when>
			</c:choose>
		</c:if>
		<c:if test="${fnc == 'reg'}">
			<c:set var="pulb"	value= "true" />
		</c:if>

		
		<td><u:radio id="resPublYn" name="resPublYn" value="Y" titleId="cm.option.publ" checked="${pulb}" /></td>
		<td><u:radio id="resPublYn" name="resPublYn" value="N" titleId="cm.option.priv" checked="${priv}"/></td>
		</tr>
		</table></td>
	</tr>
	
	

	<tr>
	<td class="head_lt"><u:msg titleId="wv.cols.resurv" alt="재설문" /></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		
		<tr>
		<!-- 
		<td><u:radio name="resurvPosbYn" value="posb" titleId="cm.option.posb" checked="${fnc == 'mod'}" /></td>
		<td><u:radio name="resurvPosbYn" value="imps" titleId="cm.option.imps" /></td>
		 -->
		 
		 

			<c:if test="${fnc == 'mod'}">
				<c:choose>
					<c:when test="${wvsVo.repetSurvYn == 'Y'}"> 
						<c:set var="posb"	value= "true" />
						<c:set var="imps"	value= "false" />
					</c:when>
					<c:when test="${wvsVo.repetSurvYn == 'N'}"> 
						<c:set var="posb"	value= "false" />
						<c:set var="imps"	value= "true" />
					</c:when>
				</c:choose>
			</c:if>
			<c:if test="${fnc == 'reg'}">
				<c:set var="posb"	value= "true" />
			</c:if>
		
		 
		<td><u:radio id="resurvPosbYn" name="resurvPosbYn" value="Y" titleId="cm.option.posb" checked="${posb}" /></td>
		<td><u:radio id="resurvPosbYn" name="resurvPosbYn" value="N" titleId="cm.option.imps" checked="${imps}" /></td>
		</tr>
		</table></td>
	</tr>

	<tr>
		<td colspan="4">
		<u:files id="wvfiles" fileVoList="${fileVoList}" module="wv" mode="set" exts="${exts }" extsTyp="${extsTyp }"/></td>
		
	</tr>
	
	<tr>
		<td class="head_lt"><u:msg titleId="wv.cols.set.wr" alt="투표 권한" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td id="voteTgtDept" style="display: none;" class="body_lt"></td>
		<td><u:buttonS titleId="bb.btn.dept" alt="부서" onclick="voteAurhOpenMuiltiOrgWithSub()" /></td>
		<td id="voteTgtUser" style="display: none;" class="body_lt"></td>
		<td><u:buttonS titleId="bb.btn.user" alt="사용자" onclick="voteAurhOpenMuiltiUser()" /></td>
		</tr>
		</tbody></table></td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="wv.cols.set.rd" alt="조회 권한" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td id="chkTgtDept" style="display: none;" class="body_lt"></td>
		<td><u:buttonS titleId="bb.btn.dept" alt="부서" onclick="chkAurhOpenMuiltiOrgWithSub()" /></td>
		<td id="chkTgtUser" style="display: none;" class="body_lt"></td>
		<td><u:buttonS titleId="bb.btn.user" alt="사용자" onclick="chkAurhOpenMuiltiUser()" /></td>
		</tr>
		</tbody></table></td>
	</tr>
</u:listArea>

<u:blank />

<u:buttonArea>
	
	<u:button titleId="cm.btn.next" alt="다음" onclick="editor('editor1').prepare(); editor('editor2').prepare();" href="javascript:formSubit();" />
	<c:if test="${wvsVo.survPrgStatCd == '6'}">
			<u:button titleId="cm.btn.del" alt="삭제" href="javascript:survDel('${wvsVo.survId}');"/>
	</c:if>
	
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>

<u:blank />

</form>

