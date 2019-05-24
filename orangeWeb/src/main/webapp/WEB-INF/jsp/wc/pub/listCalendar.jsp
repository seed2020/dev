<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"%>
<link rel="stylesheet" href="${_cxPth}/css/calendar/fullcalendar.css" type="text/css" />
<link rel="stylesheet" href="${_cxPth}/css/calendar/fullcalendar.print.css" type="text/css" media="print"/>
<script type="text/javascript" src="${_cxPth}/js/calendar/moment.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar/locales.min.js" charset="UTF-8"></script>
<c:if test="${empty strtDt }"><jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="defaultDate" /></c:if>
<c:if test="${!empty strtDt }"><c:set var="defaultDate" value="${strtDt }"/></c:if>
<u:set var="viewTyp" test="${empty param.viewTyp }" value="month" elseValue="${param.viewTyp }"/>
<!-- 권한 -->
<u:secu auth="A" ><c:set var="writeAuth" value="Y"/></u:secu>
<style type="text/css">
.fc-content-month{width:27%;float:left;background-color:#fff;color:#454545;overflow:hidden;text-align:center;}
.fc-content-week{background-color:#fff;color:#454545;overflow:hidden;text-align:center;}
.fc-content-lnk{cursor:pointer;}
.fc-content-more{width:25%;position:relative;display:block;float:left;font-size:13px;text-align:right;}
.fc-more-area{position:absolute;display:none;}
.fc-event:hover{color:#454545;}
.fc-popover div.fc-body{height:200px;overflow-y:auto;}
.fc-help-cat{background-color:#fff;color:#454545;max-width:60px;float:left;margin-right:10px;min-width:60px;text-align:center;cursor:pointer;}
</style>
<%
// 요일
String[] dayNms = new String[]{"sun","mon","tue","wed","thu","fri","sat"};
request.setAttribute("dayNms", dayNms);
// 카테고리 색상
String[] catBgs = new String[]{"#5882fa","#fa8258","#a9f5e1","#8a0808","#0100FF","#58fa58","#da81f5","#b18904","#3b0b2e"};
request.setAttribute("catBgs", catBgs);
%>
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
<!--<%// 1명의 사용자 선택 %>
function openSingUser(){
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	if($('#schUserUid').val() != '') data.push({userUid:$('#schUserUid').val()});
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$('#schUserUid').val(userVo.userUid);
			$('#schUserNm').val(userVo.rescNm);
		}
	});
};
<% // 일정 이동 %>
function selectMoveDate(date){
	schdlEvent(null, 'moveDay', date);
}

var totalList = null;

<% // 일정 전체 삭제 %>
function removeEvents(){
	removeEvtOnclick('calendar'); // onclick 이벤트 삭제
	removeEvtTooltip('calendar'); // tooltip 이벤트 삭제
	$('#calendar').find('td.fc-event-container div').remove(); // 일정 삭제
	$('#calendar').find('td.fc-event-container').attr('data-date',''); // 속성 삭제
}

<% // onclick 이벤트 삭제 %>
function removeEvtOnclick(areaId){
	$('#'+areaId+' div.fc-content-${viewTyp}, div.fc-row').off('click');
}

<% // tooltip 이벤트 삭제 %>
function removeEvtTooltip(areaId){
	$('#'+areaId+' div.fc-content-${viewTyp}').off('mouseover, mouseout');	
}

<% // 리로드 %>
function reloadCalendar(){
	dialog.closeAll(); // 팝업 닫기
	removeEvents(); // 전체 초기화
	totalList=null;
	loadEvents('${start}', '${end}'); // 일정 조회
}
<% // 카테고리 클릭%>
function selectSchdlTypList(cd){
	if(totalList==null) return;
	removeEvents();
	if(cd==null) {
		setScheduleList(totalList);	
		return;
	}
	var addEvents =[];
	$.each(totalList, function(index, obj) {
		if(obj.schdlTypCd==cd) addEvents.push(obj);
	});
	setScheduleList(addEvents);
}

<% // 타이틀 업데이트 %>
function updateSchdlTitle(){
	$('#todayTd').text('${viewTyp}'=='month' ? moment('${strtDt}').format('YYYY.MM') : moment('${start}').format('MM.DD') + '~' + moment('${end}').format('MM.DD'));
}

<% // 날짜 세팅 %>
function getAddDt(intervalUnit, strtDt, val){
	var interval=intervalUnit=='month' ? intervalUnit : 'days';
	var duration=intervalUnit=='week' ? 7 : 1;
	if(val=='p') strtDt = moment(strtDt).clone().startOf(intervalUnit).add(duration, interval).format('YYYY-MM-DD');
	else if(val=='m') strtDt = moment(strtDt).clone().startOf(intervalUnit).subtract(duration, interval).format('YYYY-MM-DD');
	return strtDt;
	
}
<% // 탭|네비 버튼 클릭 %>
function schdlEvent(actKey, val, sDate){
	var $form = $('#searchForm');
	var url = "./listCalendar.do";
	var strtDt = '${defaultDate}';
	if(actKey=='changeView'){
		$form.find('input[name="viewTyp"]').val(val);
	}else{
		$form.find('input[name="viewTyp"]').val('${viewTyp}');
		if(val=='today') strtDt = moment().format('YYYY-MM-DD');
		else if(val=='moveDay') strtDt = sDate;
		else strtDt = getAddDt('${viewTyp}', strtDt, val);
	}
	$form.find("input[name='strtDt']").val(strtDt);
	url+="?"+$form.serialize();
	location.href=url;
}
var isAnnv = false; // 기념일 삽입여부
var colMap = null;
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
	updateSchdlTitle();
}

