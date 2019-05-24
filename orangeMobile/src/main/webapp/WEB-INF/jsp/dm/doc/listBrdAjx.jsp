<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<c:forEach items="${baBrdBVoList}" var="baBrdBVo" varStatus="status">
	<div class="listdiv_fixed">
		<div class="listcheck_fixed">
		<dl>
		<m:check type="radio" id="brdChk${baBrdBVo.brdId}" name="brdChk" value="${baBrdBVo.brdId}" areaId="brdListArea" checked="${status.count==1 }"/>
		</dl>
		</div>
		
		<div class="list_fixed" >
		<dl>
		<dd class="tit">${baBrdBVo.rescNm}</dd>
		</dl>
		</div>
	</div></c:forEach><c:if
		
		test="${fn:length(baBrdBVoList) == 0}">
		<div class="listdiv_nodata">
		<dl><dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd></dl>
		</div>
	</c:if>
