<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function listCm(catgId) {
	location.href = "./listCm.do?menuId=${menuId}&catId="+catgId;
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title titleId="ct.jsp.listAllCm.title" alt="전체 커뮤니티" menuNameFirst="true"/>

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listCm.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select name="schCat">
			<option value = "communityOpt" checkValue="${param.schCat}" ><u:title titleId="ct.cols.cm" alt="커뮤니티" /></option>
			<option value = "masterOpt" checkValue="${param.schCat}"><u:title titleId="ct.cols.mastNm" alt="마스터" /></option>
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="" titleId="cols.schWord" style="width: 300px;" onkeydown="if (event.keyCode == 13) searchForm.submit();"/></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search" title="<u:msg titleId="cm.btn.search" alt="검색" />"><a href="javascript:document.searchForm.submit()"><span><u:msg titleId="cm.btn.search" alt="검색" /></span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<u:blank />

<c:set var="idx" value="0"/>
<c:set var="catLength" value="${catLength}"/>
<%-- <c:forEach var="allCtCatFldVo" items="${allCtCatFldList}" varStatus="status"> --%>

  <c:forEach var="i" begin="0" varStatus="status" end="${catLength}">
	<table border="0" cellpadding="0" cellspacing="0" style="width:100%">
	<tr>
	<td>
	<c:if test="${idx < fn:length(allCtCatFldList)}">
		<div class="left" style="width:49.2%;">
			<u:listArea>
				<tr>
					<td class="head_lt"><strong>${allCtCatFldList[idx].catNm}</strong></td>
				</tr>
				<tr>
					<td>
						<div style="padding:6px 2px 6px 2px;">
							<dl>
								<dd class="ptltxt_body">
									<c:if test="${fn:length(allCtCatClsList) != 0}">
										<c:forEach var="allCtCatClsVo" items="${allCtCatClsList}" varStatus="stat">
											<c:if test="${allCtCatClsVo.catPid == allCtCatFldList[idx].catId}">
												<a href="javascript:listCm('${allCtCatClsVo.catId}');">
													<c:choose>
														<c:when test="${allCtCatClsVo.catCmntCnt == '' || allCtCatClsVo.catCmntCnt == null}">
															${allCtCatClsVo.catNm}(0)</a><%-- <c:if test="${stat.last eq false}">,</c:if> --%>
														</c:when>
														<c:otherwise>
															${allCtCatClsVo.catNm}(${allCtCatClsVo.catCmntCnt})</a><%-- <c:if test="${stat.last eq false}">,</c:if> --%>
														</c:otherwise>
													</c:choose>
											</c:if>
										</c:forEach>
									</c:if>
								</dd>
							</dl>
						</div>
					</td>
				</tr>
			</u:listArea>
		</div>
	</c:if>
	<c:set var="idx" value="${idx + 1}" />

	<c:if test="${idx < fn:length(allCtCatFldList)}">
		<div class="right" style="width:49.2%; margin:0 2px 0 0;">
			<u:listArea>
				<tr>
					<td class="head_lt"><strong>${allCtCatFldList[idx].catNm}</strong></td>
				</tr>
				<tr>
					<td>
						<div style="padding:6px 2px 6px 2px;">
							<dl>
								<dd class="ptltxt_body">
									<c:if test="${fn:length(allCtCatClsList) != 0}">
										<c:forEach var="allCtCatClsVo" items="${allCtCatClsList}" varStatus="stat">
											<c:if test="${allCtCatClsVo.catPid == allCtCatFldList[idx].catId}">
												<a href="javascript:listCm('${allCtCatClsVo.catId}');">
													<c:choose>
														<c:when test="${allCtCatClsVo.catCmntCnt == '' || allCtCatClsVo.catCmntCnt == null}">
															${allCtCatClsVo.catNm}(0)</a><%-- <c:if test="${stat.last eq false}">,</c:if> --%>
														</c:when>
														<c:otherwise>
															${allCtCatClsVo.catNm}(${allCtCatClsVo.catCmntCnt})</a><%-- <c:if test="${stat.last eq false}">,</c:if> --%>
														</c:otherwise>
													</c:choose>
											</c:if>
										</c:forEach>
									</c:if>
								</dd>
							</dl>
						</div>
					</td>
				</tr>
			</u:listArea>
		</div>
	</c:if>
	<c:set var="idx" value="${idx + 1}" />
	</td>
	</tr>
	</table> 

</c:forEach>	


<c:if test="${fn:length(allCtCatFldList) == 0}">
	<u:listArea>
		<tr>
			<td class="nodata" colspan="9" border = "1"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
		</tr>
	</u:listArea>
</c:if>

<u:blank />
