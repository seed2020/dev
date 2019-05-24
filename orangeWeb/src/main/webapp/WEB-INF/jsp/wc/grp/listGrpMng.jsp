<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // 그룹 수정 %>
//function modGrp(){
//	var $grpInfo = $("#grpInfo");
//	$grpInfo.attr('method', 'POST');
//	$grpInfo.attr('action', './setGrpPop.do?menuId=${menuId}');
//	$grpInfo.submit();
//}

function schGrpInfo(){
	var $schGrpNm = $("#schGrpNm").val();
	var $schdlGrpMbshListTbl=$("#listArea");
	var $grpMbshSchdlGrpId = $("#grpMbshSchdlGrpId").val();
	var schGrpMbshUid= [];
	var grpMbshNm = [];
	var grpMbshUid= [];
		$schdlGrpMbshListTbl.find("#schldGrpMbsh").each(function (){
			var $grpMbshUserUid = $(this).find("#grpMbshUid").val();
			var $grpMbshUserNm = $(this).find("#grpMbshUserNm").val();
			var $grpMbshUserDeptNm = $(this).find("#grpMbshDeptRescNm").val();
			var $grpMbshUserGroupNm = $(this).find("#grpMbshGradeNm").val();
			schGrpMbshUid.push(""+$grpMbshUserNm+","+$grpMbshUserDeptNm+","+$grpMbshUserGroupNm+"");
			grpMbshUid.push(""+$grpMbshUserUid+"");
			grpMbshNm.push(""+$grpMbshUserNm+"");
		});
	return {schGrpNm:$schGrpNm, 
			schGrpMbshUid:schGrpMbshUid, 
			grpMbshUid:grpMbshUid, 
			grpMbshSchdlGrpId:$grpMbshSchdlGrpId, 
			grpMbshNm:grpMbshNm };
	
}

<% // 그룹 멤버 권한 설정 팝업 %>
function authSetPop(){
	var $checkedLength = $('input:checkbox:checked').length;
	if($checkedLength == '0'){
		alert("<u:msg titleId="wc.msg.sel.member" alt="회원을 선택해주시기 바랍니다." />");
	}else{
		mastExists = false;
		var $schdlGrpMbshListTbl=$("#listArea");	
		$schdlGrpMbshListTbl.find("#schldGrpMbsh").each(function(){
			if($(this).find("#checkFlag").is(":checked")){
				var $selectedUserUid = $(this).find("#grpMbshUid").val();
				if('${mastUid}' == $selectedUserUid)
					mastExists = true;
			}
		});
		
		if(mastExists)
		{
			alert("<u:msg titleId="wc.msg.sel.mastExcept" alt="선택하신 회원 목록에서 마스터 회원은 설정 및 삭제 처리가 되지 않습니다." />");
			return;
		}
		dialog.open('setAuthPop','<u:msg titleId="wc.cols.grp.setRight" alt="권한설정" />','./setAuthPop.do?menuId=${menuId}&schdlGrpId=${schdlGrpId}&mastUid=${mastUid}');
	}
}

function modMastPop(){
	dialog.open('setMastPop','마스터변경','./setMastPop.do?menuId=${menuId}&mastUid=${mastUid}&schdlGrpId=${schdlGrpId}');
}

