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
	gOrgSelectedObj = {orgId:orgId, orgNm:orgNm, rescId:rescId};
	var $withSub = $("#orgPopArea #withSubCheck");
	if($withSub.length>0){
		gOrgSelectedObj['withSub'] = $withSub[0].checked ? "Y" : "N";
	}
}
<%// 하위부서 포함 클릭시 실행%>
function setWithSub(checked){
	if(gOrgSelectedObj!=null){
		gOrgSelectedObj['withSub'] = checked ? "Y" : "N";
	}
}
<%// 확인 버튼 %>
function setSelectedOrgVo(){
	var arr = getIframeContent('orgListForSelectFrm').getSelectedNodes(true);
	var result = null;
	if(gOrgHandler!=null){
		var obj = null;
		if(arr!=null && arr.length>0){
			obj = arr[0];
			if(obj.orgId=='ROOT'){
				alertMsg("or.msg.not.select.top");<%//or.msg.not.select.top=조직도의 최상위는 선택 할 수 없습니다.%>
				return;
			}
			var $sub = $("#orgPopArea #withSubCheck");
			if($sub.length>0){
				obj['withSub'] = $sub[0].checked ? "Y" : "N";
			}
		}
		result = gOrgHandler(obj);
	}
	if(result!=false){
		gOrgHandler = null;
		gOrgSelectedObj = null;
		dialog.close('searchOrgDialog');
	}
}<c:if test="${'Y' == param.withSub }">
$(document).ready(function() {
	if(gOrgSelectedObj!=null && gOrgSelectedObj.withSub!=null && gOrgSelectedObj.withSub=='Y'){
		$("#orgPopArea #withSubCheck").trigger('click');
	}
});
</c:if>
//-->
</script>
<div id="orgPopArea" style="width:370px;">

<u:titleArea outerStyle="width:width:100%;" innerStyle="NO_INNER_IDV">
<iframe id="orgListForSelectFrm" name="orgListForSelectFrm" src="/or/org/selectOrgTreeFrm.do${urlParam}" style="width:350px; height:350px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</u:titleArea>

<c:if test="${'Y' == param.withSub }">
<form>
<u:checkArea style="position:relative; float:left">
<u:checkbox value="Y" name="withSubCheck" id="withSubCheck" titleId="or.check.withSub" onclick="setWithSub(this.checked);" />
</u:checkArea>
</form>
</c:if>
<u:blank />
<u:buttonArea noBottomBlank="">
	<u:button titleId="cm.btn.confirm" href="javascript:setSelectedOrgVo();" alt="확인" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>