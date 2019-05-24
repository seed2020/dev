<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String callback = (String)request.getAttribute("callback");
	if(callback==null || callback.isEmpty()) callback = "openUserListFrm";
%>
<script type="text/javascript">
<!--
var gOnlyDept = false;<%// 클릭하였을때 파트일 경우 부서정보로 바꿔서 리턴함 - parent 에서 값 변경 %>
<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name, rescId){
	if(id==null || id=='') return;
	if(gOnlyDept){
		callDeptClick(id);
	} else {
		parent.<%= callback%>(id, name, rescId);
	}
}
<%// 부서ID로만 parent 의 callback 함수 호출 %>
function callDeptClick(orgId){
	var node, exts, tree = TREE.getTree('orgTree');
	while(true){
		node = tree.nodes[orgId];
		if(node==null) return;
		exts = node['exts'];
		if(exts==null) return;
		if(exts['orgTypCd']!='P'){
			parent.<%= callback%>(orgId, null, exts['rescId']);
			return;
		}
		orgId = exts['orgPid'];
	}
}
<%// 선택된 노드 클릭 함수 호출 %>
function callSelectedClick(){
	var sel = TREE.getTree('orgTree').selected;
	var selId = $(sel).attr('id');
	selId = selId.substring(0, selId.length-2);
	var selName = $(sel).find("nobr a:last").text();
	var exts = TREE.getExtData('orgTree','exts');
	onTreeClick(selId, selName, exts==null ? '' : exts['rescId']);
}
<%// 트리의 확장 데이터 리턴 %>
function getTreeData(forOption){
	var sel = TREE.getTree('orgTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		if(forOption=='mod'){
			alertMsg('cm.msg.mod.root');<%//최상위 항목은 수정 할 수 없습니다.%>
			return null;
		} else if(forOption=='reg'){
			return {orgId:'ROOT', mnuGrpId:'${param.mnuGrpId}'};
		}
	} else {
		var exts = TREE.getExtData('orgTree','exts');
		return exts;
	}
}
<%// 트리의 항목 위로/아래로 이동 %>
function move(direction){
	var sel = TREE.getTree('orgTree').selected;
	if(sel==null){
		alertMsg('cm.msg.noSelect');<%//선택한 항목이 없습니다.%>
	} else if($(sel).attr('id')=='ROOTLI'){
		alertMsg('cm.msg.move.root');<%//최상위 항목은 이동 할 수 없습니다.%>
	} else if(direction=='up' && $(sel).prev().length==0){
		alertMsg('cm.msg.move.first.up');<%//맨 위의 항목 입니다.%>
	} else if(direction=='down' && $(sel).next().length==0){
		alertMsg('cm.msg.move.last.down');<%//맨 아래의 항목 입니다.%>
	} else {
		
		var mnuPid, selId = $(sel).attr('id');
		if(selId.length>2){
			selId = selId.substring(0, selId.length-2);
			mnuPid = TREE.getExtData('orgTree','exts')['orgPid'];
			callAjax('./transOrgMoveAjx.do?menuId=${menuId}', {orgId:selId, mnuPid:mnuPid, direction:direction}, function(data){
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
function reoladFrame(orgId){
	var href = './treeOrgFrm.do?menuId=${menuId}';
	if(orgId!=null && orgId!='') href = href + "&orgId="+orgId;
	location.replace(href);
}
<%// [메뉴조합구성:부서선택 팝업에서 호출] - unselect:선택 해제 여부  %>
function getSelectedNodes(unselect){
	var arr=[], $on = $("#orgTree").find("a.menu_open");
	var extObj;
	$on.each(function(){
		extObj = getNodeValue($(this).parent().parent());
		arr.push(extObj);
	});
	if(unselect == true){
		$on.removeClass("menu_open");
	}
	return arr;
}
<%// 선택된 부서의 해당하는 상위 기관 코드/명을 함께 리턴 - 결재/수신처 에서 사용 %>
function getSelectedNodesWithParentG(orgTypCd){
	var arr=[], $on = $("#orgTree").find("a.menu_open");
	var extObj, $li, pExtObj;
	$on.each(function(){
		$li = $(this).parent().parent();
		extObj = getNodeValue($li);
		if(extObj.orgTypCd=='D' || extObj.orgTypCd=='P'){
			pExtObj = extObj;
			while(pExtObj.orgTypCd=='D' || pExtObj.orgTypCd=='P'){
				$li = $(getParentTag($li[0], 'li'));
				if($li==null){
					pExtObj = null;
					break;
				}
				pExtObj = getNodeValue($li);
			}
			if(pExtObj != null){
				extObj['gOrgId'] = pExtObj.orgId;
				extObj['gRescNm'] = pExtObj.rescNm;
				arr.push(extObj);
			}
		} else {
			arr.push(extObj);
		}
	});
	return arr;
}
<%// 해당 노드의 확장 데이터 Object 리턴 %>
function getNodeValue($li){
	var exts = $li.attr("data-exts");
	if(exts==null){ return {orgId:'ROOT'}; }
	var extObj = $.parseJSON(exts);
	delete extObj.title;
	delete extObj.useYn;
	extObj['rescNm'] = $li.children("nobr").children("a:last").text();
	return extObj;
}
<%// 선택된 조직의 부서(파트면 상위) 목록 리턴  %>
function getSelectedDept(){
	var arr=[], ids=[], $on = $("#orgTree").find("a.menu_open");
	var extObj;
	$on.each(function(){
		extObj = getNodeValue($(this).parent().parent());
		while(extObj['orgTypCd']=='P'){
			extObj = getNodeValue($("#"+extObj['orgPid']+"LI"));
		}
		if(!ids.contains(extObj['orgId'])){
			ids.push(extObj['orgId']);
			arr.push(extObj);
		}
	});
	return arr;
}
<%// 선택 해제 %>
function deSelectNodes(){
	$("#orgTree").find("a.menu_open").removeClass("menu_open");
}
<%// tree 스크립트로 생성 %>
$(document).ready(function() {
	
	var tree = TREE.create('orgTree');
	tree.onclick = 'onTreeClick';
	tree.setIconTitle('${iconTitle}');
	tree.setRoot('ROOT', '<u:msg titleId="or.label.orgRootName" alt="조직도"/>');
	tree.setSkin("${_skin}");
	<c:if test="${not empty openLvl}">tree.openLvl = ${openLvl};</c:if><%// 특정부서 하위나 상위부서 조회시 모든 부서를 펼쳐 보이게함 %>
	<c:if test="${not empty treeSelectOption}">TREE.selectOption = ${treeSelectOption};</c:if><%// 부서선택 팝업용 - 여러개 선택 가능 하도록 %>
	
	<%// tree.add() param : orgPid, id, name, type, sortOrdr %>
	<c:forEach items="${orOrgBVoList}" var="orOrgBVo" varStatus="status" ><c:if test="${empty noPart or orOrgBVo.orgTypCd != 'P'}">
	tree.add("${orOrgBVo.orgPid}","${orOrgBVo.orgId}","<u:out value='${orOrgBVo.rescNm}' type='script' />","${orOrgBVo.orgTypCd}","${orOrgBVo.sortOrdr}","${orOrgBVo.rescId}",{orgId:"${orOrgBVo.orgId}",orgPid:"${orOrgBVo.orgPid}",compId:"${orOrgBVo.compId}",rescId:"${orOrgBVo.rescId}",useYn:"${orOrgBVo.useYn}",title:"${orOrgBVo.orgId}", orgTypCd:"${orOrgBVo.orgTypCd}", orgAbbrRescNm:"${orOrgBVo.orgAbbrRescNm}"});
	</c:if></c:forEach>
	tree.draw();<c:if test="${empty noInitSelect}"><c:if
		test="${not empty param.orgId}"><%// 파라미터 orgId 가 선택 되도록함 %>
	tree.selectTree("${param.orgId}");
	onTreeClick("${param.orgId}");</c:if><c:if
		test="${empty param.orgId and not empty selectedOrgId}"><%// 회사관리에서 회사관리자 선택시 - 회사를 열어줌 %>
	tree.selectTree("${selectedOrgId}");
	onTreeClick("${selectedOrgId}");</c:if><c:if
		test="${empty param.orgId and empty selectedOrgId}"><%// 사용자가 속한 부서가 선택 되도록 함 %>
	tree.selectTree("${sessionScope.userVo.orgId}");
	onTreeClick("${sessionScope.userVo.orgId}");</c:if>
	<c:if test="${not empty processSelect}">parent.processSelect(tree);</c:if><%// 부서선택 팝업용 - 이전 선택 다시 하도록 %></c:if>
	<c:if test="${callback=='openDemoUser'}">
	var storage = window.localStorage;
	if(storage != null){
		var demoOrgId = storage.getItem('demoOrgId');
		if(demoOrgId!=null && demoOrgId!=''){
			parent.gDemoOrgId = demoOrgId;
			tree.selectTree(demoOrgId);
			parent.openDemoUser(demoOrgId);
		} else {
			var obj = getNodeValue($('#ROOTUL li:first li:first'));
			if(obj.orgId=='ROOT') obj = getNodeValue($('#ROOTUL li:first'));
			if(obj.orgId!='ROOT'){
				demoOrgId = obj.orgId;
				tree.selectTree(demoOrgId);
				parent.openDemoUser(demoOrgId);
			}
		}
	}
	</c:if>
	<c:if test="${not empty treeSelectOption}">deSelectNodes();</c:if><%// 다중선택이면 기본 선택 제거 %>
});<%
// 삭제된 부서 지우기 %>
function hideDelTree(flag){
	var $me, exts, extObj;
	$("#ROOTUL LI").each(function(){
		$me = $(this);
		exts = $me.attr("data-exts");
		if(exts==null || exts=="") return;
		extObj = $.parseJSON(exts);
		if("N"==extObj.useYn){
			$me.css("display", flag ? "none" : "");
		}
	});
}
//-->
</script>

<div id="orgTree" class="tree"></div>