<% // 그룹 멤버 권한 설정 저장%>
function schdlGrpMbshAuthSet(){
	var authSet = authGradSet();
	var $authSetUserForm = $("#authSetUserInfo");
	var $schdlGrpMbshListTbl=$("#listArea");	
	var authSetUserListInfo = [];
		$schdlGrpMbshListTbl.find("#schldGrpMbsh").each(function(){
			if($(this).find("#checkFlag").is(":checked")){
				var $selectedUserUid = $(this).find("#grpMbshUid").val();
				var $selectedUserSchdlGrpId = $(this).find("#grpMbshSchdlGrpId").val();
				authSetUserListInfo.push("<input id='authSetUserUid' name='authSetUserUid' type='hidden' style='width:80px' value='"+$selectedUserUid+"' />");
				authSetUserListInfo.push("<input id='authSetUserSchdlGrpId' name='authSetUserSchdlGrpId' type='hidden' style='width:80px' value='"+$selectedUserSchdlGrpId+"' />");
				authSetUserListInfo.push("<input id='authGrad' name='authGrad' type='hidden' style='width:80px' value='"+authSet.authGrad+"' />");
			}
		});
		authSetUserListInfo.push("<input id='behave' name = 'behave' type = 'hidden' value='authSet'/>");
		$authSetUserForm.html(authSetUserListInfo.join(''));
		$authSetUserForm.attr('method', 'post');
		$authSetUserForm.attr('action','./listGrpMng.do?menuId=${menuId}&schdlGrpId=${schdlGrpId}&mastUid=${mastUid}');
		$authSetUserForm.submit();
}

<% // 그룹 멤버 삭제 %>
function schdlGrpMbshDel(){
	var $delUserForm = $("#delUserInfo");
	var $schdlGrpMbshListTbl=$("#listArea");
	var delUserListInfo = [];
	var $checkedLength = $('input:checkbox:checked').length;
	if($checkedLength == '0'){
		alert("<u:msg titleId="wc.msg.sel.member" alt="회원을 선택해주시기 바랍니다." />");
	}else{
		mastExists = false;
		$schdlGrpMbshListTbl.find("#schldGrpMbsh").each(function(){
			if($(this).find("#checkFlag").is(":checked")){
				var $selectedUserUid = $(this).find("#grpMbshUid").val();
				
				if('${mastUid}' == $selectedUserUid)
					mastExists = true;
				
				var $selectedUserSchdlGrpId = $(this).find("#grpMbshSchdlGrpId").val();
				delUserListInfo.push("<input id='delUserUid' name='delUserUid' type='hidden' style='width:80px' value='"+$selectedUserUid+"' />");
				delUserListInfo.push("<input id='delUserSchdlGrpId' name='delUserSchdlGrpId' type='hidden' style='width:80px' value='"+$selectedUserSchdlGrpId+"' />");
			}
		});
		
		if(mastExists)
		{
			alert("<u:msg titleId="wc.msg.sel.mastExcept" alt="선택하신 회원 목록에서 마스터 회원은 설정 및 삭제 처리가 되지 않습니다." />");
			return;
		}
		
		delUserListInfo.push("<input id='behave' name = 'behave' type = 'hidden' value='del'/>");
		$delUserForm.html(delUserListInfo.join(''));
		$delUserForm.attr('method', 'post');
		$delUserForm.attr('action','./listGrpMng.do?menuId=${menuId}&schdlGrpId=${schdlGrpId}&mastUid=${mastUid}');
		$delUserForm.submit();
	}
	
	
}

<% // 전체 체크박스 선택 %>
function checkAll(){
	if($("#checkFlagAll").is(":checked")){
		$("input[name='checkFlag']:checkbox").each(function(){
			$(this).parent().attr("class", "checked");
			$(this).trigger('click');  
		});
		
	}else{
		$("input[name='checkFlag']:checkbox").each(function(){
			$(this).parent().attr("class", "");
			$(this).trigger('click');  
		});
	}
}

