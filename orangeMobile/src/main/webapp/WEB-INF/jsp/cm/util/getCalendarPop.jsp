<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="option" test="${param.opt!=null && param.opt!=''}" value="'${param.id}',${param.opt }" elseValue="'${param.id}'"/>
<script type="text/javascript">
//<![CDATA[
function fnCalendarConfirm(){
	var date = $('#'+calendar.myId).val();
	var win = $m.nav.getWin();
	<c:if test="${not empty param.popId}">
		win = win.$m.dialog.getDialog('${param.popId}');
	</c:if>
	if(win==null) return;
	<c:if test="${empty param.popId}">
		win = win.document;
	</c:if>
	if(calendar.checkOpt!=null){
		if(calendar.checkOpt['end']!=null){
			var $endCal = $(win).find('.etr_calendar input#'+calendar.checkOpt['end']);
			var $endCalSpan = $(win).find('.etr_calendar span#'+calendar.checkOpt['end'])
			var endDt = $endCal.val();
			if(endDt!=null && endDt!='' && date>endDt){ 
				$endCal.val(date); 
				var hm = '';
				<c:if test="${!empty param.hm}">
				var arrDtHm = $endCalSpan.text().split(' ');
				hm = ' ' + arrDtHm[1];
				</c:if>		
				$endCalSpan.text(date + hm);
			}
		}
		if(calendar.checkOpt['start']!=null){
			var $startCal = $(win).find('.etr_calendar input#'+calendar.checkOpt['start']);
			var $startCalSpan = $(win).find('.etr_calendar span#'+calendar.checkOpt['start'])
			var startDt = $startCal.val();
			if(startDt!=null && startDt!='' && date<startDt){ 
				$startCal.val(date); 
				var hm = '';
				<c:if test="${!empty param.hm}">
				var arrDtHm = $startCalSpan.text().split(' ');
				hm = ' ' + arrDtHm[1];
				</c:if>		
				$startCalSpan.text(date + hm);
			}
		}
	}
	<c:if test="${!empty param.handler && param.handler != 'undefined'}">
	var dateVa=date;
	<c:if test="${!empty param.hm}">dateVa=dateVa + ' ' + $("#${param.hmId}").val();</c:if>
	if($m.nav.getWin().${param.handler}(dateVa, calendar.myId)) {return;};
	</c:if>
	$(win).find('.etr_calendar input#'+calendar.myId).val(date);
	$(win).find('.etr_calendar span#'+calendar.myId).text(date);
	<c:if test="${!empty param.hm}">
		$(win).find('.etr_calendar input#${param.hmId}').val($("#${param.hmId}").val());
		$(win).find('.etr_calendar span#'+calendar.myId).text(date + ' ' + $("#${param.hmId}").val());
	</c:if>
	$m.dialog.close(calendar.myId);
}

$(document).ready(function() {
	calendar.open(${option});
	
	var hmTp = '${param.hm}';
	
	if(hmTp != ''){
		var hmVal = '${param.hmVal}';
		if(hmVal!=''){
			var hmValArr = hmVal.split(':');
			$("#hr").text(hmValArr[0]);
			$("#mi").text(hmValArr[1]);
		}else{
			var d = new Date();
			$("#hr").text((d.getHours() < 10 ? "0"+d.getHours() : d.getHours()));
			$("#mi").text('00'); //(d.getMinutes() < 10 ? "0"+d.getMinutes() : d.getMinutes())
			setHm();
		}
	}
	
	if($('#'+calendar.myId).val() == ''){
		fnSetValToday();
	}
	
	<c:if test="${!empty param.hm}">
	touchEvt();
	</c:if>
});

