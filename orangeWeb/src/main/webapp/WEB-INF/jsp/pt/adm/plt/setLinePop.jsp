<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<div style="width:400px">
<form id="setLinePop">
<input type="hidden" name="menuId" value="${menuId}" />
<u:input id="mode" type="hidden" value="${param.mode}" />
<u:input id="clsCd" type="hidden" value="${ptCdBVo.clsCd}" />
<u:listArea>

	<c:if test="${param.type=='H'}">
	<tr>
	<td width="35%" class="head_lt"><u:msg titleId="pt.jsp.setPltStep2.vertiPx" alt="세로 픽셀" /></td>
	<td><u:input id="px" value="" titleId="pt.jsp.setPltStep2.vertiPx" maxByte="400" valueOption="number" mandatory="Y" /></td>
	</tr>
	</c:if>
	<c:if test="${param.type=='V'}">
	<tr>
	<td width="35%" class="head_lt"><u:msg titleId="pt.jsp.setPltStep2.horizPx" alt="가로 픽셀" /></td>
	<td><u:input id="px" value="" titleId="pt.jsp.setPltStep2.horizPx" maxByte="400" valueOption="number" mandatory="Y" /></td>
	</tr>
	</c:if>
	
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" href="javascript:saveLine('${param.type}', $('#setLinePop #px').val());" id="btnSaveLine" alt="저장" auth="W" />
	<u:button titleId="cm.btn.cancel" onclick="dialog.close(this);" alt="취소" />
</u:buttonArea>

</form>
</div>