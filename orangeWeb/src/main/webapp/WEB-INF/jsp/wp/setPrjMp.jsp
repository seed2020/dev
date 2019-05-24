<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	// 공수입력칸 넓이
	request.setAttribute("cWidth", 28);

%><script type="text/javascript">
<!--<%--
[클릭] 테이블 [월] 클릭 --%>
function toggleTr(obj){
	if(isClosed()) return;
	
	var tr = getParentTag(obj, 'tr');
	var chkd = $(tr).attr('data-chkd') != 'Y';
	
	$(tr).find("td.body_ct").each(function(){
		if(chkd){
			if($(this).attr('data-holiday')!='Y'){
				$(this).addClass('prj_chkd');
			}
		} else {
			if($(this).attr('data-holiday')!='Y'){
				$(this).removeClass('prj_chkd');
			} else {
				$(this).removeClass('prj_chkd');
				$(this).css('background-color', $(this).attr('data-color'));
			}
		}
	});
	$(tr).attr('data-chkd', chkd ? 'Y' : '');
}<%--
[클릭] 테이블 각각의 [셀] 클릭 --%>
function toggleTd(obj){
	if(isClosed()) return;
	
	if($(obj).hasClass('prj_chkd')){
		$(obj).removeClass('prj_chkd');
		if($(obj).attr('data-holiday')=='Y'){
			$(obj).css('background-color', $(obj).attr('data-color'));
		}
	} else {
		$(obj).addClass('prj_chkd');
		if($(obj).attr('data-holiday')=='Y'){
			$(obj).css('background-color', '');
		}
	}
}
function isClosed(){
	return "${not empty wpPrjBVo.cmplYmd ? 'Y' : ''}" == 'Y';
}<%--
[일괄변경 팝업] 열기 --%>
function openRsltMdPop(){
	dialog.open("setRsltMdDialog", '<u:msg titleId="cm.changAll" alt="일괄변경" />', "./setRsltMdPop.do?menuId=${menuId}&cat=${param.cat}");
}<%--
[일괄변경 팝업] 확인 --%>
function setPrjMp(){
	var pop = $("#joinMpArea");
	var rsltMd = pop.find("[name='rsltMd']:checked").val();
	var note = pop.find("[name='note']").val();
	
	if(rsltMd=='0'){
		rsltMd = '';
		note = '';
	}
	$("#yearlyMpArea td.prj_chkd").each(function(){
		$(this).text(rsltMd);
		$(this).attr("title", note);
		$(this).removeClass('prj_chkd');
		if($(this).attr('data-holiday')=='Y'){
			$(this).css('background-color', $(this).attr('data-color'));
		}
	});
	dialog.close("setRsltMdDialog");
	$("#yearlyMpArea tr").attr('data-chkd', '');
}<%--
[저장] 버튼 클릭 --%>
function savePrjMp(){
	var arr = [], rsltMd, note, obj;
	var $tds = $("#yearlyMpArea td.body_ct");
	$tds.each(function(){
		rsltMd = $(this).text().trim();
		if(rsltMd=='1.0') rsltMd = '1';
		note = $(this).attr('title');
		if(rsltMd!='' && $(this).attr('data-rsltYmd')!=null){
			obj = {rsltYmd:$(this).attr('data-rsltYmd'), rsltMd:rsltMd};
			if(note!=null && note!='') obj['note'] = note;
			arr.push(obj);
		}
	});
	
	var workDays = [0,0,0,0,0,0,0,0,0,0,0,0];
	$("#yearlyMpArea tr").each(function(){
		var month = $(this).attr('data-month');
		if(month!=null){
			var m = parseInt(month);
			$(this).find('td.body_ct').each(function(){
				if($(this).attr('data-holiday')!='Y'){
					workDays[m-1]++;
				}
			});
		}
	});
	
	
	var $frm = $("#prjMpSaveFrm");
	$frm.find("[name=data]").val(JSON.stringify(arr));
	$frm.find("[name=workDays]").val(workDays.join(','));
	
	$frm.attr('action', './transPrjMp.do');
	$frm.attr('target', 'dataframe'); 
	$frm.submit();
}<%--
[년도 변경] --%>
function changeYear(year){
	var frm = $("#prjMpYearFrm");
	frm.attr("action", "${_uri}");
	frm.find("input[name='year']").val(year);
	frm.submit();
}<%--
[투입인력선택] select 변경 --%>
function changeManPower(mpId){
	var frm = $("#prjMpYearFrm");
	frm.attr("action", "${_uri}");
	frm.find("input[name='mpId']").val(mpId);
	frm.submit();
}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<u:secu auth="A"><u:set test="${true}" var="isAdm" value="Y" /></u:secu>
<style>.prj_chkd {background-color : #C1E6E3}</style>
<u:title alt="공수 관리" menuNameFirst="true" />

<u:listArea id="prjArea" colgroup="13%,37%,13%,37%">
<tr>
	<td class="head_ct"><u:msg titleId="wp.prjCd" alt="프로잭트 코드" /></td>
	<td class="body_lt"><u:out value="${wpPrjBVo.prjCd}" /></td>
	<td class="head_ct"><u:msg titleId="wp.prjNm" alt="프로잭트 명" /></td>
	<td class="body_lt"><u:out value="${wpPrjBVo.prjNm}" /></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="wp.pred" alt="기간" /></td>
	<td class="body_lt"><table border="0" cellpadding="0" cellspacing="0"><tr>
		<td><u:out value="${wpPrjBVo.strtYmd}" type="shortdate" /></td>
		<td style="padding-left:8px; padding-right:5px;">~</td>
		<td><u:out value="${wpPrjBVo.endYmd}" type="shortdate" /></td>
		</tr></table></td>
	<td class="head_ct"><u:msg titleId="wp.endDt" alt="종료일" /></td>
	<td class="body_lt"><u:out value="${wpPrjBVo.cmplYmd}" type="shortdate" /></td>
</tr>
</u:listArea>

<div style="position:relative;">
<table style="width:90px; left:46%; position:relative;" border="0" cellspacing="0" cellpadding="0"><tbody>
	<tr>
		<td class="frontico notPrint"><a href="javascript:changeYear(${year-1});"><img width="20" height="20" src="/images/blue/ico_left.png"></a></td>
		<td style="width: 9px;">&nbsp;</td>
		<td style="margin:0px; padding: 5px 0px 0px 2px; font-weight:bold">${year}</td>
		<td style="width: 9px;">&nbsp;</td>
		<td class="frontico notPrint"><a href="javascript:changeYear(${year+1});"><img width="20" height="20" src="/images/blue/ico_right.png"></a></td>
	</tr>
</tbody></table><c:if test="${empty wpPrjBVo.cmplYmd and 
	(not empty coverOuts or wpPrjBVo.pmId eq sessionScope.userVo.userUid)}">