<% // 일정 삽입 %>
function setScheduleList(events){
	var maxCnt='${viewTyp}'=='month' ? 12 : null;
	var day, start, end, html, container, idx;
	$('td.fc-day-number, td.fc-day-week').each(function(index, dayObj){
		day = moment($(this).attr('data-date')).format('YYYYMMDD');
		container = null;
		idx=1;
		html='';
		$.each(events, function(subIndex, vo) {
			start = moment(vo.start).format('YYYYMMDD');
			end = moment(vo.end).format('YYYYMMDD');
			if(start<=day && end>=day){				
				if(container==null){
					container = $(dayObj).parents('div.fc-week:first').find('div.fc-content-skeleton tbody:first td:eq('+$(dayObj).attr('data-col')+')');
					container.addClass('fc-event-container');
					container.attr('data-date', day);
					container.append('<div class="fc-more-area"></div>');
				}
				html+='<div class="fc-day-grid-event fc-event fc-content fc-content-${viewTyp}'+(maxCnt!=null && idx>=maxCnt ? ' fc-limited' : '')+'" data-subj="'+vo.subj+'" data-schdlId="'+vo.schdlId+'" data-typNm="'+vo.schdlTypNm+'" data-cont="'+vo.cont+'">';
				html+='<span class="fc-title">'+vo.subj+('${viewTyp}'=='week' ? '['+vo.schdlTypNm+']' : '')+'</span>';
				html+='</div>';
				idx++;
			}
		});
		if(html!='') $(container).append(html);
	});	
	
	// more 버튼
	setMoreBtn();
	
	// onclick 이벤트 등록
	setEvtOnclick('calendar');
	
	// tooltip 이벤트 등록
	setEvtTooltip('calendar');
	
	// 화면 클릭시 팝업 닫기
	$(document).click(function(event){setEvtMoreClose();	});
	// 윈도우 리사이즈시 팝업 닫기	
	$(window).resize(function(){setEvtMoreClose();});
}

<% // more 버튼 상세 %>
function addMoreBtn(obj){
	var moreCnt=$(obj).find('div.fc-limited').length;
	if(moreCnt==null || moreCnt==undefined || moreCnt==0) return;
	$(obj).append('<div class="fc-content-more fc-content fc-day-grid-event"><a class="fc-more">+'+moreCnt+' 개</a></div>');
}

<% // more 세팅 %>
function setMoreBtn(areaId){
	var $area=areaId==undefined || areaId==null ? $('#calendar').find('td.fc-event-container') : $(areaId);
	$area.each(function(){
		addMoreBtn($(this));
	});
	
	$area.find('a.fc-more').on('click', function(event){
		viewMorePop($(this));
		event.stopPropagation();
	});
}

