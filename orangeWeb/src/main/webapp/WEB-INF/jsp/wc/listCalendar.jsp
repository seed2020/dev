<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"%>
<link rel="stylesheet" href="${_cxPth}/css/calendar/fullcalendar.css" type="text/css" />
<link rel="stylesheet" href="${_cxPth}/css/calendar/fullcalendar.print.css" type="text/css" media="print"/>
<script type="text/javascript" src="${_cxPth}/js/calendar/moment.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar/fullcalendar.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${_cxPth}/js/calendar/lang-all.js" charset="UTF-8"></script>
<c:if test="${empty strtDt }"><jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="defaultDate" /></c:if>
<c:if test="${!empty strtDt }"><c:set var="defaultDate" value="${strtDt }"/></c:if>
<u:set var="viewTyp" test="${empty param.viewTyp }" value="month" elseValue="${param.viewTyp }"/>
<u:set test="${!empty param.fncCal}" var="fncCal" value="${param.fncCal}" elseValue="psn" />
<u:set var="queryParamUserUid" test="${!empty param.agnt && param.agnt ne '-1'}" value="&paramUserUid=${param.agnt }" elseValue=""/>
<!-- 권한 -->
<u:secu auth="W" ><c:set var="writeAuth" value="Y"/></u:secu>
<style>
.fc-day-txt {
    display: inline-block;
    *display:inline: ;
    float: left;
    padding: 1px 0 0 2px;    
    font-size: 12px;
    font-weight: bold;
    zoom: 1;
    vertical-align: top;
    cursor: pointer;
}
</style>
<script type="text/javascript">
<!--<% // [상단버튼:국가조회] 팝업 %>
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
	calendarPrintForm.submit();
};
<% // 상세보기 팝업 %>
function viewSchdlPop(schdlId) {
	//schdl 값 알아내기 위해서 셋팅
	$("#scds_schdlId").val(schdlId);
	var url = './viewSchdlPop.do?${params}&schdlId='+schdlId;
	//url +='${queryParamUserUid}';
	dialog.open('viewSchdlPop','<u:msg titleId="wc.btn.schdlDetail" alt="상세보기" />', url);
}

