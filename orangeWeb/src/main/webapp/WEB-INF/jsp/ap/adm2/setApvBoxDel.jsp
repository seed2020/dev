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

<u:title title="결재함 비우기" menuNameFirst="true" />

<% // 목록 %>
<u:listArea id="listArea" colgroup="3%,">
	<tr>
	<td class="head_bg">&nbsp;</td>
	<td class="head_ct">결재함</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><u:checkbox name="chk" value="1" checked="false" /></td>
	<td class="body_lt">진행함</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><u:checkbox name="chk" value="1" checked="false" /></td>
	<td class="body_lt">반려함</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><u:checkbox name="chk" value="1" checked="false" /></td>
	<td class="body_lt">접수함</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><u:checkbox name="chk" value="1" checked="false" /></td>
	<td class="body_lt">배부함</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><u:checkbox name="chk" value="1" checked="false" /></td>
	<td class="body_lt">등록대장</td>
	</tr>
	
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="bodybg_ct"><u:checkbox name="chk" value="1" checked="false" /></td>
	<td class="body_lt">배부대장</td>
	</tr>
</u:listArea>

<u:pagination />

<% // 하단 버튼 %>
<u:buttonArea>
	<u:button title="비우기" auth="W" href="javascript:alert('선택한 결재함을 비우시겠습니까?');" />
</u:buttonArea>

