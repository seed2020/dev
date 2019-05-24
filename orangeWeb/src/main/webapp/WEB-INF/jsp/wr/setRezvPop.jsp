<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:out value="${wrRezvBVo.rezvStrtDt}" type="date" var="rezvStrtYmd" />
<u:out value="${wrRezvBVo.rezvStrtDt}" type="hm" var="rezvStrtHm" />
<u:out value="${wrRezvBVo.rezvEndDt}" type="date" var="rezvEndYmd" />
<u:out value="${wrRezvBVo.rezvEndDt}" type="hm" var="rezvEndHm" />
<u:set test="${!empty wrRezvBVo.rezvId }" var="isRezvCheck" value="N" elseValue="Y"/>
<script type="text/javascript">
<!--
//자원현황 팝업
function listRezvPop(schTyp) {
	var url = './listRezvStatPop.do?menuId=${menuId}&rescKndId='+$("#setRezv #rescKndId").val()+'&rescMngId='+$("#setRezv #rescMngId").val();
	url+="&durCat=fromYmd";
	url+="&durStrtDt="+$('#rezvStrtYmd').val();
	url+="&durEndDt="+$('#rezvEndYmd').val();
	if(schTyp) url+="&schTyp="+schTyp;
	<c:if test="${!empty param.paramCompId }">url+="&paramCompId=${param.paramCompId}";</c:if>
	dialog.open('listRezvStatPop','<u:msg titleId="wr.btn.rezvStat" alt="예약현황"/>',url);
};

var isRezvCheck = '${isRezvCheck}';
<% // 예약일시 세팅 %>
function setRezvDt() {
	if ($('#rezvStrtYmd').val() != '' || $('#rezvEndYmd').val() != '' ) {
		$('#setRezv #rezvStrtDt').val($('#rezvStrtYmd').val() + ' ' + $('#rezvStrtHm').val());
		$('#setRezv #rezvEndDt').val($('#rezvEndYmd').val() + ' ' + $('#rezvEndHm').val());
	} else {
		$('#setRezv #rezvStrtDt').val('');
		$('#setRezv #rezvEndDt').val('');
	}
};

//시간을 변경하게되면 일시 체크 유효성 검증을 활성화한다.
function fnHmChange(){
	isRezvCheck = 'Y';
};

<% // 예약일시 비교(시작일시 < 종료일시) %>
function validateRezvDt() {
	var rezvStrtDt = $('#setRezv #rezvStrtDt').val();
	var rezvEndDt = $('#setRezv #rezvEndDt').val();
	var rexp = /[-: ]/g;
	if(rezvStrtDt.replace(rexp,'') >= rezvEndDt.replace(rexp,'') ){
		alertMsg("wr.msg.noRezvDt");
		return false;
	}	
	return true;
};

//비어있는 시간인지 조회
function checkRezvDt(){
	if($('#setRezv #rescKndId').val() != '' && $('#setRezv #rescMngId').val() != '' && $('#setRezv #rezvStrtDt').val() != '' && $('#setRezv #rezvEndDt').val() != ''){
		$.ajax({
	        url: './selectRezvAjx.do?menuId=${menuId}',
	        type: 'POST',
	        data:{
	        	rescKndId : $('#setRezv #rescKndId').val(),
	        	rescMngId : $('#setRezv #rescMngId').val(),
	        	rezvStrtDt : $('#setRezv #rezvStrtDt').val(),
	        	rezvEndDt : $('#setRezv #rezvEndDt').val(),
	        	rezvId : '${wrRezvBVo.rezvId}', //수정일 경우 해당 rezvId를 첨부
	        	paramCompId : '${param.paramCompId}'
	             },
	        dataType : "json",
	        success: function(data){
	        	if(data.message != null && data.message != ''){alert(data.message); return false;}
	        	if(data.model.count > 0){
	        		alertMsg("wr.msg.noDupRezv");//예약하려는 일정은 이미 등록되어 있는 일정입니다.
	        		return;
	        	}
	        	save();//저장
	        }
		});
	}
};

<% // [하단버튼:저장시 유효성 검증] %>
function setValidation() {
	setRezvDt();
	if(!validator.validate('setRezv')) return;//기본 유효성 검증
	if(!validateRezvDt()) return;//일시 비교
	if(!checkRezvDt()) return;//비어 있는 시간 검증
	else save();
};

