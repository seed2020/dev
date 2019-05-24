<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
$(document).ready(function() {
	<%// 목록의 footer 위치를 일정하게 %>
	$space.placeFooter('list');
});
//]]>
</script>
<section>
    <div class="listarea">
    <article>
             
	<c:if test="${fn:length(ctVistrHstLVoList) == 0}">
            <div class="listdiv_nodata" >
            <dl>
            <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
            </dl>
            </div>
	</c:if>
	<c:if test="${fn:length(ctVistrHstLVoList) > 0}">
		<c:forEach items="${ctVistrHstLVoList}" var="baReadHstLVo" varStatus="status">
                  <div class="listdiv" onclick="$m.user.viewUserPop('${baReadHstLVo.userUid}');">
                      <div class="list">
                      <dl>
                      <dd class="tit">${baReadHstLVo.orUserBVo.rescNm}(${baReadHstLVo.orOdurBVo.lginId})
                       / ${baReadHstLVo.orUserBVo.deptRescNm} / ${baReadHstLVo.orUserBVo.gradeNm}</dd>
                      <dd class="name">
                      <u:out value="${baReadHstLVo.accsDt}" type="longdate" />
				</dd>
                   </dl>
                      </div>
                  </div>
		</c:forEach>
	</c:if>

    </article>
    
</div>
	<m:pagination />
    
<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
</section>


