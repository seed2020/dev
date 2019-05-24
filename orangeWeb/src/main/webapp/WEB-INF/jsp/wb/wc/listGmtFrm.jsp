<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
//선택한 항목 리턴
function getChecked(){
	var $check = $("#listArea input[type='radio']:checked");
	if($check[0] == undefined) {
		alertMsg('cm.msg.noSelect');
		return null;
	}
	var obj = getParentTag($check[0], 'tr');
	var clsCd = $(obj).find("input[id='clsCd']").val();
	var arrs = {clsCd:clsCd,cd:$check.val()};
	return arrs;
}

$(document).ready(function() {
setUniformCSS();
});
</script>
<form id="setRegForm" name="setRegForm" style="padding:10px;" >
	<u:listArea id="listArea" colgroup="30px,*,15%,15%">
	<tr id="headerTr">
		<td class="head_ct" >&nbsp;</td>
		<td class="head_ct">국가명</td>
		<td class="head_ct">단위(분)</td>
		<td class="head_ct">단위(시간)</td>
	</tr>
	<c:choose>
		<c:when test="${!empty ptCdBVoList}">
			<c:forEach var="list" items="${ptCdBVoList}" varStatus="status">
				<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
					<td class="body_ct"><u:radio id="check_${list.cd }" name="gmtCdCheck" value="${list.cd }" checkValue="${param.cd }" checked="${empty param.cd && status.count == 1}"/><input type="hidden" id="clsCd" value="${list.clsCd }"/></td>
					<td class="body_lt">${list.rescNm }</td>
					<td class="body_ct">${list.refVa1 }</td>
					<td class="body_ct">${list.refVa2 }</td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td class="nodata" colspan="4"><u:msg titleId="cm.msg.noData" alt="해당하는 데이터가 없습니다." /></td>
			</tr>
		</c:otherwise>
	</c:choose>
	</u:listArea>
</form>
<u:blank />