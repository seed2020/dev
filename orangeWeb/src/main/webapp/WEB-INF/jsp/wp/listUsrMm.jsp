<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	request.setAttribute("cWidth", 28);

%><script type="text/javascript">
<!--<%--
[소버튼] 임직원 / 외주직원 클릭 --%>
function setManPower(which){
	if(which=='emp'){
		var data = {};
		searchUserPop({data:data}, function(userVo){
			if(userVo!=null){
				var $form = $("#searchForm1");
				$form.find("input[name=mpId]").val(userVo.userUid);
				$form.find("input[name=mpTypCd]").val('emp');
				$form.submit();
			}
		});
	} else {
		dialog.open("listOutsDialog", '<u:msg titleId="wp.outsourcing" alt="외주 직원" />', "./listOutsPop.do?menuId=${menuId}&cat=${param.cat}&mode=single");
	}
}<%--
[기능] 외주직원 팝업 콜백 --%>
function setOutsourcingData(arr){
	if(arr!=null && arr.length>0){
		var $form = $("#searchForm1");
		$form.find("input[name=mpId]").val(arr[0].id);
		$form.find("input[name=mpTypCd]").val('out');
		$form.submit();
	}
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title alt="개인별 수행 내역" menuNameFirst="true" />

<%-- // 검색영역 --%>
<u:searchArea style="position:relative; z-index:2;">
	<form id="searchForm1" name="searchForm1" action="${_uri}" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="cat" value="${param.cat}" />
	<u:input type="hidden" id="mpId" value="${param.mpId}" />
	<u:input type="hidden" id="mpTypCd" value="${param.mpTypCd}" />
	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td class="search_tit"><u:msg titleId="pt.jsp.setAuthGrp.user" alt="사용자" /></td>
		<td><table border="0" cellpadding="0" cellspacing="0"><tr>
		<td><u:input id="mpNm" titleId="pt.jsp.setAuthGrp.user"
			style="width:80px" readonly="Y" value="${not empty orUserBVo ? orUserBVo.rescNm : wpMpBVo.mpNm}" /></td>
		<td><u:buttonS titleId="cm.option.empl" alt="임직원" onclick="setManPower('emp');"/></td>
		<td><u:buttonS titleId="wp.outsourcing" alt="외주 직원" onclick="setManPower('out');"/></td>
		</tr></table></td>
		<td style="width:25px;"></td>
		
		<td class="search_tit"><u:msg titleId="wp.pred" alt="기간" /></td>
		<td>
		<u:calendar
				titleId="cm.cal.startDd" alt="시작일자"
				id="strtYmd" value="${strtYmd}" option="{end:'endYmd'}"
				readonly="${not empty noChange ? 'Y' : ''}" /></td>
		<td style="padding-left:8px; padding-right:5px;">~</td>
		<td><u:calendar
				titleId="cm.cal.endDd" alt="종료일자"
				id="endYmd" value="${param.endYmd}" option="{start:'strtYmd'}"
				readonly="${not empty noChange ? 'Y' : ''}" /></td>
		<td style="width:25px;"></td>
		
		</tr>
		</table>
	</td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:document.searchForm1.submit();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<c:if test="${not empty orUserBVo or not empty wpMpBVo}">
<u:listArea colgroup="13%,37%,13%,37%">
<tr>
	<td class="head_ct"><u:msg titleId="pt.jsp.setAuthGrp.user" alt="사용자" /></td>
	<td class="body_lt"><c:if
			test="${not empty orUserBVo}"><u:out value="${orUserBVo.rescNm}" /></c:if><c:if
			test="${not empty wpMpBVo}"><u:out value="${wpMpBVo.mpNm}" /></c:if></td>
	<td class="head_ct"><u:msg titleId="cm.gubun" alt="구분" /></td>
	<td class="body_lt"><c:if test="${not empty orUserBVo or not empty wpMpBVo}"><u:msg titleId="wp.mpTypCd.${param.mpTypCd}" /></c:if></td>
</tr><c:if
	test="${not empty orUserBVo}">
<tr>
	<td class="head_ct"><u:term termId="or.term.grade" alt="직급"/></td>
	<td class="body_lt"><u:out value="${orUserBVo.gradeNm}" /></td>
	<td class="head_ct"><u:msg titleId="cols.dept" alt="부서"/></td>
	<td class="body_lt"><u:out value="${orUserBVo.deptRescNm}" /></td>
</tr>
</c:if><c:if
	test="${not empty wpMpBVo}">
<tr>
	<td class="head_ct"><u:term termId="or.term.grade" alt="직급"/></td>
	<td class="body_lt"><u:out value="${wpMpBVo.mpGrade}" /></td>
	<td class="head_ct"><u:msg titleId="cols.phon" alt="전화번호" /></td>
	<td class="body_lt"><u:out value="${wpMpBVo.mpPhone}" /></td>
</tr>
</c:if>
</u:listArea></c:if>

<c:if test="${not empty orUserBVo or not empty wpMpBVo}">
<u:listArea colgroup="15%,20%,20%,15%,15%,15%">
<tr>
	<th class="head_ct"><u:msg titleId="wp.prjCd" alt="프로잭트 코드" /></th>
	<th class="head_ct"><u:msg titleId="wp.prjNm" alt="프로잭트 명" /></th>
	<th class="head_ct"><u:msg titleId="wp.prjPred" alt="프로잭트 기간" /></th>
	<td class="head_ct"><u:msg titleId="wp.prjRole1Cd" alt="역할분류" /></td>
	<th class="head_ct"><u:msg titleId="wp.mdPlan" alt="투입계획" /></th>
	<th class="head_ct"><u:msg titleId="wp.mdRslt" alt="투입결과" /></th>
</tr>
<c:if test="${fn:length(wpPrjBVoList)==0}" >
	<tr>
	<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:forEach items="${wpPrjBVoList}" var="wpPrjBVo" varStatus="status">
<tr>
	<td class="body_ct"><u:out value="${wpPrjBVo.prjCd}" /></td>
	<td class="body_ct"><u:out value="${wpPrjBVo.prjNm}" /></td>
	<td class="body_ct"><c:if
		test="${not empty wpPrjBVo.strtYmd or not empty wpPrjBVo.endYmd}"><u:out
			value="${wpPrjBVo.strtYmd}" type="shortdate" /> ~ <u:out
			value="${wpPrjBVo.endYmd}" type="shortdate" /></c:if></td><u:convertMap
				srcId="planDMap" attId="${wpPrjBVo.prjNo}" var="wpPrjMpPlanDVo" intKey="${true}" />
	<td class="body_ct"><c:if
		test="${not empty wpPrjMpPlanDVo.prjRole2Cd}"><u:msg titleId="wp.prjRole2Cd.${wpPrjMpPlanDVo.prjRole2Cd}" /></c:if></td>
	<td class="body_ct">${wpPrjMpPlanDVo.mpMmSum}</td><u:convertMap
				srcId="rsltDMap" attId="${wpPrjBVo.prjNo}" var="wpPrjMpRsltDVo" intKey="${true}" />
	<td class="body_ct">${wpPrjMpRsltDVo.rsltMm}</td>
</tr>
</c:forEach>
</u:listArea>
</c:if>


<c:if test="${not empty yearMonthList}">
<u:title titleId="cm.btn.detl" alt="상세정보" menuNameFirst="false" type="small" />

<table class="listtable" border="0" cellspacing="1" cellpadding="0" style="width:${(31*cWidth)+70}px; margin-top:-1px; table-layout: fixed;">
<tr style="height:27px;">
	<td class="head_ct" style="width:70px; text-align:center;">&nbsp;</td>
	<c:forEach begin="1" end="31" step="1" var="day">
	<td class="head_ct" style="width:${cWidth+2}px; border-right:${ day % 5 eq 0 ? '1px solid lightgrey' : ''}">${day}</td>
	</c:forEach>
</tr>
</table>
<c:forEach items="${yearMonthList}" var="yearMonth" varStatus="status"><c:if
	test="${yearMonth.endsWith('12') and not status.first}">
<div style="height:5px;"></div></c:if>
<table class="listtable" border="0" cellspacing="1" cellpadding="0" style="width:${(lastDayOfMonth[month]*cWidth)+70}px;; margin-top:-1px; table-layout: fixed;">
<tr style="height:27px;" onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="head_ct" style="width:70px;">${yearMonth}</td><u:set
		test="true" var="week" value="${firstDayOfWeekMap.get(yearMonth)}" />
	<c:forEach begin="1" end="${lastDayOfMonthMap.get(yearMonth)}" step="1" var="day"><u:set test="true"
		var="rsltYmd" value="${yearMonth}-${day<10 ? '0'.concat(day) : day}" />
	<td class="body_ct" style="width:${cWidth}px; text-align:center; background-color:${
		not empty rsltLMap.get(rsltYmd).color ? rsltLMap.get(rsltYmd).color :
		week eq 1 or holidayList.contains(rsltYmd) ? '#FFF2F2' :
		week eq 7 ? '#EEF1FF' : ''
		}; font-weight:bold; color:#525252; border-right:${ day % 5 eq 0 ? '1px solid lightgrey' : ''};" data-color="${
		week eq 1 or holidayList.contains(rsltYmd) ? '#FFF2F2' : week eq 7 ? '#EEF1FF' : ''
		}" title="<u:out value='${rsltLMap.get(rsltYmd).prjNm}' type='value' />">${rsltLMap.get(rsltYmd).rsltMd}</td><u:set
		test="${week eq 7}" var="week" value="1" elseValue="${week + 1}" />
	</c:forEach>
</tr>
</table>
</c:forEach>
</c:if>

<div class="blank"></div>
