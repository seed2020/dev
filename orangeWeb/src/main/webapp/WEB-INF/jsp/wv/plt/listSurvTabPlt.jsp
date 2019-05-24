<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="tabHght" test="${!empty tabList && fn:length(tabList) > 1 }" value="28" elseValue="0"/>
<script type="text/javascript">
<!--
<% // [탭클릭] 게시물 조회 %>
function fnTabClick(tId){
	getIframeContent('survTabArea').reload('./listSurvTabFrm.do?tId='+tId+'&${paramsForList}&hghtPx=${param.hghtPx-tabHght-15}');
};

$(document).ready(function() {
	<%// 유니폼 적용 %>
	//setUniformCSS();
});
//-->
</script>
<% // 목록 %>
<div class="ptlbody_ct" >
<!-- TAB ID가 2개 이상일경우에만 탭을 구성한다. -->
<%-- <c:if test="${tabHght > 0 }">
	<u:tabGroup id="bcTab" noBottomBlank="true">
		<c:forEach var="tabInfo" items="${tabList }" varStatus="status">
			<u:tab id="ctTab" titleId="wb.jsp.${tabInfo[1] }.title" on="${tabInfo[0] eq tId }" onclick="fnTabClick('${tabInfo[0] }');"/>
		</c:forEach>
	</u:tabGroup>
</c:if>
 --%>
<iframe id="survTabArea" name="survTabArea" src="./listSurvTabFrm.do?tId=${tId }&${paramsForList }&hghtPx=${param.hghtPx-tabHght-15}" style="width:100%;height:${param.hghtPx-tabHght-15}px;" frameborder="0" marginheight="0" marginwidth="0"></iframe>
</div>