<% // 등록 팝업 %>
function setSchdlPop(schdlId){
	//쓰기 권한 체크
	if('${writeAuth}' != 'Y') return;
	var url = "./setSchdlPop.do";
	var popTitle = '<u:msg titleId="wc.btn.schdlReg" alt="일정등록" />';
	if(arguments.length > 1){
		var schdlStartDt = arguments[1];
		url+= "?schdlStartDt="+schdlStartDt;
		if(arguments[2] != undefined && arguments[2]!= null && !arguments[2]) url+= "&alldayYn=Y";
	}else{
		if(schdlId != null) {
			url+= "?schdlId="+schdlId;
			popTitle = '<u:msg titleId="wc.btn.schdlMod" alt="일정수정" />';
		}
	}
	url+= url.indexOf('?') > -1 ? '&' : '?';
	url += "&menuId=${menuId}";
	url += "&fncCal=${fncCal}";
	url +='${queryParamUserUid}';
	
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
	
	$form.attr('action','/wc/listNewSchdl.do?fncCal=${fncCal}&menuId=${menuId}');
	$form.submit();

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
<% // 일정 이동 %>
function selectMoveDate(date){
	schdlEvent(null, 'moveDay', date);
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
var totalList = null;
<% // 리로드 %>
function reloadCalendar(){
	dialog.closeAll();
	$('#calendar').fullCalendar('removeEvents');
	$('#calendar').fullCalendar('refetchEvents');
	totalList = $('#calendar').fullCalendar('clientEvents');
}
<% // 카테고리 클릭%>
function showSchdlList(catId){
	if(totalList==null) totalList = $('#calendar').fullCalendar('clientEvents');
	//$.each(totalList, function(index, obj) {
		//$('#calendar').fullCalendar('getView').showEvent(obj);
		//updateEvent
	//});
	var addEvents =[];
	if(catId==null) {
		$('#calendar').fullCalendar('removeEvents');
		$('#calendar').fullCalendar('addEventSource', totalList);	
		return;
	}
	$.each(totalList, function(index, obj) {
		if(obj.schdlTypCd==catId) addEvents.push(obj);
	});
	$('#calendar').fullCalendar('removeEvents');
	$('#calendar').fullCalendar('addEventSource', addEvents);
}

<% // 타이틀 업데이트 %>
function updateSchdlTitle(){
	$('#todayTd').text($('#calendar').fullCalendar('getView').title);
}

<% // 날짜 세팅 %>
function getAddDt(viewObj, strtDt, val){
	if(val=='p') strtDt = moment(strtDt).clone().startOf(viewObj.intervalUnit).add(viewObj.intervalDuration).format('YYYY-MM-DD');
	else if(val=='m') strtDt = moment(strtDt).clone().startOf(viewObj.intervalUnit).subtract(viewObj.intervalDuration).format('YYYY-MM-DD');
	return strtDt;
	
}
<% // 탭|네비 버튼 클릭 %>
function schdlEvent(actKey, val, sDate){
	var $form = $('#calendarPrintForm');
	var url = "./listNewSchdl.do";
	var strtDt = '${defaultDate}';
	if(actKey=='changeView'){
		$form.find('input[name="viewTyp"]').val(val);
	}else{
		$form.find('input[name="viewTyp"]').val('${viewTyp}');
		if(val=='today') strtDt = $('#calendar').fullCalendar('getNow').format('YYYY-MM-DD');
		else if(val=='moveDay') strtDt = sDate;
		else strtDt = getAddDt($('#calendar').fullCalendar('getView'), strtDt, val);
	}
	$form.find("input[name='strtDt']").val(strtDt);
	url+="?"+$form.serialize();
	location.href=url;
}
var isAnnv = false; // 기념일 삽입여부
var colMap = null;
<% // 일정종류 색상코드 조회 %>
function getColMap(key){
	if(colMap==null){
		colMap = new ParamMap();
		<c:forEach var="list" items="${wcCatClsBVoList }" varStatus="status">
			colMap.put('bg_${list.catId}','${list.bgcolCd}');
			colMap.put('font_${list.catId}','${list.fontColrCd}');
		</c:forEach>
	}
	return colMap.get(key);
}<% // 기념일 삽입 %>
function setAnnvList(annvList){
	if(isAnnv) return;
	var vo, day, strtDay, endDay, html, tgtDiv, titles;
	var isCreate;
	$('td.fc-day-number, th.fc-day-header').each(function(index, parent){
		day = moment($(this).attr('data-date')).format('YYYYMMDD');
		isCreate = true;
		titles = '';
		tgtDiv = null;
		$.each(annvList, function(index, obj) {
			vo = obj.map;
			strtDay = moment(vo.schdlStartDt).format('YYYYMMDD');
			endDay = moment(vo.schdlEndDt).format('YYYYMMDD');
			if(strtDay<=day && endDay>=day){
				if(vo.holiYn == 'Y') $(parent).addClass('fc-day-red');
				if(isCreate){
					html='<div class="fc-day-container ellipsis"></div>';
					$(parent).append(html);
				}
				if(tgtDiv==null) tgtDiv = $(parent).find('div.fc-day-container');
				html='<span class="fc-day-info'+(vo.holiYn != 'Y' ? ' fc-day-info-week' : '')+'">';
				html+=vo.subj;
				html+='</span>';
				$(tgtDiv).append(html);
				titles+=titles=='' ? vo.subj : ', '+vo.subj;
				isCreate = false;
			}
		});
		if(titles!='') $(tgtDiv).attr('title', titles);
	});
	
	isAnnv = true;
}
<% // 일정 수정 %>
function updateEvents(event, revertFunc){	
	var param = new ParamMap().getData("calendarPrintForm");
	var dftFmt = 'YYYY-MM-DD HH:mm';
	var start = event.start.clone();
	var endFormat = event.allDay ? 'YYYY-MM-DD' : dftFmt;
	var end = event.end == null ? start.clone() : event.end.clone();
	if(event.end!=null && event.allDay) end.add(-1, 'days');
	if(event.end==null && !event.allDay) end.add(1, 'hour');
	
	var schdlId = event.schdlId;
	var alldayYn = event.allDay ? "Y" : "N";
	param.put('schdlId', schdlId);
	param.put('start', start.format(dftFmt));
	param.put('end', end.format(endFormat));
	param.put('alldayYn', alldayYn);
	
	callAjax('./transSchdlAjx.do?menuId=${menuId}&fncCal=${fncCal}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
			revertFunc();
		}
		if(data.result == 'ok'){
			if(totalList==null) totalList = $('#calendar').fullCalendar('clientEvents');
			if(event.allDay)
				end.add(1, 'days');
			$.each(totalList, function(index, obj) {
				if(schdlId==obj.schdlId){
					obj.start = event.start;
					obj.end = end;
					obj.allDay = event.allDay;
				}
			});
		}
	});
}
<% // 일정조회 %>
function loadEvents(start, end, callback){
	var param = new ParamMap().getData("calendarPrintForm");
	param.put('start', start);
	param.put('end', end);
	param.put('natCd', '${param.natCd}');
	var fncCal = '${fncCal}';
	callAjax('./listSchdlAjx.do?menuId=${menuId}&fncCal=${fncCal}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			var events = [];
			var wcSchdlBVoList = data.wcSchdlBVoList, vo, editable, start, end, isSingle;
			var viewName = $('#calendar').fullCalendar('getView').name;
			$.each(wcSchdlBVoList, function(index, obj) {
				vo = obj.map;
				start = moment(vo.schdlStartDt).clone();
				end = moment(vo.schdlEndDt).clone();
				isSingle = viewName=='month' && vo.alldayYn!='Y' && start.isSame(end,'day');
				editable = (fncCal == 'my' && (vo.schdlKndCd==1 || (vo.schdlKndCd==2 && vo.editYn=='Y'))) || ((fncCal != 'my' && fncCal != 'open') && (vo.schdlKndCd==3 || vo.schdlKndCd==4) );				
				events.push({
					id:vo.schdlId,
					schdlId: vo.schdlId,
                    title: vo.subj,
                    start: start,
                    end: vo.alldayYn=='Y' ? end.add(1, 'days') : end,
                    allDay: vo.alldayYn=='Y',
                    //color: getColMap('bg_'+vo.schdlTypCd),
                    //textColor: getColMap('font_'+vo.schdlTypCd),
                    color: isSingle ? 'transparent' : getColMap('bg_'+vo.schdlTypCd),
                    textColor: isSingle ? getColMap('bg_'+vo.schdlTypCd) : getColMap('font_'+vo.schdlTypCd),
                    schdlTypCd: vo.schdlTypCd,
                    regrNm: vo.regrNm,
                    locNm:  vo.locNm,
                    editable : editable
                });
        	});
			// 기념일
			setAnnvList(data.annvList);
			callback(events);
		}
	});
}

