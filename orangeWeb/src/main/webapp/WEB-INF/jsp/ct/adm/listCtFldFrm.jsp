<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name){
	//var hasChild = $('#'+id+'LI').find("ul:first").children().length > 0;
	//parent.openCdList(hasChild ? 'ROOT' : id);
	parent.openCdList(id);
};

// 트리 선택
function onSelectFld(id){
	var tree = TREE.getTree('cdTree');
	tree.selectTree(id);
};

<%// 폴더 삭제%>
function delFld(){
	var exts = TREE.getExtData('cdTree','exts');
	if(exts==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if('ROOT'==exts['id']){
		alertMsg('pt.msg.not.del.cd.sys');<%//pt.msg.not.del.cd.sys=시스템 코드는 삭제 할 수 없습니다.%>
	} else if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transCtFldDelAjx.do?menuId=${menuId}', {mode:'delete', catId:exts['id']}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				var $node = $('#'+exts['id']+'LI');
				var $next = $node.prev();
				if($next.length>0){
					var id = $next.attr('id');
					reload('./listCtFldFrm.do?menuId=${menuId}&bcFldId='+id);
				} else if(($next = $node.next()).length>0){
					var id = $next.attr('id');
					reload('./listCtFldFrm.do?menuId=${menuId}&bcFldId='+id);
				} else {
					reload('./listCtFldFrm.do?menuId=${menuId}&bcFldId='+exts['pid']);
				}
			}else{
				return null;
			}
			
			
		});
	}
};

<%// 트리의 확장 데이터 리턴 %>
function getTreeData(forOption, regCls){
	var sel = TREE.getTree('cdTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		if(forOption=='mod'){
			alertMsg('cm.msg.mod.root');<%//최상위 항목은 수정 할 수 없습니다.%>
			return {id:'ROOT'};
		} else if(regCls=='cls'){
			alertMsg('ct.msg.cls.reg.root');<%//분류 등록은 최상위 항목에 등록 할 수 없습니다.%>
		} else if(forOption=='reg'){
			return {id:'ROOT'};
		}
	}
	return {id:TREE.getExtData('cdTree','exts').id};
};


<% // TREE %>
$(document).ready(function() {
	var tree = TREE.create('cdTree');
	tree.onclick = 'onTreeClick';
	tree.setRoot('ROOT', 'Category');
	tree.setSkin("${_skin}");
	//tree.openLvl = 1;
	
	<%// tree.add() param : pid, id, name, type, sortOrdr %>
	<c:forEach items="${ctCatFldBVoList}" var="list" varStatus="status" >
		tree.add("${list.catPid}","${list.catId}","<u:out value='${list.catNm}' type='script' />","F","${list.catOrdr}",null,{pid:"${list.catPid}",id:"${list.catId}",fldNm:"${list.catNm}"});
	</c:forEach>
	tree.draw();
	<u:set test="${empty param.catId}" var="selectedNode" value="ROOT" elseValue="${param.catId}" />
	tree.selectTree("${selectedNode}");
	//onTreeClick("${selectedNode}");
});

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<div id="cdTree" class="tree"></div>
