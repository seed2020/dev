<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"%>
<jsp:useBean id="now" class="java.util.Date" />
<u:set test="${!empty param.tabNo}" var="tabNo" value="${param.tabNo}" elseValue="0" />
<u:set test="${!empty param.fncCal}" var="fncCal" value="${param.fncCal}" elseValue="psn" />

<!-- 권한 -->
<u:secu auth="W" ><c:set var="writeAuth" value="Y"/></u:secu>

<script type="text/javascript">

<% // [상단버튼:국가조회] 팝업 %>
function setNatPop(isSrch) {
	var url = './setNatPop.do?menuId=${menuId}&fncCal=${fncCal}';
	var title = '<u:msg titleId="wc.btn.set.nat" alt="국가설정" />';
	if(isSrch) {
		url+= '&chkNatCd=${param.natCd}&callback=searchNat';
		title = '<u:msg titleId="wc.btn.chn.nat" alt="국가변경" />';
	}
	dialog.open('setNatDialog', title, url);
}

<% // 국가기념일 조회 %>
function searchNat(cds){
	if(cds==undefined || cds.length>1) return;
	var $form = $('#calendarPrintForm');
	$form.find("[name='natCd']").remove();
	$form.appendHidden({name:'natCd',value:cds[0]});
	$form.submit();
}

//이벤트 삭제
function removeMouseEvent(obj){
	obj.unbind('mouseover');
	obj.unbind('mouseout');
};

//더보기 팝업
function listMorePop(clsNm,suffix){
	//레이어
	var container = $('#con_'+clsNm+'_'+suffix);
	//레이어 display 가 block 일 경우 숨김
	if(container.css('display') == 'block') { container.hide(); return;}
	//날짜를 보여줄 컨테이너
	var titleContainer = container.find('#titleContainer');
	//내용을 보여줄 컨테이너
	var contentsContainer = container.find('#contentsContainer');
	//타이틀 컨테이너 초기화
	titleContainer.find('#titleYmd').html('');
	//내용 컨테이너 초기화
	contentsContainer.html('');
	var days = clsNm.split('-');
	var title = days[0]+'.'+getDayVal(days[1])+'.'+getDayVal(days[2]);
	var dayOfWeek = ['sun','mon','tue','wed','thu','fri','sat'];	
	var dayOfWeekNm = callMsg('wc.cols.'+dayOfWeek[days[3]]);
	title+="("+dayOfWeekNm+")";
	//타이틀 삽입
	titleContainer.find('#titleYmd').append(title);
	//more 창에 보여질 내용 조회
	$('.more_'+clsNm+'_'+suffix).each(function(){
		//복사
		$clone = $(this).clone(true);
		$clone.css('display','block');
		$clone.css('width','98%');
		$clone.css('white-space','nowrap');
		$clone.css('text-overflow','ellipsis');
		//해당 span의 마우스 이벤트 제거
		removeMouseEvent($clone.find('.scdArea'));
		contentsContainer.append($clone);
	});
	$('.moreContainer').hide();
	container.show();
};

//즐겨찾기목록 조회
function setBumkSelect(){
	var schdlKndCd = $('#calendarPrintForm #schdlKndCd').val();
	if(schdlKndCd != ''){
		var $select = $('#calendarPrintForm #bumk');
		$select.find('option').each(function(){
			$(this).remove();
		});
		$select.append('<u:option value="-1" titleId="wc.cols.bumkTarget" alt="즐겨찾기"/>');
		$.ajax({
	        url: './selectBumkAjx.do?menuId=${menuId}&fncCal=${fncCal}',
	        type: 'POST',
	        data:{
	        	schdlKndCd : schdlKndCd
	             },
	        dataType : "json",
	        success: function(data){
	        	//if(data == null){}
	        	var value;
	        	$.each(data.model.list , function(index, wcBumkDVo) {
	        		if(schdlKndCd == 1 ) value = wcBumkDVo.bumkTgtUid;
	        		else value = wcBumkDVo.bumkTgtDeptId;
	        		value+=":"+wcBumkDVo.bumkId;
	        		$select.append('<u:option value="'+value+'" title="'+wcBumkDVo.bumkDispNm+'"/>');
	        	});
	        	$select.uniform();
	        }
		});
	}
};

//조회대상 Select
function fnOpenSchdlCdSelect(obj){
	var $view = $("#calendarPrintForm");
	$view.find("#viewUserUid").val('');
	$view.find("#viewUserNm").val('');
	$view.find("#othr").val('');
	
	$view.find("#viewOrgId").val("");
	$view.find("#viewOrgNm").val("");
	setBumkSelect();
};
//그룹선택
function fnGrpSelect(obj){
	var arrGrp=[];
	if(obj.value != ''){
		arrGrp.push("<input id='choiGrpIds' name='choiGrpIds' type='hidden' value='"+obj.value+"'>");
		arrGrp.push("<input id='choiGrpNms' name='choiGrpNms' type='hidden' value='"+obj.text+"'>");
		arrGrp.push("<input id='grpResetFlag' name='grpResetFlag' type='hidden' value='true'>");
	}else{
		arrGrp.push("<input id='grpResetFlag' name='grpResetFlag' type='hidden' value='true'>");
	}
	$("#choiGrpDiv").html(arrGrp.join(''));
	selectMonth('');
};

//상세보기 팝업
function viewSchdlPop(schdlId) {
	//schdl 값 알아내기 위해서 셋팅
	$("#scds_schdlId").val(scds_schdlId);
	dialog.open('viewSchdlPop','<u:msg titleId="wc.btn.schdlDetail" alt="상세보기" />','./viewSchdlPop.do?${params}&schdlId='+schdlId+'');
}


function getDayVal(val){
	return val < 10 ? '0'+val : val;
};

//등록 팝업
function setSchdlPop(schdlId){
	//쓰기 권한 체크
	if('${writeAuth}' != 'Y') return;
	var url = "./setSchdlPop.do";
	var popTitle = '<u:msg titleId="wc.btn.schdlReg" alt="일정등록" />';
	if(arguments.length > 1){
		var schdlStartDt = arguments[0]+''+getDayVal(arguments[1])+''+getDayVal(arguments[2]);
		if(arguments.length == 4) schdlStartDt+= ''+getDayVal(arguments[3].split(':')[0])+''+arguments[3].split(':')[1];
		url+= "?schdlStartDt="+schdlStartDt;
	}else{
		if(schdlId != null) {
			url+= "?schdlId="+schdlId;
			popTitle = '<u:msg titleId="wc.btn.schdlMod" alt="일정수정" />';
		}	
	}
	url+= url.indexOf('?') > -1 ? '&' : '?';
	var tabNo = $("#tabNo").val();
	url += "tabNo="+tabNo;
	url += "&menuId=${menuId}";
	url += "&fncCal=${fncCal}";
	url += "&molyYear="+$("#molyYear").val();
	url += "&molyMonth="+$("#molyMonth").val();
	url += "&welyYear="+$("#welyYear").val();
	url += "&welyMonth="+$("#welyMonth").val();
	url += "&welyWeek="+$("#welyWeek").val();
	url += "&dalyYear="+$("#dalyYear").val();
	url += "&dalyMonth="+$("#dalyMonth").val();
	url += "&dalyDay="+$("#dalyDay").val();
	url += "&action=";
	dialog.open('setSchdlPop',popTitle,url);
	dialog.onClose("setSchdlPop", function(){ editor('cont').clean(); unloadEvent.removeEditor('cont'); });
};

//일정대상 select
function fnTargetSubmit(obj){
	if(obj.value =="2") $('#calendarPrintForm #grpResetFlag').val('true');
	else $("#choiGrpDiv").html('');
	$('#calendarPrintForm #myGrp').val('');
	calendarPrintForm.submit();
	return;
	if(obj.value =="2") setDisabled($('#calendarPrintForm #myGrp'), false);
	else{
		$('#calendarPrintForm #myGrp').val('');
		calendarPrintForm.submit();
	}
};
<!--
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
		$('#tabNo').val('0');
	}
	else if($('#submitTab').val() == 'welyList'){
		$('#tabNo').val('1');
	}
	else if($('#submitTab').val() == 'dalyList'){
		$('#tabNo').val('2');
	}
	$form.submit();
}


