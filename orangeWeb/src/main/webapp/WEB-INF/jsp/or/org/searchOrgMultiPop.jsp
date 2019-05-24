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
<div id="orgPopArea" style="width:720px;">

<div style="float:left; width:47.5%;">
<u:titleArea
	outerStyle="height:356px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
<iframe id="orgListForSelectFrm" name="orgListForSelectFrm" src="/or/org/selectOrgTreeFrm.do${urlParam}" style="width:350px; height:350px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>

<div style="float:left; width:5%; text-align:center; margin:170px 0 0 0;">
	<table style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
		<tr><td><a href="javascript:addOrgs();"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:removeOrgs();"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td></tr>
	</table>
</div>

<div style="float:right; width:47.5%;">
<u:titleArea
	outerStyle="height:356px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV">
<iframe id="listSeltdOrgFrm" name="listSeltdOrgFrm" src="/cm/util/reloadable.do" style="width:99.8%; height:350px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>
</div>
<u:blank />
<u:buttonArea noBottomBlank="">
	<u:button titleId="cm.btn.confirm" href="javascript:setSelectedOrgArr();" alt="확인" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>