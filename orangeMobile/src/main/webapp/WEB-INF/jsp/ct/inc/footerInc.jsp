<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
function goMyCmList(url){
	$m.nav.prev(event, url);
}
function fnDropOut(){
	$m.dialog.open({
		id:'dropOutPop',
		title:'${footer.ctFncNm}',
		url:'${footer.ctFncUrl}${footer.ctFncUid}&ctId=${footer.ctId}',
	});  
}
//]]>
</script>
<c:if test="${empty footerClass}">
	<div id="scrollSpace" style="height:0px;"></div>
	<div id="footerSpace" style="height:0px;"></div></c:if>
	<div class="footer" id="footer">
		<div class="wrap">
			<div class="${empty footerClass ? 'footer_mu' : footerClass }">
				<dl>
					<dd onclick="fnDropOut();"><u:out value="${footer.ctFncNm}" /></dd>
					<dd onclick="$m.menu.logout();"><u:msg titleId="pt.top.logout" alt="로그아웃" /></dd>
				</dl>
			</div>
		</div>
		<div class="copyright">copyright(C)2015 <strong>INNOBIZ</strong>. all rights reserved.</div>
	</div>
	