function applyTooltip() {
	var obj,tooltipContainer;
	$('.scdArea').mouseover(function() {
		obj = getParentTag(this, 'div');
		tooltipContainer = $(obj).find('#tooltipContainer');
		if(tooltipContainer.html() != ''){
			tooltipContainer.show();
			return;
		}
		
		var $scdObj=$("#"+$(this).attr("id"));
		var $tooltip_title=$("#tooltip_title");
		$tooltip_title.html($scdObj.find("#scds_tooltip_subj").val());
		var $tooltip_registrant = $("#tooltip_registrant");
		$tooltip_registrant.html("<u:msg titleId="wc.cols.book" alt="예약자" /> : " + 	$scdObj.find("#scds_tooltip_regNm").val());
		var $tooltip_startToEndDay = $("#tooltip_startToEndDay");
		$tooltip_startToEndDay.html("<u:msg titleId="wc.cols.bookDt" alt="예약일자" /> : " + $scdObj.find("#scds_tooltip_schdlStartDay").val().replace(/-/g, ".") + " ~ " + $scdObj.find("#scds_tooltip_schdlEndDay").val().replace(/-/g, "."));
		var $tooltip_startToEndTime = $("#tooltip_startToEndTime");
		$tooltip_startToEndTime.html("<u:msg titleId="wc.cols.bookTm" alt="예약시간" /> : " + 	$scdObj.find("#scds_tooltip_schdlStartDt").val() + " ~ " + $scdObj.find("#scds_tooltip_schdlEndDt").val());
		
		$clone = $('#tooltip').clone(true);
		$clone.css('display','block');
		
		tooltipContainer.append($clone);
		tooltipContainer.show();
		
	});
	$('.scdArea').mouseout(function(event) {
		$('div.tooltipCls').hide();
	//$('#tooltip').hide();
	});
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
		$('#action').val(null);

		var $form = $('#calendarPrintForm');

		if($('#submitTab').val() == 'molyList'){

			$form.attr('method','post');
			$form.attr('action','./listNewSchdl.do?menuId=${menuId}');
			
			$('#tabNo').val('0');
			$form.submit();
		}
		else if($('#submitTab').val() == 'welyList'){
			$form.attr('method','post');
			$('#tabNo').val('1');
			$form.attr('action','./listNewSchdl.do?menuId=${menuId}');
			$form.submit();
		}
		else if($('#submitTab').val() == 'dalyList'){
			$form.attr('method','post');
			$('#tabNo').val('2');
			$form.attr('action','./listNewSchdl.do?menuId=${menuId}');
			$form.submit();
		}
	}
function setRowHeight(val){
	if(val = ''){
		$("#dayPromiseRow").css('height', '');
		$("#dayWorkRow").css('height', '');
		$("#dayEventRow").css('height','');
		$("#dayAnnvRow").css('height', '');
	}
	else{
		$("#dayPromiseRow").css('height', $("#dayPromiseRow").height()+"px");
		$("#dayWorkRow").css('height', $("#dayWorkRow").height()+"px");
		$("#dayEventRow").css('height', $("#dayEventRow").height()+"px");
		$("#dayAnnvRow").css('height', $("#dayAnnvRow").height()+"px");
	}
};

function showSchdlList(val){
	if(val==null) val='';
	$('#action').val(null);

	var $form = $('#calendarPrintForm');

	if($('#submitTab').val() == 'molyList'){

		$form.attr('method','post');
		$form.attr('action','./listNewSchdl.do?menuId=${menuId}&fncCal=${fncCal}&catId='+val);
		
		$('#tabNo').val('0');
		$form.submit();
	}
	else if($('#submitTab').val() == 'welyList'){
		$form.attr('method','post');
		$('#tabNo').val('1');
		$form.attr('action','./listNewSchdl.do?menuId=${menuId}&fncCal=${fncCal}&catId='+val);
		$form.submit();
	}
	else if($('#submitTab').val() == 'dalyList'){
		$form.attr('method','post');
		$('#tabNo').val('2');
		$form.attr('action','./listNewSchdl.do?menuId=${menuId}&fncCal=${fncCal}&catId='+val);
		$form.submit();
	}
};

function showProm() {
	toggleButtons('scd_promise');
	setRowHeight(null);

	$(".scd_empty")			.css('display', 'none').parent().css('display', 'none');
	$('.scd_promise')		.css('display', '').parent().parent().css('display', '');
	$('.scd_work')			.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_event')			.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_anniversary')	.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_prev')			.css('display', 'none').parent().parent().css('display', 'none');

}
function showWork() {
	toggleButtons('scd_work');
	setRowHeight(null);

	$(".scd_empty")			.css('display', 'none').parent().css('display', 'none');
	$('.scd_promise')		.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_work')			.css('display', '').parent().parent().css('display', '');
	$('.scd_event')			.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_anniversary')	.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_prev')			.css('display', 'none').parent().parent().css('display', 'none');
}
function showEvnt() {
	toggleButtons('scd_event');
	setRowHeight(null);

	$(".scd_empty")			.css('display', 'none').parent().css('display', 'none');
	$('.scd_promise')		.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_work')			.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_event')			.css('display', '').parent().parent().css('display', '');
	$('.scd_anniversary')	.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_prev')			.css('display', 'none').parent().parent().css('display', 'none');
}
function showAnnv() {
	toggleButtons('scd_anniversary');
	setRowHeight(null);

	$(".scd_empty")			.css('display', 'none').parent().css('display', 'none');
	$('.scd_promise')		.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_work')			.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_event')			.css('display', 'none').parent().parent().css('display', 'none');
	$('.scd_anniversary')	.css('display', '').parent().parent().css('display', '');
	$('.scd_prev')			.css('display', 'none').parent().parent().css('display', 'none');
}

function changeDt() {
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
			'welyList': "${wcScdCalWeek.year}" + "-"  + convWelyMonth + " " +"<c:choose><c:when test="${wcScdCalWeek.week == 1}"><u:msg titleId="wc.cols.week1" alt="1주차" /></c:when><c:when test="${wcScdCalWeek.week == 2}"><u:msg titleId="wc.cols.week2" alt="2주차" /></c:when><c:when test="${wcScdCalWeek.week == 3}"><u:msg titleId="wc.cols.week3" alt="3주차" /></c:when><c:when test="${wcScdCalWeek.week == 4}"><u:msg titleId="wc.cols.week4" alt="4주차" /></c:when><c:when test="${wcScdCalWeek.week == 5}"><u:msg titleId="wc.cols.week5" alt="5주차" /></c:when><c:when test="${wcScdCalWeek.week == 6}"><u:msg titleId="wc.cols.week6" alt="6주차" /></c:when><c:otherwise></c:otherwise></c:choose>",
			'dalyList':"${wcScdCalDay.year}" + "-"  + convDalyMonth + "-" + convDalyDay
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
	if($('#schdlKndCd').val() == 1){
		//openSingUser();
		searchOthrUserTop(event, opt, "calendarPrintForm");
	}
	else openSingOrg();
};


//1명의 사용자 선택
function openSingUser(){
	var $view = $("#calendarPrintForm");
	var data = {
		userUid:$view.find("#viewUserUid").val()};

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
	$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
	$('#tabNo').val('0');
	$form.submit();
	}else if($('#submitTab').val() == 'welyList'){
	$form.attr('method','post');
	$('#tabNo').val('1');
	$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
	$form.submit();
	}else if($('#submitTab').val() == 'dalyList'){
	$form.attr('method','post');
	$('#tabNo').val('2');
	$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
	$form.submit();
	}
	}
	});

}



//1 부서 선택
function openSingOrg(){
	var $view = $("#calendarPrintForm");
	var data = {
		orgId:$view.find("#viewOrgId").val()};

	searchOrgPop({data:data}, function(orgVo){
	if(orgVo!=null){
	$view.find("#viewOrgId").val(orgVo.orgId);
	$view.find("#viewOrgNm").val(orgVo.rescNm);
	$view.find("#othr").val(orgVo.rescNm);
	var $form = $('#calendarPrintForm');
	$("#bumk option:eq(0)").attr("selected", "selected");
	if($('#submitTab').val() == 'molyList'){

	$form.attr('method','post');
	$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
	$('#tabNo').val('0');
	$form.submit();
	}else if($('#submitTab').val() == 'welyList'){
	$form.attr('method','post');
	$('#tabNo').val('1');
	$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
	$form.submit();
	}else if($('#submitTab').val() == 'dalyList'){
	$form.attr('method','post');
	$('#tabNo').val('2');
	$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
	$form.submit();
	}
	}
	//return false;// 창이 안닫힘
	});
}


function agntSelect(obj){
	$("#bumk option:eq(0)").attr("selected", "selected");
	$("#viewUserUid").val("");
	$("#viewUserNm").val("");
	$("#viewOrgId").val("");
	$("#viewOrgNm").val("");
	$("#othr").val("");
	var $form = $('#calendarPrintForm');
	$('#calendarPrintForm #agnt').val(obj.value);
	
	$('#calendarPrintForm #schdlKndCd').val("1");
	$("#calendarPrintForm #schdlTypCd option:eq(0)").attr("selected", "selected");
	$("#calendarPrintForm #myGrp option:eq(0)").attr("selected", "selected");
	
	if($('#submitTab').val() == 'molyList'){

		$form.attr('method','post');
		$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$('#tabNo').val('0');
		$form.submit();
	}
	else if($('#submitTab').val() == 'welyList'){
		$form.attr('method','post');
		$('#tabNo').val('1');
		$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
	}
	else if($('#submitTab').val() == 'dalyList'){
		$form.attr('method','post');
		$('#tabNo').val('2');
		$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
	}


}

