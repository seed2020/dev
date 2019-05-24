<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // [목록:제목] 보안글 조회를 위한 로그인폼 화면 %>
$(document).ready(function() {
	dialog.open('setLoginPop','<u:msg titleId="bb.jsp.setLoginPop.title" alt="보안글 인증" />','./setLoginPop.do?menuId=${menuId}&brdId=${param.brdId}&bullId=${param.bullId}&viewPage=${param.viewPage}');
});
//-->
</script>
