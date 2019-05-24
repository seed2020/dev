<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
		%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
		%><%

%>
<script type="text/javascript">
<!--
<% // 아이프레임 높이 %>
function setListCmtFrmHeight() {
	$("#listCmtFrm", window.parent.document).load(function() {
		var h = $(this).contents().find("#cmtListArea").height();
		h += 115;
		$(this).height( h );
	});
}
<% // [등록] 한줄답변 등록 %>
function regCmt() {
	var cmt = $('#cmt').val();
	if ($.trim(cmt) == '') {
		alertMsg('bb.msg.cmt.required');<% // bb.msg.cmt.required=한줄답변을 입력하세요. %>
		$('#cmt').focus();
		return;
	}
	callAjax('./transCmtAjx.do?menuId=${menuId}', {bullId:'${param.bullId}', cmt:cmt}, function(data) {
		if (data.message != null) {
			alert(data.message);
		}
		if (data.result == 'ok') {
			location.replace(location.href);
		}
	});
}
<% // [삭제] 한줄답변 삭제 %>
function delCmt(id) {
	if (confirmMsg("cm.cfrm.del")) {	<% // cm.cfrm.del=삭제하시겠습니까? %>
		callAjax('./transCmtDelAjx.do?menuId=${menuId}', {bullId:'${param.bullId}', cmtId:id}, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				location.replace(location.href);
			}
		});
	}
}
$(document).ready(function() {
	setListCmtFrmHeight();
	setUniformCSS();
});
//-->
</script>

<u:listArea colgroup="94%,6%">
	<tr>
	<td><u:textarea id="cmt" titleId="cols.cmt" maxByte="1000" style="width:98.5%;" rows="2" /></td>
	<td class="listicon_ct"><u:buttonS titleId="cm.btn.reg" alt="등록" onclick="regCmt();"/></td>
	</tr>
</u:listArea>

<u:listArea id="cmtListArea" colgroup="6%,,10%,12%,6%">
	<c:if test="${fn:length(ctCmtDVoList) == 0}">
		<tr>
		<td class="nodata" colspan="5"><u:msg titleId="bb.msg.noCmt" alt="한줄답변이 없습니다." /></td>
		</tr>
	</c:if>
	<c:if test="${fn:length(ctCmtDVoList) > 0}">
	<c:forEach items="${ctCmtDVoList}" var="ctCmtDVo" varStatus="status">
	<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
	<td class="body_ct"><u:out value="${recodeCount - ctCmtDVo.rnum + 1}" type="number" /></td>
	<td class="body_lt"><u:out value="${ctCmtDVo.cmt}" /></td>
	<td class="body_ct"><a href="javascript:window.parent.viewUserPop('${ctCmtDVo.regrUid}');"><u:out value="${ctCmtDVo.regrNm}" /></a></td>
	<td class="body_ct"><u:out value="${ctCmtDVo.regDt}" type="longdate" /></td>
	<td class="listicon_ct">
		<u:buttonS titleId="cm.btn.del" alt="삭제" onclick="delCmt('${ctCmtDVo.cmtId}');" auth="A" ownerUid="${ctCmtDVo.regrUid}" />
		</td>
	</tr>
	</c:forEach>
	</c:if>
</u:listArea>

<u:pagination noTotalCount="true" />