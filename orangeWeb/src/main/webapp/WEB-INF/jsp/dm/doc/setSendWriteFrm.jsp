<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="frmYn" test="${!empty pageSuffix && pageSuffix == 'Frm' }" value="Y" elseValue="N"/>
<u:params var="nonPageParams" excludes="docId,pageNo"/>
<u:params var="viewPageParams" excludes="docId,docPid"/>
<u:set var="includeParams" test="${empty dmDocLVoMap.docId && !empty param.docPid}" value="&docId=${param.docPid }" elseValue="&docId=${dmDocLVoMap.docId }"/>
<u:params var="params" excludes="data"/>
<u:set var="urlPrefix" test="${!empty param.docTyp && param.docTyp eq 'brd'}" value="/cm/doc" elseValue="."/>
<script type="text/javascript">
<!--//오늘 시간 리턴
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
		alertMsg('cm.calendar.check.dateAI');
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
		alertMsg('cm.calendar.check.dateAI');
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
			alertMsg('cm.calendar.check.dateAI');
			$('#'+objId).val(initTime);
			setUniformCSS();
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
}<% // 게시예약 변경 이벤트 %>
function onBullRezvYnClick(checked) {
	if (checked) {
		$('#bullRezvYmdArea').show();
		$('#bullRezvHm').show();
		$('#bullRezvHm').parents('.selector').show();
		$('#bullRezvYn').val('Y');
	} else {
		$('#bullRezvYmdArea').hide();
		$('#bullRezvHm').hide();
		$('#bullRezvHm').parents('.selector').hide();
		$('#bullRezvYn').val('N');
	}
}
<% // 게시판선택 %>
function selectBb() {
	var callback = 'setBrdIds';
	var arr = '';
	var mul = (callback == 'moveBull') ? '&mul=N' : '&mul=Y';
	var params = '&brdId=${baBrdBVo.brdId}' + mul + '&callback=' + callback + '&callbackArgs=' + arr;
	if($('#brdIdList').val() != '' ) params += "&brdIds="+$('#brdIdList').val();
	dialog.open('selectBbPop','<u:msg titleId="bb.jsp.selectBb.title" alt="게시판 선택" />','./selectBbPop.do?menuId=${menuId}' + params);
	dialog.onClose('selectBbPop', function(){
		$("#subj").focus();
	});
	
	//dialog.onClose('selectBbPop', function(){
	//	$("#schWord", getIframeContent('selectBbFrm')).blur();
	//});
}
<% // 게시판선택 callback %>
function setBrdIds(brdIds, brdNms) {
	if (brdIds.indexOf('${baBrdBVo.brdId}') == -1) {
		brdIds = '${baBrdBVo.brdId},' + brdIds;
		brdNms = '${baBrdBVo.brdNm},' + brdNms;
	}
	$('#brdIdList').val(brdIds);
	$('#brdNms').text(brdNms);
}<% // 게시대상 세팅 %>
function setBbTgt() {
	$('#tgtDeptYn').val($("#bbTgtDeptHidden input[name='orgId']").length > 0 ? 'Y' : 'N');
	$('#tgtUserYn').val($("#bbTgtUserHidden input[name='userUid']").length > 0 ? 'Y' : 'N');
}
<% // 게시대상 부서 표시 %>
function setBbTgtDept(len) {
	var $nameTd = $("#bbTgtDept");
	if (len > 0) {
		$nameTd.html('<u:msg titleId="bb.btn.dept" alt="부서" /> ' + len);
		$nameTd.show();
	} else {
		$nameTd.html('');
		$nameTd.hide();
	}
}
<% // 게시대상 사용자 표시 %>
function setBbTgtUser(len) {
	var $nameTd = $("#bbTgtUser");
	if (len > 0) {
		$nameTd.html('<u:msg titleId="bb.btn.user" alt="사용자" /> ' + len);
		$nameTd.show();
	} else {
		$nameTd.html('');
		$nameTd.hide();
	}
}
<% // 다중 부서 선택 - 하위부서 여부 포함 %>
function openMuiltiOrgWithSub(mode){
	var $inputTd = $("#bbTgtDeptHidden"), data = [];<% // data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	var $subs = $inputTd.find("input[name='withSubYn']");
	$inputTd.find("input[name='orgId']").each(function(index){
		data.push({orgId:$(this).val(), withSub:$($subs[index]).val()});
	});
	<% // option : data, multi, withSub, titleId %>
	searchOrgPop({data:data, multi:true, withSub:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, orgVo){
				buffer.push("<input name='orgId' type='hidden' value='"+orgVo.orgId+"' />\n");
				buffer.push("<input name='withSubYn' type='hidden' value='"+orgVo.withSub+"' />\n");
				buffer.push("\n");
			});
			$inputTd.html(buffer.join(''));
			setBbTgtDept(arr.length);
		} else {
			$inputTd.html('');
			setBbTgtDept(0);
		}
		//return false;// 창이 안닫힘
	});
}
<% // 여러명의 사용자 선택 %>
function openMuiltiUser(mode){
	var $inputTd = $("#bbTgtUserHidden"), data = [];<% // data: 팝업 열때 오른쪽에 뿌릴 데이타 %>
	$inputTd.find("input[name='userUid']").each(function(){
		data.push({userUid:$(this).val()});
	});
	<% // option : data, multi, titleId %>
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, userVo){
				buffer.push("<input name='userUid' type='hidden' value='"+userVo.userUid+"' />\n");
				buffer.push("\n");
			});
			$inputTd.html(buffer.join(''));
			setBbTgtUser(arr.length);
		} else {
			$inputTd.html('');
			setBbTgtUser(0);
		}
	});
}<%// 폴더를 변경할 경우 페이지를 리로딩해준다.[폴더에 등록된 유형에 대한 추가항목 로드] %>
function setPageChk(prefix){
	if(prefix == 'cls') return;
	reloadFrame('listAddItemFrm', '${urlPrefix}/listAddItemFrm.do?menuId=${menuId}&fldId='+$('#fldId').val()+'&storId=${param.storId}');
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
	var url = '${urlPrefix}/findSelPop.do?menuId=${menuId}&lstTyp='+lstTyp+"&selIds="+data+"&fncMul="+(lstTyp == 'C' ? 'Y':'N');
	var msgTitle = lstTyp == 'C' ? '<u:msg titleId="dm.cols.listTyp.cls" alt="분류보기" />' : '<u:msg titleId="dm.cols.listTyp.fld" alt="폴더보기" />';
	dialog.open('findSelPop', msgTitle, url);
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
	dialog.close('findSelPop');
	setPageChk(prefix);
}<%// 폴더를 변경할 경우 페이지를 리로딩해준다.[폴더에 등록된 유형에 대한 추가항목 로드] %>
function setPsnPageChk(prefix){
	if(prefix == 'cls') return;
	reloadFrame('listAddItemFrm', './listPsnAddItemFrm.do?${params}&fldId='+$('#fldId').val());
};<%// [버튼] 분류,폴더 %>
function findFldPop(lstTyp){
	var prefix = getTabPrefix(lstTyp);
	var $area = $("#"+prefix+"InfoArea"), data = [];
	$area.find("input[id='"+prefix+"Id']").each(function(){
		data.push($(this).val());
	});
	var url = './findFldPop.do?menuId=${menuId}&lstTyp='+lstTyp+"&fldId="+data+"&fncMul="+(lstTyp == 'C' ? 'Y':'N');
	var msgTitle = lstTyp == 'C' ? '<u:msg titleId="dm.cols.listTyp.cls" alt="분류보기" />' : '<u:msg titleId="dm.cols.listTyp.fld" alt="폴더보기" />';
	dialog.open('findFldPop', msgTitle, url);
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
	dialog.close('findFldPop');
	setPsnPageChk(prefix);
}<%// 업무별 값 세팅 %>
function setValid(){
	<c:if test="${param.tabId ne 'brd'}">//게시판이 아닐경우 확장컬럼값 폼에 세팅
	// 추가항목 조회
	if(getIframeContent("listAddItemFrm").location.href.indexOf("reloadable.do")<0){
		var arrs = getIframeContent('listAddItemFrm').getChkVal();
		if(arrs != null){
			$.each(arrs,function(index,vo){
				$("#setForm").appendHidden({name:vo.name,value:vo.value});
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
function save(){
	if (validator.validate('setForm') && confirmMsg("cm.cfrm.save")) {
		setValid();
		var $form = $("#setForm");
		$form.attr('method','post');
		$form.attr('action','${urlPrefix}/${transPage}.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$('#cont').val(editor('cont').getHtml());
		saveFileToForm('${filesId}', $form[0], null);
		//$form.submit();
		return true;
	}else{
		return false;
	}
}<%// 폴더의 선택 ID 리턴 %>
function getSelectAllId(tabId){
	if(tabId != 'doc') return null;
	var fldId = $('#setForm').find('input[name="fldId"]').val();
	return fldId == '' ? null : fldId;
}
$(document).ready(function() {
	<c:if test="${param.tabId eq 'brd' && bbBullLVo.bullRezvYn != 'Y'}">
	onBullRezvYnClick(false);
	</c:if>
	<c:if test="${param.tabId ne 'brd'}">
	resizeIframe('listAddItemFrm');
	</c:if>
	setUniformCSS();
});
//-->
</script><%
	if(request.getAttribute("namoEditorEnable")==null){
		java.util.Map<String,String> dmDocLVoMap = (java.util.Map<String,String>)request.getAttribute("dmDocLVoMap");
		if(dmDocLVoMap != null){
			String _bodyHtml = com.innobiz.orange.web.cm.utils.EscapeUtil.escapeWriteableHtml(dmDocLVoMap.get("cont"));
			request.setAttribute("_bodyHtml", _bodyHtml);
		}
	} else {
		java.util.Map<String,String> dmDocLVoMap = (java.util.Map<String,String>)request.getAttribute("dmDocLVoMap");
		request.setAttribute("_bodyHtml", dmDocLVoMap.get("cont"));
	}
%>
<c:set var="voMap" value="${dmDocLVoMap }" scope="request"/>
<form id="setForm" method="post" enctype="multipart/form-data" style="padding:10px;">
<u:input type="hidden" id="menuId" value="${menuId}"/>
<u:input type="hidden" id="tabId" value="${param.tabId}"/>
<u:input type="hidden" id="docTyp" value="${param.docTyp}"/>
<!-- 저장소ID -->
<c:if test="${!empty paramStorId }">
<u:input type="hidden" id="paramStorId" value="${paramStorId}" />
</c:if>
<!-- 원본문서구분에 따른 파라미터 세팅 -->
<c:if test="${param.docTyp eq 'doc' || param.docTyp eq 'psn' }"><u:input type="hidden" id="docRefId" value="${param.docId}"/></c:if>
<c:if test="${param.docTyp eq 'brd' }"><u:input type="hidden" id="brdId" value="${param.brdId}"/><u:input type="hidden" id="bullId" value="${param.bullId}"/></c:if>
<c:if test="${param.docTyp eq 'apv' }"><u:input type="hidden" id="apvId" value="${param.apvId}"/></c:if>
<u:input type="hidden" id="dialog" value="setSendWritePop"/>
<c:choose>
	<c:when test="${param.tabId eq 'doc' }"><!-- 공용폴더 -->
		<u:input type="hidden" id="listPage" value="${urlPrefix}/${listPage}.do?${nonPageParams}" />
		<u:set var="viewPage" test="${!empty dmDocLVoMap.statCd && dmDocLVoMap.statCd eq 'T'}" value="${urlPrefix}/${listPage}.do?${nonPageParams}" elseValue="${urlPrefix}/${viewPage}.do?${viewPageParams}${includeParams }"/>
		
		<c:if test="${!empty param.docPid }"><u:input type="hidden" id="docPid" value="${param.docPid }" /></c:if>
		<u:set var="colgroup" test="${!empty param.docPid }" value="15%," elseValue="15%,35%,15%,35%"/>
		
		<u:input type="hidden" id="viewPage" value="${viewPage }" />
		<c:if test="${!empty param.docPid }">
			<div class="color_txt"><u:msg titleId="dm.msg.sendWrite.subDoc" arguments="${dmDocLVoMap.fldNm }" alt="'{0}' 폴더로 저장합니다."/></div>
		</c:if>
		<u:set var="setDisabled" test="${!empty param.docPid }" value="Y" elseValue="N"/>
		<u:listArea colgroup="${colgroup }" noBottomBlank="true">
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
			<td class="bodybg_lt" colspan="3"><u:input id="subj" titleId="cols.subj" value="${dmDocLVoMap.subj}" maxByte="240" mandatory="Y" style="width:95%;"/></td>
		</tr>
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.fld" alt="폴더" /></td>
			<td class="bodybg_lt" >
				<div id="fldInfoArea" style="display:inline;">
					<div id="idArea" style="display:none;"><u:input type="hidden" id="fldId" value="${dmDocLVoMap.fldId }" disabled="${setDisabled }"/></div>
					<div id="nmArea" style="display:inline;"><u:input id="fldNm" titleId="cols.fld" value="${dmDocLVoMap.fldNm}" mandatory="Y" style="width:55%;" readonly="Y" disabled="${setDisabled }"/></div>
				</div>
				<c:if test="${empty dmDocLVoMap.docPid && (empty param.docId || dmDocLVoMap.statCd eq 'T')}"><u:buttonS titleId="dm.btn.fldSel" alt="폴더 선택" onclick="findSelPop('F');" /></c:if>
			</td>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.cls" alt="분류" /></td>
			<td class="bodybg_lt" >
				<c:set var="clsNmTmp" />
				<div id="clsInfoArea" style="display:inline;">
					<div id="idArea" style="display:none;">
						<c:forEach var="clsVo" items="${dmClsBVoList }" varStatus="status">
							<u:input type="hidden" id="clsId" value="${clsVo.clsId }"/>
							<c:set var="clsNmTmp" value="${clsNmTmp}${status.count > 1 ? ',' : '' }${clsVo.clsNm }"/>
						</c:forEach>
					</div>
					<div id="nmArea" style="display:inline;"><u:input id="clsNm" titleId="cols.cls" value="${clsNmTmp}" mandatory="Y" style="width:55%;" readonly="Y"/></div>
				</div>
				<u:buttonS titleId="dm.btn.clsSel" alt="분류 선택" onclick="findSelPop('C');" />
			</td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="dm.cols.kwd" alt="키워드" /></td>
			<td class="bodybg_lt" colspan="3">
				<c:set var="kwnNms" />
				<c:forEach var="kwdVo" items="${dmKwdLVoList }" varStatus="status"><c:set var="kwnNms" value="${kwnNms }${status.count > 1 ? ',' : ''}${kwdVo.kwdNm }"/></c:forEach>
				<u:input id="kwdNm" titleId="dm.cols.kwd" value="${kwnNms}" maxByte="120" style="width:80%;"/>
			</td>
		</tr>
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="dm.cols.keepPrd" alt="보존연한" /></td>
			<td class="bodybg_lt" ><select id="docKeepPrdCd" name="docKeepPrdCd"<u:elemTitle titleId="dm.cols.keepPrd" alt="보존연한" />><c:forEach 
					items="${itemDispMap.docKeepPrdNm.colmVo.cdList}" var="cd" varStatus="status">
					<option value="${cd.cdId}" <c:if test="${cd.cdId == dmDocLVoMap.docKeepPrdCd}">selected="selected"</c:if>>${cd.rescNm}</option></c:forEach>
					</select></td>
			<td class="head_lt"><u:mandatory /><u:msg titleId="dm.cols.secul" alt="보안등급" /></td>
			<td class="bodybg_lt" ><select id="seculCd" name="seculCd"<u:elemTitle titleId="dm.cols.secul" alt="보안등급" /> <c:if test="${setDisabled eq 'Y' }">disabled="disabled"</c:if>>
				<option value="none"><u:msg titleId="cm.option.noSelect" alt="선택안함" /></option><c:forEach 
					items="${itemDispMap.seculNm.colmVo.cdList}" var="cd" varStatus="status">
					<option value="${cd.cdId}" <c:if test="${cd.cdId == dmDocLVoMap.seculCd}">selected="selected"</c:if>>${cd.rescNm}</option></c:forEach>
					</select></td>
		</tr>
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="dm.cols.ownr" alt="소유자" /></td>
			<td class="bodybg_lt" colspan="3"><table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td>
						<u:input type="hidden" id="ownrUid" value="${dmDocLVoMap.ownrUid}"/>
						<u:input id="ownrNm" value="${dmDocLVoMap.ownrNm}" titleId="dm.cols.ownr" readonly="Y" mandatory="Y"/>							
					</td>
					<td><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="openSingUser();" /></td>
				</tr>
				</tbody>
			</table>
			</td>
		</tr>
		<tr>
			<td class="head_lt"><u:msg titleId="dm.cols.bumk" alt="즐겨찾기" /></td>
			<td class="bodybg_lt" colspan="3"><c:if test="${!empty dmBumkBVoList }"><u:msg var="bumkTitle" titleId="dm.cols.bumk"/><select id="bumkId" name="bumkId"<u:elemTitle titleId="dm.cols.bumk" alt="즐겨찾기" />>
				<u:option value="" title="--${bumkTitle }--"/>
				<c:forEach var="bumkVo" items="${dmBumkBVoList }" varStatus="status">
					<u:option value="${bumkVo.bumkId }" title="${bumkVo.bumkNm }" checkValue="${dmDocLVoMap.bumkId }"/>
				</c:forEach>
				</select></c:if><c:if test="${empty dmBumkBVoList }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></c:if></td>
		</tr>
		</u:listArea>
		
		<u:blank />
		<u:set var="addItemSrc" test="${!empty dmDocLVoMap.fldId}" value="${urlPrefix}/listAddItemFrm.do?${params}&fldId=${dmDocLVoMap.fldId }" elseValue="/cm/util/reloadable.do"/>
		<iframe id="listAddItemFrm" name="listAddItemFrm" src="${addItemSrc }" style="width:100%;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
		<u:editor id="cont" width="100%" height="350px" module="dm" value="${_bodyHtml}" padding="2"/>
		<u:listArea>
			<tr>
			<td>
				<u:set var="urlParam" test="${param.docTyp eq 'brd'}" value="brdId=${brdId}" elseValue=""/>
				<u:files id="${filesId}" fileVoList="${fileVoList}" module="${module }" mode="set" urlParam="${urlParam }" exts="${exts }" extsTyp="${extsTyp }"/>
			</td>
		</tr>
		</u:listArea>
	</c:when>
	<c:when test="${param.tabId eq 'brd' }"><!-- 게시판 -->
		<u:out value="${bbBullLVo.bullRezvDt}" type="date" var="bullRezvYmd" />
		<u:out value="${bbBullLVo.bullRezvDt}" type="hm" var="bullRezvHm" />
		<u:out value="${bbBullLVo.bullExprDt}" type="date" var="bullExprYmd" />
		<u:out value="${bbBullLVo.bullExprDt}" type="hm" var="bullExprHm" />

		<u:input type="hidden" id="listPage" value="./${listPage}.do?${paramsForList}" />
		<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${params}&bullId=${param.bullId}" />
		<u:input type="hidden" id="brdId" value="${baBrdBVo.brdId}" />
		<u:input type="hidden" id="bullId" value="${param.bullId}" />
		<u:input type="hidden" id="bullPid" value="${param.bullPid}" />
		<u:input type="hidden" id="bullStatCd" value="B" />
		<u:input type="hidden" id="bullRezvDt" value="${bbBullLVo.bullRezvDt}" />
		<u:input type="hidden" id="tgtDeptYn" value="${bbBullLVo.tgtDeptYn}" />
		<u:input type="hidden" id="tgtUserYn" value="${bbBullLVo.tgtUserYn}" />
		
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
		
		<% // 폼 필드 %>
		<u:set test="${bbTgtDispYn || (baBrdBVo.kndCd == 'R' && param.bullPid == null && bbBullLVo.bullPid == null)}" var="colgroup" value="18%,32%,18%,32%" elseValue="18%,82%" />
		<u:listArea colgroup="${colgroup}" noBottomBlank="true">
			<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
			<td colspan="3"><u:input id="subj" titleId="cols.subj" style="width:98%;" value="${dmDocLVoMap.subj}" mandatory="Y" maxByte="240"/></td>
			</tr>
		
			<c:if test="${baBrdBVo.brdTypCd == 'A'}">
			<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.regr" alt="등록자" /></td>
			<td colspan="3"><u:input id="anonRegrNm" titleId="cols.regr" style="width: 150px;" value="${bbBullLVo.anonRegrNm}" mandatory="Y" maxByte="30"/></td>
			</tr>
			</c:if>
		
			<c:if test="${baBrdBVo.allCompYn != 'Y'}">
			<c:if test="${bbChoiYn == true}">
				<u:set test="${brdNms != null}" var="brdNms" value="${brdNms}" elseValue="${baBrdBVo.rescNm}" />
				<u:set test="${brdIdList != null}" var="brdIdList" value="${brdIdList}" elseValue="${baBrdBVo.brdId}" />
			<tr>
			<td class="head_lt"><u:msg titleId="cols.bb" alt="게시판" /></td>
			<td colspan="3"><table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td class="body_lt"><span id="brdNms">${brdNms}</span></td>
				<td><u:buttonS titleId="bb.btn.bbChoi" alt="게시판선택" onclick="selectBb();" />
					<u:input type="hidden" id="brdIdList" value="${brdIdList}" /></td>
				</tr>
				</table></td>
			</tr>
			</c:if>
			</c:if>
		
			<c:if test="${baBrdBVo.catYn == 'Y' && param.bullPid == null}">
			<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.cat" alt="카테고리" /></td>
			<td colspan="3"><select name="catId">
				<c:forEach items="${baCatDVoList}" var="catVo" varStatus="status">
				<u:option value="${catVo.catId}" title="${catVo.rescNm}" checkValue="${bbBullLVo.catId}" />
				</c:forEach>
				</select></td>
			</tr>
			</c:if>
		
			<tr>
			<c:if test="${bbTgtDispYn}">
			<td class="head_lt"><u:msg titleId="bb.cols.bbTgt" alt="게시대상" /></td>
			<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
				<tr>
				<td id="bbTgtDept" style="display: none;" class="body_lt"></td>
				<c:if test="${empty bbTgtYn || bbTgtYn == false}">
					<td><u:buttonS titleId="bb.btn.dept" alt="부서" onclick="openMuiltiOrgWithSub()" /></td>
				</c:if>
				<td id="bbTgtUser" style="display: none;" class="body_lt"></td>
				<c:if test="${empty bbTgtYn || bbTgtYn == false}">
					<td><u:buttonS titleId="bb.btn.user" alt="사용자" onclick="openMuiltiUser()" /></td>
				</c:if>
				</tr>
				</tbody></table></td>
			</c:if>
			<u:set test="${bbTgtDispYn}" var="colspan" value="1" elseValue="3" />
			<td class="head_lt"><u:msg titleId="bb.cols.bbOpt" alt="게시옵션" /></td>
			<td colspan="${colspan}"><u:checkArea>
				<u:checkbox name="secuYn" value="Y" titleId="bb.option.secu" alt="보안" inputClass="bodybg_lt" checkValue="${bbBullLVo.secuYn}" />
				<u:checkbox name="ugntYn" value="Y" titleId="bb.option.ugnt" alt="긴급" inputClass="bodybg_lt" checkValue="${bbBullLVo.ugntYn}" />
				<c:if test="${(param.bullPid == null || param.bullPid == '') && bbBullLVo.bullPid == null}">
				<u:checkbox name="notcYn" value="Y" titleId="bb.option.notc" alt="공지" inputClass="bodybg_lt" checkValue="${bbBullLVo.notcYn}" />
				</c:if>
				</u:checkArea></td>
			</tr>
		
			<u:set test="${baBrdBVo.kndCd == 'R' && param.bullPid == null && bbBullLVo.bullPid == null}" var="colspan" value="1" elseValue="3" />
			<tr>
			<td class="head_lt"><u:msg titleId="cols.bullRezvDt" alt="게시예약일" /></td>
			<td colspan="${colspan}"><table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<c:if test="${bullRezvDtYn == false}">
				<td class="body_lt">
					<c:if test="${bbBullLVo.bullRezvYn == 'Y'}">
					<u:out value="${bbBullLVo.bullRezvDt}" type="longdate" />
					</c:if>
					</td>
				</c:if>
				<c:if test="${bullRezvDtYn == true}">
					<u:input type="hidden" id="bullRezvYn" value="${bullRezvChecked == true ? 'Y' : 'N' }" />
					<c:choose>
						<c:when test="${param.bullId != null && !empty param.bullId }">
							<u:checkbox  name="bullRezvYnChk" id="bullRezvYnChk" value="Y" titleId="bb.cols.bullRezv" alt="게시예약" checked="${bullRezvChecked}" onclick="onBullRezvYnClick(this.checked);" inputClass="bodybg_lt" />
						</c:when>
						<c:otherwise><u:checkbox name="bullRezvYnChk" id="bullRezvYnChk" value="Y" titleId="bb.cols.bullRezv" alt="게시예약" checked="${bullRezvChecked}" onclick="onBullRezvYnClick(this.checked);" inputClass="bodybg_lt" /></c:otherwise>
					</c:choose>
					<td><u:calendar id="bullRezvYmd" value="${bullRezvYmd}" option="{checkHandler:onBullRezvDayChange}"/></td>
					<td>
						<select id="bullRezvHm" name="bullRezvHm" onchange="onBullRezvTimeChange('bullRezvHm',$('#bullRezvYmd').val(),'${bullRezvHm }');">
						<c:forEach begin="0" end="23" step="1" var="hour" varStatus="status">
							<u:set test="${hour < 10}" var="hh" value="0${hour}" elseValue="${hour}" />
							<u:option value="${hh}:00" title="${hh}:00" checkValue="${bullRezvHm}" />
							<u:option value="${hh}:30" title="${hh}:30" checkValue="${bullRezvHm}" />
						</c:forEach>
						</select>
					</td>
				</c:if>
				</tr>
				</table></td>
			<c:if test="${baBrdBVo.kndCd == 'R' && param.bullPid == null && bbBullLVo.bullPid == null}">
			<td class="head_lt"><u:msg titleId="cols.bullExprDt" alt="게시완료일" /></td>
			<td><table border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td><u:calendar id="bullExprYmd" value="${bullExprYmd}" option="{checkHandler:onBullExprDayChange}"/></td>
				<td><select id="bullExprHm" name="bullExprHm" onchange="onBullRezvTimeChange('bullExprHm',$('#bullExprYmd').val(),'${bullExprHm }');">
					<c:forEach begin="0" end="23" step="1" var="hour" varStatus="status">
						<u:set test="${hour < 10}" var="hh" value="0${hour}" elseValue="${hour}" />
						<u:option value="${hh}:00" title="${hh}:00" checkValue="${bullExprHm}" />
						<u:option value="${hh}:30" title="${hh}:30" checkValue="${bullExprHm}" />
					</c:forEach>
					</select>
					<u:input type="hidden" id="bullExprDt" value="${bbBullLVo.bullExprDt}" />
					</td>
				</tr>
				</table></td>
			</c:if>
			</tr>
		
			<!-- 게시물사진 -->
			<c:if test="${baBrdBVo.photoYn == 'Y'}">
			<tr>
			<td class="head_lt"><u:msg alt="사진 선택" titleId="or.jsp.setOrg.photoTitle" /></td>
			<td colspan="3"><table border="0" cellpadding="0" cellspacing="0"><tbody>
				<tr>
				<td><u:file id="photo" titleId="or.jsp.setOrg.photoTitle" alt="사진 선택" exts="gif,jpg,jpeg,png,tif" /></td>
				</tr>
				
				<c:if test="${bbBullLVo.photoVo != null}">
				<c:set var="maxWdth" value="200" />
				<u:set test="${bbBullLVo.photoVo.imgWdth <= maxWdth}" var="imgWdth" value="${bbBullLVo.photoVo.imgWdth}" elseValue="${maxWdth}" />
				<tr>
				<td><img src="${_cxPth}${bbBullLVo.photoVo.savePath}" width="${imgWdth}"></td>
				</tr>
				</c:if>
				</tbody></table></td>
			</tr>
			</c:if>
		
			<!-- 확장컬럼 -->
			<c:forEach items="${baColmDispDVoList}" var="baColmDispDVo" varStatus="status">
				<c:set var="colmVo" value="${baColmDispDVo.colmVo}" />
				<c:set var="colmNm" value="${colmVo.colmNm.toLowerCase()}" />
				<c:set var="colmTyp" value="${colmVo.colmTyp}" />
				<c:set var="colmTypVal" value="${colmVo.colmTypVal}" />
			<c:if test="${baColmDispDVo.useYn == 'Y' && colmVo.exColmYn == 'Y'}">
			<c:if test="${(fnc == 'reg' && baColmDispDVo.regDispYn == 'Y') || (fnc == 'mod' && baColmDispDVo.modDispYn == 'Y')}">
			<tr>
			<td class="head_lt">${colmVo.rescNm}</td>
			<td colspan="3">
				<!-- 확장컬럼의 데이터 최대길이(byte) [input,textarea만 해당] -->
				<u:set var="dataMaxByte" test="${(colmTyp == 'TEXT' || colmTyp == 'TEXTAREA') && colmVo.dataTyp eq 'VARCHAR' && !empty colmVo.colmLen }" value="${colmVo.colmLen }" elseValue="100"/>
				<c:if test="${colmTyp == 'TEXT'}"><u:input id="${colmNm}" value="${bbBullLVo.getExColm(colmVo.colmNm)}" titleId="cols.${colmNm}" maxByte="${dataMaxByte }" style="width: 98%;" /></c:if>
				<c:if test="${colmTyp == 'TEXTAREA'}"><u:textarea id="${colmNm}" value="${bbBullLVo.getExColm(colmVo.colmNm)}" titleId="cols.${colmNm}" maxByte="${dataMaxByte }" style="width:98%;" rows="${colmTypVal}" /></c:if>
				<c:if test="${colmTyp == 'PHONE'}"><u:phone id="${colmNm}" value="${bbBullLVo.getExColm(colmVo.colmNm)}" titleId="cols.${colmNm}" /></c:if>
				<c:if test="${colmTyp == 'CALENDAR'}"><u:calendar id="${colmNm}" value="${bbBullLVo.getExColm(colmVo.colmNm)}" titleId="cols.${colmNm}" /></c:if>
				<c:if test="${colmTyp == 'CODE'}">
					<u:set test="${cdListIndex == null}" var="cdListIndex" value="0" elseValue="${cdListIndex + 1}" />
					<select name="${colmNm}">
					<c:forEach items="${cdList[cdListIndex]}" var="cd" varStatus="status">
					<option value="${cd.cdId}" <c:if test="${cd.cdId == bbBullLVo.getExColm(colmVo.colmNm)}">selected="selected"</c:if>>${cd.rescNm}</option>
					</c:forEach>
					</select>
				</c:if>
				</td>
			</tr>
			</c:if>
			</c:if>
			</c:forEach>
		</u:listArea>
		<u:editor id="cont" width="100%" height="450px" module="dm" value="${_bodyHtml}" padding="2"/>
		<u:listArea>
			<tr>
			<td><u:files id="${filesId}" fileVoList="${fileVoList}" module="dm" mode="set" exts="${exts }" extsTyp="${extsTyp }"/></td>
			</tr>
		</u:listArea>
	</c:when>
	<c:otherwise><!-- 개인폴더 -->
		<u:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams}" />
		<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${viewPageParams}&docId=${dmDocLVoMap.docId }" />
		<u:input type="hidden" id="docId" value="${dmDocLVoMap.docId }" />
		<c:set var="colgroup" value="15%,"/>
		<u:listArea colgroup="${colgroup }" noBottomBlank="true">
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
			<td class="bodybg_lt" colspan="3"><u:input id="subj" titleId="cols.subj" value="${dmDocLVoMap.subj}" maxByte="240" mandatory="Y" style="width:95%;"/></td>
		</tr>
		<tr>
			<td class="head_lt"><u:mandatory /><u:msg titleId="cols.fld" alt="폴더" /></td>
			<td class="bodybg_lt" >
				<div id="fldInfoArea" style="display:inline;">
					<div id="idArea" style="display:none;"><u:input type="hidden" id="fldId" value="${dmDocLVoMap.fldId }"/></div>
					<div id="nmArea" style="display:inline;"><u:input id="fldNm" titleId="cols.fld" value="${dmDocLVoMap.fldNm}" mandatory="Y" style="width:55%;" readonly="Y"/></div>
				</div>
				<u:buttonS titleId="dm.btn.fldSel" alt="폴더 선택" onclick="findFldPop('F');" />
			</td>
		</tr>
		</u:listArea>
		<u:blank />
		<u:set var="addItemSrc" test="${!empty dmDocLVoMap.fldId}" value="./listPsnAddItemFrm.do?${params}&fldId=${dmDocLVoMap.fldId }" elseValue="/cm/util/reloadable.do"/>
		<iframe id="listAddItemFrm" name="listAddItemFrm" src="${addItemSrc }" style="width:100%;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
		<u:editor id="cont" width="100%" height="450px" module="dm" value="${_bodyHtml}" padding="2"/>
		<u:listArea>
			<tr>
			<td>
				<u:files id="${filesId}" fileVoList="${fileVoList}" module="dm" mode="set" exts="${exts }" extsTyp="${extsTyp }"/>
			</td>
		</tr>
		</u:listArea>
	</c:otherwise>
</c:choose>


</form>