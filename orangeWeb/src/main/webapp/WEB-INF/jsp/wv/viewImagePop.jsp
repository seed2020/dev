<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:${dialogWidth};text-align:center;">
	<div onclick="dialog.close(this);" oncontextmenu="dialog.close(this);">
	<img alt="<u:msg alt='이미지 보기' titleId='or.jsp.setOrg.viewImageTitle' />" src="${_cxPth}${wvQueExamVo.imgSavePath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' />
</div>
<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>
