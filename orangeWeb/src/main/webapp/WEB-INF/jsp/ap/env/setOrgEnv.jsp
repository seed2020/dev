<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	// 디폴트로 부서정보[탭]이 선택되게, 파라미터(tab)으로 넘어온 값이 있으면 해당값의 탭 오픈함
	// tab : Info, Img, Cls
	String tab = request.getParameter("tab");
	if(tab==null || !tab.equals("Img") || !tab.equals("Cls")) tab = "Info";
	request.setAttribute("tab", tab);
	
%>
<script type="text/javascript">
<!--<%
// 탭 변경 - 해당 프레임 리로드, 해당 버튼만 보이게 %>
function setDetl(frmNm, extParam){
	var url = (frmNm=='Cls' ? './treeOrgCls' : './setOrg'+frmNm)+"Frm.do?menuId=${menuId}${orgParam}"+(extParam==null ? "" : extParam);
	$("#dept"+frmNm+"Frame").attr('src', url);
	var $area = $("#rightBtnArea");
	if(frmNm=='Info'){
		displayBtn($area, true,  ["deptInfoBtn"]);
		displayBtn($area, false, ["addImgBtn","addAllClsBtn","addClsBtn","modClsBtn","delClsBtn","moveClsBtn","moveClsUpBtn","moveClsDownBtn"]);
	} else if(frmNm=='Img'){
		displayBtn($area, true,  ["addImgBtn"]);
		displayBtn($area, false, ["deptInfoBtn","addAllClsBtn","addClsBtn","modClsBtn","delClsBtn","moveClsBtn","moveClsUpBtn","moveClsDownBtn"]);
	} else if(frmNm=='Cls'){
		displayBtn($area, true,  ["addAllClsBtn","addClsBtn","modClsBtn","delClsBtn","moveClsBtn","moveClsUpBtn","moveClsDownBtn"]);
		displayBtn($area, false, ["addImgBtn","deptInfoBtn"]);
	}
}<%
// 버튼 보이기 조절 %>
function displayBtn($area, showFlag, arr){
	arr.each(function(index, obj){
		if(showFlag){
			$area.find("#"+obj).show();
		} else {
			$area.find("#"+obj).hide();
		}
	});
}<%
// [버튼] 저장 - 부서정보[탭] %>
function saveOrgInfo(){
	getIframeContent("deptInfoFrame").saveOrgInfo();
}<%
// [버튼] 추가 - 부서이미지[탭] %>
function addOrgImg(){
	getIframeContent("deptImgFrame").addOrgImg();
}<%
// [버튼] 일괄 분류 등록, 분류 추가, 분류 수정, 분류 삭제 - 분류 정보[탭] %>
function clickManageCls(mode){
	getIframeContent("deptClsFrame").manageCls(mode);
}<%
// [버튼] 위로이동, 아래로이동 - 분류 정보[탭] %>
function moveCls(direction){
	getIframeContent("deptClsFrame").move(direction);
}<%
// [버튼] 분류이동 - callback %>
function callbackMoveCls(){
	var from = getIframeContent("deptClsFrame").gClsId;
	var to = gClsId;
	if(to==null){
		alertMsg('ap.msg.chooseCls');<%//ap.msg.chooseCls=분류정보를 선택해 주십시요.%>
		return;
	}
	if(from==to){
		alertMsg('ap.msg.chooseDifCls');<%//ap.msg.chooseDifCls=동일한 분류정보로 이동 할 수 없습니다.%>
		return;
	}
	var result = false;
	callAjax("./transOrgClsCutPasteAjx.do?menuId=${menuId}&bxId=${param.bxId}", {from:from, to:to}, function(data){
		if(data.message != null) alert(data.message);
		result = data.result == 'ok';
	});
	if(result){
		reloadTree(from, 'org');
		dialog.close('setMoveOrgClsDialog');
	}
}<%
// [버튼] 분류이동 - callback - 저장뒤 호출 %>
function reloadTree(clsInfoId, type){
	getIframeContent('deptClsFrame').reload('./treeOrgClsFrm.do?menuId=${menuId}'+(clsInfoId=='ROOT' ? '' : '&clsInfoId='+clsInfoId));
	dialog.close('setMoveOrgClsDialog');
}
$(document).ready(function() {
	setUniformCSS();
	setDetl('${tab}');
});
//-->
</script>

