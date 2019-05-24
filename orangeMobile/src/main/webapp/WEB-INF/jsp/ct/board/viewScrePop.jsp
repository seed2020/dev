<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<%-- <u:msg titleId="cols.scre" alt="점수" />
<c:forEach begin="1" end="5" step="1" varStatus="status"><u:set test="${status.count <= avgScre}" var="star" value="★" elseValue="☆" />${star}</c:forEach> --%>


        <div class="listarea">
        <article>
              
		<c:if test="${fn:length(ctScreHstLVoList) == 0}">
             <div class="listdiv_nodata" >
             <dl>
             <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
             </dl>
             </div>
		</c:if>
		<c:if test="${fn:length(ctScreHstLVoList) > 0}">
			<c:forEach items="${ctScreHstLVoList}" var="baScreHstLVo" varStatus="status">        
                   <div class="listdiv" onclick="$m.user.viewUserPop('${baScreHstLVo.userUid}');">
                       <div class="list">
                       <dl>
                       <dd class="tit">${baScreHstLVo.orUserBVo.rescNm}(${baScreHstLVo.orOdurBVo.lginId})
                        / ${baScreHstLVo.orUserBVo.deptRescNm} / ${baScreHstLVo.orUserBVo.gradeNm}</dd>
                       <dd class="name">
                       <c:forEach begin="1" end="5" step="1" varStatus="status">
                       	<u:set test="${status.count <= baScreHstLVo.scre}" var="star" value="★" elseValue="☆" />${star}
                       </c:forEach>
                       <u:out value="${baScreHstLVo.regDt}" type="longdate" />
					</dd>
                    </dl>
                       </div>
                   </div>
			</c:forEach>
		</c:if>

        </article>
        </div>

		<div class="blank20"></div>
        <div class="btnarea">
            <div class="size">
            <dl>
            <dd class="btn" onclick="$m.dialog.close('viewScrePop')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
         </dl>
            </div>
        </div>