<table style="position:absolute; right:0px; top:0px;" border="0" cellspacing="0" cellpadding="0" class="notPrint"><tbody>
	<tr>
		<td><u:msg titleId="wp.chooseManPowerIn" alt="투입 인력 선택" /> : </td>
		<td style="width: 4px;">&nbsp;</td>
		<td><select id="changeMp" onchange="changeManPower(this.value);"><c:forEach
				items="${wpPrjMpPlanDVoList}" var="wpPrjMpPlanDVo"><c:if
				test="${wpPrjMpPlanDVo.mpId eq sessionScope.userVo.userUid or wpPrjMpPlanDVo.mpTypCd eq 'out'}">
			<u:option value="${wpPrjMpPlanDVo.mpId}" title="${wpPrjMpPlanDVo.mpNm}" checkValue="${mpId}" /></c:if></c:forEach></select></td>
	</tr>
</tbody></table></c:if>
</div>
<div class="blank"></div>

<div id="yearlyMpArea">
<table class="listtable" border="0" cellspacing="1" cellpadding="0" style="width:${(31*cWidth)+60}px;; margin-top:-1px; table-layout: fixed;">
<tr style="height:27px;">
	<td class="head_ct" style="width:60px; text-align:center;">&nbsp;</td>
	<c:forEach begin="1" end="31" step="1" var="day">
	<td class="head_ct" style="width:${cWidth+2}px; border-right:${ day % 5 eq 0 ? '1px solid lightgrey' : ''}">${day}</td>
	</c:forEach>
