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
	//var tree = TREE.getTree('cdTree');
	//tree.selectTree(id);
	TREE.select('cdTree',id);
};

<%// 폴더 삭제%>
function delFld(){
	var sel = TREE.getTree('cdTree').selected;
	if(sel==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
		return;
	} else if($(sel).attr('id')=='ROOTLI'){
		alertMsg('cm.msg.mod.root');<%//최상위 항목은 이동 할 수 없습니다.%>
		return;
	} 
	
	var exts = TREE.getExtData('cdTree','exts');
	if(exts==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if('ROOT'==exts['id']){
		alertMsg('pt.msg.not.del.cd.sys');<%//pt.msg.not.del.cd.sys=시스템 코드는 삭제 할 수 업습니다.%>
	} else if(confirmMsg("wb.cfrm.del.fld")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transBcFldDelAjx.do?menuId=${menuId}', {mode:'delete', abvFldId:exts['pid'], bcFldId:exts['id']}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				var $node = $('#'+exts['id']+'LI');
				var $next = $node.prev();
				if($next.length>0){
					var id = $next.attr('id');
					reload('./listBcFldFrm.do?menuId=${menuId}&bcFldId='+id);
				} else if(($next = $node.next()).length>0){
					var id = $next.attr('id');
					reload('./listBcFldFrm.do?menuId=${menuId}&bcFldId='+id);
				} else {
					reload('./listBcFldFrm.do?menuId=${menuId}&bcFldId='+exts['pid']);
				}
			}
			
			
		});
	}
};

<%// 트리의 확장 데이터 리턴 %>
function getTreeData(forOption){
	var sel = TREE.getTree('cdTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		if(forOption=='mod'){
			alertMsg('cm.msg.mod.root');<%//최상위 항목은 수정 할 수 없습니다.%>
			return {id:'ROOT'};
		}  else if(forOption=='copy'){
			alertMsg('cm.msg.copy.root');<%//최상위 항목은 복사 할 수 없습니다.%>
			return {id:'ROOT'};
		} else if(forOption=='move'){
			alertMsg('cm.msg.move.root');<%//최상위 항목은 이동 할 수 없습니다.%>
			return {id:'ROOT'};
		} else if(forOption=='reg'){
			return {id:'ROOT'};
		}
	}
	return {id:TREE.getExtData('cdTree','exts').id};
};

<%// 트리의 항목 위로/아래로 이동 %>
function move(direction){
	var sel = TREE.getTree('cdTree').selected;
	if(sel==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if($(sel).attr('id')=='ROOTLI'){
		alertMsg('cm.msg.move.root');<%//최상위 항목은 이동 할 수 없습니다.%>
	} else if(direction=='up' && $(sel).prev().length==0){
		alertMsg('cm.msg.move.first.up');<%//맨 위의 항목 입니다.%>
	} else if(direction=='down' && $(sel).next().length==0){
		alertMsg('cm.msg.move.last.down');<%//맨 아래의 항목 입니다.%>
	} else {
		var clsCd, cd = $(sel).attr('id');
		if(cd.length>2){
			cd = cd.substring(0, cd.length-2);
			clsCd = TREE.getExtData('cdTree','exts')['pid'];
			callAjax('./transCdMove.do?menuId=${menuId}', {clsCd:clsCd, cds:[cd], direction:direction}, function(data){
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
};

<% // TREE %>
$(document).ready(function() {
	var tree = TREE.create('cdTree');
	tree.onclick = 'onTreeClick';
	tree.setRoot('ROOT', '<u:msg titleId="cm.option.all" alt="전체" />');
	tree.setSkin("${_skin}");
	//tree.openLvl = 1;
	
	<%// tree.add() param : pid, id, name, type, sortOrdr %>
	<c:forEach items="${wbBcFldBVoList}" var="list" varStatus="status" >
	tree.add("${list.abvFldId}","${list.bcFldId}","<u:out value='${list.fldNm}' type='script' />","F","${list.sortOrdr}",null,{pid:"${list.abvFldId}",id:"${list.bcFldId}",fldNm:"${list.fldNm}"});
	</c:forEach>
	tree.draw();
	<u:set test="${empty param.bcFldId}" var="selectedNode" value="ROOT" elseValue="${param.bcFldId}" />
	tree.selectTree("${selectedNode}");
	//onTreeClick("${selectedNode}");
});

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<div id="cdTree" class="tree"></div>
