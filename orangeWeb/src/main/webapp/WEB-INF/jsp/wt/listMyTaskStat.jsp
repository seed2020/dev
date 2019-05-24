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

<u:title titleId="wt.jsp.listMyTaskStat.title" alt="나의 작업 통계" menuNameFirst="true"/>

<u:title titleId="wt.jsp.listMyTaskStat.subTitle01" type="small" alt="작업관리 처리 현황" />
<u:listArea>
	<tr>
	<td colspan="2" class="head_ct"><u:msg titleId="wt.cols.total" alt="총계" /></td>
	<td class="head_ct"><u:msg titleId="wt.cols.roleO" alt="지시" /></td>
	<td class="head_ct"><u:msg titleId="wt.cols.roleR" alt="담당" /></td>
	<td class="head_ct"><u:msg titleId="wt.cols.roleA" alt="책임" /></td>
	<td class="head_ct"><u:msg titleId="wt.cols.roleC" alt="협의" /></td>
	<td class="head_ct"><u:msg titleId="wt.cols.roleI" alt="참조" /></td>
	</tr>

	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td width="16%" class="body_ct"><u:msg titleId="wt.cols.all" alt="전체" /></td>
	<td width="14%" class="body_ct">0</td>
	<td width="14%" class="body_ct">0</td>
	<td width="14%" class="body_ct">0</td>
	<td width="14%" class="body_ct">0</td>
	<td width="14%" class="body_ct">0</td>
	<td width="14%" class="body_ct">0</td>
	</tr>

	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct"><u:msg titleId="wt.cols.notCmlt" alt="미완료" /></td>
	<td class="body_ct">0</td>
	<td class="body_ct">0</td>
	<td class="body_ct">0</td>
	<td class="body_ct">0</td>
	<td class="body_ct">0</td>
	<td class="body_ct">0</td>
	</tr>

	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct"><u:msg titleId="wt.cols.cmlt" alt="완료" /></td>
	<td class="body_ct">0</td>
	<td class="body_ct">0</td>
	<td class="body_ct">0</td>
	<td class="body_ct">0</td>
	<td class="body_ct">0</td>
	<td class="body_ct">0</td>
	</tr>
</u:listArea>

<u:title titleId="wt.jsp.listMyTaskStat.subTitle02" type="small" alt="지시" />
<u:listArea>
	<tr>
	<td width="5%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.cmltRate" alt="완료율" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleR" alt="담당" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleA" alt="책임" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleC" alt="협의" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleI" alt="참조" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.ddln" alt="기한" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.dlayDays" alt="지연일수" /></td>
	</tr>

	<tr>
	<td colspan="9" style="height: 24px;" class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</u:listArea>

<u:title titleId="wt.jsp.listMyTaskStat.subTitle03" type="small" alt="담당" />
<u:listArea>
	<tr>
	<td width="5%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.cmltRate" alt="완료율" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleO" alt="지시" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleA" alt="책임" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleC" alt="협의" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleI" alt="참조" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.ddln" alt="기한" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.dlayDays" alt="지연일수" /></td>
	</tr>

	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct">1</td>
	<td class="body_lt">작업관리 뷰 작업</td>
	<td class="body_ct">30</td>
	<td class="body_ct"><a href="javascript:viewUserPop('U0000003');">조용필</a></td>
	<td class="body_ct">&nbsp;</td>
	<td class="body_ct">&nbsp;</td>
	<td class="body_ct">&nbsp;</td>
	<td class="body_ct">2013-12-31</td>
	<td class="body_ct"></td>
	</tr>
</u:listArea>

<u:title titleId="wt.jsp.listMyTaskStat.subTitle04" type="small" alt="책임" />
<u:listArea>
	<tr>
	<td width="5%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.cmltRate" alt="완료율" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleO" alt="지시" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleR" alt="담당" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleC" alt="협의" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleI" alt="참조" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.ddln" alt="기한" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.dlayDays" alt="지연일수" /></td>
	</tr>

	<tr>
	<td colspan="9" style="height: 24px;" class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</u:listArea>

<u:title titleId="wt.jsp.listMyTaskStat.subTitle05" type="small" alt="협의" />
<u:listArea>
	<tr>
	<td width="5%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.cmltRate" alt="완료율" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleO" alt="지시" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleR" alt="담당" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleA" alt="책임" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleI" alt="참조" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.ddln" alt="기한" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.dlayDays" alt="지연일수" /></td>
	</tr>

	<tr>
	<td colspan="9" style="height: 24px;" class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</u:listArea>

<u:title titleId="wt.jsp.listMyTaskStat.subTitle06" type="small" alt="참조" />
<u:listArea>
	<tr>
	<td width="5%" class="head_ct"><u:msg titleId="cols.no" alt="번호" /></td>
	<td class="head_ct"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.cmltRate" alt="완료율" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleO" alt="지시" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleR" alt="담당" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleA" alt="책임" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.roleC" alt="협의" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="cols.ddln" alt="기한" /></td>
	<td width="8%" class="head_ct"><u:msg titleId="wt.cols.dlayDays" alt="지연일수" /></td>
	</tr>

	<tr>
	<td colspan="9" style="height: 24px;" class="nodata"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
	</tr>
</u:listArea>

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.download" alt="다운로드" href="" auth="R" />
</u:buttonArea>
