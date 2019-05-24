<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:out value="${wrRezvBVo.rezvStrtDt}" type="date" var="rezvStrtYmd" />
<u:out value="${wrRezvBVo.rezvStrtDt}" type="hm" var="rezvStrtHm" />
<u:out value="${wrRezvBVo.rezvEndDt}" type="date" var="rezvEndYmd" />
<u:out value="${wrRezvBVo.rezvEndDt}" type="hm" var="rezvEndHm" />
<u:secu auth="W" ownerUid="${wrRezvBVo.regrUid}"><u:set test="${true}" var="writeAuth" value="Y"/></u:secu>
<script type="text/javascript">
//<![CDATA[
           
//자원현황 팝업
function listRezvStatSub(schTyp) {
	var url = '/wr/listRezvStatSub.do?menuId=${menuId}&rescKndId='+$("#setRezv #rescKndId").val()+'&rescMngId='+$("#setRezv #rescMngId").val();
	url+="&durCat=fromYmd";
	url+="&durStrtDt="+$('#rezvStrtYmd').val();
	url+="&durEndDt="+$('#rezvEndYmd').val();
	if(schTyp) url+="&schTyp="+schTyp;
	<c:if test="${!empty param.paramCompId }">url+="&paramCompId=${param.paramCompId}";</c:if>
	$m.nav.next(event, url);
};

function setRezvDt() {
	if ($('#rezvStrtYmd').val() != '' ) {
		$('#setRezv #rezvStrtDt').val($('#rezvStrtYmd').val() + ' ' + $('#rezvStrtHm').val());
	} else {
		$('#setRezv #rezvStrtDt').val('');
	}
	if ($('#rezvEndYmd').val() != '' ) {
		$('#setRezv #rezvEndDt').val($('#rezvEndYmd').val() + ' ' + $('#rezvEndHm').val());
	} else {
		$('#setRezv #rezvEndDt').val('');
	}
}    
<% //필수값 체크 %>    
function validateRezvDt() {
	var rezvStrtDt = $('#setRezv #rezvStrtDt').val();
	var rezvEndDt = $('#setRezv #rezvEndDt').val();
	if(rezvStrtDt==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [시작일시] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.strtDt"]);
		return false;
	}
	if(rezvEndDt==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [종료일시] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.endDt"]);
		return false;
	}
	
	var rexp = /[-: ]/g;
	if(rezvStrtDt.replace(rexp,'') >= rezvEndDt.replace(rexp,'') ){
		$m.msg.alertMsg("wr.msg.noRezvDt");
		return false;
	}	
	return true;
}

function checkRezvDt(){
	if($("#subj").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [제목] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.subj"], function(){
			$("#setRezv input[name='subj']").focus();
		});
		return;
	}
	
	if($("#rescKndId").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [자원종류] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.rescKnd"]);
		return;
	}
	
	if($("#rescMngId").val().trim()==''){
		// cm.input.check.mandatory="{0}"(을)를 입력해 주십시요. - [자원명] %>
		$m.msg.alertMsg("cm.input.check.mandatory", ["#cols.rescNm"]);
		return;
	}
	
	if($('#setRezv #rescKndId').val() != '' && $('#setRezv #rescMngId').val() != '' && $('#setRezv #rezvStrtDt').val() != '' && $('#setRezv #rezvEndDt').val() != ''){
		$m.ajax('/wr/selectRezvAjx.do?menuId=${menuId}&rezvId=${wrRezvBVo.rezvId}', {
				rescKndId : $('#setRezv #rescKndId').val(),
				rescMngId : $('#setRezv #rescMngId').val(),
	        	rezvStrtDt : $('#setRezv #rezvStrtDt').val(),
	        	rezvEndDt : $('#setRezv #rezvEndDt').val(), 
	        	rezvId : '${wrRezvBVo.rezvId}',
	        	paramCompId : '${param.paramCompId}'
	        	}, function(data){
		        	if(data.message != null && data.message != ''){alert(data.message); return;}
		        	if(data.count > 0){
		        		$m.msg.alertMsg("wr.msg.noDupRezv");//예약하려는 일정은 이미 등록되어 있는 일정입니다.
		        		return;
		        	}
		        	save();//저장
			});
		}
}

