<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	// 공수입력칸 넓이
	request.setAttribute("cWidth", 48);
	request.setAttribute("role1Cds", new String[]{ "con", "dev", "all" });

	request.setAttribute("color1", "#F9FFFF");
	request.setAttribute("color2", "#F4FFFF");

%><script type="text/javascript">
<!--<%--
--%>
$(document).ready(function() {
	setUniformCSS();
});
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
<tr>
	<td class="head_ct"><u:msg titleId="wp.mdPlan" alt="투입계획" />(M/M)</td>
	<td class="body_lt"><u:out value="${prjMpDetails.planSum}" /></td>
	<td class="head_ct"><u:msg titleId="wp.mdRslt" alt="투입결과" />(M/M)</td>
	<td class="body_lt"><u:out value="${prjMpDetails.rsltSum}" /></td>
</tr>
</u:listArea>


<u:title titleId="wp.manPowerIn" alt="투입 인력" type="small" />

<div style="overflow-y:show; overflow-x:auto; width:100%">
<u:listArea id="manPowerArea" tableStyle="width:${517 -60 + 36 + maxMCount * cWidth}px; table-layout: fixed">
<tr>
	<td class="head_ct" style="width:80px"><u:msg titleId="wp.prjRole1Cd" alt="역할분류" /></td>
	<td class="head_ct" style="width:130px"><u:msg titleId="wp.prjRole1Cd" alt="역할" /></td>
	<td class="head_ct" style="width:80px"><u:msg titleId="cm.gubun" alt="구분" /></td>
	<td class="head_ct" style="width:80px"><u:msg titleId="cols.nm" alt="이름" /></td>
	<td class="head_ct" style="width:60px"></td>
	<td class="head_ct" style="width:60px"><u:msg titleId="wp.sum" alt="합계" /></td><c:forEach
		begin="${prjMpDetails.advance ? 0 : 1}" end="${prjMpDetails.maxMno}" step="1" var="mNo" varStatus="status">
	<td class="head_ct" style="width:${cWidth}px"><c:if
		test="${mNo eq 0}"><u:msg titleId="wp.advance" alt="선투입" /></c:if><c:if
		test="${mNo ne 0}"><span title="M+${mNo}">${prjMpDetails.toYearMonth(mNo)}</span></c:if></td>
	</c:forEach>
</tr><c:forEach items="${role1Cds}" var="cd1"><c:if

	test="${cd1 eq 'con' or cd1 eq 'dev'}"><u:convert
		srcId="${cd1}WpPrjMpPlanDVoList" var="wpPrjMpPlanDVoList" /><c:forEach
		items="${wpPrjMpPlanDVoList}" var="wpPrjMpPlanDVo" varStatus="trStatus"><%--${cd1 eq 'con' and trStatus.first ? '' : 'border-top:1px solid #CECECE;'}--%>
<tr data-role1Cd="${cd1}" >
	<td class="body_ct" style="width:80px; border-bottom:1px solid #CECECE;" id="role1CdTd" rowspan="2"><c:if
		test="${not empty wpPrjMpPlanDVo.prjRole1Cd}"><u:msg
			titleId="wp.prjRole1Cd.${wpPrjMpPlanDVo.prjRole1Cd}" /></c:if></td>
	<td class="body_ct" style="width:190px; border-bottom:1px solid #CECECE;" id="role2CdTd" rowspan="2"><c:if
		test="${not empty wpPrjMpPlanDVo.prjRole2Cd}"><u:msg titleId="wp.prjRole2Cd.${wpPrjMpPlanDVo.prjRole2Cd}" /></c:if></td>
	<td class="body_ct" style="width:80px; border-bottom:1px solid #CECECE;" id="mpTypCdTd" rowspan="2"><c:if
		test="${not empty wpPrjMpPlanDVo.mpTypCd}"><u:msg
			titleId="wp.mpTypCd.${wpPrjMpPlanDVo.mpTypCd}" /></c:if></td>
	<td class="body_ct" style="width:80px; border-bottom:1px solid #CECECE;" id="mpNmTd" rowspan="2"><u:out value="${wpPrjMpPlanDVo.mpNm}" /></td><u:convert
		value="${prjMpDetails.getPlanMap(wpPrjMpPlanDVo.mpId)}" var="planMap" />
	
	<td class="body_ct" style="width:60px; " id="sumTd"><u:msg titleId="wp.plan" alt="계획" /></td>
	<td class="body_ct" style="width:60px; " id="sumTd"><u:convertMap srcId="planMap" attId="-100" intKey="${true}" /></td><c:forEach
		begin="${prjMpDetails.advance ? 0 : 1}" end="${prjMpDetails.maxMno}" step="1" var="mNo" varStatus="status">
	<td class="body_ct" style="width:${cWidth}px; "><u:convertMap srcId="planMap" attId="${mNo}" hash="${true}" /></td>
	</c:forEach>
