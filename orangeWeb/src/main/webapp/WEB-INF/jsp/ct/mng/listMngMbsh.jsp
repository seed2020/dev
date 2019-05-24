<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

<% // 여러명의 사용자 선택 %>
function openMuiltiUser(mode, fnc){
	var $view = $("#mbshList"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	var param={data:data, multi:true, mode:mode=="" ?'search':'view'};
	<c:if test="${!empty globalOrgChartEnable && globalOrgChartEnable==true}">
		param.global='Y';
	</c:if>
	
	<%// option : data, multi, titleId %>
	searchUserPop(param, function(arr){
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
			buffer.push("<input id='ctId' name='ctId' type = 'hidden' value = '${ctId}' />");
			if(fnc == "join"){
				buffer.push("<input id='fnc' name='fnc' type = 'hidden' value = 'join' />");
				
				$view.html(buffer.join(''));
				$view.attr('method', 'post');
				$view.attr('action','./listMngMbsh.do?menuId=${menuId}&ctId=${ctId}');
				$view.submit();
				
			}else{
				$view.html(buffer.join(''));
				sendEmail();
			}
			
		}
	});
}

//이메일 발송
function sendEmail() {
	var $mbshCtId = $("#ctId").val();
	var $selectCtMbshList = $("#mbshList");
	var selectCtMbshIds = [];
	var $ctMbshListTbl = $("#mbshList");

	$ctMbshListTbl.find("input[name='userUid']").each(function(){
		var $selectedCtMbshId = $(this).val();
			selectCtMbshIds.push($selectedCtMbshId);
	});
	
	if(selectCtMbshIds == '' || selectCtMbshIds == null){
		//ct.msg.inviteFail = (영) 사용자를 선택해주시기 바랍니다.
		alertMsg("ct.msg.inviteFail");
	}else{
		emailSendPop({mbshCtId:$mbshCtId,selectCtMbshIds:selectCtMbshIds}, '${menuId }');
	}
	/*
	callAjax('./sendMailCtMbsh.do?menuId=${menuId}', {mbshCtId:$mbshCtId, selectCtMbshIds:selectCtMbshIds}, function(data){
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			//dialog.open('sendEmailPop','메일보내기','/cm/sendEmailPop.do?menuId=${menuId}&emailId='+data.emailId);
			dialog.open('zmailPop','메일보내기','/cm/zmailPop.do?menuId=${menuId}&emailId='+data.emailId);
		}
	});*/
}

//선택된 회원 기본권한 수정
function chkMbshAuthMod(){
	var $mbshCtId = $("#ctId").val();
	var $mbshDefAuth = $("#mbshDefAuth").val();
	
	var $selectCtMbshList = $("#selectCtMbshList");
	var selectCtMbshIds = [];
	var $checkedLength = $('input:checkbox:checked').length;
	var $ctMbshListTbl = $("#listArea");
	
	if($checkedLength == '0'){
		alertMsg("ct.msg.selectMem"); <%//회원을 선택해주시기 바랍니다.%>
	}else{
		$ctMbshListTbl.find("tr[name='cmntMbshVo']").each(function(){
			var $selectedCtMbshId = $(this).find("#mbshId").val();
			$(this).find("#checkFlag").each(function(){
				if($(this).is(":checked")||$(this).attr("checked") == "checked"){
					if($(this).attr("tr")!='headerTr'){
						selectCtMbshIds.push($selectedCtMbshId);
					}
				}
			});
		});
		
		if (confirmMsg("ct.cfrm.mbshAuthMod")) {
			callAjax('./transMbshListAuthMod.do?menuId=${menuId}', {mbshCtId:$mbshCtId, selectCtMbshIds:selectCtMbshIds, mbshDefAuth:$mbshDefAuth}, function(data){
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.href = './listMngMbsh.do?menuId=${menuId}&ctId='+$mbshCtId;
				}
			});
		}else{
			return;
		}
	}
}

//선택된 회원 임의 탈퇴
function chkMbshWidr(){
	var $mbshCtId = $("#ctId").val();
	var $selectCtMbshList = $("#selectCtMbshList");
	var selectCtMbshIds = [];
	var $checkedLength = $('input:checkbox:checked').length;
	var $ctMbshListTbl = $("#listArea");
	
	if($checkedLength == '0'){
		alertMsg("ct.msg.selectMem"); <%//회원을 선택해주시기 바랍니다.%>
	}else{
		$ctMbshListTbl.find("tr[name='cmntMbshVo']").each(function(){
			var $selectedCtMbshId = $(this).find("#mbshId").val();
			$(this).find("#checkFlag").each(function(){
				if($(this).is(":checked")||$(this).attr("checked") == "checked"){
					if($(this).attr("tr")!='headerTr'){
						selectCtMbshIds.push($selectedCtMbshId);
					}
				}
			});
		});
		
		if (confirmMsg("ct.cfrm.mbshWidr")) {
			callAjax('./transMbshListDel.do?menuId=${menuId}', {mbshCtId:$mbshCtId, selectCtMbshIds:selectCtMbshIds}, function(data){
				if (data.message != null) {
					alert(data.message);
				}
				if (data.result == 'ok') {
					location.href = './listMngMbsh.do?menuId=${menuId}&ctId='+$mbshCtId;
				}
			});
		}else{
			return;
		}
	}
}

