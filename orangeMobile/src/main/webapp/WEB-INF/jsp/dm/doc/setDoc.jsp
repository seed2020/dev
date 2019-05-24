<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="nonPageParams" excludes="docId,pageNo,docPid,setDocId,noCache"/>
<u:params var="viewPageParams" excludes="docId,docPid,setDocId,noCache"/>
<u:set var="includeParams" test="${empty dmDocLVoMap.docId && !empty dmDocLVoMap.docPid}" value="&docId=${dmDocLVoMap.docPid }" elseValue="&docId=${dmDocLVoMap.docId }"/>
<script type="text/javascript">
//<![CDATA[
var saves = false; 
<%// 달력 클릭 %>
function fnCalendar(id,opt){
	$m.dialog.open({
		id:id,
		noPopbody:true,
		cld:true,
		url:'/cm/util/getCalendarPop.do?id='+id+'&opt='+opt+'&val='+$('#'+id).val()+'&calStyle=m',
	});
}
<%// [버튼] 확인 %>
function setCfrmPop(){
	// 신규문서일 경우에 심의여부를 확인한다.
	<c:if test="${empty dmDocLVoMap.docId || (!empty dmDocLVoMap.docId && dmDocLVoMap.statCd ne 'C')}">
	var discYn = getDiscYn();
	if(discYn == 'Y'){
		saves = true;
		save();
		return;
	}
	</c:if>
	var url = '/dm/doc/setDocCfrmPop.do?${paramsForList}&docId=${dmDocLVoMap.docId}&docNoMod=${authMap.docNoMod}';
	url+='&fldId='+$('#setForm #fldId').val();
	$m.dialog.open({
		id:'setCfrmDialog',
		title:'<u:msg titleId="cm.btn.confirm" alt="확인" />',
		url:url,
	});
}<%// [확인] 저장 %>
function saveCfrmOk(arrs){
	var $form = $("#setForm");
	$.each(arrs,function(index,vo){
		$form.find("[name='"+vo.name+"']").remove();
		$form.appendHidden({name:vo.name,value:vo.value});
	});
	saves = true;
	save();
}<%// [버튼] 임시저장 %>
function tmpSave(){
	var $form = $("#setForm");
	if($form.find("input[name='subj']").val() == ''){
		$m.msg.alertMsg('cm.input.check.mandatory',['<u:msg titleId="cols.subj" alt="제목" />']);
		$form.find("input[name='subj']").focus();
		return;
	}
	if($form.find("input[name='fldId']").val() == ''){
		$m.msg.alertMsg('cm.input.check.mandatory',['<u:msg titleId="cols.fld" alt="폴더" />']);
		$form.find("input[name='fldNm']").focus();
		return;
	}
	$form.appendHidden({name:'statCd',value:'T'});
	saves = true;
	save(true);
}<%// [버튼] 저장 %>
function save(isValid){
	if(!isValid && !validator.validate('setForm')){
		saves = false;
		return;
	}
	if(!saves) {
		setCfrmPop();
		return;
	}
	if(saves){
		$m.msg.confirmMsg("cm.cfrm.save", null, function(result){
			if(result){
				var $form = $('#setForm');
				// 추가항목 조회
				if($('#listAddItemArea').html() != ''){
					var arrs = getChkVal();
					if(arrs != null){
						$.each(arrs,function(index,vo){
							//$form.find("[name='"+vo.name+"']").remove();
							$form.appendHidden({name:vo.name,value:vo.value});
						});	
					}
				}
				$form.attr('method','post');
				$form.attr('action','/dm/doc/${transPage}Post.do?menuId=${menuId}');
				
				$m.nav.post($form);
			}else{
				saves = false;
			}
		});
	}
}<%// [하단버튼:저장] %>
function saveDoc(){
	var $form = $("#setForm");
	$form.find("[name='setPage']").remove();
	save(false);
}<%// [하단버튼:계속등록] %>
function saveContinue(){
	var $form = $("#setForm");
	$form.find("[name='setPage']").remove();
	$form.appendHidden({name:'setPage',value:'/dm/doc/${setPage}.do?${nonPageParams}'});
	save();
}
function getEditHtml(){
	return $('#bodyHtmlArea').html();
};

