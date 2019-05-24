<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="frmYn" test="${!empty pageSuffix && pageSuffix == 'Frm' }" value="Y" elseValue="N"/>
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<u:params var="nonPageParams" excludes="schBcRegrUid,pageNo,bcId"/>
<script type="text/javascript">
<!--
// 미팅 상세보기
function viewMetngPop(bcMetngDetlId) {
	//location.href="./viewMetng.do?menuId=${menuId}&bcMetngDetlId="+bcMetngDetlId;
	parent.dialog.open('viewMetngPop', '<u:msg titleId="wb.jsp.viewMetngPop.title" alt="미팅상세보기" />', './viewMetngPop.do?menuId=${menuId}${agntParam}&bcMetngDetlId='+bcMetngDetlId);
};

//사진 삭제후 페이지 리로드
function pageReload(){
	location.replace('./${viewPage}.do?${paramsForList}&bcId=${wbBcBVo.bcId }${agntParam }');
};

//사진보기
function viewBcImageDetl(bcId){
	parent.dialog.open('viewImageDialog1', '<u:msg titleId="or.jsp.setOrg.viewImageTitle" alt="이미지 보기" />', './viewImagePop.do?menuId=${menuId}&bcId='+bcId);
};

//여러명의 사용자 선택
function openMuiltiUser(mode){
	var $view = $("#apntrList"), data = [];<%// data: 팝업 열때 오른쪽에 뿌릴 데이타 %>	
	$view.find("input[id='userUid']").each(function(){
		data.push({userUid:$(this).val()});
	});
	<%// option : data, multi, titleId %>
	searchUserPop({data:data, multi:true, mode:mode==null ?'search':'view'}, function(arr){
		if(arr!=null){
			var buffer = [];
			arr.each(function(index, userVo){
				buffer.push("<input type='hidden' name='bcApntrUid' id='userUid' value='"+userVo.userUid+"' />\n");
				buffer.push("<input type='hidden' name='userNm' id='rescNm' value='"+userVo.rescNm+"' />\n");
				buffer.push("<input type='hidden' name='rescId' id='rescId' value='"+userVo.rescId+"' />\n");
				buffer.push("<input type='hidden' name='updateYn' id='updateYn' value='N' />\n");
				buffer.push("<br/>\n");
			});
			$view.html(buffer.join(''));
			setUniformCSS($view[0]);
		}
	});
};

// 즐겨찾기 추가,삭제
fnBumkUpdate = function( bumkYn ){
	if(confirmMsg("wb.cfrm."+(bumkYn == 'Y' ? 'add' : 'del')+"Bumk")) {
		bumkUpdateForm.bumkYn.value = bumkYn;
		var $form = $('#bumkUpdateForm');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();	
	}
};

// 삭제
fnDelete = function(){
	if(confirmMsg("cm.cfrm.del")) {
		var $form = $('#deleteForm');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();	
	}
};
<% // [하단버튼:배열] %>
function getRightBtnList(){
	var $area = $("#rightBtnArea");
	return $area.find('ul')[0].outerHTML;
}
<%// 등록 수정 %>
function setBc(){
	location.href = './${setPage}${pageSuffix}.do?${paramsForList}${agntParam }&bcId=${param.bcId }';
}
<% // [하단버튼:목록] %>
function listBc(){
	location.replace($('#listPage').val());
}<%// 저장, 삭제시 리로드 %>
function reloadBcFrm(url, dialogId){
	//팝업 닫기
	if(dialogId != undefined && dialogId != null && dialogId !='') {
		if(dialogId == 'all') parent.dialog.closeAll();
		else parent.dialog.close(dialogId);
	}
	if(url != undefined && url != null) location.replace(url);
	else location.replace(location.href);
};
$(document).ready(function() {
	// 버튼 활성화
	<c:if test="${pageSuffix eq 'Frm'}">parent.applyDocBtn();</c:if>
setUniformCSS();
});
//-->
</script>

<c:if test="${empty pageSuffix}"><u:title titleId="wb.jsp.viewBc.title" alt="명함 등록" menuNameFirst="true"/></c:if>
<u:set var="style" test="${pageSuffix eq 'Frm' }" value="style='padding:10px;'" elseValue="style='padding-top:10px;'"/>
<div ${style }>
<form id="viewBc">
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<jsp:include page="/WEB-INF/jsp/wb/viewBcFrm.jsp" />

	<u:blank />

	<u:set var="rightBtnDisplay" test="${frmYn eq 'Y' }" value="display:none;" elseValue=""/>
	
	<% // 하단 버튼 %>
	<u:buttonArea id="rightBtnArea" style="${rightBtnDisplay }">
		<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
		<c:if test="${listPage eq 'listBc' || listPage eq 'listAllBc' || listPage eq 'listPubBc' || ( listPage ne 'listBc' && wbBcAgntAdmBVo.authCd eq 'RW' )}">
			<c:if test="${listPage eq 'listBc'}">
				<u:button titleId="cm.btn.${wbBcBVo.bumkYn eq 'Y' ? 'delBumk' : 'addBumk' }" alt="즐겨찾기에서${wbBcBVo.bumkYn eq 'Y' ? '제거' : '추가' }" href="javascript:fnBumkUpdate('${wbBcBVo.bumkYn eq 'Y' ? 'N' : 'Y' }');" auth="W" ownerUid="${listPage eq 'listBc' ? wbBcBVo.regrUid : ''}" />
			</c:if>
			<u:button titleId="cm.btn.mod" alt="수정" href="javascript:setBc();" auth="${listPage eq 'listAllBc' ? 'A' : 'W' }" ownerUid="${listPage eq 'listBc' || listPage eq 'listPubBc' ? wbBcBVo.regrUid : ''}"/>
			<u:button titleId="cm.btn.del" alt="삭제" href="javascript:;" onclick="fnDelete();" auth="${listPage eq 'listAllBc' ? 'A' : 'W' }" ownerUid="${listPage eq 'listBc' || listPage eq 'listPubBc' ? wbBcBVo.regrUid : ''}"/>
		</c:if>
		<u:button titleId="cm.btn.list" alt="목록" href="javascript:listBc();" auth="R" />
	</u:buttonArea>
</form>
<form id="bumkUpdateForm" name="bumkUpdateForm" action="./transBumkBc${pageSuffix }.do">
	<u:input type="hidden" name="menuId" value="${menuId}" />
	<u:input type="hidden" name="typ" value="${param.typ}" />
	<u:input type="hidden" name="bcId" value="${wbBcBVo.bcId}" />
	<u:input type="hidden" name="bumkYn" value="${wbBcBVo.bumkYn}" />
</form>
<form id="deleteForm" name="deleteForm" action="./${transDelPage }${pageSuffix }.do">
	<u:input type="hidden" name="menuId" value="${menuId}" />
	<u:input type="hidden" name="bcId" value="${wbBcBVo.bcId}" />
	<c:if test="${listPage eq 'listAgntBc' }">
		<u:input type="hidden" id="schBcRegrUid" name="schBcRegrUid" value="${schBcRegrUid }"/>
	</c:if>
	<u:input type="hidden" id="listPage" value="./${listPage}${pageSuffix }.do?${nonPageParams}${agntParam }" />
</form>
</div>