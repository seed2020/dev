<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"%>
<link rel="stylesheet" href="${_cxPth}/css/calendar/fullcalendar.css" type="text/css" />
<link rel="stylesheet" href="${_cxPth}/css/calendar/fullcalendar.print.css" type="text/css" media="print"/>
<script type="text/javascript" src="${_cxPth}/js/calendar/moment.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar/fullcalendar.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar/lang-all.js" charset="UTF-8"></script>
<c:if test="${empty strtDt }"><jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="defaultDate" /></c:if>
<c:if test="${!empty strtDt }"><c:set var="defaultDate" value="${strtDt }"/></c:if>
<u:set var="viewTyp" test="${empty param.viewTyp }" value="month" elseValue="${param.viewTyp }"/>
<u:set test="${!empty param.fncCal}" var="fncCal" value="${param.fncCal}" elseValue="psn" />

<!-- 권한 -->
<c:if test="${!empty authChkW && authChkW == 'W' }"><c:set var="writeAuth" value="Y"/></c:if>
<style>
.fc-day-txt {
    display: inline-block;
    *display:inline: ;
    float: ${viewTyp eq 'month' ? 'none' : 'left'};
    padding: 1px 0 0 2px;    
    font-size: 12px;
    font-weight: bold;
    zoom: 1;
    vertical-align: top;
    cursor: pointer;
}
</style>
<script type="text/javascript">
<% // [상단버튼:국가조회] 팝업 %>
function setNatPop(isSrch) {
	var url = './setNatPop.do?menuId=${menuId}&fncCal=${fncCal}';
	var title = '<u:msg titleId="wc.btn.set.nat" alt="국가설정" />';
	if(isSrch) {
		url+= '&chkNatCd=${param.natCd}&callback=searchNat';
		title = '<u:msg titleId="wc.btn.chn.nat" alt="국가변경" />';
	}
	dialog.open('setNatDialog', title, url);
}
<% // 국가기념일 조회 %>
function searchNat(cds){
	if(cds==undefined || cds.length>1) return;
	var $form = $('#calendarPrintForm');
	$form.find("[name='natCd']").remove();
	$form.appendHidden({name:'natCd',value:cds[0]});
	$form.submit();
}
<% // 상세보기 팝업 %>
function viewSchdlPop(schdlId) {
	//schdl 값 알아내기 위해서 셋팅
	$("#scds_schdlId").val(schdlId);
	dialog.open('viewSchdlPop','<u:msg titleId="wc.btn.schdlDetail" alt="상세보기" />','./viewSchdlPop.do?${params}&schdlId='+schdlId+'');
}

<% // 등록 팝업 %>
function setSchdlPop(schdlId){
	//쓰기 권한 체크
	if('${writeAuth}' != 'Y') return;
	var url = "./setSchdlPop.do";
	var popTitle = '<u:msg titleId="wc.btn.schdlReg" alt="일정등록" />';
	if(arguments.length > 1){
		var schdlStartDt = arguments[1];
		url+= "?schdlStartDt="+schdlStartDt;
		if(arguments[2] != undefined && arguments[2]!= null && !arguments[2]) url+= "&alldayYn=Y";
	}else{
		if(schdlId != null) {
			url+= "?schdlId="+schdlId;
			popTitle = '<u:msg titleId="wc.btn.schdlMod" alt="일정수정" />';
		}
	}
	url+= url.indexOf('?') > -1 ? '&' : '?';
	url += "menuId=${menuId}";
	url += "&ctId=${param.ctId}";
	dialog.open('setSchdlPop',popTitle,url);
	dialog.onClose("setSchdlPop", function(){ editor('cont').clean(); unloadEvent.removeEditor('cont'); });
};
<% // 일정 이동 %>
function selectMoveDate(date){
	schdlEvent(null, 'moveDay', date);
}
var totalList = null;
<% // 리로드 %>
function reloadCalendar(){
	dialog.closeAll();
	$('#calendar').fullCalendar('refetchEvents');
	totalList = $('#calendar').fullCalendar('clientEvents');
}
<% // 카테고리 클릭%>
function showSchdlList(catId){
	if(totalList==null) totalList = $('#calendar').fullCalendar('clientEvents');
	var addEvents =[];
	if(catId==null) {
		$('#calendar').fullCalendar('removeEvents');
		$('#calendar').fullCalendar('addEventSource', totalList);	
		return;
	}
	$.each(totalList, function(index, obj) {
		if(obj.schdlTypCd==catId) addEvents.push(obj);
	});
	$('#calendar').fullCalendar('removeEvents');
	$('#calendar').fullCalendar('addEventSource', addEvents);
}