<% // onclick 이벤트 등록 %>
function setEvtOnclick(areaId){
	if('${writeAuth}' != 'Y') return;
	$('#'+areaId+' div.fc-content-${viewTyp}').on('click', function(event){
		setSchdlPop($(this).attr('data-schdlId'));
		event.stopPropagation();
	});
	$('#'+areaId+' div.fc-row').on('click', function(event){
		if(event.target.nodeName.toLowerCase()!='td') return false;
		$('#'+areaId+' td.fc-past').removeClass('fc-highlight');
		var $td=$(event.target).parents('div.fc-row:first').find('div.fc-bg:first tbody:first td').eq(event.target.cellIndex);
		$td.not('.fc-today').addClass('fc-highlight');
		setSchdlPop(null, $td.attr('data-date').replace('-',''));
		event.stopPropagation();
	});
}

<% // tooltip 이벤트 등록 %>
function setEvtTooltip(areaId){
	$('#'+areaId+' div.fc-content-${viewTyp}').on('mouseover', function(event){
		tooltipCreate($(this), event);
	});
	
	$('#'+areaId+' div.fc-content-${viewTyp}').on('mouseout', function(event){
		$('div.fc-tooltip').hide();
	});
}

<% // more 창 닫기 이벤트 등록 %>
function setEvtMoreClose(){
	if(!$(event.target).closest('div.fc-more-popover').length &&
       !$(event.target).is('div.fc-more-popover')) {
        if($('div.fc-more-popover').is(":visible")) {
        	morePopClose();
        }
    }
}

<% // more 창 닫기 %>
function morePopClose(){
	$('#calendar div.fc-more-popover').hide();
}

<% // 팝업 위치 설정 %>
function setMorePosition(morePop, obj, container){
	var topEl=$(obj).parents('div.fc-row:first');
	var leftEl = $(container).find('div.fc-content:first');
	morePop.css('top', topEl.position().top+'px');
	morePop.css('left', leftEl.position().left+'px');
}

<% // more 창 표시 %>
function viewMorePop(obj){
	var container=$(obj).parents('td.fc-event-container:first');
	var date = $(container).attr('data-date');
	var popId=date+'_more';
	
	morePopClose();
	
	if($('#'+popId).html()!=undefined){
		setMorePosition($('#'+popId), obj, container);
		$('#'+popId).show();
		return;
	}
	moment.locale('${_lang}'=='zh' ? 'zh-cn' : '${_lang}');
	var title = moment(date).format('LL');
	var moreList=$(container).find('div.fc-content-${viewTyp}');
	html='<div class="fc-popover fc-more-popover" id="'+popId+'">';
	html+='<div class="fc-header fc-widget-header">';
	html+='<span class="fc-close fc-icon fc-icon-x" onclick="morePopClose();"></span>';
	html+='<span class="fc-title">'+title+'</span>';
	html+='<div class="fc-clear"></div>';
	html+='</div>';
	html+='<div class="fc-body fc-widget-content">';
	html+='<div class="fc-event-container">';
	$.each(moreList, function() {
		html+=$(this)[0].outerHTML;
	});
	html+='</div>';
	html+='</div>';
	html+='</div>';
	
	var appGrid=$('#calendar div.fc-day-grid:first');
	$(appGrid).append(html);
	
	var $area=$('#'+popId);
	$area.find('div.fc-limited').removeClass('fc-limited');
	
	setMorePosition($area, obj, container);
	
	// more 창 생성시 tooltip 이벤트 등록
	setEvtTooltip(popId);
	
	// onclick 이벤트 등록
	setEvtOnclick(popId);
}

<% // 일정조회 %>
function loadEvents(start, end){
	var param = new ParamMap().getData("searchForm");
	param.put('start', start);
	param.put('end', end);
	param.put('natCd', '${param.natCd}');
	param.put('schdlTypCd', '${param.schdlTypCd}');
	callAjax('./listSchdlAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {			
			var events = [];
			var wcSchdlBVoList = data.wcSchdlBVoList, vo;
			$.each(wcSchdlBVoList, function(index, obj) {
				vo = obj.map;
				events.push({
					schdlId: vo.schdlId,
                    title: vo.subj,
                    start: moment(vo.strtDt).clone(),
                    end: moment(vo.endDt).clone(),
                    allDay: vo.alldayYn,
                    subj: vo.subj,
                    schdlTypNm: vo.schdlTypNm,
                    schdlTypCd: vo.schdlTypCd,
                    cont: vo.cont
                });
        	});
			
			// 기념일
			setAnnvList(data.annvList);
			
			// 일정
			setScheduleList(events);
			if(totalList==null) totalList=events;
		}
	});
}