<% // 일정 초기화 %>
function initCalendar(){
	$('#calendar').fullCalendar({
		header: {left: '', center: '', right: ''},
		defaultView:'${viewTyp}',
		height: browser.ie && browser.ver <=7 ? 670 : 650,
		lang: '${_lang}'=='zh' ? 'zh-cn' : '${_lang}',//en,zh-cn
		defaultDate: '${defaultDate}',
		titleRangeSeparator:'~',
		titleFormat: {month: 'YYYY.MM', week: "MM.DD", day: 'YYYY.MM.DD'},
		selectable: true,
		unselectAuto: true,
		dayOfMonthFormat:'D ddd',//요일 날짜
		dayOfDayFormat:'D ddd',
		editable: true,
		eventLimit: true, // allow "more" link when too many events
		//minTime: "06:30:00",//시작시간
	    //maxTime: "24:00:00",//종료시간
	    //slotDuration: '00:30:00', 단위
		slotLabelFormat: 'a hh:mm',
		slotEventOverlap:false,//겹치는 일정 표시(true:겹침,false:겹치지않게)
		dragOpacity: {agenda: 0.5},//드래그 할때 투명도 조절
		defaultTimedEventDuration: '01:00:00', // 타임라인 기본 간격(이동시)
		//tooltip:true, // tooltip 보이기 여부
		allowDisplay:true,
		loading: function(bool) {
			//$('#loading').toggle(bool);
		},
		dayClick: function(date, jsEvent, view) {
			if(view.name=='agendaWeek' || view.name=='agendaDay'){
				setSchdlPop(null, moment(date).format('YYYYMMDDHHmm'), date.hasTime());
			}else{
				setSchdlPop(null, moment(date).format('YYYYMMDD'), null);
			}
	    },
	    eventDrop: function(event, delta, revertFunc) {
	    	updateEvents(event, revertFunc);
		},
		eventResize: function(event, delta, revertFunc, jsEvent, ui, view) {
			updateEvents(event, revertFunc);
		},
	    events: function(start, end, timezone, callback) {
	    	loadEvents(start, end, callback);
	    },
		eventClick: function(event) {//일정 클릭시
	        if (event.schdlId) {
	        	viewSchdlPop(event.schdlId);
	            return false;
	        }
	    },
	    eventAfterAllRender:function(view){				
	    	updateSchdlTitle(); // 타이틀
		},	   
	    eventMouseover:function(event, jsEvent, view ){
	    	tooltipCreate(event, jsEvent);
		},
		eventMouseout:function(event, jsEvent, view ){
			$('div.fc-tooltip').hide();
		},	    
		timeFormat: 'HH:mm' // uppercase H for 24-hour clock
	});
}