<u:title title="결재정보설정" menuNameFirst="true" />

<u:tabGroup id="apvOrgEnvTab" noBottomBlank="true">
	<u:tab id="apvOrgEnvTab" areaId="deptInfoTabArea" onclick="setDetl('Info');" titleId="ap.jsp.setOrgEnv.tab.deptInfo" alt="부서 정보" on="${tab == 'Info'}" />
	<u:tab id="apvOrgEnvTab" areaId="deptImgTabArea" onclick="setDetl('Img');" titleId="ap.jsp.setOrgEnv.tab.deptImg" alt="관인/서명인 관리" on="${tab == 'Img'}" />
	<c:if test="${optConfigMap.catEnab == 'Y'}">
	<u:tab id="apvOrgEnvTab" areaId="deptClsTabArea" onclick="setDetl('Cls');" titleId="ap.jsp.setOrgEnv.tab.deptCls" alt="분류 정보" on="${tab == 'Cls'}" />
	</c:if>
</u:tabGroup>

<u:tabArea outerStyle="height:410px; overflow-x:hidden; overflow-y:auto;" innerStyle="NO_INNER_IDV" >

<div id="deptInfoTabArea"<c:if test="${tab != 'Info'}"> style="display: none;"</c:if>>
<iframe id="deptInfoFrame" name="deptInfoFrame" src="${_cxPth}/cm/util/reloadable.do" style="width:100%; height:402px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
<div id="deptImgTabArea"<c:if test="${tab != 'Img'}"> style="display: none;"</c:if>>
<iframe id="deptImgFrame" name="deptImgFrame" src="${_cxPth}/cm/util/reloadable.do" style="width:100%; height:402px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>
<div id="deptClsTabArea"<c:if test="${tab != 'Cls'}"> style="display: none;"</c:if>>
<iframe id="deptClsFrame" name="deptClsFrame" src="${_cxPth}/cm/util/reloadable.do" style="width:100%; height:402px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>

</u:tabArea>

<% // 부서정보 %>
<div id="deptInfoTabArea" style="display: none;">
</div>

<% // 부서이미지 %>
<div id="deptImgTabArea" style="display: none;">
</div>

<% // 분류정보 %>
<c:if test="${optConfigMap.catEnab == 'Y'}">
<div id="deptClsTabArea" style="display: none;">
</div>
</c:if>

<u:buttonArea id="rightBtnArea">
	<u:button titleId="cm.btn.save" alt="저장" id="deptInfoBtn" href="javascript:saveOrgInfo();" auth="A" />
	<c:if test="${not empty adminPage or optConfigMap.alwChgOfcSeal == 'Y'}">
	<u:button titleId="cm.btn.add" alt="추가" id="addImgBtn" href="javascript:addOrgImg();" auth="A" style="display:none;" />
	</c:if>
	<c:if test="${optConfigMap.catEnab == 'Y'}">
	<c:if test="${not empty adminPage}">
	<u:button titleId="ap.btn.addAllCls" alt="일괄 분류 등록" id="addAllClsBtn" href="javascript:clickManageCls('addAllCls');" auth="A" style="display:none;" />
	</c:if>
	<u:button titleId="ap.btn.addCls" alt="분류 추가" id="addClsBtn" href="javascript:clickManageCls('addCls');" auth="A" style="display:none;" />
	<u:button titleId="ap.btn.modCls" alt="분류 수정" id="modClsBtn" href="javascript:clickManageCls('modCls');" auth="A" style="display:none;" />
	<u:button titleId="ap.btn.delCls" alt="분류 삭제" id="delClsBtn" href="javascript:clickManageCls('delCls');" auth="A" style="display:none;" />
	<u:button titleId="ap.btn.moveCls" alt="분류 이동" id="moveClsBtn" href="javascript:clickManageCls('moveCls');" auth="A" style="display:none;" />
	<u:button titleId="cm.btn.up" alt="위로이동" id="moveClsUpBtn" href="javascript:moveCls('up');" auth="A" style="display:none;" />
	<u:button titleId="cm.btn.down" alt="아래로이동" id="moveClsDownBtn" href="javascript:moveCls('down');" auth="A" style="display:none;" />
	</c:if>
</u:buttonArea>
