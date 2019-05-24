<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<u:params var="nonParams" excludes="bcId,schBcRegrUid,pageNo"/>
<u:params var="nonPageParams" excludes="schBcRegrUid,pageNo"/>
<script type="text/javascript">
<!--
<%// 선택된 폴더 %>
var gFldId=null;
<%// [버튼] 폴더등록 %>
function regFld(){
	var tree = getIframeContent('fldTree').getTreeData('reg');
	if(tree!=null){<%//pt.jsp.setCd.regFld=폴더등록%>
		dialog.open('setFldDialog','<u:msg titleId="pt.jsp.setCd.regFld"/>','./setFldPop.do?menuId=${menuId}&mode=reg${agntParam}&bcFldId='+tree.id);
	}
};

<%// [버튼] 폴더수정 %>
function modFld(){<%//pt.jsp.setCd.modFld=폴더수정%>
	var tree = getIframeContent('fldTree').getTreeData('mod');
	if(tree.id != 'ROOT'){
		dialog.open('setFldDialog','<u:msg titleId="pt.jsp.setCd.modFld"/>','./setFldPop.do?menuId=${menuId}&mode=mod${agntParam}&bcFldId='+tree.id);
	}
};

// 폴더 복사
function copyFld() {
	var tree = getIframeContent('fldTree').getTreeData('copy');
	if(tree.id != 'ROOT') selArrs= {mode:'copy',bcFldId : tree.id};
	else return;
	dialog.open('findBcFldPop','<u:msg titleId="wb.btn.fldChoi" alt="폴더 선택" />','./findBcFldPop.do?menuId=${menuId}${agntParam}&callback=transSelectFld');
};

// 폴더 이동
function moveFld() {
	var tree = getIframeContent('fldTree').getTreeData('move');
	if(tree.id != 'ROOT') selArrs= {mode:'move',bcFldId : tree.id};
	else return;
	dialog.open('findBcFldPop','<u:msg titleId="wb.btn.fldChoi" alt="폴더 선택" />','./findBcFldPop.do?menuId=${menuId}${agntParam}&callback=transSelectFld');
};

<%// [버튼] 폴더삭제 %>
function delFld(){
	getIframeContent('fldTree').delFld();
};

var selArrs;
//폴더선택
function transSelectFld(){
	var tree = getIframeContent('findBcFldFrm').getTreeData();
	if(tree != null && selArrs != null){
		if(tree.id == selArrs.bcFldId){
			alertMsg("wb.msg.copy.noDupFldNm");
			return;
		}
		if(selArrs.mode == 'copy' || selArrs.mode == 'move'){
			if(confirmMsg("wb.cfrm."+selArrs.mode+".fld")) {<%//"wb.cfrm."+selArrs.mode+".fld"=복사하시겠습니까 ?%>
				callAjax('./transBcFldCopyAjx.do?menuId=${menuId}${agntParam}', {mode:selArrs.mode, abvFldId:tree.id, bcFldId:selArrs.bcFldId}, function(data){
					if(data.message!=null){
						alert(data.message);
					}
					if(data.result=='ok'){
						getIframeContent('fldTree').reload('./${listPage }FldFrm.do?menuId=${menuId}${agntParam }&bcFldId='+data.bcFldId);
					}
				});
			}
		}else if(selArrs.mode == 'copyBc' || selArrs.mode == 'moveBc'){
			if(confirmMsg("wb.cfrm."+selArrs.mode)) {<%//"wb.cfrm."+selArrs.mode+".fld"=삭제하시겠습니까 ?%>
				callAjax('./transBcCopyAjx.do?menuId=${menuId}${agntParam}', {mode:selArrs.mode, bcFldId:tree.id , toBcIds:selArrs.toBcIds}, function(data){
					if(data.message!=null){
						alert(data.message);
					}
					if(data.result=='ok'){
						getIframeContent('openListFrm').reload('./${listPage }Frm.do?${nonParams}${agntParam }&typ=L&schFldTypYn=F&listType=list&schFldId='+tree.id);
						getIframeContent('fldTree').onSelectFld(tree.id);
					}
				});
			}
		}
	}
	
	dialog.close('findBcFldPop');
};