//즐겨찾기 select
function bumkSelect(obj){
	if(obj.value=='-1'){
		location.href="./listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}";
		return;
	}
	$("#agnt option:eq(0)").attr("selected", "selected");
	$("#viewUserUid").val("");
	$("#viewUserNm").val("");
	$("#viewOrgId").val("");
	$("#viewOrgNm").val("");
	$("#othr").val("");
	var $form = $('#calendarPrintForm');
	if($('#submitTab').val() == 'molyList'){
		$('#tabNo').val('0');
	}
	else if($('#submitTab').val() == 'welyList'){
		$('#tabNo').val('1');
	}
	else if($('#submitTab').val() == 'dalyList'){
		$('#tabNo').val('2');
	}
	$form.submit();
};

//즐겨찾기 추가 팝업
function setBumpPopOpen(){
	if($("#othr").val()!=''){
		var schdlKndCd = $('#calendarPrintForm #schdlKndCd').val();
		var url="./setBumkPop.do?menuId=${menuId}&schdlKndCd="+schdlKndCd+"&fncCal=${fncCal}&fncCalSub="+(schdlKndCd == '1' ? 'psn' : 'dept')+"&viewOrgId="+$("#viewOrgId").val()+"&viewUserUid="+$("#viewUserUid").val()
		+"&viewUserNm="+encodeURIComponent($("#viewUserNm").val())+"&viewOrgNm="+encodeURIComponent($("#viewOrgNm").val());

		dialog.open('setBumkPop','<u:msg titleId="wc.cols.bumkAdd" alt="즐겨찾기 추가" />',url);
	}
	else{
		alert("<u:msg titleId="wc.msg.not.selTarget" alt="조회 대상 선택후 재시도 해주세요." />");
	}
}

//즐겨찾기 수정 팝업
function modBumpPopOpen(){
	<c:if test="${param.bumk != null && param.bumk != '-1'}">
		var schdlKndCd = $('#calendarPrintForm #schdlKndCd').val();
		var url="./setBumkPop.do?menuId=${menuId}&bumkId=${param.bumk }&fncCal=${fncCal}&fncCalSub="+(schdlKndCd == '1' ? 'psn' : 'dept')+"&viewOrgId="+$("#viewOrgId").val()+"&viewUserUid="+$("#viewUserUid").val()
		+"&viewUserNm="+encodeURIComponent($("#viewUserNm").val())+"&viewOrgNm="+encodeURIComponent($("#viewOrgNm").val());

		dialog.open('setBumkPop','<u:msg titleId="wc.cols.bumkmod" alt="즐겨찾기 수정" />',url);
	</c:if>
	<c:if test="${param.bumk == null || param.bumk == '-1'}">
		alert("<u:msg titleId="wc.msg.not.selTargetBumk" alt="즐겨찾기 목록에서 대상을 선택 후 재시도 해주세요" />");
	</c:if>
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
		$form.attr('method','get');
		$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$('#tabNo').val('0');
		$form.submit();
	}
	else if($('#submitTab').val() == 'welyList'){
		$("#welyYear").val(year);
		$("#welyMonth").val(month);
		$("#welyWeek").val(getSecofWeek(date));
		$form.attr('method','get');
		$('#tabNo').val('1');
		$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
		$form.submit();
	}
	else if($('#submitTab').val() == 'dalyList'){
		$("#dalyYear").val(year);
		$("#dalyMonth").val(month);
		$("#dalyDay").val(day);
		$form.attr('method','get');
		$('#tabNo').val('2');
		$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
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
	event = event ? event : window.event; 
	var $view = $("#calendarPrintForm");
	if(gTopUserSearchObj==null){
		gTopUserSearchObj = $("#"+formId+" #othr").first();
		gTopUserSearchTxt = gTopUserSearchObj.attr("title");
	}
	if(eventOpt=='focus'){
		if(gTopUserSearchObj.val()==gTopUserSearchTxt){
			gTopUserSearchObj.val('');
		}
	}
	else if(eventOpt=='blur') {
		if(gTopUserSearchObj.val()==''){
			gTopUserSearchObj.val(gTopUserSearchTxt);
		}
	}
	else if(eventOpt=='keydown' || eventOpt=='click') {
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
					$('#tabNo').val('0');
				}
				else if($('#submitTab').val() == 'welyList'){
					$('#tabNo').val('1');
				}
				else if($('#submitTab').val() == 'dalyList'){
					$('#tabNo').val('2');
				}
				$form.submit();
			}
		}
	}
};



$(document).ready(function() {
setUniformCSS();
changeTab('schdlTab','${tabNo}');
initTodayTd('${tabNo}');
//showAll();
applyTooltip();
});
//-->
</script>

<div class="front notPrint">
	<div class="front_left">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding:3px 4px 0 0px">
				<u:title titleId="wc.jsp.listPsnSchdl.${fncCal}.title" alt="개인 일정" menuNameFirst="true"/>
			</td>
			<c:if test="${fncCal == 'my'}">
				<td class="width5"></td>
				<td class="frontinput">
					<select id="agnt"  name="agnt" onchange="agntSelect(this);">
						<u:option value="-1" titleId="wc.cols.schdlAgnt" alt="일정대리수행"/>
						<c:forEach  var="agntItem" items="${wcAgntVos}" varStatus="status">
							<u:option value="${agntItem.userUid}" title="${agntItem.rescNm }" alt="${agntItem.rescNm }" selected="${agntItem.userUid eq agnt }"/>
						</c:forEach>
					</select>
				</td>
			</c:if>
	 		</tr>
		</table>
	</div>
</div>
				
<input id="scds_promGstUid" type="hidden" value=""/>
<input id="scds_promGstNm" type="hidden" value=""/>
<input id="scds_promGstDptNm" type="hidden" value=""/>

<% // TAB %>
<div id="tabDiv" class="notPrint">
	<u:tabGroup id="schdlTab" >
	<u:tab id="schdlTab" areaId="molyList" titleId="wc.jsp.listPsnSchdl.tab.molySchdl" alt="월간일정" on="true" onclick="selectTab(0)" />
	<u:tab id="schdlTab" areaId="welyList" titleId="wc.jsp.listPsnSchdl.tab.welySchdl" alt="주간일정" onclick="selectTab(1)"/>
	<u:tab id="schdlTab" areaId="dalyList" titleId="wc.jsp.listPsnSchdl.tab.dalySchdl" alt="일간일정" onclick="selectTab(2)"/>
	<u:tabButton titleId="wc.btn.set.nat" alt="국가설정" href="javascript:setNatPop(false);" auth="W" />
	<u:tabButton titleId="wc.btn.chn.nat" alt="국가변경" href="javascript:setNatPop(true);" auth="W" />
	<u:tabButton titleId="cm.btn.reg" alt="등록" href="javascript:setSchdlPop();" auth="W"/>
	<u:tabButton titleId="cm.btn.print" alt="인쇄" img="ico_print.png" imgW="14" onclick="printWeb();" />
	</u:tabGroup>
</div>