<% // tooltip 생성 %>
function tooltipCreate(event, jsEvent){
	var areaId = '#tooltip_'+event.schdlId;	
	var title = event.title;
	var regrNm = event.regrNm;
	var locNm = event.locNm;
	var durationDt = event.start.clone().format('YYYY-MM-DD')+' ~ '+(event.allDay ? moment(event.end).clone().subtract(1,'days') : moment(event.end).clone()).format('YYYY-MM-DD');
	var durationTime = (event.allDay ? '<u:msg titleId="wc.cols.wholeDay" alt="종일일정" />' : event.start.format('a hh:mm')+' ~ '+moment(event.end).format('a hh:mm'));
	if($(areaId).html()==undefined){
		var html='<div id="tooltip_'+event.schdlId+'" class="fc-tooltip"><div id="tooltip" class="tooltip">';
		//html+='<div class="tooltip_arrow"><img src="${_ctx}/images/${_skin}/arrow_lt.png"></div>';
		html+='<div class="tooltip_body"><div class="tooltip_text" ><ul>';
		html+='<li><div class="ellipsis" style="width:200px"><strong id="title">'+title+'</strong></div></li>';
		if((browser.ie && browser.ver==7)){
			html+='<li class="tooltip_line"></li>';
		}else{
			html+='<li class="blank_s2" ></li>';
			html+='<li class="tooltip_line"></li>';
			html+='<li class="blank_s5"></li>';
		}
		html+='<li><u:msg titleId="cols.regr" alt="등록자" /> : <span id="regrNm">'+regrNm+'</span></li>';		
		html+='<li><u:msg titleId="cols.prd" alt="기간" /> : <span id="durationDt">'+durationDt+'</span></li>';
		html+='<li><u:msg titleId="wc.cols.time" alt="시간" />: <span id="durationTime">'+durationTime+'</span></li>';
		html+='<li'+(locNm===undefined || locNm=='' ? ' style="display:none;"' : '')+'><u:msg titleId="cols.loc" alt="장소" />: <span id="locNm">'+locNm+'</span></li>';
		html+='<span id="tooltip_content"></span>';
		html+='</li></ul></div>';		
		html+='</div></div>';
		$('body').append(html);
	}else{
		var id = null;
		$(areaId).find('span, strong').each(function(){
			id = $(this).attr('id');
			if(id=='title') $(this).text(title);
			else if(id=='regrNm') $(this).text(regrNm);
			else if(id=='durationDt') $(this).text(durationDt);
			else if(id=='durationTime') $(this).text(durationTime);
			else if(id=='locNm'){
				$(this).text(locNm);
				$li=$(this).closest('li');
				if(locNm!=undefined && locNm!='') $li.show();
				else $li.hide();
			}
		});
	}
	var maxWidth = $(document).width();
	if(((jsEvent.pageX-20)+210)>maxWidth) $(areaId).css('left', maxWidth-230);
	else $(areaId).css('left', jsEvent.pageX - 20);
	$(areaId).css('top', jsEvent.pageY + 10);
    $(areaId).show();
}
<% // [POPUP] 개인설정 %>
function setUserSetupPop(){
	dialog.open('setUserSetupDialog','<u:msg titleId="cm.btn.setup" alt="설정" />', './setUserSetupPop.do?menuId=${menuId}&fncCal=${param.fncCal}');
}