function hr(ctrl){
	var hr = parseInt($("#hr").text());
	if(ctrl=='+') hr++;
	else hr--;
	if(hr>23) hr = 0;
	if(hr<0) hr = 23;
	$("#hr").text(hr);
	setHm();
}
<c:if test="${empty param.hmUnit}">
function mi(ctrl){
	var mi = parseInt($("#mi").text());
	if(ctrl=='+') mi=mi+30;
	else mi=mi-30;
	if(mi>30) mi = 0;
	if(mi<0) mi = 30;
	if(mi == 0 ) mi = '00';
	$("#mi").text(mi);
	setHm();
}
</c:if>
<c:if test="${!empty param.hmUnit && param.hmUnit=='Y'}">
function mi(ctrl){
	var mi = parseInt($("#mi").text());
	if(ctrl=='+') mi++;
	else mi--;
	if(mi>59) mi = 0;
	if(mi<0) mi = 59;
	if(mi<10) mi='0'+mi;
	$("#mi").text(mi);
	setHm();
}
</c:if>

function setHm(){
	var hr = parseInt($("#hr").text());
	var mi = parseInt($("#mi").text());
	if(hr<10) hr='0'+hr;
	if(mi<10) mi='0'+mi;
	$("#${param.hmId}").val(hr+':'+mi);
}

function fnToday(){
	var d = new Date();
	calendar.moveTo(d.﻿getFullYear(),(d.getMonth()+1));
	fnSetValToday();
}

function fnSetValToday(){
	var d = new Date();
	$('#'+calendar.myId).val(d.﻿getFullYear()+'-'+calendar.fm(d.getMonth()+1)+'-'+calendar.fm(d.getDate()));
}

var touchEvent = false; // 터치이벤트 활성화 여부
var clickEvent = false; // 클릭이벤트 활성화 여부
<% // 롱탭 이벤트 활성화 %>
function startLongTapTimer(e) {
    var self = this;
    this.longTabTimer = setTimeout(function() {
    	$(e).trigger('click');    	
        delete self.longTabTimer;
        startLongTapTimer(e);
    }, 100);
}
<% // 롱탭 이벤트 삭제 %>
function deleteLongTabTimer() {  
    if( typeof this.longTabTimer !== 'undefined') {
        clearTimeout(this.longTabTimer);
        delete this.longTabTimer;
    }
}
<% // 터치 이벤트 초기화 %>
function touchEvt(){
	$('div.calendar_setup').find('dd.up, dd.down').each(function(){
		$(this).on('touchstart',function(e){
			startLongTapTimer(this); // 롱탭 이벤트 활성화
			touchEvent=true; // 터치이벤트
		});
		$(this).on('touchmove',function(e){
			if(!touchEvent)
				return;
			deleteLongTabTimer();
		});
		$(this).on('touchend',function(e){
			if(!touchEvent)
				return;
			deleteLongTabTimer();
			touchEvent=false;
		});
	});
	//return;
	$('div.calendar_setup').find('dd#hr, dd#mi').each(function(){
		$(this).on('click',function(e){
			if(clickEvent) return;
			$(this).closest('dl').find('dd.up, dd.down').css('visibility', 'hidden');
			var $input=$('<input type="number" pattern="[0-9]*" inputmode="numeric" min="0" style="width:38px;height:40px;font:80% \'Tahoma\';" maxlength="2" oninput="calValidate(this);" onblur="unloadInput(this,\''+$(this).text()+'\');" />');
			// onchange="unloadInput(this,\''+$(this).text()+'\');"
			$(this).html('');
			$(this).append($input);			
			$input.focus(function(){
				setScrollTop($(this));
			});
			$input.focus();
			//$input.select();
			clickEvent=true;
		});
	});
}<% // 키보드 팝업시 스크롤 위치 변경 %>
function setScrollTop(obj){
    $('#scrollContainer').animate({scrollTop : obj.offset().top}, 400);
}<% // 숫자 입력 후 적용 %>
function unloadInput(obj, bText){
	$('div.calendar_setup').find('dd.up, dd.down').css('visibility', 'visible');
	var val = obj.value;
	if(val!='')
		val=val.replace(/\D/g,'');
	var parent=$(obj).closest('dd');
	if(val!='' && parent.attr('id')=='hr' && (parseInt(val)<0 || parseInt(val)>23))
		val='12';
	if(val!='' && parent.attr('id')=='mi' && (parseInt(val)<0 || parseInt(val)>59))
		val='30';	
	$(obj).remove();
	parent.text(val=='' ? bText : parseInt(val)<10 && val.length==1 ? '0'+val : val);
	clickEvent=false;
	setHm();
}<% // 자리수 체크 %>
function calValidate(obj){
	if($(obj).val().length>2){
		$(obj).val($(obj).val().substring(1));
	}
}

