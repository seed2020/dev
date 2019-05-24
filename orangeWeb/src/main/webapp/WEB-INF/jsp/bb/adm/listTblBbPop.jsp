<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<div style="width:400px">
<form id="listTblBbForm">

<% // 카테고리 %>
<div class="titlearea">
	<div class="tit_left">
	<dl>
	<dd class="title_s">${baTblBVo.rescNm}</dd>
	</dl>
	</div>
</div>

<% // 목록 %>
<u:listArea id="listArea">
	<tr>
	<td class="head_ct"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
	</tr>

<c:if test="${fn:length(baBrdBVoList) == 0}">
	<tr>
	<td class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baBrdBVoList) > 0}">
	<c:forEach items="${baBrdBVoList}" var="baBrdBVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_lt">${baBrdBVo.rescNm}</td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination noTotalCount="true" />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" />
</u:buttonArea>

</form>
</div>
