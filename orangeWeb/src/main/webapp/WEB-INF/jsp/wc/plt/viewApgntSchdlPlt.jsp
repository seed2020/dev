<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:authUrl var="viewUrl" url="/wc/viewSchdlPop.do" authCheckUrl="/wc/listNewSchdl.do?fncCal=my"/><!-- view page 호출관련 url 조합(menuId추가) -->
<u:authUrl var="listUrl" url="/wc/listNewSchdl.do?fncCal=my" authCheckUrl="/wc/listNewSchdl.do?fncCal=my"/><!-- list page 호출관련 url 조합(menuId추가) -->
	
	<script type="text/javascript">
	function selectMonth(param){

		$('#action').val(param);

		var $form = $('#calendarPrintForm');

		
		$form.attr('method','post');
		$('#tabNo').val('2');
		$form.attr('action','/wc/plt/viewMySchdlPlt.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
		

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
		var authUrl = '${listUrl}';
		if(type=="psn")
			top.location.href=authUrl;
		else if(type=="dept")
			top.location.href="/wc/listNewSchdl.do?fncCal=dept";
		else if(type=="comp")
			top.location.href="/wc/listNewSchdl.do?fncCal=comp";
	}
	
	
	$(document).ready(function() {
		setUniformCSS();
	
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
			
			<c:forEach var="apntSchdl" items="${apntSchdlList}" varStatus="status">
				<!-- 타인일정 반복 구간 S-->
				<tr>
				  <td class="line"></td>
				</tr>
				<tr>
				  <td class="height5"></td>
				</tr>
		
				<tr>
				  <td class="body_lt"><img src="/images/${_skin}/ico_document.png" /><strong><a href="javascript:listSchdl('psn');">[${apntSchdl.rescNm}] <u:msg titleId="wc.jsp.listPsnSchdl.apgnt.title" alt="님의 오늘 일정"/></a></strong></td>
				</tr>
				
				
				<!-- 타인 한사람 일정 반복 구간 S-->			
				<c:forEach var="scds" items="${apntSchdl.scdCalDay.scds}" varStatus="status">
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
					
					  	<span class="scdArea scd_${scds.schdlTypCd }" ${catFontCss }>				  	
					  		<fmt:formatDate value="${dateStartTempParse}" pattern="HH:mm"/>&nbsp;<u:out value="${scds.subj}"/>
					  	</span>
					  </td>
					</tr>				
					
					</c:if>
				</c:forEach>
				<!-- 타인 한사람 반복 구간 E-->
				
		
				<tr>
				  <td class="height5"></td>
				</tr>
				<!-- 타인일정 반복 구간 E-->
			</c:forEach>
			
	
		</table>
	</form>
