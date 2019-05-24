<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<section>
    <div class="listarea">
    <article>
             
	<c:if test="${fn:length(baReadHstLVoList) == 0}">
            <div class="listdiv_nodata" >
            <dl>
            <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
            </dl>
            </div>
	</c:if>
	<c:if test="${fn:length(baReadHstLVoList) > 0}">
		<c:forEach items="${baReadHstLVoList}" var="baReadHstLVo" varStatus="status">
                  <div class="listdiv" onclick="$m.user.viewUserPop('${baReadHstLVo.userUid}');">
                      <div class="list">
                      <dl>
                      <dd class="tit">${baReadHstLVo.orUserBVo.rescNm}(${baReadHstLVo.orOdurBVo.lginId})
                       / ${baReadHstLVo.orUserBVo.deptRescNm} / ${baReadHstLVo.orUserBVo.gradeNm}</dd>
                      <dd class="name">
                      <u:out value="${baReadHstLVo.readDt}" type="longdate" />
				</dd>
                   </dl>
                      </div>
                  </div>
		</c:forEach>
	</c:if>

    </article>
	<m:pagination />
    
    <jsp:include page="/WEB-INF/jsp/cm/inc/footerInc.jsp" flush="false" />
    
</div>
</section>


