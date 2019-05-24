<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
//커뮤니티 자료관리 선택삭제
function selectDataDel(){
	var selectFncIds = [];
	var arr=[], id, obj;
	$("#listArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
		return null;
	}
	for(var i=0;i<arr.length;i++){
		$bcId = $(arr[i]).find("input[name='ctComFncUid']");
		if($bcId.val()!=''){
			selectFncIds.push($bcId.val());
		}
	}
	//<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?" var="msg" />
	//cm.cfrm.del = 삭제하시겠습니까?
	if (confirmMsg("cm.cfrm.del")) {
		callAjax('./ajaxSelectDataDel.do?menuId=${menuId}', {ctId:"${ctId}", selectFncIds:selectFncIds, fncMng:$("#fncMng").val()}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				searchForm.submit();
				//strtDt:strtDt, endDt:endDt, dateSelect:dateSelect  fncMng
				//location.href = './listMngData.do?menuId=${menuId}&ctId=${ctId}&fncMng=${fncMng}&strtDt='+strtDt +'&endDt=' + endDt + '&dateSelect=' + dateSelect;
				
			}
		});
	}else{
		return;
	}
}

//커뮤니티 자료관리 전체삭제
function selectAllDel(){
	
	var fncMng = $("#fncMng").val();
	var strtDt = $("#strtDt").val();
	var endDt = $("#endDt").val();
	var dateSelect = $("#dateSelect").val();
	
	if("${fn:length(ctFncMngFileList)}" == 0){
		alertMsg('cm.msg.noData');<%//해당하는 데이터가 없습니다.%>
		return false;
	}
	
	//cm.cfrm.del = 삭제하시겠습니까?
	if (confirmMsg("cm.cfrm.del")) {
		callAjax('./ajaxSelectAllDel.do?menuId=${menuId}', {ctId:"${ctId}", fncMng:fncMng, strtDt:strtDt, endDt:endDt}, function(data){
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				//strtDt:strtDt, endDt:endDt, dateSelect:dateSelect  fncMng
				location.href = './listMngData.do?menuId=${menuId}&ctId=${ctId}&fncMng=${fncMng}&strtDt='+strtDt +'&endDt=' + endDt + '&dateSelect=' + dateSelect;
				
			}
		});
	}else{
		return;
	}
}

//초기화
function resetDate(){
	this.changeDate("");
	$("#dateSelect").val("");
	$("#dateSelect").trigger('click');
	
}

//기간 변경
function changeDate(date){
	if(date != ""){
		$("#strtDt").val("2000-01-01");	
		$("input[name=strtDt]").attr("readonly",true);
		$("input[name=endDt]").attr("readonly",true);
		$("#strtDtCalBtn").css("display","none");
		$("#endDtCalBtn").css("display","none");
		$("#endDt").val(getAgoDate(0,-Number(date), -1));
		
		
	}else{
		$("input[name=strtDt]").attr("readonly",false);
		$("input[name=endDt]").attr("readonly",false);
		$("#strtDtCalBtn").css("display","block");
		$("#endDtCalBtn").css("display","block");
		$("#strtDt").val("");
		$("#endDt").val("");
		
	}
	
}

