<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><c:if


test="${not empty orUserImgDVo.imgWdth}"
><div style="width:${dialogWidth};">
<div onclick="dialog.close(this);" oncontextmenu="dialog.close(this);">
<img alt="<u:msg alt='이미지 보기' titleId='or.jsp.setOrg.viewImageTitle' 
/>" src="${_ctx}${orUserImgDVo.imgPath}" width="${orUserImgDVo.imgWdth}" height="${orUserImgDVo.imgHght}" oncontextmenu="return false;" />
</div>

<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div></c:if><c:if


test="${empty orUserImgDVo.imgWdth}"><script type="text/javascript">
function resizeImgPop(){
	var $img = $('#userImgPopImg');
	var w = $img.width(), h = $img.height()+42;
	if(w>100){
		$('#userImgPopDiv').width(Math.max(w,200)).height(h);
		dialog.resize('viewImageDialog');
	}
}
</script><div id="userImgPopDiv" style="width:200px">
<div onclick="dialog.close(this);" oncontextmenu="dialog.close(this);">
<img id="userImgPopImg" alt="<u:msg alt='이미지 보기' titleId='or.jsp.setOrg.viewImageTitle' 
/>" src="${orUserImgDVo.imgPath}" oncontextmenu="return false;" onload="resizeImgPop();" onerror="this.src = '/images/${_skin}/photo_noimg.png'" />
</div>

<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div></c:if>

