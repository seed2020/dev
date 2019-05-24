<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//<![CDATA[
$(document).ready(function() {
	$layout.adjustBodyHtml('bodyHtmlArea');
	
	<%// 목록의 footer 위치를 일정하게 %>
	//$space.placeFooter('list');
	$space.placeFooter('tabview');
});
//]]>
</script>
<!--section S-->
<section>
		<!--titlezone S-->
        <div class="titlezone">
            <div class="titarea">
            <dl>
            <dd class="tit">${_ctEstbBVo.ctNm}</dd>
            <dd class="name">
            	<c:if test="${_ctEstbBVo.logUserJoinStat == '3'}">
					<c:choose>
						<c:when test="${_ctEstbBVo.logUserSeculCd == 'M'}">
							<u:msg titleId="ct.option.mbshLev0" alt="마스터"/>
						</c:when>
						<c:when test="${_ctEstbBVo.logUserSeculCd == 'G'}">
							<u:msg titleId="ct.option.mbshLev4" alt="게스트"/>
						</c:when>
						<c:when test="${_ctEstbBVo.logUserSeculCd == 'S'}">
							<u:msg titleId="ct.option.mbshLev1" alt="스텝"/>
						</c:when>
						<c:when test="${_ctEstbBVo.logUserSeculCd == 'A'}">
							<u:msg titleId="ct.option.mbshLev3" alt="준회원"/>
						</c:when>
						<c:when test="${_ctEstbBVo.logUserSeculCd == 'R'}">
							<u:msg titleId="ct.option.mbshLev2" alt="정회원"/>
						</c:when>
					</c:choose>
					|
					<c:choose>
						<c:when test="${_ctEstbBVo.ctActStat eq 'S'}">
							<u:msg titleId="ct.option.joinStat01" alt="승인대기중"/>
						</c:when>
						<c:when test="${_ctEstbBVo.ctStat ne 'C' && _ctEstbBVo.ctActStat eq 'A'}">
							<u:msg titleId="ct.cols.act" alt="활동중"/>
						</c:when>
						<c:when test="${_ctEstbBVo.ctStat eq 'C' && _ctEstbBVo.ctActStat eq 'A'}">
							<u:msg titleId="ct.cols.closeWait" alt="폐쇄신청중" />						
						</c:when>
						<c:when test="${_ctEstbBVo.ctActStat eq 'C'}">
							<u:msg titleId="ct.cols.close" alt="폐쇄"/>
						</c:when>
					</c:choose>
					| ${_ctEstbBVo.mbshCnt}
				</c:if>
            </dd>
         </dl>
            </div>
        </div>
        <!--//titlezone E-->
       
        
       <div  class="s_tablearea" >
        	<div class="blank30"></div>
            <table class="s_table">
            <!-- <caption>타이틀</caption> -->
            <colgroup>
                <col width="36%"/>
                <col width=""/>
            </colgroup>
            <tbody>
                <tr>
                    <th class="shead_lt"><u:msg titleId="ct.cols.mastNm" alt="마스터" /></th>
                    <td class="sbody_lt"><a href="javascript:;" onclick="$m.user.viewUserPop('${_ctEstbBVo.mastUid}');">${_ctEstbBVo.mastNm}</a></td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="ct.cols.statusOfMembers" alt="회원현황" /></th>
                    <td class="shead_lt"><u:msg titleId="ct.cols.all" alt="전체" /> ${allPeople}, <u:msg titleId="ct.cols.today" alt="오늘" /> ${todayPeople}</td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="ct.cols.setup.day" alt="설립일" /></th>
                    <td class="shead_lt"><fmt:parseDate var="dateTempParse" value="${_ctEstbBVo.ctApvdDt}" pattern="yyyy-MM-dd HH:mm:ss"/><fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/></td>
                </tr>
                <tr>
                    <th class="shead_lt"><u:msg titleId="ct.cols.attFileOver" alt="첨부용량제한(MB)" /></th>
                    <td class="shead_lt">${fileSum} /<c:choose>
				<c:when test="${_ctEstbBVo.attLimSize == '-1'}"><u:msg titleId="ct.cols.attFileUnlim" alt="무제한" /></c:when>
				<c:otherwise>${_ctEstbBVo.attLimSize}MB</c:otherwise>
				</c:choose></td>
                </tr>
        
            </tbody>
            </table>
        </div>
        <div id="tabViewArea">	
		<div class="bodyzone_scroll">
			<div class="bodyarea">
			<dl>
				<dd class="bodytxt_scroll">
					<div class="scroll editor" id="bodyHtmlArea">
						<div id="zoom">${_ctEstbBVo.ctItro}</div>
					</div>
				</dd>
			</dl>
			</div>
		</div>
        </div>    
		<jsp:include page="/WEB-INF/jsp/ct/inc/footerInc.jsp" flush="false" />
    
</section>
<!--//section E-->
        
  