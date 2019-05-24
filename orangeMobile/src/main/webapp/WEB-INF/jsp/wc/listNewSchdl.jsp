<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<jsp:useBean id="now" class="java.util.Date" />
<u:set test="${!empty param.tabNo}" var="tabNo" value="${param.tabNo}" elseValue="0" />
<u:set test="${!empty fncCal}" var="fncCal" value="${fncCal}" elseValue="my" />
<u:set var="queryParamUserUid" test="${!empty param.agnt && param.agnt ne '-1'}" value="&paramUserUid=${param.agnt }" elseValue=""/>
<!-- 권한 -->
<u:secu auth="W" ><c:set var="writeAuth" value="Y"/></u:secu>
<style>
td{margin:0; padding:0; border:0; outline:0; font-size:100%; vertical-align:baseline; background:transparent;}
</style>
<script type="text/javascript">
//<![CDATA[
<% // [하단버튼:국가변경] %>
function setNatPop(isSrch){
	$m.ajax('${_cxPth}/wc/getNatListAjx.do?menuId=${menuId}&fncCal=${fncCal}', null, function(data){
		var i, objVo, objArr = data.wcNatBVoList, natList=[];
		for(i=0;i<objArr.length;i++){
			objVo = objArr[i];
			natList.push({cd:objVo.cd, nm:objVo.rescNm});
		}
		if(natList.length==0){
			$m.msg.alertMsg('cm.msg.noData');<% //cm.msg.noData=해당하는 데이터가 없습니다.%>
			return;
		}
		var chkNatCd = '${param.natCd}';
		if(!isSrch || (isSrch && chkNatCd=='' && data.chkNatCd != null)) chkNatCd = data.chkNatCd;
		
		$m.dialog.openSelect({id:'selectNatPop', cdList:natList, selected:chkNatCd}, function(selObj){
			if(selObj!=null){
				if(!isSrch) {
					$m.ajax('/wc/transNatAjx.do?menuId=${menuId}&fncCal=${fncCal}', {natCd:selObj.cd}, function(data) {
						if (data.message != null) {
							$m.dialog.alert(data.message);
						}
					});
				}else searchNat(selObj.cd);
			}
		});
	}, {async:true});
}
<% // 국가기념일 조회 %>
function searchNat(cd){
	if(cd==undefined) return;
	var $form = $('#calendarPrintForm');
	$form.find("[name='natCd']").remove();
	$form.appendHidden({name:'natCd',value:cd});
	$m.nav.curr(event, '/wc/listNewSchdl.do?'+$form.serialize());
}
           
function getDayVal(val){
	return val < 10 ? '0'+val : val;
};
function selectMonth(param){
	if(param == null) $('#action').remove();
	else $('#action').val(param);
	if($('#submitTab').val() == 'molyList'){
		$('#tabNo').val('0');
	}
	else if($('#submitTab').val() == 'welyList'){
		$('#tabNo').val('1');
	}
	else if($('#submitTab').val() == 'dalyList'){
		$('#tabNo').val('2');
	}	
	$m.nav.curr(event, '/wc/listNewSchdl.do?'+$('#calendarPrintForm').serialize());
};

function selectTab(tabNoRT){
	var tabArrs = ["molyList","welyList","dalyList"];
	var tabTitles = ['<u:msg titleId="wc.jsp.listPsnSchdl.tab.molySchdl" alt="월간일정"/>','<u:msg titleId="wc.jsp.listPsnSchdl.tab.welySchdl" alt="주간일정"/>','<u:msg titleId="wc.jsp.listPsnSchdl.tab.dalySchdl" alt="일간일정"/>'];
	$("#tabNo").val(tabNoRT);
	$('#submitTab').val(tabArrs[tabNoRT]);
	$('div.schduleContainer').each(function(){$(this).hide();});
	$('div.'+tabArrs[tabNoRT]).each(function(){$(this).show();});
	$('#tabArea').hide();
	$('#pageTitle').text("<u:msg titleId="wc.jsp.prefix.${fncCal}.title" alt="나의"/> "+tabTitles[tabNoRT]);
};

<% // 대리인 선택 %>
function agntSelect(obj){	
	$("#viewUserUid").val("");
	$("#viewUserNm").val("");
	$("#viewOrgId").val("");
	$("#viewOrgNm").val("");	
	
	$('#calendarPrintForm #agnt').val(obj.cd);
	$('#calendarPrintForm #schdlKndCd').val("1");
	$("#calendarPrintForm #schdlTypCd").val("");
	$("#calendarPrintForm #myGrp").val("");
	
};

var agntArrs = []
<% // 일정 대리수행 팝업 %>
function schProxySchdlPop() {
	if(agntArrs.length == 0) return;
	$m.dialog.openSelect({id:'agntUid', cdList:agntArrs, selected:'${param.agnt}'}, function(selObj){
		if(selObj!=null){
			agntSelect(selObj);
			$m.nav.curr(event, '/wc/listNewSchdl.do?'+$('#calendarPrintForm').serialize());
		}
	});
};

//그룹선택
function fnGrpSelect(obj){
	var arrGrp=[];
	if(obj.cd != ''){
		arrGrp.push("<input id='choiGrpIds' name='choiGrpIds' type='hidden' value='"+obj.cd+"'>");
		arrGrp.push("<input id='choiGrpNms' name='choiGrpNms' type='hidden' value='"+obj.nm+"'>");
		arrGrp.push("<input id='grpResetFlag' name='grpResetFlag' type='hidden' value='true'>");
		$('#calendarPrintForm #myGrp').val(obj.cd);
	}else{
		$('#calendarPrintForm #myGrp').val('');
		arrGrp.push("<input id='grpResetFlag' name='grpResetFlag' type='hidden' value='true'>");
	}
	$("#choiGrpDiv").html(arrGrp.join(''));
	selectMonth('');
};

<% // 그룹선택 팝업 %>
function openGrpSelect(){
	var grpArrs = [];
	grpArrs.push({cd:'', nm:'<u:msg titleId="cm.option.all" alt="전체선택"/>'});
	<c:forEach  var="grpItem" items="${wcSchdlGroupBVoList}" varStatus="status">
		grpArrs.push({cd:'${grpItem.schdlGrpId}', nm:'${grpItem.grpNm }'});
	</c:forEach>
	if(grpArrs.length == 0) return;
	$m.dialog.openSelect({id:'openGrpSelect', cdList:grpArrs, selected:'${param.myGrp}'}, function(selObj){
		if(selObj!=null){
			fnGrpSelect(selObj);
		}
	});
};

