<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--

<% // TREE %>
function onTreeClick() {
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
	if (validator.validate('findBcFldForm')) {
		document.searchForm.submit();
	}
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


<%// tree 스크립트로 생성 %>
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
});

$(document).ready(function() {
	setUniformCSS();
	//$('#schWord').focus();
	
	if($('#schWord').val() != '' ){
		var tree = TREE.getTree('cdTree');
		fnSchArrs(tree);
	}
});
	
//-->
</script>
<u:searchArea>
	<form id="findBcFldForm" name="searchForm" action="./findBcFldFrm.do" >
		<u:input type="hidden" id="menuId" value="${menuId}" />
		<u:input type="hidden" id="schBcRegrUid" value="${param.schBcRegrUid }"/>
		<table class="search_table" cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td>
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="search_tit"><u:msg titleId="cols.fldNm" alt="폴더명" /></td>
							<td><u:input id="schWord" maxByte="50" name="schWord" value="${param.schWord }" titleId="cols.schWord" style="width: 200px;" mandatory="Y" /></td>
						</tr>
					</table>
				</td>
				<td>
					<div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:;" onclick="searchBtn();"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div>
				</td>
			</tr>
		</table>
	</form>
</u:searchArea>

<% // tree %>
<u:titleArea outerStyle="height:300px;"
innerStyle="width:100%; height:300px; overflow:auto;">
<div id="cdTree" class="tree"></div>
</u:titleArea>
