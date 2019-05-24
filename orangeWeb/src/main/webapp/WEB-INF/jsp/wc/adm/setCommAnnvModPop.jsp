<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--

$(function() {
	var schdlList = viewSearchMyScd();
	$("#schdlId").val(schdlList.schdlId);
	$("#annvNm").val(schdlList.subj);
	$("#loc").val(schdlList.loc);
	$("#setAnnvChoiDt").val(schdlList.startYmd);
	$("#setAnnvCmltDt").val(schdlList.endYmd);
	
	//기간일부터 며칠 간		
	$("#selectStartYmd").val(betweenCalDay(schdlList.startYmd, schdlList.endYmd)).attr("selected", "selected");
	if(schdlList.holiYn == 'Y'){
		$('input:radio[name=holiYn]:input[value=Y]').attr("checked", true);
	}else if(schdlList.holiYn == 'N'){
		$('input:radio[name=holiYn]:input[value=N]').attr("checked", true);
	}
	
	if(schdlList.solaLunaYn == 'Y'){
		$('input:radio[name=solaYn]:input[value=Y]').attr("checked", true);
	}else if(schdlList.solaLunaYn == 'N'){
		$('input:radio[name=solaYn]:input[value=N]').attr("checked", true);
	}
	
	//바로 그려주기위해서 function이 document보다 먼저 html이 그려진다.
	//그래서 mod에서는 function에서 date를 먼저 그려야한다.
	//공통기념일은 반복시작일 반복종료일 10년 주기 만들기
	/* var d = new Date();
	var currentYear = d.getFullYear();
	var afterYear = currentYear + 10;
	
	for(var i=currentYear; i < afterYear; i++){
		$("#startYear").append("<option value=" + i + ">" + i + "</option>");
		$("#endYear").append("<option value=" + i + ">" + i + "</option>");
	}
	
	
	$("#startYear").val("${repetStartY}").attr("selected", "selected");
	$("#endYear").val("${repetEndY}").attr("selected", "selected"); */
	

});

