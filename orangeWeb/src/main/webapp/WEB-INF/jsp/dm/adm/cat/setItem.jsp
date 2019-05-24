<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<%
//저장 - 버튼 클릭 %>
function save(){
	//var param = new ParamMap().getData("setEnvForm");
	var $form = $("#setForm");
	$form.attr('action','./${transPage}.do');
	$form.attr('target','dataframe');
	$form.submit();
};
$(document).ready(function() {
setUniformCSS();
});
//-->
</script>
<u:title titleId="dm.jsp.setItemMgm" alt="항목관리" menuNameFirst="true"/>
<form id="setForm" method="post">
<input type="hidden" name="menuId" value="${menuId}" />

<c:set var="colgroup" value="15%,35%,15%,35%"/>
<u:listArea colgroup="${colgroup }">
	<tr>
		<td class="head_ct"><u:msg titleId="cols.itemNm" alt="항목명" /></td>
		<td class="head_ct"><u:msg titleId="dm.cols.itemTyp" alt="항목구분" /></td>
		<td class="head_ct"><u:msg titleId="cols.desc" alt="설명" /></td>
		<td class="head_ct"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
	</tr>
	<c:forEach var="list" items="${dmItemBVoList }" varStatus="status">
		<tr onmouseover='this.className="trover"' onmouseout='this.className="trout"'>
			<td><table border="0" cellpadding="0" cellspacing="0"><tbody>
			<tr>
			<td id="langTyp${colmIndex}">
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:convert srcId="${list.rescId}_${langTypCdVo.cd}" var="rescVa" />
				<u:set test="${status.first}" var="style" value="width:100px;" elseValue="width:100px; display:none" />
				<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
				<u:input id="rescVa_${langTypCdVo.cd}${colmIndex}" name="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.colmDispNm" value="${rescVa}" style="${style}"
					maxByte="120" validator="changeLangSelectorColm('setForm', id, va)" mandatory="Y" />
			</c:forEach>
			</td>
			<td>
			<c:if test="${fn:length(_langTypCdListByCompId)>1}">
				<select id="langSelector${colmIndex}" onchange="changeLangTypCd('setForm','langTyp${colmIndex}',this.value)" <u:elemTitle titleId="cols.langTyp" />>
				<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
				<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
				</c:forEach>
				</select>
			</c:if>
			<u:input type="hidden" id="rescId${colmIndex}" name="rescId" value="${list.rescId}" />
			</td>
			</tr>
			</tbody></table></td>
			<td class="body_ct">&nbsp;</td>
			<td class="body_lt">&nbsp;</td>
			<td class="body_ct"><select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
				<u:option value="Y" titleId="cm.option.use" checkValue="${ptMnuGrpBVo.useYn}" />
				<u:option value="N" titleId="cm.option.notUse" checkValue="${ptMnuGrpBVo.useYn}" />
				</select>
			</td>
		</tr>
	</c:forEach>
</u:listArea>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" href="javascript:save();" auth="A" />
</u:buttonArea>
</form>