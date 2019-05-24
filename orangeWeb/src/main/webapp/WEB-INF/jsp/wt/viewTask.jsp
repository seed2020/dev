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

<u:title titleId="wt.jsp.viewTask.title" alt="작업조회" menuNameFirst="true"/>

<form id="viewTask">
<u:input type="hidden" id="menuId" value="${menuId}" />

<% // 폼 필드 %>
<u:listArea>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="body_lt" colspan="3">작업관리 화면 뷰 작업</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.cont" alt="내용" /></td>
	<td class="body_lt" colspan="3">작업관리 화면의 뷰 작성 및 링크 작업</td>
	</tr>

	<tr>
	<td width="18%" class="head_lt"><u:msg titleId="cols.regr" alt="등록자" /></td>
	<td width="32%" class="body_lt"><a href="javascript:viewUserPop('U0000003');">홍길동</a></td>
	<td width="18%" class="head_lt"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td width="32%" class="body_lt">2014-01-13</td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.ddln" alt="기한" /></td>
	<td class="body_lt">2014-01-16</td>
	<td class="head_lt"><u:msg titleId="cols.strtDt" alt="시작일시" /></td>
	<td class="body_lt">2014-01-14</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.prgStat" alt="진행상태" /></td>
	<td class="body_lt">진행</td>
	<td class="head_lt"><u:msg titleId="cols.prio" alt="우선순위" /></td>
	<td class="body_lt">낮음</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.cmltRate" alt="완료율" /></td>
	<td class="body_lt" colspan="3">70%</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.taskr" alt="작업자" />
	<td class="body_lt" colspan="3"><a href="javascript:viewUserPop('U0000003');">홍길동</a>(담당), 
		<a href="javascript:viewUserPop('U0000003');">심수봉</a>(담당), 
		<a href="javascript:viewUserPop('U0000003');">김윤아</a>(담당)</td>
	</tr>

	<tr>
	<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
	<td colspan="3">&nbsp;</td>
	</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.mod" href="./setTask.do?menuId=${menuId}&fnc=mod" alt="수정" />
	<c:if test="${param.fncMy != 'Y'}">
	<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?" var="msg" />
	<u:button titleId="cm.btn.del" href="javascript:confirm('${msg}');" alt="삭제" auth="W" />
	</c:if>
	<u:button titleId="cm.btn.list" href="javascript:history.go(-1);" alt="목록" />
</u:buttonArea>

</form>

