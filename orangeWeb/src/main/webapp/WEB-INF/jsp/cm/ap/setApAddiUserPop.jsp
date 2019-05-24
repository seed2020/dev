<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:250px">
<u:listArea>

	<tr>
	<td class="head_ct"><u:msg titleId="pt.top.selAddiUser" alt="겸직선택" /></td>
	</tr>
	<c:forEach items="${apvMapList}" var="apvMap">
	<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'">
	<td class="body_ct"><a href="${apvMap.url}"><u:out value="${apvMap.deptNm} : ${apvMap.count}" 
		/> <u:msg titleId="cm.count" alt="건" /></a></td>
	</tr>
	</c:forEach>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</div>