//체크박스 전체 선택
function checkAll(){
	if($("#checkFlagAll").is(":checked")){
		$("input[name='checkFlag']:checkbox").each(function(){
			if($(this).attr("disabled") != 'Y'){
				$(this).parent().attr("class", "checked");
				$(this).attr("checked","checked");
			}
		});
		
	}else{
		$("input[name='checkFlag']:checkbox").each(function(){
			if(!$(this).attr("disabled") != 'Y'){
				$(this).parent().attr("class", "");
				$(this).removeAttr("checked");
			}
		});
	}
}

function setMngMbshPop(mbshId) {
	var $ctId = $("#ctId").val();
	dialog.open('setMngMbshPop','<u:msg titleId="ct.jsp.listMngMbsh.title" alt="커뮤니티 회원정보변경"/>','./setMngMbshPop.do?menuId=${menuId}&mbshId='+mbshId+'&ctId='+$ctId);
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title title="${menuTitle }"  alt="커뮤니티 회원정보변경" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listMngMbsh.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="ctId" name="ctId" value="${ctId}"/>

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
				<u:option value="mbshNmOpt" titleId="ct.cols.userNm" alt="이름" checkValue="${param.schCat}" />
				<u:option value="mbshDeptNmOpt" titleId="ct.cols.deptNm" alt="부서" checkValue="${param.schCat}" />
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<form id = "selectCtMbshList">
</form>

<form id = "mbshList">
</form>

<% // 목록 %>
<u:listArea id="listArea">
	<tr id="headerTr">
	<td width="3%" class="head_bg"><u:checkbox id="checkFlagAll" name="checkFlagAll" value="all" checked="false" onclick="javascript:checkAll();"/></td>
	<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.joinDt" alt="가입일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.dftAuth" alt="기본권한" /></td>
	</tr>
	
	<c:forEach var="ctMbshVo" items="${ctMbshList}" varStatus="status">
		<tr id="cmntMbshVo" name="cmntMbshVo" onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="bodybg_ct">
				<c:choose>
					<c:when test="${ctMbshVo.userSeculCd == 'M'}">
						<u:checkbox id="mastCheckFlag" name="mastCheckFlag" value="${status.count}" checked="false" disabled="Y"/>
					</c:when>
					<c:otherwise>
						<u:checkbox id="checkFlag" name="checkFlag" value="${status.count}" checked="false" />
					</c:otherwise>
				</c:choose>
			</td>
			<td class="body_ct">${recodeCount - ctMbshVo.rnum + 1 }</td>
			<td class="body_lt"><a href="javascript:viewUserPop('${ctMbshVo.userUid}');">${ctMbshVo.userNm}</a>
				<input id="mbshId" name="mbshId" type="hidden" value="${ctMbshVo.userUid}"/>
			</td>
			<td class="body_ct">${ctMbshVo.deptNm}</td>
			<td class="body_ct">
				<fmt:parseDate var="dateTempParse" value="${ctMbshVo.joinDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			</td>
			<td class="body_ct">
				<c:choose>
					<c:when test="${ctMbshVo.userSeculCd == 'M'}">
						<u:msg titleId="ct.cols.mbshLev0" alt="마스터" />
					</c:when>
					<c:when test="${ctMbshVo.userSeculCd == 'S'}">
						<u:msg titleId="ct.cols.mbshLev1" alt="스텝" />
					</c:when>
					<c:when test="${ctMbshVo.userSeculCd == 'A'}">
						<u:msg titleId="ct.cols.mbshLev3" alt="준회원" />
					</c:when>
					<c:when test="${ctMbshVo.userSeculCd == 'R'}">
						<u:msg titleId="ct.cols.mbshLev2" alt="정회원" />
					</c:when>
				</c:choose>
			</td>
		</tr>
	</c:forEach>
	<c:if test="${fn:length(ctMbshList) == 0 }">
		<tr>
			<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	
	</c:if>
	

</u:listArea>

<u:pagination />


<% // 작은 버튼 영역 %>
<div class="front">
<div class="front_left">
	<table border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td class="color_txt">※ <u:msg titleId="ct.jsp.listMngMbsh.tx01" alt="목록에서 채크한 모든 회원정보를 일괄변경합니다." /></td>
	</tr>
	</tbody></table>
</div>
</div>
<u:listArea>
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.dftAuth" alt="기본권한" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td><select id="mbshDefAuth" name="mbshDefAuth">
			<option value="S"><u:msg titleId="ct.cols.mbshLev1" alt="스텝" /></option>
			<option value="R"><u:msg titleId="ct.cols.mbshLev2" alt="정회원" /></option>
			<option value="A"><u:msg titleId="ct.cols.mbshLev3" alt="준회원" /></option>
			</select></td>
		<td><u:buttonS href="" titleId="cm.btn.mod" alt="수정" onclick="javascript:chkMbshAuthMod()"/></td>
		</tr>
		</tbody></table></td>
	</tr>
	
	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.forcedWidr" alt="임의탈퇴" /></td>
	<td><u:buttonS href="" titleId="ct.btn.widr" alt="탈퇴" onclick="javascript:chkMbshWidr()"/></td>
	</tr>
</u:listArea>


<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="ct.btn.mbshJoin" alt="회원가입" onclick="javascript:openMuiltiUser('','join')" />
	<c:if test="${mailEnable == 'Y' }">
		<u:button titleId="ct.btn.invtEmailSend" alt="초대메일발송" onclick="javascript:openMuiltiUser('','sendEmail');" />
	</c:if>
</u:buttonArea>