<% // 타이틀 업데이트 %>
function updateSchdlTitle(){
	$('#todayTd').text($('#calendar').fullCalendar('getView').title);
}

<% // 날짜 세팅 %>
function getAddDt(viewObj, strtDt, val){
	if(val=='p') strtDt = moment(strtDt).clone().startOf(viewObj.intervalUnit).add(viewObj.intervalDuration).format('YYYY-MM-DD');
	else if(val=='m') strtDt = moment(strtDt).clone().startOf(viewObj.intervalUnit).subtract(viewObj.intervalDuration).format('YYYY-MM-DD');
	return strtDt;
	
}
<% // 탭|네비 버튼 클릭 %>
function schdlEvent(actKey, val, sDate){
	var $form = $('#calendarPrintForm');
	var url = "./listNewSchdl.do";
	var strtDt = '${defaultDate}';
	if(actKey=='changeView'){
		url+="?viewTyp="+val;
	}else{
		url+="?viewTyp=${viewTyp}";
		//url+="&pmVal="+val;
		if(val=='today') strtDt = $('#calendar').fullCalendar('getNow').format('YYYY-MM-DD');
		else if(val=='moveDay') strtDt = sDate;
		else strtDt = getAddDt($('#calendar').fullCalendar('getView'), strtDt, val);
	}
	$form.find("input[name='strtDt']").val(strtDt);
	url+="&"+$form.serialize();
	location.href=url;
	//updateSchdlTitle();
}
var isAnnv = false; // 기념일 삽입여부
var colMap = null;
<% // 일정종류 색상코드 조회 %>
function getColMap(key){
	if(colMap==null){
		colMap = new ParamMap();
		<c:forEach var="list" items="${wcCatClsBVoList }" varStatus="status">
			colMap.put('bg_${list.catId}','${list.bgcolCd}');
			colMap.put('font_${list.catId}','${list.fontColrCd}');
		</c:forEach>
	}
	return colMap.get(key);
}<% // 기념일 삽입 %>
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
<% // 일정 수정 %>
function updateEvents(event, revertFunc){	
	var param = new ParamMap().getData("calendarPrintForm");
	var dftFmt = 'YYYY-MM-DD HH:mm';
	var start = event.start.clone();
	var endFormat = event.allDay ? 'YYYY-MM-DD' : dftFmt;
	var end = event.end == null ? start.clone() : event.end.clone();
	if(event.end!=null && event.allDay) end.add(-1, 'days');
	if(event.end==null && !event.allDay) end.add(1, 'hour');
	
	var schdlId = event.schdlId;
	var alldayYn = event.allDay ? "Y" : "N";
	param.put('schdlId', schdlId);
	param.put('start', start.format(dftFmt));
	param.put('end', end.format(endFormat));
	param.put('alldayYn', alldayYn);
	
	callAjax('./transSchdlAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
			revertFunc();
		}
		if(data.result == 'ok'){
			if(totalList==null) totalList = $('#calendar').fullCalendar('clientEvents');
			$.each(totalList, function(index, obj) {
				if(schdlId==obj.schdlId){
					obj.start = event.start;
					obj.end = event.end;
					obj.allDay = event.allDay;
				}
			});
		}
	});
}
<% // 일정조회 %>
function loadEvents(start, end, callback){
	var param = new ParamMap().getData("calendarPrintForm");
	param.put('start', start);
	param.put('end', end);
	param.put('natCd', '${param.natCd}');
	callAjax('./listSchdlAjx.do?menuId=${menuId}&fncCal=${fncCal}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			var events = [];
			var wcSchdlBVoList = data.wcSchdlBVoList, vo, editable, start, end, isSingle;
			var viewName = $('#calendar').fullCalendar('getView').name;
			$.each(wcSchdlBVoList, function(index, obj) {
				vo = obj.map;
				start = moment(vo.schdlStartDt).clone();
				end = moment(vo.schdlEndDt).clone();
				isSingle = viewName=='month' && vo.alldayYn!='Y' && start.isSame(end,'day');
				editable = vo.regrUid == '${sessionScope.userVo.userUid }';
				events.push({
					id:vo.schdlId,
					schdlId: vo.schdlId,
                    title: vo.subj,
                    start: start,
                    end: vo.alldayYn=='Y' ? end.add(1, 'days') : end,
                    allDay: vo.alldayYn=='Y',
                    color: isSingle ? '#fff' : getColMap('bg_'+vo.schdlTypCd),
                    textColor: isSingle ? getColMap('bg_'+vo.schdlTypCd) : getColMap('font_'+vo.schdlTypCd),
                    schdlTypCd: vo.schdlTypCd,
                    regrNm: vo.regrNm,
                    editable : editable
                });
        	});
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
		defaultView:'${viewTyp}',
		height: 650,
		lang: '${_lang}',//en,zh-cn
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
		allowDisplay:true,
		loading: function(bool) {
			//$('#loading').toggle(bool);
		},
		dayClick: function(date, jsEvent, view) {
			if(view.name=='agendaWeek' || view.name=='agendaDay'){
				setSchdlPop(null, moment(date).format('YYYYMMDDHHmm'), date.hasTime());
			}else{
				setSchdlPop(null, moment(date).format('YYYYMMDD'), null);
			}
	    },
	    eventDrop: function(event, delta, revertFunc) {
	    	updateEvents(event, revertFunc);
		},
		eventResize: function(event, delta, revertFunc, jsEvent, ui, view) {
			updateEvents(event, revertFunc);
		},
	    events: function(start, end, timezone, callback) {
	    	loadEvents(start, end, callback);
	    },
		eventClick: function(event) {//일정 클릭시
	        if (event.schdlId) {
	        	viewSchdlPop(event.schdlId);
	            return false;
	        }
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
		timeFormat: 'a hh:mm' // uppercase H for 24-hour clock
	});
	//updateSchdlTitle();
}
<% // tooltip 생성 %>
function tooltipCreate(event, jsEvent){
	var areaId = '#tooltip_'+event.schdlId;	
	var title = event.title;
	var regrNm = event.regrNm;
	var durationDt = event.start.clone().format('YYYY-MM-DD')+' ~ '+(event.allDay ? moment(event.end).clone().subtract(1,'days') : event.end.clone()).format('YYYY-MM-DD');
	var durationTime = (event.allDay ? '<u:msg titleId="wc.cols.wholeDay" alt="종일일정" />' : event.start.format('a hh:mm')+' ~ '+event.end.format('a hh:mm'));
	if($(areaId).html()==undefined){
		var html='<div id="tooltip_'+event.schdlId+'" class="fc-tooltip"><div id="tooltip" class="tooltip">';
		//html+='<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>';
		html+='<div class="tooltip_body"><div class="tooltip_text" ><ul>';
		html+='<li><div class="ellipsis" style="width:200px"><strong id="title">'+title+'</strong></div></li>';
		html+='<li class="blank_s2" ></li><li class="tooltip_line"></li>';
		html+='<li class="blank_s5"></li><li>';
		html+='<u:msg titleId="cols.regr" alt="등록자" /> : <span id="regrNm">'+regrNm+'</span><br />';		
		html+='<u:msg titleId="cols.prd" alt="기간" /> : <span id="durationDt">'+durationDt+'</span><br />';
		html+='<u:msg titleId="wc.cols.time" alt="시간" />: <span id="durationTime">'+durationTime+'</span><br />';
		html+='<span id="tooltip_content"></span>';
		html+='</li></ul></div></div></div>';		
		$('body').append(html);
	}else{
		var id = null;
		$(areaId).find('span, strong').each(function(){
			id = $(this).attr('id');
			if(id=='title') $(this).text(title);
			else if(id=='regrNm') $(this).text(regrNm);
			else if(id=='durationDt') $(this).text(durationDt);
			else if(id=='durationTime') $(this).text(durationTime);
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
});
//-->
</script>
<div class="front notPrint">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px">
				<u:title title="${menuTitle }"  alt="커뮤니티 일정" menuNameFirst="true"/>
			</td>
	 		</tr>
		</table>
	</div>
</div>
				
<input id="scds_promGstUid" type="hidden" value=""/>
<input id="scds_promGstNm" type="hidden" value=""/>
<input id="scds_promGstDptNm" type="hidden" value=""/>

<% // TAB %>
<div id="tabDiv" class="notPrint">
	<u:tabGroup id="schdlTab" >
	<u:tab id="schdlTab" areaId="molyList" titleId="wc.jsp.listPsnSchdl.tab.molySchdl" alt="월간일정" onclick="schdlEvent( 'changeView', 'month' );" on="${viewTyp eq 'month' }"/>
	<u:tab id="schdlTab" areaId="welyList" titleId="wc.jsp.listPsnSchdl.tab.welySchdl" alt="주간일정" onclick="schdlEvent( 'changeView', 'agendaWeek' );" on="${viewTyp eq 'agendaWeek' }"/>
	<u:tab id="schdlTab" areaId="dalyList" titleId="wc.jsp.listPsnSchdl.tab.dalySchdl" alt="일간일정" onclick="schdlEvent( 'changeView', 'agendaDay' );" on="${viewTyp eq 'agendaDay' }"/>
	<c:if test="${!empty authChkW && authChkW == 'W' }">
		<u:tabButton titleId="cols.reg" alt="등록" href="javascript:setSchdlPop();" />
	</c:if>
	<u:tabButton titleId="cm.btn.print" alt="인쇄" img="ico_print.png" imgW="14" onclick="printWeb();" />
	</u:tabGroup>
</div>

<% // 상단 FRONT %>
<div class="front">

	<div class="front_left">

		<form method="get" id="calendarPrintForm" name ="calendarPrintForm">
			<u:input type="hidden" name="menuId"  value="${menuId}"/>
			<input  type="hidden" id="ctId" name="ctId" value="${param.ctId }"/>
			<u:input type="hidden" id="viewTyp" value="${viewTyp}"/><!-- 탭 -->
			<u:input type="hidden" id="strtDt" value="${strtDt}"/><!-- 시작일자 -->
		
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>				
				<td id="todayTd" class="scd_head fc-title-header">&nbsp;</td>
				<td class="frontico notPrint"><a href="javascript:schdlEvent(null, 'm');"><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
				<td class="frontico notPrint"><a href="javascript:schdlEvent(null, 'p');"><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
				<td class="frontico notPrint"><u:buttonS href="javascript:schdlEvent(null, 'today');" titleId="wc.btn.today" alt="오늘" auth="W" /></td>
				<td class="notPrint"><div id="loading" style="display:none;"><img src="${_cxPth}/images/cm/bigWaiting.gif" width="22" height="22"/></div></td>
			</tr>
		</table>
		</form>
	</div>
	<div class="front_right notPrint" >
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td id="selectDtCalBtn">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<!-- 조회,수정 하기 위해서 schdlId 히든값셋팅 -->
				<td><input type="hidden" id="scds_schdlId"/></td>
				<td class="frontbtn" >
					<div id="selectDtCalArea" style="display: none; position: absolute;"></div>
					<u:buttonS href="javascript:calendar.open('selectDt',null,function(date, option){selectMoveDate(date);return true;})" titleId="wc.btn.dtMove" alt="날짜이동"   />
				</td>
			</tr>
		</table>
	</div>
</div>

<!-- 달력 -->
<div id="calendar"></div>
<u:blank />
<% // 하단 FRONT %>
<div class="front notPrint">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="frontbtn"><u:buttonBgS id="scd_all" titleId="wc.btn.allView" alt="전체보기" onclick="showSchdlList(null);" /></td>
				<td class="width5"></td>
				<c:forEach var="list" items="${wcCatClsBVoList }" varStatus="status">
					<td class="frontbtn"><u:buttonBgS id="scd_${list.catId }" title="${list.catNm }" alt="${list.catNm }" onclick="showSchdlList('${list.catId }')" bgCd="${list.bgcolCd eq '#ffffff' ? '' : list.bgcolCd }" fontColrCd="${list.fontColrCd }"/></td>
					<td class="width5"></td>
				</c:forEach>
			</tr>
		</table>
	</div>
</div>