<% // 여러명의 사용자 선택 %>
function openMuiltiUser(mode){
		var $view = $("#mbshList"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
		$("#listArea").find("#grpMbshUid").each(function(){
			//data.push({userUid:$(this).val()});
		});
		<%// option : data, multi, titleId %>
		searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
			if(arr!=null){
				var buffer = [];
				arr.each(function(index, userVo){
					buffer.push("<input id='deptRescNm' name='deptRescNm' type='hidden' style='width:80px' value='"+userVo.deptRescNm+"' />");
					buffer.push("<input id='rescNm' name='rescNm' type='hidden' style='width:80px' value='"+userVo.rescNm+"' />");
					buffer.push("<input id='userUid' name='userUid' type='hidden' style='width:80px' value='"+userVo.userUid+"' />");
					buffer.push("<input id='titleNm' name='titleNm' type='hidden' style='width:80px' value='"+userVo.titleNm+"' />");
					buffer.push("<input id='mbno' name='mbno' type='hidden' style='width:80px' value='"+userVo.mbno+"' />");
					buffer.push("<input id='gradeNm' name='gradeNm' type='hidden' style='width:80px' value='"+userVo.gradeNm+"' />");
					
				});
				buffer.push("<input id='schdlGrpId' name='schdlGrpId' type = 'hidden' value = '${schdlGrpId}' />");
				buffer.push("<input id='behave' name = 'behave' type = 'hidden' value='add'/>");
				
				$view.html(buffer.join(''));
				$view.attr('method', 'post');
				$view.attr('action','./listGrpMng.do?menuId=${menuId}&schdlGrpId=${schdlGrpId}&mastUid=${mastUid}');
				$view.submit();
			}
		});
	}

//function sendEmail() {
//	dialog.open('sendEmailPop','이메일 발송','/ct/sendEmailPop.do?menuId=${menuId}');
//}

