<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript" src="${_cxPth}/js/moment.min.js" charset="UTF-8"></script>
<c:if test="${empty strtDt }"><jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="defaultDate" /></c:if>
<c:if test="${!empty strtDt }"><c:set var="defaultDate" value="${strtDt }"/></c:if>
<u:set var="viewTyp" test="${empty param.viewTyp }" value="month" elseValue="${param.viewTyp }"/>
<u:set test="${!empty param.tabNo}" var="tabNo" value="${param.tabNo}" elseValue="0" />
<!-- 권한 -->
<u:secu auth="W" ><c:set var="writeAuth" value="Y"/></u:secu>
<style>
td{margin:0; padding:0; border:0; outline:0; font-size:100%; vertical-align:baseline; background:transparent;}
.fc-content-week{background:#5882fa;color:#ffffff;}
.fc-sun{color:#d22681;}
</style>
<%
// 요일
String[] dayNms = new String[]{"sun","mon","tue","wed","thu","fri","sat"};
request.setAttribute("dayNms", dayNms);
%>
<script type="text/javascript">
//<![CDATA[
function selectTab(tabNo){
	var tabArrs = ["month","week"];
	$("#searchForm #tabNo").val(tabNo);
	schdlEvent( 'changeView', tabArrs[tabNo]);
};
function fnCalendar(id,opt){
	$m.dialog.open({
	id:id,
	noPopbody:true,
	cld:true,
	url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&handler=selectMoveDate'+'&calStyle=m',
	});
};
<%// 1명의 사용자 선택 %>
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
	$('#todayTd').text('${viewTyp}'=='month' ? moment('${strtDt}').format('YYYY-MM') : moment('${start}').format('YYYY-MM'));
	//$('#todayTd').text('${viewTyp}'=='month' ? moment('${strtDt}').format('YYYY-MM') : moment('${start}').format('MM.DD') + '~' + moment('${end}').format('MM.DD'));
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
	var url = "/wc/pub/listCalendar.do";
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
	$m.nav.curr(event, url);
}
var isAnnv = false; // 기념일 삽입여부
var colMap = null;
<% // 기념일 삽입 %>
function setAnnvList(annvList){
	if(isAnnv) return;
	var day, strtDay, endDay, html, tgtDiv, titles, tdCls;
	var isMonth='${viewTyp}' == 'month' ? true : false;
	var viewTyp='${viewTyp}';
	$('td.fc-content').each(function(index, parent){
		day = moment($(this).attr('data-date')).format('YYYYMMDD');
		isCreate = true;
		titles = '';
		html='';
		$.each(annvList, function(index, vo) {
			strtDay = moment(vo.schdlStartDt).format('YYYYMMDD');
			endDay = moment(vo.schdlEndDt).format('YYYYMMDD');
			if(strtDay<=day && endDay>=day){
				if(viewTyp=='month' && $(parent).find('div.size').length>0) return true;
				if(vo.holiYn == 'Y'){
					if(viewTyp=='month'){
						tdCls=$(parent).attr('class').replace(/sunday|day|saturday/g,'sunday');
						$(parent).attr('class', tdCls);
					}else{
						tdCls=$(parent).parent().find('td').eq(0).attr('class').replace(/sunday|md|saturday/g,'sunday');
						$(parent).parent().find('td').eq(0).attr('class', tdCls);
					}
				}
				if(viewTyp=='month')
					html+='<div class="holiday'+($(parent).attr('class').indexOf('_g')>-1 ? '_g' :'')+(vo.holiYn == 'Y' ? '_red' : '')+'"><div class="size">';
				else{
					html+='<span';
					if(vo.holiYn == 'Y') html+=' class="fc-sun"';
					html+='>';
				}
				html+=vo.subj;
				if(viewTyp=='month'){
					html+='</div></div>';
					$(parent).append(html);
				}else{
					html+='</span>';
				}
			}
		});
		if(viewTyp=='week' && html!='' ){
			html='<div class="basic_w">'+html+'</div>';
			$(parent).append(html);
		}
	});
	isAnnv = true;
	//updateSchdlTitle();
}

<% // 일정 삽입 %>
function setScheduleList(events){
	var viewTyp='${viewTyp}';
	var maxCnt=viewTyp=='month' ? 4 : null;
	var day, start, end, html, container, idx;
	$('td.fc-content').each(function(index, parent){
		day = moment($(this).attr('data-date')).format('YYYYMMDD');
		container = null;
		idx=1;
		html='';
		$.each(events, function(subIndex, vo) {
			start = moment(vo.start).format('YYYYMMDD');
			end = moment(vo.end).format('YYYYMMDD');
			if(start<=day && end>=day){
				if(viewTyp=='month')
					html+='<div class="schdlItem pbg'+idx+(maxCnt!=null && idx>=maxCnt ? ' fc-limited' : '')+'" data-subj="'+vo.subj+'" data-schdlId="'+vo.schdlId+'" data-typNm="'+vo.schdlTypNm+'" data-cont="'+vo.cont+'">'
				else
					html+='<div class="schdlItem basic_w fc-content-week" data-subj="'+vo.subj+'" data-schdlId="'+vo.schdlId+'" data-typNm="'+vo.schdlTypNm+'" data-cont="'+vo.cont+'">'
				html+=vo.subj;
				if(viewTyp=='week') html+='['+vo.schdlTypNm+']';
				html+='</div>';
				idx++;
			}
		});
		if(html!=''){
			if(viewTyp=='month')
				$(parent).append('<div class="pbgarea">'+html+'</div>');
			else
				$(parent).append(html);
			$(parent).addClass('fc-event-container');
		}else{
			$(parent).addClass('fc-event');
		}
	});	
	
	// more 버튼
	setMoreBtn();
	
	// onclick 이벤트 등록
	setEvtOnclick('calendar');
}

<% // more 세팅 %>
function setMoreBtn(areaId){
	var $area=areaId==undefined || areaId==null ? $('#calendar').find('td.fc-event-container') : $(areaId);
	$area.on('click', function(event){
		listSchdlPop(this, $(this).attr('data-date'));
		event.stopPropagation();
	});
}

<% // onclick 이벤트 등록 %>
function setEvtOnclick(areaId){
	if('${writeAuth}' != 'Y') return;
	$('#'+areaId+' td.fc-event').on('click', function(event){
		setSchdl(null, $(this).attr('data-date').replace('-',''));
		event.stopPropagation();
	});
}

<% // 목록 팝업 %>
function listSchdlPop(obj, date){
	var html = [];
	html.push('	<div class="btnarea">');
	html.push('<div class="size">');
	html.push('	<dl>');
	<c:if test="${writeAuth eq 'Y'}">
	html.push('<dd class="btn" onclick="$m.nav.getWin().setSchdl(null, \''+date+'\');"><u:msg titleId="cm.btn.write" alt="등록" /></dd>');	
	</c:if>
	html.push('			</dl>');
	html.push('		</div>');
	html.push('	</div>');
	html.push('	<div class="unified_listarea_pop2">');
	html.push('		<div class="listarea">');
	html.push('		<article>');
	$(obj).find('div.schdlItem').each(function(){
		html.push('			<div class="listdiv" onclick="$m.nav.getWin().viewSchdl(\''+$(this).attr('data-schdlId')+'\');">');
		html.push('				<div class="list">');
		html.push('				<dl>');
		html.push('				<dd class="tit">'+$(this).attr('data-subj')+'</dd>');
		html.push('				<dd class="body">'+$(this).attr('data-cont')+'</dd>');
		html.push('				</dl>');
		html.push('				</div>');
		html.push('			</div>');
	});
	
	html.push('		</article>');
	html.push('		</div>');
	html.push('	</div>');
	var title = moment(date).clone().format('YYYY-MM-DD');
	$m.dialog.open({
		id:'listSchdlPop',
		title:title,
		html:html.join('\n')
	});
};

<% // 상세보기 %>
function viewSchdl(schdlId){
	$m.dialog.close('listSchdlPop');
	var url = "/wc/pub/viewSchdl.do?menuId=${menuId}&schdlId="+schdlId+'&viewTyp=${param.viewTyp}&strtDt=${defaultDate}';
	$m.nav.next(event, url);
};

function viewSchdlPopTemp(obj){
	var html = [];
	html.push('<article>');
	html.push('<div class="listdiv" onclick="$m.user.viewUserPop(\'U000000F\');">');
	html.push('<div class="list">');
	html.push('<dl>');
	html.push('<dd class="tit">김둘 | 임직원</dd>');
	html.push('<dd class="body">영업관리부 | k02@innogw.com</dd>');
	html.push('</dl>');
	html.push('</div>');
	html.push('</div>');
	html.push('</article>');
	var title = moment(date).clone().format('YYYY-MM-DD');
	$m.dialog.open({
		id:'listSchdlPop',
		title:title,
		html:html.join('\n')
	});

	var url = "/wc/pub/viewSchdl.do?menuId=${menuId}&schdlId="+schdlId;
	$m.nav.next(event, url);
};

<% // 일정조회 %>
function loadEvents(start, end){
	var param = new ParamMap().getData("searchForm");
	param.put('start', start);
	param.put('end', end);
	param.put('natCd', '${param.natCd}');
	param.put('schdlTypCd', '${param.schdlTypCd}');
	$m.ajax('/wc/pub/listSchdlAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {			
			var events = [];
			var wcSchdlBVoList = data.wcSchdlBVoList, vo, start, end;
			$.each(wcSchdlBVoList, function(index, vo) {
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

<% // 등록 %>
function setSchdl(schdlId){
	$m.dialog.close('listSchdlPop');
	//쓰기 권한 체크
	if('${writeAuth}' != 'Y') return;
	var url = "/wc/pub/setSchdl.do?menuId=${menuId}&viewTyp=${viewTyp}";
	if(schdlId != null) {
		url+= "&schdlId="+schdlId;
	}
	if(arguments.length > 1){
		url+= "&strtDt="+moment(arguments[1]).format('YYYY-MM-DD');
	}else if(arguments.length==0){
		url+= "&strtDt="+moment().format('YYYY-MM-DD');
	}
	
	$m.nav.next(event, url);
};
<%// footer 위치를 일정하게 %>
function placeFooter(){
	var scheduleTop,footerHeight,gap;
	$('div.schedule_body_scroll').each(function(){
		scheduleTop = $(this).position().top+5;
		footerHeight = $(this).find('#footer').height()+30, screenHeight = $m.nav.screenSize().h;
		gap = screenHeight - scheduleTop - footerHeight;
		$(this).find('#footerSpace').height(gap).show();
	});	
};

function initSwipe(){
	var lyGap = 100;//기준 터치이동 px
	$('div.schedule_calendar_scroll, div.schedule_body_scroll').each(function(){
		var touchX = 0,touchY = 0;
		$(this).on('touchstart',function(e){
			//touchstart
			var touch = e.originalEvent.changedTouches[0];
			touchX = touch.pageX; //e.touches[0].clientX;
			touchY = touch.pageY; //e.touches[0].clientX;
		});
		$(this).on('touchend',function(e){
			//touchend
			var touch = e.originalEvent.changedTouches[0];
			var moveX = touch.pageX; //e.originalEvent.changedTouches[0];
			
			var gap;
			if(moveX > touchX) gap = moveX - touchX;
			else gap = touchX - moveX;
			if(gap > lyGap){
				if(moveX > touchX){
					//오른쪽
					schdlEvent(null, 'm');
				}else{
					//왼쪽
					schdlEvent(null, 'p');
				}
			}else{//기준 px 이하로 터치 이동이 발생할 경우
				//if($(this).hasClass('schedule_calendar_scroll')) $(this).find('div.schedule_calendar').css('left',0);
				//else $(this).find('div.schedule_body').css('left',0);
				
			}
		});
		$(this).on('touchmove',function(e){
			//touchmove
			var touch = e.originalEvent.changedTouches[0];
			var chX = Math.abs(touchX - touch.pageX);
			var chY = Math.abs(touchY - touch.pageY);
			//좌표가 좌우로 많이 움직였을때만 preventDefault 이벤트 추가
			if(chX > chY){
				isPrevent = true;		
			}else{
				isPrevent = false;
			}
			if(isPrevent){
				e.preventDefault();
			}
		});
	});		
};
$(document).ready(function() {
	loadEvents('${start}', '${end}'); // 일정 로드
	placeFooter();
	
	initSwipe();
});

//]]>
</script>

<form id="searchForm" method="get" >
<input type="hidden" name="menuId"  value="${menuId}"/>
<m:input type="hidden" id="viewTyp" value="${viewTyp}"/><!-- 탭 -->
<m:input type="hidden" id="strtDt" value="${strtDt}"/><!-- 시작일자 -->
<input type="hidden" id="tabNo" name="tabNo"  value="${tabNo}"/>
</form>		
<!--schedule_top S-->
<div class="schedule_top" >
	<div class="scd_top1" >
		<dl>
		<dd class="scd_prev" onclick="schdlEvent(null, 'm');"></dd>
		<dd class="scd_tit1" id="todayTd" ><u:set var="titleDate" test="${viewTyp eq 'month'  }" value="${strtDt}" elseValue="${start}"
		/><fmt:parseDate var="titleDate" value="${titleDate }" pattern="yyyy-MM-dd"/><fmt:formatDate value="${titleDate }" pattern="yyyy-MM" /></dd>
		<dd class="scd_next" onclick="schdlEvent(null, 'p');"></dd>
		</dl>
	</div>
	<div class="scd_today" onclick="schdlEvent(null, 'today');"><u:msg titleId="wc.btn.today" alt="오늘" /></div>
</div>
<!--//schedule_top E-->

<div id="calendar">
<c:if test="${viewTyp eq 'month' }">
<!--schedule_calendar S-->
<div class="schedule_calendar" >
	<table class="scd_calendar">
		<colgroup>
		<col width="14.28%"/>
		<col width="14.28%"/>
		<col width="14.28%"/>
		<col width="14.28%"/>
		<col width="14.28%"/>
		<col width="14.28%"/>
		<col />
		</colgorup>
		<tbody>
		<tr class="scd_cweek">
			<c:forEach var="dayNo" begin="0" end="6" step="1"
			><td class="week${dayNo==0 ? '_sunday' : dayNo==6 ? '_saturday' : '' }"><u:msg var="dayNm" titleId="wc.cols.${dayNms[dayNo] }" 
			/>${dayNm }</td></c:forEach>
		</tr>
		</tbody>
	</table>
</div>
<!--//schedule_calendar E-->
<!--schedule_calendar_scroll S-->
<div class="schedule_calendar_scroll schduleContainer schedulePage molyList" >
	<!--schedule_calendar S-->
	<div class="schedule_calendar" >
		<table class="scd_calendar" >
			<colgroup>
			<col width="14.28%"/>
			<col width="14.28%"/>
			<col width="14.28%"/>
			<col width="14.28%"/>
			<col width="14.28%"/>
			<col width="14.28%"/>
			<col />
			</colgroup>
			<tbody>
			<c:set var="maxRowIndex" value="5"/>
			<c:forEach var="list" items="${wrMonthVo.wrWeekVo}" varStatus="status">
				<tr class="scd_cbody">
					<c:forEach var="subList" items="${list.wrDayVo}" varStatus="subStatus">
						<c:set var="dayClass" value="day"/>
						<c:choose>
							<c:when test="${subList.isHoliDay eq 'scddate_red' }"><c:set var="dayClass" value="${subList.todayYn eq 'Y' ? 'day_today_sun' : 'sunday'}"/></c:when>
							<c:when test="${subList.isHoliDay eq 'scddate_red_prev' }"><c:set var="dayClass" value="sunday_g"/><c:set var="schdlPrevCls" value="_g"/></c:when>
							<c:when test="${subList.isHoliDay eq 'scddate' && dayStatus.index == 6 }"><c:set var="dayClass" value="${subList.todayYn eq 'Y' ? 'day_today_sat' : 'saturday'}"/></c:when>
							<c:when test="${subList.isHoliDay eq 'scddate_prev' && dayStatus.index == 6 }"><c:set var="dayClass" value="saturday_g"/><c:set var="schdlPrevCls" value="_g"/></c:when>
							<c:when test="${subList.isHoliDay eq 'scddate' && dayStatus.index < 6}"><c:set var="dayClass" value="${subList.todayYn eq 'Y' ? 'day_today' : 'day'}"/></c:when>
							<c:when test="${subList.isHoliDay eq 'scddate_prev' }"><c:set var="dayClass" value="day_g"/><c:set var="schdlPrevCls" value="_g"/></c:when>
						</c:choose>
						<td class="fc-content ${dayClass }" data-date="${subList.days}">
							<u:out value="${subList.day}"/>
						</td>
					</c:forEach>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<!--//schedule_calendar E-->
	<!--footer S-->
    <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    <!--//footer E-->
</div>
</c:if>
<c:if test="${viewTyp eq 'week' }">
<!--schedule_body_scroll S-->
<div class="schedule_body_scroll" >      
    <!--schedule_body S-->
    <div class="schedule_body">
    <table class="scd_body">
        <colgroup>
            <col width="63"/>
            <col />
        </colgorup>
        <tbody>
            <tr>
                <td class="scd_line" colspan="2"></td>
            </tr>
            <c:forEach var="subList" items="${wrWeekVo.wrDayVo}" varStatus="subStatus">
            	<tr>
                    <td class="${subStatus.index==0 ? 'scdtit_sunday' : subStatus.index==6 ? 'scdtit_saturday' : 'scdtit_md'}"><u:msg var="dayNm" titleId="wc.cols.${dayNms[subStatus.index] }"
                    />${subList.day} ${dayNm }</td>
                    <td class="fc-content basic" data-date="${fn:replace(subList.days,'-','')}"></td>
                </tr>
            </c:forEach>
            
        </tbody>
    </table>
    </div>
    <!--//schedule_body E-->
    
    <!--footer S-->
    <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    <!--//footer E-->
    
</div>
<!--//schedule_body_scroll E-->
</c:if>
<!--//schedule_calendar_scroll E-->
</div>        
        
        

