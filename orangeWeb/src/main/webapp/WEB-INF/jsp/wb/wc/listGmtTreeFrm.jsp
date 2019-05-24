<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

<%// 트리 클릭 - 오른쪽의 코드 목록을 오픈, ROOT의 경우 빈화면으로 대치, 하위폴더가 있어도 빈화면으로 대치 %>
function onTreeClick(id, name){
	parent.openCdList(id);
};

<% // TREE %>
$(document).ready(function() {
	var tree = TREE.create('cdTree');
	tree.onclick = 'onTreeClick';
	tree.setRoot('GMT_CD', '세계시간표');
	tree.setSkin("${_skin}");
	//tree.openLvl = 1;
	
	<%// tree.add() param : pid, id, name, type, sortOrdr %>
	<c:forEach items="${ptCdBVoList}" var="list" varStatus="natyStatus">
	tree.add("${list.clsCd}","${list.cd}","<u:out value='${list.rescNm}' type='script' />","F","${list.sortOrdr}",null,{pid:"${list.clsCd}",id:"${list.cd}",fldNm:"${list.rescNm}"});
	</c:forEach>
	tree.draw();
	<u:set test="${empty param.clsCd}" var="selectedNode" value="GMT_CD" elseValue="${param.clsCd}" />
	tree.selectTree("${selectedNode}");
	//onTreeClick("${selectedNode}");
});

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<div id="cdTree" class="tree"></div>
