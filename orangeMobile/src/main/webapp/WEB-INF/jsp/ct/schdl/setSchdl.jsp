<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<c:if test="${!empty authChkW && authChkW == 'W' }"><c:set var="writeAuth" value="Y"/></c:if>
<script type="text/javascript">
//<![CDATA[
<%// 명함 정보 추가 %>
function atndAdd(objArr){
	addCols(objArr,"FRND");
};
<%// 선택추가 %>
function addCols(arr , emplTypCd){	
	if(arr==null) return;
	//var $tr, $hiddenTr = $("#listArea tbody:first #hiddenTr"),emplId,emplNm;
	//var html = $hiddenTr[0].outerHTML;
	//var vas = emplTypCd == 'ETC' ? null : getAllVas(emplTypCd);
	
	var area = $('#guestTypContainer').find('#selectedListArea');
	var html,emplId;//empty = area.find('dd:first').length==0;
	var vas = getAllVas(emplTypCd);
	var len = area.children().length == 0 ? false : true; 
	arr.each(function(index, data){
		emplId = emplTypCd == 'EMPL' ? data.userUid : (emplTypCd == 'ETC' ? data.etcId : data.bcId );
		if(vas==null || !vas.contains(emplId)){
			html = "";
			if(len) html +='<dd class="line"></dd>';
			html += '<dd class="txt_dd" data-guestUid="'+emplId+'" data-emplTypCd="'+emplTypCd+'">';
			html += '<div class="txt_lt">';
			html += data.bcNm+'/'+'<u:msg titleId="wc.option.frnd" alt="지인"/>';
			if(data.compNm != '') html+='/'+data.compNm;
			if(data.email != '') html+='/'+data.email;
			html +='<input type="hidden" name="guestUid" value="'+data.bcId+'" />';
			html +='<input type="hidden" name="guestNm" value="'+data.bcNm+'"/>';
			html +='<input type="hidden" name="guestCompNm" value="'+data.compNm+'"/>';
			html +='<input type="hidden" name="email" value="'+data.email+'"/>';
			html +='<input type="hidden" name="guestEmplYn" value="N"/>';
			html += '</div>';
			html += '<div class="txt_delete" onclick="delSelects(this);"></div>';
			html +='</dd>';
			area.append(html);
			len = true;
		}
	});
};

<%//콤보박스 삭제 %>
function delSelects(obj){
	var $parent = $(obj).parent();
	var $next = $parent.next().length > 0 ? $parent.next() : null;
	if($next != null && $next.attr('class') == 'line') $next.remove();
	$parent.remove();
};

<%//현재 등록된 id 목록 리턴 %>
function getAllVas(emplTypCd){
	var arr=[];
	var area = $('#guestTypContainer').find('#selectedListArea');
	area.children("dd[class!='line']").each(function(){
		if($(this).attr("data-emplTypCd") == emplTypCd){
			arr.push($(this).attr('data-guestUid'));
		}
	});
	
	if(arr.length==0){
		return null;
	}
	return arr;
};

//여러명의 사용자 선택
function openMuiltiUser(){
	var param = {};
	<c:if test="${!empty globalOrgChartEnable && globalOrgChartEnable==true}">
		param.global='Y';
	</c:if>
	$m.user.selectUsers(param, function(arr){
		if(arr==null || arr.length==0) return true;
		var area = $('#guestTypContainer').find('#selectedListArea');
		var html,emplId;//empty = area.find('dd:first').length==0;
		var emplTypCd = 'EMPL';
		var vas = getAllVas(emplTypCd);
		var len = area.children().length == 0 ? false : true; 
		arr.each(function(index, data){
			emplId = emplTypCd == 'EMPL' ? data.userUid : (emplTypCd == 'ETC' ? data.etcId : data.bcId );
			if(vas==null || !vas.contains(emplId)){
				html = "";
				if(len) html +='<dd class="line"></dd>';
				html += '<dd class="txt_dd" data-guestUid="'+emplId+'" data-emplTypCd="'+emplTypCd+'">';
				html += '<div class="txt_lt">';
				html += data.rescNm+'/'+'<u:msg titleId="cm.option.empl" alt="임직원"/>';
				if(data.deptRescNm != '') html+='/'+data.deptRescNm;
				if(data.email != '') html+='/'+data.email;
				//area.append('<input type="hidden" name="schdlId" value="${list.schdlId}" />');
				html +='<input type="hidden" name="guestUid" value="'+data.userUid+'" />';
				html +='<input type="hidden" name="guestNm" value="'+data.rescNm+'"/>';
				html +='<input type="hidden" name="guestCompNm" value="'+data.deptRescNm+'"/>';
				html +='<input type="hidden" name="email" value="'+data.email+'"/>';
				html +='<input type="hidden" name="guestEmplYn" value="Y"/>';
				html += '</div>';
				html += '<div class="txt_delete" onclick="delSelects(this);"></div>';
				html +='</dd>';
				area.append(html);
				len = true;
			}
		});
	});
};

