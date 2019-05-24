<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%// 폐쇄된 커뮤니티 Alert %>
function closedAlert(closeOpin , ctStat){
	if(ctStat == 'ctStat') alertMsg("ct.close.request",closeOpin);	
	else alertMsg("ct.close.complete",closeOpin);
};

<%// 커뮤니티 홈 화면 %>
function viewCm(ctId, fncUid) {
	location.href = "./viewCm.do?menuId="+ fncUid +"&ctId=" + ctId + "&prevMenuId=${menuId}";
};

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ct.jsp.listMyCm.title" alt="나의 커뮤니티" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listMyCm.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<!-- <u:input type="hidden" id="signal" value="my" /> -->

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
				<u:option value="communityOpt" titleId="ct.cols.ctNm" alt="커뮤니티" checkValue="${param.schCat}" />
				<u:option value="masterOpt" titleId="ct.cols.mastNm" alt="마스터" checkValue="${param.schCat}" />
			</select></td>
			
			<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.schWord" style="width: 400px;" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>
			<td class="width20"></td>
			<td class="search_tit">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<u:checkbox name="schClose" value="Y" titleId="ct.cols.close" checkValue="${param.schClose}" textClass="search_body" />
					</tr>
				</table>
			</td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<u:listArea id="listArea">
	<tr>
	<td width="6%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="ct.cols.cm" alt="커뮤니티" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mast" alt="마스터" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.mbshCnt" alt="회원수" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="ct.cols.createDt" alt="생성일시" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.auth" alt="권한" /></td>
	<td width="10%" class="head_ct"><u:msg titleId="cols.stat" alt="상태" /></td>
	</tr>
	
	<c:forEach var="myCtVo" items="${myCtMapList}" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td class="body_ct">${recodeCount - myCtVo.rnum + 1}</td>
			<td class="body_lt">
			<a <c:if test="${myCtVo.ctStat eq 'A'}" >href="javascript:viewCm('${myCtVo.ctId}','${myCtVo.ctFncUid}');"</c:if> 
			   <c:if test="${myCtVo.ctStat eq 'C' || myCtVo.ctActStat eq 'C' }" >href="javascript:closedAlert('${myCtVo.rjtOpinCont}','${myCtVo.ctActStat eq 'C' ? 'ctActStat' : 'ctStat' }');"</c:if>
			>
				<u:out value="${myCtVo.ctNm}"/>
				<c:choose>
					<c:when test="${myCtVo.logUserJoinStat == '1'}">
						(<u:msg titleId="ct.option.joinStat01" alt="승인대기중" />)
					</c:when>
					<c:when test="${myCtVo.logUserJoinStat == '2'}">
						
					</c:when>
					<c:when test="${myCtVo.logUserJoinStat == '3'}">
						
					</c:when>
					<c:otherwise>
						<u:icoBest icon="" />
					</c:otherwise>
				</c:choose>
			</a>
				<c:choose>
					<c:when test="${myCtVo.ctEvalCd == 'B'}">
						<u:icoBest icon="best" />
					</c:when>
					<c:when test="${myCtVo.ctEvalCd == 'E'}">
						<u:icoBest icon="excellent" />
					</c:when>
					<c:when test="${myCtVo.ctEvalCd == 'G'}">
						<u:icoBest icon="good" />
					</c:when>
					<c:otherwise>
						<u:icoBest icon="" />
					</c:otherwise>
				</c:choose>
				<c:if test="${myCtVo.recmdYn == 'Y'}">
					<u:icon type="recommend" display="true" />
				</c:if>
			</td>
							
			<td class="body_ct"><a href="javascript:viewUserPop('${myCtVo.mastUid}');">${myCtVo.mastNm}</a></td>
			<td class="body_ct">${myCtVo.mbshCnt}</td>
			<td class="body_ct">
				<fmt:parseDate var="dateTempParse" value="${myCtVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd"/>
			</td>
			<td class="body_ct">
				<c:if test="${myCtVo.logUserJoinStat == '3'}">
					<c:choose>
						<c:when test="${myCtVo.logUserSeculCd == 'M'}">
							<u:msg titleId="ct.option.mbshLev0" alt="마스터"/>
						</c:when>
						<c:when test="${myCtVo.logUserSeculCd == 'G'}">
							<u:msg titleId="ct.option.mbshLev4" alt="게스트"/>
						</c:when>
						<c:when test="${myCtVo.logUserSeculCd == 'S'}">
							<u:msg titleId="ct.option.mbshLev1" alt="스텝"/>
						</c:when>
						<c:when test="${myCtVo.logUserSeculCd == 'A'}">
							<u:msg titleId="ct.option.mbshLev3" alt="준회원"/>
						</c:when>
						<c:when test="${myCtVo.logUserSeculCd == 'R'}">
							<u:msg titleId="ct.option.mbshLev2" alt="정회원"/>
						</c:when>
					</c:choose>
				</c:if>
			</td>
			<td class="body_ct">
				<c:choose>
					<c:when test="${myCtVo.ctActStat eq 'S'}">
						<u:msg titleId="ct.option.joinStat01" alt="승인대기중"/>
					</c:when>
					<c:when test="${myCtVo.ctStat ne 'C' && myCtVo.ctActStat eq 'A'}">
						<u:msg titleId="ct.cols.act" alt="활동중"/>
					</c:when>
					<c:when test="${myCtVo.ctStat eq 'C' && myCtVo.ctActStat eq 'A'}">
						<u:msg titleId="ct.cols.closeWait" alt="폐쇄신청중" />						
					</c:when>
					<c:when test="${myCtVo.ctActStat eq 'C'}">
						<u:msg titleId="ct.cols.close" alt="폐쇄"/>
					</c:when>
				</c:choose>
			</td>
		</tr>
	
	</c:forEach>
	 
	<c:if test="${fn:length(myCtMapList) == 0}">
			<tr>
				<td class="nodata" colspan="9"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
	</c:if>
</u:listArea>

<u:pagination />

