<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 명함 정보 추가 %>
function atndAdd(objArr){
	addCols(objArr,"N");
};

<%// 명함 조회 팝업 %>
function findBcPop(){
	dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}${agntParam}');
};

<%// 선택추가 %>
function addCols(arr , emplTypCd){	
	if(arr==null) return;
	var $tr, $hiddenTr = $("#guestListArea tbody:first #hiddenTr"),emplId,emplNm;
	var html = $hiddenTr[0].outerHTML;
	var vas = getAllVas(emplTypCd);
	arr.each(function(index, obj){
		emplId = emplTypCd == 'Y' ? obj.userUid : obj.bcId;
		if(vas==null || !vas.contains(emplId)){
			$hiddenTr.before(html);
			$tr = $hiddenTr.prev();
			emplNm = emplTypCd == 'Y' ? obj.rescNm : obj.bcNm;
			$tr.attr('id','tr'+emplTypCd+'_'+emplId);
			$tr.find("input[type='checkbox']").val(emplId);
			$tr.find("input[type='checkbox']").attr("data-emplTypCd",emplTypCd);
			$tr.find("td#emplNm").text(emplNm);
			$tr.find("td#emplTypNm").text(emplTypCd == 'Y' ? "<u:msg titleId="cm.option.empl" alt="임직원"/>" : "<u:msg titleId="wc.option.frnd" alt="지인"/>");
			$tr.find("input[name='guestNm']").val(emplNm);
			$tr.find("input[name='guestEmplYn']").val(emplTypCd);
			$tr.find("input[name='guestUid']").val(emplId);
			$tr.find("td#emplCompNm div.ellipsis").text(emplTypCd == 'Y' ? obj.deptRescNm : obj.compNm);
			$tr.find("td#emplEmail").text(obj.email);
			$tr.find("input[name='guestCompNm']").val(emplTypCd == 'Y' ? obj.deptRescNm : obj.compNm);
			$tr.find("input[name='email']").val(obj.email);
			$tr.show();
			setJsUniform($tr[0]);
		}
	});
};

<%// 선택제거 %>
function removeCols(){
	var arr = getCheckedTrs("cm.msg.noSelect");
	if(arr==null) return;
	arr.each(function(index, tr){
		$(tr).remove();
	}, true);
};

<%//checkbox 가 선택된 tr 테그 목록 리턴 %>
function getCheckedTrs(noSelectMsg){
	var arr=[], id, obj;
	$("#guestListArea tbody:first input[type='checkbox']:checked").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr') arr.push(obj);
	});
	if(arr.length==0){
		if(noSelectMsg!=null) alertMsg(noSelectMsg);
		return null;
	}
	return arr;
};

//임직원, 지인 라디오 선택
function guestChange(obj){
	$('#setRegForm input[id="guest"]').val('');
};

//참석자 추가
function guestAdd(){
	var guestTyp = $('#setRegForm input:radio[name="guestTyp"]:checked').val();
	if(guestTyp == 'N'){
		if($('#guest').val()!=''){
			callAjax('./srchUserAjx.do?menuId=${menuId}&ctId=${param.ctId}', {srchTyp:'bc',srchName:$('#guest').val()}, function(data) {
				if (data.returnString != null) {
					var arr=[];
					arr.push($.parseJSON(data.returnString));
					atndAdd(arr);
					return;	
				}
				dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}${agntParam}&callBack=atndAdd&fncMul=Y&schCat=bcNm&schWord='+encodeURIComponent($('#guest').val()));
			});
		}else{
			dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}${agntParam}&callBack=atndAdd&fncMul=Y&schCat=bcNm&schWord='+encodeURIComponent($('#guest').val()));
		}	
	}else{
		if($('#guest').val()!=''){
			callAjax('./srchUserAjx.do?menuId=${menuId}&ctId=${param.ctId}', {srchTyp:'user',srchName:$('#guest').val()}, function(data) {
				if (data.returnString != null) {
					var arr=[];
					arr.push($.parseJSON(data.returnString));
					addCols(arr,"Y");
					return;
				}
				openMuiltiUser();
			});
		}else{
			openMuiltiUser();
		}
	}
};

//여러명의 사용자 선택
function openMuiltiUser(mode){
	var $view = $("#guestListArea"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>	
	$view.find("tbody:first input[type='checkbox']").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr' && $(this).attr('data-emplTypCd') != null && $(this).attr('data-emplTypCd') == 'Y' ) {
			data.push({userUid:$(this).val()});
		};
	});
	
	var param={data:data, multi:true, mode:mode==null ?'search':'view' , userNm:encodeURIComponent($('#guest').val())};
	<c:if test="${!empty globalOrgChartEnable && globalOrgChartEnable==true}">
		param.global='Y';
	</c:if>
	
	<%// option : data, multi, titleId %>
	searchUserPop(param, function(arr){
		if(arr!=null){
			addCols(arr,"Y");
		}
	});
};