var tgtArrs = [];
<% // 일정대상 팝업 %>
function schSchdlKndCdPop() {
	if(tgtArrs.length == 0) return;
	$m.dialog.openSelect({id:'targetKndCd', cdList:tgtArrs, selected:'${param.schdlKndCd}'}, function(selObj){
		if(selObj!=null){
			$('#calendarPrintForm #schdlKndCd').val(selObj.cd);
			if(selObj.cd =="2") {
				$('#calendarPrintForm #grpResetFlag').val('true');
				openGrpSelect();
			}else {
				$('#calendarPrintForm #myGrp').val('');
				$("#choiGrpDiv").html('');
				$m.nav.curr(event, '/wc/listNewSchdl.do?'+$('#calendarPrintForm').serialize());
			}
		}
	});
};

<% // 대리인 초기화 %>
function agntInit(){
	agntArrs.push({cd:'', nm:'<u:msg titleId="wc.cols.schdlAgnt" alt="일정대리수행"/>'});
	<c:forEach  var="agntItem" items="${wcAgntVos}" varStatus="status">
		agntArrs.push({cd:'${agntItem.userUid}', nm:'${agntItem.rescNm }'});
	</c:forEach>
};


<% // 일정대상 초기화 %>
function tgtInit(){
	tgtArrs.push({cd:'', nm:'<u:msg titleId="cm.option.all" alt="전체선택"/>'});
	<c:forEach  var="list" items="${schdlKndCdList}" varStatus="status">
		tgtArrs.push({cd:'${list[0]}', nm:'${list[1] }'});
	</c:forEach>
};

//사용자 선택
function schSelectUser(){
	$m.user.selectOneUser(null, function(userVo){
		if(userVo==null){<%
			// or.msg.noUser=선택된 사용자가 없습니다.%>
			$m.msg.alertMsg('or.msg.noUser');
		} else {
			$("#calendarPrintForm #viewOrgId").val('');
			$("#calendarPrintForm #viewOrgNm").val('');
			$("#calendarPrintForm #viewUserUid").val(userVo.userUid);
			$("#calendarPrintForm #viewUserNm").val(userVo.rescNm);
			$('#calendarPrintForm #agnt').val('');
			$m.nav.curr(event, '/wc/listNewSchdl.do?'+$('#calendarPrintForm').serialize());
		}
		return false;
	});
};

//부서 선택 - 싱글
function schOneOrg(){
	var selected = $("#calendarPrintForm #viewOrgId").val();
	if(selected == '') selected = null;
	$m.org.selectOneOrg({selected:selected}, function(orgVo){
		if(orgVo==null){<%
			// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.. - [부서]%>
			$m.msg.alertMsg("cm.msg.noSelectedItem", ["#cols.org"]);<%// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.%>
		} else {
			$("#calendarPrintForm #viewOrgId").val(orgVo.orgId);
			$("#calendarPrintForm #viewOrgNm").val(orgVo.rescNm);
			$("#calendarPrintForm #viewUserUid").val('');
			$("#calendarPrintForm #viewUserNm").val('');
			$('#calendarPrintForm #agnt').val('');
			$m.nav.curr(event, '/wc/listNewSchdl.do?'+$('#calendarPrintForm').serialize());
		}
		return false;
	});
};
<%
// 등록되어 있는 부서 목록 %>
function getOrgData(){
	var $area = $("#orgArea");
	var arr = [];
	$area.find("input[name='orgData']").each(function(){
		arr.push(JSON.parse(this.value));
	});
	return arr.length==0 ? null : arr;
}
<%
//부서 선택 - 멀티(샘플) %>
function schOrgs(){
	var oldOrgData = getOrgData();//등록되어 있는 부서 정보
	$m.org.selectOrgs({selected:oldOrgData}, function(arr){
		if(arr==null){<%
			// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.. - [부서]%>
			$m.msg.alertMsg("cm.msg.noSelectedItem", ["#cols.org"]);<%// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.%>
		} else {
			var $area = $("#orgArea");
			$area.html("");
			arr.each(function(index, data){
				$area.append("<input name='orgData' type='hidden' value='"+escapeValue(JSON.stringify(data))+"' />");
			});
			return true;
		}
		return false;
	});
};

<% // 목록 팝업 %>
function listSchdlPop(obj,title){
	if($(obj).find('div.schdlItem').length == 0){
		if('${writeAuth}' == 'Y') setSchdl(title.split('-')[0] , title.split('-')[1] ,title.split('-')[2]);
		return;
	}
	var html = [];
	html.push('	<div class="btnarea">');
	html.push('<div class="size">');
	html.push('	<dl>');
	//html.push('			<dd class="btn">삭제</dd>');
	html.push('<dd class="btn" onclick="$m.nav.getWin().setSchdl(\''+title.split('-')[0]+'\',\''+title.split('-')[1]+'\',\''+title.split('-')[2]+'\');"><u:msg titleId="cm.btn.write" alt="등록" /></dd>');
	html.push('			</dl>');
	html.push('		</div>');
	html.push('	</div>');
	html.push('	<div class="unified_listarea_pop2">');
	html.push('		<div class="listarea">');
	html.push('		<article>');
	
	$(obj).find('div.schdlItem').each(function(){
		html.push('			<div class="listdiv" onclick="$m.nav.getWin().viewSchdl(\''+$(this).attr('data-schdlId')+'\');">');
		html.push('				<div class="list">');
		html.push('				<dl>');
		html.push('				<dd class="tit">'+$(this).attr('data-subj')+'</dd>');
		html.push('				<dd class="body">'+$(this).attr('data-period')+' / '+$(this).attr('data-time')+'</dd>');
		html.push('				</dl>');
		html.push('				</div>');
		html.push('			</div>');
	});
	
	html.push('		</article>');
	html.push('		</div>');
	html.push('	</div>');
	title = title.split('-')[0]+'-'+getDayVal(title.split('-')[1])+'-'+getDayVal(title.split('-')[2]);
	$m.dialog.open({
		id:'listSchdlPop',
		title:title,
		html:html.join('\n')
	});
};

<% // 상세보기 %>
function viewSchdl(schdlId){
	$m.dialog.close('listSchdlPop');
	var url = "/wc/viewSchdl.do?schdlId="+schdlId;
	var tabNo = $("#tabNo").val();
	url += "&tabNo="+tabNo;
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
	$m.nav.next(event, url);
};

