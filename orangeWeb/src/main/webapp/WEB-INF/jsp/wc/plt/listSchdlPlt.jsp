<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:authUrl var="viewUrl" url="/wc/viewSchdlPop.do" authCheckUrl="/wc/listNewSchdl.do?fncCal=my"/><!-- view page 호출관련 url 조합(menuId추가) -->
<u:authUrl var="listUrl" url="/wc/listNewSchdl.do?fncCal=my" authCheckUrl="/wc/listNewSchdl.do?fncCal=my"/><!-- list page 호출관련 url 조합(menuId추가) -->
<link rel="stylesheet" href="${_cxPth}/css/calendar/fullcalendar.css" type="text/css" />
<link rel="stylesheet" href="${_cxPth}/css/calendar/fullcalendar.print.css" type="text/css" media="print"/>
<style type="text/css">
.fc-limited{display:none;}
.fc-content{float:left;color:#fff;overflow:hidden;width:95%;background-color:#DF7401;border-color:#DF7401;white-space:nowrap;}
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
//상세보기 팝업
function viewSchdlPop(schdlId) {
	var authUrl = '${viewUrl}';
	var prefix = authUrl.indexOf('?') > -1 ? "&" : "?";
	authUrl+=prefix+"${params}&fncCal=my&plt=Y&schdlId="+schdlId;
	parent.dialog.open('viewSchdlPop','<u:msg titleId="wc.btn.schdlDetail" alt="상세보기" />',authUrl);
};

//이전 다음 버튼
function fnPageMove(value){
	$('#schedulePmValue').val(value);
	$('#schDay').val('');
	searchForm.submit();
};

function listSchdl(year,month,day){	
	var authUrl = '${listUrl}';
	var prefix = authUrl.indexOf('?') > -1 ? "&" : "?";
	authUrl+=prefix+"strtDt="+(year+'-'+(parseInt(month)<10 ? '0'+month : month)+'-'+(parseInt(day)<10 ? '0'+day : day));
	top.location.href=authUrl;		
};

<c:if test="${empty param.listType || param.listType eq 'month' }">
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
				html+='<span class="fc-title">'+title+'</span>';
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

// 일정 로드
function loadEvents(){
	var events = [];
	<c:forEach var="list" items="${wcsList }" varStatus="listStatus">
		events.push({
			schdlId: '${list.schdlId}',
		    title: '<u:out value="${list.subj}" type="value"/>',
		    start: '${list.schdlStartDt}',
		    end: '${list.schdlEndDt}',
		    allDay: '${list.alldayYn}'=='Y',
		    subj: '<u:out value="${list.subj}" type="value"/>',
		    schdlTypNm: '${list.schdlTypNm}',
		    schdlTypCd: '${list.schdlTypCd}'
		});
	</c:forEach>
	// 일정
	setScheduleList(events);
}
</c:if>
$(document).ready(function(){
	$(".listtable td div").css('padding-top', '3px');
	<c:if test="${empty param.listType || param.listType eq 'month' }">loadEvents();</c:if>// 일정 로드
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
		  <c:choose>
			<c:when test="${param.listType eq 'day' }">
				<td class="frontico"><a href="javascript:" onclick="fnPageMove('m');"><img src="${_ctx}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
				<td class="head_ct" style="font-weight:bold;width:85px;">${wrDayVo.days }</td>
				<td class="frontico"><a href="javascript:" onclick="fnPageMove('p');"><img src="${_ctx}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
			</c:when>
			<c:otherwise>
				<td class="frontico"><a href="javascript:" onclick="fnPageMove('m');"><img src="${_ctx}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
				<td class="head_ct" style="font-weight:bold;width:75px;">${fn:substring(wrMonthVo.strtDt,0,7) }</td>
				<td class="frontico"><a href="javascript:" onclick="fnPageMove('p');"><img src="${_ctx}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
			</c:otherwise>
		</c:choose>
		<td><u:buttonS href="javascript:;" titleId="ct.cols.today" alt="오늘" onclick="fnPageMove('t');"/></td>
		
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
	
<c:choose>
	<c:when test="${empty param.listType || param.listType eq 'month' }">
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
	</c:when>
	<c:when test="${param.listType eq 'day' }">
		<table class="ptltable" border="0" cellpadding="0" cellspacing="0">
			<tr>
			  <td class="body_lt"><img src="/images/${_skin}/ico_document.png" /><strong><u:msg titleId="wc.option.psnSchdl" alt="개인일정"/></strong></td>
			</tr>
			<!-- 개인일정 반복 구간 S-->
			<c:forEach var="scds" items="${wcsList}" varStatus="status">
				<c:if test="${scds.schdlTypCd != '5' && scds.schdlKndCd == '1'&& !empty scds.subj}">
				<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
				<u:set var="catFontCss" test="${!empty mapList && !empty mapList.fontColrCd}" value="style='font-size:11px; color:${mapList.bgcolCd }; line-height:16px; padding:0 3px 0 4px;'" elseValue="style='font-size:11px; line-height:16px; padding:0 3px 0 4px;'"/>
				<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="HH:mm"/>
			
				<tr>
				  <td class="body_lt">
					<a href="javascript:viewSchdlPop('${scds.schdlId}');">
				  	<span class="scdArea scd_${scds.schdlTypCd }" ${catFontCss }>				  	
				  		<fmt:formatDate value="${dateStartTempParse}" pattern="HH:mm"/>&nbsp;[<u:out value="${mapList.catNm}"/>]&nbsp;<u:out value="${scds.subj}"/>
				  	</span>
				  	</a>
				  </td>
				</tr>				
				
				</c:if>
			</c:forEach>
			<!-- 개인일정 반복 구간 E-->
			<tr>
			  <td class="height5"></td>
			</tr>
			<tr>
			  <td class="line"></td>
			</tr>
			<tr>
			  <td class="height5"></td>
			</tr>
			<tr>
			  <td class="body_lt"><img src="/images/${_skin}/ico_document.png" /><strong><u:msg titleId="wc.option.grpSchdl" alt="그룹일정"/></strong></td>
			</tr>
			<!-- 그룹일정 반복 구간 S-->
			<c:forEach var="scds" items="${wcsList}" varStatus="status">
				<c:if test="${scds.schdlTypCd != '5' && scds.schdlKndCd == '2'&& !empty scds.subj}">
				<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
				<u:set var="catFontCss" test="${!empty mapList && !empty mapList.fontColrCd}" value="style='font-size:11px; color:${mapList.bgcolCd }; line-height:16px; padding:0 3px 0 4px;'" elseValue="style='font-size:11px; line-height:16px; padding:0 3px 0 4px;'"/>
				<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="HH:mm"/>
			
				<tr>
				  <td class="body_lt">
					<a href="javascript:viewSchdlPop('${scds.schdlId}');">
				  	<span class="scdArea scd_${scds.schdlTypCd }" ${catFontCss }>				  	
				  		<fmt:formatDate value="${dateStartTempParse}" pattern="HH:mm"/>&nbsp;[<u:out value="${mapList.catNm}"/>]&nbsp;<u:out value="${scds.subj}"/>
				  	</span>
				  	</a>
				  </td>
				</tr>				
				
				</c:if>
			</c:forEach>
			<!-- 그룹일정 반복 구간 E-->
			<tr>
			  <td class="height5"></td>
			</tr>
			<tr>
			  <td class="line"></td>
			</tr>
			<tr>
			  <td class="height5"></td>
			</tr>
			<tr>
			  <td class="body_lt"><img src="/images/${_skin}/ico_document.png" /><strong><u:msg titleId="wc.option.deptSchdl" alt="부서일정"/></strong></td>
			</tr>
			<!-- 부서일정 반복 구간 S-->
			<c:forEach var="scds" items="${wcsList}" varStatus="status">
				<c:if test="${scds.schdlTypCd != '5' && scds.schdlKndCd == '3'&& !empty scds.subj}">
				<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
				<u:set var="catFontCss" test="${!empty mapList && !empty mapList.fontColrCd}" value="style='font-size:11px; color:${mapList.bgcolCd }; line-height:16px; padding:0 3px 0 4px;'" elseValue="style='font-size:11px; line-height:16px; padding:0 3px 0 4px;'"/>
				<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="HH:mm"/>
			
				<tr>
				  <td class="body_lt">
					<a href="javascript:viewSchdlPop('${scds.schdlId}');">
				  	<span class="scdArea scd_${scds.schdlTypCd }" ${catFontCss }>				  	
				  		<fmt:formatDate value="${dateStartTempParse}" pattern="HH:mm"/>&nbsp;[<u:out value="${mapList.catNm}"/>]&nbsp;<u:out value="${scds.subj}"/>
				  	</span>
				  	</a>
				  </td>
				</tr>				
				
				</c:if>
			</c:forEach>
			<!-- 부서일정 반복 구간 E-->
			
			<tr>
			  <td class="height5"></td>
			</tr>
			<tr>
			  <td class="line"></td>
			</tr>
			<tr>
			  <td class="height5"></td>
			</tr>
	
			<tr>
			  <td class="body_lt"><img src="/images/${_skin}/ico_document.png" /><strong><u:msg titleId="wc.option.compSchdl" alt="회사일정"/></strong></td>
			</tr>
			<!-- 회사 일정 반복 구간 S -->
			<c:forEach var="scds" items="${wcsList}" varStatus="status">
				<c:if test="${scds.schdlTypCd != '5' && scds.schdlKndCd == '4'&& !empty scds.subj}">
				<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
				<u:set var="catFontCss" test="${!empty mapList && !empty mapList.fontColrCd}" value="style='font-size:11px; color:${mapList.bgcolCd }; line-height:16px; padding:0 3px 0 4px;'" elseValue="style='font-size:11px; line-height:16px; padding:0 3px 0 4px;'"/>
				<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="HH:mm"/>
				<tr>
				  <td class="body_lt">
					<a href="javascript:viewSchdlPop('${scds.schdlId}');">
				  	<span class="scdArea scd_${scds.schdlTypCd }" ${catFontCss }>				  	
				  		<fmt:formatDate value="${dateStartTempParse}" pattern="HH:mm"/>&nbsp;[<u:out value="${mapList.catNm}"/>]&nbsp;<u:out value="${scds.subj}"/>
				  	</span>
				  	</a>
				  </td>
				</tr>				
				
				</c:if>
			</c:forEach>
			<!-- 회사 일정 반복 구간 E -->
		</table>
	</c:when>
	
</c:choose>

<form name="searchForm" action="./listSchdlPlt.do" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="schedulePmValue" />
<c:choose>
	<c:when test="${param.listType eq 'day' }"><u:input type="hidden" id="startDay" value="${wrDayVo.days}"/></c:when>
	<c:otherwise><u:input type="hidden" id="startDay" value="${wrMonthVo.strtDt}"/></c:otherwise>
</c:choose>
<u:input type="hidden" id="listType" value="${param.listType }"/>
<u:input type="hidden" id="hghtPx" value="${param.hghtPx}" />
</form>

</div>