function sendEmail() {
	var $schdlGrpMbshListTbl=$("#listArea");
	var selectUserList = [];
	$schdlGrpMbshListTbl.find("#schldGrpMbsh").each(function(){
		if($(this).find("#checkFlag").is(":checked")){
			var $selectedUserUid = $(this).find("#grpMbshUid").val();
			selectUserList.push($selectedUserUid);
		}
	});
		
	if(validator.validate('grpInfo')){
		// sendEmailDt 는 날짜, sendOpt은 일일, 주간, 월간이냐의 라디오 박스이므로,
		// 나에게는 필요없는 부분이다. 나는 input 타입의 checkFlag라는 값을 가진 리스트 들이 필요하다.
		//var param =  {sendEmailDt:$("#sendEmailDt").val(), sendOpt:$(':radio[name="sendOpt"]:checked').val()};
		if(selectUserList == ''){
			alert('<u:msg titleId="wc.msg.grp.notSel" alt="선택 항목이 없습니다." />');
		}else{
			emailSendPop({selectUserList:selectUserList}, '${menuId }');
		}
		
		
		/*
		callAjax('./setGrpMbshSend.do?menuId=${menuId}', {selectUserList:selectUserList}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				//dialog.open('sendEmailPop','이메일 발송','/cm/zmailPop.do?menuId=${menuId}&emailId='+data.emailId);
				dialog.open('sendEmailPop','메일보내기','/cm/sendEmailPop.do?menuId=${menuId}&emailId='+data.emailId);
			}
		});
		*/
	
	}
	
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:msg titleId="wc.jsp.listGrpMng.title" var="title" alt="그룹관리" />
<c:set var="grpNm" value="${grpNm}" />

<u:msg titleId="wc.cols.grp.Mod" alt="그룹수정" var="grpMod"/>
<u:title title="${title} - ${grpNm}" alt="그룹관리 - 그룹명" menuNameFirst="true">
	<c:if test="${wcSchdlGroupB.auth == 'MNG'}">
		<u:titleButton titleId="wc.btn.grpMod" alt="그룹수정" onclick="dialog.open('setGrpModPop','${grpMod }','./setGrpModPop.do?menuId=${menuId}&schdlGrpId=${schdlGrpId}');" />
	</c:if>
</u:title>

<u:input type="hidden" id="schldGrpMbsh" value="" />
<u:input type="hidden" id="checkFlag" value="" />
<input type = "hidden" id="grpMbshUid" value=""/>
<input type = "hidden" id="schdlGrpId" name = "schdlGrpId" value="${schdlGrpId}"/>
<input type = "hidden" id = "grpNm" name = "grpNm" value="${grpNm}" />
<input id="grpMbshUserNm" name="grpMbshUserNm" type="hidden" />
<input id="grpMbshDeptRescNm" name="grpMbshDeptRescNm" type="hidden"/>
<input id="grpMbshGradeNm" name="grpMbshGradeNm" type="hidden"/>
<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listGrpMng.do" >
		<u:input type="hidden" id="menuId" value="${menuId}" />
		<u:input type="hidden" id="schdlGroupId" value="${schdlGrpId}" />
		<input id="behave" name = "behave" type = "hidden" value="search"/>
	
		<table class="search_table" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td>
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td><select name="schCat">
									<option value = "nameOption" checkValue="${param.schCat}" > <u:msg titleId="cols.nm" alt="이름" /> </option>
									<option value = "deptOption" checkValue="${param.schCat}" > <u:msg titleId="cols.dept" alt="부서" /> </option>
								</select></td>
							<td>
								<u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" onkeydown="if (event.keyCode == 13) searchForm.submit();"/>
							</td>
						</tr>
					</table>
				</td>
				<td>
					<div class="button_search">
						<ul>
							<li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />">
								<a href="javascript:document.searchForm.submit()">
									<span><u:msg titleId="cm.btn.search" alt="검색" /></span>
								</a>
							</li>
						</ul>
					</div>
				</td>
			</tr>	
		</table>
	</form>
</u:searchArea>

<form id ="authSetUserInfo">
</form>

<form id = "mbshList">
</form>

<form id = "delUserInfo">
</form>
<% // 목록 %>
<form id = "grpInfo">
<input type = "hidden" id="schGrpId" name = "schGrpId" value="${schdlGrpId}"/>
<u:listArea id="listArea">
	<tr>
	<!-- 
	<u:checkbox name="chk" value="all" checked="false" />
	 -->
		<td width="3%" class="head_bg"><input type="checkbox" id="checkFlagAll" onclick="javascript:checkAll();"/></td>
		<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
		<td class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.grade" alt="직급" /></td>
		<td width="20%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.phon" alt="전화번호" /></td>
		<td width="10%" class="head_ct"><u:msg titleId="cols.auth" alt="권한" /></td>
	</tr>
		<input id = "mastUid" name = "mastUid" type = "hidden" value = "${mastUid}"/>
	
	
	<c:forEach var="WcSchdlGrpMbshDVo" items="${wcSchdlGroupMbshDMapList}" varStatus="status"> 
	<tr id="schldGrpMbsh" onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
		<!-- <u:checkbox name="chk" value="1" checked="false" /> -->
		<td class="bodybg_ct"><input id="checkFlag" name="checkFlag" type="checkbox" class="checkFlag"/></td>
		<td class="body_ct"><u:out value="${WcSchdlGrpMbshDVo.rnum}" type="number" /></td>
		<td class="body_ct"><a href="javascript:viewUserPop('${WcSchdlGrpMbshDVo.userUid}');">${WcSchdlGrpMbshDVo.userNm}</a>
		
		</td>
		
		<u:input type="hidden" id="grpMbshUid" name="grpMbshUid" value="${WcSchdlGrpMbshDVo.userUid}" />
		<u:input type="hidden" id="grpMbshUserNm" name="grpMbshUserNm" value="${WcSchdlGrpMbshDVo.userNm}" />
		<u:input type="hidden" id="grpMbshDeptRescNm" name="grpMbshDeptRescNm" value="${WcSchdlGrpMbshDVo.deptRescNm}" />
		<u:input type="hidden" id="grpMbshGradeNm" name="grpMbshGradeNm" value="${WcSchdlGrpMbshDVo.gradeNm}" />
		<u:input type="hidden" id="grpMbshSchdlGrpId" name="grpMbshSchdlGrpId" value="${WcSchdlGrpMbshDVo.schdlGrpId}" />
		
<%-- 		<input id="grpMbshUid" name="grpMbshUid" type="hidden" value="${WcSchdlGrpMbshDVo.userUid}"/>
		<input id="grpMbshUserNm" name="grpMbshUserNm" type="hidden" value="${WcSchdlGrpMbshDVo.userNm}"/>
		<input id="grpMbshDeptRescNm" name="grpMbshDeptRescNm" type="hidden" value="${WcSchdlGrpMbshDVo.deptRescNm}"/>
		<input id="grpMbshGradeNm" name="grpMbshGradeNm" type="hidden" value="${WcSchdlGrpMbshDVo.gradeNm}"/>
		<input id="grpMbshSchdlGrpId" name="grpMbshSchdlGrpId" type="hidden" value="${WcSchdlGrpMbshDVo.schdlGrpId}"/> --%>
		
		<!-- 
		<td class="body_ct">${WcSchdlGrpMbshDVo.userUid}</td>
		
		 -->
		<td class="body_ct">${WcSchdlGrpMbshDVo.gradeNm}</td>
		<td class="body_ct">${WcSchdlGrpMbshDVo.deptRescNm}</td>
		<td class="body_ct">${WcSchdlGrpMbshDVo.mbno}</td>
		<c:choose>
			<c:when test="${WcSchdlGrpMbshDVo.authGradCd == 'A'}">
				<td class="body_ct"><u:msg titleId="wc.cols.grp.adm" alt="관리" /></td>
			</c:when>
			<c:when test="${WcSchdlGrpMbshDVo.authGradCd == 'R'}">
				<td class="body_ct"><u:msg titleId="wc.cols.grp.red" alt="읽기" /></td>
			</c:when>
			<c:when test="${WcSchdlGrpMbshDVo.authGradCd == 'W'}">
				<td class="body_ct"><u:msg titleId="wc.cols.grp.wrt" alt="쓰기" /></td>
			</c:when>
			<c:otherwise>
				<td class="body_ct"><u:msg titleId="wc.cols.grp.noRight" alt="미권한" /></td>
			</c:otherwise>
		</c:choose>
	</tr>
	</c:forEach>
	
	<c:if test="${fn:length(wcSchdlGroupMbshDMapList) == 0}">
			<tr>
				<td class="nodata" colspan="9"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
	</c:if>
	
</u:listArea>
</form>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<c:if test="${wcSchdlGroupB.auth == 'MNG'}">
		<c:if test="${mailEnable == 'Y' }">
			<u:button titleId="cm.btn.emailSend" alt="이메일발송" auth="W" onclick="javascript:sendEmail();" />
		</c:if>
		
		<!--
		<u:button titleId="cm.btn.auth" alt="권한설정" href="javascript:schdlGrpMbshAuthSet();" />
		 -->
		<u:button titleId="cm.btn.auth" alt="권한설정" href="javascript:authSetPop();" auth="R" />
		 
		<u:button titleId="wc.btn.mastChn" alt="마스터변경" href="javascript:modMastPop();" auth="R" />
		<u:button titleId="wc.btn.mbshReg" alt="회원등록" auth="W" onclick="openMuiltiUser();" />
		<!-- 
		<u:button titleId="wc.btn.mbshReg" alt="회원등록" auth="W" href="javascript:dialog.open('findOrgcPop','조직도','/bb/findOrgcPop.do?menuId=${menuId}');" />
		 -->
		<u:msg titleId="cm.msg.noSelect" alt="선택한 항목이 없습니다." var="msg" />
		<u:button titleId="wc.btn.mbshDel" alt="회원삭제" auth="W" href="javascript:schdlGrpMbshDel();" />
	</c:if>	
	
	<u:button titleId="cm.btn.list" alt="목록" href="./listGrp.do?menuId=${menuId}&fncMy=Y" auth="R" />
</u:buttonArea>

