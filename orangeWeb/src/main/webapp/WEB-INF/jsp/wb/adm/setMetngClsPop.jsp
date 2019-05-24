<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ include file="/WEB-INF/jsp/cm/inc/cmJspInc.jsp"
%>
<script type="text/javascript">
<% // [팝업:저장] 테이블 저장 %>
function saveCls() {
	if (validator.validate('setMetngClsForm') && confirmMsg("cm.cfrm.save")) {
		var $form = $('#setMetngClsForm');
		$form.attr('method','post');
		$form.attr('action','./transMetngCls.do?menuId=${menuId}');
		$form.attr('target','dataframe');
		$form[0].submit();
	}
};
</script>
<div style="width:400px">
	<form id="setMetngClsForm">
		<u:input type="hidden" id="rescId" value="${wbMetngClsCdBVo.rescId}" />
		<% // 폼 필드 %>
		<u:listArea>
		<tr>
			<td width="32%" class="head_lt"><u:msg titleId="cols.clsNm" alt="분류명" /></td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0">
					<tbody>
					<tr>
						<td id="langTypArea">
							<c:forEach items="${_langTypCdListByCompId}" var="langTypCdVo" varStatus="status">
								<u:convert srcId="${wbMetngClsCdBVo.rescId}_${langTypCdVo.cd}" var="rescVa" />
								<u:set test="${status.first}" var="style" value="width:200px;" elseValue="width:200px; display:none" />
								<u:set test="${fn:length(_langTypCdListByCompId)==1}" var="titlePrefix" value="" elseValue="${langTypCdVo.rescNm}" />
								<u:input id="rescVa_${langTypCdVo.cd}" titlePrefix="${titlePrefix}" titleId="cols.clsNm" value="${rescVa}" style="${style}"
									maxByte="120" validator="changeLangSelector('setMetngClsForm', id, va)" mandatory="Y" />
							</c:forEach>
						</td>
						<td>
							<c:if test="${fn:length(_langTypCdListByCompId)>1}">
								<select id="langSelector" onchange="changeLangTypCd('setMetngClsForm','langTypArea',this.value)" <u:elemTitle titleId="cols.langTyp" />>
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
	</u:listArea>

	<% // 하단 버튼 %>
	<u:buttonArea>
		<u:button titleId="cm.btn.save" onclick="saveCls();" alt="저장" auth="A" />
		<u:button href="javascript:void(0)" onclick="dialog.close(this);" alt="닫기" titleId="cm.btn.close" />
	</u:buttonArea>

	</form>
</div>
