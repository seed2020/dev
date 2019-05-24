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
	<u:msg titleId="cm.cfrm.del" alt="삭제하시겠습니까?" var="msg" />
	<u:button titleId="cm.btn.selDel" alt="선택삭제" href="javascript:confirm('${msg}');" auth="W" />
	<u:button titleId="cm.btn.allDel" alt="전체삭제" href="javascript:confirm('${msg}');" auth="W" />
</u:buttonArea>
