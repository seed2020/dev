<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:set test="${param.lstTyp eq 'C'}" var="atrbIdPrefix" value="cls" elseValue="fld"	/>
<script type="text/javascript">
<!--
$(document).ready(function() {
	setUniformCSS();
});
//-->
</script>

<div style="padding:10px;">
<u:listArea id="listArea" >
	<tr id="headerTr">
	<th class="head_ct"><u:msg titleId="bb.btn.listOrdr" alt="목록순서" /></th>
	</tr>
	<c:forEach items="${itemDispList}" var="dispVo" varStatus="status">
		<tr onmouseover="this.className='trover'" onmouseout="this.className='trout'" >
		<td class="body_lt">${dispVo.colmVo.itemDispNm}</td>
		</tr>
	</c:forEach>
</u:listArea>
</div>