function guestAdd(){
	var guestTyp = $('#setRegForm input:radio[name="guestTyp"]:checked').val();
	if(guestTyp == 'FRND'){
		$m.nav.next(null, '/ct/schdl/findBc.do?menuId=${menuId}&ctId=${param.ctId}${agntParam}&selection=multi');
		//dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}${agntParam}&callBack=atndAdd&fncMul=Y&schCat=bcNm&schWord='+encodeURIComponent($('#guest').val()));
	}else{
		openMuiltiUser();
	}
}

function fnCalendar(id,opt,hm,hmId,handler){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		url:'/cm/util/getCalendarPop.do?&id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&hm='+hm+'&hmId='+hmId+'&hmVal='+$('#'+hmId).val()+'&handler='+handler+'&unit=ontime',
	});
};

<% //콤보박스 클릭 %>
function setOptionVal(id , code, value){
	var $form = $("#setRegForm");
	$form.find("input[name='"+id+"']").val(code);
	$form.find("#"+id+"View span").text(value);
	$form.find("#"+id+"Container").hide();
};

<% //반복설정 옵션 %>
function getRepetOpts(){
	var repetOptions = [];
	repetOptions.push({cd:'N', nm:'<u:msg titleId="cm.option.clear" alt="해제"/>'});
	repetOptions.push({cd:'DALY', nm:'<u:msg titleId="wc.option.daly" alt="일일"/>'});
	repetOptions.push({cd:'WELY', nm:'<u:msg titleId="wc.option.wely" alt="주간"/>'});
	repetOptions.push({cd:'MOLY', nm:'<u:msg titleId="wc.option.moly" alt="월간"/>'});
	repetOptions.push({cd:'YELY', nm:'<u:msg titleId="wc.option.yely" alt="연간"/>'});	
	return repetOptions; 
};

<% // 전체설정 초기화 %>
function chkFileSetting(){
	$('dd#fileSetting').eq(0).trigger('click');
}

<% //반복설정  %>
function repetSelect(){
	$m.dialog.openSelect({id:'repetSelectPop', cdList:getRepetOpts(), selected:''}, function(selObj){
		if(selObj!=null){
			if(selObj.cd =="N") {
				//반복설정 해제 N
				$('#repetSetup').val('N');
				$('#repetHtmlArea').html('');
				chkFileSetting();
			}else{
				var url = '/ct/schdl/repetSelectSub.do?menuId=${menuId}&ctId=${param.ctId}&repetKnd='+selObj.cd;
				var schdlStrtYmd = $('#setRegForm #schdlStrtYmd').val();
				if(schdlStrtYmd != '') url+='&repetchoiDisDt='+schdlStrtYmd;
				if('${param.schdlId}' != '') url+='&schdlId=${param.schdlId}';
				$m.nav.next(event,url);
				/* $m.dialog.open({
					id:'repetSelectPop',
					title:'<u:msg titleId="cols.repetSetup" alt="반복설정" />',
					url:'/ct/schdl/repetSelectPop.do?menuId=${menuId}&repetKnd='+selObj.cd,
				}); */
			}
		}else{
			chkFileSetting();
		}
	});
};

