<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:secu auth="W" ><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<u:set test="${param.rescKndId != null}" var="rescKndId" value="${param.rescKndId}" elseValue="" />
<u:set test="${param.rescMngId != null}" var="rescMngId" value="${param.rescMngId}" elseValue="" />
<script type="text/javascript">
//<![CDATA[
//이전 다음 버튼
function fnPageMove(value){
	$('#schedulePmValue').val(value);
	$('#schDay').val('');
	$m.nav.curr(event, '/wr/${listPage }.do?'+$('#searchForm').serialize());
};

function searchList(event){
	$m.nav.curr(event, '/wr/${listPage }.do?'+$('#searchForm').serialize());
}

//상세보기
function viewRezvPop(rezvId) {
	$m.nav.next(event, '/wr/viewRezv.do?${params}&listPage=${listPage}&rezvId='+rezvId);
};

function fnSetRescKndId(cd)
{
	$('#rescKndId').val(cd);	
	$('.listselect .select1 span').text($(".open1 dd[data-rescKndId='"+cd+"']").text());
	$('.open1').hide();

	$(".open2 dl dd").each(function(){
	$(this).remove();
	});

	<c:if test="${listPage ne 'listRezvStat' || ( listPage eq 'listRezvStat' && !empty param.listType && param.listType ne 'week') }">$(".open2 dl").append("<dd class='txt' onclick='fnSetRescMngId(\"\");' data-rescMngId=''><u:msg titleId="cm.option.all" alt="전체선택"/></dd><dd class='line'></dd>");</c:if>	
	if(cd != ''){
		$m.ajax('/wr/selectRescAjx.do?menuId=${menuId}', {rescKndId:$('#rescKndId').val()}, function(data){
		$.each(data.list , function(index, vo) {
		var obj = JSON.parse(JSON.stringify(vo));
		$(".open2 dl").append("<dd class='txt' onclick='fnSetRescMngId(\""+obj.rescMngId+"\");' data-rescMngId='"+obj.rescMngId+"'>"+obj.rescNm+"</dd><dd class='line'></dd>");
		});
		});
		$('.listselect .select2 span').text($(".open2 dd:first").text());	
		$('#rescMngId').val($(".open2 dd:first").attr('data-rescMngId'));
	}
}

function fnSetRescMngId(cd)
{
	$('#rescMngId').val(cd);
	$('.listselect .select2 span').text($(".open2 dd[data-rescMngId='"+cd+"']").text());
	$('.open2').hide();
}

function initSwipe(){
	var lyGap = 100;//기준 터치이동 px
	$('div.bookbody_scroll').each(function(){
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
					fnPageMove('m');
				}else{
					fnPageMove('p');					
				}
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

var holdHide = false, holdHide2 = false;
$(document).ready(function() {
	//fnSetRescKndId('${rescKndId}');
	//fnSetRescMngId('${rescMngId}');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".open1").hide();
		if(holdHide2) holdHide2 = false;
		else $(".open2").hide();	
	});
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
	<c:if test="${listPage eq 'listRezvStat'}">	initSwipe();</c:if>
});

<% // [상단버튼:등록] 등록 %>
function setRezv(rezvId , obj){
	<c:if test="${listPage ne 'listRezvDisc' && writeAuth == 'Y'}">
	var url = '/wr/setRezv.do?${params}&listPage=${listPage}';

	if(rezvId != null){
		url+= '&rezvId='+rezvId;
	}

	if(obj != null){
		var strtYmd = $(obj).attr('data-day');
		var strtTime = $(obj).attr('data-time');
		url+= "&rezvStrtDt="+strtYmd+" "+strtTime;
	}

	$m.nav.next(event, url);
	</c:if>
};

//마우스 오버시 css 변경
function fnRezvMouseOver(areaId , type , obj){
	$('.bk_body').removeClass('open');

	if(areaId == '' && type == 'open'){
		$(obj).addClass('open');
		return;
	}
	if(type == 'open'){
		$('.'+areaId).addClass('open');
	}
};

//]]>
</script>


<!--section S-->
<section>

<form name="searchForm" id="searchForm" action="/wr/${listPage }.do" >
<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
<input type="hidden" id="schedulePmValue"  name="schedulePmValue"/>
<input type="hidden" id="startDay" name="startDay" value="${wrWeekVo.strtDt}"/>
<input type="hidden" id="fncMy"  name="fncMy" value="${param.fncMy }"/>
<input type="hidden" id="listType" name="listType" value="${param.listType }"/>
<input type="hidden" id="bgcolYn" name="bgcolYn" value="${param.bgcolYn }"/>
<input type="hidden" id="schDay" name="schDay" value="${param.schDay}" />

