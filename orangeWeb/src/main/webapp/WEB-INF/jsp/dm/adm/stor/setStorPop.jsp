<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%><u:params var="nonPageParams" excludes="storId,pageNo,data,_"/>
<script type="text/javascript">
<!--<% // [팝업:저장] 코드그룹 저장 %>
function saveStor() {
	if (validator.validate('setStorForm')) {
		var $form = $('#setStorForm');
		$form.attr('method','post');
		$form.attr('action','./transStor.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
}
$(document).ready(function() {
});
//-->
</script>

<div style="width:550px">
<form id="setStorForm">
<u:input type="hidden" id="menuId" value="${menuId}" />
<u:input type="hidden" id="storId" value="${dmStorBVo.storId}" />
<u:input type="hidden" id="listPage" value="./listStor.do?${nonPageParams}" />
<c:if test="${!empty param.paramCompId }"><u:input type="hidden" id="paramCompId" value="${param.paramCompId }" /></c:if>
<% // 폼 필드 %>
<u:listArea colgroup="27%,73%">
	<tr>
	<td class="head_lt"><u:mandatory /><u:msg titleId="dm.cols.storNm" alt="저장소명" /></td>
	<td><table cellspacing="0" cellpadding="0" border="0">
		<tr>
		<td id="langTypArea">
		<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:convert srcId="${dmStorBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
			<u:set test="${status.first}" var="style" value="width:150px;" elseValue="width:150px; display:none" />
			<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
			<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="dm.cols.storNm" value="${rescVa}" style="${style}"
				maxByte="120" validator="changeLangSelector('setStorForm', id, va)" mandatory="Y" />
		</c:forEach>
		</td>
		<td>
		<c:if test="${fn:length(_langTypCdListByCompId)>1}">
			<select id="langSelector" onchange="changeLangTypCd('setStorForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
			<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
			<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
			</c:forEach>
			</select>
		</c:if>
		<u:input type="hidden" id="rescId" value="${dmStorBVo.rescId}" />
		</td>
		</tr>
		</table></td>
	</tr>
	<c:if test="${dmStorBVo.dftYn eq 'N' }">
		<tr>
			<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
			<td>
				<u:checkArea>
					<u:radio name="useYn" value="Y" titleId="cm.option.use" checked="${empty dmStorBVo.useYn || dmStorBVo.useYn eq 'Y' ? true : false}" />
					<u:radio name="useYn" value="N" titleId="cm.option.notUse" checked="${dmStorBVo.useYn eq 'N' ? true : false }" />
				</u:checkArea>
			</td>
		</tr>
	</c:if>
	<%-- <tr>
		<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td>
			<u:checkArea>
				<u:radio name="useYn" value="Y" titleId="cm.option.use" checked="${empty dmStorBVo.useYn || dmStorBVo.useYn eq 'Y' ? true : false}" />
				<u:radio name="useYn" value="N" titleId="cm.option.notUse" checked="${dmStorBVo.useYn eq 'N' ? true : false }" />
			</u:checkArea>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="dm.cols.dftYn" alt="기본여부" /></td>
		<td>
			<c:if test="${isDftYn == true }"><u:checkArea>
				<u:radio name="dftYn" value="Y" titleId="cm.option.use" checked="${empty dmStorBVo.dftYn || dmStorBVo.dftYn eq 'Y' ? true : false}" />
				<u:radio name="dftYn" value="N" titleId="cm.option.notUse" checked="${dmStorBVo.dftYn eq 'N' ? true : false }" />
			</u:checkArea></c:if>
			<c:if test="${isDftYn == false }"><u:checkArea>
				<u:radio name="dftYn" value="Y" titleId="cm.option.use" checked="true" />
			</u:checkArea></c:if>
		</td>
	</tr> --%>
</u:listArea>

<u:buttonArea>
	<u:button titleId="cm.btn.save" alt="저장" onclick="saveStor();" auth="A" />
	<u:button titleId="cm.btn.close" alt="닫기" onclick="dialog.close(this);" />
</u:buttonArea>

</form>
</div>