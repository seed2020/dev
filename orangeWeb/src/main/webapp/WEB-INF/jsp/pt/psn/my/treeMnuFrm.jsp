<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--

<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name){
	if(id==null || id=='') return;
	if(id=='ROOT'){
		parent.openMnu(id,'Y');
		return;
	}
	var exts = TREE.getExtData('mnuTree','exts');
	if(exts==null) return;
	parent.openMnu(id, exts.fldYn);
}
<%// 폴더 삭제%>
function delFld(){
	var exts = TREE.getExtData('mnuTree','exts');
	if(exts==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if('Y'==exts['sysYn'] || 'ROOT'==exts['id']){
		alertMsg('pt.msg.not.del.mnu.sys');<%//시스템 메뉴는 삭제 할 수 업습니다.%>
	} else if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transFldDelAjx.do?menuId=${menuId}', {mode:'delete', mnuLoutCombIds:[exts['mnuLoutCombId']]}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				var $node = $('#'+exts['id']+'LI');
				var $next = $node.prev();
				if($next.length>0){
					var id = $next.attr('id');
					reload('./treeMnuFrm.do?menuId=${menuId}&mnuLoutId=${param.mnuLoutId}&mnuLoutCombPid='+id.substring(0,id.length-2));
				} else if(($next = $node.next()).length>0){
					var id = $next.attr('id');
					reload('./treeMnuFrm.do?menuId=${menuId}&mnuLoutId=${param.mnuLoutId}&mnuLoutCombPid='+id.substring(0,id.length-2));
				} else {
					reload('./treeMnuFrm.do?menuId=${menuId}&mnuLoutId=${param.mnuLoutId}&mnuLoutCombPid='+exts['pid']);
				}
			}
		});
	}
}
<%// 트리의 확장 데이터 리턴 %>
function getTreeData(forOption){
	var sel = TREE.getTree('mnuTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		if(forOption=='mod'){
			alertMsg('cm.msg.mod.root');<%//최상위 항목은 수정 할 수 없습니다.%>
			return null;
		} else if(forOption=='reg'){
			return {mnuLoutCombId:'ROOT', mnuLoutId:'${param.mnuLoutId}'};
		}
	} else {
		var exts = TREE.getExtData('mnuTree','exts');
		if(exts!=null){
			exts['mnuLoutId'] = '${param.mnuLoutId}';
		}
		return exts;
	}
}
<%// 트리의 항목 위로/아래로 이동 %>
function move(direction){
	var sel = TREE.getTree('mnuTree').selected;
	if(sel==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if($(sel).attr('id')=='ROOTLI'){
		alertMsg('cm.msg.move.root');<%//최상위 항목은 이동 할 수 없습니다.%>
	} else if(direction=='up' && $(sel).prev().length==0){
		alertMsg('cm.msg.move.first.up');<%//맨 위의 항목 입니다.%>
	} else if(direction=='down' && $(sel).next().length==0){
		alertMsg('cm.msg.move.last.down');<%//맨 아래의 항목 입니다.%>
	} else {
		
		var mnuLoutCombPid, cd = $(sel).attr('id');
		if(cd.length>2){
			cd = cd.substring(0, cd.length-2);
			mnuLoutCombPid = TREE.getExtData('mnuTree','exts')['pid'];
			callAjax('./transFldMoveAjx.do?menuId=${menuId}', {mnuLoutCombIds:[cd], mnuLoutCombPid:mnuLoutCombPid, direction:direction}, function(data){
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
}
<%// html reload %>
function reoladFrame(mnuLoutCombPid){
	var href = './treeMnuFrm.do?menuId=${menuId}&mnuLoutId=${param.mnuLoutId}';
	if(mnuLoutCombPid!=null && mnuLoutCombPid!='') href = href + "&mnuLoutCombPid="+mnuLoutCombPid;
	location.replace(href);
}
<%// [메뉴조합구성:레이아웃구성에서 호출] - 선택된 하위 노드 리턴  %>
function getSelectedNodeTrees(mode){
	var arr=[], dup={}, $tree=$("#mnuTree"), $li;
	if(mode=='all' || $tree.find("a.menu_open:first").parent().attr('id')=='ROOTLI'){
		$tree.find("#ROOTUL").children().each(function(){
			setSelectedLi($(this), arr, dup);
		});
		$tree.find("a.menu_open").removeClass("menu_open");
	} else {
		var $on = $tree.find("a.menu_open");
		$on.each(function(){
			$li = $(this).parent().parent();
			setSelectedLi($li, arr, dup);
		});
		$on.removeClass("menu_open");
	}
	return arr;
}
function setSelectedLi($li, arr, dup){
	var exts = $li.attr("data-exts");
	if(exts!=null && exts!=''){
		var extObj = $.parseJSON(exts);
		
		if(dup[extObj.mnuLoutCombId]==null){
			delete extObj.title;
			extObj[extObj.mnuId == '' ? 'refRescId' : 'mnuRescId'] = extObj.rescId;
			delete extObj.rescId;
			extObj['mnuRescNm'] = $li.find("nobr:first a:last").text();
			
			dup[extObj.mnuLoutCombId]=extObj;
			arr.push(extObj);
			var $lis = $li.find("#"+extObj.mnuLoutCombId+"UL:first").children();
			delete extObj.mnuLoutCombId;
			if($lis.length>0){
				extObj['children'] = [];
				$lis.each(function(){
					setSelectedLi($(this), extObj['children'], dup);
				});
			}
		}
	}
}

<%// tree 스크립트로 생성 %>
$(document).ready(function() {
	
	var tree = TREE.create('mnuTree');
	tree.onclick = 'onTreeClick';
	tree.setRoot('${param.mnuLoutId}', '<u:msg titleId="pt.jsp.setMnu.topTreeName" alt="최상위메뉴"/>');
	tree.setSkin("${_skin}");
	<c:if test="${not empty treeSelectOption}">TREE.selectOption = ${treeSelectOption};</c:if>
	
	<%// tree.add() param : pid, id, name, type, sortOrdr %>
	<c:forEach items="${ptMnuLoutCombDVoList}" var="ptMnuLoutCombDVo" varStatus="status" >
	tree.add("${ptMnuLoutCombDVo.mnuLoutCombPid}","${ptMnuLoutCombDVo.mnuLoutCombId}","<u:out value='${ptMnuLoutCombDVo.rescNm}' type='script' />","<u:decode srcValue='${ptMnuLoutCombDVo.fldYn}' tgtValue='Y' value='F' elseValue='A' />","${ptMnuLoutCombDVo.sortOrdr}","${ptMnuLoutCombDVo.mnuRescId}",{mnuLoutCombId:"${ptMnuLoutCombDVo.mnuLoutCombId}",mnuId:"${ptMnuLoutCombDVo.mnuId}",title:"${ptMnuLoutCombDVo.mnuLoutCombId}",rescId:"${empty ptMnuLoutCombDVo.mnuRescId ? ptMnuLoutCombDVo.rescId : ptMnuLoutCombDVo.mnuRescId}",fldYn:"${ptMnuLoutCombDVo.fldYn}"});
	</c:forEach>
	tree.draw();
	<u:set test="${param.mnuLoutCombId==null or param.mnuLoutCombId==''}" var="selectedNode" value="${param.mnuLoutCombPid}" elseValue="${param.mnuLoutCombId}" />
	tree.selectTree("${selectedNode}");
	onTreeClick("${selectedNode}");
});
//-->
</script>

<div id="mnuTree" class="tree"></div>
