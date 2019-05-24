<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"%>
<jsp:useBean id="now" class="java.util.Date" />


<u:set test="${!empty param.tabNo}" var="tabNo" value="${param.tabNo}" elseValue="0" />
<u:set test="${!empty param.fncCal}" var="fncCal" value="${param.fncCal}" elseValue="psn" />


<script type="text/javascript">


<!--
//나의일정검색 viewProm값 전달
function viewSearchMyScd(){//return : {subj, loc, dt, openGradCd, cont, scds_promGstUids, scds_promGstNms, scds_promGstDptNms}
	var scds_promGstNms = [];
	var scds_promGstDptNms = [];
	var scds_promGstUids = [];
	var tabNo = $("#tabNo").val();
	var $selectRow;
	
	if(tabNo == "0"){
		$selectRow=$("#"+$("#scds_schdlId").val() +"_month");
	}else if(tabNo == "1"){
		$selectRow=$("#"+$("#scds_schdlId").val() +"_week");
	}else if(tabNo == "2"){
		$selectRow=$("#"+$("#scds_schdlId").val() +"_day");
	}
	
	//var $selectRow=$("#"+$("#scds_schdlId").val() +"_month");
	//수정,삭제버튼 block flag;
	var searchMylist = null;
	
	var $scds_schdlId = $("#scds_schdlId").val();
	
	var $scds_subj = $selectRow.find("#scds_tooltip_subj").val();
	var $scds_locNm = $selectRow.find("#scds_tooltip_locNm").val();
	//view Prom Date
	var $scds_Dt = $selectRow.find("#scds_tooltip_schdlStartDay").val() + " ~ " + $selectRow.find("#scds_tooltip_schdlEndDay").val() + " (" + $selectRow.find("#scds_tooltip_schdlStartDt").val() + " ~ "+ $selectRow.find("#scds_tooltip_schdlEndDt").val() + ")";

	var $scds_openGradCd = $selectRow.find("#scds_openGradCd").val();
	
	var $scds_cont = $selectRow.find("#scds_tooltip_cont").val();
	var $scds_startYmd = $selectRow.find("#scds_tooltip_schdlStartDay").val();
	var $scds_endYmd = $selectRow.find("#scds_tooltip_schdlEndDay").val();
	var $scds_startHHmm = $selectRow.find("#scds_tooltip_schdlStartDt").val();
	var $scds_endHHmm = $selectRow.find("#scds_tooltip_schdlEndDt").val();
	
	
	var $workCmltYmd = $selectRow.find("#scds_workCmltYmd").val();
	
	
	var $scds_holiYn = $selectRow.find("#scds_holiYn").val();
	var $scds_solaLunaYn = $selectRow.find("#scds_solaLunaYn").val();
	//할일우선순위
	var $scds_workPrioOrdr =  $selectRow.find("#scds_workPrioOrdr").val();
	var $scds_schdlStatCd =  $selectRow.find("#scds_schdlStatCd").val();
	
	var $selctCol = $selectRow.find("#scds_promGstUid");
	
	
	 $selctCol.find("#scds_promGstUid").each(function(){
		 scds_promGstUids.push($(this).val());
	 });
	 
	 $selctCol.find("#scds_promGstNm").each(function(){
		 scds_promGstNms.push($(this).val());
	 });

	 $selctCol.find("#scds_promGstDptNm").each(function(){
		 scds_promGstDptNms.push($(this).val()); 
	 });
	
	return {subj:$scds_subj, loc:$scds_locNm, dt:$scds_Dt,  openGradCd:$scds_openGradCd, cont:$scds_cont, workPrioOrdr:$scds_workPrioOrdr,solaLunaYn:$scds_solaLunaYn,
			startYmd:$scds_startYmd, endYmd:$scds_endYmd ,schdlStatCd:$scds_schdlStatCd,startHHmm:$scds_startHHmm, endHHmm:$scds_endHHmm,holiYn:$scds_holiYn,workCmltYmd:$workCmltYmd,
			scds_promGstUids:scds_promGstUids, scds_promGstNms:scds_promGstNms, scds_promGstDptNms:scds_promGstDptNms, searchMylist:searchMylist, schdlId:$scds_schdlId};
}

function betweenCalDay (startYmd, endYmd){

	var startDateArray = startYmd.split("-");
	var endDateArray = endYmd.split("-");  
	
	var startDateObj = new Date(startDateArray[0], Number(startDateArray[1])-1, startDateArray[2]);
	var endDateObj = new Date(endDateArray[0], Number(endDateArray[1])-1, endDateArray[2]);
	
	var betweenDay = (endDateObj.getTime() - startDateObj.getTime())/1000/60/60/24; 
	//ex) (2014-05-20) - (2014-05-15) = 5일이지만 당일 포함 6일간 이니 +1 해줌
	return betweenDay + 1;
}

function selectTab(tabNoRT){
	$("#tabNo").val(tabNoRT);
}

function selectMonth(param){
	
	$('#action').val(param);
	
	var $form = $('#calendarPrintForm');
	
	if($('#submitTab').val() == 'molyList'){
		
		$form.attr('method','post');
		$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$('#tabNo').val('0');
		$form.submit();
	}else if($('#submitTab').val() == 'welyList'){
		$form.attr('method','post');
		$('#tabNo').val('1');
		$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
	}else if($('#submitTab').val() == 'dalyList'){
		$form.attr('method','post');
		$('#tabNo').val('2');
		$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
	}
	
}


function applyTooltip() {
	
	
	$('.scd_promise,.scd_work,.scd_event,.scd_anniversary,.scd_prev').mouseover(function(event) {
		$('#tooltip').css('top', event.pageY - 85);
		$('#tooltip').css('left', event.pageX - 230);
		$('#tooltip').show();
		
		var $scdObj=$("#"+$(this).attr("id"));
		var $tooltip_title=$("#tooltip_title");
		$tooltip_title.html($scdObj.find("#scds_tooltip_subj").val());
		var $tooltip_registrant = $("#tooltip_registrant");
 		$tooltip_registrant.html("예약자 : " + 	$scdObj.find("#scds_tooltip_regNm").val());
		var $tooltip_startToEndDay = $("#tooltip_startToEndDay");
		$tooltip_startToEndDay.html("예약일자 : " + $scdObj.find("#scds_tooltip_schdlStartDay").val().replace(/-/g, ".") + " ~ " + $scdObj.find("#scds_tooltip_schdlEndDay").val().replace(/-/g, "."));
		var $tooltip_startToEndTime = $("#tooltip_startToEndTime");
 		$tooltip_startToEndTime.html("예약시간 : " + 	$scdObj.find("#scds_tooltip_schdlStartDt").val() + " ~ " + $scdObj.find("#scds_tooltip_schdlEndDt").val());
		//var $tooltip_content = $("#tooltip_content");
 		//$tooltip_content.html("내용 : " + 	$scdObj.find("#scds_tooltip_cont").val());
		
	});
	$('.scd_promise,.scd_work,.scd_event,.scd_anniversary,.scd_prev').mouseout(function(event) {
		$('#tooltip').hide();        
	});
}


function viewProm(scds_schdlId) {
	//schdl 값 알아내기 위해서 셋팅
	$("#scds_schdlId").val(scds_schdlId);
	dialog.open('viewPromPop','<u:msg titleId="wc.jsp.viewProm.title" alt="약속조회"/>','./viewPromPop.do?menuId=${menuId}&fncCal=${fncCal}&scds_schdlId='+scds_schdlId+''); 
}

function viewWork(scds_schdlId) {
	//schdl 값 알아내기 위해서 셋팅
	$("#scds_schdlId").val(scds_schdlId);
	dialog.open('viewWorkPop','<u:msg titleId="wc.jsp.viewWork.title" alt="할일조회"/>','./viewWorkPop.do?menuId=${menuId}&fncCal=${fncCal}&scds_schdlId='+scds_schdlId+'');
}

function viewEvnt(scds_schdlId) {
	//schdl 값 알아내기 위해서 셋팅
	$("#scds_schdlId").val(scds_schdlId);
	dialog.open('viewEvntPop','<u:msg titleId="wc.jsp.viewEvnt.title" alt="행사조회"/>','./viewEvntPop.do?menuId=${menuId}&fncCal=${fncCal}&scds_schdlId='+scds_schdlId+'');
}

function viewAnnv(scds_schdlId) {
	//schdl 값 알아내기 위해서 셋팅
	$("#scds_schdlId").val(scds_schdlId);
	dialog.open('viewAnnvPop','<u:msg titleId="wc.jsp.viewAnnv.title" alt="기념일조회"/>','./viewAnnvPop.do?menuId=${menuId}&fncCal=${fncCal}&scds_schdlId='+scds_schdlId+'');
}

