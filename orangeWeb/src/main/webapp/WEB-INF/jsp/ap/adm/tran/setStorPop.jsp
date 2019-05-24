<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:secu
	auth="A"><u:set test="${true}" var="hasAuthA" value="Y" /></u:secu><u:secu
	auth="SYS"><u:set test="${true}" var="hasAuthSYS" value="Y" /></u:secu>
<script type="text/javascript">
function saveStorPop() {
	if (validator.validate('setStorForm')) {
		var $form = $('#setStorForm');
		$form.attr('method','post');
		$form.attr('action','./transStorPop.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}
</script>
<div id="setStorArea" style="width:450px;">
<form id="setStorForm">
<u:listArea  colgroup="30%,70%"><c:if
	test="${not empty hasAuthSYS}">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.storNm" alt="저장소명" /></td>
	<td class="body_lt"><table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convert srcId="${apStorCompRVo.storRescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${status.first}" var="style" value="width:150px;" elseValue="width:150px; display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="dm.cols.storNm" value="${rescVa}" style="${style}"
				maxByte="120" validator="changeLangSelector('setStorArea', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setStorArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${apStorCompRVo.storRescId}" />
		</td>
		</tr>
		</table></td>
</tr></c:if><c:if
	test="${empty hasAuthSYS}">
<tr>
	<td class="head_lt"><u:msg titleId="dm.cols.storNm" alt="저장소명" /></td>
	<td class="body_lt"><u:out value="${apStorCompRVo.storRescNm}"/></td>
</tr></c:if>
<tr>
	<td class="head_lt"><u:msg titleId="ap.jsp.allDoc" alt="전체 문서" /></td>
	<td class="body_lt"><u:out value="${apStorCompRVo.allDocCnt}"/></td>
</tr>
<tr>
	<td class="head_lt"><u:msg titleId="ap.jsp.compDoc" alt="회사 문서" /></td>
	<td class="body_lt"><u:out value="${apStorCompRVo.compDocCnt}"/></td>
</tr><c:if
	test="${not empty hasAuthA}">
<tr>
	<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td class="body_lt">
		<u:checkArea>
			<u:radio name="useYn" value="Y" titleId="cm.option.use" checkValue="${apStorCompRVo.useYn}" />
			<u:radio name="useYn" value="N" titleId="cm.option.notUse" checkValue="${apStorCompRVo.useYn}" />
		</u:checkArea>
	</td>
</tr></c:if><c:if
	test="${empty hasAuthA}">
<tr>
	<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	<td class="body_lt"><c:if
		test="${apStorCompRVo.useYn == 'Y'}"><u:msg titleId="cm.option.use" alt="사용" /></c:if><c:if
		test="${apStorCompRVo.useYn != 'Y'}"><u:msg titleId="cm.option.notUse" alt="사용안함" /></c:if>
	</td>
</tr></c:if>
</u:listArea>
</form>
<u:buttonArea>
<u:button id="transferBtn" titleId="cm.btn.save" alt="저장" onclick="saveStorPop();" auth="A" />
<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>
</div>