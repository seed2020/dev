<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="frmUrl" test="${!empty param.docTyp && param.docTyp eq 'brd'}" value="/cm/doc/setSendBrdWriteSub.do?${paramsForList }" elseValue="/dm/doc/setSendWriteSub.do?${paramsForList }"/>
<u:params var="nonPageParams" excludes="docId,pageNo,noCache"/>
<u:params var="viewPageParams" excludes="docId,docPid,noCache"/>
<u:set var="includeParams" test="${empty dmDocLVoMap.docId && !empty param.docPid}" value="&docId=${param.docPid }" elseValue="&docId=${dmDocLVoMap.docId }"/>
<u:params var="params" excludes="data,noCache"/>
<u:set var="urlPrefix" test="${!empty param.docTyp && param.docTyp eq 'brd'}" value="/cm/doc" elseValue="/dm/doc"/>
<script type="text/javascript">
//<![CDATA[
//오늘 날짜 리턴
function getToday(){
	var d = new Date();
	return d.getFullYear() +"-"+ ((d.getMonth() > 8) ? d.getMonth()+1 : "0"+(d.getMonth()+1)) +"-"+ ((d.getDate() > 9) ? d.getDate() : "0"+d.getDate());
}         
         
//오늘 시간 리턴
function getToTime(){
	var d = new Date();
	return (d.getHours() < 10 ? "0"+d.getHours() : d.getHours() ) +":"+ (d.getMinutes() < 10 ? "0"+d.getMinutes() : d.getMinutes() );
}

<% // 일시 replace %>
function getDayString(date , regExp){
	return date.replace(regExp,'');
};

<% // 일시 비교 %>
function fnCheckDay(today , setday){
	return today > setday ? true : false;
};

<% // 예약일시 체크 %>
function onBullRezvDayChange(date){
	var regExp = /[^0-9]/g;
	var today = getDayString(getToday(),regExp);
	var setday = getDayString(date,regExp);
	if(fnCheckDay(today , setday)){
		$m.msg.alertMsg('cm.calendar.check.dateAI');
		return true;
	}
	onBullRezvTimeChange('bullRezvHm',setday,'${bullRezvHm }');
	return false;
};

<% // 게시완료일 체크 %>
function onBullExprDayChange(date){
	var regExp = /[^0-9]/g;
	var today = getDayString(getToday(),regExp);
	var setday = getDayString(date,regExp);
	if(fnCheckDay(today , setday)){
		$m.msg.alertMsg('cm.calendar.check.dateAI');
		return true;
	}
	onBullRezvTimeChange('bullExprHm',setday,'${bullExprHm }');
	return false;
};

<% // 예약시간 체크 %>
function onBullRezvTimeChange(objId , setday , initTime){
	var regExp = /[^0-9]/g;
	var today = getDayString(getToday(),regExp);
	setday = getDayString(setday,regExp);
	if(today == setday ){
		var toTime = getToTime().replace(/[^0-9]/g,'');
		var setTime = $('#'+objId).val().replace(/[^0-9]/g,'');
		if(fnCheckDay(toTime , setTime)){
			$m.msg.alertMsg('cm.calendar.check.dateAI');
			$('#'+objId).val(initTime);
		}
	}
};


<% // 예약일시 세팅 %>
function setRezvDt() {
	if ($('#bullRezvYmd').val() != '' && $('#bullRezvYnChk').is(':checked')) {
		$('#bullRezvDt').val($('#bullRezvYmd').val() + ' ' + $('#bullRezvHm').val() + ':00');
	} else {
		$('#bullRezvDt').val('');
	}
}
<% // 완료일시 세팅 %>
function setExprDt() {
	if ($('#bullExprYmd').val() != '') {
		$('#bullExprDt').val($('#bullExprYmd').val() + ' ' + $('#bullExprHm').val() + ':00');
	}
}
<% // 게시대상 세팅 %>
function setBbTgt() {
	$('#tgtDeptYn').val($("#bbTgtDeptHidden input[name='orgId']").length > 0 ? 'Y' : 'N');
	$('#tgtUserYn').val($("#bbTgtUserHidden input[name='userUid']").length > 0 ? 'Y' : 'N');
}

function fnSetCatId(cd)
{
	$('#catId').val(cd);
	$('.schTxt2 span').text($(".schOpnLayer2 dd[data-schCd='"+cd+"']").text());
	$('.schOpnLayer2').hide();
}


function fnSetExtSelect(colm, cd)
{
	$('#'+colm).val(cd);
	$('.colmTxt'+colm+' span').text($(".colmLayer"+colm+" dd[data-schCd='"+cd+"']").text());
	$('.colmLayer'+colm).hide();
}
function fnCalendar(id,opt,hm,hmId,handler){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		url:'/cm/util/getCalendarPop.do?&id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&hm='+hm+'&hmId='+hmId+'&hmVal='+$('#'+hmId).val()+'&handler='+handler,
	});
}

