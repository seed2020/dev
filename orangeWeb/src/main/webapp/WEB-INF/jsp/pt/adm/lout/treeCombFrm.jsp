<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<script type="text/javascript">
<!--

<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name){
	parent.dialog.close("setFldPopDialog");
}
<%// [아이콘 △,▽ ] - 좌상단, parent에서 호출 %>
function moveComb(direction){
	var tree = TREE.getTree('combTree');
	tree.move(direction);
}
<%// [아이콘 >, >> ] - parent에서 호출 %>
function removeMnu(mode){
	var tree = TREE.getTree('combTree');
	if(mode=='all' || tree.isRootSelected()){
		tree.removeAll();
	} else {
		tree.removeSelected();
	}
}
<%// [소버튼 좌측하단] - 폴더등록/폴더수정/폴더삭제 %>
function mngFld(mode){
	if(mode=='del'){
		removeMnu(mode);
	}
}
<%// TREE 확장 데이터 가져오기 %>
function getTreeData(mode, extId){
	var sel = TREE.getTree('combTree').selected;
	if(sel==null){
		if(mode=='mod'){
			alertMsg('cm.msg.noSelect');<%// cm.msg.noSelect=선택한 항목이 없습니다.%>
			return null;
		} else if(mode=='add'){
			return {id:'ROOT', fldYn:"Y"};
		}
	} else if($(sel).attr('id')=='ROOTLI'){
		if(mode=='mod'){
			alertMsg('cm.msg.mod.root');<%//최상위 항목은 수정 할 수 없습니다.%>
			return null;
		} else if(mode=='add'){
			return {id:'ROOT', fldYn:"Y"};
		}
	} else {
		var exts = TREE.getExtData('combTree', extId);
		if(extId=='exts' && exts.fldYn=='N'){
			<%// pt.jsp.setMnu.msg4=메뉴의 수정은 메뉴관리에서 하시기 바랍니다.%>
			alertMsg("pt.jsp.setMnu.msg4");
			return null;
		}
		return exts;
	}
}
<%// [팝업] 폴더등록/폴더수정 - 확인버튼 클릭 %>
function setFldPop(mode, values){
	var tree = TREE.getTree('combTree');
	var sel = tree.selected;
	if(mode=='mod'){
		$(sel).find("nobr:first a:last").text(values.cur);
		delete values.cur;
		$(sel).attr("data-rescs", JSON.stringify(values));
	} else if(mode=='add'){
		var pid = tree.getPidForAppend();
		var name = values.cur;
		delete values.cur;
		var nodes = [{pid:pid, id:parent.getNextSeq(), name:name, type:'F', rescs:values, exts:{fldYn:'Y'}}];
		tree.appendNodes(pid, nodes);
	}
}

