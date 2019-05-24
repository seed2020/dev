<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<script type="text/javascript">
<!--

function setSerialNo() {
	dialog.open('setSerialNoPop', '채번수정', './setSerialNoPop.do?menuId=${menuId}&fnc=mod');
}

$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<u:title title="채번관리" menuNameFirst="true" />

<% // 검색영역 %>
<u:searchArea>
	<form name="searchForm" action="./listSerialNo.do" >
	<u:input type="hidden" id="menuId" value="${menuId}" />

	<table class="search_table" cellspacing="0" cellpadding="0" border="0">
	<tr>
	<td><table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<u:radio name="schScop" value="1" title="전체" inputClass="bodybg_lt" />
		<u:radio name="schScop" value="2" title="검색" inputClass="bodybg_lt" />
		<td class="width20"></td>
		<td class="search_tit">부서명</td>
		<td><u:input id="schWord" value="" titleId="cols.schWord" style="width: 150px;" /></td>
		<td><a href="javascript:" class="ico_search"><span>검색아이콘</span></a></td>
		</tr>
		</table></td>
	<td><div class="button_search"><ul><li class="search"><a href="javascript:"><span>검색</span></a></li></ul></div></td>
	</tr>
	</table>
	</form>
</u:searchArea>

<% // 목록 %>
<u:listArea id="listArea" colgroup="3%,30%,20%,20%,27%">
	<tr>
	<td class="head_bg"><u:checkbox name="chk" value="all" checked="false" /></td>
	<td class="head_ct">부서명</td>
	<td class="head_ct">SEED</td>
	<td class="head_ct">채번값</td>
	<td class="head_ct">비고</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><u:checkbox name="chk" value="1" checked="false" /></td>
	<td class="body_ct"><a href="javascript:setSerialNo();">홍보부</a></td>
	<td class="body_ct">140215</td>
	<td class="body_ct">30</td>
	<td class="body_ct">&nbsp;</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><u:checkbox name="chk" value="1" checked="false" /></td>
	<td class="body_ct"><a href="javascript:setSerialNo();">개발팀</a></td>
	<td class="body_ct">140214</td>
	<td class="body_ct">32</td>
	<td class="body_ct">&nbsp;</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><u:checkbox name="chk" value="1" checked="false" /></td>
	<td class="body_ct"><a href="javascript:setSerialNo();">영업1부</a></td>
	<td class="body_ct">140213</td>
	<td class="body_ct">512</td>
	<td class="body_ct">&nbsp;</td>
	</tr>
</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:msg titleId="cm.msg.preparing" var="msg" alt="준비중.." />
	<u:button title="초기화" auth="W" href="javascript:alert('${msg}');" />
	<u:button title="전체초기화" auth="W" href="javascript:alert('${msg}');" />
</u:buttonArea>