function setValidation() {
	setRezvDt();
	if(!validateRezvDt()) return; //일시 비교
	checkRezvDt(); //비어 있는 시간 검증 후 저장
}          

function save() {
	$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
		if(result){
			var $form = $('#setRezv');
			$m.nav.post($form);
			//$m.nav.curr(event, '/wr/transRezv.do?menuId=${menuId}&rezvId=${wrRezvBVo.rezvId}&'+$('#setRezv').serialize());
		}
	});
}

function fnSetRescKndId(cd)
{
	$('#rescKndId').val(cd);
	$('#rescMngId').val("");
	$('.schTxt1 span').text($(".schSelect1 dd[data-rescKndId='"+cd+"']").text());
	$('.schSelect1').hide();

	$(".schSelect2 dl dd").each(function(){
		$(this).remove();
	});

	$(".schSelect2 dl").append("<dd class='txt' onclick='fnSetRescMngId(\"\");' data-rescMngId=''><u:msg titleId="cm.option.all" alt="전체선택"/></dd><dd class='line'></dd>");
	$('.schTxt2 span').text($(".schSelect2 dd:first").text());
	if(cd != ''){
		$m.ajax('/wr/selectRescAjx.do?menuId=${menuId}', {rescKndId:$('#rescKndId').val()}, function(data){
	        	$.each(data.list , function(index, vo) {
	        		var obj = JSON.parse(JSON.stringify(vo));
		        	$(".schSelect2 dl").append("<dd class='txt' onclick='fnSetRescMngId(\""+obj.rescMngId+"\");' data-rescMngId='"+obj.rescMngId+"'>"+obj.rescNm+"</dd><dd class='line'></dd>");
		     	});
		});
	} 
}

function fnSetRescMngId(cd)
{
	$('#rescMngId').val(cd);
	$('.schTxt2 span').text($(".schSelect2 dd[data-rescMngId='"+cd+"']").text());
	$('.schSelect2').hide();
	fnGetDiscrInfo();
}

function fnCalendar(id,opt,hm,hmId){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		url:'/cm/util/getCalendarPop.do?&id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&hm='+hm+'&hmId='+hmId+'&hmVal='+$('#'+hmId).val(),
	});
}

<% //Select Option 클릭 %>
function setSelOptions(codeNm, code, value){
	var $form = $("#setRezv");
	$form.find("input[name='"+codeNm+"']").val(code);
	$form.find("#"+codeNm+"Container #selectView span").text(value);
	$form.find("#"+codeNm+"Container #"+codeNm+"Open").hide();
}

var holdHide = false, holdHide2 = false, holdHide3 = false;
$(document).ready(function() {
	fnSetRescKndId('${wrRezvBVo.rescKndId}');
	fnSetRescMngId('${wrRezvBVo.rescMngId}');
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	$layout.adjustBodyHtml('bodyHtmlArea');
	$('body:first').on('click', function(){
		if(holdHide) holdHide = false;
		else $(".schSelect1").hide();
		if(holdHide2) holdHide2 = false;
		else $(".schSelect2").hide();
	});
	fnGetDiscrInfo();
});

//심의자 정보 조회
function fnGetDiscrInfo(){
	var pObj = $('#setRezv');
	var $discrArea = pObj.find('#discrInfoArea');
	$discrArea.html('');
	if(pObj.find("input[name='resqEmailYn']:checked").val() == 'Y'){
		pObj.find("input[name='resqEmailYn']:checked").trigger('click');
	}
	pObj.find('#resqEmailYnArea').hide();
	var rescKndId = pObj.find('#rescKndId').val();
	if( rescKndId == '') return;
	var rescMngId = pObj.find('#rescMngId').val();
	if( rescMngId == '') return;
	$m.ajax('/wr/selectRescMngAjx.do?menuId=${menuId}', {rescKndId:rescKndId, rescMngId:rescMngId}, function(data){
		if(data.message!=null){
			$m.dialog.alert(data.message);
		}
		if(data.result =='ok'){
			var wrRescMngBVo = $.parseJSON(data.wrRescMngBVo);
			if(wrRescMngBVo.discYn == 'Y'){//심의여부가 'Y'면 심의자 정보를 세팅한다.
				pObj.find('#resqEmailYnArea').show();
				$discrArea.append('(<u:msg titleId="wr.cols.discrNm" alt="심의자"/> : <a href="javascript:$m.user.viewUserPop(\''+wrRescMngBVo.rescAdmUid+'\');">'+wrRescMngBVo.rescAdmNm+'</a>)');
			}
		}
	});
};