<% // 상단 FRONT %>
<div class="front">

	<div class="front_left">

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
			
			<!-- 대리자 정보 추가 -->
			<u:input type="hidden" id="agnt" name="agnt"  value="${param.agnt}"/>
			
		<div id="choiGrpDiv" name="choiGrpDiv" style="display:none">
			<input id='grpResetFlag' name='grpResetFlag' type='hidden' value='${grpResetFlag }'>
		</div>
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="frontico"><a href="javascript:selectMonth('before')"><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
				<td id="todayTd" class="scd_head">&nbsp;</td>
				<td class="frontico"><a href="javascript:selectMonth('after')"><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
				<c:if test="${fncCal == 'open'}">
				<td class="fronttit"><u:msg titleId="wc.cols.schTarget" alt="조회대상"/></td>
				<td class="frontinput">
					<select id="schdlKndCd"  name="schdlKndCd" onchange="fnOpenSchdlCdSelect(this);">
						<u:option value="1" titleId="wc.option.otherPsnSchdl" alt="타인일정" checkValue="${param.schdlKndCd}"/>
						<u:option value="3" titleId="wc.option.otherDeptSchdl" alt="타부서일정" checkValue="${param.schdlKndCd}"/>
					</select> 
				</td>
				<td class="frontinput"><u:input id="othr" value="${othr}" titleId="cols.othr" onfocus="searchOthrUserTop(event, 'focus', 'calendarPrintForm')" onblur="searchOthrUserTop(event, 'blur', 'calendarPrintForm')" onkeydown="searchOthrUserTop(event, 'keydown', 'calendarPrintForm')"  /></td>
				<td class="frontbtn"><u:buttonS titleId="cm.btn.noml.pop" alt="조회" href="javascript:;" onclick="findOther('${empty param.schdlKndCd ? 1 : param.schdlKndCd}',event,'click');"/></td>
				<td class="frontinput"  >
					<select id="bumk" name="bumk" onchange="bumkSelect(this);" style="width:170px;">
					<u:option value="-1" titleId="wc.cols.bumkTarget" alt="즐겨찾기"/>
					<c:forEach  var="bumkItem" items="${bumkList}" varStatus="status">
						<u:set var="bumkId" test="${empty param.schdlKndCd || param.schdlKndCd == '1' }" value="${bumkItem.bumkTgtUid }" elseValue="${bumkItem.bumkTgtDeptId }"/>
						<u:option value="${bumkId }:${bumkItem.bumkId }" title="${bumkItem.bumkDispNm}" checkValue="${param.bumk }"/>
					</c:forEach>
					</select>
				</td>
				<td class="frontbtn">
					<u:buttonS href="javascript:setBumpPopOpen()" titleId="cm.btn.addBumk" alt="즐겨찾기에추가" />
					<u:buttonS href="javascript:modBumpPopOpen()" titleId="cm.btn.modBumk" alt="즐겨찾기수정" />
				</td>
				</c:if>
				<c:if test="${fncCal == 'my'}">
				<%-- <td class="fronttit"><u:msg titleId="cols.schdlKnd" alt="일정종류"/></td>
				<td class="frontinput">
					<select id="schdlTypCd"  name="schdlTypCd" onchange="showSchdlList(this.value);">
					<u:option value="" titleId="cm.option.all" alt="전체선택"/>
					<c:forEach  var="list" items="${wcCatClsBVoList}" varStatus="status">
					<u:option value="${list.catId}" title="${list.catNm }" checkValue="${param.schdlTypCd}"/>
					</c:forEach>
					</select>
				</td> --%>
				<td class="fronttit"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></td>
				<td class="frontinput">
					<select id="schdlKndCd"  name="schdlKndCd" onchange="fnTargetSubmit(this);">
					<u:option value="" titleId="cm.option.all" alt="전체선택"/>
					<c:forEach  var="list" items="${schdlKndCdList}" varStatus="status">
					<u:option value="${list[0]}" title="${list[1] }" checkValue="${param.schdlKndCd}"/>
					</c:forEach>
					</select>
				</td>
				<td class="fronttit"><u:msg titleId="wc.cols.grpSelect" alt="그룹선택"/></td>
				<td class="frontinput">
					<u:set var="myGrpDisabled" test="${param.schdlKndCd eq '2' }" value="" elseValue="disabled='disabled'"/>
					<select id="myGrp" name="myGrp" onchange="fnGrpSelect(this);" ${myGrpDisabled }>
					<u:option value="" titleId="cm.option.all" alt="전체선택"/>
					<c:forEach  var="grpItem" items="${wcSchdlGroupBVoList}" varStatus="status">
					<u:option value="${grpItem.schdlGrpId}" title="${grpItem.grpNm }" checkValue="${param.myGrp}"/>
					</c:forEach>
					</select>
				</td>
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

<!-- 월간일정 S -->
<div id="molyList" class="listarea" style="display: none;">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
<tr>
	<td width="15%" class="scdhead"><strong><u:msg titleId="wc.cols.sun" alt="일" /></strong></td>
	<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.mon" alt="월" /></strong></td>
	<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.tue" alt="화" /></strong></td>
	<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.wed" alt="수" /></strong></td>
	<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.thu" alt="목" /></strong></td>
	<td width="14%" class="head_ct"><strong><u:msg titleId="wc.cols.fri" alt="금" /></strong></td>
	<td width="15%" class="head_ct"><strong><u:msg titleId="wc.cols.sat" alt="토" /></strong></td>
</tr>
<c:set var="maxRowIndex" value="4"/>
<c:forEach  var="calWeek" items="${wcScdCalMonth.weeks}" varStatus="weekStatus">
<tr>
	<c:forEach  var="calDay" items="${calWeek.days}" varStatus="dayStatus">
	<td class="${calDay.toDayFlag}">
		<span class="${calDay.holiFlag}" onclick="setSchdlPop('${calDay.year}','${calDay.month}','${calDay.day}');" style="${writeAuth == 'Y' ? 'cursor:pointer;' : ''}"><u:out value="${calDay.day}"/></span>

		<c:forEach var="scds" items="${calDay.scds}" varStatus="status">
		<c:if test="${scds.schdlTypCd == '5' and status.index==0}">
		<c:choose>
		<c:when test="${calDay.holiFlag == 'scddate_prev'}"> <c:set var="scds_color"	value= "scd_prev" /> </c:when>
		<c:when test="${calDay.holiFlag == 'scddate_red_prev'}"><c:set var="scds_color"	value= "scd_red_prev" /></c:when>
		<c:when test="${calDay.holiFlag == 'scddate_red'}"> <c:set var="scds_color"	value= "scd_red" /> </c:when>
		<c:otherwise><c:set var="scds_color"	value= "scd_scddate" /></c:otherwise>
		</c:choose>
		<span class="${scds_color}" title="${scds.subj}"><u:out value="${scds.subj}" maxLength="17"/></span><br>
		</c:if>
		</c:forEach>

		<%-- <c:set var="maxSchdsIndex" value="${calDay.scdMaxIndex}"/> --%>
		<!-- 더보기 여부 -->
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
		</c:otherwise>
		</c:choose>
			<c:set var="moreParam" value="${calDay.year }-${calDay.month }-${calDay.day }-${calDay.dayOfTheWeek }"/>
			<u:set var="moreDisplay" test="${tempIndex >= maxRowIndex }" value="display:none;" elseValue=""/>
			<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
			<u:set var="catBgCss" test="${(!empty mapList && !empty mapList.bgcolCd) && ( !empty scds.schdlRepetState || scds.alldayYn eq 'Y')}" value="style='width:100%; font-size:11px; color:#454545; background:${mapList.bgcolCd }; border-top:1px solid ${mapList.bgcolCd }; border-bottom:1px solid ${mapList.bgcolCd };border-right:1px solid ${mapList.bgcolCd };line-height:16px; padding:2px 3px 0 0px;margin-top:1px;${moreDisplay }'" elseValue="style='padding: 2px 3px 0px 0px;height:18px;margin-top:1px;${moreDisplay }'"/>
			<div ${catBgCss } class="more_${moreParam }_month${!empty scds.subj ? '' : 'none'} ellipsis">
				<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
				<u:msg titleId="cols.luna" alt="음력" var="luna"/>
				
				<c:if test="${scds.solaLunaYn eq 'N'}">
				<fmt:parseDate var="dateLunaStartDt" value="${scds.schdlLunaStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convLunaStartDt" value="${dateLunaStartDt}" pattern="yyyy-MM-dd"/>
				<c:set var="scds_tooltip_schdlStartDay" value="(${luna } :${convLunaStartDt}) ${convStartDt}"/>
				</c:if>
				<c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
				<c:set var="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
				</c:if>

				<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="yyyy-MM-dd"/>

				<c:if test="${scds.solaLunaYn=='N'}">
				<fmt:parseDate var="dateLunaEndDt" value="${scds.schdlLunaEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convLunaEndDt" value="${dateLunaEndDt}" pattern="yyyy-MM-dd"/>
				<c:set var="scds_tooltip_schdlEndDay" value="(${luna } :${convLunaEndDt}) ${convEndDt}"/>
				</c:if>
				<c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
				<c:set var="scds_tooltip_schdlEndDay" value="${convEndDt}"/>
				</c:if>
				
				<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="HH:mm"/>
				<c:set var="scds_tooltip_schdlStartDt" value="${convStartDt}"/>

				<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="HH:mm"/>
				<c:set var="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>
			
			
				<!-- tootip Container -->
				<c:if test="${!empty scds.subj }">
					<div id="tooltipContainer" class="tooltipCls" style="position:absolute;display:none;z-index:999;margin-top:14px;">
						<div id="tooltip" class="tooltip" style="position:absolute;display:block;z-index:999;">
							<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
							<div class="tooltip_body">
								<div class="tooltip_text" >
									<ul>
										<li ><div  class="ellipsis" style="width:200px"><strong  id="tooltip_title"><u:out value="${scds.subj}" /></strong></div></li>
										<li class="blank_s2" ></li>
										<li class="tooltip_line"></li>
										<li class="blank_s5"></li>
										<li>
											<span id="tooltip_registrant"><u:msg titleId="wc.cols.book" alt="예약자" /> : ${scds.regrNm}</span><br>
											<span id="tooltip_startToEndDay"><u:msg titleId="wc.cols.bookDt" alt="예약일자" /> : ${scds_tooltip_schdlStartDay} ~ ${scds_tooltip_schdlEndDay}</span><br>
											<span id="tooltip_startToEndTime"><u:msg titleId="wc.cols.bookTm" alt="예약시간" /> : ${scds_tooltip_schdlStartDt} ~ ${scds_tooltip_schdlEndDt}</span><br>
											<span id="tooltip_content"></span><br>
										</li>
									</ul>
								</div>
							</div>
						</div> 
					</div>
				</c:if>



				<u:set var="catFontCss" test="${!empty mapList && !empty mapList.fontColrCd}" value="style='font-size:11px; color:${empty scds.schdlRepetState && scds.alldayYn ne 'Y' ? mapList.bgcolCd : mapList.fontColrCd }; line-height:16px; padding:0 3px 0 4px;'" elseValue="style='font-size:11px; line-height:16px; padding:0 3px 0 4px;'"/>
				<a href="javascript:viewSchdlPop('${scds.schdlId}');">
				<fmt:parseDate var="dateTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				
				<span class="scdArea scd_${scds.schdlTypCd } ${scds_color eq 'scd_prev' ? scds_color : ''}" id="${scds.schdlId}_month" ${catFontCss }>
				<c:if test="${scds.alldayYn ne 'Y' }"><fmt:formatDate value="${dateTempParse}" pattern="HH:mm"/> </c:if><u:out value="${scds.subj}" />

		
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
			<c:choose>
				<c:when test="${maxSchdsIndex >= maxRowIndex }">
					<div class=""  style="margin-top:1px;height:22px;float:right;" id="emptyScd">
						<u:buttonS href="javascript:;" onclick="listMorePop('${moreParam }','month');" titleId="cm.btn.more" alt="더보기" />
					</div>
					<div id="con_${moreParam }_month" class="moreContainer" style="position:absolute;border:2px solid #6e6e6e;z-index:999;background:#ffffff;width:220px;display:none;padding:2px;">
						<div id="titleContainer" style="background:#f2f2f2;text-align:center;height:22px;vertical-align:middle;padding:3px 2px 0px 3px;"><strong id="titleYmd" style="float:left;font-size:13px;color:#1c1c1c;"></strong><span style="float:right;"><a href="javascript:;" onclick="listMorePop('${moreParam }','month');"><img src="${_cxPth}/images/${_skin}/btn_close11.gif" alt="닫기" width="15" height="14"/></a></span></div>
						<div id="contentsContainer" style="background:#ffffff;height:120px;overflow-y:auto;width:100%;"></div>
					</div>
				</c:when>
				<c:otherwise>
					<u:set var="emptyMaxCnt" test="${maxSchdsIndex eq '-1' }" value="${maxRowIndex }" elseValue="${maxRowIndex - maxSchdsIndex }"/>
					<c:forEach var="emptyList" begin="1" end="${emptyMaxCnt}" step="1">
						<div class=""  style="margin-top:1px;height:18px;" id="emptyScd">&nbsp;</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</td>
		</c:forEach>
	</tr>
	</c:forEach>
	</table>
	</div>
	<!-- 월간일정  E -->


	<!-- 주간일정 S -->
