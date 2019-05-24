<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<c:set var="subj" value="커뮤니티 마스터 상반기 모임 공지" />
<c:set var="regr" value="조용필" />
<c:set var="regDt" value="2014-01-21" />
<c:set var="cont" value="부담없이 놀러 오세요~ 많이 많이 놀러 오세요~ 감사합니다." />

<u:title titleId="ct.jsp.viewNotc.title" alt="커뮤니티 공지사항 조회" menuNameFirst="true"/>

<form id="viewNotcForm">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<div class="listarea">
	<table class="listtable" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;">
	<colgroup><col width="18%"/><col width="32%"/><col width="18%"/><col width="32%"/></colgroup>
		<tr>
		<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
		<td colspan="3" class="body_lt">${ctAdmNotcBVo.subj}</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.regr" alt="등록자" /></td>
		<td class="body_lt">${ctAdmNotcBVo.regrNm}</td>
		<td class="head_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
		<td class="body_lt">
			<fmt:parseDate var="dateTempParse" value="${ctAdmNotcBVo.regDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd HH:mm:ss"/>
		</td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.readCnt" alt="조회수" /></td>
		<td class="body_lt">${ctAdmNotcBVo.readCnt}</td>
		<td class="head_lt"><u:msg titleId="ct.cols.exprDt" alt="만료일시" /></td>
				<td class="body_lt">
					<fmt:parseDate var="dateTempParse" value="${ctAdmNotcBVo.bullExprDt}" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate value="${dateTempParse}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
		</tr>
		
		<tr>
		<td colspan="4" class="body_lt" ><div style="overflow:auto;" class="editor">${ctAdmNotcBVo.cont}</div></td>
		</tr>
		
		<tr>
		<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
			<td colspan="3">
				<c:if test="${!empty fileVoList }"><u:files id="${filesId}" fileVoList="${fileVoList}" module="ct" mode="view" /></c:if>
			</td>
		</tr>
	</table>
</div>
<u:blank/>
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.list" alt="목록" auth="R" href="./listNotc.do?menuId=${menuId}" />
</u:buttonArea>

</form>