function save(){
	if($("#subj").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [제목] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.subj"], function(){
			$("#setRegForm input[name='subj']").focus();
		});
		return;
	}
	
	var startDt=$("#schdlStrtYmd").val();
	var endDt=$("#schdlEndYmd").val();
	if(startDt==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [시작일자] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.strtDt"]);
		return;
	}
	
	if(endDt==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [종료일자] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.endDt"]);
		return;
	}
	if(startDt==endDt){
		if(!$('#setRegForm').find('input[name="alldayYn"]').is(':checked') && $("#schdlStrtTime").val() == $("#schdlEndTime").val()){
			//wc.msg.chkSameTime=시작시간과 종료시간이 같을 수 없습니다.
			$m.dialog.alert("<u:msg titleId="wc.msg.chkSameTime" alt="시작시간과 종료시간이 같을 수 없습니다."/>");
			return;
		}
		
		if(parseInt($("#schdlStrtTime").val().substring( 0, 2))>parseInt($("#schdlEndTime").val().substring( 0, 2))){
			$m.dialog.alert("<u:msg titleId="wc.msg.chkTime" alt="시작시간이 종료시간 보다 클수는 없습니다."/>");
			return;
		}
	}
	
	$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
		if(result){
			var transPage = "transSchdlPost";
			if('${ctSchdlBVo.schdlId}' != ''){
				transPage = "transSchdlModPost";
			}
			var $form = $('#setRegForm');
			$form.attr('action','/ct/schdl/'+transPage+'.do?menuId=${menuId}&ctId=${param.ctId}');
			$m.nav.post($form);
		}
	});
};

<% //반복설정%>
function setRepetSetup(arr){
	if(arr!= null){
		var dayStr = ['<u:msg titleId="wc.option.sun" alt="일요일" />','<u:msg titleId="wc.option.mon" alt="월요일" />','<u:msg titleId="wc.option.tue" alt="화요일" />','<u:msg titleId="wc.option.wed" alt="수요일" />','<u:msg titleId="wc.option.thu" alt="목요일" />','<u:msg titleId="wc.option.fri" alt="금요일" />','<u:msg titleId="wc.option.sat" alt="토요일" />'];
		var html = "";
		if(arr.repetKnd == 'DALY'){
			$('#dalySelect').val(arr.dalySelect);
			html += '<u:msg titleId="wc.option.daly" alt="일일" /> - <u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" /> '+arr.dalySelect+' <u:msg titleId="wc.jsp.setRepetPop.tx03" alt="일마다" />';
		}else if(arr.repetKnd == 'WELY'){
			$('#welySelect').val(arr.welySelect);
			$('#dow').val(arr.dow);
			html += '<u:msg titleId="wc.option.wely" alt="주간" /> - <u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" /> '+arr.welySelect+' <u:msg titleId="wc.jsp.setRepetPop.tx04" alt="주마다" />';
			var dows = arr.dow.split('/');
			html+='&nbsp;';
			for(var i=0;i<dows.length;i++){
				if(dows[i] == 'SUN'){ html+= '<u:msg titleId="wc.option.sun" alt="일"/>';}
				if(dows[i] == 'MON'){ html+= '<u:msg titleId="wc.option.mon" alt="월"/>';}
				if(dows[i] == 'TUE'){ html+= '<u:msg titleId="wc.option.tue" alt="화"/>';}
				if(dows[i] == 'WED'){ html+= '<u:msg titleId="wc.option.wed" alt="수"/>';}
				if(dows[i] == 'THU'){ html+= '<u:msg titleId="wc.option.thu" alt="목"/>';}
				if(dows[i] == 'FRI'){ html+= '<u:msg titleId="wc.option.fri" alt="금"/>';}
				if(dows[i] == 'SAT'){ html+= '<u:msg titleId="wc.option.sat" alt="토"/>';}
			}
		}else if(arr.repetKnd == 'MOLY'){
			html+='<u:msg titleId="wc.option.moly" alt="월간" /> - <u:msg titleId="wc.jsp.setRepetPop.tx02" alt="매" />';
			if(arr.molyKnd == '1'){
				$('#firMolySelect').val(arr.firMolySelect);
				$('#firMolyDaySelect').val(arr.firMolyDaySelect);
				html+= arr.firMolySelect+'<u:msg titleId="wc.jsp.setRepetPop.tx05" alt="개월마다" /> ';
				html+= arr.firMolyDaySelect+'<u:msg titleId="wc.jsp.setRepetPop.tx07" alt="일" />';
				
			}else{
				$('#secMolySelect').val(arr.secMolySelect);
				$('#secMolyWeekSelect').val(arr.secMolyWeekSelect);
				$('#secMolyWeekOfDaySelect').val(arr.secMolyWeekOfDaySelect);
				html+= arr.secMolySelect+'<u:msg titleId="wc.jsp.setRepetPop.tx05" alt="개월마다" /> ';
				if(arr.secMolyWeekSelect == '1') html+='<u:msg titleId="wc.cols.week1" alt="1주차" />';
				if(arr.secMolyWeekSelect == '2') html+='<u:msg titleId="wc.cols.week2" alt="2주차" />';
				if(arr.secMolyWeekSelect == '3') html+='<u:msg titleId="wc.cols.week3" alt="3주차" />';
				if(arr.secMolyWeekSelect == '4') html+='<u:msg titleId="wc.cols.week4" alt="4주차" />';
				if(arr.secMolyWeekSelect == '5') html+='<u:msg titleId="wc.cols.week5" alt="5주차" />';
				html+='&nbsp;';
				html+=dayStr[arr.secMolyWeekOfDaySelect -1]+'<u:msg titleId="wc.cols.dayOfWeek" alt="요일" />';
			}
			$('#molyKnd').val(arr.molyKnd);
		}else if(arr.repetKnd == 'YELY'){
			html+='<u:msg titleId="wc.option.yely" alt="연간" /> - <u:msg titleId="wc.jsp.setRepetPop.tx08" alt="매년" />';
			if(arr.yelyKnd == '1'){
				$('#firYelySelect').val(arr.firYelySelect);
				$('#firYelyDaySelect').val(arr.firYelyDaySelect);
				html+=arr.firYelySelect+'<u:msg titleId="wc.jsp.setRepetPop.tx06" alt="월" />'+arr.firYelyDaySelect+'<u:msg titleId="wc.jsp.setRepetPop.tx07" alt="일" />';
			}else{
				$('#secYelySelect').val(arr.secYelySelect);
				$('#secYelyWeekSelect').val(arr.secYelyWeekSelect);
				$('#secYelyWeekOfDaySelect').val(arr.secYelyWeekOfDaySelect);
				html+=arr.secYelySelect+'<u:msg titleId="wc.jsp.setRepetPop.tx06" alt="월" />';
				if(arr.secYelyWeekSelect == '1') html+='<u:msg titleId="wc.cols.week1" alt="1주차" />';
				if(arr.secYelyWeekSelect == '2') html+='<u:msg titleId="wc.cols.week2" alt="2주차" />';
				if(arr.secYelyWeekSelect == '3') html+='<u:msg titleId="wc.cols.week3" alt="3주차" />';
				if(arr.secYelyWeekSelect == '4') html+='<u:msg titleId="wc.cols.week4" alt="4주차" />';
				if(arr.secYelyWeekSelect == '5') html+='<u:msg titleId="wc.cols.week5" alt="5주차" />';
				html+='&nbsp;';
				html+=dayStr[arr.secYelyWeekOfDaySelect -1]+'<u:msg titleId="wc.cols.dayOfWeek" alt="요일" />';
			}
		}
		$('#repetchoiDt').val(arr.repetchoiDt);  
		$('#repetcmltDt').val(arr.repetcmltDt);
		//반복설정 사용 Y
		$('#repetSetup').val('Y');
		$('#repetKnd').val(arr.repetKnd);
		$('#repetHtmlArea').html(html);
	}
};