//DIV 팝업
var custumDialog = {
	data:{ clientX:null, clientY:null, objectTop:null, objectLeft:null, documentHeight:null, documentWidth:null, instance:null, zindex:1000 },
	noFocus:false,
	openYn:{},
	closeHandler:{},
	onClose:function(id, handler){ this.closeHandler[id] = handler; },
	open:function(id, title, url, param, width, height, focusId, failHandler){
		var $dialog = $('#'+id);
		if($dialog.length==0){
			$(".styleThese").first().append('<div class="layerpoparea" id="'+id+'" style="position:absolute; z-index:'+(++dialog.data.zindex)+'; visibility:visible; display:none;" ></div>');
			$dialog = $('#'+id);
			
			var buff=[];
			buff.push('<div id="dialogTitle" class="lypoptit" style="cursor:move;">');
			buff.push('	<dl>');
			buff.push('	<dd id="dialogTitleTxt" class="title">'+title+'</dd>');
			buff.push('	<dd class="btn"><a href="javascript:void(0);" id="cloasBtn" onclick="setRepetPop.hide();" class="close"><span>close</span></a></dd>');
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
			
		} else {
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
			} else {
				if(focusId==null){
					var $focus = $dialog.find("input:visible, select:visible, textarea:visible").not("[readonly='readonly']").first();
					if($focus.length>0) $focus.focus();
					else {
						$dialog.find("#cloasBtn:first").focus();
					}
				} else {
					$dialog.find("#"+focusId).focus();
				}
			}
		}
		this.openYn[id] = 'Y';
	},
	resize:function(id, width, height, opened){
		var $dialog = $('#'+id);
		var $sizeRefer = $dialog.find('#dialogBody').find("div:first");
		
		var dialogWidth = width==null ? Math.max($sizeRefer.width()+30, 200) : width;
		var dialogHeight = height==null ? Math.max($sizeRefer.height()+55, 120) : height;
		$dialog.css('width',dialogWidth+'px');
		$dialog.css('height',dialogHeight+'px');
		
		if(opened==null){}
		else if(opened==true){
			var left = parseInt($dialog.css("left")) + 2000;
			$dialog.css("left", left+"px");
		} else {
			var dialogTop = Math.max(parseInt(($(window).height() - dialogHeight)/2 + $(window).scrollTop()),$(window).scrollTop());
			var dialogLeft = Math.max(parseInt(($(window).width() - dialogWidth)/2 + $(window).scrollLeft()),$(window).scrollLeft());
			$dialog.css("top",dialogTop);
			$dialog.css("left",dialogLeft);
		}
	},
	close:function(obj){
		var id=null, handler;
		if (typeof obj == 'string') {
			var $pop = $('#'+obj);
			$pop.find('#cloasBtn').focus();
			$pop.hide();
			$pop.find('.bodyarea').empty();
			$pop.css('left','0px');
			id = obj;
		} else {
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
	},
	forward:function(id){
		var $dialog = $('#'+id);
		if($dialog.length>0) $dialog.css('z-index', ++dialog.data.zindex);
	},
	isOpen:function(id){
		return 'Y' == this.openYn[id];
	},
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
	if($('#natCd option').size()==0){
		// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다. - wc.btn.set.nat=국가설정 %>
		alertMsg("cm.msg.noSelectedItem", ["#wc.btn.set.nat"]);
		return;
	}
	if(validator.validate('setAnnvForm')){
		
		
		var $form = $('#setAnnvForm');	
		
		if(typeof $('#agnt').val()!="undefined"){		
			$form.append("<input type='hidden' id='agnt' name='agnt' value='"+$('#agnt').val()+"'>");
		}
		
		$("#firYelySelect").val(eval($("#setAnnvChoiDt").val().substr(5,2)));
		$("#firYelyDaySelect").val(eval($("#setAnnvChoiDt").val().substr(8,2)));
		$("#repetchoiDt").val( $("select[name=startYear]").val()+"-01-01");
		$("#repetcmltDt").val( $("select[name=endYear]").val()+"-12-31");
		
		var startY = $("#startYear").val();
		var endY = $("#endYear").val();
		if( startY > endY){
			alert("<u:msg titleId="wc.msg.annv.setDt" alt="반복시작일시가 종료일시보다 클 수 없습니다."/>");
			return false;
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
		//editor("editor1").prepare();
		
		$form.attr('method','post');
		$form.attr('action','./transSetCommAnnvPopUpdate.do?menuId=${menuId}');
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','dataframe');
		$form[0].submit();		
	
		dialog.close(this);
	}	
}


//참석자 삭제
function delGuest(){
	
	var selectOptionVal=$("#guests option:selected").val();
	var selectOptionText=$("#select_box option:selected").text();	
	var index = $("#guests option").index($("#guests option:selected"));
	$("#guests option:selected").remove();
	
	var userUidIndex=0;
	var idx=0;
	var isEmp="";
	$("#guestUid").find("#userUid").each(function(){
		if($(this).val()==selectOptionVal){
			isEmp=true;
			userUidIndex=idx;
		}
		idx++;
	});
	
	idx=0;
	$("#guestFriUid").find("#friUid").each(function(){
		if($(this).val()==selectOptionVal){
			isEmp=false;
			userUidIndex=idx;
		}
		idx++;
	});
	
	if(isEmp){
		$("#guestUid").find("#userUid:eq("+userUidIndex+")").remove();
		$("#guestExtnEmail").find("#userUid:eq("+userUidIndex+")").remove();
		$("#guestEmail").find("#email:eq("+userUidIndex+")").remove();
		$("#guestDeptRescNm").find("#deptRescNm:eq("+userUidIndex+")").remove();
		$("#guestPositNm").find("#positNm:eq("+userUidIndex+")").remove();
		$("#guestRescNm").find("#rescNm:eq("+userUidIndex+")").remove();
		$("#guestRescId").find("#rescId:eq("+userUidIndex+")").remove();
		
	}else{
		$("#guestFriUid").find("#friUid:eq("+userUidIndex+")").remove();
		$("#guestFriNms").find("#friNm:eq("+userUidIndex+")").remove();
		$("#guestCompNms").find("#friCompNm:eq("+userUidIndex+")").remove();
	}
}

var setRepetPop=null;
function repetSelect(mode){
	if(setRepetPop==null){
		$("#setRepetPop").remove();
		custumDialog.open('setRepetPop','<u:msg titleId="wc.btn.repetSetup" alt="반복설정"/>','../setRepetPop.do?menuId=${menuId}&');
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
	}else{
		setRepetPop.show();
	}
		
	
}

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
			$("#setRegForm input[name=compNm]").val(obj.compNm);
		});		
	}
	dialog.close('findBcPop');
};