function setButtonSrc(img, on) {
	var src = $('#' + img + ' img').attr('src');
	if (on) src = src.replace(img, img + '_on');
	else	src = src.replace(img + '_on', img);
	$('#' + img + ' img').attr("src", src);
}

function toggleButtons(sel) {
	var src = $('#' + sel + ' img').attr('src');
	if (src.indexOf('_on') == -1) {
		setButtonSrc('scd_all', (sel == 'scd_all'));
		<c:if test="${fncCal == 'psn' || fncCal == 'my'}">
		setButtonSrc('scd_promise', (sel == 'scd_promise'));
		setButtonSrc('scd_work', (sel == 'scd_work'));
		</c:if>
		setButtonSrc('scd_event', (sel == 'scd_event'));
		setButtonSrc('scd_anniversary', (sel == 'scd_anniversary'));
	}
}

function showAll() {
	toggleButtons('scd_all');
	var notPsn = ${fncCal == 'psn' || fncCal == 'my'} ? '' : 'none';

	$("#dayPromiseRow").css('height', '');
	$("#dayWorkRow").css('height', '');
	$("#dayEventRow").css('height','');
	$("#dayAnnvRow").css('height', '');
	
	$(".scd_empty")			.css('display', '').parent().css('display', '');	
	$('.scd_promise')		.css('display', notPsn).parent().parent().css('display', notPsn);	
	$('.scd_work')			.css('display', '').parent().parent().css('display', '');
	$('.scd_event')			.css('display', '').parent().parent().css('display', '');
	$('.scd_anniversary')	.css('display', '').parent().parent().css('display', '');
	$('.scd_prev')			.css('display', '').parent().parent().css('display', '');
}
function showProm() {
	toggleButtons('scd_promise');
	
	$("#dayPromiseRow").css('height', $("#dayPromiseRow").height()+"px");
	$("#dayWorkRow").css('height', $("#dayWorkRow").height()+"px");
	$("#dayEventRow").css('height', $("#dayEventRow").height()+"px");
	$("#dayAnnvRow").css('height', $("#dayAnnvRow").height()+"px");
		
	$(".scd_empty")			.css('display', 'none').parent().css('display', 'none');	
	$('.scd_promise')		.css('display', '').parent().parent().css('display', '');	
	$('.scd_work')			.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_event')			.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_anniversary')	.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_prev')			.css('display', 'none').parent().parent().css('display', 'none');
	
}
function showWork() {
	toggleButtons('scd_work');

	$("#dayPromiseRow").css('height', $("#dayPromiseRow").height()+"px");
	$("#dayWorkRow").css('height', $("#dayWorkRow").height()+"px");
	$("#dayEventRow").css('height', $("#dayEventRow").height()+"px");
	$("#dayAnnvRow").css('height', $("#dayAnnvRow").height()+"px");
	
	$(".scd_empty")			.css('display', 'none').parent().css('display', 'none');	
	$('.scd_promise')		.css('display', 'none').parent().parent().css('display', 'none');	
	$('.scd_work')			.css('display', '').parent().parent().css('display', '');
	$('.scd_event')			.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_anniversary')	.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_prev')			.css('display', 'none').parent().parent().css('display', 'none');
}
function showEvnt() {
	toggleButtons('scd_event');

	$("#dayPromiseRow").css('height', $("#dayPromiseRow").height()+"px");
	$("#dayWorkRow").css('height', $("#dayWorkRow").height()+"px");
	$("#dayEventRow").css('height', $("#dayEventRow").height()+"px");
	$("#dayAnnvRow").css('height', $("#dayAnnvRow").height()+"px");
	
	$(".scd_empty")			.css('display', 'none').parent().css('display', 'none');	
	$('.scd_promise')		.css('display', 'none').parent().parent().css('display', 'none');	
	$('.scd_work')			.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_event')			.css('display', '').parent().parent().css('display', '');
	$('.scd_anniversary')	.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_prev')			.css('display', 'none').parent().parent().css('display', 'none');
}
function showAnnv() {
	toggleButtons('scd_anniversary');

	$("#dayPromiseRow").css('height', $("#dayPromiseRow").height()+"px");
	$("#dayWorkRow").css('height', $("#dayWorkRow").height()+"px");
	$("#dayEventRow").css('height', $("#dayEventRow").height()+"px");
	$("#dayAnnvRow").css('height', $("#dayAnnvRow").height()+"px");
	
	$(".scd_empty")			.css('display', 'none').parent().css('display', 'none');	
	$('.scd_promise')		.css('display', 'none').parent().parent().css('display', 'none');	
	$('.scd_work')			.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_event')			.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_anniversary')	.css('display', '').parent().parent().css('display', '');
	$('.scd_prev')			.css('display', 'none').parent().parent().css('display', 'none');
}

function changeDt() {
	alert(1);
	$('#calendar').css('right', '10px');
	$('#calendar').css('top', '185px');
	$('#calendar').toggle();
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
	
	var arr = {
		'molyList': "${wcScdCalMonth.year}" + "-"  + convMolyMonth,
		'welyList': "${wcScdCalWeek.year}" + "-"  + convWelyMonth + " " +"${wcScdCalWeek.week}"  +"주차",
		'dalyList':"${wcScdCalDay.year}" + "-"  + convDalyMonth + "-" + convDalyDay,
		};
	$('#todayTd').html(arr[areaId]);
}

function initTodayTd(tabNo) {
	$('#schdlTab .tab_left a').click(function(event) {
		setTodayTd($(this).parent().attr('data-areaid'));
	});
	$('#schdlTab .tab_left a').eq(tabNo).click();
}

function findOther(fncCal,event,opt) {
	if (fncCal == 'psn')
		//openSingUser();
		searchOthrUserTop(event, opt, "calendarPrintForm");
	else
		openSingOrg();
}


//1명의 사용자 선택
function openSingUser(){
	var $view = $("#calendarPrintForm");
	var data = {userUid:$view.find("#viewUserUid").val()};
	
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$view.find("#viewUserUid").val(userVo.userUid);
			$view.find("#viewUserNm").val(userVo.rescNm);
			$view.find("#othr").val(userVo.rescNm);
			$("#bumk option:eq(0)").attr("selected", "selected");
			$("#agnt option:eq(0)").attr("selected", "selected");
			var $form = $('#calendarPrintForm');
			if($('#submitTab').val() == 'molyList'){
				
				$form.attr('method','post');
				$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
				$('#tabNo').val('0');
				$form.submit();
			}else if($('#submitTab').val() == 'welyList'){
				$form.attr('method','post');
				$('#tabNo').val('1');
				$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
				$form.submit();
			}else if($('#submitTab').val() == 'dalyList'){
				$form.attr('method','post');
				$('#tabNo').val('2');
				$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
				$form.submit();
			}
		}
	});
	
}



//1 부서 선택
function openSingOrg(){
	var $view = $("#calendarPrintForm");
	var data = {orgId:$view.find("#viewOrgId").val()};
	
	searchOrgPop({data:data}, function(orgVo){
		if(orgVo!=null){
			$view.find("#viewOrgId").val(orgVo.orgId);
			$view.find("#viewOrgNm").val(orgVo.rescNm);
			$view.find("#othr").val(orgVo.rescNm);
			var $form = $('#calendarPrintForm');
			$("#bumk option:eq(0)").attr("selected", "selected");
			if($('#submitTab').val() == 'molyList'){
				
				$form.attr('method','post');
				$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
				$('#tabNo').val('0');
				$form.submit();
			}else if($('#submitTab').val() == 'welyList'){
				$form.attr('method','post');
				$('#tabNo').val('1');
				$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
				$form.submit();
			}else if($('#submitTab').val() == 'dalyList'){
				$form.attr('method','post');
				$('#tabNo').val('2');
				$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
				$form.submit();
			}
		}
		//return false;// 창이 안닫힘
	});
}


function agntSelect(){
	$("#bumk option:eq(0)").attr("selected", "selected");
	$("#viewUserUid").val("");
	$("#viewUserNm").val("");
	$("#viewOrgId").val("");
	$("#viewOrgNm").val("");
	$("#othr").val("");
	var $form = $('#calendarPrintForm');
	if($('#submitTab').val() == 'molyList'){
		
		$form.attr('method','post');
		$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$('#tabNo').val('0');
		$form.submit();
	}else if($('#submitTab').val() == 'welyList'){
		$form.attr('method','post');
		$('#tabNo').val('1');
		$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
	}else if($('#submitTab').val() == 'dalyList'){
		$form.attr('method','post');
		$('#tabNo').val('2');
		$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
	}
	
	
}

