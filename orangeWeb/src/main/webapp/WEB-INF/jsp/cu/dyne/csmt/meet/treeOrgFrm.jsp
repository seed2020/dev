<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set var="callback" test="${!empty param.callback }" value="${param.callback }" elseValue="openListFrm"/>
<u:set var="fldGrpIdQueryString" test="${!empty param.fldGrpId }" value="&fldGrpId=${param.fldGrpId }" elseValue=""/>
<script type="text/javascript">
<!--<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name, rescId){
	if(id==null || id=='') return;
	<c:if test="${empty param.initFrm || param.initFrm == 'Y'}">parent.${callback}(id);</c:if>	
}<%// html reload %>
function reoladFrame(orgId){
	var href = './treeDocFldFrm.do?menuId=${menuId}${fldGrpIdQueryString}';
	if(orgId!=null && orgId!='') href = href + "&orgId="+orgId;
	location.replace(href);
}<%// 트리의 확장 데이터 리턴 %>
function getTreeData(){
	var sel = TREE.getTree('orgTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return {id:'ROOT'};
	} else {
		var exts = TREE.getExtData('orgTree','exts');
		if(exts == null || exts.fldGrpId == 'COMP' || exts.fldGrpId == 'DEPT' || exts.fldGrpId == 'NONE') return null;
		return exts;
	}
}<%// 트리의 선택 데이터 리턴 %>
function getTreeSelect(mode){
	var sel = TREE.getTree('orgTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('orgTree','exts');
		if(exts == null || ( (mode == undefined || mode == '') && (exts.fldGrpId == 'COMP' || exts.fldGrpId == 'DEPT' || exts.fldGrpId == 'NONE'))) return null;
		return [{id:exts.orgId,nm:exts.rescNm,fldGrpId:exts.fldGrpId,fldTypCd:exts.fldTypCd}];
	}
}<%// 트리의 선택 ID 리턴 %>
function getSelectId(){
	var sel = TREE.getTree('orgTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('orgTree','exts');
		if(exts == null || exts.fldGrpId == 'COMP' || exts.fldGrpId == 'DEPT' || exts.fldGrpId == 'NONE') return null;
		return exts.orgId;
	}
}<%// 트리의 선택 ID 리턴 %>
function getSelectAllId(){
	var sel = TREE.getTree('orgTree').selected;
	if($(sel).attr('id')=='ROOTLI'){
		return null;
	} else {
		var exts = TREE.getExtData('orgTree','exts');
		if(exts == null) return null;
		return exts.orgId;
	}
}<%// tree 스크립트로 생성 %>
$(document).ready(function() {
	
	var tree = TREE.create('orgTree');
	tree.onclick = 'onTreeClick';
	//tree.setIconTitle('${iconTitle}');
	tree.setRoot('ROOT', '<u:msg titleId="cols.org" alt="조직"/>');
	tree.setSkin("${_skin}");
	<c:if test="${not empty param.openLvl}">tree.openLvl = ${param.openLvl};</c:if><%// 특정폴더 하위나 상위폴더 조회시 모든 트리를 펼쳐 보이게함 %>
	<c:if test="${not empty param.treeSelectOption}">TREE.selectOption = '${param.treeSelectOption}';</c:if><%// 여러개 선택 가능 하도록 %>
	
	<%// tree.add() param : orgPid, id, name, type, sortOrdr %>
	<c:forEach items="${orgList}" var="orOrgBVo" varStatus="status" >
	tree.add("${orOrgBVo.orgPid}","${orOrgBVo.orgId}","<u:out value='${orOrgBVo.rescNm}' type='script' />","${orOrgBVo.orgTypCd}","${orOrgBVo.sortOrdr}","${orOrgBVo.rescId}",{orgId:"${orOrgBVo.orgId}",rescNm:"<u:out value='${orOrgBVo.rescNm}' type='script' />",orgPid:"${orOrgBVo.orgPid}",rescId:"${orOrgBVo.rescId}",useYn:"${orOrgBVo.useYn}",title:"<u:out value='${orOrgBVo.rescNm}' type='script' />"});
	</c:forEach>
	tree.draw();<c:if test="${empty noInitSelect}"><c:if
		test="${not empty param.orgId}"><%// 파라미터 orgId 가 선택 되도록함 %>
	tree.selectTree("${param.orgId}");
	onTreeClick("${param.orgId}");</c:if><c:if
		test="${empty param.orgId}"><%// 사용자가 속한 부서가 선택 되도록 함 %>
	tree.selectTree("${sessionScope.userVo.orgId}");
	onTreeClick("${sessionScope.userVo.orgId}");</c:if>
	</c:if>
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