function addUser(mode){
	var guestTyp = $('#setAnnvForm input:radio[name="guestTyp"]:checked').val();	
	if(guestTyp=="EMPL"){		
		openMuiltiUser(mode);
	}else{
		dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','../wb/findBcPop.do?menuId=${menuId}&callBack=atndAdd');
	}
}

//명함 추가
function atndAdd(objArr){	
	addCols(objArr,"FRND");
};

function addCols(arr , emplTypCd){	
	if(arr==null) return;
	
	var friUidBuffer = [];
	var friNmsBuffer = [];
	var friCompNmsBuffer = [];
	var buffer = [];
	arr.each(function(index, obj){
		friUidBuffer.push("<input id='friUid' name='friUid' type='hidden' value='"+obj.bcId+"' />\n");
		friNmsBuffer.push("<input id='friNm' name='friNm' type='hidden' value='"+obj.bcNm+"' />\n");
		friCompNmsBuffer.push("<input id='friCompNm' name='rescNm' type='hidden' value='"+obj.compNm+"' />\n");	
		buffer.push("<option value='"+obj.bcId+"'>"+obj.bcNm+"/"+obj.compNm+"/지인(명함)</option>");
	});	
	var friUids=$("#guestFriUid");
	var friNms=$("#guestFriNms");
	var friCompNms=$("#guestCompNms");
	
	friUids.append(friUidBuffer.join(''));
	friNms.append(friNmsBuffer.join(''));
	friCompNms.append(friCompNmsBuffer.join(''));
	$("#guests").append(buffer.join(''));
};

//여러명의 사용자 선택
function openMuiltiUser(mode){
	var $view = $("#guestUid"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	
	$view.find("#userUid").each(function(){
		data.push({userUid:$(this).val()});
	});
	
	<%// option : data, multi, titleId %>
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			var userUids=[];
			var rescIds=[];
			var rescNms=[];
			var positNms=[];
			var deptRescNms=[];
			var emails=[];
			var extnEmails=[];
			
			arr.each(function(index, userVo){	
				buffer.push("<option value='"+userVo.userUid+"'>"+userVo.rescNm+"/"+userVo.deptRescNm+"/"+userVo.positNm+"</option>");
				userUids.push("<input id='userUid' name='userUid' type='hidden' value='"+userVo.userUid+"' />\n");
				rescIds.push("<input id='rescId' name='rescId' type='hidden' value='"+userVo.rescId+"' />\n");
				rescNms.push("<input id='rescNm' name='rescNm' type='hidden' value='"+userVo.rescNm+"' />\n");
				positNms.push("<input id='positNm' name='positNm' type='hidden' value='"+userVo.positNm+"' />\n");
				deptRescNms.push("<input id='deptRescNm' name='deptRescNm' type='hidden' value='"+userVo.deptRescNm+"' />\n");
				emails.push("<input id='email' name='email' type='hidden' value='"+userVo.email+"' />\n");
				extnEmails.push("<input id='extnEmail' name='extnEmail' type='hidden' value='"+userVo.extnEmail+"' />\n");
				
			});
			
			jQuery('#guests option').each(function(i){
				var optionDesc=$(this).val();
				var desc=optionDesc.split("/");
				if(desc[2]!="지인(명함)"){
					$(this).remove();
				}
			});
			
			$("#guestExtnEmail").html(extnEmails.join(''));	
			$("#guestEmail").html(emails.join(''));			
			$("#guestDeptRescNm").html(deptRescNms.join(''));			
			$("#guestPositNm").html(positNms.join(''));			
			$("#guestRescNm").html(rescNms.join(''));			
			$("#guestRescId").html(rescIds.join(''));			
			$view.html(userUids.join(''));			
			$("#guests").append(buffer.join(''));
		}
	});
	
}

function compareDate(setAnnvChoiDt){
	$("#setAnnvCmltDt").val(addDay(setAnnvChoiDt, $("select[name=selectStartYmd]").val()));
}

function selectedSevenDay(){
	
	$("#setAnnvCmltDt").val(addDay($("#setAnnvChoiDt").val(), $("select[name=selectStartYmd]").val()));
	
}