function setEditHtml(editHtml){
	$('#bodyHtmlArea').html(editHtml);
	$('#cont').html(editHtml);
};

function setFileInfo(id , va){
	$('#'+id+'Area').find('.filearea').each(function(){
		if(!$(this).hasClass('tmp')){
			$(this).remove();
		}
	});
	if(va == null ) {
		resetFileTag(id);
		return;
	};
	
	var $last = $('#'+id+'Area .filearea:last');
	var $clone = $last.clone();
	$last.removeClass('tmp');
	$last.show();
	
	var p = va.lastIndexOf('\\');
	if (p > 0) va = va.substring(p + 1);
	$last.find('#'+id+'_fileView').text(va).removeAttr('id');
	$clone.insertAfter($last);	
};

//file-tag 에서 사용
var gFileTagMap = {};
function resetFileTag(id){
	var html = gFileTagMap[id];
	if(html!=null){
		var $file = $("#"+id+"File");
		$file.before(html);
		$file.remove();
		$("#"+id+"FileView").val('');
	}
};
function setFileTag(id, value, handler, exts){
	var viewId = id+'FileView';
	if(value==null) value = "";
	else {
		var p = value.lastIndexOf('\\');
		if(p>0) value = value.substring(p+1);
	}
	var $view = $("#"+viewId);
	var oldValue = $view.val();
	$view.val(value);
	
	if(exts!=null && exts!="" && value!=""){
		if(oldValue!=value){//IE에서 클릭했을때 이벤트 타는 버그 고침
			var va = value.toLowerCase();
			var matched = false;
			extArr = exts.toLowerCase().split(",");
			extArr.each(function(index, ext){
				if(va.endsWith("."+ext.trim())){
					matched = true;
					return false;
				}
			});
			if(!matched){
				$m.msg.alertMsg("cm.msg.attach.not.support.ext",[exts]);
				resetFileTag(viewId.substring(0, viewId.length-8));
				if(handler!=null) handler(viewId.substring(0, viewId.length-8), null);
			} else {
				if(handler!=null) handler(viewId.substring(0, viewId.length-8), value);
			}
		}
	} else {
		if(handler!=null) handler(viewId.substring(0, viewId.length-8), value);
	}
};

