<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[<%
// -- searchParam --
// selection		- single:한부서선택, multi:여러부서 선택
// selected      - 선택된 부서 (multi : obj배열 , single : orgId)
%>
<%
// [트리에서 선택된 노드배열] - unselect:선택 해제 여부  %>
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
}<%
// 해당 노드의 확장 데이터 Object 리턴 %>
function getNodeValue($li){
	var exts = $li.attr("data-exts");
	if(exts==null){ return {orgId:'ROOT'}; }
	var extObj = $.parseJSON(exts);
	delete extObj.title;
	delete extObj.useYn;
	extObj['rescNm'] = $li.children("nobr").children("a:last").text();
	return extObj;
}

$(document).ready(function() {
	<%// 선택 영역에 부서 세팅 %>
	if('${param.selection}' == 'multi' && $m.org.selected != null){
		addToSelected($m.org.selected,'init');		
	}
	<%// 트리 초기화 %>
	initTree();
});<%
// 확인 버튼 - 1부서 %>
function setOneOrg(){
	var arr = getSelectedNodes(false);
	var obj = null;
	if(arr!=null && arr.length>0){
		obj = arr[0];
		if(obj.orgId=='ROOT'){
			$m.dialog.alert("<u:msg titleId="or.msg.not.select.top" alt="조직도의 최상위는 선택 할 수 없습니다."/>");
			return;
		}
	}
	if($m.org.callback(obj) != false){
		history.back();
	}
}<%
// 확인 버튼 - 여러부서 %>
function setOrgs(){
	var arr = getSelected();
	if($m.org.callback(arr.length==0 ? null : arr) != false){
		history.back();
	}
}<%
// [+] 더하기 버튼 %>
function addChecked(){
	var arr = getSelectedNodes(true);
	if(arr.length == 0){
		// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.. - [부서]%>
		$m.msg.alertMsg("cm.msg.noSelectedItem", ["#cols.org"]);<%// cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.%>
	}
	if(arr.length>0){
		if(arr[0].orgId=='ROOT'){
			arr = arr.slice(1);
		}
		addToSelected(arr.reverse());
	}
}<%
// 선택항목에 더하기 %>
function addToSelected(arr, type){
	if(arr==null || arr.length==0) return;
	var obj, txt;
	
	var aleadyHas, aleadys = getSelected();
	
	var area = getSelectedListArea();
	var empty = area.find('dd:first').length==0;
	arr.each(function(index, item){
		txt = JSON.stringify(item);
		obj = item;
		
		if(type != 'init'){
			aleadyHas = false;
			aleadys.each(function(index, oldItem){
				if(oldItem.orgId==obj.orgId){
					aleadyHas = true;
					return false;
				}
			});
			if(aleadyHas) return;
		}
		
		if(index!=0 || !empty){
			area.append('<dd class="blank">l</dd>');
		}
		txt = txt.replace(/\'/g,"&apos;");
		area.append('<dd class="txt" onclick="highlightTouched(this)" data-org=\''+txt+'\'>'+(obj.rescNm==null ? 'NO-NAME' : obj.rescNm)+'</dd>');
	});
}<%
// [-] 빼기 버튼 %>
function removeChecked(){
	var selected = getSelectedListArea().children("dd[class='txt_on']");
	if(selected.length>0){
		var prev = selected.prev();
		if(prev.attr('class')=='blank') prev.remove();
		selected.remove();
	}
}<%
// 선택항목 - obj 배열로 리턴 %>
function getSelected(){
	var arr = [];
	getSelectedListArea().children("dd[class!='blank']").each(function(){
		arr.push(JSON.parse($(this).attr('data-org')));
	});
	return arr;
}<%
// 선택 영역 리턴 %>
var selectedListArea = null;
function getSelectedListArea(){
	if(selectedListArea==null){
		selectedListArea = $('#section').children('#selectedArea').find('#selectedListArea');
	}
	return selectedListArea;
}<%
// 터치한것 선택 표시 %>
function highlightTouched(obj, isCurrOrg){
	var area = getSelectedListArea();
	area.find('dd.txt_on').attr('class','txt');
	area.find('dd.txt_gon').attr('class','txt');
	if(obj!=null){
		if(isCurrOrg==true) $(obj).attr('class','txt_gon');
		else $(obj).attr('class','txt_on');
	}
}<%
// 트리용 Object %>
var tree = null;
function initTree(){
	tree = TREE.create('orgTree');
	tree.onclick = 'onTreeClick';
	tree.setIconTitle('${iconTitle}');
	tree.setRoot('ROOT', '<u:msg titleId="or.label.orgRootName" alt="조직도"/>');
	tree.setSkin("${_skin}");
	<c:if test="${param.selection=='multi'}">TREE.selectOption = 2;</c:if><%// 부서선택 팝업용 - 여러개 선택 가능 하도록 %>
	$m.ajax('${_cxPth}/or/user/treeOrgAjx.do', null, function(data){
		if(data.openLvl != null) tree.openLvl = data.openLvl;
		var i, org, orgArr = data.orOrgBVoList;
		for(i=0;i<orgArr.length;i++){
			org = orgArr[i];
			tree.add(org.orgPid, org.orgId, org.rescNm, org.orgTypCd, org.sortOrdr, org.rescId, {orgId:org.orgId, orgPid:org.orgPid, compId:org.compId, rescId:org.rescId, useYn:org.useYn, orgTypCd:org.orgTypCd, orgAbbrRescNm:org.orgAbbrRescNm});
		}
		tree.draw();
		if('${param.selection}' == 'single' && $m.org.selected != null) tree.selectTree($m.org.selected);
		if('${param.selection}' == 'multi' || $m.org.selected == null) tree.selectTree("${sessionScope.userVo.orgId}");
	}, {async:true});
}<%
// 트리 클릭 %>
function onTreeClick(orgId, orgNm){
}
//]]>
</script>
	<u:set var="setOrg" test="${param.selection eq 'multi' }" value="setOrgs" elseValue="setOneOrg"/>
	<header>
		<div class="back" onclick="history.back();"></div>
	    <div class="subtit2"><u:msg titleId="or.label.orgRootName" alt="조직도"/></div>
	    <div class="save" onclick="${setOrg}();"></div>
	</header>
	
	<section id="section">
		<div id="tabViewArea">
			<div id="treeArea" class="unified_tree2" style="${param.selection=='single' ? 'bottom:98px; ' : ''}top:50px;">
			<div class="unified_treearea" style="overflow:auto;">
			<div class="treearea"><div class="tree" id="orgTree"></div></div>
		</div>
		</div>
		</div>
		<c:if	
			test="${param.selection == 'single'}">
		<div class="unified_btn2">
		<div class="btnarea">
			<div class="size">
			<dl>
				<dd class="btn" onclick="setOneOrg()"><u:msg titleId="mcm.btn.ok" alt="확인" /></dd>
				<dd class="btn" onclick="history.back();"><u:msg titleId="cm.btn.cancel" alt="취소"/></dd>
			</dl>
			</div>
		</div>
		</div></c:if><c:if
			test="${param.selection != 'single'}"><u:cmt cmt="일반 부서 선택" />
		<div class="unified_btn">
		<div class="btnarea">
			<div class="size">
			<dl>
				<dd class="plus" onclick="addChecked()"></dd>
				<dd class="minus" onclick="removeChecked()"></dd>
				<dd class="btn" onclick="setOrgs()"><u:msg titleId="mcm.btn.ok" alt="확인" /></dd>
			</dl>
			</div>
		</div>
		</div></c:if>
		<c:if
			test="${param.selection != 'single'}"><u:cmt cmt="선택한 부서 모으는 영역" />
		<div class="unified_btm" id="selectedArea">
			<div class="unified_btm_icol"></div>
			<div class="unified_btm_icor"></div>
			<div class="unified_btmin">
				<dl id="selectedListArea"></dl>
			</div>
		</div>
		</c:if>
		
	</section>