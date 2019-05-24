<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"%><%
	request.setAttribute("bg_blue", "#065ae0");
	request.setAttribute("bg_red",  "#ef4040;");
%>
<link rel="stylesheet" href="${_cxPth}/css/calendar/fullcalendar.css" type="text/css" />
<link rel="stylesheet" href="${_cxPth}/css/calendar/fullcalendar.print.css" type="text/css" media="print"/>
<script type="text/javascript" src="${_cxPth}/js/calendar/moment.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar/fullcalendar.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar/lang-all.js" charset="UTF-8"></script>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy" var="endYear" />
<c:if test="${empty strtDt }"><fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="defaultDate" /></c:if>
<c:if test="${!empty strtDt }"><c:set var="defaultDate" value="${strtDt }"/></c:if>
<style>
.fc-day-txt {
    display: inline-block;
    *display:inline: ;
    float: left;
    padding: 1px 0 0 2px;    
    font-size: 12px;
    font-weight: bold;
    zoom: 1;
    vertical-align: top;
    cursor: pointer;
}
</style>
<script type="text/javascript">
<!--
var pageType='page'; // 페이지
<% // 마감구분 선택 %>
function selectCloseKnd(obj){
	if(pageType=='ajax'){
		$('#calendar').fullCalendar('removeEvents');
		$('#calendar').fullCalendar('refetchEvents');
	}else{
		var $form = $('#searchForm');
		var url = "./listCalDeadline.do";
		url+="?"+$form.serialize();
		location.href=url;	
	}
	//$('#calendar').fullCalendar('updateEvents');
}<% // 일정 이동 %>
function selectDate(){
	var strtYear=$('#searchForm select[id="strtYear"]');
	var strtMonth=$('#searchForm select[id="strtMonth"]');
	if(strtYear.val()=='' || strtMonth.val()=='') return;
	var strtDt=strtYear.val()+'-'+strtMonth.val()+'-01';	
	if(pageType=='ajax'){		
		$('#calendar').fullCalendar('gotoDate', strtDt);
	}else{
		var $form = $('#searchForm');
		var url = "./listCalDeadline.do";
		$form.find("input[name='strtDt']").val(strtDt);
		url+="?"+$form.serialize();
		location.href=url;	
	}
}
var totalList = null;
<% // 카테고리 클릭%>
function setCloseYn(yn){
	if(totalList==null) totalList = $('#calendar').fullCalendar('clientEvents');
	var addEvents =[];
	if(yn==null) {
		$('#calendar').fullCalendar('removeEvents');
		$('#calendar').fullCalendar('addEventSource', totalList);	
		return;
	}
	$.each(totalList, function(index, obj) {
		if(obj.closeYn==yn) addEvents.push(obj);
	});
	$('#calendar').fullCalendar('removeEvents');
	$('#calendar').fullCalendar('addEventSource', addEvents);
}