<%// 정보 추가 %>
function atndAdd(objArr){
	addCols(objArr,"FRND");
};
<%// 선택추가 %>
function addCols(arr , emplTypCd){	
	if(arr==null) return;
	var area = $('#guestTypContainer').find('#selectedListArea');
	var html,emplId;//empty = area.find('dd:first').length==0;
	var vas = getAllVas(emplTypCd);
	var len = area.children().length == 0 ? false : true; 
	arr.each(function(index, data){
		emplId = emplTypCd == 'EMPL' ? data.userUid : (emplTypCd == 'ETC' ? data.etcId : data.bcId );
		if(vas==null || !vas.contains(emplId)){
			html = "";
			if(len) html +='<dd class="line"></dd>';
			//if(index == 0 && empty) $('#guestNmView span').text(data.bcNm);
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
			html +='<input type="hidden" name="emailYn" value="Y"/>';
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

<% // 사용자 추가 %>
function addUsers(arr){
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
			html +='<input type="hidden" name="emailYn" value="Y"/>';
			html += '</div>';
			html += '<div class="txt_delete" onclick="delSelects(this);"></div>';
			html +='</dd>';
			area.append(html);
			len = true;
		}
	});
}

//여러명의 사용자 선택
function openMuiltiUser(){
	var param = {};
	<c:if test="${!empty globalOrgChartEnable && globalOrgChartEnable==true}">
		param.global='Y';
	</c:if>
	
	$m.user.selectUsers(param, function(arr){
		if(arr==null || arr.length==0) return true;
		addUsers(arr);
	});
};

function guestAdd(){
	var guestTyp = $('#setRezv input:radio[name="guestTyp"]:checked').val();
	if(guestTyp == 'FRND'){
		$m.nav.next(null, '/wc/findBc.do?menuId=${param.menuId}&fncCal=${fncCal}${agntParam}&selection=multi');
		//dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}${agntParam}&callBack=atndAdd&fncMul=Y&schCat=bcNm&schWord='+encodeURIComponent($('#guest').val()));
	}else if(guestTyp == 'GROUP'){
		$m.nav.next(null, '/wc/findUserGrp.do?menuId=${param.menuId}&selection=multi${queryParamUserUid}');
	}else{
		openMuiltiUser();
	}
}

<%// [버튼] 사용자그룹 세팅 %>
function setUserGrpList(arr){
	addUsers(arr);
}

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
	var url = '/wc/viewEmptyTimeGuest.do?menuId=${menuId}&scdlStartDt='+$("#schdlStrtYmd").val()+'&scdlEndDt='+$("#schdlEndYmd").val()+'&guestUids='+guestUidsStr+'&guestNms='+guestNmStr; 
	$m.nav.next(null, url);
};

function getEditHtml(){
	return $('#bodyHtmlArea').html();
}
function setEditHtml(editHtml){
	$('#bodyHtmlArea').html(editHtml);
	$('#cont').html(editHtml);
}
//]]>
</script>
<div class="btnarea">
    <div class="size">
        <dl>
        	<dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>           	 
           	<c:if test="${writeAuth == 'Y' }">
            <dd class="btn" onclick="setValidation();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
            </c:if>
     </dl>
    </div>