<%//현재 등록된 id 목록 리턴 %>
function getAllVas(emplTypCd){
	var arr=[];
	$('#guestListArea input[type="checkbox"]').each(function(){
		if($(this).attr("data-emplTypCd") == emplTypCd){
			arr.push($(this).val());
		}
	});
	if(arr.length==0){
		return null;
	}
	return arr;
};

//명함 선택
function fnBcSelect(detlViewType, callBack ){
	var objArr = getIframeContent(detlViewType+'Frm').fnSelArrs();
	if(objArr == null ) return;
	if(callBack != "" ){
		eval(callBack)(objArr);
	}else{
		objArr.each(function(index, obj){
			$("#setRegForm input[name=bcId]").val(obj.bcId);
			$("#setRegForm input[name=bcNm]").val(obj.bcNm);
			$("#setRegForm input[name=bcCompNm]").val(obj.compNm);
		});		
	}
	dialog.close('findBcPop');
};

//종일여부
function fnAllday(obj){
	var disabled = false;
	if(obj.checked) disabled = true;
	setDisabled($('#setRegForm #schdlStrtTime'), disabled);
	setDisabled($('#setRegForm #schdlEndTime'), disabled);
};
//일정대상 select
function fnTargetChange(obj){
	if(obj.value =="2") setDisabled($('#setRegForm #grpId'), false);
	else setDisabled($('#setRegForm #grpId'), true);
};

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};

