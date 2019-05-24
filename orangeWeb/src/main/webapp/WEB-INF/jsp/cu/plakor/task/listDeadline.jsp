<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ page import="java.util.Calendar"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	//request.setAttribute("bg_blue", "#0080FF;");
	//request.setAttribute("bg_red",  "#f78181;");
	
	request.setAttribute("bg_blue", "#5e9eed");
	request.setAttribute("bg_red",  "#d68277");
%>
<c:set var="maxDate" value="<%=Calendar.getInstance().getActualMaximum(Calendar.DATE) %>"/>
<script type="text/javascript" src="${_cxPth}/js/calendar/moment.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar/locales.min.js" charset="UTF-8"></script>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy" var="endYear" />
<c:if test="${empty strtDt }"><fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="defaultDate" /></c:if>
<c:if test="${!empty strtDt }"><c:set var="defaultDate" value="${strtDt }"/></c:if>
<!-- 페이지 갯수 초기화 -->
<u:set var="pageCnt" test="${!empty param.pageCnt && param.pageCnt.matches('[0-9]+') && (param.pageCnt==3 || param.pageCnt%5==0) && param.pageCnt>=3 && param.pageCnt<=30}" value="${param.pageCnt }" elseValue="5"/>
<style type="text/css">
.status_list {display:block;width:100%;}
.status {border: 1px solid rgb(191, 200, 210); border-image: none; background-color: rgb(255, 255, 255);width:89%;height:20px;margin:3px 0 2px 0;border-radius:5px;float:left;}
.status div.progress{height:100%;text-align:center;}
.status div.bg_blue{background:${bg_blue}}
.status div.bg_red{background:${bg_red}}
.status div.progress span{color:#FFFFFF;}
.status_cnt {width:9%;height:20px;margin:5px 0 2px 5px;float:left;font-weight:bold;font-size:14px;}

@media print {
#closeContainer {width:100% !important;clear:both;margin-left:0px !important;;}
#listArea{height:100% !important;}
}
</style>
<script type="text/javascript">
<!--
<% // 타이틀 업데이트 %>
function updateSchdlTitle(){
	$('#todayTd').text(moment('${defaultDate}').format('YYYY.MM'));
}<% // 날짜 세팅 %>
function getAddDt(intervalUnit, strtDt, val){
	var interval=intervalUnit=='month' ? intervalUnit : 'days';
	var duration=intervalUnit=='week' ? 7 : 1;
	if(val=='next') strtDt = moment(strtDt).clone().startOf(intervalUnit).add(duration, interval).format('YYYY-MM-DD');
	else if(val=='prev') strtDt = moment(strtDt).clone().startOf(intervalUnit).subtract(duration, interval).format('YYYY-MM-DD');
	return strtDt;
	
}<% // 탭|네비 버튼 클릭 %>
function schdlEvent(val, strtDt){
	var $form = $('#searchForm');
	var url = "./listDeadline.do";
	if(val!=null){
		if(val=='today') strtDt = moment().format('YYYY-MM-DD');
		else strtDt = getAddDt('month', '${defaultDate}', val);
	}
	if(strtDt===undefined) strtDt='${defaultDate}';
	$form.find("input[name='strtDt']").val(strtDt);
	url+="?"+$form.serialize();
	location.href=url;
}<% // 일정 이동 %>
function selectDate(){
	var strtYear=$('#searchForm select[id="strtYear"]');
	var strtMonth=$('#searchForm select[id="strtMonth"]');
	if(strtYear.val()=='' || strtMonth.val()=='') return;
	var strtDt=strtYear.val()+'-'+strtMonth.val()+'-01';
	schdlEvent(null, strtDt);	
}<% // 페이지 갯수 변경 %>
function selectPageCnt(){
	schdlEvent(null);
}
$(document).ready(function() {
	setUniformCSS();
	updateSchdlTitle();
});
//-->
</script>
<u:title title="업무마감" menuNameFirst="true"/>
<% // 상단 FRONT %>
<div class="front">
<div class="front_left">
	<form method="get" id="searchForm" name ="searchForm">
		<u:input type="hidden" name="menuId"  value="${menuId}"/>
		<u:input type="hidden" id="strtDt" value="${strtDt}"/><!-- 시작일자 -->
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>				
				<td id="todayTd" class="scd_head fc-title-header" style="margin:0px;padding:5px 0 0 2px;font-size:14px;">&nbsp;</td>
				<td style="width:9px;">&nbsp;</td>
				<td class="frontico notPrint"><a href="javascript:schdlEvent('prev');"><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
				<td class="frontico notPrint"><a href="javascript:schdlEvent('next');"><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
				<td class="frontico notPrint"><u:buttonS href="javascript:schdlEvent('today');" titleId="wc.btn.today" alt="오늘" auth="W" /></td>
				<td class="fronttit notPrint">마감연도</td>
				<td class="frontinput notPrint">
					<select id="strtYear" onchange="selectDate();">
						<u:option value="" titleId="cm.select.actname" selected="true"/>
						<c:forEach var="year" begin="0" end="${endYear-1980 }" varStatus="status" ><u:option value="${endYear-year }" title="${endYear-year }" checkValue="${fn:substring(defaultDate,0,4) }"/></c:forEach>
					</select> 
				</td>
				<td class="fronttit notPrint">마감월</td>
				<td class="frontinput notPrint">
					<select id="strtMonth" onchange="selectDate();">
						<u:option value="" titleId="cm.select.actname" selected="true"/>
						<c:forEach var="month" begin="1" end="12" varStatus="status"
						><u:set var="strtMonth" test="${month<10 }" value="0${month }" elseValue="${month }"
						/><u:option value="${strtMonth }" title="${strtMonth }" checkValue="${fn:substring(defaultDate,5,7) }"/></c:forEach>
					</select> 
				</td>
				<td class="fronttit notPrint">목록갯수</td>
				<td class="frontinput notPrint">
					<select id="pageCnt" name="pageCnt" onchange="selectPageCnt();">
						<u:option value="3" title="3" checkValue="${pageCnt}"/>
						<c:forEach var="cnt" begin="1" end="10" varStatus="status"
						><u:option value="${cnt*5 }" title="${cnt*5 }" checkValue="${pageCnt}"/></c:forEach>
					</select> 
				</td>
				<td class="notPrint"><div id="loading" style="display:none;"><img src="${_cxPth}/images/cm/bigWaiting.gif" width="22" height="22"/></div></td>
			</tr>
		</table>
	</form>
</div>
<div class="front_right notPrint"><u:buttonS titleId="cm.btn.print" alt="인쇄" onclick="printWeb();"/></div>
</div>
<div class="notPrint" style="width:100%;">
<u:blank />
<div class="status" style="width:100px;"><div class="progress bg_blue" style="width:100%;"><span style="line-height:22px;">계획일</span></div></div>
<div class="status" style="width:100px;"><div class="progress bg_red" style="width:100%;"><span style="line-height:22px;">확정일</span></div></div>
<u:blank />
</div>
<!-- 목록 -->
<div style="width:100%;display:block;">
<c:if test="${empty closeKindList }">
<div style="display:block;float:left;margin-left:0px;width:48%;margin-top:10px;">
<u:title title="마감" type="small" alt="마감" />
<div class="status_list"><div class="status"><div class="progress bg_blue" style="width:0%;"></div></div><div class="status_cnt" style="color:${bg_blue}">0 일</div></div>
<div class="status_list"><div class="status"><div class="progress bg_red" style="width:0%;"></div></div><div class="status_cnt" style="color:${bg_red}">0 일</div></div>
<u:listArea id="listArea" colgroup=",20%,20%,15%">
	<tr id="headerTr">
		<th class="head_ct">주관부서</th>
		<th class="head_ct">계획일</th>
		<th class="head_ct">확정일</th>
		<th class="head_ct">지연일</th>
	</tr>
	<tr>
	<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</u:listArea>
</div>
</c:if>
<c:if test="${!empty closeKindList }">
<c:set var="map" value="${deadlineListMap }" scope="request"/>
<c:forEach var="closeKindMap" items="${closeKindList }" varStatus="status">
<div id="closeContainer" style="display:block;float:left;margin-left:${status.index>0 && (status.index+1)%2==0 ? '15' : '0'}px;width:48%;margin-top:10px;">
<u:convertMap var="deadlineList" srcId="map" attId="deadlineMap_${closeKindMap.closeKind }" type="html"/>
<u:title title="${closeKindMap.minorNm } ( ${fn:length(deadlineList)} )" type="small" alt="${closeKindMap.minorNm }" />
<u:set var="closePlanDt" test="${empty closeKindMap.closePlanDt }" value="0" elseValue="${closeKindMap.closePlanDt }"/>
<u:set var="deptCloseDt" test="${empty closeKindMap.deptCloseDt }" value="0" elseValue="${closeKindMap.deptCloseDt }"/>
<div class="status_list"><div class="status"><div class="progress bg_blue" style="width:${closePlanDt/maxDate*100}%;"></div></div><div class="status_cnt" style="color:${bg_blue}">${closePlanDt } 일</div></div>
<div class="status_list"><div class="status"><div class="progress bg_red" style="width:${deptCloseDt/maxDate*100}%;"></div></div><div class="status_cnt" style="color:${bg_red}">${deptCloseDt } 일</div></div>
<c:set var="maxHeight" value="${(pageCnt*25)+26 }"/>
<u:listArea id="listArea" colgroup=",20%,20%,15%" style="overflow-y:auto;height:${maxHeight }px;">
	<tr id="headerTr">
		<th class="head_ct">주관부서</th>
		<th class="head_ct">계획일</th>
		<th class="head_ct">확정일</th>
		<th class="head_ct">지연일</th>
	</tr>
	<c:if test="${fn:length(deadlineList) == 0}">
		<tr>
		<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(deadlineList)>0}">
		<c:forEach items="${deadlineList}" var="deadlineMap" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<td class="body_lt">${deadlineMap.deptNm }</td>
				<td class="body_ct"><u:out value="${deadlineMap.closePlanDt }" type="date"/></td>
				<td class="body_ct"><u:out value="${deadlineMap.closeConfirmDt }" type="date"/></td>
				<td class="body_ct">
					<c:if test="${!empty deadlineMap.closePlanDt && !empty deadlineMap.closeConfirmDt}">
						<fmt:parseDate value="${deadlineMap.closePlanDt }" var="planDt" pattern="yyyy-MM-dd"/>
						<fmt:parseNumber value="${planDt.time / (1000*60*60*24)}" integerOnly="true" var="strDate" />
						<fmt:parseDate value="${deadlineMap.closeConfirmDt }" var="closeDt" pattern="yyyy-MM-dd"/>
						<fmt:parseNumber value="${closeDt.time / (1000*60*60*24)}" integerOnly="true" var="endDate" />
						<u:set var="delayDate" test="${endDate-strDate >0}" value="${endDate-strDate }일" elseValue=""/>
						${delayDate }
					</c:if>
				</td>
			</tr>
		</c:forEach>	
	</c:if>
</u:listArea>
</div>
</c:forEach>
</c:if>
</div>


