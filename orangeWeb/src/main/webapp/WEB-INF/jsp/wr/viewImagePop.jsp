<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
function delPhoto(){
	if(confirmMsg("cm.cfrm.del")) {
		var $form = $("#photoImageForm");
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}
//-->
</script>
<form id="photoImageForm" method="post" action="./transImageDel.do">
	<c:set var="wrRescImgDVo" value="${wrRescMngBVo.wrRescImgDVo}" />
	<input type="hidden" name="menuId" value="${menuId}" />
	<input type="hidden" name="bcId" value="${param.rescMngId}" />
	<div style="width:800;text-align:center;">
		<div onclick="dialog.close(this);" oncontextmenu="dialog.close(this);">
		<img alt="<u:msg alt='이미지 보기' titleId='or.jsp.setOrg.viewImageTitle' />" src="${_cxPth}${wrRescImgDVo.imgPath}" width="${wrRescImgDVo.imgWdth}" height="${wrRescImgDVo.imgHght}" oncontextmenu="return false;" />
	</div>
	<u:buttonArea topBlank="true">
		<%-- <c:if test="${!empty param.bcId && empty param.isDel }"><u:button titleId="cm.btn.del" onclick="delPhoto();" alt="삭제" /></c:if> --%>		
		<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
	</u:buttonArea>
	</div>
</form>