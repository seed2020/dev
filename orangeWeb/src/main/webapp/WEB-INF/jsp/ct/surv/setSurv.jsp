<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

$(function(){
	//setVoteTgtDept('${voteTgtDeptCnt}');
	//setVoteTgtUser('${voteTgtUserCnt}');
	//setChkTgtDept('${chkTgtDeptCnt}');
	//setChkTgtUser('${chkTgtUserCnt}');
	
	
	
	$("#subj").focus();
});

function setSurvAuth() {
	dialog.open('setSurvAuthPop', '<u:msg titleId="wv.cols.set.right" alt="권한설정" />', './setSurvAuthPop.do?menuId=${menuId}');
}

function formSubit(){
    
    var edit1Check = editor('editor1').empty();

	/*
	// 투표 및 조회 부서&유저 ID 목록
	var voteChkTgtIds = [];
	
	// 투표 권한 부서 ID 목록
	var voteTgtDeptIds = [];
	var $voteTgtDeptList = $("#voteTgtDeptHidden").find("input[name='orgId']");
	$voteTgtDeptList.each(function(){
		voteTgtDeptIds.push($(this).val());
	});
	voteChkTgtIds.push("<input id='voteTgtDeptIds' name='voteTgtDeptIds' type='hidden' value ='"+voteTgtDeptIds+"'/>");
	
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
	
	// 조회 권한 유저 ID 목록
	var chkTgtUserIds = [];
	var $chkTgtUserList = $("#chkTgtUserHidden").find("input[name='userUid']");
	$chkTgtUserList.each(function(){
		chkTgtUserIds.push($(this).val());
	});
	voteChkTgtIds.push("<input id='chkTgtUserIds' name='chkTgtUserIds' type='hidden' value ='"+chkTgtUserIds+"'/>");
	
	$("#voteChkTgt").html(voteChkTgtIds.join(''));
	*/
	
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
			
			
			if($('input:checkbox[name="tgt"]').is(":checked") == false){
				alert("<u:msg titleId="wv.msg.set.tgt" alt="'대상' (을)를 선택 해주십시오." />");	
				return;
			}
			
			
			var $form = $('#setSurv');
		    $form.attr('method','post');
		    $form.attr('enctype','multipart/form-data');
		    $form.attr('action','./transSetSurvQuesNextSave.do?menuId=${menuId}&fnc=${fnc}&survId=${ctsVo.survId}&ctId=${ctId}');
		    editor('editor1').prepare();
		    editor('editor2').prepare();
		    $form.submit();

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
	searchOrgPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
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
	searchOrgPop({data:data, multi:true,  mode:mode==null ?'search':'view'}, function(arr){
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


<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="subj" value="점심 메뉴에 대한 투표" elseValue="" />
<u:set test="${fnc == 'mod'}" var="itnt" value="점심 메뉴에 대한 직원들의 만족도를 조사하고자 합니다" elseValue="" />
<u:set test="${fnc == 'mod'}" var="greeting" value="투표에 응해 주셔서 감사합니다." elseValue="" />
<u:set test="${fnc == 'mod'}" var="strtDt" value="2014-02-14" elseValue="" />
<u:set test="${fnc == 'mod'}" var="finDt" value="2014-02-21" elseValue="" />
<u:set test="${fnc == 'mod'}" var="selected" value=" selected" elseValue="" />

<u:title title="${menuTitle }" alt="투표등록" menuNameFirst="true"/>

<form id="setSurv">
<u:input type="hidden" id="menuId" value="${menuId}" />

<div class="front">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="red_stxt">* <u:msg titleId="ct.jsp.setSurv.tx01" alt="투표 문항은 다음 화면에서 등록(수정)합니다" /></td>
		</tr>
		</table>
	</div>
</div>

<% // 폼 필드 %>
<u:listArea>
	<tr>
		<td width="18%" class="head_lt">
			<u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
		<td >
			<u:input id="subj" value="${ctsVo.subj}" titleId="cols.subj" style="width: 99%;"  mandatory="Y" maxByte="240"/></td>
	</tr>

	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.itnt" alt="취지" /></td>
	<td id="editor1Area" style="padding:2px;height:202px;vertical-align:middle;">
		<u:input id="edit1Check" name="edit1Check" type="hidden" mandatory="Y" />
		<u:editor id="editor1" width="100%" height="200" module="ct" areaId="editor1Area"  value="${ctsVo.survItnt}" />
	</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="wv.cols.greeting" alt="하단인사말" /></td>
	<td id="editor2Area" style="padding:2px;height:202px;vertical-align:middle;">
		<u:editor id="editor2" width="100%" height="200" module="ct" areaId="editor2Area" value="${ctsVo.survFtr}"/>
	</td>
	</tr>

	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.tgt" alt="대상" /></td>
	<u:set test="${ctsVo.survTgtM =='M' && !empty ctsVo.survTgtM}" var="survTgtM" value="true" elseValue="false"/>
	<u:set test="${ctsVo.survTgtS =='S' && !empty ctsVo.survTgtS}" var="survTgtS" value="true" elseValue="false"/>
	<u:set test="${ctsVo.survTgtR =='R' && !empty ctsVo.survTgtR}" var="survTgtR" value="true" elseValue="false"/>
	<u:set test="${ctsVo.survTgtA =='A' && !empty ctsVo.survTgtA}" var="survTgtA" value="true" elseValue="false"/>
	<td><u:checkArea>
		<u:checkbox name="tgt" value="M" titleId="ct.option.mbshLev0" alt="마스터" checked="${survTgtM}" inputClass="bodybg_lt" />
		<u:checkbox name="tgt" value="S" titleId="ct.option.mbshLev1" alt="스텝"  checked="${survTgtS}" inputClass="bodybg_lt" />
		<u:checkbox name="tgt" value="R" titleId="ct.option.mbshLev2" alt="정회원" checked="${survTgtR}" inputClass="bodybg_lt" />
		<u:checkbox name="tgt" value="A" titleId="ct.option.mbshLev3" alt="준회원" checked="${survTgtA}" inputClass="bodybg_lt" />
		</u:checkArea></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.prd" alt="기간" /></td>
	<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="body_lt"><u:msg titleId="cols.strtDt" alt="시작일시" /></td>
		<td class="width5"></td>
		<td ><u:calendar id="strtDt" option="{end:'finDt'}"  handler="function(date,option){return compareDate(date);}" titleId="cols.choiDt" alt="선택일시" mandatory="Y" value="${ctsVo.survStartDt}" /></td>
		<td>&nbsp;</td>
		</tr>

		<tr>
		<td class="body_lt"><u:msg titleId="cols.finDt" alt="마감일시" /></td>
		<td class="width5"></td>
		<td ><u:calendar id="finDt" option="{start:'strtDt'}"  handler="function(date,option){return compareDate(date);}"  value="${ctsVo.survEndDt}"/></td>
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
		<td class="head_lt"><u:msg titleId="wv.cols.resurv" alt="재설문" /></td>
		<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
			
			<tr>
			<!-- 
			<td><u:radio name="resurvPosbYn" value="posb" titleId="cm.option.posb" checked="${fnc == 'mod'}" /></td>
			<td><u:radio name="resurvPosbYn" value="imps" titleId="cm.option.imps" /></td>
			 -->
				<c:if test="${fnc == 'mod'}">
					<c:choose>
						<c:when test="${ctsVo.repetSurvYn == 'Y'}"> 
							<c:set var="posb"	value= "true" />
							<c:set var="imps"	value= "false" />
						</c:when>
						<c:when test="${ctsVo.repetSurvYn == 'N'}"> 
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
			</table>
		</td>
	</tr>
</u:listArea>

<u:blank />

<u:buttonArea>
	<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
	<!-- <u:button titleId="cm.btn.auth" alt="권한" onclick="setSurvAuth();" auth="W" /> -->
	<c:if test="${!empty authChkW && authChkW == 'W' }">
		<u:button titleId="cm.btn.next" alt="다음" onclick="editor('editor1').prepare(); editor('editor2').prepare();" href="javascript:formSubit();" />
	</c:if>
	<u:button titleId="cm.btn.cancel" alt="취소" href="javascript:history.go(-1);" />
</u:buttonArea>

<u:blank />

</form>

