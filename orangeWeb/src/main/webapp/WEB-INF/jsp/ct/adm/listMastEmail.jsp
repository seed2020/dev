<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
<% // 이메일 발송 %>
function sendEmail() {
	var $ctMbshListTbl = $("#listArea");
	var selectMbshList = [];
	var $checkedLength = $('input:checkbox:checked').length;
	if($checkedLength == '0'){
		alert('<u:msg titleId="ct.msg.selectMem" alt="회원을 선택해주시기 바랍니다." />');
	}else{
		$ctMbshListTbl.find("tr[name='cmntMbshVo']").each(function(){
			// OK
			var $selectedMbshUid = $(this).find("#ctMastUid").val();
			$(this).find("#checkFlag").each(function(){
				// OK
				if($(this).is(":checked")||$(this).attr("checked") == "checked"){
					if($(this).attr("tr")!='headerTr'){
						selectMbshList.push($selectedMbshUid);
					}
				}
			});
		});
		
		emailSendPop({selectMbshList:selectMbshList}, '${menuId }');
		
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


$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ct.jsp.listMastEmail.title" alt="마스터/스탭 목록" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listMastEmail.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<option value = "mbshNmOpt" checkValue="${param.schCat}"><u:msg titleId="ct.cols.userNm" alt="커뮤니티"/></option>
			<option value = "communityOpt" checkValue="${param.schCat}"><u:msg titleId="ct.cols.cm" alt="커뮤니티"/></option>
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="" titleId="cols.schWord" style="width: 400px;" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<u:listArea id="listArea">
	<tr id="headerTr">
	<td width="3%" class="head_bg"><u:checkbox id="checkFlagAll" name="checkFlagAll" value="all" checked="false" onclick="javascript:checkAll();"/></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.grade" alt="직급" /></td>
	<td class="head_ct"><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.auth" alt="권한" /></td>
	</tr>
	<c:set var="userUid" value="" />
	<c:forEach var="ctMbshVo" items="${ctMbshList}" varStatus="status">
		<c:set var="insertYn" value="N"/>
		<c:if test="${insertYn == 'N' }">
			<c:if test="${ctMbshVo.userUid != userUid}">
				<tr id="cmntMbshVo" name="cmntMbshVo" onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<c:set var="userUid" value="${ctMbshVo.userUid}" />
					<td class="bodybg_ct"><u:checkbox id="checkFlag" name="checkFlag" value="${status.count}" checked="false" /></td>
					<td class="body_ct"><a href="javascript:viewUserPop('${ctMbshVo.userUid}');">${ctMbshVo.userNm}</a>
						<input id="ctMastUid" name="ctMastUid" type="hidden" value="${ctMbshVo.userUid}"/>
					</td>
					<td class="body_ct">${ctMbshVo.deptNm}</td>
					<td class="body_ct">${ctMbshVo.gradeNm}</td>
					<td class="body_ct">${ctMbshVo.ctNm}</td>
					<td class="body_ct">
						<c:choose>
							<c:when test="${ctMbshVo.userSeculCd == 'M'}">
								<u:msg titleId="ct.option.mbshLev0" alt="마스터"/>
							</c:when>
							<c:when test="${ctMbshVo.userSeculCd == 'S'}">
								<u:msg titleId="ct.option.mbshLev1" alt="스텝"/>
							</c:when>
						</c:choose>
					</td>
				</tr>
				<c:set var="insertYn" value="Y"/>
			</c:if>
			
		</c:if>
		<c:if test="${insertYn == 'N' }">
			<c:if test="${ctMbshVo.userUid == userUid}">
				<tr id="cmntMbshVo" name="cmntMbshVo" onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="bodybg_ct"></td>
					<td class="body_ct"></td>
					<td class="body_ct"></td>
					<td class="body_ct"></td>
					<td class="body_ct">${ctMbshVo.ctNm}</td>
					<td class="body_ct">
						<c:choose>
							<c:when test="${ctMbshVo.userSeculCd == 'M'}">
								<u:msg titleId="ct.option.mbshLev0" alt="마스터"/>
							</c:when>
							<c:when test="${ctMbshVo.userSeculCd == 'S'}">
								<u:msg titleId="ct.option.mbshLev1" alt="스텝"/>
							</c:when>
						</c:choose>
					</td>
				</tr>
			</c:if>
		</c:if>
	
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
	<c:if test="${mailEnable == 'Y' }">
		<u:button titleId="cm.btn.emailSend" alt="이메일발송" auth="W" onclick="javascript:sendEmail();" />
	</c:if>
</u:buttonArea>

