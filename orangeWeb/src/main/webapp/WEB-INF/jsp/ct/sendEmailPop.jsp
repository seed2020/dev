<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" 
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>

<div style="width:700px">
<form id="sendEmailPop">

<c:set var="subj" value="" />
<c:set var="sendr" value="신승훈 &lt;yj@enobiz.com&gt;" />
<c:set var="recvr" value="김건모 &lt;yj@enobiz.com&gt;" />
<c:set var="cont" value="" />

<u:listArea noBottomBlank="true">
	<tr>
	<td width="27%" class="head_lt"><u:msg titleId="cols.subj" alt="제목" /></td>
	<td width="73%"><u:input id="subj" value="${subj}" titleId="cols.subj" style="width: 493px;" maxByte="240" /></td>
	</tr>
	
	<tr>
	<td class="head_lt"><u:msg titleId="cols.sendr" alt="보낸사람" /></td>
	<td><u:input id="sendr" value="${sendr}" titleId="cols.sendr" style="width: 493px;" /></td>
	</tr>
	
	<tr>
	<td rowspan="2" class="head_lt"><u:msg titleId="cols.recvr" alt="받는사람" /></td>
	<td><table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td width="230"><u:input id="recvr" value="" titleId="cols.recvr" style="width: 456px;" /></td>
		<td><u:buttonS href="" titleId="cm.btn.search" alt="검색" /></td>
		</tr>
		</tbody></table></td>
	</tr>
	
	<tr>
	<td><table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody>
		<tr>
		<td width="230"><select size="3" style="width: 465px;">
			<option>${recvr}</option>
			</select></td>
		<td style="padding-top: 2px; vertical-align: top;"><u:buttonS href="" titleId="cm.btn.del" alt="삭제" /></td>
		</tr>
		</tbody></table></td>
	</tr>
</u:listArea>
<u:listArea noBottomBlank="true">
	<tr>
	<td id="editor1Area" colspan="2"><u:editor id="editor1" width="697" height="300" 
		module="bb" areaId="editor1Area" value="${cont}" /></td>
	</tr>
</u:listArea>
<u:listArea noBottomBlank="true">
	<tr>
	<td class="head_lt"><u:msg titleId="cols.attFile" alt="첨부파일" /></td>
	<td colspan="3">
		<div class="attachbtn">
		<dl>
		<dd class="attachbtn_check"><input type="checkbox" id="contactBack"/></dd>
		<dd><a href="javascript:" class="sbutton button_small"><span>파일첨부</span></a><a href="javascript:" class="sbutton button_small"><span><img src="${_cxPth}/images/${_skin}/ico_delete.png"/>삭제</span></a></dd>
		</dl>
		</div>
		</td>
	</tr>
</u:listArea>

<u:blank />

<u:buttonArea>
	<u:msg titleId="bb.msg.send.success" var="msg" alt="성공적으로 발송하였습니다." />
	<u:button titleId="cm.btn.send" onclick="alert('${msg}'); dialog.close(this);" alt="보내기" auth="R" />
	<u:button titleId="cm.btn.close" onclick="dialog.close(this);" alt="닫기" />
</u:buttonArea>

</form>
</div>
