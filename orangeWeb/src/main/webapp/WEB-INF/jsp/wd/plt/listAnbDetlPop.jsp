<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%


%>
<div style="width:400px">

<u:listArea style="max-height:600px; overflow-x:hidden; overflow-y:auto;" colgroup="35%,20%,45%">
<tr>
	<th class="head_ct"><u:msg titleId="wd.jsp.useDate" alt="사용일" /></th>
	<th class="head_ct"><u:msg titleId="wd.jsp.cnt" alt="수량" /></th>
	<th class="head_ct"><u:msg titleId="ap.list.apvDt" alt="결재일시" /></th>
</tr>
<c:if test="${empty wdAnbUseLVoList}">
	<tr>
		<td class="nodata" colspan="3"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${not empty wdAnbUseLVoList}">
<c:forEach items="${wdAnbUseLVoList}" var="wdAnbUseLVo">
<tr>
	<td class="body_ct">${wdAnbUseLVo.useYmd}</td>
	<td class="body_ct">${wdAnbUseLVo.useCnt}</td>
	<td class="body_ct">${wdAnbUseLVo.regDt}</td>
</tr></c:forEach>
</c:if>
</u:listArea>

<u:buttonArea id="wdViewDetlCmdArea">
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>