<% // [하단버튼:저장] %>
function save() {
	if (isInUtf8Length($('#setRezv #cont').val(), 0, '${bodySize}') > 0) {
		alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.cont" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
		return;
	}
	if (true/*confirmMsg("cm.cfrm.save")*/ ) {
		
		var $form = $('#setRezv');
		$form.attr('method','post');
		$form.attr('action','./transRezv.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		editor('cont').prepare();
		$form[0].submit();
	}
};

//자원목록 조회
function setRescMng(obj){
	var $setRezv = $('#setRezv');
	$('#setRezv #rescMngId').find('option').each(function(){
		$(this).remove();
	});
	if($(obj).val() != ''){
		$.ajax({
	        url: './selectRescAjx.do?menuId=${menuId}',
	        type: 'POST',
	        data:{
	        	rescKndId : $(obj).val(),
	        	useYn : 'Y'
	             },
	        dataType : "json",
	        success: function(data){
	        	//if(data == null){}
	        	$.each(data.model.list , function(index, rescMngVo) {
	        		$setRezv.find('#rescMngId').append('<u:option value="'+rescMngVo.rescMngId+'" title="'+rescMngVo.rescNm+'"/>');
	        	});
	        	$setRezv.find('#rescMngId').uniform();
	        	fnGetDiscrInfo();
	        }
		});
	}
};

//심의자 정보 조회
function fnGetDiscrInfo(){
	var pObj = $('#setRezv');
	var $discrArea = pObj.find('#discrInfoArea');
	$discrArea.html('');
	if(!pObj.find("input[name='resqEmailYn']").is(':checked')){
		pObj.find("input[name='resqEmailYn']").trigger('click');
	}
	pObj.find('#resqEmailYnArea').hide();
	var rescKndId = pObj.find('#rescKndId').val();
	if( rescKndId == '') return;
	var rescMngId = pObj.find('#rescMngId').val();
	if( rescMngId == '') return;
	callAjax('./selectRescMngAjx.do?menuId=${menuId}', {rescKndId:rescKndId, rescMngId:rescMngId}, function(data){
		if(data.message!=null){
			alert(data.message);
		}
		if(data.result == 'ok' && data.wrRescMngBVo != null){
			var wrRescMngBVo = $.parseJSON(data.wrRescMngBVo);
			if(wrRescMngBVo.discYn == 'Y'){//심의여부가 'Y'면 심의자 정보를 세팅한다.
				pObj.find('#resqEmailYnArea').show();
				$discrArea.append('<u:msg titleId="wr.cols.discrNm" alt="심의자"/> : <a href="javascript:viewUserPop(\''+wrRescMngBVo.rescAdmUid+'\');">'+wrRescMngBVo.rescAdmNm+'</a>');
			}	
		}
	});
};

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

<% // 임직원, 지인 라디오 선택%>
function guestChange(obj){
	var $input = $('#setRezv input[id="guest"]');
	$input.val('');
	var disabled = false;
	if($(obj).val()=='G') disabled = true;
	setDisabled($input, disabled);
};

<% // 참석자 추가%>
function guestAdd(){
	var guestTyp = $('#setRezv input:radio[name="guestTyp"]:checked').val();
	
	var guest=$('#setRezv input[id="guest"]').val();
	
	if(guestTyp == 'N'){
		if(guest!=''){
			callAjax('./srchUserAjx.do?menuId=${menuId}&fncCal=${fncCal}', {srchTyp:'bc',srchName:guest, paramUserUid:'${param.paramUserUid }'}, function(data) {
				if (data.returnString != null) {
					var arr=[];
					arr.push($.parseJSON(data.returnString));
					atndAdd(arr);
					return;	
				}
				dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}&fncCal=${fncCal}${agntParam}&callBack=atndAdd&fncMul=Y&schCat=bcNm&schWord='+encodeURIComponent(guest));
			});
		}else{
			dialog.open('findBcPop','<u:msg titleId="wb.jsp.findBcPop.title" alt="명함선택" />','./findBcPop.do?menuId=${menuId}&fncCal=${fncCal}${agntParam}&callBack=atndAdd&fncMul=Y&schCat=bcNm&schWord='+encodeURIComponent(guest));
		}		
	}else if(guestTyp == 'G'){
		setUserGrpPop();
	}else{
		if(guest!=''){
			callAjax('./srchUserAjx.do?menuId=${menuId}&fncCal=${fncCal}', {srchTyp:'user',srchName:guest}, function(data) {
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
			$("#setRezv input[name=bcId]").val(obj.bcId);
			$("#setRezv input[name=bcNm]").val(obj.bcNm);
			$("#setRezv input[name=bcCompNm]").val(obj.compNm);
		});		
	}
	dialog.close('findBcPop');
};

//비어 있는 시간 확인
function emptyTimeGuest(){
	var startDt = $("#rezvStrtYmd").val();
	var endDt = $("#rezvEndYmd").val();
	
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
	
	if(startDt == "" || endDt ==""){
		alert("<u:msg titleId="wc.msg.prom.inpTime" alt="'약속시간'을 입력해주시기 바랍니다."/>");
		return;
	}
	
	if(guestUsers.length == 0){
		alert("<u:msg titleId="wc.msg.prom.posAttend" alt="'참석자'를 지정해주시기 바랍니다."/>");
		return;
	}
	
	var guestUidsStr ="";
	var guestNmStr = "";
	
	for(var i=0;i<guestUsers.length;i++){
		guestUidsStr += guestUidsStr == "" ? guestUsers[i].userUid : "|"+ guestUsers[i].userUid;
		guestNmStr += guestNmStr == "" ? guestUsers[i].userNm : "|"+ guestUsers[i].userNm;
	}
	guestNmStr=encodeURIComponent(guestNmStr);
	dialog.open('viewEmptyTimeGuestPop', '<u:msg titleId="wc.btn.freeTmCnfm" alt="빈시간확인"/>','./viewEmptyTimeGuestPop.do?${paramsForList}&scdlStartDt='+startDt+'&scdlEndDt='+endDt+'&guestUids='+guestUidsStr+'&guestNms='+guestNmStr);
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

<%//uniform 적용하기 %>
function setJsUniform(obj){
	$(obj).find("input, textarea, select, button").uniform();
	$(obj).find("input[type='checkbox'],input[type='radio']").bind("keydown", function(event){
		if(event.which==13) $(this).trigger("click");
	});
};

//-->
</script>
<div style="width:713px;">
<form id="setRezv">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="rezvId" value="${wrRezvBVo.rezvId}" />

<u:input type="hidden" id="rezvStrtDt" value="${wrRezvBVo.rezvStrtDt}" />
<u:input type="hidden" id="rezvEndDt" value="${wrRezvBVo.rezvEndDt}" />

<u:input type="hidden" id="listPage" value="./${listPage }.do?${paramsForList}" />

<!-- 일정ID 추가 -->
<u:input type="hidden" id="schdlId" value="${wrRezvBVo.schdlId}" />

<c:if test="${!empty param.paramCompId }"><u:input type="hidden" id="paramCompId" value="${param.paramCompId}" /></c:if>

<% // 폼 필드 %>
<u:listArea colgroup="18%,82%">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.rescKnd" alt="자원종류" /></td>
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td width="30%">
						<select id="rescKndId" name="rescKndId"  style="width:150px;" onchange="setRescMng(this);">
							<c:forEach items="${wrRescKndBVoList}" var="list" varStatus="status">
								<u:option value="${list.rescKndId}" title="${list.kndNm}" selected="${wrRezvBVo.rescKndId == list.rescKndId}"/>
							</c:forEach>
						</select>
					</td>
					<td width="20%">
						<u:checkArea id="resqEmailYnArea" style="display:none;">
							<c:choose>
								<c:when test="${mailEnable == 'Y' }"><u:checkbox id="resqEmailYn" name="resqEmailYn" value="Y" titleId="wr.option.resqEmailYn" alt="신청메일여부" checkValue="${wrRezvBVo.resqEmailYn }" checked="${empty wrRezvBVo.resqEmailYn }"/></c:when>
								<c:otherwise><input type="checkbox" name="resqEmailYn" id="resqEmailYn" value="Y" style="display:none;"/></c:otherwise>
							</c:choose>
						</u:checkArea>
					</td>
					<td class="body_lt">
						<span id="discrInfoArea" ></span>&nbsp;
					</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.rescNm" alt="자원명" /></td>
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td width="80%">
						<select id="rescMngId" name="rescMngId" style="width:300px;"onchange="fnGetDiscrInfo();">
							<c:forEach items="${wrRescMngBVoList}" var="list" varStatus="status">
								<u:option value="${list.rescMngId}" title="${list.rescNm}" selected="${wrRezvBVo.rescMngId == list.rescMngId}"/>
							</c:forEach>
						</select>
					</td>
					<td><u:buttonS href="javascript:;" onclick="listRezvPop();" titleId="wr.btn.rezvStat" alt="예약현황" /></td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
	
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.rezvDt" alt="예약일시" /></td>
		<td class="${!empty wrRezvBVo.rezvId ? 'body_lt' : ''}">
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td><u:calendar id="rezvStrtYmd" value="${rezvStrtYmd}" titleId="wr.cols.rezvStrtYmd" mandatory="Y" /></td>
					<td>
						<select id="rezvStrtHm" name="rezvStrtHm" onchange="fnHmChange();" >
						<c:forEach begin="0" end="23" step="1" var="hour" varStatus="status">
							<u:set test="${hour < 10}" var="hh" value="0${hour}" elseValue="${hour}" />
							<u:option value="${hh}:00" title="${hh}:00" checkValue="${rezvStrtHm}" />
							<c:if test="${hour < 24}"><u:option value="${hh}:30" title="${hh}:30" checkValue="${rezvStrtHm}" /></c:if>
						</c:forEach>
						</select>
					</td>
					<td class="body_lt">~</td>
					<td><u:calendar id="rezvEndYmd" value="${rezvEndYmd}" titleId="wr.cols.rezvEndYmd" mandatory="Y" /></td>
					<td>
						<select id="rezvEndHm" name="rezvEndHm" onchange="fnHmChange();" >
						<c:forEach begin="0" end="23" step="1" var="hour" varStatus="status">
							<u:set test="${hour < 10}" var="hh" value="0${hour}" elseValue="${hour}" />
							<u:option value="${hh}:00" title="${hh}:00" checkValue="${rezvEndHm == '00:00' ? '24:00' : rezvEndHm}" />
							<c:if test="${hour < 24}"><u:option value="${hh}:30" title="${hh}:30" checkValue="${rezvEndHm}" /></c:if>
						</c:forEach>
						</select>
					</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
		<td><u:input id="subj" value="${wrRezvBVo.subj}" titleId="cols.subj" style="width: 96%;" maxByte="240" mandatory="Y"/></td>
	</tr>

</u:listArea>

<c:if test="${envConfigMap.tgtUseYn eq 'Y' || envConfigMap.guestUseYn eq 'Y'}">
<u:listArea colgroup="18%,82%">
<c:if test="${envConfigMap.tgtUseYn eq 'Y'}">
<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="wc.cols.schdlKndCd" alt="일정대상"/></td>
	<td><u:set var="paramKndCd" test="${!empty wrRezvBVo.schdlKndCd }" value="${wrRezvBVo.schdlKndCd }" elseValue="${envConfigMap.schdlKndCd }"/><u:checkArea><c:forEach items="${schdlKndCdList}" var="cd" varStatus="status"
	><u:radio name="schdlKndCd" value="${cd[0]}" title="${cd[1] }" alt="${cd[1] }" inputClass="bodybg_lt" checkValue="${paramKndCd }" checked="${empty paramKndCd && status.first }"
	/></c:forEach></u:checkArea></td>
</tr>
</c:if>
<c:if test="${envConfigMap.guestUseYn eq 'Y'}">
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
	</td>
</tr>
</c:if>
</u:listArea>
</c:if>

<div id="contArea" class="listarea" style="width:100%; height:302px; padding-top:2px"></div>
<%
	if(request.getAttribute("namoEditorEnable")==null){
		com.innobiz.orange.web.wr.vo.WrRezvBVo wrRezvBVo = (com.innobiz.orange.web.wr.vo.WrRezvBVo)request.getAttribute("wrRezvBVo");
		if(wrRezvBVo != null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(wrRezvBVo.getCont());
			request.setAttribute("_bodyHtml", _bodyHtml);
		}
	} else {
		com.innobiz.orange.web.wr.vo.WrRezvBVo wrRezvBVo = (com.innobiz.orange.web.wr.vo.WrRezvBVo)request.getAttribute("wrRezvBVo");
		request.setAttribute("_bodyHtml", wrRezvBVo.getCont());
	}
%>
<u:editor id="cont" width="100%" height="300px" module="wr" areaId="contArea" value="${_bodyHtml}" namoToolbar="wcPop" />

<u:blank />
<u:buttonArea>
	<c:if test="${empty wrRezvBVo.rezvId || (!empty wrRezvBVo.rezvId && (wrRezvBVo.discStatCd eq 'B' || wrRezvBVo.discStatCd eq 'R')) }">
		<u:button titleId="cm.btn.save" href="javascript:setValidation();" alt="저장" auth="W" ownerUid="${wrRezvBVo.regrUid }" />
	</c:if>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>

<script type="text/javascript">
fnGetDiscrInfo();
</script>