</tr>
<tr><u:convert
		value="${prjMpDetails.getRsltMap(wpPrjMpPlanDVo.mpId)}" var="rsltMap" />
	<td class="body_ct" style="width:60px; border-bottom:1px solid #CECECE;" id="sumTd"><u:msg titleId="wp.rslt" alt="결과" /></td>
	<td class="body_ct" style="width:60px; border-bottom:1px solid #CECECE;" id="sumTd"><u:convertMap srcId="rsltMap" attId="-100" intKey="${true}" /></td><c:forEach
		begin="${prjMpDetails.advance ? 0 : 1}" end="${prjMpDetails.maxMno}" step="1" var="mNo" varStatus="status">
	<td class="body_ct" style="width:${cWidth}px; border-bottom:1px solid #CECECE;"><u:convertMap srcId="rsltMap" attId="${mNo}" hash="${true}" /></td>
	</c:forEach>
</tr>
</c:forEach></c:if>
<tr data-role1Cd="${cd1}Sum"><%--border-top:1px solid #CECECE;--%>
	<td class="body_ct" colspan="4" rowspan="2" style="border-bottom:1px solid #CECECE; background-color: ${cd1 eq 'all' ? color2 : color1};"><u:msg titleId="wp.${cd1}Sum" alt="컨설팅 소계"/></td><u:convert
		value="${prjMpDetails.getSumMap(cd1, 'plan')}" var="sumMap" />
	
	<td class="body_ct" style="width:60px; background-color: ${cd1 eq 'all' ? color2 : color1};"><u:msg titleId="wp.plan" alt="계획" /></td>
	<td class="body_ct" style="width:${cWidth}px; background-color: ${cd1 eq 'all' ? color2 : color1};"><u:convertMap srcId="sumMap" attId="-100" intKey="${true}" /></td><c:forEach
		begin="${prjMpDetails.advance ? 0 : 1}" end="${prjMpDetails.maxMno}" step="1" var="mNo" varStatus="status">
	<td class="body_ct" style="width:${cWidth}px; background-color: ${cd1 eq 'all' ? color2 : color1};"><u:convertMap srcId="sumMap" attId="${mNo}" intKey="${true}" /></td>
	</c:forEach>
</tr>
<tr><u:convert
		value="${prjMpDetails.getSumMap(cd1, 'rslt')}" var="sumMap" />
		
	<td class="body_ct" style="width:60px; border-bottom:1px solid #CECECE; background-color: ${cd1 eq 'all' ? color2 : color1};"><u:msg titleId="wp.rslt" alt="결과" /></td>
	<td class="body_ct" style="width:${cWidth}px; border-bottom:1px solid #CECECE; background-color: ${cd1 eq 'all' ? color2 : color1};"><u:convertMap srcId="sumMap" attId="-100" intKey="${true}" /></td><c:forEach
		begin="${prjMpDetails.advance ? 0 : 1}" end="${prjMpDetails.maxMno}" step="1" var="mNo" varStatus="status">
	<td class="body_ct" style="width:${cWidth}px; border-bottom:1px solid #CECECE; background-color: ${cd1 eq 'all' ? color2 : color1};"><u:convertMap srcId="sumMap" attId="${mNo}" intKey="${true}" /></td>
	</c:forEach>
</tr></c:forEach>
</u:listArea>
</div>

<div class="blank"></div>


<u:buttonArea>
	<u:button titleId="wp.detl" alt="상세" href="./listPrjMpDetl.do?menuId=${menuId}&cat=${param.cat}&prjNo=${param.prjNo}" />
	<u:button titleId="cm.btn.close" alt="닫기" href="./listPrj.do?menuId=${menuId}&cat=${param.cat}" />
</u:buttonArea>