<% // 등록 %>
function setSchdl(schdlId){
	$m.dialog.close('listSchdlPop');
	var url = "/wc/setSchdl.do";
	if(arguments.length > 1){
		var schdlStartDt = arguments[0]+''+getDayVal(arguments[1])+''+getDayVal(arguments[2]);
		if(arguments.length == 4) schdlStartDt+= ''+getDayVal(arguments[3].split(':')[0])+''+arguments[3].split(':')[1];
		url+= "?schdlStartDt="+schdlStartDt;
	}else{
		if(schdlId != null) {
			url+= "?schdlId="+schdlId;
		}	
	}
	url+= url.indexOf('?') > -1 ? '&' : '?';
	var tabNo = $("#tabNo").val();
	url += "tabNo="+tabNo;
	url += "&menuId=${menuId}";
	url += "&fncCal=${fncCal}";
	url +='${queryParamUserUid}';
	$m.nav.next(event, url);
};

function fnCalendar(id,opt){
	$m.dialog.open({
	id:id,
	noPopbody:true,
	cld:true,
	url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&handler=fnCalMove'+'&calStyle=m',
	});
};

<% //주차계산 %>
function getSecofWeek(date){
	var year=date.split('-')[0];
	var month=date.split('-')[1];
	var day=date.split('-')[2];
	var fd = new Date( year, parseInt(month)-1, 1 );
	return Math.ceil((parseInt(day)+fd.getDay())/7);
};

<% //날짜이동 %>
function fnCalMove(date){
	var year=date.split('-')[0];
	var month=date.split('-')[1];
	var day=date.split('-')[2];
	
	if($('#submitTab').val() == 'molyList'){
		$("#calendarPrintForm input[name='molyYear']").val(year);
		$("#calendarPrintForm input[name='molyMonth']").val(month);
	}else if($('#submitTab').val() == 'welyList'){
		$("#calendarPrintForm input[name='welyYear']").val(year);
		$("#calendarPrintForm input[name='welyMonth']").val(month);
		$("#calendarPrintForm input[name='welyWeek']").val(getSecofWeek(date));
	}
	else if($('#submitTab').val() == 'dalyList'){
		$("#calendarPrintForm input[name='dalyYear']").val(year);
		$("#calendarPrintForm input[name='dalyMonth']").val(month);
		$("#calendarPrintForm input[name='dalyDay']").val(day);
	}
	$('#action').val('');
	$m.nav.curr(event, '/wc/listNewSchdl.do?'+$('#calendarPrintForm').serialize());
};
<%// footer 위치를 일정하게 %>
function placeFooter(){
	var scheduleTop,footerHeight,gap;
	$('div.schedule_body_scroll').each(function(){
		scheduleTop = $(this).position().top+5;
		footerHeight = $(this).find('#footer').height()+30, screenHeight = $m.nav.screenSize().h;
		gap = screenHeight - scheduleTop - footerHeight;
		$(this).find('#footerSpace').height(gap).show();
	});	
};

<% // [POPUP] 개인설정 %>
function setUserSetupPop() {
	$m.ajax('${_cxPth}/wc/getUserSetupAjx.do?menuId=${menuId}&fncCal=${fncCal}', {setupId: 'schdlKndCd'}, function(data) {
		if (data.message != null) {
			$m.dialog.alert(data.message);
			return;
		}
		var selected = data.value!=null ? data.value : '';
		$m.dialog.openSelect({id:'setSchdlKndCd', cdList:tgtArrs, selected:selected}, function(selObj){
			if(selObj!=null){
				var paramMap = new ParamMap();
				paramMap.put('schdlKndCd', selObj.cd);
				var paramList = JSON.stringify(paramMap.toJSON());
				$m.ajax('${_cxPth}/wc/transUserSetupAjx.do?menuId=${menuId}&fncCal=${fncCal}', {paramList: paramList}, function(data) {
					if (data.message != null) {
						$m.dialog.alert(data.message);
					}
				});
			}
		});		
	});
};

function initSwipe(){
	var lyGap = 100;//기준 터치이동 px
	$('div.schedule_calendar_scroll, div.schedule_body_scroll').each(function(){
		//$(this).find('div.schedule_body, div.schedule_calendar').each(function(){//플리킹 효과를 주기 위해서 css 를 변경한다.
			//$(this).css({'overflow':'hidden','position':'absolute','width':'100%'});
		//});
		var touchX = 0,touchY = 0;
		$(this).on('touchstart',function(e){
			//touchstart
			var touch = e.originalEvent.changedTouches[0];
			touchX = touch.pageX; //e.touches[0].clientX;
			touchY = touch.pageY; //e.touches[0].clientX;
			//$('#flick-right').css('left',$(this).find('div.schedule_calendar').width());
		});
		$(this).on('touchend',function(e){
			//touchend
			var touch = e.originalEvent.changedTouches[0];
			var moveX = touch.pageX; //e.originalEvent.changedTouches[0];
			
			var gap;
			if(moveX > touchX) gap = moveX - touchX;
			else gap = touchX - moveX;
			if(gap > lyGap){
				if(moveX > touchX){
					//오른쪽
					selectMonth('before');
				}else{
					//왼쪽
					selectMonth('after');
				}
			}else{//기준 px 이하로 터치 이동이 발생할 경우
				//if($(this).hasClass('schedule_calendar_scroll')) $(this).find('div.schedule_calendar').css('left',0);
				//else $(this).find('div.schedule_body').css('left',0);
				
			}
		});
		$(this).on('touchmove',function(e){
			//touchmove
			var touch = e.originalEvent.changedTouches[0];
			var chX = Math.abs(touchX - touch.pageX);
			if(chX > 30){//30px 이하일 경우 플리킹 효과 X
				if(touch.pageX > touchX) {//플리킹 효과
					//if($(this).hasClass('schedule_calendar_scroll')) $(this).find('div.schedule_calendar').css('left',chX);
					//else $(this).find('div.schedule_body').css('left',chX);
				}else{
					//if($(this).hasClass('schedule_calendar_scroll')) $(this).find('div.schedule_calendar').css('left',-chX);
					//else $(this).find('div.schedule_body').css('left',-chX);
				}
			}
			var chY = Math.abs(touchY - touch.pageY);
			//좌표가 좌우로 많이 움직였을때만 preventDefault 이벤트 추가
			if(chX > chY){
				isPrevent = true;		
			}else{
				isPrevent = false;
			}
			if(isPrevent){
				e.preventDefault();
			}
		});
	});		
};
$(document).ready(function() {
	selectTab('${tabNo}');
	if('${fncCal}' == 'my') {
		agntInit();
		tgtInit();
	}
	placeFooter();
	
	initSwipe();
});