function add_date(i) // 매서드가 될 함수 구현
{
   var currentDate; // 계산된 날
   currentDate = this.getDate() + i*1; // 현재 날짜에 더해(빼)줄 날짜를 계산
   this.setDate(currentDate); // 계산된 날짜로 다시 세팅
}

function addDay(ymd, v_day){
    var yyyy = eval(ymd.substr(0,4));
    var mm = eval(ymd.substr(5,2) + "- 1") ;
    var dd = ymd.substr(8,2);
    var dt3 = new Date(yyyy, mm, eval(dd + '+' + (v_day-1)));
    yyyy = dt3.getFullYear();
    mm = (dt3.getMonth()+1)<10? "0" + (dt3.getMonth()+1) : (dt3.getMonth()+1) ;
    dd = dt3.getDate()<10 ? "0" + dt3.getDate() : dt3.getDate();
    return  "" + yyyy + "-" + mm + "-" + dd ;
}

$(document).ready(function() {
	if($('#fncCal').val()=='grp'){
		$('#grpRow').show();
	}else{
		$('#grpRow').hide();
	}
	
	//반복시작일 반복종료일 10년 주기 만들기
	var d = new Date();
	var currentYear = d.getFullYear();
	var afterYear = currentYear + 30;
	var strtDftYear = currentYear - 10;
	var repetStartY = '${repetStartY}';
	var repetEndY = '${repetEndY}';
	for(var i=currentYear; i < afterYear; i++){
		if(i==currentYear){
			if(repetStartY < strtDftYear) $("#startYear").append("<option value=" + repetStartY + ">" + repetStartY + "</option>");
			for(var j=0;j<currentYear - strtDftYear;j++){
				$("#startYear").append("<option value=" + (strtDftYear+j) + ">" + (strtDftYear+j) + "</option>");
			}
			if(repetEndY < currentYear) $("#endYear").append("<option value=" + repetEndY + ">" + repetEndY + "</option>");
		}
		$("#startYear").append("<option value=" + i + ">" + i + "</option>");
		$("#endYear").append("<option value=" + i + ">" + i + "</option>");
	}
	
	$("#startYear").val("${repetStartY}").attr("selected", "selected");
	$("#endYear").val("${repetEndY}").attr("selected", "selected");
});
//-->
</script>

<u:set test="${param.fnc == 'mod'}" var="fnc" value="mod" elseValue="reg" />
<u:set test="${fnc == 'mod'}" var="annvNm" value="dddd" elseValue="" />
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
	

<div style="width:700px">
<form id="setAnnvForm"  method="post" >
<u:input type="hidden" id="menuId" value="${menuId}" />
<input id="repetSetup" name="repetSetup" type="hidden" value="Y"/>
<input id="repetKnd" name="repetKnd" type="hidden" value="YELY"/>
<input id="yelyKnd" name="yelyKnd" type="hidden" value="1" />
<input id="firYelySelect" name="firYelySelect" type="hidden" value="1" />
<input id="firYelyDaySelect" name="firYelyDaySelect" type="hidden"  value="1"/>	
<input id="repetchoiDt" name="repetchoiDt" type="hidden" />
<input id="repetcmltDt" name="repetcmltDt" type="hidden" />