//폴더선택
function fnFldSelect(){
	var tree = getIframeContent('findBcFldFrm').getTreeData();
	if(tree.id == 'ROOT'){
		$('#schFldNm').val('');
		$('#schFldId').val('');
	}else{
		$('#schFldId').val(tree.id);
		$('#schFldNm').val(tree.fldNm);
	}
	dialog.close('findBcFldPop');
};

<%// [팝업:저장 후처리] - 트리 리로드 %>
function reloadTree(bcFldId){
	getIframeContent('fldTree').reload('./${listPage }FldFrm.do?menuId=${menuId}${agntParam }&bcFldId='+bcFldId);
	dialog.close('setFldDialog');
};
<%// 버튼 보이기 조절 - displayBtn 함수 이용  id배열 넘겨줌 %>
function applyDocBtn(action, param){
	var $area = $("#rightBtnArea");<%// lstTyp : 폴더ID, 분류ID %>
	$area.find('ul>li').remove();
	var btnHtml = getIframeContent('openListFrm').getRightBtnList();
	if(btnHtml != '')
		$area.append(btnHtml);
}
<%// [트리: 트리클릭] - 오른쪽 리스트 열기 %>
function openCdList(id){
	if(id!=null){
		gFldId = id;
	}
	//$("#openListFrm").attr('src', './${listPage}Frm.do?menuId=${menuId}&typ=L&schFldTypYn=F&schFldId='+id+'&listType=list');
	$("#openListFrm").attr('src', './${listPage }Frm.do?${nonParams}${agntParam }&schFldTypYn=F&listType=list&popYn=Y&schFldId='+id+'&typ='+$('#searchForm #typ').val());
};
<%// 저장, 삭제시 리로드 - 오른쪽 리스트 열기 %>
function reloadBcFrm(url, dialogId){
	getIframeContent('openListFrm').reloadBcFrm(url, dialogId);
	//reloadFrame('openListFrm', page);
};
<% // 엑셀 파일 다운로드 %>
function excelDownFile() {
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').excelDownFile();
	}
};
<%// 즐겨찾기 추가,삭제 %>
function fnBumkUpdate(bumkYn){
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').fnBumkUpdate(bumkYn);
	}
};
<%// 등록 수정 %>
function setBc(){
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').setBc();
	}
	
}
<%// 목록 %>
function listBc(){
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').listBc();
	}
}
<%// 명함복사(사용자) %>
function copyBcToUser(){
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').copyBcToUser();
	}
}
//명함 복사
function copyBc() {
	var objArr = getIframeContent('openListFrm').fnSelBc();
	if(objArr != null){
		selArrs= {mode:'copyBc',toBcIds : objArr};
		dialog.open('findBcFldPop','<u:msg titleId="wb.btn.fldChoi" alt="폴더 선택" />','./findBcFldPop.do?menuId=${menuId}&callback=transSelectFld');
	}
};

// 명함 이동
function moveBc() {
	var objArr = getIframeContent('openListFrm').fnSelBc();
	if(objArr != null){
		selArrs= {mode:'moveBc',toBcIds : objArr};
		dialog.open('findBcFldPop','<u:msg titleId="wb.btn.fldChoi" alt="폴더 선택" />','./findBcFldPop.do?menuId=${menuId}&callback=transSelectFld');
	}
};
<%// 선택삭제%>
function fnDelete(){
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').fnDelete();
	}
};