//]]>
</script>

<div class="${!empty param.calStyle && param.calStyle eq 'm' ? 'popup' : 'cal_popup' }">

	<div class="${!empty param.calStyle && param.calStyle eq 'm' ? 'inner' : 'calendar_scroll' }" id="scrollContainer">

	<input id="${param.id}" value="${param.val}" type="hidden" />
	<div id="${param.id}CalArea"></div>

	<c:if test="${!empty param.hm}">
	<input id="${param.hmId}" value="${param.hmVal}" type="hidden" />
	     <div class="calendar_setup">
	         <div class="setupin">     
                <dl class="year">
                    <dd class="up" onclick="hr('+');"></dd>
                    <dd class="txt" id="hr">2015</dd>
                    <dd class="down" onclick="hr('-');"></dd>
                </dl>
				<c:if test="${param.hm == 'm'}">
				<dl>
                    <dd class="txt2">:</dd>
	            </dl>
                <dl class="month">
                	<c:choose>
                		<c:when test="${empty param.unit || param.unit ne 'ontime'}"><dd class="up" onclick="mi('+');"></dd></c:when>
                		<c:otherwise><dd style="height:43px;margin:0 auto -14px auto"></dd></c:otherwise>
                	</c:choose>
                    <dd class="txt" id="mi">12</dd>
                    <c:choose>
                		<c:when test="${empty param.unit || param.unit ne 'ontime'}"><dd class="down" onclick="mi('-');"></dd></c:when>
                		<c:otherwise><dd style="height:43px;margin:0 auto -14px auto"></dd></c:otherwise>
                	</c:choose>
	            </dl>
				</c:if>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty param.calStyle && param.calStyle eq 'm' }">
		<div class="calendar_today${!empty param.calStyle && param.calStyle eq 'm' ? '_s' : '' }">
		    <dl>
		    <dd class="txt" onclick="fnToday();"><u:msg titleId="ct.cols.today" alt="오늘" /></dd>
		    </dl>
	    </div>
		<div class="calendar_btn${!empty param.calStyle && param.calStyle eq 'm' ? 'fix' : '' }">
	        <div class="">
	        <dl>
	        <dd class="left" onclick="$m.dialog.close('${param.id}')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
	        <dd class="right" onclick="fnCalendarConfirm();"><u:msg titleId="cm.btn.confirm" alt="확인" /></dd>
	
	        </dl>
	        </div>
	    </div>
	</c:if>
	<c:if test="${empty param.calStyle || param.calStyle ne 'm' }">
		<div class="calendar_today${!empty param.calStyle && param.calStyle eq 'm' ? '_s' : '' }">
		    <dl>
		    <dd class="txt" onclick="fnToday();"><u:msg titleId="ct.cols.today" alt="오늘" /></dd>
		    </dl>
	    </div>
	</c:if>
 	</div>
    <c:if test="${empty param.calStyle || param.calStyle ne 'm' }">
    	<div class="calendar_btn${!empty param.calStyle && param.calStyle eq 'm' ? 'fix' : '' }">
	        <div class="">
	        <dl>
	        <dd class="left" onclick="$m.dialog.close('${param.id}')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
	        <dd class="right" onclick="fnCalendarConfirm();"><u:msg titleId="cm.btn.confirm" alt="확인" /></dd>
	
	        </dl>
	        </div>
	    </div>
    </c:if>    
</div>    