//DIV 팝업
var custumDialog = {
	data:{
		clientX:null, clientY:null, objectTop:null, objectLeft:null, documentHeight:null, documentWidth:null, instance:null, zindex:1000 }
	,
	noFocus:false,
	openYn:{
	}
	,
	closeHandler:{
	}
	,
	onClose:function(id, handler){
		this.closeHandler[id] = handler;
	}
	,
	open:function(id, title, url, param, width, height, focusId, failHandler){
		var $dialog = $('#'+id);
		if($dialog.length==0){
			$(".styleThese").first().append('<div class="layerpoparea" id="'+id+'" style="position:absolute; z-index:'+(++dialog.data.zindex)+'; visibility:visible; display:none;" ></div>');
			$dialog = $('#'+id);

			var buff=[];
			buff.push('<div id="dialogTitle" class="lypoptit" style="cursor:move;">');
			buff.push('	<dl>');
			buff.push('	<dd id="dialogTitleTxt" class="title">'+title+'</dd>');
			buff.push('	<dd class="btn"><a href="javascript:void(0);" id="cloasBtn" onclick="setRepetPopClose();" class="close"><span>close</span></a></dd>');
			buff.push('	</dl>');
			buff.push('</div>');
			buff.push('<div id="dialogBody" class="bodyarea">');
			buff.push('</div>');
			$dialog.append(buff.join('\n'));

			$dialog.find('#dialogTitle').bind("mousedown", function(event){
			var outer = this.parentNode;
			dialog.data.instance = outer;
			dialog.data.objectTop = parseInt($(outer).css('top'));
			dialog.data.objectLeft = parseInt($(outer).css('left'));
			dialog.data.documentHeight = $(document).height();
			dialog.data.documentWidth = $(document).width();
			dialog.data.clientX = event.clientX;
			dialog.data.clientY = event.clientY;

			if($(outer).css('z-index')!=dialog.data.zindex){
			$(outer).css('z-index', ++dialog.data.zindex);
			}

			event.preventDefault();
			return false;
			});
			var windowObj = (browser.ie && browser.ver<9) ? document : window;
			$(windowObj).bind("mouseup", function(event){
			dialog.data.instance = null;
			});
			$(windowObj).bind("mousemove", function(event){
			if(dialog.data.instance != null){
			var diff;
			var topPos = event.clientY - dialog.data.clientY + dialog.data.objectTop;
			topPos = Math.max(-10, topPos);
			diff = (browser.ie && browser.ver<8) || browser.firefox ? 25 : 40;
			topPos = Math.min(dialog.data.documentHeight-diff, topPos);
			var leftPos = event.clientX - dialog.data.clientX + dialog.data.objectLeft;
			leftPos = Math.max(30 - $(dialog.data.instance).width(), leftPos);
			diff = browser.ie && browser.ver>7 ? 50 : 30;
			leftPos = Math.min(dialog.data.documentWidth-diff, leftPos);
			$(dialog.data.instance).css('top', topPos);
			$(dialog.data.instance).css('left', leftPos);
			event.preventDefault();
			return false;
			}
			});

		}
		else {
			$dialog.find('#dialogTitleTxt').text(title);
			$dialog.css('z-index', ++dialog.data.zindex);
		}
		var popHtml = callHtml(url, param, failHandler);
		if(popHtml!=null){
			var $body = $dialog.find('#dialogBody');
			var html = popHtml;

			$body.html(html);
			var left = $dialog.css("left");
			var opened = left!='auto' && left!='0px';

			left = !opened ? -2000 : parseInt(left) - 2000;
			$dialog.css("left", left+"px");
			$dialog.show();

			$body.find("input, textarea, select, button").not(".skipThese").uniform();
			$body.find("input:checkbox,input:radio").bind("keydown", function(event){
			if(event.which==13) $(this).trigger("click");
			});
			this.resize(id, width, height, opened);
			if(this.noFocus){
				this.noFocus = false;
			}
			else {
				if(focusId==null){
					var $focus = $dialog.find("input:visible, select:visible, textarea:visible").not("[readonly='readonly']").first();
					if($focus.length>0) $focus.focus();
					else {
						$dialog.find("#cloasBtn:first").focus();
					}
				}
				else {
					$dialog.find("#"+focusId).focus();
				}
			}
		}
		this.openYn[id] = 'Y';
	}
	,
	resize:function(id, width, height, opened){
		var $dialog = $('#'+id);
		var $sizeRefer = $dialog.find('#dialogBody').find("div:first");

		var dialogWidth = width==null ? Math.max($sizeRefer.width()+30, 200) : width;
		var dialogHeight = height==null ? Math.max($sizeRefer.height()+55, 120) : height;
		$dialog.css('width',dialogWidth+'px');
		$dialog.css('height',dialogHeight+'px');

		if(opened==null){
		}
		else if(opened==true){
			var left = parseInt($dialog.css("left")) + 2000;
			$dialog.css("left", left+"px");
		}
		else {
			var dialogTop = Math.max(parseInt(($(window).height() - dialogHeight)/2 + $(window).scrollTop()),$(window).scrollTop());
			var dialogLeft = Math.max(parseInt(($(window).width() - dialogWidth)/2 + $(window).scrollLeft()),$(window).scrollLeft());
			$dialog.css("top",dialogTop);
			$dialog.css("left",dialogLeft);
		}
	}
	,
	close:function(obj){
		var id=null, handler;
		if (typeof obj == 'string') {
			var $pop = $('#'+obj);
			$pop.find('#cloasBtn').focus();
			$pop.hide();
			$pop.find('.bodyarea').empty();
			$pop.css('left','0px');
			id = obj;
		}
		else {
			var p = obj.parentNode;
			while(p!=null){
				if(p.tagName!=null && p.tagName.toLowerCase()=='body'){
					break;
				}
				if($(p).attr('class')=='layerpoparea'){
					$(p).find('#cloasBtn').focus();
					$(p).hide();
					$(p).find('.bodyarea').empty();
					$(p).css('left','0px');
					id = $(p).attr('id');
					break;
				}
				p = p.parentNode;
			}
		}
		if(id!=null){
			this.openYn[id] = 'N';
			if((handler = dialog.closeHandler[id]) != null){
				dialog.closeHandler[id] = null;
				handler(id);
			}
		}
	}
	,
	forward:function(id){
		var $dialog = $('#'+id);
		if($dialog.length>0) $dialog.css('z-index', ++dialog.data.zindex);
	}
	,
	isOpen:function(id){
		return 'Y' == this.openYn[id];
	}
	,
	closeAll:function(){
		for(var id in this.openYn) {
			if(this.openYn[id] == 'Y'){
				dialog.close(id);
			}
		}
	}
};


function findOrgc() {
	dialog.open('findOrgcPop','<u:msg titleId="or.jsp.setOrg.orgTreeTitle" alt="조직도" />','/bb/findOrgcPop.do?menuId=${menuId}');
}

//반복설정 test
var gRepetHandler = null;
var gRepetSelectedObj = null;
function repetSetting( handler){
	gRepetHandler = handler;
}

