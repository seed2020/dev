<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	// 공수입력칸 넓이
	request.setAttribute("cWidth", 28);
	request.setAttribute("role1Cds", new String[]{ "con", "dev", "all" });


%><script type="text/javascript">
<!--<%--
--%>

//-->
</script>
<u:title alt="프로잭트별 집계" menuNameFirst="true" />

<u:listArea id="prjArea" colgroup="13%,37%,13%,37%">
<tr>
	<td class="head_ct"><u:msg titleId="wp.prjCd" alt="프로잭트 코드" /></td>
	<td class="body_lt"><u:out value="${wpPrjBVo.prjCd}" /></td>
	<td class="head_ct"><u:msg titleId="wp.prjNm" alt="프로잭트 명" /></td>
	<td class="body_lt"><u:out value="${wpPrjBVo.prjNm}" /></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="wp.pred" alt="기간" /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0"><tr>
		<td><u:out value="${wpPrjBVo.strtYmd}" type="shortdate" /></td>
		<td style="padding-left:8px; padding-right:5px;">~</td>
		<td><u:out value="${wpPrjBVo.endYmd}" type="shortdate" /></td>
		</tr></table></td>
	<td class="head_ct"><u:msg titleId="wp.endDt" alt="종료일" /></td>
	<td class="body_lt"><u:out value="${wpPrjBVo.cmplYmd}" type="shortdate" /></td>
</tr>
</u:listArea>

<c:forEach items="${wpPrjMpPlanDVoList}" var="wpPrjMpPlanDVo"><u:convert
		srcId="yearMonthList${wpPrjMpPlanDVo.mpId}" var="yearMonthList" /><u:convert
		srcId="monthlyMap${wpPrjMpPlanDVo.mpId}" var="monthlyMap" /><c:if
			test="${not empty yearMonthList}">

<u:title title="${wpPrjMpPlanDVo.mpNm}" type="small" />
<table class="listtable" border="0" cellspacing="1" cellpadding="0" style="width:${(31*cWidth)+70}px; margin-top:-1px; table-layout: fixed;">
<tr style="height:27px;">
	<td class="head_ct" style="width:70px; text-align:center;">&nbsp;</td>
	<c:forEach begin="1" end="31" step="1" var="day">
	<td class="head_ct" style="width:${cWidth+2}px; border-right:${ day % 5 eq 0 ? '1px solid lightgrey' : ''}">${day}</td>
	</c:forEach>
</tr>
</table>
<c:forEach items="${yearMonthList}" var="yearMonth">
<table class="listtable" border="0" cellspacing="1" cellpadding="0" style="width:${(lastDayOfMonth[month]*cWidth)+70}px;; margin-top:-1px; table-layout: fixed;">
<tr style="height:27px;" onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="head_ct" style="width:70px;">${yearMonth}</td><u:set
		test="true" var="week" value="${firstDayOfWeekMap.get(yearMonth)}" />
	<c:forEach begin="1" end="${lastDayOfMonthMap.get(yearMonth)}" step="1" var="day"><u:set test="true"
		var="joinYmd" value="${yearMonth}-${day<10 ? '0'.concat(day) : day}" />
	<td class="body_ct" data-holiday="${
		week eq 7 or week eq 1 or holidayList.contains(joinYmd) ? 'Y' : ''}" data-joinYmd="${
		joinYmd}" style="width:${cWidth}px; text-align:center; background-color:${
		week eq 1 or holidayList.contains(joinYmd) ? '#FFF2F2' : week eq 7 ? '#EEF1FF' : ''
		}; font-weight:bold; color:#525252; border-right:${ day % 5 eq 0 ? '1px solid lightgrey' : ''};" data-color="${
		week eq 1 or holidayList.contains(joinYmd) ? '#FFF2F2' : week eq 7 ? '#EEF1FF' : ''
		}" title="<u:out value='${monthlyMap.get(joinYmd).note}' type='value' />">${monthlyMap.get(joinYmd).rsltMd}</td><u:set
		test="${week eq 7}" var="week" value="1" elseValue="${week + 1}" />
	</c:forEach>
</tr>
</table>
</c:forEach>

<div class="blank"></div>
</c:if>
</c:forEach>

<u:buttonArea>
	<u:button titleId="cm.btn.close" alt="닫기" href="./listPrjMp.do?menuId=${menuId}&cat=${param.cat}&prjNo=${param.prjNo}" />
</u:buttonArea>