function fnbullRezvYnChk(){
	if($('dd#bullRezvYnChkArea').attr("class") == "check"){ 
		$('.bullRezvCalLayer').show();
		$('#bullRezvYn').val('Y');
	}else{
		$('.bullRezvCalLayer').hide();
		$('#bullRezvYn').val('N');
	}
};
<% //Select Option 클릭 %>
function setSelOptions(codeNm, code, value){
	var $form = $("#setForm");
	$form.find("input[name='"+codeNm+"']").val(code);
	$form.find("#"+codeNm+"Container #selectView span").text(value);
	$form.find("#"+codeNm+"Container #"+codeNm+"Open").hide();
}<% //사용자 조회%>
function openSingleUser(areaNm){
	$m.user.selectOneUser({userUid:'${dmDocLVoMap.ownrUid}'}, function(userVo){
		if(userVo==null){<%
			// or.msg.noUser=선택된 사용자가 없습니다.%>
			$m.msg.alertMsg('or.msg.noUser');
			return false;
		} else {
			$('#'+areaNm+'InfoArea').find("input[id='"+areaNm+"Uid']").val(userVo.userUid);
			$('#'+areaNm+'InfoArea').find("input[id='"+areaNm+"Nm']").val(userVo.rescNm);
			return true;
		}
	});
}
var listAddItemArea = null;
<%// 폴더를 변경할 경우 페이지를 리로딩해준다.[폴더에 등록된 유형에 대한 추가항목 로드] %>
function setPageChk(prefix){
	if(prefix == 'cls') return;
	$m.ajax('${_cxPth}${urlPrefix}/listAddItemAjx.do?menuId=${menuId}', {fldId:'${dmDocLVoMap.fldId }',paramStorId:'${paramStorId}'}, function(html){
		if(listAddItemArea==null) listAddItemArea = $('#section').find('#listAddItemArea');
		listAddItemArea.html(html);
	}, {mode:'HTML', async:true});
};<%// 1명의 사용자 선택 %>
function openSingUser(){
	var data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	if($('#ownrUid').val() != '') data.push({userUid:$('#ownrUid').val()});
	<%// option : data, multi, withSub, titleId %>
	searchUserPop({data:data}, function(userVo){
		if(userVo!=null){
			$('#ownrUid').val(userVo.userUid);
			$('#ownrNm').val(userVo.rescNm);
		}
	});
};<%// 분류,폴더 Prefix %>
function getTabPrefix(lstTyp){
	var prefix = "fld";
	if(lstTyp == 'C') prefix = "cls";
	return prefix;
}<%// [버튼] 분류,폴더 %>
function findSelPop(lstTyp){
	var prefix = getTabPrefix(lstTyp);
	var $area = $("#"+prefix+"InfoArea"), data = [];
	$area.find("input[id='"+prefix+"Id']").each(function(){
		data.push($(this).val());
	});
	var url = '/dm/doc/findSelSub.do?menuId=${menuId}&lstTyp='+lstTyp+"&selIds="+data+"&fncMul="+(lstTyp == 'C' ? 'Y':'N');
	$m.nav.next(null,url);
	//var msgTitle = lstTyp == 'C' ? '<u:msg titleId="dm.cols.listTyp.cls" alt="분류보기" />' : '<u:msg titleId="dm.cols.listTyp.fld" alt="폴더보기" />';
	//dialog.open('findSelPop', msgTitle, url);
};<%// 분류,폴더 적용%>
function setSelInfos(arr, lstTyp){
	var prefix = getTabPrefix(lstTyp);
	$area = $('#'+prefix+'InfoArea');
	
	var buffer = [];
	var nms = '';
	arr.each(function(index, obj){
		buffer.push("<input type='hidden' id='"+prefix+"Id' name='"+prefix+"Id' value='"+obj.id+"'/>\n");
		nms+= nms == '' ? obj.nm : ','+obj.nm;
	});
	$area.find('#idArea').html('');
	$area.find('#idArea').html(buffer.join(''));
	$area.find('#nmArea input[id="'+prefix+'Nm"]').val(nms);
	setPageChk(prefix);
}<%// 폴더를 변경할 경우 페이지를 리로딩해준다.[폴더에 등록된 유형에 대한 추가항목 로드] %>
function setPsnPageChk(prefix){
	if(prefix == 'cls') return;
	$m.ajax('${_cxPth}/dm/doc/listPsnAddItemAjx.do?menuId=${menuId}', {fldId:$('#fldId').val(),paramStorId:'${paramStorId}'}, function(html){
		if(listAddItemArea==null) listAddItemArea = $('#section').find('#listAddItemArea');
		listAddItemArea.html(html);
	}, {mode:'HTML', async:true});
};<%// [버튼] 분류,폴더 %>
function findFldPop(lstTyp){
	var prefix = getTabPrefix(lstTyp);
	var $area = $("#"+prefix+"InfoArea"), data = [];
	$area.find("input[id='"+prefix+"Id']").each(function(){
		data.push($(this).val());
	});
	var url = '/dm/doc/findFldSub.do?menuId=${menuId}&lstTyp='+lstTyp+"&fldId="+data+"&fncMul="+(lstTyp == 'C' ? 'Y':'N');
	$m.nav.next(null,url);
};<%// 분류,폴더 적용%>
function setFldInfos(arr, lstTyp){
	var prefix = getTabPrefix(lstTyp);
	$area = $('#'+prefix+'InfoArea');
	
	var buffer = [];
	var nms = '';
	arr.each(function(index, obj){
		buffer.push("<input type='hidden' id='"+prefix+"Id' name='"+prefix+"Id' value='"+obj.id+"'/>\n");
		nms+= nms == '' ? obj.nm : ','+obj.nm;
	});
	$area.find('#idArea').html('');
	$area.find('#idArea').html(buffer.join(''));
	$area.find('#nmArea input[id="'+prefix+'Nm"]').val(nms);
	setPsnPageChk(prefix);
}
<%// [버튼] 저장 %>
function save(){
	if (validator.validate('setForm')) {
		
		if(confirmMsg("cm.cfrm.save")){
			setValid();
			var $form = $("#setForm");
			$form.attr('method','post');
			$form.attr('action','${urlPrefix}/${transPage}.do?menuId=${menuId}');
			$form.attr('target','dataframe');
			$('#cont').val(jellyEditor('cont').getHtml());
			$form.submit();
		}
	}
}<%// 폴더의 선택 ID 리턴 %>
function getSelectAllId(tabId){
	if(tabId != 'doc') return null;
	var fldId = $('#setForm').find('input[name="fldId"]').val();
	return fldId == '' ? null : fldId;
}<%// 업무별 값 세팅 %>
function setValid(){
	<c:if test="${param.tabId ne 'brd'}">//게시판이 아닐경우 확장컬럼값 폼에 세팅
	// 추가항목 조회
	if($('#listAddItemArea').html() != ''){
		var arrs = getChkVal();
		if(arrs != null){
			$.each(arrs,function(index,vo){
				$('#setForm').appendHidden({name:vo.name,value:vo.value});
			});	
		}
	}
	</c:if>
	<c:if test="${param.tabId eq 'brd'}">
		<c:if test="${baBrdBVo.discYn != 'Y'}">
		$('#bullStatCd').val('B');
		</c:if>
		<c:if test="${baBrdBVo.discYn == 'Y'}">
		$('#bullStatCd').val('S');
		</c:if>
		setRezvDt();
		setExprDt();
		setBbTgt();
	</c:if>
}
<%// [버튼] 저장 %>
function saveWrite(){
	if (validator.validate('setForm')) {
		$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
			if(result){
				setValid();
				var $form = $("#setForm");
				$form.attr('method','post');
				$form.attr('action','/dm/doc/${transPage}Post.do?menuId=${menuId}');
				$m.nav.post($form);
			}
		});
	}
}<%// [팝업] 하위문서 %>
function findWriteDocPop(tabId, callback){
	var url = '/cm/doc/findDocSub.do?${nonPageParams}&fncMul=N&tabId=${param.tabId}';
	if(tabId != undefined && tabId == 'doc') {
		var selId = $('#setForm').find('input[name="fldId"]').val();
		if(selId == null){
			$m.msg.alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
			return;
		}
		url+= "&fldId="+selId;
	}
	if(callback != null) url+= "&callback="+callback;
	$m.nav.next(null, url);
};<%// [하단버튼:] 보내기옵션 %>
function setSendOptPop(){
	var selId = '';
	if(selId == null){
		$m.msg.alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld=왼쪽 '폴더'를 선택 후 사용해 주십시요.%>
		return;
	}
	dialog.open('setSendOptDialog', '<u:msg titleId="dm.jsp.sendWrite.title" alt="보내기작성" />', '/cm/doc/setSendOptPop.do?${paramsForList}&docId=${param.docId}&selId='+selId+'&tabId='+clkTab);
};<%// [프레임리로드:] 하위문서선택시  %>
function sendSubReload(arr){
	if(arr == null) {
		$m.nav.curr(null, '${frmUrl}');
		$('#sendBtnArea #subDocListBtn').show();
	}else {
		$m.nav.prev(null, '${frmUrl}&docPid='+arr);
		$('#sendBtnArea #subDocListBtn').hide();
	}
};
$(document).ready(function() {
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	<c:if test="${param.tabId eq 'brd' && bbBullLVo.bullRezvYn != 'Y'}">
	fnSetCatId('${bbBullLVo.catId}');
	</c:if>
	<c:if test="${param.tabId eq 'doc'}">setPageChk('fld');</c:if>
	<c:if test="${param.tabId eq 'psn'}">setPsnPageChk('fld');</c:if>
});
//]]>
</script>
<%
	@SuppressWarnings("unchecked")
	java.util.Map<String,String> dmDocLVoMap = (java.util.Map<String,String>)request.getAttribute("dmDocLVoMap");
	if(dmDocLVoMap != null){
		String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(dmDocLVoMap.get("cont"));
		request.setAttribute("_bodyHtml", _bodyHtml);
	}
