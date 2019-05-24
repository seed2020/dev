<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--

$(document).ready(function() {
	setUniformCSS();
	$('#schWord').focus();
});
//-->
</script>
<% // 검색 %>
<form name="searchForm" action="./selectBbFrm.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />
	<u:input type="hidden" id="brdId" value="${param.brdId}" />
	<u:input type="hidden" id="mul" value="${param.mul}" />
	<u:input type="hidden" id="exYn" value="${param.exYn}" />
	<u:input type="hidden" id="replyYn" value="${param.replyYn}" />
	<u:input type="hidden" id="kndCd" value="${param.kndCd}" />
	<u:input type="hidden" id="brdTypCd" value="${param.brdTypCd}" />
	<u:input type="hidden" id="discYn" value="${param.discYn}" />
	<u:input type="hidden" id="tblId" value="${param.tblId}" />
	
	<u:listArea>
		<tr>
		<td width="27%" class="head_lt"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
		<td ><table border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td><u:input id="schWord" maxByte="50" value="${param.schWord}" titleId="cols.bbNm" />
				<input type="hidden" name="schCat" value="BRD_NM" />
				</td>
			<td><a href="javascript:searchForm.submit()" class="ico_search"><span><u:msg alt="검색" titleId="cm.btn.search" /></span></a></td>
			</tr>
			</table></td>
		</tr>
	</u:listArea>
</form>

<% // 목록 %>
<u:listArea id="listArea">
	<tr>
	<td width="3%" class="head_bg">
		<c:if test="${param.mul == 'Y'}">
		<input type="checkbox" id="checkHeader" title="<u:msg titleId='cm.check.all' />" onclick="checkAllCheckbox('listArea', this.checked);" value=""/>
		</c:if>
		</td>
	<td width="97%" class="head_ct"><u:msg titleId="cols.bbNm" alt="게시판명" /></td>
	</tr>

<c:if test="${fn:length(baBrdBVoList) == 0}">
	<tr>
	<td class="nodata" colspan="2"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</c:if>
<c:if test="${fn:length(baBrdBVoList) > 0}">
	<c:forEach items="${baBrdBVoList}" var="brdVo" varStatus="status">
		<u:set test="${baBrdBVo.brdId == brdVo.brdId}" var="disabled" value="Y" elseValue="N" />
		<c:if test="${!empty param.brdIds && baBrdBVo.brdId ne brdVo.brdId}">
			<c:set var="doneLoop" value="false"/>
			<c:set var="brdCheckYn" value="N"/>
			<c:forTokens items="${param.brdIds }" delims="," var="brdId">
				<c:if test="${not doneLoop && brdId eq brdVo.brdId}">
					<c:set var="brdCheckYn" value="Y"/>
					<c:set var="doneLoop" value="true"/>
				</c:if>
			</c:forTokens>
		</c:if>
	<tr>
	<td class="bodybg_ct">
		<c:if test="${param.mul == 'Y'}">
		<u:checkbox name="chk" value="${brdVo.brdId}" checked="${disabled == 'Y' || brdCheckYn eq 'Y'}" disabled="${disabled}" />
		</c:if>
		<c:if test="${param.mul != 'Y'}">
		<u:radio name="chk" value="${brdVo.brdId}" checked="false" disabled="${disabled}" />
		</c:if>
		</td>
	<td class="body_lt"><label for="chk${brdVo.brdId}">${brdVo.rescNm}</label></td>
	</tr>
	</c:forEach>
</c:if>
</u:listArea>

<u:pagination noTotalCount="true" />