function delFileInfo(checkedObj, id) {
	$m.msg.confirmMsg("cm.cfrm.del", null, function(result){ <% // cm.cfrm.del=삭제하시겠습니까 ? %>
		if(result){
			$area = $(checkedObj).parents('.filearea');
			if ($area.hasClass('tmp') == false) {
				$area.remove();
			}
			resetFileTag(id);
		}
	});
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
}<% // [하단버튼:삭제] %>
function delDocTrans(param, valid) {
	if (!valid || $m.msg.confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		$m.ajax('/dm/doc/transDocDelAjx.do?menuId=${menuId}', param, function(data) {
			if (data.message != null) {
				$m.dialog.alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = $('#listPage').val();
			}
		});
	}
}<% // [하단버튼:삭제] 문서%>
function delDoc(statCd) {
	delDocTrans({docId:$('#docId').val(),statCd:statCd},true);
}<% // [하단버튼:취소] %>
function cancelDoc(){
	history.go(-1);
}
var listAddItemArea = null;
<%// 폴더를 변경할 경우 페이지를 리로딩해준다.[폴더에 등록된 유형에 대한 추가항목 로드] %>
function setPageChk(prefix){
	if(prefix == 'cls') return;
	$m.ajax('${_cxPth}/dm/doc/listAddItemAjx.do?menuId=${menuId}', {fldId:$('#fldId').val(),paramStorId:'${paramStorId}',docPid:'${param.docPid}',docId:'${param.docId}'}, function(html){
		if(listAddItemArea==null) listAddItemArea = $('#section').find('#listAddItemArea');
		listAddItemArea.html(html);
	}, {mode:'HTML', async:true});
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
}
var holdHide = false, holdHide2 = false, holdHideExt = false;
$(document).ready(function() {
	$("input[type='text'], textarea").each(function(){
		$space.apply(this, {relative:30});
	});
	
	var bodyHtml = $("#lobHandlerArea").html();
	if(bodyHtml!=''){
		setEditHtml(bodyHtml);
	}
	$layout.adjustBodyHtml('bodyHtmlArea');
	<c:if test="${!empty param.docId || !empty param.docPid || (!empty param.setDocId && !empty dmDocLVoMap.fldId)}">setPageChk('fld');</c:if>
});
//]]>
</script>
<u:secu auth="W">	
<div class="btnarea">
    <div class="size">
        <dl>
        	<c:if test="${empty param.docId || !empty authMap.update}">
        		<c:if test="${empty param.docId }"><dd class="btn" onclick="saveContinue();"><u:msg titleId="dm.btn.registered.continued" alt="계속등록" /></dd></c:if>
        		<dd class="btn" onclick="saveDoc();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
        		<c:if test="${empty param.docId}"><dd class="btn" onclick="tmpSave();"><u:msg titleId="cm.btn.tmpSave" alt="임시저장" /></dd></c:if>
        	</c:if>
        	<c:if test="${!empty param.docId}">
        		<c:if test="${!empty authMap.delete}"><dd class="btn" onclick="delDoc('F');"><u:msg titleId="cm.btn.del" alt="삭제" /></dd></c:if>
				<c:if test="${!empty authMap.save}"><dd class="btn" onclick="saveDoc();"><u:msg titleId="cm.btn.save" alt="저장" /></dd></c:if>
				<c:if test="${!empty authMap.tmpSave}"><dd class="btn" onclick="tmpSave();"><u:msg titleId="cm.btn.tmpSave" alt="임시저장" /></dd></c:if>
        	</c:if>
        	<dd class="btn" onclick="cancelDoc();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>           	 
     </dl>
    </div>
