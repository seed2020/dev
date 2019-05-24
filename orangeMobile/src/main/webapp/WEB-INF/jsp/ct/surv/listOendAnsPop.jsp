<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
        <div class="s_tablearea view contCls">
        	<div class="blank30"></div>
            <table class="s_table">
            <colgroup>
                <col width="100%"/>
            </colgroup>
            <thead>
                <tr>
                <th class="shead_lt">
					<u:msg titleId="cols.ques" alt="질문" /><u:out value="${quesCount} ) ${ctsQueVo.quesCont}"  maxLength="65" />
                </th>
                </tr>
			</thead>
         </table>
     </div>

        <div class="listarea">
        <article>
              

		<c:if test="${fn:length(ctSurvBMapList) == 0}">
             <div class="listdiv_nodata" >
             <dl>
             <dd class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></dd>
             </dl>
             </div>
		</c:if>
		<c:if test="${fn:length(ctSurvBMapList) > 0}">
			<c:forEach  var="wvsOend" items="${ctSurvBMapList}"  varStatus="status">
                   <div class="listdiv">
                       <div class="list" >
                       <dl>
                       <dd class="tit" style="white-space:normal;text-overflow:clip;">
                       <u:out value="${wvsOend.oendReplyCont}"/>
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
            <dd class="btn" onclick="$m.dialog.close('listOendAnsPop')"><u:msg titleId="cm.btn.close" alt="닫기" /></dd>
         </dl>
            </div>
        </div>