<input type="hidden" name="rescKndId" id="rescKndId" value="" />
<input type="hidden" name="rescMngId" id="rescMngId" value="" />

<!--booktop_area S-->
<div class="booktop_area">

	<div class="listsearch">
		<div class="listselect">
			<c:set var="rescKndNm"/>
			<div class="open1" style="display:none;">
				<div class="open_in1">
					<div class="open_div">
						<dl>
						<c:if test="${listPage ne 'listRezvStat' || ( listPage eq 'listRezvStat' && !empty param.listType && param.listType ne 'week') }">
							<dd class="txt" onclick="fnSetRescKndId('');" data-rescKndId=""><u:msg titleId="cm.option.all" alt="전체선택"/></dd>
							<dd class="line"></dd>
						</c:if>
						<c:if test="${empty param.rescKndId && !empty wrRescKndBVoList}"><c:set var="rescKndNm" value="${wrRescKndBVoList[0].kndNm }"/></c:if>
						<c:forEach items="${wrRescKndBVoList}" var="list" varStatus="status">
						<c:if test="${!empty param.rescKndId && param.rescKndId == list.rescKndId }"><c:set var="rescKndNm" value="${list.kndNm }"/></c:if>
						<dd class="txt" onclick="fnSetRescKndId('${list.rescKndId}');" data-rescKndId="${list.rescKndId}">${list.kndNm}</dd>
						<dd class="line"></dd>
						</c:forEach>
						</dl>
					</div>
				</div>
			</div>
			<c:set var="rescMngNm"/>
			<div class="open2" style="display:none;">
				<div class="open_in2">
					<div class="open_div">
						<dl>
							<c:if test="${listPage ne 'listRezvStat' || ( listPage eq 'listRezvStat' && !empty param.listType && param.listType ne 'week') }">
								<dd class="txt" onclick="fnSetRescMngId('');" data-rescMngId=""><u:msg titleId="cm.option.all" alt="전체선택"/></dd>
								<dd class="line"></dd>
							</c:if>
							<c:if test="${empty param.rescMngId && !empty wrRescMngBVoList}"><c:set var="rescMngNm" value="${wrRescMngBVoList[0].rescNm }"/></c:if>
							<c:forEach items="${wrRescMngBVoList}" var="list" varStatus="status">
								<c:if test="${!empty param.rescMngId && param.rescMngId == list.rescMngId }"><c:set var="rescMngNm" value="${list.rescNm }"/></c:if>
							<dd class="txt" onclick="fnSetRescMngId('${list.rescMngId}');" data-rescMngId="${list.rescMngId}">${list.rescNm}</dd>
							<dd class="line"></dd>
							</c:forEach>
						</dl>
					</div>
				</div>
			</div>

			<div class="select1">
				<div class="select_in1" onclick="holdHide = true;$('.open1').toggle();">
					<dl>
					<dd class="select_txt1"><span>${rescKndNm }</span></dd>
					<dd class="select_btn"></dd>
					</dl>
				</div>
			</div>
			<div class="select2">
				<div class="select_in2" onclick="holdHide2 = true;$('.open2').toggle();">
					<dl>
					<dd class="select_txt2"><span>${rescMngNm }</span></dd>
					<dd class="select_btn"></dd>
					</dl>
				</div>
			</div>

		</div>
		<div class="searchbtn2" onclick="searchList(event);"></div>
	</div>

	<div class="booktop">
		<dl>
		<dd class="bk_prev" onclick="fnPageMove('m');"></dd>
		<dd class="bk_tit">${wrWeekVo.strtDt} ~ ${fn:substring(wrWeekVo.endDt,5,10)}</dd>
		<dd class="bk_next" onclick="fnPageMove('p');"></dd>
		<dd class="bk_today" onclick="fnPageMove('t');"><u:msg titleId="ct.cols.today" alt="오늘"/></dd>
		</dl>
	</div>
</div>
<!--//booktop_area E-->
</form>


