<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="openListFrm"/>
<script type="text/javascript">
<!--<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name, rescId){
	if(id==null || id=='') return;
	parent.${callback}(id, name, rescId);
}<%// html reload %>
function reoladFrame(clsId){
	var href = './treeClsFrm.do?menuId=${menuId}';
	if(clsId!=null && clsId!='') href = href + "&clsId="+clsId;
	location.replace(href);
}<%// 트리의 확장 데이터 리턴 %>
function getTreeData(){
	var sel = TREE.getTree('clsTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return {id:'ROOT'};
	} else {
		var exts = TREE.getExtData('clsTree','exts');
		return exts;
	}
}<%// 트리의 선택 데이터 리턴 %>
function getTreeSelect(){
	var sel = TREE.getTree('clsTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('clsTree','exts');
		if(exts == null ) return null;
		return [{id:exts.clsId,nm:exts.clsNm}];
	}
}<%// tree 스크립트로 생성 %>
$(document).ready(function() {
	
	var tree = TREE.create('clsTree');
	tree.onclick = 'onTreeClick';
	//tree.setIconTitle('${iconTitle}');
	tree.setRoot('ROOT', '<u:msg titleId="cols.cls" alt="분류"/>');
	tree.setSkin("${_skin}");
	<c:if test="${not empty openLvl}">tree.openLvl = ${openLvl};</c:if><%// 특정폴더 하위나 상위폴더 조회시 모든 트리를 펼쳐 보이게함 %>
	<c:if test="${not empty treeSelectOption}">TREE.selectOption = ${treeSelectOption};</c:if><%// 여러개 선택 가능 하도록 %>
	
	<%// tree.add() param : clsPid, id, name, type, sortOrdr %>
	<c:forEach items="${dmClsBVoList}" var="dmClsBVo" varStatus="status" >
	tree.add("${dmClsBVo.clsPid}","${dmClsBVo.clsId}","<u:out value='${dmClsBVo.clsNm}' type='script' />","F","${dmClsBVo.sortOrdr}","${dmClsBVo.rescId}",{clsId:"${dmClsBVo.clsId}",clsNm:"${dmClsBVo.clsNm}",clsPid:"${dmClsBVo.clsPid}",storId:"${dmClsBVo.storId}",rescId:"${dmClsBVo.rescId}",useYn:"${dmClsBVo.useYn}",title:"<u:out value='${dmClsBVo.clsNm}' type='script' />"});
	</c:forEach>
	tree.draw();<c:if test="${empty noInitSelect}"><c:if
		test="${not empty param.clsId}"><%// 파라미터 clsId 가 선택 되도록함 %>
	tree.selectTree("${param.clsId}");
	onTreeClick("${param.clsId}");</c:if><c:if
		test="${empty param.clsId && not empty clsId}"><%// 파라미터 clsId 가 선택 되도록함 %>
	tree.selectTree("${clsId}");
	onTreeClick("${clsId}");</c:if><c:if
		test="${empty param.clsId && empty clsId}"><%// 파라미터 clsId 가 선택 되도록함 %>
	tree.selectTree("ROOT");
	onTreeClick("ROOT");</c:if>
	</c:if>
});
//-->
</script>
<div id="clsTree" class="tree"></div>
