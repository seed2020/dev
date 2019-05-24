<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--
function formSubmit(ctId){
	if(confirmMsg("ct.cfrm.colse")) { <% // ct.cfrm.colse=폐쇄 하시겠습니까? %>
		callAjax('./setCmntClose.do?menuId=${menuId}', {ctId:ctId}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.href = './setMngClose.do?menuId=${menuId}&ctId=${ctId}';
			}else if(data.result == 'guest'){
				location.href = './setMngClose.do?menuId=${menuId}&ctId=${ctId}';
			}else if(data.result == 'colse'){
				location.href = './setMngClose.do?menuId=${menuId}&ctId=${ctId}';
			}
		});
 }
	
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title title="${menuTitle }"  alt="커뮤니티폐쇄" menuNameFirst="true"/>

<div class="headbox">
<dl>
<dd class="headbox_tit"><u:msg titleId="ct.jsp.setMngClose.tx01" alt="커뮤니티 폐쇄는 다음과 같은 경우에만 가능합니다." /></dd>
<dd class="headbox_body">- <u:msg titleId="ct.jsp.setMngClose.tx02" alt="해당 커뮤니티의 마스터인 경우" /></dd>
<dd class="headbox_body">- <u:msg titleId="ct.jsp.setMngClose.tx03" alt="해당 커뮤니티의 마스터외에 가입되어 있는 회원이 없는 경우" /></dd>
<dd class="headbox_red">※ <u:msg titleId="ct.jsp.setMngClose.tx04" alt="가입 회원이 존재 할 경우에는 [커뮤니티관리] &gt; [회원관리] &gt; [회원정보변경] &gt; [회원전체목록] 에서 회원을 탈퇴시킬 수 있습니다." /></dd>
</dl>
</div>

<u:blank />

<u:listArea>
	<tr>
	<td class="head_lt"><strong>${ctEstbBVo.catPnm}&gt;${ctEstbBVo.catNm} [ ${ctEstbBVo.ctNm} ] </strong> 커뮤니티 정보</td>
	</tr>

	<tr>
		<td>
			<div style="padding:6px 2px 0 2px;">
				<dl>
				<dd class="ptltxt_heads"><strong><u:msg titleId="cols.mast" alt="마스터" /> :</strong> ${ctEstbBVo.mastNm}</dd>
				<dd class="ptltxt_heads"><strong><u:msg titleId="cols.mbshCnt" alt="회원수" /> :</strong> ${allPeople}명(마스터포함)</dd>
				<fmt:parseDate var="dateTempParse" value="${ctEstbBVo.ctApvdDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<dd class="ptltxt_heads"><strong><u:msg titleId="cols.createDt" alt="생성일시" /> :</strong> <fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/></dd>
				<dd class="ptltxt_heads"><strong><u:msg titleId="ct.cols.ctActStat" alt="상태" /> :</strong>
					<c:choose>
						<c:when test="${ctEstbBVo.ctStat == 'A'}">
							<c:if test="${ctEstbBVo.ctActStat == 'A'}">
								<u:msg titleId="ct.cols.act" alt="활동중" />
									<c:if test="${ctEstbBVo.clsReqsYn == 'Y'}">
										<dd class="ptltxt_heads">
											<div class="ellipsis" title="${ctEstbBVo.rjtOpinCont}">
												<strong><u:msg titleId="ct.cols.clsRjtOpin" alt="폐쇄거부 사유" /> :</strong> ${ctEstbBVo.rjtOpinCont}
											</div>
										</dd>
									</c:if>
							</c:if>
							<c:if test="${ctEstbBVo.ctActStat == 'C'}">
								<u:msg titleId="ct.cols.close" alt="폐쇄" />
									<dd class="ptltxt_heads">
										<div class="ellipsis" title="${ctEstbBVo.rjtOpinCont}">
											<strong><u:msg titleId="ct.cols.opin" alt="사유" /> :</strong> ${ctEstbBVo.rjtOpinCont}
										</div>
									</dd>
							</c:if>
						</c:when>
						<c:when test="${ctEstbBVo.ctStat == 'C'}">
							<c:if test="${ctEstbBVo.ctActStat == 'A'}">
							
								<u:msg titleId="ct.cols.closeWait" alt="폐쇄신청중" />
							</c:if>
							<c:if test="${ctEstbBVo.ctActStat == 'C'}">
								<u:msg titleId="ct.cols.close" alt="폐쇄" />
									<dd class="ptltxt_heads">
										<div class="ellipsis" title="${ctEstbBVo.rjtOpinCont}">
											<strong><u:msg titleId="ct.cols.opin" alt="사유" /> :</strong> ${ctEstbBVo.rjtOpinCont}
										</div>
									</dd>
							</c:if>
						</c:when>
						<c:when test="${ctEstbBVo.ctStat == 'R'}">
							<c:if test="${ctEstbBVo.ctActStat == 'C'}">
								<u:msg titleId="ct.cols.close" alt="폐쇄" />
									<dd class="ptltxt_heads">
										<div class="ellipsis" title="${ctEstbBVo.rjtOpinCont}">
											<strong><u:msg titleId="ct.cols.opin" alt="사유" /> :</strong> ${ctEstbBVo.rjtOpinCont}
										</div>
									</dd>
							</c:if>
						</c:when>
					</c:choose>
				</dd>
				</dl>
			</div>
		</td>
	</tr>
</u:listArea>
<u:blank />

<% // 하단 버튼 %>
<u:buttonArea>
	<if test="${ctEstbBVo.ctStat == 'A'}">
		<c:if test="${ctEstbBVo.ctActStat == 'A'}">
			<u:button titleId="ct.btn.cmClose" alt="커뮤니티 폐쇄" href="javascript:formSubmit('${ctId}')"/>
		</c:if>
	</if>
	
</u:buttonArea>

<u:blank />