</div>
<section>

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    <form id="setRezv" name="setRezv" action="/wr/transRezvPost.do?menuId=${menuId}">
	<input type="hidden" id="rezvStrtDt" name="rezvStrtDt"  value="${wrRezvBVo.rezvStrtDt}" />
	<input type="hidden" id="rezvEndDt"  name="rezvEndDt" value="${wrRezvBVo.rezvEndDt}" />
	<input type="hidden" id="rescKndId"  name="rescKndId" value="${wrRezvBVo.rescKndId}" />
	<input type="hidden" id="rescMngId" name="rescMngId"  value="${wrRezvBVo.rescMngId}" />
    <input type="hidden" id="schdlId" name="schdlId" value="${wrRezvBVo.schdlId}" />
    
    <input type="hidden" id="listPage"  name="listPage" value="/wr/${listPage}.do?menuId=${menuId}&${nonPageParams}" />
	<input type="hidden" id="viewPage"  name="viewPage" value="/wr/${listPage eq 'listRezvDisc' ? 'viewRezv' : viewPage}.do?${params}" />
    
    <input type="hidden" id="rezvId"  name="rezvId" value="${wrRezvBVo.rezvId}" />
    
    <textarea id="cont" name="cont" style="display:none">${wrRezvBVo.cont}</textarea>
	
	<c:if test="${!empty param.paramCompId }"><m:input type="hidden" id="paramCompId" value="${param.paramCompId}" /></c:if>
	
    <div class="entryzone">
        <div class="entryarea">
        <dl>
        <dd class="etr_tit"><u:msg titleId="wr.jsp.setRezv.title" alt="자원예약"/></dd>
        
        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.subj" alt="제목" /></dd>
        <dd class="etr_input"><div class="etr_inputin"><input type="text" id="subj" name="subj" class="etr_iplt" value="${wrRezvBVo.subj}" /></div></dd>
        
        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.rescKnd" alt="자원종류" /></dd>
        <dd class="etr_select">
            <div class="etr_ipmany">
                <div class="select_in1 schTxt1" onclick="holdHide = true;$('.schSelect1').toggle();">
                <dl>
                <dd class="select_txt"><span></span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </div>
             <div class="etr_open1 schSelect1" style="display:none">
                <div class="open_in1">
                    <div class="open_div">
                    <dl>
		            <dd class="txt" onclick="fnSetRescKndId('');" data-rescKndId=""><u:msg titleId="cm.option.all" alt="전체선택"/></dd>
		            <dd class="line"></dd>
					<c:forEach items="${wrRescKndBVoList}" var="list" varStatus="status">
			            <dd class="txt" onclick="fnSetRescKndId('${list.rescKndId}');" data-rescKndId="${list.rescKndId}">${list.kndNm}</dd>
			            <dd class="line"></dd>
					</c:forEach>
                 </dl>
                    </div>
                </div>
            </div>
		</dd>
		
        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.rescNm" alt="자원명" /></dd>
        <dd class="etr_select">
            <div class="etr_ipmany">
                <div class="select_in1 schTxt2" onclick="holdHide2 = true;$('.schSelect2').toggle();">
                <dl>
                <dd class="select_txt"><span></span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </div>
             <div class="etr_open1 schSelect2" style="display:none">
                <div class="open_in1">
                    <div class="open_div">
                    <dl></dl>
                    </div>
                </div>
            </div>
		</dd>
		
        <dd class="etr_bodytit_asterisk"><%-- <u:msg titleId="cols.rezvDt" alt="예약일시" /> --%><u:msg titleId="cols.strtDt" alt="시작일자" /></dd>
        <dd class="etr_select">
             <div class="etr_calendar">
             	 <input id="rezvStrtHm" name="rezvStrtHm" value="${rezvStrtHm}" type="hidden" />
             	 <input id="rezvStrtYmd" name="rezvStrtYmd" value="${rezvStrtYmd}" type="hidden" />
                 <div class="etr_calendarin">
                 <dl>
                 <dd class="ctxt" onclick="fnCalendar('rezvStrtYmd','{end:\'rezvEndYmd\'}','m','rezvStrtHm');"><span id="rezvStrtYmd">${rezvStrtYmd} ${rezvStrtHm}</span></dd>
                 <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                 <dd class="cbtn" onclick="fnCalendar('rezvStrtYmd','{end:\'rezvEndYmd\'}','m','rezvStrtHm');"></dd>
                 </dl>
                 </div>
             </div>
        </dd>    
        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.endDt" alt="종료일자" /></dd>
        <dd class="etr_select">
            <div class="etr_calendar">
            	<input id="rezvEndHm" name="rezvEndHm" value="${rezvEndHm}" type="hidden" />
            	<input id="rezvEndYmd" name="rezvEndYmd" value="${rezvEndYmd}" type="hidden" />
                <div class="etr_calendarin">
                <dl>
                <dd class="ctxt" onclick="fnCalendar('rezvEndYmd','{start:\'rezvStrtYmd\'}','m','rezvEndHm');"><span id="rezvEndYmd">${rezvEndYmd} ${rezvEndHm}</span></dd>
                <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                <dd class="cbtn" onclick="fnCalendar('rezvEndYmd','{start:\'rezvStrtYmd\'}','m','rezvEndHm');"></dd>
                </dl>
                </div>
            </div>
        </dd>
		
        <m:checkArea id="resqEmailYnArea">
        	<u:set var="checked" test="${wrRezvBVo.resqEmailYn == 'Y'}" value="true" elseValue="false"/>
        	<c:choose>
				<c:when test="${mailEnable == 'Y' }"><m:check type="checkbox" id="resqEmailYn" name="resqEmailYn" inputId="resqEmailYn" value="Y" checked="${checked }" titleId="wr.option.resqEmailYn"/></c:when>
				<c:otherwise><dd style="display:none;"><input type="checkbox" name="resqEmailYn" id="resqEmailYn" value="Y" /></dd></c:otherwise>
			</c:choose>
			<dd class="etr_body"><span id="discrInfoArea" ></span></dd>
         </m:checkArea>
				
		
		</dl>
		</div>
	</div>
	<div class="blank10"></div>
    <div class="btnarea">
        <div class="size">
            <dl>
            <dd class="btn" onclick="listRezvStatSub();"><u:msg titleId="wr.btn.rezvStat" alt="예약현황" /></dd>
         </dl>
        </div>
    </div>
	
	<c:if test="${envConfigMap.tgtUseYn eq 'Y' || envConfigMap.guestUseYn eq 'Y'}">
	<div class="entryzone">
		<div class="blank20"></div>
	    <div class="entryarea">
	    <dl>
	    	<c:if test="${envConfigMap.tgtUseYn eq 'Y'}">
			<dd class="etr_bodytit"><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></dd>
	        <dd class="etr_select" id="schdlKndCdContainer">
	        	<c:set var="schdlKndNm" value=""/>
	        	<c:set var="schdlKndCd" value=""/>
	            <div class="etr_open1" id="schdlKndCdOpen" style="display:none">
	                <div class="open_in1">
	                    <div class="open_div"><u:set var="paramKndCd" test="${!empty wrRezvBVo.schdlKndCd }" value="${wrRezvBVo.schdlKndCd }" elseValue="${envConfigMap.schdlKndCd }"/>
	                    <dl>
	                    <c:forEach items="${schdlKndCdList}" var="cd" varStatus="status">
	                    <c:if test="${(empty paramKndCd && status.first) || (cd[0] == paramKndCd)}"><c:set var="schdlKndNm" value="${cd[1] }"/><c:set var="schdlKndCd" value="${cd[0] }"/></c:if>
						<c:if test="${status.count>0 }"><dd class="line"></dd></c:if>
	                    <dd class="txt" onclick="setSelOptions('schdlKndCd',$(this).attr('data-code'),$(this).text());" data-code="${cd[0]}">${cd[1] }</dd>
	                    </c:forEach>
	                 </dl>
	                    </div>
	                </div>
	            </div>
	            <input type="hidden" name="schdlKndCd" value="${schdlKndCd }"/>
	            <div class="etr_ipmany">
	                <div class="select_in1" onclick="holdHide = true; $('#schdlKndCdContainer #schdlKndCdOpen').toggle();">
	                <dl>
	                <dd class="select_txt" id="selectView"><span>${schdlKndNm }</span></dd>
	                <dd class="select_btn"></dd>
	                </dl>
	                </div>
	            </div>
	        </dd>
			</c:if><c:if test="${envConfigMap.guestUseYn eq 'Y'}">			
	    	<dd class="etr_bodytit"><div class="icotit_dot"><u:msg titleId="cols.guestApnt" alt="참석자지정" /></div></dd>
			<dd class="etr_input">
				<div class="etr_ipmany" id="guestTypChkContainer">
	            <dl>
	            	<m:check type="radio" id="guestTyp" name="guestTyp" inputId="guestTyp" value="EMPL" areaId="guestTypChkContainer" titleId="cm.option.empl" checked="true" />
	            	<m:check type="radio" id="guestTyp" name="guestTyp" inputId="guestTyp" value="FRND" areaId="guestTypChkContainer" titleId="wc.option.frnd"/>
	            	<m:check type="radio" id="guestTyp" name="guestTyp" inputId="guestTyp" value="GROUP" areaId="guestTypChkContainer" titleId="wc.term.userGroup"/>
	            	<dd class="etr_btn" onclick="guestAdd();"><u:msg titleId="cm.btn.add" alt="추가" /></dd>
	            	<dd class="etr_btn" onclick="emptyTimeGuest();"><u:msg titleId="cm.btn.confirm" alt="확인" /></dd>
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
                        		<dd class="txt_dd" data-guestUid="${list.guestUid }" data-emplTypCd="${!empty list.guestEmplYn && list.guestEmplYn eq 'Y' ? 'EMPL' : 'FRND'}" >
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
										<input type="hidden" name="emailYn" value="Y"/>
	                                </div>
	                                <div class="txt_delete" onclick="delSelects(this);"></div>
	                            </dd>
                            	<c:if test="${fn:length(wcPromGuestDVoList) > status.count }"><dd class="line"></dd></c:if>
							</c:forEach>
                     	</dl>
                        </div>
                    </div>
                </div>
                <c:if test="${ !empty wcSchdlBVo.schdlId && !empty wcPromGuestDVoList}"><c:set var="guestNm" value="${wcPromGuestDVoList[0].guestNm}"/></c:if>
                <div class="etr_ipmany">
                    <div class="select_in1" onclick="$('#setRezv #guestTypContainer').toggle();">
                    <dl>
                    <dd class="select_txt" id="guestNmView"><span><u:msg titleId="cols.guestApnt" alt="참석자지정" /></span></dd>
                    <dd class="select_btn"></dd>
                    </dl>
                    </div>
                </div>
            </dd>
            <c:if test="${mailEnable == 'Y'}">
            	<dd class="etr_input">
                    <div class="etr_ipmany">
                    <dl>
                    	<u:set var="checked" test="${wcSchdlBVo.emailSendYn == 'Y'}" value="true" elseValue="false"/>
						<m:check type="checkbox" id="emailSendY" name="emailSendYn" inputId="emailSendYn" value="Y" titleId="cols.emailSend" checked="${checked }" />
                    </dl>
                    </div>
                </dd>
            </c:if></c:if>
	        </dl>
	    </div>
	</div>
	
	</c:if>
	
	
    <div class="blankzone">
        <div class="blank25"></div>
        <div class="line1"></div>
        <div class="line8"></div>
        <div class="line1"></div>
        <div class="blank25"></div>
    </div>


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
         <dd class="etr_input"><div class="etr_bodyline editor" id="bodyHtmlArea"><c:if test="${!empty wrRezvBVo.cont }"><u:out value="${wrRezvBVo.cont}" type="noscript" /></c:if></div></dd>


		</dl>
		</div>
	</div>
	
    <div class="blank25"></div>
    <div class="btnarea">
        <div class="size">
            <dl>
            <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
            <c:if test="${writeAuth == 'Y' }">
            <dd class="btn" onclick="setValidation();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
            </c:if>
         </dl>
        </div>
    </div>

	
	</form>
	
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
 </section>