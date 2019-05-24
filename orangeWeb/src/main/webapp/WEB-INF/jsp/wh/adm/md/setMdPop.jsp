<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<!--
<% // [팝업:저장] 테이블 저장 %>
function save() {
	if (validator.validate('setForm')) {
		var param = new ParamMap().getData('setForm');
		callAjax('./transMdAjx.do?menuId=${menuId}', param, function(data) {
			if (data.message != null) {
				alert(data.message);
			}
			if (data.result == 'ok') {
				reloadFrame('mdTree', './treeMdFrm.do?menuId=${menuId}&mdId='+data.mdId);
			}
			dialog.close('setMdDialog');
		});
	}
};
$(document).ready(function() {
});
//-->
</script>
<div style="width:350px;">
<form id="setForm">
<u:input type="hidden" id="menuId" value="${menuId }"/>
<c:if test="${!empty whMdBVo }">
<u:input type="hidden" id="mdId" value="${whMdBVo.mdId }"/>
</c:if>
<c:if test="${!empty param.mdPid }">
<u:input type="hidden" id="mdPid" value="${param.mdPid }"/>
</c:if>

<% // 폼 필드 %>
	<u:listArea id="mdArea" colgroup="25%,">
	<tr>
		<td class="head_lt"><u:mandatory /><u:msg titleId="wh.cols.md.grpNm" alt="모듈그룹명" /></td>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tbody>
				<tr>
					<td id="langTypArea">
						<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:convert srcId="${whMdBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
							<u:set test="${status.first}" var="style" value="width:180px;" elseValue="width:180px; display:none" />
							<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
							<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="wh.cols.md.nm" value="${rescVa}" style="${style}"
								maxByte="120" validator="changeLangSelector('mdArea', id, va)" mandatory="Y" />
						</c:forEach>
						<u:input type="hidden" id="rescId" value="${whMdBVo.rescId}" />
					</td>
					<td id="langTypOptions">
						<c:if test="${fn:length(_langTypCdListByCompId)>1}">
							<select id="langSelector" onchange="changeLangTypCd('mdArea','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
							<u:option value="${langTypCdVo.cd}" title="${langTypCdVo.rescNm}" />
							</c:forEach>
							</select>
						</c:if>
					</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td class="head_lt"><u:msg titleId="cols.useYn" alt="사용여부" /></td>
		<td><select id="useYn" name="useYn" <u:elemTitle titleId="cols.useYn" />>
			<u:option value="Y" titleId="cm.option.use" checkValue="${whMdBVo.useYn}" />
			<u:option value="N" titleId="cm.option.notUse" checkValue="${whMdBVo.useYn}" />
			</select></td>
	</tr>
	<c:if test="${!empty whMdBVo && whMdBVo.mdPid eq 'ROOT' && !empty ptCompBVoList}"><tr>
		<td class="head_lt"><u:msg titleId="cols.comp" alt="회사" /></td>
		<td><select name="paramCompId" <u:elemTitle titleId="cols.comp" /> style="min-width:100px;">
					<c:forEach items="${ptCompBVoList}" var="ptCompBVo" varStatus="status">
						<u:option value="${ptCompBVo.compId}" title="${ptCompBVo.rescNm}" checkValue="${whMdBVo.compId }"/>
					</c:forEach>
				</select></td>
	</tr></c:if>
</u:listArea>
</form>
<u:blank />
<% // 하단 버튼 %>
<u:buttonArea style="clear:both;">
	<u:button titleId="cm.btn.save" onclick="save();" alt="저장" auth="W" />
	<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
</u:buttonArea>
</div>
