<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

	// 아이콘 배경 이미지 스타일 명의 뒤 두자리
	String[] numbers = { "01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26" };
	request.setAttribute("numbers", numbers);

%>

<div id="chooseIconPop" style="width:655px; height:390px;">

	<div id="header_${_skin}" style="width:655px; height:350px;<c:if test='${not browser.ie or browser.ver > 7}'> padding-left:5px;</c:if> background:#ffffff">
	<div class="topmuarea">
	<dl style="float:left">
	<c:forEach items="${numbers}" var="number" varStatus="status">
	<dd class="topmu"><div style="text-align:center"><u:msg titleId="pt.topicon.${number}"/></div><a href="javascript:void(0);" onclick="activePopIcon(this, '${param.mode}');" class="topmu_${number}" onfocus='this.blur()'><span><u:msg titleId="pt.topicon.${number}"/></span></a></dd>
	<c:if test="${not status.last}"></c:if><dd class="topmu_blank"></dd>
	</c:forEach>
	</dl>
	</div>
	</div>

	<u:buttonArea>
		<u:button titleId="cm.btn.confirm" href="javascript:setIcons();" alt="확인" auth="A" />
		<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
	</u:buttonArea>

</div>