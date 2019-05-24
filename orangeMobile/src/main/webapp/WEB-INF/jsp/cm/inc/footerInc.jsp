<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><c:if
		test="${empty footerClass}">
	<div id="scrollSpace" style="height:0px;"></div>
	<div id="footerSpace" style="height:0px;"></div></c:if>
	<div class="footer" id="footer">
		<div class="wrap">
			<div class="${empty footerClass ? 'footer_mu' : footerClass }">
				<dl><c:forEach
    	
					items="${footerList}" var="ptMnuLoutDVo" varStatus="subStatus">
					<dd onclick="$m.nav.next(event, '${ptMnuLoutDVo.mnuUrl}');"><u:out value="${ptMnuLoutDVo.rescNm}" /></dd></c:forEach>
					<dd onclick="$m.menu.logout();"><u:msg titleId="pt.top.logout" alt="로그아웃" /></dd>
				</dl>
			</div>
		</div>
		<div class="copyright">copyright(C)2015 <strong>INNOBIZ</strong>. all rights reserved.</div>
	</div>
	