<% // tooltip 생성 %>
function tooltipCreate(obj, event){
	var evtObj=$(obj);
	var schdlId=evtObj.attr('data-schdlId');
	var areaId = '#tooltip_'+schdlId;
	var title = evtObj.attr('data-subj');
	title+='['+evtObj.attr('data-typNm')+']';
	var cont=evtObj.attr('data-cont');
	if($(areaId).html()==undefined){
		var html='<div id="tooltip_'+schdlId+'" class="fc-tooltip"><div id="tooltip" class="tooltip">';
		html+='<div class="tooltip_body"><div class="tooltip_text" ><ul>';
		html+='<li><div class="ellipsis" style="width:200px"><strong id="title">'+title+'</strong></div></li>';
		html+='<li class="blank_s2" ></li><li class="tooltip_line"></li>';
		html+='<li class="blank_s5"></li><li>';
		html+='<span id="content">'+cont+'</span>';		
		//html+='<span id="tooltip_content"></span>';
		html+='</li></ul></div>';		
		html+='</div></div>';
		$('body').append(html);
	}else{
		var id = null;
		$(areaId).find('span, strong').each(function(){
			id = $(this).attr('id');
			if(id=='title') $(this).text(title);
			else if(id=='content') $(this).text(cont);
		});
	}
	var maxWidth = $(document).width();
	if(((event.pageX-20)+210)>maxWidth) $(areaId).css('left', maxWidth-230);
	else $(areaId).css('left', event.pageX - 20);
	$(areaId).css('top', event.pageY + 10);
    $(areaId).show();
}

<% // 등록 팝업 %>
function setSchdlPop(schdlId){
	//쓰기 권한 체크
	if('${writeAuth}' != 'Y') return;
	var url = "./setSchdlPop.do?menuId=${menuId}";
	var popTitle = '<u:msg titleId="wc.btn.schdlReg" alt="일정등록" />';
	if(schdlId != null) {
		url+= "&schdlId="+schdlId;
		popTitle = '<u:msg titleId="wc.btn.schdlMod" alt="일정수정" />';
	}
	if(arguments.length > 1){
		url+= "&strtDt="+arguments[1];
	}else if(arguments.length==0){
		url+= "&strtDt="+moment().format('YYYYMMDD');
	}
	dialog.open('setSchdlPop',popTitle,url);	
};

$(document).ready(function() {
	loadEvents('${start}', '${end}'); // 일정 로드
	setUniformCSS();	
	
	var wrapper=$('div.homewrapper');
	var classList=wrapper.attr('class');
	$.each(classList.split(' '), function(index, name){
		if(name.indexOf('print')>-1) wrapper.removeClass(name);
	});
	wrapper.addClass('printAp10');
});
//-->
</script>

<u:title titleId="wc.jsp.listPsnSchdl.my.title" alt="개인 일정" menuNameFirst="true" notPrint="true"/>
				
<% // TAB %>
<div id="tabDiv" class="notPrint">
	<u:tabGroup id="schdlTab" >
	<u:tab id="schdlTab" areaId="molyList" titleId="wc.jsp.listPsnSchdl.tab.molySchdl" alt="월간일정" onclick="schdlEvent( 'changeView', 'month' );" on="${viewTyp eq 'month' }"/>
	<u:tab id="schdlTab" areaId="welyList" titleId="wc.jsp.listPsnSchdl.tab.welySchdl" alt="주간일정" onclick="schdlEvent( 'changeView', 'week' );" on="${viewTyp eq 'week' }"/>
	<u:tabButton titleId="cm.btn.reg" alt="등록" href="javascript:setSchdlPop();" auth="A"/>
	<u:tabButton titleId="cm.btn.print" alt="인쇄" img="ico_print.png" imgW="14" onclick="printWeb();" />
	</u:tabGroup>
</div>

