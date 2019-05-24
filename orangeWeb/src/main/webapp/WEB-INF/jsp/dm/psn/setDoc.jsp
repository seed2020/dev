<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="frmYn" test="${!empty pageSuffix && pageSuffix == 'Frm' }" value="Y" elseValue="N"/>
<u:params var="nonPageParams" excludes="docId,pageNo"/>
<u:params var="viewPageParams" excludes="docId,docPid"/>
<u:set var="includeParams" test="${empty dmDocLVoMap.docId && !empty dmDocLVoMap.docPid}" value="&docId=${dmDocLVoMap.docPid }" elseValue="&docId=${dmDocLVoMap.docId }"/>
<script type="text/javascript">
<!--<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}<% // [하단버튼:삭제] %>
function delDocTrans(param, valid) {
	if (!valid || confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transPsnDocDelAjx.do?menuId=${menuId}', param, function(data) {
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
	history.go(-1);
}<%// 폴더를 변경할 경우 페이지를 리로딩해준다.[폴더에 등록된 유형에 대한 추가항목 로드] %>
function setPageChk(prefix){
	if(prefix == 'cls') return;
	reloadFrame('listAddItemFrm', './listPsnAddItemFrm.do?menuId=${menuId}&fldId='+$('#fldId').val()+'&storId=${param.storId}');
};<%// 분류,폴더 Prefix %>
function getTabPrefix(lstTyp){
	var prefix = "fld";
	if(lstTyp == 'C') prefix = "cls";
	return prefix;
}<%// [버튼] 분류,폴더 %>
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
	setPageChk(prefix);
}<%// [버튼] 저장 %>
function save(){
	if (validator.validate('setForm')) {
		if (isInUtf8Length($('#cont').val(), 0, '${bodySize}') > 0) {
			alertMsg('cm.input.check.maxbyte', ['<u:msg titleId="cols.cont" />','${bodySize}']);<% // cm.input.check.maxbyte="{0}"의 최대 바이트수 ({1} bytes)를 초과 하였습니다. %>
			return;
		}
		if(confirmMsg("cm.cfrm.save")){
			var $form = $("#setForm");
			// 추가항목 조회
			if(getIframeContent("listAddItemFrm").location.href.indexOf("reloadable.do")<0){
				var arrs = getIframeContent('listAddItemFrm').getChkVal();
				if(arrs != null){
					$.each(arrs,function(index,vo){
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
		}
	}
}<%// [하단버튼:저장] %>
function saveDoc(){
	var $form = $("#setForm");
	$form.find("[name='setPage']").remove();
	save();
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
			});
		}
	}
	resizeIframe('listAddItemFrm');
	setUniformCSS();
	<c:if test="${pageSuffix eq 'Frm'}">parent.applyDocBtn();</c:if>
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
<u:input type="hidden" id="viewPage" value="./${viewPage}.do?${viewPageParams}&docId=${dmDocLVoMap.docId }" />
<u:input type="hidden" id="docId" value="${dmDocLVoMap.docId }" />
<c:if test="${frmYn == 'N' }"><u:title titleId="dm.jsp.dftInfo.title" alt="기본정보" type="small" /></c:if>
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
<u:set var="setDocParam" test="${!empty param.setDocId}" value="&docId=${param.setDocId }" elseValue=""/>
<u:set var="addItemSrc" test="${(frmYn eq 'Y' && !empty param.fldId ) || !empty dmDocLVoMap.fldId}" value="./listPsnAddItemFrm.do?${params}${setDocParam }" elseValue="/cm/util/reloadable.do"/>
<iframe id="listAddItemFrm" name="listAddItemFrm" src="${addItemSrc }" style="width:100%;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
<u:editor id="cont" width="100%" height="400px" module="dm" value="${dmDocLVoMap.cont}" padding="2" />
<u:listArea colgroup="15%,">
	<tr>
	<td>
		<u:files id="${filesId}" fileVoList="${fileVoList}" module="dm" mode="set" actionParam="psn" exts="${exts }" extsTyp="${extsTyp }"/>
	</td>
</tr>
</u:listArea>
<u:blank />
<u:set var="rightBtnDisplay" test="${frmYn eq 'Y' }" value="display:none;" elseValue=""/>
<% // 하단 버튼 %>
<u:buttonArea id="rightBtnArea" style="${rightBtnDisplay }">
	<c:if test="${empty param.docId }"><u:button titleId="dm.btn.registered.continued" alt="계속등록" href="javascript:saveContinue();" auth="W" /></c:if>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:saveDoc();" auth="W" />
	<%-- <c:if test="${!empty param.docId}">
		<u:button titleId="cm.btn.del" alt="삭제" href="javascript:delDoc('F');" auth="W" />
	</c:if> --%>
	<u:button titleId="cm.btn.cancel" href="javascript:cancelDoc();" alt="취소" />
</u:buttonArea>

</form>
<div id="lobHandlerArea" style="display:none;"><c:if test="${!empty dmDocLVoMap.cont }">${dmDocLVoMap.cont }</c:if><u:clob lobHandler="${lobHandler }"/></div>