function formSubit(){
	if(validator.validate('setRegForm')){
		var startDt=$("#schdlStrtYmd").val();
		var endDt=$("#schdlEndYmd").val();
		if(startDt==endDt){
			if(parseInt($("#schdlStrtTime").val().substring( 0, 2))>parseInt($("#schdlEndTime").val().substring( 0, 2))){
				alert("<u:msg titleId="wc.msg.chkTime" alt="시작시간이 종료시간 보다 클수는 없습니다."/>");
				return;
			}
		}
		
		if (true/*confirmMsg("cm.cfrm.save")*/ ) {
			var $form = $('#setRegForm');
			if(typeof $('#agnt').val()!="undefined"){
				$form.append("<input type='hidden' id='agnt' name='agnt' value='"+$('#agnt').val()+"'>");
			}
			$form.append("<input type='hidden' id='molyYear' name='molyYear' value='"+$('#molyYear').val()+"'>");
			$form.append("<input type='hidden' id='molyMonth' name='molyMonth' value='"+$('#molyMonth').val()+"'>");
			$form.append("<input type='hidden' id='welyYear' name='welyYear' value='"+$('#welyYear').val()+"'>");
			$form.append("<input type='hidden' id='welyMonth' name='welyMonth' value='"+$('#welyMonth').val()+"'>");
			$form.append("<input type='hidden' id='welyWeek' name='welyWeek' value='"+$('#welyWeek').val()+"'>");
			$form.append("<input type='hidden' id='dalyYear' name='dalyYear' value='"+$('#dalyYear').val()+"'>");
			$form.append("<input type='hidden' id='dalyMonth' name='dalyMonth' value='"+$('#dalyMonth').val()+"'");
			$form.append("<input type='hidden' id='dalyDay' name='dalyDay' value='"+$('#dalyDay').val()+"'>");
			$form.append("<input type='hidden' id='fncCal' name='fncCal' value='"+$('#fncCal').val()+"'>");
			$form.append("<input type='hidden' id='tabNo' name='tabNo' value='"+$('#tabNo').val()+"'>");
			
			
			var transPage = "transSchdl";
			if('${ctSchdlBVo.schdlId}' != ''){
				transPage = "transSchdlMod";
			}
			
			$form.attr('method','post');
			$form.attr('action','./'+transPage+'.do?${params}');
			$form.attr('enctype','multipart/form-data');
			$form.attr('target','dataframe');
			editor("cont").prepare();
			saveFileToForm('ctfiles', $form[0], null);
			//$form[0].submit();
	
			dialog.close(this);
		}
	}
}

<% // 전체설정 초기화 %>
function chkFileSetting(){
	$('input:radio[name="fileSetting"]:input[value="indiv"]').prop('checked',true);
	$('input:radio[name="fileSetting"]').uniform();
}

