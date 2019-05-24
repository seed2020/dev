<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--
<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name){
	parent.${empty param.callback ? 'openFormList' : param.callback}(id, name);
}
<%// 폴더 삭제%>
function delFormBx(){
	var exts = TREE.getExtData('formBxTree','exts');
	if(exts==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if('ROOT'==exts['id']){
		alertMsg('ap.jsp.setApvForm.notDelRoot');<%//ap.jsp.setApvForm.notDelRoot=최사위 양식함은 삭제 할 수 없습니다.%>
	} else if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transFormBxDelAjx.do?menuId=${menuId}', {formBxPid:exts['pid'], formBxId:exts['id']}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				var $node = $('#'+exts['id']+'LI');
				var $next = $node.prev();
				if($next.length>0){
					var id = $next.attr('id');
					reload('./treeFormBxFrm.do?menuId=${menuId}&formBxId='+id.substring(0,id.length-2)+'&reloadRight=Y');
				} else if(($next = $node.next()).length>0){
					var id = $next.attr('id');
					reload('./treeFormBxFrm.do?menuId=${menuId}&formBxId='+id.substring(0,id.length-2)+'&reloadRight=Y');
				} else {
					reload('./treeFormBxFrm.do?menuId=${menuId}&formBxId='+exts['pid']+'&reloadRight=Y');
				}
			}
		});
	}
}
<%// 트리의 확장 데이터 리턴 %>
function getTreeData(forOption){
	var sel = TREE.getTree('formBxTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		if(forOption=='mod'){
			alertMsg('cm.msg.mod.root');<%//최상위 항목은 수정 할 수 없습니다.%>
			return null;
		} else if(forOption=='reg'){
			return {id:'ROOT'};
		}
	} else {
		var exts = TREE.getExtData('formBxTree','exts');
		return exts;
	}
}
<%// 트리의 항목 위로/아래로 이동 %>
function move(direction){
	var sel = TREE.getTree('formBxTree').selected;
	if(sel==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if($(sel).attr('id')=='ROOTLI'){
		alertMsg('cm.msg.move.root');<%//최상위 항목은 이동 할 수 없습니다.%>
	} else if(direction=='up' && $(sel).prev().length==0){
		alertMsg('cm.msg.move.first.up');<%//맨 위의 항목 입니다.%>
	} else if(direction=='down' && $(sel).next().length==0){
		alertMsg('cm.msg.move.last.down');<%//맨 아래의 항목 입니다.%>
	} else {
		var formBxPid, formBxId = $(sel).attr('id');
		if(formBxId.length>2){
			formBxId = formBxId.substring(0, formBxId.length-2);
			formBxPid = TREE.getExtData('formBxTree','exts')['pid'];
			callAjax('./transFormBxMoveAjx.do?menuId=${menuId}', {formBxPid:formBxPid, formBxId:formBxId, direction:direction}, function(data){
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
<%// 등록/수정 후 리로드 %>
function reloadTree(formBxId){
	reload('./treeFormBxFrm.do?menuId=${menuId}&formBxId='+formBxId);
	parent.dialog.close("setFormBxDialog");
}
<%// 양식함 등록/수정/삭제 버튼 %>
function manageFormBx(mode){
	if(mode=='del'){
		delFormBx();
	} else if(mode=='reg'){
		var tree = getTreeData('reg');
		if(tree!=null){
			parent.dialog.open('setFormBxDialog','<u:msg titleId="ap.jsp.setApvForm.regFormBx" alt="양식함 등록"/>','./setFormBxPop.do?menuId=${menuId}&mode=reg&formBxPid='+tree.id);
		}
	} else if(mode=='mod'){
		var tree = getTreeData('mod');
		if(tree!=null){
			parent.dialog.open('setFormBxDialog','<u:msg titleId="ap.jsp.setApvForm.modFormBx" alt="양식함 수정"/>','./setFormBxPop.do?menuId=${menuId}&mode=mod&formBxPid='+tree.pid+'&formBxId='+tree.id);
		}
	}
}
<%// tree 스크립트로 생성 %>
$(document).ready(function() {
	var tree = TREE.create('formBxTree');
	tree.onclick = 'onTreeClick';
	tree.setRoot('ROOT', '<u:msg titleId="ap.jsp.setApvForm.treeRoot" alt="양식함"/>');
	tree.setSkin("${_skin}");
	tree.openLvl = 1;
	
	<%// tree.add() param : pid, id, name, type, sortOrdr %>
	<c:forEach items="${apFormBxDVoList}" var="apFormBxDVo" varStatus="status" >
	tree.add("${apFormBxDVo.formBxPid}","${apFormBxDVo.formBxId}","<u:out value='${apFormBxDVo.rescNm}' type='script' />","F","${apFormBxDVo.sortOrdr}","${apFormBxDVo.rescId}",{pid:"${apFormBxDVo.formBxPid}",id:"${apFormBxDVo.formBxId}",useYn:"${apFormBxDVo.useYn}",title:"${apFormBxDVo.formBxId}"});
	</c:forEach>
	tree.draw();
	tree.selectTree("${selectedNode}");
	onTreeClick('${selectedNode}','');
});

//-->
</script>

<!--Tree S-->
<div id="formBxTree" class="tree">
</div>
<!--//Tree E-->
