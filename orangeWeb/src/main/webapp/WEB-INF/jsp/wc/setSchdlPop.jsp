<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="queryParamUserUid" test="${!empty param.paramUserUid}" value="&paramUserUid=${param.paramUserUid }" elseValue=""/>
<u:set var="agntParam" test="${!empty param.paramUserUid}" value="&schBcRegrUid=${param.paramUserUid }" elseValue=""/>

<script type="text/javascript">
<!--<% // [공개여부클릭]  %>
function chkOpenGradCd(obj){
	if(obj.value=='4'){
		$('#deptBtnArea a').show();
	}else{
		deptSelectDel();
		$('#deptBtnArea a').hide();
	}		
}<% // [버튼클릭] - 부서삭제  %>
function deptSelectDel(obj){
	if(obj===undefined) $('#deptSelectArea').html('');
	else $(obj).closest('div.ubox').remove();
};<% // [버튼클릭] - 부서추가  %>
function addDept(){
	var data=[];
	$.each($('#deptSelectArea input[name="orgId"]'), function(){
		if($(this).val()!='')
			data.push({userUid:$(this).val()});
	});
	parent.searchOrgPop({data:data, multi:true, withSub:false, mode:'search'}, function(arr){
		if(arr!=null)
			setRowDept(arr);
	});
};<% // 부서추가  %>
function setRowDept(arr){	
	var buffer=[];
	var idVaList=getDeptList();
	arr.each(function(index, orgVo){
		if($.inArray(orgVo.orgId, idVaList)!=-1)
			return true;
		buffer.push(createUbox(orgVo.orgId, orgVo.rescNm, false));
	});
	if(buffer.length>0){
		$('#deptSelectArea').append(buffer.join(''));
	}
}<% // 사용자 목록 생성  %>
function createUbox(idVa, orgNm, isGrp){
	var buffer=[];
	buffer.push('<div class="ubox'+(isGrp ? ' grpList' : '')+'"><dl>');
	buffer.push('<dd class="title">');
	buffer.push(orgNm);
	buffer.push('<input type="hidden" name="orgId" value="'+idVa+'"/>');
	buffer.push('</dd>');
	buffer.push('<dd class="btn"><a class="delete" onclick="deptSelectDel(this);" href="javascript:void(0);"><span>delete</span></a></dd>');
	buffer.push('</dl></div>');
	return buffer.join('');
}<% // 부서 목록  %>
function getDeptList(){
	var idVaList=[];
	$('#deptSelectArea input[name="orgId"]').each(function(){
		idVaList.push($(this).val());
	});
	return idVaList;
}
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
	var $input = $('#setRegForm input[id="guest"]');
	$input.val('');
	var disabled = false;
	if($(obj).val()=='G') disabled = true;
	setDisabled($input, disabled);
};

