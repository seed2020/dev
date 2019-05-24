<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<div style="width:600px">
<form id="setMyBullPop">

<!-- LEFT -->
<div style="float:left; width:47%;">

<u:titleArea outerStyle="height: 270px" innerStyle="width:94%; margin: 0 auto 0 auto; padding: 10px 0 0 0">
	<u:title titleId="wc.jsp.setGrpChoiPop.tx02" type="small" alt="선택된 그룹 목록" />

	<select size="10" style="width:100%; height:225px; margin: 0; padding: 0;">
		</select>
</u:titleArea>

</div>

<!-- ICON -->
<div style="float:left; width:6%; height: 270px;">

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
<tr>
<td style="vertical-alignment: middle;">
	<table align="center" width="20" border="0" cellpadding="0" cellspacing="0"><tbody>
	<tr>
	<td><u:buttonIcon href="javascript:" titleId="cm.btn.left" alt="왼쪽으로이동" /></td>
	</tr>

	<tr>
	<td class="height5"></td>
	</tr>

	<tr>
	<td><u:buttonIcon href="javascript:" titleId="cm.btn.right" alt="오른쪽으로이동" /></td>
	</tr>
	</tbody></table></td>
</tr>
</tbody></table>
</div>

<!-- RIGHT -->
<div style="float:right; width:47%;">

<u:titleArea outerStyle="height: 270px" innerStyle="width:94%; margin: 0 auto 0 auto; padding: 10px 0 0 0">
	<u:title titleId="wc.jsp.setGrpChoiPop.tx01" type="small" alt="그룹 목록" />

	<select size="13" style="width:100%; height:225px; margin: 0; padding: 0;">
		<option>부천지역모임</option>
		<option>인천지역모임</option>
		</select>
</u:titleArea>

</div>

<u:buttonArea>
	<u:button titleId="cm.btn.save" onclick="dialog.close(this);" alt="저장" auth="R" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
