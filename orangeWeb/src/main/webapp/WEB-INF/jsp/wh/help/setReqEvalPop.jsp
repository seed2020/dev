<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--<% // [하단우측버튼] - 저장 %>
function saveEval(){	
	var param=new ParamMap().getData('evalForm');
	callAjax('./transEvalAjx.do?menuId=${menuId}', param, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace(location.href);
		}
	});
}
//-->
</script>
<div style="width:700px;">
<form id="evalForm">
<u:input type="hidden" id="reqNo" value="${param.reqNo }"/>
<u:input type="hidden" id="evalYn" value="Y"/>
<u:listArea id="listArea" colgroup="15%,35%,15%,35%">
<tr>
	<td class="head_lt"><u:msg titleId="wh.cols.eval.score" alt="평가점수" /></td>
	<td class="body_lt"><select name="evalNo" style="min-width:100px;"><c:if test="${!empty whResEvalBVoList }"
	><c:forEach var="whResEvalBVo" items="${whResEvalBVoList }" varStatus="status"
	><u:option value="${whResEvalBVo.evalNo }" title="${whResEvalBVo.evalNm }" selected="${status.first }"/></c:forEach></c:if></select></td>
	<td class="head_lt"><u:msg titleId="wh.cols.eval.regDt" alt="평가일" /></td>
	<td class="body_lt"><u:calendar id="regDt" titleId="wh.cols.eval.regDt" value="today" /></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.eval.rson" alt="평가사유"/></td>
	<td class="body_lt" colspan="3"><u:textarea id="evalRson" titleId="wh.cols.eval.rson" maxByte="400" style="width:95%" rows="7" /></td>
	</tr><tr>
	<td class="head_lt"><u:msg titleId="wh.cols.eval.addReqr" alt="추가요청사항"/></td>
	<td class="body_lt" colspan="3"><u:textarea id="addReqr" titleId="wh.cols.eval.addReqr" maxByte="400" style="width:95%" rows="7" /></td>
</tr>
</u:listArea>
</form>
	<u:blank />
	<% // 하단 버튼 %>
<u:buttonArea style="clear:both;">
	<u:button titleId="cm.btn.save" onclick="saveEval();" alt="저장" />
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</div>
