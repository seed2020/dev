<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<c:set var="width" value="${!empty param.width ? param.width : fn:contains(param.setupId, 'Bg') ? 283 : 203}"/>
<c:set var="height" value="${!empty param.height ? param.height : 63}"/>
<div style="width:${width}px">
<div onclick="dialog.close(this);" oncontextmenu="dialog.close(this);">
<img alt="<u:msg alt='이미지 보기' titleId='or.jsp.setOrg.viewImageTitle' />" src="${_cxPth}${skinImg }" width="${width-10}px" height="${height }px" oncontextmenu="return false;" />
</div>
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</div>