function bumkSelect(){
	
	$("#agnt option:eq(0)").attr("selected", "selected");
	$("#viewUserUid").val("");
	$("#viewUserNm").val("");
	$("#viewOrgId").val("");
	$("#viewOrgNm").val("");
	$("#othr").val("");
	var $form = $('#calendarPrintForm');
	if($('#submitTab').val() == 'molyList'){
		
		$form.attr('method','post');
		$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$('#tabNo').val('0');
		$form.submit();
	}else if($('#submitTab').val() == 'welyList'){
		$form.attr('method','post');
		$('#tabNo').val('1');
		$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
	}else if($('#submitTab').val() == 'dalyList'){
		$form.attr('method','post');
		$('#tabNo').val('2');
		$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
	}
	
	
}



function setBumpPopOpen(){
	if($("#othr").val()!=''){
		var url="./setBumkPop.do?menuId=${menuId}&fncCal=${fncCal}&viewOrgId"+$("#viewOrgId").val()+"&viewUserUid="+$("#viewUserUid").val()
				+"&viewUserNm="+encodeURIComponent($("#viewUserNm").val())+"&viewOrgNm="+encodeURIComponent($("#viewOrgNm").val());
		
		dialog.open('setBumkPop','<u:msg titleId="wc.cols.bumkAdd" alt="즐겨찾기 추가"/>',url);
	}else{
		alert("<u:msg titleId="wc.msg.not.selTarget" alt="조회 대상 선택후 재시도 해주세요."/>");
	}
}



function selectMoveDate(date){
	
	
	var year=date.split('-')[0];
	var month=date.split('-')[1];
	var day=date.split('-')[2];
	
	$('#action').val('');	
	var $form = $('#calendarPrintForm');
	
	if($('#submitTab').val() == 'molyList'){	
		$("#molyYear").val(year);
		$("#molyMonth").val(month);
		$form.attr('method','post');
		$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$('#tabNo').val('0');
		$form.submit();
	}else if($('#submitTab').val() == 'welyList'){
		$("#welyYear").val(year);
		$("#welyMonth").val(month);
		$("#welyWeek").val(getSecofWeek(date));
		$form.attr('method','post');
		$('#tabNo').val('1');
		$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
	}else if($('#submitTab').val() == 'dalyList'){
		$("#dalyYear").val(year);
		$("#dalyMonth").val(month);
		$("#dalyDay").val(day);
		$form.attr('method','post');
		$('#tabNo').val('2');
		$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
	}
	
}

function getSecofWeek(date){
	
	var year=date.split('-')[0];
	var month=date.split('-')[1];
	var day=date.split('-')[2];
	
  
	var fd = new Date( year, parseInt(month)-1, 1 );
    return Math.ceil((parseInt(day)+fd.getDay())/7);

}