<% // 상단 FRONT %>
<div class="front">
	<div class="front_left">
		<form id="searchForm" >
			<u:input type="hidden" name="menuId"  value="${menuId}"/>
			<u:input type="hidden" id="viewTyp" value="${viewTyp}"/><!-- 탭 -->
			<u:input type="hidden" id="strtDt" value="${strtDt}"/><!-- 시작일자 -->
			<table border="0" cellpadding="0" cellspacing="0">
			<tr>				
				<td id="todayTd" class="scd_head fc-title-header" style="margin:0px;padding:5px 0 0 2px;">&nbsp;</td>
				<td style="width:9px;">&nbsp;</td>
				<td class="frontico notPrint"><a href="javascript:schdlEvent(null, 'm');"><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
				<td class="frontico notPrint"><a href="javascript:schdlEvent(null, 'p');"><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
				<td class="frontico notPrint"><u:buttonS href="javascript:schdlEvent(null, 'today');" titleId="wc.btn.today" alt="오늘" auth="W" /></td>
				<%-- <td class="width20"></td>
				<td class="frontinput notPrint">
					<select name="schdlTypCd" onchange="$('#searchForm')[0].submit();">
					<u:option value="" titleId="cm.option.all" alt="전체" selected="${empty param.schdlTypCd }"/>
					<c:forEach var="list" items="${wcCatClsBVoList }" varStatus="status">
					<u:option value="${list.cd }" title="${list.rescNm }" alt="${list.rescNm }" checkValue="${param.schdlTypCd}"/>
					</c:forEach>
					</select>
				</td> --%>
				<td class="width20"></td>
				<td class="frontinput notPrint">
					<table id="schTblContainer" border="0" cellpadding="0" cellspacing="0">
						<tbody>
						<tr>
							<td>
								<u:input type="hidden" id="schUserUid" value="${param.schUserUid}"/>
								<u:input id="schUserNm" titleId="cols.user" readonly="Y"  value="${param.schUserNm }"/>
							</td>
							<td><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="openSingUser();" /></td>
						</tr>
						</tbody>
					</table>
				</td>
				<td class="notPrint">
				<u:buttonS href="javascript:$('#searchForm')[0].submit();" titleId="cm.btn.read" alt="사용자 조회" />
				<u:buttonS href="javascript:;" onclick="valueReset('schTblContainer', null);" titleId="cm.btn.del" alt="삭제" /></td>
			</tr>
			</table>
		</form>
	</div>
	<div class="front_right notPrint" >
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td id="selectDtCalBtn">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<!-- 조회,수정 하기 위해서 schdlId 히든값셋팅 -->
				<td class="frontbtn" >
					<div id="selectDtCalArea" style="display: none; position: absolute;"></div>
					<u:buttonS href="javascript:calendar.open('selectDt',null,function(date, option){selectMoveDate(date);return true;})" titleId="wc.btn.dtMove" alt="날짜이동"   />
				</td>
			</tr>
		</table>
	</div>
</div>

