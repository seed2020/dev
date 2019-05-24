<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:authUrl var="listUrl" url="/wc/pub/listCalendar.do" authCheckUrl="/wc/pub/listCalendar.do"/><!-- list page 호출관련 url 조합(menuId추가) -->
<c:if test="${empty strtDt }"><jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="defaultDate" /></c:if>
<c:if test="${!empty strtDt }"><c:set var="defaultDate" value="${strtDt }"/></c:if>
<c:set var="viewTyp" value="month"/>
<link rel="stylesheet" href="${_cxPth}/css/calendar/fullcalendar.css" type="text/css" />
<link rel="stylesheet" href="${_cxPth}/css/calendar/fullcalendar.print.css" type="text/css" media="print"/>
<style type="text/css">
.fc-limited{display:none;}
.fc-content{float:left;color:#fff;overflow:hidden;width:44%;background-color:#DF7401;border-color:#DF7401;white-space:nowrap;}
.fc-more-area{position:absolute;display:none;}
.fc-event:hover{color:#454545;}
div.fc-body{height:70px;overflow-y:auto;}
.fc-more-popover{width:180px;}
.fc-more-popover .fc-event-container{padding:5px;}
.fc-event, .fc-event:hover{color:#fff;}
</style>
<script type="text/javascript" src="${_cxPth}/js/calendar/moment.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar/locales.min.js" charset="UTF-8"></script>
<script type="text/javascript">
<!--
<% // 목록으로 이동 %>
function listSchdl(year,month,day){	
	var authUrl = '${listUrl}';
	var prefix = authUrl.indexOf('?') > -1 ? "&" : "?";
	authUrl+=prefix+'viewTyp=${viewTyp}&strtDt='+moment('${defaultDate}').format('YYYY-MM-DD');
	top.location.href=authUrl;		
};
<% // 타이틀 업데이트 %>
function updateSchdlTitle(){
	$('#todayTd').text(moment('${strtDt}').format('YYYY-MM'));
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
	var url = "./listErpSchdlPlt.do";
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
<% // 팝업 위치 설정 %>
function setMorePosition(morePop, container, event){
	var maxWidth = $(document).width();
	var maxHeight = $(document).height();
	var top=$(container).position().top;
	var left=$(container).position().left;
	if(top+96>=maxHeight) top=top-70;
	else top=top+22;
	if(left+190>maxWidth) left=maxWidth-190;
	morePop.css('top', top+'px');
	morePop.css('left', left+'px');
}

<% // more 창 닫기 이벤트 등록 %>
function setEvtMoreClose(event){
	if(!$(event.target).closest('div.fc-more-popover').length &&
       !$(event.target).is('div.fc-more-popover')) {
        if($('div.fc-more-popover').is(":visible")) {
        	morePopClose();
        }
    }
}
<% // more 창 닫기 %>
function morePopClose(){
	$('div.fc-more-popover').hide();
}
<% // more 창 표시 %>
function viewMorePop(container, evt){
	var date = $(container).attr('data-date');
	var popId=date+'_more';
	
	morePopClose();
	
	if($('#'+popId).html()!=undefined){
		setMorePosition($('#'+popId), container, evt);
		$('#'+popId).show();
		return;
	}
	
	html='<div class="fc-popover fc-more-popover" id="'+popId+'">';
	html+='<div class="fc-body fc-widget-content">';
	html+='<div class="fc-event-container">';
	var moreList=$(container).find('div.fc-limited');
	$.each(moreList, function() {
		html+=$(this)[0].outerHTML;
	});
	html+='</div>';
	html+='</div>';
	html+='</div>';
	
	$('#moreContainer').append(html);
	
	var $area=$('#'+popId);
	$area.find('div.fc-limited').removeClass('fc-limited');
	
	setMorePosition($area, container, evt);
}
<% // more 세팅 %>
function setMoreBtn(areaId){
	$('td.scd_plt_day').on({
		mouseover:function(event){
			viewMorePop($(this), event);
			//event.stopPropagation();
		}
	});
	$('#calendar').on('mouseover', function(event){
		if(!$(event.target).closest('div.fc-more-popover').length &&
	       !$(event.target).is('div.fc-more-popover') && 
	       !$(event.target).closest('td.scd_plt_day').length && !$(event.target).is('td.scd_plt_day')) {
			morePopClose();
	    }
	});
}
<% // 일정 삽입 %>
function setScheduleList(events){
	if(events==null || events.length==0) return;
	moment.locale('${_lang}'=='zh' ? 'zh-cn' : '${_lang}');
	var maxCnt=0; // 최대 갯수
	var day, start, end, html, idx, title;
	$('td.fc-day-number').each(function(index, container){
		day = moment($(this).attr('data-date')).format('YYYYMMDD');
		idx=1;
		html='';
		$.each(events, function(subIndex, vo) {
			if(maxCnt>0 && idx>maxCnt) return false;
			start = moment(vo.start).format('YYYYMMDD');
			end = moment(vo.end).format('YYYYMMDD');
			if(start<=day && end>=day){
				title=!vo.allDay ? moment(vo.start).format('a hh:mm') +' '+vo.subj: vo.subj;
				html+='<div class="fc-day-grid-event fc-event fc-content fc-limited"'+' data-subj="'+title+'" data-schdlId="'+vo.schdlId+'" data-typNm="'+vo.schdlTypNm+'" >';
				html+='<span class="fc-title">'+title+'-'+vo.schdlTypNm+'</span>';
				html+='</div>';
				idx++;
			}
		});
		if(html!=''){
			$(container).append(html);
			$(container).addClass('scd_plt_day');
		}
	});	
	
	// more 버튼
	setMoreBtn();
	
	// 화면 클릭시 팝업 닫기
	$(document).click(function(event){setEvtMoreClose(event);	});
	// 윈도우 리사이즈시 팝업 닫기	
	$(window).resize(function(){setEvtMoreClose(event);});
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
                    start: vo.strtDt,
                    end: vo.endDt,
                    allDay: true,
                    subj: vo.subj,
                    schdlTypNm: vo.schdlTypNm,
                    schdlTypCd: vo.schdlTypCd
                });
        	});
			updateSchdlTitle();
			// 기념일
			//setAnnvList(data.annvList);
			
			// 일정
			setScheduleList(events);
			
		}
	});
}
$(document).ready(function(){
	$(".listtable td div").css('padding-top', '3px');
	loadEvents('${start}', '${end}'); // 일정 로드
});
//-->
</script>
<div id="calendar" class="ptlbody_ct" >