<% // 타이틀 업데이트 %>
function updateSchdlTitle(){
	$('#todayTd').text($('#calendar').fullCalendar('getView').title);
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
	var url = "./listCalDeadline.do";
	if(val!=null){
		if(val=='today') strtDt = moment().format('YYYY-MM-DD');
		else strtDt = getAddDt('month', '${defaultDate}', val);
	}
	if(strtDt===undefined) strtDt='${defaultDate}';
	$form.find("input[name='strtDt']").val(strtDt);
	url+="?"+$form.serialize();
	location.href=url;
}
var isAnnv = false; // 기념일 삽입여부
<% // 기념일 삽입 %>
function setAnnvList(annvList){
	if(isAnnv) return;
	var vo, day, strtDay, endDay, html, tgtDiv, titles;
	var isCreate;
	$('td.fc-day-number, th.fc-day-header').each(function(index, parent){
		day = moment($(this).attr('data-date')).format('YYYYMMDD');
		isCreate = true;
		titles = '';
		tgtDiv = null;
		$.each(annvList, function(index, obj) {
			vo = obj.map;
			strtDay = moment(vo.schdlStartDt).format('YYYYMMDD');
			endDay = moment(vo.schdlEndDt).format('YYYYMMDD');
			if(strtDay<=day && endDay>=day){
				if(vo.holiYn == 'Y') $(parent).addClass('fc-day-red');
				if(isCreate){
					html='<div class="fc-day-container ellipsis"></div>';
					$(parent).append(html);
				}
				if(tgtDiv==null) tgtDiv = $(parent).find('div.fc-day-container');
				html='<span class="fc-day-info'+(vo.holiYn != 'Y' ? ' fc-day-info-week' : '')+'">';
				html+=vo.subj;
				html+='</span>';
				$(tgtDiv).append(html);
				titles+=titles=='' ? vo.subj : ', '+vo.subj;
				isCreate = false;
			}
		});
		if(titles!='') $(tgtDiv).attr('title', titles);
	});
	
	isAnnv = true;
}
<% // 일정조회 %>
function loadEvents(start, end, callback){
	var param = new ParamMap().getData("searchForm");
	param.put('start', start);
	param.put('end', end);
	callAjax('./listCalDeadlineAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			var currentDate=moment($('#calendar').fullCalendar('getDate')).format('YYYY-MM-DD');
			$('#searchForm #strtDt').val(currentDate);
			var events = [];
			var deadlineList = data.deadlineList, vo, start, end, id, closeYn;
			if(deadlineList!=null){
				$.each(deadlineList, function(index, obj) {				
					vo = obj.map;
					if(vo.closePlanDt===undefined) return true;
					start = moment(vo.closePlanDt).clone();
					end = moment(vo.closePlanDt).clone();
					id=vo.yyyymm+vo.closeKind;
					closeYn=vo.closeConfirmDt!=undefined;
					events.push({
						id:id,
						schdlId: id,
	                    title: (closeYn ? '● ' : '◎ ') +vo.deptNm,
	                    start: start,
	                    end: end,
	                    allDay: true,
	                    closeKind: vo.closeKind,
	                    closeType: vo.closeType,
	                    majorNm: vo.majorNm,
	                    minorNm: vo.minorNm,
	                    closePlanDt: vo.closePlanDt===undefined ? '' : moment(vo.closePlanDt).clone(),
	                    closeDt: vo.closeDt===undefined ? '' : moment(vo.closeDt).clone(),
	                    closeConfirmDt: vo.closeConfirmDt===undefined ? '' : moment(vo.closeConfirmDt).clone(),
	                    color: 'transparent',
	                    textColor: closeYn ? '${bg_blue }' : '${bg_red }',
	                    closeYn: closeYn ? 'Y' : 'N',
	                    editable : false
	                });
	        	});
			}
			// 기념일
			setAnnvList(data.annvList);
			callback(events);
		}
	});
}

<% // 일정 초기화 %>
function initCalendar(){
	$('#calendar').fullCalendar({
		header: {left: '', center: '', right: ''},
		defaultView:'month',
		height: browser.ie && browser.ver <=7 ? 670 : 650,
		lang: '${_lang}'=='zh' ? 'zh-cn' : '${_lang}',//en,zh-cn
		defaultDate: '${defaultDate}',
		titleRangeSeparator:'~',
		titleFormat: {month: 'YYYY.MM', week: "MM.DD", day: 'YYYY.MM.DD'},
		selectable: true,
		unselectAuto: true,
		dayOfMonthFormat:'D ddd',//요일 날짜
		dayOfDayFormat:'D ddd',
		editable: true,
		eventLimit: true, // allow "more" link when too many events
		//minTime: "06:30:00",//시작시간
	    //maxTime: "24:00:00",//종료시간
	    //slotDuration: '00:30:00', 단위
		slotLabelFormat: 'a hh:mm',
		slotEventOverlap:false,//겹치는 일정 표시(true:겹침,false:겹치지않게)
		dragOpacity: {agenda: 0.5},//드래그 할때 투명도 조절
		defaultTimedEventDuration: '01:00:00', // 타임라인 기본 간격(이동시)
		//tooltip:true, // tooltip 보이기 여부
		allowDisplay:true,
		loading: function(bool) {
			//$('#loading').toggle(bool);
		},
		dayClick: function(date, jsEvent, view) {
			return false;
	    },
	    eventDrop: function(event, delta, revertFunc) {
	    	return false;
		},
		eventResize: function(event, delta, revertFunc, jsEvent, ui, view) {
			return false;
		},
	    events: function(start, end, timezone, callback) {
	    	loadEvents(start, end, callback);
	    },
		eventClick: function(event) {//일정 클릭시
			return false;
	    },
	    eventAfterAllRender:function(view){
	    	updateSchdlTitle(); // 타이틀
		},	   
	    eventMouseover:function(event, jsEvent, view ){
	    	tooltipCreate(event, jsEvent);
		},
		eventMouseout:function(event, jsEvent, view ){
			$('div.fc-tooltip').hide();
		},	    
		timeFormat: 'HH:mm' // uppercase H for 24-hour clock
	});
	//updateSchdlTitle(); // 타이틀
}