</tr>
</table><c:forEach begin="0" end="11" step="1" var="month">
<table class="listtable" border="0" cellspacing="1" cellpadding="0" style="width:${(lastDayOfMonth[month]*cWidth)+60}px;; margin-top:-1px; table-layout: fixed;">
<tr data-month="${month+1}" style="height:27px; cursor:pointer" onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="head_ct" style="width:60px;" onclick="toggleTr(this);"><u:msg titleId="cm.m.${month+1}" alt="1~12월" /></td><u:set
		test="true" var="week" value="${weekOfFirstDay[month]}" />
	<c:forEach begin="1" end="${lastDayOfMonth[month]}" step="1" var="day"><u:set test="true"
		var="rsltYmd" value="${year}-${month<9 ? '0'.concat(month+1) : month+1}-${day<10 ? '0'.concat(day) : day}" />
	<td class="body_ct" data-holiday="${
		week eq 7 or week eq 1 or holidayList.contains(rsltYmd) ? 'Y' : ''}" data-rsltYmd="${
		rsltYmd}" style="width:${cWidth}px; text-align:center; background-color:${
		week eq 1 or holidayList.contains(rsltYmd) ? '#FFF2F2' : week eq 7 ? '#EEF1FF' : ''
		}; font-weight:bold; color:#525252; border-right:${ day % 5 eq 0 ? '1px solid lightgrey' : ''};" data-color="${
		week eq 1 or holidayList.contains(rsltYmd) ? '#FFF2F2' : week eq 7 ? '#EEF1FF' : ''
		}" onclick="toggleTd(this)" title="<u:out value='${wpPrjMpRsltLVoMap.get(rsltYmd).note}' type='value' />">${wpPrjMpRsltLVoMap.get(rsltYmd).rsltMd}</td><u:set
		test="${week eq 7}" var="week" value="1" elseValue="${week + 1}" />
	</c:forEach>
</tr>
</table></c:forEach>
</div>
<div class="blank"></div>

<form id="prjMpYearFrm">
<input type="hidden" name="menuId" value="${menuId}">
<input type="hidden" name="cat" value="${param.cat}">
<input type="hidden" name="prjNo" value="${param.prjNo}">
<input type="hidden" name="mpId" value="${param.mpId}">
<input type="hidden" name="year" value="">
</form>
<form id="prjMpSaveFrm" method="post">
<input type="hidden" name="menuId" value="${menuId}">
<input type="hidden" name="cat" value="${param.cat}">
<input type="hidden" name="prjNo" value="${param.prjNo}">
<input type="hidden" name="mpId" value="${param.mpId}">
<input type="hidden" name="year" value="${year}">
<input type="hidden" name="data" value="">
<input type="hidden" name="workDays" value="">
</form>

<u:buttonArea><c:if
		test="${empty wpPrjBVo.cmplYmd}">
	<u:button titleId="cm.changAll" alt="일괄변경" onclick="openRsltMdPop()" auth="W" />
	<u:button titleId="cm.btn.save" alt="저장" onclick="savePrjMp()" auth="W" /></c:if>
	<u:button titleId="cm.btn.close" alt="닫기" href="./listPrj.do?menuId=${menuId}&cat=${param.cat}" />
</u:buttonArea>