<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="paramsForList" excludes="formNo" />
<script type="text/javascript">
<!--
<%// [선택된 메뉴ID 조회] %>
function getSelectedMnu(){
	var mnuId=getIframeContent('mnuFrm').getSelectedMnu();
	if(mnuId==null) return null;
	var arrs={};
	arrs['mnuId']=mnuId;
	arrs['mnuGrpId']=$('#mnuGrpId').val();
	
	return arrs;
}
<%// 메뉴 트리 리로드 %>
function reloadMnuTree(mnuGrpId, mnuPid, mnuId){
	var url='/wf/adm/form/treeMnuFrm.do?menuId=${menuId}&callback=cancelHandler&valUM=${param.valUM}&mnuGrpId='+mnuGrpId;
	if(mnuPid!=undefined){
		if(mnuId!='') url+='&mnuId='+mnuId;
		else url+='&mnuId='+mnuPid;
	}
	reloadFrame('mnuFrm', url);
}<% // 폴더관리 %>
function mngFld(mode) {
	var tree = getIframeContent('mnuFrm').getTreeData();
	if (tree == null) {
		alertMsg('cm.msg.noSelect');
		<% // cm.msg.noSelect=선택한 항목이 없습니다. %>
	} else if (tree.fldYn == 'N') {
		if (mode == 'reg') {
			alertMsg('pt.jsp.setMnu.msg1');
			<% // pt.jsp.setMnu.msg1=메뉴에는 하위 폴더를 등록 할 수 없습니다. %>
		} else if (mode == 'mod') {
			alertMsg('pt.jsp.setMnu.msg2');
			<% // pt.jsp.setMnu.msg2=메뉴는 오른쪽 상세설정에서 수정해 주십시요. %>
		}
	} else if (('Y' == tree['sysYn'] || 'ROOT' == tree['id']) && mode=='mod') {
		alertMsg('cm.msg.mod.root');
		<% // 최상위 항목은 수정 할 수 없습니다. %>
	} else {
		var popTitle = (mode == 'reg') ? '<u:msg titleId="pt.jsp.setCd.regFld" alt="폴더등록"/>' : '<u:msg titleId="pt.jsp.setCd.modFld" alt="폴더수정"/>';
		var url = '/pt/adm/mnu/setFldPop.do?menuId=${menuId}&valUM=${param.valUM}&admMnuYn=N&mnuGrpId=' + tree.mnuGrpId;
		url += '&' + ((mode == 'reg') ? 'mnuPid' : 'mnuId') + '=' + tree.mnuId;
		parent.dialog.open('setFldDialog', popTitle, url);
	}
}
<% // 폴더 삭제 %>
function delFld() {
	var exts = getIframeContent('mnuFrm').getTreeData();
	if (exts == null) {
		alertMsg('cm.msg.noSelect');
		<% // 선택한 항목이 없습니다. %>
	} else if ('Y' == exts['sysYn'] || 'ROOT' == exts['id']) {
		alertMsg('pt.msg.not.del.mnu.sys');
		<% // 시스템 메뉴는 삭제 할 수 업습니다. %>
	} else if (confirmMsg("cm.cfrm.del")) {<% // cm.cfrm.del=삭제하시겠습니까 ? %>
		callAjax('/pt/adm/mnu/transFldDelAjx.do?menuId=${menuId}&valUM=${param.valUM}', {mode: 'delete', mnuIds: [exts['mnuId']]}, function (data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				reloadMnuTree($('#mnuGrpId').val());
			}
		});
	}
}<% // 메뉴 이동 %>
function move(direction){
	getIframeContent('mnuFrm').move(direction);
}<% // 메뉴 이동 저장 %>
function moveMnuSave(){
	getIframeContent('mnuFrm').moveMnuSave();
}<% // 메뉴 트리 리로드 %>
function reloadMnuFrm(mnuPid, mnuId){
	reloadMnuTree($('#mnuGrpId').val(), mnuPid, mnuId);
}
function cancelHandler(){}
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>
<div class="titlearea">
<div class="tit_left"><dl><dd class="title_s"><u:msg titleId="bb.jsp.setBbMnuPop.leftTitle" alt="메뉴 트리 구성" /></dd></dl></div>
<div class="tit_right"><ul><li class="ico"><select id="mnuGrpId" style="min-width:100px;" onchange="reloadMnuTree(this.value);">
<c:if test="${fn:length(ptMnuGrpBVoList) > 0}"><c:forEach items="${ptMnuGrpBVoList}" var="mnuGrpVo" varStatus="status">
<u:option value="${mnuGrpVo.mnuGrpId}" title="${mnuGrpVo.rescNm}" checkValue="${ptMnuGrpBVo.mnuGrpId}" />
</c:forEach></c:if></select></li><li class="ico">
<u:titleIcon type="move.top" href="javascript:move('tup')" auth="A" />
<u:titleIcon type="up" href="javascript:move('up')" auth="A" />
<u:titleIcon type="down" href="javascript:move('down')" auth="A" />
<u:titleIcon type="move.bottom" href="javascript:move('tdown')" auth="A" /></li></ul></div></div>
<u:set var="mnuTreeUrl" test="${!empty firstMnuGrpId }" value="./treeMnuFrm.do?menuId=${menuId}&callback=cancelHandler&mnuGrpId=${firstMnuGrpId }" elseValue="/cm/util/reloadable.do"/>
<iframe id="mnuFrm" name="mnuFrm" src="${mnuTreeUrl }" style="width:100%;min-height:330px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<u:buttonArea topBlank="true">
	<c:if test="${param.valUM ne 'M' }">
		<u:buttonS alt="폴더등록" href="javascript:mngFld('reg');" titleId="pt.jsp.setCd.regFld" id="btnAddFld" auth="A" />
		<u:buttonS alt="폴더수정" href="javascript:mngFld('mod');" titleId="pt.jsp.setCd.modFld" id="btnModFld" auth="A" />
		<u:buttonS alt="삭제" href="javascript:delFld();" titleId="cm.btn.del" id="btnDelFld" auth="A" />
	</c:if>
	<u:buttonS alt="순서저장" href="javascript:moveMnuSave();" titleId="wf.btn.ordr.save" auth="A" />
</u:buttonArea>