<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--

<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name){
	var hasChild = $('#'+id+'LI').find("ul:first").children().length > 0;
	parent.openCdList(hasChild ? 'ROOT' : id);
}
<%// 폴더 삭제%>
function delFld(){
	var exts = TREE.getExtData('cdTree','exts');
	if(exts==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if('Y'==exts['sysYn'] || 'ROOT'==exts['id']){
		alertMsg('pt.msg.not.del.cd.sys');<%//pt.msg.not.del.cd.sys=시스템 코드는 삭제 할 수 업습니다.%>
	} else if(confirmMsg("cm.cfrm.del")) {<%//cm.cfrm.del=삭제하시겠습니까 ?%>
		callAjax('./transCdDelAjx.do?menuId=${menuId}', {mode:'delete', clsCd:exts['pid'], cds:[exts['id']]}, function(data){
			if(data.message!=null){
				alert(data.message);
			}
			if(data.result=='ok'){
				var $node = $('#'+exts['id']+'LI');
				var $next = $node.prev();
				if($next.length>0){
					var id = $next.attr('id');
					reload('./treeCdFrm.do?menuId=${menuId}&clsCd='+id.substring(0,id.length-2)+'&reloadRight=Y');
				} else if(($next = $node.next()).length>0){
					var id = $next.attr('id');
					reload('./treeCdFrm.do?menuId=${menuId}&clsCd='+id.substring(0,id.length-2)+'&reloadRight=Y');
				} else {
					reload('./treeCdFrm.do?menuId=${menuId}&clsCd='+exts['pid']+'&reloadRight=Y');
				}
			}
		});
	}
}
<%// 트리의 확장 데이터 리턴 %>
function getTreeData(forOption){
	var sel = TREE.getTree('cdTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		if(forOption=='mod'){
			alertMsg('cm.msg.mod.root');<%//최상위 항목은 수정 할 수 없습니다.%>
			return null;
		} else if(forOption=='reg'){
			return {id:'ROOT'};
		}
	} else {
		var exts = TREE.getExtData('cdTree','exts');
		if(exts!=null){
			if(forOption=='reg'){
				var count = -1;
				callAjax('./getChildCountAjx.do?menuId=${menuId}', {clsCd:exts['id'], fldYn:'N'}, function(data){
					count = data.count;
				});
				if(count>0){
					alertMsg("pt.msg.not.reg.fld.withChild");<%//pt.msg.not.reg.fld.withChild=자식코드가 있는 폴더에 하위폴더를 생성 할 수 없습니다.%>
					return null;
				}
			}
		}
		return exts;
	}
}
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
			callAjax('./transCdMoveAjx.do?menuId=${menuId}', {clsCd:clsCd, cds:[cd], direction:direction}, function(data){
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
<%// tree 스크립트로 생성 %>
$(document).ready(function() {
	
	var tree = TREE.create('cdTree');
	tree.onclick = 'onTreeClick';
	tree.setRoot('ROOT', '<u:msg titleId="pt.jsp.setCd.topTreeName"/>');<%//pt.jsp.setCd.topTreeName=최상위코드%>
	tree.setSkin("${_skin}");
	tree.openLvl = 1;
	
	<%// tree.add() param : pid, id, name, type, sortOrdr %>
	<c:forEach items="${ptCdBVoList}" var="ptCdBVo" varStatus="status" >
	tree.add("${ptCdBVo.clsCd}","${ptCdBVo.cd}","<u:out value='${ptCdBVo.rescNm}' type='script' />","F","${ptCdBVo.sortOrdr}","${ptCdBVo.rescId}",{pid:"${ptCdBVo.clsCd}",id:"${ptCdBVo.cd}",sysYn:"${ptCdBVo.sysCdYn}",title:"${ptCdBVo.cd}"});
	</c:forEach>
	tree.draw();
	<u:set test="${param.cd==null or param.cd==''}" var="selectedNode" value="${param.clsCd}" elseValue="${param.cd}" />
	tree.selectTree("${selectedNode}");
	<c:if test="${param.reloadRight=='Y'}">onTreeClick('${selectedNode}','');</c:if>
});

//-->
</script>

<!--Tree S-->
<div id="cdTree" class="tree">
</div>
<!--//Tree E-->
