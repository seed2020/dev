<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<u:set var="viewMode" test="${empty param.mode || (!empty param.mode && param.mode ne 'view') }" value="N" elseValue="Y"/>
<div class="listarea" style="overflow-y:auto;height:300px;">
	<article>
		<c:forEach var="date" items="${fromToList }" varStatus="status"
		><c:set var="checked" value="N"/><c:set var="dateStyle" value=""/>
			<c:if test="${empty param.chkDates || (!empty param.chkDates && fn:contains(param.chkDates, date[0]))}"><c:set var="checked" value="Y" 
			/></c:if>
			<c:if test="${empty param.chkDates && (!empty param.holiYn && param.holiYn eq 'N' && 
				((!empty excludeList && fn:contains(excludeList, date[0])) || (date[1] eq '1' || date[1] eq '7'))) }"
			><c:set var="checked" value="N" /></c:if
			><c:if test="${(!empty excludeList && fn:contains(excludeList, date[0])) || date[1] eq '1'}"><c:set var="dateStyle" value="class=\"red_txt\""/></c:if
			><c:if test="${date[1] eq '7' }"><c:set var="dateStyle" value="class=\"color_txt\""/></c:if
			>
		<c:if test="${empty param.mode || (!empty param.mode && param.mode eq 'view' && checked eq 'Y')}">
		<div class="listdiv">
			<div class="list">
				<ul>
				<li class="tit">
					<fmt:parseDate var="date" value="${date[0] }" pattern="yyyyMMdd"
			/><strong ${dateStyle }><fmt:formatDate value="${date }" type="date" dateStyle="long" /></strong>
				</li>
			 </ul>
			</div>
		</div>
		</c:if>
		</c:forEach>
	</article>
</div>
<div class="popbtnarea">
<div class="btnarea">
	<div class="size">
	<dl>
		<dd class="btn" onclick="$m.dialog.close('fromToDateDialog');"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
	</dl>
	</div>
</div>
</div>
	