<%// 빠른 추가%>
function saveQuic(){
	if (validator.validate('setRegForm') && true/*confirmMsg("cm.cfrm.save")*/ ) {
		var $form = $('#setRegForm');
		$form.attr('method','post');
		$form.attr('action','./${transPage}.do?menuId=${menuId}');
		$form.attr('enctype','multipart/form-data');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};

<%// 친밀도 추가%>
function save(){
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').save();
	}
}<%// 친밀도 추가%>
function fnClnsReload(data){
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').fnClnsReload(data);
	}
}<%// [버튼:]취소 %>
function historyPage(){
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').historyPage();
	}
}<%//사진 변경 - 후처리 %>
function setImage(filePath, width, height){
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').setImage(filePath, width, height);
	}
	dialog.close('setImageDialog');
}<%//사진 미리보기 %>
function setImageReview( filePath ){
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').setImageReview(filePath);
	}
	dialog.close('setImageDialog');
}//이메일 발송
function setEmailSend(){
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').setEmailSend();
	}
};<% // 대리명함 select 선택 %>
function fnAgntSch(obj){
	if($(obj).val()=='') return;
	$('#schFldNm').val('');
	$('#schFldId').val('');
	searchForm.submit();
};
<% // 미팅 상세보기 %>
function viewMetngPop(bcMetngDetlId) {
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').viewMetngPop(bcMetngDetlId);
	}
};
<% // 보기형식 변경 %>
function setSrchTyp(typ){
	$('#searchForm #typ').val(typ);
}
<% // 명함 추가 %>
function addBcMultiPop(){
	if(gFldId==null){
		alertMsg("dm.jsp.setDoc.not.fld");<%//dm.jsp.setDoc.not.fld='폴더'를 선택 후 사용해 주십시요.%>
	} else {
		getIframeContent('openListFrm').addBcMultiPop(gFldId);
	}
}
<%// [POPUP] 조직 사용자 추가 - 싱글 %>
function addBcSinglePop(){
	getIframeContent('openListFrm').addBcSinglePop();
}
$(document).ready(function() {
	setUniformCSS();
	});
//-->
</script>

<% // 검색영역 %>
<div class="listarea" id="searchArea">
	<form name="searchForm" id="searchForm" action="./${listPage }.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="typ" value="${param.typ}" />
	<u:input type="hidden" id="schInitial" value="${param.schInitial}" />
	<u:input type="hidden" id="listPage" value="${listPage}" />
	
	<!-- 검색 페이지 -->
	<jsp:include page="/WEB-INF/jsp/wb/listBcSearch.jsp" >
		<jsp:param value="${pageSuffix }" name="pageSuffix"/>
	</jsp:include>
	</form>
</div>
<u:blank />

<% // 빠른 추가 %>
<c:if test="${(listPage eq 'listBc' || wbBcAgntAdmBVo.authCd eq 'RW' ) && wbBcUserScrnSetupRVo.inputWndLocCd eq 'UP' }"><jsp:include page="/WEB-INF/jsp/wb/setQuicBc.jsp" /></c:if>

<!-- LEFT -->
<div style="float:left; width:25.8%;" class="notPrint">

<u:title titleId="wb.jsp.listBcFld.fldTree" type="small" alt="폴더트리">
</u:title>

<u:titleArea frameId="fldTree" frameSrc="./${listPage }FldFrm.do?menuId=${menuId}${agntParam }"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
<div class="titlearea">
	<div class="tit_right">
		<c:if test="${listPage eq 'listBc' || listPage eq 'listPubBc' || (listPage eq 'listAgntBc' && wbBcAgntAdmBVo.authCd eq 'RW')}">
			<u:titleButton titleId="cm.btn.reg" alt="등록" onclick="regFld();" auth="W"/>
			<u:titleButton titleId="cm.btn.mod" alt="수정" onclick="modFld();" auth="W"/>
			<u:titleButton titleId="cm.btn.del" alt="삭제" onclick="delFld();" auth="W"/>
			<u:titleButton titleId="cm.btn.copy" alt="복사" onclick="copyFld();" auth="W"/>
			<u:titleButton titleId="cm.btn.move" alt="이동" onclick="moveFld();" auth="W"/>
		</c:if>
	</div>
</div>
	
</div>

	
<!-- RIGHT -->
<div style="float:right; width:71%;" class="print100">

<u:title titleId="wb.jsp.listBcFld.bcList" type="small" alt="명함목록" />

<u:titleArea frameId="openListFrm" frameSrc="/cm/util/reloadable.do"
	innerStyle="NO_INNER_IDV"
	frameStyle="width:100%; height:590px;" />
	
	<u:buttonArea id="rightBtnArea" noBottomBlank="true">
		<c:if test="${listPage ne 'listOpenDoc' }"><u:button titleId="cm.btn.reg" alt="등록" href="javascript:setBc();" auth="W" /></c:if>
	</u:buttonArea>
	
</div>

<u:blank />

<% // 빠른 추가 %>
<c:if test="${(listPage eq 'listBc' || wbBcAgntAdmBVo.authCd eq 'RW' ) && ( empty wbBcUserScrnSetupRVo || wbBcUserScrnSetupRVo.inputWndLocCd eq 'DOWN') }"><jsp:include page="/WEB-INF/jsp/wb/setQuicBc.jsp" /></c:if>

