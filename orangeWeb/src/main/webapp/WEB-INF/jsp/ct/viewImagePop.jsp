<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:${dialogWidth};text-align:center;">
	<div onclick="dialog.close(this);" oncontextmenu="dialog.close(this);">
	<c:if test="${fnc =='ques'}">
		<img alt="<u:msg alt='이미지 보기' titleId='or.jsp.setOrg.viewImageTitle' />" src="${_cxPth}${ctSurvQuesDVo.imgSavePath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' />
	</c:if>
	<c:if test="${fnc =='exam'}" >
		<img alt="<u:msg alt='이미지 보기' titleId='or.jsp.setOrg.viewImageTitle' />" src="${_cxPth}${ctQuesExamDVo.imgSavePath}" onerror='this.src="${_cxPth}/images/${_skin}/photo_noimg.png"' />
	</c:if>
</div>
<u:buttonArea topBlank="true">
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>
