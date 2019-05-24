<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><%

%>
<div style="width:400px">

<u:listArea id="joinMpArea" colgroup="30%,70%">
<tr>
	<td class="head_ct"><u:msg titleId="wp.mp" alt="공수" /></td>
	<td class="body_lt"><u:checkArea>
	<u:radio value="1.0" title="1.0" id="md1" name="rsltMd" checked="true"/>
	<u:radio value="0.5" title="0.5" id="md2" name="rsltMd" />
	<u:radio value="0" title="0" id="md3" name="rsltMd" />
	</u:checkArea></td>
</tr>
<tr>
	<td class="head_ct"><u:msg titleId="cols.note" alt="비고" /></td>
	<td ><u:textarea id="note" name="note" titleId="cols.note" rows="4" style="width:95%" maxByte="120"/></td>
</tr>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.confirm" alt="확인" onclick="setPrjMp()" auth="W" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>
</div>