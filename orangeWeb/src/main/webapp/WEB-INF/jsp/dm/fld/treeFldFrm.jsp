<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="openListFrm"/>
<script type="text/javascript">
<!--<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name, rescId){
	if(id==null || id=='') return;
	<c:if test="${empty param.initFrm || param.initFrm == 'Y'}">parent.${callback}(id);</c:if>	
}<%// html reload %>
function reoladFrame(fldId){
	var href = './treeFldFrm.do?menuId=${menuId}';
	if(fldId!=null && fldId!='') href = href + "&fldId="+fldId;
	location.replace(href);
}<%// 트리의 확장 데이터 리턴 %>
function getTreeData(){
	var sel = TREE.getTree('fldTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return {id:'ROOT'};
	} else {
		var exts = TREE.getExtData('fldTree','exts');
		return exts;
	}
}<%// 트리의 선택 데이터 리턴 %>
function getTreeSelect(mode){
	var sel = TREE.getTree('fldTree').selected;
	if($(sel).attr('id')=='ROOTLI' && (mode==undefined || mode=='')){
		return null;
	} else {
		if($(sel).attr('id')=='ROOTLI') return [{id:'ROOT',nm:'<u:msg titleId="cols.fld" alt="폴더"/>'}];
		var exts = TREE.getExtData('fldTree','exts');
		return [{id:exts.fldId,nm:exts.fldNm}];
	}
}<%// 트리의 선택 ID 리턴 %>
function getSelectId(){
	var sel = TREE.getTree('fldTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('fldTree','exts');
		return exts.fldId;
	}
}<%// tree 스크립트로 생성 %>
$(document).ready(function() {
	
	var tree = TREE.create('fldTree');
	tree.onclick = 'onTreeClick';
	//tree.setIconTitle('${iconTitle}');
	tree.setRoot('ROOT', '<u:msg titleId="cols.fld" alt="폴더"/>');
	tree.setSkin("${_skin}");
	<c:if test="${not empty param.openLvl}">tree.openLvl = ${param.openLvl};</c:if><%// 특정폴더 하위나 상위폴더 조회시 모든 트리를 펼쳐 보이게함 %>
	<c:if test="${not empty param.treeSelectOption}">TREE.selectOption = '${param.treeSelectOption}';</c:if><%// 여러개 선택 가능 하도록 %>
	
	<%// tree.add() param : fldPid, id, name, type, sortOrdr %>
	<c:forEach items="${dmFldBVoList}" var="dmFldBVo" varStatus="status" >
	tree.add("${dmFldBVo.fldPid}","${dmFldBVo.fldId}","<u:out value='${dmFldBVo.fldNm}' type='script' />","F","0${dmFldBVo.sortOrdr}","${dmFldBVo.rescId}",{fldId:"${dmFldBVo.fldId}",fldNm:"<u:out value='${dmFldBVo.fldNm}' type='script' />",fldPid:"${dmFldBVo.fldPid}",storId:"${dmFldBVo.storId}",rescId:"${dmFldBVo.rescId}",useYn:"${dmFldBVo.useYn}",title:"<u:out value='${dmFldBVo.fldNm}' type='script' />"});
	</c:forEach>
	tree.draw();<c:if test="${empty noInitSelect}"><c:if
		test="${not empty param.fldId}"><%// 파라미터 fldId 가 선택 되도록함 %>
	tree.selectTree("${param.fldId}");
	onTreeClick("${param.fldId}");</c:if><c:if
		test="${empty param.fldId}"><%// 사용자가 속한 부서가 선택 되도록 함 %>
	tree.selectTree("ROOT");
	onTreeClick("ROOT");</c:if>
	</c:if>
});
//-->
</script>
<div id="fldTree" class="tree"></div>