</div>
</u:secu>
<section id="section">

    <div class="blankzone">
        <div class="blank20"></div>
    </div>
    
	<form id="setForm" name="setForm" enctype="multipart/form-data" action="/dm/${transPage}.do?menuId=${menuId}">
	<input type="hidden" name="menuId" value="${menuId}" />
	<input type="hidden" id="listPage" name="listPage" value="/dm/doc/${listPage}.do?${nonPageParams}" />
	<u:set var="viewPage" test="${!empty dmDocLVoMap.statCd && dmDocLVoMap.statCd eq 'T'}" value="/dm/doc/${listPage}.do?${nonPageParams}" elseValue="/dm/doc/${viewPage}.do?${viewPageParams}${includeParams }"/>
	<input type="hidden" id="viewPage" name="viewPage" value="${viewPage }" />
	<input type="hidden" id="docId" name="docId" value="${dmDocLVoMap.docId }" />
	<c:if test="${!empty dmDocLVoMap.docNo }"><input type="hidden" id="docNo" name="docNo" value="${dmDocLVoMap.docNo }" /></c:if>
	<c:if test="${!empty dmDocLVoMap.docPid }"><input type="hidden" id="docPid" name="docPid" value="${dmDocLVoMap.docPid }" /></c:if>
	<c:if test="${dmDocLVoMap.dftYn eq 'Y' && !empty dmDocLVoMap.docGrpId }"><input type="hidden" id="docGrpId" name="docGrpId" value="${dmDocLVoMap.docGrpId }" /></c:if>
	
	<textarea id="cont" name="cont" style="display:none"></textarea>
	
    <div class="entryzone">
        <div class="entryarea">
        <dl>
        <dd class="etr_tit"><u:msg titleId="dm.jsp.setDoc.${!empty dmDocLVoMap.docId ? 'mod' : 'reg'}.title" alt="문서등록/문서수정" /></dd>
        <dd class="etr_bodytit_asterisk"><u:msg titleId="cols.subj" alt="제목" /></dd>
        <dd class="etr_input"><div class="etr_inputin"><m:input id="subj" titleId="cols.subj" value="${dmDocLVoMap.subj}" maxByte="240" mandatory="Y" style="width:95%;"/></div></dd>
		<u:set var="setDisabled" test="${!empty dmDocLVoMap.docPid }" value="Y" elseValue="N"/>
		<c:if test="${setDisabled eq 'N' }">
			<dd class="etr_bodytit_asterisk"><u:msg titleId="cols.fld" alt="폴더" /></dd>
			<dd class="etr_input">
				<div class="etr_ipmany">
				<dl>
				<dd class="etr_ip_lt">
					<div id="fldInfoArea" >
						<div id="idArea" style="display:none;"><input type="hidden" id="fldId" name="fldId" value="${dmDocLVoMap.fldId}" disabled="${setDisabled }"/></div>
						<div id="nmArea" style="display:inline;"><m:input type="text" id="fldNm" name="fldNm" className="etr_iplt" value="${dmDocLVoMap.fldNm}" readonly="Y" mandatory="Y" disabled="${setDisabled }"/></div>
	              	</div>
				</dd>
				<c:if test="${empty dmDocLVoMap.docPid && (empty param.docId || dmDocLVoMap.statCd eq 'T')}"><dd class="etr_se_rt" ><div class="etr_btn" onclick="findSelPop('F');"><u:msg titleId="dm.btn.fldSel" alt="폴더선택"/></div></dd></c:if>
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
				<c:if test="${empty param.docId || !empty authMap.cls }"><dd class="etr_se_rt" ><div class="etr_btn" onclick="findSelPop('C');"><u:msg titleId="dm.btn.clsSel" alt="분류선택"/></div></dd></c:if>
				</dl>
				</div>
			</dd>
		
		<dd class="etr_bodytit"><u:msg titleId="dm.cols.kwd" alt="키워드" /> - <u:msg titleId="dm.msg.kwd.comma" alt="콤마(,)구분" /></dd>
        <dd class="etr_input"><div class="etr_inputin">
        <c:set var="kwnNms" />
		<c:forEach var="kwdVo" items="${dmKwdLVoList }" varStatus="status"><c:set var="kwnNms" value="${kwnNms }${status.count > 1 ? ',' : ''}${kwdVo.kwdNm }"/></c:forEach>
		<input type="text" id="kwdNm" name="kwdNm" class="etr_iplt" value="${kwnNms}" maxlength="55"/>
        </div></dd>
        
        <dd class="etr_bodytit"><u:msg titleId="dm.cols.keepPrd" alt="보존연한" /></dd>
        <dd class="etr_select" id="docKeepPrdCdContainer">
        	<c:set var="docKeepPrdNm" value=""/>
        	<c:set var="docKeepPrdCd" value=""/>
            <div class="etr_open1" id="docKeepPrdCdOpen" style="display:none">
                <div class="open_in1">
                    <div class="open_div">
                    <dl>
                    <c:forEach items="${itemDispMap.docKeepPrdNm.colmVo.cdList}" var="cd" varStatus="status">
                    <c:if test="${(empty dmDocLVoMap.docKeepPrdCd && status.count == 1) || (cd.cdId == dmDocLVoMap.docKeepPrdCd)}"><c:set var="docKeepPrdNm" value="${cd.rescNm }"/><c:set var="docKeepPrdCd" value="${cd.cdId }"/></c:if>
					<c:if test="${status.count>1 }"><dd class="line"></dd></c:if>
                    <dd class="txt" onclick="setSelOptions('docKeepPrdCd',$(this).attr('data-code'),$(this).text());" data-code="${cd.cdId}">${cd.rescNm }</dd>
                    </c:forEach>
                 </dl>
                    </div>
                </div>
            </div>
            <input type="hidden" name="docKeepPrdCd" value="${docKeepPrdCd }"/>
            <div class="etr_ipmany">
                <div class="select_in1" onclick="holdHide = true; $('#docKeepPrdCdContainer #docKeepPrdCdOpen').toggle();">
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
                    <c:if test="${empty dmDocLVoMap.docId || (!empty dmDocLVoMap.docId && dmDocLVoMap.seculCd eq 'none')}"><c:set var="seculNm" value="${noSelect }"/><c:set var="seculCd" value="none"/></c:if>
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
		<c:if test="${empty dmDocLVoMap.docId}">
		<dd class="etr_bodytit"><u:msg titleId="dm.cols.bumk" alt="즐겨찾기" /></dd>
        <dd class="etr_select" id="bumkIdContainer">
        	<c:set var="bumkNm" value=""/>
        	<c:set var="bumkId" value=""/>
            <div class="etr_open1" id="bumkIdOpen" style="display:none">
                <div class="open_in1">
                    <div class="open_div">
                    <dl>
                    <c:forEach var="bumkVo" items="${dmBumkBVoList }" varStatus="status">
                    <c:if test="${(empty dmDocLVoMap.bumkId && status.count == 1) || (dmDocLVoMap.bumkId == bumkVo.bumkId)}"><c:set var="bumkNm" value="${bumkVo.bumkNm }"/><c:set var="bumkId" value="${bumkVo.bumkId }"/></c:if>
					<c:if test="${status.count>1 }"><dd class="line"></dd></c:if>
                    <dd class="txt" onclick="setSelOptions('bumkId',$(this).attr('data-code'),$(this).text());" data-code="${bumkVo.bumkId}">${bumkVo.bumkNm }</dd>
                    </c:forEach>
                 </dl>
                    </div>
                </div>
            </div>
            <input type="hidden" name="bumkId" value="${bumkId }"/>
            <div class="etr_ipmany">
                <div class="select_in1" onclick="holdHide = true; $('#bumkIdContainer #bumkIdOpen').toggle();">
                <dl>
                <dd class="select_txt" id="selectView"><span>${bumkNm }</span></dd>
                <dd class="select_btn"></dd>
                </dl>
                </div>
            </div>
        </dd>
        </c:if>
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
	
    <div class="blank25"></div>
    <u:secu auth="W">
    <div class="btnarea">
        <div class="size">
            <dl>
            	<c:if test="${empty param.docId || !empty authMap.update}">
            		<c:if test="${empty param.docId }"><dd class="btn" onclick="saveContinue();"><u:msg titleId="dm.btn.registered.continued" alt="계속등록" /></dd></c:if>
	        		<dd class="btn" onclick="saveDoc();"><u:msg titleId="cm.btn.save" alt="저장" /></dd>
	        		<c:if test="${empty param.docId}"><dd class="btn" onclick="tmpSave();"><u:msg titleId="cm.btn.tmpSave" alt="임시저장" /></dd></c:if>
	        	</c:if>
	        	<c:if test="${!empty param.docId}">
	        		<c:if test="${!empty authMap.delete}"><dd class="btn" onclick="delDoc('F');"><u:msg titleId="cm.btn.del" alt="삭제" /></dd></c:if>
					<c:if test="${!empty authMap.save}"><dd class="btn" onclick="saveDoc();"><u:msg titleId="cm.btn.save" alt="저장" /></dd></c:if>
					<c:if test="${!empty authMap.tmpSave}"><dd class="btn" onclick="tmpSave();"><u:msg titleId="cm.btn.tmpSave" alt="임시저장" /></dd></c:if>
	        	</c:if>
	        	<dd class="btn" onclick="cancelDoc();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
         </dl>
        </div>
    </div>
	</u:secu>
	
	</form>
	
	<jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
 </section>
 <div id="lobHandlerArea" style="display:none;"><c:if test="${!empty dmDocLVoMap.cont }">${dmDocLVoMap.cont }</c:if><u:clob lobHandler="${lobHandler }"/></div>