<!--bookbody_area S-->
<div class="bookbody_area">
	<table class="bookbody">
		<colgroup>
		<col />
		<col width="11.9%"/>
		<col width="11.9%"/>
		<col width="11.9%"/>
		<col width="11.9%"/>
		<col width="11.9%"/>
		<col width="11.9%"/>
		<col width="11.9%"/>
		</colgorup>
		<tbody>
		<tr class="weekarea">
			<td class="week"></td>
			<c:forEach var="week" items="${wrWeekVo.wrDayVo }" varStatus="status">
			<u:msg var="dayOfWeek" titleId="wr.jsp.dayOfWeek${status.index }.title" alt="요일"/>
			<c:set var="weekDayTitle" value="${week.days }&nbsp;${dayOfWeek }"/>
			<td class="week<c:if test="${status.index == 0}">_sunday</c:if><c:if test="${status.index == 6}">_saturday</c:if>"><u:out value="${dayOfWeek }" maxLength="6"/></td>
			</c:forEach>
		</tr>
		</tbody>
	</table>
</div>
<!--//bookbody_area E-->

<!--bookbody_scroll S-->
<div class="bookbody_scroll">

	<!--bookbody_area S-->
	<div class="bookbody_area">
		<table class="bookbody">
			<colgroup>
			<col />
			<col width="11.9%"/>
			<col width="11.9%"/>
			<col width="11.9%"/>
			<col width="11.9%"/>
			<col width="11.9%"/>
			<col width="11.9%"/>
			<col width="11.9%"/>
			</colgorup>
			<tbody>
			<c:set var="strtHH" value="7"/>
			<c:set var="endHH" value="23"/>
			<c:forEach var="si" begin="${strtHH }" end="${endHH }" varStatus="siStatus">
			<tr>
				<u:set var="siValue" test="${si < 10}" value="0${si }" elseValue="${si }"/>
				<td class="time"><c:if test="${siStatus.first }"><u:msg titleId="cm.option.am" alt="오전"/></c:if><c:if test="${si == '12' }"><u:msg titleId="cm.option.pm" alt="오후"/></c:if>${siValue}:00</td>
				<c:forEach var="week" items="${wrWeekVo.wrDayVo }" varStatus="status">
				<c:set var="weekYmd" value="${fn:replace(week.days,'-','') }"/>
				<c:set var="fixedValue" value="${siValue }00"/>
				<c:set var="isHour" value="N" />
				<c:set var="actionHourRezv" value="setRezv(null,this);" />
				<c:set var="areaHourRezvId" value="" />
				<c:set var="areaHourRezvCss" value="" />
				<c:set var="areaHourRezvTitle" value="" />
				<c:forEach var="list" items="${wrRezvBVoList }" varStatus="listStatus">
				<c:set var="rezvStrtYmd" value="${fn:replace(list.rezvStrtYmd,'-','') }"/>
				<c:set var="rezvEndYmd" value="${fn:replace(list.rezvEndYmd,'-','') }"/>
				<c:set var="rezvStrtTime" value="${fn:replace(list.rezvStrtTime,':','') }"/>
				<c:set var="rezvEndTime" value="${fn:replace(list.rezvEndTime,':','') }"/>
				<c:choose>
				<c:when test="${rezvStrtYmd == rezvEndYmd && weekYmd >= rezvStrtYmd && weekYmd  <= rezvEndYmd }">
				<c:if test="${fixedValue >= rezvStrtTime && fixedValue < rezvEndTime}"><c:set var="isHour" value="Y" /><c:set var="areaHourRezvId" value="${list.rezvId }" /><c:set var="areaHourRezvTitle" value="${list.subj }" /><c:set var="areaHourRezvCss" value="${list.discStatCd eq 'R' ? '_gray' : '_blue' }" /></c:if>
				</c:when>
				<c:when test="${rezvStrtYmd < rezvEndYmd && ( weekYmd == rezvStrtYmd || weekYmd  == rezvEndYmd )  }">
				<c:if test="${(weekYmd == rezvStrtYmd && fixedValue >= rezvStrtTime ) || ( weekYmd == rezvEndYmd && fixedValue < rezvEndTime ) }"><c:set var="isHour" value="Y" /><c:set var="areaHourRezvId" value="${list.rezvId }" /><c:set var="areaHourRezvTitle" value="${list.subj }" /><c:set var="areaHourRezvCss" value="${list.discStatCd eq 'R' ? '_gray' : '_blue' }" /></c:if>
				</c:when>
				<c:when test="${rezvStrtYmd < rezvEndYmd && weekYmd > rezvStrtYmd && weekYmd  < rezvEndYmd }">
				<c:set var="isHour" value="Y" />
				<c:set var="areaHourRezvId" value="${list.rezvId }" />
				<c:set var="areaHourRezvCss" value="${list.discStatCd eq 'R' ? '_gray' : '_blue' }" />
				<c:set var="areaHourRezvTitle" value="${list.subj }" />
				</c:when>
				<c:otherwise>
				</c:otherwise>
				</c:choose>
				</c:forEach>
				<c:if test="${isHour eq 'Y' }"><c:set var="actionHourRezv" value="viewRezvPop('${areaHourRezvId }');"/></c:if>
				<td  class="bk_body ${areaHourRezvId}"  onclick="${actionHourRezv}" data-day="${week.days }" data-time="${siValue }:00"  title="${areaHourRezvTitle }" ><c:if test="${!empty areaHourRezvCss }"><div class="bk_body${areaHourRezvCss }"></div></c:if></td>
				</c:forEach>
			</tr>
			<tr>
				<u:set var="siValue" test="${si < 10}" value="0${si }" elseValue="${si }"/>
				<td class="time">${siValue}:30</td>
				<c:forEach var="week" items="${wrWeekVo.wrDayVo }" varStatus="status">
				<c:set var="weekYmd" value="${fn:replace(week.days,'-','') }"/>
				<c:set var="halfValue" value="${siValue }30"/>
				<c:set var="isHalfHour" value="N" />
				<c:set var="actionHalfRezv" value="setRezv(null,this);" />
				<c:set var="areaHalfRezvId" value="" />
				<c:set var="areaHalfRezvCss" value="" />
				<c:set var="areaHalfRezvTitle" value="" />
				<c:forEach var="list" items="${wrRezvBVoList }" varStatus="listStatus">
				<c:set var="rezvStrtYmd" value="${fn:replace(list.rezvStrtYmd,'-','') }"/>
				<c:set var="rezvEndYmd" value="${fn:replace(list.rezvEndYmd,'-','') }"/>
				<c:set var="rezvStrtTime" value="${fn:replace(list.rezvStrtTime,':','') }"/>
				<c:set var="rezvEndTime" value="${fn:replace(list.rezvEndTime,':','') }"/>
				<c:choose>
				<c:when test="${rezvStrtYmd == rezvEndYmd && weekYmd >= rezvStrtYmd && weekYmd  <= rezvEndYmd }">
				<c:if test="${halfValue >= rezvStrtTime && halfValue < rezvEndTime}"><c:set var="isHalfHour" value="Y" /><c:set var="areaHalfRezvId" value="${list.rezvId }" /><c:set var="areaHalfRezvTitle" value="${list.subj }" /><c:set var="areaHalfRezvCss" value="${list.discStatCd eq 'R' ? '_gray' : '_blue' }" /></c:if>
				</c:when>
				<c:when test="${rezvStrtYmd < rezvEndYmd && ( weekYmd == rezvStrtYmd || weekYmd  == rezvEndYmd )  }">
				<c:if test="${(weekYmd == rezvStrtYmd && halfValue >= rezvStrtTime ) || ( weekYmd == rezvEndYmd && halfValue < rezvEndTime ) }"><c:set var="isHalfHour" value="Y" /><c:set var="areaHalfRezvId" value="${list.rezvId }" /><c:set var="areaHalfRezvTitle" value="${list.subj }" /><c:set var="areaHalfRezvCss" value="${list.discStatCd eq 'R' ? '_gray' : '_blue' }" /></c:if>
				</c:when>
				<c:when test="${rezvStrtYmd < rezvEndYmd && weekYmd > rezvStrtYmd && weekYmd  < rezvEndYmd }">
				<c:set var="isHalfHour" value="Y" />
				<c:set var="areaHalfRezvId" value="${list.rezvId }" />
				<c:set var="areaHalfRezvCss" value="${list.discStatCd eq 'R' ? '_gray' : '_blue' }" />
				<c:set var="areaHalfRezvTitle" value="${list.subj }" />
				</c:when>
				<c:otherwise>
				</c:otherwise>
				</c:choose>
				</c:forEach>
				<c:if test="${isHalfHour eq 'Y' }"><c:set var="actionHalfRezv" value="viewRezvPop('${areaHalfRezvId }');"/></c:if>
				<td  class="bk_body ${areaHalfRezvId}"  onclick="${actionHalfRezv}" data-day="${week.days }" data-time="${siValue }:30"   title="${areaHalfRezvTitle }" ><c:if test="${!empty areaHalfRezvCss }"><div class="bk_body${areaHalfRezvCss }" ></div></c:if></td>
				</c:forEach>
			</tr>
			<tr>
				<td class="bk_line${halfValue == '1130' ? '_b' : ''}" colspan="8"></td>
			</tr>
			</c:forEach>

			</tbody>
		</table>
	</div>
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
</div>

</section>
<!--//section E-->



