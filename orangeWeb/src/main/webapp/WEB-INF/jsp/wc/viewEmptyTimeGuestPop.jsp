<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
var i=0;
var pasInt =0;

$(function(){
	var $starDt = $("#scdlStartDt").val();
	var $edDt = $("#scdlEndDt").val();
	var sdt = new Date($starDt);
	var edt = new Date($edDt);
	//var termDt = [];
	var termToDiv = [];
	var dtGap = $("#scdGapDay").val();
	pasInt = parseInt(dtGap) +1;
	var $gustScdlLst = $("#guestScdlLst").val();
	
	for(i=0; i<pasInt; i++){
		
		var yy = sdt.getFullYear().toString();
		var mm = (sdt.getMonth()+1).toString();
		var dd = sdt.getDate().toString();
		var toDay = yy + "-" + (mm[1] ? mm : '0'+mm[0]) + "-" + (dd[1] ? dd : '0'+dd[0]);
		termToDiv.push("<option value = "+i+"> "+toDay+" </option>");
		sdt.setDate(sdt.getDate() + 1);
	}
	$("#seledtBox").html(termToDiv.join(''));
	
	var $selectDate = $("#seledtBox").val();
	for(i=0; i<pasInt; i++){
		if($selectDate == i){
			$('div[id^='+$selectDate+']').show();
		}else{
			$('div[id^='+i+']').hide();
		}
	}
	
});

function showTable(){
	var $selectDate = $("#seledtBox").val();
	for(i=0; i<pasInt; i++){
		if($selectDate == i){
			$('div[id^='+$selectDate+']').show();
		}else{
			$('div[id^='+i+']').hide();
		}
	}
}

<% // 폼 전송 %>
function formSubmit(){
	var $form = $("#setGrpForm");
	$form.attr('method', 'POST');
	$form.attr('action', './transSetGrpPopSave.do?menuId=${menuId}');
	$form.submit();
}

$(document).ready(function() {
	setUniformCSS();
});

//-->
</script>
<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="grpNm" value="부천지역모임" elseValue="" />
<u:set test="${fnc == 'mod'}" var="selected" value=" selected" elseValue="" />
<input id = "userUid" name = "userUid" type = "hidden" />
<div style="width:750px">
<form id="setGrpForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<input id="setSchdlGrp" name="setSchdlGrp" value="setSchdlGrp" type="hidden"/>
<div id = "userInfo" name = "userInfo">
<input id="scdlStartDt" name ="scdlStartDt" type="hidden" value="${scdlStartDt}"/>
<input id="scdlEndDt" name ="scdlEndDt" type="hidden" value="${scdlEndDt}"/>
<input id="scdGapDay" name ="scdGapDay" type="hidden" value="${scdGapDay}"/>
<input id="guestScdlLst" name="guestScdlLst" type="hidden" value="${guestScdlLst}"/>

</div>


<div class="front">
<div class="front_left">
	<table border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
		<td class="frontbtn">
			<select id="seledtBox" onChange="javascript:showTable()">
	 		</select>
	 	</td>
	</tr>
	</tbody></table>
</div>
</div>

<% // 폼 필드 %>
<c:forEach var="guestUseDay" items="${guestScdlLst}" varStatus="status">
<div id = "${status.index}_emptyTimeDiv">
	<u:listArea>
			<tr>
				<td width="20%" class="head_ct"> <u:msg titleId="cols.nm" alt="이름"/> </td>
				<td width="5%" class="head_ct"> 6AM </td>
				<td width="5%" class="head_ct"> 7AM </td>
				<td width="5%" class="head_ct"> 8AM </td>
				<td width="5%" class="head_ct"> 9AM </td>
				<td width="5%" class="head_ct"> 10AM </td>
				<td width="5%" class="head_ct"> 11AM </td>
				<td width="5%" class="head_ct"> 12AM </td>
				<td width="5%" class="head_ct"> 1PM </td>
				<td width="5%" class="head_ct"> 2PM </td>
				<td width="5%" class="head_ct"> 3PM </td>
				<td width="5%" class="head_ct"> 4PM </td>
				<td width="5%" class="head_ct"> 5PM </td>
				<td width="5%" class="head_ct"> 6PM </td>
				<td width="5%" class="head_ct"> 7PM </td>
				<td width="5%" class="head_ct"> 8PM </td>
				<td width="5%" class="head_ct"> 9PM </td>
			</tr>
			<c:forEach var="userList" items="${guestUidList}" varStatus="status">
				<tr>
					<td width="20%" class="head_ct"> ${userList.guestNm} </td>
					
					<c:forEach var="timeList" begin="6" end="21" step="1">
						<c:set var="flag" value="false"/>
						<c:forEach var="guestUseTime" items="${guestUseDay}" varStatus="status">
								<c:if test="${userList.guestUid == guestUseTime.userUid}">
									<c:if test="${guestUseTime.alldayYn eq 'Y' || (guestUseTime.startHour <= timeList && guestUseTime.endHour > timeList )}">
										<c:set var="flag" value="true"/>
									</c:if>
								</c:if>
						</c:forEach>
						<c:if test="${flag == true}">
							<td bgcolor=#ff99cc></td>
						</c:if>
						<c:if test="${flag == false}">
							<td bgcolor=#ccfffff></td>
						</c:if>
					</c:forEach>
				</tr>
			</c:forEach>

	</u:listArea>
</div>
</c:forEach>
<div class="front">
<div class="front_right">
	<table border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
		<td width="35px" height="20px" bgcolor=#ff99cc>
		
	 	</td>
	 	<td> <u:msg titleId="wc.cols.time.use" alt="사용중"/> </td>
	 	<td width="10px"></td>
	 	<td width="35px" bgcolor=#ccfffff>
	 	</td>
	 	<td> <u:msg titleId="wc.cols.time.empty" alt="빈시간"/> </td>
	</tr>
	</tbody></table>
</div>
</div>


<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
