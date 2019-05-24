<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>


<% // 목록 %>
<u:listArea id="listArea">
	<tr>
	<td width="30%" class="head_ct"><u:msg titleId="cols.readDt" alt="조회일시" /></td>
	<td class="head_ct"><u:msg titleId="cols.readr" alt="조회자" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td width="20%" class="head_ct"><u:msg titleId="cols.grade" alt="직급" /></td>
	</tr>
<c:if test="${fn:length(baReadHstLVoList) == 0}">
	<tr>
	<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baReadHstLVoList) > 0}">
	<c:forEach items="${baReadHstLVoList}" var="baReadHstLVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct"><u:out value="${baReadHstLVo.readDt}" type="longdate" /></td>
	<td class="body_ct"><a href="javascript:parent.viewUserPop('${baReadHstLVo.userUid}');">${baReadHstLVo.orUserBVo.rescNm}(${baReadHstLVo.orOdurBVo.lginId})</a></td>
	<td class="body_ct">${baReadHstLVo.orUserBVo.deptRescNm}</td>
	<td class="body_ct">${baReadHstLVo.orUserBVo.gradeNm}</td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:blank />
<u:pagination noTotalCount="true"/>
