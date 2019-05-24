<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="option" test="${!empty param.opt}" value="'${param.id}',${param.opt }" elseValue="'${param.id}'"/>
<u:set var="y" test="${!empty param.y}" value="${param.y}" elseValue="1950"/>
<u:set var="m" test="${!empty param.m}" value="${param.m}" elseValue="01"/>
<script type="text/javascript">
//<![CDATA[
$(document).ready(function() {
	var buffer = [];
	var i, y = 1950, yearLast=75, monthLast=12;
	for(i=0;i<=yearLast;i++){
		buffer.push('<dl onclick="javascript:setYear('+(y+i)+');"><dd class="sradio'+(i==0?'_on':'')+'" id="yearCheck" data-year="'+(y+i)+'"><div class="txt">'+(y+i)+'</div></dd></dl>');
		if(i<yearLast)
			buffer.push('<dl><dd class="line"></dd></dl>\n');
	}
	$('#yearArea').html(buffer.join(''));

	buffer = [];
	for(var m=1;m<=monthLast;m++){
		buffer.push('<dl onclick="javascript:setMonth(\''+calendar.fm(m)+'\');"><dd class="sradio'+(m==1?'_on':'')+'" id="monthCheck" data-month="'+calendar.fm(m)+'"><div class="txt">'+calendar.fm(m)+'</div></dd></dl>');
		if(m<monthLast)
			buffer.push('<dl><dd class="line"></dd></dl>\n');
	}
	$('#monthArea').html(buffer.join(''));
	
	setYear('${y }');
	setMonth('${m }');

	var yearPos = $("#yearArea .sradio_on").offset();
	var monthPos = $("#monthArea .sradio_on").offset();
	$(".calendar_scrolllt").scrollTop((parseInt(yearPos.top)-758));
	$(".calendar_scrollrt").scrollTop((parseInt(monthPos.top)-758));
});	

function setYear(val){
	$('#selectYear').val(val);
	$('#yearArea #yearCheck').each(function(){
		$(this).attr("class", "sradio");
	});
	$("#yearArea dd[data-year='"+val+"']").attr("class","sradio_on");
}

function setMonth(val){
	 $('#selectMonth').val(val);
		$('#monthArea #monthCheck').each(function(){
			$(this).attr("class", "sradio");
		});
		$("#monthArea dd[data-month='"+val+"']").attr("class","sradio_on");
}

function fnSelectorConfirm(){
	var y = $('#selectYear').val(), m = $('#selectMonth').val();
	calendar.moveTo(y,m);
	$m.dialog.close('getCalendarSelectorPop');
}
//]]>
</script>
<input type="hidden" id="selectYear" value=""/>
<input type="hidden" id="selectMonth" value=""/>
<div class="popbackground"></div>

<div class="popuparea">
<div class="cal_popup">
    
        <div class="calendar_scrolllt">
            <div class="pop_radio" id="yearArea">
            </div>           
        </div>

        <div class="calendar_scrollrt">
            <div class="pop_radio" id="monthArea">
            </div>            
        </div>
            
        <div class="calendar_btn">
        <dl>
        <dd class="left" onclick="$m.dialog.close('getCalendarSelectorPop')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
        <dd class="right" onclick="fnSelectorConfirm();"><u:msg titleId="cm.btn.confirm" alt="확인" /></dd>
        </dl>
        </div>
    
</div>    
</div>