<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:350px">
<u:listArea id="listArea" colgroup="38%,">	
	<tr id="headerTr">
		<th class="head_ct"><u:msg titleId="cm.cols.recv.user" alt="받은사람"/></th>
		<th class="head_ct"><u:msg titleId="cm.cols.confirm.time" alt="확인시간"/></th>
	</tr>
	<c:if test="${fn:length(cuNoteRecvLVoList) == 0}">
		<tr>
		<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(cuNoteRecvLVoList)>0}">
		<c:forEach items="${cuNoteRecvLVoList}" var="cuNoteRecvLVo" varStatus="status">
			<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
				<td class="body_ct"><a href="javascript:viewUserPop('${cuNoteRecvLVo.recvrUid }');">${cuNoteRecvLVo.recvrRescNm }</a></td>
				<td class="body_ct"><u:out value="${cuNoteRecvLVo.recvDt }" type="longdate"/></td>
			</tr>
		</c:forEach>
	</c:if>
	
</u:listArea>
</div>