//비어 있는 시간 확인
function emptyTimeGuest(){
	var $startDt = $("#schdlStrtYmd").val();
	var $endDt = $("#schdlEndYmd").val();
	
	var guestUsers = [];
	$("#guestTypContainer input[name='guestEmplYn']").each(function(){
		if($(this).val() == 'Y'){
			obj = $(this).parentTag('dd');
			id = $(obj).attr('id');
			if($(obj).find("input[name='guestUid']") != 'undefined'){
				guestUsers.push({userUid : $(obj).find("input[name='guestUid']").val() , userNm : $(obj).find("input[name='guestNm']").val()});
			}				
		}
	});
	
	if($startDt == "" || $endDt ==""){
		$m.dialog.alert("<u:msg titleId="wc.msg.prom.inpTime" alt="시간을 입력해주시기 바랍니다."/>");
		return;
	}
	
	if(guestUsers.length == 0){
		$m.dialog.alert("<u:msg titleId="wc.msg.prom.posAttend" alt="참석자를 지정해주시기 바랍니다."/>");
		return;
	}
	
	var guestUidsStr ="";
	var guestNmStr = "";
	$("#scdlStartDt").val($startDt);
	$("#scdlEndDt").val($endDt);
	
	for(var i=0;i<guestUsers.length;i++){
		guestUidsStr += guestUidsStr == "" ? guestUsers[i].userUid : "|"+ guestUsers[i].userUid;
		guestNmStr += guestNmStr == "" ? guestUsers[i].userNm : "|"+ guestUsers[i].userNm;
	}
	guestNmStr=encodeURIComponent(guestNmStr);
	var url = '/ct/schdl/viewEmptyTimeGuest.do?menuId=${menuId}&ctId=${param.ctId}&scdlStartDt='+$("#schdlStrtYmd").val()+'&scdlEndDt='+$("#schdlEndYmd").val()+'&guestUids='+guestUidsStr+'&guestNms='+guestNmStr; 
	$m.nav.next(null, url);
};

