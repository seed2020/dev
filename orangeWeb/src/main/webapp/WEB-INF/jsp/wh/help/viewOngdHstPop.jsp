<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="params"/>
<script type="text/javascript">
<!--
//-->
</script>
<div style="width:550px;">
<u:title titleId="wh.cols.hdl.ongoCont" alt="진행사항" type="small" notPrint="true" />
<u:listArea id="listArea" colgroup="15%,35%,15%,35%"><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.recv.devPich" alt="개발담당자" /></td>
	<td class="body_lt"><a href="javascript:viewUserPop('${whReqOngdHVo.pichUid }');"><u:out value="${whReqOngdHVo.pichNm }"/></a></td>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.dueDt" alt="처리예정일" /></td>
	<td class="body_lt"><u:out value="${whReqOngdHVo.cmplDueDt }" type="date"/></td></tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.hdl.ongoCont" alt="진행사항"/></td>
	<td class="body_lt" colspan="3"><u:out value="${whReqOngdHVo.ongoCont }" type="html"/></td></tr>
	</u:listArea>
<u:blank />
<u:buttonArea>
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>