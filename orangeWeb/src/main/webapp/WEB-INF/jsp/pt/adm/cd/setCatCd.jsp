<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	// listCdFrm.jsp 에서 패딩 주지 않도록 설정
	request.setAttribute("paddingYn", "N");

	// 프레임을 사용하지 않음을 세팅
	request.setAttribute("noFrameYn", "Y");
%>
<script type="text/javascript">
<!--
function goBack(){
	<c:if test="${not empty param.backTo}">
	location.replace("${param.backTo}");
	</c:if>
	<c:if test="${empty param.backTo}">
	history.back();
	</c:if>
}
//-->
</script>

<c:if test="${empty menuTitle}">
<u:title titleId="pt.jsp.setCd.catCdMng" alt="카테고리 코드 관리" />
</c:if><c:if test="${not empty menuTitle}">
<u:title title="${menuTitle}" alt="직급/직책 ...코드 관리" />
</c:if>

<%@ include file="./listCdFrm.jsp"%>

<u:buttonArea noBottomBlank="true">
	<u:button href="javascript:move('up')" titleId="cm.btn.up" alt="위로이동" auth="A" />
	<u:button href="javascript:move('down')" titleId="cm.btn.down" alt="아래로이동" auth="A" />
	<u:button href="javascript:addRow()" titleId="cm.btn.plus" alt="행추가" auth="A" />
	<u:button href="javascript:delRow()" titleId="cm.btn.minus" alt="행삭제" auth="A" />
	<u:button href="javascript:delSelRow()" titleId="cm.btn.selDel" alt="선택삭제" auth="A" />
	<u:button href="javascript:saveCd()" titleId="cm.btn.save" alt="저장" auth="A" />
	<c:if test="${not empty param.backTo}"><u:button href="javascript:goBack()" titleId="cm.btn.cancel" alt="취소" /></c:if>
</u:buttonArea>