//]]>
</script>

<form id="calendarPrintForm" name ="calendarPrintForm" method="get" >
<input type="hidden" name="menuId"  value="${menuId}"/>
<input type="hidden" id="viewUserUid" name="viewUserUid" value="${viewUserUid}"/>
<input type="hidden" id="viewUserNm" name="viewUserNm" value="${viewUserNm}"/>
<input type="hidden" id="viewOrgId" name="viewOrgId" value="${viewOrgId}"/>
<input type="hidden" id="viewOrgNm" name="viewOrgNm" value="${viewOrgNm}"/>

<input type="hidden" id="molyYear" name="molyYear" value="${wcScdCalMonth.year}"/>
<input type="hidden" id="molyMonth" name="molyMonth"  value="${wcScdCalMonth.month}"/>
<input type="hidden" id="welyYear" name="welyYear"  value="${wcScdCalWeek.year}"/>
<input type="hidden" id="welyMonth" name="welyMonth"  value="${wcScdCalWeek.month}"/>
<input type="hidden" id="welyWeek" name="welyWeek"  value="${wcScdCalWeek.week}"/>
<input type="hidden" id="dalyYear" name="dalyYear"  value="${wcScdCalDay.year}"/>
<input type="hidden" id="dalyMonth" name="dalyMonth"  value="${wcScdCalDay.month}"/>
<input type="hidden" id="dalyDay" name="dalyDay"  value="${wcScdCalDay.day}"/>

<input type="hidden" name="fncCal"  value="${param.fncCal}"/>
<input type="hidden" id="tabNo" name="tabNo"  value="${tabNo}"/>
<input type="hidden" name="tabNoRT" />

<input  type="hidden" id="action" name="action"/>
<input  type="hidden" id="submitTab" name="submitTab" value="molyList"/>

<!-- 대리자 정보 추가 -->
<c:if test="${fncCal eq 'my' }">
<input type="hidden" id="agnt" name="agnt"  value="${param.agnt}"/>
<input  type="hidden" id="schdlKndCd" name="schdlKndCd" value="${!empty paramSchdlKndCd ? paramSchdlKndCd : param.schdlKndCd }"/>
<input  type="hidden" id="schdlTypCd" name="schdlTypCd" value="${param.schdlTypCd }"/>
<input  type="hidden" id="myGrp" name="myGrp" value="${param.myGrp }"/>
</c:if>
	
<div id="choiGrpDiv" style="display:none">
<input id='grpResetFlag' name='grpResetFlag' type='hidden' value='${grpResetFlag }'>
<c:if test="${!empty param.choiGrpIds }"
><m:input type="hidden" id="choiGrpIds" value="${param.choiGrpIds }"
/><m:input type="hidden" id="choiGrpNms" value="${param.choiGrpNms }"/></c:if>
</div>

</form>		
<!--schedule_top S-->
<div class="schedule_top schduleContainer molyList" style="display:none;">
	<div class="scd_top1" >
		<dl>
		<dd class="scd_prev" onclick="javascript:selectMonth('before');"></dd>
		<dd class="scd_tit1" id="todayTd" >${wcScdCalMonth.year}-${wcScdCalMonth.month < 10 ? '0' : ''}${wcScdCalMonth.month}</dd>
		<dd class="scd_next" onclick="javascript:selectMonth('after');"></dd>
		</dl>
	</div>
	<div class="scd_today" onclick="javascript:selectMonth();"><u:msg titleId="wc.btn.today" alt="오늘" /></div>
</div>
<!--//schedule_top E-->


<!--schedule_calendar S-->
<div class="schedule_calendar schduleContainer molyList" style="display:none;">
	<table class="scd_calendar">
		<colgroup>
		<col width="14.28%"/>
		<col width="14.28%"/>
		<col width="14.28%"/>
		<col width="14.28%"/>
		<col width="14.28%"/>
		<col width="14.28%"/>
		<col />
		</colgorup>
		<tbody>
		<tr class="scd_cweek">
			<td class="week_sunday"><u:msg titleId="wc.cols.sun" alt="일" /></td>
			<td class="week"><u:msg titleId="wc.cols.mon" alt="월" /></td>
			<td class="week"><u:msg titleId="wc.cols.tue" alt="화" /></td>
			<td class="week"><u:msg titleId="wc.cols.wed" alt="수" /></td>
			<td class="week"><u:msg titleId="wc.cols.thu" alt="목" /></td>
			<td class="week"><u:msg titleId="wc.cols.fri" alt="금" /></td>
			<td class="week_saturday"><u:msg titleId="wc.cols.sat" alt="토" /></td>
		</tr>
		</tbody>
	</table>
