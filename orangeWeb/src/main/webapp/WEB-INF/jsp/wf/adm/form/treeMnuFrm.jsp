<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="openListFrm"/>
<script type="text/javascript">
<!--<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name, rescId){
	if(id==null || id=='') return;
	if(id=='ROOT'){
		parent.${callback}(id);
		return;
	}
	var exts = TREE.getExtData('mnuTree','exts');
	if(exts==null) return;
	parent.${callback}(id);
}<%// html reload %>
function reloadFrame(grpId){
	var href = './treeGrpFrm.do?menuId=${menuId}';
	if(grpId!=null && grpId!='') href = href + "&grpId="+grpId;
	location.replace(href);
}<%// 트리의 확장 데이터 리턴 %>
function getTreeData(){
	var sel = TREE.getTree('mnuTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return {id:'ROOT', mnuGrpId:'${param.mnuGrpId}'};
	} else {
		var exts = TREE.getExtData('mnuTree','exts');
		return exts;
	}
}<% // 메뉴 이동 %>
function move(direction) {
	var sel = TREE.getTree('mnuTree').selected;
	if (sel == null) {
		alertMsg('cm.msg.noSelect');
		<% // 선택한 항목이 없습니다. %>
	} else if ($(sel).attr('id') == 'ROOTLI') {
		alertMsg('cm.msg.move.root');
		<% // 최상위 항목은 이동 할 수 없습니다. %>
	} else if ((direction == 'up' || direction == 'tup') && $(sel).prev().length == 0) {
		alertMsg('cm.msg.move.first.up');
		<% // 맨 위의 항목 입니다. %>
	} else if ((direction == 'down' || direction == 'tdown') && $(sel).next().length == 0) {
		alertMsg('cm.msg.move.last.down');
		<% // 맨 아래의 항목 입니다. %>
	} else {
		if(direction=='up'){
			$(sel).prev().before(sel);
			$(sel).removeClass('end');
			$(sel).parent().children(":last-child").addClass("end");
		}else if(direction=='tup'){
			$(sel).parent().children(":first").before(sel);
			$(sel).removeClass('end');
			$(sel).parent().children(":last-child").addClass("end");
		}else if(direction=='tdown'){
			$(sel).parent().children(":last").removeClass('end');
			$(sel).parent().children(":last").after(sel);
			$(sel).addClass("end");
		}else if(direction=='down'){
			$(sel).next().removeClass('end');
			$(sel).next().after(sel);
			$(sel).parent().children(":last").addClass("end");
		}
	}
}<% // 메뉴 전체 배열 %>
function getMnuList(mnuMap, $p){
	if($p.children().length>0 && mnuMap==null) mnuMap = new ParamMap();
	var map=null;
	var child=null;
	for( var i=0;i<$p.children().length;i++){
		var exts = $($p.children()[i]).attr("data-exts");
		var obj = $.parseJSON(exts);
		if(mnuMap.get(obj.mnuPid)!=null) map=mnuMap.get(obj.mnuPid);
		else map=[];
		map.push(obj.mnuId);
		mnuMap.put(obj.mnuPid, map);
		child=$($p.children()[i]).find("UL:first");
		if(child!=undefined && child.children().length>0) getMnuList(mnuMap, child);
	}
	return mnuMap;
}<% // 메뉴 이동 저장 %>
function moveMnuSave() {
	var $node = $('#mnuTree').find('#ROOTLI'), $p;
	if($node.length>0){
		$p = $node.parent().find("UL:first");
		var mnuMap = getMnuList(null, $p);
		if(mnuMap==null) return;		
		var mnuList = JSON.stringify(mnuMap.toJSON());
		callAjax('/wf/adm/form/transMnuMoveSaveAjx.do?menuId=${menuId}', {valUM:'${param.valUM}', mnuList: mnuList, mnuGrpId: '${param.mnuGrpId}'}, function (data) {
			if (data.message != null) {
				alert(data.message);
			}
		});
	}
};<%// 트리의 선택 데이터 리턴 %>
function getTreeSelect(mode){
	var sel = TREE.getTree('mnuTree').selected;
	if($(sel).attr('id')=='ROOTLI' && (mode==undefined || mode=='')){
		return null;
	} else {
		if($(sel).attr('id')=='ROOTLI') return [{id:'ROOT',nm:'<u:msg titleId="wh.jsp.sysMd.title" alt="시스템모듈"/>'}];
		var exts = TREE.getExtData('mnuTree','exts');
		return [{id:exts.grpId,nm:exts.grpNm}];
	}
}<%// 트리의 선택 ID 리턴 %>
function getSelectId(){
	var sel = TREE.getTree('mnuTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return '${selectedNode}';
	} else {
		var exts = TREE.getExtData('mnuTree','exts');
		return exts.mnuId;
	}
}<%// [폴더] - 트리의 선택 ID 리턴 %>
function getSelectedMnu(){
	var sel = TREE.getTree('mnuTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return '${param.mnuGrpId}';
	} else {
		var exts = TREE.getExtData('mnuTree','exts');
		if (exts['fldYn'] != 'Y') {
			return null;
		}
		return exts.mnuId;
	}
}<%// tree 스크립트로 생성 %>
$(document).ready(function() {
	
	var tree = TREE.create('mnuTree');
	tree.onclick = 'onTreeClick';
	//tree.setIconTitle('${iconTitle}');
	tree.setRoot('${param.mnuGrpId}', '<u:msg titleId="cols.mnu" alt="메뉴"/>');
	tree.setSkin("${_skin}");
	<c:if test="${not empty param.openLvl}">tree.openLvl = ${param.openLvl};</c:if><%// 특정폴더 하위나 상위폴더 조회시 모든 트리를 펼쳐 보이게함 %>
	<c:if test="${not empty param.treeSelectOption}">TREE.selectOption = '${param.treeSelectOption}';</c:if><%// 여러개 선택 가능 하도록 %>
	
	<%// tree.add() param : grpPid, id, name, type, sortOrdr %>
	<c:forEach items="${ptMnuDVoList}" var="ptMnuDVo" varStatus="status" >
	tree.add("${ptMnuDVo.mnuPid}","${ptMnuDVo.mnuId}","<u:out value='${ptMnuDVo.rescNm}' type='script' />","<u:decode srcValue='${ptMnuDVo.fldYn}' tgtValue='Y' value='F' elseValue='A' />","${ptMnuDVo.sortOrdr}","${ptMnuDVo.rescId}",{mnuId:"${ptMnuDVo.mnuId}",mnuPid:"${ptMnuDVo.mnuPid}",mnuGrpId:"${ptMnuDVo.mnuGrpId}", title:"${ptMnuDVo.mnuId}",rescId:"${ptMnuDVo.rescId}",sysYn:"${ptMnuDVo.sysMnuYn}",fldYn:"${ptMnuDVo.fldYn}"});
	</c:forEach>
	if(tree.nodeSize>1000)
		tree.openLvl = 1;
	tree.draw();
	tree.selectTree("${selectedNode}");
	onTreeClick('${selectedNode}','');
});
//-->
</script>
<div id="mnuTree" class="tree"></div>
