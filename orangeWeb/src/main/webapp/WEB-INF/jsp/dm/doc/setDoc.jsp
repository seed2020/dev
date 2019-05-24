<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="frmYn" test="${!empty pageSuffix && pageSuffix == 'Frm' }" value="Y" elseValue="N"/>
<u:params var="nonPageParams" excludes="docId,pageNo,docPid,setDocId"/>
<u:params var="viewPageParams" excludes="docId,docPid,setDocId"/>
<u:set var="includeParams" test="${empty dmDocLVoMap.docId && !empty dmDocLVoMap.docPid}" value="&docId=${dmDocLVoMap.docPid }" elseValue="&docId=${dmDocLVoMap.docId }"/>
<script type="text/javascript">
<!--<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}<% // [하단버튼:삭제] %>
function delDocTrans(param, valid) {
	if (!valid || confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transDocDelAjx.do?menuId=${menuId}', param, function(data) {
			if (data.message != null) {
				alert(data.message);
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
	<c:if test="${!empty param.docId}">
	var url = $('#setForm input[id="viewPage"]').val();
	url+='&cancel=Y';
	location.href = url;
	</c:if>
	<c:if test="${empty param.docId}">history.go(-1);</c:if>	
}<%// 폴더를 변경할 경우 페이지를 리로딩해준다.[폴더에 등록된 유형에 대한 추가항목 로드] %>
function setPageChk(prefix){
	if(prefix == 'cls') return;
	reloadFrame('listAddItemFrm', './listAddItemFrm.do?menuId=${menuId}&fldId='+$('#fldId').val()+'&paramStorId=${paramStorId}');
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
	var url = './findSelPop.do?menuId=${menuId}&lstTyp='+lstTyp+"&selIds="+data+"&fncMul="+(lstTyp == 'C' ? 'Y':'N');
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
}
var saves = false; 
<%// [버튼] 확인 %>
function setCfrmPop(){
	// 신규문서일 경우에 심의여부를 확인한다.
	<c:if test="${empty dmDocLVoMap.docId || (!empty dmDocLVoMap.docId && dmDocLVoMap.statCd ne 'C')}">
	var discYn = getIframeContent("listAddItemFrm").getDiscYn();
	if(discYn == 'Y'){
		saves = true;
		save();
		return;
	}
	</c:if>
	var url = './setDocCfrmPop.do?${paramsForList}&docId=${dmDocLVoMap.docId}&docNoMod=${authMap.docNoMod}';
	url+='&fldId='+$('#setForm #fldId').val();
	dialog.open('setCfrmPop', '<u:msg titleId="cm.btn.confirm" alt="확인" />', url);
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
		alertMsg('cm.input.check.mandatory',['<u:msg titleId="cols.subj" alt="제목" />']);
		$form.find("input[name='subj']").focus();
		return;
	}
	if($form.find("input[name='fldId']").val() == ''){
		alertMsg('cm.input.check.mandatory',['<u:msg titleId="cols.fld" alt="폴더" />']);
		$form.find("input[name='fldNm']").focus();
		return;
	}
	$form.appendHidden({name:'statCd',value:'T'});
	saves = true;
	save(true);
}<%// [버튼] 저장 %>
function save(isValid){
	if (isValid || validator.validate('setForm')) {
		if (isInUtf8Length($('#cont').val(), 0, '${bodySize}') > 0) {
			alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.cont" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
			return;
		}
		if(!saves) {
			setCfrmPop();
			return;
		}
		if(saves && confirmMsg("cm.cfrm.save")){
			var $form = $("#setForm");
			// 추가항목 조회
			if(getIframeContent("listAddItemFrm").location.href.indexOf("reloadable.do")<0){
				var arrs = getIframeContent('listAddItemFrm').getChkVal();
				if(arrs != null){
					$.each(arrs,function(index,vo){
						//$form.find("[name='"+vo.name+"']").remove();
						$form.appendHidden({name:vo.name,value:vo.value});
					});	
				}
			}
			$form.attr('method','post');
			$form.attr('action','./${transPage}.do?menuId=${menuId}');
			$form.attr('target','dataframe');
			editor('cont').prepare();
			saveFileToForm('${filesId}', $form[0], null);
			//$form.submit();
		}else{
			saves = false;
		}
	}else{
		saves = false;
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
	$form.appendHidden({name:'setPage',value:'./${setPage}.do?${nonPageParams}'});
	save();
}<%// 저장, 삭제시 리로드 %>
function reloadDocFrm(url){
	if(url != undefined && url != null) location.replace(url);
	else location.replace(location.href);
};
$(document).ready(function() {
	if(unloadEvent.editorType != 'namo'){
		var bodyHtml = $("#lobHandlerArea").html();
		if(bodyHtml!=''){
			$('#contEdit').on('load',function () {
				editor('cont').setInitHtml(bodyHtml);
				editor('cont').prepare();
				//$('#cont').val(editor('cont').getHtml());
			});
		}
	}
	resizeIframe('listAddItemFrm');
	setUniformCSS();
	<c:if test="${pageSuffix eq 'Frm'}">parent.applyDocBtn();</c:if>
	/* <c:if test="${!empty dmDocLVoMap.docId && pageSuffix eq 'Frm' }">parent.applyDocBtn('mod');</c:if>
	<c:if test="${empty dmDocLVoMap.docId && pageSuffix eq 'Frm' }">parent.applyDocBtn('reg');</c:if> */
});<%
// 나모 에디터 초기화 %>
function initNamo(id){
	var bodyHtml = $("#lobHandlerArea").html();
	if(bodyHtml!=''){
		editor('cont').setInitHtml(bodyHtml);		
	}
	editor('cont').prepare();
}
//-->
</script>

<c:if test="${frmYn == 'N' }">
<u:title titleId="dm.jsp.catMgm.title" alt="유형관리" menuNameFirst="true"/>
</c:if>
<c:if test="${frmYn == 'Y' }">
<c:set var="frmStyle" value="style='padding:10px;'"/>
</c:if>
<c:set var="voMap" value="${dmDocLVoMap }" scope="request"/>
<form id="setForm" method="post" enctype="multipart/form-data" ${frmStyle }>
<input type="hidden" name="menuId" value="${menuId}" />
<u:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams}" />
<u:set var="viewPage" test="${!empty dmDocLVoMap.statCd && dmDocLVoMap.statCd eq 'T'}" value="./${listPage}.do?${nonPageParams}" elseValue="./${viewPage}.do?${viewPageParams}${includeParams }"/>
<u:input type="hidden" id="viewPage" value="${viewPage }" />
<u:input type="hidden" id="docId" value="${dmDocLVoMap.docId }" />
<c:if test="${!empty dmDocLVoMap.docNo }"><u:input type="hidden" id="docNo" value="${dmDocLVoMap.docNo }" /></c:if>
<c:if test="${!empty dmDocLVoMap.docPid }"><u:input type="hidden" id="docPid" value="${dmDocLVoMap.docPid }" /></c:if>
<c:if test="${dmDocLVoMap.dftYn eq 'Y' && !empty dmDocLVoMap.docGrpId }"><u:input type="hidden" id="docGrpId" value="${dmDocLVoMap.docGrpId }" /></c:if>
<c:if test="${frmYn == 'N' }"><u:title titleId="dm.jsp.dftInfo.title" alt="기본정보" type="small" /></c:if>
<u:set var="colgroup" test="${!empty dmDocLVoMap.docPid }" value="15%," elseValue="15%,35%,15%,35%"/>
<u:listArea colgroup="${colgroup }" noBottomBlank="true">
<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="bodybg_lt" colspan="3"><u:input id="subj" titleId="cols.subj" value="${dmDocLVoMap.subj}" maxByte="240" mandatory="Y" style="width:95%;"/></td>
</tr>
<u:set var="setDisabled" test="${!empty dmDocLVoMap.docPid }" value="Y" elseValue="N"/>
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
		<c:if test="${empty param.docId || !empty authMap.cls }"><u:buttonS titleId="dm.btn.clsSel" alt="분류 선택" onclick="findSelPop('C');" /></c:if>
	</td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.kwd" alt="키워드" /></td>
	<td class="bodybg_lt" colspan="3">
		<c:set var="kwnNms" />
		<c:forEach var="kwdVo" items="${dmKwdLVoList }" varStatus="status"><c:set var="kwnNms" value="${kwnNms }${status.count > 1 ? ',' : ''}${kwdVo.kwdNm }"/></c:forEach>
		<u:input id="kwdNm" titleId="dm.cols.kwd" value="${kwnNms}" maxByte="120" style="width:80%;" /><span class="color_txt"><u:msg titleId="dm.msg.kwd.comma" alt="콤마(,)구분" /></span>
	</td>
</tr>
<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="dm.cols.keepPrd" alt="보존연한" /></td>
	<td class="bodybg_lt" ><c:if test="${empty param.docId || !empty authMap.keepDdln }"><select id="docKeepPrdCd" name="docKeepPrdCd"<u:elemTitle titleId="dm.cols.keepPrd" alt="보존연한" />><c:forEach 
			items="${itemDispMap.docKeepPrdNm.colmVo.cdList}" var="cd" varStatus="status">
			<option value="${cd.cdId}" <c:if test="${cd.cdId == dmDocLVoMap.docKeepPrdCd}">selected="selected"</c:if>>${cd.rescNm}</option></c:forEach>
			</select></c:if><c:if test="${!empty param.docId && empty authMap.keepDdln }"><u:input id="docKeepPrdNm" value="${dmDocLVoMap.docKeepPrdNm}" titleId="dm.cols.keepPrd" readonly="Y" disabled="Y"/></c:if></td>
	<td class="head_lt"><u:mandatory /><u:msg titleId="dm.cols.secul" alt="보안등급" /></td>
	<td class="bodybg_lt" ><c:if test="${empty param.docId || !empty authMap.seculCd }"><select id="seculCd" name="seculCd"<u:elemTitle titleId="dm.cols.secul" alt="보안등급" /> <c:if test="${setDisabled eq 'Y' }">disabled="disabled"</c:if>>
			<option value="none"><u:msg titleId="cm.option.noSelect" alt="선택안함" /></option><c:forEach 
			items="${itemDispMap.seculNm.colmVo.cdList}" var="cd" varStatus="status">
			<option value="${cd.cdId}" <c:if test="${cd.cdId == dmDocLVoMap.seculCd}">selected="selected"</c:if>>${cd.rescNm}</option></c:forEach>
			</select></c:if><c:if test="${!empty param.docId && empty authMap.seculCd }"><u:input id="seculNm" value="${dmDocLVoMap.seculNm}" titleId="dm.cols.secul" readonly="Y" disabled="Y"/></c:if></td>
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
			<c:if test="${empty param.docId || !empty authMap.owner || dmDocLVoMap.statCd eq 'T'}">
				<td><u:buttonS href="javascript:;" titleId="cm.btn.choice" alt="선택" onclick="openSingUser();" /></td>
				<td class="body_lt"><u:msg titleId="dm.msg.setDoc.tx01" alt="* 소유자는 한명만 등록됩니다." /></td>
			</c:if>
		</tr>
		</tbody>
	</table>
	</td>
</tr>
<c:if test="${empty dmDocLVoMap.docId}">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.bumk" alt="즐겨찾기" /></td>
	<td class="bodybg_lt" colspan="3"><c:if test="${!empty dmBumkBVoList }"><select id="bumkId" name="bumkId"<u:elemTitle titleId="dm.cols.bumk" alt="즐겨찾기" />>
		<option value=""><u:msg titleId="cm.option.noSelect" alt="선택안함" /></option>
		<c:forEach var="bumkVo" items="${dmBumkBVoList }" varStatus="status">
			<u:option value="${bumkVo.bumkId }" title="${bumkVo.bumkNm }" checkValue="${dmDocLVoMap.bumkId }"/>
		</c:forEach>
		</select></c:if><c:if test="${empty dmBumkBVoList }"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></c:if></td>
</tr>
</c:if>
</u:listArea>

<u:blank />
<u:set var="setDocParam" test="${!empty param.setDocId}" value="&docId=${param.setDocId }" elseValue=""/>
<u:set var="addItemSrc" test="${(frmYn eq 'Y' && !empty param.fldId ) || !empty dmDocLVoMap.fldId}" value="./listAddItemFrm.do?${params}${setDocParam }" elseValue="/cm/util/reloadable.do"/>
<iframe id="listAddItemFrm" name="listAddItemFrm" src="${addItemSrc }" style="width:100%;*height:0px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
<u:editor id="cont" width="100%" height="400px" module="dm" value="${dmDocLVoMap.cont}" padding="2" />

<u:listArea>
	<tr>
	<td>
		<u:files id="${filesId}" fileVoList="${fileVoList}" module="dm" mode="set" exts="${exts }" extsTyp="${extsTyp }"/>
	</td>
</tr>
</u:listArea>
<u:blank />

<c:set var="exKeys" value="view,update,delete,owner,fileview,recovery,disuse,apvd,rjt,save,tmpSave,docNoMod"/><!-- 하단 버튼 제외 key -->
<u:set var="rightBtnDisplay" test="${frmYn eq 'Y' }" value="display:none;" elseValue=""/>
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea" style="${rightBtnDisplay }">
	<c:if test="${empty param.docId || !empty authMap.update}">
		<c:if test="${empty param.docId }"><u:button titleId="dm.btn.registered.continued" alt="계속등록" href="javascript:saveContinue();" auth="W" /></c:if>
		<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveDoc();" auth="W" />
		<c:if test="${empty param.docId && isAdmin == false}"><u:button titleId="cm.btn.tmpSave" alt="임시저장" href="javascript:tmpSave();" auth="W" /></c:if>
	</c:if>
	<c:if test="${!empty param.docId}">
		<c:if test="${!empty authMap.delete}"><u:button titleId="cm.btn.del" alt="삭제" href="javascript:delDoc('F');" auth="W" /></c:if>
		<c:if test="${!empty authMap.save}"><u:button titleId="cm.btn.save" alt="저장" href="javascript:saveDoc();" auth="W" /></c:if>
		<c:if test="${!empty authMap.tmpSave}"><u:button titleId="cm.btn.tmpSave" alt="임시저장" href="javascript:tmpSave();" auth="W" /></c:if>
	</c:if>
	<u:button titleId="cm.btn.cancel" href="javascript:cancelDoc();" alt="취소" />
</u:buttonArea>

</form>
<div id="lobHandlerArea" style="display:none;"><c:if test="${!empty dmDocLVoMap.cont }">${dmDocLVoMap.cont }</c:if><u:clob lobHandler="${lobHandler }"/></div>
