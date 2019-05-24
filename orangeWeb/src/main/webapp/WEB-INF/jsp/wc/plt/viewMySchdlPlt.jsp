<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>	
	<script type="text/javascript">
	function selectMonth(param){

		$('#action').val(param);

		var $form = $('#calendarPrintForm');

		
		$form.attr('method','post');
		$('#tabNo').val('2');
		$form.attr('action','/wc/plt/viewMySchdlPlt.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
		

	}
	
	//상세보기 팝업
	function viewSchdlPop(schdlId) {
		//alert('${params}');
		parent.dialog.open('viewSchdlPop','<u:msg titleId="wc.btn.schdlDetail" alt="상세보기" />','/wc/viewSchdlPop.do?${params}&plt=Y&schdlId='+schdlId+'');
	};
	
	


	
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

		var arr = {
			'molyList': "${wcScdCalMonth.year}" + "-"  + convMolyMonth,
			'welyList': "${wcScdCalWeek.year}" + "-"  + convWelyMonth + " " +"${wcScdCalWeek.week}"  +"주차",
			'dalyList':"${wcScdCalDay.year}" + "-"  + convDalyMonth + "-" + convDalyDay
		};
		$('#todayTd').html("${wcScdCalDay.year}" + "-"  + convDalyMonth + "-" + convDalyDay);
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
	
	function listSchdl(type){
		if(type=="psn")
			top.location.href="/wc/listNewSchdl.do?fncCal=my";
		else if(type=="dept")
			top.location.href="/wc/listNewSchdl.do?fncCal=dept";
		else if(type=="comp")
			top.location.href="/wc/listNewSchdl.do?fncCal=comp";
	}
	
	
	$(document).ready(function() {
		setUniformCSS();
		setTodayTd(2);
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
					
				
				
		<table class="ptltable" border="0" cellpadding="0" cellspacing="0">
			<tr>
			  <td class="head_ct" style="padding:2px 0 2px 0;"><table class="center" border="0" cellpadding="0" cellspacing="0">
				  <tr>
				   <!-- 날짜 및 요일 표기 S -->
					  <td class="frontico"><a href="javascript:selectMonth('before')"><img src="/images/${_skin}/ico_left.png" /></a></td>
					  <td class="head_ct" id="todayTd" style="font-weight:bold;width:75px;"></td>
					  <td class="frontico"><a href="javascript:selectMonth('after')"><img src="/images/${_skin}/ico_right.png" /></a></td>
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
	
			<tr>
			  <td class="body_lt"><img src="/images/${_skin}/ico_document.png" /><strong><a href="javascript:listSchdl('psn');"><u:msg titleId="wc.option.psnSchdl" alt="개인일정"/></a></strong></td>
			</tr>
			
			
			<!-- 개인일정 반복 구간 S-->			
			<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
				<c:if test="${scds.schdlTypCd != '5' && scds.schdlKndCd == '1'&& !empty scds.subj}">
				<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
				<u:set var="catFontCss" test="${!empty mapList && !empty mapList.fontColrCd}" value="style='font-size:11px; color:${mapList.bgcolCd }; line-height:16px; padding:0 3px 0 4px;'" elseValue="style='font-size:11px; line-height:16px; padding:0 3px 0 4px;'"/>
				<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="HH:mm"/>
			
				<tr>
				  <td>
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
			  <td class="body_lt"><img src="/images/${_skin}/ico_document.png" /><strong><a href="javascript:listSchdl('dept');"><u:msg titleId="wc.option.deptSchdl" alt="부서일정"/></a></strong></td>
			</tr>
			<!-- 부서일정 반복 구간 S-->
			<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
				<c:if test="${scds.schdlTypCd != '5' && scds.schdlKndCd == '3'&& !empty scds.subj}">
				<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
				<u:set var="catFontCss" test="${!empty mapList && !empty mapList.fontColrCd}" value="style='font-size:11px; color:${mapList.bgcolCd }; line-height:16px; padding:0 3px 0 4px;'" elseValue="style='font-size:11px; line-height:16px; padding:0 3px 0 4px;'"/>
				<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="HH:mm"/>
			
				<tr>
				  <td>
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
			  <td class="body_lt"><img src="/images/${_skin}/ico_document.png" /><strong><a href="javascript:listSchdl('comp');"><u:msg titleId="wc.option.compSchdl" alt="회사일정"/></a></strong></td>
			</tr>
			<!-- 회사 일정 반복 구간 S -->
			<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
				<c:if test="${scds.schdlTypCd != '5' && scds.schdlKndCd == '4'&& !empty scds.subj}">
				<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
				<u:set var="catFontCss" test="${!empty mapList && !empty mapList.fontColrCd}" value="style='font-size:11px; color:${mapList.bgcolCd }; line-height:16px; padding:0 3px 0 4px;'" elseValue="style='font-size:11px; line-height:16px; padding:0 3px 0 4px;'"/>
				<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/>
				<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="HH:mm"/>
			
				<tr>
				  <td>
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
	</form>
