<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="openListFrm"/>
<script type="text/javascript">
<!--<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name, rescId){
	if(id==null || id=='') return;
	if(id=='ROOT'){
		parent.${callback}(id, true);
		return;
	}
	var exts = TREE.getExtData('mdTree','exts');
	if(exts==null) return;
	parent.${callback}(id, exts.mdTypCd=='F');
}<%// html reload %>
function reloadFrame(mdId){
	var href = './treeMdFrm.do?menuId=${menuId}';
	if(mdId!=null && mdId!='') href = href + "&mdId="+mdId;
	location.replace(href);
}<%// 트리의 확장 데이터 리턴 %>
function getTreeData(){
	var sel = TREE.getTree('mdTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return {id:'ROOT'};
	} else {
		var exts = TREE.getExtData('mdTree','exts');
		return exts;
	}
}<%// 트리의 항목 위로/아래로 이동 %>
function move(direction){
	var sel = TREE.getTree('mdTree').selected;
	if(sel==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if($(sel).attr('id')=='ROOTLI'){
		alertMsg('cm.msg.move.root');<%//최상위 항목은 이동 할 수 없습니다.%>
	} else if(direction=='up' && $(sel).prev().length==0){
		alertMsg('cm.msg.move.first.up');<%//맨 위의 항목 입니다.%>
	} else if(direction=='down' && $(sel).next().length==0){
		alertMsg('cm.msg.move.last.down');<%//맨 아래의 항목 입니다.%>
	} else {
		
		var mdPid, cd = $(sel).attr('id');
		if(cd.length>2){
			cd = cd.substring(0, cd.length-2);
			mdPid = TREE.getExtData('mdTree','exts')['pid'];
			callAjax('./transMdMoveAjx.do?menuId=${menuId}', {mdIds:[cd], mdPid:mdPid, direction:direction}, function(data){
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
function delMd(){
	var sel = TREE.getTree('mdTree').selected;
	if(sel==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
		return;
	} else if($(sel).attr('id')=='ROOTLI'){
		alertMsg('pt.msg.not.del.cd.sys');<%//pt.msg.not.del.cd.sys=시스템 코드는 삭제 할 수 업습니다.%>
		return;
	} 
	
	var exts = TREE.getExtData('mdTree','exts');
	if(exts==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if('ROOT'==exts['id']){
		alertMsg('pt.msg.not.del.cd.sys');<%//pt.msg.not.del.cd.sys=시스템 코드는 삭제 할 수 업습니다.%>
	} else if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		var mdIds=[];
		mdIds.push(exts['mdId']);
		callAjax('./transMdDelAjx.do?menuId=${menuId}', {mdIds:mdIds}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				var $node = $('#'+exts['mdId']+'LI');
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
	var sel = TREE.getTree('mdTree').selected;
	if($(sel).attr('id')=='ROOTLI' && (mode==undefined || mode=='')){
		return null;
	} else {
		if($(sel).attr('id')=='ROOTLI') return [{id:'ROOT',nm:'<u:msg titleId="wh.jsp.sysMd.title" alt="시스템모듈"/>'}];
		var exts = TREE.getExtData('mdTree','exts');
		return [{id:exts.mdId,nm:exts.mdNm}];
	}
}<%// 트리의 선택 ID 리턴 %>
function getSelectId(){
	var sel = TREE.getTree('mdTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('mdTree','exts');
		return exts.mdId;
	}
}<%// tree 스크립트로 생성 %>
$(document).ready(function() {
	
	var tree = TREE.create('mdTree');
	tree.onclick = 'onTreeClick';
	//tree.setIconTitle('${iconTitle}');
	tree.setRoot('ROOT', '<u:msg titleId="wh.jsp.sysMd.title" alt="시스템모듈"/>');
	tree.setSkin("${_skin}");
	<c:if test="${not empty param.openLvl}">tree.openLvl = ${param.openLvl};</c:if><%// 특정폴더 하위나 상위폴더 조회시 모든 트리를 펼쳐 보이게함 %>
	<c:if test="${not empty param.treeSelectOption}">TREE.selectOption = '${param.treeSelectOption}';</c:if><%// 여러개 선택 가능 하도록 %>
	
	<%// tree.add() param : mdPid, id, name, type, sortOrdr %>
	<c:forEach items="${whMdBVoList}" var="whMdBVo" varStatus="status" >
	tree.add("${whMdBVo.mdPid}","${whMdBVo.mdId}","<u:out value='${whMdBVo.mdNm}' type='script' />","${whMdBVo.mdTypCd}","0${whMdBVo.sortOrdr}","${whMdBVo.rescId}",{mdId:"${whMdBVo.mdId}",mdNm:"<u:out value='${whMdBVo.mdNm}' type='script' />",mdPid:"${whMdBVo.mdPid}",mdTypCd:"${whMdBVo.mdTypCd}", rescId:"${whMdBVo.rescId}",useYn:"${whMdBVo.useYn}",title:"<u:out value='${whMdBVo.mdNm}' type='script' />"});
	</c:forEach>
	if(tree.nodeSize>1000)
		tree.openLvl = 1;
	tree.draw();<c:if test="${empty noInitSelect}"><c:if
		test="${not empty param.mdId}"><%// 파라미터 mdId 가 선택 되도록함 %>
	tree.selectTree("${param.mdId}");
	onTreeClick("${param.mdId}");</c:if><c:if
		test="${empty param.mdId}"><%// 사용자가 속한 부서가 선택 되도록 함 %>
	tree.selectTree("ROOT");
	onTreeClick("ROOT");</c:if>
	</c:if>
});
//-->
</script>
<div id="mdTree" class="tree"></div>