function getEditHtml(){
	return $('#bodyHtmlArea').html();
};

function setEditHtml(editHtml){
	$('#bodyHtmlArea').html(editHtml);
	$('#cont').html(editHtml);
};
$(document).ready(function() {
	$layout.adjustBodyHtml('bodyHtmlArea');
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
});
//]]>
</script>
<div class="btnarea">
    <div class="size">
        <dl>
        	<dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>           	 
            <dd class="btn" onclick="save();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
     </dl>
    </div>
</div>
<section>

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<form id="setRegForm" name="setRegForm" enctype="multipart/form-data" action="/ct/schdl/${transPage}Post.do?menuId=${menuId}">
		<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
		<c:if test="${!empty ctSchdlBVo.schdlId }">
			<input id="schdlId" name="schdlId" type="hidden" value="${ctSchdlBVo.schdlId }"/>
		</c:if>
		<input id="scdlStartDt" name="scdlStartDt" type="hidden" />
		<input id="scdlEndDt" name="scdlEndDt" type="hidden" />
    	
    	<textarea id="cont" name="cont" style="display:none">${ctSchdlBVo.cont}</textarea>
    	
    	<!-- 일정종류 -->
    	<u:set var="schdlTypCd" test="${empty ctSchdlBVo.schdlId && !empty wcCatClsBVoList }" value="${wcCatClsBVoList[0].catId }" elseValue="${ctSchdlBVo.schdlTypCd }"/>
    	<input type="hidden" id="schdlTypCd" name="schdlTypCd"  value="${schdlTypCd }"/>
    	
    	
    	<c:if test="${!empty param.agnt }">
			<input type="hidden" id="agnt" name="agnt"  value="${param.agnt}"/>
		</c:if>
		
		<input type="hidden" id="listPage" name="listPage" value="/ct/schdl/${listPage }.do?menuId=${menuId}&ctId=${param.ctId}&${paramsForList}" />
		<input type="hidden" id="viewPage" name="viewPage" value="/ct/schdl/viewSchdl.do?menuId=${menuId}&schdlId=${ctSchdlBVo.schdlId}&${paramsForList}" />
		
		<div id="repetSetupAll">
			<input id="repetSetup" name="repetSetup" type="hidden" />
			<input id="repetKnd" name="repetKnd" type="hidden" />
			<input id="dalySelect" name="dalySelect" type="hidden" />
			<input id="welySelect" name="welySelect" type="hidden" />
			<input id="dow" name="dow" type="hidden" />
			<input id="molyKnd" name="molyKnd" type="hidden" />
			<input id="firMolySelect" name="firMolySelect" type="hidden" />
			<input id="firMolyDaySelect" name="firMolyDaySelect" type="hidden" />
			<input id="secMolySelect" name="secMolySelect" type="hidden" />
			<input id="secMolyWeekSelect" name="secMolyWeekSelect" type="hidden" />
			<input id="secMolyWeekOfDaySelect" name="secMolyWeekOfDaySelect" type="hidden" />
			<input id="yelyKnd" name="yelyKnd" type="hidden" />
			<input id="firYelySelect" name="firYelySelect" type="hidden" />
			<input id="firYelyDaySelect" name="firYelyDaySelect" type="hidden" />
			<input id="secYelySelect" name="secYelySelect" type="hidden" />
			<input id="secYelyWeekSelect" name="secYelyWeekSelect" type="hidden" />
			<input id="secYelyWeekOfDaySelect" name="secYelyWeekOfDaySelect" type="hidden" />
			<input id="repetchoiDt" name="repetchoiDt" type="hidden" />
			<input id="repetcmltDt" name="repetcmltDt" type="hidden" />
		</div>
			
	    <div class="entryzone">
	        <div class="entryarea">
	        <dl>
		        <dd class="etr_tit"><u:msg titleId="wc.btn.schdl${!empty ctSchdlBVo.schdlId ? 'Mod' : 'Reg'}" alt="일정 등록/수정" /></dd>
		        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.schdlKnd" alt="일정종류"/></dd>
		        <dd class="etr_select">
                     <div class="etr_open1" id="schdlTypCdContainer" style="display:none">
                        <div class="open_in1">
                            <div class="open_div">
                            <dl>
                            	<c:forEach  var="list" items="${wcCatClsBVoList}" varStatus="status">
                            		<c:if test="${status.index > 0 && fn:length(wcCatClsBVoList) > status.count }"><dd class="line"></dd></c:if>
                            		<c:if test="${!empty ctSchdlBVo.schdlId && ctSchdlBVo.schdlTypCd == list.catId}"><c:set var="schdlTypNm" value="${list.catNm}"/></c:if>
									<dd class="txt" onclick="setOptionVal('schdlTypCd',$(this).attr('data-code'),$(this).text());" data-code="${list.catId }">${list.catNm }</dd>
								</c:forEach>
	                        </dl>
                            </div>
                        </div>
                    </div>
                    <c:if test="${ empty ctSchdlBVo.schdlId && !empty wcCatClsBVoList}"><c:set var="schdlTypNm" value="${wcCatClsBVoList[0].catNm}"/></c:if>
                    <div class="etr_ipmany">
                        <div class="select_in1" onclick="$('#setRegForm #schdlTypCdContainer').toggle();">
                        <dl>
                        <dd class="select_txt" id="schdlTypCdView"><span>${schdlTypNm }</span></dd>
                        <dd class="select_btn"></dd>
                        </dl>
                        </div>
                    </div>
                </dd>
                
                <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.subj" alt="제목" /></dd>
                <dd class="etr_input"><div class="etr_inputin"><input type="text" class="etr_iplt" name="subj" id="subj" value="${ctSchdlBVo.subj }"/></div></dd>
                <dd class="etr_bodytit"><u:msg titleId="cols.loc" alt="장소" /></dd>
                <dd class="etr_input"><div class="etr_inputin"><input type="text" class="etr_iplt" name="locNm" id="locNm" value="${ctSchdlBVo.locNm}"/></div></dd>
                
                <u:out value="${ctSchdlBVo.schdlStartDt}" type="date" var="schdlStrtYmd" />
				<u:out value="${ctSchdlBVo.schdlStartDt}" type="hm" var="schdlStrtTime" />
				<u:out value="${ctSchdlBVo.schdlEndDt}" type="date" var="schdlEndYmd" />
				<u:out value="${ctSchdlBVo.schdlEndDt}" type="hm" var="schdlEndTime" />

                <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.strtDt" alt="시작일자" /></dd>
		        <dd class="etr_select">
		            <div class="etr_calendar">
		            	<input id="schdlStrtYmd" name="schdlStrtYmd" value="${schdlStrtYmd}" type="hidden" />
		             	<input id="schdlStrtTime" name="schdlStrtTime" value="${schdlStrtTime}" type="hidden" />
		            	<div class="etr_calendarin">
                        <dl>
                        <dd class="ctxt" onclick="fnCalendar('schdlStrtYmd','{end:\'schdlEndYmd\'}','m','schdlStrtTime');"><span id="schdlStrtYmd">${schdlStrtYmd} ${schdlStrtTime}</span></dd>
                        <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                        <dd class="cbtn" onclick="fnCalendar('schdlStrtYmd','{end:\'schdlEndYmd\'}','m','schdlStrtTime');"></dd>
                        </dl>
                        </div>
		             </div>
		        </dd>    
		        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.endDt" alt="종료일자" /></dd>
		        <dd class="etr_select">
		            <div class="etr_calendar">
		            	<input id="schdlEndYmd" name="schdlEndYmd" value="${schdlEndYmd}" type="hidden" />
		            	<input id="schdlEndTime" name="schdlEndTime" value="${schdlEndTime}" type="hidden" />
		            	<div class="etr_calendarin">
                        <dl>
                        <dd class="ctxt" onclick="fnCalendar('schdlEndYmd','{start:\'schdlStrtYmd\'}','m','schdlEndTime');"><span id="schdlEndYmd">${schdlEndYmd} ${schdlEndTime}</span></dd>
                        <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                        <dd class="cbtn" onclick="fnCalendar('schdlEndYmd','{start:\'schdlStrtYmd\'}','m','schdlEndTime');"></dd>
                        </dl>
                        </div>
		            </div>
		        </dd>
        		
                <dd class="etr_input">
                    <div class="etr_ipmany">
                    <dl>
                    	<u:set var="checked" test="${ctSchdlBVo.alldayYn == 'Y'}" value="true" elseValue="false"/>
						<m:check type="checkbox" id="alldayYn" name="alldayYn" inputId="alldayYn" value="Y" checked="${checked }" titleId="wc.cols.alldayYn"/>
						
						<u:set var="checked" test="${ctSchdlBVo.solaLunaYn == 'N'}" value="true" elseValue="false"/>
						<m:check type="checkbox" id="solaLunaYn" name="solaLunaYn" inputId="solaLunaYn" value="N" checked="${checked }" titleId="wc.option.luna"/>
                    </dl>
                    </div>
                </dd>
                <c:if test="${!empty scdPidCount && scdPidCount > 1 && ctSchdlBVo.repetYn eq 'Y'}">
		        <dd class="etr_bodytit"><u:msg titleId="wc.cols.allSetting" alt="전체설정" /></dd>
		        <dd class="etr_input">
                    <div class="etr_ipmany" id="fileSettingContainer">
                    <dl>
                    	<m:check type="radio" id="fileSetting" name="fileSetting" inputId="fileSetting" value="indiv" areaId="fileSettingContainer" titleId="wc.option.indivApply" checked="true" />
						<m:check type="radio" id="fileSetting" name="fileSetting" inputId="fileSetting" value="all" areaId="fileSettingContainer" titleId="wc.option.allApply" checked="false" />
						<m:check type="radio" id="fileSetting" name="fileSetting" inputId="fileSetting" value="repet" areaId="fileSettingContainer" titleId="wc.btn.repetSetup" checked="false" onclick="repetSelect();"/>
                    </dl>
                    </div>
                </dd>
                </c:if>
        	</dl>
    	</div>
 	</div>
	<c:if test="${empty ctSchdlBVo.schdlId }">
	<!--entryzone S-->
	<div class="entryzone">
		<div class="entryarea">
			<dl>
	
			<dd class="etr_bodytit">
			<div class="icotit_dot"><u:msg titleId="wc.btn.repetSetup" alt="반복설정"/></div>
			<div class="icoarea">
				<dl>
				<dd class="btn" onclick="repetSelect();"><u:msg titleId="wc.btn.repetSetup" alt="반복설정"/></dd>
				</dl>
			</div>
			</dd>
			<dd class="etr_input"><div class="etr_bodyline" id="repetHtmlArea" style="min-height:40px;"></div></dd>
	
			</dl>
		</div>
	</div>
	<!--//entryzone E-->
	</c:if>
	<!--entryzone S-->
	<div class="entryzone">
		<div class="blank20"></div>
	    <div class="entryarea">
	    <dl>
	    	<dd class="etr_bodytit"><div class="icotit_dot"><u:msg titleId="cols.guestApnt" alt="참석자지정" /></div></dd>
			<dd class="etr_input">
				<div class="etr_ipmany" id="guestTypChkContainer">
	            <dl>
	            	<m:check type="radio" id="guestTyp" name="guestTyp" inputId="guestTyp" value="EMPL" areaId="guestTypChkContainer" titleId="cm.option.empl" checked="true" />
	            	<m:check type="radio" id="guestTyp" name="guestTyp" inputId="guestTyp" value="FRND" areaId="guestTypChkContainer" titleId="wc.option.frnd"/>
	            	<dd class="etr_btn" onclick="guestAdd();"><u:msg titleId="wc.btn.guestAdd" alt="참석자추가" /></dd>
	            	<dd class="etr_btn" onclick="emptyTimeGuest();"><u:msg titleId="wc.btn.freeTmCnfm" alt="빈시간확인" /></dd>
	            </dl>
	            </div>
			</dd>
			<dd class="etr_blank"></dd>
			<dd class="etr_select">
                 <div class="etr_open1" id="guestTypContainer" style="display:none">
                    <div class="open_in1">
                        <div class="open_div">
                        <dl id="selectedListArea">
                        	<c:forEach  var="list" items="${wcPromGuestDVoList}" varStatus="status">
                        		<dd class="txt_dd" data-guestUid="${list.guestUid }" data-emplTypCd="${list.guestEmplYn }" >
	                                <div class="txt_lt" >
									${list.guestNm}/<u:msg titleId="${list.guestEmplYn eq 'Y' ? 'cm.option.empl' : 'wc.option.frnd'}" alt="지인"/>/${list.guestCompNm}/${list.email}
									<input type="hidden" name="schdlId" value="${list.schdlId}" />
									<input type="hidden" name="guestUid" value="${list.guestUid}" />
									<input type="hidden" name="guestNm" value="${list.guestNm}"/>
									<input type="hidden" name="guestDeptNm" value="${list.guestDeptNm}"/>
									<input type="hidden" name="statCd" value="${list.statCd}"/>
									<input type="hidden" name="guestCompNm" value="${list.guestCompNm}"/>
									<input type="hidden" name="email" value="${list.email}"/>
									<input type="hidden" name="guestEmplYn" value="${list.guestEmplYn}"/>
									</div>
	                                <div class="txt_delete" onclick="delSelects(this);"></div>
								</dd>
								<c:if test="${fn:length(wcPromGuestDVoList) > status.count }"><dd class="line"></dd></c:if>
							</c:forEach>
                     	</dl>
                        </div>
                    </div>
                </div>
                <c:if test="${ !empty ctSchdlBVo.schdlId && !empty wcPromGuestDVoList}"><c:set var="guestNm" value="${wcPromGuestDVoList[0].guestNm}"/></c:if>
                <div class="etr_ipmany">
                    <div class="select_in1" onclick="$('#setRegForm #guestTypContainer').toggle();">
                    <dl>
                    <dd class="select_txt" id="guestNmView"><span><u:msg titleId="cols.guestApnt" alt="참석자지정" /></span></dd>
                    <dd class="select_btn"></dd>
                    </dl>
                    </div>
                </div>
            </dd>
            <c:if test="${empty ctSchdlBVo.schdlId && mailEnable == 'Y'}">
            	<dd class="etr_input">
                    <div class="etr_ipmany">
                    <dl>
                    	<u:set var="checked" test="${ctSchdlBVo.emailSendYn == 'Y'}" value="true" elseValue="false"/>
						<m:check type="checkbox" id="emailSendY" name="emailSendYn" inputId="emailSendYn" value="Y" titleId="cols.emailSend" checked="${checked }" />
                    </dl>
                    </div>
                </dd>
            </c:if>
	        </dl>
	    </div>
	</div>
	<!--//entryzone E-->
	
	<!--entryzone S-->
	<div class="entryzone">
		<div class="entryarea">
			<dl>
	
			<dd class="etr_bodytit">
			<div class="icotit_dot"><u:msg titleId="cols.cont" alt="내용" /></div>
			<div class="icoarea">
				<dl>
				<dd class="btn" onclick="$m.openEditor();"><u:msg titleId="mcm.title.editCont" alt="내용편집" /></dd>
				</dl>
			</div>
			</dd>
			<dd class="etr_input"><div class="etr_bodyline editor" id="bodyHtmlArea" ><c:if test="${!empty ctSchdlBVo.cont }"><u:out value="${ctSchdlBVo.cont}" type="noscript" /></c:if></div></dd>
	
			</dl>
		</div>
	</div>
	<!--//entryzone E-->
	
	<!--entryzone S-->
    <div class="entryzone">
		<div class="blank20"></div>
		<div class="entryarea">
			<dl>
			<dd class="etr_bodytit">
			<div class="icotit_dot"><u:msg titleId="cols.attFile" alt="첨부파일" /></div>
			<div class="icoarea">
				<dl>
				<dd class="btn" onclick="addFile('${filesId}');"><u:msg titleId="cm.btn.fileAtt" alt="파일첨부"  /></dd>
				</dl>
			</div>
			</dd>
			</dl>
		</div>
	</div>
    <!--//entryzone E-->
    <m:files id="${filesId}" fileVoList="${fileVoList}" module="wc" mode="set" exts="${exts }" extsTyp="${extsTyp }"/>
	
    <div class="blank25"></div>
    <div class="btnarea">
        <div class="size">
            <dl>
            <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
            <dd class="btn" onclick="save();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
         </dl>
        </div>
    </div>

	
	</form>
	
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
 </section>