<% // tooltip 생성 %>
function tooltipCreate(event, jsEvent){
	var areaId = '#tooltip_'+event.schdlId;	
	var title = event.title;
	var majorNm = event.majorNm;
	var closeType = event.closeType;
	var minorNm = event.minorNm;
	var closePlanDt = event.closePlanDt!='' ? event.closePlanDt.clone().format('YYYY-MM-DD') : '';
	var closeDt = event.closeDt!='' ? event.closeDt.clone().format('YYYY-MM-DD') : '';
	var closeConfirmDt = event.closeConfirmDt!='' ? event.closeConfirmDt.clone().format('YYYY-MM-DD') : '';
	if($(areaId).html()==undefined){
		var html='<div id="tooltip_'+event.schdlId+'" class="fc-tooltip"><div id="tooltip" class="tooltip">';
		//html+='<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>';
		html+='<div class="tooltip_body"><div class="tooltip_text" ><ul>';
		html+='<li><div class="ellipsis" style="width:200px"><strong id="title">'+title+'</strong></div></li>';
		if((browser.ie && browser.ver==7)){
			html+='<li class="tooltip_line"></li>';
		}else{
			html+='<li class="blank_s2" ></li>';
			html+='<li class="tooltip_line"></li>';
			html+='<li class="blank_s5"></li>';
		}
		//html+='<li>마감구분 : <span id="majorNm">'+majorNm+'</span></li>';		
		//html+='<li>마감유형 : <span id="closeType">'+closeType+'</span></li>';
		html+='<li>마감구분 : <span id="minorNm">'+minorNm+'</span></li>';
		html+='<li>마감계획일 : <span id="closePlanDt">'+closePlanDt+'</span></li>';
		html+='<li>마감일자 : <span id="closeDt">'+closeDt+'</span></li>';
		//html+='<li>마감확인 : <span id="closeConfirmDt">'+closeConfirmDt+'</span></li>';
		html+='<span id="tooltip_content"></span>';
		html+='</li></ul></div>';		
		html+='</div></div>';
		$('body').append(html);
	}else{
		var id = null;
		$(areaId).find('span, strong').each(function(){
			id = $(this).attr('id');
			if(id=='title') $(this).text(title);
			else if(id=='majorNm') $(this).text(majorNm);
			else if(id=='closeType') $(this).text(closeType);
			else if(id=='minorNm') $(this).text(minorNm);
			else if(id=='closePlanDt') $(this).text(closePlanDt);
			else if(id=='closeDt') $(this).text(closeDt);
			else if(id=='closeConfirmDt') $(this).text(closeConfirmDt);
		});
	}
	var maxWidth = $(document).width();
	if(((jsEvent.pageX-20)+210)>maxWidth) $(areaId).css('left', maxWidth-230);
	else $(areaId).css('left', jsEvent.pageX - 20);
	$(areaId).css('top', jsEvent.pageY + 10);
    $(areaId).show();
}

$(document).ready(function() {
	setUniformCSS();	
	initCalendar(); // 일정 초기화
	var wrapper=$('div.homewrapper');
	var classList=wrapper.attr('class');
	$.each(classList.split(' '), function(index, name){
		if(name.indexOf('print')>-1) wrapper.removeClass(name);
	});
	wrapper.addClass('printAp10');
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
				<td id="todayTd" class="scd_head fc-title-header" style="margin:0px;padding:5px 0 0 2px;">&nbsp;</td>
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
				<td class="fronttit notPrint">마감구분</td>
				<td class="frontinput notPrint">
					<select id="closeKind" name="closeKind" onchange="selectCloseKnd(this);" style="min-width:70px;">
						<u:option value="" titleId="cm.option.all" selected="${empty param.closeKind }" alt="전체" />
						<c:forEach var="closeKindMap" items="${closeKindList }" varStatus="status"><u:option value="${closeKindMap.closeKind }" title="${closeKindMap.minorNm }" checkValue="${param.closeKind }"/></c:forEach>
					</select> 
				</td>
				<td class="notPrint"><div id="loading" style="display:none;"><img src="${_cxPth}/images/cm/bigWaiting.gif" width="22" height="22"/></div></td>
			</tr>
		</table>
	</form>
</div>
<div class="front_right notPrint"><u:buttonS titleId="cm.btn.print" alt="인쇄" onclick="printWeb();"/></div>
</div>

<!-- 달력 -->
<div id="calendar"></div>
<u:blank />
<% // 하단 FRONT %>
<div class="front notPrint">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="frontbtn"><u:buttonBgS titleId="wc.btn.allView" alt="전체보기" onclick="setCloseYn(null);" style="font-size:12px;"/></td>
				<td class="width5"></td>
				<td class="frontbtn"><u:buttonBgS title="● 마감" alt="마감" fontColrCd="${bg_blue }" onclick="setCloseYn('Y');" style="font-size:12px;"/></td>
				<td class="width5"></td>
				<td class="frontbtn"><u:buttonBgS title="◎ 미마감" alt="미마감" fontColrCd="${bg_red }" onclick="setCloseYn('N');" style="font-size:12px;"/></td>
			</tr>
		</table>
	</div>
</div>