<!-- 달력 -->
<div id="calendar" class="fc fc-ltr fc-unthemed">
	<div class="fc-toolbar">
		<div class="fc-left">
		</div>
		<div class="fc-right">
		</div>
		<div class="fc-center">
		</div>
		<div class="fc-clear">
		</div>
	</div>
	<div class="fc-view-container" style="">
		<c:if test="${viewTyp eq 'month' }">
		<div class="fc-view fc-month-view fc-basic-view">
			<table>
				<thead class="fc-head">
				<tr>
					<td class="fc-head-container fc-widget-header">
						<div class="fc-row fc-widget-header">
							<table>
								<thead>
								<tr>
									<c:forEach var="dayNo" begin="0" end="6" step="1">
									<th class="fc-day-header fc-widget-header fc-${dayNms[dayNo] }"><u:msg var="dayNm" titleId="wc.cols.${dayNms[dayNo] }" 
									/><strong class="fc-day-txt" title="${dayNm }">${dayNm }</strong>
									</th>
									</c:forEach>
								</tr>
								</thead>
							</table>
						</div>
					</td>
				</tr>
				</thead>
				<tbody class="fc-body">
				<tr>
					<td class="fc-widget-content">
						<div class="fc-day-grid-container">
							<div class="fc-day-grid">
								<c:forEach var="list" items="${wrMonthVo.wrWeekVo}" varStatus="status">
									<div class="fc-row fc-week fc-widget-content fc-rigid" style="height: 102px;">
										<div class="fc-bg">
										<table>
											<tbody>
											<tr>
												<c:forEach var="subList" items="${list.wrDayVo}" varStatus="subStatus"
												><td class="fc-day fc-widget-content fc-${dayNms[subStatus.index] } ${subList.todayYn eq 'Y' ? 'fc-today' : 'fc-past' }" data-date="${subList.days}" data-col="${subStatus.index }"
												></td></c:forEach>
											</tr>
											</tbody>
										</table>
									</div>
										<div class="fc-content-skeleton">
											<table>
												<thead>
												<tr>
													<c:forEach var="subList" items="${list.wrDayVo}" varStatus="subStatus"
													><td class="fc-day-number fc-${dayNms[subStatus.index] }${fn:endsWith(subList.isHoliDay,'prev') ? ' fc-other-month' : ''} ${subList.todayYn eq 'Y' ? 'fc-today' : 'fc-past' }" data-date="${subList.days}" data-col="${subStatus.index }"
													><strong class="fc-day-txt">${subList.day}</strong></td></c:forEach>
												</tr>
												</thead>
												<tbody>
												<tr>
													<c:forEach var="subList" items="${list.wrDayVo}" varStatus="subStatus"
													><td></td></c:forEach>
												</tr>
												</tbody>
											</table>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
		</c:if>
		<c:if test="${viewTyp eq 'week' }">
		<div class="fc-view fc-agendaWeek-view fc-agenda-view">
			<table>
				<thead class="fc-head">
				<tr>
					<td class="fc-head-container fc-widget-header">
						<div class="fc-row fc-widget-header" style="border-right-width: 1px; margin-right: 16px;">
							<table>
								<thead>
								<tr>
									<c:forEach var="subList" items="${wrWeekVo.wrDayVo}" varStatus="subStatus">
									<th class="fc-day-header fc-widget-header fc-${dayNms[subStatus.index] }" data-date="${fn:replace(subList.days,'-','')}"><u:msg var="dayNm" titleId="wc.cols.${dayNms[subStatus.index] }" 
									/><strong class="fc-day-txt" title="${dayNm }">${subList.day} ${dayNm }</strong>
									</th>
									</c:forEach>
								</tr>
								</thead>
							</table>
						</div>
					</td>
				</tr>
				</thead>
				<tbody class="fc-body">
				<tr>
					<td class="fc-widget-content">
						<div class="fc-day-grid fc-scroller" style="height:600px;">
							<div class="fc-row fc-week fc-widget-content" style="border-right-width: 1px; margin-right: 16px;min-height:100%;">
								<div class="fc-bg">
									<table>
										<tbody>
										<tr>
											<c:forEach var="subList" items="${wrWeekVo.wrDayVo}" varStatus="subStatus">											
											<td class="fc-day-week fc-day fc-widget-content fc-${dayNms[subStatus.index] } ${subList.todayYn eq 'Y' ? 'fc-today' : 'fc-past' }" data-date="${fn:replace(subList.days,'-','')}" data-col="${subStatus.index }"></td>
											</c:forEach>
										</tr>
										</tbody>
									</table>
								</div>
								<div class="fc-content-skeleton"><table>	<tbody><tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr></tbody></table></div>
							</div>
						</div>				
					</td>
				</tr>
				</tbody>
			</table>
		</div>
		</c:if>
	</div>
</div>
<u:blank />
<!-- style="border-color:${catBgs[status.index]}"  -->
<c:if test="${empty param.schdlTypCd }">
<ul class="notPrint" style="list-style:none;padding:3px;">
<li class="fc-event fc-help-cat" style="border-color:#000000;" onclick="selectSchdlTypList(null);"><u:msg titleId="cm.option.all" alt="전체"/></li>
<c:forEach var="list" items="${wcCatClsBVoList }" varStatus="status">
<li class="fc-event fc-help-cat" onclick="selectSchdlTypList('${list.cd}');">${list.rescNm }</li>
</c:forEach>
</ul>
<u:blank />
</c:if>
<% // 하단 FRONT %>
<div class="front notPrint">
	<div class="front_left">
	</div>
</div>
