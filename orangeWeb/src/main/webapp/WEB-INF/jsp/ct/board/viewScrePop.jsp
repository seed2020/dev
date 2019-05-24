<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<div style="width:650px">
<form id="viewScrePopForm">

<div class="front">
<div class="front_left">
	<table border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td class="color_stxt">※ <u:msg titleId="cols.scre" alt="점수" />
		<c:forEach begin="1" end="5" step="1" varStatus="status"><u:set test="${status.count <= avgScre}" var="star" value="★" elseValue="☆" />${star}</c:forEach>
		</td>
	</tr>
	</tbody></table>
</div>
</div>

<% // 목록 %>
<u:listArea id="listArea" colgroup=",150,150,100,125">
	<tr>
	<td class="head_ct"><u:msg titleId="cols.scre" alt="점수" /></td>
	<td class="head_ct"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td class="head_ct"><u:msg titleId="cols.dept" alt="부서" /></td>
	<td class="head_ct"><u:msg titleId="cols.grade" alt="직급" /></td>
	<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	</tr>
<c:if test="${fn:length(ctScreHstLVoList) == 0}">
	<tr>
	<td class="nodata" colspan="5"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(ctScreHstLVoList) > 0}">
	<c:forEach items="${ctScreHstLVoList}" var="ctScreHstLVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct"><c:forEach begin="1" end="5" step="1" varStatus="status"><u:set test="${status.count <= ctScreHstLVo.scre}" var="star" value="★" elseValue="☆" />${star}</c:forEach></td>
	<td class="body_ct"><a href="javascript:viewUserPop('${ctScreHstLVo.userUid}');">${ctScreHstLVo.orUserBVo.rescNm}(${ctScreHstLVo.orOdurBVo.lginId})</a></td>
	<td class="body_ct">${ctScreHstLVo.orUserBVo.deptRescNm}</td>
	<td class="body_ct">${ctScreHstLVo.orUserBVo.gradeNm}</td>
	<td class="body_ct"><u:out value="${ctScreHstLVo.regDt}" type="longdate" /></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button onclick="dialog.close(this);" titleId="cm.btn.close" alt="닫기" />
</u:buttonArea>

</form>
</div>