$(document).ready(function() {
	setUniformCSS();	
	initCalendar(); // 일정 초기화
	var wrapper=$('div.homewrapper');
	var classList=wrapper.attr('class');
	$.each(classList.split(' '), function(index, name){
		if(name.indexOf('print')>-1) wrapper.removeClass(name);
	});
	wrapper.addClass('printAp10');
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
							<u:option value="${agntItem.userUid}" title="${agntItem.rescNm }" alt="${agntItem.rescNm }" selected="${agntItem.userUid eq param.agnt }"/>
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
	<u:tab id="schdlTab" areaId="molyList" titleId="wc.jsp.listPsnSchdl.tab.molySchdl" alt="월간일정" onclick="schdlEvent( 'changeView', 'month' );" on="${viewTyp eq 'month' }"/>
	<u:tab id="schdlTab" areaId="welyList" titleId="wc.jsp.listPsnSchdl.tab.welySchdl" alt="주간일정" onclick="schdlEvent( 'changeView', 'agendaWeek' );" on="${viewTyp eq 'agendaWeek' }"/>
	<u:tab id="schdlTab" areaId="dalyList" titleId="wc.jsp.listPsnSchdl.tab.dalySchdl" alt="일간일정" onclick="schdlEvent( 'changeView', 'agendaDay' );" on="${viewTyp eq 'agendaDay' }"/>
	<u:msg var="schdlTgtMsg" titleId="wc.cols.schdlKndCd" />
	<u:msg var="srtupMsg" titleId="cm.btn.setup" />
	<c:if test="${param.fncCal eq 'my' }"><u:tabButton title="${schdlTgtMsg } ${srtupMsg }" alt="설정" href="javascript:setUserSetupPop();" auth="W" /></c:if>
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
			<u:input type="hidden" name="viewUserUid" value="${param.viewUserUid}"/>
			<u:input type="hidden" name="viewUserNm" value="${param.viewUserNm}"/>
			<u:input type="hidden" name="viewOrgId" value="${param.viewOrgId}"/>
			<u:input type="hidden" name="viewOrgNm" value="${param.viewOrgNm}"/>
	
			<u:input type="hidden" name="fncCal"  value="${fncCal}"/>
			
			<!-- 대리자 정보 추가 -->
			<u:input type="hidden" id="agnt" name="agnt"  value="${param.agnt}"/>
			
			<u:input type="hidden" id="viewTyp" value="${viewTyp}"/><!-- 탭 -->
			<u:input type="hidden" id="strtDt" value="${strtDt}"/><!-- 시작일자 -->
			
		<div id="choiGrpDiv" name="choiGrpDiv" style="display:none"></div>
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>				
				<td id="todayTd" class="scd_head fc-title-header" style="margin:0px;padding:5px 0 0 2px;">&nbsp;</td>
				<td style="width:9px;">&nbsp;</td>
				<td class="frontico notPrint"><a href="javascript:schdlEvent(null, 'm');"><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td>
				<td class="frontico notPrint"><a href="javascript:schdlEvent(null, 'p');"><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td>
				<td class="frontico notPrint"><u:buttonS href="javascript:schdlEvent(null, 'today');" titleId="wc.btn.today" alt="오늘" auth="W" /></td>
				<c:if test="${fncCal == 'open'}">
				<td class="fronttit notPrint"><u:msg titleId="wc.cols.schTarget" alt="조회대상"/></td>
				<td class="frontinput notPrint">
					<select id="schdlKndCd"  name="schdlKndCd" onchange="fnOpenSchdlCdSelect(this);">
						<u:option value="1" titleId="wc.option.otherPsnSchdl" alt="타인일정" checkValue="${param.schdlKndCd}"/>
						<u:option value="3" titleId="wc.option.otherDeptSchdl" alt="타부서일정" checkValue="${param.schdlKndCd}"/>
					</select> 
				</td>
				<u:set var="othr" test="${param.schdlKndCd eq '1'}" value="${param.viewUserNm }" elseValue="${param.viewOrgNm }"/>
				<td class="frontinput notPrint"><u:input id="othr" value="${othr}" titleId="cols.othr" onfocus="searchOthrUserTop(event, 'focus', 'calendarPrintForm')" onblur="searchOthrUserTop(event, 'blur', 'calendarPrintForm')" onkeydown="searchOthrUserTop(event, 'keydown', 'calendarPrintForm')"  /></td>
				<td class="frontbtn notPrint"><u:buttonS titleId="cm.btn.noml.pop" alt="조회" href="javascript:;" onclick="findOther('${empty param.schdlKndCd ? 1 : param.schdlKndCd}',event,'click');"/></td>
				<td class="frontinput notPrint"  >
					<select id="bumk" name="bumk" onchange="bumkSelect(this);" style="width:170px;">
					<u:option value="-1" titleId="wc.cols.bumkTarget" alt="즐겨찾기"/>
					<c:forEach  var="bumkItem" items="${bumkList}" varStatus="status">
						<u:set var="bumkId" test="${empty param.schdlKndCd || param.schdlKndCd == '1' }" value="${bumkItem.bumkTgtUid }" elseValue="${bumkItem.bumkTgtDeptId }"/>
						<u:option value="${bumkId }:${bumkItem.bumkId }" title="${bumkItem.bumkDispNm}" checkValue="${param.bumk }"/>
					</c:forEach>
					</select>
				</td>
				<td class="frontbtn notPrint">
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
				<td class="fronttit notPrint"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></td>
				<td class="frontinput notPrint">
					<select id="schdlKndCd"  name="schdlKndCd" onchange="fnTargetSubmit(this);">
					<u:option value="" titleId="cm.option.all" alt="전체선택"/>
					<c:forEach  var="list" items="${schdlKndCdList}" varStatus="status">
					<u:option value="${list[0]}" title="${list[1] }" checkValue="${!empty paramSchdlKndCd ? paramSchdlKndCd : param.schdlKndCd}"/>
					</c:forEach>
					</select>
				</td>
				<td class="fronttit notPrint"><u:msg titleId="wc.cols.grpSelect" alt="그룹선택"/></td>
				<td class="frontinput notPrint">
					<u:set var="myGrpDisabled" test="${param.schdlKndCd eq '2' }" value="" elseValue="disabled='disabled'"/>
					<select id="myGrp" name="myGrp" onchange="fnGrpSelect(this);" ${myGrpDisabled }>
					<u:option value="" titleId="cm.option.all" alt="전체선택"/>
					<c:forEach  var="grpItem" items="${wcSchdlGroupBVoList}" varStatus="status">
					<u:option value="${grpItem.schdlGrpId}" title="${grpItem.grpNm }" checkValue="${param.myGrp}"/>
					</c:forEach>
					</select>
				</td>
				</c:if>
				<td class="notPrint"><div id="loading" style="display:none;"><img src="${_cxPth}/images/cm/bigWaiting.gif" width="22" height="22"/></div></td>
			</tr>
		</table>
		</form>
	</div>
	<div class="front_right notPrint" >
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

<!-- 달력 -->
<div id="calendar"></div>
<u:blank />
<% // 하단 FRONT %>
<div class="front notPrint">
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
<!-- <div class="fc-tooltip"></div> -->