//참석자 추가
function guestAdd(){
	var guestTyp = $('#setRegForm input:radio[name="guestTyp"]:checked').val();
	if(guestTyp == 'N'){
		if($('#guest').val()!=''){
			callAjax('./srchUserAjx.do?menuId=${menuId}&fncCal=${fncCal}', {srchTyp:'bc',srchName:$('#guest').val(), paramUserUid:'${param.paramUserUid }'}, function(data) {
				if (data.returnString != null) {
					var arr=[];
					arr.push($.parseJSON(data.returnString));
					atndAdd(arr);
					return;	
				}
				dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}&fncCal=${fncCal}${agntParam}&callBack=atndAdd&fncMul=Y&schCat=bcNm&schWord='+encodeURIComponent($('#guest').val()));
			});
		}else{
			dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}&fncCal=${fncCal}${agntParam}&callBack=atndAdd&fncMul=Y&schCat=bcNm&schWord='+encodeURIComponent($('#guest').val()));
		}		
	}else if(guestTyp == 'G'){
		setUserGrpPop();
	}else{
		if($('#guest').val()!=''){
			callAjax('./srchUserAjx.do?menuId=${menuId}&fncCal=${fncCal}', {srchTyp:'user',srchName:$('#guest').val()}, function(data) {
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
	/* $view.find("tbody:first input[type='checkbox']").each(function(){
		obj = getParentTag(this, 'tr');
		id = $(obj).attr('id');
		if(id!='headerTr' && id!='hiddenTr' && $(this).attr('data-emplTypCd') != null && $(this).attr('data-emplTypCd') == 'Y' ) {
			data.push({userUid:$(this).val()});
		};
	}); */
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
	$('#setRegForm').find('#schdlStrtHour, #schdlStrtMinute, #schdlEndHour, #schdlEndMinute').each(function(){
		setDisabled($(this), obj.checked);
	});
};

//권한을 가지고 있는 그룹 목록 건수 
function isGrpSize(){
	return $('#setRegForm #grpId option').size();
};

//일정대상 select
function fnTargetChange(obj){
	if(obj.value =="2") {if(isGrpSize() > 1) setDisabled($('#setRegForm #grpId'), false); else { alertMsg("wc.msg.empty.grpList");<%//선택할 그룹이 없습니다.%> obj.value = "1"; setDisabled($('#setRegForm #grpId'), true);}}
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
<% // 시간 체크 %>
function chkFromDate(){
	var alldayYn=$('#setRegForm input[name="alldayYn"]').is(':checked');
	var startDt=$("#schdlStrtYmd").val();
	var endDt=$("#schdlEndYmd").val();
	if(!alldayYn && startDt==endDt){
		if(Number($("#schdlStrtHour").val()+''+$("#schdlStrtMinute").val())>=Number($("#schdlEndHour").val()+''+$("#schdlEndMinute").val())){
			alert("<u:msg titleId="wc.msg.chkTime" alt="시작시간이 종료시간 보다 클수는 없습니다."/>");
			return true;
		}
	}
	return false;
}

function formSubit(){
	var schdlKndCd=$('#setRegForm #schdlKndCd').val()
	if(schdlKndCd!=undefined && schdlKndCd=='2' && $('#setRegForm #grpId').val()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [그룹일정] %>
		alertMsg("cm.input.check.mandatory", ["#wc.option.grpSchdl"]);
		$("#setCensrForm #grpId").focus();
		return;
	}
	if(validator.validate('setRegForm')){
		if(chkFromDate()) return; 
		
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
			if('${wcSchdlBVo.schdlId}' != ''){
				transPage = "transSchdlMod";
			}

			$form.attr('method','post');
			$form.attr('action','./'+transPage+'.do?menuId=${menuId}&fncCal=${fncCal}');
			$form.attr('enctype','multipart/form-data');
			$form.attr('target','dataframe');
			editor("cont").prepare();
			saveFileToForm('wcfiles', $form[0], null);
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
		var url = './setRepetPop.do?${paramsForList}';
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
	dialog.open('viewEmptyTimeGuestPop', '<u:msg titleId="wc.btn.freeTmCnfm" alt="빈시간확인"/>','./viewEmptyTimeGuestPop.do?${paramsForList}&scdlStartDt='+$("#schdlStrtYmd").val()+'&scdlEndDt='+$("#schdlEndYmd").val()+'&guestUids='+guestUidsStr+'&guestNms='+guestNmStr);
}

<%// [버튼] 사용자그룹관리 %>
function setUserGrpPop(){
	var url = './findUserGrpPop.do?menuId=${menuId}&multi=Y&fncCal=${fncCal}${queryParamUserUid}';
	dialog.open('setUserGrpDialog', '<u:msg titleId="wc.term.userGroup" alt="사용자그룹" />', url);
}
<%// [버튼] 사용자그룹 세팅 %>
function setUserGrpList(arr){
	addCols(arr, 'Y');
}

<%// [버튼] 이메일 발송 - 참석자 전체 선택 %>
function chkEmailSend(obj){
	var checkHeader=$('#guestListArea input[id="checkHeader"]');
	if($(obj).is(':checked') && !checkHeader.is(':checked')){
		checkHeader.trigger('click');
	}
}
<%// [체크박스] 이메일 발송여부 변경 %>
function chnGuestChk(obj){
	if($(obj).is(':checked')){
		$(obj).closest('tr').find('input[name="emailYn"]').val('Y');
	}else{
		$(obj).closest('tr').find('input[name="emailYn"]').val('N');
	}
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


<div style="width:743px">

	<form id="setRegForm"  method="post" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<c:if test="${!empty wcSchdlBVo.schdlId }">
		<input id="schdlId" name="schdlId" type="hidden" value="${wcSchdlBVo.schdlId }"/>
	</c:if>
	<input id="scdlStartDt" name="scdlStartDt" type="hidden" />
	<input id="scdlEndDt" name="scdlEndDt" type="hidden" />
	
	<u:input type="hidden" id="listPage" value="./${listPage }.do?${paramsForList}" />
	
	<!-- <input id="guestUids" name="guestUids" type="hidden" />
	<input id="rescNm" name="rescNm" type="hidden"/> -->
	
	<c:if test="${empty wcSchdlBVo.schdlId }">
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
				<u:option value="${list.catId}" title="${list.catNm }" checkValue="${wcSchdlBVo.schdlTypCd}"/>
			</c:forEach>
		</select>
		</td>
		<td class="head_lt"><u:mandatory /><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></td>
		<td class="${param.fncCal eq 'my' || param.fncCal eq 'open' ? '' : 'body_lt' }">
			<c:choose>
				<c:when test="${param.fncCal eq 'my' || param.fncCal eq 'open' || wcSchdlBVo.schdlKndCd eq '1' || wcSchdlBVo.schdlKndCd eq '2'}">
					<c:choose>
						<c:when test="${listPage eq 'listAllSchdl' }">
							<c:choose>
								<c:when test="${wcSchdlBVo.schdlKndCd eq '2' }"><u:msg titleId="wc.jsp.listPsnSchdl.grp.title" alt="그룹일정"/></c:when>
								<c:otherwise><u:msg titleId="wc.jsp.listPsnSchdl.psn.title" alt="개인일정"/></c:otherwise>
							</c:choose>
							<u:input type="hidden" name="schdlKndCd"  value="${wcSchdlBVo.schdlKndCd }"/>
						</c:when>
						<c:otherwise>
							<select id="schdlKndCd"  name="schdlKndCd" onchange="fnTargetChange(this);">
								<c:forEach  var="list" items="${schdlKndCdList}" varStatus="status">
									<c:if test="${list[2] eq 'Y'}">
										<u:option value="${list[0]}" title="${list[1] }" checkValue="${wcSchdlBVo.schdlKndCd}"/>
									</c:if>
								</c:forEach>
							</select>
							<u:set var="myGrpDisabled" test="${wcSchdlBVo.schdlKndCd eq '2' }" value="" elseValue="disabled='disabled'"/>
							<select id="grpId" name="grpId" ${myGrpDisabled } style="width:100px;">
								<u:option value="" titleId="cm.option.all" alt="전체선택"/>
								<c:forEach  var="grpItem" items="${wcSchdlGroupBVoList}" varStatus="status">	
									<u:option value="${grpItem.schdlGrpId}" title="${grpItem.grpNm }" checkValue="${wcSchdlBVo.grpId}"/>
								</c:forEach>
							</select>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:when test="${param.fncCal eq 'dept' || wcSchdlBVo.schdlKndCd eq '3'}"><u:msg titleId="wc.jsp.listPsnSchdl.dept.title" alt="부서일정"/><u:input type="hidden" name="schdlKndCd"  value='3'/></c:when>
				<c:when test="${param.fncCal eq 'comp' || wcSchdlBVo.schdlKndCd eq '4'}"><u:msg titleId="wc.jsp.listPsnSchdl.comp.title" alt="회사일정"/><u:input type="hidden" name="schdlKndCd"  value='4'/></c:when>
				<c:otherwise></c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="head_lt">
			<u:msg titleId="cols.publYn" alt="공개여부" />
		</td>
		<td class="body_lt">
			<c:if test="${param.fncCal eq 'comp' || wcSchdlBVo.schdlKndCd eq '4'}"><u:msg titleId="cm.option.publ" alt="공개"/><u:input type="hidden" name="openGradCd"  value="1" /></c:if>
			<c:if test="${param.fncCal ne 'comp' && wcSchdlBVo.schdlKndCd ne '4'}">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td><u:radio name="openGradCd" value="1" titleId="cm.option.publ" alt="공개" checkValue="${wcSchdlBVo.openGradCd }" onclick="chkOpenGradCd(this);"/></td>
					<c:if test="${param.fncCal ne 'dept' && wcSchdlBVo.schdlKndCd ne '3' && param.fncCal ne 'comp' && wcSchdlBVo.schdlKndCd ne '4'}">
						<td><u:radio name="openGradCd" value="2" titleId="cm.option.apntPubl" alt="지정인공개" checkValue="${wcSchdlBVo.openGradCd }" onclick="chkOpenGradCd(this);"/></td>
					</c:if>
					<td><u:radio name="openGradCd" value="3" titleId="cm.option.priv" alt="비공개" checkValue="${wcSchdlBVo.openGradCd }" checked="${empty wcSchdlBVo.openGradCd }" onclick="chkOpenGradCd(this);"/></td>
					<c:if test="${param.fncCal eq 'dept' }"><td><u:radio name="openGradCd" value="4" titleId="wc.cols.sel.dept" alt="부서선택" checkValue="${wcSchdlBVo.openGradCd }" onclick="chkOpenGradCd(this);"/></td></c:if>
				</tr>
				</tbody>
			</table>
			</c:if>
		</td>
		<td class="head_lt"><u:msg titleId="cols.prio" alt="우선순위" /></td>
		<td>
			<select name="workPrioOrdr" id="workPrioOrdr">
				<c:forEach var="orderNum" begin="1" end="5" >
					<u:option value="${orderNum }" title="${orderNum }" checkValue="${wcSchdlBVo.workPrioOrdr }"/>
				</c:forEach>
			</select>
		</td>
	</tr>
	<c:if test="${param.fncCal eq 'dept' }">
	<tr>
		<td class="head_lt"><u:msg titleId="wc.cols.sel.dept" alt="부서선택" /></td>
		<td class="body_lt" colspan="3"><div id="deptBtnArea" style="display:block;width:100%;height:20px;"><u:buttonS href="javascript:addDept();" titleId="cm.btn.add" alt="사용자추가" auth="W" style="display:${!empty wcSchdlBVo.openGradCd && wcSchdlBVo.openGradCd eq '4' ? 'inline-block' : 'none'}" 
	/><u:buttonS href="javascript:deptSelectDel();" titleId="cm.btn.allDel" alt="전체삭제" auth="W" style="display:${!empty wcSchdlBVo.openGradCd && wcSchdlBVo.openGradCd eq '4' ? 'inline-block' : 'none'}" 
	/></div><div id="deptSelectArea" style="min-height:40px;overflow-y:auto;height:40px;"><c:forEach 
		var="wcSchdlDeptRVo" items="${wcSchdlDeptRVoList }" varStatus="status">
		<div class="ubox"><dl><dd 
		class="title">${wcSchdlDeptRVo.orgNm }<input type="hidden" name="orgId" value="${wcSchdlDeptRVo.orgId }"
		/></dd><dd class="btn"><a class="delete" onclick="deptSelectDel(this);" href="javascript:void(0);"><span>delete</span></a></dd></dl></div>
		</c:forEach></div></td>
	</tr></c:if>
	</u:listArea>
	<u:blank/>
	<% // 폼 필드 %>
	<u:listArea noBottomBlank="true">
	<tr>
		<td width="18%" class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
		<td width="82%"><u:input id="subj" value="${wcSchdlBVo.subj }" titleId="cols.subj" style="width:97%;" mandatory="Y" maxByte="120"/></td>
	</tr>

	<tr>
		<td class="head_lt"><u:msg titleId="cols.loc" alt="장소" /></td>
		<td>
			<u:input id="locNm" value="${wcSchdlBVo.locNm}" titleId="cols.loc" style="width:97%;" maxByte="60"/>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="wc.cols.schdlPriod" alt="기간"/></td>
		<td>
			<fmt:parseDate var="dateStartDt" value="${wcSchdlBVo.schdlStartDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate var="convStartDt" value="${dateStartDt}" pattern="yyyy-MM-dd"/>
			<fmt:formatDate var="schdlStrtHour" value="${dateStartDt}" pattern="HH"/>
			<fmt:formatDate var="schdlStrtMinute" value="${dateStartDt}" pattern="mm"/>
			<fmt:parseDate var="dateEndDt" value="${wcSchdlBVo.schdlEndDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate var="convEndDt" value="${dateEndDt}" pattern="yyyy-MM-dd"/>
			<fmt:formatDate var="schdlEndHour" value="${dateEndDt}" pattern="HH"/>
			<fmt:formatDate var="schdlEndMinute" value="${dateEndDt}" pattern="mm"/>
			
			<u:set var="alldayDisabled" test="${wcSchdlBVo.alldayYn eq 'Y' }" value="disabled='disabled'" elseValue=""/>	
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td width="80px"><u:calendar id="schdlStrtYmd" option="{end:'schdlEndYmd'}" titleId="cols.choiDt" alt="시작일시" mandatory="Y" value="${convStartDt }"/></td>
					<td>
						<select name="schdlStrtHour" id="schdlStrtHour" style="width:40px;" ${alldayDisabled }>
						<c:forEach var="strtHour" begin="0" end="23">
							<u:set var="timeVal" test="${strtHour < 10 }" value="0${strtHour }" elseValue="${strtHour }"/>
							<u:option value="${timeVal}" title="${timeVal}" checkValue="${schdlStrtHour }" />
						</c:forEach>
						</select>
					</td>
					<td>
						<select name="schdlStrtMinute" id="schdlStrtMinute" style="width:40px;" ${alldayDisabled }>
						<c:forEach var="strtMinute" begin="0" end="59">
							<u:set var="timeVal" test="${strtMinute < 10 }" value="0${strtMinute }" elseValue="${strtMinute }"/>
							<u:option value="${timeVal}" title="${timeVal}" checkValue="${schdlStrtMinute }" />
						</c:forEach>
						</select>
					</td>
					<td class="body_lt">~</td>
					<td width="80px"><u:calendar id="schdlEndYmd" option="{start:'schdlStrtYmd'}" titleId="cols.cmltDt" alt="완료일시" mandatory="Y" value="${convEndDt }"/></td>
					<td>
						<select name="schdlEndHour" id="schdlEndHour" style="width:40px;" ${alldayDisabled }>
						<c:forEach var="endHour" begin="0" end="23">
							<u:set var="timeVal" test="${endHour < 10 }" value="0${endHour }" elseValue="${endHour }"/>
							<u:option value="${timeVal }" title="${timeVal }" checkValue="${schdlEndHour }" />
						</c:forEach>
						</select>
					</td>
					<td>
						<select name="schdlEndMinute" id="schdlEndMinute" style="width:40px;" ${alldayDisabled }>
						<c:forEach var="endMinute" begin="0" end="59">
							<u:set var="timeVal" test="${endMinute < 10 }" value="0${endMinute }" elseValue="${endMinute }"/>
							<u:option value="${timeVal }" title="${timeVal }" checkValue="${schdlEndMinute }" />
						</c:forEach>
						</select>
					</td>
					<td><u:checkbox name="alldayYn" value="Y" titleId="wc.cols.alldayYn" alt="종일여부" onclick="fnAllday(this);" checkValue="${wcSchdlBVo.alldayYn }"/></td>
					<td><u:checkbox name="solaLunaYn" value="N" titleId="wc.option.luna" alt="음력" checkValue="${wcSchdlBVo.solaLunaYn }"/></td>
				</tbody>
			</table>
		</td>
	</tr>
	<c:if test="${!empty scdPidCount && scdPidCount > 1 && wcSchdlBVo.repetYn eq 'Y'}">
		<tr>
		<td class="head_lt"><u:msg titleId="wc.cols.allSetting" alt="전체설정" /></td>
		<td class="bodybg_lt"><table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><u:radio name="fileSetting" value="indiv" titleId="wc.option.indivApply" alt="개별반영" checked="true" /></td>
				<td><u:radio name="fileSetting" value="all" titleId="wc.option.allApply" alt="전체반영" /></td>
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
				<u:radio name="guestTyp" value="G" titleId="wc.term.userGroup" alt="사용자그룹" inputClass="bodybg_lt" onclick="guestChange(this);"/>
			<td><u:input id="guest" value="" titleId="cols.guest" style="width: 100px;" onkeydown="if (event.keyCode == 13){guestAdd();return false;}"/></td>
			<td><u:buttonS href="" titleId="cm.btn.add" alt="추가" onclick="guestAdd();"/><u:buttonS href="javascript:removeCols();" titleId="cm.btn.del" alt="삭제" /></td>
			<td><u:buttonS href="javascript:emptyTimeGuest()" titleId="wc.btn.freeTmCnfm" alt="빈시간확인" /></td>
			<c:if test="${mailEnable == 'Y'}">
				<u:checkbox id="emailSendYn" name="emailSendYn" value="Y" titleId="cols.emailSend" alt="이메일발송" inputClass="bodybg_lt" onclick="chkEmailSend(this);"/>
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
							<div id="guestListArea" class="listarea" style="width:98%; padding:5px;">
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
												<input type="checkbox" value="${list.guestUid}" data-emplTypCd="${list.guestEmplYn }" onchange="chnGuestChk(this);"/>
												<input type="hidden" name="schdlId" value="${list.schdlId}" />
												<input type="hidden" name="guestUid" value="${list.guestUid}" />
												<input type="hidden" name="guestNm" value="${list.guestNm}"/>
												<input type="hidden" name="guestDeptNm" value="${list.guestDeptNm}"/>
												<input type="hidden" name="statCd" value="${list.statCd}"/>
												<input type="hidden" name="guestCompNm" value="${list.guestCompNm}"/>
												<input type="hidden" name="email" value="${list.email}"/>
												<input type="hidden" name="guestEmplYn" value="${list.guestEmplYn}"/>
												<input type="hidden" name="emailYn" value="N"/>
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

	<div id="contArea" class="listarea" style="width:100%; height:${empty namoEditorEnable ? 184 : 306}px; padding-top:2px"></div><%
	if(request.getAttribute("namoEditorEnable")==null){
		com.innobiz.orange.web.wc.vo.WcSchdlBVo wcSchdlBVo = (com.innobiz.orange.web.wc.vo.WcSchdlBVo)request.getAttribute("wcSchdlBVo");
		if(wcSchdlBVo != null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(wcSchdlBVo.getCont());
			request.setAttribute("_bodyHtml", _bodyHtml);
		}
	} else {
		com.innobiz.orange.web.wc.vo.WcSchdlBVo wcSchdlBVo = (com.innobiz.orange.web.wc.vo.WcSchdlBVo)request.getAttribute("wcSchdlBVo");
		request.setAttribute("_bodyHtml", wcSchdlBVo.getCont());
	}
%>
	<u:editor id="cont" width="100%" height="${empty namoEditorEnable ? 180 : 300}px" module="wc" areaId="contArea" value="${_bodyHtml}" namoToolbar="wcPop" />
	
	<u:listArea>
	<tr>
		<td>
			<u:files id="wcfiles" fileVoList="${fileVoList}" module="wc" mode="set" urlParam="fncCal=${fncCal}" exts="${exts }" extsTyp="${extsTyp }" height="55"/>
		</td>
	</tr>
	</u:listArea>

	<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:formSubit();" alt="저장" auth="W"/>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
	</u:buttonArea>

	</form>
</div>