%>
<section id="section">
<u:secu auth="W">
	<div class="btnarea">
		<div class="size">
		<dl>
		<c:if test="${!empty param.tabId && param.tabId eq 'doc' && empty param.docPid }"><dd id="subDocListBtn" class="btn" onclick="findWriteDocPop('${param.tabId}', 'sendSubReload');"><u:msg titleId="dm.btn.sendSubDoc" alt="하위문서로 보내기" /></dd></c:if>
		<dd class="btn" onclick="sendSubReload(null);"><u:msg titleId="dm.btn.restore" alt="되돌리기" /></dd>
		<dd class="btn" onclick="saveWrite();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
		<dd class="btn" onclick="history.go(-1);"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
		</dl>
		</div>
	</div>
</u:secu>
<c:set var="voMap" value="${dmDocLVoMap }" scope="request"/>
<form id="setForm" method="post" enctype="multipart/form-data" >
<m:input type="hidden" id="menuId" value="${menuId}"/>
<m:input type="hidden" id="tabId" value="${param.tabId}"/>
<m:input type="hidden" id="docTyp" value="${param.docTyp}"/>
<!-- 저장소ID -->
<c:if test="${!empty paramStorId }">
<m:input type="hidden" id="paramStorId" value="${paramStorId}" />
</c:if>
<!-- 원본문서구분에 따른 파라미터 세팅 -->
<c:if test="${param.docTyp eq 'doc' || param.docTyp eq 'psn' }"><m:input type="hidden" id="docRefId" value="${param.docId}"/></c:if>
<c:if test="${param.docTyp eq 'brd' }"><m:input type="hidden" id="brdId" value="${param.brdId}"/><m:input type="hidden" id="bullId" value="${param.bullId}"/></c:if>
<c:if test="${param.docTyp eq 'apv' }"><m:input type="hidden" id="apvId" value="${param.apvId}"/></c:if>
<m:input type="hidden" id="dialog" value="setSendWritePop"/>
<c:choose>
	<c:when test="${param.tabId eq 'doc' }"><!-- 공용폴더 -->
		<m:input type="hidden" id="listPage" value="${urlPrefix}/${listPage}.do?${nonPageParams}" />
		<u:set var="viewPage" test="${!empty dmDocLVoMap.statCd && dmDocLVoMap.statCd eq 'T'}" value="${urlPrefix}/${listPage}.do?${nonPageParams}" elseValue="${urlPrefix}/${viewPage}.do?${viewPageParams}${includeParams }"/>
		
		<c:if test="${!empty param.docPid }"><m:input type="hidden" id="docPid" value="${param.docPid }" /></c:if>
		<u:set var="colgroup" test="${!empty param.docPid }" value="15%," elseValue="15%,35%,15%,35%"/>
		
		<m:input type="hidden" id="viewPage" value="${viewPage }" />
		<textarea id="cont" name="cont" style="display:none">${_bodyHtml}</textarea>
    	<input type="hidden" name="bumkId" />
		
		<div class="entryzone">
        <div class="entryarea">
        <dl>
        <c:if test="${!empty param.docPid }"><dd class="etr_blank"></dd><dd class="etr_input"><div class="etr_body_blue"><u:msg titleId="dm.msg.sendWrite.subDoc" arguments="${dmDocLVoMap.fldNm }" alt="'{0}' 폴더로 저장합니다."/></div></dd></c:if>
        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.subj" alt="제목" /></dd>
        <dd class="etr_input"><div class="etr_inputin"><m:input id="subj" titleId="cols.subj" value="${dmDocLVoMap.subj}" maxByte="240" mandatory="Y" style="width:95%;"/></div></dd>
		
		<u:set var="setDisabled" test="${!empty param.docPid }" value="Y" elseValue="N"/>
		<c:if test="${setDisabled eq 'N' }">
			<dd class="etr_bodytit_asterisk"><u:msg titleId="cols.fld" alt="폴더" /></dd>
			<dd class="etr_input">
				<div class="etr_ipmany">
				<dl>
					<dd class="etr_ip_lt">
						<div id="fldInfoArea" >
							<div id="idArea" style="display:none;"><input type="hidden" id="fldId" name="fldId" value="${dmDocLVoMap.fldId}" /></div>
							<div id="nmArea" style="display:inline;"><m:input type="text" id="fldNm" name="fldNm" className="etr_iplt" value="${dmDocLVoMap.fldNm}" readonly="Y" mandatory="Y" /></div>
		              	</div>
					</dd>
                    <c:if test="${empty param.docPid && (empty param.docId || dmDocLVoMap.statCd eq 'T')}"><dd class="etr_se_rt" ><div class="etr_btn" onclick="findSelPop('F');"><u:msg titleId="dm.btn.fldSel" alt="폴더선택"/></div></dd></c:if>
				</dl>
				</div>
			</dd>
		</c:if>
			<dd class="etr_bodytit_asterisk"><u:msg titleId="cols.cls" alt="분류" /></dd>
			<dd class="etr_input">
				<div class="etr_ipmany">
				<dl>
				<dd class="etr_ip_lt">
					<div id="clsInfoArea" >
						<div id="idArea" style="display:none;">
							<c:set var="clsNmTmp" />
							<c:forEach var="clsVo" items="${dmClsBVoList }" varStatus="status">
								<input type="hidden" id="clsId" name="clsId" value="${clsVo.clsId}" />
								<c:set var="clsNmTmp" value="${clsNmTmp}${status.count > 1 ? ',' : '' }${clsVo.clsNm }"/>
							</c:forEach>
						</div>
						<div id="nmArea" style="display:inline;"><m:input type="text" name="clsNm" id="clsNm" className="etr_iplt" value="${clsNmTmp}" style="width:55%;" readonly="Y" mandatory="Y"/></div>
	              	</div>
				</dd>
				<dd class="etr_se_rt" ><div class="etr_btn" onclick="findSelPop('C');"><u:msg titleId="dm.btn.clsSel" alt="분류선택"/></div></dd>
				</dl>
				</div>
			</dd>
		
		<dd class="etr_bodytit"><u:msg titleId="dm.cols.kwd" alt="키워드" /></dd>
        <dd class="etr_input"><div class="etr_inputin">
        <c:set var="kwnNms" />
		<c:forEach var="kwdVo" items="${dmKwdLVoList }" varStatus="status"><c:set var="kwnNms" value="${kwnNms }${status.count > 1 ? ',' : ''}${kwdVo.kwdNm }"/></c:forEach>
		<input type="text" id="kwdNm" name="kwdNm" class="etr_iplt" value="${kwnNms}" maxlength="55"/>
        </div></dd>
        
        <dd class="etr_bodytit"><u:msg titleId="dm.cols.keepPrd" alt="보존연한" /></dd>
        <dd class="etr_select" id="docKeepPrdContainer">
        	<c:set var="docKeepPrdNm" value=""/>
        	<c:set var="docKeepPrdCd" value=""/>
            <div class="etr_open1" id="docKeepPrdOpen" style="display:none">
                <div class="open_in1">
                    <div class="open_div">
                    <dl>
                    <c:forEach items="${itemDispMap.docKeepPrdNm.colmVo.cdList}" var="cd" varStatus="status">
                    <c:if test="${(empty dmDocLVoMap.docKeepPrdCd && status.count == 1) || (cd.cdId == dmDocLVoMap.docKeepPrdCd)}"><c:set var="docKeepPrdNm" value="${cd.rescNm }"/><c:set var="docKeepPrdCd" value="${cd.cdId }"/></c:if>
					<c:if test="${status.count>1 }"><dd class="line"></dd></c:if>
                    <dd class="txt" onclick="setSelOptions('docKeepPrd',$(this).attr('data-code'),$(this).text());" data-code="${cd.cdId}">${cd.rescNm }</dd>
                    </c:forEach>
                 </dl>
                    </div>
                </div>
            </div>
            <input type="hidden" name="docKeepPrdCd" value="${docKeepPrdCd }"/>
            <div class="etr_ipmany">
                <div class="select_in1" onclick="holdHide = true; $('#docKeepPrdContainer #docKeepPrdOpen').toggle();">
                <dl>
                <dd class="select_txt" id="selectView"><span>${docKeepPrdNm }</span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </div>
        </dd>
        <c:if test="${setDisabled eq 'N' }">
        <dd class="etr_bodytit"><u:msg titleId="dm.cols.secul" alt="보안등급" /></dd>
        <dd class="etr_select" id="seculCdContainer">
        	<c:set var="seculNm" value=""/>
        	<c:set var="seculCd" value=""/>
            <div class="etr_open1" id="seculCdOpen" style="display:none">
                <div class="open_in1">
                    <div class="open_div">
                    <dl>
                    <dd class="txt" onclick="setSelOptions('seculCd',$(this).attr('data-code'),$(this).text());" data-code="none"><u:msg var="noSelect" titleId="cm.option.noSelect" alt="선택안함" />${noSelect }</dd>
                    <c:if test="${empty dmDocLVoMap.docId }"><c:set var="seculNm" value="${noSelect }"/></c:if>
                    <c:forEach items="${itemDispMap.seculNm.colmVo.cdList}" var="cd" varStatus="status">
                    <c:if test="${(empty dmDocLVoMap.seculCd && status.count == 1 && empty seculNm) || (cd.cdId == dmDocLVoMap.seculCd)}"><c:set var="seculNm" value="${cd.rescNm }"/><c:set var="seculCd" value="${cd.cdId }"/></c:if>
					<c:if test="${status.count>0 }"><dd class="line"></dd></c:if>
                    <dd class="txt" onclick="setSelOptions('seculCd',$(this).attr('data-code'),$(this).text());" data-code="${cd.cdId}">${cd.rescNm }</dd>
                    </c:forEach>
                 </dl>
                    </div>
                </div>
            </div>
            <input type="hidden" name="seculCd" value="${seculCd }"/>
            <div class="etr_ipmany">
                <div class="select_in1" onclick="holdHide = true; $('#seculCdContainer #seculCdOpen').toggle();">
                <dl>
                <dd class="select_txt" id="selectView"><span>${seculNm }</span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </div>
        </dd>
        </c:if>
        <dd class="etr_bodytit_asterisk"><u:msg titleId="dm.cols.ownr" alt="소유자" /></dd>
		<dd class="etr_input">
			<div class="etr_ipmany">
			<dl>
			<dd class="etr_ip_lt">
				<div id="ownrInfoArea" >
					<div id="idArea" style="display:none;"><input type="hidden" id="ownrUid" name="ownrUid" value="${dmDocLVoMap.ownrUid }"/></div>
					<div id="nmArea" style="display:inline;"><m:input type="text" id="ownrNm" name="ownrNm" className="etr_iplt" titleId="dm.cols.ownr" value="${dmDocLVoMap.ownrNm }" style="width:55%;" readonly="Y" mandatory="Y"/></div>
              	</div>
			</dd>
			<dd class="etr_se_rt" ><div class="etr_btn" onclick="openSingleUser('ownr');"><u:msg titleId="dm.cols.ownr" alt="소유자" /></div></dd>
			</dl>
			</div>
		</dd>
		<dd class="etr_bodytit"><u:msg titleId="dm.cols.bumk" alt="즐겨찾기" /></dd>
        <dd class="etr_select" id="bumkNmContainer">
        	<c:set var="bumkNm" value=""/>
            <div class="etr_open1" id="bumkNmOpen" style="display:none">
                <div class="open_in1">
                    <div class="open_div">
                    <dl>
                    <c:forEach var="bumkVo" items="${dmBumkBVoList }" varStatus="status">
                    <c:if test="${status.count == 1}"><c:set var="bumkNm" value="${bumkVo.bumkNm }"/></c:if>
					<c:if test="${status.count>1 }"><dd class="line"></dd></c:if>
                    <dd class="txt" onclick="setSelOptions('bumkNm',$(this).attr('data-code'),$(this).text());" data-code="${bumkVo.bumkId}">${bumkVo.bumkNm }</dd>
                    </c:forEach>
                 </dl>
                    </div>
                </div>
            </div>
            
            <div class="etr_ipmany">
                <div class="select_in1" onclick="holdHide = true; $('#bumkNmContainer #bumkNmOpen').toggle();">
                <dl>
                <dd class="select_txt" id="selectView"><span>${bumkNm }</span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </div>
        </dd>
		</dl>
		</div>
	</div>
	<div class="entryzone" id="listAddItemArea"></div>
	
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
         <dd class="etr_input"><div class="etr_bodyline editor" id="bodyHtmlArea"><c:if test="${!empty _bodyHtml }"><u:out value="${_bodyHtml}" type="noscript" /></c:if></div></dd>

		</dl>
		</div>
	</div>
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
       <m:files id="${filesId}" fileVoList="${fileVoList}" module="bb" mode="set" exts="${exts }" extsTyp="${extsTyp }"/>
	</c:when>
	<c:when test="${param.tabId eq 'brd' }"><!-- 게시판 -->
		<u:out value="${bbBullLVo.bullRezvDt}" type="date" var="bullRezvYmd" />
		<u:out value="${bbBullLVo.bullRezvDt}" type="hm" var="bullRezvHm" />
		<u:out value="${bbBullLVo.bullExprDt}" type="date" var="bullExprYmd" />
		<u:out value="${bbBullLVo.bullExprDt}" type="hm" var="bullExprHm" />

		<m:input type="hidden" id="listPage" value="./${listPage}.do?${paramsForList}" />
		<m:input type="hidden" id="viewPage" value="./${viewPage}.do?${params}&bullId=${param.bullId}" />
		<m:input type="hidden" id="brdId" value="${baBrdBVo.brdId}" />
		<m:input type="hidden" id="bullId" value="${param.bullId}" />
		<m:input type="hidden" id="bullPid" value="${param.bullPid}" />
		<m:input type="hidden" id="bullStatCd" value="B" />
		<m:input type="hidden" id="bullRezvDt" value="${bbBullLVo.bullRezvDt}" />
		<m:input type="hidden" id="tgtDeptYn" value="${bbBullLVo.tgtDeptYn}" />
		<m:input type="hidden" id="tgtUserYn" value="${bbBullLVo.tgtUserYn}" />
		<textarea id="cont" name="cont" style="display:none">${_bodyHtml}</textarea>
		<input type="hidden" name="catId" id="catId" value="" />
		
		<div id="bbTgtDeptHidden">
	<c:if test="${bullTgtDeptVoList != null}">
		<c:forEach items="${bullTgtDeptVoList}" var="baBullTgtDVo" varStatus="status">
		<input name="orgId" type="hidden" value="${baBullTgtDVo.tgtId}" />
		<input name="withSubYn" type="hidden" value="${baBullTgtDVo.withSubYn}" />
		</c:forEach>
	</c:if></div>
	<div id="bbTgtUserHidden">
	<c:if test="${bullTgtUserVoList != null}">
		<c:forEach items="${bullTgtUserVoList}" var="baBullTgtDVo" varStatus="status">
		<input name="userUid" type="hidden" value="${baBullTgtDVo.tgtId}" />
		</c:forEach>
	</c:if></div>
	

   
    <div class="entryzone">
        <div class="entryarea">
        <dl>

        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.subj" alt="제목" /></dd>
        <dd class="etr_input"><div class="etr_inputin"><m:input id="subj" titleId="cols.subj" style="width:98%;" value="${dmDocLVoMap.subj}" mandatory="Y" maxByte="240"/></div></dd>
		
		<c:if test="${baBrdBVo.catYn == 'Y' && param.bullPid == null}">
		<dd class="etr_bodytit"><u:msg titleId="cols.cat" alt="카테고리" /></dd>
		<dd class="etr_select">
             <div class="etr_open1 schOpnLayer2" style="display:none;">
                <div class="open_in1">
                    <div class="open_div">
                    <dl>
                    <dd class="txt" onclick="javascript:fnSetCatId('');" data-schCd=""><u:msg titleId="cm.option.all" alt="전체" /></dd>
					<dd class="line"></dd>
                    <c:forEach items="${baCatDVoList}" var="catVo" varStatus="status">
				        <dd class="txt" onclick="javascript:fnSetCatId('${catVo.catId}');" data-schCd="${catVo.catId}">${catVo.rescNm}</dd>
				        <dd class="line"></dd>
					</c:forEach>
                 </dl>
                    </div>
                </div>
            </div>
            
            <div class="etr_ipmany">
                <div class="select_in1 schTxt2" onclick="holdHide2 = true;$('.schOpnLayer2').toggle();">
                <dl>
                <dd class="select_txt"><span></span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </div>
        </dd>
		</c:if>
		
        <dd class="etr_bodytit"><u:msg titleId="bb.cols.bbOpt" alt="게시옵션" /></dd>
        <dd class="etr_input">
			<div class="etr_ipmany">
                <dl>
					<u:set var="checked" test="${bbBullLVo.secuYn == 'Y'}" value="true" elseValue="false"/>
					<m:check type="checkbox" id="secuYn" name="secuYn" inputId="secuYn" value="Y" checked="${checked }" titleId="bb.option.secu" />
					
					<u:set var="checked" test="${bbBullLVo.ugntYn == 'Y'}" value="true" elseValue="false"/>
					<m:check type="checkbox" id="ugntYn" name="ugntYn" inputId="ugntYn" value="Y" checked="${checked }" titleId="bb.option.ugnt" />
					
					<c:if test="${(param.bullPid == null || param.bullPid == '') && bbBullLVo.bullPid == null}">
					<u:set var="checked" test="${bbBullLVo.notcYn == 'Y'}" value="true" elseValue="false"/>
					<m:check type="checkbox" id="notcYn" name="notcYn" inputId="notcYn" value="Y" checked="${checked }" titleId="bb.option.notc" />
					</c:if>
					
                </dl>
            </div>
        </dd>
		
		<c:if test="${baBrdBVo.kndCd == 'R' && param.bullPid == null && bbBullLVo.bullPid == null}">
        <dd class="etr_bodytit"><u:msg titleId="cols.bullExprDt" alt="게시완료일" /></dd>
        <dd class="etr_select">
             <div class="etr_calendar">
             	<input id="bullExprHm" name="bullExprHm" value="${bullExprHm}" type="hidden" />
             	<input id="bullExprYmd" name="bullExprYmd" value="${bullExprYmd}" type="hidden" />
             	<input type="hidden" name="bullExprDt" id="bullExprDt" value="${bbBullLVo.bullExprDt}" />
             	<div class="etr_calendarin">
                <dl>
                <dd class="ctxt" onclick="fnCalendar('bullExprYmd','','m','bullExprHm');"><span id="bullExprYmd">${bullExprYmd} ${bullExprHm}</span></dd>
                <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                <dd class="cbtn" onclick="fnCalendar('bullExprYmd','','m','bullExprHm');"></dd>
                </dl>
                </div>
             </div>
        </dd> 
		</c:if>
		
		<dd class="etr_bodytit"><u:msg titleId="cols.bullRezvDt" alt="게시예약일" /></dd>
		<c:if test="${bullRezvDtYn == false}">
			<c:if test="${bbBullLVo.bullRezvYn == 'Y'}"><dd class="etr_bodytit"><u:out value="${bbBullLVo.bullRezvDt}" type="longdate" /></dd></c:if>
		</c:if>
		<c:if test="${bullRezvDtYn == true}">
		<input type="hidden" id="bullRezvYn" name="bullRezvYn" value="${bullRezvChecked == true ? 'Y' : 'N' }" />
        <dd class="etr_input">
			<div class="etr_ipmany">
                <dl>
					<u:set var="checked" test="${bullRezvChecked == true}" value="true" elseValue="false"/>
					<m:check type="checkbox" id="bullRezvYnChkArea" name="bullRezvYnChk" inputId="bullRezvYnChk" value="Y" checked="${checked }" titleId="bb.cols.bullRezv" onclick="fnbullRezvYnChk();" />
                </dl>
            </div>
        </dd>
		</c:if>
        <dd class="etr_select bullRezvCalLayer" style="display:none">
             <div class="etr_calendar">
             	<input id="bullRezvHm" name="bullRezvHm" value="${bullRezvHm}" type="hidden" />
             	<input id="bullRezvYmd" name="bullRezvYmd" value="${bullRezvYmd}" type="hidden" />
             	<div class="etr_calendarin">
                <dl>
                <dd class="ctxt" onclick="fnCalendar('bullRezvYmd','','m','bullRezvHm','onBullRezvDayChange');"><span id="bullRezvYmd">${bullRezvYmd} ${bullRezvHm}</span></dd>
                <dd class="cdelete" onclick="fnTxtDelete(this);"></dd>
                <dd class="cbtn" onclick="fnCalendar('bullRezvYmd','','m','bullRezvHm','onBullRezvDayChange');"></dd>
                </dl>
                </div>
             </div>
        </dd> 
        
		
	<c:forEach items="${baColmDispDVoList}" var="baColmDispDVo" varStatus="status">
		<c:set var="colmVo" value="${baColmDispDVo.colmVo}" />
		<c:set var="colmNm" value="${colmVo.colmNm.toLowerCase()}" />
		<c:set var="colmTyp" value="${colmVo.colmTyp}" />
		<c:set var="colmTypVal" value="${colmVo.colmTypVal}" />
	<c:if test="${baColmDispDVo.useYn == 'Y' && colmVo.exColmYn == 'Y'}">
	<c:if test="${(fnc == 'reg' && baColmDispDVo.regDispYn == 'Y') || (fnc == 'mod' && baColmDispDVo.modDispYn == 'Y')}">
	<dd class="etr_bodytit"><span>${colmVo.rescNm}</span></dd>
		<!-- 확장컬럼의 데이터 최대길이(byte) [input,textarea만 해당] -->
		<u:set var="dataMaxByte" test="${(colmTyp == 'TEXT' || colmTyp == 'TEXTAREA') && colmVo.dataTyp eq 'VARCHAR' && !empty colmVo.colmLen }" value="${colmVo.colmLen }" elseValue="100"/>
		<c:if test="${colmTyp == 'TEXT'}">
			<dd class="etr_input"><div class="etr_inputin"><input type="text" id="${colmNm}" name="${colmNm}" class="etr_iplt" value="${bbBullLVo.getExColm(colmVo.colmNm)}" maxlength="${dataMaxByte }"/></div></dd>
		</c:if>
		<c:if test="${colmTyp == 'TEXTAREA'}">
			<dd class="etr_input"><div class="etr_textareain"><textarea rows="3" id="${colmNm}" name="${colmNm}" class="etr_ta">${bbBullLVo.getExColm(colmVo.colmNm)}</textarea></div></dd>
		</c:if>
		<c:if test="${colmTyp == 'PHONE'}">
			<dd class="etr_input"><div class="etr_inputin"><input type="text" id="${colmNm}" name="${colmNm}" class="etr_iplt" value="${bbBullLVo.getExColm(colmVo.colmNm)}" /></div></dd>
		</c:if>
		<c:if test="${colmTyp == 'CALENDAR'}">			
	        <dd class="etr_select">
	             <div class="etr_calendar">
	             	<input id="${colmNm}" name="${colmNm}" value="${bbBullLVo.getExColm(colmVo.colmNm)}" type="hidden" />
	                 <div class="select_in1" onclick="fnCalendar('${colmNm}','','','');">
	                 <dl>
	                 <dd class="ctxt"><span id="${colmNm}">${bbBullLVo.getExColm(colmVo.colmNm)}</span></dd>
	                 <dd class="cbtn"></dd>
	                 </dl>
	                 </div>
	             </div>
	        </dd> 
		</c:if>
		<c:if test="${colmTyp == 'CODE'}">
			<u:set test="${cdListIndex == null}" var="cdListIndex" value="0" elseValue="${cdListIndex + 1}" />
			<input type="hidden" name="${colmNm}" id="${colmNm}" value="${bbBullLVo.getExColm(colmVo.colmNm)}"/>
			<dd class="etr_input">
	              <div class="etr_ipmany">
	              <dl>
	              <dd class="etr_se_lt">
	                  <div class="etr_open2 colmLayer${colmNm}" style="display:none;">
	                    <div class="open_in1">
	                        <div class="open_div">
						        <dl>
						        <c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status">
							        <dd class="txt" onclick="javascript:fnSetExtSelect('${colmNm}','${cd.cd}');" data-schCd="${cd.cd}">${cd.rescNm}</dd>
							        <dd class="line"></dd>
								</c:forEach>
						    	</dl>
	                        </div>
	                    </div>
	                </div>   
	                <div class="select_in1 colmTxt${colmNm}" onclick="holdHideExt = true;$('.colmLayer${colmNm}').toggle();">
	                <dl>
	                <dd class="select_txt"><span>
			        <c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status">
						<c:if test="${cd.cd == bbBullLVo.getExColm(colmVo.colmNm)}">${cd.rescNm}</c:if>
					</c:forEach></span></dd>
	                <dd class="select_btn"></dd>
	                </dl>
	                </div>
	            </dd>
	            </dl>
	            </div>
	        </dd>				

		</c:if>
	</c:if>
	</c:if>
	</c:forEach>
		
		
		</dl>
		</div>
	</div>

    <div class="blankzone">
        <div class="blank25"></div>
        <div class="line1"></div>
        <div class="line8"></div>
        <div class="line1"></div>
        <div class="blank25"></div>
    </div>
	<!-- 게시물사진 -->
	<c:if test="${baBrdBVo.photoYn == 'Y'}">
		<!--entryzone S-->
            <div class="entryzone">
                <div class="entryarea">
                <dl>
                <dd class="etr_bodytit">
                    <div class="icotit_dot"><u:msg alt="사진 선택" titleId="or.jsp.setOrg.photoTitle" /></div>
                    <m:fileBtn id="photo" titleId="or.jsp.setOrg.photoTitle" alt="사진 선택" exts="gif,jpg,jpeg,png,tif" onchange="setFileInfo"/>
                </dd>
                </dl>
                </div>
            </div>
            <!--//entryzone E-->
            <m:file module="bb" mode="set" id="photo" />
            <div class="blank25"></div>
	</c:if>

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
         <dd class="etr_input"><div class="etr_bodyline editor" id="bodyHtmlArea"><c:if test="${!empty _bodyHtml }"><u:out value="${_bodyHtml}" type="noscript" /></c:if></div></dd>

		</dl>
		</div>
	</div>
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
       <m:files id="${filesId}" fileVoList="${fileVoList}" module="bb" mode="set" exts="${exts }" extsTyp="${extsTyp }"/>
		
	</c:when>
	<c:otherwise><!-- 개인폴더 -->
		<m:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams}" />
		<m:input type="hidden" id="viewPage" value="./${viewPage}.do?${viewPageParams}&docId=${dmDocLVoMap.docId }" />
		<m:input type="hidden" id="docId" value="${dmDocLVoMap.docId }" />
		<textarea id="cont" name="cont" style="display:none">${_bodyHtml}</textarea>
		
		<div class="entryzone">
	        <div class="entryarea">
	        <dl>
	        <dd class="etr_tit"><u:msg titleId="dm.jsp.setDoc.${!empty dmDocLVoMap.docId ? 'mod' : 'reg'}.title" alt="문서등록/문서수정" /></dd>
	        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.subj" alt="제목" /></dd>
	        <dd class="etr_input"><div class="etr_inputin"><m:input id="subj" titleId="cols.subj" value="${dmDocLVoMap.subj}" maxByte="240" mandatory="Y" style="width:95%;"/></div></dd>
			
				<dd class="etr_bodytit_asterisk"><u:msg titleId="cols.fld" alt="폴더" /></dd>
				<dd class="etr_input">
					<div class="etr_ipmany">
					<dl>
					<dd class="etr_ip_lt">
						<div id="fldInfoArea" >
							<div id="idArea" style="display:none;"><input type="hidden" id="fldId" name="fldId" value="${dmDocLVoMap.fldId}" /></div>
							<div id="nmArea" style="display:inline;"><input type="text" id="fldNm" name="fldNm" class="etr_iplt" value="${dmDocLVoMap.fldNm}" style="width:55%;" readonly="readonly"/></div>
		              	</div>
					</dd>
					<dd class="etr_se_rt" ><div class="etr_btn" onclick="findFldPop('F');"><u:msg titleId="dm.btn.fldSel" alt="폴더선택"/></div></dd>
					</dl>
					</div>
				</dd>
			</dl>
			</div>
		</div>
		<div class="entryzone" id="listAddItemArea"></div>
		
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
	         <dd class="etr_input"><div class="etr_bodyline editor" id="bodyHtmlArea"><c:if test="${!empty dmDocLVoMap.cont }"><u:out value="${dmDocLVoMap.cont}" type="noscript" /></c:if></div></dd>
	
			</dl>
			</div>
		</div>
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
	       <m:files id="${filesId}" fileVoList="${fileVoList}" module="dm" mode="set" exts="${exts }" extsTyp="${extsTyp }"/>
		
		
	</c:otherwise>
</c:choose>
<u:secu auth="W">
	<div class="blank25"></div>
	<div class="btnarea">
		<div class="size">
		<dl>
		<c:if test="${!empty param.tabId && param.tabId eq 'doc' && empty param.docPid }"><dd id="subDocListBtn" class="btn" onclick="findWriteDocPop('${param.tabId}', 'sendSubReload');"><u:msg titleId="dm.btn.sendSubDoc" alt="하위문서로 보내기" /></dd></c:if>
		<dd class="btn" onclick="sendSubReload(null);"><u:msg titleId="dm.btn.restore" alt="되돌리기" /></dd>
		<dd class="btn" onclick="saveWrite();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
		<dd class="btn" onclick="history.go(-1);"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
		</dl>
		</div>
	</div>
</u:secu>

</form>
</section>