<% // 상단 주,월 이동 %>
<table class="ptltable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	  <td class="line"></td>
	</tr>
	<tr>
	  <td class="head_ct" style="padding:2px 0 2px 0;"><table class="center" border="0" cellpadding="0" cellspacing="0">
		  <tr>
		   <!-- 날짜 및 요일 표기 S -->
		      <td class="frontico"><a href="javascript:" onclick="schdlEvent(null, 'm');"><img src="${_ctx}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
			  <td class="head_ct" id="todayTd" style="font-weight:bold;width:85px;"></td>
			  <td class="frontico"><a href="javascript:" onclick="schdlEvent(null, 'p');"><img src="${_ctx}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
			  <td><u:buttonS href="javascript:;" titleId="ct.cols.today" alt="오늘" onclick="schdlEvent(null, 'today');"/></td>
		
			<!-- 날짜 및 요일 표기  E -->			  
		  </tr>
		  </table></td>
	</tr>
	<tr>
	  <td class="line"></td>
	</tr>
	<tr>
			  <td class="height5"></td>
			</tr>
</table>
	
<div id="moreContainer" class="fc fc-ltr fc-unthemed"></div>		
<!-- 월간 -->
<!-- 높이 300px 을 기준으로 하여 300 보다 높으면 300으로 고정 -->
<u:set var="maxHghtPx" test="${empty param.hghtPx || param.hghtPx > 290 }" value="290" elseValue="${param.hghtPx }"/>
<c:set var="scdHghtPx" value="${(maxHghtPx-80)/6 }"/>
<div style="width:100%;" align="center">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:99%;max-width:300px;">
		<tr>
			<td width="15%" class="scdhead" style="height:20px;"><strong><u:msg titleId="wc.cols.sun" alt="일" /></strong></td>
			<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.mon" alt="월" /></strong></td>
			<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.tue" alt="화" /></strong></td>
			<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.wed" alt="수" /></strong></td>
			<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.thu" alt="목" /></strong></td>
			<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.fri" alt="금" /></strong></td>
			<td width="15%" class="head_ct"><strong><u:msg titleId="wc.cols.sat" alt="토" /></strong></td>
		</tr>
		<c:forEach var="list" items="${wrMonthVo.wrWeekVo}" varStatus="status">
		<tr>
			<c:forEach var="subList" items="${list.wrDayVo}" varStatus="subStatus">
				<td class="fc-event-container fc-day-number${subList.todayYn eq 'Y' ? ' scd_plt_today' : ''}" style="height:${scdHghtPx}px;text-align:center;cursor:pointer;" valign="middle" onclick="listSchdl('${subList.year}','${subList.month}','${subList.day}');" data-date="${subList.days}">
					<div class="${subList.isHoliDay}" ><u:out value="${subList.day}"/></div>
				</td>
			</c:forEach>
		</tr>
		</c:forEach>	
	</table>
</div>

<form name="searchForm" id="searchForm" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="viewTyp" value="${viewTyp }"/><!-- 탭 -->
<u:input type="hidden" id="strtDt" value="${strtDt}"/><!-- 시작일자 -->
<u:input type="hidden" id="hghtPx" value="${param.hghtPx}" />
</form>

</div>
