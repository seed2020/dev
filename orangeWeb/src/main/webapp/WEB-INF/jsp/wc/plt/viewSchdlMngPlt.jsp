<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
	<script type="text/javascript">
	function selectMonth(param){

		$('#action').val(param);

		var $form = $('#calendarPrintForm');

		
		$form.attr('method','post');
		$('#tabNo').val('0');
		$form.attr('action','/wc/plt/viewSchdlMngPlt.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
		

	}
	
	function setTodayTd(areaId) {
		<%//tab별 이름 셋팅 %>
		$('#submitTab').val(areaId);
		<%//molyList 자릿수 01월 01월 맞추기..%>
		var molyMonth = "${wcScdCalMonth.month}";
		var convMolyMonth;
		if(molyMonth.length==1) convMolyMonth = "0"+ molyMonth;
		else convMolyMonth = molyMonth;
		<%//welyList 자릿수 01월 01월 맞추기..%>
		var welyMonth = "${wcScdCalWeek.month}";
		var convWelyMonth;
		if(welyMonth.length==1) convWelyMonth = "0"+ welyMonth;
		else convWelyMonth = welyMonth;
		<%//dalyList 자릿수 01월 01월 맞추기..%>
		var dalyMonth = "${wcScdCalDay.month}";
		var dalyDay = "${wcScdCalDay.day}";
		var convDalyMonth;
		var convDalyDay;
		if(dalyMonth.length==1) convDalyMonth = "0"+ dalyMonth;
		else convDalyMonth = dalyMonth;
		if(dalyDay.length==1) convDalyDay = "0"+ dalyDay;
		else convDalyDay = dalyDay;

		
		$('#todayTd').html("${wcScdCalMonth.year}" + "-"  + convMolyMonth);
	}
	
	//포틀릿의 가로 세로 길이 재정렬
	function fnPltSet(){
		var wdthPx = '${param.wdthPx}' == '' ? $('#ptltable').width() : '${param.wdthPx -18}';
		var selList = [];
		var headList = getHeadList();//목록컬럼
		var vaAlign = getVaAlignList();//정렬
		var bodyList = getBodyList();//데이터목록
		var colList = getColList();//가로 사이즈
		
		//가로사이즈에 맞게 보여줄 컬럼 목록(배열index)
		if( wdthPx <= 400 ) selList = [0,2];
		
		if(selList.length == 0 ){//항목을 모두 보여줄때
			for(var i=0;i<headList.length;i++){
				$('#head_'+headList[i].split('.')[1]).show();
				$('.body_'+bodyList[i]).addClass(vaAlign[i]);
				$('.body_'+bodyList[i]).show();
				$('.body_'+bodyList[i]).css("width",colList[i]);
			}
		}else{
			for(var i=0;i<selList.length;i++){
				$('#head_'+headList[selList[i]].split('.')[1]).show();
				$('.body_'+bodyList[selList[i]]).addClass(vaAlign[selList[i]]);
				$('.body_'+bodyList[selList[i]]).show();
				$('.body_'+bodyList[selList[i]]).css("width",colList[selList[i]]);
			}
		}
		var colspan = selList.length == 0 ? headList.length : selList.length;
		$('.line').attr('colspan',colspan);
		$('.nodata').attr('colspan',colspan);
	}
	
	function listSchdl(year,month,day){	
		
		top.location.href="/wc/listNewSchdl.do?fncCal=my&tabNo=2&dalyYear="+year+"&dalyMonth="+month+"&dalyDay="+day+"&molyYear="+year+"&molyMonth="+month+"&welyYear="+year+"&welyMonth="+month+"&welyWeek="+1+"&action=";		
	}
	
	
	$(document).ready(function() {
		setUniformCSS();
		setTodayTd(1);
	});
	</script>
	
	
	<form method="get" id="calendarPrintForm" name ="calendarPrintForm">
		<u:input type="hidden" name="menuId"  value="${menuId}"/>
		<u:input type="hidden" name="viewUserUid" value="${viewUserUid}"/>
		<u:input type="hidden" name="viewUserNm" value="${viewUserNm}"/>
		<u:input type="hidden" name="viewOrgId" value="${viewOrgId}"/>
		<u:input type="hidden" name="viewOrgNm" value="${viewOrgNm}"/>

		<u:input type="hidden" name="molyYear" value="${wcScdCalMonth.year}"/>
		<u:input type="hidden" name="molyMonth"  value="${wcScdCalMonth.month}"/>
		<u:input type="hidden" name="welyYear"  value="${wcScdCalWeek.year}"/>
		<u:input type="hidden" name="welyMonth"  value="${wcScdCalWeek.month}"/>
		<u:input type="hidden" name="welyWeek"  value="${wcScdCalWeek.week}"/>
		<u:input type="hidden" name="dalyYear"  value="${wcScdCalDay.year}"/>
		<u:input type="hidden" name="dalyMonth"  value="${wcScdCalDay.month}"/>
		<u:input type="hidden" name="dalyDay"  value="${wcScdCalDay.day}"/>

		<u:input type="hidden" name="fncCal"  value="${fncCal}"/>
		<u:input type="hidden" name="tabNo"  value="${tabNo}"/>
		<u:input type="hidden" name="tabNoRT" />

		<u:input  type="hidden" id="action"/>
		<u:input  type="hidden" id="submitTab" />
		
		
		<div class="ptlbody_ct" width='100%' border='1'>
			<!--calendar S-->
			<div class="calendar" style="width:100%;border:0px solid #ffffff;padding:5px 0 0 0;">
				<table class="month" border="0" cellpadding="0" cellspacing="0">
					<tr>
					  	<!-- 날짜 및 요일 표기 S -->
					  	<td class="frontico"><a href="javascript:selectMonth('before')"><img src="/images/${_skin}/ico_left.png" /></a></td>
						<td class="head_ct" id="todayTd" style="font-weight:bold;width:75px;"></td>
						<td class="frontico"><a href="javascript:selectMonth('after')"><img src="/images/${_skin}/ico_right.png" /></a></td>
						<!-- 날짜 및 요일 표기  E -->			  
			
					</tr>
				</table>
				
				<table class="week_pd" border="0" cellpadding="0" cellspacing="0" style="width:100%;">
					<tr>
					  <td width="*" class="week_red" valign="middle" align="center"><u:msg titleId="wc.cols.sun" alt="일" /></td>
					  <td width="14%" class="week_gray" valign="middle" align="center"><u:msg titleId="wc.cols.mon" alt="월" /></td>
					  <td width="14%" class="week_gray" valign="middle" align="center"><u:msg titleId="wc.cols.tue" alt="화" /></td>
					  <td width="14%" class="week_gray" valign="middle" align="center"><u:msg titleId="wc.cols.wed" alt="수" /></td>
					  <td width="14%" class="week_gray" valign="middle" align="center"><u:msg titleId="wc.cols.thu" alt="목" /></td>
					  <td width="14%" class="week_gray" valign="middle" align="center"><u:msg titleId="wc.cols.fri" alt="금" /></td>
					  <td width="14%" class="week_blue" valign="middle" align="center"><u:msg titleId="wc.cols.sat" alt="토" /></td>
					</tr>
					
				<c:set var="maxRowIndex" value="4"/>
				<c:forEach  var="calWeek" items="${wcScdCalMonth.weeks}" varStatus="weekStatus">
					<tr>
					  	<c:forEach  var="calDay" items="${calWeek.days}" varStatus="dayStatus">
					  		<c:set var="scds_color"	value= "day_gray" />
					  		<c:if test="${calDay.holiFlag == 'scddate_prev'}">
					  		 	<c:set var="scds_color"	value= "day_gray_off" />
					  		</c:if>
					  		
					  		<c:if test="${calDay.holiFlag == 'scddate_red_prev' }">
					  		 	<c:set var="scds_color"	value= "day_red_off" />
					  		</c:if>
					  		
					  		<c:if test="${calDay.holiFlag == 'scddate_red' }">
					  		 	<c:set var="scds_color"	value= "day_red" />
					  		</c:if>
					  		<c:if test="${fn:length(calDay.scds) >  0 }">
					  		 	<c:set var="scds_color"	value= "today" />
					  		</c:if>				  		
					  		
					  		<td valign="middle" align="center">
					  			<a href="javascript:listSchdl('${calDay.year}','${calDay.month}','${calDay.day}');" class="${scds_color}" ><u:out value="${calDay.day}"/></a>
					  		</td>
						</c:forEach>
					</tr>
				</c:forEach>
					<!-- <tr>
					  <td  valign="middle" align="center"><a href="javascript:goSchdl();" class="day_red" >2</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">3</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">4</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">5</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">6</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">7</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_blue">8</a></td>
					</tr>
					<tr>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_red">9</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">10</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">11</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">12</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">13</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">14</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_blue">15</a></td>
					</tr>
					<tr>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_red">16</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">17</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">18</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">19</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">20</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">21</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_blue">22</a></td>
					</tr>
					<tr>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_red">23</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">24</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">25</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="today">26</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">27</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray">28</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_blue_off">1</a></td>
					</tr>
					<tr>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_red_off">1</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray_off">2</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray_off">3</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray_off">4</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray_off">5</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_gray_off">6</a></td>
					  <td valign="middle" align="center"><a href="javascript:goSchdl();" class="day_blue_off">7</a></td>
					</tr>
					 -->
				</table>
			</div>
		</div>
	</form>