//타인일정 임직원 검색
var gTopUserSearchObj = null;
var gTopUserSearchTxt = null;
function searchOthrUserTop(event, eventOpt, formId){
	var $view = $("#calendarPrintForm");
	if(gTopUserSearchObj==null){
		gTopUserSearchObj = $("#"+formId+" #othr").first();
		gTopUserSearchTxt = gTopUserSearchObj.attr("title");
	}
	if(eventOpt=='focus'){
		if(gTopUserSearchObj.val()==gTopUserSearchTxt){
			gTopUserSearchObj.val('');
		}
	} else if(eventOpt=='blur') {
		if(gTopUserSearchObj.val()==''){
			gTopUserSearchObj.val(gTopUserSearchTxt);
		}
	} else if(eventOpt=='keydown' || eventOpt=='click') {
		if(eventOpt=='keydown' && event!=null){
			if(event.keyCode!=13) return;
			if(event.preventDefault) event.preventDefault();
			if(browser.firefox){
				window.setTimeout("searchOthrUserTop(null, 'click', 'calendarPrintForm')",10);
				return;
			}
		}
		var param = new ParamMap().getData(formId);
		
		param.put("userNm",param.get('othr'));
		
		dialog.open('searchUserDialog', gTopUserSearchTxt, "/or/user/searchUserPop.do?opt=single&"+param.toQueryString());
		gUserHandler=function(userVo){
			if(userVo!=null){
				$view.find("#viewUserUid").val(userVo.userUid);
				$view.find("#viewUserNm").val(userVo.rescNm);
				$view.find("#othr").val(userVo.rescNm);
				$("#bumk option:eq(0)").attr("selected", "selected");
				$("#agnt option:eq(0)").attr("selected", "selected");
				var $form = $('#calendarPrintForm');
				if($('#submitTab').val() == 'molyList'){
					
					$form.attr('method','post');
					$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
					$('#tabNo').val('0');
					$form.submit();
				}else if($('#submitTab').val() == 'welyList'){
					$form.attr('method','post');
					$('#tabNo').val('1');
					$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
					$form.submit();
				}else if($('#submitTab').val() == 'dalyList'){
					$form.attr('method','post');
					$('#tabNo').val('2');
					$form.attr('action','/wc/listPsnSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
					$form.submit();
				}
			}
		}
	}
}



$(document).ready(function() {
	setUniformCSS();
	changeTab('schdlTab','${tabNo}');
	initTodayTd('${tabNo}');
	showAll();
	applyTooltip();
});
//-->
</script>

<u:title titleId="wc.jsp.listPsnSchdl.${fncCal}.title" alt="개인 일정" menuNameFirst="true"/>
<input id="scds_promGstUid" type="hidden" value=""/>
<input id="scds_promGstNm" type="hidden" value=""/>
<input id="scds_promGstDptNm" type="hidden" value=""/>

<% // TAB %>
<div id="tabDiv" class="notPrint">
	<u:tabGroup id="schdlTab" >
		<u:tab id="schdlTab" areaId="molyList" titleId="wc.jsp.listPsnSchdl.tab.molySchdl" alt="월간일정" on="true" onclick="selectTab(0)" />
		<u:tab id="schdlTab" areaId="welyList" titleId="wc.jsp.listPsnSchdl.tab.welySchdl" alt="주간일정" onclick="selectTab(1)"/>
		<u:tab id="schdlTab" areaId="dalyList" titleId="wc.jsp.listPsnSchdl.tab.dalySchdl" alt="일간일정" onclick="selectTab(2)"/>
		<c:if test="${fncCal == 'psn' || fncCal == 'my'}">	
			<u:tabButton titleId="wc.btn.promReg" alt="약속등록" href="javascript:dialog.open('setPromPop','약속등록','./setPromPop.do?menuId=${menuId}');"/>
			<u:tabButton titleId="wc.btn.workReg" alt="할일등록" href="javascript:dialog.open('setWorkPop','할일등록','./setWorkPop.do?menuId=${menuId}');"/>
		</c:if>
		
		<u:tabButton titleId="wc.btn.evntReg" alt="행사등록" href="javascript:dialog.open('setEvntPop','행사등록','./setEvntPop.do?menuId=${menuId}&fncCal=${fncCal}');"/>
		<u:tabButton titleId="wc.btn.annvReg" alt="기념일등록" href="javascript:dialog.open('setAnnvPop','기념일등록','./setAnnvPop.do?menuId=${menuId}&fncCal=${fncCal}');"/>
		<c:if test="${fncCal == 'my'}">
			<u:tabButton titleId="cm.btn.setup" alt="설정" href="javascript:dialog.open('setMySetupPop','설정','./setMySetupPop.do?menuId=${menuId}');"/>
		</c:if>
		<c:if test="${fncCal == 'my' || fncCal == 'grp'}">
			<u:tabButton titleId="wc.btn.grpChoi" alt="그룹선택" href="javascript:dialog.open('setGrpChoiPop','그룹선택','./setGrpChoiPop.do?menuId=${menuId}');"	/>
		</c:if>
		<u:tabButton titleId="cm.btn.print" alt="인쇄" img="ico_print.png" imgW="14" onclick="printWeb();" />
	</u:tabGroup>
</div>

<% // 상단 FRONT %>
<div class="front">

	<div class="front_left">
		
	<form method="post" id="calendarPrintForm" name ="calendarPrintForm">
			
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
		
	<div id="choiGrpDiv" name="choiGrpDiv" style="display:none">
			<c:forEach  var="choiGrpIdItem" items="${choiGrpIds}" varStatus="status">	
				<input id='choiGrpIds' name='choiGrpIds' type='hidden' value='${choiGrpIdItem}'>
			</c:forEach>
			<c:forEach  var="choiGrpNmItem" items="${choiGrpNms}" varStatus="status">
				<input id='choiGrpNms' name='choiGrpNms' type='hidden' value='${choiGrpNmItem}'>
			</c:forEach>
			<input id='grpResetFlag' name='grpResetFlag' type='hidden' value='${grpResetFlag}'>
			
	</div>	
	<table border="0" cellpadding="0" cellspacing="0">
	<tr>
	
	<td class="frontico"><a href="javascript:selectMonth('before')"><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
	<td id="todayTd" class="scd_head">&nbsp;</td>
	<td class="frontico"><a href="javascript:selectMonth('after')"><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
	<c:if test="${fncCal == 'psn'}">
	<td class="frontinput">
		<select id="agnt"  name="agnt" onChange="agntSelect();">
		<option value="-1"><u:msg titleId="wc.option.proxySchdl" alt="일정 대리수행"/></option>
		<c:forEach  var="agntItem" items="${wcAgntVos}" varStatus="status">			
			<option value="${agntItem.userUid}" <c:if test="${agntItem.userUid == agnt}">selected</c:if>>${agntItem.rescNm}</option>
		</c:forEach>
		</select></td>
	</c:if>
	<c:if test="${fncCal == 'psn' || fncCal == 'dept'}">
	<u:set test="${fncCal == 'psn'}" var="othrTitle" value="타인일정" elseValue="타부서일정" />
	<td class="fronttit">${othrTitle}</td>
	<td class="frontinput"><u:input id="othr" value="${othr}" titleId="cols.othr" onfocus="searchOthrUserTop(event, 'focus', 'calendarPrintForm')" onblur="searchOthrUserTop(event, 'blur', 'calendarPrintForm')" onkeydown="searchOthrUserTop(event, 'keydown', 'calendarPrintForm')"  /></td>
	<td class="frontbtn"><u:buttonS titleId="cm.btn.read" alt="조회" href="javascript:findOther('${fncCal}',event,'click');" /></td>
	<td class="frontinput"  >
		<select id="bumk" name="bumk" onChange="bumkSelect();">
		<option value="-1"><u:msg titleId="wc.cols.bumkTarget" alt="즐겨찾기"/></option>
		<c:forEach  var="bumkItem" items="${bumkList}" varStatus="status">
			<c:if test="${fncCal == 'psn' }">
				<option value="${bumkItem.bumkTgtUid}" <c:if test="${bumkItem.bumkTgtUid == bumk}">selected</c:if>>${bumkItem.bumkDispNm}</option>
			</c:if>
			<c:if test="${fncCal == 'dept' }">
				<option value="${bumkItem.bumkTgtDeptId}" <c:if test="${bumkItem.bumkTgtDeptId == bumk}">selected</c:if>>${bumkItem.bumkDispNm}</option>
			</c:if>
			
		</c:forEach>
		</select></td>
	<td class="frontbtn"><u:buttonS href="javascript:setBumpPopOpen()" titleId="cm.btn.addBumk" alt="즐겨찾기에추가" /></td>
	</c:if>
	</tr>
	</table>
	</form>
	</div>
	<div class="front_right" >
	<table border="0" cellpadding="0" cellspacing="0">
	<tr> 
	<td id="selectDtCalBtn">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	<!-- 조회,수정 하기 위해서 schdlId 히든값셋팅 -->
	<td><input type="hidden" id="scds_schdlId"/></td>
	<td class="frontbtn" >
		<div id="selectDtCalArea" style="display: none; position: absolute;"></div>		
		<u:buttonS href="javascript:calendar.open('selectDt',null,function(date, option){selectMoveDate(date);return true;})" titleId="wc.btn.dtMove" alt="날짜이동"   />
	</td>
	</tr>
	</table>
	</div>
</div>



<!-- 원간일정 S -->
<u:listArea id="molyList" style="display: none;" noBottomBlank="true">
	<tr>
		<td width="15%" class="scdhead"><strong><u:msg titleId="wc.cols.sun" alt="일" /></strong></td>
		<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.mon" alt="월" /></strong></td>
		<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.tue" alt="화" /></strong></td>
		<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.wed" alt="수" /></strong></td>
		<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.thu" alt="목" /></strong></td>
		<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.fri" alt="금" /></strong></td>
		<td width="15%" class="head_ct"><strong><u:msg titleId="wc.cols.sat" alt="토" /></strong></td>
	</tr>
	
	<c:forEach  var="calWeek" items="${wcScdCalMonth.weeks}" varStatus="status">
	<tr>
		<c:forEach  var="calDay" items="${calWeek.days}" varStatus="status">
			<td class="${calDay.toDayFlag}">			
			<span class="${calDay.holiFlag}"><u:out value="${calDay.day}"/></span>
			
			<c:forEach var="scds" items="${calDay.scds}" varStatus="status">
				<c:if test="${scds.schdlTypCd == '5' and status.index==0}">			
					<c:choose>
						<c:when test="${calDay.holiFlag == 'scddate_prev'}"> <c:set var="scds_color"	value= "scd_prev" /> </c:when>
						<c:when test="${calDay.holiFlag == 'scddate_red_prev'}"><c:set var="scds_color"	value= "scd_prev" /> <br></c:when>
						<c:otherwise><c:set var="scds_color"	value= "scd_red" /></c:otherwise>
					</c:choose>					
					<span class="${scds_color}"><u:out value="${scds.subj}"/> </span><br>					
				</c:if>
			</c:forEach>
			
				<c:set var="maxSchdsIndex" value="${calDay.scdMaxIndex}"/>
				<c:if test="${maxSchdsIndex > 0}">					
					<c:forEach begin="1" end="${maxSchdsIndex}" step="1" var="tempIndex">
						<c:set var="scdIndexFindFlag" value= "false" />
						<c:forEach var="scds" items="${calDay.scds}" varStatus="status">				
							<c:if test="${tempIndex == scds.schdIndex}">
								
								<c:set var="scdIndexFindFlag" value= "true" />
								 <!-- 내용 S -->
								
							       <c:choose>
										<c:when test="${calDay.holiFlag == 'scddate_prev'}"> 
											<c:set var="scds_color"	value= "scd_prev" /> 
										</c:when>
										<c:when test="${calDay.holiFlag == 'scddate_red_prev'}">
											<c:set var="scds_color"	value= "scd_prev" />
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${scds.schdlTypCd == '1'}"> 
													<c:set var="scds_color"	value= "scd_promise" />
													<c:set var="viewFunction"	value= "viewProm('${scds.schdlId}')" />
												</c:when>
												<c:when test="${scds.schdlTypCd == '2'}"> 
													<c:set var="scds_color"	value= "scd_work" />
													<c:set var="viewFunction"	value= "viewWork('${scds.schdlId}')" />
												</c:when>
												<c:when test="${scds.schdlTypCd == '3'}"> 
													<c:set var="scds_color"	value= "scd_event" />
													<c:set var="viewFunction"	value= "viewEvnt('${scds.schdlId}')" />
												</c:when>
												<c:when test="${scds.schdlTypCd == '4'}"> 
													<c:set var="scds_color"	value= "scd_anniversary" />
													<c:set var="viewFunction"	value= "viewAnnv('${scds.schdlId}')" />
												</c:when>												
											</c:choose>
											
										</c:otherwise>
								   </c:choose>
							       <c:if test="${empty scds.schdlRepetState}">
							       		<div class="" style="padding: 2px 3px 0px 0px;height:18px;">
							       </c:if>
							       <c:if test="${!empty scds.schdlRepetState}">
							       		<div class="${scds.schdlRepetState}"> 
							       </c:if>
							        
							          <a href="javascript:${viewFunction}">
							           <fmt:parseDate var="dateTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
							           <span class="${scds_color}" id="${scds.schdlId}_month">
							           		<fmt:formatDate value="${dateTempParse}" pattern="HH:mm"/> <u:out value="${scds.subj}" maxLength="20" /> 
							           		<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
										   	<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
										   	
										   	
										   	<c:if test="${scds.solaLunaYn=='N'}">
										   		<fmt:parseDate var="dateLunaStartDt" value="${scds.schdlLunaStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
										   		<fmt:formatDate var="convLunaStartDt" value="${dateLunaStartDt}" pattern="yyyy-MM-dd"/>
										   		<input type="hidden" id="scds_tooltip_schdlStartDay" value="(<u:msg titleId="wc.option.luna" alt="음력"/> :${convLunaStartDt}) ${convStartDt}"/>
										    </c:if>
										    <c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
										   		<input type="hidden" id="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
										    </c:if>
								
										    <fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
										    <fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="yyyy-MM-dd"/>
										    
										    <c:if test="${scds.solaLunaYn=='N'}">
										   		<fmt:parseDate var="dateLunaEndDt" value="${scds.schdlLunaEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
										   		<fmt:formatDate var="convLunaEndDt" value="${dateLunaEndDt}" pattern="yyyy-MM-dd"/>
										   		<input type="hidden" id="scds_tooltip_schdlEndDay" value="(<u:msg titleId="wc.option.luna" alt="음력"/> :${convLunaEndDt}) ${convEndDt}"/>
										    </c:if>
										    <c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
										   		<input type="hidden" id="scds_tooltip_schdlEndDay" value="${convEndDt}"/>
										    </c:if>
										    
										  	
										  	<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
										   	<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="HH:mm"/>										   
										 	<input type="hidden" id="scds_tooltip_schdlStartDt" value="${convStartDt}"/>
										 
										    
										    <fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
										    <fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="HH:mm"/>
										    <input type="hidden" id="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>
										  	
										   	<input type="hidden" id="scds_tooltip_subj" value="${scds.subj}"/>
										   	<input type="hidden" id="scds_tooltip_locNm" value="${scds.locNm}"/>	
										   	<input type="hidden" id="scds_tooltip_cont" value="${scds.cont}"/>
										   	<input type="hidden" id="scds_tooltip_regNm" value="${scds.regrNm}"/>
										   	<!-- viewProm 데이터 추가 -->
										   	<input type="hidden" id="scds_openGradCd" value="${scds.openGradCd}"/>
										   	<!-- 참석자 -->
											<c:forEach  var="promGst" items="${scds.promGuestLst}"  varStatus="status">
											 	<input id="scds_promGstUid" type="hidden" value="${promGst.guestUid}" />
											 	<input id="scds_promGstNm" type="hidden" value="${promGst.guestNm}" />
											 	<input id="scds_promGstDptNm" type="hidden" value="${promGst.guestDeptNm}" />
											</c:forEach>
											<input type="hidden" id="scds_workPrioOrdr" value="${scds.workPrioOrdr}"/>
											<input type="hidden" id="scds_schdlStatCd" value="${scds.schdlStatCd}"/>
											<input type="hidden" id="scds_schdlStatCd" value="${scds.schdlStatCd}"/>
											<input type="hidden" id="scds_holiYn" value="${scds.holiYn}"/>
											<input type="hidden" id="scds_solaLunaYn" value="${scds.solaLunaYn}"/>
											<fmt:parseDate var="workDateEndDt" value="${scds.workCmltYmd}" pattern="yyyy-MM-dd HH:mm:ss"/>
										    <fmt:formatDate var="workCmltYmd" value="${workDateEndDt}" pattern="yyyy-MM-dd"/>
											<input type="hidden" id="scds_workCmltYmd"  value="${workCmltYmd}"/>
											
							           </span>
							         </a>
							           
							     </div>
							  
								
							   <!-- 내용 E -->
							</c:if>							
						</c:forEach>
						<c:if test="${scdIndexFindFlag == false}">
							 <c:if test="${tempIndex == 0}">
							 	<br>
							 </c:if>
							 <c:if test="${tempIndex != 0}">
							 	<div class=""  style="padding: 2px 3px 0px 0px;height:18px;" id="emptyScd">
							 		<span class="" >&nbsp;No Display Error</span>
							 	</div>
							 </c:if>
						</c:if>
					</c:forEach>
				</c:if>			 
			</td>
		</c:forEach>
	</tr>
	</c:forEach>	
</u:listArea>
<!-- 원간일정  E -->
		



<!-- 주간일정 S -->
<u:listArea id="welyList" style="display: none;" noBottomBlank="true">
	<!-- 날짜 표기 S -->
	<tr>
		<c:forEach  var="weekCalDay" items="${wcScdCalWeek.days}" varStatus="status">			
			<c:choose>
				<c:when test="${status.index==0  || scds.schdlTypCd == '5'}"> 
					<c:set var="day_num_style"	value= "scdhead_week" />					
				</c:when>
				<c:otherwise>
					<c:set var="day_num_style"	value= "head_lt" />
				</c:otherwise>								
			</c:choose>
			<c:choose>
				<c:when test="${status.index==0}"> 
					<td width="15%" class="${day_num_style}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.sun" alt="일" /></td>				
				</c:when>
				<c:when test="${status.index==1}"> 
					<td width="14%" class="${day_num_style}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.mon" alt="월" /></td>				
				</c:when>
				<c:when test="${status.index==2}"> 
					<td width="14%" class="${day_num_style}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.tue" alt="화" /></td>				
				</c:when>
				<c:when test="${status.index==3}"> 
					<td width="14%" class="${day_num_style}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.wed" alt="수" /></td>				
				</c:when>
				<c:when test="${status.index==4}"> 
					<td width="14%" class="${day_num_style}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.thu" alt="목" /></td>				
				</c:when>
				<c:when test="${status.index==5}"> 
					<td width="14%" class="${day_num_style}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.fri" alt="금" /></td>				
				</c:when>
				<c:when test="${status.index==6}"> 
					<td width="15%" class="${day_num_style}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.sat" alt="토" /></td>				
				</c:when>
			</c:choose>							
		</c:forEach>
	</tr>
	<!-- 날짜 표기 E -->
	<tr>
		<c:forEach  var="calDay" items="${wcScdCalWeek.days}" varStatus="status">
			<c:choose>
				<c:when test="${calDay.toDayFlag != 'scdtd'}"> 
					<c:set var="today_class"	value= "scd_today_week" />					
				</c:when>
				<c:otherwise>
					<c:set var="today_class"	value= "scdtd_week" />
				</c:otherwise>								
			</c:choose>
			
			<td class="${today_class}" height="300">
			<c:set var="scdHoliFindFlag" value= "false" />
			<c:forEach var="scds" items="${calDay.scds}" varStatus="status">
				<c:if test="${scds.schdlTypCd == '5' and status.index==0}">			
					<c:choose>
						<c:when test="${calDay.holiFlag == 'scddate_prev'}"> <c:set var="scds_color"	value= "scd_prev" /> </c:when>
						<c:when test="${calDay.holiFlag == 'scddate_red_prev'}"><c:set var="scds_color"	value= "scd_prev" /> <br></c:when>
						<c:otherwise><c:set var="scds_color"	value= "scd_red" /></c:otherwise>
					</c:choose>	
					<c:set var="scdHoliFindFlag" value= "true" />				
					<span class="${scds_color}"><u:out value="${scds.subj}"/></span><br>					
				</c:if>
			</c:forEach>
			<c:if test="${scdHoliFindFlag == false}">
				<span class="scd_red"> &nbsp </span><br>
			</c:if>
			
				<c:set var="maxSchdsIndex" value="${calDay.scdMaxIndex}"/>
				<c:if test="${maxSchdsIndex > 0}">					
					<c:forEach begin="1" end="${maxSchdsIndex}" step="1" var="tempIndex">
						<c:set var="scdIndexFindFlag" value= "false" />
						<c:forEach var="scds" items="${calDay.scds}" varStatus="status">				
							<c:if test="${tempIndex == scds.schdIndex}">
								
								<c:set var="scdIndexFindFlag" value= "true" />
								 <!-- 내용 S -->
								
							       <c:choose>
										<c:when test="${calDay.holiFlag == 'scddate_prev'}"> 
											<c:set var="scds_color"	value= "scd_prev" /> 
										</c:when>
										<c:when test="${calDay.holiFlag == 'scddate_red_prev'}">
											<c:set var="scds_color"	value= "scd_prev" />
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${scds.schdlTypCd == '1'}"> 
													<c:set var="scds_color"	value= "scd_promise" />
													<c:set var="viewFunction"	value= "viewProm('${scds.schdlId}')" />
												</c:when>
												<c:when test="${scds.schdlTypCd == '2'}"> 
													<c:set var="scds_color"	value= "scd_work" />
													<c:set var="viewFunction"	value= "viewWork('${scds.schdlId}')" />
												</c:when>
												<c:when test="${scds.schdlTypCd == '3'}"> 
													<c:set var="scds_color"	value= "scd_event" />
													<c:set var="viewFunction"	value= "viewEvnt('${scds.schdlId}')" />
												</c:when>
												<c:when test="${scds.schdlTypCd == '4'}"> 
													<c:set var="scds_color"	value= "scd_anniversary" />
													<c:set var="viewFunction"	value= "viewAnnv('${scds.schdlId}')" />
												</c:when>												
											</c:choose>
											
										</c:otherwise>
								   </c:choose>
							       <c:if test="${empty scds.schdlRepetState}">
							       		<div class="" style="padding: 2px 3px 0px 0px;height:18px;">
							       </c:if>
							       <c:if test="${!empty scds.schdlRepetState}">
							       		<div class="${scds.schdlRepetState}"> 
							       </c:if>
							        
							          <a href="javascript:${viewFunction}">
							           <fmt:parseDate var="dateTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
							           <span class="${scds_color}" id="${scds.schdlId}_week">
							           		<fmt:formatDate value="${dateTempParse}" pattern="HH:mm"/>  <u:out value="${scds.subj}" maxLength="20" /> 
							           		<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
										   	<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
										   	<input type="hidden" id="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
										    <fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
										    <fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="yyyy-MM-dd"/>
										    <input type="hidden" id="scds_tooltip_schdlEndDay"  value="${convEndDt}"/>
										  	
										  	<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
										   	<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="HH:mm"/>
										   	<input type="hidden" id="scds_tooltip_schdlStartDt" value="${convStartDt}"/>
										    <fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
										    <fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="HH:mm"/>
										    <input type="hidden" id="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>
										  	
										   	<input type="hidden" id="scds_tooltip_subj" value="${scds.subj}"/>
										   	<input type="hidden" id="scds_tooltip_locNm" value="${scds.locNm}"/>	
										   	<input type="hidden" id="scds_tooltip_cont" value="${scds.cont}"/>
										   	<input type="hidden" id="scds_tooltip_regNm" value="${scds.regrNm}"/>
										   	
										   	<!-- viewProm 데이터 추가 -->
										   	<input type="hidden" id="scds_openGradCd" value="${scds.openGradCd}"/>
										   	<!-- 참석자 -->
											<c:forEach  var="promGst" items="${scds.promGuestLst}"  varStatus="status">
											 	<input id="scds_promGstUid" type="hidden" value="${promGst.guestUid}" />
											 	<input id="scds_promGstNm" type="hidden" value="${promGst.guestNm}" />
											 	<input id="scds_promGstDptNm" type="hidden" value="${promGst.guestDeptNm}" />
											</c:forEach>
											<input type="hidden" id="scds_workPrioOrdr" value="${scds.workPrioOrdr}"/>
											<input type="hidden" id="scds_schdlStatCd" value="${scds.schdlStatCd}"/>
											<input type="hidden" id="scds_schdlStatCd" value="${scds.schdlStatCd}"/>
											<input type="hidden" id="scds_holiYn" value="${scds.holiYn}"/>
											<input type="hidden" id="scds_solaLunaYn" value="${scds.solaLunaYn}"/>
											<fmt:parseDate var="workDateEndDt" value="${scds.workCmltYmd}" pattern="yyyy-MM-dd HH:mm:ss"/>
										    <fmt:formatDate var="workCmltYmd" value="${workDateEndDt}" pattern="yyyy-MM-dd"/>
											<input type="hidden" id="scds_workCmltYmd"  value="${workCmltYmd}"/>
							           </span>
							         </a>
							           
							     </div>
							   
								
							   <!-- 내용 E -->
							</c:if>							
						</c:forEach>
						<c:if test="${scdIndexFindFlag == false}">
							 <c:if test="${tempIndex == 0}">
							 	<br>
							 </c:if>
							 <c:if test="${tempIndex != 0}">
							 	<div class=""  style="padding: 2px 3px 0px 0px;height:18px;" id="emptyScd">
							 		<span class="" >&nbsp;No Display Error</span>
							 	</div>
							 </c:if>
						</c:if>
					</c:forEach>
				</c:if>			 
			</td>
		</c:forEach>
		
	</tr>
</u:listArea>
<!-- 주간일정  E -->

<!-- 일일일정  S -->
<div id="dalyList" class="listarea" style="display: none;" noBottomBlank="true">
<table class="listtable" border="0" cellpadding="0" cellspacing="1"><tbody>
	<tr>
	<td width="70" class="head_ct"></td>
	<!-- 날짜 및 요일 표기 S -->
	<td class="head_lt">
		<strong><u:out value="${wcScdCalDay.day}"/></strong>
		<c:choose>
			<c:when test="${wcScdCalDay.dayOfTheWeek == '0'}">
	   			<u:msg titleId="wc.cols.sun" alt="일" />
	   		</c:when>
	   		<c:when test="${wcScdCalDay.dayOfTheWeek == '1'}">
	   			<u:msg titleId="wc.cols.mon" alt="월" />
	   		</c:when>
	   		<c:when test="${wcScdCalDay.dayOfTheWeek == '2'}">
	   			<u:msg titleId="wc.cols.tue" alt="화" />
	   		</c:when>
	   		<c:when test="${wcScdCalDay.dayOfTheWeek == '3'}">
	   			<u:msg titleId="wc.cols.wed" alt="수" />
	   		</c:when>
	   		<c:when test="${wcScdCalDay.dayOfTheWeek == '4'}">
	   			<u:msg titleId="wc.cols.thu" alt="목" />
	   		</c:when>
	   		<c:when test="${wcScdCalDay.dayOfTheWeek == '5'}">
	   			<u:msg titleId="wc.cols.fri" alt="금" />
	   		</c:when>
	   		<c:when test="${wcScdCalDay.dayOfTheWeek == '6'}">
	   			<u:msg titleId="wc.cols.sat" alt="토" />
	   		</c:when>
		</c:choose>
		
		<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
			<c:if test="${scds.schdlTypCd == '5' and status.index==0}">
				( ${scds.subj} )<br>
			</c:if>
		</c:forEach>
	</td>
	<!-- 날짜 및 요일 표기  E -->
	</tr>
	<c:set var="schdsSize"	value= "0" />
	<tr id="dayPromiseRow">
		<td class="scdtd_day" style="height:18px"><span class="scd_gray" ><u:msg titleId="wc.cols.prom" alt="약속" /></span></td>
		<td class="scdtd_day">
			<c:set var="schdsPromSize"	value= "0" />
			<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">				
				<c:if test="${scds.schdlTypCd == '1' &&!empty scds.subj}">
					<c:set var="schdsPromSize"	value= "${schdsPromSize+1}" />
					<div class="" style="padding: 0px 0px 0px 0px;">
					<a href="javascript:viewProm('${scds.schdlId}')" >
						<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			        	<span class="scd_promise" id="${scds.schdlId}_day">
			        		<fmt:formatDate value="${dateStartTempParse}" pattern="HH:mm"/>~<fmt:formatDate value="${dateEndTempParse}" pattern="HH:mm"/><u:out value="${scds.subj}"/>
			        		
			        		<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/>
						   	<input type="hidden" id="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
						  
						    <fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/>
						    <input type="hidden" id="scds_tooltip_schdlEndDay"  value="${convEndDt}"/>						  	
						  	
						   	<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="HH:mm"/>
						   	<input type="hidden" id="scds_tooltip_schdlStartDt" value="${convStartDt}"/>
						 
						    <fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="HH:mm"/>
						    <input type="hidden" id="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>						  	
						   	<input type="hidden" id="scds_tooltip_subj" value="${scds.subj}"/>
						   	<input type="hidden" id="scds_tooltip_locNm" value="${scds.locNm}"/>	
						   	<input type="hidden" id="scds_tooltip_cont" value="${scds.cont}"/>
						   	<input type="hidden" id="scds_tooltip_regNm" value="${scds.regrNm}"/>
						   	
						   	<!-- viewProm 데이터 추가 -->
						   	<input type="hidden" id="scds_openGradCd" value="${scds.openGradCd}"/>
						   	<!-- 참석자 -->
							<c:forEach  var="promGst" items="${scds.promGuestLst}"  varStatus="status">
							 	<input id="scds_promGstUid" type="hidden" value="${promGst.guestUid}" />
							 	<input id="scds_promGstNm" type="hidden" value="${promGst.guestNm}" />
							 	<input id="scds_promGstDptNm" type="hidden" value="${promGst.guestDeptNm}" />
							</c:forEach>
							<input type="hidden" id="scds_workPrioOrdr" value="${scds.workPrioOrdr}"/>
							<input type="hidden" id="scds_schdlStatCd" value="${scds.schdlStatCd}"/>
							<input type="hidden" id="scds_schdlStatCd" value="${scds.schdlStatCd}"/>
							<input type="hidden" id="scds_holiYn" value="${scds.holiYn}"/>
							<input type="hidden" id="scds_solaLunaYn" value="${scds.solaLunaYn}"/>
							<fmt:parseDate var="workDateEndDt" value="${scds.workCmltYmd}" pattern="yyyy-MM-dd HH:mm:ss"/>
						    <fmt:formatDate var="workCmltYmd" value="${workDateEndDt}" pattern="yyyy-MM-dd"/>
							<input type="hidden" id="scds_workCmltYmd"  value="${workCmltYmd}"/>
			        	</span>
			        	
			        </a>
			        </div>
			    </c:if>
			</c:forEach>
			<c:if test="${schdsPromSize>1}">
				<c:set var="schdsSize"	value="${schdsSize+(schdsPromSize-1)}" />
			</c:if>
		</td>
	</tr>
	
	<tr id="dayWorkRow">
		<td class="scdtd_day" style="height:18px"><span class="scd_gray"><u:msg titleId="wc.cols.work" alt="할일" /></span></td>
		<td class="scdtd_day">
			<c:set var="schdsWorkSize"	value= "0" />
			<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
				<c:if test="${scds.schdlTypCd == '2' &&!empty scds.subj }">
					<c:set var="schdsWorkSize"	value= "${schdsWorkSize+1}" />
					<div class="" style="padding: 0px 0px 0px 0px;">
					<a href="javascript:viewWork('${scds.schdlId}')" >
						<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			            <span class="scd_work" id="${scds.schdlId}_day">
			            	<fmt:formatDate value="${dateStartTempParse}" pattern="HH:mm"/>~<fmt:formatDate value="${dateEndTempParse}" pattern="HH:mm"/><u:out value="${scds.subj}"/>
			            	
			            	<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/>
						   	<input type="hidden" id="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
						  
						    <fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/>
						    <input type="hidden" id="scds_tooltip_schdlEndDay"  value="${convEndDt}"/>						  	
						  	
						   	<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="HH:mm"/>
						   	<input type="hidden" id="scds_tooltip_schdlStartDt" value="${convStartDt}"/>
						 
						    <fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="HH:mm"/>
						    <input type="hidden" id="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>						  	
						   	<input type="hidden" id="scds_tooltip_subj" value="${scds.subj}"/>
						   	<input type="hidden" id="scds_tooltip_locNm" value="${scds.locNm}"/>	
						   	<input type="hidden" id="scds_tooltip_cont" value="${scds.cont}"/>
						   	<input type="hidden" id="scds_tooltip_regNm" value="${scds.regrNm}"/>
						   	
						   	<!-- viewProm 데이터 추가 -->
						   	<input type="hidden" id="scds_openGradCd" value="${scds.openGradCd}"/>
						   	<!-- 참석자 -->
							<c:forEach  var="promGst" items="${scds.promGuestLst}"  varStatus="status">
							 	<input id="scds_promGstUid" type="hidden" value="${promGst.guestUid}" />
							 	<input id="scds_promGstNm" type="hidden" value="${promGst.guestNm}" />
							 	<input id="scds_promGstDptNm" type="hidden" value="${promGst.guestDeptNm}" />
							</c:forEach>
							<input type="hidden" id="scds_workPrioOrdr" value="${scds.workPrioOrdr}"/>
							<input type="hidden" id="scds_schdlStatCd" value="${scds.schdlStatCd}"/>
							<input type="hidden" id="scds_schdlStatCd" value="${scds.schdlStatCd}"/>
							<input type="hidden" id="scds_holiYn" value="${scds.holiYn}"/>
							<input type="hidden" id="scds_solaLunaYn" value="${scds.solaLunaYn}"/>
							<fmt:parseDate var="workDateEndDt" value="${scds.workCmltYmd}" pattern="yyyy-MM-dd HH:mm:ss"/>
						    <fmt:formatDate var="workCmltYmd" value="${workDateEndDt}" pattern="yyyy-MM-dd"/>
							<input type="hidden" id="scds_workCmltYmd"  value="${workCmltYmd}"/>
			            </span>
			        </a>
			        </div>
			    </c:if>
			</c:forEach>
			<c:if test="${schdsWorkSize>1}">
				<c:set var="schdsSize"	value="${schdsSize+(schdsWorkSize-1)}" />
			</c:if>
		</td>
	</tr>
	
	<tr id="dayEventRow">
		<td class="scdtd_day" style="height:18px"><span class="scd_gray"><u:msg titleId="wc.cols.evnt" alt="행사" /></span></td>
		<td class="scdtd_day">
			<c:set var="schdsEventSize"	value= "0" />
			<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
				<c:if test="${scds.schdlTypCd == '3' &&!empty scds.subj}">
					<c:set var="schdsEventSize"	value= "${schdsEventSize+1}" />
					<div class="" style="padding: 0px 0px 0px 0px;">
					<a href="javascript:viewEvnt('${scds.schdlId}')" >
						<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			       	    <span class="scd_event" id="${scds.schdlId}_day">
			       	    	<fmt:formatDate value="${dateStartTempParse}" pattern="HH:mm"/>~<fmt:formatDate value="${dateEndTempParse}" pattern="HH:mm"/><u:out value="${scds.subj}"/>
			       	    	
			       	    	<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/>
						   	<input type="hidden" id="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
						  
						    <fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/>
						    <input type="hidden" id="scds_tooltip_schdlEndDay"  value="${convEndDt}"/>						  	
						  	
						   	<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="HH:mm"/>
						   	<input type="hidden" id="scds_tooltip_schdlStartDt" value="${convStartDt}"/>
						 
						    <fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="HH:mm"/>
						    <input type="hidden" id="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>						  	
						   	<input type="hidden" id="scds_tooltip_subj" value="${scds.subj}"/>
						   	<input type="hidden" id="scds_tooltip_locNm" value="${scds.locNm}"/>	
						   	<input type="hidden" id="scds_tooltip_cont" value="${scds.cont}"/>
						   	<input type="hidden" id="scds_tooltip_regNm" value="${scds.regrNm}"/>
						   	
						   	<!-- viewProm 데이터 추가 -->
						   	<input type="hidden" id="scds_openGradCd" value="${scds.openGradCd}"/>
						   	<!-- 참석자 -->
							<c:forEach  var="promGst" items="${scds.promGuestLst}"  varStatus="status">
							 	<input id="scds_promGstUid" type="hidden" value="${promGst.guestUid}" />
							 	<input id="scds_promGstNm" type="hidden" value="${promGst.guestNm}" />
							 	<input id="scds_promGstDptNm" type="hidden" value="${promGst.guestDeptNm}" />
							</c:forEach>
							<input type="hidden" id="scds_workPrioOrdr" value="${scds.workPrioOrdr}"/>
							<input type="hidden" id="scds_schdlStatCd" value="${scds.schdlStatCd}"/>
							<input type="hidden" id="scds_schdlStatCd" value="${scds.schdlStatCd}"/>
							<input type="hidden" id="scds_holiYn" value="${scds.holiYn}"/>
							<input type="hidden" id="scds_solaLunaYn" value="${scds.solaLunaYn}"/>
							<fmt:parseDate var="workDateEndDt" value="${scds.workCmltYmd}" pattern="yyyy-MM-dd HH:mm:ss"/>
						    <fmt:formatDate var="workCmltYmd" value="${workDateEndDt}" pattern="yyyy-MM-dd"/>
							<input type="hidden" id="scds_workCmltYmd"  value="${workCmltYmd}"/>
			       	    </span>
			       	 </a>
			       	</div>
			    </c:if>
			</c:forEach>
			<c:if test="${schdsEventSize>1}">
				<c:set var="schdsSize"	value="${schdsSize+(schdsEventSize-1)}" />
			</c:if>
		</td>
	</tr>

	<tr id="dayAnnvRow">
		<td class="scdtd_day" style="height:18px"><span class="scd_gray"><u:msg titleId="wc.cols.annv" alt="기념일" /></span></td>
		<td class="scdtd_day">
			<c:set var="schdsAnniversarySize"	value= "0" />
			<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
				<c:if test="${scds.schdlTypCd == '4' &&!empty scds.subj}">
					<c:set var="schdsAnniversarySize"	value= "${schdsAnniversarySize+1}" />
					<div class="" style="padding: 0px 0px 0px 0px;">
					<a href="javascript:viewAnnv('${scds.schdlId}')" >
						<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			       		<span class="scd_anniversary" id="${scds.schdlId}_day">
			       			<fmt:formatDate value="${dateStartTempParse}" pattern="HH:mm"/>~<fmt:formatDate value="${dateEndTempParse}" pattern="HH:mm"/><u:out value="${scds.subj}"/>
			       			<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="yyyy-MM-dd"/>
						   	<input type="hidden" id="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
						  
						    <fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="yyyy-MM-dd"/>
						    <input type="hidden" id="scds_tooltip_schdlEndDay"  value="${convEndDt}"/>						  	
						  	
						   	<fmt:formatDate var="convStartDt" value="${dateStartTempParse}" pattern="HH:mm"/>
						   	<input type="hidden" id="scds_tooltip_schdlStartDt" value="${convStartDt}"/>
						 
						    <fmt:formatDate var="convEndDt" value="${dateEndTempParse}" pattern="HH:mm"/>
						    <input type="hidden" id="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>						  	
						   	<input type="hidden" id="scds_tooltip_subj" value="${scds.subj}"/>
						   	<input type="hidden" id="scds_tooltip_locNm" value="${scds.locNm}"/>	
						   	<input type="hidden" id="scds_tooltip_cont" value="${scds.cont}"/>
						   	<input type="hidden" id="scds_tooltip_regNm" value="${scds.regrNm}"/>
						   	
						   	<!-- viewProm 데이터 추가 -->
						   	<input type="hidden" id="scds_openGradCd" value="${scds.openGradCd}"/>
						   	<!-- 참석자 -->
							<c:forEach  var="promGst" items="${scds.promGuestLst}"  varStatus="status">
							 	<input id="scds_promGstUid" type="hidden" value="${promGst.guestUid}" />
							 	<input id="scds_promGstNm" type="hidden" value="${promGst.guestNm}" />
							 	<input id="scds_promGstDptNm" type="hidden" value="${promGst.guestDeptNm}" />
							</c:forEach>
							<input type="hidden" id="scds_workPrioOrdr" value="${scds.workPrioOrdr}"/>
							<input type="hidden" id="scds_schdlStatCd" value="${scds.schdlStatCd}"/>
							<input type="hidden" id="scds_schdlStatCd" value="${scds.schdlStatCd}"/>
							<input type="hidden" id="scds_holiYn" value="${scds.holiYn}"/>
							<input type="hidden" id="scds_solaLunaYn" value="${scds.solaLunaYn}"/>
							<fmt:parseDate var="workDateEndDt" value="${scds.workCmltYmd}" pattern="yyyy-MM-dd HH:mm:ss"/>
						    <fmt:formatDate var="workCmltYmd" value="${workDateEndDt}" pattern="yyyy-MM-dd"/>
							<input type="hidden" id="scds_workCmltYmd"  value="${workCmltYmd}"/>
			       		</span>
			       	</a>
			       	</div>
			    </c:if>
			</c:forEach>
			<c:if test="${schdsAnniversarySize>1}">
				<c:set var="schdsSize"	value="${schdsSize+(schdsAnniversarySize-1)}" />
			</c:if>
		</td>
	</tr>

</tbody></table>

<u:blank />

<table class="listtable" border="0" cellpadding="0" cellspacing="1"><tbody>
	<!-- 일일 시간 표시  S -->
	<fmt:formatDate value="${now}" pattern="H" var="curHour" /> 
	<fmt:formatDate value="${now}" pattern="yyyy-M-d" var="curDate" />
	
	
	<c:set var="viewDate"	value= "${wcScdCalDay.year}-${wcScdCalDay.month}-${wcScdCalDay.day}" />
	<c:forEach begin="0" end="23" step="1" var="tempHour">
		<c:set var="hour"	value= "" />
		<c:set var="hourStr"	value= "" />
		<c:set var="ampm"	value= "" />
		
		<c:if test="${tempHour < 12}">
			<c:set var="hour"	value= "${tempHour}" />
			<c:set var="ampm"	value= "" />
		</c:if>
		
		<c:if test="${tempHour > 12}">
			<c:set var="hour"	value= "${tempHour-12}" />
			<c:set var="ampm"	value= "" />
		</c:if>
		<c:if test="${tempHour == 12}">
			<c:set var="hour"	value= "12" />
			<c:set var="ampm"	value= "PM" />
		</c:if>		
		<c:if test="${tempHour == 0}">
			<c:set var="hour"	value= "12" />
			<c:set var="ampm"	value= "AM" />
		</c:if>
		<c:set var="hourStr"	value= "${hour}" />
		<c:if test="${hour < 10}">
			<c:set var="hourStr"	value= "0${hour}" />
		</c:if>
		
		<tr>
			<td width="70" class="scdtd_day">
				<span class="scddate_day">${ampm}</span> 
				<span class="scd_gray">${hourStr}:00</span>
			</td>
			<c:if test="${viewDate == curDate and curHour==tempHour }">
				<td class="scd_today_day"></td>
			</c:if>
			<c:if test="${viewDate != curDate or curHour!=tempHour }">
				<td class="scdtd_day"></td>
			</c:if>
		</tr>
	</c:forEach>
	<!-- 일일 시간 표시  E -->
</tbody></table>



<!--popup S-->
<c:set var="offsetT" value="${(schdsSize*16)+221-(schdsSize>0?2:0)}" />
<c:set var="hour" value="21" />
<c:set var="offsetH" value="-2" />
<c:set var="offsetL" value="84" />
<c:set var="popW" value="150" />
<c:set var="popM" value="10" />

<!-- 일일 일정 내용 S -->




<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
	<fmt:parseDate var="dateStartdDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
	<fmt:formatDate var="convStartHour" value="${dateStartdDt}" pattern="HH"/>
	<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
	<fmt:formatDate var="convEndHour" value="${dateEndDt}" pattern="HH"/>
	<fmt:formatDate var="convStartTime" value="${dateStartdDt}" pattern="HH:mm"/>
	
	<c:set var="popLeft" value="${status.index * (popW + popM) + offsetL}" />
	<c:set var="popTop" value="${convStartHour * hour + offsetT}" />
	<c:set var="popH" value="${((convEndHour+1-convStartHour) * hour + offsetH)-1}" />
	
	<c:choose>
		<c:when test="${scds.schdlTypCd == '1'}"> 
			<c:set var="scds_color"	value= "scd_promise" />
			<c:set var="viewFunction"	value= "viewProm('${scds.schdlId}')" />
		</c:when>
		<c:when test="${scds.schdlTypCd == '2'}"> 
			<c:set var="scds_color"	value= "scd_work" />
			<c:set var="viewFunction"	value= "viewWork('${scds.schdlId}')" />
		</c:when>
		<c:when test="${scds.schdlTypCd == '3'}"> 
			<c:set var="scds_color"	value= "scd_event" />
			<c:set var="viewFunction"	value= "viewEvnt('${scds.schdlId}')" />
		</c:when>
		<c:when test="${scds.schdlTypCd == '4'}"> 
			<c:set var="scds_color"	value= "scd_anniversary" />
			<c:set var="viewFunction"	value= "viewAnnv('${scds.schdlId}')" />
		</c:when>												
	</c:choose>						    
	
	<div style="position:absolute; top:${popTop}px; left:${popLeft}px; z-index:1; visibility:visible;">
		<div class="scd_popup" style="width:${popW}px; height:${popH}px;">
		<a href="javascript:${viewFunction}">
       		<span class="${scds_color}" id="${scds.schdlId}_day">${convStartTime} <u:out value="${scds.subj}" maxLength="15" />        		
			</span>
		</a>
		</div>
	</div>
</c:forEach>
<!-- 일일 일정 내용 E -->
<!--popup E-->

</div>
<!-- 일일일정  E -->


<% // 하단 FRONT %>
<div class="front">
	<div class="front_right">
	<table border="0" cellpadding="0" cellspacing="0">
	<tr>
	<td class="frontbtn"><u:buttonS id="scd_all" titleId="wc.btn.allView" alt="전체보기" onclick="showAll()" img="scd_all_on.png" imgH="9" /></td>
	<c:if test="${fncCal == 'psn' || fncCal == 'my'}">
	<td class="frontbtn"><u:buttonS id="scd_promise" titleId="wc.btn.promView" alt="약속보기" onclick="showProm()" img="scd_promise.png" imgH="9" /></td>
	<td class="frontbtn"><u:buttonS id="scd_work" titleId="wc.btn.workView" alt="할일보기" onclick="showWork()" img="scd_work.png" imgH="9" /></td>
	</c:if>
	<td class="frontbtn"><u:buttonS id="scd_event" titleId="wc.btn.evntView" alt="행사보기" onclick="showEvnt()" img="scd_event.png" imgH="9" /></td>
	<td class="frontbtn"><u:buttonS id="scd_anniversary" titleId="wc.btn.annvView" alt="기념일보기" onclick="showAnnv()" img="scd_anniversary.png" imgH="9" /></td>
	</tr>
	</table>
	</div>
</div>

<u:blank />

<% // TOOLTIP %>
<div id="tooltip" style="position:absolute; top:315px; left:90px; z-index:1; display: none;">
<div class="tooltip">
	<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
	<div class="tooltip_body">
		<div class="tooltip_text" >
		
			<ul>
			<li ><strong  id="tooltip_title"></strong></li>
			<li class="blank_s2" ></li>
			<li class="tooltip_line"></li>
			<li class="blank_s5"></li>
			<li>
			 <span id="tooltip_registrant"></span><br>
			 <span id="tooltip_startToEndDay"></span><br>
			 <span id="tooltip_startToEndTime"></span><br>
			 <span id="tooltip_content"></span><br></li>
				
			</ul>
		</div>
	</div>
</div>
</div>
