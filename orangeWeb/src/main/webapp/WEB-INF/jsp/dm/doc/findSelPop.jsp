<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%
	String callback = (String)request.getAttribute("callback");
	if(callback==null || callback.isEmpty()) callback = "setSelInfos";
%><u:set var="treePage" test="${param.lstTyp eq 'C'}" value="treeDocClsFrm" elseValue="treeDocFldFrm"/>
<u:set var="treePageParam" test="${param.lstTyp eq 'C'}" value="&initSelect=Y" elseValue="&fldId=${param.selIds }&fldTyp=${param.fldTyp }&initFrm=N"/>
<u:set var="paramStorIdQueryString" test="${!empty param.paramStorId }" value="&paramStorId=${param.paramStorId }" elseValue=""/><!-- 저장소ID -->
<script type="text/javascript">
<!--<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function findSelListFrm(){};
<%// [아이콘 > ] 추가%>
function addRows(){
	var tree = getIframeContent('docTree').getTreeData();
	if(tree.id != 'ROOT')	{
		var selObj = {id:tree.clsId,nm:tree.clsNm};
		getIframeContent('findSelFrm').addRows(selObj);
	}
};<%// [아이콘 < ] 삭제 %>
function delSelRows(){
	getIframeContent('findSelFrm').delSelRows();
}<%// 확인 버튼 클릭[한개] %>
function applySelect(){
	var arr = getIframeContent('docTree').getTreeSelect('${param.mode}');
	if(arr == null) {
		var msgSuffix = '${param.lstTyp}' == 'C' ? 'cls' : 'fldSub';
		alertMsg("dm.jsp.setDoc.not."+msgSuffix);<% // dm.jsp.setDoc.not.='분류' or '폴더'를 선택 후 사용해 주십시요. %>
	}
	if(arr != null) <%= callback%>(arr, '${param.lstTyp}');
	
}<%// 확인 버튼 클릭[여러개] %>
function applySelects(){
	var arr = getIframeContent('findSelFrm').getSelInfos();
	if(arr == null) {
		var msgSuffix = '${param.lstTyp}' == 'C' ? 'cls' : 'fldSub';
		alertMsg("dm.jsp.setDoc.not."+msgSuffix);<% // dm.jsp.setDoc.not.='분류' or '폴더'를 선택 후 사용해 주십시요. %>
	}
	if(arr != null) <%= callback%>(arr, '${param.lstTyp}');
}
$(document).ready(function() {
	<c:if test="${param.fncMul eq 'Y' }">
		reloadFrame('findSelFrm', '/cm/doc/findSelFrm.do?menuId=${menuId}&lstTyp=${param.lstTyp}&selIds=${param.selIds}${paramStorIdQueryString}');
	</c:if>
});
//-->
</script>
<div style="width:500px;">
<div style="height:410px;">
<u:set var="style" test="${param.fncMul eq 'Y' }" value="style='float:left;width:46%;'" elseValue=""/>

<!-- LEFT -->
<div ${style } id="leftContainer">
<u:set var="treeUrl" test="${isCm == true }" value="/cm/doc/" elseValue="./"/>
<u:titleArea frameId="docTree" frameSrc="${treeUrl}${treePage }.do?menuId=${menuId}${treePageParam }&callback=findSelListFrm&fldGrpId=${param.fldGrpId }${paramStorIdQueryString}"
	outerStyle="height:380px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:370px;" noBottomBlank="true" />
	
</div>
<c:if test="${param.fncMul eq 'Y' }">
<div style="float:left; width:9%; text-align:center; margin:175px 0 0 0;">
	<table style="margin:0 auto 0 auto;" border="0" cellpadding="0" cellspacing="0">
		<tr><td><a href="javascript:addRows();"<u:elemTitle titleId="cm.btn.selAdd" alt="선택추가" type="image" />><img src="${_cxPth}/images/${_skin}/ico_right.png" width="20" height="20" /></a></td></tr>
		<tr><td class="height5"></td></tr>
		<tr><td><a href="javascript:delSelRows();"<u:elemTitle titleId="cm.btn.selDel" alt="선택삭제" type="image" />><img src="${_cxPth}/images/${_skin}/ico_left.png" width="20" height="20" /></a></td></tr>
	</table>
</div>

<!-- RIGHT -->
<div style="float:right; width:45%;" id="rContainer">

<u:titleArea frameId="findSelFrm" frameSrc="/cm/util/reloadable.do"
	outerStyle="height:380px; overflow-x:hidden; overflow-y:auto;"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:370px;" noBottomBlank="true"  />
</div>
</c:if>
</div>
	
<u:buttonArea id="rightBtnArea">
	<c:if test="${param.fncMul eq 'Y'}"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applySelects();" /></c:if>
	<c:if test="${empty param.fncMul || param.fncMul eq 'N'}"><u:button titleId="cm.btn.confirm" alt="확인" href="javascript:applySelect();" /></c:if>
	<u:button id="btnCancel" titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</div>