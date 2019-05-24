<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String callback = (String)request.getAttribute("callback");
	if(callback==null || callback.isEmpty()) callback = "setFldInfos";
%>
<script type="text/javascript">
<!--<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function findSelListFrm(){};
<%// [아이콘 > ] 추가%>
function addRows(){
	var tree = getIframeContent('docTree').getTreeData();
	if(tree.id != 'ROOT')	{
		var selObj = {id:tree.clsId,nm:tree.clsNm};
		getIframeContent('openListFrm').addRows(selObj);
	}
};<%// [아이콘 < ] 삭제 %>
function delSelRows(){
	getIframeContent('openListFrm').delSelRows();
}<%// 확인 버튼 클릭[한개] %>
function applySelect(){
	var arr = getIframeContent('docTree').getTreeSelect('${param.mode}');
	if(arr != null) <%= callback%>(arr, '${param.lstTyp}');
	
}<%// 확인 버튼 클릭[여러개] %>
function applySelects(){
	var arr = getIframeContent('openListFrm').getSelInfos();
	if(arr != null) <%= callback%>(arr, '${param.lstTyp}');
}
$(document).ready(function() {
	<c:if test="${param.fncMul eq 'Y' }">
		reloadFrame('openListFrm', './findSelFrm.do?menuId=${menuId}&lstTyp=${param.lstTyp}&selIds=${param.selIds}');
	</c:if>
});
//-->
</script>
<div style="width:500px;">
<div style="height:410px;">
<u:set var="style" test="${param.fncMul eq 'Y' }" value="style='float:left;width:46%;'" elseValue=""/>

<!-- LEFT -->
<div ${style } id="leftContainer">

<u:titleArea frameId="docTree" frameSrc="./treeFldFrm.do?menuId=${menuId}&fldId=${param.fldId }&initFrm=N&callback=findSelListFrm"
	outerStyle="height:380px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:370px;" noBottomBlank="true" />
	
</div>
</div>
	
<u:buttonArea id="rightBtnArea">
	<c:if test="${param.fncMul eq 'Y'}"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applySelects();" auth="W" /></c:if>
	<c:if test="${empty param.fncMul || param.fncMul eq 'N'}"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applySelect();" auth="W" /></c:if>
	<u:button id="btnCancel" titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>