//날짜 구하기 ex) (0,0,0) + 또는 - 로 원하는 년,월,일을 구 해 올 수 있습니다.
function getAgoDate(yyyy, mm, dd)
{
	 var today = new Date();
	 var year = today.getFullYear();
	 var month = today.getMonth();
	 var day = today.getDate();
	 var resultDate = new Date(yyyy+year, month+mm, day+dd);
	 
     year = resultDate.getFullYear();
     month = resultDate.getMonth() + 1;
     day = resultDate.getDate();

     if (month < 10)
         month = "0" + month;
     if (day < 10)
         day = "0" + day;

     return year + "-" + month + "-" + day;
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


function viewCm(url, ctId, flag, uid, chkFnc) {
	
	var result;
	var convUrl = url;
	if(flag == 'BULL'){
		convUrl = convUrl.replace(/list/gi,'view');
		result = 'bullId=';
	}else if(flag == 'DEBR'){
		result = 'debrId=';
	}else if(flag == 'SITE'){
		result = 'siteId=';
		convUrl = convUrl.replace(/list/gi,'view');
	}
	
	if(chkFnc == ""){
		alertMsg('ct.msg.fnc.not');<%//해당 기능은 존재 하지 않습니다. %>
	}else{
		location.href = convUrl + "&ctId=" + ctId + "&" + result + uid;	
	}
	
}

$(document).ready(function() {
	setUniformCSS();
	changeDate("${dateSelect}");
	$("#strtDt").val("${strtDt}");
	$("#endDt").val("${endDt}");
	
});
//-->
</script>

<u:title title="${menuTitle }"  alt="커뮤니티 자료관리" menuNameFirst="true" />

<u:searchArea>
	<form name="searchForm" action="./listMngData.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="ctId" value="${ctId}"/>

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="search_tit"><u:msg titleId="cols.fnc" alt="기능" /></td>
			<td>
				<select id="fncMng" name="fncMng">
					<c:forEach items="${ctFncMngList}" var="ctFncMngList" varStatus="status" >
						<option value="${ctFncMngList.relTblSubj}" <c:if test="${ctFncMngList.relTblSubj == fncMng}">selected="selected" </c:if>>${ctFncMngList.ctFncNm}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		
		<tr>
		<td class="search_tit"><u:msg titleId="cols.prd" alt="기간" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td><select id="dateSelect" name="dateSelect" onchange="javascript:changeDate(this.value);">
					<option value=""><u:msg titleId='ct.cols.userDefine' alt='사용자정의' /></option>
					<c:forEach begin="1" varStatus="status" end="6">
						<option value="${status.count}" <c:if test="${dateSelect == status.count}">selected="selected" </c:if>>${status.count} <u:msg titleId='ct.cols.month.prev' alt='개월 전' />  </option>
						<!--  -->
					</c:forEach>
				</select>
			</td>
			<td><u:calendar id="strtDt" name="strtDt" option="{end:'endDt'}" mandatory="Y" />
			</td>
			<td class="body_lt">~</td>
			<td><u:calendar id="endDt" name="endDt" option="{start:'strtDt'}" mandatory="Y"  />
			</td>
			<td><u:buttonS titleId="cm.btn.reset" alt="초기화" onclick="resetDate();" /></td>
			</tr>
			</tbody></table></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<div class="listarea" id="listArea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
		<tr>
			<td width="3%" class="head_bg"><u:checkbox id="checkFlagAll" name="checkFlagAll" value="all" checked="false" onclick="javascript:checkAll();"/></td>
			<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
			<td width="20%" class="head_ct"><u:msg titleId="cols.fncNm" alt="기능명" /></td>
			<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
			<td width="10%" class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
			<td width="10%" class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		</tr>
		<c:forEach items="${ctFncMngFileList}" var="ctFncMngFileVo" varStatus="status" >
			<tr id="ctFncRow" onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<td class="bodybg_ct">
					<u:checkbox id="checkFlag" name="checkFlag" value="${status.count}" checked="false" />
				</td>
				<td class="body_ct">${recodeCount - ctFncMngFileVo.rnum + 1}</td>
				<td class="body_ct">
					<u:out value="${ctFncMngFileVo.fncNm}"/>
					<input type="hidden" id="ctComFncUid" name="ctComFncUid" value="${ctFncMngFileVo.ctComFncUid}"/>
				</td>
				<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
				<td class="body_lt">
					<div class="ellipsis" title="<u:out value="${ctFncMngFileVo.subj}"/>">
						<a href="javascript:viewCm('${ctFncMngFileVo.ctFncUrl}${ctFncMngFileVo.ctFncUid}','${ctFncMngFileVo.ctId}','${ctFncMngFileVo.relTblSubj}','${ctFncMngFileVo.ctComFncUid}','${ctFncMngFileVo.fncNm}')">${ctFncMngFileVo.subj}</a>
					</div>
				</td>
				<td class="body_ct"><a href="javascript:viewUserPop('${ctFncMngFileVo.regrUid}');">${ctFncMngFileVo.regrNm}</a></td>
				<fmt:parseDate var="dateTempParse" value="${ctFncMngFileVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<td class="body_ct"><fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/></td>
			</tr>
		</c:forEach>
		
		<c:if test="${fn:length(ctFncMngFileList) == 0}">
			<tr>
				<td class="nodata" colspan="6"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:if>
	</table>
</div>
<u:blank/>	

<u:pagination />

<u:buttonArea>
	<u:msg titleId="cm.msg.noSelect" alt="선택한 항목이 없습니다." var="msg" />
	<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?" var="msg" />
	<u:button titleId="ct.btn.choiDel" alt="선택자료삭제" onclick="javascript:selectDataDel();" />
	<u:button titleId="ct.btn.allDel" alt="전체자료삭제" onclick="javascript:selectAllDel();" />
</u:buttonArea>