<% // 폼 필드 %>
<u:listArea noBottomBlank="true">
	
	<tr>
		<td width="17%" class="head_lt"><u:mandatory /><u:msg titleId="cols.annvNm" alt="기념일명" /></td>
		<td  colspan="3"><table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convert srcId="${wcAnnvDVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${status.first}" var="style" value="width:450px;" elseValue="width:450px; display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.annvNm" value="${rescVa}" style="${style}"
				maxByte="120" validator="changeLangSelector('setAnnvForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setAnnvForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="" />
		</td>
		</tr>
		</table></td>
	</tr>
	<tr id="grpRow">
		<td width="17%" class="head_lt"><u:msg titleId="wc.btn.grpChoi" alt="그룹선택" /></td>
		<td colspan="3">
			<select id="grpId" name="grpId"  style="width: 200px;">
			<c:forEach  var="grpItem" items="${wcSchdlGroupBVoList}" varStatus="status">
				<option value="${grpItem.schdlGrpId }">${grpItem.grpNm }</option>
			</c:forEach>
		
			</select>
		
		</td>
	</tr>
	<!-- 
	<tr>
	<td class="head_lt"><u:msg titleId="cols.loc" alt="장소" /></td>
	<td colspan="3"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
			<td><u:input id="loc" value="${loc}" titleId="cols.loc" style="width: 498px;" /></td>
				<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
			<td><u:buttonS href="javascript:void(alert('${msg}'));" titleId="wc.btn.mapAtt" alt="지도첨부" /></td>
		</tr></tbody></table></td>
	</tr>
	 -->
	<input id="schdlId" name="schdlId" type="hidden" />
	
	<tr>
	<td width="17%" class="head_lt"><u:msg titleId="cols.strtDt" alt="시작일" /></td>
	<td width="33%">
	
		<table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<!--  <td><u:input id="strtYmd" value="${strtYmd}" titleId="cols.ymd" style="width:80px;" className="input_center" /></td> -->
			<td><u:calendar id="setAnnvChoiDt" option="{end:'setAnnvCmltDt'}"  handler="function(date,option){compareDate(date);return true;}"  titleId="cols.choiDt" alt="선택일시" />
			<td class="body_lt"><u:msg titleId="wc.jsp.viewWorkPop.tx01" alt="일부터" /></td>
			<td><select id="selectStartYmd" name = "selectStartYmd" onClick="selectedSevenDay()" onChange="selectedSevenDay()">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				</select></td>
			<td class="body_lt"><u:msg titleId="wc.jsp.viewWorkPop.tx02" alt="일간" /></td>
			</tr>
			</tbody>
		</table>
	</td>
	<td width="17%" class="head_lt"><u:msg titleId="cols.endDt" alt="종료일" /></td>
	<td width="33%"><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<!--  <td><u:input id="endYmd" value="${endYmd}" titleId="cols.ymd" style="width:80px;" className="input_center" /></td>  -->
		<td><u:calendar id="setAnnvCmltDt" option="{start:'setAnnvChoiDt'}" readonly="Y" titleId="cols.cmltDt" alt="완료일시" />
		</tr>
		</tbody></table>
	</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.repetStrtDt" alt="반복시작일시" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
			<td>
				<select id="startYear" name="startYear">
				</select>
			</td>
			<td class="body_lt">
				<u:msg titleId="wc.cols.year" alt="년" />
				(<u:msg titleId="wc.colse.startYear" alt="시작년도" />)
			</td>
		</tr>
		</tbody></table></td>
	<td class="head_lt"><u:msg titleId="cols.repetEndDt" alt="반복종료일시" /></td>
	<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
			<td>
				<select id="endYear" name="endYear">
				</select></td>
			<td class="body_lt">
				<u:msg titleId="wc.cols.year" alt="년" />
				(<u:msg titleId="wc.colse.endYear" alt="종료년도" />)
			</td>
		</tr>
		</tbody></table></td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.holiYn" alt="휴일여부" /></td>
	<td><u:checkArea>
		<u:radio name="holiYn" value="N" alt="평일" titleId="wc.option.wday" />
		<u:radio name="holiYn" value="Y" alt="공휴일" titleId="wc.option.commHoli" />
		</u:checkArea>
	</td>
	
	<td class="head_lt" ><u:msg titleId="cols.solaYn" alt="양력여부" /></td>
	<td colspan="3">
		<u:checkArea >
			<u:radio name="solaYn" value="Y" alt="양력" titleId="wc.option.sola" />
			<u:radio name="solaYn" value="N" alt="음력" titleId="wc.option.luna" />
		</u:checkArea></td>
	</tr>
	<tr>
	<td class="head_lt"><u:msg titleId="wc.btn.set.nat" alt="국가설정" /></td>
	<td colspan="3"><select id="natCd" name="natCd" <u:elemTitle titleId="wc.btn.set.nat" alt="국가설정" />>
		<c:forEach items="${wcNatBVoList}" var="wcNatBVo" varStatus="status">
			<u:option value="${wcNatBVo.cd}" title="${wcNatBVo.rescNm}" checkValue="${wcAnnvDVo.natCd }"/>
		</c:forEach>
	</select></td>
	</tr>


	
</u:listArea>
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="javascript:formSubit();" alt="저장" auth="R" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
