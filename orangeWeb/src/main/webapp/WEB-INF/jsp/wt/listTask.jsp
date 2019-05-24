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

<u:title titleId="wt.jsp.listTask.title" alt="작업목록" menuNameFirst="true"/>

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listTask.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select>
			<option>제목</option>
			<option>내용</option>
			</select></td>
		<td><u:input id="schWord" maxByte="50" value="" titleId="cols.schWord" style="width: 300px" /></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:"><span>검색</span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<u:listArea>
	<tr>
	<td class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="head_ct"><u:msg titleId="cols.regDt" alt="등록일시" /></td>
	<td class="head_ct"><u:msg titleId="cols.taskAlocr" alt="작업할당자" /></td>
	<td class="head_ct"><u:msg titleId="cols.cmltRate" alt="완료율" /></td>
	<td class="head_ct"><u:msg titleId="cols.att" alt="첨부" /></td>
	</tr>

	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct">1</td>
	<td class="body_lt"><a href="./viewTask.do?menuId=${menuId}">작업관리 화면 뷰 작업</a></td>
	<td class="body_ct">2013-12-27</td>
	<td class="body_ct"><a href="javascript:viewUserPop('U0000003');">조용필</a></td>
	<td class="body_ct">10%</td>
	<td class="body_ct">&nbsp;</td>
	</tr>

</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.reg" alt="등록" href="./setTask.do?menuId=${menuId}" auth="W" />
</u:buttonArea>
