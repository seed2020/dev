<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
//$(document).ready(function() {
//});
//]]>
</script><c:forEach
	
		items="${orUserMapList}" var="orUserMap" varStatus="status">
	<div class="listdiv2d">
		<div class="list_lt" onclick="$m.user.viewUserPop('${orUserMap.userUid}')">
		<dl>
			<dd class="tit"><u:out value="${orUserMap.rescNm}" /><c:if
				test="${not empty orUserMap.mbno}"> (<u:out value="${orUserMap.mbno}" />)</c:if><c:if
				test="${empty orUserMap.mbno and not empty orUserMap.compPhon}"> (<u:out value="${orUserMap.compPhon}" />)</c:if></dd>
			<dd class="body"><u:out value="${orUserMap.deptRescNm}" /><c:if
				test="${not empty orUserMap.positNm}"> / <u:out value="${orUserMap.positNm}" /></c:if></dd>
		</dl>
		</div>
		
		<div class="list_rt">
		<dl><c:if
				test="${not empty orUserMap.mbno}">
			<m:tel value="${orUserMap.mbno}" type="image" />
			<m:sms value="${orUserMap.mbno}" type="image" /></c:if><c:if
				test="${empty orUserMap.mbno and not empty orUserMap.compPhon}">
			<m:tel value="${orUserMap.compPhon}" type="image" /></c:if>
		</dl>
		</div>
	</div></c:forEach><c:if
		
		test="${fn:length(orUserMapList) == 0}">
		<div class="listdiv_nodata">
		<dl><dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd></dl>
		</div>
	</c:if><c:if
	
		test="${not empty recodeCount and recodeCount != 0}">
	<m:pagination />
	<div class="blank20"></div>
	</c:if>
