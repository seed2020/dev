<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--<%// [버튼] 즐겨찾기관리 %>
function setBumkPop(){
	var url = './setBumkPop.do?menuId=${menuId}&popYn=Y';
	dialog.open('setBumkPop', '<u:msg titleId="dm.btn.bumk.mng" alt="즐겨찾기관리" />', url);
}<%// [리로드] - 프레임 %>
function reloadFrm(frmId, url, popCloseId){
	reloadFrame(frmId, url);
	if(popCloseId != undefined) dialog.close(popCloseId);
}<%// [즐겨찾기클릭] - 오른쪽 리스트 열기 %>
function openListFrm(id){
	if(id!=null){
		gFldId = id;
	}
	reloadFrame('openListFrm', '/dm/doc/listDocFrm.do?menuId=${menuId}&bumkId='+id);
};

//-->
</script>

<u:title titleId="dm.jsp.bumk.title" alt="즐겨찾기" menuNameFirst="true" />

<!-- LEFT -->
<div style="float:left; width:27.8%;">

<u:titleArea frameId="bumkFrm" frameSrc="./listBumkFrm.do?menuId=${menuId}"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:setBumkPop();" titleId="dm.btn.bumk.mng" alt="즐겨찾기관리" auth="W" popYn="Y" />
</u:buttonArea>
</div>

	
<!-- RIGHT -->
<div style="float:right; width:71%;">

<u:titleArea frameId="openListFrm" frameSrc="/cm/util/reloadable.do"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	<u:buttonArea id="rightBtnArea" noBottomBlank="true">
	<c:if test="${!empty authMap.setDoc }"><u:button id="btnSetDoc" titleId="cm.btn.reg" alt="등록" href="javascript:setDoc();" auth="W" /></c:if>
	</u:buttonArea>
	
</div>

<u:blank />

