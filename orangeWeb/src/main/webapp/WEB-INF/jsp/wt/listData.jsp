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

<u:title titleId="wt.jsp.listData.title" alt="작업 데이터 관리" menuNameFirst="true"/>

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listData.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><select>
			<option>1</option>
			<option>2</option>
			<option>3</option>
			<option>4</option>
			<option>5</option>
			<option selected>6</option>
			<option>7</option>
			<option>8</option>
			<option>9</option>
			<option>10</option>
			<option>11</option>
			<option>12</option>
			</select></td>
		<td class="width5"></td>
		<td class="search_body"><u:msg titleId="wt.jsp.listData.tx01" alt="개월 이전 작업" /></td>
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
	<td width="3%" class="head_bg"><u:checkbox name="chk" value="all" checked="false" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td class="head_ct"><u:msg titleId="cols.cmltDt" alt="완료일시" /></td>
	<td class="head_ct"><u:msg titleId="cols.taskAlocr" alt="작업할당자" /></td>
	<td class="head_ct"><u:msg titleId="cols.cmltRate" alt="완료율" /></td>
	<td class="head_ct"><u:msg titleId="cols.att" alt="첨부" /></td>
	</tr>

	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><u:checkbox name="chk" value="1" checked="false" /></td>
	<td class="body_lt"><a href="./viewTask.do?menuId=${menuId}">작업관리 화면 뷰 작업</a></td>
	<td class="body_ct">2014-01-27</td>
	<td class="body_ct"><a href="javascript:viewUserPop('U0000003');">조용필</a></td>
	<td class="body_ct">100%</td>
	<td class="body_ct">&nbsp;</td>
	</tr>

</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.save.success" alt="저장 되었습니다." var="msg" />
	<u:button titleId="wt.btn.srchPrdSave" alt="검색기간저장" href="javascript:alert('${msg}');" auth="W" />
	<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?" var="msg" />
	<u:button titleId="cm.btn.del" alt="삭제" href="javascript:confirm('${msg}');" auth="W" />
</u:buttonArea>
