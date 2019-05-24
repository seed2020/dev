<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name, rescId){
	if(id==null || id=='') return;
}           
// 노드의 폴더명 조회
function fnNodeValue( node ){
	var nodeElement = $('#'+node['id']+'LI');
	return JSON.parse(nodeElement.attr('data-exts'))['fldNm'];
};

// 폴더 검색
function fnSchTree(node , $schArrs){
	if(node.children.length > 0 ){
		for( var i=0;i<node.children.length;i++){//첫번째 트리 배열
			if((fnNodeValue(node.children[i])).indexOf($('#schWord').val()) > -1 ){
				$schArrs.push(node.children[i]['id']);
			}
			if(node.children[i].children != null){
				fnSchTree(node.children[i] , $schArrs);
			}
		}
	}
};

// 폴더 검색(배열)
function fnSchArrs( tree ){
	var nodes = tree.nodes['ROOT'].children;
	var $schArrs = [];
	for( var i=0;i<nodes.length;i++){//첫번째 트리 배열
		if((fnNodeValue(nodes[i])).indexOf($('#schWord').val()) > -1 ){
			$schArrs.push(nodes[i]['id']);
		}
		if(nodes[i].children != null){
			fnSchTree(nodes[i] , $schArrs);
		}
	}
	if($schArrs != undefined && $schArrs.length > 0 ){
		for(var i=0;i<$schArrs.length;i++){
			tree.selectTree($schArrs[i]);
		}
	}
	//return $schArrs;
};

<% // 검색 버튼 %>
function searchBtn(event) {
	$m.nav.curr(event, '/wb/findBcFldSub.do?'+$('#findBcFldForm').serialize());
};

<%// 트리의 확장 데이터 리턴 %>
function getTreeData(){
	var sel = TREE.getTree('cdTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return {id:'ROOT'};
	} else {
		return TREE.getExtData('cdTree','exts');
	}
};

$(document).ready(function() {
	var tree = TREE.create('cdTree');
	tree.onclick = 'onTreeClick';
	tree.setRoot('ROOT', '<u:msg titleId="cm.option.all" alt="전체" />');
	tree.setSkin("${_skin}");
	tree.openLvl = 1;
	
	<%// tree.add() param : pid, id, name, type, sortOrdr %>
	<c:forEach items="${wbBcFldBVoList}" var="list" varStatus="status" >
	tree.add("${list.abvFldId}","${list.bcFldId}","<u:out value='${list.fldNm}' type='script' />","F","${list.sortOrdr}",null,{pid:"${list.abvFldId}",id:"${list.bcFldId}",fldNm:"${list.fldNm}"});
	</c:forEach>
	tree.draw();
	tree.selectTree("${empty param.bcFldId ? 'ROOT' : param.bcFldId}");

	if($('#schWord').val() != '' ){
		var tree = TREE.getTree('cdTree');
		fnSchArrs(tree);
	}
});

//폴더선택
fnFldSelect = function(){
	var win = $m.nav.getWin(-1);
	if(win==null) return;
	var tree = getTreeData();
	if(tree.id == 'ROOT'){
		win.$('#schFldNm').val('');
		win.$('#schFldId').val('');
	}else{
		win.$('#schFldId').val(tree.id);
		win.$('#schFldNm').val(tree.fldNm);
	}
	history.back();
};

//]]>
</script>

<form id="findBcFldForm" name="searchForm" action="./findBcFldFrm.do" >
<input type="hidden" id="menuId" name="menuId" value="${menuId}" />
<input type="hidden" id="schBcRegrUid" name="schBcRegrUid" value="${param.schBcRegrUid }"/>
<!--listsearch S-->
<div class="listsearch">
<div class="listselect">
    <div class="input1">
    <dl>
    <dd class="input_left"></dd>
    <dd class="input_input"><input type="text" class="input_ip" id="schWord" name="schWord" value="${param.schWord }"/></dd>
    <dd class="input_btn" onclick="searchBtn();"><div class="search"></div></dd>
    </dl>
    </div>
</div>
</div>
<!--//listsearch E-->
</form>
<div id="treeArea" class="unified_tree3" >
<div class="unified_treearea" style="overflow:auto;">
	<div class="treearea"><div class="tree" id="cdTree"></div></div>
</div>
</div>
<div class="unified_btn2">
    <div class="btnarea">
        <div class="size">
        <dl>
        <dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
        <dd class="btn" onclick="fnFldSelect();"><u:msg titleId="cm.btn.choice" alt="선택"/></dd>
     </dl>
        </div>
    </div>
</div>