<div id="welyList" class="listarea" style="display: none;">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="width:100%;table-layout:fixed;">
	<!-- 날짜 표기 S -->
	<tr>
		<c:forEach  var="weekCalDay" items="${wcScdCalWeek.days}" varStatus="status">
		<c:choose>
		<c:when test="${status.index==0  || weekCalDay.holiFlag == 'scddate_red' || weekCalDay.holiFlag == 'scddate_red_prev' }">
		<c:set var="day_num_style"	value= "scdhead_week" />
		</c:when>
		<c:otherwise>
		<c:set var="day_num_style"	value= "head_lt" />
		</c:otherwise>
		</c:choose>
		<c:choose>
		<c:when test="${status.index==0}">
		<td width="15%" class="${day_num_style}" onclick="setSchdlPop('${weekCalDay.year}','${weekCalDay.month}','${weekCalDay.day}');" style="${writeAuth == 'Y' ? 'cursor:pointer;' : ''}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.sun" alt="일" /></td>
		</c:when>
		<c:when test="${status.index==1}">
		<td width="14%" class="${day_num_style}" onclick="setSchdlPop('${weekCalDay.year}','${weekCalDay.month}','${weekCalDay.day}');" style="${writeAuth == 'Y' ? 'cursor:pointer;' : ''}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.mon" alt="월" /></td>
		</c:when>
		<c:when test="${status.index==2}">
		<td width="14%" class="${day_num_style}" onclick="setSchdlPop('${weekCalDay.year}','${weekCalDay.month}','${weekCalDay.day}');" style="${writeAuth == 'Y' ? 'cursor:pointer;' : ''}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.tue" alt="화" /></td>
		</c:when>
		<c:when test="${status.index==3}">
		<td width="14%" class="${day_num_style}" onclick="setSchdlPop('${weekCalDay.year}','${weekCalDay.month}','${weekCalDay.day}');" style="${writeAuth == 'Y' ? 'cursor:pointer;' : ''}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.wed" alt="수" /></td>
		</c:when>
		<c:when test="${status.index==4}">
		<td width="14%" class="${day_num_style}" onclick="setSchdlPop('${weekCalDay.year}','${weekCalDay.month}','${weekCalDay.day}');" style="${writeAuth == 'Y' ? 'cursor:pointer;' : ''}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.thu" alt="목" /></td>
		</c:when>
		<c:when test="${status.index==5}">
		<td width="14%" class="${day_num_style}" onclick="setSchdlPop('${weekCalDay.year}','${weekCalDay.month}','${weekCalDay.day}');" style="${writeAuth == 'Y' ? 'cursor:pointer;' : ''}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.fri" alt="금" /></td>
		</c:when>
		<c:when test="${status.index==6}">
		<td width="15%" class="${day_num_style}" onclick="setSchdlPop('${weekCalDay.year}','${weekCalDay.month}','${weekCalDay.day}');" style="${writeAuth == 'Y' ? 'cursor:pointer;' : ''}"><strong><u:out value="${weekCalDay.day}"/></strong> <u:msg titleId="wc.cols.sat" alt="토" /></td>
		</c:when>
		</c:choose>
		</c:forEach>
	</tr>
	<!-- 날짜 표기 E -->
	<tr>
		<c:forEach  var="calDay" items="${wcScdCalWeek.days}" varStatus="dayStatus">
		<c:choose>
		<c:when test="${calDay.toDayFlag != 'scdtd'}">
		<c:set var="today_class"	value= "scd_today_week" />
		</c:when>
		<c:otherwise>
		<c:set var="today_class"	value= "scdtd_week" />
		</c:otherwise>
		</c:choose>

		<td class="${today_class}" height="300">
			<c:set var="scdHoliFindCnt" value= "0" />
			<div style="white-space:nowrap; overflow:hidden;text-align:right;padding:0px;"> 
			<c:forEach var="scds" items="${calDay.scds}" varStatus="status">
				<c:if test="${scds.schdlTypCd == '5' }">
				<c:choose><c:when test="${calDay.holiFlag == 'scddate_prev'}"> <c:set var="scds_color"	value= "scd_prev" /> </c:when><c:when test="${calDay.holiFlag == 'scddate_red_prev'}"><c:set var="scds_color"	value= "scd_prev" /></c:when>
				<c:otherwise><c:set var="scds_color"	value= "scd_red" /></c:otherwise></c:choose><c:set var="scdHoliFindCnt" value= "${scdHoliFindCnt+1 }" />
				<span class="${scds.holiYn eq 'Y' ? 'scddate_red' : 'scddate'}" ><c:if test="${status.index > 0 }">, </c:if><u:out value="${scds.subj}"/></span>
				</c:if>
			</c:forEach>
			<c:if test="${scdHoliFindCnt == 0}">&nbsp;</c:if>
			</div>
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
			</c:otherwise>
			</c:choose>
			<c:set var="maxRowIndex" value="22"/>
			<c:set var="moreParam" value="${calDay.year }-${calDay.month }-${calDay.day }-${calDay.dayOfTheWeek }"/>
			<u:set var="moreDisplay" test="${tempIndex >= maxRowIndex }" value="display:none;" elseValue=""/>
			<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
			<u:set var="catBgCss" test="${(!empty mapList && !empty mapList.bgcolCd) && ( !empty scds.schdlRepetState || scds.alldayYn eq 'Y')}" value="style='width:100%; font-size:11px; color:#454545; background:${mapList.bgcolCd }; border-top:1px solid ${mapList.bgcolCd }; border-bottom:1px solid ${mapList.bgcolCd };border-right:1px solid ${mapList.bgcolCd };line-height:16px; padding:2px 3px 0 0px;margin-top:1px;${moreDisplay }'" elseValue="style='padding: 2px 3px 0px 0px;height:18px;margin-top:1px;${moreDisplay }'"/>
				<div ${catBgCss } class="more_${moreParam }_week${!empty scds.subj ? '' : 'none'} ellipsis">

				<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
				<u:msg titleId="cols.luna" alt="음력" var="luna"/>
				
				<c:if test="${scds.solaLunaYn eq 'N'}">
				<fmt:parseDate var="dateLunaStartDt" value="${scds.schdlLunaStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convLunaStartDt" value="${dateLunaStartDt}" pattern="yyyy-MM-dd"/>
				<c:set var="scds_tooltip_schdlStartDay" value="(${luna } :${convLunaStartDt}) ${convStartDt}"/>
				</c:if>
				<c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
				<c:set var="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
				</c:if>

				<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="yyyy-MM-dd"/>

				<c:if test="${scds.solaLunaYn=='N'}">
				<fmt:parseDate var="dateLunaEndDt" value="${scds.schdlLunaEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convLunaEndDt" value="${dateLunaEndDt}" pattern="yyyy-MM-dd"/>
				<c:set var="scds_tooltip_schdlEndDay" value="(${luna } :${convLunaEndDt}) ${convEndDt}"/>
				</c:if>
				<c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
				<c:set var="scds_tooltip_schdlEndDay" value="${convEndDt}"/>
				</c:if>
				
				<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="HH:mm"/>
				<c:set var="scds_tooltip_schdlStartDt" value="${convStartDt}"/>

				<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="HH:mm"/>
				<c:set var="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>
			
			
				<!-- tootip Container -->
				<c:if test="${!empty scds.subj }">
					<div id="tooltipContainer" class="tooltipCls" style="position:absolute;display:none;z-index:999;margin-top:14px;">
						<div id="tooltip" class="tooltip" style="position:absolute;display:block;z-index:999;">
							<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
							<div class="tooltip_body">
								<div class="tooltip_text" >
									<ul>
										<li ><div  class="ellipsis" style="width:200px"><strong  id="tooltip_title"><u:out value="${scds.subj}" /></strong></div></li>
										<li class="blank_s2" ></li>
										<li class="tooltip_line"></li>
										<li class="blank_s5"></li>
										<li>
											<span id="tooltip_registrant"><u:msg titleId="wc.cols.book" alt="예약자" /> : ${scds.regrNm}</span><br>
											<span id="tooltip_startToEndDay"><u:msg titleId="wc.cols.bookDt" alt="예약일자" /> : ${scds_tooltip_schdlStartDay} ~ ${scds_tooltip_schdlEndDay}</span><br>
											<span id="tooltip_startToEndTime"><u:msg titleId="wc.cols.bookTm" alt="예약시간" /> : ${scds_tooltip_schdlStartDt} ~ ${scds_tooltip_schdlEndDt}</span><br>
											<span id="tooltip_content"></span><br>
										</li>
									</ul>
								</div>
							</div>
						</div> 
					</div>
				</c:if>



					<u:set var="catFontCss" test="${!empty mapList && !empty mapList.fontColrCd}" value="style='font-size:11px; color:${empty scds.schdlRepetState && scds.alldayYn ne 'Y' ? mapList.bgcolCd : mapList.fontColrCd }; line-height:16px; padding:0 3px 0 4px;float:left;'" elseValue="style='font-size:11px; line-height:16px; padding:0 3px 0 4px;float:left;'"/>
					<a href="javascript:viewSchdlPop('${scds.schdlId}');">
					<fmt:parseDate var="dateTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<span class="scdArea scd_${scds.schdlTypCd } ${scds_color}" id="${scds.schdlId}_week" ${catFontCss }>
					<c:if test="${scds.alldayYn ne 'Y' }"><fmt:formatDate value="${dateTempParse}" pattern="HH:mm"/> </c:if><u:out value="${scds.subj}" />
					

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
				<c:if test="${maxSchdsIndex >= maxRowIndex }">
					<div class=""  style="margin-top:1px;height:22px;float:right;" id="emptyScd">
						<u:buttonS href="javascript:;" onclick="listMorePop('${moreParam }','week');" titleId="cm.btn.more" alt="더보기" />
					</div>
					<div id="con_${moreParam }_week" class="moreContainer" style="position:absolute;border:2px solid #6e6e6e;z-index:999;background:#ffffff;width:220px;display:none;padding:2px;">
						<div id="titleContainer" style="background:#f2f2f2;text-align:center;height:22px;vertical-align:middle;padding:3px 2px 0px 3px;"><strong id="titleYmd" style="float:left;font-size:13px;color:#1c1c1c;"></strong><span style="float:right;"><a href="javascript:;" onclick="listMorePop('${moreParam }','week');"><img src="${_cxPth}/images/${_skin}/btn_close11.gif" alt="닫기" width="15" height="14"/></a></span></div>
						<div id="contentsContainer" style="background:#ffffff;height:120px;overflow-y:auto;width:100%;"></div>
					</div>
				</c:if>
			</td>
			</c:forEach>

		</tr>
		</table>
		</div>
		<!-- 주간일정  E -->

		<!-- 일일일정  S -->
		<div id="dalyList" class="listarea" style="display: none;" noBottomBlank="true">
			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tbody>
				<tr>
					<td width="70" class="head_ct"></td>
					<!-- 날짜 및 요일 표기 S -->
					<u:set var="dayHoliCls" test="${wcScdCalDay.dayOfTheWeek == '0'  || wcScdCalDay.holiFlag eq 'scddate_red' }" value="scdhead_week" elseValue="head_lt"/>
					<td class="head_lt" onclick="setSchdlPop('${wcScdCalDay.year}','${wcScdCalDay.month}','${wcScdCalDay.day}');" style="${writeAuth == 'Y' ? 'cursor:pointer;' : ''}">
						<strong class="${dayHoliCls }"><u:out value="${wcScdCalDay.day}"/></strong>
						<span class="${dayHoliCls }">
						<c:forTokens var="dayWeek" items="sun,mon,tue,wed,thu,fri,sat" delims="," varStatus="dowStatus">
							<c:if test="${dowStatus.index == wcScdCalDay.dayOfTheWeek }"><u:msg titleId="wc.cols.${dayWeek }" alt="요일" /></c:if>
						</c:forTokens>
						</span>
						<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
						<c:if test="${scds.schdlTypCd == '5'}">
							<span class="${scds.holiYn eq 'Y' ? 'scdhead_week' : '' }" style="font-size:11px;"><c:if test="${status.index > 0 }">, </c:if> ${scds.subj}</span>
						</c:if>
						</c:forEach>
					</td>
					<!-- 날짜 및 요일 표기  E -->
				</tr>
				<c:set var="schdsSize"	value= "1" />
				<c:set var="moreParam" value="${wcScdCalDay.year }-${wcScdCalDay.month }-${wcScdCalDay.day }-${wcScdCalDay.dayOfTheWeek }"/>
				<tr>
					<td class="scdtd_day" style="height:18px"><span class="scd_gray" ><a href="javascript:;" onclick="listMorePop('${moreParam }','allday');"><u:msg titleId="wc.cols.wholeDay" alt="종일일정" /></a></span>
						<div id="con_${moreParam }_allday" class="moreContainer" style="position:absolute;border:2px solid #6e6e6e;z-index:999;background:#ffffff;width:220px;display:none;padding:2px;">
								<div id="titleContainer" style="background:#f2f2f2;text-align:center;height:22px;vertical-align:middle;padding:3px 2px 0px 3px;"><strong id="titleYmd" style="float:left;font-size:13px;color:#1c1c1c;"></strong><span style="float:right;"><a href="javascript:;" onclick="listMorePop('${moreParam }','allday');"><img src="${_cxPth}/images/${_skin}/btn_close11.gif" alt="닫기" width="15" height="14"/></a></span></div>
								<div id="contentsContainer" style="background:#ffffff;height:120px;overflow-y:auto;width:100%;"></div>
							</div>
					</td>
					<td class="scdtd_day">
						<div class="ellipsis" style="width:95%;">
							<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
								<c:if test="${scds.schdlTypCd != '5' && !empty scds.subj}">
								<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
								<u:set var="catFontCss" test="${!empty mapList && !empty mapList.fontColrCd}" value="style='font-size:11px; color:${mapList.bgcolCd }; line-height:16px; padding:0 3px 0 4px;float:left;'" elseValue="style='font-size:11px; line-height:16px; padding:0 3px 0 4px;float:left;'"/>
								<div class="more_${scds.alldayYn eq 'Y' ? moreParam : ''}_allday" style="padding: 0px 0px 0px 0px;float:left;">


				<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
				<u:msg titleId="cols.luna" alt="음력" var="luna"/>
				
				<c:if test="${scds.solaLunaYn eq 'N'}">
				<fmt:parseDate var="dateLunaStartDt" value="${scds.schdlLunaStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convLunaStartDt" value="${dateLunaStartDt}" pattern="yyyy-MM-dd"/>
				<c:set var="scds_tooltip_schdlStartDay" value="(${luna } :${convLunaStartDt}) ${convStartDt}"/>
				</c:if>
				<c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
				<c:set var="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
				</c:if>

				<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="yyyy-MM-dd"/>

				<c:if test="${scds.solaLunaYn=='N'}">
				<fmt:parseDate var="dateLunaEndDt" value="${scds.schdlLunaEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convLunaEndDt" value="${dateLunaEndDt}" pattern="yyyy-MM-dd"/>
				<c:set var="scds_tooltip_schdlEndDay" value="(${luna } :${convLunaEndDt}) ${convEndDt}"/>
				</c:if>
				<c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
				<c:set var="scds_tooltip_schdlEndDay" value="${convEndDt}"/>
				</c:if>
				
				<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="HH:mm"/>
				<c:set var="scds_tooltip_schdlStartDt" value="${convStartDt}"/>

				<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="HH:mm"/>
				<c:set var="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>
			
			
				<!-- tootip Container -->
				<c:if test="${!empty scds.subj }">
					<div id="tooltipContainer" class="tooltipCls" style="position:absolute;display:none;z-index:999;margin-top:14px;">
						<div id="tooltip" class="tooltip" style="position:absolute;display:block;z-index:999;">
							<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
							<div class="tooltip_body">
								<div class="tooltip_text" >
									<ul>
										<li ><div  class="ellipsis" style="width:200px"><strong  id="tooltip_title"><u:out value="${scds.subj}" /></strong></div></li>
										<li class="blank_s2" ></li>
										<li class="tooltip_line"></li>
										<li class="blank_s5"></li>
										<li>
											<span id="tooltip_registrant"><u:msg titleId="wc.cols.book" alt="예약자" /> : ${scds.regrNm}</span><br>
											<span id="tooltip_startToEndDay"><u:msg titleId="wc.cols.bookDt" alt="예약일자" /> : ${scds_tooltip_schdlStartDay} ~ ${scds_tooltip_schdlEndDay}</span><br>
											<span id="tooltip_startToEndTime"><u:msg titleId="wc.cols.bookTm" alt="예약시간" /> : ${scds_tooltip_schdlStartDt} ~ ${scds_tooltip_schdlEndDt}</span><br>
											<span id="tooltip_content"></span><br>
										</li>
									</ul>
								</div>
							</div>
						</div> 
					</div>
				</c:if>


									<a href="javascript:viewSchdlPop('${scds.schdlId}')" >
									<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
									<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
									<span class="scdArea scd_${scds.schdlTypCd }" id="${scds.schdlId}_day" ${catFontCss }>
										<c:if test="${scds.alldayYn eq 'Y' }">
											<c:if test="${scds.alldayYn ne 'Y' }"><fmt:formatDate value="${dateStartTempParse}" pattern="HH:mm"/>~<fmt:formatDate value="${dateEndTempParse}" pattern="HH:mm"/>&nbsp;</c:if>
											<u:out value="${scds.subj}"/>
										</c:if>
									
		
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
								
								
								
								
								
								
								
								
							<div class="more_${moreParam}_day" style="padding: 0px 0px 0px 0px;float:left;display:none">

				<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
				<u:msg titleId="cols.luna" alt="음력" var="luna"/>
				
				<c:if test="${scds.solaLunaYn eq 'N'}">
				<fmt:parseDate var="dateLunaStartDt" value="${scds.schdlLunaStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convLunaStartDt" value="${dateLunaStartDt}" pattern="yyyy-MM-dd"/>
				<c:set var="scds_tooltip_schdlStartDay" value="(${luna } :${convLunaStartDt}) ${convStartDt}"/>
				</c:if>
				<c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
				<c:set var="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
				</c:if>

				<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="yyyy-MM-dd"/>

				<c:if test="${scds.solaLunaYn=='N'}">
				<fmt:parseDate var="dateLunaEndDt" value="${scds.schdlLunaEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convLunaEndDt" value="${dateLunaEndDt}" pattern="yyyy-MM-dd"/>
				<c:set var="scds_tooltip_schdlEndDay" value="(${luna } :${convLunaEndDt}) ${convEndDt}"/>
				</c:if>
				<c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
				<c:set var="scds_tooltip_schdlEndDay" value="${convEndDt}"/>
				</c:if>
				
				<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="HH:mm"/>
				<c:set var="scds_tooltip_schdlStartDt" value="${convStartDt}"/>

				<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="HH:mm"/>
				<c:set var="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>
			
			
				<!-- tootip Container -->
				<c:if test="${!empty scds.subj }">
					<div id="tooltipContainer" class="tooltipCls" style="position:absolute;display:none;z-index:999;margin-top:14px;">
						<div id="tooltip" class="tooltip" style="position:absolute;display:block;z-index:999;">
							<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
							<div class="tooltip_body">
								<div class="tooltip_text" >
									<ul>
										<li ><div  class="ellipsis" style="width:200px"><strong  id="tooltip_title"><u:out value="${scds.subj}" /></strong></div></li>
										<li class="blank_s2" ></li>
										<li class="tooltip_line"></li>
										<li class="blank_s5"></li>
										<li>
											<span id="tooltip_registrant"><u:msg titleId="wc.cols.book" alt="예약자" /> : ${scds.regrNm}</span><br>
											<span id="tooltip_startToEndDay"><u:msg titleId="wc.cols.bookDt" alt="예약일자" /> : ${scds_tooltip_schdlStartDay} ~ ${scds_tooltip_schdlEndDay}</span><br>
											<span id="tooltip_startToEndTime"><u:msg titleId="wc.cols.bookTm" alt="예약시간" /> : ${scds_tooltip_schdlStartDt} ~ ${scds_tooltip_schdlEndDt}</span><br>
											<span id="tooltip_content"></span><br>
										</li>
									</ul>
								</div>
							</div>
						</div> 
					</div>
				</c:if>


									<a href="javascript:viewSchdlPop('${scds.schdlId}')" >
									<fmt:parseDate var="dateStartTempParse" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
									<fmt:parseDate var="dateEndTempParse" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
									<span class="scdArea scd_${scds.schdlTypCd }" id="${scds.schdlId}_day" ${catFontCss }>
										
											<c:if test="${scds.alldayYn ne 'Y' }"><fmt:formatDate value="${dateStartTempParse}" pattern="HH:mm"/>~<fmt:formatDate value="${dateEndTempParse}" pattern="HH:mm"/>&nbsp;</c:if>
											<u:out value="${scds.subj}"/>
						
		
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
						</div>
					</td>
				</tr>

				</tbody>
			</table>

			<u:blank />

			<table class="listtable" border="0" cellpadding="0" cellspacing="1">
				<tbody>
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
					<td width="70" class="scdtd_day" onclick="setSchdlPop('${wcScdCalDay.year}','${wcScdCalDay.month}','${wcScdCalDay.day}','${tempHour }:00');" style="${writeAuth == 'Y' ? 'cursor:pointer;' : ''}">
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
				</tbody>
			</table>

			<!--popup S-->
			<c:set var="offsetT" value="${(schdsSize*16)+141-(schdsSize>0?2:0)}" />
			<c:set var="hour" value="21" />
			<c:set var="offsetH" value="-2" />
			<c:set var="offsetL" value="84" />
			<c:set var="popW" value="150" />
			<c:set var="popM" value="10" />
			<c:set var="idx" value="0" />
			<!-- 일일 일정 내용 S -->
			<c:set var="weekDays" value="${wcScdCalDay.year}${wcScdCalDay.month < 10 ? '0' : ''}${wcScdCalDay.month}${wcScdCalDay.day < 10 ? '0' : ''}${wcScdCalDay.day}"/>
			<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
				<c:if test="${scds.schdlTypCd != '5' && !empty scds.subj && scds.alldayYn ne 'Y' && idx<7}">
					
					<fmt:parseDate var="dateStartdDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate var="convStartHour" value="${dateStartdDt}" pattern="HH"/>
					<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate var="convEndHour" value="${dateEndDt}" pattern="HH"/>
					<fmt:formatDate var="convStartTime" value="${dateStartdDt}" pattern="HH:mm"/>
					
					<!-- 일정에 대한 시간차이를 구한다. -->
					<fmt:formatDate var="dateStartYmd" value="${dateStartDt}" pattern="yyyyMMdd"/>
	                <fmt:formatDate var="dateEndYmd" value="${dateEndDt}" pattern="yyyyMMdd"/>
					<fmt:formatDate var="convFullStrtTime" value="${dateStartDt}" pattern="yyyyMMddHHmm"/>
	                <fmt:formatDate var="convFullEndTime" value="${dateEndDt}" pattern="yyyyMMddHHmm"/>
					<fmt:formatDate var="strtHour" value="${dateStartDt}" pattern="HH"/>
	                <fmt:formatDate var="endHour" value="${dateEndDt}" pattern="HH"/>
					<c:choose>
						<c:when test="${dateStartYmd == dateEndYmd && fullTimes == convFullStrtTime }">
							<c:set var="convStartHour" value="${convStartHour }"/>
							<c:set var="convEndHour" value="${convEndHour}"/>
						</c:when>
						<c:when test="${dateStartYmd < dateEndYmd && ( weekDays == dateStartYmd || weekDays  == dateEndYmd )  }">
							<c:if test="${weekDays == dateStartYmd }">
								<c:set var="convStartHour" value="${convStartHour }"/>
								<c:set var="convEndHour" value="23"/>
							</c:if>
							<c:if test="${weekDays == dateEndYmd }">
								<c:set var="convStartHour" value="0"/>
								<c:set var="convEndHour" value="${convEndHour}"/>
							</c:if>
						</c:when>
						<c:when test="${dateStartYmd < dateEndYmd && weekDays > dateStartYmd && weekDays  < dateEndYmd }">
							<c:set var="convStartHour" value="0"/>
								<c:set var="convEndHour" value="23"/>
						</c:when>
						<c:otherwise></c:otherwise>
					</c:choose>
	                
					<c:set var="popLeft" value="${idx * (popW + popM) + offsetL}" />
					<c:set var="popTop" value="${convStartHour * hour + offsetT}" />
					<c:set var="popH" value="${((convEndHour+1-convStartHour) * hour + offsetH)-1}" />
					
					<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
					<u:set var="catBgCss" test="${(!empty mapList && !empty mapList.bgcolCd) }" value="style='padding:2px 3px 0 0px; vertical-align:top; background:${mapList.bgcolCd }; border:1px solid #96a3b2;width:${popW}px; height:${popH}px;border-radius:2px;'" elseValue="style='padding:2px 3px 0 0px; vertical-align:top; background:#eef3f7; border:1px solid #96a3b2;width:${popW}px; height:${popH}px;'"/>
					<u:set var="catFontCss" test="${!empty mapList }" value="style='font-size:11px; color:${mapList.fontColrCd }; line-height:16px; padding:0 3px 0 4px;float:left;'" elseValue="style='font-size:11px; line-height:16px; padding:0 3px 0 4px;float:left;'"/>
					<div style="position:absolute; top:${popTop}px; left:${popLeft}px;  visibility:visible;" >
						<div ${catBgCss } class="ellipsis">
							<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
							<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
							<u:msg titleId="cols.luna" alt="음력" var="luna"/>
							
							<c:if test="${scds.solaLunaYn eq 'N'}">
							<fmt:parseDate var="dateLunaStartDt" value="${scds.schdlLunaStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
							<fmt:formatDate var="convLunaStartDt" value="${dateLunaStartDt}" pattern="yyyy-MM-dd"/>
							<c:set var="scds_tooltip_schdlStartDay" value="(${luna } :${convLunaStartDt}) ${convStartDt}"/>
							</c:if>
							<c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
							<c:set var="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
							</c:if>
			
							<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
							<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="yyyy-MM-dd"/>
			
							<c:if test="${scds.solaLunaYn=='N'}">
							<fmt:parseDate var="dateLunaEndDt" value="${scds.schdlLunaEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
							<fmt:formatDate var="convLunaEndDt" value="${dateLunaEndDt}" pattern="yyyy-MM-dd"/>
							<c:set var="scds_tooltip_schdlEndDay" value="(${luna } :${convLunaEndDt}) ${convEndDt}"/>
							</c:if>
							<c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
							<c:set var="scds_tooltip_schdlEndDay" value="${convEndDt}"/>
							</c:if>
							
							<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
							<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="HH:mm"/>
							<c:set var="scds_tooltip_schdlStartDt" value="${convStartDt}"/>
			
							<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
							<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="HH:mm"/>
							<c:set var="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>
			
							<!-- tootip Container -->
							<c:if test="${!empty scds.subj }">
								<div id="tooltipContainer" class="tooltipCls" style="position:absolute;display:none;z-index:999;margin-top:14px;">
									<div id="tooltip" class="tooltip" style="position:absolute;display:block;z-index:999;">
										<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
										<div class="tooltip_body">
											<div class="tooltip_text" >
												<ul>
													<li ><div  class="ellipsis" style="width:200px"><strong  id="tooltip_title"><u:out value="${scds.subj}" /></strong></div></li>
													<li class="blank_s2" ></li>
													<li class="tooltip_line"></li>
													<li class="blank_s5"></li>
													<li>
														<span id="tooltip_registrant"><u:msg titleId="wc.cols.book" alt="예약자" /> : ${scds.regrNm}</span><br>
														<span id="tooltip_startToEndDay"><u:msg titleId="wc.cols.bookDt" alt="예약일자" /> : ${scds_tooltip_schdlStartDay} ~ ${scds_tooltip_schdlEndDay}</span><br>
														<span id="tooltip_startToEndTime"><u:msg titleId="wc.cols.bookTm" alt="예약시간" /> : ${scds_tooltip_schdlStartDt} ~ ${scds_tooltip_schdlEndDt}</span><br>
														<span id="tooltip_content"></span><br>
													</li>
												</ul>
											</div>
										</div>
									</div> 
								</div>
							</c:if>
							<a href="javascript:viewSchdlPop('${scds.schdlId}');">
							<span class="scdArea scd_${scds.schdlTypCd } ${scds_color}"  id="${scds.schdlId}_day" ${catFontCss }>${convStartTime} 
								<u:out value="${scds.subj}" />
							</span>
							</a>
						</div>
				
						<c:if test="${ fn:length(wcScdCalDay.scds)>status.index&&idx==6}">
						<!-- More S-->
						<div class=""  style="margin-top:1px;height:22px;float:right;" id="emptyScd">
							<u:buttonS href="javascript:;" onclick="listMorePop('${moreParam }','day');" titleId="cm.btn.more" alt="더보기" />
						</div>
						<div id="con_${moreParam }_day" class="moreContainer" style="margin-left:-51px;position:absolute;border:2px solid #6e6e6e;z-index:999;background:#ffffff;width:220px;display:none;padding:2px;">
							<div id="titleContainer" style="background:#f2f2f2;text-align:center;height:22px;vertical-align:middle;padding:3px 2px 0px 3px;"><strong id="titleYmd" style="float:left;font-size:13px;color:#1c1c1c;"></strong><span style="float:right;"><a href="javascript:;" onclick="listMorePop('${moreParam }','day');"><img src="${_cxPth}/images/${_skin}/btn_close11.gif" alt="닫기" width="15" height="14"/></a></span></div>
							<div id="contentsContainer" style="background:#ffffff;height:120px;overflow-y:auto;width:100%;"></div>
						</div>
						<!-- More E -->
						</c:if>
						
					</div>
					<c:set var="idx" value="${idx+1}"/>
				</c:if>
			</c:forEach>
			<!-- 일일 일정 내용 E -->
			<!--popup E-->

		</div>
		<!-- 일일일정  E -->

		<u:blank />
		<% // 하단 FRONT %>
		<div class="front">
			<div class="front_left">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="frontbtn"><u:buttonBgS id="scd_all" titleId="wc.btn.allView" alt="전체보기" onclick="showSchdlList(null);" /></td>
						<td class="width5"></td>
						<c:forEach var="list" items="${wcCatClsBVoList }" varStatus="status">
							<td class="frontbtn"><u:buttonBgS id="scd_${list.catId }" title="${list.catNm }" alt="${list.catNm }" onclick="showSchdlList('${list.catId }')" bgCd="${list.bgcolCd eq '#ffffff' ? '' : list.bgcolCd }" fontColrCd="${list.fontColrCd }"/></td>
							<td class="width5"></td>
						</c:forEach>
					</tr>
				</table>
			</div>
		</div>

		<u:blank />

		<% // TOOLTIP %>
		<div id="tooltip" class="tooltip" style="position:absolute;display:none;z-index:999;">
			<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>
			<div class="tooltip_body">
				<div class="tooltip_text" >
					<ul>
						<li ><div  class="ellipsis" style="width:200px"><strong  id="tooltip_title"></strong></div></li>
						<li class="blank_s2" ></li>
						<li class="tooltip_line"></li>
						<li class="blank_s5"></li>
						<li>
							<span id="tooltip_registrant"></span><br>
							<span id="tooltip_startToEndDay"></span><br>
							<span id="tooltip_startToEndTime"></span><br>
							<span id="tooltip_content"></span><br>
						</li>
					</ul>
				</div>
			</div>
		</div>