</div>
<!--//schedule_calendar E-->
<!--schedule_calendar_scroll S-->
<div class="schedule_calendar_scroll schduleContainer schedulePage molyList" style="display:none;" >
	<!--schedule_calendar S-->
	<div class="schedule_calendar" >
		<table class="scd_calendar" >
			<colgroup>
			<col width="14.28%"/>
			<col width="14.28%"/>
			<col width="14.28%"/>
			<col width="14.28%"/>
			<col width="14.28%"/>
			<col width="14.28%"/>
			<col />
			</colgroup>
			<tbody>
			<c:set var="maxRowIndex" value="5"/>
			<c:forEach  var="calWeek" items="${wcScdCalMonth.weeks}" varStatus="weekStatus">
				<c:set var="dayClass" value="day"/>
				<tr class="scd_cbody">
					<c:forEach  var="calDay" items="${calWeek.days}" varStatus="dayStatus">
						<c:set var="schdlPrevCls" />
						<c:choose>
							<c:when test="${calDay.holiFlag eq 'scddate_red' }"><c:set var="dayClass" value="${calDay.toDayFlag eq 'scd_today' ? 'day_today_sun' : 'sunday'}"/></c:when>
							<c:when test="${calDay.holiFlag eq 'scddate_red_prev' }"><c:set var="dayClass" value="sunday_g"/><c:set var="schdlPrevCls" value="_g"/></c:when>
							<c:when test="${calDay.holiFlag eq 'scddate' && dayStatus.index == 6 }"><c:set var="dayClass" value="${calDay.toDayFlag eq 'scd_today' ? 'day_today_sat' : 'saturday'}"/></c:when>
							<c:when test="${calDay.holiFlag eq 'scddate_prev' && dayStatus.index == 6 }"><c:set var="dayClass" value="saturday_g"/><c:set var="schdlPrevCls" value="_g"/></c:when>
							<c:when test="${calDay.holiFlag eq 'scddate' && dayStatus.index < 6}"><c:set var="dayClass" value="${calDay.toDayFlag eq 'scd_today' ? 'day_today' : 'day'}"/></c:when>
							<c:when test="${calDay.holiFlag eq 'scddate_prev' }"><c:set var="dayClass" value="day_g"/><c:set var="schdlPrevCls" value="_g"/></c:when>
						</c:choose>
						<td class="${dayClass }" onclick="listSchdlPop(this,'${calDay.year }-${calDay.month }-${calDay.day }');">
							<u:out value="${calDay.day}"/>
							<c:forEach var="scds" items="${calDay.scds}" varStatus="status">
							<c:if test="${scds.schdlTypCd == '5' and status.index==0}">
								<c:choose>
								<c:when test="${calDay.holiFlag == 'scddate_prev' }"> <c:set var="scds_color"	value="holiday_g" /> </c:when>
								<c:when test="${calDay.holiFlag == 'scddate_red_prev'}"> <c:set var="scds_color"	value="holiday_g_red" /> </c:when>
								<c:when test="${calDay.holiFlag == 'scddate_red'}"> <c:set var="scds_color"	value="holiday_red" /> </c:when>
								<c:otherwise><c:set var="scds_color"	value= "holiday" /></c:otherwise>
								</c:choose>
								<div class="${scds_color }" ><div class="size"><u:out value="${scds.subj}" /></div></div>
							</c:if>
							</c:forEach>
							
							<!-- 더보기 여부 -->
							<c:set var="maxSchdsIndex" value="${calDay.scdMaxIndex}"/>
							<c:if test="${maxSchdsIndex > 0}">
								<div class="pbgarea">
								<c:forEach begin="1" end="${maxSchdsIndex}" step="1" var="tempIndex">
									<c:forEach var="scds" items="${calDay.scds}" varStatus="status">
										<c:if test="${tempIndex == scds.schdIndex}">
											<!-- 내용 S -->
											<c:set var="moreParam" value="${calDay.year }-${calDay.month }-${calDay.day }-${calDay.dayOfTheWeek }"/>
											<u:set var="moreDisplay" test="${tempIndex >= maxRowIndex }" value="display:none;" elseValue=""/>
											<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
											<u:set var="catBgStyle" test="${(!empty mapList && !empty mapList.bgcolCd) && ( !empty scds.schdlRepetState || scds.alldayYn eq 'Y')}" value="style='background:${mapList.bgcolCd };${moreDisplay }'" elseValue="style='${moreDisplay }'"/>
											<u:set var="catFontStyle" test="${!empty mapList && !empty mapList.fontColrCd}" value="style='color:${empty scds.schdlRepetState && scds.alldayYn ne 'Y' ? mapList.bgcolCd : mapList.fontColrCd };'" elseValue=""/>
											
											<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
											<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
											
											<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
											<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="yyyy-MM-dd"/>
											
											<c:if test="${scds.solaLunaYn eq 'N'}">
												<fmt:parseDate var="dateLunaStartDt" value="${scds.schdlLunaStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
												<fmt:formatDate var="convLunaStartDt" value="${dateLunaStartDt}" pattern="yyyy-MM-dd"/>
												<c:set var="scds_tooltip_schdlStartDay" value="(${luna } :${convLunaStartDt}) ${convStartDt}"/>
												
												<fmt:parseDate var="dateLunaEndDt" value="${scds.schdlLunaEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
												<fmt:formatDate var="convLunaEndDt" value="${dateLunaEndDt}" pattern="yyyy-MM-dd"/>
												<c:set var="scds_tooltip_schdlEndDay" value="(${luna } :${convLunaEndDt}) ${convEndDt}"/>
											</c:if>
											<c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
												<c:set var="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
												<c:set var="scds_tooltip_schdlEndDay" value="${convEndDt}"/>
											</c:if>
											
											<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="HH:mm"/>
											<c:set var="scds_tooltip_schdlStartDt" value="${convStartDt}"/>
											
											<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="HH:mm"/>
											<c:set var="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>
											<c:if test="${!empty scds.subj}">
			                                	<div class="schdlItem ${!empty scds.schdlRepetState || scds.alldayYn eq 'Y' ? 'pbg' : 'ptxt' }${tempIndex}${schdlPrevCls}" data-subj="${scds.subj}" data-period="${scds_tooltip_schdlStartDay}~${scds_tooltip_schdlEndDay}" data-time="${scds_tooltip_schdlStartDt }~${scds_tooltip_schdlEndDt}" data-schdlId="${scds.schdlId }" style="${moreDisplay}"><u:out value="${scds.subj}" /></div>
			                                </c:if>
										</c:if>
									</c:forEach>
								</c:forEach>
								</div>
							</c:if>
						</td>
					</c:forEach>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<!--//schedule_calendar E-->
	<!--footer S-->
    <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    <!--//footer E-->
</div>
<!--//schedule_calendar_scroll E-->

<!--schedule_top S-->
<div class="schedule_top schduleContainer welyList" style="display:none;">
    <div class="scd_top1">
    <dl>
    <dd class="scd_prev" onclick="javascript:selectMonth('before');"></dd>
    <dd class="scd_tit1" onclick="javascript:;"><fmt:parseDate var="titleDate" value="${wcScdCalWeek.days[0].days }" pattern="yyyyMMdd"/><fmt:formatDate value="${titleDate }" pattern="yyyy-MM" /></dd>
    <dd class="scd_next" onclick="javascript:selectMonth('after');"></dd>
    </dl>
    </div>
    <div class="scd_today" onclick="javascript:selectMonth();"><u:msg titleId="wc.btn.today" alt="오늘" /></div>
</div>
<!--//schedule_top E-->
    