var setRepetPop=null;
function repetSelect(mode){
	if(setRepetPop==null){
		$("#setRepetPop").remove();
		var schdlStrtYmd = $('#setRegForm #schdlStrtYmd').val();
		var url = './setRepetPop.do?menuId=${menuId}';
		if(schdlStrtYmd != '') url+='&repetchoiDisDt='+schdlStrtYmd;
		if('${param.schdlId}' != '') url+='&schdlId=${param.schdlId}';
		custumDialog.open('setRepetPop','<u:msg titleId="wc.btn.repetSetup" alt="반복설정"/>',url);
		setRepetPop=$("#setRepetPop");

		repetSetting(
		function (arr){

		var repetSetups = [];
		var repetKnds = [];
		var dalySelects = [];
		var welySelects = [];
		var dows = [];
		var molyKnds = [];
		var firMolySelects = [];
		var firMolyDaySelects = [];
		var secMolySelects = [];
		var secMolyWeekSelects = [];
		var secMolyWeekOfDaySelects = [];
		var yelyKnds = [];
		var firYelySelects = [];
		var firYelyDaySelects = [];
		var secYelySelects = [];
		var secYelyWeekSelects = [];
		var secYelyWeekOfDaySelects = [];
		var repetchoiDts = [];
		var repetcmltDts = [];


		if(arr!= null){
		arr.each(function(index, repetVo){
		gRepetSelectedObj=repetVo;
		repetSetups.push("<input id='repetSetup' name='repetSetup' type='repetSetup' value='"+repetVo.repetSetup+"' />\n");
		if(repetVo.repetSetup=='N'){
		return false;
		}
		repetKnds.push("<input id='repetKnd' name='repetKnd' type='hidden' value='"+repetVo.repetKnd+"' />\n");
		dalySelects.push("<input id='dalySelect' name='dalySelect' type='hidden' value='"+repetVo.dalySelect+"' />\n");
		welySelects.push("<input id='welySelect' name='welySelect' type='hidden' value='"+repetVo.welySelect+"' />\n");
		dows.push("<input id='dow' name='dow' type='hidden' value='"+repetVo.dow+"' />\n");
		molyKnds.push("<input id='molyKnd' name='molyKnd' type='hidden' value='"+repetVo.molyKnd+"' />\n");
		firMolySelects.push("<input id='firMolySelect' name='firMolySelect' type='hidden' value='"+repetVo.firMolySelect+"' />\n");
		firMolyDaySelects.push("<input id='firMolyDaySelect' name='firMolyDaySelect' type='hidden' value='"+repetVo.firMolyDaySelect+"' />\n");
		secMolySelects.push("<input id='secMolySelect' name='secMolySelect' type='hidden' value='"+repetVo.secMolySelect+"' />\n");
		secMolyWeekSelects.push("<input id='secMolyWeekSelect' name='secMolyWeekSelect' type='hidden' value='"+repetVo.secMolyWeekSelect+"' />\n");
		secMolyWeekOfDaySelects.push("<input id='secMolyWeekOfDaySelect' name='secMolyWeekOfDaySelect' type='hidden' value='"+repetVo.secMolyWeekOfDaySelect+"' />\n");
		yelyKnds.push("<input id='yelyKnd' name='yelyKnd' type='hidden' value='"+repetVo.yelyKnd+"' />\n");
		firYelySelects.push("<input id='firYelySelect' name='firYelySelect' type='hidden' value='"+repetVo.firYelySelect+"' />\n");
		firYelyDaySelects.push("<input id='firYelyDaySelect' name='firYelyDaySelect' type='hidden' value='"+repetVo.firYelyDaySelect+"' />\n");
		secYelySelects.push("<input id='secYelySelect' name='secYelySelect' type='hidden' value='"+repetVo.secYelySelect+"' />\n");
		secYelyWeekSelects.push("<input id='secYelyWeekSelect' name='secYelyWeekSelect' type='hidden' value='"+repetVo.secYelyWeekSelect+"' />\n");
		secYelyWeekOfDaySelects.push("<input id='secYelyWeekOfDaySelect' name='secYelyWeekOfDaySelect' type='hidden' value='"+repetVo.secYelyWeekOfDaySelect+"' />\n");
		repetchoiDts.push("<input id='repetchoiDt' name='repetchoiDt' type='hidden' value='"+repetVo.repetchoiDt+"' />\n");
		repetcmltDts.push("<input id='repetcmltDt' name='repetcmltDt' type='hidden' value='"+repetVo.repetcmltDt+"' />\n");

		});
		$("#repetSetup").html(repetSetups.join(''));
		$("#repetKnd").html(repetKnds.join(''));
		$("#dalySelect").html(dalySelects.join(''));
		$("#welySelect").html(welySelects.join(''));
		$("#dow").html(dows.join(''));
		$("#molyKnd").html(molyKnds.join(''));
		$("#firMolySelect").html(firMolySelects.join(''));
		$("#firMolyDaySelect").html(firMolyDaySelects.join(''));
		$("#secMolySelect").html(secMolySelects.join(''));
		$("#secMolyWeekSelect").html(secMolyWeekSelects.join(''));
		$("#secMolyWeekOfDaySelect").html(secMolyWeekOfDaySelects.join(''));
		$("#yelyKnd").html(yelyKnds.join(''));
		$("#firYelySelect").html(firYelySelects.join(''));
		$("#firYelyDaySelect").html(firYelyDaySelects.join(''));
		$("#secYelySelect").html(secYelySelects.join(''));
		$("#secYelyWeekSelect").html(secYelyWeekSelects.join(''));
		$("#secYelyWeekOfDaySelect").html(secYelyWeekOfDaySelects.join(''));
		$("#repetchoiDt").html(repetchoiDts.join(''));
		$("#repetcmltDt").html(repetcmltDts.join(''));

		}
		});
	}
	else{
		setRepetPop.show();
	}
};