<%// [아이콘 <, << ] - 메뉴추가 %>
function addMnu(arr){
	var tree = TREE.getTree('combTree');
	var pid = tree.getPidForAppend();
	if(pid==null){
		<%//pt.jsp.setMnu.msg1=메뉴에는 하위 폴더를 등록 할 수 없습니다.%>
		alertMsg("pt.jsp.setMnu.msg1");
	} else if(arr.length==0){
		<%//cm.msg.noSelectedItem=선택된 "{0}"(이)가 없습니다.%>
		alertMsg("cm.msg.noSelectedItem",["#pt.jsp.setIconLout.mnuGrp.title"]);
	} else {
		<%// 해당 UL에 있는 메뉴 아이디 가져오기 - alreadys 
		/*
		var alreadys = [], exts, mnuId;
		$ul.children().each(function(){
			exts = $(this).attr("data-exts");
			mnuId = $.parseJSON(exts).mnuId;
			if(mnuId!=null && mnuId!='') alreadys.push(mnuId);
		});
		*/%>
		<%// nodes에 arr의 내용을 tree에 필요한 형태로 담음 %>
		var nodes=[];
		parseToTreeNodes(pid, arr, nodes, null);
		tree.appendNodes(pid, nodes);
	}
}<%/* addMnu:arr 데이터 형태
[
	{"mnuId":"M000000X","fldYn":"Y","mnuRescId":"R0000072","mnuRescNm":"22",
		"children":[
			{"mnuId":"M0000010","fldYn":"N","mnuRescId":"R0000075","mnuRescNm":"22-1"},
			{"mnuId":"M0000011","fldYn":"N","mnuRescId":"R0000076","mnuRescNm":"22-2"}
		]
	},
	{"mnuId":"M0000012","fldYn":"N","mnuRescId":"R0000077","mnuRescNm":"33-1"}
]
*/%>
<%// nodes에 arr의 내용을 tree에 필요한 형태로 담음 %>
function parseToTreeNodes(pid, arr, nodes, alreadys){
	var node;
	arr.each(function(index, obj){
		if(alreadys==null || !alreadys.contains(obj.mnuId)){
			node = {pid:pid, id:parent.getNextSeq(), name:obj.mnuRescNm, type:obj.fldYn=='Y' ? 'F' : 'A', exts:obj, children:[]};
			nodes.push(node);
			if(obj.children!=null){
				parseToTreeNodes(node.id, obj.children, node.children, alreadys);
				delete obj.children;
			}
		}
	});
}
<%// [저장버튼] - parent 호출 %>
function getCombData(){
	var pObj = {};
	getCombDataUL($("#combTree #ROOTUL"), pObj);
	return pObj.children == null ? [] : pObj.children;
}
<%// UL 테그 하위 LI 테그로 부터 저장 할 데이터 추출 %>
function getCombDataUL($ul, pObj){
	var sortOrdr = 0, exts, rescs;
	if(pObj.title!=null) delete pObj.title;
	$ul.children().each(function(){
		exts = $.parseJSON($(this).attr("data-exts"));
		exts.sortOrdr = (++sortOrdr)+"";
		rescs = $(this).attr("data-rescs");
		if(rescs!=null && rescs!=''){
			exts.rescs = $.parseJSON(rescs);
		}
		if(pObj.children==null){
			pObj.children=[];
		}
		if(exts.title!=null) delete exts.title;
		pObj.children.push(exts);
		getCombDataUL($(this).children('ul'), exts);
	});
}
<%// tree 스크립트로 생성 %>
$(document).ready(function() {
	<u:set test="${empty param.mnuLoutId}" var="mnuLoutId" value="${param.tempId}" elseValue="${param.mnuLoutId}" />
	var tree = TREE.create('combTree');
	tree.onclick = 'onTreeClick';
	tree.setRoot('${mnuLoutId}', '<u:msg titleId="pt.jsp.setMnu.topTreeName"/>');<%//pt.jsp.setMnu.topTreeName=최상위메뉴%>
	tree.setSkin("${_skin}");
	//TREE.selectOption = 2;<%// 1:하나만 선택, 2:여러개 선택%>
	
	<%// tree.add() param : pid, id, name, type, sortOrdr %>
	<c:forEach items="${ptMnuLoutCombDVoList}" var="ptMnuLoutCombDVo" varStatus="status" ><u:set test="${not empty ptMnuLoutCombDVo.rescNm}" var="rescNm" value="${ptMnuLoutCombDVo.rescNm}" elseValue="${ptMnuLoutCombDVo.mnuRescNm}" />
	tree.add("${ptMnuLoutCombDVo.mnuLoutCombPid}","${ptMnuLoutCombDVo.mnuLoutCombId}","<u:out value='${rescNm}' type='script' />","<u:decode srcValue='${ptMnuLoutCombDVo.fldYn}' tgtValue='Y' value='F' elseValue='A' />","${ptMnuLoutCombDVo.sortOrdr}","${ptMnuLoutCombDVo.rescId}",{mnuLoutCombId:"${ptMnuLoutCombDVo.mnuLoutCombId}",title:"${ptMnuLoutCombDVo.mnuId}",fldYn:"${ptMnuLoutCombDVo.fldYn}",fldNm:"${ptMnuLoutCombDVo.fldNm}",rescId:"${ptMnuLoutCombDVo.rescId}",mnuId:"${ptMnuLoutCombDVo.mnuId}",mnuRescId:"${ptMnuLoutCombDVo.mnuRescId}"});
	</c:forEach>
	tree.draw();
});
//-->
</script>

<div id="combTree" class="tree"></div>