<!--schedule_body_scroll S-->
<div class="schedule_body_scroll schduleContainer welyList" style="display:none;">      
    
    <!--schedule_body S-->
    <div class="schedule_body">
    <table class="scd_body">
        <colgroup>
            <col width="63"/>
            <col />
        </colgorup>
        <tbody>
            <tr>
                <td class="scd_line" colspan="2"></td>
            </tr>
            <c:forEach  var="weekCalDay" items="${wcScdCalWeek.days}" varStatus="status">
            	<c:choose>
				<c:when test="${status.index==0  || weekCalDay.holiFlag == 'scddate_red'  }">
					<c:set var="day_num_style"	value= "scdtit_sunday" />
				</c:when>
				<c:when test="${status.index == 6 }">
					<c:set var="day_num_style"	value= "scdtit_saturday" />
				</c:when>
				<c:otherwise>
					<c:set var="day_num_style"	value= "scdtit_md" />
				</c:otherwise>
				</c:choose>
				<c:set var="maxSchdsIndex" value="${weekCalDay.scdMaxIndex}"/>
				<c:set var="sundayInfo" value=""/>
				<c:forEach var="scds" items="${weekCalDay.scds}" varStatus="subStatus">
					<c:if test="${scds.schdlTypCd == '5' }">
						<c:if test="${subStatus.index==0 }"><c:set var="maxSchdsIndex" value="${maxSchdsIndex+1}"/></c:if>
						<c:set var="sundayInfo" value="${sundayInfo }${!empty sundayInfo ? ',' : ''}${scds.subj}"/>
					</c:if>
				</c:forEach>
				
				<tr>
	                <td class="${day_num_style }" <c:if test="${writeAuth == 'Y' }">onclick="setSchdl('${weekCalDay.year}','${weekCalDay.month}','${weekCalDay.day}');"</c:if> <c:if test="${maxSchdsIndex > 1 }">rowspan="${maxSchdsIndex }"</c:if>>
	                	<c:choose>
	                		<c:when test="${status.index==0}"><u:msg var="dayNm" titleId="wc.cols.sun" alt="일" /></c:when>
	                		<c:when test="${status.index==1}"><u:msg var="dayNm" titleId="wc.cols.mon" alt="월" /></c:when>
	                		<c:when test="${status.index==2}"><u:msg var="dayNm" titleId="wc.cols.tue" alt="화" /></c:when>
	                		<c:when test="${status.index==3}"><u:msg var="dayNm" titleId="wc.cols.wed" alt="수" /></c:when>
	                		<c:when test="${status.index==4}"><u:msg var="dayNm" titleId="wc.cols.thu" alt="목" /></c:when>
	                		<c:when test="${status.index==5}"><u:msg var="dayNm" titleId="wc.cols.fri" alt="금" /></c:when>
	                		<c:otherwise><u:msg var="dayNm" titleId="wc.cols.sat" alt="토" /></c:otherwise>
	                	</c:choose>
	                	<c:set var="dayText" value="${weekCalDay.day} ${dayNm }"/>
	                	<c:choose>
	                		<c:when test="${weekCalDay.toDayFlag eq 'scd_today' }"><div class="scdtit_mdtoday">${dayText }</div></c:when>
	                		<c:otherwise>${dayText }</c:otherwise>
	                	</c:choose>
	                </td>
	                <td class="basic">
	                	<c:choose>
	                		<c:when test="${maxSchdsIndex > 0 && maxSchdsIndex == weekCalDay.scdMaxIndex}">
	                			<c:forEach begin="1" end="1" step="1" var="tempIndex">
									<c:forEach var="scds" items="${weekCalDay.scds}" varStatus="status">
										<c:if test="${tempIndex == scds.schdIndex}">
											<!-- 내용 S -->
											<c:choose>
												<c:when test="${weekCalDay.holiFlag == 'scddate_prev'}">
												<c:set var="scds_color"	value= "scd_prev" />
												</c:when>
												<c:when test="${weekCalDay.holiFlag == 'scddate_red_prev'}">
												<c:set var="scds_color"	value= "scd_prev" />
												</c:when>
												<c:otherwise>
												</c:otherwise>
											</c:choose>
											<c:set var="maxRowIndex" value="100"/>
											<c:set var="moreParam" value="${weekCalDay.year }-${weekCalDay.month }-${weekCalDay.day }-${weekCalDay.dayOfTheWeek }"/>
											<u:set var="moreDisplay" test="${tempIndex >= maxRowIndex }" value="display:none;" elseValue=""/>
											<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
											
											<u:set var="catBgStyle" test="${(!empty mapList && !empty mapList.bgcolCd) && ( !empty scds.schdlRepetState || scds.alldayYn eq 'Y')}" value="background:${mapList.bgcolCd };${moreDisplay }" elseValue="${moreDisplay }"/>
											<u:set var="catFontStyle" test="${!empty mapList && !empty mapList.fontColrCd}" value="color:${empty scds.schdlRepetState && scds.alldayYn ne 'Y' ? mapList.bgcolCd : mapList.fontColrCd };" elseValue=""/>
											<c:set var="schdlStyle" value="style='${catBgStyle }${catFontStyle }'"/>
											
											<div class="basicdiv" ><div class="basic_w" onclick="viewSchdl('${scds.schdlId}');" ${schdlStyle }><u:out value="${scds.subj}" /></div></div>
											
											<%-- <c:choose>
												<c:when test="${(!empty mapList && !empty mapList.bgcolCd) && ( !empty scds.schdlRepetState || scds.alldayYn eq 'Y')}">
													<div class="basicdiv" ><div class="basic_w" onclick="viewSchdl('${scds.schdlId}');" ${schdlStyle }><u:out value="${scds.subj}" /></div></div>
												</c:when>
												<c:otherwise>
													<div class="basicdiv" ><div class="basic_w" onclick="viewSchdl('${scds.schdlId}');" ${schdlStyle }><u:out value="${scds.subj}" /></div></div>
												</c:otherwise>
											</c:choose> --%>
										</c:if>
									</c:forEach>
								</c:forEach>
	                		</c:when>
	                		<c:otherwise><div class="basicdiv"><div class="basic_w" style="${weekCalDay.holiFlag == 'scddate_red' ? 'color:#d22681;' : ''}">${sundayInfo }</div></div></c:otherwise>
	                	</c:choose>
	                </td>
	            </tr>
	            <c:choose>
               		<c:when test="${maxSchdsIndex > 1 }">
               			<c:forEach begin="${maxSchdsIndex == weekCalDay.scdMaxIndex ? 2 : 1 }" end="${maxSchdsIndex}" step="1" var="tempIndex">
							<c:forEach var="scds" items="${weekCalDay.scds}" varStatus="status">
								<c:if test="${tempIndex == scds.schdIndex}">
									<tr>
										<td class="basic">
											<!-- 내용 S -->
											<c:choose>
												<c:when test="${weekCalDay.holiFlag == 'scddate_prev'}">
												<c:set var="scds_color"	value= "scd_prev" />
												</c:when>
												<c:when test="${weekCalDay.holiFlag == 'scddate_red_prev'}">
												<c:set var="scds_color"	value= "scd_prev" />
												</c:when>
												<c:otherwise>
												</c:otherwise>
											</c:choose>
											<c:set var="maxRowIndex" value="22"/>
											<c:set var="moreParam" value="${weekCalDay.year }-${weekCalDay.month }-${weekCalDay.day }-${weekCalDay.dayOfTheWeek }"/>
											<u:set var="moreDisplay" test="${tempIndex >= maxRowIndex }" value="display:none;" elseValue=""/>
											<u:convert srcId="cat_${scds.schdlTypCd }" var="mapList" />
											
											<u:set var="catBgStyle" test="${(!empty mapList && !empty mapList.bgcolCd) && ( !empty scds.schdlRepetState || scds.alldayYn eq 'Y')}" value="background:${mapList.bgcolCd };${moreDisplay }" elseValue="${moreDisplay }"/>
											<u:set var="catFontStyle" test="${!empty mapList && !empty mapList.fontColrCd}" value="color:${empty scds.schdlRepetState && scds.alldayYn ne 'Y' ? mapList.bgcolCd : mapList.fontColrCd };" elseValue=""/>
											<c:set var="schdlStyle" value="style='${catBgStyle }${catFontStyle }'"/>
											<c:choose>
												<c:when test="${(!empty mapList && !empty mapList.bgcolCd) && ( !empty scds.schdlRepetState || scds.alldayYn eq 'Y')}">
													<div class="basicdiv" ><div class="basic_w" onclick="viewSchdl('${scds.schdlId}');" ${schdlStyle }><u:out value="${scds.subj}" /></div></div>
												</c:when>
												<c:otherwise>
													<div class="basicdiv" ><div class="basic_w" onclick="viewSchdl('${scds.schdlId}');" ${schdlStyle }><u:out value="${scds.subj}" /></div></div>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:if>
							</c:forEach>
						</c:forEach>
					</c:when>
				</c:choose>
										
            </c:forEach>
            <tr>
                <td class="scd_line" colspan="2"></td>
            </tr>
        </tbody>
    </table>
    </div>
    <!--//schedule_body E-->
    
    <!--footer S-->
    <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    <!--//footer E-->
    
