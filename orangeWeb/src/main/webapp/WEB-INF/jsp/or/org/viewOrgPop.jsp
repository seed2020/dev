<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
/**

treeOrgFrm.jsp >> /pt/adm/org/treeOrgFrm.jsp

*/

%>
<script type="text/javascript">
<!--
<%// 트리의 노드 클릭시 실행 %>
function setOrgVo(orgId, orgNm, rescId){
}
<%// [아이콘 >] 부서 추가 %>
function addOrgs(){
	var arr = getIframeContent('orgListForSelectFrm').getSelectedNodes(true);
	if(arr[0].orgId=='ROOT'){
		arr = arr.slice(1);
	}
	getIframeContent('listSeltdOrgFrm').addOrgs(arr);
}
<%// [아이콘 <] 삭제 추가 %>
function removeOrgs(){
	getIframeContent('listSeltdOrgFrm').removeOrgs();
}
<%// 확인 버튼 %>
function setSelectedOrgArr(){
	var arr = getIframeContent('listSeltdOrgFrm').getOrgSelectedArr();
	var result = null;
	if(gOrgHandler!=null){
		result = gOrgHandler((arr!=null && arr.length==0) ? null : arr);
	}
	if(result!=false){
		gOrgHandler = null;
		gOrgSelectedObj = null;
		dialog.close('searchOrgDialog');
	}
}
<%// tree 에서 onload 시 호출됨 - 오른쪽 프레임에 선택된 목록 보이기 위한것 %>
function processSelect(tree){
	if(gOrgSelectedObj!=null && gOrgSelectedObj.length>0){
		var node, arr = [];
		gOrgSelectedObj.each(function(index, obj){
			node = tree.nodes[obj.orgId];
			if(node!=null){
				arr.push({orgId:obj.orgId,withSub:obj.withSub,rescId:node.rescId,rescNm:node.name});
			}
		});
		gOrgSelectedObj = arr;
	}
	reloadFrame('listSeltdOrgFrm', "/or/org/listSeltdOrgFrm.do${not empty param.withSub ? '?withSub=Y' : ''}");
}
//-->
</script>
<div id="orgPopArea" style="width:400px;">

<div style="float:left; width:0px; height:0px;">
<iframe id="orgListForSelectFrm" name="orgListForSelectFrm" src="/or/org/selectOrgTreeFrm.do?multi=${param.multi}" style="position:absolute; left:-10px; top:-10px; width:0px; height:0px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>

<u:titleArea
	outerStyle="height:356px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
<iframe id="listSeltdOrgFrm" name="listSeltdOrgFrm" src="/cm/util/reloadable.do" style="width:99.8%; height:350px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>

<u:buttonArea noBottomBlank="">
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>