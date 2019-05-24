<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="openListFrm" />
<script type="text/javascript">
<!--<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name, rescId){	
	<c:if test="${!empty param.noInitClick}">return;</c:if>
	if(id==null || id=='') return;
	if(id=='ROOT'){
		parent.${callback}(id);
		return;
	}
	var exts = TREE.getExtData('grpTree','exts');
	if(exts==null) return;
	parent.${callback}(id);
}<%// html reload %>
function reloadFrame(grpId){
	var href = './treeGrpFrm.do?menuId=${menuId}';
	if(grpId!=null && grpId!='') href = href + "&grpId="+grpId;
	location.replace(href);
}<%// 트리의 확장 데이터 리턴 %>
function getTreeData(){
	var sel = TREE.getTree('grpTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return {id:'ROOT'};
	} else {
		var exts = TREE.getExtData('grpTree','exts');
		return exts;
	}
}<%// 트리의 항목 위로/아래로 이동 %>
function move(direction){
	var sel = TREE.getTree('grpTree').selected;
	if(sel==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if($(sel).attr('id')=='ROOTLI'){
		alertMsg('cm.msg.move.root');<%//최상위 항목은 이동 할 수 없습니다.%>
	} else if(direction=='up' && $(sel).prev().length==0){
		alertMsg('cm.msg.move.first.up');<%//맨 위의 항목 입니다.%>
	} else if(direction=='down' && $(sel).next().length==0){
		alertMsg('cm.msg.move.last.down');<%//맨 아래의 항목 입니다.%>
	} else {
		
		var grpPid, cd = $(sel).attr('id');
		if(cd.length>2){
			cd = cd.substring(0, cd.length-2);
			grpPid = TREE.getExtData('grpTree','exts')['pid'];
			callAjax('./transGrpMoveAjx.do?menuId=${menuId}', {grpIds:[cd], grpPid:grpPid, direction:direction}, function(data){
				if(data.message!=null){
					alert(data.message);
				} else {
					if(direction=='up'){
						$(sel).prev().before(sel);
						$(sel).removeClass('end');
						$(sel).parent().children(":last-child").addClass("end");
					} else if(direction=='down'){
						$(sel).next().removeClass('end');
						$(sel).next().after(sel);
						$(sel).parent().children(":last").addClass("end");
					}
				}
			});
		}
	}
}<%// 삭제%>
function delGrp(){
	var sel = TREE.getTree('grpTree').selected;
	if(sel==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
		return;
	} else if($(sel).attr('id')=='ROOTLI'){
		alertMsg("cm.msg.del.root");<%//cm.msg.del.root=최상위 항목은 삭제 할 수 없습니다.%>
		return;
	} 
	
	var exts = TREE.getExtData('grpTree','exts');
	if(exts==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if('ROOT'==exts['id']){
		alertMsg("cm.msg.del.root");<%//cm.msg.del.root=최상위 항목은 삭제 할 수 없습니다.%>
	} else if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		var grpIds=[];
		grpIds.push(exts['grpId']);
		callAjax('./transGrpDelAjx.do?menuId=${menuId}', {grpIds:grpIds}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				var $node = $('#'+exts['grpId']+'LI');
				var $next = $node.prev();
				if($next.length>0){
					var id = $next.attr('id');
					parent.reloadTree(id.substring(0,id.length-2));
				} else if(($next = $node.next()).length>0){
					var id = $next.attr('id');
					parent.reloadTree(id.substring(0,id.length-2));
				} else {
					parent.reloadTree(exts['pid']);
				}
			}
		});
	}
};<%// 트리의 선택 데이터 리턴 %>
function getTreeSelect(mode){
	var sel = TREE.getTree('grpTree').selected;
	if($(sel).attr('id')=='ROOTLI' && (mode==undefined || mode=='')){
		return null;
	} else {
		if($(sel).attr('id')=='ROOTLI') return [{id:'ROOT',nm:'<u:msg titleId="wh.jsp.sysMd.title" alt="시스템모듈"/>'}];
		var exts = TREE.getExtData('grpTree','exts');
		return [{id:exts.grpId,nm:exts.grpNm}];
	}
}<%// 트리의 선택 ID 리턴 %>
function getSelectId(){
	var sel = TREE.getTree('grpTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('grpTree','exts');
		return exts.grpId;
	}
}<%// [선택된 폴더 목록] - unselect:선택 해제 여부  %>
function getSelectedNodes(unselect){
	var arr=[], $on = $("#grpTree").find("a.menu_open");
	var extObj;
	$on.each(function(){
		extObj = getNodeValue($(this).parent().parent());
		arr.push(extObj);
	});
	if(unselect == true){
		$on.removeClass("menu_open");
	}
	return arr;
}<%// [선택된 양식ID] %>
function getSelectedFormNo(){
	var sel = TREE.getTree('grpTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('grpTree','exts');
		if(exts['grpTyp']!='A') return null;
		return exts.grpId;
	}
}<%// [선택된 양식데이타 리턴] %>
function getSelectedFormData(){
	var sel = TREE.getTree('grpTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('grpTree','exts');
		if(exts['grpTyp']!='A') return null;
		var arr={};
		arr['formNo']=exts.grpId; // 양식번호
		arr['formNm']=exts.grpNm; // 양식명
		arr['genId']=exts.genId; // 생성ID
		arr['rescId']=exts.rescId; // 리소스ID
		return arr;
	}
}<%// [선택된 양식 목록] - unselect:선택 해제 여부  %>
function getSelectedForm(unselect){
	var arr=[], $on = $("#grpTree").find("a.menu_open");
	var extObj;
	$on.each(function(){
		extObj = getNodeValue($(this).parent().parent());
		if(extObj.grpTyp!='A') return true;
		arr.push(extObj);
	});
	if(unselect == true){
		$on.removeClass("menu_open");
	}
	return arr;
}<%// 해당 노드의 확장 데이터 Object 리턴 %>
function getNodeValue($li){
	var exts = $li.attr("data-exts");
	if(exts==null){ return {grpId:'ROOT'}; }
	var extObj = $.parseJSON(exts);
	delete extObj.title;
	delete extObj.useYn;
	extObj['rescNm'] = $li.children("nobr").children("a:last").text();
	return extObj;
}<%// tree 스크립트로 생성 %>
$(document).ready(function() {
	
	var tree = TREE.create('grpTree');
	tree.onclick = 'onTreeClick';
	//tree.setIconTitle('${iconTitle}');
	tree.setRoot('ROOT', '<u:msg titleId="cols.fld" alt="폴더"/>');
	tree.setSkin("${_skin}");
	<c:if test="${not empty param.openLvl}">tree.openLvl = ${param.openLvl};</c:if><%// 특정폴더 하위나 상위폴더 조회시 모든 트리를 펼쳐 보이게함 %>
	<c:if test="${not empty param.treeSelectOption}">TREE.selectOption = '${param.treeSelectOption}';</c:if><%// 여러개 선택 가능 하도록 %>
	
	<%// tree.add() param : grpPid, id, name, type, sortOrdr %>
	<c:forEach items="${wfFormGrpBVoList}" var="wfFormGrpBVo" varStatus="status" >
	tree.add("${wfFormGrpBVo.grpPid}","${wfFormGrpBVo.grpId}","<u:out value='${wfFormGrpBVo.grpNm}' type='script' />","${wfFormGrpBVo.grpTyp}","0${wfFormGrpBVo.sortOrdr}","${wfFormGrpBVo.rescId}",{grpId:"${wfFormGrpBVo.grpId}",grpNm:"<u:out value='${wfFormGrpBVo.grpNm}' type='script' />",grpPid:"${wfFormGrpBVo.grpPid}",genId:"${wfFormGrpBVo.genId}",rescId:"${wfFormGrpBVo.rescId}",useYn:"${wfFormGrpBVo.useYn}",grpTyp:"${wfFormGrpBVo.grpTyp}", title:"<u:out value='${wfFormGrpBVo.grpNm}' type='script' />"});
	</c:forEach>
	if(tree.nodeSize>1000)
		tree.openLvl = 1;
	tree.draw();
	tree.selectTree("${selectedNode}");
	<c:if test="${empty param.noInitClick}">onTreeClick('${selectedNode}','');</c:if>	
});
//-->
</script>
<div id="grpTree" class="tree"></div>