</div>
<!--//schedule_body_scroll E-->


<!--schedule_top S-->
<div class="schedule_top schduleContainer dalyList" style="display:none;">
    <div class="scd_top3">
    <dl>
    <dd class="scd_prev" onclick="javascript:selectMonth('before');"></dd>
    <dd class="scd_tit3" onclick="javascript:;">${wcScdCalDay.year}-${wcScdCalDay.month < 10 ? '0' : ''}${wcScdCalDay.month}-${wcScdCalDay.day < 10 ? '0' : ''}${wcScdCalDay.day} / <u:msg titleId="wr.jsp.dayOfWeek${wcScdCalDay.dayOfTheWeek }.title" alt="일요일" /></dd>
    <dd class="scd_next" onclick="javascript:selectMonth('after');"></dd>
    </dl>
    </div>
    <div class="scd_today" onclick="javascript:selectMonth();"><u:msg titleId="wc.btn.today" alt="오늘" /></div>
</div>
<!--//schedule_top E-->

<!--schedule_body_scroll S-->
<div class="schedule_body_scroll schduleContainer dalyList" style="display:none;">
    
    <!--schedule_body S-->
    <div class="schedule_body">
        <table class="scd_body">
        <colgroup>
            <col width="63"/>
            <col />
        </colgorup>
        <tbody>
            <tr>
                <td class="scd_line" colspan="2"></td>
            </tr>
            <tr>
                <td class="scdtit_all"><u:msg titleId="wc.cols.wholeDay" alt="종일일정" /></td>
                <td class="basic_all" >
                	<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
                		<c:if test="${( scds.schdlTypCd == '5' || (scds.schdlTypCd != '5' && scds.alldayYn eq 'Y') ) && !empty scds.subj}">
                			<u:set var="sundayCss" test="${scds.schdlTypCd == '5' && wcScdCalDay.holiFlag == 'scddate_red' }" value="color:#d22681;" elseValue=""/>
                			<div class="ellipsis" style="width:100px;${sundayCss}" <c:if test="${empty sundayCss }">onclick="viewSchdl('${scds.schdlId}');"</c:if>><u:out value="${scds.subj}" /></div>
                		</c:if>
                	</c:forEach>
                </td>
            </tr>
            <tr>
                <td class="scd_line" colspan="2"></td>
            </tr>
        </tbody>
        </table>
    </div>
    <!--//schedule_body E-->
    
    <!--schedule_body S-->
    <div class="schedule_body" id="schdlDayArea">
        <table class="scd_body">
        <colgroup>
            <col width="63"/>
            <col />
        </colgorup>
        <tbody>
            <tr>
                <td class="scd_line" colspan="2"></td>
            </tr>
            <c:set var="viewDate"	value= "${wcScdCalDay.year}-${wcScdCalDay.month}-${wcScdCalDay.day}" />
            <fmt:formatDate value="${now}" pattern="H" var="curHour" />
			<fmt:formatDate value="${now}" pattern="yyyy-M-d" var="curDate" />
			<c:set var="strtSi" value="0" scope="page"/>
			<c:set var="endSi" value="23" scope="page"/>
			<c:set var="idx" value="1" />
            <c:forEach begin="${strtSi }" end="${endSi }" step="1" var="tempHour">
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
				<c:set var="weekDays" value="${wcScdCalDay.year}${wcScdCalDay.month < 10 ? '0' : ''}${wcScdCalDay.month}${wcScdCalDay.day < 10 ? '0' : ''}${wcScdCalDay.day}"/>
				<u:set var="convertTempHour" test="${tempHour < 10}" value="0${tempHour }:00" elseValue="${tempHour }:00"/>
				<c:set var="fullTimes" value="${weekDays }${fn:replace(convertTempHour,':','')}"/>
	            <tr>
	                <td class="scdtit_time" <c:if test="${writeAuth == 'Y' }">onclick="setSchdl('${wcScdCalDay.year}','${wcScdCalDay.month}','${wcScdCalDay.day}','${tempHour }:00');"</c:if>>${ampm} ${hourStr}:00</td>
	                <td class="basic_blank${viewDate == curDate && curHour == tempHour ? 'time' : '' }">
	                	<c:forEach var="scds" items="${wcScdCalDay.scds}" varStatus="status">
	                		<fmt:parseDate var="dateStartDt" value="${scds.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
	                		<fmt:parseDate var="dateEndDt" value="${scds.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
	                		<fmt:formatDate var="dateStartYmd" value="${dateStartDt}" pattern="yyyyMMdd"/>
	                		<fmt:formatDate var="dateEndYmd" value="${dateEndDt}" pattern="yyyyMMdd"/>
	                		<fmt:formatDate var="convFullStrtTime" value="${dateStartDt}" pattern="yyyyMMddHHmm"/>
	                		<fmt:formatDate var="convFullEndTime" value="${dateEndDt}" pattern="yyyyMMddHHmm"/>
	                		<fmt:formatDate var="strtHour" value="${dateStartDt}" pattern="HH"/>
	                		<fmt:formatDate var="endHour" value="${dateEndDt}" pattern="HH"/>
	                		<c:if test="${(dateStartYmd == dateEndYmd && fullTimes == convFullStrtTime) || (dateStartYmd < dateEndYmd && ( weekDays == dateStartYmd || ( weekDays  == dateEndYmd && endHour ne '00'))) || (dateStartYmd < dateEndYmd && weekDays > dateStartYmd && weekDays  < dateEndYmd ) }">
	                			<c:set var="rezvTime" value=""/>
	                			<c:set var="maxTimeCnt" value="1"/>
								<c:choose>
									<c:when test="${dateStartYmd == dateEndYmd && fullTimes == convFullStrtTime }">
										<c:set var="rezvTime" value="${convertTempHour }"/>
										<c:set var="maxTimeCnt" value="${endHour - strtHour }"/>
									</c:when>
									<c:when test="${dateStartYmd < dateEndYmd && ( weekDays == dateStartYmd || weekDays  == dateEndYmd )  }">
										<c:if test="${weekDays == dateStartYmd }">
											<fmt:formatDate var="convStartTime" value="${dateStartDt}" pattern="HH:mm"/>
											<c:set var="rezvTime" value="${convStartTime }"/>
											<c:set var="maxTimeCnt" value="${(endSi+1) - strtHour }"/>
										</c:if>
										<c:if test="${weekDays == dateEndYmd }"><c:set var="rezvTime" value="00:00"/><c:set var="maxTimeCnt" value="${endHour - strtSi }"/></c:if>
									</c:when>
									<c:when test="${dateStartYmd < dateEndYmd && weekDays > dateStartYmd && weekDays  < dateEndYmd }">
										<c:set var="rezvTime" value="00:00"/>
										<c:set var="maxTimeCnt" value="${(endSi - strtSi)+1 }"/>
									</c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
								<c:if test="${!empty rezvTime && rezvTime == convertTempHour && scds.schdlTypCd != '5' && !empty scds.subj && scds.alldayYn ne 'Y' }">
									<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
			                		<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="yyyy-MM-dd"/>
			                		<c:if test="${scds.solaLunaYn eq 'N'}">
										<fmt:parseDate var="dateLunaStartDt" value="${scds.schdlLunaStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
										<fmt:formatDate var="convLunaStartDt" value="${dateLunaStartDt}" pattern="yyyy-MM-dd"/>
										<c:set var="scds_tooltip_schdlStartDay" value="(${luna } :${convLunaStartDt}) ${convStartDt}"/>
										
										<fmt:parseDate var="dateLunaEndDt" value="${scds.schdlLunaEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
										<fmt:formatDate var="convLunaEndDt" value="${dateLunaEndDt}" pattern="yyyy-MM-dd"/>
										<c:set var="scds_tooltip_schdlEndDay" value="(${luna } :${convLunaEndDt}) ${convEndDt}"/>
									</c:if>
									<c:if test="${empty scds.solaLunaYn||scds.solaLunaYn=='Y'}">
										<c:set var="scds_tooltip_schdlStartDay" value="${convStartDt}"/>
										<c:set var="scds_tooltip_schdlEndDay" value="${convEndDt}"/>
									</c:if>
									
									<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="HH:mm"/>
									<c:set var="scds_tooltip_schdlStartDt" value="${convStartDt}"/>
									
									<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="HH:mm"/>
									<c:set var="scds_tooltip_schdlEndDt"  value="${convEndDt}"/>
									
									<c:choose>
										<c:when test="${ idx<4 }">
											<div class="color${idx } schdlItem" onclick="viewSchdl('${scds.schdlId}');" data-subj="${scds.subj}" data-period="${scds_tooltip_schdlStartDay}~${scds_tooltip_schdlEndDay}" data-time="${scds_tooltip_schdlStartDt }~${scds_tooltip_schdlEndDt}" data-schdlId="${scds.schdlId }">
							                    <dl>
							                    <dd>${scds.subj }</dd>
							                    <c:if test="${maxTimeCnt > 1 }">
							                    <c:forEach begin="1" end="${maxTimeCnt-1}" step="1">
							                    	<dd></dd>		                    
							                    </c:forEach>
							                    </c:if>
							                    </dl>
						                    </div>
										</c:when>
										<c:otherwise><div class="schdlItem" style="display:none;" data-subj="${scds.subj}" data-period="${scds_tooltip_schdlStartDay}~${scds_tooltip_schdlEndDay}" data-time="${scds_tooltip_schdlStartDt }~${scds_tooltip_schdlEndDt}" data-schdlId="${scds.schdlId }"></div></c:otherwise>
									</c:choose>
									<c:set var="idx" value="${idx+1}"/>
		                		</c:if>
	                		</c:if>
						</c:forEach>
						<c:if test="${viewDate == curDate && curHour == tempHour }"><div class="basic_blanktimei"></div></c:if>
	                </td>
	            </tr>
	            <c:if test="${hourStr eq '11'}">
	            	<tr>
                        <td class="scd_line" colspan="2"></td>
                    </tr>
	            </c:if>
	        </c:forEach>
        </tbody>
        </table>
        <c:if test="${idx > 3 }">
	    	<!--//schedule_body E-->
		    <div class="btnarea">
		        <div class="size">
		            <dl>
		            <dd class="btn" onclick="listSchdlPop($('#schdlDayArea'),'${wcScdCalDay.year }-${wcScdCalDay.month }-${wcScdCalDay.day }');"><u:msg titleId="cm.btn.more" alt="더보기" /></dd>
		         </dl>
		        </div>
		    </div>
	    </c:if>
    </div>
    <!--footer S-->
    <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    <!--//footer E-->
    
</div>
<!--//schedule_body_scroll E-->
        
        
        

