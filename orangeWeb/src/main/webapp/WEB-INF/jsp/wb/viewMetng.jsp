<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="agntParam" test="${!empty schBcRegrUid }" value="&schBcRegrUid=${schBcRegrUid }" elseValue=""/>
<u:params var="nonPageParams" excludes="schBcRegrUid,pageNo,bcMetngDetlId"/>
<script type="text/javascript">
<!--
//상세보기
function viewBc(bcId) {
	//location.href="./viewBc.do?menuId=${menuId}&bcId="+bcId;
	dialog.open('viewBcPop', '<u:msg titleId="wb.jsp.viewBcPop.title" alt="명함상세보기" />', './viewBcPop.do?menuId=${menuId}&bcId='+bcId+'${agntParam}');
};

//삭제
fnDelete = function(){
	if(confirmMsg("cm.cfrm.del")) {
		var $form = $('#deleteForm');
		$form.attr('method','post');
		$form.attr('target','dataframe');
		$form[0].submit();	
	}
};

$(document).ready(function() {
setUniformCSS();
});
//-->
</script>

<u:title titleId="wb.jsp.viewMetng.title" alt="관련미팅정보 등록" menuNameFirst="true"/>

<jsp:include page="/WEB-INF/jsp/wb/viewMetngContents.jsp" flush="false"/>

<u:blank />

<u:buttonArea>
<u:button titleId="cm.btn.print" alt="인쇄" onclick="printWeb()" auth="R" />
<c:if test="${( listPage eq 'listMetng' && ( empty param.schOpenYn || param.schOpenYn eq 'N' ) ) || listPage eq 'listAllMetng' || wbBcAgntAdmBVo.authCd eq 'RW'}">
	<u:button titleId="cm.btn.mod" alt="수정" href="./${setPage }.do?${paramsForList}&bcMetngDetlId=${wbBcMetngDVo.bcMetngDetlId }${agntParam }" auth="${listPage eq 'listAllMetng' ? 'A' : 'W' }" ownerUid="${listPage eq 'listMetng' ? wbBcMetngDVo.regrUid : ''}" />
	<u:button titleId="cm.btn.del" alt="삭제" href="javascript:;" onclick="fnDelete();" auth="${listPage eq 'listAllMetng' ? 'A' : 'W' }" ownerUid="${listPage eq 'listMetng' ? wbBcMetngDVo.regrUid : ''}" />
</c:if>
<u:button titleId="cm.btn.list" alt="목록" href="./${listPage }.do?${paramsForList}${agntParam }" auth="R" />
</u:buttonArea>

<form id="deleteForm" name="deleteForm" action="./${transDelPage }.do">
	<u:input type="hidden" name="menuId" value="${menuId}" />
	<u:input type="hidden" name="delList" value="${wbBcMetngDVo.bcMetngDetlId}" />
	<c:if test="${listPage eq 'listAgntMetng' }">
		<u:input type="hidden" id="schBcRegrUid" name="schBcRegrUid" value="${schBcRegrUid }"/>
	</c:if>
	<u:input type="hidden" id="listPage" value="./${listPage}.do?${nonPageParams}${agntParam }" />
</form>
