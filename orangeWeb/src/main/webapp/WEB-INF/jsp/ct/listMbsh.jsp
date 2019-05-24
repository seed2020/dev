<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
<script type="text/javascript">
<!--
<% // 이메일 발송 %>
function sendEmail() {
	var $ctMbshListTbl = $("#listArea");
	var selectMbshList = [];
	var $checkedLength = $('input:checkbox:checked').length;
	if($checkedLength == '0'){
		alert("<u:msg titleId="ct.msg.selectMem" alt="회원을 선택해주시기 바랍니다." />");
	}else{
		$ctMbshListTbl.find("tr[name='cmntMbshVo']").each(function(){
			var $selectedMbshUid = $(this).find("#mbshUid").val();
			$(this).find("#checkFlag").each(function(){
				if($(this).is(":checked")||$(this).attr("checked") == "checked"){
					if($(this).attr("tr")!='headerTr'){
						selectMbshList.push($selectedMbshUid);
					}
				}
			});
		});
		emailSendPop({selectMbshList:selectMbshList}, '${menuId }');
		/*
		callAjax('./sendEmailToCtMbsh.do?menuId=${menuId}', {selectMbshList:selectMbshList}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				dialog.open('sendEmailPop','이메일 발송','/cm/zmailPop.do?menuId=${menuId}&emailId='+data.emailId);
				//dialog.open('sendEmailPop','메일보내기','/cm/sendEmailPop.do?menuId=${menuId}&emailId='+data.emailId);
			}
		});*/
	}
	
}


<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	var $form = $('#excelForm');
	$form.attr('method','post');
	$form.attr('action','./mbshListExcelDownLoad.do?menuId=${menuId}');
	$form.attr('target','dataframe');
	$form[0].submit();
};

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

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<% //  ct.jsp.listMbsh.title %>
<u:title title="${menuTitle }" alt="커뮤니티 회원목록" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listMbsh.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="ctId" name="ctId" value="${ctId}"/>

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<option value = "mbshNmOpt" checkValue="${param.schCat}"><u:title titleId="ct.cols.userNm" alt="이름" /></option>
			<option value = "mbshDeptNmOpt" checkValue="${param.schCat}"><u:title titleId="ct.cols.deptNm" alt="부서" /></option>
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="" titleId="cols.schWord" style="width: 400px;" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<div class="front">
<div class="front_left">
	<table border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td class="color_txt">※ <u:msg titleId="ct.cols.today" alt="오늘" /> ${todayPeople} <u:msg titleId="ct.cols.noms" alt="명" />, 
		<u:msg titleId="ct.cols.total" alt="전체" /> ${allPeople} <u:msg titleId="ct.cols.noms" alt="명" /></td>
	</tr>
	</tbody></table>
</div>
</div>

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
		<td class="body_ct">${recodeCount - ctMbshVo.rnum + 1}</td>
		<td class="body_lt"><a href="javascript:viewUserPop('${ctMbshVo.userUid}');">${ctMbshVo.userNm}</a>
			<input id="mbshUid" name="mbshUid" type="hidden" value="${ctMbshVo.userUid}"/>
		</td>
		<td class="body_ct">${ctMbshVo.deptNm}</td>
		<td class="body_ct">
			<fmt:parseDate var="dateTempParse" value="${ctMbshVo.joinDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
		</td>
		<td class="body_ct">
			<c:choose>
				<c:when test="${ctMbshVo.userSeculCd == 'M'}">
					<u:msg titleId="ct.option.mbshLev0" alt="마스터"/>
				</c:when>
				<c:when test="${ctMbshVo.userSeculCd == 'S'}">
					<u:msg titleId="ct.option.mbshLev1" alt="스텝"/>
				</c:when>
				<c:when test="${ctMbshVo.userSeculCd == 'A'}">
					<u:msg titleId="ct.option.mbshLev3" alt="준회원"/>
				</c:when>
				<c:when test="${ctMbshVo.userSeculCd == 'R'}">
					<u:msg titleId="ct.option.mbshLev2" alt="정회원"/>
				</c:when>
			
			</c:choose>
		</td>
		</tr>
	</c:forEach>
	<c:if test="${fn:length(ctMbshList) == 0}">
			<tr>
				<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
	</c:if>
	
</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<c:choose>
		<c:when test="${!empty myAuth && myAuth == 'M' }">
			<u:button titleId="cm.btn.excelDown" alt="엑셀다운" onclick="javascript:excelDownFile();" />
			<c:if test="${mailEnable == 'Y' }">
				<u:button titleId="cm.btn.emailSend" alt="메일발송" onclick="javascript:sendEmail();" />
			</c:if>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${!empty authChkD && authChkD == 'D' }">
					<u:button titleId="cm.btn.excelDown" alt="엑셀다운" onclick="javascript:excelDownFile();" />
					<c:if test="${mailEnable == 'Y' }">
						<u:button titleId="cm.btn.emailSend" alt="메일발송" onclick="javascript:sendEmail();" />
					</c:if>
				</c:when>
				<c:otherwise>
					<c:if test="${!empty authChkW && authChkW == 'W' }">
						<u:button titleId="cm.btn.excelDown" alt="엑셀다운" onclick="javascript:excelDownFile();" />
						<c:if test="${mailEnable == 'Y' }">
							<u:button titleId="cm.btn.emailSend" alt="메일발송" onclick="javascript:sendEmail();" />
						</c:if>
					</c:if>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
</u:buttonArea>

<form id="excelForm">
	<c:forEach items="${paramEntryList}" var="entry" varStatus="status">
		<u:input type="hidden" id="${entry.key}" value="${entry.value}" />
	</c:forEach>
	<u:input type="hidden" id="listPage" value="${listPage}" />
	<u:input type="hidden" id="ctId" value="${ctId}" />
</form>