//비어 있는 시간 확인
function emptyTimeGuest(){
	var $startDt = $("#schdlStrtYmd").val();
	var $endDt = $("#schdlEndYmd").val();
	
	var guestUsers = [];
	$("#guestListArea tbody:first input[name='guestEmplYn']").each(function(){
		if($(this).val() == 'Y'){
			obj = getParentTag(this, 'tr');
			id = $(obj).attr('id');
			if(id!='headerTr' && id!='hiddenTr') {
				guestUsers.push({userUid : $(obj).find("input[name='guestUid']").val() , userNm : $(obj).find("input[name='guestNm']").val()});
			}	
		}
	});
	
	if($startDt == "" || $endDt ==""){
		alert("<u:msg titleId="wc.msg.prom.inpTime" alt="'약속시간'을 입력해주시기 바랍니다."/>");
		return;
	}
	
	if(guestUsers.length == 0){
		alert("<u:msg titleId="wc.msg.prom.posAttend" alt="'참석자'를 지정해주시기 바랍니다."/>");
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
	dialog.open('viewEmptyTimeGuestPop', '<u:msg titleId="wc.btn.freeTmCnfm" alt="빈시간확인"/>','./viewEmptyTimeGuestPop.do?menuId=${menuId}&scdlStartDt='+$("#schdlStrtYmd").val()+'&scdlEndDt='+$("#schdlEndYmd").val()+'&guestUids='+guestUidsStr+'&guestNms='+guestNmStr);
}


$(document).ready(function () {
//파일첨부 관련 pop resize
$("#wcfilesArea").bind('resize', function(e) {
$("#setPromPop").css('height', 'auto');
});
});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="promNm" value="dddd" elseValue="" />
<u:set test="${fnc == 'mod'}" var="loc" value="대회의실" elseValue="" />
<u:set test="${fnc == 'mod'}" var="cont" value="1월 2주차 주간 미팅 참석" elseValue="" />
<u:set test="${fnc == 'mod'}" var="strtYmd" value="2014-01-10" elseValue="" />
<u:set test="${fnc == 'mod'}" var="endYmd" value="2014-01-10" elseValue="" />

<input id="userUid" type="hidden" />
<input id="rescId" type="hidden" />
<input id="rescNm" type="hidden" />
<input id="positNm" type="hidden" />
<input id="deptRescNm" type="hidden" />
<input id="email" type="hidden" />
<input id="extnEmail" type="hidden" />

<input id="friUid" type="hidden" />
<input id="friNm" type="hidden" />
<input id="friCompNm" type="hidden" />


<div style="width:715px">

	<form id="setRegForm"  method="post" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<c:if test="${!empty ctSchdlBVo.schdlId }">
		<input id="schdlId" name="schdlId" type="hidden" value="${ctSchdlBVo.schdlId }"/>
	</c:if>
	<input id="scdlStartDt" name="scdlStartDt" type="hidden" />
	<input id="scdlEndDt" name="scdlEndDt" type="hidden" />
	
	<u:input type="hidden" id="listPage" value="./${listPage }.do?${paramsForList}" />
	
	<!-- <input id="guestUids" name="guestUids" type="hidden" />
	<input id="rescNm" name="rescNm" type="hidden"/> -->
	
	<c:if test="${empty ctSchdlBVo.schdlId }">
		<div class="front">
			<div class="front_right">
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody>
					<tr>
						<td class="frontbtn"><u:buttonS titleId="wc.btn.repetSetup" alt="반복설정"	href="javascript:repetSelect();" /></td>
					</tr>
					</tbody>
				</table>
			</div>
		</div>
	</c:if>
	
	<% // 폼 필드 %>
	<u:listArea noBottomBlank="true" colgroup="18%,32%,18%,32%">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.schdlKnd" alt="일정종류"/></td>
		<td>
			<select id="schdlTypCd"  name="schdlTypCd" >
			<c:forEach  var="list" items="${wcCatClsBVoList}" varStatus="status">
				<u:option value="${list.catId}" title="${list.catNm }" checkValue="${ctSchdlBVo.schdlTypCd}"/>
			</c:forEach>
		</select>
		</td>
		<td class="head_lt"><u:msg titleId="cols.prio" alt="우선순위" /></td>
		<td>
			<select name="workPrioOrdr" id="workPrioOrdr">
				<c:forEach var="orderNum" begin="1" end="5" >
					<u:option value="${orderNum }" title="${orderNum }" checkValue="${ctSchdlBVo.workPrioOrdr }"/>
				</c:forEach>
			</select>
		</td>
	</tr>
	
	</u:listArea>
	<u:blank/>
	<% // 폼 필드 %>
	<u:listArea noBottomBlank="true">
	<tr>
		<td width="18%" class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
		<td width="82%"><u:input id="subj" value="${ctSchdlBVo.subj }" titleId="cols.subj" style="width: 556px;" mandatory="Y" maxByte="240"/></td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.loc" alt="장소" /></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td><u:input id="locNm" value="${ctSchdlBVo.locNm}" titleId="cols.loc" style="width: 556px;" maxLength="60" /></td>
					
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="wc.cols.schdlPriod" alt="기간"/></td>
		<td>
			<fmt:parseDate var="dateStartDt" value="${ctSchdlBVo.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
			<fmt:formatDate var="schdlStrtTime" value="${dateStartDt}" pattern="HH:mm"/>
			<fmt:parseDate var="dateEndDt" value="${ctSchdlBVo.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="yyyy-MM-dd"/>
			<fmt:formatDate var="schdlEndTime" value="${dateEndDt}" pattern="HH:mm"/>
			
			<u:set var="alldayDisabled" test="${ctSchdlBVo.alldayYn eq 'Y' }" value="disabled='disabled'" elseValue=""/>	
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td width="80px"><u:calendar id="schdlStrtYmd" option="{end:'schdlEndYmd'}" titleId="cols.choiDt" alt="시작일시" mandatory="Y" value="${convStartDt }"/></td>
					<td>
						<select name="schdlStrtTime" id="schdlStrtTime" style="width:60px;" ${alldayDisabled }>
						<c:forEach var="strtTime" begin="00" end="23">
							<u:set var="timeVal" test="${strtTime < 10 }" value="0${strtTime }" elseValue="${strtTime }"/>
							<u:option value="${timeVal}:00" title="${timeVal}:00" checkValue="${schdlStrtTime }" />
							<u:option value="${timeVal}:30" title="${timeVal}:30" checkValue="${schdlStrtTime }" />
						</c:forEach>
						</select>
					</td>
					<td class="body_lt">~</td>
					<td width="80px"><u:calendar id="schdlEndYmd" option="{start:'schdlStrtYmd'}" titleId="cols.cmltDt" alt="완료일시" mandatory="Y" value="${convEndDt }"/></td>
					<td>
						<select name="schdlEndTime" id="schdlEndTime" style="width:60px;" ${alldayDisabled }>
						<c:forEach var="endTime" begin="0" end="23">
							<u:set var="timeVal" test="${endTime < 10 }" value="0${endTime }" elseValue="${endTime }"/>
							<u:option value="${timeVal }:00" title="${timeVal }:00" checkValue="${schdlEndTime }" />
							<u:option value="${timeVal }:30" title="${timeVal }:30" checkValue="${schdlEndTime }" />
						</c:forEach>
						</select>
					</td>
					<td><u:checkbox name="alldayYn" value="Y" titleId="wc.cols.alldayYn" alt="종일여부" onclick="fnAllday(this);" checkValue="${ctSchdlBVo.alldayYn }"/></td>
					<td><u:checkbox name="solaLunaYn" value="N" titleId="wc.option.luna" alt="음력" checkValue="${ctSchdlBVo.solaLunaYn }"/></td>
				</tbody>
			</table>
		</td>
	</tr>
	<c:if test="${!empty scdPidCount && scdPidCount > 1 && ctSchdlBVo.repetYn eq 'Y'}">
		<tr>
		<td class="head_lt"><u:msg titleId="wc.cols.allSetting" alt="전체설정" /></td>
		<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><u:radio name="fileSetting" value="indiv" titleId="wc.option.indivApply" alt="개별반영" checked="true" /></td>
				<td><u:radio name="fileSetting" value="all" titleId="wc.option.allApply" alt="전체반영" checked="false"  /></td>
				<td><u:radio name="fileSetting" value="repet" titleId="wc.btn.repetSetup" alt="반복설정" onclick="repetSelect();"/></td>
			</tr>
			</tbody></table></td>
		</tr>
	</c:if>

	<tr>
		<td rowspan="2" class="head_lt"><u:msg titleId="cols.guestApnt" alt="참석자지정" /></td>
		<td class="bodybg_lt">
			<u:checkArea>
				<u:radio name="guestTyp" value="Y" titleId="cm.option.empl" alt="임직원" inputClass="bodybg_lt" onclick="guestChange(this);" checked="true"/>
				<u:radio name="guestTyp" value="N" titleId="wc.option.frnd" alt="지인" inputClass="bodybg_lt" onclick="guestChange(this);"/>
			<td><u:input id="guest" value="" titleId="cols.guest" style="width: 100px;" onkeydown="if (event.keyCode == 13){guestAdd();return false;}"/></td>
			<td><u:buttonS href="" titleId="wc.btn.guestAdd" alt="참석자추가" onclick="guestAdd();"/><u:buttonS href="javascript:removeCols();" titleId="wc.btn.guestDel" alt="참석자삭제" /></td>
			<td><u:buttonS href="javascript:emptyTimeGuest()" titleId="wc.btn.freeTmCnfm" alt="빈시간확인" /></td>
			<c:if test="${empty ctSchdlBVo.schdlId && mailEnable == 'Y'}">
				<u:checkbox id="emailSendYn" name="emailSendYn" value="Y" titleId="cols.emailSend" alt="이메일발송" inputClass="bodybg_lt" />
			</c:if>
			</u:checkArea>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td>
						<div style="width:100%;height:${empty namoEditorEnable ? 160 : 110}px;overflow-y:auto;">
							<div id="guestListArea" class="listarea" style="width:95%; padding:5px;">
								<table class="listtable" border="0" cellpadding="0" cellspacing="1" >
								<tr id="headerTr">
									<th width="36"  class="head_ct"><input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('guestListArea', this.checked);" value=""/></th>
									<th width="17%"  class="head_ct"><u:msg titleId="cols.nm" alt="이름" /></th>
									<th width="18%" class="head_ct"><u:msg titleId="wb.cols.emplTyp" alt="임직원구분" /></th>
									<th class="head_ct"><u:msg titleId="cols.comp" alt="회사" />/<u:msg titleId="cols.dept" alt="부서" /></th>
									<th width="17%" class="head_ct"><u:msg titleId="cols.email" alt="이메일" /></th>
								</tr>
								<c:if test="${!empty wcPromGuestDVoList}">
									<c:forEach var="list" items="${wcPromGuestDVoList}" varStatus="status">
										<u:set test="${status.last}" var="trDisp" value="display:none" />
										<u:set test="${status.last}" var="trId" value="hiddenTr" elseValue="tr${list.guestEmplYn }_${list.guestUid}" />
										<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" id="${trId}" style="${trDisp}">
											<td width="32" class="bodybg_ct">
												<input type="checkbox" value="${list.guestUid}" data-emplTypCd="${list.guestEmplYn }" />
												<input type="hidden" name="schdlId" value="${list.schdlId}" />
												<input type="hidden" name="guestUid" value="${list.guestUid}" />
												<input type="hidden" name="guestNm" value="${list.guestNm}"/>
												<input type="hidden" name="guestDeptNm" value="${list.guestDeptNm}"/>
												<input type="hidden" name="statCd" value="${list.statCd}"/>
												<input type="hidden" name="guestCompNm" value="${list.guestCompNm}"/>
												<input type="hidden" name="email" value="${list.email}"/>
												<input type="hidden" name="guestEmplYn" value="${list.guestEmplYn}"/>
											</td>
											<td class="body_ct" id="emplNm">${list.guestNm}</td>
											<td class="body_ct" id="emplTypNm"><u:msg titleId="${list.guestEmplYn eq 'Y' ? 'cm.option.empl' : 'wc.option.frnd'}" alt="지인"/></td>
											<td class="body_ct" id="emplCompNm"><div class="ellipsis" title="${list.guestCompNm }">${list.guestCompNm}</div></td>
											<td class="body_ct" id="emplEmail">${list.email}</td>
										</tr>
									</c:forEach>
								</c:if>
								</table>
							</div>
						</div>
					</td>
				</tr>
				</tbody>
			</table>
			<div id="repetSetupAll">
				<input id="repetSetup" type="hidden" />
				<input id="repetKnd" type="hidden" />
				<input id="dalySelect" type="hidden" />
				<input id="welySelect" type="hidden" />
				<input id="dow" type="hidden" />
				<input id="molyKnd" type="hidden" />
				<input id="firMolySelect" type="hidden" />
				<input id="firMolyDaySelect" type="hidden" />
				<input id="secMolySelect" type="hidden" />
				<input id="secMolyWeekSelect" type="hidden" />
				<input id="secMolyWeekOfDaySelect" type="hidden" />
				<input id="yelyKnd" type="hidden" />
				<input id="firYelySelect" type="hidden" />
				<input id="firYelyDaySelect" type="hidden" />
				<input id="secYelySelect" type="hidden" />
				<input id="secYelyWeekSelect" type="hidden" />
				<input id="secYelyWeekOfDaySelect" type="hidden" />
				<input id="repetchoiDt" type="hidden" />
				<input id="repetcmltDt" type="hidden" />
			</div>
		</td>
	</tr>
	</u:listArea>
	
	<div id="contArea" class="listarea" style="width:100%; height:${empty namoEditorEnable ? 184 : 306}px; padding-top:2px"></div>
	<u:editor id="cont" width="100%" height="${empty namoEditorEnable ? 180 : 300}px" module="wc" areaId="contArea" value="${ctSchdlBVo.cont}" namoToolbar="wcPop" />
	
	<u:listArea>
	<tr>
		<td>
			<u:files id="ctfiles" fileVoList="${fileVoList}" module="wc" mode="set" exts="${exts }" extsTyp="${extsTyp }"/>
		</td>
	</tr>
	</u:listArea>

	<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:formSubit();" alt="저장"/>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
	</u:buttonArea>

	</form>
</div>
